package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaLmsdId implements Serializable {
	private static final long serialVersionUID = 779112241996236283L;

	private String paGroupCode;
	private String lmsdCode;
}