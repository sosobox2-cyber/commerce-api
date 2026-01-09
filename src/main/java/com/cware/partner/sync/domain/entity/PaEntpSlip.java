package com.cware.partner.sync.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.cware.partner.sync.domain.id.PaEntpUserId;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="TPAENTPSLIP")
@IdClass(PaEntpUserId.class)
public class PaEntpSlip {

	@Id
	private String entpCode;
	@Id
	private String entpManSeq;
	@Id
	private String paCode;

	private String transTargetYn;

	private Timestamp lastSyncDate;
	private Timestamp modifyDate;
	private String modifyId;

	public PaEntpSlip(String entpCode, String entpManSeq, String paCode) {
		this.entpCode = entpCode;
		this.entpManSeq = entpManSeq;
		this.paCode = paCode;
	}
}
