package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for AddressBookReturnMapType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddressBookReturnMapType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddressBook" type="{http://shopn.platform.nhncorp.com/}AddressBookReturnType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressBookReturnMapType", propOrder = {
	    "AddressBook"
})
public class AddressBookReturnMapType {
	
	@XmlElement(name = "AddressBook")
    protected List<GetAddressBookReturnType> AddressBook;
	
	public List<GetAddressBookReturnType> getaddressBook() {
        if (AddressBook == null) {
        	AddressBook = new ArrayList<GetAddressBookReturnType>();
        }
        return this.AddressBook;
    }
}
