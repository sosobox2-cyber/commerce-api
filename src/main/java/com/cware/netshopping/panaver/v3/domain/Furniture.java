package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Furniture {
		
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
	// KC 인증정보
	private String certificationType;
	// 색상
	private String color;
	// 구성품
	private String components;
	// 주요 소재
	private String material;
	// 제조자(사)
	private String manufacturer;
	// 수입자
	private String importer;
	// 제조국
	private String producer;
	// 크기
	private String size;
	// 배송 설치 비용
	private String installedCharge;
	// 품질 보증 기준
	private String warrantyPolicy;
	// 재공급 사유 및 하자
	private String refurb;
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

	public String getCertificationType() {
		return certificationType;
	}

	public void setCertificationType(String certificationType) {
		this.certificationType = certificationType;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getComponents() {
		return components;
	}

	public void setComponents(String components) {
		this.components = components;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getInstalledCharge() {
		return installedCharge;
	}

	public void setInstalledCharge(String installedCharge) {
		this.installedCharge = installedCharge;
	}

	public String getWarrantyPolicy() {
		return warrantyPolicy;
	}

	public void setWarrantyPolicy(String warrantyPolicy) {
		this.warrantyPolicy = warrantyPolicy;
	}

	public String getRefurb() {
		return refurb;
	}

	public void setRefurb(String refurb) {
		this.refurb = refurb;
	}

	public String getAfterServiceDirector() {
		return afterServiceDirector;
	}

	public void setAfterServiceDirector(String afterServiceDirector) {
		this.afterServiceDirector = afterServiceDirector;
	}

	@Override
	public String toString() {
		return "Furniture [returnCostReason=" + returnCostReason + "noRefundReason=" + noRefundReason + "qualityAssuranceStandard=" + qualityAssuranceStandard + "compensationProcedure=" + compensationProcedure + "troubleShootingContents=" + troubleShootingContents 
				 + "itemName=" + itemName + "certificationType=" + certificationType + "color=" + color + "components=" + components + "material=" + material + "manufacturer=" + manufacturer + "importer=" + importer
				 + "producer=" + producer + "size=" + size + "installedCharge=" + installedCharge + "warrantyPolicy=" + warrantyPolicy + "refurb=" + refurb + "afterServiceDirector=" + afterServiceDirector + "]";
	}
		
}
