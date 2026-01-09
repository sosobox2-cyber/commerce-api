package com.cware.netshopping.passg.claim.process.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractModel;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaSsgCancelListVO;
import com.cware.netshopping.domain.PaSsgClaimListVO;
import com.cware.netshopping.domain.PaSsgOrderListVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.passg.claim.process.PaSsgClaimProcess;
import com.cware.netshopping.passg.claim.repository.PaSsgClaimDAO;

@Service("passg.claim.paSsgClaimProcess")
public class PaSsgClaimProcessImpl extends AbstractService implements PaSsgClaimProcess{
	
	@Autowired
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "passg.claim.paSsgClaimDAO")
	PaSsgClaimDAO paSsgClaimDAO;
	
	@Override
	public int saveSsgCancelList(PaSsgCancelListVO paSsgCancelVo, String claimStatus) throws Exception {
		int executedRtn 	 = 0;
		int checkCancelCnt 	 = 0;
		
		
		//취소완료일 때 취소일시와 주문일시 차이가 5분 이후일 경우 직권취소로 봄
		//주문 생성 전 취소 케이스 잡기위한처리. 주문생성전 취소, 주문 생성 후 직권취소 케이스는 처리 불가.
		if("21".equals(claimStatus)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date ordRcpDts  = format.parse(paSsgCancelVo.getOrdRcpDts());
			Date ordCnclDts = format.parse(paSsgCancelVo.getOrdCnclDts());
			
			if(paSsgCancelVo.getCnclItemQty() != paSsgCancelVo.getDircItemQty()) { //부분취소일 경우에만 적용 
				//시점차로 인해 취소 안만들어졌을 경우 주문번호 변경하여 로컬처리
				if((ordCnclDts.getTime() - ordRcpDts.getTime()) / (60*1000) < 5) {
					return 0;
				}				
			}
			
		} 
				
		// 원주문 데이터 체크
		checkCancelCnt = paSsgClaimDAO.countOrderList(paSsgCancelVo);
		if(checkCancelCnt < 1) return 0;
		
		// TPASSGCANCELLIST 중복 데이터 유무 체크
		checkCancelCnt = paSsgClaimDAO.selectPaSsgCancelListCount(paSsgCancelVo);
		if(checkCancelCnt > 0) {
			if("21".equals(claimStatus)) {
				checkCancelCnt = paSsgClaimDAO.selectPaSsgCancelMListCount(paSsgCancelVo);
				if(checkCancelCnt < 1) return 0;

				executedRtn = paSsgClaimDAO.updatePaSsgCancelComplete(paSsgCancelVo);
				if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPASSGCANCELLIST UPDATE" });
			}else {
				return 0;
			}
		}else {
			// INSERT TPASSGCANCELLIST
			executedRtn = paSsgClaimDAO.insertTpaSsgCancelList(paSsgCancelVo);
			if(executedRtn != 1){
				throw processException("msg.cannot_save", new String[] { "TPASSGCANCELLIST INSERT" });
			}
		}
		
		//취소 완료건은 TPAORDERM에도 넣어줘야됨.
		// INSERT TPAORDERM
		if("21".equals(claimStatus)) {
			executedRtn = insertPaOrderm(paSsgCancelVo, new Paorderm());
			if(executedRtn != 1){
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}
		}
		
		return 0;
	}
	
	@Override
	public int saveSsgCancelCompleteList(PaSsgCancelListVO paSsgCancelVo, String claimStatus) throws Exception {
		int executedRtn 	 = 0;
		int checkCancelCnt 	 = 0;
		int exists 			 = 0;
		String delyType = paSsgClaimDAO.selectDelyType(paSsgCancelVo.getItemId());
		String procFlag = paSsgClaimDAO.selectPaSsgCancelListProcFlag(paSsgCancelVo);
		String doFlag = paSsgClaimDAO.selectOrderdtDoFlag(paSsgCancelVo);

		//취소완료일 때 취소일시와 주문일시 차이가 5분 이후일 경우 직권취소로 봄
		//주문 생성 전 취소 케이스 잡기위한처리. 주문생성전 취소, 주문 생성 후 직권취소 케이스는 처리 불가.
		if("21".equals(claimStatus)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date ordRcpDts  = format.parse(paSsgCancelVo.getOrdRcpDts());
			Date ordCnclDts = format.parse(paSsgCancelVo.getOrdCnclDts());
			
			if(paSsgCancelVo.getCnclItemQty() != paSsgCancelVo.getDircItemQty()) { //부분취소일 경우에만 적용 
				//시점차로 인해 취소 안만들어졌을 경우 주문번호 변경하여 로컬처리
				if((ordCnclDts.getTime() - ordRcpDts.getTime()) / (60*1000) < 5) {
					return 0;
				}				
			}
		} 
			
		
		// 원주문 데이터 체크
		checkCancelCnt = paSsgClaimDAO.countOrderList(paSsgCancelVo);
		if(checkCancelCnt < 1) return 0;

		if( doFlag != null && Integer.parseInt(doFlag) >= 30 && "10".equals(delyType)) { //당사상품 & 출하지시 이후의 경우 반품데이터로 적재
			PaSsgClaimListVO paSsgClaimList = settingClaimList(paSsgCancelVo);
			
			exists = paSsgClaimDAO.countClaimList(paSsgClaimList);
			if (exists > 0 || "10".equals(procFlag)) return 0; 
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("orordNo",			paSsgCancelVo.getOrordNo());
			paramMap.put("orordItemSeq",	paSsgCancelVo.getOrordItemSeq());
			paSsgClaimDAO.updatePaSsgCancelList4Withdraw(paramMap);
			
			//=INSERT TPASSGCLAIMLIST
		    executedRtn = paSsgClaimDAO.insertPaSsgClaimList(paSsgClaimList);
		    if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPASSGCLAIMLIST INSERT" });
		    
		    // =INSERT TPAORDERM
		    executedRtn = insertPaOrderm(paSsgClaimList, new Paorderm());
			if(executedRtn != 1){
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}
		}else {
			
			// TPASSGCANCELLIST 중복 데이터 유무 체크
			checkCancelCnt = paSsgClaimDAO.selectPaSsgCancelListCount(paSsgCancelVo);
			if(checkCancelCnt > 0) { 
				if("21".equals(claimStatus)) {
					checkCancelCnt = paSsgClaimDAO.selectPaSsgCancelMListCount(paSsgCancelVo);
					if(checkCancelCnt < 1) return 0;
					
					executedRtn = paSsgClaimDAO.updatePaSsgCancelComplete(paSsgCancelVo);
					if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPASSGCANCELLIST UPDATE" });
				}else {
					return 0;
				}
			}else {
				// INSERT TPASSGCANCELLIST
				executedRtn = paSsgClaimDAO.insertTpaSsgCancelList(paSsgCancelVo);
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPASSGCANCELLIST INSERT" });
				}
			}
			
			//취소 완료건은 TPAORDERM에도 넣어줘야됨.
			// INSERT TPAORDERM
			if("21".equals(claimStatus)) {
				executedRtn = insertPaOrderm(paSsgCancelVo, new Paorderm());
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}
			}
		}
		
		
		return 0;
	}
	
	private PaSsgClaimListVO settingClaimList(PaSsgCancelListVO paSsgCancelVo) throws Exception {

		PaSsgClaimListVO paSsgClaimList = new PaSsgClaimListVO();
		PaSsgOrderListVO paSsgOrderList = null ;
		
		paSsgOrderList = paSsgClaimDAO.selectPaSsgOrderList(paSsgCancelVo);
		
		paSsgClaimList.setPaCode(paSsgCancelVo.getPaCode());
		paSsgClaimList.setPaClaimGb("30");
		paSsgClaimList.setPaOrderGb("30");
		paSsgClaimList.setOrordNo(paSsgCancelVo.getOrordNo());
		paSsgClaimList.setOrordItemSeq(paSsgCancelVo.getOrordItemSeq());
		paSsgClaimList.setOrdNo(paSsgCancelVo.getOrdNo());
		paSsgClaimList.setOrdItemSeq(paSsgCancelVo.getOrdItemSeq());
		paSsgClaimList.setShppNo(paSsgOrderList.getShppNo());
		paSsgClaimList.setShppSeq(paSsgOrderList.getShppSeq());
		paSsgClaimList.setClmRsnCd(paSsgCancelVo.getClmRsnCd());
		paSsgClaimList.setClmRsnNm(paSsgCancelVo.getClmRsnNm());
		paSsgClaimList.setClmRsnCntt(paSsgCancelVo.getClmRsnCntt());
		paSsgClaimList.setDircItemQty(paSsgCancelVo.getDircItemQty());
		paSsgClaimList.setProcItemQty(paSsgCancelVo.getProcOrdQty());
		paSsgClaimList.setItemId(paSsgCancelVo.getItemId());
		paSsgClaimList.setItemNm(paSsgCancelVo.getItemNm());
		paSsgClaimList.setRcptpeNm(paSsgOrderList.getRcptpeNm());
		paSsgClaimList.setRcptpeHpno(paSsgOrderList.getRcptpeHpno());
		paSsgClaimList.setRcptpeTelno(paSsgOrderList.getRcptpeTelno());
		paSsgClaimList.setShpplocAddr(paSsgOrderList.getShpplocAddr());
		paSsgClaimList.setShpplocBascAddr(paSsgOrderList.getShpplocBascAddr());
		paSsgClaimList.setShpplocDtlAddr(paSsgOrderList.getShpplocDtlAddr());
		paSsgClaimList.setShpplocOldZipcd(paSsgOrderList.getShpplocOldZipcd());
		paSsgClaimList.setShpplocZipcd(paSsgOrderList.getShpplocZipcd());
		paSsgClaimList.setShppDivDtlNm("SSG취소(당사)");
		paSsgClaimList.setInsertId(Constants.PA_SSG_PROC_ID);
		paSsgClaimList.setModifyDate(paSsgCancelVo.getModifyDate());
		paSsgClaimList.setInsertDate(paSsgCancelVo.getInsertDate());
		
		return paSsgClaimList;
	}

	private int insertPaOrderm(AbstractModel abstractVo, Paorderm paorderm) throws Exception {
		
		if(paorderm == null) paorderm = new Paorderm();
		
		Map<String, String> voMap = null;
		
		if(abstractVo instanceof PaSsgCancelListVO) {
			voMap = BeanUtils.describe((PaSsgCancelListVO)abstractVo);
			if("180".equals(ComUtil.objToStr(voMap.get("ordItemStatCd")))){
				voMap.put("claimQty", voMap.get("cnclItemQty"));
				
				//원주문 doFlag 확인
				HashMap<String, Object> cancel = paSsgClaimDAO.selectPaSsgCancelInfo((PaSsgCancelListVO)abstractVo);
				int doFlag = Integer.parseInt(cancel.get("DO_FLAG").toString());
				
				if(doFlag < 30) {
					voMap.put("outBefClaimGb", "0");
				} else {
					voMap.put("outBefClaimGb", "1");
				}
				paorderm.setPaClaimNo (voMap.get("ordNo"));
			}else {
				voMap.put("claimQty", voMap.get("procOrdQty"));
			}
			paorderm.setPaOrderNo  (voMap.get("orordNo"));
			paorderm.setPaOrderSeq (voMap.get("orordItemSeq"));
		} else { //교환, 환불
			voMap = BeanUtils.describe((PaSsgClaimListVO)abstractVo);
			voMap.put("outBefClaimGb","0");
			voMap.put("claimQty", voMap.get("dircItemQty")); //지시수량
			paorderm.setPaShipNo   (voMap.get("shppNo"));
			paorderm.setPaShipSeq  (voMap.get("shppSeq"));
			paorderm.setPaOrderNo  (voMap.get("orordNo"));
			paorderm.setPaClaimNo  (voMap.get("ordNo"));
			paorderm.setPaOrderSeq (voMap.get("orordItemSeq"));
			
			// 반품 인입 시 원주문 doFlag 확인
			if("30".equals(String.valueOf(voMap.get("paOrderGb")))) {
				HashMap<String, Object> claim = paSsgClaimDAO.selectPaSsgClaimInfo((PaSsgClaimListVO)abstractVo);
				// 출하지시 상태일 경우 출고전반품 데이터로 세팅 
				if("30".equals(String.valueOf(claim.get("DO_FLAG"))) && !"10".equals(String.valueOf(claim.get("DELY_GB")))) { //24.08.23 - 당사상품제외
					paorderm.setRemark1V("출고전반품 대상");
				}
			}
		}
		
		int executedRtn   = 0;
		String sysdate 	  = DateUtil.getCurrentDateTimeAsString();
		String paOrderGb  = voMap.get("paOrderGb");
		String outClaimGb = voMap.get("outBefClaimGb");
		
		paorderm.setPaGroupCode(Constants.PA_SSG_GROUP_CODE);
		paorderm.setPaOrderGb  (paOrderGb);
		paorderm.setPaCode	   (voMap.get("paCode"));
		
		paorderm.setPaProcQty  (String.valueOf(voMap.get("claimQty")));
		
		if("64".equals(ComUtil.objToStr(voMap.get("lastShppProgStatDtlCd"))) || "SSG취소(당사)".equals(ComUtil.objToStr(voMap.get("shppDivDtlNm")))) {
			paorderm.setPaDoFlag("60");
		}else if("0".equals(outClaimGb)) {
			paorderm.setPaDoFlag("20");
		} else {
			paorderm.setPaDoFlag("60");
		}
		
		paorderm.setOutBefClaimGb(outClaimGb);
		paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyId  (Constants.PA_SSG_PROC_ID);
		
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		
		if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		
		return executedRtn;
	}
	
	@Override
	public String saveSsgClaimList(PaSsgClaimListVO paSsgClaimList) throws Exception {
		//안에 절대로 Try~catch 넣지 말것!!
		int executedRtn   = 0;
		int exists 		  = 0;
		int checkClaimCnt = 0;
		//int checkCompleteCnt = 0;
		String rtnMsg	  = Constants.SAVE_SUCCESS;
		String claimGb	  = paSsgClaimList.getPaClaimGb();
		
		
		if("31".equals(claimGb) || "46".equals(claimGb)) {
			// 원클레임 데이터 체크
			checkClaimCnt = paSsgClaimDAO.countOrgClaimList(paSsgClaimList);
			if(checkClaimCnt < 1) return "";
		}
		
		//중복체크
		exists = paSsgClaimDAO.countClaimList(paSsgClaimList);
		if(exists > 0) {
			return "";
			
			/* 반품 접수 후 자동 반품완료되는 케이스 처리하기 위한 로직
			if("64".equals(paSsgClaimList.getLastShppProgStatDtlCd())) {
				checkCompleteCnt = paSsgClaimDAO.selectPaSsgClaimCompleteCount(paSsgClaimList);
				if(checkCompleteCnt < 1) return "";

				Map<String, Object> claimMap = new HashMap<String, Object>();
				
				claimMap.put("PA_DO_FLAG"	  	 , "60");
				claimMap.put("API_RESULT_CODE"	 , "회수확정(직권)");
				claimMap.put("API_RESULT_MESSAGE", "000000");
				claimMap.put("PA_CLAIM_NO"		 , paSsgClaimList.getOrdNo());
				claimMap.put("PA_CODE"			 , paSsgClaimList.getPaCode());
				claimMap.put("PA_ORDER_NO"		 , paSsgClaimList.getOrordNo());
				claimMap.put("PA_ORDER_SEQ"		 , paSsgClaimList.getOrordItemSeq());
				claimMap.put("PA_SHIP_NO"		 , paSsgClaimList.getShppNo());
				claimMap.put("PA_SHIP_SEQ"		 , paSsgClaimList.getShppSeq());
				claimMap.put("PA_ORDER_GB"		 , "30");
				
				executedRtn = paSsgClaimDAO.updatePaOrderMDoFlag(claimMap);
				if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
				
				return rtnMsg;
			}else {
				return "";
			}
			*/
		}
		
	    paSsgClaimList.setInsertId(Constants.PA_SSG_PROC_ID);
	    paSsgClaimList.setModifyId(Constants.PA_SSG_PROC_ID);
	    
	    //=INSERT TPASSGCLAIMLIST
	    executedRtn = paSsgClaimDAO.insertPaSsgClaimList(paSsgClaimList);
	    if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPASSGCLAIMLIST INSERT" });
	    
	    //=INSERT TPAORDERM
	    switch(claimGb) {
	    case "30": case "31":
	    	executedRtn = insertPaOrderm(paSsgClaimList, new Paorderm());
			if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT(CLAIM30)" });
			break;
	    case "45": case "46":
	    	executedRtn = insertPaorderMForExchange(paSsgClaimList);
	    	break;
	    }
	    if (executedRtn < 1)  throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		
		return rtnMsg;
	}
	
	private int insertPaorderMForExchange(PaSsgClaimListVO vo) throws Exception {
		
		ParamMap paramMap = null;
		Paorderm paorderm = null;
		int executedRtn = 0;
		
		//교환의 경우 교환배송, 교환철회 서로 다른 API에서 주지만, 교환철회의 경우 교환철회만 주기 때문에 교환철회 시 철회 데이터 한번에 생성
		for(int i = 0; i < 2; i++) {
			paorderm = new Paorderm();
			
			switch(vo.getPaClaimGb()) {
			case "45" :
				if(i == 1)continue;
				
				paramMap = new ParamMap();
				paramMap.put("paCode", vo.getPaCode());
				paramMap.put("itemId", vo.getItemId());
				paramMap.put("uitemId", vo.getUitemId());
				
				HashMap<String, Object> goodsInfo = paSsgClaimDAO.selectSsgExchangeGoodsInfo(paramMap);
				
				paorderm.setChangeFlag("01"); //처리완료
				paorderm.setChangeGoodsCode(goodsInfo.get("GOODS_CODE").toString());
				paorderm.setChangeGoodsdtCode(goodsInfo.get("GOODSDT_CODE").toString());
				paorderm.setPaOrderGb("45");
				break;
			case "46" :
				if(i == 0) {
					paorderm.setPaOrderGb("46");
				}else {
					paorderm.setPaOrderGb("41");
					HashMap<String, Object> map = paSsgClaimDAO.selectSsgExchangeDeliveryDt(vo);
					vo.setShppNo(ComUtil.objToStr( map.get("PA_SHIP_NO")));
					vo.setShppSeq(ComUtil.objToStr( map.get("PA_SHIP_SEQ")));
				}
				break;
			}
			
			paorderm.setPaCode		  (vo.getPaCode());
			paorderm.setPaGroupCode	  (Constants.PA_SSG_GROUP_CODE);
			paorderm.setPaClaimNo	  (vo.getOrdNo());
			paorderm.setPaOrderNo	  (vo.getOrordNo());
			paorderm.setPaOrderSeq	  (vo.getOrordItemSeq());
			paorderm.setPaShipNo   	  (vo.getShppNo());
			paorderm.setPaShipSeq  	  (vo.getShppSeq());
			paorderm.setPaProcQty	  (String.valueOf(vo.getDircItemQty()));
			paorderm.setPaDoFlag	  ("20");
			paorderm.setOutBefClaimGb ("0");
			paorderm.setInsertDate	  (vo.getInsertDate());
			paorderm.setModifyDate	  (vo.getModifyDate());
			paorderm.setModifyId	  (vo.getModifyId());
			
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
				
			if (executedRtn < 1)  throw processException("msg.cannot_save", new String[] { "TPAORDERM(45, 46) INSERT" });
		}
		return executedRtn;
	}
	
	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paSsgClaimDAO.selectClaimTargetList(paramMap);
	}
	
	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paSsgClaimDAO.selectCancelInputTargetDtList(paramMap);
	}
	
	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paSsgClaimDAO.selectOrderChangeTargetDtList(paramMap);
	}
	
	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paSsgClaimDAO.compareAddress(paramMap);
	}
	
	@Override
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("changeFlag", changeFlag);
		paramMap.put("mappingSeq", mappingSeq);
		return paSsgClaimDAO.updatePaOrdermChangeFlag(paramMap);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return paSsgClaimDAO.selectOrderCalimTargetDt30List(paramMap);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paSsgClaimDAO.selectOrderCalimTargetDt20List(paramMap);
	}
	
	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paSsgClaimDAO.selectClaimCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paSsgClaimDAO.selectChangeCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<HashMap<String, Object>> selectPaSsgRecoveryList(ParamMap paramMap) throws Exception {
		return paSsgClaimDAO.selectPaSsgRecoveryList(paramMap);
	}
	
	@Override
	public int updatePaOrderMDoFlag(Map<String,Object> paorderm) throws Exception {
		return paSsgClaimDAO.updatePaOrderMDoFlag(paorderm);
	}
	
	@Override
	public List<Map<String, Object>> selectPaSsgOrderCancelList() throws Exception {
		return paSsgClaimDAO.selectPaSsgOrderCancelList();
	}
	
	@Override
	public int updatePaSsgCancelConfirm(ParamMap paramMap) throws Exception {
		
		int executedRtn = 0;
		
		executedRtn = paSsgClaimDAO.updatePaSsgCancelList(paramMap);
		
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPASSGCANCELLIST INSERT" });
				
		List<PaSsgCancelListVO> voList = paSsgClaimDAO.selectPaSsgCancelList(paramMap);
		for(PaSsgCancelListVO vo : voList) {
			executedRtn = insertPaOrderm(vo, new Paorderm());
			if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT(CANCEL)" });
		}
		
		return executedRtn;
	}

	@Override
	public int updatePaSsgCancelList(ParamMap paramMap) throws Exception {
		return paSsgClaimDAO.updatePaSsgCancelList(paramMap);
	}
	
	@Override
	public int updatePaSsgCancelList4Withdraw(ParamMap paramMap) throws Exception {
		int executedRtn    = 0;

		//Update TPASSGCANCELLIST.WITHDRAW_YN = 1
		executedRtn = paSsgClaimDAO.updatePaSsgCancelList4Withdraw(paramMap);
		
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPASSGCANCELLIST UPDATE" });
		
		return executedRtn;
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception {
		return paSsgClaimDAO.selectPaMobileOrderAutoCancelList(paramMap);
	}

	@Override
	public PaSsgOrderListVO selectPaSsgOrderList(PaSsgCancelListVO paSsgCancelList) throws Exception {
		return paSsgClaimDAO.selectPaSsgOrderList(paSsgCancelList);
	}

}
