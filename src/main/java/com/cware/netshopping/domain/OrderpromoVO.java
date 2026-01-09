package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.Orderpromo;

/**
 *
 * @author Commerceware
 *
 */
public class OrderpromoVO extends Orderpromo {

	private static final long serialVersionUID = 1L;

	private String promoName;
	private String custNo;
	private String lotteryPromoNo;
	private String couponPromoNo;
	private String appType;
	private double Amount;
	private String couponYn;
	private String useYn;
	private String couponSeq;
	private String couponLotteryPromoName;
	private String masterSelecter;
    private Timestamp couponPromoBdate;
    private Timestamp couponPromoEdate;
    private String couponUseFixYn;
    private long couponUseDay; //= PKG11 쿠폰 유효기간 체크 변경 - 20110328 by kst
    private long validDays; //= PKG11 적립금 프로모션의 유효기간 세팅 변경 - 20110328 by kst
    private long cancelQty;
    private String limitYn;
    private String procGb;
    private String paGroupCode;
    private String useCode;    
    private double doAmt;
    
    
	public double getDoAmt() {
		return doAmt;
	}
	public void setDoAmt(double doAmt) {
		this.doAmt = doAmt;
	}
	public long getCancelQty() {
		return cancelQty;
	}
	public void setCancelQty(long cancelQty) {
		this.cancelQty = cancelQty;
	}
	public String getPromoName() {
		return promoName;
	}
	public void setPromoName(String promoName) {
		this.promoName = promoName;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getLotteryPromoNo() {
		return lotteryPromoNo;
	}
	public void setLotteryPromoNo(String lotteryPromoNo) {
		this.lotteryPromoNo = lotteryPromoNo;
	}
	public String getCouponPromoNo() {
		return couponPromoNo;
	}
	public void setCouponPromoNo(String couponPromoNo) {
		this.couponPromoNo = couponPromoNo;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public double getAmount() {
		return Amount;
	}
	public void setAmount(double amount) {
		Amount = amount;
	}
	public String getCouponYn() {
		return couponYn;
	}
	public void setCouponYn(String couponYn) {
		this.couponYn = couponYn;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getCouponSeq() {
		return couponSeq;
	}
	public void setCouponSeq(String couponSeq) {
		this.couponSeq = couponSeq;
	}
	public String getCouponLotteryPromoName() {
		return couponLotteryPromoName;
	}
	public void setCouponLotteryPromoName(String couponLotteryPromoName) {
		this.couponLotteryPromoName = couponLotteryPromoName;
	}
	public String getMasterSelecter() {
		return masterSelecter;
	}
	public void setMasterSelecter(String masterSelecter) {
		this.masterSelecter = masterSelecter;
	}
	public Timestamp getCouponPromoBdate() {
		return couponPromoBdate;
	}
	public void setCouponPromoBdate(Timestamp couponPromoBdate) {
		this.couponPromoBdate = couponPromoBdate;
	}
	public Timestamp getCouponPromoEdate() {
		return couponPromoEdate;
	}
	public void setCouponPromoEdate(Timestamp couponPromoEdate) {
		this.couponPromoEdate = couponPromoEdate;
	}
	public String getCouponUseFixYn() {
		return couponUseFixYn;
	}
	public void setCouponUseFixYn(String couponUseFixYn) {
		this.couponUseFixYn = couponUseFixYn;
	}
	public long getCouponUseDay() {
		return couponUseDay;
	}
	public void setCouponUseDay(long couponUseDay) {
		this.couponUseDay = couponUseDay;
	}
	public long getValidDays() {
		return validDays;
	}
	public void setValidDays(long validDays) {
		this.validDays = validDays;
	}
	public String getLimitYn() {
		return limitYn;
	}
	public void setLimitYn(String limitYn) {
		this.limitYn = limitYn;
	}
	public String getProcGb() {
		return procGb;
	}
	public void setProcGb(String procGb) {
		this.procGb = procGb;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getUseCode() {
		return useCode;
	}
	public void setUseCode(String useCode) {
		this.useCode = useCode;
	}
	
	
}