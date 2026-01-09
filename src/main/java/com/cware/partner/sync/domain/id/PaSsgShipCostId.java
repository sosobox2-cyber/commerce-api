package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaSsgShipCostId implements Serializable {
	private static final long serialVersionUID = -5196548624228510563L;

	private String paCode;
	private String shppcstAplUnitCd;
	private String shppcstPlcyDivCd;
	private String collectYn;
	private int shipCostBaseAmt;
	private int shipCost;
}