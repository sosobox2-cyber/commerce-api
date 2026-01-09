package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for RentalEtcSummaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RentalEtcSummaryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NoRefundReason" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ReturnCostReason" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="QualityAssuranceStandard" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CompensationProcedure" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TroubleShootingContents" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ItemName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ModelName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OwnershipTransferCondition" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PayingForLossOrDamage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RefundPolicyForCancel" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CustomerServicePhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RentalEtcSummaryType", propOrder = {
    "noRefundReason",
    "returnCostReason",
    "qualityAssuranceStandard",
    "compensationProcedure",
    "troubleShootingContents",
    "itemName",
    "modelName",
    "ownershipTransferCondition",
    "payingForLossOrDamage",
    "refundPolicyForCancel",   
    "customerServicePhoneNumber"
})
public class RentalEtcSummaryType {
	
	@XmlElement(name = "NoRefundReason", required = true)
    protected String noRefundReason;
	@XmlElement(name = "ReturnCostReason", required = true)
    protected String returnCostReason;
	@XmlElement(name = "QualityAssuranceStandard", required = true)
    protected String qualityAssuranceStandard;
	@XmlElement(name = "CompensationProcedure", required = true)
    protected String compensationProcedure;
	@XmlElement(name = "TroubleShootingContents", required = true)
    protected String troubleShootingContents;
	@XmlElement(name = "ItemName", required = true)
    protected String itemName;
	@XmlElement(name = "ModelName", required = true)
    protected String modelName;
	@XmlElement(name = "OwnershipTransferCondition", required = true)
    protected String ownershipTransferCondition;	
	@XmlElement(name = "PayingForLossOrDamage", required = true)
    protected String payingForLossOrDamage;
	@XmlElement(name = "RefundPolicyForCancel", required = true)
    protected String refundPolicyForCancel;	
	@XmlElement(name = "CustomerServicePhoneNumber", required = true)
    protected String customerServicePhoneNumber;
	
	public String getNoRefundReason() {
		return noRefundReason;
	}
	public void setNoRefundReason(String noRefundReason) {
		this.noRefundReason = noRefundReason;
	}
	public String getReturnCostReason() {
		return returnCostReason;
	}
	public void setReturnCostReason(String returnCostReason) {
		this.returnCostReason = returnCostReason;
	}
	public String getQualityAssuranceStandard() {
		return qualityAssuranceStandard;
	}
	public void setQualityAssuranceStandard(String qualityAssuranceStandard) {
		this.qualityAssuranceStandard = qualityAssuranceStandard;
	}
	public String getCompensationProcedure() {
		return compensationProcedure;
	}
	public void setCompensationProcedure(String compensationProcedure) {
		this.compensationProcedure = compensationProcedure;
	}
	public String getTroubleShootingContents() {
		return troubleShootingContents;
	}
	public void setTroubleShootingContents(String troubleShootingContents) {
		this.troubleShootingContents = troubleShootingContents;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getOwnershipTransferCondition() {
		return ownershipTransferCondition;
	}
	public void setOwnershipTransferCondition(String ownershipTransferCondition) {
		this.ownershipTransferCondition = ownershipTransferCondition;
	}
	public String getPayingForLossOrDamage() {
		return payingForLossOrDamage;
	}
	public void setPayingForLossOrDamage(String payingForLossOrDamage) {
		this.payingForLossOrDamage = payingForLossOrDamage;
	}
	public String getRefundPolicyForCancel() {
		return refundPolicyForCancel;
	}
	public void setRefundPolicyForCancel(String refundPolicyForCancel) {
		this.refundPolicyForCancel = refundPolicyForCancel;
	}
	public String getCustomerServicePhoneNumber() {
		return customerServicePhoneNumber;
	}
	public void setCustomerServicePhoneNumber(String customerServicePhoneNumber) {
		this.customerServicePhoneNumber = customerServicePhoneNumber;
	}
}
