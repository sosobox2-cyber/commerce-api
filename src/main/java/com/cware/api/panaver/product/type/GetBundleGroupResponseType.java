
package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetBundleGroupResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetBundleGroupResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType">
 *       &lt;sequence>
 *         &lt;element name="BundleGroup" type="{http://shopn.platform.nhncorp.com/}BundleGroupType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetBundleGroupResponseType", propOrder = {
    "bundleGroup"
})
public class GetBundleGroupResponseType extends BaseProductResponseType {
	
	@XmlElement(name = "BundleGroup", namespace = "")
    protected List<BundleGroupType> bundleGroup;
	
	public List<BundleGroupType> GetBundleGroup() {
		if (bundleGroup == null) {
			bundleGroup = new ArrayList<BundleGroupType>();
        }
		return this.bundleGroup;
	}
}
