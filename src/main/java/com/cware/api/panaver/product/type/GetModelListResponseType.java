package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetModelListResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetModelListResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType">
 *       &lt;sequence>
 *         &lt;element name="page" type="{http://www.w3.org/2001/XMLSchema/}int" minOccurs="0"/>
 *         &lt;element name="TotalPage" type="{http://www.w3.org/2001/XMLSchema/}int" minOccurs="0"/>
 *         &lt;element name="ModelList" type="{http://shopn.platform.nhncorp.com/}ModelMapType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetModelListResponseType", propOrder = {
    "page",
    "totalPage",
    "modelList"
})
public class GetModelListResponseType extends BaseProductResponseType {

	@XmlElement(name = "Page", namespace = "")
    protected String page;
	@XmlElement(name = "TotalPage", namespace = "")
    protected String totalPage;
	@XmlElement(name = "ModelList", namespace = "")
	protected List<ModelMapType> modelList;
	
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}	

	public List<ModelMapType> getModelList() {
		if (modelList == null) {
			modelList = new ArrayList<ModelMapType>();
        }
		return this.modelList;
	}
}
