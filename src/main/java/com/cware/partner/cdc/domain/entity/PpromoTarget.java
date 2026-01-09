package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TPPROMOTARGET")
public class PpromoTarget {

	@Id
	private String promoNo;
	private String promoSeq;
	private String goodsCode;
	private String modifyId;
	private Timestamp modifyDate;
}
