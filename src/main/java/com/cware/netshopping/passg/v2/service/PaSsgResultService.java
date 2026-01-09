package com.cware.netshopping.passg.v2.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.PaSaleGb;
import com.cware.netshopping.common.code.PaStatus;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.domain.PaSsgGoodsVO;
import com.cware.netshopping.domain.model.PaSsgGoodsdtMapping;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsTarget;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsImageMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsOfferMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.passg.v2.domain.Uitem;
import com.cware.netshopping.passg.v2.repository.PaSsgGoodsMapper;
import com.cware.netshopping.passg.v2.repository.PaSsgGoodsdtMappingMapper;

@Service
public class PaSsgResultService {

	@Autowired
	PaSsgGoodsMapper paSsgGoodsMapper;
	
	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;

	@Autowired
	PaGoodsImageMapper goodsImageMapper;

	@Autowired
	PaGoodsOfferMapper goodsOfferMapper;
	
	@Autowired
	PaSsgGoodsdtMappingMapper goodsdtMappingMapper;

	@Autowired
	TransLogService transLogService;

//	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * SSG 상품연동결과 저장
	 * 
	 * @param paSsgGoods
	 * @param priceApply
	 * @param goodsOptionList
	 * @param uitems
	 * @return
	 */
	@Transactional
	public String saveTransProduct(PaSsgGoodsVO paSsgGoods, PaGoodsPriceApply priceApply,
			List<PaSsgGoodsdtMapping> goodsOptionList, List<Uitem> uitems) {
		String rtnMsg = Constants.SAVE_SUCCESS;

	    if (!PaSaleGb.FORSALE.code().equals(paSsgGoods.getPaSaleGb())) {
			// 판매중지 리셋
	    	paSsgGoodsMapper.updateResetSaleTrans(paSsgGoods);
	 	   	return rtnMsg;
	    }
	    
 	    // SSG 전송 업데이트
 	   	paSsgGoodsMapper.updateResetTrans(paSsgGoods);
 	   	
 	   	//착불여부 업데이트
 	   	if(PaStatus.REQUEST.code().equals(paSsgGoods.getPaStatus())
 	   			|| PaStatus.REJECT.code().equals(paSsgGoods.getPaStatus()) ) { //입점시에만 업데이트 	   		
 	   		paSsgGoodsMapper.updatePaCollectYn(paSsgGoods);
 	   	}

		// 타겟 업데이트
		PaGoodsTarget goodsTarget = new PaGoodsTarget();
		goodsTarget.setGoodsCode(paSsgGoods.getGoodsCode());
		goodsTarget.setPaCode(paSsgGoods.getPaCode());
		goodsTarget.setPaGroupCode(paSsgGoods.getPaGroupCode());
		goodsTarget.setPaGoodsCode(paSsgGoods.getItemId());
		goodsTarget.setModifyId(paSsgGoods.getModifyId());
		goodsTargetMapper.updateGoodsTarget(goodsTarget);

		priceApply.setTransId(paSsgGoods.getModifyId());
		
		// 레거시 가격 데이터 (공통)
		goodsPriceMapper.updatePriceTrans(priceApply);
		
		// 프로모션개선
		goodsPriceMapper.updatePriceApplyTrans(priceApply);
		
		// 레거시 프로모션 데이터
		goodsPriceMapper.updatePromoGoodsPriceTrans(priceApply);
		
		// 상품이미지
		goodsImageMapper.updateCompleteTrans(paSsgGoods.getGoodsCode(), PaGroup.SSG.code(), paSsgGoods.getModifyId());
		// 상품정보고시
		goodsOfferMapper.updateCompleteTrans(paSsgGoods.getGoodsCode(), PaGroup.SSG.code(), paSsgGoods.getModifyId());
 	    
 	    // 단품 업데이트
		for (PaSsgGoodsdtMapping goodsdt : goodsOptionList) {
			// 옵션코드매핑
			if (uitems != null) {
				Optional<Uitem> transItem = uitems.stream().filter(uitem -> uitem.getTempUitemId().equals(
						goodsdt.getPaCode()+goodsdt.getGoodsCode()+goodsdt.getGoodsdtCode()+goodsdt.getGoodsdtSeq()
						)).findFirst();
				if (transItem.isPresent()) {
					goodsdt.setPaOptionCode(transItem.get().getUitemId());
				}
			}
			goodsdt.setModifyId(paSsgGoods.getModifyId());
			goodsdtMappingMapper.updateCompleteTrans(goodsdt);
		}
		
		// 전송 비대상 단품 리셋
		goodsdtMappingMapper.updateResetTrans(paSsgGoods.getGoodsCode(), paSsgGoods.getPaCode(), paSsgGoods.getModifyId());
		
		return rtnMsg;
	}
    
}
