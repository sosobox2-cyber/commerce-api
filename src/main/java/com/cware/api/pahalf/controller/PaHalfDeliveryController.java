package com.cware.api.pahalf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.pahalf.delivery.service.PaHalfDeliveryService;
import com.cware.netshopping.pahalf.util.PaHalfComUtill;
import com.cware.netshopping.pahalf.util.PaHalfConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/pahalf/delivery", description="하프클럽 배송")
@Controller("com.cware.api.pahalf.PaHalfDeliveryController")
@RequestMapping(value="/pahalf/delivery")
public class PaHalfDeliveryController extends AbstractController{

	@Resource(name = "pahalf.delivery.paHalfDeliveryService")
	private PaHalfDeliveryService paHalfDeliveryService;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Autowired
	private PaHalfConnectUtil paHalfConnectUtil;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "하프클럽 출고처리", notes = "하프클럽 출고처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/slip-out-proc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> slipOutProc(HttpServletRequest request) throws Exception{
		ParamMap			apiInfoMap		= new ParamMap();
		String 				prg_id 			= "IF_PAHALFAPI_03_003";
		Map<String, Object> resultMap		= null;
		Map<String, String> apiResultMap 	= null;
		List<Map<String,Object>> orderShippingList = null;
		int totalCnt = 0;
		int failCnt  = 0;
		try {
			log.info("===== 출고처리 API Start =====");
					
			// =Step 1)API Setting
			paHalfConnectUtil.getApiInfo	  (prg_id	, apiInfoMap);
			// =Step 2)중복체크
			paHalfConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			// =Step 3)배송 데이터 추출 ( 송장번호가 있는 주문 , 배송완료이면서 송장이 없는건)  
			orderShippingList = paHalfDeliveryService.selectSlipOutProcList();
			totalCnt = orderShippingList.size();
			// =Step 4)하프클럽 통신 및 후처리
			for( Map<String,Object> oc   :   orderShippingList ) {
				try {
					apiInfoMap.put("paCode", oc.get("PA_CODE"));
					
					if(oc.get("API_RESULT_MESSAGE").toString().replaceAll(" ", "").contains("송장번호는영어와숫자조합으로만가능")) {
						oc.put("CJ_NO"	, oc.get("SLIP_I_NO"));
						oc.put("OFF_NO"	, oc.get("39")); //DEFALUT 택배사
					}
					
					//ㄴ하프클럽 통신
					List<Map<String,Object>> tempList = new ArrayList<Map<String,Object>>();
					oc = (Map<String, Object>) PaHalfComUtill.replaceCamel(oc);
					
					Map<String,Object> orderShipping = new HashMap<String, Object>();
					orderShipping.put("ordNo", oc.get("ordNo"));
					orderShipping.put("ordNoNm", oc.get("ordNoNm"));
					orderShipping.put("cjNo", oc.get("cjNo"));
					orderShipping.put("offNo", oc.get("offNo"));
					tempList.add(orderShipping);
					
					resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, tempList);
					apiResultMap = PaHalfComUtill.getApiResult(resultMap);

					//ㄴ후처리 (UPDATE TPAORDERM.DO_FLAG)
					//예외케이스 :: result_code != 1 이지만 result_text.replaceAll(" ", "").contains("결제확인또는출고대기상태에서") 성공처리
					if("200".equals(apiResultMap.get("code")) || 
						apiResultMap.get("message").replaceAll(" ", "").contains("결제확인또는출고대기상태에서")) {//성공처리
						paHalfDeliveryService.updateSlipOutProc(oc);
					}else {
						//실패 처리
						oc.put("apiResultMsg", apiResultMap.get("message"));
						paHalfDeliveryService.updateSlipOutProcFail(oc);
	                    throw processException("pa.connect_error", new String[] {apiResultMap.get("message") });
	                 }
				} catch (Exception e) {
					failCnt++;
					log.info("{}: {} MAPPING_SEQ: {} 운송장번호: {}","하프클럽 출고처리 오류", PaHalfComUtill.getErrorMessage(e), oc.get("MAPPING_SEQ"), oc.get("CJ_NO"));				
				}
				
			}
			if(failCnt > 0 ) {
				throw processException("errors.detail", new String[] {"하프클럽 출고처리 오류  - 실패:" + failCnt + "건 전체:" + totalCnt + "건"});
			}
			
		}catch (Exception e) {
			log.info("{}: {}", "하프클럽 출고처리 오류", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
			log.info("===== 출고처리 API End =====");
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
				

}
