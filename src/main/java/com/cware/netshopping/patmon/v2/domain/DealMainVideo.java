package com.cware.netshopping.patmon.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DealMainVideo {
	// 동영상 링크 여부
	// 동영상 링크를 사용할 경우 딜 상세페이지 딜 대표이미지에는 동영상 대표이미지(썸네일)이 노출되며 티몬 사이트 홈에는 mainimage가
	// 노출
	Boolean flagMainVideo;

	String mainVideoUrl; // 동영상 링크

	public Boolean getFlagMainVideo() {
		return flagMainVideo;
	}

	public void setFlagMainVideo(Boolean flagMainVideo) {
		this.flagMainVideo = flagMainVideo;
	}

	public String getMainVideoUrl() {
		return mainVideoUrl;
	}

	public void setMainVideoUrl(String mainVideoUrl) {
		this.mainVideoUrl = mainVideoUrl;
	}

	@Override
	public String toString() {
		return "DealMainVideo [flagMainVideo=" + flagMainVideo + ", mainVideoUrl=" + mainVideoUrl + "]";
	}

}
