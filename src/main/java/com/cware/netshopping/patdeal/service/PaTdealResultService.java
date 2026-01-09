package com.cware.netshopping.patdeal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.PaStatus;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.domain.PaTdealGoodsVO;
import com.cware.netshopping.domain.PaTdealShipCostVO;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaTdealDisplayCategory;
import com.cware.netshopping.domain.model.PaTdealShipArea;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsTarget;
import com.cware.netshopping.pacommon.v2.repository.PaCommonMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsImageMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsOfferMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsdtMappingMapper;
import com.cware.netshopping.patdeal.repository.PaTdealCommonMapper;
import com.cware.netshopping.patdeal.repository.PaTdealGoodsMapper;

@Service
public class PaTdealResultService {

//	@Autowired
//	PaTdealGoodsMapper paTdealGoodsMapper;
		
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
	PaTdealCommonMapper paTdealCommonMapper;

	@Autowired
	PaTdealGoodsMapper paTdealGoodsMapper;

	@Autowired
	TransLogService transLogService;

//	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 티딜 상품연동결과 저장
	 * 
	 * @param paTdealGoods
	 * @param priceApply
	 * @return
	 */
	@Transactional
	public String saveTransProduct(PaTdealGoodsVO paTdealGoods, PaGoodsPriceApply priceApply,
			List<PaGoodsdtMapping> goodsdtMapping, List<String> mallProductOptionNos) {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    
		// 티딜 전송 업데이트
		paTdealGoodsMapper.updateResetTrans(paTdealGoods);

		// 타겟 업데이트
		PaGoodsTarget goodsTarget = new PaGoodsTarget();
		goodsTarget.setGoodsCode(paTdealGoods.getGoodsCode());
		goodsTarget.setPaCode(paTdealGoods.getPaCode());
		goodsTarget.setPaGroupCode(paTdealGoods.getPaGroupCode());
		goodsTarget.setPaGoodsCode(paTdealGoods.getMallProductNo());
		goodsTarget.setModifyId(paTdealGoods.getModifyId());
		goodsTargetMapper.updateGoodsTarget(goodsTarget);

		priceApply.setTransId(paTdealGoods.getModifyId());
		
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
		goodsImageMapper.updateCompleteTrans(paTdealGoods.getGoodsCode(), PaGroup.TDEAL.code(), paTdealGoods.getModifyId());
				
		// 상품정보고시
		goodsOfferMapper.updateCompleteTrans(paTdealGoods.getGoodsCode(), PaGroup.TDEAL.code(), paTdealGoods.getModifyId());
 	    
		if(PaStatus.REQUEST.code().equals(paTdealGoods.getPaStatus())) {			
			// 단품 업데이트 (입점시에만 업데이트, 수정은 옵션 매핑 api에서 처리)
			for (int i=0; i<goodsdtMapping.size(); i++) {
				PaGoodsdtMapping goodsdt = goodsdtMapping.get(i);
				goodsdt.setPaOptionCode(mallProductOptionNos.get(i));
				goodsdt.setModifyId(paTdealGoods.getModifyId());
				goodsdtMappingMapper.updateCompleteTransForTdeal(goodsdt);
			}
		}
		return rtnMsg;
		
	}
	
	
	/**
	 * 티딜 표준카테고리 저장
	 * 
	 * @param paGoodsKindsList
	 * @return
	 */
	@Transactional
	public String savePaGoodsKinds(List<PaGoodsKinds> paGoodsKindsList) {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    
		// TPAGOODSKINDSMOMENT DELETE
		paCommonMapper.deletePaGoodsKindsMoment(PaGroup.TDEAL.code());
		
		// TPAGOODSKINDSMOMENT INSERT
		for(PaGoodsKinds paGoodsKinds : paGoodsKindsList) {
			paCommonMapper.insertPaGoodsKindsMoment(paGoodsKinds);
		}
		
		// TPAGOODSKINDS INSERT
		paCommonMapper.insertPaGoodsKinds(paGoodsKindsList.get(0));
 	    
		return rtnMsg;
		
	}
	
	
	/**
	 * 티딜 전시카테고리 저장
	 * 
	 * @param paTdealDispCategoryList
	 * @return
	 */
	@Transactional
	public String savePaTdealDispCategory(List<PaTdealDisplayCategory> paTdealDispCategoryList) {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		// TPATDEALDISPLAYCATEGORY INSERT
		for(PaTdealDisplayCategory paTdealDispCategory : paTdealDispCategoryList) {
			paTdealCommonMapper.insertPaTdealDispCategory(paTdealDispCategory);
		}
 	    
		return rtnMsg;
		
	}
	
	
	/**
	 * 티딜 입출고 주소 정보 저장
	 * 
	 * @param paEntpSlip
	 * @return
	 */
	@Transactional
	public String savePaEntpSlip(PaEntpSlip paEntpSlip) {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		// TPAENTPSLIP INSERT
		paCommonMapper.insertPaEntpSlip(paEntpSlip);
 	    
		return rtnMsg;
		
	}
	
	
	/**
	 * 티딜 입출고 주소 수정 타겟 OFF
	 * 
	 * @param paEntpSlip
	 * @return
	 */
	@Transactional
	public String updatePaEntpSlip(PaEntpSlip paEntpSlip) {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		// TPAENTPSLIP UPDATE
		paCommonMapper.updatePaEntpSlip(paEntpSlip);
 	    
		return rtnMsg;
		
	}
	
	
	/**
	 * 티딜 정보고시 항목 저장
	 * 
	 * @param paOfferCodeList
	 * @return
	 */
	@Transactional
	public String savePaOfferCode(List<PaOfferCode> paOfferCodeList) {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		// TPAOFFERCODE INSERT
		for(PaOfferCode paOfferCode : paOfferCodeList) {
			paCommonMapper.insertPaOfferCode(paOfferCode);
		}
 	    
		return rtnMsg;
		
	}
	
	
	/**
	 * 티딜 배송비 지역 저장
	 * 
	 * @param patdealshipareaList
	 * @return
	 */
	@Transactional
	public String savePaTdealShipArea(List<PaTdealShipArea> patdealshipareaList) {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		// TPATDEALSHIPAREA INSERT
		for(PaTdealShipArea patdealshiparea : patdealshipareaList) {
			paTdealCommonMapper.insertPaTdealShipArea(patdealshiparea);
		}
 	    
		return rtnMsg;
		
	}

	
	/**
	 * 티딜 브랜드 저장
	 * 
	 * @param paBrandList
	 * @return
	 */
	@Transactional
	public String savePaTdealBrand(List<PaBrand> paBrandList) {
		String rtnMsg = Constants.SAVE_SUCCESS;

		// TPABRAND INSERT
		for (PaBrand paBrand : paBrandList) {
			int cnt = paCommonMapper.selectPaBrand(paBrand);
			if(cnt < 1) {
				paCommonMapper.insertPaBrand(paBrand);
			}
		}

		return rtnMsg;
	}
	
	
	/**
	 * 티딜 배송비 템플릿 정보 저장
	 * 
	 * @param shipCost
	 * @return
	 */
	@Transactional
	public String updatePaTdealShipCost(PaTdealShipCostVO shipCost) {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		// TPATDEALSHIPCOST UPDATE
		paTdealCommonMapper.updatePaTdealShipCost(shipCost);
 	    
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
		if (message.contains("상품이 존재하지 않습니다")) {
			// 제휴사 상품코드 지운 후 입점 요청처리
			paTdealGoodsMapper.requestTransTarget(goodsCode,paCode, procId, message);
			// 옵션코드 클리어
			goodsdtMappingMapper.clearOptionCode(goodsCode, paCode, procId);
			return true;
		}

		return false;
	}

	/**
	 * 티딜 옵션전송결과 저장
	 * @param goodsdtMapping
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return 
	 */
	@Transactional
	public String saveTransProductOption(List<PaGoodsdtMapping> goodsdtMapping, String goodsCode, String paCode,
			String procId) {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		// 단품 업데이트
		for (PaGoodsdtMapping goodsdt : goodsdtMapping) {
			goodsdt.setModifyId(procId);
			goodsdtMappingMapper.updateTransOrderAbleQtyForTdeal(goodsdt);
		}
		
		return rtnMsg;
		
	}
}
