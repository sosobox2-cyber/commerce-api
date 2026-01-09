package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaGoodsDtSeq implements Serializable {
	private static final long serialVersionUID = 2185651936430260721L;

	private String paCode;
	private String goodsCode;
	private String goodsdtCode;
	private String goodsdtSeq;
}