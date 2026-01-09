package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for DigitalContentsSummaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DigitalContentsSummaryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NoRefundReason" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ReturnCostReason" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="QualityAssuranceStandard" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CompensationProcedure" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TroubleShootingContents" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Producer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TermsOfUse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UsePeriod" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Medium" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Requirement" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CancelationPolicy" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "DigitalContentsSummaryType", propOrder = {
    "noRefundReason",
    "returnCostReason",
    "qualityAssuranceStandard",
    "compensationProcedure",
    "troubleShootingContents",
    "producer",
    "termsOfUse",
    "usePeriod",
    "medium",
    "requirement", 
    "cancelationPolicy",   
    "customerServicePhoneNumber"
})
public class DigitalContentsSummaryType {
	
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
	@XmlElement(name = "Producer", required = true)
    protected String producer;
	@XmlElement(name = "TermsOfUse", required = true)
    protected String termsOfUse;
	@XmlElement(name = "UsePeriod", required = true)
    protected String usePeriod;	
	@XmlElement(name = "Medium", required = true)
    protected String medium;
	@XmlElement(name = "Requirement", required = true)
    protected String requirement;	
	@XmlElement(name = "CancelationPolicy", required = true)
    protected String cancelationPolicy;	
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
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public String getTermsOfUse() {
		return termsOfUse;
	}
	public void setTermsOfUse(String termsOfUse) {
		this.termsOfUse = termsOfUse;
	}
	public String getUsePeriod() {
		return usePeriod;
	}
	public void setUsePeriod(String usePeriod) {
		this.usePeriod = usePeriod;
	}
	public String getMedium() {
		return medium;
	}
	public void setMedium(String medium) {
		this.medium = medium;
	}
	public String getRequirement() {
		return requirement;
	}
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}
	public String getCancelationPolicy() {
		return cancelationPolicy;
	}
	public void setCancelationPolicy(String cancelationPolicy) {
		this.cancelationPolicy = cancelationPolicy;
	}
	public String getCustomerServicePhoneNumber() {
		return customerServicePhoneNumber;
	}
	public void setCustomerServicePhoneNumber(String customerServicePhoneNumber) {
		this.customerServicePhoneNumber = customerServicePhoneNumber;
	}
}