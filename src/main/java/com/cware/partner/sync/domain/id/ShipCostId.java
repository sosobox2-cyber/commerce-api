package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipCostId implements Serializable {
	private static final long serialVersionUID = 7411923083400622102L;

	private String entpCode;
	private String shipCostCode;
}