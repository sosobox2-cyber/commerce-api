package com.cware.api.papreoutorder.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.papreoutorder.service.PaPreoutOrderService;
import com.cware.framework.core.basic.AbstractController;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.pahalf.util.PaHalfComUtill;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/papreoutorder", description="기출하 주문 생성")
@Controller("com.cware.api.papreoutorder.controller")
@RequestMapping(value="/papreoutorder")
public class PaPreoutOrderController extends AbstractController {
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paOrderService;

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "papreoutorder.papreoutorderService")
	private PaPreoutOrderService paPreoutOrderService;
	
	/**
	 * 기출하 주문 생성
	 * 
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "기출하 주문 생성", notes = "기출하 주문 생성", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/preout-order", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> preoutOrder(HttpServletRequest request) throws Exception {  
		ResponseMsg result = null;
		String prg_id 			= "PA_PREOUT_ORDER_INPUT";
		String duplicateCheck 	= "";
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			// 기출하 대상 조회
			List<Map<String, String>> preoutOrderTargetList = paOrderService.selectPreoutOrderTargetList();
			
			for( Map<String, String> order : preoutOrderTargetList) {
				try {
					paPreoutOrderService.preoutOrderInput(order, request);
				}catch (Exception e) {
					log.info("{} : {} 제휴주문번호: {} ","기출하 주문생성 오류",order.get("PA_ORDER_NO"), PaHalfComUtill.getErrorMessage(e));
					continue;
				}
			}
			
		} catch (Exception e) {
			log.info("{} : {}","기출하 주문생성 오류", PaHalfComUtill.getErrorMessage(e));

		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	
}