package com.cware.partner.coupang.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cware.partner.common.code.PaGroup;
import com.cware.partner.common.code.TransType;
import com.cware.partner.common.domain.ResponseMsg;
import com.cware.partner.common.domain.entity.PaTransService;
import com.cware.partner.common.exception.TransApiException;
import com.cware.partner.common.service.CodeService;
import com.cware.partner.common.service.CommonService;
import com.cware.partner.common.service.TransLogService;
import com.cware.partner.coupang.repository.PaGoodsDtMappingRepository;
import com.cware.partner.sync.domain.entity.PaGoodsDtMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * 쿠팡 옵션 재고변경
 */
@Slf4j
@Service
public class StockProductService {

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

	/**
	 * 재고변경
	 *
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	@Async("optionAsyncExecutor")
	public CompletableFuture<Integer> asyncUpdateStockProduct(String goodsCode, String paCode, String procId,
			long transBatchNo) {
		Integer result = updateStockProduct(goodsCode, paCode, procId, transBatchNo).getStatus();
		return CompletableFuture.completedFuture(result);
	}


	// 재고변경
	public PaTransService updateStockProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[COUPANG]쿠팡-재고변경");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.COUPANG.code());
		serviceLog.setProcessId(procId);
		serviceLog = transLogService.logTransServiceStart(serviceLog);

		try {
			// 재고업데이트
			ResponseMsg result = updateStock(goodsCode, paCode, procId, serviceLog.getTransServiceNo());
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

	private ResponseMsg updateStock(String goodsCode, String paCode, String procId, long transServiceNo) {

		ResponseMsg result = new ResponseMsg();

		double stockRate = codeService.getStockRate(paCode);

		List<PaGoodsDtMapping> itemList = goodsDtMappingRepository.findUpdateStock(goodsCode, paCode);

		if (itemList.size() == 0) {
			result.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
			result.setMessage("재고변경 대상 건이 아닙니다.");
			return result;
		}

		try {
			for (PaGoodsDtMapping item : itemList) {
				if (item.getTransOrderAbleQty() > 0) {
					item.setTransOrderAbleQty((int) Math.ceil(item.getTransOrderAbleQty() * stockRate));
				}
				log.info("재고변경 상품:{} 단품:{} 옵션ID:{} 재고:{}", goodsCode, item.getGoodsdtCode(), item.getPaOptionCode(), item.getTransOrderAbleQty());
				result = productApiService.updateItemQuantity(goodsCode, item.getPaOptionCode(), item.getTransOrderAbleQty(), paCode, transServiceNo);
				log.info("재고변경성공 상품:{} 단품:{} {}", goodsCode, item.getGoodsdtCode(), result);
				goodsDtMappingRepository.updateTransStock(goodsCode, item.getGoodsdtCode(), paCode, item.getTransOrderAbleQty(), procId);
			}
			result.setStatus(1);
			result.setCode(String.valueOf(HttpStatus.OK.value()));
		} catch (TransApiException ex) {
			result.setStatus(-1);
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			log.error("재고변경실패 상품:{} {}", goodsCode, result);
		}

		return result;
	}


}
