package com.cware.netshopping.pafaple.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.domain.PaFapleGoodsVO;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsTarget;
import com.cware.netshopping.pacommon.v2.repository.PaCommonMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsImageMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsOfferMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsdtMappingMapper;
import com.cware.netshopping.pafaple.repository.PaFapleCommonMapper;
import com.cware.netshopping.pafaple.repository.PaFapleGoodsMapper;

@Service
public class PaFapleResultService {

		
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
	PaCommonMapper paCommonMapper;
	
	@Autowired
	PaFapleCommonMapper paFapleCommonMapper;

	@Autowired
	PaFapleGoodsMapper paFapleGoodsMapper;

	@Autowired
	TransLogService transLogService;

//	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 패션플러스 상품연동결과 저장
	 * 
	 * @param paFapleGoods
	 * @param priceApply
	 * @return
	 */
	@Transactional
	public String saveTransProduct(PaFapleGoodsVO paFapleGoods, PaGoodsPriceApply priceApply,List<PaGoodsdtMapping> goodsdtMapping, String modCase) {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    
		// 패플 전송 업데이트
		paFapleGoodsMapper.updateResetTrans(paFapleGoods);
		
		if("BRAND".equals(String.valueOf(modCase))) {
			paFapleGoodsMapper.updateBrandChangeSaleYn(paFapleGoods);
		}

		// 타겟 업데이트
		PaGoodsTarget goodsTarget = new PaGoodsTarget();
		goodsTarget.setGoodsCode(paFapleGoods.getGoodsCode());
		goodsTarget.setPaCode(paFapleGoods.getPaCode());
		goodsTarget.setPaGroupCode("14");
		goodsTarget.setPaGoodsCode(paFapleGoods.getItemId());
		goodsTarget.setModifyId(paFapleGoods.getModifyId());
		goodsTargetMapper.updateGoodsTarget(goodsTarget);

		priceApply.setTransId(paFapleGoods.getModifyId());
		
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
		goodsImageMapper.updateCompleteTrans(paFapleGoods.getGoodsCode(), PaGroup.FAPLE.code(), paFapleGoods.getModifyId());
				
		// 상품정보고시
		goodsOfferMapper.updateCompleteTrans(paFapleGoods.getGoodsCode(), PaGroup.FAPLE.code(), paFapleGoods.getModifyId());
 	    
		if(goodsdtMapping!=null && !goodsdtMapping.isEmpty()) {			
			// 단품 업데이트 (입점시에만 업데이트, 수정은 옵션 매핑 api에서 처리)
			for (int i=0; i<goodsdtMapping.size(); i++) {
				goodsdtMapping.get(i).setModifyId(paFapleGoods.getModifyId());
				goodsdtMappingMapper.updateCompleteTransForFaple(goodsdtMapping.get(i));
			}
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
	public boolean applyRetention(String goodsCode, String paCode, String procId, String message) {

		if (message == null)
			return false;

		// 삭제된 상품인 경우
				if (message.contains("진열된 상품만 수정이 가능합니다..")) {
					// 제휴사 상품코드 지운 후 입점 요청처리
					paFapleGoodsMapper.requestTransTarget(goodsCode,paCode, procId, message);
					// 옵션코드 클리어
					goodsdtMappingMapper.clearOptionCode(goodsCode, paCode, procId);
					return true;
				}


		return false;
	}
}
