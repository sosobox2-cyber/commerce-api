package com.cware.netshopping.pacommon.v2.domain;

import java.sql.Timestamp;
import java.util.List;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;

public class PaGoodsSync {

	private long goodsSyncNo;
	private int targetCnt;
	private int procCnt;
	private int syncCnt;
	private int filterCnt;
	private int stopCnt;
	private Timestamp startDate;
	private Timestamp endDate;

	private String syncGoodsCode;
	private List<String> filterPaGroup;
	private ResponseMsg responseMsg;

	public long getGoodsSyncNo() {
		return goodsSyncNo;
	}

	public void setGoodsSyncNo(long goodsSyncNo) {
		this.goodsSyncNo = goodsSyncNo;
	}

	public int getTargetCnt() {
		return targetCnt;
	}

	public void setTargetCnt(int targetCnt) {
		this.targetCnt = targetCnt;
	}

	public int getProcCnt() {
		return procCnt;
	}

	public void setProcCnt(int procCnt) {
		this.procCnt = procCnt;
	}

	public int getSyncCnt() {
		return syncCnt;
	}

	public void setSyncCnt(int syncCnt) {
		this.syncCnt = syncCnt;
	}

	public int getFilterCnt() {
		return filterCnt;
	}

	public void setFilterCnt(int filterCnt) {
		this.filterCnt = filterCnt;
	}

	public int getStopCnt() {
		return stopCnt;
	}

	public void setStopCnt(int stopCnt) {
		this.stopCnt = stopCnt;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getSyncGoodsCode() {
		return syncGoodsCode;
	}

	public void setSyncGoodsCode(String syncGoodsCode) {
		this.syncGoodsCode = syncGoodsCode;
	}

	public List<String> getFilterPaGroup() {
		return filterPaGroup;
	}

	public void setFilterPaGroup(List<String> filterPaGroup) {
		this.filterPaGroup = filterPaGroup;
	}

	public ResponseMsg getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(ResponseMsg responseMsg) {
		this.responseMsg = responseMsg;
	}

	@Override
	public String toString() {
		return "PaGoodsSync [goodsSyncNo=" + goodsSyncNo + ", targetCnt=" + targetCnt + ", procCnt=" + procCnt
				+ ", syncCnt=" + syncCnt + ", filterCnt=" + filterCnt + ", stopCnt=" + stopCnt + ", startDate="
				+ startDate + ", endDate=" + endDate + ", syncGoodsCode=" + syncGoodsCode + ", filterPaGroup="
				+ filterPaGroup + "]";
	}
	
}
