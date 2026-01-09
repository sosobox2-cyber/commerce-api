package com.cware.partner.coupang.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.cware.partner.common.code.Application;
import com.cware.partner.common.code.PaGroup;
import com.cware.partner.common.code.TransType;
import com.cware.partner.common.domain.ResponseMsg;
import com.cware.partner.common.domain.entity.PaTransApi;
import com.cware.partner.common.exception.TransApiException;
import com.cware.partner.common.service.TransLogService;
import com.cware.partner.common.util.StringUtil;
import com.cware.partner.coupang.domain.Product;
import com.cware.partner.coupang.domain.ProductMsg;
import com.cware.partner.coupang.domain.ProductStatus;
import com.cware.partner.coupang.domain.ProductStatusHistory;

import lombok.extern.slf4j.Slf4j;

/**
 * 제휴사 상품 API 호출
 * https://developers.coupangcorp.com/hc/ko/sections/360005046534-%EC%83%81%ED%92%88-APIs
 */
@Slf4j
@Service
public class ProductApiService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	// 상품 생성 POST
	// 상품을 생성할 때는 쿠팡 규격에 맞는 출고지/반품지, 카테고리, 고시정보, 옵션 정보 등이 필요
	// 쿠팡 OPENAPI 상품등록가이드: https://developers.coupangcorp.com/hc/ko/articles/360034889893
//	private static final String CREATE_PRODUCT = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products";

	// 상품 승인 요청 PUT
	// 임시저장 상태의 상품은 승인요청->승인완료 단계를 거처 상품 페이지에 노출
	// 주의: "인벤토리(XXXX)는 승인요청 상태가 아닙니다." 메시지는 "업체상품[XXXX]이 승인요청 가능한 상태가 아닙니다." 의 의미
	// '임시저장' 상태에서만 '승인요청'이 가능
	// 상품생성, 수정 API 에서 requested 파라메터를  true 로 입력할 경우 자동으로 판매 승인요청이 진행
//	private static final String APPROVE_PRODUCT = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/{sellerProductId}/approvals";

	// 상품 조회 GET
	// 등록상품 ID(sellerProductId)로 등록된 상품의 정보를 조회
	// 상품 가격/재고/판매상태 수정 시 필요한 옵션ID(vendorItemId)를 확인
	// 상품 정보를 조회하여 상품 수정 시 활용할 수 있는 전문을 확인
	private static final String GET_PRODUCT_BY_PRODUCT_ID = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/{sellerProductId}";

	// 상품 조회 (승인불필요) GET
	// 해당 상품의 배송 및 반품지 등의 관련 정보를 조회
	// 본 API의 응답 메시지를 활용하여, [상품 수정 API (승인불필요)]에서 빠르게 정보를 수정
//	private static final String GET_PARTIAL_PRODUCT_BY_PRODUCT_ID = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/{sellerProductId}/partial";

	// 상품 수정 (승인필요) PUT
	// '상품 조회 API'를 이용하여 조회된 JSON 전문에서 원하는 값 만 수정 후, 전체 JSON 전문을 전송하여 수정
	// 이 API를 사용하면 승인 후에 반영
	// 상품 생성 시 사용한 request body에, [sellerProductId]와 [sellerProductItemId]를 삽입하여 수정 가능
	// - 옵션 수정 : 수정하고 싶은 옵션의 아이템 상단에 [sellerProductItemId]와 [vendorItemId] 삽입 후, 원하는 [attributeValueName]으로 수정하여 전송
	// - 옵션 삭제 : 삭제하고 싶은 옵션의 'items 배열 하위 값'을 제거하고, 남기고 싶은 아이템 상단에 [sellerProductItemId] 삽입 후 전송 (이미 승인완료된 이력이 있는 옵션은 삭제 불가)
	// - 옵션 추가 : [sellerProductItemId]를 입력하지 않고 items 배열을 추가하여 전송
	// ※ [sellerProductItemId]는 상품 조회 API를 이용해 확인
	// ※ 승인 완료된 상품의 판매가격, 재고수량, 판매상태, 할인율 기준가는 상품수정API가 아닌, [상품 아이템별 수량/가격/판매여부/할인율기준가 변경] API를 통해 가능
//	private static final String UPDATE_PRODUCT = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products";

	// 상품 수정 (승인불필요) PUT
	// 배송 및 반품지 관련 정보를 별도의 승인 절차 없이 빠르게 수정
	// '임시저장중', '승인대기중' 인 상품은 수정불가
//	private static final String UPDATE_PARTIAL_PRODUCT = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/{sellerProductId}/partial";

	// 상품 삭제 DELETE
	// 상품이 승인대기중 상태가 아니며, 상품에 포함된 옵션(아이템)이 모두 판매중지된 경우에 삭제 가능
//	private static final String DELETE_PRODUCT = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/{sellerProductId}";

	// 상품 등록 현황 조회 GET
	// 판매자가 등록할 수 있는 상품수와 현재 등록되어 있는 상품수를 조회
	// 생성 가능한 최대 상품수(permittedCount)가 null일 경우 제한없이 상품등록이 가능
//	private static final String GET_INFLOW_STATUS = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/inflow-status";

	// 상품 목록 페이징 조회 GET
	// 등록상품 목록을 페이징 조회
//	private static final String GET_PRODUCTS_BY_QUERY = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products";

	// 상품 목록 구간 조회 GET
	// 등록된 상품 목록을 생성일시 기준으로 조회
	// 최대 조회 범위는 10분
//	private static final String GET_PRODUCTS_BY_TIME_FRAME = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/time-frame";

	// 상품 상태변경이력 조회 GET
	// 등록상품ID(sellerProductId)로 상품 상태변경이력을 조회
	private static final String GET_PRODUCT_STATUS_HISTORY = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/{sellerProductId}/histories";

	// 상품 요약 정보 조회 GET
	// 상품등록/수정 시  입력한 판매자 상품코드(externalVendorSku)로 상품 요약 정보를 조회
//	private static final String GET_PRODUCT_BY_EXTERNAL_SKU = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/external-vendor-sku-codes/{externalVendorSkuCode}";

	// 상품 아이템별 수량/가격/상태 조회 GET
	// 상품 아이템별 재고수량, 판매가격, 판매상태를 조회
//	private static final String GET_PRODUCT_QUANTITY_PRICE_STATUS = "/v2/providers/seller_api/apis/api/v1/marketplace/vendor-items/{vendorItemId}/inventories";

	// 상품 아이템별 수량 변경 PUT
	// 판매요청 신청 후 승인완료되어 옵션ID(vendorItemId)가 발급되면 사용가능
	private static final String UPDATE_PRODUCT_QUANTITY_BY_ITEM = "/v2/providers/seller_api/apis/api/v1/marketplace/vendor-items/{vendorItemId}/quantities/{quantity}";

	// 상품 아이템별 가격 변경 PUT
	// 판매요청 신청 후 승인완료되어 옵션ID(vendorItemId)가 발급되면 사용가능
	// forceSalePriceUpdate=true 으로 가격변경 요청 시 변경 비율 제한없이 가격변경 가능
	private static final String UPDATE_PRODUCT_PRICE_BY_ITEM = "/v2/providers/seller_api/apis/api/v1/marketplace/vendor-items/{vendorItemId}/prices/{price}";

	// 상품 아이템별 판매 재개 PUT
	// 아이템별 판매상태를 판매중으로 변경
	// 이 기능은 판매요청 신청 후 승인완료되어 옵션ID(vendorItemId)가 발급되면 사용가능
	private static final String RESUME_PRODUCT_SALES_BY_ITEM = "/v2/providers/seller_api/apis/api/v1/marketplace/vendor-items/{vendorItemId}/sales/resume";

	// 상품 아이템별 판매 중지 PUT
	// 판매요청 신청 후 승인완료되어 옵션ID(vendorItemId)가 발급되면 사용가능
	private static final String STOP_PRODUCT_SALES_BY_ITEM = "/v2/providers/seller_api/apis/api/v1/marketplace/vendor-items/{vendorItemId}/sales/stop";

	// 상품 아이템별 할인율 기준가격 변경 PUT
	// 판매요청 신청 후 승인완료되어 옵션ID(vendorItemId)가 발급되면 사용가능
	private static final String UPDATE_PRODUCT_PRICE_INCL_DISCOUNT = "/v2/providers/seller_api/apis/api/v1/marketplace/vendor-items/{vendorItemId}/original-prices/{originalPrice}";

	/**
	 * 상품의 최근 승인상태 조회
	 *
	 * @param sellerProductId 등록상품ID
	 * @param paCode 제휴사코드
	 * @return
	 */
	public ProductStatus getRecentChangeStatus(String sellerProductId, String paCode) {

		UriTemplate template = new UriTemplate(GET_PRODUCT_STATUS_HISTORY);
		Map<String, String> params = new HashMap<String, String>();
		params.put("sellerProductId", sellerProductId);

		String uri = template.expand(params).toString() + "?maxPerPage=1";

		HttpMethod method = HttpMethod.GET;
		String authorization = apiRequest.generateSignature(method.name(), uri, paCode);

		try {
			ResponseEntity<ProductStatusHistory> response = restTemplate
					.exchange(apiRequest.createRequest(method, uri, authorization), ProductStatusHistory.class);

			ProductStatusHistory result = response.getBody();

			if (result.getData() == null || result.getData().length == 0 ) return null;

			ProductStatus productStatus = result.getData()[0];

			String comment = productStatus.getComment();

			if (StringUtils.hasText(comment)) {
				comment = comment.split(",")[0];
				String[] msg = comment.split("]:");
				if (msg.length > 1) comment = msg[1];
				productStatus.setComment(comment);
			}

			return productStatus;

		} catch (Exception ex) {
			log.error("sellerProductId={}", sellerProductId, ex);
		}
		return null;

	}

	/**
	 * 상품 조회
	 *
	 * @param sellerProductId 등록상품ID
	 * @param paCode 제휴사코드
	 * @return
	 */
	public Product getProduct(String goodsCode, String sellerProductId, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(GET_PRODUCT_BY_PRODUCT_ID);
		Map<String, String> params = new HashMap<String, String>();
		params.put("sellerProductId", sellerProductId);

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.GET;
		String authorization = apiRequest.generateSignature(method.name(), uri, paCode);

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("GET_PRODUCT_BY_PRODUCT_ID");
		apiLog.setApiNote("쿠팡-상품조회");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.COUPANG.code());
		apiLog.setProcessId(Application.ID.code());

		try {

			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri, authorization);

			apiLog = transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<ProductMsg> response = restTemplate
					.exchange(apiRequest.createRequest(method, uri, authorization), ProductMsg.class);

			ProductMsg result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn(ApiRequest.API_SUCCESS_CODE.equals(result.getCode()) ? "1" : "0");
			apiLog.setResultCode(result.getCode());
			apiLog.setResultMsg(result.getMessage());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			Product product = result.getData();

			return product;

		} catch (RestClientResponseException ex) {
			transLogService.logTransApiResError(apiLog, ex);
			log.error("goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			transLogService.logTransApiResError(apiLog, e);
			log.error("goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}

	}


	/**
	 * 아이템 가격변경
	 *
	 * @param vendorItemId 옵션ID
	 * @param price 가격 최소 10원 단위로 입력가능(1원 단위 입력 불가)
	 * @param paCode 제휴사코드
	 * @return
	 */
	public ResponseMsg updateItemPrice(String goodsCode, String vendorItemId, long price, String paCode,
			long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_PRODUCT_PRICE_BY_ITEM);
		Map<String, String> params = new HashMap<String, String>();
		params.put("vendorItemId", vendorItemId);
		params.put("price", String.valueOf(price));

		// forceSalePriceUpdate
		// 가격 변경 비율 제한 여부
		// false(default 값) or true (가격 변경 비율 제한 없음)
		// 입력 실수 방지를 위해 기존 가격 대비 변경할 수 있는 가격 비율 제한함.
		// forceSalePriceUpdate=true 를 추가해서 가격변경 요청 시 처리 가능함
		String uri = template.expand(params).toString() + "?forceSalePriceUpdate=true";

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setApiName("UPDATE_PRODUCT_PRICE_BY_ITEM");
		apiLog.setApiNote("쿠팡-아이템가격변경");
		apiLog.setTransServiceNo(transServiceNo);

		return requestUpdateItem(vendorItemId, paCode, uri, apiLog);

	}

	/**
	 * 아이템 기준가격변경
	 *
	 * @param vendorItemId 옵션ID
	 * @param originalPrice 할인율기준가 0원 부터 최소 10원 단위로 입력가능(1원 단위 입력 불가)
	 * @param paCode 제휴사코드
	 * @return
	 */
	public ResponseMsg updateItemOriginalPrice(String goodsCode, String vendorItemId, long originalPrice,
			String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_PRODUCT_PRICE_INCL_DISCOUNT);
		Map<String, String> params = new HashMap<String, String>();
		params.put("vendorItemId", vendorItemId);
		params.put("originalPrice", String.valueOf(originalPrice));

		String uri = template.expand(params).toString();

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setApiName("UPDATE_PRODUCT_PRICE_INCL_DISCOUNT");
		apiLog.setApiNote("쿠팡-아이템기준가격변경");
		apiLog.setTransServiceNo(transServiceNo);

		return requestUpdateItem(vendorItemId, paCode, uri, apiLog);

	}

	/**
	 * 아이템 수량변경
	 *
	 * @param vendorItemId 옵션ID
	 * @param quantity 재고수량
	 * @param paCode 제휴사코드
	 * @return
	 */
	public ResponseMsg updateItemQuantity(String goodsCode, String vendorItemId, int quantity, String paCode,
			long transServiceNo) {

		int MAX_QUANTITY = 99999; // 최대재고수량

		UriTemplate template = new UriTemplate(UPDATE_PRODUCT_QUANTITY_BY_ITEM);
		Map<String, String> params = new HashMap<String, String>();
		params.put("vendorItemId", vendorItemId);
		params.put("quantity", String.valueOf(quantity > MAX_QUANTITY ? MAX_QUANTITY : quantity));

		String uri = template.expand(params).toString();

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setApiName("UPDATE_PRODUCT_QUANTITY_BY_ITEM");
		apiLog.setApiNote("쿠팡-아이템수량변경");
		apiLog.setTransServiceNo(transServiceNo);

		return requestUpdateItem(vendorItemId, paCode, uri, apiLog);

	}

	/**
	 * 아이템 판매재개
	 *
	 * @param vendorItemId 옵션ID
	 * @param paCode 제휴사코드
	 * @return
	 */
	public ResponseMsg resumeSaleItem(String goodsCode, String vendorItemId, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(RESUME_PRODUCT_SALES_BY_ITEM);
		Map<String, String> params = new HashMap<String, String>();
		params.put("vendorItemId", vendorItemId);

		String uri = template.expand(params).toString();

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setApiName("RESUME_PRODUCT_SALES_BY_ITEM");
		apiLog.setApiNote("쿠팡-아이템판매재개");
		apiLog.setTransServiceNo(transServiceNo);

		return requestUpdateItem(vendorItemId, paCode, uri, apiLog);
	}

	/**
	 * 아이템 판매중지
	 *
	 * @param vendorItemId 옵션ID
	 * @param paCode 제휴사코드
	 * @return
	 */
	public ResponseMsg stopSaleItem(String goodsCode, String vendorItemId, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(STOP_PRODUCT_SALES_BY_ITEM);
		Map<String, String> params = new HashMap<String, String>();
		params.put("vendorItemId", vendorItemId);

		String uri = template.expand(params).toString();

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setApiName("STOP_PRODUCT_SALES_BY_ITEM");
		apiLog.setApiNote("쿠팡-아이템판매중지");
		apiLog.setTransServiceNo(transServiceNo);

		return requestUpdateItem(vendorItemId, paCode, uri, apiLog);
	}

	/**
	 * 아이템 수정 API 호출
	 *
	 * @param vendorItemId 옵션ID
	 * @param paCode 제휴사코드
	 * @param uri API URI
	 * @return
	 */
	private ResponseMsg requestUpdateItem(String vendorItemId, String paCode, String uri,
			PaTransApi apiLog) {
		HttpMethod method = HttpMethod.PUT;
		String authorization = apiRequest.generateSignature(method.name(), uri, paCode);

		try {
			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri, authorization);

			apiLog.setTransType(TransType.PRODUCT.name());
			apiLog.setPaGroupCode(PaGroup.COUPANG.code());
			apiLog.setProcessId(Application.ID.code());
			apiLog = transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<ResponseMsg> response = restTemplate.exchange(requestEntity, ResponseMsg.class);

			ResponseMsg result = response.getBody();

			apiLog.setResponsePayload(result.toString());
			apiLog.setSuccessYn(ApiRequest.API_SUCCESS_CODE.equals(result.getCode()) ? "1" : "0");
			apiLog.setResultCode(result.getCode());
			apiLog.setResultMsg(result.getMessage());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return result;

		} catch (RestClientResponseException ex) {
			transLogService.logTransApiResError(apiLog, ex);
			log.error("goodsCode={} {}", apiLog.getTransCode(), apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("goodsCode={} {}", apiLog.getTransCode(), apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			transLogService.logTransApiResError(apiLog, e);
			log.error("goodsCode={} {}", apiLog.getTransCode(), apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
}
