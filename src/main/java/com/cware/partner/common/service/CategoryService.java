package com.cware.partner.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.partner.common.code.PaGroup;
import com.cware.partner.sync.domain.entity.PaCopnCategoryAddInfo;
import com.cware.partner.sync.repository.PaCopnCategoryAddInfoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryService {

	
	@Autowired
	private PaCopnCategoryAddInfoRepository paCopnCategoryAddInfoRepository;
	
	private Map<String, String> copnCatCommissionMap;

	private Map<String, String> copnCatMinMarginRateMap;
	
	private Map<String, String> copnCatPromoMinMarginRateMap;


	@PostConstruct
	public void loadCodeList() throws Exception {

		loadCopnCatagoryList();

		log.info("쿠팡 카테고리정보 로딩");

	}

	public void loadCopnCatagoryList() throws Exception {

		List<PaCopnCategoryAddInfo> list = paCopnCategoryAddInfoRepository.getCopnCategoryAddInfo(PaGroup.COUPANG.code());
		copnCatCommissionMap    = new HashMap<String, String>();
		copnCatMinMarginRateMap = new HashMap<String, String>();
		copnCatPromoMinMarginRateMap = new HashMap<String, String>();


		for (PaCopnCategoryAddInfo paCopnCategoryAddInfo : list) {
			copnCatCommissionMap.put(paCopnCategoryAddInfo.getLmsdCode(), paCopnCategoryAddInfo.getCommission());
			copnCatMinMarginRateMap.put(paCopnCategoryAddInfo.getLmsdCode(), paCopnCategoryAddInfo.getMinMarginRate());
			copnCatPromoMinMarginRateMap.put(paCopnCategoryAddInfo.getLmsdCode(), paCopnCategoryAddInfo.getPromoMinMarginRate());
		}

	}

	public Double getCopnCatCommission(String lmsdKey) {

		if (copnCatCommissionMap == null) {
			return null;
		}
		String copnCatCommission = copnCatCommissionMap.get(lmsdKey);

		return (copnCatCommission == null) ? null : new Double(copnCatCommission);
	}
	
	public Double getCopnCatMinMarginRate(String lmsdKey) {

		if (copnCatMinMarginRateMap == null) {
			return null;
		}
		String copnCatMinMarginRate = copnCatMinMarginRateMap.get(lmsdKey);

		return (copnCatMinMarginRate == null) ? null : new Double(copnCatMinMarginRate);
	}
	
	public Double getCopnCatPromoMinMarginRate(String lmsdKey) {

		if (copnCatPromoMinMarginRateMap == null) {
			return null;
		}
		String copnCatPromoMinMarginRate = copnCatPromoMinMarginRateMap.get(lmsdKey);

		return (copnCatPromoMinMarginRate == null) ? null : new Double(copnCatPromoMinMarginRate);
	}

}
