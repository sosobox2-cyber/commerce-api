package com.cware.netshopping.pagmkt.util.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.util.ComUtil;

public class PaGmktGoodsRest extends PaGmktAbstractRest {

	@Override
	public String getRequstBody(ParamMap param) throws Exception{
		String apiCode = param.getString("apiCode");
		Map<String,Object> map = new HashMap<>();
		switch(apiCode){
		case "IF_PAGMKTAPI_V2_01_001" ://상품 등록
		case "IF_PAGMKTAPI_V2_01_003" ://상품 일괄 수정
		case "IF_PAGMKTAPI_V2_01_003_S" ://상품 일괄 수정(500에러용)				
		case "IF_PAGMKTAPI_V2_01_003_A" ://상품 일괄 수정(옥션 기입점후 지마켓 입점시킬 시)
			map = createGoodsInsertBody(param);
			break;
		case "IF_PAGMKTAPI_V2_01_013" ://구버전옵션등록
		case "IF_PAGMKTAPI_V2_01_014_O" ://구버전옵션수정
		case "IF_PAGMKTAPI_V2_01_014_B" ://구버전옵션수정
			map = createGoodsOptionBody(param);
			break;
		case "IF_PAGMKTAPI_V2_01_016" ://구버전옵션삭제
			//request불필요
			break;
		case "IF_PAGMKTAPI_V2_01_004" ://가격/재고/판매상태 변경
		case "IF_PAGMKTAPI_V2_01_038" ://리텐션 상품 연장
			map = createGoodsPriceSaleYnBody(param);
			break;
		case "IF_PAGMKTAPI_V2_01_020" ://상품명 변경
			map = createGoodsNameUpdateBody(param);
			break;
		case "IF_PAGMKTAPI_V2_01_006" ://상품 판매자 부담할인 수정
			//map = createSellerDiscountUpdateBody(param);
			break;
		case "IF_PAGMKTAPI_V2_01_009" ://상품 이미지 수정
			//map = createGoodsImageUpdateBody(param);
			break;
		case "IF_PAGMKTAPI_V2_01_037" ://G마켓 팔자주문 처리
			map = createGoodsSellingOrderBody(param);
			break;
		case "IF_PAGMKTAPI_V2_01_021" ://상품명 변경
			map = createG9DisplayBody(param);
			break;
		case "IF_PAGMKTAPI_V2_01_039" ://이베이 상품 유효기간 갱신
			map = createGoodsExpiryDate(param);
			break;
		default :	
			break;
		
		}
		return mapToJson(map);
	}
	
	
	//4.1 상품등록
	//[POST]https://sa.esmplus.com/item/v1/goods
	private Map<String,Object> createGoodsInsertBody(ParamMap requestMap){
		Map<String, Object> params = new HashMap<>();
		ParamMap paramMap = requestMap;
		HashMap<String, Object> requestGoodsMap = (HashMap<String, Object>) requestMap.get("requestGoodsMap");
		List<HashMap<String, Object>> requestOfferMapList = (List<HashMap<String, Object>>) requestMap.get("requestOfferMapList");
		HashMap<String, Object> requestDescribeMap = (HashMap<String, Object>) requestMap.get("requestDescribeMap");
		HashMap<String, Object> requestGmktPolicyMap = (HashMap<String, Object>) requestMap.get("requestGmktPolicyMap");
		HashMap<String, Object> requestIacPolicyMap = (HashMap<String, Object>) requestMap.get("requestIacPolicyMap");
		
		//List<HashMap<String, Object>> requestPriceMapList = (List<HashMap<String, Object>>) requestMap.get("requestPriceMapList");
		List<HashMap<String, Object>> requestGmktPriceMapList = (List<HashMap<String, Object>>) paramMap.get("requestGmktPriceMapList");
		List<HashMap<String, Object>> requestIacPriceMapList = (List<HashMap<String, Object>>) paramMap.get("requestIacPriceMapList");
		
		//이베이 상품명 제한 문자 조회
		List<HashMap<String, Object>> paGoodsLimitCharMapList = (List<HashMap<String, Object>>) requestMap.get("paGoodsLimitCharMapList");
		
		if(requestMap.getString("isModify").equals("Y")) {
			//상품 수정일 경우 추가될 JSON
			Map<String, Object> isSell = new HashMap<>();
			boolean saleFlag = true;
			boolean saleFlagExtra = true;
			
			if(requestGoodsMap.get("PA_SALE_GB").toString().equals("30")){
				saleFlag=false;
			}
			switch(paramMap.getString("siteGb")){
				case "PAE":
					if(requestGoodsMap.get("PA_SALE_GB_EXTRA").toString().equals("30")){
						saleFlagExtra=false;
					}
					isSell.put("gmkt", saleFlag);//Y, G마켓판매여부(SalePrice/StockQty)
					isSell.put("iac", saleFlagExtra);//Y, 옥션판매여부(신규)
					break;
				case "PAG":
					isSell.put("gmkt", saleFlag);//Y, G마켓판매여부(SalePrice/StockQty)
					isSell.put("iac", false);//Y, 옥션판매여부(신규)
					break;
				case "PAA":
					isSell.put("gmkt", false);//Y, G마켓판매여부(SalePrice/StockQty)
					isSell.put("iac", saleFlag);//Y, 옥션판매여부(신규)
					break;
			}
			params.put("isSell", isSell);
		}
		/** itemBasicInfo */
		Map<String, Object> itemBasicInfo = new HashMap<>();
		//itemBasicInfo > goodsName
		Map<String, Object> goodsName = new HashMap<>();

		String itemName = requestGoodsMap.get("GOODS_NAME").toString();
		
		//상품, 브랜드 이름 특수문자 치환
		if(paGoodsLimitCharMapList != null){
			for(HashMap<String, Object> paGoodsLimitCharMap : paGoodsLimitCharMapList){
				String replaceChar = paGoodsLimitCharMap.get("REPLACE_CHAR").toString() == null ? "" : paGoodsLimitCharMap.get("REPLACE_CHAR").toString();
				itemName = itemName.replaceAll(paGoodsLimitCharMap.get("LIMIT_CHAR").toString(), replaceChar);
			}
		}
		itemName = itemName.trim();
		itemName = itemName.replaceAll("   ", " ");
		itemName = itemName.replaceAll("  ",  " ");
		itemName = itemName.replaceAll(" ",   " ");
		
		String collectYn = ComUtil.NVL(requestMap.get("COLLECT_YN").toString(), "0");
		if("1".equals(collectYn)) {
			itemName = "(착불)" + itemName; 
		}
		if("23755294".equals(requestMap.get("goodsCode").toString()) || "23393459".equals(requestMap.get("goodsCode").toString()) ) {
			goodsName.put("kor", ComUtil.subStringBytes(itemName, 50));
		} else {
			goodsName.put("kor", ComUtil.subStringBytes(itemName, 100));//Y, 상품명, 기본상품명 + 프로모션상품명 합쳐서 100byte(ItemName)
		}
		//goodsName.put("promotion", "string");//N, 프로모션 상품명, 기본상품명 + 프로모션상품명 합쳐서 50byte(신규)
		//goodsName.put("eng", "string");//N, 영문 상품명, 100byte(ItemNameEng)
		//goodsName.put("chi", "string");//N, 중문 상품명, 100byte(ItemNameCH)
		//goodsName.put("jpn", "string");//N, 일문 상품명, 100byte(신규)
		
		itemBasicInfo.put("goodsName", goodsName);
		
		//itemBasicInfo > category
		Map<String, Object> category = new HashMap<>();
		List<Object> site = new ArrayList<>();
		List<Object> shop = new ArrayList<>();
		Map<String, Object> esm = new HashMap<>();
		Map<String, Object> siteMap;
		Map<String, Object> shopMap = new HashMap<>();
		
		switch(paramMap.getString("siteGb")){
		//TODO PAE일때 site category code를 어디서 가져올지 !!!!
			case "PAE":
				//옥션 테스트 고정 카테고리코드 requestGoodsMap.get("SITE_CATEGORY_CODE") 300004384
				siteMap = new HashMap<>();
				siteMap.put("siteType", 1);//N, 등록카테고리Site타입, G마켓/옥션 Site 카테고리 등록할 Site 선택 1.옥션 2.G마켓(신규)
				siteMap.put("catCode", requestGoodsMap.get("SITE_CATEGORY_CODE_EXTRA"));//N, Site카테고리코드, G마켓/옥션에서 제공하는 최하위(Leaf)카테고리 코드 등록	*G마켓,옥션 Site 카테고리 조회 API  or Excel 문서를 통해 코드 확인 가능(CategoryCode)
				site.add(siteMap);
				
				//지마켓 테스트 고정 카테고리코드 requestGoodsMap.get("SITE_CATEGORY_CODE") 300004384
				siteMap = new HashMap<>();
				siteMap.put("siteType", 2);//N, 등록카테고리Site타입, G마켓/옥션 Site 카테고리 등록할 Site 선택 1.옥션 2.G마켓(신규)
				siteMap.put("catCode", requestGoodsMap.get("SITE_CATEGORY_CODE"));//N, Site카테고리코드, G마켓/옥션에서 제공하는 최하위(Leaf)카테고리 코드 등록	*G마켓,옥션 Site 카테고리 조회 API  or Excel 문서를 통해 코드 확인 가능(CategoryCode)
				site.add(siteMap);
				break;
			case "PAG":
				//지마켓 테스트 고정 카테고리코드 requestGoodsMap.get("SITE_CATEGORY_CODE") 300004384
				siteMap = new HashMap<>();
				siteMap.put("siteType", 2);//N, 등록카테고리Site타입, G마켓/옥션 Site 카테고리 등록할 Site 선택 1.옥션 2.G마켓(신규)
				siteMap.put("catCode", requestGoodsMap.get("SITE_CATEGORY_CODE"));//N, Site카테고리코드, G마켓/옥션에서 제공하는 최하위(Leaf)카테고리 코드 등록	*G마켓,옥션 Site 카테고리 조회 API  or Excel 문서를 통해 코드 확인 가능(CategoryCode)
				site.add(siteMap);
				break;
			case "PAA":
				//옥션 테스트 고정 카테고리코드 requestGoodsMap.get("SITE_CATEGORY_CODE") 300004384
				siteMap = new HashMap<>();
				siteMap.put("siteType", 1);//N, 등록카테고리Site타입, G마켓/옥션 Site 카테고리 등록할 Site 선택 1.옥션 2.G마켓(신규)
				siteMap.put("catCode", requestGoodsMap.get("SITE_CATEGORY_CODE"));//N, Site카테고리코드, G마켓/옥션에서 제공하는 최하위(Leaf)카테고리 코드 등록	*G마켓,옥션 Site 카테고리 조회 API  or Excel 문서를 통해 코드 확인 가능(CategoryCode)
				site.add(siteMap);
				break;
		}
		//shopMap.put("siteType", 1);//N, 등록카테고리Site타입, G마켓/옥션 미니샵 카테고리 등록할 Site 선택1.옥션 2.G마켓(신규)
		//shopMap.put("largeCatCode", "string");//N, 미니샵대카테고리코드, 판매자 미니샵 내, 카테고리 매칭 원할 경우 미니샵 카테고리코드 입력 *미니샵 카테고리 코드 조회 API 코드 확인 가능(신규)
		//shopMap.put("middleCatCode", "string");//N,미니샵중카테고리코드(신규)
		//shopMap.put("smallCatCode", "string");//N,미니샵소카테고리코드(신규)
		shop.add(shopMap);
		
		//requestGoodsMap.get("ESM_CATEGORY_CODE") 00370002000300020000
		esm.put("catCode", requestGoodsMap.get("ESM_CATEGORY_CODE"));//Y, ESM카테고리코드, G마켓/옥션 공통 카테고리코드 등록      G마켓/옥션 Site 카테고리의 상위 카테고리 개념임      *ESM 카테고리 코드 조회 API 코드 확인 가능(신규)
		
		category.put("site", site);
		category.put("shop", null);
		category.put("esm", esm);
		itemBasicInfo.put("category", category);
		
		//itemBasicInfo > book
		Map<String, Object> book = new HashMap<>();
		
		book.put("isUseIsbnCode", false);//Y, ISBN코드 여부, 도서상품일 경우 ISBN 코드 입력 필수(신규)
		//book.put("isbnCode", "string");//N, ISBN코드 값, 도서상품일 경우, ISBN 코드 입력 필수(기존에 사용X)(Book)
		//book.put("price", 0);//N, 도서참고가격, 도서상품일 경우 입력(기존에 사용X)(ReferencePrice > Price)
		//book.put("attributeCode", "string");//N, 분류속성코드, "도서상품일 경우 입력 *G마켓,옥션 Site 카테고리 조회 API  or Excel 문서를 통해 코드 확인 가능"(AttributeCode)
		
		itemBasicInfo.put("book", null);
		
		//itemBasicInfo > catalog
		Map<String, Object> catalog = new HashMap<>();
		//List<Object> epinCode = new ArrayList<>();
		
		//epinCode.add(0);
		
		//catalog.put("modelName", "string");//N, 모델명, modelName(ModelName)
		String brandNo = "";
		if(requestGoodsMap.get("BRAND_NO")==null || requestGoodsMap.get("BRAND_NO")==""){
			brandNo = "0";
		}else{
			brandNo = requestGoodsMap.get("BRAND_NO").toString();
		}
		catalog.put("brandNo", brandNo);//Y, 브랜드코드, "브랜드 상품일 경우 ESM브랜드코드 필수 입력 *GetBrands API(3.13) or GetMakers  API에서 매칭되는 코드 조회하여 입력"(신규)
		//catalog.put("barCode", "string");//N, 바코드, 단일상품-바코드(신규)
		catalog.put("epinCode", null);//Y, ESM상품분류코드, Epin 코드 리스트(신규)
		
		itemBasicInfo.put("catalog", catalog);
		/** itemBasicInfo 끝*/
		params.put("itemBasicInfo", itemBasicInfo);
		/** itemAddtionalInfo */
		Map<String, Object> itemAddtionalInfo = new HashMap<>();
		//itemAddtionalInfo > buyableQuantity
		Map<String, Object> buyableQuantity = new HashMap<>();
		
		if(requestGoodsMap.get("ORDER_MAX_QTY").toString().equals("0")){
			buyableQuantity.put("type", 0);
			buyableQuantity.put("unitDate", null);
			buyableQuantity.put("qty", null);
		}else if(requestGoodsMap.get("CUST_ORD_QTY_CHECK_TERM").toString().equals("0")){
			buyableQuantity.put("type", 2);
			buyableQuantity.put("unitDate", null);//N, 기간, 구매수량제한 Type이 Day인 경우 입력 필수 입력(OrderLimit > OrderLimitPeriod)
			int orderMaxQty = Integer.parseInt(requestGoodsMap.get("ORDER_MAX_QTY").toString());
			if(orderMaxQty > 999){
				orderMaxQty = 999;
			}
			buyableQuantity.put("qty", orderMaxQty);//N, 최대구매수량, 구매수량 Type이 MAX, DAY, OneTime인 경우 필수 입력(OrderLimit > OrderLimitCount)
		}else{
			buyableQuantity.put("type", 3);//Y, 구매수량제한Type, 0. nolimit : 구매수량 제한 없음, 1. OneTime : 1회당 최대 구매수량   2.max : ID당 최대 구매수량, 	3.Day : 기간당 최대 구매수량(신규)
			buyableQuantity.put("unitDate", requestGoodsMap.get("CUST_ORD_QTY_CHECK_TERM"));//N, 기간, 구매수량제한 Type이 Day인 경우 입력 필수 입력(OrderLimit > OrderLimitPeriod)
			int orderMaxQty = Integer.parseInt(requestGoodsMap.get("ORDER_MAX_QTY").toString());
			if(orderMaxQty > 999){
				orderMaxQty = 999;
			}
			buyableQuantity.put("qty", orderMaxQty);//N, 최대구매수량, 구매수량 Type이 MAX, DAY, OneTime인 경우 필수 입력(OrderLimit > OrderLimitCount)
		}
		
		itemAddtionalInfo.put("buyableQuantity", buyableQuantity);
		
		//itemAddtionalInfo > price
		Map<String, Object> price = new HashMap<>();
		
		/** ars할인금액 판매가에 녹이는 처리 2019.02.01 thjeon */
		/*double salePrice = Double.parseDouble(requestGoodsMap.get("SALE_PRICE").toString());
		double dcAmt = Double.parseDouble(requestGoodsMap.get("DC_AMT").toString());
		double promoPrice = Double.parseDouble(requestPriceMapList.get(0).get("DO_COST").toString()); //SKstoa, required setting immediately-promotion 개발중... 2/7...2/21 수정
		double realPrice = salePrice-dcAmt-promoPrice;*/
		
		price.put("gmkt", requestGoodsMap.get("SALE_PRICE").toString());//Y, G마켓 판매가격, "G마켓 상품 등록 시, 필수 입력	G마켓에서 판매될 금액 입력"(SellPrice)
		price.put("iac", requestGoodsMap.get("SALE_PRICE").toString());//Y, 옥션 판매가격, "옥션 상품 등록 시, 필수 입력	옥션에서 판매될 금액 입력"(Price)
		
		itemAddtionalInfo.put("price", price);
		
		//itemAddtionalInfo > stock
		Map<String, Object> stock = new HashMap<>();
		
		
		int orderAbleQty = Integer.parseInt(requestGoodsMap.get("TRANS_ORDER_ABLE_QTY").toString());
		if(orderAbleQty>4999){
			orderAbleQty=4999;
		}
		stock.put("gmkt", orderAbleQty);//Y, G마켓 판매수량, "G마켓 상품 등록 시, 필수 입력	G마켓 해당 상품에 등록할 재고 입력"(StockQty)
		stock.put("iac", orderAbleQty);//Y, 옥션 판매수량, "옥션 상품 등록 시, 필수 입력	옥션 해당 상품에 등록할 재고 입력"(OrderStock >StockQty)
		
		itemAddtionalInfo.put("stock", stock);
		
		//itemAddtionalInfo > recommendedOpts
		Map<String, Object> recommendedOpts = new HashMap<>();
		Map<String, Object> independent = new HashMap<>();
		Map<String, Object> combination = new HashMap<>();
		List<Object> details = new ArrayList<>();
		List<Object> details2 = new ArrayList<>();
		Map<String, Object> detailsMap = new HashMap<>();
		Map<String, Object> detailsMap2 = new HashMap<>();
		
		//detailsMap.put("recommendedOptValueNo", 0);//N, 선택형 추천옵션항목코드, 추천옵션그룹별 선택 항목 조회  API에서 매칭된 코드 입력(신규)
		//detailsMap.put("isSoldOut", true);//N, 선택형 품절여부, true:품절, false:판매(신규)
		//detailsMap.put("isDisplay", true);//N, 선택형 노출여부, true:노출, false:미노출(신규)
		//detailsMap.put("qty", 0);//N, 선택형 재고수량, "미입력시 99999로 입력 되며 상품의 판매 수량을 적용 하고, 	입력시에는 위에 G판매수량/A판매수량에 입력한 판매 수량은 무시함(ESM로직과 동일)"(신규)
		//detailsMap.put("manageCode", "string");//N, 선택형 판매자옵션관리코드, 추천옵션의 판매자 관리 코드(신규)
		details.add(detailsMap);
		
		//independent.put("recommendedOptNo", 0);//N, 선택형 추천옵션그룹코드, Site카테고리별 추천옵션그룹 조회  API에서 매칭된 코드 입력(신규)
		independent.put("details", details);
		
		//detailsMap2.put("recommendedOptValueNo1", 0);//N, 조합형 추천옵션항목코드1, "조합형1번옵션항목값	추천옵션그룹별 선택 항목 조회 API에서 매칭된 코드 입력"(신규)
		//detailsMap2.put("recommendedOptValueNo2", 0);//N, 조합형 추천옵션항목코드2, "조합형2번옵션항목값	추천옵션그룹별 선택 항목 조회 API에서 매칭된 코드 입력"(신규)
		//detailsMap2.put("isSoldOut", true);//N, 조합형 품절여부, "추천옵션 품절 여부 입력	true:품절, false:판매"(신규)
		//detailsMap2.put("isDisplay", true);//N, 조합형 노출여부, "추천옵션 노출 여부 입력	true:노출, false:미노출"(신규)
		//detailsMap2.put("qty", 0);//N, 조합형 재고수량, "미입력시 99999로 입력 되며 상품의 판매 수량을 적용 하고, 입력시에는 위에 G판매수량/A판매수량에 입력한 판매 수량은 무시함(ESM로직과 동일)"(신규)
		//detailsMap2.put("manageCode", "string");//N, 조합형 판매자옵션관리코드, 추천옵션의 판매자 관리 코드(신규)
		details2.add(detailsMap2);
		
		//combination.put("recommendedOptNo1", 0);//N, 조합형 추천옵션그룹코드1, "조합형1번옵션명    Site카테고리별 추천옵션그룹 조회 API에서 매칭된 코드 입력"(신규)
		//combination.put("recommendedOptNo2", 0);//N, 조합형 추천옵션그룹코드2, "조합형2번옵션명    Site카테고리별 추천옵션그룹 조회 API에서 매칭된 코드 입력"(신규)
		combination.put("details", details2);
		
		recommendedOpts.put("type", 0);//Y, 추천옵션타입, "ESM 추천옵션용 기능으로 사용여부 체크   0:옵션미사용   1: 선택형, 2: 2개조합선택형   주문옵션은 등록 대상 G/A 카테고리가 모두 허용 될 경우만 가능"(신규)
		recommendedOpts.put("independent", independent);
		recommendedOpts.put("combination", combination);
		itemAddtionalInfo.put("recommendedOpts", recommendedOpts);
		
		//itemAddtionalInfo > sellingPeriod
		Map<String, Object> sellingPeriod = new HashMap<>();
		
		sellingPeriod.put("gmkt", 90);//Y, G마켓 판매 기간, "G마켓 판매기간 입력	Value : 15,30,60,90"(DisplayDate)
		sellingPeriod.put("iac", 90);//Y, 옥션 판매 기간, "옥션 판매기간 입력		Value : 15,30,60,90"(Period > ApplyPeriod)
		
		itemAddtionalInfo.put("sellingPeriod", sellingPeriod);
		
		//itemAddtionalInfo > managedCode
		itemAddtionalInfo.put("managedCode", requestGoodsMap.get("GOODS_CODE"));//Y, 판매자 상품코드, 해당 상품의 판매자가 관리할 고유 코드 입력 or 자사몰 상품번호 입력(OutItemNo)
		
		//itemAddtionalInfo > inventoryCode
		//itemAddtionalInfo.put("inventoryCode", "string");//N, G마켓 판매자 관리코드, "(G마켓 상품용) 상품 정보가 변경된 시점(상품정보 , 가격정보)에 등록된 code를 주문정보에 포함하여 전달함"(AddItem)
		
		/* sellerShop 사용하지않으나 필수값으로 변경됨에 따른 처리 2019.02.01 thjeon */
		//itemAddtionalInfo > sellerShop
		
		Map<String, Object> sellerShop = new HashMap<>();
		
		sellerShop.put("catNo", requestGoodsMap.get("LMSD_CODE"));//Y, 판매자 카테고리코드, 판매자 자사몰 카테고리 대/중/소/상세 코드 입력(신규)
		sellerShop.put("catName", requestGoodsMap.get("LMSD_NAME"));//Y, 판매자 카테고리명, 판매자 자사몰 카테고리 대/중/소/상세 합쳐서 전체 텍스트명 입력(신규)
		sellerShop.put("brandCode", requestGoodsMap.get("BRAND_CODE"));//Y, 판매자 브랜드코드, 판매자 자사몰 브랜드코드(신규)
		sellerShop.put("brandName", requestGoodsMap.get("BRAND_NAME"));//Y, 판매자 브랜드명, 판매자 자사몰 브랜드명(신규)
		
		itemAddtionalInfo.put("sellerShop", sellerShop);
		
		//itemAddtionalInfo > expiryDate
		//itemAddtionalInfo.put("expiryDate", "2018-03-21T00:13:47.921Z");//N, 유효일, 카테고리에 따라 필수 입력, ExpirationDate
		
		//itemAddtionalInfo > manufacturedDate
		//itemAddtionalInfo.put("manufacturedDate", "2018-03-21T00:13:47.921Z");//N, 제조일, "카테고리에 따라 필수 입력	도서상품일 경우, 도서출판년월일을 제조일에 입력"(신규)
		
		//itemAddtionalInfo > origin
		Map<String, Object> origin = new HashMap<>();
		
		
		//20일 경우 국내, 30일 경우 해외 requestGoodsMap.get("ORIGIN_ENUM").toString()
		String originType="";
		String originDt ="";
		String goodsType ="";
		
		/**
		 * 원산지 모두 상세설명참조로 보냄 2018.12.12 SK스토아 회의결과 반영 by thjeon 
		 */
		goodsType = "1";
		originType = "0";
		/*if(requestGoodsMap.get("ORIGIN_ENUM").toString().equals("20")){
			goodsType = "2";//goodsType 2(가공품)로 넘기라고함 2018.11.21 ebay허수정M 
			originType = "1";
		}else if(requestGoodsMap.get("ORIGIN_ENUM").toString().equals("30")){
			goodsType = "2";
			originType = "2";
			originDt = requestGoodsMap.get("ORIGIN_DT").toString();
		}else{
			goodsType = "1";
			originType = "0";
		}*/
		
		origin.put("goodsType", goodsType);//Y, 원산지상품Type, "해당 상품 유형 코드 입력  0:원산지표시대상아님(식품이외),  1:상세설명참조,	2:가공품, 3:농산물,	4:수산물"(G신규/A유지)
		origin.put("type", originType);//Y, 원산지 Type, "원산지 지역 코드 입력 0:없음, 1:국내, 2:해외, 3:연근해, 4:원양산, 5:기타"(Origin > Code)
		//원산지상세 requestGoodsMap.get("ORIGIN_DT").toString()
		origin.put("code", originDt);//Y, 원산지 코드, "원산지 상세 지역 코드 입력 미입력시 상품별원산지는 상세설명 참조 값으로 등록"(Origin > Place)
		//origin.put("isMultipleOrigin", true);//N, 복수원산지여부, "단순 복수원산지 여부 체크 기능/추가 입력 정보 받지 않음  true:복수원산지,false:단일원산지"(신규)
		
		itemAddtionalInfo.put("origin", origin);
		
		//itemAddtionalInfo > capacity
		//Map<String, Object> capacity = new HashMap<>();
		
		//capacity.put("vol", "string");//N, 용량/규격 값, 옥션 상품용 정보(G신규/A유지)
		//capacity.put("unit", 0);//N, 용량/규격 단위, "옥션 상품용 정보(m,p,cm,box,mg,g.kg...등)"(G신규/A유지)
		
		itemAddtionalInfo.put("capacity", null);
		
		//itemAddtionalInfo > shipping
		Map<String, Object> shipping = new HashMap<>();
		Map<String, Object> policy = new HashMap<>();
		Map<String, Object> returnAndExchange = new HashMap<>();
		Map<String, Object> dispatchPolicyNo = new HashMap<>();
		Map<String, Object> generalPost = new HashMap<>();
		Map<String, Object> visitAndTake = new HashMap<>();
		Map<String, Object> quickService = new HashMap<>();
		Map<String, Object> bundle = new HashMap<>();
		Map<String, Object> each = new HashMap<>();
		
		bundle.put("deliveryTmplId", requestGoodsMap.get("BUNDLE_NO"));//Y, 묶음배송비정책번호, 묶음배송비 정책 사용 시, 정책번호 입력(BundleNo)
		each.put("feeType", 0);//Y, 상품별배송비종류, "DeliveryFeeType > 2.상품별 배송비 선택했을 경우 입력  해당 기능 추후 제공할 예정으로, 현재 ""0""번으로 입력  0:묶음배송비사용,1:무료, 2:유료, 3:조건부무료, 4:수량별차등"(Shipping > NewItemShipping >NewItemShipping > FeeCondition)
		//each.put("feePayType", 0);//N, 상품별배송비지불방법, 착불/선결제 여부(Shipping > NewItemShipping >NewItemShipping > FeeCondition)
		//each.put("fee", 0);//Y, 상품별배송비금액, 무료인 경우 0으로 입력(Shipping > NewItemShipping >NewItemShipping > Fee)
		//each.put("baseFee", 0);//N, 상품별배송비조건부기준금액(Shipping > NewItemShipping >NewItemShipping > FeeBasePrice)
		
		policy.put("placeNo", requestGoodsMap.get("GMKT_SHIP_NO"));//Y, 출하지번호, "해당 상품의 출하지 코드 입력  *출하지 조회 API 코드 입력"(Shipping > RefundAddrNum)
		policy.put("feeType", 1);//Y, 배송비 타입, "묶음배송비 설정 정책 사용할 것인지 상품별로 배송정책 세팅한 상품인지 여부	*현재 1번 묶음배송비만 가능함/2.상품별배송비 기능은 추후 제공	  1:묶음배송비, 2:상품별배송비"(G신규/A유지)
		policy.put("bundle", bundle);
		policy.put("each", null);
		
		returnAndExchange.put("addrNo", requestGoodsMap.get("RETURN_MAN_SEQ"));//N, 판매자주소번호(반품주소), "반품 주소(미입력시 출하지번호 -> 판매자기본 - >사업자)	판매자 주소록 등록 API 등록 번호"(신규)
		/**
    	0001 우체국택배 0002 한진택배 0004 롯데택배 0005 로젠택배 0007 드림택배	
		0008 CJ GLS택배 0010 GTX택배 0011 이노지스택배 0012 하나로택배
		0013 일양택배 0014 천일택배 0015 대신택배 0016 경동택배	
		0017 합동택배 0018 위니아물류 0024 편의점택배
		 */
		String shippingCompany="";//반품 교환 택배사
		int companyNo;//주문배송 택배사
		String delyGb ="";
		if(requestGoodsMap.get("DELY_GB")==null || requestGoodsMap.get("DELY_GB")==""){
			companyNo = 10001;
			shippingCompany ="0008";
		}else{
			delyGb = requestGoodsMap.get("DELY_GB").toString();
			switch(delyGb){
				case "10"://CJ대한통운
					companyNo = 10001;
					shippingCompany="0008";
					break;
				case "11"://한진택배
					companyNo = 10007;
					shippingCompany="0002";
					break;
				case "12"://롯데택배(구,현대택배)
					companyNo = 10008;
					shippingCompany="0004";
					break;
				case "13"://우체국택배
					companyNo = 10005;
					shippingCompany="0001";
					break;
				case "14"://로젠택배
					companyNo = 10003;
					shippingCompany="0004";
					break;
				case "15"://드림택배(구,KG로지스)
					companyNo = 10004;
					shippingCompany="0007";
					break;
				case "17"://KGB택배
					companyNo = 10010;
					shippingCompany="0008";//보류
					break;
				case "27"://대신택배
					companyNo = 10014;
					shippingCompany="0015";
					break;
				case "28"://경동택배
					companyNo = 10016;
					shippingCompany="0016";
					break;
				case "29"://합동택배
					companyNo = 10074;
					shippingCompany="0017";
					break;
				case "30"://CVS편의점택배
					companyNo = 10073;
					shippingCompany="0024";
					break;
				case "32"://한의사랑택배
					companyNo = 10081;
					shippingCompany="0008";//보류
					break;
				case "33"://GTX로지스
					companyNo = 10019;
					shippingCompany="0010";
					break;
				case "34"://천일택배
					companyNo = 10017;
					shippingCompany="0014";
					break;
				case "35"://건영택배
					companyNo = 10050;
					shippingCompany="0008";//보류
					break;
				case "16"://KG옐로우캡택배 보류
				case "18"://일양로지스 보류
				case "36"://고려택배 보류
				case "40"://자체처리 보류
				case "47"://자체배송 보류
				case "48"://설치상품 보류
				case "99"://기타 보류
				default:
					companyNo = 10001;
					shippingCompany="0008";
			}
		}
		returnAndExchange.put("shippingCompany", shippingCompany);//N, 반품교환택배사코드, 
		
		int returnCost = Integer.parseInt(requestGoodsMap.get("RETURN_COST").toString());
		int ordCost = Integer.parseInt(requestGoodsMap.get("ORD_COST").toString());
		
		/**
		 * if( 일반지역의 주문/반품/교환 배송비가 3000/6000/6000 인 경우 )
		 * 		주문배송비 3000원 , 반품비용 3000원, 교환비용 6000원 으로 전송 
		 *		sk스토아 룰에따라 반품비용 = 6000원(주문+반품)이 부과되도록 구성
		 * 
		 * elsif ( 일반지역의 주문/반품/교환 배송비가 3000/3500/5000 인 경우 )
		 * 		주문배송비 3000원 , 반품비용 500원, 교환비용 1000원(교환비=반품*2) 으로 전송
		 * 		반품시 실결제 3500원 / 교환시 실결제 4000원
		 * 
		 * else
		 * 		반품시 테이블의 원금액을 받고싶다면 ... 
		 * 		returnAndExchange.put("fee", realReturnCost) 에서 realReturnCost 대신 returnCost 사용
		 */
		int realReturnCost = returnCost - ordCost;
		
		if(realReturnCost < 0){
			realReturnCost = 0;
		}
		
		if(requestGoodsMap.get("SHIP_COST_CODE").toString().substring(0, 2).equals("FR") & ordCost == 0) {
			realReturnCost = realReturnCost/2;
		}
		
		returnAndExchange.put("fee", realReturnCost);//N, 반품/교환 편도배송비, "편도배송비를 입력해야하며, 편도 배송비 기준으로 G마켓/옥션 로직(조건부 배송비 깨지는지 여부 등)에 반영되어 고객 부담 배송비 계산됨  왕복배송비를 입력할 경우 과도하게 배송비 부과되어 강성 CS 인입되므로 주의	0 : 무료, 입력금액 : 편도, 미입력 : 기본로직 (원배송비, 2500원)"(ReturnShippingFee)
		switch(paramMap.getString("siteGb")){
			case "PAE":
				dispatchPolicyNo.put("gmkt", requestGmktPolicyMap.get("POLICY_NO"));//Y, G마켓 발송타입정책번호, 발송 타입 정책 등록 API로 등록한 정책 번호 입력(TransPolicyNo)
				dispatchPolicyNo.put("iac", requestIacPolicyMap.get("POLICY_NO"));//Y, G마켓 발송타입정책번호, 발송 타입 정책 등록 API로 등록한 정책 번호 입력(TransPolicyNo)
				break;
			case "PAG":
				dispatchPolicyNo.put("gmkt", requestGmktPolicyMap.get("POLICY_NO"));//Y, G마켓 발송타입정책번호, 발송 타입 정책 등록 API로 등록한 정책 번호 입력(TransPolicyNo)
				break;
			case "PAA":
				dispatchPolicyNo.put("iac", requestIacPolicyMap.get("POLICY_NO"));//Y, G마켓 발송타입정책번호, 발송 타입 정책 등록 API로 등록한 정책 번호 입력(TransPolicyNo)
				break;
		}
		
		//generalPost.put("isUse", true);//Y, 일반우편, "(옥션 상품용)	일반우편 서비스 가능한 상품 여부 입력	true:제공,false:미제공"(ShippingFee > ShippingType)
		//generalPost.put("price", 0);//N, 일반우편요금, "(옥션 상품용)  0원:무료, 	금액입력:선결제유료"(신규)
		visitAndTake.put("isUse", false);//Y, 방문수령제공, "방문수령 서비스 가능한 상품 여부 입력	true:제공,false:미제공"(신규)
		//visitAndTake.put("type", 0);//N, 방문수령혜택, "방문수령 서비스 가능한 상품으로 설정되었을 경우 입력	 1:없음,  2:가격할인,  3:사은품"(신규)
		//visitAndTake.put("discount", 0);//N, 방문수령혜택 가격할인(신규)
		//visitAndTake.put("gifts", "string");//N, 방문수령혜택 사은품(신규)
		//visitAndTake.put("addrNo", "string");//N, 방문수령주소번호, 판매자 주소록 등록(3.5) API 등록 번호(신규)
		quickService.put("isUse", false);//Y, 퀵서비스 제공, "퀵서비스 가능한 상품 여부 입력 true:제공,false:미제공"(G신규/A유지)
		//quickService.put("companyName", "string");//N,퀵서비스업체명(신규)
		//quickService.put("phoneNo", "string");//N,퀵서비스연락처, 숫자만 입력(신규)
		//quickService.put("shippingEnableRegionCode", "string");//N, 퀵서비스제공지역, "Code 복수입력(Code는 별도 Xaml또는 데이터로제공, 복수 지역 제공가능) chkRegion_0100:서울, 0200:경기(전체), 0300:광주, 0400:대구, 0500:대전, 0600:부산, 0700:울산, 0800:인천"(신규)
		
		shipping.put("type", 1);//Y, 배송방법선택, "해당 상품의 배송방식 코드 입력  G마켓은 무조건 1번만 사용가능 / 옥션 3번 선택 시, 일반우편,퀵서비스,방문수령 중 선택 필요  1:택배소포, 2:화물배달, 3:판매자직접배송"(G신규/A유지)
		/**
		10001 대한통운 10003 로젠택배 10004 드림택배 10005 우체국택배 10006 등기우편 10007 한진택배 10008 롯데택배 10009 SC로지스 10010 KGB택배
		10011 하나로택배 10012 이노지스택배 10013 CJ택배 10014 대신택배 10015 일양택배 10016 경동택배 10017 천일택배 10019 GTX로지스 10020 한진정기화물
		10021 OCS KOREA  10022 DHL 10023 FEDEX 10024 일반우편 10025 퀵서비스 10027 LG전자물류 10028 삼성전자물류 
		10032 (G마켓)자체배송- 송장번호 유효성 체크 없으나, 구매자가 배송조회 불가
		10070 (옥션)자체배송-기타배송송장번호 유효성 체크 없으나, 구매자가 배송조회 불가
		10031 (G마켓)직접배송 10033 (옥션)직접배송 10036 EMS 10039 호남택배 10041 USPS 10042 UPS 10043 GSMNTON 10044 WarpEx 10045 성원글로벌
		10050 건영택배  10051 WIZWA 10072 CJ국제특송 10073 편의점택배(GS25) 10074 합동택배 10075 롯데국제특송 10076 위니아물류 10077 SLX
		10078 동부대우전자(대우전자로변경예정)  10079 범한판토스 10080 GPS LOGIX 10081 한의사랑택배 10083 G익스프레스 10084 쉽트랙 10085 ACI
		아래는 해외배송 전용 택배사코드로, G마켓만 지원합니다.		
		10054 (해외)우체국  10055 (해외)DHL 10056 (해외)DPD
		아래는 당일배송관 마트 판매자 전용 택배사코드입니다.
		10086 GSfresh  10087 롯데슈퍼(롯데Fresh) 10048 홈플러스
		 */
		shipping.put("companyNo", companyNo);//Y, "해당 상품의 발송처리할 택배사코드 입력	*택배사 코드 조회 API 코드 입력"(신규)
		shipping.put("policy", policy);
		//shipping.put("freeShippingFeeType", 0);//N, 상품별배송비무료배송특이사항, "(G마켓 상품용) 추가배송비가 발생하는 상품일 경우 아래 유형 선택   0 : 설정없음   1 : 지역별 차등 무료   2 : 설치 배송비	3 : 직접 수령 가능 상품"(FreeDelFeeType)
		shipping.put("returnAndExchange", returnAndExchange);
		shipping.put("dispatchPolicyNo", dispatchPolicyNo);
		shipping.put("generalPost", generalPost);
		shipping.put("visitAndTake", visitAndTake);
		shipping.put("quickService", quickService);
		
		itemAddtionalInfo.put("shipping", shipping);
		
		//itemAddtionalInfo > officialNotice
		Map<String, Object> officialNotice = new HashMap<>();
		List<Object> details3 = new ArrayList<>();
		
		officialNotice.put("officialNoticeNo", requestOfferMapList.get(0).get("PA_OFFER_TYPE"));//Y, 상품정보고시 상품군코드(GroupCode)
		for(Map<String,Object> offerMap: requestOfferMapList){
			Map<String, Object> detailsMap3 = new HashMap<>();
			detailsMap3.put("officialNoticeItemelementCode",offerMap.get("PA_OFFER_CODE"));//Y, 상품정보고시 항목코드[](SubInfoList > Code)(SubInfoList > Code)
			detailsMap3.put("value", ComUtil.NVL(offerMap.get("PA_OFFER_EXT"),"").toString()+" \n"+ComUtil.NVL(offerMap.get("KC_INFO"),"").toString());//Y, 상품정보고시 추가입력여부(SubInfoList > AddYn) + kc인증값
			detailsMap3.put("isExtraMark", false);//N, 상품정보고시 추가입력 값(SubInfoList > AddValue)
			details3.add(detailsMap3);
		}
		
		officialNotice.put("details", details3);
		
		itemAddtionalInfo.put("officialNotice", officialNotice);
		
		//itemAddtionalInfo > isAdultProduct
		if(requestGoodsMap.get("ADULT_YN").toString().equals("1")){
			itemAddtionalInfo.put("isAdultProduct", true);//Y, 성인상품여부, "성인인증을 하기 전까지 상품이미지 비노출 처리됨	true: 성인상품, false : 일반상품"(IsAdult)
		}else{
			itemAddtionalInfo.put("isAdultProduct", false);//Y, 성인상품여부, "성인인증을 하기 전까지 상품이미지 비노출 처리됨	true: 성인상품, false : 일반상품"(IsAdult)
		}
		//itemAddtionalInfo > isYouthNotAvailable
		itemAddtionalInfo.put("isYouthNotAvailable", false);//N, 청소년구매불가여부, "성인인증을 하기 전에도 상품이미지는 노출처리 되나, 구매시점 성인인증 필요   Ex) 주류 등	true: 청소년구매불가상품, false : 일반상품"(신규)
		
		//itemAddtionalInfo > isVatFree
		itemAddtionalInfo.put("isVatFree", false);//Y, 부과세여부, "반드시 면세상품일 경우에만 true로 설정하고, 일반상품은 false로 설정	true:부과세면세상품,false:부과세과세상품"(Tax)
		
		//itemAddtionalInfo > certInfo
		Map<String, Object> certInfo = new HashMap<>();
		Map<String, Object> gmkt = new HashMap<>();
		Map<String, Object> iac = new HashMap<>();
		Map<String, Object> safetyCerts = new HashMap<>();
		List<Object> certId = new ArrayList<>();
		List<Object> licenseSeq = new ArrayList<>();
		Map<String, Object> medicalInstrument = new HashMap<>();
		Map<String, Object> broadcastEquipment = new HashMap<>();
		Map<String, Object> food = new HashMap<>();
		Map<String, Object> healthFood = new HashMap<>();
		Map<String, Object> environmentFriendly = new HashMap<>();
		Map<String, Object> child = new HashMap<>();
		Map<String, Object> electric = new HashMap<>();
		Map<String, Object> life = new HashMap<>();
		List<Object> details4 = new ArrayList<>();
		List<Object> details5 = new ArrayList<>();
		List<Object> details6 = new ArrayList<>();
		Map<String, Object> detailsMap4 = new HashMap<>();
		Map<String, Object> detailsMap5 = new HashMap<>();
		Map<String, Object> detailsMap6 = new HashMap<>();
		
		//certId
		licenseSeq.add(0);
		
		gmkt.put("certId", certId);//N, G마켓 인증정보코드, (G마켓상품용)의료기기/방송통신기기/식품제조가공업/건강식품인증/친환경 인증정보 입력(신규)
		gmkt.put("licenseSeq", null);//N, 영업허가/신고코드, (G마켓상품용)영업허가/신고코드 입력(신규)
		
		medicalInstrument.put("isUse", false);//Y, 의료기기 인증 사용 여부, (옥션상품용)의료기기 인증정보 입력(G신규/A유지)
		//medicalInstrument.put("certOfficeName", "string");//N, 의료기기판매신고기관명, (옥션상품용)의료기기 인증정보 입력(G신규/A유지)
		//medicalInstrument.put("certId", "string");//N, 의료기기판매신고번호, (옥션상품용)의료기기 인증정보 입력(G신규/A유지)
		//medicalInstrument.put("itemLicenseId", "string");//N, 의료기기품목허가번호, (옥션상품용)의료기기 인증정보 입력(G신규/A유지)
		//medicalInstrument.put("adDeliberationId", "string");//N, 의료기기사전광고심의번호, (옥션상품용)의료기기 인증정보 입력(G신규/A유지)
		broadcastEquipment.put("isUse", false);//Y, 방송통신기기정합성평가 인증 사용 여부, (옥션상품용)방송통신기기 인증정보 입력(G신규/A유지)
		//broadcastEquipment.put("certId", "string");//N, 방송통신기기 인증번호, (옥션상품용)방송통신기기 인증정보 입력(G신규/A유지)
		//broadcastEquipment.put("isAddtionalCondition", true);//N, 방송통신기기인증정보상품상세별도표기, (옥션상품용)방송통신기기 인증정보 입력(G신규/A유지)
		food.put("isUse", false);//Y, 식품제조가공업입력사항여부, (옥션상품용)식품제조가공업 인증정보 입력(G신규/A유지)
		//food.put("certOfficeName", "string");//N, 식품제조/가공업 신고기관명, (옥션상품용)식품제조가공업 인증정보 입력(G신규/A유지)
		//food.put("certId", "string");//N, 식품제조/가공업 신고번호, (옥션상품용)식품제조가공업 인증정보 입력(G신규/A유지)
		healthFood.put("isUse", false);//Y, 건강기능식품인증여부, (옥션상품용)건강기능식품 인증정보 입력(G신규/A유지)
		//healthFood.put("certOfficeName", "string");//N, 식품제조/가공업 신고기관명, (옥션상품용)건강기능식품 인증정보 입력(G신규/A유지)
		//healthFood.put("certId", "string");//N, 건강기능식품판매업 신고번호, (옥션상품용)건강기능식품 인증정보 입력(G신규/A유지)
		//healthFood.put("adDeliberationNo", "string");//N, 건강기능식품사전광고 심의번호, (옥션상품용)건강기능식품 인증정보 입력(G신규/A유지)
		environmentFriendly.put("isUse", false);//Y, 친환경 인증여부, (옥션상품용)친환경 인증정보 입력(G신규/A유지)
		//environmentFriendly.put("certId", "string");//N, 친환경 인증번호, (옥션상품용)친환경 인증정보 입력(G신규/A유지)
		
		iac.put("medicalInstrument", medicalInstrument);
		iac.put("broadcastEquipment", broadcastEquipment);
		iac.put("food", food);
		iac.put("healthFood", healthFood);
		iac.put("environmentFriendly", environmentFriendly);
		
		//detailsMap4.put("certId", "string");//N, 통합어린이인증대상, "(G마켓/옥션상품통합용)	0:안전인증,1:안전확인,3:공급자적합성"(SafeCertInfoList > CertificationTargetCode)
		//detailsMap4.put("certTargetCode", 0);//N, 통합어린이인증번호, "(G마켓/옥션상품통합용) 인증대상이 공급자 적합성일 경우 TEXT입력"(SafeCertInfoList > CertificationNo)
		//detailsMap5.put("certId", "string");//N, 통합전기용품인증 대상, "(G마켓/옥션상품통합용) 0:안전인증,1:안전확인,3:공급자적합성"(SafeCertInfoList > CertificationTargetCode)
		//detailsMap5.put("certTargetCode", 0);//N, 통합전기용품인증 번호, "(G마켓/옥션상품통합용) 인증대상이 공급자 적합성일 경우 TEXT입력"(SafeCertInfoList > CertificationNo)
		//detailsMap6.put("certId", "string");//N, 통합생활용품인증 대상, "(G마켓/옥션상품통합용) 0:안전인증,1:안전확인,3:공급자적합성"(SafeCertInfoList > CertificationTargetCode)
		//detailsMap6.put("certTargetCode", 0);//N, 통합생활용품인증 번호, "(G마켓/옥션상품통합용) 인증대상이 공급자 적합성일 경우 TEXT입력"(SafeCertInfoList > CertificationNo)
		
		details4.add(detailsMap4);
		details5.add(detailsMap5);
		details6.add(detailsMap6);
		
		child.put("type", 1);//Y, 통합어린이인증 Type, "통합인증대상 상품 아닐경우 ""인증대상아님""으로 입력 (G마켓/옥션상품통합용) 0:인증대상, 1:인증대상아님,	2:상품상세별도표기"(CertificationType)
		child.put("details", details4);
		electric.put("type", 1);//Y, 통합전기용품인증 Type, "통합인증대상 상품 아닐경우 ""인증대상아님""으로 입력(G마켓/옥션상품통합용)"(CertificationType)
		electric.put("details", details5);
		life.put("type", 1);//Y, 통합생활용품인증 Type, "통합인증대상 상품 아닐경우 ""인증대상아님""으로 입력(G마켓/옥션상품통합용)"(CertificationType)
		life.put("details", details6);
		
		safetyCerts.put("child", child);
		safetyCerts.put("electric", electric);
		safetyCerts.put("life", life);
		
		certInfo.put("gmkt", gmkt);
		certInfo.put("iac", iac);
		certInfo.put("safetyCerts", safetyCerts);
		
		itemAddtionalInfo.put("certInfo", certInfo);
		
		//itemAddtionalInfo > images
		Map<String, Object> images = new HashMap<>();
		String imgUrl = requestMap.getString("http") + requestMap.getString("imageUrl")+requestGoodsMap.get("IMAGE_URL").toString().substring(1) + requestGoodsMap.get("IMAGE_P");
		//상품이미지 하드코딩테스트
		//imgUrl = "http://1.255.85.245/goods/084/20016084_g.jpg?_dc=1542177091651";
		images.put("basicImgURL", imgUrl);//Y, 상품기본이미지, 최소 600x600 / 권장 1000x1000(ItemImage >DefaultImage)
		if(requestGoodsMap.get("IMAGE_AP") == null || requestGoodsMap.get("IMAGE_AP").equals("")){
			//images.put("addtionalImg1URL", null);
		}else{
			String imgUrl1 = requestMap.getString("http") + requestMap.getString("imageUrl")+requestGoodsMap.get("IMAGE_URL").toString().substring(1) + requestGoodsMap.get("IMAGE_AP");
			images.put("addtionalImg1URL", imgUrl1);//N, 상품추가이미지1(ItemImage >AddImage1)
		}
		if(requestGoodsMap.get("IMAGE_BP") == null || requestGoodsMap.get("IMAGE_BP").equals("")){
			//images.put("addtionalImg2URL", null);
		}else{
			String imgUrl2 = requestMap.getString("http") + requestMap.getString("imageUrl")+requestGoodsMap.get("IMAGE_URL").toString().substring(1) + requestGoodsMap.get("IMAGE_BP");
			images.put("addtionalImg2URL", imgUrl2);//N, 상품추가이미지1(ItemImage >AddImage2)
		}
		itemAddtionalInfo.put("images", images);
		
		//itemAddtionalInfo > weight
		//itemAddtionalInfo.put("weight", 0);//N, 상품무게(G유지/A신규)(ItemWeight)
		
		//itemAddtionalInfo > descriptions
		Map<String, Object> descriptions = new HashMap<>();
		Map<String, Object> kor = new HashMap<>();
		
		kor.put("type", 2);//Y, 상품상세정보등록형태, "1:contentID, 2:html	ContenctID 추후 기능 제공 예정"(신규)
		//kor.put("contentId", "string");//N, 상품상세정보코드, ContenctID 추후 기능 제공 예정(신규)
		kor.put("html", requestDescribeMap.get("DESCRIBE_EXT"));//N, HTML, "상품상세 Html 입력	iframe, Script 등은 사용 불가 "(NewItemDescription > GdHtml)
		descriptions.put("kor", kor);
		
		itemAddtionalInfo.put("descriptions", descriptions);
		
		/** itemAddtionalInfo 끝*/
		params.put("itemAddtionalInfo", itemAddtionalInfo);
		/** addtionalInfo */
		Map<String, Object> addtionalInfo = new HashMap<>();
		
		//addtionalInfo > sellerDiscount
		Map<String, Object> sellerDiscount = new HashMap<>();
		Map<String, Object> gmkt2 = new HashMap<>();
		Map<String, Object> iac2 = new HashMap<>();
		
/** TODO SD1 프로모션 적용 ( ARS+일시불+즉시할인쿠폰 ) **/
		
		double dcAmt = Double.parseDouble(requestGoodsMap.get("DC_AMT").toString()); // ARS할인금액
		double lumpSumDcAmt = Double.parseDouble(requestGoodsMap.get("LUMP_SUM_DC_AMT").toString()); //일시불 할인금액
		double gmktCouponPrice = 0;
		double iacCouponPrice = 0;

		//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
		/*프로모션 G마켓, 옥션 분리*/
		//G마켓
		for(int i=0; i < requestGmktPriceMapList.size(); i++) {
			if(requestGmktPriceMapList.get(i).get("PROC_GB") != null) {
				if(!requestGmktPriceMapList.get(i).get("PROC_GB").toString().equals("D")) {
					if(requestGmktPriceMapList.get(i).get("PA_GROUP_CODE").toString().indexOf("02") > -1) { //G마켓, 옥션 분리
						gmktCouponPrice += Double.parseDouble(requestGmktPriceMapList.get(i).get("DO_COST").toString()); //할인쿠폰(자동적용쿠폰 + 제휴OUT) 할인금액
					}
				}
			}
		}
		
		//옥션
		for(int i=0; i < requestIacPriceMapList.size(); i++) {
			if(requestIacPriceMapList.get(i).get("PROC_GB") != null) {
				if(!requestIacPriceMapList.get(i).get("PROC_GB").toString().equals("D")) {
					if(requestIacPriceMapList.get(i).get("PA_GROUP_CODE").toString().indexOf("03") > -1) {
						iacCouponPrice += Double.parseDouble(requestIacPriceMapList.get(i).get("DO_COST").toString()); //할인쿠폰(자동적용쿠폰 + 제휴OUT) 할인금액
					}
				}
			}
		}
		//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
		
		double gmktPromoPrice = dcAmt +lumpSumDcAmt + gmktCouponPrice; //G마켓 SD1 연동 금액
		double iacPromoPrice = dcAmt +lumpSumDcAmt + iacCouponPrice; //옥션 SD1 연동 금액
		
		if(gmktPromoPrice > 0) {
			gmkt2.put("type", 1);//Y, G마켓할인타입, "0:사용안함, 1:정액, 2:정률 * type을 0으로 지정 시, 다른 값을 보내지 않거나, null로 호출 ""iac"": { ""type"": ""0"", ""priceOrRate1"": 0, ""priceOrRate2"": 0, ""startDate"": ""2018-06-21T03:12:47.461Z"", ""endDate"": ""2018-06-30T03:12:47.461Z"" } (X)""iac"": { ""type"": ""0"" } (O)"
			gmkt2.put("priceOrRate1", gmktPromoPrice);//N, G마켓할인액(율)(DiscountPrice)
			gmkt2.put("priceOrRate2", 0);//N, G마켓할인액(율)_SD2, SD2사용하는판매자만(권한)(DiscountPrice2)
			gmkt2.put("startDate", gDate(requestMap.getString("FROM_DATE")));//N, G마켓할인시작(DiscountDate >StartDate)
			gmkt2.put("endDate", gDate("2999-12-31 00:00:00.0"));//N, G마켓할인종료(DiscountDate >EndDate)
		} else {
			gmkt2.put("type", 0);
		}
		
		if(iacPromoPrice > 0) {
			iac2.put("type", 1);//Y, 옥션할인타입, "0:사용안함, 1:정액, 2:정률"(신규)
			iac2.put("priceOrRate1", iacPromoPrice);//N, 옥션할인액(율)(SellerDiscount)
			iac2.put("priceOrRate2", 0);//N, 옥션할인액(율)_SD2, SD2사용하는판매자만(권한)(SellerDiscount2)
			iac2.put("startDate", gDate(requestMap.getString("FROM_DATE")));//N, 옥션할인시작(SellerDiscountFromDate)
			iac2.put("endDate", gDate("2999-12-31 00:00:00.0"));//N, 옥션할인종료,SellerDiscountToDate)
		} else {
			iac2.put("type", 0);
		}
		
		sellerDiscount.put("isUse", true);//Y, 판매자할인 사용여부, "상품가격에서 추가로 판매자 할인 적용할 경우 입력 위에 등록한 G판매가격/A판매가격에서 할인 적용됨  true : 할인적용, false :할인미적용"(IsDiscount)
		sellerDiscount.put("gmkt", gmkt2);
		sellerDiscount.put("iac", iac2);
		
		addtionalInfo.put("sellerDiscount", sellerDiscount);
		
		//addtionalInfo > siteDiscount
		Map<String, Object> siteDiscount = new HashMap<>();
		
		siteDiscount.put("gmkt", true);//Y, G마켓부담할인적용, true:적용,false:미적용(IsGmktDiscount)
		siteDiscount.put("iac", true);//Y, 옥션부담할인율, true:적용,false:미적용(ItemAuctionDiscount)
		
		addtionalInfo.put("siteDiscount", siteDiscount);
		
		//addtionalInfo > gift
		Map<String, Object> gift = new HashMap<>();
		
		gift.put("name", "");//N, 사은품명(FreeGift)
		
		addtionalInfo.put("gift", gift);
		
		//addtionalInfo > pcs
		Map<String, Object> pcs = new HashMap<>();
		
		pcs.put("isUse", true);//Y, 가격비교노출, "가격비교사이트 노출여부 설정 true:노출,false:미노출"(Refusal >IsPriceCompare)
		pcs.put("isUseIacPcsCoupon", true);// N, 옥션에서 제공하는 가격비교 사이트 할인 적용 여부 설정(IsPCS)
		pcs.put("isUseGmkPcsCoupon", true);// N, G마켓에서 제공하는 가격비교 사이트 할인 적용 여부 설정(IsGmktDiscount)
		
		addtionalInfo.put("pcs", pcs);
		
		//addtionalInfo > overseaSales
		Map<String, Object> overseaSales = new HashMap<>();
		
		overseaSales.put("isAgree", false);//Y, G마켓해외배송유무, "(G마켓 상품용)	True : 전체 국가 배송 가능	False : 전체 국가 배송 불가"
		
		addtionalInfo.put("overseaSales", overseaSales);
		
		/** addtionalInfo 끝*/
		params.put("addtionalInfo", addtionalInfo);
		//String body = mapToJson(params);
		return params;
	}
	//4.2 Site 상품번호 조회
	//[GET]	https://sa.esmplus.com/item/v1/goods/{goodsNo}/status
	/*
	 public String createGoodsNoSelectBody() {
		Map<String, Object> params = new HashMap<>();
		
		params.put("goodsNo", 0);//N, 마스터상품번호
		params.put("sellManageCode", "String");//N, 판매자 상품코드
		params.put("innerResultCode", "String");//N, Site피딩관리코드
		params.put("innerMessage", "String");//N, Site피딩관리코드메시지
		
		Map<String, Object> gmkt = new HashMap<>();
		
		gmkt.put("SiteGoodsNo", "String");//N, G마켓상품번호
		gmkt.put("SiteGoodsComment", "String");//N, G마켓메시지

		Map<String, Object> iac = new HashMap<>();
		
		iac.put("SiteGoodsNo", "String");//N, 옥션상품번호
		iac.put("SiteGoodsComment", "String");//N, 옥션메시지
		
		Map<String, Object> siteDetail = new HashMap<>();
		
		siteDetail.put("gmkt", gmkt);
		siteDetail.put("iac", iac);
		
		params.put("siteDetail", siteDetail);
		
		String body = mapToJson(params);
		
		return body;	
	}
	*/
	//4.4 상품 가격/재고/판매상태 수정
	//[PUT]https://sa.esmplus.com/item/v1/goods/{goodsNo}/sell-status

	public Map<String, Object> createGoodsPriceSaleYnBody(ParamMap requestMap) {
		Map<String, Object> params = new HashMap<>();
		HashMap<String, Object> subMap = (HashMap<String, Object>) requestMap.get("map");
		
		if("".equals(requestMap.getString("isSell"))) requestMap.put("isSell","false");
		Boolean issell = Boolean.parseBoolean(requestMap.getString("isSell"));
		/** isSell */
		Map<String, Object> isSell = new HashMap<>();
		/*
		boolean saleFlag = true;
		if(subMap.get("PA_SALE_GB").toString().equals("30")){
			saleFlag=false;
		}
		switch(requestMap.getString("siteGb")){
			case "PAE":
				isSell.put("gmkt", saleFlag);//Y, G마켓판매여부(SalePrice/StockQty) "True:판매,False:판매중지  False일 경우, 함께 호출되는 재고/가격/판매기간값 업데이트 되지 않음"
				isSell.put("iac", saleFlag);//Y, 옥션판매여부(신규) "True:판매,False:판매중지  False일 경우, 함께 호출되는 재고/가격/판매기간값 업데이트 되지 않음"
				break;
			case "PAG":
				isSell.put("gmkt", saleFlag);//Y, G마켓판매여부(SalePrice/StockQty)
				isSell.put("iac", false);//Y, 옥션판매여부(신규)
				break;
			case "PAA":
				isSell.put("gmkt", false);//Y, G마켓판매여부(SalePrice/StockQty)
				isSell.put("iac", saleFlag);//Y, 옥션판매여부(신규)
				break;
		}
		*/
		//G+A 양측 판매중지시에만 사용하도록 설계... 2019.03.04
		
		/** ItemBasicInfo */
		Map<String, Object> itemBasicInfo = new HashMap<>();
		Map<String, Object> price = new HashMap<>();		
		Map<String, Object> sellingPeriod = new HashMap<>();
		
		Map<String, Object> stock = new HashMap<>();
		
		int orderAbleQty = Integer.parseInt(subMap.get("TRANS_ORDER_ABLE_QTY").toString());
		if(orderAbleQty > 4999){
			orderAbleQty = 4999;
		}else if (orderAbleQty == 0){
			orderAbleQty = 1;
		}
		
		if(requestMap.get("siteGb").toString().equals("PAG")){
			isSell.put("gmkt", issell);
		}else if(requestMap.get("siteGb").toString().equals("PAA")){
			isSell.put("iac" , issell);
		}else{
			isSell.put("gmkt", issell);
			isSell.put("iac" , issell);
		}
		
		price.put("gmkt", subMap.get("SALE_PRICE"));//Y, G마켓판매가격
		sellingPeriod.put("gmkt", 365);//Y, G마켓판매기간, "G마켓 판매기간 입력  Value : 15,30,60,90,365	입력 시, 호출시점 기준으로 +Dday 세팅되는 구조"
		stock.put("gmkt", orderAbleQty);//Y, G마켓재고
		
		price.put("iac", subMap.get("SALE_PRICE"));//Y, 옥션판매가격
		sellingPeriod.put("iac", 365);//Y, 옥션판매기간, "옥션 판매기간 입력  Value : 15,30,60,90,365	입력 시, 호출시점 기준으로 +Dday 세팅되는 구조"
		stock.put("iac", orderAbleQty);//Y, 옥션재고
		
		itemBasicInfo.put("Price", price);
		itemBasicInfo.put("SellingPeriod", sellingPeriod);
		itemBasicInfo.put("Stock", stock);
		
		params.put("isSell", isSell);
		params.put("ItemBasicInfo", itemBasicInfo);
		
		return params;
	}
	
	//4.6 상품 판매자 부담할인 수정
	//[POST]https://sa.esmplus.com/item/v1/goods/{goodsNo}/seller-discounts
	/*
	public Map<String, Object> createSellerDiscountUpdateBody(ParamMap requestMap) throws Exception {
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> goodsPriceMap = (Map<String, Object>) requestMap.get("goodsPriceMap");
		
		Map<String, Object> sellerDiscount = new HashMap<>();
		
		sellerDiscount.put("isUse", true);//N, 판매자할인 사용여부
		
		Map<String, Object> gmkt = new HashMap<>();
		
		gmkt.put("type", 1);//G마켓할인타입
		gmkt.put("priceOrRate1", goodsPriceMap.get("DC_AMT"));//G마켓할인액(율)
		gmkt.put("priceOrRate2", 0);//G마켓할인액(율)_SD2
		gmkt.put("startDate", gDate(requestMap.getString("FROM_DATE")));//G마켓할인시작
		gmkt.put("endDate", gDate("2999-12-31 00:00:00.0"));//G마켓할인종료
		
		sellerDiscount.put("gmkt", gmkt);
		
		Map<String, Object> iac = new HashMap<>();
		
		iac.put("type", 0);//옥션할인타입 (1:정액, 2:정율)
		iac.put("priceOrRate1", 0);//옥션할인액(율)
		iac.put("priceOrRate2", 0);//옥션할인액(율)_SD2
		iac.put("startDate", gDate(requestMap.getString("FROM_DATE")));//옥션할인시작
		iac.put("endDate", gDate(requestMap.getString("TO_DATE")));//옥션할인종료
		
		sellerDiscount.put("iac", null);
		
		params.put("SellerDiscount", sellerDiscount);
		
		return params;
	}
	*/
	//4.9 상품 이미지 수정
	//[PUT]	https://sa.esmplus.com/item/v1/goods/{goodsNo}/images
	/*
	public Map<String, Object> createGoodsImageUpdateBody(ParamMap requestMap) {
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> basicImage = new HashMap<>();
		HashMap<String, Object> requestGoodsImageMap = (HashMap<String, Object>) requestMap.get("requestGoodsImageMap");
		String imgUrl = requestMap.getString("http") + requestMap.getString("imageUrl")+requestGoodsImageMap.get("IMAGE_URL").toString().substring(1) + requestGoodsImageMap.get("IMAGE_P");
		String addImgUrl1 = null;
		String addImgUrl2 = null;
		
		if(requestGoodsImageMap.get("IMAGE_AP") == null || requestGoodsImageMap.get("IMAGE_AP").equals("")){
			//images.put("addtionalImg1URL", null);
		}else{
			addImgUrl1 = requestMap.getString("http") + requestMap.getString("imageUrl")+requestGoodsImageMap.get("IMAGE_URL").toString().substring(1) + requestGoodsImageMap.get("IMAGE_AP");
		}
		if(requestGoodsImageMap.get("IMAGE_BP") == null || requestGoodsImageMap.get("IMAGE_BP").equals("")){
			//images.put("addtionalImg2URL", null);
		}else{
			addImgUrl2 = requestMap.getString("http") + requestMap.getString("imageUrl")+requestGoodsImageMap.get("IMAGE_URL").toString().substring(1) + requestGoodsImageMap.get("IMAGE_BP");
		}
		
		basicImage.put("Url", imgUrl);//Y, 상품기본이미지, 최소 600x600 / 권장 1000x1000(ItemImage >DefaultImage)
		
		Map<String, Object> imageModel = new HashMap<>();
		
		imageModel.put("BasicImage", basicImage);
		
		Map<String, Object> additionalImage1 = new HashMap<>();
		
		additionalImage1.put("Url", addImgUrl1);//N, 상품추가이미지1(ItemImage >AddImage1)
		
		imageModel.put("AdditionalImage1", additionalImage1);
		
		Map<String, Object> additionalImage2 = new HashMap<>();
		
		additionalImage2.put("Url", addImgUrl2);//N, 상품추가이미지2(ItemImage >AddImage2)
		
		imageModel.put("AdditionalImage2", additionalImage2);
		
		params.put("imageModel", imageModel);
		
		return params;
	}
	*/
	//4.10 추천옵션 등록 및 수정
	//[POST]https://sa.esmplus.com/item/v1/goods/{goodsNo}/recommended-options (등록)
	//[PUT]https://sa.esmplus.com/item/v1/goods/{goodsNo}/recommended-options (수정)
	/*
	public String createRecommendedOptBody() {
		Map<String, Object> params = new HashMap<>();
		
		params.put("type", 1);//Y, 추천옵션타입, "ESM 추천옵션용 기능으로 사용여부 체크  N:옵션미사용  1: 선택형, 2: 2개조합선택형  주문옵션은 등록 대상 G/A 카테고리가 모두 허용 될 경우만 가능"(신규)
		params.put("isStockManage", true);
		Map<String, Object> detailsMap = new HashMap<>();
		
		detailsMap.put("recommendedOptValueNo", 0);//N, 선택형 추천옵션항목코드, 추천옵션그룹별 선택 항목 조회  API에서 매칭된 코드 입력(신규)
		detailsMap.put("isSoldOut", true);//N, 선택형 품절여부, true:품절, false:판매(신규)
		detailsMap.put("isDisplay", true);//N, 선택형 노출여부, true:노출, false:미노출(신규)
		detailsMap.put("qty", 0);//Y, 선택형 재고수량, "미입력시 99999로 입력 되며 상품의 판매 수량을 적용 하고, 입력시에는 위에 G판매수량/A판매수량에 입력한 판매 수량은 무시함(ESM로직과 동일)"(신규)
		detailsMap.put("manageCode", "String");//N, 선택형 판매자옵션관리코드, 추천옵션의 판매자 관리 코드(신규)
		
		Map<String, Object> independent = new HashMap<>();
		List<Object> details = new ArrayList<>();
		
		details.add(detailsMap);
		
		independent.put("recommendedOptNo", 0);//N, 선택형 추천옵션그룹코드, Site카테고리별 추천옵션그룹 조회  API에서 매칭된 코드 입력(신규)
		independent.put("details", details);
		
		params.put("independent", independent);
		
		Map<String, Object> detailsMap2 = new HashMap<>();
		
		detailsMap2.put("recommendedOptValueNo1", 0);//N, 조합형 추천옵션항목코드1, "조합형1번옵션항목값 추천옵션그룹별 선택 항목 조회 API에서 매칭된 코드 입력"(신규)
		detailsMap2.put("recommendedOptValueNo2", 0);//N, 조합형 추천옵션항목코드2, "조합형2번옵션항목값 추천옵션그룹별 선택 항목 조회 API에서 매칭된 코드 입력"(신규)
		detailsMap2.put("isSoldOut", true);//N, 조합형 품절여부, "추천옵션 품절 여부 입력	true:품절, false:판매"(신규)
		detailsMap2.put("isDisply", true);//N, 조합형 노출여부, "추천옵션 노출 여부 입력	true:노출, false:미노출"(신규)
		detailsMap2.put("qty", 0);//N, 조합형 재고수량, "미입력시 99999로 입력 되며 상품의 판매 수량을 적용 하고, 입력시에는 위에 G판매수량/A판매수량에 입력한 판매 수량은 무시함(ESM로직과 동일)"(신규)
		detailsMap2.put("manageCode", "String");//N, 조합형 판매자옵션관리코드, 추천옵션의 판매자 관리 코드(신규)
		
		Map<String, Object> combination = new HashMap<>();
		List<Object> details2 = new ArrayList<>();
		
		details2.add(detailsMap2);
		
		combination.put("recommendedOptNo1", 0);//N, 조합형 추천옵션그룹코드1, "조합형1번옵션명  Site카테고리별 추천옵션그룹 조회 API에서 매칭된 코드 입력"(신규)
		combination.put("recommendedOptNo2", 0);//N, 조합형 추천옵션그룹코드2, "조합형2번옵션명  Site카테고리별 추천옵션그룹 조회 API에서 매칭된 코드 입력"(신규)
		combination.put("details", details2);
		
		params.put("combination", combination);
		
		String body = mapToJson(params);
		
		return body;
	}
	*/
	//4.13 구버전 옵션 등록
	//[POST]https://sa.esmplus.com/item/v1/goods/{goodsNo}/order-options
	private Map<String,Object> createGoodsOptionBody(ParamMap requestMap) {
		Map<String, Object> params = new HashMap<>();
		List<HashMap<String, Object>> requestOptionMap = (List<HashMap<String, Object>>) requestMap.get("map");
		boolean combinationYn = false; // 조합형 사용 여부
		
		String goodsdtInfoKind = requestOptionMap.get(0).get("GOODSDT_INFO_KIND").toString();
		if(goodsdtInfoKind.equals("기타")||goodsdtInfoKind.equals("없음")){
			goodsdtInfoKind = "단일상품";
		};
		
		if(requestOptionMap.get(0).get("COMBINATION_YN").toString() != null){
			if("1".equals(requestOptionMap.get(0).get("COMBINATION_YN").toString())){
				if(goodsdtInfoKind.contains("색상") && goodsdtInfoKind.contains("사이즈")){
					combinationYn = true;
				}
			}
		}
		
		for(HashMap<String,Object> option : requestOptionMap){
			String goodsdtInfo = option.get("GOODSDT_INFO").toString();			
			if(!goodsdtInfo.contains("/")) {
				combinationYn = false;
			}				
		}
		
		//조합형 사용
		if(combinationYn){
			params.put("type", "2");
			params.put("isStockManage", true);//Y, 재고관리를 하는지 여부?
			
			Map<String, Object> combination = new HashMap<>();
			Map<String, Object> name1 = new HashMap<>();
			Map<String, Object> name2 = new HashMap<>();
			
			name1.put("kor", "색상");
			name2.put("kor", "사이즈");
			combination.put("name1", name1);
			combination.put("name2", name2);
			
			List<Object> details2 = new ArrayList<>();
			for(HashMap<String,Object> option : requestOptionMap){
				
				Map<String, Object> details2Map = new HashMap<>();
				Map<String, Object> value1 = new HashMap<>();
				Map<String, Object> value2 = new HashMap<>();
				
				String goodsdtInfo = option.get("GOODSDT_INFO").toString();
				
				//단품종류 문자열의 마지막이 / 일때 / 를 삭제
				if(goodsdtInfo.substring(goodsdtInfo .length() - 1, goodsdtInfo.length()).equals("/") ){
					goodsdtInfo = goodsdtInfo.substring(0, goodsdtInfo .length() - 1);
				}

				value1.put("kor", goodsdtInfo.substring(0, goodsdtInfo.indexOf("/")));
				details2Map.put("value1", value1);//조합형 옵션항목 ex(색상)
				
				value2.put("kor", goodsdtInfo.substring(goodsdtInfo.indexOf("/")+1, goodsdtInfo.length()));
				details2Map.put("value2", value2);//조합형 옵션항목 ex(크기)
				
				int orderAbleQty = Integer.parseInt(option.get("TRANS_ORDER_ABLE_QTY").toString());
				if(orderAbleQty>4999){
					orderAbleQty=4999;
				}
				
				boolean soldout = false;
				if(orderAbleQty == 0){
					soldout = true;
				}
				if(!option.get("SALE_GB").toString().equals("00")){
					//soldout = true;
					continue;
				}
				
				details2Map.put("isSoldOut", soldout);//품절여부
				details2Map.put("isDisplay", true);//노출여부
				
				Map<String, Object> qty = new HashMap<>();
				qty.put("iac", orderAbleQty);
				qty.put("gmkt", orderAbleQty); //선택형 옥션 재고수량 옵션 재고수량 입력 시, 마스터 상품의 재고수량 무시되고 옵션 재고수량 Update됨
				details2Map.put("qty", qty);
				details2Map.put("manageCode", option.get("GOODS_CODE").toString()+option.get("GOODSDT_CODE").toString());//조합형 판매자옵션관리코드
				details2Map.put("epinCode", null);
				details2.add(details2Map);
			}
			
			combination.put("details", details2);
			
			params.put("combination",combination); //조합형 옵션 사용시
			params.put("independent",null);
			params.put("text",null);
			
			
		}else{ //선택형 사용
			
			params.put("type", "1");//Y, "0: 사용 안 함		1: 선택형		2 : 2개조합형		3: 3개조합형(추후제공)		4: 계산형(추후제공)		5: 텍스트형 		6: 선택형 +텍스트형		7: 2개조합형 +텍스트형 "
			params.put("isStockManage", true);//Y, 재고관리를 하는지 여부?
			
			/** 선택형 옵션 사용시 시작*/
			/**
			     "independent": [
			        {
			            "details": [
			                {
			                    "epinCode": null,
			                    "isDisplay": true,
			                    "isSoldOut": true,
			                    "manageCode": "string",//관리코드 (상품코드 + dt번호)
			                    "qty": {
			                        "gmkt": 0, // 99999 재고수량 (tpagoodsdtmapping table 조회)
			                        "iac": null
			                    },
			                    "value": {
			                        "kor": "string" //선택형 옵션항목 ex(단일상품,빨강색)
			                    }
			                }
			            ],
			            "name": {
			                "kor": "string" //선택형 옵션그룹명 ex(색상 크기 무늬 형태)
			            }
			        }
			    ]
			 
			 */
			List<Object> independent = new ArrayList<>();
			Map<String, Object> independentMap = new HashMap<>();
			Map<String, Object> name = new HashMap<>();
			
			
			name.put("kor", goodsdtInfoKind);//선택형 옵션그룹명 ex(색상 크기 무늬 형태)
			independentMap.put("name", name);
			List<Object> details = new ArrayList<>();
			
			for(HashMap<String,Object> option : requestOptionMap){
				Map<String, Object> detailsMap = new HashMap<>();
				Map<String, Object> value = new HashMap<>();
				
				String goodsdtInfo =option.get("GOODSDT_INFO").toString();
				if(goodsdtInfo.equals("없음")){
					goodsdtInfo = "단일상품";
				}
				value.put("kor", goodsdtInfo);
				detailsMap.put("value", value);//선택형 옵션항목 ex(단일상품,빨강색)
				
				int orderAbleQty = Integer.parseInt(option.get("TRANS_ORDER_ABLE_QTY").toString());
				if(orderAbleQty>4999){
					orderAbleQty=4999;
				}
				
				boolean soldout = false;
				if(orderAbleQty == 0){
					soldout = true;
				}
				if(!option.get("SALE_GB").toString().equals("00")){
					//soldout = true;
					continue;
				}
				
				detailsMap.put("isSoldOut", soldout);//품절여부
				detailsMap.put("isDisplay", true);//노출여부
				
				Map<String, Object> qty = new HashMap<>();
				qty.put("iac", orderAbleQty);
				qty.put("gmkt", orderAbleQty);//선택형 옥션 재고수량 옵션 재고수량 입력 시, 마스터 상품의 재고수량 무시되고 옵션 재고수량 Update됨
				detailsMap.put("qty", qty);
				detailsMap.put("manageCode", option.get("GOODS_CODE").toString()+option.get("GOODSDT_CODE").toString());//선택형 판매자옵션관리코드
				detailsMap.put("epinCode", null);
				details.add(detailsMap);
			}
			independentMap.put("details", details);
			
			independent.add(independentMap);
			/** 선택형 옵션 사용시 끝*/
			
			/** 조합형 옵션 사용시 시작*/
			/*Map<String, Object> combination = new HashMap<>();
			Map<String, Object> name1 = new HashMap<>();
			Map<String, Object> name2 = new HashMap<>();
			name1.put("kor", "string");
			name2.put("kor", "string");
			combination.put("name1", name1);
			combination.put("name2", name2);
			
			List<Object> details2 = new ArrayList<>();
			Map<String, Object> details2Map = new HashMap<>();
			Map<String, Object> value1 = new HashMap<>();
			Map<String, Object> value2 = new HashMap<>();
			value1.put("kor", "string");
			value2.put("kor", "string");
			details2Map.put("value1", value1);
			details2Map.put("value2", value2);
			details2Map.put("isSoldOut", true);
			details2Map.put("isDisplay", true);
			Map<String, Object> qty2 = new HashMap<>();
			qty2.put("iac", null);
			qty2.put("gmkt", 0);
			details2Map.put("qty", qty);
			details2Map.put("manageCode", "string");
			details2Map.put("epinCode", null);
			details2.add(details2Map);
			
			combination.put("details", details2);*/
			params.put("combination",null); //조합형 옵션 사용시
			/** 조합형 옵션 사용시 끝*/
			
			/** 텍스트형 옵션 사용시 시작 */
			/*List<Object> text = new ArrayList<>();
			Map<String, Object> textMap = new HashMap<>();
			Map<String, Object> name3 = new HashMap<>();
			name3.put("kor", "string");
			textMap.put("name", name3);
			textMap.put("isDisplay", true);
			text.add(textMap);*/
			params.put("text",null);
			/** 텍스트형 옵션 사용시 끝 */
			
			params.put("independent",independent);
			
		}
		return params;
	}
	
	//4.17 상품 광고/부가서비스 등록 및 수정
	//[POST] https://sa.esmplus.com/item/v1/advertising/listing (등록)
	//[PUT] https://sa.esmplus.com/item/v1/advertising/listing (수정)
	/*
	public String createGoodsAdvertiseBody() {
		Map<String, Object> params = new HashMap<>();
		
		params.put("goodsNo", 0);//Y, 마스터상품번호(신규)
		
		Map<String, Object> iacPremium = new HashMap<>();
		
		iacPremium.put("isUse", true);//Y, 옥션프리미엄사용여부, true: 사용, false : 미사용(A신규)
		iacPremium.put("applyPeriod", 0);//N, 사용일, API호출 기준으로 1,3,5,7,14,15,28,30,56,60,90 입력(A신규)
		iacPremium.put("autoExtendType", 1);//N, 자동연장설정, 1:자동연장미사용,2:무제한연장,3:제한기간연장(A신규)
		iacPremium.put("autoExtendAmountDay", 0);//N, 자동연장사용일, 1,3,5,7,14,15,28,30,56,60,90 입력(A신규)
		iacPremium.put("autoExtendEndDate", "2018-03-21T05:33:52.882Z");//N, 제한기간일자, AutoExtendType=3 제한기간 연장일 경우, 연장종료기간날짜(A신규)
		
		params.put("iacPremium", iacPremium);
		
		Map<String, Object> iacBoldItemTitle = new HashMap<>();
		
		iacBoldItemTitle.put("isUse", true);//Y, 옥션상품명굵게사용여부, true: 사용, false : 미사용(A신규)
		iacBoldItemTitle.put("applyPeriod", 0);//N, 사용일, API호출 기준으로 1,3,5,7,14,15,28,30,56,60,90 입력(A신규)
		iacBoldItemTitle.put("autoExtendType", 1);//N, 자동연장설정, 1:자동연장미사용,2:무제한연장,3:제한기간연장(A신규)
		iacBoldItemTitle.put("autoExtendAmountDay", 0);//N, 자동연장사용일, 1,3,5,7,14,15,28,30,56,60,90 입력(A신규)
		iacBoldItemTitle.put("autoExtendEndDate", "2018-03-21T05:33:52.882Z");//N, 제한기간일자, AutoExtendType=3 제한기간 연장일 경우, 연장종료기간날짜(A신규)
		
		params.put("iacBoldItemTitle", iacBoldItemTitle);
		
		Map<String, Object> iacPremiumPlus = new HashMap<>();
		
		iacPremiumPlus.put("isUse", true);//Y, 옥션프리미엄플러스사용여부, true: 사용, false : 미사용(A신규)
		iacPremiumPlus.put("applyPeriod", 0);//N, 사용일, API호출 기준으로 1,3,5,7,14,15,28,30,56,60,90 입력(A신규)
		iacPremiumPlus.put("autoExtendType", 1);//N, 자동연장설정, 1:자동연장미사용,2:무제한연장,3:제한기간연장(A신규)
		iacPremiumPlus.put("autoExtendAmountDay", 0);//N, 자동연장사용일, 1,3,5,7,14,15,28,30,56,60,90 입력(A신규)
		iacPremiumPlus.put("autoExtendEndDate", "2018-03-21T05:33:52.882Z");//N, 제한기간일자, AutoExtendType=3 제한기간 연장일 경우, 연장종료기간날짜(A신규)
		
		params.put("iacPremiumPlus", iacPremiumPlus);
		
		Map<String, Object> gmktFocus = new HashMap<>();
		
		gmktFocus.put("isUse", true);//Y, G마켓포커스사용여부, true: 사용, false : 미사용(G신규)
		gmktFocus.put("applyPeriod", 0);//N, 사용일, API호출 기준으로 1,3,5,7,14,15,28,30,56,60,90 입력(G신규)
		gmktFocus.put("autoExtendType", 1);//N, 자동연장설정, 1:자동연장미사용,2:무제한연장,3:제한기간연장(G신규)
		gmktFocus.put("autoExtendAmountDay", 0);//N, 자동연장사용일, 1,3,5,7,14,15,28,30,56,60,90 입력(G신규)
		gmktFocus.put("autoExtendEndDate", "2018-03-21T05:33:52.882Z");//N, 제한기간일자, AutoExtendType=3 제한기간 연장일 경우, 연장종료기간날짜(G신규)
		
		params.put("gmktFocus", gmktFocus);
		
		Map<String, Object> gmktFocusPlus = new HashMap<>();
		
		gmktFocus.put("isUse", true);//Y, G마켓포커스플러스사용여부, true: 사용, false : 미사용(G신규)
		gmktFocus.put("applyPeriod", 0);//N, 사용일, API호출 기준으로 1,3,5,7,14,15,28,30,56,60,90 입력(G신규)
		gmktFocus.put("autoExtendType", 1);//N, 자동연장설정, 1:자동연장미사용,2:무제한연장,3:제한기간연장(G신규)
		gmktFocus.put("autoExtendAmountDay", 0);//N, 자동연장사용일, 1,3,5,7,14,15,28,30,56,60,90 입력(G신규)
		gmktFocus.put("autoExtendEndDate", "2018-03-21T05:33:52.882Z");//N, 제한기간일자, AutoExtendType=3 제한기간 연장일 경우, 연장종료기간날짜(G신규)
		
		params.put("gmktFocusPlus", gmktFocusPlus);
		
		String body = mapToJson(params);
		
		return body;
	}
	*/
	//4.20 상품명 변경
	//[PUT] https://sa.esmplus.com/item/v1/goods/{goodsNo}/goods-name
	public Map<String, Object> createGoodsNameUpdateBody(ParamMap requestMap) {
		Map<String, Object> params = new HashMap<>();
		HashMap<String, Object> subMap = (HashMap<String, Object>) requestMap.get("map");
		
		params.put("kor", ComUtil.subStringBytes(subMap.get("GOODS_NAME").toString(), 100));//N, 상품명(국문)
		params.put("promotion", "");//N, 프로모션 상품명
		
		return params;
	}
	
	//4.21 G9사이트 상품노출 설정
	//[POST] https://sa.esmplus.com/item/v1/goods/{goodsNo}/g9
	public String createGoodsDisplayBody() {
		Map<String, Object> params = new HashMap<>();
		
		params.put("isGmarketDisplay", true);//Y, G마켓노출여부
		params.put("isG9Display", true);//Y, G9노출여부
		
		String body = mapToJson(params);
		
		return body;
	}
	
	//4.22 그룹 생성
	//[POST] https://sa.esmplus.com/item/v1/groups
	/*
	public String createGroupBody() {
		Map<String, Object> params = new HashMap<>();
		
		params.put("groupName", "String");//Y, 그룹명
		params.put("groupListType", 0);//Y, 그룹리스트타입, 0:리스트형, 1:이미지형
		params.put("introImageFile", "String");//N, 인트로이미지, 미호출 시, 이미지 없이 처리됨
		params.put("goodsNo", 0);//Y, 마스터상품번호, "그룹기준 상품번호 등록  그룹수정 시, 상품번호 파라미터 호출하지 않음 (워드 가이드 참고) "(신규)
		
		String body = mapToJson(params);
		
		return body;
	}
	*/
	
	//4.23 그룹 수정
	//[PUT] https://sa.esmplus.com/item/v1/groups/{groupNo}
	/*
	public String createGroupUpdateBody() {
		Map<String, Object> params = new HashMap<>();
		
		params.put("groupName", "String");//Y, 그룹명
		params.put("groupListType", 0);//Y, 그룹리스트타입, 0:리스트형, 1:이미지형
		params.put("introImageFile", "String");//N, 인트로이미지, 미호출 시, 이미지 없이 처리됨
		
		String body = mapToJson(params);
		
		return body;
	}
	*/
	//4.27 그룹 상품 등록
	//[PUT] https://sa.esmplus.com/item/v1/groups/{groupNo}/goods
	/*
	public String createGoodsGroupBody() {
		Map<String, Object> params = new HashMap<>();
		
		List<Object> goodsNo = new ArrayList<>();
		
		goodsNo.add(0);//Y, 마스터상품번호, "그룹 추가할 상품번호 등록  * API > PUT 방식이므로 해당 그룹에 포함되어야 할 모든 상품번호를 호출해야함(Upsert방식)"(신규)
		
		params.put("goodsNo", goodsNo);
		
		String body = mapToJson(params);
		
		return body;
	}
	*/
	//4.32 옥션 특별할인 등록/수정
	//[POST] https://sa.esmplus.com/item/v1/goods/{goodsNo}/customer-benefit/special-discount
	/*
	public String createSpecialDiscountBody() {
		Map<String, Object> params = new HashMap<>();
		
		params.put("vipMemberTarget", true);//Y, 우수회원여부, true : 우수회원 false : 우수회원 아님(신규)
		params.put("vipMemberDiscountAmount", 0);//Y, 우수회원할인금액, 구매수량당 N원 할인(100원 단위 입력)(신규)
		params.put("multiplePurchaseDiscount", true);//Y, 복수구매할인여부, true : 사용함 false : 복수구매할인 사용 안 함 (신규)
		params.put("multiplePurchaseDiscountAmount", 0);//Y, 복수구매할인금액, 구매수량당 N원 할인(100원 단위 입력)(신규)
		params.put("multiplePurchaseDiscountQuantity", 0);//Y, 복수구매할인기준, "주문수량 N개 이상(단위설정)2 ,3, 4, 5 중 선택"(신규)
		params.put("startDate", "String");//Y, 적용시작일자, YYYY-MM-DD(신규)
		params.put("endDate", "String");//Y, 적용완료일자, YYYY-MM-DD(신규)
		
		String body = mapToJson(params);
		
		return body;
	}
	*/
	//4.34 판매자지급 스마일캐시 등록 및 수정
	//[POST] https://sa.esmplus.com/item/v1/goods/{goodsNo}/customer-benefit/cashback (등록)
	//[PUT] https://sa.esmplus.com/item/v1/goods/{goodsNo}/customer-benefit/cashback (수정)
	/*
	public String createSellerSmileCashBody() {
		Map<String, Object> params = new HashMap<>();
		
		params.put("gmarketRatio", 0);//Y, G마켓 적립률, 적립률은 0.5%~50%까지, 0.1%단위로 입력 가능(신규)
		params.put("auctionRatio", 0);//Y, 옥션 적립률, 적립률은 0.5%~50%까지, 0.1%단위로 입력 가능(신규)
		
		String body = mapToJson(params);
		
		return body;
	}
	*/
	
	public Map<String, Object> createGoodsSellingOrderBody(ParamMap requestMap) {
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> isSell = new HashMap<>();
		Map<String, Object> price = new HashMap<>();
		Map<String, Object> sellingPeriod = new HashMap<>();
		Map<String, Object> stock = new HashMap<>();
		Map<String, Object> itemBasicInfo = new HashMap<>();
		boolean iacSaleFlag = true;
		
		HashMap<String, Object> subMap = (HashMap<String, Object>) requestMap.get("map");
		
		int iacCnt = Integer.parseInt(subMap.get("IAC_CNT").toString());
		
		if(iacCnt == 0){
			iacSaleFlag = false;
		}
		
		isSell.put("gmkt", true);       // G마켓판매여부
		isSell.put("iac", iacSaleFlag); // 옥션판매여부
		params.put("isSell", isSell);
		
		price.put("gmkt", subMap.get("SALE_PRICE"));// G마켓판매가격
		price.put("iac", subMap.get("SALE_PRICE")); // 옥션판매가격
		
		sellingPeriod.put("gmkt", 365);// G마켓판매기간, "G마켓 판매기간 입력  Value : 15,30,60,90,365	입력 시, 호출시점 기준으로 +Dday 세팅되는 구조"
		sellingPeriod.put("iac", 365); // 옥션판매기간, "옥션 판매기간 입력  Value : 15,30,60,90,365	입력 시, 호출시점 기준으로 +Dday 세팅되는 구조"
		
		int orderAbleQty = Integer.parseInt(subMap.get("TRANS_ORDER_ABLE_QTY").toString());
		if(orderAbleQty > 4999){
			orderAbleQty = 4999;
		}else if (orderAbleQty == 0){
			orderAbleQty = 1;
		}
		
		stock.put("gmkt", orderAbleQty); // G마켓재고
		stock.put("iac", orderAbleQty);  // 옥션재고
		
		itemBasicInfo.put("Price", price);
		itemBasicInfo.put("SellingPeriod", sellingPeriod);
		itemBasicInfo.put("Stock", stock);
		
		params.put("ItemBasicInfo", itemBasicInfo);
		
		return params;	
	}
	
	//[POST] https://sa.esmplus.com/item/v1/goods/{goodsNo}/g9
	public Map<String, Object> createG9DisplayBody(ParamMap requestMap) {
		Map<String, Object> params = new HashMap<>();
		HashMap<String, Object> subMap = (HashMap<String, Object>) requestMap.get("map");
		
		params.put("isGmarketDisplay", "true");
		params.put("isG9Display", subMap.get("G9_DISPLAY_YN").toString());
		
		return params;
	}
	
	public Map<String, Object> createGoodsExpiryDate(ParamMap requestMap) {
		
		HashMap<String, Object> subMap = (HashMap<String, Object>) requestMap.get("map");		
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> isSell = new HashMap<>();
		
		/** ItemBasicInfo */
		Map<String, Object> itemBasicInfo = new HashMap<>();
		Map<String, Object> price = new HashMap<>();		
		Map<String, Object> sellingPeriod = new HashMap<>();
		
		Map<String, Object> stock = new HashMap<>();
		
		int orderAbleQty = Integer.parseInt(subMap.get("TRANS_ORDER_ABLE_QTY").toString());
		if(orderAbleQty > 4999){
			orderAbleQty = 4999;
		}else if (orderAbleQty == 0){
			orderAbleQty = 1;
		}
		
		if("PAE".equals(requestMap.getString("siteGb"))) {
			isSell.put("gmkt"	 , true);
			isSell.put("iac"		 , true);
		}else if("PAG".equals(requestMap.getString("siteGb")))  {
			isSell.put("gmkt"	, true);
			isSell.put("iac"		, false);
		}else {
			isSell.put("gmkt"	, false);
			isSell.put("iac"		, true);
		}
		/*
		if(subMap.get("GMKT_YN").toString().equals("Y") && subMap.get("AUCT_YN").toString().equals("Y")){
			isSell.put("gmkt", false);
			isSell.put("iac", false);
		}else if(subMap.get("GMKT_YN").toString().equals("Y") && subMap.get("AUCT_YN").toString().equals("N")){
			isSell.put("gmkt", false);
			isSell.put("iac", false);
		}else if(subMap.get("GMKT_YN").toString().equals("N") && subMap.get("AUCT_YN").toString().equals("Y")){
			isSell.put("gmkt", false);
			isSell.put("iac", false);
		}
		*/
		price.put("gmkt", subMap.get("SALE_PRICE"));//Y, G마켓판매가격
		sellingPeriod.put("gmkt", 365);//Y, G마켓판매기간, "G마켓 판매기간 입력  Value : 15,30,60,90,365	입력 시, 호출시점 기준으로 +Dday 세팅되는 구조"
		stock.put("gmkt", orderAbleQty);//Y, G마켓재고
		
		price.put("iac", subMap.get("SALE_PRICE"));//Y, 옥션판매가격
		sellingPeriod.put("iac", 365);//Y, 옥션판매기간, "옥션 판매기간 입력  Value : 15,30,60,90,365	입력 시, 호출시점 기준으로 +Dday 세팅되는 구조"
		stock.put("iac", orderAbleQty);//Y, 옥션재고
		
		itemBasicInfo.put("Price", price);
		itemBasicInfo.put("SellingPeriod", sellingPeriod);
		itemBasicInfo.put("Stock", stock);
		
		params.put("isSell", isSell);
		params.put("ItemBasicInfo", itemBasicInfo);
		
		return params;
	}
}
