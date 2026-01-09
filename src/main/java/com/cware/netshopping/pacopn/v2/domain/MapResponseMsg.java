package com.cware.netshopping.pacopn.v2.domain;

import java.util.Map;

public class MapResponseMsg extends ResultMsg {

	Map<String, String> data;

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "MapResponseMsg [data=" + data + "]";
	}
	
}
