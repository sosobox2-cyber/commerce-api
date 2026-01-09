package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TGOODSDT")
public class GoodsDt {

	@Id
	private String goodsCode;
	private String goodsdtCode;
	private String goodsName;
	private String goodsdtInfo;
	private String saleGb;
	private String modifyId;
	private Timestamp modifyDate;
}
