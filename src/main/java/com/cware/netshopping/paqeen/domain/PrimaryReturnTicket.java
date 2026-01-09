package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class PrimaryReturnTicket {
    private String ticketId;
    private Boolean isCustomerNegligence;
    private ReturnAddress returnAddress; 
    private String state;
    private String returnState;
    private String auditorGroup;
    private String reason;
    private Long createdAtMillis;
    private Long confirmedAtMillis;
    private Long withdrawnAtMillis;
    private Long rejectedAtMillis;
    private String rejectReason;
    private Long autoRefundExpectedAtMillis;
    private String autoRefundExpectedKstDate;
    private Boolean isAutoRefundExcluded;
    private String recallDeliveryExpectedKstDate;
    private Boolean isDeliveryDelayed;
    private Boolean isReturnRecallTracked;
    private List<String> rejectReasonImageUrls;
    private Long resolvedAtMillis;
    private ResolvedPriceDelta resolvedPriceDelta; 
    private List<RefundPaymentInfo> refundPaymentInfos;
    private List<OrderItem> orderItems;
    private List<String> imageUrls;
    private MultiResolutionImages multiResolutionImages;
    private RecallDeliveryProgress recallDeliveryProgress;
    private Boolean isPreRefunded;
    private String lossProcessingState;
    private Long preRefundedAtMillis;
    private ResolvedPriceDelta preRefundedPriceDelta;
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	public Boolean getIsCustomerNegligence() {
		return isCustomerNegligence;
	}
	public void setIsCustomerNegligence(Boolean isCustomerNegligence) {
		this.isCustomerNegligence = isCustomerNegligence;
	}
	public ReturnAddress getReturnAddress() {
		return returnAddress;
	}
	public void setReturnAddress(ReturnAddress returnAddress) {
		this.returnAddress = returnAddress;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getReturnState() {
		return returnState;
	}
	public void setReturnState(String returnState) {
		this.returnState = returnState;
	}
	public String getAuditorGroup() {
		return auditorGroup;
	}
	public void setAuditorGroup(String auditorGroup) {
		this.auditorGroup = auditorGroup;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Long getCreatedAtMillis() {
		return createdAtMillis;
	}
	public void setCreatedAtMillis(Long createdAtMillis) {
		this.createdAtMillis = createdAtMillis;
	}
	public Long getConfirmedAtMillis() {
		return confirmedAtMillis;
	}
	public void setConfirmedAtMillis(Long confirmedAtMillis) {
		this.confirmedAtMillis = confirmedAtMillis;
	}
	public Long getWithdrawnAtMillis() {
		return withdrawnAtMillis;
	}
	public void setWithdrawnAtMillis(Long withdrawnAtMillis) {
		this.withdrawnAtMillis = withdrawnAtMillis;
	}
	public Long getRejectedAtMillis() {
		return rejectedAtMillis;
	}
	public void setRejectedAtMillis(Long rejectedAtMillis) {
		this.rejectedAtMillis = rejectedAtMillis;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	public Long getAutoRefundExpectedAtMillis() {
		return autoRefundExpectedAtMillis;
	}
	public void setAutoRefundExpectedAtMillis(Long autoRefundExpectedAtMillis) {
		this.autoRefundExpectedAtMillis = autoRefundExpectedAtMillis;
	}
	public String getAutoRefundExpectedKstDate() {
		return autoRefundExpectedKstDate;
	}
	public void setAutoRefundExpectedKstDate(String autoRefundExpectedKstDate) {
		this.autoRefundExpectedKstDate = autoRefundExpectedKstDate;
	}
	public Boolean getIsAutoRefundExcluded() {
		return isAutoRefundExcluded;
	}
	public void setIsAutoRefundExcluded(Boolean isAutoRefundExcluded) {
		this.isAutoRefundExcluded = isAutoRefundExcluded;
	}
	public String getRecallDeliveryExpectedKstDate() {
		return recallDeliveryExpectedKstDate;
	}
	public void setRecallDeliveryExpectedKstDate(String recallDeliveryExpectedKstDate) {
		this.recallDeliveryExpectedKstDate = recallDeliveryExpectedKstDate;
	}
	public Boolean getIsDeliveryDelayed() {
		return isDeliveryDelayed;
	}
	public void setIsDeliveryDelayed(Boolean isDeliveryDelayed) {
		this.isDeliveryDelayed = isDeliveryDelayed;
	}
	public Boolean getIsReturnRecallTracked() {
		return isReturnRecallTracked;
	}
	public void setIsReturnRecallTracked(Boolean isReturnRecallTracked) {
		this.isReturnRecallTracked = isReturnRecallTracked;
	}
	public List<String> getRejectReasonImageUrls() {
		return rejectReasonImageUrls;
	}
	public void setRejectReasonImageUrls(List<String> rejectReasonImageUrls) {
		this.rejectReasonImageUrls = rejectReasonImageUrls;
	}
	public Long getResolvedAtMillis() {
		return resolvedAtMillis;
	}
	public void setResolvedAtMillis(Long resolvedAtMillis) {
		this.resolvedAtMillis = resolvedAtMillis;
	}
	public ResolvedPriceDelta getResolvedPriceDelta() {
		return resolvedPriceDelta;
	}
	public void setResolvedPriceDelta(ResolvedPriceDelta resolvedPriceDelta) {
		this.resolvedPriceDelta = resolvedPriceDelta;
	}
	public List<RefundPaymentInfo> getRefundPaymentInfos() {
		return refundPaymentInfos;
	}
	public void setRefundPaymentInfos(List<RefundPaymentInfo> refundPaymentInfos) {
		this.refundPaymentInfos = refundPaymentInfos;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public List<String> getImageUrls() {
		return imageUrls;
	}
	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}
	public MultiResolutionImages getMultiResolutionImages() {
		return multiResolutionImages;
	}
	public void setMultiResolutionImages(MultiResolutionImages multiResolutionImages) {
		this.multiResolutionImages = multiResolutionImages;
	}
	public RecallDeliveryProgress getRecallDeliveryProgress() {
		return recallDeliveryProgress;
	}
	public void setRecallDeliveryProgress(RecallDeliveryProgress recallDeliveryProgress) {
		this.recallDeliveryProgress = recallDeliveryProgress;
	}
	public Boolean getIsPreRefunded() {
		return isPreRefunded;
	}
	public void setIsPreRefunded(Boolean isPreRefunded) {
		this.isPreRefunded = isPreRefunded;
	}
	public String getLossProcessingState() {
		return lossProcessingState;
	}
	public void setLossProcessingState(String lossProcessingState) {
		this.lossProcessingState = lossProcessingState;
	}
	public Long getPreRefundedAtMillis() {
		return preRefundedAtMillis;
	}
	public void setPreRefundedAtMillis(Long preRefundedAtMillis) {
		this.preRefundedAtMillis = preRefundedAtMillis;
	}
	public ResolvedPriceDelta getPreRefundedPriceDelta() {
		return preRefundedPriceDelta;
	}
	public void setPreRefundedPriceDelta(ResolvedPriceDelta preRefundedPriceDelta) {
		this.preRefundedPriceDelta = preRefundedPriceDelta;
	}
    

}
