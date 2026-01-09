package com.cware.netshopping.palton.v2.service;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.PaLtonGoodsVO;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.service.SyncProductService;
import com.cware.netshopping.palton.goods.service.PaLtonGoodsService;
import com.cware.netshopping.palton.v2.domain.Dcat;
import com.cware.netshopping.palton.v2.domain.Epn;
import com.cware.netshopping.palton.v2.domain.Itm;
import com.cware.netshopping.palton.v2.domain.ItmImg;
import com.cware.netshopping.palton.v2.domain.ItmOpt;
import com.cware.netshopping.palton.v2.domain.PdItmsArtl;
import com.cware.netshopping.palton.v2.domain.PdItmsInfo;
import com.cware.netshopping.palton.v2.domain.Product;
import com.cware.netshopping.palton.v2.domain.PurPsbQtyInfo;
import com.cware.netshopping.palton.v2.domain.SftyAthn;
import com.cware.netshopping.palton.v2.domain.SndBgtDdInfo;
import com.cware.netshopping.palton.v2.repository.PaLtonGoodsMapper;

@Service
public class PaLtonProductService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;

	@Autowired
	@Qualifier("palton.goods.paLtonGoodsService")
	private PaLtonGoodsService paLtonGoodsService;

	@Autowired
	PaLtonProductApiService productApiService;

	@Autowired
	LotteonProductService lotteonProductService;

	@Autowired
	SyncProductService syncProductService;

	@Autowired
	PaLtonGoodsMapper paLtonGoodsMapper;

	@Autowired
	PaLtonResultService paLtonResultService;

	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;

	@Autowired
	TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 롯데온 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public ResponseMsg transProduct(String goodsCode, String paCode, String procId) {

		ResponseMsg result = new ResponseMsg("", "");

		// 상품동기화
		PaGoodsSync sync = syncProductService.syncProduct(goodsCode, PaGroup.LOTTEON.code(), procId);
		result = sync.getResponseMsg();
		if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) {
			return result;
		}

		// 스토아상품조회
		PaLtonGoodsVO paLtonGoods = paLtonGoodsMapper.getGoods(goodsCode, paCode);

		if (paLtonGoods == null) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("연동 대상 상품이 아닙니다.");
			return result;
		}

		// 입점요청중인건은 처리하지 않음
		if (PaStatus.PROCEEDING.code().equals(paLtonGoods.getPaStatus())) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("입점요청중인 상품입니다.");
			return result;
		}

		// 입점요청/입점반려건에 대해서 신규입점 요청
		if (PaStatus.REQUEST.code().equals(paLtonGoods.getPaStatus())
				|| PaStatus.REJECT.code().equals(paLtonGoods.getPaStatus())) {

			// 동기화에서 필터링된 경우
			if (String.valueOf(HttpStatus.NO_CONTENT.value()).equals(result.getCode())) {
				return result;
			}

			if (goodsTargetMapper.existsGoodsTarget(goodsCode, paCode, PaGroup.LOTTEON.code()) < 1) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
				result.setMessage("입점 대상 상품이 아닙니다(타겟데이터 없음)");
				return result;
			}

			// 신규입점
			result = registerProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				// 상품상태업데이트
				lotteonProductService.updateStatusProduct(goodsCode, paCode, procId);
				result.setMessage("입점완료되었습니다.");
			}
			return result;
		}

		PaTransService transService;
		boolean isMapping = false;

		// 상품상태업데이트
		if (!"30".equals(paLtonGoods.getApprovalStatus())) {
			transService = lotteonProductService.updateStatusProduct(goodsCode, paCode, procId);
			isMapping = String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode());
			result.setMessage(transService.getResultMsg());
		} else {
            transService = lotteonProductService.updateOptionProduct(goodsCode,
                    paLtonGoods.getSpdNo(), paCode, procId);
            isMapping = String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode());
            result.setMessage(transService.getResultMsg());
		}
		
		if ("1".equals(paLtonGoods.getTransSaleYn()) && PaSaleGb.SUSPEND.code().equals(paLtonGoods.getPaSaleGb())) {
			// 판매중지
			transService = lotteonProductService.stopSaleProduct(goodsCode, paCode, procId);
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

		if ("1".equals(paLtonGoods.getTransTargetYn())) {
			// 상품수정
			ResponseMsg updated = updateProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(updated.getCode())) {
				isUpdated = true;
				// 옵션코드매핑
				lotteonProductService.updateOptionProduct(goodsCode, paLtonGoods.getSpdNo(), paCode, procId);
				result.setMessage("수정완료되었습니다.");
			} else
				return updated;
		} else {
			// 재고변경
			transService = lotteonProductService.updateStockProduct(goodsCode, paCode, procId);
			if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) {
				result.setMessage((isUpdated ? result.getMessage() + " " : "") + transService.getResultMsg());
				isStock = true;
			}
		}

		// 단품판매재개
		boolean isResumeItem = false;
		transService = lotteonProductService.resumeSaleItem(goodsCode, paCode, procId);
		if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) {
			result.setMessage((isStock || isUpdated ? result.getMessage() + " " : "") + transService.getResultMsg());
			isResumeItem = true;
		}

		boolean isResume = false;
		if ("1".equals(paLtonGoods.getTransSaleYn()) && PaSaleGb.FORSALE.code().equals(paLtonGoods.getPaSaleGb())) {
			// 판매재개
			transService = lotteonProductService.resumeSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				result.setMessage("판매재개실패: " + transService.getResultMsg());
				return result;
			}
			if ("1".equals(transService.getSuccessYn())) {
				isResume = true;
				result.setMessage(
						(isStock || isUpdated || isResumeItem ? result.getMessage() + " " : "") + "판매재개되었습니다.");
			}
		}

		if (isStock || isUpdated || isResumeItem || isResume || isMapping) { // 상품/재고가 변경된 경우
			result.setCode(String.valueOf(HttpStatus.OK.value()));
		} else {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
			result.setMessage("변경된 사항이 없습니다.");
		}

		return result;
	}

	/**
	 * 롯데온 신규상품 입점
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
		serviceLog.setServiceNote("[API]롯데온-상품입점");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.LOTTEON.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 상품입점
		ResponseMsg result = callRegisterProduct(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		// 입점요청중 해제
		paLtonGoodsMapper.updateClearProceeding(goodsCode, paCode, procId);

		return result;

	}

	/**
	 * 롯데온 상품 수정
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
		serviceLog.setServiceNote("[API]롯데온-상품수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.LOTTEON.code());
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
	 * 롯데온 상품 조회
	 * 
	 * @param goodsCode
	 * @param spdNo
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public Product getProduct(String goodsCode, String spdNo, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]롯데온-상품조회");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.LOTTEON.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		Product product = null;

		try {
			// 상품조회
			product = productApiService.getProduct(goodsCode, spdNo, paCode, serviceLog.getTransServiceNo());
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(PaLtonApiRequest.API_SUCCESS_MSG);

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

		PaLtonGoodsVO paLtonGoods = null;

		try {
			log.info("===== 상품등록서비스 Start - {} =====", goodsCode);

			dateTime = systemService.getSysdatetimeToString();

			paramMap.put("dateTime", dateTime);
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
			paramMap.put("modCase", "INSERT");
			paLtonGoods = paLtonGoodsService.selectPaLtonGoodsInfo(paramMap);

			if (paLtonGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paLtonGoods.getSpdNo() != null) {
				result.setCode("411");
				result.setMessage("이미 등록된 상품입니다. 롯데온 상품코드 : " + paLtonGoods.getSpdNo());
				return result;
			}
			if (paLtonGoods.getDescribeExt() == null) {
				paLtonGoods.setDescribeExt("");
				if (paLtonGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", paLtonGoods.getPaGroupCode());

			// 정보고시 조회
			List<PaGoodsOffer> goodsOffer = paLtonGoodsService.selectPaLtonGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.LOTTEON.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 입점요청중
			paLtonGoodsMapper.updateProceeding(goodsCode, paCode, procId);

			// 상품설명설정
			settingDescribeExt(paLtonGoods);

			// 단품 옵션 조회
			List<PaLtonGoodsdtMappingVO> goodsdtMapping = paLtonGoodsService.selectPaLtonGoodsdtInfoList(paramMap);

			paLtonGoods.setModifyId(procId);

			// 롯데온 상품 전문
			Product product = createProduct(paLtonGoods, goodsdtMapping, goodsOffer, goodsPrice, transServiceNo);

			log.info("상품등록 API 호출 {}", goodsCode);
			String spdNo = productApiService.registerProduct(goodsCode, product, paCode, transServiceNo);

			if (StringUtils.hasText(spdNo)) {

				result.setCode("200");
				result.setMessage(PaLtonApiRequest.API_SUCCESS_MSG);

				paLtonGoods.setSpdNo(spdNo);

				rtnMsg = paLtonResultService.saveTransProduct(paLtonGoods, goodsPrice, goodsdtMapping);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + paLtonGoods.getGoodsCode());
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
                paLtonGoodsMapper.rejectTransTarget(goodsCode, paCode, procId,
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

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		PaLtonGoodsVO paLtonGoods = null;

		try {
			log.info("===== 상품수정서비스 Start - {} =====", goodsCode);

			dateTime = systemService.getSysdatetimeToString();

			paramMap.put("dateTime", dateTime);
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
			paramMap.put("modCase", "MODIFY");
			paLtonGoods = paLtonGoodsService.selectPaLtonGoodsInfo(paramMap);

			if (paLtonGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paLtonGoods.getSpdNo() == null) {
				result.setCode("411");
				result.setMessage("등록되지 않은 상품입니다. 상품코드 : " + goodsCode);
				return result;
			}
			if (paLtonGoods.getDescribeExt() == null) {
				paLtonGoods.setDescribeExt("");
				if (paLtonGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", paLtonGoods.getPaGroupCode());

			// 정보고시 조회
			List<PaGoodsOffer> goodsOffer = paLtonGoodsService.selectPaLtonGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.LOTTEON.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 상품설명설정
			settingDescribeExt(paLtonGoods);

			// 단품 옵션 조회
			List<PaLtonGoodsdtMappingVO> goodsdtMapping = paLtonGoodsService.selectPaLtonGoodsdtInfoList(paramMap);

			paLtonGoods.setModifyId(procId);

			// 롯데온 상품 전문
			Product product = createProduct(paLtonGoods, goodsdtMapping, goodsOffer, goodsPrice, transServiceNo);
			if (product == null) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
				return result;
			}

			log.info("상품수정 API 호출 {}", goodsCode);
			String spdNo = productApiService.updateProduct(goodsCode, product, paCode, transServiceNo);

			if (StringUtils.hasText(spdNo)) {

				result.setCode("200");
				result.setMessage(PaLtonApiRequest.API_SUCCESS_MSG);

				paLtonGoods.setSpdNo(spdNo);

				rtnMsg = paLtonResultService.saveTransProduct(paLtonGoods, goodsPrice, goodsdtMapping);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + paLtonGoods.getGoodsCode());
				}

			} else {
				result.setCode("500");
				result.setMessage("상품 수정에 실패했습니다.");
			}
		} catch (TransApiException ex) {
		    
            if (paLtonResultService.applyCategoryMapping(paLtonGoods, procId, ex.getMessage())) {
                result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                result.setMessage("표준카테고리가 변경되어 갱신하였습니다. 재연동해주세요.");
                return result;
            }

            if (paLtonResultService.applyResetBrand(goodsCode, paCode, procId, ex.getMessage())) {
                result.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                result.setMessage("전시카테고리에 등록이 불가한 브랜드를 제거하였습니다. 재연동해주세요.");
                return result;
            }
		    
//			if (paLtonResultService.applyRetention(goodsCode, paCode, procId, ex.getMessage())) {
//				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
//				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
//				return result;
//			}

			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단
				paLtonGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
						StringUtil.truncate(result.getMessage(), 500));
			} else if (result.getMessage() != null
					&& result.getMessage().contains("마지막으로 연동했던 API 전문과 동일한 전문을 수정 요청하였습니다")) {
				// 상품전송 타겟팅 리셋
				paLtonGoodsMapper.resetTransTarget(goodsCode, paCode, procId,
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

    /**
     * 반려제외여부
     * 
     * @param resultCode
     * @param rejectMsg
     * @return
     */
    private boolean isNotRejectMsg(String resultCode, String rejectMsg) {

        if (!StringUtils.hasText(rejectMsg) && !"Exception".equals(resultCode) && !"9999".equals(resultCode))
            return false;

        String[] rejectNotMatch =
                new String[] {"Read timed out", "데이타 등록 중 오류가 발생"};

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

		String[] rejectMatch = new String[] { "해외배송 사용 셀러만"
				, "판매옵션이 중복되었습니다"
				, "금칙어"
				, "19세이상"
				};

		return Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s));
	}

	/**
	 * 기술서와 공지사항 설정
	 * 
	 * @param paLtonGoods
	 */
	private void settingDescribeExt(PaLtonGoodsVO paLtonGoods) {

		// 상품구성템플릿
		String goodsCom = StringUtils.hasText(paLtonGoods.getGoodsCom())
				? ("<div style='line-height: 2.0em; font-family: NanumBarunGothic; font-size: 19px;'><div><h4>&middot;&nbsp;상품구성<h4><pre>"
						+ paLtonGoods.getGoodsCom() + "</pre></div></div>")
				: "";

		// 웹기술서
		paLtonGoods.setDescribeExt(
				"<div align='center'><img alt='' src='" + paLtonGoods.getTopImage() + "' /><br /><br /><br />" // 상단이미지
						+ (StringUtils.hasText(paLtonGoods.getCollectImage())
								? "<img alt='' src='" + paLtonGoods.getCollectImage() + "' /><br /><br /><br />"
								: "") // 착불이미지
						+ goodsCom // 상품구성
						+ paLtonGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />" // 기술서
						+ "<img alt='' src='" + paLtonGoods.getBottomImage() + "' /></div>"); // 하단이미지

		// 제휴 공지사항
		if (StringUtils.hasText(paLtonGoods.getNoticeExt())) {
			paLtonGoods.setDescribeExt(
					paLtonGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + paLtonGoods.getDescribeExt());
		}
	}

	private Product createProduct(PaLtonGoodsVO paLtonGoods, List<PaLtonGoodsdtMappingVO> paGoodsDtMappingList,
			List<PaGoodsOffer> paGoodsOfferList, PaGoodsPriceApply goodsPrice, long transServiceNo) throws Exception {

		Product product = new Product();

		if (paLtonGoods.getSpdNo() != null) {
			product.setSpdNo(paLtonGoods.getSpdNo());
		} 

		product.setScatNo(paLtonGoods.getScatNo());
		
		List<Dcat> dcatLst = new ArrayList<Dcat>();		
		
		String[] LfdCatNoArray = Optional.ofNullable(paLtonGoods.getLfDcatNo())
		        .filter(s -> !s.isEmpty())
		        .map(s -> s.split(","))
		        .orElse(new String[0]);
		 
		for(String lfDcatNo : LfdCatNoArray) {
			Dcat dcat = new Dcat();
			dcat.setMallCd("LTON"); //몰구분코드
			dcat.setLfDcatNo(lfDcatNo); //leaf전시카테고리번호
			dcatLst.add(dcat);
		}
		product.setDcatLst(dcatLst);
		
		product.setEpdNo(paLtonGoods.getEpdNo()); // 상품코드

		product.setSlTypCd("GNRL"); // 판매유형코드
		product.setPdTypCd("GNRL_GNRL"); // 상품유형코드

		product.setBrdNo(paLtonGoods.getBrdNo() == null ? "" : paLtonGoods.getBrdNo()); // 브랜드번호
		product.setMfcrNm(paLtonGoods.getMfcrNm()); // 제조사명
		product.setOplcCd(paLtonGoods.getOplcCd()); // 원산지코드
		product.setTdfDvsCd(paLtonGoods.getTaxYn().equals("1") ? "01" : "02"); // 과세유형코드
		product.setSlStrtDttm(paLtonGoods.getSlStrtDttm()); // 판매시작일시
		product.setSlEndDttm(paLtonGoods.getSlEndDttm()); // 판매종료일시

		//상품 정보고시
		PdItmsInfo pdItmsInfo = new PdItmsInfo();
		pdItmsInfo.setPdItmsCd(paGoodsOfferList.get(0).getPaOfferType()); // 상품품목코드 
		// 고시항목
		List<PdItmsArtl> pdItmsArtlLst = new ArrayList<PdItmsArtl>();
		String dtlRefc = ""; //상세참조
		for(PaGoodsOffer offerItem : paGoodsOfferList){
			PdItmsArtl pdItmsArtl = new PdItmsArtl();
			pdItmsArtl.setPdArtlCd(offerItem.getPaOfferCode());
			if("0200".equals(offerItem.getPaOfferCode())){
				dtlRefc = "Y";
				pdItmsArtl.setPdArtlCnts(dtlRefc); // 상품항목내용
			}else {
				pdItmsArtl.setPdArtlCnts(offerItem.getPaOfferExt()); // 상품항목내용
			}
			pdItmsArtlLst.add(pdItmsArtl);
		}
		pdItmsInfo.setPdItmsArtlLst(pdItmsArtlLst); // 상품품목항목목록
		product.setPdItmsInfo(pdItmsInfo);

		if("Y".equals(dtlRefc)) {
			List<SftyAthn> sftyAthnLst = new ArrayList<SftyAthn>();
			SftyAthn sftyAthn = new SftyAthn();
			sftyAthn.setSftyAthnTypCd("DTL_REFC");
			sftyAthn.setSftyAthnNo("상세페이지 참조");
			sftyAthnLst.add(sftyAthn);
			product.setImpPrxCd("NONE");
			product.setSftyAthnLst(sftyAthnLst);
		} else { // 2024.01.04 KC인증항목 아닐경우 sftyAthnLst [] 로 세팅한다.
			List<SftyAthn> sftyAthnLst = new ArrayList<SftyAthn>();
			SftyAthn sftyAthn = new SftyAthn();
			product.setSftyAthnLst(sftyAthnLst);
		}

		PurPsbQtyInfo purPsbQtyInfo = new PurPsbQtyInfo();
		if(paLtonGoods.getOrderMinQty() > 0) {
			purPsbQtyInfo.setItmByMinPurYn("Y"); // 단품별최소구매여부
			purPsbQtyInfo.setItmByMinPurQty(paLtonGoods.getOrderMinQty()); // 단품별최소구매수량
		} else {
			purPsbQtyInfo.setItmByMinPurYn("N"); 
		}
		if(paLtonGoods.getOrderMaxQty() > 0) {
			purPsbQtyInfo.setItmByMaxPurPsbQtyYn("Y"); // 단품별최대구매가능수량여부
			purPsbQtyInfo.setMaxPurQty(paLtonGoods.getOrderMaxQty()); // 단품별최대구매수량
		} else {
			purPsbQtyInfo.setItmByMaxPurPsbQtyYn("N"); 
		}
		product.setPurPsbQtyInfo(purPsbQtyInfo);
		
		product.setAgeLmtCd(paLtonGoods.getAdultYn().equals("0") ? "0" : "19"); // 연령제한코드
		product.setCtrtTypCd("A"); // 계약유형코드 : 문의필요
		product.setPdStatCd("NEW"); // 상품상태코드 : 새상품
		product.setDpYn("Y"); // 전시여부
		
		if ("Y".equals(paLtonGoods.getDlcrtYn())) { // 딜크릿 연동 상품 따로 셋팅
			SndBgtDdInfo sndBgtDdInfo = new SndBgtDdInfo();
			sndBgtDdInfo.setSatSndPsbYn("N");
			sndBgtDdInfo.setNldySndCloseTm("1730");
			product.setSndBgtDdInfo(sndBgtDdInfo);
			product.setDlcrtYn("Y"); // 딜크릿여부
			product.setPrcCmprEpsrYn("N"); // 가격비교여부
		}

		// 상세설명
		List<Epn> epnLst = new ArrayList<Epn>();
		Epn epn = new Epn();
		epn.setPdEpnTypCd("DSCRP"); // 상품설명유형코드 : DSCRP(상품기술서)
		epn.setCnts(paLtonGoods.getDescribeExt());
		epnLst.add(epn);
		product.setEpnLst(epnLst);
		
		product.setDvProcTypCd("LO_ENTP"); // 배송처리유형코드(LO_ENTP : e커머스 업체배송)
		if("2".equals(paLtonGoods.getShipCostReceipt())) { // 착불상품
			paLtonGoods.setSpdNm("(착불)" + paLtonGoods.getSpdNm());
			product.setDvPdTypCd("CHRG_INST"); // 배송상품유형코드 : [CHRG_INST, 유료설치상품]
			product.setSndBgtNday(3); // 발송예정일수
			product.setDvMnsCd("DGNN_DV"); // 배송수단코드 DGNN_DV : 전담배송
		} else if("1".equals(paLtonGoods.getInstallYn())) { //설치배송 상품
			product.setDvPdTypCd("FREE_INST"); // 배송상품유형코드 : [FREE_INST, 무료설치상품]
			product.setSndBgtNday(30); // 발송예정일수
			product.setDvMnsCd("DGNN_DV"); // 배송수단코드 DGNN_DV : 전담배송
		} else if("1".equals(paLtonGoods.getOrderCreateYn())) {
			product.setDvPdTypCd("OD_MFG"); // 배송상품유형코드 : [OD_MFG, 주문제작상품]
			product.setSndBgtNday(30); // 발송예정일수
			product.setDvMnsCd("DPCL"); // 배송수단코드 DPCL : 택배
		} else {
			product.setDvPdTypCd("GNRL"); // 배송상품유형코드 : [GNRL, 일반]
			product.setSndBgtNday(3); // 발송예정일수
			product.setDvMnsCd("DPCL"); // 배송수단코드 DPCL : 택배
		}
		
		// 상품명
		if(paLtonGoods.getSpdNm().getBytes("UTF-8").length < 150) { // 150Byte 제한
			product.setSpdNm(paLtonGoods.getSpdNm());
		}else {
			product.setSpdNm(ComUtil.subStringUTFBytes(paLtonGoods.getSpdNm(), 145)  + "..."); 
		}

		if("1".equals(paLtonGoods.getInstallYn()) || "1".equals(paLtonGoods.getReturnNoYn()) && "1".equals(paLtonGoods.getOrderCreateYn())) { // 설치상품이거나 교환/반품불가인 주문제작상품일 경우
			product.setRtngPsbYn("N"); // 반품불가
			product.setXchgPsbYn("N"); // 교환불가
		} else {
			product.setRtngPsbYn("Y");
			product.setXchgPsbYn("Y");
		}
		
		if(paLtonGoods.getDelynoAreaCnt() > 0) { // 제주지역, 도서산간 배송불가 상품
			product.setDvRgsprGrpCd("GN102"); // 배송권역그룹코드 : GN102[전국(제주도 및 도서지역 제외)]
		} else {
			product.setDvRgsprGrpCd("GN000"); // 배송권역그룹코드 : GN000[전국]
			product.setAdtnDvCstPolNo(paLtonGoods.getDvCstPolNo()); //도서산간,제주 추가배송비
		}
		
		product.setOwhpNo(paLtonGoods.getOwhpNo()); // 출고지번호
		product.setDvCstPolNo(paLtonGoods.getGroupCode()); // 배송비정책번호

		if("ID".equals(paLtonGoods.getShipCostCode().toString().substring(0, 2))) {
			product.setCmbnRtngPsbYn("N"); // 합반품가능여부
			product.setCmbnDvPsbYn("N"); // 합배송가능여부
		}else {
			product.setCmbnRtngPsbYn("Y"); // 합반품가능여부
			product.setCmbnDvPsbYn("Y"); // 합배송가능여부
		}
		
		product.setRtrpNo(paLtonGoods.getRtrpNo()); // 회수지번호
		product.setStkMgtYn("Y"); // 재고관리여부
		product.setSitmYn("Y"); // 판매자단품여부
		
		// 옵션
		List<Itm> itmLst = new ArrayList<Itm>();
		int sortSeq = 0;

		// 상품이미지
		Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
		String imageServer = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
		String imagePath = imageServer + paLtonGoods.getImageUrl();
        String imageResizePath = "/dims/resize/600X600";
        
		for(PaLtonGoodsdtMappingVO optionItem : paGoodsDtMappingList){
			Itm itm = new Itm();
			itm.setEitmNo(optionItem.getPaCode() + optionItem.getGoodsCode() + optionItem.getGoodsdtCode()
					+ optionItem.getGoodsdtSeq());
			itm.setSortSeq(++sortSeq);
			itm.setDpYn("1".equals(optionItem.getUseYn()) ?  "Y" : "N");
			itm.setSlPrc((long)goodsPrice.getBestPrice());
			itm.setStkQty(Long.parseLong(optionItem.getTransOrderAbleQty()));
			itm.setSitmNo(optionItem.getPaOptionCode());
			
			List<ItmOpt> itmOptLst = new ArrayList<ItmOpt>();
			ItmOpt itmOpt = new ItmOpt();
			itmOpt.setOptNm("색상/크기/무늬/형태"); // 옵션명
			itmOpt.setOptVal(optionItem.getGoodsdtInfo()); // 옵션값
			itmOptLst.add(itmOpt);
			itm.setItmOptLst(itmOptLst);
			
			List<ItmImg> itmImgLst = new ArrayList<ItmImg>();
			
			// 캐시 무효 파라미터
			String dcParam = "1".equals(paLtonGoods.getImageChangeYn()) ? "?dc=" + System.currentTimeMillis() : "";
			
			// 기본이미지
			itmImgLst.add(createImage(imagePath + paLtonGoods.getImageP() + imageResizePath + dcParam, "Y"));
			
			// 추가이미지
		    if(paLtonGoods.getImageAP()!=null) {
		    	itmImgLst.add(createImage(imagePath + paLtonGoods.getImageAP() + imageResizePath + dcParam, "N"));
		    }
		    if(paLtonGoods.getImageBP()!=null) {
		    	itmImgLst.add(createImage(imagePath + paLtonGoods.getImageBP() + imageResizePath + dcParam, "N"));
		    }
		    if(paLtonGoods.getImageCP()!=null) {
		    	itmImgLst.add(createImage(imagePath + paLtonGoods.getImageCP() + imageResizePath + dcParam, "N"));
		    }
			itm.setItmImgLst(itmImgLst);
			
			itmLst.add(itm);
		}
		product.setItmLst(itmLst);
		
		return product;
	}

	/**
	 * 이미지 전문생성
	 * 
	 * @param imageUrl 이미지URL
	 * @param rprtImgYn 대표이미지여부 Y/N
	 * @return
	 */
	private ItmImg createImage( String imageUrl, String rprtImgYn) {
		ItmImg itmImg = new ItmImg();
		itmImg.setEpsrTypCd("IMG"); // 노출유형코드
		itmImg.setEpsrTypDtlCd("IMG_SQRE"); // 노출유형상세코드
		itmImg.setOrigImgFileNm(imageUrl); // 원본이미지파일명
		itmImg.setRprtImgYn(rprtImgYn); // 대표이미지여부
		return itmImg;
	}

}
