package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetBrandListResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetBrandListResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType">
 *       &lt;sequence>
 *         &lt;element name="BrandList" type="{http://shopn.platform.nhncorp.com/}BrandMapType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetBrandListResponseType", propOrder = {
    "brandList"
})
public class GetBrandListResponseType extends BaseProductResponseType {
	@XmlElement(name = "BrandList", namespace = "")
    protected List<BrandMapType> brandList;

	public List<BrandMapType> getBrandList() {
		if (brandList == null) {
			brandList = new ArrayList<BrandMapType>();
        }
		return this.brandList;
	}
}
