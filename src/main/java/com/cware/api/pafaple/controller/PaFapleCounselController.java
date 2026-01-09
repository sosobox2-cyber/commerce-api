package com.cware.api.pafaple.controller;

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
import com.cware.netshopping.pafaple.service.PaFapleCounselService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/pafaple/counsel", description="패션플러스 CS처리")
@Controller("com.cware.api.pafaple.PaFapleCounselController")
@RequestMapping(value="/pafaple/counsel")
public class PaFapleCounselController extends AbstractController{
	
	@Autowired 
	@Qualifier("pafaple.counsel.paFapleCounselService")
	private PaFapleCounselService paFapleCounselService;
	
	/**
	 * 패션플러스 CS 알리미 조회하기
	 * 
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "CS 알리미 조회하기", notes = "CS 알리미 조회하기", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "CS 알리미 조회하기 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/cs-notice-list")
	public ResponseEntity<?> getCsNoticeList(HttpServletRequest request,
		    @RequestParam(value = "fromDate", required = false	) String fromDate,
		    @RequestParam(value = "toDate"	, required = false) String toDate) throws Exception {  
		if(fromDate == null) {
			fromDate =  "";
		}
		if(toDate == null) {
			toDate =  "";
		}
		ResponseMsg result = paFapleCounselService.getCsNoticeList(fromDate, toDate, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	/**
	 * 패션플러스 CS 알리미 답변 등록
	 * 
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "CS 알리미 답변 등록", notes = "업무 메시지 답변 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "업무 메시지 답변 등록 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/cs-notice-proc")
	public ResponseEntity<?> csNoticeProc(HttpServletRequest request,
			@RequestParam(value = "paCounselNo", required = false	) String paCounselNo) throws Exception {  
		
		ResponseMsg result = paFapleCounselService.csNoticeProc(paCounselNo, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	/**
	 * 패션플러스 고객문의 조회하기
	 * 
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "고객문의게시판조회", notes = "고객문의게시판조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "고객문의게시판조회 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/bbs-list")
	public ResponseEntity<?> getBbsList(HttpServletRequest request,
		    @RequestParam(value = "fromDate", required = false	) String fromDate,
		    @RequestParam(value = "toDate"	, required = false) String toDate) throws Exception {  
		if(fromDate == null) {
			fromDate =  "";
		}
		if(toDate == null) {
			toDate =  "";
		}
		ResponseMsg result = paFapleCounselService.getBbsList(fromDate, toDate, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}

	/**
	 * 패션플러스 고객문의 답변 등록
	 * 
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "고객문의 답변 등록", notes = "고객문의 답변 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "상품문의 답변 등록 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/bbs-add")
	public ResponseEntity<?> goodsCounselProc(HttpServletRequest request,
			@RequestParam(value = "paCounselNo", required = false	) String paCounselNo) throws Exception {  
		
		ResponseMsg result = paFapleCounselService.goodsCounselProc(paCounselNo,request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
}
