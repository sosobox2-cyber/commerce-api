package com.cware.partner.sync.domain.id;

import java.io.Serializable;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaCopnGoodsKindsFreshId implements Serializable {

	private static final long serialVersionUID = 7731965758572690526L;
	
	private String lgroup;
	private String mgroup;
	private String sgroup;
	private String dgroup;
}