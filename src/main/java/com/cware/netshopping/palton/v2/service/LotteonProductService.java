package com.cware.netshopping.palton.v2.service;

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
 * 롯데온 데몬의 상품 API호출
 *
 */
@Service
public class LotteonProductService {

	// 롯데온 데몬서버
	@Value("${partner.lotteon.service.host}")
	String API_SERVER;

	// 판매중지 API 
	@Value("${partner.lotteon.service.product.stop}")
	String PRODUCT_STOP_API;

	// 판매재개 API 
	@Value("${partner.lotteon.service.product.resume}")
	String PRODUCT_RESUME_API;

	// 단품판매재개 API 
	@Value("${partner.lotteon.service.product.resume-item}")
	String PRODUCT_RESUME_ITEM_API;

	// 재고변경 API 
	@Value("${partner.lotteon.service.product.stock}")
	String PRODUCT_STOCK_API;

	// 상태업데이트 API 
	@Value("${partner.lotteon.service.product.status}")
	String PRODUCT_STATUS_API;

	// 옵션코드매핑 API 
	@Value("${partner.lotteon.service.product.mapping}")
	String PRODUCT_MAPPING_API;
	
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
	public PaTransService stopSaleProduct(String goodsCode, String paCode, String procId) {
		String tag = "판매중지";

		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsCode", goodsCode);
		params.put("paCode", paCode);
		params.put("procId", procId);

		return requestUpdateProduct(goodsCode, tag, PRODUCT_STOP_API, params);
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
	 * 단품판매재개 요청
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public PaTransService resumeSaleItem(String goodsCode, String paCode, String procId) {
		String tag = "단품판매재개";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsCode", goodsCode);
		params.put("paCode", paCode);
		params.put("procId", procId);

		return requestUpdateProduct(goodsCode, tag, PRODUCT_RESUME_ITEM_API, params);
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
	 * 상품상태 업데이트 요청
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public PaTransService updateStatusProduct(String goodsCode, String paCode, String procId) {
		String tag = "상태업데이트";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsCode", goodsCode);
		params.put("paCode", paCode);
		params.put("procId", procId);

		return requestUpdateProduct(goodsCode, tag, PRODUCT_STATUS_API, params);
	}

	/**
	 * 옵션코드매핑 요청
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public PaTransService updateOptionProduct(String goodsCode, String spdNo, String paCode, String procId) {
		String tag = "옵션코드매핑";

		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsCode", goodsCode);
		params.put("spdNo", spdNo);
		params.put("paCode", paCode);
		params.put("procId", procId);

		return requestUpdateProduct(goodsCode, tag, PRODUCT_MAPPING_API, params);
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
