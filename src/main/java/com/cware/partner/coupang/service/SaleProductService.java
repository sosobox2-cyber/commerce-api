package com.cware.partner.coupang.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cware.partner.common.code.PaGroup;
import com.cware.partner.common.code.PaSaleGb;
import com.cware.partner.common.code.PaStatus;
import com.cware.partner.common.code.TransType;
import com.cware.partner.common.domain.ProductTrans;
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
import com.cware.partner.sync.domain.entity.PaCopnGoods;
import com.cware.partner.sync.domain.entity.PaGoodsDtMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * 쿠팡 상품 판매재개/중단
 */
@Slf4j
@Service
public class SaleProductService {

	@Autowired
	PaCopnGoodsRepository copnGoodsRepository;

	@Autowired
	PaGoodsPriceApplyRepository goodsPriceApplyRepository;

	@Autowired
	PaGoodsDtMappingRepository goodsDtMappingRepository;

	@Autowired
	PartnerProductService partnerProductService;

	@Autowired
	StatusProductService statusProductService;

	@Autowired
	PriceProductService priceProductService;

	@Autowired
	StockProductService stockProductService;

	@Autowired
	private ProductApiService productApiService;

	@Autowired
	CommonService commonService;

	@Autowired
	CodeService codeService;

	@Autowired
	TransLogService transLogService;


	/**
	 * 판매재개 (가격변경 -> 상품수정 -> 재고변경 -> 판매재개)
	 *
	 * @param goodsCode
	 * @param transTargetYn
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@Async("optionAsyncExecutor")
	public CompletableFuture<ProductTrans> asyncResumeSaleProduct(String goodsCode, String transTargetYn, String paCode, String procId,
			long transBatchNo) {

		Integer result = 0;
		boolean isUpdated = false;

		// 가격변경
		result = priceProductService.updatePriceProduct(goodsCode, paCode, procId, transBatchNo).getStatus();
		if (result >= 0) {
			// 재고변경
			stockProductService.updateStockProduct(goodsCode, paCode, procId, transBatchNo);

			if ( "1".equals(transTargetYn)) {
				// 상품수정
				int status = partnerProductService.updateProduct(goodsCode, paCode, transBatchNo);
				if (status > 0) isUpdated = true;
			}

			// 판매재개
			result = resumeSaleProduct(goodsCode, paCode, procId, transBatchNo).getStatus();
		}

		ProductTrans trans = ProductTrans.builder().goodsCode(goodsCode).paCode(paCode).procCnt(result)
				.updated(isUpdated).build();

		return CompletableFuture.completedFuture(trans);
	}

	/**
	 * 판매중지
	 *
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@Async("optionAsyncExecutor")
	public CompletableFuture<Integer> asyncStopSaleProduct(String goodsCode, String paCode, String procId,
			long transBatchNo) {
		Integer result = stopSaleProduct(goodsCode, paCode, procId, transBatchNo).getStatus();
		return CompletableFuture.completedFuture(result);
	}


	// 판매재개
	public PaTransService resumeSaleProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[COUPANG]쿠팡-판매재개");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.COUPANG.code());
		serviceLog.setProcessId(procId);
		serviceLog = transLogService.logTransServiceStart(serviceLog);

		try {
			// 판매재개
			ResponseMsg result = resumeSale(goodsCode, paCode, procId, serviceLog.getTransServiceNo());
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

	// 판매중단
	public PaTransService stopSaleProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[COUPANG]쿠팡-판매중단");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.COUPANG.code());
		serviceLog.setProcessId(procId);
		serviceLog = transLogService.logTransServiceStart(serviceLog);

		try {
			// 판매중단
			ResponseMsg result = stopSale(goodsCode, paCode, procId, serviceLog.getTransServiceNo());
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

	private ResponseMsg resumeSale(String goodsCode, String paCode, String procId, long transServiceNo) {

		ResponseMsg result = new ResponseMsg();

		Optional<PaCopnGoods> optional = copnGoodsRepository
				.findByGoodsCodeAndPaCodeAndPaSaleGbAndPaStatusAndTransSaleYn(goodsCode, paCode,
						PaSaleGb.FORSALE.code(), PaStatus.COMPLETE.code(), "1");

		if (!optional.isPresent()) {
			result.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
			result.setMessage("판매재개 대상 상품이 아닙니다.");
			return result;
		}

		List<PaGoodsDtMapping> itemList = goodsDtMappingRepository
				.findByGoodsCodeAndPaCodeAndPaOptionCodeIsNotNull(goodsCode, paCode);

		try {
			for (PaGoodsDtMapping item : itemList) {
				log.info("판매재개 상품: {} 단품: {} 옵션ID: {}", goodsCode, item.getGoodsdtCode(), item.getPaOptionCode());
				result = productApiService.resumeSaleItem(goodsCode, item.getPaOptionCode(), paCode, transServiceNo);
				log.info("판매재개성공 상품: {} 단품: {} {}", goodsCode, item.getGoodsdtCode(), result);
			}
			copnGoodsRepository.updateTransSaleTarget(goodsCode, paCode, procId);
			result.setStatus(1);
			result.setCode(String.valueOf(HttpStatus.OK.value()));
		} catch (TransApiException ex) {
			result.setStatus(-1);
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			log.error("판매재개실패 상품: {} {}", goodsCode, result);
			// 수기중단처리
			copnGoodsRepository.stopSaleForReject(goodsCode, paCode, procId,
					StringUtil.truncate(result.getMessage(), 500));
		}

		return result;
	}

	private ResponseMsg stopSale(String goodsCode, String paCode, String procId, long transServiceNo) {

		ResponseMsg result = new ResponseMsg();

		Optional<PaCopnGoods> optional = copnGoodsRepository.findStopSale(goodsCode, paCode);

		if (!optional.isPresent()) {
			result.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
			result.setMessage("판매중지 대상 상품이 아닙니다.");
			return result;
		}

		List<PaGoodsDtMapping> itemList = goodsDtMappingRepository
				.findByGoodsCodeAndPaCodeAndPaOptionCodeIsNotNull(goodsCode, paCode);

		try {
			for (PaGoodsDtMapping item : itemList) {
				log.info("판매중단 상품: {} 단품: {} 옵션ID: {}", goodsCode, item.getGoodsdtCode(), item.getPaOptionCode());
				result = productApiService.stopSaleItem(goodsCode, item.getPaOptionCode(), paCode, transServiceNo);
				log.info("판매중단성공 상품: {} 단품: {} {}", goodsCode, item.getGoodsdtCode(), result);
			}
			copnGoodsRepository.updateTransSaleTarget(goodsCode, paCode, procId);
			result.setStatus(1);
			result.setCode(String.valueOf(HttpStatus.OK.value()));
		} catch (TransApiException ex) {
			result.setStatus(-1);
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			log.error("판매중단실패 상품: {} {}", goodsCode, result);
		}

		return result;
	}


}
