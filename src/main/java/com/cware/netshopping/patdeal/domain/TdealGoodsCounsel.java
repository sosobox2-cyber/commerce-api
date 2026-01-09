package com.cware.netshopping.patdeal.domain;

import java.sql.Timestamp;

import com.cware.netshopping.patdeal.util.PaTdealComUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TdealGoodsCounsel {

	private String brandName;
	private String orderNo;
	private boolean replied;
	private Timestamp replyYmdt;//날짜
	private String nickName;
	private String memberName;
	private String type;
	private String title;
	private String productName;
	private String content;
	private String memberNo;
	private String partnerNo;
	private Timestamp updateYmdt;//날짜
	private String displayStatusType;
	private String productNo;
	private Timestamp registerYmdt;//날짜
	private String memberId;
	private String inquiryNo;
	
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public boolean isReplied() {
		return replied;
	}
	public void setReplied(boolean replied) {
		this.replied = replied;
	}
	public Timestamp getReplyYmdt() {
		return replyYmdt;
	}
	public void setReplyYmdt(String replyYmdt) {
		this.replyYmdt = PaTdealComUtil.convertStrToTimestamp(replyYmdt);//string 형태 날짜형태 변환
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public String getPartnerNo() {
		return partnerNo;
	}
	public void setPartnerNo(String partnerNo) {
		this.partnerNo = partnerNo;
	}
	public Timestamp getUpdateYmdt() {
		return updateYmdt;
	}
	public void setUpdateYmdt(String updateYmdt) {
		this.updateYmdt = PaTdealComUtil.convertStrToTimestamp(updateYmdt);//string 형태 날짜형태 변환
	}
	public String getDisplayStatusType() {
		return displayStatusType;
	}
	public void setDisplayStatusType(String displayStatusType) {
		this.displayStatusType = displayStatusType;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public Timestamp getRegisterYmdt() {
		return registerYmdt;
	}
	public void setRegisterYmdt(String registerYmdt) {
		this.registerYmdt = PaTdealComUtil.convertStrToTimestamp(registerYmdt);//string 형태 날짜형태 변환
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getInquiryNo() {
		return inquiryNo;
	}
	public void setInquiryNo(String inquiryNo) {
		this.inquiryNo = inquiryNo;
	}

	
	
}
