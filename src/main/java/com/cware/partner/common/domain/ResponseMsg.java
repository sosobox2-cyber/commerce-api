package com.cware.partner.common.domain;

import lombok.Data;

@Data
public class ResponseMsg {
	int status;
	String code;
	String message;
	String type;
}
