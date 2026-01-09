package com.cware.partner.sync.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cware.partner.common.code.ExceptQeenSourcingCode;
import com.cware.partner.common.code.PaCode;
import com.cware.partner.common.code.SaleGb;
import com.cware.partner.common.code.ShipCostFlag;
import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.GoodsDt;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;
import com.cware.partner.sync.domain.id.PaCopnGoodsKindsFreshId;
import com.cware.partner.sync.repository.PaCopnGoodsKindsFreshRepository;
import com.cware.partner.sync.repository.PaTargetExceptRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 퀸잇 입점 제약조건 체크
 *
 */
@Slf4j
@Component
public class QeenFilter extends Filter {

	@Autowired
	private PaCopnGoodsKindsFreshRepository paCopnGoodsKindsFreshRepository; 
	
	@Autowired
	PaTargetExceptRepository paTargetExceptRepository;
	
//	@Autowired
//	private PaQeenShipCostRepository paQeenShipCostRepository;
	
	public boolean apply(Goods goods, PaGoodsTarget target) {

		tag = "퀸잇필터";
		// 퀸잇 제외소싱상품 예외 입점, 셀러업체 모두 추가 연동 금지'
		if (Arrays.stream(ExceptQeenSourcingCode.values()).anyMatch(v -> v.name().equals(goods.getSourcingCode()))) {
			if (goods.getSourcingExceptInput() == null || (!goods.getSourcingExceptInput().getPaAllYn().equals("1")
					&& !goods.getSourcingExceptInput().getPaGroupCode().contains(target.getPaGroupCode()))) {
				target.setExceptNote("퀸잇 제휴연동 대상 소싱 상품이 아닙니다: " + goods.getSourcingCode());
				target.setExcept(true);
				log.info("{}: {} [{}-{}]", tag, target.getExceptNote(), target.getGoodsCode(), target.getPaGroupCode());
				logFilter("QEEN-SOURCING_CODE", target);
				return false;
			}
		}
		
		// 온라인 입점제외
//		if (PaCode.QEEN_ONLINE.code().equals(target.getPaCode())) {
//			target.setExcept(true);
//			target.setExceptNote("방송상품만 입점 가능합니다.");
//			log.info("{}: {} [{}-{}] 상품: {}, 상품명 : {}", tag, target.getExceptNote(), target.getGoodsCode(),
//					goods.getGoodsName());
//			logFilter("QEEN-GOODS_PACODE", target);
//			return false;
//		}
		
		// 상품명 길이 최대 100자
		if (goods.getGoodsNameMc().length() > 100) {
			target.setExcept(true);
			target.setExceptNote("상품명 최대 100자까지 입력 가능합니다.");
			log.info("{}: {} [{}-{}] 상품: {}, 상품명 : {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getGoodsName());
			logFilter("QEEN-GOODS_NAME", target);
			return false;
		}

		
		if (!ShipCostFlag.FREE.code().equals(goods.getPaCustShipCost().getShipCostFlag())) {
			target.setExcept(true);
			target.setExceptNote("퀸잇 정책에 따라 무료배송비 상품만 입점 가능합니다.");
			log.info("{}: {} 상품: {}, 배송비코드: {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getPaCustShipCost());
			logFilter("QEEN-CUST_SHIP_CODE", target);
			return false;
		}
		
		// 옵션명 중복 처리
		List<String> optionList = new ArrayList<String>();

		goods.getGoodsDtList().stream().filter(goodsDt -> SaleGb.FORSALE.code().equals(goodsDt.getSaleGb()))
				.collect(Collectors.toList()).forEach(goodsDt -> optionList.add(goodsDt.getGoodsdtInfo()));

		if (optionList.size() != optionList.stream().distinct().count()) {
			target.setExcept(true);
			target.setExceptNote("동일한 단품명이 있는 상품은 연동되지 않습니다.");
			log.info("{}: {} 상품: {}, 옵션수: {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getGoodsDtList().size());
			logFilter("QEEN-GOODSDT_INFO_DUP", target);
			return false;
		}

		// 옵션명 길이 체크
		for (GoodsDt goodsDt : goods.getGoodsDtList()) {
			if (SaleGb.FORSALE.code().equals(goodsDt.getSaleGb())) {

				// 옵션명 길이는 255이하, 하나라도 초과하면 입점 제외
				if (goodsDt.getGoodsdtInfo().length() > 255) {
					target.setExcept(true);
					target.setExceptNote("단품명 최대 255자까지 입력 가능합니다.");
					log.info("{}: {} 상품: {}, 옵션명: {}", tag, target.getExceptNote(), target.getGoodsCode(),
							goodsDt.getGoodsdtInfo());
					logFilter("QEEN-GOODSDT_INFO", target);
					return false;
				}
				optionList.add(goodsDt.getGoodsdtInfo());
			}
		}

		// 옵션 최대 50개까지
//		if (optionList.size() > 50) {
//			target.setExcept(true);
//			target.setExceptNote("옵션은 50개를 초과하면 안됩니다.");
//			log.info("{}: {} 상품: {}, 옵션수: {}", tag, target.getExceptNote(), target.getGoodsCode(), optionList.size());
//			logFilter("QEEN-GOODSDT_SIZE", target);
//			return false;
//		}

		// 착불상품 연동안함
		if ("1".equals(goods.getCollectYn())) {
			target.setExcept(true);
			target.setExceptNote("퀸잇 정책에 따라 착불상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("QEEN-GOODS_COLLECT", target);
			return false;
		}

		// 성인상품 연동안함
		if ("1".equals(goods.getAdultYn())) {
			target.setExcept(true);
			target.setExceptNote("퀸잇 정책에 따라 성인상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("QEEN-GOODS_ADULT", target);
			return false;
		}
		
		// 주류상품 반려
		if ("1".equals(goods.getGoodsAddInfo().getAlcoholYn())) {
			target.setExcept(true);
			target.setExceptNote("퀸잇 정책에 따라 주류상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("QEEN-ALCOHOL-YN", target);
			return false;
		}
		
		// 설치상품 반려
//		if ("1".equals(goods.getInstallYn())) {
//			target.setExcept(true);
//			target.setExceptNote("퀸잇의 경우 설치상품 연동이 불가합니다.");
//			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
//			logFilter("QEEN-GOODS_INSTALL", target);
//			return false;
//		}
		
		// 교환/반품 불가상품 반려
		if ("1".equals(goods.getReturnNoYn())) {
			target.setExcept(true);
			target.setExceptNote("퀸잇 정책에 따라 교환/반품 불가상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("QEEN-RETURN_NO_YN", target);
			return false;
		}
		
		//신선식품 반려 
		if(paCopnGoodsKindsFreshRepository.existsById(new PaCopnGoodsKindsFreshId(goods.getLgroup(),goods.getMgroup(),goods.getSgroup(),goods.getDgroup()))) {
			target.setExcept(true);
			target.setExceptNote("퀸잇 정책에 따라 신선식품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("QEEN-GOODS_FRESH_YN", target);
			return false;
		}
		// 주문제작 반려
		if ("1".equals(goods.getGoodsAddInfo().getOrderCreateYn())) {
			target.setExcept(true);
			target.setExceptNote("퀸잇 정책에 따라 주문제작상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("QEEN-GOODS_ORDER_CREATE", target);
			return false;
		}
		
		if (goods.getOrderMinQty() > 1) {
			target.setExcept(true);
			target.setExceptNote("퀸잇 정책에 따라 주문최소수량 2개 이상 상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("QEEN-GOODS_ORDERMIN_QTY", target);
			return false;
		}
		
		if("1".equals(goods.getDoNotIslandDelyYn())) {
			target.setExcept(true);
			target.setExceptNote("퀸잇 정책에 따라 도서산간/제주 배송 불가 상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("QEEN-GOODS_ISLAND_DELY_YN", target);
			return false;
		}
		
		if("1".equals(goods.getCustOrdQtyCheckYn()) && goods.getCustOrdQtyCheckTerm()>0) { //id당 주문제한 중 일수 정해진 경우 연동 제외
			target.setExcept(true);
			target.setExceptNote("퀸잇 정책에 따라 ID당 주문제한 설정상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("QEEN-GOODS_CUST_ORD_QTY_CHECK_YN", target);
			return false;
		}
		/*
		if(goods.getPaCustShipCost().getChangeCost() != goods.getPaCustShipCost().getReturnCost()) {
			target.setExcept(true);
			target.setExceptNote("퀸잇의 경우 반품비와 교환비 금액이 다른 경우 연동 불가합니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("QEEN-GOODS_SHIPCOST_CHECK_YN", target);
			return false;
		}
		
		if(paQeenShipCostRepository.getIslandJejuReturnShipCostChk(goods.getPaCustShipCost().getEntpCode(), goods.getPaCustShipCost().getShipCostCode())) {
			target.setExcept(true);
			target.setExceptNote("퀸잇의 경우 도서산간 혹은 제주의 반품비와 교환비 금액이 다른 경우 연동 불가합니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("QEEN-GOODS_SHIPCOST_ISLAND_JEJU_CHECK_YN", target);
			return false;
		}
		*/
		if(PaCode.QEEN_ONLINE.code().equals(target.getPaCode()) && !(paTargetExceptRepository.findQeenMobileTarget(goods.getEntpCode(), goods.getBrandCode()) > 0)) { //'브랜드' or  '협력사' AND '브랜드' 단위 입점 제외
			target.setExcept(true);
			target.setExceptNote("퀸잇 모바일 입점 불가 상품입니다."+"브랜드코드:"+goods.getBrandCode()+",업체코드:"+goods.getEntpCode());
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("QEEN-EXCEPT_BRAND_ENTP", target);
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
		
		return true;
	}

	@Override
	public boolean apply(Goods goods) {
		return false;
	}

}
