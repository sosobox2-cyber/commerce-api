package com.cware.netshopping.paintp.v2.service;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
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
import com.cware.netshopping.common.log.repository.TransLogMapper;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.paintp.v2.domain.Delv;
import com.cware.netshopping.paintp.v2.domain.DelvCost;
import com.cware.netshopping.paintp.v2.domain.DelvCostPlc;
import com.cware.netshopping.paintp.v2.domain.DelvCostResult;
import com.cware.netshopping.paintp.v2.domain.DelvItem;

/**
 * 인터파크 배송 API 호출
 * http://www.interpark.com/openapi/site/APIDelvInsertSpec.jsp
 * http://www.interpark.com/openapi/site/APIDelvCostPolicyInsertSpec.jsp
 * 
 */
@Service
public class PaIntpSellerApiService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaIntpApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	@Autowired
	private TransLogMapper logMapper;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 반품배송지 등록 GET
	// 반품배송지 등록 API는 유저(개발자)에게 빠르게 반품배송지를 등록할 수 있도록 제공하는 API 서비스 입니다. 
	// API는 유저가 지정한 특정 URL의 XML 데이터에 접근하여 데이터를 파싱한 후 인터파크 DB에 반품배송지 등록을 진행하며, 
	// 반품배송지 등록 과정이 완료 되면 결과를 다시 유저에게 XML 데이터로 전달 합니다.
	private static final String REGISTER_RETURN_ADDRESS = "/openapi/enterprise/EntrDelvAPIService.do?_method=insertDelvAPIData&citeKey={citeKey}&secretKey={secretKey}&dataUrl={dataUrl}";

	// 반품배송지등록 인증키
	@Value("${partner.interpark.api.seller.address.register.tv.cite.key}")
	String REGISTER_RETURN_ADDRESS_TV_CITE_KEY;
	@Value("${partner.interpark.api.seller.address.register.tv.secret.key}")
	String REGISTER_RETURN_ADDRESS_TV_SECRET_KEY;
	@Value("${partner.interpark.api.seller.address.register.online.cite.key}")
	String REGISTER_RETURN_ADDRESS_ONLINE_CITE_KEY;
	@Value("${partner.interpark.api.seller.address.register.online.secret.key}")
	String REGISTER_RETURN_ADDRESS_ONLINE_SECRET_KEY;
	
	// 반품배송지 수정 GET
	// 반품배송지수정 API는 빠르게 반품배송지를 수정할 수 있도록 제공하는 API 서비스 입니다.
	// API는 유저가 지정한 특정 조건에 해당하는 Parameter로 인터파크 DB에 반품배송지 수정을 진행하며, 
	// 반품배송지 수정 과정이 완료 되면 결과를 다시 유저에게 XML 데이터로 전달 합니다.
	private static final String UPDATE_RETURN_ADDRESS = "/openapi/enterprise/EntrDelvAPIService.do?_method=updateDelvAPIData&citeKey={citeKey}&secretKey={secretKey}&dataUrl={dataUrl}";

	// 반품배송지수정 인증키
	@Value("${partner.interpark.api.seller.address.update.tv.cite.key}")
	String UPDATE_RETURN_ADDRESS_TV_CITE_KEY;
	@Value("${partner.interpark.api.seller.address.update.tv.secret.key}")
	String UPDATE_RETURN_ADDRESS_TV_SECRET_KEY;
	@Value("${partner.interpark.api.seller.address.update.online.cite.key}")
	String UPDATE_RETURN_ADDRESS_ONLINE_CITE_KEY;
	@Value("${partner.interpark.api.seller.address.update.online.secret.key}")
	String UPDATE_RETURN_ADDRESS_ONLINE_SECRET_KEY;
	
	// 배송비정책 등록 GET
	// 배송비정책 등록 API는 유저(개발자)에게 빠르게 묶음배송비 정책을 등록할 수 있도록 제공하는 API 서비스 입니다.
	// API는 유저가 지정한 특정 URL의 XML 데이터에 접근하여 데이터를 파싱한 후 인터파크 DB에 배송비정책 등록을 진행하며, 
	// 배송비정책 등록 과정이 완료 되면 해당 업체의 배송비정책 목록을 유저에게 XML 데이터로 전달 합니다.
	private static final String REGISTER_SHIPPING_POLICY = "/openapi/enterprise/EntrDelvAPIService.do?_method=insertDelvCostPlcAPIData&citeKey={citeKey}&secretKey={secretKey}&dataUrl={dataUrl}";

	// 배송비정책등록 인증키
	@Value("${partner.interpark.api.seller.shipcost.register.tv.cite.key}")
	String REGISTER_SHIPPING_POLICY_TV_CITE_KEY;
	@Value("${partner.interpark.api.seller.shipcost.register.tv.secret.key}")
	String REGISTER_SHIPPING_POLICY_TV_SECRET_KEY;
	@Value("${partner.interpark.api.seller.shipcost.register.online.cite.key}")
	String REGISTER_SHIPPING_POLICY_ONLINE_CITE_KEY;
	@Value("${partner.interpark.api.seller.shipcost.register.online.secret.key}")
	String REGISTER_SHIPPING_POLICY_ONLINE_SECRET_KEY;
	
	
	// 배송비정책 수정 GET
	// 배송비정책 수정 API는 유저(개발자)에게 빠르게 묶음배송비정책를 수정할 수 있도록 제공하는 API 서비스 입니다. 
	// API는 유저가 지정한 특정 URL의 XML 데이터에 접근하여 데이터를 파싱한 후 인터파크 DB에 배송비정책 수정을 진행하며, 
	// 배송비정책 수정 과정이 완료 되면 해당 업체의 배송비정책 목록을 유저에게 XML 데이터로 전달 합니다.
	private static final String UPDATE_SHIPPING_POLICY = "/openapi/enterprise/EntrDelvAPIService.do?_method=updateDelvCostPlcAPIData&citeKey={citeKey}&secretKey={secretKey}&dataUrl={dataUrl}";

	// 배송비정책수정 인증키
	@Value("${partner.interpark.api.seller.shipcost.update.tv.cite.key}")
	String UPDATE_SHIPPING_POLICY_TV_CITE_KEY;
	@Value("${partner.interpark.api.seller.shipcost.update.tv.secret.key}")
	String UPDATE_SHIPPING_POLICY_TV_SECRET_KEY;
	@Value("${partner.interpark.api.seller.shipcost.update.online.cite.key}")
	String UPDATE_SHIPPING_POLICY_ONLINE_CITE_KEY;
	@Value("${partner.interpark.api.seller.shipcost.update.online.secret.key}")
	String UPDATE_SHIPPING_POLICY_ONLINE_SECRET_KEY;
	
	/**
	 * 반품배송지등록 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param delvItem
	 * @param paCode
	 * @param transServiceNo
	 * @return 주소순번
	 */
	public String registerReturnAddress(String entpCode, String entpManSeq, DelvItem delvItem, String paCode, long transServiceNo) {

		String citeKey, secretKey;

		if (PaCode.INTERPARK_TV.code().equals(paCode)) {
			citeKey = REGISTER_RETURN_ADDRESS_TV_CITE_KEY;
			secretKey = REGISTER_RETURN_ADDRESS_TV_SECRET_KEY;
		} else {
			citeKey = REGISTER_RETURN_ADDRESS_ONLINE_CITE_KEY;
			secretKey = REGISTER_RETURN_ADDRESS_ONLINE_SECRET_KEY;
		}

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + paCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("REGISTER_RETURN_ADDRESS");
		apiLog.setApiNote("인터파크-반품배송지등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.INTERPARK.code());
		apiLog.setProcessId(PaGroup.INTERPARK.processId());
		apiLog.setTransApiNo(logMapper.createTransApiNo());

		UriTemplate template = new UriTemplate(REGISTER_RETURN_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();
		params.put("citeKey", citeKey);
		params.put("secretKey", secretKey);
		params.put("dataUrl", apiRequest.INTERPARK_PAYLOAD_HOST
				+ apiRequest.INTERPARK_PAYLOAD_PATH + "/" + apiLog.getTransCode() + "/" + apiLog.getTransApiNo());

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.GET;
		
		try {

			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri);

			Delv delv = new Delv();
			delv.setItem(delvItem);
			apiLog.setRequestPayload(StringUtil.objectToXml(JAXBContext.newInstance(Delv.class), delv, "EUC-KR"));
			transLogService.logTransApi(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

			String result = response.getBody();
			apiLog.setResponsePayload(result);
			
			Delv delvResult = StringUtil.xmlToObject(result, Delv.class);

			apiLog.setSuccessYn(delvResult.getItem() != null ? "1" : "0");

			if (delvResult.getError() != null) {
				apiLog.setResultCode(delvResult.getError().getCode());
				apiLog.setResultMsg(delvResult.getError().getExplanation());
			} else {
				apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? PaIntpApiRequest.API_SUCCESS_CODE : PaIntpApiRequest.API_ERROR_CODE);
			}
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			
			// 주소순번
			String addrSeq = delvResult.getItem().getEntrDelvInfoNo();
			
			return addrSeq;

		} catch (RestClientResponseException ex) {
			log.error("반품배송지등록={}-{} {}", entpCode, entpManSeq, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("반품배송지등록={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("반품배송지등록={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 반품배송지수정 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param delvItem
	 * @param paCode
	 * @param transServiceNo
	 * @return 주소순번
	 */
	public String updateReturnAddress(String entpCode, String entpManSeq, DelvItem delvItem, String paCode, long transServiceNo) {

		String citeKey, secretKey;

		if (PaCode.INTERPARK_TV.code().equals(paCode)) {
			citeKey = UPDATE_RETURN_ADDRESS_TV_CITE_KEY;
			secretKey = UPDATE_RETURN_ADDRESS_TV_SECRET_KEY;
		} else {
			citeKey = UPDATE_RETURN_ADDRESS_ONLINE_CITE_KEY;
			secretKey = UPDATE_RETURN_ADDRESS_ONLINE_SECRET_KEY;
		}

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + paCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("UPDATE_RETURN_ADDRESS");
		apiLog.setApiNote("인터파크-반품배송지수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.INTERPARK.code());
		apiLog.setProcessId(PaGroup.INTERPARK.processId());
		apiLog.setTransApiNo(logMapper.createTransApiNo());

		UriTemplate template = new UriTemplate(UPDATE_RETURN_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();
		params.put("citeKey", citeKey);
		params.put("secretKey", secretKey);
		params.put("dataUrl", apiRequest.INTERPARK_PAYLOAD_HOST
				+ apiRequest.INTERPARK_PAYLOAD_PATH + "/" + apiLog.getTransCode() + "/" + apiLog.getTransApiNo());

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.GET;
		
		try {

			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri);

			Delv delv = new Delv();
			delv.setItem(delvItem);
			apiLog.setRequestPayload(StringUtil.objectToXml(JAXBContext.newInstance(Delv.class), delv, "EUC-KR"));
			transLogService.logTransApi(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

			String result = response.getBody();
			apiLog.setResponsePayload(result);
			Delv delvResult = StringUtil.xmlToObject(result, Delv.class);

			apiLog.setSuccessYn(delvResult.getItem() != null ? "1" : "0");

			if (delvResult.getError() != null) {
				apiLog.setResultCode(delvResult.getError().getCode());
				apiLog.setResultMsg(delvResult.getError().getExplanation());
			} else {
				apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? PaIntpApiRequest.API_SUCCESS_CODE : PaIntpApiRequest.API_ERROR_CODE);
			}
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			// 주소순번
			String addrSeq = delvResult.getItem().getEntrDelvInfoNo();
			
			return addrSeq;

		} catch (RestClientResponseException ex) {
			log.error("반품배송지수정={}-{} {}", entpCode, entpManSeq, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("반품배송지수정={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("반품배송지수정={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
	

	/**
	 * 배송비정책 등록
	 * 
	 * @param entpCode
	 * @param shipCostCode
	 * @param delvCostPlc
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String registerShippingPolicy(String entpCode, String shipCostCode, DelvCostPlc delvCostPlc, String paCode, long transServiceNo) {

		String citeKey, secretKey;

		if (PaCode.INTERPARK_TV.code().equals(paCode)) {
			citeKey = REGISTER_SHIPPING_POLICY_TV_CITE_KEY;
			secretKey = REGISTER_SHIPPING_POLICY_TV_SECRET_KEY;
		} else {
			citeKey = REGISTER_SHIPPING_POLICY_ONLINE_CITE_KEY;
			secretKey = REGISTER_SHIPPING_POLICY_ONLINE_SECRET_KEY;
		}

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + shipCostCode + paCode);
		apiLog.setTransType(TransType.SHIPCOST.name());
		apiLog.setApiName("REGISTER_SHIPPING_POLICY");
		apiLog.setApiNote("인터파크-배송비정책등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.INTERPARK.code());
		apiLog.setProcessId(PaGroup.INTERPARK.processId());
		apiLog.setTransApiNo(logMapper.createTransApiNo());

		UriTemplate template = new UriTemplate(REGISTER_SHIPPING_POLICY);
		Map<String, String> params = new HashMap<String, String>();
		params.put("citeKey", citeKey);
		params.put("secretKey", secretKey);
		params.put("dataUrl", apiRequest.INTERPARK_PAYLOAD_HOST
				+ apiRequest.INTERPARK_PAYLOAD_PATH + "/" + apiLog.getTransCode() + "/" + apiLog.getTransApiNo());

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.GET;
		
		try {

			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri);

			DelvCost delvCost = new DelvCost();
			delvCost.setItem(delvCostPlc);
			apiLog.setRequestPayload(StringUtil.objectToXml(JAXBContext.newInstance(DelvCost.class), delvCost, "EUC-KR"));
			transLogService.logTransApi(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

			String result = response.getBody();
			apiLog.setResponsePayload(result);
			DelvCostResult costResult = StringUtil.xmlToObject(result, DelvCostResult.class);

			apiLog.setSuccessYn(costResult.getDelvCostPlc() != null ? "1" : "0");

			if (costResult.getError() != null) {
				apiLog.setResultCode(costResult.getError().getCode());
				apiLog.setResultMsg(costResult.getError().getExplanation());
			} else {
				apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? PaIntpApiRequest.API_SUCCESS_CODE : PaIntpApiRequest.API_ERROR_CODE);
			}
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			
			// 배송비정책번호
			String delvCostPlcNo = costResult.getDelvCostPlc().getDelvCostPlcNo();
			
			return delvCostPlcNo;

		} catch (RestClientResponseException ex) {
			log.error("배송비정책등록={}-{} {}", entpCode, shipCostCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("배송비정책등록={}-{} {}", entpCode, shipCostCode, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("배송비정책등록={}-{}", entpCode, shipCostCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 배송비정책 수정
	 * 
	 * @param entpCode
	 * @param shipCostCode
	 * @param delvCostPlc
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String updateShippingPolicy(String entpCode, String shipCostCode, DelvCostPlc delvCostPlc, String paCode, long transServiceNo) {

		String citeKey, secretKey;

		if (PaCode.INTERPARK_TV.code().equals(paCode)) {
			citeKey = UPDATE_SHIPPING_POLICY_TV_CITE_KEY;
			secretKey = UPDATE_SHIPPING_POLICY_TV_SECRET_KEY;
		} else {
			citeKey = UPDATE_SHIPPING_POLICY_ONLINE_CITE_KEY;
			secretKey = UPDATE_SHIPPING_POLICY_ONLINE_SECRET_KEY;
		}

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + shipCostCode + paCode);
		apiLog.setTransType(TransType.SHIPCOST.name());
		apiLog.setApiName("UPDATE_SHIPPING_POLICY");
		apiLog.setApiNote("인터파크-배송비정책수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.INTERPARK.code());
		apiLog.setProcessId(PaGroup.INTERPARK.processId());
		apiLog.setTransApiNo(logMapper.createTransApiNo());

		UriTemplate template = new UriTemplate(UPDATE_SHIPPING_POLICY);
		Map<String, String> params = new HashMap<String, String>();
		params.put("citeKey", citeKey);
		params.put("secretKey", secretKey);
		params.put("dataUrl", apiRequest.INTERPARK_PAYLOAD_HOST
				+ apiRequest.INTERPARK_PAYLOAD_PATH + "/" + apiLog.getTransCode() + "/" + apiLog.getTransApiNo());

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.GET;
		
		try {

			RequestEntity<Void> requestEntity = apiRequest.createRequest(method, uri);

			DelvCost delvCost = new DelvCost();
			delvCost.setItem(delvCostPlc);
			apiLog.setRequestPayload(StringUtil.objectToXml(JAXBContext.newInstance(DelvCost.class), delvCost, "EUC-KR"));
			transLogService.logTransApi(apiLog, requestEntity);
			
			ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

			String result = response.getBody();
			apiLog.setResponsePayload(result);
			DelvCostResult costResult = StringUtil.xmlToObject(result, DelvCostResult.class);

			apiLog.setSuccessYn(costResult.getDelvCostPlc() != null ? "1" : "0");

			if (costResult.getError() != null) {
				apiLog.setResultCode(costResult.getError().getCode());
				apiLog.setResultMsg(costResult.getError().getExplanation());
			} else {
				apiLog.setResultCode(apiLog.getSuccessYn().equals("1") ? PaIntpApiRequest.API_SUCCESS_CODE : PaIntpApiRequest.API_ERROR_CODE);
			}
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			
			// 배송비정책번호
			String delvCostPlcNo = costResult.getDelvCostPlc().getDelvCostPlcNo();
			
			return delvCostPlcNo;

		} catch (RestClientResponseException ex) {
			log.error("배송비정책수정={}-{} {}", entpCode, shipCostCode, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("배송비정책수정={}-{} {}", entpCode, shipCostCode, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("배송비정책수정={}-{}", entpCode, shipCostCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
	
}
