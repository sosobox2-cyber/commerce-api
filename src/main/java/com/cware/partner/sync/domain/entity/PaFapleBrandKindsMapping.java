package com.cware.partner.sync.domain.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@Table(name = "TPAFAPLEBRANDKINDSMAPPING")
public class PaFapleBrandKindsMapping {
	
	@Id
	private String paLmsdKey;
	
	private String brandKindsName;

	
}