package com.cware.partner.coupang.domain;

import lombok.Data;

@Data
public class ContentDetail {

	// 내용
	String content;

	// 세부타입
	// IMAGE 이미지
	// TEXT 텍스트
	String detailType;

}
