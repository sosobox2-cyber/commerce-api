package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for CarArticlesSummaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CarArticlesSummaryType">
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
 *         &lt;element name="ReleaseDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ReleaseDateText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Certified" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Caution" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Manufacturer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Size" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ApplyModel" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WarrantyPolicy" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AfterServiceDirector" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CarArticlesSummaryType", propOrder = {
    "noRefundReason",
    "returnCostReason",
    "qualityAssuranceStandard",
    "compensationProcedure",
    "troubleShootingContents",
    "itemName",
    "modelName",
    "releaseDate",
    "releaseDateText",
    "certified",   
    "caution",   
    "manufacturer",
    "size",
    "applyModel",
    "warrantyPolicy",
    "afterServiceDirector",
    "roadWorthyCertification"
})
public class CarArticlesSummaryType {
	
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
	@XmlElement(name = "ReleaseDate", required = true)
    protected String releaseDate;	
	@XmlElement(name = "ReleaseDateText", required = true)
    protected String releaseDateText;
	@XmlElement(name = "Certified", required = true)
    protected String certified;	
	@XmlElement(name = "Caution", required = true)
    protected String caution;	
	@XmlElement(name = "Manufacturer", required = true)
    protected String manufacturer;	
	@XmlElement(name = "Size", required = true)
    protected String size;
	@XmlElement(name = "ApplyModel", required = true)
    protected String applyModel;
	@XmlElement(name = "WarrantyPolicy", required = true)
    protected String warrantyPolicy;
	@XmlElement(name = "AfterServiceDirector", required = true)
    protected String afterServiceDirector;
	@XmlElement(name = "RoadWorthyCertification", required = true)
    protected String roadWorthyCertification;
	
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
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getReleaseDateText() {
		return releaseDateText;
	}
	public void setReleaseDateText(String releaseDateText) {
		this.releaseDateText = releaseDateText;
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
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getApplyModel() {
		return applyModel;
	}
	public void setApplyModel(String applyModel) {
		this.applyModel = applyModel;
	}
	public String getWarrantyPolicy() {
		return warrantyPolicy;
	}
	public void setWarrantyPolicy(String warrantyPolicy) {
		this.warrantyPolicy = warrantyPolicy;
	}
	public String getAfterServiceDirector() {
		return afterServiceDirector;
	}
	public void setAfterServiceDirector(String afterServiceDirector) {
		this.afterServiceDirector = afterServiceDirector;
	}
	public String getRoadWorthyCertification() {
		return roadWorthyCertification;
	}
	public void setRoadWorthyCertification(String roadWorthyCertification) {
		this.roadWorthyCertification = roadWorthyCertification;
	}
}
