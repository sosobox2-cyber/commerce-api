package com.cware.netshopping.paqeen.domain;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class ItemProposal {
	
	private Integer productItemProposalId;
    private String code;
    private Integer quantity;
    private List<String> optionNames;
    private Integer optionPrice;
    private String salesStatus;
    private String reifiedProductItemId;
    private String kstEstimateReleaseDate;
    private Integer estimateShipmentDays;
    private List<BundledProductItemInfo> bundledProductItemInfos;
    
	public Integer getProductItemProposalId() {
		return productItemProposalId;
	}
	public void setProductItemProposalId(Integer productItemProposalId) {
		this.productItemProposalId = productItemProposalId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public List<String> getOptionNames() {
		return optionNames;
	}
	public void setOptionNames(List<String> optionNames) {
		this.optionNames = optionNames;
	}
	public Integer getOptionPrice() {
		return optionPrice;
	}
	public void setOptionPrice(Integer optionPrice) {
		this.optionPrice = optionPrice;
	}
	public String getSalesStatus() {
		return salesStatus;
	}
	public void setSalesStatus(String salesStatus) {
		this.salesStatus = salesStatus;
	}
	public String getReifiedProductItemId() {
		return reifiedProductItemId;
	}
	public void setReifiedProductItemId(String reifiedProductItemId) {
		this.reifiedProductItemId = reifiedProductItemId;
	}
	public String getKstEstimateReleaseDate() {
		return kstEstimateReleaseDate;
	}
	public void setKstEstimateReleaseDate(String kstEstimateReleaseDate) {
		this.kstEstimateReleaseDate = kstEstimateReleaseDate;
	}
	public Integer getEstimateShipmentDays() {
		return estimateShipmentDays;
	}
	public void setEstimateShipmentDays(Integer estimateShipmentDays) {
		this.estimateShipmentDays = estimateShipmentDays;
	}
	public List<BundledProductItemInfo> getBundledProductItemInfos() {
		return bundledProductItemInfos;
	}
	public void setBundledProductItemInfos(List<BundledProductItemInfo> bundledProductItemInfos) {
		this.bundledProductItemInfos = bundledProductItemInfos;
	}
    
	
}
