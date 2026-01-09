package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TPACUSTSHIPCOST")
public class PaCustShipCost {

	@Id
	private String entpCode;
	private String shipCostCode;
	private Timestamp applyDate;
	private Timestamp modifyDate;
}
