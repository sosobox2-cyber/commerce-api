package com.cware.partner.coupang.domain;

import lombok.Data;

@Data
public class Notice {

	//	상품고시정보카테고리명
	//	카테고리별 입력 가능한 상품고시정보 카테고리 중 하나를 입력
	String noticeCategoryName;

	//	상품고시정보카테고리상세명
	String noticeCategoryDetailName;

	//	내용
	String content;

}
