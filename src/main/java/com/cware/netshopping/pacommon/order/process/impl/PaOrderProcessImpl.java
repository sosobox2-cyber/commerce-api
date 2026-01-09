package com.cware.netshopping.pacommon.order.process.impl;
/**
* Order ProcessImpl
* 
* company        author   date               Description
* commerceware   남시훈       2018.04.16        ecam4j 소스 제거
* commerceware   백호선       2018.04.25        
*/

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.OrderInfoMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.common.util.PostUtil;
import com.cware.netshopping.core.custcenter.domain.OrderShipCostVO;
import com.cware.netshopping.core.custcenter.process.OrderBizProcess;
import com.cware.netshopping.core.exchange.domain.SmsSendVO;
import com.cware.netshopping.core.exchange.process.UmsBizProcess;
import com.cware.netshopping.domain.CancelInputVO;
import com.cware.netshopping.domain.CanceldtVO;
import com.cware.netshopping.domain.ClaimdtVO;
import com.cware.netshopping.domain.OrderInputVO;
import com.cware.netshopping.domain.OrderShipCostDt;
import com.cware.netshopping.domain.OrderStockInVO;
import com.cware.netshopping.domain.OrderStockOutVO;
import com.cware.netshopping.domain.OrderdtVO;
import com.cware.netshopping.domain.OrdergoodsVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.OrdershipcostVO;
import com.cware.netshopping.domain.OrderstockVO;
import com.cware.netshopping.domain.PreoutOrderInputVO;
import com.cware.netshopping.domain.PromocounselVO;
import com.cware.netshopping.domain.model.Counsel;
import com.cware.netshopping.domain.model.Couponissue;
import com.cware.netshopping.domain.model.Custcounseldt;
import com.cware.netshopping.domain.model.Custcounselm;
import com.cware.netshopping.domain.model.Customer;
import com.cware.netshopping.domain.model.Custspinfo;
import com.cware.netshopping.domain.model.Custsystem;
import com.cware.netshopping.domain.model.Custtel;
import com.cware.netshopping.domain.model.Ordercostapply;
import com.cware.netshopping.domain.model.Orderdt;
import com.cware.netshopping.domain.model.Ordergoods;
import com.cware.netshopping.domain.model.Orderm;
import com.cware.netshopping.domain.model.Orderpromo;
import com.cware.netshopping.domain.model.Orderreceipts;
import com.cware.netshopping.domain.model.Ordershipcost;
import com.cware.netshopping.domain.model.Orderstock;
import com.cware.netshopping.domain.model.PaMonitering;
import com.cware.netshopping.domain.model.PaOrderShipCost;
import com.cware.netshopping.domain.model.PaPersonalInfoNoticeSms;
import com.cware.netshopping.domain.model.PaSlipInfo;
import com.cware.netshopping.domain.model.Promocounsel;
import com.cware.netshopping.domain.model.Receiver;
import com.cware.netshopping.pacommon.claim.repository.PaClaimDAO;
import com.cware.netshopping.pacommon.order.process.PaOrderProcess;
import com.cware.netshopping.pacommon.order.repository.PaOrderDAO;


@Service("pacommon.order.paorderProcess")
public class PaOrderProcessImpl extends AbstractService implements PaOrderProcess{
	
	@Resource(name = "pacommon.order.paorderDAO")
    private PaOrderDAO paorderDAO;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "core.custcenter.orderBizProcess")
	private OrderBizProcess orderBizProcess;
	
	@Resource(name = "core.exchange.umsBizProcess")
	private UmsBizProcess umsBizProcess;
	
	@Resource(name = "com.cware.netshopping.common.util.PostUtil")
	private PostUtil postUtil;
	
	@Resource(name="common.system.systemProcess")
	private SystemProcess systemProcess;
	
	@Resource(name = "pacommon.claim.paclaimDAO")
    private PaClaimDAO paclaimDAO;
	
	
	@SuppressWarnings("rawtypes")
	public HashMap<String, Object> newSaveCancel(CancelInputVO cancelInputVO) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap[] resultMapArr		 			= new HashMap[1];
		String rtnValue = "000000";
		
		OrdershipcostVO[] ordershipcost = null;
		Ordercostapply [] ordercostapply= null;
		
		List<ClaimdtVO>  claimList  				= new ArrayList<ClaimdtVO>();
		List<OrdergoodsVO> ordergoodsList 			= new ArrayList<OrdergoodsVO>();
		List<OrderstockVO> orderstockList 			= new ArrayList<OrderstockVO>();
		List<Orderpromo> orderPromoList 			= new ArrayList<Orderpromo>();
		List<CanceldtVO> cancelDtList 				= new ArrayList<CanceldtVO>();
		ArrayList<Orderreceipts> orderreceiptsList 	= new ArrayList<Orderreceipts>();
		
		String newOrderWseq      = "";
		HashMap<String, Object> obj = new HashMap<>();
		String    dateTime       = getSysdatetimeToString();								// 시간생성(String)
		Timestamp sysDateTime    = DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss");	// 시간생성(Timestamp)
						
		List<OrderdtVO> orgOrderdtList 			= new ArrayList<OrderdtVO>();  //원주문에서 Syslast < 1 미포함
		List<OrderdtVO> orgOrderdtList4ShipCost = new ArrayList<OrderdtVO>();  //Syslast < 1  포함 주문
		List<OrderdtVO> orgOrderdtListAllOrder  = new ArrayList<OrderdtVO>();  //Syslast < 1  포함 주문

		ParamMap paramMap = new ParamMap();

		try{
			getOriginalOrder(cancelInputVO, orgOrderdtList, orgOrderdtList4ShipCost, orgOrderdtListAllOrder);
			newOrderWseq = selectWSeq(cancelInputVO);
			
			//= Setting CancelDtList, ClaimDtList
			for(OrderdtVO orgOrderdt :  orgOrderdtList){
				//= 취소 수량 체크
				if(!checkSysLastForCancel(cancelInputVO, orgOrderdt, paramMap, resultMap)) return resultMap;
				
				if(Integer.parseInt(orgOrderdt.getDoFlag()) < 30){
					settingCancel(cancelInputVO, orgOrderdt, newOrderWseq, sysDateTime, cancelDtList);
				}else{ //출하 지시가 된 사은품
					settingClaim (cancelInputVO, orgOrderdt, newOrderWseq, sysDateTime, claimList);
				}	
			}
			
			settingOrderStock    (cancelDtList,  orderstockList);
			settingOrderGoods    (cancelDtList,  ordergoodsList);
			settingOrderPromo    (cancelDtList,  orderPromoList);
			settingOrderReceipts (orgOrderdtListAllOrder, cancelDtList, orderreceiptsList);  //orgOrderdtList
			
			//= Setting ORDERSHIPCOST, ORDERCOSTAPPLY
			prepareShipCost(cancelDtList, orgOrderdtList4ShipCost, paramMap);
			obj = settingTordershipcost4SKB(paramMap);	
			ordershipcost  = (OrdershipcostVO[]) obj.get("OrderShipCost");
			ordercostapply = (Ordercostapply[]) obj.get("OrderCostApply");
		
			//= INSERT TCANCELDT, UPDATE TORDERDT , TINPLANQTY 
			saveCanceldt(cancelDtList);									
			//= INSERT TCLAIMDT ( do_flag >= 30 인 사은품)
			insertClaimdt(claimList);		
			//= UPDATE TORDERGOODS
			updateOrderGoods(ordergoodsList);		
			//= UPDATE TORDERSTOCK
			updateStockCancel(orderstockList);
			//= INSERT TORDERRECEIPTS, TPAORDERSHIPCOST
			saveOrderReceipts(cancelDtList, orderreceiptsList, ordershipcost);
			//= INSERT TORDERSHIPCOST
			insertOrdershipcost(ordershipcost);
			//= INSERT TORDERCOSTAPPLY
			insertOrderCostApply(ordercostapply);
			
			//= UPDATE TORDERPROMO
			updateOrderpromo(orderPromoList, paramMap);
			
			settingResultMap(cancelInputVO, cancelDtList ,resultMap);
			resultMapArr[0] = resultMap;			
			updatePaOrderM(resultMapArr);		

		}catch (Exception e) {
			if(e.getMessage() == null){
				rtnValue = getMessage("pa.fail_cancel_input", new String[]{e.toString()});
			} else {
				rtnValue = getMessage("pa.fail_cancel_input", new String[]{e.getMessage()});
				}
			throw processMessageException(rtnValue);
		}
		
		return resultMap;
	}
	
	private void settingOrderReceipts(List<OrderdtVO> orgOrderdtList, List<CanceldtVO> cancelDtList, List<Orderreceipts> orderreceiptsList) throws Exception{
		
		String orderGb = null;
		for(CanceldtVO vo : cancelDtList){
			if("001".equals(vo.getOrderDSeq())){ //본품
				orderGb = vo.getDoFlag();
			}
		}	
		if(Integer.parseInt(orderGb) <= 10 ) orderGb = "10";
		
		switch(orderGb){
		
			case "10": //무통장
				settingOrderReceipt(orgOrderdtList,cancelDtList, orderreceiptsList);
				break;
			default :
				settingOrderReceipt(cancelDtList,  orderreceiptsList);
		}
	}
	
	private void settingOrderReceipt(List<OrderdtVO> orgOrderdtList, List<CanceldtVO> cancelDtList, List<Orderreceipts> orderreceiptsList) throws Exception{
		ParamMap param = new ParamMap();
		long rSalAmt   = 0;
		long cancelAmt = 0;
		
		for(OrderdtVO order : orgOrderdtList ){
			rSalAmt += order.getRsaleAmt();
		}
		for(CanceldtVO cancel : cancelDtList){
			cancelAmt += cancel.getRsaleAmt();
		}
		rSalAmt -= cancelAmt;
		preCancelReceitpSetting(orgOrderdtList.get(0), cancelDtList.get(0), "11", rSalAmt, param); //무통장 check
		newOrderReceipt(param ,orderreceiptsList);  
		//배송비는 Insert 부분에서 따로 계산해준다.
	}
	
	
	private void saveOrderReceipts(List<CanceldtVO> cancelDtList, ArrayList<Orderreceipts> orderreceiptsList, OrdershipcostVO[] ordershipcost) throws Exception{
		String orderGb = null;
		for(CanceldtVO vo : cancelDtList){
			if("001".equals(vo.getOrderDSeq())){ //본품
				orderGb = vo.getDoFlag();
			}
		}	
		if(Integer.parseInt(orderGb) <= 10 ) orderGb = "10";
		
		//= 무통장 입금건에 대한 취소 처리 또는 입금완료 주문 건에 대한 취소
		switch(orderGb){
		
		case "10": //무통장
			//= INSERT ORDERRECEIPT
			insertReceiptsCancel10(orderreceiptsList, ordershipcost);
			updatePaOrderShipCost(orderreceiptsList);
				
		break;	
		
		default : //나머지
			insertReceiptsCancel(orderreceiptsList, ordershipcost);
			updateReceipts(orderreceiptsList); 
			//= INSERT TPAORDERSHIPCOST
			insertPaOrderShipCost4Cancel(ordershipcost, cancelDtList, orderreceiptsList );
		}
	}
	private void updateOrderReceiptCancel(ArrayList<Orderreceipts> orderreceiptsList) throws Exception{
		
		int executeCnt	  = 0; 
		Orderreceipts rec = new Orderreceipts();
		rec.setOrderNo		(orderreceiptsList.get(0).getOrderNo());
		rec.setCancelDate	(orderreceiptsList.get(0).getInsertDate());
		rec.setCancelId		(orderreceiptsList.get(0).getInsertId());
		
		try{
			executeCnt = paorderDAO.updateOrderReceiptCancel(rec);
			if(executeCnt < 1 )	processMessageException("CANCEL ORDERRECEIPT COUNT : 0");

		}catch(Exception e){
			throw processException("CANCEL ORDERRECEIPT FAIL", new String[] {});
		}
	}
	
	private void updatePaOrderShipCost(ArrayList<Orderreceipts> orderreceiptsList) throws Exception{
		
		String orderNo  = null;
		double shipCostAmt = 0;
		String seq		= "001"; //무통장 입금건에 대해서는 SEQ가 새로 따지는 경우는 고려해봐야함 
			
		for(Orderreceipts or: orderreceiptsList) {
			if(or.getSettleGb().equals("15")){
				orderNo		= or.getOrderNo();
				shipCostAmt = or.getQuestAmt();
			}
		}
		updatePaOrderShipCost(orderNo, seq, shipCostAmt);
		
	}
	private void updatePaOrderShipCost(String orderNo, String seq, double shipCostAmt){
		try{
			PaOrderShipCost paordershipcost = new PaOrderShipCost();
			paordershipcost.setOrderNo		(orderNo);
			paordershipcost.setSeq			(seq);
			paordershipcost.setShpFeeAmt	((long)shipCostAmt);
			paordershipcost.setPaShpFeeAmt	(0);
			paorderDAO.updatePaOrderShipCost(paordershipcost);			
		
		}catch(Exception e) {
			log.info("//----UPDATE PAORDERSHIPCOST ERROR");
		}
	}
	
	
	private void settingOrderPromo(List<CanceldtVO> cancelDtList, List<Orderpromo> orderPromoList) throws Exception{
		
		try{	
			List<Orderpromo> orgOrderPromoList 			= null;
			ParamMap paramMap  = new ParamMap();
			Timestamp sysDateTime = null;
			String	  userId      = null;
			String	  orderNo	  = null;
			String	  orderGSeq	  = null;
					
			for(CanceldtVO vo: cancelDtList){
				paramMap.put("orderNo"		, vo.getOrderNo());
				paramMap.put("orderGSeq"	, vo.getOrderGSeq());
				sysDateTime = vo.getLastProcDate();
				userId		= vo.getLastProcId();
				orderNo		= vo.getOrderNo();
				orderGSeq	= vo.getOrderGSeq();
			}
		
			log.info("//----TORDERPROMO setting");
			orgOrderPromoList = paorderDAO.selectOrderPromo(paramMap); //주문 orderpromo 조회
			
			for(Orderpromo orgOrderpromo :  orgOrderPromoList ){
				Orderpromo orderpromo = new Orderpromo();
				
				orderpromo.setBlank();
				orderpromo.setPromoNo		(orgOrderpromo.getPromoNo());
				orderpromo.setCancelDate	(sysDateTime);
				orderpromo.setCancelId		(userId);
				orderpromo.setOrderNo		(orderNo);
				orderpromo.setOrderGSeq		(orderGSeq);
				
				orderPromoList.add(orderpromo);
			}
		}catch(Exception e){
			throw processException("SELECT ORDERPROMO FAIL", new String[] {});
		}
	}
	
	private void prepareShipCost(List<CanceldtVO> cancelDtList, List<OrderdtVO> orgOrderdtList4ShipCost , ParamMap paramMap){
		if(cancelDtList.size() <1 ) return;
		List<Ordercostapply> costApply4Gift = new ArrayList<Ordercostapply>();
		
		settingCostApplyForGift(cancelDtList	, costApply4Gift);
		
		String orderNo    = cancelDtList.get(0).getOrderNo();
		String orderGSeq  = cancelDtList.get(0).getOrderGSeq();
		String orderWSeq  = cancelDtList.get(0).getOrderWSeq();
		String userId	  = cancelDtList.get(0).getLastProcId();
		Timestamp sysdate = cancelDtList.get(0).getLastProcDate();
		String entpCode   = cancelDtList.get(0).getEntpCode();
		
		paramMap.put("orderNo"					, orderNo  );
		paramMap.put("orderGSeq"				, orderGSeq);
		paramMap.put("orderDSeq"				, "001"	   );
		paramMap.put("newOrderWseq"				, orderWSeq);
		paramMap.put("entpCode"					, entpCode );
		paramMap.put("userId"					, userId   );
			
		paramMap.put("cancelDtList" 			, cancelDtList);
		paramMap.put("sysDateTime" 				, sysdate );
		paramMap.put("orgOrderdtList4ShipCost"	, orgOrderdtList4ShipCost);
		paramMap.put("CostApply4Gift"			, costApply4Gift);	
	}
	
	private void settingCostApplyForGift(List<CanceldtVO> cancelDtList, List<Ordercostapply> costApply4Gift){
		for(CanceldtVO cancel : cancelDtList){
			if(!"001".equals(cancel.getOrderDSeq())){
				Ordercostapply orderCostApply = new Ordercostapply();
				orderCostApply.setBlank				();
				orderCostApply.setCwareAction		("I");								
				orderCostApply.setOrderNo			(cancel.getOrderNo());
				orderCostApply.setOrderGSeq			(cancel.getOrderGSeq());
				orderCostApply.setOrderDSeq			(cancel.getOrderDSeq());
				orderCostApply.setOrderWSeq			(cancel.getOrderWSeq());
				orderCostApply.setApplyCostSeq		("000");
				orderCostApply.setShipCostNo		(0);
				orderCostApply.setInsertId			(cancel.getLastProcId());
				orderCostApply.setInsertDate		(cancel.getLastProcDate());
				orderCostApply.setModifyId			(cancel.getLastProcId());
				orderCostApply.setModifyDate		(cancel.getLastProcDate());
				costApply4Gift.add(orderCostApply);	
			}
		}
	}
	
	private void settingOrderReceipt(List<CanceldtVO> cancelDtList, List<Orderreceipts> orderreceiptsList) throws Exception{
		String    receipt_no  	 = selectSequenceNo("RECEIPT_NO");
		
		for(CanceldtVO cancel : cancelDtList){
			
			if(!cancel.getOrderDSeq().equals("001")) continue;
			
			Orderreceipts orderreceipts = new Orderreceipts();
			
			orderreceipts.setBlank();
			orderreceipts.setReceiptNo			 (receipt_no);
			orderreceipts.setOrderNo			 (cancel.getOrderNo());
			orderreceipts.setCustNo				 (cancel.getCustNo());
			orderreceipts.setDoFlag				 ("90");
			
			orderreceipts.setSettleGb			 ("61");
			orderreceipts.setCardBankCode		 ("");
			orderreceipts.setBankSeq			 ("");
			orderreceipts.setCardName			 ("");
			orderreceipts.setCardNo				 ("");
			orderreceipts.setCvv				 ("");		
			orderreceipts.setDepositor			 (cancel.getCustName());
			orderreceipts.setValidDate			 ("");
			orderreceipts.setQuestAmt			 (cancel.getRsaleAmt());
			orderreceipts.setReceiptPlanDate	 (null);
			orderreceipts.setOkNo				 ("");
			orderreceipts.setOkDate				 (cancel.getLastProcDate());
			orderreceipts.setOkMed				 ("000");
			orderreceipts.setOkErrorCode		 ("0000");
			orderreceipts.setVanComp			 ("");
			orderreceipts.setPayMonth			 (0);
			orderreceipts.setNorestYn			 ("0");
			orderreceipts.setNorestRate			 (0);
			orderreceipts.setNorestAmt			 (0);
			orderreceipts.setReceiptAmt			 (0);
			orderreceipts.setReceiptDate		 (null);
			orderreceipts.setEndYn				 ("1");
			orderreceipts.setRepayPbAmt			 (0);
			orderreceipts.setCancelYn			 ("0");
			orderreceipts.setCancelCode			 ("000");
			orderreceipts.setCancelDate			 (null);
			orderreceipts.setCancelId			 ("");
			orderreceipts.setSaveamtUseFlag		 ("0");
			orderreceipts.setCodDelyYn			 ("0");
			orderreceipts.setDivideYn			 ("0");
			orderreceipts.setPartialCancelYn	 ("1");
			orderreceipts.setProtxVendortxcode	 ("");
			orderreceipts.setProtxStatus		 ("");
			orderreceipts.setProtxStatusdetail	 ("");
			orderreceipts.setProtxVpstxid		 ("");
			orderreceipts.setProtxSecuritykey	 ("");
			orderreceipts.setProtxTxauthno		 ("");
			orderreceipts.setIssueNumber		 ("");
			orderreceipts.setPayboxIdentifiant	 ("");
			orderreceipts.setPayboxCodetraitement("");
			orderreceipts.setPayboxPays			 ("");
			orderreceipts.setPayboxDateq		 ("");
			orderreceipts.setPayboxReference	 ("");
			orderreceipts.setCardPassword		 ("");
			orderreceipts.setRemark1V			 ("");
			orderreceipts.setRemark2V			 ("");
			orderreceipts.setRemark3N  			 (0);
			orderreceipts.setRemark4N			 (0);
			orderreceipts.setRemark5D			 (null);
			orderreceipts.setRemark6D			 (null);
			orderreceipts.setRemark				 ("");
			orderreceipts.setProcId				 (cancel.getLastProcId());
			orderreceipts.setProcDate			 (cancel.getLastProcDate());
			orderreceipts.setInsertId			 (cancel.getLastProcId());
			orderreceipts.setInsertDate			 (cancel.getLastProcDate());
			
			orderreceiptsList.add(orderreceipts);
		}
	}
	
	private void settingOrderGoods(List<CanceldtVO> cancelDtList,  List<OrdergoodsVO> ordergoodsList) throws Exception{
		OrdergoodsVO orgOrdergoods = null;
		OrdergoodsVO ordergoods = null; 
		ParamMap paramMap 	= new ParamMap();
		try{
			for(CanceldtVO cancel : cancelDtList){
				
				if(!cancel.getOrderDSeq().equals("001")) continue;
				
				paramMap.put("orderNo",   cancel.getOrderNo());
				paramMap.put("orderGSeq", cancel.getOrderGSeq());
				orgOrdergoods = paorderDAO.selectOrderGoods(paramMap);
				
				if(orgOrdergoods == null) throw processException("SELECT ORDERGOODS FAIL : null", new String[] {});
				
				ordergoods = new OrdergoodsVO();
				ordergoods.setBlank();
				ordergoods.setCancelQty		(cancel.getCancelQty());
				ordergoods.setClaimQty		(0);
				ordergoods.setExchCnt		(0);
				ordergoods.setAsCnt			(0);				
				ordergoods.setOrderNo		(cancel.getOrderNo());
				ordergoods.setOrderGSeq		(cancel.getOrderGSeq());
				ordergoods.setCancelQtyOrg	(orgOrdergoods.getCancelQty());
				ordergoods.setClaimQtyOrg	(orgOrdergoods.getClaimCanQty());
				ordergoods.setExchCntOrg	(orgOrdergoods.getExchCnt());
				ordergoods.setAsCntOrg		(orgOrdergoods.getAsCnt());
				ordergoodsList.add(ordergoods);
			}	
		}catch(Exception e){
			throw processException("SELECT ORDERGOODS FAIL", new String[] {});
		}	
	}
	
	private void settingOrderStock(List<CanceldtVO> cancelDtList, List<OrderstockVO> orderstockList){
		
		for(CanceldtVO cancel : cancelDtList){
			OrderstockVO orderstock = new OrderstockVO();
			if(Integer.parseInt(cancel.getOrgDoFlag()) <= 10){
				orderstock.setOutPlanQty(0);
				orderstock.setOrderQty	(cancel.getCancelQty());
			}else{
				orderstock.setOutPlanQty(cancel.getCancelQty());
				orderstock.setOrderQty	(0);
			}
			orderstock.setTotSaleQty	(cancel.getCancelQty());
			orderstock.setGoodsCode		(cancel.getGoodsCode());
			orderstock.setGoodsdtCode	(cancel.getGoodsdtCode());
			orderstock.setWhCode		(cancel.getWhCode());
			orderstock.setPreoutGb		(cancel.getPreoutGb());
			orderstock.setModifyDate	(cancel.getLastProcDate());
			orderstock.setModifyId		(cancel.getLastProcId());
			orderstockList.add(orderstock);
		}
	}
	private void settingClaim(CancelInputVO cancelInputVO, OrderdtVO orgOrderdt,String newOrderWseq, Timestamp sysDateTime, List<ClaimdtVO> claimList) throws Exception{
		String 	  userId 		 = cancelInputVO.getProcId();

		ClaimdtVO claimdt = new ClaimdtVO();	
		claimdt.setOrderNo(orgOrderdt.getOrderNo());
		claimdt.setOrderGSeq(orgOrderdt.getOrderGSeq());
		claimdt.setOrderDSeq(orgOrderdt.getOrderDSeq());
		claimdt.setOrderWSeq(newOrderWseq);
		claimdt.setCustNo(orgOrderdt.getCustNo());
		claimdt.setReceiverSeq(orgOrderdt.getReceiverSeq());
		claimdt.setClaimDate(sysDateTime);
		claimdt.setLastProcId(userId);
		claimdt.setLastProcDate(sysDateTime);
		claimdt.setClaimAmt(0);
		claimdt.setDcAmt(orgOrderdt.getDcAmt());
		claimdt.setDcAmtGoods(orgOrderdt.getDcAmt());
		claimdt.setDcAmtMemb(orgOrderdt.getDcAmtMemb());
		claimdt.setDcAmtDiv(orgOrderdt.getDcAmtDiv());
		claimdt.setDcAmtCard(orgOrderdt.getDcAmtCard());
		claimdt.setRsaleAmt(orgOrderdt.getRsaleAmt());
		claimdt.setSaveamt(orgOrderdt.getSaveamt());
		claimdt.setSaveamtGb(orgOrderdt.getSaveamtGb());
		claimdt.setDoFlag("10");
		claimdt.setLastProcDate(sysDateTime);
		claimdt.setGoodsCode(orgOrderdt.getGoodsCode());
		claimdt.setGoodsdtCode(orgOrderdt.getGoodsdtCode());
		claimdt.setGoodsdtInfo(orgOrderdt.getGoodsdtInfo());
		claimdt.setClaimQty(cancelInputVO.getCancelQty());
		claimdt.setSyscancel(0);
		claimdt.setClaimGb("30");
		claimdt.setClaimDesc("제휴사 취소");   //해당 컬럼은 신세계에만 있는 컬럼.. (Insert문에서 제거)	
		claimdt.setClaimCode("999");
		claimdt.setCsLgroup(cancelInputVO.getCancelCode().substring(0, 2));
		claimdt.setCsMgroup(cancelInputVO.getCancelCode().substring(2, 4));
		claimdt.setCsSgroup(cancelInputVO.getCancelCode().substring(4, 6));
		claimdt.setCsLmsCode(cancelInputVO.getCancelCode());	
		claimdt.setClaimAmt(0);
		claimdt.setDcAmt(0);
		claimdt.setDcAmtGoods(0);
		claimdt.setDcAmtMemb(0);
		claimdt.setDcAmtDiv(0);
		claimdt.setDcAmtCard(0);
		claimdt.setRsaleAmt(0);
		claimdt.setSyslastDc(0);
		claimdt.setSyslastDcGoods(0);
		claimdt.setSyslastDcMemb(0);
		claimdt.setSyslastDcDiv(0);
		claimdt.setSyslastDcCard(0);
		claimdt.setSyslastAmt(0);
		claimdt.setHappyCardYn("0");
		claimdt.setRepayGb("0");
		claimdt.setExchPair("");
		claimdt.setRsalePaAmt(0);//= 제휴사 실결제금액
		claimdt.setRemark3N(0);
		claimList.add(claimdt);
	}
	
	private void settingCancel(CancelInputVO cancelInputVO, OrderdtVO orgOrderdt,String newOrderWseq, Timestamp sysDateTime, List<CanceldtVO> cancelDtList) throws Exception{
		String 	  userId 		 = cancelInputVO.getProcId();
		double rsalePaAmt		 = 0;
		CanceldtVO canceldtVO    = new CanceldtVO();
						
		canceldtVO.setBlank();
		canceldtVO.setOrderNo		(orgOrderdt.getOrderNo());
		canceldtVO.setOrderGSeq		(orgOrderdt.getOrderGSeq());
		canceldtVO.setOrderDSeq		(orgOrderdt.getOrderDSeq());
		canceldtVO.setOrderWSeq		(newOrderWseq);
		canceldtVO.setCustNo		(orgOrderdt.getCustNo());
		canceldtVO.setCustName		(orgOrderdt.getCustName());
		canceldtVO.setReceiverSeq	(orgOrderdt.getReceiverSeq());			
		canceldtVO.setCancelGb		("20");			
		canceldtVO.setCancelDate	(sysDateTime);
		canceldtVO.setDoFlag		(orgOrderdt.getDoFlag());
		canceldtVO.setOrgDoFlag		(orgOrderdt.getDoFlag());
		
		canceldtVO.setCancelCode	("999");
		canceldtVO.setCsLgroup		(cancelInputVO.getCancelCode().substring(0, 2));
		canceldtVO.setCsMgroup		(cancelInputVO.getCancelCode().substring(2, 4));
		canceldtVO.setCsSgroup		(cancelInputVO.getCancelCode().substring(4, 6));					
		canceldtVO.setCsLmsCode		(cancelInputVO.getCancelCode());
		
		canceldtVO.setGoodsGb		(orgOrderdt.getGoodsGb());
		canceldtVO.setMediaGb		(orgOrderdt.getMediaGb());
		canceldtVO.setMediaCode		(orgOrderdt.getMediaCode());
		canceldtVO.setGoodsCode		(orgOrderdt.getGoodsCode());
		canceldtVO.setGoodsdtCode	(orgOrderdt.getGoodsdtCode());
		canceldtVO.setGoodsdtInfo	(orgOrderdt.getGoodsdtInfo());
		canceldtVO.setSalePrice		(orgOrderdt.getSalePrice());
		canceldtVO.setBuyPrice		(orgOrderdt.getBuyPrice());
		canceldtVO.setCancelQty		(cancelInputVO.getCancelQty());
		canceldtVO.setCancelAmt		(orgOrderdt.getSalePrice() * canceldtVO.getCancelQty());
		canceldtVO.setDcRateGoods	(orgOrderdt.getDcRateGoods());
		canceldtVO.setDcRate		(orgOrderdt.getDcRate());
		canceldtVO.setDcAmtGoods	((orgOrderdt.getDcAmtGoods()/orgOrderdt.getOrderQty())*canceldtVO.getCancelQty());
		canceldtVO.setDcAmtMemb		(orgOrderdt.getDcAmtMemb());
		canceldtVO.setDcAmtDiv		(orgOrderdt.getDcAmtDiv());
		canceldtVO.setDcAmtCard		(orgOrderdt.getDcAmtCard());
		canceldtVO.setDcAmt			(canceldtVO.getDcAmtGoods());
		canceldtVO.setRsaleNet		((orgOrderdt.getRsaleNet()/orgOrderdt.getOrderQty()) * canceldtVO.getCancelQty());
		canceldtVO.setRsaleAmt		((orgOrderdt.getRsaleAmt()/orgOrderdt.getOrderQty()) * canceldtVO.getCancelQty());
		canceldtVO.setRsaleVat		(canceldtVO.getRsaleAmt() - canceldtVO.getRsaleNet());
		canceldtVO.setVatRate		(orgOrderdt.getVatRate());				
		canceldtVO.setMdCode		(orgOrderdt.getMdCode());
		canceldtVO.setSaleYn		(orgOrderdt.getSaleYn());
		canceldtVO.setWhCode		(orgOrderdt.getWhCode());
		canceldtVO.setDelyType		(orgOrderdt.getDelyType());
		canceldtVO.setDelyGb		(orgOrderdt.getDelyGb());
		canceldtVO.setDelyHopeDate	(orgOrderdt.getDelyHopeDate());
		canceldtVO.setDelyHopeYn	(orgOrderdt.getDelyHopeYn());
		canceldtVO.setDelyHopeTime	(orgOrderdt.getDelyHopeTime());
		canceldtVO.setPreoutGb		(orgOrderdt.getPreoutGb());
		canceldtVO.setSingleDueGb	(orgOrderdt.getSingleDueGb());
		canceldtVO.setSaveamt		(orgOrderdt.getSaveamt());
		canceldtVO.setSaveamtGb		(orgOrderdt.getSaveamtGb());			
		canceldtVO.setLastProcId	(userId);
		canceldtVO.setLastProcDate	(sysDateTime);
		canceldtVO.setModifyDate	(sysDateTime);
		canceldtVO.setRemark3N		((orgOrderdt.getRemark3N() / orgOrderdt.getOrderQty()) * canceldtVO.getCancelQty());
		canceldtVO.setEntpCode		(orgOrderdt.getEntpCode());
		
		if(canceldtVO.getOrderDSeq().equals("001")){
			rsalePaAmt = paorderDAO.selectRsalePaAmt(canceldtVO.getCancelGb(), canceldtVO.getOrderNo(), canceldtVO.getOrderGSeq(), canceldtVO.getOrderDSeq(), canceldtVO.getCancelQty());
			if(rsalePaAmt < 0) throw processException("pa.failed_to_calculate_rsale_pa_amt", new String[] {});
		}
		
		//SKB는 RsalePaAmt라는 컬럼이 없으므로, Remark4N에 넣기로 결의했다.
		canceldtVO.setRsalePaAmt(rsalePaAmt); //= 제휴사 실결제금액
		cancelDtList.add(canceldtVO);
	}
	
	/*
	private String checkOrderStt(OrderdtVO orgOrderdt){
		String orderGb;
		if(Integer.parseInt(orgOrderdt.getDoFlag()) <= 10){ 
			orderGb = "10";
		}else if(Integer.parseInt(orgOrderdt.getDoFlag()) > 10 &&Integer.parseInt(orgOrderdt.getDoFlag()) < 30){
			orderGb = "20";
		}else{
			orderGb = "30";
		}
		return orderGb;
	}
	*/
	private boolean checkSysLastForCancel(CancelInputVO cancelInputVO, OrderdtVO orgOrderdt, ParamMap paramMap , HashMap<String, Object> resultMap ){
		if(!orgOrderdt.getOrderDSeq().equals("001")) return true;
		
		if(cancelInputVO.getCancelQty() >  orgOrderdt.getSyslast()){
		
			resultMap.put("MAPPING_SEQ", 	cancelInputVO.getMappingSeq());
			resultMap.put("RESULT_CODE", 	"100001"); 
			resultMap.put("RESULT_MESSAGE", getMessage("pa.fail_cancel_input", new String[]{getMessage("pa.out_of_cancel_qty")}));
			return false;
		} else {
			paramMap.put("cancelQty"  , cancelInputVO.getCancelQty());
			paramMap.put("orgSyslast" , orgOrderdt.getSyslast());
			
			return true;
		}
	}
	
	
	private String selectWSeq(CancelInputVO cancelInputVO) throws Exception {
		String newOrderWseq = paorderDAO.selectWSeq(cancelInputVO);
		if(newOrderWseq == null) throw processException("SELECT W-SEQ IS NULL", new String[] {});
		return newOrderWseq;
	}
	
	private void getOriginalOrder(CancelInputVO cancelInputVO, List<OrderdtVO> orgOrderdtList, List<OrderdtVO> orgOrderdtList4ShipCost, List<OrderdtVO> orgOrderdtListAllOrder) throws Exception{
		List<OrderdtVO> tmpOrderdtList = null;
		try{
			tmpOrderdtList = paorderDAO.selectOrderDtList(cancelInputVO); 
			for(OrderdtVO vo :  tmpOrderdtList){
				if(vo.getSyslast() > 0 && cancelInputVO.getOrderGSeq().equals(vo.getOrderGSeq()) ){
					orgOrderdtList.add(vo);
				}
			}
			for(OrderdtVO vo :  tmpOrderdtList){
				if(cancelInputVO.getOrderGSeq().equals(vo.getOrderGSeq()) ){
					orgOrderdtList4ShipCost.add(vo);
				}
			}
			for(OrderdtVO vo :  tmpOrderdtList){
				orgOrderdtListAllOrder.add(vo);
			}
			
			if(orgOrderdtList.size() < 1)		   throw processException("partner.not.targetData", new String[] {"Origianl OrderData1"});
			if(orgOrderdtList4ShipCost.size() < 1) throw processException("partner.not.targetData", new String[] {"Origianl OrderData2"});
			if(orgOrderdtListAllOrder.size()  < 1) throw processException("partner.not.targetData", new String[] {"Origianl OrderData2"});

		}catch(Exception e){
			throw processException("partner.not.targetData", new String[] {"Origianl OrderData"}); 
		}
	}
	
	@Override
	public void upDateOrder(ParamMap paramMap) throws Exception {
		
		List<OrderdtVO> orderdtList 			= new ArrayList<OrderdtVO>();
		List<Orderstock> stocklist				= new ArrayList<Orderstock>();
		Orderreceipts orderReceipt				= null;
		String rtnValue							= "";
		if(paramMap.getString("siteGb").equals("PANAVER")) rtnValue = getMessage("pa.success_present_order_update", new String[]{});
		else rtnValue = getMessage("pa.success_pre_order", new String[]{});
		
		String orderNo 	  = paramMap.getString("orderNo");
		String paOrderNo  = paramMap.getString("paOrderNo");
		//String paOrderSeq = paramMap.getString("paOrderSeq");
		double paShpAmt   = paramMap.getDouble("paShpAmt");
		String userId	  = paramMap.getString("siteGb"); //updatePaOrderm20Approval에서 SiteGb에 대한 고려를 해야함.
		String dateTime   = getSysdatetimeToString();	
		Timestamp sysDateTime  	= DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss");	// 시간생성(Timestamp)
	
		

		try{
			//= 주소정제
			ParamMap addressParam = refineAddressInfo(paramMap);  //TODO 수정
			

			//= SELECT ORIGINAL ORDERLIST
			orderdtList = paorderDAO.selectOrderdtAll(orderNo);
			settingLastProcData(orderdtList, userId, sysDateTime);
			//= UPDATE ORDERDT
			updateOrderdt20Approval(orderdtList); 
			
			//= UPDATE ORDERRECEIPT
			orderReceipt = orderReceipts20Approval(orderdtList);	    
			updateOrderReceipt20Approval(orderReceipt);
			if(existOrderreceiptForShipping(orderReceipt)){ //배송비 존재
				updateOrderReceipts20ApprovalShipping(orderReceipt);
			}
			//= UPDATE ORDERSTOCK
			orderStock20Approval(orderdtList, stocklist);
			updateOrderStock20Approval(stocklist);	
			
			//= UPDATE AND INSERT RECEIVER
			saveReceiver(addressParam, paramMap , orderdtList);
			
			//= TPAORDERSHIPCOST UPDATE
			updatePaOrderShipCost	(orderNo, "001", paShpAmt);
			updatePaOrderm20Approval(orderNo, paOrderNo,  rtnValue, userId); //orderNo 추가
			
			
		}
		catch(Exception e){
			if(e.getMessage() == null){
				rtnValue = getMessage("pa.fail_order_input", new String[]{e.toString()});
			} else {
				rtnValue = getMessage("pa.fail_order_input", new String[]{e.getMessage()});
			}
			
			throw processMessageException(rtnValue);			
		}
	}

	
	private void saveReceiver(ParamMap addressParam, ParamMap paramMap , List<OrderdtVO> orderdtList) throws Exception {
		int executedRtn = 0;
		String receiverSeq = paramMap.getString("receiverSeq");
		String custNo	   = orderdtList.get(0).getCustNo();
		String receiverTel = paramMap.getString("receiverTel");
		String receiverHp  = paramMap.getString("receiverHp");
		paramMap.put("custNo"		, custNo);
		paramMap.put("beforeSeq"	, paramMap.getString("receiverSeq"));
		//paramMap.put("receiverName"	, orderdtList.get(0).getCustName());
		paramMap.put("receiverName"	, paramMap.getString("receiverName"));
		paramMap.put("orderNo"		, orderdtList.get(0).getOrderNo());
		paramMap.put("userId"		, orderdtList.get(0).getLastProcId());
		paramMap.put("sysDateTime"	, orderdtList.get(0).getLastProcDate());
		
		
		
		List<Receiver> receiverList = new ArrayList<Receiver>();

		Receiver receiver = new Receiver();
		receiver.setCustNo					(custNo);
		receiver.setBeforeSeq				(receiverSeq);
		receiver.setReceiverTel				(receiverTel);
		receiver.setReceiverHp				(receiverHp);
		receiver.setReceiverRoadPostAddr	(addressParam.getString("STD_ROAD_POST_ADDR1"));
		receiver.setReceiverRoadPostAddr2	(addressParam.getString("STD_ROAD_POST_ADDR2"));
		receiver.setRoadPostAddr			(addressParam.getString("ROAD_POST_ADDR"));
		receiver.setRoadAddr				(addressParam.getString("ROAD_ADDR"));
		receiver.setDefaultYn				("1");
		receiver.setModifyDate				(orderdtList.get(0).getLastProcDate());
		receiver.setModifyId				(orderdtList.get(0).getLastProcId());
		
		String checkAddrStatus = paorderDAO.checkAddressStatus(receiver);
		
		switch(checkAddrStatus) {
		
		case "-1":  //기존것이 주소 정제 실패..
			//주소 정제 프로그램을 통해 해결
			break;
			
		case "0":  //아무 변화 없음
			break;
			
		case "1":  //주소가 바뀐경우
			//= make old reciever to useless 
			executedRtn = paorderDAO.updateDisable(receiver);
			if (executedRtn < 1) throw processException("msg.cannot_save", new String[] {"TORDERRECEIPT OLD UPDATE"});
			
			//= Setting new Receiver
			paramMap.put("receiverSeq", paorderDAO.getNewReceiverSeq(custNo)); 
			setReceiver(paramMap , addressParam , receiverList);

			//= Insert new Receiver
			executedRtn = paorderDAO.insertReceiver(receiverList.get(0));  //기존 함수를 쓰기위해 형태만 리스트로 가져온것이 때문에 0으로 픽스(다중배송지 처리 지원 x)
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"TRECEIVER NEW INSERT"});
			}	
			
			
			//= Change OrderDt's ReceiverSeq 
			executedRtn = paorderDAO.updateOrderDtForReceiverSeq(paramMap);  //기존 함수를 쓰기위해 형태만 리스트로 가져온것이 때문에 0으로 픽스(다중배송지 처리 지원 x)
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"UPDATE ORDERDT RECEIVERSEQ"});
			}
			break;
		
		case "2":  //주소는 같고 전화번호가 바뀐경우
			ParamMap teleParam = new ParamMap();
			teleParam.put("tel"			 , paramMap.getString("receiverTel").replace("-", ""));
			teleParam.put("hp"			 , paramMap.getString("receiverHp").replace("-", ""));
			setPhone(teleParam);  
			
			receiver.setTel						(teleParam.getString("tel"));
			receiver.setReceiverSeq				(receiverSeq);
			receiver.setReceiverDdd				(teleParam.getString("ddd"));
			receiver.setReceiverTel1			(teleParam.getString("tel1"));
			receiver.setReceiverTel2			(teleParam.getString("tel2"));
			receiver.setReceiverHp				(teleParam.getString("hp"));
			receiver.setReceiverHp1				(teleParam.getString("dddH"));
			receiver.setReceiverHp2	 			(teleParam.getString("tel1H"));
			receiver.setReceiverHp3				(teleParam.getString("tel2H"));
			
			executedRtn = paorderDAO.updateTelInfo(receiver);  //기존 함수를 쓰기위해 형태만 리스트로 가져온것이 때문에 0으로 픽스(다중배송지 처리 지원 x)
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"UPDATE HP&TEL INFOMATION"});
			}
			break;
		
		
		default:   // pass
			
		}
		
	}

	private void settingLastProcData(List<OrderdtVO> orderdtList, String procId, Timestamp procDate)  {
		for(int i = 0; i < orderdtList.size() ; i++ ){
			orderdtList.get(i).setLastProcDate	(procDate);
			orderdtList.get(i).setLastProcId	(procId);
		}
	
	}

	private boolean existOrderreceiptForShipping(Orderreceipts orderReceipt) throws Exception{
		orderReceipt.setSettleGb("15");
		int cnt = paorderDAO.selectOrderreceipt(orderReceipt);		
		if(cnt > 0){
			return true;
		}else{
			return false;
		}
	}		
	private int updateOrderReceipt20Approval(Orderreceipts orderReceipt) throws Exception{
		int executedRtn = 0;	
		
		executedRtn = paorderDAO.updateOrderReceipts20Approval(orderReceipt);
	
		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] {"TORDERRECEIPT UPDATE"});
		}

		return executedRtn;
	}
	
	private int updateOrderReceipts20ApprovalShipping(Orderreceipts orderReceipt) throws Exception{
		int executedRtn = 0;	
		orderReceipt.setSettleGb("15");
		executedRtn = paorderDAO.updateOrderReceipts20ApprovalShipping(orderReceipt);
	
		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] {"TORDERRECEIPT SHIPPING UPDATE"});
		}

		return executedRtn;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap<String, Object>[] newSaveOrder(OrderInputVO[] orderInputVO) throws Exception {
		
		/**Input Value**/
		int paramLength							= orderInputVO.length;
		ParamMap[]  paramMap 					= new ParamMap[paramLength];
		Orderm     orderm     					= new Orderm();
		Customer   customer  					= new Customer();
		Custsystem custsystem 					= new Custsystem();
		List<OrderdtVO> orderdtList 			= new ArrayList<OrderdtVO>();
		List<Orderpromo> orderpromoList			= new ArrayList<Orderpromo>();
		List<Orderreceipts> orderreceiptsList 	= new ArrayList<Orderreceipts>();
		List<Receiver> receiverList				= new ArrayList<Receiver>();
		List<Ordershipcost> ordershipcostList 	= new ArrayList<Ordershipcost>();
		List<Ordercostapply> ordercostapplyList = new ArrayList<Ordercostapply>();
		List<Promocounsel> promocounselList		= new ArrayList<Promocounsel>();
		List<Ordergoods> ordergoodsList			= new ArrayList<Ordergoods>(); 
		List<PaOrderShipCost> pashipcostList	= new ArrayList<PaOrderShipCost>(); 
		List<Orderstock> stocklist				= new ArrayList<Orderstock>();
		List<Couponissue> couponissueList    	= new ArrayList<Couponissue>(); 

		String    dateTime       	 			= getSysdatetimeToString();	
		Timestamp sysDateTime   	 			= DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss");	// 시간생성(Timestamp)
		ParamMap  addressParam 					= null;
		
		/** OutPut Value **/
		String rtnValue 			 			= "000000";
		HashMap[] resultMap 		 			= new HashMap[paramLength];
			
		try{
			//SETTING LINE- --------	
			//=1)주소정제
			addressParam = refineAddressInfo(orderInputVO);
			//=2)주문 정보 생성 
			settingOrderInfo(orderInputVO , addressParam, sysDateTime ,paramMap);
			//=3)재고체크
			if(!checkStock(paramMap, resultMap)) return resultMap;//재고없음
			//=4)고객정보 생성 
			customer 	= settingCustmer		(paramMap);
			custsystem	= settingCustsystem		(paramMap);
			//=5)주문 기초 정보 생성(OrderM)
			orderm 		= settingOrderM			(paramMap);
			
			//=6)주문 상세 정보 생성 
			for (int i = 0; i < paramLength; i++) {
				if(paramMap[i].getString("orderPossYn").equals("0")) continue;
				//= 수령지 세팅(TRECEIVER)
				newReceiver(paramMap, addressParam , i, receiverList);
				//= 본품 세팅 (TORDERDT)
				newOrderDt (paramMap, i , orderdtList);
				//= 사은품 세팅 (TPROMOCOUNSEL, TORDERDT, TORDERPROMO)
				newGiftPromo(paramMap, i , promocounselList, orderdtList, orderpromoList);
			}
			
			//= insert TCUSTOMER, TCUSTSYSTEM, TRECEIVER !!배송비 계산을 위해 해당 함수는 절대 위치 바꾸지 마시오!!
			saveCustomer(customer, custsystem, null, receiverList); 
			
			//=7)배송비 계산 및 세팅 (TORDERSHIPCOST, TORDERCOSTAPPLY)  
			HashMap<String, Object> obj = new HashMap<>();					
			obj= calcShipCost(orderdtList, receiverList); //TODO 20220809 calcShipCostN으로 교체 필요해 보임
			ordershipcostList = (List<Ordershipcost>) obj.get("ordershipcostList");
			ordercostapplyList = (List<Ordercostapply>) obj.get("ordercostapplyList");	
			
			//=8)TORDERGOODS 세팅
			newOrderGoods(orderdtList, ordergoodsList);
			//=9)TORDERPROMO 세팅
			newOrderPromo(orderdtList, orderpromoList, couponissueList);
			//=10)TORDERRECEIPT 세팅
			newOrderReceipt(orderdtList, ordershipcostList, orderreceiptsList);
			//=11)TPAORDERSHIPCOST 세팅
			newPaOrderShipCost(orderdtList, receiverList, ordershipcostList, orderreceiptsList, pashipcostList);
			//=12)TORDERSTOCK 세팅
			newOrderStock(orderdtList, stocklist);
			
			//INSERT ORDER LINE- ---	
			//= insert TORDERM
			insertOrderm(orderm);
			//= insert TORDERGOODS
			insertOrdergoods(ordergoodsList);
			//= insert TORDERDT
			insertOrderdt(orderdtList);	
			//= insert TORDERSHIPCOST
			insertOrdershipcost(ordershipcostList);
			//= insert TORDERCOSTAPPLY
			insertOrderCostApply(ordercostapplyList);
			//= insert TORDERPROMO
			insertOrderpromo(orderpromoList);
			//= insert TCOUPONISSUE
			insertCouponissue(couponissueList);
			//= insert TPROMOCOUNSEL 
			insertPromoCounsel(promocounselList);	
			//= insert TORDERRECEIPTS 
			insertOrderreceipts(orderreceiptsList);
			//= insert TPAORDERSHIPCOST
			insertPaOrderShipCost(pashipcostList);	
			//= update TORDERSTOCK
			updateOrderStock(stocklist);
			
			//= 스토아 데이터 정상 처리 후 중간 테이블(Paorderm) 처리 부
			settingResultMap(resultMap, paramMap);
			updatePaOrderM(resultMap);
			
			// 개인정보 수집출처 고지 SMS 발송
			sendPaPersonalInfoNotice(paramMap);
			
			return resultMap;
			
		}catch(Exception e){
			if(e.getMessage() == null){
				rtnValue = getMessage("pa.fail_order_input", new String[]{e.toString()});
			} else {
				rtnValue = getMessage("pa.fail_order_input", new String[]{e.getMessage()});
			}
			//settingFailResultMap(resultMap, paramMap , rtnValue);
			throw processMessageException(rtnValue);	
		}
	}
	
	private int insertCouponissue(List<Couponissue> couponissueList) throws Exception{
		int executedRtn = 0;
		for(Couponissue cp: couponissueList){
			executedRtn = paorderDAO.insertCouponissue(cp);	
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"TCOUPONISSUE INSERT"});
			}	
		}
		return executedRtn;	
		
	}

	private void updatePaOrderm20Approval(String orderNo, String paOrderNo, String message, String userId) throws Exception{
		int executedRtn = 0;
		ParamMap resultParamMap = new ParamMap();
		
		try {
			resultParamMap.put("orderNo"			, orderNo );
			resultParamMap.put("paOrderNo"			, paOrderNo );
			resultParamMap.put("apiResultCode"		, "000000");
			resultParamMap.put("apiResultMessage"   , ComUtil.subStringBytes(message, 300));
			
			if(userId == null || (userId != null && !userId.equals("PANAVER"))) executedRtn = paorderDAO.updatePaOrderm20Approval(resultParamMap);
			else executedRtn = paorderDAO.updateNaverPaOrderm20Approval(resultParamMap);
			if(executedRtn < 1){
				throw processException("msg.cannot_save", new String[] { "TPAORDERM-20 UPDATE" });
			}
			
		}
		catch (Exception e) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM-20 UPDATE " });
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updatePaOrderM(HashMap[] resultMap) throws Exception{
		int executedRtn = 0;
		ParamMap resultParamMap = new ParamMap();
		
		try {		
			for(HashMap map :  resultMap ){	
				resultParamMap.setParamMap(map);
				resultParamMap.replaceCamel();	
				if(resultParamMap.getString("resultCode").equals("000000")){
					resultParamMap.put("createYn", "1");
				} else {
					resultParamMap.put("createYn", "0");
				}		
				executedRtn = paorderDAO.updatePaOrderm(resultParamMap);
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
				}
			}
		} catch (Exception e) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE " });
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void sendPaPersonalInfoNotice(ParamMap[] paramMap) throws Exception{
		if(null != paramMap) {
			int executedRtn = 0;
			
			String hp = ComUtil.NVL(paramMap[0].getString("receiverHp").replace("-", ""));
			String tel = ComUtil.NVL(paramMap[0].getString("receiverTel").replace("-", ""));
			
			List<HashMap<String, Object>> temp = new ArrayList<HashMap<String, Object>>();
			temp = paorderDAO.selectPaPersonalInfoSms(paramMap[0]);
			String msg = "";
			if(null != temp && temp.size() > 0) {
				HashMap<String, Object> info = temp.get(0);
				
				if(info.get("CNT").toString().equals("0")) {
					try {		
						if( ("".equals(hp) && "".equals(tel)) || info.size() < 1 ) {
							return;
						} else {
							SmsSendVO smsSendVO = new SmsSendVO();
							smsSendVO.setReceiveNo(hp != "" ? hp : tel); // 휴대폰번호
							
							smsSendVO.setSendFlag("04"); // 발송구분 
							smsSendVO.setBulkYn("1"); //대량발송
							
							smsSendVO.setCustNo(paramMap[0].getString("custNo")); // 고객번호
							smsSendVO.setPaName(info.get("PA_NAME").toString());
							smsSendVO.setSendNo(info.get("SEND_NO").toString());
							smsSendVO.setCustName(paramMap[0].getString("custName"));
							
							//문자내용 setting
							paramMap[0].put("sendFlag", "04");
							paramMap[0].put("data1", info.get("PA_NAME").toString());
							paramMap[0].put("data2", paramMap[0].getString("custName"));
							msg = paorderDAO.selectTransMsg(paramMap[0]);
							smsSendVO.setMsg(msg);
							
							umsBizProcess.sendSms(smsSendVO);
							
							PaPersonalInfoNoticeSms personalInfo = new PaPersonalInfoNoticeSms();
							
							personalInfo.setOrderNo(paramMap[0].getString("orderNo"));
							personalInfo.setCustNo(paramMap[0].getString("custNo"));
							personalInfo.setMediaCode(paramMap[0].getString("mediaCode"));
							personalInfo.setInsertId(paramMap[0].getString("userId"));
							personalInfo.setReceiverHp(smsSendVO.getReceiveNo());
							personalInfo.setMsg(msg);
							
							executedRtn = paorderDAO.insertPaPersonalInfoNoticeSmsList(personalInfo);
							if(executedRtn < 1) {
								throw processException("msg.cannot_save", new String[] { "paOrderProcessImpl - TPAPERSONALINFONOTICE Insert" });
							}
						}
					} catch (Exception e) {
						throw processException("msg.cannot_save", new String[] { "SEND PAPERSONALINFO NOTICE" });
					}
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void settingFailResultMap(HashMap[] resultMap, ParamMap[] paramMap, String message ){
		int paramLength = paramMap.length;
		for(int k = 0; paramLength > k; k++){
			resultMap[k] = new HashMap<>();
			resultMap[k].put("MAPPING_SEQ"		, paramMap[k].getString("mappingSeq"));
			resultMap[k].put("ORDER_NO"			, paramMap[k].getString("orderNo"));
			resultMap[k].put("RESULT_CODE"		, "999999"); 
			resultMap[k].put("RESULT_MESSAGE"	, ComUtil.subStringBytes(message, 300) );
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void settingResultMap(HashMap[] resultMap, ParamMap[] paramMap ){
		
		int paramLength = paramMap.length;
		
		for(int k = 0; paramLength > k; k++){
			resultMap[k] = new HashMap<>();
			resultMap[k].put("MAPPING_SEQ"		, paramMap[k].getString("mappingSeq"));
			resultMap[k].put("ORDER_NO"			, paramMap[k].getString("orderNo"));
			resultMap[k].put("SITE_GB"			, paramMap[k].getString("userId"));

		
			if(paramMap[k].getString("orderPossYn").equals("1")){
				
				resultMap[k].put("ORDER_G_SEQ", 	paramMap[k].getString("orderGSeq"));
				resultMap[k].put("ORDER_D_SEQ", 	paramMap[k].getString("orderDSeq"));
				resultMap[k].put("ORDER_W_SEQ", 	paramMap[k].getString("orderWSeq"));
				resultMap[k].put("RESULT_CODE", 	"000000"); 
				resultMap[k].put("RESULT_MESSAGE", 	 getMessage("pa.success_order_input"));			
			}
			//3) 재고나 판매불가로 인한 거부
			else {
				resultMap[k].put("RESULT_CODE", 	"100001"); 
				resultMap[k].put("RESULT_MESSAGE", 	getMessage("pa.fail_order_input", new String[]{getMessage("pa.out_of_stock")}));
			}
		}
	}
	
	private void settingResultMap(CancelInputVO cancelInputVO , List<CanceldtVO> cancelList, HashMap<String, Object> resultMap){
		resultMap.put("MAPPING_SEQ", 	cancelInputVO.getMappingSeq());
		resultMap.put("ORDER_NO", 		cancelList.get(0).getOrderNo());
		resultMap.put("ORDER_G_SEQ", 	cancelList.get(0).getOrderGSeq());
		resultMap.put("ORDER_D_SEQ", 	"001");
		resultMap.put("ORDER_W_SEQ", 	cancelList.get(0).getOrderWSeq());
		resultMap.put("RESULT_CODE", 	"000000"); 
		resultMap.put("RESULT_MESSAGE",	getMessage("pa.success_cancel_input")); 
	}

	
	private int insertPromoCounsel(List<Promocounsel> promocounselList) throws Exception {
		int executedRtn = 0;
		Promocounsel promocounsel = null;
		
		for(int i = 0; promocounselList.size() > i; i++){
			promocounsel = new Promocounsel();
			promocounsel = promocounselList.get(i);
			executedRtn = paorderDAO.insertPromoCounsel(promocounsel);
			
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"TPROMOCOUNSEL INSERT"});
			}
		}
		return executedRtn;	
	}

	private void newPaOrderShipCost(List<OrderdtVO> orderdtList, List<Receiver> receiverList , List<Ordershipcost> ordershipcostList, List<Orderreceipts> orderreceiptsList , List<PaOrderShipCost> paOrderShipCost){
		long shipcostAmt 			= calcFeeAmt(ordershipcostList);
		long paShipcostAmt  		= calcPaShipCostAmt(orderdtList);
		String receipNo	    		= getShipCostReceiptNo(orderreceiptsList);
		boolean isRefinedAddress 	= checkRefineAddress(receiverList);
		
		if(receipNo.equals("999999")) return;
		if(shipcostAmt   <= 0  && paShipcostAmt <= 0 ) return;						 // 제휴배송비 무료, 스토아 배송비 무료
		if(shipcostAmt   <= 0  && paShipcostAmt > 0 &&  !isRefinedAddress ) return;  // 제휴배송비 유료, 스토이 배송비 무료, 정제 실패  -> 해당 CASE에는 미정배송지 프로그램 에서 계산해서 넣는다
		
		newPaOrderShipcost(orderdtList.get(0), receipNo, paShipcostAmt, shipcostAmt, paOrderShipCost);
	}
	
	private void newOrderReceipt(List<OrderdtVO> orderdtList, List<Ordershipcost> ordershipcostList, List<Orderreceipts> orderreceiptsList) throws Exception{
		long rsaleAmt 	 = calcRsalAmt(orderdtList);
		long shipcostAmt = calcFeeAmt(ordershipcostList);
		ParamMap param = new ParamMap();
		OrderdtVO order = orderdtList.get(0);
		//= 물건에 대한 영수 내역 세팅
		preOrderReceitpSetting(order ,"11", rsaleAmt, param);    //무통장 check
		newOrderReceipt(param ,orderreceiptsList);     //객체생성
		//= 배송비에 대한 영수 내역 세팅
		preOrderReceitpSetting(order, "15", shipcostAmt, param); //무통장 check
		newOrderReceipt(param ,orderreceiptsList);     //객체생성
		
	}
	
	private void newPaOrderShipcost(OrderdtVO order , String receiptNo ,long paShipCostAmt , long shipCostAmt, List<PaOrderShipCost> paOrderShipCost){		
		
		PaOrderShipCost paordershipcost = new PaOrderShipCost();
		paordershipcost.setOrderNo				(order.getOrderNo());
		paordershipcost.setSeq					("001");
		paordershipcost.setOutReceiverSeq		(order.getReceiverSeq()); //추후에 다중배송비 지원때 고려해보아야 할 부분..
		paordershipcost.setReturnReceiverSeq	("");
		paordershipcost.setCustNo				(order.getCustNo());
		paordershipcost.setOrderDate			(order.getOrderDate());
		paordershipcost.setReceiptNo			(receiptNo);
		paordershipcost.setType					("10");
		paordershipcost.setPaShpFeeAmt			(paShipCostAmt);
		paordershipcost.setShpFeeAmt			(shipCostAmt);
		paordershipcost.setManualCancelAmt		(0);
		paordershipcost.setShpFeeYn				(1);         //0무상 1유상 2 협의
		paordershipcost.setInsertId				(order.getLastProcId());
		paordershipcost.setInsertDate			(order.getLastProcDate());
		paOrderShipCost.add(paordershipcost);
		
	}
	
	private void preOrderReceitpSetting(OrderdtVO order,String settleGb, long amt, ParamMap param){
		if(Integer.parseInt(order.getDoFlag()) > 10){ 			
			param.put("doFlag"		, "90");
			param.put("receiptAmt"	, amt);
			param.put("endYn"		, "1");
		}else{
			param.put("doFlag"		, "10");
			param.put("receiptAmt"	, "0");
			param.put("endYn"		, "0");
		}
		param.put("payAmt" 			, amt );
		param.put("settleGb"		, settleGb);
		param.put("custName"		, order.getCustName());
		param.put("custNo"			, order.getCustNo());
		param.put("orderNo"			, order.getOrderNo());
		param.put("orderDate"		, order.getOrderDate());
		param.put("orderDoFlag"		, order.getDoFlag());
		param.put("insertId"		, order.getLastProcId());
	}
	
	private void preCancelReceitpSetting(OrderdtVO order,CanceldtVO cancel, String settleGb, long amt, ParamMap param){
		
		param.put("doFlag"			, "10");
		param.put("orderDoFlag"		, "10");
		param.put("receiptAmt"		, "0");
		param.put("endYn"			, "0");	
		param.put("payAmt" 			, amt );
		param.put("settleGb"		, settleGb);
		param.put("custName"		, order.getCustName());
		param.put("custNo"			, order.getCustNo());
		param.put("orderNo"			, order.getOrderNo());
		param.put("orderDate"		, cancel.getLastProcDate());
		param.put("insertId"		, cancel.getLastProcId());
	}
	
	private void newOrderReceipt(ParamMap param, List<Orderreceipts> orderreceiptsList) throws Exception{
	
	    Timestamp receiptPlanDate = DateUtil.addDay((Timestamp)param.get("orderDate"), 2); // 자연일 + 2
	    Timestamp sysdateTime	  = (Timestamp)param.get("orderDate");
		Timestamp okDate		  = null;
		Timestamp receiptDate	  = null;
		String 	  userId		  = param.getString("insertId");
		
		//if(Long.parseLong(param.getString("payAmt")) <= 0) return;
		if(Integer.parseInt(param.getString("orderDoFlag")) > 10){ 
			receiptDate 	= sysdateTime;
			okDate			= sysdateTime;
			receiptPlanDate	= null;
		}
		
		String receiptNo = selectSequenceNo("RECEIPT_NO");

		Orderreceipts orderreceipts = new Orderreceipts();	
		orderreceipts.setBlank						();
		orderreceipts.setReceiptNo					(receiptNo);
		orderreceipts.setOrderNo					(param.getString("orderNo"));
		orderreceipts.setCustNo						(param.getString("custNo"));
		orderreceipts.setDoFlag						(param.getString("doFlag"));
		orderreceipts.setSettleGb					(param.getString("settleGb"));  //외상매출 11, 배송비 15 , 결제구분 G0001 
		orderreceipts.setCardBankCode				("");
		orderreceipts.setBankSeq					("");
		orderreceipts.setCardName					("");
		orderreceipts.setCardNo						("");
		orderreceipts.setCvv						("");		
		orderreceipts.setDepositor					(param.getString("custName"));
		orderreceipts.setValidDate					("");
		orderreceipts.setQuestAmt					(Long.parseLong(param.getString("payAmt")));
		orderreceipts.setReceiptPlanDate			(receiptPlanDate);
		orderreceipts.setOkNo						("");
		orderreceipts.setOkDate						(okDate);
		orderreceipts.setOkMed						("000");
		orderreceipts.setOkErrorCode				("0000");
		orderreceipts.setVanComp					("");
		orderreceipts.setPayMonth					(0);
		orderreceipts.setNorestYn					("0");
		orderreceipts.setNorestRate					(0);
		orderreceipts.setNorestAmt					(0);
		orderreceipts.setReceiptAmt					(Long.parseLong(param.getString("receiptAmt")));
		orderreceipts.setReceiptDate				(receiptDate);
		orderreceipts.setEndYn						(param.getString("endYn"));		
		orderreceipts.setRepayPbAmt					(orderreceipts.getQuestAmt());		
		orderreceipts.setCancelYn					("0");
		orderreceipts.setCancelCode					("000");
		orderreceipts.setCancelDate					(null);
		orderreceipts.setCancelId					("");
		orderreceipts.setSaveamtUseFlag				("0");
		orderreceipts.setCodDelyYn					("0");
		orderreceipts.setDivideYn					("0");
		orderreceipts.setPartialCancelYn			("");
		orderreceipts.setProtxVendortxcode			("");
		orderreceipts.setProtxStatus				("");
		orderreceipts.setProtxStatusdetail			("");
		orderreceipts.setProtxVpstxid				("");
		orderreceipts.setProtxSecuritykey			("");
		orderreceipts.setProtxTxauthno				("");
		orderreceipts.setIssueNumber				("");
		orderreceipts.setPayboxIdentifiant			("");
		orderreceipts.setPayboxCodetraitement		("");
		orderreceipts.setPayboxPays					("");
		orderreceipts.setPayboxDateq				("");
		orderreceipts.setPayboxReference			("");
		orderreceipts.setCardPassword				("");
		orderreceipts.setRemark1V					("");
		orderreceipts.setRemark2V					("");
		orderreceipts.setRemark3N					(0);
		orderreceipts.setRemark4N					(0);
		orderreceipts.setRemark5D					(null);
		orderreceipts.setRemark6D					(null);
		orderreceipts.setRemark						("");
		orderreceipts.setProcId						(userId);
		orderreceipts.setProcDate					(sysdateTime);
		orderreceipts.setInsertId					(userId);
		orderreceipts.setInsertDate					(sysdateTime);
		
		orderreceiptsList.add(orderreceipts);
	}
	
	private void newOrderPromo(List<OrderdtVO> orderdtList, List<Orderpromo> orderpromoList, List<Couponissue> couponissue) throws Exception{
	
		for (OrderdtVO orderdt : orderdtList ) {
			long arsDcAmt = 0;
			long LumpSumDcAmt = 0;
			long promotionDiscountPrice = 0;
			long paPromotionDiscountPrice = 0;//제휴OUT프로모션 할인액 : 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
			String procGb = "";
			
			if(orderdt.getOrderDSeq().equals("001")){ //= 본품
				arsDcAmt 	 = orderdt.getArsDcAmt();
				LumpSumDcAmt = orderdt.getLumpSumDcAmt();
				
				if(orderdt.getOrderPromo() != null){
					procGb = orderdt.getOrderPromo().getProcGb();					
					if(procGb.equals("D")){
						promotionDiscountPrice = 0;
					}else if("11".equals(orderdt.getOrderPromo().getUseCode())   //TODO 추후에 !"00".equals~ 로 개선 필요
							|| "12".equals(orderdt.getOrderPromo().getUseCode())
							|| "13".equals(orderdt.getOrderPromo().getUseCode())
							|| "14".equals(orderdt.getOrderPromo().getUseCode())
							|| "15".equals(orderdt.getOrderPromo().getUseCode())) {
						promotionDiscountPrice = 0;
					}else{
						promotionDiscountPrice  = (long) orderdt.getOrderPromo().getProcCost(); //개당 금액						
					}					
				}
				
				//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
				if(orderdt.getOrderPaPromo() != null) {
					procGb = orderdt.getOrderPaPromo().getProcGb();
					if(procGb.equals("D")) {
						paPromotionDiscountPrice = 0;
					} else {
						paPromotionDiscountPrice = (long) orderdt.getOrderPaPromo().getProcCost(); //개당 금액
					}
				}
				//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
				
				if(arsDcAmt > 0){ //= ARS할인내역 setting 
					//= ARS 할인 (do_type : 90) setting
					Orderpromo orderpromo = new Orderpromo();
					orderpromo.setSeq			(selectSequenceNo("ORDERPROMO_SEQ"));
					orderpromo.setPromoNo		(paorderDAO.selectPromoNo("90"));
					orderpromo.setDoType		("90");
					orderpromo.setOrderNo		(orderdt.getOrderNo());
					orderpromo.setOrderGSeq		(orderdt.getOrderGSeq());
					orderpromo.setProcAmt		(arsDcAmt);
					orderpromo.setCancelAmt		(0);
					orderpromo.setClaimAmt		(0);
					orderpromo.setProcCost		(arsDcAmt / orderdt.getOrderQty());
					orderpromo.setOwnProcCost	(orderdt.getOwnProcCost());
					orderpromo.setEntpProcCost	(orderdt.getEntpProcCost());
					orderpromo.setEntpCode		(orderdt.getEntpCode());
					orderpromo.setCancelYn		("0");
					orderpromo.setCancelDate	(null);
					orderpromo.setCancelId		("");
					orderpromo.setRemark		("");
					orderpromo.setInsertId		(orderdt.getLastProcId());
					orderpromo.setInsertDate	(orderdt.getLastProcDate());
					orderpromoList.add(orderpromo);
				}
				
				if(LumpSumDcAmt > 0){ //= 일시불 할인내역 setting 
					//= 일시불 할인 (do_type : 70) setting
					Orderpromo orderpromo = new Orderpromo();
					orderpromo.setSeq			(selectSequenceNo("ORDERPROMO_SEQ"));
					orderpromo.setPromoNo		(paorderDAO.selectLumpPromoNo(orderdt));
					orderpromo.setDoType		("70");
					orderpromo.setOrderNo		(orderdt.getOrderNo());
					orderpromo.setOrderGSeq		(orderdt.getOrderGSeq());
					orderpromo.setProcAmt		(LumpSumDcAmt);
					orderpromo.setCancelAmt		(0);
					orderpromo.setClaimAmt		(0);
					orderpromo.setProcCost		(LumpSumDcAmt / orderdt.getOrderQty());
					orderpromo.setOwnProcCost	(orderdt.getLumpSumOwnDcAmt());
					orderpromo.setEntpProcCost	(orderdt.getLumpSumEntpDcAmt());
					orderpromo.setEntpCode		(orderdt.getEntpCode());
					orderpromo.setCancelYn		("0");
					orderpromo.setCancelDate	(null);
					orderpromo.setCancelId		("");
					orderpromo.setRemark		("");
					orderpromo.setInsertId		(orderdt.getLastProcId());
					orderpromo.setInsertDate	(orderdt.getLastProcDate());
					orderpromoList.add(orderpromo);				
					
				}
				
				if(promotionDiscountPrice > 0){
					Orderpromo orderpromo = new Orderpromo();
					orderpromo.setSeq			(selectSequenceNo("ORDERPROMO_SEQ"));
					orderpromo.setPromoNo		(orderdt.getOrderPromo().getPromoNo());
					orderpromo.setDoType		(orderdt.getOrderPromo().getDoType());
					orderpromo.setOrderNo		(orderdt.getOrderNo());
					orderpromo.setOrderGSeq		(orderdt.getOrderGSeq());
					orderpromo.setProcAmt		(promotionDiscountPrice * orderdt.getOrderQty());
					orderpromo.setCancelAmt		(0);
					orderpromo.setClaimAmt		(0);
					orderpromo.setProcCost		(promotionDiscountPrice);
					orderpromo.setOwnProcCost	(orderdt.getOrderPromo().getOwnProcCost() );
					orderpromo.setEntpProcCost	(orderdt.getOrderPromo().getEntpProcCost());
					orderpromo.setEntpCode		(orderdt.getEntpCode());
					orderpromo.setCancelYn		("0");
					orderpromo.setCancelDate	(null);
					orderpromo.setCancelId		("");
					orderpromo.setRemark		("");
					orderpromo.setInsertId		(orderdt.getLastProcId());
					orderpromo.setInsertDate	(orderdt.getLastProcDate());
					orderpromoList.add(orderpromo);
					
					//For CouponIssue
					orderdt.getOrderPromo().setOrderNo    (orderdt.getOrderNo());
					orderdt.getOrderPromo().setCustNo     (orderdt.getCustNo());
					orderdt.getOrderPromo().setInsertDate (orderdt.getLastProcDate());
					orderdt.getOrderPromo().setInsertId	  (orderdt.getLastProcId());
					if("1".equals(orderdt.getOrderPromo().getCouponYn())){
						newCouponissue(orderdt.getOrderPromo(), couponissue);						
					}
				}
				
				//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
				if(paPromotionDiscountPrice > 0){
					Orderpromo orderpromo = new Orderpromo();
					orderpromo.setSeq			(selectSequenceNo("ORDERPROMO_SEQ"));
					orderpromo.setPromoNo		(orderdt.getOrderPaPromo().getPromoNo());
					orderpromo.setDoType		(orderdt.getOrderPaPromo().getDoType());
					orderpromo.setOrderNo		(orderdt.getOrderNo());
					orderpromo.setOrderGSeq		(orderdt.getOrderGSeq());
					orderpromo.setProcAmt		(paPromotionDiscountPrice * orderdt.getOrderQty());
					orderpromo.setCancelAmt		(0);
					orderpromo.setClaimAmt		(0);
					orderpromo.setProcCost		(paPromotionDiscountPrice);
					orderpromo.setOwnProcCost	(orderdt.getOrderPaPromo().getOwnProcCost());
					orderpromo.setEntpProcCost	(orderdt.getOrderPaPromo().getEntpProcCost());
					orderpromo.setEntpCode		(orderdt.getEntpCode());
					orderpromo.setCancelYn		("0");
					orderpromo.setCancelDate	(null);
					orderpromo.setCancelId		("");
					orderpromo.setRemark		("");
					orderpromo.setInsertId		(orderdt.getLastProcId());
					orderpromo.setInsertDate	(orderdt.getLastProcDate());
					orderpromoList.add(orderpromo);
					
					//For CouponIssue
					orderdt.getOrderPaPromo().setOrderNo    (orderdt.getOrderNo());
					orderdt.getOrderPaPromo().setCustNo     (orderdt.getCustNo());
					orderdt.getOrderPaPromo().setInsertDate (orderdt.getLastProcDate());
					orderdt.getOrderPaPromo().setInsertId	  (orderdt.getLastProcId());
					if("1".equals(orderdt.getOrderPaPromo().getCouponYn())){
						newCouponissue(orderdt.getOrderPaPromo(), couponissue);
					}
				}
				//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
				
				//즉시할인쿠폰
				double instantCoupon = orderdt.getInstantCouponDiscount();
				if(instantCoupon > 0) {
					//= 즉시지급쿠폰 할인 (do_type : 30) setting
					Orderpromo orderpromo = new Orderpromo();
					orderpromo.setSeq			(selectSequenceNo("ORDERPROMO_SEQ"));
					orderpromo.setPromoNo		("299912369999");
					orderpromo.setDoType		("30");
					orderpromo.setOrderNo		(orderdt.getOrderNo());
					orderpromo.setOrderGSeq		(orderdt.getOrderGSeq());
					orderpromo.setProcAmt		(instantCoupon);
					orderpromo.setCancelAmt		(0);
					orderpromo.setClaimAmt		(0);
					orderpromo.setProcCost		(ComUtil.modAmt( instantCoupon / orderdt.getOrderQty(), 4, 2));
					orderpromo.setOwnProcCost	(ComUtil.modAmt( instantCoupon / orderdt.getOrderQty(), 4, 2));
					orderpromo.setEntpProcCost	(0);
					orderpromo.setEntpCode		(orderdt.getEntpCode());
					orderpromo.setCancelYn		("0");
					orderpromo.setCancelDate	(null);
					orderpromo.setCancelId		("");
					orderpromo.setRemark		("");
					orderpromo.setInsertId		(orderdt.getLastProcId());
					orderpromo.setInsertDate	(orderdt.getLastProcDate());
					orderpromoList.add(orderpromo);
				}
				
				
				if( orderdt.getDcAmt() - (promotionDiscountPrice * orderdt.getOrderQty()) - (paPromotionDiscountPrice * orderdt.getOrderQty()) - arsDcAmt - LumpSumDcAmt - instantCoupon > 0) {//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
					//= 제휴할인금액(do_type : 92) setting
					Orderpromo orderpromo = new Orderpromo();
					orderpromo.setSeq			(selectSequenceNo("ORDERPROMO_SEQ"));	
					orderpromo.setPromoNo		(paorderDAO.selectPromoNo("92"));
					orderpromo.setDoType		("92");	
					orderpromo.setOrderNo		(orderdt.getOrderNo());
					orderpromo.setOrderGSeq		(orderdt.getOrderGSeq());
					orderpromo.setProcAmt		(orderdt.getDcAmt() - (promotionDiscountPrice * orderdt.getOrderQty()) - (paPromotionDiscountPrice * orderdt.getOrderQty()) - arsDcAmt - LumpSumDcAmt - instantCoupon);//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
					orderpromo.setCancelAmt		(0);
					orderpromo.setClaimAmt		(0);
					orderpromo.setProcCost		((orderdt.getDcAmt() - (promotionDiscountPrice * orderdt.getOrderQty()) - (paPromotionDiscountPrice * orderdt.getOrderQty()) - arsDcAmt - LumpSumDcAmt - instantCoupon) / orderdt.getOrderQty());//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
					orderpromo.setOwnProcCost	((orderdt.getDcAmt() - (promotionDiscountPrice * orderdt.getOrderQty()) - (paPromotionDiscountPrice * orderdt.getOrderQty()) - arsDcAmt - LumpSumDcAmt - instantCoupon) / orderdt.getOrderQty());//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
					orderpromo.setEntpProcCost	(0);
					orderpromo.setEntpCode		(orderdt.getEntpCode());
					orderpromo.setCancelYn		("0");
					orderpromo.setCancelDate	(null);
					orderpromo.setCancelId		("");
					orderpromo.setRemark		("");
					orderpromo.setInsertId		(orderdt.getLastProcId());
					orderpromo.setInsertDate	(orderdt.getLastProcDate());
					orderpromoList.add(orderpromo);
				}	
			}
		}
	}
	
	
	
	
	private void newCouponissue(OrderpromoVO orderpromo ,  List<Couponissue> couponissueList) throws Exception{
			
		Couponissue couponissue = new Couponissue();
    	couponissue.setSeq         	( selectSequenceNo("COUPONISSUE_SEQ") );
    	couponissue.setPromoNo    	( orderpromo.getPromoNo());
    	couponissue.setCustNo     	( orderpromo.getCustNo() );
    	couponissue.setGetOrderNo	( "" );
    	couponissue.setUseYn      	( "1");
    	couponissue.setUseOrderNo	( orderpromo.getOrderNo() );
    	couponissue.setCancelYn   	( "0" );
    	couponissue.setInsertDate 	( orderpromo.getInsertDate() );
    	couponissue.setInsertId   	( orderpromo.getInsertId() );
    	couponissue.setModifyDate 	( orderpromo.getInsertDate() );
    	couponissue.setModifyId   	( orderpromo.getInsertId() );
    	couponissue.setUseStartDate ( orderpromo.getCouponPromoBdate());
    	couponissue.setUseEndDate	( orderpromo.getCouponPromoEdate());
    	
    	couponissueList.add(couponissue);
	}
	
	
	
	private void newOrderGoods(List<OrderdtVO> orderdtList, List<Ordergoods> ordergoodsList){
		
		for (OrderdtVO orderdt : orderdtList ) {
			if(orderdt.getOrderDSeq().equals("001")){ //= 본품
				Ordergoods ordergoods = new Ordergoods();
				ordergoods.setOrderNo			(orderdt.getOrderNo());
				ordergoods.setOrderGSeq			(orderdt.getOrderGSeq());
				ordergoods.setCustNo			(orderdt.getCustNo());
				ordergoods.setOrderDate			(orderdt.getOrderDate());
				ordergoods.setOrderGb			("10");
				ordergoods.setPromoNo			("");
				ordergoods.setSetYn				("0");
				ordergoods.setGoodsCode			(orderdt.getGoodsCode());
				ordergoods.setOrderQty			(orderdt.getOrderQty());
				ordergoods.setCancelQty			(0);
				ordergoods.setClaimQty			(0);
				ordergoods.setReturnQty			(0);
				ordergoods.setClaimCanQty		(0);
				ordergoods.setExchCnt			(0);
				ordergoods.setAsCnt				(0);
				ordergoods.setSalePrice			(orderdt.getSalePrice());
				ordergoods.setDcRate			(orderdt.getDcRate());
				ordergoods.setDcAmtGoods		(orderdt.getDcAmtGoods());
				ordergoods.setDcAmtMemb			(orderdt.getDcAmtMemb());
				ordergoods.setDcAmtDiv			(orderdt.getDcAmtDiv());
				ordergoods.setDcAmtCard			(orderdt.getDcAmtCard());
				ordergoods.setDcAmt				(orderdt.getDcAmt());
				ordergoods.setNorestAllotMonths	(orderdt.getNorestAllotMonths());
				ordergoods.setCounseltel		("");				
				ordergoodsList.add(ordergoods);
			}
		}	
	}
	
	
	private Promocounsel newPromoCounsel(HashMap<String, Object> giftPromoMap, ParamMap param ) throws Exception{
		
		try{
			String goodsSelectNo = selectSequenceNo("GOODS_SELECT_NO");
			PromocounselVO promoCounsel = new PromocounselVO();
			Timestamp sysDateTime			= (Timestamp)param.get("sysDateTime");
			String	  userId				= param.getString("userId");
			
			promoCounsel.setPromoNo				(giftPromoMap.get("PROMO_NO").toString());
			promoCounsel.setOrderNo				(param.getString("orderNo"));
			promoCounsel.setOrderGSeq			(param.getString("orderGSeq"));
			promoCounsel.setGoodsSelectNo		(goodsSelectNo);
			promoCounsel.setLimitQty			(Long.parseLong(giftPromoMap.get("LIMIT_QTY").toString()));
			promoCounsel.setCounselQty			(Integer.parseInt(param.getString("orderQty")));
			promoCounsel.setInsertId			(userId);
			promoCounsel.setInsertDate			(sysDateTime);		
			long l_temp = promoCounsel.getCounselQty();
			long selfOrder = paorderDAO.retrieveSumCouncelQty(promoCounsel);
			
			if (promoCounsel.getLimitQty() < selfOrder + promoCounsel.getCounselQty()) {
				promoCounsel.setCounselQty(0);; //수량이 없는 경우
			}else{
				promoCounsel.setCounselQty(l_temp);
			}
			return promoCounsel;
			
		}catch(Exception e){
			throw processException("pa.fail_setting_promoCounsel" ,  new String[] { e.toString()});
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private OrderdtVO newOrderdtForGift(HashMap<String, Object> giftPromoMap, ParamMap paramMap, int i ,  List<OrderdtVO> orderdtList ) throws Exception{
		
		try{
			OrderInfoMsg[] giftInfo = null;
			ParamMap[] giftStockMap = null;
			ParamMap giftMap 		= null;
			OrderdtVO orderdt 		= null;
			
			int 	  orderDSeq		= Integer.parseInt(orderdtList.get(orderdtList.size()-1).getOrderDSeq());
			int		  orderQty 		= Integer.parseInt(paramMap.getString("orderQty"));
			
			int promoDtCnt			= 0;; 
			
			HashMap<String, Object> giftPromoDtMap = new HashMap<String, Object>();
			giftPromoDtMap.put("promoNo"	, giftPromoMap.get("PROMO_NO").toString());
			giftPromoDtMap.put("orderQty"	, paramMap.getString("orderQty"));	
			
			List<Object> giftPromoDtList = paorderDAO.selectGiftPromoDt(giftPromoDtMap);	
			
			Timestamp sysDateTime  = orderdtList.get(0).getLastProcDate();
			String	  userId	   = orderdtList.get(0).getLastProcId();
			
			promoDtCnt=  giftPromoDtList.size();
			if(promoDtCnt < 1) return null;
			
			for (int j = 0; promoDtCnt > j; j++ ){
				
				giftInfo = new OrderInfoMsg[1];
				giftStockMap = new ParamMap[1];
				
				//orderDSeq++; //= d_seq 증가.
				giftMap = new ParamMap();
				giftMap.setParamMap((HashMap<String, Object>) giftPromoDtList.get(j));
				giftMap.replaceCamel();
				giftMap.put("applyDate", paramMap.get("orderDate")); //= 사은품은 제휴사 주문발생 시간 기준으로 정보조회
				
				giftInfo[0] = paorderDAO.selectGoodsInfo(giftMap); //= 사은품 상세정보 조회
				
				giftStockMap[0] = new ParamMap();
				giftStockMap[0].setParamMap((HashMap<String, Object>)paramMap.get());
				giftStockMap[0].put("seq"			, orderDSeq + 1);
				giftStockMap[0].put("goodsCode"		, giftInfo[0].getGoodsCode());
				giftStockMap[0].put("goodsName"		, giftInfo[0].getGoodsName());
				giftStockMap[0].put("goodsdtCode"	, giftInfo[0].getGoodsDtCode());
				giftStockMap[0].put("delyType"	 	, giftInfo[0].getDelyType());
				giftStockMap[0].put("stockChkPlace"	, giftInfo[0].getStockChkPlace());
				giftStockMap[0].put("whCode"		, giftInfo[0].getWhCode());

				// 재고 check
				giftStockMap = calStock(giftStockMap);
				
				//= 사은품 재고부족일 경우 사은품 데이터를 생성하지 않음.
				if(giftStockMap == null || giftStockMap.length == 0){
					orderQty = 0;
				} 
				
				orderdt = new OrderdtVO();
				orderdt.setOrderNo							(paramMap.getString("orderNo"));
				orderdt.setOrderGSeq						(ComUtil.increaseLpad(Integer.toString(i) , 3, "0"));
				orderdt.setOrderDSeq						(ComUtil.increaseLpad(Integer.toString(orderDSeq) , 3, "0"));
				orderdt.setOrderWSeq						("001");
				orderdt.setCustNo							(paramMap.getString("custNo"));
				orderdt.setOrderDate						(sysDateTime);
				orderdt.setMdCode							(giftInfo[0].getMdCode());
				orderdt.setReceiverSeq						(paramMap.getString("receiverSeq"));
				orderdt.setOrderGb							("10");
				orderdt.setDoFlag							(paramMap.getString("doFlag"));
				orderdt.setGoodsGb							("30"); //= 사은품
				orderdt.setMediaGb							("03");
				orderdt.setMediaCode						(paramMap.getString("mediaCode"));
				orderdt.setGoodsCode						(giftInfo[0].getGoodsCode());
				orderdt.setGoodsdtCode						(giftInfo[0].getGoodsDtCode());
				orderdt.setGoodsdtInfo						(giftInfo[0].getGoodsDtInfo());
				orderdt.setSalePrice						(0);
				orderdt.setBuyPrice							(giftInfo[0].getBuyPrice());
				orderdt.setOrderQty							(orderQty);
				//orderdt.setOrderQty						(giftStockMap[0].getLong("orderQty"));
				orderdt.setOrderAmt							(0);
				orderdt.setDcAmtMemb						(0);
				orderdt.setDcAmtDiv							(0);
				orderdt.setDcAmtCard						(0);
				orderdt.setDcAmtGoods						(0);
				orderdt.setDcRateGoods						(0);
				orderdt.setDcRate							(0);
				orderdt.setDcAmt							(0);
				orderdt.setRsaleAmt							(0);
				orderdt.setSyscancel						(0);
				orderdt.setSyslast							(orderdt.getOrderQty());
				orderdt.setSyslastDcGoods					(0);
				orderdt.setSyslastDcMemb					(0);
				orderdt.setSyslastDcDiv						(0);
				orderdt.setSyslastDcCard					(0);
				orderdt.setSyslastDc						(0);
				orderdt.setSyslastAmt						(0);
				orderdt.setWhCode							(giftInfo[0].getWhCode());
				orderdt.setSaleYn							("0");
				orderdt.setDelyType							(giftInfo[0].getDelyType());
				orderdt.setDelyGb							("10");
				orderdt.setCustDelyFlag						("0");		
				orderdt.setFirstPlanDate					(DateUtil.toTimestamp(giftStockMap[0].getString("firstPlanDate"), "yyyy-MM-dd HH:mm:ss"));
				orderdt.setOutPlanDate						(DateUtil.toTimestamp(giftStockMap[0].getString("outPlanDate"), "yyyy-MM-dd HH:mm:ss"));
				orderdt.setStockFlag						(giftStockMap[0].getString("setStockFlag"));
				orderdt.setStockKey							(giftStockMap[0].getString("stockKey"));
				orderdt.setDelyHopeDate						(DateUtil.toTimestamp(giftStockMap[0].getString("delyHopeDate"), "yyyy-MM-dd HH:mm:ss"));
				orderdt.setPreoutGb							("00");
				orderdt.setGoodsSelectNo					(giftStockMap[0].getString("goodsSelectNo"));
				orderdt.setSingleDueGb						("00");		
				orderdt.setPackYn							("0");
				orderdt.setAnonyYn							("0");
				orderdt.setMsg								(paramMap.getString("msg"));
				orderdt.setMsgNote							("");
				orderdt.setHappyCardYn						("0");
				orderdt.setSaveamt							(0);
				orderdt.setSaveamtGb						("90");
				orderdt.setPromoNo1							("");
				orderdt.setPromoNo2							("");
				orderdt.setSetGoodsCode						("");
				orderdt.setReceiptYn						("0");
				orderdt.setSlipYn							("1");
				orderdt.setEntpslipNo						("");
				orderdt.setDirectYn							(giftInfo[0].getDirectShipYn());
				orderdt.setLastProcId						(userId);
				orderdt.setLastProcDate						(sysDateTime);
				orderdt.setReservDelyYn						("0");
				orderdt.setRsalePaAmt						(0);
				orderdt.setRemark3N							(0);
				orderdt.setEntpCode							("0");
				orderdt.setEntpCodeOrg						("0");
				orderdt.setPriceSeq							(giftInfo[0].getPriceSeq());

				if(orderdt.getOrderQty() > 0) orderdtList.add(orderdt);	
				
				orderDSeq++; //= d_seq 증가.
			}
			
			return orderdt;
			
		}catch(Exception e){
			throw processException("pa.fail_setting_orderDtInfo", new String[] {"'사은품' " + e.toString() } );
		}
		
	}
	
	private void newGiftPromo(ParamMap[] paramMap, int i, List<Promocounsel> promocounselList, List<OrderdtVO> orderdtList , List<Orderpromo> orderpromoList) throws Exception{
		//HashMap<String, Object> giftPromoMap   =  new HashMap<String, Object>();
		Promocounsel promoCounsel = null;
		OrderdtVO orderdt = null;
		
		List<HashMap<String, Object>> giftPromoList = paorderDAO.selectGiftPromo(paramMap[i]);
		if(giftPromoList.size() < 1) return; 	
		
		for(HashMap<String, Object> giftPromoMap : giftPromoList) {
			//1)TPROMOCOUNSEL Setting
			promoCounsel = new Promocounsel();
			promoCounsel = newPromoCounsel(giftPromoMap, paramMap[i]);  //이 함수에서 PromoCounselSetting
			if(promoCounsel.getCounselQty() > 0) {
				promocounselList.add(promoCounsel);
			}
			
			//2) TORDERDT SETTING
			orderdt = new OrderdtVO();
			orderdt = newOrderdtForGift(giftPromoMap, paramMap[i], i , orderdtList);
			
			//3) TORDERPROMO SETTING
			newOrderPromo(orderdt, paramMap[i].getString("entpCode"), giftPromoMap.get("PROMO_NO").toString(), orderpromoList);
		}
	}

	private void newOrderPromo(OrderdtVO orderdt, String entpCode, String promoNo , List<Orderpromo> orderpromoList) throws Exception{
		if(orderdt == null) return;
		if(orderdt.getOrderQty() < 1) return; //수량이 없는것들은 orderPromo 생성하지 않음.

		Orderpromo orderpromo = new Orderpromo();
		orderpromo.setSeq						(selectSequenceNo("ORDERPROMO_SEQ"));
		orderpromo.setPromoNo					(promoNo);
		orderpromo.setDoType					("10");
		orderpromo.setOrderNo					(orderdt.getOrderNo());
		orderpromo.setOrderGSeq					(orderdt.getOrderGSeq());
		orderpromo.setProcAmt					(0);
		orderpromo.setCancelAmt					(0);
		orderpromo.setClaimAmt					(0);
		orderpromo.setProcCost					(0);
		orderpromo.setOwnProcCost				(0);
		orderpromo.setEntpProcCost				(0);
		orderpromo.setEntpCode					(entpCode);
		orderpromo.setCancelYn					("0");
		orderpromo.setCancelDate				(null);
		orderpromo.setCancelId					("");
		orderpromo.setRemark					("");
		orderpromo.setInsertId					(orderdt.getLastProcId());
		orderpromo.setInsertDate				(orderdt.getLastProcDate());
		
		orderpromoList.add(orderpromo);
	}
	
	private void newOrderDt(ParamMap[] paramMap, int i, List<OrderdtVO> orderdtList) throws Exception{

		try{
			Timestamp sysDateTime 	= (Timestamp)paramMap[0].get("sysDateTime");		
			String	  userId		= paramMap[0].getString("userId");
			
			OrderdtVO orderdt = new OrderdtVO();
			orderdt.setOrderNo						(paramMap[i].getString("orderNo"));
			orderdt.setOrderGSeq					(ComUtil.increaseLpad(Integer.toString(i) , 3, "0"));
			orderdt.setOrderDSeq					(ComUtil.increaseLpad(Integer.toString(0) , 3, "0"));
			orderdt.setOrderWSeq					("001");
			orderdt.setCustNo						(paramMap[i].getString("custNo"));
			orderdt.setOrderDate					(sysDateTime);
			orderdt.setMdCode						(paramMap[i].getString("mdCode"));
			orderdt.setReceiverSeq					(paramMap[i].getString("receiverSeq"));
			orderdt.setOrderGb						("10");
			orderdt.setDoFlag						(paramMap[i].getString("doFlag"));  //10 : 무통장  20 : 결제완료 주문
			orderdt.setGoodsGb						("10");
			orderdt.setMediaGb						("03");
			orderdt.setMediaCode					(paramMap[i].getString("mediaCode"));
			orderdt.setGoodsCode					(paramMap[i].getString("goodsCode"));
			orderdt.setGoodsdtCode					(paramMap[i].getString("goodsdtCode"));
			orderdt.setGoodsdtInfo					(paramMap[i].getString("goodsdtInfo"));
			orderdt.setSalePrice					(paramMap[i].getDouble("salePrice"));
			orderdt.setBuyPrice						(paramMap[i].getDouble("buyPrice"));
			orderdt.setOrderQty						(paramMap[i].getLong("orderQty"));
			orderdt.setOrderAmt						(orderdt.getSalePrice() * orderdt.getOrderQty());
			orderdt.setPriceSeq						(paramMap[i].getString("priceSeq"));
			orderdt.setDcAmtMemb					(0);
			orderdt.setDcAmtDiv						(0);
			orderdt.setDcAmtCard					(0);
			orderdt.setDcAmtGoods					(orderdt.getOrderAmt() - paramMap[i].getDouble("rsaleAmt"));				
			orderdt.setDcRateGoods					(ComUtil.modAmt(orderdt.getDcAmtGoods()/orderdt.getOrderAmt() * 10000) / 100.0 );
			orderdt.setDcRate						(orderdt.getDcRateGoods());
			orderdt.setDcAmt						(orderdt.getDcAmtGoods());
			orderdt.setRsaleAmt						(paramMap[i].getLong("rsaleAmt"));
			orderdt.setSyscancel					(0);
			orderdt.setSyslast						(orderdt.getOrderQty());
			orderdt.setSyslastDcGoods				(orderdt.getDcAmtGoods());
			orderdt.setSyslastDcMemb				(orderdt.getDcAmtMemb());
			orderdt.setSyslastDcDiv					(orderdt.getDcAmtDiv());
			orderdt.setSyslastDcCard				(orderdt.getDcAmtCard());
			orderdt.setSyslastDc					(orderdt.getDcAmt());
			orderdt.setSyslastAmt					(orderdt.getRsaleAmt());
			orderdt.setWhCode						(paramMap[i].getString("whCode"));
			orderdt.setSaleYn						("1");
			orderdt.setDelyType						(paramMap[i].getString("delyType"));
			orderdt.setDelyGb						("10");
			orderdt.setCustDelyFlag					("0");
			orderdt.setFirstPlanDate				(DateUtil.toTimestamp(paramMap[i].getString("firstPlanDate"), "yyyy-MM-dd HH:mm:ss"));
			orderdt.setOutPlanDate					(DateUtil.toTimestamp(paramMap[i].getString("outPlanDate"), "yyyy-MM-dd HH:mm:ss"));
			orderdt.setStockFlag					(paramMap[i].getString("setStockFlag"));
			orderdt.setStockKey						(paramMap[i].getString("stockKey"));
			orderdt.setDelyHopeDate					(DateUtil.toTimestamp(paramMap[i].getString("delyHopeDate"), "yyyy-MM-dd HH:mm:ss"));	
			orderdt.setPreoutGb						("00");			
			orderdt.setGoodsSelectNo				(paramMap[i].getString("goodsSelectNo"));
			orderdt.setSingleDueGb					("00");		
			orderdt.setPackYn						("0");
			orderdt.setAnonyYn						("0");
			orderdt.setMsg							(paramMap[i].getString("msg"));
			orderdt.setMsgNote						("");
			orderdt.setHappyCardYn					("0");
			orderdt.setSaveamt						(0);
			orderdt.setSaveamtGb					("90");
			orderdt.setPromoNo1						("");
			orderdt.setPromoNo2						("");
			orderdt.setSetGoodsCode					("");
			orderdt.setReceiptYn					("0");
			orderdt.setSlipYn						("1");
			orderdt.setEntpslipNo					("");
			orderdt.setDirectYn						(paramMap[i].getString("directShipYn"));
			orderdt.setLastProcId					(userId);
			orderdt.setLastProcDate					(sysDateTime);
			orderdt.setReservDelyYn					("0");
			orderdt.setEntpCode						(paramMap[i].getString("entpCode"));
			orderdt.setEntpCodeOrg				    (paramMap[i].getString("entpCodeOrg"));
			orderdt.setNorestAllotMonths			(paramMap[i].getString("norestAllotMonths"));
			orderdt.setRsalePaAmt					(paramMap[i].getDouble("rsaleAmt")); 
			orderdt.setRemark3N						(paramMap[i].getLong("sellerDcAmt"));
			orderdt.setRemark4N						((long)(paramMap[i].getDouble("supplyPrice")) * orderdt.getOrderQty());
			orderdt.setOwnProcCost					(paramMap[i].getDouble("arsOwnDcAmt"));
			orderdt.setEntpProcCost					(paramMap[i].getDouble("arsEntpDcAmt"));
			orderdt.setPaShipCostAmt				(paramMap[i].getDouble("paShipCostAmt"));
			
			orderdt.setArsDcAmt						(paramMap[i].getLong("sellerDcAmt"));
			orderdt.setLumpSumDcAmt					(paramMap[i].getLong("lumpSumDcAmt"));
			orderdt.setLumpSumOwnDcAmt				(paramMap[i].getDouble("lumpSumOwnDcAmt"));
			orderdt.setLumpSumEntpDcAmt				(paramMap[i].getDouble("lumpSumEntpDcAmt"));
			
			orderdt.setOrderPromo					((OrderpromoVO)paramMap[i].get("orderPromo"));  //TODO null check
			orderdt.setOrderPaPromo					((OrderpromoVO)paramMap[i].get("orderPaPromo"));//제휴OUT프로모션 : 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
			
			paramMap[i].put("orderGSeq"	, orderdt.getOrderGSeq());
			paramMap[i].put("orderDSeq"	, orderdt.getOrderDSeq());
			paramMap[i].put("orderWSeq"	, orderdt.getOrderWSeq());
			paramMap[i].put("orderQty"	, orderdt.getOrderQty());
			
			//즉시할인쿠폰 추가
			orderdt.setInstantCouponDiscount(paramMap[i].getLong("instantCouponDiscount"));
			
			orderdt.setPaOrderNo	(paramMap[i].getString("paOrderNo"));
			orderdt.setPaCode		(paramMap[i].getString("paCode"));
			
			orderdtList.add(orderdt);
			
		}catch(Exception e){
			throw processException("pa.fail_setting_orderDtInfo", new String[] { e.toString() } );
		}
	}
	
	private void setPhone(ParamMap param){  //공통 함수로 빼는것이 필요..
		String tel = param.getString("tel").replace("-", "");
	    String hp  = param.getString("hp").replace("-", "");
	    switch(tel.length()){
	    	case 7:
	    		param.put("ddd"  , "");
	    		param.put("tel1" , tel.substring(0, 3));
	    		param.put("tel2" , tel.substring(3, 7));
	    		break;
	    	case 8:
	    		param.put("ddd"  , "");
	    		param.put("tel1" , tel.substring(0, 4));
	    		param.put("tel2" , tel.substring(4, 8));
	    		break;
	    	case 9:
	    		param.put("ddd"  , tel.substring(0, 2));
	    		param.put("tel1" , tel.substring(2, 5));
	    		param.put("tel2" , tel.substring(5, 9));
	    		break;	
	    	case 10:
	    		if(tel.substring(0, 2).equals("02")){
	    			param.put("ddd"  , tel.substring(0, 2));
		    		param.put("tel1" , tel.substring(2, 6));
		    		param.put("tel2" , tel.substring(6, 10));
		    		break;
	    		}else{
	    			param.put("ddd"  , tel.substring(0, 3));
		    		param.put("tel1" , tel.substring(3, 6));
		    		param.put("tel2" , tel.substring(6, 10));
		    		break;
	    		}
	    	case 11:
	    		param.put("ddd"  , tel.substring(0, 3));
	    		param.put("tel1" , tel.substring(3, 7));
	    		param.put("tel2" , tel.substring(7, 11));
	    		break;	
	    	case 12:
	    		param.put("ddd"  , tel.substring(0, 4));
	    		param.put("tel1" , tel.substring(4, 8));
	    		param.put("tel2" , tel.substring(8, 12));
	    		break;
	    	default : 
	    		param.put("ddd"	 , "");
	    		param.put("tel1" , "");
	    		param.put("tel2" , "");
	    }

	    switch(hp.length()){
	    	case 0:
	    		param.put("dddH"  , "");
	    		param.put("tel1H" , "");
	    		param.put("tel2H" , "");
	    		break;
	    	case 10:
		    	param.put("dddH"  , hp.substring(0, 3));
	    		param.put("tel1H" , hp.substring(3, 6));
	    		param.put("tel2H" , hp.substring(6, 10));
	    		break;
	    	case 11:
	    		param.put("dddH"  , hp.substring(0, 3));
	    		param.put("tel1H" , hp.substring(3, 7));
	    		param.put("tel2H" , hp.substring(7, 11));
	    		break;	
	    	case 12:
	    		param.put("dddH"  , hp.substring(0, 4));
	    		param.put("tel1H" , hp.substring(4, 8));
	    		param.put("tel2H" , hp.substring(8, 12));
	    		break;
	    	default :
	    		param.put("dddH"  , hp.substring(0, 4));
	    		param.put("tel1H" , hp.substring(4, 8));
	    		param.put("tel2H" , hp.substring(8));
	    }
	}
	
	private void setAddr(ParamMap  input , ParamMap output) throws Exception{
		
		boolean checkRefineSuccess = true;
		String	caseNotice 		   = "";
		
		//= 정제 성공 , 실패 유무 체크
		if(input.getString("CL_SUCCESS_YN").equals("0")){ 
			checkRefineSuccess = false;
			caseNotice		   = "주소정제에 실패했습니다. 수지원넷 접속 실패";
		}else{
			if( !input.getString("FLG").equals("1") || input.getString("STD_POST_NO").equals("") ){
				checkRefineSuccess = false;
				caseNotice 		   = "주소정제에 실패했습니다. 수지넷 접속 성공 주소정제실패 ";
			}else{
				if( (!input.getString("ADDR_GBN").equals("1")) && (!input.getString("ADDR_GBN").equals("2")) ){
					checkRefineSuccess = false;
					caseNotice 		   = "주소정제에 실패했습니다. 단순 정제 실패 ";
				}else{
					checkRefineSuccess = true;
				}
			}
		}
		
		//= 정제 데이터 생성
		if(checkRefineSuccess){ // 수지원넷 정제 성공 케이스
			switch(input.getString("ADDR_GBN")){
				
				case "1":
					output.put("RCV_POST"			,  input.getString("STD_POST_NO"));       //우편번호
					output.put("RCV_POST_SEQ"		,  input.getString("STD_POST_SEQ"));	  //우편번호seq
					output.put("RCV_POST_ADDR"		,  input.getString("search_addr"));					
					output.put("RCV_ADDR"			,  input.getString("search_addr2"));
					 
					output.put("ROAD_ADDR_YN"		,   "0");
					output.put("ROAD_ADDR_NO"	 	,   "" );
					output.put("ROAD_POST_ADDR"	 	,   "" );
					output.put("ROAD_ADDR"		 	,   "" );
					
					//도로명
					output.put("STD_ROAD_POST_NO"	,	input.getString("STD_ROAD_POST_NO"));
					output.put("STD_ROAD_POST_SEQ" 	,	input.getString("STD_ROAD_POST_SEQ"));
					output.put("STD_ROAD_POST_ADDR1", 	input.getString("STD_ROAD_POST_ADDR1"));
					output.put("STD_ROAD_POST_ADDR2",	input.getString("STD_ROAD_POST_ADDR2"));
					output.put("STD_ROAD_POST_LATY" , 	input.getString("STD_ROAD_POST_LATY"));
					output.put("STD_ROAD_POST_LNGX" , 	input.getString("STD_ROAD_POST_LNGX"));
				
					//지번
					output.put("STD_POST_NO"		, 	input.getString("STD_POST_NO"));
					output.put("STD_POST_SEQ"		, 	input.getString("STD_POST_SEQ"));
					output.put("STD_POST_ADDR1" 	, 	input.getString("STD_POST_ADDR1"));
					output.put("STD_POST_ADDR2" 	, 	input.getString("STD_POST_ADDR2"));
					output.put("STD_POST_LNGX" 		, 	input.getString("STD_POST_LNGX"));
					output.put("STD_POST_LATY" 		, 	input.getString("STD_POST_LATY"));

					output.put("FULL_ADDR"			,	input.getString("STD_POST_ADDR1") + input.getString("STD_POST_ADDR2"));	
					output.put("SELECT_ADDR"		,	"3");
					output.put("REFINE_RESULT_CODE"	,	input.getString("REFINE_RESULT_CODE"));
					break;
					
				case "2": //도로명
					output.put("RCV_POST"			,  input.getString("STD_ROAD_POST_NO"));       //우편번호
					output.put("RCV_POST_SEQ"		,  input.getString("STD_ROAD_POST_SEQ"));	  //우편번호seq
					output.put("RCV_ADDR"			,  "");
					output.put("RCV_POST_ADDR"		,  "");			
				
					output.put("ROAD_ADDR_YN"		,   "1");
					output.put("ROAD_ADDR_NO"	 	,   input.getString("ROAD_ADDR_NO") );
					output.put("ROAD_POST_ADDR"	 	,   input.getString("search_addr") );
					output.put("ROAD_ADDR"		 	,   input.getString("search_addr2") );//CUST_ADDRDT
					
					//도로명
					output.put("STD_ROAD_POST_NO"	,	input.getString("STD_ROAD_POST_NO"));
					output.put("STD_ROAD_POST_SEQ" 	,	input.getString("STD_ROAD_POST_SEQ"));
					output.put("STD_ROAD_POST_ADDR1", 	input.getString("STD_ROAD_POST_ADDR1"));
					output.put("STD_ROAD_POST_ADDR2",	input.getString("STD_ROAD_POST_ADDR2"));
					output.put("STD_ROAD_POST_LATY" , 	input.getString("STD_ROAD_POST_LATY"));
					output.put("STD_ROAD_POST_LNGX" , 	input.getString("STD_ROAD_POST_LNGX"));
				
					//지번
					output.put("STD_POST_NO"		, 	input.getString("STD_POST_NO"));
					output.put("STD_POST_SEQ"		, 	input.getString("STD_POST_SEQ"));
					output.put("STD_POST_ADDR1" 	, 	input.getString("STD_POST_ADDR1"));
					output.put("STD_POST_ADDR2" 	, 	input.getString("STD_POST_ADDR2"));
					output.put("STD_POST_LNGX" 		, 	input.getString("STD_POST_LNGX"));
					output.put("STD_POST_LATY" 		, 	input.getString("STD_POST_LATY"));
					
					output.put("FULL_ADDR"			,	input.getString("STD_ROAD_POST_ADDR1") + input.getString("STD_ROAD_POST_ADDR2"));	
					output.put("SELECT_ADDR"		,	"2");
					output.put("REFINE_RESULT_CODE"	,	input.getString("REFINE_RESULT_CODE"));
					break;
					
				default :
					throw processException("pa.address_fails ", new String[] { "RECEIVER객체 생성 준비 중 에러" } );

			}
			
		}else{ //실패 케이스
			output.put("RCV_POST"			,  "999999");      
			output.put("RCV_POST_SEQ"		,  "001");	  //우편번호seq
			output.put("RCV_ADDR"			,  caseNotice);
			output.put("RCV_POST_ADDR"		,  "실패주소 : " + input.getString("search_addr") +" " + input.getString("search_addr2"));			
		
			output.put("ROAD_ADDR_YN"		,   "0");
			output.put("ROAD_ADDR_NO"	 	,   "" );
			output.put("ROAD_POST_ADDR"	 	,   "" );
			output.put("ROAD_ADDR"		 	,   "" );
			
			//도로명
			output.put("STD_ROAD_POST_NO"	,	"" );
			output.put("STD_ROAD_POST_SEQ" 	,	"" );
			output.put("STD_ROAD_POST_ADDR1", 	"" );
			output.put("STD_ROAD_POST_ADDR2",	"" );
			output.put("STD_ROAD_POST_LATY" , 	"" );
			output.put("STD_ROAD_POST_LNGX" , 	"" );
		
			//지번
			output.put("STD_POST_NO"		, 	"" );
			output.put("STD_POST_SEQ"		, 	"" );
			output.put("STD_POST_ADDR1" 	, 	"" );
			output.put("STD_POST_ADDR2" 	, 	"" );
			output.put("STD_POST_LNGX" 		, 	"" );
			output.put("STD_POST_LATY" 		, 	"" );
			
			output.put("FULL_ADDR"			,	"" );	
			output.put("SELECT_ADDR"		,	"1");
			output.put("REFINE_RESULT_CODE"	,	"" );
		}
	
	}
	
		
	private void newReceiver(ParamMap[] paramMap, ParamMap  addressParams, int i, List<Receiver> receiverList) throws Exception{	
		
		if(calReceiverSeq(paramMap, i)){  //calReceiverSeq에서 receiverSeq 계산..
			setReceiver(paramMap[i], addressParams, receiverList);
		}
	}
	
	private void setReceiver(ParamMap paramMap, ParamMap  addressParams, List<Receiver> receiverList) throws Exception{
		String user_id    		= paramMap.getString("userId");
		Timestamp sysdateTime	= (Timestamp)paramMap.get("sysDateTime");	
		
		ParamMap teleParam = new ParamMap();
		teleParam.put("tel"		, paramMap.getString("receiverTel").replace("-", ""));
		teleParam.put("hp"		, paramMap.getString("receiverHp").replace("-", ""));
		setPhone(teleParam);  
	    
		ParamMap addrParam = new ParamMap();
		setAddr(addressParams	, addrParam);
	    
		Receiver receiver = new Receiver();
		receiver.setCustNo					(paramMap.getString("custNo"));
		receiver.setReceiverSeq				(paramMap.getString("receiverSeq"));
		receiver.setUseYn 					("1");
		receiver.setDefaultYn 				("1");
		receiver.setDeliveryDefaultYn		("1");
		receiver.setReceiverGb				("00");  //SaveCustomer에서 다중배송지 처리에 대한 내용이 있긴함..  setReceiverGb : 10, setDefaultYn: 0 등등

        String receiverName = paramMap.getString("receiverName");
        receiverName = ComUtil.removeEmoji(receiverName);
		receiver.setReceiver 				(receiverName);
		receiver.setReceiver1				(receiverName);
		
		receiver.setReceiver2				("");
		receiver.setReceiver3				("");
		
		receiver.setTel						(teleParam.getString("tel"));
		receiver.setReceiverDdd				(teleParam.getString("ddd"));
		receiver.setReceiverTel1			(teleParam.getString("tel1"));
		receiver.setReceiverTel2			(teleParam.getString("tel2"));
		receiver.setReceiverHp				(teleParam.getString("hp"));
		receiver.setReceiverHp1				(teleParam.getString("dddH"));
		receiver.setReceiverHp2	 			(teleParam.getString("tel1H"));
		receiver.setReceiverHp3				(teleParam.getString("tel2H"));
		
		receiver.setReceiverPost			(addrParam.getString("RCV_POST"));
		receiver.setReceiverPostSeq			(addrParam.getString("RCV_POST_SEQ"));
		receiver.setReceiverAddr			(addrParam.getString("RCV_ADDR"));
		receiver.setReceiverPostAddr		(addrParam.getString("RCV_POST_ADDR"));
		receiver.setRoadAddrYn				(addrParam.getString("ROAD_ADDR_YN"));
		receiver.setRoadAddrNo				(addrParam.getString("ROAD_ADDR_NO"));
		receiver.setRoadPostAddr			(addrParam.getString("ROAD_POST_ADDR"));
		receiver.setRoadAddr				(addrParam.getString("ROAD_ADDR"));
		receiver.setReceiverRoadPost		(addrParam.getString("STD_ROAD_POST_NO"));
		receiver.setReceiverRoadPostSeq		(addrParam.getString("STD_ROAD_POST_SEQ"));
		receiver.setReceiverRoadPostAddr	(addrParam.getString("STD_ROAD_POST_ADDR1"));
		receiver.setReceiverRoadPostAddr2	(addrParam.getString("STD_ROAD_POST_ADDR2"));
		receiver.setStdRoadPostLaty			(addrParam.getString("STD_ROAD_POST_LATY"));
		receiver.setStdRoadPostLngx			(addrParam.getString("STD_ROAD_POST_LNGX"));
		receiver.setReceiverStdPost			(addrParam.getString("STD_POST_NO"));
		receiver.setReceiverStdPostSeq		(addrParam.getString("STD_POST_SEQ"));
		receiver.setReceiverStdPostAddr		(addrParam.getString("STD_POST_ADDR1"));
		receiver.setReceiverStdPostAddr2	(addrParam.getString("STD_POST_ADDR2"));
		receiver.setStdPostLngx				(addrParam.getString("STD_POST_LNGX"));
		receiver.setStdPostLaty				(addrParam.getString("STD_POST_LATY"));
		receiver.setFullAddr				(addrParam.getString("FULL_ADDR"));
		receiver.setSelectAddr				(addrParam.getString("SELECT_ADDR"));
		receiver.setRefineResultCode		(addrParam.getString("REFINE_RESULT_CODE"));	
		
		receiver.setLastUseDate(null);
		receiver.setBeforeSeq("");
		receiver.setValidTelYn("1");
		receiver.setReceiverTel("");
		receiver.setNickName("");
		receiver.setInsertId(user_id);
		receiver.setInsertDate(sysdateTime);
	    receiver.setModifyId(user_id);
	    receiver.setModifyDate(sysdateTime);
	    receiver.setSmartOrderYn("0");
	    receiverList.add(receiver);
	}
	
	private boolean calReceiverSeq(ParamMap[] paramMap, int i) throws Exception{
		//2019.02.13   H.S BEAK : 다중 배송지 처리
		//			   1) 현재 제휴사는 다중배송지 처리를 하지 않고 있음, 하는 순간 각 모든 배송비 계산 부분에 대한 재 검증 필요
		//			   2) 리펙토링을 진행했기 때문에 해당 부분에 대한 try~catch 문은 당분간 유지
		try{
			String  curAddr 		 = null;
			String  preAddr 		 = null;
			Boolean createReceiverYn = true;
			Boolean fristRowYn		 = true;
			String  custNo		     = paramMap[0].getString("custNo");
			int	    cnt				 = 0;
			long 	receiverSeq  	 = 0;
			
			if( i > 0 ) {
				fristRowYn = false;
				cnt++;
			}
			
			if((paramMap[i].getString("receiverAddr")) == null || ("".equals(paramMap[i].getString("receiverAddr")))) return false;
			
			if(!fristRowYn){
				for(int j = 0; j < i; j++){ //같은것이 있는지 없는지 검사.
					if(paramMap[i].getString("receiverAddr") != null) 	curAddr = paramMap[i].getString("receiverAddr");
					if(paramMap[j].getString("receiverAddr") != null) 	preAddr = paramMap[j].getString("receiverAddr");				
					if((curAddr).equals(preAddr) && paramMap[j].getString("orderPossYn").equals("1")){  //paramMap[j].getString("orderPossYn2").equals("1")
						createReceiverYn = false;
						paramMap[i].put("receiverSeq", paramMap[j].getString("receiverSeq"));
						break;
					}
				}//end of for
			}

			if(createReceiverYn){ // 같은게 없는 경우
				receiverSeq = Long.parseLong(systemService.getMaxNo("TRECEIVER", "RECEIVER_SEQ", " CUST_NO = '"+ custNo +"'", 10)) + cnt;
				paramMap[i].put("receiverSeq" ,  ComUtil.lpad(String.valueOf(receiverSeq), 10, "0"));
			}
			
			return createReceiverYn;
			
		}catch(Exception e){
			throw processException("pa.calc_multi_receiver" ,  new String[] { e.toString()});
		}
	}
	
	private Orderm settingOrderM(ParamMap[] paramMap) throws Exception{
		Orderm orderm = new Orderm();
		String custName 		= paramMap[0].getString("custName");
		String user_id  	    = paramMap[0].getString("userId");
		String custNo	  		= paramMap[0].getString("custNo");
		String orderNo	  		= paramMap[0].getString("orderNo");
		Timestamp sysdate 		= (Timestamp)paramMap[0].get("sysDateTime");	

        orderm.setBlank			();
		orderm.setOrderNo		(orderNo);
		orderm.setCustNo		(custNo);
		orderm.setOrderDate		(sysdate);
		orderm.setOrderMedia	("62");
		orderm.setInsertId		(user_id);
		orderm.setPassword		("");
		orderm.setWithCode		("");
		orderm.setIpAddr		("");
		orderm.setMembGb		("90");
		orderm.setEmployeeId	("");
		orderm.setSenderName	(custName);
		orderm.setChannel		("");
		orderm.setChannel_id	("");
		return orderm;
	}
	
	private Custsystem settingCustsystem(ParamMap[] paramMap) throws Exception{
		
		Custsystem custsystem = new Custsystem();
		String mediaCode  = paramMap[0].getString("mediaCode");
		String custNo	  = paramMap[0].getString("custNo");
		String custChar	  = paramMap[0].getString("custChar");
		String user_id    = paramMap[0].getString("userId");
		Timestamp sysdate = (Timestamp)paramMap[0].get("sysDateTime");	
		
        custsystem.setCustNo			(custNo);
        custsystem.setCustGb			("00");		
        custsystem.setMembGb			("90");
        custsystem.setCustWarning		("1001");
        custsystem.setCustChar			(custChar);
        custsystem.setUseDeposit		(0);
        custsystem.setUsePbDeposit		(0);
        custsystem.setTotSaveamt		(0);
        custsystem.setDmYn				("1");
        custsystem.setDmNoGb			("00");
        custsystem.setDmNoId			("");
        custsystem.setDmNoDate			(null);
        custsystem.setFirstOrderDate	(null);
        custsystem.setLastOrderDate		(null);
        custsystem.setFirstMsaleGb		("");
        custsystem.setTotOrderCnt		(0);
        custsystem.setTotOrderAmt		(0);
        custsystem.setTotCancelCnt		(0);
        custsystem.setTotCancelAmt		(0);
        custsystem.setTotReturnCnt		(0);
        custsystem.setTotReturnAmt		(0);
        custsystem.setMembTotAmt		(0);        
        custsystem.setBirthdayMmdd		("0101");
        custsystem.setModifyId			(user_id);
        custsystem.setModifyDate		(sysdate);
        custsystem.setLastOrderDate		(sysdate);
        custsystem.setLastConMediaCode	(mediaCode);
        custsystem.setLastOrderMedia	("62");
        custsystem.setLastOrderMediaCode(mediaCode);
        custsystem.setInsertMedia		("62");
        custsystem.setInsertMediaCode	(mediaCode);
        custsystem.setInsertDate		(sysdate);
        
        return custsystem;
	}
	
	private Customer settingCustmer(ParamMap[] paramMap) throws Exception{

		Customer customer 		= new Customer();
		String membNo	    	= paramMap[0].getString("membNo");
		String custNo	 		= paramMap[0].getString("custNo");
		String custName 		= paramMap[0].getString("custName");
		String rvmthd			= paramMap[0].getString("receiveMethod");
		String user_id    		= paramMap[0].getString("userId");
		Timestamp sysdate 		= (Timestamp)paramMap[0].get("sysDateTime");		
		
		customer.setCustNo  			(custNo);
		customer.setMembNo				(membNo);
		customer.setReceiveMethod		(rvmthd);
		customer.setCustName			(custName);
		customer.setCustName1			(custName);
		customer.setCustName2			("");
		customer.setCustName3			("");
		customer.setEname				("");
		customer.setSex					("9");
		customer.setBirthdayYn			("1");
		customer.setBirthday			("");
		customer.setWeddingYn			("0");
		customer.setWeddingDate			(null);
		customer.setEmYn				("0");
		customer.setEmNo				("");
		customer.setMemId				("");
		customer.setIdInsertDate		(null);
		customer.setPasswd				("");
		customer.setPasswdHint			("");
		customer.setPasswdAnswer		("");
		customer.setResidentNo			("");		
		customer.setJobCode				("99");
		customer.setCompName			("");
		customer.setCompDeptname		("");		
		customer.setCountry				("0082");
		customer.setEmailAddr			("");
		customer.setEmailYn				("0");
		customer.setSmsYn				("0");
		customer.setOrderEmailYn		("0");
		customer.setOrderSmsYn			("0");
		customer.setNominateYn			("0");
		customer.setNominateId			("");
		customer.setCustSource			("999999");
		customer.setWithdrawalYn		("0");
	    customer.setWithdrawalCode		("");
	    customer.setWithdrawalContent	("");
	    customer.setWithdrawalDate		(null);
	    customer.setEmailFlag			("01");
	    customer.setEmailBlockCode		("0");
	    customer.setEmailBlockDate		(null);
	    customer.setNonAgeYn			("0");
	    customer.setParentName			("");
	    customer.setParentHp			("");
	    customer.setParentDi			("");
	    customer.setRemark1V			("0");
	    customer.setRemark2V			("");
	    customer.setRemark3V			("");	   
	    customer.setInsertId			(user_id);
	    customer.setInsertDate			(sysdate);
	    customer.setModifyId			(user_id);
	    customer.setModifyDate			(sysdate);
		
		return customer;		
	}
	
	//= 재고체크 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean checkStock(ParamMap[] paramMap , HashMap[] resultMap ) throws Exception{
		ParamMap orgParam[]  = paramMap;
		try {
			paramMap = calStock(paramMap);
		} catch (Exception e) {
			for(int i = 0;  orgParam.length > i; i++){
				resultMap[i] = new HashMap<>();
				resultMap[i].put("MAPPING_SEQ"		, 	orgParam[i].getString("mappingSeq"));
				resultMap[i].put("RESULT_CODE"		, 	"100001"); 
				resultMap[i].put("RESULT_MESSAGE"	, 	getMessage("pa.fail_order_input", new String[]{getMessage("pa.out_of_stock")}));
				resultMap[i].put("SITE_GB"			,	orgParam[i].getString("userId"));
			}	
			return false;
		}

		if(paramMap != null) return true;
		
		for(int i = 0;  orgParam.length > i; i++){
			resultMap[i] = new HashMap<>();
			resultMap[i].put("MAPPING_SEQ"		, 	orgParam[i].getString("mappingSeq"));
			resultMap[i].put("RESULT_CODE"		, 	"100001"); 
			resultMap[i].put("RESULT_MESSAGE"	, 	getMessage("pa.fail_order_input", new String[]{getMessage("pa.out_of_stock")}));
			resultMap[i].put("SITE_GB"			,	orgParam[i].getString("userId"));
		}		
		return false;
	}
	
	//= 주문정보 + 상품코드 및 상품정보 세팅
	private void settingOrderInfo(OrderInputVO[] orderInputVO, ParamMap addressParam, Timestamp sysDateTime, ParamMap[] paramMap) throws Exception{
		
		int paramLength					= orderInputVO.length;
		OrderInfoMsg[] goodsInfo     	= new OrderInfoMsg[paramLength];
		String    order_no    	 		= selectSequenceNo("ORDER_NO");	   						
		String    cust_no     	 		= selectSequenceNo("CUST_NO");
		String	  memb_no				= selectSequenceNo("MEMB_NO");
		try{
			for (int i = 0; i < paramLength; i++) {
				paramMap[i] = new ParamMap();
				if(addressParam != null){
					paramMap[i].put("roadAddrNo"    ,   addressParam.getString("ROAD_ADDR_NO"));
					paramMap[i].put("stdPostNo"	    ,   addressParam.getString("STD_POST_NO"));
				}
				paramMap[i].put("orderNo"		,	order_no);
				paramMap[i].put("custNo"		,	cust_no);
				paramMap[i].put("membNo"	    ,   memb_no);
				paramMap[i].put("paCode" 		,   orderInputVO[i].getPaCode());
				paramMap[i].put("doFlag" 		,   orderInputVO[i].getDoFlag());
				paramMap[i].put("mappingSeq"	,	orderInputVO[i].getMappingSeq());
				paramMap[i].put("custName"		,	orderInputVO[i].getCustName());
				paramMap[i].put("custTel1"		,	orderInputVO[i].getCustTel1());
				paramMap[i].put("custTel2"		,	orderInputVO[i].getCustTel2());
				paramMap[i].put("custChar"		,	orderInputVO[i].getCustChar());
				paramMap[i].put("orderQty"  	,	orderInputVO[i].getOrderQty());
				paramMap[i].put("mediaCode"		,	orderInputVO[i].getMediaCode());
				paramMap[i].put("orderDate"		,	orderInputVO[i].getOrderDate());
				paramMap[i].put("paGoodsCode"	,	orderInputVO[i].getPaGoodsCode());
				paramMap[i].put("goodsCode"		,	orderInputVO[i].getGoodsCode());
				paramMap[i].put("goodsdtCode"	,	orderInputVO[i].getGoodsdtCode());
				paramMap[i].put("orderQty"		,	orderInputVO[i].getOrderQty());
				paramMap[i].put("applyDate"		,	orderInputVO[i].getApplyDate());
				paramMap[i].put("rsaleAmt"		,	orderInputVO[i].getRsaleAmt());
				paramMap[i].put("supplyPrice"	,	orderInputVO[i].getSupplyPrice());
				paramMap[i].put("sellerDcAmt"	,	orderInputVO[i].getSellerDcAmt());								
				paramMap[i].put("receiverName"	,	orderInputVO[i].getReceiverName());
				paramMap[i].put("receiverTel"	,	orderInputVO[i].getReceiverTel());
				paramMap[i].put("receiverHp"	,	orderInputVO[i].getReceiverHp());
				paramMap[i].put("receiverAddr"	,	orderInputVO[i].getReceiverAddr());
				paramMap[i].put("msg"			,	orderInputVO[i].getMsg());
				paramMap[i].put("postNo"        ,   orderInputVO[i].getPostNo()); 
				paramMap[i].put("postSeq"       ,   orderInputVO[i].getPostNoSeq()); 
				paramMap[i].put("addr"          ,   orderInputVO[i].getStdAddr()); 
				paramMap[i].put("addrDt"        ,   orderInputVO[i].getStdAddrDT()); 
				paramMap[i].put("addrGbn"       ,   orderInputVO[i].getAddrGbn());
				paramMap[i].put("receiveMethod"	,	orderInputVO[i].getReceiveMethod());
				paramMap[i].put("priceSeq"		,	orderInputVO[i].getPriceSeq());
				paramMap[i].put("paShipCostAmt"	,	orderInputVO[i].getShpFeeCost());
				paramMap[i].put("userId"		,	orderInputVO[0].getProcUser());
				paramMap[i].put("sysDateTime"	, 	sysDateTime);
				
				paramMap[i].put("orderPromo"	, 	orderInputVO[i].getOrderPromo()); //판매가 기준 변경에 의한 Promotion
				paramMap[i].put("orderPaPromo"	, 	orderInputVO[i].getOrderPaPromo()); //제휴OUT프로모션 : 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
				
				goodsInfo[i] = selectGoodsInfo(paramMap[i]);				
				paramMap[i].put("goodsCode"		, 	goodsInfo[i].getGoodsCode());
				paramMap[i].put("goodsName"		, 	goodsInfo[i].getGoodsName());
				paramMap[i].put("goodsdtCOde"	,	goodsInfo[i].getGoodsDtCode());		
				paramMap[i].put("delyType"		,	goodsInfo[i].getDelyType());
				paramMap[i].put("stockChkPlace" ,	goodsInfo[i].getStockChkPlace());
				paramMap[i].put("whCode"		,	goodsInfo[i].getWhCode());	
				paramMap[i].put("entpCode"		,	goodsInfo[i].getEntpCode());
				paramMap[i].put("entpCodeOrg"	,	goodsInfo[i].getEntpCodeOrg());
				paramMap[i].put("salePrice"		,	goodsInfo[i].getSalePrice());
				paramMap[i].put("buyPrice"		,	goodsInfo[i].getBuyPrice());
				paramMap[i].put("arsOwnDcAmt"	,	goodsInfo[i].getArsOwnDcAmt());
				paramMap[i].put("arsEntpDcAmt"	,	goodsInfo[i].getArsEntpDcAmt());
				paramMap[i].put("goodsName"		,	goodsInfo[i].getGoodsName());
				paramMap[i].put("inviGoodsType"	,	goodsInfo[i].getInviGoodsType());
				paramMap[i].put("mdCode"		,	goodsInfo[i].getMdCode());
				paramMap[i].put("directShipYn"	,	goodsInfo[i].getDirectShipYn());
				paramMap[i].put("goodsdtInfo"	,	goodsInfo[i].getGoodsDtInfo());		
				paramMap[i].put("norestAllotMonths",goodsInfo[i].getNorestAllotMonths());
				
				if(orderInputVO[i].getPaCode().equals("21") || orderInputVO[i].getPaCode().equals("22")){
					paramMap[i].put("lumpSumDcAmt"		,orderInputVO[i].getLumpSumDcAmt());
					paramMap[i].put("lumpSumOwnDcAmt"	,orderInputVO[i].getLumpSumOwnDcAmt());
					paramMap[i].put("lumpSumEntpDcAmt"	,orderInputVO[i].getLumpSumEntpDcAmt());					
				}else{
					paramMap[i].put("lumpSumDcAmt"		, 0);
					paramMap[i].put("lumpSumOwnDcAmt"	, 0);
					paramMap[i].put("lumpSumEntpDcAmt"	, 0);	
				}
				
				paramMap[i].put("instantCouponDiscount", ComUtil.NVL(orderInputVO[i].getInstantCouponDiscount(), 0)); //saveOrder와 싱크맞추기 위함
				paramMap[i].put("paOrderNo"				, orderInputVO[i].getPaOrderCode());
				
				
				//각 제휴사에서 특정 CASE(예를들어 제휴사 비회원 주문) 에서 할인금액 0원으로 넘어오는 경우가 있음
				//그러나 영업데이터 생성할때  실제 할인금액이 0원임에도 ARS,일시불 할인  적용하는 경우가 있어서 해당 코드 반영함.
				if("N".equals(orderInputVO[i].getSellerDcAmtExists())) {
					paramMap[i].put("sellerDcAmt"		, 0);  //ARS
					paramMap[i].put("arsOwnDcAmt"		, 0);
					paramMap[i].put("arsEntpDcAmt"		, 0);
					paramMap[i].put("lumpSumDcAmt"		, 0);
					paramMap[i].put("lumpSumOwnDcAmt"	, 0);
					paramMap[i].put("lumpSumEntpDcAmt"	, 0);	
				}
				
				
			}			
		}catch(Exception e){
			throw processException("pa.fail_setting_orderInfo" ,  new String[] { e.toString()});
		}
	}
	//= 상품 정보 조회 
	private OrderInfoMsg selectGoodsInfo(ParamMap paramMap) throws Exception{		
		OrderInfoMsg goodsInfo = new OrderInfoMsg();
		try{
			goodsInfo = paorderDAO.selectGoodsInfo(paramMap);	
		}catch(Exception e){
			throw processMessageException("selectGoodsInfo select fail");
		}
		
		if(goodsInfo == null){
		    throw processMessageException("selectGoodsInfo select null");
		}	
		return goodsInfo;	
	}
	
	//= 주소 정제
	private ParamMap refineAddressInfo(ParamMap paramMap) throws Exception{	
		try{
			String cust_postNo  = paramMap.getString("postNo");  //다중배송지 처리 x
			String cust_postSeq = paramMap.getString("postNoSeq");
			String cust_AddrGbn = paramMap.getString("addrGbn");
			String cust_Addr    = paramMap.getString("addr");
			String cust_AddrDt  = paramMap.getString("addrDt");
			ParamMap inputParamMap  = null;
			ParamMap resultParamMap = null;

			if (cust_postNo  == null || "".equals(cust_postNo) )  cust_postNo  = "999999";
			if (cust_postSeq == null || "".equals(cust_postSeq))  cust_postSeq = "001";
			if (cust_AddrGbn == null || "".equals(cust_AddrGbn))  cust_AddrGbn = "00";
			
			inputParamMap  = new ParamMap();
			inputParamMap.put("post_no"		,   cust_postNo);
			inputParamMap.put("search_addr"	,   cust_Addr);	
			inputParamMap.put("search_addr2",	cust_AddrDt);
			inputParamMap.put("localYn"		, paramMap.getString("localYn"));
			
			if("02".equals(cust_AddrGbn)){ //01이면 지번 , 02면 도로명
				inputParamMap.put("road_addr_yn", 1);
			}
			else{
				inputParamMap.put("road_addr_yn", 0);
			}	
			
			resultParamMap = systemProcess.selectStdAddress(inputParamMap);
			resultParamMap.put("search_addr" , cust_Addr);
			resultParamMap.put("search_addr2", cust_AddrDt);
			return resultParamMap;//sk스토아 주소정제
			
		}catch(Exception e){
			throw processException("pa.address_fails", new String[] { e.toString() });
		}
	}
	
	private ParamMap refineAddressInfo(OrderInputVO[] orderInputVO) throws Exception{	
		try{
			String cust_postNo  = orderInputVO[0].getPostNo();  //다중배송지 처리 x
			String cust_postSeq = orderInputVO[0].getPostNoSeq();
			String cust_AddrGbn = orderInputVO[0].getAddrGbn();
			String cust_Addr    = orderInputVO[0].getStdAddr();
			String cust_AddrDt  = orderInputVO[0].getStdAddrDT();
			ParamMap inputParamMap  = null;
			ParamMap resultParamMap = null;

			if (cust_postNo  == null || "".equals(cust_postNo) )  cust_postNo  = "999999";
			if (cust_postSeq == null || "".equals(cust_postSeq))  cust_postSeq = "001";
			if (cust_AddrGbn == null || "".equals(cust_AddrGbn))  cust_AddrGbn = "00";
			
			inputParamMap  = new ParamMap();
			inputParamMap.put("post_no"		,   cust_postNo);
			inputParamMap.put("search_addr"	,   cust_Addr);	
			inputParamMap.put("search_addr2",	cust_AddrDt);
			inputParamMap.put("localYn"		, orderInputVO[0].getIsLocalYn());
			
			if("02".equals(cust_AddrGbn)){ //01이면 지번 , 02면 도로명
				inputParamMap.put("road_addr_yn", 1);
			}
			else{
				inputParamMap.put("road_addr_yn", 0);
			}	
			
			resultParamMap = systemProcess.selectStdAddress(inputParamMap);
			resultParamMap.put("search_addr" , cust_Addr);
			resultParamMap.put("search_addr2", cust_AddrDt);
			return resultParamMap;//sk스토아 주소정제
			
		}catch(Exception e){
			throw processException("pa.address_fails", new String[] { e.toString() });
		}
	}
	
	private void orderStock20Approval(List<OrderdtVO> orderdtList, List<Orderstock> stocklist) throws Exception{
		Orderstock stock = null;
		long orderQty 	= 0;
		long outPlanQty	= 0;
		long totalQty	= 0;
		
		for (OrderdtVO regOrderdt : orderdtList ) {
			orderQty 	=  -1 * regOrderdt.getOrderQty();  //무통장
			totalQty	=  0;
			outPlanQty	=  regOrderdt.getOrderQty();
			
			stock = settingStock(regOrderdt,orderQty,outPlanQty,totalQty );
			stocklist.add(stock);
		}
	}

		
	private void newOrderStock(List<OrderdtVO> orderdtList, List<Orderstock> stocklist) throws Exception{
		Orderstock stock = null;
		long orderQty 	= 0;
		long outPlanQty	= 0;
		long totalQty	= 0;
						
		for (OrderdtVO regOrderdt : orderdtList ) {
			
			if(Integer.parseInt(regOrderdt.getDoFlag()) > 10){
				orderQty 	=	0;
				outPlanQty	=	regOrderdt.getOrderQty();
			}else{
				orderQty 	=   regOrderdt.getOrderQty();  //무통장
				outPlanQty	=	0;
			}
			totalQty = orderQty + outPlanQty; 
			
			stock = settingStock(regOrderdt,orderQty,outPlanQty,totalQty );
			stocklist.add(stock);
		} 
	}
	
	private Orderstock settingStock( OrderdtVO regOrderdt, long orderQty, long outPlanQty, long totalQty ){
		Orderstock stock = new Orderstock();
		stock.setGoodsCode			(regOrderdt.getGoodsCode());
		stock.setGoodsdtCode		(regOrderdt.getGoodsdtCode());
		stock.setOrderQty			(orderQty);
		stock.setOutPlanQty			(outPlanQty);
		stock.setTotSaleQty			(totalQty);
		stock.setWhCode				(regOrderdt.getWhCode());
		stock.setModifyId			(regOrderdt.getLastProcId());
		stock.setModifyDate			(regOrderdt.getLastProcDate());
		return stock;
	}
	
	private int updateOrderStock20Approval(List<Orderstock> stocklist) throws Exception{
		int executedRtn = 0;	
	
		for (Orderstock stock : stocklist) {
			executedRtn = paorderDAO.updateOrderStock(stock);
	
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERSTOCK UPDATE"});
			}
		}
		return executedRtn;
	}
	
	private int updateOrderStock(List<Orderstock> stocklist) throws Exception {
		int executedRtn = 0;	
		ParamMap stockMap = null;
		int orderAbleQty = 0;

		for (Orderstock stock : stocklist) {
			//executedRtn = paorderDAO.updateOrderStockEnd(stock);
			executedRtn = paorderDAO.updateOrderStock(stock);
			
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERSTOCK UPDATE"});
			}
			
			//= torderstock update 후 잔여 재고 check.
			stockMap = new ParamMap();
			stockMap.put("goodsCode", 	stock.getGoodsCode());
			stockMap.put("goodsdtCode", stock.getGoodsdtCode());
			stockMap.put("whCode", 		stock.getWhCode());
			
			orderAbleQty = paorderDAO.selectOrderAbleQtyPa(stockMap);
			//= FUN_GET_ORDER_ABLE_QTY 조회 결과 (-)수량일 경우 exception. 
			if(orderAbleQty < 0){
				//[상품코드/단품코드] 의 재고가 없습니다. 에러 메세지 수정시 PaHalfAsyncController.setResultMap() 수정
				throw processMessageException(getMessage("errors.nodata.stock", new String[] { stockMap.getString("goodsCode") + "/" + stockMap.getString("goodsdtCode") })); 
			}
		}
		return executedRtn;
	}
	
	
	
	/**
	 * 주문저장
	 * 
	 * @param  ParamMap
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public HashMap<String, Object>[] saveOrder(OrderInputVO[] orderInputVO) throws Exception {
		int paramLength	= orderInputVO.length;
		String paOrderCode = "";
		String rtnValue = "000000";
		String user_id = orderInputVO[0].getProcUser();//11번가는 PA11, G마켓은 PAG
		
		HashMap<String, String> test = new HashMap<>();
		
		
		// 주문데이터 객체 생성
		Orderm     orderm     = new Orderm();
		Customer   customer   = new Customer();
		Receiver   receiver   = new Receiver();	
		Custsystem custsystem = new Custsystem();
		Custtel custtel = null;
		Orderpromo orderpromo = null;
		Ordergoods ordergoods = null;
		OrderdtVO orderdt 	  = null;
		Ordershipcost ordershipcost = null;
		PromocounselVO promoCounsel = null;
		Orderreceipts orderreceipts = null;
		PaOrderShipCost paordershipcost = null;
		List<Custtel> custtelList = new ArrayList<Custtel>();
		List<Couponissue> couponissueList    	= new ArrayList<Couponissue>(); 
		List<ParamMap> paramAdresses	= new ArrayList<ParamMap>();
		 
		ParamMap[] paramMap 		= new ParamMap[paramLength];
		HashMap[] resultMap 		= new HashMap[paramLength];
		OrderInfoMsg[] goodsInfo    = new OrderInfoMsg[paramLength];
				
		String    order_no    	 = selectSequenceNo("ORDER_NO");	   						// 주문번호seq 생성
		String    cust_no     	 = selectSequenceNo("CUST_NO");								// 고객번호seq 생성
		String    memb_no	  	 = selectSequenceNo("MEMB_NO");								// 회원번호seq 생성
		String    dateTime       = getSysdatetimeToString();								// 시간생성(String)
		String    receipt_no  	 = "";
		String 	  goodsSelectNo  = "";
		Timestamp sysDateTime    = DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss");	// 시간생성(Timestamp)
		Timestamp orderDate      = sysDateTime;//정산으로 인해 orderDate는 11번가 주문일이 아닌 sysdate로 합니다.
		int 	  executedRtn    = 0;
		int 	  orderDSeq 	 = 1;
		long 	  l_temp		 = 0;
		long      paShipFeeAmt   = 0;
		
		Boolean   isUnRefinedAddress = false;
		
		
		try{
			
			// 상품코드 및 상품정보 매핑
			for (int i = 0; i < paramLength; i++) {
				//주소정제
				paramAdresses.add(refineAddress(orderInputVO[i]));			
				
				//orderDate = orderInputVO[i].getOrderDate();  // 이 주석을 절!대! 함부로 풀지 마세요..!!! orderpromo에서 뻑남..
				//if(orderDate == null) orderDate = sysDateTime; 
				//정산으로 인해 orderDate는 11번가 주문일이 아닌 sysdate로 합니다. 
				paOrderCode = orderInputVO[i].getPaOrderCode();
				
				
				paramMap[i] = new ParamMap();
				paramMap[i].put("orderNo",			order_no);
				paramMap[i].put("custNo",			cust_no);
				paramMap[i].put("paCode" ,          orderInputVO[i].getPaCode());
				paramMap[i].put("mappingSeq",		orderInputVO[i].getMappingSeq());
				paramMap[i].put("custName",			orderInputVO[i].getCustName());
				paramMap[i].put("custTel1",			orderInputVO[i].getCustTel1());
				paramMap[i].put("custTel2",			orderInputVO[i].getCustTel2());
				paramMap[i].put("custChar",			orderInputVO[i].getCustChar());
				paramMap[i].put("orderQty", 		orderInputVO[i].getOrderQty());
				paramMap[i].put("mediaCode",		orderInputVO[i].getMediaCode());
				paramMap[i].put("orderDate",		orderInputVO[i].getOrderDate());
				paramMap[i].put("paGoodsCode",		orderInputVO[i].getPaGoodsCode());
				paramMap[i].put("goodsCode",		orderInputVO[i].getGoodsCode());
				paramMap[i].put("goodsdtCode",		orderInputVO[i].getGoodsdtCode());
				paramMap[i].put("orderQty",			orderInputVO[i].getOrderQty());
				paramMap[i].put("applyDate",		orderInputVO[i].getApplyDate());
				paramMap[i].put("rsaleAmt",			orderInputVO[i].getRsaleAmt());
				paramMap[i].put("supplyPrice",		orderInputVO[i].getSupplyPrice());
				paramMap[i].put("sellerDcAmt",		orderInputVO[i].getSellerDcAmt());
				paramMap[i].put("receiverName",		orderInputVO[i].getReceiverName());
				paramMap[i].put("receiverTel",		orderInputVO[i].getReceiverTel());
				paramMap[i].put("receiverHp",		orderInputVO[i].getReceiverHp());
				paramMap[i].put("receiverAddr",		orderInputVO[i].getReceiverAddr());
				paramMap[i].put("msg",				orderInputVO[i].getMsg());
				paramMap[i].put("postNo",           orderInputVO[i].getPostNo()); 
				paramMap[i].put("postSeq",          orderInputVO[i].getPostNoSeq()); 
				paramMap[i].put("addr",             orderInputVO[i].getStdAddr()); 
				paramMap[i].put("addrDt",           orderInputVO[i].getStdAddrDT()); 
				paramMap[i].put("addrGbn",          orderInputVO[i].getAddrGbn()); 
				paramMap[i].put("roadAddrNo",       paramAdresses.get(i).getString("ROAD_ADDR_NO"));
				paramMap[i].put("stdPostNo",        paramAdresses.get(i).getString("STD_POST_NO"));
				paramMap[i].put("receiveMethod",	orderInputVO[i].getReceiveMethod());
				paramMap[i].put("priceSeq",			orderInputVO[i].getPriceSeq());
				paramMap[i].put("instantCouponDiscount", ComUtil.NVL(orderInputVO[i].getInstantCouponDiscount(), 0));
			
				
				
				if(orderInputVO[i].getOrderPromo() != null){
					paramMap[i].put("orderPromo"	, 	orderInputVO[i].getOrderPromo()); //판매가 기준 변경에 의한 Promotion
				}
				
				//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
				if(orderInputVO[i].getOrderPaPromo() != null){
					paramMap[i].put("orderPaPromo"	, 	orderInputVO[i].getOrderPaPromo()); //제휴OUT프로모션 : 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
				}
				//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
				
				//각 입점 사이트로 부터 실제 고객이 계산한 금액 setting
				paShipFeeAmt += orderInputVO[i].getShpFeeCost();
				
				try{
					log.info("//----goodsInfo select");
					goodsInfo[i] = paorderDAO.selectGoodsInfo(paramMap[i]);
				}catch (Exception e) {
					rtnValue = "selectGoodsInfo select fail";
				    throw processMessageException(rtnValue);
				}
				
				if(goodsInfo[i] == null){
					rtnValue = "selectGoodsInfo select null";				    
				    throw processMessageException(rtnValue);
				}
			

				paramMap[i].put("entpCode"			,	goodsInfo[i].getEntpCode());
				paramMap[i].put("entpCodeOrg"		,	goodsInfo[i].getEntpCodeOrg());
				paramMap[i].put("salePrice"			,	goodsInfo[i].getSalePrice());
				paramMap[i].put("buyPrice"			,	goodsInfo[i].getBuyPrice());
				paramMap[i].put("goodsName"			,	goodsInfo[i].getGoodsName());
				paramMap[i].put("inviGoodsType"		,	goodsInfo[i].getInviGoodsType());
				paramMap[i].put("norestAllotMonths"	,	goodsInfo[i].getNorestAllotMonths());
				paramMap[i].put("delyType"			,	goodsInfo[i].getDelyType());
				paramMap[i].put("whCode"			,	goodsInfo[i].getWhCode());				
				paramMap[i].put("mdCode"			,	goodsInfo[i].getMdCode());
				paramMap[i].put("directShipYn"		,	goodsInfo[i].getDirectShipYn());
				paramMap[i].put("stockChkPlace"		,	goodsInfo[i].getStockChkPlace());
				paramMap[i].put("goodsdtInfo"		,	goodsInfo[i].getGoodsDtInfo());
				paramMap[i].put("arsDcAmt"			,	goodsInfo[i].getArsDcAmt());
				paramMap[i].put("arsOwnDcAmt"		,	goodsInfo[i].getArsOwnDcAmt());
				paramMap[i].put("arsEntpDcAmt"		,	goodsInfo[i].getArsEntpDcAmt());
				paramMap[i].put("lumpSumDcAmt"		,	orderInputVO[i].getLumpSumDcAmt());
				paramMap[i].put("lumpSumOwnDcAmt"	,	orderInputVO[i].getLumpSumOwnDcAmt());
				paramMap[i].put("lumpSumEntpDcAmt"	,	orderInputVO[i].getLumpSumEntpDcAmt());	
				paramMap[i].put("userId"			,	user_id);
				paramMap[i].put("sysDateTime"		,	sysDateTime);
				
				
				//각 제휴사에서 특정 CASE(예를들어 제휴사 비회원 주문) 에서 할인금액 0원으로 넘어오는 경우가 있음
				//그러나 영업데이터 생성할때  실제 할인금액이 0원임에도 ARS,일시불 할인  적용하는 경우가 있어서 해당 코드 반영함.
				if("N".equals(orderInputVO[i].getSellerDcAmtExists())) {
					paramMap[i].put("arsDcAmt"			, 0);  //ARS
					paramMap[i].put("arsOwnDcAmt"		, 0);
					paramMap[i].put("arsEntpDcAmt"		, 0);
					paramMap[i].put("lumpSumDcAmt"		, 0);
					paramMap[i].put("lumpSumOwnDcAmt"	, 0);
					paramMap[i].put("lumpSumEntpDcAmt"	, 0);	
				}				
			}
			
			//= 1) 재고체크
			log.info("//----CALSTOCK check");
			
			
			paramMap = calStock(paramMap , goodsInfo);	
			
			if(paramMap == null){ //= 전체 주문불가.
				rtnValue = getMessage("pa.fail_order_input", new String[]{getMessage("pa.out_of_stock")});
				for(int i = 0; paramLength > i; i++){
					resultMap[i] = new HashMap<>();
					resultMap[i].put("MAPPING_SEQ", 	orderInputVO[i].getMappingSeq());
					resultMap[i].put("RESULT_CODE", 	"100001"); 
					resultMap[i].put("RESULT_MESSAGE", 	rtnValue);
				}
				return resultMap;
			}
			
			//------[고객데이터 생성]--------
			//tcustomer 세팅
			log.info("//----TCUSTOMER setting");
			customer.setBlank();
			customer.setCustNo(cust_no);
			customer.setCustName(paramMap[0].getString("custName"));
			customer.setEname("");
			customer.setCustName1(paramMap[0].getString("custName"));
			customer.setCustName2("");
			customer.setCustName3("");
			customer.setSex("9");
			customer.setBirthdayYn("1");
			customer.setBirthday("");
			customer.setWeddingYn("0");
			customer.setWeddingDate(null);
			customer.setEmYn("0");
			customer.setEmNo("");
			customer.setMembNo(memb_no);
			customer.setMemId("");
			customer.setIdInsertDate(null);
			customer.setPasswd("");
			customer.setPasswdHint("");
			customer.setPasswdAnswer("");
			customer.setResidentNo("");		
			customer.setJobCode("99");
			customer.setCompName("");
			customer.setCompDeptname("");		
			customer.setCountry("0082");
			customer.setEmailAddr("");
			customer.setEmailYn("0");
			customer.setSmsYn("0");
			customer.setOrderEmailYn("0");
			customer.setOrderSmsYn("0");
			customer.setNominateYn("0");
			customer.setNominateId("");
			customer.setCustSource("999999");
			customer.setWithdrawalYn("0");
		    customer.setWithdrawalCode("");
		    customer.setWithdrawalContent("");
		    customer.setWithdrawalDate(null);
		    customer.setEmailFlag("01");
		    customer.setEmailBlockCode("0");
		    customer.setEmailBlockDate(null);
		    customer.setNonAgeYn("0");
		    customer.setParentName("");
		    customer.setParentHp("");
		    customer.setParentDi("");
		    customer.setRemark1V("0");
		    customer.setRemark2V("");
		    customer.setRemark3V("");
		    customer.setReceiveMethod(paramMap[0].getString("receiveMethod"));
		    customer.setInsertId(user_id);
		    customer.setInsertDate(sysDateTime);
		    customer.setModifyId(user_id);
		    customer.setModifyDate(sysDateTime);	    
			
	        //tcustsystem 세팅
		    log.info("//----TCUSTSYSTEM setting");
	        custsystem.setBlank();
	        custsystem.setCustNo(cust_no);
	        custsystem.setCustGb("00");		
	        custsystem.setMembGb("90");
	        custsystem.setCustWarning("1001");
	        custsystem.setCustChar(paramMap[0].getString("custChar"));
	        custsystem.setUseDeposit(0);
	        custsystem.setUsePbDeposit(0);
	        custsystem.setTotSaveamt(0);
	        custsystem.setDmYn("1");
	        custsystem.setDmNoGb("00");
	        custsystem.setDmNoId("");
	        custsystem.setDmNoDate(null);
	        custsystem.setFirstOrderDate(null);
	        custsystem.setLastOrderDate(null);
	        custsystem.setFirstMsaleGb("");
	        custsystem.setTotOrderCnt(0);
	        custsystem.setTotOrderAmt(0);
	        custsystem.setTotCancelCnt(0);
	        custsystem.setTotCancelAmt(0);
	        custsystem.setTotReturnCnt(0);
	        custsystem.setTotReturnAmt(0);
	        custsystem.setMembTotAmt(0);        
	        custsystem.setBirthdayMmdd("0101");
	        custsystem.setModifyId(user_id);
	        custsystem.setModifyDate(sysDateTime);
	        custsystem.setLastOrderDate(orderDate);
	        custsystem.setLastConMediaCode(paramMap[0].getString("mediaCode"));
	        custsystem.setLastOrderMedia("62");
	        custsystem.setLastOrderMediaCode(paramMap[0].getString("mediaCode"));
	        custsystem.setInsertMedia("62");
	        custsystem.setInsertMediaCode(paramMap[0].getString("mediaCode"));
	        custsystem.setInsertDate(sysDateTime);
	        
	        //= tcusttel 세팅
	        if(!(paramMap[0].getString("custTel1") + paramMap[0].getString("custTel2")).equals("")) { //= 주문자 전화번호가 있는 경우
	        	if(!paramMap[0].getString("custTel1").equals("")){
	        		custtel = new Custtel();
	        		custtel.setCustNo(cust_no);
	        		custtel.setTel(paramMap[0].getString("custTel1"));
	        		custtelList.add(custtel);
	        	}
	        	if(!paramMap[0].getString("custTel2").equals("") && ! paramMap[0].getString("custTel2").equals(paramMap[0].getString("custTel1"))){
	        		custtel = new Custtel();
	        		custtel.setCustNo(cust_no);
	        		custtel.setTel(paramMap[0].getString("custTel2"));
	        		custtelList.add(custtel);
	        	}
	        }
	        
			//orderm 세팅
		    log.info("//----TORDERM setting");
	        orderm.setBlank();
			orderm.setOrderNo(order_no);
			orderm.setCustNo(cust_no);
			orderm.setOrderDate(orderDate);
			orderm.setOrderMedia("62");
			orderm.setInsertId(user_id);
			orderm.setPassword("");
			orderm.setWithCode("");
			orderm.setIpAddr("");
			orderm.setMembGb("90");
			orderm.setEmployeeId("");
			orderm.setSenderName(paramMap[0].getString("custName"));
			orderm.setChannel("");
			orderm.setChannel_id("");		

			//receiver, orderdt 세팅
			// =>사은품 프로모션이 존재 할 경우 orderpromo 추가 세팅.
			log.info("//----TORDERDT setting");
			long RsaleAmt = 0;			
			
			List<OrderdtVO> orderdtList = new ArrayList<OrderdtVO>();
			List<Orderpromo> orderpromoList = new ArrayList<Orderpromo>();
			List<Orderreceipts> orderreceiptsList = new ArrayList<Orderreceipts>();
			List<Receiver> receiverList = new ArrayList<Receiver>();
			List<Ordershipcost> ordershipcostList = new ArrayList<Ordershipcost>();
			List<Ordercostapply> ordercostapplyList = new ArrayList<Ordercostapply>();
			
			
			boolean createTarget = false; //= receiver 생성대상 : true = 생성, false = 미생성
			int rCreateCnt = 0; //= receiver 생성 갯수.
			String preAddr = ""; //= 이전순번 주소
			String curAddr = "";   //= 현재순번 주소
			long receiverSeq = 0;
			
			int cnt = 0;
			int receiverfind = 0;
			
			for (int i = 0; i < paramLength; i++) {
				//= 재고부족  상품의 주문건은 주문데이터를 생성하지 않음.
				if(paramMap[i].getString("orderPossYn").equals("0")) continue;
				
				//= receiver 세팅
				if(i == 0){
					createTarget = true;
					cnt++;
				} else {
					createTarget = true;
					for(int j = 0; j < i; j++){
						if(paramMap[i].getString("receiverAddr") != null) 	curAddr = paramMap[i].getString("receiverAddr");
						if(paramMap[j].getString("receiverAddr") != null) 	preAddr = paramMap[j].getString("receiverAddr");
						
						if((curAddr).equals(preAddr) //= 동일한 주소가 있는지 비교.
								&& paramMap[j].getString("orderPossYn").equals("1")  ){
							receiverfind = j;
							createTarget = false;
							break;
						}
					}
					
					if(createTarget){
						cnt++;
					}
				}

				if(createTarget){
					final String receiverAddress = paramMap[i].getString("receiverAddr");
					paramMap[i].put("createCnt", cnt);
					ParamMap paramAdress =  paramAdresses.stream().filter(n -> receiverAddress.equals(
							n.getString("FULL_ADDR"))).findAny().get();
										
					//treceiver 세팅
					log.info("//----TRECEIVER setting");
					
					receiverSeq = Long.parseLong(systemService.getMaxNo("TRECEIVER", "RECEIVER_SEQ", " CUST_NO = '"+ cust_no +"'", 10)) + rCreateCnt;
					rCreateCnt++;
					
			        receiver = new Receiver();
			        receiver.setBlank();		
			        receiver.setCustNo(cust_no);
			        receiver.setReceiverSeq	(ComUtil.lpad(Long.toString(receiverSeq), 10, "0"));
			        receiver.setUseYn ("1");
			        receiver.setDefaultYn ("1");
			        String receiverName = paramMap[i].getString("receiverName");
			        receiverName = ComUtil.removeEmoji(receiverName);
			        receiver.setReceiver(receiverName);
					receiver.setReceiver1(receiverName);
					receiver.setReceiver2("");
					receiver.setReceiver3("");		
					
			        String tel = paramMap[i].getString("receiverTel");
			        String hp  = paramMap[i].getString("receiverHp");
			        
			        tel = tel.replace("-", "");
			        hp  = hp.replace("-", "");
			        
					if(!tel.equals("")){		//= 일반 전화번호
			            receiver.setTel(tel);
						if(tel.length() == 7){	//= DDD 없음
							receiver.setReceiverDdd("");
							receiver.setReceiverTel1(tel.substring(0, 3));
							receiver.setReceiverTel2(tel.substring(3, 7));
						}else if(tel.length() == 8){ //= DDD 없음
							receiver.setReceiverDdd("");
							receiver.setReceiverTel1(tel.substring(0, 4));
							receiver.setReceiverTel2(tel.substring(4, 8));
						}else if(tel.length() == 9){
							receiver.setReceiverDdd(tel.substring(0, 2));
							receiver.setReceiverTel1(tel.substring(2, 5));
							receiver.setReceiverTel2(tel.substring(5, 9));
						}else if(tel.length() == 10){
							if(tel.substring(0, 2).equals("02")){
								receiver.setReceiverDdd(tel.substring(0, 2));
								receiver.setReceiverTel1(tel.substring(2, 6));
								receiver.setReceiverTel2(tel.substring(6, 10));
							}else{
								receiver.setReceiverDdd(tel.substring(0, 3));
								receiver.setReceiverTel1(tel.substring(3, 6));
								receiver.setReceiverTel2(tel.substring(6, 10));
							}
						}else if(tel.length() == 11){
							receiver.setReceiverDdd(tel.substring(0, 3));
							receiver.setReceiverTel1(tel.substring(3, 7));
							receiver.setReceiverTel2(tel.substring(7, 11));
						}else if(tel.length() == 12){
							receiver.setReceiverDdd(tel.substring(0, 4));
							receiver.setReceiverTel1(tel.substring(4, 8));
							receiver.setReceiverTel2(tel.substring(8, 12));
						}
					}
					
					if(!hp.equals("")){	//= 휴대전화번호
			            receiver.setReceiverHp(hp);
						if(hp.length() == 10){
							receiver.setReceiverHp1(hp.substring(0, 3));
							receiver.setReceiverHp2(hp.substring(3, 6));
							receiver.setReceiverHp3(hp.substring(6, 10));
						}else if(hp.length() == 11){
							receiver.setReceiverHp1(hp.substring(0, 3));
							receiver.setReceiverHp2(hp.substring(3, 7));
							receiver.setReceiverHp3(hp.substring(7, 11));
						}else if(hp.length() == 12){
							receiver.setReceiverHp1(hp.substring(0, 4));
							receiver.setReceiverHp2(hp.substring(4, 8));
							receiver.setReceiverHp3(hp.substring(8, 12));
						}else{
							receiver.setReceiverHp1(hp.substring(0, 4));
							receiver.setReceiverHp2(hp.substring(4, 8));
							receiver.setReceiverHp3(hp.substring(8));
						}
					}
						
					
					//주소정제 시작
					if(paramAdress.getString("CL_SUCCESS_YN").equals("1")){
						
						if( paramAdress.getString("FLG").equals("1") && !paramAdress.getString("STD_POST_NO").equals("")){
								
							if(paramAdress.getString("ADDR_GBN").equals("1")){
								
								
								
								//= 정제된 주소가 지번주소 일 경우.
								receiver.setReceiverPost	(paramAdress.getString("STD_POST_NO"));       //우편번호
								receiver.setReceiverPostSeq	(paramAdress.getString("STD_POST_SEQ"));	  //우편번호seq

								receiver.setReceiverPostAddr(paramAdress.getString("SEARCH_ADDR"));   		//입력주소1 - 지번
								receiver.setReceiverAddr	(paramAdress.getString("SEARCH_ADDRDT"));       //입력주소2 -지번
								
								receiver.setRoadAddrYn("0"); 
								receiver.setRoadAddrNo("");  //ROAD_ADDR_NO								
								receiver.setRoadPostAddr("");   //입력주소1  - 도로명
								receiver.setRoadAddr("");    //입력주소 2  - 도로명 
								
								
								//도로명
								receiver.setReceiverRoadPost(paramAdress.getString("STD_ROAD_POST_NO"));
								receiver.setReceiverRoadPostSeq(paramAdress.getString("STD_ROAD_POST_SEQ"));
								receiver.setReceiverRoadPostAddr(paramAdress.getString("STD_ROAD_POST_ADDR1"));
								receiver.setReceiverRoadPostAddr2(paramAdress.getString("STD_ROAD_POST_ADDR2"));					
								receiver.setStdRoadPostLaty(paramAdress.getString("STD_ROAD_POST_LATY"));
								receiver.setStdRoadPostLngx(paramAdress.getString("STD_ROAD_POST_LNGX"));
								
								//지번
								receiver.setReceiverStdPost(paramAdress.getString("STD_POST_NO"));
								receiver.setReceiverStdPostSeq(paramAdress.getString("STD_POST_SEQ"));
								receiver.setReceiverStdPostAddr(paramAdress.getString("STD_POST_ADDR1"));
								receiver.setReceiverStdPostAddr2(paramAdress.getString("STD_POST_ADDR2"));
								receiver.setStdPostLngx(paramAdress.getString("STD_POST_LNGX"));
								receiver.setStdPostLaty(paramAdress.getString("STD_POST_LATY"));
								
								receiver.setFullAddr(paramAdress.getString("STD_POST_ADDR1") + paramAdress.getString("STD_POST_ADDR2"));							
								receiver.setSelectAddr("3");
								receiver.setRefineResultCode(paramAdress.getString("REFINE_RESULT_CODE"));
								
								isUnRefinedAddress = true;
								
							
							} else if(paramAdress.getString("ADDR_GBN").equals("2")){
								//= 정제된 주소가 도로명주소 일 경우.
								receiver.setReceiverPost(paramAdress.getString("STD_ROAD_POST_NO"));
								receiver.setReceiverPostSeq(paramAdress.getString("STD_ROAD_POST_SEQ"));	
								
								receiver.setReceiverAddr("");                                  //입력주소1 -지번
								receiver.setReceiverPostAddr("");                              //입력주소2 - 지번
								
								receiver.setRoadAddrYn		("1");
								receiver.setRoadAddrNo		(paramAdress.getString("ROAD_ADDR_NO"));  //ROAD_ADDR_NO								
								receiver.setRoadPostAddr	(paramAdress.getString("SEARCH_ADDR"));   //입력주소1  - 도로명
								receiver.setRoadAddr		(paramAdress.getString("SEARCH_ADDRDT")); //입력주소 2  - 도로명 
								
								
								//도로명
								receiver.setReceiverRoadPost(paramAdress.getString("STD_ROAD_POST_NO"));
								receiver.setReceiverRoadPostSeq(paramAdress.getString("STD_ROAD_POST_SEQ"));
								receiver.setReceiverRoadPostAddr(paramAdress.getString("STD_ROAD_POST_ADDR1"));
								receiver.setReceiverRoadPostAddr2(paramAdress.getString("STD_ROAD_POST_ADDR2"));					
								receiver.setStdRoadPostLaty(paramAdress.getString("STD_ROAD_POST_LATY"));
								receiver.setStdRoadPostLngx(paramAdress.getString("STD_ROAD_POST_LNGX"));
								
								//지번
								receiver.setReceiverStdPost(paramAdress.getString("STD_POST_NO"));
								receiver.setReceiverStdPostSeq(paramAdress.getString("STD_POST_SEQ"));
								receiver.setReceiverStdPostAddr(paramAdress.getString("STD_POST_ADDR1"));
								receiver.setReceiverStdPostAddr2(paramAdress.getString("STD_POST_ADDR2"));
								receiver.setStdPostLngx(paramAdress.getString("STD_POST_LNGX"));
								receiver.setStdPostLaty(paramAdress.getString("STD_POST_LATY"));
								
								receiver.setFullAddr(paramAdress.getString("STD_ROAD_POST_ADDR1") + paramAdress.getString("STD_ROAD_POST_ADDR2"));							
								receiver.setSelectAddr("2");
								receiver.setRefineResultCode(paramAdress.getString("REFINE_RESULT_CODE"));
								
								isUnRefinedAddress = true;
								
							}else{
								
								receiver.setReceiverPost("999999");       //우편번호
								receiver.setReceiverPostSeq("001");	  //우편번호seq
								
								receiver.setReceiverAddr("주소정제에 실패했습니다. 제휴미지정배송지 처리를 해주세요");
								receiver.setReceiverPostAddr("실패주소 : " + paramAdress.getString("SEARCH_ADDR") +" " + paramAdress.getString("SEARCH_ADDRDT"));                            //입력주소2 - 지번
								
								receiver.setRoadAddrYn("0"); 
								receiver.setRoadAddrNo("");  //ROAD_ADDR_NO								
								receiver.setRoadPostAddr("");   //입력주소1  - 도로명
								receiver.setRoadAddr("");    //입력주소 2  - 도로명 
								
								
								//도로명
								receiver.setReceiverRoadPost("");
								receiver.setReceiverRoadPostSeq("");
								receiver.setReceiverRoadPostAddr("");
								receiver.setReceiverRoadPostAddr2("");					
								receiver.setStdRoadPostLaty("");
								receiver.setStdRoadPostLngx("");
								
								//지번
								receiver.setReceiverStdPost("");
								receiver.setReceiverStdPostSeq("");
								receiver.setReceiverStdPostAddr("");
								receiver.setReceiverStdPostAddr2("");
								receiver.setStdPostLngx("");
								receiver.setStdPostLaty("");
								
								receiver.setFullAddr(paramAdress.getString("SEARCH_ADDR") +" " + paramAdress.getString("SEARCH_ADDRDT"));							
								receiver.setSelectAddr("1");
								receiver.setRefineResultCode(paramAdress.getString("REFINE_RESULT_CODE"));
								
								
								
							}
							
							
						}else{ //수지넷 접속 성공 주소정제실패 Case
							
								receiver.setReceiverPost("999999");       //우편번호
								receiver.setReceiverPostSeq("001");	  //우편번호seq
								
								receiver.setReceiverAddr("주소정제에 실패했습니다. FLAG<>1 ");
								receiver.setReceiverPostAddr("실패주소 : " + paramAdress.getString("SEARCH_ADDR") +" " + paramAdress.getString("SEARCH_ADDRDT"));   //입력주소2 - 지번
								
								receiver.setRoadAddrYn("0"); 
								receiver.setRoadAddrNo("");  //ROAD_ADDR_NO								
								receiver.setRoadPostAddr("");   //입력주소1  - 도로명
								receiver.setRoadAddr("");    //입력주소 2  - 도로명 
								
								
								//도로명
								receiver.setReceiverRoadPost("");
								receiver.setReceiverRoadPostSeq("");
								receiver.setReceiverRoadPostAddr("");
								receiver.setReceiverRoadPostAddr2("");					
								receiver.setStdRoadPostLaty("");
								receiver.setStdRoadPostLngx("");
								
								//지번
								receiver.setReceiverStdPost("");
								receiver.setReceiverStdPostSeq("");
								receiver.setReceiverStdPostAddr("");
								receiver.setReceiverStdPostAddr2("");
								receiver.setStdPostLngx("");
								receiver.setStdPostLaty("");
								
								receiver.setFullAddr(paramAdress.getString("SEARCH_ADDR") +" " + paramAdress.getString("SEARCH_ADDRDT"));							
								receiver.setSelectAddr("1");
								receiver.setRefineResultCode(paramAdress.getString("REFINE_RESULT_CODE"));
								

						}//end of CASE : 수지넷 접속 성공  ,주소정제실패
						
						
					} else {
						//= 정제유형 오류일 경우. (수지넷 접속 에러)
						receiver.setReceiverPost("999999");
						receiver.setReceiverPostSeq("001");									
						receiver.setReceiverAddr("주소정제에 실패했습니다. 제휴미지정배송지 처리를 해주세요");
						receiver.setReceiverPostAddr("실패주소 : " + paramAdress.getString("SEARCH_ADDR") +" " + paramAdress.getString("SEARCH_ADDRDT"));  //입력주소2 - 지번
	                           								
						receiver.setRoadAddrYn("0");
						receiver.setRoadAddrNo("");  							
						receiver.setRoadPostAddr("");  
						receiver.setRoadAddr("");    
						
						
						//도로명
						receiver.setReceiverRoadPost("");
						receiver.setReceiverRoadPostSeq("");
						receiver.setReceiverRoadPostAddr("");
						receiver.setReceiverRoadPostAddr2("");					
						receiver.setStdRoadPostLaty("");
						receiver.setStdRoadPostLngx("");
						
						//지번
						receiver.setReceiverStdPost("");
						receiver.setReceiverStdPostSeq("");
						receiver.setReceiverStdPostAddr("");
						receiver.setReceiverStdPostAddr2("");
						receiver.setStdPostLngx("");
						receiver.setStdPostLaty("");
						
						receiver.setFullAddr("");							
						receiver.setSelectAddr("1");
						receiver.setRefineResultCode(paramAdress.getString("REFINE_RESULT_CODE"));
						
					}//END OF 주소정제
					
					
					receiver.setLastUseDate(null);
					receiver.setBeforeSeq("");
					receiver.setValidTelYn("1");
					receiver.setReceiverTel("");
					receiver.setNickName("");
					receiver.setInsertId(user_id);
					receiver.setInsertDate(sysDateTime);
				    receiver.setModifyId(user_id);
				    receiver.setModifyDate(sysDateTime);
				    receiver.setSmartOrderYn("0");
				    
				    receiverList.add(receiver);
				
				}else{
					paramMap[i].put("createCnt", paramMap[receiverfind].getString("createCnt") );
				}
				
				orderDSeq = 0;
				orderdt = new OrderdtVO();
				orderdt.setOrderNo(order_no);
				orderdt.setOrderGSeq(ComUtil.increaseLpad(Integer.toString(i) , 3, "0"));
				orderdt.setOrderDSeq(ComUtil.increaseLpad(Integer.toString(orderDSeq) , 3, "0"));
				orderdt.setOrderWSeq("001");
				orderdt.setCustNo(cust_no);
				orderdt.setOrderDate(orderDate);
				orderdt.setMdCode(paramMap[i].getString("mdCode"));
				orderdt.setReceiverSeq(ComUtil.lpad(paramMap[i].getString("createCnt"), 10, "0") );
				orderdt.setOrderGb("10");
				orderdt.setDoFlag("20");
				orderdt.setGoodsGb("10");
				orderdt.setMediaGb("03");
				orderdt.setMediaCode(paramMap[i].getString("mediaCode"));
				orderdt.setGoodsCode(paramMap[i].getString("goodsCode"));
				orderdt.setGoodsdtCode(paramMap[i].getString("goodsdtCode"));
				orderdt.setGoodsdtInfo(paramMap[i].getString("goodsdtInfo"));
				
				orderdt.setSalePrice(paramMap[i].getDouble("salePrice"));
				orderdt.setBuyPrice(paramMap[i].getDouble("buyPrice"));
				orderdt.setOrderQty(paramMap[i].getLong("orderQty"));
				orderdt.setOrderAmt(orderdt.getSalePrice() * orderdt.getOrderQty());
				orderdt.setPriceSeq(paramMap[i].getString("priceSeq"));
				
				orderdt.setDcAmtMemb(0);
				orderdt.setDcAmtDiv(0);
				orderdt.setDcAmtCard(0);
				
				orderdt.setDcAmtGoods(orderdt.getOrderAmt() - paramMap[i].getDouble("rsaleAmt"));				
				//orderdt.setDcAmtGoods(orderdt.getOrderAmt() - (paramMap[i].getDouble("supplyPrice") * orderdt.getOrderQty()));
				
				//Example - 2260.00(DcAmt)/16000.00(OrderAmt)
				//orderdt.setDcRateGoods(Math.round(orderdt.getDcAmtGoods()/orderdt.getOrderAmt() * 10000)/100.0);
				orderdt.setDcRateGoods(ComUtil.modAmt(orderdt.getDcAmtGoods()/orderdt.getOrderAmt() * 10000) / 100.0 );
				
				
				orderdt.setDcRate(orderdt.getDcRateGoods());
				orderdt.setDcAmt(orderdt.getDcAmtGoods());
				
				//orderdt.setRsaleAmt(paramMap[i].getDouble("supplyPrice") * orderdt.getOrderQty());
				orderdt.setRsaleAmt(paramMap[i].getLong("rsaleAmt"));
				
				
				orderdt.setSyscancel(0);
				orderdt.setSyslast(orderdt.getOrderQty());
				orderdt.setSyslastDcGoods(orderdt.getDcAmtGoods());
				orderdt.setSyslastDcMemb(orderdt.getDcAmtMemb());
				orderdt.setSyslastDcDiv(orderdt.getDcAmtDiv());
				orderdt.setSyslastDcCard(orderdt.getDcAmtCard());
				orderdt.setSyslastDc(orderdt.getDcAmt());
				
				orderdt.setSyslastAmt(orderdt.getRsaleAmt());
				
				orderdt.setWhCode(paramMap[i].getString("whCode"));
				orderdt.setSaleYn("1");
				orderdt.setDelyType(paramMap[i].getString("delyType"));
				orderdt.setDelyGb("10");
				orderdt.setCustDelyFlag("0");
				
				orderdt.setFirstPlanDate(DateUtil.toTimestamp(paramMap[i].getString("firstPlanDate"), "yyyy-MM-dd HH:mm:ss"));
				orderdt.setOutPlanDate(DateUtil.toTimestamp(paramMap[i].getString("outPlanDate"), "yyyy-MM-dd HH:mm:ss"));
				orderdt.setStockFlag(paramMap[i].getString("setStockFlag"));
				orderdt.setStockKey(paramMap[i].getString("stockKey"));
				orderdt.setDelyHopeDate(DateUtil.toTimestamp(paramMap[i].getString("delyHopeDate"), "yyyy-MM-dd HH:mm:ss"));
				
				orderdt.setPreoutGb("00");			
				orderdt.setGoodsSelectNo(paramMap[i].getString("goodsSelectNo"));
				orderdt.setSingleDueGb("00");
							
				orderdt.setPackYn("0");
				orderdt.setAnonyYn("0");
				orderdt.setMsg(paramMap[i].getString("msg"));
				orderdt.setMsgNote("");
				orderdt.setHappyCardYn("0");
				orderdt.setSaveamt(0);
				orderdt.setSaveamtGb("90");
				orderdt.setPromoNo1("");
				orderdt.setPromoNo2("");
				orderdt.setSetGoodsCode("");
				orderdt.setReceiptYn("0");
				orderdt.setSlipYn("1");
				orderdt.setEntpslipNo("");
				orderdt.setDirectYn(paramMap[i].getString("directShipYn"));
				orderdt.setLastProcId(user_id);
				orderdt.setLastProcDate(sysDateTime);
				orderdt.setReservDelyYn("0");
				
				orderdt.setEntpCode(paramMap[i].getString("entpCode"));
				orderdt.setEntpCodeOrg(paramMap[i].getString("entpCodeOrg"));
				orderdt.setNorestAllotMonths(paramMap[i].getString("norestAllotMonths"));
				orderdt.setRsalePaAmt(paramMap[i].getDouble("rsaleAmt")); 
				//= 제휴할인 가격
				orderdt.setRemark3N(paramMap[i].getLong("sellerDcAmt"));
				//orderdt.setRemark4N(paramMap[i].getLong("rsaleAmt"));
				log.info("//----Remark4N <-> Rsale Swap ");
				orderdt.setRemark4N((long)(paramMap[i].getDouble("supplyPrice")) * orderdt.getOrderQty());
				
				
				orderdt.setOwnProcCost(paramMap[i].getDouble("arsOwnDcAmt"));
				orderdt.setEntpProcCost(paramMap[i].getDouble("arsEntpDcAmt"));
				
				orderdt.setArsDcAmt(paramMap[i].getLong("arsDcAmt"));
				orderdt.setLumpSumDcAmt(paramMap[i].getLong("lumpSumDcAmt"));
				orderdt.setLumpSumOwnDcAmt(paramMap[i].getDouble("lumpSumOwnDcAmt"));
				orderdt.setLumpSumEntpDcAmt(paramMap[i].getDouble("lumpSumEntpDcAmt"));
				orderdt.setInstantCouponDiscount(paramMap[i].getDouble("instantCouponDiscount"));
				if(paramMap[i].get("orderPromo") != null){
					orderdt.setOrderPromo  ((OrderpromoVO)paramMap[i].get("orderPromo"));  //TODO null check
				}
				orderdt.setReceiverPost(paramMap[i].getString("postNo"));
				
				//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
				if(paramMap[i].get("orderPaPromo") != null){
					orderdt.setOrderPaPromo((OrderpromoVO)paramMap[i].get("orderPaPromo"));  //제휴OUT프로모션 : 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
				}
				//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
				
				RsaleAmt += orderdt.getRsaleAmt();
				
				paramMap[i].put("orderGSeq", orderdt.getOrderGSeq());
				paramMap[i].put("orderDSeq", orderdt.getOrderDSeq());
				paramMap[i].put("orderWSeq", orderdt.getOrderWSeq());
				
				
				orderdtList.add(orderdt);
				
				//= 사은품 프로모션 정보 조회
				//HashMap<String, Object> giftPromoMap = null;
				List<HashMap<String, Object>> giftPromoList = paorderDAO.selectGiftPromo(paramMap[i]);

				for(HashMap<String, Object> giftPromoMap : giftPromoList) {
					goodsSelectNo = "";
					if(giftPromoMap.get("LIMIT_YN").toString().equals("1")){
						goodsSelectNo = selectSequenceNo("GOODS_SELECT_NO");
						
						promoCounsel = new PromocounselVO();
						promoCounsel.setPromoNo			(giftPromoMap.get("PROMO_NO").toString());
						promoCounsel.setOrderNo			(order_no);
						promoCounsel.setOrderGSeq		(orderdt.getOrderGSeq());
						promoCounsel.setGoodsSelectNo	(goodsSelectNo);
						promoCounsel.setLimitQty		(Long.parseLong(giftPromoMap.get("LIMIT_QTY").toString()));
						promoCounsel.setCounselQty		(orderdt.getOrderQty());
						promoCounsel.setInsertId		(user_id);
						promoCounsel.setInsertDate		(sysDateTime);
						
						l_temp = promoCounsel.getCounselQty();
						long selfOrder = paorderDAO.retrieveSumCouncelQty(promoCounsel);
						
						if (promoCounsel.getLimitQty() < selfOrder + promoCounsel.getCounselQty()) {
							// 프로모션 한정수량이 초과된 경우 
							l_temp = 0;
						}
						
						if (l_temp > 0) {
							promoCounsel.setCounselQty(l_temp);
							executedRtn = paorderDAO.insertPromoCounsel(promoCounsel); // 전량 접수 가능
							if (executedRtn != 1) {
								throw processException("msg.cannot_save", new String[] { "TPROMOCOUNSEL INSERT" });
							}
						}
					}//END OF LIMIT_YN
					
					if(giftPromoMap.get("LIMIT_YN").toString().equals("1") && l_temp == 0){
						break;//= 한정수량을 초과한 경우 사은품을 증정하지 않음.
					}
					
					//프로모션 상품 단품 선택.
					HashMap<String, Object> giftPromoDtMap= new HashMap<String, Object>();
					
					giftPromoDtMap.put("promoNo", giftPromoMap.get("PROMO_NO").toString());
					giftPromoDtMap.put("orderQty", orderdt.getOrderQty());
					
					List<Object> giftPromoDtList = paorderDAO.selectGiftPromoDt(giftPromoDtMap);
					if(giftPromoDtList.size() < 1) break; //사은품이 품절된경우 지급하지 않는다.
					
					OrderInfoMsg[] giftInfo = null;
					ParamMap[] giftStockMap = null;
					ParamMap giftMap 		= null;
					
					//= 선택된 단품의 사은품재고가 있을 경우 TORDERDT data setting.
					for (int j = 0; giftPromoDtList.size() > j; j++){
						orderDSeq++; //= d_seq 증가.
						giftMap = new ParamMap();
						giftMap.setParamMap((HashMap<String, Object>) giftPromoDtList.get(j));
						giftMap.replaceCamel();
						giftMap.put("applyDate", paramMap[i].get("orderDate")); //= 사은품은 제휴사 주문발생 시간 기준으로 정보조회
						
						giftInfo = new OrderInfoMsg[1];
						giftStockMap = new ParamMap[1];
						
						giftInfo[0] = paorderDAO.selectGoodsInfo(giftMap); //= 사은품 상세정보 조회
						
						giftStockMap[0] = new ParamMap();
						giftStockMap[0].setParamMap((HashMap<String, Object>)paramMap[i].get());
						giftStockMap[0].put("seq", orderDSeq);
						
						giftStockMap = calStock(giftStockMap, giftInfo); // 재고 check
						if(giftStockMap == null || giftStockMap.length == 0) continue; //= 사은품 재고부족일 경우 사은품 데이터를 생성하지 않음.
						
						orderdt = new OrderdtVO();
						orderdt.setOrderNo(order_no);
						orderdt.setOrderGSeq(ComUtil.increaseLpad(Integer.toString(i) , 3, "0"));
						orderdt.setOrderDSeq(ComUtil.increaseLpad(Integer.toString(orderDSeq) , 3, "0"));
						orderdt.setOrderWSeq("001");
						orderdt.setCustNo(cust_no);
						orderdt.setOrderDate(orderDate);
						orderdt.setMdCode(giftInfo[0].getMdCode());
						orderdt.setReceiverSeq(ComUtil.lpad(paramMap[i].getString("createCnt"), 10, "0"));
						orderdt.setOrderGb("10");
						orderdt.setDoFlag("20");
						orderdt.setGoodsGb("30"); //= 사은품
						orderdt.setMediaGb("03");
						orderdt.setMediaCode(paramMap[i].getString("mediaCode"));
						orderdt.setGoodsCode(giftInfo[0].getGoodsCode());
						orderdt.setGoodsdtCode(giftInfo[0].getGoodsDtCode());
						orderdt.setGoodsdtInfo(giftInfo[0].getGoodsDtInfo());
						
						orderdt.setSalePrice(0);
						orderdt.setBuyPrice(giftInfo[0].getBuyPrice());
						orderdt.setOrderQty(giftStockMap[0].getLong("orderQty"));
						orderdt.setOrderAmt(0);
						
						orderdt.setDcAmtMemb(0);
						orderdt.setDcAmtDiv(0);
						orderdt.setDcAmtCard(0);
						
						orderdt.setDcAmtGoods(0);
						orderdt.setDcRateGoods(0);
						orderdt.setDcRate(0);
						orderdt.setDcAmt(0);
						
						orderdt.setRsaleAmt(0);
						
						orderdt.setSyscancel(0);
						orderdt.setSyslast(orderdt.getOrderQty());
						orderdt.setSyslastDcGoods(0);
						orderdt.setSyslastDcMemb(0);
						orderdt.setSyslastDcDiv(0);
						orderdt.setSyslastDcCard(0);
						orderdt.setSyslastDc(0);
						
						orderdt.setSyslastAmt(0);
						
						orderdt.setWhCode(giftInfo[0].getWhCode());
						orderdt.setSaleYn("0");
						orderdt.setDelyType(giftInfo[0].getDelyType());
						orderdt.setDelyGb("10");
						orderdt.setCustDelyFlag("0");
						
						orderdt.setFirstPlanDate(DateUtil.toTimestamp(giftStockMap[0].getString("firstPlanDate"), "yyyy-MM-dd HH:mm:ss"));
						orderdt.setOutPlanDate(DateUtil.toTimestamp(giftStockMap[0].getString("outPlanDate"), "yyyy-MM-dd HH:mm:ss"));
						orderdt.setStockFlag(giftStockMap[0].getString("setStockFlag"));
						orderdt.setStockKey(giftStockMap[0].getString("stockKey"));
						orderdt.setDelyHopeDate(DateUtil.toTimestamp(giftStockMap[0].getString("delyHopeDate"), "yyyy-MM-dd HH:mm:ss"));
						
						orderdt.setPreoutGb("00");
						orderdt.setGoodsSelectNo(giftStockMap[0].getString("goodsSelectNo"));
						orderdt.setSingleDueGb("00");
						
						orderdt.setPackYn("0");
						orderdt.setAnonyYn("0");
						orderdt.setMsg(paramMap[i].getString("msg"));
						orderdt.setMsgNote("");
						orderdt.setHappyCardYn("0");
						orderdt.setSaveamt(0);
						orderdt.setSaveamtGb("90");
						orderdt.setPromoNo1("");
						orderdt.setPromoNo2("");
						orderdt.setSetGoodsCode("");
						orderdt.setReceiptYn("0");
						orderdt.setSlipYn("1");
						orderdt.setEntpslipNo("");
						orderdt.setDirectYn(giftInfo[0].getDirectShipYn());
						orderdt.setLastProcId(user_id);
						orderdt.setLastProcDate(sysDateTime);
						orderdt.setReservDelyYn("0");
						orderdt.setRsalePaAmt(0);
						orderdt.setRemark3N(0);
						orderdt.setEntpCode("0");
						orderdt.setEntpCodeOrg("0");
						
						orderdt.setPriceSeq(giftInfo[0].getPriceSeq());
						
						orderdtList.add(orderdt);
					}
					orderpromo = new Orderpromo();
					
					orderpromo.setSeq(selectSequenceNo("ORDERPROMO_SEQ"));
					orderpromo.setPromoNo(giftPromoMap.get("PROMO_NO").toString());
					orderpromo.setDoType("10");
					orderpromo.setOrderNo(order_no);
					orderpromo.setOrderGSeq(orderdt.getOrderGSeq());
					orderpromo.setProcAmt(0);
					orderpromo.setCancelAmt(0);
					orderpromo.setClaimAmt(0);
					orderpromo.setProcCost(0);
					orderpromo.setOwnProcCost(0);
					orderpromo.setEntpProcCost(0);
					orderpromo.setEntpCode(paramMap[i].getString("entpCode"));
					orderpromo.setCancelYn("0");
					orderpromo.setCancelDate(null);
					orderpromo.setCancelId("");
					orderpromo.setRemark("");
					orderpromo.setInsertId(user_id);
					orderpromo.setInsertDate(sysDateTime);
					
					orderpromoList.add(orderpromo);
				}
				
				
			}
			
			//insert TCUSTOMER, TCUSTSYSTEM, TRECEIVER
			saveCustomer(customer, custsystem, custtelList, receiverList);
			
			//2018-04-25 백호선
			HashMap<String, Object> obj = new HashMap<>();					
			//obj= calcShipCost(orderdtList, receiverList, cust_no, order_no, user_id, sysDateTime);
			obj = calcShipCostN(orderdtList, receiverList);
			
			ordershipcostList = (List<Ordershipcost>) obj.get("ordershipcostList");
			ordercostapplyList = (List<Ordercostapply>) obj.get("ordercostapplyList");	
			long shipFeeAmt = calcFeeAmt(ordershipcostList);
			
	
			//orderGoods 세팅
			log.info("//----TORDERGOODS select");
			List<Ordergoods> ordergoodsList = new ArrayList<Ordergoods>();
			
			int orderdtListSize = orderdtList.size(); 
			
			for (int i = 0; i < orderdtListSize; i++) {
				orderdt = new OrderdtVO();
				orderdt = orderdtList.get(i);
				
				if(orderdt.getOrderDSeq().equals("001")){ //= 본품
					ordergoods = new Ordergoods();
					ordergoods.setOrderNo(order_no);
					ordergoods.setOrderGSeq(orderdt.getOrderGSeq());
					ordergoods.setCustNo(cust_no);
					ordergoods.setOrderDate(orderDate);
					ordergoods.setOrderGb("10");
					ordergoods.setPromoNo("");
					ordergoods.setSetYn("0");
					ordergoods.setGoodsCode(orderdt.getGoodsCode());
					ordergoods.setOrderQty(orderdt.getOrderQty());
					ordergoods.setCancelQty(0);
					ordergoods.setClaimQty(0);
					ordergoods.setReturnQty(0);
					ordergoods.setClaimCanQty(0);
					ordergoods.setExchCnt(0);
					ordergoods.setAsCnt(0);
					ordergoods.setSalePrice(orderdt.getSalePrice());
					ordergoods.setDcRate(orderdt.getDcRate());
					ordergoods.setDcAmtGoods(orderdt.getDcAmtGoods());
					ordergoods.setDcAmtMemb(orderdt.getDcAmtMemb());
					ordergoods.setDcAmtDiv(orderdt.getDcAmtDiv());
					ordergoods.setDcAmtCard(orderdt.getDcAmtCard());
					ordergoods.setDcAmt(orderdt.getDcAmt());
					ordergoods.setNorestAllotMonths(orderdt.getNorestAllotMonths());
					ordergoods.setCounseltel("");
					
					ordergoodsList.add(ordergoods);
				}
			}		
			
			//orderpromo 세팅
			log.info("//----TORDERPROMO select");
			for (int i = 0; i < orderdtListSize; i++) {				
				long arsDcAmt = 0;
				long LumpSumDcAmt = 0;
				long promotionDiscountPrice = 0;
				long paPromotionDiscountPrice = 0;//제휴OUT프로모션 할인액 : 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
				double paInstantCouponDiscount = 0;
				String procGb = "";
				
				orderdt = new OrderdtVO();
				orderdt = orderdtList.get(i);
				
				if(orderdt.getOrderDSeq().equals("001")){ //= 본품
					
					arsDcAmt 	 = orderdt.getArsDcAmt() * orderdt.getOrderQty();
					LumpSumDcAmt = orderdt.getLumpSumDcAmt();
					paInstantCouponDiscount = orderdt.getInstantCouponDiscount();				
					if(orderdt.getOrderPromo() != null){
						procGb = orderdt.getOrderPromo().getProcGb();					
						if(procGb.equals("D")){
							promotionDiscountPrice = 0;
						}else{
							promotionDiscountPrice  = (long) orderdt.getOrderPromo().getProcCost(); //개당 금액						
						}					
					}
					
					//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
					//제휴OUT프로모션 할인액
					if(orderdt.getOrderPaPromo() != null){
						procGb = orderdt.getOrderPaPromo().getProcGb();					
						if(procGb.equals("D")){
							paPromotionDiscountPrice = 0;
						}else{
							paPromotionDiscountPrice = (long) orderdt.getOrderPaPromo().getProcCost(); //개당 금액						
						}					
					}
					//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
					
					if(orderdt.getRemark3N() == 0){ //= 판매자 할인금액이 없는 경우.
						if( orderdt.getDcAmt() > 0) {
							
							//= 제휴할인금액(do_type : 92) setting
							orderpromo = new Orderpromo();
							orderpromo.setSeq(selectSequenceNo("ORDERPROMO_SEQ"));
							
							orderpromo.setPromoNo(paorderDAO.selectPromoNo("92"));
							orderpromo.setDoType("92");
							
							//orderpromo.setPromoNo(paorderDAO.selectPromoNo("91"));
							//orderpromo.setDoType("91");
							
							orderpromo.setOrderNo(orderdt.getOrderNo());
							orderpromo.setOrderGSeq(orderdt.getOrderGSeq());
							orderpromo.setProcAmt(orderdt.getDcAmt());
							orderpromo.setCancelAmt(0);
							orderpromo.setClaimAmt(0);
							orderpromo.setProcCost(orderdt.getDcAmt() / orderdt.getOrderQty());
							orderpromo.setOwnProcCost(orderdt.getDcAmt() / orderdt.getOrderQty());
							orderpromo.setEntpProcCost(0);
							orderpromo.setEntpCode(orderdt.getEntpCode());
							orderpromo.setCancelYn("0");
							orderpromo.setCancelDate(null);
							orderpromo.setCancelId("");
							orderpromo.setRemark("");
							orderpromo.setInsertId(user_id);
							orderpromo.setInsertDate(sysDateTime);
							
							orderpromoList.add(orderpromo);
						}
					}else{
						if(arsDcAmt > 0){ //= ARS할인내역 setting 
							//= ARS 할인 (do_type : 90) setting
							orderpromo = new Orderpromo();
							orderpromo.setSeq			(selectSequenceNo("ORDERPROMO_SEQ"));
							orderpromo.setPromoNo		(paorderDAO.selectPromoNo("90"));
							orderpromo.setDoType		("90");
							orderpromo.setOrderNo		(order_no);
							orderpromo.setOrderGSeq		(orderdt.getOrderGSeq());
							orderpromo.setProcAmt		(arsDcAmt);
							orderpromo.setCancelAmt		(0);
							orderpromo.setClaimAmt		(0);
							orderpromo.setProcCost		(arsDcAmt / orderdt.getOrderQty());
							orderpromo.setOwnProcCost	(orderdt.getOwnProcCost());
							orderpromo.setEntpProcCost	(orderdt.getEntpProcCost());
							orderpromo.setEntpCode		(orderdt.getEntpCode());
							orderpromo.setCancelYn		("0");
							orderpromo.setCancelDate	(null);
							orderpromo.setCancelId		("");
							orderpromo.setRemark		("");
							orderpromo.setInsertId		(user_id);
							orderpromo.setInsertDate	(sysDateTime);
							orderpromoList.add(orderpromo);
						}
						
						if(LumpSumDcAmt > 0){ //= 일시불 할인내역 setting 
							//= 일시불 할인 (do_type : 70) setting
							orderpromo = new Orderpromo();
							orderpromo.setSeq			(selectSequenceNo("ORDERPROMO_SEQ"));
							orderpromo.setPromoNo		(paorderDAO.selectLumpPromoNo(orderdt));
							orderpromo.setDoType		("70");
							orderpromo.setOrderNo		(orderdt.getOrderNo());
							orderpromo.setOrderGSeq		(orderdt.getOrderGSeq());
							orderpromo.setProcAmt		(LumpSumDcAmt);
							orderpromo.setCancelAmt		(0);
							orderpromo.setClaimAmt		(0);
							orderpromo.setProcCost		(LumpSumDcAmt / orderdt.getOrderQty());
							orderpromo.setOwnProcCost	(orderdt.getLumpSumOwnDcAmt());
							orderpromo.setEntpProcCost	(orderdt.getLumpSumEntpDcAmt());
							orderpromo.setEntpCode		(orderdt.getEntpCode());
							orderpromo.setCancelYn		("0");
							orderpromo.setCancelDate	(null);
							orderpromo.setCancelId		("");
							orderpromo.setRemark		("");
							orderpromo.setInsertId		(user_id);
							orderpromo.setInsertDate	(sysDateTime);
							orderpromoList.add(orderpromo);				
							
						}
						
						if(promotionDiscountPrice > 0){
							orderpromo = new Orderpromo();
							orderpromo.setSeq			(selectSequenceNo("ORDERPROMO_SEQ"));
							orderpromo.setPromoNo		(orderdt.getOrderPromo().getPromoNo());
							orderpromo.setDoType		(orderdt.getOrderPromo().getDoType());
							orderpromo.setOrderNo		(orderdt.getOrderNo());
							orderpromo.setOrderGSeq		(orderdt.getOrderGSeq());
							orderpromo.setProcAmt		(promotionDiscountPrice * orderdt.getOrderQty());
							orderpromo.setCancelAmt		(0);
							orderpromo.setClaimAmt		(0);
							orderpromo.setProcCost		(promotionDiscountPrice);
							orderpromo.setOwnProcCost	(orderdt.getOrderPromo().getOwnProcCost() );
							orderpromo.setEntpProcCost	(orderdt.getOrderPromo().getEntpProcCost());
							orderpromo.setEntpCode		(orderdt.getEntpCode());
							orderpromo.setCancelYn		("0");
							orderpromo.setCancelDate	(null);
							orderpromo.setCancelId		("");
							orderpromo.setRemark		("");
							orderpromo.setInsertId		(user_id);
							orderpromo.setInsertDate	(sysDateTime);
							orderpromoList.add(orderpromo);
							
							//For CouponIssue
							orderdt.getOrderPromo().setOrderNo    (orderdt.getOrderNo());
							orderdt.getOrderPromo().setCustNo     (orderdt.getCustNo());
							orderdt.getOrderPromo().setInsertDate (orderdt.getLastProcDate());
							orderdt.getOrderPromo().setInsertId	  (orderdt.getLastProcId());
							if("1".equals(orderdt.getOrderPromo().getCouponYn())){
								newCouponissue(orderdt.getOrderPromo(), couponissueList);						
							}
						}			
						
						//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
						if(paPromotionDiscountPrice > 0){
							orderpromo = new Orderpromo();
							orderpromo.setSeq			(selectSequenceNo("ORDERPROMO_SEQ"));
							orderpromo.setPromoNo		(orderdt.getOrderPaPromo().getPromoNo());
							orderpromo.setDoType		(orderdt.getOrderPaPromo().getDoType());
							orderpromo.setOrderNo		(orderdt.getOrderNo());
							orderpromo.setOrderGSeq		(orderdt.getOrderGSeq());
							orderpromo.setProcAmt		(paPromotionDiscountPrice * orderdt.getOrderQty());
							orderpromo.setCancelAmt		(0);
							orderpromo.setClaimAmt		(0);
							orderpromo.setProcCost		(paPromotionDiscountPrice);
							orderpromo.setOwnProcCost	(orderdt.getOrderPaPromo().getOwnProcCost());
							orderpromo.setEntpProcCost	(orderdt.getOrderPaPromo().getEntpProcCost());
							orderpromo.setEntpCode		(orderdt.getEntpCode());
							orderpromo.setCancelYn		("0");
							orderpromo.setCancelDate	(null);
							orderpromo.setCancelId		("");
							orderpromo.setRemark		("");
							orderpromo.setInsertId		(user_id);
							orderpromo.setInsertDate	(sysDateTime);
							orderpromoList.add(orderpromo);
							
							//For CouponIssue
							orderdt.getOrderPaPromo().setOrderNo    (orderdt.getOrderNo());
							orderdt.getOrderPaPromo().setCustNo     (orderdt.getCustNo());
							orderdt.getOrderPaPromo().setInsertDate (orderdt.getLastProcDate());
							orderdt.getOrderPaPromo().setInsertId	(orderdt.getLastProcId());
							if("1".equals(orderdt.getOrderPaPromo().getCouponYn())){
								newCouponissue(orderdt.getOrderPaPromo(), couponissueList);
							}
						}
						//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
						
						if(paInstantCouponDiscount > 0) {
							//= 즉시지급쿠폰 할인 (do_type : 30) setting
							orderpromo = new Orderpromo();
							orderpromo.setSeq			(selectSequenceNo("ORDERPROMO_SEQ"));
							orderpromo.setPromoNo		("299912369999");
							orderpromo.setDoType		("30");
							orderpromo.setOrderNo		(order_no);
							orderpromo.setOrderGSeq		(orderdt.getOrderGSeq());
							orderpromo.setProcAmt		(paInstantCouponDiscount);
							orderpromo.setCancelAmt		(0);
							orderpromo.setClaimAmt		(0);
							orderpromo.setProcCost		(ComUtil.modAmt(paInstantCouponDiscount / orderdt.getOrderQty(), 4, 2));
							orderpromo.setOwnProcCost	(ComUtil.modAmt(paInstantCouponDiscount / orderdt.getOrderQty(), 4, 2));
							orderpromo.setEntpProcCost	(0);
							orderpromo.setEntpCode		(orderdt.getEntpCode());
							orderpromo.setCancelYn		("0");
							orderpromo.setCancelDate	(null);
							orderpromo.setCancelId		("");
							orderpromo.setRemark		("");
							orderpromo.setInsertId		(user_id);
							orderpromo.setInsertDate	(sysDateTime);
							orderpromoList.add(orderpromo);
						}
						
						if( orderdt.getDcAmt() - (promotionDiscountPrice * orderdt.getOrderQty()) - (paPromotionDiscountPrice * orderdt.getOrderQty()) - arsDcAmt - LumpSumDcAmt - paInstantCouponDiscount > 0) {//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
							
							//= 제휴할인금액(do_type : 92) setting
							orderpromo = new Orderpromo();
							orderpromo.setSeq(selectSequenceNo("ORDERPROMO_SEQ"));
							
							orderpromo.setPromoNo(paorderDAO.selectPromoNo("92"));
							orderpromo.setDoType("92");
							
							//orderpromo.setPromoNo(paorderDAO.selectPromoNo("91"));
							//orderpromo.setDoType("91");
							
							orderpromo.setOrderNo(order_no);
							orderpromo.setOrderGSeq(orderdt.getOrderGSeq());
							orderpromo.setProcAmt(orderdt.getDcAmt() - (promotionDiscountPrice * orderdt.getOrderQty()) - (paPromotionDiscountPrice * orderdt.getOrderQty()) - arsDcAmt - LumpSumDcAmt - paInstantCouponDiscount);//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
							orderpromo.setCancelAmt(0);
							orderpromo.setClaimAmt(0);
							orderpromo.setProcCost((orderdt.getDcAmt() - (promotionDiscountPrice * orderdt.getOrderQty()) - (paPromotionDiscountPrice * orderdt.getOrderQty()) - arsDcAmt - LumpSumDcAmt - paInstantCouponDiscount) / orderdt.getOrderQty());//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
							orderpromo.setOwnProcCost((orderdt.getDcAmt() - (promotionDiscountPrice * orderdt.getOrderQty()) - (paPromotionDiscountPrice * orderdt.getOrderQty()) - arsDcAmt - LumpSumDcAmt - paInstantCouponDiscount) / orderdt.getOrderQty());//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
							orderpromo.setEntpProcCost(0);
							orderpromo.setEntpCode(orderdt.getEntpCode());
							orderpromo.setCancelYn("0");
							orderpromo.setCancelDate(null);
							orderpromo.setCancelId("");
							orderpromo.setRemark("");
							orderpromo.setInsertId(user_id);
							orderpromo.setInsertDate(sysDateTime);
							
							orderpromoList.add(orderpromo);
						}
						
					}
				}
			}		
			
			//orderreceipt 세팅
			log.info("//----TORDERRECEIPTS select");
			receipt_no = selectSequenceNo("RECEIPT_NO");
			
			orderreceipts = new Orderreceipts();
			
			orderreceipts.setBlank();
			orderreceipts.setReceiptNo(receipt_no);
			orderreceipts.setOrderNo(order_no);
			orderreceipts.setCustNo(cust_no);
			orderreceipts.setDoFlag("90");
			orderreceipts.setSettleGb("11");  //외상매출  , 결제구분 G0001 
			orderreceipts.setCardBankCode("");
			orderreceipts.setBankSeq("");
			orderreceipts.setCardName("");
			orderreceipts.setCardNo("");
			orderreceipts.setCvv("");		
			orderreceipts.setDepositor(paramMap[0].getString("custName"));
			orderreceipts.setValidDate("");
			
			orderreceipts.setQuestAmt(RsaleAmt);
			
			orderreceipts.setReceiptPlanDate(null);
			orderreceipts.setOkNo("");
			orderreceipts.setOkDate(sysDateTime);
			orderreceipts.setOkMed("000");
			orderreceipts.setOkErrorCode("0000");
			orderreceipts.setVanComp("");
			orderreceipts.setPayMonth(0);
			orderreceipts.setNorestYn("0");
			orderreceipts.setNorestRate(0);
			orderreceipts.setNorestAmt(0);
			orderreceipts.setReceiptAmt(orderreceipts.getQuestAmt());
			orderreceipts.setReceiptDate(sysDateTime);
			orderreceipts.setEndYn("1");		
			orderreceipts.setRepayPbAmt(orderreceipts.getQuestAmt());		
			orderreceipts.setCancelYn("0");
			orderreceipts.setCancelCode("000");
			orderreceipts.setCancelDate(null);
			orderreceipts.setCancelId("");
			orderreceipts.setSaveamtUseFlag("0");
			orderreceipts.setCodDelyYn("0");
			orderreceipts.setDivideYn("0");
			orderreceipts.setPartialCancelYn("");
			orderreceipts.setProtxVendortxcode("");
			orderreceipts.setProtxStatus("");
			orderreceipts.setProtxStatusdetail("");
			orderreceipts.setProtxVpstxid("");
			orderreceipts.setProtxSecuritykey("");
			orderreceipts.setProtxTxauthno("");
			orderreceipts.setIssueNumber("");
			orderreceipts.setPayboxIdentifiant("");
			orderreceipts.setPayboxCodetraitement("");
			orderreceipts.setPayboxPays("");
			orderreceipts.setPayboxDateq("");
			orderreceipts.setPayboxReference("");
			orderreceipts.setCardPassword("");
			orderreceipts.setRemark1V("");
			orderreceipts.setRemark2V("");
			orderreceipts.setRemark3N(0);
			orderreceipts.setRemark4N(0);
			orderreceipts.setRemark5D(null);
			orderreceipts.setRemark6D(null);
			orderreceipts.setRemark("");
			orderreceipts.setProcId(user_id);
			orderreceipts.setProcDate(sysDateTime);
			orderreceipts.setInsertId(user_id);
			orderreceipts.setInsertDate(sysDateTime);
			
			orderreceiptsList.add(orderreceipts);
			
			if(shipFeeAmt > 0) { //= 주문배송비가 존재 할 경우, 정산배송비 데이터 생성.
				orderreceipts = new Orderreceipts();
				receipt_no = selectSequenceNo("RECEIPT_NO");
				
				orderreceipts.setBlank();
				orderreceipts.setReceiptNo(receipt_no);
				orderreceipts.setOrderNo(order_no);
				orderreceipts.setCustNo(cust_no);
				orderreceipts.setDoFlag("90");
				orderreceipts.setSettleGb("15");//배송비 결제구분 G0001 //신세계는 14였는데 사은품 배송비는 15로 다시 땀
				orderreceipts.setCardBankCode("");
				orderreceipts.setBankSeq("");
				orderreceipts.setCardName("");
				orderreceipts.setCardNo("");
				orderreceipts.setCvv("");		
				orderreceipts.setDepositor(paramMap[0].getString("custName"));
				orderreceipts.setValidDate("");
				
				orderreceipts.setQuestAmt(shipFeeAmt);
				
				orderreceipts.setReceiptPlanDate(null);
				orderreceipts.setOkNo("");
				orderreceipts.setOkDate(sysDateTime);
				orderreceipts.setOkMed("000");
				orderreceipts.setOkErrorCode("0000");
				orderreceipts.setVanComp("");
				orderreceipts.setPayMonth(0);
				orderreceipts.setNorestYn("0");
				orderreceipts.setNorestRate(0);
				orderreceipts.setNorestAmt(0);
				orderreceipts.setReceiptAmt(orderreceipts.getQuestAmt());
				orderreceipts.setReceiptDate(sysDateTime);
				orderreceipts.setEndYn("1");		
				orderreceipts.setRepayPbAmt(orderreceipts.getQuestAmt());		
				orderreceipts.setCancelYn("0");
				orderreceipts.setCancelCode("000");
				orderreceipts.setCancelDate(null);
				orderreceipts.setCancelId("");
				orderreceipts.setSaveamtUseFlag("0");
				orderreceipts.setCodDelyYn("0");
				orderreceipts.setDivideYn("0");
				orderreceipts.setPartialCancelYn("");
				orderreceipts.setProtxVendortxcode("");
				orderreceipts.setProtxStatus("");
				orderreceipts.setProtxStatusdetail("");
				orderreceipts.setProtxVpstxid("");
				orderreceipts.setProtxSecuritykey("");
				orderreceipts.setProtxTxauthno("");
				orderreceipts.setIssueNumber("");
				orderreceipts.setPayboxIdentifiant("");
				orderreceipts.setPayboxCodetraitement("");
				orderreceipts.setPayboxPays("");
				orderreceipts.setPayboxDateq("");
				orderreceipts.setPayboxReference("");
				orderreceipts.setCardPassword("");
				orderreceipts.setRemark1V("");
				orderreceipts.setRemark2V("");
				orderreceipts.setRemark3N(0);
				orderreceipts.setRemark4N(0);
				orderreceipts.setRemark5D(null);
				orderreceipts.setRemark6D(null);
				orderreceipts.setRemark("");
				orderreceipts.setProcId(user_id);
				orderreceipts.setProcDate(sysDateTime);
				orderreceipts.setInsertId(user_id);
				orderreceipts.setInsertDate(sysDateTime);
				
				orderreceiptsList.add(orderreceipts);
				
				//배송비 비교를 위한 Section - 실제로 계산한 배송비 vs 11번가로 부터 입금받은 배송비
				paordershipcost = new PaOrderShipCost();
				paordershipcost.setOrderNo(order_no);
				paordershipcost.setSeq("001");
				paordershipcost.setOutReceiverSeq(ComUtil.lpad(Long.toString(receiverSeq), 10, "0"));
				paordershipcost.setReturnReceiverSeq("");
				paordershipcost.setCustNo(cust_no);
				paordershipcost.setOrderDate(orderDate);
				paordershipcost.setReceiptNo(receipt_no);
				paordershipcost.setType("10");
				paordershipcost.setPaShpFeeAmt(paShipFeeAmt);
				paordershipcost.setShpFeeAmt(shipFeeAmt);
				paordershipcost.setManualCancelAmt(0);
				paordershipcost.setShpFeeYn(1);         //0무상 1유상 2 협의
				paordershipcost.setInsertId(user_id);
				paordershipcost.setInsertDate(sysDateTime);
				
			}else if(shipFeeAmt <= 0 &&  paShipFeeAmt > 0 ){  //고민이 필요한 이상한 케이스..   
				//else의 경우 (== paShipFeeAmt, shipFeeAmt 둘다 0원인 경우)는 Receipt던 PaordershipCost던 생성 x
				
				if(isUnRefinedAddress){
					
					paordershipcost = new PaOrderShipCost();
					paordershipcost.setOrderNo(order_no);
					paordershipcost.setSeq("001");
					paordershipcost.setCustNo(cust_no);
					paordershipcost.setOutReceiverSeq(ComUtil.lpad(Long.toString(receiverSeq), 10, "0"));
					paordershipcost.setReturnReceiverSeq("");
					paordershipcost.setOrderDate(orderDate);
					paordershipcost.setReceiptNo(receipt_no);
					paordershipcost.setType("10");
					paordershipcost.setPaShpFeeAmt(paShipFeeAmt);
					paordershipcost.setShpFeeAmt(shipFeeAmt);
					paordershipcost.setManualCancelAmt(0);
					paordershipcost.setShpFeeYn(1);         //0무상 1유상 2 협의
					paordershipcost.setInsertId(user_id);
					paordershipcost.setInsertDate(sysDateTime);
				}
				
				
					
			}
			
			//제주도서산간 불가 상품 주문 인입시 상담 데이터 생성
			checkNoDelyAreaOrder(receiver, orderdtList);
			
			//insert TORDERM
			insertOrderm(orderm);
			
			//insert TORDERGOODS
			insertOrdergoods(ordergoodsList);
			
			//insert TORDERDT
			insertOrderdt(orderdtList);
			
			//insert TORDERSHIPCOST
			insertOrdershipcost(ordershipcostList);
			
			//insert TORDERCOSTAPPLY
			insertOrderCostApply(ordercostapplyList);
			
			//insert TORDERPROMO
			insertOrderpromo(orderpromoList);
			
			//insert TORDERRECEIPTS
			insertOrderreceipts(orderreceiptsList);
			
			//insert TPAORDERSHIPCOST
			insertPaOrderShipCost(paordershipcost);
			
			insertCouponissue(couponissueList);
			
			//update TORDERSTOCK
			updateOrderstock(orderdtList);
			
			rtnValue = getMessage("pa.success_order_input");
		
			
			
			for(int k = 0; paramLength > k; k++){
				resultMap[k] = new HashMap<>();
				resultMap[k].put("MAPPING_SEQ", paramMap[k].getString("mappingSeq"));
				resultMap[k].put("ORDER_NO", paramMap[k].getString("orderNo"));
				
				
				if(paramMap[k].getString("orderPossYn").equals("1")){
					
					
					
					
					resultMap[k].put("ORDER_G_SEQ", 	paramMap[k].getString("orderGSeq"));
					resultMap[k].put("ORDER_D_SEQ", 	paramMap[k].getString("orderDSeq"));
					resultMap[k].put("ORDER_W_SEQ", 	paramMap[k].getString("orderWSeq"));
					resultMap[k].put("RESULT_CODE", 	"000000"); 
					resultMap[k].put("RESULT_MESSAGE", 	rtnValue);	
					
					/*
					
					//1)11번가 - sk스토아간 매입가, 판매가 불일치로 인한 판매 거부처리..
					if(paramMap[k].getString("orderPossYn2").equals("0")){
						
						resultMap[k].put("RESULT_CODE", 	"100002"); 
						resultMap[k].put("RESULT_MESSAGE", 	getMessage("pa.fail_order_input", new String[]{getMessage("pa.different_sale_price")}));
						
						try {
							insertPamonitering(paOrderCode, paramMap[k].getString("paGoodsCode"), paramMap[k].getString("goodsCode")); //TPAMONITERING
						} catch (Exception e) {
							log.info("Error when write PAMONITERING");// 어차피 로그니깐 Insert실패해도 무시하고 진행..
						}
					}
					
					//2) 정상적인 상황
					else{
						resultMap[k].put("ORDER_G_SEQ", 	paramMap[k].getString("orderGSeq"));
						resultMap[k].put("ORDER_D_SEQ", 	paramMap[k].getString("orderDSeq"));
						resultMap[k].put("ORDER_W_SEQ", 	paramMap[k].getString("orderWSeq"));
						resultMap[k].put("RESULT_CODE", 	"000000"); 
						resultMap[k].put("RESULT_MESSAGE", 	rtnValue);	
					}
					*/
				}
				//3) 재고나 판매불가로 인한 거부
				else {
					resultMap[k].put("RESULT_CODE", 	"100001"); 
					resultMap[k].put("RESULT_MESSAGE", 	getMessage("pa.fail_order_input", new String[]{getMessage("pa.out_of_stock")}));
				}
				
			
				
				
			}
			
			return resultMap;
			
		} catch (Exception e) {
			if(e.getMessage() == null){
				rtnValue = getMessage("pa.fail_order_input", new String[]{e.toString()});
			} else {
				rtnValue = getMessage("pa.fail_order_input", new String[]{e.getMessage()});
			}
			throw processMessageException(rtnValue);
		} finally {
			try {
				ParamMap resultParamMap = null;
				resultParamMap = new ParamMap();
				
				for(int l = 0; resultMap.length > l; l++){
					if(resultMap[l] == null) break;
							
					resultParamMap.setParamMap(resultMap[l]);
					resultParamMap.replaceCamel();
					
					if(resultParamMap.getString("resultCode").equals("000000")){
						resultParamMap.put("createYn", "1");
					} else {
						resultParamMap.put("createYn", "0");
					}
					
					executedRtn = paorderDAO.updatePaOrderm(resultParamMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
					}
					
				}
				
				// 개인정보 수집출처 고지 SMS 발송
				sendPaPersonalInfoNotice(paramMap);
				
			} catch (Exception e2) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
		}
		
	}
	
		/* by Beak Hosun
		*현상 :  1) 물건2개 주문 A는 Gseq = 001, B는 Gseq =002  
		         2) Gseq 001 취소 -> 
		            코어에서 CostApply 따면서 CwareAction I , Gseq 001 생성해준다.
		             - ordershipcost 리스트에서 Type 20(취소)짜리 한개 Type10(초도배송비)짜리 한개 생성
			     - costapply리스트 Gseq= 001로 한개 생성
			     -  ordershipcost type 20 For문 돌면서 costapply리스가 첨가되는 형식이라 1*1 로 총 Insert Costapply 1개 생성
			     -  실질적으로 Insert시 PK에러 나지 않음
		            *취소로 인해 발생하는 TYPE 10짜리에 대한 CwareAction U짜리 costapply는 다른 부분에서 따준다.
		
			 3) Gseq 002 취소 ->
			    -orderShipCost는 Type20 2개 생성(초도배송비 취소, Gseq 002 취소), OrderCostApply는 1줄 생성 Gseq 002 
			    -ordershipcost type 20 For문 돌면서 costapply리스가 첨가되는 형식이라 2*1 로 총 Insert Costapply 2개 생성
			    * Gseq 002,  Cware action = Insert, 배송비정책코드가 다른 리스트 2개 생성 
		      	    - Insert시 Gseq,Dseq,Wseq가 같은 2줄이 Insert되기때문에 PK에러 발생   
		      
	  	* 해결책 : 3)에서 취소시 , 초도배송비, 취소배송비는 각각 Gseq와 매핑이 되어야한다. 
			  그리고 Gseq 001 은 CwareAction U로, Gseq 002는 CwareAction I로 두개 따서 처리해줘야한다...
		
		참고 ::3625줄에 costapply 각각 따오는거 만들어야함.  1803줄부터 에 매핑작업(u또는 i)추가해야함*/


	
	/**
	 * 주문취소
	 * @param  CancelInputVO
	 * @return HashMap
	 * @throws Exception
	 */
	public HashMap<String, Object> saveCancel(CancelInputVO cancelInputVO) throws Exception {
		HashMap<String, Object> resultMap = null;
		String rtnValue = "000000";
		String user_id = cancelInputVO.getProcId(); //11번가는 PA11, G마켓은 PAG
		int executedRtn = 0;
		
		// 취소데이터 객체 생성
		OrdergoodsVO ordergoods = null;
		CanceldtVO canceldtVO = null;
		ClaimdtVO claimdt = null;
		OrderstockVO orderstock = null;
		Orderreceipts orderreceipts = null;
		
		Orderpromo orgOrderpromo = null;
		Orderpromo orderpromo = null;
		
		OrderdtVO orgOrderdt = null;
		OrdergoodsVO orgOrdergoods = null;
		OrdershipcostVO[] ordershipcost = null;
		Ordercostapply [] ordercostapply= null;
		
		List<CanceldtVO> cancelList = new ArrayList<CanceldtVO>();
		List<ClaimdtVO>  claimList  = new ArrayList<ClaimdtVO>();
		List<OrdergoodsVO> ordergoodsList = new ArrayList<OrdergoodsVO>();
		List<OrderstockVO> orderstockList = new ArrayList<OrderstockVO>();
		List<OrderdtVO> orgOrderdtList = null;
		List<Orderpromo> orderPromoList = null;
		List<Orderpromo> orgOrderPromoList = null;
		List<CanceldtVO> cancelDtList = new ArrayList<CanceldtVO>();
		ParamMap paramMap = null;
		ParamMap paramMap4ShipCost = null;
		PaOrderShipCost paOrderShipcost = null;
		ArrayList<Orderreceipts> orderreceiptsList = new ArrayList<Orderreceipts>();
		List<OrderdtVO> orgOrderdtList4ShipCost = new ArrayList<OrderdtVO>();
		List<Ordercostapply> costApply4Gift = new ArrayList<Ordercostapply>();
		
		String newOrderWseq      = "";
		String addtionalMessage  = ""; 
		
		String 	  order_no		 = cancelInputVO.getOrderNo();								// ORG_주문번호
		String    receipt_no  	 = selectSequenceNo("RECEIPT_NO");							// 접수번호seq 생성
		String    dateTime       = getSysdatetimeToString();								// 시간생성(String)
		Timestamp sysDateTime    = DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss");	// 시간생성(Timestamp)
		double rsalePaAmt= 0;
		
		try{
			
			try{ // 원주문 건(TORDERDT) 조회
				log.info("//----orderDt select");
				orgOrderdtList = paorderDAO.selectOrderDtOld(cancelInputVO); 
			}catch (Exception e) {
				rtnValue = "select orderdt fail";					    
				throw processMessageException(rtnValue);
			}
			
			try{ // 나중에 배송비를 계산할때 사용하기 위한 Original TORDERDT
				paramMap4ShipCost = new ParamMap();
				paramMap4ShipCost.put("orderNo",cancelInputVO.getOrderNo());
				paramMap4ShipCost.put("orderGSeq",cancelInputVO.getOrderGSeq());
				orgOrderdtList4ShipCost = paorderDAO.selectOrderdt4GSEQ(paramMap4ShipCost);	
					
			}catch(Exception e){
				rtnValue = "select orderdt fail 4 Shipping Fee";					    
				throw processMessageException(rtnValue);
			}
			
	
			int orgOrderdtListSize = orgOrderdtList.size();
			
			if(orgOrderdtListSize == 0){
				rtnValue = "select orderdt null";				    
				throw processMessageException(rtnValue);
			}
			
			// w_seq 생성
			log.info("//----orderDt select");	
			newOrderWseq = paorderDAO.selectWSeq(cancelInputVO);
			
			if(newOrderWseq == null){
				rtnValue = "select w seq null";
				throw processMessageException(rtnValue);
			}
			
			paramMap = new ParamMap();
			
			//tcanceldt 세팅
			log.info("//----TCANCELDT setting");
			for(int i = 0; orgOrderdtListSize > i; i++){
				canceldtVO 		= null;
				claimdt			= null;
				ordergoods 		= null;
				orderreceipts 	= null;
				orderpromo 		= null;
				orderstock 		= null;
				
				orgOrderdt = new OrderdtVO();
				orgOrderdt = orgOrderdtList.get(i);
				
				//= 취소수량 setting (order_d_seq = '001' 의 수량으로 setting)
				if(orgOrderdt.getOrderDSeq().equals("001")){
					if(cancelInputVO.getCancelQty() >  orgOrderdt.getSyslast()){
						rtnValue = getMessage("pa.fail_cancel_input", new String[]{getMessage("pa.out_of_cancel_qty")});
						
						resultMap = new HashMap<>();
						resultMap.put("MAPPING_SEQ", 	cancelInputVO.getMappingSeq());
						resultMap.put("RESULT_CODE", 	"100001"); 
						resultMap.put("RESULT_MESSAGE", rtnValue);
						return resultMap;
					} else {
						paramMap.put("cancelQty"  , cancelInputVO.getCancelQty());
						paramMap.put("orgSyslast" , orgOrderdt.getSyslast());
					}
				}
				
				if(Integer.parseInt(orgOrderdt.getDoFlag()) < 30){ //= 출하지시 이전건은 취소데이터 생성
					canceldtVO = new CanceldtVO();
					
					canceldtVO.setBlank();
					canceldtVO.setOrderNo(order_no);
					canceldtVO.setOrderGSeq(orgOrderdt.getOrderGSeq());
					canceldtVO.setOrderDSeq(orgOrderdt.getOrderDSeq());
					canceldtVO.setOrderWSeq(newOrderWseq);
					canceldtVO.setCustNo(orgOrderdt.getCustNo());
					canceldtVO.setReceiverSeq(orgOrderdt.getReceiverSeq());			
					canceldtVO.setCancelGb("20");			
					canceldtVO.setCancelDate(sysDateTime);
					canceldtVO.setDoFlag(orgOrderdt.getDoFlag());
					
					//2018-05-08
					//canceldtVO.setCancelCode(cancelInputVO.getCancelCode());
					canceldtVO.setCancelCode("999");
					canceldtVO.setCsLgroup(cancelInputVO.getCancelCode().substring(0, 2));
					canceldtVO.setCsMgroup(cancelInputVO.getCancelCode().substring(2, 4));
					canceldtVO.setCsSgroup(cancelInputVO.getCancelCode().substring(4, 6));					
					canceldtVO.setCsLmsCode(cancelInputVO.getCancelCode());
					
					
					
					canceldtVO.setGoodsGb(orgOrderdt.getGoodsGb());
					canceldtVO.setMediaGb(orgOrderdt.getMediaGb());
					canceldtVO.setMediaCode(orgOrderdt.getMediaCode());
					canceldtVO.setGoodsCode(orgOrderdt.getGoodsCode());
					canceldtVO.setGoodsdtCode(orgOrderdt.getGoodsdtCode());
					canceldtVO.setGoodsdtInfo(orgOrderdt.getGoodsdtInfo());
					canceldtVO.setSalePrice(orgOrderdt.getSalePrice());
					canceldtVO.setBuyPrice(orgOrderdt.getBuyPrice());
					canceldtVO.setCancelQty(cancelInputVO.getCancelQty());
					canceldtVO.setCancelAmt(orgOrderdt.getSalePrice() * canceldtVO.getCancelQty());
					canceldtVO.setDcRateGoods(orgOrderdt.getDcRateGoods());
					canceldtVO.setDcRate(orgOrderdt.getDcRate());
					canceldtVO.setDcAmtGoods((orgOrderdt.getDcAmtGoods()/orgOrderdt.getOrderQty())*canceldtVO.getCancelQty());
					canceldtVO.setDcAmtMemb(orgOrderdt.getDcAmtMemb());
					canceldtVO.setDcAmtDiv(orgOrderdt.getDcAmtDiv());
					canceldtVO.setDcAmtCard(orgOrderdt.getDcAmtCard());
					canceldtVO.setDcAmt(canceldtVO.getDcAmtGoods());
					canceldtVO.setRsaleNet((orgOrderdt.getRsaleNet()/orgOrderdt.getOrderQty()) * canceldtVO.getCancelQty());
					canceldtVO.setRsaleAmt((orgOrderdt.getRsaleAmt()/orgOrderdt.getOrderQty()) * canceldtVO.getCancelQty());
					canceldtVO.setRsaleVat(canceldtVO.getRsaleAmt() - canceldtVO.getRsaleNet());
					canceldtVO.setVatRate(orgOrderdt.getVatRate());				
					canceldtVO.setMdCode(orgOrderdt.getMdCode());
					canceldtVO.setSaleYn(orgOrderdt.getSaleYn());
					canceldtVO.setWhCode(orgOrderdt.getWhCode());
					canceldtVO.setDelyType(orgOrderdt.getDelyType());
					canceldtVO.setDelyGb(orgOrderdt.getDelyGb());
					canceldtVO.setDelyHopeDate(orgOrderdt.getDelyHopeDate());
					canceldtVO.setDelyHopeYn(orgOrderdt.getDelyHopeYn());
					canceldtVO.setDelyHopeTime(orgOrderdt.getDelyHopeTime());
					canceldtVO.setPreoutGb(orgOrderdt.getPreoutGb());
					canceldtVO.setSingleDueGb(orgOrderdt.getSingleDueGb());
					canceldtVO.setSaveamt(orgOrderdt.getSaveamt());
					canceldtVO.setSaveamtGb(orgOrderdt.getSaveamtGb());			
					canceldtVO.setLastProcId(user_id);
					canceldtVO.setLastProcDate(sysDateTime);
					canceldtVO.setModifyDate(sysDateTime);
					canceldtVO.setRemark3N((orgOrderdt.getRemark3N() / orgOrderdt.getOrderQty()) * canceldtVO.getCancelQty());
					canceldtVO.setEntpCode(orgOrderdt.getEntpCode());
					
					//= 본품일 경우에만 제휴사 실결제금액 계산
					if(canceldtVO.getOrderDSeq().equals("001")){
						
						//2018.05.02 백호선 Remark4N에 고객이 실제로 결제한 금액이 들어감..  (Remark4N/OdrQty) *CanQty
						//rsalePaAmt = (orgOrderdt.getRemark4N() / orgOrderdt.getOrderQty()) * canceldtVO.getCancelQty();
						rsalePaAmt = paorderDAO.selectRsalePaAmt(canceldtVO.getCancelGb(), canceldtVO.getOrderNo(), canceldtVO.getOrderGSeq(), canceldtVO.getOrderDSeq(), canceldtVO.getCancelQty());
						
						//for Testing					
						//rsalePaAmt = 1000;
						
						if(rsalePaAmt < 0){
							//= 제휴사 실결제금액 계산 실패.
							throw processMessageException(getMessage("pa.failed_to_calculate_rsale_pa_amt"));
						}
					} else {
						rsalePaAmt = 0;
					
						//사은품처리
						
						Ordercostapply orderCostApply = new Ordercostapply();
						orderCostApply.setBlank();
						orderCostApply.setCwareAction("I");								
						orderCostApply.setOrderNo(orgOrderdt.getOrderNo());
						orderCostApply.setOrderGSeq(orgOrderdt.getOrderGSeq());
						orderCostApply.setOrderDSeq(orgOrderdt.getOrderDSeq());
						orderCostApply.setOrderWSeq(newOrderWseq);
						orderCostApply.setApplyCostSeq("000");
						orderCostApply.setShipCostNo(0);
						orderCostApply.setInsertId(user_id);
						orderCostApply.setInsertDate(sysDateTime);
						orderCostApply.setModifyId(user_id);
						orderCostApply.setModifyDate(sysDateTime);
						costApply4Gift.add(orderCostApply);
						

					}
					
					//SKB는 RsalePaAmt라는 컬럼이 없으므로, Remark4N에 넣기로 결의했다.
					canceldtVO.setRsalePaAmt(rsalePaAmt); //= 제휴사 실결제금액
					cancelDtList.add(canceldtVO);
					
					
					
					paramMap.put("orderNo",   orgOrderdt.getOrderNo());
					paramMap.put("orderGSeq", orgOrderdt.getOrderGSeq());
					paramMap.put("orderDSeq", orgOrderdt.getOrderDSeq());
					paramMap.put("orderWSeq", orgOrderdt.getOrderWSeq());
					paramMap.put("newOrderWseq", newOrderWseq);
										
					
					//TORDERSTOCK 세팅
					log.info("//----TORDERSTOCK setting");
					orderstock = new OrderstockVO();
					
					orderstock.setOutPlanQty(canceldtVO.getCancelQty());
					orderstock.setTotSaleQty(canceldtVO.getCancelQty());
					orderstock.setGoodsCode(canceldtVO.getGoodsCode());
					orderstock.setGoodsdtCode(canceldtVO.getGoodsdtCode());
					orderstock.setWhCode(canceldtVO.getWhCode());
					orderstock.setPreoutGb(canceldtVO.getPreoutGb());
					orderstock.setModifyDate(sysDateTime);
					orderstock.setModifyId(user_id);
					
					if(orgOrderdt.getOrderDSeq().equals("001")){ //= 본품일 경우에만
						//tordergoods 세팅			  
						try{ // 주문 torderGoods 조회
							log.info("//----torderGoods select");
							orgOrdergoods = paorderDAO.selectOrderGoods(paramMap);
						}catch (Exception e) {
							rtnValue = "select ordergoods fail";
							throw processMessageException(rtnValue);
						}			
						if(orgOrdergoods == null){
							rtnValue = "select ordergoods fail";
							throw processMessageException(rtnValue);
						}
					
						log.info("//----TORDERGOODS setting");
						ordergoods = new OrdergoodsVO();
						
						ordergoods.setBlank();
						ordergoods.setCancelQty(canceldtVO.getCancelQty());
						ordergoods.setClaimQty(0);
						ordergoods.setExchCnt(0);
						ordergoods.setAsCnt(0);				
						ordergoods.setOrderNo(order_no);
						ordergoods.setOrderGSeq(canceldtVO.getOrderGSeq());
						ordergoods.setCancelQtyOrg(orgOrdergoods.getCancelQty());
						ordergoods.setClaimQtyOrg(orgOrdergoods.getClaimCanQty());
						ordergoods.setExchCntOrg(orgOrdergoods.getExchCnt());
						ordergoods.setAsCntOrg(orgOrdergoods.getAsCnt());
						
						//TORDERRECEIPTS 세팅
						log.info("//----TORDERRECEIPTS setting");
						orderreceipts = new Orderreceipts();;
						
						orderreceipts.setBlank();
						orderreceipts.setReceiptNo(receipt_no);
						orderreceipts.setOrderNo(order_no);
						orderreceipts.setCustNo(canceldtVO.getCustNo());
						orderreceipts.setDoFlag("90");
						//orderreceipts.setSettleGb("61");
						orderreceipts.setSettleGb("61");
						orderreceipts.setCardBankCode("");
						orderreceipts.setBankSeq("");
						orderreceipts.setCardName("");
						orderreceipts.setCardNo("");
						orderreceipts.setCvv("");		
						orderreceipts.setDepositor(orgOrderdt.getCustName());
						orderreceipts.setValidDate("");
						orderreceipts.setQuestAmt(canceldtVO.getRsaleAmt());
						orderreceipts.setReceiptPlanDate(null);
						orderreceipts.setOkNo("");
						orderreceipts.setOkDate(sysDateTime);
						orderreceipts.setOkMed("000");
						orderreceipts.setOkErrorCode("0000");
						orderreceipts.setVanComp("");
						orderreceipts.setPayMonth(0);
						orderreceipts.setNorestYn("0");
						orderreceipts.setNorestRate(0);
						orderreceipts.setNorestAmt(0);
						orderreceipts.setReceiptAmt(0);
						orderreceipts.setReceiptDate(null);
						orderreceipts.setEndYn("1");
						orderreceipts.setRepayPbAmt(0);
						orderreceipts.setCancelYn("0");
						orderreceipts.setCancelCode("000");
						orderreceipts.setCancelDate(null);
						orderreceipts.setCancelId("");
						orderreceipts.setSaveamtUseFlag("0");
						orderreceipts.setCodDelyYn("0");
						orderreceipts.setDivideYn("0");
						orderreceipts.setPartialCancelYn("1");
						orderreceipts.setProtxVendortxcode("");
						orderreceipts.setProtxStatus("");
						orderreceipts.setProtxStatusdetail("");
						orderreceipts.setProtxVpstxid("");
						orderreceipts.setProtxSecuritykey("");
						orderreceipts.setProtxTxauthno("");
						orderreceipts.setIssueNumber("");
						orderreceipts.setPayboxIdentifiant("");
						orderreceipts.setPayboxCodetraitement("");
						orderreceipts.setPayboxPays("");
						orderreceipts.setPayboxDateq("");
						orderreceipts.setPayboxReference("");
						orderreceipts.setCardPassword("");
						orderreceipts.setRemark1V("");
						orderreceipts.setRemark2V("");
						orderreceipts.setRemark3N(0);
						orderreceipts.setRemark4N(0);
						orderreceipts.setRemark5D(null);
						orderreceipts.setRemark6D(null);
						orderreceipts.setRemark("");
						orderreceipts.setProcId(user_id);
						orderreceipts.setProcDate(sysDateTime);
						orderreceipts.setInsertId(user_id);
						orderreceipts.setInsertDate(sysDateTime);
						
						orderreceiptsList.add(orderreceipts);
						
						paramMap.put("entpCode", 	orgOrderdt.getEntpCode());
						paramMap.put("userId", 		user_id);
						
						
						//TORDERSHIPCOST 세팅
				        //ordershipcost = settingTordershipcost(paramMap, sysDateTime);
						
					}
					
				} else if(!orgOrderdt.getOrderDSeq().equals("001")) { //= 출하지시 이후 사은품건은 반품데이터 생성 
					claimdt = new ClaimdtVO();
					
					claimdt.setOrderNo(orgOrderdt.getOrderNo());
					claimdt.setOrderGSeq(orgOrderdt.getOrderGSeq());
					claimdt.setOrderDSeq(orgOrderdt.getOrderDSeq());
					claimdt.setOrderWSeq(newOrderWseq);
					claimdt.setCustNo(orgOrderdt.getCustNo());
					claimdt.setReceiverSeq(orgOrderdt.getReceiverSeq());
					claimdt.setClaimDate(sysDateTime);
					claimdt.setLastProcId(user_id);
					claimdt.setLastProcDate(sysDateTime);
					claimdt.setClaimAmt(0);
					claimdt.setDcAmt(orgOrderdt.getDcAmt());
					claimdt.setDcAmtGoods(orgOrderdt.getDcAmt());
					
					claimdt.setDcAmtMemb(orgOrderdt.getDcAmtMemb());
					claimdt.setDcAmtDiv(orgOrderdt.getDcAmtDiv());
					claimdt.setDcAmtCard(orgOrderdt.getDcAmtCard());
					claimdt.setRsaleAmt(orgOrderdt.getRsaleAmt());
					claimdt.setSaveamt(orgOrderdt.getSaveamt());
					claimdt.setSaveamtGb(orgOrderdt.getSaveamtGb());
					claimdt.setDoFlag("10");
					claimdt.setLastProcDate(sysDateTime);
					
					claimdt.setGoodsCode(orgOrderdt.getGoodsCode());
					claimdt.setGoodsdtCode(orgOrderdt.getGoodsdtCode());
					claimdt.setGoodsdtInfo(orgOrderdt.getGoodsdtInfo());
					
					claimdt.setClaimQty(cancelInputVO.getCancelQty());
					claimdt.setSyscancel(0);
					claimdt.setClaimGb("30");
					claimdt.setClaimDesc("제휴사 취소");   //해당 컬럼은 신세계에만 있는 컬럼.. (Insert문에서 제거)
					
					claimdt.setClaimCode("999");
					claimdt.setCsLgroup(cancelInputVO.getCancelCode().substring(0, 2));
					claimdt.setCsMgroup(cancelInputVO.getCancelCode().substring(2, 4));
					claimdt.setCsSgroup(cancelInputVO.getCancelCode().substring(4, 6));
					claimdt.setCsLmsCode(cancelInputVO.getCancelCode());
					
					
					claimdt.setClaimAmt(0);
					claimdt.setDcAmt(0);
					claimdt.setDcAmtGoods(0);
					claimdt.setDcAmtMemb(0);
					claimdt.setDcAmtDiv(0);
					claimdt.setDcAmtCard(0);
					claimdt.setRsaleAmt(0);
					claimdt.setSyslastDc(0);
					claimdt.setSyslastDcGoods(0);
					claimdt.setSyslastDcMemb(0);
					claimdt.setSyslastDcDiv(0);
					claimdt.setSyslastDcCard(0);
					claimdt.setSyslastAmt(0);
					claimdt.setHappyCardYn("0");
					claimdt.setRepayGb("0");
					claimdt.setExchPair("");
					claimdt.setRsalePaAmt(0);//= 제휴사 실결제금액
					claimdt.setRemark3N(0);
					
				}
				
				cancelList.add(canceldtVO);
				claimList.add(claimdt);
				ordergoodsList.add(ordergoods);
				orderstockList.add(orderstock);
			
			}
			
			paramMap.put("CostApply4Gift", costApply4Gift);
			paramMap.put("cancelDtList",cancelDtList);
			paramMap.put("orgOrderdtList4ShipCost", orgOrderdtList4ShipCost);
			paramMap.put("newOrderWseq", newOrderWseq);
			paramMap.put("sysDateTime",sysDateTime );

			
			HashMap<String, Object> obj = new HashMap<>();
			

			//백호선 2018/05/11 모든게 Core에 구현되있었다. 나는 왜 새로 만들었던가 ㅠㅠ
			//settingTordershipcost4SK4BBack에 비해 테스트가 더 필요함..(이거 주석 지우자)
			obj = settingTordershipcost4SKB(paramMap);	
			ordershipcost  = (OrdershipcostVO[]) obj.get("OrderShipCost");
			ordercostapply = (Ordercostapply[]) obj.get("OrderCostApply");
			
			
			if( ("999999").equals((String) obj.get("ReciverPost"))){
				addtionalMessage = "미지정 주소지에 따른 취소배송비 생성하지 않음" + (String) obj.get("ReciverPost");
			}
			 
		
			
			
				
			//트랜잭션때문에 settingTordershipcost4SKB와 밑의 함수들 절대 순서 바꾸지 말것!!!
			
			//update TORDERDT , insert TCANCELDT, update TINPLANQTY ( do_flag < 30 인 경우.)
			saveCanceldt(cancelList);									
			//insert TCLAIMDT ( do_flag >= 30 인 사은품)
			insertClaimdt(claimList);		
			//update TORDERGOODS
			updateOrderGoods(ordergoodsList);		
			//update TORDERSTOCK
			updateStockClaim(orderstockList);
			
		
			//insert TORDERRECEIPTS
			 insertReceiptsCancel(orderreceiptsList, ordershipcost);

			insertPaOrderShipCost4Cancel(ordershipcost, cancelList, orderreceiptsList );

			
			//update TORDERRECEIPTS
			
			if (rsalePaAmt  > 0 ){
				updateReceipts(orderreceiptsList);  //Repay_PB_AMT 계산용
				////orderreceiptsList = null; //= orderreceipts 셋팅이 끝나면 리스트 null 처리.
			}
							
			//insert TORDERSHIPCOST
			insertOrdershipcost(ordershipcost);
			
			//insert TORDERCOSTAPPLY
			insertOrderCostApply(ordercostapply);
			
			
			//TORDERPROMO 세팅
			log.info("//----TORDERPROMO setting");
			orgOrderPromoList = paorderDAO.selectOrderPromo(paramMap); //주문 orderpromo 조회
			int orgOrderPromoListSize = orgOrderPromoList.size();
			orderPromoList = new ArrayList<Orderpromo>();
			
			for(int i = 0; orgOrderPromoListSize > i; i++){
				orgOrderpromo = new Orderpromo();
				orderpromo = new Orderpromo();
				
				orgOrderpromo = orgOrderPromoList.get(i); 
				
				orderpromo.setBlank();
				orderpromo.setPromoNo(orgOrderpromo.getPromoNo());
				orderpromo.setCancelDate(sysDateTime);
				orderpromo.setCancelId(user_id);
				orderpromo.setOrderNo(orgOrderdt.getOrderNo());
				orderpromo.setOrderGSeq(orgOrderdt.getOrderGSeq());
				
				orderPromoList.add(orderpromo);
			}
			
			//update TORDERPROMO
			updateOrderpromo(orderPromoList, paramMap);
			
			paramMap.put("orderDSeq", "001"); //= 본품의 D_SEQ를 return 하기 위함.
			
			
			
			// 결과값 셋팅.
			//addtionalMessage는 주소정제가 실패한 주문데이터를 취소 처리할때 세팅됨.. 우아한 방법을 고안해야할것 , 일단 테스트용
			rtnValue = getMessage("pa.success_cancel_input") + addtionalMessage;  
			
			
			
			resultMap = new HashMap<>();
			resultMap.put("MAPPING_SEQ", 	cancelInputVO.getMappingSeq());
			resultMap.put("ORDER_NO", 		paramMap.getString("orderNo"));
			resultMap.put("ORDER_G_SEQ", 	paramMap.getString("orderGSeq"));
			resultMap.put("ORDER_D_SEQ", 	paramMap.getString("orderDSeq"));
			resultMap.put("ORDER_W_SEQ", 	newOrderWseq);
			resultMap.put("RESULT_CODE", 	"000000"); 
			resultMap.put("RESULT_MESSAGE",	rtnValue);
			  
		} catch (Exception e) {
			if(e.getMessage() == null){
				rtnValue = getMessage("pa.fail_cancel_input", new String[]{e.toString()});
			} else {
				rtnValue = getMessage("pa.fail_cancel_input", new String[]{e.getMessage()});
			}
			throw processMessageException(rtnValue);
		} finally {
			try {
				if(resultMap != null){
					ParamMap resultParamMap = null;
					resultParamMap = new ParamMap();
					
					resultParamMap.setParamMap(resultMap);
					resultParamMap.replaceCamel();
					
					if(resultParamMap.getString("resultCode").equals("000000")){
						resultParamMap.put("createYn", "1");
					} else {
						resultParamMap.put("createYn", "0");
					}
					
					executedRtn = paorderDAO.updatePaOrderm(resultParamMap);
					executedRtn= 1;
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
					}
				}
			} catch (Exception e2) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
		}
		
		return resultMap;
	}
	
	
	
	private int insertPaOrderShipCost4Cancel(Ordershipcost[] ordershipcostArr, List<CanceldtVO> canceldtVO , ArrayList<Orderreceipts> orderreceiptsList) throws NumberFormatException, Exception {
		
		int executedRtn = 1; 	
		PaOrderShipCost paOrderShipcost = null;	
		String orderNo     = null;
		String userId      = null;
		String receiverSeq = null;
		String custNo	   = null;
		Timestamp cancelDate = null;
		Timestamp sysDateTime = null;
		long shipFeeAmt    = 0;
	
		for(CanceldtVO vo :canceldtVO){
			if(vo.getOrderDSeq().equals("001")){
				orderNo 	= vo.getOrderNo();
				custNo		= vo.getCustNo();
				receiverSeq = vo.getReceiverSeq();
				userId		= vo.getLastProcId();
				cancelDate	= vo.getCancelDate();
				sysDateTime	= vo.getLastProcDate();
			}
		}
		
		for(Ordershipcost os : ordershipcostArr){
			shipFeeAmt += os.getShpfeeCost();
		}
		
		if(shipFeeAmt < 0 ){
			return 1 ;
		}

		for (Orderreceipts os : orderreceiptsList){
			
			if(os.getSettleGb().equals("61") || os.getSettleGb().equals("11") ){
				continue;
			}

			long seq  = Long.parseLong(systemService.getMaxNo("TPAORDERSHIPCOST","SEQ", "ORDER_NO = '" + orderNo + "'" , 3));
			String order_gb =  null;
			
			paOrderShipcost = new PaOrderShipCost();
			paOrderShipcost.setOrderNo(orderNo);
			paOrderShipcost.setSeq((ComUtil.lpad(Long.toString(seq), 3, "0")));
			paOrderShipcost.setCustNo(custNo);
			paOrderShipcost.setOutReceiverSeq(receiverSeq);
			paOrderShipcost.setReturnReceiverSeq("");
			paOrderShipcost.setOrderDate(cancelDate);
			paOrderShipcost.setReceiptNo(os.getReceiptNo());
			
			order_gb = (os.getSettleGb().equals("15") )? "10":"20";
			paOrderShipcost.setType(order_gb);  //가변적인 val
			paOrderShipcost.setPaShpFeeAmt(shipFeeAmt);  //★일단은 11번가에서 금액을 넘겨주지 않음
			paOrderShipcost.setShpFeeAmt(shipFeeAmt);
			paOrderShipcost.setManualCancelAmt(0);
			paOrderShipcost.setShpFeeYn(1);         //0무상 1유상 2 협의
			paOrderShipcost.setInsertId(userId);
			paOrderShipcost.setInsertDate(sysDateTime);	

			executedRtn = insertPaOrderShipCost(paOrderShipcost);	
		}
		
		return executedRtn; 
	}

	
	
	/**
	 * 배송비 계산
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap<String, Object> settingTordershipcost4SKB(ParamMap paramMap) throws Exception {
	
		
		List<OrderdtVO> orgOrderdtList = new ArrayList<OrderdtVO>();
		Timestamp sysDateTime = (Timestamp) paramMap.get("sysDateTime");
		String newOrderWseq     = paramMap.getString("newOrderWseq");
		
		
		
		Collection cResultOrdershipcost = new ArrayList();
		Collection cResultOrdercostapply = new ArrayList();
		
		
		OrderdtVO orgOrderdt = null; 
		OrdershipcostVO[] returnCustShipCost = null;
		Ordercostapply[]  returnOrderCostApply = null;
		
		HashMap<String, Object> retrunMap  = new HashMap<>();
		List<Ordercostapply> costApply4Gift = new ArrayList<Ordercostapply>();
		
		
		String receiveSeq       = "001";
		String arg_goods_str    = "";
		String arg_geq_str      = "";
		String arg_dseq_str     = "";
		String arg_wseq_str     = "";		
		String arg_qty_str      = "";
		String arg_amt_str      = "";
		String arg_cust_no		= "";
		String arg_receiver_seq = "";
		String arg_calc_qty		= "";
		String arg_rcv_str      = "";
		String result           = null;
		String[] shipcostArr;
		String[] shipcostmArr;
		String[] shipcostapplyArr;
		OrderShipCostVO orderShipCost = null;
		OrdershipcostVO ordershipcost = null;
		
		String ReciverPost = null;

		orgOrderdtList = (List<OrderdtVO>) paramMap.get("orgOrderdtList4ShipCost");
		costApply4Gift = (List<Ordercostapply>) paramMap.get("CostApply4Gift");
			

		
		for (int i = 0; i < orgOrderdtList.size(); i++) {
			
			orgOrderdt = new OrderdtVO();
			orgOrderdt = orgOrderdtList.get(i);
			arg_calc_qty = "";
			
			if(paramMap.getString("orderGSeq").equals(orgOrderdt.getOrderGSeq())){
				//arg_calc_qty = Long.toString(orgOrderdt.getSyslast() - paramMap.getLong("cancelQty"));
				arg_calc_qty = Long.toString(paramMap.getLong("cancelQty"));	
			} else {
				continue;	
			}
			
			receiveSeq   = orgOrderdt.getReceiverSeq();
			
			arg_cust_no = orgOrderdt.getCustNo();
			arg_receiver_seq = orgOrderdt.getReceiverSeq();
			arg_goods_str += " " + orgOrderdt.getGoodsCode();
			
			arg_geq_str  += " " + orgOrderdt.getOrderGSeq();
			arg_dseq_str += " " + orgOrderdt.getOrderDSeq();
			arg_wseq_str += " " + orgOrderdt.getOrderWSeq();
			
			arg_rcv_str   += " " + "1";
			arg_amt_str   += " " + Double.toString((orgOrderdt.getRsaleAmt() / orgOrderdt.getOrderQty()) * Double.parseDouble(arg_calc_qty));
			//arg_amt_str   = " " + Double.toString(orgOrderdt.getRsaleAmt());
			arg_qty_str   += " " + arg_calc_qty;		
		}
		
	
		
		arg_goods_str = arg_goods_str.trim();
		arg_qty_str = arg_qty_str.trim();
		arg_amt_str = arg_amt_str.trim();
		arg_rcv_str = arg_rcv_str.trim();
		arg_geq_str = arg_geq_str.trim();
		//arg_geq_str = "001 002";
		arg_dseq_str= arg_dseq_str.trim();
		arg_wseq_str= arg_wseq_str.trim(); 
			
		
		orderShipCost = new OrderShipCostVO();	
		orderShipCost.setCustNo(arg_cust_no);
		orderShipCost.setOrderFlag("20");
		
		orderShipCost.setDate(orgOrderdt.getOrderDate());
		orderShipCost.setShipcostReceiptStr(arg_rcv_str);
		orderShipCost.setGoodsStr(arg_goods_str);
		orderShipCost.setAmtStr(arg_amt_str);
		orderShipCost.setQtyStr(arg_qty_str);
		
		orderShipCost.setOrderNo(paramMap.getString("orderNo"));
		orderShipCost.setReceiverSeq(arg_receiver_seq);
		
		
		orderShipCost.setCancelQtyStr(arg_qty_str);
		orderShipCost.setReturnQtyStr(arg_qty_str);
		orderShipCost.setExchangeQtyStr(arg_qty_str);
		orderShipCost.setOrderGSeqStr(arg_geq_str);
		orderShipCost.setOrderDSeqStr(arg_dseq_str);
		orderShipCost.setOrderWSeqStr(arg_wseq_str);
			
			
		ParamMap receiverParam = new ParamMap();
		receiverParam.put("custno",arg_cust_no);
		receiverParam.put("recieverseq", receiveSeq);
		ReciverPost =  paorderDAO.selectReciverPost(receiverParam);
		
		/*
		//조건부 배송 취소 오류 체크 로직 jmChoi
		//수기 처리 하기로 했으므로 일단 해당 로직은 주석처리함.. 조건부로 인해 터지면 당분간 수기처리하기로 했음 내용은 test()라는 함수안에 주석으로 설명해놓음  hsBeak 
		//int cnt = 0;
		//cnt = paorderDAO.checkCNOrdershipcost(paramMap);
		
		
		if(!"999999".equals(ReciverPost)){
			try{
				test();
				//result = ComUtil.NVL(orderBizProcess.orderShipCost(orderShipCost));				
					
			}catch(Exception e){
				PaMonitering pamoniter = new PaMonitering();
				
				pamoniter.setCheckNo(paramMap.getString("paOrderNo"));
				pamoniter.setGoodsCode(paramMap.getString("goodsCode"));
				pamoniter.setPaGoodsCode(paramMap.getString("paGoodsCode"));
				pamoniter.setCheckGb("80");
				pamoniter.setCheckTxt("코어 배송비 계산 오류");
				
				paorderDAO.insertTpaMonitering(pamoniter);
				
			}
		} 
		*/
		
		
		//조건부 취소 코어 오류에 대한 내용은 아래 함수 주석을 지우고 들어가보세요
		//test();
		
		if(!"999999".equals(ReciverPost)){
			result = ComUtil.NVL(orderBizProcess.orderShipCost(orderShipCost));
		}
	
		
		if(result != null ){
			if(result.length() >0 ){
				
			
				shipcostArr = result.split("::");
				shipcostmArr = shipcostArr[0].split(" ");	
				shipcostapplyArr = 	shipcostArr[1].split(" ");
				
				for (int j = 0; j < shipcostmArr.length - 7; j++) {
					if (j % 8 == 0) {
							
						ordershipcost = new OrdershipcostVO();
						ordershipcost.setBlank();
						ordershipcost.setCwareAction("I");
						ordershipcost.setOrderNo(shipcostmArr[j]);
						ordershipcost.setSeq(shipcostmArr[j+1]);
						ordershipcost.setReceiverSeq(shipcostmArr[j+2]);
						ordershipcost.setType(shipcostmArr[j+3]);
						ordershipcost.setEntpCode(shipcostmArr[j+4]);
						ordershipcost.setShpfeeCost(Double.parseDouble(shipcostmArr[j+5]));
						ordershipcost.setManualCancelAmt(0);
						ordershipcost.setShipCostNo(shipcostmArr[j+6]);
						ordershipcost.setShipCostReceipt("1"); //1 - 선불, 2 - 착불
						ordershipcost.setShpfeeCode("20");
						ordershipcost.setInsertId(paramMap.getString("userId"));
						ordershipcost.setInsertDate(sysDateTime);

						cResultOrdershipcost.add(ordershipcost);
										
					}
				}
				
				for(int j = 0; j < shipcostapplyArr.length - 6; j++) {
					if( j % 7 == 0 ) {
						Ordercostapply ordercostapply = new Ordercostapply();
						ordercostapply.setBlank();
						ordercostapply.setOrderNo(shipcostapplyArr[j]);
						ordercostapply.setOrderGSeq(shipcostapplyArr[j+1]);
						ordercostapply.setOrderDSeq(shipcostapplyArr[j+2]);
						ordercostapply.setCwareAction(shipcostapplyArr[j+6]);
						
						if (ordercostapply.getCwareAction().equals("I")){
							ordercostapply.setOrderWSeq(newOrderWseq);    //아주 드문 케이스에서 코어가 w 계산을 잘못함.
						}else{
							ordercostapply.setOrderWSeq("001");    //아주 드문 케이스에서 코어가 w 계산을 잘못함.
						}
						//ordercostapply.setOrderWSeq(shipcostapplyArr[j+3]);
						ordercostapply.setApplyCostSeq(shipcostapplyArr[j+4]);
						ordercostapply.setShipCostNo(Integer.parseInt(shipcostapplyArr[j+5]));
						ordercostapply.setCwareAction(shipcostapplyArr[j+6]);
						ordercostapply.setInsertId(paramMap.getString("userId"));
						ordercostapply.setInsertDate(sysDateTime);
						ordercostapply.setModifyId(paramMap.getString("userId"));
						ordercostapply.setModifyDate(sysDateTime);
						cResultOrdercostapply.add(ordercostapply);
					}
				}
				
			}
				
		}// END of result != null check
		
		
			

		//사은품 처리
		if( costApply4Gift != null){
			for( Ordercostapply costApplyVo  : costApply4Gift){
				cResultOrdercostapply.add(costApplyVo);
		  };
		}
		
		//= 취소된 수량으로 재계산한 배송비 정보.
		returnCustShipCost = (OrdershipcostVO[]) cResultOrdershipcost.toArray(new OrdershipcostVO[0]);
		returnOrderCostApply = (Ordercostapply[]) cResultOrdercostapply.toArray(new Ordercostapply[0]);
		
		
		retrunMap.put("OrderShipCost", returnCustShipCost);
		retrunMap.put("OrderCostApply", returnOrderCostApply);
		retrunMap.put("ReciverPost", ReciverPost );
		
		
		return retrunMap;
	}
	
	
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private HashMap<String, Object> settingTordershipcost4SKB4BakcUp(ParamMap paramMap) throws Exception {
		
		
		List<CanceldtVO> CanceldtList = new ArrayList<CanceldtVO>();
		List<OrderdtVO> orgOrderdtList = new ArrayList<OrderdtVO>();
		Timestamp sysDateTime = (Timestamp) paramMap.get("sysDateTime");
		
		
		
		Collection cOrgOrdershipcost = new ArrayList();
		Collection cResultOrdershipcost = new ArrayList();
		Collection cReturnOrdershipcost = new ArrayList();		
		Collection cReturnOrdercostapply = new ArrayList();
	
		OrderdtVO orgOrderdt = null; 
		OrdershipcostVO[] orgCustShipCost = null;
		OrdershipcostVO[] resultCustShipCost = null;
		OrdershipcostVO[] returnCustShipCost = null;
		Ordercostapply[]  returnOrderCostApply = null;
		
		HashMap<String, Object> retrunMap  = new HashMap<>();
		
		
		CanceldtList = (List<CanceldtVO>) paramMap.get("cancelDtList");
		orgOrderdtList = (List<OrderdtVO>) paramMap.get("orgOrderdtList4ShipCost");
		

		// 기존 주문배송비 가져오기.
		cOrgOrdershipcost = paorderDAO.selectOrderShipCostOrg(paramMap);
		orgCustShipCost = new OrdershipcostVO[cOrgOrdershipcost.size()];
		orgCustShipCost = (OrdershipcostVO[]) cOrgOrdershipcost.toArray(new OrdershipcostVO[0]);
		
		//torderdt 조회
		//List<OrderdtVO> orgOrderdtAllList = null;
		//orgOrderdtAllList = paorderDAO.selectOrderdtAll(paramMap);  //Transaction 문제로 인해서 Update 하기전에 인자로 받아오곘다.
		
		//List<OrderdtVO> orgOrderdtList = paorderDAO.selectOrderdt4GSEQ(paramMap); //Transaction 문제로 인해서 Update 하기전에 인자로 받아오곘다.
		
		
		String arg_goods_str    = "";
		String arg_qty_str      = "";
		String arg_amt_str      = "";
		String arg_cust_no		= "";
		String arg_receiver_seq = "";
		String arg_calc_qty		= "";
		String arg_rcv_str      = "";
		String result           = null;
		String ShipCostNum      = null;
		String[] shipcostArr;
		String[] shipcostmArr;
		OrderShipCostVO orderShipCost = null;
		OrdershipcostVO ordershipcost = null;	
		

		for (int i = 0; i < orgOrderdtList.size(); i++) {
			
			orgOrderdt = new OrderdtVO();
			orgOrderdt = orgOrderdtList.get(i);
			arg_calc_qty = "";
			
			if(paramMap.getString("orderGSeq").equals(orgOrderdt.getOrderGSeq())){
				arg_calc_qty = Long.toString(orgOrderdt.getSyslast() - paramMap.getLong("cancelQty"));
			} else {
				continue;	
			}
			
			if(Integer.parseInt(arg_calc_qty) == 0) continue; //= 잔여수량이 0개 일경우 배송비 계산을 하지 않음.
			
			arg_cust_no = orgOrderdt.getCustNo();
			arg_receiver_seq = orgOrderdt.getReceiverSeq();
			arg_goods_str = " " + orgOrderdt.getGoodsCode();
			arg_rcv_str   = " " + "1";
			arg_amt_str   = " " + Double.toString((orgOrderdt.getRsaleAmt() / orgOrderdt.getOrderQty()) * Double.parseDouble(arg_calc_qty));
			//arg_amt_str   = " " + Double.toString(orgOrderdt.getRsaleAmt());
			arg_qty_str   = " " + arg_calc_qty;		
		}
		
	
		
		if( !"0".equals(arg_calc_qty) && !"".equals(arg_calc_qty) ){
			arg_goods_str = arg_goods_str.trim();
			arg_qty_str = arg_qty_str.trim();
			arg_amt_str = arg_amt_str.trim();
			arg_rcv_str = arg_rcv_str.trim();

			
			orderShipCost = new OrderShipCostVO();	
			orderShipCost.setCustNo(arg_cust_no);
			orderShipCost.setOrderFlag("10");
			orderShipCost.setDate(orgOrderdt.getOrderDate());
			orderShipCost.setShipcostReceiptStr(arg_rcv_str);
			orderShipCost.setReceiverSeq(arg_receiver_seq);
			orderShipCost.setGoodsStr(arg_goods_str);
			orderShipCost.setAmtStr(arg_amt_str);
			orderShipCost.setQtyStr(arg_qty_str);	
			
			
			result = ComUtil.NVL(orderBizProcess.orderShipCost(orderShipCost));
			
			
			shipcostArr = result.split("::");
			shipcostmArr = shipcostArr[0].split(" ");	
				
		
	      	String EntpCode = null;
			Double Fee = 0.0;
			
			for (int j = 0; j < shipcostmArr.length - 4; j++) {
				if (j % 5 == 0) {
					
					ShipCostNum = shipcostmArr[j + 2];
					EntpCode = shipcostmArr[j];
					Fee = Double.parseDouble(shipcostmArr[j + 1]);
					ordershipcost = new OrdershipcostVO();
					ordershipcost.setEntpCode(EntpCode);
					ordershipcost.setType("10");
					ordershipcost.setShpfeeCost(Fee);
					ordershipcost.setReceiverSeq(arg_receiver_seq);
					ordershipcost.setShipCostNo(ShipCostNum);
					
					cResultOrdershipcost.add(ordershipcost);
								
				}
			}
		}
		
		
			//= 취소된 수량으로 재계산한 배송비 정보.
		resultCustShipCost = (OrdershipcostVO[]) cResultOrdershipcost.toArray(new OrdershipcostVO[0]);
	

		int x = -1;
		int y = -1;
		String seq = "";
		double tempAmt = 0;
		
		
		for (int j = 0; j < orgCustShipCost.length; j++) {
			
			y = -1;
     		
						
			for ( int i = 0; i < resultCustShipCost.length; i++) {
     			if (orgCustShipCost[j].getReceiverSeq().equals(resultCustShipCost[i].getReceiverSeq())
     			 && orgCustShipCost[j].getEntpCode().equals(resultCustShipCost[i].getEntpCode())) {
     				y = i;
     				break;
     			}
     		}
			
			
			
			
     		if (y == -1 || (resultCustShipCost.length == 0 )) { //원조회내역에 있으나 없어진 배송비 있는경우 취소처리
     			if (orgCustShipCost[j].getShpfeeCost() <= 0) continue;//원조회내역이 0 이면 패스
                tempAmt = 0;

                // 수동 취소 금액과 비교하여 금액의 차액만 취소 데이터에 추가
                if(orgCustShipCost[j].getShpfeeCost() > tempAmt){
                	seq = getMaxNo("TORDERSHIPCOST", "SEQ", " ORDER_NO='"	+ paramMap.getString("orderNo") + "' ", 3);
                	
                	ordershipcost = null;
                	ordershipcost = new OrdershipcostVO();
                	
					ordershipcost.setReceiverSeq(orgCustShipCost[j].getReceiverSeq());
					ordershipcost.setSeq(seq);
					ordershipcost.setType("20");
					ordershipcost.setShpfeeCode("20");
					ordershipcost.setShpfeeCost(orgCustShipCost[j].getShpfeeCost());
					ordershipcost.setEntpCode(orgCustShipCost[j].getEntpCode());
					ordershipcost.setShipCostNo(orgCustShipCost[j].getShipCostNo());
					ordershipcost.setOrderNo(paramMap.getString("orderNo"));
					ordershipcost.setOrderGSeq("");
					ordershipcost.setOrderDSeq("");
					ordershipcost.setOrderWSeq("");
					ordershipcost.setDelyType("");
					ordershipcost.setInsertId(paramMap.getString("userId"));
					ordershipcost.setInsertDate(sysDateTime);
					
					cReturnOrdershipcost.add(ordershipcost);
					
					
					//2018.05.02 백호선 OrderCostApply 추가분 (총 3개있음)
					for(int k= 0; k < CanceldtList.size(); k++){
						
						if(!CanceldtList.get(k).getReceiverSeq().equals(ordershipcost.getReceiverSeq()) 
						|| !CanceldtList.get(k).getEntpCode().equals(ordershipcost.getEntpCode())) 
						continue;;
						

						Ordercostapply ordercostapply = new Ordercostapply();
						ordercostapply.setOrderNo(paramMap.getString("orderNo"));
						ordercostapply.setOrderGSeq(CanceldtList.get(k).getOrderGSeq());
						ordercostapply.setOrderDSeq(CanceldtList.get(k).getOrderDSeq());
						ordercostapply.setOrderWSeq(CanceldtList.get(k).getOrderWSeq());
						
						if("001".equals(ordercostapply.getOrderDSeq())){ //본품
							ordercostapply.setApplyCostSeq(seq);
							ordercostapply.setShipCostNo(Integer.parseInt(orgCustShipCost[j].getShipCostNo()));
						}else{
							ordercostapply.setApplyCostSeq("000");
							ordercostapply.setShipCostNo(0);
						}
						ordercostapply.setInsertId(paramMap.getString("userId"));
						ordercostapply.setInsertDate(sysDateTime);
						ordercostapply.setModifyId(paramMap.getString("userId"));
						ordercostapply.setModifyDate(sysDateTime);
						
						cReturnOrdercostapply.add(ordercostapply);
						
						
					}//end of OrderCostApply
                }
                
     		}//end of  //원조회내역에 있으나 없어진 배송비 있는경우 취소처리
		}//오리지널 대비 새로 계산한 배송비
		
	

		
		for (int i = 0; i < resultCustShipCost.length; i++) {
			if (resultCustShipCost[i].getType().equals("10")) { //주문배송비 >> 원조회내역과 비교하여 차액만큼 처리하여야 한다.
				x = -1;
				for (int j = 0; j < orgCustShipCost.length; j++) {
					if (orgCustShipCost[j].getReceiverSeq().equals(resultCustShipCost[i].getReceiverSeq())
					 && orgCustShipCost[j].getEntpCode().equals(resultCustShipCost[i].getEntpCode())) {
						x = j;
						break;
					}
				}
				if (x == -1) { 	//조회된 배송비가 기존에 없는경우에는 추가
					if (resultCustShipCost[i].getShpfeeCost() == 0) continue; //추가해야할 금액이 0인 경우에는 continue;
					seq = getMaxNo("TORDERSHIPCOST", "SEQ", " ORDER_NO='"	+ paramMap.getString("orderNo") + "' ", 3);
					
					ordershipcost = null;
					ordershipcost = new OrdershipcostVO();
					ordershipcost.setReceiverSeq(resultCustShipCost[i].getReceiverSeq());
					ordershipcost.setSeq(seq);
					ordershipcost.setType(resultCustShipCost[i].getType());
					ordershipcost.setShpfeeCode("20");
					ordershipcost.setShpfeeCost(resultCustShipCost[i].getShpfeeCost());
					ordershipcost.setEntpCode(resultCustShipCost[i].getEntpCode());
					ordershipcost.setOrderNo(paramMap.getString("orderNo"));
					ordershipcost.setOrderGSeq("");
					ordershipcost.setOrderDSeq("");
					ordershipcost.setOrderWSeq("");
					ordershipcost.setShipCostNo(resultCustShipCost[i].getShipCostNo());
					ordershipcost.setDelyType("");
					ordershipcost.setInsertId(paramMap.getString("userId"));
					ordershipcost.setInsertDate(sysDateTime);
					
					cReturnOrdershipcost.add(ordershipcost);
					
					
					//2018.05.02 백호선 OrderCostApply 추가분 (총 3개있음)
					for(int k= 0; k < CanceldtList.size(); k++){
						
						if(!CanceldtList.get(k).getReceiverSeq().equals(ordershipcost.getReceiverSeq()) 
						|| !CanceldtList.get(k).getEntpCode().equals(ordershipcost.getEntpCode())) 
						continue;;
						

						Ordercostapply ordercostapply = new Ordercostapply();
						ordercostapply.setOrderNo(paramMap.getString("orderNo"));
						ordercostapply.setOrderGSeq(CanceldtList.get(k).getOrderGSeq());
						ordercostapply.setOrderDSeq(CanceldtList.get(k).getOrderDSeq());
						ordercostapply.setOrderWSeq(CanceldtList.get(k).getOrderWSeq());
						
						if("001".equals(ordercostapply.getOrderDSeq())){ //본품
							ordercostapply.setApplyCostSeq(seq);
							ordercostapply.setShipCostNo(Integer.parseInt(resultCustShipCost[i].getShipCostNo()));
						}else{
							ordercostapply.setApplyCostSeq("000");
							ordercostapply.setShipCostNo(0);
						}
						
						ordercostapply.setInsertId(paramMap.getString("userId"));
						ordercostapply.setInsertDate(sysDateTime);
						ordercostapply.setModifyId(paramMap.getString("userId"));
						ordercostapply.setModifyDate(sysDateTime);
						
						cReturnOrdercostapply.add(ordercostapply);
						
					}//end of OrderCostApply	
					
					
				} else {	//조회된 배송비가 기존에 있는경우에는 차액만큼 추가 또는 삭제
					tempAmt = orgCustShipCost[x].getShpfeeCost() - resultCustShipCost[i].getShpfeeCost(); //차액

					
					//백호선 Sk store, 차액이 0원이어도 데이터 Insert 해주자...
					//if (tempAmt != 0) {//차액이 0 이 아닌경우에는 추가 또는 삭제
						
					seq = getMaxNo("TORDERSHIPCOST", "SEQ", " ORDER_NO='"	+ paramMap.getString("orderNo") + "' ", 3);
						
					ordershipcost = null;
					ordershipcost = new OrdershipcostVO();
					ordershipcost.setReceiverSeq(resultCustShipCost[i].getReceiverSeq());
					ordershipcost.setSeq(seq);
						
			        if (tempAmt < 0) {
			            ordershipcost.setType("10");
			            ordershipcost.setShpfeeCost(tempAmt * -1);
			        } else {
			            ordershipcost.setType("20");
			            ordershipcost.setShpfeeCost(tempAmt);
			        }
			       
			        ordershipcost.setShpfeeCode("20");
			        ordershipcost.setEntpCode(resultCustShipCost[i].getEntpCode());
			        ordershipcost.setOrderNo(paramMap.getString("orderNo"));
					ordershipcost.setOrderGSeq("");
					ordershipcost.setOrderDSeq("");
					ordershipcost.setOrderWSeq("");
					ordershipcost.setShipCostNo(resultCustShipCost[i].getShipCostNo());
					ordershipcost.setDelyType("");
					ordershipcost.setInsertId(paramMap.getString("userId"));
					ordershipcost.setInsertDate(sysDateTime);
			            
					cReturnOrdershipcost.add(ordershipcost);
					
					
					
					//2018.05.02 백호선 OrderCostApply 추가분 (총 3개있음)
					for(int k= 0; k < CanceldtList.size(); k++){
						
						if(!CanceldtList.get(k).getReceiverSeq().equals(ordershipcost.getReceiverSeq()) 
						|| !CanceldtList.get(k).getEntpCode().equals(ordershipcost.getEntpCode())) 
						continue;;	

						Ordercostapply ordercostapply = new Ordercostapply();
						ordercostapply.setOrderNo(paramMap.getString("orderNo"));
						ordercostapply.setOrderGSeq(paramMap.getString("orderGSeq"));
						ordercostapply.setOrderDSeq(CanceldtList.get(k).getOrderDSeq());
						ordercostapply.setOrderWSeq(CanceldtList.get(k).getOrderWSeq());
						
						if("001".equals(ordercostapply.getOrderDSeq())){ //본품
							ordercostapply.setApplyCostSeq(seq);
							ordercostapply.setShipCostNo(Integer.parseInt(resultCustShipCost[i].getShipCostNo()));
						}else{
							ordercostapply.setApplyCostSeq("000");
							ordercostapply.setShipCostNo(0);
						}
						
						ordercostapply.setInsertId(paramMap.getString("userId"));
						ordercostapply.setInsertDate(sysDateTime);
						ordercostapply.setModifyId(paramMap.getString("userId"));
						ordercostapply.setModifyDate(sysDateTime);
						
						cReturnOrdercostapply.add(ordercostapply);
						
					}//end of OrderCostApply
							
				}
			}
		}
		
		
		returnCustShipCost = (OrdershipcostVO[]) cReturnOrdershipcost.toArray(new OrdershipcostVO[0]);
		returnOrderCostApply = (Ordercostapply[]) cReturnOrdercostapply.toArray(new Ordercostapply[0]);
		
		
		retrunMap.put("OrderShipCost", returnCustShipCost);
		retrunMap.put("OrderCostApply", returnOrderCostApply);
		
		
		return retrunMap;
	}
	
	/**
	 * 주소정제 솔루션
	 * 
	 * @param ArrayList
	 * @return
	 * @throws Exception
	 */
	public ParamMap selectStdAddress(String inputAddr) throws Exception {
		ParamMap rtnMap = new ParamMap();
		//rfnCustCommonAddrList rfn = new rfnCustCommonAddrList();
				
		log.info("===== order-input : selectStdAddress START =====");
		log.info("inputAddr" + ":" + inputAddr);
		
		try {
			
			log.info("result" + ":" + rtnMap.toString());
			log.info("===== order-input : selectStdAddress END =====");
		} catch (Exception e) {
        	throw processMessageException("errors.fail.selectStdAddress");
		}
		return rtnMap;
	}
	
	
	
	public ParamMap selectStdAddress(String postNo, String postSeq, String addrGbn, String addr, String addrDt, HttpServletRequest request) throws Exception {
		ParamMap rtnMap = new ParamMap();
		Map<String, Object> post = new HashMap<String, Object>();
		
		log.info("===== order-input : selectStdAddress START =====");
		

		/** 주소정제 추가 **/
		ArrayList<HashMap<String, Object>> paramList = new ArrayList<>();
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("POST_NO", postNo);
		hm.put("POST_SEQ", postSeq);
		hm.put("ROAD_ADDR_YN", addrGbn);
		hm.put("SEARCH_ADDR", addr);
		hm.put("SEARCH_ADDR_INFO", "");  //뭐에 쓰는거지??.
		hm.put("SEARCH_ADDR2", addrDt);
		//paramList.add(hm);
		paramList = postUtil.checkLocalYn(paramList, request, hm);
		try {
			post = postUtil.retrieveVerifyPost(paramList);
					
		} catch (Exception e) {
			e.printStackTrace();
		}// end of 정제

		@SuppressWarnings("unchecked")
		ArrayList<HashMap<String, Object>> b = (ArrayList<HashMap<String, Object>>)post.get("RESULT");
		
		if (b != null){

			//[{ZPRNR=10863, CNT=1, NODE=D, STDADDR=341-1번지 CK푸드원, NADR2S=64, CK푸드원 (신촌동), NNMB=4148012000103410001000001, NADR1S=경기도 파주시 소라지로177번길, GISY=37.7374256945, IDX=0, ZPRNSR=610, NNMX=126.6934067778, ADDR1Y=경기도 파주시 신촌동, NNMY=37.7375097473, ZPRNSJB=002, ZPRNJ=10863, GISX=126.6934070524},
			// {ZPRNR=10863, NODE=P, CNT=0, STDADDR=341-1번지 CK푸드원, NADR2S=64, CK푸드원 (신촌동), NNMB=4148012000103410001000001, NADR1S=경기도 파주시 소라지로177번길, GISY=37.7374256945, IDX=0, ZPRNSR=610, NNMX=126.6934067778, ADDR1Y=경기도 파주시 신촌동, NNMY=37.7375097473, ZPRNSJB=002, ZPRNJ=10863, GISX=126.6934070524}]
			//result.get("NNMB").toString();
			Map<String, Object> result = (Map<String, Object>) b.get(0);

			rtnMap.put("ADDR_GBN" , addrGbn);//주소입력타입[1:지번주소, 2:도로명주소] 
			rtnMap.put("CL_SUCCESS_YN" , "1"); 
			rtnMap.put("STD_POST_NO" ,result.get("ZPRNJ").toString()); 
			rtnMap.put("STD_POST_SEQ" , result.get("ZPRNSJB").toString()); 
			rtnMap.put("STD_ADDR" ,result.get("NADR1S").toString());
			rtnMap.put("STD_ADDR_DT" ,result.get("NADR2S").toString());
			rtnMap.put("STD_SEQ_ROAD_NO" , "");
						
			
		}else{ 		
			rtnMap.put("CL_SUCCESS_YN" , "0"); 
		}	  
		log.info("result" + ":" +
		rtnMap.toString());
		log.info("===== order-input : selectStdAddress END ====="); 
		return rtnMap;
	}
	
	
	
	public ParamMap selectStdAddress4Test(int addrGbn) throws Exception {
		ParamMap rtnMap = new ParamMap();
		
		log.info("===== order-input : selectStdAddress START =====");
		
		//[{ZPRNR=10863, CNT=1, NODE=D, STDADDR=341-1번지 CK푸드원, NADR2S=64, CK푸드원 (신촌동), NNMB=4148012000103410001000001, NADR1S=경기도 파주시 소라지로177번길, GISY=37.7374256945, IDX=0, ZPRNSR=610, NNMX=126.6934067778, ADDR1Y=경기도 파주시 신촌동, NNMY=37.7375097473, ZPRNSJB=002, ZPRNJ=10863, GISX=126.6934070524},
			// {ZPRNR=10863, NODE=P, CNT=0, STDADDR=341-1번지 CK푸드원, NADR2S=64, CK푸드원 (신촌동), NNMB=4148012000103410001000001, NADR1S=경기도 파주시 소라지로177번길, GISY=37.7374256945, IDX=0, ZPRNSR=610, NNMX=126.6934067778, ADDR1Y=경기도 파주시 신촌동, NNMY=37.7375097473, ZPRNSJB=002, ZPRNJ=10863, GISX=126.6934070524}]
			//result.get("NNMB").toString();
		
		if(addrGbn == 2 ){
			rtnMap.put("ADDR_GBN" , "2");//주소입력타입[1:지번주소, 2:도로명주소] 
			rtnMap.put("CL_SUCCESS_YN" , "1"); 	
			rtnMap.put("STD_POST_NO" ,"10863"); 
			rtnMap.put("STD_POST_SEQ" , "002"); 
			rtnMap.put("STD_ADDR" ,"경기도 파주시 소라지로177번길");
			rtnMap.put("STD_ADDR_DT" ,"64, CK푸드원 (신촌동)");
			rtnMap.put("STD_SEQ_ROAD_NO" , "");
		
		}else{
			rtnMap.put("ADDR_GBN" , "1");//주소입력타입[1:지번주소, 2:도로명주소] 
			rtnMap.put("CL_SUCCESS_YN" , "1"); 	
			rtnMap.put("STD_POST_NO" ,"10863"); 
			rtnMap.put("STD_POST_SEQ" , "002"); 
			rtnMap.put("STD_ADDR" ,"11번가는 무조건 도로명 씁니다");
			rtnMap.put("STD_ADDR_DT" ,"냉무");
			rtnMap.put("STD_SEQ_ROAD_NO" , "");
		}		
		return rtnMap;
	}
	
	
	/**
	 * insert Customer, insert Custsystem, insert Receiver   
	 * @param Customer, Custsystem, Receiver 
	 * @return int ( SUCCESS:1 , ERROR:0 )
	 * @throws Exception
	 */	
	private int saveCustomer(Customer customer, Custsystem custsystem, List<Custtel> custtelList, List<Receiver> receiverList) throws Exception {
		
		int executedRtn = 0;
		
		log.info("//-- insert Customer");
		executedRtn = paorderDAO.insertCustomer(customer);
		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] {"TCUSTOMER INSERT"});
		}
		
		log.info("//-- insert Custsystem");
		executedRtn = paorderDAO.insertCustsystem(custsystem);
		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] {"TCUSTSYSTEM INSERT"});
		}
		
		
		 //백호선 2018-04-25 custtel Insert를 처리해주기 때문에 이 부분은 주석처리합니다.
		/*   
		log.info("//-- insert Custtel");
		int custtelSize = custtelList.size();
		for(int i = 0; custtelSize > i; i++){
			executedRtn = paorderDAO.insertCusttel(custtelList.get(i));
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"TCUSTTEL INSERT"});
			}
		}*/
		
		
		log.info("//-- insert Receiver");
		int receiverSize = receiverList.size();
		for(int i = 0; receiverSize > i; i++){
			receiverList.get(i).setReceiverGb(i==0? "00":"10");	
			receiverList.get(i).setDefaultYn(i==0? "1":"0");	
			receiverList.get(i).setDeliveryDefaultYn(i==0? "1":"0");	
			
			executedRtn = paorderDAO.insertReceiver(receiverList.get(i));
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"TRECEIVER INSERT"});
			}
		}
		
		//= TCUSTSPINFO insert
		Custspinfo tcustspinfo = new Custspinfo();
		tcustspinfo.setBlank();
		tcustspinfo.setCustNo(customer.getCustNo());
		tcustspinfo.setType("40");		//= DM동의
		tcustspinfo.setContent("0");
		tcustspinfo.setMsg("Changed status about DM");
		tcustspinfo.setSpBdate(customer.getInsertDate());
		tcustspinfo.setSpEdate(DateUtil.toTimestamp("2999/12/31 23:59:59"));
		tcustspinfo.setInsertDate(customer.getInsertDate());
		tcustspinfo.setModifyDate(customer.getInsertDate());
		tcustspinfo.setInsertId(customer.getInsertId());
		tcustspinfo.setModifyId(customer.getInsertId());
		tcustspinfo.setSeq("001");
		
		log.info("//-- insert Custspinfo1");
		executedRtn = paorderDAO.insertCustspinfo(tcustspinfo);
		
		if (executedRtn < 1){
			throw processException("msg.cannot_save", new String[] {"TCUSTSPINFO INSERT"});
		}

		tcustspinfo.setType("50");	//= 정보동의		
		tcustspinfo.setContent("1");
		tcustspinfo.setMsg("Changed status about Infomation");
		tcustspinfo.setSeq("001");
		
		log.info("//-- insert Custspinfo2");
		executedRtn = paorderDAO.insertCustspinfo(tcustspinfo);
		
		if (executedRtn < 1){
			throw processException("msg.cannot_save", new String[] {"TCUSTSPINFO INSERT"});
		}
		
		return executedRtn;		
	}
	
	/**
	 * insert TORDERM 
	 * @param Orderm
	 * @return int ( SUCCESS:1 , ERROR:0 )
	 * @throws Exception
	 */	
	private int insertOrderm(Orderm orderm) throws Exception {
		int executedRtn = 0;
		
		log.info("//---- insert TORDERM");
		executedRtn = paorderDAO.insertOrderm(orderm);
		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] {"TORDERM INSERT"});
		}	
		return executedRtn;
	}
	
	/**
	 * insert TORDERGOODS
	 * @param Ordergoods[]
	 * @return int ( SUCCESS:1 , ERROR:0 )
	 * @throws Exception
	 */
	private int insertOrdergoods(List<Ordergoods> ordergoodsList) throws Exception {
		int executedRtn = 0;
		Ordergoods regOrdergoods = null;
		int ordergoodsListSize = ordergoodsList.size();

		for (int i = 0; i < ordergoodsListSize; i++) {
			regOrdergoods = ordergoodsList.get(i);
			
			log.info("//---- insert TORDERGOODS");
			executedRtn = paorderDAO.insertOrdergoods(regOrdergoods);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERGOODS INSERT"});
			}
		}
		return executedRtn;
	}
	
	/**
	 * insert TORDERDT
	 * @param Torderdt[]
	 * @return int ( SUCCESS:1 , ERROR:0 )
	 * @throws Exception
	 */
	private int insertOrderdt(List<OrderdtVO> orderdtList) throws Exception {
		int executedRtn = 0;
		Orderdt regOrderdt = null;
		int orderdtListSize = orderdtList.size();
		
		for (int i = 0; i < orderdtListSize; i++) {
			regOrderdt = (Orderdt) orderdtList.get(i);
			
			log.info("//---- insert TORDERDT");
			executedRtn = paorderDAO.insertOrderdt(regOrderdt);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERDT INSERT"});
			}
			
		}
		return executedRtn;
	}
	private int updateOrderdt20Approval(List<OrderdtVO> orderdtList) throws Exception{	
		int executedRtn = 0;

		for(OrderdtVO orderdt : orderdtList )
			executedRtn = paorderDAO.updateOrderdt20Approval(orderdt);
			if (executedRtn < 1) { //사은품이 껴 있을수 있기 때문에 "!= 1" 로 걸면 안됨 
				throw processException("msg.cannot_save", new String[] {"TORDERDT UPDATE"});
			}
		return executedRtn;
	}
	
	private Orderreceipts orderReceipts20Approval(List<OrderdtVO> orderdtList){
		long rSaleAmt = 0;
		Timestamp orderDate = null;
		String	  orderNo	= null;
		String    userId	= null;
		
		Orderreceipts orderreceipt = new Orderreceipts();
		for(OrderdtVO vo : orderdtList){
			if(vo.getOrderDSeq().equals("001")) { // 사은품 제외 조건
				rSaleAmt += vo.getRsaleAmt();
				orderDate = vo.getLastProcDate();
				orderNo	  = vo.getOrderNo();
				userId	  = vo.getLastProcId();
			}
		}
		orderreceipt.setOrderNo		(orderNo);
		orderreceipt.setReceiptAmt	(rSaleAmt);
		orderreceipt.setReceiptDate (orderDate);
		orderreceipt.setOkDate		(orderDate);
		orderreceipt.setEndYn		("1");
		orderreceipt.setSettleGb	("11");
		orderreceipt.setProcDate 	(orderDate);	
		orderreceipt.setProcId		(userId);	
		return orderreceipt;
	}
	
	/**
	 * insert TORDERSHIPCOST
	 * @param Ordershipcost[]
	 * @return int ( SUCCESS:1 , ERROR:0 )
	 * @throws Exception
	 */
	private int insertOrdershipcost(List<Ordershipcost> ordershipcostList) throws Exception {
		int executedRtn = 0;
		int ordershipcostSize = ordershipcostList.size();
		Ordershipcost regOrdershipcost = null;
		
		if(ordershipcostList.size() == 0) return executedRtn;

		for (int i = 0; i < ordershipcostSize; i++) {
			regOrdershipcost = ordershipcostList.get(i);
			
			log.info("//---- insert Tordershipcost");			
			executedRtn = paorderDAO.insertOrdershipcost(regOrdershipcost);
			
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERSHIPCOST INSERT"});
			}
		}
		return executedRtn;
	}

	
	private int insertOrderCostApply(List<Ordercostapply> orderCostApplyList) throws Exception {
		int executedRtn = 0;
		int orderCostApplySize = orderCostApplyList.size();
		Ordercostapply regOrderCostApply = null;
		
		if(orderCostApplyList.size() == 0) return executedRtn;

		for (int i = 0; i < orderCostApplySize; i++) {
			regOrderCostApply = orderCostApplyList.get(i);
			
			log.info("//---- insert Tordershipcost");			
			executedRtn = paorderDAO.insertOrdercostapply(regOrderCostApply);
			
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERCOSTAPPLY INSERT"});
			}
		}
		return executedRtn;
	}
	
	
	
	
	 /**
     * insert TORDERPROMO
     * @param torderpromo
     * @return
     * @throws Exception
     */
    private int insertOrderpromo(List<Orderpromo> orderpromoList) throws Exception {
        int          executedRtn    = 0;
        Orderpromo regTorderpromo = null;
        int orderpromoListSize = orderpromoList.size();

        //= insert Torderpromo
        for(int i = 0 ; i < orderpromoListSize; i++){
        	regTorderpromo = orderpromoList.get(i);

            log.info("//---- insert Torderpromo");
            executedRtn = paorderDAO.insertOrderpromo(regTorderpromo);
            if (executedRtn != 1){
            	throw processException("msg.cannot_save", new String[] {"TORDERPROMO INSERT"});
            }
        }
        return executedRtn;
    }
    
	/**
	 * insert TORDERRECEIPTS
	 * @param Torderreceipts
	 * @return int ( SUCCESS:1 , ERROR:0 )
	 * @throws Exception
	 */
	private int insertOrderreceipts(List<Orderreceipts> orderreceiptsList) throws Exception {
		int executedRtn = 0;
		Orderreceipts orderreceipts = null;
		
		log.info("//---- insert TORDERRECEIPTS");
		for(int i = 0; orderreceiptsList.size() > i; i++){
			orderreceipts = new Orderreceipts();
			orderreceipts = orderreceiptsList.get(i);
			
			executedRtn = paorderDAO.insertOrderreceipts(orderreceipts);
			
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERRECEIPTS INSERT"});
			}
		}
		
	
		return executedRtn;
	}
	
	private void insertPaOrderShipCost(List<PaOrderShipCost> paOrderShipCostList) throws Exception{	
		for(PaOrderShipCost paordershipcost : paOrderShipCostList){
			insertPaOrderShipCost(paordershipcost);
		}	
	}
	
	private int insertPaOrderShipCost(PaOrderShipCost paordershipcost) throws Exception  {
		
		if (paordershipcost == null){
			return 1;
		}
		
		log.info("//---- insert TPAORDERSHIPCOST");
		int executedRtn  = 0;
		executedRtn = paorderDAO.insertPaOrderShipCost(paordershipcost);
		
		//if (executedRtn < 1) {  //에러나도 무시
			//throw processException("msg.cannot_save", new String[] {"TPAORDERSHIPCOST INSERT"});
		//}
		
		return executedRtn;
	}
    
    /**
	 * update TORDERSTOCK
	 * @param OrderdtVO[]
	 * @return int ( SUCCESS:1 , ERROR:0 )
	 * @throws Exception
	 */
	private int updateOrderstock(List<OrderdtVO> orderdtList) throws Exception {
		int executedRtn = 0;
		OrderdtVO regOrderdt = null;
		Orderstock stock = null;
		ParamMap stockMap = null;
		int rowCnt = 0;
		int orderAbleQty = 0;

		rowCnt = orderdtList.size();
		List<Orderstock> list = new ArrayList<Orderstock>(rowCnt);

		for (int i = 0; i < rowCnt; i++) {
			regOrderdt = orderdtList.get(i);
			stock = new Orderstock();
			stock.setGoodsCode(regOrderdt.getGoodsCode());
			stock.setGoodsdtCode(regOrderdt.getGoodsdtCode());
			stock.setOrderQty(regOrderdt.getOrderQty());
			stock.setWhCode(regOrderdt.getWhCode());
			stock.setModifyId(regOrderdt.getLastProcId());
			stock.setModifyDate(regOrderdt.getLastProcDate());

			list.add(stock);
		} 

		for (int i = 0; i < rowCnt; i++) {
			stock = (Orderstock) list.get(i);
			
			log.info("//---- update TSTOCK");
			executedRtn = paorderDAO.updateOrderStockEnd(stock);
			
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERSTOCK UPDATE"});
			}
			
			//= torderstock update 후 잔여 재고 check.
			stockMap = new ParamMap();
			stockMap.put("goodsCode", 	stock.getGoodsCode());
			stockMap.put("goodsdtCode", stock.getGoodsdtCode());
			stockMap.put("whCode", 		stock.getWhCode());
			
			orderAbleQty = paorderDAO.selectOrderAbleQtyPa(stockMap);
			//= FUN_GET_ORDER_ABLE_QTY 조회 결과 (-)수량일 경우 exception. 
			if(orderAbleQty < 0){
				//[상품코드/단품코드] 의 재고가 없습니다. 에러 메세지 수정시 PaHalfAsyncController.setResultMap() 수정
				throw processMessageException(getMessage("errors.nodata.stock", new String[] { stockMap.getString("goodsCode") + "/" + stockMap.getString("goodsdtCode") })); 
			}
		}
		return executedRtn;
	}
	
	/**
	 * 재고체크
	 * @param request
	 * @param cart
	 * @return
	 * @throws Exception
	 */
	public ParamMap[] calStock(ParamMap[] paramMap , OrderInfoMsg[] goodsInfo) throws Exception {
		
		ParamMap[] resultParamMap = null;
		///int executedRtn = 0;
		
		OrderStockInVO[]  orderStockIn  = new OrderStockInVO[goodsInfo.length];
		OrderStockOutVO[] orderStockOut = new OrderStockOutVO[goodsInfo.length];
		for(int i = 0 ; i < goodsInfo.length ; i++){
            orderStockIn[i]  = new OrderStockInVO();
            orderStockOut[i] = new OrderStockOutVO();
            orderStockIn[i].setSelectFlag       ( "2" ) ;
            orderStockIn[i].setGoodsCode       	( goodsInfo[i].getGoodsCode()      					) ;
            orderStockIn[i].setGoodsName       	( goodsInfo[i].getGoodsName()      					) ;
            orderStockIn[i].setGoodsdtCode     	( goodsInfo[i].getGoodsDtCode()    					) ;
            orderStockIn[i].setOrderNo         	( paramMap[i].getString("orderNo")					) ;
            if(goodsInfo[i].getGiftYn().equals("1")){
            	orderStockIn[i].setOrderGSeq      	( paramMap[i].getString("orderGSeq")				) ;
            	orderStockIn[i].setOrderDSeq      	( ComUtil.increaseLpad(paramMap[i].getString("seq"), 3, "0"));
            } else {
            	orderStockIn[i].setOrderGSeq      	( ComUtil.increaseLpad(Integer.toString(i) , 3, "0")) ;
            	orderStockIn[i].setOrderDSeq      	( "001"								    			) ;
            }
            orderStockIn[i].setOrderWSeq      	( "001"    											) ;
            orderStockIn[i].setGoodsSelectNo  	( selectSequenceNo("GOODS_SELECT_NO")  				) ;
            orderStockIn[i].setInsertId        	( paramMap[i].getString("userId")					) ;                 					
            orderStockIn[i].setCounselQty      	( paramMap[i].getLong("orderQty")   				) ;
            orderStockIn[i].setDelyType        	( goodsInfo[i].getDelyType()       					) ;
            orderStockIn[i].setStockChkPlace  	( goodsInfo[i].getStockChkPlace()					) ;
            //orderStockIn[i].setDelyGb        	( "90"       										) ;
            orderStockIn[i].setDelyGb        	( "10"       										) ;
            orderStockIn[i].setWhCode        	( goodsInfo[i].getWhCode()       					) ;
            orderStockIn[i].setCustNo        	( paramMap[i].getString("custNo") 					) ;
            orderStockIn[i].setReceiverSeq    	( "0000000001"     									) ;
            
            orderStockIn[i].setStdPostNo        ( paramMap[i].getString("stdPostNo"));
            orderStockIn[i].setRoadAddrNo       ( paramMap[i].getString("roadAddrNo"));
            /**
             * 현재 배송지에 따른 dely_gb 는 체크하지 않고있다.
             * orderStockIn[i].setDelyGb          ( this.torderdt[i].getDely_gb()) ;
             * orderStockIn[i].setPost_no          ( this.torderdt[i].getPost_no()) ;
             * orderStockIn[i].setPost_seq         ( this.torderdt[i].getPost_seq()) ;
             */
		}

		//재고 체크
		checkOrderStock(orderStockIn, orderStockOut);
		
		int resultCnt = 0;
		int useCnt = 0;
		int outLength = orderStockOut.length;
		resultParamMap = new ParamMap[outLength];
		
		for(int j = 0 ; j < outLength; j++ ){
			resultParamMap[resultCnt] = paramMap[j]; //기존의 ParamMap 데이터 세팅
			
			resultParamMap[resultCnt].put("setStockFlag", orderStockOut[j].getStockFlag());
			resultParamMap[resultCnt].put("stockKey", orderStockOut[j].getStockKey());
			resultParamMap[resultCnt].put("firstPlanDate", orderStockOut[j].getFirstPlanDate());
			resultParamMap[resultCnt].put("outPlanDate", orderStockOut[j].getOutPlanDate());
			resultParamMap[resultCnt].put("delyHopeDate", orderStockOut[j].getDelyHopeDate());
			resultParamMap[resultCnt].put("goodsSelectNo", orderStockOut[j].getGoodsSelectNo());
			resultParamMap[resultCnt].put("delyGb", orderStockOut[j].getDelyGb());
			resultParamMap[resultCnt].put("whCode", orderStockOut[j].getWhCode());
			resultParamMap[resultCnt].put("delyHopeTime","90");
			resultParamMap[resultCnt].put("delyHopeYn","0");
			resultParamMap[resultCnt].put("preoutGb","00");
			
			if(orderStockOut[j].getProblemGoodsCode() == null){
				//= 주문가능
				resultParamMap[resultCnt].put("orderPossYn","1");
				useCnt ++;
			} else {
				//= 재고부족 or 판매중단으로 인한 주문불가.
				resultParamMap[resultCnt].put("orderPossYn","0");
			}
			resultCnt++;
		}
		
		if(useCnt < 1){
			//= 주문가능 상품이 하나도 없을 경우 return null;
			resultParamMap = null;
		}
		
        return resultParamMap;
	}
	
	/**
	 * checkOrderStock 	//= 재고체크
	 * @param OrderStockInVO[], OrderStockOutVO[] 
	 * @return void
	 * @throws Exception
	 */
	public void checkOrderStock( OrderStockInVO[] orderStockIn, OrderStockOutVO[] orderStockOut ) throws Exception{
        int             executedRtn      = 0;
        String          rtnMsg           = "000000";
        String 			saleGb 		 = "";
        OrderStockInVO  regOrderStockIn  = null;

        try{
            log.info("[]------------- Order Stock : Start --------------[]");

            log.info("//-- calculation order stock");
            for( int i=0; i < orderStockIn.length; i++){
                regOrderStockIn  = orderStockIn[i];
                
                saleGb = paorderDAO.selectGoodsSaleGb(regOrderStockIn);
                if(saleGb.equals("00")){ //= 상품,단품의 판매상태(SALE_GB)가 정상일때만 재고체크 함.
                	executedRtn = calcOrderStock(regOrderStockIn, orderStockOut[i]);
                } else { 
                	executedRtn = -4; //= 상품,단품의 판매상태(SALE_GB)가 판매불가 일 경우.
                }
                if( executedRtn < 0 ){
                	orderStockOut[i].setProblemGoodsCode(orderStockIn[i].getGoodsCode());
                	orderStockOut[i].setProblemGoodsDtCode(orderStockIn[i].getGoodsdtCode());
                	orderStockOut[i].setSaleGb(saleGb);
                    
                    log.info("//-- ProblemGoodsCode    ::"+orderStockIn[i].getGoodsCode());
                    log.info("//-- ProblemGoodsDtCode  ::"+orderStockIn[i].getGoodsdtCode());
                    log.info("//-- ProblemGoodsSaleGb  ::"+orderStockOut[i].getSaleGb());
                } else {
                	orderStockOut[i].setGoodsCode(orderStockIn[i].getGoodsCode());
                	orderStockOut[i].setGoodsdtCode(orderStockIn[i].getGoodsdtCode());
                	orderStockOut[i].setWhCode(orderStockIn[i].getWhCode());
                }
                
                if( executedRtn == -3 || executedRtn == -1){
                	log.info("errors.fail.stock");
                }
                
                if( executedRtn == -4){
                	log.info("errors.fail.non.sale.goods");
                }
            }  


            log.info("[]------------- Order Stock : End --------------[]");

        }catch(Exception e){
        	rtnMsg = getMessage("errors.fail.stock");
        	throw processMessageException(rtnMsg);
        }
        
        return ;
	}
	
	/**
	 * calcOrderStock 재고체크
	 * @param OrderStockInVO, OrderStockOutVO 
	 * @return int
	 * @throws Exception
	 */	
    @SuppressWarnings({ "unused", "rawtypes" })
	public int calcOrderStock(OrderStockInVO orderStockIn, OrderStockOutVO orderStockOut ) throws Exception {
        long        executedRtn     		= 0;
        String      stock_chk_place 		= "";
        long        cur_counsel_qty 		= 0;
        long        unit_cur_counsel_qty 	= 0;
        long        self_order      		= 0;
        long        counsel_qty     		= 0;
        long        stock_qty       		= 0;
        long        order_seq       		= 0;
        long        max_sale_qty    		= 0; //=판매가능한 최대 수량
        long        tot_sale_qty    		= 0; //=전체 주문 수량 (주문 - 취소)
        long        order_qty       		= 0; //=입금되지 않은 주문 수량
        long        out_plan_qty      		= 0; //=입금되고 아직 출하되지 않은 주문 수량/승인 + 출하지시 수량
        long        syslast         		= 0;
        long        cur_qty         		= 0;
        long        unit_cur_qty    		= 0;
        long        daily_capa_qty 			= 0;
        long        lead_time      			= 0;
        long        out_plan_cnt   			= 0;
        long      	stockQty            	= 0;
        long      	selfOrder           	= 0; //=curCounselQty + 현재 TM의 접수수량.
        long      	outPlanCnt          	= 0;
        String      baseDelyDay     		= "";
        long		delyAbleDay				= 0;
        Timestamp   firstPlanDate 		= null;
        Timestamp   outPlanDate   		= null;
        Timestamp   delyHopeDate  		= null;
        String      temp_goodsdt_code 		= "";
        ParamMap    resultMap         		= null;
        Counsel    	tcounsel       	 		= null;
        Timestamp 	dely_date_1         	= null;
        String stockMode = "1"; 			//=재고체크모드 : 1=실재고 체크
        OrderStockInVO 	orderWhCode			= null;
        HashMap   		resultHashMap       = new HashMap();
        String    buyMed              = "";
        String reservGoodsYn = "0";
		Timestamp reservOutDate = null; 
		int avgDelyLeadtime  = 0;		
		
		stockMode = systemService.getVal("STOCK_CHECK_MODE").substring(0,1);

        ParamMap paramMap = null;

        //=재고체크에 필요한 기본 정보 조회 : buy_med
        paramMap = new ParamMap();
        paramMap.put("goodsCode", orderStockIn.getGoodsCode());
		resultHashMap = paorderDAO.selectGoodsForStock(paramMap);
		buyMed = resultHashMap.get("BUY_MED").toString();
		avgDelyLeadtime = Integer.parseInt(resultHashMap.get("AVG_DELY_LEADTIME").toString());
		reservGoodsYn = resultHashMap.get("RESERV_GOODS_YN").toString();
		if (reservGoodsYn.equals("1")) {
			reservOutDate = DateUtil.toTime(resultHashMap.get("RESERV_OUT_DATE").toString());
		}
		resultHashMap = new HashMap();
		paramMap = null;

        //DateUtil.addDay(systemProcess.getSysdateToString(), (int)Constants.DELYDAY_DEFAULT_ADDDAY, DateUtil.CWARE_DATE_FORMAT);

        log.info("//---- check GOODS_CODE: " + orderStockIn.getGoodsCode() );
        log.info("//----       GOODSDT   : " + orderStockIn.getGoodsdtCode() );
        // #1---- Prepare to check orderstock
        stock_chk_place = orderStockIn.getStockChkPlace();
        counsel_qty     = orderStockIn.getCounselQty();

        log.info("//---- STOCK_CHK_PLACE : " + stock_chk_place );
        log.info("//---- COUNSEL_QTY     : " + counsel_qty );

        //= goods selection no
        //=후 결재 승인 시 콜된 경우에도 상품 선택 순번 다시 가져 온다.
        if ( orderStockIn.getGoodsSelectNo().equals("") ) {
            orderStockIn.setGoodsSelectNo(selectSequenceNo("GOODS_SELECT_NO") );
        }

//        //= Check TCOUNSEL
//		paramMap = new ParamMap();
//		paramMap.put("order_no", orderStockIn.getOrderNo());
//		paramMap.put("order_g_seq", orderStockIn.getOrderGSeq());
//		paramMap.put("order_d_seq", orderStockIn.getOrderDSeq());
//		paramMap.put("order_w_seq", orderStockIn.getOrderWSeq());
//        executedRtn = paorderDAO.selectCheckTcounsel(paramMap) ;
//
//        //= Delete TCOUNSEL
//        if (executedRtn == 1) {
//            log.info("//---- delete TCOUNSEL");
//    		paramMap = new ParamMap();
//    		paramMap.put("order_no", orderStockIn.getOrderNo());
//    		paramMap.put("order_g_seq", orderStockIn.getOrderGSeq());
//    		paramMap.put("order_d_seq", orderStockIn.getOrderDSeq());
//    		paramMap.put("order_w_seq", orderStockIn.getOrderWSeq());
//    		paramMap.put("wh_code", orderStockIn.getWhCode());
//            executedRtn = paorderDAO.deleteCounsel(paramMap) ;
//        }

        //= if the goods is a unselected gift, do not check and just return
        if ( orderStockIn.getSelectFlag().equals("0") ) {
            orderStockOut.setStockFlag("0");
            orderStockOut.setStockKey("0");
            orderStockOut.setOutPlanDate(null);
            return 0;
        }

        //배송지에 따른 DELY_GB, WH_CODE를 가져온다.
        paramMap = new ParamMap();
		paramMap.put("delyGb", 		orderStockIn.getDelyGb());
		paramMap.put("whCode", 		orderStockIn.getWhCode());
		paramMap.put("goodsCode", 	orderStockIn.getGoodsCode());
		paramMap.put("custNo", 		orderStockIn.getCustNo());
		paramMap.put("receiverSeq", orderStockIn.getReceiverSeq());
		paramMap.setParamMap(paorderDAO.selectGoodsCheckDelyGb(paramMap));

		orderStockIn.setDelyGb(paramMap.getString("DELY_GB"));
		orderStockIn.setWhCode(paramMap.getString("WH_CODE"));
		//////////////////////////////////////////////////////

        //= #2---- Retrieve the number of counsel
        //  CUR_COUNSEL_QTY : 현재 접수중인 TM보다 먼저 입력한 접수수량의 합
        //  SELF_ORDER      : Cur_Counsel_Qty + 현재 TM의 접수수량
        paramMap = new ParamMap();
		paramMap.put("goodsCode", 		orderStockIn.getGoodsCode());
		paramMap.put("goodsdtCode", 	orderStockIn.getGoodsdtCode());
		paramMap.put("goodsSelectNo", 	orderStockIn.getGoodsSelectNo());
		paramMap.put("whCode", 			orderStockIn.getWhCode());

        if( stock_chk_place.equals("1") ) {                                                     //= goods base
            cur_counsel_qty = paorderDAO.selectCurCounselQtyA(paramMap);
        } else if( stock_chk_place.equals("2") ) {                                              //= goodsdt base
            cur_counsel_qty = paorderDAO.selectCurCounselQtyB(paramMap);
        } else if( stock_chk_place.equals("3")) {          //= complex base and goodsdt_code = 000
            cur_counsel_qty      = paorderDAO.selectCurCounselQtyC(paramMap);
      	    unit_cur_counsel_qty = paorderDAO.selectCurCounselQtyD(paramMap);
        } else {
        	throw processMessageException(getMessage("errors.fail.cur_counsel_qty"));
        }
        self_order = cur_counsel_qty + counsel_qty;

        log.info("//---- CUR_COUNSEL_QTY : " + cur_counsel_qty );


        //= #3---- Check Max sale qty   */
        //  Retrieve
        //   MAX_SALE_QTY : 판매가능한 최대 수량
        //   TOT_SALE_QTY : 전체 주문 수량 (주문 - 취소)
        //   ORDER_QTY    : 입금되지 않은 주문 수량
        //   OUT_PLAN_QTY : 입금되고 아직 출하되지 않은 주문 수량 */
        resultMap = new ParamMap();
        paramMap  = new ParamMap();
		paramMap.put("goodsCode", 		orderStockIn.getGoodsCode());
		paramMap.put("goodsdtCode", 	orderStockIn.getGoodsdtCode());
		paramMap.put("whCode", 			orderStockIn.getWhCode());

        if( stock_chk_place.equals("1") ) {                                                     //= goods base
    		paramMap.setParamMap(paorderDAO.selectMaxSaleQtyA(paramMap));
    		resultMap.put("MAX_SALE_QTY", paramMap.getLong("MAX_SALE_QTY"));
    		resultMap.put("TOT_SALE_QTY", paramMap.getLong("TOT_SALE_QTY"));
    		resultMap.put("ORDER_QTY",    paramMap.getLong("ORDER_QTY"));
    		resultMap.put("OUT_PLAN_QTY", paramMap.getLong("OUT_PLAN_QTY"));

        } else if( stock_chk_place.equals("2") ) {                                              //= goodsdt base
    		paramMap.setParamMap(paorderDAO.selectMaxSaleQtyB(paramMap));
    		resultMap.put("MAX_SALE_QTY", paramMap.getLong("MAX_SALE_QTY"));
    		resultMap.put("TOT_SALE_QTY", paramMap.getLong("TOT_SALE_QTY"));
    		resultMap.put("ORDER_QTY",    paramMap.getLong("ORDER_QTY"));
    		resultMap.put("OUT_PLAN_QTY", paramMap.getLong("OUT_PLAN_QTY"));

        } else if( stock_chk_place.equals("3")) {                                                //= complex base
    		paramMap.setParamMap(paorderDAO.selectMaxSaleQtyC(paramMap));
    		resultMap.put("MAX_SALE_QTY", paramMap.getLong("MAX_SALE_QTY"));
    		resultMap.put("TOT_SALE_QTY", paramMap.getLong("TOT_SALE_QTY"));
    		resultMap.put("ORDER_QTY",    paramMap.getLong("ORDER_QTY"));
    		resultMap.put("OUT_PLAN_QTY", paramMap.getLong("OUT_PLAN_QTY"));

    		paramMap.setParamMap(paorderDAO.selectMaxSaleQtyB(paramMap));
    		resultMap.put("UNIT_MAX_SALE_QTY", paramMap.getLong("MAX_SALE_QTY"));
    		resultMap.put("UNIT_TOT_SALE_QTY", paramMap.getLong("TOT_SALE_QTY"));
    		resultMap.put("UNIT_ORDER_QTY",    paramMap.getLong("ORDER_QTY"));
    		resultMap.put("OUT_PUNIT_OUT_PLAN_QTYLAN_QTY", paramMap.getLong("OUT_PLAN_QTY"));

        } else {
        	throw processMessageException(getMessage("errors.fail.max_sale_qty"));
        }

        if( stock_chk_place.equals("3") ) {

      	  cur_qty      = resultMap.getLong("MAX_SALE_QTY")      - resultMap.getLong("TOT_SALE_QTY")      - cur_counsel_qty ;
      	  unit_cur_qty = resultMap.getLong("UNIT_MAX_SALE_QTY") - resultMap.getLong("UNIT_TOT_SALE_QTY") - unit_cur_counsel_qty;

      	  if( unit_cur_qty < cur_qty ){
      		resultMap.put("MAX_SALE_QTY", resultMap.getLong("UNIT_MAX_SALE_QTY"));
      		resultMap.put("TOT_SALE_QTY", resultMap.getLong("UNIT_TOT_SALE_QTY"));
      		resultMap.put("ORDER_QTY",    resultMap.getLong("UNIT_ORDER_QTY"));
      		resultMap.put("OUT_PLAN_QTY", resultMap.getLong("UNIT_OUT_PLAN_QTY"));
      		  cur_counsel_qty = unit_cur_counsel_qty;
      		  self_order = cur_counsel_qty + cur_counsel_qty;
      	  }
        }
        //=maxSaleQty : 판매가능한 최대 수량.
        max_sale_qty = resultMap.getLong("MAX_SALE_QTY");
        //=totSaleQty : 전체 주문 수량 (주문 - 취소).
        tot_sale_qty = resultMap.getLong("TOT_SALE_QTY");
        //=orderQty   : 입금되지 않은 주문 수량.
        order_qty    = resultMap.getLong("ORDER_QTY");
        //=outPlanQty : 입금되고 아직 출하되지 않은 주문 수량.
        out_plan_qty = resultMap.getLong("OUT_PLAN_QTY");
        log.info("//---- MAX_SALE_QTY    : " + max_sale_qty );
        log.info("//---- TOT_SALE_QTY    : " + tot_sale_qty );

        //= 입고예정수량산정정보가 없으면, 배송예정일 계산을 할 수 없으므로 RETURN
        if( resultMap.getString("MAX_SALE_QTY").equals("") || max_sale_qty == 0 ) {
            orderStockOut.setResultGeneral("-2");
            orderStockOut.setProblemRow( orderStockIn.getRow() );
            return -3;      //= change -1 >> -3
        }

        if (order_qty < 0 )   order_qty = 0;
        if (out_plan_qty < 0 ) out_plan_qty = 0;

        //= 기존 주문수량 조회
        /*if( stock_chk_place.equals("1") || stock_chk_place.equals("3") ) {
            syslast = 0;
    		paramMap = new ParamMap();
    		paramMap.put("orderNo",   orderStockIn.getOrderNo());
    		paramMap.put("orderGSeq", orderStockIn.getOrderGSeq());
    		paramMap.put("orderDSeq", orderStockIn.getOrderDSeq());
    		paramMap.put("orderWSeq", orderStockIn.getOrderWSeq());
            syslast = paorderDAO.selectSyslast(paramMap ) ;
            max_sale_qty = max_sale_qty + syslast;
        }*/

        //= order_seq ; (총 몇번째 주문인지 = 주문순번) => 출하되지 않은 주문수량 + 주문중인 주문수량 + 접수수량의 합
        order_seq = out_plan_qty + self_order + order_qty;

        //= cur_qty ; (판매가능수량) => 최대판매수량 - 총 주문(확정)수량 - 상담중인 수량
        cur_qty = max_sale_qty - tot_sale_qty - cur_counsel_qty ;

        /* 1. 판매가능수량 < 0         : 주문받을 수 없다.(F_TORDDT_STOCK_FLAG: "0")
           2. 판매가능수량 <  주문순번 : 판매가능수량 - order_qty - out_plan_qty
           3. 판매가능수량 >= 주문순번 : 계속진행.
        */
        if( cur_qty < 1 || order_seq < 1) {        //= Can not Sale
            cur_qty = max_sale_qty - tot_sale_qty;
            if( cur_qty < 0 ) cur_qty = 0;

            orderStockOut.setResultGeneral("-1");
            orderStockOut.setProblemRow( orderStockIn.getRow() );
            orderStockOut.setProblemQty( cur_qty );
            return -3;      //= change -1 >> -3

        } else if( cur_qty < counsel_qty ) {    //= Limited Sale  ; 주문접수한 수량보다 현재 주문가능한 수량이 작은경우 현재 받을 수 있는 수량까지만 받는다. 
        										//                    => SSG.COM 건인 경우 주문접수한 수량보다 주문가능 수량이 적으면 받지 않는다. by ksh.
            /*counsel_qty = cur_qty;
            self_order 	= cur_counsel_qty + counsel_qty;
            order_seq 	= out_plan_qty + self_order + order_qty ;*/
        	
        	return -3;
        }

        //= NO Countsel Qty
        if( counsel_qty < 1 ) {
            cur_qty = max_sale_qty - tot_sale_qty;
            if( cur_qty < 0 ) cur_qty = 0;

            orderStockOut.setResultGeneral("-1");
            orderStockOut.setProblemRow( orderStockIn.getRow() );
            orderStockOut.setProblemQty( cur_qty );
            return -3;      //= change -1 >> -3
        }
        
        // 상담수량을 잡지 않는다.
        //= #4---- Insert TCOUNSEL   */

        //=배달지에 따른 배송소요일 SELECT. :: 최종 : DELY_GB 는 비교하지 않고 POST_NO 만 이용해서 배송소요일 계산한다.
        paramMap = new ParamMap();
        paramMap.put("postNo", orderStockIn.getPostNo());
        //delyAbleDay = orderDAO.selectDeliveryPointCount(paramMap); //= 
		String dateTime = getSysdatetimeToString();
		outPlanDate = DateUtil.toTimestamp(dateTime, "yy/MM/dd");	

		/**
        * 실재고 체크 : 당사 배송, 업체배송(직사입 타창고), 업체배송(실재고체크 : STOCK_CHECK_MODE='1') 
        * 배송예정일 산출 기준 : 주문 접수일 + 출고작업일( 0 or 1) + 배송소요일
        * 
        * 직매입(당사+업체) , 위탁입고 -- 
       */
		
		
		
		if( "20".equals(orderStockIn.getDelyType())){ //업체배송
			if(!"13".equals(buyMed)){  // 직사입 타창고
				orderStockIn.setWhCode(ConfigUtil.getString("ENTP_WH_CODE"));  					
			}					
		}
		
		
		
		ParamMap delyGbMap = null;
	    delyGbMap = new ParamMap();
	    delyGbMap.put("delyGb", orderStockIn.getDelyGb());
	    delyGbMap.put("whCode", orderStockIn.getWhCode());
	    delyGbMap.put("goodsCode", orderStockIn.getGoodsCode());
	    delyGbMap.put("custNo", orderStockIn.getCustNo());
	    delyGbMap.put("receiverSeq", orderStockIn.getReceiverSeq());
	    delyGbMap.put("RESULT_GOODS_CHECK_DELY_GB", paorderDAO.getGoodsCheckDelyGb(delyGbMap));
	    
	    orderStockIn.setWhCode(((HashMap)((List)delyGbMap.get("RESULT_GOODS_CHECK_DELY_GB")).get(0)).get("WH_CODE").toString());
	    orderStockIn.setDelyGb(((HashMap)((List)delyGbMap.get("RESULT_GOODS_CHECK_DELY_GB")).get(0)).get("DELY_GB").toString());

	    
	    String roadAddrNo = "";
	    String postNo     = "";
	    
	    if (orderStockIn.getRoadAddrNo() != null){
	    	roadAddrNo = orderStockIn.getRoadAddrNo();
	    }
	    if (orderStockIn.getStdPostNo() != null){
	    	postNo = orderStockIn.getStdPostNo();
	    }
	    
	    paramMap = new ParamMap();
        paramMap.put("roadAddrNo", roadAddrNo);
        paramMap.put("postNo",     postNo);
        
        delyAbleDay = paorderDAO.getDeliveryPointCount(paramMap); 
        //delyAbleDay = 1; 

		
        //직 매입 타창고분	
        if("10".equals(orderStockIn.getDelyGb())  || buyMed.equals("13") || 
        		( ("20").equals(orderStockIn.getDelyType()) && "1".equals(stockMode) ) ){
        		
        	
            paramMap 	= new ParamMap();
            resultMap 	= new ParamMap();
            paramMap.put("whCode", 			orderStockIn.getWhCode());
            paramMap.put("goodsCode",		orderStockIn.getGoodsCode());
            paramMap.put("goodsdtCode", 	orderStockIn.getGoodsdtCode());
            paramMap.put("orderSeq", 		order_seq);
            paramMap.put("stockChkPlace", 	stock_chk_place);

            paramMap.put("RESULT_CHECK", paorderDAO.selectRealStock(paramMap));
            if(paramMap.get("RESULT_CHECK") == null){
            	executedRtn = 0;
            }else{
            	executedRtn = 1;
            	stockQty  = ComUtil.objToLong(((HashMap)paramMap.getObject("RESULT_CHECK")).get("STOCK_QTY"));
                selfOrder = ComUtil.objToLong(((HashMap)paramMap.getObject("RESULT_CHECK")).get("SELF_ORDER"));
            }

            if( executedRtn == 1 ) {
            	/**
            	 * 출하예정일 : 주문 접수 일이 (토요일이면 12시 / 평일 이면 15시) 이전이면 당일 작업 , 이후이면 +1일 을 하여 산정한다.
            	 * 출하예정일 : 주문접수일 + 출고작업일(tconfig DELY_HOPE_TIME_SET 기준 + tconfig DELY_HOPE_DATE_ADD_DATE) - 2017/06/21
            	 * 출하예정일 = 최초출하예정일 - 2017/06/21
            	 */
            	
            	outPlanDate = DateUtil.toTimestamp(dateTime, "yy/MM/dd");	
            	outPlanCnt = paorderDAO.getOutPlanCnt(); //주문 접수 일이 (토요일이면 12시 / 평일 이면 14시) 이전이면 당일 작업, 이후이면 +1일 을 하여 산정한다.
            	
            	
            	
            	
            	paramMap = new ParamMap();
                paramMap.put("delyGb", orderStockIn.getDelyGb());
                paramMap.put("workYn", "1");
                paramMap.put("dataNum", 4);
                paramMap.put("outPlanDate", outPlanDate);
                paramMap.put("outPlanCnt", outPlanCnt);
                executedRtn = getDelyday( paramMap );           
                
                
                if( executedRtn == -1 ) {
                	orderStockOut.setStockFlag("-3");
                    orderStockOut.setResultGeneral(orderStockOut.getStockFlag());
                    orderStockOut.setProblemRow( orderStockIn.getRow() );
                    orderStockOut.setProblemQty(0);
                    return -1;
                }
                
                dely_date_1 = DateUtil.toTime(paramMap.getString("DELY_DATE_1"));
                outPlanDate = dely_date_1;
                outPlanCnt = delyAbleDay ;
                
                
                paramMap = new ParamMap();
                paramMap.put("delyGb", orderStockIn.getDelyGb());
                paramMap.put("workYn", "1");
                paramMap.put("dataNum", 4);
                paramMap.put("outPlanDate", outPlanDate);
                paramMap.put("outPlanCnt", outPlanCnt);
                executedRtn = getDelyday( paramMap );
               
                
                if( executedRtn == -1){
                    orderStockOut.setStockFlag("-4");
                    orderStockOut.setResultGeneral(orderStockOut.getStockFlag());
                    orderStockOut.setProblemRow(orderStockIn.getRow());
                    return -1;
                }

                dely_date_1 = DateUtil.toTime(paramMap.getString("DELY_DATE_1"));

                firstPlanDate = dely_date_1;
                delyHopeDate = dely_date_1;

                
                if( selfOrder >= 0 ) {
                	orderStockOut.setStockFlag("1");//=실재고에 의해서 가저옴.
                	orderStockOut.setResultGeneral(orderStockOut.getStockFlag());
                	orderStockOut.setStockKey("");
                	orderStockOut.setFirstPlanDate	(firstPlanDate);
                	orderStockOut.setOutPlanDate	(outPlanDate);
                	orderStockOut.setDelyHopeDate	(delyHopeDate);
                	orderStockOut.setFromDate		(DateUtil.toString(dely_date_1, "yyyy/MM/dd"));
                	orderStockOut.setToDate			(DateUtil.toString(dely_date_1, "yyyy/MM/dd"));
                	orderStockOut.setOrderQty		(counsel_qty);
                	orderStockOut.setGoodsSelectNo	(orderStockIn.getGoodsSelectNo());
                	orderStockOut.setOrderNo		(orderStockIn.getOrderNo());
                	orderStockOut.setOrderGSeq	(orderStockIn.getOrderGSeq());
                	orderStockOut.setOrderDSeq	(orderStockIn.getOrderDSeq());
                	orderStockOut.setOrderWSeq	(orderStockIn.getOrderWSeq());
                	orderStockOut.setDelyGb(orderStockIn.getDelyGb());
                	orderStockOut.setWhCode(orderStockIn.getWhCode());
                    orderStockOut.setrDelyGb(orderStockIn.getDelyGb());  //= 주문당시건과 동일하게 설정
                    orderStockOut.setrWhCode(orderStockIn.getWhCode());  //= 주문당시건과 동일하게 설정
                    return 0;
                }else{
                	stockQty = 0;//=VSTOCK TABLE 에 자료가 없는 경우.
                }
            }else{
            	stockQty = 0;//=재고 정보가 없는 경우
            }
        }

    	//=당사배송, 직사입타창고분 의 실재고 주문가능수량이 없고 조달계획에 자료가 없다면 return.        	
    	if ((buyMed.substring(1,2).equals("1") || buyMed.equals("13")) && stockQty == 0) {	
    		
        	orderStockOut.setStockFlag("-3");
            orderStockOut.setResultGeneral(orderStockOut.getStockFlag());
            orderStockOut.setProblemRow( orderStockIn.getRow() );
            orderStockOut.setProblemQty(0);
            return -1;
    	}

    	
    	
    	temp_goodsdt_code = orderStockIn.getGoodsdtCode();
        if( stock_chk_place.equals("1") && !orderStockIn.getGoodsdtCode().equals("000") ){
            temp_goodsdt_code = "000";
        } else if( stock_chk_place.equals("3") && orderStockIn.getGoodsdtCode().equals("000") ) {
            temp_goodsdt_code = paorderDAO.getTargetGoodsdt( orderStockIn.getGoodsCode() );
        }

        //= TINPLANQTY 조회 DAILY_CAPA_QTY, LEAD_TIME
        paramMap = new ParamMap();
        paramMap.put("goodsCode", 		orderStockIn.getGoodsCode());
        paramMap.put("goodsdtCode", 	temp_goodsdt_code);
        paramMap.put("orderSeq", 		order_seq);
        paramMap.put("stockQty", 		stock_qty);
        paramMap.put("RESULT_CHECK", 	paorderDAO.getSupplyCapa(paramMap));
        
        
        if(paramMap.get("RESULT_CHECK") == null){
        	executedRtn = 0;
        }else{
        	executedRtn = 1;
        }
        if( executedRtn == 0 ) {
        	orderStockOut.setStockFlag("-3");
            orderStockOut.setResultGeneral(orderStockOut.getStockFlag());
            orderStockOut.setProblemRow( orderStockIn.getRow() );
            orderStockOut.setProblemQty(0);
            return -1;
        }

        lead_time = 1;
        outPlanDate = DateUtil.toTimestamp(dateTime, "yy/MM/dd");
        outPlanCnt = lead_time;
        
        
        orderStockOut.setStockFlag("3"); //=업체 CAPA 에서 가져옴.

    	
        paramMap = new ParamMap();
        paramMap.put("delyGb",orderStockIn.getDelyGb());
        paramMap.put("workYn","1");
        paramMap.put("dataNum",4);
        paramMap.put("outPlanDate",outPlanDate);
        paramMap.put("outPlanCnt",outPlanCnt);
        executedRtn=getDelyday(paramMap);
        
        
        
        if( executedRtn == -1){
            orderStockOut.setStockFlag("-3");
            orderStockOut.setResultGeneral(orderStockOut.getStockFlag());
            orderStockOut.setProblemRow(orderStockIn.getRow());
            return -1;
        }
        
        
        dely_date_1 = DateUtil.toTime(paramMap.getString("DELY_DATE_1"));
        outPlanDate   = dely_date_1;
        
        
        outPlanCnt = delyAbleDay;
        paramMap=new ParamMap();
        paramMap.put("delyGb",orderStockIn.getDelyGb());
        paramMap.put("workYn","1");
        paramMap.put("dataNum",4);
        paramMap.put("outPlanDate",outPlanDate);
        paramMap.put("outPlanCnt",outPlanCnt);
        executedRtn=getDelyday(paramMap);

        if( executedRtn == -1){
            orderStockOut.setStockFlag("-4");
            orderStockOut.setResultGeneral(orderStockOut.getStockFlag());
            orderStockOut.setProblemRow(orderStockIn.getRow());
            return -1;
        }
       
       
        dely_date_1 = DateUtil.toTime(paramMap.getString("DELY_DATE_1"));
        firstPlanDate = dely_date_1;
        delyHopeDate  = dely_date_1;

        
        orderStockOut.setResultGeneral(orderStockOut.getStockFlag());
        orderStockOut.setStockKey("");
        orderStockOut.setFirstPlanDate	(firstPlanDate);
        orderStockOut.setOutPlanDate	(outPlanDate);
        orderStockOut.setDelyHopeDate	(delyHopeDate);
        orderStockOut.setFromDate		(DateUtil.toString(dely_date_1, "yyyy/MM/dd"));
        orderStockOut.setToDate			(DateUtil.toString(dely_date_1, "yyyy/MM/dd"));
        orderStockOut.setOrderQty		(counsel_qty);
        
        orderStockOut.setGoodsSelectNo	(orderStockIn.getGoodsSelectNo());
        orderStockOut.setOrderNo		(orderStockIn.getOrderNo());
        orderStockOut.setOrderGSeq		(orderStockIn.getOrderGSeq());
        orderStockOut.setOrderDSeq		(orderStockIn.getOrderDSeq());
        orderStockOut.setOrderWSeq		(orderStockIn.getOrderWSeq());
        orderStockOut.setDelyGb			(orderStockIn.getDelyGb());
        orderStockOut.setWhCode			(orderStockIn.getWhCode());
        orderStockOut.setrDelyGb		(orderStockIn.getDelyGb());  //= 주문당시건과 동일하게 설정
        orderStockOut.setrWhCode		(orderStockIn.getWhCode());  //= 주문당시건과 동일하게 설정
        return 0;
    }
    
	/**
	 * getDelyday 배송예정일 계산
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
    @SuppressWarnings({ "rawtypes", "unused" })
    private int getDelyday( ParamMap paramMap ) throws Exception {
    	int         executedRtn    = 0;
		List  rtnList = new ArrayList();
		
		paramMap.put("RESULT_CHECK",  paorderDAO.getDelyday(paramMap));
		
		   //= tdelyday 의 데이터가 없을 경우 주문은 정상적으로 저장 처리 하기 위해서 휴일 관리를 무시하고 sysdate 기준으로 데이터를 조회한다.        
		if( paramMap.get("RESULT_CHECK") == null || ((List)paramMap.get("RESULT_CHECK")).size() < 4 ){
			log.error("## tdelyday have no valid data. that check is skipped. However, the data must be checked.");
			paramMap.put("RESULT_CHECK",  paorderDAO.getDelydayExcept(paramMap));
		}        
		   
		if(paramMap.get("RESULT_CHECK") == null){
			paramMap.put("DELY_DATE_1", "");
			paramMap.put("DELY_DATE_2", "");
			paramMap.put("DELY_DATE_3", "");
			paramMap.put("DELY_DATE_4", "");
			executedRtn = -1;
		}else{
			if(((List)paramMap.get("RESULT_CHECK")).size() < 4){
				executedRtn = -1;
			}else{
				//=출하예정일                  : 기준일자 + 1
				//=배송예정일(서울1): 기준일자 + 2
				//=배송예정일(서울2): 기준일자 + 3
				//=배송예정일(지방) : 기준일자 + 4
				paramMap.put("DELY_DATE_1", ((HashMap)((List)paramMap.get("RESULT_CHECK")).get(0)).get("DELYDAY"));
				paramMap.put("DELY_DATE_2", ((HashMap)((List)paramMap.get("RESULT_CHECK")).get(1)).get("DELYDAY"));
				paramMap.put("DELY_DATE_3", ((HashMap)((List)paramMap.get("RESULT_CHECK")).get(2)).get("DELYDAY"));
				paramMap.put("DELY_DATE_4", ((HashMap)((List)paramMap.get("RESULT_CHECK")).get(3)).get("DELYDAY"));
				executedRtn = 1;
			}
		}
		return executedRtn;
    }
    
	/**
	 * FUN_GET_CUST_SHIPCOST 
	 * @param arg_order_gb, arg_date, arg_cust_no, arg_receiver_seq, arg_goods_str, arg_amt_str, arg_qty_str
	 * @return int
	 * @throws Exception
	 */
	//=CART 배송비 총합 FUNC 자동 계산 (FUN_GET_CUST_SHIPCOST)
	@SuppressWarnings("rawtypes")
	public String selectCalShpfeeAmt( String arg_order_gb , String arg_date, String arg_cust_no , String arg_receiver_seq ,String arg_goods_str, String arg_amt_str, String arg_qty_str) throws Exception{
		ParamMap paramMap = new ParamMap();
		paramMap.put("argOrderGb", arg_order_gb);
		paramMap.put("argDate", arg_date);
		paramMap.put("argCustNo", arg_cust_no);
		paramMap.put("argReceiverSeq", arg_receiver_seq);
		paramMap.put("argGoodsStr", arg_goods_str);
		paramMap.put("argAmtStr", arg_amt_str);
		paramMap.put("argQtyStr", arg_qty_str);
		
		HashMap rtnMap = new HashMap();

		rtnMap = paorderDAO.selectCalShpfeeAmt(paramMap);
		if(rtnMap == null || rtnMap.get("RESULT_AMT").equals("")) return "";

		return rtnMap.get("RESULT_AMT").toString();
	}
	
	/**
	 * system 시간조회
	 * @param null
	 * @return String
	 * @throws Exception
	 */
	@Override
    public String getSysdatetimeToString() throws Exception {
    	return paorderDAO.getSysdatetime();
    }
	
	/**
	 * updateOrderGoods
	 * @param OrdergoodsVO
	 * @return int
	 * @throws Exception
	 */
	private int updateOrderGoods(OrdergoodsVO ordergoods) throws Exception {
		int executedRtn = 0;
		OrdergoodsVO regOrdergoods = null;
		
		if(ordergoods != null){
			regOrdergoods = ordergoods;
			
			log.info("//---- update TORDERGOODS");
			executedRtn = paorderDAO.updateOrdergoods(regOrdergoods);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERGOODS UPDATE"});
			}
		}

		return executedRtn;
	}
	
	private int updateOrderGoods(List<OrdergoodsVO> ordergoods) throws Exception {
		int executedRtn = 0;
		int rowCnt = 0;
		rowCnt = ordergoods.size();
		OrdergoodsVO reOrdergoodsVo = null;
				
		for (int i = 0; i < rowCnt; i++) {
			reOrdergoodsVo = ordergoods.get(i);
			executedRtn =  updateOrderGoods(reOrdergoodsVo);		
			
		}		
		return executedRtn;
	}
	
	
	private int saveCanceldt(List<CanceldtVO> canceldtVO) throws Exception {
		
		int executedRtn = 0;
		int rowCnt = 0;
		rowCnt = canceldtVO.size();
		CanceldtVO reCanceldtvo = null;
				
		for (int i = 0; i < rowCnt; i++) {
			reCanceldtvo = canceldtVO.get(i);
			executedRtn =  saveCanceldt(reCanceldtvo);			
		}		
		return executedRtn;		
	}
	
	/**
	 * newCanceldt
	 * @param Canceldt
	 * @return int
	 * @throws Exception
	 */
	private int saveCanceldt(CanceldtVO canceldtVO) throws Exception {
		int executedRtn = 0;

		CanceldtVO regCanceldt = null;
		if(canceldtVO != null){
			regCanceldt = canceldtVO;
			
			log.info("//---- update TORDERDT");
			executedRtn = paorderDAO.updateOrderdtSyslast(regCanceldt);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERDT UPDATE"});
			}
			
			log.info("//---- insert TCANCELDT");
			executedRtn = paorderDAO.insertCanceldt(regCanceldt);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] {"TCANCELDT INSERT"});
			}
			
			// = 최대판매가능수량 차감처리
			/*log.info("//---- update TINPLANQTY");
			executedRtn = paorderDAO.updateInplanqty(regCanceldt);*/
		}

		return executedRtn;
	}
	
	/**
	 * insertClaimdt
	 * @param Canceldt
	 * @return int
	 * @throws Exception
	 */
	private int insertClaimdt(ClaimdtVO claimdt) throws Exception {
		int executedRtn = 0;
		ClaimdtVO regClaimdt = null;
		
		if(claimdt != null){
			log.info("//---- insert TCLAIMDT");
			regClaimdt = claimdt;
			
			executedRtn = paorderDAO.insertClaimdt(regClaimdt);
			
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] {"insertClaimdt Error"});
			}
		}
		
		return executedRtn;
	}
	
	
	
	private int insertClaimdt(List<ClaimdtVO> claimdt) throws Exception {
		
		int executedRtn = 0;
		int rowCnt = 0;
		rowCnt = claimdt.size();
		ClaimdtVO reClaimdtvo = null;
		
		
		for (int i = 0; i < rowCnt; i++) {
			reClaimdtvo = claimdt.get(i);
			executedRtn =  insertClaimdt(reClaimdtvo);			
		}
		
		return executedRtn;
	}
	
	/**
	 * newReceipts
	 * @param Orderreceipts
	 * @return int
	 * @throws Exception
	 */
	private int insertReceiptsCancel(ArrayList<Orderreceipts> orderreceiptsList, OrdershipcostVO[] ordershipcost) throws Exception {
		int executedRtn = 0;
		
		double sumQuestAmt10 = 0;
		double sumQuestAmt20 = 0;
			
		for(Orderreceipts regOrderreceipts : orderreceiptsList){
			log.info("//---- insert TORDERRECEIPTS");
			executedRtn = paorderDAO.insertOrderreceipts(regOrderreceipts);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERRECEIPTS INSERT"});
			}
		}
			
		//= 정산배송비 INSERT
		//= 주문(취소)배송비 합산.
		for(OrdershipcostVO vo :  ordershipcost ){
			if(vo.getType().equals("10")){ //= 추가 배송비
				sumQuestAmt10 = sumQuestAmt10 + vo.getShpfeeCost();
			} else { //= 취소 배송비
				sumQuestAmt20 = sumQuestAmt20 + vo.getShpfeeCost();
			}
		}
				
		if(sumQuestAmt10 > 0){//= 추가 배송비
			Orderreceipts regOrderreceipts = (Orderreceipts) orderreceiptsList.get(0).clone();
			regOrderreceipts.setBlank();
			
			regOrderreceipts.setReceiptNo(selectSequenceNo("RECEIPT_NO"));
			regOrderreceipts.setSettleGb("15");
			regOrderreceipts.setDoFlag("90");
			regOrderreceipts.setQuestAmt(sumQuestAmt10);
			regOrderreceipts.setRepayPbAmt(sumQuestAmt10);
			regOrderreceipts.setReceiptAmt(sumQuestAmt10);
			regOrderreceipts.setReceiptDate(regOrderreceipts.getProcDate());
			regOrderreceipts.setPartialCancelYn("0");
				
			log.info("//---- insert TORDERRECEIPTS");
			executedRtn = paorderDAO.insertOrderreceipts(regOrderreceipts);
			
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERRECEIPTS INSERT"});
			}
			orderreceiptsList.add(regOrderreceipts);
		}
			
		if(sumQuestAmt20 > 0){ //= 취소 배송비
			Orderreceipts regOrderreceipts = (Orderreceipts) orderreceiptsList.get(0).clone();
			regOrderreceipts.setBlank();
			
			regOrderreceipts.setReceiptNo(selectSequenceNo("RECEIPT_NO"));
			regOrderreceipts.setSettleGb("65");
			regOrderreceipts.setDoFlag("90");
			regOrderreceipts.setQuestAmt(sumQuestAmt20);
			regOrderreceipts.setRepayPbAmt(0);
			regOrderreceipts.setReceiptAmt(0);
			regOrderreceipts.setPartialCancelYn("0");
			
			log.info("//---- insert TORDERRECEIPTS");
			executedRtn = paorderDAO.insertOrderreceipts(regOrderreceipts);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERRECEIPTS INSERT"});
			}
			orderreceiptsList.add(regOrderreceipts);
		}
		
		return executedRtn;
	}
	
	private int insertReceiptsCancel10(ArrayList<Orderreceipts> orderreceiptsList, OrdershipcostVO[] ordershipcost) throws Exception{
		
		double orgShipCost = 0;
		double sumQuestAmt10 = 0;
		double sumQuestAmt20 = 0;
		double resultShipAmt = 0;
		int	   executedRtn	 = 0;
		
		//= 기존 배송비 Keep
		orgShipCost = paorderDAO.selectGetOrgShipCost(orderreceiptsList.get(0).getOrderNo());
		
		//= 기존 Receipt 데이터 모두 Cancel
		updateOrderReceiptCancel(orderreceiptsList); 
		
		//= 새로 계산한 Receipt Insert
		for(Orderreceipts regOrderreceipts : orderreceiptsList){
			if(regOrderreceipts.getQuestAmt() > 0){
				executedRtn = paorderDAO.insertOrderreceipts(regOrderreceipts);
				if (executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] {"TORDERRECEIPTS INSERT"});
				}
			}
		}	
		//= 배송비 재 계산
		for(Ordershipcost co : ordershipcost){
			if(co.getType().equals("10")){ //= 추가 배송비
				sumQuestAmt10 = sumQuestAmt10 + co.getShpfeeCost();
			} else { //= 취소 배송비
				sumQuestAmt20 = sumQuestAmt20 + co.getShpfeeCost();
			}
		}
			
		resultShipAmt = orgShipCost + sumQuestAmt10 - sumQuestAmt20;

		//배송비 재 계산 분 Insert
		if(resultShipAmt > 0){ 
			Orderreceipts regOrderreceipts = (Orderreceipts) orderreceiptsList.get(0).clone();
			regOrderreceipts.setBlank();
			
			regOrderreceipts.setReceiptNo		(selectSequenceNo("RECEIPT_NO"));
			regOrderreceipts.setSettleGb		("15");
			regOrderreceipts.setDoFlag			("10");
			regOrderreceipts.setQuestAmt		(resultShipAmt);
			regOrderreceipts.setRepayPbAmt		(resultShipAmt);
			regOrderreceipts.setReceiptAmt		(0);
			regOrderreceipts.setEndYn			("0");
			regOrderreceipts.setPartialCancelYn	("0");
				
			log.info("//---- insert TORDERRECEIPTS");
			executedRtn = paorderDAO.insertOrderreceipts(regOrderreceipts);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERRECEIPTS INSERT"});
			}
			orderreceiptsList.add(regOrderreceipts);
		}

		return executedRtn;
	}
	
	
	/**
	 * modifiedReceipts
	 * @param Orderreceipts
	 * @return int
	 * @throws Exception
	 */
	private int updateReceipts(ArrayList<Orderreceipts> orderreceiptsList) throws Exception {
		int executedRtn = 0;
		List<Orderreceipts> orgReceiptsList = null;
		Orderreceipts regOrderreceipts = null;
		
		
	    for(Orderreceipts orderreceipts : orderreceiptsList){			
			if(orderreceipts.getSettleGb().equals("61")) orderreceipts.setSettleGb("11");
			else if(orderreceipts.getSettleGb().equals("65")) orderreceipts.setSettleGb("15"); 
			else if(orderreceipts.getSettleGb().equals("15")) continue;
			else if(orderreceipts.getSettleGb().equals("11")) continue;
			else throw processMessageException("Invalid settleGb Code");
				
			//= 정산배송비(14)의 경우 여러행이 있을 수 있으므로 repay_pb_amt 를 차감할 금액을 계산하여 update 함.
			orgReceiptsList = paorderDAO.selectOrderRepayPbAmt(orderreceipts);
				
			double tempAmt = 0;
				
			if(orgReceiptsList.size() > 0){
				if(orgReceiptsList.size() == 1){
					regOrderreceipts = new Orderreceipts();
					regOrderreceipts.setReceiptNo(orgReceiptsList.get(0).getReceiptNo());
					regOrderreceipts.setSettleGb(orderreceipts.getSettleGb());
					regOrderreceipts.setOrderNo(orderreceipts.getOrderNo());
					regOrderreceipts.setQuestAmt(orderreceipts.getQuestAmt());
					
					log.info("//---- update TORDERRECEIPTS");
					executedRtn = paorderDAO.updateReceiptsRepay(regOrderreceipts);
					if (executedRtn != 1) {
						throw processException("msg.cannot_save", new String[] {"TORDERRECEIPTS UPDATE"});
					}
				} else {
					tempAmt = orderreceipts.getQuestAmt();
					//procPbAmt = 0;
					boolean compYn = false;
					
					for(int k = 0; orgReceiptsList.size() > k; k++){
						if(tempAmt == orgReceiptsList.get(k).getRepayPbAmt()){ //= 동일한 잔여 금액이 있을 경우.
							regOrderreceipts = new Orderreceipts();
							regOrderreceipts.setReceiptNo(orgReceiptsList.get(k).getReceiptNo());
							regOrderreceipts.setSettleGb(orderreceipts.getSettleGb());
							regOrderreceipts.setOrderNo(orderreceipts.getOrderNo());
							regOrderreceipts.setQuestAmt(tempAmt);
							
							log.info("//---- update TORDERRECEIPTS");
							executedRtn = paorderDAO.updateReceiptsRepay(regOrderreceipts);
							if (executedRtn != 1) {
								throw processException("msg.cannot_save", new String[] {"TORDERRECEIPTS UPDATE"});
							}
							compYn = true;
							break;
						}
					}
						
					if(!compYn){
						for(int j = 0; orgReceiptsList.size() > j; j++){ //= 동일한 잔여 금액이 없을 경우.
							if(tempAmt == 0) break;
								
							regOrderreceipts = new Orderreceipts();
							regOrderreceipts.setReceiptNo(orgReceiptsList.get(j).getReceiptNo());
							regOrderreceipts.setSettleGb(orderreceipts.getSettleGb());
							regOrderreceipts.setOrderNo(orderreceipts.getOrderNo());
							
							if(tempAmt > orgReceiptsList.get(j).getRepayPbAmt()){
								tempAmt = orderreceipts.getQuestAmt() - orgReceiptsList.get(j).getRepayPbAmt();
								regOrderreceipts.setQuestAmt(orgReceiptsList.get(j).getRepayPbAmt());
							} else {
								regOrderreceipts.setQuestAmt(tempAmt);
								tempAmt = 0;
							}
							
							log.info("//---- update TORDERRECEIPTS");
							executedRtn = paorderDAO.updateReceiptsRepay(regOrderreceipts);
							if (executedRtn != 1) {
								throw processException("msg.cannot_save", new String[] {"TORDERRECEIPTS UPDATE"});
							}
						}
					}
				}
			}else {
				throw processMessageException("TORDERRECEIPTS update invalid target");
			}
	    }
				
		return executedRtn;
	}
	
	/**
	 * modifiedOrderpromo
	 * @param Orderpromo
	 * @return int
	 * @throws Exception
	 */
	private int updateOrderpromo(List<Orderpromo> orderPromoList, ParamMap paramMap) throws Exception {
		int executedRtn = 0;		
		
		Promocounsel promoCounsel = null;
		String limit_yn = "0";
		long orgSyslast = paramMap.getLong("orgSyslast");
		long cancelQty  = paramMap.getLong("cancelQty");
		

		for(Orderpromo regOrderpromo :  orderPromoList){
			if(orgSyslast == cancelQty){
				//= 전체취소일 경우에만 TORDERPROMO.CANCEL_YN = '1' UPDATE
				log.info("//---- update TORDERPROMO");
				executedRtn = paorderDAO.update(regOrderpromo);
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] {"TORDERPROMO UPDATE"});
				}
			}
			
			limit_yn = paorderDAO.getLimitYn(regOrderpromo.getPromoNo()); //= 한정 프로모션 Check
			// = 한정인 프로모션일 경우 프로모션 취소수량을 저장한다. ------------
			if (limit_yn.equals("1")) {
				log.info("//---- update TORDERPROMO");
				promoCounsel = new Promocounsel();
				
				promoCounsel.setPromoNo(regOrderpromo.getPromoNo());
				promoCounsel.setOrderNo(regOrderpromo.getOrderNo());
				promoCounsel.setOrderGSeq(regOrderpromo.getOrderGSeq());
				promoCounsel.setGoodsSelectNo(selectSequenceNo("GOODS_SELECT_NO"));
				promoCounsel.setCounselQty(cancelQty * -1);
				promoCounsel.setInsertId(regOrderpromo.getCancelId());
				promoCounsel.setInsertDate(regOrderpromo.getCancelDate());
					
				executedRtn = paorderDAO.insertPromoCounsel(promoCounsel);
				if (executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] {"TPROMOCOUNSEL INSERT"});
				}
			}
		}	
		
		return executedRtn;
	}	

	/**
	 * newOrdershipcost
	 * @param Ordershipcost
	 * @return int
	 * @throws Exception
	 */
	private int insertOrdershipcost(Ordershipcost[] ordershipcost) throws Exception {
		int executedRtn = 0;
		Ordershipcost regOrdershipcost = null;
		
		for(int i = 0; ordershipcost.length > i; i++){
			log.info("//---- insert TORDERSHIPCOST");
			regOrdershipcost = ordershipcost[i];
			
			executedRtn = paorderDAO.insertOrdershipcost(regOrdershipcost);
			
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERSHIPCOST INSERT"});
			}
		}
		return executedRtn;
	}
	
	
	private int insertOrderCostApply(Ordercostapply[] ordercostapply) throws Exception {
		int executedRtn = 0;
		
		for (Ordercostapply regOrderCostApply : ordercostapply) {
			log.info("//---- insert Tordershipcost");
			
			if ("I".equals(regOrderCostApply.getCwareAction())){
				executedRtn = paorderDAO.insertOrdercostapply(regOrderCostApply);
			}else if("U".equals(regOrderCostApply.getCwareAction())){
				executedRtn = paorderDAO.updateOrdercostapply(regOrderCostApply);
			}
		
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] {"TORDERCOSTAPPLY INSERT"});
			}
		}
		return executedRtn;
	}
	
	
	/**
	 * updateStockClaim
	 * @param Canceldt
	 * @return int
	 * @throws Exception
	 */
	private int updateStockClaim(OrderstockVO orderstock) throws Exception {
		int executedRtn = 0;
		if(orderstock != null){
			if (orderstock.getPreoutGb().equals("00")) {				
				log.info("//---- update TORDERSTOCK");
				executedRtn = paorderDAO.updateOrderStockCancel20(orderstock);
				if (executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] {"TORDERSTOCK UPDATE"});
				}
			}
		}

		return executedRtn;
	}
	
	private int updateStockClaim(List<OrderstockVO> orderstock) throws Exception {
		
		int executedRtn = 0;
		int rowCnt = 0;
		rowCnt = orderstock.size();
		OrderstockVO reorderstock = null;
				
		for (int i = 0; i < rowCnt; i++) {
			reorderstock = orderstock.get(i);
			executedRtn =  updateStockClaim(reorderstock);		

		}		
		return executedRtn;	

	}
	private int updateStockCancel(List<OrderstockVO> orderstockList) throws Exception {
		
		int executedRtn = 0;
		
		for (OrderstockVO orderstock : orderstockList) {
			if (orderstock.getPreoutGb().equals("00")) {				
				log.info("//---- update TORDERSTOCK");
				executedRtn = paorderDAO.updateOrderStockCancel(orderstock);
				if (executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] {"TORDERSTOCK UPDATE"});
				}
			}
		}		
		return executedRtn;	

	}
	
	// 시퀀스 조회
	public String selectSequenceNo(String type) throws Exception {
		String sequenceNo = "";
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("sequence_type", type);
		if (hashMap.get("sequence_type").equals("CUST_NO")
				|| hashMap.get("sequence_type").equals("ORDER_NO")
				|| hashMap.get("sequence_type").equals("GOODS_SELECT_NO")
				|| hashMap.get("sequence_type").equals("RECEIPT_NO")) {
			hashMap.put("condition_flag", (String) paorderDAO.selectSequenceNoCondition(hashMap));
		}
		sequenceNo = (String) paorderDAO.selectSequenceNo(hashMap);
		if (sequenceNo == null || sequenceNo.equals(""))
			throw processException("msg.cannot_retrieve", new String[] { "getSequenceNo : " + type });
		return sequenceNo;
	}
	
	// 특정 컬럼 max값 조회
	public String getMaxNo(String tableName, String columnName,	String modString, int seqFormat) throws Exception {
		String maxNo = "";
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("table_name", tableName);
		hashMap.put("column_name", columnName);
		hashMap.put("mod_query", modString);
		maxNo = (String) paorderDAO.selectMaxNo(hashMap);

		if (maxNo == null || ("").equals(maxNo)) {
			maxNo = ComUtil.lpad("1", seqFormat, "0");
		} else {
			maxNo = ComUtil.increaseLpad(maxNo, seqFormat, "0");
		}
		return maxNo;
	}
	
	public int updatePaOrderm(ParamMap paramMap) throws Exception{
		return paorderDAO.updatePaOrderm(paramMap);
	}
	
	
	public int insertTpaMonitering(PaMonitering pamoniter) throws Exception{
		return paorderDAO.insertTpaMonitering(pamoniter);
	}
	
	
	private HashMap<String, Object> calcShipCost(List<OrderdtVO> orderdtList,
			List<Receiver> receiverList, 
			String cust_no, String order_no, String user_id,
			Timestamp sysDateTime) throws Exception {

		int sCnt = 0;
		int receiverSize = receiverList.size();
		String ShipCostNum = null;
		String result = null;
		String[] shipcostArr = null;
		String[] shipcostmArr = null;
		String[] shipcostdtArr	= null;
		String[] shipCostNoArr	= null;
		String[] inGoodsCodeArr	= null;
		String shipCostNoM = null;
		String shipCostNoDt = null;
		String arg_goods_str = "";
		String arg_qty_str   = ""; 
		String arg_amt_str   = "";
		String arg_Rcp_Str   = "";
		Boolean bGo = true;
		Boolean	setYn	= false;
		OrderShipCostDt ordershipcostdt = null;
			
		List<Ordershipcost> ordershipcostList = new ArrayList<Ordershipcost>();
		List<Ordercostapply> ordercostapplyList = new ArrayList<Ordercostapply>();
		List<OrderShipCostDt> ordershipcostdtList = new ArrayList<OrderShipCostDt>();
		
	

		for (int i = 0; receiverSize > i; i++) {
			
			if (!"999999".equals(receiverList.get(i).getReceiverPost())) {
			
				for (int j = 0; j < orderdtList.size(); j++) {
					
					
					if( !orderdtList.get(j).getOrderDSeq().equals("001")) continue;
					
					
					
					bGo = true;
					
					if(!receiverList.get(i).getReceiverSeq().equals(orderdtList.get(j).getReceiverSeq())){
						continue;
					}
					
					if( orderdtList.get(j).getGoodsCode() != null || !"".equals(orderdtList.get(j).getGoodsCode())){
						arg_goods_str += " " + orderdtList.get(j).getGoodsCode();
						arg_Rcp_Str   += " " + "1";
					}else{
						bGo = false;
					}
					
					if(orderdtList.get(j).getOrderQty() != 0 && bGo == true ){
						arg_qty_str += " " + orderdtList.get(j).getOrderQty();	
					}else{
						arg_qty_str += " " + "0";
					}
					
					if(orderdtList.get(j).getRsaleAmt() != 0  && bGo == true ){
						arg_amt_str += " " + orderdtList.get(j).getRsaleAmt();
					}else{
						arg_amt_str += " " + "0";
					}
										
				}
				
				arg_goods_str = arg_goods_str.trim();
				arg_qty_str = arg_qty_str.trim();
				arg_amt_str = arg_amt_str.trim();
				arg_Rcp_Str = arg_Rcp_Str.trim();
				
				// 배송비 계산
				OrderShipCostVO orderShipCost = new OrderShipCostVO();
				orderShipCost.setCustNo(cust_no);
				orderShipCost.setOrderFlag("10");
				orderShipCost.setDate((DateUtil.toTimestamp(DateUtil.getLocalDateTime("yyyy/MM/dd HH:mm:ss"),"yyyy/MM/dd HH:mm:ss")));
				orderShipCost.setShipcostReceiptStr(arg_Rcp_Str);
				orderShipCost.setReceiverSeq(receiverList.get(i).getReceiverSeq());
				orderShipCost.setGoodsStr(arg_goods_str);
				orderShipCost.setAmtStr(arg_amt_str);
				orderShipCost.setQtyStr(arg_qty_str);

				result = ComUtil.NVL(orderBizProcess.orderShipCost(orderShipCost));

				shipcostArr = result.split("::");
				shipcostmArr = shipcostArr[0].split(" ");
				shipcostdtArr = shipcostArr[1].split(" ");
				
				String EntpCode = null;
				Double shipFeeAmt = 0.0;
				
				for (int j = 0; j < shipcostmArr.length - 4; j++) {
					if (j % 5 == 0) {

						log.info("//----TORDERSHIPCOST select");

						EntpCode = shipcostmArr[j];
						ShipCostNum = shipcostmArr[j + 2];						
						shipFeeAmt = Double.parseDouble(shipcostmArr[j + 1]); //0 이상만 떨구길 기대한다.
						
						Ordershipcost ordershipcost = new Ordershipcost();
						ordershipcost.setOrderNo(order_no);
						ordershipcost.setSeq(ComUtil.increaseLpad(Integer.toString(sCnt), 3, "0"));
						ordershipcost.setReceiverSeq(receiverList.get(i).getReceiverSeq());
						ordershipcost.setType("10");
						ordershipcost.setOrderGSeq("");
						ordershipcost.setOrderDSeq("");
						ordershipcost.setOrderWSeq("");
						ordershipcost.setShipCostNo(ShipCostNum);
						ordershipcost.setEntpCode(EntpCode);
						ordershipcost.setDelyType("");
						ordershipcost.setShpfeeCode("20");
						ordershipcost.setShpfeeCost(shipFeeAmt);
						ordershipcost.setManualCancelAmt(0);
						ordershipcost.setInsertId(user_id);
						ordershipcost.setInsertDate(sysDateTime);
						ordershipcost.setModifyId(user_id);
						ordershipcost.setModifyDate(sysDateTime);

						ordershipcostList.add(ordershipcost);
						//sCnt++;
					
						
						
						shipCostNoArr = shipcostmArr[j + 3].split(":"); // (포함된 원)배송비정책번호
						for(int k = 0; k < shipCostNoArr.length; k++) {
							shipCostNoM = shipCostNoArr[k];
							
							for(int l = 0; l < shipcostdtArr.length - 4; l++) {
								if( l%5 == 0 ) {
									
									shipCostNoDt = shipcostdtArr[l + 2]; // (원)배송비정책번호
										if( shipCostNoDt.equals(shipCostNoM) ) {
											
											setYn = "1".equals(shipcostdtArr[l + 3]);
											if( setYn ) {
												inGoodsCodeArr = shipcostdtArr[l + 4].split("_");
												for(int m = 0; m < inGoodsCodeArr.length; m++) {
													
													ordershipcostdt = new OrderShipCostDt();
													ordershipcostdt.setSeq(ComUtil.increaseLpad(Integer.toString(sCnt), 3, "0"));
													ordershipcostdt.setGoodsCode(inGoodsCodeArr[m]);
													ordershipcostdt.setEntpCode(shipcostdtArr[l + 1]);
													ordershipcostdt.setShipCostNo(shipCostNoDt);
													ordershipcostdt.setSetGoodsCode(shipcostdtArr[l]);
													ordershipcostdt.setReceiverSeq(receiverList.get(i).getReceiverSeq());
													ordershipcostdtList.add(ordershipcostdt);
												}
												
											}else{
												
												ordershipcostdt = new OrderShipCostDt();
												ordershipcostdt.setSeq(ComUtil.increaseLpad(Integer.toString(sCnt), 3, "0"));
												ordershipcostdt.setGoodsCode(shipcostdtArr[l]);
												ordershipcostdt.setEntpCode(shipcostdtArr[l + 1]);
												ordershipcostdt.setShipCostNo(shipCostNoDt);
												ordershipcostdt.setSetGoodsCode("");
												ordershipcostdt.setReceiverSeq(receiverList.get(i).getReceiverSeq());
												ordershipcostdtList.add(ordershipcostdt);
											}
											
											
										}
									}
															
							}
						}
						
						sCnt++;
						
					}
				}
				
				
				boolean bChk = false;
				int shipCostNo = 0;
				String shipCostSeq = "000";
				
				//주문은 Core(SK-B)에서 Costapply 안받아온다..
				for(int j = 0 ; j < orderdtList.size(); j++ ){
					
					Ordercostapply orderCostApply = new Ordercostapply();
					orderCostApply.setOrderNo(order_no);
					orderCostApply.setOrderGSeq(orderdtList.get(j).getOrderGSeq());
					orderCostApply.setOrderWSeq(orderdtList.get(j).getOrderWSeq());
					orderCostApply.setOrderDSeq(orderdtList.get(j).getOrderDSeq());
					bChk = false;
					
					
					if("001".equals(orderdtList.get(j).getOrderDSeq())){ //본품
						
						for(int k = 0; k < ordershipcostList.size()  ; k++ ){
									
							if(ordershipcostList.get(k).getEntpCode().equals(orderdtList.get(j).getEntpCodeOrg())){
								
								
								for(OrderShipCostDt shipcostdt : ordershipcostdtList){

									if(ordershipcostList.get(k).getSeq().equals(shipcostdt.getSeq()) && ordershipcostList.get(k).getEntpCode().equals(shipcostdt.getEntpCode())){
										
										if(orderdtList.get(j).getGoodsCode().equals(shipcostdt.getGoodsCode())){
											
											shipCostSeq = shipcostdt.getSeq();
											shipCostNo = Integer.parseInt(shipcostdt.getShipCostNo());
											bChk = true;
											break;
										}
									}
								}
								
								
								//해당 분기를 타면서 shipCostSeq가 000, shipCostNo가 0으로 찍히면 소스코드의 취약점 체크해야함..
								orderCostApply.setApplyCostSeq(shipCostSeq);
								orderCostApply.setShipCostNo(shipCostNo);  
										
								if( bChk == true){
									break;
								}
							}
						
						} //end of for문
					
						
					}else{  //사은품
						orderCostApply.setApplyCostSeq("000");
						orderCostApply.setShipCostNo(0);  
					}

						
					orderCostApply.setInsertId(user_id);
					orderCostApply.setInsertDate(sysDateTime);
					orderCostApply.setModifyId(user_id);
					orderCostApply.setModifyDate(sysDateTime);
					ordercostapplyList.add(orderCostApply);	
				}				
			}
		}
		
		HashMap<String, Object> obj  = new HashMap<>();
		obj.put("ordershipcostList",ordershipcostList);
		obj.put("ordercostapplyList", ordercostapplyList);
					
		return obj;
	}
	
	
	private HashMap<String, Object> calcShipCost(List<OrderdtVO> orderdtList, List<Receiver> receiverList ) throws Exception {

		String custNo  = orderdtList.get(0).getCustNo(); 
		String orderNo = orderdtList.get(0).getOrderNo();
		String userId  = orderdtList.get(0).getLastProcId();;
		Timestamp sysDateTime = orderdtList.get(0).getLastProcDate();
		
		int sCnt = 0;
		int receiverSize = receiverList.size();
		String ShipCostNum = null;
		String result = null;
		String[] shipcostArr = null;
		String[] shipcostmArr = null;
		String[] shipcostdtArr	= null;
		String[] shipCostNoArr	= null;
		String[] inGoodsCodeArr	= null;
		String shipCostNoM = null;
		String shipCostNoDt = null;
		String arg_goods_str = "";
		String arg_qty_str   = ""; 
		String arg_amt_str   = "";
		String arg_Rcp_Str   = "";
		Boolean bGo = true;
		Boolean	setYn	= false;
		OrderShipCostDt ordershipcostdt = null;
			
		List<Ordershipcost> ordershipcostList = new ArrayList<Ordershipcost>();
		List<Ordercostapply> ordercostapplyList = new ArrayList<Ordercostapply>();
		List<OrderShipCostDt> ordershipcostdtList = new ArrayList<OrderShipCostDt>();
		
	

		for (int i = 0; receiverSize > i; i++) {
			
			if (!"999999".equals(receiverList.get(i).getReceiverPost())) {
			
				for (int j = 0; j < orderdtList.size(); j++) {
					
					
					if( !orderdtList.get(j).getOrderDSeq().equals("001")) continue;
					
					
					
					bGo = true;
					
					if(!receiverList.get(i).getReceiverSeq().equals(orderdtList.get(j).getReceiverSeq())){
						continue;
					}
					
					if( orderdtList.get(j).getGoodsCode() != null || !"".equals(orderdtList.get(j).getGoodsCode())){
						arg_goods_str += " " + orderdtList.get(j).getGoodsCode();
						arg_Rcp_Str   += " " + "1";
					}else{
						bGo = false;
					}
					
					if(orderdtList.get(j).getOrderQty() != 0 && bGo == true ){
						arg_qty_str += " " + orderdtList.get(j).getOrderQty();	
					}else{
						arg_qty_str += " " + "0";
					}
					
					if(orderdtList.get(j).getRsaleAmt() != 0  && bGo == true ){
						arg_amt_str += " " + orderdtList.get(j).getRsaleAmt();
					}else{
						arg_amt_str += " " + "0";
					}
										
				}
				
				arg_goods_str = arg_goods_str.trim();
				arg_qty_str = arg_qty_str.trim();
				arg_amt_str = arg_amt_str.trim();
				arg_Rcp_Str = arg_Rcp_Str.trim();
				
				// 배송비 계산
				OrderShipCostVO orderShipCost = new OrderShipCostVO();
				orderShipCost.setCustNo(custNo);
				orderShipCost.setOrderFlag("10");
				orderShipCost.setDate((DateUtil.toTimestamp(DateUtil.getLocalDateTime("yyyy/MM/dd HH:mm:ss"),"yyyy/MM/dd HH:mm:ss")));
				orderShipCost.setShipcostReceiptStr(arg_Rcp_Str);
				orderShipCost.setReceiverSeq(receiverList.get(i).getReceiverSeq());
				orderShipCost.setGoodsStr(arg_goods_str);
				orderShipCost.setAmtStr(arg_amt_str);
				orderShipCost.setQtyStr(arg_qty_str);
				log.info("orderShipCost="+orderShipCost.toString());
				
				result = ComUtil.NVL(orderBizProcess.orderShipCost(orderShipCost));
				
				log.info("result = " + result.toString());

				shipcostArr = result.split("::");
				shipcostmArr = shipcostArr[0].split(" ");
				shipcostdtArr = shipcostArr[1].split(" ");
				
				String EntpCode = null;
				Double shipFeeAmt = 0.0;
				
				for (int j = 0; j < shipcostmArr.length - 4; j++) {
					if (j % 5 == 0) {

						log.info("//----TORDERSHIPCOST select");

						EntpCode = shipcostmArr[j];
						ShipCostNum = shipcostmArr[j + 2];						
						shipFeeAmt = Double.parseDouble(shipcostmArr[j + 1]); //0 이상만 떨구길 기대한다.
						
						checkShipFeeLog(shipFeeAmt, orderdtList, orderShipCost);
						
						Ordershipcost ordershipcost = new Ordershipcost();
						ordershipcost.setOrderNo(orderNo);
						ordershipcost.setSeq(ComUtil.increaseLpad(Integer.toString(sCnt), 3, "0"));
						ordershipcost.setReceiverSeq(receiverList.get(i).getReceiverSeq());
						ordershipcost.setType("10");
						ordershipcost.setOrderGSeq("");
						ordershipcost.setOrderDSeq("");
						ordershipcost.setOrderWSeq("");
						ordershipcost.setShipCostNo(ShipCostNum);
						ordershipcost.setEntpCode(EntpCode);
						ordershipcost.setDelyType("");
						ordershipcost.setShpfeeCode("20");
						ordershipcost.setShpfeeCost(shipFeeAmt);
						ordershipcost.setManualCancelAmt(0);
						ordershipcost.setInsertId(userId);
						ordershipcost.setInsertDate(sysDateTime);
						ordershipcost.setModifyId(userId);
						ordershipcost.setModifyDate(sysDateTime);

						ordershipcostList.add(ordershipcost);
						//sCnt++;
					
						
						
						shipCostNoArr = shipcostmArr[j + 3].split(":"); // (포함된 원)배송비정책번호
						for(int k = 0; k < shipCostNoArr.length; k++) {
							shipCostNoM = shipCostNoArr[k];
							
							for(int l = 0; l < shipcostdtArr.length - 4; l++) {
								if( l%5 == 0 ) {
									
									shipCostNoDt = shipcostdtArr[l + 2]; // (원)배송비정책번호
										if( shipCostNoDt.equals(shipCostNoM) ) {
											
											setYn = "1".equals(shipcostdtArr[l + 3]);
											if( setYn ) {
												inGoodsCodeArr = shipcostdtArr[l + 4].split("_");
												for(int m = 0; m < inGoodsCodeArr.length; m++) {
													
													ordershipcostdt = new OrderShipCostDt();
													ordershipcostdt.setSeq(ComUtil.increaseLpad(Integer.toString(sCnt), 3, "0"));
													ordershipcostdt.setGoodsCode(inGoodsCodeArr[m]);
													ordershipcostdt.setEntpCode(shipcostdtArr[l + 1]);
													ordershipcostdt.setShipCostNo(shipCostNoDt);
													ordershipcostdt.setSetGoodsCode(shipcostdtArr[l]);
													ordershipcostdt.setReceiverSeq(receiverList.get(i).getReceiverSeq());
													ordershipcostdtList.add(ordershipcostdt);
												}
												
											}else{
												
												ordershipcostdt = new OrderShipCostDt();
												ordershipcostdt.setSeq(ComUtil.increaseLpad(Integer.toString(sCnt), 3, "0"));
												ordershipcostdt.setGoodsCode(shipcostdtArr[l]);
												ordershipcostdt.setEntpCode(shipcostdtArr[l + 1]);
												ordershipcostdt.setShipCostNo(shipCostNoDt);
												ordershipcostdt.setSetGoodsCode("");
												ordershipcostdt.setReceiverSeq(receiverList.get(i).getReceiverSeq());
												ordershipcostdtList.add(ordershipcostdt);
											}
											
											
										}
									}
															
							}
						}
						
						sCnt++;
						
					}
				}
				
				
				boolean bChk = false;
				int shipCostNo = 0;
				String shipCostSeq = "000";
				
				//주문은 Core(SK-B)에서 Costapply 안받아온다..
				for(int j = 0 ; j < orderdtList.size(); j++ ){
					
					Ordercostapply orderCostApply = new Ordercostapply();
					orderCostApply.setOrderNo(orderNo);
					orderCostApply.setOrderGSeq(orderdtList.get(j).getOrderGSeq());
					orderCostApply.setOrderWSeq(orderdtList.get(j).getOrderWSeq());
					orderCostApply.setOrderDSeq(orderdtList.get(j).getOrderDSeq());
					bChk = false;
					
					
					if("001".equals(orderdtList.get(j).getOrderDSeq())){ //본품
						
						for(int k = 0; k < ordershipcostList.size()  ; k++ ){
									
							if(ordershipcostList.get(k).getEntpCode().equals(orderdtList.get(j).getEntpCodeOrg())){
								
								
								for(OrderShipCostDt shipcostdt : ordershipcostdtList){

									if(ordershipcostList.get(k).getSeq().equals(shipcostdt.getSeq()) && ordershipcostList.get(k).getEntpCode().equals(shipcostdt.getEntpCode())){
										
										if(orderdtList.get(j).getGoodsCode().equals(shipcostdt.getGoodsCode())){
											
											shipCostSeq = shipcostdt.getSeq();
											shipCostNo = Integer.parseInt(shipcostdt.getShipCostNo());
											bChk = true;
											break;
										}
									}
								}
								
								
								//해당 분기를 타면서 shipCostSeq가 000, shipCostNo가 0으로 찍히면 소스코드의 취약점 체크해야함..
								orderCostApply.setApplyCostSeq(shipCostSeq);
								orderCostApply.setShipCostNo(shipCostNo);  
										
								if( bChk == true){
									break;
								}
							}
						
						} //end of for문
					
						
					}else{  //사은품
						orderCostApply.setApplyCostSeq("000");
						orderCostApply.setShipCostNo(0);  
					}
					orderCostApply.setInsertId(userId);
					orderCostApply.setInsertDate(sysDateTime);
					orderCostApply.setModifyId(userId);
					orderCostApply.setModifyDate(sysDateTime);
					ordercostapplyList.add(orderCostApply);	
				}				
			}
		}
		
		HashMap<String, Object> obj  = new HashMap<>();
		obj.put("ordershipcostList",ordershipcostList);
		obj.put("ordercostapplyList", ordercostapplyList);
					
		return obj;
	}
	
	
	private void checkShipFeeLog(Double shipFeeAmt, List<OrderdtVO> orderdtList, OrderShipCostVO orderShipCost) {
		try {

			String paOrderNo	  = orderdtList.get(0).getPaOrderNo();
			String paCode		  = orderdtList.get(0).getPaCode();	
			
			if( !("21").equals(paCode) && !("22").equals(paCode)) return;
			
			double gmkShippingfee = paorderDAO.getGmktShippingFee(paOrderNo);
			//gmkShippingfee g마켓에서 넘겨준 배송비
			//shipFeeAmt 코어에서 계산한 배송비
			if(gmkShippingfee > 0 && shipFeeAmt < 1) {
				
				log.info(orderShipCost.toString());
				log.info("G마켓 배송비  ::: " + String.valueOf(gmkShippingfee) );
				log.info("코어배송비  ::: " + String.valueOf(shipFeeAmt) );
			}
			
		}catch (Exception e) {
			//로그 함수 때문에 이안에서 로직은 터져도 그냥 넘어감
		}
		
	}

	private long calcFeeAmt(List<Ordershipcost> ordershipcostList) {
		long feeAmt = 0;
		for (Ordershipcost vo : ordershipcostList) {
			feeAmt += vo.getShpfeeCost();
		}
		return feeAmt;
	}
	
	private long calcRsalAmt(List<OrderdtVO> orderdtList){
		long rsaleAmt = 0;
		for (OrderdtVO vo : orderdtList) {
			rsaleAmt += vo.getRsaleAmt();
		}
		return rsaleAmt;
	}
	
	private long calcPaShipCostAmt(List<OrderdtVO> orderdtList){
		long shipCostAmt = 0;
		for (OrderdtVO vo : orderdtList) {
			shipCostAmt += vo.getPaShipCostAmt();
		}
		return shipCostAmt;
	}
	
	private String getShipCostReceiptNo(List<Orderreceipts> orderreceiptsList){
		String receipNo = "999999";
		for(Orderreceipts vo : orderreceiptsList ){
			if("15".equals(vo.getSettleGb())){
				receipNo = vo.getReceiptNo();
				break;
			}
		}
		return receipNo;	
	}
	
	private Boolean checkRefineAddress(List<Receiver> receiverList){
	
		for(Receiver vo : receiverList){
			if("999999".equals(vo.getReceiverPost())){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public List<Object> selectOrderTagetDt(String paOrderNo) throws Exception {
		// TODO Auto-generated method stub
		return paorderDAO.selectOrderTagetDt(paOrderNo);
	}

	private int insertPamonitering(String paOrderCode,String paGoodsCode,String goodsCode) throws Exception{
		
		PaMonitering pamoniter = new PaMonitering();
		
		pamoniter.setCheckNo(paOrderCode);
		pamoniter.setCheckGb("10");
		pamoniter.setCheckTxt("판매가 또는 매입가 연동 불일치로 인한 주문 거부");
		pamoniter.setPaGoodsCode(paGoodsCode);
		pamoniter.setGoodsCode(goodsCode);
		
		return insertTpaMonitering(pamoniter);
		
	}

	@Override
	public int getTpaMonitering(PaMonitering pamoniter) throws Exception {
		return paorderDAO.getTpaMonitering(pamoniter);
	}

	
	
	
	private void refineParam4Orderdt(ParamMap[] paramMap ,int paramLength){
		
		
		if (paramLength == 1 ) return;
		
		ParamMap[] tempParam 		= new ParamMap[paramLength];
		for(int iRow = 0; iRow < paramLength; iRow++){
			tempParam[iRow] = new ParamMap();
			tempParam[iRow].put("USEYN", 0);
		}
		
		for(int iRow = 0; iRow < paramLength  ;  iRow ++ ){
			
			if (paramMap[iRow].get("orderPossYn2").toString().equals("1") && paramMap[iRow].get("orderPossYn").toString().equals("1")){
				for(int jRow = 0 ; jRow < paramLength ; jRow++){
					if(tempParam[jRow].get("USEYN").toString().equals("0")){
						tempParam[jRow] = paramMap[iRow];
						tempParam[jRow].put("USEYN","1");
						break;
					}
				}
				
			}else{
				for(int jRow = paramLength ; jRow > 0  ; jRow--){
					if( tempParam[jRow -1].get("USEYN").toString().equals("0")){
						tempParam[jRow -1] = paramMap[iRow];
						tempParam[jRow -1].put("USEYN","1");
						break;
					}
				}
			}
			
		}
		
		for(int i = 0 ; i < paramLength ; i++){
			paramMap[i] = tempParam[i];
		}		
		
	}

	@Override
	public int updatePreCancelOrder(String mappingSeq) throws Exception {
		return paorderDAO.updatePreCancelOrder(mappingSeq);
	}
	
	
	
	
	public ParamMap[] calStock(ParamMap[] paramMap) throws Exception {
		
		ParamMap[] resultParamMap = null;
		int length				  = paramMap.length;
		
		OrderStockInVO[]  orderStockIn  = new OrderStockInVO [length];
		OrderStockOutVO[] orderStockOut = new OrderStockOutVO[length];
		
		for(int i = 0 ; i < length ; i++){
            orderStockIn[i]  = new OrderStockInVO();
            orderStockOut[i] = new OrderStockOutVO();
            orderStockIn[i].setSelectFlag       ( "2" ) ;
            orderStockIn[i].setGoodsCode       	( paramMap[i].getString("goodsCode")				) ;
            orderStockIn[i].setGoodsName       	( paramMap[i].getString("goodsName") 				) ;
            orderStockIn[i].setGoodsdtCode     	( paramMap[i].getString("goodsdtCode") 				) ;
            orderStockIn[i].setOrderNo         	( paramMap[i].getString("orderNo")					) ;
            if(paramMap[i].getString("").equals("1")){
            	orderStockIn[i].setOrderGSeq      	( paramMap[i].getString("orderGSeq")				) ;
            	orderStockIn[i].setOrderDSeq      	( ComUtil.increaseLpad(paramMap[i].getString("seq"), 3, "0"));
            } else {
            	orderStockIn[i].setOrderGSeq      	( ComUtil.increaseLpad(Integer.toString(i) , 3, "0")) ;
            	orderStockIn[i].setOrderDSeq      	( "001"								    			) ;
            }
            orderStockIn[i].setOrderWSeq      	( "001"    											) ;
            orderStockIn[i].setGoodsSelectNo  	( selectSequenceNo("GOODS_SELECT_NO")  				) ;
            orderStockIn[i].setInsertId        	( paramMap[i].getString("userId")					) ;                 					
            orderStockIn[i].setCounselQty      	( paramMap[i].getLong("orderQty")   				) ;
            orderStockIn[i].setDelyType        	( paramMap[i].getString("delyType")     			) ;
            orderStockIn[i].setStockChkPlace  	( paramMap[i].getString("stockChkPlace")			) ;
            orderStockIn[i].setDelyGb        	( "10"       										) ;
            orderStockIn[i].setWhCode        	( paramMap[i].getString("whCode")  					) ;
            orderStockIn[i].setCustNo        	( paramMap[i].getString("custNo") 					) ;
            orderStockIn[i].setReceiverSeq    	( "0000000001"     									) ;
            
            orderStockIn[i].setStdPostNo        ( paramMap[i].getString("stdPostNo"));
            orderStockIn[i].setRoadAddrNo       ( paramMap[i].getString("roadAddrNo"));
		}

		//재고 체크
		checkOrderStock(orderStockIn, orderStockOut);
		
		int resultCnt = 0;
		int useCnt = 0;
		int outLength = orderStockOut.length;
		resultParamMap = new ParamMap[outLength];
		
		for(int j = 0 ; j < outLength; j++ ){
			resultParamMap[resultCnt] = paramMap[j]; //기존의 ParamMap 데이터 세팅
			
			resultParamMap[resultCnt].put("setStockFlag", orderStockOut[j].getStockFlag());
			resultParamMap[resultCnt].put("stockKey", orderStockOut[j].getStockKey());
			resultParamMap[resultCnt].put("firstPlanDate", orderStockOut[j].getFirstPlanDate());
			resultParamMap[resultCnt].put("outPlanDate", orderStockOut[j].getOutPlanDate());
			resultParamMap[resultCnt].put("delyHopeDate", orderStockOut[j].getDelyHopeDate());
			resultParamMap[resultCnt].put("goodsSelectNo", orderStockOut[j].getGoodsSelectNo());
			resultParamMap[resultCnt].put("delyGb", orderStockOut[j].getDelyGb());
			resultParamMap[resultCnt].put("whCode", orderStockOut[j].getWhCode());
			resultParamMap[resultCnt].put("delyHopeTime","90");
			resultParamMap[resultCnt].put("delyHopeYn","0");
			resultParamMap[resultCnt].put("preoutGb","00");
			
			if(orderStockOut[j].getProblemGoodsCode() == null){
				//= 주문가능
				resultParamMap[resultCnt].put("orderPossYn","1");
				useCnt ++;
			} else {
				//= 재고부족 or 판매중단으로 인한 주문불가.
				resultParamMap[resultCnt].put("orderPossYn","0");
			}
			resultCnt++;
		}
		
		if(useCnt < 1){
			//= 주문가능 상품이 하나도 없을 경우 return null;
			resultParamMap = null;
		}		
        return resultParamMap;
	}

	@Override
	public int updatePaOrderMFailConfrimPreOrder(HashMap<String, String> hmSheet) throws Exception {
		return paorderDAO.updatePaOrderMFailConfrimPreOrder(hmSheet);

	}

	@Override
	public int updatePaOrderm20Approval(ParamMap param) throws Exception {
		return paorderDAO.updatePaOrderm20Approval(param);
	}

	@Override
	public String getConfig(String key) throws Exception {
		return paorderDAO.getConfigVal(key);
	}
	
	
	public OrderpromoVO selectPaOrderPromo(ParamMap paramMap) throws Exception {
		return paorderDAO.selectPaOrderPromo(paramMap);
	}

	@Override
	public OrderpromoVO selectPaOrderPromo(Map<String, Object> map) throws Exception {
		return paorderDAO.selectPaOrderPromo(map);
	}
	
	@Override
	public int updateOrderCancelYn(HashMap<String, String> map) throws Exception {
		return paorderDAO.updateOrderCancelYn(map);
	}

	@Override
	public int insertTpaSlipInfo(Map<String, Object> slipOutProc) throws Exception {
		int excuteCnt = 0;
		
		try {
			String transPaDelyGb = "";
			String transSlipNo 	 = "";
			String paDelyGb 	 = "";
			String slipNo		 = "";
			String modifyId 	 = "";
			String transYn		 = "";
			String remark		 = "";
			String mappingSeq	 = "";
			
			String paGroupCode	 = slipOutProc.get("PA_GROUP_CODE").toString();
			//String mappingSeq	 = slipOutProc.get("MAPPING_SEQ").toString();
			
			switch (paGroupCode) {	//지마켓
			case "02": case"03":
				
				mappingSeq 		= slipOutProc.get("MAPPING_SEQ").toString();
				int cnt	=  paorderDAO.selectSlipProcCount(mappingSeq);
				if(cnt > 2) return 1; //지마켓은 1번 이상 운송장 변경을 한경우 운송장 변경을 할 수 없음
				
				paDelyGb 		= slipOutProc.get("PA_DELY_GB_ORG").toString();
				slipNo	 		= slipOutProc.get("INVOICE_NO_ORG").toString();
				transPaDelyGb 	= slipOutProc.get("PA_DELY_GB").toString();
				transSlipNo	 	= slipOutProc.get("INVOICE_NO").toString();
				remark			= slipOutProc.get("REMARK1_V").toString();				
				transYn 		= slipOutProc.get("TRANS_YN").toString();  //TODO TODO TODO
				modifyId 		= "PAG";	//Constants.PA_INTP_PROC_ID;		
				break;

			case "05":
				mappingSeq 		= slipOutProc.get("MAPPING_SEQ").toString();
				paDelyGb 		= slipOutProc.get("PA_DELY_GB").toString();
				slipNo	 		= slipOutProc.get("INVOICE_NO").toString();
				transPaDelyGb 	= slipOutProc.get("TRANS_PA_DELY_GB").toString();
				transSlipNo	 	= slipOutProc.get("TRANS_INVOICE_NO").toString();
				remark			= slipOutProc.get("REMARK1_V").toString();
				transYn			= slipOutProc.get("TRANS_YN").toString();
				modifyId 		= Constants.PA_COPN_PROC_ID;
				break;
			
			case "06":
				mappingSeq 		= slipOutProc.get("MAPPING_SEQ").toString();
				paDelyGb 		= slipOutProc.get("PA_DELY_GB").toString();
				slipNo			= slipOutProc.get("INVOICE_NO").toString();
				transPaDelyGb	= slipOutProc.get("TRANS_PA_DELY_GB").toString();
				transSlipNo		= slipOutProc.get("TRANS_INVOICE_NO").toString();
				remark			= slipOutProc.get("REMARK1_V").toString();
				transYn			= slipOutProc.get("TRANS_YN").toString();
				modifyId		= Constants.PA_WEMP_PROC_ID;
				break;
				
			case "08":
				mappingSeq 		= slipOutProc.get("MAPPING_SEQ").toString();
				paDelyGb 		= slipOutProc.get("DV_CO_CD").toString();
				slipNo	 		= slipOutProc.get("INVC_NO").toString();
				transPaDelyGb 	= slipOutProc.get("DV_CO_CD").toString();
				transSlipNo	 	= slipOutProc.get("INVC_NO").toString();
				remark			= slipOutProc.get("REMARK1_V").toString();
				transYn			= slipOutProc.get("TRANS_YN").toString();
				modifyId 		= Constants.PA_LTON_PROC_ID;
				break;
				
			case "09":
				mappingSeq 		= slipOutProc.get("MAPPING_SEQ").toString();
				paDelyGb 		= slipOutProc.get("PA_DELY_GB").toString();
				slipNo	 		= slipOutProc.get("INVOICE_NO").toString();
				transPaDelyGb 	= slipOutProc.get("TRANS_PA_DELY_GB").toString();
				transSlipNo	 	= slipOutProc.get("TRANS_INVOICE_NO").toString();
				remark			= slipOutProc.get("REMARK1_V").toString();
				transYn			= slipOutProc.get("TRANS_YN").toString();
				modifyId 		= Constants.PA_TMON_PROC_ID;
				break;
				
			case "11":
				mappingSeq 		= slipOutProc.get("MAPPING_SEQ").toString();
				paDelyGb 		= slipOutProc.get("PA_DELY_GB").toString();
				slipNo	 		= slipOutProc.get("INVOICE_NO").toString();
				transPaDelyGb 	= slipOutProc.get("TRANS_PA_DELY_GB").toString();
				transSlipNo	 	= slipOutProc.get("TRANS_INVOICE_NO").toString();
				remark			= slipOutProc.get("REMARK1_V").toString();
				transYn			= slipOutProc.get("TRANS_YN").toString();
				modifyId 		= Constants.PA_KAKAO_PROC_ID;
				break;
				
				
			default:
				return 0;
			}
			
			PaSlipInfo paSlipInfo = new PaSlipInfo();
			
			paSlipInfo.setPaGroupCode	(paGroupCode);
			paSlipInfo.setMappingSeq	(mappingSeq);
			paSlipInfo.setTransPaDelyGb	(transPaDelyGb);
			paSlipInfo.setTransSlipNo	(transSlipNo);
			paSlipInfo.setPaDelyGb		(paDelyGb);
			paSlipInfo.setSlipNo		(slipNo);
			paSlipInfo.setLastYn		("1");
			paSlipInfo.setTransYn		(transYn);
			paSlipInfo.setModifyId		(modifyId);
			paSlipInfo.setRemark1V		(remark);
			
			//UPDATE USE_YN = 0??
			excuteCnt = paorderDAO.updateTpaSlipInfo4Renew(paSlipInfo);		//TPASLIPINFO.LAST_YN = 0
			excuteCnt = paorderDAO.insertTpaSlipInfo(paSlipInfo);			//TPASLIPINFO INSERT
			
		}catch (Exception e) {
			log.error(e.toString());
		}
		
		return excuteCnt;
		
	}
	
	
	//제주도서산간 불가 상품 주문 인입시 상담 데이터 생성
	private void checkNoDelyAreaOrder( Receiver receiver, List<OrderdtVO> orderdtList ) {
		if(orderdtList ==null || orderdtList.size() < 1 )	  return;
		if(!"EX13".equals(orderdtList.get(0).getMediaCode())) return; //하프클럽만 적용
		
		try {
			ParamMap paramMap 		= new ParamMap();
			String note 			= "";
			boolean isNoAreaOrder 	= false;
						
			for(OrderdtVO order : orderdtList) {
				isNoAreaOrder = false;
				
				paramMap.put("orderNo"		, order.getOrderNo());
				paramMap.put("orderGSeq"	, order.getOrderGSeq());
				paramMap.put("orderDSeq"	, order.getOrderDSeq());
				paramMap.put("orderWSeq"	, order.getOrderWSeq());
				paramMap.put("custNo"		, order.getCustNo());
				paramMap.put("lGroupCode"	, "60");
				paramMap.put("mGroupCode"	, "12"); // 하프클럽만 적용
				paramMap.put("sGroupCode"	, "97"); //타 제휴사 사용시 CSKIND에 세팅 필요 
				paramMap.put("goodsCode"	, order.getGoodsCode());
				paramMap.put("goodsdtCode"	, order.getGoodsdtCode());
				paramMap.put("entpCode"		, order.getEntpCode());
				paramMap.put("sysdate"		, order.getLastProcDate());	
				
				if("999999".equals(receiver.getReceiverPost())) {
					paramMap.put("postNo"		, order.getReceiverPost());  
					paramMap.put("postSeq"		, "%");	
				}else {
					paramMap.put("postNo"		, receiver.getReceiverStdPost()); 
					paramMap.put("postSeq"		, receiver.getReceiverStdPostSeq());		
				}
				
				List<HashMap<String, String>> delyNoList= paorderDAO.selectDelyNoAreaGb(paramMap);
				String postAreaGb 	= paorderDAO.selectPostAreaGb(paramMap);
				
				//if(delyNoList == null ) continue;
				
				for(HashMap<String, String> na : delyNoList) {

					if(na.get("AREA_GB").equals(postAreaGb)) {
						isNoAreaOrder = true;
						note		  = "도서산간/제주 배송불가한 주문으로 하프클럽 고객센터에서 주문취소 예정입니다.\n출고되지 않도록 주의 부탁 드립니다.";
						paramMap.put("note"			, note);
						break;
					}					
				}
				
				if(isNoAreaOrder)	insertCustCounsel(paramMap);
			}
			
		}catch (Exception e) {
			log.error("제주도서산간 배송 불가 주문 인입  상담 생성 에러 :: " + e.getMessage());
		}
	}
	
	private int insertCustCounsel(ParamMap paramMap) throws Exception{
		int executedRtn 						= 0;
		Custcounselm custcounselm 				= new Custcounselm();
		
		//CounselSeq 채번
		String counsel_seq = systemProcess.getSequenceNo("COUNSEL_SEQ");
		if (counsel_seq == null || "".equals(counsel_seq))  throw processException("msg.cannot_create", new String[] { "COUNSEL_SEQ" });		
	
		custcounselm.setCounselSeq		(counsel_seq);
		custcounselm.setClaimNo			(counsel_seq);
		custcounselm.setCustNo			(paramMap.getString("custNo"));
		custcounselm.setDoFlag			("25");  //업체이관
		custcounselm.setOutLgroupCode	("81");  //배송 수거 문의  , 'C031';
		custcounselm.setOutMgroupCode	("47");	 //기타
		custcounselm.setOutSgroupCode	("");
		custcounselm.setTel3			("");
		custcounselm.setWildYn			("0");
		custcounselm.setQuickEndYn		("0");
		custcounselm.setQuickYn			("0");
		custcounselm.setHcReqDate		(null);
		custcounselm.setRemark			("");
		custcounselm.setRefNo1			(paramMap.getString("orderNo"));
		custcounselm.setRefNo2			(paramMap.getString("orderGSeq"));
		custcounselm.setRefNo3			(paramMap.getString("orderDSeq"));
		custcounselm.setRefNo4			(paramMap.getString("orderWSeq"));
		custcounselm.setCsSendYn		("1");
		custcounselm.setCounselMedia	("61");		
		custcounselm.setCsLgroup		(paramMap.getString("lGroupCode"));
		custcounselm.setCsMgroup		(paramMap.getString("mGroupCode"));
		custcounselm.setCsSgroup		(paramMap.getString("sGroupCode"));
		custcounselm.setCsLmsCode		(paramMap.getString("lGroupCode") + paramMap.getString("mGroupCode") + paramMap.getString("sGroupCode"));

		custcounselm.setGoodsCode		(paramMap.getString("goodsCode"));
		custcounselm.setGoodsdtCode		(paramMap.getString("goodsdtCode"));
		
		custcounselm.setSendEntpCode	(paramMap.getString("entpCode"));
		
		custcounselm.setInsertId		("PAHALF");
		custcounselm.setProcDate		(DateUtil.toTimestamp(paramMap.getString("sysdate"), "yyyy-MM-dd HH:mm:ss"));
		custcounselm.setProcId			("PAHALF");

		executedRtn = paclaimDAO.insertCounselCustcounselm(custcounselm);
		
		if (executedRtn != 1) throw processException("msg.cannot_save",new String[] { "Custcounselm insert" });		
		
		
		if(counsel_seq ==null || counsel_seq.equals("")) throw processException("errors.db.no.seq_no" ,new String[] { "Counsel_seq"});
		String counselProcNote = paramMap.getString("note");
		
		for(int i = 0; i < 2; i++) {
			Custcounseldt custcounseldt = new Custcounseldt();
			executedRtn = 0;
			
			custcounseldt.setCounselSeq		(counsel_seq);
			if(i==0) {
				custcounseldt.setCounselDtSeq	("100"); 
				custcounseldt.setDoFlag			("10");  //접수
			} else {
				custcounseldt.setCounselDtSeq	("101"); 
				custcounseldt.setDoFlag			("25");  //업체이관
			}
			custcounseldt.setTitle			("");
			custcounseldt.setDisplayYn		("");
			custcounseldt.setProcNote		("상세사유 : " + counselProcNote);
			custcounseldt.setProcId			("PAHALF");
			
			executedRtn = paclaimDAO.insertCounselCustcounseldt(custcounseldt);
			
			if (executedRtn != 1) throw processException("msg.cannot_save",new String[] { "TCUSTCOUNSELDT insert" });
		}
		return executedRtn;		
		
	}
	

	@Override
	public String selectOrderGoodsDtName(ParamMap goodsDtParam) throws Exception {
		return paorderDAO.selectOrderGoodsDtName(goodsDtParam);
	}
	
	
	private HashMap<String, Object> calcShipCostN(List<OrderdtVO> orgOrderdtList, List<Receiver> receiverList) throws Exception {

		
		List<Ordershipcost> ordershipcostList 		= new ArrayList<Ordershipcost>();
		List<Ordercostapply> ordercostapplyList 	= new ArrayList<Ordercostapply>();
		List<OrderShipCostDt> ordershipcostdtList 	= new ArrayList<OrderShipCostDt>();
		
		String[] shipcostArr 	= null;
		String[] shipcostmArr 	= null;
		String[] shipcostdtArr	= null;
		String[] shipCostNoArr	= null;
		String[] inGoodsCodeArr	= null;
		
		int tempSeq 			= 0;
		String tempSeqStr		= "";
		String orderNo		 	= orgOrderdtList.get(0).getOrderNo();
		String userId			= orgOrderdtList.get(0).getLastProcId();
		Timestamp sysDateTime	= orgOrderdtList.get(0).getLastProcDate();
		
		String preoutGb = orgOrderdtList.get(0).getPreoutGb();
			
		for(Receiver rc : receiverList) {
			if("999999".equals(rc.getReceiverPost())) continue; //주소정제에서 처리
			//INIT VAL
			String arg_goods_str = "";
			String arg_qty_str	 = "";
			String arg_amt_str	 = "";
			String arg_Rcp_Str	 = "";
			
			List<OrderdtVO> orderdtList = orgOrderdtList.stream().filter( order ->order.getReceiverSeq().equals(rc.getReceiverSeq()))
					                                             .collect(Collectors.toList());
			
			for(OrderdtVO order : orderdtList) {		
				if(!"001".equals(order.getOrderDSeq())) continue; // 사은품 제외
				arg_goods_str 	+= " " + order.getGoodsCode();
				arg_Rcp_Str   	+= " " + "1";
				arg_qty_str 	+= " " + order.getOrderQty();	
				arg_amt_str 	+= " " + order.getRsaleAmt();
			}
			
			
			OrderShipCostVO prmOrderShipCost = new OrderShipCostVO();
			prmOrderShipCost.setCustNo					(rc.getCustNo());
			prmOrderShipCost.setOrderFlag				("10");
			prmOrderShipCost.setDate					((DateUtil.toTimestamp(DateUtil.getLocalDateTime("yyyy/MM/dd HH:mm:ss"),"yyyy/MM/dd HH:mm:ss")));
			prmOrderShipCost.setShipcostReceiptStr		(arg_Rcp_Str.trim());
			prmOrderShipCost.setReceiverSeq				(rc.getReceiverSeq());
			prmOrderShipCost.setGoodsStr				(arg_goods_str.trim());
			prmOrderShipCost.setAmtStr					(arg_amt_str.trim());
			prmOrderShipCost.setQtyStr					(arg_qty_str.trim());

			String result = ComUtil.NVL(orderBizProcess.orderShipCost(prmOrderShipCost));
			
			shipcostArr 	= result.split("::");
			shipcostmArr 	= shipcostArr[0].split(" ");
			shipcostdtArr 	= shipcostArr[1].split(" ");
			
			//Setting TORDERSHIPCOST
			for (int j = 0; j < shipcostmArr.length - 4; j++) {
				if (j % 5 != 0) continue; 
				
				tempSeq ++;
				tempSeqStr = String.format("%03d", tempSeq);
				
				Ordershipcost ordershipcost = new Ordershipcost();
				ordershipcost.setOrderNo			(orderNo);
				ordershipcost.setSeq				(tempSeqStr);
				ordershipcost.setReceiverSeq		(rc.getReceiverSeq());
				ordershipcost.setType				("10");
				ordershipcost.setOrderGSeq			("");
				ordershipcost.setOrderDSeq			("");
				ordershipcost.setOrderWSeq			("");
				ordershipcost.setShipCostNo			(shipcostmArr[j + 2]);
				ordershipcost.setEntpCode			(shipcostmArr[j]);
				ordershipcost.setDelyType			("");
				ordershipcost.setShpfeeCode			("20");
				if("20".equals(preoutGb)) { //기출하 주문의 경우 배송비 0원
					ordershipcost.setShpfeeCost			(0);
				}else {
					ordershipcost.setShpfeeCost			(Double.parseDouble(shipcostmArr[j + 1]));
				}
				ordershipcost.setManualCancelAmt	(0);
				ordershipcost.setInsertId			(userId);
				ordershipcost.setInsertDate			(sysDateTime);
				
				ordershipcostList.add(ordershipcost);
				
				shipCostNoArr = shipcostmArr[j + 3].split(":"); // (포함된 원)배송비정책번호
					
				for(String shipCostNoM : shipCostNoArr ) {
					for(int l = 0; l < shipcostdtArr.length - 4; l++) {
						if( l % 5 != 0 ) continue; 
							
						String 	shipCostNoDt 	= shipcostdtArr[l + 2]; // (원)배송비정책번호
						Boolean	setYn			= false;
								
						if( shipCostNoM.equals(shipCostNoDt) ) {
							setYn = "1".equals(shipcostdtArr[l + 3]);
							if( setYn ) {
								inGoodsCodeArr = shipcostdtArr[l + 4].split("_");
								
								for(int m = 0; m < inGoodsCodeArr.length; m++) {
										OrderShipCostDt ordershipcostdt = new OrderShipCostDt();
										ordershipcostdt.setSeq			(tempSeqStr);
										ordershipcostdt.setGoodsCode	(inGoodsCodeArr[m]);
										ordershipcostdt.setEntpCode		(shipcostdtArr[l + 1]);
										ordershipcostdt.setShipCostNo	(shipCostNoDt);
										ordershipcostdt.setSetGoodsCode	(shipcostdtArr[l]);
										ordershipcostdt.setReceiverSeq	(rc.getReceiverSeq());
										ordershipcostdtList.add(ordershipcostdt);
								}
							}else{
								OrderShipCostDt ordershipcostdt = new OrderShipCostDt();
								ordershipcostdt.setSeq			(tempSeqStr);
								ordershipcostdt.setGoodsCode	(shipcostdtArr[l]);
								ordershipcostdt.setEntpCode		(shipcostdtArr[l + 1]);
								ordershipcostdt.setShipCostNo	(shipCostNoDt);
								ordershipcostdt.setSetGoodsCode	("");
								ordershipcostdt.setReceiverSeq	(rc.getReceiverSeq());
								ordershipcostdtList.add(ordershipcostdt);
							}
						}
					}					
				}
			}
			
			
			//Setting TORDERCOSTAPPLY
			for(OrderdtVO order : orderdtList) {
				
				Ordercostapply orderCostApply = new Ordercostapply();
				
				orderCostApply.setOrderNo	(order.getOrderNo());
				orderCostApply.setOrderGSeq	(order.getOrderGSeq());
				orderCostApply.setOrderDSeq	(order.getOrderDSeq());
				orderCostApply.setOrderWSeq	(order.getOrderWSeq());
				orderCostApply.setInsertId	(userId);
				orderCostApply.setInsertDate(sysDateTime);
				orderCostApply.setModifyId	(userId);
				orderCostApply.setModifyDate(sysDateTime);
				
				//사은품
				if(!"001".equals(order.getOrderDSeq())) {
					orderCostApply.setApplyCostSeq	("000");
					orderCostApply.setShipCostNo	(0);
					ordercostapplyList.add(orderCostApply);	
					continue;
				}
				
				Optional<OrderShipCostDt> orderShipCostDt = ordershipcostdtList.stream().filter(
						os ->  os.getGoodsCode().equals(order.getGoodsCode()) 
								 && os.getReceiverSeq().equals(order.getReceiverSeq())).findFirst();
			
				OrderShipCostDt ods = orderShipCostDt.get();
				
				orderCostApply.setApplyCostSeq	(ods.getSeq());
				orderCostApply.setShipCostNo	(Integer.parseInt(ods.getShipCostNo()));
				ordercostapplyList.add(orderCostApply);	
			}
		}
			
		HashMap<String, Object> obj  = new HashMap<>();
		obj.put("ordershipcostList"		,	ordershipcostList);
		obj.put("ordercostapplyList"	, 	ordercostapplyList);
		
		return obj;
	}
	
	
	private ParamMap refineAddress(OrderInputVO orderInput) throws Exception {
		
		String cust_postNo  = orderInput.getPostNo();
		String cust_postSeq = orderInput.getPostNoSeq();
		String cust_AddrGbn = orderInput.getAddrGbn();
		String cust_Addr    = orderInput.getStdAddr();
		String cust_AddrDt  = orderInput.getStdAddrDT();
		String full_Addr	= orderInput.getReceiverAddr();
				
		if (cust_postNo == null  || "".equals(cust_postNo)  ) cust_postNo  = "999999";
		if (cust_postSeq == null || "".equals(cust_postSeq) ) cust_postSeq = "001";
		if (cust_AddrGbn == null || "".equals(cust_AddrGbn) ) cust_AddrGbn = "0";
				

		ParamMap resultParamMap = new ParamMap();
		resultParamMap.put("post_no",cust_postNo);

		if("02".equals(cust_AddrGbn)){//01이면 지번 , 02면 도로명
			resultParamMap.put("road_addr_yn", 1);
		}
		else{
			resultParamMap.put("road_addr_yn", 0);
		}	
		
		resultParamMap.put("search_addr"	,	cust_Addr);	
		resultParamMap.put("search_addr2"	,	cust_AddrDt);
		resultParamMap.put("full_addr"		, 	full_Addr);
		resultParamMap.put("localYn"		,	orderInput.getIsLocalYn());
		return systemProcess.selectStdAddress(resultParamMap);//sk스토아 주소정제
	}

	@Override
	public int selectOrderGoodsDtDupleCheck(ParamMap goodsDtParam) throws Exception {
		return paorderDAO.selectOrderGoodsDtDupleCheck(goodsDtParam);
	}

	@Override
	public int updateRemark3N(HashMap<String, String> map) throws Exception {
		return paorderDAO.updateRemark3N(map);

	}

	@Override
	public List<Map<String, String>> selectPreoutOrderTargetList() throws Exception {
		return paorderDAO.selectPreoutOrderTargetList();
	}

	@Override
	public List<Map<String, Object>> selectPreoutOrderTargetDtList(ParamMap paramMap) throws Exception {
		return paorderDAO.selectPreoutOrderTargetDtList(paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, Object>[] savePreoutOrder(PreoutOrderInputVO[] preoutOrderInputVO) throws Exception {
		int paramLength							= preoutOrderInputVO.length;
		String rtnValue = "000000";
		String user_id = preoutOrderInputVO[0].getProcUser();
		// 주문데이터 객체 생성
		Orderm     orderm     = new Orderm();
		OrderdtVO orderdt 	  = null;
		Orderpromo orderpromo = null;
		Ordergoods ordergoods = null;
		Custsystem custsystem = new Custsystem();
		PromocounselVO promoCounsel = null;
		Orderreceipts orderreceipts = null;
		
		String    order_no    	 = selectSequenceNo("ORDER_NO"); // 주문번호seq 생성
		String    cust_no	     = preoutOrderInputVO[0].getCustNo();
		String    dateTime       = getSysdatetimeToString();								// 시간생성(String)
		Timestamp sysDateTime    = DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss");	// 시간생성(Timestamp)
		String    receipt_no  	 = "";
		int 	  orderDSeq 	 = 1;
		String 	  goodsSelectNo  = "";
		int 	  executedRtn    = 0;
		long 	  l_temp		 = 0;
		ParamMap[] paramMap 		= new ParamMap[paramLength];
		HashMap[] resultMap 		= new HashMap[paramLength];

		OrderInfoMsg[] goodsInfo    = new OrderInfoMsg[paramLength];

		try {
			
			// 상품코드 및 상품정보 매핑
			for (int i = 0; i < paramLength; i++) {
				paramMap[i] = new ParamMap();

				paramMap[i].put("orderNo",			order_no);
				paramMap[i].put("custNo",			cust_no);
				paramMap[i].put("paCode" ,          preoutOrderInputVO[i].getPaCode());
				paramMap[i].put("mappingSeq",		preoutOrderInputVO[i].getMappingSeq());
				paramMap[i].put("custName",			preoutOrderInputVO[i].getCustName());
				paramMap[i].put("mediaCode",		preoutOrderInputVO[i].getMediaCode());
				paramMap[i].put("orderDate",		sysDateTime);
				paramMap[i].put("goodsCode",		preoutOrderInputVO[i].getGoodsCode());
				paramMap[i].put("goodsdtCode",		preoutOrderInputVO[i].getGoodsdtCode());
				paramMap[i].put("orderQty",			preoutOrderInputVO[i].getOrderQty());

				paramMap[i].put("applyDate",		preoutOrderInputVO[i].getApplyDate());
				paramMap[i].put("receiverSeq",		preoutOrderInputVO[i].getReceiverSeq());
				paramMap[i].put("priceSeq",			preoutOrderInputVO[i].getPriceSeq());
				paramMap[i].put("dcAmt",			preoutOrderInputVO[i].getDcAmt());
				paramMap[i].put("remark3N",         preoutOrderInputVO[i].getRemark3N());
				paramMap[i].put("remark4N",         preoutOrderInputVO[i].getRemark4N());
				
				paramMap[i].put("poutOrgOrderNo",             preoutOrderInputVO[i].getOrgOrderNo());
				paramMap[i].put("poutOrgOrderNoGSeq",         preoutOrderInputVO[i].getOrgOrderGSeq());
				paramMap[i].put("poutOrgOrderNoDSeq",         preoutOrderInputVO[i].getOrgOrderDSeq());
				paramMap[i].put("poutOrgOrderNoWSeq",         preoutOrderInputVO[i].getOrgOrderWSeq());


				try{
					log.info("//----goodsInfo select");
					goodsInfo[i] = paorderDAO.selectGoodsInfo(paramMap[i]);
				}catch (Exception e) {
					rtnValue = "selectGoodsInfo select fail";
				    throw processMessageException(rtnValue);
				}
				
				if(goodsInfo[i] == null){
					rtnValue = "selectGoodsInfo select null";				    
				    throw processMessageException(rtnValue);
				}
				
				paramMap[i].put("entpCode"			,	goodsInfo[i].getEntpCode());
				paramMap[i].put("entpCodeOrg"		,	goodsInfo[i].getEntpCodeOrg());
				paramMap[i].put("salePrice"			,	goodsInfo[i].getSalePrice());
				paramMap[i].put("buyPrice"			,	goodsInfo[i].getBuyPrice());
				paramMap[i].put("goodsName"			,	goodsInfo[i].getGoodsName());
				paramMap[i].put("inviGoodsType"		,	goodsInfo[i].getInviGoodsType());
				paramMap[i].put("norestAllotMonths"	,	goodsInfo[i].getNorestAllotMonths());
				paramMap[i].put("delyType"			,	goodsInfo[i].getDelyType());
				paramMap[i].put("whCode"			,	goodsInfo[i].getWhCode());				
				paramMap[i].put("mdCode"			,	goodsInfo[i].getMdCode());
				paramMap[i].put("directShipYn"		,	goodsInfo[i].getDirectShipYn());
				paramMap[i].put("stockChkPlace"		,	goodsInfo[i].getStockChkPlace());
				paramMap[i].put("goodsdtInfo"		,	goodsInfo[i].getGoodsDtInfo());
				paramMap[i].put("arsDcAmt"			,	goodsInfo[i].getArsDcAmt());
				paramMap[i].put("arsOwnDcAmt"		,	goodsInfo[i].getArsOwnDcAmt());
				paramMap[i].put("arsEntpDcAmt"		,	goodsInfo[i].getArsEntpDcAmt());
				paramMap[i].put("userId"			,	user_id);
				paramMap[i].put("sysDateTime"		,	sysDateTime);
			}
			
			//= 1) 재고체크
			log.info("//----CALSTOCK check");
			
			
			paramMap = calStock(paramMap , goodsInfo);	
			
			if(paramMap == null){ //= 전체 주문불가.
				rtnValue = getMessage("pa.fail_preout_order_input", new String[]{getMessage("pa.out_of_stock")});
				for(int i = 0; paramLength > i; i++){
					resultMap[i] = new HashMap<>();
					resultMap[i].put("MAPPING_SEQ", 	preoutOrderInputVO[i].getMappingSeq());
					resultMap[i].put("RESULT_CODE", 	"100001"); 
					resultMap[i].put("RESULT_MESSAGE", 	rtnValue);
				}
				return resultMap;
			}
			
			//tcustsystem 세팅
			log.info("//----TCUSTSYSTEM setting");
			custsystem.setCustNo(cust_no);
			custsystem.setLastConMediaCode("62");
			custsystem.setLastConDate(sysDateTime);
			custsystem.setModifyDate(sysDateTime);
			custsystem.setModifyId(user_id);
			
			//orderm 세팅
		    log.info("//----TORDERM setting");
		    orderm.setBlank();
			orderm.setOrderNo(order_no);
			orderm.setCustNo(cust_no);
			orderm.setOrderDate(sysDateTime);
			orderm.setOrderMedia("62");
			orderm.setInsertId(user_id);
			orderm.setPassword("");
			orderm.setWithCode("");
			orderm.setIpAddr("");
			orderm.setMembGb("90");
			orderm.setEmployeeId("");
			//orderm.setSenderName(paramMap[0].getString("custName"));
			orderm.setChannel("");
			orderm.setChannel_id("");		
			
			//receiver, orderdt 세팅
			log.info("//----TORDERDT setting");
			long RsaleAmt = 0;	

			List<OrderdtVO> orderdtList = new ArrayList<OrderdtVO>();
			List<Orderpromo> orderpromoList = new ArrayList<Orderpromo>();
			List<Orderreceipts> orderreceiptsList = new ArrayList<Orderreceipts>();
			List<Receiver> receiverList = new ArrayList<Receiver>();
			List<Ordershipcost> ordershipcostList = new ArrayList<Ordershipcost>();
			List<Ordercostapply> ordercostapplyList = new ArrayList<Ordercostapply>();
			
			for (int i = 0; i < paramLength; i++) {
				//= 재고부족  상품의 주문건은 주문데이터를 생성하지 않음.
				if(paramMap[i].getString("orderPossYn").equals("0")) continue;
				orderDSeq = 0;
				orderdt = new OrderdtVO();
				orderdt.setOrderNo(order_no);
				orderdt.setOrderGSeq(ComUtil.increaseLpad(Integer.toString(i) , 3, "0"));
				orderdt.setOrderDSeq(ComUtil.increaseLpad(Integer.toString(orderDSeq) , 3, "0"));
				orderdt.setOrderWSeq("001");
				orderdt.setCustNo(cust_no);
				orderdt.setOrderDate(sysDateTime);
				orderdt.setMdCode(paramMap[i].getString("mdCode"));
				orderdt.setReceiverSeq(paramMap[i].getString("receiverSeq"));
				orderdt.setOrderGb("10");
				orderdt.setDoFlag("20");
				orderdt.setGoodsGb("10");
				orderdt.setMediaGb("03");
				orderdt.setMediaCode(paramMap[i].getString("mediaCode"));
				orderdt.setGoodsCode(paramMap[i].getString("goodsCode"));
				orderdt.setGoodsdtCode(paramMap[i].getString("goodsdtCode"));
				orderdt.setGoodsdtInfo(paramMap[i].getString("goodsdtInfo"));
				
				orderdt.setSalePrice(paramMap[i].getDouble("salePrice"));
				orderdt.setBuyPrice(paramMap[i].getDouble("buyPrice"));
				orderdt.setOrderQty(paramMap[i].getLong("orderQty"));
				orderdt.setOrderAmt(orderdt.getSalePrice() * orderdt.getOrderQty());
				orderdt.setPriceSeq(paramMap[i].getString("priceSeq"));
				
				orderdt.setDcAmtMemb(0);
				orderdt.setDcAmtDiv(0);
				orderdt.setDcAmtCard(0);
				
				orderdt.setDcAmtGoods(paramMap[i].getDouble("dcAmt"));				
				orderdt.setDcRateGoods(ComUtil.modAmt(orderdt.getDcAmtGoods()/orderdt.getOrderAmt() * 10000) / 100.0 );
				
				orderdt.setDcRate(orderdt.getDcRateGoods());
				orderdt.setDcAmt(orderdt.getDcAmtGoods());
				
				orderdt.setRsaleAmt(orderdt.getOrderAmt() - orderdt.getDcAmtGoods());
						
				orderdt.setSyscancel(0);
				orderdt.setSyslast(orderdt.getOrderQty());
				orderdt.setSyslastDcGoods(orderdt.getDcAmtGoods());
				orderdt.setSyslastDcMemb(orderdt.getDcAmtMemb());
				orderdt.setSyslastDcDiv(orderdt.getDcAmtDiv());
				orderdt.setSyslastDcCard(orderdt.getDcAmtCard());
				orderdt.setSyslastDc(orderdt.getDcAmt());
				
				orderdt.setSyslastAmt(orderdt.getRsaleAmt());
				
				orderdt.setWhCode(paramMap[i].getString("whCode"));
				orderdt.setSaleYn("1");
				orderdt.setDelyType(paramMap[i].getString("delyType"));
				orderdt.setDelyGb("10");
				orderdt.setCustDelyFlag("0");
				
				orderdt.setFirstPlanDate(DateUtil.toTimestamp(paramMap[i].getString("firstPlanDate"), "yyyy-MM-dd HH:mm:ss"));
				orderdt.setOutPlanDate(DateUtil.toTimestamp(paramMap[i].getString("outPlanDate"), "yyyy-MM-dd HH:mm:ss"));
				orderdt.setStockFlag(paramMap[i].getString("setStockFlag"));
				orderdt.setStockKey(paramMap[i].getString("stockKey"));
				orderdt.setDelyHopeDate(DateUtil.toTimestamp(paramMap[i].getString("delyHopeDate"), "yyyy-MM-dd HH:mm:ss"));

				orderdt.setPreoutGb("20");	//기출하 구분
				orderdt.setPoutOrgOrderNo(paramMap[i].getString("poutOrgOrderNo"));
				orderdt.setPoutOrgOrderNoGSeq(paramMap[i].getString("poutOrgOrderNoGSeq"));
				orderdt.setPoutOrgOrderNoDSeq(paramMap[i].getString("poutOrgOrderNoDSeq"));
				orderdt.setPoutOrgOrderNoWSeq(paramMap[i].getString("poutOrgOrderNoWSeq"));

				orderdt.setGoodsSelectNo(paramMap[i].getString("goodsSelectNo"));
				orderdt.setSingleDueGb("00");
							
				orderdt.setPackYn("0");
				orderdt.setAnonyYn("0");
				orderdt.setMsg("");
				orderdt.setMsgNote("");
				orderdt.setHappyCardYn("0");
				orderdt.setSaveamt(0);
				orderdt.setSaveamtGb("90");
				orderdt.setPromoNo1("");
				orderdt.setPromoNo2("");
				orderdt.setSetGoodsCode("");
				orderdt.setReceiptYn("0");
				orderdt.setSlipYn("1");
				orderdt.setEntpslipNo("");
				orderdt.setDirectYn(paramMap[i].getString("directShipYn"));
				orderdt.setLastProcId(user_id);
				orderdt.setLastProcDate(sysDateTime);
				orderdt.setReservDelyYn("0");
				
				orderdt.setEntpCode(paramMap[i].getString("entpCode"));
				orderdt.setEntpCodeOrg(paramMap[i].getString("entpCodeOrg"));
				orderdt.setNorestAllotMonths(paramMap[i].getString("norestAllotMonths"));
				
				//= 제휴할인 가격
				orderdt.setRemark3N(paramMap[i].getLong("remark3N"));
				orderdt.setRemark4N(paramMap[i].getLong("remark4N"));

				RsaleAmt += orderdt.getRsaleAmt();
				
				paramMap[i].put("orderGSeq", orderdt.getOrderGSeq());
				paramMap[i].put("orderDSeq", orderdt.getOrderDSeq());
				paramMap[i].put("orderWSeq", orderdt.getOrderWSeq());
				
				orderdtList.add(orderdt);
				
				//= 사은품 프로모션 정보 조회
				List<HashMap<String, Object>> giftPromoList = paorderDAO.selectGiftPromo(paramMap[i]);
				
				for(HashMap<String, Object> giftPromoMap : giftPromoList) {
					goodsSelectNo = "";
					if(giftPromoMap.get("LIMIT_YN").toString().equals("1")){
						goodsSelectNo = selectSequenceNo("GOODS_SELECT_NO");
						
						promoCounsel = new PromocounselVO();
						promoCounsel.setPromoNo			(giftPromoMap.get("PROMO_NO").toString());
						promoCounsel.setOrderNo			(order_no);
						promoCounsel.setOrderGSeq		(orderdt.getOrderGSeq());
						promoCounsel.setGoodsSelectNo	(goodsSelectNo);
						promoCounsel.setLimitQty		(Long.parseLong(giftPromoMap.get("LIMIT_QTY").toString()));
						promoCounsel.setCounselQty		(orderdt.getOrderQty());
						promoCounsel.setInsertId		(user_id);
						promoCounsel.setInsertDate		(sysDateTime);
						
						l_temp = promoCounsel.getCounselQty();
						long selfOrder = paorderDAO.retrieveSumCouncelQty(promoCounsel);
						
						if (promoCounsel.getLimitQty() < selfOrder + promoCounsel.getCounselQty()) {
							// 프로모션 한정수량이 초과된 경우 
							l_temp = 0;
						}
						
						if (l_temp > 0) {
							promoCounsel.setCounselQty(l_temp);
							executedRtn = paorderDAO.insertPromoCounsel(promoCounsel); // 전량 접수 가능
							if (executedRtn != 1) {
								throw processException("msg.cannot_save", new String[] { "TPROMOCOUNSEL INSERT" });
							}
						}
					}//END OF LIMIT_YN
					
					if(giftPromoMap.get("LIMIT_YN").toString().equals("1") && l_temp == 0){
						break;//= 한정수량을 초과한 경우 사은품을 증정하지 않음.
					}
					
					//프로모션 상품 단품 선택.
					HashMap<String, Object> giftPromoDtMap= new HashMap<String, Object>();
					
					giftPromoDtMap.put("promoNo", giftPromoMap.get("PROMO_NO").toString());
					giftPromoDtMap.put("orderQty", orderdt.getOrderQty());
					
					List<Object> giftPromoDtList = paorderDAO.selectGiftPromoDt(giftPromoDtMap);
					if(giftPromoDtList.size() < 1) break; //사은품이 품절된경우 지급하지 않는다.
					
					OrderInfoMsg[] giftInfo = null;
					ParamMap[] giftStockMap = null;
					ParamMap giftMap 		= null;
					
					//= 선택된 단품의 사은품재고가 있을 경우 TORDERDT data setting.
					for (int j = 0; giftPromoDtList.size() > j; j++){
						orderDSeq++; //= d_seq 증가.
						giftMap = new ParamMap();
						giftMap.setParamMap((HashMap<String, Object>) giftPromoDtList.get(j));
						giftMap.replaceCamel();

				        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
				        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				        LocalDateTime date = LocalDateTime.parse(dateTime, inputFormatter);
				        String formattedDate = date.format(outputFormatter);
				        
						giftMap.put("applyDate", formattedDate); //= 사은품은 제휴사 주문발생 시간 기준으로 정보조회
						
						giftInfo = new OrderInfoMsg[1];
						giftStockMap = new ParamMap[1];
						
						giftInfo[0] = paorderDAO.selectGoodsInfo(giftMap); //= 사은품 상세정보 조회
						
						giftStockMap[0] = new ParamMap();
						giftStockMap[0].setParamMap((HashMap<String, Object>)paramMap[i].get());
						giftStockMap[0].put("seq", orderDSeq);
						
						giftStockMap = calStock(giftStockMap, giftInfo); // 재고 check
						if(giftStockMap == null || giftStockMap.length == 0) continue; //= 사은품 재고부족일 경우 사은품 데이터를 생성하지 않음.
						
						orderdt = new OrderdtVO();
						orderdt.setOrderNo(order_no);
						orderdt.setOrderGSeq(ComUtil.increaseLpad(Integer.toString(i) , 3, "0"));
						orderdt.setOrderDSeq(ComUtil.increaseLpad(Integer.toString(orderDSeq) , 3, "0"));
						orderdt.setOrderWSeq("001");
						orderdt.setCustNo(cust_no);
						orderdt.setOrderDate(sysDateTime);
						orderdt.setMdCode(giftInfo[0].getMdCode());
						orderdt.setReceiverSeq(paramMap[i].getString("receiverSeq"));
						orderdt.setOrderGb("10");
						orderdt.setDoFlag("20");
						orderdt.setGoodsGb("30"); //= 사은품
						orderdt.setMediaGb("03");
						orderdt.setMediaCode(paramMap[i].getString("mediaCode"));
						orderdt.setGoodsCode(giftInfo[0].getGoodsCode());
						orderdt.setGoodsdtCode(giftInfo[0].getGoodsDtCode());
						orderdt.setGoodsdtInfo(giftInfo[0].getGoodsDtInfo());
						
						orderdt.setSalePrice(0);
						orderdt.setBuyPrice(giftInfo[0].getBuyPrice());
						orderdt.setOrderQty(giftStockMap[0].getLong("orderQty"));
						orderdt.setOrderAmt(0);
						
						orderdt.setDcAmtMemb(0);
						orderdt.setDcAmtDiv(0);
						orderdt.setDcAmtCard(0);
						
						orderdt.setDcAmtGoods(0);
						orderdt.setDcRateGoods(0);
						orderdt.setDcRate(0);
						orderdt.setDcAmt(0);
						
						orderdt.setRsaleAmt(0);
						
						orderdt.setSyscancel(0);
						orderdt.setSyslast(orderdt.getOrderQty());
						orderdt.setSyslastDcGoods(0);
						orderdt.setSyslastDcMemb(0);
						orderdt.setSyslastDcDiv(0);
						orderdt.setSyslastDcCard(0);
						orderdt.setSyslastDc(0);
						
						orderdt.setSyslastAmt(0);
						
						orderdt.setWhCode(giftInfo[0].getWhCode());
						orderdt.setSaleYn("0");
						orderdt.setDelyType(giftInfo[0].getDelyType());
						orderdt.setDelyGb("10");
						orderdt.setCustDelyFlag("0");
						
						orderdt.setFirstPlanDate(DateUtil.toTimestamp(giftStockMap[0].getString("firstPlanDate"), "yyyy-MM-dd HH:mm:ss"));
						orderdt.setOutPlanDate(DateUtil.toTimestamp(giftStockMap[0].getString("outPlanDate"), "yyyy-MM-dd HH:mm:ss"));
						orderdt.setStockFlag(giftStockMap[0].getString("setStockFlag"));
						orderdt.setStockKey(giftStockMap[0].getString("stockKey"));
						orderdt.setDelyHopeDate(DateUtil.toTimestamp(giftStockMap[0].getString("delyHopeDate"), "yyyy-MM-dd HH:mm:ss"));
						
						orderdt.setPreoutGb("20");
						orderdt.setGoodsSelectNo(giftStockMap[0].getString("goodsSelectNo"));
						orderdt.setSingleDueGb("00");
						
						orderdt.setPackYn("0");
						orderdt.setAnonyYn("0");
						orderdt.setMsg(paramMap[i].getString("msg"));
						orderdt.setMsgNote("");
						orderdt.setHappyCardYn("0");
						orderdt.setSaveamt(0);
						orderdt.setSaveamtGb("90");
						orderdt.setPromoNo1("");
						orderdt.setPromoNo2("");
						orderdt.setSetGoodsCode("");
						orderdt.setReceiptYn("0");
						orderdt.setSlipYn("1");
						orderdt.setEntpslipNo("");
						orderdt.setDirectYn(giftInfo[0].getDirectShipYn());
						orderdt.setLastProcId(user_id);
						orderdt.setLastProcDate(sysDateTime);
						orderdt.setReservDelyYn("0");
						orderdt.setRsalePaAmt(0);
						orderdt.setRemark3N(0);
						orderdt.setEntpCode("0");
						orderdt.setEntpCodeOrg("0");
						
						orderdt.setPriceSeq(giftInfo[0].getPriceSeq());
						
						orderdtList.add(orderdt);
					}
					orderpromo = new Orderpromo();
					
					orderpromo.setSeq(selectSequenceNo("ORDERPROMO_SEQ"));
					orderpromo.setPromoNo(giftPromoMap.get("PROMO_NO").toString());
					orderpromo.setDoType("10");
					orderpromo.setOrderNo(order_no);
					orderpromo.setOrderGSeq(orderdt.getOrderGSeq());
					orderpromo.setProcAmt(0);
					orderpromo.setCancelAmt(0);
					orderpromo.setClaimAmt(0);
					orderpromo.setProcCost(0);
					orderpromo.setOwnProcCost(0);
					orderpromo.setEntpProcCost(0);
					orderpromo.setEntpCode(paramMap[i].getString("entpCode"));
					orderpromo.setCancelYn("0");
					orderpromo.setCancelDate(null);
					orderpromo.setCancelId("");
					orderpromo.setRemark("");
					orderpromo.setInsertId(user_id);
					orderpromo.setInsertDate(sysDateTime);
					
					orderpromoList.add(orderpromo);
				}
			}
			
			log.info("//-- update Custsystem");
			executedRtn = paorderDAO.updateCustSystemForOrder(custsystem);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] {"TCUSTSYSTEM UPDATE"});
			}
			
			//배송비
			HashMap<String, Object> obj = new HashMap<>();	
			receiverList = paorderDAO.selectReceiverList(cust_no);
			
			obj = calcShipCostN(orderdtList, receiverList);

			ordershipcostList = (List<Ordershipcost>) obj.get("ordershipcostList");
			ordercostapplyList = (List<Ordercostapply>) obj.get("ordercostapplyList");	
			long shipFeeAmt = 0;
			
			//orderGoods 세팅
			log.info("//----TORDERGOODS select");
			List<Ordergoods> ordergoodsList = new ArrayList<Ordergoods>();
			
			int orderdtListSize = orderdtList.size(); 
			
			for (int i = 0; i < orderdtListSize; i++) {
				orderdt = new OrderdtVO();
				orderdt = orderdtList.get(i);
				
				if(orderdt.getOrderDSeq().equals("001")){ //= 본품
					ordergoods = new Ordergoods();
					ordergoods.setOrderNo(order_no);
					ordergoods.setOrderGSeq(orderdt.getOrderGSeq());
					ordergoods.setCustNo(cust_no);
					ordergoods.setOrderDate(sysDateTime);
					ordergoods.setOrderGb("10");
					ordergoods.setPromoNo("");
					ordergoods.setSetYn("0");
					ordergoods.setGoodsCode(orderdt.getGoodsCode());
					ordergoods.setOrderQty(orderdt.getOrderQty());
					ordergoods.setCancelQty(0);
					ordergoods.setClaimQty(0);
					ordergoods.setReturnQty(0);
					ordergoods.setClaimCanQty(0);
					ordergoods.setExchCnt(0);
					ordergoods.setAsCnt(0);
					ordergoods.setSalePrice(orderdt.getSalePrice());
					ordergoods.setDcRate(orderdt.getDcRate());
					ordergoods.setDcAmtGoods(orderdt.getDcAmtGoods());
					ordergoods.setDcAmtMemb(orderdt.getDcAmtMemb());
					ordergoods.setDcAmtDiv(orderdt.getDcAmtDiv());
					ordergoods.setDcAmtCard(orderdt.getDcAmtCard());
					ordergoods.setDcAmt(orderdt.getDcAmt());
					ordergoods.setNorestAllotMonths(orderdt.getNorestAllotMonths());
					ordergoods.setCounseltel("");
					
					ordergoodsList.add(ordergoods);
				}
			}

			//orderpromo 세팅
			log.info("//----TORDERPROMO select");
			List<Orderpromo> orgOrderPromoList = new ArrayList<Orderpromo>();
			orgOrderPromoList = paorderDAO.selectOrderPromoList(preoutOrderInputVO[0].getOrgOrderNo()); //원주문의 프로모션 조회
			
			for (int i = 0; i < orderdtListSize; i++) {		

				orderdt = new OrderdtVO();
				orderdt = orderdtList.get(i);
				
				if(orderdt.getOrderDSeq().equals("001")){ //= 본품
					
					for(Orderpromo orgOrderPromo : orgOrderPromoList) {
						if("10".equals(orgOrderPromo.getDoType())) continue; //원주문 사은품프로모션 pass
						if(orderdt.getPoutOrgOrderNoGSeq().equals(orgOrderPromo.getOrderGSeq())) {
							
							orderpromo = new Orderpromo();
							orderpromo.setSeq(selectSequenceNo("ORDERPROMO_SEQ"));
							
							orderpromo.setPromoNo(orgOrderPromo.getPromoNo());
							orderpromo.setDoType(orgOrderPromo.getDoType());
							
							orderpromo.setOrderNo(orderdt.getOrderNo());
							orderpromo.setOrderGSeq(orderdt.getOrderGSeq());
							orderpromo.setProcAmt(orgOrderPromo.getProcAmt());
							orderpromo.setCancelAmt(0);
							orderpromo.setClaimAmt(0);
							orderpromo.setProcCost(orgOrderPromo.getProcCost());
							orderpromo.setOwnProcCost(orgOrderPromo.getOwnProcCost());
							orderpromo.setEntpProcCost(orgOrderPromo.getEntpProcCost());
							orderpromo.setEntpCode(orderdt.getEntpCode());
							orderpromo.setCancelYn("0");
							orderpromo.setCancelDate(null);
							orderpromo.setCancelId("");
							orderpromo.setRemark("");
							orderpromo.setInsertId(user_id);
							orderpromo.setInsertDate(sysDateTime);
							
							orderpromoList.add(orderpromo);
							
						}
					}
				}
			}
			
			//orderreceipt 세팅
			log.info("//----TORDERRECEIPTS select");
			
			receipt_no = selectSequenceNo("RECEIPT_NO");
			
			orderreceipts = new Orderreceipts();
			
			orderreceipts.setBlank();
			orderreceipts.setReceiptNo(receipt_no);
			orderreceipts.setOrderNo(order_no);
			orderreceipts.setCustNo(cust_no);
			orderreceipts.setDoFlag("90");
			orderreceipts.setSettleGb("11");  //외상매출  , 결제구분 G0001 
			orderreceipts.setCardBankCode("");
			orderreceipts.setBankSeq("");
			orderreceipts.setCardName("");
			orderreceipts.setCardNo("");
			orderreceipts.setCvv("");		
			orderreceipts.setDepositor(paramMap[0].getString("custName")); //TODO
			orderreceipts.setValidDate("");
			
			orderreceipts.setQuestAmt(RsaleAmt);
			
			orderreceipts.setReceiptPlanDate(null);
			orderreceipts.setOkNo("");
			orderreceipts.setOkDate(sysDateTime);
			orderreceipts.setOkMed("000");
			orderreceipts.setOkErrorCode("0000");
			orderreceipts.setVanComp("");
			orderreceipts.setPayMonth(0);
			orderreceipts.setNorestYn("0");
			orderreceipts.setNorestRate(0);
			orderreceipts.setNorestAmt(0);
			orderreceipts.setReceiptAmt(orderreceipts.getQuestAmt());
			orderreceipts.setReceiptDate(sysDateTime);
			orderreceipts.setEndYn("1");		
			orderreceipts.setRepayPbAmt(orderreceipts.getQuestAmt());		
			orderreceipts.setCancelYn("0");
			orderreceipts.setCancelCode("000");
			orderreceipts.setCancelDate(null);
			orderreceipts.setCancelId("");
			orderreceipts.setSaveamtUseFlag("0");
			orderreceipts.setCodDelyYn("0");
			orderreceipts.setDivideYn("0");
			orderreceipts.setPartialCancelYn("");
			orderreceipts.setProtxVendortxcode("");
			orderreceipts.setProtxStatus("");
			orderreceipts.setProtxStatusdetail("");
			orderreceipts.setProtxVpstxid("");
			orderreceipts.setProtxSecuritykey("");
			orderreceipts.setProtxTxauthno("");
			orderreceipts.setIssueNumber("");
			orderreceipts.setPayboxIdentifiant("");
			orderreceipts.setPayboxCodetraitement("");
			orderreceipts.setPayboxPays("");
			orderreceipts.setPayboxDateq("");
			orderreceipts.setPayboxReference("");
			orderreceipts.setCardPassword("");
			orderreceipts.setRemark1V("");
			orderreceipts.setRemark2V("");
			orderreceipts.setRemark3N(0);
			orderreceipts.setRemark4N(0);
			orderreceipts.setRemark5D(null);
			orderreceipts.setRemark6D(null);
			orderreceipts.setRemark("");
			orderreceipts.setProcId(user_id);
			orderreceipts.setProcDate(sysDateTime);
			orderreceipts.setInsertId(user_id);
			orderreceipts.setInsertDate(sysDateTime);
			
			orderreceiptsList.add(orderreceipts);
			
			//insert TORDERM
			insertOrderm(orderm);
			
			//insert TORDERGOODS
			insertOrdergoods(ordergoodsList);
			
			//insert TORDERDT
			insertOrderdt(orderdtList);
			
			//insert TORDERSHIPCOST
			insertOrdershipcost(ordershipcostList);
			
			//insert TORDERCOSTAPPLY
			insertOrderCostApply(ordercostapplyList);
			
			//insert TORDERPROMO
			insertOrderpromo(orderpromoList);
			
			//insert TORDERRECEIPTS
			insertOrderreceipts(orderreceiptsList);
			
			//update TORDERSTOCK
			updateOrderstock(orderdtList);
			
			rtnValue = getMessage("pa.success_preout_order_input");
			
			for(int k = 0; paramLength > k; k++){
				resultMap[k] = new HashMap<>();
				resultMap[k].put("MAPPING_SEQ", paramMap[k].getString("mappingSeq"));
				resultMap[k].put("POUT_ORDER_NO", paramMap[k].getString("orderNo"));

				if(paramMap[k].getString("orderPossYn").equals("1")){
					
					resultMap[k].put("POUT_ORDER_G_SEQ", 	paramMap[k].getString("orderGSeq"));
					resultMap[k].put("POUT_ORDER_D_SEQ", 	paramMap[k].getString("orderDSeq"));
					resultMap[k].put("POUT_ORDER_W_SEQ", 	paramMap[k].getString("orderWSeq"));
					resultMap[k].put("RESULT_CODE", 	"000000"); 
					resultMap[k].put("RESULT_MESSAGE", 	rtnValue);	

				}
				// 재고나 판매불가로 인한 거부
				else {
					resultMap[k].put("RESULT_CODE", 	"100001"); 
					resultMap[k].put("RESULT_MESSAGE", 	getMessage("pa.fail_preout_order_input", new String[]{getMessage("pa.out_of_stock")}));
				}

			}
			
			return resultMap;
			
		} catch (Exception e) {
			if(e.getMessage() == null){
				rtnValue = getMessage("pa.fail_preout_order_input", new String[]{e.toString()});
			} else {
				rtnValue = getMessage("pa.fail_preout_order_input", new String[]{e.getMessage()});
			}
			throw processMessageException(rtnValue);
		} finally {
			try {
				ParamMap resultParamMap = null;
				resultParamMap = new ParamMap();
				
				for(int l = 0; resultMap.length > l; l++){
					if(resultMap[l] == null) break;
							
					resultParamMap.setParamMap(resultMap[l]);
					resultParamMap.replaceCamel();
					
					if(resultParamMap.getString("resultCode").equals("000000")){
						resultParamMap.put("createYn", "1");
					} else {
						resultParamMap.put("createYn", "0");
					}
					
					executedRtn = paorderDAO.updatePreoutPaOrderm(resultParamMap);
					if(executedRtn != 1){
						throw processException("pa.fail_preout_order_input", new String[] { "TPAORDERM UPDATE ERROR" });
					}
					
				}
				
			} catch (Exception e2) {
				throw processException("pa.fail_preout_order_input", new String[] { "TPAORDERM UPDATE ERROR" });
			}
		}
		
	}

	@Override
	public int updatePreoutPaOrderm(ParamMap paramMap) throws Exception {
		return paorderDAO.updatePreoutPaOrderm(paramMap);
	}

	
	
 
}



