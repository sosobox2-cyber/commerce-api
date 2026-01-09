package com.cware.partner.sync.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.cware.partner.sync.domain.id.PaGoodsDtId;

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
@Table(name="TPAGOODSDTMAPPING")
@IdClass(PaGoodsDtId.class)
public class PaGoodsDtMapping {

	@Id
	private String paCode;
	@Id
	private String goodsCode;
	@Id
	private String goodsdtCode;

	private int transOrderAbleQty;
	private String transTargetYn;
	private String combinationYn;
	private Timestamp lastSyncDate;

	private String paOptionCode;
	private String remark1;
	private String remark2;
	private String transStockYn;

	@Column(updatable=false)
	private String insertId;
	@Column(updatable=false)
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;

	public PaGoodsDtMapping(String paCode, String goodsCode, String goodsdtCode, String paOptionCode, String transStockYn,
			int transOrderAbleQty) {
		this.paCode = paCode;
		this.goodsCode = goodsCode;
		this.goodsdtCode = goodsdtCode;
		this.paOptionCode = paOptionCode;
		this.transStockYn = transStockYn;
		this.transOrderAbleQty = transOrderAbleQty;
	}
}
