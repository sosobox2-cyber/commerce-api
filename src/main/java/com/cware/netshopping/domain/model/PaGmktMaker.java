package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaGmktMaker extends AbstractModel {
    
    private static final long serialVersionUID = 1L;
    
    private String makecoCode;
    private String makerNo;
    private String makerName;
    
    public String getMakecoCode() {
        return this.makecoCode;
    }
    public String getMakerNo() {
        return this.makerNo;
    }
    public String getMakerName() {
        return this.makerName;
    }

    public void setMakecoCode(String makecoCode) {
	this.makecoCode = makecoCode;
    }
    public void setMakerNo(String makerNo) {
	this.makerNo = makerNo;
    }
    public void setMakerName(String makerName) {
	this.makerName = makerName;
    }
    
}
