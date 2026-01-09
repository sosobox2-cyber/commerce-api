package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OptionData {
	
	// 구매자 작성형
	private List<Inputs> inputs;
	// 옵션 리스트
	private List<Options> options;
	
	public List<Inputs> getInputs() {
		return inputs;
	}
	public void setInputs(List<Inputs> inputs) {
		this.inputs = inputs;
	}
	public List<Options> getOptions() {
		return options;
	}
	public void setOptions(List<Options> options) {
		this.options = options;
	}
	
	@Override
	public String toString() {
		return "OptionData [inputs=" + inputs + ", options=" + options + "]";
	}
	
}
