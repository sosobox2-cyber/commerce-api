package com.cware.netshopping.paintp.v2.service;

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
import com.cware.netshopping.common.code.ShipCostFlag;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.PaIntpGoodsVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaIntpGoods;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.service.SyncProductService;
import com.cware.netshopping.paintp.goods.service.PaIntpGoodsService;
import com.cware.netshopping.paintp.v2.domain.Info;
import com.cware.netshopping.paintp.v2.domain.Item;
import com.cware.netshopping.paintp.v2.repository.PaIntpGoodsMapper;

@Service
public class PaIntpProductService {

	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;

	@Autowired
	@Qualifier("paintp.goods.paIntpGoodsService")
	private PaIntpGoodsService paIntpGoodsService;

	@Autowired
	PaIntpProductApiService productApiService;

	@Autowired
	InterparkProductService interparkProductService;

	@Autowired
	SyncProductService syncProductService;

	@Autowired
	PaIntpGoodsMapper paIntpGoodsMapper;

	@Autowired
	PaIntpResultService paIntpResultService;
	
	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;

	@Autowired
	TransLogService transLogService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 인터파크 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public ResponseMsg transProduct(String goodsCode, String paCode, String procId) {

		ResponseMsg result = new ResponseMsg("", "");

		// 상품동기화
		PaGoodsSync sync = syncProductService.syncProduct(goodsCode, PaGroup.INTERPARK.code(), procId);
		result = sync.getResponseMsg();
		if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) {
			return result;
		}

		// 스토아상품조회
		PaIntpGoods paIntpGoods = paIntpGoodsMapper.getGoods(goodsCode, paCode);

		if (paIntpGoods == null) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("연동 대상 상품이 아닙니다.");
			return result;
		}

		// 입점요청중인건은 처리하지 않음
		if (PaStatus.PROCEEDING.code().equals(paIntpGoods.getPaStatus())) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("입점요청중인 상품입니다.");
			return result;
		}

		// 입점요청/입점반려건에 대해서 신규입점 요청
		if (PaStatus.REQUEST.code().equals(paIntpGoods.getPaStatus())
				|| PaStatus.REJECT.code().equals(paIntpGoods.getPaStatus())) {

			// 동기화에서 필터링된 경우
			if (String.valueOf(HttpStatus.NO_CONTENT.value()).equals(result.getCode())) {
				return result;
			}

			if (goodsTargetMapper.existsGoodsTarget(goodsCode, paCode, PaGroup.INTERPARK.code()) < 1) {
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

		boolean isUpdated = false;
		boolean isStock = false;

		if ("1".equals(paIntpGoods.getTransTargetYn()) || "1".equals(paIntpGoods.getTransSaleYn())) {
			// 상품수정
			ResponseMsg updated = updateProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(updated.getCode())) {
				isUpdated = true;
				result.setMessage("수정완료되었습니다.");
			} else
				return updated;
		} else {
			// 재고변경
			transService = interparkProductService.updateStockProduct(goodsCode, paCode, procId);
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
	 * 인터파크 신규상품 입점
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
		serviceLog.setServiceNote("[API]인터파크-상품입점");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.INTERPARK.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 상품입점
		ResponseMsg result = callRegisterProduct(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		// 입점요청중 해제
		paIntpGoodsMapper.updateClearProceeding(goodsCode, paCode, procId);

		return result;

	}

	/**
	 * 인터파크 상품 수정
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
		serviceLog.setServiceNote("[API]인터파크-상품수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.INTERPARK.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 상품수정
		ResponseMsg result = callUpdateProduct(goodsCode, paCode, procId, serviceLog.getTransServiceNo());

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;

	}

	private ResponseMsg callRegisterProduct(String goodsCode, String paCode, String procId, long transServiceNo) {
		ParamMap paramMap = new ParamMap();

		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		PaIntpGoodsVO paIntpGoods = null;

		try {
			log.info("===== 상품등록서비스 Start - {} =====", goodsCode);

			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
			paramMap.put("modCase", "INSERT");
			paIntpGoods = paIntpGoodsService.selectPaIntpGoodsInfo(paramMap);

			if (paIntpGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paIntpGoods.getPrdNo() != null) {
				result.setCode("411");
				result.setMessage("이미 등록된 상품입니다. 인터파크 상품코드 : " + paIntpGoods.getPrdNo());
				return result;
			}
			if (paIntpGoods.getDescribeExt() == null) {
				paIntpGoods.setDescribeExt("");
				if (paIntpGoods.getGoodsCom() == null && PaSaleGb.FORSALE.code().equals(paIntpGoods.getPaSaleGb())) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", paIntpGoods.getPaGroupCode());

			// 정보고시 조회
			List<PaGoodsOffer> goodsOffer = paIntpGoodsService.selectPaIntpGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.INTERPARK.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 입점요청중
			paIntpGoodsMapper.updateProceeding(goodsCode, paCode, procId);

			// 상품설명설정
			settingDescribeExt(paIntpGoods);

			// 단품 옵션 조회
			List<PaGoodsdtMapping> goodsdtMapping = paIntpGoodsService.selectPaIntpGoodsdtInfoList(paramMap);

			paIntpGoods.setModifyId(procId);

			// 인터파크 상품 전문
			Item item = createProduct(paIntpGoods, goodsdtMapping, goodsOffer, goodsPrice, transServiceNo);

			log.info("상품등록 API 호출 {}", goodsCode);
			String prdNo = productApiService.registerProduct(goodsCode, item, paCode, transServiceNo);

			if (StringUtils.hasText(prdNo)) {

				result.setCode("200");
				result.setMessage(PaIntpApiRequest.API_SUCCESS_CODE);

				paIntpGoods.setPrdNo(prdNo);

				rtnMsg = paIntpResultService.saveTransProduct(paIntpGoods, goodsPrice, goodsdtMapping);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + paIntpGoods.getGoodsCode());
				}

			} else {
				result.setCode("500");
				result.setMessage("no data found");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

			paIntpGoodsMapper.rejectTransTarget(goodsCode, paCode, procId,
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

		PaIntpGoodsVO paIntpGoods = null;

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
			paIntpGoods = paIntpGoodsService.selectPaIntpGoodsInfo(paramMap);

			if (paIntpGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paIntpGoods.getPrdNo() == null) {
				result.setCode("411");
				result.setMessage("등록되지 않은 상품입니다. 상품코드 : " + goodsCode);
				return result;
			}
			if (paIntpGoods.getDescribeExt() == null) {
				paIntpGoods.setDescribeExt("");
				if (paIntpGoods.getGoodsCom() == null && PaSaleGb.FORSALE.code().equals(paIntpGoods.getPaSaleGb())) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			paramMap.put("paGroupCode", paIntpGoods.getPaGroupCode());

			// 정보고시 조회
			List<PaGoodsOffer> goodsOffer = paIntpGoodsService.selectPaIntpGoodsOfferList(paramMap);
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.INTERPARK.code(), paCode);
			if (goodsPrice == null && PaSaleGb.FORSALE.code().equals(paIntpGoods.getPaSaleGb())) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 상품설명설정
			settingDescribeExt(paIntpGoods);

			// 단품 옵션 조회
			List<PaGoodsdtMapping> goodsdtMapping = paIntpGoodsService.selectPaIntpGoodsdtInfoList(paramMap);

			paIntpGoods.setModifyId(procId);

			// 인터파크 상품 전문
			Item item = createProduct(paIntpGoods, goodsdtMapping, goodsOffer, goodsPrice, transServiceNo);
			if (item == null) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
				return result;
			}

			log.info("상품수정 API 호출 {}", goodsCode);
			String prdNo = productApiService.updateProduct(goodsCode, item, paCode, transServiceNo);

			if (StringUtils.hasText(prdNo)) {

				result.setCode("200");
				result.setMessage(PaIntpApiRequest.API_SUCCESS_CODE);

				paIntpGoods.setPrdNo(prdNo);

				rtnMsg = paIntpResultService.saveTransProduct(paIntpGoods, goodsPrice, goodsdtMapping);

				if (!rtnMsg.equals("000000")) {
					result.setCode("500");
					result.setMessage(rtnMsg + " : " + paIntpGoods.getGoodsCode());
				}

			} else {
				result.setCode("500");
				result.setMessage("no data found");
			}
		} catch (TransApiException ex) {
			
			if (paIntpResultService.applyRetention(goodsCode, paCode, procId, ex.getMessage())) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
				return result;
			}
			
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

			if (isRejectMsg(result.getMessage())) {
				// 수기중단 사유가 "판매금지"인 경우 판매중지 타겟팅 하지 않음.
				String transSaleYn = result.getMessage().contains("판매금지") ? "0" : "1";
				// 특정 메시지에 대해서 수기중단 (레거시는 수기중단처리 없음)
				paIntpGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
						StringUtil.truncate(result.getMessage(), 500), transSaleYn);
				// 금칙어 오류 체크
				if(result.getMessage().contains("금칙어")) {
					paIntpGoodsMapper.updateProhibitidYn(goodsCode, paCode, procId);
				}
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

		String[] rejectMatch = new String[] { "특수문자 입력 불가"
				, "판매금지"
				, "유효하지 않은 파일 타입"
				, "같은 이름의 옵션항목명이 존재"
				, "데이터 입력오류"
				, "중고상품 관련 키워드"
				, "성인상품 관련 키워드"
				, "금칙어"
				};

		return Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s));
	}

	/**
	 * 기술서와 공지사항 설정
	 * 
	 * @param paIntpGoods
	 */
	private void settingDescribeExt(PaIntpGoodsVO paIntpGoods) {

		// 상품구성템플릿
		String goodsCom = StringUtils.hasText(paIntpGoods.getGoodsCom()) ? paIntpGoods.getGoodsCom() : "";

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
		paIntpGoods.setDescribeExt(
				"<div align='center'><img alt='' src='" + paIntpGoods.getTopImage() + "' /><br /><br /><br />" // 상단이미지
						+ (StringUtils.hasText(paIntpGoods.getCollectImage())
								? "<img alt='' src='" + paIntpGoods.getCollectImage() + "' /><br /><br /><br />"
								: "") // 착불이미지
						+ goodsCom // 상품구성
						+ paIntpGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />" // 기술서
						+ "<img alt='' src='" + paIntpGoods.getBottomImage() + "' /></div>"); // 하단이미지

		// 제휴 공지사항
		if (StringUtils.hasText(paIntpGoods.getNoticeExt())) {
			paIntpGoods.setDescribeExt(
					paIntpGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + paIntpGoods.getDescribeExt());
		}
	}

	private Item createProduct(PaIntpGoodsVO paIntpGoods, List<PaGoodsdtMapping> paGoodsDtMappingList,
			List<PaGoodsOffer> paGoodsOfferList, PaGoodsPriceApply goodsPrice, long transServiceNo) throws Exception {

		Item item = new Item();

		item.setPrdNo(paIntpGoods.getPrdNo());

	    //상품상태 - 새상품:01, 중고상품:02, 반품상품:03  
		item.setPrdStat("01"); //새상품 고정
		//상점번호
		item.setShopNo("0000100000");

	    //판매중:01, 품절:02, 판매중지:03, 일시품절:05, 예약판매:09, 상품삭제:98
	    item.setSaleStatTp(PaSaleGb.FORSALE.code().equals(paIntpGoods.getPaSaleGb()) ? "01" : "03");

	    if (PaSaleGb.FORSALE.code().equals(paIntpGoods.getPaSaleGb())) {
			// 전시코드
			item.setOmDispNo(paIntpGoods.getPaLmsdKey());
			
			//상품명 - 한글 60자 (영문/숫자 120자)
			if("005222".equals(paIntpGoods.getStoaBrandCode()) || "013538".equals(paIntpGoods.getStoaBrandCode())) {
				// 2023-12-14 브랜드 : '무스너클' or '오너클랜'일 경우 상품명에서 제거 후 연동 
				item.setPrdNm(StringUtil.truncate(
						paIntpGoods.getGoodsName().replaceAll("\\[무스너클\\]", "").replaceAll("\\[오너클랜\\]", "")
												  .replaceAll("[{}<>\\'\"^/|]", " ").trim().replaceAll("\\s+", " "), 120));
			} else {
				item.setPrdNm(StringUtil.truncate(
						paIntpGoods.getGoodsName().replaceAll("[{}<>\\'\"^/|]", " ").trim().replaceAll("\\s+", " "), 120));
			}
		    
		    if("1".equals(paIntpGoods.getCollectYn())) {
		    	item.setProddelvCostUseYn("Y");
		    	item.setDelvCost((long)100);
		    	//배송비결제방식 착불:01, 선불:02, 선불전환착불가능:03
		    	item.setDelvAmtPayTpCom("01");
		    	//착불 배송비 노출 여부 - Y : 노출, N : 비노출
		    	item.setDelvCostDispYn("N");
		    	//배송비 적용 방식 - 개당:01, 무조건:02, n개당:03
		    	item.setDelvCostApplyTp("02");
		    	//무료배송기준 수량 - 기준수량 입력 사용하지 않을 경우 0
		    	item.setFreedelvStdCnt(0);
		    	//n개당 배송 주문 수량 - 기준수량 입력 사용하지 않을 경우 0
		    	item.setStdQty(0);
				// 착불배송일경우 상품명앞에 착불표기 추가 21.11.05
				item.setPrdNm("(착불)" + item.getPrdNm());
		    } else {
		    	//상품배송비사용여부 - 상품배송비사용:Y, 업체배송비정책사용:N 
		    	item.setProddelvCostUseYn("N");
		    	//묶음배송비번호
		    	item.setDelvPlcNo(paIntpGoods.getGroupCode());
		    }
	
		    //제조업체명 
		    item.setHdelvMafcEntrNm(paIntpGoods.getMakecoName());
		    //원산지 
		    if("60".equals(paIntpGoods.getLgroup())) { // 대카테고리 식품일 경우 '상세설명참조' 연동 (2024.02.29)
		    	item.setPrdOriginTp("상세설명참조");
		    } else {
		    	item.setPrdOriginTp(paIntpGoods.getOriginName());
		    }
		    //부가면세상품 - 과세상품:01, 면세상품:02, 영세상품:03
		    if("1".equals(paIntpGoods.getTaxSmallYn())) {
		    	item.setTaxTp("03");
		    }else if ("1".equals(paIntpGoods.getTaxYn())) {
		    	item.setTaxTp("01");
		    }else {
		    	item.setTaxTp("02");
		    }
	
		    //성인용품여부 - 성인용품:Y, 일반용품:N 
		    item.setOrdAgeRstrYn("1".equals(paIntpGoods.getAdultYn())? "Y" : "N");
		    //판매수량 - 99999 개 이하로 입력 
		    item.setSaleLmtQty(paIntpGoods.getTransOrderAbleQty());
		    //판매시작일 - yyyyMMdd
		    item.setSaleStrDts(paIntpGoods.getIntpSaleStartDate());
		    //판매종료일 - yyyyMMdd
		    item.setSaleEndDts(paIntpGoods.getIntpSaleEndDate());
		    
		    //상품 반품택배비 사용여부
		    item.setPrdrtnCostUseYn("Y");
		    //상품 반품배송지번호. 업체 반품정책 사용하지 않을때 지정 
		    item.setRtndelvNo(paIntpGoods.getPaAddrSeq());
		    
		    long returnCost = (long)paIntpGoods.getReturnCost();
	
			if (paIntpGoods.getShipCostCode().contains(ShipCostFlag.FREE.code())) {
				item.setRtndelvCost(returnCost > 0 ? returnCost/2 : 0);
			} else {
			    item.setRtndelvCost(returnCost);
			}
	
			// 상품설명
			item.setPrdBasisExplanEd(paIntpGoods.getDescribeExt().replaceAll("\\p{C}", ""));
			
		    //쇼핑태그 - 최대 4개까지, 콤마로 구분	
		    item.setPrdKeywd("sk스토아,skstoa,에스케이스토아");
		    
		    //브랜드번호 
		    item.setBarcode(paIntpGoods.getBrandNo());
		    //브랜드명 
		    item.setPrdModelNo(paIntpGoods.getBrandName());
		    //1회당 주문 제한 수량
		    item.setPerordRstrQty((int)paIntpGoods.getOrderMaxQty());
	
	
			// 상품이미지
			Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
			String imageServer = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
			String imagePath = imageServer + paIntpGoods.getImageUrl();
			
			// 인터파크 개발서버에서 스토아 이미지 개발서버 접근불가로 임시 이미지 등록 처리
			boolean isTest = imageServer.equals("http://dev-image.skstoa.com");
			String testImage = "http://10.96.201.139/Upload/goods_image/org/3815906459.jpg";
			String testImage1 = "http://10.96.201.139/Upload/goods_image/org/3815876453.jpg";
			String testImage2 = "http://10.96.201.139/Upload/goods_image/org/3815876458.png";
	
					
		    //대표이미지 - 대표이미지 URL, 영문/숫자 조합, JPG와 PNG만 가능 
		    item.setZoomImg(imagePath + paIntpGoods.getImageP());
		    if (isTest)	item.setZoomImg(testImage);
	
		    // 상세이미지
		    List<String> detailImages = new ArrayList<>();
		    if(paIntpGoods.getImageAP()!=null) {
		    	detailImages.add(isTest ? testImage1 : imagePath + paIntpGoods.getImageAP());
		    }
		    if(paIntpGoods.getImageBP()!=null) {
		    	detailImages.add(isTest ? testImage2 : imagePath + paIntpGoods.getImageBP());
		    }
		    if(paIntpGoods.getImageCP()!=null) {
		    	detailImages.add(isTest ? testImage : imagePath + paIntpGoods.getImageCP());
		    }
		    
			if (detailImages.size() > 0) {
				//JPG와 PNG만 가능 최대 3개의 이미지까지, 콤마(,)로 구분하여 등록.
				item.setDetailImg(String.join(",", detailImages));
			}
	
		    //이미지 변경여부
			// 2023-07-20 이미지서버 클렌징 반영 시점차이로 이미지 변경건이 반영되지 않는 현상이 있어 무조건 이미지 update 바라보도록 수정
//			item.setImgUpdateYn("1".equals(paIntpGoods.getImageTransYn()) ? "Y" : "N");
			item.setImgUpdateYn("Y");
	
			// 선택형 옵션
		    StringBuilder sbOpt = new StringBuilder();
		    // {옵션명수량<재고>옵션코드<단품코드>사용여부<Y/N>}
			String optionFormat = "{%s수량<%s>옵션코드<%s>사용여부<%s>}";
			int MAX_STOCK = 99_999;
		    for(int i=0; i<paGoodsDtMappingList.size(); i++) {
		    	if(i == 0) {
		    		item.setSelOptName(paGoodsDtMappingList.get(i).getGoodsdtInfoKind().replaceAll("/$", ""));
		    		//선택형 옵션노출 정렬 유형 01-등록순, 02-가나다순. 선택형 옵션만 적용됨.
		    		item.setOptPrirTp("02");
		    	}
				sbOpt.append(String.format(optionFormat,
						paGoodsDtMappingList.get(i).getGoodsdtInfo().replaceAll("[{}<>\\'\"^/|]", " ").trim()
								.replaceAll("\\s+", " "),
						Integer.parseInt(paGoodsDtMappingList.get(i).getTransOrderAbleQty()) > MAX_STOCK
								? String.valueOf(MAX_STOCK)
								: paGoodsDtMappingList.get(i).getTransOrderAbleQty(),
						paGoodsDtMappingList.get(i).getGoodsdtCode(),
						SaleGb.FORSALE.code().equals(paGoodsDtMappingList.get(i).getSaleGb()) ? "Y" : "N"));
		    			    	
		    }
		    item.setPrdOption(sbOpt.toString());
	
		    // TODO 레거시 도서산간추가배송비로 설정되어 있지 않음. 확인
			long jejuDelvCost = (long)paIntpGoods.getJejuCost() - (long)paIntpGoods.getOrdCost();
			long etcDelvCost = (long)paIntpGoods.getIslandCost() - (long)paIntpGoods.getOrdCost();
			
		    //제주도서산간배송비사용여부 - Y : 등록/수정, N : 사용안함
		    if(jejuDelvCost > 0 || etcDelvCost > 0) {
		    	item.setJejuetcDelvCostUseYn("Y");
		    	//제주배송비 - jejuetcDelvCostUseYn가 Y일때 제주배송비와 도서산간비 둘 중 하나는 필수 
		    	item.setJejuDelvCost(jejuDelvCost > 0 ? jejuDelvCost : 0);
		    	//도서산간배송비 - jejuetcDelvCostUseYn가 Y일때 제주배송비와 도서산간비 둘 중 하나는 필수
		    	item.setEtcDelvCost(etcDelvCost > 0 ? etcDelvCost : 0);
		    }else {
		    	item.setJejuetcDelvCostUseYn("N");
		    }
	
		    //원상품번호(제휴업체상품코드)
		    item.setOriginPrdNo(paIntpGoods.getGoodsCode());
		    //A/S정보
		    item.setAsInfo("상세설명참조");
	
		    //해외구매대행 또는 해외항공배송 상품
		    item.setAbroadBsYn("N");
	
		    //상품인증여부 생활용품/전기용품/방송통신용품/어린이제품에 대한 인증 여부 (의료기기, 건강기능식품, 제조/가공식품 인증 대상 제외)
		    if("1".equals(paIntpGoods.getLifeCertYn()) || "1".equals(paIntpGoods.getElectricCertYn()) || "1".equals(paIntpGoods.getChildCertYn())) {
		    	item.setPrdCertStatus("S");
		    }    		
		    
		    if("1".equals(paIntpGoods.getMedicalYn())) {
		    	item.setMedicalCertTp("0303");
		    }
		    
		    if("1".equals(paIntpGoods.getHealthYn())) {
		    	item.setHealthCertDtlTp("0403");
		    }
		    
		    if("1".equals(paIntpGoods.getFoodYn())) {
		    	item.setFoodCertTp("0503");
		    }
	
		    // 정보고시
		    List<Info> prdinfoNoti = new ArrayList<Info>();
	
			for (PaGoodsOffer goodsOffer : paGoodsOfferList) {
				Info info = new Info();
				info.setInfoSubNo(goodsOffer.getPaOfferCode());
				info.setInfoCd("N".equals(goodsOffer.getRemark1()) ? "N" : "I");
				info.setInfoTx(goodsOffer.getPaOfferExt().replaceAll("\\p{C}", ""));
				prdinfoNoti.add(info);
			}
			item.setPrdinfoNoti(prdinfoNoti);
	    } else {
	    	if("1".equals(paIntpGoods.getProhibitidYn())) {
	    		// 금칙어 판매중지
	    		item.setPrdNm("상품명 금칙어 사용으로 수기중단처리");
	    	} else {
	    		// 판매중지 연동시 상품명 한글/영어/숫자/공백 외 모두 제거
				item.setPrdNm(StringUtil.truncate(
						paIntpGoods.getGoodsName().replaceAll("[^\uAC00-\uD7A30-9a-zA-Z\\s]", " ").trim().replaceAll("\\s+", " "), 120));
	    	}
		    //성인용품여부 - 성인용품:Y, 일반용품:N 
		    item.setOrdAgeRstrYn("1".equals(paIntpGoods.getAdultYn())? "Y" : "N");
	    }
	    
		if (goodsPrice != null) {
		    //판매가 
		    item.setSaleUnitcost((long) goodsPrice.getSalePrice());
		    
			double dcAmt = goodsPrice.getSalePrice() - goodsPrice.getBestPrice();
			if (dcAmt > 0) {
		    	item.setEntrDcUseYn("Y");		
		    	//판매자부담 즉시할인 할인구분, 정액
		    	item.setEntrDcTp("2");
		    	//판매자부담 즉시할인 할인률/할인금액
		    	item.setEntrDcNum(dcAmt);
			} else {
		    	item.setEntrDcUseYn("N");	
			}
		} else { 
			// 판매중지인 경우 프로모션개선 데이터가 존재하지 않을 수 있으므로 레거시 가격 설정
		    item.setSaleUnitcost((long)paIntpGoods.getSalePrice());
		}
		
		// 2023-09-14 설치배송 상품 : 배송방법 화물배달 셋팅
		if ("1".equals(paIntpGoods.getInstallYn())) {
			item.setDelvMthd("03");
		}

		return item;
	}

}
