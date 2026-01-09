package com.cware.partner.cdc.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaPromoMarginAutoGrpId implements Serializable {

	private static final long serialVersionUID = -8326203321191369134L;
	private String eventNo;
	private String targetGb;
	private String targetCode;
}