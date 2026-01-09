package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TPASALENOGOODS")
public class PaSaleNoGoods {

	@Id
	private String goodsCode;
	private String paGroupCode;
	private String paCode;
	private String paSaleGb;

	private String insertId;
	private Timestamp insertDate;

}