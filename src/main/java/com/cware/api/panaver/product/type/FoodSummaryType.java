
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>FoodSummaryType complex type에 대한 Java 클래스입니다.
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 * 
 * <pre>
 * &lt;complexType name="FoodSummaryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseSummaryType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FoodItem" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Weight" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Size" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PackDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PackDateText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ExpirationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ExpirationDateText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ConsumptionDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ConsumptionDateText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Producer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RelevantLawContent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ProductComposition" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Keep" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="AdCaution" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "FoodSummaryType", propOrder = {
    "foodItem",
    "weight",
    "amount",
    "size",
    "packDate",
    "packDateText",
    "expirationDate",
    "expirationDateText",
    "consumptionDate",
    "consumptionDateText",
    "producer",
    "relevantLawContent",
    "productComposition",
    "keep",
    "adCaution",
    "customerServicePhoneNumber"
})
public class FoodSummaryType
    extends BaseSummaryType
{

    @XmlElement(name = "FoodItem", required = true)
    protected String foodItem;
    @XmlElement(name = "Weight", required = true)
    protected String weight;
    @XmlElement(name = "Amount", required = true)
    protected String amount;
    @XmlElement(name = "Size", required = true)
    protected String size;
    @XmlElement(name = "PackDate")
    protected String packDate;
    @XmlElement(name = "PackDateText")
    protected String packDateText;
    @XmlElement(name = "ExpirationDate")
    protected String expirationDate;
    @XmlElement(name = "ExpirationDateText")
    protected String expirationDateText;
    @XmlElement(name = "ConsumptionDate")
    protected String consumptionDate;
    @XmlElement(name = "ConsumptionDateText")
    protected String consumptionDateText;
    @XmlElement(name = "Producer", required = true)
    protected String producer;
    @XmlElement(name = "RelevantLawContent")
    protected String relevantLawContent;
    @XmlElement(name = "ProductComposition", required = true)
    protected String productComposition;
    @XmlElement(name = "Keep", required = true)
    protected String keep;
    @XmlElement(name = "AdCaution", required = true)
    protected String adCaution;
    @XmlElement(name = "CustomerServicePhoneNumber", required = true)
    protected String customerServicePhoneNumber;

    /**
     * foodItem 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFoodItem() {
        return foodItem;
    }

    /**
     * foodItem 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFoodItem(String value) {
        this.foodItem = value;
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
     * amount 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmount() {
        return amount;
    }

    /**
     * amount 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmount(String value) {
        this.amount = value;
    }

    /**
     * size 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSize() {
        return size;
    }

    /**
     * size 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSize(String value) {
        this.size = value;
    }

    /**
     * packDate 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackDate() {
        return packDate;
    }

    /**
     * packDate 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackDate(String value) {
        this.packDate = value;
    }

    /**
     * packDateText 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackDateText() {
        return packDateText;
    }

    /**
     * packDateText 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackDateText(String value) {
        this.packDateText = value;
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
     * consumptionDate 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsumptionDate() {
        return consumptionDate;
    }

    /**
     * consumptionDate 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsumptionDate(String value) {
        this.consumptionDate = value;
    }

    /**
     * consumptionDateText 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsumptionDateText() {
        return consumptionDateText;
    }

    /**
     * consumptionDateText 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsumptionDateText(String value) {
        this.consumptionDateText = value;
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
     * relevantLawContent 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelevantLawContent() {
        return relevantLawContent;
    }

    /**
     * relevantLawContent 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelevantLawContent(String value) {
        this.relevantLawContent = value;
    }

    /**
     * productComposition 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductComposition() {
        return productComposition;
    }

    /**
     * productComposition 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductComposition(String value) {
        this.productComposition = value;
    }

    /**
     * keep 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeep() {
        return keep;
    }

    /**
     * keep 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeep(String value) {
        this.keep = value;
    }

    /**
     * adCaution 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdCaution() {
        return adCaution;
    }

    /**
     * adCaution 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdCaution(String value) {
        this.adCaution = value;
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
