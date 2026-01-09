package com.cware.partner.sync.domain.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;

@Data
@Entity
@Table(name = "TPAQEENGOODSKINDSENC")
public class PaQeenGoodsKindsEnc {
	
	@Id
	private String paLmsdKeyEnc;//PA_LMSD_KEY_ENC
	private String paLmsdKeyDec;//PA_LMSD_KEY_DEC

}