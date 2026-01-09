package com.cware.netshopping.paintp.order.process.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaIntpTargetVO;
import com.cware.netshopping.domain.model.PaIntpCancellist;
import com.cware.netshopping.domain.model.PaIntpCancellistKey;
import com.cware.netshopping.domain.model.PaIntpOrderlist;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.domain.paintp.xml.PaIntpCancelReqVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpOrderProductVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpOrderVO;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.paintp.order.process.PaIntpOrderProcess;
import com.cware.netshopping.paintp.order.repository.PaIntpOrderDAO;
import com.cware.netshopping.paintp.util.PaIntpComUtil;

@Component("paintp.order.paIntpOrderProcess")
public class PaIntpOrderProcessImpl extends AbstractProcess implements PaIntpOrderProcess{

	@Autowired
	private PaIntpOrderDAO paIntpOrderDAO;
	
	@Autowired
	private PaCommonDAO paCommonDAO;

	@Autowired
    private SystemService systemService;
	
	@Autowired
	private PaIntpComUtil paIntpComUtil;

	@Override
	public String saveOrderConfirmList(PaIntpOrderVO orderList, String paCode) throws Exception {
		String rtnMsg= Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			String dateTime = systemService.getSysdatetimeToString();
			Timestamp systemTime = DateUtil.toTimestamp(dateTime);
			if (orderList != null) {
				int prdCount = orderList.getPrdList().size();
				for (int prdIndex = 0; prdIndex < prdCount; prdIndex++ ) {
					PaIntpOrderlist orderlist = paIntpComUtil.convert(orderList, prdIndex);
					orderlist.setPaOrderGb("10");	//10, 주문
					orderlist.setWithdrawYn("0");
					orderlist.setInsertDate(systemTime);
					orderlist.setModifyDate(systemTime);
					
					//1. 발주상태가 아닌 주문상품 SKIP
					if(!"50".equals(orderlist.getCurrentState())) {//50:발주확인, 70:배송중, 75:배송완료, 80:구매확정
						continue;
					}
					//2. 기처리 여부
					int exists = paIntpOrderDAO.countOrderList(orderlist.getOrdNo(), orderlist.getOrdSeq(), "0", "10");
					if (exists > 0) {
						continue;
					}
					
					//3. TPAINTPORDERLIST Insert or Update
					executedRtn = paIntpOrderDAO.insertPaIntpOrderlist(orderlist);
					if (executedRtn != 1) {
						throw processException("msg.cannot_save", new String[] { "TPAINTPORDERLIST INSERT" });
					}
					
					Paorderm paorderm = new Paorderm();
					paorderm.setPaOrderGb("10");
					paorderm.setPaCode(paCode);
					paorderm.setPaOrderNo(orderlist.getOrdNo());
					paorderm.setPaOrderSeq(ComUtil.objToStr(orderlist.getOrdSeq(), null));
					paorderm.setPaShipNo(ComUtil.objToStr(orderlist.getDelvsetlSeq(), null));
					paorderm.setPaClaimNo(null);
					paorderm.setPaProcQty(ComUtil.objToStr(orderlist.getOrdQty(), null));
					paorderm.setPaDoFlag("30");
					paorderm.setOutBefClaimGb("0");
					paorderm.setInsertDate(orderlist.getInsertDate());
					paorderm.setModifyDate(orderlist.getModifyDate());
					paorderm.setModifyId(Constants.PA_INTP_PROC_ID);
					paorderm.setPaGroupCode(Constants.PA_INTP_GROUP_CODE);
					
					executedRtn = paCommonDAO.insertPaOrderM(paorderm);
					if (executedRtn < 0) {
						throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
					}
				}
			}
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
		return rtnMsg;
	}
	
	@Override
	public List<PaIntpTargetVO> selectOrderInputTargetList(int limitCount) throws Exception {
		return paIntpOrderDAO.selectOrderInputTargetList(limitCount);
	}
	
	@Override
	public List<Object> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return paIntpOrderDAO.selectOrderInputTargetDtList(paramMap);
	}
	
	@Override
	public Map<String, String> selectRefusalInfo(String mappingSeq) throws Exception {
		return paIntpOrderDAO.getPaOrderKeyByMappingSeq(mappingSeq);
	}
	
	@Override
	public String saveOrderRejectProcTx(String ordNo, Integer ordSeq, boolean canceled, String apiResultCode, String apiResultMessage, String preCancelReason, String clmReqSeq, String paCode) throws Exception {
		String resultCode = Constants.SAVE_SUCCESS;
		
		try {
			int result = paIntpOrderDAO.updatePaOrdermPreCancel(ordNo, ordSeq, canceled, apiResultCode, apiResultMessage, preCancelReason, clmReqSeq, paCode);
			if ( result == 1) {
				log.info("TPAORDERM update success, ordNo:" + ordNo + ", ordSeq:" + ordSeq + ", paCode:" + paCode);
			} else {
				log.info("TPAORDERM update fail, ordNo:" + ordNo + ", ordSeq:" + ordSeq + ", paCode:" + paCode);
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
		}
		catch ( Exception e ) {
			log.error("TPAORDERM update fail, ordNo:" + ordNo + ", ordSeq:" + ordSeq + ", paCode:" + paCode + ", exception:" + e.getMessage());
			resultCode = e.getMessage();
		}
		
		return resultCode;
	}
	
	@Override
	public void saveCancelRequestOrWithdrawList(PaIntpCancelReqVO cancelReqVO) throws Exception {
		if (cancelReqVO == null || cancelReqVO.getProductList() == null || cancelReqVO.getProductList().isEmpty()) {
			return;
		}
		int prdCount = cancelReqVO.getProductList().size();
		for (int prdIndex = 0; prdIndex < prdCount; prdIndex++ ) {
			PaIntpOrderlist orderlist = paIntpComUtil.convert(cancelReqVO, prdIndex);
			String ordNo     = orderlist.getOrdNo();
			String ordSeq    = orderlist.getOrdSeq();
			String clmreqSeq = orderlist.getClmreqSeq();
			
			orderlist.setPaOrderGb("20"); //20, 취소
			orderlist.setProcFlag("00");//접수

			//원주문의 정보를 조회하여 필요한 정보를 세팅
			PaIntpOrderProductVO prdVO = paIntpOrderDAO.getOrderProductInfo(orderlist.getOrdNo(), orderlist.getOrdSeq());
			if (prdVO == null) {
				//원주문이 생성되기 전...
				continue;
			}
			orderlist.setPrdNo(prdVO.getPrdNo());
			orderlist.setOptNo(prdVO.getOptNo());
			orderlist.setDelvsetlSeq(prdVO.getDelvsetlSeq());
			
			if ("1".equals(orderlist.getClmreqStat())) {// CLMREQ_STAT 1 : 취소요청
				orderlist.setWithdrawYn("0");
				//주문취소요청 내역 처리 건수 여부 조회
				int existsCount = paIntpOrderDAO.countOrderList(ordNo, ordSeq, clmreqSeq, "20");
				if (existsCount >= 1) {
					continue;
				}

				int insertResult = paIntpOrderDAO.insertCancelReqPaIntpOrderlist(orderlist);
				if (insertResult != 1) {
					throw processException("msg.cannot_save", new String[] { "TPAINTPORDERLIST INSERT" });
				}
			} else if ("2".equals(orderlist.getClmreqStat())) {// CLMREQ_STAT 2 : 취소요청 철회
				orderlist.setWithdrawYn("1");

				String withdrawYn = paIntpOrderDAO.getOrderCancelWithdrawYn(ordNo, ordSeq, clmreqSeq);
				//주문취소내역이 없거나 이미 철회가 되었다면 skip
				if (!"0".equals(withdrawYn)) {
					continue;
				}
				
				//취소요청 철회내역 저장
				int updateResult = paIntpOrderDAO.updateWithdrawYn(ordNo, ordSeq, clmreqSeq);
				if (updateResult < 1) {
					throw processException("msg.cannot_save", new String[] { "TPAINTPORDERLIST WITHDRAW_YN UPDATE" });
				}
			}
		}
	}
	
	@Override
	public List<PaIntpCancellist> selectPaIntpOrderCancelList() throws Exception {
		return paIntpOrderDAO.selectPaIntpOrderCancelList();
	}

	@Override
	public String saveCancelConfirmProc(PaIntpCancellist cancel, String claimGb) throws Exception {
		String procFlag = "10";
		String ordNo = cancel.getPaOrderNo();
		String ordSeq = cancel.getOrdSeq();
		String clmreqSeq = cancel.getClmreqSeq();
		String paOrderGb = cancel.getPaOrderGb();
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		String resultCode = Constants.SAVE_SUCCESS;
		
		try {
			Paorderm paorderm = new Paorderm();
			paorderm.setPaCode(cancel.getPaCode());
			paorderm.setPaOrderGb(paOrderGb);
			paorderm.setPaOrderNo(ordNo);
			paorderm.setPaOrderSeq(ComUtil.objToStr(ordSeq, null));
			paorderm.setPaShipNo(ComUtil.objToStr(cancel.getDelvsetlSeq(), null));
			paorderm.setPaClaimNo(clmreqSeq);
			paorderm.setPaProcQty(ComUtil.objToStr(cancel.getClmreqQty(), null));
			paorderm.setPaDoFlag("20");
			paorderm.setOutBefClaimGb(claimGb);
			paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setModifyId(Constants.PA_INTP_PROC_ID);
			paorderm.setPaGroupCode(Constants.PA_INTP_GROUP_CODE);
			
			int result = paCommonDAO.insertPaOrderM(paorderm);
			if ( result != 1) {
				resultCode = Constants.SAVE_FAIL;
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}

			if("0".equals(claimGb)) { 
				result = paIntpOrderDAO.updatePaIntpOrderListProcFlag(ordNo, ordSeq, clmreqSeq, paOrderGb, procFlag);
				if (result != 1) {
					resultCode = Constants.SAVE_FAIL;
					throw processException("msg.cannot_save", new String[] { "TPAINTPORDERLIST UPDATE" });
				}
			}
		} catch(Exception e) {
			throw processException("msg.cannot_save", new String[] { "TPAINTPORDERLIST UPDATE" });
		}
		return resultCode;
	}
	
	@Override
	public String updatePaOrdermCancelRefusal(String ordNo, String ordSeq, String clmreqSeq, String apiResultCode, String apiResultMessage)
			throws Exception {
		String resultCode = Constants.SAVE_SUCCESS;
		String procFlag = "20";
		try {
			int result = paIntpOrderDAO.updatePaIntpOrderListProcFlag(ordNo, ordSeq, clmreqSeq, "20", procFlag);
			if (result != 1) {
				resultCode = Constants.SAVE_FAIL;
				throw processException("msg.cannot_save", new String[] { "TPAINTPORDERLIST UPDATE" });
			}
		} catch(Exception e) {
			throw processException("msg.cannot_save", new String[] { "TPAINTPORDERLIST UPDATE" });
		}
		return  resultCode;
	}
	
	@Override
	public int updatePreCancelYn(String mappingSeq, String preCancelYn, String preCancelReason) throws Exception {
		return paIntpOrderDAO.updatePreCancelYn(mappingSeq, preCancelYn, preCancelReason);
	}
	
	@Override
	public List<PaIntpTargetVO> selectCancelInputTargetList() throws Exception {
		return paIntpOrderDAO.selectCancelInputTargetList();
	}
	
	@Override
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paIntpOrderDAO.selectCancelInputTargetDtList(paramMap);
	}
	
	@Override
	public PaIntpCancellistKey selectPaIntpCancellistApproval(String ordNo, Integer ordSeq, Integer clmreqSeq, String paCode)
			throws Exception {
		return paIntpOrderDAO.selectPaIntpCancellistApproval(ordNo, ordSeq, clmreqSeq, paCode);
	}
	
	@Override
	public void saveDeliveryComplete(PaIntpOrderVO complete) throws Exception {
		String ordNo  = complete.getOrdNo();
		String ordSeq = "";
		String paDoFlag = "80";
		
		try {
			for(PaIntpOrderProductVO prod : complete.getPrdList()) {
				ordSeq = prod.getOrdSeq();
				
				int targetCnt = paIntpOrderDAO.countDeliComList(ordNo, ordSeq);
				if(targetCnt > 0) { //배송완료처리(PA_DO_FLAG 80) 안된 목록이 있다면
					int result = paIntpOrderDAO.updatePaOrdermResult(ordNo, ordSeq, Constants.SAVE_SUCCESS, "배송완료처리 성공", paDoFlag);
					if(result == 1) {
						log.info("TPAORDERM update success, ordNo:" + ordNo + ", ordSeq:" + ordSeq);
					} else {
						log.info("TPAORDERM update fail, ordNo:" + ordNo + ", ordSeq:" + ordSeq);
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
					}
				}
			}
		} catch(Exception e) {
			log.error("TPAORDERM update fail, ordNo:" + ordNo + ", ordSeq:" + ordSeq + ", exception:" + e.getMessage());
			throw e;
		}
	}
	
	@Override
	public List<Map<String,String>> selectSlipOutProcList(ParamMap paramMap) throws Exception{	    
	    return paIntpOrderDAO.selectSlipOutProcList(paramMap); 
	}
	
	@Override
	public int updateSlipOutProc(Map<?,?> slipOut) throws Exception{	    
	    return paIntpOrderDAO.updateSlipOutProc(slipOut); 
	}
	
	@Override
	public int updateSlipOutProcFail(Map<?,?> slipOutFail) throws Exception{
	    return paIntpOrderDAO.updateSlipOutProcFail(slipOutFail); 
	    
	}

	@Override
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception {
		return paIntpOrderDAO.updatePreCancelYn(preCancelMap);
	}

	@Override
	public PaIntpCancellist selectOrgItemInfoByCancelInfo(String ordNo, String ordSeq, String clmreqSeq) throws Exception {
		return paIntpOrderDAO.selectOrgItemInfoByCancelInfo(ordNo, ordSeq, clmreqSeq);
	}

	@Override
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception {
		return paIntpOrderDAO.selectOrderPromo(orderMap);
	}

	@Override
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception {
		return paIntpOrderDAO.selectOrderPaPromo(orderMap);
	}
}