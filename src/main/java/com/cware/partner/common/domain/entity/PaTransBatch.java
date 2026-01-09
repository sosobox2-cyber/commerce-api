package com.cware.partner.common.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.Data;

@Data
@Entity
@DynamicInsert
@Table(name="TPATRANSBATCH")
public class PaTransBatch {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTransBatchNo")
	@SequenceGenerator(sequenceName = "SEQ_TRANS_BATCH_NO", name = "seqTransBatchNo", allocationSize = 1)
	private long transBatchNo;

	private String paGroupCode;
	private String batchName;
	private String batchNote;
	private int targetCnt;
	private int procCnt;
	private int filterCnt;
	private int successCnt;
	private int failCnt;
	private Timestamp startDate;
	private Timestamp endDate;
	private String processId;

}
