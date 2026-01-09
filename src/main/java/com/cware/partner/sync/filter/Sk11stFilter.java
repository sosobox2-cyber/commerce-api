package com.cware.partner.sync.filter;

import org.springframework.stereotype.Component;

import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;

import lombok.extern.slf4j.Slf4j;

/**
 * 11번가 입점 제약조건 체크
 *
 */
@Slf4j
@Component
public class Sk11stFilter extends Filter {

	public boolean apply(Goods goods, PaGoodsTarget target) {
		tag = "11번가필터";

		// 도서산간/제주 추가배송비 20만원 초과시 제외
		if ((goods.getPaCustShipCost().getIslandCost() - goods.getPaCustShipCost().getOrdCost()) > 200_000
				|| (goods.getPaCustShipCost().getJejuCost() - goods.getPaCustShipCost().getOrdCost()) > 200_000) {
			target.setExcept(true);
			target.setExceptNote("도서산간 또는 제주 추가배송비는 20만원을 초과하면 안됩니다.");
			log.info("{}: {} 상품: {}, 도서산간비: {}, 제주비: {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getPaCustShipCost().getIslandCost(), 	goods.getPaCustShipCost().getJejuCost());
			logFilter("11ST-ISLAND_COST", target);
			return false;
		}
		return true;
	}

	@Override
	public boolean apply(Goods goods) {
		return false;
	}

}
