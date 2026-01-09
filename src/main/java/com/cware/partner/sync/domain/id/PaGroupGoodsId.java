package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaGroupGoodsId implements Serializable {
	private static final long serialVersionUID = -2656551293286547075L;

	private String paGroupCode;
	private String goodsCode;
}