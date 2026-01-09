package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
//@Table(name="TPROMOTARGETLOG")
public class PromoTargetLog {

	@Id
	private String promoNo;
	private String promoSeq;
	private String goodsCode;
	private Timestamp logDate;
}
