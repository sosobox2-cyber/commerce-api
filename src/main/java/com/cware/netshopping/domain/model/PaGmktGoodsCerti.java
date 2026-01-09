package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaGmktGoodsCerti extends AbstractModel {

    private static final long serialVersionUID = 1L;
    
    private String goodsCode; 
    private String safeCertGroupType; 
    private String certificationType; 
    private String certificationNo; 
    private String certificationTargetCode; 
    private String certificationStatus; 
    private String certificationDate; 
    private String modelName;
    
    public String getGoodsCode() {
        return goodsCode;
    }
    public String getSafeCertGroupType() {
        return safeCertGroupType;
    }
    public String getCertificationType() {
        return certificationType;
    }
    public String getCertificationNo() {
        return certificationNo;
    }
    public String getCertificationTargetCode() {
        return certificationTargetCode;
    }
    public String getCertificationStatus() {
        return certificationStatus;
    }
    public String getCertificationDate() {
        return certificationDate;
    }
    public String getModelName() {
        return modelName;
    }
    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }
    public void setSafeCertGroupType(String safeCertGroupType) {
        this.safeCertGroupType = safeCertGroupType;
    }
    public void setCertificationType(String certificationType) {
        this.certificationType = certificationType;
    }
    public void setCertificationNo(String certificationNo) {
        this.certificationNo = certificationNo;
    }
    public void setCertificationTargetCode(String certificationTargetCode) {
        this.certificationTargetCode = certificationTargetCode;
    }
    public void setCertificationStatus(String certificationStatus) {
        this.certificationStatus = certificationStatus;
    }
    public void setCertificationDate(String certificationDate) {
        this.certificationDate = certificationDate;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    } 
    
}
