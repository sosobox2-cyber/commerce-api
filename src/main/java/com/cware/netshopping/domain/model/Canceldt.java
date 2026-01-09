package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class Canceldt extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String orderNo;
	private String orderGSeq;
	private String orderDSeq;
	private String orderWSeq;
	private String custNo;
	private String receiverSeq;
	private Timestamp cancelDate;
	private String cancelGb;
	private String doFlag;
	private String cancelCode;
	private String goodsGb;
	private String mediaGb;
	private String mediaCode;
	private String goodsCode;
	private String goodsdtCode;
	private String goodsdtInfo;
	private double salePrice;
	private double buyPrice;
	private long cancelQty;
	private double cancelAmt;
	private double dcRateGoods;
	private double dcRate;
	private double dcAmtGoods;
	private double dcAmtMemb;
	private double dcAmtDiv;
	private double dcAmtCard;
	private double dcAmt;
	private double rsaleAmt;
	private double rsaleNet;
	private double rsaleVat;
	private double vatRate;
	private String mdCode;
	private String saleYn;
	private String whCode;
	private String delyType;
	private String delyGb;
	private Timestamp delyHopeDate;
	private String delyHopeYn;
	private String delyHopeTime;
	private String preoutGb;
	private String singleDueGb;
	private double saveamt;
	private String saveamtGb;
	private String promoNo1;
	private String promoNo2;
	private String setGoodsCode;
	private String setMdCode;
	//categoryCode
	//areaCode
	private String remark1V;
	private String remark2V;
	private long remark3N;
	private long remark4N;
	private String remark5V;
	private String remark6V;
	private String lastProcId;
	
	private Timestamp lastProcDate;
	//lastProcDate
	
	private String csLgroup;
	private String csMgroup;
	private String csSgroup;
	private String csLmsCode;

	private double ohPoint;
	private double spoint_saveamt;
	private double rsalePaAmt;
	
	
	public String getCsLgroup() {
		return csLgroup;
	}
	public void setCsLgroup(String csLgroup) {
		this.csLgroup = csLgroup;
	}
	public String getCsMgroup() {
		return csMgroup;
	}
	public void setCsMgroup(String csMgroup) {
		this.csMgroup = csMgroup;
	}
	public String getCsSgroup() {
		return csSgroup;
	}
	public void setCsSgroup(String csSgroup) {
		this.csSgroup = csSgroup;
	}
	public String getCsLmsCode() {
		return csLmsCode;
	}
	public void setCsLmsCode(String csLmsCode) {
		this.csLmsCode = csLmsCode;
	}

	public double getOhPoint() {
		return ohPoint;
	}
	public void setOhPoint(double ohPoint) {
		this.ohPoint = ohPoint;
	}
	public String getOrderNo() { 
		return this.orderNo;
	}
	public String getOrderGSeq() { 
		return this.orderGSeq;
	}
	public String getOrderDSeq() { 
		return this.orderDSeq;
	}
	public String getOrderWSeq() { 
		return this.orderWSeq;
	}
	public String getCustNo() { 
		return this.custNo;
	}
	public String getReceiverSeq() { 
		return this.receiverSeq;
	}
	public Timestamp getCancelDate() { 
		return this.cancelDate;
	}
	public String getCancelGb() { 
		return this.cancelGb;
	}
	public String getDoFlag() { 
		return this.doFlag;
	}
	public String getCancelCode() { 
		return this.cancelCode;
	}
	public String getGoodsGb() { 
		return this.goodsGb;
	}
	public String getMediaGb() { 
		return this.mediaGb;
	}
	public String getMediaCode() { 
		return this.mediaCode;
	}
	public String getGoodsCode() { 
		return this.goodsCode;
	}
	public String getGoodsdtCode() { 
		return this.goodsdtCode;
	}
	public String getGoodsdtInfo() { 
		return this.goodsdtInfo;
	}
	public double getSalePrice() { 
		return this.salePrice;
	}
	public long getCancelQty() { 
		return this.cancelQty;
	}
	public double getCancelAmt() { 
		return this.cancelAmt;
	}
	public double getDcRateGoods() { 
		return this.dcRateGoods;
	}
	public double getDcRate() { 
		return this.dcRate;
	}
	public double getDcAmtGoods() { 
		return this.dcAmtGoods;
	}
	public double getDcAmtMemb() { 
		return this.dcAmtMemb;
	}
	public double getDcAmtDiv() { 
		return this.dcAmtDiv;
	}
	public double getDcAmtCard() { 
		return this.dcAmtCard;
	}
	public double getDcAmt() { 
		return this.dcAmt;
	}
	public double getRsaleAmt() { 
		return this.rsaleAmt;
	}
	public double getVatRate() { 
		return this.vatRate;
	}
	public String getMdCode() { 
		return this.mdCode;
	}
	public String getSaleYn() { 
		return this.saleYn;
	}
	public String getWhCode() { 
		return this.whCode;
	}
	public String getDelyType() { 
		return this.delyType;
	}
	public String getDelyGb() { 
		return this.delyGb;
	}
	public Timestamp getDelyHopeDate() { 
		return this.delyHopeDate;
	}
	public String getDelyHopeYn() { 
		return this.delyHopeYn;
	}
	public String getDelyHopeTime() { 
		return this.delyHopeTime;
	}
	public String getPreoutGb() { 
		return this.preoutGb;
	}
	public String getSingleDueGb() { 
		return this.singleDueGb;
	}
	public double getSaveamt() { 
		return this.saveamt;
	}
	public String getSaveamtGb() { 
		return this.saveamtGb;
	}
	public String getPromoNo1() { 
		return this.promoNo1;
	}
	public String getPromoNo2() { 
		return this.promoNo2;
	}
	public String getSetGoodsCode() { 
		return this.setGoodsCode;
	}
	public String getSetMdCode() { 
		return this.setMdCode;
	}
	public String getRemark1V() { 
		return this.remark1V;
	}
	public String getRemark2V() { 
		return this.remark2V;
	}
	public long getRemark3N() { 
		return this.remark3N;
	}
	public long getRemark4N() { 
		return this.remark4N;
	}
	public String getRemark5V() { 
		return this.remark5V;
	}
	public String getRemark6V() { 
		return this.remark6V;
	}
	public double getRsaleNet() { 
		return this.rsaleNet;
	}
	public double getRsaleVat() { 
		return this.rsaleVat;
	}
	public String getLastProcId() { 
		return this.lastProcId;
	}
	public Timestamp getLastProcDate() { 
		return this.lastProcDate;
	}
	public double getBuyPrice() { 
		return this.buyPrice;
	}

	public void setOrderNo(String orderNo) { 
		this.orderNo = orderNo;
	}
	public void setOrderGSeq(String orderGSeq) { 
		this.orderGSeq = orderGSeq;
	}
	public void setOrderDSeq(String orderDSeq) { 
		this.orderDSeq = orderDSeq;
	}
	public void setOrderWSeq(String orderWSeq) { 
		this.orderWSeq = orderWSeq;
	}
	public void setCustNo(String custNo) { 
		this.custNo = custNo;
	}
	public void setReceiverSeq(String receiverSeq) { 
		this.receiverSeq = receiverSeq;
	}
	public void setCancelDate(Timestamp cancelDate) { 
		this.cancelDate = cancelDate;
	}
	public void setCancelGb(String cancelGb) { 
		this.cancelGb = cancelGb;
	}
	public void setDoFlag(String doFlag) { 
		this.doFlag = doFlag;
	}
	public void setCancelCode(String cancelCode) { 
		this.cancelCode = cancelCode;
	}
	public void setGoodsGb(String goodsGb) { 
		this.goodsGb = goodsGb;
	}
	public void setMediaGb(String mediaGb) { 
		this.mediaGb = mediaGb;
	}
	public void setMediaCode(String mediaCode) { 
		this.mediaCode = mediaCode;
	}
	public void setGoodsCode(String goodsCode) { 
		this.goodsCode = goodsCode;
	}
	public void setGoodsdtCode(String goodsdtCode) { 
		this.goodsdtCode = goodsdtCode;
	}
	public void setGoodsdtInfo(String goodsdtInfo) { 
		this.goodsdtInfo = goodsdtInfo;
	}
	public void setSalePrice(double salePrice) { 
		this.salePrice = salePrice;
	}
	public void setCancelQty(long cancelQty) { 
		this.cancelQty = cancelQty;
	}
	public void setCancelAmt(double cancelAmt) { 
		this.cancelAmt = cancelAmt;
	}
	public void setDcRateGoods(double dcRateGoods) { 
		this.dcRateGoods = dcRateGoods;
	}
	public void setDcRate(double dcRate) { 
		this.dcRate = dcRate;
	}
	public void setDcAmtGoods(double dcAmtGoods) { 
		this.dcAmtGoods = dcAmtGoods;
	}
	public void setDcAmtMemb(double dcAmtMemb) { 
		this.dcAmtMemb = dcAmtMemb;
	}
	public void setDcAmtDiv(double dcAmtDiv) { 
		this.dcAmtDiv = dcAmtDiv;
	}
	public void setDcAmtCard(double dcAmtCard) { 
		this.dcAmtCard = dcAmtCard;
	}
	public void setDcAmt(double dcAmt) { 
		this.dcAmt = dcAmt;
	}
	public void setRsaleAmt(double rsaleAmt) { 
		this.rsaleAmt = rsaleAmt;
	}
	public void setVatRate(double vatRate) { 
		this.vatRate = vatRate;
	}
	public void setMdCode(String mdCode) { 
		this.mdCode = mdCode;
	}
	public void setSaleYn(String saleYn) { 
		this.saleYn = saleYn;
	}
	public void setWhCode(String whCode) { 
		this.whCode = whCode;
	}
	public void setDelyType(String delyType) { 
		this.delyType = delyType;
	}
	public void setDelyGb(String delyGb) { 
		this.delyGb = delyGb;
	}
	public void setDelyHopeDate(Timestamp delyHopeDate) { 
		this.delyHopeDate = delyHopeDate;
	}
	public void setDelyHopeYn(String delyHopeYn) { 
		this.delyHopeYn = delyHopeYn;
	}
	public void setDelyHopeTime(String delyHopeTime) { 
		this.delyHopeTime = delyHopeTime;
	}
	public void setPreoutGb(String preoutGb) { 
		this.preoutGb = preoutGb;
	}
	public void setSingleDueGb(String singleDueGb) { 
		this.singleDueGb = singleDueGb;
	}
	public void setSaveamt(double saveamt) { 
		this.saveamt = saveamt;
	}
	public void setSaveamtGb(String saveamtGb) { 
		this.saveamtGb = saveamtGb;
	}
	public void setPromoNo1(String promoNo1) { 
		this.promoNo1 = promoNo1;
	}
	public void setPromoNo2(String promoNo2) { 
		this.promoNo2 = promoNo2;
	}
	public void setSetGoodsCode(String setGoodsCode) { 
		this.setGoodsCode = setGoodsCode;
	}
	public void setSetMdCode(String setMdCode) { 
		this.setMdCode = setMdCode;
	}
	public void setRemark1V(String remark1V) { 
		this.remark1V = remark1V;
	}
	public void setRemark2V(String remark2V) { 
		this.remark2V = remark2V;
	}
	public void setRemark3N(long remark3N) { 
		this.remark3N = remark3N;
	}
	public void setRemark4N(long remark4N) { 
		this.remark4N = remark4N;
	}
	public void setRemark5V(String remark5V) { 
		this.remark5V = remark5V;
	}
	public void setRemark6V(String remark6V) { 
		this.remark6V = remark6V;
	}
	public void setRsaleNet(double rsaleNet) { 
		this.rsaleNet = rsaleNet;
	}
	public void setRsaleVat(double rsaleVat) { 
		this.rsaleVat = rsaleVat;
	}
	public void setLastProcId(String lastProcId) { 
		this.lastProcId = lastProcId;
	}
	public void setLastProcDate(Timestamp lastProcDate) { 
		this.lastProcDate = lastProcDate;
	}
	public void setBuyPrice(double buyPrice) { 
		this.buyPrice = buyPrice;
	}
	public double getSpoint_saveamt() {
		return spoint_saveamt;
	}
	public void setSpoint_saveamt(double spoint_saveamt) {
		this.spoint_saveamt = spoint_saveamt;
	}
	public double getRsalePaAmt() {
		return rsalePaAmt;
	}
	public void setRsalePaAmt(double rsalePaAmt) {
		this.rsalePaAmt = rsalePaAmt;
	}
	
}
