package com.cware.netshopping.panaver.v2.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.panaver.product.type.BagSummaryType;
import com.cware.api.panaver.product.type.BooksSummaryType;
import com.cware.api.panaver.product.type.CarArticlesSummaryType;
import com.cware.api.panaver.product.type.CertificationListType;
import com.cware.api.panaver.product.type.CertificationType;
import com.cware.api.panaver.product.type.CombinationOptionItemListType;
import com.cware.api.panaver.product.type.CombinationOptionItemType;
import com.cware.api.panaver.product.type.CombinationOptionNamesType;
import com.cware.api.panaver.product.type.CombinationOptionType;
import com.cware.api.panaver.product.type.CosmeticSummaryType;
import com.cware.api.panaver.product.type.DeliveryType;
import com.cware.api.panaver.product.type.DietFoodSummaryType;
import com.cware.api.panaver.product.type.DigitalContentsSummaryType;
import com.cware.api.panaver.product.type.EtcServiceSummaryType;
import com.cware.api.panaver.product.type.EtcSummaryType;
import com.cware.api.panaver.product.type.FashionItemsSummaryType;
import com.cware.api.panaver.product.type.FoodSummaryType;
import com.cware.api.panaver.product.type.FurnitureSummaryType;
import com.cware.api.panaver.product.type.GeneralFoodSummaryType;
import com.cware.api.panaver.product.type.HomeAppliancesSummaryType;
import com.cware.api.panaver.product.type.ImageAppliancesSummaryType;
import com.cware.api.panaver.product.type.ImageType;
import com.cware.api.panaver.product.type.JewellerySummaryType;
import com.cware.api.panaver.product.type.KidsSummaryType;
import com.cware.api.panaver.product.type.KitchenUtensilsSummaryType;
import com.cware.api.panaver.product.type.MedicalAppliancesSummaryType;
import com.cware.api.panaver.product.type.MicroElectronicsSummaryType;
import com.cware.api.panaver.product.type.ModelType;
import com.cware.api.panaver.product.type.MusicalInstrumentSummaryType;
import com.cware.api.panaver.product.type.NavigationSummaryType;
import com.cware.api.panaver.product.type.OfficeAppliancesSummaryType;
import com.cware.api.panaver.product.type.OpticsAppliancesSummaryType;
import com.cware.api.panaver.product.type.OptionType;
import com.cware.api.panaver.product.type.OptionalListType;
import com.cware.api.panaver.product.type.OriginAreaType;
import com.cware.api.panaver.product.type.PaCertificationVO;
import com.cware.api.panaver.product.type.PaDeliveryVO;
import com.cware.api.panaver.product.type.PaNaverGoodsVO;
import com.cware.api.panaver.product.type.ProductSummaryType;
import com.cware.api.panaver.product.type.ProductType;
import com.cware.api.panaver.product.type.RentalEtcSummaryType;
import com.cware.api.panaver.product.type.SeasonAppliancesSummaryType;
import com.cware.api.panaver.product.type.SellerDiscountType;
import com.cware.api.panaver.product.type.ShoesSummaryType;
import com.cware.api.panaver.product.type.SleepingGearSummaryType;
import com.cware.api.panaver.product.type.SportsEquipmentSummaryType;
import com.cware.api.panaver.product.type.URLType;
import com.cware.api.panaver.product.type.WearSummaryType;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.PaSaleGb;
import com.cware.netshopping.common.code.PaStatus;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.PaNaverGoodsImageVO;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaNaverGoodsImage;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsdtMappingMapper;
import com.cware.netshopping.pacommon.v2.service.SyncProductService;
import com.cware.netshopping.panaver.goods.repository.PaNaverGoodsDAO;
import com.cware.netshopping.panaver.goods.service.PaNaverGoodsService;
import com.cware.netshopping.panaver.v2.repository.PaNaverGoodsMapper;

@Service
public class PaNaverProductService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;

	@Autowired
	@Qualifier("panaver.goods.paNaverGoodsService")
	private PaNaverGoodsService paNaverGoodsService;

	@Autowired
	@Qualifier("panaver.goods.paNaverGoodsDAO")
	private PaNaverGoodsDAO paNaverGoodsDAO;

	@Autowired
	PaNaverProductApiService productApiService;

	@Autowired
	NaverProductService naverProductService;

	@Autowired
	SyncProductService syncProductService;

	@Autowired
	PaNaverGoodsMapper paNaverGoodsMapper;

	@Autowired
	PaNaverResultService paNaverResultService;

	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;

	@Autowired
	PaGoodsdtMappingMapper goodsdtMappingMapper;

	@Autowired
	TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 네이버 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public ResponseMsg transProduct(String goodsCode, String paCode, String procId) {

		ResponseMsg result = new ResponseMsg("", "");

		// 상품동기화
		PaGoodsSync sync = syncProductService.syncProduct(goodsCode, PaGroup.NAVER.code(), procId);
		result = sync.getResponseMsg();
		if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) {
			return result;
		}

		// 스토아상품조회
		PaNaverGoodsVO paNaverGoods = paNaverGoodsMapper.getGoods(goodsCode, paCode);

		if (paNaverGoods == null) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("연동 대상 상품이 아닙니다.");
			return result;
		}

		// 입점요청중인건은 처리하지 않음
		if (PaStatus.PROCEEDING.code().equals(paNaverGoods.getPaStatus())) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("입점요청중인 상품입니다.");
			return result;
		}

		// 입점요청/입점반려건에 대해서 신규입점 요청
		if (PaStatus.REQUEST.code().equals(paNaverGoods.getPaStatus())
				|| PaStatus.REJECT.code().equals(paNaverGoods.getPaStatus())) {

			// 동기화에서 필터링된 경우
			if (String.valueOf(HttpStatus.NO_CONTENT.value()).equals(result.getCode())) {
				return result;
			}

			if (goodsTargetMapper.existsGoodsTarget(goodsCode, paCode, PaGroup.NAVER.code()) < 1) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
				result.setMessage("입점 대상 상품이 아닙니다(타겟데이터 없음)");
				return result;
			}

			// 신규입점
			result = registerProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				// 옵션등록
				manageOption(goodsCode, paCode, procId, 0);
				result.setMessage("입점완료되었습니다.");
			}
			return result;
		}

		PaTransService transService;
		
		if ("1".equals(paNaverGoods.getTransSaleYn()) && PaSaleGb.SUSPEND.code().equals(paNaverGoods.getPaSaleGb())) {
			// 판매중지
			transService = naverProductService.stopSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			if ("1".equals(transService.getSuccessYn())) {
				result.setMessage("판매중지되었습니다.");
			} else {
				result.setMessage(transService.getResultMsg());
			}
			return result;
		}

		boolean isUpdated = false;
		boolean isOption = false;

		if ("1".equals(paNaverGoods.getTransTargetYn())) {
			// 상품수정
			ResponseMsg updated = updateProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(updated.getCode())) {
				isUpdated = true;
				// 옵션등록/수정
				manageOption(goodsCode, paCode, procId, 0);
				result.setMessage("수정완료되었습니다.");
			} else
				return updated;
		} else {
			// 옵션등록/수정
			ResponseMsg option = manageOption(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(option.getCode())) {
				isOption = true;
				result.setMessage("옵션이 등록/수정되었습니다.");
			}
		}

		if (isUpdated || isOption) { // 상품이 변경된 경우
			result.setCode(String.valueOf(HttpStatus.OK.value()));
		} else {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
			result.setMessage("변경된 사항이 없습니다.");
		}

		return result;
	}

	/**
	 * 네이버 신규상품 입점
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
		serviceLog.setServiceNote("[API]네이버-상품입점");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 상품입점
		ResponseMsg result = callRegisterProduct(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		// 입점요청중 해제
		paNaverGoodsMapper.updateClearProceeding(goodsCode, paCode, procId);

		return result;
	}

	/**
	 * 네이버 상품 수정
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
		serviceLog.setServiceNote("[API]네이버-상품수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
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
	 * 옵션등록/수정
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg manageOption(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스 로그 생성전 전송대상여부 체크
		if (goodsdtMappingMapper.existsTransTarget(goodsCode, paCode) < 1) {
			return new ResponseMsg("404", "처리할 내역이 없습니다.");
		}
		
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]네이버-옵션등록/수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 옵션등록/수정
		ResponseMsg result = callManageOption(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}

	/**
	 * 이미지등록
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerImage(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]네이버-이미지등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 이미지등록
		ResponseMsg result = callRegisterImage(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	/**
	 * 네이버 상품 조회
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ProductType getProduct(String goodsCode, String productId, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]네이버-상품조회");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		ProductType product = null;

		try {
			// 상품조회
			product = productApiService.getProduct(goodsCode, productId, serviceLog.getTransServiceNo());
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(PaNaverApiRequest.API_SUCCESS_CODE);

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

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 상품등록서비스 Start - {} =====", goodsCode);

			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
			paramMap.put("modCase", "INSERT");
			PaNaverGoodsVO paNaverGoods = paNaverGoodsDAO.selectPaNaverGoodsInfo(paramMap);

			if (paNaverGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paNaverGoods.getProductId() != null) {
				result.setCode("411");
				result.setMessage("이미 등록된 상품입니다. 네이버 상품코드 : " + paNaverGoods.getProductId());
				return result;
			}
			if (paNaverGoods.getDescribeExt() == null) {
				paNaverGoods.setDescribeExt("");
				if (paNaverGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}
			
			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.NAVER.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			paramMap.put("groupCode", paNaverGoods.getPaGroupCode());

			PaNaverGoodsImageVO paNaverGoodsImage = paNaverGoodsService.selectPaNaverGoodsImage(paramMap); 
			if (paNaverGoodsImage == null) {
				result = callRegisterImage(goodsCode, paCode, procId, transServiceNo);
				if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
					return result;
				}
				paNaverGoodsImage = paNaverGoodsService.selectPaNaverGoodsImage(paramMap);
			}
			
			/* 2023-02-01 인증정보 */
			List<PaCertificationVO> paCertificationList = paNaverGoodsService.selectPaCertificationList(paramMap);
			
			// 입점요청중
			paNaverGoodsMapper.updateProceeding(goodsCode, paCode, procId);

			// 상품설명설정
			settingDescribeExt(paNaverGoods);

			paNaverGoods.setModifyId(procId);

			// 네이버 상품 전문
			ProductType product = createProduct(paNaverGoods, paNaverGoodsImage, goodsPrice, transServiceNo, paCertificationList);

			log.info("상품등록 API 호출 {}", goodsCode);
			String productId = productApiService.registerProduct(goodsCode, product, transServiceNo);

			if (StringUtils.hasText(productId)) {

				result.setCode("200");
				result.setMessage(PaNaverApiRequest.API_SUCCESS_CODE);

				paNaverGoods.setProductId(productId);
				paNaverResultService.saveTransProduct(paNaverGoods, goodsPrice);
			} else {
				result.setCode("500");
				result.setMessage("상품 등록에 실패했습니다.");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

            paNaverGoodsMapper.rejectTransTarget(goodsCode, paCode, procId,
                    StringUtil.truncate(result.getMessage(), 500));

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

		ResponseMsg result = new ResponseMsg("", "");

		PaNaverGoodsVO paNaverGoods = null;

		try {
			log.info("===== 상품수정서비스 Start - {} =====", goodsCode);

			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
			paramMap.put("modCase", "MODIFY");
			paNaverGoods = paNaverGoodsDAO.selectPaNaverGoodsInfo(paramMap);

			if (paNaverGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paNaverGoods.getProductId() == null) {
				result.setCode("411");
				result.setMessage("등록되지 않은 상품입니다. 상품코드 : " + goodsCode);
				return result;
			}
			if (paNaverGoods.getDescribeExt() == null) {
				paNaverGoods.setDescribeExt("");
				if (paNaverGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.NAVER.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 상품설명설정
			settingDescribeExt(paNaverGoods);

			paramMap.put("groupCode", paNaverGoods.getPaGroupCode());
			PaNaverGoodsImageVO paNaverGoodsImage = paNaverGoodsService.selectPaNaverGoodsImage(paramMap); 
			if (paNaverGoodsImage == null) {
				result = callRegisterImage(goodsCode, paCode, procId, transServiceNo);
				if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
					return result;
				}
				paNaverGoodsImage = paNaverGoodsService.selectPaNaverGoodsImage(paramMap);
			}

			paNaverGoods.setModifyId(procId);
			
			/* 2023-02-01 인증정보 */
			List<PaCertificationVO> paCertificationList = paNaverGoodsService.selectPaCertificationList(paramMap);

			// 네이버 상품 전문
			ProductType product = createProduct(paNaverGoods, paNaverGoodsImage, goodsPrice, transServiceNo, paCertificationList);
			if (product == null) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				result.setMessage("네이버에 존재하지 않는 상품입니다.");
				return result;
			}

			log.info("상품수정 API 호출 {}", goodsCode);
			String productId = productApiService.updateProduct(goodsCode, product, transServiceNo);

			if (StringUtils.hasText(productId)) {
				result.setCode("200");
				result.setMessage(PaNaverApiRequest.API_SUCCESS_CODE);

				paNaverGoods.setProductId(productId);
				paNaverResultService.saveTransProduct(paNaverGoods, goodsPrice);
			} else {
				result.setCode("500");
				result.setMessage("상품 수정에 실패했습니다.");
			}
		} catch (TransApiException ex) {
		    		    
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단
				paNaverGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
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

    private ResponseMsg callManageOption(String goodsCode, String paCode, String procId, long transServiceNo) {
		
		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 옵션등록/수정서비스 Start - {} =====", goodsCode);
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("paGoodsCode", goodsCode);
			paramMap.put("paCode", paCode);

			List<PaGoodsdtMapping> goodsdtList = paNaverGoodsService.selectPaGoodsdt(paramMap);
			if (goodsdtList.size() < 1) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			OptionType options = createProductOption(goodsdtList);

			log.info("옵션등록/수정 API 호출 {}", goodsCode);
			String productId = productApiService.manageOption(goodsCode, options, transServiceNo);

			if (StringUtils.hasText(productId)) {
				result.setCode("200");
				result.setMessage(PaNaverApiRequest.API_SUCCESS_CODE);
				paNaverResultService.saveTransProductOption(goodsdtList, goodsCode, paCode, procId);
				
				// 옵션코드 매핑
				options  = productApiService.getOption(goodsCode, productId, transServiceNo);
				for (CombinationOptionItemType option : options.getCombination().getItemList().getItem()) {
					String goodsdtCode = option.getSellerManagerCode();
					String paOptionCode = String.valueOf(option.getId());
					log.info("상품:{} 단품:{} 옵션코드:{}", goodsCode, goodsdtCode, paOptionCode);
					if (!"null".equals(paOptionCode)) {
						goodsdtMappingMapper.updateOptionCode(goodsCode, goodsdtCode, paCode, procId, paOptionCode);
					}
				}
				
			} else {
				result.setCode("500");
				result.setMessage("옵션등록/수정에 실패했습니다.");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단
				paNaverGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
						StringUtil.truncate(result.getMessage(), 500));
			}
			
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 옵션등록/수정서비스 End - {} =====", goodsCode);
		}
		return result;
	}
    
    private ResponseMsg callRegisterImage(String goodsCode, String paCode, String procId, long transServiceNo) {
		
		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 이미지등록서비스 Start - {} =====", goodsCode);
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("paGoodsCode", goodsCode);
			paramMap.put("paGroupCode", PaGroup.NAVER.code());

			PaGoodsImage paGoodsImage = paNaverGoodsService.selectPaGoodsImage(paramMap);
			if (paGoodsImage == null) {
				result.setCode("404");
				result.setMessage("등록된 상품이미지가 없습니다.");
				return result;
			}

			String path = paGoodsImage.getImageUrl1()+paGoodsImage.getImageUrl2();	
			String imageResizePath = "/dims/resize/600X600";
			PaNaverGoodsImage paNaverGoodsImage = new PaNaverGoodsImage();
			log.info("이미지등록 API 호출 {}", goodsCode);
			if (paGoodsImage.getImageP() != null)
				paNaverGoodsImage.setImageNaverP(
						productApiService.registerImage(goodsCode, path + paGoodsImage.getImageP() + imageResizePath, transServiceNo));
			if (paGoodsImage.getImageAp() != null)
				paNaverGoodsImage.setImageNaverAp(
						productApiService.registerImage(goodsCode, path + paGoodsImage.getImageAp() + imageResizePath, transServiceNo));
			if (paGoodsImage.getImageBp() != null)
				paNaverGoodsImage.setImageNaverBp(
						productApiService.registerImage(goodsCode, path + paGoodsImage.getImageBp() + imageResizePath, transServiceNo));
			if (paGoodsImage.getImageCp() != null)
				paNaverGoodsImage.setImageNaverCp(
						productApiService.registerImage(goodsCode, path + paGoodsImage.getImageCp() + imageResizePath, transServiceNo));
			if (paGoodsImage.getImageDp() != null)
				paNaverGoodsImage.setImageNaverDp(
						productApiService.registerImage(goodsCode, path + paGoodsImage.getImageDp() + imageResizePath, transServiceNo));
			
			result.setCode("200");
			result.setMessage(PaNaverApiRequest.API_SUCCESS_CODE);
			
			paNaverGoodsImage.setGoodsCode(goodsCode);
			paNaverGoodsImage.setLastSyncDate(paGoodsImage.getLastSyncDate());
			paNaverGoodsImage.setInsertId(procId);
			paNaverGoodsImage.setModifyId(procId);
			
			paNaverResultService.saveTransImage(paNaverGoodsImage);
			
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단
				paNaverGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
						StringUtil.truncate(result.getMessage(), 500));
			}
			
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 이미지등록서비스 End - {} =====", goodsCode);
		}
		return result;
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

		String[] rejectMatch = new String[] { "등록불가인 단어"
				, "등록불가인 특수문자"
				, "사용하실 수 없습니다"
				, "입력해 주세요"
				, "중복된 조합형 옵션"
				, "인증대상"
				};

		return Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s));
	}

	/**
	 * 기술서와 공지사항 설정
	 * 
	 * @param paNaverGoods
	 */
	private void settingDescribeExt(PaNaverGoodsVO paNaverGoods) {

		// 상품구성템플릿
		String goodsCom = StringUtils.hasText(paNaverGoods.getGoodsCom())
				? ("<div style='line-height: 2.0em; font-family: NanumBarunGothic; font-size: 19px;'><div><h4>&middot;&nbsp;상품구성<h4>"
						+ paNaverGoods.getGoodsCom() + "</div></div>")
				: "";

		// 웹기술서
		paNaverGoods.setDescribeExt(
				"<div align='center'><img alt='' src='" + paNaverGoods.getTopImage() + "' /><br /><br /><br />" // 상단이미지
						+ (StringUtils.hasText(paNaverGoods.getCollectImage())
								? "<img alt='' src='" + paNaverGoods.getCollectImage() + "' /><br /><br /><br />"
								: "") // 착불이미지
						+ goodsCom // 상품구성
						+ paNaverGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />" // 기술서
						+ "<img alt='' src='" + paNaverGoods.getBottomImage() + "' /></div>"); // 하단이미지

		// 제휴 공지사항
		if (StringUtils.hasText(paNaverGoods.getNoticeExt())) {
			paNaverGoods.setDescribeExt(
					paNaverGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + paNaverGoods.getDescribeExt());
		}
	}

	private ProductType createProduct(PaNaverGoodsVO paNaverGoods, PaNaverGoodsImageVO paNaverGoodsImage,
			PaGoodsPriceApply goodsPrice, long transServiceNo, List<PaCertificationVO> paCertificationList) throws Exception {

		ProductType product;

		if (paNaverGoods.getProductId() != null) {// 상품 수정시 조회 API실행 후 변경되는 부분만 수정한다.
			// 상품조회 API
//			product = productApiService.getProduct(paNaverGoods.getGoodsCode(), paNaverGoods.getProductId(),
//					transServiceNo);
//			if (product == null)
//				return null;
			// 네이버측 정보고시 개편 후 조회 오류 발생하여 바로 수정하도록 임시 적용
			product = new ProductType();
			product.setProductId(Long.parseLong(paNaverGoods.getProductId()));
		} else {
			product = new ProductType();
			product.setStockQuantity(paNaverGoods.getTransOrderAbleQty()); // 재고수량
			product.setCategoryId(paNaverGoods.getLmsdKey()); // 카테고리
		}

		product.setSaleStartDate(paNaverGoods.getSaleStartDate().toString()); // 판매시작일 (YYYY-MM-DD HH:mm)
		if(paNaverGoods.getSaleEndDate()==null) product.setSaleEndDate("2999-12-31"); // 판매종료일
		else product.setSaleEndDate(paNaverGoods.getSaleEndDate().toString());
		product.setSalePrice((long)goodsPrice.getSalePrice()); // 판매가(원)	
		
		// 판매상태 (판매중, 판매중지)
		product.setStatusType(PaSaleGb.FORSALE.code().equals(paNaverGoods.getPaSaleGb()) ? "SALE" : "SUSP");

		product.setSaleType("NEW"); // 상품판매유형 (신상품, 중고, 진열, 리퍼)

		//주문 제작 상품 여부 ( “Y” 또는 “N” ) 
		product.setCustomMade("1".equals(paNaverGoods.getOrderMakeYN()) ? "Y" : "N");
		// 맞춤 제작 상품 여부
		product.setCustomProductYn("1".equals(paNaverGoods.getOrderMakeYN()) ? "Y" : "N");
		
		product.setProductSummary(createProductSummaryInfo(paNaverGoods.getGoodsCode())); // 상품 요약 정보	
		product.setModel(createModelInfo(paNaverGoods, product.getProductSummary())); //모델정보
		
		// 상품명 특수문자 처리 
		String paGoodsName = paNaverGoods.getPaGoodsName().replace("*", "X").replace("?", ""); 
		// 상품명에 브랜드명 없을 시 추가
		if(!"기타".equals(paNaverGoods.getBrandName())) {
			if(paGoodsName.startsWith(paNaverGoods.getBrandName())){			
				paGoodsName = paGoodsName.replaceFirst(paNaverGoods.getBrandName(), "[" + paNaverGoods.getBrandName() + "]");
			}else if(!paGoodsName.startsWith("["+paNaverGoods.getBrandName()+"]")){
				paGoodsName = "[" + paNaverGoods.getBrandName() + "]" + paGoodsName;
			}	
		}		

		String collectYn = paNaverGoods.getCollectYn();
		
		if ("1".equals(collectYn)) {
			paGoodsName = "(착불)" + paGoodsName;
		}
		product.setName(paGoodsName); // 상품명

		product.setSellerManagementCode(paNaverGoods.getGoodsCode()); // 판매자가 관리하는 상품 코드

		if(paNaverGoods.getExceptCode() != null){
// 		    네이버 필수인증정보 카테고리 상품의 경우 인증대상을 모두 예외로 세팅하여 반영하고 인증번호는 세팅되지 않도록 주석처리한다. 2022.10.25 LEEJY
			if(paNaverGoods.getExceptYn() > 0) {	// 2023-02-01 GBJEONG 예외인증정보 유무
				product.setCertificationList(createCertificationListInfo(paCertificationList)); // 인증정보
			} else {
				if("KC".equals(paNaverGoods.getExceptCode())) {	// KC 인증대상
					product.setkCCertifiedProductExclusion("Y");
				}else if("GRN".equals(paNaverGoods.getExceptCode())) { // 친환경 인증대상
					product.setGreenCertifiedProductExclusion("Y");
				}else if("CHI".equals(paNaverGoods.getExceptCode())) { // 어린이제품 인증대상
					product.setChildCertifiedProductExclusion("Y");
				}
			}
		}

		product.setOriginArea(createOriginAreaInfo(paNaverGoods)); // 원산지 정보
		
		// 부가세 타입 코드
		if ("1".equals(paNaverGoods.getTaxSmallYN())) {
			product.setTaxType("SMALL");
		} else {
			product.setTaxType("1".equals(paNaverGoods.getTaxYN()) ? "TAX" : "DUTYFREE");
		}

		// 미성년자 구매가능 여부
		product.setMinorPurchasable("0".equals(paNaverGoods.getAdultYN()) ? "Y" : "N");
		
		product.setImage(createImageInfo(paNaverGoodsImage)); // 이미지 정보

		product.setDetailContent(paNaverGoods.getDescribeExt().replaceAll("\\p{C}", "")); // 상품 상세 내용.
		
		product.setAfterServiceTelephoneNumber(paNaverGoods.getCsTel()); // A/S 전화번호
		product.setAfterServiceGuideContent(paNaverGoods.getCsDetail()); // A/S 내용. HTML 입력 불가	

		product.setDelivery(createDeliveryInfo(paNaverGoods)); // 배송정보. 배송 없는 상품인 경우 아예 입력하지 않는다.
		
		SellerDiscountType sellerDiscount = new SellerDiscountType();

		sellerDiscount.setAmount(String.valueOf((long)(goodsPrice.getSalePrice() - goodsPrice.getBestPrice())));
		sellerDiscount.setMobileAmount(sellerDiscount.getAmount());
		product.setSellerDiscount(sellerDiscount); // 판매자 즉시할인	
				
		product.setPurchaseReviewExposure("Y"); // 구매평 노출 여부
		product.setRegularCustomerExclusiveProduct("N"); // 단골 회원 전용 상품 여부
		product.setKnowledgeShoppingProductRegistration("Y"); // 네이버 쇼핑 등록

		// 1회 최대구매수량 
		product.setMaxPurchaseQuantityPerOrder(paNaverGoods.getOrderMaxQty() > 0 ? (long) paNaverGoods.getOrderMaxQty() : 100);
		
		
		// 1인 최대구매수량
		// 1인 구매시 최대구매수량은 1회 구매시 최대구매수량 이상으로 입력
		if ("1".equals(paNaverGoods.getCustOrdQtyCheckYN()) && paNaverGoods.getTermOrderQty() > 0
				&& paNaverGoods.getTermOrderQty() > product.getMaxPurchaseQuantityPerOrder())
			product.setMaxPurchaseQuantityPerId((long) paNaverGoods.getTermOrderQty());
		else
			product.setMaxPurchaseQuantityPerId(product.getMaxPurchaseQuantityPerOrder());
		
		if (paNaverGoods.getOrderMinQty() >= 2)
			product.setMinPurchaseQuantity(paNaverGoods.getOrderMinQty()); // 최소구매수량. 2개부터 설정 가능하다. 최소구매수량이 1개인 경우 아예 입력하지 않아야 한다.
			
		product.setRegularCustomerExclusiveProduct("N"); // 구독회원 전용 상품 여부
		product.setKnowledgeShoppingProductRegistration("Y"); // 지식쇼핑 등록 여부. 지식쇼핑 광고주가 아닌 경우 N으로 저장된다.
		
		return product;
	}
	
	/**
	 * 상품 요약 정보
	 * 
	 * @param goodsCode
	 * @return
	 */
	private ProductSummaryType createProductSummaryInfo(String goodsCode) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("groupCode", PaGroup.NAVER.code());
		
		ProductSummaryType productSummary = new ProductSummaryType();
		String offerType = paNaverGoodsService.selectPaOfferType(paramMap);
		offerType = offerType.substring(2, 4);

		switch (offerType) {
		case "01": // 의류
			WearSummaryType wearSummaryType = paNaverGoodsService.selectWearSummary(paramMap);
			productSummary.setWear(wearSummaryType);
			break;
		case "02": // 신발
			ShoesSummaryType shoesSummaryType = paNaverGoodsService.selectShoesSummary(paramMap);
			productSummary.setShoes(shoesSummaryType);
			break;
		case "03": // 가방
			BagSummaryType bagSummaryType = paNaverGoodsService.selectBagSummary(paramMap);
			productSummary.setBag(bagSummaryType);
			break;
		case "04": // 패션잡화
			FashionItemsSummaryType fashionItemsSummaryType = paNaverGoodsService.selectFashionItemsSummary(paramMap);
			productSummary.setFashionItems(fashionItemsSummaryType);
			break;
		case "05": // 침구류/커튼
			SleepingGearSummaryType sleepingGearSummaryType = paNaverGoodsService.selectSleepingGearSummary(paramMap);
			productSummary.setSleepingGear(sleepingGearSummaryType);
			break;
		case "06": // 가구
			FurnitureSummaryType furnitureSummaryType = paNaverGoodsService.selectFurnitureSummary(paramMap);
			productSummary.setFurniture(furnitureSummaryType);
			break;
		case "07": // 영상가전
			ImageAppliancesSummaryType imageAppliancesSummaryType = paNaverGoodsService
					.selectImageAppliancesSummary(paramMap);
			productSummary.setImageAppliances(imageAppliancesSummaryType);
			break;
		case "08": // 가정용 전기제품
			HomeAppliancesSummaryType homeAppliancesSummaryType = paNaverGoodsService
					.selectHomeAppliancesSummary(paramMap);
			productSummary.setHomeAppliances(homeAppliancesSummaryType);
			break;
		case "09": // 계절가전
			SeasonAppliancesSummaryType seasonAppliancesSummaryType = paNaverGoodsService
					.selectSeasonAppliancesSummary(paramMap);
			productSummary.setSeasonAppliances(seasonAppliancesSummaryType);
			break;
		case "10": // 사무용기기
			OfficeAppliancesSummaryType officeAppliancesSummaryType = paNaverGoodsService
					.selectOfficeAppliancesSummary(paramMap);
			productSummary.setOfficeAppliances(officeAppliancesSummaryType);
			break;
		case "11": // 광학기기
			OpticsAppliancesSummaryType opticsAppliancesSummaryType = paNaverGoodsService
					.selectOpticsAppliancesSummary(paramMap);
			productSummary.setOpticsAppliances(opticsAppliancesSummaryType);
			break;
		case "12": // 소형전자
			MicroElectronicsSummaryType microElectronicsSummaryType = paNaverGoodsService
					.selectMicroElectronicsSummary(paramMap);
			productSummary.setMicroElectronics(microElectronicsSummaryType);
			break;
		case "13": // 후대폰
			break;
		case "14": // 네비게이션
			NavigationSummaryType navigationSummaryType = paNaverGoodsService.selectNavigationSummary(paramMap);
			productSummary.setNavigation(navigationSummaryType);
			break;
		case "15": // 자동차용품
			CarArticlesSummaryType carArticlesSummaryType = paNaverGoodsService.selectCarArticlesSummary(paramMap);
			productSummary.setCarArticles(carArticlesSummaryType);
			break;
		case "16": // 의료기기
			MedicalAppliancesSummaryType medicalAppliancesSummaryType = paNaverGoodsService
					.selectMedicalAppliancesSummary(paramMap);
			productSummary.setMedicalAppliances(medicalAppliancesSummaryType);
			break;
		case "17": // 주방용품
			KitchenUtensilsSummaryType kitchenUtensilsSummaryType = paNaverGoodsService
					.selectKitchenUtensilsSummary(paramMap);
			if (kitchenUtensilsSummaryType.getSize() == null) {
				kitchenUtensilsSummaryType.setSize("상품상세 참조");
			} else if (kitchenUtensilsSummaryType.getSize().length() > 200) {
				kitchenUtensilsSummaryType.setSize((String) kitchenUtensilsSummaryType.getSize().subSequence(0, 200));
			}
			
			productSummary.setKitchenUtensils(kitchenUtensilsSummaryType);
			break;
		case "18": // 화장품
			CosmeticSummaryType cosmeticSummaryType = paNaverGoodsService.selectCosmeticSummary(paramMap);
			productSummary.setCosmetic(cosmeticSummaryType);
			break;
		case "19": // 귀금속/보석/시계류
			JewellerySummaryType jewellerySummaryType = paNaverGoodsService.selectJewellerySummary(paramMap);
			productSummary.setJewellery(jewellerySummaryType);
			break;
		case "20": // 식품(농수산물)
			FoodSummaryType foodSummaryType = paNaverGoodsService.selectFoodSummary(paramMap);
			productSummary.setFood(foodSummaryType);
			break;
		case "21": // 가공식품
			GeneralFoodSummaryType generalFoodSummaryType = paNaverGoodsService.selectGeneralFoodSummary(paramMap);
			productSummary.setGeneralFood(generalFoodSummaryType);
			break;
		case "22": // 건강기능식품
			DietFoodSummaryType dietFoodSummaryType = paNaverGoodsService.selectDietFoodSummary(paramMap);
			productSummary.setDietFood(dietFoodSummaryType);
			break;
		case "23": // 어린이제품
			KidsSummaryType kidsSummaryType = paNaverGoodsService.selectKidsSummary(paramMap);
			productSummary.setKids(kidsSummaryType);
			break;
		case "24": // 악기
			MusicalInstrumentSummaryType musicalInstrumentSummaryType = paNaverGoodsService
					.selectMusicalInstrumentSummary(paramMap);
			productSummary.setMusicalInstrument(musicalInstrumentSummaryType);
			break;
		case "25": // 스포츠용품
			SportsEquipmentSummaryType sportsEquipmentSummaryType = paNaverGoodsService
					.selectSportsEquipmentSummary(paramMap);
			productSummary.setSportsEquipment(sportsEquipmentSummaryType);
			break;
		case "26": // 서적
			BooksSummaryType booksSummaryType = paNaverGoodsService.selectBooksSummary(paramMap);
			productSummary.setBooks(booksSummaryType);
			break;
		case "27": // 호텔/팬션 예약
			break;
		case "28": // 여행패키지
			break;
		case "29": // 항공권
			break;
		case "30": // 자동차 대여 서비스
			break;
		case "31": // 물품대여 서비스
			RentalEtcSummaryType rentalEtcSummaryType1 = paNaverGoodsService.selectRentalEtcSummary(paramMap);
			productSummary.setRentalEtc(rentalEtcSummaryType1);
			break;
		case "32": // 물품대여 서비스
			RentalEtcSummaryType rentalEtcSummaryType2 = paNaverGoodsService.selectRentalEtcSummary(paramMap);
			productSummary.setRentalEtc(rentalEtcSummaryType2);
			break;
		case "33": // 디지털 콘텐츠(음원/게임/인터넷강의 등)
			DigitalContentsSummaryType digitalContentsSummaryType = paNaverGoodsService
					.selectDigitalContentsSummary(paramMap);
			productSummary.setDigitalContents(digitalContentsSummaryType);
			break;
		case "34": // 상품권/쿠폰
			break;
		case "35": // 모바일쿠폰
			break;
		case "36": // 영화/공연
			break;
		case "37": // 기타 용역
			EtcServiceSummaryType etcServiceSummaryType = paNaverGoodsService.selectEtcServiceSummary(paramMap);
			productSummary.setEtcService(etcServiceSummaryType);
			break;
		case "38": // 기타 재화
			EtcSummaryType etcSummaryType = paNaverGoodsService.selectEtcSummary(paramMap);
			productSummary.setEtc(etcSummaryType);
			break;
		default:
			break;
		}
		return productSummary;
	}

	/**
	 * 모델 정보 (모델코드, 브랜드명, 제조사명)
	 * 상품에 입력할 모델정보를 생성한다.
	 * 모델 코드는 GetModelList, 브랜드명은 GetBrandList, 제조사명은 GetManufacturerList API로 조회할 수 있다.
	 * 
	 * @return 모델 정보
	 */
	private ModelType createModelInfo(PaNaverGoodsVO paNaverGoodsVO, ProductSummaryType productSummaryType) throws Exception {

		ModelType model = new ModelType();
		
		if(paNaverGoodsVO.getModelNo() != null){
			model.setId(ComUtil.objToLong(paNaverGoodsVO.getModelNo())); 			
		}
		
		model.setBrandName(paNaverGoodsVO.getBrandName()); // 브랜드명. GetBrandList로 샵N DB에 등록되어 있는 브랜드명을 조회할 수 있다.
		// 모델ID와 함께 입력하는 경우 ModelReturnType의 BrandName을 입력하면 된다.
		model.setManufacturerName(paNaverGoodsVO.getMakecoName()); // 제조사명. GetManufacturerList로 샵N DB에 등록되어 있는 제조사명을 조회할 수 있다.
		// 모델ID와 함께 입력하는 경우 ModelReturnType의 ManufacturerName을 입력하면 된다.

		if (productSummaryType == null) return model;

		ParamMap paramMap = new ParamMap();
		paramMap.put("goodsCode", paNaverGoodsVO.getGoodsCode());
		paramMap.put("paCode", paNaverGoodsVO.getPaCode());
		
		String modelInputYn = paNaverGoodsService.selectModelInputYN(paramMap);
		
		if ("1".equals(modelInputYn)) {
			if (productSummaryType.getFurniture() != null)
				model.setModelName(productSummaryType.getFurniture().getItemName());
			else if (productSummaryType.getImageAppliances() != null)
				model.setModelName(productSummaryType.getImageAppliances().getItemName());
			else if (productSummaryType.getHomeAppliances() != null)
				model.setModelName(productSummaryType.getHomeAppliances().getItemName());
			else if (productSummaryType.getSeasonAppliances() != null)
				model.setModelName(productSummaryType.getSeasonAppliances().getItemName());
			else if (productSummaryType.getOfficeAppliances() != null)
				model.setModelName(productSummaryType.getOfficeAppliances().getItemName());
			else if (productSummaryType.getOpticsAppliances() != null)
				model.setModelName(productSummaryType.getOpticsAppliances().getItemName());
			else if (productSummaryType.getMicroElectronics() != null)
				model.setModelName(productSummaryType.getMicroElectronics().getItemName());
			else if (productSummaryType.getNavigation() != null)
				model.setModelName(productSummaryType.getNavigation().getItemName());
			else if (productSummaryType.getCarArticles() != null)
				model.setModelName(productSummaryType.getCarArticles().getItemName());
			else if (productSummaryType.getMedicalAppliances() != null)
				model.setModelName(productSummaryType.getMedicalAppliances().getItemName());
			else if (productSummaryType.getKitchenUtensils() != null)
				model.setModelName(productSummaryType.getKitchenUtensils().getItemName());
			else if (productSummaryType.getKids() != null)
				model.setModelName(productSummaryType.getKids().getItemName());
			else if (productSummaryType.getMusicalInstrument() != null)
				model.setModelName(productSummaryType.getMusicalInstrument().getItemName());
			else if (productSummaryType.getSportsEquipment() != null)
				model.setModelName(productSummaryType.getSportsEquipment().getItemName());
			else if (productSummaryType.getRentalEtc() != null)
				model.setModelName(productSummaryType.getRentalEtc().getItemName());
			else if (productSummaryType.getEtc() != null)
				model.setModelName(productSummaryType.getEtc().getItemName());
			else
				model.setModelName("상품상세참조");
		}

		return model;
	}

	/**
	 * 원산지 정보 
	 * 상품에 입력할 원산지 정보를 생성한다.
	 * 원산지 코드는 GetOriginAreaList, GetAllOriginAreaList, GetSubOriginAreaList API를 사용해서 조회할 수 있다.
	 *
	 * @return 원산지 정보
	 */
	private OriginAreaType createOriginAreaInfo(PaNaverGoodsVO paNaverGoodsVO) {
		OriginAreaType originArea = new OriginAreaType();
		originArea.setCode(paNaverGoodsVO.getNaverOriginCode()); // 원산지 코드 (상세설명에 표시)
		originArea.setPlural("N"); // 복수 원산지 여부
		
//		if(!paNaverGoodsVO.getNaverOriginCode().startsWith("00")){
//			 originArea.setImporter(paNaverGoodsVO.getMakecoName()); // 수입사. 원산지 코드가 수입산(ex: 미국, 중국 등)인 경우 입력한다.
//		}
		
		// originArea.setContent("원산지 직접입력"); // 원산지 직접입력. 원산지코드가 04 (직접입력)인 경우에만 입력한다.
		return originArea;
	}

	/**
	 * 이미지 정보 생성
	 * 상품 이미지 정보를 생성한다.
	 * 이미지 URL은 이미지 업로드 API(UploadImage)로 업로드하고 반환받은 이미지 URL을 입력해야 한다.
	 *
	 * @return 이미지 정보
	 */
	private ImageType createImageInfo(PaNaverGoodsImage paNaverGoodsImage) {
		int index = 0;

		ImageType image = new ImageType();
		image.setRepresentative(new URLType());
		image.getRepresentative().setURL(paNaverGoodsImage.getImageNaverP());

		image.setOptionalList(new OptionalListType());

		if (paNaverGoodsImage.getImageNaverAp() != null) {
			image.getOptionalList().getOptional().add(index, new URLType());
			image.getOptionalList().getOptional().get(index).setURL(paNaverGoodsImage.getImageNaverAp());
			index++;
		}
		if (paNaverGoodsImage.getImageNaverBp() != null) {
			image.getOptionalList().getOptional().add(index, new URLType());
			image.getOptionalList().getOptional().get(index).setURL(paNaverGoodsImage.getImageNaverBp());
			index++;
		}
		if (paNaverGoodsImage.getImageNaverCp() != null) {
			image.getOptionalList().getOptional().add(index, new URLType());
			image.getOptionalList().getOptional().get(index).setURL(paNaverGoodsImage.getImageNaverCp());
			index++;
		}
		if (paNaverGoodsImage.getImageNaverDp() != null) {
			image.getOptionalList().getOptional().add(index, new URLType());
			image.getOptionalList().getOptional().get(index).setURL(paNaverGoodsImage.getImageNaverDp());
			index++;
		}

		return image;
	}
	

	/**
	 * 배송 정보
	 * 상품 배송정보를 입력한다.
	 * 배송 없는 상품은 DeliveryType 값을 null로 입력해야 한다.
	 *
	 * @return 배송 정보
	 */
	private DeliveryType createDeliveryInfo(PaNaverGoodsVO paNaverGoodsVO) throws Exception {
		DeliveryType delivery = new DeliveryType();
					
		delivery.setType("1"); // 배송 방법 유형 코드 (택배, 소포, 등기 / 직접배송)
		delivery.setBundleGroupAvailable("N"); // 묶음 배송 가능 여부		
		
		switch (paNaverGoodsVO.getShipCostCode().substring(0, 2)) {
		case "FR": // 무료
			delivery.setFeeType("1");
			break;
		case "CN":
		case "PL": // 조건부
			delivery.setFeeType("2");
			delivery.setFreeConditionalAmount(paNaverGoodsVO.getShipCostBaseAmt());
			break;
		default: // 상품별
			delivery.setFeeType("3");
			break;
		}
		
		delivery.setBaseFee(paNaverGoodsVO.getOrdCost()); // 기본 배송비
		delivery.setPayType("2"); // 배송비 결제 방식 타입 코드. (착불, 선결제, 착불 또는 선결제)
		
		// 지역별 추가 배송 권역 (2권역 - 내륙/제주 및 도서산간, 3권역 - 내륙/제주 외 도서 산간)
		int jejuCost = paNaverGoodsVO.getJejuCost() - paNaverGoodsVO.getOrdCost();
		int islandCost = paNaverGoodsVO.getIslandCost() - paNaverGoodsVO.getOrdCost();
		if (jejuCost > 0 || islandCost > 0) {
			delivery.setAreaType("3"); // 지역별 추가 배송 권역 (2권역 - 내륙/제주 및 도서산간, 3권역 - 내륙/제주 외 도서 산간)
			delivery.setArea2ExtraFee(jejuCost <= 0 ? 100 : jejuCost); // 2권역 배송비
			delivery.setArea3ExtraFee(islandCost <= 0 ? 100 : islandCost); // 3권역 배송비
		}
		
		delivery.setReturnDeliveryCompanyPriority("0"); // 반품/교환 택배사. GetReturnsCompanyList API로 택배사 코드를 조회하여 입력한다. (0.우체국택배)
		
		 // 반품 배송비
		if(paNaverGoodsVO.getOrdCost()==0)
			delivery.setReturnFee(paNaverGoodsVO.getReturnCost()/2);
		else
			delivery.setReturnFee(paNaverGoodsVO.getReturnCost());	
			
		delivery.setExchangeFee(paNaverGoodsVO.getChangeCost()); // 교환 배송비

		ParamMap paramMap = new ParamMap();
		paramMap.put("goodsCode", paNaverGoodsVO.getGoodsCode());
		paramMap.put("paCode", paNaverGoodsVO.getPaCode());
		
		PaDeliveryVO paDeliveryVO = paNaverGoodsService.selectPaDelivery(paramMap);
		
		// 대표 주소지로 설정
//		delivery.setReturnAddressId(Long.parseLong(paDeliveryVO.getReturnAddressId())); 		// 반품/교환지 주소 코드. GetAddressBookList API로 주소 코드를 조회하여 입력한다.
//		if(paDeliveryVO.getShippingAddressId() != null)
//			delivery.setShippingAddressId(Long.parseLong(paDeliveryVO.getShippingAddressId())); // 출고지 주소 코드. GetAddressBookList API로 주소 코드를 조회하여 입력한다.
//		else
//			delivery.setShippingAddressId(Long.parseLong(paDeliveryVO.getReturnAddressId()));	// 츨고지가 없으면 회수지 입력
		
		delivery.setDeliveryCompany(paDeliveryVO.getDeliveryCompany());
				
		return delivery;
	}

	private OptionType createProductOption(List<PaGoodsdtMapping> paGoodsdtMappingList) throws Exception {

		OptionType options = new OptionType();
		
		options.setProductId(Long.parseLong(paGoodsdtMappingList.get(0).getPaGoodsCode()));
		options.setSortType("CRE"); // 등록순
		
		CombinationOptionNamesType optionNames = new  CombinationOptionNamesType();
		optionNames.setName1("옵션");

		long MAX_STOCK = 99_999;
		List<CombinationOptionItemType> optionList = new ArrayList<CombinationOptionItemType>();		
		for(PaGoodsdtMapping optionItem : paGoodsdtMappingList) {
			CombinationOptionItemType option = new CombinationOptionItemType();
			option.setValue1(optionItem.getGoodsdtInfo().replace("*", "X").replace("?", "").replace("<", "(").replace(">", ")").replace("\"", "").replace("\\", ""));
			// 상품전체재고 최대 99,999,999개
			option.setQuantity(Long.parseLong(optionItem.getTransOrderAbleQty()) > MAX_STOCK ? MAX_STOCK
					: Long.parseLong(optionItem.getTransOrderAbleQty()));
			option.setSellerManagerCode(optionItem.getGoodsdtCode());
			option.setUsable("Y");
			optionList.add(option);
		}

		CombinationOptionItemListType optionItemList = new CombinationOptionItemListType();
		optionItemList.setItem(optionList);

		CombinationOptionType combination = new CombinationOptionType();
		combination.setNames(optionNames);
		combination.setItemList(optionItemList);
		
		options.setCombination(combination);
		
		return options;
	}
	
	/**
	 * 인증정보
	 * <p/>
	 * 상품에 입력할 인증정보를 생성한다.
	 * GetCategoryInfo API를 호출하면 해당 카테고리에 입력할 수 있는 인증정보코드를 조회할 수 있습니다.
	 *
	 * @return 인증정보 목록
	 */
	private static CertificationListType createCertificationListInfo(List<PaCertificationVO> paCertificationList) {
		CertificationType certification = null;
		CertificationListType certificationList = new CertificationListType();
		
		for(PaCertificationVO list : paCertificationList){
			certification = new CertificationType();
			certification.setId(Long.parseLong(list.getCertiCode())); // 인증유형 ID. GetCategoryInfo에서 조회하는 CertificationCategoryListType의 code 값을 입력
			certification.setKindType(list.getExceptCode()); // 인증 정보 종류
			certification.setMark("Y"); // 인증 마크 사용 여부
			
			if(list.getCertiAgencyYn().equals("1"))
				certification.setName(list.getCertiAgency()); // 인증기관
			if(list.getCertiNoYn().equals("1"))
				certification.setNumber(list.getCertiNo()); // 인증번호
			if(list.getCertiCompanyYn().equals("1"))
				certification.setCompanyName(list.getCertiCompany()); // 인증상호
			
			certificationList.getCertification().add(certification);
		}
		return certificationList;
	}
}
