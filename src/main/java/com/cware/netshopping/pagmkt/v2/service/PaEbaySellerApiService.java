package com.cware.netshopping.pagmkt.v2.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.cware.netshopping.pagmkt.v2.domain.SellerAddress;
import com.cware.netshopping.pagmkt.v2.domain.ShippingPlace;
import com.cware.netshopping.pagmkt.v2.domain.ShippingPolicy;

/**
 * 이베이 배송템플릿관리 API 호출
 * https://etapi.ebaykorea.com/goods/delivery/shippingpolicies-docid-2892/
 * 
 * 배송템플릿 설정을 위한 판매자 정보- 판매자주소록/출하지/묶음배송비정책/발송타입정책 을 차례로 등록합니다
 * 등록한 판매자주소록번호 / 출하지번호 / 묶음배송비정책번호 / 발송타입정책 번호를 상품등록/수정 시 필수값으로 등록되어야 합니다
 * 판매자주소록번호 기준으로 출하지를 설정할 수 있습니다
 * 마스터ID 단위로 1회만 등록하면 마스터ID에 묶인 모든 Seller ID에 사용할 수 있습니다
 */
@Service
public class PaEbaySellerApiService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaEbayApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 판매자주소록 등록 POST
	private static final String REGISTER_SELLER_ADDRESS = "/item/v1/sellers/address";
	// 판매자주소록 수정 PUT
	private static final String UPDATE_SELLER_ADDRESS = "/item/v1/sellers/address/{addrNo}";

	// 출하지 등록 POST
	private static final String REGISTER_SHIPPING_PLACE = "/item/v1/shipping/places";
	// 출하지 수정 PUT
	private static final String UPDATE_SHIPPING_PLACE = "/item/v1/shipping/places/{placeNo}";

	// 묶음배송비 정책 등록 POST
	private static final String REGISTER_SHIPPING_POLICY = "/item/v1/shipping/policies";
	// 묶음배송비 정책 수정 PUT
	private static final String UPDATE_SHIPPING_POLICY = "/item/v1/shipping/policies/{policyNo}";


	/**
	 * 판매자주소 등록 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param sellerAddress
	 * @param transServiceNo
	 * @return 판매자주소록번호
	 */
	public String registerSellerAddress(String entpCode, String entpManSeq, SellerAddress sellerAddress, long transServiceNo) {

		String uri = REGISTER_SELLER_ADDRESS;

		HttpMethod method = HttpMethod.POST;
		String authorization = apiRequest.generateToken(method.name(), uri, PaCode.EBAY_TV.code(),
				PaGroup.GMARKET.code());

		uri =  apiRequest.EBAY_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("REGISTER_SELLER_ADDRESS");
		apiLog.setApiNote("이베이-판매자주소록등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.GMARKET.code());
		apiLog.setProcessId(PaGroup.GMARKET.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<SellerAddress> requestEntity = new HttpEntity<>(sellerAddress, apiRequest.createHttpHeaders(authorization));
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();

			apiLog.setResponsePayload(result);
			apiLog.setResultCode(PaEbayApiRequest.API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			// 판매자주소록번호
			String addrNo = String.valueOf((Integer)StringUtil.jsonToMap(result).get("addrNo"));
			
			return addrNo;

		} catch (RestClientResponseException ex) {
			log.error("판매자주소={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			transLogService.logTransApiResErrorEbay(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("판매자주소={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("판매자주소={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 판매자주소 수정 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param sellerAddress
	 * @param transServiceNo
	 * @return 판매자주소록번호
	 */
	public String updateSellerAddress(String entpCode, String entpManSeq, SellerAddress sellerAddress, long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_SELLER_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();
		params.put("addrNo", sellerAddress.getAddrNo());

		String uri = template.expand(params).toString();
		
		HttpMethod method = HttpMethod.PUT;
		String authorization = apiRequest.generateToken(method.name(), uri, PaCode.EBAY_TV.code(),
				PaGroup.GMARKET.code());

		uri =  apiRequest.EBAY_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("UPDATE_SELLER_ADDRESS");
		apiLog.setApiNote("이베이-판매자주소록수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.GMARKET.code());
		apiLog.setProcessId(PaGroup.GMARKET.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<SellerAddress> requestEntity = new HttpEntity<>(sellerAddress, apiRequest.createHttpHeaders(authorization));
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();

			apiLog.setResponsePayload(result);
			apiLog.setResultCode(PaEbayApiRequest.API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			// 판매자주소록번호
			String addrNo = String.valueOf((Integer)StringUtil.jsonToMap(result).get("addrNo"));
			
			return addrNo;

		} catch (RestClientResponseException ex) {
			log.error("판매자주소={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			transLogService.logTransApiResErrorEbay(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("판매자주소={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("판매자주소={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 출하지 등록 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param shippingPlace
	 * @param transServiceNo
	 * @return 출하지번호
	 */
	public String registerShippingPlace(String entpCode, String entpManSeq, String shipCostCode, ShippingPlace shippingPlace, long transServiceNo) {

		String uri = REGISTER_SHIPPING_PLACE;

		HttpMethod method = HttpMethod.POST;
		String authorization = apiRequest.generateToken(method.name(), uri, PaCode.EBAY_TV.code(),
				PaGroup.GMARKET.code());

		uri =  apiRequest.EBAY_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq);
		apiLog.setTransType(TransType.SHIPCOST.name());
		apiLog.setApiName("REGISTER_SHIPPING_PLACE");
		apiLog.setApiNote("이베이-출하지등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.GMARKET.code());
		apiLog.setProcessId(PaGroup.GMARKET.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<ShippingPlace> requestEntity = new HttpEntity<>(shippingPlace, apiRequest.createHttpHeaders(authorization));
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();

			apiLog.setResponsePayload(result);
			apiLog.setResultCode(PaEbayApiRequest.API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			// 출하지번호
			String placeNo = String.valueOf((Integer)StringUtil.jsonToMap(result).get("placeNo"));
			
			return placeNo;

		} catch (RestClientResponseException ex) {
			log.error("출하지={}-{} {} {}", entpCode, entpManSeq, shipCostCode, ex.getMessage());
			transLogService.logTransApiResErrorEbay(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("출하지={}-{} {} {}", entpCode, entpManSeq, shipCostCode,ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("출하지={}-{} {}", entpCode, entpManSeq, shipCostCode,e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 출하지 수정 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param shippingPlace
	 * @param transServiceNo
	 * @return 출하지번호
	 */
	public String updateShippingPlace(String entpCode, String entpManSeq, String shipCostCode, ShippingPlace shippingPlace, long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_SHIPPING_PLACE);
		Map<String, String> params = new HashMap<String, String>();
		params.put("placeNo", shippingPlace.getPlaceNo());

		String uri = template.expand(params).toString();
		
		HttpMethod method = HttpMethod.PUT;
		String authorization = apiRequest.generateToken(method.name(), uri, PaCode.EBAY_TV.code(),
				PaGroup.GMARKET.code());

		uri =  apiRequest.EBAY_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq);
		apiLog.setTransType(TransType.SHIPCOST.name());
		apiLog.setApiName("UPDATE_SHIPPING_PLACE");
		apiLog.setApiNote("이베이-출하지수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.GMARKET.code());
		apiLog.setProcessId(PaGroup.GMARKET.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<ShippingPlace> requestEntity = new HttpEntity<>(shippingPlace, apiRequest.createHttpHeaders(authorization));
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();

			apiLog.setResponsePayload(result);
			apiLog.setResultCode(PaEbayApiRequest.API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			// 출하지번호
			String placeNo = String.valueOf((Integer)StringUtil.jsonToMap(result).get("placeNo"));
			
			return placeNo;

		} catch (RestClientResponseException ex) {
			log.error("출하지={}-{} {} {}", entpCode, entpManSeq, shipCostCode, ex.getMessage());
			transLogService.logTransApiResErrorEbay(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("출하지={}-{} {} {}", entpCode, entpManSeq, shipCostCode,ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("출하지={}-{} {}", entpCode, entpManSeq, shipCostCode,e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 묶음배송비정책 등록 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param shippingPolicy
	 * @param transServiceNo
	 * @return 묶음배송비정책번호
	 */
	public String registerShippingPolicy(String entpCode, String entpManSeq, String shipCostCode, ShippingPolicy shippingPolicy, long transServiceNo) {

		String uri = REGISTER_SHIPPING_POLICY;

		HttpMethod method = HttpMethod.POST;
		String authorization = apiRequest.generateToken(method.name(), uri, PaCode.EBAY_TV.code(),
				PaGroup.GMARKET.code());

		uri =  apiRequest.EBAY_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + shipCostCode);
		apiLog.setTransType(TransType.SHIPCOST.name());
		apiLog.setApiName("REGISTER_SHIPPING_POLICY");
		apiLog.setApiNote("이베이-묶음배송비정책등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.GMARKET.code());
		apiLog.setProcessId(PaGroup.GMARKET.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<ShippingPolicy> requestEntity = new HttpEntity<>(shippingPolicy, apiRequest.createHttpHeaders(authorization));
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();

			apiLog.setResponsePayload(result);
			apiLog.setResultCode(PaEbayApiRequest.API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			// 묶음배송비정책번호
			String policyNo = String.valueOf((Integer)StringUtil.jsonToMap(result).get("policyNo"));
			
			return policyNo;

		} catch (RestClientResponseException ex) {
			log.error("묶음배송비정책={}-{} {} {}", entpCode, entpManSeq, shipCostCode, ex.getMessage());
			transLogService.logTransApiResErrorEbay(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("묶음배송비정책={}-{} {} {}", entpCode, entpManSeq, shipCostCode,ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("묶음배송비정책={}-{} {}", entpCode, entpManSeq, shipCostCode,e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 묶음배송비정책 수정 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param shippingPolicy
	 * @param transServiceNo
	 * @return 묶음배송비정책번호
	 */
	public String updateShippingPolicy(String entpCode, String entpManSeq, String shipCostCode, ShippingPolicy shippingPolicy, long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_SHIPPING_POLICY);
		Map<String, String> params = new HashMap<String, String>();
		params.put("policyNo", shippingPolicy.getPolicyNo());

		String uri = template.expand(params).toString();
		
		HttpMethod method = HttpMethod.PUT;
		String authorization = apiRequest.generateToken(method.name(), uri, PaCode.EBAY_TV.code(),
				PaGroup.GMARKET.code());

		uri =  apiRequest.EBAY_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + shipCostCode);
		apiLog.setTransType(TransType.SHIPCOST.name());
		apiLog.setApiName("UPDATE_SHIPPING_POLICY");
		apiLog.setApiNote("이베이-묶음배송비정책수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.GMARKET.code());
		apiLog.setProcessId(PaGroup.GMARKET.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<ShippingPolicy> requestEntity = new HttpEntity<>(shippingPolicy, apiRequest.createHttpHeaders(authorization));
		
		try {
			transLogService.logTransApiReq(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();

			apiLog.setResponsePayload(result);
			apiLog.setResultCode(PaEbayApiRequest.API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			// 묶음배송비정책번호
			String policyNo = String.valueOf((Integer)StringUtil.jsonToMap(result).get("policyNo"));
			
			return policyNo;

		} catch (RestClientResponseException ex) {
			log.error("묶음배송비정책={}-{} {} {}", entpCode, entpManSeq, shipCostCode, ex.getMessage());
			transLogService.logTransApiResErrorEbay(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("묶음배송비정책={}-{} {} {}", entpCode, entpManSeq, shipCostCode,ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("묶음배송비정책={}-{} {}", entpCode, entpManSeq, shipCostCode,e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
}
