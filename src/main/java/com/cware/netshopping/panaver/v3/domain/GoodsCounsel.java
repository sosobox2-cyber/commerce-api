package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GoodsCounsel {

	// 생성 일시
	private String createDate;
	// 문의 내용
	private String question;
	// 판매자 답변 내용(여러 개면 최초 답변을 반환)
	private String answer;
	// 판매자 답변 여부
	private String answered;
	// 채널 상품번호
	private long productId;
	// 상품명
	private String productName;
	// 마스킹된 작성자 ID
	private String maskedWriterId;
	// 상품 문의 번호
	private long questionId;
	
	
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAnswered() {
		return answered;
	}
	public void setAnswered(String answered) {
		this.answered = answered;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getMaskedWriterId() {
		return maskedWriterId;
	}
	public void setMaskedWriterId(String maskedWriterId) {
		this.maskedWriterId = maskedWriterId;
	}
	public long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}
	
	@Override
	public String toString() {
		return "GoodsCounsel [createDate=" + createDate + ", question=" + question + ", answer=" + answer
				+ ", answered=" + answered + ", productId=" + productId + ", productName=" + productName
				+ ", maskedWriterId=" + maskedWriterId + ", questionId=" + questionId + "]";
	}

}
