package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaGoodsOffer;

public class PaGmktGoodsOfferVO extends PaGoodsOffer {

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
