package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class PaIntpClaimlist extends AbstractModel {
	private static final long serialVersionUID = 1L; 
	
	private String mappingSeq;
	private String ordNo; //인터파트주문번호
	private String clmNo; //인터파크클레임번호
	private String clmSeq; //인터파크클레임순번
	private String ordSeq; //인터파크주문순번
	private String paOrderGb;//제휴사주문구분 
	private String clmCrtTpNm;//클레임상태유형 
	private String clmCrtTp;//클레임상태유형코드 
	private String clmRegTp; //클레임접수자구분
	private String clmRcvrNm; //수취인명 
	private String clmRcvrTel; //수취인전화번호 
	private String clmRcvrMobile; //수취인핸드폰번호 
	private String clmRcvrZip; //수취인우편번호 
	private String clmRcvrAddr1; //수취인주소1
	private String clmRcvrAddr2; //수취인주소2
	private String clmRcvrZipDoro; //도로명수취인우편번호 
	private String clmRcvrAddr1Doro; //도로명수취인주소1
	private String clmRcvrAddr2Doro; //도로명수취인주소2
	private String rtnInvoNo; //반송장번호 
	private String rtnDelvEntrNo; //택배사코드 
	private String rtnRespTp; //수거책임구분코드
	private String rtnRespTpNm; //수거책임구분 
	private String optPrdTp; // 옵션상품유형 
	private String optParentSeq; //옵션부모상품주문순번 
	private String prdNo; //인터파크상품코드
	private String entrPrdNo; //제휴업체상품코드
	private String optPrdNo; //인터파크 상품 옵션코드
	private String optNo; //옵션코드 
	private long clmQty; //클레임수량
	private String currentClmPrdStatNm; //클레임상태
	private String currentClmPrdStat; //클레임상태코드
	private Timestamp clmDt; //클레임상태일자 
	private String clmRsnTp; //클레임사유코드 
	private String clmRsnTpNm; //클레임사유 
	private String clmRsnDtl; //클레임상세사유
	private String costRespTp; //비용책임구분코드
	private String costRespTpNm; //비용책임구분
	private String costPayMthdTp; //고객비용결제방법코드
	private String costPayMthdTpNm; //고객비용결제방법
	private String frtnCpTp; //무료반품쿠폰사용여부
	private double rtHdelvAmt; //반품택배비
	private double intialDelvAmt; //초기배송비
	private String paCode;	   //
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getOrdNo() {
		return ordNo;
	}
	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}
	public String getClmNo() {
		return clmNo;
	}
	public void setClmNo(String clmNo) {
		this.clmNo = clmNo;
	}
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getClmCrtTpNm() {
		return clmCrtTpNm;
	}
	public void setClmCrtTpNm(String clmCrtTpNm) {
		this.clmCrtTpNm = clmCrtTpNm;
	}
	public String getClmCrtTp() {
		return clmCrtTp;
	}
	public void setClmCrtTp(String clmCrtTp) {
		this.clmCrtTp = clmCrtTp;
	}
	public String getClmRegTp() {
		return clmRegTp;
	}
	public void setClmRegTp(String clmRegTp) {
		this.clmRegTp = clmRegTp;
	}
	public String getClmRcvrNm() {
		return clmRcvrNm;
	}
	public void setClmRcvrNm(String clmRcvrNm) {
		this.clmRcvrNm = clmRcvrNm;
	}
	public String getClmRcvrTel() {
		return clmRcvrTel;
	}
	public void setClmRcvrTel(String clmRcvrTel) {
		this.clmRcvrTel = clmRcvrTel;
	}
	public String getClmRcvrMobile() {
		return clmRcvrMobile;
	}
	public void setClmRcvrMobile(String clmRcvrMobile) {
		this.clmRcvrMobile = clmRcvrMobile;
	}
	public String getClmRcvrZip() {
		return clmRcvrZip;
	}
	public void setClmRcvrZip(String clmRcvrZip) {
		this.clmRcvrZip = clmRcvrZip;
	}
	public String getClmRcvrAddr1() {
		return clmRcvrAddr1;
	}
	public void setClmRcvrAddr1(String clmRcvrAddr1) {
		this.clmRcvrAddr1 = clmRcvrAddr1;
	}
	public String getClmRcvrAddr2() {
		return clmRcvrAddr2;
	}
	public void setClmRcvrAddr2(String clmRcvrAddr2) {
		this.clmRcvrAddr2 = clmRcvrAddr2;
	}
	public String getClmRcvrZipDoro() {
		return clmRcvrZipDoro;
	}
	public void setClmRcvrZipDoro(String clmRcvrZipDoro) {
		this.clmRcvrZipDoro = clmRcvrZipDoro;
	}
	public String getClmRcvrAddr1Doro() {
		return clmRcvrAddr1Doro;
	}
	public void setClmRcvrAddr1Doro(String clmRcvrAddr1Doro) {
		this.clmRcvrAddr1Doro = clmRcvrAddr1Doro;
	}
	public String getClmRcvrAddr2Doro() {
		return clmRcvrAddr2Doro;
	}
	public void setClmRcvrAddr2Doro(String clmRcvrAddr2Doro) {
		this.clmRcvrAddr2Doro = clmRcvrAddr2Doro;
	}
	public String getRtnInvoNo() {
		return rtnInvoNo;
	}
	public void setRtnInvoNo(String rtnInvoNo) {
		this.rtnInvoNo = rtnInvoNo;
	}
	public String getRtnDelvEntrNo() {
		return rtnDelvEntrNo;
	}
	public void setRtnDelvEntrNo(String rtnDelvEntrNo) {
		this.rtnDelvEntrNo = rtnDelvEntrNo;
	}
	public String getRtnRespTp() {
		return rtnRespTp;
	}
	public void setRtnRespTp(String rtnRespTp) {
		this.rtnRespTp = rtnRespTp;
	}
	public String getRtnRespTpNm() {
		return rtnRespTpNm;
	}
	public void setRtnRespTpNm(String rtnRespTpNm) {
		this.rtnRespTpNm = rtnRespTpNm;
	}
	public String getOptPrdTp() {
		return optPrdTp;
	}
	public void setOptPrdTp(String optPrdTp) {
		this.optPrdTp = optPrdTp;
	}
	public String getOptParentSeq() {
		return optParentSeq;
	}
	public void setOptParentSeq(String optParentSeq) {
		this.optParentSeq = optParentSeq;
	}
	public String getPrdNo() {
		return prdNo;
	}
	public void setPrdNo(String prdNo) {
		this.prdNo = prdNo;
	}
	public String getEntrPrdNo() {
		return entrPrdNo;
	}
	public void setEntrPrdNo(String entrPrdNo) {
		this.entrPrdNo = entrPrdNo;
	}
	public String getOptNo() {
		return optNo;
	}
	public void setOptNo(String optNo) {
		this.optNo = optNo;
	}
	public String getCurrentClmPrdStatNm() {
		return currentClmPrdStatNm;
	}
	public void setCurrentClmPrdStatNm(String currentClmPrdStatNm) {
		this.currentClmPrdStatNm = currentClmPrdStatNm;
	}
	public String getCurrentClmPrdStat() {
		return currentClmPrdStat;
	}
	public void setCurrentClmPrdStat(String currentClmPrdStat) {
		this.currentClmPrdStat = currentClmPrdStat;
	}

	public String getClmRsnTp() {
		return clmRsnTp;
	}
	public void setClmRsnTp(String clmRsnTp) {
		this.clmRsnTp = clmRsnTp;
	}
	public String getClmRsnTpNm() {
		return clmRsnTpNm;
	}
	public void setClmRsnTpNm(String clmRsnTpNm) {
		this.clmRsnTpNm = clmRsnTpNm;
	}
	public String getClmRsnDtl() {
		return clmRsnDtl;
	}
	public void setClmRsnDtl(String clmRsnDtl) {
		this.clmRsnDtl = clmRsnDtl;
	}
	public String getCostRespTp() {
		return costRespTp;
	}
	public void setCostRespTp(String costRespTp) {
		this.costRespTp = costRespTp;
	}
	public String getCostRespTpNm() {
		return costRespTpNm;
	}
	public void setCostRespTpNm(String costRespTpNm) {
		this.costRespTpNm = costRespTpNm;
	}
	public String getCostPayMthdTp() {
		return costPayMthdTp;
	}
	public void setCostPayMthdTp(String costPayMthdTp) {
		this.costPayMthdTp = costPayMthdTp;
	}
	public String getCostPayMthdTpNm() {
		return costPayMthdTpNm;
	}
	public void setCostPayMthdTpNm(String costPayMthdTpNm) {
		this.costPayMthdTpNm = costPayMthdTpNm;
	}
	public String getFrtnCpTp() {
		return frtnCpTp;
	}
	public void setFrtnCpTp(String frtnCpTp) {
		this.frtnCpTp = frtnCpTp;
	}
	public double getRtHdelvAmt() {
		return rtHdelvAmt;
	}
	public void setRtHdelvAmt(double rtHdelvAmt) {
		this.rtHdelvAmt = rtHdelvAmt;
	}	
	public double getIntialDelvAmt() {
		return intialDelvAmt;
	}
	public void setIntialDelvAmt(double intialDelvAmt) {
		this.intialDelvAmt = intialDelvAmt;
	}
	public String getOptPrdNo() {
		return optPrdNo;
	}
	public void setOptPrdNo(String optPrdNo) {
		this.optPrdNo = optPrdNo;
	}
	public String getMappingSeq() {
		return mappingSeq;
	}
	public void setMappingSeq(String mappingSeq) {
		this.mappingSeq = mappingSeq;
	}
	public String getClmSeq() {
		return clmSeq;
	}
	public void setClmSeq(String clmSeq) {
		this.clmSeq = clmSeq;
	}
	public String getOrdSeq() {
		return ordSeq;
	}
	public void setOrdSeq(String ordSeq) {
		this.ordSeq = ordSeq;
	}
	public long getClmQty() {
		return clmQty;
	}
	public void setClmQty(long clmQty) {
		this.clmQty = clmQty;
	}
	public Timestamp getClmDt() {
		return clmDt;
	}
	public void setClmDt(Timestamp clmDt) {
		this.clmDt = clmDt;
	}	
		
}
