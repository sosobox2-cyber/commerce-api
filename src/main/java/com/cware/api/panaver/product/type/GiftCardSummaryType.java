package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GiftCardSummaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GiftCardSummaryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NoRefundReason" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ReturnCostReason" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="QualityAssuranceStandard" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CompensationProcedure" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TroubleShootingContents" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Issuer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PeriodStartDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PeriodEndDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PeriodDays" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TermsOfUse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UseStorePlace" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UseStoreAddressId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="RefundPolicy" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "GiftCardSummaryType", propOrder = {
    "noRefundReason",
    "returnCostReason",
    "qualityAssuranceStandard",
    "compensationProcedure",
    "troubleShootingContents",
    "issuer",
    "periodStartDate",
    "periodEndDate",
    "periodDays",
    "termsOfUse", 
    "useStorePlace",   
    "useStoreAddressId",
    "refundPolicy",
    "customerServicePhoneNumber"
})
public class GiftCardSummaryType {
	
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
	@XmlElement(name = "Issuer", required = true)
    protected String issuer;
	@XmlElement(name = "PeriodStartDate", required = true)
    protected String periodStartDate;
	@XmlElement(name = "PeriodEndDate", required = true)
    protected String periodEndDate;	
	@XmlElement(name = "PeriodDays", required = true)
    protected int periodDays;
	@XmlElement(name = "TermsOfUse", required = true)
    protected String termsOfUse;	
	@XmlElement(name = "UseStorePlace", required = true)
    protected String useStorePlace;	
	@XmlElement(name = "UseStoreAddressId", required = true)
    protected long useStoreAddressId;	
	@XmlElement(name = "RefundPolicy", required = true)
    protected String refundPolicy;	
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
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getPeriodStartDate() {
		return periodStartDate;
	}
	public void setPeriodStartDate(String periodStartDate) {
		this.periodStartDate = periodStartDate;
	}
	public String getPeriodEndDate() {
		return periodEndDate;
	}
	public void setPeriodEndDate(String periodEndDate) {
		this.periodEndDate = periodEndDate;
	}
	public int getPeriodDays() {
		return periodDays;
	}
	public void setPeriodDays(int periodDays) {
		this.periodDays = periodDays;
	}
	public String getTermsOfUse() {
		return termsOfUse;
	}
	public void setTermsOfUse(String termsOfUse) {
		this.termsOfUse = termsOfUse;
	}
	public String getUseStorePlace() {
		return useStorePlace;
	}
	public void setUseStorePlace(String useStorePlace) {
		this.useStorePlace = useStorePlace;
	}
	public long getUseStoreAddressId() {
		return useStoreAddressId;
	}
	public void setUseStoreAddressId(long useStoreAddressId) {
		this.useStoreAddressId = useStoreAddressId;
	}
	public String getRefundPolicy() {
		return refundPolicy;
	}
	public void setRefundPolicy(String refundPolicy) {
		this.refundPolicy = refundPolicy;
	}
	public String getCustomerServicePhoneNumber() {
		return customerServicePhoneNumber;
	}
	public void setCustomerServicePhoneNumber(String customerServicePhoneNumber) {
		this.customerServicePhoneNumber = customerServicePhoneNumber;
	}
	
}