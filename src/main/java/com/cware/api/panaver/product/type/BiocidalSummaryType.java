
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>BiocidalSummaryType complex type에 대한 Java 클래스입니다.
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 * 
 * <pre>
 * &lt;complexType name="BiocidalSummaryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseSummaryType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProductName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ExpirationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ExpirationDateText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Weight" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Effect" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RangeOfUse" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Importer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Producer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Manufacturer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ChildProtection" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="HarmfulChemicalSubstance" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Maleficence" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Caution" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ApprovalNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CustomerServicePhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BiocidalSummaryType", propOrder = {
    "productName",
    "expirationDate",
    "expirationDateText",
    "weight",
    "effect",
    "rangeOfUse",
    "importer",
    "producer",
    "manufacturer",
    "childProtection",
    "harmfulChemicalSubstance",
    "maleficence",
    "caution",
    "approvalNumber",
    "customerServicePhoneNumber"
})
public class BiocidalSummaryType
    extends BaseSummaryType
{

    @XmlElement(name = "ProductName", required = true)
    protected String productName;
    @XmlElement(name = "ExpirationDate")
    protected String expirationDate;
    @XmlElement(name = "ExpirationDateText")
    protected String expirationDateText;
    @XmlElement(name = "Weight", required = true)
    protected String weight;
    @XmlElement(name = "Effect", required = true)
    protected String effect;
    @XmlElement(name = "RangeOfUse", required = true)
    protected String rangeOfUse;
    @XmlElement(name = "Importer")
    protected String importer;
    @XmlElement(name = "Producer", required = true)
    protected String producer;
    @XmlElement(name = "Manufacturer", required = true)
    protected String manufacturer;
    @XmlElement(name = "ChildProtection", required = true)
    protected String childProtection;
    @XmlElement(name = "HarmfulChemicalSubstance", required = true)
    protected String harmfulChemicalSubstance;
    @XmlElement(name = "Maleficence", required = true)
    protected String maleficence;
    @XmlElement(name = "Caution", required = true)
    protected String caution;
    @XmlElement(name = "ApprovalNumber", required = true)
    protected String approvalNumber;
    @XmlElement(name = "CustomerServicePhoneNumber", required = true)
    protected String customerServicePhoneNumber;

    /**
     * productName 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductName() {
        return productName;
    }

    /**
     * productName 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductName(String value) {
        this.productName = value;
    }

    /**
     * expirationDate 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * expirationDate 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpirationDate(String value) {
        this.expirationDate = value;
    }

    /**
     * expirationDateText 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpirationDateText() {
        return expirationDateText;
    }

    /**
     * expirationDateText 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpirationDateText(String value) {
        this.expirationDateText = value;
    }

    /**
     * weight 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeight() {
        return weight;
    }

    /**
     * weight 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeight(String value) {
        this.weight = value;
    }

    /**
     * effect 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEffect() {
        return effect;
    }

    /**
     * effect 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEffect(String value) {
        this.effect = value;
    }

    /**
     * rangeOfUse 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRangeOfUse() {
        return rangeOfUse;
    }

    /**
     * rangeOfUse 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRangeOfUse(String value) {
        this.rangeOfUse = value;
    }

    /**
     * importer 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImporter() {
        return importer;
    }

    /**
     * importer 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImporter(String value) {
        this.importer = value;
    }

    /**
     * producer 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProducer() {
        return producer;
    }

    /**
     * producer 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProducer(String value) {
        this.producer = value;
    }

    /**
     * manufacturer 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * manufacturer 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufacturer(String value) {
        this.manufacturer = value;
    }

    /**
     * childProtection 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChildProtection() {
        return childProtection;
    }

    /**
     * childProtection 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChildProtection(String value) {
        this.childProtection = value;
    }

    /**
     * harmfulChemicalSubstance 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHarmfulChemicalSubstance() {
        return harmfulChemicalSubstance;
    }

    /**
     * harmfulChemicalSubstance 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHarmfulChemicalSubstance(String value) {
        this.harmfulChemicalSubstance = value;
    }

    /**
     * maleficence 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaleficence() {
        return maleficence;
    }

    /**
     * maleficence 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaleficence(String value) {
        this.maleficence = value;
    }

    /**
     * caution 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaution() {
        return caution;
    }

    /**
     * caution 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaution(String value) {
        this.caution = value;
    }

    /**
     * approvalNumber 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApprovalNumber() {
        return approvalNumber;
    }

    /**
     * approvalNumber 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApprovalNumber(String value) {
        this.approvalNumber = value;
    }

    /**
     * customerServicePhoneNumber 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerServicePhoneNumber() {
        return customerServicePhoneNumber;
    }

    /**
     * customerServicePhoneNumber 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerServicePhoneNumber(String value) {
        this.customerServicePhoneNumber = value;
    }

}
