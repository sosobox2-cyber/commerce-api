
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>DeliveryType complex type에 대한 Java 클래스입니다.
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 * 
 * <pre>
 * &lt;complexType name="DeliveryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="BundleGroupAvailable" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="BundleGroupId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="VisitAddressId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="QuickServiceAreaList" type="{http://shopn.platform.nhncorp.com/}QuickServiceAreaListType" minOccurs="0"/&gt;
 *         &lt;element name="FeeType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="BaseFee" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="FreeConditionalAmount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="RepeatQuantity" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="SecondBaseQuantity" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="SecondExtraFee" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="ThirdBaseQuantity" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="ThirdExtraFee" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="PayType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AreaType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Area2ExtraFee" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="Area3ExtraFee" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="ReturnDeliveryCompanyPriority" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ReturnFee" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ExchangeFee" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="DeliveryInsurance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ShippingAddressId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="ReturnAddressId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="DifferentialFee" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="InstallationFee" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IndividualCustomUniqueCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AttributeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ExpectedDeliveryPeriodType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ExpectedDeliveryPeriodDirectInput" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TodayStockQuantity" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="CustomProductAfterOrderYn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="HopeDeliveryGroupUsable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="HopeDeliveryGroupId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="DeliveryCompany" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeliveryType", propOrder = {
    "type",
    "bundleGroupAvailable",
    "bundleGroupId",
    "visitAddressId",
    "quickServiceAreaList",
    "feeType",
    "baseFee",
    "freeConditionalAmount",
    "repeatQuantity",
    "secondBaseQuantity",
    "secondExtraFee",
    "thirdBaseQuantity",
    "thirdExtraFee",
    "payType",
    "areaType",
    "area2ExtraFee",
    "area3ExtraFee",
    "returnDeliveryCompanyPriority",
    "returnFee",
    "exchangeFee",
    "deliveryInsurance",
    "shippingAddressId",
    "returnAddressId",
    "differentialFee",
    "installationFee",
    "individualCustomUniqueCode",
    "attributeType",
    "expectedDeliveryPeriodType",
    "expectedDeliveryPeriodDirectInput",
    "todayStockQuantity",
    "customProductAfterOrderYn",
    "hopeDeliveryGroupUsable",
    "hopeDeliveryGroupId",
    "deliveryCompany"
})
public class DeliveryType {

    @XmlElement(name = "Type", required = true)
    protected String type;
    @XmlElement(name = "BundleGroupAvailable", required = true)
    protected String bundleGroupAvailable;
    @XmlElement(name = "BundleGroupId")
    protected Long bundleGroupId;
    @XmlElement(name = "VisitAddressId")
    protected Long visitAddressId;
    @XmlElement(name = "QuickServiceAreaList")
    protected QuickServiceAreaListType quickServiceAreaList;
    @XmlElement(name = "FeeType", required = true)
    protected String feeType;
    @XmlElement(name = "BaseFee")
    protected int baseFee;
    @XmlElement(name = "FreeConditionalAmount")
    protected Integer freeConditionalAmount;
    @XmlElement(name = "RepeatQuantity")
    protected Integer repeatQuantity;
    @XmlElement(name = "SecondBaseQuantity")
    protected Integer secondBaseQuantity;
    @XmlElement(name = "SecondExtraFee")
    protected Integer secondExtraFee;
    @XmlElement(name = "ThirdBaseQuantity")
    protected Integer thirdBaseQuantity;
    @XmlElement(name = "ThirdExtraFee")
    protected Integer thirdExtraFee;
    @XmlElement(name = "PayType")
    protected String payType;
    @XmlElement(name = "AreaType")
    protected String areaType;
    @XmlElement(name = "Area2ExtraFee")
    protected Integer area2ExtraFee;
    @XmlElement(name = "Area3ExtraFee")
    protected Integer area3ExtraFee;
    @XmlElement(name = "ReturnDeliveryCompanyPriority", required = true)
    protected String returnDeliveryCompanyPriority;
    @XmlElement(name = "ReturnFee")
    protected int returnFee;
    @XmlElement(name = "ExchangeFee")
    protected int exchangeFee;
    @XmlElement(name = "DeliveryInsurance")
    protected String deliveryInsurance;
    @XmlElement(name = "ShippingAddressId")
    protected Long shippingAddressId;
    @XmlElement(name = "ReturnAddressId")
    protected Long returnAddressId;
    @XmlElement(name = "DifferentialFee")
    protected String differentialFee;
    @XmlElement(name = "InstallationFee")
    protected String installationFee;
    @XmlElement(name = "IndividualCustomUniqueCode")
    protected String individualCustomUniqueCode;
    @XmlElement(name = "AttributeType")
    protected String attributeType;
    @XmlElement(name = "ExpectedDeliveryPeriodType")
    protected String expectedDeliveryPeriodType;
    @XmlElement(name = "ExpectedDeliveryPeriodDirectInput")
    protected String expectedDeliveryPeriodDirectInput;
    @XmlElement(name = "TodayStockQuantity")
    protected Integer todayStockQuantity;
    @XmlElement(name = "CustomProductAfterOrderYn")
    protected String customProductAfterOrderYn;
    @XmlElement(name = "HopeDeliveryGroupUsable")
    protected String hopeDeliveryGroupUsable;
    @XmlElement(name = "HopeDeliveryGroupId")
    protected Long hopeDeliveryGroupId;
    @XmlElement(name = "DeliveryCompany")
    protected String deliveryCompany;

    /**
     * type 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * type 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * bundleGroupAvailable 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBundleGroupAvailable() {
        return bundleGroupAvailable;
    }

    /**
     * bundleGroupAvailable 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBundleGroupAvailable(String value) {
        this.bundleGroupAvailable = value;
    }

    /**
     * bundleGroupId 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBundleGroupId() {
        return bundleGroupId;
    }

    /**
     * bundleGroupId 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBundleGroupId(Long value) {
        this.bundleGroupId = value;
    }

    /**
     * visitAddressId 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getVisitAddressId() {
        return visitAddressId;
    }

    /**
     * visitAddressId 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setVisitAddressId(Long value) {
        this.visitAddressId = value;
    }

    /**
     * quickServiceAreaList 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link QuickServiceAreaListType }
     *     
     */
    public QuickServiceAreaListType getQuickServiceAreaList() {
        return quickServiceAreaList;
    }

    /**
     * quickServiceAreaList 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link QuickServiceAreaListType }
     *     
     */
    public void setQuickServiceAreaList(QuickServiceAreaListType value) {
        this.quickServiceAreaList = value;
    }

    /**
     * feeType 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFeeType() {
        return feeType;
    }

    /**
     * feeType 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFeeType(String value) {
        this.feeType = value;
    }

    /**
     * baseFee 속성의 값을 가져옵니다.
     * 
     */
    public int getBaseFee() {
        return baseFee;
    }

    /**
     * baseFee 속성의 값을 설정합니다.
     * 
     */
    public void setBaseFee(int value) {
        this.baseFee = value;
    }

    /**
     * freeConditionalAmount 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFreeConditionalAmount() {
        return freeConditionalAmount;
    }

    /**
     * freeConditionalAmount 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFreeConditionalAmount(Integer value) {
        this.freeConditionalAmount = value;
    }

    /**
     * repeatQuantity 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRepeatQuantity() {
        return repeatQuantity;
    }

    /**
     * repeatQuantity 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRepeatQuantity(Integer value) {
        this.repeatQuantity = value;
    }

    /**
     * secondBaseQuantity 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSecondBaseQuantity() {
        return secondBaseQuantity;
    }

    /**
     * secondBaseQuantity 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSecondBaseQuantity(Integer value) {
        this.secondBaseQuantity = value;
    }

    /**
     * secondExtraFee 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSecondExtraFee() {
        return secondExtraFee;
    }

    /**
     * secondExtraFee 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSecondExtraFee(Integer value) {
        this.secondExtraFee = value;
    }

    /**
     * thirdBaseQuantity 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getThirdBaseQuantity() {
        return thirdBaseQuantity;
    }

    /**
     * thirdBaseQuantity 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setThirdBaseQuantity(Integer value) {
        this.thirdBaseQuantity = value;
    }

    /**
     * thirdExtraFee 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getThirdExtraFee() {
        return thirdExtraFee;
    }

    /**
     * thirdExtraFee 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setThirdExtraFee(Integer value) {
        this.thirdExtraFee = value;
    }

    /**
     * payType 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayType() {
        return payType;
    }

    /**
     * payType 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayType(String value) {
        this.payType = value;
    }

    /**
     * areaType 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAreaType() {
        return areaType;
    }

    /**
     * areaType 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAreaType(String value) {
        this.areaType = value;
    }

    /**
     * area2ExtraFee 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getArea2ExtraFee() {
        return area2ExtraFee;
    }

    /**
     * area2ExtraFee 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setArea2ExtraFee(Integer value) {
        this.area2ExtraFee = value;
    }

    /**
     * area3ExtraFee 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getArea3ExtraFee() {
        return area3ExtraFee;
    }

    /**
     * area3ExtraFee 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setArea3ExtraFee(Integer value) {
        this.area3ExtraFee = value;
    }

    /**
     * returnDeliveryCompanyPriority 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnDeliveryCompanyPriority() {
        return returnDeliveryCompanyPriority;
    }

    /**
     * returnDeliveryCompanyPriority 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnDeliveryCompanyPriority(String value) {
        this.returnDeliveryCompanyPriority = value;
    }

    /**
     * returnFee 속성의 값을 가져옵니다.
     * 
     */
    public int getReturnFee() {
        return returnFee;
    }

    /**
     * returnFee 속성의 값을 설정합니다.
     * 
     */
    public void setReturnFee(int value) {
        this.returnFee = value;
    }

    /**
     * exchangeFee 속성의 값을 가져옵니다.
     * 
     */
    public int getExchangeFee() {
        return exchangeFee;
    }

    /**
     * exchangeFee 속성의 값을 설정합니다.
     * 
     */
    public void setExchangeFee(int value) {
        this.exchangeFee = value;
    }

    /**
     * deliveryInsurance 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryInsurance() {
        return deliveryInsurance;
    }

    /**
     * deliveryInsurance 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryInsurance(String value) {
        this.deliveryInsurance = value;
    }

    /**
     * shippingAddressId 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getShippingAddressId() {
        return shippingAddressId;
    }

    /**
     * shippingAddressId 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setShippingAddressId(Long value) {
        this.shippingAddressId = value;
    }

    /**
     * returnAddressId 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getReturnAddressId() {
        return returnAddressId;
    }

    /**
     * returnAddressId 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setReturnAddressId(Long value) {
        this.returnAddressId = value;
    }

    /**
     * differentialFee 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDifferentialFee() {
        return differentialFee;
    }

    /**
     * differentialFee 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDifferentialFee(String value) {
        this.differentialFee = value;
    }

    /**
     * installationFee 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstallationFee() {
        return installationFee;
    }

    /**
     * installationFee 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstallationFee(String value) {
        this.installationFee = value;
    }

    /**
     * individualCustomUniqueCode 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndividualCustomUniqueCode() {
        return individualCustomUniqueCode;
    }

    /**
     * individualCustomUniqueCode 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndividualCustomUniqueCode(String value) {
        this.individualCustomUniqueCode = value;
    }

    /**
     * attributeType 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttributeType() {
        return attributeType;
    }

    /**
     * attributeType 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttributeType(String value) {
        this.attributeType = value;
    }

    /**
     * expectedDeliveryPeriodType 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpectedDeliveryPeriodType() {
        return expectedDeliveryPeriodType;
    }

    /**
     * expectedDeliveryPeriodType 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpectedDeliveryPeriodType(String value) {
        this.expectedDeliveryPeriodType = value;
    }

    /**
     * expectedDeliveryPeriodDirectInput 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpectedDeliveryPeriodDirectInput() {
        return expectedDeliveryPeriodDirectInput;
    }

    /**
     * expectedDeliveryPeriodDirectInput 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpectedDeliveryPeriodDirectInput(String value) {
        this.expectedDeliveryPeriodDirectInput = value;
    }

    /**
     * todayStockQuantity 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTodayStockQuantity() {
        return todayStockQuantity;
    }

    /**
     * todayStockQuantity 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTodayStockQuantity(Integer value) {
        this.todayStockQuantity = value;
    }

    /**
     * customProductAfterOrderYn 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomProductAfterOrderYn() {
        return customProductAfterOrderYn;
    }

    /**
     * customProductAfterOrderYn 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomProductAfterOrderYn(String value) {
        this.customProductAfterOrderYn = value;
    }

    /**
     * hopeDeliveryGroupUsable 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHopeDeliveryGroupUsable() {
        return hopeDeliveryGroupUsable;
    }

    /**
     * hopeDeliveryGroupUsable 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHopeDeliveryGroupUsable(String value) {
        this.hopeDeliveryGroupUsable = value;
    }

    /**
     * hopeDeliveryGroupId 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getHopeDeliveryGroupId() {
        return hopeDeliveryGroupId;
    }

    /**
     * hopeDeliveryGroupId 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setHopeDeliveryGroupId(Long value) {
        this.hopeDeliveryGroupId = value;
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

}
