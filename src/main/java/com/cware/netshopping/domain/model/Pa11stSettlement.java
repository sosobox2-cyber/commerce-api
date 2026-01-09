package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Pa11stSettlement extends AbstractModel {
	private static final long serialVersionUID = 1L; 
	
	private String	gatherDate;
	private String	ordNo;
	private String	ordPrdSeq;
	private long	ordQty;
	
	private long   	abrdCnDlvCst;
	private long	addClmDlvCst;
	private long	bmClmFstDlvCst;
	private long	bmStlDlvCst;
	private long	clmDlvCst;
	private long	clmFstDlvCst;
	private long	cupnAmt;
	private long	debaGtnStlAmt;
	
	private long	deductAmt;
	private long	dlvAmt;
	private long	dlvNo;
	private String	feeTypeNm;
	private String	memId;
	private String	memNm;
	private long	optAmt;
	
	private String	ordStlEndDt;
	private String	pocnfrmDt;
	private String	prdNm;
	private long	prdNo;
	private long	selFee;
	private String	selFixedFee;
	
	private long	selPrc;
	private long	selPrcAmt;
	private long	sellerCupnAmt;
	private long	sellerDfrmAppDlvAmt;
	private long	sellerDfrmChpCstPrd;
	private long	sellerDfrmDeferredAdFee;
	private long	sellerDfrmIntfreeFee;
	private long	sellerDfrmMultiDscCst;
	private long	sellerDfrmOcbAmt;
	private long	sellerDfrmPntPrd;
	
	private String	sellerPrdNo;
	private long	seqNo;
	private String	slctPrdOptNm;
	private String	sndEndDt;
	private long	stlAmt;
	private String	stlPlnDy;
	private long	tmallApplyDscAmt;
	private long	tmallOverDscAmt;
	private long	totalCount;
	
	
	public String getGatherDate() {
		return gatherDate;
	}
	public void setGatherDate(String gatherDate) {
		this.gatherDate = gatherDate;
	}
	public String getOrdNo() {
		return ordNo;
	}
	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}
	public String getOrdPrdSeq() {
		return ordPrdSeq;
	}
	public void setOrdPrdSeq(String ordPrdSeq) {
		this.ordPrdSeq = ordPrdSeq;
	}
	public long getOrdQty() {
		return ordQty;
	}
	public void setOrdQty(long ordQty) {
		this.ordQty = ordQty;
	}
	public long getAbrdCnDlvCst() {
		return abrdCnDlvCst;
	}
	public void setAbrdCnDlvCst(long abrdCnDlvCst) {
		this.abrdCnDlvCst = abrdCnDlvCst;
	}
	public long getAddClmDlvCst() {
		return addClmDlvCst;
	}
	public void setAddClmDlvCst(long addClmDlvCst) {
		this.addClmDlvCst = addClmDlvCst;
	}
	public long getBmClmFstDlvCst() {
		return bmClmFstDlvCst;
	}
	public void setBmClmFstDlvCst(long bmClmFstDlvCst) {
		this.bmClmFstDlvCst = bmClmFstDlvCst;
	}
	public long getBmStlDlvCst() {
		return bmStlDlvCst;
	}
	public void setBmStlDlvCst(long bmStlDlvCst) {
		this.bmStlDlvCst = bmStlDlvCst;
	}
	public long getClmDlvCst() {
		return clmDlvCst;
	}
	public void setClmDlvCst(long clmDlvCst) {
		this.clmDlvCst = clmDlvCst;
	}
	public long getClmFstDlvCst() {
		return clmFstDlvCst;
	}
	public void setClmFstDlvCst(long clmFstDlvCst) {
		this.clmFstDlvCst = clmFstDlvCst;
	}
	public long getCupnAmt() {
		return cupnAmt;
	}
	public void setCupnAmt(long cupnAmt) {
		this.cupnAmt = cupnAmt;
	}
	public long getDebaGtnStlAmt() {
		return debaGtnStlAmt;
	}
	public void setDebaGtnStlAmt(long debaGtnStlAmt) {
		this.debaGtnStlAmt = debaGtnStlAmt;
	}
	public long getDeductAmt() {
		return deductAmt;
	}
	public void setDeductAmt(long deductAmt) {
		this.deductAmt = deductAmt;
	}
	public long getDlvAmt() {
		return dlvAmt;
	}
	public void setDlvAmt(long dlvAmt) {
		this.dlvAmt = dlvAmt;
	}
	public long getDlvNo() {
		return dlvNo;
	}
	public void setDlvNo(long dlvNo) {
		this.dlvNo = dlvNo;
	}
	public String getFeeTypeNm() {
		return feeTypeNm;
	}
	public void setFeeTypeNm(String feeTypeNm) {
		this.feeTypeNm = feeTypeNm;
	}
	public String getMemId() {
		return memId;
	}
	public void setMemId(String memId) {
		this.memId = memId;
	}
	public String getMemNm() {
		return memNm;
	}
	public void setMemNm(String memNm) {
		this.memNm = memNm;
	}
	public long getOptAmt() {
		return optAmt;
	}
	public void setOptAmt(long optAmt) {
		this.optAmt = optAmt;
	}
	public String getOrdStlEndDt() {
		return ordStlEndDt;
	}
	public void setOrdStlEndDt(String ordStlEndDt) {
		this.ordStlEndDt = ordStlEndDt;
	}
	public String getPocnfrmDt() {
		return pocnfrmDt;
	}
	public void setPocnfrmDt(String pocnfrmDt) {
		this.pocnfrmDt = pocnfrmDt;
	}
	public String getPrdNm() {
		return prdNm;
	}
	public void setPrdNm(String prdNm) {
		this.prdNm = prdNm;
	}
	public long getPrdNo() {
		return prdNo;
	}
	public void setPrdNo(long prdNo) {
		this.prdNo = prdNo;
	}
	public long getSelFee() {
		return selFee;
	}
	public void setSelFee(long selFee) {
		this.selFee = selFee;
	}
	public String getSelFixedFee() {
		return selFixedFee;
	}
	public void setSelFixedFee(String selFixedFee) {
		this.selFixedFee = selFixedFee;
	}
	public long getSelPrc() {
		return selPrc;
	}
	public void setSelPrc(long selPrc) {
		this.selPrc = selPrc;
	}
	public long getSelPrcAmt() {
		return selPrcAmt;
	}
	public void setSelPrcAmt(long selPrcAmt) {
		this.selPrcAmt = selPrcAmt;
	}
	public long getSellerCupnAmt() {
		return sellerCupnAmt;
	}
	public void setSellerCupnAmt(long sellerCupnAmt) {
		this.sellerCupnAmt = sellerCupnAmt;
	}
	public long getSellerDfrmAppDlvAmt() {
		return sellerDfrmAppDlvAmt;
	}
	public void setSellerDfrmAppDlvAmt(long sellerDfrmAppDlvAmt) {
		this.sellerDfrmAppDlvAmt = sellerDfrmAppDlvAmt;
	}
	public long getSellerDfrmChpCstPrd() {
		return sellerDfrmChpCstPrd;
	}
	public void setSellerDfrmChpCstPrd(long sellerDfrmChpCstPrd) {
		this.sellerDfrmChpCstPrd = sellerDfrmChpCstPrd;
	}
	public long getSellerDfrmDeferredAdFee() {
		return sellerDfrmDeferredAdFee;
	}
	public void setSellerDfrmDeferredAdFee(long sellerDfrmDeferredAdFee) {
		this.sellerDfrmDeferredAdFee = sellerDfrmDeferredAdFee;
	}
	public long getSellerDfrmIntfreeFee() {
		return sellerDfrmIntfreeFee;
	}
	public void setSellerDfrmIntfreeFee(long sellerDfrmIntfreeFee) {
		this.sellerDfrmIntfreeFee = sellerDfrmIntfreeFee;
	}
	public long getSellerDfrmMultiDscCst() {
		return sellerDfrmMultiDscCst;
	}
	public void setSellerDfrmMultiDscCst(long sellerDfrmMultiDscCst) {
		this.sellerDfrmMultiDscCst = sellerDfrmMultiDscCst;
	}
	public long getSellerDfrmOcbAmt() {
		return sellerDfrmOcbAmt;
	}
	public void setSellerDfrmOcbAmt(long sellerDfrmOcbAmt) {
		this.sellerDfrmOcbAmt = sellerDfrmOcbAmt;
	}
	public long getSellerDfrmPntPrd() {
		return sellerDfrmPntPrd;
	}
	public void setSellerDfrmPntPrd(long sellerDfrmPntPrd) {
		this.sellerDfrmPntPrd = sellerDfrmPntPrd;
	}
	public String getSellerPrdNo() {
		return sellerPrdNo;
	}
	public void setSellerPrdNo(String sellerPrdNo) {
		this.sellerPrdNo = sellerPrdNo;
	}
	public long getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(long seqNo) {
		this.seqNo = seqNo;
	}
	public String getSlctPrdOptNm() {
		return slctPrdOptNm;
	}
	public void setSlctPrdOptNm(String slctPrdOptNm) {
		this.slctPrdOptNm = slctPrdOptNm;
	}
	public String getSndEndDt() {
		return sndEndDt;
	}
	public void setSndEndDt(String sndEndDt) {
		this.sndEndDt = sndEndDt;
	}
	public long getStlAmt() {
		return stlAmt;
	}
	public void setStlAmt(long stlAmt) {
		this.stlAmt = stlAmt;
	}
	public String getStlPlnDy() {
		return stlPlnDy;
	}
	public void setStlPlnDy(String stlPlnDy) {
		this.stlPlnDy = stlPlnDy;
	}
	public long getTmallApplyDscAmt() {
		return tmallApplyDscAmt;
	}
	public void setTmallApplyDscAmt(long tmallApplyDscAmt) {
		this.tmallApplyDscAmt = tmallApplyDscAmt;
	}
	public long getTmallOverDscAmt() {
		return tmallOverDscAmt;
	}
	public void setTmallOverDscAmt(long tmallOverDscAmt) {
		this.tmallOverDscAmt = tmallOverDscAmt;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	

	
}