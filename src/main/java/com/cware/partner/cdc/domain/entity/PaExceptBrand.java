package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.cware.partner.cdc.domain.id.PaExceptBrandId;

import lombok.Data;

@Data
@Entity
@IdClass(PaExceptBrandId.class)
@Table(name = "TPAEXCEPTBRAND")
public class PaExceptBrand {

	@Id
	private String exceptSeq;
	private String entpCode;
	@Id
	private String brandCode;
	private String useYn;
	private String modifyId;
	private Timestamp modifyDate;
}