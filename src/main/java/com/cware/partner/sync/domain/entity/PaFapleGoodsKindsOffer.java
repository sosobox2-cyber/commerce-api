package com.cware.partner.sync.domain.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity		
@Table(name = "TPAFAPLEGOODSKINDSOFFER")
public class PaFapleGoodsKindsOffer {
	
	@Id
	private String paLmsdKey;
	private String paOfferType;
	
}
