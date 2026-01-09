package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class MultiResolutionImages {
    private List<String> urls_1x;
    private List<String> urls_2x;
    private List<String> urls_3x;
	public List<String> getUrls_1x() {
		return urls_1x;
	}
	public void setUrls_1x(List<String> urls_1x) {
		this.urls_1x = urls_1x;
	}
	public List<String> getUrls_2x() {
		return urls_2x;
	}
	public void setUrls_2x(List<String> urls_2x) {
		this.urls_2x = urls_2x;
	}
	public List<String> getUrls_3x() {
		return urls_3x;
	}
	public void setUrls_3x(List<String> urls_3x) {
		this.urls_3x = urls_3x;
	}
}
