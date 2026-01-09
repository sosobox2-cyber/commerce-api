package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaGmktGoodsCerti;

public class PaGmktGoodsCertiVO extends PaGmktGoodsCerti {

    private static final long serialVersionUID = 1L;
    
    private String itemNo;
    private String paCode;
    
    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getPaCode() {
        return paCode;
    }

    public void setPaCode(String paCode) {
        this.paCode = paCode;
    }
    
}
