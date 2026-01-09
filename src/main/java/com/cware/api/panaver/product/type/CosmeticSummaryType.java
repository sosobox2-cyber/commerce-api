package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for CosmeticSummaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CosmeticSummaryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NoRefundReason" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ReturnCostReason" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="QualityAssuranceStandard" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CompensationProcedure" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TroubleShootingContents" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Capacity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Specification" type="{http://www.w3.org/2001/XMLSchema}string"/>         
 *         &lt;element name="ExpirationDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ExpirationDateText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Usage" type="{http://www.w3.org/2001/XMLSchema}string"/>        
 *         &lt;element name="CustomizedDistributor" type="{http://www.w3.org/2001/XMLSchema}string"/>     
 *         &lt;element name="Manufacturer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Distributor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MainIngredient" type="{http://www.w3.org/2001/XMLSchema}string"/>         
 *         &lt;element name="Certified" type="{http://www.w3.org/2001/XMLSchema}string"/>       
 *         &lt;element name="Caution" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WarrantyPolicy" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "CosmeticSummaryType", propOrder = {
    "noRefundReason",
    "returnCostReason",
    "qualityAssuranceStandard",
    "compensationProcedure",
    "troubleShootingContents",
    "capacity",
    "specification",
    "expirationDate",
    "expirationDateText",    
    "usage",
    "manufacturer",
    "distributor",
    "customizedDistributor",
    "mainIngredient",
    "certified",
    "caution",
    "warrantyPolicy",
    "customerServicePhoneNumber",
    "producer"
})
public class CosmeticSummaryType {
	
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
	@XmlElement(name = "Capacity", required = true)
    protected String capacity;
	@XmlElement(name = "Specification", required = true)
    protected String specification;
	@XmlElement(name = "ExpirationDate", required = true)
    protected String expirationDate;
	@XmlElement(name = "ExpirationDateText", required = true)
    protected String expirationDateText;
	@XmlElement(name = "Usage", required = true)
    protected String usage;	
	@XmlElement(name = "Manufacturer", required = true)
    protected String manufacturer;	
	@XmlElement(name = "Distributor", required = true)
    protected String distributor;
	@XmlElement(name = "MainIngredient", required = true)
    protected String mainIngredient;
	@XmlElement(name = "Certified", required = true)
    protected String certified;	
	@XmlElement(name = "Caution", required = true)
    protected String caution;	
	@XmlElement(name = "WarrantyPolicy", required = true)
    protected String warrantyPolicy;
	@XmlElement(name = "CustomerServicePhoneNumber", required = true)
    protected String customerServicePhoneNumber;
	@XmlElement(name = "Producer", required = true)
    protected String producer;
	@XmlElement(name = "CustomizedDistributor", required = true)
    protected String customizedDistributor;
	
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
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getExpirationDateText() {
		return expirationDateText;
	}
	public void setExpirationDateText(String expirationDateText) {
		this.expirationDateText = expirationDateText;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getDistributor() {
		return distributor;
	}
	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}
	public String getMainIngredient() {
		return mainIngredient;
	}
	public void setMainIngredient(String mainIngredient) {
		this.mainIngredient = mainIngredient;
	}
	public String getCertified() {
		return certified;
	}
	public void setCertified(String certified) {
		this.certified = certified;
	}
	public String getCaution() {
		return caution;
	}
	public void setCaution(String caution) {
		this.caution = caution;
	}
	public String getWarrantyPolicy() {
		return warrantyPolicy;
	}
	public void setWarrantyPolicy(String warrantyPolicy) {
		this.warrantyPolicy = warrantyPolicy;
	}
	public String getCustomerServicePhoneNumber() {
		return customerServicePhoneNumber;
	}
	public void setCustomerServicePhoneNumber(String customerServicePhoneNumber) {
		this.customerServicePhoneNumber = customerServicePhoneNumber;
	}
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}	
	public String getCustomizedDistributor() {
		return customizedDistributor;
	}
	public void setCustomizedDistributor(String customizedDistributor) {
		this.customizedDistributor = customizedDistributor;
	}
}
