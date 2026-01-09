package com.cware.netshopping.pa11st.v2.service;

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
import com.cware.netshopping.common.code.ShipCostFlag;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.Pa11stGoodsVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.Pa11stGoods;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.pa11st.goods.service.Pa11stGoodsService;
import com.cware.netshopping.pa11st.v2.domain.Item;
import com.cware.netshopping.pa11st.v2.domain.Product;
import com.cware.netshopping.pa11st.v2.domain.ProductCert;
import com.cware.netshopping.pa11st.v2.domain.ProductCertGroup;
import com.cware.netshopping.pa11st.v2.domain.ProductMedical;
import com.cware.netshopping.pa11st.v2.domain.ProductNotification;
import com.cware.netshopping.pa11st.v2.domain.ProductOption;
import com.cware.netshopping.pa11st.v2.repository.Pa11stCnShipCostMapper;
import com.cware.netshopping.pa11st.v2.repository.Pa11stGoodsMapper;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.repository.PaRetentionGoodsMapper;
import com.cware.netshopping.pacommon.v2.service.SyncProductService;

@Service
public class Pa11stProductService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;

	@Autowired
	@Qualifier("pa11st.goods.pa11stGoodsService")
	private Pa11stGoodsService pa11stGoodsService;

	@Autowired
	Pa11stProductApiService productApiService;

	@Autowired
	Sk11stProductService sk11stProductService;

	@Autowired
	SyncProductService syncProductService;

	@Autowired
	Pa11stGoodsMapper pa11stGoodsMapper;

	@Autowired
	Pa11stCnShipCostMapper shipCostMapper;

	@Autowired
	Pa11stResultService pa11stResultService;

	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;

	@Autowired
	PaRetentionGoodsMapper retentionGoodsMapper;

	@Autowired
	TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 11번가 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public ResponseMsg transProduct(String goodsCode, String paCode, String procId) {

		ResponseMsg result = new ResponseMsg("", "");

		// 상품동기화
		PaGoodsSync sync = syncProductService.syncProduct(goodsCode, PaGroup.SK11ST.code(), procId);
		result = sync.getResponseMsg();
		if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) {
			return result;
		}

		// 스토아상품조회
		Pa11stGoods pa11stGoods = pa11stGoodsMapper.getGoods(goodsCode, paCode);

		if (pa11stGoods == null) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("연동 대상 상품이 아닙니다.");
			return result;
		}

		// 입점요청중인건은 처리하지 않음
		if (PaStatus.PROCEEDING.code().equals(pa11stGoods.getPaStatus())) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("입점요청중인 상품입니다.");
			return result;
		}

		// 입점요청/입점반려건에 대해서 신규입점 요청
		if (PaStatus.REQUEST.code().equals(pa11stGoods.getPaStatus())
				|| PaStatus.REJECT.code().equals(pa11stGoods.getPaStatus())) {

			// 동기화에서 필터링된 경우
			if (String.valueOf(HttpStatus.NO_CONTENT.value()).equals(result.getCode())) {
				return result;
			}

			if (goodsTargetMapper.existsGoodsTarget(goodsCode, paCode, PaGroup.SK11ST.code()) < 1) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
				result.setMessage("입점 대상 상품이 아닙니다(타겟데이터 없음)");
				return result;
			}

			// 신규입점
			result = registerProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				// 재고번호매핑
				sk11stProductService.updateOptionProduct(goodsCode, paCode, procId);
				result.setMessage("입점완료되었습니다.");
			}
			return result;
		}

		PaTransService transService;

		if ("1".equals(pa11stGoods.getTransSaleYn()) && PaSaleGb.SUSPEND.code().equals(pa11stGoods.getPaSaleGb())) {
			// 판매중지
			transService = sk11stProductService.stopSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			result.setMessage(transService.getResultMsg());
			return result;
		}

		boolean isUpdated = false;
		boolean isStock = false;

		if ("1".equals(pa11stGoods.getTransTargetYn())) {
			// 상품수정
			ResponseMsg updated = updateProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(updated.getCode())) {
				isUpdated = true;
				// 재고번호매핑
				sk11stProductService.updateOptionProduct(goodsCode, paCode, procId);
				result.setMessage("수정완료되었습니다.");
			} else
				return updated;
		} else {
//			// 재고번호매핑
//			sk11stProductService.updateOptionProduct(goodsCode, paCode, procId);
			// 재고변경
			transService = sk11stProductService.updateStockProduct(goodsCode, paCode, procId);
			;
			if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) {
				result.setMessage((isUpdated ? result.getMessage() + " " : "") + transService.getResultMsg());
				isStock = true;
			}
		}

		boolean isResume = false;
		if ("1".equals(pa11stGoods.getTransSaleYn()) && PaSaleGb.FORSALE.code().equals(pa11stGoods.getPaSaleGb())) {
			// 판매재개
			transService = sk11stProductService.resumeSaleProduct(goodsCode, paCode, procId);
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

	/**
	 * 11번가 신규상품 입점
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
		serviceLog.setServiceNote("[API]11번가-상품입점");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.SK11ST.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 상품입점
		ResponseMsg result = callRegisterProduct(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		// 입점요청중 해제
		pa11stGoodsMapper.updateClearProceeding(goodsCode, paCode, procId);

		return result;

	}

	/**
	 * 11번가 상품 수정
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
		serviceLog.setServiceNote("[API]11번가-상품수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.SK11ST.code());
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
	 * 11번가 상품 조회
	 * 
	 * @param goodsCode
	 * @param productNo
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
		serviceLog.setServiceNote("[API]11번가-상품조회");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.SK11ST.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		Product product = null;

		try {
			// 상품조회
			product = productApiService.getProduct(goodsCode, productNo, paCode, serviceLog.getTransServiceNo());
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(Pa11stApiRequest.API_SUCCESS_CODE);

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

		Pa11stGoodsVO pa11stGoods = null;

		try {
			log.info("===== 상품등록서비스 Start - {} =====", goodsCode);

			dateTime = systemService.getSysdatetimeToString();

			paramMap.put("dateTime", dateTime);
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
			paramMap.put("modCase", "INSERT");
			pa11stGoods = pa11stGoodsService.selectPa11stGoodsInfo(paramMap);

			if (pa11stGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (pa11stGoods.getProductNo() != null) {
				result.setCode("411");
				result.setMessage("이미 등록된 상품입니다. 11번가 상품코드 : " + pa11stGoods.getProductNo());
				return result;
			}
			if (pa11stGoods.getDescribeExt() == null) {
				pa11stGoods.setDescribeExt("");
				if (pa11stGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", pa11stGoods.getPaGroupCode());

			// 정보고시 조회
			List<PaGoodsOffer> goodsOffer = pa11stGoodsService.selectPa11stGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 상품 발송 예정일 템플릿 조회
			Map<String, String> policyMap = pa11stGoodsService.selectGoodsFor11stPolicy(pa11stGoods);
			if (policyMap == null) {
				result.setCode("440");
				result.setMessage("상품 발송 예정일 템플릿이 없습니다. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.SK11ST.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 출고지배송비정책 설정
			String shipCostFlag = pa11stGoods.getShipCostCode().substring(0, 2);
			if (shipCostFlag.equals(ShipCostFlag.BASEAMT.code())
					|| shipCostFlag.equals(ShipCostFlag.BASEAMT_CODE.code())) {
				String cnPaAddrSeq = shipCostMapper.getCnPaAddrSeq(pa11stGoods.getGoodsCode(), pa11stGoods.getPaCode());
				if (cnPaAddrSeq == null) {
					result.setCode("404");
					result.setMessage("출고지배송비정책 연동을 선행하세요.");
					return result;
				}
				pa11stGoods.setAddrSeqOut(cnPaAddrSeq);
			}

			// 입점요청중
			pa11stGoodsMapper.updateProceeding(goodsCode, paCode, procId);

			// 상품설명설정
			settingDescribeExt(pa11stGoods);

			// 단품 옵션 조회
			List<PaGoodsdtMapping> goodsdtMapping = pa11stGoodsService.selectPa11stGoodsdtInfoList(paramMap);

			pa11stGoods.setModifyId(procId);

			// 11번가 상품 전문
			Product product = createProduct(pa11stGoods, goodsdtMapping, goodsOffer, goodsPrice, transServiceNo);

			log.info("상품등록 API 호출 {}", goodsCode);
			String productNo = productApiService.registerProduct(goodsCode, product, paCode, transServiceNo);

			if (StringUtils.hasText(productNo)) {

				result.setCode("200");
				result.setMessage(Pa11stApiRequest.API_SUCCESS_CODE);

				pa11stGoods.setProductNo(productNo);

				rtnMsg = pa11stResultService.saveTransProduct(pa11stGoods, goodsPrice, goodsdtMapping);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + pa11stGoods.getGoodsCode());
				}
				
//				if("1".equals(pa11stGoods.getDoNotIslandDelyYn())) {
//					String resultCode = statImpossibleAddress(pa11stGoods.getGoodsCode(), productNo, pa11stGoods.getPaCode(), procId, 0);
//					if(!"200".equals(resultCode)) {
//						pa11stGoodsMapper.updateTransTarget(pa11stGoods);
//					}
//				}
				

			} else {
				result.setCode("500");
				result.setMessage("no data found");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

            // 반려처리 제외 메시지 체크
            if (!isNotRejectMsg(result.getCode(), result.getMessage())) {
                pa11stGoodsMapper.rejectTransTarget(goodsCode, paCode, procId,
                        StringUtil.truncate(result.getMessage(), 500));
            }

			// 이전에 중복입점 후 삭제된 경우
			pa11stResultService.applyRetention(pa11stGoods, result.getMessage());
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

		Pa11stGoodsVO pa11stGoods = null;

		try {
			log.info("===== 상품수정서비스 Start - {} =====", goodsCode);

			dateTime = systemService.getSysdatetimeToString();

			paramMap.put("dateTime", dateTime);
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
			paramMap.put("modCase", "MODIFY");
			pa11stGoods = pa11stGoodsService.selectPa11stGoodsInfo(paramMap);

			if (pa11stGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (pa11stGoods.getProductNo() == null) {
				result.setCode("411");
				result.setMessage("등록되지 않은 상품입니다. 상품코드 : " + goodsCode);
				return result;
			}
			if (pa11stGoods.getDescribeExt() == null) {
				pa11stGoods.setDescribeExt("");
				if (pa11stGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", pa11stGoods.getPaGroupCode());

			// 정보고시 조회
			paramMap.put("modCase", "");
			List<PaGoodsOffer> goodsOffer = pa11stGoodsService.selectPa11stGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 상품 발송 예정일 템플릿 조회
			Map<String, String> policyMap = pa11stGoodsService.selectGoodsFor11stPolicy(pa11stGoods);
			if (policyMap == null) {
				result.setCode("440");
				result.setMessage("상품 발송 예정일 템플릿이 없습니다. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.SK11ST.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 출고지배송비정책 설정
			String shipCostFlag = pa11stGoods.getShipCostCode().substring(0, 2);
			if (shipCostFlag.equals(ShipCostFlag.BASEAMT.code())
					|| shipCostFlag.equals(ShipCostFlag.BASEAMT_CODE.code())) {
				String cnPaAddrSeq = shipCostMapper.getCnPaAddrSeq(pa11stGoods.getGoodsCode(), pa11stGoods.getPaCode());
				if (cnPaAddrSeq == null) {
					result.setCode("404");
					result.setMessage("출고지배송비정책 연동을 선행하세요.");
					return result;
				}
				pa11stGoods.setAddrSeqOut(cnPaAddrSeq);
			}

			// 상품설명설정
			settingDescribeExt(pa11stGoods);

			// 단품 옵션 조회
			List<PaGoodsdtMapping> goodsdtMapping = pa11stGoodsService.selectPa11stGoodsdtInfoList(paramMap);

			pa11stGoods.setModifyId(procId);

			// 11번가 상품 전문
			Product product = createProduct(pa11stGoods, goodsdtMapping, goodsOffer, goodsPrice, transServiceNo);
			if (product == null) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
				return result;
			}

			log.info("상품수정 API 호출 {}", goodsCode);
			String productNo = productApiService.updateProduct(goodsCode, product, paCode, transServiceNo);

			if (StringUtils.hasText(productNo)) {

				result.setCode("200");
				result.setMessage(Pa11stApiRequest.API_SUCCESS_CODE);

				pa11stGoods.setProductNo(productNo);

				rtnMsg = pa11stResultService.saveTransProduct(pa11stGoods, goodsPrice, goodsdtMapping);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + pa11stGoods.getGoodsCode());
				}
				
//				String resultCode = null;
				
//				if("1".equals(pa11stGoods.getDoNotIslandDelyYn())) {
//					resultCode = statImpossibleAddress(pa11stGoods.getGoodsCode(), productNo, pa11stGoods.getPaCode(), procId, 0);
//				}else {
//					resultCode = stopImpossibleAddress(pa11stGoods.getGoodsCode(), productNo, pa11stGoods.getPaCode(), procId, 0);
//				}
//				
//				if(!"200".equals(resultCode)) {
//					pa11stGoodsMapper.updateTransTarget(pa11stGoods);
//				}

			} else {
				result.setCode("500");
				result.setMessage("no data found");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단 (레거시는 특정 메시지가 아닌 경우 모두 수기중단함)
				pa11stGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
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

        if ("502".equals(resultCode)) return true;
        
		if (!StringUtils.hasText(rejectMsg) && !"Exception".equals(resultCode) && !"ERROR".equals(resultCode))
			return false;

		String[] rejectNotMatch = new String[] { "connect timed out",
				"The host did not accept the connection within timeout of 5000 ms" };

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

		String[] rejectMatch = new String[] { "중복된 데이터", "초과하여 설정", "옵션값은 100개까지 사용 가능", "옵션값은 공백포함 한글 25자/영문(숫자)50자",
				"판매가 정보 수정 시 최대 80% 인하까지", "상품명에 금칙어가 사용", "특수 문자", "수정처리 할수 없는 상품", "maktPrc 정가", "안전인증정보", "축산물 이력번호", "해외 쇼핑 카테고리", "아티스트", "카테고리 등록 권한" };

		return Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s));
	}

	/**
	 * 기술서와 공지사항 설정
	 * 
	 * @param pa11stGoods
	 */
	private void settingDescribeExt(Pa11stGoodsVO pa11stGoods) {

		// 상품구성템플릿
		String goodsCom = StringUtils.hasText(pa11stGoods.getGoodsCom()) ? pa11stGoods.getGoodsCom() : "";

		// 상품구성 텍스트 잘림 방지
		if (!"".equals(goodsCom) && !goodsCom.contains("<img") && !goodsCom.contains("<IMG") && !goodsCom.contains("<iframe")) {
			List<String> goodsComList = new ArrayList<>(Arrays.asList(goodsCom.split("\n")));
			for (int i = 0; i < goodsComList.size(); i++) {
				String str = goodsComList.get(i);
				if (str.length() > 40) { // 문자열 길이 체크
					List<String> longStrList = new ArrayList<>(Arrays.asList(str.split("")));
					for (int j = 1; j < longStrList.size(); j++) {
						if (j%40 == 0) longStrList.add(j, "<br />"); // 40자마다 줄바꿈
					}
					str = String.join("", longStrList);
					goodsComList.set(i, str);
				}
			}
			goodsCom = String.join("<br />", goodsComList);
		}
				
		goodsCom = (!"".equals(goodsCom))
	        	? ("<div style='line-height: 2.0em; font-family: NanumBarunGothic; font-size: 19px;'><div><h4>&middot;&nbsp;상품구성<h4><pre>"
	        		+ goodsCom.replace("\n", "<br />") + "</pre></div></div>") 
	        	: "";
			
      		
		// 웹기술서
		pa11stGoods.setDescribeExt(
				"<div align='center'><img alt='' src='" + pa11stGoods.getTopImage() + "' /><br /><br /><br />" // 상단이미지
						+ (StringUtils.hasText(pa11stGoods.getCollectImage())
								? "<img alt='' src='" + pa11stGoods.getCollectImage() + "' /><br /><br /><br />"
								: "") // 착불이미지
						+ goodsCom // 상품구성
						+ pa11stGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />" // 기술서
						+ "<img alt='' src='" + pa11stGoods.getBottomImage() + "' /></div>"); // 하단이미지

		// 제휴 공지사항
		if (StringUtils.hasText(pa11stGoods.getNoticeExt())) {
			pa11stGoods.setDescribeExt(
					pa11stGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + pa11stGoods.getDescribeExt());
		}
	}

	private Product createProduct(Pa11stGoodsVO pa11stGoods, List<PaGoodsdtMapping> paGoodsDtMappingList,
			List<PaGoodsOffer> paGoodsOfferList, PaGoodsPriceApply goodsPrice, long transServiceNo) throws Exception {

		Product product = new Product();

		if (pa11stGoods.getProductNo() != null) {// 상품 수정시 조회 API실행 후 변경되는 부분만 수정한다.

			// 상품조회 API
			Product curProduct = productApiService.getProduct(pa11stGoods.getGoodsCode(), pa11stGoods.getProductNo(),
					pa11stGoods.getPaCode(), transServiceNo);

			if (!StringUtils.hasText(curProduct.getPrdNo())) {
				pa11stResultService.applyRetention(pa11stGoods, curProduct.getMessage());
				return null;
			}

			product.setPrdNo(curProduct.getPrdNo());
			// 옵션수정여부
			product.setOptUpdateYn("Y");

		}

		// 고정가판매
		product.setSelMthdCd("01");
		// 카테고리번호
		product.setDispCtgrNo(pa11stGoods.getPaLmsdKey());
		// 일반배송상품
		product.setPrdTypCd("01");
		// 상품명, 최대 100바이트
		product.setPrdNm(ComUtil.subStringBytes(pa11stGoods.getGoodsName(), 99));
		// 브랜드
		product.setBrand(pa11stGoods.getBrandName());
		if (StringUtils.hasText(pa11stGoods.getBrandNo())) {
			product.setApiPrdAttrBrandCd(pa11stGoods.getBrandNo());
		}
		// 원산지코드
		product.setOrgnTypCd(pa11stGoods.getOrgnTypCd());
		// 원산지지역코드
		product.setOrgnTypDtlsCd(pa11stGoods.getOrgnTypDtlsCd());
		// 원산지명 (원산지코드 03 기타인 경우 원산지명 필수)
		if ("03".equals(pa11stGoods.getOrgnTypCd())) {
			product.setOrgnNmVal("기타");
		}

		if ("1".equals(pa11stGoods.getTaxYn())) {
			// 과세
			product.setSuplDtyfrPrdClfCd("01");
		} else if ("1".equals(pa11stGoods.getTaxSmallYn())) {
			// 영세
			product.setSuplDtyfrPrdClfCd("03");
		} else {
			// 면세
			product.setSuplDtyfrPrdClfCd("02");
		}

		if ("1".equals(pa11stGoods.getOrderCreateYn())) {
			// 주문제작상품
			product.setPrdStatCd("10");
		} else {
			// 새상품
			product.setPrdStatCd("01");
		}

		// 미성년자구매가능
		product.setMinorSelCnYn("1".equals(pa11stGoods.getAdultYn()) ? "N" : "Y");

		// 상품이미지
		Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
		String imageServer = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));

		String imagePath = imageServer + pa11stGoods.getImageUrl();
		String imageResizePath = "/dims/resize/600X600";

		// 11번가 개발서버에서 스토아 이미지 개발서버 접근불가로 임시 이미지 등록 처리
		boolean isTest = imageServer.equals("http://dev-image.skstoa.com");
		String testImage = "http://image.bshopping.co.kr/guide/orderCaution.jpg";

		// 캐시 무효 파라미터
		String dcParam = "1".equals(pa11stGoods.getImageChangeYn()) ? "?dc=" + System.currentTimeMillis() : "";
		
		// 기본이미지
		product.setPrdImage01(imagePath + pa11stGoods.getImageP() + imageResizePath + dcParam);
		if (isTest)
			product.setPrdImage01(testImage);
		// 추가이미지
		if (StringUtils.hasText(pa11stGoods.getImageAP())) {
			product.setPrdImage02(imagePath + pa11stGoods.getImageAP() + imageResizePath + dcParam);
			if (isTest)
				product.setPrdImage02(testImage);
		} else {
			if (pa11stGoods.getProductNo() != null) product.setPrdImage02("");
		}
		if (StringUtils.hasText(pa11stGoods.getImageBP())) {
			product.setPrdImage03(imagePath + pa11stGoods.getImageBP() + imageResizePath + dcParam);
			if (isTest)
				product.setPrdImage03(testImage);
		} else {
			if (pa11stGoods.getProductNo() != null) product.setPrdImage03("");
		}
		if (StringUtils.hasText(pa11stGoods.getImageCP())) {
			product.setPrdImage04(imagePath + pa11stGoods.getImageCP() + imageResizePath + dcParam);
			if (isTest)
				product.setPrdImage04(testImage);
		} else {
			if (pa11stGoods.getProductNo() != null) product.setPrdImage04("");
		}
		// 목록이미지 (기본이미지와 동일)
		product.setPrdImage05(product.getPrdImage01());
		if (isTest)
			product.setPrdImage05(testImage);

		// 상세설명
		product.setHtmlDetail(pa11stGoods.getDescribeExt().replaceAll("\\p{C}", ""));

		if ("1".equals(pa11stGoods.getCertYn())) {
			// 인증정보그룹
			List<ProductCertGroup> productCertGroups = new ArrayList<ProductCertGroup>();

			ProductCertGroup productCertGroup = new ProductCertGroup();
			// 01 : 전기용품/생활용품 KC인증
			productCertGroup.setCrtfGrpTypCd("01");
			// 01 : KC인증대상
			productCertGroup.setCrtfGrpObjClfCd("01");
			// 인증정보
			List<ProductCert> productCerts = new ArrayList<ProductCert>();
			ProductCert productCert = new ProductCert();
			// 132 : [전기용품/생활용품] 상품상세설명 참조
			productCert.setCertTypeCd("132");
			productCerts.add(productCert);
			productCertGroup.setProductCert(productCerts);
			productCertGroups.add(productCertGroup);

			productCertGroup = new ProductCertGroup();
			// 02 : 어린이제품 KC인증
			productCertGroup.setCrtfGrpTypCd("02");
			// 03 : KC인증대상 아님
			productCertGroup.setCrtfGrpObjClfCd("03");
			productCertGroups.add(productCertGroup);

			productCertGroup = new ProductCertGroup();
			// 03 : 방송통신기자재 KC인증
			productCertGroup.setCrtfGrpTypCd("03");
			// 03 : KC인증대상 아님
			productCertGroup.setCrtfGrpObjClfCd("03");
			productCertGroups.add(productCertGroup);

			productCertGroup = new ProductCertGroup();
			// 04 : 생활화학 및 살생물제품
			productCertGroup.setCrtfGrpTypCd("04");
			// 05 : 생활화학 및 살생물제품 대상 아님
			productCertGroup.setCrtfGrpObjClfCd("05");
			productCertGroups.add(productCertGroup);

			product.setProductCertGroup(productCertGroups);
		}

		if (pa11stGoods.getMedicalKey() != null && pa11stGoods.getMedicalRetail() != null
				&& pa11stGoods.getMedicalAd() != null) {

			// 의료기기 품목허가
			ProductMedical productMedical = new ProductMedical();
			productMedical.setMedicalKey(pa11stGoods.getMedicalKey());
			productMedical.setMedicalRetail(pa11stGoods.getMedicalRetail());
			productMedical.setMedicalAd(pa11stGoods.getMedicalAd());
			product.setProductMedical(productMedical);
		}

		if (pa11stGoods.getBeefTraceNo() != null) {
			// 축산물 이력번호
			product.setBeefTraceStat("01");
			product.setBeefTraceNo(pa11stGoods.getBeefTraceNo());
		}
		
		if ("1".equals(pa11stGoods.getBeefTraceYn())) {
			// 축산물이력 번호 필수 카테고리 -> 상세페이지 참조로 연동
			product.setBeefTraceStat("03");
		}

		// 판매자 상품코드
		product.setSellerPrdCd(pa11stGoods.getGoodsCode());
		// 판매기간 설정안함
		product.setSelTermUseYn("N");

		// 판매가
		product.setSelPrc(String.valueOf((long) goodsPrice.getSalePrice()));
		double dcAmt = goodsPrice.getSalePrice() - goodsPrice.getBestPrice();
		if (dcAmt > 0) {
			// 기본즉시할인 설정
			product.setCuponcheck("Y");
			product.setDscAmtPercnt(String.valueOf((long) dcAmt));
			product.setCupnDscMthdCd("01"); // 정액
		} else {
			product.setCuponcheck("N");
		}

		// 선택형 옵션
		product.setOptSelectYn("Y");
		product.setTxtColCnt("1");
		// 상품상세 옵션값 노출 방식 등록순
		product.setPrdExposeClfCd("00");

		product.setColTitle("옵션명");

		// 삭제후 재입점되는 경우 셀러재고번호 중복 방지를 위해 삭제이력순번을 적용
		String retentionSeq = retentionGoodsMapper.selectMaxSeq(pa11stGoods.getGoodsCode(), pa11stGoods.getPaCode(),
				PaGroup.SK11ST.code());
		if (retentionSeq == null)
			retentionSeq = "";

		List<ProductOption> productOptions = new ArrayList<ProductOption>();
		for (PaGoodsdtMapping option : paGoodsDtMappingList) {
			ProductOption productOption = new ProductOption();
			productOption.setUseYn(option.getTransOrderAbleQty().equals("0") ? "N" : "Y");
			productOption.setColOptPrice("0");
			// 특수 문자[',",%,&,<,>,#,†,\,∏,‡,콤마(,)]는 입력할 수 없습니다. 
			// |도 포함되며, %는 의미 전달이 안될수 있어 제거하지 않음
			productOption.setColValue0(
					option.getGoodsdtInfo().replaceAll("[&#<>†∏‡,\\'\"^|]", " ").trim().replaceAll("\\s+", " "));
			productOption.setColCount(String.valueOf(option.getTransOrderAbleQty()));
			productOption.setColSellerStockCd(
					option.getGoodsCode() + option.getGoodsdtCode() + pa11stGoods.getPaCode() + retentionSeq);
			productOptions.add(productOption);
		}
		product.setProductOption(productOptions);
		
		// 최소구매수량 1회제한
		product.setSelMinLimitTypCd("01");
		product.setSelMinLimitQty(String.valueOf(pa11stGoods.getOrderMinQty()));

		if (pa11stGoods.getOrderMaxQty() > 0) {
			if (pa11stGoods.getCustOrdQtyCheckYn() == 0) {// 주문수량검사여부
				// 최대구매수량 1회제한
				product.setSelLimitTypCd("01");
				product.setSelLimitQty(String.valueOf(pa11stGoods.getOrderMaxQty()));
			} else {
				// 최대구매수량 기간 제한
				product.setSelLimitTypCd("02");
				product.setSelLimitQty(String.valueOf(pa11stGoods.getTermOrderQty()));
				long townSelLmtDy = pa11stGoods.getCustOrdQtyCheckTerm();
				if (townSelLmtDy == 0 || townSelLmtDy > 30) {
					townSelLmtDy = 30;
				}
				product.setTownSelLmtDy(String.valueOf(townSelLmtDy));
			}
		}

		if (pa11stGoods.getDoNotIslandDelyYn().equals("1")) {
			// 배송가능지역 (02:전국(제주 도서산간지역 제외))
			product.setDlvCnAreaCd("02");
			// 상품홍보문구 : 도서산간불가
			product.setAdvrtStmt("도서산간 배송불가");
		} else {
			// 배송가능지역 (01:전국)
			product.setDlvCnAreaCd("01");
			// 수정시 셋팅안하면 도서산간 배송불가 문구 남아있음
			product.setAdvrtStmt(" ");
		}

		// 배송방법 (01:택배, 03:직접전달)
		product.setDlvWyCd(pa11stGoods.getInstallYn().equals("1") ? "03" : "01");
		// 발송템플릿번호
		product.setDlvSendCloseTmpltNo(pa11stGoods.getPaPolicyNo());

		if("2".equals(pa11stGoods.getShipCostReceipt())) {
			// 착불
			// 배송비종류 고정배송비
			product.setDlvCstInstBasiCd("02");
			// 고정배송비(착불을 위한 임의배송비 셋팅 -> 0원일 경우 상품판매중단됨)
			product.setDlvCst1(String.valueOf(pa11stGoods.getOrdCost()));
			// 상품상세참고
			product.setDlvCstInfoCd("01");
			// 결제방법 착불
			product.setDlvCstPayTypCd("02");
		} else {
			switch (pa11stGoods.getShipCostCode().substring(0, 2)) {
			case "FR": // 무료
				product.setDlvCstInstBasiCd("01");
				// 묶음배송가능
				product.setBndlDlvCnYn("Y");
				// 결제방법 선결제필수
				product.setDlvCstPayTypCd("03");
				break;
			case "CN":
			case "PL": // 조건부
				// 출고지 조건부 배송비
				product.setDlvCstInstBasiCd("08");
				product.setBndlDlvCnYn("Y");
				product.setDlvCstPayTypCd("03");
				break;
			default: // 상품별
				// 고정 배송비
				product.setDlvCstInstBasiCd("02");
				product.setDlvCst1(String.valueOf(pa11stGoods.getOrdCost()));
				product.setBndlDlvCnYn("N");
				product.setDlvCstPayTypCd("03");
				break;
			}
		}

		long addJejuShipcost = pa11stGoods.getJejuCost() - pa11stGoods.getOrdCost();
		long addIslandShipcost = pa11stGoods.getIslandCost() - pa11stGoods.getOrdCost();

		if (addJejuShipcost < 0) {
			addJejuShipcost = 0;
		}
		if (addIslandShipcost < 0) {
			addIslandShipcost = 0;
		}
		
		// 11번가 제주/도서산간 배송비가 11,000원 이상일때, 도서산간 배송불가로 연동
		if (addJejuShipcost >= 11000 || addIslandShipcost >= 11000) {
			// 배송가능지역 (02:전국(제주 도서산간지역 제외))
			product.setDlvCnAreaCd("02");
			// 상품홍보문구 : 도서산간불가
			product.setAdvrtStmt("도서산간 배송불가");
		}

		product.setJejuDlvCst(String.valueOf(addJejuShipcost));// 제주 추가 배송비(주문만)
		product.setIslandDlvCst(String.valueOf(addIslandShipcost));// 도서산간 추가 배송비(주문만)

		product.setAddrSeqOut(pa11stGoods.getAddrSeqOut()); // 출고지 주소코드
		product.setAddrSeqIn(pa11stGoods.getAddrSeqIn()); // 반품/교환지 주소코드

		ProductNotification productNotification = new ProductNotification();
		productNotification.setType(paGoodsOfferList.get(0).getPaOfferType());
		List<Item> itemList = new ArrayList<Item>();

		for (PaGoodsOffer goodsOffer : paGoodsOfferList) {
			Item item = new Item();
			item.setCode(goodsOffer.getPaOfferCode());
			item.setName(goodsOffer.getPaOfferExt().replace("<", " ").replace(">", " ").replaceAll("\\p{C}", ""));
			itemList.add(item);
		}
		productNotification.setItem(itemList);
		product.setProductNotification(productNotification);

		// 반품 배송비
		product.setRtngdDlvCst(String.valueOf(pa11stGoods.getReturnCost()));
		// 교환 배송비(왕복)
		product.setExchDlvCst(String.valueOf(pa11stGoods.getChangeCost()));

		// 초기배송비 무료시 부과방법 편도
		product.setRtngdDlvCd("02");

		// A/S 안내
		product.setAsDetail(pa11stGoods.getCsTel());
		// 반품/교환 안내
		product.setRtngExchDetail("SK Stoa : " + pa11stGoods.getCsTel());

		// 가격비교 사이트 등록
		product.setPrcCmpExpYn("Y");

		// 원재료 유형 코드 05 : 상품별 원산지는 상세설명 참조
		product.setRmaterialTypCd("05");
		
		return product;
	}

	
	/**
	 * 11번가 상품 배송불가지역 적용
	 * 
	 * @param goodsCode
	 * @param productNo
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public String statImpossibleAddress(String goodsCode, String productNo, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]11번가-상품 배송불가지역 적용");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.SK11ST.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		String resultCode = null;
		ResponseMsg result = new ResponseMsg("", "");
		try {
			
			resultCode = productApiService.statImpossibleAddress(goodsCode, productNo, paCode, serviceLog.getTransServiceNo());
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(Pa11stApiRequest.API_SUCCESS_CODE);

			result.setCode("200");
			result.setMessage(Pa11stApiRequest.API_SUCCESS_CODE);
		} catch (TransApiException ex) {
			serviceLog.setResultCode(ex.getCode());
			serviceLog.setResultMsg(ex.getMessage());
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			serviceLog.setResultCode("500");
			serviceLog.setResultMsg(e.getMessage());
			result.setCode("500");
			result.setMessage(e.getMessage());
		}

		transLogService.logTransServiceEnd(serviceLog);

		return resultCode;

	}
	
	/**
	 * 11번가 상품 배송불가지역 해제
	 * 
	 * @param goodsCode
	 * @param productNo
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public String stopImpossibleAddress(String goodsCode, String productNo, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]11번가-상품 배송불가지역 해제");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.SK11ST.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		String resultCode = null;
		ResponseMsg result = new ResponseMsg("", "");
		
		try {
			
			resultCode = productApiService.stopImpossibleAddress(goodsCode, productNo, paCode, serviceLog.getTransServiceNo());
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(Pa11stApiRequest.API_SUCCESS_CODE);

			result.setCode("200");
			result.setMessage(Pa11stApiRequest.API_SUCCESS_CODE);
		} catch (TransApiException ex) {
			serviceLog.setResultCode(ex.getCode());
			serviceLog.setResultMsg(ex.getMessage());
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			serviceLog.setResultCode("500");
			serviceLog.setResultMsg(e.getMessage());
			result.setCode("500");
			result.setMessage(e.getMessage());
		}

		transLogService.logTransServiceEnd(serviceLog);

		return resultCode;

	}
}
