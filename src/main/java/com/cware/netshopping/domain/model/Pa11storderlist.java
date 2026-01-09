package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class Pa11storderlist extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String ordNo;
	private long ordPrdSeq;
	private String dlvNo;
	private long ordPrdCnSeq;
	private String paOrderGb;
	private String procFlag;
	private String addPrdNo;
	private String addPrdYn;
	private long bndlDlvSeq;
	private String bndlDlvYn;
	private String custGrdNm;
	private long dlvCst;
	private String dlvCstType;
	private long bmDlvCst;
	private String bmDlvCstType;
	private String gblDlvYn;
	private String giftCd;
	private String memId;
	private long memNo;
	private long ordAmt;
	private String ordBaseAddr;
	private String ordDlvReqCont;
	private Timestamp ordDt;
	private String ordDtlsAddr;
	private String ordMailNo;
	private String ordNm;
	private long ordOptWonStl;
	private long ordPayAmt;
	private String ordPrtblTel;
	private long ordQty;
	private Timestamp ordStlEndDt;
	private String ordTlphnNo;
	private Timestamp plcodrCnfDt;
	private String prdNm;
	private long prdNo;
	private long prdStckNo;
	private String rcvrBaseAddr;
	private String rcvrDtlsAddr;
	private String rcvrMailNo;
	private String rcvrMailNoSeq;
	private String rcvrNm;
	private String rcvrPrtblNo;
	private String rcvrTlphn;
	private long selPrc;
	private long sellerDscPrc;
	private String sellerPrdCd;
	private String slctPrdOptNm;
	private long tmallDscPrc;
	private String typeAdd;
	private String typeBilNo;
	private long lstTmallDscPrc;
	private long lstSellerDscPrc;
	private long referSeq;
	private String sellerStockCd;
	private String appmtDdDlvDy;
	private String appmtEltRefuseYn;
	private String appmtselStockCd;
	private Timestamp createDt;
	private String ordCnDtlsRsn;
	private long ordCnQty;
	private String ordCnMnbdCd;
	private String ordCnRsnCd;
	private String ordCnStatCd;
	private String cancelProcNote;
	private String cancelProcId;

	public String getOrdNo() { 
		return this.ordNo;
	}
	public long getOrdPrdSeq() { 
		return this.ordPrdSeq;
	}
	public String getDlvNo() { 
		return this.dlvNo;
	}
	public long getOrdPrdCnSeq() { 
		return this.ordPrdCnSeq;
	}
	public String getPaOrderGb() { 
		return this.paOrderGb;
	}
	public String getAddPrdNo() { 
		return this.addPrdNo;
	}
	public String getAddPrdYn() { 
		return this.addPrdYn;
	}
	public long getBndlDlvSeq() { 
		return this.bndlDlvSeq;
	}
	public String getBndlDlvYn() { 
		return this.bndlDlvYn;
	}
	public String getCustGrdNm() { 
		return this.custGrdNm;
	}
	public long getDlvCst() { 
		return this.dlvCst;
	}
	public String getDlvCstType() { 
		return this.dlvCstType;
	}
	public long getBmDlvCst() { 
		return this.bmDlvCst;
	}
	public String getBmDlvCstType() { 
		return this.bmDlvCstType;
	}
	public String getGblDlvYn() { 
		return this.gblDlvYn;
	}
	public String getGiftCd() { 
		return this.giftCd;
	}
	public String getMemId() { 
		return this.memId;
	}
	public long getMemNo() { 
		return this.memNo;
	}
	public long getOrdAmt() { 
		return this.ordAmt;
	}
	public String getOrdBaseAddr() { 
		return this.ordBaseAddr;
	}
	public String getOrdDlvReqCont() { 
		return this.ordDlvReqCont;
	}
	public Timestamp getOrdDt() { 
		return this.ordDt;
	}
	public String getOrdDtlsAddr() { 
		return this.ordDtlsAddr;
	}
	public String getOrdMailNo() { 
		return this.ordMailNo;
	}
	public String getOrdNm() { 
		return this.ordNm;
	}
	public long getOrdOptWonStl() { 
		return this.ordOptWonStl;
	}
	public long getOrdPayAmt() { 
		return this.ordPayAmt;
	}
	public String getOrdPrtblTel() { 
		return this.ordPrtblTel;
	}
	public long getOrdQty() { 
		return this.ordQty;
	}
	public Timestamp getOrdStlEndDt() { 
		return this.ordStlEndDt;
	}
	public String getOrdTlphnNo() { 
		return this.ordTlphnNo;
	}
	public Timestamp getPlcodrCnfDt() { 
		return this.plcodrCnfDt;
	}
	public String getPrdNm() { 
		return this.prdNm;
	}
	public long getPrdNo() { 
		return this.prdNo;
	}
	public long getPrdStckNo() { 
		return this.prdStckNo;
	}
	public String getRcvrBaseAddr() { 
		return this.rcvrBaseAddr;
	}
	public String getRcvrDtlsAddr() { 
		return this.rcvrDtlsAddr;
	}
	public String getRcvrMailNo() { 
		return this.rcvrMailNo;
	}
	public String getRcvrMailNoSeq() { 
		return this.rcvrMailNoSeq;
	}
	public String getRcvrNm() { 
		return this.rcvrNm;
	}
	public String getRcvrPrtblNo() { 
		return this.rcvrPrtblNo;
	}
	public String getRcvrTlphn() { 
		return this.rcvrTlphn;
	}
	public long getSelPrc() { 
		return this.selPrc;
	}
	public long getSellerDscPrc() { 
		return this.sellerDscPrc;
	}
	public String getSellerPrdCd() { 
		return this.sellerPrdCd;
	}
	public String getSlctPrdOptNm() { 
		return this.slctPrdOptNm;
	}
	public long getTmallDscPrc() { 
		return this.tmallDscPrc;
	}
	public String getTypeAdd() { 
		return this.typeAdd;
	}
	public String getTypeBilNo() { 
		return this.typeBilNo;
	}
	public long getLstTmallDscPrc() { 
		return this.lstTmallDscPrc;
	}
	public long getLstSellerDscPrc() { 
		return this.lstSellerDscPrc;
	}
	public long getReferSeq() { 
		return this.referSeq;
	}
	public String getSellerStockCd() { 
		return this.sellerStockCd;
	}
	public String getAppmtDdDlvDy() { 
		return this.appmtDdDlvDy;
	}
	public String getAppmtEltRefuseYn() { 
		return this.appmtEltRefuseYn;
	}
	public String getAppmtselStockCd() { 
		return this.appmtselStockCd;
	}
	public Timestamp getCreateDt() { 
		return this.createDt;
	}
	public String getOrdCnDtlsRsn() { 
		return this.ordCnDtlsRsn;
	}
	public long getOrdCnQty() { 
		return this.ordCnQty;
	}
	public String getOrdCnMnbdCd() { 
		return this.ordCnMnbdCd;
	}
	public String getOrdCnRsnCd() { 
		return this.ordCnRsnCd;
	}
	public String getOrdCnStatCd() { 
		return this.ordCnStatCd;
	}

	public void setOrdNo(String ordNo) { 
		this.ordNo = ordNo;
	}
	public void setOrdPrdSeq(long ordPrdSeq) { 
		this.ordPrdSeq = ordPrdSeq;
	}
	public void setDlvNo(String dlvNo) { 
		this.dlvNo = dlvNo;
	}
	public void setOrdPrdCnSeq(long ordPrdCnSeq) { 
		this.ordPrdCnSeq = ordPrdCnSeq;
	}
	public void setPaOrderGb(String paOrderGb) { 
		this.paOrderGb = paOrderGb;
	}
	public void setAddPrdNo(String addPrdNo) { 
		this.addPrdNo = addPrdNo;
	}
	public void setAddPrdYn(String addPrdYn) { 
		this.addPrdYn = addPrdYn;
	}
	public void setBndlDlvSeq(long bndlDlvSeq) { 
		this.bndlDlvSeq = bndlDlvSeq;
	}
	public void setBndlDlvYn(String bndlDlvYn) { 
		this.bndlDlvYn = bndlDlvYn;
	}
	public void setCustGrdNm(String custGrdNm) { 
		this.custGrdNm = custGrdNm;
	}
	public void setDlvCst(long dlvCst) { 
		this.dlvCst = dlvCst;
	}
	public void setDlvCstType(String dlvCstType) { 
		this.dlvCstType = dlvCstType;
	}
	public void setBmDlvCst(long bmDlvCst) { 
		this.bmDlvCst = bmDlvCst;
	}
	public void setBmDlvCstType(String bmDlvCstType) { 
		this.bmDlvCstType = bmDlvCstType;
	}
	public void setGblDlvYn(String gblDlvYn) { 
		this.gblDlvYn = gblDlvYn;
	}
	public void setGiftCd(String giftCd) { 
		this.giftCd = giftCd;
	}
	public void setMemId(String memId) { 
		this.memId = memId;
	}
	public void setMemNo(long memNo) { 
		this.memNo = memNo;
	}
	public void setOrdAmt(long ordAmt) { 
		this.ordAmt = ordAmt;
	}
	public void setOrdBaseAddr(String ordBaseAddr) { 
		this.ordBaseAddr = ordBaseAddr;
	}
	public void setOrdDlvReqCont(String ordDlvReqCont) { 
		this.ordDlvReqCont = ordDlvReqCont;
	}
	public void setOrdDt(Timestamp ordDt) { 
		this.ordDt = ordDt;
	}
	public void setOrdDtlsAddr(String ordDtlsAddr) { 
		this.ordDtlsAddr = ordDtlsAddr;
	}
	public void setOrdMailNo(String ordMailNo) { 
		this.ordMailNo = ordMailNo;
	}
	public void setOrdNm(String ordNm) { 
		this.ordNm = ordNm;
	}
	public void setOrdOptWonStl(long ordOptWonStl) { 
		this.ordOptWonStl = ordOptWonStl;
	}
	public void setOrdPayAmt(long ordPayAmt) { 
		this.ordPayAmt = ordPayAmt;
	}
	public void setOrdPrtblTel(String ordPrtblTel) { 
		this.ordPrtblTel = ordPrtblTel;
	}
	public void setOrdQty(long ordQty) { 
		this.ordQty = ordQty;
	}
	public void setOrdStlEndDt(Timestamp ordStlEndDt) { 
		this.ordStlEndDt = ordStlEndDt;
	}
	public void setOrdTlphnNo(String ordTlphnNo) { 
		this.ordTlphnNo = ordTlphnNo;
	}
	public void setPlcodrCnfDt(Timestamp plcodrCnfDt) { 
		this.plcodrCnfDt = plcodrCnfDt;
	}
	public void setPrdNm(String prdNm) { 
		this.prdNm = prdNm;
	}
	public void setPrdNo(long prdNo) { 
		this.prdNo = prdNo;
	}
	public void setPrdStckNo(long prdStckNo) { 
		this.prdStckNo = prdStckNo;
	}
	public void setRcvrBaseAddr(String rcvrBaseAddr) { 
		this.rcvrBaseAddr = rcvrBaseAddr;
	}
	public void setRcvrDtlsAddr(String rcvrDtlsAddr) { 
		this.rcvrDtlsAddr = rcvrDtlsAddr;
	}
	public void setRcvrMailNo(String rcvrMailNo) { 
		this.rcvrMailNo = rcvrMailNo;
	}
	public void setRcvrMailNoSeq(String rcvrMailNoSeq) { 
		this.rcvrMailNoSeq = rcvrMailNoSeq;
	}
	public void setRcvrNm(String rcvrNm) { 
		this.rcvrNm = rcvrNm;
	}
	public void setRcvrPrtblNo(String rcvrPrtblNo) { 
		this.rcvrPrtblNo = rcvrPrtblNo;
	}
	public void setRcvrTlphn(String rcvrTlphn) { 
		this.rcvrTlphn = rcvrTlphn;
	}
	public void setSelPrc(long selPrc) { 
		this.selPrc = selPrc;
	}
	public void setSellerDscPrc(long sellerDscPrc) { 
		this.sellerDscPrc = sellerDscPrc;
	}
	public void setSellerPrdCd(String sellerPrdCd) { 
		this.sellerPrdCd = sellerPrdCd;
	}
	public void setSlctPrdOptNm(String slctPrdOptNm) { 
		this.slctPrdOptNm = slctPrdOptNm;
	}
	public void setTmallDscPrc(long tmallDscPrc) { 
		this.tmallDscPrc = tmallDscPrc;
	}
	public void setTypeAdd(String typeAdd) { 
		this.typeAdd = typeAdd;
	}
	public void setTypeBilNo(String typeBilNo) { 
		this.typeBilNo = typeBilNo;
	}
	public void setLstTmallDscPrc(long lstTmallDscPrc) { 
		this.lstTmallDscPrc = lstTmallDscPrc;
	}
	public void setLstSellerDscPrc(long lstSellerDscPrc) { 
		this.lstSellerDscPrc = lstSellerDscPrc;
	}
	public void setReferSeq(long referSeq) { 
		this.referSeq = referSeq;
	}
	public void setSellerStockCd(String sellerStockCd) { 
		this.sellerStockCd = sellerStockCd;
	}
	public void setAppmtDdDlvDy(String appmtDdDlvDy) { 
		this.appmtDdDlvDy = appmtDdDlvDy;
	}
	public void setAppmtEltRefuseYn(String appmtEltRefuseYn) { 
		this.appmtEltRefuseYn = appmtEltRefuseYn;
	}
	public void setAppmtselStockCd(String appmtselStockCd) { 
		this.appmtselStockCd = appmtselStockCd;
	}
	public void setCreateDt(Timestamp createDt) { 
		this.createDt = createDt;
	}
	public void setOrdCnDtlsRsn(String ordCnDtlsRsn) { 
		this.ordCnDtlsRsn = ordCnDtlsRsn;
	}
	public void setOrdCnQty(long ordCnQty) { 
		this.ordCnQty = ordCnQty;
	}
	public void setOrdCnMnbdCd(String ordCnMnbdCd) { 
		this.ordCnMnbdCd = ordCnMnbdCd;
	}
	public void setOrdCnRsnCd(String ordCnRsnCd) { 
		this.ordCnRsnCd = ordCnRsnCd;
	}
	public void setOrdCnStatCd(String ordCnStatCd) { 
		this.ordCnStatCd = ordCnStatCd;
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
	
}
