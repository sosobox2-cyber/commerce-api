package com.cware.netshopping.palton.v2.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import com.cware.netshopping.palton.v2.domain.DvCstSr;
import com.cware.netshopping.palton.v2.domain.DvCstSrPost;
import com.cware.netshopping.palton.v2.domain.DvpSr;
import com.cware.netshopping.palton.v2.domain.DvpSrPost;

/**
 * 롯데온 배송 API 호출 https://api.lotteon.com/apiService/
 */
@Service
public class PaLtonSellerApiService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaLtonApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 판매자 출고지/반품지 등록 POST
	// 판매자의 출고지/반품지를 등록하는 API입니다.
	private static final String REGISTER_ADDRESS = "/v1/openapi/contract/v1/dvp/registDvpSr";

	// 판매자 출고지/반품지 수정 POST
	// 판매자의 출고지/반품지를 수정하는 API입니다.
	private static final String UPDATE_ADDRESS = "/v1/openapi/contract/v1/dvp/updateDvpSr";

	// 판매자 배송비정책 등록 POST
	// 판매자의 배송비정책 등록하는 API입니다.
	private static final String REGISTER_SHIPPING_POLICY = "/v1/openapi/contract/v1/dvl/registDvCstSr";

	// 판매자 배송비정책 수정 POST
	// 판매자의 배송비정책 수정하는 API입니다.
	private static final String UPDATE_SHIPPING_POLICY = "/v1/openapi/contract/v1/dvl/updateDvCstSr";

	// 거래처번호
	@Value("${partner.lotteon.api.tr.no}")
	String TR_NO;
	@Value("${partner.lotteon.api.tv.lrtr.no}")
	String TV_LRTR_NO;
	@Value("${partner.lotteon.api.online.lrtr.no}")
	String ONLINE_LRTR_NO;
	
	/**
	 * 출고/반품지등록 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param dvpSr
	 * @param paCode
	 * @param transServiceNo
	 * @return 배송지번호
	 */
	public String registerAddress(String entpCode, String entpManSeq, DvpSr dvpSr, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(REGISTER_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;

		uri = apiRequest.LOTTEON_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + paCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("REGISTER_ADDRESS");
		apiLog.setApiNote("롯데온-출고/반품지등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.LOTTEON.code());
		apiLog.setProcessId(PaGroup.LOTTEON.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		dvpSr.setAfflTrCd(TR_NO); // 거래처번호
		dvpSr.setAfflLrtrCd(PaCode.LOTTEON_TV.code().equals(paCode) ? TV_LRTR_NO : ONLINE_LRTR_NO);
		
		List<DvpSr> body = Arrays.asList(dvpSr);
		
		HttpEntity<List<DvpSr>> requestEntity = new HttpEntity<>(body, apiRequest.createHttpHeaders());

		try {
			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<DvpSrPost> response = restTemplate.exchange(uri, method, requestEntity, DvpSrPost.class);

			DvpSrPost result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setResultCode(result.getReturnCode());
			apiLog.setResultMsg(result.getMessage());
			apiLog.setSuccessYn(PaLtonApiRequest.API_SUCCESS_CODE.equals(result.getReturnCode()) ? "1" : "0");

			String dvpNo = null;
			if (result.getData() != null && result.getData().size() > 0) {
				dvpNo = result.getData().get(0).getDvpNo();
			} else if (result.getSubMessages() != null && result.getSubMessages().size() > 0) {
				apiLog.setResultMsg(result.getSubMessages().get(0));
			}

			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0"))
				throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return dvpNo;
			
		} catch (RestClientResponseException ex) {
			log.error("출고/반품지등록={}-{} {}", entpCode, entpManSeq, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorLotteon(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("출고/반품지등록={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("출고/반품지등록={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 출고/반품지수정 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param dvpSr
	 * @param paCode
	 * @param transServiceNo
	 * @return 
	 */
	public String updateAddress(String entpCode, String entpManSeq, DvpSr dvpSr, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;

		uri = apiRequest.LOTTEON_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + paCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("UPDATE_ADDRESS");
		apiLog.setApiNote("롯데온-출고/반품지수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.LOTTEON.code());
		apiLog.setProcessId(PaGroup.LOTTEON.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		dvpSr.setAfflTrCd(TR_NO); // 거래처번호
		dvpSr.setAfflLrtrCd(PaCode.LOTTEON_TV.code().equals(paCode) ? TV_LRTR_NO : ONLINE_LRTR_NO);

		List<DvpSr> body = Arrays.asList(dvpSr);
		
		HttpEntity<List<DvpSr>> requestEntity = new HttpEntity<>(body, apiRequest.createHttpHeaders());

		try {
			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<DvpSrPost> response = restTemplate.exchange(uri, method, requestEntity, DvpSrPost.class);

			DvpSrPost result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setResultCode(result.getReturnCode());
			apiLog.setResultMsg(result.getMessage());
			apiLog.setSuccessYn(PaLtonApiRequest.API_SUCCESS_CODE.equals(result.getReturnCode()) ? "1" : "0");

			if (result.getSubMessages() != null && result.getSubMessages().size() > 0) {
				apiLog.setResultMsg(result.getSubMessages().get(0));
			}
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0"))
				throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return apiLog.getResultMsg();
			
		} catch (RestClientResponseException ex) {
			log.error("출고/반품지수정={}-{} {}", entpCode, entpManSeq, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorLotteon(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("출고/반품지수정={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("출고/반품지수정={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
	
	/**
	 * 배송비정책 등록 API 호출
	 * 
	 * @param entpCode
	 * @param shipCostCode
	 * @param dvCstSr
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String registerShippingPolicy(String entpCode, String shipCostCode, DvCstSr dvCstSr, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(REGISTER_SHIPPING_POLICY);
		Map<String, String> params = new HashMap<String, String>();

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;

		uri = apiRequest.LOTTEON_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + shipCostCode + paCode);
		apiLog.setTransType(TransType.SHIPCOST.name());
		apiLog.setApiName("REGISTER_SHIPPING_POLICY");
		apiLog.setApiNote("롯데온-배송비정책등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.LOTTEON.code());
		apiLog.setProcessId(PaGroup.LOTTEON.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		dvCstSr.setAfflTrCd(TR_NO); // 거래처번호
		dvCstSr.setAfflLrtrCd(PaCode.LOTTEON_TV.code().equals(paCode) ? TV_LRTR_NO : ONLINE_LRTR_NO);

		List<DvCstSr> body = Arrays.asList(dvCstSr);
		
		HttpEntity<List<DvCstSr>> requestEntity = new HttpEntity<>(body, apiRequest.createHttpHeaders());

		try {

			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<DvCstSrPost> response = restTemplate.exchange(uri, method, requestEntity, DvCstSrPost.class);

			DvCstSrPost result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setResultCode(result.getReturnCode());
			apiLog.setResultMsg(result.getMessage());
			apiLog.setSuccessYn(PaLtonApiRequest.API_SUCCESS_CODE.equals(result.getReturnCode()) ? "1" : "0");

			String dvCstPolNo = null;
			if (result.getData() != null && result.getData().size() > 0) {
				dvCstPolNo = result.getData().get(0).getDvCstPolNo();
			} else if (result.getSubMessages() != null && result.getSubMessages().size() > 0) {
				apiLog.setResultMsg(result.getSubMessages().get(0));
			}

			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0"))
				throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return dvCstPolNo;
			
		} catch (RestClientResponseException ex) {
			log.error("배송비정책등록={} {} {}", entpCode, shipCostCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorLotteon(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("배송비정책등록={} {} {}", entpCode, shipCostCode, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("배송비정책등록={} {}", entpCode, shipCostCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 배송비정책 수정 API 호출
	 * 
	 * @param entpCode
	 * @param shipCostCode
	 * @param dvCstSr
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String updateShippingPolicy(String entpCode, String shipCostCode, DvCstSr dvCstSr, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_SHIPPING_POLICY);
		Map<String, String> params = new HashMap<String, String>();

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;

		uri = apiRequest.LOTTEON_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + shipCostCode + paCode);
		apiLog.setTransType(TransType.SHIPCOST.name());
		apiLog.setApiName("UPDATE_SHIPPING_POLICY");
		apiLog.setApiNote("롯데온-배송비정책수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.LOTTEON.code());
		apiLog.setProcessId(PaGroup.LOTTEON.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		dvCstSr.setAfflTrCd(TR_NO); // 거래처번호
		dvCstSr.setAfflLrtrCd(PaCode.LOTTEON_TV.code().equals(paCode) ? TV_LRTR_NO : ONLINE_LRTR_NO);

		List<DvCstSr> body = Arrays.asList(dvCstSr);
		
		HttpEntity<List<DvCstSr>> requestEntity = new HttpEntity<>(body, apiRequest.createHttpHeaders());

		try {

			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<DvCstSrPost> response = restTemplate.exchange(uri, method, requestEntity, DvCstSrPost.class);

			DvCstSrPost result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setResultCode(result.getReturnCode());
			apiLog.setResultMsg(result.getMessage());
			apiLog.setSuccessYn(PaLtonApiRequest.API_SUCCESS_CODE.equals(result.getReturnCode()) ? "1" : "0");

			if (result.getSubMessages() != null && result.getSubMessages().size() > 0) {
				apiLog.setResultMsg(result.getSubMessages().get(0));
			}

			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0"))
				throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return apiLog.getResultMsg();
			
		} catch (RestClientResponseException ex) {
			log.error("배송비정책수정={} {} {}", entpCode, shipCostCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorLotteon(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("배송비정책수정={} {} {}", entpCode, shipCostCode, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("배송비정책수정={} {}", entpCode, shipCostCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 추가배송비정책 등록 API 호출
	 * 
	 * @param dvCstSr
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String registerAddShippingPolicy(DvCstSr dvCstSr, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(REGISTER_SHIPPING_POLICY);
		Map<String, String> params = new HashMap<String, String>();

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;

		uri = apiRequest.LOTTEON_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(dvCstSr.getInrmAdtnDvCst().longValue() + ":" + dvCstSr.getJejuAdtnDvCst().longValue() + ":" + paCode);
		apiLog.setTransType(TransType.SHIPCOST.name());
		apiLog.setApiName("REGISTER_SHIPPING_POLICY");
		apiLog.setApiNote("롯데온-추가배송비정책등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.LOTTEON.code());
		apiLog.setProcessId(PaGroup.LOTTEON.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		dvCstSr.setAfflTrCd(TR_NO); // 거래처번호
		dvCstSr.setAfflLrtrCd(PaCode.LOTTEON_TV.code().equals(paCode) ? TV_LRTR_NO : ONLINE_LRTR_NO);

		List<DvCstSr> body = Arrays.asList(dvCstSr);
		
		HttpEntity<List<DvCstSr>> requestEntity = new HttpEntity<>(body, apiRequest.createHttpHeaders());

		try {

			transLogService.logTransApiReq(apiLog, requestEntity);

			ResponseEntity<DvCstSrPost> response = restTemplate.exchange(uri, method, requestEntity, DvCstSrPost.class);

			DvCstSrPost result = response.getBody();

			apiLog.setResponsePayload(StringUtil.objectToJson(result));
			apiLog.setResultCode(result.getReturnCode());
			apiLog.setResultMsg(result.getMessage());
			apiLog.setSuccessYn(PaLtonApiRequest.API_SUCCESS_CODE.equals(result.getReturnCode()) ? "1" : "0");

			String dvCstPolNo = null;
			if (result.getData() != null && result.getData().size() > 0) {
				dvCstPolNo = result.getData().get(0).getDvCstPolNo();
			} else if (result.getSubMessages() != null && result.getSubMessages().size() > 0) {
				apiLog.setResultMsg(result.getSubMessages().get(0));
			}

			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0"))
				throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return dvCstPolNo;
			
		} catch (RestClientResponseException ex) {
			log.error("추가배송비정책등록={} {}", apiLog.getTransCode(), ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorLotteon(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("추가배송비정책등록={} {}", apiLog.getTransCode(), ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("추가배송비정책등록={} {}", apiLog.getTransCode(), e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
}
