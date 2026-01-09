package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetModificationRequiredProductListResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetModificationRequiredProductListResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType">
 *       &lt;sequence>
 *         &lt;element name="ModificationRequiredProductList" type="{http://shopn.platform.nhncorp.com/}ModificationRequiredProductMapType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Page" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TotalPage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetModificationRequiredProductListResponseType", propOrder = {
    "modificationRequiredProductList"
})
public class GetModificationRequiredProductListResponseType extends BaseProductResponseType {

	@XmlElement(name = "ModificationRequiredProductList", namespace = "")
    protected List<ModificationRequiredProductMapType> modificationRequiredProductList;
	@XmlElement(name = "Page", namespace = "")
    protected int page;
	@XmlElement(name = "TotlaPage", namespace = "")
    protected int totalPage;
	
	public List<ModificationRequiredProductMapType> getModificationRequiredProductList() {
		if (modificationRequiredProductList == null) {
			modificationRequiredProductList = new ArrayList<ModificationRequiredProductMapType>();
        }
		return this.modificationRequiredProductList;
	}

	public int getPage() {
		return page;
	}

	public int getTotalPage() {
		return totalPage;
	}
}
