package com.cware.partner.coupang.domain;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Category {

	// 단일상품 등록 가능 여부
	// isAllowSingleItem 값에 상관없이 1개의 단일옵션 상품을 입력할 경우 상품생성에서 속성값(attributes)입력 없이 상품등록이 가능합니다. ( 2020년 말 이후부터 적용)
	boolean isAllowSingleItem;

	// 카테고리 옵션목록 (구매옵션/검색옵션)
	// 옵션 개수 만큼 반복
	// 상품 판매 승인 이전에는 attributes의 추가, 변경, 삭제가 가능
	// 상품 판매 승인 이후에는 attributes의 추가가 가능하나 attributes의 삭제, 변경 등은 불가
	List<Map<String,Object>> attributes;

	List<Map<String,Object>> noticeCategories; // 상품고시정보목록
	List<Map<String,Object>> requiredDocumentNames; // 구비서류목록
	List<Map<String,Object>> certifications; // 상품 인증 정보

	// 허용된 상품 상태
	// 표시된 값에 따라 리퍼, 중고 상품 등록이 가능한지 확인
	// NEW: 새상품
	// REFURBISHED: 리퍼
	// USED_BEST: 중고(최상)
	// USED_GOOD: 중고(상)
	// USED_NORMAL: 중고(중)
	List<String> allowedOfferConditions;

}
