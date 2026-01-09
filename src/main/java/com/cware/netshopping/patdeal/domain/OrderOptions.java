package com.cware.netshopping.patdeal.domain;

import java.sql.Timestamp;
import java.util.List;

import com.cware.netshopping.panaver.v3.domain.CustCounsel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderOptions {
	
	private long orderOptionNo;               
	private String orderNo;                   
	private long serviceNo;                 
	private long mallNo;                      
	private long memberNo;                    
	private String productName;               //상품명
	private String optionName;                //옵션명
	private String optionValue;          //옶션값      
//	private String purchaserInputs;           
	private long partnerNo;                   //파트너번호
	private int orderCnt;                  //주문수량
	private long shippingNo;                //배송번호
	private long originShippingNo;          //최초배송번호
	private String orderStatusType;          //주문상태   
	private String claimStatusType;           //클레임상태
	private long claimNo;                  //클레임번호 
	private long mallProductNo;             //몰상품번호
	private long mallOptionNo;              //몰옵션번호
	private long orderProductNo;            //주문상품번호
	private boolean usesOption;                //옵션사용여부
	private String payType;                   //결제수단
	private long accumulationAmt;          //적립금  
	private long salePrice;                 //상품판매가
	private long addPrice;                  //옵션가격
	private long immediateDiscountAmt;      //즉시할인금액
	private long additionalDiscountAmt;     //추가할인금액
	private long standardPrice;              //정상가
	private long discountedPrice;           //할인된가격
	private long immediateDiscountedPrice;  //즉시할인적ㅇ숑가
	private long buyPrice;                  //구매가 (즉시할 인 + 추 가할인 적용)
	private long adjustedAmt;               //조정 금 액 (수량 * 상품 가 격)
	private boolean exchangedRelease;         //교환상품여부
	private long originalOrderCnt;          //원주문개수
	private String orderRegisterType;         //주문등록타입
	private boolean requiresShipping;         //배송여부
	private String extraManagementCd;         //추가관리코드
	private String optionManagementCd;        //옵션 관 리 코드
	private String payTypeLabel;              //결제수단라벨
	private String optionNameForDisplay;      
	private boolean delivered;                
	private String productNameForDisplay;
	public long getOrderOptionNo() {
		return orderOptionNo;
	}
	public void setOrderOptionNo(long orderOptionNo) {
		this.orderOptionNo = orderOptionNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public long getServiceNo() {
		return serviceNo;
	}
	public void setServiceNo(long serviceNo) {
		this.serviceNo = serviceNo;
	}
	public long getMallNo() {
		return mallNo;
	}
	public void setMallNo(long mallNo) {
		this.mallNo = mallNo;
	}
	public long getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(long memberNo) {
		this.memberNo = memberNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	public long getPartnerNo() {
		return partnerNo;
	}
	public void setPartnerNo(long partnerNo) {
		this.partnerNo = partnerNo;
	}
	public int getOrderCnt() {
		return orderCnt;
	}
	public void setOrderCnt(int orderCnt) {
		this.orderCnt = orderCnt;
	}
	public long getShippingNo() {
		return shippingNo;
	}
	public void setShippingNo(long shippingNo) {
		this.shippingNo = shippingNo;
	}
	public long getOriginShippingNo() {
		return originShippingNo;
	}
	public void setOriginShippingNo(long originShippingNo) {
		this.originShippingNo = originShippingNo;
	}
	public String getOrderStatusType() {
		return orderStatusType;
	}
	public void setOrderStatusType(String orderStatusType) {
		this.orderStatusType = orderStatusType;
	}
	public String getClaimStatusType() {
		return claimStatusType;
	}
	public void setClaimStatusType(String claimStatusType) {
		this.claimStatusType = claimStatusType;
	}
	public long getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(long claimNo) {
		this.claimNo = claimNo;
	}
	public long getMallProductNo() {
		return mallProductNo;
	}
	public void setMallProductNo(long mallProductNo) {
		this.mallProductNo = mallProductNo;
	}
	public long getMallOptionNo() {
		return mallOptionNo;
	}
	public void setMallOptionNo(long mallOptionNo) {
		this.mallOptionNo = mallOptionNo;
	}
	public long getOrderProductNo() {
		return orderProductNo;
	}
	public void setOrderProductNo(long orderProductNo) {
		this.orderProductNo = orderProductNo;
	}
	public boolean isUsesOption() {
		return usesOption;
	}
	public void setUsesOption(boolean usesOption) {
		this.usesOption = usesOption;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public long getAccumulationAmt() {
		return accumulationAmt;
	}
	public void setAccumulationAmt(long accumulationAmt) {
		this.accumulationAmt = accumulationAmt;
	}
	public long getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(long salePrice) {
		this.salePrice = salePrice;
	}
	public long getAddPrice() {
		return addPrice;
	}
	public void setAddPrice(long addPrice) {
		this.addPrice = addPrice;
	}
	public long getImmediateDiscountAmt() {
		return immediateDiscountAmt;
	}
	public void setImmediateDiscountAmt(long immediateDiscountAmt) {
		this.immediateDiscountAmt = immediateDiscountAmt;
	}
	public long getAdditionalDiscountAmt() {
		return additionalDiscountAmt;
	}
	public void setAdditionalDiscountAmt(long additionalDiscountAmt) {
		this.additionalDiscountAmt = additionalDiscountAmt;
	}
	public long getStandardPrice() {
		return standardPrice;
	}
	public void setStandardPrice(long standardPrice) {
		this.standardPrice = standardPrice;
	}
	public long getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(long discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	public long getImmediateDiscountedPrice() {
		return immediateDiscountedPrice;
	}
	public void setImmediateDiscountedPrice(long immediateDiscountedPrice) {
		this.immediateDiscountedPrice = immediateDiscountedPrice;
	}
	public long getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(long buyPrice) {
		this.buyPrice = buyPrice;
	}
	public long getAdjustedAmt() {
		return adjustedAmt;
	}
	public void setAdjustedAmt(long adjustedAmt) {
		this.adjustedAmt = adjustedAmt;
	}
	public boolean isExchangedRelease() {
		return exchangedRelease;
	}
	public void setExchangedRelease(boolean exchangedRelease) {
		this.exchangedRelease = exchangedRelease;
	}
	public long getOriginalOrderCnt() {
		return originalOrderCnt;
	}
	public void setOriginalOrderCnt(long originalOrderCnt) {
		this.originalOrderCnt = originalOrderCnt;
	}
	public String getOrderRegisterType() {
		return orderRegisterType;
	}
	public void setOrderRegisterType(String orderRegisterType) {
		this.orderRegisterType = orderRegisterType;
	}
	public boolean isRequiresShipping() {
		return requiresShipping;
	}
	public void setRequiresShipping(boolean requiresShipping) {
		this.requiresShipping = requiresShipping;
	}
	public String getExtraManagementCd() {
		return extraManagementCd;
	}
	public void setExtraManagementCd(String extraManagementCd) {
		this.extraManagementCd = extraManagementCd;
	}
	public String getOptionManagementCd() {
		return optionManagementCd;
	}
	public void setOptionManagementCd(String optionManagementCd) {
		this.optionManagementCd = optionManagementCd;
	}
	public String getPayTypeLabel() {
		return payTypeLabel;
	}
	public void setPayTypeLabel(String payTypeLabel) {
		this.payTypeLabel = payTypeLabel;
	}
	public String getOptionNameForDisplay() {
		return optionNameForDisplay;
	}
	public void setOptionNameForDisplay(String optionNameForDisplay) {
		this.optionNameForDisplay = optionNameForDisplay;
	}
	public boolean isDelivered() {
		return delivered;
	}
	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}
	public String getProductNameForDisplay() {
		return productNameForDisplay;
	}
	public void setProductNameForDisplay(String productNameForDisplay) {
		this.productNameForDisplay = productNameForDisplay;
	}     

}
