package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.Data;

@Data
@Entity
@Table(name="TPROMOM")
//프로모션 적용 대상 (즉시할인)
@Where(clause = "coupon_yn = '1' and do_type = '30' and immediately_yn = '1' and alcout_promo_yn = '1' "
		+ "and use_code = '00' and sysdate between promo_bdate and promo_edate ")
public class PromoApply {

	@Id
	private String promoNo;
	private String promoName;
	private String firstOrderYn;
	private String couponYn;
	private String appType;
	private String doType;
	private Timestamp promoBdate;
	private Timestamp promoEdate;
	private String membGbAllYn;
	private String membGb;
	private String orderMediaAllYn;
	private String orderMedia;
	private String arsYn;
	private String mediaCodeAllYn;
	private String mediaCode;
	private String limitYn;
	private String goodsAllYn;
	private String grossNetFlag;
	private double appAmt;
	private Timestamp modifyDate;
}
