package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaPromoId implements Serializable {
	private static final long serialVersionUID = 320036461279924244L;

	private String promoNo;
	private String paCode;
	private String goodsCode;
	private String seq;
}