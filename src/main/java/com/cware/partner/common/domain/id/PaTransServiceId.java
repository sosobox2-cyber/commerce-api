package com.cware.partner.common.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaTransServiceId implements Serializable {

	private static final long serialVersionUID = 7025485758409548564L;

	private String transCode;
	private String transType;
	private long transServiceNo;
}