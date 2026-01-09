package com.cware.partner.sync.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.cware.partner.sync.domain.id.PaEsmGoodskindsMappingId;
import com.cware.partner.sync.domain.id.PaGmktCategoryAddInfoId;
import com.cware.partner.sync.domain.id.PaLmsdId;

import lombok.Data;

@Data
@Entity
@Table(name="TPAESMGOODSKINDSMAPPING")
@IdClass(PaEsmGoodskindsMappingId.class)
public class PaEsmGoodskindsMapping {

	@Id
	private String siteGb;
	@Id
	private String lmsdCode;
	private String paLmsdnKey; //제휴사 상품분류 KEY
	private String PaIacLmsdKey;
	private String PaLmsdKey;
}