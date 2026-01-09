package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TGOODSPRICE")
//@Where(clause = "apply_date <= sysdate ")
public class GoodsPrice {

	@Id
	private String goodsCode;
	private Timestamp applyDate;
	private double salePrice;
	private String insertId;
}
