package com.cware.partner.sync.domain.id;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaPromoPriceId implements Serializable {
	private static final long serialVersionUID = 5752942279782482969L;

	private String goodsCode;
	private String paCode;
	private Timestamp applyDate;
	private String promoSeq;
	private String alcoutPromoYn;
}