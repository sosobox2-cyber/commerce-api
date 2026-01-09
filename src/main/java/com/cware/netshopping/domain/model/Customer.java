package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Customer extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String custNo;
	private String custName;
	private String ename;
	private String custName1;
	private String custName2;
	private String custName3;
	private String sex;
	private String birthdayYn;
	private String birthday;
	private String weddingYn;
	private Timestamp weddingDate;
	private String emYn;
	private String emNo;
	private String membNo;
	private String memId;
	private Timestamp idInsertDate;
	private String passwd;
	private String passwdHint;
	private String passwdAnswer;
	
	private String residentNo;
	
	
	private String jobCode;
	private String compName;
	private String compDeptname;
	private String country;
	private String emailAddr;
	private String emailYn;
	private String orderEmailYn;
	private String smsYn;
	private String receiveMethod;
	
	private String nominateId;
	private String withdrawalYn;
	private String withdrawalCode;
	private String withdrawalContent;
	private Timestamp withdrawalDate;
	private String remark1V;
	private String remark2V;
	private String remark3V;
	private String emailFlag;
	private String emailBlockCode;
	private Timestamp emailBlockDate;
	
	private String custSource;
	private String orderSmsYn;
	private String nominateYn;
	private String nonAgeYn;
	private String parentName;
	private String parentHp;
	private String parentDi;

	public String getCustNo() { 
		return this.custNo;
	}
	public String getCustName() { 
		return this.custName;
	}
	public String getMembNo() { 
		return this.membNo;
	}
	public String getMemId() { 
		return this.memId;
	}
	public String getPasswd() { 
		return this.passwd;
	}
	public String getPasswdHint() { 
		return this.passwdHint;
	}
	public String getPasswdAnswer() { 
		return this.passwdAnswer;
	}
	public Timestamp getIdInsertDate() { 
		return this.idInsertDate;
	}
	public String getResidentNo() { 
		return this.residentNo;
	}
	public String getSex() { 
		return this.sex;
	}
	public String getEname() { 
		return this.ename;
	}
	public String getBirthdayYn() { 
		return this.birthdayYn;
	}
	public String getBirthday() { 
		return this.birthday;
	}
	public String getWeddingYn() { 
		return this.weddingYn;
	}
	public Timestamp getWeddingDate() { 
		return this.weddingDate;
	}
	public String getJobCode() { 
		return this.jobCode;
	}
	public String getCompName() { 
		return this.compName;
	}
	public String getCompDeptname() { 
		return this.compDeptname;
	}
	public String getCountry() { 
		return this.country;
	}
	public String getEmailAddr() { 
		return this.emailAddr;
	}
	public String getEmailYn() { 
		return this.emailYn;
	}
	public String getOrderEmailYn() { 
		return this.orderEmailYn;
	}
	public String getSmsYn() { 
		return this.smsYn;
	}
	public String getReceiveMethod() { 
		return this.receiveMethod;
	}
	public String getEmNo() { 
		return this.emNo;
	}
	public String getNominateId() { 
		return this.nominateId;
	}
	public String getWithdrawalYn() { 
		return this.withdrawalYn;
	}
	public String getWithdrawalCode() { 
		return this.withdrawalCode;
	}
	public String getWithdrawalContent() { 
		return this.withdrawalContent;
	}
	public Timestamp getWithdrawalDate() { 
		return this.withdrawalDate;
	}
	public String getRemark1V() { 
		return this.remark1V;
	}
	public String getRemark2V() { 
		return this.remark2V;
	}
	public String getRemark3V() { 
		return this.remark3V;
	}
	public String getCustName1() { 
		return this.custName1;
	}
	public String getCustName2() { 
		return this.custName2;
	}
	public String getCustName3() { 
		return this.custName3;
	}
	public String getEmailFlag() { 
		return this.emailFlag;
	}
	public String getEmailBlockCode() { 
		return this.emailBlockCode;
	}
	public Timestamp getEmailBlockDate() { 
		return this.emailBlockDate;
	}
	public String getEmYn() { 
		return this.emYn;
	}
	public String getCustSource() { 
		return this.custSource;
	}
	public String getOrderSmsYn() { 
		return this.orderSmsYn;
	}

	public void setCustNo(String custNo) { 
		this.custNo = custNo;
	}
	public void setCustName(String custName) { 
		this.custName = custName;
	}
	public void setMembNo(String membNo) { 
		this.membNo = membNo;
	}
	public void setMemId(String memId) { 
		this.memId = memId;
	}
	public void setPasswd(String passwd) { 
		this.passwd = passwd;
	}
	public void setPasswdHint(String passwdHint) { 
		this.passwdHint = passwdHint;
	}
	public void setPasswdAnswer(String passwdAnswer) { 
		this.passwdAnswer = passwdAnswer;
	}
	public void setIdInsertDate(Timestamp idInsertDate) { 
		this.idInsertDate = idInsertDate;
	}
	public void setResidentNo(String residentNo) { 
		this.residentNo = residentNo;
	}
	public void setSex(String sex) { 
		this.sex = sex;
	}
	public void setEname(String ename) { 
		this.ename = ename;
	}
	public void setBirthdayYn(String birthdayYn) { 
		this.birthdayYn = birthdayYn;
	}
	public void setBirthday(String birthday) { 
		this.birthday = birthday;
	}
	public void setWeddingYn(String weddingYn) { 
		this.weddingYn = weddingYn;
	}
	public void setWeddingDate(Timestamp weddingDate) { 
		this.weddingDate = weddingDate;
	}
	public void setJobCode(String jobCode) { 
		this.jobCode = jobCode;
	}
	public void setCompName(String compName) { 
		this.compName = compName;
	}
	public void setCompDeptname(String compDeptname) { 
		this.compDeptname = compDeptname;
	}
	public void setCountry(String country) { 
		this.country = country;
	}
	public void setEmailAddr(String emailAddr) { 
		this.emailAddr = emailAddr;
	}
	public void setEmailYn(String emailYn) { 
		this.emailYn = emailYn;
	}
	public void setOrderEmailYn(String orderEmailYn) { 
		this.orderEmailYn = orderEmailYn;
	}
	public void setSmsYn(String smsYn) { 
		this.smsYn = smsYn;
	}
	public void setReceiveMethod(String receiveMethod) { 
		this.receiveMethod = receiveMethod;
	}
	public void setEmNo(String emNo) { 
		this.emNo = emNo;
	}
	public void setNominateId(String nominateId) { 
		this.nominateId = nominateId;
	}
	public void setWithdrawalYn(String withdrawalYn) { 
		this.withdrawalYn = withdrawalYn;
	}
	public void setWithdrawalCode(String withdrawalCode) { 
		this.withdrawalCode = withdrawalCode;
	}
	public void setWithdrawalContent(String withdrawalContent) { 
		this.withdrawalContent = withdrawalContent;
	}
	public void setWithdrawalDate(Timestamp withdrawalDate) { 
		this.withdrawalDate = withdrawalDate;
	}
	public void setRemark1V(String remark1V) { 
		this.remark1V = remark1V;
	}
	public void setRemark2V(String remark2V) { 
		this.remark2V = remark2V;
	}
	public void setRemark3V(String remark3V) { 
		this.remark3V = remark3V;
	}
	public void setCustName1(String custName1) { 
		this.custName1 = custName1;
	}
	public void setCustName2(String custName2) { 
		this.custName2 = custName2;
	}
	public void setCustName3(String custName3) { 
		this.custName3 = custName3;
	}
	public void setEmailFlag(String emailFlag) { 
		this.emailFlag = emailFlag;
	}
	public void setEmailBlockCode(String emailBlockCode) { 
		this.emailBlockCode = emailBlockCode;
	}
	public void setEmailBlockDate(Timestamp emailBlockDate) { 
		this.emailBlockDate = emailBlockDate;
	}
	public void setEmYn(String emYn) { 
		this.emYn = emYn;
	}
	public void setCustSource(String custSource) { 
		this.custSource = custSource;
	}
	public void setOrderSmsYn(String orderSmsYn) { 
		this.orderSmsYn = orderSmsYn;
	}
	public String getNominateYn() {
		return nominateYn;
	}
	public void setNominateYn(String nominateYn) {
		this.nominateYn = nominateYn;
	}
	public void setNonAgeYn(String nonAgeYn) {
		this.nonAgeYn = nonAgeYn;
	}
	public String getNonAgeYn() {
		return nonAgeYn;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentHp(String parentHp) {
		this.parentHp = parentHp;
	}
	public String getParentHp() {
		return parentHp;
	}
	public void setParentDi(String parentDi) {
		this.parentDi = parentDi;
	}
	public String getParentDi() {
		return parentDi;
	}
	
}
