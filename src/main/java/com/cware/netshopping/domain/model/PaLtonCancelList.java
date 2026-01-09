package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaLtonCancelList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	private String odNo;
	private String clmNo;
	private String odSeq;
	private String procSeq;
	private String orglProcSeq;
	private String odTypCd;
	private String odTypDtlCd;
	private String odPrgsStepCd;
	private String spdNo;
	private String spdNm;
	private String sitmNo;
	private String sitmNm;
	private long odQty;
	private double itmSlPrc;
	private long cnclQty;
	private String trNo;
	private String lrtrNo;
	private String odAccpDttm;
	private String purCfrmDttm;
	private String clmReqDttm;
	private String clmCmptDttm;
	private String clmRsnCd;
	private String clmRsnCnts;
	private long odFvrGrpNo;
	private double fvrAmt;
	private String sellerCnYn;
	private double frstDvCst;
	private double addDvCst;
	private String dvCstBdnMnbdCd;
	private String excpProcDvsCd;
	private String rpcSpdNo;
	private String rpcSpdNm;
	private String rpcSitmNo;
	private String rpcSitmNm;
	private long cmbnDvGrpNo;
	private String dhdyDvProcTypCd;
	private String dhdyPdYn;
	private String frstAplyDvsCd;
	private String coolbagTypCd;
	private String coolbagCnclYn;
	private String procFlag;
	private String withdrawYn;
	private Timestamp withdrawDate;
	private String cancelProcNote;
	private Timestamp cancelProcId;
	
	public String getOdNo() {
		return odNo;
	}
	public void setOdNo(String odNo) {
		this.odNo = odNo;
	}
	public String getClmNo() {
		return clmNo;
	}
	public void setClmNo(String clmNo) {
		this.clmNo = clmNo;
	}
	public String getOdSeq() {
		return odSeq;
	}
	public void setOdSeq(String odSeq) {
		this.odSeq = odSeq;
	}
	public String getProcSeq() {
		return procSeq;
	}
	public void setProcSeq(String procSeq) {
		this.procSeq = procSeq;
	}
	public String getOrglProcSeq() {
		return orglProcSeq;
	}
	public void setOrglProcSeq(String orglProcSeq) {
		this.orglProcSeq = orglProcSeq;
	}
	public String getOdTypCd() {
		return odTypCd;
	}
	public void setOdTypCd(String odTypCd) {
		this.odTypCd = odTypCd;
	}
	public String getOdTypDtlCd() {
		return odTypDtlCd;
	}
	public void setOdTypDtlCd(String odTypDtlCd) {
		this.odTypDtlCd = odTypDtlCd;
	}
	public String getOdPrgsStepCd() {
		return odPrgsStepCd;
	}
	public void setOdPrgsStepCd(String odPrgsStepCd) {
		this.odPrgsStepCd = odPrgsStepCd;
	}
	public String getSpdNo() {
		return spdNo;
	}
	public void setSpdNo(String spdNo) {
		this.spdNo = spdNo;
	}
	public String getSpdNm() {
		return spdNm;
	}
	public void setSpdNm(String spdNm) {
		this.spdNm = spdNm;
	}
	public String getSitmNo() {
		return sitmNo;
	}
	public void setSitmNo(String sitmNo) {
		this.sitmNo = sitmNo;
	}
	public String getSitmNm() {
		return sitmNm;
	}
	public void setSitmNm(String sitmNm) {
		this.sitmNm = sitmNm;
	}
	public long getOdQty() {
		return odQty;
	}
	public void setOdQty(long odQty) {
		this.odQty = odQty;
	}
	public double getItmSlPrc() {
		return itmSlPrc;
	}
	public void setItmSlPrc(double itmSlPrc) {
		this.itmSlPrc = itmSlPrc;
	}
	public long getCnclQty() {
		return cnclQty;
	}
	public void setCnclQty(long cnclQty) {
		this.cnclQty = cnclQty;
	}
	public String getTrNo() {
		return trNo;
	}
	public void setTrNo(String trNo) {
		this.trNo = trNo;
	}
	public String getLrtrNo() {
		return lrtrNo;
	}
	public void setLrtrNo(String lrtrNo) {
		this.lrtrNo = lrtrNo;
	}
	public String getOdAccpDttm() {
		return odAccpDttm;
	}
	public void setOdAccpDttm(String odAccpDttm) {
		this.odAccpDttm = odAccpDttm;
	}
	public String getPurCfrmDttm() {
		return purCfrmDttm;
	}
	public void setPurCfrmDttm(String purCfrmDttm) {
		this.purCfrmDttm = purCfrmDttm;
	}
	public String getClmReqDttm() {
		return clmReqDttm;
	}
	public void setClmReqDttm(String clmReqDttm) {
		this.clmReqDttm = clmReqDttm;
	}
	public String getClmCmptDttm() {
		return clmCmptDttm;
	}
	public void setClmCmptDttm(String clmCmptDttm) {
		this.clmCmptDttm = clmCmptDttm;
	}
	public String getClmRsnCd() {
		return clmRsnCd;
	}
	public void setClmRsnCd(String clmRsnCd) {
		this.clmRsnCd = clmRsnCd;
	}
	public String getClmRsnCnts() {
		return clmRsnCnts;
	}
	public void setClmRsnCnts(String clmRsnCnts) {
		this.clmRsnCnts = clmRsnCnts;
	}
	public long getOdFvrGrpNo() {
		return odFvrGrpNo;
	}
	public void setOdFvrGrpNo(long odFvrGrpNo) {
		this.odFvrGrpNo = odFvrGrpNo;
	}
	public double getFvrAmt() {
		return fvrAmt;
	}
	public void setFvrAmt(double fvrAmt) {
		this.fvrAmt = fvrAmt;
	}
	public String getSellerCnYn() {
		return sellerCnYn;
	}
	public void setSellerCnYn(String sellerCnYn) {
		this.sellerCnYn = sellerCnYn;
	}
	public double getFrstDvCst() {
		return frstDvCst;
	}
	public void setFrstDvCst(double frstDvCst) {
		this.frstDvCst = frstDvCst;
	}
	public double getAddDvCst() {
		return addDvCst;
	}
	public void setAddDvCst(double addDvCst) {
		this.addDvCst = addDvCst;
	}
	public String getDvCstBdnMnbdCd() {
		return dvCstBdnMnbdCd;
	}
	public void setDvCstBdnMnbdCd(String dvCstBdnMnbdCd) {
		this.dvCstBdnMnbdCd = dvCstBdnMnbdCd;
	}
	public String getExcpProcDvsCd() {
		return excpProcDvsCd;
	}
	public void setExcpProcDvsCd(String excpProcDvsCd) {
		this.excpProcDvsCd = excpProcDvsCd;
	}
	public String getRpcSpdNo() {
		return rpcSpdNo;
	}
	public void setRpcSpdNo(String rpcSpdNo) {
		this.rpcSpdNo = rpcSpdNo;
	}
	public String getRpcSpdNm() {
		return rpcSpdNm;
	}
	public void setRpcSpdNm(String rpcSpdNm) {
		this.rpcSpdNm = rpcSpdNm;
	}
	public String getRpcSitmNo() {
		return rpcSitmNo;
	}
	public void setRpcSitmNo(String rpcSitmNo) {
		this.rpcSitmNo = rpcSitmNo;
	}
	public String getRpcSitmNm() {
		return rpcSitmNm;
	}
	public void setRpcSitmNm(String rpcSitmNm) {
		this.rpcSitmNm = rpcSitmNm;
	}
	public long getCmbnDvGrpNo() {
		return cmbnDvGrpNo;
	}
	public void setCmbnDvGrpNo(long cmbnDvGrpNo) {
		this.cmbnDvGrpNo = cmbnDvGrpNo;
	}
	public String getDhdyDvProcTypCd() {
		return dhdyDvProcTypCd;
	}
	public void setDhdyDvProcTypCd(String dhdyDvProcTypCd) {
		this.dhdyDvProcTypCd = dhdyDvProcTypCd;
	}
	public String getDhdyPdYn() {
		return dhdyPdYn;
	}
	public void setDhdyPdYn(String dhdyPdYn) {
		this.dhdyPdYn = dhdyPdYn;
	}
	public String getFrstAplyDvsCd() {
		return frstAplyDvsCd;
	}
	public void setFrstAplyDvsCd(String frstAplyDvsCd) {
		this.frstAplyDvsCd = frstAplyDvsCd;
	}
	public String getCoolbagTypCd() {
		return coolbagTypCd;
	}
	public void setCoolbagTypCd(String coolbagTypCd) {
		this.coolbagTypCd = coolbagTypCd;
	}
	public String getCoolbagCnclYn() {
		return coolbagCnclYn;
	}
	public void setCoolbagCnclYn(String coolbagCnclYn) {
		this.coolbagCnclYn = coolbagCnclYn;
	}
	public String getProcFlag() {
		return procFlag;
	}
	public void setProcFlag(String procFlag) {
		this.procFlag = procFlag;
	}
	public String getWithdrawYn() {
		return withdrawYn;
	}
	public void setWithdrawYn(String withdrawYn) {
		this.withdrawYn = withdrawYn;
	}
	public Timestamp getWithdrawDate() {
		return withdrawDate;
	}
	public void setWithdrawDate(Timestamp withdrawDate) {
		this.withdrawDate = withdrawDate;
	}
	public String getCancelProcNote() {
		return cancelProcNote;
	}
	public void setCancelProcNote(String cancelProcNote) {
		this.cancelProcNote = cancelProcNote;
	}
	public Timestamp getCancelProcId() {
		return cancelProcId;
	}
	public void setCancelProcId(Timestamp cancelProcId) {
		this.cancelProcId = cancelProcId;
	}
	
}
