package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Mall {
	private String mallId;
    private String title;
    private String logoUrl;
    private String logo2xUrl;
    private String logo3xUrl;
    
	public String getMallId() {
		return mallId;
	}
	public void setMallId(String mallId) {
		this.mallId = mallId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getLogo2xUrl() {
		return logo2xUrl;
	}
	public void setLogo2xUrl(String logo2xUrl) {
		this.logo2xUrl = logo2xUrl;
	}
	public String getLogo3xUrl() {
		return logo3xUrl;
	}
	public void setLogo3xUrl(String logo3xUrl) {
		this.logo3xUrl = logo3xUrl;
	}
    
    
}
