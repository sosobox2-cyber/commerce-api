package com.cware.api.pacommon.message.pacommon;

import com.cware.framework.core.basic.*;

public class OrderInfoMsg extends AbstractMessage {

	static final long serialVersionUID = 1091286432607778919L;
    
	private long salePrice;
	private long buyPrice;
	private long saleVatRate;
	private long arsDcAmt;
	private double arsOwnDcAmt;
    private double arsEntpDcAmt;
	private String goodsCode;
	private String goodsName;
	private String entpCode;
	private String entpCodeOrg;
	private String inviGoodsType;	
	private String norestAllotMonths;
	private String delyType;
	private String whCode;
	private String mdCode;
	private String directShipYn;
	private String goodsDtCode;
	private String goodsDtInfo;
	private String stockChkPlace;
	private String giftYn;
	private String priceSeq;
	
	private long lumpSumDcAmt;
	private double lumpSumOwnDcAmt;
    private double lumpSumEntpDcAmt;
	
    
    
	public String getEntpCodeOrg() {
		return entpCodeOrg;
	}
	public void setEntpCodeOrg(String entpCodeOrg) {
		this.entpCodeOrg = entpCodeOrg;
	}
	public long getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(long salePrice) {
		this.salePrice = salePrice;
	}
	public long getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(long buyPrice) {
		this.buyPrice = buyPrice;
	}
	public long getSaleVatRate() {
		return saleVatRate;
	}
	public void setSaleVatRate(long saleVatRate) {
		this.saleVatRate = saleVatRate;
	}
	public long getArsDcAmt() {
		return arsDcAmt;
	}
	public void setArsDcAmt(long arsDcAmt) {
		this.arsDcAmt = arsDcAmt;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getEntpCode() {
		return entpCode;
	}
	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}
	public String getInviGoodsType() {
		return inviGoodsType;
	}
	public void setInviGoodsType(String inviGoodsType) {
		this.inviGoodsType = inviGoodsType;
	}
	public String getNorestAllotMonths() {
		return norestAllotMonths;
	}
	public void setNorestAllotMonths(String norestAllotMonths) {
		this.norestAllotMonths = norestAllotMonths;
	}
	public String getDelyType() {
		return delyType;
	}
	public void setDelyType(String delyType) {
		this.delyType = delyType;
	}
	public String getWhCode() {
		return whCode;
	}
	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}
	public String getMdCode() {
		return mdCode;
	}
	public void setMdCode(String mdCode) {
		this.mdCode = mdCode;
	}
	public String getDirectShipYn() {
		return directShipYn;
	}
	public void setDirectShipYn(String directShipYn) {
		this.directShipYn = directShipYn;
	}
	public String getGoodsDtCode() {
		return goodsDtCode;
	}
	public void setGoodsDtCode(String goodsDtCode) {
		this.goodsDtCode = goodsDtCode;
	}
	public String getGoodsDtInfo() {
		return goodsDtInfo;
	}
	public void setGoodsDtInfo(String goodsDtInfo) {
		this.goodsDtInfo = goodsDtInfo;
	}
	public String getStockChkPlace() {
		return stockChkPlace;
	}
	public void setStockChkPlace(String stockChkPlace) {
		this.stockChkPlace = stockChkPlace;
	}
	public String getGiftYn() {
		return giftYn;
	}
	public void setGiftYn(String giftYn) {
		this.giftYn = giftYn;
	}
	public double getArsOwnDcAmt() {
		return arsOwnDcAmt;
	}
	public void setArsOwnDcAmt(double arsOwnDcAmt) {
		this.arsOwnDcAmt = arsOwnDcAmt;
	}
	public double getArsEntpDcAmt() {
		return arsEntpDcAmt;
	}
	public void setArsEntpDcAmt(double arsEntpDcAmt) {
		this.arsEntpDcAmt = arsEntpDcAmt;
	}
	public String getPriceSeq() {
		return priceSeq;
	}
	public void setPriceSeq(String priceSeq) {
		this.priceSeq = priceSeq;
	}
	public long getLumpSumDcAmt() {
		return lumpSumDcAmt;
	}
	public void setLumpSumDcAmt(long lumpSumDcAmt) {
		this.lumpSumDcAmt = lumpSumDcAmt;
	}
	public double getLumpSumOwnDcAmt() {
		return lumpSumOwnDcAmt;
	}
	public void setLumpSumOwnDcAmt(double lumpSumOwnDcAmt) {
		this.lumpSumOwnDcAmt = lumpSumOwnDcAmt;
	}
	public double getLumpSumEntpDcAmt() {
		return lumpSumEntpDcAmt;
	}
	public void setLumpSumEntpDcAmt(double lumpSumEntpDcAmt) {
		this.lumpSumEntpDcAmt = lumpSumEntpDcAmt;
	}
	
	
}
