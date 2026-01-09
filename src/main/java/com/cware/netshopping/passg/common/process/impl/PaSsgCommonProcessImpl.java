package com.cware.netshopping.passg.common.process.impl;

import java.util.Date;
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
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaGoodsLimitChar;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaOrigin;
import com.cware.netshopping.domain.model.PaSsgDisplayCategory;
import com.cware.netshopping.domain.model.PaSsgDisplayRecommendCategory;
import com.cware.netshopping.domain.model.PaSsgGoodsCert;
import com.cware.netshopping.domain.model.PaSsgSettlement;
import com.cware.netshopping.passg.common.process.PaSsgCommonProcess;
import com.cware.netshopping.passg.common.repository.PaSsgCommonDAO;

@Service("passg.common.paSsgCommonProcess")
public class PaSsgCommonProcessImpl extends AbstractService implements PaSsgCommonProcess{
	
	@Resource(name = "passg.common.paSsgCommonDAO")
	PaSsgCommonDAO paSsgCommonDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;

	@Override
	public List<HashMap<String, Object>> selectEntpSlipCost(ParamMap apiInfoMap) throws Exception {
		return paSsgCommonDAO.selectEntpSlipCost(apiInfoMap);
	}

	@Override
	public String savePaSsgShipCost(ParamMap shipCostMap) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;	
		int executedRtn = 0;
		
		try{
			executedRtn = paSsgCommonDAO.updatePaSsgShipCost(shipCostMap);
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPASSGSHIPCOST UPDATE" });
			}
		}catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		
		return rtnMsg;
	}
	
	@Override
	public void savePaOfferCodeList(List<PaOfferCode> paOfferCodeList) throws Exception {
    	int executedRtn = 0;
    	
    	for(PaOfferCode paOfferCode:paOfferCodeList){
	    	executedRtn = paSsgCommonDAO.insertPaOfferCodeList(paOfferCode);
	    	if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPAOFFERCODE INSERT" });
    	}
	}
	
	@Override
	public void savePaBrand(List<PaBrand> paBrandList) throws Exception {
		int executedRtn = 0;

		for(PaBrand paBrand:paBrandList) {
			executedRtn = paSsgCommonDAO.insertPaBrand(paBrand);
			if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPABRAND INSERT" });
		}		
	}
	
	@Override
	public void savePaOrigin(List<PaOrigin> paOriginList) throws Exception {
		int executedRtn = 0;

		for(PaOrigin paOrigin:paOriginList) {
			executedRtn = paSsgCommonDAO.insertPaOrigin(paOrigin);
			if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPAORIGIN INSERT" });
		}		
	}
	
	@Override
	public void savePaGoodsLimitChar(List<PaGoodsLimitChar> paGoodsLimitCharList) throws Exception {
		int executedRtn = 0;

		for(PaGoodsLimitChar paGoodsLimitChar:paGoodsLimitCharList) {
			executedRtn = paSsgCommonDAO.insertPaGoodsLimitChar(paGoodsLimitChar);
			if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPAGOODSLIMITCHAR INSERT" });
		}		
	}
	
	@Override
	public void savePaGoodsKinds(List<PaGoodsKinds> paGoodsKindsList) throws Exception {
    	int executedRtn = 0;
    	
		executedRtn = paSsgCommonDAO.deletePaGoodsKindsMomentList(paGoodsKindsList.get(0));
		if(executedRtn < 0){
			log.info("tpagoodskindsmoment delete fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDSMOMENT SSG DELETE" });
		}
		
		for(PaGoodsKinds paGoodsKinds:paGoodsKindsList){
			executedRtn = paSsgCommonDAO.insertPaGoodsKindMomentsList(paGoodsKinds);
			
			if (executedRtn < 0) {
				log.info("tpagoodskindsmoment insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDSMOMENT SSG INSERT" });
			}
		}
		
		if(executedRtn > 0){
    		executedRtn = paSsgCommonDAO.insertPaGoodsKindsList(paGoodsKindsList.get(0));
    		if (executedRtn < 0) {
				log.info("tpagoodskinds insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS SSG INSERT" });
			}
		}
	}
	
	//@Override
	//public List<HashMap<String, Object>> selectSsgStandardCategoryList() throws Exception {
	//	return paSsgCommonDAO.selectSsgStandardCategoryList();
	//}
	
	@Override
	public void savePaSsgDisplayCategory(List<PaSsgDisplayCategory> paSsgDisplayCategoryList) throws Exception {
		int executedRtn = 0;

		for(PaSsgDisplayCategory paSsgDisplayCategory:paSsgDisplayCategoryList) {
			executedRtn = paSsgCommonDAO.insertPaSsgDisplayCategory(paSsgDisplayCategory);
			if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPASSGDISPLAYCATEGORY INSERT" });
		}		
	}

	@Override
	public Map<String, Object> selectSlipInsertList(PaEntpSlip paEntpSlip) throws Exception {
		return paSsgCommonDAO.selectSlipInsertList(paEntpSlip);
	}

	@Override
	public String savePaSsgEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			executedRtn = paSsgCommonDAO.insertPaSsgEntpSlip(paEntpSlip);
			
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
	public List<HashMap<String, Object>> selectEntpSlipUpdateList() throws Exception {
		return paSsgCommonDAO.selectEntpSlipUpdateList();
	}

	@Override
	public String updatePaSsgEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			executedRtn = paSsgCommonDAO.updatePaSsgEntpSlip(paEntpSlip);
			
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
	public List<PaGoodsKinds> selectGoodsKindsList() throws Exception {
		return paSsgCommonDAO.selectGoodsKindsList();
	}

	@Override
	public String savePaSsgDisplayRecommendCategory(
			List<PaSsgDisplayRecommendCategory> paSsgDisplayRecommendCategoryList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			for ( int k = 0; k < paSsgDisplayRecommendCategoryList.size(); k ++ ) {
				executedRtn = paSsgCommonDAO.savePaSsgDisplayRecommendCategory(paSsgDisplayRecommendCategoryList.get(k));
			
				if(executedRtn < 0){
					log.info("TPASSGRECOMMENDDISPLAY update fail");
					throw processException("msg.cannot_save", new String[] { "TPASSGRECOMMENDDISPLAY UPDATE" });
				}
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public List<PaGoodsKinds> selectSsgGoodsCertList() throws Exception {
		return paSsgCommonDAO.selectSsgGoodsCertList();
	}

	@Override
	public String savePaSsgGoodsCert(List<PaSsgGoodsCert> paSsgGoodsCertList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			for ( int k = 0; k < paSsgGoodsCertList.size(); k ++ ) {
				executedRtn = paSsgCommonDAO.savePaSsgGoodsCert(paSsgGoodsCertList.get(k));
			
				if(executedRtn < 0){
					log.info("TPASSGGOODSCERTIFICATION update fail");
					throw processException("msg.cannot_save", new String[] { "TPASSGGOODSCERTIFICATION INSERT" });
				}
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public String savePaSsgSettlement(List<PaSsgSettlement> paSsgSettlementList, ParamMap paramMap, int page) throws Exception {
		String rtnMsg 	= Constants.SAVE_SUCCESS;
		PaSsgSettlement paSsgSettlement = null;
		HashMap<String, Object> hashMap = null;
		String paSsgSettlementNo = "";
		int executedRtn = 0;
		int existsChk	= 0;
		Date critnDt  = DateUtil.toDate(paSsgSettlementList.get(0).getCritnDt(), DateUtil.GENERAL_DATE_FORMAT);	// 시간 포맷을 맞춰주기 위해서 
		
		String salesDateToString = DateUtil.toString(critnDt, DateUtil.GENERAL_DATE_FORMAT);
		paramMap.put("salesDateToString", salesDateToString);
		
		try {
			
			if(page < 2) {
				if("y".equals(paramMap.getString("delYn")) || "Y".equals(paramMap.getString("delYn"))) {
					executedRtn = paSsgCommonDAO.deletePaSsgSettlement(paramMap);
				}
			
				existsChk = paSsgCommonDAO.selectPaSsgSettlementExists(paramMap);
				
				if(existsChk > 0){
					return rtnMsg;
				}				
			}
			
			for(int i = 0 ; i < paSsgSettlementList.size(); i++) {
				paSsgSettlement = paSsgSettlementList.get(i);
								
				hashMap = new HashMap<String, Object>();
				hashMap.put("sequence_type", "PASSG_SETTLEMENT_NO");
				paSsgSettlementNo = (String)systemDAO.selectSequenceNo(hashMap);
				
				paSsgSettlement.setPaSsgSettlementNo(paSsgSettlementNo);
				
				executedRtn = paSsgCommonDAO.insertPaSsgSettlement(paSsgSettlementList.get(i));
				
				if(executedRtn < 0){
					log.info("TPASSGSETTLEMENT insert fail");
					throw processException("msg.cannot_save", new String[] { "TPASSGSETTLEMENT INSERT" });
				}
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

}
