package com.cware.partner.cdc.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TPAGOODSTARGET")
public class PaGoodsTarget {

	@Id
	private String goodsCode;
	private String paGroupCode;
	private String paCode;
	private String paSaleGb;
	private String paGoodsCode;
}
