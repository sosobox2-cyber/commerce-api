package com.cware.partner.common.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.cware.partner.common.domain.id.PaTransApiId;

import lombok.Data;

@Data
@Entity
@DynamicInsert
@Table(name="TPATRANSAPI")
@IdClass(PaTransApiId.class)
public class PaTransApi {

	@Id
	private String transCode;
	@Id
	private String transType;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTransApiNo")
	@SequenceGenerator(sequenceName = "SEQ_TRANS_API_NO", name = "seqTransApiNo", allocationSize = 1)
	private long transApiNo;

	private String apiName;
	private String apiUrl;
	private String apiNote;
	private String requestHeader;
	private String requestPayload;
	private Timestamp requestDate;
	private String responsePayload;
	private Timestamp responseDate;
	private String successYn;
	private String resultCode;
	private String resultMsg;
	private long transServiceNo;
	private String paGroupCode;
	private String processId;

}
