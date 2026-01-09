package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaEntpSlip extends AbstractModel {
	private static final long serialVersionUID = 1L; 
	
	private String entpCode;
	private String entpManSeq;
	private String paCode;
	private String paAddrSeq; 
	private String paAddrGb;
	private String paBundleNo;
	private String transTargetYn;
	private String lastSyncDate;
	
	//출하지 수정 500에러 용
	private String newGmktShipNo;
	private String oldGmktShipNo;
	private String shipCostCode;
	private String noShipIsland;
	private String installYn;
	
	private String postNo;
	private String address1;
	private String address2;

	private String entpManDdd;
	private String entpManTel1;
	private String entpManTel2;
	private String entpManHp1;
	private String entpManHp2;
	private String entpManHp3;
	
	public String getEntpCode() {
		return entpCode;
	}
	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}
	public String getEntpManSeq() {
		return entpManSeq;
	}
	public void setEntpManSeq(String entpManSeq) {
		this.entpManSeq = entpManSeq;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaAddrSeq() {
		return paAddrSeq;
	}
	public void setPaAddrSeq(String paAddrSeq) {
		this.paAddrSeq = paAddrSeq;
	}
	public String getPaAddrGb() {
		return paAddrGb;
	}
	public void setPaAddrGb(String paAddrGb) {
		this.paAddrGb = paAddrGb;
	}
	public String getPaBundleNo() {
		return paBundleNo;
	}
	public void setPaBundleNo(String paBundleNo) {
		this.paBundleNo = paBundleNo;
	}
	public String getTransTargetYn() {
		return transTargetYn;
	}
	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}
	public String getLastSyncDate() {
		return lastSyncDate;
	}
	public void setLastSyncDate(String lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}
	public String getNewGmktShipNo() {
		return newGmktShipNo;
	}
	public void setNewGmktShipNo(String newGmktShipNo) {
		this.newGmktShipNo = newGmktShipNo;
	}
	public String getOldGmktShipNo() {
		return oldGmktShipNo;
	}
	public void setOldGmktShipNo(String oldGmktShipNo) {
		this.oldGmktShipNo = oldGmktShipNo;
	}
	public String getShipCostCode() {
		return shipCostCode;
	}
	public void setShipCostCode(String shipCostCode) {
		this.shipCostCode = shipCostCode;
	}
	public String getNoShipIsland() {
		return noShipIsland;
	}
	public void setNoShipIsland(String noShipIsland) {
		this.noShipIsland = noShipIsland;
	}
	public String getInstallYn() {
		return installYn;
	}
	public void setInstallYn(String installYn) {
		this.installYn = installYn;
	}
	public String getPostNo() {
		return postNo;
	}
	public void setPostNo(String postNo) {
		this.postNo = postNo;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getEntpManDdd() {
		return entpManDdd;
	}
	public void setEntpManDdd(String entpManDdd) {
		this.entpManDdd = entpManDdd;
	}
	public String getEntpManTel1() {
		return entpManTel1;
	}
	public void setEntpManTel1(String entpManTel1) {
		this.entpManTel1 = entpManTel1;
	}
	public String getEntpManTel2() {
		return entpManTel2;
	}
	public void setEntpManTel2(String entpManTel2) {
		this.entpManTel2 = entpManTel2;
	}
	public String getEntpManHp1() {
		return entpManHp1;
	}
	public void setEntpManHp1(String entpManHp1) {
		this.entpManHp1 = entpManHp1;
	}
	public String getEntpManHp2() {
		return entpManHp2;
	}
	public void setEntpManHp2(String entpManHp2) {
		this.entpManHp2 = entpManHp2;
	}
	public String getEntpManHp3() {
		return entpManHp3;
	}
	public void setEntpManHp3(String entpManHp3) {
		this.entpManHp3 = entpManHp3;
	}
}