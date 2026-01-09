package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.Data;

@Data
@Entity
@Table(name = "TPATARGETEXCEPT")
// 사용여부체크
@Where(clause = "use_yn = '1'")
public class PaTargetExcept {

	@Id
	private String targetCode;
	private String targetGb;
	private String paGroupCodeAllYn; // 전체 제휴매체 제외여부	
	private String paGroupCode; // 제외매체
	private String useYn;
	private String insertId;
	private Timestamp modifyDate;
}