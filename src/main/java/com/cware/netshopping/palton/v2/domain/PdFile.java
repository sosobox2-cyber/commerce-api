package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PdFile {

	// 파일유형코드 [공통코드 : FILE_TYP_CD]
	// USD 상품상태
	// TAG_LBL Tag/케어라벨
	// PD 상품
	String fileTypCd;

	// 파일구분코드 [공통코드 : FILE_DVS_CD]
	// USD 상품상태
	// TAG_LBL Tag/케어라벨
	// 3D 상품3D이미지
	// WDTH 상품가로형
	// VDO_FILE 상품동영상_FILE
	// VDO_URL 상품동영상_URL
	String fileDvsCd;

	// 원본파일명(경로명)
	// 파일명을 포함한 다운로드가 가능한 경로를 입력한다.
	// ex) http://abc.com/12/34/56/78_90.mp4
	String origFileNm;

	public String getFileTypCd() {
		return fileTypCd;
	}

	public void setFileTypCd(String fileTypCd) {
		this.fileTypCd = fileTypCd;
	}

	public String getFileDvsCd() {
		return fileDvsCd;
	}

	public void setFileDvsCd(String fileDvsCd) {
		this.fileDvsCd = fileDvsCd;
	}

	public String getOrigFileNm() {
		return origFileNm;
	}

	public void setOrigFileNm(String origFileNm) {
		this.origFileNm = origFileNm;
	}

	@Override
	public String toString() {
		return "PdFile [fileTypCd=" + fileTypCd + ", fileDvsCd=" + fileDvsCd + ", origFileNm=" + origFileNm + "]";
	}

}
