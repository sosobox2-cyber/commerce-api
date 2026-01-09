package com.cware.partner.sync.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cware.partner.common.code.ExceptFapleSourcingCode;
import com.cware.partner.common.code.SaleGb;
import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.GoodsDt;
import com.cware.partner.sync.domain.entity.PaCustShipCost;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;
import com.cware.partner.sync.domain.id.PaCopnGoodsKindsFreshId;
import com.cware.partner.sync.repository.PaCopnGoodsKindsFreshRepository;
import com.cware.partner.sync.repository.PaFapleShipCostRepository;

import com.cware.partner.common.code.FapleIsbnCat;

import lombok.extern.slf4j.Slf4j;

/**
 * 패션플러스 입점 제약조건 체크
 *
 */
@Slf4j
@Component
public class FapleFilter extends Filter {

	@Autowired
	PaCopnGoodsKindsFreshRepository paCopnGoodsKindsFreshRepository; 
	
	@Autowired
	private PaFapleShipCostRepository paFapleShipCostRepository;
	
	public boolean apply(Goods goods, PaGoodsTarget target) {

		tag = "패션플러스필터";
		// 패션플러스 제외소싱상품 예외 입점, 셀러업체 모두 추가 연동 금지'
		if (Arrays.stream(ExceptFapleSourcingCode.values()).anyMatch(v -> v.name().equals(goods.getSourcingCode()))) {
			if (goods.getSourcingExceptInput() == null || (!goods.getSourcingExceptInput().getPaAllYn().equals("1")
					&& !goods.getSourcingExceptInput().getPaGroupCode().contains(target.getPaGroupCode()))) {
				target.setExceptNote("패션플러스 제휴연동 대상 소싱 상품이 아닙니다: " + goods.getSourcingCode());
				target.setExcept(true);
				log.info("{}: {} [{}-{}]", tag, target.getExceptNote(), target.getGoodsCode(), target.getPaGroupCode());
				logFilter("FAPLE-SOURCING_CODE", target);
				return false;
			}
		}

		// 상품명 길이 최대 100자
		if (goods.getGoodsNameMc().length() > 100) {
			target.setExcept(true);
			target.setExceptNote("상품명 최대 100자까지 입력 가능합니다.");
			log.info("{}: {} [{}-{}] 상품: {}, 상품명 : {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getGoodsName());
			logFilter("FAPLE-GOODS_NAME", target);
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
			logFilter("FAPLE-GOODSDT_INFO_DUP", target);
			return false;
		}

		// 옵션명 길이 체크(25글자 에서 넘게 들어가긴 하나 상품상세조회에서 조회 시 25글자까지 짤려서 노출)
		for (GoodsDt goodsDt : goods.getGoodsDtList()) {
			if (SaleGb.FORSALE.code().equals(goodsDt.getSaleGb())) {

				// 옵션명 길이는 25이하, 하나라도 초과하면 입점 제외
				if (goodsDt.getGoodsdtInfo().length() > 25) {
					target.setExcept(true);
					target.setExceptNote("단품명 최대 25자까지 입력 가능합니다.");
					log.info("{}: {} 상품: {}, 옵션명: {}", tag, target.getExceptNote(), target.getGoodsCode(),
							goodsDt.getGoodsdtInfo());
					logFilter("FAPLE-GOODSDT_INFO", target);
					return false;
				}
				optionList.add(goodsDt.getGoodsdtInfo());
			}
		}

		// 옵션 최대 50개까지
        if (goods.getGoodsDtList().size() > 50) {
			target.setExcept(true);
			target.setExceptNote("단품은 50개까지 등록 가능합니다.");
			log.info("{}: {} 상품: {}, 옵션수: {}", tag, target.getExceptNote(), target.getGoodsCode(), optionList.size());
			logFilter("FAPLE-GOODSDT_SIZE", target);
			return false;
		}

		// 착불상품 연동안함
		if ("1".equals(goods.getCollectYn())) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 착불상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-GOODS_COLLECT", target);
			return false;
		}

		// 성인상품 연동안함
		if ("1".equals(goods.getAdultYn())) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 성인상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-GOODS_ADULT", target);
			return false;
		}
		
		// 주류상품 반려
		if ("1".equals(goods.getGoodsAddInfo().getAlcoholYn())) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 주류상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-ALCOHOL-YN", target);
			return false;
		}
		// 설치상품 반려
		if ("1".equals(goods.getInstallYn())) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 설치상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-GOODS_INSTALL", target);
			return false;
		}
		// 교환/반품 불가상품 반려
		if ("1".equals(goods.getReturnNoYn())) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 교환/반품 불가상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-RETURN_NO_YN", target);
			return false;
		}
		
		//신선식품 반려 
		if(paCopnGoodsKindsFreshRepository.existsById(new PaCopnGoodsKindsFreshId(goods.getLgroup(),goods.getMgroup(),goods.getSgroup(),goods.getDgroup()))) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 신선식품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-GOODS_FRESH_YN", target);
			return false;
		}
		
		if ("1".equals(goods.getGoodsAddInfo().getOrderCreateYn())) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 주문제작상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-GOODS_ORDER_CREATE", target);
			return false;
		}
		if (goods.getOrderMinQty() > 1) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 주문최소수량 2개 이상 상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-GOODS_ORDERMIN_QTY", target);
			return false;
		}
		if("1".equals(goods.getDoNotIslandDelyYn())) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 도서산간/제주 배송 불가 상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-GOODS_ISLAND_DELY_YN", target);
			return false;
		}
		
		if("1".equals(goods.getCustOrdQtyCheckYn())) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 ID당 주문제한 설정상품 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-GOODS_CUST_ORD_QTY_CHECK_YN", target);
			return false;
		}
		
		// 패션플러스 도서 카테고리의 경우 ISBN 값 필수, ISBN 값 누락 시 반려 처리
		FapleIsbnCat[] fapleIsbnCat = FapleIsbnCat.values();
		for(FapleIsbnCat f : fapleIsbnCat) {
			if( goods.getLmsdCode().equals(f.label()) && "1".equals(goods.getGoodsAddInfo().getBookYn()) && goods.getGoodsAddInfo().getIsbn() == null) {
				target.setExcept(true);
				target.setExceptNote("패션플러스 도서 카테고리의 경우 ISBN 값 필수입니다.");
				log.info("{}: {} [상품:{} 상품명:{}]", tag, target.getExceptNote(), target.getGoodsCode(), goods.getGoodsName());
				logFilter("FAPLE-ISBN-YN", target);
				return false;
			}
		}
		
		// 패션플러스 중고상품 반려처리
		if("1".equals(goods.getGoodsAddInfo().getGoodsStts()) || "2".equals(goods.getGoodsAddInfo().getGoodsStts())) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 중고 또는 리퍼상품은 입점되지 않습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-GOODS_USED_YN", target);
			return false;
		}
		
		PaCustShipCost shipCost = goods.getPaCustShipCost();
		// ordcost가 returnCost 보다 높고,
		double ordCost = shipCost.getOrdCost();
		// 반품비 vs 교환비 중 큰값 - 주문배송비
		double returnCost = (shipCost.getReturnCost() >= shipCost.getChangeCost() ? shipCost.getReturnCost() : shipCost.getChangeCost()) - ordCost;
		// 도서 vs 제주 중 큰값
		double islandCost = shipCost.getIslandCost() >= shipCost.getJejuCost() ? shipCost.getIslandCost() : shipCost.getJejuCost();
		// 도서산간 vs 제주 반품비or교환비 중 큰값 - 주문배송비
		double islandReturnCost = (paFapleShipCostRepository.getIslandReturnShipCost(shipCost.getEntpCode(), shipCost.getShipCostCode())) - ordCost;
		
		if(ordCost < 0 || returnCost < 0 || islandCost <0 || islandReturnCost < 0) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 마이너스 배송비의 경우 연동 불가능 합니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-SHIPCOST_MINUS_CHECK_YN", target);
			return false;
		}
		if(ordCost >= 100000 || returnCost >= 100000  || islandCost >= 100000  || islandReturnCost >= 100000 ) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 10만원 이상 배송비의 경우 연동 불가능 합니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-SHIPCOST_10_CHECK_YN", target);
			return false;
		}
		if(ordCost > 0 && returnCost<= 0) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 배송비 존재하나 반품 배송비 0원인 경우 연동 불가능 합니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-SHIPCOST_RETURNCOST_CHECK_YN", target);
			return false;
		}
		if(islandCost > 0 && islandReturnCost<= 0) {
			target.setExcept(true);
			target.setExceptNote("패션플러스 정책에 따라 도서산간/제주 배송비 존재하나 도서산간/제주 반품 배송비 0원인 경우 연동 불가능 합니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("FAPLE-SHIPCOST_ISLANDRETRURNCOST_CHECK_YN", target);
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
