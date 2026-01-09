package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaSyncId implements Serializable {
	private static final long serialVersionUID = 2351535105380777955L;

	private String goodsCode;
	private long syncLogNo;
}