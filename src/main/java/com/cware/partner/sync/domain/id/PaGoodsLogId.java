package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaGoodsLogId implements Serializable {
	private static final long serialVersionUID = 6883506311745876012L;

	private String paCode;
	private String goodsCode;
	private String seq;
}