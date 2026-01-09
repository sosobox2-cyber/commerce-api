package com.cware.partner.sync.domain;

import com.cware.partner.sync.domain.entity.PaGoodsTarget;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SyncResult {
	boolean isSync;
	PaGoodsTarget target;
}
