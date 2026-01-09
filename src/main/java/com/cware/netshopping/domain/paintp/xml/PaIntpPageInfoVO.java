package com.cware.netshopping.domain.paintp.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

@XmlRootElement(name = "PAGE_INFO")
@XmlAccessorType(value = XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class PaIntpPageInfoVO implements Serializable {

	/** 총페이지수 */
	@XmlElement(name = "TOT_PAGE_NUM")
	private Integer totPageNum;
	
	/** 총건수 */
	@XmlElement(name = "TOT_CNT")
	private Long totCnt;
	
	/** 페이지당 건 수 */
	@XmlElement(name = "ROW_CNT")
	private Integer rowCnt;
	
	/** 현재 페이지 번호 */
	@XmlElement(name = "CURRENT_PAGE_NUM")
	private Integer currentPageNum;

	public Integer getTotPageNum() {
		return totPageNum;
	}

	public void setTotPageNum(Integer totPageNum) {
		this.totPageNum = totPageNum;
	}

	public Long getTotCnt() {
		return totCnt;
	}

	public void setTotCnt(Long totCnt) {
		this.totCnt = totCnt;
	}

	public Integer getRowCnt() {
		return rowCnt;
	}

	public void setRowCnt(Integer rowCnt) {
		this.rowCnt = rowCnt;
	}

	public Integer getCurrentPageNum() {
		return currentPageNum;
	}

	public void setCurrentPageNum(Integer currentPageNum) {
		this.currentPageNum = currentPageNum;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
