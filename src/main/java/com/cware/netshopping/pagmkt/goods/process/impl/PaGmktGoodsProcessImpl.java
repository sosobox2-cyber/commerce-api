package com.cware.netshopping.pagmkt.goods.process.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.PaGmktGoodsOfferVO;
import com.cware.netshopping.domain.PaGmktGoodsPriceVO;
import com.cware.netshopping.domain.PaGmktGoodsVO;
import com.cware.netshopping.domain.model.PaGmktDelGoodsHis;
import com.cware.netshopping.domain.model.PaGmktGoods;
import com.cware.netshopping.domain.model.PaGmktPolicy;
import com.cware.netshopping.domain.model.PaGoodsAuthYnLog;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.PaSaleNoGoods;
import com.cware.netshopping.pacommon.common.process.PaCommonProcess;
import com.cware.netshopping.pacommon.common.process.impl.PaCommonProcessImpl;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pagmkt.goods.process.PaGmktGoodsProcess;
import com.cware.netshopping.pagmkt.goods.repository.PaGmktGoodsDAO;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;
import com.cware.netshopping.pagmkt.util.rest.PaGmktGoodsRest;

@Service("pagmkt.goods.paGmktGoodsProcess")
public class PaGmktGoodsProcessImpl extends AbstractService implements PaGmktGoodsProcess {
	
	@Resource(name = "pacommon.common.pacommonProcess")
    private PaCommonProcess pacommonProcess;
	
	@Resource(name = "pacommon.common.pacommonProcess")
    private PaCommonProcessImpl paCommonProcessImpl;
	
	@Resource(name = "pagmkt.goods.paGmktGoodsDAO")
	private PaGmktGoodsDAO paGmktGoodsDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pacommon.common.paCommonDAO")
    private PaCommonDAO pacommonDAO;	
   
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsModifyList(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsDAO.selectGmktGoodsModifyList(paramMap);
	}
	@Override
	public List<HashMap<String, String>> selectGmktGoodsInsertList(HashMap<String,Object> paramMap) throws Exception {
		return paGmktGoodsDAO.selectGmktGoodsInsertList(paramMap);
	}
	@Override
	public int selectChkShipCost(String goodsCode) throws Exception {
		return paGmktGoodsDAO.selectChkShipCost(goodsCode);
	}
	@Override
	public HashMap<String,Object> selectPaEbayCheckGoods(String goodsCode) throws Exception{
		return paGmktGoodsDAO.selectPaEbayCheckGoods(goodsCode);
	}
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsPriceSaleModifyList(HashMap<String, Object> paramMap) throws Exception {
        return paGmktGoodsDAO.selectGmktGoodsPriceSaleModifyList(paramMap);
	}
	/*@Override
	public String savePaGmktGoodsModify(PaGmktGoodsVO paGmktGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    int executedRtn = 0;
 	    //UPDATE ITEM_NO 
	 	*//**
	 	API 수정 이후 PA_STATUS가 30으로 변경되는시점...,ESM_GOODS_CODE UPDATE
	 	상품수정 후 tpagoodstarget에 update하는 로직이 존재…. 
		PAE:::: G or A -> GA로 수정시 비어있는지 체크 후 신규값에 target update
	 	default:: G -> G , A -> A 는 수정시 target update를 진행하지 않음
	 	*//*
 	    
 	    if(paGmktGoods.getModifyId().equals("PAE")){
 	    	//2개 항목이 들어올 경우 esmGoodsCode를 업데이트해준다
 	    	executedRtn = paGmktGoodsDAO.updateEsmGoodsCode(paGmktGoods);
 	    	if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "updateEsmGoodsCode" });
			}
 	    	
 	    	paGmktGoods.setPaGroupCode("02");
			//NULL인 항목의 카운트
			executedRtn = paGmktGoodsDAO.selectSiteGoodsNoCheck(paGmktGoods);
			if(executedRtn == 1){//업데이트할 대상이 있는경우 ..., 그것이 02(지마켓) 일경우 :: PAA -> PAE
 				executedRtn = paGmktGoodsDAO.updateSiteGoodsNoTarget(paGmktGoods);
 				if (executedRtn < 1) {
 					throw processException("msg.cannot_save", new String[] { "updateSiteGoodsNoTarget" });
 				}
 				//UPDATE PA_GOODS_CODE
 				executedRtn = paGmktGoodsDAO.updatePaGoodsTarget(paGmktGoods);
 				if (executedRtn < 1) {
 					throw processException("msg.cannot_save", new String[] { "updatePaGoodsTarget" });
 				}
 				//UPDATE PA_STATUS updatePaGmktGoodsStatus
 				executedRtn = paGmktGoodsDAO.updatePaGmktGoodsPaStatus(paGmktGoods);
 				if (executedRtn < 1) {
 					throw processException("msg.cannot_save", new String[] { "updatePaGmktGoodsStatus" });
 				}
			}
 	    	
 	    	paGmktGoods.setPaGroupCode("03");
 	    	//NULL인 항목의 카운트
			executedRtn = paGmktGoodsDAO.selectSiteGoodsNoCheck(paGmktGoods);
			if(executedRtn == 1){//업데이트할 대상이 있는경우 ..., 그것이 03(옥션) 일경우 :: PAG -> PAE
				executedRtn = paGmktGoodsDAO.updateSiteGoodsNoExtraTarget(paGmktGoods);
 				if (executedRtn < 1) {
 					throw processException("msg.cannot_save", new String[] { "updateSiteGoodsNoExtraTarget" });
 				}
 				//UPDATE PA_GOODS_CODE
 				executedRtn = paGmktGoodsDAO.updatePaGoodsExtraTarget(paGmktGoods);
 				if (executedRtn < 1) {
 					throw processException("msg.cannot_save", new String[] { "updatePaGoodsExtraTarget" });
 				}//UPDATE PA_STATUS updatePaGmktGoodsStatus
 				executedRtn = paGmktGoodsDAO.updatePaGmktGoodsPaStatus(paGmktGoods);
 				if (executedRtn < 1) {
 					throw processException("msg.cannot_save", new String[] { "updatePaGmktGoodsStatus" });
 				}
			}
			
			
 	    }*/
	
		@Override
		public String savePaGmktGoodsModify(PaGmktGoodsVO paGmktGoods, List<PaPromoTarget> paPromoTargetList) throws Exception {
			String rtnMsg = Constants.SAVE_SUCCESS;
	 	    int executedRtn = 0;
	 	    //UPDATE ITEM_NO 
		 	/**
		 	API 수정 이후 PA_STATUS가 30으로 변경되는시점...,ESM_GOODS_CODE UPDATE
		 	상품수정 후 tpagoodstarget에 update하는 로직이 존재…. 
			PAE:::: G or A -> GA로 수정시 비어있는지 체크 후 신규값에 target update
		 	default:: G -> G , A -> A 는 수정시 target update를 진행하지 않음
		 	*/
	 	    
	 	    /* 설명    - 지마켓(이미 입점 되어있음), AUCTION(입점요청)  OR 반대케이스
	 	     *  	- 해당 상황에서는 PUTGOODS(상품수정) API를  통해 AUCTION 입점요청을 함.
	 	     *      - AUCTION은 ESM_GOODS_CODE, ITEM_NO 등등이 없어서 API결과로 들어온 값으로 UPDATE 처리*/
	 	    if(paGmktGoods.getModifyId().equals("PAE")){
	 	    	//2개 항목이 들어올 경우 esmGoodsCode를 업데이트해준다
	 	    	executedRtn = paGmktGoodsDAO.updateEsmGoodsCode(paGmktGoods);
	 	    	if (executedRtn < 0) {
					throw processException("msg.cannot_save", new String[] { "updateEsmGoodsCode" });
				}
 	    	
	 	    	paGmktGoods.setPaGroupCode("02");
				//NULL인 항목의 카운트
				executedRtn = paGmktGoodsDAO.selectSiteGoodsNoCheck(paGmktGoods);
				if(executedRtn == 1){//업데이트할 대상이 있는경우 ..., 그것이 02(지마켓) 일경우 :: PAA -> PAE
	 				executedRtn = paGmktGoodsDAO.updateSiteGoodsNoTarget(paGmktGoods);
	 				if (executedRtn < 1) {
	 					throw processException("msg.cannot_save", new String[] { "updateSiteGoodsNoTarget/ pagmktGoods" + paGmktGoods.toString() });
	 				}
	 				//UPDATE PA_GOODS_CODE
	 				executedRtn = paGmktGoodsDAO.updatePaGoodsTarget(paGmktGoods);
	 				if (executedRtn < 1) {
	 					throw processException("msg.cannot_save", new String[] { "updatePaGoodsTarget" });
	 				}
	 				//UPDATE PA_STATUS updatePaGmktGoodsStatus
	 				executedRtn = paGmktGoodsDAO.updatePaGmktGoodsPaStatus(paGmktGoods);
	 				if (executedRtn < 1) {
	 					throw processException("msg.cannot_save", new String[] { "updatePaGmktGoodsStatus" });
	 				}
				}
	 	    	
	 	    	paGmktGoods.setPaGroupCode("03");
	 	    	//NULL인 항목의 카운트
				executedRtn = paGmktGoodsDAO.selectSiteGoodsNoCheck(paGmktGoods);
				if(executedRtn == 1){//업데이트할 대상이 있는경우 ..., 그것이 03(옥션) 일경우 :: PAG -> PAE
					executedRtn = paGmktGoodsDAO.updateSiteGoodsNoExtraTarget(paGmktGoods);
	 				if (executedRtn < 1) {
	 					throw processException("msg.cannot_save", new String[] { "updateSiteGoodsNoExtraTarget/ pagmktGoods" + paGmktGoods.toString() });
	 				}
	 				//UPDATE PA_GOODS_CODE
	 				executedRtn = paGmktGoodsDAO.updatePaGoodsExtraTarget(paGmktGoods);
	 				if (executedRtn < 1) {
	 					throw processException("msg.cannot_save", new String[] { "updatePaGoodsExtraTarget" });
	 				}//UPDATE PA_STATUS updatePaGmktGoodsStatus
	 				executedRtn = paGmktGoodsDAO.updatePaGmktGoodsPaStatus(paGmktGoods);
	 				if (executedRtn < 1) {
	 					throw processException("msg.cannot_save", new String[] { "updatePaGmktGoodsStatus" });
	 				}
				}
	 	    }
	 	    
	 	    // 1이상일 경우 수정도중 MODIFY 변동 , 0일경우 변동없음
	 	    executedRtn = paGmktGoodsDAO.selectGmktGoodsModifyCheck(paGmktGoods);
	 	    //MODIFY_DATE 변동 없을경우 TARGET 0 / 아닌경우 수정대상 재포함
	 	    if ( executedRtn == 0 ) {
		 		//ESM_GOODS_CODE로 업데이트 치기때문에 이전에 ITEM_NO와 ESM_GOODS_CODE를 업데이트 해야함 2019.02.20 THJEON
		 	    executedRtn=paGmktGoodsDAO.updateGmktGoodsModify(paGmktGoods);
		 	    if (executedRtn < 0) {
					throw processException("msg.cannot_save", new String[] { "updateGmktGoodsModify" });
			    }
		 	    
		 	    //UPDATE TPAGOODSIMAGE TARGET
		 		executedRtn = paGmktGoodsDAO.updatePaGmktGoodsImage(paGmktGoods);
		 		if (executedRtn < 0) {
		 			throw processException("msg.cannot_save", new String[] { "updatePaGmktGoodsImage" });
		 	    }
		 		
		 		//UPDATE TPAGOODSOFFER TARGET
				PaGmktGoodsOfferVO paGmktGoodsOfferVO =  new PaGmktGoodsOfferVO();
				paGmktGoodsOfferVO.setGoodsCode(paGmktGoods.getGoodsCode());
				paGmktGoodsOfferVO.setPaGroupCode("02");
				executedRtn = paGmktGoodsDAO.updatePaGmktGoodsOffer(paGmktGoodsOfferVO);
				if (executedRtn < 0) {
					throw processException("msg.cannot_save", new String[] { "updatePaGmktGoodsOffer" });
			    }
			}
	 	  
	 	    PaPromoTarget paPromoTarget;
	 	    for(int i =0 ; i< paPromoTargetList.size() ; i++){
	 	    	paPromoTarget = paPromoTargetList.get(i);
	 	    	
	 	    	if(paPromoTarget.getDoCost() > 0) {
	 	 	    	executedRtn=paGmktGoodsDAO.updatePaPromoTargetCalc(paPromoTarget);
	 	 		    if (executedRtn < 0) {
	 	 				throw processException("msg.cannot_save", new String[] { "updatePaPromoTargetCalc" });
	 	 		    }
	 	    	}
	 		    
	 		    //프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
	 		    
				paGmktGoods.setDoCost(paPromoTarget.getDoCost());
				paGmktGoods.setDoOwnCost(paPromoTarget.getDoOwnCost());
				paGmktGoods.setDoEntpCost(paPromoTarget.getDoEntpCost());
				paGmktGoods.setPromoNo(paPromoTarget.getPromoNo());
				paGmktGoods.setSeq(paPromoTarget.getSeq());
				paGmktGoods.setPaGroupCode(paPromoTarget.getPaGroupCode());
				//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
	 		    if(paPromoTarget.getTransDate() == null){
	 			   //papromotarget 제휴 프로모션 테이블에 실적용 가격 및 trans_date 업데이트
				   executedRtn=paGmktGoodsDAO.updatePaPromoTarget(paGmktGoods);
				   if (executedRtn < 0) {
					   throw processException("msg.cannot_save", new String[] { "updatePaPromoTarget" });
		 			   } 			   
	 		    }else {
	 			   executedRtn=paGmktGoodsDAO.insertNewPaPromoTarget(paGmktGoods); //기존 데이터 재연동 TRANS_DATE UPDATE시 주문에 문제가 생길수 있음..그래서 새로 INSERT
		 		   if (executedRtn < 0) {
					   throw processException("msg.cannot_save", new String[] { "InsertPaPromoTarget" });
			 	   } 	
	 		    }
	 	    } 	    
	 	    
	 	    //UPDATE TPAGOODSPRICE DATE		
	 		PaGmktGoodsPriceVO paGmktGoodsPriceVO = paGmktGoodsDAO.selectPaGmktGoodsPriceOne(paGmktGoods);
	 		
			//적용되어야할 항목의 count의 갯수 (적용대상 trans_date가 null인 것의 갯수)
			int targetCnt = paGmktGoodsDAO.selectPaGmktGoodsPriceUpdateTargetCnt(paGmktGoodsPriceVO);
			if(targetCnt == 1){
				executedRtn = paGmktGoodsDAO.updatePaGmktGoodsPrice(paGmktGoodsPriceVO);
	 	 		if (executedRtn < 0) {
	 	 			throw processException("msg.cannot_save", new String[] { "updatePaGmktGoodsPrice" });
	 	 	    }
			}
	 		
	 	    /* 옵션 수정시 옵션 재등록x
	 	    executedRtn = paGmktGoodsDAO.updateGmktGoodsdtMappingforGoodsModify(paGmktGoods);
	 	    if (executedRtn < 1) {
				log.error("tpagoodsdtmapping update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
		    }*/
			
	 	    /**리텐션 처리**/
	 	    setRetention(paGmktGoods); //순서바꾸지 말것
			
			return rtnMsg;
	}
	

	@Override
	public String saveGmktGoodsPriceSaleModify(PaGmktGoods paGmktGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    int executedRtn = 0;
 	    /*
 	    executedRtn=paGmktGoodsDAO.updateGmktGoodsPrice(paGmktGoods);
 	    if (executedRtn < 0) {
			log.error("tpagoodsprice update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICE UPDATE" });
	    }
	    */
 	    executedRtn=paGmktGoodsDAO.updateGmktGoodsSaleYn(paGmktGoods);
 	    if (executedRtn < 0) {
			log.error("tpagmktgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
	    }
		return rtnMsg;
	}
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsNameModifyList(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsDAO.selectGmktGoodsNameModifyList(paramMap);
	}
	@Override
	public String saveGmktGoodsNameModify(PaGmktGoods paGmktGoods)	throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    int executedRtn = 0;
		
 	    executedRtn=paGmktGoodsDAO.updateGmktGoodsNameYn(paGmktGoods);
 	    if (executedRtn < 0) {
			log.error("tpagmktgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
	    }
		return rtnMsg;
	}
	@Override
	public List<HashMap<String,Object>> selectGoodsOptionList(HashMap<String, Object> paramMap) throws Exception{
		return paGmktGoodsDAO.selectGoodsOptionList(paramMap);
	}
	@Override
	public List<HashMap<String,Object>> selectGoodsOption(HashMap<String, Object> paramMap) throws Exception{
		return paGmktGoodsDAO.selectGoodsOption(paramMap);
	}
	
	@Override
	public List<HashMap<String,Object>> selectGoodsOptionBO(HashMap<String, Object> paramMap) throws Exception{
		return paGmktGoodsDAO.selectGoodsOptionBO(paramMap);
	}
	@Override
	public String saveGoodsOption(List<PaGoodsdtMapping> paGoodsdtMappingArr) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    int executedRtn = 0;
 	    for(PaGoodsdtMapping paGoodsdtMapping:paGoodsdtMappingArr){
 	    	executedRtn=paGmktGoodsDAO.updateGoodsOption(paGoodsdtMapping);
 	 	    if (executedRtn < 0) {
 				log.error("tpagoodsmapping update fail");
 				throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
 		    }
 	    }
		return rtnMsg;
	}
	@Override
	public String savePaGmktGoodsInsertReject(PaGmktGoods paGmktGoods) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    int executedRtn = 0;
 	    int paStatusCnt = 0;
 	    
 	    //대량 자동입점시 batch처리 오류가 발생하므로 체크
 	    paStatusCnt = paGmktGoodsDAO.selectChkPaStatus(paGmktGoods);
 	    if(paStatusCnt==1){
 	    	log.error("대량 자동입점 예외처리 check point");
 	    	return rtnMsg;
 	    }
 	    
 	    executedRtn = paGmktGoodsDAO.updatePaGmktGoodsInsertReject(paGmktGoods);
		if (executedRtn < 0) {
			log.error("tpagoodsgmktgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSGMKTGOODS UPDATE" });
	    }
 	    return rtnMsg;
	}
	@Override
	public String savePaGmktGoodsInsertSuccessFail(PaGmktGoodsVO paGmktGoods) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    int executedRtn = 0;
 	    boolean goodsEdit500Flag = false;
 	    	
		if(paGmktGoods.getReturnNote().indexOf("IF_PAGMKTAPI_V2_01_003500") > -1 || paGmktGoods.getReturnNote().indexOf("IF_PAGMKTAPI_V2_01_003502") > -1
				|| paGmktGoods.getReturnNote().indexOf("Index: 0") > -1 || paGmktGoods.getReturnNote().indexOf("동일한 배치") > -1){
			goodsEdit500Flag = true;
			
			if(paGmktGoods.getReturnNote().indexOf("IF_PAGMKTAPI_V2_01_003500") > -1) {
				executedRtn = paGmktGoodsDAO.updatePaGmktGoodsdtMappingTrans(paGmktGoods);
				if (executedRtn < 0) {
					log.error("tpagoodsdtmapping update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
				}
			}
		}else{
			// 특정메세지의 경우 판매중단 처리하지 않음 2022.05.02 LEEJY	
			if(paGmktGoods.getReturnNote().indexOf("deadlock") > -1 || paGmktGoods.getReturnNote().indexOf("제한 시간") > -1 || paGmktGoods.getReturnNote().indexOf("Connection") > -1) {				
				return rtnMsg;
			}else if(paGmktGoods.getReturnNote().indexOf("배송비를 10,000원") > -1 || paGmktGoods.getReturnNote().indexOf("판매제한 키워드에") > -1 
			|| paGmktGoods.getReturnNote().indexOf("[-10] 상품 정보 수정에 실패하였습니다.") > -1 || paGmktGoods.getReturnNote().indexOf("선택형(Independent) 항목은 20개까지") > -1
			|| paGmktGoods.getReturnNote().indexOf("상품번호가 생성 되지") > -1 || paGmktGoods.getReturnNote().indexOf("주문옵션에는 특수문자") > -1 || paGmktGoods.getReturnNote().indexOf("시퀀스에 둘 이상의") > -1
			|| paGmktGoods.getReturnNote().indexOf("G마켓에 문의 바랍니다") > -1 || paGmktGoods.getReturnNote().indexOf("최대 50byte까지") > -1 || paGmktGoods.getReturnNote().indexOf("BisnessError") > -1
			|| paGmktGoods.getReturnNote().indexOf("상품 노출이 제한") > -1 || paGmktGoods.getReturnNote().indexOf("상품 정보가 부정확합니다") > -1) {
				paGmktGoods.setPaStatus("90");	
			} else {				
				//지마켓 -> 지마켓+옥션 입점 수정 실패시.. PA_STATUS 10 OR 20 -> 20으로 해야함... 2019.02.28 thjeon
				if(!"Y".equals(paGmktGoods.getIsModifyYn())) {
					paGmktGoods.setPaStatus("20");				
				} else {
					paGmktGoods.setPaStatus("30");
				}
			}
			//executedRtn = paGmktGoodsDAO.updatePaGmktGoodsInsertReject(paGmktGoods);
			
			//판매중지처리 및 판매중지 내역 update , PA_STATUS = 30 일 경우에만 중지처리
			executedRtn = paGmktGoodsDAO.updatePaGmktGoodsInsertSuccessFail(paGmktGoods);
			if (executedRtn < 0) {
				log.error("tpagoodsgmktgoods update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSGMKTGOODS UPDATE" });
			}			
		}
		
		List<HashMap<String,Object>> paGroupCodeList = paGmktGoodsDAO.selectPaGmktGroupCode(paGmktGoods);
 	    
		for (HashMap<String,Object> paGroupCode : paGroupCodeList) {
			paGmktGoods.setPaGroupCode(paGroupCode.get("PA_GROUP_CODE").toString());
			
	 	    PaSaleNoGoods paSaleNoGoods = new PaSaleNoGoods();
			paSaleNoGoods.setPaGroupCode(paGroupCode.get("PA_GROUP_CODE").toString());
			paSaleNoGoods.setPaCode(paGmktGoods.getPaCode());
			paSaleNoGoods.setGoodsCode(paGmktGoods.getGoodsCode());
			paSaleNoGoods.setSeqNo(systemService.getMaxNo("TPASALENOGOODS", "SEQ_NO", "GOODS_CODE = '" + paGmktGoods.getGoodsCode() + "' AND PA_CODE = '" + paGmktGoods.getPaCode() + "' AND PA_GROUP_CODE = '" + paGroupCode.get("PA_GROUP_CODE").toString() +"'", 3));
			paSaleNoGoods.setPaSaleGb(paGmktGoods.getPaSaleGb());
			paSaleNoGoods.setNote(paGmktGoods.getReturnNote());
			paSaleNoGoods.setInsertId(paGroupCode.get("PA_GROUP_CODE").toString().equals("02") ? "PAG" : "PAA");
			
			//이전 seq의 paSaleGb를 체크하여 같으면 이력테이블에 넣지 않는다 2018.12.18 thjeon
			int dupChk = paGmktGoodsDAO.selectPaSaleNoGoodsSaleGb(paSaleNoGoods);
			if(dupChk == 0 && paGroupCode.get("PA_STATUS").toString().equals("30")){
				executedRtn = paGmktGoodsDAO.insertPaSaleNoGoods(paSaleNoGoods);
				if (executedRtn < 0) {
					log.info("tpasalenogoods insert fail");
				}
			}
			//500에러 아니라면 yn끄고, log insert
			if(!goodsEdit500Flag){
				
				paGmktGoods.setAutoYn("0");
				paGmktGoods.setModifyId(paGroupCode.get("PA_GROUP_CODE").toString().equals("02") ? "PAG" : "PAA");
				executedRtn = paGmktGoodsDAO.updatePaGoodsTargetForAuthYn(paGmktGoods);
				if (executedRtn < 0) {
					log.info("tpagoodstarget update fail for authyn");
				}
				
				PaGoodsAuthYnLog paGoodsAuthYnLog = new PaGoodsAuthYnLog();
				paGoodsAuthYnLog.setPaGroupCode(paGroupCode.get("PA_GROUP_CODE").toString());
				paGoodsAuthYnLog.setPaCode(paGmktGoods.getPaCode());
				paGoodsAuthYnLog.setGoodsCode(paGmktGoods.getGoodsCode());
				paGoodsAuthYnLog.setSeqNo(systemService.getMaxNo("TPAGOODSAUTHYNLOG", "SEQ_NO", 
					   "GOODS_CODE = '" + paGmktGoods.getGoodsCode() + "'"+
					   " AND PA_CODE = '"+paGmktGoods.getPaCode()+"'"+
					   " AND PA_GROUP_CODE = '"+paGroupCode.get("PA_GROUP_CODE").toString()+"'"
					   , 3));
				paGoodsAuthYnLog.setAutoYn("0");
				paGoodsAuthYnLog.setInsertId(paGroupCode.get("PA_GROUP_CODE").toString().equals("02") ? "PAG" : "PAA");
				paGoodsAuthYnLog.setNote("eBay 반려(상품수정 500에러제외)");
				
				executedRtn = paGmktGoodsDAO.insertPaGoodsAuthYnLog(paGoodsAuthYnLog);
				if (executedRtn < 0) {
					log.info("tpaGoodsAuthYnLog insert fail");
				}
			}
		}
 	    return rtnMsg;
	}
	
	
	@Override
	public List<HashMap<String,Object>> selectGmktGoodsInsertOne(ParamMap paramMap) throws Exception{
		return paGmktGoodsDAO.selectGmktGoodsInsertOne(paramMap);
	}
	
//	@Override
//	public List<HashMap<String,Object>> selectPaPromoTarget(ParamMap paramMap) throws Exception{
//		return paGmktGoodsDAO.selectPaPromoTarget(paramMap);
//	}
	@Override
	public List<HashMap<String,Object>> selectPaGmktPromoTarget(ParamMap paramMap) throws Exception{
		List<HashMap<String, Object>> promoTarget  = new ArrayList<HashMap<String,Object>>();
		String paGroupCode = "02";
		paramMap.put("paGroupCode"		, paGroupCode); //G마켓
		
		//1) 높은 가격 기준으로 프로모션을 조회한다
		paramMap.put("alcoutPromoYn"	, "1");
		List<HashMap<String, Object>> promoTarget1 = paGmktGoodsDAO.selectPaPromoTarget(paramMap);
		
		//2) 가져온 데이터에 대한 마진률 계산 
		checkPromoMargin( promoTarget1, paGroupCode);
		
		//3) 프로모션 결과가 없으면 가장 최신 만료된(D) 데이터를 찾아서 반납한다 -> why? 프로모션 연동 여부를 기록하기 위해 
		if(promoTarget1 == null || promoTarget1.size() < 1) {
			promoTarget1 = paGmktGoodsDAO.selectPaPromoDeleteTarget(paramMap);
		}
		
		promoTarget.addAll(promoTarget1);
		
		return promoTarget;
	}
	
	@Override
	public List<HashMap<String,Object>> selectPaIacPromoTarget(ParamMap paramMap) throws Exception{
		List<HashMap<String, Object>> promoTarget  = new ArrayList<HashMap<String,Object>>();
		String paGroupCode = "03";
		paramMap.put("paGroupCode"		, paGroupCode); //Auction
		
		//1) 높은 가격 기준으로 프로모션을 조회한다
		paramMap.put("alcoutPromoYn"	, "1");
		List<HashMap<String, Object>> promoTarget1 = paGmktGoodsDAO.selectPaPromoTarget(paramMap);
		
		//2) 가져온 데이터에 대한 마진률 계산 
		checkPromoMargin( promoTarget1, paGroupCode);
		
		//3) 프로모션 결과가 없으면 가장 최신 만료된(D) 데이터를 찾아서 반납한다 -> why? 프로모션 연동 여부를 기록하기 위해 
		if(promoTarget1 == null || promoTarget1.size() < 1) {
			promoTarget1 = paGmktGoodsDAO.selectPaPromoDeleteTarget(paramMap);
		}
		
		promoTarget.addAll(promoTarget1);
		
		return promoTarget;	
	}
	
	@Override
	public List<HashMap<String,Object>> selectGmktGoodsOfferInsertOne(ParamMap paramMap) throws Exception{
		return paGmktGoodsDAO.selectGmktGoodsOfferInsertOne(paramMap);
	}
	@Override
	public HashMap<String,Object> selectGmktGoodsDescribeInsertOne(ParamMap paramMap) throws Exception{
		HashMap<String,Object> describe = paGmktGoodsDAO.selectGmktGoodsDescribeInsertOne(paramMap);
		String goodsCom = "";
		
		goodsCom = (!ComUtil.NVL(describe.get("GOODS_COM")).equals("")) ? ("<div style=\"line-height: 2.0em; font-family: 'NanumBarunGothic'; font-size: 19px;\"><div><h4>&middot;&nbsp;상품구성<h4><pre>" + describe.get("GOODS_COM").toString() + "</pre></div></div>") : "";
		
		if("".equals(describe.get("COLLECT_IMAGE").toString())) {
			describe.put("DESCRIBE_EXT", "<div align='center'><img alt='' src='" + describe.get("TOP_IMAGE").toString() + "' /><br /><br /><br />" 	//상단 이미지
		  + goodsCom																																//상품 구성
		  + describe.get("DESCRIBE_EXT1").toString().replaceAll("src=\"//", "src=\"http://") 														//기술서 본문
		  + "<br /><br /><br /><img alt='' src='" + describe.get("BOTTOM_IMAGE").toString() + "' /></div>");										//하단이미지
		}else {
			describe.put("DESCRIBE_EXT", "<div align='center'><img alt='' src='" + describe.get("TOP_IMAGE").toString() + "' /><br /><br /><br /><img alt='' src='"	//상단 이미지 
		  + describe.get("COLLECT_IMAGE").toString() + "' /><br /><br /><br />"																						//착불 이미지
		  + goodsCom																																				//상품 구성
		  + describe.get("DESCRIBE_EXT1").toString().replaceAll("src=\"//", "src=\"http://") 																		//기술서 본문
		  + "<br /><br /><br /><img alt='' src='" + describe.get("BOTTOM_IMAGE").toString() + "' /></div>");														//하단 이미지
		}
		
		if(!ComUtil.NVL(describe.get("NOTICE_EXT")).equals("")) {
			describe.put("DESCRIBE_EXT", describe.get("NOTICE_EXT").toString() + describe.get("DESCRIBE_EXT"));
		}
		return describe;
	}
	@Override
	public HashMap<String,Object> selectGoodsForGmktPolicy(ParamMap paramMap) throws Exception{
		HashMap<String,Object> goodsInfoMap;
		HashMap<String,Object> policyMap;
		HashMap<String,String> exceptEntp;
		HashMap<String,String> exceptLmsd;
		
		String paCode = "";
		String entpCode = "";
		String lmsdCode = "";
		String policyType = "";
		String duration = "";
		String installYn = "";
		String paGroupCode = "";
		//goodsInfo를 조회
		goodsInfoMap = paGmktGoodsDAO.selectGoodsForGmktPolicy(paramMap);
		paCode = goodsInfoMap.get("PA_CODE").toString();
		entpCode = goodsInfoMap.get("ENTP_CODE").toString();
		lmsdCode = goodsInfoMap.get("LMSD_CODE").toString();
		installYn = goodsInfoMap.get("INSTALL_YN").toString();
		paGroupCode = "02";
		
		exceptEntp = paGmktGoodsDAO.selectExceptEntpForGmktPolicy(entpCode, paGroupCode);
		exceptLmsd = paGmktGoodsDAO.selectExceptCategoryForGmktPolicy(lmsdCode);
		
		/**
		 * 2018.12.20 thjeon
		 * SK stoa에서 상품 평균배송일 관리를 하지 않는 이유로 발송정책을 받아 사용 
		 * 해당로직은  ESM페이지에서 발송정책등록, paCode 21,22 에 해당하는 발송정책조회API로 TPAGMKTPOLICY 테이블에 policy_no가 선행처리 되어있어야함 
		 * 
		 * policyType : 배송방법 TCODE[O583] 
		 * duration   : 기간
		 */
		//default => 순차배송 3일
		policyType = "B";
		duration = "3";
		
		//예외업체
		if(exceptEntp != null){
			policyType = exceptEntp.get("POLICY_TYPE");
			duration   = exceptEntp.get("DURATION");
		}else{
			//예외 카테고리[중분류]
			if(exceptLmsd != null){
				policyType = exceptLmsd.get("POLICY_TYPE");
				duration   = exceptLmsd.get("DURATION");
			}else{
				//설치배송여부 체크상품 => 순차발송 10일 
				if(installYn.equals("1")){
					policyType = "B";
					duration = "10";
				}
			}
		}
		
		
		PaGmktPolicy paGmktPolicy = new PaGmktPolicy();
		paGmktPolicy.setPaCode(paCode);
		paGmktPolicy.setPolicyType(policyType);
		paGmktPolicy.setDuration(duration);
		paGmktPolicy.setPaGroupCode(paramMap.getString("paGroupCode"));
		policyMap = paGmktGoodsDAO.selectPolicyForGmktPolicy(paGmktPolicy);
		
		return policyMap;
	}
	
	@Override
	public String savePaGmktGoodsInsert(PaGmktGoodsVO paGmktGoods, List<PaPromoTarget> paPromoTargetList) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    int executedRtn = 0;
 	    
 	    //UPDATE ESM_GOODS_CODE / ORDER_ABLE_QTY UPDATE
 	    
 	   String itemNo = paGmktGoods.getItemNo();
 	   String itemNoExtra = paGmktGoods.getItemNoExtra();
 	   
 	   if(paGmktGoods.getModifyId().equals("PAE") && (itemNo == null || itemNoExtra == null)) {
 		    executedRtn = paGmktGoodsDAO.updatePaGmktGoodsInsert(paGmktGoods);
 			if (executedRtn < 0) {
 				log.error("tpagoodsgmktgoods update fail");
 				throw processException("msg.cannot_save", new String[] { "TPAGOODSGMKTGOODS UPDATE" });
 		    }
 			
		   paGmktGoods.setPaStatus("20");
		   executedRtn = paGmktGoodsDAO.updatePaGmktGoodsInsertReject(paGmktGoods);
		   if (executedRtn < 0) {
	      	   log.error("tpagoodsgmktgoods update fail");
			   throw processException("msg.cannot_save", new String[] { "TPAGOODSGMKTGOODS UPDATE" });
		   }
 	   } else {
 		   executedRtn = paGmktGoodsDAO.updatePaGmktGoodsInsert(paGmktGoods);
		   if (executedRtn < 0) {
			   log.error("tpagoodsgmktgoods update fail");
			   throw processException("msg.cannot_save", new String[] { "TPAGOODSGMKTGOODS UPDATE" });
		   }
 	   }
 	   
		//UPDATE ITEM_NO 
		switch(paGmktGoods.getModifyId()){
			case "PAE":
				if (itemNo != null) {
					paGmktGoods.setPaGroupCode("02");
					executedRtn = paGmktGoodsDAO.updateSiteGoodsNoTarget(paGmktGoods);
					if (executedRtn < 1) {
						log.info("tpagmktgoods update fail");
						throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
					}
					//UPDATE PA_GOODS_CODE
					executedRtn = paGmktGoodsDAO.updatePaGoodsTarget(paGmktGoods);
					if (executedRtn < 1) {
						log.info("tpagoodstarget update fail");
						throw processException("msg.cannot_save", new String[] { "TPGOODSTARGET UPDATE" });
					}
				}
				
				if (itemNoExtra != null) {
					paGmktGoods.setPaGroupCode("03");
					executedRtn = paGmktGoodsDAO.updateSiteGoodsNoExtraTarget(paGmktGoods);
					if (executedRtn < 1) {
						log.info("tpagmktgoods update fail");
						throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
					}
					//UPDATE PA_GOODS_CODE
					executedRtn = paGmktGoodsDAO.updatePaGoodsExtraTarget(paGmktGoods);
					if (executedRtn < 1) {
						log.info("tpagoodstarget update fail");
						throw processException("msg.cannot_save", new String[] { "TPGOODSTARGET UPDATE" });
					}
				}
				break;
			case "PAG":
			case "PAA":
				executedRtn = paGmktGoodsDAO.updateSiteGoodsNoTarget(paGmktGoods);
				if (executedRtn < 1) {
					log.info("tpagmktgoods update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
				}
				//UPDATE PA_GOODS_CODE
				executedRtn = paGmktGoodsDAO.updatePaGoodsTarget(paGmktGoods);
				if (executedRtn < 1) {
					log.info("tpagoodstarget update fail");
					throw processException("msg.cannot_save", new String[] { "TPGOODSTARGET UPDATE" });
				}
				break;
			
		}
		
		PaPromoTarget paPromoTarget;
 	    for(int i =0 ; i< paPromoTargetList.size() ; i++){
 	    	paPromoTarget = paPromoTargetList.get(i);
 	    	if(paPromoTarget.getDoCost() > 0 ) {
 	 	    	executedRtn=paGmktGoodsDAO.updatePaPromoTargetCalc(paPromoTarget);
 	 		    if (executedRtn < 0) {
 	 				throw processException("msg.cannot_save", new String[] { "updatePaPromoTargetCalc" });
 	 		    }
 	    	}
 		    
	    	paGmktGoods.setDoCost(paPromoTarget.getDoCost());
	    	paGmktGoods.setDoOwnCost(paPromoTarget.getDoOwnCost());
	    	paGmktGoods.setDoEntpCost(paPromoTarget.getDoEntpCost());
	    	paGmktGoods.setPromoNo(paPromoTarget.getPromoNo());
 		    paGmktGoods.setSeq(paPromoTarget.getSeq());
 		    paGmktGoods.setPaGroupCode(paPromoTarget.getPaGroupCode());

 		    //papromotarget 제휴 프로모션 테이블에 실적용 가격 및 trans_date 업데이트
 		    executedRtn = paGmktGoodsDAO.updatePaPromoTarget(paGmktGoods);
 		    if (executedRtn < 0) {
 		    	throw processException("msg.cannot_save", new String[] { "updatePaPromoTarget" });
 		    }
 	    }
		
 	    
 	    
 	    //UPDATE TPAGOODSPRICE DATE		
 		PaGmktGoodsPriceVO paGmktGoodsPriceVO = paGmktGoodsDAO.selectPaGmktGoodsPriceOne(paGmktGoods);
 		paGmktGoodsPriceVO.setTransId(paGmktGoods.getModifyId());
 		paGmktGoodsPriceVO.setInsertId(paGmktGoods.getModifyId());
 		paGmktGoodsPriceVO.setModifyId(paGmktGoods.getModifyId());
 		
 		//제휴프로모션에 데이터가 있는경우, supplyPrice가 변경되기때문에 1row insert처리, 
 		//				 데이터가 없는경우, 기존 데이터 update처리
// 		if(!paGmktGoodsPriceVO.getSupplyPrice().equals(paGmktGoodsPriceVO.getNewSupplyPrice())){
// 			//seq의 경우는 pa_code+goods_code+apply_date가 같은경우, insert될때만 + 1 
// 			paGmktGoodsPriceVO.setSupplySeq(systemService.getMaxNo("TPAGOODSPRICE", "SUPPLY_SEQ", 
// 					"GOODS_CODE = '" + paGmktGoodsPriceVO.getGoodsCode() + "'"
// 			  + " AND PA_CODE = '" + paGmktGoodsPriceVO.getPaCode() + "'"
// 			  + " AND APPLY_DATE = TO_TIMESTAMP('" + paGmktGoodsPriceVO.getApplyDate() + "','YYYY-MM-DD HH24:MI:SS.FF')", 4));
// 			paGmktGoodsDAO.insertPaGmktGoodsPrice(paGmktGoodsPriceVO);
// 			if (executedRtn < 0) {
// 	 			throw processException("msg.cannot_save", new String[] { "insertPaGmktGoodsPrice" });
// 	 	    }
// 		}else{
 			executedRtn = paGmktGoodsDAO.updatePaGmktGoodsPrice(paGmktGoodsPriceVO);
 	 		if (executedRtn < 0) {
 	 			throw processException("msg.cannot_save", new String[] { "updatePaGmktGoodsPrice" });
 	 	    }
// 		}
		
		//UPDATE TPAGOODSIMAGE TARGET
		executedRtn = paGmktGoodsDAO.updatePaGmktGoodsImage(paGmktGoods);
		if (executedRtn < 0) {
			log.error("tpagoodsimage update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSIMAGE UPDATE" });
	    }
		
		//UPDATE TPAGOODSOFFER TARGET
		PaGmktGoodsOfferVO paGmktGoodsOfferVO =  new PaGmktGoodsOfferVO();
		paGmktGoodsOfferVO.setGoodsCode(paGmktGoods.getGoodsCode());
		paGmktGoodsOfferVO.setPaGroupCode("02");
		executedRtn = paGmktGoodsDAO.updatePaGmktGoodsOffer(paGmktGoodsOfferVO);
		if (executedRtn < 0) {
			log.error("tpagoodsoffer update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSOFFER UPDATE" });
	    }
		
 	    return rtnMsg;
	}
	
	/*@Override
	public String savePaGmktGoodsInsert(PaGmktGoodsVO paGmktGoods) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    int executedRtn = 0;
 	    
 	    //UPDATE ESM_GOODS_CODE / ORDER_ABLE_QTY UPDATE
 	    
 	   String itemNo = paGmktGoods.getItemNo();
 	   String itemNoExtra = paGmktGoods.getItemNoExtra();
 	   
 	   if(paGmktGoods.getModifyId().equals("PAE") && (itemNo == null || itemNoExtra == null)) {
 		    executedRtn = paGmktGoodsDAO.updatePaGmktGoodsInsert(paGmktGoods);
 			if (executedRtn < 0) {
 				log.error("tpagoodsgmktgoods update fail");
 				throw processException("msg.cannot_save", new String[] { "TPAGOODSGMKTGOODS UPDATE" });
 		    }
 			
		   paGmktGoods.setPaStatus("20");
		   executedRtn = paGmktGoodsDAO.updatePaGmktGoodsInsertReject(paGmktGoods);
		   if (executedRtn < 0) {
	      	   log.error("tpagoodsgmktgoods update fail");
			   throw processException("msg.cannot_save", new String[] { "TPAGOODSGMKTGOODS UPDATE" });
		   }
 	   } else {
 		   executedRtn = paGmktGoodsDAO.updatePaGmktGoodsInsert(paGmktGoods);
		   if (executedRtn < 0) {
			   log.error("tpagoodsgmktgoods update fail");
			   throw processException("msg.cannot_save", new String[] { "TPAGOODSGMKTGOODS UPDATE" });
		   }
 	   }
 	   
		//UPDATE ITEM_NO 
		switch(paGmktGoods.getModifyId()){
			case "PAE":
				if (itemNo != null) {
					paGmktGoods.setPaGroupCode("02");
					executedRtn = paGmktGoodsDAO.updateSiteGoodsNoTarget(paGmktGoods);
					if (executedRtn < 1) {
						log.info("tpagmktgoods update fail");
						throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
					}
					//UPDATE PA_GOODS_CODE
					executedRtn = paGmktGoodsDAO.updatePaGoodsTarget(paGmktGoods);
					if (executedRtn < 1) {
						log.info("tpagoodstarget update fail");
						throw processException("msg.cannot_save", new String[] { "TPGOODSTARGET UPDATE" });
					}
				}
				
				if (itemNoExtra != null) {
					paGmktGoods.setPaGroupCode("03");
					executedRtn = paGmktGoodsDAO.updateSiteGoodsNoExtraTarget(paGmktGoods);
					if (executedRtn < 1) {
						log.info("tpagmktgoods update fail");
						throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
					}
					//UPDATE PA_GOODS_CODE
					executedRtn = paGmktGoodsDAO.updatePaGoodsExtraTarget(paGmktGoods);
					if (executedRtn < 1) {
						log.info("tpagoodstarget update fail");
						throw processException("msg.cannot_save", new String[] { "TPGOODSTARGET UPDATE" });
					}
				}
				break;
			case "PAG":
			case "PAA":
				executedRtn = paGmktGoodsDAO.updateSiteGoodsNoTarget(paGmktGoods);
				if (executedRtn < 1) {
					log.info("tpagmktgoods update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
				}
				//UPDATE PA_GOODS_CODE
				executedRtn = paGmktGoodsDAO.updatePaGoodsTarget(paGmktGoods);
				if (executedRtn < 1) {
					log.info("tpagoodstarget update fail");
					throw processException("msg.cannot_save", new String[] { "TPGOODSTARGET UPDATE" });
				}
				break;
			
		}
 	    
 	    //UPDATE TPAGOODSPRICE DATE		
 		PaGmktGoodsPriceVO paGmktGoodsPriceVO = paGmktGoodsDAO.selectPaGmktGoodsPriceOne(paGmktGoods);
 		
		executedRtn = paGmktGoodsDAO.updatePaGmktGoodsPrice(paGmktGoodsPriceVO);
 		if (executedRtn < 0) {
 			throw processException("msg.cannot_save", new String[] { "updatePaGmktGoodsPrice" });
 	    }
		
		//UPDATE TPAGOODSIMAGE TARGET
		executedRtn = paGmktGoodsDAO.updatePaGmktGoodsImage(paGmktGoods);
		if (executedRtn < 0) {
			log.error("tpagoodsimage update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSIMAGE UPDATE" });
	    }
		
		//UPDATE TPAGOODSOFFER TARGET
		PaGmktGoodsOfferVO paGmktGoodsOfferVO =  new PaGmktGoodsOfferVO();
		paGmktGoodsOfferVO.setGoodsCode(paGmktGoods.getGoodsCode());
		paGmktGoodsOfferVO.setPaGroupCode("02");
		executedRtn = paGmktGoodsDAO.updatePaGmktGoodsOffer(paGmktGoodsOfferVO);
		if (executedRtn < 0) {
			log.error("tpagoodsoffer update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSOFFER UPDATE" });
	    }
		
 	    return rtnMsg;
	}*/

	
    @Override
    public String insertPaGoodsTransLog(PaGoodsTransLog paGoodsTransLog)
        throws Exception {
        
	    String rtnMsg = Constants.SAVE_SUCCESS;
	    int executedRtn = 0;
	    
	    try {
		executedRtn = paCommonDAO.insertPaGoodsTransLog(paGoodsTransLog);
	if (executedRtn < 0) {
		log.info("tpagoodstranslog insert fail");
		throw processException("msg.cannot_save", new String[] { "TPAGOODSTRANSLOG INSERT" });
	}
    } catch (Exception e) {
	rtnMsg = e.getMessage();
	throw e;
    }

	    return rtnMsg;
        
    }
    
   
	@Override
	public List<HashMap<String, Object>> selectSiteGoodsNoTarget(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsDAO.selectSiteGoodsNoTarget(paramMap);
	}
	/*@Override
	public String saveSiteGoodsNoTarget(PaGmktGoods paGmktGoods) throws Exception {
	    String rtnMsg = Constants.SAVE_SUCCESS;
	    int executedRtn = 0;
	    
		executedRtn = paGmktGoodsDAO.updateSiteGoodsNoTarget(paGmktGoods);
		if (executedRtn < 1) {
			log.info("tpagmktgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
		}
		
		executedRtn = paGmktGoodsDAO.updatePaGoodsTarget(paGmktGoods);
		if (executedRtn < 1) {
			log.info("tpagoodstarget update fail");
			throw processException("msg.cannot_save", new String[] { "TPGOODSTARGET UPDATE" });
		}
	    return rtnMsg;
	}*/
	
	@Override
	public HashMap<String,Object> procPaGmktAutoInsert(ParamMap paramMap) throws Exception{
		HashMap<String,Object> resultMap = null;

		try{
			resultMap = (HashMap<String,Object>) paramMap.get();
			paGmktGoodsDAO.execSP_PA_GOODS_AUTO_INSERT1(resultMap);
		    log.info(resultMap.get("out_code").toString());
		    log.info(resultMap.get("out_msg").toString());
		}catch(Exception e){
		    log.info(e.getMessage());
		}
		return resultMap;
	}
	
	@Override
    public String selectEsmGoodsNo(ParamMap paramMap) throws Exception {
        return paGmktGoodsDAO.selectEsmGoodsNo(paramMap);
    }
	
	@Override
	public List<HashMap<String,Object>> selectGmktGoodsImageModify(ParamMap paramMap) throws Exception{
		return paGmktGoodsDAO.selectGmktGoodsImageModify(paramMap);
	}
	
	@Override
	public HashMap<String,Object> selectGmktGoodsPrice(ParamMap paramMap) throws Exception{
		return paGmktGoodsDAO.selectGmktGoodsPrice(paramMap);
	}
	
	@Override
	public String updatePaGmktGoodsImage(PaGmktGoods paGmktGoods) throws Exception {
	    String rtnMsg = Constants.SAVE_SUCCESS;
	    int executedRtn = 0;
	    
		executedRtn = paGmktGoodsDAO.updatePaGmktGoodsImage(paGmktGoods);
		if (executedRtn < 1) {
			log.info("tpagoodsprice update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICE UPDATE" });
		}
	    return rtnMsg;
	}
	
	@Override
	public String savePaGmktGoodsStatus(PaGmktDelGoodsHis paGmktDelGoodsHis) throws Exception {
	    String rtnMsg = Constants.SAVE_SUCCESS;
	    int executedRtn = 0;
	    
		executedRtn = paGmktGoodsDAO.updatePaGmktGoodsStatus(paGmktDelGoodsHis);
		if (executedRtn < 1) {
			log.info("tpagmktgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
		}
		
		executedRtn = paGmktGoodsDAO.insertPaGmktGoodsHistory(paGmktDelGoodsHis);
		if (executedRtn < 1) {
			log.info("tpagmktdelgoodshis insert fail");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTDELGOODSHIS INSERT" });
		}
	    return rtnMsg;
	}
	
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsDeleteList(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsDAO.selectGmktGoodsDeleteList(paramMap);
	}
	
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsPriceList() throws Exception {
		return paGmktGoodsDAO.selectGmktGoodsPriceList();
	}
	
	@Override
	public String savePaGmktTransDiscount(PaGmktGoods paGmktGoods) throws Exception {
	    String rtnMsg = Constants.SAVE_SUCCESS;
	    int executedRtn = 0;
	    
		executedRtn = paGmktGoodsDAO.updatePaGmktTransDiscount(paGmktGoods);
		if (executedRtn < 1) {
			log.info("tpagmktgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
		}
	    return rtnMsg;
	}
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsPriceSaleRealTimeList(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsDAO.selectGmktGoodsPriceSaleRealTimeList(paramMap);
	}
	
	@Override
	public int selectCheckDeliveryFee(ParamMap paramMap) throws Exception {
		return paGmktGoodsDAO.selectCheckDeliveryFee(paramMap);
	}
	@Override
	public String selectGmktSellerId(ParamMap paramMap) throws Exception {
		return paGmktGoodsDAO.selectGmktSellerId(paramMap);
	}
	
	@Override
	public List<HashMap<String, Object>> selectSalesDayModifyTarget() throws Exception {
		return paGmktGoodsDAO.selectSalesDayModifyTarget();
	}
	
	@Override
	public int updateGmktGoodsforSalesDayModify(ParamMap paramMap) throws Exception {
		return paGmktGoodsDAO.updateGmktGoodsforSalesDayModify(paramMap);
	}
	
	@Override
	public String selectCodeContentForTargetCnt(String apiCode) throws Exception {
		return paGmktGoodsDAO.selectCodeContentForTargetCnt(apiCode);
	}
	
	@Override
	public List<HashMap<String, Object>> selectGoodsLimitCharList(ParamMap paramMap) throws Exception {
		return paGmktGoodsDAO.selectGoodsLimitCharList(paramMap);
	}
	
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsSellingOrderList(HashMap<String, Object> paramMap) throws Exception{
		return paGmktGoodsDAO.selectGmktGoodsSellingOrderList(paramMap);
	}
	
	@Override
	public HashMap<String, Object> selectGmktGoodsSellingOrder(HashMap<String, Object> paramMap) throws Exception{
		return paGmktGoodsDAO.selectGmktGoodsSellingOrder(paramMap);
	}
	
	@Override
	public String saveGmktGoodsSellingOrder(PaGmktGoods paGmktGoods) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    int executedRtn = 0;
 	    
 	    executedRtn = paGmktGoodsDAO.updateGmktGoodsSellingOrder(paGmktGoods);
 	    if (executedRtn < 0) {
			log.error("updateGmktGoodsSellingOrder update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSSELLINGORDER UPDATE" });
	    }
		return rtnMsg;
	}
	
	@Override
	public String savePaGoodsSellingOrderInsertFail(PaGmktGoods paGmktGoods) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    int executedRtn = 0;
 	    
 	    executedRtn = paGmktGoodsDAO.updateGmktGoodsSellingOrderFail(paGmktGoods);
 	    if (executedRtn < 0) {
			log.error("updateGmktGoodsSellingOrderFail update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSSELLINGORDER UPDATE" });
	    }
		return rtnMsg;
	}
	
	@Override
	public List<HashMap<String,Object>> selectGoodsOptionListForResister(HashMap<String, Object> paramMap) throws Exception{
		return paGmktGoodsDAO.selectGoodsOptionListForResister(paramMap);
	}
	
	@Override
	public List<HashMap<String, Object>> selectG9DisplayTransList(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsDAO.selectG9DisplayTransList(paramMap);
	}
	
	@Override
	public String saveGmktG9GoodsDisplay(ParamMap paramMap)	throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    int executedRtn = 0;
		
 	    executedRtn=paGmktGoodsDAO.updateGmktG9DisplayYn(paramMap);
 	    if (executedRtn < 0) {
			log.error("tpagmktgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
	    }
		return rtnMsg;
	}

	private void checkPromoMargin(List<HashMap<String,Object>> promotionList1  , String paGroupCode) throws Exception {
		if(promotionList1 == null ) return;
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
		paramMap.put("paGroupCode"	, paGroupCode);
		
		// 마진율 체크 제외 상품 확인
		marginCheckExceptYn = paCommonDAO.selectPromoMarginExceptYn(paramMap);
		if(marginCheckExceptYn > 0) return;
				
		margin 	 = pacommonDAO.selectGetPromoMargin(paramMap); 
		if(margin >= limitMargin) return;
		
		//2) 허용된 범위 안에서 최대 마진률 promotionList 세팅
		getMaxMarginPromo   (promotionList1,  paramMap);

		//3) Except_REASON Insert
		setExceptReason		( promotionList1);
		
	}
	
	private void getMaxMarginPromo(List<HashMap<String,Object>> promotionList1, ParamMap paramMap) throws Exception {
		if(promotionList1.size() < 1) return;
		
		String promoNo = pacommonDAO.selectGetMaxPromoTarget(paramMap);  //FUN_GET_MAX_PAPROMOTARGET
		if(promoNo == null || promoNo.equals("")) {
			promotionList1.get(0).put("DO_COST", 0);
			return;
		}
		
		paramMap.put("promoNo"			, promoNo);
		paramMap.put("alcoutPromoYn"	, "1");
		List<HashMap<String, Object>> promoTarget = paGmktGoodsDAO.selectPaPromoTarget(paramMap);
		
		if(promotionList1.size() > 0)	promotionList1.remove(0);
		promotionList1.add(promoTarget.get(0));
	}
	
	private void setExceptReason(List<HashMap<String,Object>> promotionList1) {
		ParamMap paramMap 	= new ParamMap();
		
		if(promotionList1 == null) return;
		if(promotionList1.size() < 1) return;
		if(Double.parseDouble( String.valueOf(promotionList1.get(0).get("DO_COST"))) < 1) return;
				
		paramMap.put("goodsCode"		, promotionList1.get(0).get("GOODS_CODE"));
		paramMap.put("paCode"			, promotionList1.get(0).get("PA_CODE"));
		paramMap.put("limitMarginAmt"	, promotionList1.get(0).get("DO_OWN_COST"));
		paramMap.put("paGroupCode"		, promotionList1.get(0).get("PA_GROUP_CODE"));
		pacommonProcess.setExceptReason4OnePromotion(paramMap);
		return;
		
	}
	@Override
	public String updateGmktGoodsdtMappingforGoodsModify(ParamMap paramMap) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    int executedRtn = 0;
		
 	    executedRtn=paGmktGoodsDAO.updateGmktGoodsdtMappingforGoodsModify(paramMap);
 	    if (executedRtn < 0) {
			log.error("tpagmktgoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
	    }
		return rtnMsg;
	}
	
	
	public List<HashMap<String,String>> selectPagmktGoodsInfoListMass(HashMap<String,Object> paramMap) throws Exception {
		ParamMap paramMap2 = new ParamMap();
		paramMap2.setParamMap(paramMap);
		pacommonDAO.execSP_PA_MASS_GOODS_UPDATE(paramMap2);
		
		return paGmktGoodsDAO.selectMassGoodsModify(paramMap);
	}
	
	@Override
	public int updateMassTargetYn(HashMap<String, String> targetGoods) throws Exception {
		return paGmktGoodsDAO.updateMassTargetYn(targetGoods);
	}
	
	private void setRetention(PaGmktGoodsVO paGmktGoods) {
		try {
			HashMap<String, String > pagoodsInfo = paGmktGoodsDAO.selectEmsCodeItemNo(paGmktGoods);
			
			String orgEmsGoodsCode = pagoodsInfo.get("ESM_GOODS_CODE");
		    String orgAucItemNo    = pagoodsInfo.get("AUCTION_ITEM_NO");
		    String orgGmkItemNo    = pagoodsInfo.get("GMKT_ITEM_NO");
			
		    //ESM_GOODS_CODE CHANGE CHECK
		    if( ("".equals(orgEmsGoodsCode) &&  paGmktGoods.getEsmGoodsCode() !=null)  || !orgEmsGoodsCode.equals(paGmktGoods.getEsmGoodsCode())) {
		    	//UPDATE.TPAGMKTGOODS.ESM_GOODS_CODE
		    	paGmktGoodsDAO.updateEsmGoodsCode4Retention(paGmktGoods);
		    }
			
			//ItemNo Check
			switch (paGmktGoods.getModifyId()) {
			case "PAE":
				String gmkItemNo = paGmktGoods.getItemNo();			// 상품 수정 후 이베이로 부터 전달받은 SITE_GOODS_CODE
				String aucItemNo = paGmktGoods.getItemNoExtra();	//                ''
				
				if(gmkItemNo == null && aucItemNo == null) 								return; //이베이에서 결과값을 안주는 경우..
				if(orgGmkItemNo.equals(gmkItemNo) && orgAucItemNo.equals(aucItemNo))	return; //G마켓 ITEM_NO와 AUCTION ITEM_NO가 모두 바뀌지 않은 경우
				
				// UPDATE TPAGMKTGOODS.ITEM_NO , TPAGOODSDTMAPPING.PA_OPTION_CODE
				paGmktGoods.setItemNo(gmkItemNo);
				paGmktGoods.setPaGroupCode("02");
				paGmktGoodsDAO.updateSiteGoodsNoTarget4Retention(paGmktGoods);
				paGmktGoodsDAO.updatePaGoodsTarget4Retention(paGmktGoods);
				
				paGmktGoods.setItemNo(aucItemNo);
				paGmktGoods.setPaGroupCode("03");
				paGmktGoodsDAO.updateSiteGoodsNoTarget4Retention(paGmktGoods);
				paGmktGoodsDAO.updatePaGoodsTarget4Retention(paGmktGoods);
				break;
	
			case "PAG":
				if(paGmktGoods.getItemNo() == null)				 return; //이베이에서 결과값을 안주는 경우..
				if(orgGmkItemNo.equals(paGmktGoods.getItemNo())) return; //G마켓 ITEM_NO 가 바뀌지 않은 경우
				paGmktGoods.setPaGroupCode("02");
				paGmktGoodsDAO.updateSiteGoodsNoTarget4Retention(paGmktGoods);
				paGmktGoodsDAO.updatePaGoodsTarget4Retention(paGmktGoods);
			 	break;
			
			case "PAA":
				if(paGmktGoods.getItemNo() == null)				 return; //이베이에서 결과값을 안주는 경우..
				if(orgAucItemNo.equals(paGmktGoods.getItemNo())) return; //G마켓 ITEM_NO 가 바뀌지 않은 경우
				paGmktGoods.setPaGroupCode("03");
				paGmktGoodsDAO.updateSiteGoodsNoTarget4Retention(paGmktGoods);
				paGmktGoodsDAO.updatePaGoodsTarget4Retention(paGmktGoods);
				break;
				
			default:
				break;
			}
		}catch (Exception e) {
			log.error(e.toString());
		}
	}

	
	@SuppressWarnings("unchecked")
	public String getGmktSiteGb(String paCode, String goodsCode , String checkType ) throws Exception {		//실제 이베이에 판매중인 SITE_GB 값 RETURN
		String errSiteGb  		= "999"; 
		String response	  		= null;
		ParamMap paramMap 		= new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_005";       
		
		String gPaSaleFlag  = "";
		String iacSaleFlag   = "";
		//String checkType  = "SellingPeriod";  //isSell 판매상태 조회,  SellingPeriod 기간
		
		
		paramMap.put("apiCode"		, apiCode);
		paramMap.put("paCode"		, paCode);
		paramMap.put("startDate"	, systemService.getSysdatetimeToString());
		paramMap.put("goodsCode"	, goodsCode);
		paramMap.put("paCode"		, paCode);
		
		//1) SITE_GB, ESM_GOODS_CODE 값을 구함.
		String esmGoodsCode = paGmktGoodsDAO.selectGmktEmsGoodsCode(paramMap);
		if(esmGoodsCode == null || "".equals(esmGoodsCode) ) return "999";
		paramMap.put("urlParameter"	, esmGoodsCode); 
		
		//지마켓 상태조회
		paramMap.put("siteGb"		, "PAG");
		try {
			response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			//Map<String,Object> isSell = (Map<String,Object>)resMap.get("IsSell"); //{IsSell={gmkt=true, iac=false}, itemBasicInfo={Price={gmkt=25500.0, iac=0.0}, Stock={gmkt=48, iac=0}, SellingPeriod={gmkt=20311119, iac=0}}}
			//gPaSaleFlag = isSell.get("gmkt").toString(); 
			
			if(checkType.equals("isSell")) {
				Map<String,Object> isSell = (Map<String,Object>)resMap.get("IsSell"); 
				gPaSaleFlag = isSell.get("gmkt").toString(); 
			}else if( checkType.equals("SellingPeriod") ) {
				Map<String,Object> SellingPeriod = (Map<String,Object>)((Map<String,Object>)resMap.get("itemBasicInfo")).get("SellingPeriod");
				String period = SellingPeriod.get("gmkt").toString(); 
				if(!period.equals("0")) {
					gPaSaleFlag = "true";
				}else {
					gPaSaleFlag = "false";
				}
			}else return "999";
			
		}catch (Exception e) {
			gPaSaleFlag = "false";
		}
		
		//옥션 상태 조회
		paramMap.put("siteGb"		, "PAA");
		try {
			response = restUtil.getConnection(rest,  paramMap);
			
			Map<String,Object> resMap2 = ComUtil.splitJson(response);
			//Map<String,Object> isSell2 = (Map<String,Object>)resMap2.get("IsSell"); //{IsSell={gmkt=true, iac=false}, itemBasicInfo={Price={gmkt=25500.0, iac=0.0}, Stock={gmkt=48, iac=0}, SellingPeriod={gmkt=20311119, iac=0}}}
			//iacSaleFlag = isSell2.get("iac").toString(); 
			
			if(checkType.equals("isSell")) {
				Map<String,Object> isSell = (Map<String,Object>)resMap2.get("IsSell"); 
				iacSaleFlag = isSell.get("iac").toString(); 
			}else if( checkType.equals("SellingPeriod") ) {
				Map<String,Object> SellingPeriod =  (Map<String,Object>)((Map<String,Object>)resMap2.get("itemBasicInfo")).get("SellingPeriod");
				String period = SellingPeriod.get("iac").toString(); 
				if(!period.equals("0")) {
					iacSaleFlag = "true";
				}else {
					iacSaleFlag = "false";
				}
			}else return "999";
		}catch (Exception e) {
			iacSaleFlag = "false";
		}
		
		
		if("true".equals(gPaSaleFlag) && "true".equals(iacSaleFlag)) {
			return "PAE";
		}
		
		if("true".equals(gPaSaleFlag)) {
			return "PAG";
		}
		
		if("true".equals(iacSaleFlag)) {
			return "PAA";
		}
		
		return errSiteGb;
	}
	
	@Override
	public void checkRetentionGoodsModify(ParamMap paramMap) throws Exception {
		/**리텐션 정책으로 인해 ESM에 스토아 상품 정보가 삭제된 상품을 판매 재개 할때 
		지마켓이나 옥션 둘 중 하나라도 ESM에 스토아 상품 정보가 존재 중이다 -> 상품 수정 API 호출
		지파켓이나 옥션 둘다 ESM에 스토아 상품 정보가 존재하지 않음 	    -> 상품 등록 API 호출 **/
		
		/**2022-01-19 HSBAEK 지마켓 리텐션 SALE_STOP_DATE 관리가 제대로 될때 (2022-02-19일 이후)
		 * 해당 로직  살려서 테스트 후 주석 풀고 배포 필요**/
		return;
		/*
		String insertApiCode = "IF_PAGMKTAPI_V2_01_001";
		//String modifyApiCode = "IF_PAGMKTAPI_V2_01_003";
				
		if(paramMap.getString("apiCode").equals("")) 			return; //API CODE가 없으면 PASS - ERROR CASE
		if(paramMap.getString("apiCode").equals(insertApiCode))	return; //등록으로 넘어온 건 은 PASS
		
		int liveGoodsCnt = paGmktGoodsDAO.selectliveGmktGoodsCount(paramMap);
		
		if(liveGoodsCnt == 0) {
			paramMap.put("apiCode"	, insertApiCode);
			paGmktGoodsDAO.updateGmktGoodsdtMappingforGoodsModify(paramMap);
		}*/
	}
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsRetentionExtendList(ParamMap paramMap) throws Exception {
		return paGmktGoodsDAO.selectGmktGoodsRetentionExtendList(paramMap);
	}
	@Override
	public int updateGmktSaleStopDate(ParamMap paramMap) throws Exception {
		return paGmktGoodsDAO.updateGmktSaleStopDate(paramMap);
	}
	@Override
	public HashMap<String, Object> selecGoodsExpiry(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsDAO.selecGoodsExpiry(paramMap);
	}	
	@Override
	public HashMap<String, String> selecGoodsExpiryDateYn(String goodsCode) throws Exception {
		return paGmktGoodsDAO.selecGoodsExpiryDateYn(goodsCode);
	}
	@Override
	public int updateGoodsExpiryDate(ParamMap paramMap) throws Exception {
		return paGmktGoodsDAO.updateGoodsExpiryDate(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectEmsCodeItemNo(PaGmktGoodsVO paGmktGoods) throws Exception {
		return paGmktGoodsDAO.selectEmsCodeItemNo(paGmktGoods);
	}
	
	@Override
	public int selectOptionErrorChk(ParamMap paramMap) throws Exception {
		int errorCnt = 0;
		int executedRtn = 0;
		
		if (paGmktGoodsDAO.selectOptionErrorChk(paramMap) > 0) {
			executedRtn = paGmktGoodsDAO.updateReturnNote(paramMap);
			if (executedRtn < 0) {
				log.error("tpagoodsgmktgoods returnNote update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSGMKTGOODS RETURN NOTE UPDATE" });
		    }
			errorCnt = 1;
		}
		return errorCnt;
	}
	
}
