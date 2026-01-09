package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TOFFER")
public class Offer {

	@Id
	private String goodsCode;
	private String offerType;
	private String offerCode;
	private String offerName;
	private String offerContents;
	private String useYn;
	private String modifyId;
	private Timestamp modifyDate;
}
