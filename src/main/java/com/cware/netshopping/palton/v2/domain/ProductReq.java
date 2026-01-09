package com.cware.netshopping.palton.v2.domain;

import java.util.List;

public class ProductReq {

	List<Product> spdLst;

	public List<Product> getSpdLst() {
		return spdLst;
	}

	public void setSpdLst(List<Product> spdLst) {
		this.spdLst = spdLst;
	}

	@Override
	public String toString() {
		return "ProductLst [spdLst=" + spdLst + "]";
	}

}
