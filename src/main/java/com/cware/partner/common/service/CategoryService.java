package com.cware.partner.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.partner.common.code.PaGroup;
import com.cware.partner.sync.domain.entity.PaCopnCategoryAddInfo;
import com.cware.partner.sync.domain.entity.PaGmktCategoryAddInfo;
import com.cware.partner.sync.domain.entity.PaTdealCategoryAddInfo;
import com.cware.partner.sync.repository.PaCopnCategoryAddInfoRepository;
import com.cware.partner.sync.repository.PaGmktCategoryAddInfoRepository;
import com.cware.partner.sync.repository.PaTdealCategoryAddInfoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryService {

	
	@Autowired
	private PaCopnCategoryAddInfoRepository paCopnCategoryAddInfoRepository;
	
	@Autowired
	private PaTdealCategoryAddInfoRepository paTdealCategoryAddInfoRepository;
	
	@Autowired
	private PaGmktCategoryAddInfoRepository paGmktCategoryAddInfoRepository;
	
	private Map<String, String> copnCatCommissionMap;

	private Map<String, String> copnCatMinMarginRateMap;

	private Map<String, String> copnCatPromoMinMarginRateMap;

	// 수수료율
	private Map<String, String> tdealTvCatCommissionMap;
	private Map<String, String> tdealMobileCatCommissionMap;

	// 입점허들
	private Map<String, String> tdealTvCatMinMarginRateMap;
	private Map<String, String> tdealMobileCatMinMarginRateMap;

	// 프로모션마진허들
	private Map<String, String> tdealTvCatPromoMinMarginRateMap;
	private Map<String, String> tdealMobileCatPromoMinMarginRateMap;

	private Map<String, String> tdealPaTvCatCommissionMap;
	private Map<String, String> tdealPaMobileCatCommissionMap;

	private Map<String, String> tdealPaTvCatMinMarginRateMap;
	private Map<String, String> tdealPaMobileCatMinMarginRateMap;

	private Map<String, String> tdealPaTvCatPromoMinMarginRateMap;
	private Map<String, String> tdealPaMobileCatPromoMinMarginRateMap;

	//수수료율
	private Map<String, String> ebayTvCatCommissionMap;
	private Map<String, String> ebayMobileCatCommissionMap;

	//입점허들
	private Map<String, String> ebayTvCatMinMarginRateMap;
	private Map<String, String> ebayMobileCatMinMarginRateMap;
	
	//프로모션마진허들
	private Map<String, String> ebayTvCatPromoMinMarginRateMap;
	private Map<String, String> ebayMobileCatPromoMinMarginRateMap;
	
	@PostConstruct
	public void loadCodeList() throws Exception {

		loadPaCatagoryList();

		log.info("카테고리정보 로딩");

	}

	public void loadPaCatagoryList() throws Exception {

		//쿠팡 카테고리 정보
		List<PaCopnCategoryAddInfo> list = paCopnCategoryAddInfoRepository.getCopnCategoryAddInfo(PaGroup.COUPANG.code());
		copnCatCommissionMap    = new HashMap<String, String>();
		copnCatMinMarginRateMap = new HashMap<String, String>();
		copnCatPromoMinMarginRateMap = new HashMap<String, String>();


		for (PaCopnCategoryAddInfo paCopnCategoryAddInfo : list) {
			copnCatCommissionMap.put(paCopnCategoryAddInfo.getLmsdCode(), paCopnCategoryAddInfo.getCommission());
			copnCatMinMarginRateMap.put(paCopnCategoryAddInfo.getLmsdCode(), paCopnCategoryAddInfo.getMinMarginRate());
			copnCatPromoMinMarginRateMap.put(paCopnCategoryAddInfo.getLmsdCode(), paCopnCategoryAddInfo.getPromoMinMarginRate());
		}

		// 티딜 카테고리 정보
		List<PaTdealCategoryAddInfo> tdealCategoryAddInfoList = paTdealCategoryAddInfoRepository.getTdealCategoryAddInfo(PaGroup.TDEAL.code());
		
		tdealTvCatCommissionMap = new HashMap<String, String>();
		tdealMobileCatCommissionMap = new HashMap<String, String>();
		
		tdealTvCatMinMarginRateMap = new HashMap<String, String>();
		tdealMobileCatMinMarginRateMap = new HashMap<String, String>();
		
		tdealTvCatPromoMinMarginRateMap = new HashMap<String, String>();
		tdealMobileCatPromoMinMarginRateMap = new HashMap<String, String>();

		for (PaTdealCategoryAddInfo paTdealCategoryAddInfo : tdealCategoryAddInfoList) {
			
			switch(paTdealCategoryAddInfo.getPaCode()) {
			
				case "D1" :
					tdealTvCatCommissionMap.put(paTdealCategoryAddInfo.getLmsdCode(), paTdealCategoryAddInfo.getCommission());
					tdealTvCatMinMarginRateMap.put(paTdealCategoryAddInfo.getLmsdCode(), paTdealCategoryAddInfo.getMinMarginRate());
					tdealTvCatPromoMinMarginRateMap.put(paTdealCategoryAddInfo.getLmsdCode(), paTdealCategoryAddInfo.getPromoMinMarginRate());
					break;
				case "D2" :
					tdealMobileCatCommissionMap.put(paTdealCategoryAddInfo.getLmsdCode(), paTdealCategoryAddInfo.getCommission());
					tdealMobileCatMinMarginRateMap.put(paTdealCategoryAddInfo.getLmsdCode(), paTdealCategoryAddInfo.getMinMarginRate());
					tdealMobileCatPromoMinMarginRateMap.put(paTdealCategoryAddInfo.getLmsdCode(), paTdealCategoryAddInfo.getPromoMinMarginRate());
					break;
				default:
					break;
				}
		}
		
		// 티딜 카테고리 정보(paLmsdKey 기준)
		List<PaTdealCategoryAddInfo> tdealCategoryAddInfoListAll = paTdealCategoryAddInfoRepository.getTdealCategoryAddInfoAll(PaGroup.TDEAL.code());

		tdealPaTvCatCommissionMap = new HashMap<String, String>();
		tdealPaMobileCatCommissionMap = new HashMap<String, String>();

		tdealPaTvCatMinMarginRateMap = new HashMap<String, String>();
		tdealPaMobileCatMinMarginRateMap = new HashMap<String, String>();

		tdealPaTvCatPromoMinMarginRateMap = new HashMap<String, String>();
		tdealPaMobileCatPromoMinMarginRateMap = new HashMap<String, String>();

		for (PaTdealCategoryAddInfo paTdealCategoryAddInfo : tdealCategoryAddInfoListAll) {

			switch (paTdealCategoryAddInfo.getPaCode()) {

			case "D1":
				tdealPaTvCatCommissionMap.put(paTdealCategoryAddInfo.getPaLmsdKey(), paTdealCategoryAddInfo.getCommission());
				tdealPaTvCatMinMarginRateMap.put(paTdealCategoryAddInfo.getPaLmsdKey(), paTdealCategoryAddInfo.getMinMarginRate());
				tdealPaTvCatPromoMinMarginRateMap.put(paTdealCategoryAddInfo.getPaLmsdKey(), paTdealCategoryAddInfo.getPromoMinMarginRate());
				break;
			case "D2":
				tdealPaMobileCatCommissionMap.put(paTdealCategoryAddInfo.getPaLmsdKey(), paTdealCategoryAddInfo.getCommission());
				tdealPaMobileCatMinMarginRateMap.put(paTdealCategoryAddInfo.getPaLmsdKey(), paTdealCategoryAddInfo.getMinMarginRate());
				tdealPaMobileCatPromoMinMarginRateMap.put(paTdealCategoryAddInfo.getPaLmsdKey(), paTdealCategoryAddInfo.getPromoMinMarginRate());
				break;
			default:
				break;
			}
		}
		
		//이베이 카테고리 정보
		List<PaGmktCategoryAddInfo> gmktCategoryAddInfoList = paGmktCategoryAddInfoRepository.getGmktCategoryAddInfo(PaGroup.GMARKET.code());

		ebayTvCatCommissionMap = new HashMap<String, String>();
		ebayMobileCatCommissionMap = new HashMap<String, String>();
		
		ebayTvCatMinMarginRateMap = new HashMap<String, String>();
		ebayMobileCatMinMarginRateMap = new HashMap<String, String>();
		
		ebayTvCatPromoMinMarginRateMap = new HashMap<String, String>();
		ebayMobileCatPromoMinMarginRateMap = new HashMap<String, String>();

		for (PaGmktCategoryAddInfo paGmktCategoryAddInfo : gmktCategoryAddInfoList) {
		
			switch(paGmktCategoryAddInfo.getPaCode()) {
			
			case "21" : 
				ebayTvCatCommissionMap.put(paGmktCategoryAddInfo.getLmsdCode(), paGmktCategoryAddInfo.getCommission());
				ebayTvCatMinMarginRateMap.put(paGmktCategoryAddInfo.getLmsdCode(), paGmktCategoryAddInfo.getMinMarginRate());
				ebayTvCatPromoMinMarginRateMap.put(paGmktCategoryAddInfo.getLmsdCode(), paGmktCategoryAddInfo.getPromoMinMarginRate());
				break;
			case "22" : 
				ebayMobileCatCommissionMap.put(paGmktCategoryAddInfo.getLmsdCode(), paGmktCategoryAddInfo.getCommission());
				ebayMobileCatMinMarginRateMap.put(paGmktCategoryAddInfo.getLmsdCode(), paGmktCategoryAddInfo.getMinMarginRate());
				ebayMobileCatPromoMinMarginRateMap.put(paGmktCategoryAddInfo.getLmsdCode(), paGmktCategoryAddInfo.getPromoMinMarginRate());
				break;
			default:
				break;
			
		}
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

	public Double getTdealCatCommission(String lmsdKey, String paCode) {
		String tdealCatCommission = null;

		switch(paCode) {
			case "D1" : 
				tdealCatCommission = tdealTvCatCommissionMap.get(lmsdKey);
				return (tdealCatCommission == null) ? null : new Double(tdealCatCommission);
			case "D2" : 
				tdealCatCommission = tdealMobileCatCommissionMap.get(lmsdKey);
				return (tdealCatCommission == null) ? null : new Double(tdealCatCommission);
			default:
				return null;
		}
	}
	
	public Double getTdealCatMinMarginRate(String lmsdKey, String paCode) {
		String tdealCatMinMarginRate = null;
		
		switch(paCode) {
			case "D1" : 
				tdealCatMinMarginRate = tdealTvCatMinMarginRateMap.get(lmsdKey);
				return (tdealCatMinMarginRate == null) ? null : new Double(tdealCatMinMarginRate);
			case "D2" : 
				tdealCatMinMarginRate = tdealMobileCatMinMarginRateMap.get(lmsdKey);
				return (tdealCatMinMarginRate == null) ? null : new Double(tdealCatMinMarginRate);
			default:
				return null;
		} 

	}
	
	public Double getTdealCatPromoMinMarginRate(String lmsdKey, String paCode) {
		String tdealCatPromoMinMarginRate = null;
		
		switch(paCode) {
			case "D1" : 
				tdealCatPromoMinMarginRate = tdealTvCatPromoMinMarginRateMap.get(lmsdKey);
				return (tdealCatPromoMinMarginRate == null) ? null : new Double(tdealCatPromoMinMarginRate);
			case "D2" : 
				tdealCatPromoMinMarginRate = tdealMobileCatPromoMinMarginRateMap.get(lmsdKey);
				return (tdealCatPromoMinMarginRate == null) ? null : new Double(tdealCatPromoMinMarginRate);
			default:
				return null;
		} 
	}
	
	public Double getTdealPaCatCommission(String paLmsdKey, String paCode) {
		String tdealPaCatCommission = null;

		switch(paCode) {
			case "D1" : 
				tdealPaCatCommission = tdealPaTvCatCommissionMap.get(paLmsdKey);
				return (tdealPaCatCommission == null) ? null : new Double(tdealPaCatCommission);
			case "D2" : 
				tdealPaCatCommission = tdealPaMobileCatCommissionMap.get(paLmsdKey);
				return (tdealPaCatCommission == null) ? null : new Double(tdealPaCatCommission);
			default:
				return null;
		}
	}
	
	public Double getTdealPaCatMinMarginRate(String paLmsdKey, String paCode) {
		String tdealPaCatMinMarginRate = null;
		
		switch(paCode) {
			case "D1" : 
				tdealPaCatMinMarginRate = tdealPaTvCatMinMarginRateMap.get(paLmsdKey);
				return (tdealPaCatMinMarginRate == null) ? null : new Double(tdealPaCatMinMarginRate);
			case "D2" : 
				tdealPaCatMinMarginRate = tdealPaMobileCatMinMarginRateMap.get(paLmsdKey);
				return (tdealPaCatMinMarginRate == null) ? null : new Double(tdealPaCatMinMarginRate);
			default:
				return null;
		}
		
	}
	
	public Double getTdealPaCatPromoMinMarginRate(String paLmsdKey, String paCode) {
		String tdealPaCatPromoMinMarginRate = null;

		switch(paCode) {
			case "D1" : 
				tdealPaCatPromoMinMarginRate = tdealPaTvCatPromoMinMarginRateMap.get(paLmsdKey);
				return (tdealPaCatPromoMinMarginRate == null) ? null : new Double(tdealPaCatPromoMinMarginRate);
			case "D2" : 
				tdealPaCatPromoMinMarginRate = tdealPaMobileCatPromoMinMarginRateMap.get(paLmsdKey);
				return (tdealPaCatPromoMinMarginRate == null) ? null : new Double(tdealPaCatPromoMinMarginRate);
			default:
				return null;
		}
	}
	
	public Double getEbayCatCommission(String lmsdKey, String paCode) {
        String ebayCatCommission = null;
		
		switch(paCode) {
			case "21" : 
                ebayCatCommission = ebayTvCatCommissionMap.get(lmsdKey);
                return (ebayCatCommission == null) ? null : new Double(ebayCatCommission);
			case "22" : 
                ebayCatCommission = ebayMobileCatCommissionMap.get(lmsdKey);
                return (ebayCatCommission == null) ? null : new Double(ebayCatCommission);
			default:
				return null;
		}
	}	
	
	public Double getEbayCatMinMarginRate(String lmsdKey, String paCode) {
        String ebayCatMinMarginRate = null;
        
		switch(paCode) {
			case "21" : 
                ebayCatMinMarginRate = ebayTvCatMinMarginRateMap.get(lmsdKey);
                return  (ebayCatMinMarginRate == null) ? null : new Double(ebayCatMinMarginRate);
			case "22" : 
                ebayCatMinMarginRate = ebayMobileCatMinMarginRateMap.get(lmsdKey);
                return  (ebayCatMinMarginRate == null) ? null : new Double(ebayCatMinMarginRate);
			default:
				return null;
		}
	}
	
	public Double getEbayCatPromoMinMarginRate(String lmsdKey, String paCode) {
        String ebayCatPromoMinMarginRate = null;
        
		switch(paCode) {
			case "21" : 
                ebayCatPromoMinMarginRate = ebayTvCatPromoMinMarginRateMap.get(lmsdKey);
                return (ebayCatPromoMinMarginRate == null) ? null : new Double(ebayCatPromoMinMarginRate);
			case "22" : 
                ebayCatPromoMinMarginRate = ebayMobileCatPromoMinMarginRateMap.get(lmsdKey);
                return (ebayCatPromoMinMarginRate == null) ? null : new Double(ebayCatPromoMinMarginRate);
			default:
				return null;
		}
	}	
	
}
