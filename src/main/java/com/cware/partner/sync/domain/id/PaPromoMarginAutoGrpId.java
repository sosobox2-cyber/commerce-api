package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaPromoMarginAutoGrpId implements Serializable {

	private static final long serialVersionUID = 3578674539755677295L;
	private String eventNo;
	private String targetGb;
	private String targetCode;
}