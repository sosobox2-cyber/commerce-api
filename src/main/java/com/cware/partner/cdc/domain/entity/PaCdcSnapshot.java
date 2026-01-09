package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TPACDCSNAPSHOT")
public class PaCdcSnapshot {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqCdcSnapshotNo")
	@SequenceGenerator(sequenceName = "SEQ_CDC_SNAPSHOT_NO", name = "seqCdcSnapshotNo", allocationSize = 1)
	private long cdcSnapshotNo;
	private String cdcReasonCode;
	private int targetCnt;
	private int cdcCnt;
	private Timestamp startDate;
	private Timestamp endDate;
}
