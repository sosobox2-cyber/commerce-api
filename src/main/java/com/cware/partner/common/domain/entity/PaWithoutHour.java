package com.cware.partner.common.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.cware.partner.common.domain.id.PaWithoutHourId;

import lombok.Data;

@Data
@Entity
@Table(name="TPAWITHOUTHOUR")
@IdClass(PaWithoutHourId.class)
public class PaWithoutHour {

	@Id
	private String paGroupCode;
	
	@Id
	private Timestamp withoutStartDate;
	
	@Id
	private Timestamp withoutEndDate;

	private String remark;
	private String useYn;
}

