package com.cware.netshopping.palton.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PdItmsInfo {
	// 상품품목코드 [공통코드 : PD_ITMS_CD]
	// 01 [01]의류
	// 02 [02]구두/신발
	// 03 [03]가방
	// 04 [04]패션잡화(모자/벨트/액세서리)
	// 05 [05]침구류/커튼
	// 06 [06]가구(침대/소파/싱크대/DIY제품)
	// 07 [07]영상가전(TV류)
	// 08 [08]가정용 전기제품(냉장고/세탁기/식기세척기/전자레인지)
	// 09 [09]계절가전(에어컨/온풍기)
	// 10 [10]사무용기기(컴퓨터/노트북/프린터)
	// 11 [11]광학기기(디지털카메라/캠코더)
	// 12 [12]소형전자(MP3/전자사전 등)
	// 13 [13]휴대폰
	// 14 [14]내비게이션
	// 15 [15]자동차용품(자동차부품.기타 자동차용품)
	// 16 [16]의료기기
	// 17 [17]주방용품
	// 18 [18]화장품
	// 19 [19]귀금속/보석/시계류
	// 20 [20]식품(농.축.수산물)
	// 21 [21]가공식품
	// 22 [22]건강기능식품
	// 23 [23]영유아용품
	// 24 [24]악기
	// 25 [25]스포츠용품
	// 26 [26]서적
	// 27 [27]호텔/펜션 예약
	// 28 [28]여행 상품
	// 29 [29]항공권
	// 30 [30]자동차 대여 서비스(렌터카)
	// 31 [31]물품대여 서비스(정수기,비데,공기청정기 등)
	// 32 [32]물품대여 서비스(서적,유아용품,행사용품 등)
	// 33 [33]디지털 콘텐츠(음원,게임,인터넷강의 등)
	// 34 [34]상품권/쿠폰
	// 35 [35]모바일쿠폰
	// 36 [36]영화/공연
	// 37 [37]기타(용역)
	// 38 [38]기타(재화)
	String pdItmsCd;

	List<PdItmsArtl> pdItmsArtlLst; // 상품품목항목목록

	public String getPdItmsCd() {
		return pdItmsCd;
	}

	public void setPdItmsCd(String pdItmsCd) {
		this.pdItmsCd = pdItmsCd;
	}

	public List<PdItmsArtl> getPdItmsArtlLst() {
		return pdItmsArtlLst;
	}

	public void setPdItmsArtlLst(List<PdItmsArtl> pdItmsArtlLst) {
		this.pdItmsArtlLst = pdItmsArtlLst;
	}

	@Override
	public String toString() {
		return "PdItmsInfo [pdItmsCd=" + pdItmsCd + ", pdItmsArtlLst=" + pdItmsArtlLst + "]";
	}

}
