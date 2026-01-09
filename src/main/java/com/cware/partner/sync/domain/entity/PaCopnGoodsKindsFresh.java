package com.cware.partner.sync.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.cware.partner.sync.domain.id.PaCopnGoodsKindsFreshId;

import lombok.Data;

@Data
@Entity
@Table(name="TPACOPNGOODSKINDSFRESH")
@IdClass(PaCopnGoodsKindsFreshId.class)
public class PaCopnGoodsKindsFresh {

	@Id
	private String lgroup;
	@Id
	private String mgroup;
	@Id
	private String sgroup;
	@Id
	private String dgroup;
	

}