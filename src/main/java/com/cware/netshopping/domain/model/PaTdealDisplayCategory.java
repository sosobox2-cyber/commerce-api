package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaTdealDisplayCategory extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String dispCatId;
	private String depth1CatNm;
	private String depth2CatNm;
	private String depth3CatNm;
	private String depth4CatNm;
	private String depth5CatNm;
	private String fullCatNm;
	private int dispOrder;
	private String dispYn;
	private String deleteYn;
	
	public String getDispCatId() {
		return dispCatId;
	}
	public void setDispCatId(String dispCatId) {
		this.dispCatId = dispCatId;
	}
	public String getDepth1CatNm() {
		return depth1CatNm;
	}
	public void setDepth1CatNm(String depth1CatNm) {
		this.depth1CatNm = depth1CatNm;
	}
	public String getDepth2CatNm() {
		return depth2CatNm;
	}
	public void setDepth2CatNm(String depth2CatNm) {
		this.depth2CatNm = depth2CatNm;
	}
	public String getDepth3CatNm() {
		return depth3CatNm;
	}
	public void setDepth3CatNm(String depth3CatNm) {
		this.depth3CatNm = depth3CatNm;
	}
	public String getDepth4CatNm() {
		return depth4CatNm;
	}
	public void setDepth4CatNm(String depth4CatNm) {
		this.depth4CatNm = depth4CatNm;
	}
	public String getDepth5CatNm() {
		return depth5CatNm;
	}
	public void setDepth5CatNm(String depth5CatNm) {
		this.depth5CatNm = depth5CatNm;
	}
	public String getFullCatNm() {
		return fullCatNm;
	}
	public void setFullCatNm(String fullCatNm) {
		this.fullCatNm = fullCatNm;
	}
	public int getDispOrder() {
		return dispOrder;
	}
	public void setDispOrder(int dispOrder) {
		this.dispOrder = dispOrder;
	}
	public String getDispYn() {
		return dispYn;
	}
	public void setDispYn(String dispYn) {
		this.dispYn = dispYn;
	}
	public String getDeleteYn() {
		return deleteYn;
	}
	public void setDeleteYn(String deleteYn) {
		this.deleteYn = deleteYn;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}