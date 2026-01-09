package com.cware.netshopping.patmon.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DealDeliveryInfo {
	String deliveryCorp; // 배송 택배사
	Boolean parallelImport; // 병행수입여부
	String importDeclarationCertificate; // 병행수입신고필증URL
	Boolean useCustomsIdNo; // 주문시 개인통관고유번호 사용여부
	String productType; // 상품 타입
	Boolean distanceFeeGradeUsing; // 지역별 차등배송 사용여부
	String distanceFeeGradeContents; // 지역별 차등배송 사유
	Boolean extraInstallationCostUsing; // 별도설치비 사용여부
	Boolean simpleRefundAble; // 단순변심환불 가능여부
	String simpleRefundNotAvailableReason; // 단순변심환불 불가능시 사유

	public String getDeliveryCorp() {
		return deliveryCorp;
	}

	public void setDeliveryCorp(String deliveryCorp) {
		this.deliveryCorp = deliveryCorp;
	}

	public Boolean getParallelImport() {
		return parallelImport;
	}

	public void setParallelImport(Boolean parallelImport) {
		this.parallelImport = parallelImport;
	}

	public String getImportDeclarationCertificate() {
		return importDeclarationCertificate;
	}

	public void setImportDeclarationCertificate(String importDeclarationCertificate) {
		this.importDeclarationCertificate = importDeclarationCertificate;
	}

	public Boolean getUseCustomsIdNo() {
		return useCustomsIdNo;
	}

	public void setUseCustomsIdNo(Boolean useCustomsIdNo) {
		this.useCustomsIdNo = useCustomsIdNo;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Boolean getDistanceFeeGradeUsing() {
		return distanceFeeGradeUsing;
	}

	public void setDistanceFeeGradeUsing(Boolean distanceFeeGradeUsing) {
		this.distanceFeeGradeUsing = distanceFeeGradeUsing;
	}

	public String getDistanceFeeGradeContents() {
		return distanceFeeGradeContents;
	}

	public void setDistanceFeeGradeContents(String distanceFeeGradeContents) {
		this.distanceFeeGradeContents = distanceFeeGradeContents;
	}

	public Boolean getExtraInstallationCostUsing() {
		return extraInstallationCostUsing;
	}

	public void setExtraInstallationCostUsing(Boolean extraInstallationCostUsing) {
		this.extraInstallationCostUsing = extraInstallationCostUsing;
	}

	public Boolean getSimpleRefundAble() {
		return simpleRefundAble;
	}

	public void setSimpleRefundAble(Boolean simpleRefundAble) {
		this.simpleRefundAble = simpleRefundAble;
	}

	public String getSimpleRefundNotAvailableReason() {
		return simpleRefundNotAvailableReason;
	}

	public void setSimpleRefundNotAvailableReason(String simpleRefundNotAvailableReason) {
		this.simpleRefundNotAvailableReason = simpleRefundNotAvailableReason;
	}

	@Override
	public String toString() {
		return "DealDeliveryInfo [deliveryCorp=" + deliveryCorp + ", parallelImport=" + parallelImport
				+ ", importDeclarationCertificate=" + importDeclarationCertificate + ", useCustomsIdNo="
				+ useCustomsIdNo + ", productType=" + productType + ", distanceFeeGradeUsing=" + distanceFeeGradeUsing
				+ ", distanceFeeGradeContents=" + distanceFeeGradeContents + ", extraInstallationCostUsing="
				+ extraInstallationCostUsing + ", simpleRefundAble=" + simpleRefundAble
				+ ", simpleRefundNotAvailableReason=" + simpleRefundNotAvailableReason + "]";
	}

}
