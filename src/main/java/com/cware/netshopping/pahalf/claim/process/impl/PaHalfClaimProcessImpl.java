package com.cware.netshopping.pahalf.claim.process.impl;

import java.util.HashMap;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaHalfOrderListVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pahalf.claim.process.PaHalfClaimProcess;
import com.cware.netshopping.pahalf.claim.repository.PaHalfClaimDAO;
import com.cware.netshopping.pahalf.order.repository.PaHalfOrderDAO;
import com.cware.netshopping.pahalf.util.PaHalfComUtill;

@Service("pahalf.claim.paHalfClaimProcess")
public class PaHalfClaimProcessImpl extends AbstractService implements PaHalfClaimProcess{
	
	@Resource(name = "pahalf.claim.paHalfClaimDAO")
	private PaHalfClaimDAO paHalfClaimDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Resource(name = "pahalf.order.paHalfOrderDAO")
	private PaHalfOrderDAO paHalfOrderDAO;
	
	@Autowired
    private SystemService systemService;

	@Override
	public List<Map<String, Object>> selectExchangeCompleteList() throws Exception {
		return paHalfClaimDAO.selectExchangeCompleteList();
	}

	@Override
	public void updatePaOrderm(Map<String, Object> orderMap) throws Exception {
		Map<String,Object> paOrderm = new HashMap<String, Object>();
		
		String paDoFlag = String.valueOf(orderMap.get("paDoFlag"));
		
		if(paDoFlag == null || paDoFlag == "") {
			paOrderm.put("resultCode", Constants.SAVE_FAIL);
		}else {
			paOrderm.put("resultCode", Constants.SAVE_SUCCESS);
		}
		paOrderm.put("resultMessage"	, orderMap.get("message"));
		paOrderm.put("paDoFlag"			, paDoFlag);
		paOrderm.put("mappingSeq"		, orderMap.get("mappingSeq"));
		
		paHalfOrderDAO.updateTPaorderm(paOrderm);

	}
	
	@Override
	public List<Map<String, Object>> selectReturnApprovalList() throws Exception {
		return paHalfClaimDAO.selectReturnApprovalList();
	}

	
	@Override
	public String savePaHalfClaim(Map<String, Object> claimMap) throws Exception {
		String rtnMsg		 = Constants.SAVE_SUCCESS;
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> saleDetailList = (List<Map<String, Object>>) claimMap.get("saleDetailList");
		
		for(Map<String,Object> claim : saleDetailList) {
			PaHalfOrderListVO  paHalfOrderListVO= (PaHalfOrderListVO) PaHalfComUtill.map2VO(claim, PaHalfOrderListVO.class);
			PaHalfComUtill.map2VO(claimMap, paHalfOrderListVO); 
			
			if("30".equals(paHalfOrderListVO.getPaOrderGb()) || "40".equals(paHalfOrderListVO.getPaOrderGb())) {
				savePaHalfClaim(paHalfOrderListVO);	
			}else {
				savePaHalfClaimCancel(paHalfOrderListVO);
			}
		}
		
		return rtnMsg;
	}
	
	//반품, 교환생성 TPAHALFORDERLIST, TPAORDERM INSERT
	private String savePaHalfClaim(PaHalfOrderListVO paHalfOrderListVO) throws Exception {
		
		int executedRtn         = 0;
		String rtnMsg		    = Constants.SAVE_SUCCESS;
		String dateTime 	    = systemService.getSysdatetimeToString();
		Timestamp systemTime    = DateUtil.toTimestamp(dateTime);
		boolean isExistReturn   = true;
		boolean isExistExchange = true;
		
		
		//=기본정보 세팅
		paHalfOrderListVO.setModifyId			(Constants.PA_HALF_PROC_ID);
		paHalfOrderListVO.setModifyDate			(systemTime);
		paHalfOrderListVO.setInsertDate			(systemTime);
		

		//ClaimRequestDate 및 ClaimNo Setting
		if("30".equals(paHalfOrderListVO.getPaOrderGb())) {
			paHalfOrderListVO.setClaimRequestDate	(paHalfOrderListVO.getRefundDate() );
			paHalfOrderListVO.setClaimNo			(paHalfOrderListVO.getRefundNo());
		}else {
			paHalfOrderListVO.setClaimRequestDate	(paHalfOrderListVO.getExchangeCreateDt() );
			paHalfOrderListVO.setClaimNo			(ComUtil.increaseLpad( ComUtil.NVL( paHalfClaimDAO.selectClaimNo(paHalfOrderListVO) , "000"), 3 ,"0" ));
		}
		
		//CASE : 교환 -> 교환처리 -> 반품 인입 CASE 
		//교환 출고때 신규 주문번호 채번, 해당 신규 주문번호로 반품 인입
		if(paHalfOrderListVO.getOriOrdNo() != null && !"".equals(paHalfOrderListVO.getOriOrdNo()) 
				&& "30".equals(paHalfOrderListVO.getPaOrderGb()) ) {
        	String temp = paHalfOrderListVO.getOrdNo();
            paHalfOrderListVO.setOrdNo(paHalfOrderListVO.getOriOrdNo());
            paHalfOrderListVO.setOriOrdNo(temp);
		}
		
		//=중복 확인
		int exists	=  paHalfOrderDAO.selectCountClaimList(paHalfOrderListVO);
		if(exists > 0) {
			paHalfClaimDAO.mergeExchangeOrderNm(paHalfOrderListVO); //추후에 부하가 걸리면 SELECT~ UPDATE 문으로 수정하는 방향으로..
			checkAutoCompleteReturn(paHalfOrderListVO); //자동 반품완료 체크  -> 반품접수후 N일동안 반품처리 하지 않으면 자동 반품완료, 교환은 확인 필요..
			return "";
		}
		
		
		//= 진행중인 교환,반품 종료시킴
		isExistReturn   =  checkExistsReturn(paHalfOrderListVO); 
		isExistExchange = checkExistsExchange(paHalfOrderListVO); 
		if(!isExistReturn || !isExistExchange) return "";
		
		
		//=INSERT TPAHALFORDERLIST
		executedRtn = paHalfOrderDAO.insertPaHalfOrderList(paHalfOrderListVO); 
		if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAHALFORDERLIST INSERT" });
		
		//=INSERT TPAORDERM
		insertPaOrdermClaim( paHalfOrderListVO , new Paorderm());
		
		return rtnMsg;
	}
	
	//자동 반품완료 처리
	private void checkAutoCompleteReturn(PaHalfOrderListVO paHalfOrderListVO) {
		try{
			if(!"30".equals(paHalfOrderListVO.getPaOrderGb())) return; //반품만 Check
			
			if("반품완료".equals(paHalfOrderListVO.getGubun2())) {
			
				Map<String,Object> returnMap = paHalfClaimDAO.selectReturnStt(paHalfOrderListVO);
				if("60".equals(returnMap.get("PA_DO_FLAG") )) return;
				
				Map<String,Object> paOrderm = new HashMap<String, Object>();
				paOrderm.put("mappingSeq"		, returnMap.get("MAPPING_SEQ"));
				paOrderm.put("paDoFlag"			, "60");
				paOrderm.put("resultCode"	, Constants.SAVE_SUCCESS);
				paOrderm.put("resultMessage"	, "자동 반품 완료");
				
				paHalfOrderDAO.updateTPaorderm(paOrderm);
			}
				
		}catch (Exception e) {
			log.info("{} : {}", "하프클럽 자동 반품완료 처리 오류",PaHalfComUtill.getErrorMessage(e));
	
		}

	}

	//Return false인경우는 이번 배치에서 반품을 만들지 않는다는 의미..
	private boolean checkExistsExchange(PaHalfOrderListVO paHalfOrderListVO) throws Exception { 
		PaHalfOrderListVO exchangeCancelListVO = null;
		String dateTime 	 				   = systemService.getSysdatetimeToString();
		Timestamp systemTime				   = DateUtil.toTimestamp(dateTime);
		int cnt = 0;

		//1) 현재 진행중인 교환 체크
		cnt = paHalfOrderDAO.selectExistsExchange(paHalfOrderListVO);
		
		if(cnt < 1 ) return true;
		
		//3)INSERT TPAORDERM, TPAHALFORDERLIST
		exchangeCancelListVO = new PaHalfOrderListVO();
		exchangeCancelListVO.setPaOrderGb		 ("41");
		exchangeCancelListVO.setOrdNo			 (paHalfOrderListVO.getOrdNo());
		exchangeCancelListVO.setOrdNoNm			 (paHalfOrderListVO.getOrdNoNm());
		exchangeCancelListVO.setPaCode			 (paHalfOrderListVO.getPaCode());
		exchangeCancelListVO.setClaimNo			 (paHalfOrderListVO.getClaimNo());
		exchangeCancelListVO.setBasketNo		 (paHalfOrderListVO.getBasketNo());
		exchangeCancelListVO.setQty				 (paHalfOrderListVO.getQty());
		//exchangeCancelListVO.setGubun2			 (paHalfOrderListVO.getGubun2());
		exchangeCancelListVO.setPrdNo			 (paHalfOrderListVO.getPrdNo());
		exchangeCancelListVO.setToAddr			 (paHalfOrderListVO.getToAddr());
		exchangeCancelListVO.setToZiCd			 (paHalfOrderListVO.getToZiCd());
		exchangeCancelListVO.setToNm 			 (paHalfOrderListVO.getToNm());
		exchangeCancelListVO.setPrdQPri  		 (paHalfOrderListVO.getPrdQPri());
		exchangeCancelListVO.setAppState		 (paHalfOrderListVO.getAppState());
		exchangeCancelListVO.setCreateDt		 (paHalfOrderListVO.getCreateDt());
		exchangeCancelListVO.setPrdPri	     	 (paHalfOrderListVO.getPrdPri());
		exchangeCancelListVO.setSalPri			 (paHalfOrderListVO.getSalPri());
		exchangeCancelListVO.setDlvTmpltSeq		 (paHalfOrderListVO.getDlvTmpltSeq());
		exchangeCancelListVO.setRefundDeliPri	 (paHalfOrderListVO.getRefundDeliPri());
		exchangeCancelListVO.setRefundFromAddr	 (paHalfOrderListVO.getRefundFromAddr());
		exchangeCancelListVO.setRefundFromEmTel	 (paHalfOrderListVO.getRefundFromEmTel());
		exchangeCancelListVO.setRefundFromNm	 (paHalfOrderListVO.getRefundFromNm());
		
		

		exchangeCancelListVO.setClaimRequestDate (systemTime);
		
		savePaHalfClaimCancel(exchangeCancelListVO);

		return false;
	}

	private boolean checkExistsReturn(PaHalfOrderListVO paHalfOrderListVO) throws Exception {
		//1) 현재 진행중인 반품 체크
		int cnt = 0;
		cnt = paHalfOrderDAO.selectExistsReturn(paHalfOrderListVO); 
		
		if(cnt < 1 ) return true; 
		
		//2)returnCancel SETTING -- 반품취소 만들기 위해 세팅
		PaHalfOrderListVO returnCancel = new PaHalfOrderListVO();
		String dateTime 	 		   = systemService.getSysdatetimeToString();
		Timestamp systemTime 		   = DateUtil.toTimestamp(dateTime);
		
		returnCancel.setClaimRequestDate (systemTime);
		returnCancel.setPaOrderGb        ("31");
		returnCancel.setBasketNo         (paHalfOrderListVO.getBasketNo());
		returnCancel.setOrdNoNm          (paHalfOrderListVO.getOrdNoNm());
		returnCancel.setPaCode           (paHalfOrderListVO.getPaCode());
		returnCancel.setOrdNo            (paHalfOrderListVO.getOrdNo());
		returnCancel.setQty              (paHalfOrderListVO.getQty());
		returnCancel.setPrdNo			 (paHalfOrderListVO.getPrdNo());
		returnCancel.setToAddr			 (paHalfOrderListVO.getToAddr());
		returnCancel.setToZiCd			 (paHalfOrderListVO.getToZiCd());
		returnCancel.setToNm			 (paHalfOrderListVO.getToNm());
		returnCancel.setPrdQPri			 (paHalfOrderListVO.getPrdQPri());
		returnCancel.setAppState		 (paHalfOrderListVO.getAppState());
		returnCancel.setCreateDt		 (paHalfOrderListVO.getCreateDt());
		returnCancel.setPrdPri			 (paHalfOrderListVO.getPrdPri());
		returnCancel.setSalPri			 (paHalfOrderListVO.getSalPri());
		returnCancel.setClaimNo			 (paHalfOrderListVO.getClaimNo());
		returnCancel.setClaimRequestDate (paHalfOrderListVO.getClaimRequestDate());
		returnCancel.setRefundFromNm	 (paHalfOrderListVO.getRefundFromNm());
		returnCancel.setRefundFromAddr	 (paHalfOrderListVO.getRefundFromAddr());
		returnCancel.setRefundFromZiCd	 (paHalfOrderListVO.getRefundFromZiCd());
		returnCancel.setRefundFromTel	 (paHalfOrderListVO.getRefundFromTel());
		returnCancel.setRefundFromEmTel	 (paHalfOrderListVO.getRefundFromEmTel());
		returnCancel.setOptValueNm		 (paHalfOrderListVO.getOptValueNm());
		returnCancel.setPrdNm			 (paHalfOrderListVO.getPrdNm());
		returnCancel.setDlvTmpltSeq		 (paHalfOrderListVO.getDlvTmpltSeq());
		returnCancel.setStockNo			 (paHalfOrderListVO.getStockNo());
		
		//3)INSERT TPAORDERM, TPAHALFORDERLIST
		savePaHalfClaimCancel(returnCancel);
		return false;
	}

	//반품 취소, 교환취소 생성 TPAHALFORDERLIST, TPAORDERM INSERT
	private String savePaHalfClaimCancel(PaHalfOrderListVO paHalfOrderListVO) throws Exception {
		int executedRtn 	 = 0;
		String rtnMsg		 = Constants.SAVE_SUCCESS;
		String dateTime 	 = systemService.getSysdatetimeToString();
		Timestamp systemTime = DateUtil.toTimestamp(dateTime);
		Paorderm paorderM	 = new Paorderm();
		
		//=기본정보 세팅
		paHalfOrderListVO.setModifyId		(Constants.PA_HALF_PROC_ID);
		paHalfOrderListVO.setModifyDate		(systemTime);
		paHalfOrderListVO.setInsertDate		(systemTime);
		paHalfOrderListVO.setOrgPaOrderGb	("31".equals(paHalfOrderListVO.getPaOrderGb()) ?  "30" : "45");

		//CASE : 교환 -> 교환처리 -> 반품 ->반취 인입 CASE 
        //교환 출고때 신규 주문번호 채번, 해당 신규 주문번호로 반취 인입
        if(paHalfOrderListVO.getOriOrdNo() != null && !"".equals(paHalfOrderListVO.getOriOrdNo()) 
                && "31".equals(paHalfOrderListVO.getPaOrderGb()) ) {
        	String temp = paHalfOrderListVO.getOrdNo();
            paHalfOrderListVO.setOrdNo(paHalfOrderListVO.getOriOrdNo());
            paHalfOrderListVO.setOriOrdNo(temp);
        }
		
		if("31".equals(paHalfOrderListVO.getPaOrderGb())) { //반품취소
			paHalfOrderListVO.setClaimNo	    (paHalfClaimDAO.selectClaimNo4ReturnCancel(paHalfOrderListVO));
		}
		
		if(paHalfOrderListVO.getClaimNo() == null || "".equals(paHalfOrderListVO.getClaimNo()) || 
				"41".equals(paHalfOrderListVO.getPaOrderGb())) { //교환 취소거나 원반품이 없는 반품취소건 
			paHalfOrderListVO.setClaimNo		(ComUtil.increaseLpad(ComUtil.NVL(paHalfClaimDAO.selectClaimNo4ClaimCancel(paHalfOrderListVO) , "000") , 3, "0"));				
		}
      
		//=원 CS 및 중복 CHECK
		Map<String, Integer> checkClaimCancelMap = paHalfClaimDAO.selectAvailableClaim(paHalfOrderListVO);
		if(Integer.parseInt(String.valueOf(checkClaimCancelMap.get("CLAIM_CNT"))) == 0) return Constants.SAVE_FAIL;  //원주문이 없는경우 
		if(Integer.parseInt(String.valueOf(checkClaimCancelMap.get("CLAIM_CNT"))) - Integer.parseInt(String.valueOf(checkClaimCancelMap.get("CLAIM_CANCEL_CNT")))  < 1) return Constants.SAVE_FAIL;  //중복 저장 방지 
		
		//=중복 확인
		int exists	=  paHalfOrderDAO.selectCountClaimList(paHalfOrderListVO);
		if(exists > 0)  return Constants.SAVE_FAIL;

		//=INSERT TPAHALFORDERLIST
		executedRtn = paHalfOrderDAO.insertPaHalfOrderList(paHalfOrderListVO); 
		if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAHALFORDERLIST INSERT" });
		
		//=회수 이후 들어온 취소건에 대한 처리 ==> SD체크에서 CREATE_YN = 9 추출
		int doFlag = paHalfClaimDAO.selectCalimDoFlag(paHalfOrderListVO);
		if(doFlag >= 60) {
			paorderM.setApiResultCode		(Constants.SAVE_FAIL);
			paorderM.setApiResultMessage	("회수확정 이후 인입된 " +  ("31".equals(paHalfOrderListVO.getPaOrderGb()) ?  "반품" : "교환") + "취소건");
			paorderM.setCreateYn			("9");
		}
		
		//=INSERT TPAORDERM  
		insertPaOrdermClaim( paHalfOrderListVO , paorderM);
		
		return rtnMsg;
	}
		
	private void insertPaOrdermClaim(PaHalfOrderListVO paHalfOrderListVO, Paorderm paorderM) throws Exception {
		//=INSERT TPAORDERM 
		switch (paHalfOrderListVO.getPaOrderGb()) {
		case "30": case "31":
			insertPaOrderm			  (paHalfOrderListVO, paorderM);
			break;
		case "40": case "41":
			insertPaorderMForExchange (paHalfOrderListVO, paorderM);	
			break;
		default:
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}				
	}
	
	private int insertPaOrderm(PaHalfOrderListVO paHalfClaim, Paorderm paorderm) throws Exception {
		if(paorderm == null) paorderm = new Paorderm();
				
		int executedRtn   			= 0;
		String sysdate	  			= DateUtil.getCurrentDateTimeAsString();
		String outClaimGb			= "0";
		if( paorderm.getPaDoFlag()  == null || "".equals(paorderm.getPaDoFlag()))  paorderm.setPaDoFlag("20");
		if( paorderm.getPaOrderGb() == null || "".equals(paorderm.getPaOrderGb())) paorderm.setPaOrderGb(paHalfClaim.getPaOrderGb());
		if("반품완료".equals(paHalfClaim.getGubun2())) paorderm.setPaDoFlag("60");
		
		paorderm.setPaGroupCode		(Constants.PA_HALF_GROUP_CODE);
		paorderm.setPaCode			(paHalfClaim.getPaCode());
		paorderm.setPaOrderNo	 	(paHalfClaim.getOrdNo());
		paorderm.setPaOrderSeq	 	(paHalfClaim.getOrdNoNm());
		paorderm.setPaClaimNo	 	(paHalfClaim.getClaimNo());
		paorderm.setPaShipNo		(paHalfClaim.getBasketNo());
		paorderm.setPaProcQty	 	(String.valueOf(paHalfClaim.getQty()));
		paorderm.setOutBefClaimGb	(outClaimGb);
		paorderm.setInsertDate		(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyDate		(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyId		(Constants.PA_HALF_PROC_ID);
		
		//= TPAORDERM INSERT
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		return executedRtn;
	}
	
	private void insertPaorderMForExchange(PaHalfOrderListVO paHalfClaim, Paorderm paorderm) throws Exception{
		
		for(int i = 0 ; i < 2; i++) { //40, 45 (41. 46)
			
			//GOODS_CODE, GOODSDT_CODE SETTING
			if("40".equals(paHalfClaim.getPaOrderGb()) && i == 0) {
				String prdNo = paHalfClaim.getExPrdNo();
				
				if(prdNo == null || "".equals(prdNo)) {
					prdNo = paHalfClaim.getPrdNo();
				}
				Map<String,String> exGoodsDtMap = new HashMap<String, String>();
				exGoodsDtMap.put("productNo", prdNo);
				
				List<Map<String, Object>> goodsDtList=  paHalfClaimDAO.selectExchangeGoodsdt(prdNo);
				
				paorderm.setChangeGoodsCode(goodsDtList.get(0).get("GOODS_CODE").toString());
				if( goodsDtList.size() > 1 ) { 
					if(paHalfClaim.getExStockNo() == null || "".equals(paHalfClaim.getExStockNo())) {
						paorderm.setChangeFlag		  ("00"); 
						paorderm.setChangeGoodsdtCode ("");			
					}else {
						exGoodsDtMap.put("paOptionCode", paHalfClaim.getExStockNo());
						String goodsDtCode = paHalfClaimDAO.selectExchangeGoodsDtCode(exGoodsDtMap);
						paorderm.setChangeFlag		  ("01"); 
						paorderm.setChangeGoodsdtCode (goodsDtCode);			
					}
				}							
				else {
					paorderm.setChangeFlag		  ("01"); 				
					paorderm.setChangeGoodsdtCode (goodsDtList.get(0).get("GOODSDT_CODE").toString());
				}
				
			}

			//PA_ORDER_GB SETTING
			if(i ==0) {
				paorderm.setPaOrderGb		  ("40".equals(paHalfClaim.getPaOrderGb()) ? "40" : "41");
				paorderm.setPaDoFlag		  ("30");
			}else {
				paorderm.setPaOrderGb		  ("40".equals(paHalfClaim.getPaOrderGb()) ? "45" : "46");
				paorderm.setPaDoFlag		  ("20");
			}
			
			// INSERT TPAORDERM
			insertPaOrderm(paHalfClaim, paorderm);
		}
	}

	@Override
	public HashMap<String, Object> selectOrderClaimTargetDt30(ParamMap paramMap) throws Exception {
		return paHalfClaimDAO.selectOrderClaimTargetDt30(paramMap);
	}

	@Override
	public HashMap<String, Object> selectOrderClaimTargetDt20(ParamMap paramMap) throws Exception {
		return paHalfClaimDAO.selectOrderClaimTargetDt20(paramMap);
	}

	@Override
	public String compareAddress(HashMap<String, Object> orderClaimVO) throws Exception {
		return paHalfClaimDAO.compareAddress(orderClaimVO);
	}

	@Override
	public HashMap<String, Object> selectClaimCancelTargetDt(ParamMap paramMap) throws Exception {
		return paHalfClaimDAO.selectClaimCancelTargetDt(paramMap);
	}

	@Override
	public List<HashMap<String, Object>> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paHalfClaimDAO.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public List<HashMap<String, Object>> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paHalfClaimDAO.selectChangeCancelTargetDtList(paramMap);
	}

	@Override
	public String checkShpFeeYn(HashMap<String, Object> orderClaimTargetDt) throws Exception {
		return paHalfClaimDAO.checkShpFeeYn(orderClaimTargetDt);
	}
	 
}
