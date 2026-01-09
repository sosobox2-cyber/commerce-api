package com.cware.partner.sync.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TPAPROMOTARGETEXCEPT")
public class PaPromoTargetExcept {

	@Id
	private String targetGb;
	private String targetCode;
	private String targetSeq;
	private String useYn;
	private Timestamp modifyDate;
	private Timestamp paExceptEdate;
	private Timestamp paExceptBdate;
	private double paExceptMargin;
}