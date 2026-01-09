package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TGOODS")
public class Goods {

	@Id
	private String goodsCode;
	private String goodsName;
	private String entpCode;
	private String brandCode;
	private String lmsdCode; 
	private String delyType;
	private String returnManSeq;
	private String shipCostCode;
	private String saleGb;
	private Timestamp saleEndDate;
	private Timestamp modifyDate;
	private String insertId;
	private String modifyId;

}
