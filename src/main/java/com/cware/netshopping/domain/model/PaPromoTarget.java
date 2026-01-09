package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaPromoTarget extends AbstractModel {

    private static final long serialVersionUID = 1L;
    private String promoNo;
    private String paCode;
    private String goodsCode;
    private String seq;
    private String doType;
    private String couponYn;
    private String immediatelyYn;
    private Timestamp promoBdate;
    private Timestamp promoEdate;
    private double salePrice; 
    private double dcAmt;
    private String amtRateFlag;
    private double doRate;
    private double doAmt;
    private double standardAppAmt;
    private double limitAmt;
    private String useCode;
    private double ownCost;
    private double entpCost;
    private double doCost;
    private double doOwnCost;
    private double doEntpCost;
    private Timestamp transDate;
    private String remarkV1;
    private String remarkN1;
    private String procGb;
    private String paGroupCode;
    
    //21-05-04 프로시저 이관 중 추가 
    private String oldPromoNo;
    private String oldGoodsName;
    private Timestamp oldPromoEdate;
    private String oldAmtRateFlag;
    private double oldDoRate;
    private double oldDoAmt;
    private double oldStandardAppAmt;
    private double oldLimitAmt;
    private String oldUseCode;
    private double oldOwnCost;
    private double oldEntpCost;
    private String oldProcGb;
    private int promoSort;
    private String dateTime;
    
    private Timestamp compareDate;
    private String exceptReason;
    private String alcoutPromoYn;
    
    
    private String massTargetYn;
    
    
	public String getMassTargetYn() {
		return massTargetYn;
	}
	public void setMassTargetYn(String massTargetYn) {
		this.massTargetYn = massTargetYn;
	}
	public String getAlcoutPromoYn() {
		return alcoutPromoYn;
	}
	public void setAlcoutPromoYn(String alcoutPromoYn) {
		this.alcoutPromoYn = alcoutPromoYn;
	}
	public Timestamp getCompareDate() {
		return compareDate;
	}
	public void setCompareDate(Timestamp compareDate) {
		this.compareDate = compareDate;
	}
	public String getExceptReason() {
		return exceptReason;
	}
	public void setExceptReason(String exceptReason) {
		this.exceptReason = exceptReason;
	}
	public String getProcGb() {
		return procGb;
	}
	public void setProcGb(String procGb) {
		this.procGb = procGb;
	}
	public String getPromoNo() {
		return promoNo;
	}
	public void setPromoNo(String promoNo) {
		this.promoNo = promoNo;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getDoType() {
		return doType;
	}
	public void setDoType(String doType) {
		this.doType = doType;
	}
	public String getCouponYn() {
		return couponYn;
	}
	public void setCouponYn(String couponYn) {
		this.couponYn = couponYn;
	}
	public String getImmediatelyYn() {
		return immediatelyYn;
	}
	public void setImmediatelyYn(String immediatelyYn) {
		this.immediatelyYn = immediatelyYn;
	}
	public Timestamp getPromoBdate() {
		return promoBdate;
	}
	public void setPromoBdate(Timestamp promoBdate) {
		this.promoBdate = promoBdate;
	}
	public Timestamp getPromoEdate() {
		return promoEdate;
	}
	public void setPromoEdate(Timestamp promoEdate) {
		this.promoEdate = promoEdate;
	}
	public double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}
	public double getDcAmt() {
		return dcAmt;
	}
	public void setDcAmt(double dcAmt) {
		this.dcAmt = dcAmt;
	}
	public String getAmtRateFlag() {
		return amtRateFlag;
	}
	public void setAmtRateFlag(String amtRateFlag) {
		this.amtRateFlag = amtRateFlag;
	}
	public double getDoRate() {
		return doRate;
	}
	public void setDoRate(double doRate) {
		this.doRate = doRate;
	}
	public double getDoAmt() {
		return doAmt;
	}
	public void setDoAmt(double doAmt) {
		this.doAmt = doAmt;
	}
	public double getStandardAppAmt() {
		return standardAppAmt;
	}
	public void setStandardAppAmt(double standardAppAmt) {
		this.standardAppAmt = standardAppAmt;
	}
	public double getLimitAmt() {
		return limitAmt;
	}
	public void setLimitAmt(double limitAmt) {
		this.limitAmt = limitAmt;
	}
	public String getUseCode() {
		return useCode;
	}
	public void setUseCode(String useCode) {
		this.useCode = useCode;
	}
	public double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(double ownCost) {
		this.ownCost = ownCost;
	}
	public double getEntpCost() {
		return entpCost;
	}
	public void setEntpCost(double entpCost) {
		this.entpCost = entpCost;
	}
	public double getDoCost() {
		return doCost;
	}
	public void setDoCost(double doCost) {
		this.doCost = doCost;
	}
	public double getDoOwnCost() {
		return doOwnCost;
	}
	public void setDoOwnCost(double doOwnCost) {
		this.doOwnCost = doOwnCost;
	}
	public double getDoEntpCost() {
		return doEntpCost;
	}
	public void setDoEntpCost(double doEntpCost) {
		this.doEntpCost = doEntpCost;
	}
	public Timestamp getTransDate() {
		return transDate;
	}
	public void setTransDate(Timestamp transDate) {
		this.transDate = transDate;
	}
	public String getRemarkV1() {
		return remarkV1;
	}
	public void setRemarkV1(String remarkV1) {
		this.remarkV1 = remarkV1;
	}
	public String getRemarkN1() {
		return remarkN1;
	}
	public void setRemarkN1(String remarkN1) {
		this.remarkN1 = remarkN1;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getOldPromoNo() {
		return oldPromoNo;
	}
	public void setOldPromoNo(String oldPromoNo) {
		this.oldPromoNo = oldPromoNo;
	}
	public String getOldGoodsName() {
		return oldGoodsName;
	}
	public void setOldGoodsName(String oldGoodsName) {
		this.oldGoodsName = oldGoodsName;
	}
	public Timestamp getOldPromoEdate() {
		return oldPromoEdate;
	}
	public void setOldPromoEdate(Timestamp oldPromoEdate) {
		this.oldPromoEdate = oldPromoEdate;
	}
	public String getOldAmtRateFlag() {
		return oldAmtRateFlag;
	}
	public void setOldAmtRateFlag(String oldAmtRateFlag) {
		this.oldAmtRateFlag = oldAmtRateFlag;
	}
	public double getOldDoRate() {
		return oldDoRate;
	}
	public void setOldDoRate(double oldDoRate) {
		this.oldDoRate = oldDoRate;
	}
	public double getOldDoAmt() {
		return oldDoAmt;
	}
	public void setOldDoAmt(double oldDoAmt) {
		this.oldDoAmt = oldDoAmt;
	}
	public double getOldStandardAppAmt() {
		return oldStandardAppAmt;
	}
	public void setOldStandardAppAmt(double oldStandardAppAmt) {
		this.oldStandardAppAmt = oldStandardAppAmt;
	}
	public double getOldLimitAmt() {
		return oldLimitAmt;
	}
	public void setOldLimitAmt(double oldLimitAmt) {
		this.oldLimitAmt = oldLimitAmt;
	}
	public String getOldUseCode() {
		return oldUseCode;
	}
	public void setOldUseCode(String oldUseCode) {
		this.oldUseCode = oldUseCode;
	}
	public double getOldOwnCost() {
		return oldOwnCost;
	}
	public void setOldOwnCost(double oldOwnCost) {
		this.oldOwnCost = oldOwnCost;
	}
	public double getOldEntpCost() {
		return oldEntpCost;
	}
	public void setOldEntpCost(double oldEntpCost) {
		this.oldEntpCost = oldEntpCost;
	}
	public String getOldProcGb() {
		return oldProcGb;
	}
	public void setOldProcGb(String oldProcGb) {
		this.oldProcGb = oldProcGb;
	}
	public int getPromoSort() {
		return promoSort;
	}
	public void setPromoSort(int promoSort) {
		this.promoSort = promoSort;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
}
