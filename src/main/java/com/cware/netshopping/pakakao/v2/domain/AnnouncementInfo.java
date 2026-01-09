package com.cware.netshopping.pakakao.v2.domain;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AnnouncementInfo {

	// 상품군 코드
	// WEAR 의류
	// SHOES 구두/신발
	// BAG 가방
	// FASHION_ITEM 패션잡화(모자/벨트/액세서리)
	// BEDDING_CURTAIN 침구류/커튼
	// FURNITURE 가구(침대/소파/싱크대/DIY제품)
	// IMAGE_APPLIANCE 영상가전(TV류)
	// HOME_APPLIANCE 가정용전기제품(냉장고/세탁기/식기세척기/전자레인지)
	// SEASONAL_APPLIANCE 계절가전(에어컨/온풍기)
	// OFFICE_EQUIPMENT 사무용기기(컴퓨터/노트북/프린터)
	// OPTICS_EQUIPMENT 광학기기(디지털카메라/캠코더)
	// MICROELECTRONICS 소형전자(MP3/전자사전 등)
	// CELLPHONE 휴대폰
	// NAVIGATION 내비게이션
	// CAR_EQUIPMENT 자동차용품(자동차부품/기타자동차용품)
	// MEDICAL_EQUIPMENT 의료기기
	// KITCHEN_UTENSILS 주방용품
	// COSMETIC 화장품
	// JEWELRY 귀금속/보석/시계류
	// FOOD 식품(농수축산물)
	// PROCESSED_FOOD 가공식품
	// HEALTH_FUNCTIONAL_FOOD 건강기능식품
	// KIDS 영유아용품
	// MUSICAL_INSTRUMENT 악기
	// SPORTS_EQUIPMENT 스포츠용품
	// BOOK 서적
	// HOTEL_PENSION_BOOKING 호텔/펜션예약
	// TRAVEL_PACKAGE 여행패키지
	// AIRLINE_TICKET 항공권
	// RENT_CAR 자동차대여서비스(렌터카)
	// RENTAL_HA 물품대여서비스(정수기,비데,공기청정기 등)
	// RENTAL_ETC 물품대여서비스(서적,유아용품,행사용품 등)
	// DIGITAL_CONTENTS 디지털콘텐츠(음원,게임,인터넷강의 등)
	// GIFTCARD_COUPON 상품권/쿠폰
	// MOBILE_COUPON 모바일쿠폰
	// MOVIE_CONCERT 영화/공연
	// HOUSEHOLD_CHEMICAL 생활화학제품
	// BIOCIDAL_PRODUCT 살생물제품
	// ETC_SERVICE 기타 용역
	// ETC_PRODUCT 기타 재화
	String announcementType;

	// 상품군별 상세 고시정보
	Map<String, Object> announcement;

	public String getAnnouncementType() {
		return announcementType;
	}

	public void setAnnouncementType(String announcementType) {
		this.announcementType = announcementType;
	}

	public Map<String, Object> getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(Map<String, Object> announcement) {
		this.announcement = announcement;
	}

	@Override
	public String toString() {
		return "AnnouncementInfo [announcementType=" + announcementType + ", announcement=" + announcement + "]";
	}

}
