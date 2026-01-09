package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class IsbnInfo {
	
	// SBN 13자리
	private String isbn13;
	// ISSN 8자리
	private String issn;
	// 독립출판물 여부
	private String independentPublicationYn;
	
	
	public String getIsbn13() {
		return isbn13;
	}
	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}
	public String getIssn() {
		return issn;
	}
	public void setIssn(String issn) {
		this.issn = issn;
	}
	public String getIndependentPublicationYn() {
		return independentPublicationYn;
	}
	public void setIndependentPublicationYn(String independentPublicationYn) {
		this.independentPublicationYn = independentPublicationYn;
	}
	
	@Override
	public String toString() {
		return "IsbnInfo [isbn13=" + isbn13 +  "issn=" + issn +  "independentPublicationYn=" + independentPublicationYn+ "]";
	}

}
