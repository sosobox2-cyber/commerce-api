package com.cware.netshopping.panaver.common.process.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.PanaverentpaddressmomentVO;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaNaverOrigin;
import com.cware.netshopping.domain.model.Panaverentpaddressmoment;
import com.cware.netshopping.domain.model.Panavergoodskinds;
import com.cware.netshopping.domain.model.Panaverkindscerti;
import com.cware.netshopping.domain.model.Panaverkindscertikinds;
import com.cware.netshopping.domain.model.Panaverkindsexcept;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.panaver.common.process.PaNaverCommonProcess;
import com.cware.netshopping.panaver.common.repository.PaNaverCommonDAO;

@Service("panaver.common.paNaverCommonProcess")
public class PaNaverCommonProcessImpl extends AbstractService implements PaNaverCommonProcess{
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;

	@Resource(name = "panaver.common.paNaverCommonDAO")
	private PaNaverCommonDAO paNaverCommonDAO;

    @Resource(name = "common.system.systemService")
    private SystemService systemService;    
    
    @Override
	public String savePaNaverOriginList(List<PaNaverOrigin> paNaverOriginList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	
    	executedRtn = paNaverCommonDAO.deletePaNaverOriginList();
    	
    	for(PaNaverOrigin paNaverOrigin:paNaverOriginList){
	    	executedRtn = paNaverCommonDAO.insertPaNaverOriginList(paNaverOrigin);
	    	if(executedRtn < 0){
				log.error("tpanavertorigin insert fail");
				throw processException("msg.cannot_save", new String[] { "TPANAVERORIGIN INSERT" });
			}
    	}
    	
    	
		return rtnMsg;
	}
    
    @Override
	public String saveMappingPaNaverOrigin(List<PaNaverOrigin> paNaverOriginList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		PaNaverOrigin paNaverOrigin = null;
		if(paNaverOriginList.size() > 0){
			executedRtn = paNaverCommonDAO.deletePaNaverOriginList();
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPANAVERORIGIN DELETE" });
			}
			for(int i = 0; i < paNaverOriginList.size(); i++){
				paNaverOrigin = paNaverOriginList.get(i);
				executedRtn = paNaverCommonDAO.insertPaNaverOriginList(paNaverOrigin); 
				if (executedRtn < 1) {		
					throw processException("msg.cannot_save", new String[] { "TPANAVERORIGIN INSERT" });
				}					
			}
			executedRtn = paNaverCommonDAO.insertPaNaverOriginMapping(paNaverOriginList.get(0));
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPANAVERORIGINMAPPING INSERT" });
			}	
		}
		return rtnMsg;
	}
    
    //19.09.18 카테고리 작업
    
    @Override
	public String savePaNaverGoodsKinds(List<Panavergoodskinds> paNaverGoodsKindsList, List<PaGoodsKinds> paGoodsKindsList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;

			executedRtn = paNaverCommonDAO.deletePaNaverGoodsKindsList();
			if(executedRtn < 0){
				log.info("tpaNaverGoodsKinds delete fail");
				throw processException("msg.cannot_save", new String[] { "TPANAVERGOODSKINDS DELETE" });
			}
			
			executedRtn = paNaverCommonDAO.updatePaGoodsKindsUnUseListUseYn(paGoodsKindsList.get(0));
			if(executedRtn < 0){					
				log.info("tpagoodskinds update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS UPDATE" });
			}

			for(Panavergoodskinds paNaverGoodsKinds:paNaverGoodsKindsList){
				//	TPANAVERGOODSKINDS INSERT
				executedRtn = paNaverCommonDAO.insertPaNaverGoodsKindsList(paNaverGoodsKinds);
				if(executedRtn < 0){
					log.error("tpaNaverGoodsKinds insert fail");
					throw processException("msg.cannot_save", new String[] { "TPANAVERGOODSKINDS INSERT" });
				}
			}
			for(PaGoodsKinds paGoodsKinds:paGoodsKindsList){
				executedRtn = paNaverCommonDAO.mergePaGoodsKindsList(paGoodsKinds);
				if(executedRtn < 0){
					log.error("tpaGoodsKinds insert fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS INSERT" });
				}
			}
			
			
		return rtnMsg;		
	}

	@Override
	public String savePaNaverCategoryInfo(List<Panaverkindsexcept> paNaverKindsExceptList, List<Panavergoodskinds> paNaverGoodsKindsList,
											List<Panaverkindscerti> paNaverKindsCertiList, List<Panaverkindscertikinds> paNaverKindsCertiKindsList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
//    	executedRtn = paNaverCommonDAO.deletePaNaverCategoryInfo();
    	
    	if(executedRtn < 0){
			log.info("TPANAVERGOODSKINDSEXCEPT delete fail");
			throw processException("msg.cannot_save", new String[] { "TPANAVERGOODSKINDSEXCEPT DELETE" });
		}
    	
    	for(int i = 0; i < paNaverKindsExceptList.size(); i++){
    		executedRtn = paNaverCommonDAO.checkPaNaverCategoryInfo(paNaverKindsExceptList.get(i));
    		if(executedRtn < 1) {
    			executedRtn = paNaverCommonDAO.insertPaNaverCategoryInfoList(paNaverKindsExceptList.get(i));
    			if (executedRtn < 1) {		
    				throw processException("msg.cannot_save", new String[] { "TPANAVERGOODSKINDSEXCEPT INSERT" });
    			}			
    		}
    	}
    	
    	for(int i=0; i < paNaverGoodsKindsList.size(); i++){    		
    		paNaverCommonDAO.updatePaNaverCategoryInfoYN(paNaverGoodsKindsList.get(i));
    	}
    	
    	for(int i = 0; i < paNaverKindsCertiList.size(); i++){
    		executedRtn = paNaverCommonDAO.checkPaNaverKindsCerti(paNaverKindsCertiList.get(i));
    		if (executedRtn < 1) {
    			executedRtn = paNaverCommonDAO.insertPaNaverKindsCertiList(paNaverKindsCertiList.get(i));
    			if (executedRtn < 1) {		
    				throw processException("msg.cannot_save", new String[] { "TPANAVERGOODSKINDSCERTI INSERT" });
    			}			
    		}
    	}
    	
    	for(int i = 0; i < paNaverKindsCertiKindsList.size(); i++){
    		executedRtn = paNaverCommonDAO.checkPaNaverKindsCertiKindsList(paNaverKindsCertiKindsList.get(i));
    		if (executedRtn < 1) {
    			executedRtn = paNaverCommonDAO.insertPaNaverKindsCertiKindsList(paNaverKindsCertiKindsList.get(i));
    			if (executedRtn < 1) {		
    				throw processException("msg.cannot_save", new String[] { "TPANAVERGOODSKINDSCERTIKIND INSERT" });
    			}			
    		}
    	}
    	
    	
		return rtnMsg;
	}

	//최하위 카테고리 ID 조회
	@Override
	public List<Panavergoodskinds> selectPaNaverCategoryInfo() throws Exception {
		return paNaverCommonDAO.selectPaNaverCategoryInfo();
	}
	
	//네이버 주소록 조회 및 수정
		@Override
		public String savePaNaverAddressList(List<Panaverentpaddressmoment> paNaverEntpAddressMomentList) throws Exception {
			String rtnMsg = Constants.SAVE_SUCCESS;
			int totalExecutedRtn = 0;
			int executedRtn = 0;
			List<PanaverentpaddressmomentVO> paNaverEntpAddressList = null;
			Panaverentpaddressmoment paNaverEntpAddressMoment = null;
			try{

				if(paNaverEntpAddressMomentList.size() > 0){
					for(int i=0; i<paNaverEntpAddressMomentList.size(); i++){				
						executedRtn = paNaverCommonDAO.insertPaNaverEntpAddressMomentList(paNaverEntpAddressMomentList.get(i));
					}
					if(executedRtn < 0){
						log.info("tpanaverentpaddressmoment insert fail");
						throw processException("msg.cannot_save", new String[] { "TPANAVERENTPADDRESSMOMENT INSERT" });
					}
					totalExecutedRtn += executedRtn;
				}else{
					log.info("tpamaverentpaddressmoment insert fail");
					throw processException("msg.cannot_save", new String[] { "TPANAVERENTPADDRESSMOMENT INSERT" });
				}

				if(totalExecutedRtn > 0){
					paNaverEntpAddressList = paNaverCommonDAO.selectInsertOrUpdate();
					if(paNaverEntpAddressList.size() > 0){
						for(int i=0; i<paNaverEntpAddressList.size(); i++){
							if(paNaverEntpAddressList.get(i).getFlag().equals("I")){
								paNaverCommonDAO.insertPaNaverEntpAddressList(paNaverEntpAddressList.get(i));
								paNaverCommonDAO.updateTpaEntpSlipChange(paNaverEntpAddressList.get(i));
							}else if(paNaverEntpAddressList.get(i).getFlag().equals("U")){
								paNaverCommonDAO.updatePaNaverEntpAddressList(paNaverEntpAddressList.get(i));
							}else{
								continue;
							}
						}
					}
				}
				
				paNaverCommonDAO.deleteTpaEntpSlip();
				paNaverCommonDAO.insertTpaEntpSlip();
				
			}catch(Exception e){
				rtnMsg = e.getMessage();
	    		throw e;
			}
			return rtnMsg;
		}

}	

