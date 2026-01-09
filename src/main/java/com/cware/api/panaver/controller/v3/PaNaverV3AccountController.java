package com.cware.api.panaver.controller.v3;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.netshopping.panaver.v3.service.PaNaverV3AccountService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller("com.cware.api.panaver.v3.PaNaverV3AccountController")
@RequestMapping(value="/panaver/v3/account")
public class PaNaverV3AccountController extends AbstractController {
	
	@Resource(name = "panaver.v3.account.paNaverV3AccountService")
	private PaNaverV3AccountService paNaverV3AccountService;
	
	/**
	 * 건별 정산 내역 조회
	 * 
	 * @param request
	 * @param searchDate
	 * @param pageNumber
	 * @param pageSize
	 * @param delYn
	 * @param procId
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "건별 정산 내역 조회", notes = "건별 정산 내역 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/case-settle-list")
	public ResponseEntity<?> settleList(HttpServletRequest request,
			@ApiParam(name = "searchDate", required = false, value = "조회일") @RequestParam(value = "searchDate", required = false, defaultValue = "") String searchDate,
			@ApiParam(name = "pageNumber", required = false, value = "페이지 번호") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@ApiParam(name = "pageSize", required = false, value = "페이지 크기") @RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize,
			@ApiParam(name = "delYn", required = false, value = "삭제 처리 여부") @RequestParam(value = "delYn", required = false, defaultValue = "N") String delYn,
			@ApiParam(name = "procId", required = false, value = "처리자 ID") @RequestParam(value = "procId", required = false, defaultValue = "PANAVER") String procId) throws Exception {	
		
		ResponseMsg result = paNaverV3AccountService.getCaseSettleList(searchDate, pageNumber, pageSize, delYn, procId, request);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
