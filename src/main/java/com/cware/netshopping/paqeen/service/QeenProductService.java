package com.cware.netshopping.paqeen.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.cware.netshopping.common.log.domain.PaTransService;


/**
 * 퀸잇 데몬의 상품 API호출
 *
 */
@Service
public class QeenProductService {

	// 퀸잇 데몬서버
	@Value("${partner.qeen.service.host}")
	String API_SERVER;

	// 판매중지 API 
	@Value("${partner.qeen.service.product.stop}")
	String PRODUCT_STOP_API;

	// 재고변경 API
	@Value("${partner.qeen.service.product.stock}")
	String PRODUCT_STOCK_API;

	// 판매재개 API
	@Value("${partner.qeen.service.product.resume}")
	String PRODUCT_RESUME_API;

	@Autowired
	private RestTemplate restTemplate;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 판매중지 요청
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public PaTransService stopSaleProduct(String goodsCode, String paCode,	String procId) {
		String tag = "판매중지";

		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsCode", goodsCode);
		params.put("paCode", paCode);
		params.put("procId", procId);

		return requestUpdateProduct(goodsCode, tag, PRODUCT_STOP_API, params);
	}

	/**
	 * 재고변경 요청
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public PaTransService updateStockProduct(String goodsCode, String paCode, String procId) {
		String tag = "재고변경";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsCode", goodsCode);
		params.put("paCode", paCode);
		params.put("procId", procId);

		return requestUpdateProduct(goodsCode, tag, PRODUCT_STOCK_API, params);
	}
	
	/**
	 * 판매재개 요청
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */	
	public PaTransService resumeSaleProduct(String goodsCode, String paCode, String procId) {
		String tag = "판매재개";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsCode", goodsCode);
		params.put("paCode", paCode);
		params.put("procId", procId);

		return requestUpdateProduct(goodsCode, tag, PRODUCT_RESUME_API, params);
	}
	/**
	 * 상품 업데이트 요청
	 * 
	 * @param goodsCode
	 * @param tag
	 * @param apiUrl
	 * @param params
	 * @return
	 */
	private PaTransService requestUpdateProduct(String goodsCode, String tag, String apiUrl, Map<String, String> params) {
		UriTemplate template = new UriTemplate(apiUrl);
		String uri = template.expand(params).toString();

		log.info("{} 요청 ====> {} ", tag, uri);
		ResponseEntity<PaTransService> response = restTemplate.exchange(API_SERVER + uri, HttpMethod.PUT, null,
				PaTransService.class);
		PaTransService result = response.getBody();

		// 200, 404인 경우 성공으로 처리
		result.setSuccessYn(String.valueOf(HttpStatus.OK.value()).equals(result.getResultCode())
				|| String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getResultCode()) ? "1" : "0");
		
		// 성공이 아니면 에러로 로깅
		if ("0".equals(result.getSuccessYn())) {
			log.error("{} 실패 ====> {} {}", tag, goodsCode, result);
		} else {
			log.info("{} 결과 ====> {} {}", tag, goodsCode, result);
		}

		return result;
	}

	
}
