package com.cware.api.pafaple.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.pafaple.service.PaFapleCancelService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value="/pafaple/cancel", description="패션플러스 취소/품절처리")
@Controller("com.cware.api.pafaple.PaFapleCancelController")
@RequestMapping(value="/pafaple/cancel")
public class PaFapleCancelController extends AbstractController {

	@Autowired
	@Qualifier("pafaple.cancel.paFapleCancelService")
	private PaFapleCancelService paFapleCancelService;
	
	/*취소신청,승인 대상 수집 API*/
	@ApiOperation(value = "취소신청,승인 대상 수집 API", notes = "cancelStatus : '2' 취소승인 대상 수집, 날짜형식 : yyyy-MM-dd", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/cancel-list/{cancelStatus}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseMsg> getcancelList(
			@PathVariable("cancelStatus") String cancelStatus,
			@RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
			@RequestParam(value = "toDate", required = false, defaultValue = "") String toDate,
			HttpServletRequest request) throws Exception {
		
		paFapleCancelService.getCancelList(cancelStatus, fromDate, toDate, request);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 품절주문 처리 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "품절취소처리 API (BO호출용)", notes = "품절취소처리 API (BO호출용)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/soldOut-cancel", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<?> cancelApprovalProcBo(HttpServletRequest request) throws Exception {
		
		ParamMap apiInfoMap = new ParamMap();
		apiInfoMap = paFapleCancelService.cancelApprovalProcBo(request);
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 모바일 자동취소 (품절취소반품)
	 * @return
	 * @throws Exception
	 */
	@ApiIgnore
	@ApiOperation(value = "모바일 자동취소 (품절취소반품)", notes = "모바일 자동취소 (품절취소반품)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/mobile-order-cancel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> mobileOrderSoldOut(HttpServletRequest request) throws Exception{
		
		ParamMap apiInfoMap = new ParamMap();
		apiInfoMap = paFapleCancelService.mobileOrderSoldOut(request);
		getcancelList("2", "", "", request); // 취소신청,승인 대상 수집 API 호출
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
}
