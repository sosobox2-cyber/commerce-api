package com.cware.netshopping.domain;

import com.cware.framework.core.basic.AbstractVO;

public class OrderClaimVO extends AbstractVO {
	private static final long serialVersionUID = 1L;
	
	private String mappingSeq;
	private String orderNo;
	private String claimGb;
	private String orderGSeq;
	private String orderDSeq;
	private String orderWSeq;
	private int claimQty;
	private String claimCode;
	private String claimDesc;
	private String claimType;
	private String shipcostChargeYn;
	private String returnName;
	private String returnTel;
	private String returnHp;
	private String returnAddr;
	private String receiverName;
	private String receiverTel;
	private String receiverHp;
	private String receiverAddr;
	private String outBefClaimGb;
	private String exchGoodsdtCode;
	private String adminProcYn;
	private String custDelyYn;
	private String returnDelyGb;
	private String returnSlipNo;
	private String localYn;
	private String RcvrMailNo;
	private String RcvrMailNoSeq;
	private String RcvrBaseAddr;
	private String RcvrDtlsAddr;
	private String RcvrTypeAdd;
	private String paOrderNo;
	private String goodsCode;
	private String paGoodsCode;
	private String sameAddr;
	
	
	private String csLgroup;
	private String csMgroup;
	private String csSgroup;
	private String csLmsCode;
	  
	private String standardType; //기준유형  - 기준외 :0 ,  기준내 :1
	private String shpfeeYn;     //배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2
	private Long   shpFeeAmt;    //고객이 부과한 배송비(유상 또는 무상, 무상인경우 과실이 판매자)
	private boolean is20Claim;   //출하지시후 취소건(운송장x)은 True 

	private String msg;
	
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Long getShpFeeAmt() {
		return shpFeeAmt;
	}
	public void setShpFeeAmt(Long shpFeeAmt) {
		this.shpFeeAmt = shpFeeAmt;
	}
	
	public boolean getIsIs20Claim() {
		return is20Claim;
	}
	public void setIs20Claim(boolean is20Claim) {
		this.is20Claim = is20Claim;
	}
	public String getRcvrMailNoSeq() {
		return RcvrMailNoSeq;
	}
	public void setRcvrMailNoSeq(String rcvrMailNoSeq) {
		RcvrMailNoSeq = rcvrMailNoSeq;
	}
	
	public String getRcvrMailNo() {
		return RcvrMailNo;
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
	public void setRcvrMailNo(String rcvrMailNo) {
		RcvrMailNo = rcvrMailNo;
	}
	public String getRcvrBaseAddr() {
		return RcvrBaseAddr;
	}
	public void setRcvrBaseAddr(String rcvrBaseAddr) {
		RcvrBaseAddr = rcvrBaseAddr;
	}
	public String getRcvrDtlsAddr() {
		return RcvrDtlsAddr;
	}
	public void setRcvrDtlsAddr(String rcvrDtlsAddr) {
		RcvrDtlsAddr = rcvrDtlsAddr;
	}
	public String getRcvrTypeAdd() {
		return RcvrTypeAdd;
	}
	public void setRcvrTypeAdd(String rcvrTypeAdd) {
		RcvrTypeAdd = rcvrTypeAdd;
	}
	public String getLocalYn() {
		return localYn;
	}
	public void setLocalYn(String localYn) {
		this.localYn = localYn;
	}
	public String getMappingSeq() {
		return mappingSeq;
	}
	public void setMappingSeq(String mappingSeq) {
		this.mappingSeq = mappingSeq;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getClaimGb() {
		return claimGb;
	}
	public void setClaimGb(String claimGb) {
		this.claimGb = claimGb;
	}
	public String getOrderGSeq() {
		return orderGSeq;
	}
	public void setOrderGSeq(String orderGSeq) {
		this.orderGSeq = orderGSeq;
	}
	public String getOrderWSeq() {
		return orderWSeq;
	}
	public void setOrderWSeq(String orderWSeq) {
		this.orderWSeq = orderWSeq;
	}
	public int getClaimQty() {
		return claimQty;
	}
	public void setClaimQty(int claimQty) {
		this.claimQty = claimQty;
	}
	public String getClaimCode() {
		return claimCode;
	}
	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getShipcostChargeYn() {
		return shipcostChargeYn;
	}
	public void setShipcostChargeYn(String shipcostChargeYn) {
		this.shipcostChargeYn = shipcostChargeYn;
	}
	public String getReturnName() {
		return returnName;
	}
	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}
	public String getReturnTel() {
		return returnTel;
	}
	public void setReturnTel(String returnTel) {
		this.returnTel = returnTel;
	}
	public String getReturnHp() {
		return returnHp;
	}
	public void setReturnHp(String returnHp) {
		this.returnHp = returnHp;
	}
	public String getReturnAddr() {
		return returnAddr;
	}
	public void setReturnAddr(String returnAddr) {
		this.returnAddr = returnAddr;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverTel() {
		return receiverTel;
	}
	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}
	public String getReceiverHp() {
		return receiverHp;
	}
	public void setReceiverHp(String receiverHp) {
		this.receiverHp = receiverHp;
	}
	public String getReceiverAddr() {
		return receiverAddr;
	}
	public void setReceiverAddr(String receiverAddr) {
		this.receiverAddr = receiverAddr;
	}
	public String getOutBefClaimGb() {
		return outBefClaimGb;
	}
	public void setOutBefClaimGb(String outBefClaimGb) {
		this.outBefClaimGb = outBefClaimGb;
	}
	public String getExchGoodsdtCode() {
		return exchGoodsdtCode;
	}
	public void setExchGoodsdtCode(String exchGoodsdtCode) {
		this.exchGoodsdtCode = exchGoodsdtCode;
	}
	public String getOrderDSeq() {
		return orderDSeq;
	}
	public void setOrderDSeq(String orderDSeq) {
		this.orderDSeq = orderDSeq;
	}
	public String getAdminProcYn() {
		return adminProcYn;
	}
	public void setAdminProcYn(String adminProcYn) {
		this.adminProcYn = adminProcYn;
	}
	public String getClaimDesc() {
		return claimDesc;
	}
	public void setClaimDesc(String claimDesc) {
		this.claimDesc = claimDesc;
	}
	public String getCustDelyYn() {
		return custDelyYn;
	}
	public void setCustDelyYn(String custDelyYn) {
		this.custDelyYn = custDelyYn;
	}
	public String getReturnDelyGb() {
		return returnDelyGb;
	}
	public void setReturnDelyGb(String returnDelyGb) {
		this.returnDelyGb = returnDelyGb;
	}
	public String getReturnSlipNo() {
		return returnSlipNo;
	}
	public void setReturnSlipNo(String returnSlipNo) {
		this.returnSlipNo = returnSlipNo;
	}
	public String getPaOrderNo() {
		return paOrderNo;
	}
	public void setPaOrderNo(String paOrderNo) {
		this.paOrderNo = paOrderNo;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getPaGoodsCode() {
		return paGoodsCode;
	}
	public void setPaGoodsCode(String paGoodsCode) {
		this.paGoodsCode = paGoodsCode;
	}
	public String getSameAddr() {
		return sameAddr;
	}
	public void setSameAddr(String sameAddr) {
		this.sameAddr = sameAddr;
	}
	
}
