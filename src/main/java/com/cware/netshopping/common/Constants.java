package com.cware.netshopping.common;


/**
 * @Class Name : Constants.java
 * @Description : Constants Class
 * @Modification Information
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2009.08.05              최초생성
 *
 * @author Commerceware
 * @since 2009.08.05
 * @version 1.0
 * @see
 *
 *  Copyright (C) by Commerceware All right reserved.
 */
public class Constants {

	public static final String LOGIN_SESSION_KEY = "user";
	public static final String RDNM_SESSION_KEY = "confirmRdNmImage_";
	public static final String SAVE_SUCCESS = "000000";
	public static final String SAVE_FAIL = "999999";
	public static final String WEB_INSERT_ID = "WEB";
	public static final String MOB_INSERT_ID = "MOB";
	/**
	 * 제휴 : 11번가
	 */
	public static final int    PA_11ST_CONTRACT_CNT = 2;
	public static final String PA_11ST_ONLINE = "PA_ONLINE";
	public static final String PA_11ST_BROAD = "PA_BROAD";
	public static final String PA_11ST_ONLINE_CODE = "12";
	public static final String PA_11ST_BROAD_CODE = "11";
	
	/**
	 * 제휴 
	 */
	/** 제휴 입점 상태 코드 : O502 **/
	public static final String PA_PA_STATUS_CD_REQUEST = "10"; //입점요청
	public static final String PA_PA_STATUS_CD_COMPLETED = "30"; //입점완료
	
	/** 제휴 판매상태코드 코드 : O503 **/
	public static final String PA_PA_SALE_GB_SALE = "20"; //판매중
	public static final String PA_PA_SALE_GB_PAUSE = "30"; //일시판매중지 
	public static final String PA_PA_SALE_GB_STOP_SALE = "40"; //영구판매중지 
	
	/** 제휴 구분 **/
	public static final String PA_BROAD = "PA_BROAD"; //방송
	public static final String PA_ONLINE = "PA_ONLINE"; //온라인
	
	/** 제휴 상담 구분 **/
	public static final String PA_COUNSEL_GB_GOODS = "01"; //상품
	public static final String PA_COUNSEL_GB_DELIVERY = "02"; //배송
	public static final String PA_COUNSEL_GB_CANCEL = "03"; //취소
	public static final String PA_COUNSEL_GB_EXCHANGE = "04"; //상품
	public static final String PA_COUNSEL_GB_ETC = "05"; //상품
	public static final String PA_COUNSEL_GB_ALIMI = "07"; //긴급메세지
	
	/** 
	 * 제휴 : G마켓  
	 */
	/** 대표 제휴사 코드 : G마켓 **/
	public static final String PA_GMKT_GROUP_CODE = "02";
	public static final int    PA_GMKT_CONTRACT_CNT = 2;
	/** 제휴사 코드 **/
	public static final String PA_GMKT_BROAD_CODE = "21"; //G마켓 방송
	public static final String PA_GMKT_ONLINE_CODE = "22"; //G마켓 온라인

	/** 제휴사 G마켓 연동 결과 **/
	public static final String PA_GMKT_RESULT_SUCCESS = "Success"; //성공
	public static final String PA_GMKT_RESULT_CHANGE = "Change"; //성공
	
	/** 제휴사 G마켓 주소구분 코드  **/
	public static final String PA_GMKT_ADDR_GB_CODE_RETURN = "20"; //회수

	/** 제휴사 G마켓 안전인증타입 **/
	public static final String PA_GMKT_CERTIFICATION_TYPE_REQUIRE_CERT = "RequireCert"; //대상
	public static final String PA_GMKT_CERTIFICATION_TYPE_NOT_CERT = "NotCert"; //대상아님
	public static final String PA_GMKT_CERTIFICATION_TYPE_ADD_DESCRIPTION = "AddDescription"; //상품상세표기 
	public static final String PA_GMKT_CERTIFICATION_TYPE_REQUIRE_CERT_WITH_API = "RequireCertWithAPI"; //API로인증
	
	/** 제휴사 G마켓 연동 : 상수처리 **/
	public static final String PA_GMKT_NUM_Y = "1";
	public static final String PA_GMKT_NUM_N = "0";
	public static final String PA_GMKT_Y = "Y";
	public static final String PA_GMKT_N = "N";
	public static final String PA_GMKT_MOD_CASE_INSERT = "INSERT";
	public static final String PA_GMKT_MOD_CASE_MODIFY = "MODIFY";
	public static final String PA_GMKT_RESULT_SUCCESS_OK = "200";
	
	/** 제휴사 G마켓 상담 구분 **/
	public static final String PA_GMKT_COUNSEL_GB_GOODS = "01"; //상품
	public static final String PA_GMKT_COUNSEL_GB_DELIVERY = "02"; //배송
	public static final String PA_GMKT_COUNSEL_GB_RETURN = "03"; //반품/환불
	public static final String PA_GMKT_COUNSEL_GB_CANCEL = "04"; //취소
	public static final String PA_GMKT_COUNSEL_GB_EXCHANGE = "05"; //교환
	public static final String PA_GMKT_COUNSEL_GB_ETC = "06"; //기타
	public static final String PA_GMKT_COUNSEL_GB_ALIMI = "08"; //긴급메세지
	
	/** 제휴사 옥션 상담 구분 **/
	public static final String PA_IAC_COUNSEL_GB_GOODS = "01"; //상품
	public static final String PA_IAC_COUNSEL_GB_DELIVERY = "02"; //배송
	public static final String PA_IAC_COUNSEL_GB_RETURN = "03"; //반품/취소/환불
	public static final String PA_IAC_COUNSEL_GB_EXCHANGE = "05"; //교환
	public static final String PA_IAC_COUNSEL_GB_ETC = "06"; //기타
	public static final String PA_IAC_COUNSEL_GB_ALIMI = "08"; //긴급메세지
	
	/** 제휴사 코드 **/
	public static final String PA_NAVER_CODE = "41"; //네이버 온라인
	/** 제휴사 네이버 상담 구분 **/
	public static final String PA_NAVER_COUNSEL_GB_GOODS = "01"; //상품
	public static final String PA_NAVER_COUNSEL_GB_DELIVERY = "02"; //배송
	public static final String PA_NAVER_COUNSEL_GB_RETURN = "03"; //반품
	public static final String PA_NAVER_COUNSEL_GB_EXCHANGE = "04"; //교환
	public static final String PA_NAVER_COUNSEL_GB_REFUND = "05"; //환불
	public static final String PA_NAVER_COUNSEL_GB_ETC = "06"; //기타
	
	/** 제휴사 커머스API 네이버 PROC_ID **/
	public static final String PA_CM_NAVER_PROC_ID = "06"; //기타
	
	public static final int    PA_COPN_CONTRACT_CNT = 2;
	public static final String PA_COPN_BROAD_CODE     = "51";
	public static final String PA_COPN_ONLINE_CODE     = "52";
	public static final String PA_COPN_PROC_ID        = "PACOPN";
	public static final String PA_COPN_COUNT_PER_PAGE = "50";
	public static final String PA_COPN_SHIPPING_COMPLETE_ADD_FROM_DAY = "40";
	public static final String PA_COPN_SHIPPING_COMPLETE_ADD_TO_DAY   = "60";
	public static final String PA_COPN_SUCCESS_OK     = "200";
	public static final String PA_COPN_MOD_CASE_INSERT = "INSERT";
	public static final String PA_COPN_MOD_CASE_MODIFY = "MODIFY";
	public static final String PA_COPN_RESULT_SUCCESS = "SUCCESS"; //성공
	public static final String PA_COPN_LOGIN_ID = "skstoa"; //쿠팡 로그인 ID
	public static final String PA_COPN_ONLINE_LOGIN_ID = "skstoamobile"; //쿠팡 로그인 ID
	
	public static final String PA_COPN_COUNSEL_GB_GOODS = "01"; //상품
	
	public static final int    PA_WEMP_CONTRACT_CNT = 2;
	public static final String PA_WEMP_GROUP_CODE	= "06";
	public static final String PA_WEMP_BROAD_CODE	= "61";
	public static final String PA_WEMP_ONLINE_CODE	= "62";
	public static final String PA_WEMP_PROC_ID		= "PAWEMP";
	public static final String PA_WEMP_COUNSEL_GB_GOODS = "01";
	
	/** 제휴 : 인터파크 * */
	public static final int    PA_INTP_CONTRACT_CNT = 2;
	public static final String PA_INTP_GROUP_CODE = "07";
	public static final String PA_INTP_BROAD_CODE = "71";
	public static final String PA_INTP_ONLINE_CODE = "72";
	public static final String PA_INTP_COM_URL = "PAINTP_COM_BASE_URL";
	public static final String PA_INTP_COUNSEL_GB_GOODS = "01";
	//public static final String PA_INTP_CANCEL_BUSINESS_HOURS = "PAINTP_CANCEL_BUSINESS_HOURS";
	public static final String PA_INTP_QUERY_TERM_MAX_HOURS = "PAINTP_QUERY_TERM_MAX_HOURS";
	public static final String PA_INTP_SUPPLY_ENTR_NO = "PAINTP_SUPPLY_ENTR_NO";
	public static final String PA_INTP_SUPPLY_CTRT_SEQ = "PAINTP_SUPPLY_CTRT_SEQ";
	public static final String PA_INTP_ENTR_ID = "PAINTP_ENTR_ID";
	/** 인터파크 주문/취소/CS API 결과코드 - 성공 */
	public static final String PA_INTP_ORDER_RESULT_SUCCESS = "000";
	public static final String PA_INTP_PROC_ID = "PAINTP";
	public static final String PA_INTP_BROAD = "PA_INTP_BROAD"; //방송
	public static final String PA_INTP_ONLINE = "PA_INTP_ONLINE"; //온라인
	
	/** 제휴 : 롯데온 **/
	public static final int    PA_LTON_CONTRACT_CNT = 2;
	public static final String PA_LTON_GROUP_CODE = "08";
	public static final String PA_LTON_BROAD_CODE 	= "81";
	public static final String PA_LTON_ONLINE_CODE 	= "82";
	public static final String PA_LTON_PROC_ID = "PALTON";
	public static final String PA_LTON_SUCCESS_CODE = "0000";
	
	/** 제휴 : 티몬 **/
	public static final int    PA_TMON_CONTRACT_CNT = 2;
	public static final String PA_TMON_GROUP_CODE = "09";
	public static final String PA_TMON_BROAD_CODE 	= "91";
	public static final String PA_TMON_ONLINE_CODE 	= "92";
	public static final String PA_TMON_PROC_ID = "PATMON";
	public static final String PA_TMON_SUCCESS_CODE = "0000";
	
	/** 제휴 : SSG **/
	public static final int    PA_SSG_CONTRACT_CNT 	= 2;
	public static final String PA_SSG_GROUP_CODE 	= "10";
	public static final String PA_SSG_BROAD_CODE 	= "A1";
	public static final String PA_SSG_ONLINE_CODE 	= "A2";
	public static final String PA_SSG_PROC_ID 		= "PASSG";
	public static final String PA_SSG_SUCCESS_CODE 	= "0000";

	/** 제휴 : 카카오 **/
	public static final int    PA_KAKAO_CONTRACT_CNT 	= 2;
	public static final String PA_KAKAO_GROUP_CODE 		= "11";
	public static final String PA_KAKAO_BROAD_CODE 		= "B1";
	public static final String PA_KAKAO_ONLINE_CODE 	= "B2";
	public static final String PA_KAKAO_PROC_ID 		= "PAKAKAO";
	public static final String PA_KAKAO_SUCCESS_CODE 	= "0000";
	
	/** 제휴 : HALF **/
	public static final int    PA_HALF_CONTRACT_CNT = 2;
	public static final String PA_HALF_GROUP_CODE 	= "12";
	public static final String PA_HALF_BROAD_CODE 	= "C1";
	public static final String PA_HALF_ONLINE_CODE 	= "C2";
	public static final String PA_HALF_PROC_ID 		= "PAHALF";
	public static final String PA_HALF_SUCCESS_CODE = "0000";
	
	/** 제휴 : TDEAL **/
	public static final int    PA_TDEAL_CONTRACT_CNT = 2;
	public static final String PA_TDEAL_BROAD_CODE  = "D1"; 
	public static final String PA_TDEAL_ONLINE_CODE  = "D2"; 
	public static final String PA_TDEAL_PROC_ID = "PATDEAL";
	public static final String PA_TDEAL_GROUP_CODE 	= "13";
	public static final String PA_TDEAL_SUCCESS_CODE = "0000";	
	
	/** 제휴사 TDEAL 상담 구분 **/
	public static final String PA_TDEAL_COUNSEL_GB_PRODUCT  = "01"; //상품
	public static final String PA_TDEAL_COUNSEL_GB_DELIVERY = "02"; //배송
	public static final String PA_TDEAL_COUNSEL_GB_CANCEL   = "03"; //취소
	public static final String PA_TDEAL_COUNSEL_GB_RETURN   = "04"; //반품
	public static final String PA_TDEAL_COUNSEL_GB_EXCHANGE = "05"; //교환
	public static final String PA_TDEAL_COUNSEL_GB_REFUND   = "06"; //환불
	public static final String PA_TDEAL_COUNSEL_GB_OTHER    = "07"; //기타
	public static final String PA_TDEAL_COUNSEL_GB_ALIMI    = "08"; //긴급메세지	
	
	/** 제휴 : 패션플러스  **/
	public static final int    PA_FAPLE_CONTRACT_CNT = 2;
	public static final String PA_FAPLE_BROAD_CODE  = "E1"; 
	public static final String PA_FAPLE_ONLINE_CODE  = "E2"; 
	public static final String PA_FAPLE_PROC_ID = "PAFAPLE";
	public static final String PA_FAPLE_GROUP_CODE 	= "14";
	public static final String PA_FAPLE_SUCCESS_CODE = "0000";	
	public static final String PA_FAPLE_SUCCESS_STATUS = "OK";	
	
	/** 제휴 : 퀸잇  **/
	public static final int    PA_QEEN_CONTRACT_CNT = 2;
	public static final String PA_QEEN_BROAD_CODE  = "F1"; 
	public static final String PA_QEEN_ONLINE_CODE  = "F2"; 
	public static final String PA_QEEN_PROC_ID = "PAQEEN";
	public static final String PA_QEEN_GROUP_CODE 	= "15";
	public static final String PA_QEEN_SUCCESS_CODE = "0000";	
	public static final String PA_QEEN_SUCCESS_STATUS = "OK";	
	
	/** 제휴사 패션플러스 상담 구분 **/
	public static final String PA_FAPLE_COUNSEL_GB_PRODUCT  = "01"; //상품
	public static final String PA_FAPLE_COUNSEL_GB_DELIVERY = "02"; //배송
	public static final String PA_FAPLE_COUNSEL_GB_CLAIM  = "03"; //클레임
	public static final String PA_FAPLE_COUNSEL_GB_REFUND   = "05"; //환불
	public static final String PA_FAPLE_COUNSEL_GB_ALIMI  = "06"; //CS알리미
	public static final String PA_FAPLE_COUNSEL_GB_OTHER    = "07"; //기타	
	
	/** 제휴사 퀸잇 상담 구분 **/
	public static final String PA_QEEN_COUNSEL_GB_PRODUCT  = "01"; //상품
	public static final String PA_QEEN_COUNSEL_GB_DELIVERY = "02"; //배송
	public static final String PA_QEEN_COUNSEL_GB_CANCEL   = "03"; //취소
	public static final String PA_QEEN_COUNSEL_GB_RETURN   = "04"; //반품
	public static final String PA_QEEN_COUNSEL_GB_EXCHANGE = "05"; //교환
	public static final String PA_QEEN_COUNSEL_GB_OTHER    = "06"; //기타	
	/**
	 * SYSTEM FIXED CONFIG
	 */
	/** Cart Session Name **/
	public static final String CART_KEY             = "cart";
	/** Login User Session Name **/
	public static final String USER_KEY             = "user";
	/** Login User (nonMember) Session Name **/
	public static final String USER_NONMEMBER_KEY   = "user_nonmember_yn";
	/** 기본 로그 화일 명 **/
	public static final String BASE_LOG_FILE        = "front-bandai-base";
	/** 주문 로그 화일 명 **/
	public static final String ORDER_LOG_FILE       = "front-bandai-order";
	/** Member 기본 로그 화일 명 **/
	public static final String MBASE_LOG_FILE       = "member-bandai-base";
	/** 카드I/F 로그 화일 명 **/
	public static final String CARD_LOG_FILE       = "front-bandai-card";
	/** application 에서 사용되는 resource name **/
	public static final String RESOURCE_BUNDLE_NAME = "resource.Messages";
	/** Statement QueryTimeOut **/
	public static final int QUERY_TIME_OUT          = 20;
	/** System environment variable :: Application Config File Name **/
	public static final String SYSTEM_ENV_CONFIG_FILE_PATH = "CWARE_FRONT_CONFIG_FILE";
	
	/** Domain Name **/
	public static final String TSHOP_DOMAIN				= "http://www.skymall.co.kr";
	public static final String TSHOP_PAYMENT_DOMAIN		= "https://www.skymall.co.kr";

	/**
	 * APPLICATION FIXED CONFIG
	 */
	/** 최종 소수점 유효 자리수 **/
	public static final int DEFAULT_FINAL_SIZE         	= 0;
	/** 기본 반올림 정책  [ 2:버림  3:반올림 4:올림 ] **/
	public static final int DEFAULT_MODE               	= 2;
	/** 계산시 유효 소수점 자리수 **/
	public static final int VALID_DECIMAL              	= 6;
	/** 반올림 정책 : 올림   BigDecimal.ROUND_CEILING **/
	public static final int MODE_CEIL                  	= 2;
	/** 반올림 정책 : 버림   BigDecimal.ROUND_FLOOR   **/
	public static final int MODE_FLOOR                 	= 3;
	/** 반올림 정책 : 반올림 BigDecimal.ROUND_HALF_UP **/
	public static final int MODE_ROUND                 	= 4;
	/** 주문 한건당 최대 상품의 판매 가능 수량 **/
	public static final long  ORDER_MAX_QTY            	= 10;
	/** 대량 구매 적용 대상 최대 상품 할인율 **/
	public static final double STANDARD_AMT_DC_RATE    	= 90;
	/** 최대 할인율 **/
	public static final double MAX_DC_RATE             	= 90;
	/** 최대 적립율 **/
	public static final double MAX_SAVEAMT_RATE        	= 40;
	/** 적립 포인트 유효기간 **/
	public static final long SAVEAMT_LIMIT_MONTH       	= 12;
	/** 회원 가입시 적립 포인트 **/
	public static final double SAVEAMT_AFFILIATION     	= 1000;
	/** 회원 가입 추가정보 입력시 포인트 **/
	public static final double SAVEAMT_AFFILIATION_ADD 	= 500;
	/** 포인트 구매시 첫 가용포인트 **/
	public static final double SAVEAMT_FIRST_USEAMT    	= 3;
    /** 배송일 기준 일자 **/
    public static final long   DELYDAY_DEFAULT_ADDDAY  	= 3;
	/** tcustomer.receiver_method : //= 04:internet **/
	public static final String WEB_RECEIVER_METHOD     	= "04";
	/** tcustomer.receiver_method : //= 04:mobile **/
	public static final String MOB_RECEIVER_METHOD     	= "07";
	/** 배송비 구분 Tcode **/
	public static final String SHPFEE_GUBUN_LGROUP     	= "B005";
	/** 배송비 기준코드 Tcode **/
	public static final String SHPFEE_CODE_LGROUP      	= "B850";
	/** 보통배송의 배송비 기본 배송비 그룹 **/
	public static final String BASE_SHPFEE_GROUP       	= "10";
	/** 보통배송의 배송비 기본 배송비 구분 **/
	public static final String BASE_SHPFEE_GUBUN       	= "10";
	/** 회원 구분별 무료배송여부 **/
	public static final String MEMBGB_SHPFEE_CODE      	= "21";
	/** 회원 구분별 유료배송 기준 **/
	public static final String MEMBGB_SHPFEE_BASE      	= "22";
	/** 페이징 처리에서 사용되는 javascript method **/
	public static final String PAGE_SCRIPT_NAME        	= "goToPage";
	/** 회원가입시 탈퇴 기간 체크 여부 **/
	public static final String WITHDRAWAL_CHECK_YN     	= "1";
	/** 탈퇴 유예기간 **/
	//public static final long   WITHDRAWAL_PERIOD      = 3;
	/** 탈퇴 유예기간 **/
	public static final String   WITHDRAWAL_PERIOD   	= "30";
	/** 신규회원의 발급쿠폰 : Tconfig.item name **/
	public static final String NEW_CUST_COUPON         	= "NEW_CUST_COUPON";
	public static final String SMS_PHONE_NO         	= "00000000";
	

	/**
	 * APPLICATION UNFIXED CONFIG
	 * (warning) no final variable setting
	 */
	/** 페이징 처리에서 좌측 버튼 이미지명 **/
	public static  String RIGHT_BTN_URL                 = "/common/images/button/btn_list_forward.gif";
	/** 페이징 처리에서 우측 버튼 이미지명 **/
	public static  String LEFT_BTN_URL                  = "/common/images/button/btn_list_previous.gif";
	/** tcustomer.country : 0082:ko/0044:uk/0033:fr **/
	public static  String WEB_COUNTRY                   = "0082";
	/**  main page 에 전시되는 신상품 Count **/
	public static  long   NEWGOODS_DISPLAY_CNT          = 3;
	/**  main page 에 전시되는 best 5 상품 Count **/
	public static  long   BESTGOODS_DISPLAY_CNT         = 2;
	/**  pategory page 에 전시되는 추천상품 Count **/
	public static  long   C_ACC_DISPLAY_CNT    			= 5;
	/**  main page 에 전시되는 뉴스/이벤트 Count **/
	public static  long   MAIN_NEWS_EVENT_DISPLAY_CNT   = 3;
	/**  main page 에 전시되는 상품 사용후기 Count **/
	public static  long   MAIN_GOODSCOMMENT_DISPLAY_CNT = 2;
	/** 메일 웹에이전트(URL) 작업 HOST ADDRESS **/
	public static  String[] MAIL_WORK_HOST              = {"127.0.0.1", "192.168.100.153" , "210.109.110.184", "210.109.110.133"};
	/** 페이징 처리에서 페이지당 전시되는 Scale1 **/
	public static  long   PAGESCALE_1                   = 12;
	/** 페이징 처리에서 페이지당 전시되는 Scale2 **/
	public static  long   PAGESCALE_2                   = 10;
	/** 페이징 처리에서 페이지당 전시되는 Scale3 **/
	public static  long   PAGESCALE_3                   = 6;
	/** 페이징 처리에서 페이지당 전시되는 Scale4 **/
	public static  long   PAGESCALE_4                   = 5;
	/** 페이징 처리에서 페이지당 전시되는 Scale5 **/
    public static  long   PAGESCALE_5                   = 3;
	/** 페이징 처리에서 페이지당 전시되는 Scale6 **/
    public static  long   PAGESCALE_6                   = 2;
	/** 페이징 처리에서 페이지당 전시되는 Scale7 **/
    public static  long   PAGESCALE_7                   = 4;

	/** 웹 주문 매체 **/
	public static String WEB_ORDER_MEDIA         = "61";
	/** 웹 매체 구분 **/
	public static String WEB_MEDIA_GB            = "02";
	/** 웹 광고 코드 **/
	public static String WEB_MEDIA_CODE          = "EC01";
	

	/** 모바일 주문 매체 **/
	public static String MOB_ORDER_MEDIA         = "62";
	/** 모바일 매체 구분 **/
	public static String MOB_MEDIA_GB            = "03";
	/** 모바일 광고 코드 **/
	public static String MOB_MEDIA_CODE          = "MC01";
	
	public static String WEB_MEMB_GB              = "XX";

	public static String CJ_TRAKING_URL			 = "http://nexs.cjgls.com/web/info.jsp?slipno=";

	/** 당사배송 ENTP 코드 **/
	public static String SHIP_ENTP_CODE			 = "100001";

	/** 테스트용**/
	public static String INICIS_INIPAYHOME			 = "C:/INIpay50";

	/** 테스트용**/
	//public static String INICIS_INIPAYMID01			 = "INIpayTest";
	//public static String INICIS_INIPAYMID09			 = "INIpayTest";

	/** 실서버 **/
	//public static String INICIS_INIPAYHOME		     = "/data2/netshopping/pkg11-202/interface/inicis";

	/** 실서버**/
	public static String INICIS_INIPAYMID01			 = "INIpayTest";
	public static String INICIS_INIPAYMID09			 = "INIpayTest";
	
	public static String INICIS_ADMIN			    =  "1111";
	
	public static String INICIS_URL			     	= "http://front.commerceware.co.kr";
	
	public static String INICIS_NOINTEREST			= "no";
	
	public static String INICIS_QUOTABASE			= "lumpsum:00:02:03:04:05:06";
	
	
	/** 핸드폰 인증에 필요한 필수 값 **/
	public static String KMC_CP_ID 				= "KTHM1003";  		// 회원사 ID
	public static String KMC_CERT_MET 		= "M"; 					// 본인인증방법(휴대폰으로 고정)
	
//	public static String KMC_URL_CODE		= "006001";  				// URL CODE (로컬 코드)
//	public static String KMC_URL_CODE			= "005001";  				// URL CODE (개발 코드)
	public static String KMC_URL_CODE			= "007001";  				// URL CODE (상용 코드)
	
	/** 핸드폰 본인인증서비스 결과수신 POPUP URL (로컬) **/
//	public static String RESULT_URL = "http://www.test.skymall.co.kr/customer/kmc-certification-result";
	/** 핸드폰 본인인증서비스 결과수신 POPUP URL (개발) **/
//	public static String RESULT_URL = "http://www.dev.skytshop.com/customer/kmc-certification-result";
	/** 핸드폰 본인인증서비스 결과수신 POPUP URL (상용) **/
	public static String RESULT_URL = "http://front.commerceware.co.kr/customer/kmc-certification-result";
	
	/** 핸드폰 본인인증서비스 호출 URL **/
	public static String ACTION_URL = "https://www.kmcert.com/kmcis/web/kmcisReq.jsp";
	
	/** IPIN 인증에 필요한 필수 값 **/
//	public static String IPIN_URL_CODE	= "010002";	// 서비스번호(로컬)
//	public static String IPIN_URL_CODE	= "008002";	// 서비스번호(개발)
	public static String IPIN_URL_CODE	= "012002";	// 서비스번호(상용)
	
	/** IPIN 본인인증서비스 결과수신 POPUP URL (로컬) **/
//	public static String IPIN_RESULT_URL 	= "23http://www.test.skymall.co.kr/customer/ipin-certification-result";
	/** IPIN 본인인증서비스 결과수신 POPUP URL (개발) **/
//	public static String IPIN_RESULT_URL 	= "23http://www.dev.skytshop.com/customer/ipin-certification-result";
	/** IPIN 본인인증서비스 결과수신 POPUP URL (상용) **/
	public static String IPIN_RESULT_URL 	= "23http://front.commerceware.co.kr/customer/ipin-certification-result";
	
	/** IPIN 본인인증서비스 호출 URL **/
	public static String IPIN_ACTION_URL = "https://ipin.siren24.com/i-PIN/jsp/ipin_j10.jsp";

	/** Kollus 미디어 토큰 호출 URL **/
	public static String KOLLUS_MEDIA_TOKEN_URL = "http://v.kr.kollus.com/sr/meida.mp4?key=";
	
	/** Kollus 미디어 컨텐츠 키 호출 URL **/
	public static String KOLLUS_MEDIA_CONTENT_KEY_URL = "http://v.kr.kollus.com/";
	
	/** Kollus 미디어 Profile Key **/ 
	public static String KOLLUS_MEDIA_PROFILE_KEY = "dreamcom-pc1-normal";
	
}