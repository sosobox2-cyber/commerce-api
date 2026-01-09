package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemShppCritn {
	// 배송 주체 코드 (commCd:P017)
	// 31 : 자사창고
	// 32 : 업체창고
	// 41 : 협력업체
	String shppMainCd;

	// 배송 방법 코드 (commCd:P021)
	// 10 : 자사배송
	// 20 : 택배배송
	// 30 : 매장방문
	// 40 : 등기
	// 50 : 미배송
	// 60 : 미발송
	// 90 : 특수배송
	String shppMthdCd;
	String jejuShppDisabYn; // 제주도 배송불가 여부
	String ismtarShppDisabYn; // 도서산간 배송불가 여부
	String whoutAddrId; // 출고 주소 ID
	String snbkAddrId; // 반품 주소 ID
	String whoutShppcstId; // 출고 배송비 ID
	String retShppcstId; // 반품 배송비 ID
	String ismtarAddShppcstId; // 도서산간 추가배송비 ID
	String jejuAddShppcstId; // 제주도 추가배송비 ID
	String mareaShppYn; // 수도권 배송여부
	String prmShppcstId; // 프리미엄배송 고정단가표 ID

	public String getShppMainCd() {
		return shppMainCd;
	}

	public void setShppMainCd(String shppMainCd) {
		this.shppMainCd = shppMainCd;
	}

	public String getShppMthdCd() {
		return shppMthdCd;
	}

	public void setShppMthdCd(String shppMthdCd) {
		this.shppMthdCd = shppMthdCd;
	}

	public String getJejuShppDisabYn() {
		return jejuShppDisabYn;
	}

	public void setJejuShppDisabYn(String jejuShppDisabYn) {
		this.jejuShppDisabYn = jejuShppDisabYn;
	}

	public String getIsmtarShppDisabYn() {
		return ismtarShppDisabYn;
	}

	public void setIsmtarShppDisabYn(String ismtarShppDisabYn) {
		this.ismtarShppDisabYn = ismtarShppDisabYn;
	}

	public String getWhoutAddrId() {
		return whoutAddrId;
	}

	public void setWhoutAddrId(String whoutAddrId) {
		this.whoutAddrId = whoutAddrId;
	}

	public String getSnbkAddrId() {
		return snbkAddrId;
	}

	public void setSnbkAddrId(String snbkAddrId) {
		this.snbkAddrId = snbkAddrId;
	}

	public String getWhoutShppcstId() {
		return whoutShppcstId;
	}

	public void setWhoutShppcstId(String whoutShppcstId) {
		this.whoutShppcstId = whoutShppcstId;
	}

	public String getRetShppcstId() {
		return retShppcstId;
	}

	public void setRetShppcstId(String retShppcstId) {
		this.retShppcstId = retShppcstId;
	}

	public String getIsmtarAddShppcstId() {
		return ismtarAddShppcstId;
	}

	public void setIsmtarAddShppcstId(String ismtarAddShppcstId) {
		this.ismtarAddShppcstId = ismtarAddShppcstId;
	}

	public String getJejuAddShppcstId() {
		return jejuAddShppcstId;
	}

	public void setJejuAddShppcstId(String jejuAddShppcstId) {
		this.jejuAddShppcstId = jejuAddShppcstId;
	}

	public String getMareaShppYn() {
		return mareaShppYn;
	}

	public void setMareaShppYn(String mareaShppYn) {
		this.mareaShppYn = mareaShppYn;
	}

	public String getPrmShppcstId() {
		return prmShppcstId;
	}

	public void setPrmShppcstId(String prmShppcstId) {
		this.prmShppcstId = prmShppcstId;
	}

	@Override
	public String toString() {
		return "ItemShppCritn [shppMainCd=" + shppMainCd + ", shppMthdCd=" + shppMthdCd + ", jejuShppDisabYn="
				+ jejuShppDisabYn + ", ismtarShppDisabYn=" + ismtarShppDisabYn + ", whoutAddrId=" + whoutAddrId
				+ ", snbkAddrId=" + snbkAddrId + ", whoutShppcstId=" + whoutShppcstId + ", retShppcstId=" + retShppcstId
				+ ", ismtarAddShppcstId=" + ismtarAddShppcstId + ", jejuAddShppcstId=" + jejuAddShppcstId
				+ ", mareaShppYn=" + mareaShppYn + ", prmShppcstId=" + prmShppcstId + "]";
	}

}
