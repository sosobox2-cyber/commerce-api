package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaSsgDisplayCategory extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String dispCtgId;
	private String dispCtgNm;
	private String dispCtgClsCd;
	private String dispCtgClsNm;
	private String dispCtgPathNm;
	private String aplSiteNo;
	private String aplSiteNoNm;
	private String dispCtgLastLvlYn;
	private String dispYn;
	
	public String getDispCtgId() {
		return dispCtgId;
	}
	public void setDispCtgId(String dispCtgId) {
		this.dispCtgId = dispCtgId;
	}
	public String getDispCtgNm() {
		return dispCtgNm;
	}
	public void setDispCtgNm(String dispCtgNm) {
		this.dispCtgNm = dispCtgNm;
	}
	public String getDispCtgClsCd() {
		return dispCtgClsCd;
	}
	public void setDispCtgClsCd(String dispCtgClsCd) {
		this.dispCtgClsCd = dispCtgClsCd;
	}
	public String getDispCtgClsNm() {
		return dispCtgClsNm;
	}
	public void setDispCtgClsNm(String dispCtgClsNm) {
		this.dispCtgClsNm = dispCtgClsNm;
	}
	public String getDispCtgPathNm() {
		return dispCtgPathNm;
	}
	public void setDispCtgPathNm(String dispCtgPathNm) {
		this.dispCtgPathNm = dispCtgPathNm;
	}
	public String getAplSiteNo() {
		return aplSiteNo;
	}
	public void setAplSiteNo(String aplSiteNo) {
		this.aplSiteNo = aplSiteNo;
	}
	public String getAplSiteNoNm() {
		return aplSiteNoNm;
	}
	public void setAplSiteNoNm(String aplSiteNoNm) {
		this.aplSiteNoNm = aplSiteNoNm;
	}
	public String getDispCtgLastLvlYn() {
		return dispCtgLastLvlYn;
	}
	public void setDispCtgLastLvlYn(String dispCtgLastLvlYn) {
		this.dispCtgLastLvlYn = dispCtgLastLvlYn;
	}
	public String getDispYn() {
		return dispYn;
	}
	public void setDispYn(String dispYn) {
		this.dispYn = dispYn;
	}
}