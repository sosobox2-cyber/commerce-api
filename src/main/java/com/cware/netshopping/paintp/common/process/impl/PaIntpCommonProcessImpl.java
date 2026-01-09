package com.cware.netshopping.paintp.common.process.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaIntpDelvSettlement;
import com.cware.netshopping.domain.model.PaIntpSettlement;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.paintp.common.process.PaIntpCommonProcess;
import com.cware.netshopping.paintp.common.repository.PaIntpCommonDAO;

@Service("paintp.common.paIntpCommonProcess")
public class PaIntpCommonProcessImpl extends AbstractService implements PaIntpCommonProcess{

	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "paintp.common.paIntpCommonDAO")
	private PaIntpCommonDAO paIntpCommonDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Override
	public String savePaIntpGoodsKinds(List<PaGoodsKinds> paGoodsKindsList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			executedRtn = paCommonDAO.deletePaGoodsKindsListMoment(paGoodsKindsList.get(0).getPaGroupCode());
			
			if(executedRtn < 0){
				log.info("tPaIntplmsdlist delete fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS DELETE" });
			}
			
			for(PaGoodsKinds paGoodsKinds:paGoodsKindsList){
				executedRtn += paCommonDAO.insertPaGoodsKindsListMoment(paGoodsKinds); //= tpagoodskinds insert
			}
			if(executedRtn > 0){
				log.info("tPaIntplmsdlist insert success");
				//= tpagoodskinds insert
				executedRtn = paCommonDAO.insertPaGoodsKindsList(paGoodsKindsList.get(0)); 
				if (executedRtn < 0) {
					log.info("tPaIntplmsdlist insert fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS INSERT" });
				}
					
				//= tpagoodskinds update
				executedRtn = paCommonDAO.updatePaGoodsKindsList(paGoodsKindsList.get(0)); 
				if (executedRtn < 0) {
					log.info("tPaIntplmsdlist update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS UPDATE" });
				}
					
				//= tpagoodskinds use_yn update
				executedRtn = paCommonDAO.updatePaGoodsKindsListUseYn(paGoodsKindsList.get(0)); 
				if (executedRtn < 0) {
					log.info("tPaIntplmsdlist update use_yn fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS UPDATE" });
				}
				
				//사용하지 않는 카테고리 use_yn = 0 처리.
//				executedRtn = paIntpCommonDAO.updatePaGoodsKindsUnUseListUseYn(paGoodsKindsList.get(0)); 
//				if (executedRtn < 0) {
//					log.info("tPaIntplmsdlist update use_yn fail");
//					throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS UPDATE" });
//				}
					
			} else {
				log.info("tPaIntplmsdlistmoment insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDSMOMENT INSERT" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public String savePaIntpBrand(List<PaBrand> paBrandList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		PaBrand paBrand = null;

		try {
			for(int i = 0; i < paBrandList.size(); i++) {
				paBrand = paBrandList.get(i);
				executedRtn = paIntpCommonDAO.insertPaIntpBrand(paBrand);
				if(executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TPAINTPBRAND INSERT" });
				}
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}

		return rtnMsg;
	}

	@Override
	public HashMap<String, String> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception {
		return paIntpCommonDAO.selectEntpShipInsertList(paEntpSlip);
	}

	@Override
	public String savePaIntpEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			executedRtn = paIntpCommonDAO.insertPaIntpEntpSlip(paEntpSlip);
			
			if(executedRtn < 0){
				log.info("TPAENTPSLIP insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAENTPSLIP INSERT" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public List<Object> selectEntpShipUpdateList(String paAddrGb) throws Exception {
		return paIntpCommonDAO.selectEntpShipUpdateList(paAddrGb);
	}

	@Override
	public String savePaIntpEntpSlipUpdate(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			executedRtn = paIntpCommonDAO.updatePaIntpEntpSlip(paEntpSlip);
			
			if(executedRtn < 0){
				log.info("TPAENTPSLIP update fail");
				throw processException("msg.cannot_save", new String[] { "TPAENTPSLIP UPDATE" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public List<HashMap<?,?>> selectEntpSlipCost(ParamMap paramMap) throws Exception {
		return paIntpCommonDAO.selectEntpSlipCost(paramMap);
	}
	
	/**
	 * 인터파크 배송비 정책 등록 - 배송비 저장
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	@Override
	public String savePaIntpCustShipCost(ParamMap paramMap)  throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try{
			executedRtn = paIntpCommonDAO.updatePaIntpCustShipCost(paramMap);
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPACUSTSHIPCOST UPDATE" });
			}
		}catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public String savePaIntpSettlement(List<PaIntpSettlement> paIntpSettlementList, ParamMap paramMap) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		PaIntpSettlement paIntpSettlement = null;
		HashMap<String, Object> hashMap = null;
		String paIntpSettlementNo = "";
		int existsChk = 0;
		
		try {
			
			if("y".equals(paramMap.getString("delYn")) || "Y".equals(paramMap.getString("delYn"))) {
				executedRtn = paIntpCommonDAO.deletePaIntpSettlement(paramMap);
			}
			
			for(int i = 0 ; i < paIntpSettlementList.size(); i ++) {
				paIntpSettlement = paIntpSettlementList.get(i);
				
				existsChk = paIntpCommonDAO.selectPaIntpSettlementExists(paIntpSettlement);
				
				if(existsChk > 0){
					continue;
				}
				
				hashMap = new HashMap<String, Object>();
				hashMap.put("sequence_type", "PAINTP_SETTLEMENT_NO");
				paIntpSettlementNo = (String)systemDAO.selectSequenceNo(hashMap);
				
				paIntpSettlement.setPaIntpSettlementNo(paIntpSettlementNo);
				
				executedRtn = paIntpCommonDAO.insertPaIntpSettlement(paIntpSettlement);
				if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPAINTPSETTLEMNET INSERT" });
			}
		}catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
		return rtnMsg;
	}

	@Override
	public String savePaIntpDelvSettlement(List<PaIntpDelvSettlement> paIntpDelvSettlements, ParamMap paramMap)
			throws Exception {
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		PaIntpDelvSettlement paIntpDelvSettlement = null;
		HashMap<String, Object> hashMap = null;
		String paIntpDelvSettlementNo = "";
		int existsChk = 0;
		
		try {
			if("y".equals(paramMap.getString("delYn")) || "Y".equals(paramMap.getString("delYn"))) {
				executedRtn = paIntpCommonDAO.deletePaIntpDelvSettlement(paramMap);
			}
			
			for(int i = 0 ; i < paIntpDelvSettlements.size(); i++) {
				paIntpDelvSettlement = paIntpDelvSettlements.get(i);
				
				existsChk = paIntpCommonDAO.selectPaIntpDelvSettlementExists(paIntpDelvSettlement);
				
				if(existsChk > 0){
					continue;
				}
				hashMap = new HashMap<String, Object>();
				hashMap.put("sequence_type", "DELVSETTLE_NO");
				paIntpDelvSettlementNo = (String)systemDAO.selectSequenceNo(hashMap);
				
				paIntpDelvSettlement.setDelvSettleNo(paIntpDelvSettlementNo);
				
				executedRtn = paIntpCommonDAO.insertPaIntpDelvSettlement(paIntpDelvSettlement);
				if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPAINTPDELVSETTLEMNET INSERT" });
			}
		}catch (Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
		return rtnMsg;
	}
}
