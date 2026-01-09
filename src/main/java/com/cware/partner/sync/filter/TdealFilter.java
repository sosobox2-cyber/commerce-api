package com.cware.partner.sync.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.cware.partner.common.code.ExceptTdealSourcingCode;
import com.cware.partner.common.code.SaleGb;
import com.cware.partner.common.code.ShipCostFlag;
import com.cware.partner.sync.domain.entity.EntpUser;
import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.GoodsDt;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;
import lombok.extern.slf4j.Slf4j;

/**
 * 티딜 입점 제약조건 체크
 *
 */
@Slf4j
@Component
public class TdealFilter extends Filter {

	public boolean apply(Goods goods, PaGoodsTarget target) {

		tag = "티딜필터";
		
		// 티딜 제외소싱상품 예외 입점, 셀러업체 모두 추가 연동 금지'
		if (Arrays.stream(ExceptTdealSourcingCode.values()).anyMatch(v -> v.name().equals(goods.getSourcingCode()))) {
			if (goods.getSourcingExceptInput() == null || (!goods.getSourcingExceptInput().getPaAllYn().equals("1")
					&& !goods.getSourcingExceptInput().getPaGroupCode().contains(target.getPaGroupCode()))) {
				target.setExceptNote("티딜 제휴연동 대상 소싱 상품이 아닙니다: " + goods.getSourcingCode());
				target.setExcept(true);
				log.info("{}: {} [{}-{}]", tag, target.getExceptNote(), target.getGoodsCode(), target.getPaGroupCode());
				logFilter("TDEAL-SOURCING_CODE", target);
				return false;
			}
		}
		
		// 주소정제 성공한 건 들만 입점처리
		// 업체출고지 유효성 체크
		EntpUser shipMan = goods.getShipManUser();
		if (shipMan.getStdPost() == null || shipMan.getStdRoadPost() == null) {
			target.setExcept(true);
			target.setExceptNote("업체출고지 주소가 유효하지 않습니다.");
			log.info("{}: {} 상품: {}, 업체: {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getShipEntpCode());
			logFilter("TDEAL-SHIP_MAN_ADDR", target);
			return false;
		}

		// 업체회수지 유효성 체크
		EntpUser returnMan = goods.getReturnManUser();
		if (returnMan.getStdPost() == null || returnMan.getStdRoadPost() == null) {
			target.setExcept(true);
			target.setExceptNote("업체회수지 주소가 유효하지 않습니다.");
			log.info("{}: {} 상품: {}, 업체: {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getShipEntpCode());
			logFilter("TDEAL-RETURN_MAN_ADDR", target);
			return false;
		}
		
		// 업체 출고/회수 담당자 체크
		if ( !"30".equals(shipMan.getEntpManGb()) || !"20".equals(returnMan.getEntpManGb()) ) {
			target.setExcept(true);
			target.setExceptNote("업체 출고/회수 담당자가 올바르지 않습니다.");
			log.info("{}: {} 상품: {}, 업체: {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getShipEntpCode());
			logFilter("TDEAL-ENTP_MAN_GB", target);
			return false;
		}
		
		// 티딜의 경우 무료배송만 입점 가능함
		if (!ShipCostFlag.FREE.code().equals(goods.getPaCustShipCost().getShipCostFlag())) {
			target.setExcept(true);
			target.setExceptNote("티딜의 경우 무료배송만 입점 가능합니다.");
			log.info("{}: {} 상품: {}, 배송비코드: {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getPaCustShipCost());
			logFilter("TDEAL-CUST_SHIP_CODE", target);
			return false;
		}
		
		// 상품명 길이 최대 100자
		if (goods.getGoodsNameMc().length() > 100) {
			target.setExcept(true);
			target.setExceptNote("상품명 길이가 100을 초과하면 안됩니다.");
			log.info("{}: {} [{}-{}] 상품: {}, 상품명 : {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getGoodsName());
			logFilter("TDEAL-GOODS_NAME", target);
			return false;
		}

		// 옵션명 중복 처리
		List<String> optionList = new ArrayList<String>();
		
		goods.getGoodsDtList().stream().filter( 
				goodsDt -> SaleGb.FORSALE.code().equals(goodsDt.getSaleGb())).collect(Collectors.toList())
										.forEach( goodsDt -> optionList.add(goodsDt.getGoodsdtInfo()));
		
		if (optionList.size() != optionList.stream().distinct().count()) {
			target.setExcept(true);
			target.setExceptNote("동일한 옵션명이 있습니다.");
			log.info("{}: {} 상품: {}, 옵션수: {}", tag, target.getExceptNote(), target.getGoodsCode(), goods.getGoodsDtList().size());
			logFilter("TDEAL-GOODSDT_INFO_DUP", target);
			return false;
		}
		
		// 옵션명 길이 체크
		for (GoodsDt goodsDt : goods.getGoodsDtList()) {
			if (SaleGb.FORSALE.code().equals(goodsDt.getSaleGb())) {

				// 옵션명 길이는 255이하, 하나라도 초과하면 입점 제외
				if (goodsDt.getGoodsdtInfo().length() > 255) {
					target.setExcept(true);
					target.setExceptNote("옵션명 길이가 255를 초과하면 안됩니다.");
					log.info("{}: {} 상품: {}, 옵션명: {}", tag, target.getExceptNote(), target.getGoodsCode(),
							goodsDt.getGoodsdtInfo());
					logFilter("TDEAL-GOODSDT_INFO", target);
					return false;
				}
			}
		}
		
		// 옵션 최대 100개까지
		if (optionList.size() > 100) {
			target.setExcept(true);
			target.setExceptNote("옵션은 100개를 초과하면 안됩니다.");
			log.info("{}: {} 상품: {}, 옵션수: {}", tag, target.getExceptNote(), target.getGoodsCode(), optionList.size());
			logFilter("TDEAL-GOODSDT_SIZE", target);
			return false;
		}
		
		// 착불상품 연동안함
		if("1".equals(goods.getCollectYn())) {
			target.setExcept(true);
			target.setExceptNote("티딜의 경우 착불상품 연동이 불가합니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("TDEAL-GOODS_COLLECT", target);
			return false;
		}
		
		// 성인상품 연동안함
		if("1".equals(goods.getAdultYn())) {
			target.setExcept(true);
			target.setExceptNote("티딜의 경우 성인상품 연동이 불가합니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("TDEAL-GOODS_ADULT", target);
			return false;
		}
		
		//착불배송비가 개발되기 전, 착불상품일 경우 제휴입점이 불가하도록 임시조치 처리
        if("1".equals(goods.getCollectYn())) {
        	target.setExcept(true);
        	target.setExceptNote("착불상품인 경우 제휴입점이 불가합니다.");
        	log.info("{}: {} [{}]", tag, target.getExceptNote(), target.getGoodsCode());
        	logFilter("GOODS-COLLECT_SHIPCOST", target);
        	return false;
        }
		
//	설치,주문제작 상품 연동하기로 결정	2024.04.09
//		
//		// 설치상품 연동 안함
//		if("1".equals(goods.getInstallYn())) {
//			target.setExcept(true);
//			target.setExceptNote("티딜의 경우 설치상품 연동이 불가합니다.");
//			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
//			logFilter("TDEAL-GOODS_INSTALL", target);
//			return false;
//		}
//		
//		// 주문제작 연동 안함
//		if("1".equals(goods.getGoodsAddInfo().getOrderCreateYn())) {
//			target.setExcept(true);
//			target.setExceptNote("티딜의 경우 주문제작 연동이 불가합니다.");
//			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
//			logFilter("TDEAL-GOODS_ORDER_CREATE", target);
//			return false;
//		}
		
		
		return true;
	}

	@Override
	public boolean apply(Goods goods) {
		return false;
	}

}
