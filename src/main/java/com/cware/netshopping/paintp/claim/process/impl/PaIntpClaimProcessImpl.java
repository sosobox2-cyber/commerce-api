package com.cware.netshopping.paintp.claim.process.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaIntpCancellist;
import com.cware.netshopping.domain.model.PaIntpClaimlist;
import com.cware.netshopping.domain.model.PaIntpOrderlist;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.domain.paintp.xml.PaIntpClaimVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpOrderProductVO;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.paintp.claim.process.PaIntpClaimProcess;
import com.cware.netshopping.paintp.claim.repository.PaIntpClaimDAO;
import com.cware.netshopping.paintp.order.repository.PaIntpOrderDAO;
import com.cware.netshopping.paintp.util.PaIntpComUtil;

@Service("paintp.claim.paIntpClaimProcess")
public class PaIntpClaimProcessImpl extends AbstractService implements PaIntpClaimProcess{
	
	@Autowired
	private PaIntpClaimDAO PaIntpClaimDAO;
	
	@Autowired
	private PaCommonDAO paCommonDAO;
	
	@Autowired
    private SystemService systemService;
	
	@Autowired
	private PaIntpClaimDAO paIntpClaimDAO;
	
	@Autowired
	private PaIntpComUtil paIntpComUtil;
	
	@Autowired
	private PaIntpOrderDAO paIntpOrderDAO;
	
	/**
	 * 반품신청목록조회(반품 요청 목록조회) IF_PaIntpAPI_03_010 : 저장.
	 * @param List<PaIntpClaimlistVO>
	 * @return String
	 * @throws Exception
	 */	
	@Override
	public void saveClaimList(PaIntpClaimVO claimVO) throws Exception {
		final String dateTime 		= systemService.getSysdatetimeToString();
		final Timestamp systemTime  = DateUtil.toTimestamp(dateTime);
		int executedRtn 			= 0;
		int existsCnt 				= 0;
		
		for(int i = 0; i < claimVO.getClaimPrdList().size(); i++){
			//=1) DATA CONVERT & SETTING
			PaIntpClaimlist claimlistVO = paIntpComUtil.convert(claimVO, i);
			
			claimlistVO.setInsertDate(systemTime);
			claimlistVO.setModifyDate(systemTime);
			claimlistVO.setInsertId  (Constants.PA_INTP_PROC_ID);
			claimlistVO.setModifyId  (Constants.PA_INTP_PROC_ID);
			
			//=2) DATA DUPLICATION CHECK
			existsCnt = PaIntpClaimDAO.selectPaIntpClaimListExists(claimlistVO.getOrdNo(),claimlistVO.getOrdSeq(),claimlistVO.getClmNo(),claimlistVO.getClmSeq(), claimlistVO.getPaOrderGb());
			if(existsCnt > 0) continue; 
			
			//=3). TPAINTPCLAIMLIST INSERT
			executedRtn = PaIntpClaimDAO.insertPaIntpClaimList(claimlistVO);
			if (executedRtn != 1)  throw processException("msg.cannot_save", new String[] { "TPAINTPCLAIMLIST INSERT" });
						
			//=4) TPAORDERM INSERT
			switch(claimlistVO.getPaOrderGb()){
			case"30": case"31":
				insertPaorderMForReturn(claimlistVO);
				break;
				
			case"40": case"41":
				insertPaorderMForExchange(claimlistVO);	
				break;
			}
		}	
	}	
	
	@Override
	public void saveClaimDoneList(PaIntpClaimVO claimVO) throws Exception {
		ParamMap paramMap 			= new ParamMap();
		String MappingSeq			= "";
		
		for(int i = 0; i < claimVO.getClaimPrdList().size(); i++){
			//=1) DATA CONVERT & SETTING
			PaIntpClaimlist claimlistVO = paIntpComUtil.convert(claimVO, i);

			//=2) 이미 처리된 데이터인지 Check
			paramMap.put("paOrderNo"	, claimlistVO.getOrdNo());
			paramMap.put("paOrderSeq"	, claimlistVO.getOrdSeq());
			paramMap.put("paCode"		, claimlistVO.getPaCode());
			paramMap.put("paClaimNo"	, claimlistVO.getClmNo());
			paramMap.put("paOrderGb"	, claimlistVO.getPaOrderGb());
			MappingSeq = PaIntpClaimDAO.selectPaIntpCliamOrdMappingSeq(paramMap);
			
			if(MappingSeq == null || MappingSeq.equals("")) continue; //= 이미 수집된 데이터 일 경우 skip.	
			
			if("30".equals(claimlistVO.getPaOrderGb())) { //반품처리
				updatePaOrderM4DoFlg(claimlistVO , "60" , "반품 회수 확정(직권)");

			}else { //교환처리
				switch (claimlistVO.getCurrentClmPrdStat()) {
				case "30": case "50":
					updatePaOrderM4DoFlg(claimlistVO , "60" ,"교환 회수 확정(직권)");
					break;
				case "80": //case "90":
					updatePaOrderM4DoFlg(claimlistVO , "80", "교환 출고 확정(직권)");
				default:
					break;
				}
			}
		}
	}	
	
	private void updatePaOrderM4DoFlg(PaIntpClaimlist claimlist, String doFlg, String msg) throws Exception{
		Paorderm paorderm 			= new Paorderm();
		int executedRtn  			= 0;
		final String dateTime 		= systemService.getSysdatetimeToString();
		final Timestamp systemTime  = DateUtil.toTimestamp(dateTime);
		
		paorderm.setApiResultCode		("0");
		paorderm.setApiResultMessage	(msg);
		paorderm.setPaDoFlag			(doFlg);
		paorderm.setProcDate			(systemTime);
		paorderm.setPaOrderNo			(claimlist.getOrdNo());
		paorderm.setPaClaimNo			(claimlist.getClmNo());
		paorderm.setPaCode				(claimlist.getPaCode());
		paorderm.setPaOrderGb 			(claimlist.getPaOrderGb());
		
		executedRtn = paIntpClaimDAO.updatePaOrdermResult(paorderm);
		if (executedRtn != 1)  throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
	}
	
	private void insertPaorderMForReturn(PaIntpClaimlist claimlist) throws Exception{
		
		Paorderm paorderm = new Paorderm();
		int executedRtn   = 0;
		
		//= 3. TPORDERM
		paorderm.setPaCode			(claimlist.getPaCode());		  
		paorderm.setPaGroupCode		(Constants.PA_INTP_GROUP_CODE);   //07 : 인터파크
		paorderm.setPaOrderGb		(claimlist.getPaOrderGb());		  //달아줘야함
		paorderm.setPaOrderNo		(claimlist.getOrdNo());
		paorderm.setPaOrderSeq		(claimlist.getOrdSeq().toString());
		paorderm.setPaClaimNo		(claimlist.getClmNo());
		paorderm.setPaProcQty		(String.valueOf(claimlist.getClmQty()));
		
		switch (claimlist.getCurrentClmPrdStat()) { //TODO 직권 체크 한번 해봐야함..
		case "10": case "20":  case "40" : 
			paorderm.setPaDoFlag	("20"); 
			break;
		default:
			paorderm.setPaDoFlag	("60"); //직권취소(상담원) 로 발생한 반품 케이스 , 해당 케이스는 처음부터 인터파크에서 반품 완료 데이터를 발송한 경우 발생한다.(빈도가 낮아보임) 
			break;
		}
		
		paorderm.setOutBefClaimGb	("0");
		paorderm.setInsertDate		(claimlist.getInsertDate());
		paorderm.setModifyDate		(claimlist.getModifyDate());
		paorderm.setModifyId		(claimlist.getInsertId());
		
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		if (executedRtn != 1)  throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		
	}
	
	private void insertPaorderMForExchange(PaIntpClaimlist claimlist) throws Exception{

		Paorderm paorderm 					 = null;
		int executedRtn  					 = 0;
		int goodsCnt 						 = 0;
		ParamMap paramMap					 = null;
		
		for(int j = 0; j < 2; j++){
			paorderm = new Paorderm();
			
			switch(claimlist.getPaOrderGb()){
			case "40":
				paramMap = new ParamMap();
				paramMap.put("paCode"	, claimlist.getPaCode());
				paramMap.put("goodsCode", claimlist.getEntrPrdNo());
				goodsCnt = paIntpClaimDAO.selectGoodsdtInfoCount(paramMap);
				
				if(goodsCnt > 1) { //단품이 여러개이면 텍스트 보고 상담원이 직접 입력
					paorderm.setChangeFlag		  ("00"); 
					paorderm.setChangeGoodsCode   (claimlist.getEntrPrdNo());
					paorderm.setChangeGoodsdtCode ("");					
				}else {
					paorderm.setChangeFlag		  ("01"); 
					paorderm.setChangeGoodsCode   (claimlist.getEntrPrdNo());
					paorderm.setChangeGoodsdtCode (claimlist.getOptNo());
				}
							
				/* 아래 주석은 처음 인터파크에서 요청한것 처럼 무조건 인터파크 제휴교환은 같은 단품으로만 교환이 가능할때 살리도록 한다.
				
				paramMap.put("paCode"	 , claimlist.getPaCode());
				goodsInfoMap = paIntpClaimDAO.selectGoodsOptionCode4Exchange(paramMap);
				paorderm.setChangeFlag		  ("01"); 
				paorderm.setChangeGoodsCode   (goodsInfoMap.get("GOODS_CODE"));
				paorderm.setChangeGoodsdtCode (goodsInfoMap.get("GOODSDT_CODE"));
				*/
				
				if(j == 0){
					paorderm.setPaOrderGb("40");
				}else {
					paorderm.setPaOrderGb("45");
				}					
				break;
				
			case "41":
				if(j == 0){
					paorderm.setPaOrderGb("41");
				}else {
					paorderm.setPaOrderGb("46");
				}	
				break;
			}
			
			paorderm.setPaCode			(claimlist.getPaCode());
			paorderm.setPaGroupCode		(Constants.PA_INTP_GROUP_CODE);
			paorderm.setPaOrderNo		(claimlist.getOrdNo());
			paorderm.setPaOrderSeq		(claimlist.getOrdSeq().toString());
			paorderm.setPaClaimNo		(claimlist.getClmNo());
			paorderm.setPaProcQty		(String.valueOf(claimlist.getClmQty()));
			paorderm.setPaDoFlag		("20");
			paorderm.setOutBefClaimGb	("0");
			paorderm.setInsertDate		(claimlist.getInsertDate());
			paorderm.setModifyDate		(claimlist.getModifyDate());
			paorderm.setModifyId		(claimlist.getInsertId());
			
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
			if (executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		} //end of for
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return PaIntpClaimDAO.selectClaimTargetList(paramMap);
	}

	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return PaIntpClaimDAO.compareAddress(paramMap);
	}

	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return PaIntpClaimDAO.selectOrderCalimTargetDt30List(paramMap);
	}

	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return PaIntpClaimDAO.selectOrderCalimTargetDt20List(paramMap);
	}
	
	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return PaIntpClaimDAO.selectClaimCancelTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return PaIntpClaimDAO.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return PaIntpClaimDAO.selectChangeCancelTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectReturnOkList() throws Exception {
		return PaIntpClaimDAO.selectReturnOkList();
	}

	@Override
	public void updatePaOrdermResult(Paorderm paorderm) throws Exception {
		PaIntpClaimDAO.updatePaOrdermResult(paorderm);
	}

	@Override
	public List<Object> selectExchangeSlipList() throws Exception {
		return PaIntpClaimDAO.selectExchangeSlipList();
	}

	@Override
	public void createCancelList(PaIntpClaimVO claimVO) throws Exception {
		
		if(claimVO == null || claimVO.getClaimPrdList() == null) return;
		
		final String sysdate 		= DateUtil.getCurrentDateTimeAsString();		
		int existsCount 			= 0;
		int doFlag 		 			= 0;
		String outClaimGb			= "";
		
		for(int i = 0; i < claimVO.getClaimPrdList().size(); i++){
			//=1) DATA CONVERT & SETTING
			PaIntpOrderlist claimlistVO = paIntpComUtil.convert2(claimVO, i);			
			PaIntpOrderProductVO prdVO 	= paIntpOrderDAO.getOrderProductInfo(claimlistVO.getOrdNo(), claimlistVO.getOrdSeq());
			if (prdVO == null ) continue;
			
			claimlistVO.setClmreqSeq	(paIntpClaimDAO.selectMaxClmReqSeq(claimlistVO.getOrdNo(), claimlistVO.getOrdSeq()));
			claimlistVO.setPrdNo		(prdVO.getPrdNo());
			claimlistVO.setOptNo		(prdVO.getOptNo());
			claimlistVO.setDelvsetlSeq	(prdVO.getDelvsetlSeq());
			claimlistVO.setWithdrawYn	("0");
			claimlistVO.setInsertId		(Constants.PA_INTP_PROC_ID);
			existsCount = paIntpOrderDAO.countOrderList(claimlistVO.getOrdNo(), claimlistVO.getOrdSeq(), "", "20");
			
			if (existsCount >= 1) continue;
			int insertResult = paIntpOrderDAO.insertCancelReqPaIntpOrderlist(claimlistVO);
			if (insertResult != 1) throw processException("msg.cannot_save", new String[] { "TPAINTPORDERLIST INSERT" });
				
			PaIntpCancellist cancel = paIntpClaimDAO.selectPaIntpOrderCanceDone(claimlistVO);
			
			doFlag = Integer.parseInt(cancel.getDoFlag());
			
			if(doFlag < 30) {
				outClaimGb = "0";
			}else {
				outClaimGb = "1";
			}
			
			Paorderm paorderm = new Paorderm();
			paorderm.setPaCode			(cancel.getPaCode());
			paorderm.setPaOrderGb		(cancel.getPaOrderGb());
			paorderm.setPaOrderNo		(cancel.getPaOrderNo());
			paorderm.setPaOrderSeq		(ComUtil.objToStr(cancel.getOrdSeq()		, null));
			paorderm.setPaShipNo		(ComUtil.objToStr(cancel.getDelvsetlSeq()	, null));
			paorderm.setPaClaimNo		(ComUtil.objToStr(cancel.getClmreqSeq()		, null));
			paorderm.setPaProcQty		(ComUtil.objToStr(cancel.getClmreqQty()		, null));
			paorderm.setPaDoFlag		("20");
			paorderm.setOutBefClaimGb	(outClaimGb);
			paorderm.setInsertDate		(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setModifyDate		(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setModifyId		(Constants.PA_INTP_PROC_ID);
			paorderm.setPaGroupCode		(Constants.PA_INTP_GROUP_CODE);
			
			int result = paCommonDAO.insertPaOrderM(paorderm);
			if ( result != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });			
						
		}	

	}
	
}