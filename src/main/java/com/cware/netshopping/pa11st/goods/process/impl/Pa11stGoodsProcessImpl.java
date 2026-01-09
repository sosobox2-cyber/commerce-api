package com.cware.netshopping.pa11st.goods.process.impl;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.Pa11stGoodsPriceVO;
import com.cware.netshopping.domain.Pa11stGoodsVO;
import com.cware.netshopping.domain.Pa11stGoodsdtMappingVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.model.Pa11stPolicy;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsAuthYnLog;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.PaSaleNoGoods;
import com.cware.netshopping.pa11st.goods.process.Pa11stGoodsProcess;
import com.cware.netshopping.pa11st.goods.repository.Pa11stGoodsDAO;
import com.cware.netshopping.pacommon.common.process.PaCommonProcess;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;

@Service("pa11st.goods.pa11stGoodsProcess")
public class Pa11stGoodsProcessImpl extends AbstractService implements Pa11stGoodsProcess {
	
	@Resource(name = "pacommon.common.pacommonProcess")
    private PaCommonProcess pacommonProcess;
	
	@Resource(name = "pa11st.goods.pa11stGoodsDAO")
	private Pa11stGoodsDAO pa11stGoodsDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pacommon.common.paCommonDAO")
    private PaCommonDAO pacommonDAO;	
   
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Override
	public PaEntpSlip selectPa11stEntpSlip(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stEntpSlip(paramMap);
	}

	@Override
	public PaEntpSlip selectPa11stReturnSlip(ParamMap paramMap) throws Exception {

		return pa11stGoodsDAO.selectPa11stReturnSlip(paramMap);
	}

	@Override
	public Pa11stGoodsVO selectPa11stGoodsInfo(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stGoodsInfo(paramMap);
	}

	@Override
	public List<PaGoodsdtMapping> selectPa11stGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stGoodsdtInfoList(paramMap);
	}
	

	@Override
	public List<PaGoodsOffer> selectPa11stGoodsOfferList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stGoodsOfferList(paramMap);
	}

	@Override
	public String savePa11stGoods(Pa11stGoodsVO pa11stGoods, List<PaGoodsdtMapping> pa11stGoodsdt, List<PaPromoTarget> paPromoTargetList, String prgId) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			// 1이상일 경우 수정도중 MODIFY 변동 , 0일경우 변동없음
			executedRtn = pa11stGoodsDAO.selectPa11stGoodsModifyCheck(pa11stGoods);
			//MODIFY_DATE 변동 없을경우 TARGET 0 / 아닌경우 수정대상 재포함
			if( executedRtn == 0 ) {
				executedRtn = pa11stGoodsDAO.updatePa11stGoods(pa11stGoods);
				if (executedRtn < 0) {
					log.info("tpa11stgoods update fail");
					throw processException("msg.cannot_save", new String[] { "TPA11STGOODS UPDATE" });
				}
				
				executedRtn = pa11stGoodsDAO.updatePa11stGoodsImage(pa11stGoods);
				if (executedRtn < 0) {
					log.info("tpagoodsimage update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSIMAGE UPDATE" });
				}
				
				executedRtn = pa11stGoodsDAO.updatePa11stCustShipCost(pa11stGoods);
				if (executedRtn < 0) {
					log.info("tpacustshipcost update fail");
					throw processException("msg.cannot_save", new String[] { "TPACUSTSHIPCOST UPDATE" });
				}
				
				executedRtn = pa11stGoodsDAO.updatePa11stGoodsOffer(pa11stGoods);
				if (executedRtn < 0) {
					log.info("tpa11stgoodsoffer update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSOFFER UPDATE" });
				}

			}
			
			//상품등록API의 경우에만 GOODSDTMAPPING의 TRAGET을 0으로 만든다.
			if("IF_PA11STAPI_01_001".equals(prgId)) {
				for(int i=0; i<pa11stGoodsdt.size(); i++){
					pa11stGoodsdt.get(i).setModifyId(pa11stGoods.getModifyId());
					pa11stGoodsdt.get(i).setModifyDate(pa11stGoods.getModifyDate());
					executedRtn = pa11stGoodsDAO.updatePa11stGoodsdt(pa11stGoodsdt.get(i));
					if (executedRtn < 0) {
						log.info("tpagoodsdt update fail");
						throw processException("msg.cannot_save", new String[] { "TPAGOODSDT UPDATE" });
					}
				}
			}
			
			//TODO 실시간가격변경 완료후 데이터 update or insert
			PaPromoTarget paPromoTarget;
	 	    for(int i =0 ; i< paPromoTargetList.size() ; i++){
	 	    	paPromoTarget = paPromoTargetList.get(i);
	 	    	
	 	    	if(paPromoTarget.getDoCost() > 0) {
		 	    	executedRtn=pa11stGoodsDAO.updatePaPromoTargetCalc(paPromoTarget);
		 		    if (executedRtn < 0) {
		 				throw processException("msg.cannot_save", new String[] { "updatePaPromoTargetCalc" });
		 		    }
	 	    	}
	 		    
	 		    //프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
	 		    /*if( i == 0){
	 		    	pa11stGoods.setDoCost(paPromoTarget.getDoCost());
	 		    	pa11stGoods.setDoOwnCost(paPromoTarget.getDoOwnCost());
	 		    	pa11stGoods.setDoEntpCost(paPromoTarget.getDoEntpCost());
	 		    	pa11stGoods.setPromoNo(paPromoTarget.getPromoNo());
	 		    	pa11stGoods.setSeq(paPromoTarget.getSeq());
	 		    }*/
	 		    pa11stGoods.setDoCost(paPromoTarget.getDoCost());
	 		    pa11stGoods.setDoOwnCost(paPromoTarget.getDoOwnCost());
	 		    pa11stGoods.setDoEntpCost(paPromoTarget.getDoEntpCost());
	 		    pa11stGoods.setPromoNo(paPromoTarget.getPromoNo());
	 		    pa11stGoods.setSeq(paPromoTarget.getSeq());
	 		    //프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
	 		    if(paPromoTarget.getTransDate() == null){
	 			   //papromotarget 제휴 프로모션 테이블에 실적용 가격 및 trans_date 업데이트
	 			   executedRtn=pa11stGoodsDAO.updatePaPromoTarget(pa11stGoods);
	 			   if (executedRtn < 0) {
	 				   throw processException("msg.cannot_save", new String[] { "updatePaPromoTarget" });
	 			   }	 
	 			   
	 		    } else if(paPromoTarget.getTransDate() != null) {
	 			  executedRtn=pa11stGoodsDAO.insertNewPaPromoTarget(pa11stGoods);
	 			   if (executedRtn < 0) {
	 				   throw processException("msg.cannot_save", new String[] { "InsertPaPromoTarget" });
	 			   }	 
	 		   }
	 	    }
	 	    
	 	    
	 	    //UPDATE TPAGOODSPRICE DATE		
	 		Pa11stGoodsPriceVO pa11stGoodsPriceVO = pa11stGoodsDAO.selectPa11stGoodsPriceOne(pa11stGoods);
	 		pa11stGoodsPriceVO.setTransId(pa11stGoods.getModifyId());
	 		pa11stGoodsPriceVO.setInsertId(pa11stGoods.getModifyId());
	 		pa11stGoodsPriceVO.setModifyId(pa11stGoods.getModifyId());
	 		
	 		//제휴프로모션에 데이터가 있는경우, supplyPrice가 변경되기때문에 1row insert처리, 
	 		//				 데이터가 없는경우, 기존 데이터 update처리
//	 		if(!pa11stGoodsPriceVO.getSupplyPrice().equals(pa11stGoodsPriceVO.getNewSupplyPrice())){
//	 			executedRtn = pa11stGoodsDAO.updatePa11stGoodsPrice(pa11stGoods);
//	 			if (executedRtn < 0) {
//	 	 			throw processException("msg.cannot_save", new String[] { "insertPaGmktGoodsPrice" });
//	 	 	    }
//	 			
//	 			//seq의 경우는 pa_code+goods_code+apply_date가 같은경우, insert될때만 + 1 
//	 			pa11stGoodsPriceVO.setSupplySeq(systemService.getMaxNo("TPAGOODSPRICE", "SUPPLY_SEQ", 
//	 					"GOODS_CODE = '" + pa11stGoodsPriceVO.getGoodsCode() + "'"
//	 			  + " AND PA_CODE = '" + pa11stGoodsPriceVO.getPaCode() + "'"
//	 			  + " AND APPLY_DATE = TO_TIMESTAMP('" + pa11stGoodsPriceVO.getApplyDate() + "','YYYY-MM-DD HH24:MI:SS.FF')", 4));
//	 			executedRtn = pa11stGoodsDAO.insertPa11stGoodsPrice(pa11stGoodsPriceVO);
//	 			if (executedRtn < 0) {
//	 	 			throw processException("msg.cannot_save", new String[] { "insertPaGmktGoodsPrice" });
//	 	 	    }
//	 		}else{
//	 			int targetCnt = pa11stGoodsDAO.selectPa11stGoodsPriceUpdateTargetCnt(pa11stGoodsPriceVO);
//	 			if(targetCnt == 1){
//	 				executedRtn = pa11stGoodsDAO.updatePa11stGoodsPriceForPromo(pa11stGoodsPriceVO);
//		 	 		if (executedRtn < 0) {
//		 	 			throw processException("msg.cannot_save", new String[] { "updatePa11stGoodsPrice" });
//		 	 	    }
//	 			}
//	 			executedRtn = pa11stGoodsDAO.updatePa11stGoodsPriceForPromo(pa11stGoodsPriceVO);
//	 	 		if (executedRtn < 0) {
//	 	 			throw processException("msg.cannot_save", new String[] { "updatePaGmktGoodsPrice" });
//	 	 	    }
//	 		}
			
			executedRtn = pa11stGoodsDAO.updatePa11stGoodsPrice(pa11stGoods);
			if (executedRtn < 0) {
				log.info("tpagoodsprice update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICE UPDATE" });
			}

			executedRtn = pa11stGoodsDAO.updatePaGoodsTarget(pa11stGoods);
			if (executedRtn < 0) {
				log.info("tpagoodstarget update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSTARGET UPDATE" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
		return rtnMsg;
	}

	@Override
	public List<PaEntpSlip> selectPa11stEntpSlipList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stEntpSlipList(paramMap);
	}

	@Override
	public List<PaEntpSlip> selectPa11stReturnSlipList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stReturnSlipList(paramMap);
	}
	
	@Override
	public List<Pa11stGoodsVO> selectPa11stGoodsInfoList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stGoodsInfoList(paramMap);
	}

	@Override
	public List<PaGoodsPriceVO> selectPa11stGoodsPriceList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stGoodsPriceList(paramMap);

	}

	@Override
	public String savePa11stGoodsPrice(PaGoodsPriceVO paGoodsPrice, List<PaPromoTarget> paPromoTargetList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		/*
		Pa11stGoodsVO pa11stGoods = new Pa11stGoodsVO();
		pa11stGoods.setModifyId(paGoodsPrice.getModifyId());
		pa11stGoods.setModifyDate(paGoodsPrice.getModifyDate());
		pa11stGoods.setApplyDate(paGoodsPrice.getApplyDate());
		pa11stGoods.setGoodsCode(paGoodsPrice.getGoodsCode());
		pa11stGoods.setPaCode(paGoodsPrice.getPaCode());
		
		//TODO 실시간가격변경 완료후 데이터 update or insert
		PaPromoTarget paPromoTarget;
 	    for(int i =0 ; i< paPromoTargetList.size() ; i++){
 	    	paPromoTarget = paPromoTargetList.get(i);
 	    	executedRtn=pa11stGoodsDAO.updatePaPromoTargetCalc(paPromoTarget);
 		    if (executedRtn < 0) {
 				throw processException("msg.cannot_save", new String[] { "updatePaPromoTargetCalc" });
 		    }
 		    
 		    if( i == 0){
 		    	pa11stGoods.setDoCost(paPromoTarget.getDoCost());
 		    	pa11stGoods.setDoOwnCost(paPromoTarget.getDoOwnCost());
 		    	pa11stGoods.setDoEntpCost(paPromoTarget.getDoEntpCost());
 		    	pa11stGoods.setPromoNo(paPromoTarget.getPromoNo());
 		    	pa11stGoods.setSeq(paPromoTarget.getSeq());
 		    }
 	    }
 	    
 	    //papromotarget 제휴 프로모션 테이블에 실적용 가격 및 trans_date 업데이트
 	    executedRtn=pa11stGoodsDAO.updatePaPromoTarget(pa11stGoods);
	    if (executedRtn < 0) {
			throw processException("msg.cannot_save", new String[] { "updatePaPromoTarget" });
	    }
 	    
 	    //UPDATE TPAGOODSPRICE DATE		
 		Pa11stGoodsPriceVO pa11stGoodsPriceVO = pa11stGoodsDAO.selectPa11stGoodsPriceOne(pa11stGoods);
 		pa11stGoodsPriceVO.setTransId(pa11stGoods.getModifyId());
 		pa11stGoodsPriceVO.setInsertId(pa11stGoods.getModifyId());
 		pa11stGoodsPriceVO.setModifyId(pa11stGoods.getModifyId());
 		
 		//제휴프로모션에 데이터가 있는경우, supplyPrice가 변경되기때문에 1row insert처리, 
 		//				 데이터가 없는경우, 기존 데이터 update처리
 		if(!pa11stGoodsPriceVO.getSupplyPrice().equals(pa11stGoodsPriceVO.getNewSupplyPrice())){
 			//seq의 경우는 pa_code+goods_code+apply_date가 같은경우, insert될때만 + 1 
 			pa11stGoodsPriceVO.setSupplySeq(systemService.getMaxNo("TPAGOODSPRICE", "SUPPLY_SEQ", 
 					"GOODS_CODE = '" + pa11stGoodsPriceVO.getGoodsCode() + "'"
 			  + " AND PA_CODE = '" + pa11stGoodsPriceVO.getPaCode() + "'"
 			  + " AND APPLY_DATE = TO_TIMESTAMP('" + pa11stGoodsPriceVO.getApplyDate() + "','YYYY-MM-DD HH24:MI:SS.FF')", 4));
 			pa11stGoodsDAO.insertPa11stGoodsPrice(pa11stGoodsPriceVO);
 			if (executedRtn < 0) {
 	 			throw processException("msg.cannot_save", new String[] { "insertPaGmktGoodsPrice" });
 	 	    }
 		}else{
 			executedRtn = pa11stGoodsDAO.updatePa11stGoodsPriceForPromo(pa11stGoodsPriceVO);
 	 		if (executedRtn < 0) {
 	 			throw processException("msg.cannot_save", new String[] { "updatePaGmktGoodsPrice" });
 	 	    }
 		}
		*/
		
		
		try {
			Pa11stGoodsVO pa11stGoods = new Pa11stGoodsVO();
			pa11stGoods.setModifyId(paGoodsPrice.getModifyId());
			pa11stGoods.setModifyDate(paGoodsPrice.getModifyDate());
			pa11stGoods.setApplyDate(paGoodsPrice.getApplyDate());
			pa11stGoods.setGoodsCode(paGoodsPrice.getGoodsCode());
			pa11stGoods.setPaCode(paGoodsPrice.getPaCode());
			executedRtn = pa11stGoodsDAO.updatePa11stGoodsPrice(pa11stGoods);
			if (executedRtn < 0) {
				log.info("tpagoodsprice update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICE UPDATE" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
		
	}

	@Override
	public List<Pa11stGoodsdtMappingVO> selectPa11stGoodsdtMappingList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stGoodsdtMappingList(paramMap);
	}

	@Override
	public String savePa11stGoodsdtMapping(Pa11stGoodsdtMappingVO paGoodsdtMappingVO) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		executedRtn = pa11stGoodsDAO.updatePa11stGoodsdt(paGoodsdtMappingVO);
		if (executedRtn < 0) {
			log.info("tpagoodsdt update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
		}
		
		return rtnMsg;
	}

	@Override
	public String savePa11stGoodsdtQty(Pa11stGoodsdtMappingVO paGoodsdtMappingVO) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;

		executedRtn = pa11stGoodsDAO.updatePa11stGoodsdtQty(paGoodsdtMappingVO);
		if (executedRtn < 0) {
			log.info("tpagoodsdt update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
		}
		
		return rtnMsg;
	}
	@Override
	public HashMap<String, String> selectPa11stGoodsDescribe(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stGoodsDescribe(paramMap);
	}

	@Override
	public String savePa11stGoodsDescribe(HashMap<String, String> goodsDesc) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		Pa11stGoodsVO pa11stGoods = new Pa11stGoodsVO();
		
		pa11stGoods.setModifyId(goodsDesc.get("MODIFY_ID"));
		pa11stGoods.setModifyDate(DateUtil.toTimestamp(goodsDesc.get("MODIFY_DATE"), "yyyy/MM/dd HH:mm:ss"));
		pa11stGoods.setInsertDate(DateUtil.toTimestamp(goodsDesc.get("MODIFY_DATE"), "yyyy/MM/dd HH:mm:ss"));
		pa11stGoods.setGoodsCode(goodsDesc.get("GOODS_CODE"));
		
		executedRtn = pa11stGoodsDAO.updatePa11stGoods(pa11stGoods);
		if (executedRtn < 0) {
			log.info("tpa11stgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPA11STGOODS UPDATE" });
		}
		
		return rtnMsg;
	}

	@Override
	public int insertPa11stGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception {
		return paCommonDAO.insertPaGoodsTransLog(paGoodsTransLog);
	}

	@Override
	public String savePa11stGoodsdtMappingPaOptionCode(List<Pa11stGoodsdtMappingVO> paGoodsdtMapping) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;		
		
		for(int i=0; i<paGoodsdtMapping.size(); i++){
			executedRtn = pa11stGoodsDAO.updatePa11stGoodsdtMappingPaOptionCode(paGoodsdtMapping.get(i));
			if (executedRtn < 0) {
				log.info("tpagoodsdtmapping update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
			}
		}
		
		return rtnMsg;
	}

	@Override
	public Pa11stGoodsVO selectPa11stGoodsProductNo(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stGoodsProductNo(paramMap);
	}

	@Override
	public String savePa11stGoodsSell(Pa11stGoodsVO pa11stGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;		
		executedRtn = pa11stGoodsDAO.updatePa11stGoods(pa11stGoods);
		if (executedRtn < 0) {
			log.info("tpa11stgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPA11STGOODS UPDATE" });
		}
		return rtnMsg;
	}

	@Override
	public String checkPa11stCheckSaleGb(String goodsCode) throws Exception {
		return paCommonDAO.selectGoodsSaleGb(goodsCode);
	}
	
	@Override
	public List<Pa11stGoodsdtMappingVO> selectPa11stGoodsdtMappingStockList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stGoodsdtMappingStockList(paramMap);
	}

	@Override
	public List<Pa11stGoodsdtMappingVO> selectPa11stGoodsDtStockList() throws Exception {
		return pa11stGoodsDAO.selectPa11stGoodsDtStockList();
	}

	@Override
	public List<Pa11stGoodsVO> selectedSoldOutPa11stGoodsList() throws Exception {
		return pa11stGoodsDAO.selectedSoldOutPa11stGoodsList();
	}
	@Override
	public String updateSoldOutTransSaleYn(Pa11stGoodsVO pa11stGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		PaSaleNoGoods paSaleNoGoods = new PaSaleNoGoods();
		paSaleNoGoods.setPaGroupCode(pa11stGoods.getPaGroupCode());
		paSaleNoGoods.setPaCode(pa11stGoods.getPaCode());
		paSaleNoGoods.setGoodsCode(pa11stGoods.getGoodsCode());
		paSaleNoGoods.setSeqNo(systemService.getMaxNo("TPASALENOGOODS", "SEQ_NO", "GOODS_CODE = '" + pa11stGoods.getGoodsCode() + "' AND PA_CODE = '"+pa11stGoods.getPaCode()+"'", 3));
		paSaleNoGoods.setProductNo(pa11stGoods.getProductNo());
		paSaleNoGoods.setPaSaleGb("30");
		paSaleNoGoods.setInsertId("PA11");
		
		
		executedRtn = pa11stGoodsDAO.updateSoldOutTransSaleYn(pa11stGoods);
		if (executedRtn < 0) {
			log.info("tpa11stgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPA11STGOODS UPDATE" });
		}
		executedRtn = pa11stGoodsDAO.insertPaSaleNoGoods(paSaleNoGoods);
		if (executedRtn < 0) {
			log.info("tpasalenogoods insert fail");
		}
		
		return rtnMsg;
	}
	
	@Override
	public String savePa11stGoodsdtMappingQty(Pa11stGoodsVO pa11stGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;		
		
		executedRtn = pa11stGoodsDAO.updatePa11stGoodsdtMappingQty(pa11stGoods);
		if (executedRtn < 0) {
			log.info("tpa11stgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPA11STGOODS UPDATE" });
		}
		
		return rtnMsg;
	}
	@Override
	public HashMap<String,Object> procPa11stGoodsSync(ParamMap paramMap) throws Exception {

		HashMap<String,Object> resultMap = null;

		try{
			resultMap = (HashMap<String,Object>) paramMap.get();
			pa11stGoodsDAO.execSP_PAGOODS_SYNC_PROC(resultMap);
		    log.info(resultMap.get("out_code").toString());
		    log.info(resultMap.get("out_msg").toString());
		}catch(Exception e){
		    log.info(e.getMessage());
		}
		return resultMap;
	}
	
	@Override
	public List<Pa11stGoodsVO> selectPa11stGoodsSaleStopList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stGoodsSaleStopList(paramMap);
	}
	
	@Override
	public List<Pa11stGoodsVO> selectPa11stGoodsSaleRestartList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stGoodsSaleRestartList(paramMap);
	}
	@Override
	public String savePa11stGoodsFail(Pa11stGoodsVO pa11stGoods) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		//11번가에서 수신받는 메세지, 또는 paramMap.get("code"), paramMap.get("message")에서 새는 부분이있어서
		//실제 정상적으로 입점이 처리 됬음에도 반려 세팅하는 경우가 있음 한번 더 체크
		if("20".equals(pa11stGoods.getPaStatus())) {
			int cnt = pa11stGoodsDAO.selectCheckGoodsInsertStatus(pa11stGoods);
			if (cnt > 0 ) return rtnMsg;
		}
		
		executedRtn =  pa11stGoodsDAO.updatePa11stGoodsFail(pa11stGoods);
		if (executedRtn < 0) {
			log.info("tpa11stgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPA11STGOODS UPDATE" });
		}
		return rtnMsg;
	}
	@Override
	public int updateTransSaleYn(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.updateTransSaleYn(paramMap);
	}
	
	@Override
	public String selectPa11stGoodsDesc(String goodsCode) throws Exception{
		return pa11stGoodsDAO.selectPa11stGoodsDesc(goodsCode);
	}
	public int selectPa11stGoodsDescCnt998(ParamMap paramMap) throws Exception{
		return pa11stGoodsDAO.selectPa11stGoodsDescCnt998(paramMap);
	}
	public int selectPa11stGoodsDescCnt999(ParamMap paramMap) throws Exception{
		return pa11stGoodsDAO.selectPa11stGoodsDescCnt999(paramMap);
	}
	public String selectPa11stGoodsDesc998(ParamMap paramMap) throws Exception{
		String desc1 = pa11stGoodsDAO.selectPa11stGoodsDesc998FR(paramMap);
	    String desc2 = pa11stGoodsDAO.selectPa11stGoodsDesc998TO(paramMap);
	    String desc = desc1+desc2;
		return desc;
	}
	public String selectPa11stGoodsDesc999(ParamMap paramMap) throws Exception{
		String desc1 = pa11stGoodsDAO.selectPa11stGoodsDesc999FR(paramMap);
	    String desc2 = pa11stGoodsDAO.selectPa11stGoodsDesc999TO(paramMap);
	    String desc = desc1+desc2;
		return desc;
	}
	public int selectPa11stGoodsShipCnt(String goodsCode) throws Exception{
		return pa11stGoodsDAO.selectPa11stGoodsShipCnt(goodsCode);
	}
	
	public String saveStopMonitering(Pa11stGoodsVO pa11stGoods) throws Exception{
		int executedRtn = 0;
		executedRtn = pa11stGoodsDAO.insertStopMonitering(pa11stGoods);
		if (executedRtn != 1) {
			log.error("STOP TPAMONITERING INSERT ERROR");
		}
		return Constants.SAVE_SUCCESS;
	}
	public String saveRestartMonitering(Pa11stGoodsVO pa11stGoods) throws Exception{
		int executedRtn = 0;
		executedRtn = pa11stGoodsDAO.insertRestartMonitering(pa11stGoods);
		if (executedRtn != 1) {
			log.error("RESTART TPAMONITERING INSERT ERROR");
		}
		return Constants.SAVE_SUCCESS;
	}
	
	@Override
	public String saveShipCostPaGoods(Pa11stGoodsVO pa11stGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		PaSaleNoGoods paSaleNoGoods = new PaSaleNoGoods();
		paSaleNoGoods.setPaGroupCode(pa11stGoods.getPaGroupCode());
		paSaleNoGoods.setPaCode(pa11stGoods.getPaCode());
		paSaleNoGoods.setGoodsCode(pa11stGoods.getGoodsCode());
		paSaleNoGoods.setSeqNo(systemService.getMaxNo("TPASALENOGOODS", "SEQ_NO", "GOODS_CODE = '" + pa11stGoods.getGoodsCode() + "' AND PA_CODE = '"+pa11stGoods.getPaCode()+"'", 3));
		paSaleNoGoods.setProductNo(pa11stGoods.getProductNo());
		paSaleNoGoods.setPaSaleGb("30");
		paSaleNoGoods.setInsertId("PA11");
		
		executedRtn = pa11stGoodsDAO.updateShipCostTransSaleYn(pa11stGoods);
		if (executedRtn < 0) {
			log.info("tpa11stgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPA11STGOODS UPDATE" });
		}
		
		executedRtn = pa11stGoodsDAO.insertPaSaleNoGoods(paSaleNoGoods);
		if (executedRtn < 0) {
			log.info("tpasalenogoods insert fail");
			throw processException("msg.cannot_save", new String[] { "TPASALENOGOODS UPDATE" });
		}
		return rtnMsg;
	}
	
	@Override
	public List<Pa11stGoodsdtMappingVO> selectPa11stOrderMappingList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stOrderMappingList(paramMap);
	}
	@Override
	public String savePa11stGoodsdtMappingOrder(List<Pa11stGoodsdtMappingVO> paGoodsdtMapping) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		for(int i=0; i<paGoodsdtMapping.size(); i++){
			if(i == 0){
				pa11stGoodsDAO.deleteGoodsdtMappingOrder(paGoodsdtMapping.get(i));
			}
			pa11stGoodsDAO.insertGoodsdtMappingOrder(paGoodsdtMapping.get(i));
		}
		
		//TPAMONITERING UPDATE
		pa11stGoodsDAO.updateTpaMonitering4WriteInfo(paGoodsdtMapping.get(0));
		
		
		return rtnMsg;
	}
	
	@Override
	public String insertCnCostMonitering(Pa11stGoodsVO pa11stGoods) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		executedRtn = pa11stGoodsDAO.insertCnCostMonitering(pa11stGoods);
		if (executedRtn < 0) {
			log.info("tpamonitering insert fail");
			//throw processException("msg.cannot_save", new String[] { "TPAMONITERING INSERT" });
		}
		return rtnMsg;
	}
	
	@Override
	public String updateCnShipCostByMonitering(Pa11stGoodsVO pa11stGoods) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		executedRtn = pa11stGoodsDAO.updateCnShipCostByMonitering(pa11stGoods);
		if (executedRtn < 0) {
			log.error("tpacustshipcost insert fail");
			throw processException("msg.cannot_save", new String[] { "TPACUSTSHIPCOST UPDATE" });
		}
		return rtnMsg;
	}
	
	@Override
	public String updatePaGoodsModifyFail(Pa11stGoodsVO pa11stGoods) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		PaSaleNoGoods paSaleNoGoods = null;
		
		executedRtn = pa11stGoodsDAO.updatePaGoodsModifyFail(pa11stGoods);
		if (executedRtn < 0) {
			log.error("tpa11stgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPA11STGOODS UPDATE" });
		}
		
		paSaleNoGoods = new PaSaleNoGoods();
		paSaleNoGoods.setPaGroupCode(pa11stGoods.getPaGroupCode());
		paSaleNoGoods.setGoodsCode(pa11stGoods.getGoodsCode());
		paSaleNoGoods.setPaCode(pa11stGoods.getPaCode());
		paSaleNoGoods.setSeqNo(systemService.getMaxNo("TPASALENOGOODS", "SEQ_NO", "GOODS_CODE = '" + pa11stGoods.getGoodsCode() + "' AND PA_CODE = '"+pa11stGoods.getPaCode()+"'", 3));
		paSaleNoGoods.setProductNo(pa11stGoods.getProductNo());
		paSaleNoGoods.setPaSaleGb("30");
		paSaleNoGoods.setInsertId("PA11");
		paSaleNoGoods.setNote(pa11stGoods.getReturnNote());
		
		executedRtn = pa11stGoodsDAO.insertPaSaleNoGoods(paSaleNoGoods);
		if (executedRtn < 0) {
			log.error("tpasalenogoods insert fail");
			throw processException("msg.cannot_save", new String[] { "TPASALENOGOODS INSERT" });
		}
		
		pa11stGoods.setAutoYn("0");
		pa11stGoods.setModifyId("PA11");
		executedRtn = pa11stGoodsDAO.updatePaGoodsTargetForAuthYn(pa11stGoods);
		if (executedRtn < 0) {
			log.info("11st tpagoodstarget update fail for authyn");
		}
		
		PaGoodsAuthYnLog paGoodsAuthYnLog = new PaGoodsAuthYnLog();
		paGoodsAuthYnLog.setPaGroupCode(pa11stGoods.getPaGroupCode());
		paGoodsAuthYnLog.setPaCode(pa11stGoods.getPaCode());
		paGoodsAuthYnLog.setGoodsCode(pa11stGoods.getGoodsCode());
		paGoodsAuthYnLog.setSeqNo(systemService.getMaxNo("TPAGOODSAUTHYNLOG", "SEQ_NO", 
			   "GOODS_CODE = '" + pa11stGoods.getGoodsCode() + "'"+
			   " AND PA_CODE = '"+pa11stGoods.getPaCode()+"'"+
			   " AND PA_GROUP_CODE = '"+pa11stGoods.getPaGroupCode()+"'"
			   , 3));
		paGoodsAuthYnLog.setAutoYn("0");
		paGoodsAuthYnLog.setInsertId("PA11");
		paGoodsAuthYnLog.setNote("11ST 반려");
		
		executedRtn = pa11stGoodsDAO.insertPaGoodsAuthYnLog(paGoodsAuthYnLog);
		if (executedRtn < 0) {
			log.info("11st tpaGoodsAuthYnLog insert fail");
		}
		
		return rtnMsg;
	}
	
	@Override
	public List<HashMap<String,Object>> selectPaPromoTarget(ParamMap paramMap) throws Exception{
		List<HashMap<String,Object>> promotionList = new ArrayList<HashMap<String,Object>>();
		//1) 높은 가격 기준으로 프로모션을 조회한다
		paramMap.put("alcoutPromoYn", "1");
		List<HashMap<String,Object>> promotionList1 = pa11stGoodsDAO.selectPaPromoTarget(paramMap);
		
		//2) 가져온 데이터에 대한 마진률 계산 
		checkPromoMargin(promotionList1);
		
		
		//3) 프로모션 결과가 없으면 가장 최신 만료된(D) 데이터를 찾아서 반납한다 -> why? 프로모션 연동 여부를 기록하기 위해 
		if(promotionList1.size() < 1) {
			paramMap.put("alcoutPromoYn", "1");
			promotionList1	= pa11stGoodsDAO.selectPaPromoDeleteTarget(paramMap);	
		}
		
		promotionList.addAll(promotionList1);
		
		return promotionList;		
	}
	
	@Override
	public HashMap<String, String> selectGoodsFor11stPolicy(Pa11stGoodsVO pa11stGoods) throws Exception {
		HashMap<String,String> policyMap = new HashMap<String, String>();
		HashMap<String,String> exceptEntp;
		HashMap<String,String> exceptLmsd;
		
		String paCode = "";
		String entpCode = "";
		String lmsdCode = "";
		String policyType = "";
		String duration = "";
		String installYn = "";
		
		//goodsInfo를 조회
		paCode = pa11stGoods.getPaCode();
		entpCode = pa11stGoods.getEntpCode();
		lmsdCode = pa11stGoods.getLmsdCode();
		installYn = pa11stGoods.getInstallYn();
		
		exceptEntp = pa11stGoodsDAO.selectExceptEntpFor11stPolicy(entpCode);
		exceptLmsd = pa11stGoodsDAO.selectExceptCategoryFor11stPolicy(lmsdCode);
		
		/**
		 * 2019.06.19 yekim
		 * 이베이 발송예정일 정책과 동일하게 적용.
		 * SK stoa에서 상품 평균배송일 관리를 하지 않는 이유로 발송정책을 받아 사용 
		 * 해당로직은  ESM페이지에서 발송정책등록, paCode 11,12 에 해당하는 발송정책조회API로 TPAGMKTPOLICY 테이블에 policy_no가 선행처리 되어있어야함 
		 * 11번가,eBay 발송 예정 템플릿 관리는 모두 TPAGMKTPOLICY의 PA_CODE로 구분하여 사용함..
		 * 테이블 명으로 헷갈리지 말것.!
		 * 
		 * policyType : 배송방법 TCODE[O583] 
		 * duration   : 기간
		 */
		//default => 순차배송 3일
		policyType = "B";	//2019.06.19 기준 11번가는 일반 배송(B)정책 만 사용
		duration = "3";
		
        //예외업체
		if(exceptEntp != null){
			duration   = exceptEntp.get("DURATION");
		}else{
			//예외 카테고리[중분류]
			if(exceptLmsd != null){
				duration   = exceptLmsd.get("DURATION");
			}else{
				//설치배송여부 체크상품 => 7일 
				if(installYn.equals("1")){
					duration = "7";
				}
			}
		}
		
		Pa11stPolicy pa11stPolicy = new Pa11stPolicy();
		pa11stPolicy.setPaCode(paCode);
		pa11stPolicy.setPolicyType(policyType);
		pa11stPolicy.setDuration(duration);
		policyMap = pa11stGoodsDAO.selectPolicyFor11stPolicy(pa11stPolicy);
		
		if(policyMap != null){
			pa11stGoods.setPaPolicyNo(policyMap.get("POLICY_NO"));
		}
		
		return policyMap;
		
	}
	
	@Override
	public int updatePaStatus90(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.updatePaStatus90(paramMap);
	}

	public String saveTpagoodsVodUrl(Pa11stGoodsVO pickCast) throws Exception{
		int executedRtn = 0;
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		String dateTime = systemService.getSysdatetimeToString();
		Timestamp applyDate = DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss");
		
		List<?> VodUrlInsertList = pa11stGoodsDAO.selectVodUrlInsertList();
		String pickCastUrl = "";
		String goodsUrl = "";
		
		//insert
		if(VodUrlInsertList.size() > 0) {
			for(int i = 0; i < VodUrlInsertList.size(); i++) {
				try {
					HashMap<String, Object> vodUrl = (HashMap<String, Object>)VodUrlInsertList.get(i);
					
					pickCastUrl = vodUrl.get("PICKCAST_URL").toString();
					goodsUrl = vodUrl.get("GOODS_VOD_URL").toString();
					
					vodUrl.put("applyDate", applyDate);
					if(!"N".equals(pickCastUrl)) {
						vodUrl.put("VOD_DISPLAY_GB", "1");
						vodUrl.put("VOD_URL", pickCastUrl);
					} else {
						vodUrl.put("VOD_DISPLAY_GB", "2");
						vodUrl.put("VOD_URL", goodsUrl);
					}
					
					executedRtn = pa11stGoodsDAO.insertPaGoodsVodUrl(vodUrl);
					if(executedRtn < 1) {
						log.error("11번가 상품 VOD URL 저장 실패");
						throw processException("msg.cannot_save", new String[] { "TPAGOODSVODURL INSERT" });
					}
				} catch(Exception e) {
					log.error("INSERT TPAGOODSVODURL FAIL");
					continue;
				}
			}
		}
		
		//update
		//1. PICKCAST
		List<?> pickVodUrlUpdateList = pa11stGoodsDAO.selectPickVodUrlUpdateList(); //연동 중 pick과 url 다른 것 모두 update 처리
		if(pickVodUrlUpdateList.size() > 0) {
			for(int i = 0; i < pickVodUrlUpdateList.size(); i++) {
				try{
					HashMap<String, Object> pickVodUrl = (HashMap<String, Object>)pickVodUrlUpdateList.get(i);
					
					pickVodUrl.put("VOD_DISPLAY_GB", "1");
					pickVodUrl.put("applyDate", applyDate);
					
					String temp = pickVodUrl.get("NEW_VOD_URL").toString();
					if("N".equals(temp)) {
						pickVodUrl.put("VOD_URL", null);
					} else {
						pickVodUrl.put("VOD_URL", temp);
					}
					
					executedRtn = pa11stGoodsDAO.updatePaGoodsVodUrl(pickVodUrl);
					if(executedRtn < 1) {
						log.error("11번가 상품 VOD URL 저장 실패(pickcast)");
						throw processException("msg.cannot_save", new String[] { "TPAGOODSVODURL UPDATE" });
					}
					
					
				} catch(Exception e) {
					log.error("11번가 상품 VOD URL update 실패");
				}
				
			}
		}
		
		//2.GOODS VOD
		List<?> goodsVodUrlUpdateList = pa11stGoodsDAO.selectGoodsVodUrlUpdateList(); 
		if(goodsVodUrlUpdateList.size() > 0) {
			for(int i = 0; i < goodsVodUrlUpdateList.size(); i++) {
				HashMap<String, Object> goodsVodUrl = (HashMap<String, Object>)goodsVodUrlUpdateList.get(i);
				
				goodsVodUrl.put("VOD_DISPLAY_GB", "2");
				goodsVodUrl.put("applyDate", applyDate);
				
				goodsVodUrl.put("VOD_URL", goodsVodUrl.get("NEW_VOD_URL"));
				
				executedRtn = pa11stGoodsDAO.updatePaGoodsVodUrl(goodsVodUrl);
				if(executedRtn < 1) {
					log.error("11번가 상품 VOD URL 저장 실패(goods)");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSVODURL UPDATE" });
				}
				
			}
		}
		
		return rtnMsg;
	}
	
	@Override
	public List<Pa11stGoodsVO> selectPa11stVodUrlTransList() throws Exception {
		return pa11stGoodsDAO.selectPa11stVodUrlTransList();
	}
	
	@Override
	public int updatePaGoodsVodUrlTransYn(Pa11stGoodsVO pa11stGoods) throws Exception{
		return pa11stGoodsDAO.updatePaGoodsVodUrlTransYn(pa11stGoods);
	}
	
	@Override
	public int updatePaGoodsVodUrlFailMsg(ParamMap paramMap) throws Exception{
		return pa11stGoodsDAO.updatePaGoodsVodUrlFailMsg(paramMap);
	}
	
	@Override
	public int updateTransTargetYn(ParamMap paramMap) throws Exception {
		int executedRtn = 0;
		
		executedRtn = pa11stGoodsDAO.updatePaGoodsDtMappingTransYn(paramMap);
		if (executedRtn < 0) {
			log.error("tpagoodsdtmapping update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
		}
		
		executedRtn = pa11stGoodsDAO.updatePa11stGoodsTransYn(paramMap);
		if (executedRtn < 0) {
			log.error("tpa11stgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPA11STGOODS UPDATE" });
		}
		
		return executedRtn;
	}
	
	@Override
	public int updatePa11stGoodsTransTargetYn(ParamMap paramMap) throws Exception {
		int executedRtn = 0;
		
		executedRtn = pa11stGoodsDAO.updatePa11stGoodsTransYn(paramMap);
		if (executedRtn < 0) {
			log.error("tpa11stgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPA11STGOODS UPDATE" });
		}
		
		return executedRtn;
	}
	@Override
	public HashMap<String,String> selectPa11stGoodsNoticeYn(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stGoodsNoticeYn(paramMap);
	}
	/* REQ_PRM_041 11번가 제휴OUT 딜 상품등록 START */
	@Override
	public HashMap<String,Object> selectPa11stAlcoutDealInfo(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stAlcoutDealInfo(paramMap);
	}
	
	@Override
	public Pa11stGoodsVO selectPa11stAlcoutDealGoodsInfo(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stAlcoutDealGoodsInfo(paramMap);
	}
	
	@Override
	public List<HashMap<String,Object>> selectAlcoutDealGoodsList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectAlcoutDealGoodsList(paramMap);
	}
	
	@Override
	public List<HashMap<String,Object>> selectPa11stAlcoutDealGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stAlcoutDealGoodsdtInfoList(paramMap);
	}
	
	/*@Override
	public List<HashMap<String,Object>> selectPaPromoTargetGoodsList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPaPromoTargetGoodsList(paramMap);
	}*/
	
	@Override
	public String savePa11stAlcoutDeal(HashMap<String,Object> alcoutDealInfo) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			executedRtn = pa11stGoodsDAO.updatePa11stAlcoutDeal(alcoutDealInfo);
			if (executedRtn < 0) {
				log.info("TGDS_ALCOUT_DEAL_GOODS_MAPP update fail");
				throw processException("msg.cannot_save", new String[] { "TGDS_ALCOUT_DEAL_GOODS_MAPP UPDATE" });
			}
			
		} catch (Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
		return rtnMsg;
	}
	
	@Override
	public String savePa11stAlcoutDealGoodsdtMappingPaOptionCode(List<HashMap<String,Object>> alcoutDealGoodsMappingList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;		
		
		for(int i=0; i<alcoutDealGoodsMappingList.size(); i++){
			executedRtn = pa11stGoodsDAO.updatePa11stAlcoutDealGoodsdtMappingPaOptionCode(alcoutDealGoodsMappingList.get(i));
			if (executedRtn < 0) {
				log.info("TGDS_ALCOUT_DEAL_GOODS_MAPP update fail");
				throw processException("msg.cannot_save", new String[] { "TGDS_ALCOUT_DEAL_GOODS_MAPP UPDATE" });
			}
		}
		
		return rtnMsg;
	}
	
	@Override
	public String savePa11stAlcoutDealGoodsFail(HashMap<String,Object> alcoutDealInfo) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		executedRtn =  pa11stGoodsDAO.savePa11stAlcoutDealGoodsFail(alcoutDealInfo);
		if (executedRtn < 0) {
			log.info("tpa11stgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPA11STGOODS UPDATE" });
		}
		return rtnMsg;
	}
	
	@Override
	public List<HashMap<String,Object>> selectPa11stModifyAlcoutDealList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stModifyAlcoutDealList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectPa11stAlcoutDealGoodsDescribeModify(HashMap<String,Object> alcoutDealInfo) throws Exception {
		return pa11stGoodsDAO.selectPa11stAlcoutDealGoodsDescribeModify(alcoutDealInfo);
	}
	
	@Override
	public List<Pa11stGoodsVO> selectPa11stAlcoutDealVodUrlTransList() throws Exception {
		return pa11stGoodsDAO.selectPa11stAlcoutDealVodUrlTransList();
	}
	
	@Override
	public List<HashMap<String,Object>> selectPa11stNotExistsGoodsdtList(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stNotExistsGoodsdtList(paramMap);
	}
	
	@Override
	public String insertPa11stNotExistsGoodsdt(HashMap<String, Object> pa11stNotExistsGoodsdt) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		executedRtn = pa11stGoodsDAO.insertPa11stNotExistsGoodsdt(pa11stNotExistsGoodsdt);
		if (executedRtn < 0) {
			log.info("TGDS_ALCOUT_GOODS_DEAL_MAPP INSERT fail");
			throw processException("msg.cannot_save", new String[] { "TGDS_ALCOUT_GOODS_DEAL_MAPP INSERT" });
		}
		
		return rtnMsg;
	}
	
	@Override
	public List<Pa11stGoodsVO> selectPa11stAlcoutDealGoodsDescribe(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stAlcoutDealGoodsDescribe(paramMap);
	}
	
	@Override
	public int saveAlcoutDealPriceLog(List<HashMap> alcoutDealPriceLogList) throws Exception {
		return pa11stGoodsDAO.saveAlcoutDealPriceLog(alcoutDealPriceLogList);
	}
	
	private void checkPromoMargin(List<HashMap<String,Object>> promotionList1 ) throws Exception {
		
		if(promotionList1 == null) return;
		if(promotionList1.size() < 1) return;
		
		int marginCheckExceptYn     = 0;
		double margin 	  	   	    = 0;
		double limitMargin	   	    = 0;
		String sMode	   	   	    = "1";
		double outOwnCost 		 	= 0;
		ParamMap paramMap	   	    = new ParamMap();
		HashMap<String,Object> promoTarget1 	= null;
		ParamMap minMarginMap		= new ParamMap();
		
		minMarginMap.put("paGroupCode", promotionList1.get(0).get("PA_GROUP_CODE"));
		minMarginMap.put("paCode", promotionList1.get(0).get("PA_CODE"));

//		limitMargin = Double.parseDouble( ComUtil.NVL(systemDAO.getVal("PA_LIMIT_MARGIN") , "-99"));
		limitMargin  = Double.parseDouble(ComUtil.NVL(pacommonDAO.selectPaMinMarginRate(minMarginMap), "-99")); // 2022-09-20 TCONFIG -> TPAPROMOMINMARGINRATE 로 관리
		if(limitMargin == -99) return;
		
		outOwnCost  = Double.parseDouble( String.valueOf( ((HashMap<String,Object>)promotionList1.get(0)).get("DO_OWN_COST") ));
		promoTarget1 = promotionList1.get(0);
		
		//1) 마진률 CHECK
		paramMap.put("goodsCode"	, promoTarget1.get("GOODS_CODE"));
		paramMap.put("outOwnPrice"	, outOwnCost);
		paramMap.put("mode"			, sMode);
		paramMap.put("paCode"	  	, promoTarget1.get("PA_CODE"));
		paramMap.put("paGroupCode"	, "01");
		
		// 마진율 체크 제외 상품 확인
		marginCheckExceptYn = paCommonDAO.selectPromoMarginExceptYn(paramMap);
		if(marginCheckExceptYn > 0) return;
				
		margin 	 = pacommonDAO.selectGetPromoMargin(paramMap); 
		if(margin >= limitMargin) return;
		
		//2) 허용된 범위 안에서 최대 마진률 promotionList 세팅
		getMaxMarginPromo   (promotionList1,  paramMap);
		
		//4) Except_REASON Insert
		setExceptReason		(promotionList1 );
		
	}
	
	private void getMaxMarginPromo(List<HashMap<String,Object>> promotionList1, ParamMap paramMap) throws Exception {
		if(promotionList1.size() < 1)	return;
		
		String promoNo = pacommonDAO.selectGetMaxPromoTarget(paramMap);  //FUN_GET_MAX_PAPROMOTARGET
		if(promoNo == null || promoNo.equals("")) {
			promotionList1.get(0).put("DO_COST", 0);
			return;
		}
		
		paramMap.put("alcoutPromoYn", "1");
		paramMap.put("promoNo"		, promoNo);
		List<HashMap<String,Object>> promotionList = pa11stGoodsDAO.selectPaPromoTarget(paramMap);
		
		if(promotionList1.size() > 0)	promotionList1.remove(0);
		promotionList1.add(promotionList.get(0));
	}
	
	private void setExceptReason(List<HashMap<String,Object>> promotionList1) {
		ParamMap paramMap 	= new ParamMap();
		
		if(promotionList1 == null ) return;
		if(promotionList1.size() < 1) return; 
		if(Double.parseDouble( String.valueOf(promotionList1.get(0).get("DO_COST"))) < 1) return;
		
		paramMap.put("goodsCode"		, promotionList1.get(0).get("GOODS_CODE"));
		paramMap.put("paCode"			, promotionList1.get(0).get("PA_CODE"));
		paramMap.put("limitMarginAmt"	, promotionList1.get(0).get("DO_OWN_COST"));
		paramMap.put("paGroupCode"		, "");
		pacommonProcess.setExceptReason4OnePromotion(paramMap);
	}
	
	@Override
	public int selectOutDealGoodsCheck(Pa11stGoodsVO asyncPa11stGoods) throws Exception {
		return pa11stGoodsDAO.selectOutDealGoodsCheck(asyncPa11stGoods);
	}
	
	@Override
	public int updateOutDealGoodsTarget(Pa11stGoodsVO asyncPa11stGoods) throws Exception {
		return pa11stGoodsDAO.updateOutDealGoodsTarget(asyncPa11stGoods);
	}
	/* REQ_PRM_041 11번가 제휴OUT 딜 상품등록 END */

	@Override
	public int updateMassTargetYn(Pa11stGoodsVO asyncPa11stGoods) throws Exception {
		return pa11stGoodsDAO.updateMassTargetYn(asyncPa11stGoods);
	}

	@Override
	public List<Pa11stGoodsVO> selectPa11stGoodsInfoListMass(ParamMap paramMap) throws Exception {
		paramMap.put("modCase"		, "MODIFY");
		pacommonDAO.execSP_PA_MASS_GOODS_UPDATE(paramMap);
		return pa11stGoodsDAO.selectPa11stGoodsInfoList(paramMap);
	}

	@Override
	public List<PaGoodsPriceVO> selectPa11stGoodsPriceListMass(ParamMap paramMap) throws Exception {
		paramMap.put("modCase"		, "PRICE");
		pacommonDAO.execSP_PA_MASS_GOODS_UPDATE(paramMap);
		return pa11stGoodsDAO.selectPa11stGoodsPriceList(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaGoodsTrans(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPaGoodsTrans(paramMap);
	}

	@Override
	public int updatePa11stGoodsFailInsert(HashMap<String, String> paGoods) throws Exception {
		return pa11stGoodsDAO.updatePa11stGoodsFailInsert(paGoods);
	}

	@Override
	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) throws Exception {
		return pa11stGoodsDAO.updateMassTargetYnByEpCode(massMap);
	}

	@Override
	public String savePa11stGoodsdtTargetTx(String goodsCode) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		executedRtn = pa11stGoodsDAO.updatePa11stGoodsdtTarget(goodsCode);
		if (executedRtn < 0) {
			log.info("tpagoodsdt update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
		}
		return rtnMsg;
	}

	@Override
	public List<HashMap<String, Object>> selectPa11stModifyAlcoutDealTarget(ParamMap paramMap) throws Exception {
		return pa11stGoodsDAO.selectPa11stModifyAlcoutDealTarget(paramMap);
	}

	@Override
	public int insertAlcoutDealPriceLog(HashMap<String, Object> logMap) throws Exception {
		return pa11stGoodsDAO.insertAlcoutDealPriceLog(logMap);
	}

	@Override
	public String selectMaxRetentionSeq(String goodsCode, String paCode) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("goodsCode"	, goodsCode);
		paramMap.put("paCode"		, paCode);
		paramMap.put("paGroupCode"	, "01");
		return pa11stGoodsDAO.selectMaxRetentionSeq(paramMap);
	}
	
	@Override
	public List<HashMap<String,Object>> selectPaGoodsPriceApply(ParamMap paramMap) throws Exception{

		List<HashMap<String,Object>> promotionList = pa11stGoodsDAO.selectPaGoodsPriceApply(paramMap);
		
		return promotionList;
	}
}

