package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Orderreceipts;

/**
 *
 * @author Commerceware
 *
 */
public class OrderreceiptsVO extends Orderreceipts implements Comparable<Object> {

	private static final long serialVersionUID = 1L;

	private Orderreceipts od = null;
    public int compareTo(Object o){
        int  rtn = 0;
    	od = (Orderreceipts)o;
    	rtn = (this.getSettleGb()).compareTo(od.getSettleGb());
    	return rtn;
    }

	private String depositamtGb;
	private String familyGb;
	private String doFlagOrg;
	private String saveamtGb;
	private String tel;
	private String vaccountYn;
	private String depoResiNo;
	private String certiYn;
	private String repayNote;
	private String receiver;
	private Long cmsno;
	private boolean largeCms = false;
	private double partQuestAmt;
	private String spointCardPassword;
	private String staffCardYn;
	private String ssgCardCode;
	private String niceBankCode;
	
	public double getPartQuestAmt() {
		return partQuestAmt;
	}
	public void setPartQuestAmt(double partQuestAmt) {
		this.partQuestAmt = partQuestAmt;
	}

	public Long getCmsno() {
		return cmsno;
	}
	public void setCmsno(Long cmsno) {
		this.cmsno = cmsno;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getDepositamtGb() {
		return depositamtGb;
	}
	public void setDepositamtGb(String depositamtGb) {
		this.depositamtGb = depositamtGb;
	}
	public String getFamilyGb() {
		return familyGb;
	}
	public void setFamilyGb(String familyGb) {
		this.familyGb = familyGb;
	}
	public String getDoFlagOrg() {
		return doFlagOrg;
	}
	public void setDoFlagOrg(String doFlagOrg) {
		this.doFlagOrg = doFlagOrg;
	}
	public String getSaveamtGb() {
		return saveamtGb;
	}
	public void setSaveamtGb(String saveamtGb) {
		this.saveamtGb = saveamtGb;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getVaccountYn() {
		return vaccountYn;
	}
	public void setVaccountYn(String vaccountYn) {
		this.vaccountYn = vaccountYn;
	}
	public String getDepoResiNo() {
		return depoResiNo;
	}
	public void setDepoResiNo(String depoResiNo) {
		this.depoResiNo = depoResiNo;
	}
	public String getCertiYn() {
		return certiYn;
	}
	public void setCertiYn(String certiYn) {
		this.certiYn = certiYn;
	}
	public String getRepayNote() {
		return repayNote;
	}
	public void setRepayNote(String repayNote) {
		this.repayNote = repayNote;
	}
	public boolean isLargeCms() {
		return largeCms;
	}
	public void setLargeCms(boolean largeCms) {
		this.largeCms = largeCms;
	}
	public String getSpointCardPassword() {
		return spointCardPassword;
	}
	public void setSpointCardPassword(String spointCardPassword) {
		this.spointCardPassword = spointCardPassword;
	}
	public String getStaffCardYn() {
		return staffCardYn;
	}
	public void setStaffCardYn(String staffCardYn) {
		this.staffCardYn = staffCardYn;
	}
	public String getSsgCardCode() {
		return ssgCardCode;
	}
	public void setSsgCardCode(String ssgCardCode) {
		this.ssgCardCode = ssgCardCode;
	}
	public String getNiceBankCode() {
		return niceBankCode;
	}
	public void setNiceBankCode(String niceBankCode) {
		this.niceBankCode = niceBankCode;
	}
	
}