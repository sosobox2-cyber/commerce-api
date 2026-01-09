package com.cware.api.panaver.product.type;

import com.cware.framework.core.basic.AbstractModel;

public class PaCertificationVO extends AbstractModel {
	
	private static final long serialVersionUID = 1L; 
	
	private String categoryId;		// 카테고리id
	private String exceptCode;		// 제외 코드
	private String certiCode;		// 인증 코드
	
	private String certiCompanyYn;	// 인증상호 여부[1:필수/0:필수아님]
	private String certiCompany;	// 인증상호
	
	private String certiAgencyYn;	// 인증기관 여부[1:필수/0:필수아님]
	private String certiAgency;		// 인증기관

	private String certiNoYn;		// 인증번호 여부[1:필수/0:필수아님]
	private String certiNo;			// 인증번호
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getExceptCode() {
		return exceptCode;
	}
	public void setExceptCode(String exceptCode) {
		this.exceptCode = exceptCode;
	}
	public String getCertiCode() {
		return certiCode;
	}
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}
	public String getCertiCompanyYn() {
		return certiCompanyYn;
	}
	public void setCertiCompanyYn(String certiCompanyYn) {
		this.certiCompanyYn = certiCompanyYn;
	}
	public String getCertiCompany() {
		return certiCompany;
	}
	public void setCertiCompany(String certiCompany) {
		this.certiCompany = certiCompany;
	}
	public String getCertiAgencyYn() {
		return certiAgencyYn;
	}
	public void setCertiAgencyYn(String certiAgencyYn) {
		this.certiAgencyYn = certiAgencyYn;
	}
	public String getCertiAgency() {
		return certiAgency;
	}
	public void setCertiAgency(String certiAgency) {
		this.certiAgency = certiAgency;
	}
	public String getCertiNoYn() {
		return certiNoYn;
	}
	public void setCertiNoYn(String certiNoYn) {
		this.certiNoYn = certiNoYn;
	}
	public String getCertiNo() {
		return certiNo;
	}
	public void setCertiNo(String certiNo) {
		this.certiNo = certiNo;
	}
}