package com.cware.netshopping.pa11st.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductMedical {

	// 의료기기 품목허가번호 (필수)
    @XmlElement(name = "MedicalKey")
	private String medicalKey;

	// 의료기기 판매업신고 기관 및 번호
    @XmlElement(name = "MedicalRetail")
	private String medicalRetail;

	// 의료기기사전광고심의번호
    @XmlElement(name = "MedicalAd")
	private String medicalAd;

	public String getMedicalKey() {
		return medicalKey;
	}

	public void setMedicalKey(String medicalKey) {
		this.medicalKey = medicalKey;
	}

	public String getMedicalRetail() {
		return medicalRetail;
	}

	public void setMedicalRetail(String medicalRetail) {
		this.medicalRetail = medicalRetail;
	}

	public String getMedicalAd() {
		return medicalAd;	
	}

	public void setMedicalAd(String medicalAd) {
		this.medicalAd = medicalAd;
	}

	@Override
	public String toString() {
		return "ProductMedical [MedicalKey=" + medicalKey + ", MedicalRetail=" + medicalRetail + ", MedicalAd="
				+ medicalAd + ", getMedicalKey()=" + getMedicalKey() + ", getMedicalRetail()=" + getMedicalRetail()
				+ ", getMedicalAd()=" + getMedicalAd() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

}
