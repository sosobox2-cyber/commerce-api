package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoId implements Serializable {
	private static final long serialVersionUID = 8113503247770742213L;

	private String promoNo;
	private String promoSeq;
}