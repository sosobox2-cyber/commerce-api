package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Orderreceipts extends AbstractModel implements Cloneable{
	private static final long serialVersionUID = 1L;

	private String receiptNo;
	private String orderNo;
	private String custNo;
	private String doFlag;
	private String settleGb;
	private String cardBankCode;
	private String bankSeq;
	private String cardName;
	private String cardNo;
	private String cvv;
	private String depositor;
	private String validDate;
	private double questAmt;
	private Timestamp receiptPlanDate;
	private String okNo;
	private Timestamp okDate;
	private String okMed;
	private String okErrorCode;
	private String vanComp;
	private long payMonth;
	private String norestYn;
	private double norestRate;
	private double norestAmt;
	private double receiptAmt;
	private Timestamp receiptDate;
	private String endYn;
	private double repayPbAmt;
	private double repayPbAmtOrg;
	private String cancelYn;
	private String cancelCode;
	private Timestamp cancelDate;
	private String cancelId;
	private String saveamtUseFlag;
	private String codDelyYn;
	private String divideYn;
	private String remark1V;
	private String remark2V;
	private double remark3N;
	private long remark4N;
	private Timestamp remark5D;
	private Timestamp remark6D;
	private String remark;
	private String protxVendortxcode;
	private String protxStatus;
	private String protxStatusdetail;
	private String protxVpstxid;
	private String protxSecuritykey;
	private String protxTxauthno;
	private String issueNumber;
	private String payboxIdentifiant;
	private String payboxCodetraitement;
	private String payboxPays;
	private String payboxDateq;
	private String payboxReference;
	private String cardPassword;
	private Timestamp procDate;
	private String procId;
	private String partialCancelYn;
	
	public Object clone() {
		try {
			return super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	public double getRepayPbAmtOrg() {
		return repayPbAmtOrg;
	}

	public void setRepayPbAmtOrg(double repayPbAmtOrg) {
		this.repayPbAmtOrg = repayPbAmtOrg;
	}

	public String getReceiptNo() {
		return this.receiptNo;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public String getCustNo() {
		return this.custNo;
	}

	public String getDoFlag() {
		return this.doFlag;
	}

	public String getSettleGb() {
		return this.settleGb;
	}

	public String getCardBankCode() {
		return this.cardBankCode;
	}

	public String getBankSeq() {
		return this.bankSeq;
	}

	public String getCardName() {
		return this.cardName;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public String getCvv() {
		return this.cvv;
	}

	public String getDepositor() {
		return this.depositor;
	}

	public String getValidDate() {
		return this.validDate;
	}

	public double getQuestAmt() {
		return this.questAmt;
	}

	public Timestamp getReceiptPlanDate() {
		return this.receiptPlanDate;
	}

	public String getOkNo() {
		return this.okNo;
	}

	public Timestamp getOkDate() {
		return this.okDate;
	}

	public String getOkMed() {
		return this.okMed;
	}

	public String getOkErrorCode() {
		return this.okErrorCode;
	}

	public String getVanComp() {
		return this.vanComp;
	}
	
	public long getPayMonth() {
		return this.payMonth;
	}

	public String getNorestYn() {
		return this.norestYn;
	}

	public double getNorestRate() {
		return this.norestRate;
	}

	public double getNorestAmt() {
		return this.norestAmt;
	}

	public double getReceiptAmt() {
		return this.receiptAmt;
	}

	public Timestamp getReceiptDate() {
		return this.receiptDate;
	}

	public String getEndYn() {
		return this.endYn;
	}

	public double getRepayPbAmt() {
		return this.repayPbAmt;
	}

	public String getCancelYn() {
		return this.cancelYn;
	}

	public String getCancelCode() {
		return this.cancelCode;
	}

	public Timestamp getCancelDate() {
		return this.cancelDate;
	}

	public String getCancelId() {
		return this.cancelId;
	}

	public String getSaveamtUseFlag() {
		return this.saveamtUseFlag;
	}

	public String getCodDelyYn() {
		return this.codDelyYn;
	}

	public String getDivideYn() {
		return this.divideYn;
	}

	public String getRemark1V() {
		return this.remark1V;
	}

	public String getRemark2V() {
		return this.remark2V;
	}

	public double getRemark3N() {
		return this.remark3N;
	}

	public long getRemark4N() {
		return this.remark4N;
	}

	public Timestamp getRemark5D() {
		return this.remark5D;
	}

	public Timestamp getRemark6D() {
		return this.remark6D;
	}

	public String getRemark() {
		return this.remark;
	}

	public String getProtxVendortxcode() {
		return this.protxVendortxcode;
	}

	public String getProtxStatus() {
		return this.protxStatus;
	}

	public String getProtxStatusdetail() {
		return this.protxStatusdetail;
	}

	public String getProtxVpstxid() {
		return this.protxVpstxid;
	}

	public String getProtxSecuritykey() {
		return this.protxSecuritykey;
	}

	public String getProtxTxauthno() {
		return this.protxTxauthno;
	}

	public String getIssueNumber() {
		return this.issueNumber;
	}

	public String getPayboxIdentifiant() {
		return this.payboxIdentifiant;
	}

	public String getPayboxCodetraitement() {
		return this.payboxCodetraitement;
	}

	public String getPayboxPays() {
		return this.payboxPays;
	}

	public String getPayboxDateq() {
		return this.payboxDateq;
	}

	public String getPayboxReference() {
		return this.payboxReference;
	}

	public String getCardPassword() {
		return this.cardPassword;
	}

	public Timestamp getProcDate() {
		return this.procDate;
	}

	public String getProcId() {
		return this.procId;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public void setDoFlag(String doFlag) {
		this.doFlag = doFlag;
	}

	public void setSettleGb(String settleGb) {
		this.settleGb = settleGb;
	}

	public void setCardBankCode(String cardBankCode) {
		this.cardBankCode = cardBankCode;
	}

	public void setBankSeq(String bankSeq) {
		this.bankSeq = bankSeq;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public void setDepositor(String depositor) {
		this.depositor = depositor;
	}

	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}

	public void setQuestAmt(double questAmt) {
		this.questAmt = questAmt;
	}

	public void setReceiptPlanDate(Timestamp receiptPlanDate) {
		this.receiptPlanDate = receiptPlanDate;
	}

	public void setOkNo(String okNo) {
		this.okNo = okNo;
	}

	public void setOkDate(Timestamp okDate) {
		this.okDate = okDate;
	}

	public void setOkMed(String okMed) {
		this.okMed = okMed;
	}

	public void setOkErrorCode(String okErrorCode) {
		this.okErrorCode = okErrorCode;
	}

	public void setVanComp(String vanComp) {
		this.vanComp = vanComp;
	}

	public void setPayMonth(long payMonth) {
		this.payMonth = payMonth;
	}

	public void setNorestYn(String norestYn) {
		this.norestYn = norestYn;
	}

	public void setNorestRate(double norestRate) {
		this.norestRate = norestRate;
	}

	public void setNorestAmt(double norestAmt) {
		this.norestAmt = norestAmt;
	}

	public void setReceiptAmt(double receiptAmt) {
		this.receiptAmt = receiptAmt;
	}

	public void setReceiptDate(Timestamp receiptDate) {
		this.receiptDate = receiptDate;
	}

	public void setEndYn(String endYn) {
		this.endYn = endYn;
	}

	public void setRepayPbAmt(double repayPbAmt) {
		this.repayPbAmt = repayPbAmt;
	}

	public void setCancelYn(String cancelYn) {
		this.cancelYn = cancelYn;
	}

	public void setCancelCode(String cancelCode) {
		this.cancelCode = cancelCode;
	}

	public void setCancelDate(Timestamp cancelDate) {
		this.cancelDate = cancelDate;
	}

	public void setCancelId(String cancelId) {
		this.cancelId = cancelId;
	}

	public void setSaveamtUseFlag(String saveamtUseFlag) {
		this.saveamtUseFlag = saveamtUseFlag;
	}

	public void setCodDelyYn(String codDelyYn) {
		this.codDelyYn = codDelyYn;
	}

	public void setDivideYn(String divideYn) {
		this.divideYn = divideYn;
	}

	public void setRemark1V(String remark1V) {
		this.remark1V = remark1V;
	}

	public void setRemark2V(String remark2V) {
		this.remark2V = remark2V;
	}

	public void setRemark3N(double remark3N) {
		this.remark3N = remark3N;
	}

	public void setRemark4N(long remark4N) {
		this.remark4N = remark4N;
	}

	public void setRemark5D(Timestamp remark5D) {
		this.remark5D = remark5D;
	}

	public void setRemark6D(Timestamp remark6D) {
		this.remark6D = remark6D;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setProtxVendortxcode(String protxVendortxcode) {
		this.protxVendortxcode = protxVendortxcode;
	}

	public void setProtxStatus(String protxStatus) {
		this.protxStatus = protxStatus;
	}

	public void setProtxStatusdetail(String protxStatusdetail) {
		this.protxStatusdetail = protxStatusdetail;
	}

	public void setProtxVpstxid(String protxVpstxid) {
		this.protxVpstxid = protxVpstxid;
	}

	public void setProtxSecuritykey(String protxSecuritykey) {
		this.protxSecuritykey = protxSecuritykey;
	}

	public void setProtxTxauthno(String protxTxauthno) {
		this.protxTxauthno = protxTxauthno;
	}

	public void setIssueNumber(String issueNumber) {
		this.issueNumber = issueNumber;
	}

	public void setPayboxIdentifiant(String payboxIdentifiant) {
		this.payboxIdentifiant = payboxIdentifiant;
	}

	public void setPayboxCodetraitement(String payboxCodetraitement) {
		this.payboxCodetraitement = payboxCodetraitement;
	}

	public void setPayboxPays(String payboxPays) {
		this.payboxPays = payboxPays;
	}

	public void setPayboxDateq(String payboxDateq) {
		this.payboxDateq = payboxDateq;
	}

	public void setPayboxReference(String payboxReference) {
		this.payboxReference = payboxReference;
	}

	public void setCardPassword(String cardPassword) {
		this.cardPassword = cardPassword;
	}

	public void setProcDate(Timestamp procDate) {
		this.procDate = procDate;
	}

	public void setProcId(String procId) {
		this.procId = procId;
	}

	public String getPartialCancelYn() {
		return partialCancelYn;
	}

	public void setPartialCancelYn(String partialCancelYn) {
		this.partialCancelYn = partialCancelYn;
	}
}
