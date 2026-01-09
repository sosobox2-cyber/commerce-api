package com.cware.api.patdeal.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.patdeal.message.OrderConfirmResoponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.patdeal.service.PaTdealCancelService;
import com.cware.netshopping.patdeal.domain.OrderDetail;
import com.cware.netshopping.patdeal.service.PaTdealClaimService;
import com.cware.netshopping.patdeal.service.PaTdealDeliveryService;
//import com.cware.netshopping.patdeal.service.PaTdealDeliveryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller("com.cware.api.patdeal.PaTdealDeliveryController")
@RequestMapping(path = "/patdeal/delivery")
public class PaTdealDeliveryController extends AbstractController{
		
	@Autowired 
	@Qualifier("patdeal.delivery.paTdealDeliveryService")
	private PaTdealDeliveryService paTdealDeliveryService;
	
	@Autowired 
	@Qualifier("patdeal.cancel.paTdealCancelService")
	private PaTdealCancelService paTdealCancelService;
	
	@Autowired 
	@Qualifier("patdeal.claim.paTdealClaimService")
	private PaTdealClaimService paTdealClaimService;
	
	@Autowired
	private PaTdealAsyncController paTdealAsyncController;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;

	/**
	 * 주문 목록 조회
	 * 
	 * @param request
	 * @param fromDate
	 * @param toDate
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "주문 목록 조회", notes = "주문 목록 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "주문 목록 조회 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/order-info")
	public ResponseEntity<?> getOrderInfo(HttpServletRequest request,
			@ApiParam(name = "fromDate", value = "기간시작일[yyyy-MM-dd]", defaultValue = "") @RequestParam(value = "fromDate", required = false	) String fromDate,
			@ApiParam(name = "toDate", value = "기간종료일[yyyy-MM-dd]", defaultValue = "") @RequestParam(value = "toDate"	, required = false) String toDate) throws Exception {  
		
		ResponseMsg result = paTdealDeliveryService.getOrderList(fromDate, toDate, request);
		if("200".equals(result.getCode())) {
			//주문확인 호출
			OrderConfirmResoponseMsg orderConfirmResult = paTdealDeliveryService.orderConfirmProc(null, request);
			
			if ( String.valueOf(HttpStatus.OK.value()).equals(orderConfirmResult.getCode()) ) {
				
				if(!orderConfirmResult.getSoldOutList().isEmpty()) { // 품절 취소 처리 
					paTdealCancelService.soldOutCancelProc(orderConfirmResult.getSoldOutList(), request);
				}
				
				if(!orderConfirmResult.getCancelList().isEmpty()) { // 도서산간/제주 불가 주문건 취소 처리
					paTdealCancelService.cancelApprovalProc(orderConfirmResult.getCancelList(), request);
				}
				
			}
		}
		
		orderInputMain(request);//ASYNC

		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	
	/**
	 * 티딜 주문 승인 (상품 준비중 상태로 변경 요청하기)
	 * 
	 * @param request
	 * @param orderProductOptionNo
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "주문 승인", notes = "주문 승인", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "주문 승인 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/order-confirm")
	public ResponseEntity<?> orderConfirm(HttpServletRequest request,
		    @RequestParam(value = "orderProductOptionNo", required = false	) String orderProductOptionNo ) throws Exception {  
		// orderOptionNo  = orderProductOptionNo 
		
		OrderConfirmResoponseMsg orderConfirmResult = paTdealDeliveryService.orderConfirmProc(orderProductOptionNo, request);
		
		if ( String.valueOf(HttpStatus.OK.value()).equals(orderConfirmResult.getCode()) ) {
			
			if(!orderConfirmResult.getSoldOutList().isEmpty()) { // 품절 취소 처리 
				paTdealCancelService.soldOutCancelProc(orderConfirmResult.getSoldOutList(), request);
			}
			
			if(!orderConfirmResult.getCancelList().isEmpty()) { // 도서산간/제주 불가 주문건 취소 처리
				paTdealCancelService.cancelApprovalProc(orderConfirmResult.getCancelList(), request);
			}
			
		}
		
		return new ResponseEntity<>(orderConfirmResult, HttpStatus.OK);
		
	}
	
	
	/**
	 * 티딜 상품 준비중 상태 목록을 배송중 상태로 변경하기 (발송처리)
	 * 
	 * @param request
	 * @param orderProductOptionNo
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "발송 처리", notes = "발송 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "발송 처리 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/slip-out-proc")
	public ResponseEntity<?> slipOutProc(HttpServletRequest request,
		    @RequestParam(value = "orderProductOptionNo", required = false	) String orderProductOptionNo ) throws Exception {  
		// orderOptionNo  = orderProductOptionNo 
		
		ResponseMsg result = paTdealDeliveryService.slipOutProc(orderProductOptionNo, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	
	
	/**
	 * 티딜 주문상품 수취확인처리 요청하기
	 * 
	 * @param request
	 * @param procId
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "주문상품 수취확인처리 요청하기", notes = "주문상품 수취확인처리 요청하기", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "수취 확인 처리 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/delivery-complete-proc")
	public ResponseEntity<?> deliveryCompleteProc(HttpServletRequest request,
			@RequestParam(value = "orderProductOptionNo", required = false	) String orderProductOptionNo) throws Exception {  
		// orderOptionNo  = orderProductOptionNo 
		
		ResponseMsg result = paTdealDeliveryService.deliveryCompleteProc(orderProductOptionNo, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	
	private void orderInputMain(HttpServletRequest request) throws Exception {
		String prg_id 			= "PATDEAL_ORDER_INPUT";
		String duplicateCheck 	= "";
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			int limitCount = ConfigUtil.getInt("PA_ORDER_CREATE_CNT");
			//주문 생성 대상 조회
			List<Map<String, String>> orderInputTargetList = paTdealDeliveryService.selectOrderInputTargetList(limitCount);
			
			for( Map<String, String> order : orderInputTargetList) {
				try {
					paTdealAsyncController.orderInputAsync(order, request);
				}catch (Exception e) {
					log.info("{} : {} 제휴주문번호: {} ","티딜 주문생성 오류",order.get("PA_ORDER_NO"), e.getMessage());
					continue;
				}
			}
			
		}catch (Exception e) {
			log.info("{} : {}","티딜 주문생성 오류", e.getMessage());
		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}
	
	/**
	 * 주문 상세 조회
	 * 
	 * @param paCode
	 * @param orderNo
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "주문 상세 조회", notes = "주문 상세 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "주문 목록 조회 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/order-info-detail")
	public ResponseEntity<?> orderInfoDetail( @RequestParam(value = "paCode", required = true) String paCode, 
		    @RequestParam(value = "orderNo", required = true) String orderNo, HttpServletRequest request) throws Exception {  
		
		OrderDetail OrderDetail  = new OrderDetail();
		OrderDetail = paTdealClaimService.orderInfoDetail(paCode, orderNo, request);
			
		return new ResponseEntity<>(OrderDetail,HttpStatus.OK);
		
	}
	
	
}
