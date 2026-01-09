package com.cware.netshopping.panaver.v3.domain;

import com.cware.netshopping.domain.model.PaNaverAddress;
import com.cware.netshopping.panaver.v3.domain.CollectAddress;
import com.cware.netshopping.panaver.v3.domain.ReturnReceiveAddress;
import com.cware.netshopping.panaver.v3.domain.ShippingAddress;
import com.cware.netshopping.panaver.v3.domain.TakingAddress;

public class PaNaverV3AddressVO extends PaNaverAddress {

	private static final long serialVersionUID = 1L;
	
	public void setShippingAddress(ShippingAddress address) {
		if (null != address.getAddressType()) this.setAddressType(address.getAddressType());
		if (null != address.getZipCode()) this.setZipCode(address.getZipCode());
		if (null != address.getBaseAddress()) this.setBaseAddress(address.getBaseAddress());
		if (null != address.getDetailedAddress()) this.setDetailedAddress(address.getDetailedAddress());
		this.setIsRoadNameAddress(String.valueOf(address.getIsRoadNameAddress()));
		if (null != address.getCity()) this.setCity(address.getCity());
		if (null != address.getState()) this.setState(address.getState());
		if (null != address.getCountry()) this.setCountry(address.getCountry());
		if (null != address.getTel1()) this.setTel1(address.getTel1());
		if (null != address.getTel2()) this.setTel2(address.getTel2());
		if (null != address.getName()) this.setName(address.getName());
	}
	
	public void setTakingAddress(TakingAddress address) {
		if (null != address.getAddressType()) this.setAddressType(address.getAddressType());
		if (null != address.getZipCode()) this.setZipCode(address.getZipCode());
		if (null != address.getBaseAddress()) this.setBaseAddress(address.getBaseAddress());
		if (null != address.getDetailedAddress()) this.setDetailedAddress(address.getDetailedAddress());
		this.setIsRoadNameAddress(String.valueOf(address.getIsRoadNameAddress()));
		if (null != address.getCity()) this.setCity(address.getCity());
		if (null != address.getState()) this.setState(address.getState());
		if (null != address.getCountry()) this.setCountry(address.getCountry());
		if (null != address.getTel1()) this.setTel1(address.getTel1());
		if (null != address.getTel2()) this.setTel2(address.getTel2());
		if (null != address.getName()) this.setName(address.getName());
	}

	public void setCollectAddress(CollectAddress address) {
		if (null != address.getAddressType()) this.setAddressType(address.getAddressType());
		if (null != address.getZipCode()) this.setZipCode(address.getZipCode());
		if (null != address.getBaseAddress()) this.setBaseAddress(address.getBaseAddress());
		if (null != address.getDetailedAddress()) this.setDetailedAddress(address.getDetailedAddress());
		this.setIsRoadNameAddress(String.valueOf(address.getIsRoadNameAddress()));
		if (null != address.getCity()) this.setCity(address.getCity());
		if (null != address.getState()) this.setState(address.getState());
		if (null != address.getCountry()) this.setCountry(address.getCountry());
		if (null != address.getTel1()) this.setTel1(address.getTel1());
		if (null != address.getTel2()) this.setTel2(address.getTel2());
		if (null != address.getName()) this.setName(address.getName());
	}
	
	public void setReturnReceiveAddress(ReturnReceiveAddress address) {
		if (null != address.getAddressType()) this.setAddressType(address.getAddressType());
		if (null != address.getZipCode()) this.setZipCode(address.getZipCode());
		if (null != address.getBaseAddress()) this.setBaseAddress(address.getBaseAddress());
		if (null != address.getDetailedAddress()) this.setDetailedAddress(address.getDetailedAddress());
		this.setIsRoadNameAddress(String.valueOf(address.getIsRoadNameAddress()));
		if (null != address.getCity()) this.setCity(address.getCity());
		if (null != address.getState()) this.setState(address.getState());
		if (null != address.getCountry()) this.setCountry(address.getCountry());
		if (null != address.getTel1()) this.setTel1(address.getTel1());
		if (null != address.getTel2()) this.setTel2(address.getTel2());
		if (null != address.getName()) this.setName(address.getName());
	}
}
