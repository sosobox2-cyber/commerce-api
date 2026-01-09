package com.cware.netshopping.panaver.v3.process.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cware.api.panaver.message.v3.ChangedProductOrderInfoListMsg;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.panaver.v3.domain.ChangedProductOrderInfo;
import com.cware.netshopping.panaver.v3.domain.ChangedProductOrderInfoResponse;
import com.cware.netshopping.panaver.v3.domain.ChangedProductOrderInfoResponseData;
import com.cware.netshopping.panaver.v3.domain.ClaimDeliveryFeePayMethodType;
import com.cware.netshopping.panaver.v3.domain.ClaimStatusType;
import com.cware.netshopping.panaver.v3.domain.DeliveryMethodType;
import com.cware.netshopping.panaver.v3.domain.GiftReceivingStatusType;
import com.cware.netshopping.panaver.v3.domain.HoldbackStatusType;
import com.cware.netshopping.panaver.v3.domain.PaNaverV3AddressVO;
import com.cware.netshopping.panaver.v3.domain.PaNaverV3ClaimListVO;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfoAll;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfoMsg;
import com.cware.netshopping.panaver.v3.domain.ProductOrderStatusType;
import com.cware.netshopping.panaver.v3.domain.ShippingAddress;
import com.cware.netshopping.panaver.v3.domain.TakingAddress;
import com.cware.netshopping.panaver.v3.process.PaNaverV3InfoCommonProcess;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3CancelDAO;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3DeliveryDAO;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3ExchangeDAO;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3InfoCommonDAO;
import com.cware.netshopping.panaver.v3.service.PaNaverV3DeliveryService;
import com.cware.netshopping.panaver.v3.service.PaNaverV3InfoCommonService;
import com.cware.netshopping.panaver.v3.util.PaNaverV3ConnectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("panaver.v3.infocommon.paNaverV3InfoCommonProcess")
public class PaNaverV3InfoCommonProcessImpl extends AbstractProcess implements PaNaverV3InfoCommonProcess {
	
	@Autowired
	PaNaverV3InfoCommonDAO paNaverV3InfoCommonDAO;
	
	@Autowired
	PaNaverV3DeliveryDAO paNaverV3DeliveryDAO;
	
	@Autowired
	PaNaverV3CancelDAO paNaverV3CancelDAO;
	
	@Autowired
	PaNaverV3ExchangeDAO paNaverV3ExchangeDAO;
	
	@Autowired
	PaCommonDAO paCommonDAO;
	
	@Autowired
	SystemDAO systemDAO;
	
	@Autowired
	private PaNaverV3ConnectUtil paNaverV3ConnectUtil;
	
	@Resource(name = "panaver.v3.infocommon.paNaverV3InfoCommonService")
	private PaNaverV3InfoCommonService paNaverV3InfoCommonService;
	
	@Resource(name = "panaver.v3.delivery.paNaverV3DeliveryService")
	private PaNaverV3DeliveryService paNaverV3DeliveryService;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Override
	public ChangedProductOrderInfoListMsg getChangedProductOrderInfoList(String lastChangedType, String fromDate, String toDate, String productOrderId, int limitCount, String procId, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PANAVERAPI_V3_03_002";
		String prgId = apiCode+"_"+lastChangedType;
		String duplicateCheck = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<ChangedProductOrderInfo> responseList = new ArrayList<ChangedProductOrderInfo>();
		List<ChangedProductOrderInfo> resultList = new ArrayList<ChangedProductOrderInfo>();

		boolean isRequestDone = false;
		
		// API 트래킹 로그 데이터 세팅
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", procId);

		log.info("======= 네이버 변경 상품 주문 내역 조회 API Start - {} =======", lastChangedType);
		
		try {
			// API 중복실행 체크
			duplicateCheck = systemService.checkCloseHistoryTx("start", prgId);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prgId});
			
			/** 1) 전문 데이터 세팅  **/
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			String dateTime = systemService.getSysdatetimeToString();
			
			Calendar from = Calendar.getInstance();
			Calendar to = Calendar.getInstance();
			
			if (fromDate != null && !fromDate.equals("")) {
				from.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(fromDate));
			} else {
				from.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateTime));
				from.add(Calendar.DATE, -1);
			}
			
			if (toDate != null && !toDate.equals("")) {
				to.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(toDate));
			} else {
				to.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateTime));
				to.add(Calendar.SECOND, -5); // 조회 종료 일시는 현재 시각(API 호출 시각)으로부터 약 5초 이전으로 설정 (커머스API 권장사항)
			}
			
			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();
			queryParameters.put("lastChangedType", lastChangedType);
			queryParameters.put("lastChangedFrom", dateFormat.format(from.getTime()));
			queryParameters.put("lastChangedTo", dateFormat.format(to.getTime()));
			queryParameters.put("limitCount", Integer.toString(limitCount));
			
			// Path Parameters
			String pathParameters = "";
			
			// VO 선언
			ChangedProductOrderInfoResponse response = new ChangedProductOrderInfoResponse();
			
			while (!isRequestDone) {
				Thread.sleep(500); // 네이버 호출량 제어 (1초당 2건)
				
				log.info("네이버 변경 상품 주문 내역 조회  API 호출 {}", lastChangedType);
				
				/** 2) 네이버 변경 상품 주문 내역 조회 호출 **/
				resultMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, null);
				
				// Map -> VO 변환
				ObjectMapper objectMapper = new ObjectMapper();
				response = objectMapper.convertValue(resultMap, ChangedProductOrderInfoResponse.class);
				
				ChangedProductOrderInfoResponseData responseData = response.getData();
				
				if (responseData != null) {
					log.debug("GetChangedProductOrderList requestId :: {} succeed", response.getTraceId());
					paramMap.put("code", "200");

					if (responseData.getCount() > 0) {
						log.debug("retreived changed order count : {}", responseData.getCount());				
						isRequestDone = (response.getData().getMore() == null) ? true : false;
						
						responseList.addAll(responseData.getChangedProductOrderInfoList()); // 변경 상품 주문 내역 조회 결과
						
						/** 3) 변경 상품 주문 내역 조회 결과 저장 (TPANAVERORDERCHANGE) **/	
						List<ChangedProductOrderInfo> savedOrderList = paNaverV3InfoCommonService.mergeChangeOrderListTx(responseData.getChangedProductOrderInfoList(), productOrderId);
						
						resultList.addAll(savedOrderList); // 변경 상품 주문 내역 조회 신규 저장건 (제휴 데이터 생성 대상)
						
						log.debug("saved changed order count : {}", savedOrderList.size());
						
						if (paramMap.getString("message") != null && !"".equals(paramMap.getString("message"))) {
							paramMap.put("message", paramMap.getString("message")+ " + (" + String.valueOf(savedOrderList.size()) + ")");
						} else {
							paramMap.put("message", "주문 변경 내역 (" + String.valueOf(savedOrderList.size()) + ") 건 저장");
						}
						
						// 추가 응답 항목 존재 체크 > moreSequence 이상인 항목만 조회
						if (!isRequestDone) {
							queryParameters.put("lastChangedFrom", responseData.getMore().getMoreFrom());
							queryParameters.put("moreSequence", responseData.getMore().getMoreSequence());
						}
					} else {
						log.debug("retreived changed order count : 0");
						paramMap.put("message", "주문 변경 내역 (0) 건 조회");
					}
				} else {
					log.error("GetChangedProductOrderList requestId :: {} failed", response.getTraceId());
					paramMap.put("code", "400");
					paramMap.put("message", "GetChangedProductOrderList " + response.getMessage());
					
					isRequestDone = true;
				}		
			}
		} catch (TransApiException ex) {
			paramMap.put("code", "500");
			paramMap.put("message", ex.getMessage());
			log.error(ex.getMessage(), ex);
		} catch (Exception e) {
			paNaverV3ConnectUtil.dealException(e, paramMap);
			log.error(e.getMessage(), e);
		} finally {
			try {
				paNaverV3ConnectUtil.dealSuccess(paramMap, request);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			
			if (duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prgId);
			
			log.info("======= 네이버 변경 상품 주문 내역 조회 API End - {} =======", lastChangedType);
		}
		return new ChangedProductOrderInfoListMsg(paramMap.getString("code"), paramMap.getString("message"), resultList, responseList);
	}

	@Override
	public List<ChangedProductOrderInfo> mergeChangeOrderListProc(List<ChangedProductOrderInfo> changedProductOrderInfoList, String productOrderId) throws Exception {
		List<ChangedProductOrderInfo> resultList = new ArrayList<ChangedProductOrderInfo>();
		int result = 0;
		
		try {
			for(ChangedProductOrderInfo order : changedProductOrderInfoList) {
				
				// 타켓 상품주문번호 존재하는 경우, 해당 상품주문번호만 적재 처리
				if (!productOrderId.isEmpty() && !order.getProductOrderId().equals(productOrderId)) {
					continue; 
				}
				
				// 결제 대기건 스킵 (결제 완료 후, 발주처리 및 주문생성)
				if (order.getProductOrderStatus().equals(ProductOrderStatusType.PAYMENT_WAITING.toString())) {
					continue; 
				}
				
				// 선물하기 주문건(수락 대기) 제외 (추후 수락완료(배송지입력) 후, 발주처리 및 주문생성)
				if (order.getGiftReceivingStatus() != null && (order.getGiftReceivingStatus().equals(GiftReceivingStatusType.WAIT_FOR_RECEIVING.toString()))) {
					continue;
				}
				
				result = paNaverV3InfoCommonDAO.mergeChangeOrderList(order); // 변경 상품 주문내역 조회 결과 저장 (INSERT TPANAVERORDERCHANGE)
	
				if (result > 0) resultList.add(order);
			}
		} catch (Exception e) {
			throw processException("msg.cannot_save", new String[] { "TPANAVERORDERCHANGE MERGE" });
		}
		return resultList;
	}
	
	@Override
	public List<Object> selectOrderInputTargetList() throws Exception {
		return paNaverV3InfoCommonDAO.selectOrderInputTargetList();
	}
	
	@Override
	public List<Object> selectCancelInputTargetList() throws Exception {
		return paNaverV3InfoCommonDAO.selectCancelInputTargetList();
	}
	
	@Override
	public List<Object> selectOrderClaimTargetList() throws Exception {
		return paNaverV3InfoCommonDAO.selectOrderClaimTargetList();
	}
	
	@Override
	public List<Object> selectOrderChangeTargetList() throws Exception {
		return paNaverV3InfoCommonDAO.selectOrderChangeTargetList();
	}
	
	@Override
	public List<Object> selectClaimCancelTargetList() throws Exception {
		return paNaverV3InfoCommonDAO.selectClaimCancelTargetList();
	}
	
	@Override
	public List<Object> selectChangeCancelTargetList() throws Exception {
		return paNaverV3InfoCommonDAO.selectChangeCancelTargetList();
	}

	@Override
	public int updateChangeApplied(ChangedProductOrderInfo order) throws Exception {
		return paNaverV3InfoCommonDAO.updateChangeApplied(order);
	}

	@Override
	public ProductOrderInfoAll insertCancelInfoProc(ProductOrderInfoAll productOrderInfo) throws Exception {
		ParamMap paramMap = new ParamMap();
		
		String procId = "PANAVER";
		
		boolean preCanceled = false;
		ProductOrderInfoAll cancelApprovalTarget = null; // 네이버 취소승인 API 호출 대상
		
		String productOrderId = productOrderInfo.getProductOrder().getProductOrderId();
		String orderId = productOrderInfo.getOrder().getOrderId();
		String claimId = productOrderInfo.getProductOrder().getClaimId();

		paramMap.put("productOrderId", productOrderId);
		paramMap.put("orderId", orderId);
		paramMap.put("claimId", claimId);

		/** 네이버 취소 데이터 생성 및 업데이트 **/
		// 1. TPANAVERCLAIMLIST INSERT
		// 2. TPANAVERORDERLIST UPDATE
		// 3. TPAORDERM INSERT
		if (productOrderInfo.getCancelOrder() != null) {
			/** 1) 네이버 취소 데이터 생성전 선처리 (기취소건, 취소완료건) **/
			// 1-1) 기취소건(주문 생성이전 취소건) > 네이버 주문 데이터 저장 (TPANAVERORDERM, TPANAVERORDERLIST)
			preCanceled = checkPreCancelOrder(productOrderInfo);

			// 1-2) 취소완료(기취소 제외) > 취소완료일 업데이트
			if (!preCanceled) {
				if (checkCancelCompleteOrder(productOrderInfo)) {
					return null; // 네이버 주문취소 정보 저장 및 업데이트 스킵
				}
			}
			
			// 1-3) 취소승인 미처리건 체크 (취소승인 재처리)
			if (paNaverV3InfoCommonDAO.selectCancelProcTarget(paramMap) > 0) {
				return productOrderInfo;
			}
			
			/** 2) 네이버 주문취소 정보 저장 및 업데이트 (TPANAVERCLAIMLIST INSERT, TPANAVERORDERLIST UPDATE) **/
			// 2-1) TPANAVERCLAIMLIST INSERT
			PaNaverV3ClaimListVO claim = new PaNaverV3ClaimListVO();
			claim.setClaimSeq(paNaverV3InfoCommonDAO.selectClaimSeq());
			claim.setProductOrderId(productOrderId);
			claim.setCancelInfo(productOrderInfo.getCancelOrder());
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String sysdate = systemDAO.getSysdatetime();
			claim.setInsertDate(new Timestamp(sdf.parse(sysdate).getTime()));
			claim.setModifyDate(new Timestamp(sdf.parse(sysdate).getTime()));
			
			// 직권취소완료 > 반품접수
			if (claim.getClaimStatus().equals(ClaimStatusType.ADMIN_CANCEL_DONE.toString())) {
				claim.setClaimGb("30");
			} else {
				claim.setClaimGb("20");
			}
			
			if (paNaverV3InfoCommonDAO.insertCancelClaim(claim) != 1) {
				throw processException("msg.cannot_save", new String[] { "TPANAVERCLAIMLIST INSERT" });
			}
			
			// 2-2) TPANAVERORDERLIST UPDATE
			paramMap.put("claimSeq", String.valueOf(claim.getClaimSeq()));
			paramMap.put("claimType", "CANCEL");
			paramMap.put("claimStatus", claim.getClaimStatus());

			if (productOrderInfo.getProductOrder().getClaimStatus().equals(ClaimStatusType.CANCEL_DONE.toString())) {
				paramMap.put("procFlag", "10"); // 취소완료 데이터 저장 실패건 > 제휴 주문/취소 진행단계 승인(10) [O513]
			}

			if (paNaverV3InfoCommonDAO.updateOrderListClaimInfo(paramMap) != 1) {
				throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST UPDATE" });
			}
			
			/** 3) 제휴 주문취소 정보 저장 (TPAORDERM INSERT) **/
			Paorderm paorderm = new Paorderm();	
			
			if (productOrderInfo.getProductOrder().getClaimStatus().equals(ClaimStatusType.CANCEL_REQUEST.toString())) {
				// 3-1) 취소요청
				paorderm.setPaCode("41");
				paorderm.setPaOrderGb("20"); // 취소
				paorderm.setPaOrderNo(orderId);
				paorderm.setPaOrderSeq(productOrderId);
				paorderm.setPaShipNo("");
				paorderm.setPaClaimNo(claimId);
				paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
				paorderm.setPaDoFlag("00");
				paorderm.setPaGroupCode("04");
				paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
				
 				// 주문 진행단계 조회
 				String doFlag = paNaverV3InfoCommonDAO.selectOrderdtDoFlag(paramMap);
 				
 				// 출하지시 여부 확인
				if (doFlag != null && Integer.parseInt(doFlag) >= 30) {
					paorderm.setOutBefClaimGb("1"); // 반품접수대상
					paorderm.setPaDoFlag("60"); // 반품생성 성공
				} else {
					paorderm.setOutBefClaimGb("0");
					paorderm.setPaDoFlag("20"); // 주문취소 성공
				}
				paorderm.setInsertDate(claim.getInsertDate());
				paorderm.setModifyDate(claim.getModifyDate());
				paorderm.setModifyId(procId);
				
				if (productOrderInfo.getCancelOrder().getRequestChannel().equals("API")) paorderm.setPreCancelYn("1");
				
				// 제휴 주문취소 생성 여부 체크 (TPAORDERM)
				if (paNaverV3InfoCommonDAO.selectPaOrderMCancelCnt(paorderm) < 1) {
					// 취소 데이터 INSERT (TPAORDERM)
					if (paCommonDAO.insertPaOrderM(paorderm) != 1) {
						throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
					}
				} else {
					paramMap.put("paOrderGb", "20"); // 취소
					
					// 취소 데이터 UPDATE (TPAORDERM.PA_CLAIM_NO)
					if (paNaverV3InfoCommonDAO.updatePaordermClaimId(paramMap) != 1) {
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
					}
				}
				
				// 출하지시 이전 취소요청건은 바로 취소승인 처리
				if (paorderm.getOutBefClaimGb().equals("0")) cancelApprovalTarget = productOrderInfo;
			} else if (productOrderInfo.getProductOrder().getClaimStatus().equals(ClaimStatusType.ADMIN_CANCEL_DONE.toString())) {
				// 3-2) 직권취소완료 > 반품접수
				paorderm = new Paorderm();
				paorderm.setPaCode("41");
				paorderm.setPaOrderNo(orderId);
				paorderm.setPaOrderSeq(productOrderId);
				paorderm.setPaClaimNo(claimId);
				paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
				paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
				paorderm.setPaDoFlag("60"); // 반품승인
				paorderm.setPaOrderGb("30"); // 반품
				paorderm.setOutBefClaimGb("0");
				paorderm.setPaGroupCode("04");
				paorderm.setInsertDate(claim.getInsertDate());
				paorderm.setModifyDate(claim.getModifyDate());
				paorderm.setModifyId(procId);
				
				// 반품 데이터 INSERT (TPAORDERM)
				if (paCommonDAO.insertPaOrderM(paorderm) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}
			} else if (productOrderInfo.getProductOrder().getClaimStatus().equals(ClaimStatusType.CANCEL_DONE.toString())) {
				// 3-3) 취소완료 (기취소건, 미결제취소완료건)
				HashMap<String, String> cancelDoneInfo = paNaverV3InfoCommonDAO.selectCancelDoneInfo(productOrderId); // 네이버 주문취소 완료건 조회 (TPANAVERORDERLIST, TPANAVERCLAIMLIST)
				
				if (cancelDoneInfo == null) {
					paorderm.setPaCode("41");
					paorderm.setPaOrderGb("20"); // 취소
					paorderm.setPaGroupCode("04");
					paorderm.setPaOrderNo(orderId);
					paorderm.setPaOrderSeq(productOrderId);
					paorderm.setPaShipNo("");
					paorderm.setPaClaimNo(claimId);
					paorderm.setPaProcQty(ComUtil.objToStr(productOrderInfo.getProductOrder().getQuantity()));
					paorderm.setPaDoFlag("20");
					
					String doFlag = paNaverV3InfoCommonDAO.selectOrderdtDoFlag(paramMap); // 주문 진행단계 조회
					
					// 출하지시 여부 확인
					if (doFlag != null && Integer.parseInt(doFlag) >= 30) {
						paorderm.setOutBefClaimGb("1"); // 반품접수대상
						paorderm.setPaDoFlag("60"); // 반품생성 성공
					} else {
						paorderm.setOutBefClaimGb("0");
						paorderm.setPaDoFlag("20");	// 주문취소 성공	
					}
					paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
					paorderm.setInsertDate(claim.getInsertDate());
					paorderm.setModifyDate(claim.getModifyDate());
					paorderm.setModifyId(procId);
					
					// 제휴 주문취소 생성 여부 확인 (TPAORDERM)
					if (paNaverV3InfoCommonDAO.selectPaOrderMCancelCnt(paorderm) < 1) {
						// 취소 데이터 INSERT (TPAORDERM)
						if (paCommonDAO.insertPaOrderM(paorderm) != 1) {
							throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
						}
						
						// 기취소건 (TPAORDERM.PRE_CANCEL_YN UPDATE)
						if (preCanceled) {
							paramMap.put("paOrderGb", "20");
							paramMap.put("mappingSeq", paNaverV3DeliveryDAO.selectMappingSeqByProductOrderInfo(paramMap));
							paramMap.put("preCancelYn", "1");
							paramMap.put("preCancelReason", productOrderInfo.getCancelOrder().getCancelReason().toString());
							paNaverV3CancelDAO.updatePreCancelYn(paramMap);
						}
					}
				}
			} else if (productOrderInfo.getProductOrder().getClaimStatus().equals(ClaimStatusType.CANCEL_REJECT.toString())) {
				// 3-4) 출고전 반품 기취소건
				paramMap.put("paOrderGb", "20");
				paramMap.put("outBefClaimGb", "1");
				paramMap.put("mappingSeq", paNaverV3InfoCommonDAO.selectMappingSeqByProductOrderInfo(paramMap));
				paramMap.put("preCancelYn", "1");
				paramMap.put("preCancelReason", productOrderInfo.getCancelOrder().getCancelReason().toString());
				paNaverV3CancelDAO.updatePreCancelYn(paramMap);
			}
		}
		return cancelApprovalTarget;
	}

	private boolean checkPreCancelOrder(ProductOrderInfoAll productOrderInfo) throws Exception {
		boolean preCanceled = false;
		
		// 기취소건(주문 생성이전 취소건) > 네이버 주문 데이터 저장 (TPANAVERORDERM, TPANAVERORDERLIST)
		if (paNaverV3InfoCommonDAO.selectOrderListCnt(productOrderInfo.getProductOrder().getProductOrderId()) < 1) {
			ParamMap paramMap = new ParamMap();
			paramMap.put("productOrderId", productOrderInfo.getProductOrder().getProductOrderId());
			paramMap.put("orderId", productOrderInfo.getOrder().getOrderId());
			
			preCanceled = true;

			// 기취소 주문건 저장 (TPANAVERORDERM)
			if (paNaverV3DeliveryDAO.mergeNaverOrderm(productOrderInfo.getOrder()) < 1) {
				throw processException("msg.cannot_save", new String[] { "TPANAVERORDERM INSERT" });
			}
			
			// 기취소 주문건 저장 (TPANAVERORDERLIST)
			if (paNaverV3DeliveryDAO.mergeOrderList(productOrderInfo.getProductOrder()) < 1) {
				throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST INSERT" });
			}
			
			// 배송지 주소 저장 (TPANAVERADDRESS)
			ShippingAddress shippingAddress = productOrderInfo.getProductOrder().getShippingAddress();
			if (shippingAddress != null) {
				PaNaverV3AddressVO address = new PaNaverV3AddressVO();
				address.setAddressSeq(paNaverV3InfoCommonDAO.selectAddressSeq());	
				address.setShippingAddress(shippingAddress);
				
				if (paNaverV3InfoCommonDAO.insertPaNaverAddress(address) < 1) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERADDRESS SHIPPINGADDRESS INSERT" });
				}
				shippingAddress.setAddressSeq(address.getAddressSeq());
			}
			
			// 출고지 주소 저장 (TPANAVERADDRESS)
			TakingAddress takingAddress = productOrderInfo.getProductOrder().getTakingAddress();
			if (takingAddress != null) {
				PaNaverV3AddressVO address = new PaNaverV3AddressVO();
				address.setAddressSeq(paNaverV3InfoCommonDAO.selectAddressSeq());	
				address.setTakingAddress(takingAddress);

				if (paNaverV3InfoCommonDAO.insertPaNaverAddress(address) < 1) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERADDRESS TAKINGADDRESS INSERT" });
				}
				takingAddress.setAddressSeq(address.getAddressSeq());
			}
			
			// 주문 배송지/출고지 정보 업데이트 (TPANAVERORDERLIST)
			if (shippingAddress.getAddressSeq() != null || takingAddress.getAddressSeq() != null) {
				paramMap.put("shippingAddressSeq", shippingAddress.getAddressSeq());
				paramMap.put("takingAddressSeq", takingAddress.getAddressSeq());
				
				if (paNaverV3InfoCommonDAO.updateOrderListAddress(paramMap) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST ADDRESS UPDATE" });
				}
			}	
			
			// 주문번호 업데이트 (TPANAVERORDERLIST.ORDER_ID)
			if (paNaverV3InfoCommonDAO.updateOrderListOrderId(paramMap) < 0) {
				throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST ORDERID UPDATE" });
			}
		}
		return preCanceled;
	}
	
	private boolean checkCancelCompleteOrder(ProductOrderInfoAll productOrderInfo) throws Exception {
		boolean cancelCompleteYn = false;
		
		// 취소완료(기취소 제외) > 취소완료일 업데이트
		if (productOrderInfo.getCancelOrder().getClaimStatus().equals(ClaimStatusType.CANCEL_DONE.toString())) {
			ParamMap paramMap = new ParamMap();
			paramMap.put("productOrderId", productOrderInfo.getProductOrder().getProductOrderId());
			paramMap.put("cancelCompletedDate", productOrderInfo.getCancelOrder().getCancelCompletedDate());
			
			String claimSeq = paNaverV3InfoCommonDAO.selectCancelDoneClaim(paramMap); // 취소완료건 시퀀스 정보 조회 (TPANAVERCLAIMLIST)
			
			if (claimSeq != null) {
				paramMap.put("claimSeq", claimSeq);
				
				// 취소완료일 UPDATE (TPANAVERCLAIMLIST)
				if (paNaverV3CancelDAO.updateCancelDoneClaim(paramMap) != 1) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERCLAIMLIST UPDATE" });
				}
				
				// 네이버 주문취소 정보 저장 및 업데이트 스킵
				cancelCompleteYn = true;
			}
		}
		return cancelCompleteYn;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ProductOrderInfoMsg orderDetailInfo(String productOrderIds, String procId, HttpServletRequest request) throws Exception {	
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PANAVERAPI_V3_03_001";
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("siteGb", "PANAVER");
		
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		List<ProductOrderInfoAll> responseList  = new ArrayList<ProductOrderInfoAll>();
		
		log.info("======= 네이버 상품 주문 상세 내역 조회 API Start =======");
		try {
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			// Body 세팅S
			ParamMap apiDataObject = new ParamMap();
			apiDataObject.put("productOrderIds",productOrderIds.split(","));
			// Body 세팅 E
			
			responseMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);
			
			// List변환
			ObjectMapper objectMapper = new ObjectMapper();
			responseList = objectMapper.convertValue((List<ProductOrderInfoAll>) responseMap.get("data"), new TypeReference<List<ProductOrderInfoAll>>() {});	
			
			if(responseList.size()>0) {
				paramMap.put("code", "200");
				paramMap.put("message", "네이버 주문 상세내역 조회 완료");
			}else {
				paramMap.put("code", "400");
				paramMap.put("message", getMessage("errors.no.select"));
				return new ProductOrderInfoMsg (paramMap.getString("code"),paramMap.getString("message"), responseList);
			}
			
		} catch (TransApiException ex) {
			paramMap.put("code", "500");
			paramMap.put("message", ex.getMessage());
			log.error(paramMap.getString("message"), ex);
			return new ProductOrderInfoMsg (paramMap.getString("code"),paramMap.getString("message"), responseList);
		} catch (Exception e) {
			// TODO: handle exception
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage());
			paNaverV3ConnectUtil.dealException(e, paramMap);
			log.error(paramMap.getString("message"), e);
			return new ProductOrderInfoMsg (paramMap.getString("code"),paramMap.getString("message"), responseList);
		} finally {
			try{
				paNaverV3ConnectUtil.dealSuccess(paramMap, request);
			} catch(Exception e){
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
		}
		
		log.info("======= 네이버 상품 주문 상세 내역 조회 API End =======");
		
		return new ProductOrderInfoMsg (paramMap.getString("code"),paramMap.getString("message"), responseList);
	}

	@Override
	public ProductOrderInfoAll insertExchangeInfo(ProductOrderInfoAll productOrderInfo) throws Exception {
		ParamMap paramMap = new ParamMap();
		PaNaverV3ClaimListVO claim = new PaNaverV3ClaimListVO(); 
		String sysdate;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		PaNaverV3AddressVO address;
		Paorderm paorderm = null;
		
		/** 네이버 교환 데이터 생성 및 업데이트 **/
		// 1. TPANAVERADDRESS INSERT
		// 2. TPANAVERCLAIMLIST INSERT
		// 3. TPANAVERORDERLIST UPDATE
		// 4. TPAORDERM INSERT
		claim.setClaimSeq(paNaverV3InfoCommonDAO.selectClaimSeq());
		claim.setProductOrderId(productOrderInfo.getProductOrder().getProductOrderId());
		claim.setExchangeInfo(productOrderInfo.getExchangeOrder());
		
		/** 1) 네이버 주문교환 주소지 저장 (TPANAVERADDRESS INSERT) **/
		// 1-1) 수거지 주소 저장 (TPANAVERADDRESS)
		if (productOrderInfo.getExchangeOrder().getCollectAddress() != null) {
			address = new PaNaverV3AddressVO();
			
			address.setCollectAddress(productOrderInfo.getExchangeOrder().getCollectAddress());
			address.setAddressSeq(paNaverV3InfoCommonDAO.selectAddressSeq());
			if (paNaverV3InfoCommonDAO.insertPaNaverAddress(address) == 1) claim.setCollectAddressSeq(address.getAddressSeq());;
		}
		
		// 1-2) 반품지 주소 저장 (TPANAVERADDRESS)
		if (productOrderInfo.getExchangeOrder().getReturnReceiveAddress() != null) {
			address = new PaNaverV3AddressVO();
			
			address.setReturnReceiveAddress(productOrderInfo.getExchangeOrder().getReturnReceiveAddress());
			address.setAddressSeq(paNaverV3InfoCommonDAO.selectAddressSeq());				
			if (paNaverV3InfoCommonDAO.insertPaNaverAddress(address) == 1) claim.setReturnReceiveAddress(address.getAddressSeq());
		}
		sysdate = systemDAO.getSysdatetime();
		claim.setInsertDate(new Timestamp(sdf.parse(sysdate).getTime()));
		claim.setModifyDate(new Timestamp(sdf.parse(sysdate).getTime()));
		
		/** 2) 네이버 클레임 정보 저장 (TPANAVERCLAIMLIST INSERT) **/
		if (paNaverV3InfoCommonDAO.insertExchangeClaim(claim) != 1) {
			throw processException("msg.cannot_save", new String[] { "TPANAVERCLAIMLIST INSERT" });
		}
		paramMap.put("claimSeq", String.valueOf(claim.getClaimSeq()));
		paramMap.put("claimType", "EXCHANGE");
		paramMap.put("claimStatus", claim.getClaimStatus());
		paramMap.put("orderId", productOrderInfo.getOrder().getOrderId());
		paramMap.put("productOrderId", productOrderInfo.getProductOrder().getProductOrderId());
		paramMap.put("claimId", productOrderInfo.getProductOrder().getClaimId());
		
		/** 3) 네이버 주문 정보 업데이트 (TPANAVERORDERLIST UPDATE) **/
		if (paNaverV3InfoCommonDAO.updateOrderListClaimInfo(paramMap) != 1) {
			throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST EXCHANGEINFO UPDATE" });
		}
		
		// 교환 보류건 체크 (교환 배송비 존재시, 자동 교환 보류 > 교환 배송비 결제시 보류 해제)
		// 반품안심케어 대상건은 제외
		if (productOrderInfo.getExchangeOrder().getHoldbackStatus() != null 
				&& productOrderInfo.getExchangeOrder().getHoldbackStatus().equals(HoldbackStatusType.HOLDBACK.toString())
				&& !ClaimDeliveryFeePayMethodType.UNCLAIMED.codeName().equals(productOrderInfo.getExchangeOrder().getClaimDeliveryFeePayMethod())) {
			return null;
		}				
		
		// 단품정보 조회
		List<HashMap<String, String>> goodsInfo = paNaverV3ExchangeDAO.selectGoodsdtInfo(productOrderInfo.getProductOrder().getProductId());
		
		// 교환 진행단계 조회 (상품 주문번호, 클레임 번호 > TPAORDERM)
		String collectDoFlag = paNaverV3InfoCommonDAO.selectExchangeCollectDoFlag(paramMap);
		
		/** 4) 제휴 교환 정보 저장 (TPAORDERM INSERT) **/
		if (collectDoFlag == null 
				&& !productOrderInfo.getExchangeOrder().getClaimStatus().equals(ClaimStatusType.EXCHANGE_REJECT.toString())) { // 신규 교환요청건		
			paorderm = new Paorderm();
			
			// 단일상품 체크 > 동일옵션으로 교환
			if (goodsInfo.size() == 1) {
				paorderm.setChangeGoodsdtCode(goodsInfo.get(0).get("GOODSDT_CODE"));
				paorderm.setChangeFlag("01"); // 처리완료
			} else {
				paorderm.setChangeGoodsdtCode("");
				paorderm.setChangeFlag("00"); // 미처리
			}
			paorderm.setChangeGoodsCode(goodsInfo.get(0).get("GOODS_CODE"));
			paorderm.setPaCode("41");	
			paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderId());
			paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderId());
			paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimId());
			paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
			paorderm.setPaGroupCode("04");
			paorderm.setOutBefClaimGb("0");
			paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
			paorderm.setInsertDate(claim.getInsertDate());
			paorderm.setModifyDate(claim.getModifyDate());
			paorderm.setModifyId("PANAVER");
			
			// 제휴사 배송단계 세팅
			if (productOrderInfo.getExchangeOrder().getClaimStatus().equals(ClaimStatusType.COLLECT_DONE.toString())) {
				paorderm.setPaDoFlag("60"); // 회수확정 (교환수거완료)
			} else if (productOrderInfo.getExchangeOrder().getCollectDeliveryMethod() != null 
					&& productOrderInfo.getExchangeOrder().getCollectDeliveryMethod().equals(DeliveryMethodType.RETURN_INDIVIDUAL.toString())) {
				paorderm.setPaDoFlag("50"); // 회수지시 (교환요청 > 고객직접발송)
			} else {
				paorderm.setPaDoFlag("20"); // 승인 (교환요청)
			}
			paorderm.setPaOrderGb("45"); // 교환회수

			if (paCommonDAO.insertPaOrderM(paorderm) < 0) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}
			paorderm.setPaOrderGb("40"); // 교환배송
			paorderm.setPaDoFlag("20");
			
			if (paCommonDAO.insertPaOrderM(paorderm) < 0) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}
		} else if((collectDoFlag != null && collectDoFlag.equals("20")) 
				|| (collectDoFlag == null && productOrderInfo.getExchangeOrder().getClaimStatus().equals(ClaimStatusType.EXCHANGE_REJECT.toString()))) { // 교환접수건 or 교환 철회건 or 기취소 교환건
			if (productOrderInfo.getExchangeOrder().getClaimStatus().equals(ClaimStatusType.EXCHANGE_REJECT.toString())) { 
				// 교환 철회건 or 기취소 교환건
				paorderm = new Paorderm();
				
				if (goodsInfo.size() == 1) {
					paorderm.setChangeGoodsdtCode(goodsInfo.get(0).get("GOODSDT_CODE"));
					paorderm.setChangeFlag("01");
				} else {
					paorderm.setChangeGoodsdtCode("");
					paorderm.setChangeFlag("00");
				}
				paorderm.setChangeGoodsCode(goodsInfo.get(0).get("GOODS_CODE"));
				paorderm.setPaCode("41");		
				paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderId());
				paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderId());
				paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
				paorderm.setPaGroupCode("04");
				paorderm.setOutBefClaimGb("0");
				paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
				paorderm.setInsertDate(claim.getInsertDate());
				paorderm.setModifyDate(claim.getModifyDate());
				paorderm.setModifyId("PANAVER");
				
				// 신규 클레임 요청으로 인한 클레임 철회건 체크
				if (productOrderInfo.getExchangeOrder().getRequestChannel().equals("API")) {
					paorderm.setPreCancelYn("1"); // 기존 클레임 기취소 처리
					paorderm.setPaClaimNo(paNaverV3InfoCommonDAO.selectRecentExchangeClaimId(paramMap.getString("productOrderId"))); // 기존 클레임 ID 세팅
				} else {
					paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimId());
				}
				
				paorderm.setPaOrderGb("46");
				paorderm.setPaDoFlag(null);
				paorderm.setProcDate(null);
				
				if (paCommonDAO.insertPaOrderM(paorderm) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}	
				paorderm.setPaOrderGb("41");
				paorderm.setPaDoFlag(null);
				paorderm.setProcDate(null);
				
				if (paCommonDAO.insertPaOrderM(paorderm) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}
			} else if (productOrderInfo.getExchangeOrder().getClaimStatus().equals(ClaimStatusType.EXCHANGE_REQUEST.toString())) {
				// 교환접수건 철회 후, 신규 교환요청
				// 철회 데이터는 앞단 기존 클레임 요청 접수건 체크시 생성
				paorderm = new Paorderm();
				paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimId());
				paorderm.setOutBefClaimGb("0");
				paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
				
				// 수거방법 체크
				if (productOrderInfo.getExchangeOrder().getCollectDeliveryMethod() != null 
						&& productOrderInfo.getExchangeOrder().getCollectDeliveryMethod().equals(DeliveryMethodType.RETURN_INDIVIDUAL.toString())) {
					paorderm.setPaDoFlag("50"); // 회수지시 (고객직접발송)
				} else {
					paorderm.setPaDoFlag("20"); // 승인
				}
				
				// 교환회수 클레임 ID 업데이트 (TPAORDERM)
				paramMap.put("paOrderGb", "45");
				if (paNaverV3InfoCommonDAO.updatePaordermClaimId(paramMap) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
				}
				
				// 교환배송 클레임 ID 업데이트 (TPAORDERM)
				paramMap.put("paOrderGb", "40");	
				if (paNaverV3InfoCommonDAO.updatePaordermClaimId(paramMap) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
				}			
			}
		}
		return productOrderInfo;
	}

	@Override
	public ProductOrderInfoAll insertReturnInfo(ProductOrderInfoAll productOrderInfo) throws Exception {
		ParamMap paramMap = new ParamMap();
		PaNaverV3ClaimListVO claim = new PaNaverV3ClaimListVO();
		String sysdate;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		PaNaverV3AddressVO address;
		Paorderm paorderm = null;
		
		/** 네이버 반품 데이터 생성 및 업데이트 **/
		// 1. TPANAVERADDRESS INSERT
		// 2. TPANAVERCLAIMLIST INSERT
		// 3. TPANAVERORDERLIST UPDATE
		// 4. TPAORDERM INSERT
		claim.setClaimSeq(paNaverV3InfoCommonDAO.selectClaimSeq());
		claim.setProductOrderId(productOrderInfo.getProductOrder().getProductOrderId());
		claim.setReturnInfo(productOrderInfo.getReturnOrder());
		
		/** 1) 네이버 주문반품 주소지 저장 (TPANAVERADDRESS INSERT) **/
		// 1-1) 수거지 주소 저장 (TPANAVERADDRESS)
		if (productOrderInfo.getReturnOrder().getCollectAddress() != null) {
			address = new PaNaverV3AddressVO();
			
			address.setCollectAddress(productOrderInfo.getReturnOrder().getCollectAddress());
			address.setAddressSeq(paNaverV3InfoCommonDAO.selectAddressSeq());
			
			if (paNaverV3InfoCommonDAO.insertPaNaverAddress(address) == 1) claim.setCollectAddressSeq(address.getAddressSeq());
		}
		
		// 1-2) 반품지 주소 저장 (TPANAVERADDRESS)
		if (productOrderInfo.getReturnOrder().getReturnReceiveAddress() != null) {
			address = new PaNaverV3AddressVO();
			
			address.setReturnReceiveAddress(productOrderInfo.getReturnOrder().getReturnReceiveAddress());
			address.setAddressSeq(paNaverV3InfoCommonDAO.selectAddressSeq());
			
			if (paNaverV3InfoCommonDAO.insertPaNaverAddress(address) == 1) claim.setReturnReceiveAddress(address.getAddressSeq());
		}
		sysdate = systemDAO.getSysdatetime();
		claim.setInsertDate(new Timestamp(sdf.parse(sysdate).getTime()));
		claim.setModifyDate(new Timestamp(sdf.parse(sysdate).getTime()));
		
		/** 2) 네이버 클레임 정보 저장 (TPANAVERCLAIMLIST INSERT) **/
		if (paNaverV3InfoCommonDAO.insertReturnClaim(claim) != 1) {
			throw processException("msg.cannot_save", new String[] { "TPANAVERCLAIMLIST INSERT" });
		}
		
		/** 3) 네이버 주문 정보 업데이트 (TPANAVERORDERLIST UPDATE) **/
		paramMap.put("claimSeq", String.valueOf(claim.getClaimSeq()));
		paramMap.put("claimType", "RETURN");
		paramMap.put("claimStatus", claim.getClaimStatus());
		paramMap.put("orderId", productOrderInfo.getOrder().getOrderId());
		paramMap.put("productOrderId", productOrderInfo.getProductOrder().getProductOrderId());
		paramMap.put("claimId", productOrderInfo.getProductOrder().getClaimId());

		if (paNaverV3InfoCommonDAO.updateOrderListClaimInfo(paramMap) != 1) {
			throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST RETURNINFO UPDATE" });
		}
		
		/** 4) 제휴 반품 정보 저장 (TPAORDERM INSERT) **/
		String collectDoFlag = paNaverV3InfoCommonDAO.selectReturnCollectDoFlag(paramMap); // 반품 진행단계 조회
		
		// 진행중인 반품 데이터 없는 경우 (기취소반품 OR 직권반품완료 OR 반품요청 OR 신규반품요청)
		// API로 인한 반품 수집건인 경우 collectDoFlag 는 NULL인 상태  -> productOrderInfo 담겨있는 CLIAM_ID는 새롭게 반품 요청으로 인한 CLAIM_ID 이기 때문에  collectDoFlag가 조회되지 않는다.
		// 기취소반품도   collectDoFlag 는 NULL인 상태 -> 우리쪽 반품 수집 전 반품철회한 데이터 이기 때문 
		if (collectDoFlag == null) {
			paorderm = new Paorderm();
			paorderm.setPaCode("41");
			paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderId());
			paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderId());
			paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
			paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
			paorderm.setOutBefClaimGb("0");
			paorderm.setPaGroupCode("04");
			paorderm.setInsertDate(claim.getInsertDate());
			paorderm.setModifyDate(claim.getModifyDate());
			paorderm.setModifyId("PANAVER");
			
			if (productOrderInfo.getReturnOrder().getClaimStatus().equals(ClaimStatusType.RETURN_REJECT.toString())) {
				// 반품철회 수집하지 못한 상태에서 새로운 반품 받아온 경우  기존 클레임 ID 세팅
				if (productOrderInfo.getReturnOrder().getRequestChannel().equals("API")) {
					paorderm.setPaClaimNo(paNaverV3InfoCommonDAO.selectRecentReturnClaimId(paramMap.getString("productOrderId"))); // 기존 클레임 ID 세팅
				} else {
					// 일반적인 반품철회, 기취소 반품인 경우 
					paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimId());
				}		
				
				// 수거방법 변경('나중에 직접 발송할게요' > '이미 판매자에게 발송했습니다')후, 신규 클레임 요청으로 인한 철회 데이터 생성시 무결성 오류 방지
				if (productOrderInfo.getReturnOrder().getCollectDeliveryMethod() != null 
						&& productOrderInfo.getReturnOrder().getCollectDeliveryMethod().equals("13")) {
					paorderm.setPaShipNo("직접 발송");
				}	
				paorderm.setPaOrderGb("31");
				paorderm.setPaDoFlag(null);
				paorderm.setProcDate(null);
			} else {
				paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimId());
				paorderm.setPaOrderGb("30");
				
				// 직권 반품완료 (반품요청 데이터 수집전, 어드민에서 반품철회)
				if (productOrderInfo.getReturnOrder().getClaimStatus().equals(ClaimStatusType.RETURN_DONE.toString())) {
					paorderm.setPaDoFlag("60"); // 회수확정
				} 
				
				// 반품요청
				else {
					// 고객직접 발송(이미 판매자에게 발송했습니다) > 회수지시
					if (productOrderInfo.getReturnOrder().getCollectDeliveryMethod() != null 
							&& productOrderInfo.getReturnOrder().getCollectDeliveryMethod().equals(DeliveryMethodType.RETURN_INDIVIDUAL.toString())) {
						paorderm.setPaDoFlag("50"); // 회수지시
						paorderm.setPaShipNo("직접 발송");
					} 
					
					// 판매자/구매자 간 별도 협의 (나중에 직접 발송할게요) > 승인 (회수지시 대상)
					else {
						paorderm.setPaDoFlag("20"); // 승인
					}
				}	
			}		

			// TPAORDERM INSERT
			if (paCommonDAO.insertPaOrderM(paorderm) < 0) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}
			return productOrderInfo;
		} else if (collectDoFlag.equals("20") || collectDoFlag.equals("50")) { // 반품진행중 (반품요청 접수된건 > 승인(20) 또는 회수지시(50) 상태)
			
			if (productOrderInfo.getReturnOrder().getClaimStatus().equals(ClaimStatusType.RETURN_DONE.toString())) {
				// 반품완료 (네이버) > 직권반품완료
				paramMap.put("procFlag", "60"); // 처리완료
				
				// TPANAVERORDERLIST.PROC_FLAG UPDATE [O513]
				if (paNaverV3InfoCommonDAO.updateProcFlag(paramMap) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST UPDATE" });
				}
			} else if (productOrderInfo.getReturnOrder().getClaimStatus().equals(ClaimStatusType.RETURN_REJECT.toString())) {
				// 반품철회 (네이버) > 일반적인 반품철회건 or 수거방법 변경으로 인한 반품철회건
				paorderm = new Paorderm();
				paorderm.setPaCode("41");
				paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderId());
				paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderId());
				paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimId());
				paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
				paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));	
				paorderm.setPaOrderGb("31");
				paorderm.setPaDoFlag(null);
				paorderm.setProcDate(null);
				paorderm.setOutBefClaimGb("0");
				paorderm.setPaGroupCode("04");
				paorderm.setInsertDate(claim.getInsertDate());
				paorderm.setModifyDate(claim.getModifyDate());
				paorderm.setModifyId("PANAVER");
				
				// 수거방법 변경('나중에 직접 발송할게요' > '이미 판매자에게 발송했습니다')후, 반품 철회시 무결성 오류 방지
				if (productOrderInfo.getReturnOrder().getCollectDeliveryMethod() != null 
						&& productOrderInfo.getReturnOrder().getCollectDeliveryMethod().equals(DeliveryMethodType.RETURN_INDIVIDUAL.toString())) {
					paorderm.setPaShipNo("직접 발송");
				}
				
				// 반품 철회 데이터 INSERT (TPAORDERM)
				if (paCommonDAO.insertPaOrderM(paorderm) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}
			}
		} else if (productOrderInfo.getReturnOrder().getClaimStatus().equals(ClaimStatusType.RETURN_REJECT.toString())) {
			// 반품철회 (회수확정 후 반품승인전, 고객이 반품철회) > 저장시 반품실패건으로 수기처리 대상건 (CREATE_YN: 0, PRE_CANCEL_YN: 0) 
			paorderm = new Paorderm();
			paorderm.setPaCode("41");
			paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderId());
			paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderId());
			paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimId());
			paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
			paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));		
			paorderm.setPaOrderGb("31");
			paorderm.setPaDoFlag(null);
			paorderm.setProcDate(null);		
			paorderm.setOutBefClaimGb("0");
			paorderm.setPaGroupCode("04");
			paorderm.setInsertDate(claim.getInsertDate());
			paorderm.setModifyDate(claim.getModifyDate());
			paorderm.setModifyId("PANAVER");
			
			// 반품 철회 데이터 INSERT (TPAORDERM)
			if (paCommonDAO.insertPaOrderM(paorderm) < 0) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}
		}
		return productOrderInfo;
	}

	@Override
	public String getExistingClaimCount(String productOrderId, String claimType, String claimId) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("productOrderId", productOrderId);
		paramMap.put("claimType", claimType);
		paramMap.put("claimId", claimId);
		
		return paNaverV3InfoCommonDAO.selectExistingClaimCount(paramMap);
	}

	@Override
	public HashMap<String, String> selectClaimShipcostInfo(String productOrderId, String claimStatus) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("productOrderId", productOrderId);
		paramMap.put("claimStatus", claimStatus);
		
		return paNaverV3InfoCommonDAO.selectClaimShipcostInfo(paramMap);
	}
	
	public ChangedProductOrderInfoListMsg getUnappliedChangedProductOrderInfoList(String productOrderId, String procId, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PANAVERAPI_V3_03_016";
		String duplicateCheck = "";
		
		List<ChangedProductOrderInfo> unappliedChangedInfoList = new ArrayList<ChangedProductOrderInfo>(); // 변경 상품 주문 내역 미반영건 
		List<ChangedProductOrderInfo> resultList = new ArrayList<ChangedProductOrderInfo>(); // 변경 상품 주문 내역 미반영건 재처리 대상
		int changeCount = 0;
		
		// API 트래킹 로그 데이터 세팅
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", procId);

		log.info("======= 네이버 변경 상품 주문 내역 미반영건 조회 API Start =======");	
		
		try {
			// API 중복실행 체크
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
						
			/** 1) 변경 상품 주문 내역 미반영건 조회 **/
			ParamMap parameterMap = new ParamMap();
			parameterMap.put("productOrderId", productOrderId);
			
			unappliedChangedInfoList = paNaverV3InfoCommonDAO.selectUnappliedChangedInfo(parameterMap); // 변경 반영여부 0인건 조회 (TPANAVERORDERCHANGE.CHANGED_APPLIED_YN)

			for (ChangedProductOrderInfo order : unappliedChangedInfoList) {	
				/** 2) 변경 상품 주문 내역 재조회 파라미터 세팅  **/
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				
				Calendar to = Calendar.getInstance();
				to.setTime(order.getLastChangedDate());
				to.add(Calendar.SECOND, 1);

				String lastChangedType = order.getLastChangedType(); // 최종 변경 구분
				String fromDate = dateFormat.format(order.getLastChangedDate()); // 조회 시작 일시
				String toDate = dateFormat.format(to.getTime()); // 조회 종료 일시
				int limitCount = 300; // 조회 응답 개수 제한
				
				/** 3) 네이버 변경 상품 주문 내역 조회 **/
				ChangedProductOrderInfoListMsg result = paNaverV3InfoCommonService.getChangedProductOrderInfoList(lastChangedType, fromDate, toDate, order.getProductOrderId(), limitCount, procId, request);
				
				List<ChangedProductOrderInfo> responseList = result.getChangedProductOrderInfoList();
				int reApplyCount = 0;
				
				if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode()) && !responseList.isEmpty()) {
					for (int i=0; i<responseList.size(); i++) {
						// 최종 변경 상태 유지건 > 재처리 대상
						if (responseList.get(i).getProductOrderId().equals(order.getProductOrderId())) {
							resultList.add(order);
							reApplyCount++;
						}
					}
				}
				
				if (reApplyCount == 0) {
					// 최종 변경 상태 변경건 > 미반영건 제외 처리
					if (paNaverV3InfoCommonService.updateUnappliedChangedInfo(order) > 0) {
						changeCount++;
					}
				}
			}
			paramMap.put("code", "200");
			
			if (unappliedChangedInfoList == null || unappliedChangedInfoList.isEmpty()) {
				log.debug("retreived changed order count : 0");
				paramMap.put("message", "주문 변경 내역 미반영 (0)건 조회");
			} else {
				log.debug("re-appling order count : {}", resultList.size());
				paramMap.put("message", "주문 변경 내역 미반영 (" + String.valueOf(unappliedChangedInfoList.size()) + ")건 조회, ("+ String.valueOf(resultList.size()) + ")건 재처리 "+", ("+ changeCount + ")건 변경");
			}
		} catch (TransApiException ex) {
			paramMap.put("code", "500");
			paramMap.put("message", ex.getMessage());
			log.error(ex.getMessage(), ex);
		} catch (Exception e) {
			paNaverV3ConnectUtil.dealException(e, paramMap);
			log.error(e.getMessage(), e);
		} finally {
			try {
				paNaverV3ConnectUtil.dealSuccess(paramMap, request);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			
			if (duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
			
			log.info("======= 네이버 변경 상품 주문 내역 미반영건 조회 API End =======");
		}
		return new ChangedProductOrderInfoListMsg (paramMap.getString("code"), paramMap.getString("message"), resultList, unappliedChangedInfoList);
	}

	@Override
	public int updateUnappliedChangedInfo(ChangedProductOrderInfo order) throws Exception {
		return paNaverV3InfoCommonDAO.updateUnappliedChangedInfo(order);
	}

	@Override
	public int selectExistingClaimReject(String orderId, String productOrderId, String claimType, String claimId) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("orderId", orderId);
		paramMap.put("productOrderId", productOrderId);
		paramMap.put("claimType", claimType);
		paramMap.put("claimId", claimId);
		
		return paNaverV3InfoCommonDAO.selectExistingClaimReject(paramMap);
	}

	@Override
	public int selectExistingCancelReject(String orderId, String productOrderId, String claimId) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("orderId", orderId);
		paramMap.put("productOrderId", productOrderId);
		paramMap.put("claimId", claimId);
		
		return paNaverV3InfoCommonDAO.selectExistingCancelReject(paramMap);
	}
}
