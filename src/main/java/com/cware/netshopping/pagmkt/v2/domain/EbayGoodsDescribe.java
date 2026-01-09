package com.cware.netshopping.pagmkt.v2.domain;

public class EbayGoodsDescribe {

	private String topImage;
	private String bottomImage;
	private String describeExt;
	private String collectImage;
	private String noticeExt;
	private String goodsCom;

	public String getTopImage() {
		return topImage;
	}

	public void setTopImage(String topImage) {
		this.topImage = topImage;
	}

	public String getBottomImage() {
		return bottomImage;
	}

	public void setBottomImage(String bottomImage) {
		this.bottomImage = bottomImage;
	}

	public String getDescribeExt() {
		return describeExt;
	}

	public void setDescribeExt(String describeExt) {
		this.describeExt = describeExt;
	}

	public String getCollectImage() {
		return collectImage;
	}

	public void setCollectImage(String collectImage) {
		this.collectImage = collectImage;
	}

	public String getNoticeExt() {
		return noticeExt;
	}

	public void setNoticeExt(String noticeExt) {
		this.noticeExt = noticeExt;
	}

	public String getGoodsCom() {
		return goodsCom;
	}

	public void setGoodsCom(String goodsCom) {
		this.goodsCom = goodsCom;
	}

	@Override
	public String toString() {
		return "EbayGoodsDescribe [topImage=" + topImage + ", bottomImage=" + bottomImage + ", describeExt="
				+ describeExt + ", collectImage=" + collectImage + ", noticeExt=" + noticeExt + ", goodsCom=" + goodsCom
				+ "]";
	}

}
