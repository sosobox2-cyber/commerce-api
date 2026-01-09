package com.cware.netshopping.pawemp.claim.process.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaWempClaimItemList;
import com.cware.netshopping.domain.model.PaWempClaimList;
import com.cware.netshopping.domain.model.PaWempOrderItemList;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pawemp.claim.model.Claim;
import com.cware.netshopping.pawemp.claim.model.Delivery;
import com.cware.netshopping.pawemp.claim.model.OrderProduct;
import com.cware.netshopping.pawemp.claim.model.Pickup;
import com.cware.netshopping.pawemp.claim.process.PaWempClaimProcess;
import com.cware.netshopping.pawemp.claim.repository.PaWempClaimDAO;
import com.cware.netshopping.pawemp.common.model.OrderOption;
import com.cware.netshopping.pawemp.common.model.ShipAddress;
import com.cware.netshopping.pawemp.common.repository.PaWempCommonDAO;
import com.cware.netshopping.pawemp.order.repository.PaWempOrderDAO;

@Service("pawemp.claim.paWempClaimProcess")
public class PaWempClaimProcessImpl extends AbstractService implements PaWempClaimProcess{
	
	@Resource(name = "pawemp.claim.paWempClaimDAO")
	private PaWempClaimDAO paWempClaimDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Resource(name = "pawemp.common.paWempCommonDAO")
	private PaWempCommonDAO paWempCommonDAO;
	
	@Resource(name = "pawemp.order.paWempOrderDAO")
	private PaWempOrderDAO paWempOrderDAO;
	
	@Override
	public List<PaWempClaimItemList> selectPaWempClaimItemList(String paClaimNo, String paOrderNo, String paShipNo, String paOrderGb, String paOrderSeq) throws Exception {
		ParamMap paramMap = new ParamMap();
		
		if(StringUtils.isNotBlank(paClaimNo)) {
			paramMap.put("paClaimNo", paClaimNo);
		}
		if(StringUtils.isNotBlank(paOrderNo)) {
			paramMap.put("paOrderNo", paOrderNo);
		}
		if(StringUtils.isNotBlank(paShipNo)) {
			paramMap.put("paShipNo", paShipNo);
		}
		if(StringUtils.isNotBlank(paOrderGb)) {
			paramMap.put("paOrderGb", paOrderGb);
		}
		if(StringUtils.isNotBlank(paOrderSeq)) {
			paramMap.put("paOrderSeq", paOrderSeq);
		}
		
		return paWempClaimDAO.selectPaWempClaimItemList(paramMap);
	}
	
	@Override
	public List<Object> selectOrderReturnTargetDt30List(ParamMap paramMap) throws Exception {
		return paWempClaimDAO.selectOrderReturnTargetDt30List(paramMap);
	}
	
	@Override
	public List<Object> selectOrderReturnTargetDt20List(ParamMap paramMap) throws Exception {
		return paWempClaimDAO.selectOrderReturnTargetDt20List(paramMap);
	}
	
	@Override
	public List<Object> selectOrderReturnTargetList() throws Exception{
		return paWempClaimDAO.selectOrderReturnTargetList();
	}
	
	@Override
	public String savePaWempClaim(PaWempClaimList paWempClaimList) throws Exception{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Paorderm paorderm = null;
		
		int executedRtn = 0;
		int existsCnt = paWempClaimDAO.selectPaWempClaimListExists(paWempClaimList);
		
		if(existsCnt>0){
			return Constants.SAVE_FAIL;
		}
		executedRtn = paWempClaimDAO.insertPaWempClaimList(paWempClaimList);
		if(executedRtn<1){
			throw processException("msg.cannot_save", new String[]{"TPAWEMPCLAIMLIST INSERT"});
		}
		
		for(PaWempClaimItemList claimItemList : paWempClaimList.getClaimItemList() ){
			executedRtn = paWempClaimDAO.insertPaWempClaimItemList(claimItemList);
			if(executedRtn<1){
				throw processException("msg.cannot_save", new String[]{"TPAWEMPCLAIMITEMLIST INSERT"});
			}
			paorderm = new Paorderm();
			paorderm.setPaCode(paWempClaimList.getPaCode());
			paorderm.setPaOrderGb(paWempClaimList.getPaOrderGb());
			paorderm.setPaOrderNo(claimItemList.getPaOrderNo());
			paorderm.setPaOrderSeq(claimItemList.getPaOrderSeq());
			paorderm.setPaClaimNo(claimItemList.getPaClaimNo());
			paorderm.setPaShipNo(claimItemList.getPaShipNo());
			if(claimItemList.getOptionQty() < 1) {
				paorderm.setPaProcQty(Long.toString(claimItemList.getProductQty()));
			} else {
				paorderm.setPaProcQty(Long.toString(claimItemList.getOptionQty()));
			}
			if( "02".equals(paWempClaimList.getClaimStatus()) ) {
				paorderm.setPaDoFlag("60");
			} else {
				paorderm.setPaDoFlag("20");
			}
			//관리자페이지에서 수동품절반품처리 한 경우.
			if(StringUtils.contains(paWempClaimList.getClaimReason(),"재고없음/발송불가")) {
				paorderm.setOutBefClaimGb("1");
			}else {
				paorderm.setOutBefClaimGb("0");
			}
			paorderm.setModifyId(Constants.PA_WEMP_PROC_ID);
			paorderm.setInsertDate(new Timestamp(sdf.parse(systemDAO.getSysdatetime()).getTime()));
			paorderm.setModifyDate(new Timestamp(sdf.parse(systemDAO.getSysdatetime()).getTime()));
			paorderm.setPaGroupCode(Constants.PA_WEMP_GROUP_CODE);
			
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}
		}
		
		return Constants.SAVE_SUCCESS;
	}

	@Override
	public String savePaWempReturnCancel(PaWempClaimList arrPaWempClaim) throws Exception{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Paorderm paorderm = null;
		
		int executedRtn = 0;
		List<PaWempClaimList> existCheck = null;
		PaWempClaimList paWempClaim = null;
		
		existCheck = paWempClaimDAO.selectPaWempReturnExists(arrPaWempClaim);
		
		if(!existCheck.isEmpty()){ //반품접수 데이터가 있을때
			paWempClaim = existCheck.get(0);
			if(paWempClaim.getPaOrderGb().equals("31")){//기등록된 반품 접수 
				return Constants.SAVE_SUCCESS;
			}else{
				executedRtn = paWempClaimDAO.insertPaWempClaimList(arrPaWempClaim);
				if (executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] { "TPAWEMPCLAIMLIST INSERT" });
				}
				for(PaWempClaimItemList claimItemList : arrPaWempClaim.getClaimItemList() ){
					executedRtn = paWempClaimDAO.insertPaWempClaimItemList(claimItemList);
					if(executedRtn<1){
						throw processException("msg.cannot_save", new String[]{"TPAWEMPCLAIMITEMLIST INSERT"});
					}
					//= 3. TPAORDERM INSERT
					paorderm = new Paorderm();
					paorderm.setPaCode(arrPaWempClaim.getPaCode());
					paorderm.setPaOrderGb(arrPaWempClaim.getPaOrderGb());
					paorderm.setPaOrderNo(claimItemList.getPaOrderNo());
					paorderm.setPaOrderSeq(claimItemList.getPaOrderSeq());
					paorderm.setPaClaimNo(claimItemList.getPaClaimNo());
					paorderm.setPaShipNo(claimItemList.getPaShipNo());
					if(claimItemList.getOptionQty() < 1) {
						paorderm.setPaProcQty(Long.toString(claimItemList.getProductQty()));
					} else {
						paorderm.setPaProcQty(Long.toString(claimItemList.getOptionQty()));
					}
					paorderm.setPaDoFlag("20");
					paorderm.setOutBefClaimGb("0");
					paorderm.setModifyId(Constants.PA_WEMP_PROC_ID);
					paorderm.setInsertDate(new Timestamp(sdf.parse(systemDAO.getSysdatetime()).getTime()));
					paorderm.setModifyDate(new Timestamp(sdf.parse(systemDAO.getSysdatetime()).getTime()));
					paorderm.setPaGroupCode(Constants.PA_WEMP_GROUP_CODE);
					
					executedRtn = paCommonDAO.insertPaOrderM(paorderm);
					if (executedRtn < 0) {
						throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
					}
				}
			}
		}else{
			log.error("반품접수가 안된 반품철회 내역 ( claimBundleNo : " + arrPaWempClaim.getPaClaimNo() + ", purchaseNo : " +arrPaWempClaim.getPaOrderNo() + ", bundleNo : " + arrPaWempClaim.getPaShipNo() );
		}
		
		return Constants.SAVE_SUCCESS;
	}

	@Override
	public List<Object> selectReturnCancelTargetDtList(ParamMap paramMap) throws Exception{
		return paWempClaimDAO.selectReturnCancelTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectReturnCancelTargetList() throws Exception{
		return paWempClaimDAO.selectReturnCancelTargetList();
	}

	@Override
	public List<Object> selectPickupList() throws Exception {
		return paWempClaimDAO.selectPickupList();
	}

	@Override
	public int updatePickupResult(HashMap<String, Object> pickup) throws Exception {
		return paWempClaimDAO.updatePickupResult(pickup);
	}

	@Override
	public List<Object> selectReceiveConfirmList() throws Exception {
		return paWempClaimDAO.selectReceiveConfirmList();
	}

	@Override
	public int updateReceiveConfirmResult(HashMap<String, Object> receive) throws Exception {
		return paWempClaimDAO.updateReceiveConfirmResult(receive);
	}

	@Override
	public List<Object> selectReturnApprovalList() throws Exception {
		return paWempClaimDAO.selectReturnApprovalList();
	}
	
	@Override
	public int updateReturnApprovalResult(HashMap<String, Object> returnApproval) throws Exception{
		return paWempClaimDAO.updateReturnApprovalResult(returnApproval);
	}
	
	@Override
	public PaWempClaimList makePaWempClaimList(Claim claim, String paOrderGb, String paCode) throws Exception {
	
		PaWempClaimList wempClaim = new PaWempClaimList();
		
		wempClaim.setPaCode(paCode);
		wempClaim.setPaClaimNo(Long.toString(claim.getClaimBundleNo()));
		wempClaim.setPaOrderNo(Long.toString(claim.getPurchaseNo()));
		wempClaim.setPaShipNo(Long.toString(claim.getBundleNo()));
		wempClaim.setPaOrderGb(paOrderGb);
		
		//클레임 상태
		wempClaim.setClaimStatus(claim.getClaimStatus());
		wempClaim.setRequestDate(DateUtil.toTimestamp(claim.getRequestDate(),DateUtil.WEMP_DATETIME_FORMAT));
		wempClaim.setApproveDate(DateUtil.toTimestamp(claim.getApproveDate(),DateUtil.WEMP_DATETIME_FORMAT));
		wempClaim.setPendingDate(DateUtil.toTimestamp(claim.getPendingDate(),DateUtil.WEMP_DATETIME_FORMAT));
		wempClaim.setRejectDate(DateUtil.toTimestamp(claim.getRejectDate(),DateUtil.WEMP_DATETIME_FORMAT));
		wempClaim.setClaimReason(ComUtil.subStringBytes(claim.getClaimReason(), 48));
		wempClaim.setClaimReasonDetail(ComUtil.subStringBytes(claim.getClaimReasonDetail(), 498));
		wempClaim.setPendingReason(ComUtil.subStringBytes(claim.getPendingReason(), 48));
		wempClaim.setPendingReasonDetail(ComUtil.subStringBytes(claim.getPendingReasonDetail(), 498));
		wempClaim.setRejectReason(ComUtil.subStringBytes(claim.getRejectReason(), 48));
		wempClaim.setRejectReasonDetail(ComUtil.subStringBytes(claim.getRejectReasonDetail(), 498));
		
		//CLAIM_WHO_REASON
		wempClaim.setClaimWhoReason(claim.getClaimWhoReason());
		wempClaim.setClaimFee(claim.getClaimFee());
		
		//CLAIM_SHIP_FEE_ENCLOSE
		wempClaim.setClaimShipfeeEnclose(claim.getClaimShipFeeEnclose());  //박스동봉
		
		//PICK UP  반품/교환 해당
		if(StringUtils.equals(wempClaim.getPaOrderGb(), "30") || StringUtils.equals(wempClaim.getPaOrderGb(), "40")) {
			Pickup pickup = claim.getPickup();
			wempClaim.setPickupStatus(pickup.getShipStatus());
			wempClaim.setPickupMethod(pickup.getShipMethod());
			wempClaim.setPickupScheduleDate(DateUtil.toTimestamp(pickup.getScheduleShipDate(),DateUtil.WEMP_DATE_FORMAT));
			// DELY_COMP Code가 아닌 이름으로 넘어올 경우 코드로 입력필요
			if(!StringUtils.isBlank(pickup.getParcelCompany())) {
				HashMap<String, String> delyGbMap = paWempCommonDAO.selectDelyGb(StringUtils.trim(pickup.getParcelCompany()));
				if(delyGbMap == null || StringUtils.isBlank(delyGbMap.get("PA_DELY_GB"))) {
					wempClaim.setPickupDelyComp(ComUtil.subStringBytes(pickup.getParcelCompany(), 5));  //못찾았으면 혹시 코드로 온걸수도 있으므로..
				} else {
					wempClaim.setPickupDelyComp(delyGbMap.get("PA_DELY_GB").toString());
				}
			}
			wempClaim.setPickupInvoiceNo(pickup.getInvoiceNo());
			wempClaim.setPickupName(pickup.getName());
			wempClaim.setPickupPhone(pickup.getPhone());
			
			//PICKUP.SHIPADDRESS
			ShipAddress shipAddress = pickup.getShipAddress();
			wempClaim.setPickupZipcode(shipAddress.getZipcode());
			wempClaim.setPickupBaseAddr(shipAddress.getAddrFixed());
			wempClaim.setPickupDetailAddr(shipAddress.getAddrDetail());
			wempClaim.setPickupMessage(shipAddress.getMessage());
		}
		
		//DELIVERY  교환만 해당
		if(StringUtils.equals(wempClaim.getPaOrderGb(), "40")) {
			
			Delivery delivery = claim.getDelivery();
			wempClaim.setDeliveryStatus(delivery.getShipStatus());
			wempClaim.setDeliveryMethod(delivery.getShipMethod());
			wempClaim.setDeliveryScheduleDate(DateUtil.toTimestamp(delivery.getScheduleShipDate(),DateUtil.WEMP_DATETIME_FORMAT));
			// DELY_COMP Code가 아닌 이름으로 넘어올 경우 코드로 입력필요
			if(!StringUtils.isBlank(delivery.getParcelCompany())) {
				HashMap<String, String> delyGbMap = paWempCommonDAO.selectDelyGb(StringUtils.trim(delivery.getParcelCompany()));
				if(delyGbMap == null || StringUtils.isBlank(delyGbMap.get("PA_DELY_GB"))) {
					wempClaim.setDeliveryDelyComp(ComUtil.subStringBytes(delivery.getParcelCompany(), 5));  //못찾았으면 혹시 코드로 온걸수도 있으므로..
				} else {
					wempClaim.setDeliveryDelyComp(delyGbMap.get("PA_DELY_GB").toString());
				}
			}
			wempClaim.setDeliveryInvoiceNo(delivery.getInvoiceNo());
			wempClaim.setDeliveryName(delivery.getName());
			wempClaim.setDeliveryPhone(delivery.getPhone());
			
			//DELIVERY.SHIPADDRESS
			ShipAddress shipAddress = delivery.getShipAddress();
			wempClaim.setDeliveryZipcode(shipAddress.getZipcode());
			wempClaim.setDeliveryBaseAddr(ComUtil.subStringBytes(shipAddress.getAddrFixed(), 498));
			wempClaim.setDeliveryDetailAddr(ComUtil.subStringBytes(shipAddress.getAddrDetail(), 498));
			wempClaim.setDeliveryMessage(ComUtil.subStringBytes(shipAddress.getMessage(), 98));
		}
		
		if(!claim.getOrderProduct().isEmpty()){
			List<PaWempClaimItemList> itemList = this.makePaWempClaimItemList(claim, paOrderGb);
			wempClaim.setClaimItemList(itemList);
		}
		
		wempClaim.setInsertId(Constants.PA_WEMP_PROC_ID);
		wempClaim.setModifyId(Constants.PA_WEMP_PROC_ID);
		
		return wempClaim;
	}
	
	@Override
	public List<PaWempClaimItemList> makePaWempClaimItemList(Claim claim, String paOrderGb) throws Exception {
		List<PaWempClaimItemList> claimItemList = new ArrayList<PaWempClaimItemList>();
		
		for(OrderProduct orderProduct : claim.getOrderProduct()){
			for(OrderOption orderOption : orderProduct.getOrderOption()){
				PaWempClaimItemList wempClaimItem = new PaWempClaimItemList();
				
				wempClaimItem.setPaClaimNo(Long.toString(claim.getClaimBundleNo()));
				wempClaimItem.setPaOrderNo(Long.toString(claim.getPurchaseNo()));
				wempClaimItem.setPaShipNo(Long.toString(claim.getBundleNo()));
				wempClaimItem.setPaOrderGb(paOrderGb);
				
				//TPAWEMPORDERITEMLIST에서 PA_ORDER_SEQ조회해서 채움
				ParamMap paramMap = new ParamMap();
				paramMap.put("paOrderNo", Long.toString(claim.getPurchaseNo()));
				paramMap.put("paShipNo", Long.toString(claim.getBundleNo()));
				paramMap.put("orderProductNo", Long.toString(orderProduct.getOrderNo()));
				paramMap.put("orderOptionNo", Long.toString(orderOption.getOrderOptionNo()));
				List<PaWempOrderItemList> orderItemList = paWempOrderDAO.selectPaWempOrderItemList(paramMap);
				if(orderItemList == null || orderItemList.size() < 1) {
					log.warn("makePaWempClaimItemList fail selectPaWempOrderItemList");
					continue;
				}				
				wempClaimItem.setPaOrderSeq(orderItemList.get(0).getPaOrderSeq());
				
				wempClaimItem.setOrderProductNo(Long.toString(orderProduct.getOrderNo()));
				wempClaimItem.setProductNo(Long.toString(orderProduct.getProductNo()));
				wempClaimItem.setProductName(orderProduct.getProductName());
				wempClaimItem.setProductPrice(orderProduct.getProductPrice());
				wempClaimItem.setProductQty(orderProduct.getProductQty());
				wempClaimItem.setGoodsCode(orderProduct.getSellerProductCode());
				
				wempClaimItem.setOrderOptionNo(Long.toString(orderOption.getOrderOptionNo()));
				wempClaimItem.setOptionNo(Long.toString(orderOption.getOptionNo()));
				wempClaimItem.setOptionName(orderOption.getOptionName());
				wempClaimItem.setOptionQty(orderOption.getOptionQty());
				wempClaimItem.setGoodsdtCode(orderOption.getSellerOptionCode());
				if("30".equals(paOrderGb) || "40".equals(paOrderGb)) {
					wempClaimItem.setProcFlag("10");
				}else {
					wempClaimItem.setProcFlag("00"); //[O513]기본적으로 00 접수, 용도에 따라 직접세팅필요
				}
				wempClaimItem.setInsertId(Constants.PA_WEMP_PROC_ID);
				wempClaimItem.setModifyId(Constants.PA_WEMP_PROC_ID);
				claimItemList.add(wempClaimItem);
			}
		}
		
		return claimItemList;
	}
	
	@Override
	public String savePaWempCancel(PaWempClaimList paWempClaimList) throws Exception {
		int executedRtn = 0;
		int existsCnt = paWempClaimDAO.selectPaWempClaimListExists(paWempClaimList);
		
		if(existsCnt > 0){
			return Constants.SAVE_SUCCESS;
		}
		executedRtn = paWempClaimDAO.insertPaWempClaimList(paWempClaimList);
		if(executedRtn<1){
			throw processException("msg.cannot_save", new String[]{"TPAWEMPCLAIMLIST INSERT"});
		}
		
		for(PaWempClaimItemList claimItemList : paWempClaimList.getClaimItemList() ){
			executedRtn = paWempClaimDAO.insertPaWempClaimItemList(claimItemList);
			if(executedRtn<1){
				throw processException("msg.cannot_save", new String[]{"TPAWEMPCLAIMITEMLIST INSERT"});
			}
		}
		
		return Constants.SAVE_SUCCESS;
	}
	
	@Override
	public String compareAddress(ParamMap param) throws Exception{
		return paWempClaimDAO.compareAddress(param);
	}
}
