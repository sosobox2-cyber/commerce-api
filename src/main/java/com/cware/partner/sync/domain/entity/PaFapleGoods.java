package com.cware.partner.sync.domain.entity;


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
@Table(name = "TPAFAPLEGOODS")
public class PaFapleGoods extends PartnerGoods {
	private String itemId;
	private String goodsName;
	private String paLmsdKey;

	private String brandId;
	
	private String brandChangeYn;
	
}