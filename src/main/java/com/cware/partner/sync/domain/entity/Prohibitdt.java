package com.cware.partner.sync.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.Data;

@Data
@Entity
@Table(name = "TPROHIBITDT")
// EP 금칙어만 조회
@Where(clause = "prohibit_id = 'EP'")
public class Prohibitdt {

	@Id
	private String seq;
	private String prohibitId;
	private String lgroup;
	private String prohibitNote;
	private String insertId;
	private String insertDate;
	private String modifyId;
	private String modifyDate;
}
