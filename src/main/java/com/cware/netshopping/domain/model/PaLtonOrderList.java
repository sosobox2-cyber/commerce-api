package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaLtonOrderList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	private String paOrderGb;     
	private String odNo;         
	private String clmNo;        
	private String odSeq;   
	private String odTypCd;        
	private String odTypDtlCd;       
	private String dvRtrvDvsCd;      
	private String procSeq;        
	private String orglProcSeq;    
	private String chNo;          
	private String chDtlNo;         
	private String odCmptDttm;     
	private String odPrgsStepCd;      
	private String odSlTypCd;       
	private String xchgDvsCd;    
	private String clmRsnCd;        
	private String clmRsnCnts;        
	private String imptDvsCd;    
	private String pprCrdMsgYn;      
	private String pprCrdMsg;     
	private String pprCrdSndpNm;   
	private String pprCrdRcvrNm; 
	private String trNo;       
	private String trNm;      
	private String lrtrNo;          
	private String lrtrNm;        
	private String odrNm;          
	private String mphnNo;          
	private String telNo;         
	private String emlAddr;            
	private String ctrtTypCd;
	private String dvPdTypCd;
	private String spdNo;        
	private String epdNo;
	private String spdNm;
	private String sitmNo;               
	private String eitmNo;
	private String sitmNm;
	private double slPrc;
	private long   odQty; 
	private double slAmt;   
	private String cmbnDvPsbYn;            
	private String cmbnDvGrpNo;  
	private String dvMnsCd;      
	private String hpDvDttm;               
	private String dmstOvsDvDvsCd; 
	private String indvCstmPclrNo;  
	private String brkHmapPkcpYn;    
	private String dvMsg;      
	private String dvRsvDvsCd;    
	private double dvCst;         
	private String odCstTypCd;    
	private double adtnDvCst;     
	private String dvpCustNm;            
	private String dvpEmlAddr;    
	private String dvpTelNo;   
	private String dvpMphnNo;          
	private String sftNo;   
	private String dvpZipNo;   
	private String dvpStnmZipAddr;         
	private String dvpStnmDtlAddr;
	private String owhoDttm;       
	private String rtrpSiDttm;
	
	private double prSfcoShrAmtSum;
	private double prEntpShrAmtSum;
	private double sptDcPgmCmsnSum;
	private double fvrAmtSum;
	private double actualAmt;
	
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
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
	public String getDvRtrvDvsCd() {
		return dvRtrvDvsCd;
	}
	public void setDvRtrvDvsCd(String dvRtrvDvsCd) {
		this.dvRtrvDvsCd = dvRtrvDvsCd;
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
	public String getChNo() {
		return chNo;
	}
	public void setChNo(String chNo) {
		this.chNo = chNo;
	}
	public String getChDtlNo() {
		return chDtlNo;
	}
	public void setChDtlNo(String chDtlNo) {
		this.chDtlNo = chDtlNo;
	}
	public String getOdCmptDttm() {
		return odCmptDttm;
	}
	public void setOdCmptDttm(String odCmptDttm) {
		this.odCmptDttm = odCmptDttm;
	}
	public String getOdPrgsStepCd() {
		return odPrgsStepCd;
	}
	public void setOdPrgsStepCd(String odPrgsStepCd) {
		this.odPrgsStepCd = odPrgsStepCd;
	}
	public String getOdSlTypCd() {
		return odSlTypCd;
	}
	public void setOdSlTypCd(String odSlTypCd) {
		this.odSlTypCd = odSlTypCd;
	}
	public String getXchgDvsCd() {
		return xchgDvsCd;
	}
	public void setXchgDvsCd(String xchgDvsCd) {
		this.xchgDvsCd = xchgDvsCd;
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
	public String getImptDvsCd() {
		return imptDvsCd;
	}
	public void setImptDvsCd(String imptDvsCd) {
		this.imptDvsCd = imptDvsCd;
	}
	public String getPprCrdMsgYn() {
		return pprCrdMsgYn;
	}
	public void setPprCrdMsgYn(String pprCrdMsgYn) {
		this.pprCrdMsgYn = pprCrdMsgYn;
	}
	public String getPprCrdMsg() {
		return pprCrdMsg;
	}
	public void setPprCrdMsg(String pprCrdMsg) {
		this.pprCrdMsg = pprCrdMsg;
	}
	public String getPprCrdSndpNm() {
		return pprCrdSndpNm;
	}
	public void setPprCrdSndpNm(String pprCrdSndpNm) {
		this.pprCrdSndpNm = pprCrdSndpNm;
	}
	public String getPprCrdRcvrNm() {
		return pprCrdRcvrNm;
	}
	public void setPprCrdRcvrNm(String pprCrdRcvrNm) {
		this.pprCrdRcvrNm = pprCrdRcvrNm;
	}
	public String getTrNo() {
		return trNo;
	}
	public void setTrNo(String trNo) {
		this.trNo = trNo;
	}
	public String getTrNm() {
		return trNm;
	}
	public void setTrNm(String trNm) {
		this.trNm = trNm;
	}
	public String getLrtrNo() {
		return lrtrNo;
	}
	public void setLrtrNo(String lrtrNo) {
		this.lrtrNo = lrtrNo;
	}
	public String getLrtrNm() {
		return lrtrNm;
	}
	public void setLrtrNm(String lrtrNm) {
		this.lrtrNm = lrtrNm;
	}
	public String getOdrNm() {
		return odrNm;
	}
	public void setOdrNm(String odrNm) {
		this.odrNm = odrNm;
	}
	public String getMphnNo() {
		return mphnNo;
	}
	public void setMphnNo(String mphnNo) {
		this.mphnNo = mphnNo;
	}
	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	public String getEmlAddr() {
		return emlAddr;
	}
	public void setEmlAddr(String emlAddr) {
		this.emlAddr = emlAddr;
	}
	public String getCtrtTypCd() {
		return ctrtTypCd;
	}
	public void setCtrtTypCd(String ctrtTypCd) {
		this.ctrtTypCd = ctrtTypCd;
	}
	public String getDvPdTypCd() {
		return dvPdTypCd;
	}
	public void setDvPdTypCd(String dvPdTypCd) {
		this.dvPdTypCd = dvPdTypCd;
	}
	public String getSpdNo() {
		return spdNo;
	}
	public void setSpdNo(String spdNo) {
		this.spdNo = spdNo;
	}
	public String getEpdNo() {
		return epdNo;
	}
	public void setEpdNo(String epdNo) {
		this.epdNo = epdNo;
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
	public String getEitmNo() {
		return eitmNo;
	}
	public void setEitmNo(String eitmNo) {
		this.eitmNo = eitmNo;
	}
	public String getSitmNm() {
		return sitmNm;
	}
	public void setSitmNm(String sitmNm) {
		this.sitmNm = sitmNm;
	}
	public double getSlPrc() {
		return slPrc;
	}
	public void setSlPrc(double slPrc) {
		this.slPrc = slPrc;
	}
	public long getOdQty() {
		return odQty;
	}
	public void setOdQty(long odQty) {
		this.odQty = odQty;
	}
	public double getSlAmt() {
		return slAmt;
	}
	public void setSlAmt(double slAmt) {
		this.slAmt = slAmt;
	}
	public String getCmbnDvPsbYn() {
		return cmbnDvPsbYn;
	}
	public void setCmbnDvPsbYn(String cmbnDvPsbYn) {
		this.cmbnDvPsbYn = cmbnDvPsbYn;
	}
	public String getCmbnDvGrpNo() {
		return cmbnDvGrpNo;
	}
	public void setCmbnDvGrpNo(String cmbnDvGrpNo) {
		this.cmbnDvGrpNo = cmbnDvGrpNo;
	}
	public String getDvMnsCd() {
		return dvMnsCd;
	}
	public void setDvMnsCd(String dvMnsCd) {
		this.dvMnsCd = dvMnsCd;
	}
	public String getHpDvDttm() {
		return hpDvDttm;
	}
	public void setHpDvDttm(String hpDvDttm) {
		this.hpDvDttm = hpDvDttm;
	}
	public String getDmstOvsDvDvsCd() {
		return dmstOvsDvDvsCd;
	}
	public void setDmstOvsDvDvsCd(String dmstOvsDvDvsCd) {
		this.dmstOvsDvDvsCd = dmstOvsDvDvsCd;
	}
	public String getIndvCstmPclrNo() {
		return indvCstmPclrNo;
	}
	public void setIndvCstmPclrNo(String indvCstmPclrNo) {
		this.indvCstmPclrNo = indvCstmPclrNo;
	}
	public String getBrkHmapPkcpYn() {
		return brkHmapPkcpYn;
	}
	public void setBrkHmapPkcpYn(String brkHmapPkcpYn) {
		this.brkHmapPkcpYn = brkHmapPkcpYn;
	}
	public String getDvMsg() {
		return dvMsg;
	}
	public void setDvMsg(String dvMsg) {
		this.dvMsg = dvMsg;
	}
	public String getDvRsvDvsCd() {
		return dvRsvDvsCd;
	}
	public void setDvRsvDvsCd(String dvRsvDvsCd) {
		this.dvRsvDvsCd = dvRsvDvsCd;
	}
	public double getDvCst() {
		return dvCst;
	}
	public void setDvCst(double dvCst) {
		this.dvCst = dvCst;
	}
	public String getOdCstTypCd() {
		return odCstTypCd;
	}
	public void setOdCstTypCd(String odCstTypCd) {
		this.odCstTypCd = odCstTypCd;
	}
	public double getAdtnDvCst() {
		return adtnDvCst;
	}
	public void setAdtnDvCst(double adtnDvCst) {
		this.adtnDvCst = adtnDvCst;
	}
	public String getDvpCustNm() {
		return dvpCustNm;
	}
	public void setDvpCustNm(String dvpCustNm) {
		this.dvpCustNm = dvpCustNm;
	}
	public String getDvpEmlAddr() {
		return dvpEmlAddr;
	}
	public void setDvpEmlAddr(String dvpEmlAddr) {
		this.dvpEmlAddr = dvpEmlAddr;
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
	public String getSftNo() {
		return sftNo;
	}
	public void setSftNo(String sftNo) {
		this.sftNo = sftNo;
	}
	public String getDvpZipNo() {
		return dvpZipNo;
	}
	public void setDvpZipNo(String dvpZipNo) {
		this.dvpZipNo = dvpZipNo;
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
	public String getOwhoDttm() {
		return owhoDttm;
	}
	public void setOwhoDttm(String owhoDttm) {
		this.owhoDttm = owhoDttm;
	}
	public String getRtrpSiDttm() {
		return rtrpSiDttm;
	}
	public void setRtrpSiDttm(String rtrpSiDttm) {
		this.rtrpSiDttm = rtrpSiDttm;
	}
	public double getPrSfcoShrAmtSum() {
		return prSfcoShrAmtSum;
	}
	public void setPrSfcoShrAmtSum(double prSfcoShrAmtSum) {
		this.prSfcoShrAmtSum = prSfcoShrAmtSum;
	}
	public double getPrEntpShrAmtSum() {
		return prEntpShrAmtSum;
	}
	public void setPrEntpShrAmtSum(double prEntpShrAmtSum) {
		this.prEntpShrAmtSum = prEntpShrAmtSum;
	}
	public double getSptDcPgmCmsnSum() {
		return sptDcPgmCmsnSum;
	}
	public void setSptDcPgmCmsnSum(double sptDcPgmCmsnSum) {
		this.sptDcPgmCmsnSum = sptDcPgmCmsnSum;
	}
	public double getFvrAmtSum() {
		return fvrAmtSum;
	}
	public void setFvrAmtSum(double fvrAmtSum) {
		this.fvrAmtSum = fvrAmtSum;
	}
	public double getActualAmt() {
		return actualAmt;
	}
	public void setActualAmt(double actualAmt) {
		this.actualAmt = actualAmt;
	}   
	
}
