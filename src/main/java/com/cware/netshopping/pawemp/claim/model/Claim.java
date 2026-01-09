package com.cware.netshopping.pawemp.claim.model;

import java.util.List;

public class Claim {
	private long claimBundleNo;
	private String claimType;
	private String claimStatus;
	private long purchaseNo;
	private long bundleNo;
	private String requestDate;
	private String approveDate;
	private String pendingDate;
	private String rejectDate;
	private String claimReason;
	private String claimReasonDetail;
	private String pendingReason;
	private String pendingReasonDetail;
	private String rejectReason;
	private String rejectReasonDetail;
	private String claimWhoReason;
	private long claimFee;
	private String claimShipFeeEnclose;
	private Pickup pickup;
	private Delivery delivery;
	private List<OrderProduct> orderProduct;
	public long getClaimBundleNo() {
		return claimBundleNo;
	}
	public void setClaimBundleNo(long claimBundleNo) {
		this.claimBundleNo = claimBundleNo;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public long getPurchaseNo() {
		return purchaseNo;
	}
	public void setPurchaseNo(long purchaseNo) {
		this.purchaseNo = purchaseNo;
	}
	public long getBundleNo() {
		return bundleNo;
	}
	public void setBundleNo(long bundleNo) {
		this.bundleNo = bundleNo;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public String getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}
	public String getPendingDate() {
		return pendingDate;
	}
	public void setPendingDate(String pendingDate) {
		this.pendingDate = pendingDate;
	}
	public String getRejectDate() {
		return rejectDate;
	}
	public void setRejectDate(String rejectDate) {
		this.rejectDate = rejectDate;
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
	public String getClaimShipFeeEnclose() {
		return claimShipFeeEnclose;
	}
	public void setClaimShipFeeEnclose(String claimShipFeeEnclose) {
		this.claimShipFeeEnclose = claimShipFeeEnclose;
	}
	public Pickup getPickup() {
		return pickup;
	}
	public void setPickup(Pickup pickup) {
		this.pickup = pickup;
	}
	public Delivery getDelivery() {
		return delivery;
	}
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}
	public List<OrderProduct> getOrderProduct() {
		return orderProduct;
	}
	public void setOrderProduct(List<OrderProduct> orderProduct) {
		this.orderProduct = orderProduct;
	}
	
	
	
}
