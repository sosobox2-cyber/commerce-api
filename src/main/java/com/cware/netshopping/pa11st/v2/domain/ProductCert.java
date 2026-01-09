package com.cware.netshopping.pa11st.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductCert {

	// 인증유형 (필수)
	// 108 : 유기농산물
	// 109 : 무농약농산물
	// 110 : 저농약농산물
	// 111 : 유기축산물
	// 112 : 무항생제축산물
	// 113 : 친환경수산물
	// 114 : 위해요소 중점관리(HACCP)
	// 115 : 농산물 우수관리인증(GAP)
	// 116 : 가공식품 표준화인증(KS)
	// 117 : 유기가공식품 인증
	// 118 : 수산물 품질인증
	// 119 : 수산특산물 품질인증
	// 120 : 수산전통식품 품질인증
	// 122 : 건강기능식품 광고심의
	// 101 : [생활용품] 안전인증
	// 103 : [생활용품] 안전확인
	// 124 : [생활용품] 공급자적합성확인
	// 123 : [생활용품] 어린이보호포장
	// 102 : [전기용품] 안전인증
	// 104 : [전기용품] 안전확인
	// 127 : [전기용품] 공급자적합성확인
	// 132 : [전기용품/생활용품] 상품상세설명 참조
	// 128 : [어린이제품] 안전인증
	// 129 : [어린이제품] 안전확인
	// 130 : [어린이제품] 공급자적합성확인
	// 134 : [어린이제품] 상품상세설명 참조
	// 105 : [방송통신기자재] 적합성평가 (적합인증, 적합등록)
	// 135 : [방송통신기자재] 상품상세설명 참조
	// 133 : [생활화학 및 살생물제품] 자가검사번호
	// 136 : [생활화학 및 살생물제품] 상품상세설명 참조
	// 인증유형 등록항목 보기: http://i.011st.com/product/manual/Product_Cert_20220628_update.xlsx
	private String certTypeCd;

	// 인증번호 (필수)
	private String certKey;

	public String getCertTypeCd() {
		return certTypeCd;
	}

	public void setCertTypeCd(String certTypeCd) {
		this.certTypeCd = certTypeCd;
	}

	public String getCertKey() {
		return certKey;
	}

	public void setCertKey(String certKey) {
		this.certKey = certKey;
	}

	@Override
	public String toString() {
		return "ProductCert [certTypeCd=" + certTypeCd + ", certKey=" + certKey + "]";
	}

}
