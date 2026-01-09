package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaTdealEvent extends AbstractModel {
	private static final long serialVersionUID = 1L; 
	
	private String goodsCode;
	private String goodsEventName;
	private String promoYn;
	private String dispCatId;
	private String saleStartDate;
	private String saleEndDate;
	private Timestamp eventBdate;
	private Timestamp eventEdate;
	private String useYn;
	private String lastSyncDate;
	private String paLmsdKey;
	private String paBrandNo;
	private String cartUseYn;
	private int maxBuyPersonCnt;
	
	private String insertId;
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;
	
	public String getCartUseYn() {
		return cartUseYn;
	}
	public void setCartUseYn(String cartUseYn) {
		this.cartUseYn = cartUseYn;
	}
	public int getMaxBuyPersonCnt() {
		return maxBuyPersonCnt;
	}
	public void setMaxBuyPersonCnt(int maxBuyPersonCnt) {
		this.maxBuyPersonCnt = maxBuyPersonCnt;
	}
	public String getPaLmsdKey() {
		return paLmsdKey;
	}
	public void setPaLmsdKey(String paLmsdKey) {
		this.paLmsdKey = paLmsdKey;
	}
	public String getPaBrandNo() {
		return paBrandNo;
	}
	public void setPaBrandNo(String paBrandNo) {
		this.paBrandNo = paBrandNo;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsEventName() {
		return goodsEventName;
	}
	public void setGoodsEventName(String goodsEventName) {
		this.goodsEventName = goodsEventName;
	}
	public String getPromoYn() {
		return promoYn;
	}
	public void setPromoYn(String promoYn) {
		this.promoYn = promoYn;
	}
	public String getDispCatId() {
		return dispCatId;
	}
	public void setDispCatId(String dispCatId) {
		this.dispCatId = dispCatId;
	}
	public String getSaleStartDate() {
		return saleStartDate;
	}
	public void setSaleStartDate(String saleStartDate) {
		this.saleStartDate = saleStartDate;
	}
	public String getSaleEndDate() {
		return saleEndDate;
	}
	public void setSaleEndDate(String saleEndDate) {
		this.saleEndDate = saleEndDate;
	}
	public Timestamp getEventBdate() {
		return eventBdate;
	}
	public void setEventBdate(Timestamp eventBdate) {
		this.eventBdate = eventBdate;
	}
	public Timestamp getEventEdate() {
		return eventEdate;
	}
	public void setEventEdate(Timestamp eventEdate) {
		this.eventEdate = eventEdate;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getLastSyncDate() {
		return lastSyncDate;
	}
	public void setLastSyncDate(String lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}
	public String getInsertId() {
		return insertId;
	}
	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}
	public Timestamp getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}
	public String getModifyId() {
		return modifyId;
	}
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	
}
