package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class MultiResolutionImage {
    private String url_1x;
    private String url_2x;
    private String url_3x;
    
	public String getUrl_1x() {
		return url_1x;
	}
	public void setUrl_1x(String url_1x) {
		this.url_1x = url_1x;
	}
	public String getUrl_2x() {
		return url_2x;
	}
	public void setUrl_2x(String url_2x) {
		this.url_2x = url_2x;
	}
	public String getUrl_3x() {
		return url_3x;
	}
	public void setUrl_3x(String url_3x) {
		this.url_3x = url_3x;
	}
	
	
}
