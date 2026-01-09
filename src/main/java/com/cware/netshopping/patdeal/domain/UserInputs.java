package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInputs {
	
	private String inputValue;
	private String inputLabel;
	public String getInputValue() {
		return inputValue;
	}
	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}
	public String getInputLabel() {
		return inputLabel;
	}
	public void setInputLabel(String inputLabel) {
		this.inputLabel = inputLabel;
	}
	
	@Override
	public String toString() {
		return "UserInputs [inputValue=" + inputValue + ", inputLabel=" + inputLabel + "]";
	}

	
}
