package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class SpPaGoodsInfo extends AbstractModel {
	
	/**
	 * SP_PAGOODS_SYNC_PROC [ CUR_GOODS_INFO ]
	 */

	private static final long serialVersionUID = 1L;
	private String goodsCode;
	private int paCodeChk; //네이버만 따로 처리하는게 있어서 네이버 포함 여부 체크
	private String oldGoodsName;
	private String newGoodsName;
	private String oldSaleGb;
	private String newSaleGb;
	private String oldLmsdCode;
	private String newLmsdCode;
	private String oldMakecoCode;
	private String newMakecoCode;
	private String newBrandCode;
	private String oldBrandName;
	private String newBrandName;
	private String oldOriginCode;
	private String newOriginCode;
	private String oldOriginName;
	private String newOriginName;
	private String oldTaxYn;
	private String newTaxYn;
	private String oldTaxSmallYn;
	private String newTaxSmallYn;
	private String oldAdultYn;
	private String newAdultYn;
	private int oldOrderMinQty;
	private int newOrderMinQty;
	private int oldOrderMaxQty;
	private int newOrderMaxQty;
	private int oldCustOrdQtyCheckTerm;
	private int newCustOrdQtyCheckTerm;
	private String oldEntpCode;
	private String newEntpCode;
	private String oldShipManSeq;
	private String newShipManSeq;
	private String oldReturnManSeq;
	private String newReturnManSeq;
	private String oldShipCostCode;
	private String newShipCostCode;
	private int oldAvgDelyLeadtime;
	private int newAvgDelyLeadtime;
	private String syncMode;
	private Timestamp oldSaleStartDate;	
	private Timestamp newSaleStartDate;	
	private Timestamp oldSaleEndDate;		
	private Timestamp newSaleEndDate;		
	private String newGoodsNameMc;
	private String newOrderCreateYn;
	private String oldOrderCreateYn;
	private String oldKeyword;
	private String newKeyword;
	private String oldCollectYn;
	private String newCollectYn;
	private String oldDoNotIslandDelyYn;
	private String newDoNotIslandDelyYn;
	private String dateTime;
	
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public int getPaCodeChk() {
		return paCodeChk;
	}
	public void setPaCodeChk(int paCodeChk) {
		this.paCodeChk = paCodeChk;
	}
	public String getOldGoodsName() {
		return oldGoodsName;
	}
	public void setOldGoodsName(String oldGoodsName) {
		this.oldGoodsName = oldGoodsName;
	}
	public String getNewGoodsName() {
		return newGoodsName;
	}
	public void setNewGoodsName(String newGoodsName) {
		this.newGoodsName = newGoodsName;
	}
	public String getOldSaleGb() {
		return oldSaleGb;
	}
	public void setOldSaleGb(String oldSaleGb) {
		this.oldSaleGb = oldSaleGb;
	}
	public String getNewSaleGb() {
		return newSaleGb;
	}
	public void setNewSaleGb(String newSaleGb) {
		this.newSaleGb = newSaleGb;
	}
	public String getOldLmsdCode() {
		return oldLmsdCode;
	}
	public void setOldLmsdCode(String oldLmsdCode) {
		this.oldLmsdCode = oldLmsdCode;
	}
	public String getNewLmsdCode() {
		return newLmsdCode;
	}
	public void setNewLmsdCode(String newLmsdCode) {
		this.newLmsdCode = newLmsdCode;
	}
	public String getOldMakecoCode() {
		return oldMakecoCode;
	}
	public void setOldMakecoCode(String oldMakecoCode) {
		this.oldMakecoCode = oldMakecoCode;
	}
	public String getNewMakecoCode() {
		return newMakecoCode;
	}
	public void setNewMakecoCode(String newMakecoCode) {
		this.newMakecoCode = newMakecoCode;
	}
	public String getNewBrandCode() {
		return newBrandCode;
	}
	public void setNewBrandCode(String newBrandCode) {
		this.newBrandCode = newBrandCode;
	}
	public String getOldBrandName() {
		return oldBrandName;
	}
	public void setOldBrandName(String oldBrandName) {
		this.oldBrandName = oldBrandName;
	}
	public String getNewBrandName() {
		return newBrandName;
	}
	public void setNewBrandName(String newBrandName) {
		this.newBrandName = newBrandName;
	}
	public String getOldOriginCode() {
		return oldOriginCode;
	}
	public void setOldOriginCode(String oldOriginCode) {
		this.oldOriginCode = oldOriginCode;
	}
	public String getNewOriginCode() {
		return newOriginCode;
	}
	public void setNewOriginCode(String newOriginCode) {
		this.newOriginCode = newOriginCode;
	}
	public String getOldOriginName() {
		return oldOriginName;
	}
	public void setOldOriginName(String oldOriginName) {
		this.oldOriginName = oldOriginName;
	}
	public String getNewOriginName() {
		return newOriginName;
	}
	public void setNewOriginName(String newOriginName) {
		this.newOriginName = newOriginName;
	}
	public String getOldTaxYn() {
		return oldTaxYn;
	}
	public void setOldTaxYn(String oldTaxYn) {
		this.oldTaxYn = oldTaxYn;
	}
	public String getNewTaxYn() {
		return newTaxYn;
	}
	public void setNewTaxYn(String newTaxYn) {
		this.newTaxYn = newTaxYn;
	}
	public String getOldTaxSmallYn() {
		return oldTaxSmallYn;
	}
	public void setOldTaxSmallYn(String oldTaxSmallYn) {
		this.oldTaxSmallYn = oldTaxSmallYn;
	}
	public String getNewTaxSmallYn() {
		return newTaxSmallYn;
	}
	public void setNewTaxSmallYn(String newTaxSmallYn) {
		this.newTaxSmallYn = newTaxSmallYn;
	}
	public String getOldAdultYn() {
		return oldAdultYn;
	}
	public void setOldAdultYn(String oldAdultYn) {
		this.oldAdultYn = oldAdultYn;
	}
	public String getNewAdultYn() {
		return newAdultYn;
	}
	public void setNewAdultYn(String newAdultYn) {
		this.newAdultYn = newAdultYn;
	}
	public int getOldOrderMinQty() {
		return oldOrderMinQty;
	}
	public void setOldOrderMinQty(int oldOrderMinQty) {
		this.oldOrderMinQty = oldOrderMinQty;
	}
	public int getNewOrderMinQty() {
		return newOrderMinQty;
	}
	public void setNewOrderMinQty(int newOrderMinQty) {
		this.newOrderMinQty = newOrderMinQty;
	}
	public int getOldOrderMaxQty() {
		return oldOrderMaxQty;
	}
	public void setOldOrderMaxQty(int oldOrderMaxQty) {
		this.oldOrderMaxQty = oldOrderMaxQty;
	}
	public int getNewOrderMaxQty() {
		return newOrderMaxQty;
	}
	public void setNewOrderMaxQty(int newOrderMaxQty) {
		this.newOrderMaxQty = newOrderMaxQty;
	}
	public int getOldCustOrdQtyCheckTerm() {
		return oldCustOrdQtyCheckTerm;
	}
	public void setOldCustOrdQtyCheckTerm(int oldCustOrdQtyCheckTerm) {
		this.oldCustOrdQtyCheckTerm = oldCustOrdQtyCheckTerm;
	}
	public int getNewCustOrdQtyCheckTerm() {
		return newCustOrdQtyCheckTerm;
	}
	public void setNewCustOrdQtyCheckTerm(int newCustOrdQtyCheckTerm) {
		this.newCustOrdQtyCheckTerm = newCustOrdQtyCheckTerm;
	}
	public String getOldEntpCode() {
		return oldEntpCode;
	}
	public void setOldEntpCode(String oldEntpCode) {
		this.oldEntpCode = oldEntpCode;
	}
	public String getNewEntpCode() {
		return newEntpCode;
	}
	public void setNewEntpCode(String newEntpCode) {
		this.newEntpCode = newEntpCode;
	}
	public String getOldShipManSeq() {
		return oldShipManSeq;
	}
	public void setOldShipManSeq(String oldShipManSeq) {
		this.oldShipManSeq = oldShipManSeq;
	}
	public String getNewShipManSeq() {
		return newShipManSeq;
	}
	public void setNewShipManSeq(String newShipManSeq) {
		this.newShipManSeq = newShipManSeq;
	}
	public String getOldReturnManSeq() {
		return oldReturnManSeq;
	}
	public void setOldReturnManSeq(String oldReturnManSeq) {
		this.oldReturnManSeq = oldReturnManSeq;
	}
	public String getNewReturnManSeq() {
		return newReturnManSeq;
	}
	public void setNewReturnManSeq(String newReturnManSeq) {
		this.newReturnManSeq = newReturnManSeq;
	}
	public String getOldShipCostCode() {
		return oldShipCostCode;
	}
	public void setOldShipCostCode(String oldShipCostCode) {
		this.oldShipCostCode = oldShipCostCode;
	}
	public String getNewShipCostCode() {
		return newShipCostCode;
	}
	public void setNewShipCostCode(String newShipCostCode) {
		this.newShipCostCode = newShipCostCode;
	}
	public int getOldAvgDelyLeadtime() {
		return oldAvgDelyLeadtime;
	}
	public void setOldAvgDelyLeadtime(int oldAvgDelyLeadtime) {
		this.oldAvgDelyLeadtime = oldAvgDelyLeadtime;
	}
	public int getNewAvgDelyLeadtime() {
		return newAvgDelyLeadtime;
	}
	public void setNewAvgDelyLeadtime(int newAvgDelyLeadtime) {
		this.newAvgDelyLeadtime = newAvgDelyLeadtime;
	}
	public String getSyncMode() {
		return syncMode;
	}
	public void setSyncMode(String syncMode) {
		this.syncMode = syncMode;
	}
	public Timestamp getOldSaleStartDate() {
		return oldSaleStartDate;
	}
	public void setOldSaleStartDate(Timestamp oldSaleStartDate) {
		this.oldSaleStartDate = oldSaleStartDate;
	}
	public Timestamp getNewSaleStartDate() {
		return newSaleStartDate;
	}
	public void setNewSaleStartDate(Timestamp newSaleStartDate) {
		this.newSaleStartDate = newSaleStartDate;
	}
	public Timestamp getOldSaleEndDate() {
		return oldSaleEndDate;
	}
	public void setOldSaleEndDate(Timestamp oldSaleEndDate) {
		this.oldSaleEndDate = oldSaleEndDate;
	}
	public Timestamp getNewSaleEndDate() {
		return newSaleEndDate;
	}
	public void setNewSaleEndDate(Timestamp newSaleEndDate) {
		this.newSaleEndDate = newSaleEndDate;
	}
	public String getNewGoodsNameMc() {
		return newGoodsNameMc;
	}
	public void setNewGoodsNameMc(String newGoodsNameMc) {
		this.newGoodsNameMc = newGoodsNameMc;
	}
	public String getNewOrderCreateYn() {
		return newOrderCreateYn;
	}
	public void setNewOrderCreateYn(String newOrderCreateYn) {
		this.newOrderCreateYn = newOrderCreateYn;
	}
	public String getOldOrderCreateYn() {
		return oldOrderCreateYn;
	}
	public void setOldOrderCreateYn(String oldOrderCreateYn) {
		this.oldOrderCreateYn = oldOrderCreateYn;
	}
	public String getOldKeyword() {
		return oldKeyword;
	}
	public void setOldKeyword(String oldKeyword) {
		this.oldKeyword = oldKeyword;
	}
	public String getNewKeyword() {
		return newKeyword;
	}
	public void setNewKeyword(String newKeyword) {
		this.newKeyword = newKeyword;
	}
	public String getOldCollectYn() {
		return oldCollectYn;
	}
	public void setOldCollectYn(String oldCollectYn) {
		this.oldCollectYn = oldCollectYn;
	}
	public String getNewCollectYn() {
		return newCollectYn;
	}
	public void setNewCollectYn(String newCollectYn) {
		this.newCollectYn = newCollectYn;
	}
	public String getOldDoNotIslandDelyYn() {
		return oldDoNotIslandDelyYn;
	}
	public void setOldDoNotIslandDelyYn(String oldDoNotIslandDelyYn) {
		this.oldDoNotIslandDelyYn = oldDoNotIslandDelyYn;
	}
	public String getNewDoNotIslandDelyYn() {
		return newDoNotIslandDelyYn;
	}
	public void setNewDoNotIslandDelyYn(String newDoNotIslandDelyYn) {
		this.newDoNotIslandDelyYn = newDoNotIslandDelyYn;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
}
