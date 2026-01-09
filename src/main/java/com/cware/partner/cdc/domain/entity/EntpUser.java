package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TENTPUSER")
public class EntpUser {

	@Id
	private String entpCode;
	private String entpManSeq;
	private String entpManGb;
	private Timestamp modifyDate;
}
