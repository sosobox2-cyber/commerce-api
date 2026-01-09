package com.cware.partner.sync.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TENTPHOLIDAY")
public class EntpHoliday {

	@Id
	private String entpHolidayNo;
	private String entpCode;
	private String holidayReason;
	private Timestamp holiStartDate;
	private Timestamp holiEndDate;
	private Timestamp delyEndDate;
	private Timestamp delyRestartDate;
	private String holidayReasonCode;
	private String insertId;
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;
}
