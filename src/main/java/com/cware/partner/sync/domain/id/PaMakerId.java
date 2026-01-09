package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaMakerId implements Serializable {
	private static final long serialVersionUID = -7980612377596799583L;

	private String paGroupCode;
	private String makerCode;
}