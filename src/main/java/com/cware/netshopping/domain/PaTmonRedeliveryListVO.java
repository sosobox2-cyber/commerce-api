package com.cware.netshopping.domain;

import java.util.ArrayList;
import java.util.Map;

import com.cware.netshopping.domain.model.PaTmonRedeliveryList;

public class PaTmonRedeliveryListVO extends PaTmonRedeliveryList implements Cloneable {
	
	private static final long serialVersionUID = 1L;
	private String paCode;
	private String decryptYn;
	private Map<String, Object> originDeliveryInvoice;
	private Map<String, Object> returnDeliveryInvoice;
	private Map<String, Object> reDeliveryInvoice;
	private ArrayList<Object>   claimDeals;
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getDecryptYn() {
		return decryptYn;
	}
	public void setDecryptYn(String decryptYn) {
		this.decryptYn = decryptYn;
	}
	public Map<String, Object> getOriginDeliveryInvoice() {
		return originDeliveryInvoice;
	}
	public void setOriginDeliveryInvoice(Map<String, Object> originDeliveryInvoice) {
		this.originDeliveryInvoice = originDeliveryInvoice;
	}
	public Map<String, Object> getReturnDeliveryInvoice() {
		return returnDeliveryInvoice;
	}
	public void setReturnDeliveryInvoice(Map<String, Object> returnDeliveryInvoice) {
		this.returnDeliveryInvoice = returnDeliveryInvoice;
	}
	public Map<String, Object> getReDeliveryInvoice() {
		return reDeliveryInvoice;
	}
	public void setReDeliveryInvoice(Map<String, Object> reDeliveryInvoice) {
		this.reDeliveryInvoice = reDeliveryInvoice;
	}
	public ArrayList<Object> getClaimDeals() {
		return claimDeals;
	}
	public void setClaimDeals(ArrayList<Object> claimDeals) {
		this.claimDeals = claimDeals;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
