package com.cware.partner.cdc.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TGOODSADDINFO")
//@Where(clause = "em_goods_yn = '0' and global_dely_yn = '0' and dawn_yn = '0'")
public class GoodsAddInfo {

	@Id
	private String goodsCode;
	private String emGoodsYn;
	private String globalDelyYn;
	private String dawnYn;

}
