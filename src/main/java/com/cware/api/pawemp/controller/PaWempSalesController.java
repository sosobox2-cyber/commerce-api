package com.cware.api.pawemp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
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
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.pawemp.common.service.PaWempApiService;
import com.cware.netshopping.pawemp.sales.model.SettleOptionSales;
import com.cware.netshopping.pawemp.sales.model.SettleShipSales;
import com.cware.netshopping.pawemp.sales.service.PaWempSalesService;
@ApiIgnore
@Api(value="/pawemp/sales", description="정산")
@Controller("com.cware.api.pawemp.PaWempSalesController")
@RequestMapping(value="/pawemp/sales")
@EnableAsync
public class PaWempSalesController extends AbstractController {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pawemp.common.paWempApiService")
	private PaWempApiService paWempApiService;

	@Resource(name = "pawemp.sales.paWempSalesService")
	private PaWempSalesService paWempSalesService;
	
	/**
	 * 정산매출(옵션, 배송비)정보 조회 
	 * @return ResponseEntity
	 * @throws Exception
	 */
	//basicDt형식은 2020-06-01
	@ApiOperation(value = "정산매출정보 조회", notes = "정산매출정보 조회", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { 
				@ApiResponse(code = 200, message = "OK"), 
				@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
				@ApiResponse(code = 490, message = "중복처리 오류 발생."),
				@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/settlement-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> settlementList(HttpServletRequest request,
		@ApiParam(name="basicDt", required=false, value="기준일자", defaultValue = "") @RequestParam(value="basicDt", required=false, defaultValue = "") String basicDt,
		@ApiParam(name="deleteYn", required=false, value="매출정보 삭제여부", defaultValue = "") @RequestParam(value="deleteYn", required=false, defaultValue = "") String deleteYn) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> shipApiInfo = new HashMap<String, String>();
		HashMap<String, String> optApiInfo  = new HashMap<String, String>();
		String duplicateCheck = "";
		int dtChk             = 0; //미사용
		String queryStr       = ""; 
		String shipApiCode    = "IF_PAWEMPAPI_05_002"; // 정상배송비매출
		String optApiCode     = "IF_PAWEMPAPI_05_004"; // 정상옵션매출 2.0
		
		log.info("===== 정산매출정보 조회 API START =====");
		log.info("01.API 기본정보 세팅");
		String dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode"   , optApiCode);
		paramMap.put("broadCode" , Constants.PA_WEMP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
		paramMap.put("startDate" , dateTime);
		
		// 기초날짜가 없을 경우 셋팅 (Default D-1)
		if(StringUtils.isBlank(basicDt)){
			basicDt = DateUtil.toString(DateUtil.addDay(new Date(), -1), DateUtil.WEMP_DATE_FORMAT);
		}
		paramMap.put("basicDt"   , basicDt);
		
		try{
			log.info("02.API 중복실행검사");
			//duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			//if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});

			if (StringUtils.isNotEmpty(deleteYn) && StringUtils.equalsIgnoreCase("Y", deleteYn)) {
				log.info("===== 정산매출 정보 삭제  =====");
				int executedRtn = paWempSalesService.deletePaWempSales(paramMap);
				if (executedRtn < 0) log.info("===== 정산매출 정보 삭제 실패  =====");
			}	
			
			log.info("03.API정보 조회");
			optApiInfo = systemService.selectPaApiInfo(paramMap);
			optApiInfo.put("apiInfo" , paramMap.getString("apiCode"));
			
			paramMap.put("apiCode"   , shipApiCode);
			shipApiInfo = systemService.selectPaApiInfo(paramMap);
			shipApiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			for(int count=0 ; count<Constants.PA_WEMP_CONTRACT_CNT; count++) {
				try{
					if(count == 0) {
						paramMap.put("paName", Constants.PA_BROAD);
						paramMap.put("paCode", Constants.PA_WEMP_BROAD_CODE);
					} else {
						paramMap.put("paName", Constants.PA_ONLINE);
						paramMap.put("paCode", Constants.PA_WEMP_ONLINE_CODE);
					}
					
					log.info("04.배송비 매출 정산일자 중복데이터 체크");
					paramMap.put("apiCode", shipApiCode);		
					//dtChk = paWempSalesService.selectChkPaWempSalesShip(paramMap);
					if(dtChk == 0){
						log.info("05.정산매출(배송비)정보 조회 API 호출"); 			
						queryStr = "basicDt=" + paramMap.get("basicDt").toString();
						List<SettleShipSales> shipSalesList = paWempApiService.callWApiList(shipApiInfo, "GET", SettleShipSales[].class, queryStr, paramMap.getString("paName"));
						if(shipSalesList != null){
							if(shipSalesList.size() > 0){ // 조회데이터 있을 경우
								String rtnMsg = paWempSalesService.saveSettelmentShipListTx(shipSalesList, paramMap.getString("paCode"));
								if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
									paramMap.put("code"   , "500");
									paramMap.put("message", rtnMsg);
								} else {
									paramMap.put("code"   , "200");
									paramMap.put("message", "OK");
								}
							}else{ // 조회데이터 없을 경우
								paramMap.put("code"   , "404");
								paramMap.put("message", getMessage("pa.not_exists_process_list"));
							}
						}else{ //API 연결 실패
							paramMap.put("code"   , "500");
							paramMap.put("message", getMessage("errors.exist",new String[] { "settlementList" }));
						}
					}else {
						paramMap.put("code"   , "490");
						paramMap.put("message", getMessage("ssg.cannot_dup_data", new String[]{"날짜:"+basicDt}));
					}
					
					log.info("06.상품 매출 정산일자 중복데이터 체크");
					paramMap.put("apiCode", optApiCode);
					//dtChk = 0;
					//dtChk = paWempSalesService.selectChkPaWempSalesOpt(paramMap);
					if(dtChk == 0){
						log.info("07.정산매출(상품)정보 조회 API 호출");		
						queryStr = "basicDt=" + paramMap.get("basicDt").toString();
						List<SettleOptionSales> optSalesList = paWempApiService.callWApiList(optApiInfo, "GET", SettleOptionSales[].class, queryStr, paramMap.getString("paName"));
						if(optSalesList != null){
							if(optSalesList.size() > 0){ // 조회데이터 있을 경우
								String rtnMsg = paWempSalesService.saveSettelmentOptListTx(optSalesList, paramMap.getString("paCode"));
								if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
									paramMap.put("code"   , "500");
									paramMap.put("message", rtnMsg);
								} else {
									paramMap.put("code"   , "200");
									paramMap.put("message", "OK");
								}
							}else{ // 조회데이터 없을 경우
								paramMap.put("code"   , "404");
								paramMap.put("message", getMessage("pa.not_exists_process_list"));
							}
						}else{ //API 연결 실패
							paramMap.put("code"   , "500");
							paramMap.put("message", getMessage("errors.exist",new String[] { "settlementList" }));
						}
					}else {
						paramMap.put("code"   , "490");
						paramMap.put("message", getMessage("ssg.cannot_dup_data", new String[]{"날짜:"+basicDt}));
					}
				}catch(Exception e){
					paramMap.put("code"   , "500");
					paramMap.put("message", e.getMessage());
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}
		}catch (Exception e) {
			/*if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}*/
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			/*if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", optApiCode);
			}*/
			log.info("===== 정산매출정보 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
}
