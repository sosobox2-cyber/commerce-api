package com.cware.partner.coupang.domain;

import java.util.Map;

import com.cware.partner.common.domain.ResponseMsg;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApiResponseMsg extends ResponseMsg {
	Map<String, Object> data;
}
