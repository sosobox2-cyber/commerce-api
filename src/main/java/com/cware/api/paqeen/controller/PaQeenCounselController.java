package com.cware.api.paqeen.controller;

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
import com.cware.netshopping.paqeen.message.CounselCommentsResoponseMsg;
import com.cware.netshopping.paqeen.message.CounselDetailResoponseMsg;
import com.cware.netshopping.paqeen.service.PaQeenCounselService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/paqeen/counsel", description="퀸잇 문의사항")
@Controller("com.cware.api.paqeen.PaQeenCounselController")
@RequestMapping(value="/paqeen/counsel")
public class PaQeenCounselController extends AbstractController {
	
	@Autowired 
	@Qualifier("paqeen.counsel.paQeenCounselService")
	private PaQeenCounselService paQeenCounselService;
	
	
	/**
	 * 퀸잇 문의 사항 목록 조회하기
	 * 
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "문의 사항 목록 조회하기", notes = "문의 사항 목록 조회하기", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "문의 사항 목록 조회하기 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/cx-counsel-list")
	public ResponseEntity<?> cxCounselList(HttpServletRequest request,
		    @RequestParam(value = "fromDate", required = false	) String fromDate,
		    @RequestParam(value = "toDate"	, required = false) String toDate) throws Exception {  
		
		ResponseMsg result = paQeenCounselService.getCxCounselList(fromDate, toDate, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	/**
	 * 퀸잇 문의 사항 상세 조회하기
	 * 
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "문의 사항 상세 조회하기", notes = "문의 사항 상세 조회하기", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "문의 사항 상세 조회하기 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/cx-counsel-detail")
	public ResponseEntity<?> cxCounselDetail(HttpServletRequest request,
			@RequestParam(value = "paCode", required = true) String paCode,
			@RequestParam(value = "inquiryId", required = true) String inquiryId) throws Exception {  
		
		CounselDetailResoponseMsg result = paQeenCounselService.getCxCounselDetail(paCode,inquiryId, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	/**
	 * 퀸잇 문의 댓글 조회하기
	 * 
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "문의 사항 댓글 조회하기", notes = "문의 사항 댓글 조회하기", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "문의 사항 댓글 조회하기 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/cx-counsel-comments")
	public ResponseEntity<?> cxCounselComments(HttpServletRequest request,
			@RequestParam(value = "paCode", required = true) String paCode,
			@RequestParam(value = "inquiryId", required = true) String inquiryId) throws Exception {  
		
		CounselCommentsResoponseMsg result = paQeenCounselService.getCxCounselComments(paCode,inquiryId, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	/**
	 * 퀸잇 문의 답변 댓글 등록하기
	 * 
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "문의 답변 댓글 등록하기", notes = "문의 답변 댓글 등록하기", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "문의 답변 댓글 등록하기 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/cx-counsel-proc")
	public ResponseEntity<?> cxCounselProc(HttpServletRequest request,
			@RequestParam(value = "paCounselNo", required = false) String paCounselNo) throws Exception {  
		
		ResponseMsg result = paQeenCounselService.cxCounselProc(paCounselNo, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
}
