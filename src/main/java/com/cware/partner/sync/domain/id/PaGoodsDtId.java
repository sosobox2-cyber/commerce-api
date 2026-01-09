package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaGoodsDtId implements Serializable {
	private static final long serialVersionUID = 1016856778885610118L;
	private String paCode;
	private String goodsCode;
	private String goodsdtCode;
}