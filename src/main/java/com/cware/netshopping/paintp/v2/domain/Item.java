package com.cware.netshopping.paintp.v2.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 상품
 * 
 * http://www.interpark.com/openapi/site/APIInsertSpecNew.jsp
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

	// 인터파크 상품번호
	private String prdNo;
	
	// 상품상태(필수) - 새상품:01, 중고상품:02, 반품상품:03
	private String prdStat;

	// 인터파크 상점번호(필수) (default - 0000100000)
	private String shopNo;

	// 인터파크 전시코드(필수)
	private String omDispNo;

	// 상품명(필수) - 한글 60자 (영문/숫자 120자)
	private String prdNm;

	// 제조업체명(필수)
	private String hdelvMafcEntrNm;

	// 원산지(필수)
	private String prdOriginTp;

	// 부가면세상품(필수) - 과세상품:01, 면세상품:02, 영세상품:03
	private String taxTp;

	// 성인용품여부(필수) - 성인용품:Y, 일반용품:N
	private String ordAgeRstrYn;

	// (필수) 판매중:01, 품절:02, 판매중지:03, 일시품절:05, 예약판매:09, 상품삭제:98
	// * 삭제된 상품은 다시 조회하거나 수정할 수 없습니다.
	private String saleStatTp;

	// 판매가(필수)
	private Long saleUnitcost;

	// 판매수량(필수) - 99999 개 이하로 입력
	private String saleLmtQty;

	// 판매시작일(필수) - yyyyMMdd
	// 호출당시 날짜 입력
	private String saleStrDts;

	// 판매종료일(필수) - yyyyMMdd
	// 종료일이 지정되지 않았을 경우 99991231 입력
	private String saleEndDts;

	// 출시예정일 - yyyyMMdd
	// - 예약판매로 등록할 경우 필수
	private String prdReleaseDt;

	// 상품배송비사용여부(필수) - 상품배송비사용:Y, 업체배송비정책사용:N
	private String proddelvCostUseYn;

	// 묶음배송비번호 - proddelvCostUseYn가 'N'일때 사용
	// 미입력시 기본 업체 배송정책번호로 등록
	private String delvPlcNo;

	// 상품 반품택배비 사용여부(필수) - 상품반품택배비사용:Y, 업체반품택배비사용:N
	private String prdrtnCostUseYn;

	// 상품 반품택배비. prdrtnCostUseYn 가 'Y' 일 경우 필수임
	private Long rtndelvCost;

	// 상품 반품배송지번호. 업체 반품정책 사용하지 않을때 지정
	private String rtndelvNo;

	// 상품 시중가
	// 북마켓 카테고리인 경우 필수 입력
	private Long mktPr;

	// 상품설명(필수)
	private String prdBasisExplanEd;

	// 대표이미지(필수) - 대표이미지 URL, 영문/숫자 조합, JPG와 PNG만 가능
	private String zoomImg;

	// 뒷문구 - 최대 80byte
	private String prdPostfix;

	// 쇼핑태그 - 최대 4개까지, 콤마로 구분
	private String prdKeywd;

	// 상품모델명- 최대 40byte
	private String prdModelNo;

	// 브랜드번호
	private String brandNo;

	// 브랜드명
	private String brandNm;

	// 업체POINT - 업체부여 포인트 금액 입력, 판매가의 최대 10%까지 가능
	private Long entrPoint;

	// 1회당 주문 제한 수량
	private Integer perordRstrQty;

	// 선택형 옵션 이름을 지정할때 사용. 선택1을 타입, 선택2를 색상으로 하는 경우
	// 타입,사이즈 으로 태그 설정. 지정 하지 않은 경우 선택1,선택2로 기본 지정됨
	private String selOptName;

	// 선택형 옵션노출 정렬 유형. 옵션이 있을 경우 필수.
	// 01-등록순, 02-가나다순. 선택형 옵션만 적용됨.
	private String optPrirTp;

	// 선택형 옵션(상품옵션), 옵션이 있을 경우 필수로 선택해야 하는 사항들.
	// http://www.interpark.com/openapi/site/optionnotice.jsp
	// 재고 : A타입-55:1장 /A타입-66:5장 /A타입-77:7장 /B타입-95:10장 /B타입-100:20장 /B타입-105:30장
	// 추가금액 : A타입-55:0원 /A타입-66:2000원 /A타입-77:3000원 /B타입-95:500원 /B타입-100:1000원
	// /B타입-105:1500원
	// 사용여부 : A타입-55:사용안함 /A타입-66:사용함 /A타입-77:사용함 /B타입-95:사용함 /B타입-100:사용함
	// /B타입-105:사용안함 인 경우, 다음과 같이 데이터를 생성.
	// 옵션코드 : A타입-55:0-1 /A타입-66:0-2 /A타입-77:0-3 /B타입-95:0-A /B타입-100:0-B
	// /B타입-105:0-C
	// 1Depth 옵션구조
	// {A타입-55수량<1>추가금액<0>옵션코드<0-1>사용여부<N>}{A타입-66수량<5>추가금액<2000>옵션코드<0-2>사용여부<Y>}{A타입-77수량<10>추가금액<3000>옵션코드<0-3>사용여부<Y>}
	// 2Depth 옵션구조
	// {A타입<55,66,77>수량<1,5,7>추가금액<0,2000,3000>옵션코드<0-1,0-2,0-3>사용여부<N,Y,Y>}{B타입<95,100,105>수량<10,20,30>추가금액<500,1000,1500>옵션코드<0-A,0-B,0-C>사용여부<Y,Y,N>}
	// * 옵션명에 사용 불가한 특수문자 및 단어
	// 사용 불가 특수문자: { , } , < , > , \ , ' , " , ^ , / , |
	// 사용 불가 단어: 수량, 추가금액, 옵션코드, 사용여부
	// - 옵션상품에 대한 재고관리를 따로 하지 않을 경우 수량 영역을 삭제
	// - 옵션의 수량은 옵션별 최대 99,999개 까지 가능
	// - 추가금액을 사용하지 않을 시에는 추가금액 영역을 삭제
	// - 옵션 적용 가격 중 적어도 하나는 판매가와 일치하여야 합니다. (사용 여부가 'Y'인 옵션 상품중 추가금액 0 설정)
	// - 옵션코드는 제휴업체의 내부 옵션 코드
	// - 사용여부를 사용하지 않을 시에는 사용여부 영역을 삭제(미입력시 디폴트로 'Y' 설정됨)
	// - 옵션의 경우 재고가 없거나 해당 상품이 존재하지 않을 경우에 사용여부에 값을 입력하여 옵션등록이 가능합니다.
	private String prdOption;

	// 추가형 옵션(추가구성상품), 주상품에 추가하여 판매할 상품 추가.
	// http://www.interpark.com/openapi/site/optionnotice.jsp
	// 재고 : 가방케이스-블랙:10장 /가방케이스-화이트:20장 /머플러-화이트:5장 /머플러-그레이:10장
	// 금액 : 가방케이스-블랙:500원 /가방케이스-화이트:1000원 /머플러-화이트:1000원 /머플러-그레이:2000원
	// 옵션코드 : 가방케이스-블랙:add-A /가방케이스-화이트:add-B /머플러-화이트:add-C /머플러-그레이:add-D
	// {가방케이스<블랙,화이트>수량<10,20>금액<500,1000>옵션코드<add-A,add-B>}{머플러<화이트,그레이>수량<5,10>금액<1000,2000>옵션코드<add-C,add-D>}
	// * 옵션명에 사용 불가한 특수문자 및 단어
	// 사용 불가 특수문자: { , } , < , > , \ , ' , " , ^ , / , |
	// 사용 불가 단어: 수량, 추가금액, 옵션코드, 사용여부
	// - 옵션상품에 대한 재고관리를 따로 하지 않을 경우 수량 영역을 삭제
	// - 금액을 사용하지 않을 시에는 금액 영역을 삭제
	// - 금액은 반드시 0 이상 이어야 함
	// - 옵션코드는 제휴업체의 내부 옵션 코드
	private String addOption;

	// 상품페이지 추가형 옵션 수량 입력 필드 사용여부
	private String addQtyUseYn;

	// 입력형 옵션. ex) 사은품을 입력하세요.
	private String inOpt;

	// 스마트옵션 사용여부 - Y : 사용, N : 사용안함
	// http://ipss.interpark.com/html/ipss/product/sellerProductSmartOptionGuide.html
	private String smOptYn;

	// 스마트옵션 템플릿, 스마트옵션 사용여부(smOptYn)가 Y일때 적용
	// 미입력시 default '템플릿 적용(가로)'로 등록
	// - 01 : 템플릿 적용(가로)
	// - 02 : 템플릿 적용(세로)
	// - 03 : 템플릿 적용(디자인)
	// - 04 : 템플릿 미적용(가로)
	// - 05 : 템플릿 미적용(세로)
	private String smOptTeplTp;

	// 스마트옵션 사용여부(smOptYn)가 Y인 경우 필수 입력
	// 스마트옵션 사용 시 마이너스 옵션가 사용 불가
	// 선택형옵션(prdOption)의 선택1에 해당하는 옵션 갯수와 스마트옵션의 갯수는 동일해야 함
	// ex) prdOption이 {A타입<55,66>}{B타입<66,77,88>} 인 경우 smOpt은 2개 생성되어야 함(A타입, B타입)
	private String smOptInfo;

	// 스마트옵션 상세설명 상단 HTML, 필수아님
	private String smOptExplanTop;

	// 스마트옵션 상세설명 하단 HTML, 필수아님
	private String smOptExplanBottom;

	// 배송비 -상품 배송비 선택일때 필수, 0이면 무료배송
	private Long delvCost;

	// 일부지역 유료 여부 - Y : 설정함, N : 설정안함
	// 상품 배송비 선택이면서 무료배송일 때에만 설정가능, 필수아님
	private String prdDelvExctYn;

	// 판매기간
	// Y : (항목삭제)설정함. 판매기간 설정이 가능합니다.
	// N : 설정안함(고정가판매의 경우만). 즉시 영구판매가이루어 집니다.
	private String selTermUseYn;

	// 배송비 결제 방식 - 착불:01, 선불:02, 선불전환착불가능:03
	// 상품배송비를 사용할 경우 필수, 무료배송일때:02
	private String delvAmtPayTpCom;
	
	// 착불 배송비 노출 여부 - Y : 노출, N : 비노출
	// 비노출 시 기본배송비와 추가배송비(제주/도서산간) 노출 안함
	private String delvCostDispYn;


	// 배송비 적용 방식 - 개당:01, 무조건:02, n개당:03
	private String delvCostApplyTp;

	// 무료배송기준 수량 - 기준수량 입력
	// 사용하지 않을 경우 0
	private Integer freedelvStdCnt;

	// n개당 배송 주문 수량 - 기준수량 입력
	// 사용하지 않을 경우 0
	private Integer stdQty;

	// 제주도서산간배송비사용여부 - Y : 등록/수정, N : 사용안함
	private String jejuetcDelvCostUseYn;

	// 제주배송비
	// jejuetcDelvCostUseYn가 Y일때 제주배송비와 도서산간비 둘 중 하나는 필수,
	// 0이면 제주배송비 0원, null이면 사용안함
	private Long jejuDelvCost;

	// 도서산간배송비
	// jejuetcDelvCostUseYn가 Y일때 제주배송비와 도서산간비 둘 중 하나는 필수,
	// 0이면 도서산간배송비 0원, null이면 사용안함
	private Long etcDelvCost;

	// 배송방법
	// 택배:01, 우편(소포/등기):02, 화물배달(가구직배송):03,배송필요없음:00
	// (미입력시 디폴트로 '01' 설정됨)
	private String delvMthd;

	// 특이사항
	private String spcaseEd;

	// 무이자할부 사용여부 - Y : 등록/수정, N : 사용안함
	private String totalIntfreeInstmUseYn;

	// 무이자할부시작일 - yyyyMMdd
	private String intfreeInstmStrDts;

	// 무이자할부종료일 - yyyyMMdd
	private String intfreeInstmEndDts;

	// 무이자할부개월수 - 3,6,10,12 개월 선택 입력
	private String listInstmMonths;

	// 가격비교 등록 여부 -등록함:Y, 등록안함:N
	private String ippSubmitYn;

	// 원상품번호(제휴업체상품코드)
	private String originPrdNo;

	// A/S정보(필수)
	private String asInfo;

	// 상세이미지 - 상세이미지 URL, 영문/숫자 조합, JPG와 PNG만 가능
	// 최대 3개의 이미지까지, 콤마(,)로 구분하여 등록.
	// ex1)
	// http://www.interpark.com/image/a.jpg,http://www.interpark.com/image/b.jpg,http://www.interpark.com/image/c.jpg
	private String detailImg;

	// 해외구매대행 또는 해외항공배송 상품
	// - N : 해외구매대행상품 아님
	// - Y : 해외구매대행상품(주문시 개인통관고유부호 수집안함)
	// - I : 해외구매대행상품(주문시 개인통관고유부호 수집함)
	// 입력하지 않은 경우 N으로 적용 됩니다.
	// * 개인통관고유부호 수집시 수집된 정보는 수입통관용으로만 사용하셔야 합니다.
	private String abroadBsYn;

	// 의료기기 인증대상상품의 인증유형
	// - 0301 : 인증대상아님
	// - 0302 : 의료기기 대상품
	// - 0303 : 상품설명 내 표기
	// * 의료기기 판매업 신고 및 표시광고 사전심의 사항을 입력해 주시기 바랍니다.
	// * 허위사실 입력시 관련 법규에 의한 처벌 대상이 될 수 있습니다.
	private String medicalCertTp;

	// 의료기기 판매업 신고기관,
	// 의료기기 인증대상상품의 인증유형(medicalCertTp) 값이 의료기기 대상품(0302)로 입력될 경우 필수 입력
	private String medicalRepOrg;

	// 의료기기 판매업 신고번호,
	// 의료기기 인증대상상품의 인증유형(medicalCertTp) 값이 의료기기 대상품(0302)로 입력될 경우 필수 입력
	private String medicalRepNo;

	// 의료기기 품목허가번호,
	// 의료기기 인증대상상품의 인증유형(medicalCertTp) 값이 의료기기 대상품(0302)로 입력될 경우 필수 입력
	private String medicalPerNo;

	// 의료기기 광고심의번호,
	// 의료기기 인증대상상품의 인증유형(medicalCertTp) 값이 의료기기 대상품(0302)로 입력될 경우 필수 입력
	private String medicalRevNo;

	// 건강기능식품 인증대상상품의 인증유형
	// - 0401 : 인증대상아님
	// - 0402 : 건강기능 대상품
	// - 0403 : 상품설명 내 표기
	// * 건강기능식품 판매업 신고 및 표시광고 사전심의 사항을 입력해 주시기 바랍니다.
	// * 허위사실 입력시 관련 법규에 의한 처벌 대상이 될 수 있습니다.
	private String healthCertTp;

	// 건강기능식품 판매업 신고기관,
	// 건강기능식품 인증대상상품의 인증유형(healthCertTp) 값이 건강기능식품 대상품(0402)로 입력될 경우 필수 입력
	private String healthRepOrg;

	// 건강기능식품 판매업 신고번호,
	// 건강기능식품 인증대상상품의 인증유형(healthCertTp) 값이 건강기능식품 대상품(0402)로 입력될 경우 필수 입력
	private String healthRepNo;

	// 건강기능식품 사전광고심의 대상여부,
	// 건강기능식품 인증대상상품의 인증유형(healthCertTp) 값이 건강기능식품 대상품(0402)로 입력될 경우 필수 입력
	// - 0401 : 사전광고심의 대상
	// - 0402 : 사전광고심의 대상아님
	private String healthCertDtlTp;

	// 건강기능식품 사전광고심의번호,
	// 건강기능식품 사전광고심의 대상여부(healthCertDtlTp) 값이 사전광고심의 대상(0401)인 경우 필수 입력
	private String healthRevNo;

	// 제조/가공식품 인증대상상품의 인증유형
	// - 0501 : 인증대상아님
	// - 0502 : 제조/가공식품
	// - 0503 : 상품설명 내 표기
	// * 식품을 제조/가공하여 판매할 경우 신고기관 및 신고번호를 입력해 주시기 바랍니다.
	// * 허위사실 입력시 관련 법규에 의한 처벌 대상이 될 수 있습니다.
	private String foodCertTp;

	// 제조/가공식품 신고기관,
	// 제조/가공식품 인증대상상품의 인증유형(foodCertTp) 값이 제조/가공식품(0502)인 경우 필수 입력
	private String foodRepOrg;

	// 제조/가공식품 신고번호,
	// 제조/가공식품 인증대상상품의 인증유형(foodCertTp) 값이 제조/가공식품(0502)인 경우 필수 입력
	private String foodRepNo;

	// 친환경 인증정보 등록여부
	// 신선식품(001770) 카테고리만 등록 가능
	// - N : 등록된 인증정보 삭제
	private String ecoCertYn;

	// 친환경 인증대상상품의 인증유형
	// ecoCertYn 값이 N이 아닌 경우 필수 입력
	// - 01 : 유기농산물
	// - 02 : 무농약농산물
	// - 03 : 저농약농산물
	// - 04 : 유기축산물
	// - 05 : 무항생제축산물
	private String ecoCertTp;

	// 친환경 인증대상상품의 인증기관 - 최대 100byte
	// ecoCertYn 값이 N이 아닌 경우 필수 입력
	private String ecoCertOrg;

	// 친환경 인증대상상품의 인증번호 - 최대 30byte
	// ecoCertYn 값이 N이 아닌 경우 필수 입력
	private String ecoCertNo;

	// 북마켓 카테고리인 경우 필수 입력 ( 온라인강의, 음반, DVD 카테고리 제외 )
	private String isbn;

	// 중고상품사용개월수, 상품상태(prdStat) 값이 중고(02)인 경우 필수 입력
	private String oldUseMonth;

	// 바코드(88코드/KAN코드) - 최대 20byte
	private String barcode;

	// 상품인증여부
	// 생활용품/전기용품/방송통신용품/어린이제품에 대한 인증 여부
	// (의료기기, 건강기능식품, 제조/가공식품 인증 대상 제외)
	// - Y : 인증함
	// - N : 인증대상아님
	// - S : 상품설명 내 별도 표기
	// * 허위사실 입력시 관련 법규에 의한 처벌 대상이 될 수 있습니다.
	private String prdCertStatus;

	// 인증 KC 안전관리대상 선택고지사항
	// 상품인증여부(prdCertStatus)가 Y인경우 선택입력
	// 구매대행 or 병행수입 선택시 인증정보(prdCertDetail) 입력 필수 아님
	// - 01 : 구매대행 상품
	// - 02 : 병행수입 상품
	private String prdCertOptTp;

	// 상품인증여부(prdCertStatus)가 Y로 입력될 경우 필수 입력이나
	// 인증 안전관대상(prdCertOptTp)를 입력하는 경우 필수 입력 아님
	// 복수의 인증상세유형 등록 가능
	private String prdCertDetail;

	// 상품상태(saleStatTp)가 판매금지(98)가 아닌경우 필수 입력
	@XmlElementWrapper(name = "prdinfoNoti")
	@XmlElement(name = "info")
	private List<Info> prdinfoNoti;

	// 사은품 문구
	// 사은품 정보에 대한 설명.
	private String giftInfo;

	// 기준요금제
	// 휴대폰(할부) 상품 기준요금제 정보
	// 휴대폰 카테고리(001170001, 001170002, 001170003, 001170020)
	// 위 전시번호일 경우는 필수입력
	private String planTxt;

	// 휴대폰 가입비 (현재 사용안함 필드)
	private String membershipTp;

	// 휴대폰 유심비
	// 휴대폰 카테고리 경우는 필수입력
	// - E : 면제
	// - S : 후납
	private String usimTp;

	// 판매자부담 즉시할인 설정여부
	// - Y : 설정함
	// - N : 설정안함
	private String entrDcUseYn;

	// 판매자부담 즉시할인 할인구분
	// 판매자부담 즉시할인 설정여부(entrDcUseYn)가 "Y"일 경우 필수 입력
	// - 1 : 정률
	// - 2 : 정액
	private String entrDcTp;

	// 판매자부담 즉시할인 할인률/할인금액
	// 판매자부담 즉시할인 설정여부(entrDcUseYn)가 "Y"일 경우 필수 입력
	// 판매가의 70%까지 등록 가능, 정률일경우 소수점 둘째짜리까지 가능
	private Double entrDcNum;

	// 판매자부담 즉시할인 할인시작일 - yyyyMMdd
	// 미입력시 상품 판매기간동안 할인 적용
	private String entrDcStrDt;

	// 판매자부담 즉시할인 할인종료일 - yyyyMMdd
	// 미입력시 상품 판매기간동안 할인 적용
	private String entrDcEndDt;

	// 배송속성
	// - 01 : 일반배송
	// - 02 : 오늘출발
	// 미입력시 일반배송(01) 적용
	private String delvAttbt;

	// 별도 설치비 유무(Y/N)
	// 미입력시 'N'
	private String InstallCostYn;

	// 주문제작 여부(Y/N)
	// 미입력시 'N'
	private String customMadeYn;

	// 주문제작시 발송예정일
	// 주문제작 여부 - 'Y' 입력시 필수
	private String stdDelvwhDt;

	// 맞춤제작 여부(Y/N)
	// 미입력시 'N'
	private String customPrdYn;

	// 수입신고번호
	// 하이픈(-)을 제거한 14자리 일련번호
	private String importNo;

	// 수입신고필증 이미지-수입신고필증 이미지 URL, JPG와 PNG만 가능
	private String importLicense;
	
	// 이미지수정여부 - 대표이미지,상세이미지의 수정여부를 결정 합니다.
	// Y : 이미지 수정 필요
	// N : 이미지 수정 불필요
	// (기본값 : N)
	// 대표이미지나 상세이미지 중에 하나만이라도 수정이 필요한 경우 Y로 설정해야 합니다.
	private String imgUpdateYn;

	public String getPrdStat() {
		return prdStat;
	}

	public void setPrdStat(String prdStat) {
		this.prdStat = prdStat;
	}

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}

	public String getOmDispNo() {
		return omDispNo;
	}

	public void setOmDispNo(String omDispNo) {
		this.omDispNo = omDispNo;
	}

	public String getPrdNm() {
		return prdNm;
	}

	public void setPrdNm(String prdNm) {
		this.prdNm = prdNm;
	}

	public String getHdelvMafcEntrNm() {
		return hdelvMafcEntrNm;
	}

	public void setHdelvMafcEntrNm(String hdelvMafcEntrNm) {
		this.hdelvMafcEntrNm = hdelvMafcEntrNm;
	}

	public String getPrdOriginTp() {
		return prdOriginTp;
	}

	public void setPrdOriginTp(String prdOriginTp) {
		this.prdOriginTp = prdOriginTp;
	}

	public String getTaxTp() {
		return taxTp;
	}

	public void setTaxTp(String taxTp) {
		this.taxTp = taxTp;
	}

	public String getOrdAgeRstrYn() {
		return ordAgeRstrYn;
	}

	public void setOrdAgeRstrYn(String ordAgeRstrYn) {
		this.ordAgeRstrYn = ordAgeRstrYn;
	}

	public String getSaleStatTp() {
		return saleStatTp;
	}

	public void setSaleStatTp(String saleStatTp) {
		this.saleStatTp = saleStatTp;
	}

	public Long getSaleUnitcost() {
		return saleUnitcost;
	}

	public void setSaleUnitcost(Long saleUnitcost) {
		this.saleUnitcost = saleUnitcost;
	}

	public String getSaleLmtQty() {
		return saleLmtQty;
	}

	public void setSaleLmtQty(String saleLmtQty) {
		this.saleLmtQty = saleLmtQty;
	}

	public String getSaleStrDts() {
		return saleStrDts;
	}

	public void setSaleStrDts(String saleStrDts) {
		this.saleStrDts = saleStrDts;
	}

	public String getSaleEndDts() {
		return saleEndDts;
	}

	public void setSaleEndDts(String saleEndDts) {
		this.saleEndDts = saleEndDts;
	}

	public String getPrdReleaseDt() {
		return prdReleaseDt;
	}

	public void setPrdReleaseDt(String prdReleaseDt) {
		this.prdReleaseDt = prdReleaseDt;
	}

	public String getProddelvCostUseYn() {
		return proddelvCostUseYn;
	}

	public void setProddelvCostUseYn(String proddelvCostUseYn) {
		this.proddelvCostUseYn = proddelvCostUseYn;
	}

	public String getDelvPlcNo() {
		return delvPlcNo;
	}

	public void setDelvPlcNo(String delvPlcNo) {
		this.delvPlcNo = delvPlcNo;
	}

	public String getPrdrtnCostUseYn() {
		return prdrtnCostUseYn;
	}

	public void setPrdrtnCostUseYn(String prdrtnCostUseYn) {
		this.prdrtnCostUseYn = prdrtnCostUseYn;
	}

	public Long getRtndelvCost() {
		return rtndelvCost;
	}

	public void setRtndelvCost(Long rtndelvCost) {
		this.rtndelvCost = rtndelvCost;
	}

	public String getRtndelvNo() {
		return rtndelvNo;
	}

	public void setRtndelvNo(String rtndelvNo) {
		this.rtndelvNo = rtndelvNo;
	}

	public Long getMktPr() {
		return mktPr;
	}

	public void setMktPr(Long mktPr) {
		this.mktPr = mktPr;
	}

	public String getPrdBasisExplanEd() {
		return prdBasisExplanEd;
	}

	public void setPrdBasisExplanEd(String prdBasisExplanEd) {
		this.prdBasisExplanEd = prdBasisExplanEd;
	}

	public String getZoomImg() {
		return zoomImg;
	}

	public void setZoomImg(String zoomImg) {
		this.zoomImg = zoomImg;
	}

	public String getPrdPostfix() {
		return prdPostfix;
	}

	public void setPrdPostfix(String prdPostfix) {
		this.prdPostfix = prdPostfix;
	}

	public String getPrdKeywd() {
		return prdKeywd;
	}

	public void setPrdKeywd(String prdKeywd) {
		this.prdKeywd = prdKeywd;
	}

	public String getPrdModelNo() {
		return prdModelNo;
	}

	public void setPrdModelNo(String prdModelNo) {
		this.prdModelNo = prdModelNo;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getBrandNm() {
		return brandNm;
	}

	public void setBrandNm(String brandNm) {
		this.brandNm = brandNm;
	}

	public Long getEntrPoint() {
		return entrPoint;
	}

	public void setEntrPoint(Long entrPoint) {
		this.entrPoint = entrPoint;
	}

	public Integer getPerordRstrQty() {
		return perordRstrQty;
	}

	public void setPerordRstrQty(Integer perordRstrQty) {
		this.perordRstrQty = perordRstrQty;
	}

	public String getSelOptName() {
		return selOptName;
	}

	public void setSelOptName(String selOptName) {
		this.selOptName = selOptName;
	}

	public String getOptPrirTp() {
		return optPrirTp;
	}

	public void setOptPrirTp(String optPrirTp) {
		this.optPrirTp = optPrirTp;
	}

	public String getPrdOption() {
		return prdOption;
	}

	public void setPrdOption(String prdOption) {
		this.prdOption = prdOption;
	}

	public String getAddOption() {
		return addOption;
	}

	public void setAddOption(String addOption) {
		this.addOption = addOption;
	}

	public String getAddQtyUseYn() {
		return addQtyUseYn;
	}

	public void setAddQtyUseYn(String addQtyUseYn) {
		this.addQtyUseYn = addQtyUseYn;
	}

	public String getInOpt() {
		return inOpt;
	}

	public void setInOpt(String inOpt) {
		this.inOpt = inOpt;
	}

	public String getSmOptYn() {
		return smOptYn;
	}

	public void setSmOptYn(String smOptYn) {
		this.smOptYn = smOptYn;
	}

	public String getSmOptTeplTp() {
		return smOptTeplTp;
	}

	public void setSmOptTeplTp(String smOptTeplTp) {
		this.smOptTeplTp = smOptTeplTp;
	}

	public String getSmOptInfo() {
		return smOptInfo;
	}

	public void setSmOptInfo(String smOptInfo) {
		this.smOptInfo = smOptInfo;
	}

	public String getSmOptExplanTop() {
		return smOptExplanTop;
	}

	public void setSmOptExplanTop(String smOptExplanTop) {
		this.smOptExplanTop = smOptExplanTop;
	}

	public String getSmOptExplanBottom() {
		return smOptExplanBottom;
	}

	public void setSmOptExplanBottom(String smOptExplanBottom) {
		this.smOptExplanBottom = smOptExplanBottom;
	}

	public Long getDelvCost() {
		return delvCost;
	}

	public void setDelvCost(Long delvCost) {
		this.delvCost = delvCost;
	}

	public String getPrdDelvExctYn() {
		return prdDelvExctYn;
	}

	public void setPrdDelvExctYn(String prdDelvExctYn) {
		this.prdDelvExctYn = prdDelvExctYn;
	}

	public String getSelTermUseYn() {
		return selTermUseYn;
	}

	public void setSelTermUseYn(String selTermUseYn) {
		this.selTermUseYn = selTermUseYn;
	}

	public String getDelvAmtPayTpCom() {
		return delvAmtPayTpCom;
	}

	public void setDelvAmtPayTpCom(String delvAmtPayTpCom) {
		this.delvAmtPayTpCom = delvAmtPayTpCom;
	}

	public String getDelvCostApplyTp() {
		return delvCostApplyTp;
	}

	public void setDelvCostApplyTp(String delvCostApplyTp) {
		this.delvCostApplyTp = delvCostApplyTp;
	}

	public Integer getFreedelvStdCnt() {
		return freedelvStdCnt;
	}

	public void setFreedelvStdCnt(Integer freedelvStdCnt) {
		this.freedelvStdCnt = freedelvStdCnt;
	}

	public Integer getStdQty() {
		return stdQty;
	}

	public void setStdQty(Integer stdQty) {
		this.stdQty = stdQty;
	}

	public String getJejuetcDelvCostUseYn() {
		return jejuetcDelvCostUseYn;
	}

	public void setJejuetcDelvCostUseYn(String jejuetcDelvCostUseYn) {
		this.jejuetcDelvCostUseYn = jejuetcDelvCostUseYn;
	}

	public Long getJejuDelvCost() {
		return jejuDelvCost;
	}

	public void setJejuDelvCost(Long jejuDelvCost) {
		this.jejuDelvCost = jejuDelvCost;
	}

	public Long getEtcDelvCost() {
		return etcDelvCost;
	}

	public void setEtcDelvCost(Long etcDelvCost) {
		this.etcDelvCost = etcDelvCost;
	}

	public String getDelvMthd() {
		return delvMthd;
	}

	public void setDelvMthd(String delvMthd) {
		this.delvMthd = delvMthd;
	}

	public String getSpcaseEd() {
		return spcaseEd;
	}

	public void setSpcaseEd(String spcaseEd) {
		this.spcaseEd = spcaseEd;
	}

	public String getTotalIntfreeInstmUseYn() {
		return totalIntfreeInstmUseYn;
	}

	public void setTotalIntfreeInstmUseYn(String totalIntfreeInstmUseYn) {
		this.totalIntfreeInstmUseYn = totalIntfreeInstmUseYn;
	}

	public String getIntfreeInstmStrDts() {
		return intfreeInstmStrDts;
	}

	public void setIntfreeInstmStrDts(String intfreeInstmStrDts) {
		this.intfreeInstmStrDts = intfreeInstmStrDts;
	}

	public String getIntfreeInstmEndDts() {
		return intfreeInstmEndDts;
	}

	public void setIntfreeInstmEndDts(String intfreeInstmEndDts) {
		this.intfreeInstmEndDts = intfreeInstmEndDts;
	}

	public String getListInstmMonths() {
		return listInstmMonths;
	}

	public void setListInstmMonths(String listInstmMonths) {
		this.listInstmMonths = listInstmMonths;
	}

	public String getIppSubmitYn() {
		return ippSubmitYn;
	}

	public void setIppSubmitYn(String ippSubmitYn) {
		this.ippSubmitYn = ippSubmitYn;
	}

	public String getOriginPrdNo() {
		return originPrdNo;
	}

	public void setOriginPrdNo(String originPrdNo) {
		this.originPrdNo = originPrdNo;
	}

	public String getAsInfo() {
		return asInfo;
	}

	public void setAsInfo(String asInfo) {
		this.asInfo = asInfo;
	}

	public String getDetailImg() {
		return detailImg;
	}

	public void setDetailImg(String detailImg) {
		this.detailImg = detailImg;
	}

	public String getAbroadBsYn() {
		return abroadBsYn;
	}

	public void setAbroadBsYn(String abroadBsYn) {
		this.abroadBsYn = abroadBsYn;
	}

	public String getMedicalCertTp() {
		return medicalCertTp;
	}

	public void setMedicalCertTp(String medicalCertTp) {
		this.medicalCertTp = medicalCertTp;
	}

	public String getMedicalRepOrg() {
		return medicalRepOrg;
	}

	public void setMedicalRepOrg(String medicalRepOrg) {
		this.medicalRepOrg = medicalRepOrg;
	}

	public String getMedicalRepNo() {
		return medicalRepNo;
	}

	public void setMedicalRepNo(String medicalRepNo) {
		this.medicalRepNo = medicalRepNo;
	}

	public String getMedicalPerNo() {
		return medicalPerNo;
	}

	public void setMedicalPerNo(String medicalPerNo) {
		this.medicalPerNo = medicalPerNo;
	}

	public String getMedicalRevNo() {
		return medicalRevNo;
	}

	public void setMedicalRevNo(String medicalRevNo) {
		this.medicalRevNo = medicalRevNo;
	}

	public String getHealthCertTp() {
		return healthCertTp;
	}

	public void setHealthCertTp(String healthCertTp) {
		this.healthCertTp = healthCertTp;
	}

	public String getHealthRepOrg() {
		return healthRepOrg;
	}

	public void setHealthRepOrg(String healthRepOrg) {
		this.healthRepOrg = healthRepOrg;
	}

	public String getHealthRepNo() {
		return healthRepNo;
	}

	public void setHealthRepNo(String healthRepNo) {
		this.healthRepNo = healthRepNo;
	}

	public String getHealthCertDtlTp() {
		return healthCertDtlTp;
	}

	public void setHealthCertDtlTp(String healthCertDtlTp) {
		this.healthCertDtlTp = healthCertDtlTp;
	}

	public String getHealthRevNo() {
		return healthRevNo;
	}

	public void setHealthRevNo(String healthRevNo) {
		this.healthRevNo = healthRevNo;
	}

	public String getFoodCertTp() {
		return foodCertTp;
	}

	public void setFoodCertTp(String foodCertTp) {
		this.foodCertTp = foodCertTp;
	}

	public String getFoodRepOrg() {
		return foodRepOrg;
	}

	public void setFoodRepOrg(String foodRepOrg) {
		this.foodRepOrg = foodRepOrg;
	}

	public String getFoodRepNo() {
		return foodRepNo;
	}

	public void setFoodRepNo(String foodRepNo) {
		this.foodRepNo = foodRepNo;
	}

	public String getEcoCertYn() {
		return ecoCertYn;
	}

	public void setEcoCertYn(String ecoCertYn) {
		this.ecoCertYn = ecoCertYn;
	}

	public String getEcoCertTp() {
		return ecoCertTp;
	}

	public void setEcoCertTp(String ecoCertTp) {
		this.ecoCertTp = ecoCertTp;
	}

	public String getEcoCertOrg() {
		return ecoCertOrg;
	}

	public void setEcoCertOrg(String ecoCertOrg) {
		this.ecoCertOrg = ecoCertOrg;
	}

	public String getEcoCertNo() {
		return ecoCertNo;
	}

	public void setEcoCertNo(String ecoCertNo) {
		this.ecoCertNo = ecoCertNo;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getOldUseMonth() {
		return oldUseMonth;
	}

	public void setOldUseMonth(String oldUseMonth) {
		this.oldUseMonth = oldUseMonth;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getPrdCertStatus() {
		return prdCertStatus;
	}

	public void setPrdCertStatus(String prdCertStatus) {
		this.prdCertStatus = prdCertStatus;
	}

	public String getPrdCertOptTp() {
		return prdCertOptTp;
	}

	public void setPrdCertOptTp(String prdCertOptTp) {
		this.prdCertOptTp = prdCertOptTp;
	}

	public String getPrdCertDetail() {
		return prdCertDetail;
	}

	public void setPrdCertDetail(String prdCertDetail) {
		this.prdCertDetail = prdCertDetail;
	}

	public List<Info> getPrdinfoNoti() {
		return prdinfoNoti;
	}

	public void setPrdinfoNoti(List<Info> prdinfoNoti) {
		this.prdinfoNoti = prdinfoNoti;
	}

	public String getGiftInfo() {
		return giftInfo;
	}

	public void setGiftInfo(String giftInfo) {
		this.giftInfo = giftInfo;
	}

	public String getPlanTxt() {
		return planTxt;
	}

	public void setPlanTxt(String planTxt) {
		this.planTxt = planTxt;
	}

	public String getMembershipTp() {
		return membershipTp;
	}

	public void setMembershipTp(String membershipTp) {
		this.membershipTp = membershipTp;
	}

	public String getUsimTp() {
		return usimTp;
	}

	public void setUsimTp(String usimTp) {
		this.usimTp = usimTp;
	}

	public String getEntrDcUseYn() {
		return entrDcUseYn;
	}

	public void setEntrDcUseYn(String entrDcUseYn) {
		this.entrDcUseYn = entrDcUseYn;
	}

	public String getEntrDcTp() {
		return entrDcTp;
	}

	public void setEntrDcTp(String entrDcTp) {
		this.entrDcTp = entrDcTp;
	}

	public Double getEntrDcNum() {
		return entrDcNum;
	}

	public void setEntrDcNum(Double entrDcNum) {
		this.entrDcNum = entrDcNum;
	}

	public String getEntrDcStrDt() {
		return entrDcStrDt;
	}

	public void setEntrDcStrDt(String entrDcStrDt) {
		this.entrDcStrDt = entrDcStrDt;
	}

	public String getEntrDcEndDt() {
		return entrDcEndDt;
	}

	public void setEntrDcEndDt(String entrDcEndDt) {
		this.entrDcEndDt = entrDcEndDt;
	}

	public String getDelvAttbt() {
		return delvAttbt;
	}

	public void setDelvAttbt(String delvAttbt) {
		this.delvAttbt = delvAttbt;
	}

	public String getInstallCostYn() {
		return InstallCostYn;
	}

	public void setInstallCostYn(String installCostYn) {
		InstallCostYn = installCostYn;
	}

	public String getCustomMadeYn() {
		return customMadeYn;
	}

	public void setCustomMadeYn(String customMadeYn) {
		this.customMadeYn = customMadeYn;
	}

	public String getStdDelvwhDt() {
		return stdDelvwhDt;
	}

	public void setStdDelvwhDt(String stdDelvwhDt) {
		this.stdDelvwhDt = stdDelvwhDt;
	}

	public String getCustomPrdYn() {
		return customPrdYn;
	}

	public void setCustomPrdYn(String customPrdYn) {
		this.customPrdYn = customPrdYn;
	}

	public String getImportNo() {
		return importNo;
	}

	public void setImportNo(String importNo) {
		this.importNo = importNo;
	}

	public String getImportLicense() {
		return importLicense;
	}

	public void setImportLicense(String importLicense) {
		this.importLicense = importLicense;
	}

	public String getPrdNo() {
		return prdNo;
	}

	public void setPrdNo(String prdNo) {
		this.prdNo = prdNo;
	}

	@Override
	public String toString() {
		return "Item [prdNo=" + prdNo + ", prdStat=" + prdStat + ", shopNo=" + shopNo + ", omDispNo=" + omDispNo
				+ ", prdNm=" + prdNm + ", hdelvMafcEntrNm=" + hdelvMafcEntrNm + ", prdOriginTp=" + prdOriginTp
				+ ", taxTp=" + taxTp + ", ordAgeRstrYn=" + ordAgeRstrYn + ", saleStatTp=" + saleStatTp
				+ ", saleUnitcost=" + saleUnitcost + ", saleLmtQty=" + saleLmtQty + ", saleStrDts=" + saleStrDts
				+ ", saleEndDts=" + saleEndDts + ", prdReleaseDt=" + prdReleaseDt + ", proddelvCostUseYn="
				+ proddelvCostUseYn + ", delvPlcNo=" + delvPlcNo + ", prdrtnCostUseYn=" + prdrtnCostUseYn
				+ ", rtndelvCost=" + rtndelvCost + ", rtndelvNo=" + rtndelvNo + ", mktPr=" + mktPr
				+ ", prdBasisExplanEd=" + prdBasisExplanEd + ", zoomImg=" + zoomImg + ", prdPostfix=" + prdPostfix
				+ ", prdKeywd=" + prdKeywd + ", prdModelNo=" + prdModelNo + ", brandNo=" + brandNo + ", brandNm="
				+ brandNm + ", entrPoint=" + entrPoint + ", perordRstrQty=" + perordRstrQty + ", selOptName="
				+ selOptName + ", optPrirTp=" + optPrirTp + ", prdOption=" + prdOption + ", addOption=" + addOption
				+ ", addQtyUseYn=" + addQtyUseYn + ", inOpt=" + inOpt + ", smOptYn=" + smOptYn + ", smOptTeplTp="
				+ smOptTeplTp + ", smOptInfo=" + smOptInfo + ", smOptExplanTop=" + smOptExplanTop
				+ ", smOptExplanBottom=" + smOptExplanBottom + ", delvCost=" + delvCost + ", prdDelvExctYn="
				+ prdDelvExctYn + ", selTermUseYn=" + selTermUseYn + ", delvAmtPayTpCom=" + delvAmtPayTpCom + ", delvCostDispYn=" + delvCostDispYn
				+ ", delvCostApplyTp=" + delvCostApplyTp + ", freedelvStdCnt=" + freedelvStdCnt + ", stdQty=" + stdQty
				+ ", jejuetcDelvCostUseYn=" + jejuetcDelvCostUseYn + ", jejuDelvCost=" + jejuDelvCost + ", etcDelvCost="
				+ etcDelvCost + ", delvMthd=" + delvMthd + ", spcaseEd=" + spcaseEd + ", totalIntfreeInstmUseYn="
				+ totalIntfreeInstmUseYn + ", intfreeInstmStrDts=" + intfreeInstmStrDts + ", intfreeInstmEndDts="
				+ intfreeInstmEndDts + ", listInstmMonths=" + listInstmMonths + ", ippSubmitYn=" + ippSubmitYn
				+ ", originPrdNo=" + originPrdNo + ", asInfo=" + asInfo + ", detailImg=" + detailImg + ", abroadBsYn="
				+ abroadBsYn + ", medicalCertTp=" + medicalCertTp + ", medicalRepOrg=" + medicalRepOrg
				+ ", medicalRepNo=" + medicalRepNo + ", medicalPerNo=" + medicalPerNo + ", medicalRevNo=" + medicalRevNo
				+ ", healthCertTp=" + healthCertTp + ", healthRepOrg=" + healthRepOrg + ", healthRepNo=" + healthRepNo
				+ ", healthCertDtlTp=" + healthCertDtlTp + ", healthRevNo=" + healthRevNo + ", foodCertTp=" + foodCertTp
				+ ", foodRepOrg=" + foodRepOrg + ", foodRepNo=" + foodRepNo + ", ecoCertYn=" + ecoCertYn
				+ ", ecoCertTp=" + ecoCertTp + ", ecoCertOrg=" + ecoCertOrg + ", ecoCertNo=" + ecoCertNo + ", isbn="
				+ isbn + ", oldUseMonth=" + oldUseMonth + ", barcode=" + barcode + ", prdCertStatus=" + prdCertStatus
				+ ", prdCertOptTp=" + prdCertOptTp + ", prdCertDetail=" + prdCertDetail + ", prdinfoNoti=" + prdinfoNoti
				+ ", giftInfo=" + giftInfo + ", planTxt=" + planTxt + ", membershipTp=" + membershipTp + ", usimTp="
				+ usimTp + ", entrDcUseYn=" + entrDcUseYn + ", entrDcTp=" + entrDcTp + ", entrDcNum=" + entrDcNum
				+ ", entrDcStrDt=" + entrDcStrDt + ", entrDcEndDt=" + entrDcEndDt + ", delvAttbt=" + delvAttbt
				+ ", InstallCostYn=" + InstallCostYn + ", customMadeYn=" + customMadeYn + ", stdDelvwhDt=" + stdDelvwhDt
				+ ", customPrdYn=" + customPrdYn + ", importNo=" + importNo + ", importLicense=" + importLicense + "]";
	}

	public String getImgUpdateYn() {
		return imgUpdateYn;
	}

	public void setImgUpdateYn(String imgUpdateYn) {
		this.imgUpdateYn = imgUpdateYn;
	}

	public String getDelvCostDispYn() {
		return delvCostDispYn;
	}
	
	public void setDelvCostDispYn(String delvCostDispYn) {
		this.delvCostDispYn = delvCostDispYn;
	}

}
