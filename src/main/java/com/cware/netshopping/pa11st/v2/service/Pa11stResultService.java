package com.cware.netshopping.pa11st.v2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.domain.model.Pa11stGoods;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.pa11st.v2.repository.Pa11stGoodsMapper;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsTarget;
import com.cware.netshopping.pacommon.v2.domain.PaRetentionGoods;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsImageMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsOfferMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsdtMappingMapper;
import com.cware.netshopping.pacommon.v2.repository.PaRetentionGoodsMapper;

@Service
public class Pa11stResultService {

	@Autowired
	Pa11stGoodsMapper pa11stGoodsMapper;
	
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
	PaRetentionGoodsMapper retentionGoodsMapper;
	
	@Autowired
	TransLogService transLogService;

//	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 11번가 상품연동결과 저장
	 * 
	 * @param pa11stGoods
	 * @param priceApply
	 * @param goodsOptionList
	 * @return
	 */
	@Transactional
	public String saveTransProduct(Pa11stGoods pa11stGoods, PaGoodsPriceApply priceApply, List<PaGoodsdtMapping> goodsOptionList) {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    
 	    // 11번가 전송 업데이트
 	   	pa11stGoodsMapper.updateResetTrans(pa11stGoods);

		// 타겟 업데이트
		PaGoodsTarget goodsTarget = new PaGoodsTarget();
		goodsTarget.setGoodsCode(pa11stGoods.getGoodsCode());
		goodsTarget.setPaCode(pa11stGoods.getPaCode());
		goodsTarget.setPaGroupCode(pa11stGoods.getPaGroupCode());
		goodsTarget.setPaGoodsCode(pa11stGoods.getProductNo());
		goodsTarget.setModifyId(pa11stGoods.getModifyId());
		goodsTargetMapper.updateGoodsTarget(goodsTarget);

		priceApply.setTransId(pa11stGoods.getModifyId());
		
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
		goodsImageMapper.updateCompleteTrans(pa11stGoods.getGoodsCode(), PaGroup.SK11ST.code(), pa11stGoods.getModifyId());
		// 상품정보고시
		goodsOfferMapper.updateCompleteTrans(pa11stGoods.getGoodsCode(), PaGroup.SK11ST.code(), pa11stGoods.getModifyId());
 	    
 	    // 단품 업데이트
		for (PaGoodsdtMapping goodsdt : goodsOptionList) {
			goodsdt.setModifyId(pa11stGoods.getModifyId());
			goodsdtMappingMapper.updateCompleteTrans(goodsdt);
		}
		
		// 전송 비대상 단품 리셋
		goodsdtMappingMapper.updateResetTrans(pa11stGoods.getGoodsCode(), pa11stGoods.getPaCode(), pa11stGoods.getModifyId());
		
		return rtnMsg;
	}

	/**
	 * 삭제된 상품인 경우 이력 저장 후 입점 요청 처리
	 * 
	 * @param pa11stGoods
	 * @param message
	 * @return
	 */
	@Transactional
	public boolean applyRetention(Pa11stGoods pa11stGoods, String message) {

		if (message == null)
			return false;

		// 삭제된 상품인 경우
		if (message.indexOf("해당 상품의 정보를 찾을 수 없습니다") > -1
				|| message.indexOf("셀러재고코드(sellerStockCd)는 같은 이름을 중복으로 사용할 수 없습니다") > -1) {
			// 삭제 이력 생성
			PaRetentionGoods retentionGoods = new PaRetentionGoods();
			retentionGoods.setPaGroupCode(PaGroup.SK11ST.code());
			retentionGoods.setPaCode(pa11stGoods.getPaCode());
			retentionGoods.setGoodsCode(pa11stGoods.getGoodsCode());
			retentionGoods.setPaGoodsCode(pa11stGoods.getProductNo());
			retentionGoods.setStatus("U");
			retentionGoods.setTargetYn("0");
			retentionGoods.setMemo(message);
			retentionGoods.setInsertId(pa11stGoods.getModifyId());
			retentionGoodsMapper.insertRetentionGoods(retentionGoods);

			// 제휴사 상품코드 지운 후 입점 요청처리
			pa11stGoodsMapper.requestTransTarget(pa11stGoods.getGoodsCode(), pa11stGoods.getPaCode(),
					pa11stGoods.getModifyId(), message);

			return true;
		}

		return false;
	}
}
