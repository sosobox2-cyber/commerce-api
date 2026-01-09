package com.cware.netshopping.pagmkt.v2.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.PaSaleGb;
import com.cware.netshopping.common.code.PaStatus;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.model.PaGmktGoods;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsTarget;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsImageMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsOfferMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsdtMappingMapper;
import com.cware.netshopping.pagmkt.v2.domain.EbayGoods;
import com.cware.netshopping.pagmkt.v2.repository.PaGmktGoodsMapper;

@Service
public class PaEbayResultService {

	@Autowired
	PaGmktGoodsMapper gmktGoodsMapper;
	
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

	@Autowired
	EbayProductService  ebayProductService;

//	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 이베이 상품연동결과 저장
	 * 
	 * @param ebayGoods
	 * @param goodsOptionList
	 * @return
	 */
	@Transactional
	public String saveTransProduct(EbayGoods ebayGoods, List<PaGoodsdtMapping> goodsOptionList) {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    
 	   PaGoodsPriceApply priceApply = null;
 	    
 	    // 이베이 전송 업데이트
 	    // 지마켓
 	    if (ebayGoods.isGmkt()) {
 	    	priceApply = ebayGoods.getGmktPrice();
 	    	saveProductBySite(ebayGoods, ebayGoods.getGmktGoods(), priceApply, ebayGoods.isStartGmkt());
 	    }
 	    // 옥션
 	    if (ebayGoods.isIac()) {
 	    	priceApply = ebayGoods.getIacPrice();
 	    	saveProductBySite(ebayGoods, ebayGoods.getIacGoods(), priceApply, ebayGoods.isStartIac());
 	    }

		// 레거시 가격 데이터 (공통)
		goodsPriceMapper.updatePriceTrans(priceApply);
		
		// 상품이미지
		goodsImageMapper.updateCompleteTrans(ebayGoods.getGoodsCode(), PaGroup.GMARKET.code(), ebayGoods.getModifyId());
		// 상품정보고시
		goodsOfferMapper.updateCompleteTrans(ebayGoods.getGoodsCode(), PaGroup.GMARKET.code(), ebayGoods.getModifyId());
 	    
 	    // 단품 업데이트
		for (PaGoodsdtMapping goodsdt : goodsOptionList) {
			goodsdt.setModifyId(ebayGoods.getModifyId());
			goodsdtMappingMapper.updateCompleteTransForEbay(goodsdt);
		}
		// 전송 비대상 단품 리셋
		goodsdtMappingMapper.updateResetTrans(ebayGoods.getGoodsCode(), ebayGoods.getPaCode(), ebayGoods.getModifyId());
		
		return rtnMsg;
	}

	private void saveProductBySite(EbayGoods ebayGoods, PaGmktGoods gmktGoods, PaGoodsPriceApply priceApply, boolean isStart) {
		gmktGoods.setEsmGoodsCode(ebayGoods.getEsmGoodsCode());
		gmktGoods.setTransOrderAbleQty(ebayGoods.getTransOrderAbleQty());
		gmktGoods.setModifyId(ebayGoods.getModifyId());
		priceApply.setTransId(ebayGoods.getModifyId());

		if (isStart) {
			// 상품입점
			if (PaStatus.COMPLETE.code().equals(gmktGoods.getPaStatus())) {
				gmktGoodsMapper.updateCompleteTrans(gmktGoods);
			} else {
				// 이베이 정책에 의해 반려된 경우 판매중지 처리하여 이후 수정 연동시 대상에서 제외
				if (gmktGoods.getReturnNote() != null && (gmktGoods.getReturnNote().contains("거부")
						|| gmktGoods.getReturnNote().contains("권한신청") || gmktGoods.getReturnNote().contains("직거래를 유도")
						|| gmktGoods.getReturnNote().contains("필수선택명") || gmktGoods.getReturnNote().contains("상품 고시 필수 정보"))) {
					gmktGoods.setPaSaleGb(PaSaleGb.SUSPEND.code());
					// 자동입점요청 대상에서 제외
					goodsTargetMapper.disableAutoYn(gmktGoods.getGoodsCode(), gmktGoods.getPaCode(),
							gmktGoods.getPaGroupCode(), gmktGoods.getModifyId());
				}
				gmktGoodsMapper.updateRejectTrans(gmktGoods);
				return;
			}
		} else {
			// 전송 리셋처리
			gmktGoodsMapper.updateResetTrans(gmktGoods);
		}

		// 타겟 업데이트
		PaGoodsTarget goodsTarget = new PaGoodsTarget();
		goodsTarget.setGoodsCode(gmktGoods.getGoodsCode());
		goodsTarget.setPaCode(gmktGoods.getPaCode());
		goodsTarget.setPaGroupCode(gmktGoods.getPaGroupCode());
		goodsTarget.setPaGoodsCode(gmktGoods.getItemNo());
		goodsTarget.setModifyId(gmktGoods.getModifyId());
		goodsTargetMapper.updateGoodsTarget(goodsTarget);
		
		// 프로모션개선
		goodsPriceMapper.updatePriceApplyTrans(priceApply);
		// 레거시 프로모션 데이터
		if (priceApply.getCouponPromoNo() == null) {
			goodsPriceMapper.updateDelPromoTrans(priceApply);
		} else {
			goodsPriceMapper.updatePromoTrans(priceApply);
		}
	}

	public int saveRejectTrans(EbayGoods ebayGoods, String rejectMsg) {
		
		if (!StringUtils.hasText(rejectMsg)) return 0;

		String[] rejectMatch = new String[] { 
				"배송비를 10,000원", 
				"판매제한 키워드에", 
//				"상품 정보 수정에 실패하였습니다",
				"선택형(Independent) 항목은 20개까지",
				"상품번호가 생성 되지", 
				"주문옵션에는 특수문자", 
				"시퀀스에 둘 이상의",
				"최대 50byte까지", 
//				"BisnessError",
				"상품 노출이 제한", "상품 번호가 잘 못 되었습니다", 
				"해당 물품의 정보를 변경 할 수 없습니다",
				"조회할 수 없는 상품",
				"주문옵션 항목값이 동일한 행이 존재",
				"이미지 사이즈가 500X500 이상이어야 합니다",
				"소비자 피해 방지 차원에서 등록이 제한",
				"등록된 물품이 아닙니다",
				"다른 문구를 넣어 주십시오",
				"필수선택에 중복된 항목과 선택값이 존재합니다",
//				"상품 정보가 부정확합니다.", // 보통은 지마켓에서 판매불가 처리한 건 수정시 발생하나, 입점 후 수정시 발생하기도 함. 시간 경과 후 정상 처리됨
				"판매가 대비 70%까지 입력이 가능합니다",
				"상품정보고시 항목 입력 길이가 1000byte를 초과",
				"선택형(Independent) 상세정보(Details) 항목은 항목별로 50개까지",
				"상품 기본정보 조회 불가",
				"권한신청이 필요합니다",
				"중복된 옵션명의 옵션값이 존재합니다",
				"등록 불가 카테고리 입니다",
				"필수 정보가 입력되지 않았거나",
				"허용되지 않은 문자",
				"이상 입력할 수 없습니다",
				"금칙어",
				"직권중지(제한) 상품은 수정 불가",
				"거부 사유 확인 후 재신청",
				"상품 고시 필수 정보가 입력되지 않았습니다",
				"필수선택명에 '선택안함' 또는 '선택없음'으로는 입력하실 수 없습니다",
				"인코딩",
				"상품 등록이 정상적으로 이루어 지지 않았습니다",
				"도서 정가를 입력"
				};

 	    if (!Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s))) return 0;

		int result = 0;
		
 	    // 이베이 전송 업데이트
 	    // 지마켓
 	    if (ebayGoods.isGmkt()) {
 	    	ebayGoods.getGmktGoods().setModifyId(ebayGoods.getModifyId());
 	    	ebayGoods.getGmktGoods().setReturnNote(StringUtil.truncate(rejectMsg, 500));
 	    	if (ebayGoods.isStartGmkt()) {
 	    		result = gmktGoodsMapper.updateRejectTrans(ebayGoods.getGmktGoods()); // 입점 반려
 	    	} else {
 	    		result = applyRetention(ebayGoods.getGmktGoods());
 	    		if (result == 0) result = gmktGoodsMapper.updateStopTrans(ebayGoods.getGmktGoods()); // 수기중단
 	    	}
 	    }
 	    // 옥션
 	    if (ebayGoods.isIac()) {
 	    	ebayGoods.getIacGoods().setModifyId(ebayGoods.getModifyId());
 	    	ebayGoods.getIacGoods().setReturnNote(StringUtil.truncate(rejectMsg, 500));
 	    	if (ebayGoods.isStartIac()) {
 	    		result = gmktGoodsMapper.updateRejectTrans(ebayGoods.getIacGoods()); // 입점 반려
 	    	} else {
 	    		result = applyRetention(ebayGoods.getIacGoods());
 	    		if (result == 0) result = gmktGoodsMapper.updateStopTrans(ebayGoods.getIacGoods()); // 수기중단
 	    	}
 	    }
				
		return result;
	}

	public int extendPeriod(EbayGoods ebayGoods, String rejectMsg) {
		
		if (!StringUtils.hasText(rejectMsg)) return 0;

		String[] rejectMatch = new String[] { 
				"Period.ApplyPeriod of any child element of Selling.PeriodCollection cannot be negative", 
				"상품 정보 수정에 실패하였습니다"
				};

 	    if (!Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s))) return 0;

		// 판매기한연장
		PaTransService transResult = ebayProductService.sellingPeriodProduct(ebayGoods.getGoodsCode(), ebayGoods.getPaCode(), ebayGoods.getModifyId());
				
		return transResult.getStatus();
	}

	/**
	 * 삭제된 상품인 경우 입점 요청 처리
	 * 
	 * @param gmktGoods
	 * @return
	 */
	public int applyRetention(PaGmktGoods gmktGoods) {
		
		if (gmktGoods.getReturnNote() == null) return 0;
		
		// 삭제된 상품인 경우
		if (gmktGoods.getReturnNote().indexOf("조회할 수 없는 상품 입니다") > -1
				|| gmktGoods.getReturnNote().indexOf("상품 번호가 잘 못 되었습니다.(GoodsMaster)") > -1
				|| gmktGoods.getReturnNote().indexOf("상품 등록이 정상적으로 이루어 지지 않았습니다") > -1
				) {			
			// 제휴사 상품코드 지운 후 입점 요청처리
			gmktGoodsMapper.requestTransTarget(gmktGoods);
			return 1;
		}
		
		return 0;
	}
}
