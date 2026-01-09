package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TPAPROMOEXCEPTBRAND")
public class PaPromoExceptBrand {

	@Id
	private String entpCode;
	private String brandSeq;
	private String brandCode;
	private String useYn;
	private Timestamp paExceptEdate;
	private Timestamp paExceptBdate; 
	private Timestamp modifyDate;

	
}