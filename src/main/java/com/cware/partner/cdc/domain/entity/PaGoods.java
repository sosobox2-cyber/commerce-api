package com.cware.partner.cdc.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TPAPRODUCT")
public class PaGoods {

	@Id
	private String goodsCode;
	private String saleGb;
}
