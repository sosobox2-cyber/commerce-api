package com.cware.netshopping.paintp.v2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.PaSaleGb;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaIntpGoods;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsTarget;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsImageMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsOfferMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsdtMappingMapper;
import com.cware.netshopping.paintp.v2.repository.PaIntpGoodsMapper;

@Service
public class PaIntpResultService {

	@Autowired
	PaIntpGoodsMapper paIntpGoodsMapper;
	
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
	 * 인터파크 상품연동결과 저장
	 * 
	 * @param paIntpGoods
	 * @param priceApply
	 * @param goodsOptionList
	 * @return
	 */
	@Transactional
	public String saveTransProduct(PaIntpGoods paIntpGoods, PaGoodsPriceApply priceApply, List<PaGoodsdtMapping> goodsOptionList) {
		String rtnMsg = Constants.SAVE_SUCCESS;

	    if (!PaSaleGb.FORSALE.code().equals(paIntpGoods.getPaSaleGb())) {
	    	if("1".equals(paIntpGoods.getProhibitidYn())) {
	    		// 금칙어 여부 리셋
	    		paIntpGoods.setReturnNote("금칙어 사용 불가(기존 상품명: '" + paIntpGoods.getGoodsName() + "')");
	    		paIntpGoodsMapper.resetProhibitidYn(paIntpGoods);
	    	}
			// 판매중지 리셋
	 	   	paIntpGoodsMapper.updateResetSaleTrans(paIntpGoods);
	 	   	return rtnMsg;
	    }
	    
 	    // 인터파크 전송 업데이트
 	   	paIntpGoodsMapper.updateResetTrans(paIntpGoods);

		// 타겟 업데이트
		PaGoodsTarget goodsTarget = new PaGoodsTarget();
		goodsTarget.setGoodsCode(paIntpGoods.getGoodsCode());
		goodsTarget.setPaCode(paIntpGoods.getPaCode());
		goodsTarget.setPaGroupCode(paIntpGoods.getPaGroupCode());
		goodsTarget.setPaGoodsCode(paIntpGoods.getPrdNo());
		goodsTarget.setModifyId(paIntpGoods.getModifyId());
		goodsTargetMapper.updateGoodsTarget(goodsTarget);

		if (priceApply != null) {
			priceApply.setTransId(paIntpGoods.getModifyId());
			
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
		}
		
		// 상품이미지
		goodsImageMapper.updateCompleteTrans(paIntpGoods.getGoodsCode(), PaGroup.INTERPARK.code(), paIntpGoods.getModifyId());
		// 상품정보고시
		goodsOfferMapper.updateCompleteTrans(paIntpGoods.getGoodsCode(), PaGroup.INTERPARK.code(), paIntpGoods.getModifyId());
 	    
 	    // 단품 업데이트
		for (PaGoodsdtMapping goodsdt : goodsOptionList) {
			goodsdt.setModifyId(paIntpGoods.getModifyId());
			goodsdtMappingMapper.updateCompleteTrans(goodsdt);
		}
		
		// 전송 비대상 단품 리셋
		goodsdtMappingMapper.updateResetTrans(paIntpGoods.getGoodsCode(), paIntpGoods.getPaCode(), paIntpGoods.getModifyId());
		
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
		if (message.indexOf("해당상품은 휴면상품으로 처리된 상품입니다") > -1) {
			// 제휴사 상품코드 지운 후 입점 요청처리
			paIntpGoodsMapper.requestTransTarget(goodsCode,paCode, procId, message);
			return true;
		}

		return false;
	}
}
