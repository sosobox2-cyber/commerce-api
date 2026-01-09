package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for CategoryReturnType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CategoryReturnType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CategoryName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Last" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ExceptionalCategoryList" type="{http://shopn.platform.nhncorp.com/}StringCodeMapType"/>
 *         &lt;element name="CertificationCategoryList" type="{http://shopn.platform.nhncorp.com/}NumericCodeMapType"/>
 *         &lt;element name="AttributeCategoryList" type="{http://shopn.platform.nhncorp.com/}AttributeCategoryMapType"/>
 *         &lt;element name="CertificationInfoList" type="{http://shopn.platform.nhncorp.com/}CertificationInfoMapType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CategoryReturnType", propOrder = {
    "categoryName",
    "id",
    "name",
    "last",
    "exceptionalCategoryList",
    "certificationCategoryList",
    "attributeCategoryList",
    "certificationInfoList"
})
public class CategoryReturnType {
	
	@XmlElement(name = "CategoryName")
    protected String categoryName;
	@XmlElement(name = "Id")
    protected String id;
	@XmlElement(name = "Name")
    protected String name;
	@XmlElement(name = "Last")
    protected String last;
	@XmlElement(name = "ExceptionalCategoryList")
    protected List<StringCodeMapType> exceptionalCategoryList;
	@XmlElement(name = "CertificationCategoryList")
    protected List<NumericCodeMapType> certificationCategoryList;
	@XmlElement(name = "AttributeCategoryList")
    protected List<AttributeCategoryMapType> attributeCategoryList;
	@XmlElement(name = "CertificationInfoList")
    protected List<CertificationInfoMapType> certificationInfoList;
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}

	public List<StringCodeMapType> getExceptionalCategoryList() {
        if (exceptionalCategoryList == null) {
        	exceptionalCategoryList = new ArrayList<StringCodeMapType>();
        }
        return this.exceptionalCategoryList;
    }
	public List<NumericCodeMapType> getCertificationCategoryList() {
        if (certificationCategoryList == null) {
        	certificationCategoryList = new ArrayList<NumericCodeMapType>();
        }
        return this.certificationCategoryList;
    }
	public List<AttributeCategoryMapType> getAttributeCategoryList() {
        if (attributeCategoryList == null) {
        	attributeCategoryList = new ArrayList<AttributeCategoryMapType>();
        }
        return this.attributeCategoryList;
    }
	public List<CertificationInfoMapType> getCertificationInfoList() {
        if (certificationInfoList == null) {
        	certificationInfoList = new ArrayList<CertificationInfoMapType>();
        }
        return this.certificationInfoList;
    }
	
}
