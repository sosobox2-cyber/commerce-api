package com.cware.partner.coupang.domain;

import lombok.Data;

@Data
public class Attribute {

	//	옵션타입명
	String attributeTypeName;

	//	옵션값
	String attributeValueName;

	//	구매옵션/검색옵션 구분필드.
	//	EXPOSED : 구매옵션
	//	NONE : 검색옵션
	String exposed;

	//	수정 여부 구분필드.
	//	true : 수정가능
	//	false : 수정불가
	String editable;

}
