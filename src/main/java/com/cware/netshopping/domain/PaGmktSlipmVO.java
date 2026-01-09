package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Slipm;

public class PaGmktSlipmVO extends Slipm {

    private static final long serialVersionUID = 1L;

    private String mappingSeq;
    
    private String paOrderNo;
    
    private String paOrderSeq;
    
    private String paDelyGbName;

    private String paDoFlag;
    
    private String paCode;
    
    public String getMappingSeq() {
        return mappingSeq;
    }

    public void setMappingSeq(String mappingSeq) {
        this.mappingSeq = mappingSeq;
    }

    public String getPaOrderNo() {
        return paOrderNo;
    }

    public void setPaOrderNo(String paOrderNo) {
        this.paOrderNo = paOrderNo;
    }

    public String getPaOrderSeq() {
        return paOrderSeq;
    }

    public String getPaDelyGbName() {
        return paDelyGbName;
    }

    public void setPaOrderSeq(String paOrderSeq) {
        this.paOrderSeq = paOrderSeq;
    }

    public void setPaDelyGbName(String paDelyGbName) {
        this.paDelyGbName = paDelyGbName;
    }

    public String getPaDoFlag() {
        return paDoFlag;
    }

    public void setPaDoFlag(String paDoFlag) {
        this.paDoFlag = paDoFlag;
    }

    public String getPaCode() {
        return paCode;
    }

    public void setPaCode(String paCode) {
        this.paCode = paCode;
    }
    
}
