package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

/**
 * 표준카테고리 조회 API : 상품품목고시 정보 리스트
 * @author user
 *
 */
public class PaLtonPdItmsList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String stdCatId;
	private String pdItmsCd;
	
	public String getStdCatId() {
		return stdCatId;
	}
	public void setStdCatId(String stdCatId) {
		this.stdCatId = stdCatId;
	}
	public String getPdItmsCd() {
		return pdItmsCd;
	}
	public void setPdItmsCd(String pdItmsCd) {
		this.pdItmsCd = pdItmsCd;
	}
	
}
