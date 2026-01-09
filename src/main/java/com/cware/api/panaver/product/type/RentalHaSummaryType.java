
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RentalHaSummaryType complex type에 대한 Java 클래스입니다.
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 * 
 * <pre>
 * &lt;complexType name="RentalHaSummaryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseSummaryType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ItemName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ModelName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="OwnershipTransferCondition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Maintenance" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PayingForLossOrDamage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RefundPolicyForCancel" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Specification" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "RentalHaSummaryType", propOrder = {
    "itemName",
    "modelName",
    "ownershipTransferCondition",
    "maintenance",
    "payingForLossOrDamage",
    "refundPolicyForCancel",
    "specification",
    "customerServicePhoneNumber"
})
public class RentalHaSummaryType
    extends BaseSummaryType
{

    @XmlElement(name = "ItemName", required = true)
    protected String itemName;
    @XmlElement(name = "ModelName", required = true)
    protected String modelName;
    @XmlElement(name = "OwnershipTransferCondition")
    protected String ownershipTransferCondition;
    @XmlElement(name = "Maintenance", required = true)
    protected String maintenance;
    @XmlElement(name = "PayingForLossOrDamage", required = true)
    protected String payingForLossOrDamage;
    @XmlElement(name = "RefundPolicyForCancel", required = true)
    protected String refundPolicyForCancel;
    @XmlElement(name = "Specification", required = true)
    protected String specification;
    @XmlElement(name = "CustomerServicePhoneNumber", required = true)
    protected String customerServicePhoneNumber;

    /**
     * itemName 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * itemName 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemName(String value) {
        this.itemName = value;
    }

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
     * maintenance 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaintenance() {
        return maintenance;
    }

    /**
     * maintenance 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaintenance(String value) {
        this.maintenance = value;
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
     * specification 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecification() {
        return specification;
    }

    /**
     * specification 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecification(String value) {
        this.specification = value;
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
