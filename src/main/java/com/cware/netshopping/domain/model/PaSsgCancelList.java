package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaSsgCancelList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	private String ordNo;
	private String ordItemSeq;
	private String orordNo; 
    private String orordItemSeq;
	private String clmDemndDts;
	private String itemId;
	private String uitemId;
	private String itemNm;
	private String uitemNm;
	private String exchTgtUitemId;
	private long procOrdQty;
	private String shppcstBdnMainCd;
	private String clmRsnCd;
	private String clmRsnCntt;
	private String withdrawYn;
	private Timestamp withdrawDate;
	private String procFlag;
	private String cancelProcNote;
	private String cancelProcId;
	private String procNote;
	
    private String ordpeNm;
    private String ordRcpMediaCd;
    private String ordRcpMediaNm;
    private String ordItemStatCd;
    private String ordItemStatNm;
    private String clmRsnNm;
    private String imptDivCd;
    private String ordCnclDts;
    private long   cnclItemQty;
    private String ordRcpDts;
    private long   dircItemQty;
    
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
	public String getClmDemndDts() {
		return clmDemndDts;
	}
	public void setClmDemndDts(String clmDemndDts) {
		this.clmDemndDts = clmDemndDts;
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
	public String getItemNm() {
		return itemNm;
	}
	public void setItemNm(String itemNm) {
		this.itemNm = itemNm;
	}
	public String getUitemNm() {
		return uitemNm;
	}
	public void setUitemNm(String uitemNm) {
		this.uitemNm = uitemNm;
	}
	public String getExchTgtUitemId() {
		return exchTgtUitemId;
	}
	public void setExchTgtUitemId(String exchTgtUitemId) {
		this.exchTgtUitemId = exchTgtUitemId;
	}
	public long getProcOrdQty() {
		return procOrdQty;
	}
	public void setProcOrdQty(long procOrdQty) {
		this.procOrdQty = procOrdQty;
	}
	public String getShppcstBdnMainCd() {
		return shppcstBdnMainCd;
	}
	public void setShppcstBdnMainCd(String shppcstBdnMainCd) {
		this.shppcstBdnMainCd = shppcstBdnMainCd;
	}
	public String getClmRsnCd() {
		return clmRsnCd;
	}
	public void setClmRsnCd(String clmRsnCd) {
		this.clmRsnCd = clmRsnCd;
	}
	public String getClmRsnCntt() {
		return clmRsnCntt;
	}
	public void setClmRsnCntt(String clmRsnCntt) {
		this.clmRsnCntt = clmRsnCntt;
	}
	public String getWithdrawYn() {
		return withdrawYn;
	}
	public void setWithdrawYn(String withdrawYn) {
		this.withdrawYn = withdrawYn;
	}
	public Timestamp getWithdrawDate() {
		return withdrawDate;
	}
	public void setWithdrawDate(Timestamp withdrawDate) {
		this.withdrawDate = withdrawDate;
	}
	public String getProcFlag() {
		return procFlag;
	}
	public void setProcFlag(String procFlag) {
		this.procFlag = procFlag;
	}
	public String getCancelProcNote() {
		return cancelProcNote;
	}
	public void setCancelProcNote(String cancelProcNote) {
		this.cancelProcNote = cancelProcNote;
	}
	public String getCancelProcId() {
		return cancelProcId;
	}
	public void setCancelProcId(String cancelProcId) {
		this.cancelProcId = cancelProcId;
	}
	public String getProcNote() {
		return procNote;
	}
	public void setProcNote(String procNote) {
		this.procNote = procNote;
	}
	public String getOrdpeNm() {
		return ordpeNm;
	}
	public void setOrdpeNm(String ordpeNm) {
		this.ordpeNm = ordpeNm;
	}
	public String getOrdRcpMediaCd() {
		return ordRcpMediaCd;
	}
	public void setOrdRcpMediaCd(String ordRcpMediaCd) {
		this.ordRcpMediaCd = ordRcpMediaCd;
	}
	public String getOrdRcpMediaNm() {
		return ordRcpMediaNm;
	}
	public void setOrdRcpMediaNm(String ordRcpMediaNm) {
		this.ordRcpMediaNm = ordRcpMediaNm;
	}
	public String getOrdItemStatCd() {
		return ordItemStatCd;
	}
	public void setOrdItemStatCd(String ordItemStatCd) {
		this.ordItemStatCd = ordItemStatCd;
	}
	public String getOrdItemStatNm() {
		return ordItemStatNm;
	}
	public void setOrdItemStatNm(String ordItemStatNm) {
		this.ordItemStatNm = ordItemStatNm;
	}
	public String getClmRsnNm() {
		return clmRsnNm;
	}
	public void setClmRsnNm(String clmRsnNm) {
		this.clmRsnNm = clmRsnNm;
	}
	public String getImptDivCd() {
		return imptDivCd;
	}
	public void setImptDivCd(String imptDivCd) {
		this.imptDivCd = imptDivCd;
	}
	public String getOrdCnclDts() {
		return ordCnclDts;
	}
	public void setOrdCnclDts(String ordCnclDts) {
		this.ordCnclDts = ordCnclDts;
	}
	public long getCnclItemQty() {
		return cnclItemQty;
	}
	public void setCnclItemQty(long cnclItemQty) {
		this.cnclItemQty = cnclItemQty;
	}
	public String getOrdRcpDts() {
		return ordRcpDts;
	}
	public void setOrdRcpDts(String ordRcpDts) {
		this.ordRcpDts = ordRcpDts;
	}
	public long getDircItemQty() {
		return dircItemQty;
	}
	public void setDircItemQty(long dircItemQty) {
		this.dircItemQty = dircItemQty;
	}
    
}
