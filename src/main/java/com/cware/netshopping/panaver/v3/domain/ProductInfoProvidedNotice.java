package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductInfoProvidedNotice {
		
	// 상품정보제공고시 타입
	private String productInfoProvidedNoticeType;
	// 의류 상품정보제공고시
	private Wear wear;
	// 구두/신발 상품정보제공고시
	private Shoes shoes;
	// 가방 상품정보제공고시
	private Bag bag;
	// 패션잡화(모자/벨트/액세서리) 상품정보제공고시
	private FashionItems fashionItems;
	// 침구류/커튼 상품정보제공고시
	private SleepingGear sleepingGear;
	// 가구(침대/소파/싱크대/DIY제품) 상품정보제공고시
	private Furniture furniture;
	// 영상가전(TV류) 상품정보제공고시
	private ImageAppliances imageAppliances;
	// 가정용 전기제품(냉장고/세탁기/식기세척기/전자레인지) 정보제공고시
	private HomeAppliances homeAppliances;
	// 계절가전(에어컨/온풍기) 상품정보제공고시
	private SeasonAppliances seasonAppliances;
	// 사무용기기(컴퓨터/노트북/프린터) 상품정보제공고시
	private OfficeAppliances officeAppliances;
	// 광학기기(디지털카메라/캠코더) 상품정보제공고시
	private OpticsAppliances opticsAppliances;
	// 소형전자(MP3/전자사전 등) 상품정보제공고시
	private MicroElectronics microElectronics;
	// 내비게이션 상품정보제공고시
	private Navigation navigation;
	// 자동차용품(자동차부품/기타 자동차용품) 상품정보제공고시
	private CarArticles carArticles;
	// 의료기기 상품정보제공고시
	private MedicalAppliances medicalAppliances;
	// 주방용품 상품정보제공고시
	private KitchenUtensils kitchenUtensils;
	// 화장품 상품정보제공고시
	private Cosmetic cosmetic;
	// 귀금속/보석/시계류 상품정보제공고시
	private Jewellery jewellery;
	// 식품(농수산물) 상품정보제공고시
	private Food food;
	// 가공식품 상품정보제공고시
	private GeneralFood generalFood;
	// 건강기능식품 상품정보제공고시
	private DietFood dietFood;
	// 어린이제품요약정보 상품정보제공고시
	private Kids kids;
	// 악기 상품정보제공고시
	private MusicalInstrument musicalInstrument;
	// 스포츠용품 상품정보제공고시
	private SportsEquipment sportsEquipment;
	// 서적 상품정보제공고시
	private Books books;
	// 물품대여 서비스(서적, 유아용품, 행사용품 등) 상품정보제공고시
	private RentalEtc rentalEtc;
	// 디지털 콘텐츠(음원, 게임, 인터넷강의 등) 상품정보제공고시
	private DigitalContents digitalContents;
	// 상품권/쿠폰 상품정보제공고시
	private GiftCard giftCard;	
	// 모바일 쿠폰 상품정보제공고시
	private MobileCoupon mobileCoupon;
	// 영화/공연 상품정보제공고시
	private MovieShow movieShow;
	// 기타 용역 상품정보제공고시
	private EtcService etcService;
	// 생활화학제품 상품정보제공고시
	private Biochemistry biochemistry;
	// 살생물제품 상품정보제공고시
	private Biocidal biocidal;
	// 휴대폰 상품정보제공고시
	private CellPhone cellPhone;
	// 기타 재화 상품정보제공고시
	private Etc etc;
	
	
	public String getProductInfoProvidedNoticeType() {
		return productInfoProvidedNoticeType;
	}

	public void setProductInfoProvidedNoticeType(String productInfoProvidedNoticeType) {
		this.productInfoProvidedNoticeType = productInfoProvidedNoticeType;
	}

	public Wear getWear() {
		return wear;
	}

	public void setWear(Wear wear) {
		this.wear = wear;
	}

	public Shoes getShoes() {
		return shoes;
	}

	public void setShoes(Shoes shoes) {
		this.shoes = shoes;
	}

	public Bag getBag() {
		return bag;
	}

	public void setBag(Bag bag) {
		this.bag = bag;
	}

	public FashionItems getFashionItems() {
		return fashionItems;
	}

	public void setFashionItems(FashionItems fashionItems) {
		this.fashionItems = fashionItems;
	}

	public SleepingGear getSleepingGear() {
		return sleepingGear;
	}

	public void setSleepingGear(SleepingGear sleepingGear) {
		this.sleepingGear = sleepingGear;
	}

	public Furniture getFurniture() {
		return furniture;
	}

	public void setFurniture(Furniture furniture) {
		this.furniture = furniture;
	}

	public ImageAppliances getImageAppliances() {
		return imageAppliances;
	}

	public void setImageAppliances(ImageAppliances imageAppliances) {
		this.imageAppliances = imageAppliances;
	}

	public HomeAppliances getHomeAppliances() {
		return homeAppliances;
	}

	public void setHomeAppliances(HomeAppliances homeAppliances) {
		this.homeAppliances = homeAppliances;
	}

	public SeasonAppliances getSeasonAppliances() {
		return seasonAppliances;
	}

	public void setSeasonAppliances(SeasonAppliances seasonAppliances) {
		this.seasonAppliances = seasonAppliances;
	}

	public OfficeAppliances getOfficeAppliances() {
		return officeAppliances;
	}

	public void setOfficeAppliances(OfficeAppliances officeAppliances) {
		this.officeAppliances = officeAppliances;
	}

	public OpticsAppliances getOpticsAppliances() {
		return opticsAppliances;
	}

	public void setOpticsAppliances(OpticsAppliances opticsAppliances) {
		this.opticsAppliances = opticsAppliances;
	}

	public MicroElectronics getMicroElectronics() {
		return microElectronics;
	}

	public void setMicroElectronics(MicroElectronics microElectronics) {
		this.microElectronics = microElectronics;
	}

	public Navigation getNavigation() {
		return navigation;
	}

	public void setNavigation(Navigation navigation) {
		this.navigation = navigation;
	}

	public CarArticles getCarArticles() {
		return carArticles;
	}

	public void setCarArticles(CarArticles carArticles) {
		this.carArticles = carArticles;
	}

	public MedicalAppliances getMedicalAppliances() {
		return medicalAppliances;
	}

	public void setMedicalAppliances(MedicalAppliances medicalAppliances) {
		this.medicalAppliances = medicalAppliances;
	}

	public KitchenUtensils getKitchenUtensils() {
		return kitchenUtensils;
	}

	public void setKitchenUtensils(KitchenUtensils kitchenUtensils) {
		this.kitchenUtensils = kitchenUtensils;
	}

	public Cosmetic getCosmetic() {
		return cosmetic;
	}

	public void setCosmetic(Cosmetic cosmetic) {
		this.cosmetic = cosmetic;
	}

	public Jewellery getJewellery() {
		return jewellery;
	}

	public void setJewellery(Jewellery jewellery) {
		this.jewellery = jewellery;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public GeneralFood getGeneralFood() {
		return generalFood;
	}

	public void setGeneralFood(GeneralFood generalFood) {
		this.generalFood = generalFood;
	}

	public DietFood getDietFood() {
		return dietFood;
	}

	public void setDietFood(DietFood dietFood) {
		this.dietFood = dietFood;
	}

	public Kids getKids() {
		return kids;
	}

	public void setKids(Kids kids) {
		this.kids = kids;
	}

	public MusicalInstrument getMusicalInstrument() {
		return musicalInstrument;
	}

	public void setMusicalInstrument(MusicalInstrument musicalInstrument) {
		this.musicalInstrument = musicalInstrument;
	}

	public SportsEquipment getSportsEquipment() {
		return sportsEquipment;
	}

	public void setSportsEquipment(SportsEquipment sportsEquipment) {
		this.sportsEquipment = sportsEquipment;
	}

	public Books getBooks() {
		return books;
	}

	public void setBooks(Books books) {
		this.books = books;
	}

	public RentalEtc getRentalEtc() {
		return rentalEtc;
	}

	public void setRentalEtc(RentalEtc rentalEtc) {
		this.rentalEtc = rentalEtc;
	}

	public DigitalContents getDigitalContents() {
		return digitalContents;
	}

	public void setDigitalContents(DigitalContents digitalContents) {
		this.digitalContents = digitalContents;
	}

	public GiftCard getGiftCard() {
		return giftCard;
	}

	public void setGiftCard(GiftCard giftCard) {
		this.giftCard = giftCard;
	}

	public MobileCoupon getMobileCoupon() {
		return mobileCoupon;
	}

	public void setMobileCoupon(MobileCoupon mobileCoupon) {
		this.mobileCoupon = mobileCoupon;
	}

	public MovieShow getMovieShow() {
		return movieShow;
	}

	public void setMovieShow(MovieShow movieShow) {
		this.movieShow = movieShow;
	}

	public EtcService getEtcService() {
		return etcService;
	}

	public void setEtcService(EtcService etcService) {
		this.etcService = etcService;
	}

	public Biochemistry getBiochemistry() {
		return biochemistry;
	}

	public void setBiochemistry(Biochemistry biochemistry) {
		this.biochemistry = biochemistry;
	}

	public Biocidal getBiocidal() {
		return biocidal;
	}

	public void setBiocidal(Biocidal biocidal) {
		this.biocidal = biocidal;
	}

	public CellPhone getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(CellPhone cellPhone) {
		this.cellPhone = cellPhone;
	}

	public Etc getEtc() {
		return etc;
	}

	public void setEtc(Etc etc) {
		this.etc = etc;
	}

	@Override
	public String toString() {
		return "ProductInfoProvidedNotice [productInfoProvidedNoticeType=" + productInfoProvidedNoticeType + "wear="+ wear + "shoes="+ shoes + "bag="+ bag
				+ "fashionItems=" + fashionItems + "sleepingGear="+ sleepingGear + "furniture="+ furniture + "imageAppliances="+ imageAppliances
				+ "homeAppliances=" + homeAppliances + "seasonAppliances="+ seasonAppliances + "officeAppliances="+ officeAppliances + "opticsAppliances="+ opticsAppliances
				+ "microElectronics=" + microElectronics + "navigation="+ navigation + "carArticles="+ carArticles + "medicalAppliances="+ medicalAppliances
				+ "kitchenUtensils=" + kitchenUtensils + "cosmetic="+ cosmetic + "jewellery="+ jewellery + "food="+ food
				+ "generalFood=" + generalFood + "dietFood="+ dietFood + "kids="+ kids + "musicalInstrument="+ musicalInstrument
				+ "sportsEquipment=" + sportsEquipment + "books="+ books + "rentalEtc="+ rentalEtc + "digitalContents="+ digitalContents
				+ "giftCard=" + giftCard + "mobileCoupon="+ mobileCoupon + "movieShow="+ movieShow + "etcService="+ etcService
				+ "biochemistry=" + biochemistry + "biocidal="+ biocidal + "cellPhone="+ cellPhone + "etc="+ etc	+ "]";
	}
}
