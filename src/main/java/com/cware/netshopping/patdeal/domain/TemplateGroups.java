package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class TemplateGroups {
	
	// 추가할 템플릿 내역(등록 시 사용)
	@JsonProperty("templates")
	private List<Template> templateList;
	// 수정할 템플릿 리스트
	@JsonProperty("modifyTemplates")
	private List<Template> modifyTemplateList;
	
	// 배송비 템플릿 그룹 번호
	private String templateGroupNo;
	// 배송 불가능한 국가 리스트
	private List<Object> undeliverableCountries;
	// 사용할 지역별 추가배송비 번호
	private String areaFeeNo;
	// 배송비 템플릿 그룹 선택 유형 [ MAXIMUM_SELECTED: 최대부과, MINIMUM_SELECTED: 최소부과 ]
	private String groupDeliveryAmtType;
	// 그룹 명
	private String name;
	// 지역별 추가배송비 사용 여부
	private boolean usesAreaFee;
	// 배송비 결제 선불 여부
	private boolean prepaid;
	
	public List<Template> getTemplateList() {
		return templateList;
	}
	public void setTemplateList(List<Template> templateList) {
		this.templateList = templateList;
	}
	public List<Template> getModifyTemplateList() {
		return modifyTemplateList;
	}
	public void setModifyTemplateList(List<Template> modifyTemplateList) {
		this.modifyTemplateList = modifyTemplateList;
	}
	public String getTemplateGroupNo() {
		return templateGroupNo;
	}
	public void setTemplateGroupNo(String templateGroupNo) {
		this.templateGroupNo = templateGroupNo;
	}
	public List<Object> getUndeliverableCountries() {
		return undeliverableCountries;
	}
	public void setUndeliverableCountries(List<Object> undeliverableCountries) {
		this.undeliverableCountries = undeliverableCountries;
	}
	public String getAreaFeeNo() {
		return areaFeeNo;
	}
	public void setAreaFeeNo(String areaFeeNo) {
		this.areaFeeNo = areaFeeNo;
	}
	public String getGroupDeliveryAmtType() {
		return groupDeliveryAmtType;
	}
	public void setGroupDeliveryAmtType(String groupDeliveryAmtType) {
		this.groupDeliveryAmtType = groupDeliveryAmtType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isUsesAreaFee() {
		return usesAreaFee;
	}
	public void setUsesAreaFee(boolean usesAreaFee) {
		this.usesAreaFee = usesAreaFee;
	}
	public boolean isPrepaid() {
		return prepaid;
	}
	public void setPrepaid(boolean prepaid) {
		this.prepaid = prepaid;
	}
	
	@Override
	public String toString() {
		return "TemplateGroups [templateList=" + templateList + ", modifyTemplateList=" + modifyTemplateList
				+ ", templateGroupNo=" + templateGroupNo + ", undeliverableCountries=" + undeliverableCountries
				+ ", areaFeeNo=" + areaFeeNo + ", groupDeliveryAmtType=" + groupDeliveryAmtType + ", name=" + name
				+ ", usesAreaFee=" + usesAreaFee + ", prepaid=" + prepaid + "]";
	}
	
}
