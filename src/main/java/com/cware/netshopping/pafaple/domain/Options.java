package com.cware.netshopping.pafaple.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class Options {
	
	@JsonProperty("VendorOptID")
	String VendorOptID;	   
	@JsonProperty("Color")
	String Color;		    
	@JsonProperty("Size")
	String Size;		    
	@JsonProperty("StockQty")
	Integer StockQty;	    
	@JsonProperty("OptPrice")
	Integer OptPrice;
	
	@JsonProperty("VendorOptID")
	public String getVendorOptID() {
		return VendorOptID;
	}
	public void setVendorOptID(String vendorOptID) {
		VendorOptID = vendorOptID;
	}
	@JsonProperty("Color")
	public String getColor() {
		return Color;
	}
	public void setColor(String color) {
		Color = color;
	}
	@JsonProperty("Size")
	public String getSize() {
		return Size;
	}
	public void setSize(String size) {
		Size = size;
	}
	@JsonProperty("StockQty")
	public Integer getStockQty() {
		return StockQty;
	}
	public void setStockQty(Integer stockQty) {
		StockQty = stockQty;
	}
	@JsonProperty("OptPrice")
	public Integer getOptPrice() {
		return OptPrice;
	}
	public void setOptPrice(Integer optPrice) {
		OptPrice = optPrice;
	}
}
