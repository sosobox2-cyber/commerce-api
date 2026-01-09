package com.cware.netshopping.pafaple.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaFapleOrderSettlement;
import com.cware.netshopping.domain.model.PaFapleShipSettlement;
import com.cware.netshopping.pafaple.process.PaFapleAccountProcess;
import com.cware.netshopping.pafaple.repository.PaFapleAccountDAO;
import com.cware.netshopping.pafaple.util.PaFapleApiRequest;
import com.cware.netshopping.pafaple.util.PaFapleConnectUtil;

@Service("pafaple.account.PaFapleAccountProcess")
public class PaFapleAccountProcessImpl extends AbstractProcess implements PaFapleAccountProcess {
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	private PaFapleConnectUtil paFapleConnectUtil;
	
	@Autowired
	private PaFapleApiRequest paFapleApiRequest;
	
	@Autowired
	private PaFapleAccountDAO paFapleAccountDAO;
	
	@Override
	public ResponseMsg getOrderSettlementList(String stdYMD, HttpServletRequest request) throws Exception {
		
		String apiCode = "IF_PAFAPLEAPI_05_001";
		String duplicateCheck = "";
		String apiResultStatus = null;
		String paCode  = "";
		int executedRtn = 0;
		List<Map<String, Object>> settlementList = null;
		ParamMap paramMap = new ParamMap();
		PaFapleOrderSettlement paFapleOrderSettlement = null;
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		try {
			
			log.info("======= 패션플러스 주문정산조회 API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paFapleApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			for(int i = 0; i< Constants.PA_FAPLE_CONTRACT_CNT ; i ++) {
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				
				log.info("03.API Request Setting");
				
				Map<String, Object> apiRequestMap  = new HashMap<String, Object>();
				apiRequestMap.put("PageNum", 1);
				apiRequestMap.put("PageSize", 999999999);
				if(stdYMD == null) {
					stdYMD   = DateUtil.addMonth(DateUtil.getCurrentDateAsString() , -1 , DateUtil.GENERAL_DATE_FORMAT).substring(0,6);
				}
				apiRequestMap.put("stdYMD", stdYMD);
				
				log.info("04.API Call");
				responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiRequestMap);
			
				if (responseMap != null) {
					apiResultStatus = String.valueOf(responseMap.get("Status"));
					
					if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(apiResultStatus)) {
						log.info("05.Processing");
						settlementList = (List<Map<String, Object>>)(responseMap.get("BiReckoning_list"));
						
						for(Map<String, Object> settlement : settlementList) {
							try {
								paFapleOrderSettlement = new PaFapleOrderSettlement();
								executedRtn = 0;
								 
								String[] OrderIdSeq  = settlement.get("order_id").toString().split("-");
								
								paFapleOrderSettlement.setPaFaplesettlementNo(systemService.getSequenceNo("PAFAPLE_SETTLEMENT_NO")); 
								paFapleOrderSettlement.setOrderId(OrderIdSeq[0].toString());
								paFapleOrderSettlement.setOrderSeq(OrderIdSeq[1].toString());
								paFapleOrderSettlement.setOrderType(ComUtil.objToStr(settlement.get("orderType")));
								paFapleOrderSettlement.setDeliveryDate(ComUtil.objToStr(settlement.get("delivery_date")));
								paFapleOrderSettlement.setItemNo(ComUtil.objToStr(settlement.get("ItemNo")));
								paFapleOrderSettlement.setItemName(ComUtil.objToStr(settlement.get("ItemName")));
								paFapleOrderSettlement.setAttr1Name(ComUtil.objToStr(settlement.get("Attr1Name")));
								paFapleOrderSettlement.setAttr2Name(ComUtil.objToStr(settlement.get("Attr2Name")));
								paFapleOrderSettlement.setMargin(ComUtil.objToLong(settlement.get("margin")));
								paFapleOrderSettlement.setQty(ComUtil.objToLong(settlement.get("Qty")));
								paFapleOrderSettlement.setSalesAmt(ComUtil.objToLong(settlement.get("sales_amt")));
								paFapleOrderSettlement.setPurchaseAmt(ComUtil.objToLong(settlement.get("purchase_amt")));
								paFapleOrderSettlement.setProfitsAmt(ComUtil.objToLong(settlement.get("profits_amt")));
								paFapleOrderSettlement.setSalecpon(ComUtil.objToStr(settlement.get("salecpon")));
								paFapleOrderSettlement.setOldOrderId(ComUtil.objToStr(settlement.get("Old_order_id")));
								paFapleOrderSettlement.setCouponFapleAmt(ComUtil.objToLong(settlement.get("Coupon_Faple_amt")));
								paFapleOrderSettlement.setCompleteDate(ComUtil.objToStr(settlement.get("complete_date")));
								paFapleOrderSettlement.setGiveAmt(ComUtil.objToLong(settlement.get("Give_amt")));
								paFapleOrderSettlement.setBrandIdSalesName(ComUtil.objToStr(settlement.get("brand_id_sales_name")));
								paFapleOrderSettlement.setOptId(ComUtil.objToStr(settlement.get("OptID")));
      
								if(paFapleAccountDAO.selectPaFapleOrderSettlementList(paFapleOrderSettlement) > 0 ) {
									continue;
								}
								executedRtn = paFapleAccountDAO.insertPaFapleOrderSettlementList(paFapleOrderSettlement);
								if (executedRtn != 1) {
									throw processException("msg.cannot_save", new String[] { "TPAFAPLESETTLEMENT INSERT" });
								}
								
							} catch (Exception e) {
								log.error("Exception occurs : " + e.getMessage());
							}
						}
					} else if ("Err-Dat-902".equals(apiResultStatus)) { // Err-Dat-902 : 조회할 정산내역이 없습니다.
						paramMap.put("code", "404");
						paramMap.put("message", getMessage("errors.no.select"));
					} else {
						throw new Exception(String.valueOf(paramMap));
					}
				}
			}
		}catch (Exception e) {
			paFapleConnectUtil.checkException(paramMap, e);
		} finally {
			paFapleConnectUtil.closeApi(request, paramMap);
			log.info("======= 패션플러스 주문정산조회 API End - {} =======");
		}
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public ResponseMsg getShipSettlementList(String stdYMD, HttpServletRequest request) throws Exception {
		
		String apiCode = "IF_PAFAPLEAPI_05_002";
		String duplicateCheck = "";
		String apiResultStatus = null;
		String paCode  = "";
		int executedRtn = 0;
		List<Map<String, Object>> settlementList = null;
		ParamMap paramMap = new ParamMap();
		PaFapleShipSettlement paFapleShipSettlement = null;
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		try {
			
			log.info("======= 패션플러스 배송비 정산조회 API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paFapleApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			for(int i = 0; i< Constants.PA_FAPLE_CONTRACT_CNT ; i ++) {
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				
				log.info("03.API Request Setting");
				
				Map<String, Object> apiRequestMap  = new HashMap<String, Object>();
				apiRequestMap.put("PageNum", 1);
				apiRequestMap.put("PageSize", 999999999);
				if(stdYMD == null) {
					stdYMD   = DateUtil.addMonth(DateUtil.getCurrentDateAsString() , -1 , DateUtil.GENERAL_DATE_FORMAT).substring(0,6);
				}
				apiRequestMap.put("stdYMD", stdYMD);
				
				log.info("04.API Call");
				responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiRequestMap);
			
				if (responseMap != null) {
					apiResultStatus = String.valueOf(responseMap.get("Status"));
					
					if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(apiResultStatus)) {
						log.info("05.Processing");
						settlementList = (List<Map<String, Object>>)(responseMap.get("BiReckoning_list"));
						
						for(Map<String, Object> settlement : settlementList) {
							try {
								paFapleShipSettlement = new PaFapleShipSettlement();
								executedRtn = 0;
								 
								paFapleShipSettlement.setPaFapleshipsettlementNo(systemService.getSequenceNo("PAFAPLE_SHIP_SETTLEMENT_NO"));
								paFapleShipSettlement.setSenderId(ComUtil.objToStr(settlement.get("Senderid")));	
								paFapleShipSettlement.setSenderName(ComUtil.objToStr(settlement.get("Sendername")));		
								paFapleShipSettlement.setMemberName(ComUtil.objToStr(settlement.get("MemberName")));		
								paFapleShipSettlement.setOrderId(ComUtil.objToStr(settlement.get("Order_id")));			
								paFapleShipSettlement.setDeliveryDate(ComUtil.objToStr(settlement.get("DeliveryDate")));	
								paFapleShipSettlement.setChargeRatioOfHQ(ComUtil.objToLong(settlement.get("ChargeRatioOfHQ")));	
								paFapleShipSettlement.setNormalSenderFee(ComUtil.objToLong(settlement.get("NormalSenderFee")));	
								paFapleShipSettlement.setFeeHQ(ComUtil.objToLong(settlement.get("FeeHQ")));			
								paFapleShipSettlement.setSenderFee(ComUtil.objToLong(settlement.get("SenderFee")));			
								paFapleShipSettlement.setContents(ComUtil.objToStr(settlement.get("Contents")));		
								paFapleShipSettlement.setOldOrderId(ComUtil.objToStr(settlement.get("Old_Order_id")));	
								if(ComUtil.objToStr(settlement.get("Contents")).contains("정상배송료")) {
									paFapleShipSettlement.setOrderGb("10"); //배송
								}else {
									paFapleShipSettlement.setOrderGb("30"); //반품
								}
      
								if(paFapleAccountDAO.selectPaFapleShipSettlementList(paFapleShipSettlement) > 0 ) {
									continue;
								}
								executedRtn = paFapleAccountDAO.insertPaFapleShipSettlementList(paFapleShipSettlement);
								if (executedRtn != 1) {
									throw processException("msg.cannot_save", new String[] { "TPAFAPLESETTLEMENT INSERT" });
								}
								
							} catch (Exception e) {
								log.error("Exception occurs : " + e.getMessage());
							}
						}
					} else if ("Err-Dat-902".equals(apiResultStatus)) { // Err-Dat-902 : 조회할 정산내역이 없습니다.
						paramMap.put("code", "404");
						paramMap.put("message", getMessage("errors.no.select"));
					} else {
						throw new Exception(String.valueOf(paramMap));
					}
				}
			}
		}catch (Exception e) {
			paFapleConnectUtil.checkException(paramMap, e);
		} finally {
			paFapleConnectUtil.closeApi(request, paramMap);
			log.info("======= 패션플러스 배송비정산조회 API End - {} =======");
		}
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
}
