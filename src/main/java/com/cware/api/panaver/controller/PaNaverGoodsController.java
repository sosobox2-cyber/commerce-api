package com.cware.api.panaver.controller;

import io.swagger.annotations.ApiOperation;

import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.panaver.product.type.BagSummaryType;
import com.cware.api.panaver.product.type.BooksSummaryType;
import com.cware.api.panaver.product.type.CarArticlesSummaryType;
import com.cware.api.panaver.product.type.CertificationListType;
import com.cware.api.panaver.product.type.CertificationType;
import com.cware.api.panaver.product.type.ChangeProductSaleStatusRequestType;
import com.cware.api.panaver.product.type.ChangeProductSaleStatusResponseType;
import com.cware.api.panaver.product.type.CombinationOptionItemListType;
import com.cware.api.panaver.product.type.CombinationOptionItemType;
import com.cware.api.panaver.product.type.CombinationOptionNamesType;
import com.cware.api.panaver.product.type.CombinationOptionType;
import com.cware.api.panaver.product.type.CosmeticSummaryType;
import com.cware.api.panaver.product.type.DeliveryType;
import com.cware.api.panaver.product.type.DietFoodSummaryType;
import com.cware.api.panaver.product.type.DigitalContentsSummaryType;
import com.cware.api.panaver.product.type.EtcServiceSummaryType;
import com.cware.api.panaver.product.type.EtcSummaryType;
import com.cware.api.panaver.product.type.FashionItemsSummaryType;
import com.cware.api.panaver.product.type.FoodSummaryType;
import com.cware.api.panaver.product.type.FurnitureSummaryType;
import com.cware.api.panaver.product.type.GeneralFoodSummaryType;
import com.cware.api.panaver.product.type.GetModelListRequestType2;
import com.cware.api.panaver.product.type.GetModelListResponseType;
import com.cware.api.panaver.product.type.GetOptionRequestType;
import com.cware.api.panaver.product.type.GetOptionResponseType;
import com.cware.api.panaver.product.type.HomeAppliancesSummaryType;
import com.cware.api.panaver.product.type.ImageAppliancesSummaryType;
import com.cware.api.panaver.product.type.ImageService;
import com.cware.api.panaver.product.type.ImageServicePortType;
import com.cware.api.panaver.product.type.ImageType;
import com.cware.api.panaver.product.type.ImageURLListType;
import com.cware.api.panaver.product.type.JewellerySummaryType;
import com.cware.api.panaver.product.type.KidsSummaryType;
import com.cware.api.panaver.product.type.KitchenUtensilsSummaryType;
import com.cware.api.panaver.product.type.ManageOptionRequestType;
import com.cware.api.panaver.product.type.ManageOptionResponseType;
import com.cware.api.panaver.product.type.ManageProductRequestType;
import com.cware.api.panaver.product.type.ManageProductResponseType;
import com.cware.api.panaver.product.type.MedicalAppliancesSummaryType;
import com.cware.api.panaver.product.type.MicroElectronicsSummaryType;
import com.cware.api.panaver.product.type.ModelType;
import com.cware.api.panaver.product.type.MusicalInstrumentSummaryType;
import com.cware.api.panaver.product.type.NavigationSummaryType;
import com.cware.api.panaver.product.type.OfficeAppliancesSummaryType;
import com.cware.api.panaver.product.type.OpticsAppliancesSummaryType;
import com.cware.api.panaver.product.type.OptionType;
import com.cware.api.panaver.product.type.OptionalListType;
import com.cware.api.panaver.product.type.OriginAreaType;
import com.cware.api.panaver.product.type.PaCertificationVO;
import com.cware.api.panaver.product.type.PaDeliveryVO;
import com.cware.api.panaver.product.type.PaNaverGoodsVO;
import com.cware.api.panaver.product.type.ProductService;
import com.cware.api.panaver.product.type.ProductServicePortType;
import com.cware.api.panaver.product.type.ProductSummaryType;
import com.cware.api.panaver.product.type.ProductType;
import com.cware.api.panaver.product.type.RentalEtcSummaryType;
import com.cware.api.panaver.product.type.SaleStatusType;
import com.cware.api.panaver.product.type.SeasonAppliancesSummaryType;
import com.cware.api.panaver.product.type.SellerDiscountType;
import com.cware.api.panaver.product.type.ShoesSummaryType;
import com.cware.api.panaver.product.type.SleepingGearSummaryType;
import com.cware.api.panaver.product.type.SportsEquipmentSummaryType;
import com.cware.api.panaver.product.type.URLType;
import com.cware.api.panaver.product.type.UploadImageRequestType;
import com.cware.api.panaver.product.type.UploadImageResponseType;
import com.cware.api.panaver.product.type.WearSummaryType;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaNaverGoodsImageVO;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaNaverGoodsImage;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.panaver.goods.service.PaNaverGoodsService;

@Controller("com.cware.api.panaver.PaNaverGoodsController")
@RequestMapping(value="/panaver/goods")
public class PaNaverGoodsController extends AbstractController {
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "panaver.goods.paNaverGoodsService")
	private PaNaverGoodsService paNaverGoodsService;
	
	static { //ComUtil 실행
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * 모델 정보 (모델코드, 브랜드명, 제조사명)
	 * <p/>
	 * 상품에 입력할 모델정보를 생성한다.
	 * 모델 코드는 GetModelList, 브랜드명은 GetBrandList, 제조사명은 GetManufacturerList API로 조회할 수 있다.
	 * 
	 * @return 모델 정보
	 */
	private ModelType createModelInfo(PaNaverGoodsVO paNaverGoodsVO, ProductSummaryType productSummaryType, String modelInputYN) {
		ModelType model = new ModelType();
		
		if(paNaverGoodsVO.getModelNo() != null){
			model.setId(ComUtil.objToLong(paNaverGoodsVO.getModelNo())); // 6154900444L 모델 ID. GetModelList API로 조회한 ModelReturnType의 ModelId를 입력			
		}
		
		//model.setId(ComUtil.objToLong(1000001)); // 6154900444L 모델 ID. GetModelList API로 조회한 ModelReturnType의 ModelId를 입력
		model.setBrandName(paNaverGoodsVO.getBrandName()); // 브랜드명. GetBrandList로 샵N DB에 등록되어 있는 브랜드명을 조회할 수 있다.
		// 모델ID와 함께 입력하는 경우 ModelReturnType의 BrandName을 입력하면 된다.
		model.setManufacturerName(paNaverGoodsVO.getMakecoName()); // 제조사명. GetManufacturerList로 샵N DB에 등록되어 있는 제조사명을 조회할 수 있다.
		// 모델ID와 함께 입력하는 경우 ModelReturnType의 ManufacturerName을 입력하면 된다.
		
		if(productSummaryType!=null && modelInputYN.equals("1")){
			if(productSummaryType.getFurniture()!=null) 			model.setModelName(productSummaryType.getFurniture().getItemName());
			else if(productSummaryType.getImageAppliances()!=null) 	model.setModelName(productSummaryType.getImageAppliances().getItemName());
			else if(productSummaryType.getHomeAppliances()!=null) 	model.setModelName(productSummaryType.getHomeAppliances().getItemName());
			else if(productSummaryType.getSeasonAppliances()!=null) model.setModelName(productSummaryType.getSeasonAppliances().getItemName());
			else if(productSummaryType.getOfficeAppliances()!=null) model.setModelName(productSummaryType.getOfficeAppliances().getItemName());
			else if(productSummaryType.getOpticsAppliances()!=null) model.setModelName(productSummaryType.getOpticsAppliances().getItemName());
			else if(productSummaryType.getMicroElectronics()!=null) model.setModelName(productSummaryType.getMicroElectronics().getItemName());
			else if(productSummaryType.getNavigation()!=null) 		model.setModelName(productSummaryType.getNavigation().getItemName());
			else if(productSummaryType.getCarArticles()!=null) 		model.setModelName(productSummaryType.getCarArticles().getItemName());
			else if(productSummaryType.getMedicalAppliances()!=null)model.setModelName(productSummaryType.getMedicalAppliances().getItemName());
			else if(productSummaryType.getKitchenUtensils()!=null) 	model.setModelName(productSummaryType.getKitchenUtensils().getItemName());
			else if(productSummaryType.getKids()!=null) 			model.setModelName(productSummaryType.getKids().getItemName());
			else if(productSummaryType.getMusicalInstrument()!=null)model.setModelName(productSummaryType.getMusicalInstrument().getItemName());
			else if(productSummaryType.getSportsEquipment()!=null) 	model.setModelName(productSummaryType.getSportsEquipment().getItemName());
			else if(productSummaryType.getRentalEtc()!=null) 		model.setModelName(productSummaryType.getRentalEtc().getItemName());
			else if(productSummaryType.getEtc()!=null) 				model.setModelName(productSummaryType.getEtc().getItemName());	
			else 													model.setModelName("상품상세참조");
		}   
		
		return model;
	}

	/**
	 * 원산지 정보 
	 * <p/>
	 * 상품에 입력할 원산지 정보를 생성한다.
	 * 원산지 코드는 GetOriginAreaList, GetAllOriginAreaList, GetSubOriginAreaList API를 사용해서 조회할 수 있다.
	 *
	 * @return 원산지 정보
	 */
	private OriginAreaType createOriginAreaInfo(PaNaverGoodsVO paNaverGoodsVO) {
		OriginAreaType originArea = new OriginAreaType();
		originArea.setCode(paNaverGoodsVO.getNaverOriginCode()); // 원산지 코드
		originArea.setPlural("N"); // 복수 원산지 여부
		
		if(!paNaverGoodsVO.getNaverOriginCode().startsWith("00")){
			 originArea.setImporter(paNaverGoodsVO.getMakecoName()); // 수입사. 원산지 코드가 수입산(ex: 미국, 중국 등)인 경우 입력한다.
		}
		
		// originArea.setContent("원산지 직접입력"); // 원산지 직접입력. 원산지코드가 04 (직접입력)인 경우에만 입력한다.
		return originArea;
	}
	
	/**
	 * 이미지 정보 생성
	 * <p/>
	 * 상품 이미지 정보를 생성한다.
	 * 이미지 URL은 이미지 업로드 API(UploadImage)로 업로드하고 반환받은 이미지 URL을 입력해야 한다.
	 *
	 * @return 이미지 정보
	 */
	private ImageType createImageInfo(PaNaverGoodsImage paNaverGoodsImage) {
		int index=0;
		
		ImageType image = new ImageType();
		image.setRepresentative(new URLType());
		image.getRepresentative().setURL(paNaverGoodsImage.getImageNaverP());
		
		image.setOptionalList(new OptionalListType());
		
		if(paNaverGoodsImage.getImageNaverAp()!=null){
			image.getOptionalList().getOptional().add(index, new URLType());
			image.getOptionalList().getOptional().get(index).setURL(paNaverGoodsImage.getImageNaverAp());
			index++;
		}
		if(paNaverGoodsImage.getImageNaverBp()!=null){
			image.getOptionalList().getOptional().add(index, new URLType());
			image.getOptionalList().getOptional().get(index).setURL(paNaverGoodsImage.getImageNaverBp());
			index++;
		}
		if(paNaverGoodsImage.getImageNaverCp()!=null){
			image.getOptionalList().getOptional().add(index, new URLType());
			image.getOptionalList().getOptional().get(index).setURL(paNaverGoodsImage.getImageNaverCp());
			index++;
		}
		if(paNaverGoodsImage.getImageNaverDp()!=null){
			image.getOptionalList().getOptional().add(index, new URLType());
			image.getOptionalList().getOptional().get(index).setURL(paNaverGoodsImage.getImageNaverDp());
			index++;
		}
		
		return image;
	}

	/**
	 * @return 판매자 즉시 할인 정보
	 */
	private SellerDiscountType createSellerDiscountInfo(PaNaverGoodsVO paNaverGoodsVO, List<PaPromoTarget> paPromoTargetList) throws Exception{
		SellerDiscountType sellerDiscount = new SellerDiscountType();
		int couponPrice = 0;
		
		//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
		for(PaPromoTarget promoTarget : paPromoTargetList) {
			if(promoTarget.getProcGb() != null){
				if(!promoTarget.getProcGb().equals("D")){
					couponPrice += (int) promoTarget.getDoCost(); //할인쿠폰(자동적용쿠폰 + 제휴OUT) 할인금액
				}
			}
		}
		//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
		
		sellerDiscount.setAmount(paNaverGoodsVO.getDcAmt()+paNaverGoodsVO.getLumpSumDcAmt()+couponPrice+"");
		sellerDiscount.setMobileAmount(paNaverGoodsVO.getDcAmt()+paNaverGoodsVO.getLumpSumDcAmt()+couponPrice+"");
		
		return sellerDiscount;
	}
	
	private List<PaPromoTarget> paPromoTargetInfoSetting(ParamMap paramMap) throws Exception{
		List<HashMap<String,Object>> requestPriceMapList = paNaverGoodsService.selectPaPromoTarget(paramMap);
		if(requestPriceMapList.size()==0){
			requestPriceMapList = new ArrayList<>();
			
			HashMap<String,Object> requestPriceMap = new HashMap<>();
			requestPriceMap.put("PROMO_NO","000000000000");
			requestPriceMap.put("SEQ","0000");
			requestPriceMap.put("DO_COST",0);
			requestPriceMap.put("DO_OWN_COST",0);
			requestPriceMap.put("DO_ENTP_COST",0);
			requestPriceMap.put("PROC_GB",'D');			
			
			requestPriceMapList.add(requestPriceMap);
		}
		
		PaPromoTarget paPromoTarget;
    	List<PaPromoTarget> paPromoTargetList = new ArrayList<>();
    	for(HashMap<String,Object> priceMap : requestPriceMapList){
    		paPromoTarget = new PaPromoTarget();
    		paPromoTarget.setGoodsCode(paramMap.getString("goodsCode"));
    		paPromoTarget.setPaCode(paramMap.getString("paCode"));
    		paPromoTarget.setPromoNo(priceMap.get("PROMO_NO").toString());
    		paPromoTarget.setSeq(priceMap.get("SEQ").toString());
    		paPromoTarget.setProcGb(priceMap.get("PROC_GB").toString());
    		paPromoTarget.setDoCost(Double.parseDouble(priceMap.get("DO_COST").toString()));
    		paPromoTarget.setDoOwnCost(Double.parseDouble(priceMap.get("DO_OWN_COST").toString()));
    		paPromoTarget.setDoEntpCost(Double.parseDouble(priceMap.get("DO_ENTP_COST").toString()));
    		if(priceMap.get("TRANS_DATE") != null){
    			paPromoTarget.setTransDate(DateUtil.toTimestamp(priceMap.get("TRANS_DATE").toString(),"yyyy/MM/dd HH:mm:ss"));
    		}
    		
    		paPromoTargetList.add(paPromoTarget);
    	}
		
		return paPromoTargetList;
	}
	
	/**
	 * 인증정보
	 * <p/>
	 * 상품에 입력할 인증정보를 생성한다.
	 * GetCategoryInfo API를 호출하면 해당 카테고리에 입력할 수 있는 인증정보코드를 조회할 수 있습니다.
	 *
	 * @return 인증정보 목록
	 */
	private static CertificationListType createCertificationListInfo(List<PaCertificationVO> paCertificationList) {
		CertificationType certification = null;
		CertificationListType certificationList = new CertificationListType();
		
		for(PaCertificationVO list : paCertificationList){
			certification = new CertificationType();
			certification.setId(Long.parseLong(list.getCertiCode())); // 인증유형 ID. GetCategoryInfo에서 조회하는 CertificationCategoryListType의 code 값을 입력
			certification.setKindType(list.getExceptCode()); // 인증 정보 종류
			certification.setMark("Y"); // 인증 마크 사용 여부
			
			if(list.getCertiAgencyYn().equals("1"))
				certification.setName(list.getCertiAgency()); // 인증기관
			if(list.getCertiNoYn().equals("1"))
				certification.setNumber(list.getCertiNo()); // 인증번호
			if(list.getCertiCompanyYn().equals("1"))
				certification.setCompanyName(list.getCertiCompany()); // 인증상호
			
			certificationList.getCertification().add(certification);
			
		}
		return certificationList;
	}
	
	/**
	 * 배송 정보
	 * <p/>
	 * 상품 배송정보를 입력한다.
	 * 배송 없는 상품은 DeliveryType 값을 null로 입력해야 한다.
	 *
	 * @return 배송 정보
	 */
	private static DeliveryType createDeliveryInfo(PaNaverGoodsVO paNaverGoodsVO, PaDeliveryVO paDeliveryVO) {
		DeliveryType delivery = new DeliveryType();
					
		delivery.setType("1"); // 배송 방법 유형 코드 (택배, 소포, 등기 / 직접배송)
		delivery.setBundleGroupAvailable("N"); // 묶음 배송 가능 여부		
		
		// 배송비 유형 코드 (무료, 조건부 무료, 유료, 수량별 부과 - 반복구간, 수량별 부과 - 구간 직접 설정)
		if(paNaverGoodsVO.getShipCostcode().contains("FR"))			delivery.setFeeType("1"); // 무료배송
		else if(paNaverGoodsVO.getShipCostcode().contains("CN"))	delivery.setFeeType("2"); // 조건부
		else if(paNaverGoodsVO.getShipCostcode().contains("PL"))	delivery.setFeeType("2"); // 조건부
		else if(paNaverGoodsVO.getShipCostcode().contains("ID"))	delivery.setFeeType("3"); // 상품별
		else if(paNaverGoodsVO.getShipCostcode().contains("QN"))	delivery.setFeeType("4"); // 수량단위
		
		delivery.setBaseFee(paNaverGoodsVO.getOrdCost()); // 기본 배송비

		// 무료 조건 금액. '조건부 무료'일 경우 입력한다.
		if(paNaverGoodsVO.getShipCostcode().contains("CN") || paNaverGoodsVO.getShipCostcode().contains("PL"))	
			delivery.setFreeConditionalAmount(paNaverGoodsVO.getShipCostBaseAmt());
		// 반복 수량. '수량별 부과 - 반복구간'일 경우 입력한다.
		else if(paNaverGoodsVO.getShipCostcode().contains("QN"))	
			delivery.setRepeatQuantity(paNaverGoodsVO.getShipCostBaseAmt());
		
		delivery.setPayType("2"); // 배송비 결제 방식 타입 코드. (착불, 선결제, 착불 또는 선결제)
		
		// 지역별 추가 배송 권역 (2권역 - 내륙/제주 및 도서산간, 3권역 - 내륙/제주 외 도서 산간)
		int jejuCost = paNaverGoodsVO.getJejuCost()-paNaverGoodsVO.getOrdCost();
		int islandCost = paNaverGoodsVO.getIslandCost()-paNaverGoodsVO.getOrdCost();
		if(jejuCost + islandCost > 0){
			delivery.setAreaType("3"); // 지역별 추가 배송 권역 (2권역 - 내륙/제주 및 도서산간, 3권역 - 내륙/제주 외 도서 산간)
			delivery.setArea2ExtraFee(jejuCost==0?100:jejuCost); // 2권역 배송비
			delivery.setArea3ExtraFee(islandCost==0?100:islandCost); // 3권역 배송비			
		}
		
		delivery.setReturnDeliveryCompanyPriority("0"); // 반품/교환 택배사. GetReturnsCompanyList API로 택배사 코드를 조회하여 입력한다. (0.우체국택배)
		
		 // 반품 배송비
		if(paNaverGoodsVO.getOrdCost()==0)
			delivery.setReturnFee(paNaverGoodsVO.getReturnCost()/2);
		else
			delivery.setReturnFee(paNaverGoodsVO.getReturnCost());	
			
		delivery.setExchangeFee(paNaverGoodsVO.getChangeCost()); // 교환 배송비
		
		delivery.setReturnAddressId(Long.parseLong(paDeliveryVO.getReturnAddressId())); 		// 반품/교환지 주소 코드. GetAddressBookList API로 주소 코드를 조회하여 입력한다.
		if(paDeliveryVO.getShippingAddressId() != null)
			delivery.setShippingAddressId(Long.parseLong(paDeliveryVO.getShippingAddressId())); // 출고지 주소 코드. GetAddressBookList API로 주소 코드를 조회하여 입력한다.
		else
			delivery.setShippingAddressId(Long.parseLong(paDeliveryVO.getReturnAddressId()));	// 츨고지가 없으면 회수지 입력
		
		delivery.setDeliveryCompany(paDeliveryVO.getDeliveryCompany());
				
		return delivery;
	}

	/**
	 * @return 상품 요약 정보 , 상품 등록 시 필수
	 */
	private ProductSummaryType createProductSummaryInfo(ParamMap paramMap) {
		ProductSummaryType productSummary = new ProductSummaryType();;
		String offerType="";

		try {
			offerType = paNaverGoodsService.selectPaOfferType(paramMap);
			offerType = offerType.substring(2, 4);

			switch (offerType) {
			case "01": //의류
				WearSummaryType wearSummaryType = paNaverGoodsService.selectWearSummary(paramMap);
				productSummary.setWear(wearSummaryType);	
				break;
			case "02": //신발
				ShoesSummaryType shoesSummaryType = paNaverGoodsService.selectShoesSummary(paramMap);
				productSummary.setShoes(shoesSummaryType);		
				break;
			case "03": //가방
				BagSummaryType bagSummaryType = paNaverGoodsService.selectBagSummary(paramMap);
				productSummary.setBag(bagSummaryType);	
				break;
			case "04": //패션잡화
				FashionItemsSummaryType fashionItemsSummaryType = paNaverGoodsService.selectFashionItemsSummary(paramMap);
				productSummary.setFashionItems(fashionItemsSummaryType);			
				break;
			case "05": //침구류/커튼
				SleepingGearSummaryType sleepingGearSummaryType = paNaverGoodsService.selectSleepingGearSummary(paramMap);
				productSummary.setSleepingGear(sleepingGearSummaryType);		
				break;
			case "06": //가구
				FurnitureSummaryType furnitureSummaryType = paNaverGoodsService.selectFurnitureSummary(paramMap);
				productSummary.setFurniture(furnitureSummaryType);		
				break;
			case "07": //영상가전
				ImageAppliancesSummaryType imageAppliancesSummaryType = paNaverGoodsService.selectImageAppliancesSummary(paramMap);
				productSummary.setImageAppliances(imageAppliancesSummaryType);		
				break;
			case "08": //가정용 전기제품
				HomeAppliancesSummaryType homeAppliancesSummaryType = paNaverGoodsService.selectHomeAppliancesSummary(paramMap);
				productSummary.setHomeAppliances(homeAppliancesSummaryType);		
				break;
			case "09": //계절가전
				SeasonAppliancesSummaryType seasonAppliancesSummaryType = paNaverGoodsService.selectSeasonAppliancesSummary(paramMap);
				productSummary.setSeasonAppliances(seasonAppliancesSummaryType);		
				break;
			case "10": //사무용기기
				OfficeAppliancesSummaryType officeAppliancesSummaryType = paNaverGoodsService.selectOfficeAppliancesSummary(paramMap);
				productSummary.setOfficeAppliances(officeAppliancesSummaryType);		
				break;
			case "11": //광학기기
				OpticsAppliancesSummaryType opticsAppliancesSummaryType = paNaverGoodsService.selectOpticsAppliancesSummary(paramMap);
				productSummary.setOpticsAppliances(opticsAppliancesSummaryType);		
				break;
			case "12": //소형전자
				MicroElectronicsSummaryType microElectronicsSummaryType = paNaverGoodsService.selectMicroElectronicsSummary(paramMap);
				productSummary.setMicroElectronics(microElectronicsSummaryType);		
				break;
			case "13": //후대폰
				break;
			case "14": //네비게이션
				NavigationSummaryType navigationSummaryType = paNaverGoodsService.selectNavigationSummary(paramMap);
				productSummary.setNavigation(navigationSummaryType);		
				break;
			case "15": //자동차용품
				CarArticlesSummaryType carArticlesSummaryType = paNaverGoodsService.selectCarArticlesSummary(paramMap);
				productSummary.setCarArticles(carArticlesSummaryType);		
				break;
			case "16": //의료기기
				MedicalAppliancesSummaryType medicalAppliancesSummaryType = paNaverGoodsService.selectMedicalAppliancesSummary(paramMap);
				productSummary.setMedicalAppliances(medicalAppliancesSummaryType);			
				break;
			case "17": //주방용품
				KitchenUtensilsSummaryType kitchenUtensilsSummaryType = paNaverGoodsService.selectKitchenUtensilsSummary(paramMap);
				if(kitchenUtensilsSummaryType.getSize().length()>200)
					kitchenUtensilsSummaryType.setSize((String) kitchenUtensilsSummaryType.getSize().subSequence(0, 200));
				else
					kitchenUtensilsSummaryType.getSize();
				productSummary.setKitchenUtensils(kitchenUtensilsSummaryType);		
				break;
			case "18": //화장품
				CosmeticSummaryType cosmeticSummaryType = paNaverGoodsService.selectCosmeticSummary(paramMap);
				productSummary.setCosmetic(cosmeticSummaryType);			
				break;
			case "19": //귀금속/보석/시계류
				JewellerySummaryType jewellerySummaryType = paNaverGoodsService.selectJewellerySummary(paramMap);
				productSummary.setJewellery(jewellerySummaryType);			
				break;
			case "20": //식품(농수산물)
				FoodSummaryType foodSummaryType = paNaverGoodsService.selectFoodSummary(paramMap);
				productSummary.setFood(foodSummaryType);		
				break;
			case "21": //가공식품
				GeneralFoodSummaryType generalFoodSummaryType = paNaverGoodsService.selectGeneralFoodSummary(paramMap);
				productSummary.setGeneralFood(generalFoodSummaryType);			
				break;
			case "22": //건강기능식품
				DietFoodSummaryType dietFoodSummaryType = paNaverGoodsService.selectDietFoodSummary(paramMap);
				productSummary.setDietFood(dietFoodSummaryType);			
				break;
			case "23": //영유가용품	
				KidsSummaryType kidsSummaryType = paNaverGoodsService.selectKidsSummary(paramMap);
				productSummary.setKids(kidsSummaryType);			
				break;
			case "24": //악기	
				MusicalInstrumentSummaryType musicalInstrumentSummaryType = paNaverGoodsService.selectMusicalInstrumentSummary(paramMap);
				productSummary.setMusicalInstrument(musicalInstrumentSummaryType);			
				break;
			case "25": //스포츠용품	
				SportsEquipmentSummaryType sportsEquipmentSummaryType = paNaverGoodsService.selectSportsEquipmentSummary(paramMap);
				productSummary.setSportsEquipment(sportsEquipmentSummaryType);			
				break;
			case "26": //서적	
				BooksSummaryType booksSummaryType = paNaverGoodsService.selectBooksSummary(paramMap);
				productSummary.setBooks(booksSummaryType);				
				break;
			case "27": //호텔/팬션 예약
				break;
			case "28": //여행패키지
				break;
			case "29": //항공권				
				break;
			case "30": //자동차 대여 서비스
				break;
			case "31": //물품대여 서비스
				RentalEtcSummaryType rentalEtcSummaryType1 = paNaverGoodsService.selectRentalEtcSummary(paramMap);
				productSummary.setRentalEtc(rentalEtcSummaryType1);		
				break;
			case "32": //물품대여 서비스
				RentalEtcSummaryType rentalEtcSummaryType2 = paNaverGoodsService.selectRentalEtcSummary(paramMap);
				productSummary.setRentalEtc(rentalEtcSummaryType2);	
				break;
			case "33": //디지털 콘텐츠(음원/게임/인터넷강의 등)
				DigitalContentsSummaryType digitalContentsSummaryType = paNaverGoodsService.selectDigitalContentsSummary(paramMap);
				productSummary.setDigitalContents(digitalContentsSummaryType);		
				break;
			case "34": //상품권/쿠폰		
				break;
			case "35": //모바일쿠폰			
				break;
			case "36": //영화/공연	
				break;
			case "37": //기타 용역
				EtcServiceSummaryType etcServiceSummaryType = paNaverGoodsService.selectEtcServiceSummary(paramMap);
				productSummary.setEtcService(etcServiceSummaryType);			
				break;
			case "38": //기타 재화
				EtcSummaryType etcSummaryType = paNaverGoodsService.selectEtcSummary(paramMap);
				productSummary.setEtc(etcSummaryType);		
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productSummary;
	}
	
	//uploadImage API
	private String uploadImage(String url) throws Exception {
		UploadImageRequestType UploadImageRequest = new UploadImageRequestType();
		UploadImageRequest.setRequestID(UUID.randomUUID().toString());
		UploadImageRequest.setVersion(ConfigUtil.getString("PANAVER_GOODS_VERSION"));
		UploadImageRequest.setSellerId(ConfigUtil.getString("PANAVER_MALL_ID"));
		
		ImageURLListType imageURLList = new ImageURLListType();
		imageURLList.getURL().add(url);
		
		UploadImageRequest.setImageURLList(imageURLList);
		UploadImageRequest.setAccessCredentials(ComUtil.paNaverCreateAccessCredentials("ImageService", "UploadImage"));
		
		ImageService imageService = new ImageService();
		ImageServicePortType port = imageService.getImageServiceSOAP12PortHttp();
		UploadImageResponseType response = port.uploadImage(UploadImageRequest);
		
		if(response.getResponseType().equals("SUCCESS")){
			return response.getImageList().getImage().get(0).getURL();
		}else{
			return "";
		}		
	}
	
	// 상품 setting
	private ProductType goodsSetting(PaNaverGoodsImageVO paNaverGoodsImage, PaNaverGoodsVO paNaverGoodsVO, ParamMap paramMap, String modelInputYN, List<PaCertificationVO> paCertificationList, PaDeliveryVO paDeliveryVO, List<PaPromoTarget> paPromoTargetList) throws Exception {
		String paGoodsName="";
		
		// 상품 객체 생성
		ProductType product = new ProductType();
		
		if(paNaverGoodsVO.getProductId()!=null)
			product.setProductId(Long.parseLong(paNaverGoodsVO.getProductId())); // 상품을 수정하는 경우에 해당 상품 번호를 입력해야 합니다.

		product.setSaleStartDate(paNaverGoodsVO.getSaleStartDate().toString()); // 판매시작일 (YYYY-MM-DD HH:mm)
		if(paNaverGoodsVO.getSaleEndDate()==null) product.setSaleEndDate("2999-12-31"); // 판매종료일
		else product.setSaleEndDate(paNaverGoodsVO.getSaleEndDate().toString());
		
		product.setSalePrice(paNaverGoodsVO.getSalePrice()); // 판매가(원)	
		
		// 단품(옵션) n개인 경우, 재고는 보내지 않는다.
		if((paNaverGoodsVO.getGoodsdtCnt()==1) || (paNaverGoodsVO.getProductId()==null && paNaverGoodsVO.getGoodsdtCnt()>1)) 
			product.setStockQuantity(paNaverGoodsVO.getTransOrderAbleQty()); // 재고수량			
		
		// 판매상태 (판매중, 판매중지)
		if(paNaverGoodsVO.getPaSaleGb().equals("20"))		product.setStatusType("SALE"); 
		else if(paNaverGoodsVO.getPaSaleGb().equals("30"))	product.setStatusType("SUSP");
		
		product.setSaleType("NEW"); // 상품판매유형 (신상품, 중고, 진열, 리퍼)

		//주문 제작 상품 여부 ( “Y” 또는 “N” ) 
		if(paNaverGoodsVO.getOrderMakeYN().equals("0")) 	product.setCustomMade("N");
		else if(paNaverGoodsVO.getOrderMakeYN().equals("1"))product.setCustomMade("Y"); 

		product.setProductSummary(createProductSummaryInfo(paramMap));// 상품 요약 정보 , 상품 등록 시 필수	
		product.setModel(createModelInfo(paNaverGoodsVO, product.getProductSummary(), modelInputYN)); //모델정보
		
		product.setCategoryId(paNaverGoodsVO.getLmsdKey()); // Leaf 카테러그 Id
		
		// 상품명 특수문자 처리
		paGoodsName = paNaverGoodsVO.getPaGoodsName().replace("*", "X").replace("?", ""); 
		// 상품명에 브랜드명 없을 시 추가
		if(!paNaverGoodsVO.getBrandName().equals("기타")){
			if(paGoodsName.startsWith(paNaverGoodsVO.getBrandName())){			
				paGoodsName = paGoodsName.replaceFirst(paNaverGoodsVO.getBrandName(), "[" + paNaverGoodsVO.getBrandName() + "]");
			}else if(!paGoodsName.startsWith("["+paNaverGoodsVO.getBrandName()+"]")){
				paGoodsName = "[" + paNaverGoodsVO.getBrandName() + "]" + paGoodsName;
			}	
		}		
		
		String shipCostCode = paNaverGoodsVO.getShipCostcode().substring(0,2);
		String collectYn = paNaverGoodsVO.getCollectYn();
		
		if ("1".equals(collectYn) /* && "FR".equals(shipCostCode) */) {
			paGoodsName = "(착불)" + paGoodsName;
		}
		
		product.setName(paGoodsName); // 상품명
		
		product.setSellerManagementCode(paNaverGoodsVO.getGoodsCode()); // 판매자가 관리하는 상품 코드
		
		if(paNaverGoodsVO.getExceptCode() != null){
// 		    네이버 필수인증정보 카테고리 상품의 경우 인증대상을 모두 예외로 세팅하여 반영하고 인증번호는 세팅되지 않도록 주석처리한다. 2022.10.25 LEEJY			
//			product.setCertificationList(createCertificationListInfo(paCertificationList)); // 인증정보
			
			if(paNaverGoodsVO.getExceptCode().equals("KC")){	// KC 인증대상
				product.setkCCertifiedProductExclusion("Y");
			}else if(paNaverGoodsVO.getExceptCode().equals("GRN")){ // 친환경 인증대상
				product.setGreenCertifiedProductExclusion("Y");
			}else if(paNaverGoodsVO.getExceptCode().equals("CHI")){	// 어린이제품 인증대상
				product.setChildCertifiedProductExclusion("Y");
			}
		}
		product.setOriginArea(createOriginAreaInfo(paNaverGoodsVO)); // 원산지 정보
		
		// 부가세 타입 코드
		if(paNaverGoodsVO.getTaxSmallYN().equals("1")) product.setTaxType("SMALL"); 
		else if(paNaverGoodsVO.getTaxYN().equals("1")) product.setTaxType("TAX"); 
		else if(paNaverGoodsVO.getTaxYN().equals("0")) product.setTaxType("DUTYFREE"); 
		
		// 미성년자 구매가능 여부
		if(paNaverGoodsVO.getAdultYN().equals("0")) 	product.setMinorPurchasable("Y"); 
		else if(paNaverGoodsVO.getAdultYN().equals("1"))product.setMinorPurchasable("N");
		
		product.setImage(createImageInfo(paNaverGoodsImage)); // 이미지 정보

		product.setDetailContent(paNaverGoodsVO.getDescribeExt()); // 상품 상세 내용.
		
		product.setAfterServiceTelephoneNumber(paNaverGoodsVO.getCsTel()); // A/S 전화번호
		product.setAfterServiceGuideContent(paNaverGoodsVO.getCsDetail()); // A/S 내용. HTML 입력 불가	

		product.setDelivery(createDeliveryInfo(paNaverGoodsVO, paDeliveryVO)); // 배송정보. 배송 없는 상품인 경우 아예 입력하지 않는다.
		product.setSellerDiscount(createSellerDiscountInfo(paNaverGoodsVO, paPromoTargetList)); // 판매자 즉시할인	
				
		product.setPurchaseReviewExposure("Y"); // 구매평 노출 여부
		product.setRegularCustomerExclusiveProduct("N"); // 단골 회원 전용 상품 여부
		product.setKnowledgeShoppingProductRegistration("Y"); // 네이버 쇼핑 등록
		
		// 1회 최대구매수량
		if(paNaverGoodsVO.getOrderMaxQty()>0) product.setMaxPurchaseQuantityPerOrder((long)paNaverGoodsVO.getOrderMaxQty());
		else product.setMaxPurchaseQuantityPerOrder(Long.parseLong("100"));
		
		// 1인 최대구매수량
		if(paNaverGoodsVO.getCustOrdQtyCheckYN().equals("1") && paNaverGoodsVO.getTermOrderQty() > 0 && paNaverGoodsVO.getTermOrderQty() > product.getMaxPurchaseQuantityPerOrder()) // 1인 구매시 최대구매수량은 1회 구매시 최대구매수량 이상으로 입력
			product.setMaxPurchaseQuantityPerId((long) paNaverGoodsVO.getTermOrderQty());
		else product.setMaxPurchaseQuantityPerId(product.getMaxPurchaseQuantityPerOrder());
		
		if(paNaverGoodsVO.getOrderMinQty()>=2)
			product.setMinPurchaseQuantity(paNaverGoodsVO.getOrderMinQty()); // 최소구매수량. 2개부터 설정 가능하다. 최소구매수량이 1개인 경우 아예 입력하지 않아야 한다.
		
		// 맞춤 제작 상품 여부
		if(paNaverGoodsVO.getOrderCreateYN().equals("0")) 	   product.setCustomProductYn("N"); 
		else if(paNaverGoodsVO.getOrderCreateYN().equals("1")) product.setCustomProductYn("Y");				

		product.setRegularCustomerExclusiveProduct("N"); // 구독회원 전용 상품 여부
		product.setKnowledgeShoppingProductRegistration("Y"); // 지식쇼핑 등록 여부. 지식쇼핑 광고주가 아닌 경우 N으로 저장된다.
		
//		product.setFreeInterest(createFreeInterestInfo()); // 무이자 할부
		
		return product;
	}
	
	/**
	 * 조합형 옵션 정보
	 * <p/>
	 * 조합형 옵션
	 * 단독형 옵션, 조합형 옵션, 직접 입력형 옵션 중 최소 한 개는 입력 단독형 옵션과 조합형 옵션은 함께 사용 불가
	 * @param paGoodsdtList 
	 *
	 * @return 옵션 정보
	 */
	private static CombinationOptionType createCombinationOption(List<PaGoodsdtMapping> paGoodsdtMappingList) {
		CombinationOptionItemType combinationOptionItem = null;
		CombinationOptionNamesType combinationOptionNames = new CombinationOptionNamesType();
		List<CombinationOptionItemType> list = new ArrayList<CombinationOptionItemType>();
//		String goodsdtInfo[] = null;

		combinationOptionNames.setName1("옵션"); //paGoodsdtMappingList.get(0).getGoodsdtInfoKind()
						
		for(int i=0; i<paGoodsdtMappingList.size(); i++){
			combinationOptionItem = new CombinationOptionItemType();
			combinationOptionItem.setValue1(paGoodsdtMappingList.get(i).getGoodsdtInfo().replace("*", "X").replace("?", "").replace("<", "(").replace(">", ")").replace("\"", "").replace("\\", ""));

			if(Long.parseLong(paGoodsdtMappingList.get(i).getTransOrderAbleQty()) >= Long.parseLong("200000"))
				combinationOptionItem.setQuantity(Long.parseLong(paGoodsdtMappingList.get(i).getTransOrderAbleQty())/100); // 재고 수량 
			else	
				combinationOptionItem.setQuantity(Long.parseLong(paGoodsdtMappingList.get(i).getTransOrderAbleQty())); // 재고 수량 
			
			combinationOptionItem.setSellerManagerCode(paGoodsdtMappingList.get(i).getGoodsdtCode()); // 판매자 관리 코드
			combinationOptionItem.setUsable("Y"); // 사용 여부 (“Y” 또는 “N”)
			
			list.add(combinationOptionItem);			
		}	
		
		CombinationOptionItemListType combinationOptionItemList = new CombinationOptionItemListType();
		combinationOptionItemList.setItem(list);
		
		CombinationOptionType combinationOption = new CombinationOptionType();
		combinationOption.setNames(combinationOptionNames); // 옵션명 목록
		combinationOption.setItemList(combinationOptionItemList); // 옵션 목록

		return combinationOption;
	}

	/**
	 * 상품 등록 시 사용할 이미지 업로드
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 이미지 등록", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/goods-image-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsImageInsert(HttpServletRequest request,
			@RequestParam(value="goodsCode", required=true) String goodsCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsImage paGoodsImage = null;		
		String imageURL = "";
		PaNaverGoodsImageVO paNaverGoodsImageVO = null;
		
		log.info("===== 상품이미지등록 API Start =====");
		//log.info("01.API 기본 정보 세팅");
		
		//connectionSetting 설정
		String prg_id = "IF_PANAVERAPI_01_005";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		paramMap.put("modCase", "INSERT");
		paramMap.put("code","200");
		paramMap.put("siteGb","PANAVER");
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paCode", "41");
		
		try{	
			//log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});

			//log.info("03.상품 이미지 조회 ");	
			paramMap.put("paGoodsCode", goodsCode);
			paramMap.put("paGroupCode", "04");
			paGoodsImage = paNaverGoodsService.selectPaGoodsImage(paramMap);
			
			if(paGoodsImage != null){				
				//TPANAVERGOODSIMAGE 데이터 저장
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String formattedDate = sdf.format(date);
				
				paNaverGoodsImageVO = new PaNaverGoodsImageVO();
				paNaverGoodsImageVO.setGoodsCode(paGoodsImage.getGoodsCode());
				paNaverGoodsImageVO.setLastSyncDate(paGoodsImage.getLastSyncDate());
				paNaverGoodsImageVO.setInsertId("SYSTEM");
				paNaverGoodsImageVO.setInsertDate(DateUtil.toTimestamp(formattedDate, "yyyy/MM/dd HH:mm:ss"));
				paNaverGoodsImageVO.setModifyId("SYSTEM");
				paNaverGoodsImageVO.setModifyDate(DateUtil.toTimestamp(formattedDate, "yyyy/MM/dd HH:mm:ss"));
				paNaverGoodsImageVO.setNaverImageYn(paGoodsImage.getNaverImageYn());
				
				imageURL = paGoodsImage.getImageUrl1()+paGoodsImage.getImageUrl2();	
				//log.info("04.네이버 상품이미지 API 실행");
				if(paGoodsImage.getImageP()!=null) 	paNaverGoodsImageVO.setImageNaverP(uploadImage(imageURL+paGoodsImage.getImageP()));
				if(paGoodsImage.getImageAp()!=null) paNaverGoodsImageVO.setImageNaverAp(uploadImage(imageURL+paGoodsImage.getImageAp()));
				if(paGoodsImage.getImageBp()!=null) paNaverGoodsImageVO.setImageNaverBp(uploadImage(imageURL+paGoodsImage.getImageBp()));
				if(paGoodsImage.getImageCp()!=null) paNaverGoodsImageVO.setImageNaverCp(uploadImage(imageURL+paGoodsImage.getImageCp()));
				if(paGoodsImage.getImageDp()!=null) paNaverGoodsImageVO.setImageNaverDp(uploadImage(imageURL+paGoodsImage.getImageDp()));
				
				//log.info("05.네이버 상품이미지 등록/수정");
				paNaverGoodsService.savePaNaverGoodsImageTx(paNaverGoodsImageVO);

				paramMap.put("message",getMessage("pa.save_paNaver_goods_image", new String[] {" 상품코드 : " +  paGoodsImage.getGoodsCode()}));
			}
		}catch(Exception e){
			if(duplicateCheck.equals("1"))
				paramMap.put("code","490");
			else 
				paramMap.put("code","500");			
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			} else paramMap.put("message","java.lang.NullPointerException");
			
			paramMap.put("resultType","ERROR");
			
			if(paGoodsImage.getGoodsCode()!=null)
				log.error("상품이미지등록 Error, 상품코드 : "+paGoodsImage.getGoodsCode());				
			
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				paramMap.put("message", paramMap.getString("message").length() > 999 ? paramMap.getString("message").substring(0, 999) : paramMap.getString("message"));
				paramMap.put("resultMessage",paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);

				if(paNaverGoodsImageVO.getImageNaverP()!=null){
					paramMap.put("resultType","SUCCESS");					
				}
				paramMap.put("productId",paramMap.getString("goodsCode"));
					
				savePaGoodsTransLog(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 상품이미지등록 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 상품 등록 및 수정 
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 등록 및 수정", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/legacy-goods-trans", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsTrans(HttpServletRequest request
			,@RequestParam(value="goodsCode", required=false) String goodsCode
			,@RequestParam(value="modCase", required=false, defaultValue="ALL") String modCase
			,@RequestParam(value="procId", required=false, defaultValue="PANAVER") String procId
			,@RequestParam(value="searchTermGb"	, required=false, defaultValue = "") String searchTermGb
			) throws Exception{
		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		String duplicateCheck = "";
		ProductType product = null;	
		PaNaverGoodsImageVO paNaverGoodsImage = null;	
		PaNaverGoodsVO paNaverGoods = null;
		List<PaNaverGoodsVO> paNaverGoodsList = null;
		List<PaCertificationVO> paCertificationList = null;
		List<PaPromoTarget> paPromoTargetList = null;
		PaDeliveryVO paDeliveryVO = null;
		String paCode="41";
		String groupCode="04";
		//PaGoodsTransLog paGoodsTransLog = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		String checkSaleGb = "";
		String respType="";
		int optionCnt=0;
		String modelInputYN="";
		
		log.info("===== 상품등록 API Start =====");
		//log.info("01.API 기본 정보 세팅");
		
		//connectionSetting 설정
		String prg_id = "IF_PANAVERAPI_01_001";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		paramMap.put("modCase", modCase);// ALL:전체, MODIFY:수정, INSERT:신규
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("code","200");
		paramMap.put("siteGb","PANAVER");
		paramMap.put("groupCode", groupCode);
		paramMap.put("paCode", paCode);
		
		try{			
			//log.info("02.API 중복실행검사");
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});				
			}
			//상품 수정 중 동기화배치가 돌아 상품정보 수정될 경우 다시 수정대상에 포함 하기위해 MODIFY_DATE와  수정대상조회 시작시간을 비교함
			String goodsListTime = systemService.getSysdatetimeToString(); 
			
			log.info("03.입점할 상품 리스트 조회");
			paNaverGoodsList = paNaverGoodsService.selectPaNaverGoodsInfoList(paramMap);			
			if(paNaverGoodsList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list", new String[] {" 상품코드 : " + goodsCode}));
				paramMap.put("resultMessage",paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}			
			for(int i=0; i<paNaverGoodsList.size(); i++){
				try{
					log.info("상품코드 : "+paNaverGoodsList.get(i).getGoodsCode());
					
					log.info("04.네이버 상품이미지 조회");
					paramMap.put("goodsCode", paNaverGoodsList.get(i).getGoodsCode());
					paNaverGoodsImage = paNaverGoodsService.selectPaNaverGoodsImage(paramMap); 
					
					log.info("05.네이버 상품이미지 등록");
					if(paNaverGoodsImage==null) {
						goodsImageInsert(request, paramMap.getString("goodsCode"));
						paNaverGoodsImage = paNaverGoodsService.selectPaNaverGoodsImage(paramMap);				
					}

					String goodsCom = "";
					
					goodsCom = (!ComUtil.NVL(paNaverGoodsList.get(i).getGoodsCom()).equals("")) ? ("<div style=\"line-height: 2.0em; font-family: 'NanumBarunGothic'; font-size: 19px;\"><div><h4>&middot;&nbsp;상품구성<h4>" + paNaverGoodsList.get(i).getGoodsCom() + "</div></div>") : "";
					
					if("".equals(paNaverGoodsList.get(i).getCollectImage()) || paNaverGoodsList.get(i).getCollectImage() == null) {
						paNaverGoodsList.get(i).setDescribeExt("<div align='center'><img alt='' src='" + paNaverGoodsList.get(i).getTopImage() + "' /><br /><br /><br />"	//상단 이미지 
					  + goodsCom	//상품 구성
					  + paNaverGoodsList.get(i).getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + paNaverGoodsList.get(i).getBottomImage() + "' /></div>"); //기술서 + 하단 이미지				
					}else {
						paNaverGoodsList.get(i).setDescribeExt("<div align='center'><img alt='' src='" + paNaverGoodsList.get(i).getTopImage() + "' /><br /><br /><br /><img alt='' src='" + paNaverGoodsList.get(i).getCollectImage() + "' /><br /><br /><br />"  //상단 이미지 + 착불 이미지
					  + goodsCom	//상품 구성
					  + paNaverGoodsList.get(i).getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + paNaverGoodsList.get(i).getBottomImage() + "' /></div>"); //기술서 + 하단 이미지
					}
					
					if(!ComUtil.NVL(paNaverGoodsList.get(i).getNoticeExt()).equals("")) {
						paNaverGoodsList.get(i).setDescribeExt(paNaverGoodsList.get(i).getNoticeExt() + paNaverGoodsList.get(i).getDescribeExt());
					}

					//log.info("07.모델정보 - 모델명 추가 여부(결과: 0 또는 1)");
					modelInputYN = paNaverGoodsService.selectModelInputYN(paramMap);

					//log.info("08.안전인증 조회");
					paCertificationList = paNaverGoodsService.selectPaCertificationList(paramMap);

					//log.info("09.출고지/회수지 조회");
					paDeliveryVO = paNaverGoodsService.selectPaDelivery(paramMap);
					
					//프로모션 조회
					paPromoTargetList = paPromoTargetInfoSetting(paramMap);
					
					log.info("10.데이터 세팅");
					product = goodsSetting(paNaverGoodsImage, paNaverGoodsList.get(i), paramMap, modelInputYN, paCertificationList, paDeliveryVO, paPromoTargetList);
					paramMap.put("paCode",paNaverGoodsList.get(i).getPaCode());
					
					//log.info("11.결과 저장");
					if(product != null){
						log.info("12.API 호출 ");
						ManageProductRequestType productRequest = new ManageProductRequestType();
						productRequest.setRequestID(UUID.randomUUID().toString());
						productRequest.setVersion(ConfigUtil.getString("PANAVER_GOODS_VERSION"));
						productRequest.setAccessCredentials(ComUtil.paNaverCreateAccessCredentials("ProductService", "ManageProduct"));
						productRequest.setSellerId(ConfigUtil.getString("PANAVER_MALL_ID"));
						productRequest.setProduct(product);

						// ManageProduct 호출
						ProductService productService = new ProductService();
						ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
						ManageProductResponseType response = port.manageProduct(productRequest);
						respType = response.getResponseType(); //"SUCCESS" / "ERROR"

						paramMap.put("message",response.getResponseType());
						log.info("result message : "+paramMap.getString("message"));

						paramMap.put("resultType",respType);
						if(respType.equals("SUCCESS")){
							//response DATA
							paramMap.put("productId",response.getProductId());
							paramMap.put("message",getMessage("pa.paNaver_response", new String[] {" 상품코드 : " + paramMap.getString("goodsCode")}));

							if(paramMap.getString("resultType").equals("SUCCESS")){
								
								paNaverGoodsList.get(i).setPaStatus("30");//입점완료
								paNaverGoodsList.get(i).setProductId(paramMap.getString("productId"));
								paNaverGoodsList.get(i).setReturnNote(paramMap.getString("message"));
								paNaverGoodsList.get(i).setInsertId(procId);
								paNaverGoodsList.get(i).setModifyId(procId);
								paNaverGoodsList.get(i).setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paNaverGoodsList.get(i).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paNaverGoodsList.get(i).setLastModifyDate(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));
								
								log.info("13.제휴사 상품정보 저장");
								rtnMsg = paNaverGoodsService.savePaNaverGoodsTx(paNaverGoodsList.get(i));
								
								if(!rtnMsg.equals("000000")){
									paramMap.put("code","404");
									paramMap.put("rtnMessage",rtnMsg);
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("rtnMessage")), HttpStatus.OK);
								} else {
									paramMap.put("code","200");
								}
								//log.info("14.상품가격 - TRANS_ID, TRANS_DATE 수정);
//								paNaverGoodsService.savePaGoodsPriceTx(paramMap, paPromoTargetList.get(0));
								paNaverGoodsService.savePaGoodsPriceTx(paramMap, paPromoTargetList);//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
															
								log.info("15.옵션 API 호출");
								optionCnt = paNaverGoodsService.selectPaGoodsdtMapping(paramMap);
								if(optionCnt>=2){
									optionInsert(request, paramMap.getString("productId"), paNaverGoodsList.get(i));
								}else{
									paramMap.put("transOrderAbleQty", paNaverGoodsList.get(i).getTransOrderAbleQty());
									paNaverGoodsService.saveSinglePaGoodsdtMappingTx(paramMap);
								}
								
								//log.info("16.상품 판매 여부 조회");
								checkSaleGb = paNaverGoodsService.selectPaNaverSaleGb(paNaverGoodsList.get(i).getGoodsCode());
								
								log.info("17.판매상태변경");
							    if(paNaverGoodsList.get(i).getTransOrderAbleQty()==0 || checkSaleGb.equals("11") || checkSaleGb.equals("05") || checkSaleGb.equals("19")){
							    	changeProductSaleStatus(request, paramMap.getString("productId"), "30", paNaverGoodsList.get(i));
							    }
							} 
							paramMap.put("message",getMessage("pa.save_paNaver_goods", new String[] {" 상품코드 : " +  product.getProductId()}));
						} else {
							if(response.getError().getDetail().length()>=890){
								paramMap.put("message",response.getError().getDetail().substring(0, 890));
								paramMap.put("errMessage",response.getError().getDetail().substring(0, 890));
							}else{
								paramMap.put("message",response.getError().getDetail());								
								paramMap.put("errMessage",response.getError().getDetail());
							}
							paramMap.put("code","404");
							
							if(product.getProductId()!=null){						
								paramMap.put("productId",product.getProductId());								
								// 판매중지처리
								savePaGoodsModifyFail(paramMap);
							}else{
								paramMap.put("productId",paramMap.getString("goodsCode"));	
							}

							log.info("18.상품 판매상태  API - 판매중단처리");
							if(paNaverGoodsList.get(i).getProductId()!=null)
								changeProductSaleStatus(request, paNaverGoodsList.get(i).getProductId(), "30", paNaverGoodsList.get(i));
						} 
					}
					
					if(product.getProductId()==null){
						paramMap.put("code","411");
						paramMap.put("message","오류메세지 : " + paramMap.getString("errMessage"));
						log.error(paramMap.getString("message"));
						continue;
					}					
				}catch(Exception e){
					if(e.getMessage()!=null)
						paramMap.put("message","Exception: "+e+" / DetailMessage: "+e.getMessage());
					else paramMap.put("message","java.lang.NullPointerException");
					log.error("상품 등록 및 수정 Error : "+e.getMessage());
					
					if(paramMap.getString("goodsCode")!=null)
						log.error("상품등록_1 Error, 상품코드 : "+paNaverGoodsList.get(i).getGoodsCode());	

					paramMap.put("goodsCode",paNaverGoodsList.get(i).getGoodsCode());
					paramMap.put("resultType", "ERROR");
					if(paNaverGoodsList.get(i).getProductId()!=null)						
						paramMap.put("productId",paNaverGoodsList.get(i).getProductId());
					else if(paNaverGoodsList.get(i).getGoodsCode()!=null)
						paramMap.put("productId",paNaverGoodsList.get(i).getGoodsCode());	
					// 판매중지처리 
					savePaGoodsModifyFail(paramMap);
				}finally{	
					if(!paramMap.getString("resultType").equals("SUCCESS") && paramMap.getString("productId").equals(paramMap.getString("goodsCode"))){ // 등록실패
						paramMap.put("message", paramMap.getString("message").length() > 999 ? paramMap.getString("message").substring(0, 999) : paramMap.getString("message"));
						paNaverGoods = new PaNaverGoodsVO();
						paNaverGoods.setGoodsCode(paramMap.getString("goodsCode"));
						paNaverGoods.setPaCode(paramMap.getString("paCode"));
						paNaverGoods.setPaStatus("20");//입점반려
						paNaverGoods.setReturnNote(paramMap.getString("message"));
						paNaverGoods.setModifyId(procId);
						
						//log.info("19.제휴사 실패정보 저장");
						paNaverGoodsService.savePaNaverGoodsFailTx(paNaverGoods);
					}else if(paramMap.getString("resultType").equals("SUCCESS")){ // 등록/수정 성공
						paramMap.put("message",getMessage("pa.save_paNaver_goods", new String[] {" 상품코드 : " +  product.getProductId()}));
					}

					try{
						savePaGoodsTransLog(paramMap);
					}catch(Exception e){
						log.error("ApiTracking Insert Error : "+e.getMessage());
					}
				}
			}
			
		}catch(Exception e){
			if(duplicateCheck.equals("1"))
				paramMap.put("code","490");
			else 
				paramMap.put("code","500");			
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			} else paramMap.put("message","java.lang.NullPointerException");
			
			if(paramMap.getString("goodsCode")!=null)
				log.error("상품등록_2 Error, 상품코드 : "+paramMap.getString("goodsCode"));	
			
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				paramMap.put("message", paramMap.getString("message").length() > 999 ? paramMap.getString("message").substring(0, 999) : paramMap.getString("message"));
				paramMap.put("resultMessage",paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(!searchTermGb.equals("1")){
				if(duplicateCheck.equals("0")){
					systemService.checkCloseHistoryTx("end", prg_id);
				}				
			}
			log.info("===== 상품등록 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 옵션 등록 및 수정 
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "옵션 등록 및 수정", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/option-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> optionInsert(HttpServletRequest request,
			@RequestParam(value="productId", required=true) String productId,
			@RequestParam(value="PaNaverGoods", required=true) PaNaverGoodsVO paNaverGoods) throws Exception{
		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		String duplicateCheck = "";	
		List<PaGoodsdtMapping> paGoodsdtMappingList = null;
		OptionType option = null;
		
		log.info("===== 옵션 등록 및 수정 API Start =====");
		//log.info("01.API 기본 정보 세팅");
		
		//connectionSetting 설정
		String prg_id = "IF_PANAVERAPI_01_002";
		dateTime = systemService.getSysdatetimeToString();

		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		paramMap.put("modCase", "SELECT");
		paramMap.put("code","200");
		paramMap.put("siteGb","PANAVER");
		paramMap.put("productId",productId);
		paramMap.put("paCode", paNaverGoods.getPaCode());
		paramMap.put("goodsCode", paNaverGoods.getGoodsCode());
		
		try{			
			//log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
						
			log.info("03.옵션 정보 조회");
			paramMap.put("paGoodsCode", paNaverGoods.getGoodsCode());
			paramMap.put("paGroupCode", "04");
			paGoodsdtMappingList = paNaverGoodsService.selectPaGoodsdt(paramMap); 
			
			if(paGoodsdtMappingList != null && paGoodsdtMappingList.size()>0){
				//log.info("04.옵션 세팅");
				option = new OptionType();
				option.setProductId(Long.parseLong(paramMap.getString("productId")));
				option.setSortType("CRE");
				option.setCombination(createCombinationOption(paGoodsdtMappingList));
				
				ManageOptionRequestType optionRequest = new ManageOptionRequestType();
				optionRequest.setRequestID(UUID.randomUUID().toString());
				optionRequest.setVersion(ConfigUtil.getString("PANAVER_GOODS_VERSION"));
				optionRequest.setAccessCredentials(ComUtil.paNaverCreateAccessCredentials("ProductService", "ManageOption"));
				optionRequest.setSellerId(ConfigUtil.getString("PANAVER_MALL_ID"));
				optionRequest.setOption(option);

				ProductService optionService = new ProductService();
				ProductServicePortType port = optionService.getProductServiceSOAP12PortHttp();
				ManageOptionResponseType response = port.manageOption(optionRequest);

				paramMap.put("resultType", response.getResponseType());				
				if ("SUCCESS".equals(response.getResponseType())) {
					paramMap.put("message",getMessage("pa.paNaver_option_insert_response", new String[] {" 상품코드 : " + paramMap.getString("goodsCode")}));
					log.info("05.옵션 정보 조회");
					optionInfoSelect(request, response.getProductId(), paramMap.getString("paGoodsCode"), paramMap.getString("paCode"));
				}else{
					//log.info("05-1.저장 실패사유");
					if(response.getError().getDetail().length()>=890)
						paramMap.put("message",response.getError().getDetail().substring(0, 890));
					else
						paramMap.put("message",response.getError().getDetail());
					paramMap.put("modifyId","PANAVER");
					paNaverGoodsService.updatePaNaverOptionFailTx(paramMap);

					log.info("05-2.상품 판매상태  API - 판매중단처리");
					changeProductSaleStatus(request, paramMap.getString("productId"), "30", paNaverGoods);				
				} 				
			}else{
				paramMap.put("resultType", "ERROR");	
				paramMap.put("message",getMessage("pa.fail_paNaver_option", new String[] {" 상품코드 : " +  paramMap.getString("goodsCode")}));				
			}
		}catch(Exception e){
			if(duplicateCheck.equals("1"))
				paramMap.put("code","490");
			else 
				paramMap.put("code","500");			
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			} else paramMap.put("message","java.lang.NullPointerException");
			
			if(paNaverGoods.getGoodsCode()!=null)
				log.error("옵션 등록 및 수정 Error, 상품코드 : "+paNaverGoods.getGoodsCode());	
			
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				paramMap.put("message", paramMap.getString("message").length() > 999 ? paramMap.getString("message").substring(0, 999) : paramMap.getString("message"));
				paramMap.put("resultMessage",paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
				savePaGoodsTransLog(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 옵션 등록 및 수정  API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 옵션 정보 조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 옵션 조회", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/option-info", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> optionInfoSelect(HttpServletRequest request
			, @RequestParam(value="productId", required=true) long productId
			, @RequestParam(value="goodsCode", required=true) String goodsCode
			, @RequestParam(value="paCode", required=true) String paCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		String duplicateCheck = "";	
		PaGoodsdtMapping paGoodsdtMapping = null;
		
		log.info("===== 상품옵션 조회 API Start =====");
		//log.info("01.API 기본 정보 세팅");
		
		//connectionSetting 설정
		String prg_id = "IF_PANAVERAPI_01_004";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		paramMap.put("modCase", "INSERT");
		paramMap.put("code","200");
		paramMap.put("siteGb","PANAVER");
		paramMap.put("productId",productId);
		paramMap.put("paCode",paCode);
		paramMap.put("goodsCode",goodsCode);
		
		try{			
			//log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
						
			GetOptionRequestType optionRequest = new GetOptionRequestType();
			optionRequest.setRequestID(UUID.randomUUID().toString()); 
			optionRequest.setVersion(ConfigUtil.getString("PANAVER_GOODS_VERSION"));
			optionRequest.setSellerId(ConfigUtil.getString("PANAVER_MALL_ID"));
			optionRequest.setProductId(productId);
			optionRequest.setAccessCredentials(ComUtil.paNaverCreateAccessCredentials("ProductService", "GetOption"));
			
			ProductService productService = new ProductService();
			ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
			GetOptionResponseType response = port.getOption(optionRequest);

			paramMap.put("resultType",response.getResponseType());
			if ("SUCCESS".equals(response.getResponseType())) {				
				//TPAGOODSMAPPING 세팅 LIST
				for(int i=0; i<response.getOption().getCombination().getItemList().getItem().size(); i++){
					paGoodsdtMapping = new PaGoodsdtMapping();
					
					paGoodsdtMapping.setPaCode(paCode);
					paGoodsdtMapping.setGoodsCode(goodsCode);
					paGoodsdtMapping.setGoodsdtCode(response.getOption().getCombination().getItemList().getItem().get(i).getSellerManagerCode());
					paGoodsdtMapping.setPaOptionCode(response.getOption().getCombination().getItemList().getItem().get(i).getId()+"");
					paGoodsdtMapping.setTransOrderAbleQty(response.getOption().getCombination().getItemList().getItem().get(i).getQuantity()+"");
					
					log.info("03.옵션 등록 및 수정");
					paNaverGoodsService.savePaNaverOptionTx(paGoodsdtMapping);
				}
				paramMap.put("message",getMessage("pa.paNaver_option_info_response", new String[] {" 상품코드 : " + paramMap.getString("goodsCode")}));					
			}else{
				if(response.getError().getDetail().length()>=890)
					paramMap.put("message",response.getError().getDetail().substring(0, 890));
				else
					paramMap.put("message",response.getError().getDetail());
			}
		}catch(Exception e){
			if(duplicateCheck.equals("1"))
				paramMap.put("code","490");
			else 
				paramMap.put("code","500");		
			if(e.getMessage()!=null)
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			else paramMap.put("message","java.lang.NullPointerException");
			
			log.error("상품 등록 및 수정 Error : "+e.getMessage()); 
			
			if(goodsCode!=null)
				log.error("상품옵션 조회 Error, 상품코드 : "+goodsCode);	
			
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				paramMap.put("message", paramMap.getString("message").length() > 999 ? paramMap.getString("message").substring(0, 999) : paramMap.getString("message"));
				paramMap.put("resultMessage",paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
				savePaGoodsTransLog(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 상품옵션 조회 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	
	/**
	 * 상품 판매상태 일괄 수정 
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 판매 상태 일괄 수정", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/legacy-goods-sell-modify-all-object", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> changeProductSaleStatusAllObject(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		
		String dateTime = "";
		String duplicateCheck = "";
		List<PaNaverGoodsVO> paNaverGoodsList = null;
		String paCode="41";
	
		log.info("===== 특정 상품 판매상태 일괄 변경 API Start=====");
		//log.info("01.API 기본정보 세팅");

		String prg_id = "IF_PANAVERAPI_01_006";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		paramMap.put("paCode", paCode);
		paramMap.put("code","200");
		paramMap.put("siteGb","PANAVER");
		
		try{
			//log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			paNaverGoodsList = paNaverGoodsService.selectGoodsSellModifyAllObjectList(paramMap);			
			if(paNaverGoodsList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list", new String[] {" 상품코드 : " + paramMap.getString("goodsCode")}));
				paramMap.put("resultMessage",paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}		
			for(PaNaverGoodsVO paNaverGoods : paNaverGoodsList){	
				log.info("03.상품 판매상태 수정 API 호츨");		
				changeProductSaleStatus(request, paNaverGoods.getProductId(), paNaverGoods.getPaSaleGb(), paNaverGoods);				
			}		
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			} else paramMap.put("message","java.lang.NullPointerException");
			
			if(paramMap.getString("goodsCode")!=null)
				log.error("상품옵션 조회 Error, 상품코드 : "+paramMap.getString("goodsCode"));	
			
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("message", paramMap.getString("message").length() > 999 ? paramMap.getString("message").substring(0, 999) : paramMap.getString("message"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 특정 상품 판매상태 일괄 변경 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 * 상품 판매상태 수정 
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 판매 상태 수정", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/goods-sell-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> changeProductSaleStatus(HttpServletRequest request,
			@RequestParam(value="productId", required=true) String productId,
			@RequestParam(value="checkSaleGb", required=true) String checkSaleGb,
			@RequestParam(value="paNaverGoods", required=true) PaNaverGoodsVO paNaverGoods) throws Exception{
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		String dateTime = "";
		String duplicateCheck = "";
		//PaGoodsTransLog paGoodsTransLog = null;
		String respType="";
		String statusMessage="";
	
		log.info("===== 판매상태변경 API Start=====");
		//log.info("01.API 기본정보 세팅");

		String prg_id = "IF_PANAVERAPI_01_003";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		paramMap.put("goodsCode", paNaverGoods.getGoodsCode());
		paramMap.put("paCode", paNaverGoods.getPaCode());
		paramMap.put("productId", productId);
		paramMap.put("code","200");
		paramMap.put("siteGb","PANAVER");
		
		try{
			//log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
//			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			// SaleStatusType 객체 생성
			SaleStatusType saleStatus = new SaleStatusType();
			saleStatus.setProductId(Long.parseLong(paramMap.getString("productId")));

			if(checkSaleGb.equals("30")){ // 판매 중지
				saleStatus.setStatusType("SUSP"); // 판매 상태 (SUSP(판매 중지))
				paNaverGoods.setPaSaleGb("30");						
				statusMessage = "판매 중지";
			} else if(checkSaleGb.equals("20")){
				if(paNaverGoods.getTransOrderAbleQty()!=0){
					saleStatus.setStatusType("SALE"); // 판매 상태 (SALE(판매 중))
					paNaverGoods.setPaSaleGb("20");	
					paNaverGoods.setTransOrderAbleQty(paNaverGoods.getTransOrderAbleQty());	
					statusMessage = "판매 중";	
				}else {
					saleStatus.setStatusType("OSTK"); // 판매 상태 (OSTK(품절))
					paNaverGoods.setPaSaleGb("20");	
					statusMessage = "품절";
				}
			} 
						
			ChangeProductSaleStatusRequestType ChangeProductSaleStatusRequest = new ChangeProductSaleStatusRequestType();
			ChangeProductSaleStatusRequest.setRequestID(UUID.randomUUID().toString());
			ChangeProductSaleStatusRequest.setVersion(ConfigUtil.getString("PANAVER_GOODS_VERSION"));
			ChangeProductSaleStatusRequest.setAccessCredentials(ComUtil.paNaverCreateAccessCredentials("ProductService", "ChangeProductSaleStatus"));
			ChangeProductSaleStatusRequest.setSellerId(ConfigUtil.getString("PANAVER_MALL_ID"));
			ChangeProductSaleStatusRequest.setSaleStatus(saleStatus);

			// ManageProduct 호출
			ProductService optionService = new ProductService();
			ProductServicePortType port = optionService.getProductServiceSOAP12PortHttp();
			ChangeProductSaleStatusResponseType response = port.changeProductSaleStatus(ChangeProductSaleStatusRequest);
			respType = response.getResponseType();

			paramMap.put("resultType",respType);
			//log.info("03.Response에서 상품번호 확인");
			if ("SUCCESS".equals(respType)) {
				paramMap.put("productId", response.getProductId());
				paramMap.put("resultType",respType);
				paramMap.put("message",getMessage("pa.save_paNaver_goods_sell_modify", new String[] {" 상품코드 : " + paramMap.getString("goodsCode")})+" ("+statusMessage+")");
		
				if(paramMap.getString("resultType").equals("SUCCESS")){
					paNaverGoods.setModifyId("PANAVER");
					paNaverGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paNaverGoods.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paNaverGoods.setTransSaleYn("0");
					
					log.info("05.동기화 완료 저장");
					rtnMsg = paNaverGoodsService.savePaNaverGoodsSellTx(paNaverGoods);
					
					if(!rtnMsg.equals("000000")){
						paramMap.put("code","404");
						paramMap.put("rtnMessage",rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("rtnMessage")), HttpStatus.OK);
					} else {
						paramMap.put("code","200");
					}					
				} else if( paramMap.getString("message").indexOf("상품만 판매중지가 가능합니다.") > -1 ){
					paramMap.put("goodsCode", paNaverGoods.getGoodsCode());
					paramMap.put("paCode", paNaverGoods.getPaCode());
					paramMap.put("modifyId", "PANAVER");
					paNaverGoodsService.saveTransSaleYNTx(paramMap);
					
					paramMap.put("code","200");
					log.info("===이미 중지된 상품 TRANS_SALE_YN = 0 UPDATE process===");
					paNaverGoodsService.saveStopMonitering(paNaverGoods);
				} else {
					paramMap.put("code","404");
				}
			}else{
				// 주석 삭제 예정
				// 품절 -> 판매중(재고입력x) : ERR-PRD-000102 / 재고수량 항목을 입력해 주세요. (테스트하면서 추가 선택)
				// 품절 -> 판매중단 :        ERR-PRD-000102 / 품절 상품은 판매중 상태로만 변경할 수 있습니다.O
				// 품절 -> 품절 :           ERR-PRD-000102 / 판매중 상품만 품절 상태로 변경할 수 있습니다.O
				// 판매중단 -> 품절 :        ERR-PRD-000102 / 판매중 상품만 품절 상태로 변경할 수 있습니다.O
				log.info("03-1.오류로 변경안된 판매상태 수정");
				if(response.getError().getCode().equals("ERR-PRD-000102")&&
					(response.getError().getDetail().equals("품절 상품은 판매중 상태로만 변경할 수 있습니다.")
					||response.getError().getDetail().equals("판매중 상품만 품절 상태로 변경할 수 있습니다."))){
					paramMap.put("modifyId", "PANAVER");
					paNaverGoodsService.saveTransSaleYNTx(paramMap);
				}

				if(response.getError().getDetail().length()>=890)
					paramMap.put("message",response.getError().getDetail().substring(0, 890));
				else
					paramMap.put("message",response.getError().getDetail());
			}
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			} else paramMap.put("message","java.lang.NullPointerException");
			
			if(paNaverGoods.getGoodsCode()!=null)
				log.error("상품옵션 조회 Error, 상품코드 : "+paNaverGoods.getGoodsCode());	
			
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("message", paramMap.getString("message").length() > 999 ? paramMap.getString("message").substring(0, 999) : paramMap.getString("message"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
				savePaGoodsTransLog(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 판매상태변경 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/** 판매중지처리/message처리 */
	public void savePaGoodsModifyFail(ParamMap paramMap) throws Exception{
		PaNaverGoodsVO paNaverGoods = new PaNaverGoodsVO();
		paNaverGoods.setPaGroupCode("04");
		paNaverGoods.setGoodsCode(paramMap.getString("goodsCode"));
		paNaverGoods.setPaCode(paramMap.getString("paCode"));
		paNaverGoods.setPaSaleGb("30");
		paNaverGoods.setReturnNote(paramMap.getString("message"));
		paNaverGoods.setProductId(paramMap.getString("productId"));
		paNaverGoodsService.savePaGoodsModifyFailTx(paNaverGoods);
	}
	
	/** TRANSLOG(상품) */
	public void savePaGoodsTransLog(ParamMap paramMap) throws Exception{
		PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();		
		paGoodsTransLog.setGoodsCode(paramMap.getString("goodsCode"));
		paGoodsTransLog.setPaCode(paramMap.getString("paCode"));
		paGoodsTransLog.setItemNo(paramMap.getString("productId").isEmpty()?paramMap.getString("goodsCode"):paramMap.getString("productId"));
		paGoodsTransLog.setRtnCode(paramMap.getString("resultType"));
		paGoodsTransLog.setRtnMsg(paramMap.getString("apiCode") + " || " + paramMap.getString("message"));
		paGoodsTransLog.setSuccessYn(paramMap.getString("resultType").equals("SUCCESS")==true?"1":"0");
		paGoodsTransLog.setProcDate(paramMap.getTimestamp("startDate"));
		paGoodsTransLog.setProcId(paramMap.getString("siteGb"));
		paNaverGoodsService.insertPaNaverGoodsTransLogTx(paGoodsTransLog);
	}
	
	@RequestMapping(value = "/modelInfo", method = RequestMethod.GET)
	@ResponseBody
	public GetModelListResponseType getModelInfo(@RequestParam(value = "page" , defaultValue = "1" , required = false)String page
			 							        ,@RequestParam(value = "modelName" , defaultValue = "", required = false) String modelName
			 							        ,HttpServletRequest request) {

		GetModelListResponseType response = null;

			try {
				//log.info(modelName);
				//modelName = new String(modelName.getBytes("8859_1"), "UTF-8");
				//log.info(modelName);
				GetModelListRequestType2 modelListRequest = new GetModelListRequestType2();
				modelListRequest.setRequestID(UUID.randomUUID().toString());
				modelListRequest.setVersion(ConfigUtil.getString("PANAVER_GOODS_VERSION"));
				modelListRequest.setModelName(modelName);

				modelListRequest.setPage(page);
				modelListRequest.setAccessCredentials(ComUtil.paNaverCreateAccessCredentials("ProductService", "GetModelList"));
				
				ProductService productService = new ProductService();
				ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
				response = port.getModelList2(modelListRequest);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		  
		return response;
	}
}