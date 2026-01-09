package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Brand extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String brandCode;
	private String brandName;
	private String brandImage;
	private String brandDesc;
	private String url;
	private String searchKeyword;
	private String initialKor;
	private String initialEng;

	public String getBrandCode() { 
		return this.brandCode;
	}
	public String getBrandName() { 
		return this.brandName;
	}
	public String getBrandImage() { 
		return this.brandImage;
	}
	public String getBrandDesc() { 
		return this.brandDesc;
	}
	public String getUrl() { 
		return this.url;
	}
	public String getSearchKeyword() { 
		return this.searchKeyword;
	}
	public String getInitialKor() { 
		return this.initialKor;
	}
	public String getInitialEng() { 
		return this.initialEng;
	}

	public void setBrandCode(String brandCode) { 
		this.brandCode = brandCode;
	}
	public void setBrandName(String brandName) { 
		this.brandName = brandName;
	}
	public void setBrandImage(String brandImage) { 
		this.brandImage = brandImage;
	}
	public void setBrandDesc(String brandDesc) { 
		this.brandDesc = brandDesc;
	}
	public void setUrl(String url) { 
		this.url = url;
	}
	public void setSearchKeyword(String searchKeyword) { 
		this.searchKeyword = searchKeyword;
	}
	public void setInitialKor(String initialKor) { 
		this.initialKor = initialKor;
	}
	public void setInitialEng(String initialEng) { 
		this.initialEng = initialEng;
	}
}
