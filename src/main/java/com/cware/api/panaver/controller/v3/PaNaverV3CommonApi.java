package com.cware.api.panaver.controller.v3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.netshopping.panaver.v3.service.PaNaverV3CommonService;
import com.cware.netshopping.panaver.v3.domain.CategoriesList;
import com.cware.netshopping.panaver.v3.domain.Categories;
import com.cware.netshopping.panaver.v3.domain.Origin;
import com.cware.netshopping.panaver.v3.domain.BrandsList;
import com.cware.netshopping.panaver.v3.domain.ModelsList;
import com.cware.netshopping.panaver.v3.domain.Models;

@RestController
@RequestMapping(path = "/panaver/v3/common")
public class PaNaverV3CommonApi {
	
	@Autowired
	PaNaverV3CommonService paNaverCommonService;

	
	/**
	 * 원산지 코드 정보 전체 조회
	 * 
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/origin-areas")
	public ResponseEntity<?> origin(@RequestParam(defaultValue = "CMNAVERAPI") String procId) {
		
		Origin origin = paNaverCommonService.getOrigin(procId);

		return new ResponseEntity<>(origin, HttpStatus.OK);
	}
	
	
	/**
	 * 전체 카테고리 조회
	 * 
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/categories")
	public ResponseEntity<?> categories(@RequestParam(defaultValue = "CMNAVERAPI") String procId) {
		
		CategoriesList categoriesList = paNaverCommonService.getCategories(procId);

		return new ResponseEntity<>(categoriesList, HttpStatus.OK);
	}
		
	
	/**
	 * 카테고리 상세 정보 조회
	 * 
	 * @param categoryId
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/categories-detail")
	public ResponseEntity<?> categories(@RequestParam(defaultValue = "") String categoryId, @RequestParam(defaultValue = "CMNAVERAPI") String procId) {
		
		Categories categories = paNaverCommonService.getCategories(categoryId, procId);

		return new ResponseEntity<>(categories, HttpStatus.OK);
	}
	
	
	/**
	 * 브랜드 조회
	 * @param name
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/product-brands")
	public ResponseEntity<?> brands(@RequestParam String name, @RequestParam(defaultValue = "CMNAVERAPI") String procId) {
		
		BrandsList brandsLsit = paNaverCommonService.getBrands(name, procId);

		return new ResponseEntity<>(brandsLsit, HttpStatus.OK);
	}
	
	
	/**
	 * 모델 조회
	 * @param name
	 * @param page
	 * @param size
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/product-models")
	public ResponseEntity<?> Models(@RequestParam String name, @RequestParam String page, @RequestParam String size, @RequestParam(defaultValue = "CMNAVERAPI") String procId) {
		
		ModelsList mdelsList = paNaverCommonService.getModels(name, page, size, procId);

		return new ResponseEntity<>(mdelsList, HttpStatus.OK);
	}
	
	/**
	 * 모델 단건 조회
	 * @param id
	 * @param procId
	 * @return
	 */
	@GetMapping(value="/product-models-single")
	public ResponseEntity<?> ModelsSingle(@RequestParam String id, @RequestParam(defaultValue = "CMNAVERAPI") String procId) {
		
		Models model = paNaverCommonService.getModelsSingle(id, procId);
		
		return new ResponseEntity<>(model, HttpStatus.OK);
	}
	
}
