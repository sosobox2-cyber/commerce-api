package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaGoodsOfferId implements Serializable {
	private static final long serialVersionUID = -551198472620229226L;

	private String goodsCode;
	private String paGroupCode;
	private String paOfferType;
	private String paOfferCode;

}