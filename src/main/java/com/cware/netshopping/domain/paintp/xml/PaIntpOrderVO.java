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

/**
 * 주문내역조회 > 주문정보 xml mapping VO
 * @author FIC05202
 *
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "ORDER")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PaIntpOrderVO implements Serializable {

	/** id */
	@XmlAttribute(name = "ID")
	private Integer id;
	
	/** 인터파크주문번호 */
	@XmlElement(name = "ORD_NO")
	private String ordNo;

	/** 주문일(yyyyMMdd) */
	@XmlElement(name = "ORDER_DT")
	private String orderDt;
	
	/** 주문일자(yyyyMMddHHmiss) */
	@XmlElement(name = "ORDER_DTS")
	private String orderDts;
	
	/** 입금일자(yyyyMMddHHmiss) */
	@XmlElement(name = "PAY_DTS")
	private String payDts;
	
	/** 주문자명 */
	@XmlElement(name = "ORD_NM")
	private String ordNm;
	
	/** 주문자핸드폰번호 */
	@XmlElement(name = "MOBILE_TEL")
	private String mobileTel;
	
	/** 주문자전화번호 */
	@XmlElement(name = "TEL")
	private String tel;
	
	/** 주문자메일주소 */
	@XmlElement(name = "EMAIL")
	private String email;
	
	/** 수취인명 */
	@XmlElement(name = "RCVR_NM")
	private String rcvrNm;
	
	/** 수취인핸드폰번호 */
	@XmlElement(name = "DELI_MOBILE")
	private String deliMobile;
	
	/** 수취인전화번호 */
	@XmlElement(name = "DELI_TEL")
	private String deliTel;
	
	/** 수취인우편번호 */
	@XmlElement(name = "DEL_ZIP")
	private String delZip;
	
	/** 수취인주소1 */
	@XmlElement(name = "DELI_ADDR1")
	private String deliAddr1;
	
	/** 수취인주소2 */
	@XmlElement(name = "DELI_ADDR2")
	private String deliAddr2;
	
	/** 수취인 도로명 우편번호 */
	@XmlElement(name = "DEL_ZIP_DORO")
	private String delZipDoro;
	
	/** 수취인 도로명 주소1 */
	@XmlElement(name = "DELI_ADDR1_DORO")
	private String deliAddr1Doro;
	
	/** 수취인 도로명 주소2 */
	@XmlElement(name = "DELI_ADDR2_DORO")
	private String deliAddr2Doro;
	
	/** 배송메시지 */
	@XmlElement(name = "DELI_COMMENT")
	private String deliComment;
	
	/** 해외통관고유번호 */
	@XmlElement(name = "RESIDENT_NO")
	private String residentNo;
	
	/** 주문자ID */
	@XmlElement(name = "MEM_ID")
	private String memId;
	
	/** 결제유형 */
	@XmlElement(name = "PAY_REF_MTHD_TP")
	private String payRefMthdTp;
	
	/** 주문발생유형 */
	@XmlElement(name = "ORDCLM_CRT_TP")
	private String ordclmCrtTp;
	
	/** 배송비 목록 */
	@XmlElementWrapper(name = "DELIVERY")
	@XmlElement(name = "DELV")
	private List<PaIntpDeliveryVO> delivList;
	
	/** 배송비 상세 목록 */
	@XmlElementWrapper(name = "DELIVERY_DETAIL")
	@XmlElement(name = "PRD_DELV")
	private List<PaIntpDeliveryDetailVO> deliveryDetailList;
	
	/** 주문 상품 목록 */
	@XmlElementWrapper(name = "PRODUCT")
	@XmlElement(name = "PRD")
	private List<PaIntpOrderProductVO> prdList;

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

	
	public String getOrderDt() {
		return orderDt;
	}

	public void setOrderDt(String orderDt) {
		this.orderDt = orderDt;
	}

	public String getOrderDts() {
		return orderDts;
	}

	public void setOrderDts(String orderDts) {
		this.orderDts = orderDts;
	}

	public String getPayDts() {
		return payDts;
	}

	public void setPayDts(String payDts) {
		this.payDts = payDts;
	}

	public String getOrdNm() {
		return ordNm;
	}

	public void setOrdNm(String ordNm) {
		this.ordNm = ordNm;
	}

	public String getMobileTel() {
		return mobileTel;
	}

	public void setMobileTel(String mobileTel) {
		this.mobileTel = mobileTel;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRcvrNm() {
		return rcvrNm;
	}

	public void setRcvrNm(String rcvrNm) {
		this.rcvrNm = rcvrNm;
	}

	public String getDeliMobile() {
		return deliMobile;
	}

	public void setDeliMobile(String deliMobile) {
		this.deliMobile = deliMobile;
	}

	public String getDeliTel() {
		return deliTel;
	}

	public void setDeliTel(String deliTel) {
		this.deliTel = deliTel;
	}

	public String getDelZip() {
		return delZip;
	}

	public void setDelZip(String delZip) {
		this.delZip = delZip;
	}

	public String getDeliAddr1() {
		return deliAddr1;
	}

	public void setDeliAddr1(String deliAddr1) {
		this.deliAddr1 = deliAddr1;
	}

	public String getDeliAddr2() {
		return deliAddr2;
	}

	public void setDeliAddr2(String deliAddr2) {
		this.deliAddr2 = deliAddr2;
	}

	public String getDelZipDoro() {
		return delZipDoro;
	}

	public void setDelZipDoro(String delZipDoro) {
		this.delZipDoro = delZipDoro;
	}

	public String getDeliAddr1Doro() {
		return deliAddr1Doro;
	}

	public void setDeliAddr1Doro(String deliAddr1Doro) {
		this.deliAddr1Doro = deliAddr1Doro;
	}

	public String getDeliAddr2Doro() {
		return deliAddr2Doro;
	}

	public void setDeliAddr2Doro(String deliAddr2Doro) {
		this.deliAddr2Doro = deliAddr2Doro;
	}

	public String getDeliComment() {
		return deliComment;
	}

	public void setDeliComment(String deliComment) {
		this.deliComment = deliComment;
	}

	public String getResidentNo() {
		return residentNo;
	}

	public void setResidentNo(String residentNo) {
		this.residentNo = residentNo;
	}

	public String getMemId() {
		return memId;
	}

	public void setMemId(String memId) {
		this.memId = memId;
	}
	
	public String getPayRefMthdTp() {
		return payRefMthdTp;
	}

	public void setPayRefMthdTp(String payRefMthdTp) {
		this.payRefMthdTp = payRefMthdTp;
	}
	
	public String getOrdclmCrtTp() {
		return ordclmCrtTp;
	}

	public void setOrdclmCrtTp(String ordclmCrtTp) {
		this.ordclmCrtTp = ordclmCrtTp;
	}

	public List<PaIntpDeliveryVO> getDelivList() {
		return delivList;
	}

	public void setDelivList(List<PaIntpDeliveryVO> delivList) {
		this.delivList = delivList;
	}

	public List<PaIntpDeliveryDetailVO> getDeliveryDetailList() {
		return deliveryDetailList;
	}

	public void setDeliveryDetailList(List<PaIntpDeliveryDetailVO> deliveryDetailList) {
		this.deliveryDetailList = deliveryDetailList;
	}
	
	public List<PaIntpOrderProductVO> getPrdList() {
		return prdList;
	}

	public void setPrdList(List<PaIntpOrderProductVO> prdList) {
		this.prdList = prdList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}