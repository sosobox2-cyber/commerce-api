package com.cware.netshopping.domain.paintp.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

@SuppressWarnings("serial")
@XmlRootElement(name = "ORDER")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PaIntpClaimVO implements Serializable {

	/** id */
	@XmlAttribute(name = "ID")
	private Integer id;
	
	/** 인터파크주문번호 */
	@XmlElement(name = "ORD_NO")
	private String ordNo;

	/** 인터파크클레임번호 */
	@XmlElement(name = "CLM_NO")
	private String clmNo;
	
	/** 인터파크클레임순번 */
	@XmlElement(name = "CLM_SEQ")
	private String clmSeq;
	
	/** 클레임상태유형 */
	@XmlElement(name = "CLM_CRT_TPNM")
	private String clmCrtTpNm;
	
	/** 클레임상태유형코드 */
	@XmlElement(name = "CLM_CRT_TP")
	private String clmCrtTp;
	
	/** 클레임접수자구분 */
	@XmlElement(name = "CLM_REG_TP")
	private String clmRegTp;
	
	/** 수취인명 */
	@XmlElement(name = "CLM_RCVR_NM")
	private String clmRcvrNm;
	
	/** 수취인전화번호 */
	@XmlElement(name = "CLM_RCVR_TEL")
	private String clmRcvrTel;
	
	/** 수취인핸드폰번호 */
	@XmlElement(name = "CLM_RCVR_MOBILE")
	private String clmRcvrMobile;
	
	/** 수취인우편번호 */
	@XmlElement(name = "CLM_RCVR_ZIP")
	private String clmRcvrZip;
	
	/** 수취인주소1 */
	@XmlElement(name = "CLM_RCVR_ADDR1")
	private String clmRcvrAddr1;
	
	/** 수취인주소2 */
	@XmlElement(name = "CLM_RCVR_ADDR2")
	private String clmRcvrAddr2;
	
	/** 도로명 수취인 우편번호 */
	@XmlElement(name = "CLM_RCVR_ZIP_DORO")
	private String clmRcvrZipDoro;
	
	/** 도로명 수취인 주소1 */
	@XmlElement(name = "CLM_RCVR__ADDR1_DORO")
	private String clmRcvrAddr1Doro;
	
	/** 도로명 수취인 주소2 */
	@XmlElement(name = "CLM_RCVR__ADDR2_DORO")
	private String clmRcvrAddr2Doro;
	
	/** 반송장번호 */
	@XmlElement(name = "RTN_INVO_NO")
	private String rtnInvoNo;
	
	/** 택배사코드 */
	@XmlElement(name = "RTN_DELV_ENTR_NO")
	private String rtnDelvEntrNo;

	/** 수거책임구분코드 */
	@XmlElement(name = "RTN_RESP_TP")
	private String rtnRespTp;
	
	/** 수거책임구분 */
	@XmlElement(name = "RTN_RESP_TPNM")
	private String rtnRespTpNm;
	
	/** 주문상품 목록 */
	@XmlElementWrapper(name = "PRODUCT")
	@XmlElement(name = "PRD")
	private List<PaIntpClaimProductVO> claimPrdList;
	
	/** 무료반품쿠폰사용여부 */
	@XmlElement(name = "FRTN_CP_TP")
	private String frtnCpTp;
	
	/** 반품택배비 */
	@XmlElement(name = "RT_HDELV_AMT")
	private double rtHdelvAmt;

	/** 초기배송비 */
	@XmlElement(name = "INITIAL_DELV_AMT")
	private double initialDelvAmt;
	
	/** 제휴사 구분코드  **/
	private String paCode; //내부적으로 사용하는 변수, xml 파싱의 결과물이 아님
	private String paOrderGb; 
		
	public String getPaOrderGb() {
		return paOrderGb;
	}

	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}

	public String getPaCode() {
		return paCode;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getClmSeq() {
		return clmSeq;
	}

	public void setClmSeq(String clmSeq) {
		this.clmSeq = clmSeq;
	}

	public String getClmCrtTpNm() {
		return clmCrtTpNm;
	}

	public void setClmCrtTpNm(String clmCrtTpNm) {
		this.clmCrtTp = clmCrtTpNm;
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
	
	public double getRtHdelvAmt() {
		return rtHdelvAmt;
	}
	
	public void setRtnRespTpNm(double rtHdelvAmt) {
		this.rtHdelvAmt = rtHdelvAmt;
	}
	
	public String getFrtnCpTp() {
		return frtnCpTp;
	}
	
	public void setFrtnCpTp(String frtnCpTp) {
		this.frtnCpTp = frtnCpTp;
	}
	
	public double getInitialDelvAmt() {
		return initialDelvAmt;
	}
	
	public void setInitialDelvAmt(double initialDelvAmt) {
		this.initialDelvAmt = initialDelvAmt;
	}
	
	public List<PaIntpClaimProductVO> getClaimPrdList() {
		return claimPrdList;
	}

	public void setClaimPrdList(List<PaIntpClaimProductVO> claimPrdList) {
		this.claimPrdList = claimPrdList;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}