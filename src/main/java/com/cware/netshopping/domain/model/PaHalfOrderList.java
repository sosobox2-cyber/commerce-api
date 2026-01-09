package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaHalfOrderList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	 
	private String ordNo;
	private String ordNoNm;
	private String basketNo;
	private String prdNo;
	private String stockNo;
	private String memNo;
	private String guestNo;
	private String siteCd;
	private String deviceCd;
	private String chkBc;
	private String cbkNm;
	private String toAddr;
	private String toZiCd;
	private String toNm;
	private String toTel;
	private String toEmTel;
	private Timestamp prdSalYmd;// ??????
	private double prdQPri;
	private double deliPri;
	private double payPri;
	private double okcashPri;
	private int    sumQty;
	private String memo;
	private String mandM;
	private String printYn;
	private String delayMail;
	private String eventYn;
	private String location;
	private String confirmCd;
	private String tradeNumber;
	private String appState;
	private String appState02;
	private String guestEmail;
	private String guestTel;
	private String fromNm;
	private String ccInstallment;
	private String cardMethod;
	private String interest;
	private String smsGubun;
	private String pgMid;
	private String useReservePri;
	private String globalDeliPri;
	private String glbNationCd;
	private String pg;
	private Timestamp createDt;
	private Timestamp updateDt;
	private String memoCd;
	private double prdPri;
	private int    qty;
	private double salPri;
	private String dealType;
	private String deliState;
	private String outYmd;
	private String appGubun;
	private double margin;
	private String brdCd;
	private String cpSeq;
	private String cpType;
	private String cartGroup;
	private double deliLimit;
	private double saveReservePst;	//???
	private String optCd;
	private String prdWeight;
	private Timestamp sdYmd;	//????
	private String themeCd;
	private String dealPcode;
	private String trackingnum;
	private String dlvTmpltSeq;
	private String oriOrdNo;
	private String oriOrdNoNm;
	private String cjNo;
	private String selAcntNo;
	private String outNo;
	private String confirmYn;
	private String refundCompleteYn;
	private String rtrnMoneyYn;
	private String stdCtgrNo;
	private String optValueNm;
	private String prdNm;
	private String prdCd;
	private String gubun;
	private String gubun2;
	private String deliFree;
	private String hgubun;
	private String cdeliState;
	private String pgubun;
	private String paOrderGb;
	private String exchangeState;
	private String refundState;
	private String refundBsMemo;
	private String refundNo;
	private Timestamp refundDate;
	private double refundDeliPri;
	private String refundFromAddr;
	private String refundFromEmTel;
	private String refundFromNm;
	private String refundFromTel;
	private String refundFromZiCd;
	private String refundMemo;
	private double exchangeDeliPri;
	private String exchangeOrdNo;
	private String exchangeFromNm;
	private String exchangeFromZiCd;
	private String exchangeFromAddr;
	private String exchangeFromTel;
	private String exchangeFromEmTel;
	private String exchangeMemo;
	private String exchangeBsMemo;
	private Timestamp exchangeCreateDt;
	private String bpPickupTypeNm;
	private String delirtnNo;
	private String exPrdNo;
	private String exStockNo;
	private String exOptValueNm;
	private String exCancelFlag;
	private String reCancelFlag;
	private String pMailYn;
	private String pOrdNo;
	private String isSet;
	private String claimNo;
	private Timestamp claimRequestDate;
	private double couponAmt;
	private String delirtnOffNo;
	
	public String getOrdNo() {
		return ordNo;
	}
	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}
	public String getOrdNoNm() {
		return ordNoNm;
	}
	public void setOrdNoNm(String ordNoNm) {
		this.ordNoNm = ordNoNm;
	}
	public String getBasketNo() {
		return basketNo;
	}
	public void setBasketNo(String basketNo) {
		this.basketNo = basketNo;
	}
	public String getPrdNo() {
		return prdNo;
	}
	public void setPrdNo(String prdNo) {
		this.prdNo = prdNo;
	}
	public String getStockNo() {
		return stockNo;
	}
	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}
	public String getMemNo() {
		return memNo;
	}
	public void setMemNo(String memNo) {
		this.memNo = memNo;
	}
	public String getGuestNo() {
		return guestNo;
	}
	public void setGuestNo(String guestNo) {
		this.guestNo = guestNo;
	}
	public String getSiteCd() {
		return siteCd;
	}
	public void setSiteCd(String siteCd) {
		this.siteCd = siteCd;
	}
	public String getDeviceCd() {
		return deviceCd;
	}
	public void setDeviceCd(String deviceCd) {
		this.deviceCd = deviceCd;
	}
	public String getChkBc() {
		return chkBc;
	}
	public void setChkBc(String chkBc) {
		this.chkBc = chkBc;
	}
	public String getCbkNm() {
		return cbkNm;
	}
	public void setCbkNm(String cbkNm) {
		this.cbkNm = cbkNm;
	}
	public String getToAddr() {
		return toAddr;
	}
	public void setToAddr(String toAddr) {
		this.toAddr = toAddr;
	}
	public String getToZiCd() {
		return toZiCd;
	}
	public void setToZiCd(String toZiCd) {
		this.toZiCd = toZiCd;
	}
	public String getToNm() {
		return toNm;
	}
	public void setToNm(String toNm) {
		this.toNm = toNm;
	}
	public String getToTel() {
		return toTel;
	}
	public void setToTel(String toTel) {
		this.toTel = toTel;
	}
	public String getToEmTel() {
		return toEmTel;
	}
	public void setToEmTel(String toEmTel) {
		this.toEmTel = toEmTel;
	}
	public Timestamp getPrdSalYmd() {
		return prdSalYmd;
	}
	public void setPrdSalYmd(Timestamp prdSalYmd) {
		this.prdSalYmd = prdSalYmd;
	}
	public double getPrdQPri() {
		return prdQPri;
	}
	public void setPrdQPri(double prdQPri) {
		this.prdQPri = prdQPri;
	}
	public double getDeliPri() {
		return deliPri;
	}
	public void setDeliPri(double deliPri) {
		this.deliPri = deliPri;
	}
	public double getPayPri() {
		return payPri;
	}
	public void setPayPri(double payPri) {
		this.payPri = payPri;
	}
	public double getOkcashPri() {
		return okcashPri;
	}
	public void setOkcashPri(double okcashPri) {
		this.okcashPri = okcashPri;
	}
	public int getSumQty() {
		return sumQty;
	}
	public void setSumQty(int sumQty) {
		this.sumQty = sumQty;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMandM() {
		return mandM;
	}
	public void setMandM(String mandM) {
		this.mandM = mandM;
	}
	public String getPrintYn() {
		return printYn;
	}
	public void setPrintYn(String printYn) {
		this.printYn = printYn;
	}
	public String getDelayMail() {
		return delayMail;
	}
	public void setDelayMail(String delayMail) {
		this.delayMail = delayMail;
	}
	public String getEventYn() {
		return eventYn;
	}
	public void setEventYn(String eventYn) {
		this.eventYn = eventYn;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getConfirmCd() {
		return confirmCd;
	}
	public void setConfirmCd(String confirmCd) {
		this.confirmCd = confirmCd;
	}
	public String getTradeNumber() {
		return tradeNumber;
	}
	public void setTradeNumber(String tradeNumber) {
		this.tradeNumber = tradeNumber;
	}
	public String getAppState() {
		return appState;
	}
	public void setAppState(String appState) {
		this.appState = appState;
	}
	public String getAppState02() {
		return appState02;
	}
	public void setAppState02(String appState02) {
		this.appState02 = appState02;
	}
	public String getGuestEmail() {
		return guestEmail;
	}
	public void setGuestEmail(String guestEmail) {
		this.guestEmail = guestEmail;
	}
	public String getGuestTel() {
		return guestTel;
	}
	public void setGuestTel(String guestTel) {
		this.guestTel = guestTel;
	}
	public String getFromNm() {
		return fromNm;
	}
	public void setFromNm(String fromNm) {
		this.fromNm = fromNm;
	}
	public String getCcInstallment() {
		return ccInstallment;
	}
	public void setCcInstallment(String ccInstallment) {
		this.ccInstallment = ccInstallment;
	}
	public String getCardMethod() {
		return cardMethod;
	}
	public void setCardMethod(String cardMethod) {
		this.cardMethod = cardMethod;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getSmsGubun() {
		return smsGubun;
	}
	public void setSmsGubun(String smsGubun) {
		this.smsGubun = smsGubun;
	}
	public String getPgMid() {
		return pgMid;
	}
	public void setPgMid(String pgMid) {
		this.pgMid = pgMid;
	}
	public String getUseReservePri() {
		return useReservePri;
	}
	public void setUseReservePri(String useReservePri) {
		this.useReservePri = useReservePri;
	}
	public String getGlobalDeliPri() {
		return globalDeliPri;
	}
	public void setGlobalDeliPri(String globalDeliPri) {
		this.globalDeliPri = globalDeliPri;
	}
	public String getGlbNationCd() {
		return glbNationCd;
	}
	public void setGlbNationCd(String glbNationCd) {
		this.glbNationCd = glbNationCd;
	}
	public String getPg() {
		return pg;
	}
	public void setPg(String pg) {
		this.pg = pg;
	}
	public Timestamp getCreateDt() {
		return createDt;
	}
	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}
	public Timestamp getUpdateDt() {
		return updateDt;
	}
	public void setUpdateDt(Timestamp updateDt) {
		this.updateDt = updateDt;
	}
	public String getMemoCd() {
		return memoCd;
	}
	public void setMemoCd(String memoCd) {
		this.memoCd = memoCd;
	}
	public double getPrdPri() {
		return prdPri;
	}
	public void setPrdPri(double prdPri) {
		this.prdPri = prdPri;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public double getSalPri() {
		return salPri;
	}
	public void setSalPri(double salPri) {
		this.salPri = salPri;
	}
	public String getDealType() {
		return dealType;
	}
	public void setDealType(String dealType) {
		this.dealType = dealType;
	}
	public String getDeliState() {
		return deliState;
	}
	public void setDeliState(String deliState) {
		this.deliState = deliState;
	}
	public String getOutYmd() {
		return outYmd;
	}
	public void setOutYmd(String outYmd) {
		this.outYmd = outYmd;
	}
	public String getAppGubun() {
		return appGubun;
	}
	public void setAppGubun(String appGubun) {
		this.appGubun = appGubun;
	}
	public double getMargin() {
		return margin;
	}
	public void setMargin(double margin) {
		this.margin = margin;
	}
	public String getBrdCd() {
		return brdCd;
	}
	public void setBrdCd(String brdCd) {
		this.brdCd = brdCd;
	}
	public String getCpSeq() {
		return cpSeq;
	}
	public void setCpSeq(String cpSeq) {
		this.cpSeq = cpSeq;
	}
	public String getCpType() {
		return cpType;
	}
	public void setCpType(String cpType) {
		this.cpType = cpType;
	}
	public String getCartGroup() {
		return cartGroup;
	}
	public void setCartGroup(String cartGroup) {
		this.cartGroup = cartGroup;
	}
	public double getDeliLimit() {
		return deliLimit;
	}
	public void setDeliLimit(double deliLimit) {
		this.deliLimit = deliLimit;
	}
	public double getSaveReservePst() {
		return saveReservePst;
	}
	public void setSaveReservePst(double saveReservePst) {
		this.saveReservePst = saveReservePst;
	}
	public String getOptCd() {
		return optCd;
	}
	public void setOptCd(String optCd) {
		this.optCd = optCd;
	}
	public String getPrdWeight() {
		return prdWeight;
	}
	public void setPrdWeight(String prdWeight) {
		this.prdWeight = prdWeight;
	}
	public Timestamp getSdYmd() {
		return sdYmd;
	}
	public void setSdYmd(Timestamp sdYmd) {
		this.sdYmd = sdYmd;
	}
	public String getThemeCd() {
		return themeCd;
	}
	public void setThemeCd(String themeCd) {
		this.themeCd = themeCd;
	}
	public String getDealPcode() {
		return dealPcode;
	}
	public void setDealPcode(String dealPcode) {
		this.dealPcode = dealPcode;
	}
	public String getTrackingnum() {
		return trackingnum;
	}
	public void setTrackingnum(String trackingnum) {
		this.trackingnum = trackingnum;
	}
	public String getDlvTmpltSeq() {
		return dlvTmpltSeq;
	}
	public void setDlvTmpltSeq(String dlvTmpltSeq) {
		this.dlvTmpltSeq = dlvTmpltSeq;
	}
	public String getOriOrdNo() {
		return oriOrdNo;
	}
	public void setOriOrdNo(String oriOrdNo) {
		this.oriOrdNo = oriOrdNo;
	}
	public String getOriOrdNoNm() {
		return oriOrdNoNm;
	}
	public void setOriOrdNoNm(String oriOrdNoNm) {
		this.oriOrdNoNm = oriOrdNoNm;
	}
	public String getCjNo() {
		return cjNo;
	}
	public void setCjNo(String cjNo) {
		this.cjNo = cjNo;
	}
	public String getSelAcntNo() {
		return selAcntNo;
	}
	public void setSelAcntNo(String selAcntNo) {
		this.selAcntNo = selAcntNo;
	}
	public String getOutNo() {
		return outNo;
	}
	public void setOutNo(String outNo) {
		this.outNo = outNo;
	}
	public String getConfirmYn() {
		return confirmYn;
	}
	public void setConfirmYn(String confirmYn) {
		this.confirmYn = confirmYn;
	}
	public String getRefundCompleteYn() {
		return refundCompleteYn;
	}
	public void setRefundCompleteYn(String refundCompleteYn) {
		this.refundCompleteYn = refundCompleteYn;
	}
	public String getRtrnMoneyYn() {
		return rtrnMoneyYn;
	}
	public void setRtrnMoneyYn(String rtrnMoneyYn) {
		this.rtrnMoneyYn = rtrnMoneyYn;
	}
	public String getStdCtgrNo() {
		return stdCtgrNo;
	}
	public void setStdCtgrNo(String stdCtgrNo) {
		this.stdCtgrNo = stdCtgrNo;
	}
	public String getOptValueNm() {
		return optValueNm;
	}
	public void setOptValueNm(String optValueNm) {
		this.optValueNm = optValueNm;
	}
	public String getPrdNm() {
		return prdNm;
	}
	public void setPrdNm(String prdNm) {
		this.prdNm = prdNm;
	}
	public String getPrdCd() {
		return prdCd;
	}
	public void setPrdCd(String prdCd) {
		this.prdCd = prdCd;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getGubun2() {
		return gubun2;
	}
	public void setGubun2(String gubun2) {
		this.gubun2 = gubun2;
	}
	public String getDeliFree() {
		return deliFree;
	}
	public void setDeliFree(String deliFree) {
		this.deliFree = deliFree;
	}
	public String getHgubun() {
		return hgubun;
	}
	public void setHgubun(String hgubun) {
		this.hgubun = hgubun;
	}
	public String getCdeliState() {
		return cdeliState;
	}
	public void setCdeliState(String cdeliState) {
		this.cdeliState = cdeliState;
	}
	public String getPgubun() {
		return pgubun;
	}
	public void setPgubun(String pgubun) {
		this.pgubun = pgubun;
	}
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getExchangeState() {
		return exchangeState;
	}
	public void setExchangeState(String exchangeState) {
		this.exchangeState = exchangeState;
	}
	public String getRefundState() {
		return refundState;
	}
	public void setRefundState(String refundState) {
		this.refundState = refundState;
	}
	public String getRefundBsMemo() {
		return refundBsMemo;
	}
	public void setRefundBsMemo(String refundBsMemo) {
		this.refundBsMemo = refundBsMemo;
	}
	public String getRefundNo() {
		return refundNo;
	}
	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}
	public Timestamp getRefundDate() {
		return refundDate;
	}
	public void setRefundDate(Timestamp refundDate) {
		this.refundDate = refundDate;
	}
	public double getRefundDeliPri() {
		return refundDeliPri;
	}
	public void setRefundDeliPri(double refundDeliPri) {
		this.refundDeliPri = refundDeliPri;
	}
	public String getRefundFromAddr() {
		return refundFromAddr;
	}
	public void setRefundFromAddr(String refundFromAddr) {
		this.refundFromAddr = refundFromAddr;
	}
	public String getRefundFromEmTel() {
		return refundFromEmTel;
	}
	public void setRefundFromEmTel(String refundFromEmTel) {
		this.refundFromEmTel = refundFromEmTel;
	}
	public String getRefundFromNm() {
		return refundFromNm;
	}
	public void setRefundFromNm(String refundFromNm) {
		this.refundFromNm = refundFromNm;
	}
	public String getRefundFromTel() {
		return refundFromTel;
	}
	public void setRefundFromTel(String refundFromTel) {
		this.refundFromTel = refundFromTel;
	}
	public String getRefundFromZiCd() {
		return refundFromZiCd;
	}
	public void setRefundFromZiCd(String refundFromZiCd) {
		this.refundFromZiCd = refundFromZiCd;
	}
	public String getRefundMemo() {
		return refundMemo;
	}
	public void setRefundMemo(String refundMemo) {
		this.refundMemo = refundMemo;
	}
	public double getExchangeDeliPri() {
		return exchangeDeliPri;
	}
	public void setExchangeDeliPri(double exchangeDeliPri) {
		this.exchangeDeliPri = exchangeDeliPri;
	}
	public String getExchangeOrdNo() {
		return exchangeOrdNo;
	}
	public void setExchangeOrdNo(String exchangeOrdNo) {
		this.exchangeOrdNo = exchangeOrdNo;
	}
	public String getExchangeFromNm() {
		return exchangeFromNm;
	}
	public void setExchangeFromNm(String exchangeFromNm) {
		this.exchangeFromNm = exchangeFromNm;
	}
	public String getExchangeFromZiCd() {
		return exchangeFromZiCd;
	}
	public void setExchangeFromZiCd(String exchangeFromZiCd) {
		this.exchangeFromZiCd = exchangeFromZiCd;
	}
	public String getExchangeFromAddr() {
		return exchangeFromAddr;
	}
	public void setExchangeFromAddr(String exchangeFromAddr) {
		this.exchangeFromAddr = exchangeFromAddr;
	}
	public String getExchangeFromTel() {
		return exchangeFromTel;
	}
	public void setExchangeFromTel(String exchangeFromTel) {
		this.exchangeFromTel = exchangeFromTel;
	}
	public String getExchangeFromEmTel() {
		return exchangeFromEmTel;
	}
	public void setExchangeFromEmTel(String exchangeFromEmTel) {
		this.exchangeFromEmTel = exchangeFromEmTel;
	}
	public String getExchangeMemo() {
		return exchangeMemo;
	}
	public void setExchangeMemo(String exchangeMemo) {
		this.exchangeMemo = exchangeMemo;
	}
	public String getExchangeBsMemo() {
		return exchangeBsMemo;
	}
	public void setExchangeBsMemo(String exchangeBsMemo) {
		this.exchangeBsMemo = exchangeBsMemo;
	}
	public Timestamp getExchangeCreateDt() {
		return exchangeCreateDt;
	}
	public void setExchangeCreateDt(Timestamp exchangeCreateDt) {
		this.exchangeCreateDt = exchangeCreateDt;
	}
	public String getBpPickupTypeNm() {
		return bpPickupTypeNm;
	}
	public void setBpPickupTypeNm(String bpPickupTypeNm) {
		this.bpPickupTypeNm = bpPickupTypeNm;
	}
	public String getDelirtnNo() {
		return delirtnNo;
	}
	public void setDelirtnNo(String delirtnNo) {
		this.delirtnNo = delirtnNo;
	}
	public String getExPrdNo() {
		return exPrdNo;
	}
	public void setExPrdNo(String exPrdNo) {
		this.exPrdNo = exPrdNo;
	}
	public String getExStockNo() {
		return exStockNo;
	}
	public void setExStockNo(String exStockNo) {
		this.exStockNo = exStockNo;
	}
	public String getExOptValueNm() {
		return exOptValueNm;
	}
	public void setExOptValueNm(String exOptValueNm) {
		this.exOptValueNm = exOptValueNm;
	}
	public String getExCancelFlag() {
		return exCancelFlag;
	}
	public void setExCancelFlag(String exCancelFlag) {
		this.exCancelFlag = exCancelFlag;
	}
	public String getReCancelFlag() {
		return reCancelFlag;
	}
	public void setReCancelFlag(String reCancelFlag) {
		this.reCancelFlag = reCancelFlag;
	}
	public String getpMailYn() {
		return pMailYn;
	}
	public void setpMailYn(String pMailYn) {
		this.pMailYn = pMailYn;
	}
	public String getpOrdNo() {
		return pOrdNo;
	}
	public void setpOrdNo(String pOrdNo) {
		this.pOrdNo = pOrdNo;
	}
	public String getIsSet() {
		return isSet;
	}
	public void setIsSet(String isSet) {
		this.isSet = isSet;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public Timestamp getClaimRequestDate() {
		return claimRequestDate;
	}
	public void setClaimRequestDate(Timestamp claimRequestDate) {
		this.claimRequestDate = claimRequestDate;
	}
	public double getCouponAmt() {
		return couponAmt;
	}
	public void setCouponAmt(double couponAmt) {
		this.couponAmt = couponAmt;
	}
	public String getDelirtnOffNo() {
		return delirtnOffNo;
	}
	public void setDelirtnOffNo(String delirtnOffNo) {
		this.delirtnOffNo = delirtnOffNo;
	}

}
