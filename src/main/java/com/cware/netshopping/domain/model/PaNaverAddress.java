package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaNaverAddress extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String addressSeq;
	private String addressType;
	private String zipCode;
	private String baseAddress;
	private String detailedAddress;
	private String isRoadNameAddress;
	private String city;
	private String state;
	private String country;
	private String tel1;
	private String tel2;
	private String name;

	public String getAddressSeq() { 
		return this.addressSeq;
	}
	public String getAddressType() { 
		return this.addressType;
	}
	public String getZipCode() { 
		return this.zipCode;
	}
	public String getBaseAddress() { 
		return this.baseAddress;
	}
	public String getDetailedAddress() { 
		return this.detailedAddress;
	}
	public String getIsRoadNameAddress() { 
		return this.isRoadNameAddress;
	}
	public String getCity() { 
		return this.city;
	}
	public String getState() { 
		return this.state;
	}
	public String getCountry() { 
		return this.country;
	}
	public String getTel1() { 
		return this.tel1;
	}
	public String getTel2() { 
		return this.tel2;
	}
	public String getName() { 
		return this.name;
	}

	public void setAddressSeq(String addressSeq) { 
		this.addressSeq = addressSeq;
	}
	public void setAddressType(String addressType) { 
		this.addressType = addressType;
	}
	public void setZipCode(String zipCode) { 
		this.zipCode = zipCode;
	}
	public void setBaseAddress(String baseAddress) { 
		this.baseAddress = baseAddress;
	}
	public void setDetailedAddress(String detailedAddress) { 
		this.detailedAddress = detailedAddress;
	}
	public void setIsRoadNameAddress(String isRoadNameAddress) { 
		this.isRoadNameAddress = isRoadNameAddress;
	}
	public void setCity(String city) { 
		this.city = city;
	}
	public void setState(String state) { 
		this.state = state;
	}
	public void setCountry(String country) { 
		this.country = country;
	}
	public void setTel1(String tel1) { 
		this.tel1 = tel1;
	}
	public void setTel2(String tel2) { 
		this.tel2 = tel2;
	}
	public void setName(String name) { 
		this.name = name;
	}
}
