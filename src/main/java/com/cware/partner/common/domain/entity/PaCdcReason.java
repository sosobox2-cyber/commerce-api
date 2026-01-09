package com.cware.partner.common.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TPACDCREASON")
public class PaCdcReason {

	@Id
	private String cdcReasonCode;
	private String cdcReasonName;
	private String cdcReasonNote;
	private String cdcEvent;
	private int cdcBoosting;

	private Timestamp lastCdcDate;
	private String useYn;
}
