package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Pacopnorderitemlist extends AbstractModel{
	private static final long serialVersionUID = 1L;
	
	private String shipmentBoxId;
	private String orderId;
	private String itemSeq;
	private String vendorItemPackageId;
	private String vendorItemPackageName;
	private String productId;
	private String vendorItemId;
	private String vendorItemName;
	private String externalVendorSkuCode;
	private String etcInfoHeader;
	private String etcInfoValue;
	private String etcInfoValues;
	private String sellerProductId;
	private String sellerProductName;
	private String sellerProductItemName;
	private String firstSellerProductItemName;
	private String extraProperties;
	private String pricingBadge;
	private String usedProduct;
	private String deliveryChargeTypeName;
	private String canceled;
	
	private long shippingCount;
	private long salesPrice;
	private long orderPrice;
	private long discountPrice;
	private long cancelCount;
	private long holdCountForCancel;
	
	private Timestamp estimatedShippingDate;
	private Timestamp plannedShippingDate;
	private Timestamp invoiceNumberUploadDate;
	private Timestamp confirmDate;
	
	private long instantCouponDiscount;//즉시할인쿠폰
	
	public String getShipmentBoxId() {
		return shipmentBoxId;
	}
	public void setShipmentBoxId(String shipmentBoxId) {
		this.shipmentBoxId = shipmentBoxId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getItemSeq() {
		return itemSeq;
	}
	public void setItemSeq(String itemSeq) {
		this.itemSeq = itemSeq;
	}
	public String getVendorItemPackageName() {
		return vendorItemPackageName;
	}
	public void setVendorItemPackageName(String vendorItemPackageName) {
		this.vendorItemPackageName = vendorItemPackageName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getVendorItemId() {
		return vendorItemId;
	}
	public void setVendorItemId(String vendorItemId) {
		this.vendorItemId = vendorItemId;
	}
	public String getVendorItemName() {
		return vendorItemName;
	}
	public void setVendorItemName(String vendorItemName) {
		this.vendorItemName = vendorItemName;
	}
	public String getExternalVendorSkuCode() {
		return externalVendorSkuCode;
	}
	public void setExternalVendorSkuCode(String externalVendorSkuCode) {
		this.externalVendorSkuCode = externalVendorSkuCode;
	}
	public String getEtcInfoHeader() {
		return etcInfoHeader;
	}
	public void setEtcInfoHeader(String etcInfoHeader) {
		this.etcInfoHeader = etcInfoHeader;
	}
	public String getEtcInfoValue() {
		return etcInfoValue;
	}
	public void setEtcInfoValue(String etcInfoValue) {
		this.etcInfoValue = etcInfoValue;
	}
	public String getEtcInfoValues() {
		return etcInfoValues;
	}
	public void setEtcInfoValues(String etcInfoValues) {
		this.etcInfoValues = etcInfoValues;
	}
	public String getSellerProductId() {
		return sellerProductId;
	}
	public void setSellerProductId(String sellerProductId) {
		this.sellerProductId = sellerProductId;
	}
	public String getSellerProductName() {
		return sellerProductName;
	}
	public void setSellerProductName(String sellerProductName) {
		this.sellerProductName = sellerProductName;
	}
	public String getSellerProductItemName() {
		return sellerProductItemName;
	}
	public void setSellerProductItemName(String sellerProductItemName) {
		this.sellerProductItemName = sellerProductItemName;
	}
	public String getFirstSellerProductItemName() {
		return firstSellerProductItemName;
	}
	public void setFirstSellerProductItemName(String firstSellerProductItemName) {
		this.firstSellerProductItemName = firstSellerProductItemName;
	}
	public String getExtraProperties() {
		return extraProperties;
	}
	public void setExtraProperties(String extraProperties) {
		this.extraProperties = extraProperties;
	}
	public String getPricingBadge() {
		return pricingBadge;
	}
	public void setPricingBadge(String pricingBadge) {
		this.pricingBadge = pricingBadge;
	}
	public String getUsedProduct() {
		return usedProduct;
	}
	public void setUsedProduct(String usedProduct) {
		this.usedProduct = usedProduct;
	}
	public String getDeliveryChargeTypeName() {
		return deliveryChargeTypeName;
	}
	public void setDeliveryChargeTypeName(String deliveryChargeTypeName) {
		this.deliveryChargeTypeName = deliveryChargeTypeName;
	}
	public String getCanceled() {
		return canceled;
	}
	public void setCanceled(String canceled) {
		this.canceled = canceled;
	}
	public String getVendorItemPackageId() {
		return vendorItemPackageId;
	}
	public void setVendorItemPackageId(String vendorItemPackageId) {
		this.vendorItemPackageId = vendorItemPackageId;
	}
	public long getShippingCount() {
		return shippingCount;
	}
	public void setShippingCount(long shippingCount) {
		this.shippingCount = shippingCount;
	}
	public long getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(long salesPrice) {
		this.salesPrice = salesPrice;
	}
	public long getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(long orderPrice) {
		this.orderPrice = orderPrice;
	}
	public long getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(long discountPrice) {
		this.discountPrice = discountPrice;
	}
	public long getCancelCount() {
		return cancelCount;
	}
	public void setCancelCount(long cancelCount) {
		this.cancelCount = cancelCount;
	}
	public long getHoldCountForCancel() {
		return holdCountForCancel;
	}
	public void setHoldCountForCancel(long holdCountForCancel) {
		this.holdCountForCancel = holdCountForCancel;
	}
	public Timestamp getEstimatedShippingDate() {
		return estimatedShippingDate;
	}
	public void setEstimatedShippingDate(Timestamp estimatedShippingDate) {
		this.estimatedShippingDate = estimatedShippingDate;
	}
	public Timestamp getPlannedShippingDate() {
		return plannedShippingDate;
	}
	public void setPlannedShippingDate(Timestamp plannedShippingDate) {
		this.plannedShippingDate = plannedShippingDate;
	}
	public Timestamp getInvoiceNumberUploadDate() {
		return invoiceNumberUploadDate;
	}
	public void setInvoiceNumberUploadDate(Timestamp invoiceNumberUploadDate) {
		this.invoiceNumberUploadDate = invoiceNumberUploadDate;
	}
	public Timestamp getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Timestamp confirmDate) {
		this.confirmDate = confirmDate;
	}
	public long getInstantCouponDiscount() {
		return instantCouponDiscount;
	}
	public void setInstantCouponDiscount(long instantCouponDiscount) {
		this.instantCouponDiscount = instantCouponDiscount;
	}
	
}
