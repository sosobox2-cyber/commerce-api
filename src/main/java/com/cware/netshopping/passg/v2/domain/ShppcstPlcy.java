package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "requestShppcstPlcyInsert")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShppcstPlcy {
	// 배송비정책 수정 시 필수
	// 배송비 ID
	String shppcstId;

	// 배송비정책구분코드 (commCd:I165)
	// 10 출고배송비 정책
	// 20 반품배송비 정책
	// 60 추가배송비 정책(도서산간)
	// 70 추가배송비 정책(제주)
	String shppcstPlcyDivCd;

	// 배송비 적용 단위 코드 (commCd:I113)
	// 출고, 추가 배송비 정책 등록 시
	// (shppcstPlcyDivCd = 10, 60, 70) :
	// 10 주문 금액 합산
	// 30 상품 수량 별
	// 40 상품별 주문금액 합산
	// 반품 배송비 정책 등록 시
	// (shppcstPlcyDivCd = 20) :
	// 10 주문 금액 합산
	// 30 상품 수량 별
	// 40 상품별 주문금액 합산
	String shppcstAplUnitCd;

	// 선불 착불 구분 코드 (commCd:I115)
	// 10 배송비선불
	// 20 배송비착불
	String prpayCodDivCd;

	// 출고배송비 정책 등록시에만 사용(shppcstPlcyDivCd = 10)
	// 면제 기준 금액(9자 이내)
	// * 면제기준금액 이상일 경우 배송비 무료
	Integer shppcstExmpCritnAmt;
	Integer shppcst; // 배송비(9자 이내)
	String shppcstCntt; // 배송비 설명(250자 이내)

	// 기본 정책 여부(Y/N)
	// 출고배송비정책 : 기본정책 Y 복수 사용 허용
	// 반품배송비정책 : 기본정책 Y 복수 사용 불가
	// 추가배송비정책 : 기본정책 Y 미사용(N 고정)
	String bascPlcyYn;
	String shppcstAplYn; // 배송비 적용 여부(Y/N)

	public String getShppcstId() {
		return shppcstId;
	}

	public void setShppcstId(String shppcstId) {
		this.shppcstId = shppcstId;
	}

	public String getShppcstPlcyDivCd() {
		return shppcstPlcyDivCd;
	}

	public void setShppcstPlcyDivCd(String shppcstPlcyDivCd) {
		this.shppcstPlcyDivCd = shppcstPlcyDivCd;
	}

	public String getShppcstAplUnitCd() {
		return shppcstAplUnitCd;
	}

	public void setShppcstAplUnitCd(String shppcstAplUnitCd) {
		this.shppcstAplUnitCd = shppcstAplUnitCd;
	}

	public String getPrpayCodDivCd() {
		return prpayCodDivCd;
	}

	public void setPrpayCodDivCd(String prpayCodDivCd) {
		this.prpayCodDivCd = prpayCodDivCd;
	}

	public Integer getShppcstExmpCritnAmt() {
		return shppcstExmpCritnAmt;
	}

	public void setShppcstExmpCritnAmt(Integer shppcstExmpCritnAmt) {
		this.shppcstExmpCritnAmt = shppcstExmpCritnAmt;
	}

	public Integer getShppcst() {
		return shppcst;
	}

	public void setShppcst(Integer shppcst) {
		this.shppcst = shppcst;
	}

	public String getShppcstCntt() {
		return shppcstCntt;
	}

	public void setShppcstCntt(String shppcstCntt) {
		this.shppcstCntt = shppcstCntt;
	}

	public String getBascPlcyYn() {
		return bascPlcyYn;
	}

	public void setBascPlcyYn(String bascPlcyYn) {
		this.bascPlcyYn = bascPlcyYn;
	}

	public String getShppcstAplYn() {
		return shppcstAplYn;
	}

	public void setShppcstAplYn(String shppcstAplYn) {
		this.shppcstAplYn = shppcstAplYn;
	}

	@Override
	public String toString() {
		return "ShppcstPlcy [shppcstId=" + shppcstId + ", shppcstPlcyDivCd=" + shppcstPlcyDivCd + ", shppcstAplUnitCd="
				+ shppcstAplUnitCd + ", prpayCodDivCd=" + prpayCodDivCd + ", shppcstExmpCritnAmt=" + shppcstExmpCritnAmt
				+ ", shppcst=" + shppcst + ", shppcstCntt=" + shppcstCntt + ", bascPlcyYn=" + bascPlcyYn
				+ ", shppcstAplYn=" + shppcstAplYn + "]";
	}

}
