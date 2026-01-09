package com.cware.netshopping.domain.model;

public class PaQeenGoodsdtMapping extends PaGoodsdtMapping {
	
	private static final long serialVersionUID = 1L;
	
	private String goodsCode;
	private String goodsdtCode;
	private String goodsdtSeq;
	private String productItemProposalId;
	private String goodsdtInfo;
	private String useYn;
	private String transSaleYn;
	
	public String getGoodsdtSeq() {
		return goodsdtSeq;
	}
	public void setGoodsdtSeq(String goodsdtSeq) {
		this.goodsdtSeq = goodsdtSeq;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsdtCode() {
		return goodsdtCode;
	}
	public void setGoodsdtCode(String goodsdtCode) {
		this.goodsdtCode = goodsdtCode;
	}
	public String getProductItemProposalId() {
		return productItemProposalId;
	}
	public void setProductItemProposalId(String productItemProposalId) {
		this.productItemProposalId = productItemProposalId;
	}
	public String getGoodsdtInfo() {
		return goodsdtInfo;
	}
	public void setGoodsdtInfo(String goodsdtInfo) {
		this.goodsdtInfo = goodsdtInfo;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getTransSaleYn() {
		return transSaleYn;
	}
	public void setTransSaleYn(String transSaleYn) {
		this.transSaleYn = transSaleYn;
	}
	
	
	
	
}