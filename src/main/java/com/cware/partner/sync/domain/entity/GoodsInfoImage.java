package com.cware.partner.sync.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.cware.partner.sync.domain.id.GoodsInfoImageId;

import lombok.Data;

@Data
@Entity
@Table(name="TGOODSINFOIMAGE")
@IdClass(GoodsInfoImageId.class)
public class GoodsInfoImage {

	@Id
	private String goodsCode;
	@Id
	private int infoGoodsSeq;
	private String infoImageType;
	private String infoImageFile1;
	private String infoImageFile;
	private Timestamp modifyDate;
}
