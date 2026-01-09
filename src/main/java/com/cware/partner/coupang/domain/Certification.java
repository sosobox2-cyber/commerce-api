package com.cware.partner.coupang.domain;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Certification {

	// 인증정보Type
	// 카테고리 메타정보 조회 API를 통해 등록가능한 Type을 구할 수 있다.
	// 인증대상이 아닌 카테고리일 경우 : NOT_REQUIRED
	String certificationType;

	// 상품인증정보코드
	// 인증기관에서 발급받은 코드
	String certificationCode;

	// 인증정보 첨부파일
	// 이동통신사전승낙서 및 인증대리점 인증서 등록 시 사용함
	// 1.이동통신 사전승낙서 또는 인증대리점 인증서
	// 2.판매점 사전승낙 인증마크 또는 이동통신사 대리점 인증마크
	// 아래 업체이미지 경로 또는 쿠팡CDN 경로 중 하나 이상 필수 입력 필요
	// 1. vendorPath: 업체에서 사용하는 이미지 경로, http://로 시작하는 경로일 경우 자동 다운로드하여 쿠팡 CDN에 추가됨
	// 2. cdnPath: 쿠팡 CDN 서버에 올린 경우 직접 입력
	//	Example:
	//	"certifications": [
	//	        {
	//	          "certificationType": "MOBILE_DEVICE_DEALER_PERMIT",
	//	          "certificationCode": "",
	//	          "certificationAttachments": [
	//	            {
	//	              "vendorPath": "http://vendor.com/image/vendoritem/3001519145/cert.jpg"
	//	            },
	//	            {
	//	              "vendorPath": "http://vendor.com/image/vendoritem/3001519145/logo.jpg"
	//	           }
	//	          ]
	//	        }
	//	]
	List<Map<String,String>> certificationAttachments;
}
