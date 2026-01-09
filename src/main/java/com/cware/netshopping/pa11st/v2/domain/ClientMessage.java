package com.cware.netshopping.pa11st.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientMessage {

	// 출력 결과 메시지 (필수)
	// 에러는 모두 메시지로 처리 됩니다. 메시지를 꼭 확인해 주세요.
	// * 단일상품등록 했으나 일반상품으로 등록시 성공(code: 210) 처리됩니다.
	private String message;

	// 상품번호 (필수)
	// 상품등록시 11번가 상품코드가 발급됩니다.
	private String productNo;

	// 결과코드 (필수)
	// 일반상품 성공 : 200 / 단일상품 성공 : 210
	// * 단일상품으로 등록했으나 정책에 위배시 일반상품으로 성공(200) 처리되는 부분 유의
	// 200 : 성공 (일반상품등록)
	// 210 : 성공 (단일상품등록)
	// 500 : 에러
	// 상품등록실패 : 교환반품 안내는 반드시 입력하셔야 합니다.
	// 상품등록실패 : 1번째 옵션값1[Black Dandelion White/Men&#39;s 7]을 확인 하십시오. 특수
	// 문자[&#39;,",%,&,<,>,#,†]는 입력할 수 없습니다.
	// 상품등록실패 : Image URL 의 소스의 Content-Type이 이미지가 아니거나, gif/jpg/jpeg 가 아닙니다.
	// Header[Content-Type:text/html; charset=iso-8859-1]
	// 상품등록실패 : 상품홍보문구는 28바이트 초과하여 입력하실 수 없습니다.
	private String resultCode;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	@Override
	public String toString() {
		return "ClientMessage [message=" + message + ", productNo=" + productNo + ", resultCode=" + resultCode + "]";
	}

}
