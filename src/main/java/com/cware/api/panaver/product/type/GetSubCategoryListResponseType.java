package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetSubCategoryListResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetSubCategoryListResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType">
 *       &lt;sequence>
 *         &lt;element name="CategoryList" type="{http://shopn.platform.nhncorp.com/}CategoryMapType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSubCategoryListResponseType", propOrder = {
    "categoryList"
})
public class GetSubCategoryListResponseType extends BaseProductResponseType {
	@XmlElement(name = "CategoryList", namespace = "")
    protected List<CategoryMapType> categoryList;

	public List<CategoryMapType> getCategoryList() {
		if (categoryList == null) {
			categoryList = new ArrayList<CategoryMapType>();
        }
		return this.categoryList;
	}
}
