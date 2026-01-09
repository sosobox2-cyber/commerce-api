package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaLtonStdCtgr extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String stdCatId;
	private String stdCatNm;
	private String stdCatDesc;
	private String uprStdCatId;
	private String depthNo;
	private String leafYn;
	private String prioRnk;
	private String tdfCd;
	private String ageLimitCd;
	private String smartpickYn;
	private String deductCultYn;
	private String abandPickupYn;
	private String exchMoneyYn;
	private String counselProdYn;
	private String modDate;
	private String useYn;
	private String chlAthn;
	private String chlCfm;
	private String chlSups;
	private String cmcnAthn;
	private String cmcnReg;
	private String cmcnTntt;
	private String elcAthn;
	private String elcCfm;
	private String elcSups;
	private String lifeAthn;
	private String lifeCfm;
	private String lifeSups;
	private String lifeStd;
	private String chemLife;
	private String chemBioc;
	private String etc;
	
	public String getStdCatId() {
		return stdCatId;
	}
	public void setStdCatId(String stdCatId) {
		this.stdCatId = stdCatId;
	}
	public String getStdCatNm() {
		return stdCatNm;
	}
	public void setStdCatNm(String stdCatNm) {
		this.stdCatNm = stdCatNm;
	}
	public String getStdCatDesc() {
		return stdCatDesc;
	}
	public void setStdCatDesc(String stdCatDesc) {
		this.stdCatDesc = stdCatDesc;
	}
	public String getUprStdCatId() {
		return uprStdCatId;
	}
	public void setUprStdCatId(String uprStdCatId) {
		this.uprStdCatId = uprStdCatId;
	}
	public String getDepthNo() {
		return depthNo;
	}
	public void setDepthNo(String depthNo) {
		this.depthNo = depthNo;
	}
	public String getLeafYn() {
		return leafYn;
	}
	public void setLeafYn(String leafYn) {
		this.leafYn = leafYn;
	}
	public String getPrioRnk() {
		return prioRnk;
	}
	public void setPrioRnk(String prioRnk) {
		this.prioRnk = prioRnk;
	}
	public String getTdfCd() {
		return tdfCd;
	}
	public void setTdfCd(String tdfCd) {
		this.tdfCd = tdfCd;
	}
	public String getAgeLimitCd() {
		return ageLimitCd;
	}
	public void setAgeLimitCd(String ageLimitCd) {
		this.ageLimitCd = ageLimitCd;
	}
	public String getSmartpickYn() {
		return smartpickYn;
	}
	public void setSmartpickYn(String smartpickYn) {
		this.smartpickYn = smartpickYn;
	}
	public String getDeductCultYn() {
		return deductCultYn;
	}
	public void setDeductCultYn(String deductCultYn) {
		this.deductCultYn = deductCultYn;
	}
	public String getAbandPickupYn() {
		return abandPickupYn;
	}
	public void setAbandPickupYn(String abandPickupYn) {
		this.abandPickupYn = abandPickupYn;
	}
	public String getExchMoneyYn() {
		return exchMoneyYn;
	}
	public void setExchMoneyYn(String exchMoneyYn) {
		this.exchMoneyYn = exchMoneyYn;
	}
	public String getCounselProdYn() {
		return counselProdYn;
	}
	public void setCounselProdYn(String counselProdYn) {
		this.counselProdYn = counselProdYn;
	}
	public String getModDate() {
		return modDate;
	}
	public void setModDate(String modDate) {
		this.modDate = modDate;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getChlAthn() {
		return chlAthn;
	}
	public void setChlAthn(String chlAthn) {
		this.chlAthn = chlAthn;
	}
	public String getChlCfm() {
		return chlCfm;
	}
	public void setChlCfm(String chlCfm) {
		this.chlCfm = chlCfm;
	}
	public String getChlSups() {
		return chlSups;
	}
	public void setChlSups(String chlSups) {
		this.chlSups = chlSups;
	}
	public String getCmcnAthn() {
		return cmcnAthn;
	}
	public void setCmcnAthn(String cmcnAthn) {
		this.cmcnAthn = cmcnAthn;
	}
	public String getCmcnReg() {
		return cmcnReg;
	}
	public void setCmcnReg(String cmcnReg) {
		this.cmcnReg = cmcnReg;
	}
	public String getCmcnTntt() {
		return cmcnTntt;
	}
	public void setCmcnTntt(String cmcnTntt) {
		this.cmcnTntt = cmcnTntt;
	}
	public String getElcAthn() {
		return elcAthn;
	}
	public void setElcAthn(String elcAthn) {
		this.elcAthn = elcAthn;
	}
	public String getElcCfm() {
		return elcCfm;
	}
	public void setElcCfm(String elcCfm) {
		this.elcCfm = elcCfm;
	}
	public String getElcSups() {
		return elcSups;
	}
	public void setElcSups(String elcSups) {
		this.elcSups = elcSups;
	}
	public String getLifeAthn() {
		return lifeAthn;
	}
	public void setLifeAthn(String lifeAthn) {
		this.lifeAthn = lifeAthn;
	}
	public String getLifeCfm() {
		return lifeCfm;
	}
	public void setLifeCfm(String lifeCfm) {
		this.lifeCfm = lifeCfm;
	}
	public String getLifeSups() {
		return lifeSups;
	}
	public void setLifeSups(String lifeSups) {
		this.lifeSups = lifeSups;
	}
	public String getLifeStd() {
		return lifeStd;
	}
	public void setLifeStd(String lifeStd) {
		this.lifeStd = lifeStd;
	}
	public String getChemLife() {
		return chemLife;
	}
	public void setChemLife(String chemLife) {
		this.chemLife = chemLife;
	}
	public String getChemBioc() {
		return chemBioc;
	}
	public void setChemBioc(String chemBioc) {
		this.chemBioc = chemBioc;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	
}
