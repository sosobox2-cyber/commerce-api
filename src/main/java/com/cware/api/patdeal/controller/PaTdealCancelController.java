package com.cware.api.patdeal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaTdealClaimListVO;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.patdeal.service.PaTdealCancelService;
import com.cware.netshopping.patdeal.service.PaTdealClaimService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value="/patdeal/claim", description="TDEAL CANCEL")
@Controller("com.cware.api.patdeal.PaTdealCancelController")
@RequestMapping(path = "/patdeal/cancel")
public class PaTdealCancelController extends AbstractController{
	
	@Autowired
	@Qualifier("patdeal.claim.paTdealClaimService")
	private PaTdealClaimService paTdealClaimService;
	
	@Autowired
	@Qualifier("patdeal.cancel.paTdealCancelService")
	private PaTdealCancelService paTdealCancelService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@Autowired
	private PaTdealAsyncController paTdealAsyncController;
	
	@Autowired
	@Qualifier("com.cware.api.patdeal.PaTdealClaimController")
	private PaTdealClaimController paTdealClaimController;
	
	/**
	 * 취소신청 목록조회
	 * @param claimStatus
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "취소신청 목록조회 API", notes = "취소신청 목록조회 API", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/cancel-list/{claimStatus}", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> paTdealCancelVOList(
			@PathVariable("claimStatus") String claimStatus,
			@ApiParam(name = "fromDate", value = "기간시작일[yyyy-MM-dd]", defaultValue = "") @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate",   value = "기간종료일[yyyy-MM-dd]", defaultValue = "") @RequestParam(value = "toDate",   required = false) String toDate,
			HttpServletRequest request) throws Exception {
		// STEP 1. 취소 클레임 조회
		List<PaTdealClaimListVO> paTdealCancelVOList = paTdealClaimService.getTdealClaimList(claimStatus, fromDate, toDate, request);
		
		// STEP 2. 취소 저장
		for(PaTdealClaimListVO paTdealCancelVo : paTdealCancelVOList) {
			paTdealCancelService.saveTdealCancelListTx(claimStatus, paTdealCancelVo);
		}
		
		// STEP 3. 취소 승인
		paTdealCancelService.cancelConfirmProc(request);
		
		// STEP 4. 취소 동기화
		//////step 4-1.취소 동기화 대상 탐색
		ParamMap paramMap = new ParamMap();
		paramMap.put("paOrderGb", "20");
		List<HashMap<String, Object>> cancelInputTargetList = paTdealClaimService.selectClaimTargetList(paramMap);
		
		//////step 4-2.동기화
		for(HashMap<String, Object> cancelInputTarget : cancelInputTargetList) {
			try {
				paTdealAsyncController.cancelInputAsync(cancelInputTarget,request);
			} catch (Exception e) {
				log.error( "TDEAL 주문 취소 생성 오류", e);
				continue;
			}
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 취소승인
	 * @param claimNo
	 * @param orderNo
	 * @param optionManagementCd
	 * @param paCode
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("null")
	@ApiOperation(value = "취소승인 API (BO호출용)", notes = "취소승인 API (BO호출용)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/cancel-approval-proc-bo", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<?> cancelApprovalProcBo(
			@RequestParam(value = "claimNo", 		    required = true) String claimNo,
			@RequestParam(value = "orderNo", 		    required = true) String orderNo,
			@RequestParam(value = "optionManagementCd", required = true) String optionManagementCd,
			@RequestParam(value = "orderCnt",			required = true) String orderCnt,
			@RequestParam(value = "paCode", 		    required = true) String paCode,
			HttpServletRequest request) throws Exception {
		
		HashMap<String, Object> requestMap = new HashMap<String, Object>();
		ParamMap apiInfoMap = new ParamMap();
		requestMap.put("PA_CODE", paCode);
		requestMap.put("CLAIM_NO", claimNo);
		requestMap.put("ORDER_NO", orderNo);
		requestMap.put("OPTION_MANAGEMENT_CD", optionManagementCd);
		requestMap.put("ORDER_CNT", orderCnt);
		requestMap.put("OUT_BEF_CLAIM_GB", "1");
		requestMap.put("CLAIM_STATUS", "20");
		apiInfoMap = paTdealCancelService.cancelConfirm(requestMap,request);
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 일괄취소반품
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "일괄취소반품 API", notes = "일괄취소반품 API", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/orderSoldOut", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<?> orderSoldOut(HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("paGroupCode", "13");
		// STEP1. 품절취소반품, 일괄취소반품 대상 조회 
		List<Object> cancelList = paCommonService.selectPaSoldOutordList(paramMap);
		for(Object cancelItem : cancelList) {
			// STEP2. 대상 취소 요청  
			paTdealCancelService.cancelRequest(cancelItem, request);
		}
		// STEP3. 2에서 요청된 취소 조회 및 저장 및 승인
		paTdealCancelVOList("20", null, null, request);
		
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 티딜 품절 취소처리 (주문승인이전)
	 * 
	 * @param request
	 * @param procId
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "품절 취소처리", notes = "품절 취소처리(주문승인이전)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "품절 취소 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/soldOut-cancel")
	public ResponseEntity<?> soleOutCancel(HttpServletRequest request,
		    @RequestParam(value = "soldOutList", required = false	) List<Map<String, Object>> soldOutList) throws Exception {  
		
		ResponseMsg result = paTdealCancelService.soldOutCancelProc(soldOutList, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	/**
	 * 티딜 주문 취소하기(주문승인이전)
	 * 
	 * @param request
	 * @param procId
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "주문 취소하기", notes = "주문 취소하기(주문승인이전)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "취소 처리 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/cancel-approval-proc")
	public ResponseEntity<?> cancelApprovalProc(HttpServletRequest request,
			@RequestParam(value = "cancelList", required = false	) List<Map<String, Object>> cancelList) throws Exception {  
		
		ResponseMsg result = paTdealCancelService.cancelApprovalProc(cancelList, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
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
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/mobile-order-cancel", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<?> mobileOrderSoldOut(HttpServletRequest request) throws Exception {
		
		// STEP1. 품절취소반품, 일괄취소반품 대상 조회 
		List<HashMap<String, String>> cancelList = paTdealCancelService.selectPaMobileOrderAutoCancelList();
		for(HashMap<String, String> cancelItem : cancelList) {
			// STEP2. 대상 취소 요청  
			paTdealCancelService.mobliecancelRequest(cancelItem, request);
		}
		// STEP3. 2에서 요청된 취소 조회 및 저장 및 승인
		paTdealCancelVOList("20", null, null, request);
		
		paTdealClaimController.returnInputMain(request);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
