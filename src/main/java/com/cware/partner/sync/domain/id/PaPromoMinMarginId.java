package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaPromoMinMarginId implements Serializable {
	private static final long serialVersionUID = 290177559760292450L;
	private String paGroupCode;
	private String paCode;

}