package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class OfficeAppliances {
		
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
	// KC 인증정보
	private String certificationType;
	// 정격전압
	private String ratedVoltage;
	// 소비전력
	private String powerConsumption;
	// 에너지소비효율등급
	private String energyEfficiencyRating;
	// 동일 모델의 출시연월
	private String releaseDate;
	// 동일 모델 출시연월일 직접 입력
	private String releaseDateText;
	// 제조자(사)
	private String manufacturer;
	// 크기
	private String size;
	// 무게
	private String weight;	
	// 주요 사양
	private String specification;
	// 품질 보증 기준
	private String warrantyPolicy;
	// A/S 책임자와 전화번호
	private String afterServiceDirector;

	
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

	public String getCertificationType() {
		return certificationType;
	}

	public void setCertificationType(String certificationType) {
		this.certificationType = certificationType;
	}

	public String getRatedVoltage() {
		return ratedVoltage;
	}

	public void setRatedVoltage(String ratedVoltage) {
		this.ratedVoltage = ratedVoltage;
	}

	public String getPowerConsumption() {
		return powerConsumption;
	}

	public void setPowerConsumption(String powerConsumption) {
		this.powerConsumption = powerConsumption;
	}

	public String getEnergyEfficiencyRating() {
		return energyEfficiencyRating;
	}

	public void setEnergyEfficiencyRating(String energyEfficiencyRating) {
		this.energyEfficiencyRating = energyEfficiencyRating;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getReleaseDateText() {
		return releaseDateText;
	}

	public void setReleaseDateText(String releaseDateText) {
		this.releaseDateText = releaseDateText;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getWarrantyPolicy() {
		return warrantyPolicy;
	}

	public void setWarrantyPolicy(String warrantyPolicy) {
		this.warrantyPolicy = warrantyPolicy;
	}

	public String getAfterServiceDirector() {
		return afterServiceDirector;
	}

	public void setAfterServiceDirector(String afterServiceDirector) {
		this.afterServiceDirector = afterServiceDirector;
	}

	@Override
	public String toString() {
		return "OfficeAppliances [returnCostReason=" + returnCostReason + "noRefundReason=" + noRefundReason + "qualityAssuranceStandard=" + qualityAssuranceStandard + "compensationProcedure=" + compensationProcedure + "troubleShootingContents=" + troubleShootingContents 
				 + "itemName=" + itemName + "modelName=" + modelName + "certificationType=" + certificationType + "ratedVoltage=" + ratedVoltage + "powerConsumption=" + powerConsumption + "energyEfficiencyRating=" + energyEfficiencyRating + "releaseDate=" + releaseDate
				 + "releaseDateText=" + releaseDateText + "manufacturer=" + manufacturer + "size=" + size + "weight=" + weight+ "specification=" + specification + "warrantyPolicy=" + warrantyPolicy + "afterServiceDirector=" + afterServiceDirector +"]";
	}
		
}
