package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaGoodsAttrSeq implements Serializable {
	private static final long serialVersionUID = -8833953548953209369L;

	private String goodsCode;
	private String attributeSeq;
}