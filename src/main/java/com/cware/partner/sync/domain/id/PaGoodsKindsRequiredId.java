package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaGoodsKindsRequiredId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8940637342566785844L;
	private String paGroupCode;
	private String lmsdCode;
	private String requiredGb;
}