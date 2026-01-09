package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class Paorderm extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String mappingSeq;
	private String paCode;
	private String paGroupCode;
	private String paOrderGb;
	private String paOrderNo;
	private String paOrderSeq;
	private String paShipNo;
	private String paShipSeq;
	private String paClaimNo;
	private String paProcQty;
	private String paDoFlag;
	private Timestamp procDate;
	private String apiResultCode;
	private String apiResultMessage;
	private String orderNo;
	private String orderGSeq;
	private String orderDSeq;
	private String orderWSeq;
	private String createYn;
	private Timestamp createDate;
	private String preCancelYn;
	private String preCancelReason;
	private String remark1V;
	private String remark2V;
	private int remark3N;
	private int remark4N;
	private String remark5V;
	private String remark6V;
	private String outBefClaimGb;
	private String paHoldYn;
	private String changeGoodsCode;
	private String changeGoodsdtCode;
	private String changeFlag;
	private String paHoldCode;
	private String isAll;
	
	
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getMappingSeq() {
		return mappingSeq;
	}
	public void setMappingSeq(String mappingSeq) {
		this.mappingSeq = mappingSeq;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getPaOrderNo() {
		return paOrderNo;
	}
	public void setPaOrderNo(String paOrderNo) {
		this.paOrderNo = paOrderNo;
	}
	public String getPaOrderSeq() {
		return paOrderSeq;
	}
	public void setPaOrderSeq(String paOrderSeq) {
		this.paOrderSeq = paOrderSeq;
	}
	public String getPaShipNo() {
		return paShipNo;
	}
	public void setPaShipNo(String paShipNo) {
		this.paShipNo = paShipNo;
	}
	public String getPaShipSeq() {
		return paShipSeq;
	}
	public void setPaShipSeq(String paShipSeq) {
		this.paShipSeq = paShipSeq;
	}
	public String getPaClaimNo() {
		return paClaimNo;
	}
	public void setPaClaimNo(String paClaimNo) {
		this.paClaimNo = paClaimNo;
	}
	public String getPaProcQty() {
		return paProcQty;
	}
	public void setPaProcQty(String paProcQty) {
		this.paProcQty = paProcQty;
	}
	public String getPaDoFlag() {
		return paDoFlag;
	}
	public void setPaDoFlag(String paDoFlag) {
		this.paDoFlag = paDoFlag;
	}
	public Timestamp getProcDate() {
		return procDate;
	}
	public void setProcDate(Timestamp procDate) {
		this.procDate = procDate;
	}
	public String getApiResultCode() {
		return apiResultCode;
	}
	public void setApiResultCode(String apiResultCode) {
		this.apiResultCode = apiResultCode;
	}
	public String getApiResultMessage() {
		return apiResultMessage;
	}
	public void setApiResultMessage(String apiResultMessage) {
		this.apiResultMessage = apiResultMessage;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderGSeq() {
		return orderGSeq;
	}
	public void setOrderGSeq(String orderGSeq) {
		this.orderGSeq = orderGSeq;
	}
	public String getOrderDSeq() {
		return orderDSeq;
	}
	public void setOrderDSeq(String orderDSeq) {
		this.orderDSeq = orderDSeq;
	}
	public String getOrderWSeq() {
		return orderWSeq;
	}
	public void setOrderWSeq(String orderWSeq) {
		this.orderWSeq = orderWSeq;
	}
	public String getCreateYn() {
		return createYn;
	}
	public void setCreateYn(String createYn) {
		this.createYn = createYn;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public String getPreCancelYn() {
		return preCancelYn;
	}
	public void setPreCancelYn(String preCancelYn) {
		this.preCancelYn = preCancelYn;
	}
	public String getPreCancelReason() {
		return preCancelReason;
	}
	public void setPreCancelReason(String preCancelReason) {
		this.preCancelReason = preCancelReason;
	}
	public String getRemark1V() {
		return remark1V;
	}
	public void setRemark1V(String remark1v) {
		remark1V = remark1v;
	}
	public String getRemark2V() {
		return remark2V;
	}
	public void setRemark2V(String remark2v) {
		remark2V = remark2v;
	}
	public int getRemark3N() {
		return remark3N;
	}
	public void setRemark3N(int remark3n) {
		remark3N = remark3n;
	}
	public int getRemark4N() {
		return remark4N;
	}
	public void setRemark4N(int remark4n) {
		remark4N = remark4n;
	}
	public String getRemark5V() {
		return remark5V;
	}
	public void setRemark5V(String remark5v) {
		remark5V = remark5v;
	}
	public String getRemark6V() {
		return remark6V;
	}
	public void setRemark6V(String remark6v) {
		remark6V = remark6v;
	}
	public String getOutBefClaimGb() {
		return outBefClaimGb;
	}
	public void setOutBefClaimGb(String outBefClaimGb) {
		this.outBefClaimGb = outBefClaimGb;
	}
	public String getPaHoldYn() {
		return paHoldYn;
	}
	public void setPaHoldYn(String paHoldYn) {
		this.paHoldYn = paHoldYn;
	}
	public String getChangeGoodsCode() {
		return changeGoodsCode;
	}
	public void setChangeGoodsCode(String changeGoodsCode) {
		this.changeGoodsCode = changeGoodsCode;
	}
	public String getChangeGoodsdtCode() {
		return changeGoodsdtCode;
	}
	public void setChangeGoodsdtCode(String changeGoodsdtCode) {
		this.changeGoodsdtCode = changeGoodsdtCode;
	}
	public String getChangeFlag() {
		return changeFlag;
	}
	public void setChangeFlag(String changeFlag) {
		this.changeFlag = changeFlag;
	}
	public String getPaHoldCode() {
	    	return paHoldCode;
	}
	public void setPaHoldCode(String paHoldCode) {
	    	this.paHoldCode = paHoldCode;
	}
	public String getIsAll() {
		return isAll;
	}
	public void setIsAll(String isAll) {
		this.isAll = isAll;
	}
	
	
}
