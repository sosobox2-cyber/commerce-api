package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ProductGoods {
	
	private String productId;
    private String mallId;
    private String mallProductCode;
    private String mallName;
    private String url;
    private String imageUrl;
    private MultiResolutionImage multiResolutionImage;
    private String thumbnailUrl;
    private MultiResolutionThumbnail multiResolutionThumbnail;
    private Thumbnail thumbnail;
    private String name;
    private Long finalPrice;
    private Integer discountPercentage;
    private ProductPriceDetail productPriceDetail;
    private String brand;
    private Integer rating;
    private String salesStatus;
    private boolean isContestProduct;
    private Integer rank; 
    private String logoUrl;
    private String logo2xUrl;
    private String logo3xUrl;
    private Long originalPrice;
    private boolean display;
    private Integer reviewRatingAvg; 
    private Integer reviewCount;
    private Category category;
    private DeliveryPolicy deliveryPolicy;
    private Integer maxQuantityLimit; 
    private String maxQuantityLimitType; 
    private boolean isSample;
    
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getMallId() {
		return mallId;
	}
	public void setMallId(String mallId) {
		this.mallId = mallId;
	}
	public String getMallProductCode() {
		return mallProductCode;
	}
	public void setMallProductCode(String mallProductCode) {
		this.mallProductCode = mallProductCode;
	}
	public String getMallName() {
		return mallName;
	}
	public void setMallName(String mallName) {
		this.mallName = mallName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public MultiResolutionImage getMultiResolutionImage() {
		return multiResolutionImage;
	}
	public void setMultiResolutionImage(MultiResolutionImage multiResolutionImage) {
		this.multiResolutionImage = multiResolutionImage;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public MultiResolutionThumbnail getMultiResolutionThumbnail() {
		return multiResolutionThumbnail;
	}
	public void setMultiResolutionThumbnail(MultiResolutionThumbnail multiResolutionThumbnail) {
		this.multiResolutionThumbnail = multiResolutionThumbnail;
	}
	public Thumbnail getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(Thumbnail thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(Long finalPrice) {
		this.finalPrice = finalPrice;
	}
	public int getDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(int discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	public ProductPriceDetail getProductPriceDetail() {
		return productPriceDetail;
	}
	public void setProductPriceDetail(ProductPriceDetail productPriceDetail) {
		this.productPriceDetail = productPriceDetail;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getSalesStatus() {
		return salesStatus;
	}
	public void setSalesStatus(String salesStatus) {
		this.salesStatus = salesStatus;
	}
	public boolean isContestProduct() {
		return isContestProduct;
	}
	public void setContestProduct(boolean isContestProduct) {
		this.isContestProduct = isContestProduct;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getLogo2xUrl() {
		return logo2xUrl;
	}
	public void setLogo2xUrl(String logo2xUrl) {
		this.logo2xUrl = logo2xUrl;
	}
	public String getLogo3xUrl() {
		return logo3xUrl;
	}
	public void setLogo3xUrl(String logo3xUrl) {
		this.logo3xUrl = logo3xUrl;
	}
	public Long getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(Long originalPrice) {
		this.originalPrice = originalPrice;
	}
	public boolean isDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	public int getReviewRatingAvg() {
		return reviewRatingAvg;
	}
	public void setReviewRatingAvg(int reviewRatingAvg) {
		this.reviewRatingAvg = reviewRatingAvg;
	}
	public int getReviewCount() {
		return reviewCount;
	}
	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public DeliveryPolicy getDeliveryPolicy() {
		return deliveryPolicy;
	}
	public void setDeliveryPolicy(DeliveryPolicy deliveryPolicy) {
		this.deliveryPolicy = deliveryPolicy;
	}
	public int getMaxQuantityLimit() {
		return maxQuantityLimit;
	}
	public void setMaxQuantityLimit(int maxQuantityLimit) {
		this.maxQuantityLimit = maxQuantityLimit;
	}
	public String getMaxQuantityLimitType() {
		return maxQuantityLimitType;
	}
	public void setMaxQuantityLimitType(String maxQuantityLimitType) {
		this.maxQuantityLimitType = maxQuantityLimitType;
	}
	public boolean isSample() {
		return isSample;
	}
	public void setSample(boolean isSample) {
		this.isSample = isSample;
	}
    
}
