package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.cware.partner.cdc.domain.id.PaCdcGoodsId;

import lombok.Data;

@Data
@Entity
@Table(name="TPACDCGOODSSNAPSHOT")
@IdClass(PaCdcGoodsId.class)
public class PaCdcGoodsSnapshot {

	@Id
	private String goodsCode;
	@Id
	private long cdcSnapshotNo;

	private Timestamp insertDate;
	private String processId;
}
