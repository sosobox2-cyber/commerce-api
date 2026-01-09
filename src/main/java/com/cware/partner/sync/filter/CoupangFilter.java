package com.cware.partner.sync.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cware.partner.common.code.ExceptCopnInstallFreeShip;
import com.cware.partner.common.code.TcopnProhibit;
import com.cware.partner.coupang.repository.PaCopnGoodsRepository;
import com.cware.partner.sync.domain.entity.EntpUser;
import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.GoodsDt;
import com.cware.partner.sync.domain.entity.PaCopnGoods;
import com.cware.partner.sync.domain.entity.PaGoodsDtMapping;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;
import com.cware.partner.sync.domain.id.PaGoodsDtId;
import com.cware.partner.sync.domain.id.PaGoodsId;
import com.cware.partner.sync.repository.PaCollectYnRepository;
import com.cware.partner.coupang.repository.PaGoodsDtMappingRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 쿠팡 입점 제약조건 체크
 *
 */
@Slf4j
@Component
public class CoupangFilter extends Filter {

//	@Autowired
//	private PaGoodsPriceRepository paGoodsPriceRepository;
	
	@Autowired
	private PaCollectYnRepository paCollectYnRepository;
	
	@Autowired
	private PaCopnGoodsRepository paCopnGoodsRepository;
	
	@Autowired
	private PaGoodsDtMappingRepository goodsDtRepository; 

	public boolean apply(Goods goods, PaGoodsTarget target) {

		tag = "쿠팡필터";

		// 조건부 배송비에 대한 필터는 공통으로 이관 22.2.11
//		// 조건부나 개별조건부일때 배송비기준금액이 100원 단위보다 커야하고 기준금액이 100원이상이어야함.
//		if (ShipCostFlag.BASEAMT.code().equals(goods.getPaCustShipCost().getShipCostFlag())
//				|| ShipCostFlag.BASEAMT_CODE.code().equals(goods.getPaCustShipCost().getShipCostFlag())) {
//			if (goods.getPaCustShipCost().getShipCostBaseAmt() < 100) {
//				target.setExcept(true);
//				target.setExceptNote("무료배송기준금액은 100원 이상이어야합니다.");
//				log.info("{}: {} [{}-{}] 상품: {}, 기준금액: {}", tag, target.getExceptNote(), target.getGoodsCode(),
//						goods.getPaCustShipCost().getShipCostBaseAmt());
//				logFilter("COUPANG-SHIP_COST_BASE_AMT", target);
//				return false;
//			}
//			if (Math.floorMod(goods.getPaCustShipCost().getShipCostBaseAmt(), 100) > 0) {
//				target.setExcept(true);
//				target.setExceptNote("무료배송기준금액은 100원단위 이상이어야합니다.");
//				log.info("{}: {} [{}-{}] 상품: {}, 기준금액: {}", tag, target.getExceptNote(), target.getGoodsCode(),
//						goods.getPaCustShipCost().getShipCostBaseAmt());
//				logFilter("COUPANG-SHIP_COST_BASE_AMT", target);
//				return false;
//			}
//		}
		// 상품명 길이는 100이하여야 함.
		if (goods.getGoodsName().length() > 100) {
			target.setExcept(true);
			target.setExceptNote("상품명 길이가 100을 초과하면 안됩니다.");
			log.info("{}: {} [{}-{}] 상품: {}, 상품명 : {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getGoodsName());
			logFilter("COUPANG-GOODS_NAME", target);
			return false;
		}

		// 반품배송비가 25만원을 넘으면 제외
		if (goods.getPaCustShipCost().getReturnCost() > 250000) {
			target.setExcept(true);
			target.setExceptNote("반품배송비가 25만원을 초과하면 안됩니다.");
			log.info("{}: {} 상품: {}, 반품비: {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getPaCustShipCost().getReturnCost());
			logFilter("COUPANG-RETURN_COST", target);
			return false;
		}

		// 옵션 체크
		List<String> optionList = new ArrayList<String>();
		
		Optional<PaCopnGoods> isNewYn = paCopnGoodsRepository.findById(new PaGoodsId(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()));

		// 옵션명 길이 체크 하나라도 초과하면 입점 제외
		for (GoodsDt goodsDt : goods.getGoodsDtList()) {
//			if (SaleGb.FORSALE.code().equals(goodsDt.getSaleGb())) {
				// 옵션값 길이 최대 30 (단품속성명)
//				if (goodsDt.getOtherText() != null && goodsDt.getOtherText().length() > 30) {
//					target.setExcept(true);
//					target.setExceptNote("단품기타 길이가 30을 초과하면 안됩니다.");
//					log.info("{}: {} 옵션: {}-{}, 단품기타: {}", tag, target.getExceptNote(), target.getGoodsCode(),
//							goodsDt.getGoodsdtCode(), goodsDt.getOtherText());
//					logFilter("COUPANG-OTHER_TEXT", target);
//					return false;
//				}
				// 쿠팡 상품연동시 단품상세로 전송하고 있어 단품상세 == 옵션값임.
				// 옵션값 30 길이제한 적용
				// 옵션명 길이는 최대 150 (단품상세) => 내부 관리용일때만
				if( goodsDt.getGoodsdtInfo().length() > 30) {
					target.setExcept(true);
					target.setExceptNote("옵션값은 30자를 초과하면 안됩니다.");
					log.info("{}: {} 옵션: {}-{}, 옵션명: {}", tag, target.getExceptNote(), target.getGoodsCode(),
							goodsDt.getGoodsdtCode(), goodsDt.getGoodsdtInfo());
					logFilter("COUPANG-GOODSDT_INFO", target);
					return false;
				}
				
				if (!isNewYn.isPresent()) {
			        // 상품이 처음 입점된 경우, 모든 단품은 신규로 처리하되 반려하지 않음
			        continue; // 단품 비교 로직 건너뜀
			    }
				
				Optional<PaGoodsDtMapping> optional = goodsDtRepository
						.findById(new PaGoodsDtId(target.getPaCode(), target.getGoodsCode(), goodsDt.getGoodsdtCode()));
				
				if (!optional.isPresent() && isNewYn.isPresent()) {
			        // 새 단품 추가됨
			        target.setExcept(true);
			        target.setExceptNote("단품이 새롭게 추가되었습니다.");
			        log.info("{}: {} 상품코드: {}, 단품코드: {}", tag, target.getExceptNote(), target.getGoodsCode(),
			                goodsDt.getGoodsdtCode());
			        logFilter("COUPANG-GOODSDT_INFO_NEW", target);
			        return false;
			    }
				
				optionList.add(goodsDt.getGoodsdtInfo().trim());
//			}
		}

		// 옵션 최대 200개까지
		if (optionList.size() > 200) {
			target.setExcept(true);
			target.setExceptNote("옵션은 200개를 초과하면 안됩니다.");
			log.info("{}: {} 상품: {}, 옵션수: {}", tag, target.getExceptNote(), target.getGoodsCode(), optionList.size());
			logFilter("COUPANG-GOODSDT_SIZE", target);
			return false;
		}

		// 동일 옵션명 등록 불가
		if (optionList.size() != optionList.stream().distinct().count()) {
			target.setExcept(true);
			target.setExceptNote("동일한 옵션명이 있습니다.");
			log.info("{}: {} 상품: {}, 옵션수: {}", tag, target.getExceptNote(), target.getGoodsCode(), optionList.size());
			logFilter("COUPANG-GOODSDT_INFO_DUP", target);
			return false;
		}

		EntpUser shipMan = goods.getShipManUser();

		// 업체주소 유효성 체크
		if ((shipMan.getStdPostAddr1() == null && shipMan.getPostAddr() == null) ||
				(shipMan.getStdPostAddr2() == null && shipMan.getAddr() == null)) {
			target.setExcept(true);
			target.setExceptNote("업체출고지 주소가 유효하지 않습니다.");
			log.info("{}: {} 상품: {}, 업체: {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getShipEntpCode());
			logFilter("COUPANG-SHIP_MAN_ADDR", target);
			return false;
		}

		int telLength =String.join(Optional.ofNullable(shipMan.getEntpManDdd()).orElse("")
				, Optional.ofNullable(shipMan.getEntpManTel1()).orElse("")
				, Optional.ofNullable(shipMan.getEntpManTel2()).orElse("")).length();

		int cellLength =String.join(Optional.ofNullable(shipMan.getEntpManHp1()).orElse("")
				, Optional.ofNullable(shipMan.getEntpManHp2()).orElse("")
				, Optional.ofNullable(shipMan.getEntpManHp3()).orElse("")).length();

		// 업체 연락처 유효성 체크
		if ((telLength < 9 || telLength > 12) || (cellLength < 9 && cellLength > 12 )) {
			target.setExcept(true);
			target.setExceptNote("업체출고지 연락처가 유효하지 않습니다.");
			log.info("{}: {} 상품: {}, 업체: {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getEntpCode());
			logFilter("COUPANG-SHIP_MAN_TEL", target);
			return false;
		}

		// 22.5.12 가격 변동폭 상관없이 연동하기로 함
//		// 직전 금액과의 변동폭이 50% 이상이면 필터처리
//		Optional<PaGoodsPrice> optional = paGoodsPriceRepository.findTransApplyGoodsPrice(target.getPaCode(), target.getGoodsCode());
//		if (optional.isPresent()) {
//			PaGoodsPrice paPrice = optional.get();
//			GoodsPrice price = goods.getGoodsPrice();
//
//			double paBestPrice = paPrice.getSalePrice() - paPrice.getDcAmt() - paPrice.getLumpSumDcAmt();
//
//			if (paBestPrice/2 >= price.getBestPrice() || paBestPrice * 2 <= price.getBestPrice()) {
//				target.setExceptNote("직전 연동금액에서 변동폭이 50%이상입니다.");
//				log.info("{}: {} 상품: {}, 직전연동가: {}, 현재가: {}", tag, target.getExceptNote(), target.getGoodsCode(),
//						paBestPrice, price.getBestPrice());
//				logFilter("COUPANG-SALE_PRICE_50", target);
//				return false;
//			}
//
//		}
		
		if("1".equals(goods.getCollectYn()) && paCollectYnRepository.countCopnCollecyLmsdCnt(goods.getLmsdCode()) < 1) {
			target.setExcept(true);
			target.setExceptNote("쿠팡의 착불 사용 가능 카테고리가 아닙니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("COUPANG-COLLECT_CATEGORY", target);
			return false;
		}
		
		// 상품명 애플 관련 문구 체크
		if(goods.getGoodsName().contains("아이폰") || goods.getGoodsName().contains("에어팟") || goods.getGoodsName().contains("애플워치")) {
			target.setExcept(true);
			target.setExceptNote("애플 상표권 침해 문구 상품은 제휴OUT 연동 대상에서 제외됩니다.");
			log.info("{}: {} [상품:{} 상품명:{}]", tag, target.getExceptNote(), target.getGoodsCode(), goods.getGoodsName());
			logFilter("COUPANG-APPLE_BRAND_WORD", target);
			return false;
		}
		
		// 쿠팡은 주류상품 반려
		if ("1".equals(goods.getGoodsAddInfo().getAlcoholYn())) {
			target.setExcept(true);
			target.setExceptNote("쿠팡 주류상품은 입점할 수 없습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("COUPANG-ALCOHOL-YN", target);
			return false;
		}
		
		// 쿠팡은 주문제작상품 반려
		if ("1".equals(goods.getGoodsAddInfo().getOrderCreateYn())) {
			target.setExcept(true);
			target.setExceptNote("쿠팡 주문제작상품은 입점할 수 없습니다.");
			log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("COUPANG-MAKE-YN", target);
			return false;
		}
		
		// 쿠팡 설치 상품 중 반품/교환비 ‘0원’ 상품에 대한 연동 대상 제외 적용
        // 특정 업체예외(113095,118064,107768) and 소싱매체: 모바일 and 설치상품 and (반품 or 교환배송비 하나라도 무료배송일때) and 착불상품 아닌 것
        if (!Arrays.stream(ExceptCopnInstallFreeShip.values()).anyMatch(v -> v.name().equals("E"+ goods.getEntpCode()))) {
        	if  ("61".equals(goods.getSourcingMedia()) && "1".equals(goods.getInstallYn()) && "0".equals(goods.getCollectYn())
        			&& (goods.getPaCustShipCost().getReturnCost() == 0 || goods.getPaCustShipCost().getChangeCost() == 0)) {
        		target.setExcept(true);
        		target.setExceptNote("쿠팡 설치상품은 반품 또는 교환배송비가 무료배송일 때 입점할 수 없습니다.");
        		log.info("{}: {} 상품: {}", tag, target.getExceptNote(), target.getGoodsCode());
        		logFilter("COUPANG-INSTALL-RETURN-FREE-YN", target);
        		return false;
        	}
        }
        
        // 쿠팡 상품명에 금칙어 존재 시 반려
        TcopnProhibit[] copnProhibit = TcopnProhibit.values();
        for(TcopnProhibit c : copnProhibit) {
        	if(goods.getGoodsName().contains(c.label())) {
	        	target.setExcept(true);
	    		target.setExceptNote("상품명에 금칙어가 존재하여 반려되었습니다. 금칙어 : " + c.label());
	    		log.info("{}: {} [상품:{} 상품명:{}]", tag, target.getExceptNote(), target.getGoodsCode(), goods.getGoodsName());
	    		logFilter("COUPANG-PROHIBIT-YN", target);
	    		return false;	
        	}
        }
        
      //착불배송비가 개발되기 전, 착불상품일 경우 제휴입점이 불가하도록 임시조치 처리
//        if("1".equals(goods.getCollectYn())) {
//        	target.setExcept(true);
//        	target.setExceptNote("착불상품인 경우 제휴입점이 불가합니다.");
//        	log.info("{}: {} [{}]", tag, target.getExceptNote(), target.getGoodsCode());
//        	logFilter("GOODS-COLLECT_SHIPCOST", target);
//        	return false;
//        }
        
        if (isNewYn.isPresent()) {
        	// 상품명 변경시 반려
        	Optional<PaCopnGoods> optional = paCopnGoodsRepository.findById(new PaGoodsId(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()));
        	PaCopnGoods partnerGoods = optional.get();
        	target.setPartnerGoods(partnerGoods);
        	if (!"1".equals(partnerGoods.getChangeNameYn())) {
        		if (goods.getPaGoods().getLastSyncDate().after(partnerGoods.getLastSyncDate()) && partnerGoods.getSellerProductId() != null) {
        			if (!goods.getGoodsName().replaceAll("\\s+", "").equals(partnerGoods.getDisplayProductName().replaceAll("\\s+", ""))) {
        				target.setExcept(true);
        				target.setExceptNote("상품명 변경");
        				log.info("{}: {} [상품:{} 상품명:{}]", tag, target.getExceptNote(), target.getGoodsCode(), goods.getGoodsName());
        				logFilter("GOODS-NAME-CHANGE", target);
        				return false;
        			}
        		}
        	}
        }
        
    	//쿠팡 필수구매옵션 등록 여부
    	if ("N".equals(paCopnGoodsRepository.findCopnOptExceptYn(target.getGoodsCode()))) { // 옵션등록 예외 상품 확인
    		if ("Y".equals(paCopnGoodsRepository.findCopnOptRequiredYn(target.getGoodsCode()))) { // 필수 옵션 필요로 하는지 확인
	    		if ("미등록".equals(paCopnGoodsRepository.findCopnPurchaseOptionYn(target.getGoodsCode()))) { // 필수 옵션 등록 유무 확인
	    			target.setExcept(true);
	    			target.setExceptNote("쿠팡 필수구매옵션 미입력");
	    			log.info("{}: {} [상품:{} 상품명:{}]", tag, target.getExceptNote(), target.getGoodsCode(), goods.getGoodsName());
	    			logFilter("COUPANG-PURCHASE-OPTION", target);
	    			return false;
	    		}
    		}
    	}

		return true;
	}

	@Override
	public boolean apply(Goods goods) {
		return false;
	}

}
