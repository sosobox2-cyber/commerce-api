package com.cware.netshopping.pacopn.exchange.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Pacopnexchangeitemlist;
import com.cware.netshopping.domain.model.Pacopnexchangelist;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pacopn.exchange.process.PaCopnExchangeProcess;
import com.cware.netshopping.pacopn.exchange.repository.PaCopnExchangeDAO;

@Service("pacopn.exchange.paCopnExchangeProcess")
public class PaCopnExchangeProcessImpl extends AbstractService implements PaCopnExchangeProcess {

	@Resource(name = "common.system.systemDAO")
	private SystemDAO systemDAO;

	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;

	@Resource(name = "pacopn.exchange.paCopnExchangeDAO")
	private PaCopnExchangeDAO paCopnExchangeDAO;

	public String saveExchangeList(List<Pacopnexchangelist> exchangeListArr) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		String sysdate = DateUtil.getCurrentDateTimeAsString();

		Paorderm paorderm = null;
		HashMap<String, Object> goodsMap = null;
		int existsCnt = 0, executedRtn = 0;

		for(Pacopnexchangelist exchangelist: exchangeListArr){
			// 데이터 중복 Check & Skip
			existsCnt = paCopnExchangeDAO.selectPaCopnClaimListExists(exchangelist);
			if(existsCnt > 0) continue;
			

			exchangelist.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			exchangelist.setModifyDate(exchangelist.getInsertDate());

			executedRtn = paCopnExchangeDAO.insertPacopnexchangelist(exchangelist);
			if(executedRtn != 1){
				throw processException("msg.cannot_save", new String[] { "TPACOPNEXCHANGELIST INSERT" });
			}

			for(Pacopnexchangeitemlist pacopnexchangeitemlist : exchangelist.getExchangeItemDtoV1s()){
				pacopnexchangeitemlist.setInsertDate(exchangelist.getInsertDate());
				executedRtn = paCopnExchangeDAO.insertPacopnexchangeitemlist(pacopnexchangeitemlist);
				if (executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPACOPNEXCHANGELIST INSERT" });
				}
				// 교환 상품 정보
				goodsMap = paCopnExchangeDAO.selectExchangeGoodsdt(pacopnexchangeitemlist);
				
				for(int j = 0; j < 2; j++){
					paorderm = new Paorderm();
					paorderm.setBlank();
					paorderm.setPaCode(goodsMap.get("PA_CODE").toString());
					paorderm.setPaOrderGb((j==0) ? exchangelist.getPaOrderGb() : ("40".equals(exchangelist.getPaOrderGb()) ? "45" : "46"));
					paorderm.setPaOrderNo(exchangelist.getOrderId());
					paorderm.setPaOrderSeq(pacopnexchangeitemlist.getItemSeq());
					paorderm.setPaClaimNo(exchangelist.getExchangeId());
					paorderm.setPaProcQty(new Long((((Pacopnexchangelist)exchangelist).getExchangeItemDtoV1s())[0].getQuantity()).toString());
					paorderm.setPaDoFlag("20");
					paorderm.setOutBefClaimGb("0");
					paorderm.setChangeFlag("01");
					paorderm.setChangeGoodsCode(goodsMap.get("GOODS_CODE").toString());
					paorderm.setChangeGoodsdtCode(goodsMap.get("GOODSDT_CODE").toString());
					paorderm.setInsertDate(exchangelist.getInsertDate());
					paorderm.setModifyDate(exchangelist.getModifyDate());
					paorderm.setModifyId(Constants.PA_COPN_PROC_ID);
					paorderm.setPaGroupCode("05");
					
					executedRtn = paCommonDAO.insertPaOrderM(paorderm);
					if (executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
					}
				}
			}

		}
		return rtnMsg;
	}

	public List<Object> selectOrderChangeTargetList() throws Exception{
		return paCopnExchangeDAO.selectOrderChangeTargetList();
	}

	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paCopnExchangeDAO.selectOrderChangeTargetDtList(paramMap);
	}

	public List<Object> selectChangeCancelTargetList() throws Exception {
		return paCopnExchangeDAO.selectChangeCancelTargetList();
	}

	public List<Object> selectOrderChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paCopnExchangeDAO.selectOrderChangeCancelTargetDtList(paramMap);
	}

	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception {
		return paCopnExchangeDAO.updatePreCancelYn(preCancelMap);
	}

	public String saveExchangeRejectProc(ParamMap paramMap) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {

			executedRtn = paCopnExchangeDAO.updatePaOrdermPreCancel(paramMap); //= tpaorderm update

			if(executedRtn < 1){
				log.error("[saveExchangeRefusal] tpaorderm update fail");
				log.error("ordNo : " + paramMap.getString("ordNo") + ", ordPrdSeq : " + paramMap.getString("ordPrdSeq") + ", clmReqSeq : " + paramMap.getString("clmReqSeq"));
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}

		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	public List<Map<String,Object>> selectExchangeReturn(String paCode) throws Exception {
		return paCopnExchangeDAO.selectExchangeReturn(paCode);
	}

	public int updateExchangeReturnConfirm(ParamMap paramMap) throws Exception{
		return paCopnExchangeDAO.updateExchangeReturnConfirm(paramMap);
	}

	public List<Map<String,Object>> selectExchangeSlipOutTargetList() throws Exception {
		return paCopnExchangeDAO.selectExchangeSlipOutTargetList();
	}

	public int updateExchangeCompleteResult(ParamMap paramMap) throws Exception {
		return paCopnExchangeDAO.updateExchangeCompleteResult(paramMap);
	}
	
	public String updateExchangeDeliveryComplete(Paorderm paOrderm) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try{
			executedRtn = paCopnExchangeDAO.updateExchangeDeliveryComplete(paOrderm);
			if(executedRtn < 1){
				log.error("");
				throw processException("msg.cannot_save", new String[]{ "TPAORDERM UPDATE" });
			}
		}catch(Exception e){
			rtnMsg = e.getMessage();
		}
		
		return rtnMsg;
	}
	
	@Override
	public List<HashMap<String, Object>> selectExchangeCompleteList() throws Exception{
		return paCopnExchangeDAO.selectExchangeCompleteList();
	}
	
	@Override
	public HashMap<String, String> selectOrgShipmentBoxId(ParamMap paramMap) throws Exception{
		return paCopnExchangeDAO.selectOrgShipmentBoxId(paramMap);
	}
	
	public HashMap<String, String> selectExchangeDetail(String paClaimNo) throws Exception{

	    HashMap<String, String> rtnAMap = paCopnExchangeDAO.selectExchangeDetail(paClaimNo);
	    
	    return rtnAMap;
	}

	@Override
	public List<String> selectExchangeCreatedDate() throws Exception {
		return paCopnExchangeDAO.selectExchangeCreatedDate();
	}
}
