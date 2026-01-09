package com.cware.netshopping.patdeal.domain;

import java.sql.Timestamp;
import java.util.List;

import com.cware.netshopping.patdeal.util.PaTdealComUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderProductOptions {
	
	private String orderProductOptionNo;
	private String deliveryTemplateNo;
	private String partnerNo;
	private String partnerName;
	private String deliveryPartnerNo;
	private String categoryNo;
	private String brandNo;
	private String mallOptionNo;
	private String mallAdditionalProductNo;
	private String stockNo;
	private String claimNo;
	private String releaseWarehouseNo;
	private String optionUseYn;
	private String optionName;
	private String optionValue;
	private String optionManagementCd;
	private String imageUrl;
	private String hsCode;
	
	private long orderCnt;
	private long originalOrderCnt;
	private long salePrice;
	private long immediateDiscountAmt;
	private long addPrice;
	private long additionalDiscountAmt;
	private long partnerChargeAmt;
	private long adjustedAmt;
	private long commissionRate;
	
	private String productType;
	private String orderOptionType;
	private String orderStatusType;
	private String claimStatusType;
	private String refundableYn;
	private String cancelableYn;
	private String returnableYn;
	private String shippingAreaType;
	private String deliveryInternationalYn;
	private String holdYn;
	private String exchangeYn;
	
	private String userInputText;// ?? 확인 필요, 명세서에서는 string으로 되어 있으나, api 응답값은 [] 형태로 줌
	
	private Timestamp statusChangeYmdt;
	private Timestamp orderYmdt;
	private Timestamp payYmdt;
	private Timestamp orderAcceptYmdt;
	private Timestamp releaseReadyYmdt;
	private Timestamp releaseYmdt;
	private Timestamp deliveryCompleteYmdt;
	private Timestamp buyConfirmYmdt;
	private Timestamp registerYmdt;
	private Timestamp updateYmdt;
	
	private String deliveryYn;
	private String originOrderProductOptionNo;
	private String extraJson;
	private String setOptionJson;
	private String sku;
	
	private long memberAccumulationRate;
	private long mallProductAccumulationRate;
	
	private String reservationYn;
	
	private long purchasePrice;
	
	private String freeGiftYn;
	private String extraManagementCd;

	private List<SetOptions> setOptions;
	private List<UserInputs> userInputs;
	
	public String getOrderProductOptionNo() {
		return orderProductOptionNo;
	}
	public void setOrderProductOptionNo(String orderProductOptionNo) {
		this.orderProductOptionNo = orderProductOptionNo;
	}
	public String getDeliveryTemplateNo() {
		return deliveryTemplateNo;
	}
	public void setDeliveryTemplateNo(String deliveryTemplateNo) {
		this.deliveryTemplateNo = deliveryTemplateNo;
	}
	public String getPartnerNo() {
		return partnerNo;
	}
	public void setPartnerNo(String partnerNo) {
		this.partnerNo = partnerNo;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public String getDeliveryPartnerNo() {
		return deliveryPartnerNo;
	}
	public void setDeliveryPartnerNo(String deliveryPartnerNo) {
		this.deliveryPartnerNo = deliveryPartnerNo;
	}
	public String getCategoryNo() {
		return categoryNo;
	}
	public void setCategoryNo(String categoryNo) {
		this.categoryNo = categoryNo;
	}
	public String getBrandNo() {
		return brandNo;
	}
	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
	public String getMallOptionNo() {
		return mallOptionNo;
	}
	public void setMallOptionNo(String mallOptionNo) {
		this.mallOptionNo = mallOptionNo;
	}
	public String getMallAdditionalProductNo() {
		return mallAdditionalProductNo;
	}
	public void setMallAdditionalProductNo(String mallAdditionalProductNo) {
		this.mallAdditionalProductNo = mallAdditionalProductNo;
	}
	public String getStockNo() {
		return stockNo;
	}
	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getReleaseWarehouseNo() {
		return releaseWarehouseNo;
	}
	public void setReleaseWarehouseNo(String releaseWarehouseNo) {
		this.releaseWarehouseNo = releaseWarehouseNo;
	}
	public String getOptionUseYn() {
		return optionUseYn;
	}
	public void setOptionUseYn(String optionUseYn) {
		this.optionUseYn = optionUseYn;
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
	public String getOptionManagementCd() {
		return optionManagementCd;
	}
	public void setOptionManagementCd(String optionManagementCd) {
		this.optionManagementCd = optionManagementCd;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getHsCode() {
		return hsCode;
	}
	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}
	public long getOrderCnt() {
		return orderCnt;
	}
	public void setOrderCnt(long orderCnt) {
		this.orderCnt = orderCnt;
	}
	public long getOriginalOrderCnt() {
		return originalOrderCnt;
	}
	public void setOriginalOrderCnt(long originalOrderCnt) {
		this.originalOrderCnt = originalOrderCnt;
	}
	public long getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(long salePrice) {
		this.salePrice = salePrice;
	}
	public long getImmediateDiscountAmt() {
		return immediateDiscountAmt;
	}
	public void setImmediateDiscountAmt(long immediateDiscountAmt) {
		this.immediateDiscountAmt = immediateDiscountAmt;
	}
	public long getAddPrice() {
		return addPrice;
	}
	public void setAddPrice(long addPrice) {
		this.addPrice = addPrice;
	}
	public long getAdditionalDiscountAmt() {
		return additionalDiscountAmt;
	}
	public void setAdditionalDiscountAmt(long additionalDiscountAmt) {
		this.additionalDiscountAmt = additionalDiscountAmt;
	}
	public long getPartnerChargeAmt() {
		return partnerChargeAmt;
	}
	public void setPartnerChargeAmt(long partnerChargeAmt) {
		this.partnerChargeAmt = partnerChargeAmt;
	}
	public long getAdjustedAmt() {
		return adjustedAmt;
	}
	public void setAdjustedAmt(long adjustedAmt) {
		this.adjustedAmt = adjustedAmt;
	}
	public long getCommissionRate() {
		return commissionRate;
	}
	public void setCommissionRate(long commissionRate) {
		this.commissionRate = commissionRate;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getOrderOptionType() {
		return orderOptionType;
	}
	public void setOrderOptionType(String orderOptionType) {
		this.orderOptionType = orderOptionType;
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
	public String getRefundableYn() {
		return refundableYn;
	}
	public void setRefundableYn(String refundableYn) {
		this.refundableYn = refundableYn;
	}
	public String getCancelableYn() {
		return cancelableYn;
	}
	public void setCancelableYn(String cancelableYn) {
		this.cancelableYn = cancelableYn;
	}
	public String getReturnableYn() {
		return returnableYn;
	}
	public void setReturnableYn(String returnableYn) {
		this.returnableYn = returnableYn;
	}
	public String getShippingAreaType() {
		return shippingAreaType;
	}
	public void setShippingAreaType(String shippingAreaType) {
		this.shippingAreaType = shippingAreaType;
	}
	public String getDeliveryInternationalYn() {
		return deliveryInternationalYn;
	}
	public void setDeliveryInternationalYn(String deliveryInternationalYn) {
		this.deliveryInternationalYn = deliveryInternationalYn;
	}
	public String getHoldYn() {
		return holdYn;
	}
	public void setHoldYn(String holdYn) {
		this.holdYn = holdYn;
	}
	public String getExchangeYn() {
		return exchangeYn;
	}
	public void setExchangeYn(String exchangeYn) {
		this.exchangeYn = exchangeYn;
	}
	public String getUserInputText() {
		return userInputText;
	}
	public void setUserInputText(String userInputText) {
		this.userInputText = userInputText;
	}
	public Timestamp getStatusChangeYmdt() {
		return statusChangeYmdt;
	}
	public void setStatusChangeYmdt(String statusChangeYmdt) {
		
		this.statusChangeYmdt = PaTdealComUtil.convertStrToTimestamp(statusChangeYmdt); //string 형태 날짜형태 변환 
	}
	public Timestamp getOrderYmdt() {
		return orderYmdt;
	}
	public void setOrderYmdt(String orderYmdt) {
		this.orderYmdt = PaTdealComUtil.convertStrToTimestamp(orderYmdt);//string 형태 날짜형태 변환 
	}
	public Timestamp getPayYmdt() {
		return payYmdt;
	}
	public void setPayYmdt(String payYmdt) {
		this.payYmdt = PaTdealComUtil.convertStrToTimestamp(payYmdt);//string 형태 날짜형태 변환 
	}
	public Timestamp getOrderAcceptYmdt() {
		return orderAcceptYmdt;
	}
	public void setOrderAcceptYmdt(String orderAcceptYmdt) {
		this.orderAcceptYmdt = PaTdealComUtil.convertStrToTimestamp(orderAcceptYmdt);//string 형태 날짜형태 변환 
	}
	public Timestamp getReleaseReadyYmdt() {
		return releaseReadyYmdt;
	}
	public void setReleaseReadyYmdt(String releaseReadyYmdt) {
		this.releaseReadyYmdt = PaTdealComUtil.convertStrToTimestamp(releaseReadyYmdt);//string 형태 날짜형태 변환 
	}
	public Timestamp getReleaseYmdt() {
		return releaseYmdt;
	}
	public void setReleaseYmdt(String releaseYmdt) {
		this.releaseYmdt = PaTdealComUtil.convertStrToTimestamp(releaseYmdt);//string 형태 날짜형태 변환 
	}
	public Timestamp getDeliveryCompleteYmdt() {
		return deliveryCompleteYmdt;
	}
	public void setDeliveryCompleteYmdt(String deliveryCompleteYmdt) {
		this.deliveryCompleteYmdt = PaTdealComUtil.convertStrToTimestamp(deliveryCompleteYmdt);//string 형태 날짜형태 변환 
	}
	public Timestamp getBuyConfirmYmdt() {
		return buyConfirmYmdt;
	}
	public void setBuyConfirmYmdt(String buyConfirmYmdt) {
		this.buyConfirmYmdt = PaTdealComUtil.convertStrToTimestamp(buyConfirmYmdt);//string 형태 날짜형태 변환 
	}
	public Timestamp getRegisterYmdt() {
		return registerYmdt;
	}
	public void setRegisterYmdt(String registerYmdt) {
		this.registerYmdt = PaTdealComUtil.convertStrToTimestamp(registerYmdt);//string 형태 날짜형태 변환 
	}
	public Timestamp getUpdateYmdt() {
		return updateYmdt;
	}
	public void setUpdateYmdt(String updateYmdt) {
		this.updateYmdt = PaTdealComUtil.convertStrToTimestamp(updateYmdt);//string 형태 날짜형태 변환 
	}
	public String getDeliveryYn() {
		return deliveryYn;
	}
	public void setDeliveryYn(String deliveryYn) {
		this.deliveryYn = deliveryYn;
	}
	public String getOriginOrderProductOptionNo() {
		return originOrderProductOptionNo;
	}
	public void setOriginOrderProductOptionNo(String originOrderProductOptionNo) {
		this.originOrderProductOptionNo = originOrderProductOptionNo;
	}
	public String getExtraJson() {
		return extraJson;
	}
	public void setExtraJson(String extraJson) {
		this.extraJson = extraJson;
	}
	public String getSetOptionJson() {
		return setOptionJson;
	}
	public void setSetOptionJson(String setOptionJson) {
		this.setOptionJson = setOptionJson;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public long getMemberAccumulationRate() {
		return memberAccumulationRate;
	}
	public void setMemberAccumulationRate(long memberAccumulationRate) {
		this.memberAccumulationRate = memberAccumulationRate;
	}
	public long getMallProductAccumulationRate() {
		return mallProductAccumulationRate;
	}
	public void setMallProductAccumulationRate(long mallProductAccumulationRate) {
		this.mallProductAccumulationRate = mallProductAccumulationRate;
	}
	public String getReservationYn() {
		return reservationYn;
	}
	public void setReservationYn(String reservationYn) {
		this.reservationYn = reservationYn;
	}
	public long getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(long purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getFreeGiftYn() {
		return freeGiftYn;
	}
	public void setFreeGiftYn(String freeGiftYn) {
		this.freeGiftYn = freeGiftYn;
	}
	public String getExtraManagementCd() {
		return extraManagementCd;
	}
	public void setExtraManagementCd(String extraManagementCd) {
		this.extraManagementCd = extraManagementCd;
	}
	public List<SetOptions> getSetOptions() {
		return setOptions;
	}
	public void setSetOptions(List<SetOptions> setOptions) {
		this.setOptions = setOptions;
	}
	public List<UserInputs> getUserInputs() {
		return userInputs;
	}
	public void setUserInputs(List<UserInputs> userInputs) {
		this.userInputs = userInputs;
	}
	
	@Override
	public String toString() {
		return "OrderProductOptions [orderProductOptionNo=" + orderProductOptionNo + ", deliveryTemplateNo="
				+ deliveryTemplateNo + ", partnerNo=" + partnerNo + ", partnerName=" + partnerName
				+ ", deliveryPartnerNo=" + deliveryPartnerNo + ", categoryNo=" + categoryNo + ", brandNo=" + brandNo
				+ ", mallOptionNo=" + mallOptionNo + ", mallAdditionalProductNo=" + mallAdditionalProductNo
				+ ", stockNo=" + stockNo + ", claimNo=" + claimNo + ", releaseWarehouseNo=" + releaseWarehouseNo
				+ ", optionUseYn=" + optionUseYn + ", optionName=" + optionName + ", optionValue=" + optionValue
				+ ", optionManagementCd=" + optionManagementCd + ", imageUrl=" + imageUrl + ", hsCode=" + hsCode
				+ ", orderCnt=" + orderCnt + ", originalOrderCnt=" + originalOrderCnt + ", salePrice=" + salePrice
				+ ", immediateDiscountAmt=" + immediateDiscountAmt + ", addPrice=" + addPrice
				+ ", additionalDiscountAmt=" + additionalDiscountAmt + ", partnerChargeAmt=" + partnerChargeAmt
				+ ", adjustedAmt=" + adjustedAmt + ", commissionRate=" + commissionRate + ", productType=" + productType
				+ ", orderOptionType=" + orderOptionType + ", orderStatusType=" + orderStatusType + ", claimStatusType="
				+ claimStatusType + ", refundableYn=" + refundableYn + ", cancelableYn=" + cancelableYn
				+ ", returnableYn=" + returnableYn + ", shippingAreaType=" + shippingAreaType
				+ ", deliveryInternationalYn=" + deliveryInternationalYn + ", holdYn=" + holdYn + ", exchangeYn="
				+ exchangeYn + ", userInputText=" + userInputText + ", statusChangeYmdt=" + statusChangeYmdt
				+ ", orderYmdt=" + orderYmdt + ", payYmdt=" + payYmdt + ", orderAcceptYmdt=" + orderAcceptYmdt
				+ ", releaseReadyYmdt=" + releaseReadyYmdt + ", releaseYmdt=" + releaseYmdt + ", deliveryCompleteYmdt="
				+ deliveryCompleteYmdt + ", buyConfirmYmdt=" + buyConfirmYmdt + ", registerYmdt=" + registerYmdt
				+ ", updateYmdt=" + updateYmdt + ", deliveryYn=" + deliveryYn + ", originOrderProductOptionNo="
				+ originOrderProductOptionNo + ", extraJson=" + extraJson + ", setOptionJson=" + setOptionJson
				+ ", sku=" + sku + ", memberAccumulationRate=" + memberAccumulationRate
				+ ", mallProductAccumulationRate=" + mallProductAccumulationRate + ", reservationYn=" + reservationYn
				+ ", purchasePrice=" + purchasePrice + ", freeGiftYn=" + freeGiftYn + ", extraManagementCd="
				+ extraManagementCd + ", setOptions=" + setOptions + ", userInputs=" + userInputs + "]";
	}
	
	
}
