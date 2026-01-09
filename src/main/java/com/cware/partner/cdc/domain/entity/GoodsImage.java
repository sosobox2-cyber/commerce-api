package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TGOODSIMAGE")
public class GoodsImage {

	@Id
	private String goodsCode;
	private String imageNo;
	private String modifyId;
	private Timestamp modifyDate;
}
