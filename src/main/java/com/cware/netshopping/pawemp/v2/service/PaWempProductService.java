package com.cware.netshopping.pawemp.v2.service;

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
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.PaSaleGb;
import com.cware.netshopping.common.code.PaStatus;
import com.cware.netshopping.common.code.SaleGb;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaWempGoodsVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaWempGoods;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.service.SyncProductService;
import com.cware.netshopping.pawemp.goods.service.PaWempGoodsService;
import com.cware.netshopping.pawemp.v2.domain.Basic;
import com.cware.netshopping.pawemp.v2.domain.Detail;
import com.cware.netshopping.pawemp.v2.domain.Etc;
import com.cware.netshopping.pawemp.v2.domain.Notice;
import com.cware.netshopping.pawemp.v2.domain.NoticeGroup;
import com.cware.netshopping.pawemp.v2.domain.Option;
import com.cware.netshopping.pawemp.v2.domain.OptionValue;
import com.cware.netshopping.pawemp.v2.domain.Product;
import com.cware.netshopping.pawemp.v2.domain.Sale;
import com.cware.netshopping.pawemp.v2.repository.PaWempGoodsMapper;

@Service
public class PaWempProductService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;

	@Autowired
	@Qualifier("pawemp.goods.paWempGoodsService")
	private PaWempGoodsService paWempGoodsService;

	@Autowired
	PaWempProductApiService productApiService;

	@Autowired
	WempProductService wempProductService;

	@Autowired
	SyncProductService syncProductService;

	@Autowired
	PaWempGoodsMapper paWempGoodsMapper;

	@Autowired
	PaWempResultService paWempResultService;

	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;

	@Autowired
	TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 위메프 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public ResponseMsg transProduct(String goodsCode, String paCode, String procId) {

		ResponseMsg result = new ResponseMsg("", "");

		// 상품동기화
		PaGoodsSync sync = syncProductService.syncProduct(goodsCode, PaGroup.WEMP.code(), procId);
		result = sync.getResponseMsg();
		if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) {
			return result;
		}

		// 스토아상품조회
		PaWempGoods paWempGoods = paWempGoodsMapper.getGoods(goodsCode, paCode);

		if (paWempGoods == null) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("연동 대상 상품이 아닙니다.");
			return result;
		}

		// 입점요청중인건은 처리하지 않음
		if (PaStatus.PROCEEDING.code().equals(paWempGoods.getPaStatus())) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("입점요청중인 상품입니다.");
			return result;
		}

		// 입점요청/입점반려건에 대해서 신규입점 요청
		if (PaStatus.REQUEST.code().equals(paWempGoods.getPaStatus())
				|| PaStatus.REJECT.code().equals(paWempGoods.getPaStatus())) {

			// 동기화에서 필터링된 경우
			if (String.valueOf(HttpStatus.NO_CONTENT.value()).equals(result.getCode())) {
				return result;
			}

			if (goodsTargetMapper.existsGoodsTarget(goodsCode, paCode, PaGroup.WEMP.code()) < 1) {
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

		if ("1".equals(paWempGoods.getTransSaleYn()) && PaSaleGb.SUSPEND.code().equals(paWempGoods.getPaSaleGb())) {
			// 판매중지
			transService = wempProductService.stopSaleProduct(goodsCode, paCode, procId);
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

		if ("1".equals(paWempGoods.getTransTargetYn())) {
			// 상품수정
			ResponseMsg updated = updateProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(updated.getCode())) {
				isUpdated = true;
				result.setMessage("수정완료되었습니다.");
			} else
				return updated;
		} else {
			// 재고변경
			transService = wempProductService.updateStockProduct(goodsCode, paCode, procId);
			if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) {
				result.setMessage((isUpdated ? result.getMessage() + " " : "") + transService.getResultMsg());
				isStock = true;
			}
		}

		boolean isResume = false;
		if ("1".equals(paWempGoods.getTransSaleYn()) && PaSaleGb.FORSALE.code().equals(paWempGoods.getPaSaleGb())) {
			// 판매재개
			transService = wempProductService.resumeSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				result.setMessage("판매재개실패: " + transService.getResultMsg());
				return result;
			}
			if ("1".equals(transService.getSuccessYn())) {
				isResume = true;
				result.setMessage(
						(isStock || isUpdated ? result.getMessage() + " " : "") + "판매재개되었습니다.");
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

	/**
	 * 위메프 신규상품 입점
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
		serviceLog.setServiceNote("[API]위메프-상품입점");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.WEMP.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 상품입점
		ResponseMsg result = callRegisterProduct(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		// 입점요청중 해제
		paWempGoodsMapper.updateClearProceeding(goodsCode, paCode, procId);

		return result;

	}

	/**
	 * 위메프 상품 수정
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
		serviceLog.setServiceNote("[API]위메프-상품수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.WEMP.code());
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
	 * 위메프 상품 조회
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public Product getProduct(String goodsCode, String productNo, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]위메프-상품조회");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.WEMP.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		Product product = null;

		try {
			// 상품조회
			product = productApiService.getProduct(goodsCode, productNo, paCode, serviceLog.getTransServiceNo());
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(PaWempApiRequest.API_SUCCESS_CODE);

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

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		PaWempGoodsVO paWempGoods = null;

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
			paramMap.put("modCase", "INSERT");
			paWempGoods = paWempGoodsService.selectPaWempGoodsInfo(paramMap);

			if (paWempGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paWempGoods.getProductNo() != null) {
				result.setCode("411");
				result.setMessage("이미 등록된 상품입니다. 위메프 상품코드 : " + paWempGoods.getProductNo());
				return result;
			}
			if (paWempGoods.getDescribeExt() == null) {
				paWempGoods.setDescribeExt("");
				if (paWempGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", paWempGoods.getPaGroupCode());

			// 정보고시 조회
			List<PaGoodsOfferVO> goodsOffer = paWempGoodsService.selectPaWempGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.WEMP.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 입점요청중
			paWempGoodsMapper.updateProceeding(goodsCode, paCode, procId);

			// 상품설명설정
			settingDescribeExt(paWempGoods);

			// 단품 옵션 조회
			List<PaGoodsdtMapping> goodsdtMapping = paWempGoodsService.selectPaWempGoodsdtInfoList(paramMap);

			paWempGoods.setModifyId(procId);

			// 위메프 상품 전문
			Product product = createProduct(paWempGoods, goodsdtMapping, goodsOffer, goodsPrice, transServiceNo);

			log.info("상품등록 API 호출 {}", goodsCode);
			String productNo = productApiService.registerProduct(goodsCode, product, paCode, transServiceNo);

			if (StringUtils.hasText(productNo)) {

				result.setCode("200");
				result.setMessage(PaWempApiRequest.API_SUCCESS_CODE);

				paWempGoods.setProductNo(productNo);

				rtnMsg = paWempResultService.saveTransProduct(paWempGoods, goodsPrice, goodsdtMapping);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + paWempGoods.getGoodsCode());
				}

			} else {
				result.setCode("500");
				result.setMessage("no data found");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

			paWempGoodsMapper.rejectTransTarget(goodsCode, paCode, procId,
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
		ParamMap paramMap = new ParamMap();
		String dateTime = "";

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		PaWempGoodsVO paWempGoods = null;

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
			paramMap.put("modCase", "MODIFY");
			paWempGoods = paWempGoodsService.selectPaWempGoodsInfo(paramMap);

			if (paWempGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paWempGoods.getProductNo() == null) {
				result.setCode("411");
				result.setMessage("등록되지 않은 상품입니다. 상품코드 : " + goodsCode);
				return result;
			}
			if (paWempGoods.getDescribeExt() == null) {
				paWempGoods.setDescribeExt("");
				if (paWempGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", paWempGoods.getPaGroupCode());

			// 정보고시 조회
			List<PaGoodsOfferVO> goodsOffer = paWempGoodsService.selectPaWempGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.WEMP.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 상품설명설정
			settingDescribeExt(paWempGoods);

			// 단품 옵션 조회
			List<PaGoodsdtMapping> goodsdtMapping = paWempGoodsService.selectPaWempGoodsdtInfoList(paramMap);

			paWempGoods.setModifyId(procId);

			// 위메프 상품 전문
			Product product = createProduct(paWempGoods, goodsdtMapping, goodsOffer, goodsPrice, transServiceNo);
			if (product == null) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
				return result;
			}

			log.info("상품수정 API 호출 {}", goodsCode);
			String productNo = productApiService.updateProduct(goodsCode, product, paCode, transServiceNo);

			if (StringUtils.hasText(productNo)) {

				result.setCode("200");
				result.setMessage(PaWempApiRequest.API_SUCCESS_CODE);

				paWempGoods.setProductNo(productNo);

				rtnMsg = paWempResultService.saveTransProduct(paWempGoods, goodsPrice, goodsdtMapping);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + paWempGoods.getGoodsCode());
				}

			} else {
				result.setCode("500");
				result.setMessage("no data found");
			}
		} catch (TransApiException ex) {
			
			if (paWempResultService.applyRetention(goodsCode, paCode, procId, ex.getMessage())) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
				return result;
			}

			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단 (레거시는 수기중단처리 없음)
				paWempGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
						StringUtil.truncate(result.getMessage(), 500));
			} else if (result.getMessage() != null && result.getMessage().contains("해당 상품은 이벤트로 지정된 상품입니다")) {
				// 상품전송 타겟팅 리셋
				paWempGoodsMapper.resetTransTarget(goodsCode, paCode, procId,
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

	private boolean isRejectMsg(String rejectMsg) {

		if (!StringUtils.hasText(rejectMsg))
			return false;

		String[] rejectMatch = new String[] { "금칙어가 포함되었습니다"
				, "API로 정보 등록/수정이 불가능한 카테고리"
				, "카테고리의 정보에 따라 변경 불가능" 
				, "중복 옵션이 존재"
				, "byte 이하로 입력"
				, "상품 상세설명 HTML 내 iframe 태그"
				};

		return Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s));
	}

	/**
	 * 기술서와 공지사항 설정
	 * 
	 * @param paWempGoods
	 */
	private void settingDescribeExt(PaWempGoodsVO paWempGoods) {

		// 상품구성템플릿
		String goodsCom = StringUtils.hasText(paWempGoods.getGoodsCom())
				? ("<div style='line-height: 2.0em; font-family: NanumBarunGothic; font-size: 19px;'><div><h4>&middot;&nbsp;상품구성<h4><pre>"
						+ paWempGoods.getGoodsCom() + "</pre></div></div>")
				: "";

		// 웹기술서
		paWempGoods.setDescribeExt(
				"<div align='center'><img alt='' src='" + paWempGoods.getTopImage() + "' /><br /><br /><br />" // 상단이미지
						+ (StringUtils.hasText(paWempGoods.getCollectImage())
								? "<img alt='' src='" + paWempGoods.getCollectImage() + "' /><br /><br /><br />"
								: "") // 착불이미지
						+ goodsCom // 상품구성
						+ paWempGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />" // 기술서
						+ "<img alt='' src='" + paWempGoods.getBottomImage() + "' /></div>"); // 하단이미지

		// 제휴 공지사항
		if (StringUtils.hasText(paWempGoods.getNoticeExt())) {
			paWempGoods.setDescribeExt(
					paWempGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + paWempGoods.getDescribeExt());
		}
	}

	private Product createProduct(PaWempGoodsVO paWempGoods, List<PaGoodsdtMapping> paGoodsDtMappingList,
			List<PaGoodsOfferVO> paGoodsOfferList, PaGoodsPriceApply goodsPrice, long transServiceNo) throws Exception {

		Product product;

		if (paWempGoods.getProductNo() != null) {// 상품 수정시 조회 API실행 후 변경되는 부분만 수정한다.
//			product.setProductNo(paWempGoods.getProductNo());
			// 상품조회 API
			product = productApiService.getProduct(paWempGoods.getGoodsCode(), paWempGoods.getProductNo(),
					paWempGoods.getPaCode(), transServiceNo);
			if (product == null)
				return null;
		} else {
			product = new Product();
		}

		Basic basic = new Basic();
		
		if ("1".equals(paWempGoods.getCollectYn())) {
			basic.setProductName("(착불)"+paWempGoods.getGoodsName());
		} else {
			basic.setProductName(paWempGoods.getGoodsName());
		}

		basic.setProductType("N");  //상품유형 (N:새상품, U:중고, R:리퍼, B:반품(리세일), O:주문제작)
		basic.setDcateCode(paWempGoods.getPaLmsdKey());
		basic.setShipPolicyNo(paWempGoods.getShipPolicyNo());
		basic.setAdultLimitYn("1".equals(paWempGoods.getAdultYn()) ? "Y" : "N");
		basic.setDisplayYn("Y"); //20201021 이후로 항상 검색되게 변경처리

		if(StringUtils.hasText(paWempGoods.getBrandNo())){ // 브랜드 번호
			basic.setBrandNo(paWempGoods.getBrandNo());
		}
		product.setBasic(basic);
		
		Option option = new Option();
		
		option.setSelectOptionUseYn("Y"); // 선택형 옵션 사용여부
		option.setSelectOptionDepth(1); 
		// 옵션제목 (최대 10자)
		option.setSelectOptionTitle1(StringUtil.cut(paGoodsDtMappingList.get(0).getGoodsdtInfoKind().replaceAll("/$", ""), 10));			

		int MAX_STOCK = 99_999;
		List<OptionValue> optionList = new ArrayList<OptionValue>();
		
		for(PaGoodsdtMapping optionItem : paGoodsDtMappingList){
			OptionValue optionValue = new OptionValue();
			optionValue.setOptionValue1(optionItem.getGoodsdtInfo()); // 옵션값1 (최대 70자)
			optionValue.setStockCount(Integer.parseInt(optionItem.getTransOrderAbleQty()) > MAX_STOCK ? MAX_STOCK : 
				Integer.parseInt(optionItem.getTransOrderAbleQty())); // 옵션 재고수량 (0 ~ 99999)
			optionValue.setDisplayYn(SaleGb.FORSALE.code().equals(optionItem.getSaleGb()) ? "Y" : "N"); // 옵션 노춡여부
			optionValue.setSellerOptionCode(optionItem.getGoodsdtCode()); // 업체 옵션코드 최대(50자)
			optionList.add(optionValue);
		}
		option.setSelectOptionValueList(optionList);
		option.setTextOptionUseYn("N");
		product.setOption(option);

		Sale sale = new Sale();

		sale.setSaleStartDate(paWempGoods.getPaSaleStartDate()); // 판매기간 시작일 yyyy-MM-dd HH:00
		// 판매기간 종료일 변경여부 체크
		String add5YearEndDate = DateUtil.addMonth(paWempGoods.getPaSaleStartDate(), 60, "yyyy-MM-dd HH:00"); //판매종료일은 시작일+5년까지 설정할 수 있습니다. 판매시작일 +5년 날짜
		int check = DateUtil.compareTo(paWempGoods.getSaleEndDate(), add5YearEndDate, "yyyy-MM-dd HH:00"); // 판매기간 종료일 데이터 변경됐는지 체크
		
		if(check < 0) {// DB(TGOODS)조회 상품 판매기간 종료일이 시작일+5년 보다 작은경우
			sale.setSaleEndDate(paWempGoods.getSaleEndDate());  // TGOODS에서 조회 판매기간 종료일 yyyy-MM-dd HH:00
			paWempGoods.setPaSaleEndDate(paWempGoods.getSaleEndDate());      // TPAWEMPGOODS에도 동기화
		} else { // 크거나 같을 경우에는 기존 판매기간 종료일로 셋팅, 클경우에는 수정오류가 발생함. 판매종료일은 시작일+5년이므로..)
			sale.setSaleEndDate(add5YearEndDate); // 판매기간 종료일 yyyy-MM-dd HH:00
			paWempGoods.setPaSaleEndDate(add5YearEndDate);
		}
		
		sale.setOriginPrice((long)goodsPrice.getSalePrice()); // 판매가
		sale.setSalePrice((long)goodsPrice.getBestPrice()); //  연동가
		sale.setTaxYn("1".equals(paWempGoods.getTaxYn()) ? "Y" : "N");
		sale.setPurchaseMinCount((int)paWempGoods.getOrderMinQty());


		if(paWempGoods.getOrderMaxQty() > 0) {
			sale.setPurchaseLimitYn("Y");
			sale.setPurchaseLimitDuration("O"); //구매제한 타입 (O:1회, P:기간제한-구매제한일자 필수입력)
			sale.setPurchaseLimitCount((int)paWempGoods.getOrderMaxQty()); // 구매제한 개수 - 구매제한 일자에 대한 구매제한 개수 (0 ~ 9999)
		} else if(paWempGoods.getCustOrdQtyCheckYn() == 1) {  // 고객주문수량검사여부
			sale.setPurchaseLimitYn("Y");
			sale.setPurchaseLimitCount(Integer.parseInt(paWempGoods.getTermOrderQty()));
			if(paWempGoods.getCustOrdQtyCheckTerm() > 0 ) {  // 구매제한 일자가 0보다 클경우 
				sale.setPurchaseLimitDuration("P"); //구매제한 타입 (O:1회, P:기간제한-구매제한일자 필수입력)
				sale.setPurchaseLimitDay((int)paWempGoods.getCustOrdQtyCheckTerm()); //구매제한 일자 (1 ~ 30) 고객주문수량검사기간
			} else {
				sale.setPurchaseLimitDuration("O"); 
			}
		} else { // 구매제한수량 없음
			sale.setPurchaseLimitYn("N");
		}
		sale.setReferencePriceType("WMP"); // 위메프가-미등록/정상가격 미입력
		product.setSale(sale);
		
		Detail detail = new Detail();
		
		// 상품이미지
		Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
		String imageServer = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));

		String imagePath = imageServer + paWempGoods.getImageUrl();
		String imageResizePath = "/dims/resize/600X600";

		// 위메프 개발서버에서 스토아 이미지 개발서버 접근불가로 임시 이미지 등록 처리
//		boolean isTest = imageServer.equals("http://dev-image.skstoa.com");
//		String testImage = "http://dev-image.skstoa.com/goods/029/20020029_c.jpg";

		// 기본이미지
		detail.setBasicImgUrl(imagePath + paWempGoods.getImageP() + imageResizePath); // 상품 대표 이미지 URL (최대 200자) - [최적화 가이드] 사이즈: 460*460 / 최소:	200*200 / 용량: 2MB 이하 / 파일 : JPG, JPEG, PNG
//		if (isTest)	detail.setBasicImgUrl(testImage);

	    List<String> addImgUrlList = new ArrayList<>();
	    if(paWempGoods.getImageAP()!=null) {
	    	addImgUrlList.add(imagePath + paWempGoods.getImageAP() + imageResizePath);
	    }
	    if(paWempGoods.getImageBP()!=null) {
	    	addImgUrlList.add(imagePath + paWempGoods.getImageBP() + imageResizePath);
	    }
		if (addImgUrlList.size() > 0) {
			//상품 추가 이미지 URL 최대 2개
			detail.setAddImgUrlList(addImgUrlList);
		}
		// 리스팅 이미지
		detail.setListImgUrl(detail.getBasicImgUrl());
		
		// 상세설명
		detail.setDescType("HTML");
		detail.setDescHtml(paWempGoods.getDescribeExt());
		product.setDetail(detail);

		//상품 정보고시
		List<NoticeGroup> noticeGroupList = new ArrayList<NoticeGroup>();
		NoticeGroup noticeGroup = new NoticeGroup();
		noticeGroup.setGroupNoticeNo(paGoodsOfferList.get(0).getPaOfferType());  // 정책번호

		List<Notice> noticeList = new ArrayList<Notice>();
		// 고시항목
		for(PaGoodsOfferVO offerItem : paGoodsOfferList){
			Notice notice = new Notice();
			notice.setNoticeNo(offerItem.getPaOfferCode());
			notice.setDescription(offerItem.getPaOfferExt());
			noticeList.add(notice);
		}
		
		noticeGroup.setNoticeList(noticeList);
		noticeGroupList.add(noticeGroup);
		product.setNoticeList(noticeGroupList);
		
		Etc etc = new Etc();
		
		// 검색키워드(최대10개)
		String defaultKeyword = "sk스토아,SK스토아,SKstoa,skstoa,에스케이스토아"; 

		String[] keywords = paWempGoods.getGoodsName().split(",");
		StringBuilder searchTags = new StringBuilder();
		for(int i=0; i<keywords.length; i++){
			if (i == 5) break;
			searchTags.append(keywords[i] + ",");
		}
		etc.setKeywordWemakeprice(searchTags.toString() + defaultKeyword);
		etc.setParallelImportYn("N");  // 병행수입여부
		etc.setSellerProdCode(paWempGoods.getGoodsCode()); // 업체상품코드
		etc.setKcKidIsCertification("D");
		etc.setKcLifeIsCertification("D");
		etc.setKcElectricIsCertification("D");
		etc.setKcReportIsCertification("D");
		etc.setKcLifeChemistryIsCertification("D");
		etc.setPriceComparisonSiteYn("Y");
		if("1".equals(paWempGoods.getLuxuryYn())) {
			etc.setLuxuryYn("Y");
		} else {
			etc.setLuxuryYn("N");
		}
		product.setEtc(etc);

		return product;
	}

}
