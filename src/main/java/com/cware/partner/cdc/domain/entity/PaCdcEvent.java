package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TPACDCEVENT")
public class PaCdcEvent {

	@Id
	private String cdcEvent;
	private Timestamp lastCdcDate;
}
