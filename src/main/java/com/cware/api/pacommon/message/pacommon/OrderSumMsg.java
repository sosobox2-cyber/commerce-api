package com.cware.api.pacommon.message.pacommon;

import com.cware.framework.core.basic.AbstractMessage;

public class OrderSumMsg extends AbstractMessage {
	
	static final long serialVersionUID = -6387052718204789793L;
	
	long salePriceTotal;
	long arsDcAmtTotal;
	long dcAmtTotal;
	long couponAmt;
	long showCardDcAmt;
	long saveAmt;
	long useSpointAmt;
	long totalAmt;
	long shpfeeAmt;
	String mediaCode;
	String amtCompare = "0";
	
	
	public long getShpfeeAmt() {
		return shpfeeAmt;
	}
	public void setShpfeeAmt(long shpfeeAmt) {
		this.shpfeeAmt = shpfeeAmt;
	}
	public String getMediaCode() {
		return mediaCode;
	}
	public void setMediaCode(String mediaCode) {
		this.mediaCode = mediaCode;
	}
	public long getSalePriceTotal() {
		return salePriceTotal;
	}
	public void setSalePriceTotal(long salePriceTotal) {
		this.salePriceTotal = salePriceTotal;
	}
	public double getArsDcAmtTotal() {
		return arsDcAmtTotal;
	}
	public void setArsDcAmtTotal(long arsDcAmtTotal) {
		this.arsDcAmtTotal = arsDcAmtTotal;
	}
	public long getDcAmtTotal() {
		return dcAmtTotal;
	}
	public void setDcAmtTotal(long dcAmtTotal) {
		this.dcAmtTotal = dcAmtTotal;
	}
	public long getCouponAmt() {
		return couponAmt;
	}
	public void setCouponAmt(long couponAmt) {
		this.couponAmt = couponAmt;
	}
	public long getShowCardDcAmt() {
		return showCardDcAmt;
	}
	public void setShowCardDcAmt(long showCardDcAmt) {
		this.showCardDcAmt = showCardDcAmt;
	}
	public long getSaveAmt() {
		return saveAmt;
	}
	public void setSaveAmt(long saveAmt) {
		this.saveAmt = saveAmt;
	}
	public long getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(long totalAmt) {
		this.totalAmt = totalAmt;
	}
	public long getUseSpointAmt() {
		return useSpointAmt;
	}
	public void setUseSpointAmt(long useSpointAmt) {
		this.useSpointAmt = useSpointAmt;
	}
	public String getAmtCompare() {
		return amtCompare;
	}
	public void setAmtCompare(String amtCompare) {
		this.amtCompare = amtCompare;
	}

}
