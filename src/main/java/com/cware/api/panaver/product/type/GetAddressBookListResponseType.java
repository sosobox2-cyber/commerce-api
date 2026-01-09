
package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetAddressBookListResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetAddressBookListResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType">
 *       &lt;sequence>
 *         &lt;element name="AddressBookList" type="{http://shopn.platform.nhncorp.com/}AddressBookReturnMapType" minOccurs="0"/>
 *         &lt;element name="Page" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TotalPage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAddressBookListResponseType", propOrder = {
    "addressBookList",
    "page",
    "totalPage"
})
public class GetAddressBookListResponseType extends BaseProductResponseType {
	
	@XmlElement(name = "AddressBookList", namespace = "")
    protected List<AddressBookReturnMapType> addressBookList;
	@XmlElement(name = "page", namespace = "")
    protected int page;
	@XmlElement(name = "totalPage", namespace = "")
    protected int totalPage;
	
	public List<AddressBookReturnMapType> getAddressBookList() {
		if (addressBookList == null) {
			addressBookList = new ArrayList<AddressBookReturnMapType>();
        }
		return this.addressBookList;
	}

	public int getPage() {
		return page;
	}

	public int getTotalPage() {
		return totalPage;
	}
	

}
