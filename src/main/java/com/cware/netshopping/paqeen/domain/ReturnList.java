package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ReturnList {
	private List<Return> retrun;

	public List<Return> getRetrun() {
		return retrun;
	}

	public void setRetrun(List<Return> retrun) {
		this.retrun = retrun;
	} 
	
}
