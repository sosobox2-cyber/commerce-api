package com.cware.api.pacommon.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaGoodsTargetRec;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.SpAuthTransInfo;
import com.cware.netshopping.domain.model.SpPaGoodsDescInfo;
import com.cware.netshopping.domain.model.SpPaGoodsDtInfo;
import com.cware.netshopping.domain.model.SpPaGoodsInfo;
import com.cware.netshopping.domain.model.SpPaOfferInfo;
import com.cware.netshopping.domain.PaNoticeApplyVO;
import com.cware.netshopping.pacommon.common.service.PaCommonService;

import io.swagger.annotations.ApiParam;

@Controller("com.cware.api.pacommon.controller")
@RequestMapping(value="/pacommon/common")
public class PaCommonController extends AbstractController {

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
//	@Autowired
//	private Pa11stAsycController pa11stAsyncController;
//	@Autowired
//	private PaGmktV2AsycController paGmktAsyncController;
//	@Autowired
//	private PaNaverAsyncController paNaverAsyncController;
	//@Autowired
	//private PaCopnAsyncController paCopnAsyncController;
//	@Autowired
//	private PaWempAsyncController paWempAsyncController;
//	@Autowired
//	private PaIntpAsyncController paIntpAsyncController;
//	@Autowired
//	private PaLtonAsyncController paLtonAsyncController;
//	@Autowired
//	private PaTmonAsyncController paTmonAsyncController;
//	@Autowired
//	private PaSsgAsyncController paSsgAsyncController;
	//@Autowired
	//private PaKakaoAsyncController paKakaoAsyncController;
	
//	@RequestMapping(value = "/pagoods-auto-insert", method = RequestMethod.GET)
//	@ResponseBody
	public ResponseEntity<?> paGoodsAutoInsert(HttpServletRequest request,
			@ApiParam(name = "paGroupCode", required = false, value = "제휴사그룹코드") @RequestParam(value = "paGroupCode", required = false) String paGroupCode,
			@ApiParam(name = "goodsCode",   required = false, value = "입점대상 상품코드") @RequestParam(value = "goodsCode", required = false) String goodsCode) throws Exception {
		List<PaGoodsTargetRec> paGoodsTargetList = null;
		
		ParamMap paramMap = new ParamMap();
		
		String prg_id = "IF_PACOMMON_00_001";
		String dateTime = "";
		String resultMsg = "";
		String duplicateCheck = "";
		String msg = "";
		
		int resultCnt = 0;
		int totalCnt = 0;
		int eTVLimitMargin = 0;
		try {
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("siteGb", 	  "PARTNER");
			paramMap.put("apiCode",   prg_id);
			paramMap.put("startDate", dateTime);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if ("1".equals(duplicateCheck))	throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			paramMap.put("goodsCode", 	goodsCode);
			paramMap.put("paGroupCode", paGroupCode);
			
			// 입점 대상 상품 정보 조회
			paGoodsTargetList = paCommonService.selectPaGoodsInsertTarget(paramMap);
			totalCnt = paGoodsTargetList.size();
			eTVLimitMargin = paCommonService.selectEtvMarginCheck();
			
			if(paGoodsTargetList.size() <= 0) {
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("errors.no.select"));
			} else {
				for(int i = 0; i < totalCnt; i++) {
					ParamMap paGoodsInfo = new ParamMap();
					String brandNo = "";
					String makerNo = "";
					
					try {
						PaGoodsTargetRec paGoodstarget = paGoodsTargetList.get(i); //한바퀴 돌면 초기화 됨
						
						//초기화
						StringBuffer sb = new StringBuffer();
						goodsCode = paGoodstarget.getGoodsCode(); 
						paGroupCode = paGoodstarget.getPaGroupCode();
						paramMap.put("goodsCode", 	goodsCode);
						paramMap.put("paGroupCode", paGroupCode);
						log.info("GOODS_CODE : " + paGoodstarget.getGoodsCode() + " Start");						
						
						// 01. 정보고시 체크
						resultMsg = paCommonService.selectPaOfferCheck(paGoodstarget);
						sb.append(resultMsg);
						paGoodsInfo.put("offerChk", resultMsg.equals("") ? 1 : 0);
						paGoodsInfo.put("goodsCode", goodsCode);
						
						// 02. 원산지 체크
						if(paGroupCode.equals("01")) {
							if(paGoodstarget.getOriginCode().equals("9999")) {
								paGoodsInfo.put("orgnTypCd", "03");
							} else if(paGoodstarget.getOriginCode().equals("0082")) {
								paGoodsInfo.put("orgnTypCd", "01");
							} else {
								String orgnTypDtlsCd = paCommonService.selectOriginMapping11st(paGoodstarget.getOriginCode());
								paGoodsInfo.put("orgnTypCd", "02");
								paGoodsInfo.put("orgnTypDtlsCd", orgnTypDtlsCd);
							}
						} else if(paGroupCode.equals("02") || paGroupCode.equals("03")) { 
							if(paGoodstarget.getOriginCode().equals("9999")) {
								paGoodsInfo.put("originEnum", "40");
							} else if(paGoodstarget.getOriginCode().equals("0082")) {
								paGoodsInfo.put("originEnum", "20");
							} else {
								String orgnTypDtlsCd = paCommonService.selectOriginMappingGmkt(paGoodstarget.getOriginCode());
								paGoodsInfo.put("originEnum", "30");
								paGoodsInfo.put("orgnTypDtlsCd", orgnTypDtlsCd);
							}
						} else if(paGroupCode.equals("04")) {
							String naverOrgCd = paCommonService.selectOriginMappingNaver(paGoodstarget.getOriginCode());
							paGoodsInfo.put("naverOriginCode", naverOrgCd);
						} else if(paGroupCode.equals("08") || paGroupCode.equals("10") || paGroupCode.equals("11")) {
							String paOrgCd = paCommonService.selectOriginMapping(paGoodstarget);
							paGoodsInfo.put("paOriginCode", paOrgCd);
						}
						
						// 03. 기술서 체크
						resultMsg = paCommonService.selectPaDescribeCheck(paGoodstarget);
						paGoodsInfo.put("describeCode", !resultMsg.equals("") ? "998" : "999"); // 기존 : 연동제한 체크 후 v_describe_chk > 0
						sb.append(resultMsg);
						
						// 04. 이미지 체크
						resultMsg = paCommonService.selectImageCheck(goodsCode);
						sb.append(resultMsg);
						
						// 05. 재고 체크
						resultMsg = paCommonService.selectPaStockCheck(paGoodstarget);
						sb.append(resultMsg);
						
						// 06. 배송비 체크 
						resultMsg = paCommonService.selectPaShipCostCheck(paGoodstarget);
						sb.append(resultMsg);
						
						// 07-1. 마진/최저판매가 체크
						resultMsg = paCommonService.selectChkMinMarSale(paGoodstarget);
						sb.append(resultMsg);
						
						// 07-2. 모바일 eTV 마진 체크
						if("1".equals(paGoodstarget.getMobileEtvYn()) && "61".equals(paGoodstarget.getSourcingMedia()) && paGoodstarget.getMarginRate() < eTVLimitMargin) {
							resultMsg = "모바일 eTV 마진, ";
							sb.append(resultMsg);
						}
						
						// 08. 브랜드, 메이커 체크
						if(paGroupCode.equals("01")) {
							brandNo = paCommonService.selectBrandMapping11st(paGoodstarget.getBrandCode());
						} else if(paGroupCode.equals("02") || paGroupCode.equals("03")) {
							brandNo = paCommonService.selectBrandMappingGmkt(paGoodstarget.getBrandCode());
							makerNo = paCommonService.selectMakerMappingGmkt(paGoodstarget.getMakecoCode());
						} else if(paGroupCode.equals("07") || paGroupCode.equals("08") || paGroupCode.equals("10")) {
							brandNo = paCommonService.selectBrandMapping(paGoodstarget);
						} else if ( paGroupCode.equals("11") ){
							//통합예정
							brandNo = paCommonService.selectBrandMapping(paGoodstarget);
							makerNo = paCommonService.selectMakerMapping(paGoodstarget);
						}
						
						paGoodsInfo.put("brandNo", brandNo);
						paGoodsInfo.put("makerNo", makerNo);
					
						// 09. 원산지 tcode등록여부 확인(B023)
						resultMsg = paCommonService.selectPaOriginCheck(paramMap);
						sb.append(resultMsg);
						
						// 10. 모바일이용권 미대상 확인
						resultMsg = paCommonService.selectPaMobGiftGbCheck(goodsCode);
						sb.append(resultMsg);
						
						// 11. 쿠팡 카테고리별 정보고시 매핑 확인
						if(paGroupCode.equals("05")) {
							int chkGoodsOffer = paCommonService.selectPaCopnCheckGoodsOffer(goodsCode); // 정보고시 설정여부 확인
							paGoodsInfo.put("chkGoodsOffer", chkGoodsOffer >= 1 ? 1 : 0);
							paGoodsInfo.put("paLmsdKey", paGoodstarget.getPaLmsdKey());
							
							if(paGoodsInfo.getInt("offerChk") > 0) {
								String paOfferTypeName = paCommonService.selectPaCopnPaOfferBo(goodsCode);
								paGoodsInfo.put("paOfferTypeName", paOfferTypeName);
								
								int paCtgChk = paCommonService.selectCheckPaCopnAttr(paGoodsInfo);
								paGoodsInfo.put("paCtgChk", paCtgChk);
							} else {
								String paOfferTypeName = paCommonService.selectPaCopnPaOffer(goodsCode);
								paGoodsInfo.put("paOfferTypeName", paOfferTypeName);
								
								int paCtgChk = paCommonService.selectCheckPaCopnAttr(paGoodsInfo);
								paGoodsInfo.put("paCtgChk", paCtgChk);
								
								if(paCtgChk == 0) {
									paGoodsInfo.put("paOfferTypeName", "기타재화");
									int paCtgEtcChk = paCommonService.selectCheckPaCopnAttrEtc(paGoodsInfo); 
									paGoodsInfo.put("paCtgEtcChk", paCtgEtcChk);
								}
								if(paGoodsInfo.getInt("paCtgEtcChk") == 0) {
									String paCtgEtcName = paCommonService.selectPaCopnAttrEtcName(paGoodsInfo);
									paGoodsInfo.put("paCtgEtcName", paCtgEtcName);
								}
							}
						}
						
						// 12. 주소체크
						resultMsg = paCommonService.selectEntpuserCheck(paramMap);
						sb.append(resultMsg);
						
						// 13. 단품갯수체크
						resultMsg = paCommonService.selectPaGoodsdtCnt(paramMap);
						sb.append(resultMsg);
						
						// 14. 옵션명 길이
						resultMsg = paCommonService.selectPaGoodsdtLength(paramMap);
						sb.append(resultMsg);
						
						// 15. 상품명 길이 체크 
						resultMsg = paCommonService.selectPaGoodsNameLength(paramMap);
						sb.append(resultMsg);
						
						if(paGroupCode.equals("10")) {
							// 16. 신세계 정보고시 체크 
							resultMsg = paCommonService.selectPaSsgGoodsOfferCodeCheck(paramMap);
							sb.append(resultMsg);
							
							// 17. 신세계 착불상품 체크
							resultMsg = paCommonService.selectPaSsgGoodsCollectYnCheck(paramMap);
							sb.append(resultMsg);
						}
						
						//연동제한 체크 후 데이터 처리
						if(sb.toString().equals("")) resultMsg = paCommonService.saveTargetTx(paGoodstarget, paGoodsInfo);
						
						if(resultMsg.equals(Constants.SAVE_SUCCESS)) {
							resultCnt++;
						} else {
							paGoodstarget.setRtnMsg(sb.toString());
							paCommonService.inertPaGoodsQaLog(paGoodstarget);
							paCommonService.deletePaGoodsTarget(paGoodstarget);
							resultCnt++;
						}
					} catch(Exception e) {
						paramMap.put("code", "500");
						paramMap.put("message", e.getMessage());
						log.info(e.getMessage() + " | " + goodsCode);
					}
				}
			}
		} catch(Exception e) {
			
			msg = "대상건수:" + totalCnt + ", 성공건수:" + resultCnt;
			
			paramMap.put("code"   , ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(duplicateCheck)) ? "배치가 실행중입니다." : msg + e.getMessage());
			
		} finally {
			try {
				if(!paramMap.getString("code").equals("490")){
					msg = "대상건수:" + totalCnt + ", 성공건수:" + resultCnt + "|";
					
					//대상건수 모두 성공하였을 경우
					if(totalCnt == resultCnt){
						paramMap.put("code","200");
					} else {
						paramMap.put("code","500");
					}
					paramMap.put("message", msg);
				}
				
				systemService.insertApiTrackingTx(request, paramMap);
			} catch(Exception e) {
				log.error("ApiTracking_log Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK); 
	}
	
//	@ApiResponses(value = { 
//			   @ApiResponse(code = 200, message = "OK"), 
//			   @ApiResponse(code = 470, message = "STEP ERROR"),
//			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
//			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
//			   })
//	@RequestMapping(value = "/pagoods-sync-proc", method = RequestMethod.GET)
//	@ResponseBody
	public ResponseEntity<?> paGoodsSyncProc(HttpServletRequest request,
			@ApiParam(name = "goodsCode",   required = false, value = "상품코드") @RequestParam(value = "goodsCode", required = false) String goodsCode) throws Exception {

		List<SpPaOfferInfo>     spPaOfferInfoList       = null;
		List<SpPaGoodsInfo>     spPaGoodsInfoList       = null;
		List<PaPromoTarget>		spPaPromoTargetAllList  = null;
		List<SpPaGoodsDtInfo>   spPaGoodsDtInfoList     = null;
		List<SpAuthTransInfo>   spAuthTransInfoList     = null;
		List<SpPaGoodsDtInfo>   spPaLtonSsgGoodsDtInfoList = null;
		List<SpPaGoodsDescInfo> spPaGoodsDescInfoList   = null;
		List<HashMap<String, String>> goodsPaExceptList = null;
		List<HashMap<String, String>> saleEndDateTargetList = null;
		List<HashMap<String, String>> goodsEventTransInfoList = null;
		List<PaNoticeApplyVO>	spPaNoticeTargetAllList  = null;
		
		ParamMap paramMap = new ParamMap();
		
		String resultCode = "200";
		String dateTime = "";
		String resultMsg = "";
		String duplicateCheck = "";
		String prg_id = "IF_PACOMMON_00_002";
		StringBuffer sb = new StringBuffer();
		
		int totalCnt = 0;
		
		try {
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("siteGb", 	  "PARTNER");
			paramMap.put("apiCode",   prg_id);
			paramMap.put("dateTime",  dateTime);
			paramMap.put("goodsCode", goodsCode);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if ("1".equals(duplicateCheck))	throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			//paCommonService.queryTestFunction();
			//--------------------------------------------------- 처리시작 ---------------------------------------------------//
			
			/* 03/02 부 제외 처리함 */
			
			log.info("자동재입점 선행. CUR_GOODS_AUTH_TRANS Start"); 
			spAuthTransInfoList =  paCommonService.selectGoodsAuthTransList(); 
			totalCnt =  spAuthTransInfoList.size();			  
			if(totalCnt > 0) {
			   int failCnt = 0;
			   try {
				   	  for ( int i = 0; i < totalCnt ; i++) { 
					  SpAuthTransInfo authTransInfo = spAuthTransInfoList.get(i);
					  log.info("자동재입점 선행. CUR_GOODS_AUTH_TRANS GOODS_CODE : " +  authTransInfo.getGoodsCode()); authTransInfo.setDateTime(dateTime);
					  authTransInfo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			  
					  resultMsg = paCommonService.saveGoodsAuthTransInfoTx(authTransInfo);
					  } 
				} catch(Exception e) { 
					  log.info("자동재입점 선행. CUR_GOODS_AUTH_TRANS ERROR : " +  e.getMessage()); resultCode = "470"; failCnt++; 
				}
				String sbMsg =  "자동재입점 선행 TOTAL : " + totalCnt + ", FAIL : " + failCnt + " | ";
				sb.append(sbMsg);
			}
			log.info("자동재입점 선행. CUR_GOODS_AUTH_TRANS end");
			
			log.info("상품 연동제외 관리. CUR_GOODS_PA_EXCEPT Start");
			goodsPaExceptList = paCommonService.selectGoodsPaExceptList(paramMap); // AK백화점(업체코드:109153) 11번가, 이베이는 입점 가능해서 조회되지 않게 수정함
			totalCnt = goodsPaExceptList.size();
			if(totalCnt > 0) {
				int failCnt = 0;
				try {
					for(HashMap<String, String> goodsPaExceptData : goodsPaExceptList) {
						log.info("상품 연동제외 관리. CUR_GOODS_PA_EXCEPT GOODS_CODE" + goodsPaExceptData.get("GOODS_CODE").toString());
						goodsPaExceptData.put("dateTime", dateTime);
						
						resultMsg = paCommonService.saveGoodsPaExceptTx(goodsPaExceptData);
					}
				} catch(Exception e) {
					log.info("상품 연동제외 관리. CUR_GOODS_PA_EXCEPT ERROR : " + e.getMessage());
					resultCode = "470";
					failCnt++;
				}
				String sbMsg = "상품 연동제외 관리 TOTAL : " + totalCnt + ", FAIL : " + failCnt + " | ";
				sb.append(sbMsg);
			}
			log.info("상품 연동제외 관리. CUR_GOODS_PA_EXCEPT End");
			
			log.info("행사상품 재입점. CUR_GOODS_EVENT_TRANS Start");
			goodsEventTransInfoList = paCommonService.selectGoodsEventTransInfoList();
			totalCnt = goodsEventTransInfoList.size();
			if(totalCnt > 0) {
				int failCnt = 0;
				try {
					for(HashMap<String, String> goodsEventTransData : goodsEventTransInfoList) {
						log.info("행사상품 재입점. CUR_GOODS_EVENT_TRANS GOODS_CODE" + goodsEventTransData.get("GOODS_CODE").toString());
						goodsEventTransData.put("dateTime", dateTime);
						
						resultMsg = paCommonService.saveGoodsEventTransInfoTx(goodsEventTransData);
					}
				} catch(Exception e) {
					log.info("행사상품 재입점. CUR_GOODS_EVENT_TRANS ERROR : " + e.getMessage());
					resultCode = "470";
					failCnt++;
				}
				String sbMsg = "행사상품 재입점 TOTAL : " + totalCnt + ", FAIL : " + failCnt + " | ";
				sb.append(sbMsg);
			}
			log.info("행사상품 재입점. CUR_GOODS_EVENT_TRANS end"); 
			
			
			// 상품 고시 동기화
			log.info("STEP 1. CUR_OFFER_INFO Start");
			spPaOfferInfoList = paCommonService.selectOfferInfoList(paramMap);
			int failOfferCnt 	= 0;
			Set<HashMap<String, String>> transTargetSet  = new HashSet<HashMap<String, String>>();
			Set<HashMap<String, String>> saleGbStopSet	 = new HashSet<HashMap<String, String>>(); 
			
			try {
				for(SpPaOfferInfo paOfferData : spPaOfferInfoList) {  //하나 실패하면 나머지 모두 안돌아가는 문제 발생함.. 추후 개선 필요
					paOfferData.setDateTime(dateTime);
					log.info("STEP 1 CUR_OFFER_INFO GOODS_CODE : " + paOfferData.getGoodsCode());
					resultMsg = paCommonService.saveOfferInfoTx(paOfferData, transTargetSet, saleGbStopSet);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("STEP 1 Fail > GOODS_CODE : " + paOfferData.getGoodsCode());
						failOfferCnt++;
						continue;
					}
					
				}
				paCommonService.saveOfferGoodsTarget(transTargetSet, saleGbStopSet); //UPDATE TPAGOODS_SALE_GB , TRANS_TARGET (PA11STGOODS, PAGMKTGOODS,.... ETC~)			
				
			}catch (Exception e) {
				log.info("STEP 1 ERROR : " + e.getMessage());
				resultCode = "470";
				failOfferCnt++;
			}finally {
				String sbMsg = "STEP 1. CUR_OFFER_INFO TOTAL : " + spPaOfferInfoList.size() + ", FAIL : " + failOfferCnt + " | ";
				sb.append(sbMsg);				
			}
			log.info("STEP 1. CUR_OFFER_INFO end");
				
			
			// 상품 정보 동기화
			log.info("STEP 2. CUR_GOODS_INFO Start");
			spPaGoodsInfoList = paCommonService.selectGoodsInfoList(paramMap);
			totalCnt = spPaGoodsInfoList.size();
			if(totalCnt > 0) {
				int failCnt = 0;
				try {
					for(int i = 0; i < totalCnt; i++) {
						SpPaGoodsInfo paGoodsData = spPaGoodsInfoList.get(i);
						log.info("STEP 2. CUR_GOODS_INFO GOODS_CODE " + paGoodsData.getGoodsCode());
						paGoodsData.setDateTime(dateTime);
						resultMsg = paCommonService.saveGoodsInfoTx(paGoodsData);
						
						if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
							log.info("STEP 2 Fail > GOODS_CODE : " + paGoodsData.getGoodsCode());
							continue;
						}
					}
				} catch(Exception e) {
					log.info("STEP 2 ERROR : " + e.getMessage());
					resultCode = "470";
					failCnt++;
				}
				String sbMsg = "STEP 2. CUR_GOODS_INFO TOTAL : " + totalCnt + ", FAIL : " + failCnt + " | ";
				sb.append(sbMsg);
			}
			log.info("STEP 2. CUR_GOODS_INFO end");
			
			
			// 단품 정보 동기화  
			log.info("STEP 3. CUR_GOODSDT_INFO Start");
			spPaGoodsDtInfoList = paCommonService.selectGoodsDtInfoList(paramMap);
			totalCnt = spPaGoodsDtInfoList.size();
			if(totalCnt > 0) {
				String flag = "";			  //Step3
				String compareGoodsCode = ""; //Step3
				int failCnt = 0;
				try {
					for(int i = 0; i < totalCnt; i++) {
						SpPaGoodsDtInfo paGoodsDtInfo = spPaGoodsDtInfoList.get(i);
						log.info("STEP 3. CUR_GOODSDT_INFO GOODS_CODE " + paGoodsDtInfo.getGoodsCode());
						paGoodsDtInfo.setDateTime(dateTime);
						if(compareGoodsCode.equals("") || !compareGoodsCode.equals(paGoodsDtInfo.getGoodsCode())) {
							flag = "Y";
						} else {
							compareGoodsCode = paGoodsDtInfo.getGoodsCode();
						}
						
						resultMsg = paCommonService.saveGoodsDtInfoTx(paGoodsDtInfo, flag);
						
						if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
							log.info("STEP 3 Fail > GOODS_CODE : " + paGoodsDtInfo.getGoodsCode());
							continue;
						}
					}
				} catch(Exception e) {
					log.info("STEP 3 ERROR : " + e.getMessage());
					resultCode = "470";
					failCnt++;
				}
				String sbMsg = "STEP 3. CUR_GOODSDT_INFO TOTAL : " + totalCnt + ", FAIL : " + failCnt + " | ";
				sb.append(sbMsg);
			}
			log.info("STEP 3. CUR_GOODSDT_INFO end");
			
			// 롯데ON, SSG 단품명 변경
			log.info("STEP 3-1. CUR_UPDATE_LTON,SSG_GOODSDT_INFO Start");
			spPaLtonSsgGoodsDtInfoList = paCommonService.selectPaLtonSsgGoodsDtInfoList(paramMap);
			totalCnt = spPaLtonSsgGoodsDtInfoList.size();

			if(totalCnt > 0) {
				int failCnt = 0;
				try {
					for(int i = 0; i < totalCnt; i++) {
						SpPaGoodsDtInfo paLtonSsgGoodsDtInfo = spPaLtonSsgGoodsDtInfoList.get(i);
						log.info("STEP 3-`. CUR_UPDATE_LTON,SSG_GOODSDT_INFO GOODS_CODE " + paLtonSsgGoodsDtInfo.getGoodsCode());
						paLtonSsgGoodsDtInfo.setDateTime(dateTime);
						resultMsg = paCommonService.updateLtonSsgGoodsDtInfoTx(paLtonSsgGoodsDtInfo);							
						
						if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
							log.info("STEP 3-1 Fail > GOODS_CODE : " + paLtonSsgGoodsDtInfo.getGoodsCode());
							continue;
						}
					}
				} catch(Exception e) {
					log.info("STEP 3-1 ERROR : " + e.getMessage());
					resultCode = "470";
					failCnt++;
				}
				String sbMsg = "STEP 3-1. CUR_UPDATE_LTONSSG_GOODSDT_INFO TOTAL : " + totalCnt + ", FAIL : " + failCnt + " | ";
				sb.append(sbMsg);
			}
			log.info("STEP 3-1. CUR_UPDATE_LTONSSG_GOODSDT_INFO end");
			
			// 기술서 수정일시 동기화
			log.info("STEP 4. CUR_GOODS_DESCRIBE Start");
			spPaGoodsDescInfoList = paCommonService.selectPaGoodsDescInfoList(paramMap);
			totalCnt = spPaGoodsDescInfoList.size();
			if(totalCnt > 0) {
				int failCnt = 0;
				try {
					for(int i = 0; i < totalCnt; i++) {
						SpPaGoodsDescInfo paGoodsDescInfo = spPaGoodsDescInfoList.get(i);
						log.info("STEP 4. CUR_GOODS_DESCRIBE GOODS_CODE " + paGoodsDescInfo.getGoodsCode());
						paGoodsDescInfo.setDateTime(dateTime);
						paGoodsDescInfo.setLastDescribeSyncDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						resultMsg = paCommonService.saveGoodsDescInfoTx(paGoodsDescInfo);
						
						if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
							log.info("STEP 4 Fail > GOODS_CODE : " + paGoodsDescInfo.getGoodsCode());
							continue;
						}
					}
				} catch(Exception e) {
					log.info("STEP 4 ERROR : " + e.getMessage());
					resultCode = "470";
					failCnt++;
				}
				String sbMsg = "STEP 4. CUR_GOODS_DESCRIBE TOTAL : " + totalCnt + ", FAIL : " + failCnt + " | ";
				sb.append(sbMsg);
			}
			log.info("STEP 4. CUR_GOODS_DESCRIBE end");
			
			// 판매가 기준병결을 위한 즉시할인 프로모션 동기화(11번가, 이베이, 네이버, 쿠팡, 위메프, 인터파크)
			log.info("STEP 5-3. CUR_PAPROMO_TARGET_ALL Start");
			spPaPromoTargetAllList = paCommonService.selectPaPromoTargetAllList();
			totalCnt = spPaPromoTargetAllList.size();
			if(totalCnt > 0) {
				int failCnt = 0;
				try {
					for(int i = 0; i < totalCnt; i++) {
						PaPromoTarget paPromotargetAllData = spPaPromoTargetAllList.get(i);
						log.info("STEP 5-3. CUR_PAPROMO_TARGET_ALL GOODS_CODE " + paPromotargetAllData.getGoodsCode());
						paPromotargetAllData.setDateTime(dateTime);
						resultMsg = paCommonService.savePaPromoTargetAllTx(paPromotargetAllData);
						
						if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
							log.info("STEP 5-3 Fail > GOODS_CODE : " + paPromotargetAllData.getGoodsCode());
							continue;
						}
					}
				} catch(Exception e) {
					log.info("STEP 5-3 ERROR : " + e.getMessage());
					resultCode = "470";
					failCnt++;
				}
				String sbMsg = "STEP 5-3. CUR_PAPROMO_TARGET_ALL TOTAL : " + totalCnt + ", FAIL : " + failCnt + " | ";
				sb.append(sbMsg);
			}
			log.info("STEP 5-3. CUR_PAPROMO_TARGET_ALL end");
			
			// 제휴공지사항 데이터 동기화 처리
			log.info("STEP 5-4. CUR_PANOTICE_TARGET Start");
			spPaNoticeTargetAllList = paCommonService.selectPaNoticeTargetList();
			totalCnt = spPaNoticeTargetAllList.size();
			if(totalCnt > 0) {
				int failCnt = 0;
				try {
					for(int i = 0; i < totalCnt; i++) {
						PaNoticeApplyVO paNoticeAllData = spPaNoticeTargetAllList.get(i);
						paNoticeAllData.setDateTime(dateTime);
						resultMsg = paCommonService.savePaNoticeTargetAllTx(paNoticeAllData);
						
						if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
							log.info("STEP 5-4 Fail > GOODS_CODE : " + paNoticeAllData.getGoodsCode());
							failCnt++;
							continue;
						}
						log.info("STEP 5-4. CUR_PANOTICEM_TARGET_ALL GOODS_CODE " + paNoticeAllData.getGoodsCode());
					}
				} catch(Exception e) {
					log.info("STEP 5-4 ERROR : " + e.getMessage());
					resultCode = "470";
					failCnt++;
				}
				String sbMsg = "STEP 5-4. CUR_PANOTICEM_TARGET_ALL TOTAL : " + totalCnt + ", FAIL : " + failCnt + " | ";
				sb.append(sbMsg);
			}
			log.info("STEP 5-4. CUR_PANOTICEM_TARGET_ALL end");

			//Step.5-6 판매가 기준변경을 위한 즉시할인 프로모션 동기화 이게 프로시져 안에 있는데 도대체 몰까?????
			
			// 판매중인 상품 판매종료일 검증
			log.info("STEP 6. CUR_SALE_END_DATE_TARGET Start");
			saleEndDateTargetList = paCommonService.selectSaleEndDateTargetList();
			totalCnt = saleEndDateTargetList.size();
			if(totalCnt > 0) {
				int failCnt = 0;
				try {
					for(HashMap<String, String> saleEndDateTarget : saleEndDateTargetList) {
						log.info("STEP 6. CUR_SALE_END_DATE_TARGET GOODS_CODE " + saleEndDateTarget.get("GOODS_CODE").toString());
						saleEndDateTarget.put("dateTime", dateTime);
						resultMsg = paCommonService.saveSaleEndDateTargetInfoTx(saleEndDateTarget);
						
						if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
							log.info("STEP 6 Fail > GOODS_CODE : " + saleEndDateTarget.get("GOODS_CODE"));
							continue;
						}
					}
				} catch(Exception e) {
					log.info("STEP 6 ERROR : " + e.getMessage());
					resultCode = "470";
					failCnt++;
				}
				String sbMsg = "STEP 6. CUR_SALE_END_DATE_TARGET TOTAL : " + totalCnt + ", FAIL : " + failCnt + " | ";
				sb.append(sbMsg);
			}
			log.info("STEP 6. CUR_SALE_END_DATE_TARGET end");
			
//			pa11stAsyncController.spPagoodsSync11st(request, goodsCode, paramMap.get("siteGb").toString());
//			paGmktAsyncController.spPagoodsSyncEbay(request, goodsCode, paramMap.get("siteGb").toString());
//			paNaverAsyncController.spPagoodsSyncNaver(request, goodsCode, paramMap.get("siteGb").toString());
//			paCopnAsyncController.spPagoodsSyncCopn(request, goodsCode, paramMap.get("siteGb").toString());
//			paWempAsyncController.spPagoodsSyncWemp(request, goodsCode, paramMap.get("siteGb").toString());
//			paIntpAsyncController.spPagoodsSyncIntp(request, goodsCode, paramMap.get("siteGb").toString());
//			paLtonAsyncController.spPagoodsSyncLton(request, goodsCode, paramMap.get("siteGb").toString());
//			paTmonAsyncController.spPagoodsSyncTmon(request, goodsCode, paramMap.get("siteGb").toString());
//			paSsgAsyncController.spPagoodsSyncSsg(request, goodsCode, paramMap.get("siteGb").toString());
			//paKakaoAsyncController.spPagoodsSyncKakao(request, goodsCode, paramMap.get("siteGb").toString());
			
			paramMap.put("message", sb.toString());
			//--------------------------------------------------- 처리 끝 ---------------------------------------------------//
			
		} catch(Exception e) {
			resultCode = ("1".equals(duplicateCheck)) ? "490" : "500" ;
			paramMap.put("message", ("1".equals(duplicateCheck)) ? "배치가 실행중입니다." : e.getMessage() + sb.toString());
		} finally {
			try {
				paramMap.put("code", resultCode);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch(Exception e) {
				log.error("ApiTracking_log Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK); 
	}
}