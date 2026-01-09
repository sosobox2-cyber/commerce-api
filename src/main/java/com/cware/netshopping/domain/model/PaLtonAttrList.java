package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

/**
 * 표준카테고리 조회 API : 속성유형 리스트
 * @author user
 *
 */
public class PaLtonAttrList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String stdCatId;
	private String attrPiType;
	private int attrId;
	private int prioRnk;
	
	public String getStdCatId() {
		return stdCatId;
	}
	public void setStdCatId(String stdCatId) {
		this.stdCatId = stdCatId;
	}
	public String getAttrPiType() {
		return attrPiType;
	}
	public void setAttrPiType(String attrPiType) {
		this.attrPiType = attrPiType;
	}
	public int getAttrId() {
		return attrId;
	}
	public void setAttrId(int attrId) {
		this.attrId = attrId;
	}
	public int getPrioRnk() {
		return prioRnk;
	}
	public void setPrioRnk(int prioRnk) {
		this.prioRnk = prioRnk;
	}
	
}
