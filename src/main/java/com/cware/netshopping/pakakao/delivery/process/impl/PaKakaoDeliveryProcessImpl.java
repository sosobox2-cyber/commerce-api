package com.cware.netshopping.pakakao.delivery.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.PaKakaoOrderListVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pakakao.claim.process.impl.PaKakaoClaimProcessImpl;
import com.cware.netshopping.pakakao.delivery.process.PaKakaoDeliveryProcess;
import com.cware.netshopping.pakakao.delivery.repository.PaKakaoDeliveryDAO;

@Service("pakakao.delivery.paKakaoDeliveryProcessImpl")
public class PaKakaoDeliveryProcessImpl extends AbstractService implements PaKakaoDeliveryProcess{
	
	@Autowired
	PaKakaoDeliveryDAO paKakaoDeliveryDAO;
	
	//@Autowired
	//private SystemService systemService;

	@Autowired
	private PaCommonDAO paCommonDAO;
	
	@Autowired
	private PaKakaoClaimProcessImpl paKakaoClaimProcessImpl;

	@Override
	public int countOrderList(String orderId) throws Exception {
		return paKakaoDeliveryDAO.countOrderList(orderId);
	}
	
	@Override
	public int updatePreCanYn(Map<String, Object> map) throws Exception {
		return paKakaoDeliveryDAO.updatePreCanYn(map);
	}

	@Override
	public String saveKakaoOrderList(PaKakaoOrderListVO paKakaoOrderListVO) throws Exception {
		int	executedRtn = 0;
		String rtnMsg = Constants.SAVE_SUCCESS;
	
		executedRtn = paKakaoDeliveryDAO.insertPaKakaoOrderList(paKakaoOrderListVO);
		if(executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAKAKAOORDERLIST INSERT" });
		}
		
		Paorderm paorderm = setPaOrderM(paKakaoOrderListVO);
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		if(executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		
		return rtnMsg;
		
	}

	private Paorderm setPaOrderM(PaKakaoOrderListVO paKakaoOrderListVO) {
		
		PaKakaoOrderListVO orderVo = (PaKakaoOrderListVO) paKakaoOrderListVO;
		
		Paorderm paorderm = new Paorderm();
		paorderm.setPaCode	 	 (orderVo.getPaCode());
		paorderm.setPaOrderGb	 (paKakaoOrderListVO.getPaOrderGb());
		paorderm.setPaDoFlag	 ("20");
		paorderm.setPaOrderNo	 (orderVo.getPaymentId());				// Stoa 쪽에서 받아들이는 주문이랑 동일하게 하기 위해 결제 번호를 키값으로 넣음
		paorderm.setPaOrderSeq	 (orderVo.getId());						// KAKAO는 단품마다 주문번호가 다르게 들어옴 	
		paorderm.setPaProcQty	 (ComUtil.objToStr(orderVo.getQuantity()));
		paorderm.setApiResultCode("000000");
		paorderm.setApiResultMessage("승인처리");
		paorderm.setOutBefClaimGb("0");
		paorderm.setInsertDate	 (orderVo.getInsertDate());
		paorderm.setModifyDate	 (orderVo.getModifyDate());
		paorderm.setModifyId	 (Constants.PA_KAKAO_PROC_ID);
		paorderm.setPaGroupCode	 (Constants.PA_KAKAO_GROUP_CODE);

		return paorderm;
	}

	@Override
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception {
		return paKakaoDeliveryDAO.selectOrderInputTargetList(limitCount);
	}

	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(Map<String, String> order) throws Exception {
		return paKakaoDeliveryDAO.selectOrderInputTargetDtList(order);
	}

	@Override
	public int updatePaOrderMDoFlag(Map<String, Object> map) throws Exception {
		return paKakaoDeliveryDAO.updatePaOrderMDoFlag(map);
	}

	@Override
	public HashMap<String, Object> selectRefusalInfo(String string) throws Exception {
		return paKakaoDeliveryDAO.selectRefusalInfo(string);
	}

	@Override
	public List<Map<String, Object>> selectSlipOutProcList() throws Exception {
		return paKakaoDeliveryDAO.selectSlipOutProcList();
	}
	
	@Override
	public List<Map<String, Object>> selectSlipOutProcOptionList(HashMap<String, Object> saveMap) throws Exception {
		return paKakaoDeliveryDAO.selectSlipOutProcOptionList(saveMap);
	}

	@Override
	public List<Map<String, Object>> selectDelayOrderList() throws Exception {
		return paKakaoDeliveryDAO.selectDelayOrderList();
	}

	@Override
	public int updatePaKakaoDeliveryExpectedAt(Map<String, Object> delayMap) throws Exception {
		int executedRtn = 0;
		
		executedRtn = paKakaoDeliveryDAO.updatePaKakaoDeliveryExpectedAt(delayMap);
		if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM.REMARK1_V UPDATE" });
		
		return executedRtn;
	}

	@Override
	public void updatePaKakaoDeliveryComplete(PaKakaoOrderListVO vo) throws Exception {
		int executedRtn 	 = 0;
		
		Map<String, Object> completeOrder= setCompleteOrder(vo);
			
		executedRtn = paKakaoDeliveryDAO.countChkOrderComplete(completeOrder);
		if(executedRtn > 0) return;
		
		executedRtn = paKakaoDeliveryDAO.updatePaOrderMDoFlag(completeOrder);
		if (executedRtn < 1) {		
			throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
		}
	}
	
	private Map<String, Object> setCompleteOrder(PaKakaoOrderListVO vo) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("PA_CODE", vo.getPaCode());
		map.put("PA_ORDER_NO", vo.getPaymentId());
		map.put("PA_ORDER_SEQ", vo.getId());
		map.put("PA_ORDER_GB", "10");
		map.put("API_RESULT_CODE", "000000");
		map.put("API_RESULT_MESSAGE", "배송완료처리");
		map.put("PA_DO_FLAG", "80");
    
		return map;
	}

	@Override
	public void updateBuyDecision(PaKakaoOrderListVO vo) throws Exception {
		int executedRtn = 0;
		int checkDecision = 0;
		
		
		checkDecision = paKakaoDeliveryDAO.checkUpdateBuyDecision(vo);
		if(checkDecision > 0) return ;
		
		updatePaKakaoDeliveryComplete(vo);
		if("40".equals(vo.getPaOrderGb())) {
			paKakaoClaimProcessImpl.updateKakaoExchangeComplete(vo);
		}
		
		executedRtn = paKakaoDeliveryDAO.updateBuyDecision(vo);
		if (executedRtn < 1) {		
			throw processException("msg.cannot_save", new String[] { "TPAKAKAOORDERLIST UPDATE" });
		}	
	}
}
