package com.cware.netshopping.domain.model;

import java.sql.Timestamp;
import com.cware.framework.core.basic.AbstractModel;

public class PaIntpOrderlist extends AbstractModel {
	private static final long serialVersionUID = 1L;
	
	/** 인터파크 주문번호 */
	private String ordNo;
	/** 인터파크 주문순번 */
	private String ordSeq;
	/** 제휴사 주문구분 */
	private String paOrderGb;
	/** 클레임요청순번 */
	private String clmreqSeq;
	/** 옵션상품 유형 */
	private String optPrdTp;
	/** 주문취소 철회여부 (1: 철회, 0: 미철회) */
	private String withdrawYn;
	/** 주문일시 */
	private Timestamp orderDt;
	/** 주문일자 */
	private Timestamp orderDts;
	/** 결제일시 */
	private Timestamp payDt;
	/** 옵션 부모상품 주문순번 */
	private String optParentSeq;
	/** 상품명 */
	private String prdNm;
	/** 인터파크 상품코드 */
	private String prdNo;
	/** 인터파크 옵션상품코드 */
	private String optPrdNo;
	/** 제휴업체 상품코드 */
	private String entrPrdNo;
	/** 옵션상품코드 */
	private String optNo;
	/** 옵션명(선택형+입력형) */
	private String optNm;
	/** 옵션명(선택형) */
	private String selOptNm;
	/** 옵션명(입력형) */
	private String inOptNm;
	/** 주문자ID */
	private String memId;
	/** 인터파크 업체번호 */
	private String supplyEntrNo;
	/** 공급계약일련번호 */
	private String supplyCtrtSeq;
	/** 판매단가 */
	private double saleUnitcost;
	/** 선할인단가 */
	private double preUseUnitcost;
	/** 선할인금액 */
	private double preUseAmt;
	/** 주문수량 */
	private long ordQty;
	/** 주문금액 */
	private double ordAmt;
	/** 현재상태 */
	private String currentState;
	/** 주문상태일자 [출고지시일자] */
	private Timestamp ordclmStatDt;
	/** 인터파크 부담 쿠폰할인 단가(1개당) */
	private double dcCouponAmt;
	/** 업체 부담 쿠폰할인 단가(1개당) */
	private double entrDcCouponAmt;
	/** 쿠폰할인단가(인터파크 부담 + 업체 부담, 1개당) */
	private double totDcCouponAmt;
	/** 판매자즉시할인단가 */
	private double entrDisUnitCost;
	/** 인터파크 I-POINT 할인단가 */
	private double ipointDcUnitcost;
	/** 실판매단가(판매단가 - I-POINT 판매단가) */
	private double realSaleUnitcost;
	/** 배송비착불여부(Y:착불, N:선불, 무료) */
	private String isCollected;
	/** 해외구매대행여부 */
	private String abroadBsYn;
	/** 배송비번호 */
	private String delvsetlSeq;
	/** 결제유형 */
	private String payRefMthdTp;
	/** 주문발생유형 */
	private String ordclmCrtTp;
	/** 배송비[추가배송비 포함] */
	private double delAmt;
	/** 배송비 */
	private double delvAmt;
	/** 추가배송비 */
	private double addDelvAmt;
	/** 주문자명 */
	private String ordNm;
	/** 주문자핸드폰번호 */
	private String mobileTel;
	/** 주문자전화번호 */
	private String tel;
	/** 주문자이메일 */
	private String email;
	/** 수취인명 */
	private String rcvrNm;
	/** 수취인핸드폰번호 */
	private String deliMobile;
	/** 수취인전화번호 */
	private String deliTel;
	/** 수취인우편번호 */
	private String delZip;
	/** 도로명 수취인우편번호 - 지번주소 선택시 없음 */
	private String delZipDoro;
	/** 수취인주소1 */
	private String deliAddr1;
	/** 수취인주소2 */
	private String deliAddr2;
	/** 수취인도로명우편번호 */
	private String deliZipDoro;
	/** 수취인도로명주소1 */
	private String deliAddr1Doro;
	/** 수취인도로명주소2 */
	private String deliAddr2Doro;
	/** 배송메세지 */
	private String delvComment;
	/** 해외통관고유부호 */
	private String residentNo;
	/** 클레임요청일자 */
	private Timestamp clmreqDt;
	/** 클레임요청일시(yyyyMMddHHmmss) */
	private Timestamp clmreqDts;
	/** 클레임요청구분 */
	private String clmreqTpnm;
	/** 클레임요청구분코드 */
	private String clmreqTp;
	/** 수거방법 */
	private String rtnMthdTpnm;
	/** 수거방법코드 */
	private String rtnMthdTp;
	/** 무료반품쿠폰사용여부 */
	private String frtnCpTp;
	/** 클레임요청상태 */
	private String clmreqStatnm;
	/** 클레임요청상태코드 */
	private String clmreqStat;
	/** 클레임요청수량 */
	private long clmreqQty;
	/** 클레임요청철회일시 */
	private Timestamp clmreqCnclDts;
	/** 클레임요청사유 */
	private String clmreqRsnTpnm;
	/** 클레임요청사유코드 */
	private String clmreqRsnTp;
	/** 클레임요청상세사유 */
	private String clmreqRsnDtl;
	/** 클레임요청승인일자 */
	private Timestamp clmreqAcceptDts;
	/** 클레임요청거부일자 */
	private Timestamp clmreqRefuseDts;
	/** 클레임요청거부사유 */
	private String clmreqRefuseRsnDtl;
	/** 취소처리메모 */
	private String cancelProcNote;
	/** 취소처리자ID */
	private String cancelProcId;
	private String procFlag;
	
	
	/** 초기배송비 */
	private double initialDelvAmt;


	public String getOrdNo() {
		return ordNo;
	}


	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}


	public String getOrdSeq() {
		return ordSeq;
	}


	public void setOrdSeq(String ordSeq) {
		this.ordSeq = ordSeq;
	}


	public String getPaOrderGb() {
		return paOrderGb;
	}


	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}


	public String getClmreqSeq() {
		return clmreqSeq;
	}


	public void setClmreqSeq(String clmreqSeq) {
		this.clmreqSeq = clmreqSeq;
	}


	public String getOptPrdTp() {
		return optPrdTp;
	}


	public void setOptPrdTp(String optPrdTp) {
		this.optPrdTp = optPrdTp;
	}


	public String getWithdrawYn() {
		return withdrawYn;
	}


	public void setWithdrawYn(String withdrawYn) {
		this.withdrawYn = withdrawYn;
	}


	public Timestamp getOrderDt() {
		return orderDt;
	}


	public void setOrderDt(Timestamp orderDt) {
		this.orderDt = orderDt;
	}


	public Timestamp getOrderDts() {
		return orderDts;
	}


	public void setOrderDts(Timestamp orderDts) {
		this.orderDts = orderDts;
	}


	public Timestamp getPayDt() {
		return payDt;
	}


	public void setPayDt(Timestamp payDt) {
		this.payDt = payDt;
	}


	public String getOptParentSeq() {
		return optParentSeq;
	}


	public void setOptParentSeq(String optParentSeq) {
		this.optParentSeq = optParentSeq;
	}


	public String getPrdNm() {
		return prdNm;
	}


	public void setPrdNm(String prdNm) {
		this.prdNm = prdNm;
	}


	public String getPrdNo() {
		return prdNo;
	}


	public void setPrdNo(String prdNo) {
		this.prdNo = prdNo;
	}


	public String getOptPrdNo() {
		return optPrdNo;
	}


	public void setOptPrdNo(String optPrdNo) {
		this.optPrdNo = optPrdNo;
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


	public String getOptNm() {
		return optNm;
	}


	public void setOptNm(String optNm) {
		this.optNm = optNm;
	}


	public String getSelOptNm() {
		return selOptNm;
	}


	public void setSelOptNm(String selOptNm) {
		this.selOptNm = selOptNm;
	}


	public String getInOptNm() {
		return inOptNm;
	}


	public void setInOptNm(String inOptNm) {
		this.inOptNm = inOptNm;
	}


	public String getMemId() {
		return memId;
	}


	public void setMemId(String memId) {
		this.memId = memId;
	}


	public String getSupplyEntrNo() {
		return supplyEntrNo;
	}


	public void setSupplyEntrNo(String supplyEntrNo) {
		this.supplyEntrNo = supplyEntrNo;
	}


	public String getSupplyCtrtSeq() {
		return supplyCtrtSeq;
	}


	public void setSupplyCtrtSeq(String supplyCtrtSeq) {
		this.supplyCtrtSeq = supplyCtrtSeq;
	}


	public double getSaleUnitcost() {
		return saleUnitcost;
	}


	public void setSaleUnitcost(double saleUnitcost) {
		this.saleUnitcost = saleUnitcost;
	}


	public double getPreUseUnitcost() {
		return preUseUnitcost;
	}


	public void setPreUseUnitcost(double preUseUnitcost) {
		this.preUseUnitcost = preUseUnitcost;
	}


	public double getPreUseAmt() {
		return preUseAmt;
	}


	public void setPreUseAmt(double preUseAmt) {
		this.preUseAmt = preUseAmt;
	}


	public long getOrdQty() {
		return ordQty;
	}


	public void setOrdQty(long ordQty) {
		this.ordQty = ordQty;
	}


	public double getOrdAmt() {
		return ordAmt;
	}


	public void setOrdAmt(double ordAmt) {
		this.ordAmt = ordAmt;
	}


	public String getCurrentState() {
		return currentState;
	}


	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}


	public Timestamp getOrdclmStatDt() {
		return ordclmStatDt;
	}


	public void setOrdclmStatDt(Timestamp ordclmStatDt) {
		this.ordclmStatDt = ordclmStatDt;
	}


	public double getDcCouponAmt() {
		return dcCouponAmt;
	}


	public void setDcCouponAmt(double dcCouponAmt) {
		this.dcCouponAmt = dcCouponAmt;
	}


	public double getEntrDcCouponAmt() {
		return entrDcCouponAmt;
	}


	public void setEntrDcCouponAmt(double entrDcCouponAmt) {
		this.entrDcCouponAmt = entrDcCouponAmt;
	}


	public double getTotDcCouponAmt() {
		return totDcCouponAmt;
	}


	public void setTotDcCouponAmt(double totDcCouponAmt) {
		this.totDcCouponAmt = totDcCouponAmt;
	}


	public double getEntrDisUnitCost() {
		return entrDisUnitCost;
	}


	public void setEntrDisUnitCost(double entrDisUnitCost) {
		this.entrDisUnitCost = entrDisUnitCost;
	}


	public double getIpointDcUnitcost() {
		return ipointDcUnitcost;
	}


	public void setIpointDcUnitcost(double ipointDcUnitcost) {
		this.ipointDcUnitcost = ipointDcUnitcost;
	}


	public double getRealSaleUnitcost() {
		return realSaleUnitcost;
	}


	public void setRealSaleUnitcost(double realSaleUnitcost) {
		this.realSaleUnitcost = realSaleUnitcost;
	}


	public String getIsCollected() {
		return isCollected;
	}


	public void setIsCollected(String isCollected) {
		this.isCollected = isCollected;
	}


	public String getAbroadBsYn() {
		return abroadBsYn;
	}


	public void setAbroadBsYn(String abroadBsYn) {
		this.abroadBsYn = abroadBsYn;
	}


	public String getDelvsetlSeq() {
		return delvsetlSeq;
	}


	public void setDelvsetlSeq(String delvsetlSeq) {
		this.delvsetlSeq = delvsetlSeq;
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


	public double getDelAmt() {
		return delAmt;
	}


	public void setDelAmt(double delAmt) {
		this.delAmt = delAmt;
	}


	public double getDelvAmt() {
		return delvAmt;
	}


	public void setDelvAmt(double delvAmt) {
		this.delvAmt = delvAmt;
	}


	public double getAddDelvAmt() {
		return addDelvAmt;
	}


	public void setAddDelvAmt(double addDelvAmt) {
		this.addDelvAmt = addDelvAmt;
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


	public String getDelZipDoro() {
		return delZipDoro;
	}


	public void setDelZipDoro(String delZipDoro) {
		this.delZipDoro = delZipDoro;
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


	public String getDeliZipDoro() {
		return deliZipDoro;
	}


	public void setDeliZipDoro(String deliZipDoro) {
		this.deliZipDoro = deliZipDoro;
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


	public String getDelvComment() {
		return delvComment;
	}


	public void setDelvComment(String delvComment) {
		this.delvComment = delvComment;
	}


	public String getResidentNo() {
		return residentNo;
	}


	public void setResidentNo(String residentNo) {
		this.residentNo = residentNo;
	}


	public Timestamp getClmreqDt() {
		return clmreqDt;
	}


	public void setClmreqDt(Timestamp clmreqDt) {
		this.clmreqDt = clmreqDt;
	}


	public Timestamp getClmreqDts() {
		return clmreqDts;
	}


	public void setClmreqDts(Timestamp clmreqDts) {
		this.clmreqDts = clmreqDts;
	}


	public String getClmreqTpnm() {
		return clmreqTpnm;
	}


	public void setClmreqTpnm(String clmreqTpnm) {
		this.clmreqTpnm = clmreqTpnm;
	}


	public String getClmreqTp() {
		return clmreqTp;
	}


	public void setClmreqTp(String clmreqTp) {
		this.clmreqTp = clmreqTp;
	}


	public String getRtnMthdTpnm() {
		return rtnMthdTpnm;
	}


	public void setRtnMthdTpnm(String rtnMthdTpnm) {
		this.rtnMthdTpnm = rtnMthdTpnm;
	}


	public String getRtnMthdTp() {
		return rtnMthdTp;
	}


	public void setRtnMthdTp(String rtnMthdTp) {
		this.rtnMthdTp = rtnMthdTp;
	}


	public String getFrtnCpTp() {
		return frtnCpTp;
	}


	public void setFrtnCpTp(String frtnCpTp) {
		this.frtnCpTp = frtnCpTp;
	}


	public String getClmreqStatnm() {
		return clmreqStatnm;
	}


	public void setClmreqStatnm(String clmreqStatnm) {
		this.clmreqStatnm = clmreqStatnm;
	}


	public String getClmreqStat() {
		return clmreqStat;
	}


	public void setClmreqStat(String clmreqStat) {
		this.clmreqStat = clmreqStat;
	}


	public long getClmreqQty() {
		return clmreqQty;
	}


	public void setClmreqQty(long clmreqQty) {
		this.clmreqQty = clmreqQty;
	}


	public Timestamp getClmreqCnclDts() {
		return clmreqCnclDts;
	}


	public void setClmreqCnclDts(Timestamp clmreqCnclDts) {
		this.clmreqCnclDts = clmreqCnclDts;
	}


	public String getClmreqRsnTpnm() {
		return clmreqRsnTpnm;
	}


	public void setClmreqRsnTpnm(String clmreqRsnTpnm) {
		this.clmreqRsnTpnm = clmreqRsnTpnm;
	}


	public String getClmreqRsnTp() {
		return clmreqRsnTp;
	}


	public void setClmreqRsnTp(String clmreqRsnTp) {
		this.clmreqRsnTp = clmreqRsnTp;
	}


	public String getClmreqRsnDtl() {
		return clmreqRsnDtl;
	}


	public void setClmreqRsnDtl(String clmreqRsnDtl) {
		this.clmreqRsnDtl = clmreqRsnDtl;
	}


	public Timestamp getClmreqAcceptDts() {
		return clmreqAcceptDts;
	}


	public void setClmreqAcceptDts(Timestamp clmreqAcceptDts) {
		this.clmreqAcceptDts = clmreqAcceptDts;
	}


	public Timestamp getClmreqRefuseDts() {
		return clmreqRefuseDts;
	}


	public void setClmreqRefuseDts(Timestamp clmreqRefuseDts) {
		this.clmreqRefuseDts = clmreqRefuseDts;
	}


	public String getClmreqRefuseRsnDtl() {
		return clmreqRefuseRsnDtl;
	}


	public void setClmreqRefuseRsnDtl(String clmreqRefuseRsnDtl) {
		this.clmreqRefuseRsnDtl = clmreqRefuseRsnDtl;
	}


	public String getCancelProcNote() {
		return cancelProcNote;
	}


	public void setCancelProcNote(String cancelProcNote) {
		this.cancelProcNote = cancelProcNote;
	}


	public String getCancelProcId() {
		return cancelProcId;
	}


	public void setCancelProcId(String cancelProcId) {
		this.cancelProcId = cancelProcId;
	}


	public double getInitialDelvAmt() {
		return initialDelvAmt;
	}


	public void setInitialDelvAmt(double initialDelvAmt) {
		this.initialDelvAmt = initialDelvAmt;
	}


	public String getProcFlag() {
		return procFlag;
	}


	public void setProcFlag(String procFlag) {
		this.procFlag = procFlag;
	}
	
	
	
}
