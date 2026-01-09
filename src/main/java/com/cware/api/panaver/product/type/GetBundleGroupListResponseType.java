
package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetbundleGroupListResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetbundleGroupListResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType">
 *       &lt;sequence>
 *         &lt;element name="bundleGroupList" type="{http://shopn.platform.nhncorp.com/}bundleGroupListMapType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetbundleGroupListResponseType", propOrder = {
    "bundleGroupList"
})
public class GetBundleGroupListResponseType extends BaseProductResponseType {
	
	@XmlElement(name = "BundleGroupList", namespace = "")
    protected List<BundleGroupListMapType> bundleGroupList;
	
	public List<BundleGroupListMapType> getBundleGroupList() {
		if (bundleGroupList == null) {
			bundleGroupList = new ArrayList<BundleGroupListMapType>();
        }
		return this.bundleGroupList;
	}
}
