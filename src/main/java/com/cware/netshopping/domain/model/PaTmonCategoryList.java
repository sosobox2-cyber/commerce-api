package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaTmonCategoryList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private Long no;
	private String name;
	private String selectable;
	
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSelectable() {
		return selectable;
	}
	public void setSelectable(String selectable) {
		this.selectable = selectable;
	}
	
	

}
