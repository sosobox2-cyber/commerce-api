package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class ProductPriceDetail {
    private Long finalPrice;
    private Long originalPrice;
    private Integer discountPercentage;
    private Integer originalToDirectCouponDiscountPercentage;
    private Integer finalToDirectCouponDiscountPercentage;
    private Long directCouponAppliedFinalPrice;
    private List<String> directCouponAppliedTypes;
    private Integer originalToMaximumBenefitCouponPricePercentage;
    private Integer finalToMaximumBenefitCouponPricePercentage;
    private Long maximumBenefitCouponAppliedFinalPrice;
    private Integer originalToMaximumPotentialBenefitCouponPricePercentage;
    private Integer finalToMaximumPotentialBenefitCouponPricePercentage;
    private Long maximumPotentialBenefitCouponAppliedFinalPrice; 
    private Boolean isFirstOrderCouponPromotionPriceTarget;
    
	public Long getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(Long finalPrice) {
		this.finalPrice = finalPrice;
	}
	public Long getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(Long originalPrice) {
		this.originalPrice = originalPrice;
	}
	public int getDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(int discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	public int getOriginalToDirectCouponDiscountPercentage() {
		return originalToDirectCouponDiscountPercentage;
	}
	public void setOriginalToDirectCouponDiscountPercentage(int originalToDirectCouponDiscountPercentage) {
		this.originalToDirectCouponDiscountPercentage = originalToDirectCouponDiscountPercentage;
	}
	public int getFinalToDirectCouponDiscountPercentage() {
		return finalToDirectCouponDiscountPercentage;
	}
	public void setFinalToDirectCouponDiscountPercentage(int finalToDirectCouponDiscountPercentage) {
		this.finalToDirectCouponDiscountPercentage = finalToDirectCouponDiscountPercentage;
	}
	public Long getDirectCouponAppliedFinalPrice() {
		return directCouponAppliedFinalPrice;
	}
	public void setDirectCouponAppliedFinalPrice(Long directCouponAppliedFinalPrice) {
		this.directCouponAppliedFinalPrice = directCouponAppliedFinalPrice;
	}
	public List<String> getDirectCouponAppliedTypes() {
		return directCouponAppliedTypes;
	}
	public void setDirectCouponAppliedTypes(List<String> directCouponAppliedTypes) {
		this.directCouponAppliedTypes = directCouponAppliedTypes;
	}
	public int getOriginalToMaximumBenefitCouponPricePercentage() {
		return originalToMaximumBenefitCouponPricePercentage;
	}
	public void setOriginalToMaximumBenefitCouponPricePercentage(int originalToMaximumBenefitCouponPricePercentage) {
		this.originalToMaximumBenefitCouponPricePercentage = originalToMaximumBenefitCouponPricePercentage;
	}
	public int getFinalToMaximumBenefitCouponPricePercentage() {
		return finalToMaximumBenefitCouponPricePercentage;
	}
	public void setFinalToMaximumBenefitCouponPricePercentage(int finalToMaximumBenefitCouponPricePercentage) {
		this.finalToMaximumBenefitCouponPricePercentage = finalToMaximumBenefitCouponPricePercentage;
	}
	public Long getMaximumBenefitCouponAppliedFinalPrice() {
		return maximumBenefitCouponAppliedFinalPrice;
	}
	public void setMaximumBenefitCouponAppliedFinalPrice(Long maximumBenefitCouponAppliedFinalPrice) {
		this.maximumBenefitCouponAppliedFinalPrice = maximumBenefitCouponAppliedFinalPrice;
	}
	public int getOriginalToMaximumPotentialBenefitCouponPricePercentage() {
		return originalToMaximumPotentialBenefitCouponPricePercentage;
	}
	public void setOriginalToMaximumPotentialBenefitCouponPricePercentage(
			int originalToMaximumPotentialBenefitCouponPricePercentage) {
		this.originalToMaximumPotentialBenefitCouponPricePercentage = originalToMaximumPotentialBenefitCouponPricePercentage;
	}
	public int getFinalToMaximumPotentialBenefitCouponPricePercentage() {
		return finalToMaximumPotentialBenefitCouponPricePercentage;
	}
	public void setFinalToMaximumPotentialBenefitCouponPricePercentage(
			int finalToMaximumPotentialBenefitCouponPricePercentage) {
		this.finalToMaximumPotentialBenefitCouponPricePercentage = finalToMaximumPotentialBenefitCouponPricePercentage;
	}
	public Long getMaximumPotentialBenefitCouponAppliedFinalPrice() {
		return maximumPotentialBenefitCouponAppliedFinalPrice;
	}
	public void setMaximumPotentialBenefitCouponAppliedFinalPrice(Long maximumPotentialBenefitCouponAppliedFinalPrice) {
		this.maximumPotentialBenefitCouponAppliedFinalPrice = maximumPotentialBenefitCouponAppliedFinalPrice;
	}
	public Boolean getIsFirstOrderCouponPromotionPriceTarget() {
		return isFirstOrderCouponPromotionPriceTarget;
	}
	public void setIsFirstOrderCouponPromotionPriceTarget(Boolean isFirstOrderCouponPromotionPriceTarget) {
		this.isFirstOrderCouponPromotionPriceTarget = isFirstOrderCouponPromotionPriceTarget;
	}
}
