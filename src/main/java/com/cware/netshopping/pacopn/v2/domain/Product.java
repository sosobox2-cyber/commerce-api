package com.cware.netshopping.pacopn.v2.domain;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Product {
	String sellerProductId; // 등록상품ID

	// 등록상품상태
	// 심사중/ 임시저장/ 승인대기중/ 승인완료/ 부분승인완료/ 승인반려/ 상품삭제
	String statusName;

	// 노출카테고리코드
	// 카테고리 목록 조회 API 또는 카테고리 정보 excel을 다운받아 노출카테고리코드 확인 가능
	String displayCategoryCode;

	// 등록상품명
	// 발주서에 사용되는 상품명
	// 최대 길이 : 100 자
	String sellerProductName;

	String productId; // 노출상품ID

	// 판매자ID
	// 쿠팡에서 업체에게 발급한 고유 코드
	String vendorId;

	// 판매시작일시
	// "yyyy-MM-dd'T'HH:mm:ss" 형식
	String saleStartedAt;

	// 판매종료일시
	// "yyyy-MM-dd'T'HH:mm:ss" 형식
	// *2099년 까지 길게 선택 가능
	String saleEndedAt;

	// 노출상품명
	// 실제 쿠팡 판매페이지에서 노출될 상품명이나 실제 노출 시 변경될 수 있으며 브랜드, 제품명, 상품군의 변경에 의해 변경
	// [brand]+[generalProductName]과 동일하게 입력할 것을 권장, 미입력 상태로도 등록 가능
	// 미입력 시 [brand]+[generalProductName]이 노출되거나, [sellerProductName]이 노출될 수 있음
	// 최대 길이 : 150 자
	String displayProductName;

	// 브랜드
	// 브랜드명은 한글/영어 표준이름 입력
	// 띄어쓰기 및 특수문자 없이 입력
	String brand;

	// 제품명
	// 구매옵션[Attribute exposed] 정보(사이즈, 색상 등)를 포함하지 않는 상품명. 모델명 추가 기입 가능
	String generalProductName;

	// 상품군
	// 상품의 그룹으로 노출카테고리의 최하위명을 참고하여 입력.
	// 제품명[generalProductName]과 중복될 경우, 입력 불필요
	String productGroup;

	// 배송방법
	// SEQUENCIAL: 일반배송(순차배송)
	// COLD_FRESH: 신선냉동
	// MAKE_ORDER: 주문제작
	// AGENT_BUY: 구매대행
	// VENDOR_DIRECT: 설치배송 또는 판매자 직접 전달
	// 신선식품은 반드시 일반배송이 아닌 신선냉동 타입을 선택
	String deliveryMethod;

	// 택배사 코드
	// https://developers.coupangcorp.com/hc/ko/articles/360034156033
	String deliveryCompanyCode;

	// 배송비종류
	// FREE-무료배송, NOT_FREE-유료배송, CHARGE_RECEIVED-착불배송, CONDITIONAL_FREE-조건부무료배송
	// 무료배송 설정 시 초도반품배송비(편도)[deliveryChargeOnReturn]와, 반품배송비(편도)[returnCharge] 금액 설정
	// 유료배송 설정 시	기본배송비[deliveryCharge]와 반품배송비(편도) 금액 설정
	// 조건부 무료배송 설정 시 기본배송비와 반품배송비(편도) 금액 설정
	// 착불배송 설정 시	착불배송 가능 카테고리는 따로 정리되어 있으며, 판매자 안내를 위해 판매자콜센터에 공유
	// **[CONDITIONAL_FREE] 사용 시, 원하는 조건부 무료배송 금액을 별도로 설정
	String deliveryChargeType;

	// 기본배송비
	// 유료배송 또는 조건부 무료배송 시, 편도 배송비 금액 입력
	long deliveryCharge;

	// 무료배송을 위한 조건 금액
	// 예시 : 10,000원 이상 조건부 무료배송을 설정하기 원할 경우 [deliveryChargeType]을 'CONDITIONAL_FREE'로 설정 후, [freeShipOverAmount]에 10000을 입력
	// 100원 이상 단위로 입력 가능
	// 무료배송인 경우, 0 입력
	long freeShipOverAmount;

	// 초도반품배송비
	// 무료배송인 경우 반품시 소비자가 지불하는 배송비
	long deliveryChargeOnReturn;

	// 도서산간 배송여부
	// Y: 도서산간 배송
	// N: 도서산간 배송안함
	String remoteAreaDeliverable;

	// 묶음 배송여부
	// UNION_DELIVERY: 묶음 배송 가능
	// NOT_UNION_DELIVERY: 묶음 배송 불가능
	// ** 묶음 배송 조건
	// 출고지 정보 필수 입력 (출고지 정보가 같은 상품만 묶음 배송 가능)
	// 착불배송 불가 설정 불가
	String unionDeliveryType;

	// 반품지센터코드
	// 반품지 생성 후, 추출 된 반품지 센터코드를 입력
	// 반품지 생성은 Wing 또는 반품지 생성 API를 통해 가능
	// 반품지 생성이 불가능한 경우, "NO_RETURN_CENTERCODE"를 입력하여 직접 반품지 정보 등록 가능
	// **반품자동연동 서비스(굿스플로우)는 계약한 택배사가 있어야만 이용이 가능하며, 반품지 센터코드 입력 필수
	// **해외 배송 상품은 반드시 국내 반품지 주소지와 계약된 택배사 코드를 입력하여 반품지 센터 코드를 생성
	// *해외 배송 상품 반품지 가이드 https://developers.coupangcorp.com/hc/ko/articles/360023109273
	String returnCenterCode;

	// 반품지명
	// 쿠팡 Wing 또는 반품지 조회 API를 통해 반품지 등록 후 확인
	// '반품지 조회 시, shippingPlaceName로 노출되는 값 입력'
	String returnChargeName;

	// 반품지연락처
	// 쿠팡 Wing 또는 반품지 조회 API를 통해 반품지 등록 후 확인
	String companyContactNumber;

	// 반품지우편번호
	// 쿠팡 Wing 또는 반품지 생성 API를 통해 반품지 등록 후 확인
	String returnZipCode;

	// 반품지주소
	// 쿠팡 Wing 또는 반품지 생성 API를 통해 반품지 등록 후 확인
	String returnAddress;

	// 반품지주소상세
	// 쿠팡 Wing 또는 반품지 생성 API를 통해 반품지 등록 후 확인
	String returnAddressDetail;

	// 반품배송비
	// 반품회수시 편도 배송비
	// *초도반품배송비와 비교 시, 100~150%까지만 입력 가능
	long returnCharge;

	String afterServiceInformation; // A/S안내
	String afterServiceContactNumber; // A/S전화번호

	// 출고지주소코드
	// 묶음 배송 선택할 경우 필수, 출고지 조회 API를 통해 조회 가능
	String outboundShippingPlaceCode;

	// 실사용자아이디(쿠팡 Wing 아이디)
	// 업체(Vendor)에 소속된 사용자아이디
	String vendorUserId;

	// 자동승인요청여부
	// 상품 등록 시, 자동으로 판매승인요청을 진행할지 여부 선택
	// false : 작성 내용만 저장하고 판매요청 전 상태 (판매를 원할 시에는 상품 승인요청 API 또는 wing에서 판매요청을 진행 해야 함)
	// true : 저장 및 자동으로 판매 승인 요청
	boolean requested;

	// 업체상품옵션목록
	// 최대 200개 옵션 등록 가능
	List<Item> items;

	// 구비 서류 필수인 경우 입력
	// 구비서류는 5MB이하의 파일 입력 가능 (PDF, HWP, DOC, DOCX, TXT, PNG, JPG, JPEG)
	List<Map<String, String>> requiredDocuments;

	// 주문제작 안내 메시지
	// 배송 방법을 '주문제작'으로 선택했을 경우, 고객에게 안내할 메시지를 입력
	String extraInfoMessage;

	// 제조사
	// 정확한 제조사를 기입할 수 없는 경우, [brand] 항목과 동일하게 입력 가능
	String manufacture;

	public String getSellerProductId() {
		return sellerProductId;
	}

	public void setSellerProductId(String sellerProductId) {
		this.sellerProductId = sellerProductId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getDisplayCategoryCode() {
		return displayCategoryCode;
	}

	public void setDisplayCategoryCode(String displayCategoryCode) {
		this.displayCategoryCode = displayCategoryCode;
	}

	public String getSellerProductName() {
		return sellerProductName;
	}

	public void setSellerProductName(String sellerProductName) {
		this.sellerProductName = sellerProductName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getSaleStartedAt() {
		return saleStartedAt;
	}

	public void setSaleStartedAt(String saleStartedAt) {
		this.saleStartedAt = saleStartedAt;
	}

	public String getSaleEndedAt() {
		return saleEndedAt;
	}

	public void setSaleEndedAt(String saleEndedAt) {
		this.saleEndedAt = saleEndedAt;
	}

	public String getDisplayProductName() {
		return displayProductName;
	}

	public void setDisplayProductName(String displayProductName) {
		this.displayProductName = displayProductName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getGeneralProductName() {
		return generalProductName;
	}

	public void setGeneralProductName(String generalProductName) {
		this.generalProductName = generalProductName;
	}

	public String getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}

	public String getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public String getDeliveryCompanyCode() {
		return deliveryCompanyCode;
	}

	public void setDeliveryCompanyCode(String deliveryCompanyCode) {
		this.deliveryCompanyCode = deliveryCompanyCode;
	}

	public String getDeliveryChargeType() {
		return deliveryChargeType;
	}

	public void setDeliveryChargeType(String deliveryChargeType) {
		this.deliveryChargeType = deliveryChargeType;
	}

	public long getDeliveryCharge() {
		return deliveryCharge;
	}

	public void setDeliveryCharge(long deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}

	public long getFreeShipOverAmount() {
		return freeShipOverAmount;
	}

	public void setFreeShipOverAmount(long freeShipOverAmount) {
		this.freeShipOverAmount = freeShipOverAmount;
	}

	public long getDeliveryChargeOnReturn() {
		return deliveryChargeOnReturn;
	}

	public void setDeliveryChargeOnReturn(long deliveryChargeOnReturn) {
		this.deliveryChargeOnReturn = deliveryChargeOnReturn;
	}

	public String getRemoteAreaDeliverable() {
		return remoteAreaDeliverable;
	}

	public void setRemoteAreaDeliverable(String remoteAreaDeliverable) {
		this.remoteAreaDeliverable = remoteAreaDeliverable;
	}

	public String getUnionDeliveryType() {
		return unionDeliveryType;
	}

	public void setUnionDeliveryType(String unionDeliveryType) {
		this.unionDeliveryType = unionDeliveryType;
	}

	public String getReturnCenterCode() {
		return returnCenterCode;
	}

	public void setReturnCenterCode(String returnCenterCode) {
		this.returnCenterCode = returnCenterCode;
	}

	public String getReturnChargeName() {
		return returnChargeName;
	}

	public void setReturnChargeName(String returnChargeName) {
		this.returnChargeName = returnChargeName;
	}

	public String getCompanyContactNumber() {
		return companyContactNumber;
	}

	public void setCompanyContactNumber(String companyContactNumber) {
		this.companyContactNumber = companyContactNumber;
	}

	public String getReturnZipCode() {
		return returnZipCode;
	}

	public void setReturnZipCode(String returnZipCode) {
		this.returnZipCode = returnZipCode;
	}

	public String getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}

	public String getReturnAddressDetail() {
		return returnAddressDetail;
	}

	public void setReturnAddressDetail(String returnAddressDetail) {
		this.returnAddressDetail = returnAddressDetail;
	}

	public long getReturnCharge() {
		return returnCharge;
	}

	public void setReturnCharge(long returnCharge) {
		this.returnCharge = returnCharge;
	}

	public String getAfterServiceInformation() {
		return afterServiceInformation;
	}

	public void setAfterServiceInformation(String afterServiceInformation) {
		this.afterServiceInformation = afterServiceInformation;
	}

	public String getAfterServiceContactNumber() {
		return afterServiceContactNumber;
	}

	public void setAfterServiceContactNumber(String afterServiceContactNumber) {
		this.afterServiceContactNumber = afterServiceContactNumber;
	}

	public String getOutboundShippingPlaceCode() {
		return outboundShippingPlaceCode;
	}

	public void setOutboundShippingPlaceCode(String outboundShippingPlaceCode) {
		this.outboundShippingPlaceCode = outboundShippingPlaceCode;
	}

	public String getVendorUserId() {
		return vendorUserId;
	}

	public void setVendorUserId(String vendorUserId) {
		this.vendorUserId = vendorUserId;
	}

	public boolean isRequested() {
		return requested;
	}

	public void setRequested(boolean requested) {
		this.requested = requested;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<Map<String, String>> getRequiredDocuments() {
		return requiredDocuments;
	}

	public void setRequiredDocuments(List<Map<String, String>> requiredDocuments) {
		this.requiredDocuments = requiredDocuments;
	}

	public String getExtraInfoMessage() {
		return extraInfoMessage;
	}

	public void setExtraInfoMessage(String extraInfoMessage) {
		this.extraInfoMessage = extraInfoMessage;
	}

	public String getManufacture() {
		return manufacture;
	}

	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

	@Override
	public String toString() {
		return "Product [sellerProductId=" + sellerProductId + ", statusName=" + statusName + ", displayCategoryCode="
				+ displayCategoryCode + ", sellerProductName=" + sellerProductName + ", productId=" + productId
				+ ", vendorId=" + vendorId + ", saleStartedAt=" + saleStartedAt + ", saleEndedAt=" + saleEndedAt
				+ ", displayProductName=" + displayProductName + ", brand=" + brand + ", generalProductName="
				+ generalProductName + ", productGroup=" + productGroup + ", deliveryMethod=" + deliveryMethod
				+ ", deliveryCompanyCode=" + deliveryCompanyCode + ", deliveryChargeType=" + deliveryChargeType
				+ ", deliveryCharge=" + deliveryCharge + ", freeShipOverAmount=" + freeShipOverAmount
				+ ", deliveryChargeOnReturn=" + deliveryChargeOnReturn + ", remoteAreaDeliverable="
				+ remoteAreaDeliverable + ", unionDeliveryType=" + unionDeliveryType + ", returnCenterCode="
				+ returnCenterCode + ", returnChargeName=" + returnChargeName + ", companyContactNumber="
				+ companyContactNumber + ", returnZipCode=" + returnZipCode + ", returnAddress=" + returnAddress
				+ ", returnAddressDetail=" + returnAddressDetail + ", returnCharge=" + returnCharge
				+ ", afterServiceInformation=" + afterServiceInformation + ", afterServiceContactNumber="
				+ afterServiceContactNumber + ", outboundShippingPlaceCode=" + outboundShippingPlaceCode
				+ ", vendorUserId=" + vendorUserId + ", requested=" + requested + ", items=" + items
				+ ", requiredDocuments=" + requiredDocuments + ", extraInfoMessage=" + extraInfoMessage
				+ ", manufacture=" + manufacture + "]";
	}
	
}
