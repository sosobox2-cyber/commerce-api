package com.cware.partner.coupang.domain;

import java.util.List;

import lombok.Data;

@Data
public class Content {

	// 컨텐츠타입
	// IMAGE 이미지
	// IMAGE_NO_SPACE 이미지(공백없음)
	// TEXT 텍스트
	// IMAGE_TEXT 이미지-텍스트
	// TEXT_IMAGE 텍스트-이미지
	// IMAGE_IMAGE 이미지-이미지
	// TEXT_TEXT 텍스트-텍스트
	// TITLE 제목
	// HTML HTML
	String contentsType;

	//	상세컨텐츠목록
	List<ContentDetail> contentDetails;

}
