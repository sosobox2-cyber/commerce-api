package com.cware.netshopping.pacopn.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BundleInfo {

	String bundleType;

	public String getBundleType() {
		return bundleType;
	}


	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	@Override
	public String toString() {
		return "BundleInfo [bundleType=" + bundleType + "]";
	}

	
}
