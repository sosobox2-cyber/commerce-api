package com.cware.netshopping.pacopn.v2.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.cware.netshopping.common.code.PaCode;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.pacopn.v2.domain.MapResponseMsg;
import com.cware.netshopping.pacopn.v2.domain.OutboundShippingPlace;

/**
 * 쿠팡 물류센터 API 호출
 * https://developers.coupangcorp.com/hc/ko/sections/360005081873-%EB%AC%BC%EB%A5%98%EC%84%BC%ED%84%B0-APIs
 */
@Service
public class PaCopnSellerApiService {

	// 방송상품 판매자
	@Value("${partner.coupang.api.tv.vendor.id}")
	String TV_VENDOR_ID;
	@Value("${partner.coupang.api.tv.vendor.user.id}")
	String TV_VENDOR_USER_ID;

	// 온라인상품 판매자
	@Value("${partner.coupang.api.online.vendor.id}")
	String ONLINE_VENDOR_ID;
	@Value("${partner.coupang.api.online.vendor.user.id}")
	String ONLINE_VENDOR_USER_ID;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaCopnApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 출고지 생성 POST
	// 판매자는 이 API를 사용하여 하나 이상의 출고지를 만들 수 있으며, 생성된 출고지는 상품 등록시 출고지를 지정할 때 사용
	// 동일한 주소지를 가진 출고지 생성은 제한됨
	// 출고지 생성/수정 시 usable 파라메터 값을 true(사용)로 설정 
	private static final String REGISTER_OUTBOUND_SHIPPING_CENTER = "/v2/providers/openapi/apis/api/v4/vendors/{vendorId}/outboundShippingCenters";

	// 출고지 수정 PUT
	// 출고지를 수정하려면 outboundShippingPlaceCode 및 remoteInfoId가 필요
	// '출고지 조회' API를 사용하여 outboundShippingPlaceCode 와 remoteInfoId를 얻을 수 있음
	private static final String UPDATE_OUTBOUND_SHIPPING_PLACECODE = "/v2/providers/openapi/apis/api/v4/vendors/{vendorId}/outboundShippingCenters/{outboundShippingPlaceCode}";

	// 출고지 조회 GET
	// 등록된 출고지 목록을 조회
	// 또는 출고지명, 출고지 코드를 사용하여 등록된 출고지 정보를 조회
	// **출고지 정보 조회시 사용하는 파라미터** 
	// 1. 등록 된 전체 출고지 목록을 조회할 경우 : pageNum와 pageSize만 입력 
	// 2. 원하는 출고지 정보만 조회할 경우 : placeNames 또는 placeCodes만 입력
//	private static final String OUTBOUND_SHIPPING_PLACE = "/v2/providers/marketplace_openapi/apis/api/v1/vendor/shipping-place/outbound";

	/**
	 * 출고지등록 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shippingPlace
	 * @param paCode
	 * @param transServiceNo
	 * @return 출고지코드
	 */
	public String registerShippingPlace(String entpCode, String entpManSeq, OutboundShippingPlace shippingPlace, String paCode, long transServiceNo) {

		// 판매자ID
		settingVendorId(shippingPlace, paCode);
		
		UriTemplate template = new UriTemplate(REGISTER_OUTBOUND_SHIPPING_CENTER);
		Map<String, String> params = new HashMap<String, String>();
		params.put("vendorId", shippingPlace.getVendorId());

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;
		String authorization = apiRequest.generateSignature(method.name(), uri, paCode);

		
		uri =  apiRequest.COUPANG_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("REGISTER_OUTBOUND_SHIPPING_CENTER");
		apiLog.setApiNote("쿠팡-출고지생성");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.COUPANG.code());
		apiLog.setProcessId(PaGroup.COUPANG.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<OutboundShippingPlace> requestEntity = new HttpEntity<>(shippingPlace, apiRequest.createHttpHeaders(authorization));
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<MapResponseMsg> response = restTemplate
					.exchange(uri, method, requestEntity, MapResponseMsg.class);

			MapResponseMsg result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn("200".equals(result.getCode()) ? "1" : "0");
			apiLog.setResultCode(result.getCode());
			apiLog.setResultMsg(result.getMessage());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			// 출고지코드
			String shippingPlaceCode = result.getData().get("resultMessage");
			
			return shippingPlaceCode;

		} catch (RestClientResponseException ex) {
			log.error("업체담당자={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			transLogService.logTransApiResError(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("업체담당자={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("업체담당자={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 방송/온라인 판매자계정 ID 설정
	 * 
	 * @param shippingPlace
	 * @param paCode
	 */
	private void settingVendorId(OutboundShippingPlace shippingPlace, String paCode) {
		if (PaCode.COUPANG_TV.code().equals(paCode)) {
			shippingPlace.setVendorId(TV_VENDOR_ID);
			shippingPlace.setUserId(TV_VENDOR_USER_ID);
		} else {
			shippingPlace.setVendorId(ONLINE_VENDOR_ID);
			shippingPlace.setUserId(ONLINE_VENDOR_USER_ID);
		}
	}

	/**
	 * 출고지수정 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shippingPlace
	 * @param paCode
	 * @param transServiceNo
	 * @return 결과메시지
	 */
	public String updateShippingPlace(String entpCode, String entpManSeq, OutboundShippingPlace shippingPlace, String paCode, long transServiceNo) {

		// 판매자ID
		settingVendorId(shippingPlace, paCode);
		
		UriTemplate template = new UriTemplate(UPDATE_OUTBOUND_SHIPPING_PLACECODE);
		Map<String, String> params = new HashMap<String, String>();
		params.put("vendorId", shippingPlace.getVendorId());
		params.put("outboundShippingPlaceCode", shippingPlace.getOutboundShippingPlaceCode());

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.PUT;
		String authorization = apiRequest.generateSignature(method.name(), uri, paCode);

		
		uri =  apiRequest.COUPANG_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("UPDATE_OUTBOUND_SHIPPING_PLACECODE");
		apiLog.setApiNote("쿠팡-출고지수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.COUPANG.code());
		apiLog.setProcessId(PaGroup.COUPANG.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<OutboundShippingPlace> requestEntity = new HttpEntity<>(shippingPlace, apiRequest.createHttpHeaders(authorization));
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<MapResponseMsg> response = restTemplate
					.exchange(uri, method, requestEntity, MapResponseMsg.class);

			MapResponseMsg result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setSuccessYn("200".equals(result.getCode()) ? "1" : "0");
			apiLog.setResultCode(result.getCode());
			apiLog.setResultMsg(result.getMessage());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			// 결과메시지
			String resultMsg = result.getData().get("resultMessage");
			
			return resultMsg;

		} catch (RestClientResponseException ex) {
			log.error("업체담당자={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			transLogService.logTransApiResError(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("업체담당자={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("업체담당자={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

}
