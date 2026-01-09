package com.cware.partner.sync.domain.id;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaTmonShipCostId implements Serializable {

	private static final long serialVersionUID = 6390733657165163699L;

	private String paCode;
	private String entpCode;
	private String productType;
	private String shipManSeq;
	private String returnManSeq;
	private String shipCostCode;
	private Timestamp applyDate;
	private String noShipIsland;
}