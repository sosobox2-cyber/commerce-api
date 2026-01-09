package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaHalfShipInfoId implements Serializable {
	private static final long serialVersionUID = 2560964373776475229L;
	private String paCode;
	private String entpCode;
	private String shipManSeq;
	private String returnManSeq;
	private String shipCostCode;
	private String noShipJejuIsland;
}