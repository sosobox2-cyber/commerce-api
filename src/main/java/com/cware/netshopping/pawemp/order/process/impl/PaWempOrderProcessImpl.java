package com.cware.netshopping.pawemp.order.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.cware.api.pawemp.controller.PaWempGoodsController;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaWempDeliveryVO;
import com.cware.netshopping.domain.PaWempTargetVO;
import com.cware.netshopping.domain.PawemporderlistVO;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaWempClaimItemList;
import com.cware.netshopping.domain.model.PaWempClaimList;
import com.cware.netshopping.domain.model.PaWempOrderItemList;
import com.cware.netshopping.domain.model.PaWempOrderList;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pawemp.claim.model.SetClaimApprove;
import com.cware.netshopping.pawemp.common.enums.WempCode;
import com.cware.netshopping.pawemp.common.model.ReturnData;
//import com.cware.netshopping.pawemp.common.model.SetParcelDelivery;
import com.cware.netshopping.pawemp.common.repository.PaWempCommonDAO;
import com.cware.netshopping.pawemp.common.service.PaWempApiService;
import com.cware.netshopping.pawemp.goods.repository.PaWempGoodsDAO;
import com.cware.netshopping.pawemp.goods.service.PaWempGoodsService;
import com.cware.netshopping.pawemp.order.model.SetClaimRejectRequest;
import com.cware.netshopping.pawemp.order.model.SetOrderCancelRequest;
import com.cware.netshopping.pawemp.order.model.SetOrderDeliveryRequest;
import com.cware.netshopping.pawemp.order.process.PaWempOrderProcess;
import com.cware.netshopping.pawemp.order.repository.PaWempOrderDAO;

@Service("pawemp.order.paWempOrderProcess")
public class PaWempOrderProcessImpl extends AbstractService implements PaWempOrderProcess {
	
	@Resource(name = "pawemp.common.paWempApiService")
	public PaWempApiService paWempApiService;
	
	@Resource(name = "pawemp.order.paWempOrderDAO")
	private PaWempOrderDAO paWempOrderDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;

	@Resource(name = "pawemp.common.paWempCommonDAO")
	private PaWempCommonDAO paWempCommonDAO;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "com.cware.api.pawemp.PaWempGoodsController")
    private PaWempGoodsController paWempGoodsController;
	
	@Resource(name = "pawemp.goods.paWempGoodsService")
	public PaWempGoodsService paWempGoodsService;
	
	@Resource(name = "pawemp.goods.paWempGoodsDAO")
	private PaWempGoodsDAO paWempGoodsDAO;
	
	@Override
	public String savePaWempOrder(PawemporderlistVO paWempOrderList, List<PaWempOrderItemList> paWempOrderItemList) throws Exception{
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		int existsCnt   = 0;
		int executedRtn = 0;
		
		existsCnt = paWempOrderDAO.selectPaWempOrderListExists(paWempOrderList);
		if(existsCnt > 0){ return Constants.SAVE_FAIL; }
		
		// 1. TPAWEMPORDERLIST insert
		executedRtn = paWempOrderDAO.insertPaWempOrderList(paWempOrderList);
		if(executedRtn < 1){
			log.info("savePaWempOrder insertPaWempOrderList fail");
			throw processException("msg.cannot_save", new String[]{"insertPaWempOrderList INSERT"});
		}
		
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		for(PaWempOrderItemList orderItem : paWempOrderItemList) {
			// 2. TPAWEMPORDERITEMLIST insert
			executedRtn = paWempOrderDAO.insertPaWempOrderItemList(orderItem);
			if(executedRtn < 1){
				log.info("savePaWempOrder insertPaWempOrderList fail");
				throw processException("msg.cannot_save", new String[] { "insertPaWempOrderItemList fail" });
			}
			
			// 3. TPAORDERM insert
			Paorderm paorderm = new Paorderm();
			paorderm.setPaOrderGb("10");
			paorderm.setPaCode(paWempOrderList.getPaCode());
			paorderm.setPaOrderNo(orderItem.getPaOrderNo());
			paorderm.setPaOrderSeq(orderItem.getPaOrderSeq());
			paorderm.setPaShipNo(orderItem.getPaShipNo());
			//이건 몬짓거리지?
			if(orderItem.getOptionQty() < 1) {
				paorderm.setPaProcQty(Long.toString(orderItem.getProductQty()));
			} else {
				paorderm.setPaProcQty(Long.toString(orderItem.getOptionQty()));
			}
			paorderm.setPaDoFlag("30");
			paorderm.setOutBefClaimGb("0");
			paorderm.setPaGroupCode  ("06");
			paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setModifyId(Constants.PA_WEMP_PROC_ID);
			
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
			if(executedRtn < 1){
				throw processException("msg.cannot_save", new String[]{"TPAORDERM INSERT"});
			}
		}
		return rtnMsg;
	}

	@Override
	public List<PaWempTargetVO> selectOrderInputTargetList(int targetCnt) throws Exception {
		ParamMap paramMap = new ParamMap();
		
		// 1. 이미 TPAORDERM에 입력된 주문인 경우 중복으로 판단하여 skip
		paramMap.put("paOrderCreateCnt", targetCnt);
		
		return paWempOrderDAO.selectOrderInputTargetList(paramMap);
	}
	
	@Override
	public List<Object> selectOrderInputTargetDtList (ParamMap paramMap) throws Exception {
		return paWempOrderDAO.selectOrderInputTargetDtList(paramMap);
	}
	
	@Override
	public PaWempTargetVO selectRefusalInfo(String mappingSeq) throws Exception {
		return paWempOrderDAO.selectRefusalInfo(mappingSeq);
	}
	
	@Override
	public ParamMap orderRefusalProc(PaWempTargetVO targetVo) throws Exception {
		ParamMap resultMap = new ParamMap();
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = null;
		String apiCode = "IF_PAWEMPAPI_03_006";
		String paName = targetVo.getPaCode().equals(Constants.PA_WEMP_BROAD_CODE) ? Constants.PA_BROAD : Constants.PA_ONLINE;

		paramMap.put("apiCode"   , apiCode);
		paramMap.put("broadCode" , Constants.PA_WEMP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
		
		try {			
			log.info("01.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			log.info("02.주문취소 API 전송");			
			SetOrderCancelRequest cancelReq = new SetOrderCancelRequest();
			cancelReq.setOrderOptionNo(Long.parseLong(targetVo.getOrderOptionNo()));
			ReturnData returnData = (ReturnData)paWempApiService.callWApiObejct(apiInfo, "POST", cancelReq, ReturnData.class, paName);
			if(returnData.getReturnKey() == 1) { //성공
				log.info("주문취소 API 전송성공 결과 returnKey:1, returnMsg:"+returnData.getReturnMsg());
				resultMap.put("result_code","1");
				resultMap.put("result_text","OK");
				
				// 해당 옵션 품절처리
				if(!"06".equals(targetVo.getOrderCancelYn())) {
					ParamMap param = new ParamMap();		
					
					param.put("apiCode", "IF_PAWEMPAPI_01_010");
					param.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
					param.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
				    param.put("paName", paName);
					param.put("goodsCode", targetVo.getGoodsCode());
					param.put("goodsdtCode", targetVo.getGoodsdtCode());
					param.put("paCode", targetVo.getPaCode());
				    param.put("stopSale", "Y");
				    
				    String transYn = paWempOrderDAO.selectSoldoutTransYn(param);
				    
				    if("0".equals(transYn)) { // 재고 품절처리 필요한 경우
					    List<PaGoodsdtMapping> goodsdtMapping = paWempGoodsService.selectPaWempGoodsdtInfoList(param);
					    
					    if(goodsdtMapping.size() > 0) {
							HashMap<String, String> goodsApiInfo = new HashMap<String, String>();
							
						    goodsApiInfo = systemService.selectPaApiInfo(param);
						    goodsApiInfo.put("apiInfo", "IF_PAWEMPAPI_01_010");
							
						    // 상품 재고 수정 통신
							ReturnData goodsReturnData = (ReturnData) paWempApiService.callWApiObejct(goodsApiInfo, "POST", paWempGoodsController.updateOptionObject(targetVo.getProductNo(), goodsdtMapping), ReturnData.class, param.getString("paName"));
						
							if(goodsReturnData != null && "완료 되었습니다.".equals(goodsReturnData.getReturnMsg())){
								String dateTime = systemService.getSysdatetimeToString();
								
								for(PaGoodsdtMapping optionItem : goodsdtMapping){
									optionItem.setModifyId("PAWEMP");
									optionItem.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
									optionItem.setRemark1("stopSale"); // 판매취소에서만 (일반 재고수정 X)
									
									if(targetVo.getGoodsdtCode().equals(optionItem.getGoodsdtCode()) || "1".equals(optionItem.getTransStockYn())) { // 품절처리한 단품일 경우
										optionItem.setTransStockYn("1");
									} else {
										optionItem.setTransStockYn("0");
									}
									
									int executedRtn = paWempGoodsDAO.updatePaWempGoodsdtOrderAbleQty(optionItem);
									if (executedRtn < 0) {
										log.info("TPAGOODSDTMAPPING UPDATE FAIL");
										throw processException("msg.cannot_save", new String[]{"TPAGOODSDTMAPPING UPDATE"});
									}
								}
							}
					    }
					}
				}
			} else {
				log.info("주문취소 API 전송실패 결과 returnKey:"+returnData.getReturnKey()+", returnMsg:"+returnData.getReturnMsg());
				resultMap.put("result_code","0");
				resultMap.put("result_text", returnData.getReturnMsg());
			}
		}catch(Exception e){
			resultMap.put("result_code", "-9999");
			resultMap.put("result_text", e.getMessage());
		}
		
		return resultMap;
	}
	
	@Override
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception {
		return paWempOrderDAO.updatePreCancelYn(preCancelMap);
	}
	
	@Override
	public List<Object> selectSlipOutProcList() throws Exception {
		return paWempOrderDAO.selectSlipOutProcList();
	}
	
	@Override
	public ParamMap slipOutProc(HashMap<String, Object> deliveryVo, HashMap<String, String> apiInfo) throws Exception {
		ParamMap resultMap = new ParamMap();
		String paName = deliveryVo.get("PA_CODE").toString().equals(Constants.PA_WEMP_BROAD_CODE) ? Constants.PA_BROAD : Constants.PA_ONLINE;
		
		try {
			log.info("01.주문관리-발송처리 API 전송");
			SetOrderDeliveryRequest deliveryReq = new SetOrderDeliveryRequest();
			deliveryReq.setBundleNo(Long.parseLong(deliveryVo.get("PA_SHIP_NO").toString()));
			
			if( StringUtils.equals("DIRECT", deliveryVo.get("PA_DELY_GB").toString())    //기타택배(자체배송) CASE 
			 || StringUtils.equals("DIRECT", deliveryVo.get("INVOICE_NO").toString())) { //배송완료면서 SLIP_NO가 없는 CASE
				deliveryReq.setShipMethod("DIRECT");
				deliveryReq.setScheduleShipDate(deliveryVo.get("SCHEDULE_SHIP_DATE").toString());
				if(!StringUtils.equals("1", deliveryVo.get("DELY_HOPE_YN").toString())) {
					log.info("slipOutProc No TSLIPM.DELY_HOPE_YN setting");
				}
			
			} else {
				deliveryReq.setShipMethod("PARCEL");
				deliveryReq.setParcelCompanyCode(deliveryVo.get("PA_DELY_GB").toString());
				deliveryReq.setInvoiceNo(deliveryVo.get("INVOICE_NO").toString());
			}
		
			ReturnData returnData = (ReturnData)paWempApiService.callWApiObejct(apiInfo, "POST", deliveryReq, ReturnData.class, paName);
			if(returnData.getReturnKey() == 1) { //성공
				log.info("발송처리 API 전송성공 결과 returnKey:1, returnMsg:"+returnData.getReturnMsg());
				resultMap.put("result_code","1");
				resultMap.put("result_text","OK");
			} else if(StringUtils.contains(returnData.getReturnMsg(), "이미")){
				resultMap.put("result_code","1");
				resultMap.put("result_text","Already Delivery");
			} else {
				log.info("발송처리 API 전송실패 결과 returnKey:"+returnData.getReturnKey()+", returnMsg:"+returnData.getReturnMsg());
				resultMap.put("result_code","0");
				resultMap.put("result_text", returnData.getReturnMsg());
			}
		}catch(Exception e){
			resultMap.put("result_code", "-9999");
			resultMap.put("result_text", e.getMessage());
		}
		
		return resultMap;
	}

	@Override
	public List<Object> selectSlipOutProcDtList (String paShipNo) throws Exception {
		return paWempOrderDAO.selectSlipOutProcDtList (paShipNo);
	}
	
	@Override
	public int updateSlipOutProc(HashMap<String, Object> deliveryVo) throws Exception {	
		ParamMap paramMap = new ParamMap();
		
		paramMap.put("mappingSeq", deliveryVo.get("MAPPING_SEQ").toString());
		paramMap.put("modifyId", Constants.PA_WEMP_PROC_ID);
		
		return paWempOrderDAO.updateSlipOutProc(paramMap);
	}
	
	@Override
	public int updateSlipOutProcFail(HashMap<String, Object> deliveryVo, String apiMsg) throws Exception {
		ParamMap paramMap = new ParamMap();
		
		paramMap.put("mappingSeq", deliveryVo.get("MAPPING_SEQ").toString());
		paramMap.put("apiMsg"  , apiMsg);
		paramMap.put("modifyId", Constants.PA_WEMP_PROC_ID);
		
		return paWempOrderDAO.updateSlipOutProcFail(paramMap);
	}
	
	@Override
	public int updateInvoiceInfo(HashMap<String, Object> deliveryVo, String deliveryStatus) throws Exception {
		PaWempOrderList paWempOrderList = new PaWempOrderList();
		
		paWempOrderList.setPaOrderNo(deliveryVo.get("PA_ORDER_NO").toString());
		paWempOrderList.setPaShipNo(deliveryVo.get("PA_SHIP_NO").toString());
		paWempOrderList.setInvoiceNo(deliveryVo.get("INVOICE_NO").toString());
		paWempOrderList.setDelyComp(deliveryVo.get("PA_DELY_GB").toString());
		paWempOrderList.setDeliveryStatus(deliveryStatus);
		paWempOrderList.setModifyId(Constants.PA_WEMP_PROC_ID);
		
		if(StringUtils.equals("DIRECT", deliveryVo.get("PA_DELY_GB").toString())) {
			paWempOrderList.setDeliveryMethod("02"); //[O724] 02:일반-직접배송
		} else {
			paWempOrderList.setDeliveryMethod("01"); //[O724] 01:일반-택배배송
		}
		
		return paWempOrderDAO.updateInvoiceInfo(paWempOrderList);
	}
	
	@Override
	public String updateDeliveryComplete(Paorderm paOrderm) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int excuteCnt = 0;
		
		excuteCnt = paWempOrderDAO.updateDeliveryComplete(paOrderm);
		if(excuteCnt < 1) {
			log.info("updatePaWempOrder updateSlipCompleteProc fail");
			throw processException("msg.cannot_save", new String[] { "updateSlipCompleteProc fail" });
		}
		return rtnMsg;
	}
	
	@Override
	public List<PaWempClaimList> selectCancelRequestList() throws Exception {
		
		return paWempOrderDAO.selectCancelRequestList();
	}
	
	@Override
	public List<PaWempDeliveryVO> selectCancelRequestDtList(ParamMap paramMap) throws Exception {
		return paWempOrderDAO.selectCancelRequestDtList(paramMap);
	}
	
	@Override
	public ParamMap cancelApproveProc(long claimBundleNo, String paCode) throws Exception {
		ParamMap resultMap = new ParamMap();
		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PAWEMPAPI_03_024";
		String paName = "";
		try {
			paramMap.put("apiCode", apiCode);
			paramMap.put("broadCode" , Constants.PA_WEMP_BROAD_CODE);
		    paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
		    if(paCode.equals(Constants.PA_WEMP_ONLINE_CODE)) paName = Constants.PA_ONLINE;
		    else paName = Constants.PA_BROAD;
			HashMap<String, String> apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			SetClaimApprove setClaimApprove = new SetClaimApprove();
			setClaimApprove.setClaimBundleNo(claimBundleNo);
			setClaimApprove.setClaimType("CANCEL");
			
			ReturnData returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST",setClaimApprove, ReturnData.class, paName);
			if(returnData.getReturnKey() == 1) { //성공
				log.info("취소승인 API 전송성공 결과 returnKey:1, returnMsg:"+returnData.getReturnMsg());
				resultMap.put("result_code","1");
				resultMap.put("result_text","OK");
			} else {
				log.info("취소승인 API 전송실패 결과 returnKey:"+returnData.getReturnKey()+", returnMsg:"+returnData.getReturnMsg());
				resultMap.put("result_code","0");
				resultMap.put("result_text", returnData.getReturnMsg());
			}
		}catch(Exception e){
			resultMap.put("result_code", "-9999");
			resultMap.put("result_text", e.getMessage());
		}
		
		return resultMap;
	}

	public ParamMap cancelRejectProc(PaWempDeliveryVO deliveryVo) throws Exception {
		ParamMap resultMap = new ParamMap();
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String apiCode = "IF_PAWEMPAPI_03_025";
		String dateTime = "";
		
		dateTime = systemService.getSysdatetimeToString();
		paramMap.put("apiCode", apiCode);
    	paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
    	paramMap.put("startDate", dateTime);
    	if(deliveryVo.getPaCode().equals(Constants.PA_WEMP_BROAD_CODE)){
    		paramMap.put("paName", Constants.PA_BROAD);
    	} else {
    		paramMap.put("paName", Constants.PA_ONLINE);
    	}
		
    	apiInfo = systemService.selectPaApiInfo(paramMap);
    	apiInfo.put("apiInfo", paramMap.getString("apiCode"));
		
		try {
			SetClaimRejectRequest setClaimReject = new SetClaimRejectRequest();
			setClaimReject.setClaimBundleNo(Long.parseLong(deliveryVo.getPaClaimNo()));
			setClaimReject.setClaimType("CANCEL");
			setClaimReject.setRejectReasonCode("CJD3"); //이미출고 고정
			setClaimReject.setRejectReasonDetail("이미출고");
			
			if(StringUtils.equals("DIRECT", deliveryVo.getParcelCompanyCode())) {
				setClaimReject.setShipMethod("DIRECT");
				setClaimReject.setScheduleShipDate(deliveryVo.getScheduleShipDate());
				if(!StringUtils.equals("1", deliveryVo.getDelyHopeYn())) {
					log.info("slipOutProc No TSLIPM.DELY_HOPE_YN setting");
				}
			} else {
				setClaimReject.setShipMethod("PARCEL");
				setClaimReject.setParcelCompanyCode(deliveryVo.getParcelCompanyCode());
				setClaimReject.setInvoiceNo(deliveryVo.getInvoiceNo());
			}
			
			ReturnData returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST", setClaimReject, ReturnData.class, paramMap.getString("paName"));
			if(returnData.getReturnKey() == 1) { //성공
				log.info("클레임거절 API 전송성공 결과 returnKey:1, returnMsg:"+returnData.getReturnMsg());
				resultMap.put("result_code","1");
				resultMap.put("result_text","OK");
			} else {
				log.info("클레임거절 API 전송실패 결과 returnKey:"+returnData.getReturnKey()+", returnMsg:"+returnData.getReturnMsg());
				resultMap.put("result_code","0");
				resultMap.put("result_text", returnData.getReturnMsg());
			}
		}catch(Exception e){
			resultMap.put("result_code", "-9999");
			resultMap.put("result_text", e.getMessage());
		}
		
		return resultMap;
	}
	
	@Override
	public ParamMap saveCancelApprove(List<PaWempDeliveryVO> deliveryVoList, String outBefClaimGb, boolean isUpdate) throws Exception {
		ParamMap resultMap = new ParamMap();
		int executedRtn = 0;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		PaWempDeliveryVO deliveryVo = new PaWempDeliveryVO();
		
		for(int i=0; i<deliveryVoList.size(); i++) {
			deliveryVo = deliveryVoList.get(i);
			
			// TPAORDERM에 취소승인으로 insert
			Paorderm paorderm = new Paorderm();
			paorderm.setPaCode(deliveryVo.getPaCode());
			paorderm.setPaOrderGb(WempCode.ORDER_GB.CANCEL.getCode()); //20 취소
			paorderm.setPaOrderNo(deliveryVo.getPaOrderNo());
			paorderm.setPaOrderSeq(deliveryVo.getPaOrderSeq());
			paorderm.setPaShipNo(deliveryVo.getPaShipNo());
			paorderm.setPaClaimNo(deliveryVo.getPaClaimNo());
			if(deliveryVo.getOptionQty() < 1) {
				paorderm.setPaProcQty(Long.toString(deliveryVo.getProductQty()));
			} else {
				paorderm.setPaProcQty(Long.toString(deliveryVo.getOptionQty()));
			}
			paorderm.setPaDoFlag("20"); //승인
			paorderm.setOutBefClaimGb(outBefClaimGb);
			paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setModifyId(Constants.PA_WEMP_PROC_ID);
			paorderm.setPaGroupCode("06");
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}
		}
		
		resultMap.put("result_code", Constants.SAVE_SUCCESS);
		if(isUpdate) {
			deliveryVo = deliveryVoList.get(0);
			// TPAWEMPCLAIMITEMLIST.PROC_FLAG '10'승인으로 update
			executedRtn = updateClaimItemProcFlag(deliveryVo.getPaClaimNo(), deliveryVo.getPaOrderNo(), deliveryVo.getPaShipNo(), WempCode.ORDER_GB.CANCEL.getCode(), "10");
			if (executedRtn < 1) { //여러row가 반영될수있음
				throw processException("msg.cannot_save", new String[] { "TPAWEMPCLAIMITEMLIST UPDATE" });
			}
		}
		
		return resultMap;
	}
	
	@Override
	public ParamMap saveCancelReject(PaWempDeliveryVO deliveryVo) throws Exception {
		ParamMap resultMap = new ParamMap();
		int executeRtn = 0;
		
		// TPAORDERM.PA_DO_FLAG '40'출고로 update
		Paorderm paorderm = new Paorderm();
		paorderm.setPaCode(deliveryVo.getPaCode());
		paorderm.setPaOrderNo(deliveryVo.getPaOrderNo());
		paorderm.setPaShipNo(deliveryVo.getPaShipNo());
		
		executeRtn = paWempOrderDAO.updatePaOrdermCancelRefusal(paorderm);
		//이미지 상태가 변경된 경우 update대상이 없을수도 있어서 결과값체크 없음
		
		// TPAWEMPCLAIMITEMLIST.PROC_FLAG '20'취소거부로 update
		executeRtn = updateClaimItemProcFlag(deliveryVo.getPaClaimNo(), deliveryVo.getPaOrderNo(), deliveryVo.getPaShipNo(), WempCode.ORDER_GB.CANCEL.getCode(), "20");
		if (executeRtn < 1) {
			throw processException("msg.cannot_save", new String[] { "TPAWEMPCLAIMITEMLIST UPDATE" });
		}
		
		resultMap.put("result_code", Constants.SAVE_SUCCESS);
		return resultMap;
	}
	
	@Override
	public ParamMap saveCancelFail(PaWempDeliveryVO deliveryVo) throws Exception {
		ParamMap resultMap = new ParamMap();
		int executeRtn = 0;
		
		//TPAWEMPCLAIMITEMLIST.PROC_FLAG '90'처리실패로 update
		executeRtn = updateClaimItemProcFlag(deliveryVo.getPaClaimNo(), deliveryVo.getPaOrderNo(), deliveryVo.getPaShipNo(), WempCode.ORDER_GB.CANCEL.getCode(), "90");
		if (executeRtn < 1) { //여러row가 반영될수있음
			throw processException("msg.cannot_save", new String[] { "TPAWEMPCLAIMITEMLIST UPDATE" });
		}
		
		resultMap.put("result_code", Constants.SAVE_SUCCESS);
		return resultMap;
	}
	
	@Override
	public int updateCancelWithdrawYn(PaWempClaimList paWempClaimList) throws Exception {
		return paWempOrderDAO.updateCancelWithdrawYn(paWempClaimList);
	}
	
	@Override
	public List<PaWempTargetVO> selectCancelInputTargetList() throws Exception {
		return paWempOrderDAO.selectCancelInputTargetList();
	}
	
	@Override
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paWempOrderDAO.selectCancelInputTargetDtList(paramMap);
	}
	
	@Override
	public int updateClaimItemProcFlag(String paClaimNo, String paOrderNo, String paShipNo, String paOrderGb, String procFlag) throws Exception {
		//TPAWEMPCLAIMITEMLIST.PROC_FLAG update
		PaWempClaimItemList claimItem = new PaWempClaimItemList();
		claimItem.setPaClaimNo(paClaimNo);
		claimItem.setPaOrderNo(paOrderNo);
		claimItem.setPaShipNo(paShipNo);
		claimItem.setPaOrderGb(paOrderGb);
		claimItem.setProcFlag(procFlag);
		claimItem.setModifyId(Constants.PA_WEMP_PROC_ID);
		return paWempOrderDAO.updateClaimItemProcFlag(claimItem);
	}
	
	@Override
	public int selectBusinessDayAccount(String paClaimNo, String delyGb) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("paClaimNo", paClaimNo);
		paramMap.put("delyGb", delyGb);
		return paWempOrderDAO.selectBusinessDayAccount(paramMap);
	}
	
	@Override
	public List<HashMap<String, Object>> selectDeliveryCompleteList() throws Exception{
		return paWempOrderDAO.selectDeliveryCompleteList();
	}
	
	@Override
	public List<PaWempDeliveryVO> selectOrgItemInfoByCancelInfo(String paClaimNo) throws Exception{
		return paWempOrderDAO.selectOrgItemInfoByCancelInfo(paClaimNo);
	}

	@Override
	public int updateDeliveryStatus(String paShipNo, String deliveryStatus) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("paShipNo"      , paShipNo);
		paramMap.put("deliveryStatus", deliveryStatus);
		paramMap.put("modifyId"      , Constants.PA_WEMP_PROC_ID);
		return paWempOrderDAO.updateDeliveryStatus(paramMap);
	}
	
	@Override
	public int selectPaWempOrderListExists(PawemporderlistVO paWempOrderList) throws Exception {
		return paWempOrderDAO.selectPaWempOrderListExists(paWempOrderList);
	}
	
	@Override
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception {
		return paWempOrderDAO.selectOrderPromo(orderMap);
	}
	
	@Override
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception {
		return paWempOrderDAO.selectOrderPaPromo(orderMap);
	}

	@Override
	public List<Map<String, Object>> selectSlipUpdateProcList() throws Exception {
		return paWempOrderDAO.selectSlipUpdateProcList();
	}
}
