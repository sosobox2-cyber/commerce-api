package com.cware.partner.sync.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.cware.partner.sync.domain.id.PaLmsdId;

import lombok.Data;

@Data
@Entity
@Table(name="TPAGOODSKINDSMAPPING")
@IdClass(PaLmsdId.class)
public class PaGoodsKindsMapping {

	@Id
	private String paGroupCode;
	@Id
	private String lmsdCode;
	private String paLmsdKey; //제휴사 상품분류 KEY

}