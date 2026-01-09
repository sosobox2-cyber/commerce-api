package com.cware.netshopping.pagmkt.cancel.process.impl;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaGmkCancel;
import com.cware.netshopping.domain.model.PaGmkClaim;
import com.cware.netshopping.domain.model.PaGmkOrder;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pagmkt.cancel.process.PaGmktCancelProcess;
import com.cware.netshopping.pagmkt.cancel.repository.PaGmktCancelDAO;
import com.cware.netshopping.pagmkt.claim.repository.PaGmktClaimDAO;
import com.cware.netshopping.pagmkt.delivery.service.PaGmktDeliveryService;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.pagmkt.util.PaGmktDateUtil;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;

@Service("pagmkt.cancel.PaGmktCancelProcess")
public class PaGmktCancelProcessImpl extends AbstractService implements PaGmktCancelProcess{

	@Resource(name = "pagmkt.cancel.PaGmktCancelDAO")
	private PaGmktCancelDAO PaGmktCancelDAO;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pagmkt.delivery.PaGmktDeliveryService")
	private PaGmktDeliveryService paGmktDeliveryService;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	private PaGmktCommonUtil CommonUtil;
	
	@Resource(name = "pagmkt.claim.PaGmktClaimDAO")
	private PaGmktClaimDAO paGmktClaimDAO;
	
	@Override
	public List<Object> selectPaGmktOrdCancelList(ParamMap paramMap) throws Exception {
		return PaGmktCancelDAO.selectPaGmktOrdCancelList(paramMap);
	}
	@Override
	public List<Object> selectPaGmktOrdCancelListForBo() throws Exception {
		return PaGmktCancelDAO.selectPaGmktOrdCancelListForBo();
	}
	public HashMap<String, Object> selectPaGmktOrdCancel(HashMap<String,Object> paramMap) throws Exception{
		return PaGmktCancelDAO.selectPaGmktOrdCancel(paramMap);
	}

	@Override
	public int updatePaGmktOrderListProcFlag(HashMap<String, Object> cancelMap)	throws Exception {
		return PaGmktCancelDAO.updatePaGmktOrderListProcFlag(cancelMap);
	}

	@Override
	public int saveCancelConfirm(PaGmktAbstractRest rest , HashMap<String, Object> cancelMap ,ParamMap param) throws Exception {
		return confirmCancel(rest, cancelMap, param);
		
	}
	
	
	@Override
	public int saveCancelReqList(PaGmkCancel paGmktCancel) throws Exception {
		int executedRtn = 0;
		int checkPaGmktCancel = 0;
		
		checkPaGmktCancel = PaGmktCancelDAO.checkPaGmktCancelList(paGmktCancel);
			
		if(checkPaGmktCancel > 0 ) return 0; 
				
		executedRtn = PaGmktCancelDAO.insertPaGmktCancelList(paGmktCancel); //= tpagmktclaimlist insert
				
		if(executedRtn != 1){
			log.info("pagmkt tpagmktorderlist insert fail");
			log.info("PayNo : " + paGmktCancel.getPayNo() + ", ContrNo : " + paGmktCancel.getContrNo());
			throw processException("msg.cannot_save", new String[] { "TPAGMKTCANCELLIST INSERT" });
		}
		
		return executedRtn;
	}

	@Override
	public String saveCancelConfirmProc(HashMap<String, Object> cancelMap, PaGmktAbstractRest rest, ParamMap param) throws Exception {

		if(cancelMap.get("DO_FLAG") ==null  || cancelMap.get("DO_FLAG").equals("")) throw processException("ERROR"); 
		
		int doFlag = Integer.parseInt(cancelMap.get("DO_FLAG").toString());
		String status = null;
		String cancelDay = null;
		
		if(doFlag < 30){ //출하지시 이전은 취소 승인
			status = "CONFIRM";
		}else if(doFlag >= 30 && !cancelMap.get("SLIP_NO").toString().equals("")){ //출하지시 후 운송장 있는건은 취소 거부 
			status = "REFUSE";
		}else{
			status = "HOLD"; // 출하지시후 운송장 없을땐 일단 상담원을 통해 처리하기 위해 HOLD
		}
			
		switch(status){
		
		case "CONFIRM":
			confirmCancel(rest,cancelMap, param);
			break;
			
		case "REFUSE":
			refuseCancel(cancelMap);
			break;
			
			
		case "HOLD":	

			//옥션의 경우, 고객센터 업무시간 외 발생시 취소승인 (금 17시 ~ 일 24시)
			if (cancelMap.get("PA_GROUP_CODE").toString().equals("03")) {
				String orderMakeYn   = cancelMap.get("ORDER_MAKE_YN").toString();
				String installYn     = cancelMap.get("INSTALL_YN").toString();
				String returnNoYn    = cancelMap.get("RETURN_NO_YN").toString();
				String delyType      = cancelMap.get("DELY_TYPE").toString();
				Double salePrice     = Double.valueOf(cancelMap.get("SALE_PRICE").toString());
				double highSalePrice = 100000;

				String createDt = cancelMap.get("CREATE_DT").toString();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				
		        LocalDateTime claimDate = LocalDateTime.parse(createDt,formatter);
		        LocalDateTime friday = claimDate.with(DayOfWeek.FRIDAY).withHour(17).withMinute(0).withSecond(0);
		        LocalDateTime sunday = claimDate.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59).withSecond(59);

		        //예외조건 : 당사배송, 주문제작, 설치상품, 교환/반품불가 상품, 판매가 10만원 이상 고단가
		        if( !"10".equals(delyType) && "0".equals(orderMakeYn) && "0".equals(installYn) 
		        		&& "0".equals(returnNoYn) && salePrice < highSalePrice ) {		
		        	if(claimDate.isAfter(friday) && (claimDate.isBefore(sunday) || claimDate.isEqual(sunday))) {
		        		status = "CONFIRM";
		        		confirmCancel(rest,cancelMap, param); //자동승인 
		        		break;
		        	}
		        }
			}

			int businessDay = PaGmktCancelDAO.selectBusinessDayAccount(cancelMap);
			
			if(cancelMap.get("PA_GROUP_CODE").toString().equals("02")) {
				cancelDay = "GMKT_CANCEL_BUSINESS_DAY";
			} else if (cancelMap.get("PA_GROUP_CODE").toString().equals("03")) {
				cancelDay = "AUCT_CANCEL_BUSINESS_DAY";
			}
			
			if(businessDay < ConfigUtil.getInt(cancelDay)){
				return status;
			}
			
			if(!cancelMap.get("SLIP_NO").toString().equals("")){
				status = "REFUSE";
				refuseCancel(cancelMap);

			}else{
				status = "CONFIRM";
				confirmCancel(rest,cancelMap, param); //자동승인
			}
			
			break;

		}// end of switch ~ case
		
		return status;
	}
	
	@Override
	public int saveCancelWithdrawList(PaGmkCancel paGmktCancel) throws Exception {
		
		int checkPaGmktOrder = 0 ; 
		int executedRtn      = 0 ;
		
		checkPaGmktOrder = PaGmktCancelDAO.checkPaGmktCancelWithdawList(paGmktCancel);  // 취소거절 tpagmktclaimlist Check	
			
		if(checkPaGmktOrder <= 0) return 0;   //애당초 받지 않은 취소 철회건은 무시..
																
		executedRtn = PaGmktCancelDAO.updatePaGmktCancelListWithdraw(paGmktCancel); //= tpagmktclaimlist update
				
		if(executedRtn != 1){
			throw processException("msg.cannot_save", new String[] { "TPAGMKTCLAIMLIST UPDATE" });
		}
			
		return executedRtn;
	}

	private int confirmCancel(PaGmktAbstractRest rest , HashMap<String, Object> cancelMap ,ParamMap param) throws Exception{
		
		int  executedRtn = 0;
		int doFlag = 0;
		try{
			doFlag = Integer.parseInt(cancelMap.get("DO_FLAG").toString());

			param.put("urlParameter", cancelMap.get("PA_ORDER_SEQ"));
			param.put("paCode"     , cancelMap.get("PA_CODE"));	
			restUtil.getConnection(rest, param);

			
			cancelMap.put("PROC_FLAG","10");
			
			if(doFlag >= 30){
				cancelMap.put("OUT_CLAIM_GB", "1");
			}else{
				cancelMap.put("OUT_CLAIM_GB", "0");
			}
				
			insertPaOrderM(cancelMap);
				 			
		}catch (Exception e) {
			cancelMap.put("PROC_FLAG","90");				
		}finally{
			executedRtn = PaGmktCancelDAO.updatePaGmktCancelListProcFlag(cancelMap);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] { "TPAGMKTCLAIMLIST UPDATE" });
			}
		
		}
		
		return executedRtn;
	}
	
	
	private void refuseCancel(HashMap<String, Object> cancelMap) throws Exception{
		
		//hs.Baek - 취소거부는 발송처리하면 됨.실질적으로 G마켓 취소거부는 발송처리 API가 진행, 그때 돌때 G마켓 상태값은 취소 -> 배송중으로 수정됩니다.
		int executedRtn = 0;

		cancelMap.put("PROC_FLAG","20");
		executedRtn = PaGmktCancelDAO.updatePaGmktCancelListProcFlag(cancelMap);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAGMKTCLAIMLIST UPDATE" });
		}
	}
	
	public int saveCancelCompleteList(PaGmkCancel paGmktCancel) throws Exception{
		
		int executedRtn = 0;
		int checkPaGmktCancel = 0;
		int checkOrgOrderCnt  = 0;
		String delyType = PaGmktCancelDAO.selectDelyType(paGmktCancel.getContrNo());
		String doFlag = PaGmktCancelDAO.selectOrderdtDoFlag(paGmktCancel);
		String procFlag = PaGmktCancelDAO.selectPaGmktCancelProcFlag(paGmktCancel);

		//paGmktCancel.setProcNo("10");
		
		checkOrgOrderCnt = PaGmktCancelDAO.checkExistOrgOrder(paGmktCancel);
		if (checkOrgOrderCnt < 1) return 0;
		
		if( doFlag != null && Integer.parseInt(doFlag) >= 30 && "10".equals(delyType)) { //당사상품 & 출하지시 이후의 경우 반품데이터로 적재
			
			PaGmkClaim paGmktClaim = settingClaim(paGmktCancel);
			
			//= 1) check TpaGmktClaimList Exist or not
			checkPaGmktCancel = paGmktClaimDAO.checkPaGmktClaimList(paGmktClaim);
			if (checkPaGmktCancel > 0 || "10".equals(procFlag)) return 0;
			
			PaGmktCancelDAO.updatePaGmktCancelListWithdraw(paGmktCancel);

			//= 2) Insert TpaGmktClaimList 
			executedRtn = paGmktClaimDAO.insertPaGmktClaimList(paGmktClaim);
			
			if(executedRtn != 1){
				log.info("pagmkt tpagmktorderlist insert fail");
				log.info("PayNo : " + paGmktCancel.getPayNo() + ", ContrNo : " + paGmktCancel.getContrNo());
				throw processException("msg.cannot_save", new String[] { "TPAGMKTCLAIMLIST INSERT" });
			}
			
			//= 3) InsertPaOrderM
			insertPaorderMForReturn(paGmktClaim);
			
		} else {
			
			//= 1) check TpaGmktCancelList Exist or not
			checkPaGmktCancel = PaGmktCancelDAO.checkPaGmktCancelDoneList(paGmktCancel);
			if (checkPaGmktCancel > 0) return 0;
			PaGmktCancelDAO.updatePaGmktCancelListWithdraw(paGmktCancel);
			
			
			//= 2) Insert TpaCancelList 
			executedRtn = PaGmktCancelDAO.insertPaGmktCancelList(paGmktCancel);
			
			if(executedRtn != 1){
				log.info("pagmkt tpagmktorderlist insert fail");
				log.info("PayNo : " + paGmktCancel.getPayNo() + ", ContrNo : " + paGmktCancel.getContrNo());
				throw processException("msg.cannot_save", new String[] { "TPAGMKTCLAIMLIST INSERT" });
			}
			
			//= 3) InsertPaOrderM
			executedRtn = insertPaOrderMForComplete(paGmktCancel);
			
			if(executedRtn != 1){
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}
		}
		
			
		return executedRtn;
		
	}
	
	private PaGmkClaim settingClaim(PaGmkCancel paGmktCancel) throws Exception {
		String sysdate = systemService.getSysdatetimeToString();

		PaGmkClaim paGmkClaim = new PaGmkClaim();
		PaGmkOrder paGmktOrder = new PaGmkOrder();
		
		paGmktOrder = PaGmktCancelDAO.selectPaGmktOrderList(paGmktCancel);
		
		paGmkClaim.setPaGroupCode				(paGmktOrder.getPaGroupCode());
		paGmkClaim.setPaCode				 	(paGmktOrder.getPaCode());
		paGmkClaim.setPayNo						(paGmktCancel.getPayNo());
		paGmkClaim.setGroupNo					(paGmktCancel.getGroupNo());
		paGmkClaim.setContrNo					(paGmktCancel.getContrNo());
		paGmkClaim.setPaOrderGb					("30");
		paGmkClaim.setContrNoSeq				(CommonUtil.getMaxPaOrderWSeq(paGmkClaim.getPayNo(),paGmkClaim.getContrNo()));
		paGmkClaim.setGoodsNo					(paGmktCancel.getGoodsNo());
		paGmkClaim.setSiteGoodsNo				(paGmktCancel.getSiteGoodsNo());
		paGmkClaim.setRequestUser				(paGmktCancel.getRequestUser());
		paGmkClaim.setReason					(paGmktCancel.getReason());
		paGmkClaim.setReasonCode				("99"); 
		paGmkClaim.setReasonDetail				(paGmktCancel.getReasonDetail());
		paGmkClaim.setApproveUser				(paGmktCancel.getApproveUser());
		paGmkClaim.setOrderDate					(paGmktCancel.getOrderDate());
		paGmkClaim.setPayDate					(paGmktCancel.getPayDate());
		paGmkClaim.setRequestDate				(paGmktCancel.getRequestDate());
		paGmkClaim.setWithDrawDate				(paGmktCancel.getWithDrawDate());
		paGmkClaim.setSenderName				(paGmktOrder.getReceiverName());
		paGmkClaim.setSenderHpNo				(paGmktOrder.getReceiverMobile());
		paGmkClaim.setSenderTelNo				(paGmktOrder.getReceiverTel());
		paGmkClaim.setSenderZipCode				(paGmktOrder.getReceiverZipCode());
		paGmkClaim.setSenderAddr				(paGmktOrder.getReceiverAddress());
		paGmkClaim.setSenderAddr1				(paGmktOrder.getReceiverAddress1());
		paGmkClaim.setSenderAddr2				(paGmktOrder.getReceiverAddress2());
		paGmkClaim.setInsertDate				(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		paGmkClaim.setModifyDate				(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		paGmkClaim.setInsertId					(paGmktCancel.getInsertId());
		return paGmkClaim;
	}
	
	private void insertPaorderMForReturn(PaGmkClaim paGmktClaim) throws Exception{
		
		Paorderm paorderm = null;
		int executedRtn = 0;
		
		//= 3. TPORDERM
		paorderm = new Paorderm();
		
		paorderm.setPaCode			(paGmktClaim.getPaCode());
		paorderm.setPaGroupCode		(paGmktClaim.getPaGroupCode());
		paorderm.setPaOrderGb		("30");
		paorderm.setPaOrderNo		(paGmktClaim.getPayNo());
		paorderm.setPaOrderSeq		(paGmktClaim.getContrNo());
		paorderm.setPaClaimNo		(paGmktClaim.getContrNoSeq());
		paorderm.setPaProcQty		(paGmktClaimDAO.selectPaGmktClaimReqQty(paGmktClaim));
		
		paorderm.setPaDoFlag	("60"); //직권취소로 발생한 반품 케이스 
	
		
		paorderm.setOutBefClaimGb	("0");
		paorderm.setInsertDate		(paGmktClaim.getInsertDate());
		paorderm.setModifyDate		(paGmktClaim.getModifyDate());
		paorderm.setModifyId		(paGmktClaim.getInsertId());
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		
	}
	
	private int insertPaOrderMForComplete(PaGmkCancel cancel) throws Exception{
		
		HashMap<String, Object> cancelMap = new HashMap<String, Object>();
				
		cancelMap.put("paOrderNo"   , cancel.getPayNo());
		cancelMap.put("paOrderSeq"  , cancel.getContrNo());
		cancelMap.put("paClaimSeq"  , cancel.getContrNoSeq()); 
		cancelMap.put("paGroupCode" , cancel.getPaGroupCode()); 

		cancelMap = PaGmktCancelDAO.selectPaGmktOrdCancel(cancelMap);
		
		if(cancelMap == null) return 0; //Throw로 던질지, 0으로 처리할지 고민..
		if(cancelMap.size() ==0) return 0; //Throw로 던질지, 0으로 처리할지 고민..
		
		return insertPaOrderM(cancelMap);
	}
	
	private int insertPaOrderM(HashMap<String, Object> cancelMap) throws Exception{
		
		
		Paorderm paorderm = new Paorderm();
		String sysdate	  = DateUtil.getCurrentDateTimeAsString();
		String outClaimGb = cancelMap.get("OUT_CLAIM_GB").toString();
		int executedRtn   = 0;
		
		paorderm.setPaCode		 	(cancelMap.get("PA_CODE").toString()); 
		paorderm.setPaGroupCode		(cancelMap.get("PA_GROUP_CODE").toString());
		paorderm.setPaOrderGb	 	("20");
		paorderm.setPaOrderNo	 	(cancelMap.get("PA_ORDER_NO").toString());
		paorderm.setPaOrderSeq	 	(cancelMap.get("PA_ORDER_SEQ").toString());
		paorderm.setPaClaimNo	 	(cancelMap.get("CONTR_NO_SEQ").toString());
		paorderm.setPaProcQty	 	(cancelMap.get("PA_PROC_QTY").toString());
		
		if("0".equals(outClaimGb)){
			paorderm.setPaDoFlag	 	("20");			
		}else{
			paorderm.setPaDoFlag	 	("60");
		}
		paorderm.setOutBefClaimGb	(cancelMap.get("OUT_CLAIM_GB").toString());
		paorderm.setInsertDate		(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyDate		(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyId		(cancelMap.get("SITE_GB").toString());
		//= TPAORDERM INSERT
		//= 취소승인처리 결과 INSERT
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		return executedRtn;
		
	}
	
	/**setPagmktCancelVo2와 다른점은 setPagmktCancelVo2은 DB SELECT 후 SETTING이고, setPagmktCancelVo은 G마켓 데이터 파싱이다. **/
	public PaGmkCancel setPagmktCancelVo(HashMap<String, Object> cancelMap, ParamMap paramMap) throws Exception{
		
		PaGmkCancel cancel = null;
		String sysdate = systemService.getSysdatetimeToString();
		
		
		cancel = new PaGmkCancel();
		cancel.setPaCode				(paramMap.getString("paCode"));
		cancel.setPaGroupCode			(paramMap.getString("paGroupCode"));
		
		cancel.setPayNo					(ComUtil.NVL(cancelMap.get("PayNo")).toString());
		cancel.setGroupNo				(ComUtil.NVL(cancelMap.get("GroupNo")).toString());
		cancel.setContrNo				(ComUtil.NVL(cancelMap.get("OrderNo")).toString());
		cancel.setContrNoSeq			(CommonUtil.getMaxPaOrderWSeq(cancel.getPayNo(),cancel.getContrNo())); 
		
		cancel.setProcNo				("00");
		cancel.setGoodsNo				(ComUtil.NVL(cancelMap.get("GoodsNo")).toString());
		cancel.setSiteGoodsNo			(ComUtil.NVL(cancelMap.get("SiteGoodsNo")).toString());
		cancel.setRequestUser			(ComUtil.NVL(cancelMap.get("RequestUser")).toString());
		cancel.setApproveUser			(ComUtil.NVL(cancelMap.get("ApproveUser")).toString());
		cancel.setCancelStatus			(ComUtil.NVL(cancelMap.get("CancelStatus")).toString());  //TODO 확인 요망
		cancel.setReason				(ComUtil.NVL(cancelMap.get("Reason")).toString());
		cancel.setReasonDetail			(ComUtil.NVL(cancelMap.get("ReasonDetail")).toString());
		cancel.setOrderDate				(PaGmktDateUtil.toTimestamp(ComUtil.NVL(cancelMap.get("OrderDate")).toString()));
		cancel.setPayDate				(PaGmktDateUtil.toTimestamp(ComUtil.NVL(cancelMap.get("PayDate")).toString()));
		cancel.setAddShippingFee		(Double.parseDouble(String.valueOf(ComUtil.NVL(cancelMap.get("AddShippingFee"),"0"))));
		cancel.setRequestDate			(PaGmktDateUtil.toTimestamp(ComUtil.NVL(cancelMap.get("RequestDate")).toString()));
		cancel.setApprovalDate			(PaGmktDateUtil.toTimestamp(ComUtil.NVL(cancelMap.get("ApproveDate")).toString()));
		cancel.setCompleteDate			(PaGmktDateUtil.toTimestamp(ComUtil.NVL(cancelMap.get("CompleteDate")).toString()));
		cancel.setWithDrawDate			(PaGmktDateUtil.toTimestamp(ComUtil.NVL(cancelMap.get("WithdrawDate")).toString()));
		
		if(cancelMap.get("WithdrawDate") == null || ComUtil.NVL(cancelMap.get("WithdrawDate")).toString().equals("")){
			cancel.setWithDrawYn		("0");
		}else{
			cancel.setWithDrawYn		("1");
		}	
		
		cancel.setModifyDate			(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		cancel.setInsertDate			(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		
		return cancel;
	}
	
	/**setPagmktCancelVo와 다른점은 setPagmktCancelVo2은 DB SELECT 후 SETTING이고, setPagmktCancelVo은 G마켓 데이터 파싱이다. **/
	public PaGmkCancel setPagmktCancelVo2(HashMap<String, Object> cancelMap, ParamMap paramMap) throws Exception{
		
		PaGmkCancel cancel = null;
		String sysdate = systemService.getSysdatetimeToString();
		
		cancel = new PaGmkCancel();
		cancel.setPaCode				(paramMap.getString("paCode"));
		cancel.setPaGroupCode			(paramMap.getString("paGroupCode"));
		
		cancel.setPayNo					(ComUtil.NVL(cancelMap.get("PAY_NO")).toString());
		cancel.setGroupNo				(ComUtil.NVL(cancelMap.get("GROUP_NO")).toString());
		cancel.setContrNo				(ComUtil.NVL(cancelMap.get("ORDER_NO")).toString());
		cancel.setContrNoSeq			(CommonUtil.getMaxPaOrderWSeq(cancel.getPayNo(),cancel.getContrNo())); 
		cancel.setProcNo				("00");
		cancel.setGoodsNo				(ComUtil.NVL(cancelMap.get("GOODS_NO")).toString());
		cancel.setSiteGoodsNo			(ComUtil.NVL(cancelMap.get("SITE_GOODS_NO")).toString());
		cancel.setRequestUser			(ComUtil.NVL(cancelMap.get("REQUEST_USER")).toString());
		cancel.setApproveUser			(ComUtil.NVL(cancelMap.get("APPROVE_USER")).toString());
		cancel.setCancelStatus			(ComUtil.NVL(cancelMap.get("CANCEL_STATUS")).toString());  //TODO 확인 요망
		cancel.setReason				(ComUtil.NVL(cancelMap.get("REASON")).toString());
		cancel.setReasonDetail			(ComUtil.NVL(cancelMap.get("REASON_DETAIL")).toString());
		cancel.setAddShippingFee		(Double.parseDouble(String.valueOf(ComUtil.NVL(cancelMap.get("ADD_SHIPPING_FEE"),"0"))));
		
		cancel.setOrderDate				(Timestamp.valueOf(cancelMap.get("ORDER_DATE").toString()));
		cancel.setRequestDate			(Timestamp.valueOf(cancelMap.get("REQUEST_DATE").toString()));
		cancel.setApprovalDate			(Timestamp.valueOf(cancelMap.get("APPROVE_DATE").toString()));
		cancel.setCompleteDate			(Timestamp.valueOf(cancelMap.get("COMPLETE_DATE").toString()));
		
		if(cancelMap.get("PAY_DATE") == null){
			cancel.setPayDate				(null);
		}else{
			cancel.setPayDate				(Timestamp.valueOf(cancelMap.get("PAY_DATE").toString()));
		}
		
		if(cancelMap.get("WITHDRAW_DATE") == null){
			cancel.setWithDrawDate		(null);
			cancel.setWithDrawYn		("0");
		}else{
			cancel.setWithDrawDate			(Timestamp.valueOf(cancelMap.get("WITHDRAW_DATE").toString()));
			cancel.setWithDrawYn		("1");
		}
		
		cancel.setModifyDate(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		cancel.setInsertDate(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		
		return cancel;
	}


	@Override
	public List<HashMap<String, Object>> selectUnpaidPreOrderList(ParamMap paramMap) throws Exception {
		return PaGmktCancelDAO.selectUnpaidPreOrderList(paramMap);
	}
	@Override
	public List<Object> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception {
		return PaGmktCancelDAO.selectPaMobileOrderAutoCancelList(paramMap);
	}
	
}