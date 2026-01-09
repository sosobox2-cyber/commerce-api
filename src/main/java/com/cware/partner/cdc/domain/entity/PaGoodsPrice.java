package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TPAGOODSPRICE")
public class PaGoodsPrice {

	@Id
	private String goodsCode;
	private String paCode;
	private Timestamp applyDate;
	private double salePrice;

	private Timestamp modifyDate;
}
