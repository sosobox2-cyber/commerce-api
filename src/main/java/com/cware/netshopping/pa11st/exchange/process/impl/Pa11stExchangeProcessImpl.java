package com.cware.netshopping.pa11st.exchange.process.impl;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
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
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.common.util.HttpUtil;
import com.cware.netshopping.domain.Pa11stclaimlistVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pa11st.claim.repository.Pa11stClaimDAO;
import com.cware.netshopping.pa11st.exchange.process.Pa11stExchangeProcess;
import com.cware.netshopping.pa11st.exchange.repository.Pa11stExchangeDAO;
import com.cware.netshopping.pa11st.order.repository.Pa11stOrderDAO;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;

@Service("pa11st.exchange.pa11stExchangeProcess")
public class Pa11stExchangeProcessImpl extends AbstractService implements Pa11stExchangeProcess{

	@Resource(name = "pa11st.exchange.pa11stExchangeDAO")
	private Pa11stExchangeDAO pa11stExchangeDAO;
	
	@Resource(name = "pa11st.claim.pa11stClaimDAO")
	private Pa11stClaimDAO pa11stClaimDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "pa11st.order.pa11stOrderDAO")
	private Pa11stOrderDAO pa11stOrderDAO;
	
	@Resource(name = "common.system.systemDAO")
	private SystemDAO systemDAO;
	
	/**
	 * 교환신청목록조회(교환 요청 목록조회)
	 * @param List<Pa11stclaimlistVO>
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String saveExchangeList(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		Paorderm paorderm = null;
		List<Object> goodsInfoList = null;
		HashMap<String, Object> goodsInfoMap = null;
		
		int existsCnt = 0;
		int executedRtn = 0;
		
		for(int i = 0; arrPa11stclaimlist.size() > i; i++){
			//= 1. 데이터 중복 CHECK.
			existsCnt = pa11stClaimDAO.selectPa11stClaimListExists(arrPa11stclaimlist.get(i));
			if(existsCnt > 0) continue; //= 이미 수집된 데이터 일 경우 skip.
			
			//= 2. TPA11STCLAIMLIST INSERT
			executedRtn = pa11stClaimDAO.insertPa11stClaimList(arrPa11stclaimlist.get(i));
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPA11STCLAIMLIST INSERT" });
			}
			
			goodsInfoList = pa11stExchangeDAO.selectGoodsdtInfo(arrPa11stclaimlist.get(i)); //= 교환대상 상품의 연동된 단품 갯수 및 단품코드 조회
			// 제휴OUT상품연동 REQ_PRM_041 : S
			if( goodsInfoList.size() == 0 ) {
				goodsInfoList = pa11stExchangeDAO.selectDealGoodsdtInfo(arrPa11stclaimlist.get(i)); //= 교환대상 상품의 연동된 단품 갯수 및 단품코드 조회
			}
			// 제휴OUT상품연동 REQ_PRM_041 : E
			
			goodsInfoMap = new HashMap<>();
			goodsInfoMap = (HashMap<String, Object>) goodsInfoList.get(0);
			
			for(int j = 0; j < 2; j++){
				//= 3. TPAORDERM INSERT
				paorderm = new Paorderm();
				
				if(goodsInfoList.size() == 1 ){ //= 연동된 단품이 1개일 경우.
					paorderm.setChangeGoodsdtCode(goodsInfoMap.get("GOODSDT_CODE").toString());
					paorderm.setChangeFlag("01");
				} else { //= 연동된 단품이 N개일 경우.
					paorderm.setChangeGoodsdtCode("");
					paorderm.setChangeFlag("00");
				}
				paorderm.setChangeGoodsCode(goodsInfoMap.get("GOODS_CODE").toString());
				
				paorderm.setPaCode(arrPa11stclaimlist.get(i).getPaCode());
				if(j == 0){
					paorderm.setPaOrderGb(arrPa11stclaimlist.get(i).getPaOrderGb());
				}else {
					paorderm.setPaOrderGb("45");
				}
				paorderm.setPaOrderNo(arrPa11stclaimlist.get(i).getOrdNo());
				paorderm.setPaOrderSeq(arrPa11stclaimlist.get(i).getOrdPrdSeq());
				paorderm.setPaClaimNo(arrPa11stclaimlist.get(i).getClmReqSeq());
				paorderm.setPaProcQty(Integer.toString(arrPa11stclaimlist.get(i).getClmReqQty()));
				paorderm.setPaDoFlag("20");
				paorderm.setOutBefClaimGb("0");
				paorderm.setInsertDate(arrPa11stclaimlist.get(i).getInsertDate());
				paorderm.setModifyDate(arrPa11stclaimlist.get(i).getModifyDate());
				paorderm.setModifyId("PA11");
				
				executedRtn = paCommonDAO.insertPaOrderM(paorderm);
				if (executedRtn < 0) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}
			}
			
		}
		return rtnMsg;
	}
	
	/**
	 * 11번가 교환승인처리 - 교환출고 대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectExchangeSlipList() throws Exception{
		return pa11stExchangeDAO.selectExchangeSlipList();
	}
	
	/**
	 * 11번가 교환승인처리 - 교환승인처리
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ParamMap saveExchangeConfirmProc(HashMap<String, Object> returnMap) throws Exception{
		HttpURLConnection conn = null;
		String requestType = "GET";
		String parameter = "";
		Document doc = null;
		NodeList descNodes = null;
		ParamMap resultMap = new ParamMap();
		int executedRtn = 0;
		int targetCnt = 1;
		int successCnt = 0;
		int failCnt = 0;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		
		try{
			
			parameter = "/"+returnMap.get("PA_CLAIM_NO")+"/"+returnMap.get("PA_ORDER_NO")+"/"+returnMap.get("PA_ORDER_SEQ")+"/"+returnMap.get("PA_DELY_GB")+"/"+returnMap.get("SLIP_NO");
			
			conn = ComUtil.pa11stConnectionSetting((HashMap<String, String>) returnMap.get("apiInfo"), returnMap.get("PA_API_KEY").toString(), requestType, parameter);
			
			// RESPONSE XML 			
			doc = ComUtil.parseXML(conn.getInputStream());
			descNodes = doc.getElementsByTagName("ResultOrder");
			
			conn.disconnect();
			
			for(int j=0; j<descNodes.getLength();j++){
				for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
					resultMap.put(node.getNodeName().trim(), node.getTextContent().trim());
				}
			}
			
			if( resultMap.getString("result_code").equals("0") ){
				resultMap.put("result_text","교환승인처리 성공");
				resultMap.put("slipPaDoFlag","40");
				resultMap.put("returnPaDoFlag","60");
				successCnt++;
			} else {
				//11ST API 연결 실패
				resultMap.put("slipPaDoFlag","");
				resultMap.put("returnPaDoFlag","");
				failCnt++;
			}
			
		}catch (Exception e) {
			log.info(getMessage("errors.process") + " : IF_PA11STAPI_03_015 fail ["+returnMap.get("MAPPING_SEQ")+"]");
			resultMap.put("result_code", "-9999");
			resultMap.put("result_text", e.getMessage());
			resultMap.put("slipPaDoFlag","");
			resultMap.put("returnPaDoFlag","");
			failCnt++;
			
		}finally{
			Paorderm paorderm = new Paorderm();
			paorderm.setMappingSeq(returnMap.get("MAPPING_SEQ").toString());
			paorderm.setPaOrderNo(returnMap.get("PA_ORDER_NO").toString());
			paorderm.setPaOrderSeq(returnMap.get("PA_ORDER_SEQ").toString());
			paorderm.setPaClaimNo(returnMap.get("PA_CLAIM_NO").toString());
			paorderm.setApiResultCode(resultMap.getString("result_code"));
			paorderm.setApiResultMessage(resultMap.getString("result_text"));
			paorderm.setPaCode(returnMap.get("PA_CODE").toString());
			paorderm.setPaDoFlag(resultMap.getString("slipPaDoFlag"));
			paorderm.setPaOrderGb("40");
			paorderm.setProcDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			//= TPAORDERM UPDATE - 교환배송 건 PA_DO_FLAG 40
			executedRtn = pa11stExchangeDAO.updateExchangePaOrdermResult(paorderm);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
			
			paorderm.setPaOrderGb("45");
			paorderm.setPaDoFlag(resultMap.getString("returnPaDoFlag"));
			//= TPAORDERM UPDATE - 교환회수 건 PA_DO_FLAG 60
			executedRtn = pa11stExchangeDAO.updateExchangePaOrdermResult(paorderm);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
		}
			
		//= 대상건수와 성공건수가 다를 경우 TAPITRACKING.API_RESULT_MSG에 저장.
		if(targetCnt == successCnt){
			resultMap.put("rtnMsg",Constants.SAVE_SUCCESS);
			resultMap.put("resultCnt",successCnt);
		} else {
			resultMap.put("rtnMsg",Constants.SAVE_FAIL );
			resultMap.put("failCnt",failCnt);
		}
		
		return resultMap;
	}
	
	/**
	 * 11번가 교환거부처리 - 처리결과 update
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String saveExchangeRejectProc(ParamMap paramMap) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			executedRtn = pa11stOrderDAO.updatePaOrdermPreCancel(paramMap); //= tpaorderm update
			
			if(executedRtn < 1){
				log.info("[saveExchangeRefusal] tpaorderm update fail");
				log.info("ordNo : " + paramMap.getString("ordNo") + ", ordPrdSeq : " + paramMap.getString("ordPrdSeq") + ", clmReqSeq : " + paramMap.getString("clmReqSeq"));
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
			
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		
		return rtnMsg;
	}
	
	/**
	 * 11번가 교환철회목록조회 저장
	 * @param List<Pa11stclaimlistVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveExchangeCancelList(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		Paorderm paorderm = null;
		
		int existsCnt = 0;
		int executedRtn = 0;
		
		for(int i = 0; arrPa11stclaimlist.size() > i; i++){
			//= 1. 데이터 중복 CHECK.
			existsCnt = pa11stClaimDAO.selectPa11stClaimListExists(arrPa11stclaimlist.get(i));
			if(existsCnt > 0) continue; //= 이미 수집된 데이터 일 경우 skip.
			
			//= 2. TPA11STCLAIMLIST INSERT
			executedRtn = pa11stClaimDAO.insertPa11stClaimList(arrPa11stclaimlist.get(i));
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPA11STCLAIMLIST INSERT" });
			}
			
			for(int j = 0; j < 2; j++){
				//= 3. TPAORDERM INSERT
				paorderm = new Paorderm();
				paorderm.setPaCode(arrPa11stclaimlist.get(i).getPaCode());
				if(j == 0){
					paorderm.setPaOrderGb(arrPa11stclaimlist.get(i).getPaOrderGb());
				}else {
					paorderm.setPaOrderGb("46");
				}
				paorderm.setPaOrderNo(arrPa11stclaimlist.get(i).getOrdNo());
				paorderm.setPaOrderSeq(arrPa11stclaimlist.get(i).getOrdPrdSeq());
				paorderm.setPaClaimNo(arrPa11stclaimlist.get(i).getClmReqSeq());
				paorderm.setPaProcQty(Integer.toString(arrPa11stclaimlist.get(i).getClmReqQty()));
				paorderm.setPaDoFlag("20");
				paorderm.setOutBefClaimGb("0");
				paorderm.setInsertDate(arrPa11stclaimlist.get(i).getInsertDate());
				paorderm.setModifyDate(arrPa11stclaimlist.get(i).getModifyDate());
				paorderm.setModifyId("PA11");
				
				executedRtn = paCommonDAO.insertPaOrderM(paorderm);
				if (executedRtn < 0) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}
			}
			
		}
		return rtnMsg;
	}
	
	/**
	 * 교환거부처리 - 교환취소건 존재여부 확인
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectExchangeRefusalExists(ParamMap paramMap) throws Exception{
		return pa11stExchangeDAO.selectExchangeRefusalExists(paramMap);
	}
	
	/**
	 * 11번가 교환접수 대상 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderChangeTargetList() throws Exception{
		return pa11stExchangeDAO.selectOrderChangeTargetList();
	}
	
	/**
	 * 교환거부처리 API 호출 - IF_PA11STAPI_03_016
	 * 
	 * @param ParamMap
	 * @return ParamMap
	 * @throws Exception
	 */
	public ParamMap reqExchangeRejectProc(HashMap<String, String> reqMap) throws Exception {
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		ParamMap configMap = null;
		Properties params = null;
		
		configMap = new ParamMap();
		configMap.put("apiCode", "IF_PA11STAPI_03_016");
		
		apiInfo = systemDAO.selectPaApiInfo(configMap);
		
		params = new Properties();
		params.setProperty("ordNo",			reqMap.get("PA_ORDER_NO").toString());
		params.setProperty("ordPrdSeq",		reqMap.get("PA_ORDER_SEQ").toString());
		params.setProperty("clmReqSeq", 	reqMap.get("PA_CLAIM_NO").toString());
		params.setProperty("refsRsnCd", 	reqMap.get("refsRsnCd").toString());
		params.setProperty("refsRsn", 		URLEncoder.encode(reqMap.get("refsRsn").toString(), "UTF-8").replaceAll("\\+", "%20"));
		params.setProperty("paCode", 		reqMap.get("PA_CODE").toString());
		
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
	 * 11번가 교환취소 대상 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectChangeCancelTargetList() throws Exception {
		return pa11stExchangeDAO.selectChangeCancelTargetList();
	}
	
	/**
	 * 11번가 교환접수 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return pa11stExchangeDAO.selectOrderChangeTargetDtList(paramMap);
	}
	
	/**
	 * 11번가 교환접수 결과처리 - 교환거부처리 parameter 조회.
	 * @param String
	 * @return HashMap<String, String>
	 * @throws Exception
	 */
	public HashMap<String, String> selectRejectInfo(String mappingSeq) throws Exception {
		return pa11stExchangeDAO.selectRejectInfo(mappingSeq);
	}
	
	/**
	 * 11번가 반품취소 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return pa11stExchangeDAO.selectChangeCancelTargetDtList(paramMap);
	}
	
	
	public int refindOrderChangeTargetList(String paOrderNo) throws Exception {
		return pa11stExchangeDAO.refindOrderChangeTargetList(paOrderNo);
	}

	public int updatePaOrderm4preChangeCancle(Paorderm paorderm) throws Exception {
		return pa11stExchangeDAO.updatePaOrderm4preChangeCancle(paorderm);
	}
	
}