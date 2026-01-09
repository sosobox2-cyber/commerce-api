package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaHalfBrandId implements Serializable {
	private static final long serialVersionUID = -2416209027823978514L;
	private String paCode;
	private String brandCode;
	private String paBrandNo;
}