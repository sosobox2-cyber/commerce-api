package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetModelListRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetModelListRequestType">
 *   &lt;complexContent>
 *         &lt;element name"Page" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name"ModelName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name"ModelId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetModelListRequestType2", propOrder = {
	    "page",
	    "modelName"
})
public class GetModelListRequestType2 extends BaseProductRequestType {
	
	@XmlElement(name = "Page", namespace = "")
    protected String page;
	@XmlElement(name = "ModelName", namespace = "")
    protected String modelName;

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
}
