package com.cware.netshopping.pawemp.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Option {
	// 선택형 옵션 사용여부(Y:사용, N:미사용) 필수
	String selectOptionUseYn;
	// 선택형 옵션단계(1~2) 필수
	int selectOptionDepth;
	// 선택형 옵션제목1(최대 10자) 필수
	String selectOptionTitle1;
	// 선택형 옵션제목2(최대 10자)
	String selectOptionTitle2;
	// 선택형 옵션제목3(최대 10자)
	String selectOptionTitle3;
	// 선택형 옵션제목4(최대 10자)
	String selectOptionTitle4;
	// 선택형 옵션제목5(최대 10자)
	String selectOptionTitle5;
	// 선택형 옵션 리스트(최대 200개)
	List<OptionValue> selectOptionValueList;
	// 텍스트 옵션 사용여부 (Y:사용, N:미사용) 필수
	String textOptionUseYn;
	// 텍스트 옵션단계(1~2)
	Integer textOptionDepth;
	// 텍스트 옵션제목1 (최대 15자)
	String textOptionTitle1;
	// 텍스트 옵션제목2 (최대 15자)
	String textOptionTitle2;

	public String getSelectOptionUseYn() {
		return selectOptionUseYn;
	}

	public void setSelectOptionUseYn(String selectOptionUseYn) {
		this.selectOptionUseYn = selectOptionUseYn;
	}

	public int getSelectOptionDepth() {
		return selectOptionDepth;
	}

	public void setSelectOptionDepth(int selectOptionDepth) {
		this.selectOptionDepth = selectOptionDepth;
	}

	public String getSelectOptionTitle1() {
		return selectOptionTitle1;
	}

	public void setSelectOptionTitle1(String selectOptionTitle1) {
		this.selectOptionTitle1 = selectOptionTitle1;
	}

	public String getSelectOptionTitle2() {
		return selectOptionTitle2;
	}

	public void setSelectOptionTitle2(String selectOptionTitle2) {
		this.selectOptionTitle2 = selectOptionTitle2;
	}

	public String getSelectOptionTitle3() {
		return selectOptionTitle3;
	}

	public void setSelectOptionTitle3(String selectOptionTitle3) {
		this.selectOptionTitle3 = selectOptionTitle3;
	}

	public String getSelectOptionTitle4() {
		return selectOptionTitle4;
	}

	public void setSelectOptionTitle4(String selectOptionTitle4) {
		this.selectOptionTitle4 = selectOptionTitle4;
	}

	public String getSelectOptionTitle5() {
		return selectOptionTitle5;
	}

	public void setSelectOptionTitle5(String selectOptionTitle5) {
		this.selectOptionTitle5 = selectOptionTitle5;
	}

	public List<OptionValue> getSelectOptionValueList() {
		return selectOptionValueList;
	}

	public void setSelectOptionValueList(List<OptionValue> selectOptionValueList) {
		this.selectOptionValueList = selectOptionValueList;
	}

	public String getTextOptionUseYn() {
		return textOptionUseYn;
	}

	public void setTextOptionUseYn(String textOptionUseYn) {
		this.textOptionUseYn = textOptionUseYn;
	}

	public Integer getTextOptionDepth() {
		return textOptionDepth;
	}

	public void setTextOptionDepth(Integer textOptionDepth) {
		this.textOptionDepth = textOptionDepth;
	}

	public String getTextOptionTitle1() {
		return textOptionTitle1;
	}

	public void setTextOptionTitle1(String textOptionTitle1) {
		this.textOptionTitle1 = textOptionTitle1;
	}

	public String getTextOptionTitle2() {
		return textOptionTitle2;
	}

	public void setTextOptionTitle2(String textOptionTitle2) {
		this.textOptionTitle2 = textOptionTitle2;
	}

	@Override
	public String toString() {
		return "Option [selectOptionUseYn=" + selectOptionUseYn + ", selectOptionDepth=" + selectOptionDepth
				+ ", selectOptionTitle1=" + selectOptionTitle1 + ", selectOptionTitle2=" + selectOptionTitle2
				+ ", selectOptionTitle3=" + selectOptionTitle3 + ", selectOptionTitle4=" + selectOptionTitle4
				+ ", selectOptionTitle5=" + selectOptionTitle5 + ", selectOptionValueList=" + selectOptionValueList
				+ ", textOptionUseYn=" + textOptionUseYn + ", textOptionDepth=" + textOptionDepth
				+ ", textOptionTitle1=" + textOptionTitle1 + ", textOptionTitle2=" + textOptionTitle2 + "]";
	}

}
