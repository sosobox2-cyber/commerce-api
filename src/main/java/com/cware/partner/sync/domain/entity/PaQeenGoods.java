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
@Table(name = "TPAQEENGOODS")
public class PaQeenGoods extends PartnerGoods {
	private String reifiedProductId;//REIFIED_PRODUCT_ID
	private String productProposalId;//PRODUCT_PROPOSAL_ID
	private String goodsName;
	private String paLmsdKey;
	
	private String paBrandCode;

}