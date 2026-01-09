package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CustCounsel {

	// 문의 번호
	private long inquiryNo;
	// 문의 유형. 상품, 배송, 반품, 교환, 환불, 기타가 존재합니다.
	private String category;
	// 문의 제목
	private String title;
	// 문의 내용
	private String inquiryContent;
	// 문의 등록 일시(yyyy-MM-dd'T'HH:mm:ss.SSSXXX)
	private String inquiryRegistrationDateTime;
	// 최근 문의 답변 ID
	private long answerContentId;
	// 최근 문의 답변 내용
	private String answerContent;
	// 최근 문의 답변 템플릿 번호
	private long answerTemplateNo;
	// 최근 문의 답변 등록 일시(yyyy-MM-dd'T'HH:mm:ss.SSSXXX)
	private String answerRegistrationDateTime;
	// 문의 답변 여부
	private boolean answered;
	// 주문 ID
	private String orderId;
	// 상품번호
	private String productNo;
	// 상품 주문 ID 목록(여러 개의 상품 주문에 대해 문의했을 경우 각각의 상품 주문 ID가 ','로 구분되어 출력됨)
	private String productOrderIdList;
	// 상품명
	private String productName;
	// 상품 주문 옵션
	private String productOrderOption;
	// 구매자 ID
	private String customerId;
	// 구매자 이름
	private String customerName;
	
	
	public long getInquiryNo() {
		return inquiryNo;
	}
	public void setInquiryNo(long inquiryNo) {
		this.inquiryNo = inquiryNo;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInquiryContent() {
		return inquiryContent;
	}
	public void setInquiryContent(String inquiryContent) {
		this.inquiryContent = inquiryContent;
	}
	public String getInquiryRegistrationDateTime() {
		return inquiryRegistrationDateTime;
	}
	public void setInquiryRegistrationDateTime(String inquiryRegistrationDateTime) {
		this.inquiryRegistrationDateTime = inquiryRegistrationDateTime;
	}
	public long getAnswerContentId() {
		return answerContentId;
	}
	public void setAnswerContentId(long answerContentId) {
		this.answerContentId = answerContentId;
	}
	public String getAnswerContent() {
		return answerContent;
	}
	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}
	public long getAnswerTemplateNo() {
		return answerTemplateNo;
	}
	public void setAnswerTemplateNo(long answerTemplateNo) {
		this.answerTemplateNo = answerTemplateNo;
	}
	public String getAnswerRegistrationDateTime() {
		return answerRegistrationDateTime;
	}
	public void setAnswerRegistrationDateTime(String answerRegistrationDateTime) {
		this.answerRegistrationDateTime = answerRegistrationDateTime;
	}
	public boolean isAnswered() {
		return answered;
	}
	public void setAnswered(boolean answered) {
		this.answered = answered;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getProductOrderIdList() {
		return productOrderIdList;
	}
	public void setProductOrderIdList(String productOrderIdList) {
		this.productOrderIdList = productOrderIdList;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductOrderOption() {
		return productOrderOption;
	}
	public void setProductOrderOption(String productOrderOption) {
		this.productOrderOption = productOrderOption;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	@Override
	public String toString() {
		return "CustCounsel [inquiryNo=" + inquiryNo + ", category=" + category + ", title=" + title
				+ ", inquiryContent=" + inquiryContent + ", inquiryRegistrationDateTime=" + inquiryRegistrationDateTime
				+ ", answerContentId=" + answerContentId + ", answerContent=" + answerContent + ", answerTemplateNo="
				+ answerTemplateNo + ", answerRegistrationDateTime=" + answerRegistrationDateTime + ", answered="
				+ answered + ", orderId=" + orderId + ", productNo=" + productNo + ", productOrderIdList="
				+ productOrderIdList + ", productName=" + productName + ", productOrderOption=" + productOrderOption
				+ ", customerId=" + customerId + ", customerName=" + customerName + "]";
	}

}
