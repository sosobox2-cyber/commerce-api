package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaOfferId implements Serializable {

	private static final long serialVersionUID = -4636154581818229093L;
	private String paGroupCode;
	private String paOfferType;
	private String paOfferCode;

}