package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class EtcService {
		
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
	// 서비스 제공 사업자
	private String serviceProvider;
	// 법에 의한 인증ㆍ허가 등을 받았음을 확인할 수 있는 경우 그에 대한 사항
	private String certificateDetails;
	// 이용 조건
	private String usableCondition;
	// 취소ㆍ중도해약ㆍ해지 조건 및 환불 기준
	private String cancelationStandard;
	// 취소ㆍ환불 방법
	private String cancelationPolicy;
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

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public String getCertificateDetails() {
		return certificateDetails;
	}

	public void setCertificateDetails(String certificateDetails) {
		this.certificateDetails = certificateDetails;
	}

	public String getUsableCondition() {
		return usableCondition;
	}

	public void setUsableCondition(String usableCondition) {
		this.usableCondition = usableCondition;
	}

	public String getCancelationStandard() {
		return cancelationStandard;
	}

	public void setCancelationStandard(String cancelationStandard) {
		this.cancelationStandard = cancelationStandard;
	}

	public String getCancelationPolicy() {
		return cancelationPolicy;
	}

	public void setCancelationPolicy(String cancelationPolicy) {
		this.cancelationPolicy = cancelationPolicy;
	}

	public String getCustomerServicePhoneNumber() {
		return customerServicePhoneNumber;
	}

	public void setCustomerServicePhoneNumber(String customerServicePhoneNumber) {
		this.customerServicePhoneNumber = customerServicePhoneNumber;
	}

	@Override
	public String toString() {
		return "EtcService [returnCostReason=" + returnCostReason + "noRefundReason=" + noRefundReason + "qualityAssuranceStandard=" + qualityAssuranceStandard + "compensationProcedure=" + compensationProcedure + "troubleShootingContents=" + troubleShootingContents 
				 + "serviceProvider=" + serviceProvider + "certificateDetails=" + certificateDetails + "usableCondition=" + usableCondition + "cancelationStandard=" + cancelationStandard + "cancelationPolicy=" + cancelationPolicy + "customerServicePhoneNumber=" + customerServicePhoneNumber +"]";
	}
		
}
