package com.cware.netshopping.patmon.v2.service;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.PaSaleGb;
import com.cware.netshopping.common.code.PaStatus;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaTmonGoodsVO;
import com.cware.netshopping.domain.PaTmonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.service.SyncProductService;
import com.cware.netshopping.patmon.goods.service.PaTmonGoodsService;
import com.cware.netshopping.patmon.v2.domain.Deal;
import com.cware.netshopping.patmon.v2.domain.DealNoMap;
import com.cware.netshopping.patmon.v2.domain.DealOption;
import com.cware.netshopping.patmon.v2.domain.DealProductInfo;
import com.cware.netshopping.patmon.v2.domain.DealProductInfoItem;
import com.cware.netshopping.patmon.v2.repository.PaTmonGoodsMapper;

@Service
public class PaTmonProductService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;

	@Autowired
	@Qualifier("patmon.goods.paTmonGoodsService")
	private PaTmonGoodsService paTmonGoodsService;

	@Autowired
	PaTmonProductApiService productApiService;

	@Autowired
	TmonProductService tmonProductService;

	@Autowired
	SyncProductService syncProductService;

	@Autowired
	PaTmonGoodsMapper paTmonGoodsMapper;

	@Autowired
	PaTmonResultService paTmonResultService;

	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;

	@Autowired
	TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 티몬 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public ResponseMsg transProduct(String goodsCode, String paCode, String procId) {

		ResponseMsg result = new ResponseMsg("", "");

		// 상품동기화
		PaGoodsSync sync = syncProductService.syncProduct(goodsCode, PaGroup.TMON.code(), procId);
		result = sync.getResponseMsg();
		if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) {
			return result;
		}

		// 스토아상품조회
		PaTmonGoodsVO paTmonGoods = paTmonGoodsMapper.getGoods(goodsCode, paCode);

		if (paTmonGoods == null) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("연동 대상 상품이 아닙니다.");
			return result;
		}

		// 입점요청중인건은 처리하지 않음
		if (PaStatus.PROCEEDING.code().equals(paTmonGoods.getPaStatus())) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("입점요청중인 상품입니다.");
			return result;
		}

		// 입점요청/입점반려건에 대해서 신규입점 요청
		if (PaStatus.REQUEST.code().equals(paTmonGoods.getPaStatus())
				|| PaStatus.REJECT.code().equals(paTmonGoods.getPaStatus())) {

			// 동기화에서 필터링된 경우
			if (String.valueOf(HttpStatus.NO_CONTENT.value()).equals(result.getCode())) {
				return result;
			}

			if (goodsTargetMapper.existsGoodsTarget(goodsCode, paCode, PaGroup.TMON.code()) < 1) {
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
		
		if ("1".equals(paTmonGoods.getTransSaleYn()) && PaSaleGb.SUSPEND.code().equals(paTmonGoods.getPaSaleGb())) {
			// 판매중지
			transService = tmonProductService.stopSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			if ("1".equals(transService.getSuccessYn())) {
				result.setMessage("판매중지되었습니다.");
			} else {
				result.setMessage(transService.getResultMsg());
			}
			return result;
		}

		boolean isOption = false;
		boolean isUpdated = false;
		boolean isStock = false;


		// 옵션등록
		// 상품수정
		ResponseMsg options = registerProductOption(goodsCode, paCode, procId, 0);
		if (String.valueOf(HttpStatus.OK.value()).equals(options.getCode())) {
			isOption = true;
			result.setMessage("옵션이 추가되었습니다.");
		}
		
		if ("1".equals(paTmonGoods.getTransTargetYn())) {
			// 상품수정
			ResponseMsg updated = updateProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(updated.getCode())) {
				isUpdated = true;
				result.setMessage("수정완료되었습니다.");
			} else
				return updated;
		} 

		// 재고변경 (티몬은 상품수정에서 재고변경 되지 않음)
		transService = tmonProductService.updateStockProduct(goodsCode, paCode, procId);
		if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) {
			result.setMessage((isOption || isUpdated ? result.getMessage() + " " : "") + transService.getResultMsg());
			isStock = true;
		}

		boolean isResume = false;
		if ("1".equals(paTmonGoods.getTransSaleYn()) && PaSaleGb.FORSALE.code().equals(paTmonGoods.getPaSaleGb())) {
			// 판매재개
			transService = tmonProductService.resumeSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				result.setMessage("판매재개실패: " + transService.getResultMsg());
				return result;
			}
			if ("1".equals(transService.getSuccessYn())) {
				isResume = true;
				result.setMessage(
						(isOption || isStock || isUpdated ? result.getMessage() + " " : "") + "판매재개되었습니다.");
			}
		}

		if (isOption || isStock || isUpdated || isResume) { // 상품/재고가 변경된 경우
			result.setCode(String.valueOf(HttpStatus.OK.value()));
		} else {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
			result.setMessage("변경된 사항이 없습니다.");
		}

		return result;
	}

	/**
	 * 티몬 신규상품 입점
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]티몬-상품입점");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TMON.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 상품입점
		ResponseMsg result = callRegisterProduct(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		// 입점요청중 해제
		paTmonGoodsMapper.updateClearProceeding(goodsCode, paCode, procId);

		return result;

	}

	/**
	 * 티몬 상품 수정
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg updateProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]티몬-상품수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TMON.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 상품수정
		ResponseMsg result = callUpdateProduct(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * 티몬 옵션 등록
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerProductOption(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]티몬-옵션등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TMON.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 옵션등록
		ResponseMsg result = callRegisterOption(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	/**
	 * 티몬 상품 조회
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public Deal getProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]티몬-상품조회");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TMON.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		Deal product = null;

		try {
			// 상품조회
			product = productApiService.getProduct(goodsCode, paCode, serviceLog.getTransServiceNo());
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(PaTmonApiRequest.API_SUCCESS_CODE);

		} catch (TransApiException ex) {
			serviceLog.setResultCode(ex.getCode());
			serviceLog.setResultMsg(ex.getMessage());
		} catch (Exception e) {
			serviceLog.setResultCode("500");
			serviceLog.setResultMsg(e.getMessage());
		}

		transLogService.logTransServiceEnd(serviceLog);

		return product;

	}

	private ResponseMsg callRegisterProduct(String goodsCode, String paCode, String procId, long transServiceNo) {
		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		int exceptYn = 0;

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		PaTmonGoodsVO paTmonGoods = null;

		try {
			log.info("===== 상품등록서비스 Start - {} =====", goodsCode);

			dateTime = systemService.getSysdatetimeToString();

			paramMap.put("dateTime", dateTime);
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
			paramMap.put("modCase", "INSERT");
			paTmonGoods = paTmonGoodsService.selectPaTmonGoodsInfo(paramMap);

			if (paTmonGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paTmonGoods.getDealNo() != null) {
				result.setCode("411");
				result.setMessage("이미 등록된 상품입니다. 티몬 상품코드 : " + paTmonGoods.getDealNo());
				return result;
			}
			if (paTmonGoods.getDescribeExt() == null) {
				paTmonGoods.setDescribeExt("");
				if (paTmonGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", paTmonGoods.getPaGroupCode());

			// 정보고시 조회
			List<PaGoodsOfferVO> goodsOffer = paTmonGoodsService.selectPaTmonGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.TMON.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 입점요청중
			paTmonGoodsMapper.updateProceeding(goodsCode, paCode, procId);

			// 상품설명설정
			settingDescribeExt(paTmonGoods);

			// 단품 옵션 조회
			List<PaGoodsdtMapping> goodsdtMapping = paTmonGoodsService.selectPaTmonGoodsdtInfoList(paramMap);

			paTmonGoods.setModifyId(procId);
			
			/* 2023-06-15 GBJEONG
			 * 티몬은 일반배송 -> 설치배송으로 배송템플릿을 변경할 수 없음.
			 * 하이마트 상품 일반배송 -> 설치상품으로 변경됨에 따라  기존 등록된 상품들 수정 실패하여 재입점 처리
			 * vendorDealNo, vendorDealOptionNo 끝에 01을 붙여 중복입점을 시킨다.
			 * 관리 테이블 : TPATMONGOODSSHIPPOLICY
			 */
			exceptYn = paTmonGoodsService.selectPaTmonExceptShipPolicy(paTmonGoods.getGoodsCode());
			paTmonGoods.setExceptYn(exceptYn);

			// 티몬 상품 전문
			Deal product = createProduct(paTmonGoods, goodsdtMapping, goodsOffer, goodsPrice);

			log.info("상품등록 API 호출 {}", goodsCode);
			DealNoMap dealNoMap = productApiService.registerProduct(goodsCode, product, paCode, transServiceNo);

			if (StringUtils.hasText(dealNoMap.getTmonDealNo())) {

				result.setCode("200");
				result.setMessage(PaTmonApiRequest.API_SUCCESS_CODE);

				paTmonGoods.setDealNo(dealNoMap.getTmonDealNo());

				for(PaGoodsdtMapping optionItem : goodsdtMapping) {
					Long tmonDealOptionNo = (Long)dealNoMap.getDealOptionNos().get(paTmonGoods.getExceptYn() > 0 ? optionItem.getPaCode() + optionItem.getGoodsCode() + optionItem.getGoodsdtCode() + "01" : optionItem.getPaCode() + optionItem.getGoodsCode() + optionItem.getGoodsdtCode());
					if (tmonDealOptionNo > 0) {
						optionItem.setPaOptionCode(String.valueOf(tmonDealOptionNo));
						optionItem.setModifyId(procId);
					}
				}

				rtnMsg = paTmonResultService.saveTransProduct(paTmonGoods, goodsPrice, goodsdtMapping);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + paTmonGoods.getGoodsCode());
				}

			} else {
				result.setCode("500");
				result.setMessage("상품 등록에 실패했습니다.");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

			// 반려처리 제외 메시지 체크
			if (!isNotRejectMsg(result.getCode(), result.getMessage())) {
    			paTmonGoodsMapper.rejectTransTarget(goodsCode, paCode, procId,
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

	private ResponseMsg callUpdateProduct(String goodsCode, String paCode, String procId, long transServiceNo) {
		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		int exceptYn = 0;

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		PaTmonGoodsVO paTmonGoods = null;

		try {
			log.info("===== 상품수정서비스 Start - {} =====", goodsCode);

			dateTime = systemService.getSysdatetimeToString();

			paramMap.put("dateTime", dateTime);
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
			paramMap.put("modCase", "MODIFY");
			paTmonGoods = paTmonGoodsService.selectPaTmonGoodsInfo(paramMap);

			if (paTmonGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paTmonGoods.getDealNo() == null) {
				result.setCode("411");
				result.setMessage("등록되지 않은 상품입니다. 상품코드 : " + goodsCode);
				return result;
			}
			if (paTmonGoods.getDescribeExt() == null) {
				paTmonGoods.setDescribeExt("");
				if (paTmonGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", paTmonGoods.getPaGroupCode());

			// 정보고시 조회
			List<PaGoodsOfferVO> goodsOffer = paTmonGoodsService.selectPaTmonGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.TMON.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 상품설명설정
			settingDescribeExt(paTmonGoods);

			// 단품 옵션 조회
			List<PaGoodsdtMapping> goodsdtMapping = paTmonGoodsService.selectPaTmonGoodsdtInfoList(paramMap);

			paTmonGoods.setModifyId(procId);
			
			/* 2023-06-15 GBJEONG
			 * 티몬은 일반배송 -> 설치배송으로 배송템플릿을 변경할 수 없음.
			 * 하이마트 상품 일반배송 -> 설치상품으로 변경됨에 따라  기존 등록된 상품들 수정 실패하여 재입점 처리
			 * vendorDealNo, vendorDealOptionNo 끝에 01을 붙여 중복입점을 시킨다.
			 * 관리 테이블 : TPATMONGOODSSHIPPOLICY
			 */
			exceptYn = paTmonGoodsService.selectPaTmonExceptShipPolicy(paTmonGoods.getGoodsCode());
			paTmonGoods.setExceptYn(exceptYn);

			// 티몬 상품 전문
			Deal product = createProduct(paTmonGoods, goodsdtMapping, goodsOffer, goodsPrice);
			if (product == null) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
				return result;
			}

			log.info("상품수정 API 호출 {}", goodsCode);
			productApiService.updateProduct(goodsCode, product, paCode, transServiceNo);
			result.setCode("200");
			result.setMessage(PaTmonApiRequest.API_SUCCESS_CODE);

			rtnMsg = paTmonResultService.saveTransProduct(paTmonGoods, goodsPrice, goodsdtMapping);

			if (!rtnMsg.equals("000000")) {
				result.setCode("500");
				result.setMessage(rtnMsg + " : " + paTmonGoods.getGoodsCode());
			}

		} catch (TransApiException ex) {
			
			if (paTmonResultService.applyRetention(goodsCode, paCode, procId, ex.getMessage())) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
				return result;
			}

			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단
				paTmonGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
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

	private ResponseMsg callRegisterOption(String goodsCode, String paCode, String procId, long transServiceNo) {
		ParamMap paramMap = new ParamMap();

		ResponseMsg result = new ResponseMsg("", "");
		int exceptYn = 0;


		try {
			log.info("===== 옵션등록서비스 Start - {} =====", goodsCode);

			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			
			/* 2023-06-15 GBJEONG
			 * 티몬은 일반배송 -> 설치배송으로 배송템플릿을 변경할 수 없음.
			 * 하이마트 상품 일반배송 -> 설치상품으로 변경됨에 따라  기존 등록된 상품들 수정 실패하여 재입점 처리
			 * vendorDealNo, vendorDealOptionNo 끝에 01을 붙여 중복입점을 시킨다.
			 * 관리 테이블 : TPATMONGOODSSHIPPOLICY
			 */
			exceptYn = paTmonGoodsService.selectPaTmonExceptShipPolicy(goodsCode);

			// 신규 옵션 조회
			List<PaTmonGoodsdtMappingVO> goodsdtList = paTmonGoodsService.selectPaTmonGoodsdtAddedList(paramMap);
			if (goodsdtList.size() < 1) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}
			
			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.TMON.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 티몬 옵션등록 전문
			List<DealOption> dealOptions = createProductOption( goodsdtList, goodsPrice, exceptYn);
			
			log.info("옵션등록 API 호출 {}", goodsCode);
			Map<String, Object> dealOptionNo = productApiService.registerProductOption(goodsCode, dealOptions, paCode, transServiceNo);

			for(PaTmonGoodsdtMappingVO optionItem : goodsdtList) {
				Long tmonDealOptionNo = (Long)dealOptionNo.get(exceptYn > 0 ? optionItem.getPaCode() + optionItem.getGoodsCode() + optionItem.getGoodsdtCode() + "01" : optionItem.getPaCode() + optionItem.getGoodsCode() + optionItem.getGoodsdtCode());
				
				if (tmonDealOptionNo > 0) {
					optionItem.setPaOptionCode(String.valueOf(tmonDealOptionNo));
					optionItem.setModifyId(procId);
				}
			}

			result.setCode("200");
			result.setMessage(PaTmonApiRequest.API_SUCCESS_CODE);

			paTmonResultService.saveTransProductOption(goodsdtList);

		} catch (TransApiException ex) {

			if (paTmonResultService.applyRetention(goodsCode, paCode, procId, ex.getMessage())) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
				return result;
			}

			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단
				paTmonGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
						StringUtil.truncate(result.getMessage(), 500));
			}
			
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 옵션등록서비스 End - {} =====", goodsCode);
		}
		return result;
	}

    /**
     * 반려제외여부
     * 
     * @param resultCode
     * @param rejectMsg
     * @return
     */
    private boolean isNotRejectMsg(String resultCode, String rejectMsg) {

        if (!StringUtils.hasText(rejectMsg) && !"503".equals(resultCode))
            return false;

        // 티몬 내부 API 연동 오류
        String[] rejectNotMatch =
                new String[] {"ResourceAccessException", "CannotCreateTransactionException",
                        "PersistenceException", "502 Bad Gateway", "이미지 파일에 대해 응답이 없습니다"};

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

        String[] rejectMatch = new String[] {"금칙어", "배송템플릿", "정상적인 이미지 URL 이 아닙니다", "중복된 옵션 타이틀"};

		return Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s));
	}

	/**
	 * 기술서와 공지사항 설정
	 * 
	 * @param paTmonGoods
	 */
	private void settingDescribeExt(PaTmonGoodsVO paTmonGoods) {

		// 상품구성템플릿
		String goodsCom = StringUtils.hasText(paTmonGoods.getGoodsCom())
				? ("<div style='line-height: 2.0em; font-family: NanumBarunGothic; font-size: 19px;'><div><h4>&middot;&nbsp;상품구성<h4><pre>"
						+ paTmonGoods.getGoodsCom() + "</pre></div></div>")
				: "";

		// 웹기술서
		paTmonGoods.setDescribeExt(
				"<div align='center'><img alt='' src='" + paTmonGoods.getTopImage() + "' /><br /><br /><br />" // 상단이미지
						+ (StringUtils.hasText(paTmonGoods.getCollectImage())
								? "<img alt='' src='" + paTmonGoods.getCollectImage() + "' /><br /><br /><br />"
								: "") // 착불이미지
						+ goodsCom // 상품구성
						+ paTmonGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />" // 기술서
						+ "<img alt='' src='" + paTmonGoods.getBottomImage() + "' /></div>"); // 하단이미지

		// 제휴 공지사항
		if (StringUtils.hasText(paTmonGoods.getNoticeExt())) {
			paTmonGoods.setDescribeExt(
					paTmonGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + paTmonGoods.getDescribeExt());
		}
	}

	private Deal createProduct(PaTmonGoodsVO paTmonGoods, List<PaGoodsdtMapping> paGoodsDtMappingList,
			List<PaGoodsOfferVO> paGoodsOfferList, PaGoodsPriceApply goodsPrice) throws Exception {

		Deal product = new Deal();

		//딜번호(티몬전시단위)에 해당 연동업체측 키
		product.setVendorDealNo(paTmonGoods.getExceptYn() > 0 ? paTmonGoods.getPaCode() + "-" + paTmonGoods.getGoodsCode() + "01" : paTmonGoods.getPaCode() + "-" + paTmonGoods.getGoodsCode());
		
		product.setExceptYn(paTmonGoods.getExceptYn()); // 배송템플릿 오류 여부
		
		product.setDeliveryTemplateNo(paTmonGoods.getPaShipPolicyNo()); // 배송템플릿번호
				
		List<String> sections = new ArrayList<String>();
		sections.add("선택");
		product.setSections(sections); // 상품선택정보들
		
		product.setManagedTitle(StringUtil.ellipsis(paTmonGoods.getManagedTitle(), 60));
		
		if (paTmonGoods.getPaLgroup().matches("57000000|48000000|49000000|52000000|54000000")) {
			product.setLegalPermissionType("NONE"); //법적허가/신고대상 상품코드
		}
		
		if(paTmonGoods.getCustOrdQtyCheckYn() == 1) {		//기간별 판매 체크가 되어 있으면 
			product.setMaxPurchaseQty(Integer.parseInt(paTmonGoods.getTermOrderQty())); // 1인당 최대 구매가능 수량
			if(paTmonGoods.getCustOrdQtyCheckTerm() > 0) {//0일로 넣었을경우 에러남 
				product.setPurchaseResetPeriod((int)paTmonGoods.getCustOrdQtyCheckTerm()); //1인당 최대 구매가능 수량 주기	
			}
		}else if(paTmonGoods.getOrderMaxQty() > 0 && paTmonGoods.getOrderMaxQty() < 1000) {
			product.setMaxPurchaseQty((int)paTmonGoods.getOrderMaxQty()); 
			product.setPurchaseResetPeriod(1); 
		}else {
			product.setMaxPurchaseQty(999); //  0 이거나 999보다 클경우 이슈 발생 
			product.setPurchaseResetPeriod(1); 
		}

		product.setTitle(StringUtil.ellipsis(paTmonGoods.getTitle(), 60));
		product.setTitleDecoration("행복한 쇼핑, SK스토아"); //판매용 제목 상단 홍보 문구(딜 홍보 문구)
		if("1".equals(paTmonGoods.getCollectYn())) {
			product.setTitleDecoration("착불 | " + product.getTitleDecoration());
		}

		// 상품이미지
		Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
		String imageServer = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
		String imagePath = imageServer + paTmonGoods.getImageUrl();
        String imageResizePath = "/dims/resize/600X600";
		
		List<String> mainImages = new ArrayList<String>();
		mainImages.add(imagePath + paTmonGoods.getImageP() + imageResizePath);
		if(paTmonGoods.getImageAP() != null) {
			mainImages.add(imagePath + paTmonGoods.getImageAP() + imageResizePath);
		}
		if(paTmonGoods.getImageBP() != null) {
			mainImages.add(imagePath + paTmonGoods.getImageBP() + imageResizePath);
		}
		if(paTmonGoods.getImageCP() != null) {
			mainImages.add(imagePath + paTmonGoods.getImageCP() + imageResizePath);
		}
		if(paTmonGoods.getImageDP() != null) {
			mainImages.add(imagePath + paTmonGoods.getImageDP() + imageResizePath);
		}
		product.setMainImages(mainImages); // 사이트 노출용 메인이미지들
		
		product.setDetailContents(paTmonGoods.getDescribeExt()); // 사이트 노출용 딜상세 내용

		product.setAdditionalInput(false); //주문시 부가정보 입력받을지 여부
		
		if("9999".equals(paTmonGoods.getOriginCode()) || "60".equals(paTmonGoods.getLgroup())) { // 대카테고리 식품일 경우 '상품상세참조' 연동 (2024.02.29)
			product.setOriginCountryType("D"); // 원산지 표기 방식 (상품상세참조)
		}else if("0082".equals(paTmonGoods.getOriginCode())) {
			product.setOriginCountryType("O"); // 원산지 표기 방식 (한국)
		}else {
			product.setOriginCountryType("E"); // 원산지 표기 방식 (직접입력)
			product.setOriginCountryDetail(paTmonGoods.getOriginName());
		}

		//상품 정보고시
		List<DealProductInfoItem> productInfos = new ArrayList<DealProductInfoItem>();
		for(PaGoodsOfferVO offerItem : paGoodsOfferList){
			DealProductInfoItem productInfoItem = new DealProductInfoItem();
			productInfoItem.setSection(offerItem.getPaOfferCodeName());
			productInfoItem.setDescription(offerItem.getPaOfferExt());
			productInfos.add(productInfoItem);
		}
		DealProductInfo productInfo = new DealProductInfo();
		productInfo.setProductType(paGoodsOfferList.get(0).getPaOfferTypeName());
		productInfo.setProductInfos(productInfos);
		product.setProductInfos(Arrays.asList(productInfo));
		
		product.setKcAuthSubmitType("X");

		product.setDeliveryCorp(paTmonGoods.getDelyGb()); //배송사(택배사)
		product.setSearch(true); //검색 노출 여부
		
		List<String> keywords = new ArrayList<String>();
		keywords.add("sk스토아");
		keywords.add("SK스토아");
		keywords.add("SKstoa");
		keywords.add("skstoa");
		keywords.add("에스케이스토아");
		product.setKeywords(keywords); //검색 키워드		
		product.setPriceComparison(true); //가격비교 노출동의 여부

		if (paTmonGoods.getDealNo() == null) {
			// 설치배송/주문제작배송여부가 변경되면 더이상 연동 불가 
			product.setProductType(paTmonGoods.getProductType()); //배송상품 유형 타입
			product.setAdultOnly("1".equals(paTmonGoods.getAdultYn()) ? true : false);
		} 
		
		product.setCategoryNo(paTmonGoods.getCategoryNo()); // 티몬 카테고리 번호
		
		// 레거시에서는 등록할 때만 전문 생성
		// 수정시에는 false -> true만 허용됨
		if ("1".equals(paTmonGoods.getAdultYn())) product.setAdultOnly(true);
		product.setBrandName(paTmonGoods.getBrandName());
		
		List<DealOption> dealOptions = new ArrayList<DealOption>();
		
		for(PaGoodsdtMapping optionItem : paGoodsDtMappingList) {
			DealOption option = new DealOption();

			option.setVendorDealOptionNo(paTmonGoods.getExceptYn() > 0 ? 
					optionItem.getPaCode() + optionItem.getGoodsCode() + optionItem.getGoodsdtCode() + "01" :
					optionItem.getPaCode() + optionItem.getGoodsCode() + optionItem.getGoodsdtCode());
			option.setSections(Arrays.asList(optionItem.getGoodsdtInfo().replaceAll("[#<>\\\"^|]", " ").trim().replaceAll("\\s+", " ")));
			option.setSalesPrice((long)goodsPrice.getBestPrice());
			
			if(paTmonGoods.getDealNo() == null) {	// 상품 수정에서 제외되는 항목 
				option.setStock(Integer.parseInt(optionItem.getTransOrderAbleQty()));
			}
			dealOptions.add(option);
		}
		
		product.setDealOptions(dealOptions);
		
		return product;
	}

	private List<DealOption> createProductOption(List<PaTmonGoodsdtMappingVO> paGoodsDtMappingList,
			PaGoodsPriceApply goodsPrice, int exceptYn) throws Exception {

		List<DealOption> dealOptions = new ArrayList<DealOption>();
		
		for(PaTmonGoodsdtMappingVO optionItem : paGoodsDtMappingList) {
			DealOption option = new DealOption();

			option.setVendorDealOptionNo(exceptYn > 0 ? optionItem.getPaCode() + optionItem.getGoodsCode() + optionItem.getGoodsdtCode() + "01" :
					optionItem.getPaCode() + optionItem.getGoodsCode() + optionItem.getGoodsdtCode());
			option.setSections(Arrays.asList(optionItem.getGoodsdtInfo().replaceAll("[#<>\\\"^|]", " ").trim().replaceAll("\\s+", " ")));
			option.setSalesPrice((long)goodsPrice.getBestPrice());
			option.setStock(Integer.parseInt(optionItem.getTransOrderAbleQty()));
			dealOptions.add(option);
		}
		
		return dealOptions;
	}
}
