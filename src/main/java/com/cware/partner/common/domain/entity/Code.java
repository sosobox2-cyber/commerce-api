package com.cware.partner.common.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.cware.partner.common.domain.id.CodeId;

import lombok.Data;

@Data
@Entity
@Table(name="TCODE")
@IdClass(CodeId.class)
public class Code {

	@Id
	private String codeLgroup;
	@Id
	private String codeMgroup;

	private String codeName;
	private String codeGroup;
	private String remark;
	private String remark1;
	private String remark2;
	private String useYn;
}

