package com.cware.netshopping.palton.v2.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.domain.PaLtonGoodsVO;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsTarget;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsImageMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsOfferMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.palton.v2.repository.PaLtonGoodsMapper;
import com.cware.netshopping.palton.v2.repository.PaLtonGoodsdtMappingMapper;

@Service
public class PaLtonResultService {

	@Autowired
	PaLtonGoodsMapper paLtonGoodsMapper;
	
	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;

	@Autowired
	PaGoodsImageMapper goodsImageMapper;

	@Autowired
	PaGoodsOfferMapper goodsOfferMapper;
	
	@Autowired
	PaLtonGoodsdtMappingMapper goodsdtMappingMapper;

	@Autowired
	TransLogService transLogService;

//	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 롯데온 상품연동결과 저장
	 * 
	 * @param paLtonGoods
	 * @param priceApply
	 * @param goodsOptionList
	 * @return
	 */
	@Transactional
	public String saveTransProduct(PaLtonGoodsVO paLtonGoods, PaGoodsPriceApply priceApply, List<PaLtonGoodsdtMappingVO> goodsOptionList) {
		String rtnMsg = Constants.SAVE_SUCCESS;
 	    
 	    // 롯데온 전송 업데이트
 	   	paLtonGoodsMapper.updateResetTrans(paLtonGoods);

		// 타겟 업데이트
		PaGoodsTarget goodsTarget = new PaGoodsTarget();
		goodsTarget.setGoodsCode(paLtonGoods.getGoodsCode());
		goodsTarget.setPaCode(paLtonGoods.getPaCode());
		goodsTarget.setPaGroupCode(paLtonGoods.getPaGroupCode());
		goodsTarget.setPaGoodsCode(paLtonGoods.getSpdNo());
		goodsTarget.setModifyId(paLtonGoods.getModifyId());
		goodsTargetMapper.updateGoodsTarget(goodsTarget);

		priceApply.setTransId(paLtonGoods.getModifyId());
		
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
		goodsImageMapper.updateCompleteTrans(paLtonGoods.getGoodsCode(), PaGroup.LOTTEON.code(), paLtonGoods.getModifyId());
		// 상품정보고시
		goodsOfferMapper.updateCompleteTrans(paLtonGoods.getGoodsCode(), PaGroup.LOTTEON.code(), paLtonGoods.getModifyId());
 	    
 	    // 단품 업데이트
		for (PaLtonGoodsdtMappingVO goodsdt : goodsOptionList) {
			goodsdt.setModifyId(paLtonGoods.getModifyId());
			goodsdtMappingMapper.updateCompleteTrans(goodsdt);
		}
		
		// 전송 비대상 단품 리셋
		goodsdtMappingMapper.updateResetTrans(paLtonGoods.getGoodsCode(), paLtonGoods.getPaCode(), paLtonGoods.getModifyId());
		
		return rtnMsg;
	}

    /**
     * 변경된 표준카테고리와 전시카테고리 재매핑
     * 
     * @param paLtonGoods
     * @param procId
     * @param message
     * @return
     */
    public boolean applyCategoryMapping(PaLtonGoodsVO paLtonGoods, String procId, String message) {

        if (message == null)
            return false;

        Pattern pattern = Pattern.compile("표준카테고리번호[\\[](.*?)[\\]]에 맵핑된 전시카테고리번호가 아닙니다");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) { 
            String stdCatId = matcher.group(1);
            
            if (StringUtils.hasText(stdCatId) && !stdCatId.equals(paLtonGoods.getScatNo())) {
                // 표준/전시카테고리 재매핑
                paLtonGoodsMapper.mappingCategory(paLtonGoods.getGoodsCode(),
                        paLtonGoods.getPaCode(), procId, message, stdCatId);
                return true;
            }
        }
        
        return false;
    }

    /**
     * 제외 브랜드인 경우 브랜드매핑 지움
     * 
     * @param goodsCode
     * @param paCode
     * @param procId
     * @param message
     * @return
     */
    public boolean applyResetBrand(String goodsCode, String paCode, String procId, String message) {

        if (message == null)
            return false;

      // 제외브랜드인 경우
      if (message.contains("등록이 불가한 브랜드")) {
          // 브랜드 매핑 삭제
          paLtonGoodsMapper.resetBrand(goodsCode,paCode, procId, message);
          return true;
      }

        return false;
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

//		// 삭제된 상품인 경우
//		if (message.equals("데이터가 없습니다.")) {
//			// 제휴사 상품코드 지운 후 입점 요청처리
//			paLtonGoodsMapper.requestTransTarget(goodsCode,paCode, procId, message);
//			return true;
//		}

		return false;
	}
}
