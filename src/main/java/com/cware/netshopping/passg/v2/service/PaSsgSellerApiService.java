package com.cware.netshopping.passg.v2.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.passg.v2.domain.ShppcstPlcy;
import com.cware.netshopping.passg.v2.domain.ShppcstPlcyResult;
import com.cware.netshopping.passg.v2.domain.VenAddr;
import com.cware.netshopping.passg.v2.domain.VenAddrDelInfo;
import com.cware.netshopping.passg.v2.domain.VenAddrResult;

/**
 * SSG 업체기준정보 API 호출 
 * https://eapi.ssgadm.com/info/item/insertVenAddrDelInfo.ssg
 */
@Service
public class PaSsgSellerApiService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaSsgApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 업체 배송지 주소/택배계약정보 등록 POST
	// 파트너사가 상품을 출하시키고 반송받을 출고지, 반송지 주소 코드를 생성하는 API 입니다.
	// 해당 코드를 상품에 매핑하여, 해당 상품의 출고지, 반송지 주소를 관리합니다.
	// 배송지 생성 시 도로명주소와 일반주소 두 가지를 모두 입력하여 주셔야 합니다.
	// 배송지 생성 시 택배사 및 택배계약정보를 입력하실 수 있습니다.
	private static final String REGISTER_ADDRESS = "/venInfo/{version}/insertVenAddr.ssg";

	// 업체 배송지 주소/택배계약정보 수정 POST
	// 파트너사가 상품을 출하시키고 반송받을 출고지, 반송지 주소 코드를 수정하는 API 입니다.
	// 해당 코드를 상품에 매핑하여, 해당 상품의 출고지, 반송지 주소를 관리합니다.
	// 배송지 수정 시 도로명주소와 일반주소 두 가지를 모두 입력하여 주셔야 합니다.
	// 배송지 수정 시 택배사 및 택배계약정보를 입력하실 수 있습니다.
	private static final String UPDATE_ADDRESS = "/venInfo/{version}/updateVenAddr.ssg";

	// 업체 배송비 정책 등록 POST
	// 업체가 상품별로 등록을 원하는 배송비 정책을 등록하는 API 입니다.
	// 배송비는 구간별로 생성할 수 있으며, 배송비 면제 기준금액을 중심으로 해당 금액 이하일 경우 배송비를 지급 받고, 
	// 면제 기준금액 이상이면 배송비를 부과 하지 않습니다.
	// 백화점은 해당 API를 사용하실 필요가 없습니다.
	private static final String REGISTER_SHIPPING_POLICY = "/venInfo/{version}/insertShppcstPlcy.ssg";

	/**
	 * 업체배송지주소등록 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param venAddr
	 * @param paCode
	 * @param transServiceNo
	 * @return 주소생성결과
	 */
	public VenAddrDelInfo registerAddress(String entpCode, String entpManSeq, VenAddr venAddr, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(REGISTER_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", "0.3");

		String uri = template.expand(params).toString();
		
		HttpMethod method = HttpMethod.POST;

		uri = apiRequest.SSG_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + paCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("REGISTER_ADDRESS");
		apiLog.setApiNote("SSG-업체배송지주소등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SSG.code());
		apiLog.setProcessId(PaGroup.SSG.processId());
		apiLog.setApiUrl(method.name() + " " + uri);


		try {
			HttpEntity<VenAddr> requestEntity = new HttpEntity<>(venAddr, apiRequest.createHttpHeaders(paCode));
			transLogService.logTransApiReqXml(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String xmlString =response.getBody();
			VenAddrResult result = StringUtil.xmlToObject(xmlString, VenAddrResult.class);

			apiLog.setResponsePayload(xmlString);
			apiLog.setSuccessYn("00".equals(result.getResultCode()) ? "1" : "0");
			apiLog.setResultCode(result.getResultCode());
			apiLog.setResultMsg(StringUtils.hasText(result.getResultDesc()) ? result.getResultDesc() : result.getResultMessage());
			if (apiLog.getResultMsg() != null) {
				apiLog.setResultMsg(apiLog.getResultMsg()
						.replaceAll("ssg.framework.support.spring.exception.BizMsgException:", "").trim());
			}

			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return result.getVenAddrDelInfo().get(0);
			
		} catch (RestClientResponseException ex) {
			log.error("업체배송지주소등록={}-{} {}", entpCode, entpManSeq, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("업체배송지주소등록={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("업체배송지주소등록={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 업체배송지주소수정 API 호출
	 * 
	 * @param entpCode
	 * @param entpManSeq
	 * @param venAddr
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public VenAddrDelInfo updateAddress(String entpCode, String entpManSeq, VenAddr venAddr, String paCode,
			long transServiceNo) {

		UriTemplate template = new UriTemplate(UPDATE_ADDRESS);
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", "0.3");

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;

		uri = apiRequest.SSG_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(entpCode + entpManSeq + paCode);
		apiLog.setTransType(TransType.SELLER.name());
		apiLog.setApiName("UPDATE_ADDRESS");
		apiLog.setApiNote("SSG-업체배송지주소수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SSG.code());
		apiLog.setProcessId(PaGroup.SSG.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		try {
			HttpEntity<VenAddr> requestEntity = new HttpEntity<>(venAddr, apiRequest.createHttpHeaders(paCode));
			transLogService.logTransApiReqXml(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String xmlString = response.getBody();
			VenAddrResult result = StringUtil.xmlToObject(xmlString, VenAddrResult.class);

			apiLog.setResponsePayload(xmlString);
			apiLog.setSuccessYn("00".equals(result.getResultCode()) ? "1" : "0");
			apiLog.setResultCode(result.getResultCode());
			apiLog.setResultMsg(StringUtils.hasText(result.getResultDesc()) ? result.getResultDesc() : result.getResultMessage());
			if (apiLog.getResultMsg() != null) {
				apiLog.setResultMsg(apiLog.getResultMsg()
						.replaceAll("ssg.framework.support.spring.exception.BizMsgException:", "").trim());
			}

			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0"))
				throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return result.getVenAddrDelInfo().get(0);

		} catch (RestClientResponseException ex) {
			log.error("업체배송지주소수정={}-{} {}", entpCode, entpManSeq, ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("업체배송지주소수정={}-{} {}", entpCode, entpManSeq, ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("업체배송지주소수정={}-{}", entpCode, entpManSeq, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
	
	/**
	 * 업체배송비정책등록 API 호출
	 * 
	 * @param shppcstPlcy
	 * @param paCode
	 * @param transServiceNo
	 * @return
	 */
	public String registerShippingPolicy(ShppcstPlcy shppcstPlcy, String paCode, long transServiceNo) {

		UriTemplate template = new UriTemplate(REGISTER_SHIPPING_POLICY);
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", "0.1");

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.POST;

		uri = apiRequest.SSG_GATEWAY + uri;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(shppcstPlcy.getShppcstAplUnitCd() + shppcstPlcy.getShppcstPlcyDivCd() + ":"
				+ shppcstPlcy.getShppcstExmpCritnAmt() + ":" + shppcstPlcy.getShppcst() + ":" + paCode);
		apiLog.setTransType(TransType.SHIPCOST.name());
		apiLog.setApiName("REGISTER_SHIPPING_POLICY");
		apiLog.setApiNote("SSG-배송비정책등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.SSG.code());
		apiLog.setProcessId(PaGroup.SSG.processId());
		apiLog.setApiUrl(method.name() + " " + uri);

		try {

			HttpEntity<ShppcstPlcy> requestEntity = new HttpEntity<>(shppcstPlcy, apiRequest.createHttpHeaders(paCode));
			transLogService.logTransApiReqXml(apiLog, requestEntity);

			ResponseEntity<String> response = restTemplate.exchange(uri, method, requestEntity, String.class);

			String xmlString = response.getBody();
			ShppcstPlcyResult result = StringUtil.xmlToObject(xmlString, ShppcstPlcyResult.class);

			apiLog.setResponsePayload(xmlString);
			apiLog.setSuccessYn("00".equals(result.getResultCode()) ? "1" : "0");
			apiLog.setResultCode(result.getResultCode());
			apiLog.setResultMsg(StringUtils.hasText(result.getResultDesc()) ? result.getResultDesc() : result.getResultMessage());
			if (apiLog.getResultMsg() != null) {
				apiLog.setResultMsg(apiLog.getResultMsg()
						.replaceAll("ssg.framework.support.spring.exception.BizMsgException:", "").trim());
			}

			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0"))
				throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return result.getShppcstId();
			
		} catch (RestClientResponseException ex) {
			log.error("배송비정책등록={} {} {}", shppcstPlcy.getShppcstAplUnitCd(), shppcstPlcy.getShppcstPlcyDivCd(),
					ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorXml(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error("배송비정책등록={} {} {}", shppcstPlcy.getShppcstAplUnitCd(), shppcstPlcy.getShppcstPlcyDivCd(),
					ex.getMessage());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("배송비정책등록={} {}", shppcstPlcy.getShppcstAplUnitCd(), shppcstPlcy.getShppcstPlcyDivCd(), e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
}
