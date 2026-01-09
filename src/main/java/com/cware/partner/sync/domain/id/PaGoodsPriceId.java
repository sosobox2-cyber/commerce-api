package com.cware.partner.sync.domain.id;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaGoodsPriceId implements Serializable {
	private static final long serialVersionUID = -6251936747491019425L;

	private String paCode;
	private String goodsCode;
	private Timestamp applyDate;
//	private String supplySeq;

}