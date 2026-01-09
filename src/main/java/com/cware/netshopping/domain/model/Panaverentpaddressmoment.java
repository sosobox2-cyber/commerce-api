package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Panaverentpaddressmoment extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String addressId;
	private String name;
	private String addressType;
	private String postalCode;
	private String baseAddress;
	private String detailAddress;
	private String fullAddress;
	private String phoneNumber1;
	private String phoneNumber2;
	private String hasLocation;
	private String roadNameAddress;
	private String overseasAddress;

	public String getAddressId() { 
		return this.addressId;
	}
	public String getName() { 
		return this.name;
	}
	public String getAddressType() { 
		return this.addressType;
	}
	public String getPostalCode() { 
		return this.postalCode;
	}
	public String getBaseAddress() { 
		return this.baseAddress;
	}
	public String getDetailAddress() { 
		return this.detailAddress;
	}
	public String getFullAddress() { 
		return this.fullAddress;
	}
	public String getPhoneNumber1() { 
		return this.phoneNumber1;
	}
	public String getPhoneNumber2() { 
		return this.phoneNumber2;
	}
	public String getHasLocation() { 
		return this.hasLocation;
	}
	public String getRoadNameAddress() { 
		return this.roadNameAddress;
	}
	public String getOverseasAddress() { 
		return this.overseasAddress;
	}

	public void setAddressId(String addressId) { 
		this.addressId = addressId;
	}
	public void setName(String name) { 
		this.name = name;
	}
	public void setAddressType(String addressType) { 
		this.addressType = addressType;
	}
	public void setPostalCode(String postalCode) { 
		this.postalCode = postalCode;
	}
	public void setBaseAddress(String baseAddress) { 
		this.baseAddress = baseAddress;
	}
	public void setDetailAddress(String detailAddress) { 
		this.detailAddress = detailAddress;
	}
	public void setFullAddress(String fullAddress) { 
		this.fullAddress = fullAddress;
	}
	public void setPhoneNumber1(String phoneNumber1) { 
		this.phoneNumber1 = phoneNumber1;
	}
	public void setPhoneNumber2(String phoneNumber2) { 
		this.phoneNumber2 = phoneNumber2;
	}
	public void setHasLocation(String hasLocation) { 
		this.hasLocation = hasLocation;
	}
	public void setRoadNameAddress(String roadNameAddress) { 
		this.roadNameAddress = roadNameAddress;
	}
	public void setOverseasAddress(String overseasAddress) { 
		this.overseasAddress = overseasAddress;
	}
}
