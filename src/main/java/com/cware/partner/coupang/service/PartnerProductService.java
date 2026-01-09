package com.cware.partner.coupang.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.cware.partner.common.code.Application;
import com.cware.partner.common.code.PaGroup;
import com.cware.partner.common.domain.ProductTrans;
import com.cware.partner.common.domain.ResponseMsg;
import com.cware.partner.common.service.CodeService;
import com.cware.partner.common.service.CommonService;
import com.cware.partner.common.service.TransLogService;
import com.cware.partner.common.util.StringUtil;
import com.cware.partner.coupang.repository.PaCopnGoodsRepository;
import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.PaGoods;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;
import com.cware.partner.sync.domain.id.PaGoodsId;
import com.cware.partner.sync.filter.CoupangFilter;
import com.cware.partner.sync.filter.GoodsFilter;
import com.cware.partner.sync.filter.GoodsPriceFilter;
import com.cware.partner.sync.filter.PartnerFilter;
import com.cware.partner.sync.repository.GoodsPriceRepository;
import com.cware.partner.sync.repository.GoodsRepository;
import com.cware.partner.sync.repository.PaCustShipCostRepository;
import com.cware.partner.sync.repository.PaGoodsTargetRepository;
import com.cware.partner.sync.repository.PaTargetExceptRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 쿠팡 제휴상품 연동 (파트너API호출)
 */
@Slf4j
@Service
public class PartnerProductService {

	@Autowired
	PaCopnGoodsRepository copnGoodsRepository;

	@Autowired
	GoodsRepository goodsRepository;
	@Autowired
	GoodsPriceRepository goodsPriceRepository;
	@Autowired
	PaCustShipCostRepository shipCostRepository;
	@Autowired
	PaTargetExceptRepository paTargetExceptRepository;
	@Autowired
	PaGoodsTargetRepository paGoodsTargetRepository;

	@Autowired
	GoodsFilter goodsFilter;
	@Autowired
	GoodsPriceFilter goodsPriceFilter;
	@Autowired
	PartnerFilter partnerFilter;
	@Autowired
	CoupangFilter coupangFilter;

	@Autowired
	CommonService commonService;
	@Autowired
	CodeService codeService;
	@Autowired
	TransLogService transLogService;

	@Autowired
	RestTemplate restTemplate;

	// API서버
	@Value("${partner.coupang.api.host}")
	String API_SERVER;

	// 제휴사 상품등록 API URL
	@Value("${partner.coupang.api.product.register}")
	String PRODUCT_REGISTER_API;

	// 제휴사 상품수정 API URL
	@Value("${partner.coupang.api.product.update}")
	String PRODUCT_UPDATE_API;

	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public CompletableFuture<ProductTrans> asyncRegisterProduct(String goodsCode, String paCode, long transBatchNo) {
		Integer result = registerProduct(goodsCode, paCode, transBatchNo);
		ProductTrans trans = ProductTrans.builder().goodsCode(goodsCode).paCode(paCode).procCnt(result).build();
		return CompletableFuture.completedFuture(trans);
	}

	@Async
	public CompletableFuture<ProductTrans> asyncUpdateProduct(String goodsCode, String paCode, long transBatchNo) {
		Integer result = updateProduct(goodsCode, paCode, transBatchNo);
		ProductTrans trans = ProductTrans.builder().goodsCode(goodsCode).paCode(paCode).procCnt(result).build();
		return CompletableFuture.completedFuture(trans);
	}

	// 신규 상품 입점
	@Transactional
	public int registerProduct(String goodsCode, String paCode, long transBatchNo) {

		// 상품조회
		Goods goods = goodsRepository.getById(goodsCode);

		log.info("상품 정보: {} {} ", goods.getGoodsCode(), goods.getGoodsName());

		// 배송비조회
		goods.setPaCustShipCost(shipCostRepository.getCustShipCost(goods.getShipEntpCode(), goods.getShipCostCode()));

		PaGoodsTarget target = paGoodsTargetRepository
				.getById(new PaGoodsId(PaGroup.COUPANG.code(), paCode, goodsCode));

		target.setPartnerBase(codeService.getPartnerBase(target.getPaCode()));
		target.setGoodsSyncNo(goods.getGoodsSyncNo());
		
		Timestamp procDate = commonService.currentDate();
		PaGoods paGoods = new PaGoods();
		
		paGoods.setLastSyncDate(procDate);
		goods.setPaGoods(paGoods);
		
		// 필터 수행하여 반려 처리
		if (!applyFilter(goods, target)) {
			String filterNote = Optional.ofNullable(goods.getExceptNote()).orElse(target.getExceptNote());
			copnGoodsRepository.rejectTransTarget(goodsCode, paCode, Application.ID.code(),
					Optional.ofNullable(goods.getExceptNote()).orElse(target.getExceptNote()));
			log.info("필터 처리 ====> {} {}", target.getGoodsCode(), filterNote);
			return 0;
		}

		UriTemplate template = new UriTemplate(PRODUCT_REGISTER_API);
		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsCode", target.getGoodsCode());
		params.put("paCode", target.getPaCode());
		params.put("procId", Application.ID.code());
		params.put("transBatchNo", String.valueOf(transBatchNo));

		String uri = template.expand(params).toString();

		log.info("입점 요청 ====> {} ", uri);
//		ResponseEntity<ResponseMsg> response = restTemplate.exchange(API_SERVER + uri, HttpMethod.GET, null,
//				ResponseMsg.class);
		ResponseEntity<ResponseMsg> response = restTemplate.getForEntity(API_SERVER + PRODUCT_REGISTER_API,
				ResponseMsg.class, params);
		ResponseMsg result = response.getBody();

		// 성공이 아니면 에러로 로깅
		if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
			log.error("입점 실패 ====> {} {}", target.getGoodsCode(), result);

			switch (result.getCode()) {
			case "404": // 입점 요청전에 쿼리 대상 등에서 누락된 경우로 사후에 처리하도록 반려처리후 "처리할자료가 없습니다" 메시지 남김
				copnGoodsRepository.rejectTransTarget(goodsCode, paCode, Application.ID.code(), result.getMessage());
				log.info("입점조건안됨: {} {}", target.getGoodsCode(), result.getMessage());
				break;
			case "490":
				log.info("동일배치수행: {} {}", target.getGoodsCode(), result.getMessage());
				break;
			case "440": // 업체주소연동 오류일때 반려처리
				copnGoodsRepository.rejectTransTarget(goodsCode, paCode, Application.ID.code(), result.getMessage());
				log.info("업체주소오류 반려처리 {}", target.getGoodsCode());
				break;
			default:// API요청은 성공했으나 처리중 혹은 제휴사API에서 기타 메시지 리턴한 경우는 일단 모두 반려처리
				copnGoodsRepository.rejectTransTarget(goodsCode, paCode, Application.ID.code(),
						StringUtil.truncate(result.getMessage(), 500));
				log.info("반려처리 {}: {} {}", target.getGoodsCode(), result.getCode(), result.getMessage());
				break;
			}
			return -1;
		}

		log.info("입점 결과 ====> {} {}", target.getGoodsCode(), result);

		return 1;
	}

	// 상품수정
	public int updateProduct(String goodsCode, String paCode, long transBatchNo) {

		UriTemplate template = new UriTemplate(PRODUCT_UPDATE_API);
		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsCode", goodsCode);
		params.put("paCode", paCode);
		params.put("procId", Application.ID.code());
		params.put("transBatchNo", String.valueOf(transBatchNo));

		String uri = template.expand(params).toString();

		log.info("수정 요청 ====> {} ", uri);
//		ResponseEntity<ResponseMsg> response = restTemplate.exchange(API_SERVER + uri, HttpMethod.GET, null,
//				ResponseMsg.class);
		ResponseEntity<ResponseMsg> response = restTemplate.getForEntity(API_SERVER + PRODUCT_UPDATE_API,
				ResponseMsg.class, params);
		ResponseMsg result = response.getBody();

		// 성공이 아니면 에러로 로깅
		if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
			log.error("수정 실패 ====> {} {}", goodsCode, result);
			if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) return 0;
//			if (String.valueOf(HttpStatus.BAD_REQUEST.value()).equals(result.getCode())) {
//				// 수기중단처리
//				copnGoodsRepository.stopSaleForReject(goodsCode, paCode, Application.ID.code(),
//						StringUtil.truncate(result.getMessage(), 500));
//				log.info("수기중단 {}: {} {}", goodsCode, result.getCode(), result.getMessage());
//			}
			return -1;
		}

		log.info("수정 결과 ====> {} {}", goodsCode, result);

		return 1;
	}

	public boolean applyFilter(Goods goods, PaGoodsTarget target) {

		// 제휴연동 제외
		goods.setTargetExcept(paTargetExceptRepository.findTargetExcept(goods.getGoodsCode(), goods.getEntpCode(),
				goods.getBrandCode(), goods.getSourcingMedia()));
		if (!goodsFilter.apply(goods))
			return false;

		// 상품가격조회
		goods.setGoodsPrice(goodsPriceRepository.findApplyGoodsPrice(goods.getGoodsCode()));
		if (!goodsPriceFilter.apply(goods))
			return false;

		// 제외처리 등 제휴사별 필터
		if (!partnerFilter.apply(goods, target))
			return false;
		if (!coupangFilter.apply(goods, target))
			return false;

		return true;
	}

}
