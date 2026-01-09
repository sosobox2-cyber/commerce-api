package com.cware.partner.sync.domain.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaKakaoImageId implements Serializable {
	private static final long serialVersionUID = -5922663207031167947L;

	private String paGroupCode;
	private String goodsCode;
	private String imageGb;
}