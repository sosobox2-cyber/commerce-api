package com.cware.partner.common.domain.id;

import java.io.Serializable;

import lombok.Data;

@Data
public class CodeId implements Serializable {
	private static final long serialVersionUID = -7219273184607042589L;

	private String codeLgroup;
	private String codeMgroup;
}