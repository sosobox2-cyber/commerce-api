package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class RentalEtc {
		
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
	// 품명
	private String itemName;
	// 모델명
	private String modelName;
	// 소유권 이전 조건
	private String ownershipTransferCondition;
	// 상품의 고장, 분실, 훼손 시 소비자 책임
	private String payingForLossOrDamage;
	// 중도 해약 시 환불 기준
	private String refundPolicyForCancel;
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getOwnershipTransferCondition() {
		return ownershipTransferCondition;
	}

	public void setOwnershipTransferCondition(String ownershipTransferCondition) {
		this.ownershipTransferCondition = ownershipTransferCondition;
	}

	public String getPayingForLossOrDamage() {
		return payingForLossOrDamage;
	}

	public void setPayingForLossOrDamage(String payingForLossOrDamage) {
		this.payingForLossOrDamage = payingForLossOrDamage;
	}

	public String getRefundPolicyForCancel() {
		return refundPolicyForCancel;
	}

	public void setRefundPolicyForCancel(String refundPolicyForCancel) {
		this.refundPolicyForCancel = refundPolicyForCancel;
	}

	public String getCustomerServicePhoneNumber() {
		return customerServicePhoneNumber;
	}

	public void setCustomerServicePhoneNumber(String customerServicePhoneNumber) {
		this.customerServicePhoneNumber = customerServicePhoneNumber;
	}

	@Override
	public String toString() {
		return "RentalEtc [returnCostReason=" + returnCostReason + "noRefundReason=" + noRefundReason + "qualityAssuranceStandard=" + qualityAssuranceStandard + "compensationProcedure=" + compensationProcedure + "troubleShootingContents=" + troubleShootingContents 
				 + "itemName=" + itemName + "modelName=" + modelName + "ownershipTransferCondition=" + ownershipTransferCondition + "payingForLossOrDamage=" + payingForLossOrDamage + "refundPolicyForCancel=" + refundPolicyForCancel+ "components=" + customerServicePhoneNumber+"customerServicePhoneNumber=" + "]";
	}
		
}
