package com.cware.partner.sync.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cware.partner.sync.domain.id.PaGmktCategoryAddInfoId;

import lombok.Data;

@Data
@Entity
@Table(name="TPAGMKTCATEGORYADDINFO")
@IdClass(PaGmktCategoryAddInfoId.class)
public class PaGmktCategoryAddInfo {

	public PaGmktCategoryAddInfo(String lmsdCode, String minMarginRate, String commission, String paCode, String promoMinMarginRate) {
		this.commission = commission;
		this.minMarginRate = minMarginRate;
		this.lmsdCode = lmsdCode;
		this.paCode = paCode;
		this.promoMinMarginRate = promoMinMarginRate;
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