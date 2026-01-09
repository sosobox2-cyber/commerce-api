package com.cware.partner.cdc.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaCdcGoodsId implements Serializable {
	private static final long serialVersionUID = 3547280910584561502L;

	private String goodsCode;
	private long cdcSnapshotNo;
}