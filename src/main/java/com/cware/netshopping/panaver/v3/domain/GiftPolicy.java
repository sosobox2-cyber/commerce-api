package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class GiftPolicy {
		
	// 사은품 내용
	private String presentContent;
	
	
	public String getPresentContent() {
		return presentContent;
	}

	public void setPresentContent(String presentContent) {
		this.presentContent = presentContent;
	}

	@Override
	public String toString() {
		return "GiftPolicy [presentContent=" + presentContent + "]";
	}
		
}
