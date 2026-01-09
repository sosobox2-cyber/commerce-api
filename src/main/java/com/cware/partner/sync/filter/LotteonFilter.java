package com.cware.partner.sync.filter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;
import com.cware.partner.sync.domain.entity.Prohibitdt;

import com.cware.partner.sync.repository.ProhibitRepository;

import lombok.extern.slf4j.Slf4j;
import java.util.regex.Pattern;


/**
 * 롯데온 입점 제약조건 체크
 *
 */
@Slf4j
@Component
public class LotteonFilter extends Filter {
	
	@Autowired
	private ProhibitRepository prohibitRepository;

	
	public boolean apply(Goods goods, PaGoodsTarget target) {

		tag = "롯데온필터";

		// 단품개수가 500개를 초과하면 제외
		if (goods.getGoodsDtList().size() > 500) {
			target.setExcept(true);
			target.setExceptNote("단품개수가 500개를 초과했습니다.");
			log.info("{}: {} [{}-{}] 상품: {}, 단품개수 : {}", tag, target.getExceptNote(), target.getGoodsCode(),
					goods.getGoodsDtList().size());
			logFilter("LOTTEON-GOODSDT_SIZE", target);
			return false;
		}
				
		// 롯데온, EP 금칙어와 동일한 필터 적용		
		List <Prohibitdt> prohibitList= prohibitRepository.findAll(); // EP 금칙어 리스트
		String ltonGoodsName = goods.getGoodsName().replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]", " "); // 롯데온 상품명 특수문자 제거
		
		for (Prohibitdt prohibitdt : prohibitList) {
	       String pattern = "(^|\\s)" + Pattern.quote(prohibitdt.getProhibitNote()) + "(\\s|$)"; // 롯데온 금칙어 양쪽 공란 입력
	       if (Pattern.compile(pattern).matcher(ltonGoodsName).find()) { // 롯데온 상품명과 EP 금칙어 비교
			   target.setExcept(true);
	    	   target.setExceptNote("상품명에 금칙어가 존재하여 반려되었습니다. 금칙어 : " + prohibitdt.getProhibitNote());
	    	   log.info("{}: {} [상품:{} 상품명:{}]", tag, target.getExceptNote(), target.getGoodsCode(), goods.getGoodsName());
	    	   logFilter("LOTTEON-PROHIBIT-YN", target);	    		
	           return false;
	       }
		}
		
		return true;
	}

	@Override
	public boolean apply(Goods goods) {
		return false;
	}

}
