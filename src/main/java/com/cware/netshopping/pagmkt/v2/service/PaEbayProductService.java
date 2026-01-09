package com.cware.netshopping.pagmkt.v2.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.PaSaleGb;
import com.cware.netshopping.common.code.PaStatus;
import com.cware.netshopping.common.code.PolicyType;
import com.cware.netshopping.common.code.SaleGb;
import com.cware.netshopping.common.code.ShipCostFlag;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaGmktGoods;
import com.cware.netshopping.domain.model.PaGmktPolicy;
import com.cware.netshopping.domain.model.PaGoodsLimitChar;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsLimitCharMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.service.SyncProductService;
import com.cware.netshopping.pagmkt.goods.service.PaGmktGoodsService;
import com.cware.netshopping.pagmkt.v2.domain.AddtionalInfo;
import com.cware.netshopping.pagmkt.v2.domain.Bundle;
import com.cware.netshopping.pagmkt.v2.domain.BuyableQuantity;
import com.cware.netshopping.pagmkt.v2.domain.Catalog;
import com.cware.netshopping.pagmkt.v2.domain.Category;
import com.cware.netshopping.pagmkt.v2.domain.CertInfo;
import com.cware.netshopping.pagmkt.v2.domain.CertInfoDetail;
import com.cware.netshopping.pagmkt.v2.domain.CertInfoIac;
import com.cware.netshopping.pagmkt.v2.domain.Combination;
import com.cware.netshopping.pagmkt.v2.domain.Description;
import com.cware.netshopping.pagmkt.v2.domain.Descriptions;
import com.cware.netshopping.pagmkt.v2.domain.EbayGoods;
import com.cware.netshopping.pagmkt.v2.domain.EbayGoodsDescribe;
import com.cware.netshopping.pagmkt.v2.domain.EbayGoodsOffer;
import com.cware.netshopping.pagmkt.v2.domain.EsmSite;
import com.cware.netshopping.pagmkt.v2.domain.Images;
import com.cware.netshopping.pagmkt.v2.domain.Independent;
import com.cware.netshopping.pagmkt.v2.domain.IndependentDetail;
import com.cware.netshopping.pagmkt.v2.domain.CombinationDetail;
import com.cware.netshopping.pagmkt.v2.domain.RecommendedOptName;
import com.cware.netshopping.pagmkt.v2.domain.RecommendedOptName1;
import com.cware.netshopping.pagmkt.v2.domain.RecommendedOptName2;
import com.cware.netshopping.pagmkt.v2.domain.ItemAddtionalInfo;
import com.cware.netshopping.pagmkt.v2.domain.ItemBasicInfo;
import com.cware.netshopping.pagmkt.v2.domain.MultiLanguage;
import com.cware.netshopping.pagmkt.v2.domain.OfficialNotice;
import com.cware.netshopping.pagmkt.v2.domain.OfficialNoticeDetail;
import com.cware.netshopping.pagmkt.v2.domain.Option;
import com.cware.netshopping.pagmkt.v2.domain.Origin;
import com.cware.netshopping.pagmkt.v2.domain.OverseaSales;
import com.cware.netshopping.pagmkt.v2.domain.Pcs;
import com.cware.netshopping.pagmkt.v2.domain.Policy;
import com.cware.netshopping.pagmkt.v2.domain.Price;
import com.cware.netshopping.pagmkt.v2.domain.Product;
import com.cware.netshopping.pagmkt.v2.domain.ProductResponse;
import com.cware.netshopping.pagmkt.v2.domain.Qty;
import com.cware.netshopping.pagmkt.v2.domain.RecommendMultiLanguage;
import com.cware.netshopping.pagmkt.v2.domain.ReturnAndExchange;
import com.cware.netshopping.pagmkt.v2.domain.SafetyCert;
import com.cware.netshopping.pagmkt.v2.domain.SafetyCerts;
import com.cware.netshopping.pagmkt.v2.domain.SellerDiscount;
import com.cware.netshopping.pagmkt.v2.domain.SellerDiscountSite;
import com.cware.netshopping.pagmkt.v2.domain.SellerShop;
import com.cware.netshopping.pagmkt.v2.domain.Shipping;
import com.cware.netshopping.pagmkt.v2.domain.Site;
import com.cware.netshopping.pagmkt.v2.domain.SiteDetail;
import com.cware.netshopping.pagmkt.v2.domain.SiteIntValue;
import com.cware.netshopping.pagmkt.v2.domain.SiteNoValue;
import com.cware.netshopping.pagmkt.v2.domain.SiteProduct;
import com.cware.netshopping.pagmkt.v2.domain.SiteValue;
import com.cware.netshopping.pagmkt.v2.repository.PaGmktGoodsMapper;

@Service
public class PaEbayProductService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;

	@Autowired
	@Qualifier("pagmkt.goods.paGmktGoodsService")
	private PaGmktGoodsService paGmktGoodsService;

	@Autowired
	PaEbayProductApiService productApiService;

	@Autowired
	PaEbayResultService  ebayResultService;

	@Autowired
	EbayProductService  ebayProductService;
	
	@Autowired
	SyncProductService syncProductService;

	@Autowired
	PaGmktGoodsMapper gmktGoodsMapper;
	
	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;
	
	@Autowired
	PaGoodsLimitCharMapper limitCharMapper;
	
	@Autowired
	TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 이베이 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public ResponseMsg transProduct(String goodsCode, String paCode, String procId) {

		ResponseMsg result = new ResponseMsg("", "");
		
		// 상품동기화 
		PaGoodsSync sync = syncProductService.syncProduct(goodsCode, PaGroup.GMARKET.code(), procId);
		result = sync.getResponseMsg();
		if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) {
			return result;
		}
		
		// 스토아상품조회
		List<EbayGoods> goodsList = gmktGoodsMapper.getGoodsList(goodsCode, paCode);

		int cnt = goodsList.size();
		
		if (cnt == 0) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("연동 대상 상품이 아닙니다.");
			return result;
		}

		EbayGoods ebayGoods = goodsList.get(0);
		ebayGoods.setDual(cnt == 2 ? true: false);
		
		boolean isStop = false;
		boolean isModify = false;
		
		for (EbayGoods goods : goodsList) {
			
			// 입점요청중인건은 처리하지 않음
			if(PaStatus.PROCEEDING.code().equals(goods.getPaStatus())) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
				result.setMessage("입점요청중인 상품입니다.");
				return result;
			}

			if (PaGroup.GMARKET.code().equals(goods.getPaGroupCode())) {
				ebayGoods.setGmktGoods(goods);
			} else {
				ebayGoods.setIacGoods(goods);
			}
			
			if (ebayGoods.getPaStatus().compareTo(goods.getPaStatus()) < 0) {
				ebayGoods.setPaStatus(goods.getPaStatus());
			}
			if (ebayGoods.getTransTargetYn().compareTo(goods.getTransTargetYn()) < 0) {
				ebayGoods.setTransTargetYn(goods.getTransTargetYn());
			}

			if (PaSaleGb.SUSPEND.code().equals(goods.getPaSaleGb())) {
				if ("1".equals(goods.getTransSaleYn())
						&& PaStatus.COMPLETE.code().compareTo(goods.getPaStatus()) <= 0) {
					isStop = true;
				}
			} else if ("1".equals(goods.getTransTargetYn())) {
				isModify = true;
			}
		}

		if (PaStatus.REQUEST.code().equals(ebayGoods.getPaStatus())
				|| PaStatus.REJECT.code().equals(ebayGoods.getPaStatus())) {

			// 동기화에서 필터링된 경우
			if (ebayGoods.isDual()) {
				if (sync.getFilterPaGroup().contains(ebayGoods.getGmktGoods().getPaGroupCode()) 
						&& sync.getFilterPaGroup().contains(ebayGoods.getIacGoods().getPaGroupCode())) {
					result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
					result.setMessage("입점 제외된 상품입니다.");
					return result;
				}
			} else {

				if (sync.getFilterPaGroup().contains(ebayGoods.getPaGroupCode()) ) {
					result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
					result.setMessage("입점 제외된 상품입니다.");
					return result;
				}
			}
			
			// 신규입점
			result = registerProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				result.setMessage("입점완료되었습니다.");
			}
			return result;
		}
		
		// 판매중지 대상건이 있는 경우 
		if (isStop) {
			PaTransService transService = ebayProductService.stopSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			if ("1".equals(transService.getSuccessYn())) {
				result.setMessage("판매중지되었습니다.");
			} else {
				result.setMessage(transService.getResultMsg());
			}
		}
		
		// 상품수정대상건이 있는 경우
		if (isModify) {
			// 상품수정
			result = updateProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				result.setMessage("수정완료되었습니다.");				
			}
			return result;
		} 
		
		if (isStop) return result;
		
		result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
		result.setMessage("변경된 사항이 없습니다.");
		
		return result;
	}
	
	/**
	 * 이베이 신규상품 입점
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
		serviceLog.setServiceNote("[API]이베이-상품입점");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.GMARKET.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// 상품입점
		ResponseMsg result = callRegisterProduct(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		// 입점요청중 해제
		gmktGoodsMapper.updateClearProceeding(goodsCode, paCode, procId);
		
		return result;
		
	}
	
	/**
	 * 이베이 상품 수정
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
		serviceLog.setServiceNote("[API]이베이-상품수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.GMARKET.code());
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
	 * 이베이 상품 조회
	 * 
	 * @param goodsCode
	 * @param esmGoodsCode
	 * @param paCode
	 * @param paGroupCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public Product getProduct(String goodsCode, String esmGoodsCode, String paCode, String paGroupCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]이베이-상품조회");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.GMARKET.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		Product product = null;
		
		try {
			// 상품조회
			product = productApiService.getProduct(goodsCode, esmGoodsCode, paCode, paGroupCode, serviceLog.getTransServiceNo());
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(PaEbayApiRequest.API_SUCCESS_CODE);
					
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
		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		log.info("===== 상품등록서비스 Start - {} =====", goodsCode);

		// 상품정보 조회
		List<EbayGoods> goodsList = gmktGoodsMapper.selectGoodsTransTaget(goodsCode, paCode, false);
		
		if (goodsList.size() == 0) {
			result.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
			result.setMessage("처리할 내역이 없습니다.");
			return result;
		} 

		if (goodsList.size() > 2) {
			result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
			result.setMessage("입점 데이터 확인이 필요합니다.");
			return result;
		} 

		EbayGoods ebayGoods = goodsList.get(0);

		if (!SaleGb.FORSALE.code().equals(ebayGoods.getSaleGb())) {
			result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
			result.setMessage("판매상태가 아닙니다.");
			return result;
		}

		try {
			ebayGoods.setDual(goodsList.size() == 2 ? true: false);
			
			String paGroupCode = null;
			
			for (EbayGoods goods : goodsList) {
				if (PaGroup.GMARKET.code().equals(goods.getPaGroupCode())) {
					ebayGoods.setGmktGoods(goods);
					paGroupCode = PaGroup.GMARKET.code();
				} else {
					ebayGoods.setIacGoods(goods);
					paGroupCode = PaGroup.AUCTION.code();
				}
			}
			
			// 동시입점 상태 체크
			if (ebayGoods.isDual()) {
				// 지마켓/옥션 둘중 하나만 입점되어도 신규 등록 불가 (수정사용)
				if (PaStatus.COMPLETE.code().compareTo(ebayGoods.getGmktGoods().getPaStatus()) <= 0 ||
						PaStatus.COMPLETE.code().compareTo(ebayGoods.getIacGoods().getPaStatus()) <= 0) {
					result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
					result.setMessage("이전에 입점되었습니다.");
					return result;
				}
				paGroupCode = PaGroup.GMARKET.code() + "," + PaGroup.AUCTION.code();
			}

			// 정보고시 조회
			List<EbayGoodsOffer> goodsOfferList = gmktGoodsMapper.selectGoodsOffer(goodsCode);
			if (goodsOfferList.size() < 1) {
				result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 상품설명 조회
			EbayGoodsDescribe goodsDescribe = gmktGoodsMapper.selectGoodsDescribe(goodsCode, paCode);
			if (goodsDescribe == null
					|| (goodsDescribe.getDescribeExt() == null && goodsDescribe.getGoodsCom() == null)) {
				result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
				result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}
			if (goodsDescribe.getDescribeExt() == null) goodsDescribe.setDescribeExt("");
			
			// 입점요청중
			gmktGoodsMapper.updateProceeding(goodsCode, paCode, procId);

			ebayGoods.setModifyId(procId);
			ebayGoods.setGmkt(ebayGoods.getGmktGoods() == null ? false : true);
			ebayGoods.setIac(ebayGoods.getIacGoods() == null ? false : true);
			
			// 프로모션 개선
			if(ebayGoods.isGmkt()) {
				ebayGoods.setGmktPrice(goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.GMARKET.code(), paCode));
				if (ebayGoods.getGmktPrice() == null) {
					result.setCode("404");
					result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
					return result;
				} 
			}
			if(ebayGoods.isIac()) {
				ebayGoods.setIacPrice(goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.AUCTION.code(), paCode));
				if (ebayGoods.getIacPrice() == null) {
					result.setCode("404");
					result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
					return result;
				} 
			}
			
			// 옵션정보
			List<PaGoodsdtMapping> goodsOptionList = gmktGoodsMapper.selectGoodsOption(goodsCode, paCode);
			
			// 이베이 상품 전문
			Product product = createProduct(ebayGoods, goodsOfferList, goodsDescribe, goodsOptionList, transServiceNo);

			log.info("상품등록 API 호출 {}", goodsCode);
			ProductResponse response = productApiService.registerProduct(goodsCode, product, paCode, paGroupCode, transServiceNo);
			
			SiteDetail siteDetail = response.getSiteDetail();
			SiteProduct gmktResult = siteDetail.getGmkt();
			SiteProduct iacResult = siteDetail.getIac();
			
			String goodsNo = response.getGoodsNo();
			

			if (StringUtils.hasText(goodsNo)) {
				
				result.setCode("200");
				result.setMessage(PaEbayApiRequest.API_SUCCESS_CODE);
				
				ebayGoods.setEsmGoodsCode(goodsNo);
				
				if (ebayGoods.isGmkt()) {
					ebayGoods.setStartGmkt(true);
					log.info("지마켓 상품등록결과{} - {}", goodsCode, gmktResult);
					if (StringUtils.hasText(gmktResult.getSiteGoodsNo())) {
						ebayGoods.getGmktGoods().setItemNo(gmktResult.getSiteGoodsNo());
						ebayGoods.getGmktGoods().setPaStatus(PaStatus.COMPLETE.code());
					} else {
//						ebayGoods.getGmktGoods().setReturnNote("G마켓 상품코드의 값이 생성되지 않았습니다.");
						ebayGoods.getGmktGoods().setReturnNote(StringUtil.truncate(gmktResult.getSiteGoodsComment(), 500));
						ebayGoods.getGmktGoods().setPaStatus(PaStatus.REJECT.code());
					}
				}

				if (ebayGoods.isIac()) {
					ebayGoods.setStartIac(true);
					log.info("옥션 상품등록결과{} - {}", goodsCode, iacResult);
					if (StringUtils.hasText(iacResult.getSiteGoodsNo())) {
						ebayGoods.getIacGoods().setItemNo(iacResult.getSiteGoodsNo());
						ebayGoods.getIacGoods().setPaStatus(PaStatus.COMPLETE.code());
					} else {
//						ebayGoods.getIacGoods().setReturnNote("옥션 상품코드의 값이 생성되지 않았습니다.");
						ebayGoods.getIacGoods().setReturnNote(StringUtil.truncate(iacResult.getSiteGoodsComment(), 500));
						ebayGoods.getIacGoods().setPaStatus(PaStatus.REJECT.code());
					}
				}

				// 이베이 상품등록 저장
				rtnMsg = ebayResultService.saveTransProduct(ebayGoods, goodsOptionList);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + goodsCode);
				}
			} else {
				result.setCode("500");
				result.setMessage("no data found");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

            // 반려처리 제외 메시지 체크
            if (!isNotRejectMsg(result.getCode(), result.getMessage())) {
                gmktGoodsMapper.rejectTransTarget(goodsCode, paCode, procId,
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

    /**
     * 반려제외여부
     * 
     * @param resultCode
     * @param rejectMsg
     * @return
     */
    private boolean isNotRejectMsg(String resultCode, String rejectMsg) {

        if ("502".equals(resultCode)) return true;
        
        if (!StringUtils.hasText(rejectMsg) && !"Exception".equals(resultCode))
            return false;

        String[] rejectNotMatch =
                new String[] {"failed to respond"};

        return Arrays.stream(rejectNotMatch).anyMatch(s -> rejectMsg.contains(s));
    }
    
	private ResponseMsg callUpdateProduct(String goodsCode, String paCode, String procId, long transServiceNo) {
		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		log.info("===== 상품수정서비스 Start - {} =====", goodsCode);

		// 상품정보 조회
		List<EbayGoods> goodsList = gmktGoodsMapper.selectGoodsTransTaget(goodsCode, paCode, true);

		if (goodsList.size() == 0) {
			result.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
			result.setMessage("처리할 내역이 없습니다.");
			return result;
		}

		if (goodsList.size() > 2) {
			result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
			result.setMessage("입점 데이터 확인이 필요합니다.");
			return result;
		}

		EbayGoods ebayGoods = goodsList.get(0);

		if (!SaleGb.FORSALE.code().equals(ebayGoods.getSaleGb())) {
			result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
			result.setMessage("판매상태가 아닙니다.");
			return result;
		}

		try {
				
			ebayGoods.setDual(goodsList.size() == 2 ? true: false);
			
			String paGroupCode = null;
			
			for (EbayGoods goods : goodsList) {
				if (PaGroup.GMARKET.code().equals(goods.getPaGroupCode())) {
					ebayGoods.setGmktGoods(goods);
					paGroupCode = PaGroup.GMARKET.code();
				} else {
					ebayGoods.setIacGoods(goods);
					paGroupCode = PaGroup.AUCTION.code();
				}
				if (StringUtils.hasText(goods.getEsmGoodsCode())) {
					ebayGoods.setEsmGoodsCode(goods.getEsmGoodsCode());
				}
			}
			
			// 동시입점 
			if (ebayGoods.isDual()) {
				paGroupCode = PaGroup.GMARKET.code() + "," + PaGroup.AUCTION.code();
			}

			// 정보고시 조회
			List<EbayGoodsOffer> goodsOfferList = gmktGoodsMapper.selectGoodsOffer(goodsCode);
			if (goodsOfferList.size() < 1) {
				result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 상품설명 조회
			EbayGoodsDescribe goodsDescribe = gmktGoodsMapper.selectGoodsDescribe(goodsCode, paCode);
			if (goodsDescribe == null
					|| (goodsDescribe.getDescribeExt() == null && goodsDescribe.getGoodsCom() == null)) {
				result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
				result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}
			if (goodsDescribe.getDescribeExt() == null) goodsDescribe.setDescribeExt("");

			ebayGoods.setModifyId(procId);
			ebayGoods.setGmkt(ebayGoods.getGmktGoods() == null ? false : true);
			ebayGoods.setIac(ebayGoods.getIacGoods() == null ? false : true);

			// 프로모션 개선
			if(ebayGoods.isGmkt()) {
				ebayGoods.setGmktPrice(goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.GMARKET.code(), paCode));
				if (ebayGoods.getGmktPrice() == null) {
					result.setCode("404");
					result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
					return result;
				} 
			}
			if(ebayGoods.isIac()) {
				ebayGoods.setIacPrice(goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.AUCTION.code(), paCode));
				if (ebayGoods.getIacPrice() == null) {
					result.setCode("404");
					result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
					return result;
				} 
			}

			// 옵션정보
			List<PaGoodsdtMapping> goodsOptionList = gmktGoodsMapper.selectGoodsOption(goodsCode, paCode);
			
			// 이베이 상품 전문
			Product product = createProduct(ebayGoods, goodsOfferList, goodsDescribe, goodsOptionList, transServiceNo);

			if (ebayGoods.isGmkt()) {
				ebayGoods.setStartGmkt(!StringUtils.hasText(ebayGoods.getGmktGoods().getItemNo()));
			}
			
			if (ebayGoods.isIac()) {
				ebayGoods.setStartIac(!StringUtils.hasText(ebayGoods.getIacGoods().getItemNo()));
			}
			
			log.info("상품수정 API 호출 {}", goodsCode);
			ProductResponse response = productApiService.updateProduct(goodsCode, ebayGoods.getEsmGoodsCode(), product, paCode, paGroupCode, transServiceNo);
			
			SiteDetail siteDetail = response.getSiteDetail();
			SiteProduct gmktResult = siteDetail.getGmkt();
			SiteProduct iacResult = siteDetail.getIac();
			
			String goodsNo = response.getGoodsNo();
			

			if (StringUtils.hasText(goodsNo)) {
				
				result.setCode("200");
				result.setMessage(PaEbayApiRequest.API_SUCCESS_CODE);
				
				ebayGoods.setEsmGoodsCode(goodsNo);
				
				if (ebayGoods.isGmkt()) {
					log.info("지마켓 상품수정결과{} - {}", goodsCode, gmktResult);
					if (ebayGoods.isStartGmkt()) { // 상품입점
						if (StringUtils.hasText(gmktResult.getSiteGoodsNo())) {
							ebayGoods.getGmktGoods().setItemNo(gmktResult.getSiteGoodsNo());
							ebayGoods.getGmktGoods().setPaStatus(PaStatus.COMPLETE.code());
						} else {
//							ebayGoods.getGmktGoods().setReturnNote("G마켓 상품코드의 값이 생성되지 않았습니다.");
							ebayGoods.getGmktGoods().setReturnNote(StringUtil.truncate(gmktResult.getSiteGoodsComment(), 500));
							ebayGoods.getGmktGoods().setPaStatus(PaStatus.REJECT.code());
						}
					} else if (StringUtils.hasText(gmktResult.getSiteGoodsNo())) {
						if (!StringUtil.compare(ebayGoods.getGmktGoods().getItemNo(), gmktResult.getSiteGoodsNo())) {
							log.info("지마켓 상품코드 변경 확인 {}: {}->{}", goodsCode, ebayGoods.getGmktGoods().getItemNo(),
									gmktResult.getSiteGoodsNo());
							ebayGoods.getGmktGoods().setItemNo(gmktResult.getSiteGoodsNo());
						}
					}
				}

				if (ebayGoods.isIac()) {
					log.info("옥션 상품수정결과{} - {}", goodsCode, iacResult);
					if (ebayGoods.isStartIac()) { // 상품입점
						if (StringUtils.hasText(iacResult.getSiteGoodsNo())) {
							ebayGoods.getIacGoods().setItemNo(iacResult.getSiteGoodsNo());
							ebayGoods.getIacGoods().setPaStatus(PaStatus.COMPLETE.code());
						} else {
//							ebayGoods.getIacGoods().setReturnNote("옥션 상품코드의 값이 생성되지 않았습니다.");
							ebayGoods.getIacGoods().setReturnNote(StringUtil.truncate(iacResult.getSiteGoodsComment(), 500));
							ebayGoods.getIacGoods().setPaStatus(PaStatus.REJECT.code());
						}
					} else if (StringUtils.hasText(iacResult.getSiteGoodsNo())) {
						if (!StringUtil.compare(ebayGoods.getIacGoods().getItemNo(), iacResult.getSiteGoodsNo())) {
							log.info("옥션 상품코드 변경 확인 {}: {}->{}", goodsCode, ebayGoods.getIacGoods().getItemNo(),
									iacResult.getSiteGoodsNo());
							ebayGoods.getIacGoods().setItemNo(iacResult.getSiteGoodsNo());
						}
					}
				}

				// 이베이 상품수정 저장
				rtnMsg = ebayResultService.saveTransProduct(ebayGoods, goodsOptionList);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + goodsCode);
				}
			} else {
				result.setCode("500");
				result.setMessage("no data found");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

			// 수기중단사유 체크후 처리
			if (ebayResultService.saveRejectTrans(ebayGoods, ex.getMessage()) == 0) {
				// 수기중단이 아닌 경우 판매기한연장 체크후 처리
				ebayResultService.extendPeriod(ebayGoods, ex.getMessage());
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

	private Product createProduct(EbayGoods ebayGoods, List<EbayGoodsOffer> goodsOfferList,
			EbayGoodsDescribe goodsDescribe, List<PaGoodsdtMapping> goodsOptionList,
			long transServiceNo) throws Exception {

		Product product = new Product();

		if (StringUtils.hasText(ebayGoods.getEsmGoodsCode())) {
			SiteValue isSell = new SiteValue();
			
			isSell.setGmkt(ebayGoods.isGmkt() && PaSaleGb.FORSALE.code().equals(ebayGoods.getGmktGoods().getPaSaleGb()));
			isSell.setIac(ebayGoods.isIac() && PaSaleGb.FORSALE.code().equals(ebayGoods.getIacGoods().getPaSaleGb()));
			
			product.setIsSell(isSell);
		}
		
		// 상품기본정보
		ItemBasicInfo itemBasicInfo = new ItemBasicInfo();
		
		// 상품명 특수문자 제거
		String keyword = sanitizeSpecialChar(ebayGoods.getGoodsName());
		if ("1".equals(ebayGoods.getCollectYn())) {
			keyword = "(착불)" + keyword;
		}

		// 상품명 설정
		itemBasicInfo.setGoodsName(new MultiLanguage(StringUtil.truncate(keyword, 100)));
		
		// 카테고리 설정
		Category category = new Category();
		List<Site> site = new ArrayList<Site>();
		
		
		if (ebayGoods.isGmkt()) site.add(new Site(2, ebayGoods.getGmktGoods().getSiteCategoryCode()));
		if (ebayGoods.isIac()) site.add(new Site(1, ebayGoods.getIacGoods().getSiteCategoryCode()));
		
		category.setSite(site);
		category.setEsm(new EsmSite(ebayGoods.getEsmCategoryCode()));
		itemBasicInfo.setCategory(category);
		
		Catalog catalog = new Catalog();
		catalog.setBrandNo(StringUtils.hasText(ebayGoods.getBrandNo()) ? Integer.valueOf(ebayGoods.getBrandNo()) : 0);
		itemBasicInfo.setCatalog(catalog);
		 
		product.setItemBasicInfo(itemBasicInfo);
		
		// 상품추가정보
		ItemAddtionalInfo itemAddtionalInfo = new ItemAddtionalInfo();
		
		// 구매수량제한
		BuyableQuantity buyableQuantity = new BuyableQuantity();
		if (ebayGoods.getOrderMaxQty() == 0) {
			// 구매수량 제한 없음
			buyableQuantity.setType(0);
		} else if (ebayGoods.getCustOrdQtyCheckTerm() == 0) {
			// ID당 최대 구매수량
			buyableQuantity.setType(2);
			buyableQuantity.setQty((int) (ebayGoods.getOrderMaxQty() > 999 ? 999 : ebayGoods.getOrderMaxQty()));
		} else {
			// 기간당 최대 구매수량
			buyableQuantity.setType(3);
			buyableQuantity.setUnitDate((int)ebayGoods.getCustOrdQtyCheckTerm());
			buyableQuantity.setQty((int) (ebayGoods.getOrderMaxQty() > 999 ? 999 : ebayGoods.getOrderMaxQty()));
		}
		itemAddtionalInfo.setBuyableQuantity(buyableQuantity);
		
		if ("1".equals(ebayGoods.getGoodsStts()) || "2".equals(ebayGoods.getGoodsStts())) {
			itemAddtionalInfo.setGoodsStatus("2");
		}
		
		// 가격
		Price price = new Price();
		if (ebayGoods.isGmkt()) price.setGmkt(ebayGoods.getGmktPrice().getSalePrice());
		if (ebayGoods.isIac()) price.setIac(ebayGoods.getIacPrice().getSalePrice());
		itemAddtionalInfo.setPrice(price);
		
		// 옵션등록
		// 신버전 옵션 추가 (itemAddtionalInfo > recommendedOpts) 2023.12.14 사용 가능으로 변경 Leejy
		Option recommendedOpts = new Option();		
		
		String goodsdtInfoKind = goodsOptionList.get(0).getGoodsdtInfoKind();		
		if (goodsdtInfoKind.matches("없음|기타")) goodsdtInfoKind = "단일상품";
			
		boolean isCombination = false; // 조합형여부
		if ("1".equals(ebayGoods.getCombinationYn())) {
			if (goodsdtInfoKind.contains("색상") && goodsdtInfoKind.contains("사이즈")) {
				isCombination = true;
			}
		}
		
		if (!goodsOptionList.stream().anyMatch(option -> option.getGoodsdtInfo().contains("/"))) {
			isCombination = false;
		}
		
		double stockRate = Double.parseDouble(gmktGoodsMapper.getStockRate(ebayGoods.getPaCode()));
		long stockQty = 0;
		
		// 추천옵션 조합형 직접입력 (SK스토아 옵션형태가 색상/사이즈 포함시, 조합형으로 연동)
		if (isCombination) {
			recommendedOpts.setType(2); // 추천옵션 타입: 조합형, independent로 입력
			recommendedOpts.setStockManage(true); // 옵션재고관리			
			
			List<CombinationDetail> details = new ArrayList<CombinationDetail>();
			
			Combination combination = new Combination();
			RecommendedOptName1 recommendedOptName1 = new RecommendedOptName1();
			RecommendedOptName2 recommendedOptName2 = new RecommendedOptName2();
			
			combination.setRecommendedOptNo1(0); // (추천옵션코드 0으로 세팅)
			combination.setRecommendedOptNo2(0); // (추천옵션코드 0으로 세팅)
			
			recommendedOptName1.setKoreanText("색상"); // 추천옵션명1 (색상)
			recommendedOptName1.setExposeLanguage(1); // 국문
			combination.setRecommendedOptName1(recommendedOptName1);
			
			recommendedOptName2.setKoreanText("사이즈"); // 추천옵션명2 (사이즈)
			recommendedOptName2.setExposeLanguage(1); // 국문
			combination.setRecommendedOptName2(recommendedOptName2);
						
			for (PaGoodsdtMapping option : goodsOptionList) {
				CombinationDetail combinationDetail = new CombinationDetail();
				combinationDetail.setRecommendedOptValueNo1(0); // 추천옵션항목코드1 (직접입력으로 0으로 세팅)
				combinationDetail.setRecommendedOptValueNo2(0); // 추천옵션항목코드2 (직접입력으로 0으로 세팅)
				
				String[] attributes = option.getGoodsdtInfo().split("/"); 
				
				RecommendMultiLanguage recommendMultiLanguage1 = new RecommendMultiLanguage(attributes[0]); // 실제 옵션명 ("색상명")
				recommendMultiLanguage1.setExposeLanguage(1); // 국문
				combinationDetail.setRecommendedOptValue1(recommendMultiLanguage1);
				
				RecommendMultiLanguage recommendMultiLanguage2 = new RecommendMultiLanguage(attributes[1]); // 실제 옵션명 ("사이즈명")
				recommendMultiLanguage2.setExposeLanguage(1); // 국문
				combinationDetail.setRecommendedOptValue2(recommendMultiLanguage2);
								
				int orderAbleQty = (int)Math.ceil(Integer.parseInt(option.getTransOrderAbleQty()) * stockRate);
				option.setTransOrderAbleQty(String.valueOf(orderAbleQty));
				if (orderAbleQty > 4999) orderAbleQty = 4999;
				stockQty += orderAbleQty;
				
				combinationDetail.setSoldOut(orderAbleQty == 0); // 품절여부
				combinationDetail.setDisplay(true); // 옵션 전시여부
				combinationDetail.setQty(new Qty(orderAbleQty)); // 재고
				combinationDetail.setManageCode(ebayGoods.getGoodsCode() + option.getGoodsdtCode()); // 판매자단품코드
				
				details.add(combinationDetail);
			}
			combination.setCombinationDetail(details);
			recommendedOpts.setRecommendCombination(combination);
			
		// 추천옵션 선택형 직접입력
		}else {
			recommendedOpts.setType(1); // 추천옵션 타입: 선택형, independent로 입력
			recommendedOpts.setStockManage(true); // 옵션재고관리 
			
			List<IndependentDetail> details = new ArrayList<IndependentDetail>();		
			
			Independent independent = new Independent();		
			RecommendedOptName recommendedOptName = new RecommendedOptName();
			independent.setRecommendedOptNo(0); // (추천옵션코드 0으로 세팅)
			
			recommendedOptName.setKoreanText(goodsdtInfoKind); // 추천옵션명(단일상품)
			recommendedOptName.setExposeLanguage(1); // 국문
			independent.setRecommendedOptName(recommendedOptName);	
			
			for (PaGoodsdtMapping option : goodsOptionList) {
				IndependentDetail detail = new IndependentDetail();
				
				String goodsdtInfo = option.getGoodsdtInfo();
				if (goodsdtInfo.equals("없음")) goodsdtInfo = "단일상품";
				
				RecommendMultiLanguage recommendMultiLanguage = new RecommendMultiLanguage(goodsdtInfo); // 실제 옵션명 (상품상세)
				recommendMultiLanguage.setExposeLanguage(1); // 국문
				detail.setRecommendedOptValueNo(0); // 추천옵션항목코드 (직접입력으로 0으로 세팅) 
				detail.setRecommendedOptValue(recommendMultiLanguage);
				
				int orderAbleQty = (int)Math.ceil(Integer.parseInt(option.getTransOrderAbleQty()) * stockRate);
				option.setTransOrderAbleQty(String.valueOf(orderAbleQty)); 
				if (orderAbleQty > 4999) orderAbleQty = 4999;
				stockQty += orderAbleQty;
				
				detail.setSoldOut(orderAbleQty == 0); // 품절여부
				detail.setDisplay(true); // 옵션 전시여부
				detail.setQty(new Qty(orderAbleQty)); // 재고
				detail.setManageCode(ebayGoods.getGoodsCode() + option.getGoodsdtCode()); // 판매자단품코드
				
				details.add(detail);
			}
			independent.setDetails(details);
			recommendedOpts.setRecommendIndependent(independent);
		}		
		
		itemAddtionalInfo.setRecommendedOpts(recommendedOpts); //(itemAddtionalInfo > recommendedOpts)
		
		// 구버전 옵션 API 사용중단으로 (itemAddtionalInfo > orderOpts) 주문옵션 사용안함 처리  2023.12.14 Leejy
		Option orderOpts = new Option();
		orderOpts.setType(0); // 사용안함
		itemAddtionalInfo.setOrderOpts(orderOpts);
		
//		구번전 옵션 API 중단으로 모두 주석처리
//		List<IndependentDetail> details = new ArrayList<IndependentDetail>();
//		
//		// 주문옵션
//		Option orderOpts = new Option();
//		String goodsdtInfoKind = goodsOptionList.get(0).getGoodsdtInfoKind();
//		
//		if (goodsdtInfoKind.matches("없음|기타")) goodsdtInfoKind = "단일상품";
//		
//		boolean isCombination = false; // 조합형여부
//		if ("1".equals(ebayGoods.getCombinationYn())) {
//			if (goodsdtInfoKind.contains("색상") && goodsdtInfoKind.contains("사이즈")) {
//				isCombination = true;
//			}
//		}
//		
//		if (!goodsOptionList.stream().anyMatch(option -> option.getGoodsdtInfo().contains("/"))) {
//			isCombination = false;
//		}
//
//		double stockRate = Double.parseDouble(gmktGoodsMapper.getStockRate(ebayGoods.getPaCode()));
//		long stockQty = 0;
//		
//		if (isCombination) {
//			orderOpts.setType(2); // 2개 조합형 
//			
//			orderOpts.setStockManage(true);
//			
//			Combination combination = new Combination();
//			
//			combination.setName1(new MultiLanguage("색상"));
//			combination.setName2(new MultiLanguage("사이즈"));
//
//			List<IndependentDetail> details = new ArrayList<IndependentDetail>();
//			
//			for (PaGoodsdtMapping option : goodsOptionList) {
//				IndependentDetail detail = new IndependentDetail();
//				String[] attributes = option.getGoodsdtInfo().split("/"); 
//				detail.setValue1(new MultiLanguage(attributes[0]));
//				detail.setValue2(new MultiLanguage(attributes[1]));
//				int orderAbleQty = (int)Math.ceil(Integer.parseInt(option.getTransOrderAbleQty()) * stockRate);
//				option.setTransOrderAbleQty(String.valueOf(orderAbleQty));
//				if (orderAbleQty > 4999) orderAbleQty = 4999;
//				stockQty += orderAbleQty;
//				detail.setSoldOut(orderAbleQty == 0); // 품절여부
//				detail.setDisplay(true); // 옵션 전시여부
//				detail.setQty(new Qty(orderAbleQty));
//				detail.setManageCode(ebayGoods.getGoodsCode() + option.getGoodsdtCode()); //판매자단품코드
//				details.add(detail);
//			}
//			combination.setDetails(details);
//			orderOpts.setCombination(combination);
//			
//		} else {
//
//			orderOpts.setType(1); // 선택형
//			
//			orderOpts.setStockManage(true);
//			
//
//			List<Independent> independentList = new ArrayList<Independent>();
//			Independent independent = new Independent();
//			independent.setName(new MultiLanguage(goodsdtInfoKind));
//
//			List<IndependentDetail> details = new ArrayList<IndependentDetail>();
//			
//			for (PaGoodsdtMapping option : goodsOptionList) {
//				IndependentDetail detail = new IndependentDetail();
//				String goodsdtInfo = option.getGoodsdtInfo();
//				if (goodsdtInfo.equals("없음")) goodsdtInfo = "단일상품";
//				detail.setValue(new MultiLanguage(goodsdtInfo));
//				int orderAbleQty = (int)Math.ceil(Integer.parseInt(option.getTransOrderAbleQty()) * stockRate);
//				option.setTransOrderAbleQty(String.valueOf(orderAbleQty));
//				if (orderAbleQty > 4999) orderAbleQty = 4999;
//				stockQty += orderAbleQty;
//				detail.setSoldOut(orderAbleQty == 0); // 품절여부
//				detail.setDisplay(true); // 옵션 전시여부
//				detail.setQty(new Qty(orderAbleQty));
//				detail.setManageCode(ebayGoods.getGoodsCode() + option.getGoodsdtCode()); //판매자단품코드
//				details.add(detail);
//			}
//			independent.setDetails(details);
//			independentList.add(independent);
//			orderOpts.setIndependent(independentList);
//		}
//		
//		itemAddtionalInfo.setOrderOpts(orderOpts);

		// 재고
		SiteIntValue stock = new SiteIntValue();
		ebayGoods.setTransOrderAbleQty(stockQty);
		int qty = (int)(ebayGoods.getTransOrderAbleQty() > 4999 ? 4999 : ebayGoods.getTransOrderAbleQty());
		if (ebayGoods.isGmkt()) stock.setGmkt(qty);
		if (ebayGoods.isIac()) stock.setIac(qty);
		itemAddtionalInfo.setStock(stock);
		
		// 판매기한
		SiteIntValue sellingPeriod = new SiteIntValue();
		if (ebayGoods.isGmkt()) sellingPeriod.setGmkt(90);
		if (ebayGoods.isIac()) sellingPeriod.setIac(90);
		itemAddtionalInfo.setSellingPeriod(sellingPeriod);
		
		// 판매자 상품코드
		itemAddtionalInfo.setManagedCode(ebayGoods.getGoodsCode());
		
		// 판매자 카테고리/브랜드
		SellerShop sellerShop = new SellerShop();
		sellerShop.setCatCode(ebayGoods.getLmsdCode());
		sellerShop.setCatName(ebayGoods.getLmsdName());
		sellerShop.setBrandCode(ebayGoods.getBrandCode());
		sellerShop.setBrandName(ebayGoods.getBrandName());
		itemAddtionalInfo.setSellerShop(sellerShop);
		
		// 원산지
		Origin origin = new Origin();
		origin.setGoodsType(1); // 상세설명참조
		origin.setType(0); // 원산지지역 없음
		itemAddtionalInfo.setOrigin(origin);
		
		// 배송비
		Shipping shipping = new Shipping();
		
		// 출하지
		Policy policy = new Policy();
		policy.setPlaceNo(ebayGoods.getGmktShipNo()); // 출하지번호
		policy.setFeeType(1); // 묶음배송비
		
		// 묶음배송비정책
		Bundle bundle = new Bundle();
		bundle.setDeliveryTmplId(ebayGoods.getBundleNo());
		policy.setBundle(bundle);
		
		shipping.setPolicy(policy);
		
		// 회수지정보
		ReturnAndExchange returnAndExchange = new ReturnAndExchange();
		returnAndExchange.setAddrNo(ebayGoods.getReturnManSeq());
		
		// 반품/교환비
		// 레거시는 반품비를 왕복배송비로 판단하여 주문배송비를 차감해서 연동하고 있음. 
		// 현재 스토아 기준으로 반품비에 대해서 왕복/편도를 업체가 임의로 등록하는 것으로 보임
		// 단 무료배송은 반품비 왕복으로 등록한다고 함.
		// 이베이는 이전 기준 그대로 초도반품비가 반품비에 포함된것으로 해석하여 연동
		// 교환비는 왕복으로 등록하니 교환비 검증 후 되도록 교환비로 처리하고 안되면 반품비로 처리 
		// 일단, 무료배송인 경우에 이베이에서는 스토아와 달리 초도반품비를 부과하므로 반품비/2로 연동(확정)
		// 무료가 아닌 경우에는 주문배송비와 비교하여 반품비가 크면 왕복으로 보고 주문배송비 차감하여 연동

		// 레거시 동기화 데이터 오류로인해 교환비 비교 불가
//		double returnCost = ebayGoods.getChangeCost() > ebayGoods.getReturnCost() ? ebayGoods.getChangeCost() :ebayGoods.getReturnCost();

		double returnCost = ebayGoods.getReturnCost();
		
		if (ebayGoods.getShipCostCode().contains(ShipCostFlag.FREE.code())) {
			returnAndExchange.setFee(returnCost > 0 ? returnCost/2 : 0);
		} else {
			if (returnCost > ebayGoods.getOrdCost()) {
				// 등록된 반품비가 주문배송비보다 클 경우 반품비에서 주문배송비 차감하여 반품편도비 설정
				returnAndExchange.setFee(returnCost - ebayGoods.getOrdCost());
			} else {
				// 주문배송비 이하이면 반품비가 편도로 입력되었다고 보고 그대로 설정
				returnAndExchange.setFee(returnCost);
			}
		}
		shipping.setReturnAndExchange(returnAndExchange);
		
		// 발송정책
		SiteNoValue dispatchPolicyNo = new SiteNoValue();
		if (ebayGoods.isGmkt())	dispatchPolicyNo.setGmkt(getDispatchPolicy(ebayGoods.getGmktGoods(), ebayGoods.getInstallYn()));
		if (ebayGoods.isIac()) dispatchPolicyNo.setIac(getDispatchPolicy(ebayGoods.getIacGoods(), ebayGoods.getInstallYn()));
		shipping.setDispatchPolicyNo(dispatchPolicyNo);
		
		// 배송방법
		if("1".equals(ebayGoods.getCollectYn())) {
			shipping.setType(2); //직접발송
		}else {
			shipping.setType(1); //택배소포
			// 회수지 택배 설정 및 택배사코드 
			shipping.setCompanyNo(getShippingCompanyNo(ebayGoods.getDelyGb(), returnAndExchange));
		}
		
		// 제주/도서산간배송불가여부
		shipping.setBackwoodsDeliveryYn("1".equals(ebayGoods.getDoNotIslandDelyYn()) ? "N" : "Y");
		
		itemAddtionalInfo.setShipping(shipping);
		
		// 상품정보고시
		OfficialNotice officialNotice = new OfficialNotice();
		List<OfficialNoticeDetail> details = new ArrayList<OfficialNoticeDetail>();
		for (EbayGoodsOffer ebayGoodsOffer : goodsOfferList) {
			OfficialNoticeDetail detail = new OfficialNoticeDetail();
			detail.setOfficialNoticeItemelementCode(ebayGoodsOffer.getPaOfferCode());
			detail.setValue(ebayGoodsOffer.getPaOfferExt());
			detail.setExtraMark(false);
			details.add(detail);
		}
		officialNotice.setOfficialNoticeNo(goodsOfferList.get(0).getPaOfferType());
		officialNotice.setDetails(details);
		itemAddtionalInfo.setOfficialNotice(officialNotice);
		
		// 성인상품여부
		itemAddtionalInfo.setAdultProduct("1".equals(ebayGoods.getAdultYn()));
		
		// 청소년구매불가여부
		itemAddtionalInfo.setYouthNotAvailable(false);
		
		// 면세여부
		// 레거시에 무조건 과세로 설정되어 있음
		itemAddtionalInfo.setVatFree("0".equals(ebayGoods.getTaxYn()));
		
		// 인증정보
		CertInfo certInfo = new CertInfo();
		
		if (ebayGoods.isIac()) {
			CertInfoIac iac = new CertInfoIac();
			CertInfoDetail certDetail = new CertInfoDetail(false);
			iac.setMedicalInstrument(certDetail);
			iac.setBroadcastEquipment(certDetail);
			iac.setFood(certDetail);
			iac.setHealthFood(certDetail);
			iac.setEnvironmentFriendly(certDetail);
			certInfo.setIac(iac);
		}
		
		SafetyCerts safetyCerts = new SafetyCerts();
		SafetyCert safetyCert = new SafetyCert();
		safetyCert.setType(1);
		safetyCerts.setChild(safetyCert);
		safetyCerts.setElectric(safetyCert);
		safetyCerts.setLife(safetyCert);
		certInfo.setSafetyCerts(safetyCerts);

		itemAddtionalInfo.setCertInfo(certInfo);
		
		// 상품이미지
		Images images = new Images();

		Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
		String imageServer = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
		
		String imagePath = imageServer + ebayGoods.getImageUrl();
		String imageResizePath = "/dims/resize/600X600";
		
		// 기본이미지
		if(ebayGoods.getImageP().contains("info")) { // 딜상품 대표이미지 존재
			images.setBasicImgURL(imagePath + ebayGoods.getImageP());
		} else {
			images.setBasicImgURL(imagePath + ebayGoods.getImageG() + imageResizePath);
		}
		// 추가이미지
		if (StringUtils.hasText(ebayGoods.getImageAp())) {
			images.setAddtionalImg1URL(imagePath + ebayGoods.getImageAp() + imageResizePath);
		}
		if (StringUtils.hasText(ebayGoods.getImageBp())) {
			images.setAddtionalImg2URL(imagePath + ebayGoods.getImageBp() + imageResizePath);
		}
		// 레거시는 추가이미지 2개까지만 연동함. 
		if (StringUtils.hasText(ebayGoods.getImageCp())) {
			images.setAddtionalImg3URL(imagePath + ebayGoods.getImageCp() + imageResizePath);
		}
		
		itemAddtionalInfo.setImages(images);
		
		// 상품설명
		Descriptions descriptions = new Descriptions();
		Description kor = new Description();
		
		kor.setType(2); // html
		kor.setHtml(createDescription(goodsDescribe));
		descriptions.setKor(kor);
		itemAddtionalInfo.setDescriptions(descriptions);
		
		product.setItemAddtionalInfo(itemAddtionalInfo);
		
		// 가격추가 정보
		AddtionalInfo addtionalInfo = new AddtionalInfo();
		SellerDiscount sellerDiscount = new SellerDiscount();
		String startDate = DateUtil.toDateString("yyyy-MM-dd", systemService.getDate());

		sellerDiscount.setUse(false); // 판매자할인 사용여부
		
		// 지마켓 할인정보 
		if (ebayGoods.isGmkt()) {
			SellerDiscountSite gmkt = new SellerDiscountSite();
			PaGoodsPriceApply gmktPrice = ebayGoods.getGmktPrice();
			double dcAmt = gmktPrice.getSalePrice() - gmktPrice.getBestPrice(); 
			if (dcAmt > 0) {
				// 할인 적용
				gmkt.setType(1); // 정액
				gmkt.setPriceOrRate1(dcAmt);
				gmkt.setStartDate(startDate);
				gmkt.setEndDate("2999-12-31");
				
				sellerDiscount.setGmkt(gmkt);
				sellerDiscount.setUse(true);
			}
		}
		
		// 옥션 할인정보 
		if (ebayGoods.isIac()) {
			SellerDiscountSite iac = new SellerDiscountSite();
			PaGoodsPriceApply iacPrice = ebayGoods.getIacPrice();
			double dcAmt = iacPrice.getSalePrice() - iacPrice.getBestPrice(); 
			if (dcAmt > 0) {
				// 할인 적용
				iac.setType(1); // 정액
				iac.setPriceOrRate1(dcAmt);
				iac.setStartDate(startDate);
				iac.setEndDate("2999-12-31");
				
				sellerDiscount.setIac(iac);
				sellerDiscount.setUse(true);
			}
		}
		
		addtionalInfo.setSellerDiscount(sellerDiscount);
		
		// 사이트부담지원 할인
		SiteValue siteDiscount = new SiteValue();
		siteDiscount.setGmkt(true);
		siteDiscount.setIac(true);
		addtionalInfo.setSiteDiscount(siteDiscount);
		
		// 가격비교사이트 노출여부
		Pcs pcs = new Pcs();
		pcs.setUse(true);
		pcs.setUseIacPcsCoupon(true);
		pcs.setUseGmkPcsCoupon(true);
		addtionalInfo.setPcs(pcs);
		
		// 해외판매대행
		OverseaSales overseaSales = new OverseaSales();
		overseaSales.setAgree(false); // 해외판매 진행하지 않음
		addtionalInfo.setOverseaSales(overseaSales);
		
		product.setAddtionalInfo(addtionalInfo);
		
		return product;
	}


	/**
	 * 이베이 발송정책
	 * 
	 * @param gmktGoods
	 * @param installYn
	 * @return
	 */
	private int getDispatchPolicy(PaGmktGoods gmktGoods, String installYn) {
		
		// 예외업체 발송정책
		PaGmktPolicy gmktPolicy = gmktGoodsMapper.selectDispatchPolicyExceptEntp(gmktGoods.getEntpCode(),
				gmktGoods.getPaCode(), gmktGoods.getPaGroupCode());
		if (gmktPolicy != null)	return Integer.parseInt(gmktPolicy.getPolicyNo());
		
		// 예외중분류 발송정책
		gmktPolicy = gmktGoodsMapper.selectDispatchPolicyExceptMgroup(
				gmktGoods.getLmsdCode().substring(0, 4), gmktGoods.getPaCode(), gmktGoods.getPaGroupCode());
		if (gmktPolicy != null)	return Integer.parseInt(gmktPolicy.getPolicyNo());

		gmktPolicy = new PaGmktPolicy();
		
		gmktPolicy.setPaCode(gmktGoods.getPaCode());
		gmktPolicy.setPaGroupCode(gmktGoods.getPaGroupCode());
		
		// 순차배송
		gmktPolicy.setPolicyType("1".equals(gmktGoods.getOrderCreateYn()) ? PolicyType.CUSTOM.code() : PolicyType.INORDER.code());
		
		// 설치상품, 주문제작상품
		if ("1".equals(installYn) || "1".equals(gmktGoods.getOrderCreateYn())) {
			gmktPolicy.setDuration("10");
		} else {
			gmktPolicy.setDuration("3");
		}
		
		gmktPolicy = gmktGoodsMapper.selectDispatchPolicy(gmktPolicy);

		if (gmktPolicy != null)	return Integer.parseInt(gmktPolicy.getPolicyNo());
		
		return 0;
	}

	/**
	 *  특수문자 제거
	 * 
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	private String sanitizeSpecialChar(String keyword) throws Exception {
		if (!StringUtils.hasLength(keyword)) return keyword;

		List<PaGoodsLimitChar> limitCharList = limitCharMapper.getLimitCharList(PaGroup.GMARKET.code());

		for (PaGoodsLimitChar limitChar : limitCharList) {
			keyword = keyword.replaceAll(limitChar.getLimitChar(), Objects.toString(limitChar.getReplaceChar(), ""));
		}
		keyword = keyword.trim();
		keyword = keyword.replaceAll("\\s+", " ").replaceAll("\\p{C}", "");
		return keyword;
	}
	
	/**
	 * 회수지 택배사 설정 및 택배사코드 반환
	 * 
	 * @param delyGb
	 * @param returnAndExchange
	 * @return
	 */
	private int getShippingCompanyNo(String delyGb, ReturnAndExchange returnAndExchange) {
		
		int companyNo; // 택배사코드
		
		// 기본 CJ대한통운
		if (!StringUtils.hasText(delyGb)) delyGb = "10"; 
		
		switch(delyGb){
			case "10"://CJ대한통운
				returnAndExchange.setShippingCompany("0008");
				companyNo = 10001;
				break;
			case "11"://한진택배
				returnAndExchange.setShippingCompany("0002");
				companyNo = 10007;
				break;
			case "12"://롯데택배(구,현대택배)
				returnAndExchange.setShippingCompany("0004");
				companyNo = 10008;
				break;
			case "13"://우체국택배
				returnAndExchange.setShippingCompany("0001");
				companyNo = 10005;
				break;
			case "14"://로젠택배
				returnAndExchange.setShippingCompany("0004");
				companyNo = 10003;
				break;
			case "15"://드림택배(구,KG로지스)
				returnAndExchange.setShippingCompany("0007");
				companyNo = 10004;
				break;
			case "17"://KGB택배
				returnAndExchange.setShippingCompany("0008");
				companyNo = 10010;
				break;
			case "27"://대신택배
				returnAndExchange.setShippingCompany("0015");
				companyNo = 10014;
				break;
			case "28"://경동택배
				returnAndExchange.setShippingCompany("0016");
				companyNo = 10016;
				break;
			case "29"://합동택배
				returnAndExchange.setShippingCompany("0017");
				companyNo = 10074;
				break;
			case "30"://CVS편의점택배
				returnAndExchange.setShippingCompany("0024");
				companyNo = 10073;
				break;
			case "32"://한의사랑택배
				returnAndExchange.setShippingCompany("0008");
				companyNo = 10081;
				break;
			case "33"://GTX로지스
				returnAndExchange.setShippingCompany("0010");
				companyNo = 10019;
				break;
			case "34"://천일택배
				returnAndExchange.setShippingCompany("0014");
				companyNo = 10017;
				break;
			case "35"://건영택배
				returnAndExchange.setShippingCompany("0008");
				companyNo = 10050;
				break;
			case "16"://KG옐로우캡택배 보류
			case "18"://일양로지스 보류
			case "36"://고려택배 보류
			case "40"://자체처리 보류
			case "47"://자체배송 보류
			case "48"://설치상품 보류
			case "99"://기타 보류
			default:
				returnAndExchange.setShippingCompany("0008");
				companyNo = 10001;
		}
		return companyNo;
	}

	/**
	 * 상품설명
	 * 
	 * @param goodsDescribe
	 */
	private String createDescription(EbayGoodsDescribe goodsDescribe) {
		
		// 상품구성템플릿
        String goodsCom = StringUtils.hasText(goodsDescribe.getGoodsCom()) ? goodsDescribe.getGoodsCom() : ""; 
        
	    //이베이 상품구성 텍스트 잘림 방지			
		if(!"".equals(goodsCom) && !goodsCom.contains("<img") && !goodsCom.contains("<IMG") && !goodsCom.contains("<iframe")){ 
			List<String> goodsComList = new ArrayList<>(Arrays.asList(goodsCom.split("\n"))); 
			for(int i=0; i<goodsComList.size(); i++) {
				String str = goodsComList.get(i);
				if(str.length() > 40) { //문자열 길이 체크
					List<String> longStrList = new ArrayList<>(Arrays.asList(str.split("")));
					for(int j =1; j<longStrList.size(); j++) {
				    	if(j%40 == 0) longStrList.add(j, "\n"); //40자마다 줄바꿈
				    }
					str = String.join("", longStrList);
					goodsComList.set(i, str);
				}
			}
		    goodsCom = String.join("\n", goodsComList);
		}
		
        goodsCom = (!"".equals(goodsCom))
        	? ("<div style='line-height: 2.0em; font-family: NanumBarunGothic; font-size: 19px;'><div><h4>&middot;&nbsp;상품구성<h4><pre>"
        		+ goodsCom + "</pre></div></div>") 
        	: "";
		
		// 웹기술서
		String description = 
				"<div align='center'><img alt='' src='" + goodsDescribe.getTopImage() + "' /><br /><br /><br />" // 상단이미지 
				+ (StringUtils.hasText(goodsDescribe.getCollectImage()) ?
						"<img alt='' src='" + goodsDescribe.getCollectImage() + "' /><br /><br /><br />" : "") // 착불이미지
				+ goodsCom // 상품구성
				+ goodsDescribe.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />" // 기술서
				+ "<img alt='' src='" + goodsDescribe.getBottomImage() + "' /></div>"; //하단이미지

		// 제휴 공지사항
		if(StringUtils.hasText(goodsDescribe.getNoticeExt())) {
			description = goodsDescribe.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + description;
		}
		
		return description;
	}
}
