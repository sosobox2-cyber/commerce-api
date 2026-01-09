package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Orderdt;

@SuppressWarnings("rawtypes")
public class OrderdtVO extends Orderdt implements Comparable {
	private static final long serialVersionUID = 1L;

    //= compare
    private OrderdtVO od = null;
    public int compareTo(Object o){
        int  rtn = 0;
    	od = (OrderdtVO)o;
    	rtn = (this.getGoodsCode()+this.getGoodsdtCode()).compareTo(od.getGoodsCode()+od.getGoodsdtCode());
    	return rtn;
    }

	private String stockChkPlace = "";
	private String cartGoodsGb = "";
	private String cartGoodsKey = "";
	private String cartGoodsPromoNo = "";
	private String goodsName = "";
	private String goodsSelectNo = "";
	private String norestAllotMonths;
	private String entpCode;
	private String custName;
	private String receiverPost;
	private long procPossQty;
	private double ownProcCost;		//= 프로모션 당사부담단가
    private double entpProcCost;	//= 프로모션 업체부담단가
    private double paShipCostAmt;	//= 제휴용 배송비(주문 테이블에 없음)
    private OrderpromoVO orderPromo; //= 판매가 기준변경 전용 프로모션 
    private OrderpromoVO orderPaPromo; //= 제휴OUT프로모션 : 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
    private int exchangeYn;
    
    private long arsDcAmt;
	private long lumpSumDcAmt;
	private double lumpSumOwnDcAmt;
    private double lumpSumEntpDcAmt;
    private String procGb;    
    private String paOrderNo;

	private String paCode;
	private double instantCouponDiscount;
	private String entpCodeOrg; 	
	
    public String getEntpCodeOrg() {
		return entpCodeOrg;
	}
	public void setEntpCodeOrg(String entpCodeOrg) {
		this.entpCodeOrg = entpCodeOrg;
	}
	public String getPaOrderNo() {
		return paOrderNo;
	}
	public void setPaOrderNo(String paOrderNo) {
		this.paOrderNo = paOrderNo;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getCartGoodsPromoNo() {
		return cartGoodsPromoNo;
	}
	public void setCartGoodsPromoNo(String cartGoodsPromoNo) {
		this.cartGoodsPromoNo = cartGoodsPromoNo;
	}
	public String getCartGoodsKey() {
		return cartGoodsKey;
	}
	public void setCartGoodsKey(String cartGoodsKey) {
		this.cartGoodsKey = cartGoodsKey;
	}
	public String getCartGoodsGb() {
		return cartGoodsGb;
	}
	public void setCartGoodsGb(String cartGoodsGb) {
		this.cartGoodsGb = cartGoodsGb;
	}
	public String getStockChkPlace() {
		return stockChkPlace;
	}
	public void setStockChkPlace(String stockChkPlace) {
		this.stockChkPlace = stockChkPlace;
	}
	public String getGoodsSelectNo() {
		return goodsSelectNo;
	}
	public void setGoodsSelectNo(String goodsSelectNo) {
		this.goodsSelectNo = goodsSelectNo;
	}
	public String getNorestAllotMonths() {
		return norestAllotMonths;
	}
	public void setNorestAllotMonths(String norestAllotMonths) {
		this.norestAllotMonths = norestAllotMonths;
	}
	public String getEntpCode() {
		return entpCode;
	}
	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getReceiverPost() {
		return receiverPost;
	}
	public void setReceiverPost(String receiverPost) {
		this.receiverPost = receiverPost;
	}
	public long getProcPossQty() {
		return procPossQty;
	}
	public void setProcPossQty(long procPossQty) {
		this.procPossQty = procPossQty;
	}
	public double getOwnProcCost() {
		return ownProcCost;
	}
	public void setOwnProcCost(double ownProcCost) {
		this.ownProcCost = ownProcCost;
	}
	public double getEntpProcCost() {
		return entpProcCost;
	}
	public void setEntpProcCost(double entpProcCost) {
		this.entpProcCost = entpProcCost;
	}
	public double getPaShipCostAmt() {
		return paShipCostAmt;
	}
	public void setPaShipCostAmt(double paShipCostAmt) {
		this.paShipCostAmt = paShipCostAmt;
	}
	public OrderpromoVO getOrderPromo() {
		return orderPromo;
	}
	public void setOrderPromo(OrderpromoVO orderPromo) {
		this.orderPromo = orderPromo;
	}
	public OrderpromoVO getOrderPaPromo() {
		return orderPaPromo;
	}
	public void setOrderPaPromo(OrderpromoVO orderPaPromo) {
		this.orderPaPromo = orderPaPromo;
	}
	public int getExchangeYn() {
		return exchangeYn;
	}
	public void setExchangeYn(int exchangeYn) {
		this.exchangeYn = exchangeYn;
	}
	public long getArsDcAmt() {
		return arsDcAmt;
	}
	public void setArsDcAmt(long arsDcAmt) {
		this.arsDcAmt = arsDcAmt;
	}
	public long getLumpSumDcAmt() {
		return lumpSumDcAmt;
	}
	public void setLumpSumDcAmt(long lumpSumDcAmt) {
		this.lumpSumDcAmt = lumpSumDcAmt;
	}
	public double getLumpSumOwnDcAmt() {
		return lumpSumOwnDcAmt;
	}
	public void setLumpSumOwnDcAmt(double lumpSumOwnDcAmt) {
		this.lumpSumOwnDcAmt = lumpSumOwnDcAmt;
	}
	public double getLumpSumEntpDcAmt() {
		return lumpSumEntpDcAmt;
	}
	public void setLumpSumEntpDcAmt(double lumpSumEntpDcAmt) {
		this.lumpSumEntpDcAmt = lumpSumEntpDcAmt;
	}
	public String getProcGb() {
		return procGb;
	}
	public void setProcGb(String procGb) {
		this.procGb = procGb;
	}
	public double getInstantCouponDiscount() {
		return instantCouponDiscount;
	}
	public void setInstantCouponDiscount(double instantCouponDiscount) {
		this.instantCouponDiscount = instantCouponDiscount;
	}
	
}
