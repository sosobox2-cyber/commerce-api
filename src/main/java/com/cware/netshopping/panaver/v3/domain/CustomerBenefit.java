package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CustomerBenefit {
		
	// 판매자 즉시 할인 정책
	private ImmediateDiscountPolicy immediateDiscountPolicy;
	// 판매자 상품 구매 포인트 정책
	private PurchasePointPolicy purchasePointPolicy;
	// 판매자 상품 리뷰 포인트 정책
	private ReviewPointPolicy reviewPointPolicy;
	// 무이자 할부 정책
	private FreeInterestPolicy freeInterestPolicy;
	// 사은품 정책
	private GiftPolicy giftPolicy;
	// 판매자 복수 구매 할인 정책	
	private MultiPurchaseDiscountPolicy multiPurchaseDiscountPolicy;
	
	
	public ImmediateDiscountPolicy getImmediateDiscountPolicy() {
		return immediateDiscountPolicy;
	}

	public void setImmediateDiscountPolicy(ImmediateDiscountPolicy immediateDiscountPolicy) {
		this.immediateDiscountPolicy = immediateDiscountPolicy;
	}

	public PurchasePointPolicy getPurchasePointPolicy() {
		return purchasePointPolicy;
	}

	public void setPurchasePointPolicy(PurchasePointPolicy purchasePointPolicy) {
		this.purchasePointPolicy = purchasePointPolicy;
	}

	public ReviewPointPolicy getReviewPointPolicy() {
		return reviewPointPolicy;
	}

	public void setReviewPointPolicy(ReviewPointPolicy reviewPointPolicy) {
		this.reviewPointPolicy = reviewPointPolicy;
	}

	public FreeInterestPolicy getFreeInterestPolicy() {
		return freeInterestPolicy;
	}

	public void setFreeInterestPolicy(FreeInterestPolicy freeInterestPolicy) {
		this.freeInterestPolicy = freeInterestPolicy;
	}

	public GiftPolicy getGiftPolicy() {
		return giftPolicy;
	}

	public void setGiftPolicy(GiftPolicy giftPolicy) {
		this.giftPolicy = giftPolicy;
	}

	public MultiPurchaseDiscountPolicy getMultiPurchaseDiscountPolicy() {
		return multiPurchaseDiscountPolicy;
	}

	public void setMultiPurchaseDiscountPolicy(MultiPurchaseDiscountPolicy multiPurchaseDiscountPolicy) {
		this.multiPurchaseDiscountPolicy = multiPurchaseDiscountPolicy;
	}

	@Override
	public String toString() {
		return "CustomerBenefit [immediateDiscountPolicy=" + immediateDiscountPolicy +"purchasePointPolicy=" + purchasePointPolicy + "reviewPointPolicy=" + reviewPointPolicy + "freeInterestPolicy=" + freeInterestPolicy
				+ "giftPolicy=" + giftPolicy +"multiPurchaseDiscountPolicy=" + multiPurchaseDiscountPolicy +"]";
	}

}
