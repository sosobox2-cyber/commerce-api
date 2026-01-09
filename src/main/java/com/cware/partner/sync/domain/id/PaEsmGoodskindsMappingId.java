package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaEsmGoodskindsMappingId implements Serializable {

	private static final long serialVersionUID = 5011504112655835123L;
	private String lmsdCode;
	private String siteGb;
}