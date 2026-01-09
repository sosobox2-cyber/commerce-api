package com.cware.api.pahalf.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.AbstractVO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.CancelInputVO;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.domain.OrderInputVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.pahalf.claim.service.PaHalfClaimService;
import com.cware.netshopping.pahalf.order.service.PaHalfOrderService;
import com.cware.netshopping.pahalf.util.PaHalfComUtill;

import io.swagger.annotations.Api;

@Api(value="/pahalf/async", description="공통")
@Controller("com.cware.api.pahalf.PaHalfAsyncController")
@RequestMapping(value="/pahalf/async")
public class PaHalfAsyncController extends AbstractController {

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paorderService;
	
	@Resource(name = "pahalf.order.paHalfOrderService")
	private PaHalfOrderService paHalfOrderService;
	
	@Resource(name = "com.cware.api.pahalf.PaHalfOrderController")
	private PaHalfOrderController paHalfOrderController;
	
	@Resource(name = "pahalf.claim.paHalfClaimService")
	public PaHalfClaimService paHalfClaimService;
	
	@Resource(name = "pacommon.claim.paclaimService")
	private PaClaimService paclaimService;	
	
	//주문저장
	public void orderInputAsync(Map<String, String> order, HttpServletRequest request) throws Exception {
		List<Map<String, Object>>  orderInputTargetDtList = null;
		String paOrderNo  					= order.get("PA_ORDER_NO");
		OrderInputVO[] orderInputVO 		= null;
		HashMap<String, Object>[] resultMap = null;
		OrderInputVO			  vo		= new OrderInputVO();
		int	index 							= 0;
		ParamMap paramMap					= null;
		String isLocalYn					= "N";
		//가격 Check 
		double c1RsaleAmtSumSk		= 0;  
		double c2RsaleAmtSumSk		= 0;
		double c1RsaleAmtSumHalf 	= 0;
		double c2RsaleAmtSumHalf 	= 0;
		
		
		//로컬 세팅
		if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
			isLocalYn = "Y";
		}
		
		try {
			//=1) SELECT ORDER_DETAIL_INFOMATION
			paramMap = new ParamMap();
			paramMap.put("paOrderNo"		, paOrderNo);
			
			orderInputTargetDtList = paHalfOrderService.selectOrderInputTargetDtList(paramMap);
			if(orderInputTargetDtList == null || orderInputTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectOrderInputTargetDtList size 0" });
			
			//=2) ORDER_DATA SETTING
			orderInputVO = new OrderInputVO[orderInputTargetDtList.size()];
			PaHalfComUtill.replaceCamelList(orderInputTargetDtList);
			for(Map<String, Object> map : orderInputTargetDtList) {
				//BASE ORDER_INFOMATION SETTING
				vo = (OrderInputVO)PaHalfComUtill.map2VO(map, OrderInputVO.class); 

				if(vo.getOrderQty() != 1) {
					orderInputVO[index] = vo;
					throw processException("pa.fail_order_input", new String[] { "수량정보가 잘못됐습니다." }); //하프클럽은 단품이 무조건 1개로 들어와야 함 
				}
				
				//가격비교
				String paApplyDate   = map.get("applyDate").toString(); // TPAGOODSPRICEAPPLY 기준 APPLY_DATE
				String stoaApplyDate = map.get("stoaApplyDate").toString(); // TGOODSPRICE 기준 APPLY_DATE
				String paOrderDate   = map.get("orderDate").toString(); // 하프클럽 주문일시
				
				if(!paApplyDate.equals(stoaApplyDate)) {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					Date orderDate = format.parse(paOrderDate.substring(0, 8));
			        Date stoaDate = format.parse(stoaApplyDate.substring(0, 8));
			        
			        // 스토아 최신 가격 적용일자와 제휴 주문일자 차이 계산
			        long diffDays = orderDate.getTime() - stoaDate.getTime();
			        diffDays = diffDays / (24 * 60 * 60 * 1000);
			        
					if(diffDays >= 3) { // 3일 이상 차이나는 경우
						orderInputVO[index] = vo;
						throw processException("pa.fail_order_input", new String[]{ "가격 정보가 잘못되었습니다." });
					}
				}
				
				//PROMOTION SETTING
				//if(vo.getSalePrice() -  vo.getRsaleAmt() != vo.getSellerDcAmt() + vo.getInstantCouponDiscount()) throw processException("msg.no.select", new String[] { "프로모션 연동이 잘못되었습니다 " });
				if(vo.getSalePrice() - vo.getSellerDcAmt()  != vo.getSalPri()) {
					orderInputVO[index] = vo;
					throw processException("pa.fail_order_input", new String[] { "프로모션 연동이 잘못되었습니다 " });
				}
				
				OrderpromoVO outPromo = setPromo(vo);
				vo.setOrderPaPromo(outPromo);
				
				//=ETC SETTING
				vo.setIsLocalYn	(isLocalYn);
				vo.setProcUser  (Constants.PA_HALF_PROC_ID);
				
				//=ADDR SETTING
				String addr[] = ComUtil.NVL(map.get("receiverAddr")).toString().split("_");
				vo.setStdAddr     	(addr[0]);
				String stdAddrDt = "";
				if(addr.length >= 2) {		
					for(int i = 1; i<addr.length; i++) {
						if(i==1) {
							stdAddrDt = addr[i];
						}else {
							stdAddrDt = stdAddrDt + " " + addr[i];
						}
					}
					vo.setStdAddrDT   	(stdAddrDt);
				}
				
				//고객 전화번호 공백제거 
				String receiverHp = (vo.getReceiverHp() == null ? "" : vo.getReceiverHp());
				String receiverTel = (vo.getReceiverTel() == null ? "" : vo.getReceiverTel());

				vo.setReceiverHp(receiverHp.replace(" ", ""));
				vo.setReceiverTel(receiverTel.replace(" ", ""));
				
				orderInputVO[index] = vo;
				index++;
				
				//가격 점검 Setting
				if("C1".equals(vo.getPaCode()) ) {
					c1RsaleAmtSumHalf = vo.getRsaleAmtSum(); 
					c1RsaleAmtSumSk  += vo.getRsaleAmt();
				}
				if("C2".equals(vo.getPaCode()) ) {
					c2RsaleAmtSumHalf = vo.getRsaleAmtSum();
					c2RsaleAmtSumSk  += vo.getRsaleAmt();
				}
				
			}
			
			if(c2RsaleAmtSumHalf != c2RsaleAmtSumSk ) throw processException("pa.fail_order_input", new String[] { "프로모션 연동이 잘못되었습니다 " });
			if(c1RsaleAmtSumHalf != c1RsaleAmtSumSk ) throw processException("pa.fail_order_input", new String[] { "프로모션 연동이 잘못되었습니다 " });

			//=3) BO 데이터 생성
			resultMap = paorderService.saveOrderTx(orderInputVO);
			
		}catch (Exception e) {
			updatePaOrdermTxForRollback(orderInputVO, e);
			//재고가 없는 경우
			if(PaHalfComUtill.getErrorMessage(e).indexOf("재고가 없습니다") > -1) {
				resultMap = setResultMap( orderInputVO , e);
			}
			
			paramMap = new ParamMap();
			paramMap.put("apiCode"	, "PAHALF_ORDER_INPUT_ASYNC");
			paramMap.put("message"	, "pa_order_no : " + paOrderNo + " > " + PaHalfComUtill.getErrorMessage(e));
		
		}finally {
			saveApiTracking(paramMap, request);
		}
		
		//재고 부족으로 인한 주문 거절(판매자 주문 취소)
		refusalOrder(request, resultMap);
	}
	
	//주문 취소 저장
	@SuppressWarnings("unchecked")
	public void cancelInputAsync(HashMap<String, Object> cancelTargetList, HttpServletRequest request) {
		ParamMap paramMap 	= null;
		int executedRtn 	= 0;
		
		try {
			paramMap = new ParamMap();
			paramMap.setParamMap(cancelTargetList);
			paramMap.replaceCamel();
			
			HashMap<String, Object> cancelDtInfo = paHalfOrderService.selectCancelInputTargetDtList(paramMap);
			if(cancelDtInfo == null) throw processException("msg.no.select", new String[] { "selectCancelInputTargetDtList" });		
			
			String preCancelYn = cancelDtInfo.get("PRE_CANCEL_YN").toString();
			
			switch (preCancelYn) {
			case "0": //취소
				cancelDtInfo = (HashMap<String, Object>) PaHalfComUtill.replaceCamel(cancelDtInfo);
				CancelInputVO cancelInputVO = (CancelInputVO) PaHalfComUtill.map2VO(cancelDtInfo, CancelInputVO.class);
				
				try {
					paorderService.saveCancelTx(cancelInputVO);
					
				} catch (Exception e) {
					CancelInputVO[] cancelInputArray = new CancelInputVO[1];
					cancelInputArray[0] = cancelInputVO;
					updatePaOrdermTxForRollback(cancelInputArray , e); // TPAORDERM.RESULT_CODE 99999 처리
				}
				break;

			default: //기취소
				Map<String, Object> preCanMap = new HashMap<String, Object>();
				preCanMap.put("mappingSeq"		, String.valueOf(cancelDtInfo.get("MAPPING_SEQ")));
				preCanMap.put("preCancelYn"		, preCancelYn);
				preCanMap.put("resultCode"	, Constants.SAVE_SUCCESS);
				preCanMap.put("resultMessage", "기취소(주문생성전 취소)");
				
				executedRtn = paHalfOrderService.updateTPaorderm(preCanMap);
				if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
				
				break;
			}

		}
		catch ( Exception e ) {
			paramMap.put("apiCode"	, "PAHALF_CANCEL_INPUT_A");
			paramMap.put("message"	, "pa_order_no : " + paramMap.getString("paOrderNo") + " > " + PaHalfComUtill.getErrorMessage(e));
		}
		finally {
			saveApiTracking(paramMap, request);
		}
		
	}
	
	//반품 저장
	@SuppressWarnings("unchecked")
	public void orderClaimAsync(HashMap<String, Object> claimMap, HttpServletRequest request) throws Exception {
		HashMap<String, Object> orderClaimTargetDt	= null;
		ParamMap paramMap 							= new ParamMap();
		OrderClaimVO orderClaimVO 					= null;
		orderClaimVO 								= new OrderClaimVO();
		paramMap.setParamMap(claimMap);
		paramMap.replaceCamel();
		
		try {
			//1) 반품 타겟  추출
			if(paramMap.getString("paOrderGb").equals("30")) {
				orderClaimTargetDt = paHalfClaimService.selectOrderClaimTargetDt30(paramMap); 
			} else {
				orderClaimTargetDt = paHalfClaimService.selectOrderClaimTargetDt20(paramMap); //출고전 반품
			}
			if(orderClaimTargetDt == null || orderClaimTargetDt.isEmpty()) throw processException("msg.no.select", new String[] { "selectOrderCalimTargetDt30(20)List" });
			
			//2) 반품 VO 데이터 생성
			orderClaimTargetDt = (HashMap<String, Object>) PaHalfComUtill.replaceCamel(orderClaimTargetDt);
			orderClaimVO	   = (OrderClaimVO) PaHalfComUtill.map2VO(orderClaimTargetDt, OrderClaimVO.class);
			
			//3) 추가정보 세팅
			setAddInfoOrderClaimVO(orderClaimTargetDt , orderClaimVO);
			
			paclaimService.saveOrderClaimTx(orderClaimVO);
		
		}catch(Exception e) {
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO - " + paramMap.getString("paOrderNo") + " > " + PaHalfComUtill.getErrorMessage(e);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PAHALF_ORDER_CLAIM_A");
			
			//2) TPAORDERM Update
			OrderClaimVO[] orderClaimList = new OrderClaimVO[1];
			orderClaimList[0] 			  = orderClaimVO;
			updatePaOrdermTxForRollback(orderClaimList, e);
			
		}finally {
			saveApiTracking(paramMap, request);
		}
		
	}

	//반품취소 저장
	@SuppressWarnings("unchecked")
	public void claimCancelAsync(HashMap<String, Object> claimMap, HttpServletRequest request) throws Exception {
		
		HashMap<String, Object> cancelInputTargetDt = null;
		ParamMap paramMap 					 		= new ParamMap();
		OrderClaimVO orderClaimVO 			 		= null;
		int executedRtn 							= 0;
		paramMap.setParamMap(claimMap);
		paramMap.replaceCamel();		
		
		try {
			cancelInputTargetDt = paHalfClaimService.selectClaimCancelTargetDt(paramMap); 
			if(cancelInputTargetDt == null || cancelInputTargetDt.isEmpty()) throw processException("msg.no.select", new String[] { "selectClaimCancelTargetDtList" });
						
			switch(cancelInputTargetDt.get("PRE_CANCEL_YN").toString()){
			
				case "0": //일반적인 반품 취소
					cancelInputTargetDt = (HashMap<String, Object>) PaHalfComUtill.replaceCamel(cancelInputTargetDt);
					orderClaimVO	   = (OrderClaimVO) PaHalfComUtill.map2VO(cancelInputTargetDt, OrderClaimVO.class);
					//3) 추가정보 세팅
					setAddInfoOrderClaimVO(cancelInputTargetDt , orderClaimVO);
					
					//반품취소 데이터 생성
					paclaimService.saveClaimCancelTx(orderClaimVO);	
					break;
				
				case "1": //기취소
					Map<String, Object> preCanMap = new HashMap<String, Object>();
					preCanMap.put("mappingSeq"			, String.valueOf(cancelInputTargetDt.get("MAPPING_SEQ")));
					preCanMap.put("preCancelYn"			, "1");
					preCanMap.put("resultCode"		, Constants.SAVE_SUCCESS);
					preCanMap.put("resultMessage"	, "기취소(반품 생성전 취소)");
					
					paHalfOrderService.updateTPaorderm(preCanMap);
					if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
					break;
			}
			
		}catch(Exception e){
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO - " + paramMap.getString("paOrderNo") + " > " + PaHalfComUtill.getErrorMessage(e);
			paramMap.put("code"		, "500");
			paramMap.put("apiCode"  , "PAHALF_CLAIM_CANCEL_ASYNC");
			paramMap.put("message" 	, message);
			
			//2) TPAORDERM Update
			OrderClaimVO[] orderClaimList = new OrderClaimVO[1];
			orderClaimList[0] = orderClaimVO;
			updatePaOrdermTxForRollback(orderClaimList, e);
		}finally{
			saveApiTracking(paramMap, request);
		}	
	}

	//교환 저장
	@SuppressWarnings("unchecked")
	public void orderChangeAsync(HashMap<String, Object> exchangeMap, HttpServletRequest request) throws Exception {
		HashMap<String, Object>[] resultMap 					= null;
		List<HashMap<String,Object>> orderChangeTargetDtList	= null;
		OrderClaimVO orderClaimVO 								= null;
		OrderClaimVO[] orderClaimVOArray 						= null;
		int index 												= 0;
		int targetSize 											= 0;
		ParamMap paramMap 										= new ParamMap();
		paramMap.setParamMap(exchangeMap);
		paramMap.replaceCamel();

		try {
			orderChangeTargetDtList = paHalfClaimService.selectOrderChangeTargetDtList(paramMap); 
			targetSize = orderChangeTargetDtList.size();
			if(targetSize < 2) throw processException("msg.no.select", new String[] { "selectOrderChangeTargetDtList" });
			
			orderClaimVOArray = new OrderClaimVO[orderChangeTargetDtList.size()];

			for(HashMap<String,Object> orderchange : orderChangeTargetDtList) {
				
				orderchange 	= (HashMap<String, Object>) PaHalfComUtill.replaceCamel(orderchange);
				orderClaimVO	= (OrderClaimVO) PaHalfComUtill.map2VO(orderchange, OrderClaimVO.class);
				//3) 추가정보 세팅
				setAddInfoOrderClaimVO(orderchange , orderClaimVO);
				
				orderClaimVOArray[index] = orderClaimVO;
				index ++;
			}
			
			resultMap = paclaimService.saveOrderChangeTx(orderClaimVOArray);
						
			for(HashMap<String, Object> result : resultMap) {
				result = (HashMap<String, Object>) PaHalfComUtill.replaceCamel(result);
				if(!"100001".equals(result.get("resultCode"))) continue;
			
				result.put("changeFlag", "06");
				paHalfOrderService.updateTPaorderm(result);  // TPAORDERM.CHANGE_FLAG = 06(교환불가OB처리) UPDATE
			}
		}catch(Exception e){			
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO - " + paramMap.getString("paOrderNo") + " > " + PaHalfComUtill.getErrorMessage(e);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PAHALF_ORDER_CHANGE_ASYNC");
			//2) UPDATE TPAORDERM
			updatePaOrdermTxForRollback(orderClaimVOArray, e);
		}finally{
			saveApiTracking(paramMap, request);			
		}
	}

	//교환취소 저장
	@SuppressWarnings("unchecked")
	public void changeCancelAsync(HashMap<String, Object> cancelMap, HttpServletRequest request) throws Exception {
		List<HashMap<String,Object>> changeCancelTargetDtList	= null;
		OrderClaimVO orderClaimVO 								= null;
		int index 												= 0;
		OrderClaimVO[] orderClaimVOArray 						= null;
		boolean	preCanYn										= false;
		ParamMap paramMap 										= new ParamMap();
		int executedRtn											= 0;
		paramMap.setParamMap(cancelMap);
		paramMap.replaceCamel();
		
		try {
			changeCancelTargetDtList = paHalfClaimService.selectChangeCancelTargetDtList(paramMap);
			if(changeCancelTargetDtList.size() < 2) throw processException("msg.no.select", new String[] { "selectChangeCancelTargetDtList" });
			
			orderClaimVOArray = new OrderClaimVO[changeCancelTargetDtList.size()];
			
			for(HashMap<String,Object> changecancel : changeCancelTargetDtList) {

				switch(changecancel.get("PRE_CANCEL_YN").toString()){
					case "0": //일반 취소
						changecancel 	= (HashMap<String, Object>) PaHalfComUtill.replaceCamel(changecancel);
						orderClaimVO	= (OrderClaimVO) PaHalfComUtill.map2VO(changecancel, OrderClaimVO.class);
						//3) 추가정보 세팅
						setAddInfoOrderClaimVO(changecancel , orderClaimVO);
						orderClaimVOArray[index] = orderClaimVO;
						preCanYn = false;
						break;
						
					case "1": //기취소
						
						Map<String, Object> preCanMap = new HashMap<String, Object>();
						preCanMap.put("mappingSeq"			, String.valueOf(changecancel.get("MAPPING_SEQ")));
						preCanMap.put("preCancelYn"			, "1");
						preCanMap.put("resultCode"		, Constants.SAVE_SUCCESS);
						preCanMap.put("resultMessage"	, "기취소(교환 생성전 취소)");
						
						paHalfOrderService.updateTPaorderm(preCanMap);
						if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
						
						preCanYn = true;
						break;
				}
				index++;
			}
			
			if(!preCanYn){
				paclaimService.saveChangeCancelTx(orderClaimVOArray);
			}
			
		}catch(Exception e){			
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paramMap.getString("paOrderNo") + " > " + PaHalfComUtill.getErrorMessage(e);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PAHALF_CHANGE_CANCEL_ASYNC");
			//2) UPDATE TPAORDERM
			updatePaOrdermTxForRollback(orderClaimVOArray, e);	
		}finally{
			saveApiTracking(paramMap, request);
		}		
	}
	
	private void setAddInfoOrderClaimVO(HashMap<String, Object> orderClaimTargetDt, OrderClaimVO orderClaimVO) throws Exception { 
		//중복 주소 검증
		if("30".equals(orderClaimVO.getClaimGb()) || "40".equals(orderClaimVO.getClaimGb()) || "45".equals(orderClaimVO.getClaimGb())){
			String checkAddr = paHalfClaimService.compareAddress(orderClaimTargetDt);
			orderClaimVO.setSameAddr(checkAddr);

	        //=ADDR SETTING
	        String addr[] = ComUtil.NVL(orderClaimVO.getRcvrBaseAddr()).split("_");
	        orderClaimVO.setRcvrBaseAddr  (addr[0]);
	        
	        String rcvrDtlsAddr = "";
			if(addr.length >= 2) {		
				for(int i = 1; i<addr.length; i++) {
					if(i==1) {
						rcvrDtlsAddr = addr[i];
					}else {
						rcvrDtlsAddr = rcvrDtlsAddr + " " + addr[i];
					}
				}
				orderClaimVO.setRcvrDtlsAddr   	(rcvrDtlsAddr);
			}
		}
		
		//고객 전화번호 공백제거
		String receiverHp = (orderClaimVO.getReceiverHp() == null ? "" : orderClaimVO.getReceiverHp());
		String receiverTel = (orderClaimVO.getReceiverTel() == null ? "" : orderClaimVO.getReceiverTel());

		orderClaimVO.setReceiverHp(receiverHp.replace(" ", ""));
		orderClaimVO.setReceiverTel(receiverTel.replace(" ", ""));
		
		String cLaimCode = orderClaimTargetDt.get("claimCode").toString();
		if (cLaimCode.length() ==6){
			orderClaimVO.setCsLgroup		(cLaimCode.substring(0,2));
			orderClaimVO.setCsMgroup		(cLaimCode.substring(2,4));
			orderClaimVO.setCsSgroup		(cLaimCode.substring(4,6));
			orderClaimVO.setCsLmsCode		(cLaimCode);		
			orderClaimVO.setStandardType	("0");     //// 기준내 :1 ,기준외 : 0
		}
		
		String shpChargeYn  = String.valueOf( orderClaimTargetDt.get("clmLstDlvCst").toString());  
		
		/*if("1".equals(shpChargeYn)) { //배송비 중복 부과 방지
			shpChargeYn = paHalfClaimService.checkShpFeeYn(orderClaimTargetDt);
		}*/
		
		if ("1".equals(shpChargeYn)) {
			orderClaimVO.setShpfeeYn		("1");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
			orderClaimVO.setShipcostChargeYn("1"); 
		} else {
			orderClaimVO.setShpfeeYn		("0");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2
			orderClaimVO.setShipcostChargeYn("0"); 
		}
			
		//출하지시 이후 취소건은 반품인데, 이경우 배송비 부과여부를 무상으로 처리한다.
		if("1".equals(orderClaimVO.getOutBefClaimGb()) && "20".equals(orderClaimVO.getClaimGb())){
			orderClaimVO.setShpfeeYn		("0");
			orderClaimVO.setShipcostChargeYn("0"); 
			orderClaimVO.setShpFeeAmt		(0L);
			orderClaimVO.setIs20Claim		(true);
		}else{
			orderClaimVO.setIs20Claim		(false);
		}
	}
	
	private OrderpromoVO setPromo(OrderInputVO vo) {
        OrderpromoVO orderPaPromo = new OrderpromoVO();
        orderPaPromo.setPromoNo			(vo.getPromoNo());
        orderPaPromo.setDoType			("30");
        orderPaPromo.setDoAmt			(vo.getDoAmt());
        orderPaPromo.setProcCost		(vo.getDoAmt());
        orderPaPromo.setOwnProcCost		(vo.getOwnCost());
        orderPaPromo.setEntpProcCost	(vo.getEntpCost());
        orderPaPromo.setCouponPromoBdate(vo.getCouponPromoBdate());
        orderPaPromo.setCouponPromoEdate(vo.getCouponPromoEdate());
        orderPaPromo.setCouponYn		("1");
        orderPaPromo.setProcGb			("I");
        
        return orderPaPromo;
	}

	private void refusalOrder(HttpServletRequest request, HashMap<String, Object>[] resultMap) throws Exception {
		if(resultMap == null) return;
		
		ResponseEntity<?> responseMsg  = null;
		String mappingSeq	= "";
		String memo			="재고없음으로 인한 판매자 취소";
		String status		= "58"; //58:품절취소, 59:자동품절(출고지연 D+7)
		String preCancelYn	= "1";
		String resultCode	= "";
		String resultMsg	= "";
		
		
		for(HashMap<String ,Object> map :  resultMap) {
			if( !("100001").equals(map.get("RESULT_CODE"))) continue; //100001 - 재고없음
					
			try {
				mappingSeq = String.valueOf(map.get("MAPPING_SEQ"));
				responseMsg  = paHalfOrderController.refusalOrder(request , mappingSeq, memo, status);
				
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")) {
					throw new Exception("판매자 주문 취소 실패 + " +  PropertyUtils.describe(responseMsg.getBody()).get("message") );
				}
				resultCode  = Constants.SAVE_SUCCESS;
				resultMsg 	= "판매자 주문 취소";
				preCancelYn = "1";
				
			}catch (Exception e) {
				resultCode  = Constants.SAVE_FAIL;
				resultMsg 	= PaHalfComUtill.getErrorMessage(e);
				preCancelYn = "0";
			}finally {
				Map<String, Object> preCanMap = new HashMap<String, Object>();
				preCanMap.put("mappingSeq"			, mappingSeq);
				preCanMap.put("preCancelYn"			, preCancelYn);
				preCanMap.put("resultCode"		, resultCode);
				preCanMap.put("resultMessage"	, resultMsg);
				
				paHalfOrderService.updateTPaorderm(preCanMap);
			}
			
		}
	}
	
	private void saveApiTracking(ParamMap paramMap , HttpServletRequest request) {
		if(paramMap == null) return;
		if(paramMap.getString("apiCode").equals("")) return;
		
		try{
			paramMap.put("startDate"	, systemService.getSysdatetimeToString());
			paramMap.put("code"			, "500");
			paramMap.put("siteGb"		, Constants.PA_HALF_PROC_ID);
			systemService.insertApiTrackingTx(request, paramMap);
			
		}catch(Exception ee){
			log.error("ApiTracking Insert Error : "+ee.toString());
		}
	}
	
	private void updatePaOrdermTxForRollback(AbstractVO[] aVO, Exception e){
		
		if(aVO == null || aVO.length < 1 || aVO[0] == null)  return;
		
		ParamMap paramMap 	= null;
		int excuteCnt 		= 0;
		
		for(int j = 0; aVO.length > j; j++){
			paramMap = new ParamMap();
			
			if(aVO instanceof OrderInputVO[]){
				OrderInputVO[] orderInput = (OrderInputVO[]) aVO;
				paramMap.put("mappingSeq", orderInput[j].getMappingSeq());
			}else if(aVO instanceof OrderClaimVO[]){
				OrderClaimVO[] orderClaim = (OrderClaimVO[]) aVO;
				paramMap.put("mappingSeq", orderClaim[j].getMappingSeq());
			}else if(aVO instanceof CancelInputVO[]){
				CancelInputVO[] orderCancel = (CancelInputVO[]) aVO;
				paramMap.put("mappingSeq", orderCancel[j].getMappingSeq());
			}
			
			String errMsg = PaHalfComUtill.getErrorMessage(e);
			paramMap.put("resultCode"		, "999999");
			paramMap.put("resultMessage"	, errMsg.length() > 1950 ? errMsg.substring(0,1950) : errMsg);
			paramMap.put("createYn"			, "0");
			
			try {
				excuteCnt = paorderService.updatePaOrdermTx(paramMap);
				if(excuteCnt != 1){
					log.error(paramMap.getString("mappingSeq") + " Update ERROR - Result Code 999999 in TPAORDERM");
				}
			} catch (Exception e1) {
				log.error(paramMap.getString("mappingSeq") + " Update ERROR - Result Code 999999 in TPAORDERM");
				continue;
			}
		}//end of for
	}

	@SuppressWarnings({ "unchecked" })
	private HashMap<String, Object>[] setResultMap(OrderInputVO[] orderInputVO,   Exception e) {
		int i 	 = 0;
		int cnt  = 0;
		String errGoodsdtCode 	= "";
		String errGoodsCode		= "";
		
		//에러메세지 정제 //추후에 좋은 방식으로 변경 필요
		String errMsg = PaHalfComUtill.getErrorMessage(e);
		String stkMsg = getMessage("errors.nodata.stock").replace("[{0}]", "").trim();
		String ordMsg = getMessage("pa.fail_order_input").replace("{0}", "").trim();
		errMsg = errMsg.replace(ordMsg, "").replace("[", "").replace("]", "").trim();
		errMsg = errMsg.replace(stkMsg, "").trim();// ex) 200001/001
		
		
		String errGoodsCodes[] = errMsg.split("/");
		errGoodsCode   = errGoodsCodes[0];
		if(errGoodsCodes.length > 1) errGoodsdtCode = errGoodsCodes[1];
		
		
		for(OrderInputVO vo : orderInputVO){
			if(!vo.getGoodsCode().equals(errGoodsCode)) continue;
			if(errGoodsdtCode != null && !"".equals(errGoodsdtCode) && !vo.getGoodsdtCode().equals(errGoodsdtCode)) continue;
			cnt ++;
		}
		
		HashMap<String, Object>[] resultMap = new HashMap[cnt];
		
		for(OrderInputVO vo : orderInputVO){
			if(!vo.getGoodsCode().equals(errGoodsCode)) continue;
			if(errGoodsdtCode != null && !"".equals(errGoodsdtCode) && !vo.getGoodsdtCode().equals(errGoodsdtCode)) continue;
			resultMap[i] = new HashMap<>();
			resultMap[i].put("MAPPING_SEQ"		, 	vo.getMappingSeq());
			resultMap[i].put("RESULT_CODE"		, 	"100001"); 
			resultMap[i].put("RESULT_MESSAGE"	, 	"재고 부족으로 인한 판매자 취소");
			i++;
		}
		
		return resultMap;
	}

	
}