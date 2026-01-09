package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetAddressBookReturnType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetAddressBookReturnType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddressId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AddressType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PostalCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BaseAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DetailAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FullAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PhoneNumber1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PhoneNumber2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HasLocation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RoadNameAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OverseasAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAddressBookReturnType", propOrder = {
	    "addressId",
	    "name",
	    "addressType",
	    "postalCode",
	    "baseAddress",
	    "detailAddress",
	    "fullAddress",
	    "phoneNumber1",
	    "phoneNumber2",
	    "hasLocation",
	    "roadNameAddress",
	    "overseasAddress",
})
public class GetAddressBookReturnType {

    @XmlElement(name = "AddressId")
    protected String addressId;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "AddressType")
    protected String addressType;
    @XmlElement(name = "PostalCode")
    protected String postalCode;
    @XmlElement(name = "BaseAddress")
    protected String baseAddress;
    @XmlElement(name = "DetailAddress")
    protected String detailAddress;
    @XmlElement(name = "FullAddress")
    protected String fullAddress;
    @XmlElement(name = "PhoneNumber1")
    protected String phoneNumber1;
    @XmlElement(name = "PhoneNumber2")
    protected String phoneNumber2;
    @XmlElement(name = "HasLocation")
    protected String hasLocation;
    @XmlElement(name = "RoadNameAddress")
    protected String roadNameAddress;
    @XmlElement(name = "OverseasAddress")
    protected String overseasAddress;
    
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getBaseAddress() {
		return baseAddress;
	}
	public void setBaseAddress(String baseAddress) {
		this.baseAddress = baseAddress;
	}
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	public String getFullAddress() {
		return fullAddress;
	}
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	public String getPhoneNumber1() {
		return phoneNumber1;
	}
	public void setPhoneNumber1(String phoneNumber1) {
		this.phoneNumber1 = phoneNumber1;
	}
	public String getPhoneNumber2() {
		return phoneNumber2;
	}
	public void setPhoneNumber2(String phoneNumber2) {
		this.phoneNumber2 = phoneNumber2;
	}
	public String getHasLocation() {
		return hasLocation;
	}
	public void setHasLocation(String hasLocation) {
		this.hasLocation = hasLocation;
	}
	public String getRoadNameAddress() {
		return roadNameAddress;
	}
	public void setRoadNameAddress(String roadNameAddress) {
		this.roadNameAddress = roadNameAddress;
	}
	public String getOverseasAddress() {
		return overseasAddress;
	}
	public void setOverseasAddress(String overseasAddress) {
		this.overseasAddress = overseasAddress;
	}    
}
