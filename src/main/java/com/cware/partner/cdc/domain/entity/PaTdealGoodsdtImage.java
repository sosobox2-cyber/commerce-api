package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.cware.partner.cdc.domain.id.PaTdealGoodsdtImageId;

import lombok.Data;

@Data
@Entity
@Table(name = "TPATDEALGOODSDTIMAGE")
@IdClass(PaTdealGoodsdtImageId.class)
public class PaTdealGoodsdtImage {

	@Id
	private String goodsCode;
	@Id
	private String goodsdtCode;
	private String imagePath;
	private String imageFile;
	private String modifyId;
	private Timestamp modifyDate;
}