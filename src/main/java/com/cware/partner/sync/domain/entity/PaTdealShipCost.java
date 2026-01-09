package com.cware.partner.sync.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.cware.partner.sync.domain.id.PaTdealShipCostId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@DynamicInsert
@Table(name = "TPATDEALSHIPCOST")
@IdClass(PaTdealShipCostId.class)
public class PaTdealShipCost {
	@Id
	private String paCode;
	@Id
	private String entpCode;
	@Id
	private String shipManSeq;
	@Id
	private String returnManSeq;
	@Id
	private String shipCostCode;
	
	private int shipCostBaseAmt;
	private double ordCost;
	private double returnCost;
	private double changeCost;
	
	private double jejuCost;
	private double islandCost;
	private String transTargetYn;
	
	private Timestamp lastSyncDate;
	
	@Column(updatable=false)
	private String insertId;
	@Column(updatable=false)
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;
	

}
