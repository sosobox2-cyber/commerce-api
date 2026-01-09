package com.cware.partner.coupang.domain;

import com.cware.partner.common.domain.ResponseMsg;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProductStatusHistory extends ResponseMsg {
	String nextToken;
	ProductStatus[] data;
}
