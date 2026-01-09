package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetManufacturerListResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetManufacturerListResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType">
 *       &lt;sequence>
 *         &lt;element name="ManufacturerList" type="{http://shopn.platform.nhncorp.com/}ManufacturerMapType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetManufacturerListResponseType", propOrder = {
    "manufacturerList"
})
public class GetManufacturerListResponseType extends BaseProductResponseType {
	@XmlElement(name = "ManufacturerList", namespace = "")
    protected List<ManufacturerMapType> manufacturerList;

	public List<ManufacturerMapType> getBrandList() {
		if (manufacturerList == null) {
			manufacturerList = new ArrayList<ManufacturerMapType>();
        }
		return this.manufacturerList;
	}
}
