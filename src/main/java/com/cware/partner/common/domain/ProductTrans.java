package com.cware.partner.common.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductTrans {
	String goodsCode;
	String paCode;
	int procCnt;
	boolean updated;
}
