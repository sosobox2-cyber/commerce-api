package com.cware.netshopping.pakakao.v2.service;

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

import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.repository.TransLogMapper;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.pakakao.v2.domain.SellerAddress;

/**
 * 카카오 셀러 API 호출
 * 
 * https://shopping-developers.kakao.com/hc/ko/articles/4578925615119
 */
@Service
public class PaKakaoSellerApiService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaKakaoApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	@Autowired
	private TransLogMapper logMapper;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 판매자 주소록 등록 POST
	// 파트너사가 상품을 출하시키고 반송받을 출고지, 반송지 주소 코드를 생성하는 API 입니다.
	// 해당 코드를 상품에 매핑하여, 해당 상품의 출고지, 반송지 주소를 관리합니다.
	// 배송지 생성 시 도로명주소와 일반주소 두 가지를 모두 입력하여 주셔야 합니다.
	// 배송지 생성 시 택배사 및 택배계약정보를 입력하실 수 있습니다.
	private static final String REGISTER_ADDRESS = "/v1/shopping/bizseller/seller-addresses/register";

	// 판매자 주소록 수정 PUT
	// 파트너사가 상품을 출하시키고 반송받을 출고지, 반송지 주소 코드를 수정하는 API 입니다.
	// 해당 코드를 상품에 매핑하여, 해당 상품의 출고지, 반송지 주소를 관리합니다.
	// 배송지 수정 시 도로명주소와 일반주소 두 가지를 모두 입력하여 주셔야 합니다.
	// 배송지 수정 시 택배사 및 택배계약정보를 입력하실 수 있습니다.
	private static final String UPDATE_ADDRESS = "/v1/shopping/bizseller/seller-addresses/modify";

	/**
	 * 판매자주소록등록 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param sellerAddress
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String registerAddress(String entpCode, String entpManSeq, SellerAddress sellerAddress, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(REGISTER_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();

		String uri = template.expand(params).toString();
		
		HttpMethod method = HttpMethod.POST;

		uri = apiRequest.KAKAO_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + paCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("REGISTER_ADDRESS");
		apiLog.setApiNote("카카오-판매자주소록등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.KAKAO.code());
		apiLog.setProcessId(PaGroup.KAKAO.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		apiLog.setTransApiNo(logMapper.createTransApiNo());
		apiLog.setRequestPayload(StringUtil.objectToJson(sellerAddress));

		try {
			HttpEntity<String> requestEntity = new HttpEntity<>(apiLog.getRequestPayload(), apiRequest.createHttpHeaders(paCode));

			transLogService.logTransApi(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();

			Map<String, Object> resultMap = StringUtil.jsonToMap(result);
			String id = String.valueOf(resultMap.get("id"));

			apiLog.setResponsePayload(result);
			apiLog.setSuccessYn("null".equals(id) ? "0" : "1");
			
			if ("0".equals(apiLog.getSuccessYn())) {
				
				Map<String,Object> validationMap = (Map<String, Object>) resultMap.get("validation");
				String addressDetail = String.valueOf(validationMap.get("addressDetail")).replaceAll(" ","");
				if ((addressDetail != null) && (addressDetail.contains("이미등록된주소입니다"))) {
					id = addressDetail.replaceAll("[^\\d]", "");
					apiLog.setSuccessYn("1");
				}
			}
			
			if ("1".equals(apiLog.getSuccessYn())) {
				apiLog.setResultCode(PaKakaoApiRequest.API_SUCCESS_CODE);
			} else {
				apiLog.setResultCode(String.valueOf(resultMap.get("errorCode")));
				apiLog.setResultMsg(String.valueOf(resultMap.get("validation")));
				if ("null".equals(apiLog.getResultMsg())) {
					apiLog.setResultMsg(String.valueOf(resultMap.get("errorMessage")));
				}
			}
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return id;
			
		} catch (RestClientResponseException ex) {
			log.error("판매자주소록등록={}-{} {}", entpCode, entpManSeq, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorKakaoSeller(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("판매자주소록등록={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("판매자주소록등록={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
	/**
	 * 판매자주소록수정 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param sellerAddress
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String updateAddress(String entpCode, String entpManSeq, SellerAddress sellerAddress, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();

		String uri = template.expand(params).toString();
		
		HttpMethod method = HttpMethod.PUT;

		uri = apiRequest.KAKAO_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + paCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("UPDATE_ADDRESS");
		apiLog.setApiNote("카카오-판매자주소록수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.KAKAO.code());
		apiLog.setProcessId(PaGroup.KAKAO.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		apiLog.setTransApiNo(logMapper.createTransApiNo());
		apiLog.setRequestPayload(StringUtil.objectToJson(sellerAddress));

		try {
			HttpEntity<String> requestEntity = new HttpEntity<>(apiLog.getRequestPayload(), apiRequest.createHttpHeaders(paCode));

			transLogService.logTransApi(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();
			Map<String, Object> resultMap = StringUtil.jsonToMap(result);
			String id = String.valueOf(resultMap.get("id"));
			
			apiLog.setResponsePayload(result);
			apiLog.setSuccessYn("null".equals(id) ? "0" : "1");
			
			if ("0".equals(apiLog.getSuccessYn())) {
				
				Map<String,Object> validationMap = (Map<String, Object>) resultMap.get("validation");
				String addressDetail = String.valueOf(validationMap.get("addressDetail")).replaceAll(" ","");
				if ((addressDetail != null) && (addressDetail.contains("이미등록된주소입니다"))) {
					id = addressDetail.replaceAll("[^\\d]", "");
					apiLog.setSuccessYn("1");
				}
			}
			
			if ("1".equals(apiLog.getSuccessYn())) {
				apiLog.setResultCode(PaKakaoApiRequest.API_SUCCESS_CODE);
			} else {
				apiLog.setResultCode(String.valueOf(resultMap.get("errorCode")));
				apiLog.setResultMsg(String.valueOf(resultMap.get("validation")));
				if ("null".equals(apiLog.getResultMsg())) {
					apiLog.setResultMsg(String.valueOf(resultMap.get("errorMessage")));
				}
			}
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return id;
			
		} catch (RestClientResponseException ex) {
			log.error("판매자주소록수정={}-{} {}", entpCode, entpManSeq, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorKakaoSeller(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("판매자주소록수정={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("판매자주소록수정={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

}
