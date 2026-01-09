package com.cware.partner.common.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;

import com.cware.partner.common.domain.id.PaTransServiceId;

import lombok.Data;

@Data
@Entity
@DynamicInsert
@Table(name="TPATRANSSERVICE")
@IdClass(PaTransServiceId.class)
public class PaTransService {

	@Id
	private String transCode;
	@Id
	private String transType;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTransServiceNo")
	@SequenceGenerator(sequenceName = "SEQ_TRANS_SERVICE_NO", name = "seqTransServiceNo", allocationSize = 1)
	private long transServiceNo;

	private String serviceName;
	private String serviceNote;
	private String successYn;
	private String resultCode;
	private String resultMsg;
	private long transBatchNo;
	private String paGroupCode;
	private Timestamp startDate;
	private Timestamp endDate;
	private String processId;

	@Transient
	private int status; // 1:처리, 0:대상아님(필터), -1:실패
}
