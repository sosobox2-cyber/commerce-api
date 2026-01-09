package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaGoodsdtMapping;

public class PaGmktGoodsdtMappingVO extends PaGoodsdtMapping {

    private static final long serialVersionUID = 1L;

    private String itemNo;
    
    private String goodsdtInfoKind;
    
    private String goodsdtInfo;
    
    private String sortType;
    
    private String optionSortType;

    private long ordQty;
    
    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getGoodsdtInfoKind() {
        return goodsdtInfoKind;
    }

    public String getGoodsdtInfo() {
        return goodsdtInfo;
    }

    public void setGoodsdtInfoKind(String goodsdtInfoKind) {
        this.goodsdtInfoKind = goodsdtInfoKind;
    }

    public void setGoodsdtInfo(String goodsdtInfo) {
        this.goodsdtInfo = goodsdtInfo;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getOptionSortType() {
        return optionSortType;
    }

    public void setOptionSortType(String optionSortType) {
        this.optionSortType = optionSortType;
    }

    public long getOrdQty() {
        return ordQty;
    }

    public void setOrdQty(long ordQty) {
        this.ordQty = ordQty;
    }
    
}
