package com.cware.netshopping.paqeen.process.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.paqeen.controller.PaQeenAsyncController;
import com.cware.framework.core.basic.AbstractModel;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaQeenCancelListVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.paqeen.domain.CancelList;
import com.cware.netshopping.paqeen.domain.CancelListResponse;
import com.cware.netshopping.paqeen.domain.Item;
import com.cware.netshopping.paqeen.domain.ItemGroupForSeller;
import com.cware.netshopping.paqeen.domain.OrderList;
import com.cware.netshopping.paqeen.process.PaQeenCancelProcess;
import com.cware.netshopping.paqeen.repository.PaQeenCancelDAO;
import com.cware.netshopping.paqeen.repository.PaQeenDeliveryDAO;
import com.cware.netshopping.paqeen.service.PaQeenCancelService;
import com.cware.netshopping.paqeen.util.PaQeenApiRequest;
import com.cware.netshopping.paqeen.util.PaQeenConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("paqeen.cancel.paQeenCancelProcess")
public class PaQeenCancelProcessImpl extends AbstractService implements PaQeenCancelProcess{

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	private PaQeenConnectUtil paQeenConnectUtil;
	
	@Autowired
	private PaQeenApiRequest paQeenApiRequest;
	
	@Autowired
	private PaQeenAsyncController paQeenAsyncController;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Autowired
	private PaOrderService paorderService;
	
	@Autowired
	private PaCommonService paCommonService;
	
	@Autowired
	@Qualifier("paqeen.cancel.paQeenCancelService")
	private PaQeenCancelService paQeenCancelService;
	
	@Autowired
	private PaQeenDeliveryDAO paQeenDeliveryDAO;
	
	@Autowired
	private PaQeenCancelDAO paQeenCancelDAO;
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getCancelList(String startAt, String endAt, HttpServletRequest request, String cancelStatus) throws Exception {
		
		
		String apiCode = "";
		if("20".equals(cancelStatus)) {
			apiCode = "IF_PAQEENAPI_03_004";
		}else {
			apiCode = "IF_PAQEENAPI_03_022";
		}
		
		String duplicateCheck = "";
		String paCode = "";
		Timestamp sysdateTime = systemService.getSysdatetime();
		ParamMap paramMap = new ParamMap();
		ParamMap retrieveDate = null;
		int page =1;
		boolean isNext = true;
		
		try {
			
			log.info("======= 퀸잇 취소목록조회 API Start - {} =======");
			
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
				retrieveDate = paQeenConnectUtil.setRetrieveDateFormat(startAt, endAt, "1");
				startAt = retrieveDate.getString("startAt");
				endAt = retrieveDate.getString("endAt");
				page = 1;
				isNext = true;
				while(isNext) { 
					HashMap<String, String> apiUrlParameter  = new HashMap<String, String>();
					HashMap<String, String> apiRequestObject  = new HashMap<String, String>();
					
					apiUrlParameter.put("startAt", startAt);
					apiUrlParameter.put("endAt", endAt);
					apiUrlParameter.put("page", Integer.toString(page));
					apiUrlParameter.put("size", "30");
					
					switch(cancelStatus) {
					case "20" : 
						apiUrlParameter.put("ticketState", "SUBMITTED");
						break;
					case "21" : 
						apiUrlParameter.put("ticketState", "RESOLVED");
						break;
					}
					
					log.info("04.API Call");
					responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, apiUrlParameter);
					
					ObjectMapper objectMapper = new ObjectMapper();
					CancelListResponse cancelListResponse = new CancelListResponse();
					cancelListResponse = objectMapper.convertValue(responseMap, CancelListResponse.class);
					
					if(((List<Map<String, Object>>)(responseMap.get("list"))).size() < 1 || cancelListResponse.getTotalPageCount() == page) {
						isNext = false;
					} else {
						page++;
					}
					
					if(cancelListResponse.getList() != null) {
						log.debug("getCancelList succeed");
						paramMap.put("code", "200");
						if(cancelListResponse.getList().size() > 0) {
							for(CancelList order : cancelListResponse.getList()) {
								PaQeenCancelListVO paQeenCancelList = new PaQeenCancelListVO();
								
								paQeenCancelList.setTicketId(order.getTicketId());
								paQeenCancelList.setPaCode(paCode);
								paQeenCancelList.setOrderId(order.getLinkedOrder().getOrderSummary().getOrderId());
								paQeenCancelList.setState(order.getState());
								paQeenCancelList.setPaOrderGb("20");
								paQeenCancelList.setIsCustomerNegligence(order.isCustomerNegligence() == true ? "1" :"0" );
								paQeenCancelList.setReasonType(cancelReasonMapping(order));
								paQeenCancelList.setReason(order.getReason());
								paQeenCancelList.setRejectReason( order.getRejectReason());
								paQeenCancelList.setAuditorGroup( order.getAuditorGroup());
								paQeenCancelList.setWithdrawYn("0");
								
								paQeenCancelList.setCreatedAtMillis(new Timestamp(order.getCreatedAtMillis()));
								if(order.getConfirmedAtMillis() != 0)
									paQeenCancelList.setConfirmedAtMillis(new Timestamp(order.getConfirmedAtMillis()));
								if(order.getResolvedAtMillis() != 0)
									paQeenCancelList.setResolvedAtMillis(new Timestamp(order.getResolvedAtMillis()));
								if(order.getRejectedAtMillis() != 0)
									paQeenCancelList.setRejectedAtMillis(new Timestamp(order.getRejectedAtMillis()));
								if(order.getWithdrawnAtMillis() != 0)
									paQeenCancelList.setWithdrawnAtMillis(new Timestamp(order.getWithdrawnAtMillis()));
								paQeenCancelList.setInsertDate(sysdateTime);
								paQeenCancelList.setModifyDate(sysdateTime);
								for( int j =0; j < order.getOrderItems().size(); j++) {
									paQeenCancelList.setOrderItemId(order.getOrderItems().get(j).getId());
									paQeenCancelList.setGroupId(order.getOrderItems().get(j).getId().substring(0,23));
									paQeenCancelList.setQuantity(order.getOrderItems().get(j).getQuantity());
									
									saveQeenCancelList(paQeenCancelList, cancelStatus);
								}
								
							}
						}
					}
				}
			}
		}catch (Exception e) {
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 취소목록조회 API End - {} =======");
		}
		
		
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	
	public void saveQeenCancelList(PaQeenCancelListVO paQeenCancelList, String cancelStatus) throws Exception {
		int executedRtn = 0;
		int checkCancelCnt = 0;
		int existsYn = 0;
		
		// 주문 없는 데이터 스킵 
		checkCancelCnt = paQeenCancelDAO.countOrderList(paQeenCancelList);
		if(checkCancelCnt < 1) return;
		
		// 직택배 조회
		String delyType = paQeenCancelDAO.selectCancelOrderDelyGb(paQeenCancelList);
		int doFlag = Integer.parseInt(ComUtil.NVL(paQeenCancelDAO.selectDoflag(paQeenCancelList),"0")) ;
		
		// TPAQEENCANCELLIST 에서 이미  완료된 주문인지 판단 
		checkCancelCnt = paQeenCancelDAO.selectPaQeenCancelListCount(paQeenCancelList);
		if (checkCancelCnt > 0) {
			log.info("orderNo(" + paQeenCancelList.getOrderId() + ") is already exists in TPAQEENCANCELLIST.");
			return;
		}
		
		paQeenCancelList.setOutBefClaimGb(ComUtil.NVL(paQeenCancelDAO.selectOutBefClaimGb(paQeenCancelList), "0")); // 반품생성대상 여부 조회
		
		HashMap<String,Object> hashMap = new HashMap<String, Object>();
		hashMap.put("paClaimName", paQeenCancelList.getReasonType());
		hashMap.put("claimGb", paQeenCancelList.getPaOrderGb());
		
		if("10".equals(delyType) && doFlag >= 30 && "21".equals(cancelStatus) ) { // 직택배 직권취소 일 경우에만 30 에 60 
			paQeenCancelList.setPaOrderGb("30");
			paQeenCancelList.setOutBefClaimGb("0");
		}
		
		if ("20".equals(cancelStatus)) {
			paQeenCancelList.setProcFlag("00");
			if("1".equals(paQeenCancelList.getOutBefClaimGb())) {
				paQeenCancelList.setCancelProcNote("취소접수완료(반품전환)");
			}else {
				paQeenCancelList.setCancelProcNote("취소접수완료");
			}
		}else{
			paQeenCancelList.setCancelProcNote("직권취소 완료");
			paQeenCancelList.setProcFlag("10");
		}
	
		
		// TPAQEENCANCALLIST 데이터 유무 확인
		existsYn = paQeenCancelDAO.checkCancelExistYn(paQeenCancelList);
		if (existsYn < 1) {
			executedRtn = paQeenCancelDAO.insertTpaQeenCancelList(paQeenCancelList);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAQEENCANCELLIST INSERT" });
			}
		}else {
			if ("21".equals(cancelStatus)) {
				executedRtn = paQeenCancelDAO.updateTpaQeenCancelList(paQeenCancelList);
				if (executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] { "TPAQEENCANCELLIST UPDATE" });
				}
			}
		}
			
			Paorderm paorderm = setPaorderM(paQeenCancelList , cancelStatus);
		if ("21".equals(cancelStatus)) {
			existsYn = paQeenCancelDAO.checkOrdermExistYn(paorderm);
			
			if (existsYn < 1) {
				//INSERT TPAORDERM
				executedRtn = paCommonDAO.insertPaOrderM(paorderm);
				if (executedRtn != 1) {
					log.error("Failed to INSERT orderNo(" + paQeenCancelList.getOrderId() + ") into TPAORDERM");
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}
			}else {
				paorderm.setApiResultCode("000000");
				paorderm.setApiResultMessage("직권취소 완료");
				executedRtn = paQeenCancelDAO.updatePaOrderM(paorderm);
				if (executedRtn != 1) {
					log.error("Failed to UPDATE orderNo(" + paQeenCancelList.getOrderId() + ") into TPAORDERM");
					throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
				}
			}
		}
	}

	private Paorderm setPaorderM(AbstractModel abstractVO, String cancelStatus) throws Exception {
		
		String paOrderGb     = null;
		String outBefCliamGb = null;
		String sysdate       = DateUtil.getCurrentDateTimeAsString();
		Paorderm paorderm    = new Paorderm();
		
		if (abstractVO instanceof PaQeenCancelListVO) { // 취소 요청/완료 조회 저장 시
			paOrderGb = ((PaQeenCancelListVO)abstractVO).getPaOrderGb();
			outBefCliamGb = ((PaQeenCancelListVO)abstractVO).getOutBefClaimGb();
			
			if("1".equals(outBefCliamGb)) {
				paorderm.setPaDoFlag("60");
			}else {
				paorderm.setPaDoFlag("20");
			}
			
			// 직택배 조회
			String delyType = paQeenCancelDAO.selectCancelOrderDelyGb((PaQeenCancelListVO)abstractVO);
			int doFlag = Integer.parseInt(ComUtil.NVL(paQeenCancelDAO.selectDoflag((PaQeenCancelListVO)abstractVO),"0")) ;
			if("10".equals(delyType) && doFlag > 30 && "21".equals(cancelStatus) ) {
				paorderm.setPaDoFlag("60");
			}
			
			String orderId = ((PaQeenCancelListVO)abstractVO).getOrderId();
			String ticketId = ((PaQeenCancelListVO)abstractVO).getTicketId();
			String groupId = ((PaQeenCancelListVO)abstractVO).getGroupId();
			String paShipNo = ((PaQeenCancelListVO)abstractVO).getOrderItemId();
			
			String subTicketId = ticketId.replace(orderId, "");
			String subGroupId = groupId.replace(orderId, "");
			String subPaShipNo = paShipNo.replace(orderId, "");
			
			paorderm.setPaCode(((PaQeenCancelListVO)abstractVO).getPaCode());
			paorderm.setPaGroupCode("15");
			paorderm.setPaOrderNo(orderId);
			paorderm.setPaOrderSeq(subGroupId);
			paorderm.setPaShipNo(subPaShipNo);
			paorderm.setPaClaimNo(subTicketId);
			paorderm.setPaProcQty(String.valueOf(((PaQeenCancelListVO)abstractVO).getQuantity()));
			paorderm.setPreCancelYn("0");
			
		} 
		
		else {
			throw processException("errors.cannot" , new String[] { "abstractVO is not suitable." });
		}
		
		paorderm.setPaOrderGb(paOrderGb);
		paorderm.setOutBefClaimGb(outBefCliamGb);
		paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyId(Constants.PA_QEEN_PROC_ID);
		
		return paorderm;
	}
	
	private void cancelInputMain(HttpServletRequest request) throws Exception {

		String apiCode = "PAQEEN_CANCEL_INPUT";
		String duplicateCheck = "";
		ParamMap paramMap = new ParamMap();
		
		try {
			log.info("===== " + apiCode + " Start =====");
		
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			paramMap.put("paOrderGb", "20");
			// 취소 생성 대상 조회
			List<HashMap<String, Object>> cancelInputTargetList = paQeenCancelService.selectClaimTargetList(paramMap);
			for(HashMap<String, Object> cancelInputTarget : cancelInputTargetList) {
				try {
					// BO 취소 생성
					paQeenAsyncController.cancelInputAsync(cancelInputTarget, request);
					
				} catch(Exception e) {
					log.error("Exception occurs : " + e.getMessage());
				}
			}
		} catch (Exception e) {
			log.error("Exception occurs : " + e.getMessage());
		} finally {
				paQeenConnectUtil.closeApi(request, paramMap);
		}
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paQeenCancelDAO.selectClaimTargetList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paQeenCancelDAO.selectCancelInputTargetDtList(paramMap);
	}

	@Override
	public ResponseMsg cancelConfirmProc(HttpServletRequest request) throws Exception {
		
		String apiCode = "IF_PAQEENAPI_03_005";
		String duplicateCheck = "";
		String paCode = "";
		String message = "";
		Timestamp sysdateTime = systemService.getSysdatetime();
		ParamMap paramMap = new ParamMap();
		
		try {
			
			log.info("======= 퀸잇 취소승인 API Start - {} =======");
			
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
				
				log.info("03.API Request Setting");
				List<Map<String, Object>> cancelList = paQeenCancelDAO.selectPaQeenOrderCancelList(paramMap);
				
				for(Map<String, Object> cancelMap : cancelList) {
					String orderItemId = cancelMap.get("ORDER_ITEM_ID").toString();
					String orderCancelYn = cancelMap.get("ORDER_CANCEL_YN").toString();
					int doFlag = Integer.parseInt(String.valueOf(cancelMap.get("DO_FLAG")));
					
					if(doFlag < 30) {
						// 취소 승인 
						Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
						HashMap<String, String> apiUrlParameter  = new HashMap<String, String>();
						HashMap<String, String> apiRequestObject  = new HashMap<String, String>();
						
						int executedRtn = 0;
						
						paramMap.put("urlParameter", cancelMap.get("TICKET_ID").toString() );
						
						
						log.info("04.API Call");
						try {
							responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, apiUrlParameter);
						}catch (Exception e) {
							message +=  "취소 이슈 발생 : "+cancelMap.get("TICKET_ID").toString()+" / " + e.getMessage();
							
							// 이미 취소된 건에 대한 오류일 경우 
							if(e.getMessage().contains("msg=Can't confirm ticket of not submitted state.") && e.getMessage().contains("code=3")) {
								String orderId = cancelMap.get("ORDER_ID").toString();
								
								String ticketId = cancelMap.get("TICKET_ID").toString();
								String subTicketId = ticketId.replace(orderId, "");
								
								Paorderm paorderm = new Paorderm();
								paorderm.setPaClaimNo (ticketId);
								paorderm.setPaOrderNo (orderId);
								paorderm.setPaGroupCode(Constants.PA_QEEN_GROUP_CODE);
								paorderm.setPaOrderGb  ("20");
								paorderm.setPaCode	   (cancelMap.get("PA_CODE").toString());
								paorderm.setPaProcQty  (String.valueOf(cancelMap.get("QUANTITY")));
								if ("1".equals(cancelMap.get("OUT_BEF_CLAIM_GB").toString())) {
									paorderm.setPaDoFlag("60");
								} else {
									paorderm.setPaDoFlag("20");
								}
								paorderm.setOutBefClaimGb(cancelMap.get("OUT_BEF_CLAIM_GB").toString());
								paorderm.setInsertDate(sysdateTime);
								paorderm.setModifyDate(sysdateTime);
								paorderm.setModifyId  (Constants.PA_QEEN_PROC_ID);
								
								String paShipNo = cancelMap.get("ORDER_ITEM_ID").toString();
								String paOrderSeq = cancelMap.get("GROUP_ID").toString();
								String subPaShipNo = paShipNo.replace(orderId, "");
								String subOrderSeq = paOrderSeq.replace(orderId, "");
								
								paorderm.setPaShipNo(paShipNo);
								paorderm.setPaOrderSeq (paOrderSeq);
								
								executedRtn = paQeenCancelDAO.updatePaQeenCancelStatus(paorderm);
								if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAQEENCANCELLIST UPDATE" });
								
								paorderm.setPaClaimNo (subTicketId);
								paorderm.setPaOrderSeq(subOrderSeq);
								paorderm.setPaShipNo(subPaShipNo);
								paorderm.setApiResultMessage("기취소된 건 취소완료");
								executedRtn = paCommonDAO.insertPaOrderM(paorderm);
								if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
								
							}
							continue;
						}
						
						
						if(((Map<String, Object>)(responseMap)).size() < 1) {
							continue;
						}
						
						ObjectMapper objectMapper = new ObjectMapper();
						OrderList cancelResponse = new OrderList();
						cancelResponse = objectMapper.convertValue(responseMap, OrderList.class);
						
						if(cancelResponse != null) {
							log.debug("getCancelList succeed");
							paramMap.put("code", "200");
							
							List<ItemGroupForSeller> itemGroup = cancelResponse.getOrderSummary().getItemGroupsForSeller();
							
							String orderId = cancelResponse.getOrderSummary().getOrderId().toString();
							
							String ticketId = cancelMap.get("TICKET_ID").toString();
							String subTicketId = ticketId.replace(orderId, "");
							
							Paorderm paorderm = new Paorderm();
							paorderm.setPaClaimNo (ticketId);
							paorderm.setPaOrderNo (orderId);
							paorderm.setPaGroupCode(Constants.PA_QEEN_GROUP_CODE);
							paorderm.setPaOrderGb  ("20");
							paorderm.setPaCode	   (cancelMap.get("PA_CODE").toString());
							paorderm.setPaProcQty  (String.valueOf(cancelMap.get("QUANTITY")));
							if ("1".equals(cancelMap.get("OUT_BEF_CLAIM_GB").toString())) {
								paorderm.setPaDoFlag("60");
							} else {
								paorderm.setPaDoFlag("20");
							}
							paorderm.setOutBefClaimGb(cancelMap.get("OUT_BEF_CLAIM_GB").toString());
							paorderm.setInsertDate(sysdateTime);
							paorderm.setModifyDate(sysdateTime);
							paorderm.setModifyId  (Constants.PA_QEEN_PROC_ID);
							
							for(ItemGroupForSeller itemGroupForSeller : itemGroup ) {
								List<Item> itemGroupItemgs = itemGroupForSeller.getItems();
								for(Item items : itemGroupItemgs ) {
									String paShipNo = items.getId();
									if(!orderItemId.equals(paShipNo))continue;
									String paOrderSeq = items.getId().substring(0,23);
									String subPaShipNo = paShipNo.replace(orderId, "");
									String subOrderSeq = subPaShipNo.substring(0,3);
									
									paorderm.setPaShipNo(paShipNo);
									paorderm.setPaOrderSeq (paOrderSeq);
									
									executedRtn = paQeenCancelDAO.updatePaQeenCancelStatus(paorderm);
									if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAQEENCANCELLIST UPDATE" });
									
									paorderm.setPaClaimNo (subTicketId);
									paorderm.setPaOrderSeq(subOrderSeq);
									paorderm.setPaShipNo(subPaShipNo);
									
									executedRtn = paCommonDAO.insertPaOrderM(paorderm);
									if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
								}
							}
						}
					} else if(doFlag > 30) {
						// 취소 반려 
						cancelRefusalProc(cancelMap, request);
					}
				}
			}
			paramMap.put("code", "200");
			message += "퀸잇 취소 승인 성공";
		}catch (Exception e) {
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paramMap.put("message", message);
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 취소승인 API End - {} =======");
		}
		
		cancelInputMain(request);
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public ResponseMsg cancelRefusalProc(Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PAQEENAPI_03_006";
		String duplicateCheck = "";
		String paCode = "";
		String resultMsg = "";
		Timestamp sysdateTime = systemService.getSysdatetime();
		ParamMap paramMap = new ParamMap();
		ParamMap retrieveDate = null;
		
		try {
			
			log.info("======= 퀸잇 취소 반려 API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			String resultCode = null;
			
			log.info("03.API Request Setting");
			List<Map<String, Object>> cancelRefusalList = paQeenCancelDAO.selectPaQeenOrderCancelRefusalList(requestMap);
			
			paramMap.put("paCode", requestMap.get("PA_CODE"));
			
			for(Map<String, Object> cancelRefusalMap : cancelRefusalList) {
				Map<String, Object> responseMap 		 = new HashMap<String, Object>() ;
				HashMap<String, String> apiUrlParameter  = new HashMap<String, String>();
				HashMap<String, String> apiRequestObject = new HashMap<String, String>();
				int executedRtn = 0;
				
				paramMap.put("urlParameter", cancelRefusalMap.get("TICKET_ID").toString() );
				apiRequestObject.put("reason", "배송이 시작된 주문 입니다. 취소가 불가능합니다.");
				
				log.info("04.API Call");
				responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, apiUrlParameter);
				
				if(((Map<String, Object>)(responseMap)).size() < 1) {
					continue;
				}
				
				if(responseMap != null) {
					resultCode = paramMap.getString("resultCode");
					log.info("04.Processing");
					if("200".equals(resultCode)) {
						paramMap.put("procFlag", "20");
						paramMap.put("message", "취소거부완료(발송 이후 취소 건 )");
						paramMap.put("orderId", cancelRefusalMap.get("ORDER_ID"));
						paramMap.put("groupId", cancelRefusalMap.get("GROUP_ID"));
						paramMap.put("ticketId", cancelRefusalMap.get("TICKET_ID"));
					//	paramMap.put("isCustomerNegligence", responseMap.get("isCustomerNegligence").toString());
						
						executedRtn = paQeenCancelDAO.updatePaQeenCancelRefusalList(paramMap);
						if(executedRtn < 1){
							paramMap.put("code", "500");
							resultMsg += "orderId:"+cancelRefusalMap.get("ORDER_ID")+" orordItemSeq:"+cancelRefusalMap.get("GROUP_ID") +" TPAORDERM(proc_flag) UPDATE Fail/";
							paramMap.put("message", resultMsg);
						}
					}
				}
			}
		}catch (Exception e) {
			if(e.getMessage().contains("msg=Can't reject ticket of state not rejectable.") && e.getMessage().contains("code=3")) {
				int executedRtn = 0; 
				
				PaQeenCancelListVO paQeenCancelListVO = new PaQeenCancelListVO();
				paQeenCancelListVO.setOrderId(requestMap.get("ORDER_ID").toString());
				paQeenCancelListVO.setTicketId(requestMap.get("TICKET_ID").toString());
				paQeenCancelListVO.setModifyId(Constants.PA_QEEN_PROC_ID);
				
				executedRtn = paQeenCancelDAO.updateCancelWithdrawYnTx(paQeenCancelListVO);
				
				paramMap.put("code", "500");
				paramMap.put("message", "퀸잇 취소 승인 처리 실패 ("+ requestMap.get("ORDER_ID").toString() +")");
				
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TPAWEMPCLAIMLIST UPDATE" });
				}
			}
			
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 취소목록조회 API End - {} =======");
		}
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public ParamMap cancelConfirmBo(HashMap<String, Object> requestMap, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PAQEENAPI_03_018";
		String duplicateCheck = "";
		Timestamp sysdateTime = systemService.getSysdatetime();
		ParamMap paramMap = new ParamMap();
		
		try {
			log.info("======= 퀸잇 취소승인 BO API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			paramMap.put("paCode", requestMap.get("PA_CODE"));
			log.info("03.API Request Setting");
				
			Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
			HashMap<String, String> apiUrlParameter  = new HashMap<String, String>();
			HashMap<String, String> apiRequestObject  = new HashMap<String, String>();
			int executedRtn = 0;
			
			paramMap.put("urlParameter", requestMap.get("TICKET_ID").toString() );
			
			log.info("04.API Call");
			try {
				responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, apiUrlParameter);
				
				if(((Map<String, Object>)(responseMap)).size() < 1) {
					paramMap.put("code", "500");
					paramMap.put("message", "퀸잇 취소 승인 대상 없음 ");
					return paramMap;
				}
				
				ObjectMapper objectMapper = new ObjectMapper();
				OrderList cancelResponse = new OrderList();
				cancelResponse = objectMapper.convertValue(responseMap, OrderList.class);
				
				if(cancelResponse != null) {
					log.debug("getCancelList succeed");
					paramMap.put("code", "200");
					
					List<ItemGroupForSeller> itemGroup = cancelResponse.getOrderSummary().getItemGroupsForSeller();
					String paOrderNo = cancelResponse.getOrderSummary().getOrderId();
					String orgPaClaimNo = requestMap.get("TICKET_ID").toString();
					
					Paorderm paorderm = new Paorderm();
					//paorderm.setPaClaimNo (paClaimNo);
					paorderm.setPaOrderNo (paOrderNo);
					paorderm.setPaGroupCode(Constants.PA_QEEN_GROUP_CODE);
					paorderm.setPaOrderGb  ("20");
					paorderm.setPaCode	   (requestMap.get("PA_CODE").toString());
					paorderm.setPaProcQty  (String.valueOf(requestMap.get("QUANTITY")));
					if ("1".equals(requestMap.get("OUT_BEF_CLAIM_GB").toString())) {
						paorderm.setPaDoFlag("60");
					} else {
						paorderm.setPaDoFlag("20");
					}
					paorderm.setOutBefClaimGb(requestMap.get("OUT_BEF_CLAIM_GB").toString());
					paorderm.setInsertDate(sysdateTime);
					paorderm.setModifyDate(sysdateTime);
					paorderm.setModifyId  (Constants.PA_QEEN_PROC_ID);
					
					for(ItemGroupForSeller itemGroupForSeller : itemGroup ) {
						List<Item> itemGroupItemgs = itemGroupForSeller.getItems();
						String paOrderSeq = itemGroupForSeller.getGroupId();
						
						paorderm.setPaOrderSeq (paOrderSeq);
						
						for(Item items : itemGroupItemgs ) {
							String paShipNo = items.getId();
							paorderm.setPaShipNo(paShipNo);
							String paClaimNo = items.getPrimaryCancelTicket().getTicketId();
							
							paorderm.setPaClaimNo(paClaimNo);
							
							if(orgPaClaimNo.equals(paorderm.getPaClaimNo()) && paQeenCancelDAO.checkQeenCancelList(paorderm) > 0) {
								executedRtn = paQeenCancelDAO.updatePaQeenCancelStatus(paorderm);
								if(executedRtn <= 0) throw processException("msg.cannot_save", new String[] { "TPAQEENCANCELLIST UPDATE" });
								String subOrderSeq = paOrderSeq.replace(paOrderNo, "");
								String subPaShipNo = paShipNo.replace(paOrderNo, "");
								String subTicketId = paClaimNo.replace(paOrderNo, "");
								
								paorderm.setPaClaimNo (subTicketId);
								paorderm.setPaOrderSeq(subOrderSeq);
								paorderm.setPaShipNo(subPaShipNo);
								
								executedRtn = paCommonDAO.insertPaOrderM(paorderm);
								if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
								
								paorderm.setPaClaimNo (paClaimNo);
								paorderm.setPaOrderSeq(paOrderSeq);
								paorderm.setPaShipNo(paShipNo);
							}
						}
					}
					paramMap.put("code", "200");
					paramMap.put("message", "퀸잇 취소 승인 성공");
				}
			}catch (Exception e) {
				if(e.getMessage().contains("msg=Can't confirm ticket of not submitted state.") && e.getMessage().contains("code=3")) {
											
					
					PaQeenCancelListVO paQeenCancelListVO = new PaQeenCancelListVO();
					paQeenCancelListVO.setOrderId(requestMap.get("ORDER_ID").toString());
					paQeenCancelListVO.setTicketId(requestMap.get("TICKET_ID").toString());
					paQeenCancelListVO.setModifyId(Constants.PA_QEEN_PROC_ID);
					
					executedRtn = paQeenCancelDAO.updateCancelWithdrawYnTx(paQeenCancelListVO);
					
					paramMap.put("code", "500");
					paramMap.put("message", "퀸잇 취소 승인 처리 실패 ("+ requestMap.get("ORDER_ID").toString() +")");
					
					if (executedRtn < 1) {
						throw processException("msg.cannot_save", new String[] { "TPAWEMPCLAIMLIST UPDATE" });
					}
				}
			}
		}catch (Exception e) {
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 취소승인 BO API End - {} =======");
		}
		
		return paramMap;
	}

	@Override
	public List<HashMap<String, Object>> selectPaQeenSoldOutordList(ParamMap paramMap) throws Exception {
		return paQeenCancelDAO.selectPaQeenSoldOutordList(paramMap);
	}

	@Override
	public ResponseMsg cancelRequest(HashMap<String, Object> cancelItem, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PAQEENAPI_03_019";
		String duplicateCheck = "";
		String preCancelYn = "0";
		String resultMsg = null;
		String resultCode = null;
		
		ParamMap paramMap = new ParamMap();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		
		log.info("======= 퀸잇 취소 요청 API Start - {} =======");
		
		HashMap<String, Object> apiRequestObject = new HashMap<String, Object>();
		List<HashMap<String, Object>> partials = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> partial = new HashMap<String, Object>();
		HashMap<String, String> apiUrlParameter  = new HashMap<String, String>();
		
		try {
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paramMap.put("paCode", cancelItem.get("PA_CODE"));
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			log.info("03.API Request Setting");
			
			String reasonDetail ="";
			
			// 고객보상관리, 일괄취소반품(BO)에 등록된 경우, 등록된 사유 가져와서 세팅 
			if("06".equals(cancelItem.get("ORDER_CANCEL_YN")) ) {
				reasonDetail = paQeenCancelDAO.selectCustCmpstnReason(cancelItem);
				
			}
			if(reasonDetail!=null &&!reasonDetail.isEmpty()) {
				cancelItem.put("REASON_DETAIL", reasonDetail);
			}else if(!cancelItem.containsKey("REASON_DETAIL")) {
				cancelItem.put("REASON_DETAIL", "재고부족");
			}
			
			partial.put("orderItemId",  cancelItem.get("PA_SHIP_NO"));
			partial.put("quantity", cancelItem.get("PA_PROC_QTY"));
			
			partials.add(partial);
			
			// 채널이랑 사유 정해야 함 
			apiRequestObject.put("channel", "etc");
			apiRequestObject.put("reason", cancelItem.get("REASON_DETAIL").toString());
			apiRequestObject.put("isCustomerNegligence", false);
			apiRequestObject.put("partials", partials);
			
			paramMap.put("urlParameter", cancelItem.get("PA_ORDER_NO").toString());
			
			log.info("04.API Call");
			try {
				responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, apiUrlParameter);	
			}catch (Exception e) {
				if(!"200".equals(paramMap.get("resultCode"))) {
				preCancelYn = "0";
				resultMsg = e.getMessage();
				log.info("API returnCode : " + paramMap.get("resultCode") + " returnMsg : " + resultMsg);
				}
			}
			
			if (responseMap != null) {
				resultCode = paramMap.getString("resultCode");
				log.info("04.Processing");
					if("200".equals(resultCode)) {
						resultMsg = "재고부족 or 판매불가 상태 따른 판매자 취소처리";
					}
					
					HashMap<String, Object> m = new HashMap<String,Object>();
					
					m.put("resultMessage"	, resultMsg);
					m.put("resultCode"		, resultCode);
					m.put("mappingSeq"		, cancelItem.get("MAPPING_SEQ"));
					
					try {
						paQeenDeliveryDAO.updatePaorderm(m); 
					} catch (Exception e2) {
						log.error("Exception occurs : " + e2.getMessage());
					}
			}
			paramMap.put("code", "200");
			paramMap.put("message", "퀸잇 취소 요청 성공");
			
		}catch (Exception e) {
			log.error(e.getMessage());
			paramMap.put("code", "500");
			paramMap.put("message", "퀸잇 취소 요청 실패");
		}finally {
			String paOrderSeq = cancelItem.get("PA_ORDER_SEQ").toString().replace(cancelItem.get("PA_ORDER_NO").toString(), "");
			
			HashMap<String, String> m = new HashMap<String, String>();
			m.put("PA_GROUP_CODE", "15");
			m.put("PA_ORDER_NO", cancelItem.get("PA_ORDER_NO").toString());
			m.put("PA_ORDER_SEQ", paOrderSeq);
			m.put("PA_CODE", cancelItem.get("PA_CODE").toString());
			m.put("ORG_ORD_CAN_YN", cancelItem.get("ORDER_CANCEL_YN").toString());
			m.put("SITE_GB", cancelItem.get("SITE_GB").toString());
			m.put("ORDER_NO", cancelItem.get("ORDER_NO").toString());
			
			if( !"200".equals(String.valueOf(resultCode))){
				m.put("RSLT_MESSAGE","판매취소요청(일괄취소반품,품절취소반품) 실패(api 연동 실패)");
				m.put("ORDER_CANCEL_YN", "90");
			}else {
				m.put("RSLT_MESSAGE", "판매취소 성공");
				m.put("ORDER_CANCEL_YN", "10");
			}
			
			paorderService.updateOrderCancelYnTx(m);
			paCommonService.saveOrderCancelCounselTx(m);
			
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 취소 요청  API End - {} =======");
		}
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	
	private String cancelReasonMapping(CancelList cancelList) {
		String reasonType = ""; 
		
		switch (cancelList.getReason()) {
		case "마음이 바뀌었어요" : 
			reasonType = "01";
			break;
		case "주문 실수가 있었어요" : 
			reasonType = "02";
			break;
		case "배송이 너무 늦어져요" : 
			reasonType = "03";
			break;
		case "서비스가 불만족스러워요" : 
			reasonType = "04";
			break;
		case "출고 불가 연락을 받았어요" : 
			reasonType = "05";
			break;
		default :
			if(cancelList.isCustomerNegligence()) {
				reasonType = "06";
			}else {
				reasonType = "07";
			}
			break;
		}
		
		return reasonType;
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception {
		return paQeenCancelDAO.selectPaMobileOrderAutoCancelList();
	}

	@Override
	public ResponseMsg mobileOrderSoldOut(HashMap<String, String> cancelItem, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PAQEENAPI_03_024";
		String duplicateCheck = "";
		String resultCode = null;
		String paOrderNo = cancelItem.get("PA_ORDER_NO").toString();
		
		ParamMap paramMap = new ParamMap();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		
		log.info("======= 퀸잇 모바일자동취소  API Start - {} =======");
		
		HashMap<String, Object> apiRequestObject = new HashMap<String, Object>();
		List<HashMap<String, Object>> partials = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> partial = new HashMap<String, Object>();
		HashMap<String, String> apiUrlParameter  = new HashMap<String, String>();
		HashMap<String, String> map = new HashMap<String, String>();
		
		try {
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paramMap.put("paCode", cancelItem.get("PA_CODE"));
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			log.info("03.API Request Setting");
			
			cancelItem.put("REASON_DETAIL", "재고부족");
			partial.put("orderItemId",  cancelItem.get("PA_SHIP_NO"));
			partial.put("quantity", cancelItem.get("PA_PROC_QTY"));
			partials.add(partial);
			
			map.put("PA_GROUP_CODE", Constants.PA_QEEN_GROUP_CODE);
			map.put("PA_CODE", cancelItem.get("PA_CODE").toString());
			map.put("PA_ORDER_NO", paOrderNo);
			map.put("PA_ORDER_SEQ", cancelItem.get("PA_ORDER_SEQ").toString());
			map.put("ORDER_NO", cancelItem.get("ORDER_NO").toString());
			map.put("ORDER_G_SEQ", cancelItem.get("ORDER_G_SEQ"));
			map.put("MAPPING_SEQ", cancelItem.get("MAPPING_SEQ").toString()); // 퀸잇의 경우 다수  옵션의 경우 같은 PA_ORDER_SEQ를 가짐으로 해당 조건 필요
		 	map.put("PROC_ID", Constants.PA_QEEN_PROC_ID);
			
			// 채널이랑 사유 정해야 함 
			apiRequestObject.put("channel", "etc");
			apiRequestObject.put("reason", cancelItem.get("REASON_DETAIL").toString());
			apiRequestObject.put("isCustomerNegligence", false);
			apiRequestObject.put("partials", partials);
			
			paramMap.put("urlParameter", paOrderNo);
			
			log.info("04.API Call");
			try {
				responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, apiUrlParameter);	
				if (responseMap != null) {
					resultCode = paramMap.getString("resultCode");
					log.info("04.Processing");
						if("200".equals(resultCode)) {
							mobileOrderSoldOutSeuccess(map, paramMap);
						} else {
							mobileOrderSoldOutFail(map, paramMap, "");
						}
				}
			} catch (Exception e) {
				if(e.getMessage().indexOf("이미 취소된 주문입니다.") > 0) {
					mobileOrderSoldOutSeuccess(map, paramMap);
				} else {
					log.error(e.getMessage());
					mobileOrderSoldOutFail(map, paramMap, e.getMessage());
				}
			}
		}catch (Exception e) {
			log.error(e.getMessage());
			paramMap.put("code", "500");
			paramMap.put("message", "퀸잇 모바일자동취소 요청 실패");
		}finally {
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 모바일자동취소  API End - {} =======");
		}
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	
	public void mobileOrderSoldOutSeuccess(HashMap<String, String> map, ParamMap paramMap) throws Exception {
		map.put("REMARK3_N", "10");
		map.put("RSLT_MESSAGE", "모바일자동취소 성공");
		paorderService.updateRemark3NTx(map);
		
		//상담생성 & 문자발송 & 상품품절처리
		paCommonService.savePaMobileOrderCancelTx(map);
		
		paramMap.put("code", "200");
		paramMap.put("message", "퀸잇 모바일 자동취소 성공");
	}
	
	public void mobileOrderSoldOutFail(HashMap<String, String> map, ParamMap paramMap, String msg) throws Exception {
		paramMap.put("code", "500");
		paramMap.put("message", "퀸잇 모바일 자동취소 실패" + msg);
		
		map.put("REMARK3_N", "90");
		map.put("RSLT_MESSAGE", "모바일자동취소 실패 " + msg);
		paorderService.updateRemark3NTx(map);
	}
}	

