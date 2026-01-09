package com.cware.netshopping.palton.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SpicInfo {
	// 스마트픽유형코드목록 [공통코드 : SPIC_TYP_CD]
	// 해당되는 스마트픽유형코드들을 입력한다.
	// CRSS 내주변픽업
	// RVS 리버스픽
	// STR 매장픽업
	List<String> spicTypCdLst;

	public List<String> getSpicTypCdLst() {
		return spicTypCdLst;
	}

	public void setSpicTypCdLst(List<String> spicTypCdLst) {
		this.spicTypCdLst = spicTypCdLst;
	}

	@Override
	public String toString() {
		return "SpicInfo [spicTypCdLst=" + spicTypCdLst + "]";
	}
	
	
}
