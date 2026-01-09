package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Books {
		
	// 제품하자/오배송에 따른 청약철회 조항
	private String returnCostReason;
	// 제품하자가 아닌 소비자의 단순변심에 따른 청약철회가 불가능한 경우 그 구체적 사유와 근거
	private String noRefundReason;
	// 재화 등의 교환ㆍ반품ㆍ보증 조건 및 품질 보증 기준
	private String qualityAssuranceStandard;
	// 대금을 환불받기 위한 방법과 환불이 지연될 경우 지연배상금을 지급받을 수 있다는 사실 및 배상금 지급의 구체적인 조건·절차
	private String compensationProcedure;
	// 소비자피해보상의 처리, 재화 등에 대한 불만 처리 및 소비자와 사업자 사이의 분쟁 처리에 관한 사항
	private String troubleShootingContents;
	// 도서명
	private String title;
	// 저자
	private String author;
	// 출판사
	private String publisher;
	// 크기
	private String size;
	// 쪽수
	private String pages;
	// 제품 구성(전집 또는 세트일 경우 낱권 구성, CD 등)
	private String components;
	// 발행일
	private String publishDate;
	// 발행일 직접 입력
	private String publishDateText;
	// 목차 또는 책 소개
	private String description;	
		
	
	public String getReturnCostReason() {
		return returnCostReason;
	}

	public void setReturnCostReason(String returnCostReason) {
		this.returnCostReason = returnCostReason;
	}

	public String getNoRefundReason() {
		return noRefundReason;
	}

	public void setNoRefundReason(String noRefundReason) {
		this.noRefundReason = noRefundReason;
	}

	public String getQualityAssuranceStandard() {
		return qualityAssuranceStandard;
	}

	public void setQualityAssuranceStandard(String qualityAssuranceStandard) {
		this.qualityAssuranceStandard = qualityAssuranceStandard;
	}

	public String getCompensationProcedure() {
		return compensationProcedure;
	}

	public void setCompensationProcedure(String compensationProcedure) {
		this.compensationProcedure = compensationProcedure;
	}

	public String getTroubleShootingContents() {
		return troubleShootingContents;
	}

	public void setTroubleShootingContents(String troubleShootingContents) {
		this.troubleShootingContents = troubleShootingContents;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String getComponents() {
		return components;
	}

	public void setComponents(String components) {
		this.components = components;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getPublishDateText() {
		return publishDateText;
	}

	public void setPublishDateText(String publishDateText) {
		this.publishDateText = publishDateText;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Books [returnCostReason=" + returnCostReason + "noRefundReason=" + noRefundReason + "qualityAssuranceStandard=" + qualityAssuranceStandard + "compensationProcedure=" + compensationProcedure + "troubleShootingContents=" + troubleShootingContents 
				 + "title=" + title + "author=" + author + "publisher=" + publisher + "size=" + size + "pages=" + pages+ "components=" + components+ "publishDate=" + publishDate + "publishDateText=" + publishDateText +"description=" + description +"]";
	}
		
}
