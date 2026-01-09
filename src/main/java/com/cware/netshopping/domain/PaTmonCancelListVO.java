package com.cware.netshopping.domain;

import java.util.ArrayList;
import java.util.Map;

import com.cware.netshopping.domain.model.PaTmonCancelList;

public class PaTmonCancelListVO extends PaTmonCancelList implements Cloneable {
	
	private static final long serialVersionUID = 1L;
	private String paCode;
	private String decryptYn;
	private String paOrderGb;
	private String outBefClaimGb;
	private ArrayList<Object> claimDeals;
	private Map<String, Object> originDeliveryInvoice;
	
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
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}	
	public ArrayList<Object> getClaimDeals() {
		return claimDeals;
	}
	public void setClaimDeals(ArrayList<Object> claimDeals) {
		this.claimDeals = claimDeals;
	}
	public Map<String, Object> getOriginDeliveryInvoice() {
		return originDeliveryInvoice;
	}
	public void setOriginDeliveryInvoice(Map<String, Object> originDeliveryInvoice) {
		this.originDeliveryInvoice = originDeliveryInvoice;
	}	
	public String getOutBefClaimGb() {
		return outBefClaimGb;
	}
	public void setOutBefClaimGb(String outBefClaimGb) {
		this.outBefClaimGb = outBefClaimGb;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
