package com.cware.partner.coupang.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cware.partner.common.code.Application;
import com.cware.partner.common.code.PaGroup;
import com.cware.partner.common.code.TransType;
import com.cware.partner.common.domain.ResponseMsg;
import com.cware.partner.common.domain.entity.PaTransService;
import com.cware.partner.common.exception.TransApiException;
import com.cware.partner.common.repository.PaGoodsPriceApplyRepository;
import com.cware.partner.common.service.CodeService;
import com.cware.partner.common.service.CommonService;
import com.cware.partner.common.service.TransLogService;
import com.cware.partner.common.util.StringUtil;
import com.cware.partner.coupang.repository.PaCopnGoodsRepository;
import com.cware.partner.coupang.repository.PaGoodsDtMappingRepository;
import com.cware.partner.sync.domain.entity.PaGoodsDtMapping;
import com.cware.partner.sync.domain.entity.PaGoodsPrice;
import com.cware.partner.sync.domain.entity.PaGoodsPriceApply;
import com.cware.partner.sync.domain.entity.PaPromoTarget;

import lombok.extern.slf4j.Slf4j;

/**
 * 쿠팡 가격 변경
 */
@Slf4j
@Service
public class PriceProductService {

	@Autowired
	PaCopnGoodsRepository copnGoodsRepository;

	@Autowired
	PaGoodsPriceApplyRepository goodsPriceApplyRepository;

	@Autowired
	PaGoodsDtMappingRepository goodsDtMappingRepository;

	@Autowired
	private ProductApiService productApiService;

	@Autowired
	CommonService commonService;

	@Autowired
	CodeService codeService;

	@Autowired
	TransLogService transLogService;


	@Async("optionAsyncExecutor")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public CompletableFuture<Integer> asyncUpdatePriceProduct(String goodsCode, String paCode, String procId,
			long transBatchNo) {
		Integer result = updatePriceProduct(goodsCode, paCode, procId, transBatchNo).getStatus();
		return CompletableFuture.completedFuture(result);
	}

	// 가격변경
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public PaTransService updatePriceProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[COUPANG]쿠팡-가격변경");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.COUPANG.code());
		serviceLog.setProcessId(procId);
		serviceLog = transLogService.logTransServiceStart(serviceLog);

		try {
			// 가격변경
			ResponseMsg result = updatePriceProduct(goodsCode, paCode,  serviceLog.getTransServiceNo());
			serviceLog.setResultCode(result.getCode());
			serviceLog.setResultMsg(result.getMessage());
			serviceLog.setStatus(result.getStatus());
		} catch (TransApiException ex){
			serviceLog.setResultCode(ex.getCode());
			serviceLog.setResultMsg(ex.getMessage());
			serviceLog.setStatus(-1);
		} catch (Exception e) {
			serviceLog.setResultCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			serviceLog.setResultMsg(e.getMessage());
			serviceLog.setStatus(-1);
		}
		transLogService.logTransServiceEnd(serviceLog);
		return serviceLog;

	}

	private ResponseMsg updatePriceProduct(String goodsCode, String paCode, long transServiceNo) {

		Optional<PaGoodsPriceApply> optional = goodsPriceApplyRepository.findGoodsPriceApply(goodsCode,
				PaGroup.COUPANG.code(), paCode);

		ResponseMsg result = new ResponseMsg();

		if (!optional.isPresent()) {
			result.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
			result.setMessage("가격변경 대상이 아닙니다.");
			return result;
		}

		PaGoodsPriceApply priceApply = optional.get();

		// 레거시 데이터를 먼저 체크하여 API연동여부 결정
		// 모든 데이터가 전환되면 레거시 체크하지 않고 무조건 연동되도록 레거시 로직 제거
		PaGoodsPrice price = goodsPriceApplyRepository.findTargetPrice(priceApply);
		PaPromoTarget promo = null;
		PaPromoTarget endPromo = null;

		if (priceApply.getCouponPromoNo() == null) {
			endPromo = 	goodsPriceApplyRepository.findTargetEndPromo(priceApply);
		} else {
			promo = goodsPriceApplyRepository.findTargetPromo(priceApply);
		}

		boolean isTrans = true;

//		if (price != null || promo != null || endPromo != null) {
			// API연동 수행 (데이터 전환 완료후 에는 무조건 연동)
			result = updatePrice(goodsCode, paCode, (long) priceApply.getBestPrice(),
					(long) priceApply.getSalePrice(), transServiceNo);

			isTrans = ApiRequest.API_SUCCESS_CODE.equals(result.getCode());
			// 레거시 데이터 업데이트 (데이터 전환 후에는 제거)
			if (isTrans) {
				if (price != null) goodsPriceApplyRepository.updatePriceTrans(price, Application.ID.code());
				if (endPromo != null) {
					goodsPriceApplyRepository.updatePromoTrans(endPromo, Application.ID.code());
				} else if(promo != null) {
					goodsPriceApplyRepository.updatePromoTrans(promo, Application.ID.code());
				}
			}
//		}

		// 가격적용 전송 설정
		if (isTrans) {
			goodsPriceApplyRepository.updatePriceApplyTrans(priceApply, Application.ID.code());
			result.setStatus(1);
			result.setCode(String.valueOf(HttpStatus.OK.value()));
			result.setMessage("가격변경이 완료되었습니다. 연동가격: "+ String.valueOf(priceApply.getBestPrice()));
		}

		return result;
	}

	/**
	 * 상품가격변경 연동
	 *
	 * @param goodsCode     상품코드
	 * @param price         최종가격
	 * @param originalPrice 판매가
	 * @param paCode
	 * @return boolean 상품 가격변경 성공여부
	 */
	private ResponseMsg updatePrice(String goodsCode, String paCode, long price, long originalPrice, long transServiceNo) {

		List<PaGoodsDtMapping> itemList = goodsDtMappingRepository
				.findByGoodsCodeAndPaCodeAndPaOptionCodeIsNotNull(goodsCode, paCode);

		ResponseMsg result = new ResponseMsg();

		if (itemList.size() == 0) {
			result.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
			result.setMessage("가격변경 대상이 아닙니다.");
		}

		try {
			for (PaGoodsDtMapping item : itemList) {
				log.info("가격변경 상품:{} 단품:{} 옵션ID:{} 최종가격:{} 기준가격:{}", goodsCode, item.getGoodsdtCode(),
						item.getPaOptionCode(), price, originalPrice);
				result = productApiService.updateItemPrice(goodsCode, item.getPaOptionCode(), price, paCode,
						transServiceNo);
				log.info("가격변경성공 상품:{} 단품:{} {}", goodsCode, item.getGoodsdtCode(), result);
				result = productApiService.updateItemOriginalPrice(goodsCode, item.getPaOptionCode(), originalPrice, paCode,
						transServiceNo);
				log.info("기준가격변경성공 상품:{} 단품:{} {}", goodsCode, item.getGoodsdtCode(), result);
			}

		} catch (TransApiException ex) {
			result.setStatus(-1);
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			log.error("가격변경실패 상품:{} {}", goodsCode, result);
			// 수기중단처리
			copnGoodsRepository.stopSaleForReject(goodsCode, paCode, Application.ID.code(),
					StringUtil.truncate(result.getMessage(), 500));
		}

		return result;
	}

}
