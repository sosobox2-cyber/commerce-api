package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaOriginId implements Serializable {
	private static final long serialVersionUID = 9114783684042265965L;

	private String paGroupCode;
	private String originCode;
}