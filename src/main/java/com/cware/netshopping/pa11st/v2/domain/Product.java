package com.cware.netshopping.pa11st.v2.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 상품
 * 
 * 상품등록 예제샘플모음: http://openapi.11st.co.kr/example/ProductExample1705.zip 셀러오피스
 * 매뉴얼(상품관리편): http://i.011st.com/ui_img/seller/pdf/prod.pdf
 *
 */
@XmlRootElement(name = "Product")
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

	// 해외상품코드
	// 국내 셀러일 경우 생략
	// A : 해당 브랜드 정식 도매
	// B : 해당 브랜드 직영 온,오프라인 매장(백화점포함)
	// C : 오프라인 아울렛
	// D : 현지 온라인 쇼핑몰
	// E : A~D에 해당되지 않는 구입처(경매 등)
	private String abrdBuyPlace;

	// 해외사이즈 조견표 노출여부
	// 해외셀러에만 해당하는 부분이며, ‘Y’: 노출함, ‘N’: 노출 안함 으로 구분됩니다.
	// 해당 필드는 선택사항이고, 입력하지 않을 시에는 디폴트 값인 노출함으로 등록됩니다.
	// 기존 해외쇼핑상품의 경우 옵션 있는 경우 무조건 사이즈 조견표가 노출되었는데,
	// 셀러가 상품의 사이즈 조견표 노출여부를 직접 설정할 수 있도록 추가 되었습니다.
	// Y : 노출
	// N : 노출안함
	private String abrdSizetableDispYn;

	// 닉네임
	// 특수문자 등이 포함되어 있을 경우 <![CDATA[ ]]> 로 묶어 주세요.
	// 닉네임을 입력하지 않으면 대표 닉네임이 자동으로 등록됩니다.
	private String selMnbdNckNm;

	// 판매방식 (필수)
	// "판매방식"은 반드시 입력해주셔야 합니다.
	// 현재 api 서비스에서는 "고정가판매"와 "예약판매", "중고판매"만 제공합니다.
	// 01 : 고정가판매
	// 02 : 사용안함
	// 03 : 사용안함
	// 04 : 예약판매
	// 05 : 중고판매
	private String selMthdCd;

	// 카테고리 번호 (필수)
	// 최하위 카테고리만 입력가능합니다.
	// 세카테고리를 입력하셔야 하며 세카테고리가 없는 경우 소카테고리를 입력하셔야 합니다.
	// 카테고리번호 조회 서비스를 이용하여 실시간 조회가 가능합니다.
	// 카테고리 수정은 세카테고리 (혹은 소카테고리) 까지만 가능합니다.
	// 대카테고리, 중카테고리를 변경하고자 할 경우 상품을 새로 등록해주세요.
	private String dispCtgrNo;

	// 서비스 상품 코드 (필수)
	// 여행 카테고리 선택 시 타입을 구분하여 등록할 수 있습니다.
	// 여행 제휴사 상품은 여행 상품등록처리 API를 이용해 주시기 바랍니다.
	// 01 : 일반배송상품
	private String prdTypCd;

	// 전세계배송 HSCode (필수)
	// 대한민국 관세청에 신고되는 HSCode 입니다.
	// dlvCstInstBasiCd 유형이 (10)11번가해외배송조건부배송비 (11번가 해외 배송을 사용하는 경우)일 때 필수입니다.
	// SO 혹은 PO의 상품등록 카테고리에서 기본 HSCode를 확인 하실 수 있습니다.
	// 상품별 성격에 맞는 HSCode를 선택하셔야 하며, 잘못 등록하여 문제가 발생할 경우 셀러분께서 해결하셔야 합니다.
	// 아래 첨부파일을 참조 부탁 드립니다.
	// (http://soffice.11st.co.kr/download/product/gblHscodeList.xls) 참조
	private String gblHsCode;

	// 상품명 (필수)
	// 특수문자 등이 포함되어 있을 경우 <![CDATA[ ]]> 로 묶어 주세요.
	// 글자수는 100Byte로 제한됩니다.
	// 한글 50자, 영문/숫자 100자 이내로 입력을 권장합니다.
	// 입력이 불가한 특수문자가 포함될 경우, 해당 문자는 상품명에서 자동 미노출처리 됩니다
	// *단일상품명은 등록 15일 이후 상품명 수정이 불가능합니다.
	// 빈번한 수정이 필요한 부분은 홍보상품명 영역을 활용해주세요.
	// 클린 상품명은 검색 노출에 도움이 될 수 있습니다.
	private String prdNm;

	// 영문 상품명
	// 특수문자 등이 포함되어 있을 경우 <![CDATA[ ]]> 로 묶어 주세요.
	// 글자수는 100Byte 로 제한됩니다.
	// 영문/숫자 100자 이내로 입력해 주십시오.
	// 입력이 불가한 특수문자가 포함될 경우, 해당 문자는 상품명에서 자동 미노출처리 됩니다.
	private String prdNmEng;

	// 상품홍보문구
	// 특수문자 등이 포함되어 있을 경우 <![CDATA[ ]]> 로 묶어 주세요.
	// 글자수는 28Byte로 제한됩니다.
	// 한글 14자, 영문/숫자 28자 이내로 입력을 권장합니다.
	// 클린 홍보문구는 검색 노출에 도움이 될 수 있습니다.
	private String advrtStmt;

	// 브랜드 (필수)
	// 브랜드를 정확히 입력하면 해당 상품의 검색 노출이 더 많아집니다.
	// 브랜드는 텍스트 형태로만 입력 브랜드 관련 서비스에 전시를 위해서는 브랜드명을 정확히 입력해 주셔야 합니다.
	// 특히 스펠링에 유의해주세요.
	// *브랜드가 없을 시, "알수없음"을 입력해 주십시오.
	// *아래 브랜드 코드를 입력 시, 브랜드 값을 입력하지 않아도 됩니다.
	private String brand;

	// 브랜드코드
	// 관리 브랜드 코드를 입력해주세요.
	// 브랜드 코드 직접 입력시, 위의 brand 입력 값보다 우선해서 적용됩니다.
	private String apiPrdAttrBrandCd;

	// 원재료 유형 코드 (필수)
	// 01 : 농산물
	// 02 : 수산물
	// 03 : 가공품
	// 04 : 원산지 의무 표시대상 아님
	// 05 : 상품별 원산지는 상세설명 참조
	private String rmaterialTypCd;

	// 원산지 코드 (필수)
	// 01 : 국내. 국내원산지 코드를 같이 입력해야 합니다.
	// 02 : 해외. 해외원산지 코드를 같이 입력해야 합니다.
	// 03 : 기타. 원산지명을 입력해야합니다.
	private String orgnTypCd;

	// 원산지가 다른 상품 같이 등록
	// 원산지가 한 군데 이상인 경우 "Y" 입력해주세요.
	private String orgnDifferentYn;

	// 원산지 지역 코드
	// 원산지 코드가 "국내", "해외"일 경우 원산지 지역 코드 값을 입력하셔야 합니다.
	// http://image.11st.co.kr/openapi/area.xlsx
	private String orgnTypDtlsCd;

	// 원산지명
	// 원산지 코드가 "기타"일 경우 원산지명을 입력하셔야 합니다.
	private String orgnNmVal;

	// 원재료 정보
	// 원재료 유형이 가공품(03)일 경우 원재료 정보를 입력하셔야 합니다.
	// 상품은 최대 10개, 상품의 원재료 성분 정보는 최대 5개까지 등록 가능합니다.
	@XmlElement(name = "ProductRmaterial")
	private List<ProductRmaterial> productRmaterial;

	// 축산물 이력번호 (필수)
	// 식품> 축산 > 수입쇠고기 카테고리 선택 시 축산물 이력번호는 필수 입력 사항입니다.
	// 01 : 이력번호 표시대상 제품
	// 02 : 이력번호 표시대상 아님
	// 03 : 상세설명 참조
	private String beefTraceStat;

	// 이력번호 표시대상 제품
	// 01 : 이력번호 표시대상제품 선택시 이력번호 표시대상 제품(xxxx)에 들어갈 내용을 입력합니다.
	// 특수문자 등이 포함되어 있을 경우 <![CDATA[ ]]> 로 묶어 주세요.
	// 글자수는 20Byte 로 제한됩니다.
	// 한글 10자, 영문/숫자 20자 이내로 입력해 주십시오.
	private String beefTraceNo;

	// 판매자상품코드
	// 중복이 가능하며 본 코드값으로 11번가 상품 조회 등이 가능합니다.
	// 필수값이 아니며 생략 가능합니다.
	private String sellerPrdCd;

	// 부가세/면세상품코드 (필수)
	// 면세상품 선택시, 세무/법률적 책임은 판매자님께 있습니다.
	// 01 : 과세상품
	// 02 : 면세상품
	// 03 : 영세상품
	private String suplDtyfrPrdClfCd;

	// 연말정산 소득공제 여부
	// 도서/음반, 티켓/공연, ebook, 쇼킹티켓 (단, 도서/음반 카테고리 중 음반, DVD, 잡지는 소득공제 제외)
	// 상품에 한하여 연말정산 소득 공제여부를 설정해주세요.
	// Y(환급가능) 외 공백(null값) 또는 다른 문자열 등록 시 N(환급불가)으로 자동 설정됩니다.
	// Y : 환급 가능
	// N : 환급 불가
	private String yearEndTaxYn;

	// 해외구매대행상품 여부 (필수)
	// SellerOffice 가입 시에 글로벌셀러로 가입한 경우에만 사용할 수 있습니다.
	// 일반 셀러인 경우 생략해주세요.
	// 해외거주 글로벌셀러는 일반판매상품(01)로만 등록가능합니다.
	// 문의사항이 있으시면 11번가 담당MD와 상담해 주세요.
	// 01 : 일반판매상품
	// 02 : 해외판매대행상품
	private String forAbrdBuyClf;

	// 관부가세 포함 여부
	// 해외 구매대행상품에 대해서 관부가세 포함 여부를 설정합니다.
	// 01 : 포함
	// 02 : 미포함
	// 03 : 표시하지 않음
	private String importFeeCd;

	// 상품상태 (필수)
	// 주문제작상품으로 등록하시면 구매자의 취소/반품/교환이 불가능하여 클레임이 발생할 수 있으니 신중하게 등록해주시기 바랍니다.
	// Open API로 주문제작상품 등록 시, 판매자가 위 내용에 대해 숙지한 후 동의한 것으로 간주됩니다.
	// 01 : 새상품
	// 02 : 중고상품 (판매방식이 "중고판매"인 경우만 선택가능합니다.)
	// 03 : 재고상품
	// 04 : 리퍼상품(판매방식이 "중고판매"인 경우만 선택가능합니다.)
	// 05 : 전시(진열)상품(판매방식이 "중고판매"인 경우만 선택가능합니다.)
	// 07 : 희귀소장품(판매방식이 "중고판매"인 경우만 선택가능합니다.)
	// 08 : 반품상품(판매방식이 "중고판매"인 경우만 선택가능합니다.)
	// 09 : 스크래치상품(판매방식이 "중고판매"인 경우만 선택가능합니다.)
	// 10 : 주문제작상품
	private String prdStatCd;

	// 사용개월수
	// 판매방식이 중고판매인 경우 반드시 입력해 주셔야 합니다.
	// 사용개월수는 1개월 이상을 입력하셔야 합니다.
	// 사용개월수를 알 수 없을 경우 99999을 입력하세요.
	private String useMon;

	// 구입당시 판매가
	// 판매방식이 중고판매인 경우 반드시 입력해 주셔야 합니다.
	// ,없이 숫자만 입력하세요. 50,000(X) 50000(O)
	private String paidSelPrc;

	// 외관/기능상 특이사항
	// 판매방식이 중고판매인 경우 반드시 입력해 주셔야 합니다.
	// <![CDATA[ ]]> 로 내용을 묶어 주세요.
	private String exteriorSpecialNote;

	// 미성년자 구매가능 (필수)
	// 미성년자 구매불가를 선택하시면, 미성년자 회원에게 상품이미지가 노출되지 않으며 "19금"으로 표시됩니다.
	// 구매불가 상품을 구매가능으로 표시한 경우, 판매금지 처리 될 수 있습니다.
	// Y : 가능
	// N : 불가능
	private String minorSelCnYn;

	// 대표 이미지 URL (필수)
	// 이미지는 11번가 서버가 다운로드하여 600 x 600 사이즈로 리사이징 한뒤 11번가 이미지서버에 저장 됩니다.
	// 이미지 확장자는 jpg, jpeg, png 만 사용가능합니다.
	// 이미지 url 호출시 "Content-Type" 이 정의가 되어있지 않으면 이미지 다운로드가 이루어 지지 않습니다.
	// 클린 이미지는 검색 노출에 도움이 될 수 있습니다.
	private String prdImage01;

	// 추가 이미지 1 URL
	private String prdImage02;

	// 추가 이미지 2 URL
	private String prdImage03;

	// 추가 이미지 3 URL
	private String prdImage04;

	// 목록이미지
	// 검색 결과 페이지나 카테고리 리스트 페이지에서 노출되는 이미지입니다.
	// 클린 이미지는 검색 노출에 도움이 될 수 있습니다.
	private String prdImage05;

	// 카드뷰 이미지2
	// 쇼킹딜/카드뷰 이미지 사이즈: [2:1비율]
	private String prdImage09;

	// 이미지 바이트 코드
	// 바이트 코드로 변환하여 보내셔야 합니다.
	private String prdImage01Src;

	// 상세설명 (필수)
	// iframe 사용은 가능하지만 권장하지 않습니다.
	// html 을 입력하실 경우 <![CDATA[ ]]> 로 묶어 주세요.
	// 외부로의 링크는 제한되며 자세한 사항은 상세설명 물음표를 참조해 주세요.
	// html을 입력하는 경우 일부 스크립트 및 스타일 태그는 제한되니 SellerOffice 상품등록에서 상세설명 html 미리보기를 반드시
	// 테스트해 주세요.
	// html guide를 준수하여 입력하면, 구매 고객이 옵션 찾기가 편리해집니다.
	// Guide를 이용하여 상세설명을 등록해 보세요.
	private String htmlDetail;

	// 인증정보그룹
	@XmlElement(name = "ProductCertGroup")
	private List<ProductCertGroup> productCertGroup;

	// 의료기기 품목허가
	// 의료기기품목허가번호 등록 예제 -
	// http://openapi.11st.co.kr/example/medicalDeviceItemApprovalNum.txt
	@XmlElement(name = "ProductMedical")
	private ProductMedical productMedical;

	// 상품리뷰/후기 전시여부
	private String reviewDispYn;

	// 상품리뷰/후기 옵션 노출여부
	private String reviewOptDispYn;

	// 판매기간코드/예약기간코드
	// 예약기간 등록예제 - http://openapi.11st.co.kr/example/reservationPeriod.txt
	// 0:100 : 판매기간 직접입력. 판매방식 - "고정가판매" 일 경우만 사용가능
	// 3:101 : 3일
	// 5:102 : 5일
	// 7:103 : 7일
	// 15:104 : 15일
	// 30:105 : 30일(1개월)
	// 60:106 : 60일(2개월)
	// 90:107 : 90일(3개월)
	// 120:108 : 120일(4개월)
	// 1y:109 : 1년
	// 3y:110 : 3년
	// 3:401 : 3일
	// 5:402 : 5일
	// 7:403 : 7일
	// 15:404 : 15일
	// 30:405 : 30일(1개월)
	// 60:406 : 60일(2개월)
	// 90:407 : 90일(3개월)
	// 0:400 : 예약기간 직접입력
	private String selPrdClfCd;

	// 판매 시작일/예약 시작일
	private String aplBgnDy;

	// 판매 종료일/예약 종료일
	// 2999/12/31 들어오면 최대3년으로 처리
	private String aplEndDy;

	// 예약판매 상품의 고정가 판매기간 설정
	// Y : 설정함. 판매기간설정이가능합니다.
	// N : 설정안함. (고정가판매의 경우만) 즉시 영구판매가 이루어 집니다.
	private String setFpSelTermYn;

	// 판매기간
	// Y : (항목삭제)설정함. 판매기간 설정이 가능합니다.
	// N : 설정안함(고정가판매의 경우만). 즉시 영구판매가이루어 집니다.
	private String selTermUseYn;

	// 판매기간코드
	// 예약판매 후 고정가판매 인 경우, 고정가 판매기간 설정 - Y인 경우만 사용가능.
	// 0:100 : 설정안함
	// 3:101 : 3일
	// 5:102 : 5일
	// 7:103 : 7일
	// 15:104 : 15일
	// 30:105 : 30일(1개월)
	// 60:106 : 60일(2개월)
	// 90:107 : 90일(3개월)
	// 120:108 : 120일(4개월)
	// 1y:109 : 1년
	// 3y:110 : 3년
	private String selPrdClfFpCd;

	// 입고예정일
	// 입고예정일은 판매종료일과 같은 날, 혹은 그 이후로 설정해 주셔야 하며, 주문처리 시, 최대 15일에 한해서 1회 연장할 수 있습니다.
	// 입력하신 입고예정일은 상품상세 페이지에 안내되며, 입고예정일 지연 시, 신용점수가 차감되오니, 유의해 주십시오.
	private String wrhsPlnDy;

	// 약정코드
	// 휴대폰 카테고리에 상품 등록시 필수로 설정하셔야 합니다.
	// 01 : 일반 약정 단말기
	// 02 : 요금제 약정 단말기
	private String contractCd;

	// 요금제 코드
	// 요금제 약정 단말기인 경우 반드시 입력하셔야 합니다.
	private String chargeCd;

	// 약정기간 코드
	// 01 : 무약정
	// 02 : 12개월
	// 03 : 18개월
	// 04 : 24개월
	// 05 : 30개월
	// 06 : 36개월
	private String periodCd;

	// 단말기 출고 가격
	// ,없이 숫자만 입력하세요. 60,000(X) 60000(O)
	private String phonePrc;

	// 정가
	// 카테고리가 도서인 경우 반드시 입력해 주셔야 합니다.(음반, DVD/블루레이제외)
	// 도서 정가제 관련 규정을 준수하여 등록하셔야 합니다.
	// 도서정가제 규정을 위반하거나 정가를 허위로 등록할 경우 법적 책임은 판매자에게 있으며, 판매중지 등의 불이익을 받을 수 있습니다.
	// 도서 정가제: 18개월 미만의 신간도서의 경우 정가의 10% 이내 할인, 적립율은 판매가의 최대 10% 이내 적용 가능합니다.(마일리지 등
	// 합산 적립)
	private String maktPrc;

	// 판매가 (필수)
	// 판매가는 10원 단위로, 최대 10억 원 미만으로 입력 가능합니다.
	// 판매가 정보 수정 시, 최대 50% 인상/80% 인하까지 수정하실 수 있습니다.
	// 서비스이용료는 카테고리/판매가에 따라 다르게 적용될 수 있습니다.
	private String selPrc;

	// 기본즉시할인 설정여부
	// "기본즉시할인"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// S : 기존값 유지(상품수정) 는 상품수정시에만 입력가능합니다. 쿠폰에 대한 수정이 일어나지 않습니다.
	// Y : 설정함
	// N : 설정안함
	// S : 기존값 유지(상품수정)
	private String cuponcheck;

	// 할인수치
	// "기본즉시할인"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 판매가에서(xxxx)에 들어갈 수치를 입력합니다.
	private String dscAmtPercnt;

	// 할인단위 코드
	// "기본즉시할인"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 01 : 원
	// 02 : %
	private String cupnDscMthdCd;

	// 할인 적용기간 설정여부
	// "기본즉시할인"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// "할인 적용기간 설정"을 하실 경우에만 Element를 입력해 주세요.
	// Y : 설정함
	// N : 설정안함
	private String cupnUseLmtDyYn;

	// 할인적용기간 종료일
	// "기본즉시할인"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// "할인 적용기간 설정"을 하실 경우에만 Element를 입력해 주세요.
	private String cupnIssEndDy;

	// SK pay point 지급 설정여부
	// "SK pay point 지급"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// Y : 설정함
	// N : 설정안함
	private String pay11YN;

	// 적립수치
	// "SK pay point 지급"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 판매가에서(xxxx) 에 들어갈 수치를 입력합니다.
	private String pay11Value;

	// 적립단위 코드
	// "SK pay point 지급"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 01 : %
	// 02 : 원
	private String pay11WyCd;

	// 무이자 할부 제공 설정여부
	// "무이자 할부 제공"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// Y : 설정함
	// N : 설정안함
	private String intFreeYN;

	// 개월수
	// "무이자 할부 제공"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 05 : 2개월
	// 01 : 3개월
	// 06 : 4개월
	// 07 : 5개월
	// 02 : 6개월
	// 08 : 7개월
	// 09 : 8개월
	// 03 : 10개월
	// 10 : 9개월월
	// 11 : 11개월
	// 04 : 12개월
	private String intfreeMonClfCd;

	// 복수구매할인 설정 여부
	// "복수 구매할인"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// Y : 설정함
	// N : 설정안함
	private String pluYN;

	// 복수구매할인 설정 기준
	// "복수 구매할인"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 01 : 수량기준
	// 02 : 금액기준
	private String pluDscCd;

	// 복수구매할인 기준 금액 및 수량
	// "복수 구매할인"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	private String pluDscBasis;

	// 복수구매할인 금액/율
	// "복수 구매할인"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	private String pluDscAmtPercnt;

	// 복수구매할인 구분코드
	// "복수 구매할인"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 01 : %
	// 02 : 원
	private String pluDscMthdCd;

	// 복수구매할인 적용기간 설정
	// "복수 구매할인"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// Y : 설정함
	// N : 설정안함
	private String pluUseLmtDyYn;

	// 복수구매할인 적용기간 시작일
	// "복수 구매할인"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// "할인 적용기간 설정"을 하실 경우에만 Element를 입력해 주세요.
	private String pluIssStartDy;

	// 복수구매할인 적용기간 종료일
	// "복수 구매할인"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// "할인 적용기간 설정"을 하실 경우에만 Element를 입력해 주세요.
	private String pluIssEndDy;

	// 희망후원 지급 설정 여부
	// "희망후원"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// Y : 설정함
	// N : 설정안함
	private String hopeShpYn;

	// 적립수치
	// "희망후원"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	private String hopeShpPnt;

	// 적립단위 코드
	// "희망후원"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 01 : %
	// 02 : 원
	private String hopeShpWyCd;

	// 선택형 옵션 여부
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	private String optSelectYn;

	// 고정값
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 옵션을 등록하실 경우 1 고정값을 주셔야 합니다.
	private String txtColCnt;

	// 멀티옵션 일괄재고수량 설정
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// "상품상세 옵션값 노출 방식 선택"을 생략하실 경우 등록순 옵션이 노출됩니다.
	// "멀티옵션" 방식이 아닌 "싱글옵션" 방식 일 경우는 Element는 생략해주셔야 합니다.
	// 멀티옵션은 옵션별 재고 수량 설정이 api 에서는 불가합니다.
	// 일괄설정만 가능.
	// 멀티옵션 등록예제 - http://openapi.11st.co.kr/example/multiOption1-1.txt
	// http://openapi.11st.co.kr/example/multiOption2-1.txt
	// http://openapi.11st.co.kr/example/multiOption3.txt
	// 싱글옵션 등록예제 - http://openapi.11st.co.kr/example/singleOption1.txt
	private String optionAllQty;

	// 멀티옵션 옵션가 0원 설정
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// "상품상세 옵션값 노출 방식 선택"을 생략하실 경우 등록순 옵션이 노출됩니다.
	// "멀티옵션" 방식이 아닌 "싱글옵션" 방식 일 경우는 Element는 생략해주셔야 합니다.
	// 멀티옵션은 옵션별 옵션가 설정이 api 에서는 불가합니다.
	// 0원만 입력 가능.
	private String optionAllAddPrc;

	// 멀티옵션 일괄옵션추가무게 설정 (필수)
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// "상품상세 옵션값 노출 방식 선택"을 생략하실 경우 등록순 옵션이 노출됩니다.
	// "멀티옵션" 방식이 아닌 "싱글옵션" 방식 일 경우는 Element는 생략해주셔야 합니다.
	// 멀티옵션은 옵션별 옵션무게 설정이 api 에서는 불가합니다.
	// 일괄설정만 가능.
	private String optionAllAddWght;

	// 상품상세 옵션값 노출 방식 선택
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 00 : 등록순
	// 01 : 옵션값 가나다순
	// 02 : 옵션값 가나다 역순
	// 03 : 옵션가격 낮은 순
	// 04 : 옵션가격 높은 순
	private String prdExposeClfCd;

	// 전체옵션 조합여부
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// Y : 정의한 전체 옵션값이 조합되어 멀티옵션으로 등록
	// N : 옵션 매핑Key에 존재하는(선택 된) 값으로만 멀티 옵션 등록
	private String optMixYn;

	// 옵션 수정여부
	// 상품등록시가 아닌 상품 수정시에만 입력해 주세요.
	// 상품 수정시 옵션(구매자 작성형 동일) 정보 수정이 없을 시에는 "N"으로 값을 넘겨 주세요.
	// 수정 처리가 좀 더 빠르게 이루어 집니다.
	// 옵션 수정이 있을 경우는 "Y"으로 넘겨 주세요.
	// 기존 상품 수정과 동일하게 전체 상품 수정으로 이루어 집니다.
	// 해당 element를 생락하실 경우는 Defalt 값으로 "N"으로 인식하여 옵션 정보는 수정이 이루어 지지 않습니다.
	// Y : 옵션 정보 수정
	// N : 옵션 정보 수정하지 않음
	private String optUpdateYn;

	// 옵션명
	// 40Byte 까지만 입력가능하며 특수 문자[&#39;,",%,&,<,>,#,†]는 입력할 수 없습니다.
	// 11번가 단일상품의 경우 표준옵션만 허용 가능합니 다.
	// 허용 카테고리/옵션 참조 : http://image.11st.co.kr/product/sell/optionInfoByCategory.xls
	private String colTitle;

	// ProductOption
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	@XmlElement(name = "ProductOption")
	private List<ProductOption> productOption;

	// ProductRootOption
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	@XmlElement(name = "ProductRootOption")
	private ProductRootOption productRootOption;

	// ProductOptionExt
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	@XmlElement(name = "ProductOptionExt")
	private ProductOptionExt productOptionExt;

	// 옵션등록
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 구매자작성형 옵션의 등록 최대 5개까지 등록 가능
	// 구매자작성형 옵션예제 - http://openapi.11st.co.kr/example/buyerCreatedOptions1.txt
	@XmlElement(name = "ProductCustOption")
	private List<ProductCustOption> productCustOption;

	// 계산형옵션 설정여부
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 등록 : 계산형 옵션 정보 입력 시 사용, 미입력 사용안함
	// 수정 : Y : 사용, N : 삭제
	// 계산형 옵션은 가구/수납가구/학생가구, 침구/커튼/카페트, 홈/인테리어/DIY 카테고리에서만 사용 가능합니다.
	// 계산형 옵션은 조합형 옵션을 최소 1개 이상 함께 등록해야 사용 가능합니다.
	// 계산형 옵션은 작성형 옵션과 동시에 사용할 수 없습니다.
	// 독립형 옵션을 사용하면 계산형 옵션을 사용할 수 없습니다.
	// 판매최소값, 판매최대값, 단가기준값, 판매단위-숫자는 숫자로 입력하세요.
	// 초기 개발시에는 옵션등록과 SellerOffice 상품등록과 반드시 병행하시면서 개발해주셔야 합니다.
	// 계산형 옵션예제 - http://openapi.11st.co.kr/example/calculatedOption.txt
	// * 11번가 단일상품의 경우 해당 기능 사용이 불가능합니다.
	private String useOptCalc;

	// 계산형 옵션 타입설정
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// reg : 등록
	// upd : 수정
	private String optCalcTranType;

	// 계산형옵션구분값
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	private String optTypCd;

	// 첫번째 계산형 옵션명
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 최대 20byte, 초과 내용은 삭제
	private String optItem1Nm;

	// 첫번째 계산형 옵션 판매최소값
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 입력 숫자 범위 1~1,000,000
	private String optItem1MinValue;

	// 첫번째 계산형 옵션 판매최대값
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 입력 숫자 범위 1~1,000,000
	private String optItem1MaxValue;

	// 두번째 계산형 옵션명
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 최대 20byte, 초과 내용은 삭제
	private String optItem2Nm;

	// 두번째 계산형 옵션 판매최소값
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 입력 숫자 범위 1~1,000,000
	private String optItem2MinValue;

	// 두번째 계산형 옵션 판매최대값
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 입력 숫자 범위 1~1,000,000
	private String optItem2MaxValue;

	// 단가기준값
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 입력 숫자 범위 0.001~1,000,000
	private String optUnitPrc;

	// 기준 단위코드
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 01 : mm
	// 02 : cm
	// 03 : m
	private String optUnitCd;

	// 판매단위-숫자
	// "옵션등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 입력 숫자 범위 1~1,000,000
	private String optSelUnit;

	// 추가구성상품
	// "추가구성상품등록"을 설정 하지 않을시에는 Element를 모두 삭제해 주세요.
	// 추가구성상품 등록예제 -
	// http://i.011st.com/product/manual/safetyCertificationInformation1_0701.txt
	// * 11번가 단일상품의 경우 해당 기능 사용이 불가능합니다.
	@XmlElement(name = "ProductComponent")
	private List<ProductComponent> productComponent;

	// 재고수량
	// 재고 수량은 반드시 입력하셔야 하며 옵션이 있을 경우 입력값과 상관없이 옵션수량의 총합으로 자동계산 되어 반영됩니다.
	// 재고는 0으로 입력할 수 없습니다.
	// 상품 판매 중단을 원하시면 판매중지 처리하시기 바랍니다.
	private String prdSelQty;

	// 최소구매수량 설정코드
	// "최소구매수량" 서비스를 이용하지 않으신다면 Element를 생략해 주세요.
	// 자동 "제한 안한(00)"으로 설정됩니다.
	// 구매 제한 해당 제한 기간은 한달(30일)입니다.
	// 00 : 제한 안함
	// 01 : 1회 제한
	private String selMinLimitTypCd;

	// 최소구매수량 개수
	// "최소구매수량" 서비스를 이용하지 않으신다면 Element를 생략해 주세요.
	// 자동 "제한 안한(00)"으로 설정됩니다.
	private String selMinLimitQty;

	// 최대구매수량 설정코드
	// "최대구매수량" 서비스를 이용하지 않으신다면 "설정코드"와 상관없이 Element를 생략해 주세요.
	// 자동 "제한 안한(00)"으로 설정됩니다.
	// 00 : 제한 안함
	// 01 : 1회 제한
	// 02 : 기간 제한
	private String selLimitTypCd;

	// 최대구매수량 개수
	// "최대구매수량" 서비스를 이용하지 않으신다면 "설정코드"와 상관없이 Element를 생략해 주세요.
	// 자동 "제한 안한(00)"으로 설정됩니다.
	private String selLimitQty;

	// 최대구매수량 재구매기간
	// "최대구매수량" 서비스를 이용하지 않으신다면 "설정코드"와 상관없이 Element를 생략해 주세요.
	// 자동 "제한 안한(00)"으로 설정됩니다.
	private String townSelLmtDy;

	// 사은품 정보 사용여부
	// Y : 사용함
	// N : 사용안함
	private String useGiftYn;

	// ProductGift
	// * 11번가 단일상품의 경우 해당 기능 사용이 불가능합니다.
	@XmlElement(name = "ProductGift")
	private List<ProductGift> productGift;

	// 선물포장 유형코드
	// 선물포장 유형코드
	// 01 : 불가
	// 02 : 선물포장
	// 03 : 포장재동봉
	// 04 : 선물포장 + 포장재동봉
	private String gftPackTypCd;

	// 전세계배송 이용여부
	// 선택하지 않을 경우, 기본 이용안함(N)으로 세팅되며, 아래와 같은 조건이 모두 충족되어야 가능합니다.
	// 1. 셀러회원정보에 전세계배송 이용여부가 "노출 또는 이용"으로 되어있고
	// 2. 등록하려는 상품카테고리의 전세게배송 이용여부가 "이용(Y)"으로 되어있고 카테고리별 전세계배송 가능여부확인
	// 3. 상품옵션이 "독립형"이 아니어야 하고
	// 4. 상품의 배송방법이 "택배" 또는 "우편(소포/등기)"로 되어있고
	// 5. 상품의 배송비설정이 "무료" 또는 결제방법이 "선결제가능" 혹은 "선결제 필수"이어야 하고
	// 6. 통관용으로 사용될 생산지 국가를 반드시 선택해야 하고
	// 7. 상품무게는 반드시 입력해야 하고
	// 8. 상품의 출고지가 "국내주소" 인경우만 "전세계배송"이 가능.
	// Y : 이용
	// N : 이용안함
	private String gblDlvYn;

	// 배송가능지역 코드 (필수)
	// 01 : 전국
	// 02 : *전국(제주 도서산간지역 제외)
	// 03 : 서울
	// 04 : 인천
	// 05 : 광주
	// 06 : 대구
	// 07 : 대전
	// 08 : 부산
	// 09 : 울산
	// 10 : 경기
	// 11 : 강원
	// 12 : 충남
	// 13 : 충북
	// 14 : 경남
	// 15 : 경북
	// 16 : 전남
	// 17 : 전북
	// 18 : 제주
	// 19 : 서울/경기
	// 20 : 서울/경기/대전
	// 21 : 충북/충남
	// 22 : 경북/경남
	// 23 : 전북/전남
	// 24 : 부산/울산
	// 25 : 서울/경기/제주도서산간 제외지역
	// 26 : 일부지역불가
	private String dlvCnAreaCd;

	// 배송방법 (필수)
	// "전세계배송 상품" 인경우 "택배 또는 우편(소포/등기)"만 입력가능합니다.
	// 01 : 택배
	// 02 : 우편(소포/등기)
	// 03 : 직접전달(화물배달)
	// 04 : 퀵서비스
	// 05 : 배송필요없음
	private String dlvWyCd;

	// 발송택배사 (필수)
	// 00034 : CJ대한통운
	// 00011 : 한진택배
	// 00012 : 롯데(현대)택배
	// 00001 : KGB택배
	// 00007 : 우체국택배
	// 00002 : 로젠택배
	// 00008 : 우편등기
	// 00021 : 대신택배
	// 00022 : 일양로지스
	// 00023 : ACI
	// 00025 : WIZWA
	// 00026 : 경동택배
	// 00027 : 천일택배
	// 00035 : 합동택배
	// 00037 : 건영택배
	// 00099 : 기타
	// 00060 : CVSnet편의점택배
	// 00061 : CU편의점택배
	// 00062 : 호남택배
	// 00063 : SLX택배
	// 00064 : 한의사랑택배
	// 00065 : 용마로지스
	// 00066 : 세방택배
	// 00067 : 농협택배
	// 00068 : HI택배
	// 00069 : 원더스퀵
	// 00038 : LG전자 본사설치
	// 00036 : 삼성전자 본사설치
	// 00085 : 위니아딤채 본사설치
	// 00059 : 위니아대우 본사설치
	// 00039 : DHL
	// 00086 : 고려택배
	// 00087 : 애니트랙
	// 00088 : 우리한방택배
	// 00089 : 대림통운
	// 00090 : IK 물류
	// 00091 : 성훈 물류
	// 00082 : 홈이노베이션로지스
	// 00101 : 로지스밸리택배
	// 00112 : 롯데칠성
	private String dlvEtprsCd;

	// 발송마감 템플릿번호 (필수)
	// 발송마감 템플릿번호 (오늘발송, 일반발송,재고확인 후 순차발송(소량재고/제작상품) ) 1개 등록 가능하며 선택입력 정보입니다.
	// 기본적으로 배송방법이 택배인 상품에 한하여 유효하며 해외직구 상품, 예약판매상품, 주문제작상품, 셀러위탁배송 상품은 반영 대상에서
	// 제외됩니다.
	private String dlvSendCloseTmpltNo;

	// 배송비 종류 (필수)
	// 01 : 무료
	// 02 : 고정 배송비
	// 03 : 상품 조건부 무료
	// 04 : 수량별 차등, 수량별 차등은 조건추가에 따라 배송비를 최대 10개까지 설정 가능, 수량별차등 배송비는 dlvCnt1,
	// dlvCnt2, dlvCst3 정보가 세트로 움직이고 1개 이상의 경우 [^]로 구분합니다.
	// <dlvCstInstBasiCd>04</dlvCstInstBasiCd><dlvCst3>5000^1000^0</dlvCst3><dlvCnt1>1^10^100</dlvCnt1><dlvCnt2>9^99</dlvCnt2>
	// 05 : 1개당 배송비
	// 07 : 판매자 조건부 배송비, 셀러오피스 > 회원정보 > 판매정보설정 메뉴에서 판매자 조건부 배송비 설정 후 아래의 코드로 등록하시면
	// 됩니다. <dlvCstInstBasiCd>07</dlvCstInstBasiCd>
	// 08 : 출고지 조건부 배송비 2010.10.08 추가
	// 09 : 11번가 통합 출고지 배송비
	// 10 : 11번가해외배송조건부배송비 (11번가 해외 배송을 사용하는 경우)
	private String dlvCstInstBasiCd;

	// 배송비
	// 상품 조건부 무료(03), 고정 배송비(02)
	private String dlvCst1;

	// 배송비
	// 수량별 차등(04)
	private String dlvCst3;

	// 배송비
	// 1개당 배송비(05)
	private String dlvCst4;

	// 배송비
	// 고정 배송비(02) 배송비 추가 안내 적용조건 : 전세계배송(N), 선결제불가(02)
	// 01 : (상품상세참고)
	// 02 : (상품별 차등 적용)
	// 03 : (지역별 차등 적용)
	// 04 : (상품/지역별 차등)
	// 06 : (서울/경기 무료, 이외 추가비용)
	private String dlvCstInfoCd;

	// 상품조건부 무료 상품기준금액 (필수)
	// 상품조건부 무료(03)
	private String PrdFrDlvBasiAmt;

	// 수량별 차등 기준 ~이상 수량 (필수)
	// 수량별 차등(04) "수량별 차등"은 조건추가에 따라 기준 수량를 최대 10개까지 설정 가능
	private String dlvCnt1;

	// 수량별 차등 기준 ~이하 수량 (필수)
	// 수량별 차등(04) "수량별 차등"은 조건추가에 따라 기준 수량를 최대 9개까지 설정 가능
	private String dlvCnt2;

	// 묶음배송 여부 (필수)
	// Y : 가능
	// N : 불가
	private String bndlDlvCnYn;

	// 결제방법 (필수)
	// 01 : 선결제/착불 가능
	// 02 : 착불
	// 03 : 선결제
	private String dlvCstPayTypCd;

	// 제주 (필수)
	// 제주 추가 배송비
	private String jejuDlvCst;

	// 도서산간 (필수)
	// 도서산간 추가 배송비
	private String islandDlvCst;

	// 출고지 주소 코드 (필수)
	// 우선 SellerOffice 상품등록에서 출고지 주소가 등록이 되어있어야 합니다.
	// 등록된 출고지 주소를 Api 조회 서비스를 통해 주소 시퀀스코드를 조회합니다.
	// 출고지 주소조회에서 조회한 시퀀스코드를 입력하시면 됩니다.
	// 만일 출고지 주소를 생략하실 경우 기본주소로 자동 설정이 됩니다.
	// 하여 상품수정을 하실경우 수정 당시의 기본주소로 재설정이 됩니다.
	// 상품수정시 기본주소 변동으로 인한 이슈사항을 줄이기 위해 출고지 코드 입력을 권장합니다.
	// 전세계배송이 되려면 출고지가 국내여야만 한다.
	private String addrSeqOut;

	// 출고지 주소 해외 여부
	// "출고지 주소 해외 여부"는 출고지 주소가 해외일 경우에만 입력하시고 국내일 경우는 생략해 주세요.
	// Y : 출고지가 해외
	// N : 출고지가 국내
	private String outsideYnOut;

	// 방문수령추가
	// 방문수령을 사용할 경우 Y
	private String visitDlvYn;

	// 방문수령 주소 코드
	// 방문수령 주소 시퀀스 코드를 입력 해주세요
	private String visitAddrSeq;

	// 통합 ID 회원 번호
	// 출고지용 "통합 ID 회원 번호 (출고지용)"는 통합 출고지 사용하는 경우에만 입력해 주세요.
	private String addrSeqOutMemNo;

	// 반품/교환지 주소 코드 (필수)
	// 우선 SellerOffice 상품등록에서 반품/교환지 주소가 등록이 되어있어야 합니다.
	// 등록된 반품/교환지 주소를 Api 조회 서비스를 통해 주소 시퀀스코드를 조회합니다.
	// 반품/교환지 주소조회에서 조회한 시퀀스코드를 입력하시면 됩니다.
	// 만일 반품/교환지 주소를 생략하실 경우 기본주소로 자동 설정이 됩니다.
	// 하여 상품수정을 하실 경우 수정 당시의 기본주소로 재설정이 됩니다.
	// 상품수정시 기본주소 변동으로 인한 이슈사항을 줄이기 위해 출고지 코드 입력을 권장합니다.
	private String addrSeqIn;

	// 해외 반품/교환지 주소 여부
	// "반품/교환지 주소 해외 여부"는 반품/교환지 주소가 해외일 경우에만 입력하시고 국내일 경우는 생략해 주세요.
	// Y : 반품/교환지가 해외
	// N : 반품/교환지가 국내
	private String outsideYnIn;

	// 통합 ID 회원 번호
	// 반품지용 "통합 ID 회원 번호 (반품지용)"는 통합 반품지 사용하는 경우에만 입력해 주세요.
	private String addrSeqInMemNo;

	// 해외 취소 배송비
	// 배송비는 10원단위로 입력하셔야 합니다.
	// 3000(O), 2999(X), 2,900(X) 배송 주체(dlvClf) 코드가 03(11번가 해외 배송)인 경우 필수입니다.
	private String abrdCnDlvCst;

	// 반품배송비 (필수)
	// 배송비는 10원단위로 입력하셔야 합니다.
	// 3000(O), 2999(X), 2,900(X)
	private String rtngdDlvCst;

	// 교환 배송비(왕복) (필수)
	// 배송비는 10원단위로 입력하셔야 합니다.
	// 3000(O), 2999(X), 2,900(X)
	private String exchDlvCst;

	// 초기배송비 무료시 부과방법
	// 초기배송비 무료 시 부과방법 구분코드를 입력하지 않을 경우 편도반품배송비가 교환배송비보다 크거나 같은 경우에는 ’02’ 편도,
	// 편도반품배송비가 교환배송비보다 작은 경우에는 ’01’왕복 코드가 자동 등록됩니다.
	// 01 : 왕복(편도x2)
	// 02 : 편도
	private String rtngdDlvCd;

	// AS안내정보 (필수)
	// 반드시 입력하셔야 하며 입력할 내용이 없으시면 . 이라도 입력해주셔야 합니다.
	// 공백은 안됩니다. 특수문자 등이 포함되어 있을 경우 <![CDATA[ ]]> 로 묶어 주세요.
	// 최대 4,000 bytes 까지 입력 가능합니다.
	private String asDetail;

	// 반품/교환 안내 (필수)
	// 상품상세 페이지에 안내되는 내용으로, 반품/교환 문의를 줄이실 수 있습니다.
	// 반드시 입력하셔야 하며 입력할 내용이 없으시면 . 이라도 입력해주셔야 합니다.
	// 공백은 안됩니다. 특수문자 등이 포함되어 있을 경우 <![CDATA[ ]]> 로 묶어 주세요.
	// 최대 4,000 bytes 까지 입력 가능합니다.
	private String rtngExchDetail;

	// 배송주체(배송유형) (필수)
	// 출고지에 따라 결정이 됩니다.
	// 상품의 출고지가 판매자 출고지 인 경우: 업체 배송,
	// 11번가 통합 ID의 출고지인 경우: 11번가 배송,
	// 11번가 해외 통합 출고지인 경우: 11번가 해외배송
	// 01 : 11번가 배송 (통합 ID의 출고지를 사용하는 경우)
	// 02 : 업체배송 (셀러가 배송을 처리하는 경우)
	// 03 : 11번가 해외 배송 (11번가 해외 통합 출고지를 사용하는 경우) 지정하지 않는 경우 default로 02(업체배송)으로
	// 처리됩니다.
	private String dlvClf;

	// 해외 입고 유형 (필수)
	// 배송 주체(dlvClf) 코드가 03(11번가 해외 배송)인 경우 필수입니다.
	// 상품의 출고지가 11번가 무료 픽업 가능 지역인 경우: 11번가 무료 픽업,
	// 판매자 직접 발송인 경우: 판매자발송,
	// 구매 대행인 경우: 구매 대행
	// 01 : 무료픽업
	// 02 : 판매자발송
	// 03 : 구매대행
	private String abrdInCd;

	// 상품무게 (필수)
	// g 단위로 입력 배송 주체(dlvClf) 코드가 03(11번가 해외 배송)인 경우 또는 전세계배송 상품" 인경우 필수입니다.
	private String prdWght;

	// 생산지국가(통관용) (필수)
	// 대표상품의 생산지국가를 선택하시면 됩니다.
	// 전세계 배송상품인 경우 필수입니다.
	// http://soffice.11st.co.kr/product/ProductAjax.tmall?method=getNationExcel
	private String ntShortNm;

	// 판매자 해외 출고지 주소 (필수)
	// 배송 주체(dlvClf) 코드가 03(11번가 해외 배송)인 경우 필수입니다.
	// 우선 SellerOffice 상품등록에서 출고지 주소(해외)가 등록이 되어있어야 합니다.
	// 등록된 출고지 주소를 Api 조회 서비스를 통해 주소 시퀀스코드를 조회합니다.
	// 출고지 주소조회에서 조회한 시퀀스코드를 입력하시면 됩니다.
	// 상품수정시 기본주소 변동으로 인한 이슈사항을 줄이기 위해 출고지 코드 입력을 권장합니다.
	private String globalOutAddrSeq;

	// 판매자 해외 출고지 지역 정보 (필수)
	// 배송 주체(dlvClf) 코드가 03(11번가 해외 배송)인 경우 필수입니다.
	// 해외 코드로 입력하시기 바랍니다.
	// 01 : 국내
	// 02 : 해외
	private String mbAddrLocation05;

	// 판매자 반품/교환지 주소 (필수)
	// 배송 주체(dlvClf) 코드가 03(11번가 해외 배송)인 경우 필수입니다.
	// 우선 SellerOffice 상품등록에서 반품/교환지 주소(해외)가 등록이 되어있어야 합니다.
	// 등록된 반품/교환지 주소를 Api 조회 서비스를 통해 주소 시퀀스코드를 조회합니다.
	// 반품/교환지 주소조회에서 조회한 시퀀스코드를 입력하시면 됩니다.
	// 상품수정시 기본주소 변동으로 인한 이슈사항을 줄이기 위해 반품/교환지 코드 입력을 권장합니다.
	private String globalInAddrSeq;

	// 판매자 반품/교환지 지역 정보 (필수)
	// 배송 주체(dlvClf) 코드가 03(11번가 해외 배송)인 경우 필수입니다.
	// 01 : 국내
	// 02 : 해외
	private String mbAddrLocation06;

	// 제조일자
	private String mnfcDy;

	// 유효일자
	private String eftvDy;

	// 상품정보제공고시 (필수)
	// http://soffice.11st.co.kr/product/BulkProductReg.tmall?method=goProductNotiPop
	@XmlElement(name = "ProductNotification")
	private ProductNotification productNotification;

	// 제조사
	// 제조사는 텍스트 형태로만 입력하며 제조사가 없을 시 "없음"으로 입력합니다.
	private String company;

	// 모델명
	// 모델명은 텍스트 형태로만 입력하며 모델명이 없을 시 "없음"으로 입력합니다.
	// (예시 모델명 : 기본 라인 셔츠 SQBAB9401)
	private String modelNm;

	// 모델코드
	// 등록하실 상품 모델의 고유한 식별정보로, 영문+숫자, 숫자, 영문 등으로 조합된 모델번호를 입력해주십시오.
	// (예시 모델코드 : SQBAB9401)
	private String modelCd;

	// 원제
	// 카테고리가 도서인 경우 선택하여 입력하실 수 있습니다.
	// 원제명은 한글50자, 영문/숫자 100자 이내로 입력해 주십시오.
	private String mainTitle;

	// 아티스트/감독(배우)
	// 카테고리가 음반/DVD인 경우 반드시 입력해 주셔야 합니다.(음반,음반(TAPE),DVD/비디오)
	// 아티스트/감독(배우)명은 한글 50자,영문/숫자 100자 이내로 입력해 주십시오.
	private String artist;

	// 음반 라벨
	// 카테고리가 도서/음반/DVD>음반, 음반[TAPE]인 경우 선택하여 입력하실 수 있습니다.
	// 레이블은 한글 100자,영문/숫자 200자 이내로 입력해 주십시오.
	private String mudvdLabel;

	// 제조사
	// 카테고리가 도서/음반/DVD>음반, 음반[TAPE],DVD 인 경우 반드시 입력 하셔야 합니다.(음반,음반(TAPE),DVD/비디오)
	// 레이블은 한글 100자,영문/숫자 200자 이내로 입력해 주십시오.
	private String maker;

	// 앨범명
	// 카테고리가 도서/음반/DVD>음반, 음반[TAPE] 인 경우 반드시 입력 하셔야 합니다.(음반,음반(TAPE))
	// 앨범명은 한글 100자,영문/숫자 200자 이내로 입력해 주십시오.
	private String albumNm;

	// DVD 타이틀
	// 카테고리가 도서/음반/DVD>DVD 인 경우 반드시 입력 하셔야 합니다.(DVD/비디오)
	// DVD 타이틀은 한글 100자,영문/숫자 200자 이내로 입력해 주십시오.
	private String dvdTitle;

	// 장바구니 담기 제한
	// 장바구니 담기 제한은 Y/N으로 입력 됩니다.
	private String bcktExYn;

	// 가격비교 사이트 등록 여부
	// 가격비교사이트 등록은 선택사항이며 "등록함"을 권장합니다.
	// Y : 등록함
	// N : 등록안함
	private String prcCmpExpYn;

	// 단일상품여부
	// 11번가 단일상품인경우, 값 Y로 등록 필수 미입력시 N이 자동 입력됩니다.
	private String stdPrdYn;

	// 태그 목록
	@XmlElement(name = "ProductTag")
	private List<ProductTag> productTag;

	// 상품번호
	private String prdNo;

	// 처리결과 메시지
	private String message;

	public String getAbrdBuyPlace() {
		return abrdBuyPlace;
	}

	public void setAbrdBuyPlace(String abrdBuyPlace) {
		this.abrdBuyPlace = abrdBuyPlace;
	}

	public String getAbrdSizetableDispYn() {
		return abrdSizetableDispYn;
	}

	public void setAbrdSizetableDispYn(String abrdSizetableDispYn) {
		this.abrdSizetableDispYn = abrdSizetableDispYn;
	}

	public String getSelMnbdNckNm() {
		return selMnbdNckNm;
	}

	public void setSelMnbdNckNm(String selMnbdNckNm) {
		this.selMnbdNckNm = selMnbdNckNm;
	}

	public String getSelMthdCd() {
		return selMthdCd;
	}

	public void setSelMthdCd(String selMthdCd) {
		this.selMthdCd = selMthdCd;
	}

	public String getDispCtgrNo() {
		return dispCtgrNo;
	}

	public void setDispCtgrNo(String dispCtgrNo) {
		this.dispCtgrNo = dispCtgrNo;
	}

	public String getPrdTypCd() {
		return prdTypCd;
	}

	public void setPrdTypCd(String prdTypCd) {
		this.prdTypCd = prdTypCd;
	}

	public String getGblHsCode() {
		return gblHsCode;
	}

	public void setGblHsCode(String gblHsCode) {
		this.gblHsCode = gblHsCode;
	}

	public String getPrdNm() {
		return prdNm;
	}

	public void setPrdNm(String prdNm) {
		this.prdNm = prdNm;
	}

	public String getPrdNmEng() {
		return prdNmEng;
	}

	public void setPrdNmEng(String prdNmEng) {
		this.prdNmEng = prdNmEng;
	}

	public String getAdvrtStmt() {
		return advrtStmt;
	}

	public void setAdvrtStmt(String advrtStmt) {
		this.advrtStmt = advrtStmt;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getApiPrdAttrBrandCd() {
		return apiPrdAttrBrandCd;
	}

	public void setApiPrdAttrBrandCd(String apiPrdAttrBrandCd) {
		this.apiPrdAttrBrandCd = apiPrdAttrBrandCd;
	}

	public String getRmaterialTypCd() {
		return rmaterialTypCd;
	}

	public void setRmaterialTypCd(String rmaterialTypCd) {
		this.rmaterialTypCd = rmaterialTypCd;
	}

	public String getOrgnTypCd() {
		return orgnTypCd;
	}

	public void setOrgnTypCd(String orgnTypCd) {
		this.orgnTypCd = orgnTypCd;
	}

	public String getOrgnDifferentYn() {
		return orgnDifferentYn;
	}

	public void setOrgnDifferentYn(String orgnDifferentYn) {
		this.orgnDifferentYn = orgnDifferentYn;
	}

	public String getOrgnTypDtlsCd() {
		return orgnTypDtlsCd;
	}

	public void setOrgnTypDtlsCd(String orgnTypDtlsCd) {
		this.orgnTypDtlsCd = orgnTypDtlsCd;
	}

	public String getOrgnNmVal() {
		return orgnNmVal;
	}

	public void setOrgnNmVal(String orgnNmVal) {
		this.orgnNmVal = orgnNmVal;
	}

	public List<ProductRmaterial> getProductRmaterial() {
		return productRmaterial;
	}

	public void setProductRmaterial(List<ProductRmaterial> productRmaterial) {
		this.productRmaterial = productRmaterial;
	}

	public String getBeefTraceStat() {
		return beefTraceStat;
	}

	public void setBeefTraceStat(String beefTraceStat) {
		this.beefTraceStat = beefTraceStat;
	}

	public String getBeefTraceNo() {
		return beefTraceNo;
	}

	public void setBeefTraceNo(String beefTraceNo) {
		this.beefTraceNo = beefTraceNo;
	}

	public String getSellerPrdCd() {
		return sellerPrdCd;
	}

	public void setSellerPrdCd(String sellerPrdCd) {
		this.sellerPrdCd = sellerPrdCd;
	}

	public String getSuplDtyfrPrdClfCd() {
		return suplDtyfrPrdClfCd;
	}

	public void setSuplDtyfrPrdClfCd(String suplDtyfrPrdClfCd) {
		this.suplDtyfrPrdClfCd = suplDtyfrPrdClfCd;
	}

	public String getYearEndTaxYn() {
		return yearEndTaxYn;
	}

	public void setYearEndTaxYn(String yearEndTaxYn) {
		this.yearEndTaxYn = yearEndTaxYn;
	}

	public String getForAbrdBuyClf() {
		return forAbrdBuyClf;
	}

	public void setForAbrdBuyClf(String forAbrdBuyClf) {
		this.forAbrdBuyClf = forAbrdBuyClf;
	}

	public String getImportFeeCd() {
		return importFeeCd;
	}

	public void setImportFeeCd(String importFeeCd) {
		this.importFeeCd = importFeeCd;
	}

	public String getPrdStatCd() {
		return prdStatCd;
	}

	public void setPrdStatCd(String prdStatCd) {
		this.prdStatCd = prdStatCd;
	}

	public String getUseMon() {
		return useMon;
	}

	public void setUseMon(String useMon) {
		this.useMon = useMon;
	}

	public String getPaidSelPrc() {
		return paidSelPrc;
	}

	public void setPaidSelPrc(String paidSelPrc) {
		this.paidSelPrc = paidSelPrc;
	}

	public String getExteriorSpecialNote() {
		return exteriorSpecialNote;
	}

	public void setExteriorSpecialNote(String exteriorSpecialNote) {
		this.exteriorSpecialNote = exteriorSpecialNote;
	}

	public String getMinorSelCnYn() {
		return minorSelCnYn;
	}

	public void setMinorSelCnYn(String minorSelCnYn) {
		this.minorSelCnYn = minorSelCnYn;
	}

	public String getPrdImage01() {
		return prdImage01;
	}

	public void setPrdImage01(String prdImage01) {
		this.prdImage01 = prdImage01;
	}

	public String getPrdImage02() {
		return prdImage02;
	}

	public void setPrdImage02(String prdImage02) {
		this.prdImage02 = prdImage02;
	}

	public String getPrdImage03() {
		return prdImage03;
	}

	public void setPrdImage03(String prdImage03) {
		this.prdImage03 = prdImage03;
	}

	public String getPrdImage04() {
		return prdImage04;
	}

	public void setPrdImage04(String prdImage04) {
		this.prdImage04 = prdImage04;
	}

	public String getPrdImage05() {
		return prdImage05;
	}

	public void setPrdImage05(String prdImage05) {
		this.prdImage05 = prdImage05;
	}

	public String getPrdImage09() {
		return prdImage09;
	}

	public void setPrdImage09(String prdImage09) {
		this.prdImage09 = prdImage09;
	}

	public String getPrdImage01Src() {
		return prdImage01Src;
	}

	public void setPrdImage01Src(String prdImage01Src) {
		this.prdImage01Src = prdImage01Src;
	}

	public String getHtmlDetail() {
		return htmlDetail;
	}

	public void setHtmlDetail(String htmlDetail) {
		this.htmlDetail = htmlDetail;
	}

	public List<ProductCertGroup> getProductCertGroup() {
		return productCertGroup;
	}

	public void setProductCertGroup(List<ProductCertGroup> productCertGroup) {
		this.productCertGroup = productCertGroup;
	}

	public ProductMedical getProductMedical() {
		return productMedical;
	}

	public void setProductMedical(ProductMedical productMedical) {
		this.productMedical = productMedical;
	}

	public String getReviewDispYn() {
		return reviewDispYn;
	}

	public void setReviewDispYn(String reviewDispYn) {
		this.reviewDispYn = reviewDispYn;
	}

	public String getReviewOptDispYn() {
		return reviewOptDispYn;
	}

	public void setReviewOptDispYn(String reviewOptDispYn) {
		this.reviewOptDispYn = reviewOptDispYn;
	}

	public String getSelPrdClfCd() {
		return selPrdClfCd;
	}

	public void setSelPrdClfCd(String selPrdClfCd) {
		this.selPrdClfCd = selPrdClfCd;
	}

	public String getAplBgnDy() {
		return aplBgnDy;
	}

	public void setAplBgnDy(String aplBgnDy) {
		this.aplBgnDy = aplBgnDy;
	}

	public String getAplEndDy() {
		return aplEndDy;
	}

	public void setAplEndDy(String aplEndDy) {
		this.aplEndDy = aplEndDy;
	}

	public String getSetFpSelTermYn() {
		return setFpSelTermYn;
	}

	public void setSetFpSelTermYn(String setFpSelTermYn) {
		this.setFpSelTermYn = setFpSelTermYn;
	}

	public String getSelTermUseYn() {
		return selTermUseYn;
	}

	public void setSelTermUseYn(String selTermUseYn) {
		this.selTermUseYn = selTermUseYn;
	}

	public String getSelPrdClfFpCd() {
		return selPrdClfFpCd;
	}

	public void setSelPrdClfFpCd(String selPrdClfFpCd) {
		this.selPrdClfFpCd = selPrdClfFpCd;
	}

	public String getWrhsPlnDy() {
		return wrhsPlnDy;
	}

	public void setWrhsPlnDy(String wrhsPlnDy) {
		this.wrhsPlnDy = wrhsPlnDy;
	}

	public String getContractCd() {
		return contractCd;
	}

	public void setContractCd(String contractCd) {
		this.contractCd = contractCd;
	}

	public String getChargeCd() {
		return chargeCd;
	}

	public void setChargeCd(String chargeCd) {
		this.chargeCd = chargeCd;
	}

	public String getPeriodCd() {
		return periodCd;
	}

	public void setPeriodCd(String periodCd) {
		this.periodCd = periodCd;
	}

	public String getPhonePrc() {
		return phonePrc;
	}

	public void setPhonePrc(String phonePrc) {
		this.phonePrc = phonePrc;
	}

	public String getMaktPrc() {
		return maktPrc;
	}

	public void setMaktPrc(String maktPrc) {
		this.maktPrc = maktPrc;
	}

	public String getSelPrc() {
		return selPrc;
	}

	public void setSelPrc(String selPrc) {
		this.selPrc = selPrc;
	}

	public String getCuponcheck() {
		return cuponcheck;
	}

	public void setCuponcheck(String cuponcheck) {
		this.cuponcheck = cuponcheck;
	}

	public String getDscAmtPercnt() {
		return dscAmtPercnt;
	}

	public void setDscAmtPercnt(String dscAmtPercnt) {
		this.dscAmtPercnt = dscAmtPercnt;
	}

	public String getCupnDscMthdCd() {
		return cupnDscMthdCd;
	}

	public void setCupnDscMthdCd(String cupnDscMthdCd) {
		this.cupnDscMthdCd = cupnDscMthdCd;
	}

	public String getCupnUseLmtDyYn() {
		return cupnUseLmtDyYn;
	}

	public void setCupnUseLmtDyYn(String cupnUseLmtDyYn) {
		this.cupnUseLmtDyYn = cupnUseLmtDyYn;
	}

	public String getCupnIssEndDy() {
		return cupnIssEndDy;
	}

	public void setCupnIssEndDy(String cupnIssEndDy) {
		this.cupnIssEndDy = cupnIssEndDy;
	}

	public String getPay11YN() {
		return pay11YN;
	}

	public void setPay11YN(String pay11yn) {
		pay11YN = pay11yn;
	}

	public String getPay11Value() {
		return pay11Value;
	}

	public void setPay11Value(String pay11Value) {
		this.pay11Value = pay11Value;
	}

	public String getPay11WyCd() {
		return pay11WyCd;
	}

	public void setPay11WyCd(String pay11WyCd) {
		this.pay11WyCd = pay11WyCd;
	}

	public String getIntFreeYN() {
		return intFreeYN;
	}

	public void setIntFreeYN(String intFreeYN) {
		this.intFreeYN = intFreeYN;
	}

	public String getIntfreeMonClfCd() {
		return intfreeMonClfCd;
	}

	public void setIntfreeMonClfCd(String intfreeMonClfCd) {
		this.intfreeMonClfCd = intfreeMonClfCd;
	}

	public String getPluYN() {
		return pluYN;
	}

	public void setPluYN(String pluYN) {
		this.pluYN = pluYN;
	}

	public String getPluDscCd() {
		return pluDscCd;
	}

	public void setPluDscCd(String pluDscCd) {
		this.pluDscCd = pluDscCd;
	}

	public String getPluDscBasis() {
		return pluDscBasis;
	}

	public void setPluDscBasis(String pluDscBasis) {
		this.pluDscBasis = pluDscBasis;
	}

	public String getPluDscAmtPercnt() {
		return pluDscAmtPercnt;
	}

	public void setPluDscAmtPercnt(String pluDscAmtPercnt) {
		this.pluDscAmtPercnt = pluDscAmtPercnt;
	}

	public String getPluDscMthdCd() {
		return pluDscMthdCd;
	}

	public void setPluDscMthdCd(String pluDscMthdCd) {
		this.pluDscMthdCd = pluDscMthdCd;
	}

	public String getPluUseLmtDyYn() {
		return pluUseLmtDyYn;
	}

	public void setPluUseLmtDyYn(String pluUseLmtDyYn) {
		this.pluUseLmtDyYn = pluUseLmtDyYn;
	}

	public String getPluIssStartDy() {
		return pluIssStartDy;
	}

	public void setPluIssStartDy(String pluIssStartDy) {
		this.pluIssStartDy = pluIssStartDy;
	}

	public String getPluIssEndDy() {
		return pluIssEndDy;
	}

	public void setPluIssEndDy(String pluIssEndDy) {
		this.pluIssEndDy = pluIssEndDy;
	}

	public String getHopeShpYn() {
		return hopeShpYn;
	}

	public void setHopeShpYn(String hopeShpYn) {
		this.hopeShpYn = hopeShpYn;
	}

	public String getHopeShpPnt() {
		return hopeShpPnt;
	}

	public void setHopeShpPnt(String hopeShpPnt) {
		this.hopeShpPnt = hopeShpPnt;
	}

	public String getHopeShpWyCd() {
		return hopeShpWyCd;
	}

	public void setHopeShpWyCd(String hopeShpWyCd) {
		this.hopeShpWyCd = hopeShpWyCd;
	}

	public String getOptSelectYn() {
		return optSelectYn;
	}

	public void setOptSelectYn(String optSelectYn) {
		this.optSelectYn = optSelectYn;
	}

	public String getTxtColCnt() {
		return txtColCnt;
	}

	public void setTxtColCnt(String txtColCnt) {
		this.txtColCnt = txtColCnt;
	}

	public String getOptionAllQty() {
		return optionAllQty;
	}

	public void setOptionAllQty(String optionAllQty) {
		this.optionAllQty = optionAllQty;
	}

	public String getOptionAllAddPrc() {
		return optionAllAddPrc;
	}

	public void setOptionAllAddPrc(String optionAllAddPrc) {
		this.optionAllAddPrc = optionAllAddPrc;
	}

	public String getOptionAllAddWght() {
		return optionAllAddWght;
	}

	public void setOptionAllAddWght(String optionAllAddWght) {
		this.optionAllAddWght = optionAllAddWght;
	}

	public String getPrdExposeClfCd() {
		return prdExposeClfCd;
	}

	public void setPrdExposeClfCd(String prdExposeClfCd) {
		this.prdExposeClfCd = prdExposeClfCd;
	}

	public String getOptMixYn() {
		return optMixYn;
	}

	public void setOptMixYn(String optMixYn) {
		this.optMixYn = optMixYn;
	}

	public String getOptUpdateYn() {
		return optUpdateYn;
	}

	public void setOptUpdateYn(String optUpdateYn) {
		this.optUpdateYn = optUpdateYn;
	}

	public String getColTitle() {
		return colTitle;
	}

	public void setColTitle(String colTitle) {
		this.colTitle = colTitle;
	}

	public List<ProductOption> getProductOption() {
		return productOption;
	}

	public void setProductOption(List<ProductOption> productOption) {
		this.productOption = productOption;
	}

	public ProductRootOption getProductRootOption() {
		return productRootOption;
	}

	public void setProductRootOption(ProductRootOption productRootOption) {
		this.productRootOption = productRootOption;
	}

	public ProductOptionExt getProductOptionExt() {
		return productOptionExt;
	}

	public void setProductOptionExt(ProductOptionExt productOptionExt) {
		this.productOptionExt = productOptionExt;
	}

	public List<ProductCustOption> getProductCustOption() {
		return productCustOption;
	}

	public void setProductCustOption(List<ProductCustOption> productCustOption) {
		this.productCustOption = productCustOption;
	}

	public String getUseOptCalc() {
		return useOptCalc;
	}

	public void setUseOptCalc(String useOptCalc) {
		this.useOptCalc = useOptCalc;
	}

	public String getOptCalcTranType() {
		return optCalcTranType;
	}

	public void setOptCalcTranType(String optCalcTranType) {
		this.optCalcTranType = optCalcTranType;
	}

	public String getOptTypCd() {
		return optTypCd;
	}

	public void setOptTypCd(String optTypCd) {
		this.optTypCd = optTypCd;
	}

	public String getOptItem1Nm() {
		return optItem1Nm;
	}

	public void setOptItem1Nm(String optItem1Nm) {
		this.optItem1Nm = optItem1Nm;
	}

	public String getOptItem1MinValue() {
		return optItem1MinValue;
	}

	public void setOptItem1MinValue(String optItem1MinValue) {
		this.optItem1MinValue = optItem1MinValue;
	}

	public String getOptItem1MaxValue() {
		return optItem1MaxValue;
	}

	public void setOptItem1MaxValue(String optItem1MaxValue) {
		this.optItem1MaxValue = optItem1MaxValue;
	}

	public String getOptItem2Nm() {
		return optItem2Nm;
	}

	public void setOptItem2Nm(String optItem2Nm) {
		this.optItem2Nm = optItem2Nm;
	}

	public String getOptItem2MinValue() {
		return optItem2MinValue;
	}

	public void setOptItem2MinValue(String optItem2MinValue) {
		this.optItem2MinValue = optItem2MinValue;
	}

	public String getOptItem2MaxValue() {
		return optItem2MaxValue;
	}

	public void setOptItem2MaxValue(String optItem2MaxValue) {
		this.optItem2MaxValue = optItem2MaxValue;
	}

	public String getOptUnitPrc() {
		return optUnitPrc;
	}

	public void setOptUnitPrc(String optUnitPrc) {
		this.optUnitPrc = optUnitPrc;
	}

	public String getOptUnitCd() {
		return optUnitCd;
	}

	public void setOptUnitCd(String optUnitCd) {
		this.optUnitCd = optUnitCd;
	}

	public String getOptSelUnit() {
		return optSelUnit;
	}

	public void setOptSelUnit(String optSelUnit) {
		this.optSelUnit = optSelUnit;
	}

	public List<ProductComponent> getProductComponent() {
		return productComponent;
	}

	public void setProductComponent(List<ProductComponent> productComponent) {
		this.productComponent = productComponent;
	}

	public String getPrdSelQty() {
		return prdSelQty;
	}

	public void setPrdSelQty(String prdSelQty) {
		this.prdSelQty = prdSelQty;
	}

	public String getSelMinLimitTypCd() {
		return selMinLimitTypCd;
	}

	public void setSelMinLimitTypCd(String selMinLimitTypCd) {
		this.selMinLimitTypCd = selMinLimitTypCd;
	}

	public String getSelMinLimitQty() {
		return selMinLimitQty;
	}

	public void setSelMinLimitQty(String selMinLimitQty) {
		this.selMinLimitQty = selMinLimitQty;
	}

	public String getSelLimitTypCd() {
		return selLimitTypCd;
	}

	public void setSelLimitTypCd(String selLimitTypCd) {
		this.selLimitTypCd = selLimitTypCd;
	}

	public String getSelLimitQty() {
		return selLimitQty;
	}

	public void setSelLimitQty(String selLimitQty) {
		this.selLimitQty = selLimitQty;
	}

	public String getTownSelLmtDy() {
		return townSelLmtDy;
	}

	public void setTownSelLmtDy(String townSelLmtDy) {
		this.townSelLmtDy = townSelLmtDy;
	}

	public String getUseGiftYn() {
		return useGiftYn;
	}

	public void setUseGiftYn(String useGiftYn) {
		this.useGiftYn = useGiftYn;
	}

	public List<ProductGift> getProductGift() {
		return productGift;
	}

	public void setProductGift(List<ProductGift> productGift) {
		this.productGift = productGift;
	}

	public String getGftPackTypCd() {
		return gftPackTypCd;
	}

	public void setGftPackTypCd(String gftPackTypCd) {
		this.gftPackTypCd = gftPackTypCd;
	}

	public String getGblDlvYn() {
		return gblDlvYn;
	}

	public void setGblDlvYn(String gblDlvYn) {
		this.gblDlvYn = gblDlvYn;
	}

	public String getDlvCnAreaCd() {
		return dlvCnAreaCd;
	}

	public void setDlvCnAreaCd(String dlvCnAreaCd) {
		this.dlvCnAreaCd = dlvCnAreaCd;
	}

	public String getDlvWyCd() {
		return dlvWyCd;
	}

	public void setDlvWyCd(String dlvWyCd) {
		this.dlvWyCd = dlvWyCd;
	}

	public String getDlvEtprsCd() {
		return dlvEtprsCd;
	}

	public void setDlvEtprsCd(String dlvEtprsCd) {
		this.dlvEtprsCd = dlvEtprsCd;
	}

	public String getDlvSendCloseTmpltNo() {
		return dlvSendCloseTmpltNo;
	}

	public void setDlvSendCloseTmpltNo(String dlvSendCloseTmpltNo) {
		this.dlvSendCloseTmpltNo = dlvSendCloseTmpltNo;
	}

	public String getDlvCstInstBasiCd() {
		return dlvCstInstBasiCd;
	}

	public void setDlvCstInstBasiCd(String dlvCstInstBasiCd) {
		this.dlvCstInstBasiCd = dlvCstInstBasiCd;
	}

	public String getDlvCst1() {
		return dlvCst1;
	}

	public void setDlvCst1(String dlvCst1) {
		this.dlvCst1 = dlvCst1;
	}

	public String getDlvCst3() {
		return dlvCst3;
	}

	public void setDlvCst3(String dlvCst3) {
		this.dlvCst3 = dlvCst3;
	}

	public String getDlvCst4() {
		return dlvCst4;
	}

	public void setDlvCst4(String dlvCst4) {
		this.dlvCst4 = dlvCst4;
	}

	public String getDlvCstInfoCd() {
		return dlvCstInfoCd;
	}

	public void setDlvCstInfoCd(String dlvCstInfoCd) {
		this.dlvCstInfoCd = dlvCstInfoCd;
	}

	public String getPrdFrDlvBasiAmt() {
		return PrdFrDlvBasiAmt;
	}

	public void setPrdFrDlvBasiAmt(String prdFrDlvBasiAmt) {
		PrdFrDlvBasiAmt = prdFrDlvBasiAmt;
	}

	public String getDlvCnt1() {
		return dlvCnt1;
	}

	public void setDlvCnt1(String dlvCnt1) {
		this.dlvCnt1 = dlvCnt1;
	}

	public String getDlvCnt2() {
		return dlvCnt2;
	}

	public void setDlvCnt2(String dlvCnt2) {
		this.dlvCnt2 = dlvCnt2;
	}

	public String getBndlDlvCnYn() {
		return bndlDlvCnYn;
	}

	public void setBndlDlvCnYn(String bndlDlvCnYn) {
		this.bndlDlvCnYn = bndlDlvCnYn;
	}

	public String getDlvCstPayTypCd() {
		return dlvCstPayTypCd;
	}

	public void setDlvCstPayTypCd(String dlvCstPayTypCd) {
		this.dlvCstPayTypCd = dlvCstPayTypCd;
	}

	public String getJejuDlvCst() {
		return jejuDlvCst;
	}

	public void setJejuDlvCst(String jejuDlvCst) {
		this.jejuDlvCst = jejuDlvCst;
	}

	public String getIslandDlvCst() {
		return islandDlvCst;
	}

	public void setIslandDlvCst(String islandDlvCst) {
		this.islandDlvCst = islandDlvCst;
	}

	public String getAddrSeqOut() {
		return addrSeqOut;
	}

	public void setAddrSeqOut(String addrSeqOut) {
		this.addrSeqOut = addrSeqOut;
	}

	public String getOutsideYnOut() {
		return outsideYnOut;
	}

	public void setOutsideYnOut(String outsideYnOut) {
		this.outsideYnOut = outsideYnOut;
	}

	public String getVisitDlvYn() {
		return visitDlvYn;
	}

	public void setVisitDlvYn(String visitDlvYn) {
		this.visitDlvYn = visitDlvYn;
	}

	public String getVisitAddrSeq() {
		return visitAddrSeq;
	}

	public void setVisitAddrSeq(String visitAddrSeq) {
		this.visitAddrSeq = visitAddrSeq;
	}

	public String getAddrSeqOutMemNo() {
		return addrSeqOutMemNo;
	}

	public void setAddrSeqOutMemNo(String addrSeqOutMemNo) {
		this.addrSeqOutMemNo = addrSeqOutMemNo;
	}

	public String getAddrSeqIn() {
		return addrSeqIn;
	}

	public void setAddrSeqIn(String addrSeqIn) {
		this.addrSeqIn = addrSeqIn;
	}

	public String getOutsideYnIn() {
		return outsideYnIn;
	}

	public void setOutsideYnIn(String outsideYnIn) {
		this.outsideYnIn = outsideYnIn;
	}

	public String getAddrSeqInMemNo() {
		return addrSeqInMemNo;
	}

	public void setAddrSeqInMemNo(String addrSeqInMemNo) {
		this.addrSeqInMemNo = addrSeqInMemNo;
	}

	public String getAbrdCnDlvCst() {
		return abrdCnDlvCst;
	}

	public void setAbrdCnDlvCst(String abrdCnDlvCst) {
		this.abrdCnDlvCst = abrdCnDlvCst;
	}

	public String getRtngdDlvCst() {
		return rtngdDlvCst;
	}

	public void setRtngdDlvCst(String rtngdDlvCst) {
		this.rtngdDlvCst = rtngdDlvCst;
	}

	public String getExchDlvCst() {
		return exchDlvCst;
	}

	public void setExchDlvCst(String exchDlvCst) {
		this.exchDlvCst = exchDlvCst;
	}

	public String getRtngdDlvCd() {
		return rtngdDlvCd;
	}

	public void setRtngdDlvCd(String rtngdDlvCd) {
		this.rtngdDlvCd = rtngdDlvCd;
	}

	public String getAsDetail() {
		return asDetail;
	}

	public void setAsDetail(String asDetail) {
		this.asDetail = asDetail;
	}

	public String getRtngExchDetail() {
		return rtngExchDetail;
	}

	public void setRtngExchDetail(String rtngExchDetail) {
		this.rtngExchDetail = rtngExchDetail;
	}

	public String getDlvClf() {
		return dlvClf;
	}

	public void setDlvClf(String dlvClf) {
		this.dlvClf = dlvClf;
	}

	public String getAbrdInCd() {
		return abrdInCd;
	}

	public void setAbrdInCd(String abrdInCd) {
		this.abrdInCd = abrdInCd;
	}

	public String getPrdWght() {
		return prdWght;
	}

	public void setPrdWght(String prdWght) {
		this.prdWght = prdWght;
	}

	public String getNtShortNm() {
		return ntShortNm;
	}

	public void setNtShortNm(String ntShortNm) {
		this.ntShortNm = ntShortNm;
	}

	public String getGlobalOutAddrSeq() {
		return globalOutAddrSeq;
	}

	public void setGlobalOutAddrSeq(String globalOutAddrSeq) {
		this.globalOutAddrSeq = globalOutAddrSeq;
	}

	public String getMbAddrLocation05() {
		return mbAddrLocation05;
	}

	public void setMbAddrLocation05(String mbAddrLocation05) {
		this.mbAddrLocation05 = mbAddrLocation05;
	}

	public String getGlobalInAddrSeq() {
		return globalInAddrSeq;
	}

	public void setGlobalInAddrSeq(String globalInAddrSeq) {
		this.globalInAddrSeq = globalInAddrSeq;
	}

	public String getMbAddrLocation06() {
		return mbAddrLocation06;
	}

	public void setMbAddrLocation06(String mbAddrLocation06) {
		this.mbAddrLocation06 = mbAddrLocation06;
	}

	public String getMnfcDy() {
		return mnfcDy;
	}

	public void setMnfcDy(String mnfcDy) {
		this.mnfcDy = mnfcDy;
	}

	public String getEftvDy() {
		return eftvDy;
	}

	public void setEftvDy(String eftvDy) {
		this.eftvDy = eftvDy;
	}

	public ProductNotification getProductNotification() {
		return productNotification;
	}

	public void setProductNotification(ProductNotification productNotification) {
		this.productNotification = productNotification;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getModelNm() {
		return modelNm;
	}

	public void setModelNm(String modelNm) {
		this.modelNm = modelNm;
	}

	public String getModelCd() {
		return modelCd;
	}

	public void setModelCd(String modelCd) {
		this.modelCd = modelCd;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getMudvdLabel() {
		return mudvdLabel;
	}

	public void setMudvdLabel(String mudvdLabel) {
		this.mudvdLabel = mudvdLabel;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getAlbumNm() {
		return albumNm;
	}

	public void setAlbumNm(String albumNm) {
		this.albumNm = albumNm;
	}

	public String getDvdTitle() {
		return dvdTitle;
	}

	public void setDvdTitle(String dvdTitle) {
		this.dvdTitle = dvdTitle;
	}

	public String getBcktExYn() {
		return bcktExYn;
	}

	public void setBcktExYn(String bcktExYn) {
		this.bcktExYn = bcktExYn;
	}

	public String getPrcCmpExpYn() {
		return prcCmpExpYn;
	}

	public void setPrcCmpExpYn(String prcCmpExpYn) {
		this.prcCmpExpYn = prcCmpExpYn;
	}

	public String getStdPrdYn() {
		return stdPrdYn;
	}

	public void setStdPrdYn(String stdPrdYn) {
		this.stdPrdYn = stdPrdYn;
	}

	public List<ProductTag> getProductTag() {
		return productTag;
	}

	public void setProductTag(List<ProductTag> productTag) {
		this.productTag = productTag;
	}

	public String getPrdNo() {
		return prdNo;
	}

	public void setPrdNo(String prdNo) {
		this.prdNo = prdNo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Product [abrdBuyPlace=" + abrdBuyPlace + ", abrdSizetableDispYn=" + abrdSizetableDispYn
				+ ", selMnbdNckNm=" + selMnbdNckNm + ", selMthdCd=" + selMthdCd + ", dispCtgrNo=" + dispCtgrNo
				+ ", prdTypCd=" + prdTypCd + ", gblHsCode=" + gblHsCode + ", prdNm=" + prdNm + ", prdNmEng=" + prdNmEng
				+ ", advrtStmt=" + advrtStmt + ", brand=" + brand + ", apiPrdAttrBrandCd=" + apiPrdAttrBrandCd
				+ ", rmaterialTypCd=" + rmaterialTypCd + ", orgnTypCd=" + orgnTypCd + ", orgnDifferentYn="
				+ orgnDifferentYn + ", orgnTypDtlsCd=" + orgnTypDtlsCd + ", orgnNmVal=" + orgnNmVal
				+ ", ProductRmaterial=" + productRmaterial + ", beefTraceStat=" + beefTraceStat + ", beefTraceNo="
				+ beefTraceNo + ", sellerPrdCd=" + sellerPrdCd + ", suplDtyfrPrdClfCd=" + suplDtyfrPrdClfCd
				+ ", yearEndTaxYn=" + yearEndTaxYn + ", forAbrdBuyClf=" + forAbrdBuyClf + ", importFeeCd=" + importFeeCd
				+ ", prdStatCd=" + prdStatCd + ", useMon=" + useMon + ", paidSelPrc=" + paidSelPrc
				+ ", exteriorSpecialNote=" + exteriorSpecialNote + ", minorSelCnYn=" + minorSelCnYn + ", prdImage01="
				+ prdImage01 + ", prdImage02=" + prdImage02 + ", prdImage03=" + prdImage03 + ", prdImage04="
				+ prdImage04 + ", prdImage05=" + prdImage05 + ", prdImage09=" + prdImage09 + ", prdImage01Src="
				+ prdImage01Src + ", htmlDetail=" + htmlDetail + ", ProductCertGroup=" + productCertGroup
				+ ", ProductMedical=" + productMedical + ", reviewDispYn=" + reviewDispYn + ", reviewOptDispYn="
				+ reviewOptDispYn + ", selPrdClfCd=" + selPrdClfCd + ", aplBgnDy=" + aplBgnDy + ", aplEndDy=" + aplEndDy
				+ ", setFpSelTermYn=" + setFpSelTermYn + ", selTermUseYn=" + selTermUseYn + ", selPrdClfFpCd="
				+ selPrdClfFpCd + ", wrhsPlnDy=" + wrhsPlnDy + ", contractCd=" + contractCd + ", chargeCd=" + chargeCd
				+ ", periodCd=" + periodCd + ", phonePrc=" + phonePrc + ", maktPrc=" + maktPrc + ", selPrc=" + selPrc
				+ ", cuponcheck=" + cuponcheck + ", dscAmtPercnt=" + dscAmtPercnt + ", cupnDscMthdCd=" + cupnDscMthdCd
				+ ", cupnUseLmtDyYn=" + cupnUseLmtDyYn + ", cupnIssEndDy=" + cupnIssEndDy + ", pay11YN=" + pay11YN
				+ ", pay11Value=" + pay11Value + ", pay11WyCd=" + pay11WyCd + ", intFreeYN=" + intFreeYN
				+ ", intfreeMonClfCd=" + intfreeMonClfCd + ", pluYN=" + pluYN + ", pluDscCd=" + pluDscCd
				+ ", pluDscBasis=" + pluDscBasis + ", pluDscAmtPercnt=" + pluDscAmtPercnt + ", pluDscMthdCd="
				+ pluDscMthdCd + ", pluUseLmtDyYn=" + pluUseLmtDyYn + ", pluIssStartDy=" + pluIssStartDy
				+ ", pluIssEndDy=" + pluIssEndDy + ", hopeShpYn=" + hopeShpYn + ", hopeShpPnt=" + hopeShpPnt
				+ ", hopeShpWyCd=" + hopeShpWyCd + ", optSelectYn=" + optSelectYn + ", txtColCnt=" + txtColCnt
				+ ", optionAllQty=" + optionAllQty + ", optionAllAddPrc=" + optionAllAddPrc + ", optionAllAddWght="
				+ optionAllAddWght + ", prdExposeClfCd=" + prdExposeClfCd + ", optMixYn=" + optMixYn
				+ ", ProductOption=" + productOption + ", ProductRootOption=" + productRootOption
				+ ", ProductOptionExt=" + productOptionExt + ", ProductCustOption=" + productCustOption
				+ ", useOptCalc=" + useOptCalc + ", optCalcTranType=" + optCalcTranType + ", optTypCd=" + optTypCd
				+ ", optItem1Nm=" + optItem1Nm + ", optItem1MinValue=" + optItem1MinValue + ", optItem1MaxValue="
				+ optItem1MaxValue + ", optItem2Nm=" + optItem2Nm + ", optItem2MinValue=" + optItem2MinValue
				+ ", optItem2MaxValue=" + optItem2MaxValue + ", optUnitPrc=" + optUnitPrc + ", optUnitCd=" + optUnitCd
				+ ", optSelUnit=" + optSelUnit + ", ProductComponent=" + productComponent + ", prdSelQty=" + prdSelQty
				+ ", selMinLimitTypCd=" + selMinLimitTypCd + ", selMinLimitQty=" + selMinLimitQty + ", selLimitTypCd="
				+ selLimitTypCd + ", selLimitQty=" + selLimitQty + ", townSelLmtDy=" + townSelLmtDy + ", useGiftYn="
				+ useGiftYn + ", ProductGift=" + productGift + ", gftPackTypCd=" + gftPackTypCd + ", gblDlvYn="
				+ gblDlvYn + ", dlvCnAreaCd=" + dlvCnAreaCd + ", dlvWyCd=" + dlvWyCd + ", dlvEtprsCd=" + dlvEtprsCd
				+ ", dlvSendCloseTmpltNo=" + dlvSendCloseTmpltNo + ", dlvCstInstBasiCd=" + dlvCstInstBasiCd
				+ ", dlvCst1=" + dlvCst1 + ", dlvCst3=" + dlvCst3 + ", dlvCst4=" + dlvCst4 + ", dlvCstInfoCd="
				+ dlvCstInfoCd + ", PrdFrDlvBasiAmt=" + PrdFrDlvBasiAmt + ", dlvCnt1=" + dlvCnt1 + ", dlvCnt2="
				+ dlvCnt2 + ", bndlDlvCnYn=" + bndlDlvCnYn + ", dlvCstPayTypCd=" + dlvCstPayTypCd + ", jejuDlvCst="
				+ jejuDlvCst + ", islandDlvCst=" + islandDlvCst + ", addrSeqOut=" + addrSeqOut + ", outsideYnOut="
				+ outsideYnOut + ", visitDlvYn=" + visitDlvYn + ", visitAddrSeq=" + visitAddrSeq + ", addrSeqOutMemNo="
				+ addrSeqOutMemNo + ", addrSeqIn=" + addrSeqIn + ", outsideYnIn=" + outsideYnIn + ", addrSeqInMemNo="
				+ addrSeqInMemNo + ", abrdCnDlvCst=" + abrdCnDlvCst + ", rtngdDlvCst=" + rtngdDlvCst + ", exchDlvCst="
				+ exchDlvCst + ", rtngdDlvCd=" + rtngdDlvCd + ", asDetail=" + asDetail + ", rtngExchDetail="
				+ rtngExchDetail + ", dlvClf=" + dlvClf + ", abrdInCd=" + abrdInCd + ", prdWght=" + prdWght
				+ ", ntShortNm=" + ntShortNm + ", globalOutAddrSeq=" + globalOutAddrSeq + ", mbAddrLocation05="
				+ mbAddrLocation05 + ", globalInAddrSeq=" + globalInAddrSeq + ", mbAddrLocation06=" + mbAddrLocation06
				+ ", mnfcDy=" + mnfcDy + ", eftvDy=" + eftvDy + ", ProductNotification=" + productNotification
				+ ", company=" + company + ", modelNm=" + modelNm + ", modelCd=" + modelCd + ", mainTitle=" + mainTitle
				+ ", artist=" + artist + ", mudvdLabel=" + mudvdLabel + ", maker=" + maker + ", albumNm=" + albumNm
				+ ", dvdTitle=" + dvdTitle + ", bcktExYn=" + bcktExYn + ", prcCmpExpYn=" + prcCmpExpYn + ", stdPrdYn="
				+ stdPrdYn + ", ProductTag=" + productTag + ", prdNo=" + prdNo + ", message=" + message + "]";
	}

}
