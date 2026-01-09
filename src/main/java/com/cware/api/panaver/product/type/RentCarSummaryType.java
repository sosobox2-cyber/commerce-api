
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RentCarSummaryType complex type에 대한 Java 클래스입니다.
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 * 
 * <pre>
 * &lt;complexType name="RentCarSummaryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseSummaryType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ModelName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="OwnershipTransferCondition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AdditionalCharge" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="FuelCostPaymentMean" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PayingForLossOrDamage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RefundPolicyForCancel" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "RentCarSummaryType", propOrder = {
    "modelName",
    "ownershipTransferCondition",
    "additionalCharge",
    "fuelCostPaymentMean",
    "payingForLossOrDamage",
    "refundPolicyForCancel",
    "customerServicePhoneNumber"
})
public class RentCarSummaryType
    extends BaseSummaryType
{

    @XmlElement(name = "ModelName", required = true)
    protected String modelName;
    @XmlElement(name = "OwnershipTransferCondition")
    protected String ownershipTransferCondition;
    @XmlElement(name = "AdditionalCharge", required = true)
    protected String additionalCharge;
    @XmlElement(name = "FuelCostPaymentMean", required = true)
    protected String fuelCostPaymentMean;
    @XmlElement(name = "PayingForLossOrDamage", required = true)
    protected String payingForLossOrDamage;
    @XmlElement(name = "RefundPolicyForCancel", required = true)
    protected String refundPolicyForCancel;
    @XmlElement(name = "CustomerServicePhoneNumber", required = true)
    protected String customerServicePhoneNumber;

    /**
     * modelName 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * modelName 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelName(String value) {
        this.modelName = value;
    }

    /**
     * ownershipTransferCondition 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnershipTransferCondition() {
        return ownershipTransferCondition;
    }

    /**
     * ownershipTransferCondition 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnershipTransferCondition(String value) {
        this.ownershipTransferCondition = value;
    }

    /**
     * additionalCharge 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalCharge() {
        return additionalCharge;
    }

    /**
     * additionalCharge 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalCharge(String value) {
        this.additionalCharge = value;
    }

    /**
     * fuelCostPaymentMean 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFuelCostPaymentMean() {
        return fuelCostPaymentMean;
    }

    /**
     * fuelCostPaymentMean 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFuelCostPaymentMean(String value) {
        this.fuelCostPaymentMean = value;
    }

    /**
     * payingForLossOrDamage 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayingForLossOrDamage() {
        return payingForLossOrDamage;
    }

    /**
     * payingForLossOrDamage 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayingForLossOrDamage(String value) {
        this.payingForLossOrDamage = value;
    }

    /**
     * refundPolicyForCancel 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefundPolicyForCancel() {
        return refundPolicyForCancel;
    }

    /**
     * refundPolicyForCancel 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefundPolicyForCancel(String value) {
        this.refundPolicyForCancel = value;
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
