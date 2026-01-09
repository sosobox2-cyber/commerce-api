package com.cware.netshopping.pafaple.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class SBrandVO  extends SBrand {
	
	private String paCode;
	private String entpCode;
	private String brandCode;
	private String shipCostCode;
	private Integer ordCost;
	private Integer returnCost;
	private Integer islandCost;
	private Integer islandReturnCost;
	private String brandId;
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getEntpCode() {
		return entpCode;
	}
	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getShipCostCode() {
		return shipCostCode;
	}
	public void setShipCostCode(String shipCostCode) {
		this.shipCostCode = shipCostCode;
	}
	public Integer getOrdCost() {
		return ordCost;
	}
	public void setOrdCost(Integer ordCost) {
		this.ordCost = ordCost;
	}
	public Integer getReturnCost() {
		return returnCost;
	}
	public void setReturnCost(Integer returnCost) {
		this.returnCost = returnCost;
	}
	public Integer getIslandCost() {
		return islandCost;
	}
	public void setIslandCost(Integer islandCost) {
		this.islandCost = islandCost;
	}
	public Integer getIslandReturnCost() {
		return islandReturnCost;
	}
	public void setIslandReturnCost(Integer islandReturnCost) {
		this.islandReturnCost = islandReturnCost;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	
}
