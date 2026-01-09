package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Biocidal {
		
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
	// 살생물제품유형
	private String productName;
	// 중량 또는 용량 및 표준 사용량
	private String weight;	
	// 효능ㆍ효과
	private String effect;
	// 사용 대상자 및 사용 범위
	private String rangeOfUse;
	// 수입자
	private String importer;
	// 제조국
	private String producer;
	// 제조자(사))
	private String manufacturer;	
	// 어린이보호포장 대상 제품 여부
	private String childProtection;
	// 살생물물질, 나노물질, 유해화학물질(또는 중점관리물질)의 명칭
	private String harmfulChemicalSubstance;	
	// 제품 유해성ㆍ위해성 표시
	private String maleficence;
	// 사용 방법 및 사용상 주의사항
	private String caution;	
	// 승인번호
	private String approvalNumber;
	// 소비자상담 전화번호
	private String customerServicePhoneNumber;
	// 유통기한
	private String expirationDate;
	// 유통기한 직접 입력
	private String expirationDateText;
		
	
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public String getRangeOfUse() {
		return rangeOfUse;
	}

	public void setRangeOfUse(String rangeOfUse) {
		this.rangeOfUse = rangeOfUse;
	}

	public String getImporter() {
		return importer;
	}

	public void setImporter(String importer) {
		this.importer = importer;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getChildProtection() {
		return childProtection;
	}

	public void setChildProtection(String childProtection) {
		this.childProtection = childProtection;
	}

	public String getHarmfulChemicalSubstance() {
		return harmfulChemicalSubstance;
	}

	public void setHarmfulChemicalSubstance(String harmfulChemicalSubstance) {
		this.harmfulChemicalSubstance = harmfulChemicalSubstance;
	}

	public String getMaleficence() {
		return maleficence;
	}

	public void setMaleficence(String maleficence) {
		this.maleficence = maleficence;
	}

	public String getCaution() {
		return caution;
	}

	public void setCaution(String caution) {
		this.caution = caution;
	}

	public String getApprovalNumber() {
		return approvalNumber;
	}

	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}

	public String getCustomerServicePhoneNumber() {
		return customerServicePhoneNumber;
	}

	public void setCustomerServicePhoneNumber(String customerServicePhoneNumber) {
		this.customerServicePhoneNumber = customerServicePhoneNumber;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getExpirationDateText() {
		return expirationDateText;
	}

	public void setExpirationDateText(String expirationDateText) {
		this.expirationDateText = expirationDateText;
	}

	@Override
	public String toString() {
		return "Biocidal [returnCostReason=" + returnCostReason + "noRefundReason=" + noRefundReason + "qualityAssuranceStandard=" + qualityAssuranceStandard + "compensationProcedure=" + compensationProcedure + "troubleShootingContents=" + troubleShootingContents 
				 + "productName=" + productName + "weight=" + weight + "rangeOfUse=" + rangeOfUse + "importer=" + importer + "producer=" + producer + "manufacturer=" + manufacturer + "childProtection=" + childProtection + "harmfulChemicalSubstance=" + harmfulChemicalSubstance
				 + "maleficence=" + maleficence + "caution=" + caution + "approvalNumber=" + approvalNumber + "customerServicePhoneNumber=" + customerServicePhoneNumber + "expirationDate=" + expirationDate + "expirationDateText=" + expirationDateText +"]";
	}
		
}
