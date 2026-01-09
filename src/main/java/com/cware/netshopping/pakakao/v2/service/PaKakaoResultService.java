package com.cware.netshopping.pakakao.v2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaKakaoGoods;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsTarget;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsImageMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsOfferMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsdtMappingMapper;
import com.cware.netshopping.pakakao.v2.repository.PaKakaoGoodsMapper;

@Service
public class PaKakaoResultService {

	@Autowired
	PaKakaoGoodsMapper paKakaoGoodsMapper;
	
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
	 * 카카오 상품연동결과 저장
	 * 
	 * @param paKakaoGoods
	 * @param priceApply
	 * @param goodsOptionList
	 * @return
	 */
	@Transactional
	public String saveTransProduct(PaKakaoGoods paKakaoGoods, PaGoodsPriceApply priceApply, List<PaGoodsdtMapping> goodsOptionList) {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    
 	    // 카카오 전송 업데이트
 	   	paKakaoGoodsMapper.updateResetTrans(paKakaoGoods);

		// 타겟 업데이트
		PaGoodsTarget goodsTarget = new PaGoodsTarget();
		goodsTarget.setGoodsCode(paKakaoGoods.getGoodsCode());
		goodsTarget.setPaCode(paKakaoGoods.getPaCode());
		goodsTarget.setPaGroupCode(paKakaoGoods.getPaGroupCode());
		goodsTarget.setPaGoodsCode(paKakaoGoods.getProductId());
		goodsTarget.setModifyId(paKakaoGoods.getModifyId());
		goodsTargetMapper.updateGoodsTarget(goodsTarget);

		priceApply.setTransId(paKakaoGoods.getModifyId());
		
		// 레거시 가격 데이터 (공통)
		goodsPriceMapper.updatePriceTrans(priceApply);
		
		// 프로모션개선
		goodsPriceMapper.updatePriceApplyTrans(priceApply);
				
		// 상품이미지
		goodsImageMapper.updateCompleteTrans(paKakaoGoods.getGoodsCode(), PaGroup.KAKAO.code(), paKakaoGoods.getModifyId());
		// 상품정보고시
		goodsOfferMapper.updateCompleteTrans(paKakaoGoods.getGoodsCode(), PaGroup.KAKAO.code(), paKakaoGoods.getModifyId());
 	    
 	    // 단품 업데이트
		for (PaGoodsdtMapping goodsdt : goodsOptionList) {
			goodsdt.setModifyId(paKakaoGoods.getModifyId());
			goodsdtMappingMapper.updateCompleteTransForKakao(goodsdt);
		}
		
		// 전송 비대상 단품 리셋
		goodsdtMappingMapper.updateResetTrans(paKakaoGoods.getGoodsCode(), paKakaoGoods.getPaCode(), paKakaoGoods.getModifyId());
		
		return rtnMsg;
	}

	/**
	 * 삭제된 상품인 경우 입점 요청 처리
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param messge
	 * @param code
	 * @return
	 */
	@Transactional
	public boolean applyRetention(String goodsCode, String paCode, String procId, String message, String code) {

		if (code == null)
			return false;

		// 삭제된 상품인 경우
		if (code.equals("-12002")) {
			// 제휴사 상품코드 지운 후 입점 요청처리
			paKakaoGoodsMapper.requestTransTarget(goodsCode,paCode, procId, message);
			// 옵션코드 클리어
			goodsdtMappingMapper.clearOptionCode(goodsCode, paCode, procId);
			return true;
		}

		return false;
	}
}
