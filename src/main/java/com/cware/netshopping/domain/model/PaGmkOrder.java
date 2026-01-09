package com.cware.netshopping.domain.model;


import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaGmkOrder extends AbstractModel {
    
    private static final long serialVersionUID = 1L;
   
    private String paCode;
    private String paGroupCode;
    
    private String paDoFlag;
    private String payNo; //장바구니 번호
    private	String groupNo; //묶음 배송비 정책번호(배송 번호)
    private String contrNo; //주문번호 (Contr_No)
    private String contrNoSeq; //주문번호 순번
    
    private Timestamp orderDate; //주문일
    private Timestamp payDate;	 //결제일
    private Timestamp orderConfirmDate; //주문확인일
    private Timestamp buyCompleteDate;  //구매결정일
    private String	  goodsNo;	 //EMS상품번호
    private String	  siteGoodsNo;//사이트 상품번호
    private String    outGoodsNo; //판매자관리코드(판매자상품번호)
    private String	  goodsName;  //상품이름

    private	double	 salePrice;	   //판매단가	  
    private double	 paymentPrice; //판매금액
    private double	 acntPrice;	   //결제금액    
    private double   costPrice;    //공급원가
    private int		 contrQty ;	   //수량
    
    private double   sellerDiscountPrice;  //판매자 할인 금액 총액  (sellerDiscountPrice1+sellerDiscountPrice2)
    private double	 sellerDiscountPrice1; //판매자 쿠폰 할인 금액1
    private double	 sellerDiscountPrice2; //판매자 쿠폰 할인 금액2
    private	double	 directDiscountPrice;  //Ebay 할인금액
  
    private double	 singlePayDcAmt; //일시불할인
    private double	 multiBuyDcAmt;	 //복수구매 할인(auction)
    private double	 vipBuyDcAmt;	 //우수회원할인(auction)
    
    private String	 feeContidtion; //배송비 조건
    private double	 shippingFee;	//배송비
    private	double	 addShippingFee;//도서 산간 추가 배송비
    private double	 addJejuShippingFee; //제주 추가 배송비
    
    private Timestamp transDate;	//배송정보등록일
    private Timestamp transDueDate; //발송마감일
    private Timestamp transCompleteDate; //배송완료일
    private String	  transType;		 //발송정책
    
 
    
    private double	 settlePrice;	//정산예정금액
    private double	 serviceFee;	//서비스이용료
    private double	 sellerCashBackMoney; //판매자지급 스마일캐시
    
    
    private String	 itemOptionValue;		 //주문옵션
    private int		 itemOptionOrderCnt; 	 //주문옵션개수
    private String	 itemOptionCode;	     //주문옵션코드
    private String	 itemAddOptionValue;	 //추가구성
    private int		 itemAddOptionOrderCnt;  //추가구성개수
    private String	 itemAddOptionCode;	     //추가구성코드
    
    private	double	 optSelPrice; 			 //주문옵션추가금액
    private	double	 optAddPrice;			 //추가구성 금액
    
    private String	 buyerName;	//구매자명
    private String	 buyerId;	//구매자ID
    private String	 buyerMobile;//구매자 휴대폰
    private String	 buyerTel;	 //구매자 전화번호

    private String	 receiverName;	  //수령인명
    private String 	 receiverMobile;  //수령인휴대폰
    private String 	 receiverTel;	  //수령인 전화번호
    private String 	 receiverZipCode; //수령인 우편번호
    private String 	 receiverAddress; //수령인 주소(수령인주소1+2) // FullAddress
    private String 	 receiverAddress1;//수령인 주소1
    private String 	 receiverAddress2;//수령인 주소2
    
    private String    delMemo;		   //배송시 요구사항
    private Timestamp allocationDate1; //배송요청시작시간
    private Timestamp allocationDate2; //배송요청종료시간
    private String 	  delySlotId;	   //배송SlotID
    private double	  branchPrice;	   //지점 추가 금액
    private String	  replaceYn;	   //대체상품동의여부
    
    private String delyName; //택배사명
    private String delyNo;	 //송장번호
    private String overseaTransYn; //해외배송여부
    private String outOrderNo;	   //글로벌샵여부
    private String infoCin;		   //개인통관고유번호
   
    private String skuNo;  //SKU번호
    private int	   skuQty; //SKU수량

    private String optionGoods; //사은품
    private String inventoryNo;	//G마켓 히스토리 코드
    
    private String g9OrderYn;   
    
    private String isGiftOrder; // 선물하기 주문 구분
    private Timestamp giftConfirmDate; // 선물수락일
    private Timestamp giftConfirmDueDate; // 선물수락기한
    
    
    public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
    public String getPaDoFlag() {
		return paDoFlag;
	}
	public void setPaDoFlag(String paDoFlag) {
		this.paDoFlag = paDoFlag;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public String getContrNo() {
		return contrNo;
	}
	public void setContrNo(String contrNo) {
		this.contrNo = contrNo;
	}
	public String getContrNoSeq() {
		return contrNoSeq;
	}
	public void setContrNoSeq(String contrNoSeq) {
		this.contrNoSeq = contrNoSeq;
	}
	public Timestamp getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	public Timestamp getPayDate() {
		return payDate;
	}
	public void setPayDate(Timestamp payDate) {
		this.payDate = payDate;
	}
	public Timestamp getOrderConfirmDate() {
		return orderConfirmDate;
	}
	public void setOrderConfirmDate(Timestamp orderConfirmDate) {
		this.orderConfirmDate = orderConfirmDate;
	}
	public Timestamp getBuyCompleteDate() {
		return buyCompleteDate;
	}
	public void setBuyCompleteDate(Timestamp buyCompleteDate) {
		this.buyCompleteDate = buyCompleteDate;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getSiteGoodsNo() {
		return siteGoodsNo;
	}
	public void setSiteGoodsNo(String siteGoodsNo) {
		this.siteGoodsNo = siteGoodsNo;
	}
	public String getOutGoodsNo() {
		return outGoodsNo;
	}
	public void setOutGoodsNo(String outGoodsNo) {
		this.outGoodsNo = outGoodsNo;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}
	public double getPaymentPrice() {
		return paymentPrice;
	}
	public void setPaymentPrice(double paymentPrice) {
		this.paymentPrice = paymentPrice;
	}
	public double getAcntPrice() {
		return acntPrice;
	}
	public void setAcntPrice(double acntPrice) {
		this.acntPrice = acntPrice;
	}
	public double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}
	public int getContrQty() {
		return contrQty;
	}
	public void setContrQty(int contrQty) {
		this.contrQty = contrQty;
	}
	public double getSellerDiscountPrice() {
		return sellerDiscountPrice;
	}
	public void setSellerDiscountPrice(double sellerDiscountPrice) {
		this.sellerDiscountPrice = sellerDiscountPrice;
	}
	public double getSellerDiscountPrice1() {
		return sellerDiscountPrice1;
	}
	public void setSellerDiscountPrice1(double sellerDiscountPrice1) {
		this.sellerDiscountPrice1 = sellerDiscountPrice1;
	}
	public double getSellerDiscountPrice2() {
		return sellerDiscountPrice2;
	}
	public void setSellerDiscountPrice2(double sellerDiscountPrice2) {
		this.sellerDiscountPrice2 = sellerDiscountPrice2;
	}
	public double getDirectDiscountPrice() {
		return directDiscountPrice;
	}
	public void setDirectDiscountPrice(double directDiscountPrice) {
		this.directDiscountPrice = directDiscountPrice;
	}
	public double getSinglePayDcAmt() {
		return singlePayDcAmt;
	}
	public void setSinglePayDcAmt(double singlePayDcAmt) {
		this.singlePayDcAmt = singlePayDcAmt;
	}
	public double getMultiBuyDcAmt() {
		return multiBuyDcAmt;
	}
	public void setMultiBuyDcAmt(double multiBuyDcAmt) {
		this.multiBuyDcAmt = multiBuyDcAmt;
	}
	public double getVipBuyDcAmt() {
		return vipBuyDcAmt;
	}
	public void setVipBuyDcAmt(double vipBuyDcAmt) {
		this.vipBuyDcAmt = vipBuyDcAmt;
	}
	public String getFeeContidtion() {
		return feeContidtion;
	}
	public void setFeeContidtion(String feeContidtion) {
		this.feeContidtion = feeContidtion;
	}
	public double getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(double shippingFee) {
		this.shippingFee = shippingFee;
	}
	public double getAddShippingFee() {
		return addShippingFee;
	}
	public void setAddShippingFee(double addShippingFee) {
		this.addShippingFee = addShippingFee;
	}
	public double getAddJejuShippingFee() {
		return addJejuShippingFee;
	}
	public void setAddJejuShippingFee(double addJejuShippingFee) {
		this.addJejuShippingFee = addJejuShippingFee;
	}
	public Timestamp getTransDate() {
		return transDate;
	}
	public void setTransDate(Timestamp transDate) {
		this.transDate = transDate;
	}
	public Timestamp getTransDueDate() {
		return transDueDate;
	}
	public void setTransDueDate(Timestamp transDueDate) {
		this.transDueDate = transDueDate;
	}
	public Timestamp getTransCompleteDate() {
		return transCompleteDate;
	}
	public void setTransCompleteDate(Timestamp transCompleteDate) {
		this.transCompleteDate = transCompleteDate;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public double getSettlePrice() {
		return settlePrice;
	}
	public void setSettlePrice(double settlePrice) {
		this.settlePrice = settlePrice;
	}
	public double getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(double serviceFee) {
		this.serviceFee = serviceFee;
	}
	public double getSellerCashBackMoney() {
		return sellerCashBackMoney;
	}
	public void setSellerCashBackMoney(double sellerCashBackMoney) {
		this.sellerCashBackMoney = sellerCashBackMoney;
	}
	public String getItemOptionValue() {
		return itemOptionValue;
	}
	public void setItemOptionValue(String itemOptionValue) {
		this.itemOptionValue = itemOptionValue;
	}
	public int getItemOptionOrderCnt() {
		return itemOptionOrderCnt;
	}
	public void setItemOptionOrderCnt(int itemOptionOrderCnt) {
		this.itemOptionOrderCnt = itemOptionOrderCnt;
	}
	public String getItemOptionCode() {
		return itemOptionCode;
	}
	public void setItemOptionCode(String itemOptionCode) {
		this.itemOptionCode = itemOptionCode;
	}
	public String getItemAddOptionValue() {
		return itemAddOptionValue;
	}
	public void setItemAddOptionValue(String itemAddOptionValue) {
		this.itemAddOptionValue = itemAddOptionValue;
	}
	public int getItemAddOptionOrderCnt() {
		return itemAddOptionOrderCnt;
	}
	public void setItemAddOptionOrderCnt(int itemAddOptionOrderCnt) {
		this.itemAddOptionOrderCnt = itemAddOptionOrderCnt;
	}
	public String getItemAddOptionCode() {
		return itemAddOptionCode;
	}
	public void setItemAddOptionCode(String itemAddOptionCode) {
		this.itemAddOptionCode = itemAddOptionCode;
	}
	public double getOptSelPrice() {
		return optSelPrice;
	}
	public void setOptSelPrice(double optSelPrice) {
		this.optSelPrice = optSelPrice;
	}
	public double getOptAddPrice() {
		return optAddPrice;
	}
	public void setOptAddPrice(double optAddPrice) {
		this.optAddPrice = optAddPrice;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getBuyerMobile() {
		return buyerMobile;
	}
	public void setBuyerMobile(String buyerMobile) {
		this.buyerMobile = buyerMobile;
	}
	public String getBuyerTel() {
		return buyerTel;
	}
	public void setBuyerTel(String buyerTel) {
		this.buyerTel = buyerTel;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverMobile() {
		return receiverMobile;
	}
	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}
	public String getReceiverTel() {
		return receiverTel;
	}
	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}
	public String getReceiverZipCode() {
		return receiverZipCode;
	}
	public void setReceiverZipCode(String receiverZipCode) {
		this.receiverZipCode = receiverZipCode;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getReceiverAddress1() {
		return receiverAddress1;
	}
	public void setReceiverAddress1(String receiverAddress1) {
		this.receiverAddress1 = receiverAddress1;
	}
	public String getReceiverAddress2() {
		return receiverAddress2;
	}
	public void setReceiverAddress2(String receiverAddress2) {
		this.receiverAddress2 = receiverAddress2;
	}
	public String getDelMemo() {
		return delMemo;
	}
	public void setDelMemo(String delMemo) {
		this.delMemo = delMemo;
	}
	public Timestamp getAllocationDate1() {
		return allocationDate1;
	}
	public void setAllocationDate1(Timestamp allocationDate1) {
		this.allocationDate1 = allocationDate1;
	}
	public Timestamp getAllocationDate2() {
		return allocationDate2;
	}
	public void setAllocationDate2(Timestamp allocationDate2) {
		this.allocationDate2 = allocationDate2;
	}
	public String getDelySlotId() {
		return delySlotId;
	}
	public void setDelySlotId(String delySlotId) {
		this.delySlotId = delySlotId;
	}
	public double getBranchPrice() {
		return branchPrice;
	}
	public void setBranchPrice(double branchPrice) {
		this.branchPrice = branchPrice;
	}
	public String getReplaceYn() {
		return replaceYn;
	}
	public void setReplaceYn(String replaceYn) {
		this.replaceYn = replaceYn;
	}
	public String getDelyName() {
		return delyName;
	}
	public void setDelyName(String delyName) {
		this.delyName = delyName;
	}
	public String getDelyNo() {
		return delyNo;
	}
	public void setDelyNo(String delyNo) {
		this.delyNo = delyNo;
	}
	public String getOverseaTransYn() {
		return overseaTransYn;
	}
	public void setOverseaTransYn(String overseaTransYn) {
		this.overseaTransYn = overseaTransYn;
	}
	public String getOutOrderNo() {
		return outOrderNo;
	}
	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}
	public String getInfoCin() {
		return infoCin;
	}
	public void setInfoCin(String infoCin) {
		this.infoCin = infoCin;
	}
	public String getSkuNo() {
		return skuNo;
	}
	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}
	public int getSkuQty() {
		return skuQty;
	}
	public void setSkuQty(int skuQty) {
		this.skuQty = skuQty;
	}
	public String getOptionGoods() {
		return optionGoods;
	}
	public void setOptionGoods(String optionGoods) {
		this.optionGoods = optionGoods;
	}
	public String getInventoryNo() {
		return inventoryNo;
	}
	public void setInventoryNo(String inventoryNo) {
		this.inventoryNo = inventoryNo;
	}
	public String getG9OrderYn() {
		return g9OrderYn;
	}
	public void setG9OrderYn(String g9OrderYn) {
		this.g9OrderYn = g9OrderYn;
	}
	public String getIsGiftOrder() {
		return isGiftOrder;
	}
	public void setIsGiftOrder(String isGiftOrder) {
		this.isGiftOrder = isGiftOrder;
	}
	public Timestamp getGiftConfirmDate() {
		return giftConfirmDate;
	}
	public void setGiftConfirmDate(Timestamp giftConfirmDate) {
		this.giftConfirmDate = giftConfirmDate;
	}
	public Timestamp getGiftConfirmDueDate() {
		return giftConfirmDueDate;
	}
	public void setGiftConfirmDueDate(Timestamp giftConfirmDueDate) {
		this.giftConfirmDueDate = giftConfirmDueDate;
	}
}
