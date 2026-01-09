package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Book {
	String isbn; // 국제 표준 도서 번호
	String isbnExtensClsCd; // 국제 표준 도서 번호 확장 분류 코드
	String bookEngNm; // 도서 영문 명
	String ortitlNm; // 원 제목 명
	String subtitlNm; // 부 제목 명
	String authorNm; // 저자 명
	String trltpeNm; // 역자 명
	String pubscoNm; // 출판사 명
	String pageCntCntt; // 페이지 수 내용
	String pubsDt; // 출판 일자 (YYYYMMDD)
	Integer fxprc; // 정찰가

	// 정찰가통화코드 (commCd:I217)
	// 10 원(한국)
	// 20 달러(미국)
	// 30 엔(일본)
	// 40 위안(중국)
	// 50 프랑스(프랑스)
	// 60 파운드(영국)
	String fxprcCurrCd;
	String appxBeingYn; // 부록존재여부
	String appxCntt; // 부록내용
	String tbcnttCntt; // 목차내용
	String authorIntroCntt; // 저자 소개 내용
	String trltpeIntroCntt; // 역자 소개 내용
	String prfaceCntt; // 머리말 내용
	String pubscoBkrevwCntt; // 출판사 서평 내용
	String bookFxprcRegmAplYn; // 도서 정찰가 제도 적용 여부

	// 도서 상품 구분 코드 (commCd:I216)
	// N 일반
	// B 도서
	// M 음반
	String bookItemDivCd;
	String limitGrdNm; // 제한 등급 명
	String etcCntt; // 기타 내용
	String addGuideCntt; // 추가 안내 내용
	Integer sellScr; // 판매 점수
	String bookCpnNo; // 도서 쿠폰 번호
	Integer bookCpnDcAmt; // 도서 쿠폰 할인 금액

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getIsbnExtensClsCd() {
		return isbnExtensClsCd;
	}

	public void setIsbnExtensClsCd(String isbnExtensClsCd) {
		this.isbnExtensClsCd = isbnExtensClsCd;
	}

	public String getBookEngNm() {
		return bookEngNm;
	}

	public void setBookEngNm(String bookEngNm) {
		this.bookEngNm = bookEngNm;
	}

	public String getOrtitlNm() {
		return ortitlNm;
	}

	public void setOrtitlNm(String ortitlNm) {
		this.ortitlNm = ortitlNm;
	}

	public String getSubtitlNm() {
		return subtitlNm;
	}

	public void setSubtitlNm(String subtitlNm) {
		this.subtitlNm = subtitlNm;
	}

	public String getAuthorNm() {
		return authorNm;
	}

	public void setAuthorNm(String authorNm) {
		this.authorNm = authorNm;
	}

	public String getTrltpeNm() {
		return trltpeNm;
	}

	public void setTrltpeNm(String trltpeNm) {
		this.trltpeNm = trltpeNm;
	}

	public String getPubscoNm() {
		return pubscoNm;
	}

	public void setPubscoNm(String pubscoNm) {
		this.pubscoNm = pubscoNm;
	}

	public String getPageCntCntt() {
		return pageCntCntt;
	}

	public void setPageCntCntt(String pageCntCntt) {
		this.pageCntCntt = pageCntCntt;
	}

	public String getPubsDt() {
		return pubsDt;
	}

	public void setPubsDt(String pubsDt) {
		this.pubsDt = pubsDt;
	}

	public Integer getFxprc() {
		return fxprc;
	}

	public void setFxprc(Integer fxprc) {
		this.fxprc = fxprc;
	}

	public String getFxprcCurrCd() {
		return fxprcCurrCd;
	}

	public void setFxprcCurrCd(String fxprcCurrCd) {
		this.fxprcCurrCd = fxprcCurrCd;
	}

	public String getAppxBeingYn() {
		return appxBeingYn;
	}

	public void setAppxBeingYn(String appxBeingYn) {
		this.appxBeingYn = appxBeingYn;
	}

	public String getAppxCntt() {
		return appxCntt;
	}

	public void setAppxCntt(String appxCntt) {
		this.appxCntt = appxCntt;
	}

	public String getTbcnttCntt() {
		return tbcnttCntt;
	}

	public void setTbcnttCntt(String tbcnttCntt) {
		this.tbcnttCntt = tbcnttCntt;
	}

	public String getAuthorIntroCntt() {
		return authorIntroCntt;
	}

	public void setAuthorIntroCntt(String authorIntroCntt) {
		this.authorIntroCntt = authorIntroCntt;
	}

	public String getTrltpeIntroCntt() {
		return trltpeIntroCntt;
	}

	public void setTrltpeIntroCntt(String trltpeIntroCntt) {
		this.trltpeIntroCntt = trltpeIntroCntt;
	}

	public String getPrfaceCntt() {
		return prfaceCntt;
	}

	public void setPrfaceCntt(String prfaceCntt) {
		this.prfaceCntt = prfaceCntt;
	}

	public String getPubscoBkrevwCntt() {
		return pubscoBkrevwCntt;
	}

	public void setPubscoBkrevwCntt(String pubscoBkrevwCntt) {
		this.pubscoBkrevwCntt = pubscoBkrevwCntt;
	}

	public String getBookFxprcRegmAplYn() {
		return bookFxprcRegmAplYn;
	}

	public void setBookFxprcRegmAplYn(String bookFxprcRegmAplYn) {
		this.bookFxprcRegmAplYn = bookFxprcRegmAplYn;
	}

	public String getBookItemDivCd() {
		return bookItemDivCd;
	}

	public void setBookItemDivCd(String bookItemDivCd) {
		this.bookItemDivCd = bookItemDivCd;
	}

	public String getLimitGrdNm() {
		return limitGrdNm;
	}

	public void setLimitGrdNm(String limitGrdNm) {
		this.limitGrdNm = limitGrdNm;
	}

	public String getEtcCntt() {
		return etcCntt;
	}

	public void setEtcCntt(String etcCntt) {
		this.etcCntt = etcCntt;
	}

	public String getAddGuideCntt() {
		return addGuideCntt;
	}

	public void setAddGuideCntt(String addGuideCntt) {
		this.addGuideCntt = addGuideCntt;
	}

	public Integer getSellScr() {
		return sellScr;
	}

	public void setSellScr(Integer sellScr) {
		this.sellScr = sellScr;
	}

	public String getBookCpnNo() {
		return bookCpnNo;
	}

	public void setBookCpnNo(String bookCpnNo) {
		this.bookCpnNo = bookCpnNo;
	}

	public Integer getBookCpnDcAmt() {
		return bookCpnDcAmt;
	}

	public void setBookCpnDcAmt(Integer bookCpnDcAmt) {
		this.bookCpnDcAmt = bookCpnDcAmt;
	}

	@Override
	public String toString() {
		return "Book [isbn=" + isbn + ", isbnExtensClsCd=" + isbnExtensClsCd + ", bookEngNm=" + bookEngNm
				+ ", ortitlNm=" + ortitlNm + ", subtitlNm=" + subtitlNm + ", authorNm=" + authorNm + ", trltpeNm="
				+ trltpeNm + ", pubscoNm=" + pubscoNm + ", pageCntCntt=" + pageCntCntt + ", pubsDt=" + pubsDt
				+ ", fxprc=" + fxprc + ", fxprcCurrCd=" + fxprcCurrCd + ", appxBeingYn=" + appxBeingYn + ", appxCntt="
				+ appxCntt + ", tbcnttCntt=" + tbcnttCntt + ", authorIntroCntt=" + authorIntroCntt
				+ ", trltpeIntroCntt=" + trltpeIntroCntt + ", prfaceCntt=" + prfaceCntt + ", pubscoBkrevwCntt="
				+ pubscoBkrevwCntt + ", bookFxprcRegmAplYn=" + bookFxprcRegmAplYn + ", bookItemDivCd=" + bookItemDivCd
				+ ", limitGrdNm=" + limitGrdNm + ", etcCntt=" + etcCntt + ", addGuideCntt=" + addGuideCntt
				+ ", sellScr=" + sellScr + ", bookCpnNo=" + bookCpnNo + ", bookCpnDcAmt=" + bookCpnDcAmt + "]";
	}

}
