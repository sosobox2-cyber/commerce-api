package com.cware.netshopping.patmon.delivery.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractModel;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaTmonOrderListVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.patmon.delivery.process.PaTmonDeliveryProcess;
import com.cware.netshopping.patmon.delivery.repository.PaTmonDeliveryDAO;

@Service("patmon.delivery.paTmonDeliveryProcessImpl")
public class PaTmonDeliveryProcessImpl extends AbstractService implements PaTmonDeliveryProcess{
	
	@Autowired
	PaTmonDeliveryDAO paTmonDeliveryDAO;
	@Autowired
	private PaCommonDAO paCommonDAO;	

	@Override
	public String saveTmonOrderList(List<PaTmonOrderListVO> paTmonOrderList) throws Exception {
		//안에 절대로 Try~catch 넣지 말것!!
		int executedRtn 	 = 0;
		String rtnMsg		 = Constants.SAVE_SUCCESS;
		int exists = 0;
		
		exists = paTmonDeliveryDAO.countPaOrderList(paTmonOrderList.get(0));
		if(exists > 0) {
			return "";		
		}
		
		for(int i=0; i<paTmonOrderList.size(); i++) {
			
			//=INSERT TPATMONORDERLIST
			executedRtn = paTmonDeliveryDAO.insertPaTmonOrderList(paTmonOrderList.get(i));
			if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPATMONORDERLIST INSERT" });
			
			//=INSERT TPAORDERM 
			Paorderm paorderm	= setPaOrderM(paTmonOrderList.get(i));
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
			if (executedRtn < 1)  throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		return rtnMsg;
	}
	
	private Paorderm setPaOrderM(AbstractModel vo) {
		
		PaTmonOrderListVO orderVo = (PaTmonOrderListVO)vo;
		
		Paorderm paorderm = new Paorderm();
		paorderm.setPaCode(orderVo.getPaCode());
		paorderm.setPaOrderGb("10");
		paorderm.setPaOrderNo(orderVo.getTmonOrderNo());
		paorderm.setPaShipNo(orderVo.getDeliveryNo());
		paorderm.setPaOrderSeq(orderVo.getTmonDealOptionNo());
		paorderm.setPaProcQty(ComUtil.objToStr(orderVo.getQty(), null));
		paorderm.setPaDoFlag("D1".equals(orderVo.getDeliveryStatus())?"10":"20");
		paorderm.setOutBefClaimGb("0");		
		paorderm.setInsertDate(orderVo.getInsertDate());
		paorderm.setModifyDate(orderVo.getModifyDate());
		paorderm.setModifyId(Constants.PA_TMON_PROC_ID);
		paorderm.setPaGroupCode(Constants.PA_TMON_GROUP_CODE);
		
		return paorderm;
	}
	
	@Override
	public List<Map<String, Object>> selectDeliveryReadyList() throws Exception {
		return paTmonDeliveryDAO.selectDeliveryReadyList();
	}
	
	@Override
	public List<Map<String, Object>> selectDeliveryReadyDetailList(ParamMap paramMap) throws Exception {
		return paTmonDeliveryDAO.selectDeliveryReadyDetailList(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> selectSlipOutProcList() throws Exception {
		return paTmonDeliveryDAO.selectSlipOutProcList();
	}
	
	@Override
	public List<Map<String, Object>> selectUpdateSlipOutProcList() throws Exception {
		return paTmonDeliveryDAO.selectUpdateSlipOutProcList();
	}
	
	@Override
	public List<Map<String, Object>> selectSlipOutProcOptionList(ParamMap paramMap) throws Exception {
		return paTmonDeliveryDAO.selectSlipOutProcOptionList(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> selectUpdateSlipOutProcOptionList(ParamMap paramMap) throws Exception {
		return paTmonDeliveryDAO.selectUpdateSlipOutProcOptionList(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception {
		return paTmonDeliveryDAO.selectOrderInputTargetList(limitCount);
	}
	
	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return paTmonDeliveryDAO.selectOrderInputTargetDtList(paramMap);
	}
	
	@Override
	public OrderpromoVO selectOrderPromo(Map<String, Object> map) throws Exception {
		return paTmonDeliveryDAO.selectOrderPromo(map);
	}
	
	@Override
	public String selectTmonOrderSeq(String tmonOrderNo, String deliveryNo) throws Exception {
		String orderSeq = "";
		int result = 0;
		
		int countOrder = paTmonDeliveryDAO.countOrder(tmonOrderNo, deliveryNo);
		if(countOrder > 0) {
			result = paTmonDeliveryDAO.selectTmonOrderSeq(tmonOrderNo, deliveryNo);
		}else {
			result = 0;
		}
		
		result++;
		
		orderSeq = String.format("%03d",result);
		
		return orderSeq;
	}
	
	@Override
	public int updatePaOrderMPreCancelYn(String tmonOrderNo) throws Exception{
		return paTmonDeliveryDAO.updatePaOrderMPreCancelYn(tmonOrderNo);
	}
	
	@Override
	public int updatePreCanYn(Map<String, Object> map) throws Exception {
		return paTmonDeliveryDAO.updatePreCanYn(map);
	}
	
	@Override
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) throws Exception {
		return paTmonDeliveryDAO.selectRefusalInfo(mappingSeq);
	}
	
	@Override
	public List<Map<String, Object>> selectDeliveryConfirmList() throws Exception {
		return paTmonDeliveryDAO.selectDeliveryConfirmList();
	}
	
	@Override
	public List<Map<String, Object>> selectDeliveryCompleteList(int searchProcDate) throws Exception {
		return paTmonDeliveryDAO.selectDeliveryCompleteList(searchProcDate);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public String updatePaTmonOrderListInitial(PaTmonOrderListVO vo) throws Exception {
		int executedRtn 	 = 0;
		String rtnMsg		 = "";
		
		for(int i=0; i < vo.getDeliveryFees().size(); i++) {
			HashMap<String, Object> dealiveryMap = (HashMap<String, Object>) vo.getDeliveryFees().get(i);
			vo.setDeliveryNo(dealiveryMap.get("deliveryNo").toString());
			
			List<Map<String, Object>> dealOptionList = paTmonDeliveryDAO.selectOrderTmonDealOptionList(vo);
			
			for(Map<String, Object> dealOption : dealOptionList) {
				for(int j=0; j < vo.getDealOptionPromotions().size(); j++) {
					HashMap<String, Object> promoMap = (HashMap<String, Object>) vo.getDealOptionPromotions().get(j);
					if(dealOption.get("TMON_DEAL_OPTION_NO").toString().equals(promoMap.get("tmonDealOptionNo").toString())) {
					
						PaTmonOrderListVO orderInitial= setOrderInitial(vo, dealiveryMap, promoMap);
						
						//=UPDATE TPATMONORDERLIST
						executedRtn = paTmonDeliveryDAO.updateOrderInitial(orderInitial);
						if (executedRtn < 1)  throw processException("msg.cannot_save", new String[] { "TPATMONORDERLIST UPDATE" });
					}
				}
			}
		}
		
		//=UPDATE TPAORDERM
		executedRtn = paTmonDeliveryDAO.updatePaOrderMDoFlag30(vo);
		if (executedRtn < 1)  throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
		
		rtnMsg = Constants.SAVE_SUCCESS;
		
		return rtnMsg;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public String updatePaTmonDeliveryComplete(PaTmonOrderListVO vo) throws Exception {
		int executedRtn 	 = 0;
		String rtnMsg		 = "";
		
		for(int i=0; i < vo.getDeals().size(); i++) {
			HashMap<String, Object> map = (HashMap<String, Object>) vo.getDeals().get(i);
			if("D5".equals(map.get("deliveryStatus").toString())) {
				ParamMap completeOrder= setCompleteOrder(vo, map);
				
				executedRtn = paTmonDeliveryDAO.countChkOrderComplete(completeOrder);
				if(executedRtn > 0) continue;
				
				executedRtn = paTmonDeliveryDAO.updatePaOrderMDoFlag(completeOrder);
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
				} else {
					rtnMsg = Constants.SAVE_SUCCESS;
				}
			} else {
				rtnMsg = Constants.SAVE_SUCCESS;
			}
		}
		
		return rtnMsg;
	}
	
	@Override
	public int updatePaOrderMDoFlag(ParamMap paramMap) throws Exception {
		return paTmonDeliveryDAO.updatePaOrderMDoFlag(paramMap);
	}
	
	private PaTmonOrderListVO setOrderInitial(PaTmonOrderListVO vo, HashMap<String, Object> dealiveryMap, HashMap<String, Object> promoMap) {
		PaTmonOrderListVO orderInitial = new PaTmonOrderListVO();
		
		orderInitial.setTmonOrderNo(vo.getTmonOrderNo());
		orderInitial.setPaCode(vo.getPaCode());
		
		orderInitial.setDeliveryNo(dealiveryMap.get("deliveryNo").toString());
		orderInitial.setAmount(ComUtil.objToLong(dealiveryMap.get("amount")));  
		orderInitial.setTmonAmount(ComUtil.objToLong(dealiveryMap.get("tmonAmount")));
		orderInitial.setPartnerAmount(ComUtil.objToLong(dealiveryMap.get("partnerAmount")));
		orderInitial.setUserAmount(ComUtil.objToLong(dealiveryMap.get("userAmount")));
		orderInitial.setLongDistanceAmount(ComUtil.objToLong(dealiveryMap.get("longDistanceAmount")));
		orderInitial.setLongDistanceTmonAmount(ComUtil.objToLong(dealiveryMap.get("longDistanceTmonAmount")));
		orderInitial.setLongDistancePartnerAmount(ComUtil.objToLong(dealiveryMap.get("longDistancePartnerAmount")));
		orderInitial.setLongDistanceUserAmount(ComUtil.objToLong(dealiveryMap.get("longDistanceUserAmount")));
		
		orderInitial.setTmonDealOptionNo(promoMap.get("tmonDealOptionNo").toString());
		orderInitial.setDiscountAmount(ComUtil.objToLong(promoMap.get("discountAmount")));
		orderInitial.setPartnerDiscountAmount(ComUtil.objToLong(promoMap.get("partnerDiscountAmount")));
		orderInitial.setCouponNo(promoMap.containsKey("couponNo")?promoMap.get("couponNo").toString():"");
		orderInitial.setDiscountPolicyNo(promoMap.containsKey("discountPolicyNo")?promoMap.get("discountPolicyNo").toString():"");
		
		return orderInitial;
	}
	
	private ParamMap setCompleteOrder(PaTmonOrderListVO vo, HashMap<String, Object> map) {
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("paCode", vo.getPaCode());
		paramMap.put("paOrderNo",vo.getTmonOrderNo() );
		paramMap.put("paShipNo", map.get("deliveryNo").toString());
		paramMap.put("tmonDealNo", map.get("tmonDealNo").toString());
		paramMap.put("paDoFlagOrg", "40");
		paramMap.put("PA_DO_FLAG", "80");
		paramMap.put("API_RESULT_CODE", "000000");
		paramMap.put("API_RESULT_MESSAGE", "배송완료처리");
		
		return paramMap;
	}

}
