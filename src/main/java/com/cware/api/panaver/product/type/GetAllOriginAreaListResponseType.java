package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetAllOriginAreaListResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetAllOriginAreaListResponseType">
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
@XmlType(name = "GetAllOriginAreaListResponseType", propOrder = {
    "originAreaList"
})
public class GetAllOriginAreaListResponseType extends BaseProductResponseType {
	
	@XmlElement(name = "OriginAreaList", namespace = "")
    protected List<OriginMapType> originAreaList;
	
	public List<OriginMapType> getOriginAreaList() {
		if (originAreaList == null) {
			originAreaList = new ArrayList<OriginMapType>();
        }
		return this.originAreaList;
	}
	
}
