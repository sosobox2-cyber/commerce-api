package com.cware.netshopping.domain;

import com.cware.api.panaver.order.seller.SellerServiceStub.Address;
import com.cware.netshopping.domain.model.PaNaverAddress;

public class PaNaverAddressVO extends PaNaverAddress {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1069543244416179768L;

	public void setAddress(Address address) {
		if(null != address.getAddressType()) this.setAddressType(address.getAddressType().getValue());
		if(null != address.getZipCode()) this.setZipCode(address.getZipCode());
		if(null != address.getBaseAddress()) this.setBaseAddress(address.getBaseAddress());
		if(null != address.getDetailedAddress()) this.setDetailedAddress(address.getDetailedAddress());
		this.setIsRoadNameAddress(String.valueOf(address.getIsRoadNameAddress()));
		if(null != address.getCity()) this.setCity(address.getCity());
		if(null != address.getState()) this.setState(address.getState());
		if(null != address.getCountry()) this.setCountry(address.getCountry());
		if(null != address.getTel1()) this.setTel1(address.getTel1());
		if(null != address.getTel2()) this.setTel2(address.getTel2());
		if(null != address.getName()) this.setName(address.getName());
	}
}
