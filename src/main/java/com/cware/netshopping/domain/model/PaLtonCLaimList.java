package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaLtonCLaimList extends AbstractModel {
	
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
	private long rtngQty;   //반품수량
	private String trNo;
	private String lrtrNo;
	private String odAccpDttm;
	private String purCfrmDttm;
	private String clmReqDttm;
	private String clmAccpDttm;
	private String clmCmptDttm;
	private String clmRsnCd;
	private String clmRsnCnts;
	private String spicYn;
	private String rtrvSeq;
	private String rtrvCustNm;
	private String rtrvTelNo;
	private String rtrvMphnNo;
	private String rtrvZipNo;
	private String rtrvZipNoSeq;
	private String rtrvStnmZipAddr;
	private String rtrvStnmDtlAddr;
	private String dvMsg;
	private String spicTypCd;
	private String rnkhSpplcNo;
	private String rnklSpplcNo;
	private String pkupPlcNo;
	private String pkupPlcNm;
	private String spicBxchNo;
	private String pkupBgtDttm;
	private String excpProcDvsCd;
	private String rpcSpdNo;
	private String rpcSpdNm;
	private String rpcSitmNo;
	private String rpcSitmNm;
	private double frstDvCst;		//반품배송비
	private double addDvCst;		//반품추가배송비
	private double rcst;			//반품비	
	private String dvCstBdnMnbdCd;
	private String shopCnvMsg;
	private String dvLrtrNo;
	private String hpDvDttm;
	private String afflSqncTypCd;
	private String afflCbCd;
	private String dvSqncCd;
	private String sftNoUseYn;
	private String sftDvpTelNo;
	private String sftDvpMphnNo;
	private String cmbnDvGrpNo;
	private String thdyDvProcTypCd;
	private String thdyPdYn;
	private String frstAplyDvsCd;
	//교환추가 필드
	private double rtngAddDvCst; 		//반품추가배송비
	private String dvRtrvDvsCd;
	private double rtngDvCst;			//반품배송비
	private double xchgAddDvCst;		//교환추가배송비
	private double xchgDvCst;			//교환배송비
	private String xchgDvsCd;
	private long xchgQty;
	private String cmbnDvPsbYn;
	private String dvpSeq;
	private String dvpCustNm;
	private String dvpTelNo;
	private String dvpMphnNo;
	private String dvpZipNoSeq;
	private String dvpStnmZipAddr;
	private String dvpStnmDtlAddr;
	private String jntFtdrPwd;
	private String dnDvRcptOptCd;
	private String dnDvVstMthdCd;
	private String xchgOptcChgYn;
	
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
	public long getRtngQty() {
		return rtngQty;
	}
	public void setRtngQty(long rtngQty) {
		this.rtngQty = rtngQty;
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
	public String getClmAccpDttm() {
		return clmAccpDttm;
	}
	public void setClmAccpDttm(String clmAccpDttm) {
		this.clmAccpDttm = clmAccpDttm;
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
	public String getSpicYn() {
		return spicYn;
	}
	public void setSpicYn(String spicYn) {
		this.spicYn = spicYn;
	}
	public String getRtrvSeq() {
		return rtrvSeq;
	}
	public void setRtrvSeq(String rtrvSeq) {
		this.rtrvSeq = rtrvSeq;
	}
	public String getRtrvCustNm() {
		return rtrvCustNm;
	}
	public void setRtrvCustNm(String rtrvCustNm) {
		this.rtrvCustNm = rtrvCustNm;
	}
	public String getRtrvTelNo() {
		return rtrvTelNo;
	}
	public void setRtrvTelNo(String rtrvTelNo) {
		this.rtrvTelNo = rtrvTelNo;
	}
	public String getRtrvMphnNo() {
		return rtrvMphnNo;
	}
	public void setRtrvMphnNo(String rtrvMphnNo) {
		this.rtrvMphnNo = rtrvMphnNo;
	}
	public String getRtrvZipNo() {
		return rtrvZipNo;
	}
	public void setRtrvZipNo(String rtrvZipNo) {
		this.rtrvZipNo = rtrvZipNo;
	}
	public String getRtrvZipNoSeq() {
		return rtrvZipNoSeq;
	}
	public void setRtrvZipNoSeq(String rtrvZipNoSeq) {
		this.rtrvZipNoSeq = rtrvZipNoSeq;
	}
	public String getRtrvStnmZipAddr() {
		return rtrvStnmZipAddr;
	}
	public void setRtrvStnmZipAddr(String rtrvStnmZipAddr) {
		this.rtrvStnmZipAddr = rtrvStnmZipAddr;
	}
	public String getRtrvStnmDtlAddr() {
		return rtrvStnmDtlAddr;
	}
	public void setRtrvStnmDtlAddr(String rtrvStnmDtlAddr) {
		this.rtrvStnmDtlAddr = rtrvStnmDtlAddr;
	}
	public String getDvMsg() {
		return dvMsg;
	}
	public void setDvMsg(String dvMsg) {
		this.dvMsg = dvMsg;
	}
	public String getSpicTypCd() {
		return spicTypCd;
	}
	public void setSpicTypCd(String spicTypCd) {
		this.spicTypCd = spicTypCd;
	}
	public String getRnkhSpplcNo() {
		return rnkhSpplcNo;
	}
	public void setRnkhSpplcNo(String rnkhSpplcNo) {
		this.rnkhSpplcNo = rnkhSpplcNo;
	}
	public String getRnklSpplcNo() {
		return rnklSpplcNo;
	}
	public void setRnklSpplcNo(String rnklSpplcNo) {
		this.rnklSpplcNo = rnklSpplcNo;
	}
	public String getPkupPlcNo() {
		return pkupPlcNo;
	}
	public void setPkupPlcNo(String pkupPlcNo) {
		this.pkupPlcNo = pkupPlcNo;
	}
	public String getPkupPlcNm() {
		return pkupPlcNm;
	}
	public void setPkupPlcNm(String pkupPlcNm) {
		this.pkupPlcNm = pkupPlcNm;
	}
	public String getSpicBxchNo() {
		return spicBxchNo;
	}
	public void setSpicBxchNo(String spicBxchNo) {
		this.spicBxchNo = spicBxchNo;
	}
	public String getPkupBgtDttm() {
		return pkupBgtDttm;
	}
	public void setPkupBgtDttm(String pkupBgtDttm) {
		this.pkupBgtDttm = pkupBgtDttm;
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
	public double getRcst() {
		return rcst;
	}
	public void setRcst(double rcst) {
		this.rcst = rcst;
	}
	public String getDvCstBdnMnbdCd() {
		return dvCstBdnMnbdCd;
	}
	public void setDvCstBdnMnbdCd(String dvCstBdnMnbdCd) {
		this.dvCstBdnMnbdCd = dvCstBdnMnbdCd;
	}
	public String getShopCnvMsg() {
		return shopCnvMsg;
	}
	public void setShopCnvMsg(String shopCnvMsg) {
		this.shopCnvMsg = shopCnvMsg;
	}
	public String getDvLrtrNo() {
		return dvLrtrNo;
	}
	public void setDvLrtrNo(String dvLrtrNo) {
		this.dvLrtrNo = dvLrtrNo;
	}
	public String getHpDvDttm() {
		return hpDvDttm;
	}
	public void setHpDvDttm(String hpDvDttm) {
		this.hpDvDttm = hpDvDttm;
	}
	public String getAfflSqncTypCd() {
		return afflSqncTypCd;
	}
	public void setAfflSqncTypCd(String afflSqncTypCd) {
		this.afflSqncTypCd = afflSqncTypCd;
	}
	public String getAfflCbCd() {
		return afflCbCd;
	}
	public void setAfflCbCd(String afflCbCd) {
		this.afflCbCd = afflCbCd;
	}
	public String getDvSqncCd() {
		return dvSqncCd;
	}
	public void setDvSqncCd(String dvSqncCd) {
		this.dvSqncCd = dvSqncCd;
	}
	public String getSftNoUseYn() {
		return sftNoUseYn;
	}
	public void setSftNoUseYn(String sftNoUseYn) {
		this.sftNoUseYn = sftNoUseYn;
	}
	public String getSftDvpTelNo() {
		return sftDvpTelNo;
	}
	public void setSftDvpTelNo(String sftDvpTelNo) {
		this.sftDvpTelNo = sftDvpTelNo;
	}
	public String getSftDvpMphnNo() {
		return sftDvpMphnNo;
	}
	public void setSftDvpMphnNo(String sftDvpMphnNo) {
		this.sftDvpMphnNo = sftDvpMphnNo;
	}
	public String getCmbnDvGrpNo() {
		return cmbnDvGrpNo;
	}
	public void setCmbnDvGrpNo(String cmbnDvGrpNo) {
		this.cmbnDvGrpNo = cmbnDvGrpNo;
	}
	public String getThdyDvProcTypCd() {
		return thdyDvProcTypCd;
	}
	public void setThdyDvProcTypCd(String thdyDvProcTypCd) {
		this.thdyDvProcTypCd = thdyDvProcTypCd;
	}
	public String getThdyPdYn() {
		return thdyPdYn;
	}
	public void setThdyPdYn(String thdyPdYn) {
		this.thdyPdYn = thdyPdYn;
	}
	public String getFrstAplyDvsCd() {
		return frstAplyDvsCd;
	}
	public void setFrstAplyDvsCd(String frstAplyDvsCd) {
		this.frstAplyDvsCd = frstAplyDvsCd;
	}
	public double getRtngAddDvCst() {
		return rtngAddDvCst;
	}
	public void setRtngAddDvCst(double rtngAddDvCst) {
		this.rtngAddDvCst = rtngAddDvCst;
	}
	public String getDvRtrvDvsCd() {
		return dvRtrvDvsCd;
	}
	public void setDvRtrvDvsCd(String dvRtrvDvsCd) {
		this.dvRtrvDvsCd = dvRtrvDvsCd;
	}
	public double getRtngDvCst() {
		return rtngDvCst;
	}
	public void setRtngDvCst(double rtngDvCst) {
		this.rtngDvCst = rtngDvCst;
	}
	public double getXchgAddDvCst() {
		return xchgAddDvCst;
	}
	public void setXchgAddDvCst(double xchgAddDvCst) {
		this.xchgAddDvCst = xchgAddDvCst;
	}
	public double getXchgDvCst() {
		return xchgDvCst;
	}
	public void setXchgDvCst(double xchgDvCst) {
		this.xchgDvCst = xchgDvCst;
	}
	public String getXchgDvsCd() {
		return xchgDvsCd;
	}
	public void setXchgDvsCd(String xchgDvsCd) {
		this.xchgDvsCd = xchgDvsCd;
	}
	public long getXchgQty() {
		return xchgQty;
	}
	public void setXchgQty(long xchgQty) {
		this.xchgQty = xchgQty;
	}
	public String getCmbnDvPsbYn() {
		return cmbnDvPsbYn;
	}
	public void setCmbnDvPsbYn(String cmbnDvPsbYn) {
		this.cmbnDvPsbYn = cmbnDvPsbYn;
	}
	public String getDvpSeq() {
		return dvpSeq;
	}
	public void setDvpSeq(String dvpSeq) {
		this.dvpSeq = dvpSeq;
	}
	public String getDvpCustNm() {
		return dvpCustNm;
	}
	public void setDvpCustNm(String dvpCustNm) {
		this.dvpCustNm = dvpCustNm;
	}
	public String getDvpTelNo() {
		return dvpTelNo;
	}
	public void setDvpTelNo(String dvpTelNo) {
		this.dvpTelNo = dvpTelNo;
	}
	public String getDvpMphnNo() {
		return dvpMphnNo;
	}
	public void setDvpMphnNo(String dvpMphnNo) {
		this.dvpMphnNo = dvpMphnNo;
	}
	public String getDvpZipNoSeq() {
		return dvpZipNoSeq;
	}
	public void setDvpZipNoSeq(String dvpZipNoSeq) {
		this.dvpZipNoSeq = dvpZipNoSeq;
	}
	public String getDvpStnmZipAddr() {
		return dvpStnmZipAddr;
	}
	public void setDvpStnmZipAddr(String dvpStnmZipAddr) {
		this.dvpStnmZipAddr = dvpStnmZipAddr;
	}
	public String getDvpStnmDtlAddr() {
		return dvpStnmDtlAddr;
	}
	public void setDvpStnmDtlAddr(String dvpStnmDtlAddr) {
		this.dvpStnmDtlAddr = dvpStnmDtlAddr;
	}
	public String getJntFtdrPwd() {
		return jntFtdrPwd;
	}
	public void setJntFtdrPwd(String jntFtdrPwd) {
		this.jntFtdrPwd = jntFtdrPwd;
	}
	public String getDnDvRcptOptCd() {
		return dnDvRcptOptCd;
	}
	public void setDnDvRcptOptCd(String dnDvRcptOptCd) {
		this.dnDvRcptOptCd = dnDvRcptOptCd;
	}
	public String getDnDvVstMthdCd() {
		return dnDvVstMthdCd;
	}
	public void setDnDvVstMthdCd(String dnDvVstMthdCd) {
		this.dnDvVstMthdCd = dnDvVstMthdCd;
	}
	public String getXchgOptcChgYn() {
		return xchgOptcChgYn;
	}
	public void setXchgOptcChgYn(String xchgOptcChgYn) {
		this.xchgOptcChgYn = xchgOptcChgYn;
	}

}

/*odNo
clmNo
odSeq
procSeq
orglProcSeq
odTypCd
odTypDtlCd
odPrgsStepCd
spdNo
spdNm
sitmNo
sitmNm
odQty
itmSlPrc
rtngQty
trNo
lrtrNo
odAccpDttm
purCfrmDttm
clmReqDttm
clmAccpDttm
clmCmptDttm
clmRsnCd
clmRsnCnts
spicYn
rtrvSeq
rtrvCustNm
rtrvTelNo
rtrvMphnNo
rtrvZipNo
rtrvZipNoSeq
rtrvStnmZipAddr
rtrvStnmDtlAddr
dvMsg
spicTypCd
rnkhSpplcNo
rnklSpplcNo
pkupPlcNo
pkupPlcNm
spicBxchNo
pkupBgtDttm
excpProcDvsCd
rpcSpdNo
rpcSpdNm
rpcSitmNo
rpcSitmNm
frstDvCst
addDvCst
rcst
dvCstBdnMnbdCd
shopCnvMsg
dvLrtrNo
hpDvDttm
afflSqncTypCd
afflCbCd
dvSqncCd
sftNoUseYn
sftDvpTelNo
sftDvpMphnNo
cmbnDvGrpNo
thdyDvProcTypCd
thdyPdYn
frstAplyDvsCd
교환
rtngAddDvCst   
dvRtrvDvsCd
rtngDvCst
xchgAddDvCst
xchgDvCst
xchgDvsCd
xchgQty
cmbnDvPsbYn
dvpSeq
dvpCustNm
dvpTelNo
dvpMphnNo
dvpZipNoSeq
dvpStnmZipAddr
dvpStnmDtlAddr
jntFtdrPwd
dnDvRcptOptCd
dnDvVstMthdCd
xchgOptcChgYn

*/

