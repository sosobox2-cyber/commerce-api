package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Claimdt extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String orderNo;
	private String orderGSeq;
	private String orderDSeq;
	private String orderWSeq;
	private String custNo;
	private String receiverSeq;
	private Timestamp claimDate;
	private String claimGb;
	private String doFlag;
	private String claimCode;
	private String exchPair;
	private String goodsGb;
	private String mediaGb;
	private String mediaCode;
	private String goodsCode;
	private String goodsdtCode;
	private String goodsdtInfo;
	private double salePrice;
	private long claimQty;
	private long syscancel;
	private long syslast;
	private double claimAmt;
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
	private double syslastDcGoods;
	private double syslastDcMemb;
	private double syslastDcDiv;
	private double syslastDcCard;
	private double syslastDc;
	private double syslastAmt;
	private double syslastNet;
	private double syslastVat;
	private double vatRate;
	private String mdCode;
	private String saleYn;
	private String whCode;
	private String delyType;
	private String delyGb;
	private String custDelyFlag;
	private Timestamp firstPlanDate;
	private Timestamp outPlanDate;
	private String stockFlag;
	private String stockKey;
	private Timestamp delyHopeDate;
	private String delyHopeYn;
	private String delyHopeTime;
	private String preoutGb;
	private String goodsSelectNo;
	private String singleDueGb;
	private String packYn;
	private String anonyYn;
	private String msg;
	private String msgNote;
	private String happyCardYn;
	private double saveamt;
	private String saveamtGb;
	private String repayGb;
	private Timestamp repayDate;
	private String repayId;
	private String promoNo1;
	private String promoNo2;
	private String setGoodsCode;
	private String setMdCode;
	private String slipYn;
	private String asNo;
	private String remark1V;
	private String remark2V;
	private long remark3N;
	private long remark4N;
	private String remark5V;
	private String remark6V;
	private String entpslipNo;
	private String orderMedia;
	private String lastProcId;
	private Timestamp lastProcDate;
	private String doFlagOrg;
	private String claimDesc;
	private double rsalePaAmt;
	private String slipNo;
	
	private String csLgroup;
	private String csMgroup;
	private String csSgroup;
	private String csLmsCode;
	
	private String standardType; //기준유형  - 기준외 :0 ,  기준내 :1
	private String shpfeeYn; //배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2
	
	
	

	public String getStandardType() {
		return standardType;
	}
	public void setStandardType(String standardType) {
		this.standardType = standardType;
	}
	public String getShpfeeYn() {
		return shpfeeYn;
	}
	public void setShpfeeYn(String shpfeeYn) {
		this.shpfeeYn = shpfeeYn;
	}
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
	public Timestamp getClaimDate() { 
		return this.claimDate;
	}
	public String getClaimGb() { 
		return this.claimGb;
	}
	public String getDoFlag() { 
		return this.doFlag;
	}
	public String getClaimCode() { 
		return this.claimCode;
	}
	public String getExchPair() { 
		return this.exchPair;
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
	public long getClaimQty() { 
		return this.claimQty;
	}
	public long getSyscancel() { 
		return this.syscancel;
	}
	public long getSyslast() { 
		return this.syslast;
	}
	public double getClaimAmt() { 
		return this.claimAmt;
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
	public double getRsaleNet() { 
		return this.rsaleNet;
	}
	public double getRsaleVat() { 
		return this.rsaleVat;
	}
	public double getSyslastDcGoods() { 
		return this.syslastDcGoods;
	}
	public double getSyslastDcMemb() { 
		return this.syslastDcMemb;
	}
	public double getSyslastDcDiv() { 
		return this.syslastDcDiv;
	}
	public double getSyslastDcCard() { 
		return this.syslastDcCard;
	}
	public double getSyslastDc() { 
		return this.syslastDc;
	}
	public double getSyslastAmt() { 
		return this.syslastAmt;
	}
	public double getSyslastNet() { 
		return this.syslastNet;
	}
	public double getSyslastVat() { 
		return this.syslastVat;
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
	public String getCustDelyFlag() { 
		return this.custDelyFlag;
	}
	public Timestamp getFirstPlanDate() { 
		return this.firstPlanDate;
	}
	public Timestamp getOutPlanDate() { 
		return this.outPlanDate;
	}
	public String getStockFlag() { 
		return this.stockFlag;
	}
	public String getStockKey() { 
		return this.stockKey;
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
	public String getGoodsSelectNo() { 
		return this.goodsSelectNo;
	}
	public String getSingleDueGb() { 
		return this.singleDueGb;
	}
	public String getPackYn() { 
		return this.packYn;
	}
	public String getAnonyYn() { 
		return this.anonyYn;
	}
	public String getMsg() { 
		return this.msg;
	}
	public String getMsgNote() { 
		return this.msgNote;
	}
	public String getHappyCardYn() { 
		return this.happyCardYn;
	}
	public double getSaveamt() { 
		return this.saveamt;
	}
	public String getSaveamtGb() { 
		return this.saveamtGb;
	}
	public String getRepayGb() { 
		return this.repayGb;
	}
	public Timestamp getRepayDate() { 
		return this.repayDate;
	}
	public String getRepayId() { 
		return this.repayId;
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
	public String getSlipYn() { 
		return this.slipYn;
	}
	public String getAsNo() { 
		return this.asNo;
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
	public String getEntpslipNo() { 
		return this.entpslipNo;
	}
	public String getOrderMedia() { 
		return this.orderMedia;
	}
	public String getLastProcId() { 
		return this.lastProcId;
	}
	public Timestamp getLastProcDate() { 
		return this.lastProcDate;
	}
	public String getDoFlagOrg() { 
		return this.doFlagOrg;
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
	public void setClaimDate(Timestamp claimDate) { 
		this.claimDate = claimDate;
	}
	public void setClaimGb(String claimGb) { 
		this.claimGb = claimGb;
	}
	public void setDoFlag(String doFlag) { 
		this.doFlag = doFlag;
	}
	public void setClaimCode(String claimCode) { 
		this.claimCode = claimCode;
	}
	public void setExchPair(String exchPair) { 
		this.exchPair = exchPair;
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
	public void setClaimQty(long claimQty) { 
		this.claimQty = claimQty;
	}
	public void setSyscancel(long syscancel) { 
		this.syscancel = syscancel;
	}
	public void setSyslast(long syslast) { 
		this.syslast = syslast;
	}
	public void setClaimAmt(double claimAmt) { 
		this.claimAmt = claimAmt;
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
	public void setRsaleNet(double rsaleNet) { 
		this.rsaleNet = rsaleNet;
	}
	public void setRsaleVat(double rsaleVat) { 
		this.rsaleVat = rsaleVat;
	}
	public void setSyslastDcGoods(double syslastDcGoods) { 
		this.syslastDcGoods = syslastDcGoods;
	}
	public void setSyslastDcMemb(double syslastDcMemb) { 
		this.syslastDcMemb = syslastDcMemb;
	}
	public void setSyslastDcDiv(double syslastDcDiv) { 
		this.syslastDcDiv = syslastDcDiv;
	}
	public void setSyslastDcCard(double syslastDcCard) { 
		this.syslastDcCard = syslastDcCard;
	}
	public void setSyslastDc(double syslastDc) { 
		this.syslastDc = syslastDc;
	}
	public void setSyslastAmt(double syslastAmt) { 
		this.syslastAmt = syslastAmt;
	}
	public void setSyslastNet(double syslastNet) { 
		this.syslastNet = syslastNet;
	}
	public void setSyslastVat(double syslastVat) { 
		this.syslastVat = syslastVat;
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
	public void setCustDelyFlag(String custDelyFlag) { 
		this.custDelyFlag = custDelyFlag;
	}
	public void setFirstPlanDate(Timestamp firstPlanDate) { 
		this.firstPlanDate = firstPlanDate;
	}
	public void setOutPlanDate(Timestamp outPlanDate) { 
		this.outPlanDate = outPlanDate;
	}
	public void setStockFlag(String stockFlag) { 
		this.stockFlag = stockFlag;
	}
	public void setStockKey(String stockKey) { 
		this.stockKey = stockKey;
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
	public void setGoodsSelectNo(String goodsSelectNo) { 
		this.goodsSelectNo = goodsSelectNo;
	}
	public void setSingleDueGb(String singleDueGb) { 
		this.singleDueGb = singleDueGb;
	}
	public void setPackYn(String packYn) { 
		this.packYn = packYn;
	}
	public void setAnonyYn(String anonyYn) { 
		this.anonyYn = anonyYn;
	}
	public void setMsg(String msg) { 
		this.msg = msg;
	}
	public void setMsgNote(String msgNote) { 
		this.msgNote = msgNote;
	}
	public void setHappyCardYn(String happyCardYn) { 
		this.happyCardYn = happyCardYn;
	}
	public void setSaveamt(double saveamt) { 
		this.saveamt = saveamt;
	}
	public void setSaveamtGb(String saveamtGb) { 
		this.saveamtGb = saveamtGb;
	}
	public void setRepayGb(String repayGb) { 
		this.repayGb = repayGb;
	}
	public void setRepayDate(Timestamp repayDate) { 
		this.repayDate = repayDate;
	}
	public void setRepayId(String repayId) { 
		this.repayId = repayId;
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
	public void setSlipYn(String slipYn) { 
		this.slipYn = slipYn;
	}
	public void setAsNo(String asNo) { 
		this.asNo = asNo;
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
	public void setEntpslipNo(String entpslipNo) { 
		this.entpslipNo = entpslipNo;
	}
	public void setOrderMedia(String orderMedia) { 
		this.orderMedia = orderMedia;
	}
	public void setLastProcId(String lastProcId) { 
		this.lastProcId = lastProcId;
	}
	public void setLastProcDate(Timestamp lastProcDate) { 
		this.lastProcDate = lastProcDate;
	}
	public void setDoFlagOrg(String doFlagOrg) { 
		this.doFlagOrg = doFlagOrg;
	}
	public String getClaimDesc() {
		return claimDesc;
	}
	public void setClaimDesc(String claimDesc) {
		this.claimDesc = claimDesc;
	}
	public double getRsalePaAmt() {
		return rsalePaAmt;
	}
	public void setRsalePaAmt(double rsalePaAmt) {
		this.rsalePaAmt = rsalePaAmt;
	}
	public String getSlipNo() {
		return slipNo;
	}
	public void setSlipNo(String slipNo) {
		this.slipNo = slipNo;
	}
	
}
