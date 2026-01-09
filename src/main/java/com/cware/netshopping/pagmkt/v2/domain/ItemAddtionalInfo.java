package com.cware.netshopping.pagmkt.v2.domain;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ItemAddtionalInfo {

	// 구매 수량 제한
	private BuyableQuantity buyableQuantity;

	// Document에 없는 파라미터
	private Map<String, Object> buyableQuantityPolicy;

	// 가격
	private Price price;

	// 재고
	private SiteIntValue stock;

	// 추천옵션 사용안함
	private Option recommendedOpts;

	// 주문 옵션
	private Option orderOpts;

	// 판매 기간
	private SiteIntValue sellingPeriod;

	// 판매자 상품코드 ( 백오피스 상품코드 )
	// 해당 상품의 판매자가 관리할 고유 코드 입력 or 자사몰 상품번호 입력
	private String managedCode;

	// (G마켓 상품용)
	// 상품 정보가 변경된 시점(상품정보 , 가격정보)에 등록된 code를 주문정보에 포함하여 전달함
	private String inventoryCode;

	// 판매자 카테고리, 브랜드
	private SellerShop sellerShop;

	// 유효일
	// 카테고리에 따라 필수 입력
	private String expiryDate;

	// 제조일
	// 카테고리에 따라 필수 입력
	// 도서상품일 경우, 도서출판년월일을 제조일에 입력
	private String manufacturedDate;

	// 원산지
	private Origin origin;

	// (옥션상품용)
	// 용량/규격 값
	private Map<String, Object> capacity;

	// 배송비
	private Shipping shipping;

	// 정보고시
	private OfficialNotice officialNotice;

	// 성인상품여부
	// true: 성인상품
	// false : 일반상품
	// 성인인증을 하기 전까지 상품이미지 비노출 처리
	private Boolean isAdultProduct;
	
	// 청소년구매불가여부
	// 성인인증 하기 전에도 상품이미지는 노출처리 되나, 구매시점 성인인증 필요
	// Ex) 주류 등
	// true: 청소년구매불가상품, false : 일반상품
	private Boolean isYouthNotAvailable;
	
	// 부과세여부
	// 면세상품일 경우에만 true로 설정, 일반상품은 false로 설정
	// true : 부과세면세상품, false : 부과세과세상품
	private Boolean isVatFree;

	private CertInfo certInfo;
	private Images images;
	private Double weight;

	// 상품 상세 정보
	private Descriptions descriptions;

	private Map<String, Object> addonService;
	private Map<String, Object> skuInfo;
	private String goodsStatus;
	private Object preSaleShippingDate;

	public BuyableQuantity getBuyableQuantity() {
		return buyableQuantity;
	}

	public void setBuyableQuantity(BuyableQuantity buyableQuantity) {
		this.buyableQuantity = buyableQuantity;
	}

	public Map<String, Object> getBuyableQuantityPolicy() {
		return buyableQuantityPolicy;
	}

	public void setBuyableQuantityPolicy(Map<String, Object> buyableQuantityPolicy) {
		this.buyableQuantityPolicy = buyableQuantityPolicy;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public SiteIntValue getStock() {
		return stock;
	}

	public void setStock(SiteIntValue stock) {
		this.stock = stock;
	}

	public Option getRecommendedOpts() {
		return recommendedOpts;
	}

	public void setRecommendedOpts(Option recommendedOpts) {
		this.recommendedOpts = recommendedOpts;
	}

	public Option getOrderOpts() {
		return orderOpts;
	}

	public void setOrderOpts(Option orderOpts) {
		this.orderOpts = orderOpts;
	}

	public SiteIntValue getSellingPeriod() {
		return sellingPeriod;
	}

	public void setSellingPeriod(SiteIntValue sellingPeriod) {
		this.sellingPeriod = sellingPeriod;
	}

	public String getManagedCode() {
		return managedCode;
	}

	public void setManagedCode(String managedCode) {
		this.managedCode = managedCode;
	}

	public String getInventoryCode() {
		return inventoryCode;
	}

	public void setInventoryCode(String inventoryCode) {
		this.inventoryCode = inventoryCode;
	}

	public SellerShop getSellerShop() {
		return sellerShop;
	}

	public void setSellerShop(SellerShop sellerShop) {
		this.sellerShop = sellerShop;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getManufacturedDate() {
		return manufacturedDate;
	}

	public void setManufacturedDate(String manufacturedDate) {
		this.manufacturedDate = manufacturedDate;
	}

	public Origin getOrigin() {
		return origin;
	}

	public void setOrigin(Origin origin) {
		this.origin = origin;
	}

	public Map<String, Object> getCapacity() {
		return capacity;
	}

	public void setCapacity(Map<String, Object> capacity) {
		this.capacity = capacity;
	}

	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	public OfficialNotice getOfficialNotice() {
		return officialNotice;
	}

	public void setOfficialNotice(OfficialNotice officialNotice) {
		this.officialNotice = officialNotice;
	}

	@JsonProperty("isAdultProduct")
	public Boolean isAdultProduct() {
		return isAdultProduct;
	}

	@JsonProperty("isAdultProduct")
	public void setAdultProduct(Boolean isAdultProduct) {
		this.isAdultProduct = isAdultProduct;
	}

	@JsonProperty("isYouthNotAvailable")
	public Boolean isYouthNotAvailable() {
		return isYouthNotAvailable;
	}

	@JsonProperty("isYouthNotAvailable")
	public void setYouthNotAvailable(Boolean isYouthNotAvailable) {
		this.isYouthNotAvailable = isYouthNotAvailable;
	}

	@JsonProperty("isVatFree")
	public Boolean isVatFree() {
		return isVatFree;
	}

	@JsonProperty("isVatFree")
	public void setVatFree(Boolean isVatFree) {
		this.isVatFree = isVatFree;
	}

	public CertInfo getCertInfo() {
		return certInfo;
	}

	public void setCertInfo(CertInfo certInfo) {
		this.certInfo = certInfo;
	}

	public Images getImages() {
		return images;
	}

	public void setImages(Images images) {
		this.images = images;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Descriptions getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Descriptions descriptions) {
		this.descriptions = descriptions;
	}

	public Map<String, Object> getAddonService() {
		return addonService;
	}

	public void setAddonService(Map<String, Object> addonService) {
		this.addonService = addonService;
	}

	public Map<String, Object> getSkuInfo() {
		return skuInfo;
	}

	public void setSkuInfo(Map<String, Object> skuInfo) {
		this.skuInfo = skuInfo;
	}

	public String getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(String goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public Object getPreSaleShippingDate() {
		return preSaleShippingDate;
	}

	public void setPreSaleShippingDate(Object preSaleShippingDate) {
		this.preSaleShippingDate = preSaleShippingDate;
	}

	@Override
	public String toString() {
		return "ItemAddtionalInfo [buyableQuantity=" + buyableQuantity + ", buyableQuantityPolicy="
				+ buyableQuantityPolicy + ", price=" + price + ", stock=" + stock + ", recommendedOpts="
				+ recommendedOpts + ", orderOpts=" + orderOpts + ", sellingPeriod=" + sellingPeriod + ", managedCode="
				+ managedCode + ", inventoryCode=" + inventoryCode + ", sellerShop=" + sellerShop + ", expiryDate="
				+ expiryDate + ", manufacturedDate=" + manufacturedDate + ", origin=" + origin + ", capacity="
				+ capacity + ", shipping=" + shipping + ", officialNotice=" + officialNotice + ", isAdultProduct="
				+ isAdultProduct + ", isYouthNotAvailable=" + isYouthNotAvailable + ", isVatFree=" + isVatFree
				+ ", certInfo=" + certInfo + ", images=" + images + ", weight=" + weight + ", descriptions="
				+ descriptions + ", addonService=" + addonService + ", skuInfo=" + skuInfo + ", goodsStatus="
				+ goodsStatus + ", preSaleShippingDate=" + preSaleShippingDate + "]";
	}

}
