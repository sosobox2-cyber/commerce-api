package com.cware.netshopping.pawemp.counsel.model;

/**
 * 상품 QnA 답변 등록 
 *
 */
public class SetAnswerRequest {
	
	private long qnaSeq;
	private String answer;
	
	public long getQnaSeq() {
		return qnaSeq;
	}
	public void setQnaSeq(long qnaSeq) {
		this.qnaSeq = qnaSeq;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
