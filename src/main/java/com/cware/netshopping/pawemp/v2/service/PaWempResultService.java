package com.cware.netshopping.pawemp.v2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaWempGoods;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsTarget;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsImageMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsOfferMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsdtMappingMapper;
import com.cware.netshopping.pawemp.v2.repository.PaWempGoodsMapper;

@Service
public class PaWempResultService {

	@Autowired
	PaWempGoodsMapper paWempGoodsMapper;
	
	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;

	@Autowired
	PaGoodsImageMapper goodsImageMapper;

	@Autowired
	PaGoodsOfferMapper goodsOfferMapper;
	
	@Autowired
	PaGoodsdtMappingMapper goodsdtMappingMapper;
	
	@Autowired
	TransLogService transLogService;

//	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 위메프 상품연동결과 저장
	 * 
	 * @param paWempGoods
	 * @param priceApply
	 * @param goodsOptionList
	 * @return
	 */
	@Transactional
	public String saveTransProduct(PaWempGoods paWempGoods, PaGoodsPriceApply priceApply, List<PaGoodsdtMapping> goodsOptionList) {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    
 	    // 위메프 전송 업데이트
 	   	paWempGoodsMapper.updateResetTrans(paWempGoods);

		// 타겟 업데이트
		PaGoodsTarget goodsTarget = new PaGoodsTarget();
		goodsTarget.setGoodsCode(paWempGoods.getGoodsCode());
		goodsTarget.setPaCode(paWempGoods.getPaCode());
		goodsTarget.setPaGroupCode(paWempGoods.getPaGroupCode());
		goodsTarget.setPaGoodsCode(paWempGoods.getProductNo());
		goodsTarget.setModifyId(paWempGoods.getModifyId());
		goodsTargetMapper.updateGoodsTarget(goodsTarget);

		priceApply.setTransId(paWempGoods.getModifyId());
		
		// 레거시 가격 데이터 (공통)
		goodsPriceMapper.updatePriceTrans(priceApply);
		
		// 프로모션개선
		goodsPriceMapper.updatePriceApplyTrans(priceApply);
		
		// 레거시 프로모션 데이터
		if (priceApply.getCouponPromoNo() == null) {
			goodsPriceMapper.updateDelPromoTrans(priceApply);
		} else {
			goodsPriceMapper.updatePromoTrans(priceApply);
		}
		
		// 상품이미지
		goodsImageMapper.updateCompleteTrans(paWempGoods.getGoodsCode(), PaGroup.WEMP.code(), paWempGoods.getModifyId());
		// 상품정보고시
		goodsOfferMapper.updateCompleteTrans(paWempGoods.getGoodsCode(), PaGroup.WEMP.code(), paWempGoods.getModifyId());
 	    
 	    // 단품 업데이트
		for (PaGoodsdtMapping goodsdt : goodsOptionList) {
			goodsdt.setModifyId(paWempGoods.getModifyId());
			goodsdtMappingMapper.updateCompleteTrans(goodsdt);
		}
		
		// 전송 비대상 단품 리셋
		goodsdtMappingMapper.updateResetTrans(paWempGoods.getGoodsCode(), paWempGoods.getPaCode(), paWempGoods.getModifyId());
		
		return rtnMsg;
	}

	/**
	 * 삭제된 상품인 경우 입점 요청 처리
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param message
	 * @return
	 */
	public boolean applyRetention(String goodsCode, String paCode, String procId, String message) {

		if (message == null)
			return false;

		// 삭제된 상품인 경우
		if (message.equals("데이터가 없습니다.")) {
			// 제휴사 상품코드 지운 후 입점 요청처리
			paWempGoodsMapper.requestTransTarget(goodsCode,paCode, procId, message);
			return true;
		}

		return false;
	}
}
