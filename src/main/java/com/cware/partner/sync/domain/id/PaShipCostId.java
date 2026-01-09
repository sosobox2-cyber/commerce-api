package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaShipCostId implements Serializable {
	private static final long serialVersionUID = -8930758577362034123L;

	private String paCode;
	private String entpCode;
	private String shipCostCode;
}