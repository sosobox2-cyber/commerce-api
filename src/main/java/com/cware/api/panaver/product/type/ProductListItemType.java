
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ProductListItemType complex type에 대한 Java 클래스입니다.
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 * 
 * <pre>
 * &lt;complexType name="ProductListItemType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProductId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="InjectProductId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="CategoryId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SellerManagementCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="StatusType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Display" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SalePrice" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="DiscountedPrice" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="StockQuantity" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="KnowledgeShoppingProductRegistration" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DeliveryType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DeliveryAttributeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DeliveryFee" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="ReturnFee" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="ExchangeFee" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="DeliveryCompany" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MultiPurchaseDiscount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Mileage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NMPMileage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PurchaseReviewPoint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PremiumReviewPoint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AfterUseTextReviewPoint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AfterUsePhotoVideoReviewPoint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RegularCustomerPoint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FreeInterest" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Gift" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SaleStartDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SaleEndDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RegisteredDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ModifiedDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Event" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductListItemType", propOrder = {
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
    "deliveryCompany",
    "multiPurchaseDiscount",
    "mileage",
    "nmpMileage",
    "purchaseReviewPoint",
    "premiumReviewPoint",
    "afterUseTextReviewPoint",
    "afterUsePhotoVideoReviewPoint",
    "regularCustomerPoint",
    "freeInterest",
    "gift",
    "saleStartDate",
    "saleEndDate",
    "registeredDate",
    "modifiedDate",
    "event"
})
public class ProductListItemType {

    @XmlElement(name = "ProductId")
    protected Long productId;
    @XmlElement(name = "InjectProductId")
    protected Long injectProductId;
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
    protected Long salePrice;
    @XmlElement(name = "DiscountedPrice")
    protected Long discountedPrice;
    @XmlElement(name = "StockQuantity")
    protected Long stockQuantity;
    @XmlElement(name = "KnowledgeShoppingProductRegistration")
    protected String knowledgeShoppingProductRegistration;
    @XmlElement(name = "DeliveryType")
    protected String deliveryType;
    @XmlElement(name = "DeliveryAttributeType")
    protected String deliveryAttributeType;
    @XmlElement(name = "DeliveryFee")
    protected Integer deliveryFee;
    @XmlElement(name = "ReturnFee")
    protected Integer returnFee;
    @XmlElement(name = "ExchangeFee")
    protected Integer exchangeFee;
    @XmlElement(name = "DeliveryCompany")
    protected String deliveryCompany;
    @XmlElement(name = "MultiPurchaseDiscount")
    protected String multiPurchaseDiscount;
    @XmlElement(name = "Mileage")
    protected String mileage;
    @XmlElement(name = "NMPMileage")
    protected String nmpMileage;
    @XmlElement(name = "PurchaseReviewPoint")
    protected String purchaseReviewPoint;
    @XmlElement(name = "PremiumReviewPoint")
    protected String premiumReviewPoint;
    @XmlElement(name = "AfterUseTextReviewPoint")
    protected String afterUseTextReviewPoint;
    @XmlElement(name = "AfterUsePhotoVideoReviewPoint")
    protected String afterUsePhotoVideoReviewPoint;
    @XmlElement(name = "RegularCustomerPoint")
    protected String regularCustomerPoint;
    @XmlElement(name = "FreeInterest")
    protected String freeInterest;
    @XmlElement(name = "Gift")
    protected String gift;
    @XmlElement(name = "SaleStartDate")
    protected String saleStartDate;
    @XmlElement(name = "SaleEndDate")
    protected String saleEndDate;
    @XmlElement(name = "RegisteredDate")
    protected String registeredDate;
    @XmlElement(name = "ModifiedDate")
    protected String modifiedDate;
    @XmlElement(name = "Event")
    protected String event;

    /**
     * productId 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * productId 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setProductId(Long value) {
        this.productId = value;
    }

    /**
     * injectProductId 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getInjectProductId() {
        return injectProductId;
    }

    /**
     * injectProductId 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setInjectProductId(Long value) {
        this.injectProductId = value;
    }

    /**
     * categoryId 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * categoryId 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategoryId(String value) {
        this.categoryId = value;
    }

    /**
     * name 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * name 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * sellerManagementCode 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellerManagementCode() {
        return sellerManagementCode;
    }

    /**
     * sellerManagementCode 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellerManagementCode(String value) {
        this.sellerManagementCode = value;
    }

    /**
     * statusType 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusType() {
        return statusType;
    }

    /**
     * statusType 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusType(String value) {
        this.statusType = value;
    }

    /**
     * display 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplay() {
        return display;
    }

    /**
     * display 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplay(String value) {
        this.display = value;
    }

    /**
     * salePrice 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSalePrice() {
        return salePrice;
    }

    /**
     * salePrice 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSalePrice(Long value) {
        this.salePrice = value;
    }

    /**
     * discountedPrice 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDiscountedPrice() {
        return discountedPrice;
    }

    /**
     * discountedPrice 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDiscountedPrice(Long value) {
        this.discountedPrice = value;
    }

    /**
     * stockQuantity 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getStockQuantity() {
        return stockQuantity;
    }

    /**
     * stockQuantity 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setStockQuantity(Long value) {
        this.stockQuantity = value;
    }

    /**
     * knowledgeShoppingProductRegistration 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKnowledgeShoppingProductRegistration() {
        return knowledgeShoppingProductRegistration;
    }

    /**
     * knowledgeShoppingProductRegistration 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKnowledgeShoppingProductRegistration(String value) {
        this.knowledgeShoppingProductRegistration = value;
    }

    /**
     * deliveryType 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryType() {
        return deliveryType;
    }

    /**
     * deliveryType 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryType(String value) {
        this.deliveryType = value;
    }

    /**
     * deliveryAttributeType 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryAttributeType() {
        return deliveryAttributeType;
    }

    /**
     * deliveryAttributeType 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryAttributeType(String value) {
        this.deliveryAttributeType = value;
    }

    /**
     * deliveryFee 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDeliveryFee() {
        return deliveryFee;
    }

    /**
     * deliveryFee 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDeliveryFee(Integer value) {
        this.deliveryFee = value;
    }

    /**
     * returnFee 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getReturnFee() {
        return returnFee;
    }

    /**
     * returnFee 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setReturnFee(Integer value) {
        this.returnFee = value;
    }

    /**
     * exchangeFee 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getExchangeFee() {
        return exchangeFee;
    }

    /**
     * exchangeFee 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setExchangeFee(Integer value) {
        this.exchangeFee = value;
    }

    /**
     * deliveryCompany 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryCompany() {
        return deliveryCompany;
    }

    /**
     * deliveryCompany 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryCompany(String value) {
        this.deliveryCompany = value;
    }

    /**
     * multiPurchaseDiscount 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMultiPurchaseDiscount() {
        return multiPurchaseDiscount;
    }

    /**
     * multiPurchaseDiscount 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMultiPurchaseDiscount(String value) {
        this.multiPurchaseDiscount = value;
    }

    /**
     * mileage 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMileage() {
        return mileage;
    }

    /**
     * mileage 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMileage(String value) {
        this.mileage = value;
    }

    /**
     * nmpMileage 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNMPMileage() {
        return nmpMileage;
    }

    /**
     * nmpMileage 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNMPMileage(String value) {
        this.nmpMileage = value;
    }

    /**
     * purchaseReviewPoint 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurchaseReviewPoint() {
        return purchaseReviewPoint;
    }

    /**
     * purchaseReviewPoint 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurchaseReviewPoint(String value) {
        this.purchaseReviewPoint = value;
    }

    /**
     * premiumReviewPoint 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPremiumReviewPoint() {
        return premiumReviewPoint;
    }

    /**
     * premiumReviewPoint 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPremiumReviewPoint(String value) {
        this.premiumReviewPoint = value;
    }

    /**
     * afterUseTextReviewPoint 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAfterUseTextReviewPoint() {
        return afterUseTextReviewPoint;
    }

    /**
     * afterUseTextReviewPoint 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAfterUseTextReviewPoint(String value) {
        this.afterUseTextReviewPoint = value;
    }

    /**
     * afterUsePhotoVideoReviewPoint 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAfterUsePhotoVideoReviewPoint() {
        return afterUsePhotoVideoReviewPoint;
    }

    /**
     * afterUsePhotoVideoReviewPoint 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAfterUsePhotoVideoReviewPoint(String value) {
        this.afterUsePhotoVideoReviewPoint = value;
    }

    /**
     * regularCustomerPoint 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegularCustomerPoint() {
        return regularCustomerPoint;
    }

    /**
     * regularCustomerPoint 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegularCustomerPoint(String value) {
        this.regularCustomerPoint = value;
    }

    /**
     * freeInterest 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFreeInterest() {
        return freeInterest;
    }

    /**
     * freeInterest 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFreeInterest(String value) {
        this.freeInterest = value;
    }

    /**
     * gift 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGift() {
        return gift;
    }

    /**
     * gift 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGift(String value) {
        this.gift = value;
    }

    /**
     * saleStartDate 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaleStartDate() {
        return saleStartDate;
    }

    /**
     * saleStartDate 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaleStartDate(String value) {
        this.saleStartDate = value;
    }

    /**
     * saleEndDate 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaleEndDate() {
        return saleEndDate;
    }

    /**
     * saleEndDate 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaleEndDate(String value) {
        this.saleEndDate = value;
    }

    /**
     * registeredDate 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegisteredDate() {
        return registeredDate;
    }

    /**
     * registeredDate 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegisteredDate(String value) {
        this.registeredDate = value;
    }

    /**
     * modifiedDate 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModifiedDate() {
        return modifiedDate;
    }

    /**
     * modifiedDate 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModifiedDate(String value) {
        this.modifiedDate = value;
    }

    /**
     * event 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvent() {
        return event;
    }

    /**
     * event 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvent(String value) {
        this.event = value;
    }

}
