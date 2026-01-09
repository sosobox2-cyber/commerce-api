package com.cware.netshopping.pacopn.v2.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.PaCopnGoodsVO;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaCopnGoods;
import com.cware.netshopping.domain.model.PaCopnGoodsAttri;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.service.SyncProductService;
import com.cware.netshopping.pacopn.goods.service.PaCopnGoodsService;
import com.cware.netshopping.pacopn.v2.domain.Attribute;
import com.cware.netshopping.pacopn.v2.domain.Certification;
import com.cware.netshopping.pacopn.v2.domain.Content;
import com.cware.netshopping.pacopn.v2.domain.ContentDetail;
import com.cware.netshopping.pacopn.v2.domain.Image;
import com.cware.netshopping.pacopn.v2.domain.Item;
import com.cware.netshopping.pacopn.v2.domain.Notice;
import com.cware.netshopping.pacopn.v2.domain.Product;
import com.cware.netshopping.pacopn.v2.repository.PaCopnGoodsMapper;
import com.cware.netshopping.domain.model.PaCopnGoodsUserAttri;

@Service
public class PaCopnProductService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;

	@Autowired
	@Qualifier("pacopn.goods.paCopnGoodsService")
	private PaCopnGoodsService paCopnGoodsService;

	@Autowired
	PaCopnProductApiService productApiService;

	@Autowired
	CoupangProductService coupangProductService;
	
	@Autowired
	SyncProductService syncProductService;

	@Autowired
	PaCopnGoodsMapper copnGoodsMapper;
	
	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;
	
	@Autowired
	TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 쿠팡 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public ResponseMsg transProduct(String goodsCode, String paCode, String procId) {

		
		ResponseMsg result = new ResponseMsg("", "");
		
		// 상품동기화
		PaGoodsSync sync = syncProductService.syncProduct(goodsCode, PaGroup.COUPANG.code(), procId);
		result = sync.getResponseMsg();
		if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) {
			return result;
		}
		
		// 스토아상품조회
		PaCopnGoods copnGoods = copnGoodsMapper.getGoods(goodsCode, paCode);
		
		if (copnGoods == null) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("연동 대상 상품이 아닙니다.");
			return result;
		}
		
		// 입점요청중인건은 처리하지 않음
		if(PaStatus.PROCEEDING.code().equals(copnGoods.getPaStatus())) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("입점요청중인 상품입니다.");
			return result;
		}
		
		// 입점요청/입점반려건에 대해서 신규입점 요청
		if (PaStatus.REQUEST.code().equals(copnGoods.getPaStatus())
				|| PaStatus.REJECT.code().equals(copnGoods.getPaStatus())) {

			// 동기화에서 필터링된 경우
			if (String.valueOf(HttpStatus.NO_CONTENT.value()).equals(result.getCode())) {
				return result;
			}
			
			if (goodsTargetMapper.existsGoodsTarget(goodsCode, paCode, PaGroup.COUPANG.code()) < 1) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
				result.setMessage("입점 대상 상품이 아닙니다(타겟데이터 없음)");
				return result;
			}
			
			// 신규입점
			result = registerProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				// 1초뒤 업데이트 요청
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					log.error("입점완료 후 대기", e);
				}
				// 상품상태업데이트
				coupangProductService.updateStatusProduct(goodsCode, paCode, procId);
				result.setMessage("입점완료되었습니다.");
			}
			return result;
		}
		
		PaTransService transService;
		boolean isMapping = false;
		
		// 상품상태업데이트
		if (!"30".equals(copnGoods.getApprovalStatus())) {
			transService = coupangProductService.updateStatusProduct(goodsCode, paCode, procId);
			isMapping = String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode());
			result.setMessage(transService.getResultMsg());
		}
		
		if ("1".equals(copnGoods.getTransSaleYn()) && PaSaleGb.SUSPEND.code().equals(copnGoods.getPaSaleGb())) {
			// 판매중지
			transService = coupangProductService.stopSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			if ("1".equals(transService.getSuccessYn())) {
				result.setMessage("판매중지되었습니다.");
			} else {
				result.setMessage(transService.getResultMsg());
			}
			return result;
		}

		// 가격변경
		boolean isPrice = false;
		transService = coupangProductService.updatePriceProduct(goodsCode, paCode, procId);
		if ("0".equals(transService.getSuccessYn())) {
			result.setCode(transService.getResultCode());
			result.setMessage("가격변경실패: " + transService.getResultMsg());
			return result;
		} else if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) {
			result.setMessage(transService.getResultMsg());
			isPrice = true;
		}
		
		// 재고변경
		boolean isStock = false;
		transService = coupangProductService.updateStockProduct(goodsCode, paCode, procId);;
		if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) {
			result.setMessage((isPrice ? result.getMessage() + " ": "") + transService.getResultMsg());
			isStock = true;
		}

		boolean isUpdated = false;
		if ("1".equals(copnGoods.getTransTargetYn())) {
			// 상품수정
			ResponseMsg updated = updateProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(updated.getCode())) {
				isUpdated = true;
				result.setMessage((isPrice || isStock ? result.getMessage() + " ": "") + "수정완료되었습니다.");
			}
		} 

		boolean isResume = false;
		if ("1".equals(copnGoods.getTransSaleYn()) && PaSaleGb.FORSALE.code().equals(copnGoods.getPaSaleGb())) {
			// 판매재개
			transService = coupangProductService.resumeSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				result.setMessage("판매재개실패: " + transService.getResultMsg());
				return result;
			}
			if ("1".equals(transService.getSuccessYn())) {
				isResume = true;
				result.setMessage((isPrice || isStock || isUpdated ? result.getMessage() + " " : "") + "판매재개되었습니다.");
			}
		}

		if (isUpdated) {
			// 1초뒤 업데이트 요청
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				log.error("수정완료 후 대기", e);
			}
			// 상품상태업데이트
			coupangProductService.updateStatusProduct(goodsCode, paCode, procId);
		}
		
		if (isPrice || isStock || isUpdated || isResume || isMapping) { // 상품/상태/가격/재고가 변경된 경우
			result.setCode(String.valueOf(HttpStatus.OK.value()));
		} else {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
			result.setMessage("변경된 사항이 없습니다.");
		}
		
		return result;
	}
	
	/**
	 * 쿠팡 신규상품 입점
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
		serviceLog.setServiceNote("[API]쿠팡-상품입점");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.COUPANG.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// 상품입점
		ResponseMsg result = callRegisterProduct(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		// 입점요청중 해제
		copnGoodsMapper.updateClearProceeding(goodsCode, paCode, procId);
		
		return result;
		
	}
	
	/**
	 * 쿠팡 상품 수정
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
		serviceLog.setServiceNote("[API]쿠팡-상품수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.COUPANG.code());
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
	 * 쿠팡 상품 조회
	 * 
	 * @param goodsCode
	 * @param sellerProductId
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public Product getProduct(String goodsCode, String sellerProductId, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]쿠팡-상품조회");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.COUPANG.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		Product product = null;
		
		try {
			// 상품조회
			product = productApiService.getProduct(goodsCode, sellerProductId, paCode, serviceLog.getTransServiceNo());
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(PaCopnApiRequest.API_SUCCESS_CODE);
					
		} catch (TransApiException ex){
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

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		PaCopnGoodsVO paCopnGoods = null;
		List<PaCopnGoodsAttri> copnGoodsAttri = null;
		List<PaGoodsOfferVO> goodsOffer = null;
		List<PaGoodsdtMapping> goodsdtMapping = null;
		List<PaCopnGoodsUserAttri> copnGoodsUserAttri = null;
		List<PaCopnGoodsUserAttri> copnGoodsUserSearchAttri = null;
		List<PaPromoTarget> paPromoTargetList = null;// 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)

		try {
			log.info("===== 상품등록서비스 Start - {} =====", goodsCode);

			dateTime = systemService.getSysdatetimeToString();

			// 상품 수정 중 동기화배치가 돌아 상품정보 수정될 경우 다시 수정대상에 포함 하기위해 MODIFY_DATE와 수정대상조회 시작시간을 비교함
			String goodsListTime = systemService.getSysdatetimeToString();
			paramMap.put("dateTime", dateTime);
			paramMap.put("goodsListTime", goodsListTime);
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
        	paramMap.put("modCase", Constants.PA_COPN_MOD_CASE_INSERT);
			paCopnGoods = paCopnGoodsService.selectPaCopnGoodsInfo(paramMap);

			if (paCopnGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			} 
			
			if (paCopnGoods.getSellerProductId() != null) {
				result.setCode("411");
				result.setMessage("이미 등록된 상품입니다. 쿠팡 상품코드 : " + paCopnGoods.getSellerProductId());
				return result;
			}
			if (paCopnGoods.getDescribeExt() == null) {
				paCopnGoods.setDescribeExt("");
				if (paCopnGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", paCopnGoods.getPaGroupCode());
			
			// 정보고시 조회
			goodsOffer = paCopnGoodsService.selectPaCopnGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 입점요청중
			copnGoodsMapper.updateProceeding(goodsCode, paCode, procId);
			
			// 상품설명설정
			settingDescribeExt(paCopnGoods);
			
			// 키워드 특수문자 제거
			paCopnGoods.setKeyWord(convertKeyword(paramMap, paCopnGoods.getKeyWord()));
						
			// 쿠팡 매핑 옵션 조회
			copnGoodsAttri = paCopnGoodsService.selectPaCopnGoodsAttriList(paramMap);
			
			// SK스토아 단품 옵션 조회
			goodsdtMapping = paCopnGoodsService.selectPaCopnGoodsdtInfoList(paramMap);
			
			// 사용자 구매옵션 조회
			copnGoodsUserAttri = paCopnGoodsService.selectPaCopnGoodsUserAttriList(paramMap);
			
			// 쿠팡 검색옵션 조회
			copnGoodsUserSearchAttri = paCopnGoodsService.selectPaCopnGoodsUserSearchAttriList(paramMap);
			
			// 프로모션 조회
			paPromoTargetList = paCopnGoodsService.selectPaPromoTarget(paramMap);// 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.COUPANG.code(), paCode);

			// 쿠팡 상품 전문
			Product product = createProduct(paCopnGoods, goodsdtMapping, copnGoodsAttri, copnGoodsUserAttri, copnGoodsUserSearchAttri, goodsOffer, goodsPrice,
					transServiceNo );

			log.info("상품등록 API 호출 {}", goodsCode);
			String sellerProductId = productApiService.registerProduct(goodsCode, product, paCode, transServiceNo);

			if (StringUtils.hasText(sellerProductId)) {
				
				result.setCode("200");
				result.setMessage(PaCopnApiRequest.API_SUCCESS_CODE);

				if (copnGoodsAttri.size() != 0) {
					copnGoodsAttri.get(0).setModifyId(procId);
					copnGoodsAttri.get(0).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paCopnGoodsService.savePaCopnGoodsAttriTx(copnGoodsAttri);

					if (!rtnMsg.equals("000000")) {
						result.setCode("404");
						result.setMessage(rtnMsg);
						return result;
					}
				}

				// TPACOPNGOODS에 결과 UPDATE
				paCopnGoods.setSellerProductId(sellerProductId);
				paCopnGoods.setPaStatus("30"); // 입점완료
				paCopnGoods.setInsertId(procId);
				paCopnGoods.setModifyId(procId);
				paCopnGoods.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paCopnGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paCopnGoods.setLastModifyDate(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));

				rtnMsg = paCopnGoodsService.savePaCopnGoodsTx(paCopnGoods, goodsdtMapping, paPromoTargetList, goodsPrice);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + paCopnGoods.getGoodsCode());
				}

			} else {
				result.setCode("500");
				result.setMessage("no data found");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

			// 정보고시 에러
			if (result.getMessage().indexOf("고시정보") > -1) {
				paCopnGoods.setPaSaleGb("30"); // 판매중지
				paCopnGoods.setPaStatus("20"); // 반려
				paCopnGoods.setReturnNote(StringUtil.truncate(result.getMessage(), 500));
				paCopnGoods.setModifyId(procId);
				try {
					paCopnGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					rtnMsg = paCopnGoodsService.savePaCopnGoodsFailTx(paCopnGoods);
				} catch (Exception e) {
					result.setCode("500");
					result.setMessage(e.getMessage());
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 상품등록 API End - {} =====", goodsCode);
		}
		return result;
	}
	
	private ResponseMsg callUpdateProduct(String goodsCode, String paCode, String procId, long transServiceNo) {
		ParamMap paramMap = new ParamMap();
		String dateTime = "";

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		PaCopnGoodsVO paCopnGoods = null;
		List<PaCopnGoodsAttri> copnGoodsAttri = null;
		List<PaGoodsOfferVO> goodsOffer = null;
		List<PaGoodsdtMapping> goodsdtMapping = null;
		List<PaPromoTarget> paPromoTargetList = null;// 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
		List<PaCopnGoodsUserAttri> copnGoodsUserAttri = null;
		List<PaCopnGoodsUserAttri> copnGoodsUserSearchAttri = null;

		try {
			log.info("===== 상품수정서비스 Start - {} =====", goodsCode);

			dateTime = systemService.getSysdatetimeToString();

			// 상품 수정 중 동기화배치가 돌아 상품정보 수정될 경우 다시 수정대상에 포함 하기위해 MODIFY_DATE와 수정대상조회 시작시간을 비교함
			String goodsListTime = systemService.getSysdatetimeToString();
			paramMap.put("dateTime", dateTime);
			paramMap.put("goodsListTime", goodsListTime);
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
        	paramMap.put("modCase", Constants.PA_COPN_MOD_CASE_MODIFY);
			paCopnGoods = paCopnGoodsService.selectPaCopnGoodsInfo(paramMap);

			if (paCopnGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paCopnGoods.getSellerProductId() == null) {
				result.setCode("411");
				result.setMessage("등록되지 않은 상품입니다. 상품코드 : " + goodsCode);
				return result;
			}
			if (paCopnGoods.getDescribeExt() == null) {
				paCopnGoods.setDescribeExt("");
				if (paCopnGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}
			
			if (paCopnGoods.getApprovalStatus().equals("05") || paCopnGoods.getApprovalStatus().equals("10")
					|| paCopnGoods.getApprovalStatus().equals("15") || paCopnGoods.getApprovalStatus().equals("25")) { 
				result.setCode("480");
				result.setMessage("승인요청이 완료되지 않았습니다. 상품코드 : " + goodsCode);
				return result;
			}

			paramMap.put("paGroupCode", paCopnGoods.getPaGroupCode());
			
			// 정보고시 조회
			goodsOffer = paCopnGoodsService.selectPaCopnGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 상품설명설정
			settingDescribeExt(paCopnGoods);
			
			// 키워드 특수문자제거
			paCopnGoods.setKeyWord(convertKeyword(paramMap, paCopnGoods.getKeyWord()));

			// 쿠팡 매핑 옵션 조회
			copnGoodsAttri = paCopnGoodsService.selectPaCopnGoodsAttriList(paramMap);
			
			// SK스토아 단품 옵션 조회
			goodsdtMapping = paCopnGoodsService.selectPaCopnGoodsdtInfoList(paramMap);
			
			// 쿠팡 구매옵션 조회
			copnGoodsUserAttri = paCopnGoodsService.selectPaCopnGoodsUserAttriList(paramMap);
			
			// 쿠팡 검색옵션 조회
			copnGoodsUserSearchAttri = paCopnGoodsService.selectPaCopnGoodsUserSearchAttriList(paramMap);
			
			// 프로모션 조회
			paPromoTargetList = paCopnGoodsService.selectPaPromoTarget(paramMap);

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.COUPANG.code(), paCode);

			// 레거시 구매옵션 한개로 통일 by 2020.12.02 이후로 소스 검토 필요
			
			// 쿠팡 상품 전문
			Product product = createProduct(paCopnGoods, goodsdtMapping, copnGoodsAttri, copnGoodsUserAttri, copnGoodsUserSearchAttri, goodsOffer, goodsPrice,
					transServiceNo);

			log.info("상품수정 API 호출 {}", goodsCode);
			String sellerProductId = productApiService.updateProduct(goodsCode, product, paCode, transServiceNo);

			if (StringUtils.hasText(sellerProductId)) {
				
				result.setCode("200");
				result.setMessage(PaCopnApiRequest.API_SUCCESS_CODE);
				
				// TPACOPNGOODS에 결과 UPDATE
				paCopnGoods.setSellerProductId(sellerProductId);
				paCopnGoods.setApprovalStatus("15");
				paCopnGoods.setModifyId(procId);
				paCopnGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paCopnGoods.setLastModifyDate(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));

				rtnMsg = paCopnGoodsService.savePaCopnGoodsTx(paCopnGoods, goodsdtMapping, paPromoTargetList, goodsPrice);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + paCopnGoods.getGoodsCode());
				}

			} else {
				result.setCode("500");
				result.setMessage("no data found");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

			// 정보고시/배송비정책오류 반려
			if (isRejectMsg(result.getMessage())) {
				paCopnGoods.setPaSaleGb("30"); // 판매중지
    			paCopnGoods.setPaStatus("90"); //연동제외
    			paCopnGoods.setTransSaleYn("1");
				paCopnGoods.setReturnNote(StringUtil.truncate(result.getMessage(), 500));
				paCopnGoods.setModifyId(procId);
				try {
					paCopnGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					rtnMsg = paCopnGoodsService.savePaCopnGoodsFailTx(paCopnGoods);
				} catch (Exception e) {
					result.setCode("500");
					result.setMessage(e.getMessage());
					log.error(e.getMessage(), e);
				}
			} else if (result.getMessage().indexOf("판매중인 상품은 삭제할 수 없습니다") > -1 ) {
    			paCopnGoods.setApprovalStatus("15");
				paCopnGoods.setReturnNote("판매중인 상품은 삭제할 수 없습니다");
				paCopnGoods.setModifyId(procId);
				try {
					paCopnGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					rtnMsg = paCopnGoodsService.savePaCopnGoodsFailTx(paCopnGoods);
				} catch (Exception e) {
					result.setCode("500");
					result.setMessage(e.getMessage());
					log.error(e.getMessage(), e);
				}
			} else if (result.getMessage().indexOf("입력되지 않았습니다") > -1 ) { // 쿠팡 필수구매옵션 에러(수기중단 아닌 판매중지처리)
				paCopnGoods.setPaSaleGb("30"); // 판매중지
    			paCopnGoods.setPaStatus("30"); //연동제외
    			paCopnGoods.setTransSaleYn("1");
				paCopnGoods.setReturnNote(StringUtil.truncate(result.getMessage(), 500));
				paCopnGoods.setModifyId(procId);
				try {
					paCopnGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					rtnMsg = paCopnGoodsService.savePaCopnGoodsFailTx(paCopnGoods);
				} catch (Exception e) {
					result.setCode("500");
					result.setMessage(e.getMessage());
					log.error(e.getMessage(), e);
				}
			}
			
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 상품수정 API End - {} =====", goodsCode);
		}
		return result;
	}

	private boolean isRejectMsg(String rejectMsg) {
		
		if (!StringUtils.hasText(rejectMsg)) return false;

		String[] rejectMatch = new String[] { "고시정보", "배송비", "옵션값은 최대 30자", "형식에 맞게" };
		
 	    return Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s));
	}
	

	/**
	 * 검색어 특수문자 제거
	 * 
	 * @param paramMap
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	private String convertKeyword(ParamMap paramMap, String keyword) throws Exception {
		List<HashMap<String, Object>> paGoodsLimitCharMapList;
		paGoodsLimitCharMapList = paCopnGoodsService.selectGoodsLimitCharList(paramMap);

		// 검색어 특수문자 치환
		if (keyword != null) {
			if (paGoodsLimitCharMapList != null) {
				for (HashMap<String, Object> paGoodsLimitCharMap : paGoodsLimitCharMapList) {
					String replaceChar = paGoodsLimitCharMap.get("REPLACE_CHAR").toString() == null ? ""
							: paGoodsLimitCharMap.get("REPLACE_CHAR").toString();
					keyword = keyword.replaceAll(paGoodsLimitCharMap.get("LIMIT_CHAR").toString(), replaceChar);
				}
			}
			keyword = keyword.trim();
			keyword = keyword.replaceAll("   ", " ");
			keyword = keyword.replaceAll("  ", " ");
			keyword = keyword.replaceAll(" ", " ");
			return keyword;
		}
		return null;
	}

	/**
	 * 기술서와 공지사항 설정
	 * 
	 * @param paCopnGoods
	 */
	private void settingDescribeExt(PaCopnGoodsVO paCopnGoods) {
		
		// 상품구성템플릿
		String goodsCom = StringUtils.hasText(paCopnGoods.getGoodsCom())
				? ("<div style='line-height: 2.0em; font-family: NanumBarunGothic; font-size: 19px;'><div><h4>&middot;&nbsp;상품구성<h4><h4>"
						+ paCopnGoods.getGoodsCom() + "<h4></div></div>")
				: "";

//		// 웹기술서
		paCopnGoods.setDescribeExt(
				"<div align='center'><img alt='' src='" + paCopnGoods.getTopImage() + "' /><br /><br /><br />" // 상단이미지 
				+ (StringUtils.hasText(paCopnGoods.getCollectImage()) ?
						"<img alt='' src='" + paCopnGoods.getCollectImage() + "' /><br /><br /><br />" : "") // 착불이미지
				+ goodsCom // 상품구성
				+ paCopnGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />" // 기술서
				+ "<img alt='' src='" + paCopnGoods.getBottomImage() + "' /></div>"); //하단이미지

		// 제휴 공지사항
		if(StringUtils.hasText(paCopnGoods.getNoticeExt())) {
			paCopnGoods.setDescribeExt(paCopnGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + paCopnGoods.getDescribeExt());
		}
	}
	
	private Product createProduct(PaCopnGoodsVO paCopnGoods, List<PaGoodsdtMapping> paGoodsDtMappingList,
			List<PaCopnGoodsAttri> paCopnGoodsAttriList, List<PaCopnGoodsUserAttri>paCopnGoodsUserAttriList, List<PaCopnGoodsUserAttri>paCopnGoodsUserSearchAttriList, List<PaGoodsOfferVO> paGoodsOfferList,
			PaGoodsPriceApply goodsPrice, long transServiceNo) throws Exception {

		Product product;

		if (paCopnGoods.getSellerProductId() != null) {// 상품 수정시 조회 API실행 후 변경되는 부분만 수정한다.

			// 상품조회 API
			product = productApiService.getProduct(paCopnGoods.getGoodsCode(), paCopnGoods.getSellerProductId(),
					paCopnGoods.getPaCode(), transServiceNo);

			if (product == null)
				return null;

		} else {
			product = new Product();
		}

		product.setDisplayCategoryCode(paCopnGoods.getDisplayCategoryCode());
        product.setSellerProductName(paCopnGoods.getDisplayProductName());
		product.setSaleStartedAt(
				DateUtil.timestampToString(paCopnGoods.getSaleStartDate(), DateUtil.COPN_T_DATETIME_FORMAT));
		product.setSaleEndedAt(
				DateUtil.timestampToString(paCopnGoods.getSaleEndDate(), DateUtil.COPN_T_DATETIME_FORMAT));
		product.setDisplayProductName("");

		if (StringUtils.hasText(paCopnGoods.getBrandName())) {
			product.setBrand(paCopnGoods.getBrandName().replaceAll("㈜|(주)", ""));
		} else {
			product.setBrand("");
		}
        product.setGeneralProductName(paCopnGoods.getGeneralProductName());

		if ("1".equals(paCopnGoods.getCopnFreshYn())) {
			product.setDeliveryMethod("COLD_FRESH");
		} else if ("1".equals(paCopnGoods.getInstallYn())) {
			product.setDeliveryMethod("VENDOR_DIRECT");
		} else if ("1".equals(paCopnGoods.getCreateYn())) {
			product.setDeliveryMethod("MAKE_ORDER");
		} else {
			product.setDeliveryMethod("SEQUENCIAL");
		}

		product.setDeliveryCompanyCode("DIRECT");

		if (paCopnGoods.getOrdCost() > 0) { // 개별/조건부
			switch (paCopnGoods.getShipCostCode().substring(0, 2)) {
			case "FR": // SK스토아 무료 배송비 정책
				product.setDeliveryChargeType("FREE");
				product.setUnionDeliveryType("UNION_DELIVERY");
				product.setDeliveryCharge(0);
				product.setFreeShipOverAmount(0);
				break;
			case "CN":
			case "PL": // 조건부
				product.setDeliveryChargeType("CONDITIONAL_FREE");
				product.setUnionDeliveryType("UNION_DELIVERY");
				product.setDeliveryCharge(paCopnGoods.getOrdCost());
				product.setFreeShipOverAmount(paCopnGoods.getShipCostBaseAmt());
				break;
			default: // 상품별
				product.setDeliveryChargeType("NOT_FREE");
				product.setUnionDeliveryType("NOT_UNION_DELIVERY");
				product.setDeliveryCharge(paCopnGoods.getOrdCost());
				product.setFreeShipOverAmount(0);
				break;
			}
		} else { // 무료배송
			product.setDeliveryChargeType("FREE");
			product.setUnionDeliveryType("NOT_UNION_DELIVERY");
			product.setDeliveryCharge(0);
			product.setFreeShipOverAmount(0);
		}

		// 착불 확인
		if ("1".equals(paCopnGoods.getCollectYn())) {
			product.setDeliveryChargeType("CHARGE_RECEIVED");
			product.setUnionDeliveryType("NOT_UNION_DELIVERY");
		}

		product.setDeliveryChargeOnReturn(0);
		product.setReturnCharge(paCopnGoods.getReturnCost());

		product.setRemoteAreaDeliverable("N"); // 도서산간 배송여부
		product.setReturnCenterCode("NO_RETURN_CENTERCODE");
		product.setReturnChargeName(paCopnGoods.getReturnChargeName());
		product.setCompanyContactNumber(paCopnGoods.getCompanyContactNumber());
		product.setReturnZipCode(paCopnGoods.getReturnZipCode());
		product.setReturnAddress(paCopnGoods.getReturnAddress());
		if (paCopnGoods.getReturnAddressDetail() == null) {
			product.setReturnAddressDetail(" "); // 반품지 주소 상세
		} else {
			product.setReturnAddressDetail(paCopnGoods.getReturnAddressDetail());
		}
		product.setAfterServiceInformation(paCopnGoods.getRemark()); // A/S 안내
		product.setAfterServiceContactNumber(paCopnGoods.getCsTel());// A/S 전화번호
		product.setOutboundShippingPlaceCode(paCopnGoods.getOutBoundShippingPlaceCode());

		List<Item> items = new ArrayList<Item>();

		List<Item> updateItems = product.getItems();
		
		for (PaGoodsdtMapping paGoodsdtMapping : paGoodsDtMappingList) {
			Item item = null ;
			if (paGoodsdtMapping.getPaOptionCode() != null) {
				Optional<Item> updateItem = updateItems.stream().filter(
						option -> option.getVendorItemId().equals(paGoodsdtMapping.getPaOptionCode())).findFirst();
				if (updateItem.isPresent()) {
					item = updateItem.get();
				} 
			} 
			
			if (item == null) item = new Item();
			
			// 프로모션개선 적용
			item.setOriginalPrice((long) goodsPrice.getSalePrice());
			item.setSalePrice((long) goodsPrice.getBestPrice());

			// 판매가능수량
			item.setMaximumBuyCount(Integer.parseInt(paGoodsdtMapping.getTransOrderAbleQty()));

			// 기간제한
			if (paCopnGoods.getDirectShipYn().equals("1")) { // 직택배일 경우
				item.setMaximumBuyForPerson(1); // 인당최대구매수량
				if (paCopnGoods.getCustOrdQtyCheckYn() == 1) {
					if (paCopnGoods.getCustOrdQtyCheckTerm() != 0) {
						item.setMaximumBuyForPersonPeriod((int) paCopnGoods.getCustOrdQtyCheckTerm());
					} else { // 0이면 전체기간을 의미이므로 365일 입력
						item.setMaximumBuyForPersonPeriod(365);
					}
				} else { // 기간당 살 수 있는 수량 체크가 없다면
					item.setMaximumBuyForPersonPeriod(1);
				}
			} else if (paCopnGoods.getOrderMaxQty() > 0) {
				item.setMaximumBuyForPerson((int) paCopnGoods.getOrderMaxQty());
				item.setMaximumBuyForPersonPeriod(1);
			} else if (paCopnGoods.getCustOrdQtyCheckYn() == 1) {
				item.setMaximumBuyForPerson(Integer.parseInt(paCopnGoods.getTermOrderQty()));
				item.setMaximumBuyForPersonPeriod(
						paCopnGoods.getCustOrdQtyCheckTerm() == 0 ? 365 : (int) paCopnGoods.getCustOrdQtyCheckTerm());
			} else {
				item.setMaximumBuyForPerson(0);
				item.setMaximumBuyForPersonPeriod(1);
			}

			// 상품 발송 예정일 템플릿 조회
			item.setOutboundShippingTimeDay(Integer
					.parseInt((String) paCopnGoodsService.selectGoodsForCopnPolicy(paCopnGoods).get("DURATION")));

			item.setUnitCount(0); // 단위 수량, 개당 가격 필요하지 않을 경우 '0'

			if ("1".equals(paCopnGoods.getAdultYn())) {
				item.setAdultOnly("ADULT_ONLY");
			} else {
				item.setAdultOnly("EVERYONE");
			}
			if ("1".equals(paCopnGoods.getTaxYn())) {
				item.setTaxType("TAX");
			} else {
				item.setTaxType("FREE");
			}
			item.setParallelImported("NOT_PARALLEL_IMPORTED"); // 병행수입 아님
			item.setOverseasPurchased("NOT_OVERSEAS_PURCHASED"); // 구매대행 아님
			item.setPccNeeded(false); // 개인통관번호 입력하지 않음
			item.setExternalVendorSku(paGoodsdtMapping.getGoodsCode() + paGoodsdtMapping.getGoodsdtCode()); // 업체상품코드(SK스토아
																											// 단품코드)
			item.setBarcode("");
			item.setEmptyBarcode(true);
			item.setEmptyBarcodeReason(systemService.getSysdatetimeToString());
			item.setModelNo("");

			// 상품 인증정보
			List<Certification> certifications = new ArrayList<Certification>();
			Certification certification = new Certification();
			certification.setCertificationType(paCopnGoods.getCertificationType());
			certification.setCertificationCode(paCopnGoods.getCertificationCode());
			certifications.add(certification);
			item.setCertifications(certifications);

			// 검색어
			if (paCopnGoods.getKeyWord() != null) {
				String[] keywords = paCopnGoods.getKeyWord().replace(", ", ",").split(",");

				List<String> searchTags = new ArrayList<String>(20);
				for (String keyword : keywords) {
					if (searchTags.size() == 20) break; // 검색어는 20개까지만 허용
					if (keyword.length() <= 20) { // 키워드 길이가 20자내인 경우만 허용
						searchTags.add(keyword);
					}
				}
				item.setSearchTags(searchTags);
			}

			// 상품이미지
			List<Image> images = new ArrayList<Image>();
			int imageOrder = 0;

			Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
			String imageServer = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));

			if(paCopnGoods.getInfoImageG() != null) {
				images.add(createImage(paCopnGoods.getInfoImageG(), imageOrder++, "REPRESENTATION",
						imageServer + paCopnGoods.getImageUrl()));
			}else if (paCopnGoods.getImageP() != null) {
				images.add(createImage(paCopnGoods.getImageP(), imageOrder++, "REPRESENTATION",
						imageServer + paCopnGoods.getImageUrl()));
			}
			if (paCopnGoods.getImageAP() != null) {
				images.add(createImage(paCopnGoods.getImageAP(), imageOrder++, "DETAIL",
						imageServer + paCopnGoods.getImageUrl()));
			}
			if (paCopnGoods.getImageBP() != null) {
				images.add(createImage(paCopnGoods.getImageBP(), imageOrder++, "DETAIL",
						imageServer + paCopnGoods.getImageUrl()));
			}
			if (paCopnGoods.getImageCP() != null) {
				images.add(createImage(paCopnGoods.getImageCP(), imageOrder++, "DETAIL",
						imageServer + paCopnGoods.getImageUrl()));
			}
			if (paCopnGoods.getImageDP() != null) {
				images.add(createImage(paCopnGoods.getImageDP(), imageOrder++, "DETAIL",
						imageServer + paCopnGoods.getImageUrl()));
			}
			item.setImages(images);

			// 정보고시
			List<Notice> notices = new ArrayList<Notice>();
			for (PaGoodsOfferVO paCopnGoodsOffer : paGoodsOfferList) {
				Notice notice = new Notice();
				notice.setNoticeCategoryDetailName(paCopnGoodsOffer.getPaOfferCodeName());// 상품고시정보카테고리상세명
				notice.setNoticeCategoryName(paCopnGoodsOffer.getPaOfferTypeName());
				notice.setContent(paCopnGoodsOffer.getPaOfferExt());
				notices.add(notice);
			}
			item.setNotices(notices);

			// 옵션 속성
			List<Attribute> attributes = new ArrayList<Attribute>();
			String optionName = "";
			String itemNameValue = "";
			String mappingValue[] = null;
			
			// 쿠팡 수동 옵션 존재 여부
			int userAttriYn = 0;

			// 쿠팡 수동 옵션 존재 할 경우, TPACOPNGOODSUSERATTRI 기준으로 구매옵션 생성
			if (paCopnGoodsUserAttriList.size() != 0) {
				for (PaCopnGoodsUserAttri paCopnGoodsUserAttri : paCopnGoodsUserAttriList) {
					if(paGoodsdtMapping.getGoodsdtCode().equals(paCopnGoodsUserAttri.getGoodsdtCode())) {
						Attribute atrribute = new Attribute();
						atrribute.setAttributeTypeName(paCopnGoodsUserAttri.getAttributeTypeName());
						if ("없음".equals(paCopnGoodsUserAttri.getUsableUnit())) {
							atrribute.setAttributeValueName(paCopnGoodsUserAttri.getAttributeValueName());
							itemNameValue += paCopnGoodsUserAttri.getAttributeValueName() + " ";
						} else {
							atrribute.setAttributeValueName(paCopnGoodsUserAttri.getAttributeValueName() + paCopnGoodsUserAttri.getUsableUnit());
							itemNameValue += paCopnGoodsUserAttri.getAttributeValueName() + paCopnGoodsUserAttri.getUsableUnit() + " ";
						} 
						attributes.add(atrribute);
						userAttriYn = 1; // 쿠팡 수동옵션 존재 여부
					}
				}
			}
			
			// 쿠팡 수동 옵션 존재하지 않는다면, 수동 매핑 기준으로 구매옵션 생성
			if(userAttriYn == 0) {
				if (paCopnGoodsAttriList.size() == 0) {
					Attribute attribute = new Attribute();
					attribute.setAttributeValueName("");
					attributes.add(attribute);
				} else {
					for (PaCopnGoodsAttri paCopnGoodsAttri : paCopnGoodsAttriList) {
						String optionValue = "";
	
						if (paCopnGoodsAttri.getAttributeTypeMapping() != null
								|| paCopnGoodsAttri.getAttributeValueName() != null) {
							if (paCopnGoodsAttri.getAttributeTypeMapping() != null) {
								if ("색상/크기/무늬/형태".equals(paCopnGoodsAttri.getAttributeTypeName())) {
									optionValue = paGoodsdtMapping.getGoodsdtInfo() + "/";
								} else {
									mappingValue = paCopnGoodsAttri.getAttributeTypeMapping().split(",");
	
									for (int l = 0; l < mappingValue.length; l++) {
										switch (mappingValue[l]) {
										case "색상":
											optionValue = optionValue.concat(paGoodsdtMapping.getColorName() + "/");
											break;
										case "사이즈":
											optionValue = optionValue.concat(paGoodsdtMapping.getSizeName() + "/");
											break;
										case "무늬": // 미사용
											optionValue = optionValue.concat(paGoodsdtMapping.getPatternName() + "/");
											break;
										case "형태": // 미사용
											optionValue = optionValue.concat(paGoodsdtMapping.getFormName() + "/");
											break;
										case "기타":
											optionValue = optionValue.concat(paGoodsdtMapping.getOtherText() + "/");
											break;
										}
									}
								}
								if (!"".equals(optionValue)) {
									optionValue = optionValue.substring(0, optionValue.length() - 1); // 마지막 문자 자르기
								}
							}
							Attribute atrribute = new Attribute();
							atrribute.setAttributeTypeName(paCopnGoodsAttri.getAttributeTypeName());
							if (paCopnGoodsAttri.getAttributeValueName() == null) {
								atrribute.setAttributeValueName(optionValue);
								optionName = optionName + " " + optionValue;
							} else {
								atrribute.setAttributeValueName(paCopnGoodsAttri.getAttributeValueName());
								optionName = optionName + " " + paCopnGoodsAttri.getAttributeValueName();
							}
							atrribute.setExposed("EXPOSED");
							atrribute.setEditable("true");
							attributes.add(atrribute);
						}
					}
				}
			}
			
			// 필수 검색옵션 존재시
			if (paCopnGoodsUserSearchAttriList.size() != 0) {
				for (PaCopnGoodsUserAttri paCopnGoodsUserSearchAttri : paCopnGoodsUserSearchAttriList) {
					if(paGoodsdtMapping.getGoodsdtCode().equals(paCopnGoodsUserSearchAttri.getGoodsdtCode())) {
						Attribute atrribute = new Attribute();
						atrribute.setAttributeTypeName(paCopnGoodsUserSearchAttri.getAttributeTypeName());
						if ("없음".equals(paCopnGoodsUserSearchAttri.getUsableUnit())) {
							atrribute.setAttributeValueName(paCopnGoodsUserSearchAttri.getAttributeValueName());
							itemNameValue += paCopnGoodsUserSearchAttri.getAttributeValueName() + " ";
						} else {
							atrribute.setAttributeValueName(paCopnGoodsUserSearchAttri.getAttributeValueName() + (paCopnGoodsUserSearchAttri.getUsableUnit() == null ? "" : paCopnGoodsUserSearchAttri.getUsableUnit()));
							itemNameValue += paCopnGoodsUserSearchAttri.getAttributeValueName() + (paCopnGoodsUserSearchAttri.getUsableUnit() == null ? "" : paCopnGoodsUserSearchAttri.getUsableUnit()) + " ";
						}
						atrribute.setExposed("NONE");
						attributes.add(atrribute);
					}
				}
			}

			if (paCopnGoods.getSellerProductId() != null) { // 상품 수정시에만 상품 조회 API를 통한 옵션 목록 중 필수값 그대로 셋팅
				List<Attribute> updateAttributes = item.getAttributes();
				
				if (updateAttributes != null) {
					for (int p = 0; p < updateAttributes.size(); p++) {
						Attribute atrribute = new Attribute();
						
						boolean attributeExists = false;
						
						for (Attribute existingAttribute : attributes) {//상품 등록때 등록했던 필수 옵션 값 존재시 continue
							if (existingAttribute.getAttributeTypeName().equals(updateAttributes.get(p).getAttributeTypeName()) &&
									"EXPOSED".equals(updateAttributes.get(p).getExposed())) {
								attributeExists = true;
								break;
							}
						}
						
						if (attributeExists) {
							continue;
						}
						
						if ("EXPOSED".equals(updateAttributes.get(p).getExposed())) { // 쿠팡에서 등록한 필수 옵션만 따라 넣기
							atrribute.setAttributeTypeName(updateAttributes.get(p).getAttributeTypeName());
							atrribute.setAttributeValueName(updateAttributes.get(p).getAttributeValueName());
						} else {
							continue;
						}
						attributes.add(atrribute);
					}
				}
			}
				
			
			item.setAttributes(attributes);

			// 업체상품옵션명, 각각의 아이템에 중복되지 않도록 기입
			if (paCopnGoodsUserAttriList.size() != 0) {
				item.setItemName(itemNameValue);
			} else {
				item.setItemName(optionName.equals("") ? "단일상품" : optionName);
			}

			// 기술서
			List<Content> contents = new ArrayList<Content>();

			Content content = new Content();
			content.setContentsType("TEXT");

			List<ContentDetail> contentDetails = new ArrayList<ContentDetail>();

			ContentDetail contentDetail = new ContentDetail();

			// 기술서 style 제거
			if (!"".equals(ComUtil.NVL(paCopnGoods.getDescribeExt()))) {
				String attributesToRemove = "width|height|style";
				String orgString = paCopnGoods.getDescribeExt();
				String tmpString = ComUtil.cleanHtmlFragment(orgString, attributesToRemove);
				String tmp2String = ComUtil.parse(tmpString);
				paCopnGoods.setDescribeExt(tmp2String);
			}

			contentDetail.setContent(paCopnGoods.getDescribeExt());
			contentDetail.setDetailType("TEXT");
			contentDetails.add(contentDetail);

			content.setContentDetails(contentDetails);
			contents.add(content);
			item.setContents(contents);

			if (!"0".equals(paCopnGoods.getGoodsStts())) {
				item.setOfferCondition("USED_BEST");
			} else {
				item.setOfferCondition("NEW");
			}

			items.add(item);
		}

		product.setItems(items);
		product.setManufacture(paCopnGoods.getMakecoName());

		return product;
	}

	private Image createImage(String imageName, int imageOrder, String imageType, String imagePath) {
		Image image = new Image();
		image.setImageOrder(imageOrder);
		image.setImageType(imageType);
        if(imageName.contains("info") || imageName.contains("type")) { //1000*1000 대표이미지 연동
            image.setVendorPath(imagePath + imageName);
        }else {
            image.setVendorPath(imagePath + imageName + "/dims/resize/600X600");
        }
        
		return image;
	}

}
