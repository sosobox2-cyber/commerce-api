package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class Custcounselm extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String counselSeq;
	private String custNo;
	private String doFlag;
	private String refNo1;
	private String refNo2;
	private String refNo3;
	private String refNo4;
	private String outLgroupCode;
	private String outMgroupCode;
	private String outSgroupCode;
	private String goodsCode;
	private String goodsdtCode;
	private String claimNo;
	private String tel;
	private String ddd;
	private String tel1;
	private String tel2;
	private String tel3;
	private String wildYn;
	private String quickYn;
	private Timestamp hcReqDate;
	private String autofaxYn;
	private String emailYn;
	private String fromEmailAddr;
	private String ipAddr;
	private long searchCnt;
	private long recoCnt;
	private String deleteYn;
	private String remark;
	private String refId1;
	private String refId2;
	private String csSendYn;
	private String quickEndYn;
	private String oldCounselSeq;
	private String oldOutLgroupCode;
	private String oldOutMgroupCode;
	private String oldOutSgroupCode;
	private String procId;
	private Timestamp procDate;
	private String sendEntpCode;
	private String sendEmpno;
	private String counselMedia;
	private String csLgroup;
	private String csMgroup;
	private String csSgroup;
	private String csLmsCode;
	private String quickTransYn;
	private String transFlag;
	
	public String getCounselSeq() { 
		return this.counselSeq;
	}
	public String getCustNo() { 
		return this.custNo;
	}
	public String getDoFlag() { 
		return this.doFlag;
	}
	public String getRefNo1() { 
		return this.refNo1;
	}
	public String getRefNo2() { 
		return this.refNo2;
	}
	public String getRefNo3() { 
		return this.refNo3;
	}
	public String getRefNo4() { 
		return this.refNo4;
	}
	public String getOutLgroupCode() { 
		return this.outLgroupCode;
	}
	public String getOutMgroupCode() { 
		return this.outMgroupCode;
	}
	public String getGoodsCode() { 
		return this.goodsCode;
	}
	public String getGoodsdtCode() { 
		return this.goodsdtCode;
	}
	public String getClaimNo() { 
		return this.claimNo;
	}
	public String getTel() { 
		return this.tel;
	}
	public String getDdd() { 
		return this.ddd;
	}
	public String getTel1() { 
		return this.tel1;
	}
	public String getTel2() { 
		return this.tel2;
	}
	public String getTel3() { 
		return this.tel3;
	}
	public String getWildYn() { 
		return this.wildYn;
	}
	public String getQuickYn() { 
		return this.quickYn;
	}
	public Timestamp getHcReqDate() { 
		return this.hcReqDate;
	}
	public String getAutofaxYn() { 
		return this.autofaxYn;
	}
	public String getEmailYn() { 
		return this.emailYn;
	}
	public String getFromEmailAddr() { 
		return this.fromEmailAddr;
	}
	public String getIpAddr() { 
		return this.ipAddr;
	}
	public long getSearchCnt() { 
		return this.searchCnt;
	}
	public long getRecoCnt() { 
		return this.recoCnt;
	}
	public String getDeleteYn() { 
		return this.deleteYn;
	}
	public String getRemark() { 
		return this.remark;
	}
	public String getRefId1() { 
		return this.refId1;
	}
	public String getRefId2() { 
		return this.refId2;
	}
	public String getCsSendYn() { 
		return this.csSendYn;
	}
	public String getQuickEndYn() { 
		return this.quickEndYn;
	}
	public String getOldCounselSeq() { 
		return this.oldCounselSeq;
	}
	public String getOldOutLgroupCode() { 
		return this.oldOutLgroupCode;
	}
	public String getOldOutMgroupCode() { 
		return this.oldOutMgroupCode;
	}
	public String getProcId() { 
		return this.procId;
	}
	public Timestamp getProcDate() { 
		return this.procDate;
	}
	public String getSendEntpCode() { 
		return this.sendEntpCode;
	}
	public String getSendEmpno() { 
		return this.sendEmpno;
	}

	public void setCounselSeq(String counselSeq) { 
		this.counselSeq = counselSeq;
	}
	public void setCustNo(String custNo) { 
		this.custNo = custNo;
	}
	public void setDoFlag(String doFlag) { 
		this.doFlag = doFlag;
	}
	public void setRefNo1(String refNo1) { 
		this.refNo1 = refNo1;
	}
	public void setRefNo2(String refNo2) { 
		this.refNo2 = refNo2;
	}
	public void setRefNo3(String refNo3) { 
		this.refNo3 = refNo3;
	}
	public void setRefNo4(String refNo4) { 
		this.refNo4 = refNo4;
	}
	public void setOutLgroupCode(String outLgroupCode) { 
		this.outLgroupCode = outLgroupCode;
	}
	public void setOutMgroupCode(String outMgroupCode) { 
		this.outMgroupCode = outMgroupCode;
	}
	public void setGoodsCode(String goodsCode) { 
		this.goodsCode = goodsCode;
	}
	public void setGoodsdtCode(String goodsdtCode) { 
		this.goodsdtCode = goodsdtCode;
	}
	public void setClaimNo(String claimNo) { 
		this.claimNo = claimNo;
	}
	public void setTel(String tel) { 
		this.tel = tel;
	}
	public void setDdd(String ddd) { 
		this.ddd = ddd;
	}
	public void setTel1(String tel1) { 
		this.tel1 = tel1;
	}
	public void setTel2(String tel2) { 
		this.tel2 = tel2;
	}
	public void setTel3(String tel3) { 
		this.tel3 = tel3;
	}
	public void setWildYn(String wildYn) { 
		this.wildYn = wildYn;
	}
	public void setQuickYn(String quickYn) { 
		this.quickYn = quickYn;
	}
	public void setHcReqDate(Timestamp hcReqDate) { 
		this.hcReqDate = hcReqDate;
	}
	public void setAutofaxYn(String autofaxYn) { 
		this.autofaxYn = autofaxYn;
	}
	public void setEmailYn(String emailYn) { 
		this.emailYn = emailYn;
	}
	public void setFromEmailAddr(String fromEmailAddr) { 
		this.fromEmailAddr = fromEmailAddr;
	}
	public void setIpAddr(String ipAddr) { 
		this.ipAddr = ipAddr;
	}
	public void setSearchCnt(long searchCnt) { 
		this.searchCnt = searchCnt;
	}
	public void setRecoCnt(long recoCnt) { 
		this.recoCnt = recoCnt;
	}
	public void setDeleteYn(String deleteYn) { 
		this.deleteYn = deleteYn;
	}
	public void setRemark(String remark) { 
		this.remark = remark;
	}
	public void setRefId1(String refId1) { 
		this.refId1 = refId1;
	}
	public void setRefId2(String refId2) { 
		this.refId2 = refId2;
	}
	public void setCsSendYn(String csSendYn) { 
		this.csSendYn = csSendYn;
	}
	public void setQuickEndYn(String quickEndYn) { 
		this.quickEndYn = quickEndYn;
	}
	public void setOldCounselSeq(String oldCounselSeq) { 
		this.oldCounselSeq = oldCounselSeq;
	}
	public void setOldOutLgroupCode(String oldOutLgroupCode) { 
		this.oldOutLgroupCode = oldOutLgroupCode;
	}
	public void setOldOutMgroupCode(String oldOutMgroupCode) { 
		this.oldOutMgroupCode = oldOutMgroupCode;
	}
	public void setProcId(String procId) { 
		this.procId = procId;
	}
	public void setProcDate(Timestamp procDate) { 
		this.procDate = procDate;
	}
	public void setSendEntpCode(String sendEntpCode) { 
		this.sendEntpCode = sendEntpCode;
	}
	public void setSendEmpno(String sendEmpno) { 
		this.sendEmpno = sendEmpno;
	}
	public String getOutSgroupCode() {
		return outSgroupCode;
	}
	public void setOutSgroupCode(String outSgroupCode) {
		this.outSgroupCode = outSgroupCode;
	}
	public String getOldOutSgroupCode() {
		return oldOutSgroupCode;
	}
	public void setOldOutSgroupCode(String oldOutSgroupCode) {
		this.oldOutSgroupCode = oldOutSgroupCode;
	}
	public String getCounselMedia() {
		return counselMedia;
	}
	public void setCounselMedia(String counselMedia) {
		this.counselMedia = counselMedia;
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
	public String getQuickTransYn() {
		return quickTransYn;
	}
	public void setQuickTransYn(String quickTransYn) {
		this.quickTransYn = quickTransYn;
	}
	public String getTransFlag() {
		return transFlag;
	}
	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}
}
