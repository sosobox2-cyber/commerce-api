package com.cware.netshopping.paqeen.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.PaSaleGb;
import com.cware.netshopping.common.code.PaStatus;
import com.cware.netshopping.common.code.SaleGb;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.PaQeenGoodsVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaQeenGoodsdtMapping;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.service.SyncProductService;
import com.cware.netshopping.paqeen.domain.Classification;
import com.cware.netshopping.paqeen.domain.ItemProposal;
import com.cware.netshopping.paqeen.domain.Measurement;
import com.cware.netshopping.paqeen.domain.Price;
import com.cware.netshopping.paqeen.domain.Product;
import com.cware.netshopping.paqeen.domain.ProductProposal;
import com.cware.netshopping.paqeen.domain.ProductResponse;
import com.cware.netshopping.paqeen.message.ProductConfirmResoponseMsg;
import com.cware.netshopping.paqeen.repository.PaQeenGoodsMapper;
import com.cware.netshopping.paqeen.util.PaQeenApiRequest;
import com.cware.netshopping.paqeen.util.PaQeenConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class PaQeenProductService {
	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	TransLogService transLogService;
	
	@Autowired
	PaQeenGoodsMapper paQeenGoodsMapper;
	
	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;
	
	@Autowired
	PaQeenConnectUtil paQeenConnectUtil;
	
	@Autowired
	PaQeenResultService paQeenResultService;
	
	@Autowired
	PaQeenApiRequest paQeenApiRequest;
	
	@Autowired
	SyncProductService syncProductService;
	
	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;
	
	@Autowired
	QeenProductService qeenProductService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public ResponseMsg registerProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PAQEENAPI_01_001");
		serviceLog.setServiceNote("[API]퀸잇-상품 등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.QEEN.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 상품입점
		ResponseMsg result = callRegisterProduct(goodsCode, paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	public ResponseMsg updateProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PAQEENAPI_01_002");
		serviceLog.setServiceNote("[API]퀸잇-상품 수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.QEEN.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// 상품수정
		ResponseMsg result = callUpdateProduct(goodsCode, paCode, procId, serviceLog);
		
		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);
		
		return result;
	}
	
	public ProductConfirmResoponseMsg confirmProduct(String goodsCode, Integer productProposalId, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PAQEENAPI_01_003");
		serviceLog.setServiceNote("[API]퀸잇-상품 승인");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.QEEN.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// 상품승인
		ProductConfirmResoponseMsg result = callConfirmProduct(goodsCode, productProposalId ,paCode, procId, serviceLog);
		
		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);
		
		return result;
	}
	
	
	
	/**
	 * 퀸잇 상품 개별 전송
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public ResponseMsg transProduct(String goodsCode, String paCode, String procId) {
		ResponseMsg result = new ResponseMsg("", "");
		
		// 상품동기화
		PaGoodsSync sync = syncProductService.syncProduct(goodsCode, PaGroup.QEEN.code(), procId);
		result = sync.getResponseMsg();
		if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) {
			return result;
		}
		PaQeenGoodsVO paQeenGoods = paQeenGoodsMapper.getGoods(goodsCode,paCode);
		
		if(paQeenGoods == null) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("연동 대상 상품이 아닙니다.");
			return result;
		}

		// 입점요청중인건은 처리하지 않음
		if (PaStatus.PROCEEDING.code().equals(paQeenGoods.getPaStatus())) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("입점요청중인 상품입니다.");
			return result;
		}
		
		
		// 입점요청/입점반려건에 대해서 신규입점 요청
		if (PaStatus.REQUEST.code().equals(paQeenGoods.getPaStatus())
				|| PaStatus.REJECT.code().equals(paQeenGoods.getPaStatus())) {

			// 동기화에서 필터링된 경우
			if (String.valueOf(HttpStatus.NO_CONTENT.value()).equals(result.getCode())) {
				return result;
			}

			if (goodsTargetMapper.existsGoodsTarget(goodsCode, paCode, PaGroup.QEEN.code()) < 1) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
				result.setMessage("입점 대상 상품이 아닙니다(타겟데이터 없음)");
				return result;
			}

			// 신규입점
			result = registerProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				result.setMessage("입점완료되었습니다.");
			}
			return result;
		}
		
		PaTransService transService;
		
		if ("1".equals(paQeenGoods.getTransSaleYn()) && PaSaleGb.SUSPEND.code().equals(paQeenGoods.getPaSaleGb())) {
			// 판매중지
			transService = qeenProductService.stopSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			if ("1".equals(transService.getSuccessYn())) {
				result.setMessage("판매중지되었습니다.");
			} else {
				result.setMessage(transService.getResultMsg());
			}
			return result;
		}
		
		
		boolean isUpdated = false;
		boolean isStock = false;
		boolean isResume = false;
		
		if ("1".equals(paQeenGoods.getTransTargetYn())) {
			// 상품수정
			ResponseMsg updated = updateProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(updated.getCode())) {
				isUpdated = true;
				result.setMessage("수정완료되었습니다.");
			} else {
				return updated;
			}
		} else {
			//재고변경
			transService = qeenProductService.updateStockProduct(goodsCode, paCode, procId);
			
			if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) {
				result.setMessage((isUpdated ? result.getMessage() + " " : "") + transService.getResultMsg());
				isStock = true;
			}
		}
		
		if ("1".equals(paQeenGoods.getTransSaleYn()) && PaSaleGb.FORSALE.code().equals(paQeenGoods.getPaSaleGb())) {
			// 판매재개
			transService = qeenProductService.resumeSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				result.setMessage("판매재개실패: " + transService.getResultMsg());
				return result;
			}
			if ("1".equals(transService.getSuccessYn())) {
				isResume = true;
				result.setMessage(
						(isStock || isUpdated ? result.getMessage() + " " : "") + transService.getResultMsg());
			}
		}
		
		if (isStock || isUpdated || isResume) { // 상품/재고가 변경된 경우
			result.setCode(String.valueOf(HttpStatus.OK.value()));
		} else {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
			result.setMessage("변경된 사항이 없습니다.");
		}
		
		return result;
	}
	
	
	private ResponseMsg callRegisterProduct(String goodsCode, String paCode, String procId, PaTransService serviceLog) {

		ResponseMsg result = new ResponseMsg("", "");

		PaQeenGoodsVO paQeenGoods = null;

		try {
			log.info("===== 상품등록서비스 Start - {} =====", goodsCode);

			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("apiCode", serviceLog.getServiceName());
			paramMap.put("paGroupCode", PaGroup.QEEN.code());
			
			
			// 상품정보 조회
			paramMap.put("modCase", "INSERT");
			paQeenGoods = paQeenGoodsMapper.selectPaQeenGoodsInfo(paramMap.get());
			
			if (paQeenGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paQeenGoods.getReifiedProductId() != null) {
				result.setCode("411");
				result.setMessage("이미 등록된 상품입니다. 퀸잇 상품코드 : " + paQeenGoods.getReifiedProductId() 
																					+ ", 퀸잇 임시코드 : " + paQeenGoods.getProductProposalId() );
				return result;
			}

			if (paQeenGoods.getDescribeExt() == null) {
				paQeenGoods.setDescribeExt("");
				if (paQeenGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}
			
			// 정보고시 조회
			List<PaGoodsOffer> goodsOffer = paQeenGoodsMapper.selectPaQeenGoodsOfferList(paramMap.get());
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}
			
			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.QEEN.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}
			
			// 상품설명설정
			settingDescribeExt(paQeenGoods);
			// 단품 옵션 조회
			List<PaQeenGoodsdtMapping> goodsdtMapping = paQeenGoodsMapper.selectPaQeenGoodsdtInfoList(paramMap.get());
			
			paQeenGoods.setModifyId(procId);
			
			//전문세팅
			Product product = createProduct(paQeenGoods, goodsdtMapping, goodsOffer, goodsPrice, serviceLog.getTransServiceNo());
			
			// Body 세팅
			ParamMap apiRequestObject = new ParamMap();
			apiRequestObject.put("body", paQeenApiRequest.getBody(product));
			
			Map<String, Object> responseMap = new HashMap<String, Object>();	
			responseMap = paQeenConnectUtil.callPaQeenAPI(paramMap, apiRequestObject, serviceLog, null);
			
			ProductResponse productResponse = new ProductResponse();
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			productResponse = objectMapper.convertValue(responseMap, ProductResponse.class);
			
			if(productResponse.getProductProposal().isModifying()) {
				//임시 제휴상품 번호
				Integer productProposalId = productResponse.getProductProposal().getData().getProductProposalId();
				if(productProposalId!=null) {
					
					//productProposalId 저장하는 로직 s
					
					// 상품 등록 후 상품 승인 실패(통신 오류로 인해)하는 경우 퀸잇 측에 임시 상품 등록된 상태 이기 때문에 
					// 다시 상품 등록 하려고 해도 mallProductCode 중복으로 실패 >  임시번호 저장해두고 상품 등록 다음 배치에서 재시도 하기위해 
					
					paQeenGoods.setProductProposalId(productProposalId.toString());
					
					List<ItemProposal> itemProposals = productResponse.getProductProposal().getData().getItemProposals();
					paQeenResultService.saveTransProposalProduct(paQeenGoods, goodsPrice, goodsdtMapping, itemProposals); // 임시번호 저장
					
					// productProposalId 저장하는 로직 e
					
					
					//상품 승인 호출(reifiedProductId:정의된 제휴 상품 번호 리턴)
					
					ProductConfirmResoponseMsg confirmResponseMsg = confirmProduct(goodsCode,productProposalId, paCode, procId, 0);
					
					if(String.valueOf(HttpStatus.OK.value()).equals(confirmResponseMsg.getCode())) {
						ProductProposal productProposal = new ProductProposal();
						productProposal = confirmResponseMsg.getProductResponse().getProductProposal();
						
						if(productProposal.isReified()) {

							Product confirmProduct = productProposal.getData();
							List<ItemProposal> confirmItemProposals = confirmProduct.getItemProposals();
							//상품저장
							paQeenGoods.setReifiedProductId(confirmProduct.getReifiedProductId());
							paQeenGoods.setProductProposalId(productProposalId.toString());
							paQeenResultService.saveTransProduct(paQeenGoods, goodsPrice, goodsdtMapping, confirmItemProposals);
							
							result.setCode("200");
							result.setMessage(PaQeenApiRequest.API_SUCCESS_CODE);
							
						}else {
							result.setCode("500");
							result.setMessage("상품 등록되었으나 승인 실패 및 정보 저장 실패");
						}
					}else {
						result.setCode("500");
						result.setMessage("상품 등록되었으나 승인 실패 및 정보 저장 실패");
					}
					
				}else {
					result.setCode("500");
					result.setMessage("상품 등록에 실패했습니다");
				}
				
			}else {
				result.setCode("500");
				result.setMessage("상품 등록에 실패했습니다");
			}
			
			
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			//반려처리 제외 메시지 체크
			if (!isNotRejectMsg(result.getCode(), result.getMessage())) {
				paQeenGoodsMapper.rejectTransTarget(goodsCode, paCode, procId,
						StringUtil.truncate(result.getMessage(), 500));
			}

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 상품등록서비스 End - {} =====", goodsCode);
		}

		return result;
	}
	
	private ResponseMsg callUpdateProduct(String goodsCode, String paCode, String procId, PaTransService serviceLog) {
		
		ResponseMsg result = new ResponseMsg("", "");
		
		PaQeenGoodsVO paQeenGoods = null;
		
		try {
			log.info("===== 상품수정서비스 Start - {} =====", goodsCode);
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("apiCode", serviceLog.getServiceName());
			paramMap.put("paGroupCode", PaGroup.QEEN.code());
			
			paramMap.put("modCase", "MODIFY");
			// 상품정보 조회
			paQeenGoods = paQeenGoodsMapper.selectPaQeenGoodsInfo(paramMap.get());
			
			if (paQeenGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}
			
			if (paQeenGoods.getReifiedProductId() == null || paQeenGoods.getProductProposalId() == null) {
				result.setCode("411");
				result.setMessage("등록되지 않은 상품입니다. 상품코드 : " + goodsCode);
				return result;
			}
			
			if (paQeenGoods.getDescribeExt() == null) {
				paQeenGoods.setDescribeExt("");
				if (paQeenGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}
			
			// 정보고시 조회
			List<PaGoodsOffer> goodsOffer = paQeenGoodsMapper.selectPaQeenGoodsOfferList(paramMap.get());
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}
			
			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.QEEN.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}
			
			// 상품설명설정
			settingDescribeExt(paQeenGoods);
			// 단품 옵션 조회
			List<PaQeenGoodsdtMapping> goodsdtMapping = paQeenGoodsMapper.selectPaQeenGoodsdtInfoList(paramMap.get());
			
			paQeenGoods.setModifyId(procId);
			
			//전문세팅
			Product product = createProduct(paQeenGoods, goodsdtMapping, goodsOffer, goodsPrice, serviceLog.getTransServiceNo());
			
			// Body 세팅
			ParamMap apiRequestObject = new ParamMap();
			apiRequestObject.put("body", paQeenApiRequest.getBody(product));
			
			Map<String, Object> responseMap = new HashMap<String, Object>();	
			responseMap = paQeenConnectUtil.callPaQeenAPI(paramMap, apiRequestObject, serviceLog, null);
			
			ProductResponse productResponse = new ProductResponse();
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			productResponse = objectMapper.convertValue(responseMap, ProductResponse.class);
			
			if(productResponse.getProductProposal().isModifying()) {
				
				//임시 제휴상품 번호
				Integer productProposalId = productResponse.getProductProposal().getData().getProductProposalId();
				if(productProposalId!=null) {
					
					//상품 승인 호출(reifiedProductId:정의된 제휴 상품 번호 리턴)
					ProductConfirmResoponseMsg confirmResponseMsg = confirmProduct(goodsCode, productProposalId, paCode, procId, 0);
					
					if(String.valueOf(HttpStatus.OK.value()).equals(confirmResponseMsg.getCode())) {
						
						ProductProposal productProposal = new ProductProposal();
						productProposal = confirmResponseMsg.getProductResponse().getProductProposal();
						
						if(productProposal.isReified()) {
							
							Product confirmProduct = productProposal.getData();
							List<ItemProposal> confirmItemProposals = confirmProduct.getItemProposals();
							
							//상품저장
							//paQeenGoods.setReifiedProductId(confirmProduct.getReifiedProductId());
							//paQeenGoods.setProductProposalId(confirmProduct.getProductProposalId().toString());
							paQeenResultService.saveTransProduct(paQeenGoods, goodsPrice, goodsdtMapping, confirmItemProposals);
							
							result.setCode("200");
							result.setMessage(PaQeenApiRequest.API_SUCCESS_CODE);
							
						}else {
							result.setCode("500");
							result.setMessage("상품 수정되었으나 승인 실패 및 정보 저장 실패");
						}
						
					}else {
						result.setCode("500");
						result.setMessage("상품 수정되었으나 승인 실패 및 정보 저장 실패");
					}
					
					
					
				}else {
					result.setCode("500");
					result.setMessage("상품 수정에 실패했습니다");
				}
				
			}else {
				result.setCode("500");
				result.setMessage("상품 수정에 실패했습니다");
			}
			
			
		} catch (TransApiException ex) {
			
			// 퀸잇 삭제 처리 하지 않는다고 하여 주석 처리
//			if (paQeenResultService.applyRetention(goodsCode, paCode, procId, ex.getMessage())) {
//				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
//				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
//				return result;
//			}
			
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단
				paQeenGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
						StringUtil.truncate(result.getMessage(), 500));
			}
			
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 상품수정서비스 End - {} =====", goodsCode);
		}
		
		return result;
	}
	
	private ProductConfirmResoponseMsg callConfirmProduct(String goodsCode, Integer productProposalId, String paCode, String procId, PaTransService serviceLog) {
		
		ResponseMsg result = new ResponseMsg("", "");
		
		ProductResponse productResponse = new ProductResponse();
		try {
			log.info("===== 상품승인서비스 Start - {} =====", goodsCode);
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);		
			paramMap.put("apiCode", serviceLog.getServiceName());
			paramMap.put("paGroupCode", PaGroup.QEEN.code());
			
			// Body 세팅
			ParamMap apiRequestObject = new ParamMap();
			apiRequestObject.put("urlParameter", productProposalId);
			 
			apiRequestObject.put("body", "");
			
			Map<String, Object> responseMap = new HashMap<String, Object>();	
			
			responseMap = paQeenConnectUtil.callPaQeenAPI(paramMap, apiRequestObject, serviceLog, null);
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			productResponse = objectMapper.convertValue(responseMap, ProductResponse.class);
			
			result.setCode("200");
			result.setMessage(PaQeenApiRequest.API_SUCCESS_CODE);
		
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 상품승인서비스 End - {} =====", goodsCode);
		}
		return new ProductConfirmResoponseMsg(result.getCode(), result.getMessage(), productResponse);
	}
	
	/**
	 * 기술서와 공지사항 설정
	 * 
	 * @param paQeenGoods
	 */
	private void settingDescribeExt(PaQeenGoodsVO paQeenGoods) {

		String goodsCom = StringUtils.hasText(paQeenGoods.getGoodsCom())
				? ("<div style='line-height: 2.0em; font-family: NanumBarunGothic; font-size: 19px;'><div><h4>&middot;&nbsp;상품구성<h4><pre>"
						+ paQeenGoods.getGoodsCom() + "</pre></div></div>")
				: "";

		// 웹기술서
		paQeenGoods.setDescribeExt(
				"<div align='center'><img alt='' src='" + paQeenGoods.getTopImage() + "' /><br /><br /><br />" // 상단이미지
						+ goodsCom // 상품구성
						+ paQeenGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />" // 기술서
						+ "<img alt='' src='" + paQeenGoods.getBottomImage() + "' /></div>"); // 하단이미지

		// 제휴 공지사항
		if (StringUtils.hasText(paQeenGoods.getNoticeExt())) {
			paQeenGoods.setDescribeExt(
					paQeenGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + paQeenGoods.getDescribeExt());
		}
	}

	//전문세팅
	private Product createProduct(PaQeenGoodsVO paQeenGoods, List<PaQeenGoodsdtMapping> goodsdtMapping,
			List<PaGoodsOffer> goodsOffer, PaGoodsPriceApply goodsPrice, long transServiceNo) throws Exception {
		
		Product product = new Product();
		
		Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
		String imageServer = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
		
		//임시 제휴 상품코드
		
		if(paQeenGoods.getProductProposalId() != null) {
			product.setProductProposalId(Integer.parseInt(paQeenGoods.getProductProposalId()));
		}
		
		//제휴 상품코드
		product.setReifiedProductId(paQeenGoods.getReifiedProductId()!=null ? paQeenGoods.getReifiedProductId(): null);
		
		product.setTitle(paQeenGoods.getGoodsName()); //상품이름
		
		//가격세팅 s
		Price price = new Price();
		
		price.setOriginalPrice((int) goodsPrice.getSalePrice());//정가(할인전금액)
		price.setSellingPrice((int) goodsPrice.getBestPrice());//판매가
		
		product.setPrice(price);
		//가격세팅 e
		
		product.setMallId("");//mall아이디
		product.setBrandCode(paQeenGoods.getPaBrandCode());
		
		product.setMallProductCode(paQeenGoods.getPaCode()+"_"+paQeenGoods.getGoodsCode());//몰 상에서의 상품 코드
		//product.setCategoryId(paQeenGoods.getPaLmsdKey());//최하위 카테고리 id
		product.setLeafCategoryId(paQeenGoods.getPaLmsdKey());//최하위 카테고리 id

		// 이미지 세팅 s , 추후 퀸잇측에서 정제해준 이미지 ? 받아와서 넣을듯?  
		List<String> imageUrls = new ArrayList<String>();
		
		if(paQeenGoods.getImageG() != null) {
			imageUrls.add(imageServer + paQeenGoods.getImageUrl()+ paQeenGoods.getImageG());
		}
		if(paQeenGoods.getImageAg() != null) {
			imageUrls.add(imageServer + paQeenGoods.getImageUrl()+ paQeenGoods.getImageAg());
		}
		if(paQeenGoods.getImageBg() != null) {
			imageUrls.add(imageServer + paQeenGoods.getImageUrl()+ paQeenGoods.getImageBg());
		}
		if(paQeenGoods.getImageCg() != null) {
			imageUrls.add(imageServer + paQeenGoods.getImageUrl()+ paQeenGoods.getImageCg());
		}
		if(paQeenGoods.getImageDg() != null) {
			imageUrls.add(imageServer + paQeenGoods.getImageUrl()+ paQeenGoods.getImageDg());
		}
		
		product.setImageUrls(imageUrls);//상품 이미지 목록
		// 이미지 세팅 e
		
		product.setDescriptionPageHtml(paQeenGoods.getDescribeExt());//상품 상세 페이지 html
		
		// 정보고시 s
		Map<String, String> dataMap = new HashMap<>();
		for(PaGoodsOffer paGoodsOffer : goodsOffer) {
			  dataMap.put(paGoodsOffer.getPaOfferCodeName(), ComUtil.subStringBytes(paGoodsOffer.getPaOfferExt().toString(),500));
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String announcementV2Json = mapper.writeValueAsString(dataMap); // json 형태로 변환
		
		product.setAnnouncementV2(announcementV2Json);//json로 직렬화된 고시정보
		// 정보고시 e
		
		//분류정보 s
		List<Classification> classificationList = new ArrayList<Classification>();
		product.setClassifications(classificationList);//분류 표(재질정보), 아무것도 세팅하지 않으면 []로 연동
		//분류정보 e
		
		//실측정보 s
		List<Measurement> measurementList = new ArrayList<Measurement>();
		product.setMeasurements(measurementList);//실측 자료, 아무것도 세팅하지 않으면 []로 연동
		//실측정보 e
		product.setSalesStatus("ACTIVE");//ACTIVE판매중  ,ARCHIVED 판매 중지, PERMANENTLY_ARCHIVED 영구 판매 중지
		
		//옵션 헤더 타이틀 s
		List<String> optionTitles = new ArrayList<String>();
		optionTitles.add("옵션");
		product.setOptionTitles(optionTitles);
		//옵션 헤더 타이틀 e
		
		//옵션 아이템 제안 목록 s
		List<ItemProposal> itemProposals =  new ArrayList<ItemProposal>();
		
		for(PaQeenGoodsdtMapping paGoodsdt : goodsdtMapping) {
			
			ItemProposal itemProposal = new ItemProposal();
			
			if(paGoodsdt.getProductItemProposalId()!=null) {
				itemProposal.setProductItemProposalId(Integer.parseInt(paGoodsdt.getProductItemProposalId()));//저장된 상품 옵션 제안서의 id
			}
			if(paGoodsdt.getPaOptionCode()!=null) {
				itemProposal.setReifiedProductItemId(paGoodsdt.getPaOptionCode());
			}
			
			itemProposal.setCode(paGoodsdt.getPaCode() + "_" + paGoodsdt.getGoodsCode() +"_"+ paGoodsdt.getGoodsdtCode()+ "_" + paGoodsdt.getGoodsdtSeq());
			itemProposal.setQuantity(ComUtil.objToInt(paGoodsdt.getTransOrderAbleQty()));
			
			List<String> optionNames = new ArrayList<String>();
			optionNames.add(paGoodsdt.getGoodsdtInfo());
		
			itemProposal.setOptionNames(optionNames);
			//itemProposal.setOptionPrice(0);//옵션별 가격, 미사용
			itemProposal.setSalesStatus(SaleGb.FORSALE.code().equals(paGoodsdt.getSaleGb())? "ACTIVE": "ARCHIVED");//ACTIVE 판매중, ARCHIVED 판매중지 
			
			if("1".equals(paQeenGoods.getOrderCreatYn()) || "1".equals(paQeenGoods.getInstallYn())) {
				//itemProposal.setKstEstimateReleaseDate("");//yyyy-mm-dd, EstimateShipmentDays사용
				itemProposal.setEstimateShipmentDays(30);// 예상 출고 날짜 추가 개념 10 으로 세팅하면 10일뒤로 세팅됨
			}
			
			itemProposals.add(itemProposal);
		}
		product.setItemProposals(itemProposals);
		//옵션 아이템 제안 목록 e
		
		product.setOverrodePolicyTargetId(Integer.parseInt(paQeenGoods.getPaShipcostId()));// 배송정책id
		
		
		if("1".equals(paQeenGoods.getCustOrdQtyCheckYn())) { //id 당 주문제한 
			product.setMaxQuantityLimit(paQeenGoods.getTermOrderQty());
			product.setMaxQuantityLimitType("PER_USER");//PER_USER -> 유저 당 개수제한
		}else if(paQeenGoods.getOrderMaxQty()>0){
			product.setMaxQuantityLimit(paQeenGoods.getOrderMaxQty());
			product.setMaxQuantityLimitType("PER_ORDER");//PER_ORDER -> 주문 당 개수제한
		}
		
		return product;
	}
	
	
	
	/**
	 * 반려제외여부
	 * 
	 * @param resultCode
	 * @param rejectMsg
	 * @return
	 */
    private boolean isNotRejectMsg(String resultCode, String rejectMsg) {

        if (!StringUtils.hasText(rejectMsg) )
            return false;

        String[] rejectNotMatch =
                new String[] {};

        return Arrays.stream(rejectNotMatch).anyMatch(s -> rejectMsg.contains(s));
    }
    
	/**
	 * 수기중단대상 여부
	 * 
	 * @param rejectMsg
	 * @return
	 */
	private boolean isRejectMsg(String rejectMsg) {

		if (!StringUtils.hasText(rejectMsg))
			return false;

        String[] rejectMatch = new String[] {};

		return Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s));
	}
	
}

