package com.cware.netshopping.panaver.v2.service;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cware.api.panaver.product.type.GetOptionRequestType;
import com.cware.api.panaver.product.type.GetOptionResponseType;
import com.cware.api.panaver.product.type.GetProductRequestType;
import com.cware.api.panaver.product.type.GetProductResponseType;
import com.cware.api.panaver.product.type.ImageService;
import com.cware.api.panaver.product.type.ImageServicePortType;
import com.cware.api.panaver.product.type.ImageURLListType;
import com.cware.api.panaver.product.type.ManageOptionRequestType;
import com.cware.api.panaver.product.type.ManageOptionResponseType;
import com.cware.api.panaver.product.type.ManageProductRequestType;
import com.cware.api.panaver.product.type.ManageProductResponseType;
import com.cware.api.panaver.product.type.OptionType;
import com.cware.api.panaver.product.type.ProductService;
import com.cware.api.panaver.product.type.ProductServicePortType;
import com.cware.api.panaver.product.type.ProductType;
import com.cware.api.panaver.product.type.UploadImageRequestType;
import com.cware.api.panaver.product.type.UploadImageResponseType;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;

/**
 * 네이버 상품 API 호출 (SOAP)
 */
@Service
public class PaNaverProductApiService {
	
	@Autowired
	private PaNaverApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String SERVICE_NAME = "ProductService";

	// 상품등록
	// 상품을 등록하거나 수정한다.
	// 요청 정보 안에 포함된 <Product> 요소 내의 <ProductId>의 값이 없을 경우에는 상품을 등록하고
	// <ProductId> 값이 존재할 경우에는 해당 ID에 해당하는 상품의 내용을 수정한다
	private static final String CREATE_PRODUCT = "ManageProduct";

	// 상품수정
	private static final String UPDATE_PRODUCT = "ManageProduct";

	// 상품조회
	// ManageProduct를 통해 입력한 상품 정보를 조회한다.
	private static final String GET_PRODUCT_BY_PRODUCT_ID = "GetProduct";

	// 옵션등록/수정
	// 특정 상품에 옵션을 등록하거나 수정한다.
	// 옵션은 단독형, 조합형, 직접 입력형 옵션 세 가지 유형이 있다. 
	// 이 중 단독형 옵션과 조합형 옵션은 함께 입력할 수 없다.
	// 각 옵션 유형은 복수 개의 아이템으로 구성된다. 
	// 각 옵션 아이템에 <Id> 값이 없는 경우 해당 옵션을	새로 추가한다. 
	// 옵션 아이템에 <Id> 필드가 있으면 해당 Id에 해당하는 옵션을 수정한다. 
	// 옵션	아이템을 입력하지 않으면 해당 옵션은 삭제한다. 
	// 옵션 Id는 GetOption API를 사용하여 조회할 수 있다.
	// 조합형 옵션의 경우 옵션명이 변경되면 옵션 Id를 입력해도 무시하며 무조건 기존에 등록되어 있는	조합형 옵션을 삭제하고 새로 등록한다.
	private static final String MANAGE_OPTION = "ManageOption";

	// 옵션조회
	// ManageOption를 통해 입력한 옵션 정보를 조회한다.
	private static final String GET_OPTION_BY_PRODUCT_ID = "GetOption";
	
	// 이미지업로드 (ImageService)
	// SOAP 프로토콜을 사용하여 네이버 포토 인프라에 상품 등록 시 사용할 이미지를 업로드한다
	// 업로드할 이미지는 URL 형태로만 입력할 수 있으며, 업로드할 수 있는 이미지의 형식은 다음과 같다.
	// JPG, GIF, PNG, BMP
	// 이미지 업로드 시 사용할 이미지 사이즈는 "ImageType"을 참조한다
	// 업로드된 이미지의 URL을 반환한다.
	private static final String CREATE_IMAGE = "UploadImage";

	@Value("${partner.naver.api.mall.id}")
	String MALL_ID;


	@Value("${partner.api.read-timeout}")
	int READ_TIMEOUT;

	@Value("${partner.api.connect-timeout}")
	int CONNECT_TIMEOUT;

	/**
	 * 상품등록API 호출
	 * 
	 * @param goodsCode
	 * @param product
	 * @param transServiceNo
	 * @return
	 */
	public String registerProduct(String goodsCode, ProductType product, long transServiceNo) {

		String operationName = CREATE_PRODUCT;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("CREATE_PRODUCT");
		apiLog.setApiNote("네이버-상품생성");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.NAVER.code());
		apiLog.setProcessId(PaGroup.NAVER.processId());
		apiLog.setApiUrl(SERVICE_NAME + "#" + operationName);
		
		try {
			return manageProduct(product, operationName, apiLog);
		} catch (TransApiException ex) {
			log.error("상품등록 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("상품등록 goodsCode={}", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	private String manageProduct(ProductType product, String operationName, PaTransApi apiLog) {
		ManageProductRequestType request = new ManageProductRequestType();
		request = (ManageProductRequestType) apiRequest.createRequest(request, SERVICE_NAME, operationName);

		request.setSellerId(MALL_ID);
		request.setProduct(product);
		
		transLogService.logTransApiReqSoap(apiLog, request);

		ProductServicePortType productService = (new ProductService()).getProductServiceSOAP12PortHttp();
		
		// SOAP 타임아웃 설정
		Map<String, Object> requestContext = ((BindingProvider)productService).getRequestContext();
		requestContext.put("com.sun.xml.internal.ws.connect.timeout", CONNECT_TIMEOUT);
		requestContext.put("com.sun.xml.internal.ws.request.timeout", READ_TIMEOUT);

		ManageProductResponseType response = productService.manageProduct(request);

		apiLog.setResponsePayload(StringUtil.objectToXml(response));
		apiLog.setResultCode(response.getResponseType());

		apiLog.setSuccessYn(PaNaverApiRequest.API_SUCCESS_CODE.equals(apiLog.getResultCode()) ? "1" : "0");

		if (response.getError() != null) {
			apiLog.setResultCode(response.getError().getCode());
			apiLog.setResultMsg(response.getError().getMessage() + "-" + response.getError().getDetail());
		}

		transLogService.logTransApiRes(apiLog);

		if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		
		return String.valueOf(response.getProductId());
	}

	/**
	 * 상품수정API 호출
	 * 
	 * @param goodsCode
	 * @param product
	 * @param transServiceNo
	 * @return
	 */
	public String updateProduct(String goodsCode, ProductType product, long transServiceNo) {

		String operationName = UPDATE_PRODUCT;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("UPDATE_PRODUCT");
		apiLog.setApiNote("네이버-상품수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.NAVER.code());
		apiLog.setProcessId(PaGroup.NAVER.processId());
		apiLog.setApiUrl(SERVICE_NAME + "#" + operationName);
		
		try {
			return manageProduct(product, operationName, apiLog);
		} catch (TransApiException ex) {
			log.error("상품수정 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("상품수정 goodsCode={}", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 상품 조회
	 * 
	 * @param goodsCode
	 * @param productId
	 * @param transServiceNo
	 * @return
	 */
	public ProductType getProduct(String goodsCode, String productId, long transServiceNo) {

		String operationName = GET_PRODUCT_BY_PRODUCT_ID;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("GET_PRODUCT_BY_PRODUCT_ID");
		apiLog.setApiNote("네이버-상품조회");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.NAVER.code());
		apiLog.setProcessId(PaGroup.NAVER.processId());
		apiLog.setApiUrl(SERVICE_NAME + "#" + operationName);
		
		try {

			GetProductRequestType request = new GetProductRequestType();
			request = (GetProductRequestType) apiRequest.createRequest(request, SERVICE_NAME, operationName);

			request.setSellerId(MALL_ID);
			request.setProductId(Long.parseLong(productId));

			transLogService.logTransApiReqSoap(apiLog, request);

			ProductServicePortType productService = (new ProductService()).getProductServiceSOAP12PortHttp();

			// SOAP 타임아웃 설정
			Map<String, Object> requestContext = ((BindingProvider)productService).getRequestContext();
			requestContext.put("com.sun.xml.internal.ws.connect.timeout", CONNECT_TIMEOUT);
			requestContext.put("com.sun.xml.internal.ws.request.timeout", READ_TIMEOUT);

			GetProductResponseType response = productService.getProduct(request);

			apiLog.setResponsePayload(StringUtil.objectToXml(response));
			apiLog.setResultCode(response.getResponseType());

			apiLog.setSuccessYn(PaNaverApiRequest.API_SUCCESS_CODE.equals(apiLog.getResultCode()) ? "1" : "0");

			if (response.getError() != null) {
				apiLog.setResultCode(response.getError().getCode());
				apiLog.setResultMsg(response.getError().getMessage() + "-" + response.getError().getDetail());
			}

			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return response.getProduct();
		} catch (TransApiException ex) {
			log.error("상품조회 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("상품조회 goodsCode={}", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
	

	/**
	 * 옵션등록/수정API 호출
	 * 
	 * @param goodsCode
	 * @param option
	 * @param transServiceNo
	 * @return
	 */
	public String manageOption(String goodsCode, OptionType option, long transServiceNo) {

		String operationName = MANAGE_OPTION;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("MANAGE_OPTION");
		apiLog.setApiNote("네이버-옵션등록/수정");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.NAVER.code());
		apiLog.setProcessId(PaGroup.NAVER.processId());
		apiLog.setApiUrl(SERVICE_NAME + "#" + operationName);
		
		try {
			ManageOptionRequestType request = new ManageOptionRequestType();
			request = (ManageOptionRequestType) apiRequest.createRequest(request, SERVICE_NAME, operationName);

			request.setSellerId(MALL_ID);
			request.setOption(option);
			
			transLogService.logTransApiReqSoap(apiLog, request);

			ProductServicePortType productService = (new ProductService()).getProductServiceSOAP12PortHttp();
			
			// SOAP 타임아웃 설정
			Map<String, Object> requestContext = ((BindingProvider)productService).getRequestContext();
			requestContext.put("com.sun.xml.internal.ws.connect.timeout", CONNECT_TIMEOUT);
			requestContext.put("com.sun.xml.internal.ws.request.timeout", READ_TIMEOUT);

			ManageOptionResponseType response = productService.manageOption(request);

			apiLog.setResponsePayload(StringUtil.objectToXml(response));
			apiLog.setResultCode(response.getResponseType());

			apiLog.setSuccessYn(PaNaverApiRequest.API_SUCCESS_CODE.equals(apiLog.getResultCode()) ? "1" : "0");

			if (response.getError() != null) {
				apiLog.setResultCode(response.getError().getCode());
				apiLog.setResultMsg(response.getError().getMessage() + "-" + response.getError().getDetail());
			}

			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			return String.valueOf(response.getProductId());
		} catch (TransApiException ex) {
			log.error("옵션등록/수정 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("옵션등록/수정 goodsCode={}", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}

	/**
	 * 옵션 조회
	 * 
	 * @param goodsCode
	 * @param productId
	 * @param transServiceNo
	 * @return
	 */
	public OptionType getOption(String goodsCode, String productId, long transServiceNo) {

		String operationName = GET_OPTION_BY_PRODUCT_ID;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("GET_OPTION_BY_PRODUCT_ID");
		apiLog.setApiNote("네이버-옵션조회");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.NAVER.code());
		apiLog.setProcessId(PaGroup.NAVER.processId());
		apiLog.setApiUrl(SERVICE_NAME + "#" + operationName);
		
		try {

			GetOptionRequestType request = new GetOptionRequestType();
			request = (GetOptionRequestType) apiRequest.createRequest(request, SERVICE_NAME, operationName);

			request.setSellerId(MALL_ID);
			request.setProductId(Long.parseLong(productId));

			transLogService.logTransApiReqSoap(apiLog, request);

			ProductServicePortType productService = (new ProductService()).getProductServiceSOAP12PortHttp();
			
			// SOAP 타임아웃 설정
			Map<String, Object> requestContext = ((BindingProvider)productService).getRequestContext();
			requestContext.put("com.sun.xml.internal.ws.connect.timeout", CONNECT_TIMEOUT);
			requestContext.put("com.sun.xml.internal.ws.request.timeout", READ_TIMEOUT);

			GetOptionResponseType response = productService.getOption(request);

			apiLog.setResponsePayload(StringUtil.objectToXml(response));
			apiLog.setResultCode(response.getResponseType());

			apiLog.setSuccessYn(PaNaverApiRequest.API_SUCCESS_CODE.equals(apiLog.getResultCode()) ? "1" : "0");

			if (response.getError() != null) {
				apiLog.setResultCode(response.getError().getCode());
				apiLog.setResultMsg(response.getError().getMessage() + "-" + response.getError().getDetail());
			}

			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			return response.getOption();
		} catch (TransApiException ex) {
			log.error("옵션조회 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("옵션조회 goodsCode={}", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
	
	/**
	 * 이미지등록
	 * 
	 * @param goodsCode
	 * @param imageUrl
	 * @param transServiceNo
	 * @return
	 */
	public String registerImage(String goodsCode, String imageUrl, long transServiceNo) {

		String serviceName = "ImageService";
		String operationName = CREATE_IMAGE;

		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(goodsCode);
		apiLog.setTransType(TransType.PRODUCT.name());
		apiLog.setApiName("CREATE_IMAGE");
		apiLog.setApiNote("네이버-이미지등록");
		apiLog.setTransServiceNo(transServiceNo);
		apiLog.setPaGroupCode(PaGroup.NAVER.code());
		apiLog.setProcessId(PaGroup.NAVER.processId());
		apiLog.setApiUrl(serviceName + "#" + operationName);

		ImageURLListType imageURLList = new ImageURLListType();
		imageURLList.getURL().add(imageUrl); 
		
		try {
			UploadImageRequestType request = new UploadImageRequestType();
			request = (UploadImageRequestType) apiRequest.createRequest(request, serviceName, operationName);

			request.setSellerId(MALL_ID);
			request.setImageURLList(imageURLList);
			
			transLogService.logTransApiReqSoap(apiLog, request);

			ImageServicePortType imgetService = (new ImageService()).getImageServiceSOAP12PortHttp();
			
			// SOAP 타임아웃 설정
			Map<String, Object> requestContext = ((BindingProvider)imgetService).getRequestContext();
			requestContext.put("com.sun.xml.internal.ws.connect.timeout", CONNECT_TIMEOUT);
			requestContext.put("com.sun.xml.internal.ws.request.timeout", READ_TIMEOUT);

			UploadImageResponseType response = imgetService.uploadImage(request);

			apiLog.setResponsePayload(StringUtil.objectToXml(response));
			apiLog.setResultCode(response.getResponseType());

			apiLog.setSuccessYn(PaNaverApiRequest.API_SUCCESS_CODE.equals(apiLog.getResultCode()) ? "1" : "0");

			if (response.getError() != null) {
				apiLog.setResultCode(response.getError().getCode());
				apiLog.setResultMsg(response.getError().getMessage() + "-" + response.getError().getDetail());
			}

			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
			
			return response.getImageList().getImage().get(0).getURL();
		} catch (TransApiException ex) {
			log.error("이미지등록 goodsCode={} {}", goodsCode, apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error("이미지등록 goodsCode={}", goodsCode, e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
	
}
