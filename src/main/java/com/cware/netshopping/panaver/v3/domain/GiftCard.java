package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class GiftCard {
		
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
	// 발행자
	private String issuer;
	// 유효기간 시작일
	private String periodStartDate;
	// 유효기간 종료일
	private String periodEndDate;
	// 유효기간(구매일로부터 00일)
	private String periodDays;
	// 이용 조건
	private String termsOfUse;
	// 이용 가능 매장(장소)
	private String useStorePlace;
	// 이용 가능 매장(판매자 주소 ID)
	private String useStoreAddressId;
	// 이용 가능 매장(URL)
	private String useStoreUrl;
	// 잔액 환급 조건
	private String refundPolicy;
	// 소비자 상담 관련 전화번호
	private String customerServicePhoneNumber;

	
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

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getPeriodStartDate() {
		return periodStartDate;
	}

	public void setPeriodStartDate(String periodStartDate) {
		this.periodStartDate = periodStartDate;
	}

	public String getPeriodEndDate() {
		return periodEndDate;
	}

	public void setPeriodEndDate(String periodEndDate) {
		this.periodEndDate = periodEndDate;
	}

	public String getPeriodDays() {
		return periodDays;
	}

	public void setPeriodDays(String periodDays) {
		this.periodDays = periodDays;
	}

	public String getTermsOfUse() {
		return termsOfUse;
	}

	public void setTermsOfUse(String termsOfUse) {
		this.termsOfUse = termsOfUse;
	}

	public String getUseStorePlace() {
		return useStorePlace;
	}

	public void setUseStorePlace(String useStorePlace) {
		this.useStorePlace = useStorePlace;
	}

	public String getUseStoreAddressId() {
		return useStoreAddressId;
	}

	public void setUseStoreAddressId(String useStoreAddressId) {
		this.useStoreAddressId = useStoreAddressId;
	}

	public String getUseStoreUrl() {
		return useStoreUrl;
	}

	public void setUseStoreUrl(String useStoreUrl) {
		this.useStoreUrl = useStoreUrl;
	}

	public String getRefundPolicy() {
		return refundPolicy;
	}

	public void setRefundPolicy(String refundPolicy) {
		this.refundPolicy = refundPolicy;
	}

	public String getCustomerServicePhoneNumber() {
		return customerServicePhoneNumber;
	}

	public void setCustomerServicePhoneNumber(String customerServicePhoneNumber) {
		this.customerServicePhoneNumber = customerServicePhoneNumber;
	}

	@Override
	public String toString() {
		return "GiftCard [returnCostReason=" + returnCostReason + "noRefundReason=" + noRefundReason + "qualityAssuranceStandard=" + qualityAssuranceStandard + "compensationProcedure=" + compensationProcedure + "troubleShootingContents=" + troubleShootingContents 
				 + "issuer=" + issuer + "periodStartDate=" + periodStartDate + "periodEndDate=" + periodEndDate + "periodDays=" + periodDays + "termsOfUse=" + termsOfUse+ "useStorePlace=" + useStorePlace+ "useStoreAddressId=" + useStoreAddressId
				 + "useStoreUrl=" + useStoreUrl + "refundPolicy=" + refundPolicy + "customerServicePhoneNumber=" + customerServicePhoneNumber + "]";
	}
		
}
