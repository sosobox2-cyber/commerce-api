package com.cware.partner.sync.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

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
@Table(name = "TPATDEALEVENT")
public class PaTdealEvent {
	@Id
	private String eventNo;
	private String goodsCode;
	private String goodsEventName;
	private String promoYn;
	private String dispCatId;
	private String paLmsdKey;
	private Timestamp saleStartDate;
	private Timestamp saleEndDate;
	private Timestamp eventBdate;
	private Timestamp eventEdate;
	private String useYn;
	private String priceModifyAllowYn;
	
	@Column(updatable=false)
	private String insertId;
	@Column(updatable=false)
	private String modifyId;
	private Timestamp modifyDate;
	
	private Timestamp lastSyncDate;
	
	public PaTdealEvent(String goodsCode,String eventNo, String paLmsdKey) {
		this.goodsCode = goodsCode;
		this.eventNo = eventNo;		
		this.paLmsdKey = paLmsdKey;
	}
	
}
