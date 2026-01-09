package com.cware.netshopping.palton.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Product {

	String spdNo; // 판매자상품번호
	String epdNo; // 업체상품번호
	String trGrpCd; // 거래처그룹코드
	String trNo; // 거래처번호
	String lrtrNo; // 하위거래처번호
	String scatNo; // 표준카테고리번호
	List<Dcat> dcatLst; // 전시카테고리목록

	// 판매유형코드 [공통코드 : SL_TYP_CD]
	// GNRL : 일반판매상품
	// CNSL : 상담판매상품
	// GFT : 사은품
	String slTypCd;

	// 상품유형코드 [공통코드 : PD_TYP_CD]
	// GNRL_GNRL : 일반판매_일반상품
	// GNRL_ECPN : 일반판매_e쿠폰상품
	// GNRL_GFTV : 일반판매_상품권
	// GNRL_ZRWON : 일반판매_0원상품
	// CNSL_CNSL : 상담판매_상담상품
	// GFT_GNRL : 사은품_일반상품
	// GFT_ECPN : 사은품_e쿠폰상품
	// GFT_GFTV : 사은품_상품권
	String pdTypCd;

	// 상품권형태코드 [공통코드 : GFTV_SHP_CD]
	// 상품유형구분코드가 GNRL_GFTV(상품권)인 경우에는 필수 입력 모바일상품권의 경우에는 e쿠폰 항목을 입력하여야 한다.
	// PPR : 지류
	// MBL : 모바일
	String gftvShpCd;

	// 판매자상품명
	// 입력된 판매자상품명은 상품명 정제를 거쳐 전시상품명으로 노출된다.
	String spdNm;

	// 상품명(전시상품명)
	String pdNm;

	// 브랜드번호 [속성모듈 제공 항목]
	// 속성모듈 API를 통하여 수신된 브랜드번호를 입력한다.
	String brdNo;

	// 제조사명
	// TXT 값으로 입력한다.
	String mfcrNm;

	// 원산지코드
	// 기타인 경우에는 "상품상세 참조"코드 입력
	String oplcCd;

	// 과면세구분코드 [공통코드 : TDF_DVS_CD]
	// 01 과세
	// 02 면세
	// 03 영세
	// 04 해당없음(상품권)
	String tdfDvsCd;

	// 판매시작일시 [YYYYMMDDHH24MISS ex) 20190801100000]
	String slStrtDttm;

	// 판매종료일시 [YYYYMMDDHH24MISS ex) 20190801100000]
	String slEndDttm;

	PdItmsInfo pdItmsInfo; // 상품품목고시정보
	
	String impPrxCd; // 수입대행코드

	// 안전인증목록
	// 안전인증정보 입력시 하단의 항목을 입력한다.
	List<SftyAthn> sftyAthnLst;

	// 표준카테고리속성목록
	// 표준카테고리에 매핑된 상품속성 입력시 하단의 항목을 입력한다.
	List<ScatAttr> scatAttrLst;

	// 판매상태코드 [공통코드 : SL_STAT_CD]
	// END 판매종료
	// SALE 판매중
	// SOUT 품절
	// STP 판매중지
	String slStatCd;

	// 상품판매상태사유코드 [공통코드 : SL_STAT_RSN_CD]
	// SOUT_STK 재고수량 0 품절처리
	// SOUT_LMT 한정수량 0 품절처리
	// SOUT_ADMR 관리자 수동품절처리
	// SOUT_ITM 모든단품품절로 상품 품절처리
	// SOUT_RSV 예약상품 품절처리
	// STP_TNS TNS(금칙어/신고) 패널티 판매중지
	// STP_CTL 카탈로그에 의한 판매중지
	// STP_MNTR 상품정보 모니터링 부적합 처리
	// STP_DV 배송서비스 패널티 판매중지
	// STP_UNAPRV 상품미승인 상태에 의한 판매중지처리
	// END_TR 거래처계약종료로 인한 판매종료 처리
	// END_ADMR 관리자 수동 판매종료 처리
	// END_TNS TNS(위해상품) 판매종료 처리
	String slStatRsnCd;

	PdAprvStatInfo pdAprvStatInfo; // 상품승인상태정보

	// 입력형옵션목록
	// 최대 5개의 입력형옵션을 설정할 수 있다.
	List<ItypOpt> itypOptLst;

	PurPsbQtyInfo purPsbQtyInfo; // 구매가능수량정보

	// 연령제한코드 [공통코드 : AGE_LMT_CD]
	// 0 전연령 구매가능
	// 15 15세이상 구매가능
	// 19 19세이상 구매가능
	String ageLmtCd;

	// 선물가능여부 [Y, N]
	// 디폴트:N
	String prstPsbYn;

	// 가격비교노출여부 [Y, N]
	// 디폴트:Y
	String prcCmprEpsrYn;

	// 도서문화비 공제여부 [Y, N]
	// 디폴트:N
	// 거래처와 표준카테고리가 모두 도서문화비 공제대상에 해당하는 경우에만 공제여부가 Y이다.
	String bookCultCstDdctYn;

	// ISBN
	// 도서문화비 공제여부가 Y이고 카테고리가 도서관련 카테고리일 경우 ISBN NO를 입력한다
	String isbnCd;

	// 수입사명
	// TXT 입력
	String impCoNm;

	// 수입구분코드 [공통코드 : IMP_DVS_CD]
	// 수입사명이 있는 경우 입력한다.
	// DRC_IMP 직수입
	// PRL_IMP 병행수입
	// NONE 해당없음
	String impDvsCd;

	// 환금성상품여부 [Y, N]
	// 디폴트:N
	// 표준카테고리 속성을 상속 받는다.
	// 환금성 상품으로 설정되는 경우 주문에서 결제수단에 따라 구매가 제한된다.
	String cshbltyPdYn;

	// 폐가전수거여부 [Y, N]
	// 디폴트:N
	String brkHmapPkcpPsbYn;

	// 계약유형코드[공통코드 : CTRT_TYP_CD]
	// A 중개
	// B 위탁
	String ctrtTypCd;

	PdSzInfo pdSzInfo; // 배송사이즈정보

	// 상품상태코드 [공통코드 : PD_STAT_CD]
	// 상품상태코드가 새상품(NEW)이 아닌 경우에는 파일유형코드와 파일구분코드를 USD로 하여 상품상태이미지를 반드시 등록하여야 한다.
	// NEW 새상품
	// DP 전시상품
	// RFRBSH 리퍼상품
	// SCRTC 스크레치상품
	// RTRN_NRML 반품(정상)상품
	// RTRN_DMG 반품(박스훼손)상품
	// USD 중고상품
	String pdStatCd;

	// 전시여부 [Y, N]
	// 디폴트:Y
	String dpYn;

	// 검색키워드목록
	// 5개 이하만 등록 가능
	List<String> scKwdLst;

	// 상품콘텐츠파일목록
	// 상품상태코드가 새상품(NEW)이 아닌 경우에는 파일유형코드와 파일구분코드를 USD로 하여 상품상태이미지를 반드시 등록하여야 한다.
	List<PdFile> pdFileLst;

	List<Epn> epnLst; // 상품설명목록

	// 결제수단예외목록 [공통코드 : PY_MNS_CD]
	// 10 신용카드
	// 11 L.pay신용카드
	// 20 L.pay계좌
	// 21 무통장입금
	// 22 실시간계좌이체
	// 30 휴대폰
	// 40 L-Point
	// 50 모바일상품권
	// 60 카카오페이
	// 61 네이버페이
	List<String> pyMnsExcpLst;

	// 취소가능여부 [Y, N]
	// 취소 불가인 상품인 경우에는 'N'으로 설정
	// 디폴트:Y
	String cnclPsbYn;

	// 즉시취소가능여부 [Y, N]
	// 특정 시점(출고 등)까지는 문의 없이 바로 취소 가능한 경우 "Y"로 설정
	// 디플트: Y
	String immdCnclPsbYn;

	// 국내해외배송구분코드 [공통코드 : DMST_OVS_DV_DVS_CD]
	// 디폴트:국내배송
	// DMST 국내배송
	// OVS 해외발송
	// RVRS_DPUR 역직구
	String dmstOvsDvDvsCd;

	String pstkYn; // 선재고여부 [Y, N] 디폴트:N

	// 배송처리유형코드 [공통코드 : DV_PROC_TYP_CD]
	// LO_CNTR e커머스 센터배송
	// LO_ENTP e커머스 업체배송
	String dvProcTypCd;

	// 배송상품유형코드 [공통코드 : DV_PD_TYP_CD]
	// TDY_SND 오늘발송 0
	// GNRL 일반상품 3
	// OD_MFG 주문제작상품 15
	// FREE_INST 무료설치상품 3
	// CHRG_INST 유료설치상품 3
	// PRMM_INST 프리미엄설치상품 365
	// ECPN e쿠폰 0
	// GFTV 상품권 3
	// OVS 해외배송 15
	String dvPdTypCd;

	// 발송예정일수
	// 배송상품유형코드에 따라 최대 발송예정일수를 입력한다.
	Integer sndBgtNday;

	SndBgtDdInfo sndBgtDdInfo; // 발송예정일정보

	// 배송권역그룹코드
	// 배송모듈을 통하여 관리되는 코드를 입력한다.
	String dvRgsprGrpCd;

	// 배송수단코드 [공통코드 : DV_MNS_CD]
	// 단건만 입력가능
	// DGNN_DV 전담배송(직접배송)
	// DPCL 택배
	// NONE_DV 무배송
	// REG_MAIL 등기
	// ZIP 우편
	String dvMnsCd;

	// 출고지번호
	// 거래처 API "(일반 Seller용) 판매자 출고지/반품지 등록"을 통하여 등록된 출고지번호를 입력한다.
	String owhpNo;

	// 택배사코드 [공통코드 : DV_CO_CD]
	// 0001 롯데택배
	// 0003 현대택배
	// 0004 우체국택배
	// 0005 로젠택배
	// 0006 한진택배
	// 0014 GTX 로지스 택배
	// 0016 KGB택배
	// 0023 건영택배
	// 0024 경동택배
	// 0025 고려택배
	// 0027 대신정기화물택배
	// 0028 대신택배
	// 0031 드림택배
	// 0037 엘로우캡택배
	// 0040 이노지스택배
	// 0042 일양택배
	// 0043 천일택배
	// 0044 편의점택배
	// 0046 하나로택배
	// 0048 한의사랑택배
	// 0049 합동택배
	// 0050 호남택배
	// 9999 기타택배
	String hdcCd;

	String dvCstPolNo; // 배송비정책번호
	String adtnDvCstPolNo; // 추가배송비정책번호

	// 합배송가능여부 [Y, N]
	// 디폴트:Y
	String cmbnDvPsbYn;

	// 배송비기준수량
	// 디폴트:0
	Integer dvCstStdQty;

	// 퀵배송사용여부 [Y, N]
	// 디폴트:N
	String qckDvUseYn;

	// 당일배송가능여부 [Y, N]
	// 디폴트:N
	String crdayDvPsbYn;

	// 당일배송정보
	// 당일배송가능여부가 Y인 경우 필수값
	CrdayDvInfo crdayDvInfo;

	// 스마트픽사용여부 [Y, N]
	// 디폴트:N
	String spicUseYn;

	// 스마트픽정보
	// 스마트픽사용여부 Y인 경우 필수
	SpicInfo spicInfo;

	// 희망일배송가능여부 [Y, N]
	// 디폴트:N
	String hpDdDvPsbYn;

	// 희망일배송가능기간
	// 희망일배송가능여부 Y인 경우 필수
	Integer hpDdDvPsbPrd;

	// 저장유형코드 [공통코드 : SAVE_TYP_CD]
	// 디폴트:해당없음
	// RFRG 냉장
	// FRZN 냉동
	// FRSH 신선
	// NONE 해당없음
	String saveTypCd;

	// 반품가능여부 [Y, N]
	// 디폴트:Y
	String rtngPsbYn;

	// 교환가능여부 [Y, N]
	// 디폴트:Y
	String xchgPsbYn;

	// 맞교환가능여부 [Y, N]
	// 디폴트:N
	String echgPsbYn;

	// 합반품가능여부 [Y, N]
	// 합배송가능여부가 Y인 경우 Y, N 선택 가능. N인 경우 N만 선택 가능
	String cmbnRtngPsbYn;

	// 반품택배사코드 [공통코드 : DV_CO_CD]
	String rtngHdcCd;

	// 반품회수가능여부 [Y, N]
	// 디폴트:Y
	String rtngRtrvPsbYn;

	// 회수지번호
	// 거래처 API "(일반 Seller용) 판매자 출고지/반품지 등록"을 통하여 등록된 회수지번호를 입력한다.
	String rtrpNo;

	// 회수유형코드
	// 계약유형이 중개일 경우에만 해당.
	// ENTP_RTRV 업체회수
	// DGNN_RTRV 자동회수
	String rtrvTypCd;

	// e쿠폰정보
	// 해당 상품이 e쿠폰인 경우에만 입력한다.
	EcpnInfo ecpnInfo;

	// 렌탈상품정보
	// 상품유형이 렌탈일 경우 필수값
	RntlPdInfo rntlPdInfo;

	// 개통형상품정보
	// 상품유형구분코드가 일반판매_0원상품(GNRL_ZRWON)에 해당하는 개통형상품인 경우 필수입력한다.
	OpngPdInfo opngPdInfo;

	// 재고관리여부 [Y, N]
	// 'N'인 경우 재고가 999,999,999로 들어간다. 웹재고를 관리하지 않는다.
	String stkMgtYn;

	// 판매자단품여부 [Y, N]
	// Y이면 단품속성목록을 설정해야 한다.
	// N이면 단품속성목록을 설정 안한다. 옵션이 없는 단품 한가지로 설정된다.
	String sitmYn;

	String thdyPdYn; // 명절상품여부 [Y, N]
	List<Itm> itmLst; // 단품목록

	// 셀러추천상품목록
	// 최대 10개까지 등록 가능하다.
	List<SlrRcPd> slrRcPdLst;

	// 추가상품사용여부 [Y, N]
	// 디폴트:N
	String adtnPdYn;

	AdtnPdInfo adtnPdInfo; // 추가상품정보
	
	String dlcrtYn;//딜크릿

	public String getSpdNo() {
		return spdNo;
	}

	public void setSpdNo(String spdNo) {
		this.spdNo = spdNo;
	}

	public String getEpdNo() {
		return epdNo;
	}

	public void setEpdNo(String epdNo) {
		this.epdNo = epdNo;
	}

	public String getTrGrpCd() {
		return trGrpCd;
	}

	public void setTrGrpCd(String trGrpCd) {
		this.trGrpCd = trGrpCd;
	}

	public String getTrNo() {
		return trNo;
	}

	public void setTrNo(String trNo) {
		this.trNo = trNo;
	}

	public String getLrtrNo() {
		return lrtrNo;
	}

	public void setLrtrNo(String lrtrNo) {
		this.lrtrNo = lrtrNo;
	}

	public String getScatNo() {
		return scatNo;
	}

	public void setScatNo(String scatNo) {
		this.scatNo = scatNo;
	}

	public List<Dcat> getDcatLst() {
		return dcatLst;
	}

	public void setDcatLst(List<Dcat> dcatLst) {
		this.dcatLst = dcatLst;
	}

	public String getSlTypCd() {
		return slTypCd;
	}

	public void setSlTypCd(String slTypCd) {
		this.slTypCd = slTypCd;
	}

	public String getPdTypCd() {
		return pdTypCd;
	}

	public void setPdTypCd(String pdTypCd) {
		this.pdTypCd = pdTypCd;
	}

	public String getGftvShpCd() {
		return gftvShpCd;
	}

	public void setGftvShpCd(String gftvShpCd) {
		this.gftvShpCd = gftvShpCd;
	}

	public String getSpdNm() {
		return spdNm;
	}

	public void setSpdNm(String spdNm) {
		this.spdNm = spdNm;
	}

	public String getPdNm() {
		return pdNm;
	}

	public void setPdNm(String pdNm) {
		this.pdNm = pdNm;
	}

	public String getBrdNo() {
		return brdNo;
	}

	public void setBrdNo(String brdNo) {
		this.brdNo = brdNo;
	}

	public String getMfcrNm() {
		return mfcrNm;
	}

	public void setMfcrNm(String mfcrNm) {
		this.mfcrNm = mfcrNm;
	}

	public String getSlStrtDttm() {
		return slStrtDttm;
	}

	public void setSlStrtDttm(String slStrtDttm) {
		this.slStrtDttm = slStrtDttm;
	}

	public String getSlEndDttm() {
		return slEndDttm;
	}

	public void setSlEndDttm(String slEndDttm) {
		this.slEndDttm = slEndDttm;
	}

	public PdItmsInfo getPdItmsInfo() {
		return pdItmsInfo;
	}

	public void setPdItmsInfo(PdItmsInfo pdItmsInfo) {
		this.pdItmsInfo = pdItmsInfo;
	}

	public List<SftyAthn> getSftyAthnLst() {
		return sftyAthnLst;
	}

	public void setSftyAthnLst(List<SftyAthn> sftyAthnLst) {
		this.sftyAthnLst = sftyAthnLst;
	}

	public List<ScatAttr> getScatAttrLst() {
		return scatAttrLst;
	}

	public void setScatAttrLst(List<ScatAttr> scatAttrLst) {
		this.scatAttrLst = scatAttrLst;
	}

	public String getSlStatCd() {
		return slStatCd;
	}

	public void setSlStatCd(String slStatCd) {
		this.slStatCd = slStatCd;
	}

	public String getSlStatRsnCd() {
		return slStatRsnCd;
	}

	public void setSlStatRsnCd(String slStatRsnCd) {
		this.slStatRsnCd = slStatRsnCd;
	}

	public PdAprvStatInfo getPdAprvStatInfo() {
		return pdAprvStatInfo;
	}

	public void setPdAprvStatInfo(PdAprvStatInfo pdAprvStatInfo) {
		this.pdAprvStatInfo = pdAprvStatInfo;
	}

	public List<ItypOpt> getItypOptLst() {
		return itypOptLst;
	}

	public void setItypOptLst(List<ItypOpt> itypOptLst) {
		this.itypOptLst = itypOptLst;
	}

	public PurPsbQtyInfo getPurPsbQtyInfo() {
		return purPsbQtyInfo;
	}

	public void setPurPsbQtyInfo(PurPsbQtyInfo purPsbQtyInfo) {
		this.purPsbQtyInfo = purPsbQtyInfo;
	}

	public String getAgeLmtCd() {
		return ageLmtCd;
	}

	public void setAgeLmtCd(String ageLmtCd) {
		this.ageLmtCd = ageLmtCd;
	}

	public String getPrstPsbYn() {
		return prstPsbYn;
	}

	public void setPrstPsbYn(String prstPsbYn) {
		this.prstPsbYn = prstPsbYn;
	}

	public String getPrcCmprEpsrYn() {
		return prcCmprEpsrYn;
	}

	public void setPrcCmprEpsrYn(String prcCmprEpsrYn) {
		this.prcCmprEpsrYn = prcCmprEpsrYn;
	}

	public String getBookCultCstDdctYn() {
		return bookCultCstDdctYn;
	}

	public void setBookCultCstDdctYn(String bookCultCstDdctYn) {
		this.bookCultCstDdctYn = bookCultCstDdctYn;
	}

	public String getIsbnCd() {
		return isbnCd;
	}

	public void setIsbnCd(String isbnCd) {
		this.isbnCd = isbnCd;
	}

	public String getImpCoNm() {
		return impCoNm;
	}

	public void setImpCoNm(String impCoNm) {
		this.impCoNm = impCoNm;
	}

	public String getImpDvsCd() {
		return impDvsCd;
	}

	public void setImpDvsCd(String impDvsCd) {
		this.impDvsCd = impDvsCd;
	}

	public String getCshbltyPdYn() {
		return cshbltyPdYn;
	}

	public void setCshbltyPdYn(String cshbltyPdYn) {
		this.cshbltyPdYn = cshbltyPdYn;
	}

	public String getBrkHmapPkcpPsbYn() {
		return brkHmapPkcpPsbYn;
	}

	public void setBrkHmapPkcpPsbYn(String brkHmapPkcpPsbYn) {
		this.brkHmapPkcpPsbYn = brkHmapPkcpPsbYn;
	}

	public String getCtrtTypCd() {
		return ctrtTypCd;
	}

	public void setCtrtTypCd(String ctrtTypCd) {
		this.ctrtTypCd = ctrtTypCd;
	}

	public PdSzInfo getPdSzInfo() {
		return pdSzInfo;
	}

	public void setPdSzInfo(PdSzInfo pdSzInfo) {
		this.pdSzInfo = pdSzInfo;
	}

	public String getPdStatCd() {
		return pdStatCd;
	}

	public void setPdStatCd(String pdStatCd) {
		this.pdStatCd = pdStatCd;
	}

	public String getDpYn() {
		return dpYn;
	}

	public void setDpYn(String dpYn) {
		this.dpYn = dpYn;
	}

	public List<String> getScKwdLst() {
		return scKwdLst;
	}

	public void setScKwdLst(List<String> scKwdLst) {
		this.scKwdLst = scKwdLst;
	}

	public List<PdFile> getPdFileLst() {
		return pdFileLst;
	}

	public void setPdFileLst(List<PdFile> pdFileLst) {
		this.pdFileLst = pdFileLst;
	}

	public List<Epn> getEpnLst() {
		return epnLst;
	}

	public void setEpnLst(List<Epn> epnLst) {
		this.epnLst = epnLst;
	}

	public List<String> getPyMnsExcpLst() {
		return pyMnsExcpLst;
	}

	public void setPyMnsExcpLst(List<String> pyMnsExcpLst) {
		this.pyMnsExcpLst = pyMnsExcpLst;
	}

	public String getCnclPsbYn() {
		return cnclPsbYn;
	}

	public void setCnclPsbYn(String cnclPsbYn) {
		this.cnclPsbYn = cnclPsbYn;
	}

	public String getImmdCnclPsbYn() {
		return immdCnclPsbYn;
	}

	public void setImmdCnclPsbYn(String immdCnclPsbYn) {
		this.immdCnclPsbYn = immdCnclPsbYn;
	}

	public String getDmstOvsDvDvsCd() {
		return dmstOvsDvDvsCd;
	}

	public void setDmstOvsDvDvsCd(String dmstOvsDvDvsCd) {
		this.dmstOvsDvDvsCd = dmstOvsDvDvsCd;
	}

	public String getPstkYn() {
		return pstkYn;
	}

	public void setPstkYn(String pstkYn) {
		this.pstkYn = pstkYn;
	}

	public String getDvProcTypCd() {
		return dvProcTypCd;
	}

	public void setDvProcTypCd(String dvProcTypCd) {
		this.dvProcTypCd = dvProcTypCd;
	}

	public String getDvPdTypCd() {
		return dvPdTypCd;
	}

	public void setDvPdTypCd(String dvPdTypCd) {
		this.dvPdTypCd = dvPdTypCd;
	}

	public Integer getSndBgtNday() {
		return sndBgtNday;
	}

	public void setSndBgtNday(Integer sndBgtNday) {
		this.sndBgtNday = sndBgtNday;
	}

	public SndBgtDdInfo getSndBgtDdInfo() {
		return sndBgtDdInfo;
	}

	public void setSndBgtDdInfo(SndBgtDdInfo sndBgtDdInfo) {
		this.sndBgtDdInfo = sndBgtDdInfo;
	}

	public String getDvRgsprGrpCd() {
		return dvRgsprGrpCd;
	}

	public void setDvRgsprGrpCd(String dvRgsprGrpCd) {
		this.dvRgsprGrpCd = dvRgsprGrpCd;
	}

	public String getDvMnsCd() {
		return dvMnsCd;
	}

	public void setDvMnsCd(String dvMnsCd) {
		this.dvMnsCd = dvMnsCd;
	}

	public String getOwhpNo() {
		return owhpNo;
	}

	public void setOwhpNo(String owhpNo) {
		this.owhpNo = owhpNo;
	}

	public String getHdcCd() {
		return hdcCd;
	}

	public void setHdcCd(String hdcCd) {
		this.hdcCd = hdcCd;
	}

	public String getDvCstPolNo() {
		return dvCstPolNo;
	}

	public void setDvCstPolNo(String dvCstPolNo) {
		this.dvCstPolNo = dvCstPolNo;
	}

	public String getAdtnDvCstPolNo() {
		return adtnDvCstPolNo;
	}

	public void setAdtnDvCstPolNo(String adtnDvCstPolNo) {
		this.adtnDvCstPolNo = adtnDvCstPolNo;
	}

	public String getCmbnDvPsbYn() {
		return cmbnDvPsbYn;
	}

	public void setCmbnDvPsbYn(String cmbnDvPsbYn) {
		this.cmbnDvPsbYn = cmbnDvPsbYn;
	}

	public Integer getDvCstStdQty() {
		return dvCstStdQty;
	}

	public void setDvCstStdQty(Integer dvCstStdQty) {
		this.dvCstStdQty = dvCstStdQty;
	}

	public String getQckDvUseYn() {
		return qckDvUseYn;
	}

	public void setQckDvUseYn(String qckDvUseYn) {
		this.qckDvUseYn = qckDvUseYn;
	}

	public String getCrdayDvPsbYn() {
		return crdayDvPsbYn;
	}

	public void setCrdayDvPsbYn(String crdayDvPsbYn) {
		this.crdayDvPsbYn = crdayDvPsbYn;
	}

	public CrdayDvInfo getCrdayDvInfo() {
		return crdayDvInfo;
	}

	public void setCrdayDvInfo(CrdayDvInfo crdayDvInfo) {
		this.crdayDvInfo = crdayDvInfo;
	}

	public String getSpicUseYn() {
		return spicUseYn;
	}

	public void setSpicUseYn(String spicUseYn) {
		this.spicUseYn = spicUseYn;
	}

	public SpicInfo getSpicInfo() {
		return spicInfo;
	}

	public void setSpicInfo(SpicInfo spicInfo) {
		this.spicInfo = spicInfo;
	}

	public String getHpDdDvPsbYn() {
		return hpDdDvPsbYn;
	}

	public void setHpDdDvPsbYn(String hpDdDvPsbYn) {
		this.hpDdDvPsbYn = hpDdDvPsbYn;
	}

	public Integer getHpDdDvPsbPrd() {
		return hpDdDvPsbPrd;
	}

	public void setHpDdDvPsbPrd(Integer hpDdDvPsbPrd) {
		this.hpDdDvPsbPrd = hpDdDvPsbPrd;
	}

	public String getSaveTypCd() {
		return saveTypCd;
	}

	public void setSaveTypCd(String saveTypCd) {
		this.saveTypCd = saveTypCd;
	}

	public String getRtngPsbYn() {
		return rtngPsbYn;
	}

	public void setRtngPsbYn(String rtngPsbYn) {
		this.rtngPsbYn = rtngPsbYn;
	}

	public String getXchgPsbYn() {
		return xchgPsbYn;
	}

	public void setXchgPsbYn(String xchgPsbYn) {
		this.xchgPsbYn = xchgPsbYn;
	}

	public String getEchgPsbYn() {
		return echgPsbYn;
	}

	public void setEchgPsbYn(String echgPsbYn) {
		this.echgPsbYn = echgPsbYn;
	}

	public String getCmbnRtngPsbYn() {
		return cmbnRtngPsbYn;
	}

	public void setCmbnRtngPsbYn(String cmbnRtngPsbYn) {
		this.cmbnRtngPsbYn = cmbnRtngPsbYn;
	}

	public String getRtngHdcCd() {
		return rtngHdcCd;
	}

	public void setRtngHdcCd(String rtngHdcCd) {
		this.rtngHdcCd = rtngHdcCd;
	}

	public String getRtngRtrvPsbYn() {
		return rtngRtrvPsbYn;
	}

	public void setRtngRtrvPsbYn(String rtngRtrvPsbYn) {
		this.rtngRtrvPsbYn = rtngRtrvPsbYn;
	}

	public String getRtrpNo() {
		return rtrpNo;
	}

	public void setRtrpNo(String rtrpNo) {
		this.rtrpNo = rtrpNo;
	}

	public String getRtrvTypCd() {
		return rtrvTypCd;
	}

	public void setRtrvTypCd(String rtrvTypCd) {
		this.rtrvTypCd = rtrvTypCd;
	}

	public EcpnInfo getEcpnInfo() {
		return ecpnInfo;
	}

	public void setEcpnInfo(EcpnInfo ecpnInfo) {
		this.ecpnInfo = ecpnInfo;
	}

	public RntlPdInfo getRntlPdInfo() {
		return rntlPdInfo;
	}

	public void setRntlPdInfo(RntlPdInfo rntlPdInfo) {
		this.rntlPdInfo = rntlPdInfo;
	}

	public OpngPdInfo getOpngPdInfo() {
		return opngPdInfo;
	}

	public void setOpngPdInfo(OpngPdInfo opngPdInfo) {
		this.opngPdInfo = opngPdInfo;
	}

	public String getStkMgtYn() {
		return stkMgtYn;
	}

	public void setStkMgtYn(String stkMgtYn) {
		this.stkMgtYn = stkMgtYn;
	}

	public String getSitmYn() {
		return sitmYn;
	}

	public void setSitmYn(String sitmYn) {
		this.sitmYn = sitmYn;
	}

	public String getThdyPdYn() {
		return thdyPdYn;
	}

	public void setThdyPdYn(String thdyPdYn) {
		this.thdyPdYn = thdyPdYn;
	}

	public List<Itm> getItmLst() {
		return itmLst;
	}

	public void setItmLst(List<Itm> itmLst) {
		this.itmLst = itmLst;
	}

	public List<SlrRcPd> getSlrRcPdLst() {
		return slrRcPdLst;
	}

	public void setSlrRcPdLst(List<SlrRcPd> slrRcPdLst) {
		this.slrRcPdLst = slrRcPdLst;
	}

	public String getAdtnPdYn() {
		return adtnPdYn;
	}

	public void setAdtnPdYn(String adtnPdYn) {
		this.adtnPdYn = adtnPdYn;
	}

	public AdtnPdInfo getAdtnPdInfo() {
		return adtnPdInfo;
	}

	public void setAdtnPdInfo(AdtnPdInfo adtnPdInfo) {
		this.adtnPdInfo = adtnPdInfo;
	}

	public String getOplcCd() {
		return oplcCd;
	}

	public void setOplcCd(String oplcCd) {
		this.oplcCd = oplcCd;
	}

	public String getTdfDvsCd() {
		return tdfDvsCd;
	}

	public void setTdfDvsCd(String tdfDvsCd) {
		this.tdfDvsCd = tdfDvsCd;
	}
	
	public String getImpPrxCd() {
		return impPrxCd;
	}

	public void setImpPrxCd(String impPrxCd) {
		this.impPrxCd = impPrxCd;
	}
	
	public String getDlcrtYn() {
		return dlcrtYn;
	}

	public void setDlcrtYn(String dlcrtYn) {
		this.dlcrtYn = dlcrtYn;
	}

	@Override
	public String toString() {
		return "Product [spdNo=" + spdNo + ", epdNo=" + epdNo + ", trGrpCd=" + trGrpCd + ", trNo=" + trNo + ", lrtrNo="
				+ lrtrNo + ", scatNo=" + scatNo + ", dcatLst=" + dcatLst + ", slTypCd=" + slTypCd + ", pdTypCd="
				+ pdTypCd + ", gftvShpCd=" + gftvShpCd + ", spdNm=" + spdNm + ", pdNm=" + pdNm + ", brdNo=" + brdNo
				+ ", mfcrNm=" + mfcrNm + ", slStrtDttm=" + slStrtDttm + ", slEndDttm=" + slEndDttm + ", pdItmsInfo="
				+ pdItmsInfo + ", sftyAthnLst=" + sftyAthnLst + ", scatAttrLst=" + scatAttrLst + ", slStatCd="
				+ slStatCd + ", slStatRsnCd=" + slStatRsnCd + ", pdAprvStatInfo=" + pdAprvStatInfo + ", itypOptLst="
				+ itypOptLst + ", purPsbQtyInfo=" + purPsbQtyInfo + ", ageLmtCd=" + ageLmtCd + ", prstPsbYn="
				+ prstPsbYn + ", prcCmprEpsrYn=" + prcCmprEpsrYn + ", bookCultCstDdctYn=" + bookCultCstDdctYn
				+ ", isbnCd=" + isbnCd + ", impCoNm=" + impCoNm + ", impDvsCd=" + impDvsCd + ", cshbltyPdYn="
				+ cshbltyPdYn + ", brkHmapPkcpPsbYn=" + brkHmapPkcpPsbYn + ", ctrtTypCd=" + ctrtTypCd + ", pdSzInfo="
				+ pdSzInfo + ", pdStatCd=" + pdStatCd + ", dpYn=" + dpYn + ", scKwdLst=" + scKwdLst + ", pdFileLst="
				+ pdFileLst + ", epnLst=" + epnLst + ", pyMnsExcpLst=" + pyMnsExcpLst + ", cnclPsbYn=" + cnclPsbYn
				+ ", immdCnclPsbYn=" + immdCnclPsbYn + ", dmstOvsDvDvsCd=" + dmstOvsDvDvsCd + ", pstkYn=" + pstkYn
				+ ", dvProcTypCd=" + dvProcTypCd + ", dvPdTypCd=" + dvPdTypCd + ", sndBgtNday=" + sndBgtNday
				+ ", sndBgtDdInfo=" + sndBgtDdInfo + ", dvRgsprGrpCd=" + dvRgsprGrpCd + ", dvMnsCd=" + dvMnsCd
				+ ", owhpNo=" + owhpNo + ", hdcCd=" + hdcCd + ", dvCstPolNo=" + dvCstPolNo + ", adtnDvCstPolNo="
				+ adtnDvCstPolNo + ", cmbnDvPsbYn=" + cmbnDvPsbYn + ", dvCstStdQty=" + dvCstStdQty + ", qckDvUseYn="
				+ qckDvUseYn + ", crdayDvPsbYn=" + crdayDvPsbYn + ", crdayDvInfo=" + crdayDvInfo + ", spicUseYn="
				+ spicUseYn + ", spicInfo=" + spicInfo + ", hpDdDvPsbYn=" + hpDdDvPsbYn + ", hpDdDvPsbPrd="
				+ hpDdDvPsbPrd + ", saveTypCd=" + saveTypCd + ", rtngPsbYn=" + rtngPsbYn + ", xchgPsbYn=" + xchgPsbYn
				+ ", echgPsbYn=" + echgPsbYn + ", cmbnRtngPsbYn=" + cmbnRtngPsbYn + ", rtngHdcCd=" + rtngHdcCd
				+ ", rtngRtrvPsbYn=" + rtngRtrvPsbYn + ", rtrpNo=" + rtrpNo + ", rtrvTypCd=" + rtrvTypCd + ", ecpnInfo="
				+ ecpnInfo + ", rntlPdInfo=" + rntlPdInfo + ", opngPdInfo=" + opngPdInfo + ", stkMgtYn=" + stkMgtYn
				+ ", sitmYn=" + sitmYn + ", thdyPdYn=" + thdyPdYn + ", itmLst=" + itmLst + ", slrRcPdLst=" + slrRcPdLst
				+ ", adtnPdYn=" + adtnPdYn + ", adtnPdInfo=" + adtnPdInfo +  ", impPrxCd="+ impPrxCd + ", dlcrtYn=" + dlcrtYn + " ]";
	}

}
