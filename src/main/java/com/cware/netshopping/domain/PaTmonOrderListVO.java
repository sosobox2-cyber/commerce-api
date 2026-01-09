package com.cware.netshopping.domain;

import java.util.ArrayList;
import java.util.Map;

import com.cware.netshopping.domain.model.PaTmonOrderList;

public class PaTmonOrderListVO extends PaTmonOrderList implements Cloneable {
	
	private static final long serialVersionUID = 1L;
	private String paCode;
	private String paOrderGb;
	private String decryptYn;
	private Map<String, Object> receiverAddress;
	private ArrayList<Object> deals;
	private ArrayList<Object> deliveryOrderDeal;
	private ArrayList<Object> deliveryFees;
	private ArrayList<Object> dealOptionPromotions;
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getDecryptYn() {
		return decryptYn;
	}
	public void setDecryptYn(String decryptYn) {
		this.decryptYn = decryptYn;
	}
	public Map<String, Object> getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(Map<String, Object> receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public ArrayList<Object> getDeals() {
		return deals;
	}
	public void setDeals(ArrayList<Object> deals) {
		this.deals = deals;
	}
	public ArrayList<Object> getDeliveryOrderDeal() {
		return deliveryOrderDeal;
	}
	public void setDeliveryOrderDeal(ArrayList<Object> deliveryOrderDeal) {
		this.deliveryOrderDeal = deliveryOrderDeal;
	}
	public ArrayList<Object> getDeliveryFees() {
		return deliveryFees;
	}
	public void setDeliveryFees(ArrayList<Object> deliveryFees) {
		this.deliveryFees = deliveryFees;
	}
	public ArrayList<Object> getDealOptionPromotions() {
		return dealOptionPromotions;
	}
	public void setDealOptionPromotions(ArrayList<Object> dealOptionPromotions) {
		this.dealOptionPromotions = dealOptionPromotions;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
