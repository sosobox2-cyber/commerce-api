package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ProductListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProductListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProductId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="InjectProductId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="CategoryId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SellerManagementCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StatusType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Display" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SalePrice" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="DiscountedPrice" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="StockQuantity" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="KnowledgeShoppingProductRegistration" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DeliveryType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DeliveryAttributeType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DeliveryFee" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ReturnFee" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ExchangeFee" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MultiPurchaseDiscount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Mileage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NMPMileage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductListType", propOrder = {
	    "productId",
	    "injectProductId",
	    "categoryId",
	    "name",
	    "sellerManagementCode",
	    "statusType",
	    "display",
	    "salePrice",
	    "discountedPrice",
	    "stockQuantity",
	    "knowledgeShoppingProductRegistration",
	    "deliveryType",
	    "deliveryAttributeType",
	    "deliveryFee",
	    "returnFee",
	    "exchangeFee",
	    "multiPurchaseDiscount",
	    "mileage",
	    "mMPMileage"
})
public class ProductListType {

    @XmlElement(name = "ProductId")
    protected String productId;
    @XmlElement(name = "InjectProductId")
    protected String injectProductId;
    @XmlElement(name = "CategoryId")
    protected String categoryId;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "SellerManagementCode")
    protected String sellerManagementCode;
    @XmlElement(name = "StatusType")
    protected String statusType;
    @XmlElement(name = "Display")
    protected String display;
    @XmlElement(name = "SalePrice")
    protected String salePrice;
    @XmlElement(name = "DiscountedPrice")
    protected String discountedPrice;
    @XmlElement(name = "StockQuantity")
    protected String stockQuantity;
    @XmlElement(name = "KnowledgeShoppingProductRegistration")
    protected String knowledgeShoppingProductRegistration;
    @XmlElement(name = "DeliveryType")
    protected String deliveryType;
    @XmlElement(name = "DeliveryAttributeType")
    protected String deliveryAttributeType;
    @XmlElement(name = "DeliveryFee")
    protected String deliveryFee;
    @XmlElement(name = "ReturnFee")
    protected String returnFee;
    @XmlElement(name = "ExchangeFee")
    protected String exchangeFee;
    @XmlElement(name = "MultiPurchaseDiscount")
    protected String multiPurchaseDiscount;
    @XmlElement(name = "Mileage")
    protected String mileage;
    @XmlElement(name = "NMPMileage")
    protected String mMPMileage;
    
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getInjectProductId() {
		return injectProductId;
	}
	public void setInjectProductId(String injectProductId) {
		this.injectProductId = injectProductId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSellerManagementCode() {
		return sellerManagementCode;
	}
	public void setSellerManagementCode(String sellerManagementCode) {
		this.sellerManagementCode = sellerManagementCode;
	}
	public String getStatusType() {
		return statusType;
	}
	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	public String getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(String discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	public String getStockQuantity() {
		return stockQuantity;
	}
	public void setStockQuantity(String stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	public String getKnowledgeShoppingProductRegistration() {
		return knowledgeShoppingProductRegistration;
	}
	public void setKnowledgeShoppingProductRegistration(
			String knowledgeShoppingProductRegistration) {
		this.knowledgeShoppingProductRegistration = knowledgeShoppingProductRegistration;
	}
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
	public String getDeliveryAttributeType() {
		return deliveryAttributeType;
	}
	public void setDeliveryAttributeType(String deliveryAttributeType) {
		this.deliveryAttributeType = deliveryAttributeType;
	}
	public String getDeliveryFee() {
		return deliveryFee;
	}
	public void setDeliveryFee(String deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
	public String getReturnFee() {
		return returnFee;
	}
	public void setReturnFee(String returnFee) {
		this.returnFee = returnFee;
	}
	public String getExchangeFee() {
		return exchangeFee;
	}
	public void setExchangeFee(String exchangeFee) {
		this.exchangeFee = exchangeFee;
	}
	public String getMultiPurchaseDiscount() {
		return multiPurchaseDiscount;
	}
	public void setMultiPurchaseDiscount(String multiPurchaseDiscount) {
		this.multiPurchaseDiscount = multiPurchaseDiscount;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getmMPMileage() {
		return mMPMileage;
	}
	public void setmMPMileage(String mMPMileage) {
		this.mMPMileage = mMPMileage;
	}
    
}
