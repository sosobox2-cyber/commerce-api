package com.cware.netshopping.pakakao.v2.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaKakaoGoodsVO;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaKakaoGoods;
import com.cware.netshopping.domain.model.PaKakaoGoodsImage;
import com.cware.netshopping.domain.model.PaKakaoTalkDeal;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.service.SyncProductService;
import com.cware.netshopping.pakakao.goods.service.PaKakaoGoodsService;
import com.cware.netshopping.pakakao.v2.domain.AnnouncementInfo;
import com.cware.netshopping.pakakao.v2.domain.Cert;
import com.cware.netshopping.pakakao.v2.domain.Combination;
import com.cware.netshopping.pakakao.v2.domain.CombinationAttribute;
import com.cware.netshopping.pakakao.v2.domain.CombinationName;
import com.cware.netshopping.pakakao.v2.domain.Delivery;
import com.cware.netshopping.pakakao.v2.domain.Discount;
import com.cware.netshopping.pakakao.v2.domain.GroupDiscountRequest;
import com.cware.netshopping.pakakao.v2.domain.ImageInfo;
import com.cware.netshopping.pakakao.v2.domain.Option;
import com.cware.netshopping.pakakao.v2.domain.Period;
import com.cware.netshopping.pakakao.v2.domain.ProductImage;
import com.cware.netshopping.pakakao.v2.domain.ProductOriginAreaInfo;
import com.cware.netshopping.pakakao.v2.domain.ProductRequest;
import com.cware.netshopping.pakakao.v2.repository.PaKakaoGoodsMapper;

@Service
public class PaKakaoProductService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;

	@Autowired
	@Qualifier("pakakao.goods.paKakaoGoodsService")
	private PaKakaoGoodsService paKakaoGoodsService;

	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;

	@Autowired
	PaKakaoProductApiService productApiService;

	@Autowired
	KakaoProductService kakaoProductService;

	@Autowired
	SyncProductService syncProductService;

	@Autowired
	PaKakaoGoodsMapper paKakaoGoodsMapper;

	@Autowired
	PaKakaoResultService paKakaoResultService;

	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;

	@Autowired
	TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 카카오 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public ResponseMsg transProduct(String goodsCode, String paCode, String procId) {

		ResponseMsg result = new ResponseMsg("", "");

		// 상품동기화
		PaGoodsSync sync = syncProductService.syncProduct(goodsCode, PaGroup.KAKAO.code(), procId);
		result = sync.getResponseMsg();
		if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) {
			return result;
		}

		// 스토아상품조회
		PaKakaoGoods paKakaoGoods = paKakaoGoodsMapper.getGoods(goodsCode, paCode);

		if (paKakaoGoods == null) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("연동 대상 상품이 아닙니다.");
			return result;
		}

		// 입점요청중인건은 처리하지 않음
		if (PaStatus.PROCEEDING.code().equals(paKakaoGoods.getPaStatus())) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("입점요청중인 상품입니다.");
			return result;
		}

		// 입점요청/입점반려건에 대해서 신규입점 요청
		if (PaStatus.REQUEST.code().equals(paKakaoGoods.getPaStatus())
				|| PaStatus.REJECT.code().equals(paKakaoGoods.getPaStatus())) {

			// 동기화에서 필터링된 경우
			if (String.valueOf(HttpStatus.NO_CONTENT.value()).equals(result.getCode())) {
				return result;
			}

			if (goodsTargetMapper.existsGoodsTarget(goodsCode, paCode, PaGroup.KAKAO.code()) < 1) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
				result.setMessage("입점 대상 상품이 아닙니다(타겟데이터 없음)");
				return result;
			}

			// 신규입점
			result = registerProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				result.setMessage("입점완료되었습니다.");
			} else if ("417".equals(result.getCode())) {
				// 이미지등록요청
				kakaoProductService.registerImageProduct(goodsCode, paCode, procId);
				// 이미지등록확인
				kakaoProductService.checkImageProduct(goodsCode, paCode, procId);
			}
			return result;
		}

		PaTransService transService;

		if ("1".equals(paKakaoGoods.getTransSaleYn()) && PaSaleGb.SUSPEND.code().equals(paKakaoGoods.getPaSaleGb())) {
			// 판매중지
			transService = kakaoProductService.stopSaleProduct(goodsCode, paCode, procId);
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

		if ("1".equals(paKakaoGoods.getTransTargetYn())) {
			// 상품수정
			ResponseMsg updated = updateProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(updated.getCode())) {
				isUpdated = true;
				result.setMessage("수정완료되었습니다.");
			} else {
				if ("417".equals(updated.getCode())) {
					// 이미지등록요청
					kakaoProductService.registerImageProduct(goodsCode, paCode, procId);
					// 이미지등록확인
					kakaoProductService.checkImageProduct(goodsCode, paCode, procId);
				}
				return updated;
			}
		} else {
			// 재고변경
			transService = kakaoProductService.updateStockProduct(goodsCode, paCode, procId);
			if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) {
				result.setMessage((isUpdated ? result.getMessage() + " " : "") + transService.getResultMsg());
				isStock = true;
			}
		}

		boolean isResume = false;
		if ("1".equals(paKakaoGoods.getTransSaleYn()) && PaSaleGb.FORSALE.code().equals(paKakaoGoods.getPaSaleGb())) {
			// 판매재개
			transService = kakaoProductService.resumeSaleProduct(goodsCode, paCode, procId);
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
	 * 카카오 신규상품 입점
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
		serviceLog.setServiceNote("[API]카카오-상품입점");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.KAKAO.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 상품입점
		ResponseMsg result = callRegisterProduct(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		// 입점요청중 해제
		paKakaoGoodsMapper.updateClearProceeding(goodsCode, paCode, procId);

		return result;

	}

	/**
	 * 카카오 상품 수정
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
		serviceLog.setServiceNote("[API]카카오-상품수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.KAKAO.code());
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
	 * 카카오 상품 조회
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ProductRequest getProduct(String goodsCode, String productNo, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]카카오-상품조회");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.KAKAO.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		ProductRequest product = null;

		try {
			// 상품조회
			product = productApiService.getProduct(goodsCode, productNo, paCode, serviceLog.getTransServiceNo());
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(PaKakaoApiRequest.API_SUCCESS_CODE);

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
		
		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 상품등록서비스 Start - {} =====", goodsCode);
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
			paramMap.put("modCase", "INSERT");
			PaKakaoGoodsVO paKakaoGoods = paKakaoGoodsService.selectPaKakaoGoodsInfo(paramMap);

			if (paKakaoGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paKakaoGoods.getProductId() != null) {
				result.setCode("411");
				result.setMessage("이미 등록된 상품입니다. 카카오 상품코드 : " + paKakaoGoods.getProductId());
				return result;
			}
			if (paKakaoGoods.getDescribeExt() == null) {
				paKakaoGoods.setDescribeExt("");
				if (paKakaoGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", paKakaoGoods.getPaGroupCode());

			// 정보고시 조회
			List<PaGoodsOfferVO> goodsOffer = paCommonService.selectPaGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.KAKAO.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			List<PaKakaoGoodsImage> imageList = paKakaoGoodsMapper.getImageList(goodsCode);
			if (imageList.size() < 1) {
				result.setCode("417");
				result.setMessage("상품이미지 업로드가 완료되지 않았습니다. 잠시 후 재시도해주세요.");
				return result;
			}

			// 입점요청중
			paKakaoGoodsMapper.updateProceeding(goodsCode, paCode, procId);

			// 상품설명설정
			settingDescribeExt(paKakaoGoods);

			// 단품 옵션 조회
			List<PaGoodsdtMapping> goodsdtMapping = paKakaoGoodsService.selectPaGoodsdtInfoList(paramMap);

			paKakaoGoods.setModifyId(procId);

			// 카카오 상품 전문
			ProductRequest product = createProduct(paKakaoGoods, goodsdtMapping, goodsOffer, imageList,
					goodsPrice, null, transServiceNo);

			log.info("상품등록 API 호출 {}", goodsCode);
			String productId = productApiService.registerProduct(goodsCode, product, paCode, transServiceNo);

			if (StringUtils.hasText(productId)) {

				result.setCode("200");
				result.setMessage(PaKakaoApiRequest.API_SUCCESS_CODE);

				paKakaoGoods.setProductId(productId);

				rtnMsg = paKakaoResultService.saveTransProduct(paKakaoGoods, goodsPrice, goodsdtMapping);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + paKakaoGoods.getGoodsCode());
				}

			} else {
				result.setCode("500");
				result.setMessage("no data found");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

			paKakaoGoodsMapper.rejectTransTarget(goodsCode, paCode, procId,
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
		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 상품수정서비스 Start - {} =====", goodsCode);
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
			paramMap.put("modCase", "MODIFY");
			PaKakaoGoodsVO paKakaoGoods = paKakaoGoodsService.selectPaKakaoGoodsInfo(paramMap);

			if (paKakaoGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paKakaoGoods.getProductId() == null) {
				result.setCode("411");
				result.setMessage("등록되지 않은 상품입니다. 상품코드 : " + goodsCode);
				return result;
			}
			if (paKakaoGoods.getDescribeExt() == null) {
				paKakaoGoods.setDescribeExt("");
				if (paKakaoGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", paKakaoGoods.getPaGroupCode());

			// 정보고시 조회
			List<PaGoodsOfferVO> goodsOffer = paCommonService.selectPaGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.KAKAO.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			List<PaKakaoGoodsImage> imageList = paKakaoGoodsMapper.getImageList(goodsCode);
			if (imageList.size() < 1) {
				result.setCode("417");
				result.setMessage("상품이미지 업로드가 완료되지 않았습니다. 잠시 후 재시도해주세요.");
				return result;
			}
			
			// 상품설명설정
			settingDescribeExt(paKakaoGoods);

			// 단품 옵션 조회
			List<PaGoodsdtMapping> goodsdtMapping = paKakaoGoodsService.selectPaGoodsdtInfoList(paramMap);
			
			// 톡딜 조회
			PaKakaoTalkDeal dealData = paKakaoGoodsService.selectPaKakaoDealInfo(paramMap);

			paKakaoGoods.setModifyId(procId);

			// 카카오 상품 전문
			ProductRequest product = createProduct(paKakaoGoods, goodsdtMapping, goodsOffer, imageList, goodsPrice,
					dealData, transServiceNo);
			if (product == null) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
				return result;
			}

			log.info("상품수정 API 호출 {}", goodsCode);
			String productId = productApiService.updateProduct(goodsCode, product, paCode, transServiceNo);

			if (StringUtils.hasText(productId)) {

				result.setCode("200");
				result.setMessage(PaKakaoApiRequest.API_SUCCESS_CODE);

				paKakaoGoods.setProductId(productId);

				rtnMsg = paKakaoResultService.saveTransProduct(paKakaoGoods, goodsPrice, goodsdtMapping);
				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + paKakaoGoods.getGoodsCode());
				}
				
				// 톡딜 업데이트
				if(dealData != null) {
					dealData.setModifyId(procId);
					paKakaoGoodsMapper.updateTalkDealBDate(goodsCode, dealData.getPromoNo(), procId);
					paKakaoGoodsMapper.stopTalkDealGoods(goodsCode, paCode, procId, "톡딜대상상품");
					// 톡딜 ID 조회
					if(dealData.getDealId() == null) {
						ProductRequest productInfo = productApiService.getProduct(goodsCode, productId, paCode, transServiceNo);
						paKakaoGoodsMapper.updateTalkDealId(goodsCode, productInfo.getGroupDiscount().getId().toString(), dealData.getPromoNo(), procId);
					}
				} else {
					paKakaoGoodsMapper.updateTalkDealEDate(goodsCode, procId);
				}

			} else {
				result.setCode("500");
				result.setMessage("no data found");
			}
		} catch (TransApiException ex) {

			// 카카오에서 리텐션 적용될때 오류메시지 확인 후 적용
			// 리텐션정책: 최근 1년간 상품 정보 업데이트 이력이 없고, 주문내역도 없는 ‘휴면상품’
//			if (paKakaoResultService.applyRetention(goodsCode, paCode, procId, ex.getMessage(), ex.getCode())) {
//				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
//				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
//				return result;
//			}
			
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단
				paKakaoGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
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

		String[] rejectMatch = new String[] { "제한된 단어"
				, "자 이내로 입력"
				, "입력이 불가능한 문자" 
				, "중복된 옵션"
				, "최대 100개"
				, "기본배송비 이상 금액"
				, "배송비 최대"
				, "스토어에 유사한 상품이 이미 등록"
				};

		return Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s));
	}

	/**
	 * 기술서와 공지사항 설정
	 * 
	 * @param paKakaoGoods
	 */
	private void settingDescribeExt(PaKakaoGoodsVO paKakaoGoods) {

		// 상품구성템플릿
		String goodsCom = StringUtils.hasText(paKakaoGoods.getGoodsCom())
				? ("<div style='line-height: 2.0em; font-family: NanumBarunGothic; font-size: 19px;'><div><h4>&middot;&nbsp;상품구성<h4><pre>"
						+ paKakaoGoods.getGoodsCom() + "</pre></div></div>")
				: "";

		// 웹기술서
		paKakaoGoods.setDescribeExt(
				"<div align='center'><img alt='' src='" + paKakaoGoods.getTopImage() + "' /><br /><br /><br />" // 상단이미지
						+ (StringUtils.hasText(paKakaoGoods.getCollectImage())
								? "<img alt='' src='" + paKakaoGoods.getCollectImage() + "' /><br /><br /><br />"
								: "") // 착불이미지
						+ goodsCom // 상품구성
						+ paKakaoGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />" // 기술서
						+ "<img alt='' src='" + paKakaoGoods.getBottomImage() + "' /></div>"); // 하단이미지

		// 제휴 공지사항
		if (StringUtils.hasText(paKakaoGoods.getNoticeExt())) {
			paKakaoGoods.setDescribeExt(
					paKakaoGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + paKakaoGoods.getDescribeExt());
		}
	}

	private ProductRequest createProduct(PaKakaoGoodsVO paKakaoGoods, List<PaGoodsdtMapping> paGoodsDtMappingList,
			List<PaGoodsOfferVO> paGoodsOfferList, List<PaKakaoGoodsImage> imageList, 
			PaGoodsPriceApply goodsPrice, PaKakaoTalkDeal dealData, long transServiceNo) throws Exception {

		ProductRequest product = new ProductRequest();

		if (paKakaoGoods.getProductId() != null) {
			product.setProductId(paKakaoGoods.getProductId());
		}
		
		product.setCategoryId(paKakaoGoods.getCategoryId());
		product.setName(paKakaoGoods.getGoodsName());
		product.setBrand(paKakaoGoods.getBrand());
		product.setManufacturer(paKakaoGoods.getManufacture());

		// 상세설명
		product.setProductDetailDescription(paKakaoGoods.getDescribeExt());
		
		product.setTaxType("1".equals(paKakaoGoods.getTaxYn()) ? "TAX" : "DUTYFREE");
		product.setSalePrice(new BigDecimal(goodsPrice.getSalePrice()));
		product.setUseSalePeriod(false);
		product.setProductCondition("NEW");
		product.setPlusFriendSubscriberExclusive(false);

		// 카카오 최대 구매수량 1개 이상 1000개 이하
		if (1 <= paKakaoGoods.getOrderMaxQty()) {
			product.setMaxPurchaseQuantity(
					paKakaoGoods.getOrderMaxQty() <= 1000 ? (int) paKakaoGoods.getOrderMaxQty() : 1000);
		}
		
		// 카카오 최소 구매수량 2개이상 1000개이하(기본 1개)
		if (2 <= paKakaoGoods.getOrderMinQty()) {
			product.setMinPurchaseQuantity(
					paKakaoGoods.getOrderMinQty() <= 1000 ? (int) paKakaoGoods.getOrderMinQty() : 1000);
		}
		
		product.setStoreManagementCode(paKakaoGoods.getPaCode() + paKakaoGoods.getGoodsCode());
		product.setDisplayStatus("OPEN");
		product.setShoppingHowDisplayable(true);
		product.setTalkDisplayable(true);

		ProductOriginAreaInfo originInfo = new ProductOriginAreaInfo();
		
		// 국내산
		if("00".equals(paKakaoGoods.getProductOrginAreaInfo())) {
			if("60".equals(paKakaoGoods.getLgroup())) {	// 식품 카테고리 경우 혼합/기타 & 상세설명참조로 연동
				originInfo.setOriginAreaType("USER_INPUT");
				originInfo.setOriginAreaContent("상세설명참조");
			} else {
				originInfo.setOriginAreaType("LOCAL");
			}
		}else if("03".equals(paKakaoGoods.getProductOrginAreaInfo())) { // 혼합/기타
			originInfo.setOriginAreaType("USER_INPUT");
			originInfo.setOriginAreaContent("상세설명참조");
		}else {
			originInfo.setOriginAreaType("IMPORT");
			originInfo.setOriginAreaCode(paKakaoGoods.getProductOrginAreaInfo());
		}
		product.setProductOriginAreaInfo(originInfo);
		
		// 상품이미지
		ImageInfo representImage = new ImageInfo();
		List<ImageInfo> optionalImages = new ArrayList<ImageInfo>();
		
		for (PaKakaoGoodsImage image : imageList) {
			if("P".equals(image.getImageGb())) {
				representImage.setUrl(image.getKakaoImage());
			}else {
				ImageInfo optionImage = new ImageInfo();
				optionImage.setUrl(image.getKakaoImage());
				optionalImages.add(optionImage);
			}
		}
		ProductImage productImage = new ProductImage();
		productImage.setImageRatio("SQUARE");
		productImage.setRepresentImage(representImage);
		if (optionalImages.size() > 0) productImage.setOptionalImages(optionalImages);
		product.setProductImage(productImage);
		
		// 상품 정보고시
		AnnouncementInfo announcementInfo = new AnnouncementInfo();
		announcementInfo.setAnnouncementType(paGoodsOfferList.get(0).getPaOfferType());

		Map<String, Object> announcement = new HashMap<String, Object>();
		for(PaGoodsOfferVO offerItem : paGoodsOfferList){
			if("consumerServicePhoneNumber".equals(offerItem.getPaOfferCode())) {
				//전화번호 양식으로 넣어야 되서 SK스토아 고객센터 번호 입력처리
				announcement.put(offerItem.getPaOfferCode(), "1670-0694");
			} else {
				announcement.put(offerItem.getPaOfferCode(), offerItem.getPaOfferExt());
			}
		}
		announcementInfo.setAnnouncement(announcement);
		product.setAnnouncementInfo(announcementInfo);
		
		// 할인정보 설정
		Discount discount = new Discount();
		double dcAmt = goodsPrice.getSalePrice() - goodsPrice.getBestPrice(); 
		if ( dealData != null ) {
			GroupDiscountRequest groupDiscount = new GroupDiscountRequest();
			Period period = new Period();
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			double totalDiscount = goodsPrice.getArsDcAmt() + goodsPrice.getLumpSumDcAmt() + dealData.getPrice();
			
			// 톡딜 종료일시 1초 빼줘야 카카오 적용 시 프로모션 기간과 동일
			Calendar to = Calendar.getInstance();
			to.setTime(dealData.getDealEdate());
			to.add(Calendar.SECOND , -1);
			
			period.setFrom(transFormat.format(dealData.getDealBdate()));
			period.setTo(transFormat.format(to.getTime()));
			
			groupDiscount.setUseGroupDiscount(true);
			groupDiscount.setPrice(new BigDecimal(totalDiscount));
			groupDiscount.setUseStock(false);
			groupDiscount.setPeriod(period);
			// 톡딜 수정 시 필수
			if(dealData.getDealId() != null) {
				groupDiscount.setId(Long.parseLong(dealData.getDealId()));
			}
			
			product.setGroupDiscount(groupDiscount);
			
		} else if(dcAmt > 0) {
			discount.setType("PRICE");
			discount.setValue((int)dcAmt);
			discount.setUseDiscount(true);
		} else {
			discount.setUseDiscount(false);
		}
		product.setDiscount(discount);
		
		// 배송비 정보
		Delivery delivery = new Delivery();
		
		delivery.setDeliveryMethodType("DELIVERY");

		double returnCost = paKakaoGoods.getReturnCost();
		
		switch (paKakaoGoods.getShipCostCode().substring(0, 2)) {
		case "FR": // 무료
			delivery.setDeliveryFeeType("FREE");
			delivery.setBundleGroupAvailable(true);
			returnCost = returnCost > 0 ? returnCost / 2 : 0; // 무료배송은 왕복배송비 설정되므로 편도로 계산
			break;
		case "CN":
		case "PL": // 조건부
			delivery.setDeliveryFeeType("CONDITIONAL_FREE");
			delivery.setFreeConditionalAmount(new BigDecimal(paKakaoGoods.getShipCostBaseAmt()));
			delivery.setBundleGroupAvailable(true);
			break;
		default: // 상품별
			delivery.setDeliveryFeeType("PAID");
			delivery.setBundleGroupAvailable(false);
			break;
		}
		delivery.setBaseFee(new BigDecimal(paKakaoGoods.getOrdCost()));
		delivery.setDeliveryFeePaymentType("PREPAID");

		// 반품,교환 배송비 0원 처리. 카카오에서 결제금액에서 차감되게 개발 되면 금액 세팅 예정
		// 카카오 측 반품비 결제 개발 완료로 인한 반품, 교환비 연동(22.11.30)
		// 카카오에서 12/2 기능 원복으로 0원으로 다시 원복 (22.12.5)
		// 연동구조개선 오픈시 반품/교환비 연동 (22.12.15)
		// 운영 테스트시 "환불금에서 차감" 버튼이 노출되지 않아 카카오측에 문의, 오픈시 연동하지 않도록 함. (22.12.15)
		// 카카오 측 반품비 결제 운영 반영 완료로 인한 반품, 교환비 연동(22.12.22)
		// 판매자에게 직접 송금 노출되어 다시 주석처리(22.12.22)
		// 반품, 교환 배송비 연동(22.12.28)
		delivery.setReturnDeliveryFee(new BigDecimal(returnCost));
		delivery.setExchangeDeliveryFee(new BigDecimal(paKakaoGoods.getChangeCost()));
//		delivery.setReturnDeliveryFee(new BigDecimal(0));
//		delivery.setExchangeDeliveryFee(new BigDecimal(0));

		delivery.setShippingAddressId(paKakaoGoods.getShippingAddressId());
		delivery.setReturnAddressId(paKakaoGoods.getReturnAddressId());
		delivery.setUsePickUpDelivery(false);
		delivery.setUseQuickDelivery(false);
		delivery.setAsPhoneNumber(paKakaoGoods.getAsTelNo());
		delivery.setAsGuideWords("상세설명참조");

		if ("1".equals(paKakaoGoods.getNoIslandYn())) {
			delivery.setAvailableIsolatedArea(false); // 도서산간 배송 가능 여부
			delivery.setUseIsolatedAreaFee(false);
		} else {
			delivery.setAvailableIsolatedArea(true);

			// 2022 05 13 추가배송비 0원도 연동 가능하여 배송비 유무 상관없이 배송비 지정여부 true 세팅
			delivery.setUseIsolatedAreaFee(true); // 도서산간 추가 배송비 지정 여부
			delivery.setJejuAreaAdditionalFee(new BigDecimal(paKakaoGoods.getAddJejuCost()));
			delivery.setIsolatedAreaAdditionalFee(new BigDecimal(paKakaoGoods.getAddIslandCost()));
		}
		product.setDelivery(delivery);

		// 옵션
		Option option = new Option();
		
		option.setType("COMBINATION");
		List<Combination> combinations = new ArrayList<Combination>();
		List<CombinationAttribute> combinationAttributes = new ArrayList<CombinationAttribute>();

		int MAX_STOCK = 99_999_999;
		for(PaGoodsdtMapping optionItem : paGoodsDtMappingList){
			// 신규입점시에만 옵션 재고가 없으면 반려되기 때문에 재고 없으면 보내지 않음
			// 수정시에는 판매중이면 재고가 없어도 연동하여 품절로 표시되도록 함.
			if (paKakaoGoods.getProductId() != null || Integer.parseInt(optionItem.getTransOrderAbleQty()) > 0) {
				CombinationName combinationName = new CombinationName();
				combinationName.setKey("색상/크기/무늬/형태");
				combinationName.setValue(optionItem.getGoodsdtInfo()); 
	
				Combination combination = new Combination();
				combination.setName(Arrays.asList(combinationName));
				combination.setPrice(new BigDecimal(0));			
				combination.setStockQuantity(Integer.parseInt(optionItem.getTransOrderAbleQty()) > MAX_STOCK ? MAX_STOCK : 
					Integer.parseInt(optionItem.getTransOrderAbleQty()));
				combination.setManagedCode(optionItem.getPaOptionCode());
				combination.setUsable(SaleGb.FORSALE.code().equals(optionItem.getSaleGb()) ? true : false); 
				combinations.add(combination);
				
				CombinationAttribute combinationAttribute = new CombinationAttribute();
				combinationAttribute.setName("색상/크기/무늬/형태");
				combinationAttribute.setValue(optionItem.getGoodsdtInfo()); 
				combinationAttributes.add(combinationAttribute);
			}
		}
		option.setCombinations(combinations);
		option.setCombinationAttributes(combinationAttributes);
		product.setOption(option);

		//인증정보
		if("1".equals(paKakaoGoods.getCertYn())) {
			Cert cert = new Cert();
			 if(paKakaoGoods.getCategoryId().matches("100100102106|100100102100")) {
					cert.setCertType("KC_5");
					cert.setCertCode(paKakaoGoods.getKcInfo());
				} else {
					cert.setCertType("DETAIL_REF");
				}
			 product.setCerts(Arrays.asList(cert));
		}

		// 미성년자 구매가능 여부
		product.setMinorPurchasable("1".equals(paKakaoGoods.getAdultYn()) ? false : true);
		
		return product;
	}

}
