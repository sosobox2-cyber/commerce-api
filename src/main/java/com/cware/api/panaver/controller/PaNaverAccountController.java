package com.cware.api.panaver.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PanaversettlementVO;
import com.cware.netshopping.panaver.account.service.PaNaverAccountService;

@Controller("com.cware.api.panaver.PaNaverAccountController")
@RequestMapping(value="/panaver/account")
public class PaNaverAccountController extends AbstractController{

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "panaver.account.PaNaverAccountService")
	private PaNaverAccountService paNaverAccountService;
	
	/**
	 * 매출내역 조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "daily-settle-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> settlementList(HttpServletRequest request,
			@RequestParam(value="fromDate", required=false, defaultValue="") String fromDate,
			@RequestParam(value="toDate", required=false, defaultValue="") String toDate,
			@RequestParam(value="delYn", required=false, defaultValue="") String delYn) throws Exception{
		
		/**
	     *  네이버 날짜 전달 파라미터 날짜포맷 : 년(4) 월(2) 일(2)
	     *  기간입력
	     *  Default D-1 ~ D
	     * */
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck = "";
		String startDate = "";
		String endDate = "";
		String procPaCode = "";
		PanaversettlementVO paNaverSettlement = null;
		List<PanaversettlementVO> arrPaNaverAccountList = new ArrayList<PanaversettlementVO>();
		
		log.info("===== 매출내역 조회 API Start=====");
		log.info("01.API 기본 정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PANAVERAPI_05_001";
		String request_type = "GET";
		String dateTime = systemService.getSysdatetimeToString();
		Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_NAVER_CODE);
		paramMap.put("startDate", dateTime);
		
		int dtChk = 0;
		
		String respCode = null;
		String respMsg = null;
		String paCode = null;
		int totalPageCount = 1;
		int currentPage = 1;
		
		
		
		try{
			log.info("02.API 중복 실행 검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo.put("API_URL", "settlements/by-case");
			apiInfo.put("INTERNAL_URL", "/panaver/accout/daily-settle-list");
			
			//기간 입력
			if(!(fromDate.isEmpty()||fromDate.equals(""))&&!(toDate.isEmpty()||toDate.equals(""))){
				startDate = fromDate;
				endDate = toDate;
			}else{//예외 (Default D-1
				endDate = ComUtil.dateFormater(DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT));
				startDate = ComUtil.dateFormater(DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT));
			}

			paCode = Constants.PA_NAVER_CODE;

			log.info("API_URL :" + apiInfo.get("API_URL"));
			
			paramMap.put("paCode", Constants.PA_NAVER_CODE);
			paramMap.put("fromDate", startDate);
			paramMap.put("toDate", endDate);
			dtChk = 0;
			
			if(!delYn.equals("Y") && !delYn.equals("y")){
				log.info("03.정산일자 중복데이터 체크");
				dtChk = paNaverAccountService.selectChkPaNaverAccount(paramMap);
			}
			
			if(dtChk > 0) {
				paramMap.put("code","490");
				paramMap.put("message",getMessage("ssg.cannot_dup_data", new String[]{"날짜:"+startDate + "~" + endDate}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			
			Map queryString = new HashMap();
			queryString.put("startDate", startDate);
			queryString.put("endDate", endDate);
			
			while(true) {
				
				queryString.put("pageNumber", String.valueOf(currentPage));
				String parameter = ComUtil.makeHTTPQuery(queryString); //QueryString 생성 method
				log.info("----> QueryString parameter info : " + parameter);
				
				log.info("03.매출내역 조회 API 호출");
				JSONObject resultJson = new JSONObject(ComUtil.paNaverHttpGetConnection(apiInfo, procPaCode, request_type, parameter));
				respCode = resultJson.getString("code");
				respMsg = resultJson.getString("message");

				if("Success".equals(respCode)) {
					if(!(resultJson.get("body").equals(null))){
						log.info("----> respCode : " + respCode);
						log.info("----> respMsg : " + respMsg);
						
						if(currentPage == 1) {
							totalPageCount = resultJson.getJSONObject("body").getInt("totalPageCount");							
						}
						currentPage = resultJson.getJSONObject("body").getInt("currentPage");
						
						JSONArray resultData = resultJson.getJSONObject("body").getJSONArray("list");
						log.info("----> 정산완료 목록 건수 : " + resultData.length());
						if(resultData.length() > 0) {
							for(int i=0; i<resultData.length(); i++){
								JSONObject accountJson = resultData.getJSONObject(i);
								paNaverSettlement = new PanaversettlementVO();
								paNaverSettlement.setPaCode(paCode);
								paNaverSettlement.setSettleBasicDate(DateUtil.toTimestamp(accountJson.get("settleBasisDate").toString(), "yyyyMMdd"));
								paNaverSettlement.setSettleExpectDate(DateUtil.toTimestamp(accountJson.get("settleExpectDate").toString(),"yyyyMMdd"));
								if(accountJson.get("settleCompleteDate").toString().equals("null")){
									paNaverSettlement.setSettleCompleteDate(null);
								}else{
									paNaverSettlement.setSettleCompleteDate(DateUtil.toTimestamp(accountJson.get("settleCompleteDate").toString(),"yyyyMMdd"));
								}
								paNaverSettlement.setOrderId(accountJson.get("orderId").toString());
								paNaverSettlement.setProductOrderId(accountJson.get("productOrderId").toString());
								paNaverSettlement.setProductOrderType(accountJson.get("productOrderType").toString());
								paNaverSettlement.setSettleType(accountJson.get("settleType").toString());
								paNaverSettlement.setProductId(accountJson.get("productId").toString());
								paNaverSettlement.setProductName(accountJson.get("productName").toString());
								paNaverSettlement.setPurchaserName(accountJson.get("purchaserName").toString());
								paNaverSettlement.setPaySettleAmt(accountJson.getDouble("paySettleAmount"));
								paNaverSettlement.setTotalCommiAmt(accountJson.getDouble("totalCommissionAmount"));
								paNaverSettlement.setTotalPayCommiAmt(accountJson.getDouble("totalPayCommissionAmount"));
								paNaverSettlement.setPrimaryPayMean(accountJson.get("primaryPayMeans").toString());
								paNaverSettlement.setPrimaryPayMeanBasicAmt(accountJson.getDouble("primaryPayMeansBasisAmount"));
								paNaverSettlement.setPrimaryPayMeanPayCommiAmt(accountJson.getDouble("primaryPayMeansPayCommissionAmount"));
								paNaverSettlement.setSubPayMean(accountJson.get("subPayMeans").toString());
								paNaverSettlement.setSubPayMeanBasicAmt(accountJson.getDouble("subPayMeansBasisAmount"));
								paNaverSettlement.setSubPayMeanPayCommiAmt(accountJson.getDouble("subPayMeansPayCommissionAmount"));
								paNaverSettlement.setSub2PayMean(accountJson.get("sub2PayMeans").toString());
								paNaverSettlement.setSub2PayMeanBasicAmt(accountJson.getDouble("sub2PayMeansBasisAmount"));
								paNaverSettlement.setSub2PayMeanPayCommiAmt(accountJson.getDouble("sub2PayMeansPayCommissionAmount"));
								paNaverSettlement.setSub3PayMean(accountJson.get("sub3PayMeans").toString());
								paNaverSettlement.setSub3PayMeanBasicAmt(accountJson.getDouble("sub3PayMeansBasisAmount"));
								paNaverSettlement.setSub3PayMeanPayCommiAmt(accountJson.getDouble("sub3PayMeansPayCommissionAmount"));
								paNaverSettlement.setChannelCommiAmt(accountJson.getDouble("channelCommissionAmount"));
								paNaverSettlement.setFreeInstallmentCommiAmt(accountJson.getDouble("freeInstallmentCommissionAmount"));
								paNaverSettlement.setSaleCommissionAmount(accountJson.getDouble("saleCommissionAmount"));
								paNaverSettlement.setSellingInterlockCommiAmt(accountJson.getDouble("sellingInterlockCommissionAmount"));
								paNaverSettlement.setBenefitSettleAmt(accountJson.getDouble("benefitSettleAmount"));
								paNaverSettlement.setSettleExpectAmt(accountJson.getDouble("settleExpectAmount"));
								paNaverSettlement.setQuickSettleCreated(accountJson.get("quickSettleCreated").toString());
								paNaverSettlement.setDelYn(delYn);
								paNaverSettlement.setFromDate(DateUtil.toTime(startDate.substring(0,4)+"/"+startDate.substring(4,6)+"/"+startDate.substring(6,8)));
								paNaverSettlement.setToDate(DateUtil.toTime(endDate.substring(0,4)+"/"+endDate.substring(4,6)+"/"+endDate.substring(6,8)));
								paNaverSettlement.setInsertId("BATCH");
								paNaverSettlement.setInsertDate(sysdateTime);
								
								paNaverSettlement.setPayDate(DateUtil.toTimestamp(accountJson.get("payDate").toString(),"yyyyMMdd"));

								arrPaNaverAccountList.add(paNaverSettlement);
							}
						} else {
							paramMap.put("code","200");
							paramMap.put("message", respMsg);
						}
					} else {
						paramMap.put("code","200");
						paramMap.put("message", respMsg);
					}
					currentPage++;
				} else {
					//API 연결 실패
					paramMap.put("code","500");
					paramMap.put("message", getMessage("errors.exist",new String[] { respMsg }));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}				
				
				if(currentPage > totalPageCount) {
					break;
				}
			}
			
			paNaverAccountService.saveSettelmentListTx(arrPaNaverAccountList);
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
			
		}catch(Exception e){
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", "PANAVER");
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("04.매출내역 조회 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
}
