
package com.cware.netshopping.domain.model;

public class PaGoodsdtMapping extends PaGoodsdt {
	
	private static final long serialVersionUID = 1L;
	private String paCode;
	private String paOptionCode;
	private String transOrderAbleQty;
	private String remark1;
	private String remark2;
	private String goodsdtSeq;
	private String transStockYn;
	private String imagePath;
	private String imageFile;
	
	private String paGoodsCode; // 제휴사 상품코드
	
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getImageFile() {
		return imageFile;
	}
	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}
	public String getGoodsdtSeq() {
		return goodsdtSeq;
	}
	public void setGoodsdtSeq(String goodsdtSeq) {
		this.goodsdtSeq = goodsdtSeq;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaOptionCode() {
		return paOptionCode;
	}
	public void setPaOptionCode(String paOptionCode) {
		this.paOptionCode = paOptionCode;
	}
	public String getTransOrderAbleQty() {
		return transOrderAbleQty;
	}
	public void setTransOrderAbleQty(String transOrderAbleQty) {
		this.transOrderAbleQty = transOrderAbleQty;
	}
	public String getTransStockYn() {
		return transStockYn;
	}
	public void setTransStockYn(String transStockYn) {
		this.transStockYn = transStockYn;
	}
	public String getPaGoodsCode() {
		return paGoodsCode;
	}
	public void setPaGoodsCode(String paGoodsCode) {
		this.paGoodsCode = paGoodsCode;
	}
	
}