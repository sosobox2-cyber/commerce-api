package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ModelReturnType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModelReturnType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ModelName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ModelId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="WholeCategoryName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CategoryId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ManufacturerName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ManufacturerCode" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="BrandName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BrandCode" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModelReturnType", propOrder = {
    "modelName",
    "modelId",
    "wholeCategoryName",
    "categoryId",
    "manufacturerName",
    "manufacturerCode",
    "brandName",
    "brandCode"
})
public class ModelReturnType {
	
	@XmlElement(name = "ModelName")
    protected String modelName;
	@XmlElement(name = "ModelId")
    protected String modelId;
	@XmlElement(name = "WholeCategoryName")
    protected String wholeCategoryName;
	@XmlElement(name = "CategoryId")
    protected String categoryId;
	@XmlElement(name = "ManufacturerName")
    protected String manufacturerName;
	@XmlElement(name = "ManufacturerCode")
    protected String manufacturerCode;
	@XmlElement(name = "BrandName")
    protected String brandName;
	@XmlElement(name = "BrandCode")
    protected String brandCode;
	
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public String getWholeCategoryName() {
		return wholeCategoryName;
	}
	public void setWholeCategoryName(String wholeCategoryName) {
		this.wholeCategoryName = wholeCategoryName;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getManufacturerCode() {
		return manufacturerCode;
	}
	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
}
