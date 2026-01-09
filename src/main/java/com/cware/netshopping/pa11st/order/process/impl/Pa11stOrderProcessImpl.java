package com.cware.netshopping.pa11st.order.process.impl;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.common.util.HttpUtil;
import com.cware.netshopping.domain.Pa11storderlistVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.Pa11stclaimlistVO;
import com.cware.netshopping.pa11st.claim.repository.Pa11stClaimDAO;
import com.cware.netshopping.pa11st.order.process.Pa11stOrderProcess;
import com.cware.netshopping.pa11st.order.repository.Pa11stOrderDAO;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;

@Service("pa11st.order.pa11stOrderProcess")
public class Pa11stOrderProcessImpl extends AbstractService implements Pa11stOrderProcess{

	@Resource(name = "pa11st.order.pa11stOrderDAO")
	private Pa11stOrderDAO pa11stOrderDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "common.system.systemDAO")
	private SystemDAO systemDAO;

	@Resource(name = "pa11st.claim.pa11stClaimDAO")
	private Pa11stClaimDAO pa11stClaimDAO;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	
	/**
	 * 11번가 판매불가처리 - 처리결과 update
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String saveOrderRejectProc(ParamMap paramMap) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			executedRtn = pa11stOrderDAO.updatePaOrdermPreCancel(paramMap); //= tpaorderm update
			
			if(executedRtn == 1){
				log.info("tpaorderm update success");
			} else {
				log.info("tpaorderm update fail");
				log.info("ordNo : " + paramMap.getString("ordNo") + ", ordPrdSeq : " + paramMap.getString("ordPrdSeq"));
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
			
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		
		return rtnMsg;
	}
	
	/**
	 * 11번가 취소신청목록조회
	 * @param List<Pa11storderlist>
	 * @return String
	 * @throws Exception
	 */
	public String saveCancelList(List<Pa11storderlistVO> arrPa11storderlist) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		for(int i = 0; arrPa11storderlist.size() > i; i++){
			
			int checkCnt = pa11stOrderDAO.selectCancelPa11stOrderListExists(arrPa11storderlist.get(i));
			if(checkCnt == 1){
				continue;
			}
			
			//= TPA11STORDERLIST INSERT
			executedRtn = pa11stOrderDAO.insertCancelPa11stOrderList(arrPa11storderlist.get(i));
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPA11STORDERLIST INSERT" });
			}
			
		}
		return rtnMsg;
	}
	
	/**
	 * 11번가 취소처리대상조회 
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	@Override
	public List<Object> selectPa11stOrdCancelList() throws Exception{
		return pa11stOrderDAO.selectPa11stOrdCancelList();
	}
	
	/**
	 * 11번가 주문취소승인/주문취소거부
	 * @param HashMap<String, Object>
	 * @return ParamMap
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ParamMap saveCancelConfirmProc(HashMap<String, Object> cancelMap) throws Exception{
		HttpURLConnection conn = null;
		String requestType = "GET";
		String parameter = "";
		Document doc = null;
		NodeList descNodes = null;
		ParamMap resultMap = new ParamMap();
		Paorderm paorderm = null;
		int executedRtn = 0;
		int targetCnt = 1;
		int successCnt = 0;
		int failCnt = 0;
		int businessDay = 0;
		String sendDate = DateUtil.getCurrentDateAsString();
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		//취소승인대상 - DO_FLAG(20) 상태면 취소승인처리 API 호출
		//취소거부대상 - DO_FLAG(30) 이후 운송장번호 존재하는 경우 : 취소거부처리  API 호출
		//반품대상     - DO_FLAG(30) 이후 운송장번호 없는 경우 취소신청 후 1영업일제(24시간 경과) 된 건은 취소승인처리  API 호출 후 반품처리
		//          (택배사 방식에 따라 직택배는 반품처리, 굿스플로 및 업체배송은 전체취소 건인 경우 출고전반품처리, 부분취소인 경우 반품처리)
		int doFlag = Integer.parseInt(cancelMap.get("DO_FLAG").toString());
		
		if(doFlag < 30){//=1. 취소승인대상
			try{
				parameter = "/"+cancelMap.get("ORD_PRD_CN_SEQ")+"/"+cancelMap.get("PA_ORDER_NO")+"/"+cancelMap.get("PA_ORDER_SEQ");
				
				log.info("주문취소 승인처리 API 호출");
				conn = ComUtil.pa11stConnectionSetting((HashMap<String, String>) cancelMap.get("approvalApiInfo"), cancelMap.get("PA_API_KEY").toString(), requestType, parameter);
				
				// RESPONSE XML 			
				doc = ComUtil.parseXML(conn.getInputStream());
				descNodes = doc.getElementsByTagName("ResultOrder");
				
				conn.disconnect();
				
				for(int j=0; j<descNodes.getLength();j++){
					for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
						resultMap.put(node.getNodeName().trim(), node.getTextContent().trim());
					}
				}
				
				log.info("주문취소 승인처리 결과 처리");
				if( resultMap.getString("result_code").equals("0") ){
					cancelMap.put("PROC_FLAG","10");
					successCnt++;
				} else {
					//11ST API 연결 실패
					cancelMap.put("PROC_FLAG","90");
					failCnt++;
				}
				
				if(resultMap.getString("result_code").equals("0")){
					paorderm = new Paorderm();
					paorderm.setPaCode(cancelMap.get("PA_CODE").toString());
					paorderm.setPaOrderGb(cancelMap.get("PA_ORDER_GB").toString());
					paorderm.setPaOrderNo(cancelMap.get("PA_ORDER_NO").toString());
					paorderm.setPaOrderSeq(cancelMap.get("PA_ORDER_SEQ").toString());
					paorderm.setPaShipNo(cancelMap.get("DLV_NO").toString());
					paorderm.setPaClaimNo(cancelMap.get("ORD_PRD_CN_SEQ").toString());
					paorderm.setPaProcQty(cancelMap.get("ORD_CN_QTY").toString());
					paorderm.setPaDoFlag("20");
					paorderm.setOutBefClaimGb("0");
					paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
					paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
					paorderm.setModifyId("PA11");
					//= TPAORDERM INSERT
					//= 취소승인처리 결과 INSERT
					executedRtn = paCommonDAO.insertPaOrderM(paorderm);
					if (executedRtn != 1) {
						throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
					}
				}
				
			}catch (Exception e) {
				log.info(getMessage("errors.process") + " : IF_PA11STAPI_03_008 fail ["+cancelMap.get("PA_ORDER_NO")+"/"+cancelMap.get("PA_ORDER_SEQ")+"]");
				resultMap.put("result_code", "-9999");
				resultMap.put("result_text", e.getMessage());
				cancelMap.put("PROC_FLAG","90");
				failCnt++;
				
			}finally{
				//= TPA11STORDERLIST UPDATE
				//= 호출결과 PROC_FLAG UPDATE 진행단계 [10 : 승인, 20 : 거부, 90: 처리실패]
				executedRtn = pa11stOrderDAO.updatePa11stOrderListProcFlag(cancelMap);
				if (executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] { "TPA11STORDERLIST UPDATE" });
				}
			}
			
		}else if(doFlag >= 30 && !cancelMap.get("SLIP_NO").toString().equals("")){ //=2. 취소거부 대상
			try{
				//= 11번가 주문취소거부처리(배송번호 기준으로 주문취소거부 처리가 되므로 이미 처리된 건에 대해서 중복체크) 
				int cancelRefusalCnt = pa11stOrderDAO.selectCancelRefusalExists(cancelMap);
				
				if(cancelRefusalCnt > 0){	
					
					parameter = "/"+cancelMap.get("PA_ORDER_NO")+"/"+cancelMap.get("PA_ORDER_SEQ")+"/"+cancelMap.get("ORD_PRD_CN_SEQ")+"/01/"+sendDate+"/"+cancelMap.get("PA_DELY_GB")+"/"+cancelMap.get("SLIP_NO");
					
					log.info("주문취소 거부처리 API 호출");
					conn = ComUtil.pa11stConnectionSetting((HashMap<String, String>) cancelMap.get("refusalApiInfo"), cancelMap.get("PA_API_KEY").toString(), requestType, parameter);
					
					// RESPONSE XML 			
					doc = ComUtil.parseXML(conn.getInputStream());
					descNodes = doc.getElementsByTagName("ResultOrder");
					
					conn.disconnect();
					
					for(int j=0; j<descNodes.getLength();j++){
						for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
							resultMap.put(node.getNodeName().trim(), node.getTextContent().trim());
						}
					}
					
					log.info("주문취소 거부처리 결과 처리");
					if( resultMap.getString("result_code").equals("0") ){
						cancelMap.put("PROC_FLAG","20");
						
						//= TPAORDERM UPDATE
						//= 주문취소거부 처리하면 11번가 발송처리 되므로 원주문건 PA_DO_FLAG = 40으로 UPDATE
						cancelMap.put("PA_DO_FLAG","40");
						cancelMap.put("API_RESULT_CODE","0");
						cancelMap.put("API_RESULT_MESSAGE","주문취소거부 처리로 인해 발송처리 되었습니다.");
						executedRtn = pa11stOrderDAO.updatePaOrdermCancelRefusal(cancelMap);
						if (executedRtn < 1) {
							throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
						}
						successCnt++;
					} else {
						//11ST API 연결 실패
						cancelMap.put("PROC_FLAG","90");
						failCnt++;
					}
				}else {
					successCnt++;
				}
				
			}catch (Exception e) {
				log.info(getMessage("errors.process") + " : IF_PA11STAPI_03_008 fail ["+cancelMap.get("PA_ORDER_NO")+"/"+cancelMap.get("PA_ORDER_SEQ")+"]");
				resultMap.put("result_code", "-9999");
				resultMap.put("result_text", e.getMessage());
				cancelMap.put("PROC_FLAG","90");
				failCnt++;
				
			}finally{
				if(!resultMap.getString("result_code").equals("")){
					//= TPA11STORDERLIST UPDATE
					//= 호출결과 PROC_FLAG UPDATE 진행단계 [10 : 승인, 20 : 거부, 90: 처리실패]
					executedRtn = pa11stOrderDAO.updatePa11stOrderListRefusalProcFlag(cancelMap);
					if (executedRtn < 1) {
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
					}
				}
			}
		}else {
			try{
				//= 11번가 취소신청 후 65시간 해당되는지 체크
				businessDay = pa11stOrderDAO.selectBusinessDayAccount(cancelMap);
				if(businessDay >= ConfigUtil.getInt("CANCEL_BUSINESS_HOUR")){
					parameter = "/"+cancelMap.get("ORD_PRD_CN_SEQ")+"/"+cancelMap.get("PA_ORDER_NO")+"/"+cancelMap.get("PA_ORDER_SEQ");
					
					log.info("주문취소 승인처리 API 호출");
					conn = ComUtil.pa11stConnectionSetting((HashMap<String, String>) cancelMap.get("approvalApiInfo"), cancelMap.get("PA_API_KEY").toString(), requestType, parameter);
					
					// RESPONSE XML 			
					doc = ComUtil.parseXML(conn.getInputStream());
					descNodes = doc.getElementsByTagName("ResultOrder");
					
					conn.disconnect();
					
					for(int j=0; j<descNodes.getLength();j++){
						for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
							resultMap.put(node.getNodeName().trim(), node.getTextContent().trim());
						}
					}
					
					log.info("주문취소 승인처리 결과 처리");
					if( resultMap.getString("result_code").equals("0") ){
						cancelMap.put("PROC_FLAG","10");
					} else {
						//11ST API 연결 실패
						cancelMap.put("PROC_FLAG","90");
						failCnt++;
					}
					if(resultMap.getString("result_code").equals("0")){
						paorderm = new Paorderm();
						paorderm.setPaCode(cancelMap.get("PA_CODE").toString());
						paorderm.setPaOrderGb(cancelMap.get("PA_ORDER_GB").toString());
						paorderm.setPaOrderNo(cancelMap.get("PA_ORDER_NO").toString());
						paorderm.setPaOrderSeq(cancelMap.get("PA_ORDER_SEQ").toString());
						paorderm.setPaShipNo(cancelMap.get("DLV_NO").toString());
						paorderm.setPaClaimNo(cancelMap.get("ORD_PRD_CN_SEQ").toString());
						paorderm.setPaProcQty(cancelMap.get("ORD_CN_QTY").toString());
						paorderm.setPaDoFlag("20");
						paorderm.setOutBefClaimGb("1");
						paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
						paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
						paorderm.setModifyId("PA11");
						//= TPAORDERM INSERT
						//= 취소승인처리 결과 INSERT
						executedRtn = paCommonDAO.insertPaOrderM(paorderm);
						if (executedRtn != 1) {
							throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
						}
						
						successCnt++;
					}
				}
			}catch (Exception e) {
				log.info(getMessage("errors.process") + " : IF_PA11STAPI_03_008 fail ["+cancelMap.get("PA_ORDER_NO")+"/"+cancelMap.get("PA_ORDER_SEQ")+"]");
				resultMap.put("result_code", "-9999");
				resultMap.put("result_text", e.getMessage());
				cancelMap.put("PROC_FLAG","90");
				failCnt++;
				
			}finally{
				if(!resultMap.getString("result_code").equals("")){
					//= TPA11STORDERLIST UPDATE
					//= 호출결과 PROC_FLAG UPDATE 진행단계 [10 : 승인, 20 : 거부, 90: 처리실패]
					executedRtn = pa11stOrderDAO.updatePa11stOrderListProcFlag(cancelMap);
					if (executedRtn != 1) {
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
					}
				}
				if(businessDay < 66){
					successCnt++;
				}
			}
		}
		//= 대상건수와 성공건수가 다를 경우 TAPITRACKING.API_RESULT_MSG에 저장.
		if(targetCnt == successCnt){
			resultMap.put("rtnMsg",Constants.SAVE_SUCCESS);
			resultMap.put("successCnt",successCnt);
		} else {
			resultMap.put("rtnMsg",Constants.SAVE_FAIL );
			resultMap.put("failCnt",failCnt);
		}
		
		return resultMap;
	}
	
	/**
	 * 11번가 취소철회목록 조회 - 저장
	 * @param List<Pa11storderlistVO>
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String saveCancelWithdrawList(List<Pa11storderlistVO> arrPa11storderlist) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		int targetCnt = arrPa11storderlist.size(); 	//= 대상 건수
		int procCnt = 0;							//= 처리건수
		int preProcCnt = 0;							//= 기처리된 건수
		HashMap<String, Object> existsMap = new HashMap<>();
		
		for(int i = 0; arrPa11storderlist.size() > i; i++){
			
			existsMap = pa11stOrderDAO.selectCancelWithdrawExists(arrPa11storderlist.get(i));
			
			if(existsMap == null) {
				preProcCnt ++;
				continue;
			} else {
				if(existsMap.get("WITHDRAW_YN").equals("1")){
					preProcCnt ++;
					continue;
				}
			}
			
			//= TPA11STORDERLIST UPDATE - 취소철회건 저장
			executedRtn = pa11stOrderDAO.updateWithdrawYn(arrPa11storderlist.get(i));
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPA11STORDERLIST.WITHDRAW_YN INSERT" });
			}
			procCnt++;
			
		}
		
		rtnMsg = "대상건수:" + Integer.toString(targetCnt) + " " + "처리건수:" + Integer.toString(procCnt)+ " " + "기처리 건수:" + Integer.toString(preProcCnt);
		
		return rtnMsg;
	}
	
	/**
	 * 11번가 취소완료목록 조회 - 저장
	 * @param List<Pa11storderlistVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveCancelCompleteList(List<Pa11storderlistVO> arrPa11storderlist) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		String procFlag = "";
		String doFlag = "";
		String outBefClaimGb = "";
		Paorderm paorderm = null;
		
		for(int i = 0; arrPa11storderlist.size() > i; i++){
			int existsCount = 0;
			String delyType = pa11stOrderDAO.selectDelyType(String.valueOf(arrPa11storderlist.get(i).getPrdNo()));
			procFlag = pa11stOrderDAO.selectPa11stOrderListProcFlag(arrPa11storderlist.get(i));
			doFlag = pa11stOrderDAO.selectOrderdtDoFlag(arrPa11storderlist.get(i));

			if( doFlag != null && Integer.parseInt(doFlag) >= 30 && "10".equals(delyType)) { //당사상품 & 출하지시 이후의 경우 반품데이터로 적재
				Pa11stclaimlistVO pa11stclaimlist = settingClaimlist(arrPa11storderlist.get(i));
				
				existsCount = pa11stClaimDAO.selectPa11stClaimListExists(pa11stclaimlist);
				if(existsCount > 0 || "10".equals(procFlag)) continue; //= 이미 수집된 데이터, 승인된 취소건 skip.
				
				if("00".equals(procFlag)) { //취소요청건 withdrawYn = 1 update
					pa11stOrderDAO.updateWithdrawYn(arrPa11storderlist.get(i));
				}
				
				//= 1. TPA11STCLAIMLIST INSERT
				executedRtn = pa11stClaimDAO.insertPa11stClaimList(pa11stclaimlist);
				if (executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] { "TPA11STCLAIMLIST INSERT" });
				}
				
			}else {
				existsCount = pa11stOrderDAO.countOrderList(arrPa11storderlist.get(i));
				
				if(procFlag != null && procFlag.equals("10") || existsCount < 1){ //= 이미 승인된 취소건, 원주문 데이터 미인입건 skip.
					continue;
				}
				
				//= 1. TPA11STORDERLIST UPDATE or INSERT
				executedRtn = pa11stOrderDAO.mergePa11stOrderList(arrPa11storderlist.get(i));
				if (executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] { "TPA11STORDERLIST MERGE" });
				}
			}
			
			
			//= 2. TPAORDERM INSERT
			if(doFlag != null && Integer.parseInt(doFlag) >= 30 && !"10".equals(delyType) ){ //= 원주문건의 do_flag가  30이상일 경우 : outBefClaimGb = 1
				outBefClaimGb = "1";
			} else {
				outBefClaimGb = "0";
			}
			
			paorderm = new Paorderm();
			paorderm.setPaCode(arrPa11storderlist.get(i).getPaCode());
	        if(doFlag != null && (Integer.parseInt(doFlag)  >= 30) && "10".equals(delyType)) {
				paorderm.setPaOrderGb("30");
				paorderm.setPaDoFlag("60");
	        }else {
	        	paorderm.setPaOrderGb("20");
				paorderm.setPaDoFlag("20");
	        }
			paorderm.setPaOrderNo(arrPa11storderlist.get(i).getOrdNo());
			paorderm.setPaOrderSeq(Long.toString(arrPa11storderlist.get(i).getOrdPrdSeq()));
			paorderm.setPaShipNo(arrPa11storderlist.get(i).getDlvNo());
			paorderm.setPaClaimNo(Long.toString(arrPa11storderlist.get(i).getOrdPrdCnSeq()));
			paorderm.setPaProcQty(Long.toString(arrPa11storderlist.get(i).getOrdCnQty()));
			paorderm.setOutBefClaimGb(outBefClaimGb);
			paorderm.setInsertDate(arrPa11storderlist.get(i).getInsertDate());
			paorderm.setModifyDate(arrPa11storderlist.get(i).getModifyDate());
			paorderm.setModifyId("PA11");
			
			int cnt = pa11stOrderDAO.selectPaOrderM(paorderm);
			
			if (cnt < 1) {
				executedRtn = paCommonDAO.insertPaOrderM(paorderm);
				if (executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}
			}
			
			
		}
		return rtnMsg;
	}
	
	private Pa11stclaimlistVO settingClaimlist(Pa11storderlistVO pa11storderlist) throws Exception {
		String dateTime = systemService.getSysdatetimeToString();

		Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);

		Pa11stclaimlistVO pa11stclaimlist = null;
		Pa11storderlistVO orgPa11storderlist = null;
		orgPa11storderlist = pa11stOrderDAO.selectPa11stOrderList(pa11storderlist);
		
		pa11stclaimlist = new Pa11stclaimlistVO();
		pa11stclaimlist.setPaCode(pa11storderlist.getPaCode());
		pa11stclaimlist.setOrdNo(pa11storderlist.getOrdNo());
		pa11stclaimlist.setOrdPrdSeq(String.valueOf(pa11storderlist.getOrdPrdSeq()));
		pa11stclaimlist.setClmReqSeq(String.valueOf(pa11storderlist.getOrdPrdCnSeq()));
		pa11stclaimlist.setPaOrderGb("30");
		pa11stclaimlist.setClmReqCont("11번가 취소(당사)");
		pa11stclaimlist.setClmReqQty((int)pa11storderlist.getOrdCnQty());
		pa11stclaimlist.setClmReqRsn(pa11storderlist.getOrdCnRsnCd());
		pa11stclaimlist.setOptName(pa11storderlist.getSlctPrdOptNm());
		pa11stclaimlist.setReqDt(pa11storderlist.getCreateDt());
		pa11stclaimlist.setOrdNm(orgPa11storderlist.getOrdNm());
		pa11stclaimlist.setOrdTlphnNo(orgPa11storderlist.getRcvrTlphn());
		pa11stclaimlist.setOrdPrtblTel(orgPa11storderlist.getRcvrPrtblNo());
		pa11stclaimlist.setRcvrMailNo(orgPa11storderlist.getRcvrMailNo());
		pa11stclaimlist.setRcvrMailNoSeq(orgPa11storderlist.getRcvrMailNoSeq());
		pa11stclaimlist.setRcvrBaseAddr(orgPa11storderlist.getRcvrBaseAddr());
		pa11stclaimlist.setRcvrDtlsAddr(orgPa11storderlist.getRcvrDtlsAddr());
		pa11stclaimlist.setRcvrTypeAdd(orgPa11storderlist.getTypeAdd());
		pa11stclaimlist.setClmLstDlvCst(0);
		pa11stclaimlist.setInsertDate(sysdateTime);
		pa11stclaimlist.setModifyDate(sysdateTime);
		return pa11stclaimlist;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String saveDeliveryReqOutCancel(HashMap<String, Object> orderMap) throws Exception{
		HashMap cancelMap = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		String slipINo = "";
		
		cancelMap = (HashMap)pa11stOrderDAO.selectDeliveryReqOutCancelTarget(orderMap);
			
		if(cancelMap != null ){ // 출고전반품대상
			cancelMap.put("MODIFY_ID", "PA11");
			cancelMap.put("MODIFY_DATE", sysdate);
			executedRtn = pa11stOrderDAO.updatedirectClaimdt(cancelMap);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] { "TCLAIMDT update" });
			}
		
			// = TORDERGOODS update
			if (cancelMap.get("ORDER_D_SEQ").toString().equals("001")
					&& (!cancelMap.get("GOODS_GB").toString().equals("30"))) {
				executedRtn = pa11stOrderDAO.updateTordergoods(cancelMap);
				if (executedRtn < 1) {
					throw processException("msg.cannot_save",
							new String[] { "TORDERGOODS update" });
				}
			}

			slipINo = pa11stOrderDAO.selectSlipINoSequence();
			
			// = TSLIPM insert
			cancelMap.put("NEW_SLIP_I_NO", slipINo);
			cancelMap.put("LAST_PROC_ID", "PA11");
			cancelMap.put("RETURN_QC_CODE", "100");
			executedRtn = pa11stOrderDAO.insertCancelSlipm(cancelMap);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] { "TSLIPM insert" });
			}
			
			// = TSLIPDT insert
			executedRtn = pa11stOrderDAO.insertClaimSlipdt(cancelMap);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] { "TSLIPDT insert" });
			}

			// = TORDERSTOCK update
			executedRtn = pa11stOrderDAO.updateOrderstock(cancelMap);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] { "TORDERSTOCK update" });
			}
			
			// = 직매입타창고인경우 TSLIPCREATE update
			if (cancelMap.get("WH_CODE").toString().equals("980") || cancelMap.get("WH_CODE").toString().equals("981") || cancelMap.get("STOCK_CHECK_YN").toString().equals("1")) {
				executedRtn = pa11stOrderDAO.updateSlipcreate40(cancelMap);
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TSLIPCREATE update" });
				}
			}
			
			// = 기존 출하지시건을 출고확정 상태로 변경
			// = tslipm update
			executedRtn = pa11stOrderDAO.updateSlipM(cancelMap);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] { "TSLIPM update" });
			}
			
			// 주문수량, 배송완료수량 같을 경우 배송완료처리
			executedRtn = pa11stOrderDAO.chkSyslastDelyQty(cancelMap);
			if(executedRtn == 1){
				// = 배송상태를 배송완료로 변경
				executedRtn = pa11stOrderDAO.updateOrderDt(cancelMap);
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TORDERDT update" });
				}
			}
			// 분리포장일 때 배송(SLIP) 중 한건은 출고, 한건은 출하지시이면 주문(ORDER) 상태는 출하지시 상태임
			// 이때 출하지시 건을 출고전반품처리 하면 배송(SLIP)은 출고이지만 주문(ORDER)은 출하지시상태로 남게 됨
			// 따라서 주문(ORDER)의 상태를 배송(SLIP)과 동기화 시켜 줌
			else{
				executedRtn = pa11stOrderDAO.chkSlipProcMin(cancelMap);
				if(executedRtn == 1){
					executedRtn = pa11stOrderDAO.updateOrderSlipDoFlagSync(cancelMap);
					if (executedRtn < 1) {
						throw processException("msg.cannot_save", new String[] { "TORDERDT update" });
					}
				}
			}
			
			if(cancelMap.get("DELY_TYPE").toString().equals("GoodsFlow")){ // 굿스플로 이용 업체
				// 굿스플로 출고전반품 API 호출
			}
		}
		return rtnMsg;
	}
	
	/**
	 * 11번가 주문생성 대상 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderInputTargetList() throws Exception{
		return pa11stOrderDAO.selectOrderInputTargetList();
	}
	
	/**
	 * 11번가 주문생성 대상 조회
	 * @param String
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderInputTargetDtList(HashMap<String, String> orderMap) throws Exception{
		return pa11stOrderDAO.selectOrderInputTargetDtList(orderMap);
	}
	
	/**
	 * 판매불가처리 API 호출 - IF_PA11STAPI_03_004
	 * 
	 * @param ParamMap
	 * @return ParamMap
	 * @throws Exception
	 */
	public ParamMap reqSaleRefusalProc(HashMap<String, String> reqMap) throws Exception {
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		ParamMap configMap = null;
		Properties params = null;
		
		configMap = new ParamMap();
		configMap.put("apiCode", "IF_PA11STAPI_03_004");
		
		apiInfo = systemDAO.selectPaApiInfo(configMap);
		
		params = new Properties();
		params.setProperty("ordNo",			reqMap.get("PA_ORDER_NO").toString());
		params.setProperty("ordPrdSeq",		reqMap.get("PA_ORDER_SEQ").toString());
		params.setProperty("ordCnRsnCd", 	reqMap.get("ordCnRsnCd").toString());
		params.setProperty("ordCnDtlsRsn", 	URLEncoder.encode(reqMap.get("ordCnDtlsRsn").toString(), "UTF-8"));
		params.setProperty("paCode", 		reqMap.get("paCode").toString());
		
		configMap.put("HTTP_CONFIG_ADDRESS",		ConfigUtil.getString("OPEN_API_BASE_ADDRESS"));
		configMap.put("HTTP_CONFIG_PROTOCOL",		ConfigUtil.getString("OPEN_API_BASE_PROTOCOL"));
		configMap.put("HTTP_CONFIG_PORT",			ConfigUtil.getString("OPEN_API_BASE_PORT"));
		configMap.put("HTTP_CONFIG_TIME_OUT", 		5000);
		configMap.put("HTTP_CONFIG_ADDRESS_DETAIL", apiInfo.get("INTERNAL_URL"));
		configMap.put("HTTP_CONFIG_CONTENT_TYPE",	"application/x-www-form-urlencoded;charset=utf-8");
		
		configMap = HttpUtil.getGetHttpClient(configMap, params);		

		return configMap;
	}
	
	/**
	 * 11번가 주문취소 대상 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectCancelInputTargetList() throws Exception{
		return pa11stOrderDAO.selectCancelInputTargetList();
	}
	
	/**
	 * 11번가 주문생성 결과처리 - 판매불가정보 parameter 조회.
	 * @param String
	 * @return HashMap<String, String>
	 * @throws Exception
	 */
	public HashMap<String, String> selectRefusalInfo(String mappingSeq) throws Exception {
		return pa11stOrderDAO.selectRefusalInfo(mappingSeq);
	}
	
	/**
	 * 11번가 주문취소 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return pa11stOrderDAO.selectCancelInputTargetDtList(paramMap);
	}
	
	/**
	 * 11번가 주문취소 처리 - 기취소
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception {
		return pa11stOrderDAO.updatePreCancelYn(preCancelMap);
	}

	@Override
	public HashMap<String, Object> selectPa11stOrdCancelApprovalList(ParamMap cancelMap) throws Exception {
		return pa11stOrderDAO.selectPa11stOrdCancelApprovalList(cancelMap);
	}

	@Override
	public String saveCancelApprovalProc(HashMap<String, Object> cancelMap) throws Exception {
		Paorderm paorderm = null;
		int executedRtn = 0;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		String rtnMsg = Constants.SAVE_SUCCESS;

		try{

			if(cancelMap.get("apiResultCode").equals("0")){
				paorderm = new Paorderm();
				paorderm.setPaCode(cancelMap.get("PA_CODE").toString());
				paorderm.setPaOrderGb(cancelMap.get("PA_ORDER_GB").toString());
				paorderm.setPaOrderNo(cancelMap.get("PA_ORDER_NO").toString());
				paorderm.setPaOrderSeq(cancelMap.get("PA_ORDER_SEQ").toString());
				paorderm.setPaShipNo(cancelMap.get("DLV_NO").toString());
				paorderm.setPaClaimNo(cancelMap.get("ORD_PRD_CN_SEQ").toString());
				paorderm.setPaProcQty(cancelMap.get("ORD_CN_QTY").toString());
				paorderm.setPaDoFlag("20");
				paorderm.setOutBefClaimGb("1");
				paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
				paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
				paorderm.setModifyId("PA11");
				//= TPAORDERM INSERT
				//= 취소승인처리 결과 INSERT
				executedRtn = paCommonDAO.insertPaOrderM(paorderm);
				if (executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}
				/*if (executedRtn > 0) {
					executedRtn = paCommonDAO.updatePaOrderM(paorderm);
					if (executedRtn < 0) {
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
					}
				}*/
			}
		}catch (Exception e) {
		    log.info(e.getMessage());
		}finally{
			
		}
		return rtnMsg;
	}
	
	/**
	 * 11번가 주문생성이전취소 - 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectCancelWithoutOrderList(ParamMap paramMap) throws Exception {
		return pa11stOrderDAO.selectCancelWithoutOrderList(paramMap);
	}
	
	/**
	 * 11번가 주문생성이전취소 - TPAORDERM update
	 * @param List<Object>
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePreCancelReason(Paorderm paorderm) throws Exception {
		return pa11stOrderDAO.updatePreCancelReason(paorderm);
	}
	
	/**
	 * 11번가 프로모션 조회 - 조회
	 * @param HashMap<String, String>
	 * @return OrderpromoVO
	 * @throws Exception
	 */
	@Override
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception {
		return pa11stOrderDAO.selectOrderPromo(orderMap);
	}
	
	
	/**
	 * 11번가 프로모션 조회 - 조회
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
	 * @param HashMap<String, String>
	 * @return OrderpromoVO
	 * @throws Exception
	 */
	@Override
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception {
		return pa11stOrderDAO.selectOrderPaPromo(orderMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception {
		return pa11stOrderDAO.selectPaMobileOrderAutoCancelList(paramMap);

	}
}