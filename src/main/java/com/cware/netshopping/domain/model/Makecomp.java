package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Makecomp extends AbstractModel {

    private static final long serialVersionUID = 1L;

    private String makecoCode;
    private String makecoName;

    public String getMakecoCode() {
        return this.makecoCode;
    }
    public String getMakecoName() {
        return this.makecoName;
    }
    
    public void setMakecoCode(String makecoCode) {
	this.makecoCode = makecoCode;
    }
    public void setMakecoName(String makecoName) {
        this.makecoName = makecoName;
    }
    
}
