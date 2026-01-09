package com.cware.partner.common.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaTransApiId implements Serializable {

	private static final long serialVersionUID = -4390690737085843916L;

	private String transCode;
	private String transType;
	private long transApiNo;
}