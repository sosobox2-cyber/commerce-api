package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TINPLANQTY")
public class InPlanQty {

	@Id
	private String goodsCode;
	private String goodsdtCode;
	private String seq;
	private String modifyId;
	private Timestamp startDate;
	private Timestamp endDate;
	private Timestamp modifyDate;
}
