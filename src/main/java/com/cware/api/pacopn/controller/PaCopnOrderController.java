package com.cware.api.pacopn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.framework.core.basic.AbstractController;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.pacopn.order.service.PaCopnOrderService;

@Api(value = "/pacopn/order", description = "공통")
@Controller("com.cware.api.pacopn.PaCopnOrderController")
@RequestMapping(value = "/pacopn/order")
public class PaCopnOrderController extends AbstractController{
	
	@Resource(name = "com.cware.api.pacopn.paCopnAsyncController")
	private PaCopnAsyncController paCopnAsyncController;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pacopn.order.paCopnOrderService")
	private PaCopnOrderService paCopnOrderService;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "쿠팡 주문접수", notes = "쿠팡 주문접수", httpMethod = "GET")
	@RequestMapping(value = "/order-input", method = RequestMethod.GET)
	@ResponseBody
	public void orderInputMain(HttpServletRequest request) throws Exception{
		List<Object> orderInputTargetList        = null;
		HashMap<String, String> orderInputTarget = null;
		String promoAllowTerm = ComUtil.NVL(systemService.getValRealTime("PAPROMO_ALLOW_TERM") , "0.1" );	// 프로모션 연동 종료 건 조회 허용 시간 
		
		
		String dupCheck = "";
		String prg_id    = "PACOPN_ORDER_INPUT";
		
		log.info("=========================== COPN Order Create Start =========================");
		try{
			dupCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{prg_id});
			
			orderInputTargetList = paCopnOrderService.selectOrderInputTargetList(ConfigUtil.getInt("PA_ORDER_CREATE_CNT"));
			
			for(int i=0; i<orderInputTargetList.size(); i++){
				try{
					orderInputTarget = (HashMap<String, String>) orderInputTargetList.get(i);
					orderInputTarget.put("PAPROMO_ALLOW_TERM", promoAllowTerm);
					
					paCopnAsyncController.orderInputAsync(orderInputTarget, request);
				}catch(Exception e){
					continue;
				}
			}
		}catch(Exception e){
			log.error("error msg: " + e.getMessage());
		}finally{
			try {
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", prg_id);
				}
			}catch(Exception e){
				log.error("[CloseHistoryTx Error] " + e.getMessage());
			}
			log.info("=========================== COPN Order Create  End  =========================");
		}
	}
	
}
