package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaStockExceptId implements Serializable {

	private static final long serialVersionUID = 3578674539755677295L;
	private String targetGb;
	private String targetCode;
}