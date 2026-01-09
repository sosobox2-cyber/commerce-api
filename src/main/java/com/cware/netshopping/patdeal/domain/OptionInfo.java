package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OptionInfo {
	
	// 옵션 타입
	private String type;
	
	// 옵션 선택 타입
	private String selectType;
	
	// 옵션명 목록
	private List<String> labels;

	//일체형 옵션
	private List<FlatOption> flatOptions;
	
	//구매자 작성형 정보
	private List<Inputs> inputs;
	
	//분리형 옵션
	private List<MultiLevelOption> multiLevelOptions;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<FlatOption> getFlatOptions() {
		return flatOptions;
	}

	public void setFlatOptions(List<FlatOption> flatOptions) {
		this.flatOptions = flatOptions;
	}

	public List<Inputs> getInputs() {
		return inputs;
	}

	public void setInputs(List<Inputs> inputs) {
		this.inputs = inputs;
	}

	public List<MultiLevelOption> getMultiLevelOptions() {
		return multiLevelOptions;
	}

	public void setMultiLevelOptions(List<MultiLevelOption> multiLevelOptions) {
		this.multiLevelOptions = multiLevelOptions;
	}
	
	
	
}
