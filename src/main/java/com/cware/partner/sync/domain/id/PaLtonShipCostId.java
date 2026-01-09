package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaLtonShipCostId implements Serializable {
	private static final long serialVersionUID = -1242807260666449504L;

	private String paCode;
	private double jejuCost;
	private double islandCost;
}