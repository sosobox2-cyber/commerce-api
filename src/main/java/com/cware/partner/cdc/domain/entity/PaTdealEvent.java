package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;

@Data
@Entity
@Table(name = "TPATDEALEVENT")
public class PaTdealEvent {

	@Id
	private String eventNo;
	private String goodsCode;
	private String goodsEventName;
	private String promoYn;
	private String dispCatId;
	private Timestamp saleStartDate;
	private Timestamp saleEDate;
	private Timestamp eventBdate;
	private Timestamp eventEdate;
	private String useYn;
	private String insertId;
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;
	

}