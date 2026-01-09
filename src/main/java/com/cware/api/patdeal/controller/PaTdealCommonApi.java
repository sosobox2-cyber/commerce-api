package com.cware.api.patdeal.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.patdeal.domain.AreasList;
import com.cware.netshopping.patdeal.domain.CategoriesList;
import com.cware.netshopping.patdeal.domain.DisplayCategoriesList;
import com.cware.netshopping.patdeal.domain.DutyCategoriesList;
import com.cware.netshopping.patdeal.service.PaTdealCommonService;

@RestController
@RequestMapping(path = "/patdeal/common")
public class PaTdealCommonApi {
	
	@Autowired
	PaTdealCommonService paTdealCommonService;
	
	
	/**
	 * 전체 카테고리 조회
	 * 
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/categories")
	public ResponseEntity<?> categories(@RequestParam(defaultValue = "ENTDEALAPI") String procId) {
				
		CategoriesList categoriesList = paTdealCommonService.getCategories(procId);
		
		return new ResponseEntity<>(categoriesList, HttpStatus.OK);
	}
		
	
	/**
	 * 전체 전시카테고리 조회
	 * 
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/display-categories")
	public ResponseEntity<?> displayCategories(@RequestParam(defaultValue = "ENTDEALAPI") String procId) {
				
		DisplayCategoriesList displayCategoriesList = paTdealCommonService.getDisplayCategories(procId);
		
		return new ResponseEntity<>(displayCategoriesList, HttpStatus.OK);
	}
	
	
	/**
	 * 전체 정보고시 항목 조회
	 * 
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/offer")
	public ResponseEntity<?> offer(@RequestParam(defaultValue = "ENTDEALAPI") String procId) {
				
		DutyCategoriesList dutyCategoriesList = paTdealCommonService.getOffer(procId);
		
		return new ResponseEntity<>(dutyCategoriesList, HttpStatus.OK);
	}
	
	/**
	 * 전체 브랜드 조회
	 * 
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/brands")
	public ResponseEntity<?> brands(@RequestParam(defaultValue = "ENTDEALAPI") String procId) {
				
		Map<String, Object> brandsList = paTdealCommonService.getBrands(procId);
		
		return new ResponseEntity<>(brandsList, HttpStatus.OK);
	}
	
	/**
	 * 브랜드 생성
	 * 
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/brands/register")
	public ResponseEntity<?> registerBrands(String brandCode, @RequestParam(defaultValue = "ENTDEALAPI") String procId) {
				
		ResponseMsg result = paTdealCommonService.registerBrand(brandCode,procId);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	/**
	 * 배송비 지역 조회
	 * 
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/areas")
	public ResponseEntity<?> areas(@RequestParam(defaultValue = "ENTDEALAPI") String procId) {
				
		AreasList areasList = paTdealCommonService.getAreas(procId);
		
		return new ResponseEntity<>(areasList, HttpStatus.OK);
	}
	
}
