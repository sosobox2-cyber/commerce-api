package com.cware.partner.cdc.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaTdealGoodsdtImageId implements Serializable {
	private static final long serialVersionUID = 4774952212454859953L;
	
	private String goodsCode;
	private String goodsdtCode;
}