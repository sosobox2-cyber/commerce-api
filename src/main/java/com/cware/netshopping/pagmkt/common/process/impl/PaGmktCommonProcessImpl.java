package com.cware.netshopping.pagmkt.common.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaEsmGoodsKinds;
import com.cware.netshopping.domain.model.PaGmktOrigin;
import com.cware.netshopping.domain.model.PaGmktPolicy;
import com.cware.netshopping.domain.model.PaGmktSettlement;
import com.cware.netshopping.domain.model.PaGmktShipCostDt;
import com.cware.netshopping.domain.model.PaGmktShipCostM;
import com.cware.netshopping.domain.model.PaMaker;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaSiteGoodsKinds;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pagmkt.common.process.PaGmktCommonProcess;
import com.cware.netshopping.pagmkt.common.repository.PaGmktCommonDAO;

@Service("pagmkt.common.paGmktCommonProcess")
public class PaGmktCommonProcessImpl extends AbstractService implements PaGmktCommonProcess {

	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
    
    @Resource(name = "pagmkt.common.paGmktCommonDAO")
	private PaGmktCommonDAO paGmktCommonDAO;
    
    @Resource(name = "common.system.systemService")
    private SystemService systemService;
    
	@Override
	public String savePaGmktGoodsKindsEsm(List<PaEsmGoodsKinds> paEsmGoodsKindsList) throws Exception {
        String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	
    	try {
    		executedRtn = paGmktCommonDAO.deletePaEsmGoodsKindsList();
    		
    		if(executedRtn < 0){
    			log.info("tpagmktesmlist delete fail");
    			throw processException("msg.cannot_save", new String[] { "TPAESMGOODSKINDS DELETE" });
    		}
    		
    		for(PaEsmGoodsKinds paEsmGoodsKinds:paEsmGoodsKindsList){
    			executedRtn = paGmktCommonDAO.insertPaEsmGoodsKindsList(paEsmGoodsKinds);
    			
    			if (executedRtn < 0) {
					log.info("tpagmktesmlist insert fail");
					throw processException("msg.cannot_save", new String[] { "TPAESMGOODSKINDS INSERT" });
    			}
    		}
    		
    		executedRtn = paGmktCommonDAO.updatePaEsmGoodsKindsListGroupName(); 
    	    
			if (executedRtn < 0) {
				log.info("tpagmktlmsdlist updateUseYn fail");
				throw processException("msg.cannot_save", new String[] { "TPAESMGOODSKINDS UPDATE GROUPNAME" });
			}

    	} catch (Exception e) {
    		rtnMsg = e.getMessage();
    		throw e;
    	}
    	
    	return rtnMsg;
	    
	}
	
	@Override
	public String savePaGmktGoodsKindsSite(List<PaSiteGoodsKinds> paSiteGoodsKindsList) throws Exception {
        String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	int cnt = 0;
    	
    	try {
    		for(PaSiteGoodsKinds paSiteGoodsKinds:paSiteGoodsKindsList){
    			
    			cnt = paGmktCommonDAO.selectChkPaSitGoodsKinds(paSiteGoodsKinds);
    			
    			if (cnt < 1) {
	    			executedRtn = paGmktCommonDAO.insertPaSiteGoodsKindsList(paSiteGoodsKinds);
	    			
	    			if (executedRtn < 0) {
						log.info("tpagmktsitelist insert fail");
						throw processException("msg.cannot_save", new String[] { "TPASITEGOODSKINDS INSERT" });
	    			}
    			}
    		}

    	} catch (Exception e) {
    		rtnMsg = e.getMessage();
    		throw e;
    	}
    	
    	return rtnMsg;
	    
	}

	@Override
	public List<HashMap<String, String>> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception {
	    return paGmktCommonDAO.selectEntpShipInsertList(paEntpSlip);
	}
	@Override
	public List<HashMap<String, String>> selectEntpShipModifyList(PaEntpSlip paEntpSlip) throws Exception {
	    return paGmktCommonDAO.selectEntpShipModifyList(paEntpSlip);
	}
	@Override
	public List<HashMap<String, String>> selectEntpShipCostInsertList(PaEntpSlip paEntpSlip) throws Exception {
	    return paGmktCommonDAO.selectEntpShipCostInsertList(paEntpSlip);
	}
	@Override
	public List<HashMap<String, String>> selectEntpShipCostModifyList(PaEntpSlip paEntpSlip) throws Exception {
	    return paGmktCommonDAO.selectEntpShipCostModifyList(paEntpSlip);
	}
	@Override
	public List<HashMap<String, String>> selectEntpShipPoliciesInsertList(String gmktShipNo) throws Exception {
	    return paGmktCommonDAO.selectEntpShipPoliciesInsertList(gmktShipNo);
	}

	@Override
	public String updateEntpShipPoliciesInsert(HashMap<String, Object> paramMap) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	
    	executedRtn = paGmktCommonDAO.updateEntpShipPoliciesInsert(paramMap);
    	if(executedRtn < 0){
			log.error("tpagmktshipcostdt update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTDT UPDATE" });
		}
		return rtnMsg;
	}

	
	@Override
	public List<HashMap<String, String>> selectEntpShipPoliciesModifyList(String gmktShipNo) throws Exception {
	    return paGmktCommonDAO.selectEntpShipPoliciesModifyList(gmktShipNo);
	}
	@Override
	public String updateEntpShipPoliciesModify(HashMap<String, Object> paramMap) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	
    	executedRtn = paGmktCommonDAO.updateEntpShipPoliciesModify(paramMap);
    	if(executedRtn < 0){
			log.error("tpagmktshipcostdt update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTDT UPDATE" });
		}
		return rtnMsg;
	}
	
	/** G마켓 발송정책 조회 */
	@Override
	public String savePaGmktPolicy(List<PaGmktPolicy> policies) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	
    	try {
    		executedRtn = paGmktCommonDAO.deletePaGmktPolicy(policies.get(0));
    		
    		if(executedRtn < 0){
    			log.error("TPAGMKTPOLICY delete fail");
    			throw processException("msg.cannot_save", new String[] { "TPAGMKTPOLICY DELETE" });
    		}
    		
    		for(PaGmktPolicy policy : policies){
    			executedRtn = paGmktCommonDAO.insertPaGmktPolicy(policy);
    			if (executedRtn < 0) {
					log.error("TPAGMKTPOLICY insert fail");
					throw processException("msg.cannot_save", new String[] { "TPAGMKTPOLICY INSERT" });
    			}
    		}

    	} catch (Exception e) {
    		rtnMsg = e.getMessage();
    		throw e;
    	}
    	
    	return rtnMsg;
	}

	
	
	
	
	
	@Override
	public String savePaGmktEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
	    String rtnMsg = Constants.SAVE_SUCCESS;
	    int executedRtn = 0;
	    try {
		executedRtn = paGmktCommonDAO.insertPaGmktEntpSlip(paEntpSlip);
			
		if(executedRtn < 0){
			log.info("TPAENTPSLIP insert fail");
			throw processException("msg.cannot_save", new String[] { "TPAENTPSLIP INSERT" });
		}
	    } catch (Exception e) {
		rtnMsg = e.getMessage();
		throw e;
	    }
	    
	    return rtnMsg;
	}
	
	@Override
	public String savePaGmktEntpSlipUpdate(PaEntpSlip paEntpSlip)
	    throws Exception {
	    String rtnMsg = Constants.SAVE_SUCCESS;
	    int executedRtn = 0;
	    try {
		executedRtn = paGmktCommonDAO.updatePaGmktEntpSlip(paEntpSlip);
			
		if(executedRtn < 0){
		    log.info("TPAENTPSLIP update fail");
			throw processException("msg.cannot_save", new String[] { "TPAENTPSLIP UPDATE fail" });
		}
	    } catch (Exception e) {
		rtnMsg = e.getMessage();
		throw e;
	    }
	    return rtnMsg;
	}
	
	@Override
	public List<String> selectLmsdnCodeList() throws Exception {
	    return paGmktCommonDAO.selectLmsdnCodeList();
	}
	
	@Override
	public String savePaGmktGoodsKindsMatching(List<PaSiteGoodsKinds> paSiteGoodsKindsList) throws Exception {
        String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	
    	try {
    		executedRtn = paGmktCommonDAO.deletePaMatchingGoodsKindsList();
    		
    		if(executedRtn < 0){
    			log.info("tpagmktesmlist delete fail");
    			throw processException("msg.cannot_save", new String[] { "TPAESMGOODSKINDS DELETE fail" });
    		}
    		
    		for(PaSiteGoodsKinds paSiteGoodsKinds:paSiteGoodsKindsList){
    			executedRtn += paGmktCommonDAO.insertPaMatchingGoodsKindsList(paSiteGoodsKinds);
    			
    			if (executedRtn < 0) {
					log.info("tpagmktmatchinglist insert fail");
					throw processException("msg.cannot_save", new String[] { "TPAMATCHINGGOODSKINDS INSERT fail" });
    			}
    		}

    	} catch (Exception e) {
    		rtnMsg = e.getMessage();
    		throw e;
    	}
    	
    	return rtnMsg;
	    
	}

	@Override
	public String savePaGmktShipCostMInsert(PaGmktShipCostM paGmktShipCostM) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	int executedRtn500 = 0;
    	
    	boolean reShippingPlacesRequestFlag = false;
    	
    	//업체 + 출하지 기준  500에러 발생 여부 확인(max :GMKT_SHIP_SEQ) 
    	HashMap<String, String> oldShipCostM = paGmktCommonDAO.selectPaGmktShipCostMFor500(paGmktShipCostM);
    	if(oldShipCostM != null){
    		reShippingPlacesRequestFlag = true;
    	}
    	
		executedRtn = paGmktCommonDAO.updatePagmktshipcostmForInsert(paGmktShipCostM);
		if(executedRtn < 1){
			log.error("tpagmktshipcostm update fail by 출고지등록");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTM UPDATE fail by 출고지등록" });
		}
		
		if(reShippingPlacesRequestFlag){
			
			// 출고지 등록 후,target = 1인 row의 gmktShipNo만 업데이트 쳐주는 로직 보완(500에러 발생 후 출고지 등록 시에는 target = 0인 row도   gmktShipNo update)
			executedRtn500 = paGmktCommonDAO.updatePagmktshipcostmForInsert500(paGmktShipCostM);
			log.info("updatePagmktshipcostmForInsert500 CNT : " + executedRtn500);
			
			executedRtn = paGmktCommonDAO.updatePaentpslipForInsert500(paGmktShipCostM);
			
		}else{
			executedRtn = paGmktCommonDAO.updatePaentpslipForInsert(paGmktShipCostM);
		}
		
		if(executedRtn < 1){
			log.error("tpaentpslip update fail by 출고지등록");
			throw processException("msg.cannot_save", new String[] { "TPAENTPSLIP UPDATE fail by 출고지등록" });
		}
    	
    	return rtnMsg;
	}

	@Override
	public String savePaGmktShipCostMUpdate(PaGmktShipCostM paGmktShipCostM) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	
		executedRtn = paGmktCommonDAO.updatePagmktshipcostmForUpdate(paGmktShipCostM);
		
		//같은것이 두개가 나와서 동시처리가 가능함
		if(executedRtn < 0){
			log.error("tpagmktshipcostm update fail by 출고지수정");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTM UPDATE fail by 출고지수정" });
		}
    	return rtnMsg;
	}
	
	public String savePaGmktShipCostM(PaEntpSlip paEntpSlip) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	
    	//step0 출고지가 아닌경우(반품지) 출고지테이블에 등록하지않음- 상품코드로 출고지만 조회로직 2018.11.02 
    	/*if(paEntpSlip.getPaAddrGb().equals("20")){
    		return rtnMsg;
    	}*/
    	
    	List<PaGmktShipCostM> paGmktShipCostMList = paGmktCommonDAO.selectPaGmktShipCostM(paEntpSlip);
		
    	//step1 출고지(pagmktshipcostm) 조회 후 추가
    	for( PaGmktShipCostM paGmktShipCostm : paGmktShipCostMList ){
    		//paGmktShipCostm.setGmktShipSeq(systemService.getMaxNo("TPAGMKTSHIPCOSTM", "GMKT_SHIP_SEQ", "" , 8));
    		paGmktShipCostm.setGmktShipSeq(systemService.getSequenceNo("SEQ_GMKT_SHIP_SEQ"));
    		executedRtn = paGmktCommonDAO.insertPaGmktShipCostM(paGmktShipCostm);
    		if(executedRtn < 1){
    			log.error("tpagmktshipcostm insert fail by 판매자주소 등록");
    			throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTM INSERT" });
    		}
    	}
    	//step2 출고지(pagmktshipcostm) max가져오기
    	HashMap<String,String> resMap = paGmktCommonDAO.selectMaxPaGmktShipCostM(paEntpSlip);
    	if(resMap!=null){
    		
    		//step2-1 출고지 동일 가격 개선 쿼리
    		/**
    		selectMaxTransEndDateCompare Union
			case1::신규등록 -> COUNT = 1 , GMKT_SHIP_NO = NULL
			case2::동일가격 -> COUNT = 1 , GMKT_SHIP_NO != NULL
			다른가격 -> COUNT = 2 , GMKT_SHIP_NO != NULL
			
			동일가격 (COUNT = 1 이며, GMKT_SHIP_NO IS NOT NULL) 인 경우 SKIP한다.
    		*/
    		boolean skipFlag = false; //skip = true 이면 실행하지않는다.
    		
    		List<HashMap<String,String>> compCnt = paGmktCommonDAO.selectMaxTransEndDateCompare(resMap);
    		if(compCnt.size()!=0){ //null check
    			if(String.valueOf(compCnt.get(0).get("COUNT")).equals("1")){
    				if(!(String.valueOf(compCnt.get(0).get("GMKT_SHIP_NO"))==null || String.valueOf(compCnt.get(0).get("GMKT_SHIP_NO")).equals(""))){ // null check
    					skipFlag = true; //skip
    				}
    			}
    		}
    		
    		if(!skipFlag){
    			//step3 출고지(pagmktshipcostm) max값 update 
    	    	executedRtn = paGmktCommonDAO.updateMaxPaGmktShipCostM(resMap);
    	    	if(executedRtn < 1){
    				log.error("tpagmktshipcostm update fail by 판매자주소 등록");
    				throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTM UPDATE" });
    			}
    		}
    		
	    	//step4 출고지(pagmktshipcostm) 해당업체 플래그 down
	    	executedRtn = paGmktCommonDAO.updateNotMaxPaGmktShipCostM(resMap);
	    	if(executedRtn < 1){
				log.error("tpagmktshipcostm update fail by 판매자주소 등록");
				throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTM UPDATE" });
			}
    	}
    	return rtnMsg;
	}
	public String savePaGmktShipCostDt(PaEntpSlip paEntpSlip) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	int isExistCnt = 0;
    	
    	PaGmktShipCostM paGmktShipCostM = new PaGmktShipCostM();
    	paGmktShipCostM.setEntpCode(paEntpSlip.getEntpCode());
    	paGmktShipCostM.setEntpManSeq(paEntpSlip.getEntpManSeq());
    	
    	boolean flag = false;
    	
    	//업체 + 출하지 기준  500에러 발생 여부 확인(max :GMKT_SHIP_SEQ) 
    	HashMap<String, String> oldShipCostM = paGmktCommonDAO.selectPaGmktShipCostMFor500(paGmktShipCostM);
    	if(oldShipCostM != null){
    		
    		String newGmktShipNo = paGmktCommonDAO.selectPaentpslipGmktShipNo(paEntpSlip);
    		if(newGmktShipNo == null || "".equals(newGmktShipNo)){
    			log.error("500에러 출고지 재 등록후 신규 gmktShipNo SELECT NULL. entpCode: " + paEntpSlip.getEntpCode() + " entpManSeq: " + paEntpSlip.getEntpManSeq());
				throw processException("msg.cannot_save", new String[] { "500에러 출고지 재 등록후 신규 gmktShipNo SELECT NULL." });
    		}
    		
    		if(!newGmktShipNo.equals(oldShipCostM.get("GMKT_SHIP_NO"))){
    			
    			List<PaGmktShipCostDt> list  = paGmktCommonDAO.selectPaGmktShipCostDt(paEntpSlip);
    			for(PaGmktShipCostDt data : list){
    				log.info("===================== 500에러 출고지 묶음배송 TB UPDATE =====================");
    				log.info("oldGmktShipNo : " + oldShipCostM.get("GMKT_SHIP_NO"));
    				log.info("newGmktShipNo : " + newGmktShipNo);
    				log.info("gmktShipNo : " + data.getGmktShipNo());
    				log.info("bundleNo : " + data.getBundleNo());
    				log.info("entpCode : " + data.getEntpCode());
    				log.info("entpManSeq : " + data.getEntpManSeq());
    				log.info("shipCostCode : " + data.getShipCostCode());
    				log.info("shipCostBaseAmt : " + data.getShipCostBaseAmt());
    				log.info("ordCostAmt : " + data.getOrdCostAmt());
    				log.info("====================================================================");
    			}
    			
    			paEntpSlip.setNewGmktShipNo(newGmktShipNo);
        		paEntpSlip.setOldGmktShipNo(oldShipCostM.get("GMKT_SHIP_NO"));
        		if(paGmktCommonDAO.updatePaGmktShipCostDtGmktShipNo(paEntpSlip) < 1){
        			log.error("500에러 출고지 재 등록후 gmktshipcostdt gmktShipNo update fail");
    				throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTDT UPDATE" });
        		}
        		
        		flag = true;
    		}
    	}

    	List<PaGmktShipCostDt> paGmktShipCostDtList  = paGmktCommonDAO.selectPaGmktShipCostDt(paEntpSlip);
    	for(PaGmktShipCostDt paGmktShipCostDt : paGmktShipCostDtList){
    		if(paGmktShipCostDt.getBundleNo() == null || paGmktShipCostDt.getBundleNo().equals("")){
    			isExistCnt = paGmktCommonDAO.selectChkShipDt(paGmktShipCostDt);
    			if(isExistCnt == 0){
    				executedRtn = paGmktCommonDAO.insertPaGmktShipCostDt(paGmktShipCostDt); 
    				
    				if(executedRtn < 1){
        				log.error("tpagmktshipcostdt insert fail by 묶음배송");
        				throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTDT INSERT" });
        			}
    			}else{
    				if(flag){
        				paGmktShipCostDt.setTransTargetYn("1");
        				executedRtn = paGmktCommonDAO.updatePaGmktShipCostDtFor500(paGmktShipCostDt);    		
            			if(executedRtn < 1){
            				log.error("tpagmktshipcostdt update fail by 500에러 출고지 묶음배송");
            				throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTDT UPDATE" });
            			}
        			}
    			}
    		}else{
				executedRtn = paGmktCommonDAO.updatePaGmktShipCostDt(paGmktShipCostDt);    		
    			if(executedRtn < 1){
    				log.error("tpagmktshipcostdt update fail by 묶음배송");
    				throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTDT UPDATE" });
    			}
    		}
    	}
		
    	return rtnMsg;
	}
	@Override
	public HashMap<String,String> selectBeforeInsertGoodsBaseInfo(String goodsCode) throws Exception{
		return paGmktCommonDAO.selectBeforeInsertGoodsBaseInfo(goodsCode);
	}
	@Override
	public List<HashMap<String,String>> selectBeforeInsertGoodsEntp(String goodsCode) throws Exception{
		return paGmktCommonDAO.selectBeforeInsertGoodsEntp(goodsCode);
	}
	@Override
	public List<HashMap<String,String>> selectBeforeInsertGoodsEntpModify(ParamMap paramMap) throws Exception{
		return paGmktCommonDAO.selectBeforeInsertGoodsEntpModify(paramMap);
	}
	@Override
	public List<HashMap<String,String>> selectBeforeInsertGoodsShip() throws Exception{
		return paGmktCommonDAO.selectBeforeInsertGoodsShip();
	}
	@Override
	public List<HashMap<String,String>> selectBeforeInsertGoodsShipModify() throws Exception{
		return paGmktCommonDAO.selectBeforeInsertGoodsShipModify();
	}
	@Override
	public List<HashMap<String,String>> selectBeforeInsertGoodsBundle() throws Exception{
		return paGmktCommonDAO.selectBeforeInsertGoodsBundle();
	}
	@Override
	public List<HashMap<String,String>> selectBeforeInsertGoodsBundleModify() throws Exception{
		return paGmktCommonDAO.selectBeforeInsertGoodsBundleModify();
	}
	
	@Override
	public String savePaGmktBrandList(List<PaBrand> paBrandList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	
    	for(PaBrand paBrand:paBrandList){
	    	executedRtn = paGmktCommonDAO.insertPaGmktBrandList(paBrand);
	    	if(executedRtn < 0){
				log.error("tpabrand insert fail");
				throw processException("msg.cannot_save", new String[] { "TPABRAND INSERT" });
			}
    	}
		return rtnMsg;
	}
	
	@Override
	public String savePaGmktMakerList(List<PaMaker> paMakerList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	
    	for(PaMaker paMaker:paMakerList){
	    	executedRtn = paGmktCommonDAO.insertPaGmktMakerList(paMaker);
	    	if(executedRtn < 0){
				log.error("tpamaker insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAMAKER INSERT" });
			}
    	}
		return rtnMsg;
	}
	
	@Override
	public String savePaGmktOriginList(List<PaGmktOrigin> paGmktOriginList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	
    	executedRtn = paGmktCommonDAO.deletePaGmktOriginList();
    	
    	for(PaGmktOrigin paGmktOrigin:paGmktOriginList){
	    	executedRtn = paGmktCommonDAO.insertPaGmktOriginList(paGmktOrigin);
	    	if(executedRtn < 0){
				log.error("tpagmktorigin insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAGMKTORIGIN INSERT" });
			}
    	}
		return rtnMsg;
	}
	
	@Override
	public String savePaGmktOfferCodeList(List<PaOfferCode> paOfferCodeList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	String paGroupCode = "02";
    	
    	executedRtn = paGmktCommonDAO.deletePaGmktOfferCodeList(paGroupCode);
		
		if(executedRtn < 0){
			log.info("tpaoffercode delete fail");
			throw processException("msg.cannot_save", new String[] { "TPAOFFERCODE DELETE" });
		}
    	
    	for(PaOfferCode paOfferCode:paOfferCodeList){
	    	executedRtn = paGmktCommonDAO.insertPaGmktOfferCodeList(paOfferCode);
	    	if(executedRtn < 0){
				log.error("tpaoffercode insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAOFFERCODE INSERT" });
			}
    	}
		return rtnMsg;
	}
	
	@Override
	public String savePaGmktSettleOrder(List<PaGmktSettlement> paGmktSettlementList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	int cnt;
    	
    	for(PaGmktSettlement paGmktSettlement:paGmktSettlementList){
    		cnt = paGmktCommonDAO.selectChkGmktSettlement(paGmktSettlement);
    		if (cnt == 0) {
    			executedRtn = paGmktCommonDAO.insertPaGmktSettleOrder(paGmktSettlement);
    	    	if(executedRtn < 0){
    				log.error("tpagmktsettlement insert fail");
    				throw processException("msg.cannot_save", new String[] { "TPAGMKTSETTLEMENT INSERT" });
    			}
    		}
    	}
		return rtnMsg;
	}
	
	@Override
	public String savePaGmktSettleDelivery(List<PaGmktSettlement> paGmktSettlementList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	int cnt;
    	
    	for(PaGmktSettlement paGmktSettlement:paGmktSettlementList){
    		cnt = paGmktCommonDAO.selectChkGmktShipSettlement(paGmktSettlement);
    		if (cnt == 0) {
    			executedRtn = paGmktCommonDAO.insertPaGmktSettleDelivery(paGmktSettlement);
    			if(executedRtn < 0){
    				log.error("tpagmktsettlement insert fail");
    				throw processException("msg.cannot_save", new String[] { "TPAGMKTSETTLEMENT INSERT" });
    			}
    		}
    		
    	}
		return rtnMsg;
	}
	
	@Override
	public String deletePaGmktSettle(ParamMap paramMap) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	
    	executedRtn = paGmktCommonDAO.deletePaGmktSettlementList(paramMap);
    	if(executedRtn < 0){
			log.error("tpagmktsettlement delete fail");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTSETTLEMENT DELETE" });
		}
    	
		return rtnMsg;
	}
	
	@Override
	public String updatePaGmktShipCostMUseYn(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	
    	executedRtn = paGmktCommonDAO.updatePaGmktShipCostMUseYn(paEntpSlip);
    	if(executedRtn < 0){
			log.error("updatePaGmktShipCostMUseYn fail");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTM UPDATE" });
		}
    	
		return rtnMsg;
	}
	
	@Override
	public HashMap<String, String> selectPaGmktShipCostMFor500(PaGmktShipCostM paGmktShipCostM) throws Exception {
	    return paGmktCommonDAO.selectPaGmktShipCostMFor500(paGmktShipCostM);
	}
	
	@Override
	public int updatePaGmktShipCostMErrorYn(PaGmktShipCostM paGmktShipCostM) throws Exception{
		return paGmktCommonDAO.updatePaGmktShipCostMErrorYn(paGmktShipCostM);
	}
	
	@Override
	public List<HashMap<String, String>> selectPaGmtkGoodsTargetList(PaEntpSlip paEntpSlip) throws Exception {
	    return paGmktCommonDAO.selectPaGmtkGoodsTargetList(paEntpSlip);
	}
	
	@Override
	public int updatePaGmtkGoodsTargetOn(String goodsCode) throws Exception{
		return paGmktCommonDAO.updatePaGmtkGoodsTargetOn(goodsCode);
	}

	@Override
	public int saveTPaGmktShipCostdt(HashMap<String, String> custshipcost) throws Exception {
		int executedRtn = 0;
		
		PaGmktShipCostDt pagmktShipCost = new PaGmktShipCostDt(); 
		pagmktShipCost.setEntpCode			(custshipcost.get("ENTP_CODE"));
		pagmktShipCost.setEntpManSeq		(custshipcost.get("ENTP_MAN_SEQ"));
		pagmktShipCost.setShipCostCode  	(custshipcost.get("SHIP_COST_CODE"));
		pagmktShipCost.setShipCostBaseAmt	(Double.parseDouble(String.valueOf(custshipcost.get("SHIP_COST_BASE_AMT"))));
		pagmktShipCost.setOrdCostAmt		(Double.parseDouble(String.valueOf(custshipcost.get("ORD_COST_AMT"))));
		pagmktShipCost.setTransTargetYn		("1");
		
		String paGmktShipNo	= paGmktCommonDAO.selectGmktShipNo(pagmktShipCost);
		if(paGmktShipNo == null || "".equals(paGmktShipNo)) paGmktShipNo = "9999999";
		pagmktShipCost.setGmktShipNo		(paGmktShipNo);
		
		int cnt = paGmktCommonDAO.selectChkShipDt(pagmktShipCost);
		
		if(cnt > 0) {
			executedRtn = paGmktCommonDAO.updateTPaGmktShipCostDt(pagmktShipCost);
			if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTDT UPDATE" });
		}else {
			executedRtn = paGmktCommonDAO.insertPaGmktShipCostDt(pagmktShipCost); 
			if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTDT INSERT" });
		}
		
		return executedRtn;
	}

	@Override
	public int saveGmktShipNo(HashMap<String, String> templetMap) throws Exception {
		
		int executedRtn = 0;
		PaGmktShipCostM paGmktShipCostM = new PaGmktShipCostM();
		paGmktShipCostM.setEntpCode		(templetMap.get("ENTP_CODE"));
		paGmktShipCostM.setEntpManSeq	(templetMap.get("ENTP_MAN_SEQ"));
		paGmktShipCostM.setGmktShipNo	(templetMap.get("GMKT_SHIP_NO"));
		paGmktShipCostM.setIslandCost	(Double.parseDouble(String.valueOf(templetMap.get("SEND_ISLAND_COST"))));
		paGmktShipCostM.setJejuCost		(Double.parseDouble(String.valueOf(templetMap.get("SEND_JEJU_COST"))));
		
		//UPDATE TPAGMKTSHIPCOSTDT
		executedRtn = paGmktCommonDAO.updatePaEntpSlipGmktShipNo(paGmktShipCostM); 
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAENTPSLIP.PA_GMKT_SHIP_NO UPDATE" });
		
		//UPDATE TPAENTPSLIP
		executedRtn = paGmktCommonDAO.updatePaGmktShipCostGmktShipNo(paGmktShipCostM); 
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAGMKTSHIPCOSTDT.PA_GMKT_SHIP_NO UPDATE" });

		return executedRtn;
	}

	@Override
	public int savePaShipCost(Map<String, String> requestMap) throws Exception {
		int executedRtn	  = 0;
		ParamMap paramMap = new ParamMap();
		
		paramMap.put("entpCode"		, requestMap.get("ENTP_CODE"));
		paramMap.put("entpManSeq"	, requestMap.get("ENTP_MAN_SEQ"));
		paramMap.put("shipCostCode"	, requestMap.get("SHIP_COST_CODE"));
		
		if(requestMap.get("policyNo") != null && !"".equals(requestMap.get("policyNo")) && !"null".equals(requestMap.get("policyNo")) ) {
			paramMap.put("bundleNo" 	, requestMap.get("policyNo"));
			paramMap.put("gmktShipNo" 	, requestMap.get("placeNo"));
		}
		
		//UPDATE TPAGMKTCUSTSHIPCOST
		executedRtn = paGmktCommonDAO.updatePaGmktShipCostGmktBundleNo(paramMap); 
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAGMKTCUSTSHIPCOST.BUNDLE_NO UPDATE" });
		
		//UPDATE TPACUSTSHIPCOST
		executedRtn = paGmktCommonDAO.updatePaCustShipCost(paramMap); 	
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPACUSTSHIPCOST.TRANS_TARGET_YN UPDATE(GMKT)" });
		
		return executedRtn;
	}

	@Override
	public List<HashMap<String, String>> selectNewGoodsEntpTarget(ParamMap paramMap) throws Exception {
		return paGmktCommonDAO.selectNewGoodsEntpTarget(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectEntpSlipCost(ParamMap paramMap) throws Exception {
		return paGmktCommonDAO.selectEntpSlipCost(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectEntpSlipShip4Insert(ParamMap paramMap) throws Exception {
		return paGmktCommonDAO.selectEntpSlipShip4Insert(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectEntpSlipShip4Modify(ParamMap paramMap) throws Exception {
		return paGmktCommonDAO.selectEntpSlipShip4Modify(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaGmktShipCostTargetList(ParamMap paramMap) throws Exception {
		return paGmktCommonDAO.selectPaGmktShipCostTargetList(paramMap);
	}

	@Override
	public HashMap<String, String> selectMaxShipCostFee(HashMap<String, String> templetMap) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("entpCode"		, templetMap.get("ENTP_CODE"));
		paramMap.put("entpManSeq"	, templetMap.get("ENTP_MAN_SEQ"));
		
		return paGmktCommonDAO.selectMaxShipCostFee(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaGmktShipCostDtTargetList(ParamMap paramMap) throws Exception {
		return paGmktCommonDAO.selectPaGmktShipCostDtTargetList(paramMap);
	}

	@Override
	public HashMap<String, String> selectEntpSlipCostByEntpCodeNSeq(HashMap<String, String> entpInfo) throws Exception {
		ParamMap paramMap = new ParamMap();
		//paramMap.setParamMap(entpInfo);
		paramMap.put("entpCode"		, entpInfo.get("ENTP_CODE"));
		paramMap.put("entpManSeq"	, entpInfo.get("ENTP_MAN_SEQ"));
		
		return paGmktCommonDAO.selectEntpSlipCostByEntpCodeNSeq(paramMap);
	}

	@Override
	public String selectTConfigVal(String item) throws Exception {
		return paGmktCommonDAO.selectTConfigVal(item);
	}
	
}
