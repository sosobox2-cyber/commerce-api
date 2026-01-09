package com.cware.partner.common.domain.id;

import java.io.Serializable;
import java.sql.Timestamp;


import lombok.Data;

@Data
public class PaWithoutHourId implements Serializable {

	private static final long serialVersionUID = 225060287305151777L;
	
	private String paGroupCode;
	private Timestamp withoutStartDate;
	private Timestamp withoutEndDate;
}