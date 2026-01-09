package com.cware.netshopping.domain;


import java.sql.*;

import com.cware.netshopping.domain.model.Ordergoods;

/**
 *
 * @author Commerceware
 *
 */
public class OrdergoodsVO extends Ordergoods{

	private static final long serialVersionUID = 1L;

	private String goodsName;
	private String compGoodsCode;
	private Timestamp orderDate;
	private String mediaGb;
	private String mediaCode;
	private String receiverSeq;
	private String EntpCode;
	private String EntpName;
	private String orderDateStr;
	private long cancelQtyOrg;
	private long claimQtyOrg;
	private long claimCanQtyOrg;
	private long exchCntOrg;
	private long asCntOrg;
	private double shipFeeBaseAmt;
	private double shipFee;
	private double shipFeeReturn;
	private String saveamtFixRateYn;
	private long inGoodsCodeCount;
	private String custOrdQtyCheckYn;	//= 고객주문수량검사여부
	private long   custOrdQtyCheckTerm; //= 고객주문수량검사기간 0 - 평생, 1 - 당일, 1이상 - 일자체크
    private long   termOrderQty;		//= 기간내 판매수량
    private String invi_goods_type;
    private String single_order_yn;
    private String do_not_cash_yn;
    
    private String reserv_goods_yn;
    private String reserv_out_date;
    private String hope_dely_input_yn;
    private String emp_promo_yn;
    private String emp_promo_yn_010;
    private String do_not_island_dely;
    private String lgroup;
    private String addr_required_yn;

    private double tv_saveamt;
    

	public double getTv_saveamt() {
		return tv_saveamt;
	}

	public void setTv_saveamt(double tv_saveamt) {
		this.tv_saveamt = tv_saveamt;
	}

	public String getReserv_goods_yn() {
		return reserv_goods_yn;
	}

	public void setReserv_goods_yn(String reserv_goods_yn) {
		this.reserv_goods_yn = reserv_goods_yn;
	}

	public String getReserv_out_date() {
		return reserv_out_date;
	}

	public void setReserv_out_date(String reserv_out_date) {
		this.reserv_out_date = reserv_out_date;
	}

	public String getHope_dely_input_yn() {
		return hope_dely_input_yn;
	}

	public void setHope_dely_input_yn(String hope_dely_input_yn) {
		this.hope_dely_input_yn = hope_dely_input_yn;
	}

	public String getDo_not_cash_yn() {
		return do_not_cash_yn;
	}

	public void setDo_not_cash_yn(String do_not_cash_yn) {
		this.do_not_cash_yn = do_not_cash_yn;
	}

	public String getSingle_order_yn() {
		return single_order_yn;
	}

	public void setSingle_order_yn(String single_order_yn) {
		this.single_order_yn = single_order_yn;
	}

	public String getInvi_goods_type() {
		return invi_goods_type;
	}

	public void setInvi_goods_type(String invi_goods_type) {
		this.invi_goods_type = invi_goods_type;
	}

	public void initSheetOrdergoods(){
        setCwareAction        ( "" );
        setCwareInfo          ( "" );
        setGoodsName          ("");
        setGoodsCode          ("");
        setCompGoodsCode      ("");
        setSalePrice          (0);
        setSetYn              ("0");
        setEntpCode           ("");
        setOrderNo            ("");
        setOrderGSeq          ("");
        setCustNo             ("");
        setOrderDate          (null);
        setOrderGb            ("10");
        setPromoNo            ("");
        setOrderQty           (0);
        setCancelQty          (0);
        setReturnQty          (0);
        setExchCnt            (0);
        setAsCnt              (0);
        setDcRate             (0);
        setDcAmtGoods         (0);
        setDcAmtMemb          (0);
        setDcAmtDiv           (0);
        setDcAmt              (0);
        setMediaGb            ("01");
        setMediaCode          ("000");
        setReceiverSeq        ("");
        setNorestAllotMonths  ("");
        setShipFeeBaseAmt     (0);
        setShipFee            (0);
        setShipFeeReturn      (0);
        setSaveamtFixRateYn   ("0");
    }

	public long getCancelQtyOrg() {
		return cancelQtyOrg;
	}

	public void setCancelQtyOrg(long cancelQtyOrg) {
		this.cancelQtyOrg = cancelQtyOrg;
	}

	public long getClaimQtyOrg() {
		return claimQtyOrg;
	}

	public void setClaimQtyOrg(long claimQtyOrg) {
		this.claimQtyOrg = claimQtyOrg;
	}

	public long getClaimCanQtyOrg() {
		return claimCanQtyOrg;
	}

	public void setClaimCanQtyOrg(long claimCanQtyOrg) {
		this.claimCanQtyOrg = claimCanQtyOrg;
	}

	public long getExchCntOrg() {
		return exchCntOrg;
	}

	public void setExchCntOrg(long exchCntOrg) {
		this.exchCntOrg = exchCntOrg;
	}

	public long getAsCntOrg() {
		return asCntOrg;
	}

	public void setAsCntOrg(long asCntOrg) {
		this.asCntOrg = asCntOrg;
	}

	public String getOrderDateStr() {
		return orderDateStr;
	}
	public void setOrderDateStr(String orderDateStr) {
		this.orderDateStr = orderDateStr;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getCompGoodsCode() {
		return compGoodsCode;
	}
	public void setCompGoodsCode(String compGoodsCode) {
		this.compGoodsCode = compGoodsCode;
	}
	public Timestamp getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	public String getMediaGb() {
		return mediaGb;
	}
	public void setMediaGb(String mediaGb) {
		this.mediaGb = mediaGb;
	}
	public String getMediaCode() {
		return mediaCode;
	}
	public void setMediaCode(String mediaCode) {
		this.mediaCode = mediaCode;
	}
	public String getReceiverSeq() {
		return receiverSeq;
	}
	public void setReceiverSeq(String receiverSeq) {
		this.receiverSeq = receiverSeq;
	}
	public String getEntpCode() {
		return EntpCode;
	}
	public void setEntpCode(String entpCode) {
		EntpCode = entpCode;
	}

	public double getShipFeeBaseAmt() {
		return shipFeeBaseAmt;
	}

	public void setShipFeeBaseAmt(double shipFeeBaseAmt) {
		this.shipFeeBaseAmt = shipFeeBaseAmt;
	}

	public double getShipFee() {
		return shipFee;
	}

	public void setShipFee(double shipFee) {
		this.shipFee = shipFee;
	}

	public double getShipFeeReturn() {
		return shipFeeReturn;
	}

	public void setShipFeeReturn(double shipFeeReturn) {
		this.shipFeeReturn = shipFeeReturn;
	}

	public String getEntpName() {
		return EntpName;
	}

	public void setEntpName(String entpName) {
		EntpName = entpName;
	}

	public String getSaveamtFixRateYn() {
		return saveamtFixRateYn;
	}

	public void setSaveamtFixRateYn(String saveamtFixRateYn) {
		this.saveamtFixRateYn = saveamtFixRateYn;
	}

	public long getInGoodsCodeCount() {
		return inGoodsCodeCount;
	}

	public void setInGoodsCodeCount(long inGoodsCodeCount) {
		this.inGoodsCodeCount = inGoodsCodeCount;
	}

	public String getCustOrdQtyCheckYn() {
		return custOrdQtyCheckYn;
	}

	public void setCustOrdQtyCheckYn(String custOrdQtyCheckYn) {
		this.custOrdQtyCheckYn = custOrdQtyCheckYn;
	}

	public long getCustOrdQtyCheckTerm() {
		return custOrdQtyCheckTerm;
	}

	public void setCustOrdQtyCheckTerm(long custOrdQtyCheckTerm) {
		this.custOrdQtyCheckTerm = custOrdQtyCheckTerm;
	}

	public long getTermOrderQty() {
		return termOrderQty;
	}

	public void setTermOrderQty(long termOrderQty) {
		this.termOrderQty = termOrderQty;
	}
	
    public String getEmp_promo_yn() {
		return emp_promo_yn;
	}

	public void setEmp_promo_yn(String emp_promo_yn) {
		this.emp_promo_yn = emp_promo_yn;
	}
	
	public String getEmp_promo_yn_010() {
		return emp_promo_yn_010;
	}

	public void setEmp_promo_yn_010(String emp_promo_yn_010) {
		this.emp_promo_yn_010 = emp_promo_yn_010;
	}	

	public String getDo_not_island_dely() {
		return do_not_island_dely;
	}

	public void setDo_not_island_dely(String do_not_island_dely) {
		this.do_not_island_dely = do_not_island_dely;
	}
	
	public String getLgroup() {
		return lgroup;
	}

	public void setLgroup(String lgroup) {
		this.lgroup = lgroup;
	}

	public String getAddr_required_yn() {
		return addr_required_yn;
	}

	public void setAddr_required_yn(String addr_required_yn) {
		this.addr_required_yn = addr_required_yn;
	}
	
}