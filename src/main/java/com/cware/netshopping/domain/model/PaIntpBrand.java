package com.cware.netshopping.domain.model;
import com.cware.framework.core.basic.AbstractModel;

public class PaIntpBrand extends AbstractModel{

	
	private static final long serialVersionUID = 1L;
	
	private String brandCode;
    private String brandNo;
    private String brandName;
    private String brandNameEng;
    
    
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getBrandNo() {
		return brandNo;
	}
	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandNameEng() {
		return brandNameEng;
	}
	public void setBrandNameEng(String brandNameEng) {
		this.brandNameEng = brandNameEng;
	}

    
    
}
