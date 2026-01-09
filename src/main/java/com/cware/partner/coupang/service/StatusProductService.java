package com.cware.partner.coupang.service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cware.partner.common.code.Application;
import com.cware.partner.common.code.ApprovalStatus;
import com.cware.partner.common.code.PaGroup;
import com.cware.partner.common.code.TransType;
import com.cware.partner.common.domain.ResponseMsg;
import com.cware.partner.common.domain.entity.PaTransService;
import com.cware.partner.common.exception.TransApiException;
import com.cware.partner.common.service.CodeService;
import com.cware.partner.common.service.CommonService;
import com.cware.partner.common.service.TransLogService;
import com.cware.partner.coupang.domain.Item;
import com.cware.partner.coupang.domain.Product;
import com.cware.partner.coupang.repository.PaCopnGoodsRepository;
import com.cware.partner.coupang.repository.PaGoodsDtMappingRepository;
import com.cware.partner.sync.domain.entity.PaCopnGoods;

import lombok.extern.slf4j.Slf4j;

/**
 * 쿠팡 상품상태/코드 매핑
 */
@Slf4j
@Service
public class StatusProductService {

	@Autowired
	PaCopnGoodsRepository copnGoodsRepository;

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
	public CompletableFuture<Integer> asyncUpdateStatusProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		Integer result = updateStatusProduct(goodsCode, paCode, procId, transBatchNo).getStatus();
		return CompletableFuture.completedFuture(result);
	}

	@Async("optionAsyncExecutor")
	public CompletableFuture<Integer> asyncUpdateOptionProduct(String goodsCode, String sellerProductId,
			String currentStatus, String paCode, String procId, long transBatchNo) {
		Integer result = updateOptionProduct(goodsCode, sellerProductId, currentStatus, paCode, procId, transBatchNo);
		return CompletableFuture.completedFuture(result);
	}

	public PaTransService updateStatusProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[COUPANG]쿠팡-상품상태업데이트");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.COUPANG.code());
		serviceLog.setProcessId(procId);
		serviceLog = transLogService.logTransServiceStart(serviceLog);

		try {
			// 상품상태업데이트
			ResponseMsg result = updateStatusProduct(goodsCode, paCode,  serviceLog.getTransServiceNo());
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

	public int updateOptionProduct(String goodsCode, String sellerProductId, String currentStatus, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[COUPANG]쿠팡-옵션코드매핑");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.COUPANG.code());
		serviceLog.setProcessId(procId);
		serviceLog = transLogService.logTransServiceStart(serviceLog);

		try {
			// 옵션코드업데이트
			ResponseMsg result = updateOptionProduct(goodsCode, sellerProductId, currentStatus, paCode,  serviceLog.getTransServiceNo());
			serviceLog.setResultCode(result.getCode());
			serviceLog.setResultMsg(result.getMessage());
			transLogService.logTransServiceEnd(serviceLog);
			return result.getStatus();
		} catch (TransApiException ex){
			serviceLog.setResultCode(ex.getCode());
			serviceLog.setResultMsg(ex.getMessage());
		} catch (Exception e) {
			serviceLog.setResultCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			serviceLog.setResultMsg(e.getMessage());
		}
		transLogService.logTransServiceEnd(serviceLog);

		return -1;

	}

	// 상품상태업데이트
	private ResponseMsg updateStatusProduct(String goodsCode, String paCode, long transServiceNo) {

		ResponseMsg result = new ResponseMsg();

		Optional<PaCopnGoods> optional = copnGoodsRepository
				.findByGoodsCodeAndPaCodeAndSellerProductIdIsNotNull(goodsCode, paCode);

		if (!optional.isPresent()) {
			result.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
			result.setMessage("입점된 상품이 아닙니다.");
			return result;
		}

		PaCopnGoods copnGoods = optional.get();

		Product product = productApiService.getProduct(goodsCode, copnGoods.getSellerProductId(), paCode, transServiceNo);
		String approvalStatus = codeService.getApprovalStatus(product.getStatusName());
		log.info("상품:{} {} 승인상태:{} -> {}({})", goodsCode, copnGoods.getSellerProductId(), copnGoods.getApprovalStatus(),
				product.getStatusName(), approvalStatus);


		if (approvalStatus != null && !approvalStatus.equals(copnGoods.getApprovalStatus())) {
			result.setMessage(product.getStatusName());
			if (ApprovalStatus.COMPLETE.code().equals(approvalStatus)) {
				for (Item item : product.getItems()) {
					String goodsdtCode = item.getExternalVendorSku().replace(goodsCode, "");
					log.info("상품:{} 단품:{} 옵션ID:{} 아이템ID:{}", goodsCode, goodsdtCode, item.getVendorItemId(),
							item.getSellerProductItemId());
					if (item.getVendorItemId() != null) {
						if (goodsDtMappingRepository.updateOptionCode(goodsCode, goodsdtCode, paCode, Application.ID.code(),
								item.getVendorItemId(), item.getSellerProductItemId(), product.getProductId()) < 1) {
							result.setStatus(-1);
							result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
							result.setMessage(result.getMessage() + " 옵션코드 업데이트 실패");
							log.error("상품:{} 단품:{} {}", goodsCode, goodsdtCode, result.getMessage());
							return result;
						}
					}
				}
			}
			if (copnGoodsRepository.updateApprovalStatus(goodsCode, paCode, Application.ID.code(),
					approvalStatus) < 1) {
				result.setStatus(-1);
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				result.setMessage("업데이트 실패");
				log.error("상품:{} {}", goodsCode, result.getMessage());
				return result;
			}
		} else {
			result.setStatus(0);
			result.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
			result.setMessage("상태가 변경되지 않았습니다.");
			log.info("상품:{} {}", goodsCode, result.getMessage());
			return result;
		}

		result.setStatus(1);
		result.setCode(String.valueOf(HttpStatus.OK.value()));
		result.setMessage("상태가 업데이트 되었습니다.");

		return result;
	}


	// 옵션코드매핑
	private ResponseMsg updateOptionProduct(String goodsCode, String sellerProductId, String currentStatus, String paCode, long transServiceNo) {

		Product product = productApiService.getProduct(goodsCode, sellerProductId, paCode, transServiceNo);
		String approvalStatus = codeService.getApprovalStatus(product.getStatusName());
		log.info("상품:{} {} 승인상태:{} -> {}({})", goodsCode, sellerProductId, currentStatus,
				product.getStatusName(), approvalStatus);

		ResponseMsg result = new ResponseMsg();

		if (ApprovalStatus.COMPLETE.code().equals(approvalStatus)) {
			for (Item item : product.getItems()) {
				String goodsdtCode = item.getExternalVendorSku().replace(goodsCode, "");
				log.info("상품:{} 단품:{} 옵션ID:{} 아이템ID:{}", goodsCode, goodsdtCode, item.getVendorItemId(),
						item.getSellerProductItemId());
				if (item.getVendorItemId() != null) {
					if (goodsDtMappingRepository.updateOptionCode(goodsCode, goodsdtCode, paCode, Application.ID.code(),
							item.getVendorItemId(), item.getSellerProductItemId(), product.getProductId()) < 1) {
						result.setStatus(-1);
						result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
						result.setMessage("옵션코드 업데이트 실패");
						log.error("상품:{} 단품:{} {}", goodsCode, goodsdtCode, result.getMessage());
						return result;
					}
				}
			}
		} else {
			result.setStatus(0);
			result.setMessage("승인완료 상태가 아닙니다.");
			log.info("상품:{} {}", goodsCode, result.getMessage());
			return result;
		}

		result.setStatus(1);
		result.setCode(String.valueOf(HttpStatus.OK.value()));
		result.setMessage(ApiRequest.API_SUCCESS_CODE);

		return result;
	}

}
