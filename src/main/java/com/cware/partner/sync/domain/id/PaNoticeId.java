package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaNoticeId implements Serializable {
	private static final long serialVersionUID = 8632121821412676453L;

	private String noticeNo;
	private String paGroupCode;
	private String goodsCode;

}