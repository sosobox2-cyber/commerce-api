package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.Data;

@Data
public class PaPromoMarginExceptId implements Serializable {
	private static final long serialVersionUID = -8597044978109862125L;
	
	private String goodsCode;
	private String paGroupCode;
}
