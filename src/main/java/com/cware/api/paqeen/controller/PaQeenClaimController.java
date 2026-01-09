package com.cware.api.paqeen.controller;

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
import com.cware.netshopping.paqeen.service.PaQeenClaimService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/paqeen/claim", description="퀸잇 반품/교환")
@Controller
@RequestMapping(value = "/paqeen/claim")
public class PaQeenClaimController extends AbstractController {
	
	@Autowired
	@Qualifier("paqeen.claim.paQeenClaimService")
	private PaQeenClaimService paQeenClaimService;
	
	/*반품 대상 수집  API*/
	@ApiOperation(value = "반품대상수집  API", notes = "30: 반품 31: 반품 철회 60: 반품완료" , httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/reutrn-list/{returnStatus}", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<ResponseMsg> getReturnList(HttpServletRequest request,
			@PathVariable("returnStatus") String processFlag,
			  @RequestParam(value = "fromDate", required = false) String fromDate,
			    @RequestParam(value = "toDate"	, required = false) String toDate) throws Exception {
		
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		
		paQeenClaimService.getReturnList(processFlag,fromDate, toDate, request);
		
		//반품 승인
		paQeenClaimService.getReturnRecall20(request);
		
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
	/*교환대상수집  API*/
	@ApiOperation(value = "교환대상수집  API", notes = "40: 교환, 41: 교환철회 ", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/claim-list/{claimStatus}", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<ResponseMsg> getExchangeList(HttpServletRequest request,
			@PathVariable("claimStatus") String claimStatus,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate"	, required = false) String toDate) throws Exception {
		
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		
		//교환 조회
		paQeenClaimService.getExchangeList(fromDate, toDate, request, claimStatus);
		
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
	
	/*교환상품수거지시  API*/
	@ApiOperation(value = "교환상품수거지시  API", notes = "", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/claim-recall", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<ResponseMsg> getExchangeRecall(HttpServletRequest request) throws Exception {
		
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		
		// 교환 승인
		paQeenClaimService.getExchangeRecall(request,"20");
		
		//교환 수거지시 
		paQeenClaimService.getExchangeRecall(request,"50");
		
		//교환 수거 완료
		paQeenClaimService.getExchangeRecall(request,"60");
		
		//교환 검수 승인
		paQeenClaimService.getExchangeRecall(request,"70");
		
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
	/*교환 상품 출고 지시  API*/
	@ApiOperation(value = "교환 상품 출고 지시  API", notes = "", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/claim-delivery", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<ResponseMsg> getExchangeDelivery(HttpServletRequest request) throws Exception {
		
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		
		// 교환 상품 출고 
		paQeenClaimService.getExchangeDelivery(request);
		
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
	/*반품상품수거지시  API*/
	@ApiOperation(value = "반품상품수거지시  API", notes = "", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/claim-return-recall", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<ResponseMsg> getReturnRecall(HttpServletRequest request) throws Exception {
		
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		
		//반품 승인 //미사용
//		paQeenClaimService.getReturnRecall(request,"20");
		
//		//반품 수거지시 미사용
//		paQeenClaimService.getReturnRecall(request,"50");
		
		//반품 수거 완료
		paQeenClaimService.getReturnRecall(request,"50");
		
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
}
