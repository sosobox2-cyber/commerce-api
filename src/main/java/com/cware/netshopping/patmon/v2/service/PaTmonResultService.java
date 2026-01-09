package com.cware.netshopping.patmon.v2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.domain.PaTmonGoodsVO;
import com.cware.netshopping.domain.PaTmonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsTarget;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsImageMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsOfferMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsdtMappingMapper;
import com.cware.netshopping.patmon.v2.repository.PaTmonGoodsMapper;

@Service
public class PaTmonResultService {

	@Autowired
	PaTmonGoodsMapper paTmonGoodsMapper;
	
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
	 * 티몬 상품연동결과 저장
	 * 
	 * @param paTmonGoods
	 * @param priceApply
	 * @param goodsOptionList
	 * @return
	 */
	@Transactional
	public String saveTransProduct(PaTmonGoodsVO paTmonGoods, PaGoodsPriceApply priceApply, List<PaGoodsdtMapping> goodsOptionList) {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    
 	    // 티몬 전송 업데이트
 	   	paTmonGoodsMapper.updateResetTrans(paTmonGoods);

		// 타겟 업데이트
		PaGoodsTarget goodsTarget = new PaGoodsTarget();
		goodsTarget.setGoodsCode(paTmonGoods.getGoodsCode());
		goodsTarget.setPaCode(paTmonGoods.getPaCode());
		goodsTarget.setPaGroupCode(paTmonGoods.getPaGroupCode());
		goodsTarget.setPaGoodsCode(paTmonGoods.getDealNo());
		goodsTarget.setModifyId(paTmonGoods.getModifyId());
		goodsTargetMapper.updateGoodsTarget(goodsTarget);

		priceApply.setTransId(paTmonGoods.getModifyId());
		
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
		goodsImageMapper.updateCompleteTrans(paTmonGoods.getGoodsCode(), PaGroup.TMON.code(), paTmonGoods.getModifyId());
		// 상품정보고시
		goodsOfferMapper.updateCompleteTrans(paTmonGoods.getGoodsCode(), PaGroup.TMON.code(), paTmonGoods.getModifyId());
 	    
 	    // 단품 업데이트
		for (PaGoodsdtMapping goodsdt : goodsOptionList) {
			goodsdt.setModifyId(paTmonGoods.getModifyId());
			goodsdtMappingMapper.updateCompleteTransForTmon(goodsdt);
		}
		
		// 티몬은 상품수정에서 재고변경 불가
		// 전송 비대상 단품 리셋
//		goodsdtMappingMapper.updateResetTrans(paTmonGoods.getGoodsCode(), paTmonGoods.getPaCode(), paTmonGoods.getModifyId());
		
		return rtnMsg;
	}

	/**
	 * 티몬 옵션등록결과 저장
	 * 
	 * @param goodsOptionList
	 * @return
	 */
	@Transactional
	public String saveTransProductOption(List<PaTmonGoodsdtMappingVO> goodsOptionList) {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    
 	    // 단품 업데이트
		for (PaGoodsdtMapping goodsdt : goodsOptionList) {
			if (goodsdt.getPaOptionCode() != null) goodsdtMappingMapper.updateCompleteTransForTmon(goodsdt);
		}
		
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
	@Transactional
	public boolean applyRetention(String goodsCode, String paCode, String procId, String message) {

		if (message == null)
			return false;

		// 삭제된 상품인 경우
		if (message.contains(paCode+ "-" + goodsCode +")가 없습니다") ||
				message.contains("삭제된")) {
			// 제휴사 상품코드 지운 후 입점 요청처리
			paTmonGoodsMapper.requestTransTarget(goodsCode,paCode, procId, message);
			// 옵션코드 클리어
			goodsdtMappingMapper.clearOptionCode(goodsCode, paCode, procId);
			return true;
		}

		return false;
	}
}
