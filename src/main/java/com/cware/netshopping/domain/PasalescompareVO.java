package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Pasalescompare;

public class PasalescompareVO extends Pasalescompare {
	private static final long serialVersionUID = 1L;

	// 11번가
	private String selFixedFee;
	private double selPrc;
	private double stlAmt;
	private double sellerCupnAmt;
	private double tmallApplyDscAmt;
	private double tmallOverDscAmt;
	private double abrdCnDlvCst;
	private double addClmDlvCst;
	private double bmClmFstDlvCst;
	private double bmStlDlvCst;
	private double clmDlvCst;
	private double clmFstDlvCst;
	private double selPrcAmt;
	private double dlvAmt;
	private String procDate;
	// 위메프 정산 수량 - TPAORDERM 수량 비교값(0: 수량 일치, 그외: 수량 불일치)
	private int paWempDiffQty;

	public String getSelFixedFee() {
		return selFixedFee;
	}

	public void setSelFixedFee(String selFixedFee) {
		this.selFixedFee = selFixedFee;
	}

	public double getSelPrc() {
		return selPrc;
	}

	public void setSelPrc(double selPrc) {
		this.selPrc = selPrc;
	}

	public double getStlAmt() {
		return stlAmt;
	}

	public void setStlAmt(double stlAmt) {
		this.stlAmt = stlAmt;
	}

	public double getSellerCupnAmt() {
		return sellerCupnAmt;
	}

	public void setSellerCupnAmt(double sellerCupnAmt) {
		this.sellerCupnAmt = sellerCupnAmt;
	}

	public double getTmallApplyDscAmt() {
		return tmallApplyDscAmt;
	}

	public void setTmallApplyDscAmt(double tmallApplyDscAmt) {
		this.tmallApplyDscAmt = tmallApplyDscAmt;
	}

	public double getTmallOverDscAmt() {
		return tmallOverDscAmt;
	}

	public void setTmallOverDscAmt(double tmallOverDscAmt) {
		this.tmallOverDscAmt = tmallOverDscAmt;
	}

	public double getAbrdCnDlvCst() {
		return abrdCnDlvCst;
	}

	public void setAbrdCnDlvCst(double abrdCnDlvCst) {
		this.abrdCnDlvCst = abrdCnDlvCst;
	}

	public double getAddClmDlvCst() {
		return addClmDlvCst;
	}

	public void setAddClmDlvCst(double addClmDlvCst) {
		this.addClmDlvCst = addClmDlvCst;
	}

	public double getBmClmFstDlvCst() {
		return bmClmFstDlvCst;
	}

	public void setBmClmFstDlvCst(double bmClmFstDlvCst) {
		this.bmClmFstDlvCst = bmClmFstDlvCst;
	}

	public double getBmStlDlvCst() {
		return bmStlDlvCst;
	}

	public void setBmStlDlvCst(double bmStlDlvCst) {
		this.bmStlDlvCst = bmStlDlvCst;
	}

	public double getClmDlvCst() {
		return clmDlvCst;
	}

	public void setClmDlvCst(double clmDlvCst) {
		this.clmDlvCst = clmDlvCst;
	}

	public double getClmFstDlvCst() {
		return clmFstDlvCst;
	}

	public void setClmFstDlvCst(double clmFstDlvCst) {
		this.clmFstDlvCst = clmFstDlvCst;
	}

	public double getSelPrcAmt() {
		return selPrcAmt;
	}

	public void setSelPrcAmt(double selPrcAmt) {
		this.selPrcAmt = selPrcAmt;
	}

	public double getDlvAmt() {
		return dlvAmt;
	}

	public void setDlvAmt(double dlvAmt) {
		this.dlvAmt = dlvAmt;
	}

	public String getProcDate() {
		return procDate;
	}

	public void setProcDate(String procDate) {
		this.procDate = procDate;
	}

	public int getPaWempDiffQty() {
		return paWempDiffQty;
	}

	public void setPaWempDiffQty(int paWempDiffQty) {
		this.paWempDiffQty = paWempDiffQty;
	}
}