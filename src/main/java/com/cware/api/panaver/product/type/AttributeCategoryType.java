package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for AttributeCategoryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AttributeCategoryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}String"/>
 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EssentailYN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeValueList" type="{http://shopn.platform.nhncorp.com/}StringCodeMapType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttributeCategoryType", propOrder = {
    "code",
    "name",
    "type",
    "essentailYN",
    "attributeValueList"
})
public class AttributeCategoryType {
	
	@XmlElement(name = "Code")
    protected String code;
	@XmlElement(name = "Name")
    protected String name;
	@XmlElement(name = "Type")
    protected String type;
	@XmlElement(name = "EssentailYN")
    protected String essentailYN;
	@XmlElement(name = "AttributeValueList")
    protected List<StringCodeMapType> attributeValueList;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEssentailYN() {
		return essentailYN;
	}
	public void setEssentailYN(String essentailYN) {
		this.essentailYN = essentailYN;
	}

	public List<StringCodeMapType> getAttributeValueList() {
        if (attributeValueList == null) {
        	attributeValueList = new ArrayList<StringCodeMapType>();
        }
        return this.attributeValueList;
    }
	
}
