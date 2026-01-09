package com.cware.netshopping.passg.delivery.process.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaSsgOrderListVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.passg.claim.repository.PaSsgClaimDAO;
import com.cware.netshopping.passg.delivery.process.PaSsgDeliveryProcess;
import com.cware.netshopping.passg.delivery.repository.PaSsgDeliveryDAO;

@Service("passg.delivery.paSsgDeliveryProcessImpl")
public class PaSsgDeliveryProcessImpl extends AbstractService implements PaSsgDeliveryProcess{
	
	@Autowired
	PaSsgDeliveryDAO paSsgDeliveryDAO;
	
	@Autowired
	PaSsgClaimDAO paSsgClaimDAO;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private PaCommonDAO paCommonDAO;

	@Override
	public String saveSsgOrderList(PaSsgOrderListVO vo) throws Exception {
		int	executedRtn = 0;
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = systemService.getSysdatetimeToString();
		Timestamp systemTime = DateUtil.toTimestamp(dateTime);
		
		vo.setInsertId(Constants.PA_SSG_PROC_ID);
		vo.setInsertDate(systemTime);
		vo.setModifyDate(systemTime);
		if("15".equals(vo.getShppDivDtlCd())) { //교환출고
			vo.setPaOrderGb("40");
			
			int exists = paSsgDeliveryDAO.countChangeWhOut(vo);
			if(exists < 1) return "";
			
			executedRtn = paSsgDeliveryDAO.updateChangeOrderDoFlag(vo);
			if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			
		}else {
			vo.setPaOrderGb("10");
			
			int exists = paSsgDeliveryDAO.countOrderList(vo);
			if(exists > 0) return "";
			
			executedRtn = paSsgDeliveryDAO.insertPaSsgOrderList(vo);
			if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPASSGORDERLIST INSERT" });
			
			Paorderm paorderm = setPaOrderM(vo);
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
			if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		
		return rtnMsg;
	}
	
	@Override
	public String saveSsgChangeOrderList(PaSsgOrderListVO vo) throws Exception {
		int	executedRtn = 0;
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = systemService.getSysdatetimeToString();
		Timestamp systemTime = DateUtil.toTimestamp(dateTime);
		
		if(vo.getOrordNo() == null) {
			//SSG에서 API문서와 다르게 데이터 줘서 수정 전 임시 소스
			vo.setOrordNo(vo.getOrOrdNo());
		}
		
		if("15".equals(vo.getShppDivDtlCd())) {
			vo.setShpplocRoadAddr(vo.getOrdpeRoadAddr());
		}
		
		vo.setInsertId(Constants.PA_SSG_PROC_ID);
		vo.setInsertDate(systemTime);
		vo.setModifyDate(systemTime);		
	    ParamMap paramMap = new ParamMap();
	    paramMap.put("paCode", vo.getPaCode());
	    paramMap.put("itemId", vo.getItemId());
	    paramMap.put("uitemId", vo.getUitemId());
	    HashMap<String, Object> goodsInfo = paSsgClaimDAO.selectSsgExchangeGoodsInfo(paramMap);
	    
	    vo.setGoodsCode(goodsInfo.get("GOODS_CODE").toString());
	    vo.setGoodsDtCode(goodsInfo.get("GOODSDT_CODE").toString());
		
		int exists = paSsgDeliveryDAO.countChangeOrderList(vo);
		if(exists > 0) return "";
		
		executedRtn = paSsgDeliveryDAO.insertPaSsgOrderList(vo);
		if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPASSGORDERLIST INSERT" });
		
		Paorderm paorderm = setPaOrderM(vo);
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		return rtnMsg;
	}

	private Paorderm setPaOrderM(PaSsgOrderListVO vo) {
		
		PaSsgOrderListVO orderVo = (PaSsgOrderListVO) vo;
		
		Paorderm paorderm = new Paorderm();
		paorderm.setPaCode	 	 (orderVo.getPaCode());
		paorderm.setPaOrderGb	 (orderVo.getPaOrderGb());
		paorderm.setPaShipNo	 (orderVo.getShppNo());
		paorderm.setPaShipSeq	 (orderVo.getShppSeq());
		paorderm.setPaProcQty	 (ComUtil.objToStr(orderVo.getDircItemQty(), null));
		paorderm.setOutBefClaimGb("0");
		paorderm.setInsertDate	 (orderVo.getInsertDate());
		paorderm.setModifyDate	 (orderVo.getModifyDate());
		paorderm.setModifyId	 (Constants.PA_SSG_PROC_ID);
		paorderm.setPaGroupCode	 (Constants.PA_SSG_GROUP_CODE);
		paorderm.setPaOrderNo	 (orderVo.getOrordNo());			
		paorderm.setPaOrderSeq	 (ComUtil.objToStr(orderVo.getOrordItemSeq(), null));
		
		if("15".equals(vo.getShppDivDtlCd())) { //교환출고
			paorderm.setPaDoFlag("10");
			paorderm.setPaClaimNo(vo.getOrdNo());
			paorderm.setChangeFlag("01");
			paorderm.setChangeGoodsCode(vo.getGoodsCode());
			paorderm.setChangeGoodsdtCode(vo.getGoodsDtCode());
		}else {
			paorderm.setPaDoFlag	 ("30");
		}
		
		return paorderm;
	}
	
	@Override
	public int updatePreCanYn(Map<String, Object> map) throws Exception {
		return paSsgDeliveryDAO.updatePreCanYn(map);
	}

	@Override
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception {
		return paSsgDeliveryDAO.selectOrderInputTargetList(limitCount);
	}

	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(Map<String, String> order) throws Exception {
		return paSsgDeliveryDAO.selectOrderInputTargetDtList(order);
	}

	@Override
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) throws Exception {
		return paSsgDeliveryDAO.selectRefusalInfo(mappingSeq);
	}

	@Override
	public int updatePaOrderMDoFlag(ParamMap paramMap) throws Exception {
		
		int result = 0;
		
		result = paSsgDeliveryDAO.updatePaOrderMDoFlag(paramMap);
		
		if(result < 1) throw new Exception();
		
		return result;
	}

	@Override
	public List<Map<String, Object>> selectPartialSlipList() throws Exception {
		return paSsgDeliveryDAO.selectPartialSlipList();
	}
	
	@Override
	public List<Map<String, Object>> selectSlipOutProcList() throws Exception {
		return paSsgDeliveryDAO.selectSlipOutProcList();
	}

	@Override
	public List<Map<String, Object>> selectReleaseOrderList() throws Exception {
		return paSsgDeliveryDAO.selectReleaseOrderList();
	}

	@Override
	public List<Map<String, Object>> selectOrderCompleteList() throws Exception {
		return paSsgDeliveryDAO.selectOrderCompleteList();
	}

	@Override
	public List<Map<String, Object>> selectPaSsgChangeConfirmList() throws Exception {
		return paSsgDeliveryDAO.selectPaSsgChangeConfirmList();
	}

	@Override
	public List<Map<String, Object>> selectDelayOrderList() throws Exception {
		return paSsgDeliveryDAO.selectDelayOrderList();
	}

	@Override
	public int updatePaSsgWhinExpcDt(ParamMap paramMap) throws Exception {
		int executedRtn = 0;
		
		executedRtn = paSsgDeliveryDAO.updatePaSsgWhinExpcDt(paramMap);
		if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "WHOUT_CRITN_DT UPDATE" });
		
		paramMap.put("remark", "출고지연처리 성공 : " + paramMap.get("dateTime").toString());
		executedRtn = paSsgDeliveryDAO.updatePaOrderMRemark(paramMap);
		if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM.REMARK1_V UPDATE" });
		
		return executedRtn;
	}
	
	@Override
	public int updatePartialFlag(ParamMap paramMap) throws Exception {
		int	executedRtn = 0;
		
		executedRtn = paSsgDeliveryDAO.updatePartialFlag(paramMap);
		if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPASSGORDERLIST UPDATE" });
		
		executedRtn = paSsgDeliveryDAO.updatePartialHoldYn(paramMap);
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
		
		return executedRtn;
	}

	@Override
	public int updatePartialShhpNo(Map<String, Object> partialDataMap) throws Exception {
		int	executedRtn = 0;
		ParamMap paramMap = new ParamMap();
		
		executedRtn = paSsgDeliveryDAO.updatePartialShhpNo(partialDataMap);
		if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPASSGORDERLIST UPDATE" });
		
		paramMap.put("mappingSeq", partialDataMap.get("MAPPING_SEQ").toString());
		paramMap.put("paHoldYn", "0");
		
		executedRtn = paSsgDeliveryDAO.updatePartialHoldYn(paramMap);
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
		
		return executedRtn;
	}
	
	@Override
	public List<Map<String, Object>> selectPartialDeliveryList() throws Exception {
		return paSsgDeliveryDAO.selectPartialDeliveryList();
	}
}
