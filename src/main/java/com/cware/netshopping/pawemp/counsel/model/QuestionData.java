package com.cware.netshopping.pawemp.counsel.model;

/**
 * 상품 Q&A 조회 아이템 
 */
public class QuestionData {
	
	private long qnaSeq;
	private String question;
	private String questionDate;
	private long productNo;
	private String productName;
	private String orderNo;
	private String privateYn;
	
	public long getQnaSeq() {
		return qnaSeq;
	}
	public void setQnaSeq(long qnaSeq) {
		this.qnaSeq = qnaSeq;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getQuestionDate() {
		return questionDate;
	}
	public void setQuestionDate(String questionDate) {
		this.questionDate = questionDate;
	}
	public long getProductNo() {
		return productNo;
	}
	public void setProductNo(long productNo) {
		this.productNo = productNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPrivateYn() {
		return privateYn;
	}
	public void setPrivateYn(String privateYn) {
		this.privateYn = privateYn;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
}
