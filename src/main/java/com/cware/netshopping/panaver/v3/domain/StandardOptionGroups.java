package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StandardOptionGroups {

	// 표준형 옵션 그룹 타입
	private String groupName;	
	// 조합형 옵션
	private List<StandardOptionAttributes> standardOptionAttributes;
	
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<StandardOptionAttributes> getStandardOptionAttributes() {
		return standardOptionAttributes;
	}

	public void setStandardOptionAttributes(List<StandardOptionAttributes> standardOptionAttributes) {
		this.standardOptionAttributes = standardOptionAttributes;
	}

	@Override
	public String toString() {
		return "StandardOptionGroups [groupName=" + groupName + ", standardOptionAttributes=" + standardOptionAttributes + "]";
	}

}
