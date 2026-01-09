/*package com.cware.netshopping.pagmkt.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.lang.StringUtils;

import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.domain.PaGmktGoodsCertiVO;
import com.cware.netshopping.domain.PaGmktGoodsOfferVO;
import com.cware.netshopping.domain.PaGmktGoodsPriceVO;
import com.cware.netshopping.domain.PaGmktGoodsVO;
import com.cware.netshopping.domain.PaGmktGoodsdtMappingVO;
import com.cware.netshopping.domain.model.Config;

public class PaGmktSoapBodyMessageCreator {

    
	*//**
	 * G마켓 REQUEST AddItem MESSAGE 설정
	 * @param SOAPMessage
	 * @param PaGmktGoodsVO
	 * @param Config
	 * @return SOAPMessage
	 * @throws Exception
	 *//*
    	public SOAPMessage createAddItemBody(SOAPMessage soapMessage, PaGmktGoodsVO paGmktGoods, Config imageUrl) throws Exception {
	    
	    // SOAP FACTORY
    	    SOAPFactory soapFactory = SOAPFactory.newInstance();
	    
	    // SOAP Body
	    SOAPBody requestSoapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
	    QName addItemQName = new QName(ConfigUtil.getString("PAGMKT_COM_BASE_URL"), "AddItem");
	    SOAPElement addItemRoot = requestSoapBody.addChildElement(addItemQName);
	    
	    // addItem
	    SOAPElement addItem = addItemRoot.addChildElement("AddItem");
	    addItem.addAttribute(soapFactory.createName("OutItemNo"), paGmktGoods.getGoodsCode()); //외부상품번호 //필수
	    addItem.addAttribute(soapFactory.createName("CategoryCode"), paGmktGoods.getPaSgroup()); //소분류코드 //필수
	    if(StringUtils.isNotBlank(paGmktGoods.getItemNo())){
		addItem.addAttribute(soapFactory.createName("GmktItemNo"), paGmktGoods.getItemNo()); //G마켓 상품번호
	    }
	    addItem.addAttribute(soapFactory.createName("ItemName"), paGmktGoods.getGoodsName()); //상품명 //필수
	    //addItem.addAttribute(soapFactory.createName("ItemEngName"), "string"); //영문상품명
	    //addItem.addAttribute(soapFactory.createName("ItemDescription"), "string"); //상품상세정보 //필수 //<![CDATA[]]> ????
	    addItem.addAttribute(soapFactory.createName("GdHtml"), paGmktGoods.getDescribeExt()); //New 상품 상세정보- 상품정보
	    //addItem.addAttribute(soapFactory.createName("GdAddHtml"), "string"); //New 상품 상세정보- 추가구성
	    //addItem.addAttribute(soapFactory.createName("GdPrmtHtml"), "string"); //New 상품 상세정보- 광고/홍보

	    if(StringUtils.isNotBlank(paGmktGoods.getMakerNo())){
		addItem.addAttribute(soapFactory.createName("MakerNo"), paGmktGoods.getMakerNo()); //제조사 번호
	    }
	    if(StringUtils.isNotBlank(paGmktGoods.getBrandNo())){
		addItem.addAttribute(soapFactory.createName("BrandNo"), paGmktGoods.getBrandNo()); //브랜드 번호
	    }
	    //addItem.addAttribute(soapFactory.createName("ModelName"), "string"); //모델명
	    addItem.addAttribute(soapFactory.createName("IsAdult"), String.valueOf(Constants.PA_GMKT_NUM_Y.equals(paGmktGoods.getAdultYn()))); //성인용품여부 //필수
	    addItem.addAttribute(soapFactory.createName("Tax"), paGmktGoods.getTax()); //부가세 면세여부 //필수
	    //addItem.addAttribute(soapFactory.createName("MadeDate"), "date"); //제조(출판)년월일
	    //addItem.addAttribute(soapFactory.createName("AppearedDate"), "date"); //출시년월
	    //addItem.addAttribute(soapFactory.createName("ExpirationDate"), "date"); //유효일
	    //addItem.addAttribute(soapFactory.createName("FreeGift"), "string"); //사은품
	    addItem.addAttribute(soapFactory.createName("ItemKind"), "Shipping"); //상품종류 //Shipping or Ecoupon
	    //addItem.addAttribute(soapFactory.createName("InventoryNo"), "string"); //판매자관리코드
	    //addItem.addAttribute(soapFactory.createName("ItemWeight"), "double"); //상품무게
	    addItem.addAttribute(soapFactory.createName("IsOverseaTransGoods"), "false"); //해외배송가능여부
	    //addItem.addAttribute(soapFactory.createName("FreeDelFeeType"), "int"); //무료배송타입
	    //addItem.addAttribute(soapFactory.createName("IsGmktDiscount"), "boolean"); //G마켓 할인적용 여부 //boolean //미입력시 동의

	    // addItem > ReferencePrice
	    //QName referencePriceQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "ReferencePrice");
	    //SOAPElement referencePrice = addItem.addChildElement(referencePriceQName);
	    //referencePrice.addAttribute(soapFactory.createName("Kind"), "Quotation or Department or HomeShopping"); //참고가격 종류
	    //referencePrice.addAttribute(soapFactory.createName("Price"), "decimal"); //참고가격
	    
	    // addItem > Refusal
	    QName refusalQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "Refusal");
	    SOAPElement refusal = addItem.addChildElement(refusalQName);
	    refusal.addAttribute(soapFactory.createName("IsPriceCompare"), "false"); //가격비교 노출제외 //boolean
	    refusal.addAttribute(soapFactory.createName("IsNego"), "true"); //흥정하기 노출제외 //boolean
	    refusal.addAttribute(soapFactory.createName("IsJaehuDiscount"), "false"); //제휴할인 제한 //boolean
	    refusal.addAttribute(soapFactory.createName("IsPack"), "false"); //장바구니 불가 //boolean
	    
	    // addItem > ItemImage
	    QName itemImageQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "ItemImage");
	    SOAPElement itemImage = addItem.addChildElement(itemImageQName);
	    itemImage.addAttribute(soapFactory.createName("DefaultImage"), imageUrl.getVal()+paGmktGoods.getImageUrl()+paGmktGoods.getImageP()); //상품 기본이미지 URL //필수
	    
	    if(StringUtils.isNotBlank(paGmktGoods.getImageAP())){
	    	itemImage.addAttribute(soapFactory.createName("AddImage1"), imageUrl.getVal()+paGmktGoods.getImageUrl()+paGmktGoods.getImageAP()); //상품 추가 이미지1
	    }
	    if(StringUtils.isNotBlank(paGmktGoods.getImageBP())){
	        itemImage.addAttribute(soapFactory.createName("AddImage2"), imageUrl.getVal()+paGmktGoods.getImageUrl()+paGmktGoods.getImageBP()); //상품 추가 이미지2
	    }
	    
	    // addItem > As
	    QName asQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "As");
	    SOAPElement as = addItem.addChildElement(asQName);
	    as.addAttribute(soapFactory.createName("Telephone"), "신세계티비쇼핑 고객센터 : "+paGmktGoods.getCsTel()); //연락처 //필수
	    as.addAttribute(soapFactory.createName("Address"), "Seller"); //AS센터 주소/정보 //필수 //"Manufacturing_Seller or Seller"
	    
	    // addItem > Shipping
	    QName shippingQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "Shipping");
	    SOAPElement shipping = addItem.addChildElement(shippingQName);
	    
	    if(StringUtils.isBlank(paGmktGoods.getGroupCode())){
		shipping.addAttribute(soapFactory.createName("SetType"), "New"); //배송비 구분 //필수

		// addItem > Shipping > NewItemShipping
		SOAPElement newItemShipping = shipping.addChildElement("NewItemShipping");
		
		//상품별 배송비 종류
		//Free or ConditionalFee or PayOnDelivery or PrepayableOnDelivery or FixedFee
		
		switch(StringUtils.substring(paGmktGoods.getShipCostCode(), 0, 1)){
		 case "A" : newItemShipping.addAttribute(soapFactory.createName("FeeCondition"), "Free");
		 	    newItemShipping.addAttribute(soapFactory.createName("Fee"), "0"); //배송비 //decimal
		 	    break;
		 case "B" : newItemShipping.addAttribute(soapFactory.createName("FeeCondition"), "ConditionalFee"); //상품별 배송비 종류
		            newItemShipping.addAttribute(soapFactory.createName("FeeBasePrice"), String.valueOf(paGmktGoods.getShipCostBaseAmt())); //상품별 배송비 조건
		            newItemShipping.addAttribute(soapFactory.createName("Fee"), String.valueOf(paGmktGoods.getOrdCost())); //배송비
		 	    break;
		 case "C" : newItemShipping.addAttribute(soapFactory.createName("FeeCondition"), "FixedFee"); 
	 	   	    newItemShipping.addAttribute(soapFactory.createName("Fee"), String.valueOf(paGmktGoods.getOrdCost())); //배송비
	 	   	    break;
		 default : newItemShipping.addAttribute(soapFactory.createName("FeeCondition"), "FixedFee"); 
		 	   newItemShipping.addAttribute(soapFactory.createName("Fee"), String.valueOf(paGmktGoods.getOrdCost())); //배송비
		 	   break;
		}
		
		 2차 배송비 적용시 수정 예정 start 
		newItemShipping.addAttribute(soapFactory.createName("FeeCondition"), "Free");
	 	newItemShipping.addAttribute(soapFactory.createName("Fee"), "0"); //배송비 //decimal
	 	 2차 배송비 적용시 수정 예정 end 
		
	    }else {
		shipping.addAttribute(soapFactory.createName("SetType"), "Use"); //배송비 구분 //필수
		shipping.addAttribute(soapFactory.createName("GroupCode"), paGmktGoods.getGroupCode()); //배송비 그룹코드
	    }
	    
	    //개별배송비는 묶음번호 제외 처리
	    //if(!"C".equals(StringUtils.substring(paGmktGoods.getShipCostCode(), 0, 1))){
		//shipping.addAttribute(soapFactory.createName("BundleNo"), paGmktGoods.getBundleNo()); //묶음 번호
	    //}
	    shipping.addAttribute(soapFactory.createName("TransPolicyNo"), paGmktGoods.getTransPolicyNo()); //배송비정책 //"long"
	    shipping.addAttribute(soapFactory.createName("RefundAddrNum"), paGmktGoods.getAddrSeq()); //반품배송지번호 //int
	    
	    if(paGmktGoods.getOrderMinQty() > 0){
		// addItem > BundleOrder
		QName bundleOrderQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "BundleOrder");
		SOAPElement bundleOrder = addItem.addChildElement(bundleOrderQName);
		bundleOrder.addAttribute(soapFactory.createName("BuyUnitCount"), "1"); //구매수량단위
		bundleOrder.addAttribute(soapFactory.createName("MinBuyCount"), String.valueOf(paGmktGoods.getOrderMinQty())); //최소구매수량
	    }
	    
	    
	    if(Constants.PA_GMKT_NUM_Y.equals(paGmktGoods.getCustOrdQtyCheckYn())){
	    	//addItem > OrderLimit
	    	QName orderLimitQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "OrderLimit");
	    	SOAPElement orderLimit = addItem.addChildElement(orderLimitQName);
		orderLimit.addAttribute(soapFactory.createName("OrderLimitCount"), String.valueOf(paGmktGoods.getTermOrderQty())); //최대구매가능수량
		orderLimit.addAttribute(soapFactory.createName("OrderLimitPeriod"), String.valueOf(paGmktGoods.getCustOrdQtyCheckTerm())); //구매수량확인주기
	    }else {
	    
		if(paGmktGoods.getOrderMaxQty() > 0){
		    //addItem > OrderLimit
		    QName orderLimitQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "OrderLimit");
		    SOAPElement orderLimit = addItem.addChildElement(orderLimitQName);
		    orderLimit.addAttribute(soapFactory.createName("OrderLimitCount"), String.valueOf(paGmktGoods.getOrderMaxQty())); //최대구매가능수량
		    orderLimit.addAttribute(soapFactory.createName("OrderLimitPeriod"), "0"); //구매수량확인주기
		}
	    
	    }
	    
	    
	    // addItem > AttributeCode
	    //QName attributeCodeQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "AttributeCode");
	    //SOAPElement attributeCode = addItem.addChildElement(attributeCodeQName);
	    //attributeCode.addAttribute(soapFactory.createName("AttributeCode"), "int"); //분류속성코드
	    
	    // addItem > Origin
	    QName originQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "Origin");
	    SOAPElement origin = addItem.addChildElement(originQName);
	    origin.addAttribute(soapFactory.createName("Code"), paGmktGoods.getOriginEnum()); //원산지 구분 //필수 //Unknown or Domestic or Foreign or Etc
	    origin.addAttribute(soapFactory.createName("Place"), paGmktGoods.getOriginName()); //원산지명
	    
	    // addItem > Book
	    //QName bookQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "Book");
	    //SOAPElement book = addItem.addChildElement(bookQName);
	    //book.addAttribute(soapFactory.createName("ISBN"), "string"); //도서ISBN 코드
	    
	    // addItem > GoodsKind
	    //QName goodsKindQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "GoodsKind");
	    //SOAPElement goodsKind = addItem.addChildElement(goodsKindQName);
	    //goodsKind.addAttribute(soapFactory.createName("GoodsKind"), "Unknown or New or Stock or Used or Returned or Displayed or Refurbished"); //상품상태
	    //goodsKind.addAttribute(soapFactory.createName("GoodsStatus"), "string"); //
	    //None or Under3Months or Under6Months or Under1Year or Over2Years or NotUsed or AlmostNew or Fine or Old or ForCollect or Sealed or Unsealed or UsedAfterUnsealed or DisplayedNotUsed or DisplayedAlmostNew or DisplayedFine or DisplayedOld or DisplayedForCollect
	    //goodsKind.addAttribute(soapFactory.createName("GoodsTag"), "string"); //
	    //Default or New or Hot or Sale or MDRecommend or InterestFree or Limited or Gift or LowestPrice or NoMargin or Donation or SpecialBargain or EyeCatch or PowerDealer or Premium2Days or Premium7Days or Premium2Weeks or Premium1Month or Premium2Month or Premium3Month or ImmediateDelivery or Patronage or PremiumPlus
	    
	    soapMessage.saveChanges();
	    
	    return soapMessage;
	}
    	
	*//**
	 * G마켓 REQUEST AddPrice MESSAGE 설정
	 * @param SOAPMessage
	 * @param PaGmktGoodsPriceVO
	 * @return SOAPMessage
	 * @throws Exception
	 *//*
	public SOAPMessage createAddPriceBody(SOAPMessage soapMessage, PaGmktGoodsPriceVO paGmktGoodsPrice) throws Exception {
	    // SOAP FACTORY
    	    SOAPFactory soapFactory = SOAPFactory.newInstance();

	    // SOAP Body
	    SOAPBody requestSoapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
	    QName addPriceQName = new QName(ConfigUtil.getString("PAGMKT_COM_BASE_URL"), "AddPrice");
	    SOAPElement addPriceRoot = requestSoapBody.addChildElement(addPriceQName);

	    // AddPrice
	    SOAPElement addPrice = addPriceRoot.addChildElement("AddPrice");
	    addPrice.addAttribute(soapFactory.createName("GmktItemNo"), paGmktGoodsPrice.getItemNo()); //G마켓 상품번호 //"string" 
	    addPrice.addAttribute(soapFactory.createName("DisplayDate"), paGmktGoodsPrice.getDisplayDate().toString()); //주문기간 //"date"
	    addPrice.addAttribute(soapFactory.createName("SellPrice"), paGmktGoodsPrice.getSalePrice()); //판매가격 //"decimal"
	    addPrice.addAttribute(soapFactory.createName("StockQty"), String.valueOf(paGmktGoodsPrice.getTransOrderAbleQty())); //재고수량 //"int"
	    if(StringUtils.isNotBlank(paGmktGoodsPrice.getPaOptionCode())){
		addPrice.addAttribute(soapFactory.createName("InventoryNo"), paGmktGoodsPrice.getPaOptionCode()); //판매자관리코드 //"string"
	    }

	    soapMessage.saveChanges();
	    
	    return soapMessage;
	}
	
    	
	*//**
	 * G마켓 REQUEST AddPremiumItem MESSAGE 설정
	 * @param SOAPMessage
	 * @param PaGmktGoodsPriceVO
	 * @return SOAPMessage
	 * @throws Exception
	 *//*
	public SOAPMessage createAddPremiumItemBody(SOAPMessage soapMessage, PaGmktGoodsPriceVO paGmktGoodsPrice) throws Exception {
	    // SOAP FACTORY
    	    SOAPFactory soapFactory = SOAPFactory.newInstance();

	    // SOAP Body
	    SOAPBody requestSoapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
	    QName addPremiumItemQName = new QName(ConfigUtil.getString("PAGMKT_COM_BASE_URL"), "AddPremiumItem");
	    SOAPElement addPremiumItemRoot = requestSoapBody.addChildElement(addPremiumItemQName);

	    // AddPremiumItem
	    SOAPElement addPremiumItem = addPremiumItemRoot.addChildElement("AddPremiumItem");
	    addPremiumItem.addAttribute(soapFactory.createName("GmktItemNo"), paGmktGoodsPrice.getItemNo()); //G마켓 상품번호 //"string" 
	    
	    // AddPremiumItem > Discount
	    QName discountQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "Discount");
	    SOAPElement discount = addPremiumItem.addChildElement(discountQName);
	    
	    discount.addAttribute(soapFactory.createName("IsDiscount"), "true"); //판매자 할인 사용여부 //"boolean"
	    discount.addAttribute(soapFactory.createName("DiscountPrice"), StringUtils.isNotBlank(paGmktGoodsPrice.getDcAmt())? paGmktGoodsPrice.getDcAmt(): "0"); //판매자할인율/할인금액 //"decimal"
	    
	    //discount.addAttribute(soapFactory.createName("DiscountPrice2"), "decimal"); //판매자할인율/할인금액 (필수아님) //"decimal"
	    discount.addAttribute(soapFactory.createName("DiscountUnit"), "Money"); //판매자할인구분 (정률, 정액) //"Money or Rate"
	    
	    // AddPremiumItem > Discount > DiscountDate
	    SOAPElement discountDate = discount.addChildElement(soapFactory.createName("DiscountDate"));
	    discountDate.addAttribute(soapFactory.createName("StartDate"), paGmktGoodsPrice.getDiscountStartDate()); //판매자 할인 시작날짜(From) //date
	    discountDate.addAttribute(soapFactory.createName("EndDate"), "9999-12-31"); //판매자 할인 종료날짜 (To) //date

	    // AddPremiumItem > Mileage
	    //QName mileageQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "Mileage");
	    //SOAPElement mileage = addPremiumItem.addChildElement(mileageQName); 
	    //mileage.addAttribute(soapFactory.createName("IsMileage"), "boolean"); //마일리지 지급 사용 여부
	    //mileage.addAttribute(soapFactory.createName("MileageRate"), "decimal"); //마일리지 지급
	    
	    // AddPremiumItem > GStamp
	    //QName gStampQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "GStamp");
	    //SOAPElement gStamp = addPremiumItem.addChildElement(gStampQName);
	    //gStamp.addAttribute(soapFactory.createName("IsGStamp"), "boolean"); //G스탬프 사용 여부
	    //gStamp.addAttribute(soapFactory.createName("GStampCount"), "int"); //상품당 G스탬프 개수
	    
	    // AddPremiumItem > GStamp > GStampDate
	    //SOAPElement gStampDate = gStamp.addChildElement(soapFactory.createName("GStampDate"));
	    //gStampDate.addAttribute(soapFactory.createName("StartDate"), "date"); //스탬프 - 시작일 
	    //gStampDate.addAttribute(soapFactory.createName("EndDate"), "date"); //스탬프 - 종류일
	    
	    // AddPremiumItem > BundleDiscount
	    //QName bundleDiscountQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "BundleDiscount");
	    //SOAPElement bundleDiscount = addPremiumItem.addChildElement(bundleDiscountQName);
	    //bundleDiscount.addAttribute(soapFactory.createName("IsBundleDiscount"), "boolean"); //묶음할인 적용여부
	    //bundleDiscount.addAttribute(soapFactory.createName("BundleDiscountType"), "S or P"); //묶음할인 타입 S:복수개 구매할인혜택, P:N+1덤 할인혜택

	    // AddPremiumItem > BundleDiscount > BundleDiscountDate
	    //SOAPElement bundleDiscountDate = bundleDiscount.addChildElement(soapFactory.createName("BundleDiscountDate"));
	    //bundleDiscountDate.addAttribute(soapFactory.createName("StartDate"), "date"); //묶음할인 시작일
	    //bundleDiscountDate.addAttribute(soapFactory.createName("EndDate"), "date"); //묶음할인 종료일
	    
	    // AddPremiumItem > BundleDiscount > MultiplePurchaseDiscount
	    //SOAPElement multiplePurchaseDiscount = bundleDiscount.addChildElement(soapFactory.createName("MultiplePurchaseDiscount"));
	    //multiplePurchaseDiscount.addAttribute(soapFactory.createName("MultiplePurchaseDiscountUnit"), "Quantity or Money"); //복수구매단위 수량,금액
	    //multiplePurchaseDiscount.addAttribute(soapFactory.createName("MultiplePurchaseDiscountPrice"), "decimal"); //복수구매구입수량 OR 금액 (기준)
	    //multiplePurchaseDiscount.addAttribute(soapFactory.createName("MultiplePurchaseDiscountRate"), "decimal"); //복구구매 금액 OR 할인율
	    //multiplePurchaseDiscount.addAttribute(soapFactory.createName("DiscountUnit"), "Money or Rate"); //판매자 할인 구분 (정률, 정액)
	    
	    // AddPremiumItem > BundleDiscount > NPlusOneBonusDiscount
	    //SOAPElement nPlusOneBonusDiscount = bundleDiscount.addChildElement(soapFactory.createName("NPlusOneBonusDiscount"));
	    //nPlusOneBonusDiscount.addAttribute(soapFactory.createName("NPlusOneBonusDiscountCount"), "decimal"); //N+1덤 할인 기준 개수
	    
	    soapMessage.saveChanges();
	    
	    return soapMessage;
	}
    
    	
	*//**
	 * G마켓 REQUEST AddItemOption MESSAGE 설정
	 * @param SOAPMessage
	 * @param PaGmktGoodsdtMappingVO
	 * @return SOAPMessage
	 * @throws Exception
	 *//*
	public SOAPMessage createAddItemOptionBody(SOAPMessage soapMessage, List<PaGmktGoodsdtMappingVO> paGmktGoodsdtList) throws Exception {
	    
	    // SOAP FACTORY
	    SOAPFactory soapFactory = SOAPFactory.newInstance();

	    // SOAP Body
	    SOAPBody requestSoapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
	    QName addItemOptionQName = new QName(ConfigUtil.getString("PAGMKT_COM_BASE_URL"), "AddItemOption");
	    SOAPElement addItemOptionRoot = requestSoapBody.addChildElement(addItemOptionQName);

	    // AddItemOption
	    SOAPElement addItemOption = addItemOptionRoot.addChildElement("AddItemOption");
	    addItemOption.addAttribute(soapFactory.createName("GmktItemNo"), paGmktGoodsdtList.get(0).getItemNo()); //G마켓 상품번호
		
	    // AddItemOption < ItemSelectionList
	    QName itemSelectionListQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "ItemSelectionList");
	    SOAPElement itemSelectionList = addItemOption.addChildElement(itemSelectionListQName);
	    itemSelectionList.addAttribute(soapFactory.createName("IsInventory"), "true"); //재고사용여부 //"boolean"
	    
	    //정렬순서 TV쇼핑 (00:등록순, 01:가나다순, 02:가격낮은순)
	    //Register or Price or Name
	    switch(paGmktGoodsdtList.get(0).getSortType()){
	    	case "00" : paGmktGoodsdtList.get(0).setOptionSortType("Register"); break;
	    	case "01" : paGmktGoodsdtList.get(0).setOptionSortType("Name"); break;
	    	case "02" : paGmktGoodsdtList.get(0).setOptionSortType("Price"); break;
	    	default : paGmktGoodsdtList.get(0).setOptionSortType("Register"); break;
	    }
	    itemSelectionList.addAttribute(soapFactory.createName("OptionSortType"), paGmktGoodsdtList.get(0).getOptionSortType()); //정렬순서
	    //itemSelectionList.addAttribute(soapFactory.createName("IsCombination"), "boolean"); //조합형사용
	    //itemSelectionList.addAttribute(soapFactory.createName("OptionImageLevel"), "int"); //옵션이미지 레벨

	    for (PaGmktGoodsdtMappingVO paGmktGoodsdt : paGmktGoodsdtList){
		// AddItemOption < ItemSelectionList < itemSelection
    	    	//QName itemSelectionQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "ItemSelection");
    	    	SOAPElement itemSelection = itemSelectionList.addChildElement("ItemSelection");
    	    	itemSelection.addAttribute(soapFactory.createName("Name"), "옵션명"); //정보명 //색상^|^사이즈^|^사은품 //paGmktGoodsdt.getGoodsdtInfoKind()
    	    	itemSelection.addAttribute(soapFactory.createName("Value"), paGmktGoodsdt.getGoodsdtInfo()); //정보값 //빨강^|^90^|^밥솥
    	    
    	    	//옵션번호 : 실시간 재고 체크시 사용 (제휴사코드 + 단품번호)
	    	paGmktGoodsdt.setPaOptionCode(paGmktGoodsdt.getPaCode() + paGmktGoodsdt.getGoodsdtCode());
	    	
    	    	itemSelection.addAttribute(soapFactory.createName("Code"), paGmktGoodsdt.getPaOptionCode()); //옵션 판매자 코드
    	    	itemSelection.addAttribute(soapFactory.createName("Price"), "0"); //가격
    	    	itemSelection.addAttribute(soapFactory.createName("Remain"), paGmktGoodsdt.getTransOrderAbleQty()); //재고수량
    	    	itemSelection.addAttribute(soapFactory.createName("OptionImageUrl"), "1"); //옵션이미지URL
	    }

	    //ItemAdditionList 사용안함
	    //ItemTextList 사용안함
		
	    soapMessage.saveChanges();
	    
	    return soapMessage;
	}
	
	*//**
	 * G마켓 REQUEST AddItemReturnFee MESSAGE 설정
	 * @param SOAPMessage
	 * @param 
	 * @return SOAPMessage
	 * @throws Exception
	 *//*
	public SOAPMessage createAddItemReturnFeeBody(SOAPMessage soapMessage, PaGmktGoodsVO paGmktGoods) throws Exception {
	    // SOAP FACTORY
	    SOAPFactory soapFactory = SOAPFactory.newInstance();

	    // SOAP Body
	    SOAPBody requestSoapBody = soapMessage.getSOAPPart().getEnvelope().getBody();

	    QName addItemReturnFeeQName = new QName(ConfigUtil.getString("PAGMKT_COM_BASE_URL"), "AddItemReturnFee");
	    SOAPElement addItemReturnFeeRoot = requestSoapBody.addChildElement(addItemReturnFeeQName);

	    // AddItemReturnFee
	    SOAPElement addItemReturnFee = addItemReturnFeeRoot.addChildElement("AddItemReturnFee");
	    addItemReturnFee.addAttribute(soapFactory.createName("GmktItemNo"), paGmktGoods.getItemNo()); //G마켓 상품번호
	    //SellerBasic : 판매자기본, Item : 상품별
	    addItemReturnFee.addAttribute(soapFactory.createName("ReturnFeeType"), "Item"); //반품배송비 타입 SellerBasic or Item

	    // AddItemReturnFee > ItemReturnFee
	    QName itemReturnFeeQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "ItemReturnFee"); 
	    SOAPElement itemReturnFee = addItemReturnFee.addChildElement(itemReturnFeeQName);
	    //BySeller : 판매자부담-무료 or ByBuyer : 구매자부담-유료
	    if(paGmktGoods.getReturnCost() > 0){
		itemReturnFee.addAttribute(soapFactory.createName("ReturnChargeType"), "ByBuyer"); //반품배송비과금유형 //BySeller or ByBuyer
	    }else {
		itemReturnFee.addAttribute(soapFactory.createName("ReturnChargeType"), "BySeller"); //반품배송비과금유형 //BySeller or ByBuyer
	    }
	    itemReturnFee.addAttribute(soapFactory.createName("ReturnShippingFee"), String.valueOf(paGmktGoods.getReturnCost())); //반품배송비(편도) //decimal
	    itemReturnFee.addAttribute(soapFactory.createName("ExchangeShippingFee"), String.valueOf(paGmktGoods.getChangeCost())); //교환배송비(편도) //decimal
		
	    soapMessage.saveChanges();
	    
	    return soapMessage;
	}
	
	*//**
	 * G마켓 REQUEST EditItemImage MESSAGE 설정
	 * @param SOAPMessage
	 * @param 
	 * @return SOAPMessage
	 * @throws Exception
	 *//*
	public SOAPMessage createEditItemImageBody(SOAPMessage soapMessage, PaGmktGoodsVO paGmktGoods, Config imageUrl) throws Exception {
	    // SOAP FACTORY
	    SOAPFactory soapFactory = SOAPFactory.newInstance();

	    // SOAP Body
	    SOAPBody requestSoapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
	    QName editItemImageQName = new QName(ConfigUtil.getString("PAGMKT_COM_BASE_URL"), "EditItemImage");
	    SOAPElement editItemImageRoot = requestSoapBody.addChildElement(editItemImageQName);

	    // EditItemImage
	    SOAPElement editItemImage = editItemImageRoot.addChildElement("EditItemImage");
	    editItemImage.addAttribute(soapFactory.createName("GmktItemNo"), paGmktGoods.getItemNo()); //G마켓 상품번호

	    // EditItemImage > ItemImage
	    QName itemImageQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "ItemImage");
	    SOAPElement itemImage = editItemImage.addChildElement(itemImageQName);
	    //상품 기본이미지 URL //필수
	    itemImage.addAttribute(soapFactory.createName("DefaultImage"), imageUrl.getVal()+paGmktGoods.getImageUrl()+paGmktGoods.getImageP());
	    
	    if(StringUtils.isNotBlank(paGmktGoods.getImageAP())){
	    	itemImage.addAttribute(soapFactory.createName("AddImage1"), imageUrl.getVal()+paGmktGoods.getImageUrl()+paGmktGoods.getImageAP()); //상품 추가 이미지1
	    }
	    if(StringUtils.isNotBlank(paGmktGoods.getImageBP())){
	        itemImage.addAttribute(soapFactory.createName("AddImage2"), imageUrl.getVal()+paGmktGoods.getImageUrl()+paGmktGoods.getImageBP()); //상품 추가 이미지2
	    }
	    
	    soapMessage.saveChanges();
	    
	    return soapMessage;
	}
	
	*//**
	 * G마켓 REQUEST AddOfficialInfo MESSAGE 설정
	 * @param SOAPMessage
	 * @param 
	 * @return SOAPMessage
	 * @throws Exception
	 *//*
	public SOAPMessage createAddOfficialInfoBody(SOAPMessage soapMessage, List<PaGmktGoodsOfferVO> paGmktPaGoodsOfferList) throws Exception {
	    // SOAP FACTORY
	    SOAPFactory soapFactory = SOAPFactory.newInstance();

	    // SOAP Body
	    SOAPBody requestSoapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
	    QName addOfficialInfoQName = new QName(ConfigUtil.getString("PAGMKT_COM_BASE_URL"), "AddOfficialInfo");
	    SOAPElement addOfficialInfoRoot = requestSoapBody.addChildElement(addOfficialInfoQName);
		
	    // AddOfficialInfo
	    SOAPElement addOfficialInfo = addOfficialInfoRoot.addChildElement("AddOfficialInfo");
	    addOfficialInfo.addAttribute(soapFactory.createName("GmktItemNo"), paGmktPaGoodsOfferList.get(0).getItemNo()); //G마켓 상품번호
	    addOfficialInfo.addAttribute(soapFactory.createName("GroupCode"), paGmktPaGoodsOfferList.get(0).getPaOfferType()); //상품군 구분코드
		
	    // AddOfficialInfo > SubInfoList
	    QName subInfoListQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "SubInfoList");
	    for (PaGmktGoodsOfferVO paGmktPaGoodsOffer : paGmktPaGoodsOfferList){
		SOAPElement subInfoList = addOfficialInfo.addChildElement(subInfoListQName);
		subInfoList.addAttribute(soapFactory.createName("Code"), paGmktPaGoodsOffer.getPaOfferCode()); //항목코드번호
		subInfoList.addAttribute(soapFactory.createName("AddYn"), StringUtils.isNotBlank(paGmktPaGoodsOffer.getPaOfferExt())?"Y":"N"); //추가입력여부
		subInfoList.addAttribute(soapFactory.createName("AddValue"), ComUtil.subStringBytes(paGmktPaGoodsOffer.getPaOfferExt(), 1000)); //추가입력값
	    }

	    soapMessage.saveChanges();
    
	    return soapMessage;
	}
	
	*//**
	 * G마켓 REQUEST AddIntegrateSafeCert MESSAGE 설정
	 * @param SOAPMessage
	 * @param 
	 * @return SOAPMessage
	 * @throws Exception
	 *//*
	public SOAPMessage createAddIntegrateSafeCertBody(SOAPMessage soapMessage, List<PaGmktGoodsCertiVO> paGmktGoodsCertiList) throws Exception {

	    // SOAP FACTORY
	    SOAPFactory soapFactory = SOAPFactory.newInstance();
	    
	    //Child, Electric, Life
	    List<PaGmktGoodsCertiVO> paGmktGoodsCertiForChild = new ArrayList<PaGmktGoodsCertiVO>();
	    List<PaGmktGoodsCertiVO> paGmktGoodsCertiForElectric = new ArrayList<PaGmktGoodsCertiVO>();
	    List<PaGmktGoodsCertiVO> paGmktGoodsCertiForLife = new ArrayList<PaGmktGoodsCertiVO>();
	    
	    for (PaGmktGoodsCertiVO paGmktGoodsCerti : paGmktGoodsCertiList){
		switch(paGmktGoodsCerti.getSafeCertGroupType()){
		    case "Child" : paGmktGoodsCertiForChild.add(paGmktGoodsCerti); break;
		    case "Electric" : paGmktGoodsCertiForElectric.add(paGmktGoodsCerti); break;
		    case "Life" : paGmktGoodsCertiForLife.add(paGmktGoodsCerti); break;
		}
	    }

	    // SOAP Body
	    SOAPBody requestSoapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
	    QName addIntegrateSafeCertQName = new QName(ConfigUtil.getString("PAGMKT_COM_BASE_URL"), "AddIntegrateSafeCert");
	    SOAPElement addIntegrateSafeCertRoot = requestSoapBody.addChildElement(addIntegrateSafeCertQName);
		
	    // AddIntegrateSafeCert
	    SOAPElement addIntegrateSafeCert = addIntegrateSafeCertRoot.addChildElement("AddIntegrateSafeCert");
	    addIntegrateSafeCert.addAttribute(soapFactory.createName("GmktItemNo"), paGmktGoodsCertiList.get(0).getItemNo()); //G마켓 상품번호 //string
		
	    // AddIntegrateSafeCert > SafeCertGroupList
	    //Child
	    if(!paGmktGoodsCertiForChild.isEmpty()){
		crateSafeCertGroupList(addIntegrateSafeCert, paGmktGoodsCertiForChild);
	    }
	    //Electric
	    if(!paGmktGoodsCertiForElectric.isEmpty()){
		crateSafeCertGroupList(addIntegrateSafeCert, paGmktGoodsCertiForElectric);
	    }
	    //Life
	    if(!paGmktGoodsCertiForLife.isEmpty()){
		crateSafeCertGroupList(addIntegrateSafeCert, paGmktGoodsCertiForLife);
	    }

	    soapMessage.saveChanges();

	    return soapMessage;
	}
	
	private void crateSafeCertGroupList(SOAPElement addIntegrateSafeCert, List<PaGmktGoodsCertiVO> paGmktGoodsCertiList) throws Exception {
	    // SOAP FACTORY
	    SOAPFactory soapFactory = SOAPFactory.newInstance();
	    SOAPElement safeCertGroupList = null;
	    
	    QName safeCertGroupListQName = new QName(ConfigUtil.getString("PAGMKT_COM_XML_SCHEMA_URL"), "SafeCertGroupList");
	    safeCertGroupList = addIntegrateSafeCert.addChildElement(safeCertGroupListQName);
	    //None or Child or Electric or Life
	    safeCertGroupList.addAttribute(soapFactory.createName("SafeCertGroupType"), paGmktGoodsCertiList.get(0).getSafeCertGroupType()); //그룹타입
	    //RequireCert or NotCert or AddDescription or RequireCertWithAPI
	    safeCertGroupList.addAttribute(soapFactory.createName("CertificationType"), paGmktGoodsCertiList.get(0).getCertificationType()); //인증타입

	    for (PaGmktGoodsCertiVO paGmktGoodsCerti : paGmktGoodsCertiList){
		if(Constants.PA_GMKT_CERTIFICATION_TYPE_REQUIRE_CERT.equals(paGmktGoodsCerti.getCertificationType())
			|| Constants.PA_GMKT_CERTIFICATION_TYPE_REQUIRE_CERT_WITH_API.equals(paGmktGoodsCerti.getCertificationType())
			){
		    // AddIntegrateSafeCert > SafeCertGroupList > safeCertInfoList
		    SOAPElement safeCertInfoList = safeCertGroupList.addChildElement("SafeCertInfoList");
		    safeCertInfoList.addAttribute(soapFactory.createName("CertificationNo"), paGmktGoodsCerti.getCertificationNo()); //인증번호 //string
		    //SafeCert or SafeCheck or SupplierCheck
		    safeCertInfoList.addAttribute(soapFactory.createName("CertificationTargetCode"), paGmktGoodsCerti.getCertificationTargetCode()); //안전인증대상

		    if(Constants.PA_GMKT_CERTIFICATION_TYPE_REQUIRE_CERT.equals(paGmktGoodsCerti.getCertificationType())){
			safeCertInfoList.addAttribute(soapFactory.createName("CertificationStatus"), "적합"); //인증상태 //string
		    }else {
			safeCertInfoList.addAttribute(soapFactory.createName("CertificationStatus"), paGmktGoodsCerti.getCertificationStatus()); //인증상태 //string
		    }
		    if(Constants.PA_GMKT_CERTIFICATION_TYPE_REQUIRE_CERT_WITH_API.equals(paGmktGoodsCerti.getCertificationType())){
			safeCertInfoList.addAttribute(soapFactory.createName("CertificationDate"), ""); //인증일 //string
		    }else {
			safeCertInfoList.addAttribute(soapFactory.createName("CertificationDate"), paGmktGoodsCerti.getCertificationDate()); //인증일 //string
		    }
		    safeCertInfoList.addAttribute(soapFactory.createName("ModelName"), paGmktGoodsCerti.getModelName()); //모델명 //string
		}
	    }
	    
	}
	
}
*/