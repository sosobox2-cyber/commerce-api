package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Description {

	// 상품 상세정보 타입
	// 1:contentID(추후제공)
	// 2:html
	private int type;

	// 상품상세정보 타입 1일 경우 필수(추후제공)
	private String contentId;

	// 상품상세 Html 입력
	// iframe, Script, 및 max-width 등은 사용 불가
	private String html;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	@Override
	public String toString() {
		return "Description [type=" + type + ", contentId=" + contentId + ", html=" + html + "]";
	}

}
