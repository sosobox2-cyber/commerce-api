package com.cware.partner.cdc.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.Data;

@Data
@Entity
@Table(name="TGOODS")
// 제휴상품 입점 대상 기본 조건
@Where(clause = "sign_gb = '80' and sale_gb = '00'  and broad_sale_yn = '0' "
//		+ "and invi_goods_type='00' and ombudsman_yn='0' " // 무형상품/옴부즈맨 변경 불가이므로 동기화에서 타겟팅 제거
//      + "and gift_yn = '0' and sourcing_media in ('01', '61') " // 사은품/소싱매체 타겟팅 제거
//      + "and (sourcing_code is null or sourcing_code not in ('HALFCLUB', 'BORI', 'YIC', 'TLIFE')) " // 소싱코드 타겟팅 제거
//		+ "and sign_gb = '80' and sale_gb = '00' "
		+ "and sqc_gb in ('16', '18') and describe_sqc_gb in ('16', '18') "
		+ "and (order_media_all_yn = '1' or instr(order_media, '61') > 0 or instr(order_media, '62') > 0) "
		+ "and sysdate between sale_start_date and nvl(sale_end_date, sysdate) ")
public class GoodsStart {

	@Id
	private String goodsCode;
	private String goodsName;
	private String entpCode;
	private String brandCode;
	private String sourcingMedia;
	private Timestamp saleStartDate;
	private Timestamp modifyDate;
	private String insertId;
	private String modifyId;
}
