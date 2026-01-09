package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OptionCustom {

	// 옵션 일련번호
	private long id;
	// 옵션명
	private String groupName;
	// 옵션값
	private String name;
	// 사용 여부
	private String usable;
	

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsable() {
		return usable;
	}

	public void setUsable(String usable) {
		this.usable = usable;
	}

	@Override
	public String toString() {
		return "OptionCustom [id=" + id + ", groupName=" + groupName + ", name=" + name + ", usable=" + usable + "]";
	}

}
