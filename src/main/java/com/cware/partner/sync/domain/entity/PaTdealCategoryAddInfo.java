package com.cware.partner.sync.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cware.partner.sync.domain.id.PaTdealCategoryAddInfoId;

import lombok.Data;

@Data
@Entity
@Table(name="TPATDEALCATEGORYADDINFO")
@IdClass(PaTdealCategoryAddInfoId.class)
public class PaTdealCategoryAddInfo {

	public PaTdealCategoryAddInfo(String lmsdCode, String minMarginRate, String commission, String promoMinMarginRate, String paLmsdKey, String paCode) {
		this.commission = commission;
		this.minMarginRate = minMarginRate;
		this.lmsdCode = lmsdCode;
		this.promoMinMarginRate = promoMinMarginRate;
		this.paLmsdKey = paLmsdKey;
		this.paCode = paCode;
	}
	
	public PaTdealCategoryAddInfo(String minMarginRate, String commission, String promoMinMarginRate, String paLmsdKey, String paCode) {
		this.commission = commission;
		this.minMarginRate = minMarginRate;
		this.promoMinMarginRate = promoMinMarginRate;
		this.paLmsdKey = paLmsdKey;
		this.paCode = paCode;
	}
	
	@Id
	private String paGroupCode;
	@Id
	private String paLmsdKey;
	@Id
	private String paCode;
	
	private String commission;
	private String minMarginRate;
	private String promoMinMarginRate;
	
	@Transient
	private String lmsdCode;
	

}