package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TORDERSTOCK")
public class OrderStock {

	@Id
	private String goodsCode;
	private String goodsdtCode;
	private String whCode;
	private String modifyId;
	private Timestamp modifyDate;
}
