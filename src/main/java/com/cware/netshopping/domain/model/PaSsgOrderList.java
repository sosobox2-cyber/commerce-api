package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaSsgOrderList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	private String ordNo;
	private String ordItemSeq;
	private String ordCmplDts;
	private String shppNo;
	private String shppSeq;
	private String shppTabProgStatCd;
	private String bfOrderId;
	private String bfOrderSeq;
	private String evntSeq;
	private String shppDivDtlCd;
	private String shppDivDtlNm;
	private String reOrderYn;
	private String delayNts;
	private String lastShppProgStatDtlNm;
	private String lastShppProgStatDtlCd;
	private String shppVenId;
	private String shppVenNm;
	private String shppLrnkVenId;
	private String shppLrnkVenNm;
	private String shortgProgStatCd;
	private String shortgProgStatNm;
	private String shppTypeNm;
	private String shppTypeCd;
	private String shppTypeDtlNm;
	private String shppTypeDtlCd;
	private String delicoVenId;
	private String delicoVenNm;
	private String wblNo;
	private String boxNo;
	private double shppcst;
	private String shppcstCodYn;
	private String frgShppcstProcDivCd;
	private String frgShppcstProcDivCdNm;
	private String itemNm;
	private String splVenItemId;
	private String uSplVenItemId;
	private String itemId;
	private String uitemId;
	private String uitemNm;
	private String mdlNm;
	private String speSalestrNo;
	private long dircItemQty;
	private long cnclItemQty;
	private long ordQty;
	private double splPrc;
	private double sellprc;
	private double dcAmt;
	private double rlordAmt;
	private String frgShppYn;
	private String wgtRegYn;
	private String ordpeNm;
	private String ordpeHpno;
	private String rcptpeNm;
	private String rcptpeHpno;
	private String rcptpeTelno;
	private String shpplocAddr;
	private String shpplocZipcd;
	private String shpplocOldZipcd;
	private String shpplocRoadAddr;
	private String frebieNm;
	private String memoCntt;
	private String ordMemoCntt;
	private String itemChrctDivCd;
	private String shppStatCd;
	private String shppStatNm;
	private String orordNo;
	private String orOrdNo;
	private String orordItemSeq;
	private String wblRegErrCdNm;
	private String wblRegErrCd;
	private String shppMainCd;
	private String siteNo;
	private String siteNm;
	private String pCus;
	private String allnOrdNo;
	private String oldOrdNo;
	private String itemDiv;
	private String shpplocBascAddr;
	private String shpplocDtlAddr;
	private String ordItemDivNm;
	private String autoShortgYn;
	private String whoutCritnDt;
	private String frgShpplocCntryCd;
	private String frgShpplocHpno;
	private String frgShpplocTelno;
	private String frgShpplocZipcd;
	private String frgShpplocBascAddr;
	private String frgShpplocDtlAddr;
	private String whddDivCd;
	private String whinDelayNts;
	private String ordProcDemndStatCd;
	private String mallTypeCd;
	private String ordTypeCd06Yn;
	private String ordItemCertNoYn;
	private String ordpeRoadAddr;
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
	public String getOrdCmplDts() {
		return ordCmplDts;
	}
	public void setOrdCmplDts(String ordCmplDts) {
		this.ordCmplDts = ordCmplDts;
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
	public String getShppTabProgStatCd() {
		return shppTabProgStatCd;
	}
	public void setShppTabProgStatCd(String shppTabProgStatCd) {
		this.shppTabProgStatCd = shppTabProgStatCd;
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
	public String getEvntSeq() {
		return evntSeq;
	}
	public void setEvntSeq(String evntSeq) {
		this.evntSeq = evntSeq;
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
	public String getReOrderYn() {
		return reOrderYn;
	}
	public void setReOrderYn(String reOrderYn) {
		this.reOrderYn = reOrderYn;
	}
	public String getDelayNts() {
		return delayNts;
	}
	public void setDelayNts(String delayNts) {
		this.delayNts = delayNts;
	}
	public String getLastShppProgStatDtlNm() {
		return lastShppProgStatDtlNm;
	}
	public void setLastShppProgStatDtlNm(String lastShppProgStatDtlNm) {
		this.lastShppProgStatDtlNm = lastShppProgStatDtlNm;
	}
	public String getLastShppProgStatDtlCd() {
		return lastShppProgStatDtlCd;
	}
	public void setLastShppProgStatDtlCd(String lastShppProgStatDtlCd) {
		this.lastShppProgStatDtlCd = lastShppProgStatDtlCd;
	}
	public String getShppVenId() {
		return shppVenId;
	}
	public void setShppVenId(String shppVenId) {
		this.shppVenId = shppVenId;
	}
	public String getShppVenNm() {
		return shppVenNm;
	}
	public void setShppVenNm(String shppVenNm) {
		this.shppVenNm = shppVenNm;
	}
	public String getShppLrnkVenId() {
		return shppLrnkVenId;
	}
	public void setShppLrnkVenId(String shppLrnkVenId) {
		this.shppLrnkVenId = shppLrnkVenId;
	}
	public String getShppLrnkVenNm() {
		return shppLrnkVenNm;
	}
	public void setShppLrnkVenNm(String shppLrnkVenNm) {
		this.shppLrnkVenNm = shppLrnkVenNm;
	}
	public String getShortgProgStatCd() {
		return shortgProgStatCd;
	}
	public void setShortgProgStatCd(String shortgProgStatCd) {
		this.shortgProgStatCd = shortgProgStatCd;
	}
	public String getShortgProgStatNm() {
		return shortgProgStatNm;
	}
	public void setShortgProgStatNm(String shortgProgStatNm) {
		this.shortgProgStatNm = shortgProgStatNm;
	}
	public String getShppTypeNm() {
		return shppTypeNm;
	}
	public void setShppTypeNm(String shppTypeNm) {
		this.shppTypeNm = shppTypeNm;
	}
	public String getShppTypeCd() {
		return shppTypeCd;
	}
	public void setShppTypeCd(String shppTypeCd) {
		this.shppTypeCd = shppTypeCd;
	}
	public String getShppTypeDtlNm() {
		return shppTypeDtlNm;
	}
	public void setShppTypeDtlNm(String shppTypeDtlNm) {
		this.shppTypeDtlNm = shppTypeDtlNm;
	}
	public String getShppTypeDtlCd() {
		return shppTypeDtlCd;
	}
	public void setShppTypeDtlCd(String shppTypeDtlCd) {
		this.shppTypeDtlCd = shppTypeDtlCd;
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
	public String getBoxNo() {
		return boxNo;
	}
	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}
	public double getShppcst() {
		return shppcst;
	}
	public void setShppcst(double shppcst) {
		this.shppcst = shppcst;
	}
	public String getShppcstCodYn() {
		return shppcstCodYn;
	}
	public void setShppcstCodYn(String shppcstCodYn) {
		this.shppcstCodYn = shppcstCodYn;
	}
	public String getFrgShppcstProcDivCd() {
		return frgShppcstProcDivCd;
	}
	public void setFrgShppcstProcDivCd(String frgShppcstProcDivCd) {
		this.frgShppcstProcDivCd = frgShppcstProcDivCd;
	}
	public String getFrgShppcstProcDivCdNm() {
		return frgShppcstProcDivCdNm;
	}
	public void setFrgShppcstProcDivCdNm(String frgShppcstProcDivCdNm) {
		this.frgShppcstProcDivCdNm = frgShppcstProcDivCdNm;
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
	public String getUitemId() {
		return uitemId;
	}
	public void setUitemId(String uitemId) {
		this.uitemId = uitemId;
	}
	public String getUitemNm() {
		return uitemNm;
	}
	public void setUitemNm(String uitemNm) {
		this.uitemNm = uitemNm;
	}
	public String getMdlNm() {
		return mdlNm;
	}
	public void setMdlNm(String mdlNm) {
		this.mdlNm = mdlNm;
	}
	public String getSpeSalestrNo() {
		return speSalestrNo;
	}
	public void setSpeSalestrNo(String speSalestrNo) {
		this.speSalestrNo = speSalestrNo;
	}
	public long getDircItemQty() {
		return dircItemQty;
	}
	public void setDircItemQty(long dircItemQty) {
		this.dircItemQty = dircItemQty;
	}
	public long getCnclItemQty() {
		return cnclItemQty;
	}
	public void setCnclItemQty(long cnclItemQty) {
		this.cnclItemQty = cnclItemQty;
	}
	public long getOrdQty() {
		return ordQty;
	}
	public void setOrdQty(long ordQty) {
		this.ordQty = ordQty;
	}
	public double getSplPrc() {
		return splPrc;
	}
	public void setSplPrc(double splPrc) {
		this.splPrc = splPrc;
	}
	public double getSellprc() {
		return sellprc;
	}
	public void setSellprc(double sellprc) {
		this.sellprc = sellprc;
	}
	public double getDcAmt() {
		return dcAmt;
	}
	public void setDcAmt(double dcAmt) {
		this.dcAmt = dcAmt;
	}
	public double getRlordAmt() {
		return rlordAmt;
	}
	public void setRlordAmt(double rlordAmt) {
		this.rlordAmt = rlordAmt;
	}
	public String getFrgShppYn() {
		return frgShppYn;
	}
	public void setFrgShppYn(String frgShppYn) {
		this.frgShppYn = frgShppYn;
	}
	public String getWgtRegYn() {
		return wgtRegYn;
	}
	public void setWgtRegYn(String wgtRegYn) {
		this.wgtRegYn = wgtRegYn;
	}
	public String getOrdpeNm() {
		return ordpeNm;
	}
	public void setOrdpeNm(String ordpeNm) {
		this.ordpeNm = ordpeNm;
	}
	public String getOrdpeHpno() {
		return ordpeHpno;
	}
	public void setOrdpeHpno(String ordpeHpno) {
		this.ordpeHpno = ordpeHpno;
	}
	public String getRcptpeNm() {
		return rcptpeNm;
	}
	public void setRcptpeNm(String rcptpeNm) {
		this.rcptpeNm = rcptpeNm;
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
	public String getShpplocRoadAddr() {
		return shpplocRoadAddr;
	}
	public void setShpplocRoadAddr(String shpplocRoadAddr) {
		this.shpplocRoadAddr = shpplocRoadAddr;
	}
	public String getFrebieNm() {
		return frebieNm;
	}
	public void setFrebieNm(String frebieNm) {
		this.frebieNm = frebieNm;
	}
	public String getMemoCntt() {
		return memoCntt;
	}
	public void setMemoCntt(String memoCntt) {
		this.memoCntt = memoCntt;
	}
	public String getOrdMemoCntt() {
		return ordMemoCntt;
	}
	public void setOrdMemoCntt(String ordMemoCntt) {
		this.ordMemoCntt = ordMemoCntt;
	}
	public String getItemChrctDivCd() {
		return itemChrctDivCd;
	}
	public void setItemChrctDivCd(String itemChrctDivCd) {
		this.itemChrctDivCd = itemChrctDivCd;
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
	public String getOrordNo() {
		return orordNo;
	}
	public void setOrordNo(String orordNo) {
		this.orordNo = orordNo;
	}
	public String getOrOrdNo() {
		return orOrdNo;
	}
	public void setOrOrdNo(String orOrdNo) {
		this.orOrdNo = orOrdNo;
	}
	public String getOrordItemSeq() {
		return orordItemSeq;
	}
	public void setOrordItemSeq(String orordItemSeq) {
		this.orordItemSeq = orordItemSeq;
	}
	public String getWblRegErrCdNm() {
		return wblRegErrCdNm;
	}
	public void setWblRegErrCdNm(String wblRegErrCdNm) {
		this.wblRegErrCdNm = wblRegErrCdNm;
	}
	public String getWblRegErrCd() {
		return wblRegErrCd;
	}
	public void setWblRegErrCd(String wblRegErrCd) {
		this.wblRegErrCd = wblRegErrCd;
	}
	public String getShppMainCd() {
		return shppMainCd;
	}
	public void setShppMainCd(String shppMainCd) {
		this.shppMainCd = shppMainCd;
	}
	public String getSiteNo() {
		return siteNo;
	}
	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}
	public String getSiteNm() {
		return siteNm;
	}
	public void setSiteNm(String siteNm) {
		this.siteNm = siteNm;
	}
	public String getpCus() {
		return pCus;
	}
	public void setpCus(String pCus) {
		this.pCus = pCus;
	}
	public String getAllnOrdNo() {
		return allnOrdNo;
	}
	public void setAllnOrdNo(String allnOrdNo) {
		this.allnOrdNo = allnOrdNo;
	}
	public String getOldOrdNo() {
		return oldOrdNo;
	}
	public void setOldOrdNo(String oldOrdNo) {
		this.oldOrdNo = oldOrdNo;
	}
	public String getItemDiv() {
		return itemDiv;
	}
	public void setItemDiv(String itemDiv) {
		this.itemDiv = itemDiv;
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
	public String getOrdItemDivNm() {
		return ordItemDivNm;
	}
	public void setOrdItemDivNm(String ordItemDivNm) {
		this.ordItemDivNm = ordItemDivNm;
	}
	public String getAutoShortgYn() {
		return autoShortgYn;
	}
	public void setAutoShortgYn(String autoShortgYn) {
		this.autoShortgYn = autoShortgYn;
	}
	public String getWhoutCritnDt() {
		return whoutCritnDt;
	}
	public void setWhoutCritnDt(String whoutCritnDt) {
		this.whoutCritnDt = whoutCritnDt;
	}
	public String getFrgShpplocCntryCd() {
		return frgShpplocCntryCd;
	}
	public void setFrgShpplocCntryCd(String frgShpplocCntryCd) {
		this.frgShpplocCntryCd = frgShpplocCntryCd;
	}
	public String getFrgShpplocHpno() {
		return frgShpplocHpno;
	}
	public void setFrgShpplocHpno(String frgShpplocHpno) {
		this.frgShpplocHpno = frgShpplocHpno;
	}
	public String getFrgShpplocTelno() {
		return frgShpplocTelno;
	}
	public void setFrgShpplocTelno(String frgShpplocTelno) {
		this.frgShpplocTelno = frgShpplocTelno;
	}
	public String getFrgShpplocZipcd() {
		return frgShpplocZipcd;
	}
	public void setFrgShpplocZipcd(String frgShpplocZipcd) {
		this.frgShpplocZipcd = frgShpplocZipcd;
	}
	public String getFrgShpplocBascAddr() {
		return frgShpplocBascAddr;
	}
	public void setFrgShpplocBascAddr(String frgShpplocBascAddr) {
		this.frgShpplocBascAddr = frgShpplocBascAddr;
	}
	public String getFrgShpplocDtlAddr() {
		return frgShpplocDtlAddr;
	}
	public void setFrgShpplocDtlAddr(String frgShpplocDtlAddr) {
		this.frgShpplocDtlAddr = frgShpplocDtlAddr;
	}
	public String getWhddDivCd() {
		return whddDivCd;
	}
	public void setWhddDivCd(String whddDivCd) {
		this.whddDivCd = whddDivCd;
	}
	public String getWhinDelayNts() {
		return whinDelayNts;
	}
	public void setWhinDelayNts(String whinDelayNts) {
		this.whinDelayNts = whinDelayNts;
	}
	public String getOrdProcDemndStatCd() {
		return ordProcDemndStatCd;
	}
	public void setOrdProcDemndStatCd(String ordProcDemndStatCd) {
		this.ordProcDemndStatCd = ordProcDemndStatCd;
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
	public String getOrdpeRoadAddr() {
		return ordpeRoadAddr;
	}
	public void setOrdpeRoadAddr(String ordpeRoadAddr) {
		this.ordpeRoadAddr = ordpeRoadAddr;
	}
	
	
}
