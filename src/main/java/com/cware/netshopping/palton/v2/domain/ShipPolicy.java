package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ShipPolicy {

	// 배송정보 번호 (필수)
	String shipPolicyNo;
	// 배송정보명 (필수)
	String shipPolicyName;
	// 배송방법
	// (KP:일반-택배배송, KD:일반-직접배송, KM:일반-우편배송, OP:해외구매대행-택배배송, OD:해외구매대행-직접배송)
	String shipMethod;
	// 배송유형 (PRD:상품별배송, BND:묶음배송(판매자별)) - (등록 후 수정불가)
	String bundleKind;
	// 배송비종류
	// (FREE:무료, FIXED:유료, COND:조건부 무료)
	String shipType;
	// 배송비 (1~9999999)
	Long shipFee;
	// 배송비 노출여부(Y:노출(기본값), N:비노출) - 착불인 경우만 비노출 설정가능
	String shipFeeDisplayYn;
	// 조건부 무료 기준금액
	Long freeCondition;
	// 수량별 차등 적용여부 (Y:설정, N:설정안함-기본값)
	// - 배송유형=상품별배송 & 배송비종류=유료 인 경우만 설정 가능
	String differenceYn;
	// 수량별 차등 적용 기준수량 (1~9999)
	// - 배송유형=상품별배송 & 배송비종류=유료 & 수량별 차등 적용여부=Y인 경우 필수 입력
	Long differenceCount;
	// 배송비 결제방식 (Y:선결제-기본값, N:착불) - 배송비 종류가 ‘조건부 무료’인 경우 ‘선결제’ 고정(수정 불가)
	String prepaymentYn;
	// 반품/교환 배송비 (필수)
	// (편도) (0~9999999)
	Long claimShipFee;
	// 배송가능지역 (필수)
	// (WHOLE : 전국, NO_MOUNTAIN_ISLAND : 전국(제주/도서산간지역 제외))
	String shipArea;
	// 도서산간 추가 배송비(0~9999999) – 제주(배송가능지역 ‘전국’인 경우에만 입력)
	Long jejuShipFee;
	// 도서산간 추가 배송비(0~9999999) – 도서산간(배송가능지역 ‘전국’인 경우에만 입력)
	Long islandMountainShipFee;
	// 출고기한-날짜 (1~15) (필수)
	Integer releaseDay;
	// 출고기한-시간 (1~24) - releaseDay가 1일 일경우 입력
	Integer releaseTime;
	// 주말, 공휴일 발송여부 (Y:주말 공휴일 제외-기본값, A:주말과 공휴일 및 전일 제외, N:주말, 공휴일 포함)
	String holidayExceptYn;
	// 출고지-우편번호 (5자리) (필수)
	String releaseZipCode;
	// 출고지-기본주소(도로명) (필수)
	String releaseRoadAddress1;
	// 출고지-상세주소(도로명) (필수)
	String releaseRoadAddress2;
	// 출고지-기본주소(지번) (필수)
	String releaseAddress1;
	// 출고지-상세주소(지번) (필수)
	String releaseAddress2;
	// 회수지-우편번호 (5자리) (필수)
	String returnZipCode;
	// 회수지-기본주소(도로명) (필수)
	String returnRoadAddress1;
	// 회수지-상세주소(도로명) (필수)
	String returnRoadAddress2;
	// 회수지-기본주소(지번) (필수)
	String returnAddress1;
	// 회수지-상세주소(지번) (필수)
	String returnAddress2;
	// 안심번호 서비스 사용 노출여부(Y:노출, N:비노출)
	String safetyNoDisplayYn;

	public String getShipPolicyNo() {
		return shipPolicyNo;
	}

	public void setShipPolicyNo(String shipPolicyNo) {
		this.shipPolicyNo = shipPolicyNo;
	}

	public String getShipPolicyName() {
		return shipPolicyName;
	}

	public void setShipPolicyName(String shipPolicyName) {
		this.shipPolicyName = shipPolicyName;
	}

	public String getShipMethod() {
		return shipMethod;
	}

	public void setShipMethod(String shipMethod) {
		this.shipMethod = shipMethod;
	}

	public String getBundleKind() {
		return bundleKind;
	}

	public void setBundleKind(String bundleKind) {
		this.bundleKind = bundleKind;
	}

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

	public Long getShipFee() {
		return shipFee;
	}

	public void setShipFee(Long shipFee) {
		this.shipFee = shipFee;
	}

	public String getShipFeeDisplayYn() {
		return shipFeeDisplayYn;
	}

	public void setShipFeeDisplayYn(String shipFeeDisplayYn) {
		this.shipFeeDisplayYn = shipFeeDisplayYn;
	}

	public Long getFreeCondition() {
		return freeCondition;
	}

	public void setFreeCondition(Long freeCondition) {
		this.freeCondition = freeCondition;
	}

	public String getDifferenceYn() {
		return differenceYn;
	}

	public void setDifferenceYn(String differenceYn) {
		this.differenceYn = differenceYn;
	}

	public Long getDifferenceCount() {
		return differenceCount;
	}

	public void setDifferenceCount(Long differenceCount) {
		this.differenceCount = differenceCount;
	}

	public String getPrepaymentYn() {
		return prepaymentYn;
	}

	public void setPrepaymentYn(String prepaymentYn) {
		this.prepaymentYn = prepaymentYn;
	}

	public Long getClaimShipFee() {
		return claimShipFee;
	}

	public void setClaimShipFee(Long claimShipFee) {
		this.claimShipFee = claimShipFee;
	}

	public String getShipArea() {
		return shipArea;
	}

	public void setShipArea(String shipArea) {
		this.shipArea = shipArea;
	}

	public Long getJejuShipFee() {
		return jejuShipFee;
	}

	public void setJejuShipFee(Long jejuShipFee) {
		this.jejuShipFee = jejuShipFee;
	}

	public Long getIslandMountainShipFee() {
		return islandMountainShipFee;
	}

	public void setIslandMountainShipFee(Long islandMountainShipFee) {
		this.islandMountainShipFee = islandMountainShipFee;
	}

	public Integer getReleaseDay() {
		return releaseDay;
	}

	public void setReleaseDay(Integer releaseDay) {
		this.releaseDay = releaseDay;
	}

	public Integer getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Integer releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getHolidayExceptYn() {
		return holidayExceptYn;
	}

	public void setHolidayExceptYn(String holidayExceptYn) {
		this.holidayExceptYn = holidayExceptYn;
	}

	public String getReleaseZipCode() {
		return releaseZipCode;
	}

	public void setReleaseZipCode(String releaseZipCode) {
		this.releaseZipCode = releaseZipCode;
	}

	public String getReleaseRoadAddress1() {
		return releaseRoadAddress1;
	}

	public void setReleaseRoadAddress1(String releaseRoadAddress1) {
		this.releaseRoadAddress1 = releaseRoadAddress1;
	}

	public String getReleaseRoadAddress2() {
		return releaseRoadAddress2;
	}

	public void setReleaseRoadAddress2(String releaseRoadAddress2) {
		this.releaseRoadAddress2 = releaseRoadAddress2;
	}

	public String getReleaseAddress1() {
		return releaseAddress1;
	}

	public void setReleaseAddress1(String releaseAddress1) {
		this.releaseAddress1 = releaseAddress1;
	}

	public String getReleaseAddress2() {
		return releaseAddress2;
	}

	public void setReleaseAddress2(String releaseAddress2) {
		this.releaseAddress2 = releaseAddress2;
	}

	public String getReturnZipCode() {
		return returnZipCode;
	}

	public void setReturnZipCode(String returnZipCode) {
		this.returnZipCode = returnZipCode;
	}

	public String getReturnRoadAddress1() {
		return returnRoadAddress1;
	}

	public void setReturnRoadAddress1(String returnRoadAddress1) {
		this.returnRoadAddress1 = returnRoadAddress1;
	}

	public String getReturnRoadAddress2() {
		return returnRoadAddress2;
	}

	public void setReturnRoadAddress2(String returnRoadAddress2) {
		this.returnRoadAddress2 = returnRoadAddress2;
	}

	public String getReturnAddress1() {
		return returnAddress1;
	}

	public void setReturnAddress1(String returnAddress1) {
		this.returnAddress1 = returnAddress1;
	}

	public String getReturnAddress2() {
		return returnAddress2;
	}

	public void setReturnAddress2(String returnAddress2) {
		this.returnAddress2 = returnAddress2;
	}

	public String getSafetyNoDisplayYn() {
		return safetyNoDisplayYn;
	}

	public void setSafetyNoDisplayYn(String safetyNoDisplayYn) {
		this.safetyNoDisplayYn = safetyNoDisplayYn;
	}

	@Override
	public String toString() {
		return "ShipPolicy [shipPolicyNo=" + shipPolicyNo + ", shipPolicyName=" + shipPolicyName + ", shipMethod="
				+ shipMethod + ", bundleKind=" + bundleKind + ", shipType=" + shipType + ", shipFee=" + shipFee
				+ ", shipFeeDisplayYn=" + shipFeeDisplayYn + ", freeCondition=" + freeCondition + ", differenceYn="
				+ differenceYn + ", differenceCount=" + differenceCount + ", prepaymentYn=" + prepaymentYn
				+ ", claimShipFee=" + claimShipFee + ", shipArea=" + shipArea + ", jejuShipFee=" + jejuShipFee
				+ ", islandMountainShipFee=" + islandMountainShipFee + ", releaseDay=" + releaseDay + ", releaseTime="
				+ releaseTime + ", holidayExceptYn=" + holidayExceptYn + ", releaseZipCode=" + releaseZipCode
				+ ", releaseRoadAddress1=" + releaseRoadAddress1 + ", releaseRoadAddress2=" + releaseRoadAddress2
				+ ", releaseAddress1=" + releaseAddress1 + ", releaseAddress2=" + releaseAddress2 + ", returnZipCode="
				+ returnZipCode + ", returnRoadAddress1=" + returnRoadAddress1 + ", returnRoadAddress2="
				+ returnRoadAddress2 + ", returnAddress1=" + returnAddress1 + ", returnAddress2=" + returnAddress2
				+ ", safetyNoDisplayYn=" + safetyNoDisplayYn + "]";
	}

}
