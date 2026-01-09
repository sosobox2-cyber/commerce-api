package com.cware.netshopping.pakakao.common.process.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.model.Brand;
import com.cware.netshopping.domain.model.Makecomp;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaMaker;
import com.cware.netshopping.domain.model.PaOrigin;
import com.cware.netshopping.pakakao.common.process.PaKakaoCommonProcess;
import com.cware.netshopping.pakakao.common.repository.PaKakaoCommonDAO;

@Service("pakakao.common.paKakaoCommonProcess")
public class PaKakaoCommonProcessImpl extends AbstractService implements PaKakaoCommonProcess{
	
	@Resource(name = "pakakao.common.paKakaoCommonDAO")
	PaKakaoCommonDAO paKakaoCommonDAO;
	
	@Override
	public void savePaGoodsKinds(List<PaGoodsKinds> paGoodsKindsList) throws Exception {
    	int executedRtn = 0;
    	
		executedRtn = paKakaoCommonDAO.deletePaGoodsKindsMomentList(paGoodsKindsList.get(0));
		if(executedRtn < 0){
			log.info("tpagoodskindsmoment delete fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDSMOMENT DELETE" });
		}
		
		for(PaGoodsKinds paGoodsKinds:paGoodsKindsList){
			executedRtn = paKakaoCommonDAO.insertPaGoodsKindMomentsList(paGoodsKinds);
			
			if (executedRtn < 0) {
				log.info("tpagoodskindsmoment insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDSMOMENT INSERT" });
			}
		}
		
		if(executedRtn > 0){
    		executedRtn = paKakaoCommonDAO.insertPaGoodsKindsList(paGoodsKindsList.get(0));
    		if (executedRtn < 0) {
				log.info("tpagoodskinds insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS INSERT" });
			}
		}
	}
	
	@Override
	public void savePaOrigin(List<PaOrigin> paOriginList) throws Exception {
		int executedRtn = 0;

		for(PaOrigin paOrigin:paOriginList) {
			executedRtn = paKakaoCommonDAO.insertPaOrigin(paOrigin);
			if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPAORIGIN INSERT" });
		}		
	}
	
	@Override
	public List<Brand> selectBrandList() throws Exception {
		return paKakaoCommonDAO.selectBrandList();
	}
	
	@Override
	public void savePaBrand(List<PaBrand> paBrandList) throws Exception {
		int exists = 0;
		int executedRtn = 0;

		for(PaBrand paBrand:paBrandList) {
			//이미 등록된 브랜드인지 확인
			exists = paKakaoCommonDAO.countPaBrand(paBrand);
			if(exists > 0) continue;
			
			executedRtn = paKakaoCommonDAO.insertPaBrand(paBrand);
			if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPABRAND INSERT" });
		}		
	}
	
	@Override
	public List<Makecomp> selectMakerList() throws Exception {
		return paKakaoCommonDAO.selectMakerList();
	}
	
	@Override
	public void savePaMaker(List<PaMaker> paMakerList) throws Exception {
		int exists = 0;
		int executedRtn = 0;

		for(PaMaker paMaker:paMakerList) {
			//이미 등록된 메이커인지 확인
			exists = paKakaoCommonDAO.countPaMaker(paMaker);
			if(exists > 0) continue;
			
			executedRtn = paKakaoCommonDAO.insertPaMaker(paMaker);
			if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPAMAKER INSERT" });
		}		
	}
	
	@Override
	public List<PaEntpSlip> selectPaKakaoEntpSlip(ParamMap paramMap) throws Exception {
		return paKakaoCommonDAO.selectPaKakaoEntpSlip(paramMap);
	}
	
	@Override
	public String savePaEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			executedRtn = paKakaoCommonDAO.insertPaEntpSlip(paEntpSlip);
			
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
	public List<PaEntpSlip> selectPaKakaoEntpSlipUpdate(ParamMap paramMap) throws Exception {
		return paKakaoCommonDAO.selectPaKakaoEntpSlipUpdate(paramMap);
	}
	
	@Override
	public String updatePaEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			executedRtn = paKakaoCommonDAO.updatePaEntpSlip(paEntpSlip);
			
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
	public List<PaEntpSlip> selectPaKakaoModifyEntpSlip(ParamMap paramMap) throws Exception {
		return paKakaoCommonDAO.selectPaKakaoModifyEntpSlip(paramMap);
	}
}
