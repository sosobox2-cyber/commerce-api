package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class Custsystem extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String custNo;
	private String custGb;
	private String membGb;
	private String custWarning;
	private String custChar;
	private double useDeposit;
	private double usePbDeposit;
	private double useSaveamt;
	private double grantSaveamt;
	private double returnSaveamt;
	private double usableSaveamt;
	private double totSaveamt;
	private String dmYn;
	private String dmNoGb;
	private String dmNoId;
	private Timestamp dmNoDate;
	private Timestamp firstOrderDate;
	private Timestamp lastOrderDate;
	private String firstMsaleGb;
	private long totOrderCnt;
	private double totOrderAmt;
	private long totCancelCnt;
	private double totCancelAmt;
	private long totReturnCnt;
	private double totReturnAmt;
	private double membTotAmt;
	private String birthdayMmdd;
	private double totShipCost;
	private double totCanShipCost;
	private String lastConMediaCode;
	private String lastOrderMedia;
	private String lastOrderMediaCode;
	private String insertMedia;
	private String insertMediaCode;
	private Timestamp lastConDate;
	

	public String getCustNo() { 
		return this.custNo;
	}
	public String getCustGb() { 
		return this.custGb;
	}
	public String getMembGb() { 
		return this.membGb;
	}
	public String getCustWarning() { 
		return this.custWarning;
	}
	public String getCustChar() { 
		return this.custChar;
	}
	public double getUseDeposit() { 
		return this.useDeposit;
	}
	public double getUsePbDeposit() { 
		return this.usePbDeposit;
	}
	public double getUseSaveamt() { 
		return this.useSaveamt;
	}
	public double getGrantSaveamt() { 
		return this.grantSaveamt;
	}
	public double getReturnSaveamt() { 
		return this.returnSaveamt;
	}
	public double getUsableSaveamt() { 
		return this.usableSaveamt;
	}
	public double getTotSaveamt() { 
		return this.totSaveamt;
	}
	public String getDmYn() { 
		return this.dmYn;
	}
	public String getDmNoGb() { 
		return this.dmNoGb;
	}
	public String getDmNoId() { 
		return this.dmNoId;
	}
	public Timestamp getDmNoDate() { 
		return this.dmNoDate;
	}
	public Timestamp getFirstOrderDate() { 
		return this.firstOrderDate;
	}
	public Timestamp getLastOrderDate() { 
		return this.lastOrderDate;
	}
	public String getFirstMsaleGb() { 
		return this.firstMsaleGb;
	}
	public long getTotOrderCnt() { 
		return this.totOrderCnt;
	}
	public double getTotOrderAmt() { 
		return this.totOrderAmt;
	}
	public long getTotCancelCnt() { 
		return this.totCancelCnt;
	}
	public double getTotCancelAmt() { 
		return this.totCancelAmt;
	}
	public long getTotReturnCnt() { 
		return this.totReturnCnt;
	}
	public double getTotReturnAmt() { 
		return this.totReturnAmt;
	}
	public double getMembTotAmt() { 
		return this.membTotAmt;
	}
	public String getBirthdayMmdd() { 
		return this.birthdayMmdd;
	}
	public double getTotShipCost() { 
		return this.totShipCost;
	}
	public double getTotCanShipCost() { 
		return this.totCanShipCost;
	}

	public void setCustNo(String custNo) { 
		this.custNo = custNo;
	}
	public void setCustGb(String custGb) { 
		this.custGb = custGb;
	}
	public void setMembGb(String membGb) { 
		this.membGb = membGb;
	}
	public void setCustWarning(String custWarning) { 
		this.custWarning = custWarning;
	}
	public void setCustChar(String custChar) { 
		this.custChar = custChar;
	}
	public void setUseDeposit(double useDeposit) { 
		this.useDeposit = useDeposit;
	}
	public void setUsePbDeposit(double usePbDeposit) { 
		this.usePbDeposit = usePbDeposit;
	}
	public void setUseSaveamt(double useSaveamt) { 
		this.useSaveamt = useSaveamt;
	}
	public void setGrantSaveamt(double grantSaveamt) { 
		this.grantSaveamt = grantSaveamt;
	}
	public void setReturnSaveamt(double returnSaveamt) { 
		this.returnSaveamt = returnSaveamt;
	}
	public void setUsableSaveamt(double usableSaveamt) { 
		this.usableSaveamt = usableSaveamt;
	}
	public void setTotSaveamt(double totSaveamt) { 
		this.totSaveamt = totSaveamt;
	}
	public void setDmYn(String dmYn) { 
		this.dmYn = dmYn;
	}
	public void setDmNoGb(String dmNoGb) { 
		this.dmNoGb = dmNoGb;
	}
	public void setDmNoId(String dmNoId) { 
		this.dmNoId = dmNoId;
	}
	public void setDmNoDate(Timestamp dmNoDate) { 
		this.dmNoDate = dmNoDate;
	}
	public void setFirstOrderDate(Timestamp firstOrderDate) { 
		this.firstOrderDate = firstOrderDate;
	}
	public void setLastOrderDate(Timestamp lastOrderDate) { 
		this.lastOrderDate = lastOrderDate;
	}
	public void setFirstMsaleGb(String firstMsaleGb) { 
		this.firstMsaleGb = firstMsaleGb;
	}
	public void setTotOrderCnt(long totOrderCnt) { 
		this.totOrderCnt = totOrderCnt;
	}
	public void setTotOrderAmt(double totOrderAmt) { 
		this.totOrderAmt = totOrderAmt;
	}
	public void setTotCancelCnt(long totCancelCnt) { 
		this.totCancelCnt = totCancelCnt;
	}
	public void setTotCancelAmt(double totCancelAmt) { 
		this.totCancelAmt = totCancelAmt;
	}
	public void setTotReturnCnt(long totReturnCnt) { 
		this.totReturnCnt = totReturnCnt;
	}
	public void setTotReturnAmt(double totReturnAmt) { 
		this.totReturnAmt = totReturnAmt;
	}
	public void setMembTotAmt(double membTotAmt) { 
		this.membTotAmt = membTotAmt;
	}
	public void setBirthdayMmdd(String birthdayMmdd) { 
		this.birthdayMmdd = birthdayMmdd;
	}
	public void setTotShipCost(double totShipCost) { 
		this.totShipCost = totShipCost;
	}
	public void setTotCanShipCost(double totCanShipCost) { 
		this.totCanShipCost = totCanShipCost;
	}
	public String getLastConMediaCode() {
		return lastConMediaCode;
	}
	public void setLastConMediaCode(String lastConMediaCode) {
		this.lastConMediaCode = lastConMediaCode;
	}
	public String getLastOrderMedia() {
		return lastOrderMedia;
	}
	public void setLastOrderMedia(String lastOrderMedia) {
		this.lastOrderMedia = lastOrderMedia;
	}
	public String getLastOrderMediaCode() {
		return lastOrderMediaCode;
	}
	public void setLastOrderMediaCode(String lastOrderMediaCode) {
		this.lastOrderMediaCode = lastOrderMediaCode;
	}
	public String getInsertMedia() {
		return insertMedia;
	}
	public void setInsertMedia(String insertMedia) {
		this.insertMedia = insertMedia;
	}
	public String getInsertMediaCode() {
		return insertMediaCode;
	}
	public void setInsertMediaCode(String insertMediaCode) {
		this.insertMediaCode = insertMediaCode;
	}
	public Timestamp getLastConDate() {
		return lastConDate;
	}
	public void setLastConDate(Timestamp lastConDate) {
		this.lastConDate = lastConDate;
	}

	
}
