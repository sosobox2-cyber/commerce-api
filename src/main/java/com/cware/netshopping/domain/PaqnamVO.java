package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.Paqnam;

public class PaqnamVO extends Paqnam {

    private static final long serialVersionUID = 1L;
    
    //제휴상담상세순번
    private String paCounselDtSeq;
    //제목
    private String title;
    //처리내역
    private String procNote;
    //상담고객번호
    private String custNo;
    //상품코드
    private String goodsCode;
    //단품코드
    private String goodsDtCode;
    //업체코드
    private String entpCode;
    //상담중분류코드
    private String outMgroupCode;
    //처리일시
    private Timestamp procDate;
    //처리자
    private String procId;
    
    private String paApiKey;
    
    private String originPaCounselNo;
    private String csLgroup;
    private String csMgroup;
    private String csSgroup;
    private String csLmsCode;
    private String csMgroupName;
    private String csSgroupName;
    
    private String status;
    
    
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
	public String getPaCounselDtSeq() {
		return paCounselDtSeq;
	}
	public void setPaCounselDtSeq(String paCounselDtSeq) {
		this.paCounselDtSeq = paCounselDtSeq;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProcNote() {
		return procNote;
	}
	public void setProcNote(String procNote) {
		this.procNote = procNote;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsDtCode() {
		return goodsDtCode;
	}
	public void setGoodsDtCode(String goodsDtCode) {
		this.goodsDtCode = goodsDtCode;
	}
	public String getEntpCode() {
		return entpCode;
	}
	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}
	public String getOutMgroupCode() {
		return outMgroupCode;
	}
	public void setOutMgroupCode(String outMgroupCode) {
		this.outMgroupCode = outMgroupCode;
	}
	public String getPaApiKey() {
		return paApiKey;
	}
	public void setPaApiKey(String paApiKey) {
		this.paApiKey = paApiKey;
	}
	public Timestamp getProcDate() {
	    return procDate;
	}
	public String getProcId() {
	    return procId;
	}
	public void setProcDate(Timestamp procDate) {
	    this.procDate = procDate;
	}
	public void setProcId(String procId) {
	    this.procId = procId;
	}
	public String getOriginPaCounselNo() {
		return originPaCounselNo;
	}
	public void setOriginPaCounselNo(String originPaCounselNo) {
		this.originPaCounselNo = originPaCounselNo;
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
	public String getCsMgroupName() {
		return csMgroupName;
	}
	public void setCsMgroupName(String csMgroupName) {
		this.csMgroupName = csMgroupName;
	}
	public String getCsSgroupName() {
		return csSgroupName;
	}
	public void setCsSgroupName(String csSgroupName) {
		this.csSgroupName = csSgroupName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
}
