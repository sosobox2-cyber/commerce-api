package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoImageId implements Serializable {
	private static final long serialVersionUID = 5343889790774003553L;

	private String goodsCode;
	private int infoGoodsSeq;
}