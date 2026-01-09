package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TPAPROMOEXCEPTENTP")
public class PaPromoExceptEntp {

	@Id
	private String entpCode;
	private String paGroupCodeAllYn;
	private String paGroupCode;
	private String allBrandYn;
	private String useYn;
	private String paCode;
	private Timestamp paExceptEdate;
	private Timestamp paExceptBdate; 
	private Timestamp modifyDate;

}
