package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractVO;

public class OrderInputVO extends AbstractVO {
	private static final long serialVersionUID = 1L;
	
	private String paOrderCode;
	private String paCode;
	private String doFlag;
	private String mappingSeq;
	private String orderDate;
	private String paGoodsCode;
	private String goodsCode;
	private String goodsdtCode;
	private int orderQty;
	private String applyDate;
	private double rsaleAmt;
	private double supplyPrice;
	private long sellerDcAmt;
	private long lumpSumDcAmt;
	private long lumpSumEntpDcAmt;
	private long lumpSumOwnDcAmt;
	private String custName;
	private String custTel1;
	private String custTel2;
	private String custChar;
	private String receiverName;
	private String receiverTel;
	private String receiverHp;
	private String receiverAddr;
	private String mediaCode;
	private String msg;
	private String rcvrBaseAddr; //배송지 기본주소
	private String rcvrDtlsAddr; //배송지 상세주소
	private String rcvrMailNo;   //배송지 우편번호
	private String rcvrMailNoSeq; //배송지 우편번호 순번
	private String typeAdd;   //주소유형  01: 
	private String isLocalYn;
	private String procUser; 
	private String receiveMethod;
	private long shpFeeCost;  // 11번가에 실제로 고객이 결제한 배송비
	private String priceSeq;
	private String orderNo;
	private String paGroupCode;
	private String sellerDcAmtExists;
	private OrderpromoVO orderPromo; // 즉시지급쿠폰 프로모션
	private OrderpromoVO lumpPromo;	 // 일시불 프로모션
	private OrderpromoVO orderPaPromo;	//제휴OUT프로모션 : 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
	private String promoNo;
	private double doAmt;
	private double ownCost;
	private double entpCost;
	private Timestamp couponPromoBdate;
    private Timestamp couponPromoEdate;
    
    private double salePrice;
    private double salPri; //판매가(하프클럽용)
    private double rsaleAmtSum;//하프클럽 용 
    
    
    public double getRsaleAmtSum() {
		return rsaleAmtSum;
	}
	public void setRsaleAmtSum(double rsaleAmtSum) {
		this.rsaleAmtSum = rsaleAmtSum;
	}
	public double getSalPri() {
		return salPri;
	}
	public void setSalPri(double salPri) {
		this.salPri = salPri;
	}
	private double instantCouponDiscount; //제휴사 즉시할인쿠폰 [2021-11-30 쿠팡 뿐만 아닌 타 제휴사 할인 금액 또한 사용 위하여 '제휴사로 변경']
	
	public String getSellerDcAmtExists() {
		return sellerDcAmtExists;
	}
	public void setSellerDcAmtExists(String sellerDcAmtExists) {
		this.sellerDcAmtExists = sellerDcAmtExists;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public String getReceiveMethod() {
		return receiveMethod;
	}
	public void setReceiveMethod(String receiveMethod) {
		this.receiveMethod = receiveMethod;
	}
	
	public long getShpFeeCost() {
		return shpFeeCost;
	}
	public void setShpFeeCost(long shpFeeCost) {
		this.shpFeeCost = shpFeeCost;
	}
	public String getPaGoodsCode() {
		return paGoodsCode;
	}
	public void setPaGoodsCode(String paGoodsCode) {
		this.paGoodsCode = paGoodsCode;
	}
	public String getPaOrderCode() {
		return paOrderCode;
	}
	public void setPaOrderCode(String paOrderCode) {
		this.paOrderCode = paOrderCode;
	}
	public String getDoFlag() {
		return doFlag;
	}
	public void setDoFlag(String doFlag) {
		this.doFlag = doFlag;
	}
	public String getProcUser() {
		return procUser;
	}
	public void setProcUser(String procUser) {
		this.procUser = procUser;
	}
	public String getIsLocalYn() {
		return isLocalYn;
	}
	public void setIsLocalYn(String isLocalYn) {
		this.isLocalYn = isLocalYn;
	}
	public String getAddrGbn() {
		return typeAdd;
	}
	public void setAddrGbn(String addrGbn) {
		this.typeAdd = addrGbn;
	}
	

	public String getPostNoSeq() {
		return rcvrMailNoSeq;
	}
	public void setPostNoSeq(String postNoSeq) {
		this.rcvrMailNoSeq = postNoSeq;
	}

	
	public String getPostNo() {
		return rcvrMailNo;
	}
	public void setPostNo(String postNo) {
		this.rcvrMailNo = postNo;
	}

	
	
	public String getStdAddrDT() {
		return rcvrDtlsAddr;
	}
	public void setStdAddrDT(String stdAddrDT) {
		this.rcvrDtlsAddr = stdAddrDT;
	}

	
	public String getStdAddr() {
		return rcvrBaseAddr;
	}
	public void setStdAddr(String stdAddr) {
		this.rcvrBaseAddr = stdAddr;
	}

	
	
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getMappingSeq() {
		return mappingSeq;
	}
	public void setMappingSeq(String mappingSeq) {
		this.mappingSeq = mappingSeq;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsdtCode() {
		return goodsdtCode;
	}
	public void setGoodsdtCode(String goodsdtCode) {
		this.goodsdtCode = goodsdtCode;
	}
	public int getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(int orderQty) {
		this.orderQty = orderQty;
	}
	public double getSupplyPrice() {
		return supplyPrice;
	}
	public void setSupplyPrice(double supplyPrice) {
		this.supplyPrice = supplyPrice;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCustChar() {
		return custChar;
	}
	public void setCustChar(String custChar) {
		this.custChar = custChar;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverTel() {
		return receiverTel;
	}
	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}
	public String getReceiverHp() {
		return receiverHp;
	}
	public void setReceiverHp(String receiverHp) {
		this.receiverHp = receiverHp;
	}
	public String getReceiverAddr() {
		return receiverAddr;
	}
	public void setReceiverAddr(String receiverAddr) {
		this.receiverAddr = receiverAddr;
	}
	public String getMediaCode() {
		return mediaCode;
	}
	public void setMediaCode(String mediaCode) {
		this.mediaCode = mediaCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCustTel1() {
		return custTel1;
	}
	public void setCustTel1(String custTel1) {
		this.custTel1 = custTel1;
	}
	public String getCustTel2() {
		return custTel2;
	}
	public void setCustTel2(String custTel2) {
		this.custTel2 = custTel2;
	}
	public double getRsaleAmt() {
		return rsaleAmt;
	}
	public void setRsaleAmt(double rsaleAmt) {
		this.rsaleAmt = rsaleAmt;
	}
	public long getSellerDcAmt() {
		return sellerDcAmt;
	}
	public void setSellerDcAmt(long sellerDcAmt) {
		this.sellerDcAmt = sellerDcAmt;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getPriceSeq() {
		return priceSeq;
	}
	public void setPriceSeq(String priceSeq) {
		this.priceSeq = priceSeq;
	}
	public OrderpromoVO getOrderPromo() {
		return orderPromo;
	}
	public void setOrderPromo(OrderpromoVO orderPromo) {
		this.orderPromo = orderPromo;
	}
	public long getLumpSumDcAmt() {
		return lumpSumDcAmt;
	}
	public void setLumpSumDcAmt(long lumpSumDcAmt) {
		this.lumpSumDcAmt = lumpSumDcAmt;
	}	
	public long getLumpSumEntpDcAmt() {
		return lumpSumEntpDcAmt;
	}
	public void setLumpSumEntpDcAmt(long lumpSumEntpDcAmt) {
		this.lumpSumEntpDcAmt = lumpSumEntpDcAmt;
	}
	public long getLumpSumOwnDcAmt() {
		return lumpSumOwnDcAmt;
	}
	public void setLumpSumOwnDcAmt(long lumpSumOwnDcAmt) {
		this.lumpSumOwnDcAmt = lumpSumOwnDcAmt;
	}
	public OrderpromoVO getLumpPromo() {
		return lumpPromo;
	}
	public void setLumpPromo(OrderpromoVO lumpPromo) {
		this.lumpPromo = lumpPromo;
	}
	public OrderpromoVO getOrderPaPromo() {
		return orderPaPromo;
	}
	public void setOrderPaPromo(OrderpromoVO orderPaPromo) {
		this.orderPaPromo = orderPaPromo;
	}
	public double getInstantCouponDiscount() {
		return instantCouponDiscount;
	}
	public void setInstantCouponDiscount(double instantCouponDiscount) {
		this.instantCouponDiscount = instantCouponDiscount;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getPromoNo() {
		return promoNo;
	}
	public void setPromoNo(String promoNo) {
		this.promoNo = promoNo;
	}
	public double getDoAmt() {
		return doAmt;
	}
	public void setDoAmt(double doAmt) {
		this.doAmt = doAmt;
	}
	public double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(double ownCost) {
		this.ownCost = ownCost;
	}
	public double getEntpCost() {
		return entpCost;
	}
	public void setEntpCost(double entpCost) {
		this.entpCost = entpCost;
	}
	public Timestamp getCouponPromoBdate() {
		return couponPromoBdate;
	}
	public void setCouponPromoBdate(Timestamp couponPromoBdate) {
		this.couponPromoBdate = couponPromoBdate;
	}
	public Timestamp getCouponPromoEdate() {
		return couponPromoEdate;
	}
	public void setCouponPromoEdate(Timestamp couponPromoEdate) {
		this.couponPromoEdate = couponPromoEdate;
	}
	public double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}
	
	
}
