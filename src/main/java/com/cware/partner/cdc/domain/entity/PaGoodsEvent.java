package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TPAGOODSEVENT")
public class PaGoodsEvent {

	@Id
	private String goodsCode;
	private Timestamp startDate;
	private Timestamp endDate;
	private String useYn;
	private String modifyId;
	private Timestamp modifyDate;

}
