package com.cware.netshopping.domain.model;

import java.sql.Timestamp;
import java.util.List;

public class PaWempClaimList {
	private static final long serialVersionUID = 1L;
	
	private String paCode;
	private String paClaimNo;
	private String paOrderNo;
	private String paShipNo;
	private String paOrderGb;
	private String claimStatus;
	private Timestamp requestDate;
	private Timestamp approveDate;
	private Timestamp pendingDate;
	private Timestamp rejectDate;
	private String paClaimCode;
	private String claimReason;
	private String claimReasonDetail;
	private String pendingReason;
	private String pendingReasonDetail;
	private String rejectReason;
	private String rejectReasonDetail;
	private String claimWhoReason;
	private long claimFee;
	private String claimShipfeeEnclose;
	private String pickupStatus;
	private String pickupMethod;
	private Timestamp pickupScheduleDate;
	private String pickupDelyComp;
	private String pickupInvoiceNo;
	private String pickupName;
	private String pickupPhone;
	private String pickupZipcode;
	private String pickupBaseAddr;
	private String pickupDetailAddr;
	private String pickupMessage;
	private String deliveryStatus;
	private String deliveryMethod;
	private Timestamp deliveryScheduleDate;
	private String deliveryDelyComp;
	private String deliveryInvoiceNo;
	private String deliveryName;
	private String deliveryPhone;
	private String deliveryZipcode;
	private String deliveryBaseAddr;
	private String deliveryDetailAddr;
	private String deliveryMessage;
	private Timestamp pickupCompleteDate;
	private String cancelWithdrawYn;
	private String insertId;
	private String modifyId;
	
	private List<PaWempClaimItemList> claimItemList;
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaClaimNo() {
		return paClaimNo;
	}
	public void setPaClaimNo(String paClaimNo) {
		this.paClaimNo = paClaimNo;
	}
	public String getPaOrderNo() {
		return paOrderNo;
	}
	public void setPaOrderNo(String paOrderNo) {
		this.paOrderNo = paOrderNo;
	}
	public String getPaShipNo() {
		return paShipNo;
	}
	public void setPaShipNo(String paShipNo) {
		this.paShipNo = paShipNo;
	}
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public Timestamp getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}
	public Timestamp getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(Timestamp approveDate) {
		this.approveDate = approveDate;
	}
	public Timestamp getPendingDate() {
		return pendingDate;
	}
	public void setPendingDate(Timestamp pendingDate) {
		this.pendingDate = pendingDate;
	}
	public Timestamp getRejectDate() {
		return rejectDate;
	}
	public void setRejectDate(Timestamp rejectDate) {
		this.rejectDate = rejectDate;
	}
	public String getPaClaimCode() {
		return paClaimCode;
	}
	public void setPaClaimCode(String paClaimCode) {
		this.paClaimCode = paClaimCode;
	}
	public String getClaimReason() {
		return claimReason;
	}
	public void setClaimReason(String claimReason) {
		this.claimReason = claimReason;
	}
	public String getClaimReasonDetail() {
		return claimReasonDetail;
	}
	public void setClaimReasonDetail(String claimReasonDetail) {
		this.claimReasonDetail = claimReasonDetail;
	}
	public String getPendingReason() {
		return pendingReason;
	}
	public void setPendingReason(String pendingReason) {
		this.pendingReason = pendingReason;
	}
	public String getPendingReasonDetail() {
		return pendingReasonDetail;
	}
	public void setPendingReasonDetail(String pendingReasonDetail) {
		this.pendingReasonDetail = pendingReasonDetail;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	public String getRejectReasonDetail() {
		return rejectReasonDetail;
	}
	public void setRejectReasonDetail(String rejectReasonDetail) {
		this.rejectReasonDetail = rejectReasonDetail;
	}
	public String getClaimWhoReason() {
		return claimWhoReason;
	}
	public void setClaimWhoReason(String claimWhoReason) {
		this.claimWhoReason = claimWhoReason;
	}
	public long getClaimFee() {
		return claimFee;
	}
	public void setClaimFee(long claimFee) {
		this.claimFee = claimFee;
	}
	public String getClaimShipfeeEnclose() {
		return claimShipfeeEnclose;
	}
	public void setClaimShipfeeEnclose(String claimShipfeeEnclose) {
		this.claimShipfeeEnclose = claimShipfeeEnclose;
	}
	public String getPickupStatus() {
		return pickupStatus;
	}
	public void setPickupStatus(String pickupStatus) {
		this.pickupStatus = pickupStatus;
	}
	public String getPickupMethod() {
		return pickupMethod;
	}
	public void setPickupMethod(String pickupMethod) {
		this.pickupMethod = pickupMethod;
	}
	public Timestamp getPickupScheduleDate() {
		return pickupScheduleDate;
	}
	public void setPickupScheduleDate(Timestamp pickupScheduleDate) {
		this.pickupScheduleDate = pickupScheduleDate;
	}
	public String getPickupDelyComp() {
		return pickupDelyComp;
	}
	public void setPickupDelyComp(String pickupDelyComp) {
		this.pickupDelyComp = pickupDelyComp;
	}
	public String getPickupInvoiceNo() {
		return pickupInvoiceNo;
	}
	public void setPickupInvoiceNo(String pickupInvoiceNo) {
		this.pickupInvoiceNo = pickupInvoiceNo;
	}
	public String getPickupName() {
		return pickupName;
	}
	public void setPickupName(String pickupName) {
		this.pickupName = pickupName;
	}
	public String getPickupPhone() {
		return pickupPhone;
	}
	public void setPickupPhone(String pickupPhone) {
		this.pickupPhone = pickupPhone;
	}
	public String getPickupZipcode() {
		return pickupZipcode;
	}
	public void setPickupZipcode(String pickupZipcode) {
		this.pickupZipcode = pickupZipcode;
	}
	public String getPickupBaseAddr() {
		return pickupBaseAddr;
	}
	public void setPickupBaseAddr(String pickupBaseAddr) {
		this.pickupBaseAddr = pickupBaseAddr;
	}
	public String getPickupDetailAddr() {
		return pickupDetailAddr;
	}
	public void setPickupDetailAddr(String pickupDetailAddr) {
		this.pickupDetailAddr = pickupDetailAddr;
	}
	public String getPickupMessage() {
		return pickupMessage;
	}
	public void setPickupMessage(String pickupMessage) {
		this.pickupMessage = pickupMessage;
	}
	public String getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	public String getDeliveryMethod() {
		return deliveryMethod;
	}
	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}
	public Timestamp getDeliveryScheduleDate() {
		return deliveryScheduleDate;
	}
	public void setDeliveryScheduleDate(Timestamp deliveryScheduleDate) {
		this.deliveryScheduleDate = deliveryScheduleDate;
	}
	public String getDeliveryDelyComp() {
		return deliveryDelyComp;
	}
	public void setDeliveryDelyComp(String deliveryDelyComp) {
		this.deliveryDelyComp = deliveryDelyComp;
	}
	public String getDeliveryInvoiceNo() {
		return deliveryInvoiceNo;
	}
	public void setDeliveryInvoiceNo(String deliveryInvoiceNo) {
		this.deliveryInvoiceNo = deliveryInvoiceNo;
	}
	public String getDeliveryName() {
		return deliveryName;
	}
	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	public String getDeliveryPhone() {
		return deliveryPhone;
	}
	public void setDeliveryPhone(String deliveryPhone) {
		this.deliveryPhone = deliveryPhone;
	}
	public String getDeliveryZipcode() {
		return deliveryZipcode;
	}
	public void setDeliveryZipcode(String deliveryZipcode) {
		this.deliveryZipcode = deliveryZipcode;
	}
	public String getDeliveryBaseAddr() {
		return deliveryBaseAddr;
	}
	public void setDeliveryBaseAddr(String deliveryBaseAddr) {
		this.deliveryBaseAddr = deliveryBaseAddr;
	}
	public String getDeliveryDetailAddr() {
		return deliveryDetailAddr;
	}
	public void setDeliveryDetailAddr(String deliveryDetailAddr) {
		this.deliveryDetailAddr = deliveryDetailAddr;
	}
	public String getDeliveryMessage() {
		return deliveryMessage;
	}
	public void setDeliveryMessage(String deliveryMessage) {
		this.deliveryMessage = deliveryMessage;
	}
	public Timestamp getPickupCompleteDate() {
		return pickupCompleteDate;
	}
	public void setPickupCompleteDate(Timestamp pickupCompleteDate) {
		this.pickupCompleteDate = pickupCompleteDate;
	}
	public String getCancelWithdrawYn() {
		return cancelWithdrawYn;
	}
	public void setCancelWithdrawYn(String cancelWithdrawYn) {
		this.cancelWithdrawYn = cancelWithdrawYn;
	}
	public String getInsertId() {
		return insertId;
	}
	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}
	public String getModifyId() {
		return modifyId;
	}
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<PaWempClaimItemList> getClaimItemList() {
		return claimItemList;
	}
	public void setClaimItemList(List<PaWempClaimItemList> claimItemList) {
		this.claimItemList = claimItemList;
	}
	
	
}
