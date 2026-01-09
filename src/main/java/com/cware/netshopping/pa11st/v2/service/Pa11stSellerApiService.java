package com.cware.netshopping.pa11st.v2.service;

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
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.pa11st.v2.domain.InOutAddress;
import com.cware.netshopping.pa11st.v2.domain.ns2.InOutAddresss;

/**
 * 11번가 배송 API 호출
 * http://openapi.11st.co.kr/openapi/OpenApiGuide.tmall?categoryNo=43&apiSeq=6701&apiSpecType=1
 */
@Service
public class Pa11stSellerApiService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Pa11stApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 출고지 등록 POST
	// 출고지를 등록 할 수 있습니다. Seller Office에서 등록 및 수정이 가능합니다.
	private static final String REGISTER_OUT_ADDRESS = "/rest/areaservice/v2/registerOutAddress";

	// 출고지 수정 POST
	// 출고지를 수정 할 수 있습니다. Seller Office에서 등록 및 수정이 가능합니다.
	private static final String UPDATE_OUT_ADDRESS = "/rest/areaservice/v2/updateOutAddress";

	// 반품/교환지 등록 POST
	// 반품/교환지를 등록 할 수 있습니다. Seller Office에서 등록 및 수정이 가능합니다.
	private static final String REGISTER_RETURN_ADDRESS = "/rest/areaservice/v2/registerRtnAddress";

	// 반품/교환지 수정 POST
	// 반품/교환지를 수정 할 수 있습니다. Seller Office에서 등록 및 수정이 가능합니다.
	private static final String UPDATE_RETURN_ADDRESS = "/rest/areaservice/v2/updateRtnAddress";

	// 출고지 배송비 정책 등록/수정 POST
	// 출고지 배송비 정책을 등록 및 수정 할 수 있습니다. 
	// Seller Office에서 등록 및 수정이 가능하며, 우편번호 순번 리스트는 다음 링크에서 다운로드 받으실 수 있습니다. 
	// http://openapi.11st.co.kr/download/%EC%9A%B0%ED%8E%B8%EB%B2%88%ED%98%B8.xls
	private static final String UPDATE_SHIPPING_POLICY = "/rest/areaservice/addOutAddrBasiDlvCst";

	/**
	 * 출고지등록 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param inOutAddress
	 * @param paCode
	 * @param transServiceNo
	 * @return 주소순번
	 */
	public String registerOutAddress(String entpCode, String entpManSeq, InOutAddress inOutAddress, String paCode,
			long transServiceNo) {

		UriTemplate template = new UriTemplate(REGISTER_OUT_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;
		
		uri =  apiRequest.SK11ST_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + paCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("REGISTER_OUT_ADDRESS");
		apiLog.setApiNote("11번가-출고지생성");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SK11ST.code());
		apiLog.setProcessId(PaGroup.SK11ST.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<InOutAddress> requestEntity = new HttpEntity<>(inOutAddress, apiRequest.createHttpHeaders(paCode));
		
		try {
			transLogService.logTransApiReqXml(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();
			InOutAddresss inOutAddresss = StringUtil.xmlToObject(result, InOutAddresss.class);

			apiLog.setResponsePayload(result);
			apiLog.setSuccessYn(Pa11stApiRequest.API_SUCCESS_CODE.equals(inOutAddresss.getResult_message()) ? "1" : "0");
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? Pa11stApiRequest.API_SUCCESS_CODE : Pa11stApiRequest.API_ERROR_CODE);
			apiLog.setResultMsg(inOutAddresss.getResult_message());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			// 주소순번
			String addrSeq = inOutAddresss.getIntOutAddress().getAddrSeq();
			
			return addrSeq;

		} catch (RestClientResponseException ex) {
			log.error("출고지등록={}-{} {}", entpCode, entpManSeq, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("출고지등록={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("출고지등록={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 출고지수정 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param inOutAddress
	 * @param paCode
	 * @param transServiceNo
	 * @return 주소순번
	 */
	public String updateOutAddress(String entpCode, String entpManSeq, InOutAddress inOutAddress, String paCode,
			long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_OUT_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;
		
		uri =  apiRequest.SK11ST_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + paCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("UPDATE_OUT_ADDRESS");
		apiLog.setApiNote("11번가-출고지수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SK11ST.code());
		apiLog.setProcessId(PaGroup.SK11ST.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<InOutAddress> requestEntity = new HttpEntity<>(inOutAddress, apiRequest.createHttpHeaders(paCode));
		
		try {
			transLogService.logTransApiReqXml(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();
			InOutAddresss inOutAddresss = StringUtil.xmlToObject(result, InOutAddresss.class);

			apiLog.setResponsePayload(result);
			apiLog.setSuccessYn(Pa11stApiRequest.API_SUCCESS_CODE.equals(inOutAddresss.getResult_message()) ? "1" : "0");
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? Pa11stApiRequest.API_SUCCESS_CODE : Pa11stApiRequest.API_ERROR_CODE);
			apiLog.setResultMsg(inOutAddresss.getResult_message());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			// 주소순번
			String addrSeq = inOutAddresss.getIntOutAddress().getAddrSeq();
			
			return addrSeq;

		} catch (RestClientResponseException ex) {
			log.error("출고지수정={}-{} {}", entpCode, entpManSeq, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("출고지수정={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("출고지수정={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 반품/교환지등록 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param inOutAddress
	 * @param paCode
	 * @param transServiceNo
	 * @return 주소순번
	 */
	public String registerReturnAddress(String entpCode, String entpManSeq, InOutAddress inOutAddress, String paCode, long transServiceNo) {
		
		UriTemplate template = new UriTemplate(REGISTER_RETURN_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;
		
		uri =  apiRequest.SK11ST_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + paCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("REGISTER_RETURN_ADDRESS");
		apiLog.setApiNote("11번가-반품/교환지등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SK11ST.code());
		apiLog.setProcessId(PaGroup.SK11ST.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		inOutAddress.setDtlsAddr(inOutAddress.getDtlsAddr()+"(※반품시 판매자 지정 택배사 필수, 직접발송불가)");
		
		HttpEntity<InOutAddress> requestEntity = new HttpEntity<>(inOutAddress, apiRequest.createHttpHeaders(paCode));
		
		try {
			transLogService.logTransApiReqXml(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();
			InOutAddresss inOutAddresss = StringUtil.xmlToObject(result, InOutAddresss.class);

			apiLog.setResponsePayload(result);
			apiLog.setSuccessYn(Pa11stApiRequest.API_SUCCESS_CODE.equals(inOutAddresss.getResult_message()) ? "1" : "0");
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? Pa11stApiRequest.API_SUCCESS_CODE : Pa11stApiRequest.API_ERROR_CODE);
			apiLog.setResultMsg(inOutAddresss.getResult_message());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			// 주소순번
			String addrSeq = inOutAddresss.getIntOutAddress().getAddrSeq();
			
			return addrSeq;

		} catch (RestClientResponseException ex) {
			log.error("반품/교환지등록={}-{} {}", entpCode, entpManSeq, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("반품/교환지등록={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("반품/교환지등록={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 반품/교환지수정 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param inOutAddress
	 * @param paCode
	 * @param transServiceNo
	 * @return 주소순번
	 */
	public String updateReturnAddress(String entpCode, String entpManSeq, InOutAddress inOutAddress, String paCode, long transServiceNo) {
		
		UriTemplate template = new UriTemplate(UPDATE_RETURN_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;
		
		uri =  apiRequest.SK11ST_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + paCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("UPDATE_RETURN_ADDRESS");
		apiLog.setApiNote("11번가-반품/교환지수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SK11ST.code());
		apiLog.setProcessId(PaGroup.SK11ST.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<InOutAddress> requestEntity = new HttpEntity<>(inOutAddress, apiRequest.createHttpHeaders(paCode));
		
		try {
			transLogService.logTransApiReqXml(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();
			InOutAddresss inOutAddresss = StringUtil.xmlToObject(result, InOutAddresss.class);

			apiLog.setResponsePayload(result);
			apiLog.setSuccessYn(Pa11stApiRequest.API_SUCCESS_CODE.equals(inOutAddresss.getResult_message()) ? "1" : "0");
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? Pa11stApiRequest.API_SUCCESS_CODE : Pa11stApiRequest.API_ERROR_CODE);
			apiLog.setResultMsg(inOutAddresss.getResult_message());
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			// 주소순번
			String addrSeq = inOutAddresss.getIntOutAddress().getAddrSeq();
			
			return addrSeq;

		} catch (RestClientResponseException ex) {
			log.error("반품/교환지수정={}-{} {}", entpCode, entpManSeq, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("반품/교환지수정={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("반품/교환지수정={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 출고지 배송비 정책 등록/수정 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param shipCostCode
	 * @param inOutAddress
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String updateShippingPolicy(String entpCode, String entpManSeq, String shipCostCode,
			InOutAddress inOutAddress, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_SHIPPING_POLICY);
		Map<String, String> params = new HashMap<String, String>();

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;
		
		uri =  apiRequest.SK11ST_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + shipCostCode + paCode);
		apiLog.setTransType(TransType.SHIPCOST.name());
		apiLog.setApiName("UPDATE_SHIPPING_POLICY");
		apiLog.setApiNote("11번가-출고지배송비등록/수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SK11ST.code());
		apiLog.setProcessId(PaGroup.SK11ST.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		
		HttpEntity<InOutAddress> requestEntity = new HttpEntity<>(inOutAddress, apiRequest.createHttpHeaders(paCode));
		
		try {
			transLogService.logTransApiReqXml(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String result = response.getBody();
			InOutAddresss inOutAddresss = StringUtil.xmlToObject(result, InOutAddresss.class);
			
			String resultMessage = inOutAddresss.getResult_message();

			apiLog.setResponsePayload(result);
			apiLog.setSuccessYn(Pa11stApiRequest.API_SUCCESS_CODE.equals(resultMessage) ? "1" : "0");
			apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? Pa11stApiRequest.API_SUCCESS_CODE : Pa11stApiRequest.API_ERROR_CODE);
			apiLog.setResultMsg(resultMessage);
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			return resultMessage;

		} catch (RestClientResponseException ex) {
			log.error("출고지배송비등록/수정={}-{} {} {}", entpCode, entpManSeq, shipCostCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("출고지배송비등록/수정={}-{} {} {}", entpCode, entpManSeq, shipCostCode, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("출고지배송비등록/수정={}-{} {}", entpCode, entpManSeq, shipCostCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

}
