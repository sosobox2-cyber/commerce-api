package com.cware.partner.common.domain;

import lombok.Data;

@Data
public class ResponseMsg {
	String code;
	String message;
	String type;
	int status;
}
