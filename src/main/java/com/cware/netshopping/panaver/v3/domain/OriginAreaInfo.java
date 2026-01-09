package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OriginAreaInfo {
		
	// 원산지 상세 지역 코드
	private String originAreaCode;
	// 수입사명
	private String importer;
	// 원산지 표시 내용
	private String content;
	// 복수 원산지 여부
	private String plural;
	
	
	public String getOriginAreaCode() {
		return originAreaCode;
	}
	public void setOriginAreaCode(String originAreaCode) {
		this.originAreaCode = originAreaCode;
	}
	public String getImporter() {
		return importer;
	}
	public void setImporter(String importer) {
		this.importer = importer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPlural() {
		return plural;
	}
	public void setPlural(String plural) {
		this.plural = plural;
	}
	
	@Override
	public String toString() {
		return "OriginAreaInfo [originAreaCode=" + originAreaCode +"importer=" + importer + "content=" + content + "plural=" + plural	+"]";
	}

}
