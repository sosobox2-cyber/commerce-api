package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProductSummaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProductSummaryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Shoes" type="{http://shopn.platform.nhncorp.com/}ShoesSummaryType"/>
 *         &lt;element name="Wear" type="{http://shopn.platform.nhncorp.com/}WearSummaryType"/>
 *         &lt;element name="Bag" type="{http://shopn.platform.nhncorp.com/}BagSummaryType"/>
 *         &lt;element name="FashionItems" type="{http://shopn.platform.nhncorp.com/}FashionItemsSummaryType"/>
 *         &lt;element name="SleepingGear" type="{http://shopn.platform.nhncorp.com/}SleepingGearSummaryType"/>
 *         &lt;element name="Furniture" type="{http://shopn.platform.nhncorp.com/}FurnitureSummaryType"/>
 *         &lt;element name="ImageAppliances" type="{http://shopn.platform.nhncorp.com/}ImageAppliancesSummaryType"/>
 *         &lt;element name="HomeAppliances" type="{http://shopn.platform.nhncorp.com/}HomeAppliancesSummaryType"/>
 *         &lt;element name="SeasonAppliances" type="{http://shopn.platform.nhncorp.com/}SeasonAppliancesSummaryType"/>
 *         &lt;element name="OfficeAppliances" type="{http://shopn.platform.nhncorp.com/}OfficeAppliancesSummaryType"/>
 *         &lt;element name="OpticsAppliances" type="{http://shopn.platform.nhncorp.com/}OpticsAppliancesSummaryType"/>
 *         &lt;element name="MicroElectronics" type="{http://shopn.platform.nhncorp.com/}MicroElectronicsSummaryType"/>
 *         &lt;element name="Navigation" type="{http://shopn.platform.nhncorp.com/}NavigationSummaryType"/>
 *         &lt;element name="CarArticles" type="{http://shopn.platform.nhncorp.com/}CarArticlesSummaryType"/>
 *         &lt;element name="MedicalAppliances" type="{http://shopn.platform.nhncorp.com/}MedicalAppliancesSummaryType"/>
 *         &lt;element name="KitchenUtensils" type="{http://shopn.platform.nhncorp.com/}KitchenUtensilsSummaryType"/>
 *         &lt;element name="Cosmetic" type="{http://shopn.platform.nhncorp.com/}CosmeticSummaryType"/>
 *         &lt;element name="Jewellery" type="{http://shopn.platform.nhncorp.com/}JewellerySummaryType"/>
 *         &lt;element name="Food" type="{http://shopn.platform.nhncorp.com/}FoodSummaryType"/>
 *         &lt;element name="GeneralFood" type="{http://shopn.platform.nhncorp.com/}GeneralFoodSummaryType"/>
 *         &lt;element name="DietFood" type="{http://shopn.platform.nhncorp.com/}DietFoodSummaryType"/>
 *         &lt;element name="Kids" type="{http://shopn.platform.nhncorp.com/}KidsSummaryType"/>
 *         &lt;element name="MusicalInstrument" type="{http://shopn.platform.nhncorp.com/}MusicalInstrumentSummaryType"/>
 *         &lt;element name="SportsEquipment" type="{http://shopn.platform.nhncorp.com/}SportsEquipmentSummaryType"/>
 *         &lt;element name="Books" type="{http://shopn.platform.nhncorp.com/}BooksSummaryType"/>
 *         &lt;element name="RentalEtc" type="{http://shopn.platform.nhncorp.com/}RentalEtcSummaryType"/>
 *         &lt;element name="DigitalContents" type="{http://shopn.platform.nhncorp.com/}DigitalContentsSummaryType"/>
 *         &lt;element name="GiftCard" type="{http://shopn.platform.nhncorp.com/}GiftCardSummaryType"/>
 *         &lt;element name="MobileCoupon" type="{http://shopn.platform.nhncorp.com/}MobileCouponSummaryType"/>
 *         &lt;element name="MovieShow" type="{http://shopn.platform.nhncorp.com/}MovieShowSummaryType"/>
 *         &lt;element name="EtcService" type="{http://shopn.platform.nhncorp.com/}EtcServiceSummaryType"/>
 *         &lt;element name="Etc" type="{http://shopn.platform.nhncorp.com/}EtcSummaryType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductSummaryType", propOrder = {
    "shoes",
    "wear",
    "bag",
    "fashionItems",
    "sleepingGear",
    "furniture",
    "imageAppliances",
    "homeAppliances",
    "seasonAppliances",
    "officeAppliances",
    "opticsAppliances",
    "microElectronics",
    "navigation",
    "carArticles",
    "medicalAppliances",
    "kitchenUtensils",
    "cosmetic",
    "jewellery",
    "food",
    "generalFood",
    "dietFood",
    "kids",
    "musicalInstrument",
    "sportsEquipment",
    "books",
    "rentalEtc",
    "digitalContents",
    "giftCard",
    "mobileCoupon",
    "movieShow",
    "etcService",
    "etc"
})
public class ProductSummaryType {
	
	@XmlElement(name = "Shoes")
    protected ShoesSummaryType shoes;	
	@XmlElement(name = "Wear")
    protected WearSummaryType wear;
	@XmlElement(name = "Bag")
    protected BagSummaryType bag;
	@XmlElement(name = "FashionItems")
    protected FashionItemsSummaryType fashionItems;
	@XmlElement(name = "SleepingGear")
    protected SleepingGearSummaryType sleepingGear;
	@XmlElement(name = "Furniture")
    protected FurnitureSummaryType furniture;
	@XmlElement(name = "ImageAppliances")
    protected ImageAppliancesSummaryType imageAppliances;
	@XmlElement(name = "HomeAppliances")
    protected HomeAppliancesSummaryType homeAppliances;
	@XmlElement(name = "SeasonAppliances")
    protected SeasonAppliancesSummaryType seasonAppliances;
	@XmlElement(name = "OfficeAppliances")
    protected OfficeAppliancesSummaryType officeAppliances;
	@XmlElement(name = "OpticsAppliances")
    protected OpticsAppliancesSummaryType opticsAppliances;
	@XmlElement(name = "MicroElectronics")
    protected MicroElectronicsSummaryType microElectronics;
	@XmlElement(name = "Navigation")
    protected NavigationSummaryType navigation;
	@XmlElement(name = "CarArticles")
    protected CarArticlesSummaryType carArticles;
	@XmlElement(name = "MedicalAppliances")
    protected MedicalAppliancesSummaryType medicalAppliances;
	@XmlElement(name = "KitchenUtensils")
    protected KitchenUtensilsSummaryType kitchenUtensils;
	@XmlElement(name = "Cosmetic")
    protected CosmeticSummaryType cosmetic;
	@XmlElement(name = "Jewellery")
    protected JewellerySummaryType jewellery;
	@XmlElement(name = "Food")
    protected FoodSummaryType food;
	@XmlElement(name = "GeneralFood")
    protected GeneralFoodSummaryType generalFood;
	@XmlElement(name = "DietFood")
    protected DietFoodSummaryType dietFood;
	@XmlElement(name = "Kids")
    protected KidsSummaryType kids;
	@XmlElement(name = "MusicalInstrument")
    protected MusicalInstrumentSummaryType musicalInstrument;
	@XmlElement(name = "SportsEquipment")
    protected SportsEquipmentSummaryType sportsEquipment;
	@XmlElement(name = "Books")
    protected BooksSummaryType books;
	@XmlElement(name = "RentalEtc")
    protected RentalEtcSummaryType rentalEtc;
	@XmlElement(name = "DigitalContents")
    protected DigitalContentsSummaryType digitalContents;
	@XmlElement(name = "GiftCard")
    protected GiftCardSummaryType giftCard;
	@XmlElement(name = "MobileCoupon")
    protected MobileCouponSummaryType mobileCoupon;
	@XmlElement(name = "MovieShow")
    protected MovieShowSummaryType movieShow;
	@XmlElement(name = "EtcService")
    protected EtcServiceSummaryType etcService;
	@XmlElement(name = "Etc")
    protected EtcSummaryType etc;
	
	public ShoesSummaryType getShoes() {
		return shoes;
	}
	public void setShoes(ShoesSummaryType shoes) {
		this.shoes = shoes;
	}
	public WearSummaryType getWear() {
		return wear;
	}
	public void setWear(WearSummaryType wear) {
		this.wear = wear;
	}
	public BagSummaryType getBag() {
		return bag;
	}
	public void setBag(BagSummaryType bag) {
		this.bag = bag;
	}
	public FashionItemsSummaryType getFashionItems() {
		return fashionItems;
	}
	public void setFashionItems(FashionItemsSummaryType fashionItems) {
		this.fashionItems = fashionItems;
	}
	public SleepingGearSummaryType getSleepingGear() {
		return sleepingGear;
	}
	public void setSleepingGear(SleepingGearSummaryType sleepingGear) {
		this.sleepingGear = sleepingGear;
	}
	public FurnitureSummaryType getFurniture() {
		return furniture;
	}
	public void setFurniture(FurnitureSummaryType furniture) {
		this.furniture = furniture;
	}
	public ImageAppliancesSummaryType getImageAppliances() {
		return imageAppliances;
	}
	public void setImageAppliances(ImageAppliancesSummaryType imageAppliances) {
		this.imageAppliances = imageAppliances;
	}
	public HomeAppliancesSummaryType getHomeAppliances() {
		return homeAppliances;
	}
	public void setHomeAppliances(HomeAppliancesSummaryType homeAppliances) {
		this.homeAppliances = homeAppliances;
	}
	public SeasonAppliancesSummaryType getSeasonAppliances() {
		return seasonAppliances;
	}
	public void setSeasonAppliances(SeasonAppliancesSummaryType seasonAppliances) {
		this.seasonAppliances = seasonAppliances;
	}
	public OfficeAppliancesSummaryType getOfficeAppliances() {
		return officeAppliances;
	}
	public void setOfficeAppliances(OfficeAppliancesSummaryType officeAppliances) {
		this.officeAppliances = officeAppliances;
	}
	public OpticsAppliancesSummaryType getOpticsAppliances() {
		return opticsAppliances;
	}
	public void setOpticsAppliances(OpticsAppliancesSummaryType opticsAppliances) {
		this.opticsAppliances = opticsAppliances;
	}
	public MicroElectronicsSummaryType getMicroElectronics() {
		return microElectronics;
	}
	public void setMicroElectronics(MicroElectronicsSummaryType microElectronics) {
		this.microElectronics = microElectronics;
	}
	public NavigationSummaryType getNavigation() {
		return navigation;
	}
	public void setNavigation(NavigationSummaryType navigation) {
		this.navigation = navigation;
	}
	public CarArticlesSummaryType getCarArticles() {
		return carArticles;
	}
	public void setCarArticles(CarArticlesSummaryType carArticles) {
		this.carArticles = carArticles;
	}
	public MedicalAppliancesSummaryType getMedicalAppliances() {
		return medicalAppliances;
	}
	public void setMedicalAppliances(MedicalAppliancesSummaryType medicalAppliances) {
		this.medicalAppliances = medicalAppliances;
	}
	public KitchenUtensilsSummaryType getKitchenUtensils() {
		return kitchenUtensils;
	}
	public void setKitchenUtensils(KitchenUtensilsSummaryType kitchenUtensils) {
		this.kitchenUtensils = kitchenUtensils;
	}
	public CosmeticSummaryType getCosmetic() {
		return cosmetic;
	}
	public void setCosmetic(CosmeticSummaryType cosmetic) {
		this.cosmetic = cosmetic;
	}
	public JewellerySummaryType getJewellery() {
		return jewellery;
	}
	public void setJewellery(JewellerySummaryType jewellery) {
		this.jewellery = jewellery;
	}
	public FoodSummaryType getFood() {
		return food;
	}
	public void setFood(FoodSummaryType food) {
		this.food = food;
	}
	public GeneralFoodSummaryType getGeneralFood() {
		return generalFood;
	}
	public void setGeneralFood(GeneralFoodSummaryType generalFood) {
		this.generalFood = generalFood;
	}
	public DietFoodSummaryType getDietFood() {
		return dietFood;
	}
	public void setDietFood(DietFoodSummaryType dietFood) {
		this.dietFood = dietFood;
	}
	public KidsSummaryType getKids() {
		return kids;
	}
	public void setKids(KidsSummaryType kids) {
		this.kids = kids;
	}
	public MusicalInstrumentSummaryType getMusicalInstrument() {
		return musicalInstrument;
	}
	public void setMusicalInstrument(MusicalInstrumentSummaryType musicalInstrument) {
		this.musicalInstrument = musicalInstrument;
	}
	public SportsEquipmentSummaryType getSportsEquipment() {
		return sportsEquipment;
	}
	public void setSportsEquipment(SportsEquipmentSummaryType sportsEquipment) {
		this.sportsEquipment = sportsEquipment;
	}
	public BooksSummaryType getBooks() {
		return books;
	}
	public void setBooks(BooksSummaryType books) {
		this.books = books;
	}
	public RentalEtcSummaryType getRentalEtc() {
		return rentalEtc;
	}
	public void setRentalEtc(RentalEtcSummaryType rentalEtc) {
		this.rentalEtc = rentalEtc;
	}
	public DigitalContentsSummaryType getDigitalContents() {
		return digitalContents;
	}
	public void setDigitalContents(DigitalContentsSummaryType digitalContents) {
		this.digitalContents = digitalContents;
	}
	public GiftCardSummaryType getGiftCard() {
		return giftCard;
	}
	public void setGiftCard(GiftCardSummaryType giftCard) {
		this.giftCard = giftCard;
	}
	public MobileCouponSummaryType getMobileCoupon() {
		return mobileCoupon;
	}
	public void setMobileCoupon(MobileCouponSummaryType mobileCoupon) {
		this.mobileCoupon = mobileCoupon;
	}
	public MovieShowSummaryType getMovieShow() {
		return movieShow;
	}
	public void setMovieShow(MovieShowSummaryType movieShow) {
		this.movieShow = movieShow;
	}
	public EtcServiceSummaryType getEtcService() {
		return etcService;
	}
	public void setEtcService(EtcServiceSummaryType etcService) {
		this.etcService = etcService;
	}
	public EtcSummaryType getEtc() {
		return etc;
	}
	public void setEtc(EtcSummaryType etc) {
		this.etc = etc;
	}
}
