package com.cware.netshopping.palton.claim.process.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractModel;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaLtonCancelListVO;
import com.cware.netshopping.domain.PaLtonClaimListVO;
import com.cware.netshopping.domain.model.Custcounseldt;
import com.cware.netshopping.domain.model.Custcounselm;
import com.cware.netshopping.domain.model.PaLtonNotReceiveList;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.claim.repository.PaClaimDAO;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.palton.claim.process.PaLtonClaimProcess;
import com.cware.netshopping.palton.claim.repository.PaLtonClaimDAO;
import com.cware.netshopping.palton.delivery.repository.PaLtonDeliveryDAO;
import com.cware.netshopping.palton.util.PaLtonComUtill;

@Service("palton.claim.paLtonClaimProcess")
public class PaLtonClaimProcessImpl  extends AbstractService implements PaLtonClaimProcess{
	
	@Autowired
    private SystemService systemService;
	
	@Autowired
	private PaCommonDAO paCommonDAO;
	@Autowired
	private PaLtonClaimDAO paLtonClaimDAO;

	@Autowired
	PaLtonDeliveryDAO paLtonDeliveryDAO;
	
	@Autowired
	private SystemProcess systemProcess;
	
	@Autowired
    private PaClaimDAO paclaimDAO;
	
	@Override
	public int saveLtonCancelList(PaLtonCancelListVO cancelVo) throws Exception {
		return insertTPaLtonCancelList(cancelVo);
	}
	
	@Override
	public int saveLtonCompleteCancelList(PaLtonCancelListVO cancelVo) throws Exception {
		int executedRtn    = 0;
		
		//= Insert TPACANCELLIST
		executedRtn 	= insertTPaLtonCancelList(cancelVo);
		if(executedRtn < 1) return 0; //중복체크, 원주문 미생성
		
		//= OutClaimGb Setting
		String outClaimGb		= getOutClaimGb(cancelVo);
		cancelVo.setOutClaimGb(outClaimGb);
		
		//= Insert TPAORDERM
		executedRtn 	= insertPaOrderm(cancelVo, new Paorderm());			
		return executedRtn;
	}

	private String getOutClaimGb(PaLtonCancelListVO cancelVO) throws Exception{
		int doFlag = 0;
		String outClaimGb = "0";
		
		doFlag = paLtonClaimDAO.selectOutClaimGb(cancelVO);	
		
		if(doFlag < 30) {
			outClaimGb = "0";
		}else {
			outClaimGb = "1";
		}
		
		return outClaimGb;
	}

	@Override
	public int saveLtonWithdrawCancelList(PaLtonCancelListVO cancelVo) throws Exception {
		int executedRtn 	 = 0;
		int checkCancelCnt 	 = 0;
		//=1) Exist Original Cancel data for withdraw
		//cancelVo.setProcFlag(cancelVo.getOrglProcSeq()); //TODO proc_Flag의 관계를 잘 파악해봐야함..
		checkCancelCnt = paLtonClaimDAO.selectPaLtonWithdrawCancelListCount(cancelVo);
		if(checkCancelCnt != 1) return 0;
		
		//=2) Update TPALTONCANCELLIST.WITHDRAW_YN = 1
		executedRtn = paLtonClaimDAO.updatePaLtonCancelList4Withdraw(cancelVo);
		if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPALTONCANCELLIST UPDATE" });
		
		return executedRtn;
	}
	
	private int insertTPaLtonCancelList(PaLtonCancelListVO cancelVo) throws Exception {
		int executedRtn 	 = 0;
		int checkCancelCnt 	 = 0;
		String dateTime 	 = systemService.getSysdatetimeToString();
		Timestamp systemTime = DateUtil.toTimestamp(dateTime);
		
		//=0) Check Original OrderData
		checkCancelCnt = paLtonDeliveryDAO.countOrderList(cancelVo);
		if(checkCancelCnt < 1) return 0;
		
		//=1) Data Duplicate Check to TPaLtonCancelList
		checkCancelCnt = paLtonClaimDAO.selectPaLtonCancelListCount(cancelVo);
		if(checkCancelCnt > 0) return 0;
		
		//일반취소 직권취소 구분
		if("21".equals(cancelVo.getOdPrgsStepCd())) {
			// MERGE TPALTONCANCELLIST
			executedRtn = paLtonClaimDAO.mergePaLtonCancelList(cancelVo);
			if(executedRtn != 1){
				throw processException("msg.cannot_save", new String[] { "TPALTONCANCELLIST MERGE" });
			}
		} else {
			// INSERT TPALTONCANCELLIST
			cancelVo.setInsertDate(systemTime);			
			executedRtn = paLtonClaimDAO.insertTPaLtonCancelList(cancelVo);
			if(executedRtn != 1){
				throw processException("msg.cannot_save", new String[] { "TPALTONCANCELLIST INSERT" });
			}
		}
		return executedRtn;
	}
	
	@Override
	public int saveCancelConfirm(Map<String, Object> cancelMap) throws Exception {
		int executedRtn 		= 0;
		String procFlag			= ComUtil.NVL(cancelMap.get("procFlag"), "00").toString();

		//1) Update TPALTONCLAIMLIST.PROC_FLAG = '10'
		switch (procFlag) {
		case "00": //일반 취소승인 API에서 호출
			cancelMap.put("procFlag"	, "10");
			cancelMap.put("message"		, "취소승인완료");
			break;
		case "10": //상담원페이지에서 취소승인 처리한 경우
			cancelMap.put("procFlag"	, "10");
			cancelMap.put("message"		, "취소승인완료(상담원)");
			break;
		default:   
			return 0;
		}
		updateProcFlag(cancelMap); 
		
		//2) INSERT TPAORDERM
		executedRtn = insertTPaOrderM((HashMap<String, Object>)cancelMap);
		return executedRtn;
	}
	
	
	


	@Override
	public int insertTPaOrderM(HashMap<String, Object> cancelMap) throws Exception {
		PaLtonCancelListVO vo 	= new PaLtonCancelListVO();
		vo = (PaLtonCancelListVO)PaLtonComUtill.map2VO(cancelMap, PaLtonCancelListVO.class);
		int executedRtn = insertPaOrderm(vo, new Paorderm());
		return executedRtn;
	}
	
	
	@Override
	public void savePaLtonClaimList(PaLtonClaimListVO claimVO) throws Exception {
		final String dateTime 		= systemService.getSysdatetimeToString();
		final Timestamp systemTime  = DateUtil.toTimestamp(dateTime);
		String claimGb 				= claimVO.getPaOrderGb();
		
		//=0) Setting Data
		claimVO.setInsertDate(systemTime);
		claimVO.setInsertId(Constants.PA_LTON_PROC_ID);
		
		if(claimGb.equals("40")) {
			claimVO.setPaOrderGb(claimVO.getDvRtrvDvsCd().equals("DV") ? "40" : "45");
		} else if(claimGb.equals("41")) {
			claimVO.setPaOrderGb(claimVO.getDvRtrvDvsCd().equals("DV") ? "41" : "46");
		}
		//=1) Data Duplicate Check to TPALTONCLAIMLIST				
		int checkCancelCnt = paLtonClaimDAO.selectPaLtonClaimListCount(claimVO);
		if(checkCancelCnt > 0) {
			checkClaimStep(claimVO);
			return;
		}
		
		//=2) Insert TPALTONCLAIMLIST
		int executedRtn = paLtonClaimDAO.insertTPaLtonClaimList(claimVO);
		if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPALTONCLAIMLIST INSERT" });
		
		//=4) TPAORDERM INSERT
		switch(claimGb){
		case"30": case"31":
			insertPaOrderm(claimVO, new Paorderm());
			break;
			
		case"40": case"41":
			insertPaorderMForExchange(claimVO);	
			break;
		}
		
	} 
	
	private void checkClaimStep(PaLtonClaimListVO claimVO) {
		//롯데온은 반품배송비 같은 정보를 반품 요청단계가 아니라 스토아에서 반품 승인을 한 후인 반품 승인 상태에서 제공한다
		//따라서 반품 승인 상태에서 일부 정보를 Update를 하기 위해 만든 함수.
		try {
		
			if(!claimVO.getPaOrderGb().equals("30") || !claimVO.getOdPrgsStepCd().equals("27")) return;
			
			paLtonClaimDAO.updateTPaLtonClaimList(claimVO);
				
		}catch (Exception e) {
			log.error("롯데온 반품 접수 - API 정보 갱신 실패 ::::" + claimVO.getOdNo() +" / " + claimVO.getOdSeq() +"/ " + PaLtonComUtill.getErrorMessage(e));
		}		
	}
	
	private int insertPaOrderm(AbstractModel abstractVo, Paorderm paorderm) throws Exception {
		if(paorderm == null) paorderm = new Paorderm();
		
		Map<String, String> voMap  = null;
		
		if(abstractVo instanceof PaLtonCancelListVO) { 
			voMap = BeanUtils.describe(((PaLtonCancelListVO)abstractVo));	
			voMap.put("claimQty", voMap.get("cnclQty"));	 //취소수량
			
		}else {
			voMap = BeanUtils.describe(((PaLtonClaimListVO)abstractVo));	
			voMap.put("outClaimGb", "0");
			
			if("30".equals(voMap.get("paOrderGb")) || "31".equals(voMap.get("paOrderGb"))) {
				voMap.put("claimQty", voMap.get("rtngQty")); //반품수량
			}else {
				voMap.put("claimQty", voMap.get("xchgQty")); //교환수량
			}
		}
		
		if ("20".equals(voMap.get("paOrderGb")) && "21".equals(voMap.get("odPrgsStepCd"))) { // 24.09.25 당사 배송 상품 직권취소시 출고전 반품 완료가 아닌 접수로 생성
			String delyType = "";
			int doFlag = 0;
			delyType = paLtonClaimDAO.selectGoodsDelyType(voMap);
			doFlag = paLtonClaimDAO.selectOutClaimGb(voMap);
			if ("10".equals(delyType) && doFlag >= 30) {
				voMap.put("outClaimGb", "0");
				voMap.put("remark6V", "1");
			}
		}
				
		int executedRtn   			= 0;
		String sysdate	  			= DateUtil.getCurrentDateTimeAsString();
		String paOrderGb			= voMap.get("paOrderGb");
		String outClaimGb			= voMap.get("outClaimGb");
		String claimStep			= voMap.get("claimStep");
		String odPrgsStepCd			= voMap.get("odPrgsStepCd");
		
		paorderm.setPaGroupCode		(Constants.PA_LTON_GROUP_CODE);
		paorderm.setPaOrderGb	 	(paOrderGb);
		paorderm.setPaCode			(voMap.get("paCode"));
		
		paorderm.setPaOrderNo	 	(voMap.get("odNo"));
		paorderm.setPaOrderSeq	 	(voMap.get("odSeq"));
		paorderm.setPaClaimNo	 	(voMap.get("clmNo"));
		paorderm.setPaShipNo		(voMap.get("procSeq"));
		paorderm.setPaShipSeq		(voMap.get("orglProcSeq"));
		paorderm.setPaProcQty	 	(String.valueOf(voMap.get("claimQty")));
		paorderm.setRemark6V		(voMap.get("remark6V"));
		
		if("0".equals(outClaimGb)){
			if("30".equals(paOrderGb) || "40".equals(paOrderGb) || "45".equals(paOrderGb)) { //반품,교환은 do_flag = 10으로 한 후 승인 후 20으로 업데이트
				if("30".equals(odPrgsStepCd)) {
					//구배확정후 취소 데이터 처리
					paorderm.setPaDoFlag("60");
				} else {
					paorderm.setPaDoFlag("20"); // 반품,교환 요청단계 사라짐 - 승인 불필요
				}
			}else {
				paorderm.setPaDoFlag("20");			
			}			
		}else{
			paorderm.setPaDoFlag("60");
		}
		
		paorderm.setOutBefClaimGb	(outClaimGb);
		paorderm.setInsertDate		(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyDate		(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyId		(Constants.PA_LTON_PROC_ID);
		
		//= TPAORDERM INSERT
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		return executedRtn;
	}
	
	
	private void insertPaorderMForExchange(PaLtonClaimListVO claimlist) throws Exception{

		Paorderm paorderm 					 = new Paorderm();
		int goodsCnt 						 = 0;
		ParamMap paramMap					 = null;
		PaLtonClaimListVO exchangeVO 		 = new PaLtonClaimListVO();
		
		List<Map<String,Object>> exchangeList =  paLtonClaimDAO.getPaLtonClaimList4Exchange(claimlist);
		
		//PAORDERM의 트랜잭션을 위해 PALTONCLAIMLIST의 40,45(41,46) 데이터가 모두 INSERT 됬을때 해당 로직을 진행한다.
		if(exchangeList.size() != 2) return;
		PaLtonComUtill.replaceCamelList(exchangeList);
		
		for(Map<String, Object> exchange :exchangeList) {
			exchangeVO = (PaLtonClaimListVO) PaLtonComUtill.map2VO(exchange, PaLtonClaimListVO.class); 
			exchangeVO.setPaCode(claimlist.getPaCode());
			switch (exchangeVO.getPaOrderGb()) {
			case "40":case "45": 	
					paramMap = new ParamMap();
					paramMap.put("paCode"	, exchangeVO.getPaCode());
					paramMap.put("goodsCode", exchangeVO.getGoodsCode());
								
					goodsCnt = paLtonClaimDAO.selectGoodsdtInfoCount(paramMap); //롯데ON 은 TPALTONGOODSDTMAPPING 테이블에 따로 관리
					
					if(goodsCnt > 1) { //단품이 여러개이면 텍스트 보고 상담원이 직접 입력
						paorderm.setChangeFlag		  ("00"); 
						paorderm.setChangeGoodsCode   (exchangeVO.getGoodsCode());
						paorderm.setChangeGoodsdtCode ("");
					}else {
						paorderm.setChangeFlag		  ("01"); 
						paorderm.setChangeGoodsCode   (exchangeVO.getGoodsCode());
						paorderm.setChangeGoodsdtCode (exchangeVO.getGoodsdtCode());
					}
					break;					
			}
			paorderm.setPaCode		(claimlist.getPaCode());
			
			insertPaOrderm(exchangeVO, paorderm);
		}	
	}	

	@Override
	public List<Map<String, Object>> selectPaLtonOrderCancelList() throws Exception {
		return paLtonClaimDAO.selectPaLtonOrderCancelList();
	}

	@Override
	public List<Map<String, Object>> selectPaLtonCancelApprovalList(ParamMap paramMap) throws Exception {
		return paLtonClaimDAO.selectPaLtonCancelApprovalList(paramMap);
	}

	@Override
	public int updateProcFlag(Map<String, Object> cancelMap) throws Exception {
		return paLtonClaimDAO.updateProcFlag(cancelMap);
	}

	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paLtonClaimDAO.selectCancelInputTargetDtList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectPaLtonOrderCancel(ParamMap paramMap) throws Exception {
		return paLtonClaimDAO.selectPaLtonOrderCancel(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectPaLtonCancelRefusalList(ParamMap apiDataMap) throws Exception {
		return paLtonClaimDAO.selectPaLtonCancelRefusalList(apiDataMap);
	}

	@Override
	public List<Map<String, Object>> selectPaLtonReturnExchangeApprovalList(ParamMap paramMap) throws Exception {
		return paLtonClaimDAO.selectPaLtonReturnExchangeApprovalList(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectPaLtonReturnExchangeApprovalDtList(ParamMap apiDataMap) throws Exception {
		return paLtonClaimDAO.selectPaLtonReturnExchangeApprovalDtList(apiDataMap);
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paLtonClaimDAO.selectClaimTargetList(paramMap);
	}

	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return paLtonClaimDAO.selectOrderCalimTargetDt30List(paramMap);
	}

	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paLtonClaimDAO.selectOrderCalimTargetDt20List(paramMap);

	}

	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paLtonClaimDAO.compareAddress(paramMap);
	}

	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paLtonClaimDAO.selectClaimCancelTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paLtonClaimDAO.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paLtonClaimDAO.selectChangeCancelTargetDtList(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectReturnSlipProcList() throws Exception {
		return paLtonClaimDAO.selectReturnSlipProcList();
	}

	@Override
	public List<Map<String, Object>> selectExchangeSlipProcList() throws Exception {
		return paLtonClaimDAO.selectExchangeSlipProcList();
	}

	@Override
	public List<Map<String, Object>> selectExchangeSendList() throws Exception {
		return paLtonClaimDAO.selectExchangeSendList();
	}

	@Override
	public List<Map<String, Object>> selectExchangeCompleteList() throws Exception {
		return paLtonClaimDAO.selectExchangeCompleteList();
	}

	@Override
	public List<Map<String, Object>> selectPaLtonExchangeRefuseList() throws Exception {
		return paLtonClaimDAO.selectPaLtonExchangeRefuseList();
	}

	@Override
	public int updateTPaOrderM4ChangeRefualReuslt(ParamMap paramMap) throws Exception {
		return paLtonClaimDAO.updateTPaOrderM4ChangeRefualReuslt(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectPaLtonExchangeRefuseDtList(ParamMap apiDataMap) throws Exception {
		return paLtonClaimDAO.selectPaLtonExchangeRefuseDtList(apiDataMap);
	}

	@Override
	public int saveLtonNotReceiveList(PaLtonNotReceiveList notReceiveVO) throws Exception {
		ParamMap paramMap 		= new ParamMap();
		String   sysdate 		= DateUtil.getCurrentDateTimeAsString();
		int      executedRtn	= 0;
		
		//=ParamMap Setting
		paramMap.put("paCode"			, notReceiveVO.getPaCode());
		paramMap.put("odNo"				, notReceiveVO.getOdNo());
		paramMap.put("odSeq"			, notReceiveVO.getOdSeq());
		paramMap.put("procSeq"			, notReceiveVO.getProcSeq());
		paramMap.put("nrcptDeclAccpNo"	, notReceiveVO.getNrcptDeclAccpNo());
		paramMap.put("nrcptDeclCnts"	, notReceiveVO.getNrcptDeclCnts());
		
		int existCount = paLtonClaimDAO.selectTPaLtonNotReceiveListExist(paramMap);
		if(existCount > 0)  return 0;

		//=INSERT TPALTONNOTRECEIVELIST
		executedRtn = paLtonClaimDAO.insertTPaltonNotReceiveList(notReceiveVO);		
		if(executedRtn == 0)  throw processException("msg.cannot_save",new String[] { "TPALTONNOTRECEIVELIST insert" }); 
			
		//=INSERT TCUSTCOUNSELM, TCUSTCOUNSELDT
		insertCustCounselm  (paramMap   , sysdate);
		insertCustCounseldt (paramMap   , sysdate);
		
		//=UPDATE TPALTONNOTRECEIVERLIST.COUNSEQL_SEQ
		executedRtn = paLtonClaimDAO.updateTPaLtonNotReceiveList(paramMap);
		if(executedRtn == 0)  throw processException("msg.cannot_save",new String[] { "TPALTONNOTRECEIVELIST update" }); 
		
		return executedRtn ;
	}
	
	
	@SuppressWarnings("unchecked")
	private void insertCustCounselm(ParamMap paramMap, String sysdate) throws Exception{

		String counsel_seq 						= null; 
		String ref_id 							= "";
		int executedRtn 						= 0;
		Map<String , String> custOrderinfoMap	= new HashMap<String, String>();
		Custcounselm custcounselm 				= new Custcounselm();

		
		counsel_seq = systemProcess.getSequenceNo("COUNSEL_SEQ");
		if (counsel_seq.equals(""))  throw processException("msg.cannot_create", new String[] { "COUNSEL_SEQ" });
		paramMap.put("counselSeq", counsel_seq);
		
		//ref_id = ComUtil.NVL(PaltonClaimproc.selectCustRefId(param));
		custOrderinfoMap = paLtonClaimDAO.selectCustInfoForCustCounsel(paramMap);
		if(custOrderinfoMap ==null || custOrderinfoMap.size() == 0) throw processException("selectCustInfoForCustCounsel - NULL(0)");
		custOrderinfoMap = (Map<String, String>) PaLtonComUtill.replaceCamel(custOrderinfoMap);
		
		custcounselm = (Custcounselm)PaLtonComUtill.map2VO(custOrderinfoMap, Custcounselm.class); //DB에서 가져온 값 자동세팅
		
		custcounselm.setCounselSeq		(counsel_seq);
		custcounselm.setClaimNo			(counsel_seq);
		custcounselm.setRefNo1			(custOrderinfoMap.get("orderNo"));
		custcounselm.setDoFlag			("10");
		custcounselm.setOutLgroupCode	("81");
		custcounselm.setOutMgroupCode	("10");
		custcounselm.setOutSgroupCode	("");
		custcounselm.setTel3			("");
		custcounselm.setWildYn			("0");
		custcounselm.setQuickEndYn		("0");
		custcounselm.setQuickYn			("0");
		custcounselm.setHcReqDate		(null);
		custcounselm.setRemark			("");
		custcounselm.setRefId1			(ref_id);
		custcounselm.setCsSendYn		("0");
		custcounselm.setCounselMedia	("61");		
		custcounselm.setCsLgroup		("60");
		custcounselm.setCsMgroup		("08");
		custcounselm.setCsSgroup		("90");
		custcounselm.setCsLmsCode		("600890");
		custcounselm.setInsertId		(Constants.PA_LTON_PROC_ID);
		custcounselm.setInsertDate		(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		custcounselm.setProcId			(Constants.PA_LTON_PROC_ID);
		custcounselm.setProcDate		(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
				
		executedRtn = paclaimDAO.insertCounselCustcounselm(custcounselm);
		
		if (executedRtn != 1) throw processException("msg.cannot_save",new String[] { "Custcounselm insert" });
	}
	
	private void insertCustCounseldt(ParamMap paramMap, String sysdate) throws Exception{

		String counsel_seq = paramMap.get("counselSeq").toString();
		if(counsel_seq ==null || counsel_seq.equals("")) throw processException("errors.db.no.seq_no" ,new String[] { "Counsel_seq"});
		String counselProcNote = ComUtil.NVL(paramMap.get("nrcptDeclCnts")).toString(); 
		
		Custcounseldt custcounseldt = new Custcounseldt();
		int executedRtn 			= 0;
		
		custcounseldt.setCounselSeq		(counsel_seq);
		custcounseldt.setCounselDtSeq	("100");
		custcounseldt.setDoFlag			("10");
		custcounseldt.setTitle			("");
		custcounseldt.setDisplayYn		("");
		custcounseldt.setProcNote		("상세사유 : " + counselProcNote);
		custcounseldt.setProcId			(Constants.PA_LTON_PROC_ID);
		custcounseldt.setProcDate		(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
				
		executedRtn = paclaimDAO.insertCounselCustcounseldt(custcounseldt);
		
		if (executedRtn != 1) throw processException("msg.cannot_save",new String[] { "TCUSTCOUNSELDT insert" });
	}

	@Override
	public List<Map<String, Object>> selectWithDrawNotReceiveList() throws Exception {
		return paLtonClaimDAO.selectWithDrawNotReceiveList();
	}

	@Override
	public int updateNotReceiveList4WithDraw(ParamMap apiDatMap) throws Exception {
		return paLtonClaimDAO.updateNotReceiveList4WithDraw(apiDatMap);
	}

	@Override
	public int updatePaOrderm4PreChangeCancel(Map<String, Object> preCancelMap) throws Exception {
		return paLtonClaimDAO.updatePaOrderm4PreChangeCancel(preCancelMap);
	}
	
	@Override
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("changeFlag", changeFlag);
		paramMap.put("mappingSeq", mappingSeq);
		return paLtonClaimDAO.updatePaOrdermChangeFlag(paramMap);
	}

	@Override
	public int selectConfirmCancelQty(ParamMap paramMap) throws Exception {
		return paLtonClaimDAO.selectConfirmCancelQty(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> selectPaLtonCancelApprovalListBO(ParamMap paramMap) throws Exception {
		return paLtonClaimDAO.selectPaLtonCancelApprovalListBO(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception {
		return paLtonClaimDAO.selectPaMobileOrderAutoCancelList();
	}
	
}
