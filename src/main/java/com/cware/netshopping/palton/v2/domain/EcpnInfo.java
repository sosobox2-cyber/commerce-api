package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class EcpnInfo {
	// 현금영수증발행여부 [Y, N]
	// 디폴트 : N
	String csrcPblsYn;

	// 사용여부 확인방식코드 [공통코드 : USE_YN_CFM_WAY_CD]
	// ENTP_CFM 업체확인필요
	// ST_INQ 시스템조회가능
	String useYnCfmWayCd;

	// e쿠폰정산유형코드 [공통코드 : ECPN_SE_TYP_CD]
	// ISN 발행분
	// USE 사용분
	String ecpnSeTypCd;

	Long fbilPrc; // 권면가

	// e쿠폰유효기간구분코드 [공통코드 : ECPN_VLD_PRD_DVS_CD]
	// ISS_STD 발급일기준
	// PRD_DGNN 기간지정
	String ecpnVldPrdDvsCd;

	// 유효기간시작일시 [YYYYMMDDHH24MISS ex) 20190801100000]
	String vldPrdStrtDttm;

	// 유효기간종료일시 [YYYYMMDDHH24MISS ex) 20190801100000]
	String vldPrdEndDttm;

	String vldPrdUseStrtTm; // 유효기간사용시작시간
	String vldPrdUseEndTm; // 유효기간사용종료시간

	// 유효기간사용요일
	// 해당요일을 한번만 표기
	// ex) 사용요일 월화수목 2,3,4,5
	String vldPrdUseWkdy;

	// 반품가능구분코드 [공통코드 : RTNG_PSB_DVS_CD]
	// WTHN_PRD 사용기간내
	// EXCD 주문일+7일
	// ELPS__PRD 사용기간경과
	String rtngPsbDvsCd;

	// 환불가능여부 [Y, N]
	// 디폴트 : Y
	String rfndPsbYn;

	// e쿠폰환불유형코드 [공통코드 : ECPN_RFND_TYP_CD]
	// 90 일반_고객, 공통코드 추가속성 - 환불정산율(90%)
	// 100 일반_전액, 공통코드 추가속성 - 환불정산율(100%)
	// 70 특가_고객, 공통코드 추가속성 - 환불정산율(70%)
	// 50 특가_50%, 공통코드 추가속성 - 환불정산율(50%)
	String ecpnRfndTypCd;

	String autoRfndYn; // 자동환불여부 [Y, N]

	// 발급방식코드 [공통코드 : ISS_WAY_CD]
	// CRT 자체생성형
	// UPLD 업로드형
	// LNK 외부연동형
	// TR_SND 거래처발송형
	String issWayCd;

	// 발송방식코드 [공통코드 : SND_WAY_CD]
	// LNK 외부연동형
	// AUTO 자동형
	// TR_SND 거래처발송형
	String sndWayCd;

	String useLmtEpn; // 사용제한설명
	String atntMatrEpn; // 주의사항설명
	String usePlcEpn; // 사용장소설명

	public String getCsrcPblsYn() {
		return csrcPblsYn;
	}

	public void setCsrcPblsYn(String csrcPblsYn) {
		this.csrcPblsYn = csrcPblsYn;
	}

	public String getUseYnCfmWayCd() {
		return useYnCfmWayCd;
	}

	public void setUseYnCfmWayCd(String useYnCfmWayCd) {
		this.useYnCfmWayCd = useYnCfmWayCd;
	}

	public String getEcpnSeTypCd() {
		return ecpnSeTypCd;
	}

	public void setEcpnSeTypCd(String ecpnSeTypCd) {
		this.ecpnSeTypCd = ecpnSeTypCd;
	}

	public Long getFbilPrc() {
		return fbilPrc;
	}

	public void setFbilPrc(Long fbilPrc) {
		this.fbilPrc = fbilPrc;
	}

	public String getEcpnVldPrdDvsCd() {
		return ecpnVldPrdDvsCd;
	}

	public void setEcpnVldPrdDvsCd(String ecpnVldPrdDvsCd) {
		this.ecpnVldPrdDvsCd = ecpnVldPrdDvsCd;
	}

	public String getVldPrdStrtDttm() {
		return vldPrdStrtDttm;
	}

	public void setVldPrdStrtDttm(String vldPrdStrtDttm) {
		this.vldPrdStrtDttm = vldPrdStrtDttm;
	}

	public String getVldPrdEndDttm() {
		return vldPrdEndDttm;
	}

	public void setVldPrdEndDttm(String vldPrdEndDttm) {
		this.vldPrdEndDttm = vldPrdEndDttm;
	}

	public String getVldPrdUseStrtTm() {
		return vldPrdUseStrtTm;
	}

	public void setVldPrdUseStrtTm(String vldPrdUseStrtTm) {
		this.vldPrdUseStrtTm = vldPrdUseStrtTm;
	}

	public String getVldPrdUseEndTm() {
		return vldPrdUseEndTm;
	}

	public void setVldPrdUseEndTm(String vldPrdUseEndTm) {
		this.vldPrdUseEndTm = vldPrdUseEndTm;
	}

	public String getVldPrdUseWkdy() {
		return vldPrdUseWkdy;
	}

	public void setVldPrdUseWkdy(String vldPrdUseWkdy) {
		this.vldPrdUseWkdy = vldPrdUseWkdy;
	}

	public String getRtngPsbDvsCd() {
		return rtngPsbDvsCd;
	}

	public void setRtngPsbDvsCd(String rtngPsbDvsCd) {
		this.rtngPsbDvsCd = rtngPsbDvsCd;
	}

	public String getRfndPsbYn() {
		return rfndPsbYn;
	}

	public void setRfndPsbYn(String rfndPsbYn) {
		this.rfndPsbYn = rfndPsbYn;
	}

	public String getEcpnRfndTypCd() {
		return ecpnRfndTypCd;
	}

	public void setEcpnRfndTypCd(String ecpnRfndTypCd) {
		this.ecpnRfndTypCd = ecpnRfndTypCd;
	}

	public String getAutoRfndYn() {
		return autoRfndYn;
	}

	public void setAutoRfndYn(String autoRfndYn) {
		this.autoRfndYn = autoRfndYn;
	}

	public String getIssWayCd() {
		return issWayCd;
	}

	public void setIssWayCd(String issWayCd) {
		this.issWayCd = issWayCd;
	}

	public String getSndWayCd() {
		return sndWayCd;
	}

	public void setSndWayCd(String sndWayCd) {
		this.sndWayCd = sndWayCd;
	}

	public String getUseLmtEpn() {
		return useLmtEpn;
	}

	public void setUseLmtEpn(String useLmtEpn) {
		this.useLmtEpn = useLmtEpn;
	}

	public String getAtntMatrEpn() {
		return atntMatrEpn;
	}

	public void setAtntMatrEpn(String atntMatrEpn) {
		this.atntMatrEpn = atntMatrEpn;
	}

	public String getUsePlcEpn() {
		return usePlcEpn;
	}

	public void setUsePlcEpn(String usePlcEpn) {
		this.usePlcEpn = usePlcEpn;
	}

	@Override
	public String toString() {
		return "EcpnInfo [csrcPblsYn=" + csrcPblsYn + ", useYnCfmWayCd=" + useYnCfmWayCd + ", ecpnSeTypCd="
				+ ecpnSeTypCd + ", fbilPrc=" + fbilPrc + ", ecpnVldPrdDvsCd=" + ecpnVldPrdDvsCd + ", vldPrdStrtDttm="
				+ vldPrdStrtDttm + ", vldPrdEndDttm=" + vldPrdEndDttm + ", vldPrdUseStrtTm=" + vldPrdUseStrtTm
				+ ", vldPrdUseEndTm=" + vldPrdUseEndTm + ", vldPrdUseWkdy=" + vldPrdUseWkdy + ", rtngPsbDvsCd="
				+ rtngPsbDvsCd + ", rfndPsbYn=" + rfndPsbYn + ", ecpnRfndTypCd=" + ecpnRfndTypCd + ", autoRfndYn="
				+ autoRfndYn + ", issWayCd=" + issWayCd + ", sndWayCd=" + sndWayCd + ", useLmtEpn=" + useLmtEpn
				+ ", atntMatrEpn=" + atntMatrEpn + ", usePlcEpn=" + usePlcEpn + "]";
	}

}
