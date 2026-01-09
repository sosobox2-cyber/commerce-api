package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Origin {
		
	// 원산지 목록
	private List<OriginAreaCodeNames> originAreaCodeNames;
	
	public List<OriginAreaCodeNames> getOriginAreaCodeNames() {
		return originAreaCodeNames;
	}
	
	public void setOriginAreaCodeNames(List<OriginAreaCodeNames> originAreaCodeNames) {
		this.originAreaCodeNames = originAreaCodeNames;
	}

	@Override
	public String toString() {
		return "Origin [originAreaCodeNames=" + originAreaCodeNames + "]";
	}

}
