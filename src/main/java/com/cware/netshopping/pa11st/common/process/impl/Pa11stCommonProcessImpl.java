package com.cware.netshopping.pa11st.common.process.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.Pa11stnodelyareamVO;
import com.cware.netshopping.domain.model.Pa11stOrigin;
import com.cware.netshopping.domain.model.Pa11stSettlement;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.pa11st.common.process.Pa11stCommonProcess;
import com.cware.netshopping.pa11st.common.repository.Pa11stCommonDAO;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;

@Service("pa11st.common.pa11stCommonProcess")
public class Pa11stCommonProcessImpl extends AbstractService implements Pa11stCommonProcess{

	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "pa11st.common.pa11stCommonDAO")
	private Pa11stCommonDAO pa11stCommonDAO;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Override
	public String savePa11stGoodsKinds(List<PaGoodsKinds> paGoodsKindsList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			executedRtn = paCommonDAO.deletePaGoodsKindsListMoment(paGoodsKindsList.get(0).getPaGroupCode());
			
			if(executedRtn < 0){
				log.info("tpa11stlmsdlist delete fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS DELETE" });
			}
			
			for(PaGoodsKinds paGoodsKinds:paGoodsKindsList){
				executedRtn += paCommonDAO.insertPaGoodsKindsListMoment(paGoodsKinds); //= tpagoodskinds insert
			}
			if(executedRtn > 0){
				log.info("tpa11stlmsdlist insert success");
				//= tpagoodskinds insert
				executedRtn = paCommonDAO.insertPaGoodsKindsList(paGoodsKindsList.get(0)); 
				if (executedRtn < 0) {
					log.info("tpa11stlmsdlist insert fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS INSERT" });
				}
					
				//= tpagoodskinds update
				executedRtn = paCommonDAO.updatePaGoodsKindsList(paGoodsKindsList.get(0)); 
				if (executedRtn < 0) {
					log.info("tpa11stlmsdlist update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS UPDATE" });
				}
					
				//= tpagoodskinds use_yn update
				executedRtn = paCommonDAO.updatePaGoodsKindsListUseYn(paGoodsKindsList.get(0)); 
				if (executedRtn < 0) {
					log.info("tpa11stlmsdlist update use_yn fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS UPDATE" });
				}
				
				//사용하지 않는 카테고리 use_yn = 0 처리.
				executedRtn = pa11stCommonDAO.updatePaGoodsKindsUnUseListUseYn(paGoodsKindsList.get(0)); 
				if (executedRtn < 0) {
					log.info("tpa11stlmsdlist update use_yn fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS UPDATE" });
				}
					
			} else {
				log.info("tpa11stlmsdlistmoment insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDSMOMENT INSERT" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public HashMap<String, String> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception {
		return pa11stCommonDAO.selectEntpShipInsertList(paEntpSlip);
	}

	@Override
	public String savePa11stEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			executedRtn = pa11stCommonDAO.insertPa11stEntpSlip(paEntpSlip);
			
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
		return pa11stCommonDAO.selectEntpShipUpdateList(paAddrGb);
	}

	@Override
	public String savePa11stEntpSlipUpdate(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			executedRtn = pa11stCommonDAO.updatePa11stEntpSlip(paEntpSlip);
			
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
	public String saveMappingPa11stOrigin(List<Pa11stOrigin> pa11stOriginList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		Pa11stOrigin pa11stOrigin = null;
		try {
			if(pa11stOriginList.size() > 0){
				executedRtn = pa11stCommonDAO.deletePa11stOrigin();
				if (executedRtn < 0) {
					//= 오류
					throw processException("msg.cannot_save", new String[] { "TPA11STORIGIN DELETE" });
				}
				
				for(int i = 0; i < pa11stOriginList.size(); i++){
					pa11stOrigin = pa11stOriginList.get(i);
					executedRtn = pa11stCommonDAO.insertPa11stOrigin(pa11stOrigin); 
					if (executedRtn < 1) {
						//= 오류
						throw processException("msg.cannot_save", new String[] { "TPA11STORIGIN INSERT" });
					}					
				}
				executedRtn = pa11stCommonDAO.insertPa11stOriginMapping(pa11stOriginList.get(0));
				if (executedRtn < 0) {
					//= 오류
					throw processException("msg.cannot_save", new String[] { "TPA11STORIGINMAPPING INSERT" });
				}
				
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public String savePa11stCertGoodsKinds(List<PaGoodsKinds> paGoodsKinds) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		int count = 0;
		
		try {
			log.info(Integer.toString(paGoodsKinds.size()));
			for(int i=0; i<paGoodsKinds.size(); i++){
			
				PaGoodsKinds paCertGoodsKinds =  paGoodsKinds.get(i);
				executedRtn = pa11stCommonDAO.updatePa11stCertGoodsKinds(paCertGoodsKinds);
				if(executedRtn>0)
					count++;
				if(executedRtn == 0)
					log.info("LGROUP "+paCertGoodsKinds.getPaLgroup()+", MGROUP "+paCertGoodsKinds.getPaMgroup()
							+", SGROUP "+paCertGoodsKinds.getPaSgroup()+", DGROUP "+paCertGoodsKinds.getPaDgroup());
				if(executedRtn < 0){
					log.info("TPAGOODSKINDS update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS UPDATE" });
				}
			}
			log.info(Integer.toString(count));
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;	
	}

	@Override
	public String savePa11stOfferCode(List<PaOfferCode> paOfferCodeList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		List<PaOfferCode> paOfferTypeList = null;
		try {
			//= tpaoffercodemoment delete
			executedRtn = pa11stCommonDAO.deletePa11stOfferCodeMoment();
			
			if(executedRtn < 0){
				log.info("tpaoffercodemoment delete fail");
				throw processException("msg.cannot_save", new String[] { "TPAOFFERCODEMOMENT DELETE" });
			}
			executedRtn = 0;
			for(PaOfferCode paOfferCode:paOfferCodeList){
				//= tpaoffercodemoment insert
				executedRtn += pa11stCommonDAO.insertPa11stOfferCodeMoment(paOfferCode); 
			}
			
			if(executedRtn > 0){
				log.info("tpaoffercodemoment insert success");
				//= tpaoffercode insert
				executedRtn = pa11stCommonDAO.insertPa11stOfferCodeList(paOfferCodeList.get(0)); 
				if (executedRtn < 0) {
					log.info("tpaoffercodemoment insert fail");
					throw processException("msg.cannot_save", new String[] { "TPAOFFERCODE INSERT" });
				}
					
				//= tpaoffercode update
				executedRtn = pa11stCommonDAO.updatePa11stOfferCodeList(paOfferCodeList.get(0)); 
				if (executedRtn < 0) {
					log.info("tpa11stlmsdlist update fail");
					throw processException("msg.cannot_save", new String[] { "TPAOFFERCODE UPDATE" });
				}
					
				//= tpaoffercode use_yn update
				executedRtn = pa11stCommonDAO.updatePa11stOfferCodeListUseYn(paOfferCodeList.get(0)); 
				if (executedRtn < 0) {
					log.info("tpa11stlmsdlist update use_yn fail");
					throw processException("msg.cannot_save", new String[] { "TPAOFFERCODE UPDATE" });
				}
				
				paOfferTypeList = pa11stCommonDAO.selectPaOfferTypeList(paOfferCodeList.get(0));
				
				if(paOfferTypeList.size() > 0){					
					for(PaOfferCode paOfferType:paOfferTypeList){
						paOfferType.setInsertId(paOfferCodeList.get(0).getInsertId());
						paOfferType.setModifyId(paOfferCodeList.get(0).getModifyId());
						paOfferType.setInsertDate(paOfferCodeList.get(0).getInsertDate());
						paOfferType.setModifyDate(paOfferCodeList.get(0).getModifyDate());
						executedRtn = pa11stCommonDAO.updatePaOfferType(paOfferType);
						if (executedRtn == 0) {
							executedRtn = pa11stCommonDAO.insertPaOfferType(paOfferType); 
							if (executedRtn < 0) {
								log.info("tcode[O506] insert fail");
								throw processException("msg.cannot_save", new String[] { "TCODE INSERT" });
							} 
						}
					}
				}
					
			} else {
				log.info("tpa11stlmsdlistmoment insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAOFFERCODEMOMENT INSERT" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public List<Object> selectTroadPostList(String paCode) throws Exception {
		return pa11stCommonDAO.selectTroadPostList(paCode);
	}
	
	@Override
	public String saveNoDelyAreaInsert(List<Pa11stnodelyareamVO> arrPa11stNoDelyAreaList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		String noDelyAreaCode = "";
		Pa11stnodelyareamVO pa11stnodelyaream = null;
		
		if(arrPa11stNoDelyAreaList.size() > 0){
			noDelyAreaCode = systemService.getMaxNo("TPA11STNODELYAREAM", "NO_DELY_AREA_CODE", "", 6);
			
			pa11stnodelyaream = arrPa11stNoDelyAreaList.get(0);
			pa11stnodelyaream.setNoDelyAreaCode(noDelyAreaCode);
			
			executedRtn = pa11stCommonDAO.insertPa11stNoDelyAreaM(pa11stnodelyaream);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPA11STNODELYAREAM INSERT" });
			}
			
			for(int i = 0; i < arrPa11stNoDelyAreaList.size(); i++){
				pa11stnodelyaream = arrPa11stNoDelyAreaList.get(i);
				pa11stnodelyaream.setNoDelyAreaCode(noDelyAreaCode);
				executedRtn = pa11stCommonDAO.insertPa11stNoDelyAreaDt(pa11stnodelyaream); 
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TPA11STNODELYAREADT INSERT" });
				}					
			}
			
		}
		return rtnMsg;
	}
	
	@Override
	public String saveNoDelyAreaSelect(List<Pa11stnodelyareamVO> arrPa11stNoDelyAreaList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		Pa11stnodelyareamVO pa11stnodelyaream = null;
		try {
			if(arrPa11stNoDelyAreaList.size() > 0){
				
				for(int i = 0; i < arrPa11stNoDelyAreaList.size(); i++){
					pa11stnodelyaream = arrPa11stNoDelyAreaList.get(i);
					
					executedRtn = pa11stCommonDAO.updatePa11stNoDelyAreaDt(pa11stnodelyaream); 
					if (executedRtn < 1) {
						throw processException("msg.cannot_save", new String[] { "TPA11STNODELYAREADT UPDATE" });
					}					
				}
				
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}
	
	@Override
	public List<Object> selectNoDelyAreaUpdateList(String paCode) throws Exception {
		return pa11stCommonDAO.selectNoDelyAreaUpdateList(paCode);
	}
	
	@Override
	public String saveNoDelyAreaUpdate(List<Pa11stnodelyareamVO> arrPa11stNoDelyAreaList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		Pa11stnodelyareamVO pa11stnodelyaream = null;
		
		if(arrPa11stNoDelyAreaList.size() > 0){
			for(int i = 0; i < arrPa11stNoDelyAreaList.size(); i++){
				pa11stnodelyaream = arrPa11stNoDelyAreaList.get(i);
				
				if(pa11stnodelyaream.getCwareAction().equals("I")){
					executedRtn = pa11stCommonDAO.insertPa11stNoDelyAreaDt(pa11stnodelyaream); 
					if (executedRtn < 1) {
						throw processException("msg.cannot_save", new String[] { "TPA11STNODELYAREADT INSERT" });
					}
				}else if(pa11stnodelyaream.getDeleteYn().equals("Y") || pa11stnodelyaream.getDeleteYn().equals("N")){
					executedRtn = pa11stCommonDAO.updatePa11stNoDelyAreaDtDeleteYn(pa11stnodelyaream); 
					if (executedRtn < 1) {
						throw processException("msg.cannot_save", new String[] { "TPA11STNODELYAREADT INSERT" });
					}
				}
			}
			
		}
		return rtnMsg;
	}
	
	@Override
	public List<Object> selectNoDelyAreaApplyList(String paCode) throws Exception {
		return pa11stCommonDAO.selectNoDelyAreaApplyList(paCode);
	}
	
	@Override
	public String saveNoDelyAreaApply(Pa11stnodelyareamVO arrPa11stNoDelyArea) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try{
			if(arrPa11stNoDelyArea != null){
				executedRtn = pa11stCommonDAO.updatePa11stNoDelyAreaApply(arrPa11stNoDelyArea); 
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TPA11STNODELYAREAAPPLY UPDATE" });
				}
			}
		}catch (Exception e) {
			rtnMsg = e.getMessage();
		}

		return rtnMsg;
	}
	
	@Override
	public List<Object> selectNoDelyAreaDeleteList(String paCode) throws Exception {
		return pa11stCommonDAO.selectNoDelyAreaDeleteList(paCode);
	}
	
	@Override
	public String saveNoDelyAreaApplyGoods(String paCode) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		String noDelyAreaCode = "";
		int executedRtn = 0;
		Pa11stnodelyareamVO pa11stnodelyaream = null;
		Timestamp sysdateTime = DateUtil.toTimestamp(systemService.getSysdatetimeToString());
		try{
			pa11stnodelyaream = new Pa11stnodelyareamVO();
			noDelyAreaCode = pa11stCommonDAO.selectNoDelyAreaCodeList(paCode);
			
			pa11stnodelyaream.setNoDelyAreaCode(noDelyAreaCode);
			pa11stnodelyaream.setPaCode(paCode);
			pa11stnodelyaream.setInsertId("PA11");
			pa11stnodelyaream.setModifyId("PA11");
			pa11stnodelyaream.setInsertDate(sysdateTime);
			pa11stnodelyaream.setModifyDate(sysdateTime);
			
			executedRtn = pa11stCommonDAO.insertPa11stNoDelyAreaApply(pa11stnodelyaream);
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPA11STNODELYAREAAPPLY INSERT" });
			}
		}catch (Exception e) {
			rtnMsg = e.getMessage();
		}

		return rtnMsg;
	}
	
	@Override
	public int checkTpa11stNoDelyAream(String paCode) throws Exception {
		return pa11stCommonDAO.checkTpa11stNoDelyAream(paCode);
	}
	
	@Override
	public String savePa11stBrand(List<PaBrand> paBrandList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		PaBrand paBrand = null;
		
		try{
			if(paBrandList.size() > 0){
				/*executedRtn = pa11stCommonDAO.deletePa11stOrigin();
				if (executedRtn < 0) {
					//= 오류
					throw processException("msg.cannot_save", new String[] { "TPA11STORIGIN DELETE" });
				}*/
				
				for(int i = 0; i < paBrandList.size(); i++){
					paBrand = paBrandList.get(i);
					executedRtn = pa11stCommonDAO.insertPaBrandList(paBrand); 
					if (executedRtn < 1) {
						//= 오류
						throw processException("msg.cannot_save", new String[] { "TPA11STBRAND INSERT" });
					}					
				}
			}
		}catch (Exception e) {
			rtnMsg = e.getMessage();
		}

		return rtnMsg;
	}
	/**
	 * 11번가 출고지별 배송비관리 할 항목 조회
	 * */
	@Override
	public List<HashMap<?,?>> selectEntpSlipCost(String paCode) throws Exception {
		return pa11stCommonDAO.selectEntpSlipCost(paCode);
	}
	@Override
	public int updateEntpSlipCost(HashMap<String,String> hashMap) throws Exception{
		return pa11stCommonDAO.updateEntpSlipCost(hashMap);
	}
	
	@Override
	public String savePa11stSettlement(List<Pa11stSettlement> pa11stSettlementList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int cnt = 0;
		int chk;
		try {
			for(Pa11stSettlement pa11stSettlement:pa11stSettlementList){
				//check query 추가
				chk = pa11stCommonDAO.selectPa11stSettlementList(pa11stSettlement); 
				if (chk == 0 ) {
					paCommonDAO.insertPa11stSettlementList(pa11stSettlement);
					cnt++;
				}
			}
			log.error("정산 성공 카운트" + cnt);
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}
}
