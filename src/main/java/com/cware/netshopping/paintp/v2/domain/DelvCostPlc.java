package com.cware.netshopping.paintp.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 배송비정책
 * 
 * http://www.interpark.com/openapi/site/APIDelvCostPolicyInsertSpec.jsp
 * http://www.interpark.com/openapi/site/APIDelvCostPolicyUpdateSpec.jsp
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DelvCostPlc {

	// 묶음배송비번호(수정시 필수)
	private String delvPlcNo;

	// 묶음배송비번호(응답)
	private String delvCostPlcNo;

	// 배송비 종류(필수) (00:무료, 98:판매자 조건부 무료, 99:판매자 정액)
	private String distCostTp;

	// 결제방법(필수) (01:착불, 02:선불(배송비 종류가 무료일 경우에도 선택), 03:선/착불)
	private String distCostCd;

	// 일부지역 유료여부(Y:예, N:아니오(배송비 종류가 무료일 때만 입력))
	private String localCostYn;

	// 배송비(필수) (배송비 종류가 무료일 경우 0)
	private Long distCost;

	// 무료배송 최소금액 (배송비 종류가 조건부 무료일 때만)
	private Long maxbuyAmt;

	public String getDelvCostPlcNo() {
		return delvCostPlcNo;
	}

	public void setDelvCostPlcNo(String delvCostPlcNo) {
		this.delvCostPlcNo = delvCostPlcNo;
	}

	public String getDistCostTp() {
		return distCostTp;
	}

	public void setDistCostTp(String distCostTp) {
		this.distCostTp = distCostTp;
	}

	public String getDistCostCd() {
		return distCostCd;
	}

	public void setDistCostCd(String distCostCd) {
		this.distCostCd = distCostCd;
	}

	public String getLocalCostYn() {
		return localCostYn;
	}

	public void setLocalCostYn(String localCostYn) {
		this.localCostYn = localCostYn;
	}

	public Long getDistCost() {
		return distCost;
	}

	public void setDistCost(Long distCost) {
		this.distCost = distCost;
	}

	public Long getMaxbuyAmt() {
		return maxbuyAmt;
	}

	public void setMaxbuyAmt(Long maxbuyAmt) {
		this.maxbuyAmt = maxbuyAmt;
	}

	public String getDelvPlcNo() {
		return delvPlcNo;
	}

	public void setDelvPlcNo(String delvPlcNo) {
		this.delvPlcNo = delvPlcNo;
	}

	@Override
	public String toString() {
		return "DelvCostPlc [delvPlcNo=" + delvPlcNo + ", delvCostPlcNo=" + delvCostPlcNo + ", distCostTp=" + distCostTp
				+ ", distCostCd=" + distCostCd + ", localCostYn=" + localCostYn + ", distCost=" + distCost
				+ ", maxbuyAmt=" + maxbuyAmt + "]";
	}

}
