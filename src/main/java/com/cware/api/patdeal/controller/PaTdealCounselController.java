package com.cware.api.patdeal.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.netshopping.patdeal.service.PaTdealCounselService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller("com.cware.api.patdeal.PaTdealCounselController")
@RequestMapping(value="/patdeal/counsel")
public class PaTdealCounselController extends AbstractController{

	@Autowired 
	@Qualifier("patdeal.counsel.paTdealCounselService")
	private PaTdealCounselService paTdealCounselService;
	
	/**
	 * 티딜 상품문의 조회하기
	 * 
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "상품문의 조회하기", notes = "상품문의 조회하기", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "상품문의 조회하기 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/goods-counsel-list")
	public ResponseEntity<?> goodsCounselList(HttpServletRequest request,
		    @RequestParam(value = "fromDate", required = false	) String fromDate,
		    @RequestParam(value = "toDate"	, required = false) String toDate) throws Exception {  
		
		ResponseMsg result = paTdealCounselService.getGoodsCounselList(fromDate, toDate, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	/**
	 * 티딜 상품문의 답변 등록
	 * 
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "상품문의 답변 등록", notes = "상품문의 답변 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "상품문의 답변 등록 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/goods-counsel-proc")
	public ResponseEntity<?> goodsCounselProc(HttpServletRequest request,
			@RequestParam(value = "paCounselNo", required = false	) String paCounselNo) throws Exception {  
		
		ResponseMsg result = paTdealCounselService.goodsCounselProc(paCounselNo,request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	/**
	 * 티딜 업무 메시지 조회하기
	 * 
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "업무 메시지 조회하기", notes = "업무 메시지 조회하기", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "상품문의 조회하기 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/task-messages-list")
	public ResponseEntity<?> taskMessagesList(HttpServletRequest request,
		    @RequestParam(value = "fromDate", required = false	) String fromDate,
		    @RequestParam(value = "toDate"	, required = false) String toDate) throws Exception {  
		if(fromDate == null) {
			fromDate =  "";
		}
		if(toDate == null) {
			toDate =  "";
		}
		ResponseMsg result = paTdealCounselService.getTaskMessagesList(fromDate, toDate, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	/**
	 * 티딜 업무 메시지 답변 등록(상세 업무 메시지 등록)
	 * 
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "업무 메시지 답변 등록", notes = "업무 메시지 답변 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "업무 메시지 답변 등록 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/task-messages-proc")
	public ResponseEntity<?> taskMessagesProc(HttpServletRequest request,
			@RequestParam(value = "paCounselNo", required = false	) String paCounselNo) throws Exception {  
		
		ResponseMsg result = paTdealCounselService.getTaskMessagesProc(paCounselNo, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
}
