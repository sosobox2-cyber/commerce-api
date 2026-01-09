package com.cware.partner.common.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TCONFIG")
public class Config {

	@Id
	private String item;
	private String val;
	private String content;
}

