package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TPAEXCEPTENTP")
public class PaExceptEntp {

	@Id
	private String exceptSeq;
	private String entpCode;
	private String paGroupCodeAllYn;
	private String allBrandYn;
	private String sourcingMedia;
	
	private String paGroupCode;
	private String paCode;
	private String useYn;
	private String modifyId;
	private Timestamp modifyDate;
}