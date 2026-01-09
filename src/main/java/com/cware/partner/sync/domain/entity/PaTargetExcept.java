package com.cware.partner.sync.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TPATARGETEXCEPT")
public class PaTargetExcept {

	@Id
	private String targetCode;
	private String targetGb;
	private String paGroupCodeAllYn; // 전체 제휴매체 제외여부
	private String paGroupCode; // 제외매체
	private String useYn;
	private Timestamp modifyDate;

}