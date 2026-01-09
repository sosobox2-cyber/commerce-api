package com.cware.netshopping.pafaple.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Item extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String itemCode;
	private String marketItemCode;
	private String itemName;
	private String bannerImgUrl;
	private String itemImgUrl;
	private int itemPrice;
	private String publicItemYn;
	private String intngItemYn;
	private String repItemYn;
	private int priority;
	
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getMarketItemCode() {
		return marketItemCode;
	}
	public void setMarketItemCode(String marketItemCode) {
		this.marketItemCode = marketItemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getBannerImgUrl() {
		return bannerImgUrl;
	}
	public void setBannerImgUrl(String bannerImgUrl) {
		this.bannerImgUrl = bannerImgUrl;
	}
	public String getItemImgUrl() {
		return itemImgUrl;
	}
	public void setItemImgUrl(String itemImgUrl) {
		this.itemImgUrl = itemImgUrl;
	}
	public int getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(int itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getPublicItemYn() {
		return publicItemYn;
	}
	public void setPublicItemYn(String publicItemYn) {
		this.publicItemYn = publicItemYn;
	}
	public String getIntngItemYn() {
		return intngItemYn;
	}
	public void setIntngItemYn(String intngItemYn) {
		this.intngItemYn = intngItemYn;
	}
	public String getRepItemYn() {
		return repItemYn;
	}
	public void setRepItemYn(String repItemYn) {
		this.repItemYn = repItemYn;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}	
	
	
}