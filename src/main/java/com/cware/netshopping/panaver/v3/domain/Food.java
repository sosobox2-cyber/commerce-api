package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Food {
		
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
	// 품목 또는 명칭
	private String foodItem;
	// 포장 단위별 용량(중량)
	private String weight;
	// 포장 단위별 수량
	private String amount;
	// 포장 단위별 크기
	private String size;
	// 제조연월일
	private String packDate;
	// 제조연월일 직접 입력
	private String packDateText;
	// 소비기한 또는 품질유지기한
	private String consumptionDate;
	// 소비기한 또는 품질유지기한 직접 입력
	private String consumptionDateText;
	// 생산자
	private String producer;
	// 세부 품목군별 표시사항
	private String relevantLawContent;
	// 상품 구성
	private String productComposition;	
	// 보관 방법 또는 취급 방법
	private String keep;
	// 소비자 안전을 위한 주의사항
	private String adCaution;
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

	public String getFoodItem() {
		return foodItem;
	}

	public void setFoodItem(String foodItem) {
		this.foodItem = foodItem;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getPackDate() {
		return packDate;
	}

	public void setPackDate(String packDate) {
		this.packDate = packDate;
	}

	public String getPackDateText() {
		return packDateText;
	}

	public void setPackDateText(String packDateText) {
		this.packDateText = packDateText;
	}

	public String getConsumptionDate() {
		return consumptionDate;
	}

	public void setConsumptionDate(String consumptionDate) {
		this.consumptionDate = consumptionDate;
	}

	public String getConsumptionDateText() {
		return consumptionDateText;
	}

	public void setConsumptionDateText(String consumptionDateText) {
		this.consumptionDateText = consumptionDateText;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getRelevantLawContent() {
		return relevantLawContent;
	}

	public void setRelevantLawContent(String relevantLawContent) {
		this.relevantLawContent = relevantLawContent;
	}

	public String getProductComposition() {
		return productComposition;
	}

	public void setProductComposition(String productComposition) {
		this.productComposition = productComposition;
	}

	public String getKeep() {
		return keep;
	}

	public void setKeep(String keep) {
		this.keep = keep;
	}

	public String getAdCaution() {
		return adCaution;
	}

	public void setAdCaution(String adCaution) {
		this.adCaution = adCaution;
	}

	public String getCustomerServicePhoneNumber() {
		return customerServicePhoneNumber;
	}

	public void setCustomerServicePhoneNumber(String customerServicePhoneNumber) {
		this.customerServicePhoneNumber = customerServicePhoneNumber;
	}

	@Override
	public String toString() {
		return "Food [returnCostReason=" + returnCostReason + "noRefundReason=" + noRefundReason + "qualityAssuranceStandard=" + qualityAssuranceStandard + "compensationProcedure=" + compensationProcedure + "troubleShootingContents=" + troubleShootingContents 
				 + "foodItem=" + foodItem + "weight=" + weight + "amount=" + amount +"size=" + size + "packDate=" + packDate + "packDateText=" + packDateText + "consumptionDate=" + consumptionDate + "consumptionDateText=" + consumptionDateText+ "producer=" + producer 
				 + "relevantLawContent=" + relevantLawContent +"productComposition=" + productComposition +"keep=" + keep +"adCaution=" + adCaution +"customerServicePhoneNumber=" + customerServicePhoneNumber +"]";
	}
		
}
