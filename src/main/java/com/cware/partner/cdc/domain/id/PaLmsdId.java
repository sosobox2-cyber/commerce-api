package com.cware.partner.cdc.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaLmsdId implements Serializable {

	private static final long serialVersionUID = 5582610253030916484L;
	
	private String paGroupCode;
	private String lmsdCode;
	
}