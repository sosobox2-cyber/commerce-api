package com.cware.partner.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.partner.common.code.CodeGroup;
import com.cware.partner.common.code.PaCode;
import com.cware.partner.common.code.PaGroup;
import com.cware.partner.common.domain.PartnerBase;
import com.cware.partner.common.domain.entity.Code;
import com.cware.partner.common.domain.entity.Config;
import com.cware.partner.common.repository.CodeRepository;
import com.cware.partner.common.repository.ConfigRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CodeService {

	@Autowired
	private CodeRepository codeRepository;

	@Autowired
	private ConfigRepository configRepository;

	// 제조사코드/명
	private Map<String, String> originMap;

	// 제휴사 기준정보
	private Map<String, PartnerBase> partnerMap;

	// 쿠팡승인상태명/코드
	private Map<String, String> approvalStatusMap;

	// 프로모션 최소마진율
	private Double promoMinMarginRate;

	// 행사상품 최소마진율
	private Double eventMinMarginRate;

	@PostConstruct
	public void loadCodeList() throws Exception {
		loadOriginCode();
		loadPartnerBase();
		loadSaleCondition();
		loadStockRate();
		loadCommision();
		loadApprovalStatus();
		loadPromoMinMarginRate();
		loadEventMinMarginRate();
	}

	public void loadOriginCode() throws Exception {
		List<Code> list = codeRepository.findByCodeLgroup(CodeGroup.ORIGIN.code());

		originMap = new HashMap<String, String>();

		for (Code code : list) {
			originMap.put(code.getCodeMgroup(), code.getCodeName());
		}
	}

	// 제휴사별 기준정보 생성
	public void loadPartnerBase() {
		partnerMap = new HashMap<String, PartnerBase>();

		partnerMap.put(PaCode.SK11ST_TV.code(), new PartnerBase(PaCode.SK11ST_TV.code()));
		partnerMap.put(PaCode.SK11ST_ONLINE.code(), new PartnerBase(PaCode.SK11ST_ONLINE.code()));

		partnerMap.put(PaCode.EBAY_TV.code(), new PartnerBase(PaCode.EBAY_TV.code()));
		partnerMap.put(PaCode.EBAY_ONLINE.code(), new PartnerBase(PaCode.EBAY_ONLINE.code()));

		partnerMap.put(PaCode.NAVER.code(), new PartnerBase(PaCode.NAVER.code()));

		partnerMap.put(PaCode.COUPANG_TV.code(), new PartnerBase(PaCode.COUPANG_TV.code()));
		partnerMap.put(PaCode.COUPANG_ONLINE.code(), new PartnerBase(PaCode.COUPANG_ONLINE.code()));

		partnerMap.put(PaCode.WEMP_TV.code(), new PartnerBase(PaCode.WEMP_TV.code()));
		partnerMap.put(PaCode.WEMP_ONLINE.code(), new PartnerBase(PaCode.WEMP_ONLINE.code()));

		partnerMap.put(PaCode.INTERPARK_TV.code(), new PartnerBase(PaCode.INTERPARK_TV.code()));
		partnerMap.put(PaCode.INTERPARK_ONLINE.code(), new PartnerBase(PaCode.INTERPARK_ONLINE.code()));

		partnerMap.put(PaCode.LOTTEON_TV.code(), new PartnerBase(PaCode.LOTTEON_TV.code()));
		partnerMap.put(PaCode.LOTTEON_ONLINE.code(), new PartnerBase(PaCode.LOTTEON_ONLINE.code()));

		partnerMap.put(PaCode.TMON_TV.code(), new PartnerBase(PaCode.TMON_TV.code()));
		partnerMap.put(PaCode.TMON_ONLINE.code(), new PartnerBase(PaCode.TMON_ONLINE.code()));

		partnerMap.put(PaCode.SSG_TV.code(), new PartnerBase(PaCode.SSG_TV.code()));
		partnerMap.put(PaCode.SSG_ONLINE.code(), new PartnerBase(PaCode.SSG_ONLINE.code()));

		partnerMap.put(PaCode.KAKAO_TV.code(), new PartnerBase(PaCode.KAKAO_TV.code()));
		partnerMap.put(PaCode.KAKAO_ONLINE.code(), new PartnerBase(PaCode.KAKAO_ONLINE.code()));

		partnerMap.put(PaCode.HALF_TV.code(), new PartnerBase(PaCode.HALF_TV.code()));
		partnerMap.put(PaCode.HALF_ONLINE.code(), new PartnerBase(PaCode.HALF_ONLINE.code()));

		partnerMap.put(PaCode.TDEAL_TV.code(), new PartnerBase(PaCode.TDEAL_TV.code()));
		partnerMap.put(PaCode.TDEAL_ONLINE.code(), new PartnerBase(PaCode.TDEAL_ONLINE.code()));
	}

	// 제휴사별 최소마진/최저가격 설정
	public void loadSaleCondition() {
		if (partnerMap == null) {
			loadPartnerBase();
		}

		List<Code> list = codeRepository.findByCodeLgroup(CodeGroup.SALE_COND.code());

		for (Code code : list) {

			// 최소마진율, 최저판매가 설정
			switch (code.getCodeMgroup()) {
			case "10": // 11번가 최소마진율
				partnerMap.get(PaCode.SK11ST_TV.code()).setMinMarginRate(new Double(code.getRemark()));
				partnerMap.get(PaCode.SK11ST_ONLINE.code()).setMinMarginRate(new Double(code.getRemark1()));
				break;
			case "20": // 11번가 최저판매가
				partnerMap.get(PaCode.SK11ST_TV.code()).setMinSalePrice(new Double(code.getRemark()));
				partnerMap.get(PaCode.SK11ST_ONLINE.code()).setMinSalePrice(new Double(code.getRemark1()));
				break;
			case "30": // 이베이 최소마진율
				partnerMap.get(PaCode.EBAY_TV.code()).setMinMarginRate(new Double(code.getRemark()));
				partnerMap.get(PaCode.EBAY_ONLINE.code()).setMinMarginRate(new Double(code.getRemark1()));
				break;
			case "40": // 이베이 최저판매가
				partnerMap.get(PaCode.EBAY_TV.code()).setMinSalePrice(new Double(code.getRemark()));
				partnerMap.get(PaCode.EBAY_ONLINE.code()).setMinSalePrice(new Double(code.getRemark1()));
				break;
			case "50": // 네이버 최소마진율
				partnerMap.get(PaCode.NAVER.code()).setMinMarginRate(new Double(code.getRemark()));
				break;
			case "51": // 네이버 최저판매가
				partnerMap.get(PaCode.NAVER.code()).setMinSalePrice(new Double(code.getRemark()));
				break;
			case "60": // 쿠팡 최소마진율
				partnerMap.get(PaCode.COUPANG_TV.code()).setMinMarginRate(new Double(code.getRemark()));
				partnerMap.get(PaCode.COUPANG_ONLINE.code()).setMinMarginRate(new Double(code.getRemark1()));
				break;
			case "61": // 쿠팡 최저판매가
				partnerMap.get(PaCode.COUPANG_TV.code()).setMinSalePrice(new Double(code.getRemark()));
				partnerMap.get(PaCode.COUPANG_ONLINE.code()).setMinSalePrice(new Double(code.getRemark1()));
				break;
			case "70": // 위메프 최소마진율
				partnerMap.get(PaCode.WEMP_TV.code()).setMinMarginRate(new Double(code.getRemark()));
				partnerMap.get(PaCode.WEMP_ONLINE.code()).setMinSalePrice(new Double(code.getRemark1()));
				break;
			case "71": // 위메프 최저판매가
				partnerMap.get(PaCode.WEMP_TV.code()).setMinSalePrice(new Double(code.getRemark()));
				partnerMap.get(PaCode.WEMP_ONLINE.code()).setMinSalePrice(new Double(code.getRemark1()));
				break;
			case "80": // 인터파크 최소마진율
				partnerMap.get(PaCode.INTERPARK_TV.code()).setMinMarginRate(new Double(code.getRemark()));
				partnerMap.get(PaCode.INTERPARK_ONLINE.code()).setMinMarginRate(new Double(code.getRemark1()));
				break;
			case "81": // 인터파크 최저판매가
				partnerMap.get(PaCode.INTERPARK_TV.code()).setMinSalePrice(new Double(code.getRemark()));
				partnerMap.get(PaCode.INTERPARK_ONLINE.code()).setMinSalePrice(new Double(code.getRemark1()));
				break;
			case "82": // 롯데온 최소마진율
				partnerMap.get(PaCode.LOTTEON_TV.code()).setMinMarginRate(new Double(code.getRemark()));
				partnerMap.get(PaCode.LOTTEON_ONLINE.code()).setMinMarginRate(new Double(code.getRemark1()));
				break;
			case "83": // 롯데온 최저판매가
				partnerMap.get(PaCode.LOTTEON_TV.code()).setMinSalePrice(new Double(code.getRemark()));
				partnerMap.get(PaCode.LOTTEON_ONLINE.code()).setMinSalePrice(new Double(code.getRemark1()));
				break;
			case "84": // 티몬 최소마진율
				partnerMap.get(PaCode.TMON_TV.code()).setMinMarginRate(new Double(code.getRemark()));
				partnerMap.get(PaCode.TMON_ONLINE.code()).setMinMarginRate(new Double(code.getRemark1()));
				break;
			case "85": // 티몬 최저판매가
				partnerMap.get(PaCode.TMON_TV.code()).setMinSalePrice(new Double(code.getRemark()));
				partnerMap.get(PaCode.TMON_ONLINE.code()).setMinSalePrice(new Double(code.getRemark1()));
				break;
			case "86": // 쓱 최소마진율
				partnerMap.get(PaCode.SSG_TV.code()).setMinMarginRate(new Double(code.getRemark()));
				partnerMap.get(PaCode.SSG_ONLINE.code()).setMinMarginRate(new Double(code.getRemark1()));
				break;
			case "87": // 쓱 최저판매가
				partnerMap.get(PaCode.SSG_TV.code()).setMinSalePrice(new Double(code.getRemark()));
				partnerMap.get(PaCode.SSG_ONLINE.code()).setMinSalePrice(new Double(code.getRemark1()));
				break;
			case "88": // 카카오쇼핑 최소마진율
				partnerMap.get(PaCode.KAKAO_TV.code()).setMinMarginRate(new Double(code.getRemark()));
				partnerMap.get(PaCode.KAKAO_ONLINE.code()).setMinMarginRate(new Double(code.getRemark1()));
				break;
			case "89": // 카카오쇼핑 최저판매가
				partnerMap.get(PaCode.KAKAO_TV.code()).setMinSalePrice(new Double(code.getRemark()));
				partnerMap.get(PaCode.KAKAO_ONLINE.code()).setMinSalePrice(new Double(code.getRemark1()));
				break;
			case "90": // 하프클럽 최소마진율
				partnerMap.get(PaCode.HALF_TV.code()).setMinMarginRate(new Double(code.getRemark()));
				partnerMap.get(PaCode.HALF_ONLINE.code()).setMinMarginRate(new Double(code.getRemark1()));
				break;
			case "91": // 하프클럽 최저판매가
				partnerMap.get(PaCode.HALF_TV.code()).setMinSalePrice(new Double(code.getRemark()));
				partnerMap.get(PaCode.HALF_ONLINE.code()).setMinSalePrice(new Double(code.getRemark1()));
				break;
			case "92": // 티딜 최소마진율
				partnerMap.get(PaCode.TDEAL_TV.code()).setMinMarginRate(new Double(code.getRemark()));
				partnerMap.get(PaCode.TDEAL_ONLINE.code()).setMinMarginRate(new Double(code.getRemark()));
				break;
			case "93": // 티딜 최저판매가
				partnerMap.get(PaCode.TDEAL_TV.code()).setMinSalePrice(new Double(code.getRemark()));
				partnerMap.get(PaCode.TDEAL_ONLINE.code()).setMinSalePrice(new Double(code.getRemark()));
				break;

			default:
				break;
			}
		}
		log.info("제휴사별 최소마진/최소가격 로딩 {}", partnerMap);
	}

	// 제휴사별 수수료 정보 설정
	// 적용일별로 수수료가 결정되어 예약설정을 해야한다면 매일 00시에 스케줄러로 로딩해야함.
	public void loadCommision() {

		if (partnerMap == null) {
			loadPartnerBase();
		}

		Code code = codeRepository.findCommisionByCodeLgroup(PaGroup.SK11ST.commissionGroup());
		partnerMap.get(PaCode.SK11ST_TV.code()).setCommission(new Double(code.getRemark()));
		partnerMap.get(PaCode.SK11ST_ONLINE.code()).setCommission(new Double(code.getRemark1()));

		code = codeRepository.findCommisionByCodeLgroup(PaGroup.GMARKET.commissionGroup());
		partnerMap.get(PaCode.EBAY_TV.code()).setCommission(new Double(code.getRemark()));
		partnerMap.get(PaCode.EBAY_ONLINE.code()).setCommission(new Double(code.getRemark1()));

		code = codeRepository.findCommisionByCodeLgroup(PaGroup.NAVER.commissionGroup());
		partnerMap.get(PaCode.NAVER.code()).setCommission(new Double(code.getRemark()));

		code = codeRepository.findCommisionByCodeLgroup(PaGroup.COUPANG.commissionGroup());
		partnerMap.get(PaCode.COUPANG_TV.code()).setCommission(new Double(code.getRemark()));
		partnerMap.get(PaCode.COUPANG_ONLINE.code()).setCommission(new Double(code.getRemark1()));

		code = codeRepository.findCommisionByCodeLgroup(PaGroup.WEMP.commissionGroup());
		partnerMap.get(PaCode.WEMP_TV.code()).setCommission(new Double(code.getRemark()));
		partnerMap.get(PaCode.WEMP_ONLINE.code()).setCommission(new Double(code.getRemark1()));

		code = codeRepository.findCommisionByCodeLgroup(PaGroup.INTERPARK.commissionGroup());
		partnerMap.get(PaCode.INTERPARK_TV.code()).setCommission(new Double(code.getRemark()));
		partnerMap.get(PaCode.INTERPARK_ONLINE.code()).setCommission(new Double(code.getRemark1()));

		code = codeRepository.findCommisionByCodeLgroup(PaGroup.LOTTEON.commissionGroup());
		partnerMap.get(PaCode.LOTTEON_TV.code()).setCommission(new Double(code.getRemark()));
		partnerMap.get(PaCode.LOTTEON_ONLINE.code()).setCommission(new Double(code.getRemark1()));

		code = codeRepository.findCommisionByCodeLgroup(PaGroup.TMON.commissionGroup());
		partnerMap.get(PaCode.TMON_TV.code()).setCommission(new Double(code.getRemark()));
		partnerMap.get(PaCode.TMON_ONLINE.code()).setCommission(new Double(code.getRemark1()));

		code = codeRepository.findCommisionByCodeLgroup(PaGroup.SSG.commissionGroup());
		partnerMap.get(PaCode.SSG_TV.code()).setCommission(new Double(code.getRemark()));
		partnerMap.get(PaCode.SSG_ONLINE.code()).setCommission(new Double(code.getRemark1()));

		partnerMap.get(PaCode.KAKAO_TV.code()).setCommission(0);
		partnerMap.get(PaCode.KAKAO_ONLINE.code()).setCommission(0);

		code = codeRepository.findCommisionByCodeLgroup(PaGroup.HALF.commissionGroup());
		partnerMap.get(PaCode.HALF_TV.code()).setCommission(new Double(code.getRemark()));
		partnerMap.get(PaCode.HALF_ONLINE.code()).setCommission(new Double(code.getRemark1()));
	
		code = codeRepository.findCommisionByCodeLgroup(PaGroup.TDEAL.commissionGroup());
		partnerMap.get(PaCode.TDEAL_TV.code()).setCommission(new Double(code.getRemark()));
		partnerMap.get(PaCode.TDEAL_ONLINE.code()).setCommission(new Double(code.getRemark1()));
	}

	// 제휴사별 주문가능 재고 비율
	public void loadStockRate() throws Exception {

		if (partnerMap == null) {
			loadPartnerBase();
		}

		List<Code> list = codeRepository.findByCodeLgroup(CodeGroup.STOCK_RATE.code());

		for (Code code : list) {
			try {
				partnerMap.get(code.getCodeMgroup()).setAbleStockRate(new Double(code.getCodeGroup()));
			} catch (NullPointerException e) {
				log.info(code.getCodeMgroup() + " : " + e.getMessage());
			}
		}

	}

	// 프로모션 최소마진율
	public void loadPromoMinMarginRate() throws Exception {
		Optional<Config> config = configRepository.findById("PA_LIMIT_MARGIN");

		if (config.isPresent()) {
			promoMinMarginRate = Double.parseDouble(config.get().getVal());
			log.info("프로모션 최소마진율: {}", promoMinMarginRate);
		} else {
			promoMinMarginRate = 10.0; // 로딩 안되면 기본 10으로 적용
		}
	}

	// 행사상품 최소마진율
	public void loadEventMinMarginRate() throws Exception {
		Optional<Config> config = configRepository.findById("PA_EVENT_LIMIT_MARGIN");

		if (config.isPresent()) {
			eventMinMarginRate = Double.parseDouble(config.get().getVal());
			log.info("행사상품 최소마진율: {}", eventMinMarginRate);
		} else {
			eventMinMarginRate = 10.0; // 로딩 안되면 기본 10으로 적용
		}
	}


	public String getOriginName(String originCode) {

		if (originMap == null) {
			return null;
		}
		return originMap.get(originCode);

	}

	public PartnerBase getPartnerBase(String paCode) {
		if (partnerMap == null) {
			return null;
		}
		return partnerMap.get(paCode);
	}

	public Double getCommision(String paCode) {
		PartnerBase base = getPartnerBase(paCode);
		if (base == null)
			return null;

		return base.getCommission();

	}

	public Double getStockRate(String paCode) {
		PartnerBase base = getPartnerBase(paCode);
		if (base == null)
			return null;

		return base.getAbleStockRate();
	}

	public Double getMinMarginRate(String paCode) {
		PartnerBase base = getPartnerBase(paCode);
		if (base == null)
			return null;

		return base.getMinMarginRate();
	}

	public Double getMinSalePrice(String paCode) {
		PartnerBase base = getPartnerBase(paCode);
		if (base == null)
			return null;

		return base.getMinSalePrice();
	}

	// 쿠팡상품승인상태 Key:명, Value:코드
	public void loadApprovalStatus() throws Exception {

		List<Code> list = codeRepository.findByCodeLgroup(CodeGroup.APPROVAL_STATUS.code());

		approvalStatusMap = new HashMap<String, String>();

		for (Code code : list) {
			approvalStatusMap.put(code.getCodeName(), code.getCodeMgroup());
		}

	}

	public String getApprovalStatus(String statusName) {

		if (approvalStatusMap == null) {
			return null;
		}
		return approvalStatusMap.get(statusName);
	}

	public Double getPromoMinMarginRate() {
		return promoMinMarginRate;
	}

	public Double getEventMinMarginRate() {
		return eventMinMarginRate;
	}
}
