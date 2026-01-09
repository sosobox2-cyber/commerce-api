package com.cware.api.patdeal.message;

import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.AbstractMessage;

public class OrderConfirmResoponseMsg extends AbstractMessage {

	private static final long serialVersionUID = -5414679323292207065L;

	private List<Map<String,Object>> soldOutList; 
	
	private List<Map<String,Object>> cancelList; 
	
	public OrderConfirmResoponseMsg(String code, String message, List<Map<String,Object>> soldOutList, List<Map<String,Object>> cancelList) {
    	this.setCode(code);
    	this.setMessage(message);
    	this.setSoldOutList(soldOutList);
    	this.setCancelList(cancelList);
    }

	public List<Map<String, Object>> getSoldOutList() {
		return soldOutList;
	}

	public void setSoldOutList(List<Map<String, Object>> soldOutList) {
		this.soldOutList = soldOutList;
	}

	public List<Map<String, Object>> getCancelList() {
		return cancelList;
	}

	public void setCancelList(List<Map<String, Object>> cancelList) {
		this.cancelList = cancelList;
	}

	

}
