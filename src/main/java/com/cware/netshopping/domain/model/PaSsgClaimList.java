package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaSsgClaimList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	private String  paOrderGb; //제휴주문구분[J007]
	private String	shppVenId;
	private String	splVenNm;
	private String	lrnkSplVenNm;
	private String	bfOrderId;
	private String	bfOrderSeq;
	private String	shppDivDtlCd;
	private String	shppDivDtlNm;
	private String	orordNo;
	private String	orordItemSeq;
	private String	ordNo;
	private String	ordItemSeq;
	private String	bfShppNo;
	private String	bfShppSeq;
	private String	shppNo;
	private String	shppSeq;
	private String	itemNm;
	private String	splVenItemId;
	private String	uSplVenItemId;
	private String	itemId;
	private String	uitemNm;
	private String	uitemId;
	private String	orshppNo;
	private String	orshppSeq;
	private String	mdlNm;
	private String	frebieNm;
	private long	dircItemQty;
	private long	procItemQty;
	private long	refusRetProcQty;
	private String	clmRsnCd;
	private String	clmRsnNm;
	private String	clmRsnCntt;
	private String	shppcstBdnMainCd;
	private String	shppcstBdnMainNm;
	private String	rcovMthdCd;
	private String	rcovMthdNm;
	private String	lastShppProgStatDtlCd;
	private String	lastShppProgStatDtlNm;
	private String	shppStatCd;
	private String	shppStatNm;
	private String	retProcStatNm;
	private String	shppTypeDtlCd;
	private String	shppTypeDtlNm;
	private String	delicoVenId;
	private String	delicoVenNm;
	private String	wblNo;
	private String	resellPsblYn;
	private String	retImptMainNm;
	private String	shppcstAddPaymtNm;
	private String	refundYn;
	private String	retRefusRsnNm;
	private String	retProcMemoCntt;
	private String	retProcMemoRegpeId;
	private String	memoCntt;
	private String	rcovDircDt;
	private String	rcovCnfDt;
	private String	rcovCmplDeferDt;
	private String	rcovCmplDt;
	private String	rcovRvkDt;
	private String	ordpeNm;
	private String	rcptpeNm;
	private String	shpplocAddr;
	private String	shpplocZipcd;
	private String	shpplocOldZipcd;
	private String	rcptpeHpno;
	private String	rcptpeTelno;
	private double	sellprc;
	private double	rlordAmt;
	private double	dcAmt;
	private String	shpplocBascAddr;
	private String	shpplocDtlAddr;
	private String	mallTypeCd;
	private String	ordTypeCd06Yn;
	private String	ordItemCertNoYn;
	private String	rejectYn;
	
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getShppVenId() {
		return shppVenId;
	}
	public void setShppVenId(String shppVenId) {
		this.shppVenId = shppVenId;
	}
	public String getSplVenNm() {
		return splVenNm;
	}
	public void setSplVenNm(String splVenNm) {
		this.splVenNm = splVenNm;
	}
	public String getLrnkSplVenNm() {
		return lrnkSplVenNm;
	}
	public void setLrnkSplVenNm(String lrnkSplVenNm) {
		this.lrnkSplVenNm = lrnkSplVenNm;
	}
	public String getBfOrderId() {
		return bfOrderId;
	}
	public void setBfOrderId(String bfOrderId) {
		this.bfOrderId = bfOrderId;
	}
	public String getBfOrderSeq() {
		return bfOrderSeq;
	}
	public void setBfOrderSeq(String bfOrderSeq) {
		this.bfOrderSeq = bfOrderSeq;
	}
	public String getShppDivDtlCd() {
		return shppDivDtlCd;
	}
	public void setShppDivDtlCd(String shppDivDtlCd) {
		this.shppDivDtlCd = shppDivDtlCd;
	}
	public String getShppDivDtlNm() {
		return shppDivDtlNm;
	}
	public void setShppDivDtlNm(String shppDivDtlNm) {
		this.shppDivDtlNm = shppDivDtlNm;
	}
	public String getOrordNo() {
		return orordNo;
	}
	public void setOrordNo(String orordNo) {
		this.orordNo = orordNo;
	}
	public String getOrordItemSeq() {
		return orordItemSeq;
	}
	public void setOrordItemSeq(String orordItemSeq) {
		this.orordItemSeq = orordItemSeq;
	}
	public String getOrdNo() {
		return ordNo;
	}
	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}
	public String getOrdItemSeq() {
		return ordItemSeq;
	}
	public void setOrdItemSeq(String ordItemSeq) {
		this.ordItemSeq = ordItemSeq;
	}
	public String getBfShppNo() {
		return bfShppNo;
	}
	public void setBfShppNo(String bfShppNo) {
		this.bfShppNo = bfShppNo;
	}
	public String getBfShppSeq() {
		return bfShppSeq;
	}
	public void setBfShppSeq(String bfShppSeq) {
		this.bfShppSeq = bfShppSeq;
	}
	public String getShppNo() {
		return shppNo;
	}
	public void setShppNo(String shppNo) {
		this.shppNo = shppNo;
	}
	public String getShppSeq() {
		return shppSeq;
	}
	public void setShppSeq(String shppSeq) {
		this.shppSeq = shppSeq;
	}
	public String getItemNm() {
		return itemNm;
	}
	public void setItemNm(String itemNm) {
		this.itemNm = itemNm;
	}
	public String getSplVenItemId() {
		return splVenItemId;
	}
	public void setSplVenItemId(String splVenItemId) {
		this.splVenItemId = splVenItemId;
	}
	public String getuSplVenItemId() {
		return uSplVenItemId;
	}
	public void setuSplVenItemId(String uSplVenItemId) {
		this.uSplVenItemId = uSplVenItemId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getUitemNm() {
		return uitemNm;
	}
	public void setUitemNm(String uitemNm) {
		this.uitemNm = uitemNm;
	}
	public String getUitemId() {
		return uitemId;
	}
	public void setUitemId(String uitemId) {
		this.uitemId = uitemId;
	}
	public String getOrshppNo() {
		return orshppNo;
	}
	public void setOrshppNo(String orshppNo) {
		this.orshppNo = orshppNo;
	}
	public String getOrshppSeq() {
		return orshppSeq;
	}
	public void setOrshppSeq(String orshppSeq) {
		this.orshppSeq = orshppSeq;
	}
	public String getMdlNm() {
		return mdlNm;
	}
	public void setMdlNm(String mdlNm) {
		this.mdlNm = mdlNm;
	}
	public String getFrebieNm() {
		return frebieNm;
	}
	public void setFrebieNm(String frebieNm) {
		this.frebieNm = frebieNm;
	}
	public long getDircItemQty() {
		return dircItemQty;
	}
	public void setDircItemQty(long dircItemQty) {
		this.dircItemQty = dircItemQty;
	}
	public long getProcItemQty() {
		return procItemQty;
	}
	public void setProcItemQty(long procItemQty) {
		this.procItemQty = procItemQty;
	}
	public long getRefusRetProcQty() {
		return refusRetProcQty;
	}
	public void setRefusRetProcQty(long refusRetProcQty) {
		this.refusRetProcQty = refusRetProcQty;
	}
	public String getClmRsnCd() {
		return clmRsnCd;
	}
	public void setClmRsnCd(String clmRsnCd) {
		this.clmRsnCd = clmRsnCd;
	}
	public String getClmRsnNm() {
		return clmRsnNm;
	}
	public void setClmRsnNm(String clmRsnNm) {
		this.clmRsnNm = clmRsnNm;
	}
	public String getClmRsnCntt() {
		return clmRsnCntt;
	}
	public void setClmRsnCntt(String clmRsnCntt) {
		this.clmRsnCntt = clmRsnCntt;
	}
	public String getShppcstBdnMainCd() {
		return shppcstBdnMainCd;
	}
	public void setShppcstBdnMainCd(String shppcstBdnMainCd) {
		this.shppcstBdnMainCd = shppcstBdnMainCd;
	}
	public String getShppcstBdnMainNm() {
		return shppcstBdnMainNm;
	}
	public void setShppcstBdnMainNm(String shppcstBdnMainNm) {
		this.shppcstBdnMainNm = shppcstBdnMainNm;
	}
	public String getRcovMthdCd() {
		return rcovMthdCd;
	}
	public void setRcovMthdCd(String rcovMthdCd) {
		this.rcovMthdCd = rcovMthdCd;
	}
	public String getRcovMthdNm() {
		return rcovMthdNm;
	}
	public void setRcovMthdNm(String rcovMthdNm) {
		this.rcovMthdNm = rcovMthdNm;
	}
	public String getLastShppProgStatDtlCd() {
		return lastShppProgStatDtlCd;
	}
	public void setLastShppProgStatDtlCd(String lastShppProgStatDtlCd) {
		this.lastShppProgStatDtlCd = lastShppProgStatDtlCd;
	}
	public String getLastShppProgStatDtlNm() {
		return lastShppProgStatDtlNm;
	}
	public void setLastShppProgStatDtlNm(String lastShppProgStatDtlNm) {
		this.lastShppProgStatDtlNm = lastShppProgStatDtlNm;
	}
	public String getShppStatCd() {
		return shppStatCd;
	}
	public void setShppStatCd(String shppStatCd) {
		this.shppStatCd = shppStatCd;
	}
	public String getShppStatNm() {
		return shppStatNm;
	}
	public void setShppStatNm(String shppStatNm) {
		this.shppStatNm = shppStatNm;
	}
	public String getRetProcStatNm() {
		return retProcStatNm;
	}
	public void setRetProcStatNm(String retProcStatNm) {
		this.retProcStatNm = retProcStatNm;
	}
	public String getShppTypeDtlCd() {
		return shppTypeDtlCd;
	}
	public void setShppTypeDtlCd(String shppTypeDtlCd) {
		this.shppTypeDtlCd = shppTypeDtlCd;
	}
	public String getShppTypeDtlNm() {
		return shppTypeDtlNm;
	}
	public void setShppTypeDtlNm(String shppTypeDtlNm) {
		this.shppTypeDtlNm = shppTypeDtlNm;
	}
	public String getDelicoVenId() {
		return delicoVenId;
	}
	public void setDelicoVenId(String delicoVenId) {
		this.delicoVenId = delicoVenId;
	}
	public String getDelicoVenNm() {
		return delicoVenNm;
	}
	public void setDelicoVenNm(String delicoVenNm) {
		this.delicoVenNm = delicoVenNm;
	}
	public String getWblNo() {
		return wblNo;
	}
	public void setWblNo(String wblNo) {
		this.wblNo = wblNo;
	}
	public String getResellPsblYn() {
		return resellPsblYn;
	}
	public void setResellPsblYn(String resellPsblYn) {
		this.resellPsblYn = resellPsblYn;
	}
	public String getRetImptMainNm() {
		return retImptMainNm;
	}
	public void setRetImptMainNm(String retImptMainNm) {
		this.retImptMainNm = retImptMainNm;
	}
	public String getShppcstAddPaymtNm() {
		return shppcstAddPaymtNm;
	}
	public void setShppcstAddPaymtNm(String shppcstAddPaymtNm) {
		this.shppcstAddPaymtNm = shppcstAddPaymtNm;
	}
	public String getRefundYn() {
		return refundYn;
	}
	public void setRefundYn(String refundYn) {
		this.refundYn = refundYn;
	}
	public String getRetRefusRsnNm() {
		return retRefusRsnNm;
	}
	public void setRetRefusRsnNm(String retRefusRsnNm) {
		this.retRefusRsnNm = retRefusRsnNm;
	}
	public String getRetProcMemoCntt() {
		return retProcMemoCntt;
	}
	public void setRetProcMemoCntt(String retProcMemoCntt) {
		this.retProcMemoCntt = retProcMemoCntt;
	}
	public String getRetProcMemoRegpeId() {
		return retProcMemoRegpeId;
	}
	public void setRetProcMemoRegpeId(String retProcMemoRegpeId) {
		this.retProcMemoRegpeId = retProcMemoRegpeId;
	}
	public String getMemoCntt() {
		return memoCntt;
	}
	public void setMemoCntt(String memoCntt) {
		this.memoCntt = memoCntt;
	}
	public String getRcovDircDt() {
		return rcovDircDt;
	}
	public void setRcovDircDt(String rcovDircDt) {
		this.rcovDircDt = rcovDircDt;
	}
	public String getRcovCnfDt() {
		return rcovCnfDt;
	}
	public void setRcovCnfDt(String rcovCnfDt) {
		this.rcovCnfDt = rcovCnfDt;
	}
	public String getRcovCmplDeferDt() {
		return rcovCmplDeferDt;
	}
	public void setRcovCmplDeferDt(String rcovCmplDeferDt) {
		this.rcovCmplDeferDt = rcovCmplDeferDt;
	}
	public String getRcovCmplDt() {
		return rcovCmplDt;
	}
	public void setRcovCmplDt(String rcovCmplDt) {
		this.rcovCmplDt = rcovCmplDt;
	}
	public String getRcovRvkDt() {
		return rcovRvkDt;
	}
	public void setRcovRvkDt(String rcovRvkDt) {
		this.rcovRvkDt = rcovRvkDt;
	}
	public String getOrdpeNm() {
		return ordpeNm;
	}
	public void setOrdpeNm(String ordpeNm) {
		this.ordpeNm = ordpeNm;
	}
	public String getRcptpeNm() {
		return rcptpeNm;
	}
	public void setRcptpeNm(String rcptpeNm) {
		this.rcptpeNm = rcptpeNm;
	}
	public String getShpplocAddr() {
		return shpplocAddr;
	}
	public void setShpplocAddr(String shpplocAddr) {
		this.shpplocAddr = shpplocAddr;
	}
	public String getShpplocZipcd() {
		return shpplocZipcd;
	}
	public void setShpplocZipcd(String shpplocZipcd) {
		this.shpplocZipcd = shpplocZipcd;
	}
	public String getShpplocOldZipcd() {
		return shpplocOldZipcd;
	}
	public void setShpplocOldZipcd(String shpplocOldZipcd) {
		this.shpplocOldZipcd = shpplocOldZipcd;
	}
	public String getRcptpeHpno() {
		return rcptpeHpno;
	}
	public void setRcptpeHpno(String rcptpeHpno) {
		this.rcptpeHpno = rcptpeHpno;
	}
	public String getRcptpeTelno() {
		return rcptpeTelno;
	}
	public void setRcptpeTelno(String rcptpeTelno) {
		this.rcptpeTelno = rcptpeTelno;
	}
	public double getSellprc() {
		return sellprc;
	}
	public void setSellprc(double sellprc) {
		this.sellprc = sellprc;
	}
	public double getRlordAmt() {
		return rlordAmt;
	}
	public void setRlordAmt(double rlordAmt) {
		this.rlordAmt = rlordAmt;
	}
	public double getDcAmt() {
		return dcAmt;
	}
	public void setDcAmt(double dcAmt) {
		this.dcAmt = dcAmt;
	}
	public String getShpplocBascAddr() {
		return shpplocBascAddr;
	}
	public void setShpplocBascAddr(String shpplocBascAddr) {
		this.shpplocBascAddr = shpplocBascAddr;
	}
	public String getShpplocDtlAddr() {
		return shpplocDtlAddr;
	}
	public void setShpplocDtlAddr(String shpplocDtlAddr) {
		this.shpplocDtlAddr = shpplocDtlAddr;
	}
	public String getMallTypeCd() {
		return mallTypeCd;
	}
	public void setMallTypeCd(String mallTypeCd) {
		this.mallTypeCd = mallTypeCd;
	}
	public String getOrdTypeCd06Yn() {
		return ordTypeCd06Yn;
	}
	public void setOrdTypeCd06Yn(String ordTypeCd06Yn) {
		this.ordTypeCd06Yn = ordTypeCd06Yn;
	}
	public String getOrdItemCertNoYn() {
		return ordItemCertNoYn;
	}
	public void setOrdItemCertNoYn(String ordItemCertNoYn) {
		this.ordItemCertNoYn = ordItemCertNoYn;
	}
	public String getRejectYn() {
		return rejectYn;
	}
	public void setRejectYn(String rejectYn) {
		this.rejectYn = rejectYn;
	}
	
		
}
