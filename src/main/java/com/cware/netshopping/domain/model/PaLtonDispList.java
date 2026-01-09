package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

/**
 * 표준카테고리 조회 API : 전시카테고리 리스트
 * @author user
 *
 */
public class PaLtonDispList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String stdCatId;
	private String mallDvsCd;
	private String dispCatId;
	
	public String getStdCatId() {
		return stdCatId;
	}
	public void setStdCatId(String stdCatId) {
		this.stdCatId = stdCatId;
	}
	public String getMallDvsCd() {
		return mallDvsCd;
	}
	public void setMallDvsCd(String mallDvsCd) {
		this.mallDvsCd = mallDvsCd;
	}
	public String getDispCatId() {
		return dispCatId;
	}
	public void setDispCatId(String dispCatId) {
		this.dispCatId = dispCatId;
	}
	
}
