package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaSaleNoId implements Serializable {
	private static final long serialVersionUID = 2517923324613242171L;

	private String paGroupCode;
	private String paCode;
	private String goodsCode;
	private String seqNo;
}