package com.cware.netshopping.passg.v2.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

	// 결과 코드
	// (00 정상, 나머지는 오류)
	String resultCode;
	String resultMessage; // 결과 값
	String resultDesc; // 오류 내용
	String splVenId; // 공급 업체 ID
	String lrnkSplVenId; // 하위 공급 업체 ID
	String itemId; // 상품 번호
	String itemNm; // 상품 명

    @XmlElementWrapper
    @XmlElement(name="mtlglItem")
	List<MtlglItem> mtlglItemInfos; // 다국어 상품 정보
	String mdlNm; // 모델 명
	String brandId; // 브랜드 ID
	String stdCtgId; // 표준카테고리 ID

    @XmlElementWrapper
    @XmlElement(name="site")
	List<Site> sites;

	// 상품 적용 범위(commCd:I012)
	// 00 : 전체 적용
	// 20 : B2E 적용
	String itemAplRngTypeCd;

	// B2E 적용 범위(commCd:I072)
	// 10 : 전체 적용
	// 30 : 회원사 지정
	String b2eAplRngCd;

	// B2C 적용 범위(commCd:I073)
	// 10 : 적용
	// 20 : 적용 (대행 제휴사 제외)
	// 70 : 적용 않음
	String b2cAplRngCd;

	// 상품 특성 구분 코드(commCd:I010)
	// 10 : 일반
	// 40 : 미가공 귀금속
	// 50 : 모바일 기프트
	// 60 : 상품권
	// 70 : 쇼핑 충전금
	// TX : 도서/공연 소득공제
	String itemChrctDivCd;

	// 상품 특성 상세 코드
	// 상품 특성 구분 코드(itemChrctDivCd = 50 | 60) 일 경우
	// 상품 특성 구분 코드(itemChrctDivCd = 50) :
	// 10 : 일반
	// 50 : 상품권
	// 상품 특성 구분 코드(itemChrctDivCd = 60) :
	// 60 : 신세계 지류 상품권
	// 70 : 외부 지류 상품권
	// 80 : 기프트 카드
	// 90 : 맞춤형 기프트 카드
	String itemChrctDtlCd;

	// 전용 상품 구분 코드(commCd:I008)
	// 10 : 일반
	// 20 : GIFT(일반)
	String exusItemDivCd;

	// 전용 상품 상세 코드(commCd:I071)
	// 10 : 일반
	// 20 : 특정점
	String exusItemDtlCd;

	// 전시 적용 범위 유형 코드
	// 10 : 전체 (모바일 + PC)
	// 30 : 모바일 (모바일 선택시 전체로 설정 불가)
	String dispAplRngTypeCd;

	// 특정 영업점 번호 (특정점 (exusItemDtlCd=20)일 경우 입력)
	// ※ 특정점코드 API 참조
	String speSalestrNo;

	// 판매상태코드 (commCd:I004)
	// 05 : 정보추가필요
	// 10 : 승인전
	// 20 : 판매중
	// 80 : 일시판매중지
	// 90 : 영구판매중지
	String sellStatCd;
	String itemMngPropClsId; // 상품 관리 항목 분류 ID

    @XmlElementWrapper
    @XmlElement(name="itemMngAttr")
	List<ItemMngAttr> itemMngAttrs;
	String manufcoNm; // 제조사 명
	String prodManufCntryId; // 생산 제조 국가 ID (참고 : 원산지조회API(listOrplc API))
	
    @XmlElementWrapper
    @XmlElement(name="dispCtg")
	List<DispCtg> dispCtgs;
	String dispStrtDts; // 전시 시작 일시 (YYYYMMDD)
	String dispEndDts; // 전시 종료 일시 (YYYYMMDD)

	String srchPsblYn; // 검색 가능 여부
	String itemSrchwdNm; // 상품검색어명

	// 노출 회원 등급 (값이 존재하지 않을 경우 ALL)
	// 10 : 패밀리
	// 20 : 브론즈
	// 30 : 실버
	// 40 : 골드
	// 50 : VIP
	// 90 : VVIP
	String aplMbrGrdCd;
	Integer minOnetOrdPsblQty; // 최소 1회 주문 가능 수량
	Integer maxOnetOrdPsblQty; // 최대 1회 주문 가능 수량
	Integer max1dyOrdPsblQty; // 최대 1일 주문 가능 수량

	// 성인 상품 타입 코드 (commCd:I408)
	// 10 : 성인 상품
	// 20 : 주류 상품
	// 90 : 일반 상품
	String adultItemTypeCd;
	String hriskItemYn; // 고 위험 상품 여부
	String nitmAplYn; // 신 상품 적용 여부

    @XmlElementWrapper
    @XmlElement(name="sellPnt")
	List<SellPnt> sellPnts;

	// 판매 용량 단위 코드 (commCd:I159)
	// 03 g
	// 04 kg
	// 05 m
	// 06 ml
	// 08 개
	// 09 매
	// 12 cm
	String sellCapaUnitCd;
	Integer sellTotCapa; // 개별 용량

	// 판매 단위 용량
	// 1, 10, 100 만 가능
	Integer sellUnitCapa;
	Integer sellUnitQty; // 판매 단위 수량

	// 매입 형태 코드 (commCd:I002)
	// 10 : 직매입
	// 20 : 직매입2(판매분)
	// 40 : 특정매입
	// 60 : 위수탁
	String buyFrmCd;

	// 과세 구분 코드 (commCd:I005)
	// 10 : 과세
	// 20 : 면세
	// 30 : 영세
	String txnDivCd;
	
	// 가격설정방식
	// 1 : 공급가 자동계산 (Default)
	// 2 : 판매가 자동계산
	// 3 : 마진 자동계산
 	// 이 값 설정시 SALE_PRC_INFO, B2E_PRC 둘다 적용 받는다.
	// 값은 모두 입력 받아도 상관 없으나 해당 값 설정에 따라 해당 값이 자동으로 계산됨.
	String prcMngMthd;


    @XmlElementWrapper
    @XmlElement(name="uitemPrc")
	List<UitemPrc> salesPrcInfos;

    @XmlElementWrapper
    @XmlElement(name="uitemPrc")
	List<UitemPrc> chgSalesPrcInfos;

    @XmlElementWrapper
    @XmlElement(name="uitemPrc")
	List<UitemPrc> returnSalesPrcInfos;
	String b2ePrcMngMthdCd; // B2E 가격 관리 방법 코드

    @XmlElementWrapper
    @XmlElement(name="b2ePrcAplTgt")
	List<B2ePrcAplTgt> b2ePrcAplTgts;
    
	String invMngYn; // 재고 관리 여부
	Integer baseInvQty; // 재고 수량
	String invQtyMarkgYn; // 재고 수량 표기 여부
	RsvSaleInfo rsvSaleInfo;

	// 상품판매유형코드 (commCd:I006)
	// 10 : 일반
	// 20 : 옵션
	String itemSellTypeCd;

	// 상품판매유형상세코드 (commCd:I007)
	// 10 : 일반
	// 30 : 30 기획 (신세계몰은 기획상품 불가능)
	String itemSellTypeDtlCd;
	UitemAttr uitemAttr; // (itemSellTypeCd=20)

    @XmlElementWrapper
    @XmlElement(name="uitem")
	List<Uitem> uitems; // (itemSellTypeCd=20 and itemSellTypeDtlCd=10)

    @XmlElementWrapper
    @XmlElement(name="uitemOptnAddt")
	List<UitemOptnAddt> uitemOptnAddts;

    @XmlElementWrapper
    @XmlElement(name="itemOrdOptn")
	List<ItemOrdOptn> itemOrdOptns;
	UitemEaitemOptnCac uitemEaitemOptnCac;

    @XmlElementWrapper
    @XmlElement(name="uitemPrc")
	List<UitemPrc> uitemPluralPrcs; // (itemSellTypeCd=20)

    @XmlElementWrapper
    @XmlElement(name="uitemPrc")
	List<UitemPrc> chgUitemPluralPrcs; // (itemSellTypeCd=20)

    @XmlElementWrapper
    @XmlElement(name="uitemPrc")
	List<UitemPrc> returnUitemPluralPrcs; // (itemSellTypeCd=20)

	// 배송상품구분코드 (commCd:I070)
	// 01 : 일반
	// 03 : 설치(유료)
	// 04 : 설치(무료)
	// 05 : 주문제작
	// 06 : 해외직배송
	String shppItemDivCd;

	// 수출국가(해외 직배송 적출국)shppItemDivCd=06(해외직배송) 인 경우 필수
	// 원산지 조회 API 참고(listOrplc API)
	String exprtCntryId;
	String retExchPsblYn; // 반품 교환 가능 여부

	// 배송 주체 코드 (commCd:P017)
	// 31 : 자사창고
	// 32 : 업체창고
	// 41 : 협력업체
	// v0.4 버전부터 미사용 / v0.3 종료 시 제거
	String shppMainCd;

	// 배송 방법 코드 (commCd:P021)
	// 10 : 자사배송
	// 20 : 택배배송
	// 30 : 매장방문
	// 40 : 등기
	// 50 : 미배송
	// 60 : 미발송
	// 90 : 특수배송
	// v0.4 버전부터 미사용 / v0.3 종료 시 제거
	String shppMthdCd;

	// 수도권 배송여부
	// v0.4 버전부터 미사용 / v0.3 종료 시 제거
	String mareaShppYn;

	// 제주도 배송불가 여부
	// v0.4 버전부터 미사용 / v0.3 종료 시 제거
	String jejuShppDisabYn;

	// 도서산간 배송불가 여부
	// v0.4 버전부터 미사용 / v0.3 종료 시 제거
	String ismtarShppDisabYn;
	Integer shppRqrmDcnt; // 배송 소요 일수

	// 배송 소요 일수 변경 사유
	// 상품배송구분이 일반(01) 이고 배송소요일수가 8일 이상일 경우 필수
	String shppRqrmDcntChngRsnCntt;
	String splVenItemId; // 업체 상품 번호

	// 출고 배송비 ID
	// v0.4 버전부터 미사용 / v0.3 종료 시 제거
	String whoutShppcstId;

	// 반품 배송비 ID
	// v0.4 버전부터 미사용 / v0.3 종료 시 제거
	String retShppcstId;

	// 도서산간 추가배송비 ID
	// v0.4 버전부터 미사용 / v0.3 종료 시 제거
	String ismtarAddShppcstId;

	// 제주도 추가배송비 ID
	// v0.4 버전부터 미사용 / v0.3 종료 시 제거
	String jejuAddShppcstId;

	// 출고 주소 ID
	// v0.4 버전부터 미사용 / v0.3 종료 시 제거
	String whoutAddrId;

	// 반품 주소 ID
	// v0.4 버전부터 미사용 / v0.3 종료 시 제거
	String snbkAddrId;

	Double itemTotWgt; // 상품 총 무게

	// 희망 발송일 구분 코드 (commCd:I015)
	// <토요일 배송 제외>
	// 10 : 15일이내
	// 61 : 15일이내(내일출고가능)
	// 20 : 15일이후 30일이내
	// 30 : 30일이후
	// 90 : 발송일 최대 날짜 지정
	// <토요일 배송 포함>
	// 50 : 15일이내
	// 60 : 15일이내(내일출고가능)
	// 70 : 15일이후 30일이내
	// 80 : 30일이후 60일이내
	// 91 : 발송일 최대 날짜 지정
	String hopeShppDdDivCd;

	// 희망 발송일 종료 일시 (YYYYMMDD)
	// 희망발송일 구분코드가 (hopeShppDdEndDts=90 or 91) 일경우 필수
	String hopeShppDdEndDts;

    @XmlElementWrapper
    @XmlElement(name="imgInfo")
	List<ImgInfo> itemImgs;

    @XmlElementWrapper
    @XmlElement(name="imgInfo")
	List<ImgInfo> qualityViewImgs;
	String itemDesc; // 상품 상세 설명
	String sizeDesc; // 사이즈 조견표
	String purchGuideCntt; // 구매 안내 내용
	String asMemoCntt; // AS 메모 내용

    @XmlElementWrapper
    @XmlElement(name="qualityFile")
	List<QualityFile> qualityFiles;
	ChldCert chldCert;

    @XmlElementWrapper
    @XmlElement(name="certInfo")
	List<CertInfo> certInfos;

    @XmlElementWrapper
    @XmlElement(name="prop")
	List<Prop> certificationProps; // 등록전문에만 존재
	String giftPsblYn; // 선물 가능 여부
	String shppMsgId; // 배송 메시지 ID
	String ssgstrSellYn; // SSG 스토어(하남) 판매 여부
	String vodExtnlPathUrl; // 동영상 외부 경로 URL (허용 업체에 한하여)
	String palimpItemYn; // 병행 수입 상품 여부

	// 상품 판매 방식 코드 (commCd:I392)
	// 10 일반
	// 20 렌탈
	// 30 사전 예약
	// 40 할부
	String itemSellWayCd;

	// 상품 상태 유형 코드 (commCd:I393)
	// 10 새상품
	// 20 중고
	// 30 리퍼
	// 40 전시
	// 50 반품
	// 60 스크래치
	String itemStatTypeCd;
	String whinNotiYn; // 입고 알림 여부
	FstPriceInfo fstPriceInfo;
	Book book;
	String tourItemRedirectUrlPc; // PC 용 여행 상품 페이지 URL
	String tourItemRedirectUrlMobile; // Mobile 용 여행 상품 페이지 URL
	String giftPackPsblYn; // 선물 포장 가능 여부
	String ctvatInclYn; // 관부가세 포함여부

    @XmlElementWrapper
    @XmlElement(name="tag")
	List<Tag> tags;

	// 상품 여행속성 분류ID : 패키지 또는 액티비티 ID
	// *세분류 조회시 확인 가능
	// 세분류 조회시, 상품 여행속성 분류가 존재할 경우 해당 분류코드를 입력해야 함.
	// 대륙/국가/도시 코드 입력하고 싶을 경우, 상품 여행속성 분류 ID는 세분류와 매핑된 분류ID로 필수 입력되어야 함.
	String itemTripPropClsId;
	String itemTripPropClsNm; // 상품 여행속성 분류명

    @XmlElementWrapper
    @XmlElement(name="itemTripAttr")
	List<ItemTripAttr> itemTripAttrs;
	String luxprGantItemYn; // 개런티 상품 여부
	String ipotRptNo; // 수입면장신고번호

    @XmlElementWrapper
    @XmlElement(name="itemShppCritn")
	List<ItemShppCritn> itemShppCritns; // v0.5 버전부터 지원

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getSplVenId() {
		return splVenId;
	}

	public void setSplVenId(String splVenId) {
		this.splVenId = splVenId;
	}

	public String getLrnkSplVenId() {
		return lrnkSplVenId;
	}

	public void setLrnkSplVenId(String lrnkSplVenId) {
		this.lrnkSplVenId = lrnkSplVenId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemNm() {
		return itemNm;
	}

	public void setItemNm(String itemNm) {
		this.itemNm = itemNm;
	}

	public String getMdlNm() {
		return mdlNm;
	}

	public void setMdlNm(String mdlNm) {
		this.mdlNm = mdlNm;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getStdCtgId() {
		return stdCtgId;
	}

	public void setStdCtgId(String stdCtgId) {
		this.stdCtgId = stdCtgId;
	}

	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

	public String getItemAplRngTypeCd() {
		return itemAplRngTypeCd;
	}

	public void setItemAplRngTypeCd(String itemAplRngTypeCd) {
		this.itemAplRngTypeCd = itemAplRngTypeCd;
	}

	public String getB2eAplRngCd() {
		return b2eAplRngCd;
	}

	public void setB2eAplRngCd(String b2eAplRngCd) {
		this.b2eAplRngCd = b2eAplRngCd;
	}

	public String getB2cAplRngCd() {
		return b2cAplRngCd;
	}

	public void setB2cAplRngCd(String b2cAplRngCd) {
		this.b2cAplRngCd = b2cAplRngCd;
	}

	public String getItemChrctDivCd() {
		return itemChrctDivCd;
	}

	public void setItemChrctDivCd(String itemChrctDivCd) {
		this.itemChrctDivCd = itemChrctDivCd;
	}

	public String getItemChrctDtlCd() {
		return itemChrctDtlCd;
	}

	public void setItemChrctDtlCd(String itemChrctDtlCd) {
		this.itemChrctDtlCd = itemChrctDtlCd;
	}

	public String getExusItemDivCd() {
		return exusItemDivCd;
	}

	public void setExusItemDivCd(String exusItemDivCd) {
		this.exusItemDivCd = exusItemDivCd;
	}

	public String getExusItemDtlCd() {
		return exusItemDtlCd;
	}

	public void setExusItemDtlCd(String exusItemDtlCd) {
		this.exusItemDtlCd = exusItemDtlCd;
	}

	public String getDispAplRngTypeCd() {
		return dispAplRngTypeCd;
	}

	public void setDispAplRngTypeCd(String dispAplRngTypeCd) {
		this.dispAplRngTypeCd = dispAplRngTypeCd;
	}

	public String getSpeSalestrNo() {
		return speSalestrNo;
	}

	public void setSpeSalestrNo(String speSalestrNo) {
		this.speSalestrNo = speSalestrNo;
	}

	public String getSellStatCd() {
		return sellStatCd;
	}

	public void setSellStatCd(String sellStatCd) {
		this.sellStatCd = sellStatCd;
	}

	public String getItemMngPropClsId() {
		return itemMngPropClsId;
	}

	public void setItemMngPropClsId(String itemMngPropClsId) {
		this.itemMngPropClsId = itemMngPropClsId;
	}

	public List<ItemMngAttr> getItemMngAttrs() {
		return itemMngAttrs;
	}

	public void setItemMngAttrs(List<ItemMngAttr> itemMngAttrs) {
		this.itemMngAttrs = itemMngAttrs;
	}

	public String getManufcoNm() {
		return manufcoNm;
	}

	public void setManufcoNm(String manufcoNm) {
		this.manufcoNm = manufcoNm;
	}

	public String getProdManufCntryId() {
		return prodManufCntryId;
	}

	public void setProdManufCntryId(String prodManufCntryId) {
		this.prodManufCntryId = prodManufCntryId;
	}

	public List<DispCtg> getDispCtgs() {
		return dispCtgs;
	}

	public void setDispCtgs(List<DispCtg> dispCtgs) {
		this.dispCtgs = dispCtgs;
	}

	public String getSrchPsblYn() {
		return srchPsblYn;
	}

	public void setSrchPsblYn(String srchPsblYn) {
		this.srchPsblYn = srchPsblYn;
	}

	public String getItemSrchwdNm() {
		return itemSrchwdNm;
	}

	public void setItemSrchwdNm(String itemSrchwdNm) {
		this.itemSrchwdNm = itemSrchwdNm;
	}

	public String getAplMbrGrdCd() {
		return aplMbrGrdCd;
	}

	public void setAplMbrGrdCd(String aplMbrGrdCd) {
		this.aplMbrGrdCd = aplMbrGrdCd;
	}

	public Integer getMinOnetOrdPsblQty() {
		return minOnetOrdPsblQty;
	}

	public void setMinOnetOrdPsblQty(Integer minOnetOrdPsblQty) {
		this.minOnetOrdPsblQty = minOnetOrdPsblQty;
	}

	public Integer getMaxOnetOrdPsblQty() {
		return maxOnetOrdPsblQty;
	}

	public void setMaxOnetOrdPsblQty(Integer maxOnetOrdPsblQty) {
		this.maxOnetOrdPsblQty = maxOnetOrdPsblQty;
	}

	public Integer getMax1dyOrdPsblQty() {
		return max1dyOrdPsblQty;
	}

	public void setMax1dyOrdPsblQty(Integer max1dyOrdPsblQty) {
		this.max1dyOrdPsblQty = max1dyOrdPsblQty;
	}

	public String getAdultItemTypeCd() {
		return adultItemTypeCd;
	}

	public void setAdultItemTypeCd(String adultItemTypeCd) {
		this.adultItemTypeCd = adultItemTypeCd;
	}

	public String getHriskItemYn() {
		return hriskItemYn;
	}

	public void setHriskItemYn(String hriskItemYn) {
		this.hriskItemYn = hriskItemYn;
	}

	public String getNitmAplYn() {
		return nitmAplYn;
	}

	public void setNitmAplYn(String nitmAplYn) {
		this.nitmAplYn = nitmAplYn;
	}

	public String getSellCapaUnitCd() {
		return sellCapaUnitCd;
	}

	public void setSellCapaUnitCd(String sellCapaUnitCd) {
		this.sellCapaUnitCd = sellCapaUnitCd;
	}

	public Integer getSellTotCapa() {
		return sellTotCapa;
	}

	public void setSellTotCapa(Integer sellTotCapa) {
		this.sellTotCapa = sellTotCapa;
	}

	public Integer getSellUnitCapa() {
		return sellUnitCapa;
	}

	public void setSellUnitCapa(Integer sellUnitCapa) {
		this.sellUnitCapa = sellUnitCapa;
	}

	public Integer getSellUnitQty() {
		return sellUnitQty;
	}

	public void setSellUnitQty(Integer sellUnitQty) {
		this.sellUnitQty = sellUnitQty;
	}

	public String getBuyFrmCd() {
		return buyFrmCd;
	}

	public void setBuyFrmCd(String buyFrmCd) {
		this.buyFrmCd = buyFrmCd;
	}

	public String getTxnDivCd() {
		return txnDivCd;
	}

	public void setTxnDivCd(String txnDivCd) {
		this.txnDivCd = txnDivCd;
	}

	public List<UitemPrc> getSalesPrcInfos() {
		return salesPrcInfos;
	}

	public void setSalesPrcInfos(List<UitemPrc> salesPrcInfos) {
		this.salesPrcInfos = salesPrcInfos;
	}

	public String getB2ePrcMngMthdCd() {
		return b2ePrcMngMthdCd;
	}

	public void setB2ePrcMngMthdCd(String b2ePrcMngMthdCd) {
		this.b2ePrcMngMthdCd = b2ePrcMngMthdCd;
	}

	public List<B2ePrcAplTgt> getB2ePrcAplTgts() {
		return b2ePrcAplTgts;
	}

	public void setB2ePrcAplTgts(List<B2ePrcAplTgt> b2ePrcAplTgts) {
		this.b2ePrcAplTgts = b2ePrcAplTgts;
	}

	public String getInvMngYn() {
		return invMngYn;
	}

	public void setInvMngYn(String invMngYn) {
		this.invMngYn = invMngYn;
	}

	public Integer getBaseInvQty() {
		return baseInvQty;
	}

	public void setBaseInvQty(Integer baseInvQty) {
		this.baseInvQty = baseInvQty;
	}

	public String getInvQtyMarkgYn() {
		return invQtyMarkgYn;
	}

	public void setInvQtyMarkgYn(String invQtyMarkgYn) {
		this.invQtyMarkgYn = invQtyMarkgYn;
	}

	public RsvSaleInfo getRsvSaleInfo() {
		return rsvSaleInfo;
	}

	public void setRsvSaleInfo(RsvSaleInfo rsvSaleInfo) {
		this.rsvSaleInfo = rsvSaleInfo;
	}

	public String getItemSellTypeCd() {
		return itemSellTypeCd;
	}

	public void setItemSellTypeCd(String itemSellTypeCd) {
		this.itemSellTypeCd = itemSellTypeCd;
	}

	public String getItemSellTypeDtlCd() {
		return itemSellTypeDtlCd;
	}

	public void setItemSellTypeDtlCd(String itemSellTypeDtlCd) {
		this.itemSellTypeDtlCd = itemSellTypeDtlCd;
	}

	public UitemAttr getUitemAttr() {
		return uitemAttr;
	}

	public void setUitemAttr(UitemAttr uitemAttr) {
		this.uitemAttr = uitemAttr;
	}

	public List<Uitem> getUitems() {
		return uitems;
	}

	public void setUitems(List<Uitem> uitems) {
		this.uitems = uitems;
	}

	public UitemEaitemOptnCac getUitemEaitemOptnCac() {
		return uitemEaitemOptnCac;
	}

	public void setUitemEaitemOptnCac(UitemEaitemOptnCac uitemEaitemOptnCac) {
		this.uitemEaitemOptnCac = uitemEaitemOptnCac;
	}

	public List<UitemPrc> getUitemPluralPrcs() {
		return uitemPluralPrcs;
	}

	public void setUitemPluralPrcs(List<UitemPrc> uitemPluralPrcs) {
		this.uitemPluralPrcs = uitemPluralPrcs;
	}

	public String getShppItemDivCd() {
		return shppItemDivCd;
	}

	public void setShppItemDivCd(String shppItemDivCd) {
		this.shppItemDivCd = shppItemDivCd;
	}

	public String getExprtCntryId() {
		return exprtCntryId;
	}

	public void setExprtCntryId(String exprtCntryId) {
		this.exprtCntryId = exprtCntryId;
	}

	public String getRetExchPsblYn() {
		return retExchPsblYn;
	}

	public void setRetExchPsblYn(String retExchPsblYn) {
		this.retExchPsblYn = retExchPsblYn;
	}

	public String getShppMainCd() {
		return shppMainCd;
	}

	public void setShppMainCd(String shppMainCd) {
		this.shppMainCd = shppMainCd;
	}

	public String getShppMthdCd() {
		return shppMthdCd;
	}

	public void setShppMthdCd(String shppMthdCd) {
		this.shppMthdCd = shppMthdCd;
	}

	public String getMareaShppYn() {
		return mareaShppYn;
	}

	public void setMareaShppYn(String mareaShppYn) {
		this.mareaShppYn = mareaShppYn;
	}

	public String getJejuShppDisabYn() {
		return jejuShppDisabYn;
	}

	public void setJejuShppDisabYn(String jejuShppDisabYn) {
		this.jejuShppDisabYn = jejuShppDisabYn;
	}

	public String getIsmtarShppDisabYn() {
		return ismtarShppDisabYn;
	}

	public void setIsmtarShppDisabYn(String ismtarShppDisabYn) {
		this.ismtarShppDisabYn = ismtarShppDisabYn;
	}

	public Integer getShppRqrmDcnt() {
		return shppRqrmDcnt;
	}

	public void setShppRqrmDcnt(Integer shppRqrmDcnt) {
		this.shppRqrmDcnt = shppRqrmDcnt;
	}

	public String getShppRqrmDcntChngRsnCntt() {
		return shppRqrmDcntChngRsnCntt;
	}

	public void setShppRqrmDcntChngRsnCntt(String shppRqrmDcntChngRsnCntt) {
		this.shppRqrmDcntChngRsnCntt = shppRqrmDcntChngRsnCntt;
	}

	public String getSplVenItemId() {
		return splVenItemId;
	}

	public void setSplVenItemId(String splVenItemId) {
		this.splVenItemId = splVenItemId;
	}

	public String getWhoutShppcstId() {
		return whoutShppcstId;
	}

	public void setWhoutShppcstId(String whoutShppcstId) {
		this.whoutShppcstId = whoutShppcstId;
	}

	public String getRetShppcstId() {
		return retShppcstId;
	}

	public void setRetShppcstId(String retShppcstId) {
		this.retShppcstId = retShppcstId;
	}

	public String getIsmtarAddShppcstId() {
		return ismtarAddShppcstId;
	}

	public void setIsmtarAddShppcstId(String ismtarAddShppcstId) {
		this.ismtarAddShppcstId = ismtarAddShppcstId;
	}

	public String getJejuAddShppcstId() {
		return jejuAddShppcstId;
	}

	public void setJejuAddShppcstId(String jejuAddShppcstId) {
		this.jejuAddShppcstId = jejuAddShppcstId;
	}

	public String getWhoutAddrId() {
		return whoutAddrId;
	}

	public void setWhoutAddrId(String whoutAddrId) {
		this.whoutAddrId = whoutAddrId;
	}

	public String getSnbkAddrId() {
		return snbkAddrId;
	}

	public void setSnbkAddrId(String snbkAddrId) {
		this.snbkAddrId = snbkAddrId;
	}

	public Double getItemTotWgt() {
		return itemTotWgt;
	}

	public void setItemTotWgt(Double itemTotWgt) {
		this.itemTotWgt = itemTotWgt;
	}

	public String getHopeShppDdDivCd() {
		return hopeShppDdDivCd;
	}

	public void setHopeShppDdDivCd(String hopeShppDdDivCd) {
		this.hopeShppDdDivCd = hopeShppDdDivCd;
	}

	public String getHopeShppDdEndDts() {
		return hopeShppDdEndDts;
	}

	public void setHopeShppDdEndDts(String hopeShppDdEndDts) {
		this.hopeShppDdEndDts = hopeShppDdEndDts;
	}

	public List<ImgInfo> getItemImgs() {
		return itemImgs;
	}

	public void setItemImgs(List<ImgInfo> itemImgs) {
		this.itemImgs = itemImgs;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getSizeDesc() {
		return sizeDesc;
	}

	public void setSizeDesc(String sizeDesc) {
		this.sizeDesc = sizeDesc;
	}

	public String getPurchGuideCntt() {
		return purchGuideCntt;
	}

	public void setPurchGuideCntt(String purchGuideCntt) {
		this.purchGuideCntt = purchGuideCntt;
	}

	public String getAsMemoCntt() {
		return asMemoCntt;
	}

	public void setAsMemoCntt(String asMemoCntt) {
		this.asMemoCntt = asMemoCntt;
	}

	public ChldCert getChldCert() {
		return chldCert;
	}

	public void setChldCert(ChldCert chldCert) {
		this.chldCert = chldCert;
	}

	public List<CertInfo> getCertInfos() {
		return certInfos;
	}

	public void setCertInfos(List<CertInfo> certInfos) {
		this.certInfos = certInfos;
	}

	public String getGiftPsblYn() {
		return giftPsblYn;
	}

	public void setGiftPsblYn(String giftPsblYn) {
		this.giftPsblYn = giftPsblYn;
	}

	public String getShppMsgId() {
		return shppMsgId;
	}

	public void setShppMsgId(String shppMsgId) {
		this.shppMsgId = shppMsgId;
	}

	public String getSsgstrSellYn() {
		return ssgstrSellYn;
	}

	public void setSsgstrSellYn(String ssgstrSellYn) {
		this.ssgstrSellYn = ssgstrSellYn;
	}

	public String getVodExtnlPathUrl() {
		return vodExtnlPathUrl;
	}

	public void setVodExtnlPathUrl(String vodExtnlPathUrl) {
		this.vodExtnlPathUrl = vodExtnlPathUrl;
	}

	public String getPalimpItemYn() {
		return palimpItemYn;
	}

	public void setPalimpItemYn(String palimpItemYn) {
		this.palimpItemYn = palimpItemYn;
	}

	public String getItemSellWayCd() {
		return itemSellWayCd;
	}

	public void setItemSellWayCd(String itemSellWayCd) {
		this.itemSellWayCd = itemSellWayCd;
	}

	public String getItemStatTypeCd() {
		return itemStatTypeCd;
	}

	public void setItemStatTypeCd(String itemStatTypeCd) {
		this.itemStatTypeCd = itemStatTypeCd;
	}

	public String getWhinNotiYn() {
		return whinNotiYn;
	}

	public void setWhinNotiYn(String whinNotiYn) {
		this.whinNotiYn = whinNotiYn;
	}

	public FstPriceInfo getFstPriceInfo() {
		return fstPriceInfo;
	}

	public void setFstPriceInfo(FstPriceInfo fstPriceInfo) {
		this.fstPriceInfo = fstPriceInfo;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getTourItemRedirectUrlPc() {
		return tourItemRedirectUrlPc;
	}

	public void setTourItemRedirectUrlPc(String tourItemRedirectUrlPc) {
		this.tourItemRedirectUrlPc = tourItemRedirectUrlPc;
	}

	public String getTourItemRedirectUrlMobile() {
		return tourItemRedirectUrlMobile;
	}

	public void setTourItemRedirectUrlMobile(String tourItemRedirectUrlMobile) {
		this.tourItemRedirectUrlMobile = tourItemRedirectUrlMobile;
	}

	public String getGiftPackPsblYn() {
		return giftPackPsblYn;
	}

	public void setGiftPackPsblYn(String giftPackPsblYn) {
		this.giftPackPsblYn = giftPackPsblYn;
	}

	public String getCtvatInclYn() {
		return ctvatInclYn;
	}

	public void setCtvatInclYn(String ctvatInclYn) {
		this.ctvatInclYn = ctvatInclYn;
	}

	public String getItemTripPropClsId() {
		return itemTripPropClsId;
	}

	public void setItemTripPropClsId(String itemTripPropClsId) {
		this.itemTripPropClsId = itemTripPropClsId;
	}

	public String getItemTripPropClsNm() {
		return itemTripPropClsNm;
	}

	public void setItemTripPropClsNm(String itemTripPropClsNm) {
		this.itemTripPropClsNm = itemTripPropClsNm;
	}

	public List<ItemTripAttr> getItemTripAttrs() {
		return itemTripAttrs;
	}

	public void setItemTripAttrs(List<ItemTripAttr> itemTripAttrs) {
		this.itemTripAttrs = itemTripAttrs;
	}

	public String getLuxprGantItemYn() {
		return luxprGantItemYn;
	}

	public void setLuxprGantItemYn(String luxprGantItemYn) {
		this.luxprGantItemYn = luxprGantItemYn;
	}

	public String getIpotRptNo() {
		return ipotRptNo;
	}

	public void setIpotRptNo(String ipotRptNo) {
		this.ipotRptNo = ipotRptNo;
	}

	public List<ItemShppCritn> getItemShppCritns() {
		return itemShppCritns;
	}

	public void setItemShppCritns(List<ItemShppCritn> itemShppCritns) {
		this.itemShppCritns = itemShppCritns;
	}

	public List<MtlglItem> getMtlglItemInfos() {
		return mtlglItemInfos;
	}

	public void setMtlglItemInfos(List<MtlglItem> mtlglItemInfos) {
		this.mtlglItemInfos = mtlglItemInfos;
	}

	public String getDispStrtDts() {
		return dispStrtDts;
	}

	public void setDispStrtDts(String dispStrtDts) {
		this.dispStrtDts = dispStrtDts;
	}

	public String getDispEndDts() {
		return dispEndDts;
	}

	public void setDispEndDts(String dispEndDts) {
		this.dispEndDts = dispEndDts;
	}

	public List<SellPnt> getSellPnts() {
		return sellPnts;
	}

	public void setSellPnts(List<SellPnt> sellPnts) {
		this.sellPnts = sellPnts;
	}

	public List<UitemPrc> getChgSalesPrcInfos() {
		return chgSalesPrcInfos;
	}

	public void setChgSalesPrcInfos(List<UitemPrc> chgSalesPrcInfos) {
		this.chgSalesPrcInfos = chgSalesPrcInfos;
	}

	public List<UitemPrc> getReturnSalesPrcInfos() {
		return returnSalesPrcInfos;
	}

	public void setReturnSalesPrcInfos(List<UitemPrc> returnSalesPrcInfos) {
		this.returnSalesPrcInfos = returnSalesPrcInfos;
	}

	public List<UitemOptnAddt> getUitemOptnAddts() {
		return uitemOptnAddts;
	}

	public void setUitemOptnAddts(List<UitemOptnAddt> uitemOptnAddts) {
		this.uitemOptnAddts = uitemOptnAddts;
	}

	public List<ItemOrdOptn> getItemOrdOptns() {
		return itemOrdOptns;
	}

	public void setItemOrdOptns(List<ItemOrdOptn> itemOrdOptns) {
		this.itemOrdOptns = itemOrdOptns;
	}

	public List<UitemPrc> getChgUitemPluralPrcs() {
		return chgUitemPluralPrcs;
	}

	public void setChgUitemPluralPrcs(List<UitemPrc> chgUitemPluralPrcs) {
		this.chgUitemPluralPrcs = chgUitemPluralPrcs;
	}

	public List<UitemPrc> getReturnUitemPluralPrcs() {
		return returnUitemPluralPrcs;
	}

	public void setReturnUitemPluralPrcs(List<UitemPrc> returnUitemPluralPrcs) {
		this.returnUitemPluralPrcs = returnUitemPluralPrcs;
	}

	public List<ImgInfo> getQualityViewImgs() {
		return qualityViewImgs;
	}

	public void setQualityViewImgs(List<ImgInfo> qualityViewImgs) {
		this.qualityViewImgs = qualityViewImgs;
	}

	public List<QualityFile> getQualityFiles() {
		return qualityFiles;
	}

	public void setQualityFiles(List<QualityFile> qualityFiles) {
		this.qualityFiles = qualityFiles;
	}

	public List<Prop> getCertificationProps() {
		return certificationProps;
	}

	public void setCertificationProps(List<Prop> certificationProps) {
		this.certificationProps = certificationProps;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public String getPrcMngMthd() {
		return prcMngMthd;
	}

	public void setPrcMngMthd(String prcMngMthd) {
		this.prcMngMthd = prcMngMthd;
	}

	@Override
	public String toString() {
		return "Product [resultCode=" + resultCode + ", resultMessage=" + resultMessage + ", resultDesc=" + resultDesc
				+ ", splVenId=" + splVenId + ", lrnkSplVenId=" + lrnkSplVenId + ", itemId=" + itemId + ", itemNm="
				+ itemNm + ", mdlNm=" + mdlNm + ", brandId=" + brandId + ", stdCtgId=" + stdCtgId + ", sites=" + sites
				+ ", itemAplRngTypeCd=" + itemAplRngTypeCd + ", b2eAplRngCd=" + b2eAplRngCd + ", b2cAplRngCd="
				+ b2cAplRngCd + ", itemChrctDivCd=" + itemChrctDivCd + ", itemChrctDtlCd=" + itemChrctDtlCd
				+ ", exusItemDivCd=" + exusItemDivCd + ", exusItemDtlCd=" + exusItemDtlCd + ", dispAplRngTypeCd="
				+ dispAplRngTypeCd + ", speSalestrNo=" + speSalestrNo + ", sellStatCd=" + sellStatCd
				+ ", itemMngPropClsId=" + itemMngPropClsId + ", itemMngAttrs=" + itemMngAttrs + ", manufcoNm="
				+ manufcoNm + ", prodManufCntryId=" + prodManufCntryId + ", dispCtgs=" + dispCtgs + ", srchPsblYn="
				+ srchPsblYn + ", itemSrchwdNm=" + itemSrchwdNm + ", aplMbrGrdCd=" + aplMbrGrdCd
				+ ", minOnetOrdPsblQty=" + minOnetOrdPsblQty + ", maxOnetOrdPsblQty=" + maxOnetOrdPsblQty
				+ ", max1dyOrdPsblQty=" + max1dyOrdPsblQty + ", adultItemTypeCd=" + adultItemTypeCd + ", hriskItemYn="
				+ hriskItemYn + ", nitmAplYn=" + nitmAplYn + ", sellCapaUnitCd=" + sellCapaUnitCd + ", sellTotCapa="
				+ sellTotCapa + ", sellUnitCapa=" + sellUnitCapa + ", sellUnitQty=" + sellUnitQty + ", buyFrmCd="
				+ buyFrmCd + ", txnDivCd=" + txnDivCd + ", salesPrcInfos=" + salesPrcInfos + ", b2ePrcMngMthdCd="
				+ b2ePrcMngMthdCd + ", b2ePrcAplTgts=" + b2ePrcAplTgts + ", invMngYn=" + invMngYn + ", baseInvQty="
				+ baseInvQty + ", invQtyMarkgYn=" + invQtyMarkgYn + ", rsvSaleInfo=" + rsvSaleInfo + ", itemSellTypeCd="
				+ itemSellTypeCd + ", itemSellTypeDtlCd=" + itemSellTypeDtlCd + ", uitemAttr=" + uitemAttr + ", uitems="
				+ uitems + ", uitemEaitemOptnCac=" + uitemEaitemOptnCac + ", uitemPluralPrcs=" + uitemPluralPrcs
				+ ", shppItemDivCd=" + shppItemDivCd + ", exprtCntryId=" + exprtCntryId + ", retExchPsblYn="
				+ retExchPsblYn + ", shppMainCd=" + shppMainCd + ", shppMthdCd=" + shppMthdCd + ", mareaShppYn="
				+ mareaShppYn + ", jejuShppDisabYn=" + jejuShppDisabYn + ", ismtarShppDisabYn=" + ismtarShppDisabYn
				+ ", shppRqrmDcnt=" + shppRqrmDcnt + ", shppRqrmDcntChngRsnCntt=" + shppRqrmDcntChngRsnCntt
				+ ", splVenItemId=" + splVenItemId + ", whoutShppcstId=" + whoutShppcstId + ", retShppcstId="
				+ retShppcstId + ", ismtarAddShppcstId=" + ismtarAddShppcstId + ", jejuAddShppcstId=" + jejuAddShppcstId
				+ ", whoutAddrId=" + whoutAddrId + ", snbkAddrId=" + snbkAddrId + ", itemTotWgt=" + itemTotWgt
				+ ", hopeShppDdDivCd=" + hopeShppDdDivCd + ", hopeShppDdEndDts=" + hopeShppDdEndDts + ", itemImgs="
				+ itemImgs + ", itemDesc=" + itemDesc + ", sizeDesc=" + sizeDesc + ", purchGuideCntt=" + purchGuideCntt
				+ ", asMemoCntt=" + asMemoCntt + ", chldCert=" + chldCert + ", certInfos=" + certInfos + ", giftPsblYn="
				+ giftPsblYn + ", shppMsgId=" + shppMsgId + ", ssgstrSellYn=" + ssgstrSellYn + ", vodExtnlPathUrl="
				+ vodExtnlPathUrl + ", palimpItemYn=" + palimpItemYn + ", itemSellWayCd=" + itemSellWayCd
				+ ", itemStatTypeCd=" + itemStatTypeCd + ", whinNotiYn=" + whinNotiYn + ", fstPriceInfo=" + fstPriceInfo
				+ ", book=" + book + ", tourItemRedirectUrlPc=" + tourItemRedirectUrlPc + ", tourItemRedirectUrlMobile="
				+ tourItemRedirectUrlMobile + ", giftPackPsblYn=" + giftPackPsblYn + ", ctvatInclYn=" + ctvatInclYn
				+ ", itemTripPropClsId=" + itemTripPropClsId + ", itemTripPropClsNm=" + itemTripPropClsNm
				+ ", itemTripAttrs=" + itemTripAttrs + ", luxprGantItemYn=" + luxprGantItemYn + ", ipotRptNo="
				+ ipotRptNo + ", itemShppCritns=" + itemShppCritns + "]";
	}

}
