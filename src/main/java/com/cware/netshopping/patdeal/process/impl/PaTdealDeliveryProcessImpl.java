package com.cware.netshopping.patdeal.process.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.patdeal.message.OrderConfirmResoponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pacommon.order.repository.PaOrderDAO;
import com.cware.netshopping.patdeal.domain.DeliveryGroups;
import com.cware.netshopping.patdeal.domain.OrderDetail;
import com.cware.netshopping.patdeal.domain.OrderList;
import com.cware.netshopping.patdeal.domain.OrderListResponse;
import com.cware.netshopping.patdeal.domain.OrderOptions;
import com.cware.netshopping.patdeal.domain.OrderProductOptions;
import com.cware.netshopping.patdeal.domain.OrderProducts;
import com.cware.netshopping.patdeal.process.PaTdealDeliveryProcess;
import com.cware.netshopping.patdeal.repository.PaTdealDeliveryDAO;
import com.cware.netshopping.patdeal.service.PaTdealClaimService;
import com.cware.netshopping.patdeal.service.PaTdealDeliveryService;
import com.cware.netshopping.patdeal.util.PaTdealApiRequest;
import com.cware.netshopping.patdeal.util.PaTdealConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("patdeal.delivery.paTdealDeliveryProcess")
public class PaTdealDeliveryProcessImpl extends AbstractService implements PaTdealDeliveryProcess{
	
	@Autowired
	private PaTdealConnectUtil paTdealConnectUtil;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	@Qualifier("patdeal.delivery.paTdealDeliveryService")
	private PaTdealDeliveryService paTdealDeliveryService;
	
	@Autowired
	private PaTdealDeliveryDAO paTdealDeliveryDAO;
	
	@Autowired
	private PaCommonDAO paCommonDAO;
	
	@Autowired
	private PaTdealApiRequest paTdealApiRequest;
	
	@Resource(name = "pacommon.order.paorderDAO")
    private PaOrderDAO paorderDAO;
	
	
	@Resource(name="common.system.systemProcess")
	private SystemProcess systemProcess;
	
	@Autowired 
	@Qualifier("patdeal.claim.paTdealClaimService")
	private PaTdealClaimService paTdealClaimService;
	
	/**
	 * 주문목록조회
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getOrderList(String fromDate, String toDate, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PATDEALAPI_03_001";
		String duplicateCheck = "";
		String paCode  = "";
		
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		log.info("======= 티딜 주문 목록 조회 API Start - {} =======");
		try {
			
			paramMap = paTdealApiRequest.getApiInfo(apiCode);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			for(int i = 0; i< Constants.PA_TDEAL_CONTRACT_CNT ; i ++) {
				
				paCode = (i==0 )? Constants.PA_TDEAL_BROAD_CODE : Constants.PA_TDEAL_ONLINE_CODE;
				/** 1) 전문 데이터 세팅  **/
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String dateTime = systemService.getSysdatetimeToString();
				
				Calendar from = Calendar.getInstance();
				Calendar to = Calendar.getInstance();
				
				if (fromDate != null && !fromDate.equals("")) {
					from.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(fromDate));
				} else {
					from.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateTime));
					from.add(Calendar.DATE, -8); // 선물하기 주문건 최대 7일 이내에 주소 입력 가능하여 선물하기 주문건 위해 날짜 범주 늘림
				}
				
				if (toDate != null && !toDate.equals("")) {
					to.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(toDate));
				} else {
					to.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateTime));
					
				}
				
				// Query Parameters 세팅
				Map<String, String> queryParameters = new HashMap<String, String>();
				
				queryParameters.put("startYmd", dateFormat.format(from.getTime()));
				queryParameters.put("endYmd", dateFormat.format(to.getTime()));
				queryParameters.put("orderRequestTypes", "PAY_DONE"); // 
				//필요한 파라미터 및 통신 세팅
				// Body 세팅S
				ParamMap apiDataObject = new ParamMap();
				// Body 세팅 E
				
				OrderListResponse orderListResponse = new OrderListResponse();
				//통신
				responseMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters,  queryParameters,  apiDataObject);
				
				if(((List<Map<String, Object>>)(responseMap.get("contents"))).size() < 1) {
					continue;
				}
				
				// vo변환
				ObjectMapper objectMapper = new ObjectMapper();
				orderListResponse = objectMapper.convertValue(responseMap, OrderListResponse.class);
				
				if(orderListResponse.getContents() != null) {
					log.debug("getOrderList succeed");
					paramMap.put("code", "200");
					if(orderListResponse.getContents().size()> 0) {
						
						//tpaorderlist, tpaorderm 저장
						for(OrderList order : orderListResponse.getContents()) {
							try {
								paTdealDeliveryService.savePaTdealOrderTx(order, paCode);
								
							} catch (Exception e) {
								log.error(e.getMessage()); 
								continue;
							}
						}
						
					}else {
						log.debug("order count : 0");
						paramMap.put("message", "주문 목록 (0) 건 조회");
					}
					
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(paramMap ,  e);
		} finally {
			
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "주문 목록 대상 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			
			paTdealConnectUtil.closeApi(request, paramMap);
		}
		
		
		log.info("======= 티딜 주문 목록 조회 API End - {} =======");
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
		
	}
	
	public String savePaTdealOrder(OrderList order, String paCode) throws Exception {
		String rtnMsg		 = Constants.SAVE_SUCCESS;
		
		//tpaordem 과 tpatdealorderlist 저장
		
		int executedRtn = 0;
		
		ParamMap param;
		
		for(DeliveryGroups deliveryGroups : order.getDeliveryGroups()) {

			for(OrderProducts orderProduct : deliveryGroups.getOrderProducts()) {
				
				long lastProductCouponDiscountAmt = orderProduct.getLastProductCouponDiscountAmt();
				
				for(OrderProductOptions orderProductOptions : orderProduct.getOrderProductOptions()) { // 이 안에서 모든값 세팅
					
					if(orderProductOptions.getOrderStatusType()!=null  &&  !orderProductOptions.getOrderStatusType().isEmpty()
							&& "PAY_DONE".equals(orderProductOptions.getOrderStatusType()) && "N".equals(orderProductOptions.getExchangeYn()) ){ 
						// 1. 우리 상품 아닌 경우에는 deliveryGroups 에 정보 담아주지 않는다.
						// 2. 최상위 "order" 에는 같이 구매한 정보를 담아주기는 하지만 주문내용을 저장하기 위한 부분에는 우리의 상품 주문 정보만 담아준다.
						  
						
						//1. 선물하기의 경우 나중에주소입력 여부 true 값으로 들어옴
						//2. 주소 미입력 상태에서는 주소 "-" 형태로 들어온다.
						// 선물하기 고객이 주소 입력해도 바로 들어오지 않고 일정 시간 이후에 주소가 입력되는거 같다...(2시간..?), 주소가 들어오면 주문 생성
						// 주소가 들어와도 나중에주소입력은 true로 들어오기 떄문에 아래 로직으로 주소 있는지 체크
						if( deliveryGroups.isUsesShippingInfoLaterInput() ) { 
							if( ( StringUtils.isNotEmpty(deliveryGroups.getReceiverAddress()) && "-".equals(deliveryGroups.getReceiverAddress()) )
									&& ( StringUtils.isNotEmpty(deliveryGroups.getReceiverJibunAddress()) && "-".equals(deliveryGroups.getReceiverJibunAddress()) ) ) {
								//배송지 나중 입력이면서 도로명, 지번주소 둘다 없는 경우 선물하기 주소 미입력 상태로 판단, 주문 미생성
								continue;
							}
						} 
						
						String dateTime 	 = systemService.getSysdatetimeToString();
						Timestamp systemTime = DateUtil.toTimestamp(dateTime);
						
						param = new ParamMap();
						
						param.put("paCoce", paCode);
						param.put("paOrderGb","10");
						param.put("modifyId", Constants.PA_TDEAL_PROC_ID);
						param.put("modifyDate",systemTime);
						param.put("insertDate",systemTime);
						
						//OrderList
						param.put("memberId", order.getMemberId());
						param.put("memberNo", order.getMemberNo());
						
						param.put("orderNo",order.getOrderNo()); //pk
						param.put("orderMemo", order.getOrderMemo());
						param.put("ordererName", order.getOrdererName());
						
						param.put("orderYmdt", order.getOrderYmdt());
						param.put("ordererEmail", order.getOrdererEmail());
						param.put("ordererContact1", order.getOrdererContact1());
						param.put("ordererContact2", order.getOrdererContact2());
						
						param.put("currencyCode", order.getCurrencyCode());
						param.put("payTypeLabel", order.getPayTypeLabel());
						param.put("platformType", order.getPlatformType());
						param.put("payType", order.getPayType());
						param.put("pgType", order.getPgType());
						//OrderList [End]
						
						//OrderList > deliveryGroups 
						param.put("receiverZipCd", deliveryGroups.getReceiverZipCd());
						param.put("deliveryConditionNo", deliveryGroups.getDeliveryConditionNo());
						param.put("receiverContact1", deliveryGroups.getReceiverContact1());
						param.put("receiverContact2", deliveryGroups.getReceiverContact2());
						param.put("receiverJibunAddress", deliveryGroups.getReceiverJibunAddress());
						param.put("receiverName", deliveryGroups.getReceiverName());
						param.put("originalDeliveryNo", deliveryGroups.getOriginalDeliveryNo());
						param.put("combineDeliveryYn", deliveryGroups.getCombineDeliveryYn());
						param.put("deliveryNo", deliveryGroups.getDeliveryNo());
						param.put("remoteDeliveryAmt", deliveryGroups.getRemoteDeliveryAmt());
						param.put("deliveryTemplateGroupNo", deliveryGroups.getDeliveryTemplateGroupNo());
						param.put("receiverDetailAddress", deliveryGroups.getReceiverDetailAddress());
						param.put("deliveryMemo", deliveryGroups.getDeliveryMemo());
						param.put("receiverAddress", deliveryGroups.getReceiverAddress());
						param.put("deliveryAmt", deliveryGroups.getDeliveryAmt());
						//OrderList > deliveryGroups  [End]
						
						
						//deliveryGroups> OrderProducts 
						param.put("productManagementCd", orderProduct.getProductManagementCd());
						param.put("orderProductNo", orderProduct.getOrderProductNo());
						param.put("mallProductNo", orderProduct.getMallProductNo());
						
						// 맨 처음 상품에만 넣기
						// 같은 상품 옵션 여러개 구매시 각 옵션의 티딜 할인 금액을 알 수 없다. 상품 1개만 적용되는 쿠폰 존재, 한개에 할인 금액을 몰아서 넣기로 함 
						// async 에서는lastProductCouponDiscountAmt 적용 x , 티딜에서 거는 쿠폰 적용 안시시키로 함.
						param.put("lastProductCouponDiscountAmt", lastProductCouponDiscountAmt);
						
						lastProductCouponDiscountAmt = 0;
						
						
						param.put("productName", orderProduct.getProductName());
						//deliveryGroups> OrderProducts [End]
						
						
						//deliveryGroups> OrderProducts> orderProductOptions
						param.put("mallOptionNo", orderProductOptions.getMallOptionNo());
						param.put("purchasePrice", orderProductOptions.getPurchasePrice());
						param.put("immediateDiscountAmt", orderProductOptions.getImmediateDiscountAmt());
						param.put("orderCnt", orderProductOptions.getOrderCnt());
						param.put("originalOrderCnt", orderProductOptions.getOriginalOrderCnt());
						param.put("adjustedAmt", orderProductOptions.getAdjustedAmt());
						param.put("deliveryTemplateNo", orderProductOptions.getDeliveryTemplateNo());
						param.put("releaseWarehouseNo", orderProductOptions.getReleaseWarehouseNo());
						param.put("salePrice", orderProductOptions.getSalePrice());
						param.put("optionValue", orderProductOptions.getOptionValue());
						param.put("orderProductOptionNo", orderProductOptions.getOrderProductOptionNo());
						param.put("additionalDiscountAmt", orderProductOptions.getAdditionalDiscountAmt());
						param.put("optionManagementCd", orderProductOptions.getOptionManagementCd().replaceAll("_", ""));//pk
						param.put("optionName", orderProductOptions.getOptionName());
						//deliveryGroups> OrderProducts> orderProductOptions [End]
						
						// 발주 처리 된건 체크 
						int exists = paTdealDeliveryDAO.selectCountOrderList(param);
						if(exists > 0) continue;
						
						//INSERT TPATDEALODERLIST
						executedRtn = paTdealDeliveryDAO.insertPaTdealOrderList(param);
						
						if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPATDEALORDERLIST INSERT" });
						
						//INSERT PAORDERM
						Paorderm paorderm = new Paorderm();
						
						paorderm.setPaOrderGb		(param.get("paOrderGb").toString());
						paorderm.setPaCode			(param.get("paCoce").toString());
						paorderm.setPaOrderNo		(param.get("orderNo").toString());
						paorderm.setPaOrderSeq		(ComUtil.objToStr(param.get("optionManagementCd"), null));
						paorderm.setPaShipNo		(""); //
						paorderm.setPaProcQty		(ComUtil.objToStr(param.get("orderCnt"), null));
						paorderm.setPaDoFlag		("10");
						paorderm.setOutBefClaimGb	("0");
						paorderm.setInsertDate		(systemTime);
						paorderm.setModifyDate		(systemTime);
						paorderm.setModifyId		(Constants.PA_TDEAL_PROC_ID);
						paorderm.setPaGroupCode		(Constants.PA_TDEAL_GROUP_CODE);
						
						executedRtn = paCommonDAO.insertPaOrderM(paorderm); 
						
						if (executedRtn < 0)  throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
					}
				}
				
			}
		}
	
		return rtnMsg;
	}
	
	/**
	 * 주문승인
	 */
	@Override
	public OrderConfirmResoponseMsg orderConfirmProc(String orderProductOptionNo, HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PATDEALAPI_03_003";
		String duplicateCheck = "";
		String paCode  = "";
		
		int executedRtn  = 0;
		int orderAbleQty = 0;
		
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	 	= 	new HashMap<String, Object>() ;
		List<Map<String,Object>> soldOutList 	= 	new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> cancelList 	= 	new ArrayList<Map<String,Object>>();
		
		log.info("======= 티딜 주문 승인 API Start - {} =======");
		try {
			
			paramMap = paTdealApiRequest.getApiInfo(apiCode);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			for(int i = 0; i< Constants.PA_TDEAL_CONTRACT_CNT ; i ++) {
				
				paCode = (i==0 )? Constants.PA_TDEAL_BROAD_CODE : Constants.PA_TDEAL_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				paramMap.put("orderProductOptionNo", orderProductOptionNo);
				
				// Query Parameters 세팅
				Map<String, String> queryParameters = new HashMap<String, String>();
				
				// Body 세팅S
				// 대상 뽑아오기 
				List<Map<String,Object>> confrimTargetList= paTdealDeliveryDAO.selectOrderConfirmList(paramMap); 
				ArrayList<String> apiDataObject = null;
				
				for(Map<String,Object> confrimTarget : confrimTargetList) { // 단건으로 처리하도록, 한번에 전부 처리하면 무슨건이 실패 했는지 알기 힘듬
					try {
						Map<String,Object> map   = new HashMap<String,Object>();
						map.put("goodsCode", confrimTarget.get("PRODUCT_MANAGEMENT_CD"));
						map.put("goodsdtCode", confrimTarget.get("OPTION_MANAGEMENT_CD").toString().substring(confrimTarget.get("PRODUCT_MANAGEMENT_CD").toString().length()));
						
						orderAbleQty = paTdealDeliveryDAO.selectOrderAbleQty(map);
						
						
						if( !"40".equals(confrimTarget.get("PA_ORDER_GB")) && orderAbleQty < Integer.parseInt(confrimTarget.get("ORDER_CNT").toString()) ) {
							soldOutList.add(confrimTarget);
							
						}else if( !"40".equals(confrimTarget.get("PA_ORDER_GB")) && noDelyChk(confrimTarget, request) ) { // 제주도서산간 배송불가 주문건 
							confrimTarget.put("REASON_DETAIL", "해당 상품은 도서/산간 혹은 제주 배송 불가 상품으로, 해당 지역 배송 불가하여 주문 취소되었습니다.");
							cancelList.add(confrimTarget);
							
						}else {
							apiDataObject = new ArrayList<>();
							
							apiDataObject.add(confrimTarget.get("ORDER_PRODUCT_OPTION_NO").toString());
							// Body 세팅 E
							
							//통신
							responseMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters,  queryParameters,  apiDataObject);
							
							if( !"204".equals(responseMap.get("status")) ){
								updatePaOrderm(confrimTarget, null, "승인실패", Constants.SAVE_FAIL); 
								paramMap.put("code", responseMap.get("status"));
								paramMap.put("message", "승인실패");
								continue;//실패건은 스킵
								
							}
							//4) UPDATE TPAORDERM.DO_FLAG
							executedRtn = updatePaOrderm(confrimTarget, "30", "승인완료", Constants.SAVE_SUCCESS);
							if(executedRtn>0) {
								paramMap.put("code", "200");
								paramMap.put("message", "승인성공 및 저장 완료");
							}else {
								paramMap.put("code", "400");
								paramMap.put("message", "저장 실패");
							}
						
						}
						
					} catch (TransApiException e) {
						updatePaOrderm(confrimTarget, null, "승인실패",Constants.SAVE_FAIL); 
						paramMap.put("code", e.getCode());
						paramMap.put("message", "승인실패 : " + e.getMessage());
						
					} catch (Exception e) {
						log.error(e.getMessage());
						updatePaOrderm(confrimTarget, null, "승인실패", Constants.SAVE_FAIL); 
						paramMap.put("code", responseMap.get("status"));
						paramMap.put("message", "승인실패 : " + e.getMessage());
						
					}
				}

			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			
			paTdealConnectUtil.checkException(paramMap ,  e);
		} finally {
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "주문 승인 대상 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			paTdealConnectUtil.closeApi(request, paramMap);
			log.info("======= 티딜 주문 승인 API End - {} =======");
		}
		
		return new OrderConfirmResoponseMsg(paramMap.getString("code"), paramMap.getString("message"), soldOutList, cancelList);
	}
	
	/**
	 * 티딜 발송처리
	 */
	@Override
	public ResponseMsg slipOutProc(String orderProductOptionNo, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PATDEALAPI_03_004";
		String duplicateCheck = "";
		String paCode  = "";
		
		int executedRtn = 0;
		
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		
		log.info("======= 티딜 발송처리 API Start - {} =======");
		
		try {
			paramMap = paTdealApiRequest.getApiInfo(apiCode);
			
			paramMap.put("orderProductOptionNo", orderProductOptionNo);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			for(int i = 0; i< Constants.PA_TDEAL_CONTRACT_CNT ; i ++) {
				
				paCode = (i==0 )? Constants.PA_TDEAL_BROAD_CODE : Constants.PA_TDEAL_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				// Query Parameters 세팅
				Map<String, String> queryParameters = new HashMap<String, String>();
				
				// Body 세팅S
				// 대상 뽑아오기 
				List<Map<String,Object>> slipProcTargetList= paTdealDeliveryDAO.selectSlipProcList(paramMap);
				ArrayList<Map<String,Object>> apiDataObject = null;
				Map<String,Object> map = null;
				for(Map<String,Object> slipProcTarget :slipProcTargetList) { 
					apiDataObject = new ArrayList<Map<String,Object>>();
					try {
						// 주문상세조회 
						OrderDetail detailResponseMsg = paTdealClaimService.orderInfoDetail(slipProcTarget.get("PA_CODE").toString(), slipProcTarget.get("ORDER_NO").toString(), request);
						List<OrderProducts> responseData  = detailResponseMsg.getOrderProducts();
						for(OrderProducts  op : responseData) {
							for (OrderOptions oo : op.getOrderOptions()) {
								/*
								 A상품에 옵션 a,b 구매 후 고객이 취소 후 철회 하는 경우 ORDER_PRODUCT_OPTION_NO 변경 되어 
								 상세 조회에서 바뀐 ORDER_PRODUCT_OPTION_NO 조회필요
								 이때 주문번호가 동일하기 때문에 옵션코드와 발송상태를 비교하지 않으면 a발송 후 b 발송할때 이미
								 발송처리가 되었다는 에러 반환하여 해당 로직 추가
								 
								 ++ 원주문건이라도 상세 조회 후 상태값 체크후 보내기
								 */
								
								// 상품의 단품 코드가 같고, 상품준비중, ClaimStatusType null 
								if( slipProcTarget.get("OPTION_MANAGEMENT_CD").equals(oo.getOptionManagementCd().replaceAll("_", ""))
										&& "PRODUCT_PREPARE".equals(oo.getOrderStatusType()) 
										&& oo.getClaimStatusType() == null){ 
									
									map = new HashMap<String,Object>();
									
									map.put("orderProductOptionNo", oo.getOrderOptionNo());
									map.put("deliveryCompanyType", slipProcTarget.get("PA_DELY_GB"));
									map.put("invoiceNo", slipProcTarget.get("SLIP_NO"));
									apiDataObject.add(map);
									
								}
							}
						}
						// Body 세팅 E
						
						if(apiDataObject.size()>0) {
							
							//발송처리 API 통신
							responseMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters,  queryParameters,  apiDataObject);
							if( !"204".equals(responseMap.get("status")) ){
								updatePaOrderm(slipProcTarget, null, "발송실패",Constants.SAVE_FAIL); 
								paramMap.put("code", responseMap.get("status"));
								paramMap.put("message", "발송실패");
								continue;//실패건은 스킵
							}
							
							//UPDATE TPAORDERM.DO_FLAG
							executedRtn = updatePaOrderm(slipProcTarget, "40", "발송완료", Constants.SAVE_SUCCESS);
							if(executedRtn>0) {
								paramMap.put("code", "200");
								paramMap.put("message", "발송처리 성공 및 저장 완료");
							}else {
								paramMap.put("code", "400");
								paramMap.put("message", "저장 실패");
							}
						}
						
						
						
						
					} catch (TransApiException e) {
						updatePaOrderm(slipProcTarget, null, "발송실패",Constants.SAVE_FAIL); 
						paramMap.put("code", e.getCode());
						paramMap.put("message", "발송실패 : " + e.getMessage());
						
					} catch (Exception e) {
						updatePaOrderm(slipProcTarget, null, "발송실패",Constants.SAVE_FAIL); 
						paramMap.put("code", "500");
						paramMap.put("message", "발송실패 : " + e.getMessage());
						
					}
				}
				
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			
			paTdealConnectUtil.checkException(paramMap ,  e);
		}finally {
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "발송 처리 대상 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			paTdealConnectUtil.closeApi(request, paramMap);
			log.info("======= 티딜 발송처리 API End - {} =======");
		}
		
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
		
	}
	
	
	/**
	 * 티딜 주문상품 수취확인처리 요청하기(배송완료)
	 */
	@Override
	public ResponseMsg deliveryCompleteProc(String orderProductOptionNo, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PATDEALAPI_03_005";
		String duplicateCheck = "";
		String paCode  = "";
		
		int executedRtn = 0;
		
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		
		log.info("======= 티딜 주문상품 수취확인처리 요청하기 API Start - {} =======");
		
		try {
			paramMap = paTdealApiRequest.getApiInfo(apiCode);
			
			paramMap.put("orderProductOptionNo", orderProductOptionNo);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			for(int i = 0; i< Constants.PA_TDEAL_CONTRACT_CNT ; i ++) {
				
				paCode = (i==0 )? Constants.PA_TDEAL_BROAD_CODE : Constants.PA_TDEAL_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				// Query Parameters 세팅
				Map<String, String> queryParameters = new HashMap<String, String>();
				
				// Body 세팅S
				// 대상 뽑아오기 
				List<Map<String,Object>> deliveryCompleteTargetList= paTdealDeliveryDAO.selectDeliveryComplete(paramMap);
				ArrayList<String> apiDataObject = null;
				for(Map<String,Object> deliveryCompleteTarget : deliveryCompleteTargetList) { //한건씩 처리
					try {
						
						apiDataObject = new ArrayList<String>();
						apiDataObject.add(deliveryCompleteTarget.get("ORDER_PRODUCT_OPTION_NO").toString());
						
						// Body 세팅 E
						
						//통신
						responseMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters,  queryParameters,  apiDataObject);
						
						if( !"204".equals(responseMap.get("status")) && !responseMap.get("message").toString().contains("주문상태가 잘못되었습니다") ){
							updatePaOrderm(deliveryCompleteTarget, null, "수취확인처리실패",Constants.SAVE_FAIL); 
							paramMap.put("code", responseMap.get("status"));
							paramMap.put("message", "수취확인처리실패");
							continue;//실패건은 스킵
						}
						String resultMessage = "수취확인처리완료";
						
						if(responseMap.containsKey("message")) {
							if(responseMap.get("message").toString().contains("주문상태가 잘못되었습니다")) {
								resultMessage = "수취확인처리완료(티딜주문상태변경)";
							}
						}
						
						//UPDATE TPAORDERM.DO_FLAG
						executedRtn = updatePaOrderm(deliveryCompleteTarget, "80", resultMessage, Constants.SAVE_SUCCESS);
						if(executedRtn>0) {
							paramMap.put("code", "200");
							paramMap.put("message", "수취확인처리처리 성공 및 저장 완료");
						}else {
							paramMap.put("code", "400");
							paramMap.put("message", "저장 실패");
						}
						
					} catch (TransApiException e) {
						updatePaOrderm(deliveryCompleteTarget, null, "수취확인처리실패",Constants.SAVE_FAIL); 
						paramMap.put("code", e.getCode());
						paramMap.put("message", "수취확인처리실패 : " + e.getMessage());
						
					} catch (Exception e) {
						updatePaOrderm(deliveryCompleteTarget, null, "수취확인처리실패",Constants.SAVE_FAIL); 
						paramMap.put("code", "500");
						paramMap.put("message", "수취확인처리실패 : " + e.getMessage());
						
					}
				}
			
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			
			paTdealConnectUtil.checkException(paramMap ,  e);
		}finally {
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "수취확인처리 처리 대상 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			paTdealConnectUtil.closeApi(request, paramMap);
			log.info("======= 티딜 주문상품 수취확인처리 요청하기 API End - {} =======");
		}
		
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
		
	}
	
	
	private int updatePaOrderm(Map<String, Object> confrimTarget, String paDoFlag, String message, String resultCode) {
		int executedRtn = 0;
		
		try {
			Map<String, Object> m = new HashMap<String,Object>();
			
			m.put("paDoFlag"		, paDoFlag);
			m.put("resultMessage"	, message);
			m.put("resultCode"		, resultCode);
			m.put("mappingSeq"		, confrimTarget.get("MAPPING_SEQ"));
			
			if(confrimTarget.containsKey("preCancelYn") && !"".equals(confrimTarget.get("preCancelYn"))) { 
				m.put("preCancelYn"		, confrimTarget.get("preCancelYn"));
			}
			
			executedRtn = paTdealDeliveryDAO.updatePaorderm(m);
		} catch (Exception e) {
			log.info("{} : {}", "티딜 제휴 주문 테이블 업데이트 오류",  e.getMessage() );
		}
		return executedRtn;
	}
	
	@Override
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception {
		return paTdealDeliveryDAO.selectOrderInputTargetList(limitCount);
	}
	
	
	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception{
		
		return paTdealDeliveryDAO.selectOrderInputTargetDtList(paramMap);
	}
	

	//주소정제
	private boolean noDelyChk(Map<String,Object> confrimTarget, HttpServletRequest request) throws Exception{
		boolean result = false;
		
		try {
			
			ParamMap param = new ParamMap();
			
			param.put("goodsCode", confrimTarget.get("PRODUCT_MANAGEMENT_CD"));
			List<HashMap<String, String>> delyNoList= paorderDAO.selectDelyNoAreaGb(param);//goodsCode
			
			if(delyNoList.size()>0) {
				String isLocalYn = "N";
				//로컬 세팅
				if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
					isLocalYn = "Y";
				}
				
				String cust_postNo  = confrimTarget.get("POST_NO").toString();
				String cust_postSeq = "001";
				String cust_AddrGbn = confrimTarget.get("ADDR_GBN").toString();
				String cust_Addr    = confrimTarget.get("STD_ADDR").toString();//RECEIVER_ADDR
				String cust_AddrDt  = "";
				String full_Addr	= confrimTarget.get("RECEIVER_ADDR").toString();
				
				if (cust_postNo  == null || "".equals(cust_postNo)  ) cust_postNo  = "999999";
				if (cust_postSeq == null || "".equals(cust_postSeq) ) cust_postSeq = "001";
				if (cust_AddrGbn == null || "".equals(cust_AddrGbn) ) cust_AddrGbn = "0";
				
				ParamMap resultParamMap = new ParamMap();
				resultParamMap.put("post_no",cust_postNo);
				
				if("02".equals(cust_AddrGbn)){//01이면 지번 , 02면 도로명
					resultParamMap.put("road_addr_yn", 1);
				}
				else{
					resultParamMap.put("road_addr_yn", 0);
				}	
				
				resultParamMap.put("search_addr"	,	cust_Addr);	
				resultParamMap.put("search_addr2"	,	cust_AddrDt);
				resultParamMap.put("full_addr"		, 	full_Addr);
				resultParamMap.put("localYn"		,	isLocalYn);
				
				param = systemProcess.selectStdAddress(resultParamMap); //주소정제 호출
				
				
				//주소정제된 데이터 가지고 세팅 
				List<ParamMap> paramAdressesList	= new ArrayList<ParamMap>();
				paramAdressesList.add(param);		
				
				ParamMap paramAdress =  paramAdressesList.stream().filter(n -> 
				confrimTarget.get("RECEIVER_ADDR").toString().equals(n.getString("FULL_ADDR"))).findAny().get();
				
				if(paramAdress.getString("CL_SUCCESS_YN").equals("1")){
					if(!paramAdress.getString("FLG").equals("1")){
						cust_postNo = "999999";
					}
				}else {
					cust_postNo = "999999";
				}
				
				if("999999".equals(cust_postNo)) {
					param.put("postNo"		, confrimTarget.get("POST_NO").toString());  //주소정제 실패시 기존 티딜에서 넘겨준 우편번호 세팅
					param.put("postSeq"		, "%");	
					
				}else {
					param.put("postNo"		, paramAdress.getString("STD_POST_NO")); 
					param.put("postSeq"		, paramAdress.getString("STD_POST_SEQ"));		
				}
				// 배송불가 지역이 있으면서, 주소정제가 되지 않은건 처리위한 로직
				if("999999".equals(cust_postNo)) {
					// 우편번호가 같아도 area_gb이 다른값 존재 post_no '59777' 돌산읍 금성리은 '10', 돌산읍 군내리 송도 '20' 
					// 주소정제 실패 건  postSeq 알 수 없어서 정확환 area_gb 알 수 없다. 
					// post_no로 조회 시 area_gb(중복제외 distinct) 한개 이상 조회되는 경우 주문 취소
					
					//area_gb체크
					int chkCnt = paTdealDeliveryDAO.selectTdealAreaGbChk(param.get("postNo").toString());
					// 10, 20, 30 중 한개씩만 있는 경우 - chkCnt 0, 무조건 취소 x, 아래서 체크 
					// ex) (10,20) or (10,30) or (20,30) or (10,20,30) - 도서산간/제주 주문취소
					if(chkCnt>0) {
						return true; 
					}
					
				}
				
				String postAreaGb 	= paorderDAO.selectPostAreaGb(param);//postNo,postSeq
				
				//postAreaGb 이 20인데 delyNoList는 30이면 배송가능
				for(HashMap<String, String> na : delyNoList) {
					
					if(na.get("AREA_GB").equals(postAreaGb)) {
						result = true;//도서산간 제주 배송 불가 상품 주문건
					}					
				}
				
			}
			
			
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	
		return result;

	}
	
	/**
	 * 판매거절 주문 정보 조회
	 * @param mappingSeq
	 * @return
	 */
	@Override
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) throws Exception {
		return paTdealDeliveryDAO.selectRefusalInfo(mappingSeq);
	}
	
	
	
	/**
	 * 티딜 취소철회 대상건 발송처리 
	 */
	@Override
	public ResponseMsg cancelRejcectSlipOutProc(String paCode, String orderProductOptionNo, String deliveryCompanyType, String invoiceNo, HttpServletRequest request)throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PATDEALAPI_03_004";
	    String duplicateCheck = "";
		
		
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		
		log.info("======= 티딜 취소철회 대상건 발송처리  API Start - {} =======");
		
		try {
			paramMap = paTdealApiRequest.getApiInfo(apiCode);
			
			paramMap.put("orderProductOptionNo", orderProductOptionNo);
			paramMap.put("duplicateCheck", duplicateCheck); // 중복체크 스킵
			
			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();
			
			// Body 세팅S
			ArrayList<Map<String,Object>> apiDataObject = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("orderProductOptionNo", orderProductOptionNo);
			map.put("deliveryCompanyType", deliveryCompanyType);
			map.put("invoiceNo", invoiceNo);
			apiDataObject.add(map);
			
			// Body 세팅 E
			
			//발송처리 API 통신
			responseMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters,  queryParameters,  apiDataObject);
		
			if( !"204".equals(responseMap.get("status")) ){
				paramMap.put("code", responseMap.get("status"));
				paramMap.put("message", "취소철회 대상건 발송처리 실패 : " + responseMap.get("message"));
			}else {
				paramMap.put("code", responseMap.get("status"));
				paramMap.put("message", "취소철회 대상건 발송처리 성공");
				
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			
			paTdealConnectUtil.checkException(paramMap ,  e);
		}finally {
			
			paTdealConnectUtil.closeApi(request, paramMap);
			log.info("======= 티딜 취소철회 대상건 발송처리  API End - {} =======");
		}
		
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
		
	}
}	

