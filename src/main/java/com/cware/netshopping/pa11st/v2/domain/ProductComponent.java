package com.cware.netshopping.pa11st.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductComponent {

	// 추가상품명 (필수)
	// 추가 구성상품에 그룹핑이 가능합니다.
	// 같은 그룹네임끼리 묶어집니다.
	private String addPrdGrpNm;

	// 추가상품값 (필수)
	// 40Byte 까지만 입력가능하며 특수 문자[&#39;,",%,&,<,>,#,†]는 입력할 수 없습니다.
	private String compPrdNm;

	// 판매자추가상품번호 (필수)
	// 중복이 가능합니다.
	// 필수값이 아니며 생략 가능합니다.
	private String sellerAddPrdCd;

	// 추가구성 가격 (필수)
	private String addCompPrc;

	// 판매수량
	private String compPrdQty;

	// 부가세 (필수)
	// 01 : 과세상품
	// 02 : 면세상품
	// 03 : 영세상품
	private String compPrdVatCd;

	// 상태 (필수)
	// Y : 사용함
	// N : 사용안함
	private String addUseYn;

	// 추가 구성 무게 (필수)
	// 배송 주체(dlvClf) 코드가 03(11번가 해외 배송)인 경우 또는 "전세계배송 상품" 인경우 추가구성 등록시 필수입니다.
	// 단위 g
	private String addPrdWght;

	public String getAddPrdGrpNm() {
		return addPrdGrpNm;
	}

	public void setAddPrdGrpNm(String addPrdGrpNm) {
		this.addPrdGrpNm = addPrdGrpNm;
	}

	public String getCompPrdNm() {
		return compPrdNm;
	}

	public void setCompPrdNm(String compPrdNm) {
		this.compPrdNm = compPrdNm;
	}

	public String getSellerAddPrdCd() {
		return sellerAddPrdCd;
	}

	public void setSellerAddPrdCd(String sellerAddPrdCd) {
		this.sellerAddPrdCd = sellerAddPrdCd;
	}

	public String getAddCompPrc() {
		return addCompPrc;
	}

	public void setAddCompPrc(String addCompPrc) {
		this.addCompPrc = addCompPrc;
	}

	public String getCompPrdQty() {
		return compPrdQty;
	}

	public void setCompPrdQty(String compPrdQty) {
		this.compPrdQty = compPrdQty;
	}

	public String getCompPrdVatCd() {
		return compPrdVatCd;
	}

	public void setCompPrdVatCd(String compPrdVatCd) {
		this.compPrdVatCd = compPrdVatCd;
	}

	public String getAddUseYn() {
		return addUseYn;
	}

	public void setAddUseYn(String addUseYn) {
		this.addUseYn = addUseYn;
	}

	public String getAddPrdWght() {
		return addPrdWght;
	}

	public void setAddPrdWght(String addPrdWght) {
		this.addPrdWght = addPrdWght;
	}

	@Override
	public String toString() {
		return "ProductComponent [addPrdGrpNm=" + addPrdGrpNm + ", compPrdNm=" + compPrdNm + ", sellerAddPrdCd="
				+ sellerAddPrdCd + ", addCompPrc=" + addCompPrc + ", compPrdQty=" + compPrdQty + ", compPrdVatCd="
				+ compPrdVatCd + ", addUseYn=" + addUseYn + ", addPrdWght=" + addPrdWght + "]";
	}

}
