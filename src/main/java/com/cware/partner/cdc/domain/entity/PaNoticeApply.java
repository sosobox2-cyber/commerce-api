package com.cware.partner.cdc.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TPANOTICEAPPLY")
public class PaNoticeApply {

	@Id
	private String noticeNo;
	private String paGroupCode;
	private String goodsCode;
	private String modifyId;
}
