package com.cware.netshopping.pakakao.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OptionDisplay {

	// 바로구매 설정 여부
	boolean useInstantOrder;

	// 바로구매 템플릿 유형
	// TYPE_1 1열 (가로형)
	// TYPE_2 2열 (세로형)
	String instantOrderTemplateType;

	public boolean isUseInstantOrder() {
		return useInstantOrder;
	}

	public void setUseInstantOrder(boolean useInstantOrder) {
		this.useInstantOrder = useInstantOrder;
	}

	public String getInstantOrderTemplateType() {
		return instantOrderTemplateType;
	}

	public void setInstantOrderTemplateType(String instantOrderTemplateType) {
		this.instantOrderTemplateType = instantOrderTemplateType;
	}

	@Override
	public String toString() {
		return "OptionDisplay [useInstantOrder=" + useInstantOrder + ", instantOrderTemplateType="
				+ instantOrderTemplateType + "]";
	}

}
