package com.cware.netshopping.pagmkt.util;
import java.util.HashSet;

class PaAvailbleApiCode {

	private final static HashSet<String> apiCodes = new HashSet<String>();
		
	
	
	/**
	 * H.S.BAEK
	 * 개발에서 해당 API를 사용하기 위해서는 다음의 주석을 풀어여합니다.
	 * 예를들어 apiCodes.add("IF_PAGMKTAPI_V2_00_000")가 주석이 풀려있으면 apiCodes.add("IF_PAGMKTAPI_V2_00_000")를 사용할수 있습니다. 
	 * Gmarket의 상태값을 바꾸는 API 목록은 절대 아래 목록에 넣지 마세요!!! // TODO 지금은 넣어뒀지만 운영배포전엔 빼야함 예를들어 주문확인같은 목록
	 * **/
	
	private static void newHashSet(){
		
		if (apiCodes.size() > 0) return;
		
		apiCodes.add("IF_PAGMKTAPI_V2_00_000");		// 방송편성표연동
		apiCodes.add("IF_PAGMKTAPI_V2_00_001");		// 3.1 G마켓, 옥션 Site 카테고리 조회
		apiCodes.add("IF_PAGMKTAPI_V2_00_001_L");	// 3.1 G마켓, 옥션 Site 카테고리 전체 조회
		apiCodes.add("IF_PAGMKTAPI_V2_00_002");		// 3.2 미니샵 카테고리 코드 조회
		apiCodes.add("IF_PAGMKTAPI_V2_00_003");		// 3.3 ESM 카테고리 조회
		apiCodes.add("IF_PAGMKTAPI_V2_00_004");		// 3.4 ESM – Site 카테고리 매칭 조회 
		apiCodes.add("IF_PAGMKTAPI_V2_00_005");		// 3.5 판매자 주소록 등록
		apiCodes.add("IF_PAGMKTAPI_V2_00_006");		// 3.6 판매자 주소록 수정
		apiCodes.add("IF_PAGMKTAPI_V2_00_007");		// 3.7 판매자 주소 조회
		apiCodes.add("IF_PAGMKTAPI_V2_00_008");		// 3.8 출하지 등록
		apiCodes.add("IF_PAGMKTAPI_V2_00_009");		// 3.9 출하지 수정
		apiCodes.add("IF_PAGMKTAPI_V2_00_010");		// 3.10 출하지 조회
		apiCodes.add("IF_PAGMKTAPI_V2_00_011");		// 3.11 묶음배송비 정책 등록
		apiCodes.add("IF_PAGMKTAPI_V2_00_012");		// 3.12 묶음배송비 정책 수정
		apiCodes.add("IF_PAGMKTAPI_V2_00_013");		// 3.13 출하지 기준 묶음배송비 정책 조회
		apiCodes.add("IF_PAGMKTAPI_V2_00_014");		// 3.14 발송 타입 정책 등록
		apiCodes.add("IF_PAGMKTAPI_V2_00_015");		// 3.15 발송 타입 정책 조회
		apiCodes.add("IF_PAGMKTAPI_V2_00_016");		// 3.16 기본 발송 타입 정책 관리
		apiCodes.add("IF_PAGMKTAPI_V2_00_017");		// 3.17 브랜드명 기준 ESM브랜드코드 조회
		apiCodes.add("IF_PAGMKTAPI_V2_00_018");		// 3.18 제조사명 기준 ESM브랜드코드 조회
		apiCodes.add("IF_PAGMKTAPI_V2_00_019");		// 3.19 Site카테고리별 추천옵션그룹 조회
		apiCodes.add("IF_PAGMKTAPI_V2_00_020");		// 3.20 추천옵션별 선택 항목 조회
		apiCodes.add("IF_PAGMKTAPI_V2_01_001");		// 4.1 상품등록
		apiCodes.add("IF_PAGMKTAPI_V2_01_002");		// 4.2 Site 상품번호 조회
		apiCodes.add("IF_PAGMKTAPI_V2_01_002_L");	// 4.2~ Site 상품번호, 옵션등록
		apiCodes.add("IF_PAGMKTAPI_V2_01_003");		// 4.3 상품 일괄 수정
		apiCodes.add("IF_PAGMKTAPI_V2_01_003_L");	// 4.3~ 상품 일괄 수정 리스트 
		apiCodes.add("IF_PAGMKTAPI_V2_01_004");		// 4.4 상품 가격/재고/판매상태 수정
		apiCodes.add("IF_PAGMKTAPI_V2_01_005");		// 4.5 상품 가격/재고/판매상태 조회
		apiCodes.add("IF_PAGMKTAPI_V2_01_006");		// 4.6 상품 판매자 부담할인 수정
		apiCodes.add("IF_PAGMKTAPI_V2_01_007");		// 4.7 상품 판매자 부담할인 조회
		apiCodes.add("IF_PAGMKTAPI_V2_01_008");		// 4.8 상품 판매자 부담할인 해제
		apiCodes.add("IF_PAGMKTAPI_V2_01_009");		// 4.9 상품 이미지 수정
		apiCodes.add("IF_PAGMKTAPI_V2_01_010");		// 4.10 추천옵션 등록 	
		apiCodes.add("IF_PAGMKTAPI_V2_01_011");		// 4.11 추천옵션 수정
		apiCodes.add("IF_PAGMKTAPI_V2_01_012");		// 4.12 추천옵션 조회
		apiCodes.add("IF_PAGMKTAPI_V2_01_013");		// 4.13 구버전 옵션 등록
		apiCodes.add("IF_PAGMKTAPI_V2_01_014");		// 4.14 구버전 옵션 수정
		apiCodes.add("IF_PAGMKTAPI_V2_01_015");		// 4.15 구버전 옵션 조회
		apiCodes.add("IF_PAGMKTAPI_V2_01_016");		// 4.16 구버전 옵션 삭제
		apiCodes.add("IF_PAGMKTAPI_V2_01_017");		// 4.17 상품 광고/부가서비스 등록
		apiCodes.add("IF_PAGMKTAPI_V2_01_018");		// 4.18 상품 광고/부가서비스 수정
		apiCodes.add("IF_PAGMKTAPI_V2_01_019");		// 4.19 상품 간략 정보 조회
		apiCodes.add("IF_PAGMKTAPI_V2_01_020");		// 4.20 상품명 변경
		apiCodes.add("IF_PAGMKTAPI_V2_01_021");		// 4.21 G9사이트 상품노출 설정
		apiCodes.add("IF_PAGMKTAPI_V2_01_022");		// 4.22 그룹 생성
		apiCodes.add("IF_PAGMKTAPI_V2_01_023");		// 4.23 그룹 수정
		apiCodes.add("IF_PAGMKTAPI_V2_01_024");		// 4.24 그룹 삭제
		apiCodes.add("IF_PAGMKTAPI_V2_01_025");		// 4.25 그룹코드별 정보 조회
		apiCodes.add("IF_PAGMKTAPI_V2_01_026");		// 4.26 Seller ID별 그룹조회
		apiCodes.add("IF_PAGMKTAPI_V2_01_027");		// 4.27 그룹 상품 등록
		apiCodes.add("IF_PAGMKTAPI_V2_01_028");		// 4.28 그룹 상품 조회
		apiCodes.add("IF_PAGMKTAPI_V2_01_029");		// 4.29 상품 삭제
		apiCodes.add("IF_PAGMKTAPI_V2_01_030");		// 4.30 G마켓 복수구매할인 등록/수정
		apiCodes.add("IF_PAGMKTAPI_V2_01_031");		// 4.31 G마켓 복수구매할인 해제
		apiCodes.add("IF_PAGMKTAPI_V2_01_032");		// 4.32 옥션 특별할인 등록/수정
		apiCodes.add("IF_PAGMKTAPI_V2_01_033");		// 4.33 옥션 특별할인 해제
		apiCodes.add("IF_PAGMKTAPI_V2_01_034");		// 4.34 판매자지급 스마일캐시 등록
		apiCodes.add("IF_PAGMKTAPI_V2_01_035");		// 4.35 판매자지급 스마일캐시 수정	
		apiCodes.add("IF_PAGMKTAPI_V2_01_036");		// 4.36 판매자지급 스마일캐시 해제
		apiCodes.add("IF_PAGMKTAPI_V2_02_001");		// 5.1 입금확인전 주문 조회
		apiCodes.add("IF_PAGMKTAPI_V2_02_002");		// 5.2 주문 조회
		apiCodes.add("IF_PAGMKTAPI_V2_02_002C");	// 5.7 수취 확인 목록 조회
		apiCodes.add("IF_PAGMKTAPI_V2_02_003");		// 5.3 주문 확인
		apiCodes.add("IF_PAGMKTAPI_V2_02_004");		// 5.4 발송예정일 등록 및 갱신
		apiCodes.add("IF_PAGMKTAPI_V2_02_005");		// 5.5 배송 송장등록
		apiCodes.add("IF_PAGMKTAPI_V2_02_006");		// 5.6 배송완료 처리
		apiCodes.add("IF_PAGMKTAPI_V2_02_007");		// 5.7 주문번호 상태 조회
		apiCodes.add("IF_PAGMKTAPI_V2_02_008");		// 5.8 배송진행정보 조회(클레임히스토리)
		apiCodes.add("IF_PAGMKTAPI_V2_02_009");		// 5.9 미수령신고조회
		apiCodes.add("IF_PAGMKTAPI_V2_02_010");		// 5.10 미수령신고 철회요청
		apiCodes.add("IF_PAGMKTAPI_V2_03_001");		// 6.1 취소 조회
		apiCodes.add("IF_PAGMKTAPI_V2_03_002");		// 6.2 취소 승인
		apiCodes.add("IF_PAGMKTAPI_V2_03_002B");	// 6.2 취소 승인(제휴)
		apiCodes.add("IF_PAGMKTAPI_V2_03_003");		// 6.3 판매 취소-상품품절처리
		apiCodes.add("IF_PAGMKTAPI_V2_03_004");		// 6.4 판매 취소-상품비품절처리
		apiCodes.add("IF_PAGMKTAPI_V2_03_005");		// 6.5 반품 조회
		apiCodes.add("IF_PAGMKTAPI_V2_03_006");		// 6.6 반품수거 송장등록
		apiCodes.add("IF_PAGMKTAPI_V2_03_007");		// 6.7 반품보류 처리
		apiCodes.add("IF_PAGMKTAPI_V2_03_008");		// 6.8 반품 승인
		apiCodes.add("IF_PAGMKTAPI_V2_03_009");		// 6.9 반품건 교환전환
		apiCodes.add("IF_PAGMKTAPI_V2_03_010");		// 6.10 교환 조회
		apiCodes.add("IF_PAGMKTAPI_V2_03_011");		// 6.11 교환수거 송장등록
		apiCodes.add("IF_PAGMKTAPI_V2_03_012");		// 6.12 교환수거완료 처리
		apiCodes.add("IF_PAGMKTAPI_V2_03_013");		// 6.13 교환보류 처리
		apiCodes.add("IF_PAGMKTAPI_V2_03_014");		// 6.14 교환재발송 송장등록
		apiCodes.add("IF_PAGMKTAPI_V2_03_015");		// 6.15 교환재발송 배송완료 처리
		apiCodes.add("IF_PAGMKTAPI_V2_03_016");		// 6.16 교환보류 해제처리
		apiCodes.add("IF_PAGMKTAPI_V2_03_017");		// 6.17 교환건 반품전환
		apiCodes.add("IF_PAGMKTAPI_V2_04_001");		// 7.1 정산조회(상품대금)
		apiCodes.add("IF_PAGMKTAPI_V2_04_002");		// 7.2 정산조회(배송비)
		apiCodes.add("IF_PAGMKTAPI_V2_05_001");		// 8.1 ESM공지사항
		apiCodes.add("IF_PAGMKTAPI_V2_05_002");		// 8.2 고객 게시판 문의 조회
		apiCodes.add("IF_PAGMKTAPI_V2_05_002_L");	// 8.2~ 고객 게시판 문의 조회 
		apiCodes.add("IF_PAGMKTAPI_V2_05_003");		// 8.3 고객 게시판 문의 답변
		apiCodes.add("IF_PAGMKTAPI_V2_05_003_L");	// 8.3~ 고객 게시판 문의 답변
		apiCodes.add("IF_PAGMKTAPI_V2_05_004");		// 8.4 고객 긴급메시지 조회		
		apiCodes.add("IF_PAGMKTAPI_V2_05_004_L");	// 8.4~ 고객 긴급메시지 조회
		apiCodes.add("IF_PAGMKTAPI_V2_05_005");		// 8.5 고객 긴급메시지 답변
		apiCodes.add("IF_PAGMKTAPI_V2_05_005_L");	// 8.5~ 고객 긴급메시지 답변	
	}
	
	
	public static boolean searchApi(String apiCode){
		newHashSet();
		return apiCodes.contains(apiCode);
	}
	
}
