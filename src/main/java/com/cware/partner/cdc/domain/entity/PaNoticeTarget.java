package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TPANOTICETARGET")
public class PaNoticeTarget {

	@Id
	private String noticeNo;
	private String noticeSeq;
	private String goodsCode;
	private String modifyId;
	private Timestamp modifyDate;
}
