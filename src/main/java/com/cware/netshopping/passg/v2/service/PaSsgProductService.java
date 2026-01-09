package com.cware.netshopping.passg.v2.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaSsgGoodsVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaSsgDisplayMapping;
import com.cware.netshopping.domain.model.PaSsgGoodsdtMapping;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.service.SyncProductService;
import com.cware.netshopping.passg.goods.service.PaSsgGoodsService;
import com.cware.netshopping.passg.v2.domain.DispCtg;
import com.cware.netshopping.passg.v2.domain.ImgInfo;
import com.cware.netshopping.passg.v2.domain.ItemMngAttr;
import com.cware.netshopping.passg.v2.domain.Product;
import com.cware.netshopping.passg.v2.domain.ProductInsert;
import com.cware.netshopping.passg.v2.domain.ProductUpdate;
import com.cware.netshopping.passg.v2.domain.Site;
import com.cware.netshopping.passg.v2.domain.Uitem;
import com.cware.netshopping.passg.v2.domain.UitemPrc;
import com.cware.netshopping.passg.v2.repository.PaSsgGoodsMapper;

@Service
public class PaSsgProductService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;

	@Autowired
	@Qualifier("passg.goods.paSsgGoodsService")
	private PaSsgGoodsService paSsgGoodsService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;

	@Autowired
	PaSsgProductApiService productApiService;

	@Autowired
	SsgProductService ssgProductService;

	@Autowired
	SyncProductService syncProductService;

	@Autowired
	PaSsgGoodsMapper paSsgGoodsMapper;

	@Autowired
	PaSsgResultService paSsgResultService;

	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;

	@Autowired
	TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * SSG 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public ResponseMsg transProduct(String goodsCode, String paCode, String procId) {

		ResponseMsg result = new ResponseMsg("", "");

		// 상품동기화
		PaGoodsSync sync = syncProductService.syncProduct(goodsCode, PaGroup.SSG.code(), procId);
		result = sync.getResponseMsg();
		if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) {
			return result;
		}

		// 스토아상품조회
		PaSsgGoodsVO paSsgGoods = paSsgGoodsMapper.getGoods(goodsCode, paCode);

		if (paSsgGoods == null) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("연동 대상 상품이 아닙니다.");
			return result;
		}

		// 입점요청중인건은 처리하지 않음
		if (PaStatus.PROCEEDING.code().equals(paSsgGoods.getPaStatus())) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("입점요청중인 상품입니다.");
			return result;
		}

		// 입점요청/입점반려건에 대해서 신규입점 요청
		if (PaStatus.REQUEST.code().equals(paSsgGoods.getPaStatus())
				|| PaStatus.REJECT.code().equals(paSsgGoods.getPaStatus())) {

			// 동기화에서 필터링된 경우
			if (String.valueOf(HttpStatus.NO_CONTENT.value()).equals(result.getCode())) {
				return result;
			}

			if (goodsTargetMapper.existsGoodsTarget(goodsCode, paCode, PaGroup.SSG.code()) < 1) {
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
		
		if ("1".equals(paSsgGoods.getTransSaleYn()) && PaSaleGb.SUSPEND.code().equals(paSsgGoods.getPaSaleGb())) {
			// 판매중지
			transService = ssgProductService.stopSaleProduct(goodsCode, paCode, procId);
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

		if ("1".equals(paSsgGoods.getTransTargetYn())) {
			// 상품수정
			ResponseMsg updated = updateProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(updated.getCode())) {
				isUpdated = true;
				result.setMessage("수정완료되었습니다.");
			} else
				return updated;
		} else {
			// 재고변경
			transService = ssgProductService.updateStockProduct(goodsCode, paCode, procId);
			if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) {
				result.setMessage((isUpdated ? result.getMessage() + " " : "") + transService.getResultMsg());
				isStock = true;
			}
		}

		if (isStock || isUpdated ) { // 상품/재고가 변경된 경우
			result.setCode(String.valueOf(HttpStatus.OK.value()));
		} else {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
			result.setMessage("변경된 사항이 없습니다.");
		}

		return result;
	}

	/**
	 * SSG 신규상품 입점
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
		serviceLog.setServiceNote("[API]SSG-상품입점");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.SSG.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 상품입점
		ResponseMsg result = callRegisterProduct(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		// 입점요청중 해제
		paSsgGoodsMapper.updateClearProceeding(goodsCode, paCode, procId);

		return result;

	}

	/**
	 * SSG 상품 수정
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
		serviceLog.setServiceNote("[API]SSG-상품수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.SSG.code());
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
	 * SSG 상품 조회
	 * 
	 * @param goodsCode
	 * @param itemId
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public Product getProduct(String goodsCode, String itemId, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]SSG-상품조회");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.SSG.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		Product product = null;

		try {
			// 상품조회
			product = productApiService.getProduct(goodsCode, itemId, paCode, serviceLog.getTransServiceNo());
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(PaSsgApiRequest.API_SUCCESS_CODE);

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

		PaSsgGoodsVO paSsgGoods = null;

		try {
			log.info("===== 상품등록서비스 Start - {} =====", goodsCode);

			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
			paramMap.put("modCase", "INSERT");
			paSsgGoods = paSsgGoodsService.selectPaSsgGoodsInfo(paramMap);

			if (paSsgGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paSsgGoods.getItemId() != null) {
				result.setCode("411");
				result.setMessage("이미 등록된 상품입니다. SSG 상품코드 : " + paSsgGoods.getItemId());
				return result;
			}
			if (paSsgGoods.getDescribeExt() == null) {
				paSsgGoods.setDescribeExt("");
				if (paSsgGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", paSsgGoods.getPaGroupCode());

			// 정보고시 조회
			List<PaGoodsOfferVO> goodsOffer = paCommonService.selectPaGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.SSG.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 입점요청중
			paSsgGoodsMapper.updateProceeding(goodsCode, paCode, procId);

			// 상품설명설정
			settingDescribeExt(paSsgGoods);

			// 단품 옵션 조회
			List<PaSsgGoodsdtMapping> goodsdtMapping = paSsgGoodsService.selectPaSsgGoodsdtInfoList(paramMap);

			paSsgGoods.setModifyId(procId);

			// SSG 상품 전문
			ProductInsert productInsert = (ProductInsert)createProduct(paSsgGoods, goodsdtMapping, goodsOffer, goodsPrice, transServiceNo);

			log.info("상품등록 API 호출 {}", goodsCode);
			Product product = productApiService.registerProduct(goodsCode, productInsert, paCode, transServiceNo);

			if (StringUtils.hasText(product.getItemId())) {

				result.setCode("200");
				result.setMessage(product.getResultMessage());

				paSsgGoods.setItemId(product.getItemId());

				rtnMsg = paSsgResultService.saveTransProduct(paSsgGoods, goodsPrice, goodsdtMapping,
						product.getUitems());

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + paSsgGoods.getGoodsCode());
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
                paSsgGoodsMapper.rejectTransTarget(goodsCode, paCode, procId,
                        StringUtil.truncate(result.getMessage(), 500));
            } else if(result.getMessage().contains("동일한 상품이 이미 존재하여, 상품 등록이 불가합니다")) {
            	// TPASSGGOODS.GOODS_NAME '상품명_'으로 UPDATE, 7월 12일 ssg측 상품명 길이 정책 변경으로 인한 90byte 자르기
            	if(paSsgGoods.getGoodsName().getBytes().length < 88) { // 바이트 계산 쪽에서 87은 그냥 연동하므로 88 미만 자르기
            		paSsgGoodsMapper.updateGoodsName(goodsCode, paCode, procId,
            		paSsgGoods.getGoodsName() + "_");
            	} else {
            		paSsgGoodsMapper.rejectTransTarget(goodsCode, paCode, procId,
            		StringUtil.truncate(result.getMessage(), 500));
            	}
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

		PaSsgGoodsVO paSsgGoods = null;

		try {
			log.info("===== 상품수정서비스 Start - {} =====", goodsCode);

			dateTime = systemService.getSysdatetimeToString();

			paramMap.put("dateTime", dateTime);
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
			paramMap.put("modCase", "MODIFY");
			paSsgGoods = paSsgGoodsService.selectPaSsgGoodsInfo(paramMap);

			if (paSsgGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paSsgGoods.getItemId() == null) {
				result.setCode("411");
				result.setMessage("등록되지 않은 상품입니다. 상품코드 : " + goodsCode);
				return result;
			}
			if (paSsgGoods.getDescribeExt() == null) {
				paSsgGoods.setDescribeExt("");
				if (paSsgGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", paSsgGoods.getPaGroupCode());

			// 정보고시 조회
			List<PaGoodsOfferVO> goodsOffer = paCommonService.selectPaGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.SSG.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 상품설명설정
			settingDescribeExt(paSsgGoods);

			// 단품 옵션 조회
			List<PaSsgGoodsdtMapping> goodsdtMapping = paSsgGoodsService.selectPaSsgGoodsdtInfoList(paramMap);

			paSsgGoods.setModifyId(procId);

			// SSG 상품 전문
			ProductUpdate productUpdate = (ProductUpdate)createProduct(paSsgGoods, goodsdtMapping, goodsOffer, goodsPrice, transServiceNo);
			
			log.info("상품수정 API 호출 {}", goodsCode);
			Product product = productApiService.updateProduct(goodsCode, productUpdate, paCode, transServiceNo);

			result.setCode("200");
			result.setMessage(product.getResultMessage());

			rtnMsg = paSsgResultService.saveTransProduct(paSsgGoods, goodsPrice, goodsdtMapping, product.getUitems());

			if (!rtnMsg.equals("000000")) {
				result.setCode("500");
				result.setMessage(rtnMsg + " : " + paSsgGoods.getGoodsCode());
			}

		} catch (TransApiException ex) {

			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단
				paSsgGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
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

		if (!StringUtils.hasText(rejectMsg) && !"Exception".equals(resultCode))
			return false;

		String[] rejectNotMatch = 
				new String[] { "Read timed out", "동일한 상품이 이미 존재하여, 상품 등록이 불가합니다" };

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

		String[] rejectMatch = new String[] { "성인상품 또는 주류상품"
				, "중복 된 옵션"
				, "금칙어"
				, "byte 이상 등록해야 합니다"
				, "판매가는 기존대비 80%"
				, "입력하신 판매가가"
				, "수정이 불가능합니다"
				, "해당 카테고리에 등록할 수 없는 과세구분"
				, "표시단위(sellCapaUnitCd)는 필수 입력"
				};

		return Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s));
	}

	/**
	 * 기술서와 공지사항 설정
	 * 
	 * @param paSsgGoods
	 */
	private void settingDescribeExt(PaSsgGoodsVO paSsgGoods) {

		
		// 상품구성템플릿
		String goodsCom = StringUtils.hasText(paSsgGoods.getGoodsCom()) ? paSsgGoods.getGoodsCom() : "";

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
		paSsgGoods.setDescribeExt(
				"<div align='center'><img alt='' src='" + paSsgGoods.getTopImage() + "' /><br /><br /><br />" // 상단이미지
						+ (StringUtils.hasText(paSsgGoods.getCollectImage())
								? "<img alt='' src='" + paSsgGoods.getCollectImage() + "' /><br /><br /><br />"
								: "") // 착불이미지
						+ goodsCom // 상품구성
						+ paSsgGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />" // 기술서
//						+ "<img alt='' src='" + paSsgGoods.getBottomImage() + "' /></div>"); // 하단이미지 제거
						+ "</div>");

		// 제휴 공지사항
		if (StringUtils.hasText(paSsgGoods.getNoticeExt())) {
			paSsgGoods.setDescribeExt(
					paSsgGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + paSsgGoods.getDescribeExt());
		}
	}

	private Product createProduct(PaSsgGoodsVO paSsgGoods, List<PaSsgGoodsdtMapping> paGoodsDtMappingList,
			List<PaGoodsOfferVO> paGoodsOfferList, PaGoodsPriceApply goodsPrice, long transServiceNo) throws Exception {
		
		Product product = null;

		if (paSsgGoods.getItemId() != null) {
			product = new ProductUpdate();
			product.setItemId(paSsgGoods.getItemId());
			
			// 판매상태 | 20 : 판매중, 80 : 판매 일시 중지, 90 : 영구판매 중지
			product.setSellStatCd("20".equals(paSsgGoods.getPaSaleGb()) ? "20" : "80");
		} else {
			product = new ProductInsert();			
		}

//		product.setItemNm(paSsgGoods.getGoodsName());
		product.setItemNm(byteCuter(paSsgGoods.getGoodsName(), 87)); // ...까지 90byte 맞춰주기
		product.setBrandId(paSsgGoods.getBrandId());
		product.setStdCtgId(paSsgGoods.getStdCtgId());

		// 전시카테고리에 매핑된 사이트 목록
		ParamMap param = new ParamMap();
		param.put("goodsCode", paSsgGoods.getGoodsCode());
		List<PaSsgDisplayMapping> ssgDisplayList = paSsgGoodsService.selectPaSsgDisplayList(param);
		
		List<Site> sites = new ArrayList<Site>();
		List<DispCtg> dispCtgs = new ArrayList<DispCtg>();
		boolean isDisplay = false;
		
		for(PaSsgDisplayMapping mapping : ssgDisplayList) {
			// SSG.COM, 그로서리몰은 사이트 설정에서 제외
			if (!mapping.getSiteNo().matches("6005|7018")) {
				// 이마트몰(6001), 신세계몰(6004) 전시
				Site site = new Site();
				site.setSiteNo(mapping.getSiteNo());
				site.setSellStatCd("20");
				sites.add(site);
				isDisplay = true;
			}

			// 전시카테고리 설정
			DispCtg dispCtg		= new DispCtg();
			dispCtg.setSiteNo(mapping.getSiteNo());
			dispCtg.setDispCtgId(mapping.getDispCtgId());
			dispCtgs.add(dispCtg);
		}
		
		// 사이트번호가 하나도 설정되지 않은 경우 이마트/신세계 모두 입점
		if (!isDisplay) {
			Site site = new Site();
			site.setSiteNo("6001");
			site.setSellStatCd("20");
			sites.add(site);

			site = new Site();
			site.setSiteNo("6004");
			site.setSellStatCd("20");
			sites.add(site);
		}

		product.setSites(sites);
		product.setDispCtgs(dispCtgs);
		
		product.setItemChrctDivCd("10"); // 상품 특성 구분 코드 - 일반 : 10
		product.setExusItemDivCd("1".equals(paSsgGoods.getEventYn()) ? "20" : "10"); // 2023-09-26 전용 상품 구분 코드 - 일반 : 10, 명절 GIFT 일반 : 20
		product.setExusItemDtlCd("10"); // 전용 상품 상세 코드 - 일반 : 10
		product.setDispAplRngTypeCd("10"); // 전시 적용 범위 유형 코드 - 10 : 전체 (모바일 + PC)
		
		// 제조국 설정
		// 기타(1000000990)는 11/28부터 적용 불가, 2023년 1분기까지만 상세설명참조(1000000000) 허용
		paSsgGoods.setOrplcId("1000000990".equals(paSsgGoods.getOrplcId()) ? "1000000000" : paSsgGoods.getOrplcId());
		
		//상품 정보고시
		product.setItemMngPropClsId(paGoodsOfferList.get(0).getPaOfferType());
		// 고시항목
		List<ItemMngAttr> itemMngAttrs = new ArrayList<ItemMngAttr>();
		for(PaGoodsOfferVO offerItem : paGoodsOfferList){
			ItemMngAttr itemMngAttr = new ItemMngAttr();
			itemMngAttr.setItemMngPropId(offerItem.getPaOfferCode());
			// 입력방법코드 (commCd:I063)
			// 10 문자 20 일자 30 선택(코드)
			// 40 여부(Y/N) 50 여부(대상/비대상) (Y/N 으로 입력)
			// 60 자동계산  70 여부(CODE)
			// 입력방법이 여부(Y/N)
			if(offerItem.getIptMthdCd().matches("40|50|70")) {
				if("0000000008".equals(offerItem.getPaOfferCode()) && !"2000000078".equals(paSsgGoods.getOrplcId())) {
					// 수입여부(0000000008)
					itemMngAttr.setItemMngCntt("Y".equals(paSsgGoods.getImportedYn()) ? "Y" : "N");
				}else{
					itemMngAttr.setItemMngCntt("N");	
				}
			// 입력방법이 선택(코드)
			} else if("30".equals(offerItem.getIptMthdCd())){
				if (offerItem.getPaOfferCodeName().contains("제조국")) {
					// 제조국
					itemMngAttr.setItemMngCntt(paSsgGoods.getOrplcId());
				} else {
					// 원산지
					itemMngAttr.setItemMngCntt(convertOriginCode(paSsgGoods.getOrplcId()));
				}
				
			} else {
				itemMngAttr.setItemMngCntt(offerItem.getPaOfferExt().replaceAll("\\p{C}", ""));	
			}
			itemMngAttrs.add(itemMngAttr);

			// 수입여부
			if("0000000008".equals(offerItem.getPaOfferCode()) && "Y".equals(paSsgGoods.getImportedYn())) {
				// 건강기능식품
				if("0000000023".equals(offerItem.getPaOfferType())) {
					//수입여부(0000000008)이고 Y 일 경우 수입자(0000000009) 필수 입력 필요
					itemMngAttr = new ItemMngAttr();
					itemMngAttr.setItemMngPropId("0000000195"); // 수입업소명
					itemMngAttr.setItemMngCntt("상세설명참조");
					itemMngAttrs.add(itemMngAttr);

					itemMngAttr = new ItemMngAttr();
					itemMngAttr.setItemMngPropId("0000000196"); // 수출국명
					itemMngAttr.setItemMngCntt(paSsgGoods.getOrplcId());
					itemMngAttrs.add(itemMngAttr);
				}else {
					//수입여부(0000000008)이고 Y 일 경우 수입자(0000000009) 필수 입력 필요
					itemMngAttr = new ItemMngAttr();
					itemMngAttr.setItemMngPropId("0000000009"); // 수입자
					itemMngAttr.setItemMngCntt("상세설명참조");
					itemMngAttrs.add(itemMngAttr);
					
					//수입여부(0000000008)이고 Y 일 경우 식품위생법에 따른 수입신고(0000000186) 필수 입력 필요
					// 주방용품
					if("0000000017".equals(offerItem.getPaOfferType())) { 
						itemMngAttr = new ItemMngAttr();
						itemMngAttr.setItemMngPropId("0000000186"); // 수입식품안전관리특별법에 따른 수입신고
						itemMngAttr.setItemMngCntt("20");
						itemMngAttrs.add(itemMngAttr);
					}	
				}
			}
		}
		// 농,수,축산물, 가공, 건강기능식품 : 유통기한, 소비기한 여부 추가(필수) 2022-12-21 적용
		ItemMngAttr itemMngAttr = new ItemMngAttr();
		if(product.getItemMngPropClsId().matches("0000000019|0000000020|0000000021|0000000022")) {
			
			itemMngAttr.setItemMngPropId("0000000437"); // 유통기한,소비기한,품질유지기한 여부
			itemMngAttr.setItemMngCntt("0000000086"); // 유통기한으로 넘기게끔 협의
			itemMngAttrs.add(itemMngAttr);
			
		} else if("0000000023".equals(product.getItemMngPropClsId())) {
			
			itemMngAttr.setItemMngPropId("0000000438"); // 유통기한,소비기한 여부
			itemMngAttr.setItemMngCntt("0000000086"); // 유통기한으로 넘기게끔 협의
			itemMngAttrs.add(itemMngAttr);
			
		} else if("0000000007".equals(product.getItemMngPropClsId())) { // 가구
			
			itemMngAttr.setItemMngPropId("0000000441"); // 재공급(리퍼브) 가구 여부
			itemMngAttr.setItemMngCntt("Y");
			itemMngAttrs.add(itemMngAttr);
			
		}
		product.setItemMngAttrs(itemMngAttrs);

		// 농산물, 수산물, 축산물 필수값 세팅
		Map<String, Object> ssgFood = paSsgGoodsService.selectSsgFoodInfo(param);
		
		if(ssgFood != null) {
			product.setSellTotCapa(((BigDecimal)ssgFood.get("SELLTOTCAPA")).intValue()); // 판매 총 용량
			product.setSellUnitCapa(Integer.parseInt((String) ssgFood.get("SELLUNITCAPA"))); // 판매 단위 용량
			product.setSellUnitQty(((BigDecimal) ssgFood.get("SELLUNITQTY")).intValue()); // 판매 단위 수량
			product.setSellCapaUnitCd((String)ssgFood.get("COMMCD"));	// 판매 용량 단위 코드
		}

		product.setManufcoNm(paSsgGoods.getManufcoNm());// 제조사명
		product.setProdManufCntryId(paSsgGoods.getOrplcId()); // 제조국

		product.setDispStrtDts(paSsgGoods.getSsgSaleStartDate());
		product.setDispEndDts(paSsgGoods.getSsgSaleEndDate());		
		product.setSrchPsblYn("Y");
		product.setItemSrchwdNm("SK스토아,에스케이스토아,skstoa,SKSTOA");
		product.setMinOnetOrdPsblQty((int)paSsgGoods.getOrderMinQty());	// 최소 1회 주문 가능 수량
		// 최대 1회 주문 가능 수량
		// 최대주문가능수량이 1일 주문가능수량보다 클 수 없다. 999개로 세팅
		product.setMaxOnetOrdPsblQty(Long.parseLong(paSsgGoods.getTermOrderQty()) < paSsgGoods.getOrderMaxQty()
				? Integer.parseInt(paSsgGoods.getTermOrderQty())
				: (int) paSsgGoods.getOrderMaxQty());
		product.setMax1dyOrdPsblQty(Integer.parseInt(paSsgGoods.getTermOrderQty()));// 1일 주문 가능 수량
		product.setAdultItemTypeCd("1".equals(paSsgGoods.getAdultYn()) ? "10" : "90");
		
		product.setHriskItemYn("N"); // 고위험 상품 여부 
		product.setNitmAplYn("N"); // 신상품 적용 여부	
		product.setBuyFrmCd("60"); // 위수탁
		product.setTxnDivCd("1".equals(paSsgGoods.getTaxYn()) ? "10" : "20"); // 과세 구분 코드
		product.setPrcMngMthd("1"); // 공급가 자동계산 (Default)
		
		// 가격설정
		UitemPrc uitemPrc = new UitemPrc();
		uitemPrc.setSplprc((int)goodsPrice.getBestPrice());
		uitemPrc.setSellprc((int)goodsPrice.getBestPrice());
		uitemPrc.setMrgrt(paSsgGoods.getFeeRate()); // 마진율 
		product.setSalesPrcInfos(Arrays.asList(uitemPrc));
				
		product.setInvMngYn("Y"); // 재고 관리 여부 
		product.setInvQtyMarkgYn("N");	// 재고 수량 표기 여부 
		product.setItemSellTypeCd("20"); // 상품판매유형-옵션 
		product.setItemSellTypeDtlCd("10");	// 상품판매유형상세-일반 
		
		// 옵션
		List<Uitem> uitems = new ArrayList<Uitem>();
		List<UitemPrc> uitemPluralPrcs = new ArrayList<UitemPrc>();

		for(PaSsgGoodsdtMapping optionItem : paGoodsDtMappingList){
			Uitem uitem = new Uitem();
			uitem.setTempUitemId(optionItem.getPaCode() + optionItem.getGoodsCode() + optionItem.getGoodsdtCode()
					+ optionItem.getGoodsdtSeq());
			uitem.setUitemId(optionItem.getPaOptionCode());
			uitem.setUitemOptnTypeNm1("색상/사이즈/형태/무늬"); // 옵션명
			uitem.setUitemOptnNm1(optionItem.getGoodsdtInfo()); // 옵션값
			
			// 전송 재고량이 999,999초과하면 SSG에서 기본값으로 설정함 
			uitem.setBaseInvQty(Integer.parseInt(optionItem.getTransOrderAbleQty()));
			uitem.setUseYn("1".equals(optionItem.getUseYn()) ?  "Y" : "N");
			uitems.add(uitem);
			
			// 옵션가격 
			UitemPrc uitemPluralPrc = new UitemPrc();
			uitemPluralPrc.setTempUitemId(uitem.getTempUitemId());
			uitemPluralPrc.setUitemId(uitem.getUitemId());
			uitemPluralPrc.setSplprc(uitemPrc.getSplprc());
			uitemPluralPrc.setSellprc(uitemPrc.getSellprc());
			uitemPluralPrc.setMrgrt(uitemPrc.getMrgrt());
			uitemPluralPrcs.add(uitemPluralPrc);
		}
		product.setUitems(uitems);
		product.setUitemPluralPrcs(uitemPluralPrcs);
		
		// 배송상품 구분 코드 ( 01: 일반, 03: 설치 유료, 05: 주문제작)
		product.setShppItemDivCd(paSsgGoods.getShppItemDivCd()); 
		
		// 반품불가/설치/주문제작은 반품/교환 불가
		if ("1".equals(paSsgGoods.getNoReturnYn()) || "03".equals(paSsgGoods.getShppItemDivCd())
				|| "05".equals(paSsgGoods.getShppItemDivCd())) {
			product.setRetExchPsblYn("N");
		}else {
			product.setRetExchPsblYn("Y");
		}

		product.setShppMainCd("41"); // 배송주체: 협력업체(41) 
		product.setShppMthdCd("20"); // 택배배송
		product.setMareaShppYn("N"); // 수도권 배송여부
		product.setJejuShppDisabYn("0".equals(paSsgGoods.getNoJejuYn()) ? "N" : "Y"); // 제주도 배송불가 여부
		product.setIsmtarShppDisabYn("0".equals(paSsgGoods.getNoIslandYn()) ? "N" : "Y"); // 도서산간 배송불가 여부

		// 일반상품 3일, 설치/주문제작상품 15일
		product.setShppRqrmDcnt("01".equals(paSsgGoods.getShppItemDivCd()) ? 3 : 15);
		
		product.setSplVenItemId(paSsgGoods.getPaCode() + paSsgGoods.getGoodsCode());// 공급업체상품ID
		product.setWhoutShppcstId(paSsgGoods.getWhoutCost()); // 출고 배송비 ID
		product.setRetShppcstId(paSsgGoods.getRetCost()); // 반품 배송비 ID
		

		product.setIsmtarAddShppcstId(paSsgGoods.getIsCost()); // 도서산간 추가배송비 ID
		product.setJejuAddShppcstId(paSsgGoods.getJejuCost()); // 제주도 추가배송비 ID 

		
		product.setWhoutAddrId(paSsgGoods.getWhoutAddr()); // 출고 주소 ID
		product.setSnbkAddrId(paSsgGoods.getSnbAddr()); // 반품 주소 ID
		
		// 상품이미지
		Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
		String imageServer = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
		String imagePath = imageServer + paSsgGoods.getImageUrl();
		List<ImgInfo> itemImgs = new ArrayList<ImgInfo>();
		itemImgs.add(createImage(imagePath + paSsgGoods.getImageP(), 1));
		if(paSsgGoods.getImageAP() != null) {
			itemImgs.add(createImage(imagePath + paSsgGoods.getImageAP(), 2));
		}
		if(paSsgGoods.getImageBP() != null) {
			itemImgs.add(createImage(imagePath + paSsgGoods.getImageBP(), 3));
		}
		if(paSsgGoods.getImageCP() != null) {
			itemImgs.add(createImage(imagePath + paSsgGoods.getImageCP(), 4));
		}
		if(paSsgGoods.getImageDP() != null) {
			itemImgs.add(createImage(imagePath + paSsgGoods.getImageDP(), 5));
		}
		product.setItemImgs(itemImgs);
		
		// 상세설명
		product.setItemDesc(paSsgGoods.getDescribeExt().replaceAll("\\p{C}", ""));

		product.setGiftPsblYn("N"); // 선물 가능 여부
		product.setPalimpItemYn("N"); // 병행수입 상품 여부
		product.setItemSellWayCd("10"); // 상품 판매 방식 (일반)
		product.setItemStatTypeCd("10"); // 상품 상태 유형 (새상품)
		product.setWhinNotiYn("Y"); // 입고 알림 여부
		
		return product;
	}

	/**
	 * 제조국 코드를 원산지로 변환
	 * 
	 * @param countryId
	 * @return
	 */
	private String convertOriginCode(String countryId) {
		if (countryId == null) return null;
		
		// 한국
		if ("2000000078".equals(countryId)) {
			return "4000000302"; // 국내
		}
		
		return countryId;
	}

	/**
	 * 이미지 전문생성
	 * 
	 * @param imageUrl 이미지URL
	 * @param dataSeq 자료순번
	 * @return
	 */
	private ImgInfo createImage(String imageUrl, int dataSeq) {
		ImgInfo imgInfo = new ImgInfo();
		imgInfo.setDataSeq(dataSeq); // 자료순번
		imgInfo.setDataFileNm(imageUrl); // 자료파일명
		imgInfo.setRplcTextNm("image" + dataSeq); // 대체 텍스트 명
		return imgInfo;
	}
	
	/**
	 * 글자수 제한(90byte)
	 * @param itemNm
	 * @param cutLength
	 * @return
	 */
	private String byteCuter(String itemNm, int cutLength) {
		
		if(itemNm.toString().getBytes().length > cutLength) {
			StringBuilder sb = new StringBuilder(cutLength);
			int nCut = 0;
			for(char ch : itemNm.toString().toCharArray()) {
				nCut += String.valueOf(ch).getBytes().length;
				if(nCut > cutLength) break;
				sb.append(ch);
			}
			return sb.toString() + "..."; // 나머지 부분은 ...으로
		} else {
			return itemNm;
		}
	}

}
