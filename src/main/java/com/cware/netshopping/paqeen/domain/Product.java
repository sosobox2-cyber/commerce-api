package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Product {
	
    private Integer productProposalId;
    private String reifiedProductId;
    private String title;
    private Price price;
    private String mallId;
    private String brandCode;
    private String mallProductCode;
    private String categoryId;
    private String leafCategoryId;
    private List<String> imageUrls;
    private ThumbnailLabel thumbnailLabel;
    private String descriptionPageHtml;
    private String announcementV2;
    private List<Measurement> measurements;
    private List<Classification> classifications;
    private String salesStatus;
    private List<String> optionTitles;
    private List<ItemProposal> itemProposals;
    private String managementClient;
    private Integer overrodePolicyTargetId;
    private DeliveryPolicy deliveryPolicy;
    
    @JsonProperty("isBundledProduct")
    private boolean isBundledProduct;
    
    @JsonProperty("isBundleTargetProduct")
    private boolean isBundleTargetProduct;
    
    private List<String> productIdsToBundle;
    private String optionsCompositionInfo;
    private Integer maxQuantityLimit;
    private String maxQuantityLimitType;
    
    @JsonProperty("isSample")
    private boolean isSample;
    @JsonProperty("isContestProduct")
    private boolean isContestProduct;
    

    // Getter 및 Setter 메서드
    public Integer getProductProposalId() {
        return productProposalId;
    }

    public void setProductProposalId(Integer productProposalId) {
        this.productProposalId = productProposalId;
    }

    public String getReifiedProductId() {
        return reifiedProductId;
    }

    public void setReifiedProductId(String reifiedProductId) {
        this.reifiedProductId = reifiedProductId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getMallId() {
        return mallId;
    }

    public void setMallId(String mallId) {
        this.mallId = mallId;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getMallProductCode() {
        return mallProductCode;
    }

    public void setMallProductCode(String mallProductCode) {
        this.mallProductCode = mallProductCode;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public ThumbnailLabel getThumbnailLabel() {
        return thumbnailLabel;
    }

    public void setThumbnailLabel(ThumbnailLabel thumbnailLabel) {
        this.thumbnailLabel = thumbnailLabel;
    }

    public String getDescriptionPageHtml() {
        return descriptionPageHtml;
    }

    public void setDescriptionPageHtml(String descriptionPageHtml) {
        this.descriptionPageHtml = descriptionPageHtml;
    }

    public String getAnnouncementV2() {
        return announcementV2;
    }

    public void setAnnouncementV2(String announcementV2) {
        this.announcementV2 = announcementV2;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public List<Classification> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<Classification> classifications) {
        this.classifications = classifications;
    }

    public String getSalesStatus() {
        return salesStatus;
    }

    public void setSalesStatus(String salesStatus) {
        this.salesStatus = salesStatus;
    }

    public List<String> getOptionTitles() {
        return optionTitles;
    }

    public void setOptionTitles(List<String> optionTitles) {
        this.optionTitles = optionTitles;
    }

    public List<ItemProposal> getItemProposals() {
        return itemProposals;
    }

    public void setItemProposals(List<ItemProposal> itemProposals) {
        this.itemProposals = itemProposals;
    }

    public String getManagementClient() {
        return managementClient;
    }

    public void setManagementClient(String managementClient) {
        this.managementClient = managementClient;
    }

    public Integer getOverrodePolicyTargetId() {
        return overrodePolicyTargetId;
    }

    public void setOverrodePolicyTargetId(Integer overrodePolicyTargetId) {
        this.overrodePolicyTargetId = overrodePolicyTargetId;
    }

    @JsonProperty("isBundledProduct")
    public boolean isBundledProduct() {
        return isBundledProduct;
    }

    @JsonProperty("isBundledProduct")
    public void setBundledProduct(boolean isBundledProduct) {
        this.isBundledProduct = isBundledProduct;
    }

    @JsonProperty("isBundleTargetProduct")
    public boolean isBundleTargetProduct() {
        return isBundleTargetProduct;
    }

    @JsonProperty("isBundleTargetProduct")
    public void setBundleTargetProduct(boolean isBundleTargetProduct) {
        this.isBundleTargetProduct = isBundleTargetProduct;
    }

    public List<String> getProductIdsToBundle() {
        return productIdsToBundle;
    }

    public void setProductIdsToBundle(List<String> productIdsToBundle) {
        this.productIdsToBundle = productIdsToBundle;
    }

    public String getOptionsCompositionInfo() {
        return optionsCompositionInfo;
    }

    public void setOptionsCompositionInfo(String optionsCompositionInfo) {
        this.optionsCompositionInfo = optionsCompositionInfo;
    }

    public Integer getMaxQuantityLimit() {
        return maxQuantityLimit;
    }

    public void setMaxQuantityLimit(Integer maxQuantityLimit) {
        this.maxQuantityLimit = maxQuantityLimit;
    }

    public String getMaxQuantityLimitType() {
        return maxQuantityLimitType;
    }

    public void setMaxQuantityLimitType(String maxQuantityLimitType) {
        this.maxQuantityLimitType = maxQuantityLimitType;
    }

    @JsonProperty("isSample")
    public boolean isSample() {
        return isSample;
    }

    @JsonProperty("isSample")
    public void setSample(boolean isSample) {
        this.isSample = isSample;
    }

    @JsonProperty("isContestProduct")
    public boolean isContestProduct() {
        return isContestProduct;
    }

    @JsonProperty("isContestProduct")
    public void setContestProduct(boolean isContestProduct) {
        this.isContestProduct = isContestProduct;
    }

	public DeliveryPolicy getDeliveryPolicy() {
		return deliveryPolicy;
	}

	public void setDeliveryPolicy(DeliveryPolicy deliveryPolicy) {
		this.deliveryPolicy = deliveryPolicy;
	}

	public String getLeafCategoryId() {
		return leafCategoryId;
	}

	public void setLeafCategoryId(String leafCategoryId) {
		this.leafCategoryId = leafCategoryId;
	}
}
