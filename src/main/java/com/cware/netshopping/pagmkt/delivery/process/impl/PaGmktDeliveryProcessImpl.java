package com.cware.netshopping.pagmkt.delivery.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Custcounseldt;
import com.cware.netshopping.domain.model.Custcounselm;
import com.cware.netshopping.domain.model.PaGmkDeliveryComplete;
import com.cware.netshopping.domain.model.PaGmkNotRecive;
import com.cware.netshopping.domain.model.PaGmkOrder;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.claim.repository.PaClaimDAO;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pacommon.order.process.PaOrderProcess;
import com.cware.netshopping.pagmkt.delivery.process.PaGmktDeliveryProcess;
import com.cware.netshopping.pagmkt.delivery.repository.PaGmktDeliveryDAO;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;
import com.cware.netshopping.pagmkt.util.rest.PaGmktDeliveryRest;

@Service("pagmkt.delivery.PaGmktDeliveryProcess")
public class PaGmktDeliveryProcessImpl extends AbstractService implements PaGmktDeliveryProcess{

	@Resource(name = "pagmkt.delivery.PaGmktDeliveryDAO")
	private PaGmktDeliveryDAO PaGmktDeliveryDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO PaCommonDAO;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	@Resource(name="common.system.systemProcess")
	private SystemProcess systemProcess;
	
	@Resource(name = "pacommon.claim.paclaimDAO")
    private PaClaimDAO paclaimDAO;

	@Resource(name = "pacommon.order.paorderProcess")
	private PaOrderProcess paorderprocess;
	
	@Override
	public List<Map<String,String>> selectDeliveryCompleteList(ParamMap paramMap) throws Exception{	    	   
	    return PaGmktDeliveryDAO.selectDeliveryCompleteList(paramMap); 
	}
	
	@Override
	public int updateDeliveryCompleteProc(Map<?, ?> rtnMap) throws Exception{	    	    
	    return PaGmktDeliveryDAO.updateDeliveryCompleteProc(rtnMap); 
	}
	
	@Override
	public HashMap<String, String> selectTpaOrdermForNotReceive(Map<String,String> paramMap) throws Exception { 
		return (HashMap<String, String>) PaGmktDeliveryDAO.selectTpaOrdermForNotReceive(paramMap);
	}

	@Override
	public List<Map<String, String>> selectNotReceiveListForCancel(ParamMap paramMap) throws Exception{
		return PaGmktDeliveryDAO.selectNotReceiveListForCancel(paramMap); 
	}

	@Override
	public PaGmkNotRecive selectNotReceiveListDetail(Map<String, Object> paramMap) throws Exception {
		return PaGmktDeliveryDAO.selectNotReceiveListDetail(paramMap); 
	}

	@Override
	public int updateUnReceiveListForCancle(Map<String, Object> paramMap) throws Exception {
		return PaGmktDeliveryDAO.updateUnReceiveListForCancle(paramMap); 
	}

	@Override
	public int updateTpaGmktShippingList(ParamMap paramMap) throws Exception {
		return PaGmktDeliveryDAO.updateTpaGmktShippingList(paramMap); 
	}

	@Override
	public List<Map<String, Object>> selectChangingDelyList(ParamMap paramMap) throws Exception {
		return PaGmktDeliveryDAO.selectChangingDelyList(paramMap);
	}
	
	@Override
	public List<Map<String,Object>> selectSlipOutProcList(ParamMap paramMap) throws Exception{	    	    
	    return PaGmktDeliveryDAO.selectSlipOutProcList(paramMap); 
	}
	
	@Override
	public int updateSlipOutProc(Map<?,?> slipOut) throws Exception{	    	    
	    return PaGmktDeliveryDAO.updateSlipOutProc(slipOut); 
	}
	
	@Override
	public int updatePaOrderMFail(Map<?,?> slipOutFail) throws Exception{	 
		return PaGmktDeliveryDAO.updatePaOrderMFail(slipOutFail); 	    
	}
	
	@Override
	public int updateRemark1TpaorderM(Map<String, Object> delyMap)	throws Exception {
		return PaGmktDeliveryDAO.updateRemark1TpaorderM(delyMap);
	}
	
	@Override
	public int checkPaGmktOrderList(PaGmkOrder paGmktOrder) throws Exception {
		return PaGmktDeliveryDAO.checkPaGmktOrderList(paGmktOrder);
	}
	

	@Override
	public int updateTpaordermForCompleteOrderList(PaGmkOrder paGmktOrder) throws Exception {
		return PaGmktDeliveryDAO.updateTpaordermForCompleteOrderList(paGmktOrder);
	}
	
	@Override
	public int updateDeliveryCompleteList(List<PaGmkDeliveryComplete> paGmkDeliveryCompleteList) throws Exception{	    
	    
	    PaGmkDeliveryComplete paGmkDeliveryComplete = null;
	    
	    int excuteCnt,totCnt = 0;
	    
	    
	    for(int i = 0; i<paGmkDeliveryCompleteList.size(); i++){
		
		paGmkDeliveryComplete = paGmkDeliveryCompleteList.get(i);
		excuteCnt = PaGmktDeliveryDAO.updateDeliveryCompleteList(paGmkDeliveryComplete);
		totCnt = totCnt+excuteCnt;
	    }
	    
	    return totCnt;
	}
	
	@Override
	public int insertPaGmktOrderList(PaGmkOrder paGmkOrder) throws Exception{
		Paorderm paorderm = null;
		int executedRtn = 0;
		int checkPaGmktOrder = 0;
			
		checkPaGmktOrder = PaGmktDeliveryDAO.checkPaGmktOrderList(paGmkOrder); //= tpagmktorderlist Check				
		//checkUnpaidPaGmktOrderList
		if(checkPaGmktOrder > 0) return 0;
	
		executedRtn = PaGmktDeliveryDAO.insertPaGmktOrderList(paGmkOrder); //= tpagmktorderlist insert					
				
		if(executedRtn != 1){
		   log.error("PayNo : " + paGmkOrder.getPayNo() + ", ContrNo : " + paGmkOrder.getContrNo());
	       throw processException("msg.cannot_save", new String[] { "TPAGMKTORDERLIST INSERT" });
	    }
						
		paorderm = new Paorderm();		
		paorderm = setPaOrderm(paGmkOrder);
		executedRtn = PaCommonDAO.insertPaOrderM(paorderm); //= tpaorderm insert
						    			    
		if(executedRtn != 1){
			log.error("pagmkt tpaorderm insert fail");
			log.error("PackNo : " + paGmkOrder.getPayNo() + ", ContrNo : " + paGmkOrder.getContrNo());
		   	throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		 }
	
	    return executedRtn;
	}
	
	//이런 함수도 나중에 다 공통으로 해야함..
	private Paorderm setPaOrderm(PaGmkOrder paGmkOrder){
		Paorderm paorderm = new Paorderm();
		
		paorderm.setPaOrderGb		("10");
		paorderm.setPaCode			(paGmkOrder.getPaCode());  
		paorderm.setPaGroupCode		(paGmkOrder.getPaGroupCode());
		paorderm.setPaOrderNo		(paGmkOrder.getPayNo());
		paorderm.setPaOrderSeq		(paGmkOrder.getContrNo());
		paorderm.setPaShipNo		("");
		paorderm.setPaProcQty		(String.valueOf(paGmkOrder.getContrQty()));
		paorderm.setPaDoFlag		(paGmkOrder.getPaDoFlag());
		paorderm.setOutBefClaimGb	("0");
		paorderm.setInsertDate		(paGmkOrder.getInsertDate());
		paorderm.setModifyDate		(paGmkOrder.getModifyDate());
		paorderm.setModifyId		(paGmkOrder.getInsertId());
		
		return paorderm;			
	}

	@Override
	public void insertPaGmktNotReceiveList(PaGmkNotRecive notReceive) throws Exception { 
		
		if(notReceive == null) return;
		
		int executedRtn = 0;
		int count = 0;
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("paGroupCode", notReceive.getPaGroupCode().toString());
		param.put("paCode"	   , notReceive.getPaCode().toString());
		param.put("payNo"	   , notReceive.getPayNo().toString());
		param.put("contrNo"	   , notReceive.getContrNo().toString());
		param.put("claimDate"  , DateUtil.toTimestamp(notReceive.getClaimDate().toString(), "yyyy-MM-dd HH:mm:ss"));
		param.put("claimReason", notReceive.getClaimReasonDetail());
		param.put("insertId"   , notReceive.getInsertId().toString());	
		count  = PaGmktDeliveryDAO.selectPagmktNotReceiveListExist(param);
		
		if(count > 0 ) return;
		if(notReceive.getClaimSolveDate() != null && !(notReceive.getClaimSolveDate().equals(""))) return;

		
		executedRtn = PaGmktDeliveryDAO.insertPaGmktNotReceiveList(notReceive);
		if(executedRtn < 0) throw processException("msg.cannot_save",new String[] { "PaGmktNotReceiveList insert" });			
		createCounselInfo(param);
		updateTpagmktNotReceiveList(param);
		
	}
	

	@Override
	public int sendShipping(PaGmktAbstractRest rest, List<Map<String, Object>> slipOutProcList, ParamMap paramMap) throws Exception {
	
		if( rest == null)	 rest = new PaGmktDeliveryRest();
		
		int successCounts = 0;
		int successCount  = 0;
		
		for(Map<String,Object> slipOutProc : slipOutProcList){
		
			try{
				getConnectionForShipping(slipOutProc, rest, paramMap);
				successCount = updateTpaOrderM(slipOutProc);
				
				//운송장 변경
				insertTpaSlipInfo(slipOutProc, "1", "운송장 등록 성공");
				
			}catch(Exception e){ //response error
				
				if("03".equals(slipOutProc.get("PA_GROUP_CODE").toString())){ //Auction의 경우 운송장 체크를 하기때문에 기타로 한번 더 보낸다.
					successCount = reSendShipping(rest, slipOutProc, paramMap);
					//운송장 변경
					insertTpaSlipInfo(slipOutProc, "1", "운송장 재 연동 성공(Auction)");
					continue;
				}
				log.error("SendShippingError : " + e.getMessage());
				updateSlipOutProcFail(slipOutProc, paramMap, "jwt - Connect 통신 에러 : " + e.getMessage());			
				
				//운송장 변경
				//insertTpaSlipInfo(slipOutProc, "0", "운송장 등록 실패");
				 
				continue;
			}
			successCounts += successCount;		
		}//end of For
	
		return successCounts;
	}
	
	private int reSendShipping(PaGmktAbstractRest rest, Map<String,Object> slipOutProc, ParamMap paramMap) throws Exception {
		int successCount = 0;
		try{
			slipOutProc.put("PA_GROUP_CODE"		, "03");
			slipOutProc.put("PA_DELY_GB_ORG"	, slipOutProc.get("PA_DELY_GB"));
			slipOutProc.put("INVOICE_NO_ORG"	, slipOutProc.get("INVOICE_NO"));
			slipOutProc.put("PA_DELY_GB"		, "10034");  //Gmarket 10032 ,Auction  10034, G9 ????     10070이었는데 지옥의 이베인놈들이 10034로 임의로 바꿈
			slipOutProc.put("EXPRESS_NAME"		, "기타");
			slipOutProc.put("INVOICE_NO"		, slipOutProc.get("SLIP_I_NO").toString());  //실패시 송장은 SLIP_I_NO로 보냄
			slipOutProc.put("REMARK1_V"			, "기타택배(자체배송) 재연동");
			slipOutProc.put("TRANS_YN"			, "1");
			getConnectionForShipping(slipOutProc, rest, paramMap);
			successCount = updateTpaOrderM(slipOutProc);
			
		}catch(Exception e){
			updateSlipOutProcFail(slipOutProc, paramMap, "jwt - Connect 통신 에러 : " + e.getMessage());		
			slipOutProc.put("REMARK1_V"			, "기타택배(자체배송) 재연동 실패");
			slipOutProc.put("TRANS_YN"			, "0");
		}finally {
			paorderprocess.insertTpaSlipInfo(slipOutProc);
		}
		return successCount;
	}
	
	
	@Override
	public void updateReceiverInfo(PaGmkOrder paGmkOrder) throws Exception {
	   
		int executedRtn = PaGmktDeliveryDAO.updateReceiverInfo(paGmkOrder); //= tpaorderm insert
		
		if(executedRtn  < 1) {
		    throw processException("errors.process", new String[] { "UPDATE TPAGMKTORDERLIST -updateReceiverInfo 오류 발생" });
		}
	}

	
	private String getConnectionForShipping( Map<String,Object> SlipMap, PaGmktAbstractRest rest, ParamMap paramMap) throws Exception{

		paramMap.put("ORDER_NO"		, SlipMap.get("CONTR_NO"));		
		paramMap.put("DELY_DATE"	, SlipMap.get("SLIP_PROC_DATE"));
		paramMap.put("DELY_COMPANY"	, SlipMap.get("PA_DELY_GB"));
		paramMap.put("DELY_NO"		, SlipMap.get("INVOICE_NO"));
		paramMap.put("paCode"		, SlipMap.get("PA_CODE"));
		
		if(!checkValiationForDelyList(paramMap)) return "";
	
		return restUtil.getConnection(rest,  paramMap);
	}
	
	//= 통신의 결과가 성공하면 TPAORDERM의 PA_DO_FLAG     = '40' /*배송중(출고완료)*/ 로 만들어준다.   실패시 TPAORDERM의 RESULT_CODE 값 변경
	private int updateTpaOrderM(Map<String,Object> slipOutProc) throws Exception{
			
		int excuteCnt = 0;
					      
		if( "10032".equals(slipOutProc.get("PA_DELY_GB")) ||"10034".equals(slipOutProc.get("PA_DELY_GB")) ||"10070".equals(slipOutProc.get("PA_DELY_GB"))  ) {
			slipOutProc.put("API_RESULT_MESSAGE", "출고처리성공(기타택배)");
		}else {
			slipOutProc.put("API_RESULT_MESSAGE", "출고처리성공");
		}
		
		excuteCnt = updateSlipOutProc(slipOutProc);
				
		if(excuteCnt != 1) {
		    throw processException("errors.process", new String[] { "TPAORDERM SUCCESS-UPDATE 오류 발생" });
		}
		return excuteCnt;
	}
		
		
	private void updateSlipOutProcFail(Map<String,Object> slipOutProc, ParamMap paramMap, String errMessage) throws Exception{
		int excuteCnt =  0;
			
		errMessage = errMessage.length() > 3950 ? errMessage.substring(0, 3950) : errMessage;
			
		Map<String,Object> slipOutprocFail = new HashMap<String,Object>();
		slipOutprocFail.put("paCode"		, slipOutProc.get("PA_CODE"));
		slipOutprocFail.put("paGroupCode"	, slipOutProc.get("PA_GROUP_CODE"));
		slipOutprocFail.put("payNo"			, slipOutProc.get("PAY_NO"));
		slipOutprocFail.put("contrNo"		, slipOutProc.get("CONTR_NO"));
		slipOutprocFail.put("message"		, errMessage);
		//slipOutprocFail.put("apiCode", paramMap.getString("apiCode"));

		excuteCnt = updatePaOrderMFail(slipOutprocFail);
		if(excuteCnt != 1) {
			log.error("SendShippingError : " + "SengShipping- TPAORDERM FAIL-UPDATE 오류 발생");
		    throw processException("errors.process", new String[] { "TPAORDERM FAIL-UPDATE 오류 발생" });
		}					
	}
		

	private boolean checkValiationForDelyList(ParamMap param){
			
		if(param == null) return false;
			
		boolean checkFalg = true;
		
		if(param.getString("ORDER_NO").equals("")){
			checkFalg = false;
		}else if(param.getString("DELY_DATE").equals("")){
			checkFalg = false;
		}else if(param.getString("DELY_COMPANY").equals("")){
			checkFalg = false;
		}else if(param.getString("DELY_COMPANY").equals("")){
			checkFalg =  false;
		}
			
		if(param.getString("DELY_NO").equals("")){
			if(param.getString("DELY_COMPANY").equals(ConfigUtil.getString("PAGMK_DEFAULT_DELY_GB"))){
				param.put("DELY_NO", "1234567890");  
			}
		}
		
		return checkFalg;
	}
		
	
	private void updateTpagmktNotReceiveList(Map<String, Object> paramMap) throws Exception{
		
		int  executedRtn = 0; 
		executedRtn = PaGmktDeliveryDAO.updateTpagmktNotReceiveList(paramMap);
		if (executedRtn != 1) throw processException("msg.cannot_save",new String[] { "Custcounselm insert" });	
		
	}
	
	private void createCounselInfo(Map<String, Object> param) throws Exception {
		
		String sysdate = DateUtil.getCurrentDateTimeAsString();

		param.put("dely_type", "10");
		param.put("out_lgroup_code", "81");
		param.put("out_mgroup_code", "10");
		
		insertCustCounselm (param, sysdate);
		insertCustCounseldt(param, sysdate);
	}
	
	private void insertCustCounselm(Map<String, Object> param, String sysdate) throws Exception{
		
		String counsel_seq = null; 
		String ref_id = null;
		int executedRtn = 0;
		Map<String , String > custOrderinfoMap = new HashMap<String, String>();
		Custcounselm custcounselm = new Custcounselm();

		
		counsel_seq = systemProcess.getSequenceNo("COUNSEL_SEQ");
		param.put("counselSeq", counsel_seq);
		if (counsel_seq.equals(""))  throw processException("msg.cannot_create", new String[] { "COUNSEL_SEQ" });
		
		ref_id = ComUtil.NVL(PaGmktDeliveryDAO.selectCustRefId(param));
		custOrderinfoMap = PaGmktDeliveryDAO.selectCustInfoForSettingCustCounsel(param);
		
		if(custOrderinfoMap ==null) throw processException("Error When get Infomation for custcounsel in order to set NotreceiveList");
		
		custcounselm.setCounselSeq(counsel_seq);
		custcounselm.setDoFlag("10");
		custcounselm.setCustNo(custOrderinfoMap.get("CUST_NO").toString());// 고객번호 확인
		custcounselm.setRefNo1(custOrderinfoMap.get("ORDER_NO").toString());
		custcounselm.setRefNo2(custOrderinfoMap.get("ORDER_G_SEQ").toString());
		custcounselm.setRefNo3(custOrderinfoMap.get("ORDER_D_SEQ").toString());
		custcounselm.setRefNo4(custOrderinfoMap.get("ORDER_W_SEQ").toString());
		custcounselm.setGoodsCode(custOrderinfoMap.get("GOODS_CODE").toString());
		custcounselm.setGoodsdtCode(custOrderinfoMap.get("GOODSDT_CODE").toString());

		custcounselm.setOutLgroupCode("81");
		custcounselm.setOutMgroupCode("10");
		custcounselm.setOutSgroupCode("");
		
		custcounselm.setClaimNo(counsel_seq);
		custcounselm.setTel(ComUtil.NVL(custOrderinfoMap.get("RECEIVER_HP").toString(),custOrderinfoMap.get("TEL").toString()));
		custcounselm.setDdd(ComUtil.NVL(custOrderinfoMap.get("RECEIVER_HP1").toString(),custOrderinfoMap.get("RECEIVER_DDD").toString()));
		custcounselm.setTel1(ComUtil.NVL(custOrderinfoMap.get("RECEIVER_HP2").toString(),custOrderinfoMap.get("RECEIVER_TEL1").toString()));
		custcounselm.setTel2(ComUtil.NVL(custOrderinfoMap.get("RECEIVER_HP3").toString(),custOrderinfoMap.get("RECEIVER_TEL2").toString()));
		custcounselm.setTel3("");
		custcounselm.setWildYn("0");
		custcounselm.setQuickEndYn("0");
		custcounselm.setQuickYn("0");
		custcounselm.setHcReqDate(null);
		custcounselm.setRemark("");
		custcounselm.setRefId1(ref_id);
		custcounselm.setCsSendYn("0");
		custcounselm.setCounselMedia("61");		
		custcounselm.setCsLgroup("60");
		if("03".equals(param.get("paGroupCode").toString())){
			custcounselm.setCsMgroup("03");
		}else{
			custcounselm.setCsMgroup("02");
		}
		custcounselm.setCsSgroup("90");
		custcounselm.setCsLmsCode(custcounselm.getCsLgroup() + custcounselm.getCsMgroup() + custcounselm.getCsSgroup());		
		custcounselm.setInsertId(param.get("insertId").toString());
		custcounselm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		custcounselm.setProcId(param.get("insertId").toString());
		custcounselm.setProcDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		
		
		executedRtn = paclaimDAO.insertCounselCustcounselm(custcounselm);
		
		if (executedRtn != 1) throw processException("msg.cannot_save",new String[] { "Custcounselm insert" });
				
	}
	
	private void insertCustCounseldt(Map<String, Object> param, String sysdate) throws Exception{

		String counsel_seq = param.get("counselSeq").toString();
		if(counsel_seq ==null || counsel_seq.equals("")) throw processException("errors.db.no.seq_no",new String[] { "Cant not make, Counsel_seq is null"});
		String counselProcNote = ComUtil.NVL(param.get("claimReason")).toString(); 
		
		Custcounseldt custcounseldt = new Custcounseldt();
		int executedRtn = 0;
		
		custcounseldt.setCounselSeq(counsel_seq);
		custcounseldt.setCounselDtSeq("100");
		custcounseldt.setDoFlag("10");
		custcounseldt.setTitle("");
		custcounseldt.setDisplayYn("");
		custcounseldt.setProcNote("상세사유 : " + counselProcNote);
		custcounseldt.setProcId(param.get("insertId").toString());
		custcounseldt.setProcDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
				
		executedRtn = paclaimDAO.insertCounselCustcounseldt(custcounseldt);
		
		if (executedRtn != 1) throw processException("msg.cannot_save",new String[] { "TCUSTCOUNSELDT insert" });
	}

	@Override
	public int updatePayDate(PaGmkOrder paGmktOrder) throws Exception {
		int executedRtn = PaGmktDeliveryDAO.updatePayDate(paGmktOrder); //= tpagmktorderlist insert					
		if(executedRtn != 1){
		   log.error("PayNo : " + paGmktOrder.getPayNo() + ", ContrNo : " + paGmktOrder.getContrNo());
	       throw processException("msg.cannot_save", new String[] { "TPAGMKTORDERLIST UPDATE" });
	    }
		return executedRtn;
	}

	@Override
	public int checkUnpaidPaGmktOrderList(PaGmkOrder paGmktOrder) throws Exception {
		return PaGmktDeliveryDAO.checkUnpaidPaGmktOrderList(paGmktOrder);	
	}

	@Override
	public int sendShipping4ChangeInvoice(PaGmktAbstractRest rest, List<Map<String, Object>> slipChangeProcList, ParamMap paramMap) throws Exception {
		if( rest == null)	 rest = new PaGmktDeliveryRest();
		int rtnCnt = 0; //일단 처리하지않음
		
		for(Map<String,Object> slipOutProc : slipChangeProcList){
			
			try{
				getConnectionForShipping(slipOutProc, rest, paramMap);
				insertTpaSlipInfo(slipOutProc ,"1" ,"운송장 변경 성공");
				
			}catch(Exception e){ //response error
				insertTpaSlipInfo(slipOutProc ,"0" ,"운송장 변경 재연동 에러 / 변경 운송장 보내지 않음");
				continue;	
			}
		}//end of For
	
		return rtnCnt;
	}
	
	private void insertTpaSlipInfo(Map<String,Object> slipOutProc , String transYn , String remark) {
		try {
			slipOutProc.put("PA_GROUP_CODE"		,  slipOutProc.get("PA_GROUP_CODE").toString());
			slipOutProc.put("PA_DELY_GB_ORG"	,  slipOutProc.get("PA_DELY_GB").toString());
			slipOutProc.put("INVOICE_NO_ORG"	,  slipOutProc.get("INVOICE_NO").toString());	
			slipOutProc.put("REMARK1_V"			,  remark);
			slipOutProc.put("TRANS_YN"			,  transYn);		
			paorderprocess.insertTpaSlipInfo(slipOutProc);
			
		}catch (Exception e) {
			log.error(e.toString());
		}

	}

	@Override
	public List<Map<String, Object>> selectSlipChangeProcList(ParamMap paramMap) throws Exception {
		return PaGmktDeliveryDAO.selectSlipChangeProcList(paramMap);
	}
	
}