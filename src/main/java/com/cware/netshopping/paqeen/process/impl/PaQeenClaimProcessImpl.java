package com.cware.netshopping.paqeen.process.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.paqeen.controller.PaQeenAsyncController;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaQeenClaimListVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.paqeen.domain.ClaimListResponse;
import com.cware.netshopping.paqeen.domain.ExchangeClaimList;
import com.cware.netshopping.paqeen.domain.Order;
import com.cware.netshopping.paqeen.domain.OrderItem;
import com.cware.netshopping.paqeen.domain.OrderSummary;
import com.cware.netshopping.paqeen.domain.PriceDelta;
import com.cware.netshopping.paqeen.domain.PrimaryReturnTicket;
import com.cware.netshopping.paqeen.domain.Product;
import com.cware.netshopping.paqeen.domain.Reason;
import com.cware.netshopping.paqeen.domain.Return;
import com.cware.netshopping.paqeen.domain.ReturnAddress;
import com.cware.netshopping.paqeen.domain.ReturnCostPolicy;
import com.cware.netshopping.paqeen.domain.ReturnEstimationResult;
import com.cware.netshopping.paqeen.domain.ReturnListResponse;
import com.cware.netshopping.paqeen.process.PaQeenClaimProcess;
import com.cware.netshopping.paqeen.repository.PaQeenCancelDAO;
import com.cware.netshopping.paqeen.repository.PaQeenClaimDAO;
import com.cware.netshopping.paqeen.service.PaQeenClaimService;
import com.cware.netshopping.paqeen.util.PaQeenApiRequest;
import com.cware.netshopping.paqeen.util.PaQeenConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("paqeen.claim.paQeenClaimProcess")
public class PaQeenClaimProcessImpl extends AbstractService implements PaQeenClaimProcess{

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private PaQeenConnectUtil paQeenConnectUtil;
	
	@Autowired
	private PaQeenAsyncController paQeenAsyncController;
	
	@Resource(name = "paqeen.claim.paQeenClaimDAO")
	PaQeenClaimDAO paQeenClaimDAO;

	@Resource(name = "paqeen.cancel.paQeenCancelDAO")
	PaQeenCancelDAO paQeenCancelDAO;

	@Autowired
	@Qualifier("paqeen.claim.paQeenClaimService")
	private PaQeenClaimService paQeenClaimService;

	@Autowired
	private PaCommonDAO paCommonDAO;
	
	@Autowired
	private PaQeenApiRequest paQeenApiRequest;
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getReturnList(String processFlag, String fromDate, String toDate, HttpServletRequest request) throws Exception {
		String apiCode =""; 
		if(processFlag.equals("60")) {
			apiCode = "IF_PAQEENAPI_03_020";
		}else if(processFlag.equals("31")){
			apiCode = "IF_PAQEENAPI_03_010";
		}else {
			apiCode = "IF_PAQEENAPI_03_008";
		}
		String duplicateCheck = "";
		String paCode  = "";
		int page = 1;
		boolean nextPageFlag = true;
		ParamMap paramMap = new ParamMap();
		ParamMap retrieveDate = null;
		int resultCnt = 0;
		List<String> ticketStateList = new ArrayList<>();
		try {
			
			log.info("======= 퀸잇 반품목록조회 API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			int paQeenContractCnt = Constants.PA_QEEN_CONTRACT_CNT;
			
			for(int i = 0; i< paQeenContractCnt; i ++) {
				
				Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
				paCode = (i==0 )? Constants.PA_QEEN_BROAD_CODE : Constants.PA_QEEN_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				
				log.info("03.API Request Setting");
				if(processFlag.equals("31")) { //철회일때
					retrieveDate = paQeenConnectUtil.setRetrieveDateFormat(fromDate, toDate, "4");	
				}else {
					retrieveDate = paQeenConnectUtil.setRetrieveDateFormat(fromDate, toDate, "1");
				}
				
				fromDate = retrieveDate.getString("startAt");
				toDate = retrieveDate.getString("endAt");
				if(processFlag.equals("30")) {
					ticketStateList.add("SUBMITTED");
					ticketStateList.add("CONFIRMED");
					//apiUrlParameter.put("ticketState", "SUBMITTED");	
				}else if(processFlag.equals("31")){
					ticketStateList.add("WITHDRAWN");
					ticketStateList.add("REJECTED");
					//apiUrlParameter.put("ticketState", "WITHDRAWN");//철회
				}else if(processFlag.equals("60")){
					ticketStateList.add("RESOLVED");
					//apiUrlParameter.put("ticketState", "RESOLVED");//완료
				}else {
					return new ResponseMsg("400", "This code is incorrect.");
				}
				for(String ticketState:ticketStateList) {
					nextPageFlag = true;
					page = 1;
					while(nextPageFlag) {
						HashMap<String, String> apiUrlParameter  = new HashMap<String, String>();
						apiUrlParameter.put("ticketState", ticketState);
						apiUrlParameter.put("page", ComUtil.objToStr(page)); // 페이지 더 돌려야함 
						apiUrlParameter.put("size", "40");
						apiUrlParameter.put("startAt", fromDate);
						apiUrlParameter.put("endAt", toDate);
						if("REJECTED".equals(ticketState)) {
							apiUrlParameter.put("auditorGroup", "CX_ADMIN");
						}
						log.info("04.API Call");
						responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, null, apiUrlParameter);
						
						ObjectMapper objectMapper = new ObjectMapper();
						ReturnListResponse returnListResponse = new ReturnListResponse();
						returnListResponse = objectMapper.convertValue(responseMap, ReturnListResponse.class);
						if(((List<Map<String, Object>>)(responseMap.get("list"))).size() < 1 || returnListResponse.getTotalPageCount() <= ComUtil.objToLong(page)) {
							nextPageFlag = false;
						}else { 
							page++;
						}
						
						if(returnListResponse.getList() != null) {
							log.debug("getRetrunList succeed");
							paramMap.put("code", "200");
							if(returnListResponse.getList().size() > 0) {
								for(Return returnItem : returnListResponse.getList()) {
									resultCnt = 0;
									try {
										resultCnt = paQeenClaimService.savePaQeenReturnTx(returnItem, paCode);
									} catch (Exception e) {
										log.error(e.getMessage()); 
										continue;
									}
								}
							}
						}else {
							nextPageFlag = false;
						}
					}
				}
			}
		if(processFlag.equals("31")) {
			returnCancleInputMain(request);
		}else {
			returnInputMain(request);	
		}
		}catch (Exception e) {
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 반품목록조회 API End - {} =======");
		}
		
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getExchangeList(String fromDate, String toDate, HttpServletRequest request, String claimStatus)
			throws Exception {
		
		String apiCode = "";
		if("40".equals(claimStatus)) {
			 apiCode = "IF_PAQEENAPI_03_012";
		}else {
			 apiCode = "IF_PAQEENAPI_03_023";
		}
		String duplicateCheck = "";
		String paCode = "";
		Timestamp sysdateTime = systemService.getSysdatetime();
		ParamMap paramMap = new ParamMap();
		ParamMap retrieveDate = null;

		try {
			
			log.info("======= 퀸잇 교환목록조회 API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			int paQeenContractCnt = Constants.PA_QEEN_CONTRACT_CNT;
			
			for(int i = 0; i< paQeenContractCnt; i ++) {
				String startAt = "";
				String endAt = "";
				Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
				paCode = (i==0 )? Constants.PA_QEEN_BROAD_CODE : Constants.PA_QEEN_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				
				int page = 1;
				boolean isNext = true;
				
				log.info("03.API Request Setting");
				if("41".equals(claimStatus)) {
					retrieveDate = paQeenConnectUtil.setRetrieveDateFormat(fromDate, toDate, "4");
				}else {
					retrieveDate = paQeenConnectUtil.setRetrieveDateFormat(fromDate, toDate, "1");
				}
				startAt = retrieveDate.getString("startAt");
				endAt = retrieveDate.getString("endAt");
				
				HashMap<String, String> apiUrlParameter  = new HashMap<String, String>();
				HashMap<String, String> apiRequestObject  = new HashMap<String, String>();
				
				while(isNext) {
					apiUrlParameter.put("startAt", startAt);
					apiUrlParameter.put("size", "30");
					apiUrlParameter.put("page", Integer.toString(page));
					apiUrlParameter.put("endAt", endAt);
					apiUrlParameter.put("processState", "40".equals(claimStatus) ?  "REQUESTED" : "FINISHED");
					
					log.info("04.API Call");
					responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, apiUrlParameter);
					
					ObjectMapper objectMapper = new ObjectMapper();
					ClaimListResponse claimListResponse = new ClaimListResponse();
					claimListResponse = objectMapper.convertValue(responseMap, ClaimListResponse.class);
					
					if(((ArrayList<Map<String, Object>>)(responseMap.get("list"))).size() < 1 || claimListResponse.getTotalPageCount() == page) {
						isNext = false;
					}
					page ++;
					
					
					if(claimListResponse.getList() != null) {
						log.debug("getClaimList succeed");
						paramMap.put("code", "200");
						if(claimListResponse.getList().size() > 0) {
							for(ExchangeClaimList claim : claimListResponse.getList()) {
								try {
									ParamMap paramMap2 = new ParamMap();
									paramMap2.put("paCode", paCode);
									paramMap2.put("claimStatus", claimStatus);
									paQeenClaimService.savePaQeenExchangeTx(claim, paramMap2);
								} catch (Exception e) {
									log.error(e.getMessage()); 
									continue;
								}
							}
						}
					}
				}
			}
			if("40".equals(claimStatus)) {
				exchangeInputMain(claimStatus, request);
			}else {
				claimCancelInputMain(claimStatus, request);
			}
		}catch (Exception e) {
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 교환목록조회 API End - {} =======");
		}
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	
	
	private int saveQeenClaimList(PaQeenClaimListVO paQeenClaimList)  throws Exception {

		int executedRtn = 0;
		int checkClaimCnt = 0;
		int checkClaimDoFlag = 0;
		
		// 원주문 데이터 체크
		checkClaimCnt = paQeenClaimDAO.countOrderList(paQeenClaimList);
		if (checkClaimCnt < 1)
			return 0; // 주문이 없는 데이터는 스킵
		
		if(paQeenClaimList.getPaOrderGb().equals("31")) {
			checkClaimCnt = paQeenClaimDAO.countClaimList(paQeenClaimList);
			if (checkClaimCnt < 1)
				return 0; // 접수내역이 없는 철회은 스킵 없는 데이터는 스킵
		}
		
		//TPAQEENCLAIMLIST 중복 데이터 유무 체크 
		checkClaimDoFlag = paQeenClaimDAO.selectPaQeenClaimCount(paQeenClaimList);
		if (checkClaimDoFlag > 0) {
			if (("60").equals(paQeenClaimList.getClaimStatus()) && checkClaimDoFlag != 60) {
				// 반품완료조회인데 저장됐던 자료이면서 반품 완료 상태가 아니다 => 반품 접수 이후 관리자 페이지에서 반품완료를 한것이다.
				// 따라서 DO_FLAG를 60으로 업데이트를 한다.
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("PA_DO_FLAG", "60");
				map.put("API_RESULT_CODE", "000000");
				map.put("API_RESULT_MESSAGE", "직권 반품 완료");
				
				String orderId = paQeenClaimList.getOrderId();
				map.put("PA_SHIP_NO", paQeenClaimList.getOrderItemId().replace(orderId, ""));
				map.put("PA_ORDER_SEQ", paQeenClaimList.getGroupId().replace(orderId, ""));
				map.put("PA_CLAIM_NO", paQeenClaimList.getTicketId().replace(orderId, ""));
				map.put("PA_ORDER_NO", paQeenClaimList.getOrderId());
				map.put("PA_CODE", paQeenClaimList.getPaCode());
				map.put("PA_ORDER_GB", paQeenClaimList.getPaOrderGb());
				paQeenClaimDAO.updatePaOrderMDoFlag(map);
			}
			return 0;
		}else {
			executedRtn = paQeenClaimDAO.insertPaQeenClaimList(paQeenClaimList);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAQEENCLAIMLIST INSERT" });
			}
		}
		
		String orderId = paQeenClaimList.getOrderId();

		paQeenClaimList.setTicketId(paQeenClaimList.getTicketId().replace(orderId, ""));
		paQeenClaimList.setGroupId(paQeenClaimList.getGroupId().replace(orderId, ""));
		paQeenClaimList.setOrderItemId(paQeenClaimList.getOrderItemId().replace(orderId, ""));
				
		executedRtn = insertPaOrderm(paQeenClaimList, new Paorderm());
		if (executedRtn != 1)
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT(CLAIM40)" });
		
		paQeenClaimList.setTicketId(paQeenClaimList.getTicketId().replace(orderId, ""));
		paQeenClaimList.setGroupId(paQeenClaimList.getGroupId().replace(orderId, ""));
		
		return executedRtn;
	}

	private int insertPaOrderm(PaQeenClaimListVO paQeenClaimList, Paorderm paorderm) throws Exception {
		if (paorderm == null)
			paorderm = new Paorderm();
		
		Map<String, String> voMap = null;

		voMap = BeanUtils.describe(paQeenClaimList);
		voMap.put("outBefClaimGb", "0");
		paorderm.setPaOrderNo(voMap.get("orderId"));
		paorderm.setPaClaimNo(voMap.get("ticketId"));
		paorderm.setPaOrderSeq(voMap.get("groupId"));
		paorderm.setPaShipNo(voMap.get("orderItemId"));
		
		int executedRtn = 0;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		String paOrderGb = voMap.get("paOrderGb");
		String outClaimGb = voMap.get("outBefClaimGb");

		paorderm.setPaGroupCode(Constants.PA_QEEN_GROUP_CODE);
		paorderm.setPaOrderGb(paOrderGb);
		paorderm.setPaCode(voMap.get("paCode"));
		
		paorderm.setPaProcQty(String.valueOf(voMap.get("quantity")));
		
		if("40".equals(String.valueOf(voMap.get("paOrderGb")))){
			paorderm.setChangeFlag("00");
			paorderm.setPaDoFlag("20"); // 접수 
		}else if("41".equals(String.valueOf(voMap.get("paOrderGb")))){
			paorderm.setPaDoFlag("60"); // 철회 완료 
		}else if ("0".equals(outClaimGb)) {
			paorderm.setPaDoFlag("20");
		} 
		
		paorderm.setOutBefClaimGb(outClaimGb);
		paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyId(Constants.PA_QEEN_PROC_ID);
		
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		
		if ("40".equals(paOrderGb) || "41".equals(paOrderGb)) { // 교환일경우 45(회수) or 46(회수취소) 도 같이 생성
			paOrderGb = paOrderGb.equals("40") ? "45" : "46";
			paorderm.setPaOrderGb(paOrderGb);
			paorderm.setChangeFlag("00");
			paorderm.setPaDoFlag("10");
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		}
		if (executedRtn != 1)
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		
		return executedRtn;
	}

	private String returnReasonMapping(String reason,Boolean isCustomerNegligence) {
		String reasonType = ""; 
		
		if(reason.contains("사이즈가 커요")) {
			reasonType = "01";
		}else if(reason.contains("사이즈가 작아요")) {
			reasonType = "02";
		}else if(reason.contains("색상,소재가 기대와 달라요")) {
			reasonType = "03";
		}else if(reason.contains("핏, 디자인이 마음에 들지 않아요")) {
			reasonType = "04";
		}else if(reason.contains("상품은 괜찮은데 마음이 바뀌었어요")) {
			reasonType = "05";
		}else if(reason.contains("상품이 불량이거나 훼손되었어요")) {
			reasonType = "06";
		}else if(reason.contains("배송을 받지 못했어요")) {
			reasonType = "07";
		}else if(reason.contains("다른 상품이 배송되었어요")) {
			reasonType = "08";
		}else if(reason.contains("주문한 상품 중 일부 상품이 배송되지 않았어요")) {
			reasonType = "09";
		}else if(reason.contains("해당하는 사유가 위에 없어요")) {
			reasonType = "10";
		}else {
			if(isCustomerNegligence) {
				reasonType = "11";
			}else {
				reasonType = "12";
			}
		}
		
		return reasonType;
	}
	
	private String exchangeReasonMapping(Reason reason) {
		String reasonType = ""; 
		
		if(reason.getText().contains("사이즈를 변경하고 싶어요")) {
			reasonType = "01";
		}else if(reason.getText().contains("색상을 변경하고 싶어요")) {
			reasonType = "02";
		}else if(reason.getText().contains("상품이 불량이거나 훼손되었어요")) {
			reasonType = "03";
		}else if(reason.getText().contains("다른 상품이 배송되었어요")) {
			reasonType = "04";
		}else {
			if(reason.isCustomerNegligence()) {
				reasonType = "06";
			}else {
				reasonType = "07";
			}
		}
		
		return reasonType;
	}

	
	private void exchangeInputMain(String claimStatus, HttpServletRequest request) throws Exception {
		String prg_id 			= "PAQEEN_EXCAHANGE_INPUT";
		String duplicateCheck 	= "";
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("paOrderGb", claimStatus);
			List<HashMap<String, Object>> claimInputTargetList = paQeenClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> claimInputTarget : claimInputTargetList) {
				try {
						paQeenAsyncController.changeClaimAsync(claimInputTarget,request);
				} catch (Exception e) {
						log.error( " 퀸잇 교환 생성 오류", claimInputTarget.get("PA_ORDER_NO"),e.getMessage());
					continue;
				}
			}
			
		}catch (Exception e) {
			log.info("{} : {}","퀸잇 교환 생성 오류", e.getMessage());
		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paQeenClaimDAO.selectClaimTargetList(paramMap);
	}

	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paQeenClaimDAO.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paQeenClaimDAO.compareAddress(paramMap);
	}

	@Override
	public int updatePaOrdermChangeFlag(String string, String mappingSeq) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("changeFlag", string);
		paramMap.put("mappingSeq", mappingSeq);
		return paQeenClaimDAO.updatePaOrdermChangeFlag(paramMap);
	}

	@Override
	public ResponseMsg getExchangeRecall(HttpServletRequest request, String claimGb) throws Exception {
		String apiCode = "";
		String paDoFlag = "";
		String API_RESULT_MESSAGE = "";
		String API_ERROR_CODE = "";
		String duplicateCheck = "";
		String paCode = "";
		String message = "";
		ParamMap paramMap = new ParamMap();
		int executedRtn = 0;
		
		switch(claimGb) {
		case "20" :
			apiCode = "IF_PAQEENAPI_03_013";
			paramMap.put("claimGb", claimGb);
			paDoFlag = "20";
			API_RESULT_MESSAGE = "교환 승인 요청";
			API_ERROR_CODE = "TPAORDERM EXCHANGE CONFIRM UPDATE";
			break;
		case "50":
			apiCode = "IF_PAQEENAPI_03_014";
			paramMap.put("claimGb", claimGb);
			paDoFlag = "50";
			API_RESULT_MESSAGE = "교환 수거지시 ";
			API_ERROR_CODE = "TPAORDERM EXCHANGE COLLECTION ORDER UPDATE";
			break;
		case "60":
			apiCode = "IF_PAQEENAPI_03_015";
			paramMap.put("claimGb", claimGb);
			paDoFlag = "59";
			API_RESULT_MESSAGE = "교환 수거완료";
			API_ERROR_CODE = "TPAORDERM EXCHANGE RETRIEVE COMPLETE UPDATE";
			break;
		case "70":
			apiCode = "IF_PAQEENAPI_03_016";
			paramMap.put("claimGb", claimGb);
			API_RESULT_MESSAGE = "교환 검수완료";
			paDoFlag = "60";
			API_ERROR_CODE = "TPAORDERM EXCHANGE INSPECTION COMPLETE UPDATE";
			break;
		}
		
		try {
			
			log.info("======= 퀸잇 교환상품 승인, 수거지시, 수거완료  API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			int paQeenContractCnt = Constants.PA_QEEN_CONTRACT_CNT;
			
			for(int i = 0; i< paQeenContractCnt; i ++) {
				Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
				paCode = (i==0 )? Constants.PA_QEEN_BROAD_CODE : Constants.PA_QEEN_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				
				log.info("03.API Request Setting");
				List<Map<String, Object>> groupList = paQeenClaimDAO.selectPaQeenOrderClaimGroup(paramMap);
				
				for(Map<String, Object> group : groupList) {
					paramMap.put("ticketId", group.get("PA_CLAIM_NO"));
					
					HashMap<String, String> apiUrlParameter  = new HashMap<String, String>();
					HashMap<String, String> apiRequestObject  = new HashMap<String, String>();
					
					paramMap.put("urlParameter", group.get("PA_CLAIM_NO"));
					
					if(claimGb.equals("50")) {
						HashMap<String, Object> map = paQeenClaimDAO.selectPaQeenClaimDelyInfo(paramMap);
						apiRequestObject.put("vendor", map.get("PA_DELY_GB").toString());
						apiRequestObject.put("vendorDeliveryNumber", map.get("SLIP_NO").toString());
					}
					
					List<Map<String, Object>> cancelList = paQeenClaimDAO.selectPaQeenOrderClaimList(paramMap);
					
					try {
						log.info("04.API Call");
						responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, apiUrlParameter);
						
						for(Map<String, Object> cancelMap : cancelList) {
							
							String orderId = cancelMap.get("PA_ORDER_NO").toString();
							cancelMap.put("PA_SHIP_NO", cancelMap.get("PA_SHIP_NO").toString().replace(orderId, ""));
							cancelMap.put("PA_ORDER_SEQ", cancelMap.get("PA_ORDER_SEQ").toString().replace(orderId, ""));
							cancelMap.put("PA_CLAIM_NO", cancelMap.get("PA_CLAIM_NO").toString().replace(orderId, ""));

							if("200".equals(paramMap.get("resultCode").toString()) && responseMap == null) {
								cancelMap.put("PA_DO_FLAG", paDoFlag);
								cancelMap.put("API_RESULT_CODE", "200");
								cancelMap.put("API_RESULT_MESSAGE", API_RESULT_MESSAGE);
								
								executedRtn = paQeenClaimDAO.updatePaOrderMDoFlag(cancelMap);
								if (executedRtn != 1)
									throw processException("msg.cannot_save", new String[] {API_ERROR_CODE });
							}else {
								cancelMap.put("API_RESULT_CODE", "400");
								cancelMap.put("API_RESULT_MESSAGE", API_RESULT_MESSAGE + "실패");
								executedRtn = paQeenClaimDAO.updatePaOrderMDoFlag(cancelMap);
								if (executedRtn != 1)
									throw processException("msg.cannot_save", new String[] {API_ERROR_CODE });
							}
						}
					}catch (Exception e) {
						for(Map<String, Object> cancelMap : cancelList) {
							
							String orderId = cancelMap.get("PA_ORDER_NO").toString();
							cancelMap.put("PA_SHIP_NO", cancelMap.get("PA_SHIP_NO").toString().replace(orderId, ""));
							cancelMap.put("PA_ORDER_SEQ", cancelMap.get("PA_ORDER_SEQ").toString().replace(orderId, ""));
							cancelMap.put("PA_CLAIM_NO", cancelMap.get("PA_CLAIM_NO").toString().replace(orderId, ""));
							
							cancelMap.put("API_RESULT_CODE", "400");
							cancelMap.put("API_RESULT_MESSAGE", e.getMessage());
							executedRtn = paQeenClaimDAO.updatePaOrderMDoFlag(cancelMap);
							
							if(e.getMessage().contains("code=3")) {
								cancelMap.put("PA_DO_FLAG", paDoFlag);
								cancelMap.put("API_RESULT_CODE", "200");
								cancelMap.put("API_RESULT_MESSAGE", API_RESULT_MESSAGE);
								
								executedRtn = paQeenClaimDAO.updatePaOrderMDoFlag(cancelMap);
								if (executedRtn != 1)
									throw processException("msg.cannot_save", new String[] {API_ERROR_CODE });
							}
							
						}
						if (executedRtn != 1)
							throw processException("msg.cannot_save", new String[] {API_ERROR_CODE});
					}
				}
			}
			paramMap.put("code", "200");
			message += API_RESULT_MESSAGE + "성공";
		}catch (Exception e) {
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paramMap.put("message", message);
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 교환상품 승인, 수거지시, 수거완료  API End - {} =======");
		}
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public ResponseMsg getExchangeDelivery(HttpServletRequest request) throws Exception {
		
		String apiCode = "IF_PAQEENAPI_03_017";
		String duplicateCheck = "";
		String paCode = "";
		ParamMap paramMap = new ParamMap();
		int executedRtn = 0;

		try {
			log.info("======= 퀸잇 교환 상품 출고  API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			int paQeenContractCnt = Constants.PA_QEEN_CONTRACT_CNT;
			
			for(int i = 0; i< paQeenContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_QEEN_BROAD_CODE : Constants.PA_QEEN_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				
				List<Map<String, Object>> deliveryGroupList = paQeenClaimDAO.selectPaQeenClaimDeliveryGroupList(paramMap);
				
				for(Map<String, Object> deliveryGroupMap : deliveryGroupList) {
					Map<String, Object> responseMap 		 = new HashMap<String, Object>() ;
					HashMap<String, String> apiUrlParameter  = new HashMap<String, String>();
					HashMap<String, String> apiRequestObject = new HashMap<String, String>();
					
					paramMap.put("urlParameter", deliveryGroupMap.get("PA_CLAIM_NO"));
					
					paramMap.put("ticketId", deliveryGroupMap.get("PA_CLAIM_NO"));
					
					paramMap.put("claimGb", "80");
					HashMap<String, Object> map = paQeenClaimDAO.selectPaQeenClaimDelyInfo(paramMap);
					apiRequestObject.put("vendor", map.get("PA_DELY_GB").toString());
					apiRequestObject.put("vendorDeliveryNumber", map.get("SLIP_NO").toString());
					
					List<Map<String, Object>> DeliveryList = paQeenClaimDAO.selectPaQeenClaimDeliveryList(paramMap);
					
					try {
						log.info("04.API Call");
						responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, apiUrlParameter);
						
						for(Map<String, Object> deliveryMap : DeliveryList) {
							
							String orderId = deliveryMap.get("PA_ORDER_NO").toString();
							deliveryMap.put("PA_SHIP_NO",   deliveryMap.get("PA_SHIP_NO").toString().replace(orderId, ""));
							deliveryMap.put("PA_ORDER_SEQ", deliveryMap.get("PA_ORDER_SEQ").toString().replace(orderId, ""));
							deliveryMap.put("PA_CLAIM_NO",  deliveryMap.get("PA_CLAIM_NO").toString().replace(orderId, ""));
							
							if("200".equals(paramMap.get("resultCode").toString()) && responseMap == null) {
								deliveryMap.put("PA_DO_FLAG", "40");
								deliveryMap.put("API_RESULT_CODE", "200");
								deliveryMap.put("API_RESULT_MESSAGE", "교환 상품 발송 완료");
								
								executedRtn = paQeenClaimDAO.updatePaOrderMDoFlag(deliveryMap);
								if (executedRtn != 1)
									throw processException("msg.cannot_save", new String[] {"TPAORDERM EXCHANGE DELIVERY UPDATE"});
							}else {
								deliveryMap.put("API_RESULT_CODE", "400");
								deliveryMap.put("API_RESULT_MESSAGE", "교환 상품 발송 완료 실패");
								executedRtn = paQeenClaimDAO.updatePaOrderMDoFlag(deliveryMap);
								if (executedRtn != 1)
									throw processException("msg.cannot_save", new String[] {"TPAORDERM EXCHANGE DELIVERY UPDATE" });
							}
						}
					}catch (Exception e) {
						for(Map<String, Object> deliveryMap : DeliveryList) {
							
							String orderId = deliveryMap.get("PA_ORDER_NO").toString();
							deliveryMap.put("PA_SHIP_NO",   deliveryMap.get("PA_SHIP_NO").toString().replace(orderId, ""));
							deliveryMap.put("PA_ORDER_SEQ", deliveryMap.get("PA_ORDER_SEQ").toString().replace(orderId, ""));
							deliveryMap.put("PA_CLAIM_NO",  deliveryMap.get("PA_CLAIM_NO").toString().replace(orderId, ""));
							
							deliveryMap.put("API_RESULT_CODE", "400");
							deliveryMap.put("API_RESULT_MESSAGE", e.getMessage());
							executedRtn = paQeenClaimDAO.updatePaOrderMDoFlag(deliveryMap);
						}
						
						if (executedRtn != 1)
							throw processException("msg.cannot_save", new String[] {"TPAORDERM EXCHANGE DELIVERY UPDATE"});
					}
				}
			}
		}catch (Exception e) {
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 교환 상품 출고  API End - {} =======");
		}
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	
	private void claimCancelInputMain(String claimStatus,HttpServletRequest request) throws Exception {
		String return_prg_id 			= "PAQEEN_RETURN_CANCEL_INPUT";
		String exchange_prg_id 			= "PAQEEN_EXCHANGE_CANCEL_INPUT";
		String prg_id = "";
		String duplicateCheck 	= "";
		
		try {
			prg_id = claimStatus.equals("31")?  return_prg_id : exchange_prg_id;
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("paOrderGb", claimStatus);
			List<HashMap<String, Object>> cancelInputTargetList = paQeenClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> cancelInputTarget : cancelInputTargetList) {
				try {
					paQeenAsyncController.claimCancelAsync(cancelInputTarget,request);
				} catch (Exception e) {
					log.error( "QEEN 클레임 업데이트 오류", e);
					continue;
				}
			}

		}catch (Exception e) {
			log.info("{} : {}","퀸잇 클레임 철회 생성 오류", e.getMessage());
		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}
	
	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paQeenClaimDAO.selectChangeCancelTargetDtList(paramMap);
	}	
	
	@Override
	public int savePaQeenReturn(Return returnItem, String paCode) throws Exception { String rtnMsg		 = Constants.SAVE_SUCCESS;
		String dateTime 	 = systemService.getSysdatetimeToString();
		Timestamp systemTime = DateUtil.toTimestamp(dateTime);
		int executedRtn = 0;
		ParamMap param = new ParamMap();
		PaQeenClaimListVO paQeenClaimList = new PaQeenClaimListVO();
		PrimaryReturnTicket primaryReturnTicket  = new PrimaryReturnTicket();
		ReturnEstimationResult returnEstimationResult = new ReturnEstimationResult();
		ReturnAddress retrunAddress = new ReturnAddress();
		PriceDelta priceDelta = new PriceDelta();
		Order linkedOrder = new Order();
		OrderSummary orderSummary = new OrderSummary();
//		OrderItem orderItem = new OrderItem();
		Timestamp sysdateTime = systemService.getSysdatetime();
		
		paQeenClaimList.setPaCode(paCode); //공통
		paQeenClaimList.setInsertDate(sysdateTime);
		paQeenClaimList.setModifyDate(sysdateTime);

		primaryReturnTicket = returnItem.getOrderItems().get(0).getPrimaryReturnTicket(); //반품 기본 정보 세팅
		returnEstimationResult = returnItem.getReturnEstimationResult();
		priceDelta = (returnEstimationResult != null) ? returnItem.getReturnEstimationResult().getPriceDelta() : null;
		paQeenClaimList.setTicketId(returnItem.getTicketId());
		paQeenClaimList.setCustomerNegligence(returnItem.getIsCustomerNegligence() == true? "1":"0");
		paQeenClaimList.setState(returnItem.getState());
		paQeenClaimList.setRequestedAtMillis(new Timestamp(primaryReturnTicket.getCreatedAtMillis()));
		paQeenClaimList.setReason(returnItem.getReason());
		paQeenClaimList.setReasonType(returnReasonMapping(returnItem.getReason(),returnItem.getIsCustomerNegligence()));
		paQeenClaimList.setClaimStatus("30");
		if(returnItem.getState().equals("WITHDRAWN") || returnItem.getState().equals("REJECTED")) { 
			paQeenClaimList.setPaOrderGb("31"); 
		} else if(returnItem.getState().equals("RESOLVED")){
			paQeenClaimList.setClaimStatus("60");
			paQeenClaimList.setPaOrderGb("30"); 
		} else {
			paQeenClaimList.setPaOrderGb("30"); 
		}
		
		retrunAddress = primaryReturnTicket.getReturnAddress(); // 주소지 세팅
		paQeenClaimList.setRecipientName(retrunAddress.getRecipientName());
		paQeenClaimList.setPhoneNumber(retrunAddress.getPhoneNumber());
		paQeenClaimList.setZipCode(retrunAddress.getZipCode());
		paQeenClaimList.setAddress(retrunAddress.getAddress());
		paQeenClaimList.setDetailedAddress(retrunAddress.getDetailedAddress());
		
		
		
		linkedOrder = returnItem.getLinkedOrder();
		paQeenClaimList.setOrdererId(linkedOrder.getOrdererId());
		
		orderSummary = linkedOrder.getOrderSummary();
		paQeenClaimList.setOrderId(orderSummary.getOrderId());
		
		for(OrderItem orderItem : returnItem.getOrderItems()) { // 반품 세부 내용 세팅
			Product product = new Product();
			ReturnCostPolicy returnCostPolicy = new ReturnCostPolicy();
			
			product = orderItem.getProduct();
			returnCostPolicy = product.getDeliveryPolicy().getReturnCostPolicy();
			paQeenClaimList.setTicketId(returnItem.getTicketId());
			paQeenClaimList.setOrderItemId(orderItem.getId());
			paQeenClaimList.setGroupId(orderItem.getId().substring(0, orderItem.getId().length() - 3));
			paQeenClaimList.setOptionTitle(product.getTitle());
			paQeenClaimList.setFinalPurchaseAmount((priceDelta != null) ? priceDelta.getFinalPurchaseAmount() : 0);
			paQeenClaimList.setQuantity(ComUtil.objToLong(orderItem.getQuantity())); 
			paQeenClaimList.setDeliverFeeNormal(returnCostPolicy.getDefaultCost());
			paQeenClaimList.setDeliverFeeJeju(returnCostPolicy.getJejuIsland());
			paQeenClaimList.setDeliverFeeBackCountry(returnCostPolicy.getBackCountry());
			paQeenClaimList.setFreeExchangeTarget(orderItem.isFreeExchangeTarget() == true ? "1" :"0" );
			paQeenClaimList.setFreeReturnTarget(orderItem.isFreeReturnTarget() == true ? "1" :"0" );
			paQeenClaimList.setTotalDeliveryPrice((priceDelta != null) ? priceDelta.getTotalDeliveryPrice() : 0); //반품비
			log.info(param.toString());
			saveQeenClaimList(paQeenClaimList);
		}
		
		return 1;
	}

	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return paQeenClaimDAO.selectOrderCalimTargetDt30List(paramMap);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paQeenClaimDAO.selectOrderCalimTargetDt20List(paramMap);
	}
	
	private void returnInputMain(HttpServletRequest request) throws Exception {
		String prg_id 			= "PAQEEN_RETURN_INPUT";
		String duplicateCheck 	= "";
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("paOrderGb", "30");
			List<HashMap<String, Object>> claimInputTargetList = paQeenClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> claimInputTarget : claimInputTargetList) {
				try {
						paQeenAsyncController.returnClaimAsync(claimInputTarget,request);
				} catch (Exception e) {
						log.error( " 퀸잇 반품 생성 오류", claimInputTarget.get("PA_ORDER_NO"),e.getMessage());
					continue;
				}
			}
			
		}catch (Exception e) {
			log.info("{} : {}","퀸잇 교환 생성 오류", e.getMessage());
		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}
	
	private void returnCancleInputMain(HttpServletRequest request) throws Exception {
		String prg_id 			= "PAQEEN_RETURN_CANCEL_INPUT";
		String duplicateCheck 	= "";
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("paOrderGb", "31");
			List<HashMap<String, Object>> claimInputTargetList = paQeenClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> claimInputTarget : claimInputTargetList) {
				try {
						paQeenAsyncController.claimCancelAsync(claimInputTarget,request);
				} catch (Exception e) {
						log.error( " 퀸잇 반품 생성 오류", claimInputTarget.get("PA_ORDER_NO"),e.getMessage());
					continue;
				}
			}
			
		}catch (Exception e) {
			log.info("{} : {}","퀸잇 교환 생성 오류", e.getMessage());
		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}
	@Override
	public ResponseMsg getReturnRecall20(HttpServletRequest request) throws Exception {
		String apiCode = "";
		String paDoFlag = "";
		String API_RESULT_MESSAGE = "";
		String API_ERROR_CODE = "";
		String duplicateCheck = "";
		String paCode = "";
		String message = "";
		String claimGb = "20";
		ParamMap paramMap = new ParamMap();
		int executedRtn = 0;
		
		apiCode = "IF_PAQEENAPI_03_009";
		API_RESULT_MESSAGE = "반품 승인 요청";
		API_ERROR_CODE = "TPAORDERM RETURN CONFIRM UPDATE";
		
		try {
			log.info("======= 퀸잇 반품상품 승인  API Start - {} =======");
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paramMap.put("claimGb", claimGb);
			paramMap.put("claimDoFlag", "40");
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			int paQeenContractCnt = Constants.PA_QEEN_CONTRACT_CNT;
			
			for(int i = 0; i< paQeenContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_QEEN_BROAD_CODE : Constants.PA_QEEN_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
				
				groupList = paQeenClaimDAO.selectPaQeenReturnClaimGroupList(paramMap);
				
				for(Map<String, Object> groupInfo :groupList) {
					Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
					paramMap.put("ticketId", groupInfo.get("PA_CLAIM_NO"));
					
					log.info("03.API Request Setting");
					List<Map<String, Object>> returnlList = paQeenClaimDAO.selectPaQeenReturnClaimList20(paramMap);
					
					for(Map<String, Object> retrunMap : returnlList) {
						
						HashMap<String, String> apiUrlParameter  = new HashMap<String, String>();
						HashMap<String, String> apiRequestObject  = new HashMap<String, String>();
						
						paramMap.put("urlParameter", retrunMap.get("PA_CLAIM_NO"));
						
						String orderId = retrunMap.get("PA_ORDER_NO").toString();
						retrunMap.put("PA_SHIP_NO", retrunMap.get("PA_SHIP_NO").toString().replace(orderId, ""));
						retrunMap.put("PA_ORDER_SEQ", retrunMap.get("PA_ORDER_SEQ").toString().replace(orderId, ""));
						retrunMap.put("PA_CLAIM_NO", retrunMap.get("PA_CLAIM_NO").toString().replace(orderId, ""));
						
						try {
							log.info("04.API Call");
							responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, apiUrlParameter);
							
							if("200".equals(paramMap.get("resultCode").toString())) {
								retrunMap.put("PA_DO_FLAG", "50");
								retrunMap.put("API_RESULT_CODE", "200");
								retrunMap.put("API_RESULT_MESSAGE", API_RESULT_MESSAGE);
								executedRtn = paQeenClaimDAO.updatePaOrderMDoFlag(retrunMap);
								if (executedRtn < 1)
									throw processException("msg.cannot_save", new String[] {API_ERROR_CODE });
							}else {
								retrunMap.put("API_RESULT_CODE", "400");
								retrunMap.put("API_RESULT_MESSAGE", API_RESULT_MESSAGE + "실패");
								executedRtn = paQeenClaimDAO.updatePaOrderMDoFlag(retrunMap);
								if (executedRtn != 1)
									throw processException("msg.cannot_save", new String[] {API_ERROR_CODE });
							}
						}catch (Exception e) {
							retrunMap.put("API_RESULT_CODE", "400");
							retrunMap.put("API_RESULT_MESSAGE", e.getMessage());
							executedRtn = paQeenClaimDAO.updatePaOrderMDoFlag(retrunMap);
							if (executedRtn != 1)
								throw processException("msg.cannot_save", new String[] {API_ERROR_CODE});
						}
					}
				}
			}
			paramMap.put("code", "200");
			message += API_RESULT_MESSAGE + "성공";
		}catch (Exception e) {
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paramMap.put("message", message);
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 반품상품 승인 API End - {} =======");
		}
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	
	@Override
	public ResponseMsg getReturnRecall(HttpServletRequest request, String claimGb) throws Exception {
		String apiCode = "";
		String paDoFlag = "";
		String API_RESULT_MESSAGE = "";
		String API_ERROR_CODE = "";
		String duplicateCheck = "";
		String paCode = "";
		String message = "";
		Timestamp sysdateTime = systemService.getSysdatetime();
		ParamMap paramMap = new ParamMap();
		int executedRtn = 0;
		
		switch(claimGb) {
		case "50":
			apiCode = "IF_PAQEENAPI_03_011";
			paramMap.put("claimGb", claimGb);
			paramMap.put("claimDoFlag", "60");
			paDoFlag = "60";
			API_RESULT_MESSAGE = "반품 환불 완료";
			API_ERROR_CODE = "TPAORDERM RETURN RETRIEVE COMPLETE UPDATE";
			break;
		}
		
		try {
			
			log.info("======= 퀸잇 환불완료  API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			int paQeenContractCnt = Constants.PA_QEEN_CONTRACT_CNT;
			
			for(int i = 0; i< paQeenContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_QEEN_BROAD_CODE : Constants.PA_QEEN_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
				
				groupList = paQeenClaimDAO.selectPaQeenReturnClaimGroupList(paramMap);
				
				for(Map<String, Object> groupInfo :groupList) {
					Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
					paramMap.put("ticketId", groupInfo.get("PA_CLAIM_NO"));
					int claimCount = paQeenClaimDAO.selectPaQeenReturnClaimCount(paramMap);
					//퀸잇은 묶인 클레임중 하나의 클레임이 수락 될 경우 전체 수락 된다. 따라서 모든 클레임이 승인 됐을떄만 동작 하도록 한다.
					if(claimCount != ComUtil.objToInt(groupInfo.get("TARGET_CNT").toString())) {
						continue;
					}
					log.info("03.API Request Setting");
					List<Map<String, Object>> returnlList = paQeenClaimDAO.selectPaQeenReturnClaimList(paramMap);
					
					for(Map<String, Object> retrunMap : returnlList) {
						
						HashMap<String, String> apiUrlParameter  = new HashMap<String, String>();
						HashMap<String, String> apiRequestObject  = new HashMap<String, String>();
						
						paramMap.put("urlParameter", retrunMap.get("PA_CLAIM_NO"));
						
						String orderId = retrunMap.get("PA_ORDER_NO").toString();
						retrunMap.put("PA_SHIP_NO", retrunMap.get("PA_SHIP_NO").toString().replace(orderId, ""));
						retrunMap.put("PA_ORDER_SEQ", retrunMap.get("PA_ORDER_SEQ").toString().replace(orderId, ""));
						retrunMap.put("PA_CLAIM_NO", retrunMap.get("PA_CLAIM_NO").toString().replace(orderId, ""));
						
						try {
							log.info("04.API Call");
							responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, apiUrlParameter);
							
							if("200".equals(paramMap.get("resultCode").toString())) {
								retrunMap.put("PA_DO_FLAG", paDoFlag);
								retrunMap.put("API_RESULT_CODE", "200");
								retrunMap.put("API_RESULT_MESSAGE", API_RESULT_MESSAGE);
								
								executedRtn = paQeenClaimDAO.updatePaOrderMDoFlagClaimNo(retrunMap);	
								if (executedRtn < 1)
									throw processException("msg.cannot_save", new String[] {API_ERROR_CODE });
							}else {
								retrunMap.put("API_RESULT_CODE", "400");
								retrunMap.put("API_RESULT_MESSAGE", API_RESULT_MESSAGE + "실패");
								executedRtn = paQeenClaimDAO.updatePaOrderMDoFlag(retrunMap);
								if (executedRtn != 1)
									throw processException("msg.cannot_save", new String[] {API_ERROR_CODE });
							}
						}catch (Exception e) {
							retrunMap.put("API_RESULT_CODE", "400");
							retrunMap.put("API_RESULT_MESSAGE", e.getMessage());
							executedRtn = paQeenClaimDAO.updatePaOrderMDoFlag(retrunMap);
							if (executedRtn != 1)
								throw processException("msg.cannot_save", new String[] {API_ERROR_CODE});
						}
					}
				}
			}
			paramMap.put("code", "200");
			message += API_RESULT_MESSAGE + "성공";
		}catch (Exception e) {
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paramMap.put("message", message);
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 환불완료  API End - {} =======");
		}
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paQeenClaimDAO.selectClaimCancelTargetDtList(paramMap);
	}

	@Override
	public int savePaQeenExchange(ExchangeClaimList claim, ParamMap paramMap2) throws Exception {
		Timestamp sysdateTime = systemService.getSysdatetime();
		int executedRtn = 0;

		PaQeenClaimListVO paQeenClaimList = new PaQeenClaimListVO();
		
		Long deliverFeeNormal = claim.getImposedExchangeFee().getImposedExchangeFeeToTicket().getDeliveryFee().getDeliverFee().getNormal();
		Long recallFeeNormal = claim.getImposedExchangeFee().getImposedExchangeFeeToTicket().getDeliveryFee().getRecallFee().getNormal();
		
		Long deliverFeeJeju = claim.getImposedExchangeFee().getImposedExchangeFeeToTicket().getDeliveryFee().getDeliverFee().getJeju();
		Long recallFeeJeju = claim.getImposedExchangeFee().getImposedExchangeFeeToTicket().getDeliveryFee().getRecallFee().getJeju();
		
		Long deliverFeeBackCountry = claim.getImposedExchangeFee().getImposedExchangeFeeToTicket().getDeliveryFee().getDeliverFee().getBackCountry();
		Long recallFeeBackCountry = claim.getImposedExchangeFee().getImposedExchangeFeeToTicket().getDeliveryFee().getRecallFee().getBackCountry();
		
		paQeenClaimList.setPaCode(paramMap2.getString("paCode"));
		paQeenClaimList.setRequestedAtMillis(new Timestamp(claim.getRequestedAtMillis())); 
		paQeenClaimList.setPaOrderGb(paramMap2.getString("claimStatus"));
		paQeenClaimList.setState(claim.getProcessState());
		paQeenClaimList.setPhoneNumber(claim.getPhoneNumber().toString());
		paQeenClaimList.setRecipientName(claim.getDeliveryRequest().getAddress().getRecipientName());
		paQeenClaimList.setZipCode(claim.getDeliveryRequest().getAddress().getZipCode());
		paQeenClaimList.setAddress(claim.getDeliveryRequest().getAddress().getAddress());
		paQeenClaimList.setDetailedAddress(claim.getDeliveryRequest().getAddress().getDetailedAddress());
		paQeenClaimList.setDeliverFeeNormal(deliverFeeNormal + recallFeeNormal);
		paQeenClaimList.setDeliverFeeJeju(deliverFeeJeju + recallFeeJeju);
		paQeenClaimList.setDeliverFeeBackCountry(deliverFeeBackCountry + recallFeeBackCountry);
		paQeenClaimList.setInsertDate(sysdateTime);
		paQeenClaimList.setModifyDate(sysdateTime);
		
		for( int j =0; j < claim.getTicketItems().size(); j++) {
			paQeenClaimList.setOrderId(claim.getOrderId());
			paQeenClaimList.setGroupId(claim.getGroupId());
			paQeenClaimList.setTicketId(claim.getTicketId());
			paQeenClaimList.setCustomerNegligence(claim.getTicketItems().get(j).getReason().isCustomerNegligence() == true ? "1" :"0" );
			paQeenClaimList.setReasonType(exchangeReasonMapping(claim.getTicketItems().get(j).getReason()));
			paQeenClaimList.setReason(claim.getTicketItems().get(j).getReason().getText());
			paQeenClaimList.setQuantity(claim.getTicketItems().get(j).getPurchaseOption().getQuantity());
			paQeenClaimList.setOptionQuantity(claim.getTicketItems().get(j).getExchangeOptions().get(0).getQuantity());
			paQeenClaimList.setOptionId(claim.getTicketItems().get(j).getExchangeOptions().get(0).getOptionId());
			paQeenClaimList.setOptionTitle(claim.getTicketItems().get(j).getExchangeOptions().get(0).getOptionTitle());
			paQeenClaimList.setSalesType(claim.getTicketItems().get(j).getSalesType());
			paQeenClaimList.setProductName(claim.getTicketItems().get(j).getProductName());
			paQeenClaimList.setProductId(claim.getTicketItems().get(j).getProductId());
			paQeenClaimList.setMallProductCode(claim.getTicketItems().get(j).getMallProductCode());
			paQeenClaimList.setFreeExchangeTarget(claim.getTicketItems().get(j).isFreeExchangeTarget() == true ? "1" :"0" );
			paQeenClaimList.setFreeReturnTarget(claim.getTicketItems().get(j).isFreeReturnTarget() == true ? "1" :"0" );
			paQeenClaimList.setOrderItemId(claim.getTicketItems().get(j).getOrderLineId());
			
			if(!((paQeenClaimList.getReasonType().equals("01") ||  paQeenClaimList.getReasonType().equals("02") || paQeenClaimList.getReasonType().equals("06")) && claim.isRequiredDeposit()==true)) {
				executedRtn = saveQeenClaimList(paQeenClaimList);
			}
		}
		
		return executedRtn;
	}
	
}	

