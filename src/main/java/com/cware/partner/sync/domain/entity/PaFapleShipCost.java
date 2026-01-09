package com.cware.partner.sync.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.cware.partner.sync.domain.id.PaFapleShipCostId;

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
@Table(name = "TPAFAPLESHIPCOST")
@IdClass(PaFapleShipCostId.class)
public class PaFapleShipCost {
	@Id
	private String paCode;
	@Id
	private String entpCode;
	@Id
	private String brandCode;
	@Id
	private String shipManSeq;
	@Id
	private String returnManSeq;
	@Id
	private String shipCostCode;
	
	private String saleBrandName;//실적브랜드명
	private String brandName; //진열브랜드명
	
	@Column(updatable=false)
	private String brandId;
	
	private int shipCostBaseAmt;
	private double ordCost;
	private double returnCost;
	private double islandCost; // 도서산간제주 배송비
	private double islandReturnCost;// 도서산간제주 반품배송비
	
	@Column(updatable=false)
	private String paLgroupName;
	
	private String transTargetYn;
	
	private Timestamp lastSyncDate;
	private Timestamp lastEntpSyncDate;
	
	@Column(updatable=false)
	private String insertId;
	@Column(updatable=false)
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;
	
}
