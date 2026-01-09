package com.cware.partner.sync.domain.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TPAQEENGOODSKINDSOFFER")
public class PaQeenGoodsKindsOffer {

	@Id
	private String paLmsdKey;
	private String categoryId;//CATEGORY_ID
	private String announcementType;//ANNOUNCEMENT_TYPE
	
}
