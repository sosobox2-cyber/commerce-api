package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for EtcSummaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EtcSummaryType">
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
 *         &lt;element name="CertificateDetails" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Manufacturer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AfterServiceDirector" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "EtcSummaryType", propOrder = {
    "noRefundReason",
    "returnCostReason",
    "qualityAssuranceStandard",
    "compensationProcedure",
    "troubleShootingContents",
    "itemName",
    "modelName",
    "certificateDetails",
    "manufacturer",
    "afterServiceDirector",
    "customerServicePhoneNumber"
})
public class EtcSummaryType {
	
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
	@XmlElement(name = "CertificateDetails", required = true)
    protected String certificateDetails;
	@XmlElement(name = "Manufacturer", required = true)
    protected String manufacturer;
	@XmlElement(name = "AfterServiceDirector", required = true)
    protected String afterServiceDirector;
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
	public String getCertificateDetails() {
		return certificateDetails;
	}
	public void setCertificateDetails(String certificateDetails) {
		this.certificateDetails = certificateDetails;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getAfterServiceDirector() {
		return afterServiceDirector;
	}
	public void setAfterServiceDirector(String afterServiceDirector) {
		this.afterServiceDirector = afterServiceDirector;
	}
	public String getCustomerServicePhoneNumber() {
		return customerServicePhoneNumber;
	}
	public void setCustomerServicePhoneNumber(String customerServicePhoneNumber) {
		this.customerServicePhoneNumber = customerServicePhoneNumber;
	}
}