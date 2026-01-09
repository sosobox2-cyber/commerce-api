package com.cware.netshopping.paqeen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.domain.PaQeenGoodsVO;
import com.cware.netshopping.domain.PaQeenShipCostVO;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaQeenGoodsdtMapping;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsTarget;
import com.cware.netshopping.pacommon.v2.repository.PaCommonMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsImageMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsOfferMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.paqeen.domain.CategoryOffer;
import com.cware.netshopping.paqeen.domain.CategoryOfferList;
import com.cware.netshopping.paqeen.domain.ItemProposal;
import com.cware.netshopping.paqeen.repository.PaQeenCommonMapper;
import com.cware.netshopping.paqeen.repository.PaQeenGoodsMapper;
import com.cware.netshopping.paqeen.repository.PaQeenGoodsdtMappingMapper;

@Service
public class PaQeenResultService {

		
	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;

	@Autowired
	PaGoodsImageMapper goodsImageMapper;

	@Autowired
	PaGoodsOfferMapper goodsOfferMapper;
	
	@Autowired
	PaQeenGoodsdtMappingMapper goodsdtMappingMapper;

	@Autowired
	PaQeenGoodsMapper paQeenGoodsMapper;

	@Autowired
	TransLogService transLogService;
	
	@Autowired
	PaQeenCommonMapper paQeenCommonMapper;
	
	@Autowired
	PaCommonMapper paCommonMapper;
	
//	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 퀸잇 상품연동결과 저장
	 * 
	 * @param paQeenGoods
	 * @param priceApply
	 * @return
	 */
	@Transactional
	public String saveTransProduct(PaQeenGoodsVO paQeenGoods, PaGoodsPriceApply priceApply,List<PaQeenGoodsdtMapping> goodsdtMapping, List<ItemProposal> itemProposal) {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    
		// 퀸잇 전송 업데이트
		paQeenGoodsMapper.updateResetTrans(paQeenGoods);
		
		// 타겟 업데이트
		PaGoodsTarget goodsTarget = new PaGoodsTarget();
		goodsTarget.setGoodsCode(paQeenGoods.getGoodsCode());
		goodsTarget.setPaCode(paQeenGoods.getPaCode());
		goodsTarget.setPaGroupCode(PaGroup.QEEN.code());
	//	goodsTarget.setPaGoodsCode(paQeenGoods.getRefiedProductId());//refied 너무커서 ProductProposalId로 세팅 하기로 함
		goodsTarget.setPaGoodsCode(paQeenGoods.getProductProposalId());
		goodsTarget.setModifyId(paQeenGoods.getModifyId());
		goodsTargetMapper.updateGoodsTarget(goodsTarget);

		priceApply.setTransId(paQeenGoods.getModifyId());
		
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
		goodsImageMapper.updateCompleteTrans(paQeenGoods.getGoodsCode(), PaGroup.QEEN.code(), paQeenGoods.getModifyId());
				
		// 상품정보고시
		goodsOfferMapper.updateCompleteTrans(paQeenGoods.getGoodsCode(), PaGroup.QEEN.code(), paQeenGoods.getModifyId());
 	    
		for(PaQeenGoodsdtMapping paGoodsdt : goodsdtMapping) {
			String code = paGoodsdt.getPaCode() + "_" + paGoodsdt.getGoodsCode() +"_"+ paGoodsdt.getGoodsdtCode()+ "_" + paGoodsdt.getGoodsdtSeq();
			
			for(ItemProposal item : itemProposal) {
				if(code.equals(item.getCode())) {
					paGoodsdt.setPaOptionCode(item.getReifiedProductItemId());
					paGoodsdt.setProductItemProposalId(String.valueOf(item.getProductItemProposalId()));
					paGoodsdt.setModifyId(paQeenGoods.getModifyId());
					goodsdtMappingMapper.updateCompleteTrans(paGoodsdt);
				}
			}
			
		}
		
		return rtnMsg;
	}
	
	@Transactional
	public String saveTransProposalProduct(PaQeenGoodsVO paQeenGoods, PaGoodsPriceApply priceApply,List<PaQeenGoodsdtMapping> goodsdtMapping, List<ItemProposal> itemProposal) {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		// 퀸잇 임시번호 저장
		paQeenGoodsMapper.updateQeenProposal(paQeenGoods);
		
		for(PaQeenGoodsdtMapping paGoodsdt : goodsdtMapping) {
			String code = paGoodsdt.getPaCode() + "_" + paGoodsdt.getGoodsCode() +"_"+ paGoodsdt.getGoodsdtCode()+ "_" + paGoodsdt.getGoodsdtSeq();
			
			for(ItemProposal item : itemProposal) {
				if(code.equals(item.getCode())) {
					paGoodsdt.setProductItemProposalId(String.valueOf(item.getProductItemProposalId()));
					paGoodsdt.setModifyId(paQeenGoods.getModifyId());
					goodsdtMappingMapper.updateItemProposalId(paGoodsdt);
				}
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

		if (message == null) {
			return false;
		}
		
		// 퀸잇 삭제되는 경우 없다고 함
		
		// 삭제된 상품인 경우
//		if (message.contains("상품이 존재하지 않습니다")) {
//			// 제휴사 상품코드 지운 후 입점 요청처리
//			paQeenGoodsMapper.requestTransTarget(goodsCode,paCode, procId, message);
//			// 옵션코드 클리어
//			goodsdtMappingMapper.clearOptionCode(goodsCode, paCode, procId);
//			return true;
//		}


		return false;
	}
	
	/**
	 * 퀸잇 배송정책 정보 저장
	 * 
	 * @param shipCost
	 * @return
	 */
	@Transactional
	public String updatePaQeenShipCost(PaQeenShipCostVO shipCost) {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		// TPAQEENSHIPCOST UPDATE
		paQeenCommonMapper.updatePaQeenShipCost(shipCost);
 	    
		return rtnMsg;
		
	}
	
	
	/**
	 * 퀸잇 정보고시 항목 저장
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
	 * 퀸잇 정보고시 항목 저장
	 * saveQeenCategoryOffer
	 * @param paOfferCodeList
	 * @return
	 */
	@Transactional
	public String saveQeenCategoryOffer(CategoryOfferList categoryOfferList) {
		String rtnMsg = Constants.SAVE_SUCCESS;

		// TPAOFFERCODE INSERT
		for(CategoryOffer qeenCategoryOffer : categoryOfferList.getCategoryOffer()) {
			int cnt = paQeenCommonMapper.selectQeenCategoryOffer(qeenCategoryOffer);
			if(cnt < 1) {
				paQeenCommonMapper.insertQeenCategoryOffer(qeenCategoryOffer);
			}
		}
 	    
		return rtnMsg;
		
	}

	public String savePaGoodsKinds(List<PaGoodsKinds> paGoodsKindsList) {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		// TPAGOODSKINDS INSERT
		for(PaGoodsKinds paGoodsKinds : paGoodsKindsList) {
			paCommonMapper.insertPaGoodsKinds(paGoodsKinds);
		}
 	    
		return rtnMsg;
		
	}
	
}
