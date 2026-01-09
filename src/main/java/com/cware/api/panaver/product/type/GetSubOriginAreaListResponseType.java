package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetSubOriginAreaListResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetSubOriginAreaListResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType">
 *       &lt;sequence>
 *         &lt;element name="OriginAreaList" type="{http://shopn.platform.nhncorp.com/}StringMapType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSubOriginAreaListResponseType", propOrder = {
    "originAreaList"
})
public class GetSubOriginAreaListResponseType extends BaseProductResponseType {
	
	@XmlElement(name = "OriginAreaList", namespace = "")
    protected List<StringMapType> originAreaList;
	
	public List<StringMapType> getOriginAreaList() {
		if (originAreaList == null) {
			originAreaList = new ArrayList<StringMapType>();
        }
		return this.originAreaList;
	}
	
}
