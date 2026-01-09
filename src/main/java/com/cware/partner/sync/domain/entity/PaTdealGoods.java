package com.cware.partner.sync.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@Table(name = "TPATDEALGOODS")
public class PaTdealGoods extends PartnerGoods {
	private String mallProductNo;
	private String goodsName;
	private String paLmsdKey;

	private String paBrandNo;
	private String dispCatId;
	private String confirmStatus;
	private String originCode;
	
	private Timestamp lastDtimageSyncDate;
}