package com.cware.partner.sync.domain.id;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaGoodsPriceApplyId implements Serializable {
	private static final long serialVersionUID = 8378036511695487717L;

	private String goodsCode;
	private String paGroupCode;
	private String paCode;
	private Timestamp applyDate;
	private int priceApplySeq;
}