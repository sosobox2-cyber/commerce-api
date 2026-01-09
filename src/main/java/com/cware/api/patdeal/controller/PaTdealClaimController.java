package com.cware.api.patdeal.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
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
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.PaTdealClaimListVO;
import com.cware.netshopping.patdeal.service.PaTdealClaimService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller("com.cware.api.patdeal.PaTdealClaimController")
@RequestMapping(path = "/patdeal/claim")
public class PaTdealClaimController extends AbstractController{
		
	@Autowired
	@Qualifier("patdeal.claim.paTdealClaimService")
	private PaTdealClaimService paTdealClaimService;
	
	@Autowired
	private PaTdealAsyncController paTdealAsyncController;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	/**
	 * 클레임 목록조회
	 * @param claimStatus
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "클레임 목록조회 API", notes = "클레임 목록조회 (30: 반품, 40: 교환, 60: 반품완료)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/claim-list/{claimStatus}", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> paTdealClaimVOList(
			@PathVariable("claimStatus") String claimStatus,
			@ApiParam(name = "fromDate", value = "기간시작일[yyyy-MM-dd]", defaultValue = "") @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate",   value = "기간종료일[yyyy-MM-dd]", defaultValue = "") @RequestParam(value = "toDate",   required = false) String toDate,
			HttpServletRequest request) throws Exception {
		if(!(claimStatus.equals("30") || claimStatus.equals("40") || claimStatus.equals("60"))) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		// STEP 1. 클레임 수집 
		List<PaTdealClaimListVO> paTdealClaimVOList = paTdealClaimService.getTdealClaimList(claimStatus, fromDate, toDate, request);

		// STEP 2. 클레임 저장
		for(PaTdealClaimListVO paTdealClaimVo : paTdealClaimVOList) {
			paTdealClaimService.saveTdealClaimListTx(claimStatus, paTdealClaimVo);
		}
		// 클레임 승인 시 고객이 철회가 불가능 하므로 클레임 승인은 하지않음

		//STEP 3. 클레임 생성
		if(claimStatus.equals("40")) {
			exchangeInputMain(request);
		}else {
			returnInputMain(request);
		}
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
	/**
	 * 클레임 상태조회
	 * @param claimStatus
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "클레임 철회조회 API", notes = "클레임 철회조회 (31 : 반품철회 41 : 교환철회) ", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/claim-status/{claimStatus}", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> paTdealClaimStatusCheck(
			@PathVariable("claimStatus") String claimStatus,
			HttpServletRequest request) throws Exception {
		if(!(claimStatus.equals("31") || claimStatus.equals("41"))) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		/**
		 * 티딜에서는 클레임 철회 조회 API가 없다 따라서 
		 * 1. 주문 상세 조회를 사용하여 SK스토아 DB에 저장되어있는 클레임 접수 대상을 뽑아 낸다
		 * 2. 1.에서 뽑아낸 데이터로 티딜 상세주문 정보를 조회한다.
		 * 3. 2.에서 조회한 데이터에 클레임 상태를 확인한다. 클레임 상태가 NULL 일경후 철회된 데이터 이다.  
		 * **/
		// STEP 1.클레임 접수 대상 수집 및 저장 (클레임 철회, 완료가 되지 않은 건) 
		responseMsg = paTdealClaimService.procTdealClaimCancelList(claimStatus, request);
		// StEP 2.동기화
		claimCancelInputMain(claimStatus,request);
		
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
	/**
	 * 반품 수거 완료
	 * @param claimStatus
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "반품 교환 수거 완료 API", notes = "반품 교환 수거 완료 API", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/return-slip-out-proc", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> returnSlipOutProc(HttpServletRequest request) throws Exception {
		
		ResponseMsg responseMsg = new ResponseMsg("200","조회된 데이터가 없습니다.");
		// STEP 1.수거 완료 대상 추출
		List<HashMap<String, Object>> tdealReturnCompleList = paTdealClaimService.selectTdealReturnCompleList();	
		
		// STEP 2.수거 확정 및 상태값 업데이트
		for(HashMap<String, Object> tdealReturnCompleItem :tdealReturnCompleList) {
			try {
				//반품,교환 승인
				responseMsg = paTdealClaimService.returnApprovalProc(tdealReturnCompleItem, request);
				
				if(responseMsg.getCode().equals("204")) {
				    //수거 완료처리
				    responseMsg = paTdealClaimService.returnCompleProc(tdealReturnCompleItem, request);}
			} catch (Exception e) {
				log.error( "TDEAL 클레임 업데이트 오류", e);
				responseMsg = new ResponseMsg("400","TDEAL 클레임 생성 오류"+ e);
				continue;
			}
		}
		
		// STEP 3.교환 수거 확정 이후 출고 주문 승인 및 orderOptionNo 업데이트.
		List<HashMap<String, Object>> tdealExchangeReturnDoFlag60List = paTdealClaimService.selectTdealExchangeReturnDoFlag60List();
		for(HashMap<String, Object> tdealExchangeReturnDoFlag60Item :tdealExchangeReturnDoFlag60List) {
			try {
				responseMsg = paTdealClaimService.exchangeDeliveryProc(tdealExchangeReturnDoFlag60Item, request);
			} catch (Exception e) {
				log.error( "TDEAL 클레임 업데이트 오류", e);
				responseMsg = new ResponseMsg("400","TDEAL 클레임 생성 오류"+ e);
				continue;
			}
		}
		
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	
	public void returnInputMain(HttpServletRequest request) throws Exception {
		String prg_id 			= "PATDEAL_RETURN_INPUT";
		String duplicateCheck 	= "";
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("paOrderGb", "30");
			List<HashMap<String, Object>> claimInputTargetList = paTdealClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> claimInputTarget : claimInputTargetList) {
				try {
						paTdealAsyncController.returnClaimAsync(claimInputTarget,request);
				} catch (Exception e) {
					log.error( " TDEAL 반품 생성 오류", claimInputTarget.get("PA_ORDER_NO"),e.getMessage());
					continue;
				}
			}
			
		}catch (Exception e) {
			log.info("{} : {}","티딜 반품 생성 오류", e.getMessage());
		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}
	
	private void exchangeInputMain(HttpServletRequest request) throws Exception {
		String prg_id 			= "PATDEAL_EXCAHANGE_INPUT";
		String duplicateCheck 	= "";
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("paOrderGb", "40");
			List<HashMap<String, Object>> claimInputTargetList = paTdealClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> claimInputTarget : claimInputTargetList) {
				try {
						paTdealAsyncController.changeClaimAsync(claimInputTarget,request);
				} catch (Exception e) {
						log.error( " TDEAL 반품 생성 오류", claimInputTarget.get("PA_ORDER_NO"),e.getMessage());
					continue;
				}
			}
			
		}catch (Exception e) {
			log.info("{} : {}","티딜 교환 생성 오류", e.getMessage());
		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}
	
	private void claimCancelInputMain(String claimStatus,HttpServletRequest request) throws Exception {
		String return_prg_id 			= "PATDEAL_RETURN_CANCEL_INPUT";
		String exchange_prg_id 			= "PATDEAL_EXCHANGE_CANCEL_INPUT";
		String prg_id = "";
		String duplicateCheck 	= "";
		
		try {
			prg_id = claimStatus.equals("31")?  return_prg_id : exchange_prg_id;
			
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("paOrderGb", claimStatus);
			List<HashMap<String, Object>> cancelInputTargetList = paTdealClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> cancelInputTarget : cancelInputTargetList) {
				try {
					paTdealAsyncController.claimCancelAsync(cancelInputTarget,request);
				} catch (Exception e) {
					log.error( "TDEAL 클레임 업데이트 오류", e);
					continue;
				}
			}
			
		}catch (Exception e) {
			log.info("{} : {}","티딜 클레임 철회 생성 오류", e.getMessage());
		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}
	
}
