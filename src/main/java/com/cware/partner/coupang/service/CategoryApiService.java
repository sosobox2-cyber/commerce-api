package com.cware.partner.coupang.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.cware.partner.common.code.PaCode;
import com.cware.partner.coupang.domain.Category;
import com.cware.partner.coupang.domain.CategoryMsg;

import lombok.extern.slf4j.Slf4j;

/**
 * 쿠팡 카테고리 API 호출
 * https://developers.coupangcorp.com/hc/ko/sections/360005046514-%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC-APIs
 */
@Slf4j
@Service
public class CategoryApiService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApiRequest apiRequest;

	// 카테고리 메타정보 조회 GET
	// 노출 카테고리코드(displayCategoryCode)를 이용하여 해당 카테고리에 속한 고시정보, 옵션, 구비서류, 인증정보 목록 등을 조회
	// 상품 생성 시, 쿠팡에서 규정하고 있는 각 카테고리의 메타 정보와 일치하는 항목으로 상품 생성 전문을 구성해야 함
	// 노출 카테고리코드 조회 방법: https://developers.coupangcorp.com/hc/ko/articles/360023110213
	private static final String GET_CATEGORY_META = "/v2/providers/seller_api/apis/api/v1/marketplace/meta/category-related-metas/display-category-codes/{displayCategoryCode}";

	// 카테고리 추천 POST
	// 상품정보(상품명, 브랜드, 속성 등)를 입력하시면, 해당 정보와 가장 일치하는 쿠팡 카테고리(displayCategoryCode)를 찾아서 제안해주는 서비스
	// 쿠팡은 카테고리에 따른 책정 수수료가 상이하므로, 카테고리 선택 시 이점 유의
	// 최대한 상품 특성이 잘 나타날 수 있도록 상품명에 상품 정보를 상세하게 입력
	// 하나의 상품명에 서로 다른 타입의 상품이 들어가지 않도록 주의
	// 딜 형식으로 상품명을 등록하지 않도록 주의
//	private static final String GET_PRODUCT_AUTO_CATEGORY = "/v2/providers/openapi/apis/api/v1/categorization/predict";

	// 카테고리 자동 매칭 서비스 동의 확인 GET
	// 판매자ID가 현재 카테고리 자동매칭 서비스에 동의했는지 체크
	// ※ 카테고리 자동매칭 서비스 동의 경로
	// 판매관리시스템(WING) 로그인 > 우측상단 업체명 클릭 > 추가판매정보 클릭
//	private static final String GET_AUTO_CATEGORY_AGREED = "/v2/providers/seller_api/apis/api/v1/marketplace/vendors/{vendorId}/check-auto-category-agreed";

	// 카테고리 목록조회 GET
	// 노출 카테고리 목록 전체를 조회
	// 전체 카테고리 정보 Excel file 다운로드: https://wing.coupang.com/excel/categories/download/file
//	private static final String GET_DISPLAY_CATEGORIES = "/v2/providers/seller_api/apis/api/v1/marketplace/meta/display-categories";

	// 카테고리 조회 GET
	// 카테고리 정보를 노출 카테고리 코드(displayCategoryCode)를 이용하여 조회
	// 1 Depth 카테고리 정보 조회는 노출카테고리코드 값을 0으로 설정 후 호출
//	private static final String GET_DISPLAY_CATEGORY = "/v2/providers/seller_api/apis/api/v1/marketplace/meta/display-categories/{displayCategoryCode}";

	// 카테고리 유효성 검사 GET
	// 해당 노출 카테고리가 현재 사용 가능한지 체크
	// 카테고리 리뉴얼 등으로 인해, 사용 중인 카테고리가 사용하지 않는 카테고리로 변경될 수 있음.
	// 단, 카테고리 리뉴얼은 연 2회 이루어지며 리뉴얼로 인해 변경된 카테고리에 대하여 판매자에게 공지되기 때문에 수시로 카테고리 유효성 검사를 할 필요 없음.
//	private static final String GET_DISPLAY_CATEGORY_STATUS = "/v2/providers/seller_api/apis/api/v1/marketplace/meta/display-categories/{displayCategoryCode}/status";

	/**
	 * 카테고리 메타정보 조회
	 *
	 * @param displayCategoryCode 노출카테고리코드
	 * @return
	 */
	public Category getCategoryMeta(String displayCategoryCode) {

		UriTemplate template = new UriTemplate(GET_CATEGORY_META);
		Map<String, String> params = new HashMap<String, String>();
		params.put("displayCategoryCode", displayCategoryCode);

		String uri = template.expand(params).toString();

		HttpMethod method = HttpMethod.GET;
		String authorization = apiRequest.generateSignature(method.name(), uri, PaCode.COUPANG_ONLINE.code());

		try {
			ResponseEntity<CategoryMsg> response = restTemplate
					.exchange(apiRequest.createRequest(method, uri, authorization), CategoryMsg.class);

			CategoryMsg result = response.getBody();

			Category category = result.getData();

			return category;

		} catch (Exception ex) {
			log.error("displayCategoryCode={}", displayCategoryCode, ex);
		}
		return null;

	}
}
