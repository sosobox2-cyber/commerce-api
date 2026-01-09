package com.cware.netshopping.panaver.v3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cware.api.panaver.product.type.PaNaverGoodsVO;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsTarget;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsImageMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsOfferMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsdtMappingMapper;
import com.cware.netshopping.panaver.v2.repository.PaNaverGoodsImageMapper;
import com.cware.netshopping.panaver.v2.repository.PaNaverGoodsMapper;
import com.cware.netshopping.panaver.v3.repository.PaNaverGoodsV3Mapper;

@Service
public class PaNaverResultV3Service {

	@Autowired
	PaNaverGoodsMapper paNaverGoodsMapper;
	
	@Autowired
	PaNaverGoodsV3Mapper paNaverGoodsV3Mapper;
	
	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;

	@Autowired
	PaGoodsImageMapper goodsImageMapper;

	@Autowired
	PaNaverGoodsImageMapper naverGoodsImageMapper;

	@Autowired
	PaGoodsOfferMapper goodsOfferMapper;
	
	@Autowired
	PaGoodsdtMappingMapper goodsdtMappingMapper;

	@Autowired
	TransLogService transLogService;

//	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 네이버 상품연동결과 저장
	 * 
	 * @param paNaverGoods
	 * @param priceApply
	 * @return
	 */
	@Transactional
	public String saveTransProduct(PaNaverGoodsVO paNaverGoods, PaGoodsPriceApply priceApply) {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    
 	    // 네이버 전송 업데이트
 	   	paNaverGoodsV3Mapper.updateResetTrans(paNaverGoods);

		// 타겟 업데이트
		PaGoodsTarget goodsTarget = new PaGoodsTarget();
		goodsTarget.setGoodsCode(paNaverGoods.getGoodsCode());
		goodsTarget.setPaCode(paNaverGoods.getPaCode());
		goodsTarget.setPaGroupCode(paNaverGoods.getPaGroupCode());
		goodsTarget.setPaGoodsCode(paNaverGoods.getProductId());
		goodsTarget.setModifyId(paNaverGoods.getModifyId());
		goodsTargetMapper.updateGoodsTarget(goodsTarget);

		priceApply.setTransId(paNaverGoods.getModifyId());
		
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
		// 상품정보고시
		goodsOfferMapper.updateCompleteTrans(paNaverGoods.getGoodsCode(), PaGroup.NAVER.code(), paNaverGoods.getModifyId());
 	    
		return rtnMsg;
	}
	
	/**
	 * 네이버 옵션전송결과 저장
	 * 
	 * @param goodsOptionList
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	@Transactional
	public String saveTransProductOption(List<PaGoodsdtMapping> goodsOptionList, String goodsCode, String paCode, String procId) {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		// 단품 업데이트
		for (PaGoodsdtMapping goodsdt : goodsOptionList) {
			goodsdt.setModifyId(procId);
			goodsdtMappingMapper.updateCompleteTransForV3Naver(goodsdt);
		}
		
		// 전송 비대상 단품 리셋
		//goodsdtMappingMapper.updateResetTrans(goodsCode, paCode, procId);// 불필요 삭제 2023.11.06 nwkim
						
		return rtnMsg;
	}

}
