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
import com.cware.partner.common.domain.entity.Code;
import com.cware.partner.common.domain.entity.Config;
import com.cware.partner.common.repository.CodeRepository;
import com.cware.partner.common.repository.ConfigRepository;
import com.cware.partner.sync.domain.PartnerBase;
import com.cware.partner.sync.repository.PaPromoMinMarginRateRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CodeService {

	@Autowired
	private CodeRepository codeRepository;

	@Autowired
	private ConfigRepository configRepository;

	@Autowired
	private PaPromoMinMarginRateRepository paPromoMinMarginRateRepository;

	// 제조사코드/명
	private Map<String, String> originMap;

	// 제휴사 기준정보
	private Map<String, PartnerBase> partnerMap;

	// 행사상품 최소마진율
	private Double eventMinMarginRate;

	@PostConstruct
	public void loadCodeList() throws Exception {
		loadOriginCode();
		loadPartnerBase();
		loadSaleCondition();
		loadStockRate();
		loadCommision();
		loadPromoMinMarginRate();
		loadEventMinMarginRate();

		log.info("제휴사 기준정보 로딩 {}", partnerMap);
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
		
		partnerMap.put(PaCode.FAPLE_TV.code(), new PartnerBase(PaCode.FAPLE_TV.code()));
		partnerMap.put(PaCode.FAPLE_ONLINE.code(), new PartnerBase(PaCode.FAPLE_ONLINE.code()));
		
		partnerMap.put(PaCode.QEEN_TV.code(), new PartnerBase(PaCode.QEEN_TV.code()));
		partnerMap.put(PaCode.QEEN_ONLINE.code(), new PartnerBase(PaCode.QEEN_ONLINE.code()));

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
				partnerMap.get(PaCode.WEMP_ONLINE.code()).setMinMarginRate(new Double(code.getRemark1()));
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
				partnerMap.get(PaCode.SSG_TV.code()).setTaxFreeMarginRate(new Double(code.getRemark2() == null ? code.getRemark():code.getRemark2()));
				partnerMap.get(PaCode.SSG_ONLINE.code()).setTaxFreeMarginRate(new Double(code.getRemark2() == null ? code.getRemark1():code.getRemark2()));
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
			case "94": // 패션플러스 최소마진율
				partnerMap.get(PaCode.FAPLE_TV.code()).setMinMarginRate(new Double(code.getRemark()));
				partnerMap.get(PaCode.FAPLE_ONLINE.code()).setMinMarginRate(new Double(code.getRemark1()));
				break;
			case "95": // 패션플러스 최저판매가
				partnerMap.get(PaCode.FAPLE_TV.code()).setMinSalePrice(new Double(code.getRemark()));
				partnerMap.get(PaCode.FAPLE_ONLINE.code()).setMinSalePrice(new Double(code.getRemark1()));
				break;
			case "96": // 퀸잇 최소마진율
				partnerMap.get(PaCode.QEEN_TV.code()).setMinMarginRate(new Double(code.getRemark()));
				partnerMap.get(PaCode.QEEN_ONLINE.code()).setMinMarginRate(new Double(code.getRemark1()));
				break;
			case "97": // 퀸잇 최저판매가
				partnerMap.get(PaCode.QEEN_TV.code()).setMinSalePrice(new Double(code.getRemark()));
				partnerMap.get(PaCode.QEEN_ONLINE.code()).setMinSalePrice(new Double(code.getRemark1()));
				break;

			default:
				break;
			}
		}
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
		
		code = codeRepository.findCommisionByCodeLgroup(PaGroup.FAPLE.commissionGroup());
		partnerMap.get(PaCode.FAPLE_TV.code()).setCommission(new Double(code.getRemark()));
		partnerMap.get(PaCode.FAPLE_ONLINE.code()).setCommission(new Double(code.getRemark1()));
		
		code = codeRepository.findCommisionByCodeLgroup(PaGroup.QEEN.commissionGroup());
		partnerMap.get(PaCode.QEEN_TV.code()).setCommission(new Double(code.getRemark()));
		partnerMap.get(PaCode.QEEN_ONLINE.code()).setCommission(new Double(code.getRemark1()));
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

	/* 2022-09-29 프로모션 최소 마진율 TCONFIG -> TPAPROMOMINMARGIN 으로 관리 */
	// 프로모션 최소마진율
	public void loadPromoMinMarginRate() throws Exception {

		if (partnerMap == null) {
			loadPartnerBase();
		}

		partnerMap.get(PaCode.SK11ST_TV.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.SK11ST_TV.groupCode(), PaCode.SK11ST_TV.code()));
		partnerMap.get(PaCode.SK11ST_ONLINE.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.SK11ST_ONLINE.groupCode(), PaCode.SK11ST_ONLINE.code()));

		partnerMap.get(PaCode.EBAY_TV.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.EBAY_TV.groupCode(), PaCode.EBAY_TV.code()));
		partnerMap.get(PaCode.EBAY_ONLINE.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.EBAY_ONLINE.groupCode(), PaCode.EBAY_ONLINE.code()));

		partnerMap.get(PaCode.NAVER.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.NAVER.groupCode(), PaCode.NAVER.code()));

		partnerMap.get(PaCode.COUPANG_TV.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.COUPANG_TV.groupCode(), PaCode.COUPANG_TV.code()));
		partnerMap.get(PaCode.COUPANG_ONLINE.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.COUPANG_ONLINE.groupCode(), PaCode.COUPANG_ONLINE.code()));

		partnerMap.get(PaCode.WEMP_TV.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.WEMP_TV.groupCode(), PaCode.WEMP_TV.code()));
		partnerMap.get(PaCode.WEMP_ONLINE.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.WEMP_ONLINE.groupCode(), PaCode.WEMP_ONLINE.code()));

		partnerMap.get(PaCode.INTERPARK_TV.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.INTERPARK_TV.groupCode(), PaCode.INTERPARK_TV.code()));
		partnerMap.get(PaCode.INTERPARK_ONLINE.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.INTERPARK_ONLINE.groupCode(), PaCode.INTERPARK_ONLINE.code()));

		partnerMap.get(PaCode.LOTTEON_TV.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.LOTTEON_TV.groupCode(), PaCode.LOTTEON_TV.code()));
		partnerMap.get(PaCode.LOTTEON_ONLINE.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.LOTTEON_ONLINE.groupCode(), PaCode.LOTTEON_ONLINE.code()));

		partnerMap.get(PaCode.TMON_TV.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.TMON_TV.groupCode(), PaCode.TMON_TV.code()));
		partnerMap.get(PaCode.TMON_ONLINE.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.TMON_ONLINE.groupCode(), PaCode.TMON_ONLINE.code()));

		partnerMap.get(PaCode.SSG_TV.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.SSG_TV.groupCode(), PaCode.SSG_TV.code()));
		partnerMap.get(PaCode.SSG_ONLINE.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.SSG_ONLINE.groupCode(), PaCode.SSG_ONLINE.code()));

		partnerMap.get(PaCode.KAKAO_TV.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.KAKAO_TV.groupCode(), PaCode.KAKAO_TV.code()));
		partnerMap.get(PaCode.KAKAO_ONLINE.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.KAKAO_ONLINE.groupCode(), PaCode.KAKAO_ONLINE.code()));

		partnerMap.get(PaCode.HALF_TV.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.HALF_TV.groupCode(), PaCode.HALF_TV.code()));
		partnerMap.get(PaCode.HALF_ONLINE.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.HALF_ONLINE.groupCode(), PaCode.HALF_ONLINE.code()));
		
		partnerMap.get(PaCode.TDEAL_TV.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.TDEAL_TV.groupCode(), PaCode.TDEAL_TV.code()));
		partnerMap.get(PaCode.TDEAL_ONLINE.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.TDEAL_ONLINE.groupCode(), PaCode.TDEAL_ONLINE.code()));
	
		partnerMap.get(PaCode.FAPLE_TV.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.FAPLE_TV.groupCode(), PaCode.FAPLE_TV.code()));
		partnerMap.get(PaCode.FAPLE_ONLINE.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.FAPLE_ONLINE.groupCode(), PaCode.FAPLE_ONLINE.code()));
		
		partnerMap.get(PaCode.QEEN_TV.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.QEEN_TV.groupCode(), PaCode.QEEN_TV.code()));
		partnerMap.get(PaCode.QEEN_ONLINE.code()).setPromoMinMarginRate(paPromoMinMarginRateRepository.getPromoMinMarginRate(PaCode.QEEN_ONLINE.groupCode(), PaCode.QEEN_ONLINE.code()));
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

	public Double getPromoMinMarginRate(String paCode) {
		PartnerBase rate = partnerMap.get(paCode);
		if (rate == null) {
			return null;
		}
		return rate.getPromoMinMarginRate();
	}

	public Double getEventMinMarginRate() {
		return eventMinMarginRate;
	}
}
