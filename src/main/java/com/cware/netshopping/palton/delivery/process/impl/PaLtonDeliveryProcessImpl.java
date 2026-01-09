package com.cware.netshopping.palton.delivery.process.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractModel;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaLtonOrderListVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.palton.delivery.process.PaLtonDeliveryProcess;
import com.cware.netshopping.palton.delivery.repository.PaLtonDeliveryDAO;

@Service("palton.delivery.paLtonDeliveryProcessImpl")
public class PaLtonDeliveryProcessImpl extends AbstractService implements PaLtonDeliveryProcess{
	
	@Autowired
	PaLtonDeliveryDAO paLtonDeliveryDAO;
	@Autowired
	private PaCommonDAO paCommonDAO;			
	@Autowired
    private SystemService systemService;
	

	@Override
	public String saveLtonOrderList(PaLtonOrderListVO vo) throws Exception {
		//안에 절대로 Try~catch 넣지 말것!!
		int executedRtn 	 = 0;
		String rtnMsg		 = Constants.SAVE_SUCCESS;
		String dateTime 	 = systemService.getSysdatetimeToString();
		Timestamp systemTime = DateUtil.toTimestamp(dateTime);
		
		//=기본정보 세팅
		vo.setModifyId		(Constants.PA_LTON_PROC_ID);
		vo.setModifyDate	(systemTime);
		vo.setInsertDate	(systemTime);
		vo.setPaOrderGb		("10");
		
		//=발주확인
		int exists	= paLtonDeliveryDAO.countOrderList(vo);
		if(exists > 0) return "";
		
		//=INSERT TPALTONORDERLIST
		executedRtn = paLtonDeliveryDAO.insertPaltonOrderList(vo);
		if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPALTONORDERLIST INSERT" });
		
		//=INSERT TPAORDERM 
		Paorderm paorderm	= setPaOrderM(vo);
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		if (executedRtn < 0)  throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		
		return rtnMsg;
	}
	
	
	private Paorderm setPaOrderM(AbstractModel vo) {
		
		PaLtonOrderListVO orderVo = (PaLtonOrderListVO)vo;
		
		Paorderm paorderm = new Paorderm();
		paorderm.setPaOrderGb		("10");
		paorderm.setPaCode			(orderVo.getPaCode());
		paorderm.setPaOrderNo		(orderVo.getOdNo());
		paorderm.setPaOrderSeq		(ComUtil.objToStr(orderVo.getOdSeq(), null));
		paorderm.setPaClaimNo		(orderVo.getClmNo());
		paorderm.setPaShipNo		(orderVo.getProcSeq());
		paorderm.setPaShipSeq		(orderVo.getOrglProcSeq());//TODO 유효한것인지 체크 필요
		paorderm.setPaProcQty		(ComUtil.objToStr(orderVo.getOdQty(), null));
		paorderm.setPaDoFlag		("10"); //롯데온.. 출고/회수지시 연동완료 통보후 20으로 변경
		paorderm.setOutBefClaimGb	("0");

		paorderm.setInsertDate		(orderVo.getInsertDate());
		paorderm.setModifyDate		(orderVo.getModifyDate());
		paorderm.setModifyId		(Constants.PA_LTON_PROC_ID);
		paorderm.setPaGroupCode		(Constants.PA_LTON_GROUP_CODE);
		
		return paorderm;
	}

	@Override
	public List<Map<String, Object>> selectDeliveryReadyList() throws Exception {
		return paLtonDeliveryDAO.selectDeliveryReadyList();
	}


	@Override
	public int updatePaOrderMDoFlag(Map<String, Object> map) throws Exception {
		return paLtonDeliveryDAO.updatePaOrderMDoFlag(map);
	}


	@Override
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception {
		return paLtonDeliveryDAO.selectOrderInputTargetList(limitCount);
	}


	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return paLtonDeliveryDAO.selectOrderInputTargetDtList(paramMap);
	}


	@Override
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) throws Exception {
		return paLtonDeliveryDAO.selectRefusalInfo(mappingSeq);
	}


	@Override
	public List<Map<String, Object>> selectSlipOutProcList() throws Exception {
		return paLtonDeliveryDAO.selectSlipOutProcList();
	}


	@Override
	public List<Map<String, Object>> selectDeliveryCompleteList() throws Exception {
		return paLtonDeliveryDAO.selectDeliveryCompleteList();
	}


	@Override
	public int updatePreCanYn(Map<String, Object> map) throws Exception {
		return paLtonDeliveryDAO.updatePreCanYn(map);
	}


	@Override
	public OrderpromoVO selectOrderPromo(Map<String, Object> map) throws Exception {
		return paLtonDeliveryDAO.selectOrderPromo(map);
	}


	@Override
	public List<Map<String, Object>> selectExchangeHoldList() throws Exception {
		return paLtonDeliveryDAO.selectExchangeHoldList();
	}


	@Override
	public int saveHoldInfo(ParamMap paramMap) throws Exception {
		int executedRtn = 0;
		if(paramMap.get("flag").toString().equals("EXCHANGE")) {
			executedRtn = paLtonDeliveryDAO.updateExchangeAmt(paramMap); // 교환배송비 update
		}
		
		executedRtn = paLtonDeliveryDAO.updatePaHoldYn(paramMap); // SK데이터 교환 보류 상태 해제, 롯데ON 반품보류 상태 설정
		
		return executedRtn;
	}


	@Override
	public List<Map<String, Object>> selectRetrievalExceptList() throws Exception {
		return paLtonDeliveryDAO.selectRetrievalExceptList();
	}


	@Override
	public List<Map<String, Object>> selectSlipUpdateProcList() throws Exception {
		return paLtonDeliveryDAO.selectSlipUpdateProcList();
	}
	
	
	@Override
	public List<Map<String, Object>> selectDeliveryDelayProcList() throws Exception {
		return paLtonDeliveryDAO.selectDeliveryDelayProcList();
	}
	
	@Override
	public int updatePaLtonSndAgrdDttm(Map<String, Object> map) throws Exception {
		return paLtonDeliveryDAO.updatePaLtonSndAgrdDttm(map);
	}

}
