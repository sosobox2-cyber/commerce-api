package com.cware.partner.common.domain.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class DateItem {
	@Id
    @Column(name = "DATE_VALUE")
	private Instant date;
}
