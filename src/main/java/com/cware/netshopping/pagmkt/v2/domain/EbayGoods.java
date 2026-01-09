package com.cware.netshopping.pagmkt.v2.domain;

import com.cware.netshopping.domain.model.PaGmktGoods;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;

public class EbayGoods extends PaGmktGoods {

	private static final long serialVersionUID = -1883136583888900283L;

	private PaGmktGoods gmktGoods; // 지마켓
	private PaGmktGoods iacGoods; // 옥션
	private PaGoodsPriceApply gmktPrice; // 지마켓 연동가
	private PaGoodsPriceApply iacPrice; // 옥션 연동가

	private boolean isDual; // 동시입점여부
	private boolean isGmkt; // 지마켓 입점여부
	private boolean isIac; // 옥션 입점여부
	private boolean isStartGmkt; // 지마켓 신규 입점여부
	private boolean isStartIac; // 옥션 신규 입점여부

	private String bundleNo;
	private String installYn;
	private String lmsdName;
	private String brandCode;
	private String gmktShipNo;
	private String delyGb;
	private double ordCost;
	private double returnCost;
	private double changeCost;

	private String imageUrl;
	private String imageP;
	private String imageAp;
	private String imageBp;
	private String imageCp;
	private String imageG;

	private String combinationYn;
	private String orderCreateYn;

	public PaGmktGoods getGmktGoods() {
		return gmktGoods;
	}

	public void setGmktGoods(PaGmktGoods gmktGoods) {
		this.gmktGoods = gmktGoods;
	}

	public PaGmktGoods getIacGoods() {
		return iacGoods;
	}

	public void setIacGoods(PaGmktGoods iacGoods) {
		this.iacGoods = iacGoods;
	}

	public PaGoodsPriceApply getGmktPrice() {
		return gmktPrice;
	}

	public void setGmktPrice(PaGoodsPriceApply gmktPrice) {
		this.gmktPrice = gmktPrice;
	}

	public PaGoodsPriceApply getIacPrice() {
		return iacPrice;
	}

	public void setIacPrice(PaGoodsPriceApply iacPrice) {
		this.iacPrice = iacPrice;
	}

	public boolean isDual() {
		return isDual;
	}

	public void setDual(boolean isDual) {
		this.isDual = isDual;
	}

	public boolean isGmkt() {
		return isGmkt;
	}

	public void setGmkt(boolean isGmkt) {
		this.isGmkt = isGmkt;
	}

	public boolean isIac() {
		return isIac;
	}

	public void setIac(boolean isIac) {
		this.isIac = isIac;
	}

	public boolean isStartGmkt() {
		return isStartGmkt;
	}

	public void setStartGmkt(boolean isStartGmkt) {
		this.isStartGmkt = isStartGmkt;
	}

	public boolean isStartIac() {
		return isStartIac;
	}

	public void setStartIac(boolean isStartIac) {
		this.isStartIac = isStartIac;
	}

	public String getBundleNo() {
		return bundleNo;
	}

	public void setBundleNo(String bundleNo) {
		this.bundleNo = bundleNo;
	}

	public String getInstallYn() {
		return installYn;
	}

	public void setInstallYn(String installYn) {
		this.installYn = installYn;
	}

	public String getLmsdName() {
		return lmsdName;
	}

	public void setLmsdName(String lmsdName) {
		this.lmsdName = lmsdName;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getGmktShipNo() {
		return gmktShipNo;
	}

	public void setGmktShipNo(String gmktShipNo) {
		this.gmktShipNo = gmktShipNo;
	}

	public String getDelyGb() {
		return delyGb;
	}

	public void setDelyGb(String delyGb) {
		this.delyGb = delyGb;
	}

	public double getOrdCost() {
		return ordCost;
	}

	public void setOrdCost(double ordCost) {
		this.ordCost = ordCost;
	}

	public double getReturnCost() {
		return returnCost;
	}

	public void setReturnCost(double returnCost) {
		this.returnCost = returnCost;
	}

	public double getChangeCost() {
		return changeCost;
	}

	public void setChangeCost(double changeCost) {
		this.changeCost = changeCost;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageP() {
		return imageP;
	}

	public void setImageP(String imageP) {
		this.imageP = imageP;
	}

	public String getImageAp() {
		return imageAp;
	}

	public void setImageAp(String imageAp) {
		this.imageAp = imageAp;
	}

	public String getImageBp() {
		return imageBp;
	}

	public void setImageBp(String imageBp) {
		this.imageBp = imageBp;
	}

	public String getImageCp() {
		return imageCp;
	}

	public void setImageCp(String imageCp) {
		this.imageCp = imageCp;
	}

	public String getCombinationYn() {
		return combinationYn;
	}

	public void setCombinationYn(String combinationYn) {
		this.combinationYn = combinationYn;
	}
	
	public String getImageG() {
		return imageG;
	}

	public void setImageG(String imageG) {
		this.imageG = imageG;
	}
	
	public String getOrderCreateYn() {
		return orderCreateYn;
	}

	public void setOrderCreateYn(String orderCreateYn) {
		this.orderCreateYn = orderCreateYn;
	}

	@Override
	public String toString() {
		return "EbayGoods [gmktGoods=" + gmktGoods + ", iacGoods=" + iacGoods + ", gmktPrice=" + gmktPrice
				+ ", iacPrice=" + iacPrice + ", isDual=" + isDual + ", isGmkt=" + isGmkt + ", isIac=" + isIac
				+ ", isStartGmkt=" + isStartGmkt + ", isStartIac=" + isStartIac + ", bundleNo=" + bundleNo
				+ ", installYn=" + installYn + ", lmsdName=" + lmsdName + ", brandCode=" + brandCode + ", gmktShipNo="
				+ gmktShipNo + ", delyGb=" + delyGb + ", ordCost=" + ordCost + ", returnCost=" + returnCost
				+ ", changeCost=" + changeCost + ", imageUrl=" + imageUrl + ", imageP=" + imageP + ", imageAp="
				+ imageAp + ", imageBp=" + imageBp + ", imageCp=" + imageCp + ", imageG=" + imageG + ", combinationYn="
				+ combinationYn + "]";
	}
	
}
