package com.cware.partner.sync.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.cware.partner.sync.domain.id.PaQeenShipCostId;

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
@Table(name = "TPAQEENSHIPCOST")
@IdClass(PaQeenShipCostId.class)
public class PaQeenShipCost {
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
	
	private String paShipcostName;//제휴사배송정책이름
	private String brandCodes; //브랜드코드
	
	private int shipCostBaseAmt;
	private double ordCost;
	private double returnCost;
	private double islandCost; // 도서산간 배송비
	private double islandReturnCost;// 도서산간 추가 반품비
	private double jejuCost; //제주 배송비
	private double jejuReturnCost;// 제주 추가 반품비
	
	private String transTargetYn;
	
	private Timestamp lastSyncDate;
	private Timestamp lastEntpSyncDate;
	
	@Column(updatable=false)
	private String insertId;
	@Column(updatable=false)
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;
	
	public PaQeenShipCost(double islandReturnCost, double jejuReturnCost) {
		this.islandReturnCost = islandReturnCost;
		this.jejuReturnCost = jejuReturnCost;
	
	}
	
}
