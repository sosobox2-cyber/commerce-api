package com.cware.netshopping.panaver.v3.domain;

import java.util.ArrayList;
import java.util.List;

public class ChangedProductOrderInfoList {
		
	private List<ChangedProductOrderInfo> payedList = new ArrayList<ChangedProductOrderInfo>();
	private List<ChangedProductOrderInfo> cancelRequestedList = new ArrayList<ChangedProductOrderInfo>();
	private List<ChangedProductOrderInfo> canceledList = new ArrayList<ChangedProductOrderInfo>();
	private List<ChangedProductOrderInfo> exchangeRequestedList = new ArrayList<ChangedProductOrderInfo>();
	private List<ChangedProductOrderInfo> returnRequestedList = new ArrayList<ChangedProductOrderInfo>();
	private List<ChangedProductOrderInfo> returnedList = new ArrayList<ChangedProductOrderInfo>();
	private List<ChangedProductOrderInfo> returnRejectList = new ArrayList<ChangedProductOrderInfo>();
	private List<ChangedProductOrderInfo> exchangeRejectList = new ArrayList<ChangedProductOrderInfo>();
	private List<ChangedProductOrderInfo> cancelRejectList = new ArrayList<ChangedProductOrderInfo>();
	private List<ChangedProductOrderInfo> exchangeCollectedList = new ArrayList<ChangedProductOrderInfo>();
	private List<ChangedProductOrderInfo> payWaitingList = new ArrayList<ChangedProductOrderInfo>();
	private List<ChangedProductOrderInfo> purchaseDecidedList = new ArrayList<ChangedProductOrderInfo>();
	private int saveCount;
	
	public List<ChangedProductOrderInfo> getPayedList() {
		return payedList;
	}
	public void setPayedList(List<ChangedProductOrderInfo> payedList) {
		this.payedList = payedList;
	}
	public List<ChangedProductOrderInfo> getCancelRequestedList() {
		return cancelRequestedList;
	}
	public void setCancelRequestedList(List<ChangedProductOrderInfo> cancelRequestedList) {
		this.cancelRequestedList = cancelRequestedList;
	}
	public List<ChangedProductOrderInfo> getCanceledList() {
		return canceledList;
	}
	public void setCanceledList(List<ChangedProductOrderInfo> canceledList) {
		this.canceledList = canceledList;
	}
	public List<ChangedProductOrderInfo> getExchangeRequestedList() {
		return exchangeRequestedList;
	}
	public void setExchangeRequestedList(List<ChangedProductOrderInfo> exchangeRequestedList) {
		this.exchangeRequestedList = exchangeRequestedList;
	}
	public List<ChangedProductOrderInfo> getReturnRequestedList() {
		return returnRequestedList;
	}
	public void setReturnRequestedList(List<ChangedProductOrderInfo> returnRequestedList) {
		this.returnRequestedList = returnRequestedList;
	}
	public List<ChangedProductOrderInfo> getReturnedList() {
		return returnedList;
	}
	public void setReturnedList(List<ChangedProductOrderInfo> returnedList) {
		this.returnedList = returnedList;
	}
	public List<ChangedProductOrderInfo> getReturnRejectList() {
		return returnRejectList;
	}
	public void setReturnRejectList(List<ChangedProductOrderInfo> returnRejectList) {
		this.returnRejectList = returnRejectList;
	}
	public List<ChangedProductOrderInfo> getExchangeRejectList() {
		return exchangeRejectList;
	}
	public void setExchangeRejectList(List<ChangedProductOrderInfo> exchangeRejectList) {
		this.exchangeRejectList = exchangeRejectList;
	}
	public List<ChangedProductOrderInfo> getCancelRejectList() {
		return cancelRejectList;
	}
	public void setCancelRejectList(List<ChangedProductOrderInfo> cancelRejectList) {
		this.cancelRejectList = cancelRejectList;
	}
	public List<ChangedProductOrderInfo> getExchangeCollectedList() {
		return exchangeCollectedList;
	}
	public void setExchangeCollectedList(List<ChangedProductOrderInfo> exchangeCollectedList) {
		this.exchangeCollectedList = exchangeCollectedList;
	}
	public List<ChangedProductOrderInfo> getPayWaitingList() {
		return payWaitingList;
	}
	public void setPayWaitingList(List<ChangedProductOrderInfo> payWaitingList) {
		this.payWaitingList = payWaitingList;
	}
	public List<ChangedProductOrderInfo> getPurchaseDecidedList() {
		return purchaseDecidedList;
	}
	public void setPurchaseDecidedList(List<ChangedProductOrderInfo> purchaseDecidedList) {
		this.purchaseDecidedList = purchaseDecidedList;
	}
	public int getSaveCount() {
		return saveCount;
	}
	public void setSaveCount(int saveCount) {
		this.saveCount = saveCount;
	}
}
