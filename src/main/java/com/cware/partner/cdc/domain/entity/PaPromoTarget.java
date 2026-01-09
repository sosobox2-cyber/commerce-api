package com.cware.partner.cdc.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TPAPROMOTARGET")
public class PaPromoTarget {

	@Id
	private String promoNo;
	private String goodsCode;
	private String paCode;
	private String modifyId;
	private String procGb;
}