package com.cware.netshopping.pahalf.order.process.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaHalfOrderListVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pahalf.order.process.PaHalfOrderProcess;
import com.cware.netshopping.pahalf.order.repository.PaHalfOrderDAO;
import com.cware.netshopping.pahalf.util.PaHalfComUtill;

@Service("pahalf.order.paHalfOrderProcess")
public class PaHalfOrderProcessImpl extends AbstractService implements PaHalfOrderProcess {
	
	@Resource(name = "pahalf.order.paHalfOrderDAO")
	private PaHalfOrderDAO paHalfOrderDAO;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Autowired
	private PaCommonDAO paCommonDAO;

	
	
	@SuppressWarnings("unchecked")
	public String savePaHalfOrder(Map<String, Object> mainOrder) throws Exception {
		String rtnMsg		 = Constants.SAVE_SUCCESS;
		List<Map<String,Object>> saleDetailList = (List<Map<String, Object>>) mainOrder.get("saleDetailList");
		
		for(Map<String,Object> order : saleDetailList) {
			PaHalfOrderListVO  paHalfOrderListVO= (PaHalfOrderListVO) PaHalfComUtill.map2VO(order, PaHalfOrderListVO.class);
			PaHalfComUtill.map2VO(mainOrder, paHalfOrderListVO);
			//주문으로 들어왔는데 원주문 번호가 있는 경우 : 교환번호로 update
			if(paHalfOrderListVO.getOriOrdNo() != null) { 
				paHalfOrderDAO.updateExchangeOrdNo(paHalfOrderListVO);
				continue;
			} 
			savePaHalfOrder(paHalfOrderListVO);	
		}
		
		return rtnMsg;
	}
	
	
	private String savePaHalfOrder(PaHalfOrderListVO paHalfOrderListVO) throws Exception {
		int executedRtn 	 = 0;
		String rtnMsg		 = Constants.SAVE_SUCCESS;
		String dateTime 	 = systemService.getSysdatetimeToString();
		Timestamp systemTime = DateUtil.toTimestamp(dateTime);
		
		//=기본정보 세팅
		paHalfOrderListVO.setModifyId		(Constants.PA_HALF_PROC_ID);
		paHalfOrderListVO.setModifyDate		(systemTime);
		paHalfOrderListVO.setInsertDate		(systemTime);
		paHalfOrderListVO.setPaOrderGb		("10");
		paHalfOrderListVO.setOrderGb		("10");
		
		//=발주확인
		int exists	=  paHalfOrderDAO.selectCountOrderList(paHalfOrderListVO);
		if(exists > 0) return "";
		
		//=INSERT TPAHALFORDERLIST
		executedRtn = paHalfOrderDAO.insertPaHalfOrderList(paHalfOrderListVO); 
		if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAHALFORDERLIST INSERT" });
		
		//=INSERT TPAORDERM 
		insertPaOrderm(paHalfOrderListVO, new Paorderm());
		
		return rtnMsg;
	}
	
	private int insertPaOrderm(PaHalfOrderListVO orderVo , Paorderm paorderm) throws Exception{
		int executedRtn 	 = 0;
		String outBefCalimGb = "0";
		String paDoFlag		 = "";
		
		switch (orderVo.getOrderGb()) {
		case "10":
			paDoFlag = "10";
			break;

		case "20":
			int doFlag = Integer.parseInt(ComUtil.NVL(paHalfOrderDAO.selectDoflag(orderVo),"0")) ;
			
			if( doFlag >= 30) { //출하지시 이후건은 출고전 반품
				outBefCalimGb = "1";
				paDoFlag 	  = "60";
			}else{
				outBefCalimGb = "0";
				paDoFlag 	  = "20";
			}
			break;
		
		case "30": //교환주문 취소건 or 직매입 출하지시이후건 -> 반품생성
			paDoFlag	  = "60";
			outBefCalimGb = "0";
			break;
			
		default:
			throw processException("msg.cannot_save", new String[] { "TPAORDERM_ORDER_GB" });
		}
		
		paorderm.setPaOrderGb		(orderVo.getOrderGb());
		paorderm.setPaCode			(orderVo.getPaCode());
		paorderm.setPaOrderNo		(orderVo.getOrdNo());
		paorderm.setPaOrderSeq		(ComUtil.objToStr(orderVo.getOrdNoNm(), null));
		paorderm.setPaClaimNo	 	(orderVo.getClaimNo());
		paorderm.setPaShipNo		(orderVo.getBasketNo());
		paorderm.setPaProcQty		(ComUtil.objToStr(orderVo.getQty(), null));
		paorderm.setPaDoFlag		(paDoFlag);
		paorderm.setOutBefClaimGb	(outBefCalimGb);
		paorderm.setInsertDate		(orderVo.getInsertDate());
		paorderm.setModifyDate		(orderVo.getModifyDate());
		paorderm.setModifyId		(Constants.PA_HALF_PROC_ID);
		paorderm.setPaGroupCode		(Constants.PA_HALF_GROUP_CODE);
		
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		if (executedRtn < 0)  throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		
		return executedRtn;
	}

	@Override
	public List<Map<String, Object>> selectOrderConfirmList(String paCode) throws Exception {
		return paHalfOrderDAO.selectOrderConfirmList(paCode);
	}
	

	@Override
	public int updateTPaorderm(Map<String, Object> order) throws Exception {
		return paHalfOrderDAO.updateTPaorderm(order);
	}

	@Override
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception {
		return paHalfOrderDAO.selectOrderInputTargetList(limitCount);
	}

	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return paHalfOrderDAO.selectOrderInputTargetDtList(paramMap);
	}

	@Override
	public Map<String, Object> selectRefusalInfo(String mappingSeq) throws Exception {
		return paHalfOrderDAO.selectRefusalInfo(mappingSeq);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String savePaHalfCancel(Map<String, Object> cancelMain) throws Exception {
		String rtnMsg		 = Constants.SAVE_SUCCESS;
		List<Map<String,Object>> saleDetailList = (List<Map<String, Object>>) cancelMain.get("saleDetailList");
		
		for(Map<String,Object> cancel : saleDetailList) {
			PaHalfOrderListVO  paHalfOrderListVO= (PaHalfOrderListVO) PaHalfComUtill.map2VO(cancel, PaHalfOrderListVO.class);
			PaHalfComUtill.map2VO(cancelMain, paHalfOrderListVO); 
			savePaHalfCancel(paHalfOrderListVO);	
		}
		
		return rtnMsg;
	}
	
	private String savePaHalfCancel(PaHalfOrderListVO paHalfOrderListVO) throws Exception {
		int executedRtn 	 = 0;
		String rtnMsg		 = Constants.SAVE_SUCCESS;
		String dateTime 	 = systemService.getSysdatetimeToString();
		Timestamp systemTime = DateUtil.toTimestamp(dateTime);
		
		//=기본정보 세팅
		paHalfOrderListVO.setModifyId		(Constants.PA_HALF_PROC_ID);
		paHalfOrderListVO.setModifyDate		(systemTime);
		paHalfOrderListVO.setInsertDate		(systemTime);
		paHalfOrderListVO.setPaOrderGb		("20");
		paHalfOrderListVO.setOrderGb		("20");
		
		//채번된 교환주문에 대한 취소인입
        if(paHalfOrderListVO.getOriOrdNo() != null && !"".equals(paHalfOrderListVO.getOriOrdNo()) ) {
            String temp = paHalfOrderListVO.getOrdNo();
            paHalfOrderListVO.setOrdNo(paHalfOrderListVO.getOriOrdNo());
            paHalfOrderListVO.setOriOrdNo(temp);
            
            paHalfOrderListVO.setPaOrderGb		("30");
    		paHalfOrderListVO.setOrderGb		("30");
    		paHalfOrderListVO.setBpPickupTypeNm("업체직접수거");
    		
    		//ClaimRequestDate 및 ClaimNo Setting
    		paHalfOrderListVO.setClaimRequestDate	(paHalfOrderListVO.getRefundDate() );
    		paHalfOrderListVO.setClaimNo			(paHalfOrderListVO.getRefundNo());
    		
    		//교환배송지정보로 setting
    		paHalfOrderListVO.setRefundFromAddr(paHalfOrderListVO.getToAddr());
    		paHalfOrderListVO.setRefundFromZiCd(paHalfOrderListVO.getToZiCd());
    		paHalfOrderListVO.setRefundFromEmTel(paHalfOrderListVO.getToEmTel());
    		paHalfOrderListVO.setRefundFromNm(paHalfOrderListVO.getToNm());
    		
            //중복체크
            executedRtn = paHalfOrderDAO.checkPaHalfExchageCancel(paHalfOrderListVO);
            if(executedRtn > 0) return "";
            
            executedRtn = paHalfOrderDAO.insertPaHalfExchangeCancel(paHalfOrderListVO); 
            if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAHALFEXCHANGECANCEL INSERT" });
            //return "";
        }
		
        //직매입상품 출하지시상태일 경우 반품 생성
        int doFlag = Integer.parseInt(ComUtil.NVL(paHalfOrderDAO.selectDoflag(paHalfOrderListVO),"0")) ;
        String delyType = paHalfOrderDAO.selectDelyType(paHalfOrderListVO.getPrdNo());
        
        if((doFlag >= 30) && "10".equals(delyType)) {
        	paHalfOrderListVO.setPaOrderGb		("30");
    		paHalfOrderListVO.setOrderGb		("30");
    		paHalfOrderListVO.setBpPickupTypeNm("업체직접수거");
    		
    		//ClaimRequestDate 및 ClaimNo Setting
    		paHalfOrderListVO.setClaimRequestDate	(paHalfOrderListVO.getRefundDate() );
    		paHalfOrderListVO.setClaimNo			(paHalfOrderListVO.getRefundNo());
    		
    		//주문배송지정보로 setting
    		paHalfOrderListVO.setRefundFromAddr(paHalfOrderListVO.getToAddr());
    		paHalfOrderListVO.setRefundFromZiCd(paHalfOrderListVO.getToZiCd());
    		paHalfOrderListVO.setRefundFromEmTel(paHalfOrderListVO.getToEmTel());
    		paHalfOrderListVO.setRefundFromNm(paHalfOrderListVO.getToNm());
        }
        
		//=취소확인
		int exists	=  paHalfOrderDAO.selectCountClaimList(paHalfOrderListVO);
		if(exists > 0) return "";
		
		//=원주문이 없는 취소건 확인
		exists	=  paHalfOrderDAO.selectCountOrderList(paHalfOrderListVO);
		if(exists < 1) return "";
		
		//=INSERT TPAHALFORDERLIST
		executedRtn = paHalfOrderDAO.insertPaHalfOrderList(paHalfOrderListVO); 
		if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAHALFORDERLIST INSERT" });
		
		//=INSERT TPAORDERM 
		insertPaOrderm(paHalfOrderListVO, new Paorderm());
		
		return rtnMsg;
	}


	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paHalfOrderDAO.selectClaimTargetList(paramMap);
	}


	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paHalfOrderDAO.selectCancelInputTargetDtList(paramMap);
	}


	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception {
		return paHalfOrderDAO.selectPaMobileOrderAutoCancelList();
	}

	 
}
