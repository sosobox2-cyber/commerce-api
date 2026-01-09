package com.cware.netshopping.pawemp.common.process.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.model.Brand;
import com.cware.netshopping.domain.model.Makecomp;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaWempBrand;
import com.cware.netshopping.domain.model.PaWempEntpSlip;
import com.cware.netshopping.domain.model.PaWempMaker;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pawemp.common.process.PaWempCommonProcess;
import com.cware.netshopping.pawemp.common.repository.PaWempCommonDAO;

@Service("pawemp.common.paWempCommonProcess")
public class PaWempCommonProcessImpl extends AbstractService implements PaWempCommonProcess {

	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "pawemp.common.paWempCommonDAO")
	private PaWempCommonDAO paWempCommonDAO;

	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	@Override
	public String savePaWempGoodsKinds(List<PaGoodsKinds> paGoodsKindsList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;

		try {
			executedRtn = paCommonDAO.deletePaGoodsKindsListMoment(paGoodsKindsList.get(0).getPaGroupCode());

			if (executedRtn < 0) {
				log.info("tpawemplmsdlist delete fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS DELETE" });
			}
			for (PaGoodsKinds paGoodsKinds : paGoodsKindsList) {
				executedRtn += paCommonDAO.insertPaGoodsKindsListMoment(paGoodsKinds); // = tpagoodskinds insert
			}

			if (executedRtn > 0) {
				log.info("tpawemplmsdlistMoment insert success");
				// = tpagoodskinds insert
				executedRtn = paCommonDAO.insertPaGoodsKindsList(paGoodsKindsList.get(0));

				if (executedRtn < 0) {
					log.info("tpawemplmsdlist insert fail");
					throw processException("msg.cannot_save",new String[] { "TPAGOODSKINDS INSERT" });
				}

				// = tpagoodskinds update
				executedRtn = paCommonDAO.updatePaGoodsKindsList(paGoodsKindsList.get(0));

				if (executedRtn < 0) {
					log.info("tpawemplmsdlist update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS UPDATE" });
				}

				// = tpagoodskinds use_yn update
				executedRtn = paCommonDAO.updatePaGoodsKindsListUseYn(paGoodsKindsList.get(0));

				if (executedRtn < 0) {
					log.info("tpawemplmsdlist updateUseYn fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS UPDATE USEYN" });
				}
			} else {
				log.info("tpawemplmsdlistmoment insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDSMOMENT INSERT" });
			}

		} catch (Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}

		return rtnMsg;
	}
	
	@Override
	public String savePaWempOfferCode(List<PaOfferCode> paOfferCodeList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		List<PaOfferCode> paOfferTypeList = null;
		try {
			//= tpaoffercodemoment delete
			executedRtn = paWempCommonDAO.deletePaWempOfferCodeMoment();
			
			if(executedRtn < 0){
				log.info("tpaoffercodemoment delete fail");
				throw processException("msg.cannot_save", new String[] { "TPAOFFERCODEMOMENT DELETE" });
			}
			executedRtn = 0;
			for(PaOfferCode paOfferCode:paOfferCodeList){
				//= tpaoffercodemoment insert
				executedRtn += paWempCommonDAO.insertPaWempOfferCodeMoment(paOfferCode); 
			}
			
			if(executedRtn > 0){
				log.info("tpaoffercodemoment insert success");
				//= tpaoffercode insert
				executedRtn = paWempCommonDAO.insertPaWempOfferCodeList(paOfferCodeList.get(0)); 
				if (executedRtn < 0) {
					log.info("tpaoffercodemoment insert fail");
					throw processException("msg.cannot_save", new String[] { "TPAOFFERCODE INSERT" });
				}
					
				//= tpaoffercode update
				executedRtn = paWempCommonDAO.updatePaWempOfferCodeList(paOfferCodeList.get(0)); 
				if (executedRtn < 0) {
					log.info("tpaoffercode update fail");
					throw processException("msg.cannot_save", new String[] { "TPAOFFERCODE UPDATE" });
				}
					
				//= tpaoffercode use_yn update
				executedRtn = paWempCommonDAO.updatePaWempOfferCodeListUseYn(paOfferCodeList.get(0)); 
				if (executedRtn < 0) {
					log.info("tpaoffercode update use_yn fail");
					throw processException("msg.cannot_save", new String[] { "TPAOFFERCODE UPDATE" });
				}
				
				paOfferTypeList = paWempCommonDAO.selectPaOfferTypeList(paOfferCodeList.get(0));
				
				if(paOfferTypeList.size() > 0){					
					for(PaOfferCode paOfferType:paOfferTypeList){
						paOfferType.setInsertId(paOfferCodeList.get(0).getInsertId());
						paOfferType.setModifyId(paOfferCodeList.get(0).getModifyId());
						paOfferType.setInsertDate(paOfferCodeList.get(0).getInsertDate());
						paOfferType.setModifyDate(paOfferCodeList.get(0).getModifyDate());
						executedRtn = paWempCommonDAO.updatePaOfferType(paOfferType);
						if (executedRtn == 0) {
							executedRtn = paWempCommonDAO.insertPaOfferType(paOfferType); 
							if (executedRtn < 0) {
								log.info("tcode[O506] insert fail");
								throw processException("msg.cannot_save", new String[] { "TCODE INSERT" });
							} 
						}
					}
				}
					
			} else {
				log.info("tpaoffercodemoment insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAOFFERCODEMOMENT INSERT" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public List<Brand> selectBrandList() throws Exception {
		return paWempCommonDAO.selectBrandList();
	}

	@Override
	public String savePaWempBrand(List<PaWempBrand> paWempBrandList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;

		try { 
			// = paWempBrand insert
			for (PaWempBrand paWempBrand : paWempBrandList) {
				executedRtn += paWempCommonDAO.insertPaWempBrandList(paWempBrand);
			}

			if (executedRtn < 0) {
				log.info("paBrandList insert fail");
				throw processException("msg.cannot_save",new String[] { "TPAWEMPBRAND INSERT" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}

		return rtnMsg;
	}

	@Override
	public List<Makecomp> selectMakerList() throws Exception {
		return paWempCommonDAO.selectMakerList();
	}

	@Override
	public String savePaWempMaker(List<PaWempMaker> paWempMakerList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;

		try { 
			// = paWempMaker insert
			for (PaWempMaker paWempMaker : paWempMakerList) {
				executedRtn += paWempCommonDAO.insertPaWempMakerList(paWempMaker);
			}

			if (executedRtn < 0) {
				log.info("paMakerList insert fail");
				throw processException("msg.cannot_save",new String[] { "TPAWEMPMAKER INSERT" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}

		return rtnMsg;
	}
	
	@Override
	public HashMap<String, Object> selectEntpShipInsertList(PaWempEntpSlip paEntpSlip) throws Exception {
		return paWempCommonDAO.selectEntpShipInsertList(paEntpSlip);
	}

	@Override
	public String savePaWempEntpSlip(PaWempEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;

		try {
			executedRtn = paWempCommonDAO.insertPaWempEntpSlip(paEntpSlip);
			if (executedRtn < 0) {
				log.info("TPAWEMPENTPSLIP insert fail");
				throw processException("msg.cannot_save", new String[]{"TPAWEMPENTPSLIP INSERT"});
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}

		return rtnMsg;
	}
	
	@Override
	public List<Object> selectEntpShipUpdateList(ParamMap paramMap) throws Exception {
		return paWempCommonDAO.selectEntpShipUpdateList(paramMap);
	}

	@Override
	public String updatePaWempEntpSlip(PaWempEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;

		try {
			//출고/회수지 target_yn = '0' 셋팅
			executedRtn = paWempCommonDAO.updatePaWempEntpSlip(paEntpSlip);
			if (executedRtn < 0) {
				log.info("TPAWEMPENTPSLIP update fail");
				throw processException("msg.cannot_save", new String[]{"TPAWEMPENTPSLIP UPDATE"});
			}
			//베송비 target_yn = '0' 셋팅
			executedRtn = paWempCommonDAO.updatePaWempCustShipCost(paEntpSlip);
			if (executedRtn < 0) {
				log.info("TPACUSTSHIPCOST update fail");
				throw processException("msg.cannot_save", new String[]{"TPACUSTSHIPCOST UPDATE"});
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}

		return rtnMsg;
	}
}
