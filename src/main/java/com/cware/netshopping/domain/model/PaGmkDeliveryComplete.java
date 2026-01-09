package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaGmkDeliveryComplete extends AbstractModel {
    
    private static final long serialVersionUID = 1L;
    
    private String paCode;
    private String packNo;
    private String contrNo;    
    private Timestamp contrDate;
    private Timestamp transDate;
    private Timestamp completeDate;
    private String goodsNo;
    private String venderGoodsNo;
    private Timestamp modifyDate;

    public String getPaCode() {
        return paCode;
    }
    public void setPaCode(String paCode) {
        this.paCode = paCode;
    }
    public String getPackNo() {
        return packNo;
    }
    public void setPackNo(String packNo) {
        this.packNo = packNo;
    }
    public String getContrNo() {
        return contrNo;
    }
    public void setContrNo(String contrNo) {
        this.contrNo = contrNo;
    }
    public Timestamp getContrDate() {
        return contrDate;
    }
    public void setContrDate(Timestamp contrDate) {
        this.contrDate = contrDate;
    }
    public Timestamp getTransDate() {
        return transDate;
    }
    public void setTransDate(Timestamp transDate) {
        this.transDate = transDate;
    }
    public Timestamp getCompleteDate() {
        return completeDate;
    }
    public void setCompleteDate(Timestamp completeDate) {
        this.completeDate = completeDate;
    }
    public String getGoodsNo() {
        return goodsNo;
    }
    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }
    public String getVenderGoodsNo() {
        return venderGoodsNo;
    }
    public void setVenderGoodsNo(String venderGoodsNo) {
        this.venderGoodsNo = venderGoodsNo;
    }
    public Timestamp getModifyDate() {
        return modifyDate;
    }
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }
    
    
        
}
