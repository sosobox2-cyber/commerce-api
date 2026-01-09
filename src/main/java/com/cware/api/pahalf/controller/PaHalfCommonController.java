package com.cware.api.pahalf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.PaHalfShipInfoVO;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaHalfBrand;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.pahalf.common.service.PaHalfCommonService;
import com.cware.netshopping.pahalf.util.PaHalfAdvConnectUtil;
import com.cware.netshopping.pahalf.util.PaHalfComUtill;
import com.cware.netshopping.pahalf.util.PaHalfConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//공통코드 조회
@Api(value="/pahalf/common", description="하프클럽 공통")
@Controller("com.cware.api.pahalf.PaHalfCommonController")
@RequestMapping("/pahalf/common")
public class PaHalfCommonController extends AbstractController {

	@Resource(name = "common.system.systemService")
	public SystemService systemService;

	@Resource(name = "pahalf.common.paHalfCommonService")
	public PaHalfCommonService paHalfCommonService;

	@Autowired
	private PaHalfConnectUtil paHalfConnectUtil;
	
	@Autowired
	private PaHalfAdvConnectUtil paHalfAdvConnectUtil;
	
	@Autowired
	private TransLogService transLogService;
	
	/**
	 * 공통코드 조회
	 * @param request
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCodeList(HttpServletRequest request, String code) throws Exception {
		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		String prg_id = "IF_PAHALFAPI_00_001";

		// =Step 1)API Setting
		paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅

		// =Step 2)DATA Setting
		apiDataMap.put("grpCd", code);
		apiInfoMap.put("queryString", PaHalfComUtill.makeQuryString(apiDataMap));

		// =Step 3)Connect and Get API Response
		apiInfoMap.put("paCode", Constants.PA_HALF_BROAD_CODE);
		Map<String, Object> resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
		
		// =API DATA GET
		List<Map<String, Object>> apiResultList = PaHalfComUtill.map2List(resultMap);

		return apiResultList;
	}
	
	/**
	 * 고시정보조회
	 * @param request
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "고시정보조회", notes = "고시정보조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/get-offer-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getOfferList(HttpServletRequest request) throws Exception {	
		
		ParamMap apiInfoMap = new ParamMap();
		String prg_id = "IF_PAHALFAPI_00_001";

		try {
			// =Step 1)중복체크
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);
			// =Step 2)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			
			List<Map<String, Object>> apiResultList = getCodeList(request, "NO000");
			//각 고시 타입별 리스트 담는 용
			List<Map<String, Object>> offerList = new ArrayList<>();	
			
			//정보고시 담는 용
			List<PaOfferCode> paOfferCodeList = new ArrayList<PaOfferCode>(); 
			
			for (Map<String, Object> resultData : apiResultList) {
				
				offerList = getCodeList(request,String.valueOf(resultData.get("dtlsCd")));
				
				for(Map<String, Object> paOffer : offerList) {
					
					PaOfferCode paOfferCode = new PaOfferCode();
					paOfferCode.setPaGroupCode(Constants.PA_HALF_GROUP_CODE);
					paOfferCode.setPaOfferType(String.valueOf(paOffer.get("grpCd")));
					paOfferCode.setPaOfferTypeName(String.valueOf(paOffer.get("grpCdNm")));
					paOfferCode.setPaOfferCode(String.valueOf(paOffer.get("dtlsCd")));
					paOfferCode.setPaOfferCodeName(String.valueOf(paOffer.get("dtlsComNm")));
					paOfferCode.setUseYn(("Y").equals(String.valueOf(paOffer.get("useYn"))) ?  "1":"0");
					paOfferCode.setSortSeq("0");
					paOfferCode.setUseYn("1");
					paOfferCode.setUnitRequiredYn("0");
					paOfferCode.setRequiredYn("1");
					paOfferCode.setInsertId(Constants.PA_HALF_PROC_ID);
					paOfferCode.setModifyId(Constants.PA_HALF_PROC_ID);
					paOfferCode.setRemark(String.valueOf(paOffer.get("dtlsComNm")));
					
					paOfferCodeList.add(paOfferCode);
				}
			}
			String rtnMsg = paHalfCommonService.savePaHalfOfferCode(paOfferCodeList);
			

			if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
				apiInfoMap.put("code","404");
				apiInfoMap.put("message",rtnMsg);
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}

			
		} catch (Exception e) {
			log.info("{}: {}", "하프클럽 고시정보조회 오류", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);

		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
			log.info("===== 고시정보 조회 API End =====");
		}

		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 * 표준아이템(카테고리) 조회 API
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "표준아이템(카테고리) 조회 API", notes = "표준아이템(카테고리) 조회 API", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goodsKinds-stdItem-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getGoodsKindsStdItemList(HttpServletRequest request) throws Exception {

		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		String prg_id 		= "IF_PAHALFAPI_00_002";
	
		try { 
			// =Step 1)중복체크
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);
			// =Step 2)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅

			// =Step 3)DATA Setting
			apiDataMap.put("siteCd", "1");
			apiDataMap.put("hgrnkCtgrNo", "0");
			apiInfoMap.put("queryString", PaHalfComUtill.makeQuryString(apiDataMap));

			// =Step 4)Connect and Get API Response
			apiInfoMap.put("paCode", Constants.PA_HALF_BROAD_CODE);
			Map<String, Object> resultMapL 	= paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
			
			log.info("===== 표준아이템(카테고리) 조회 API Start =====");
			// =API DATA GET
			List<Map<String, Object>> stdItemListL = PaHalfComUtill.map2List(resultMapL);
			// 표준카테고리 조회 결과 저장 리스트
			List<PaGoodsKinds> stdItemList = new ArrayList<>();
			
			//3depth 조회 시작
			for (Map<String, Object> i : stdItemListL) {
				//카테고리 번호, 카테고리 명
				String stdCtgrNoL = String.valueOf(i.get("stdCtgrNo"));
				String stdCtgrNmL = (String)i.get("stdCtgrNm");
				
				List<Map<String, Object>> stdItemListM = null;
				if(!"0".equals(stdCtgrNoL) && !"-1".equals(stdCtgrNoL)){ //카테고리 번호가 0일경우 문제발생
					stdItemListM = getStdItemList(apiInfoMap, apiDataMap, stdCtgrNoL);
				}
				else {
					continue;
				}
				
				for (Map<String, Object> j : stdItemListM) {
					String stdCtgrNoM = String.valueOf(j.get("stdCtgrNo"));
					String stdCtgrNmM = (String)j.get("stdCtgrNm");
					
					List<Map<String, Object>> stdItemListS = null;
					if(!"0".equals(stdCtgrNoM) && !"-1".equals(stdCtgrNoM)){
						stdItemListS = getStdItemList(apiInfoMap, apiDataMap, stdCtgrNoM);
					}
					else {
						continue;
					}
					for (Map<String, Object> k : stdItemListS) {
						String stdCtgrNoS = String.valueOf(k.get("stdCtgrNo"));
						String stdCtgrNmS = (String)k.get("stdCtgrNm");
						String useYn = (String)k.get("useYn");
						
						stdItemList.add(stdItemMapping(stdCtgrNoL, stdCtgrNmL, stdCtgrNoM, stdCtgrNmM, stdCtgrNoS, stdCtgrNmS, useYn));
					}

				}
			}
			
			log.info(paHalfCommonService.saveStdItemList(stdItemList));

		} catch (Exception e) {
			log.info("{}: {}", "하프클럽 표준아이템조회 오류", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
			log.info("===== 표준아이템(카테고리) 조회 API End =====");
		}

		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}

	// 표준 아이템 depth connect
	public List<Map<String, Object>> getStdItemList(ParamMap apiInfoMap, ParamMap apiDataMap, String stdCtgrNo) throws Exception {
		apiDataMap.put("hgrnkCtgrNo", stdCtgrNo);
		apiInfoMap.put("queryString", PaHalfComUtill.makeQuryString(apiDataMap));
		Map<String, Object> resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
		Map<String, String> apiResultMap = PaHalfComUtill.getApiResult(resultMap);
		
		if("200".equals(String.valueOf(apiResultMap.get("code")))) return PaHalfComUtill.map2List(resultMap);
		else throw processException("pa.connect_error", new String[] {"PaHalfCommonController.getGoodsKindsStdItemList"});
	}

	public PaGoodsKinds stdItemMapping(String stdCtgrNoL, String stdCtgrNmL, String stdCtgrNoM, String stdCtgrNmM, String stdCtgrNoS, String stdCtgrNmS, String useYn) {
			
		PaGoodsKinds goodsKinds = new PaGoodsKinds();
		goodsKinds.setPaGroupCode(Constants.PA_HALF_GROUP_CODE);
		goodsKinds.setPaLgroup(stdCtgrNoL);
		goodsKinds.setPaLgroupName(stdCtgrNmL);
		goodsKinds.setPaMgroup(stdCtgrNoM);
		goodsKinds.setPaMgroupName(stdCtgrNmM);
		goodsKinds.setPaSgroup(stdCtgrNoS);
		goodsKinds.setPaSgroupName(stdCtgrNmS);
		goodsKinds.setPaLmsdKey(stdCtgrNoS);
		goodsKinds.setUseYn(("N".equals(useYn))? "0" : "1");
		goodsKinds.setModifyId(Constants.PA_HALF_PROC_ID);
		goodsKinds.setInsertId(Constants.PA_HALF_PROC_ID);
		
		return goodsKinds;
	}
	

	/**
	 * 상세 브랜드 목록 조회
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상세 브랜드 목록 조회", notes = "상세 브랜드 목록 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/brand-detail-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> brandDetailList(HttpServletRequest request) throws Exception {

		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		String prg_id       = "IF_PAHALFAPI_00_003";
		String paCode       = "";
		String returnNoYn   = "";
		String useYn		= "";
		Map<String, Object> resultMap 		    = null;
		List<Map<String, Object>> apiResultList = null;

		try {
			// =Step 1)중복체크
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);
			// =Step 2)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅

			// =Step 3)DATA Setting
			apiDataMap.put("siteCd", "1");
			apiInfoMap.put("queryString", PaHalfComUtill.makeQuryString(apiDataMap));

			log.info("===== 상세 브랜드 목록 조회 API Start =====");
			
			//=Step 4) 하프클럽 통신 및 데이터 저장
			for(int i = 0; i < Constants.PA_HALF_CONTRACT_CNT ; i++ ) {
				//ㄴ1.통신
				paCode = (i==0 )? Constants.PA_HALF_BROAD_CODE : Constants.PA_HALF_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				
				//Connect and Get API Response
				resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
				apiResultList = PaHalfComUtill.map2List(resultMap);
				
				// 브랜드리스트
				List<PaHalfBrand> paHalfBrandList = new ArrayList<PaHalfBrand>();
				
				if ("200".equals(PaHalfComUtill.getApiResult(resultMap).get("code"))) { 
					
					for(Map<String, Object> resultData : apiResultList) {
						PaHalfBrand paHalfBrand = new PaHalfBrand();
						paHalfBrand.setPaGroupCode(Constants.PA_HALF_GROUP_CODE);
						paHalfBrand.setPaCode     (paCode);
						paHalfBrand.setPaBrandNo  (String.valueOf(resultData.get("prdGroupNo")));
						paHalfBrand.setPaDetailBrandNo(String.valueOf(resultData.get("prdGroupCd")));
						String paBrandName = String.valueOf(resultData.get("prdGroupNm"));
						paHalfBrand.setPaBrandName(paBrandName);
						if( (paBrandName.contains("교환")) && (paBrandName.contains("반품")) && (paBrandName.contains("불가")) ) {
							returnNoYn = "1";
						}else {
							returnNoYn = "0";
						}
						paHalfBrand.setReturnNoYn(returnNoYn);
						useYn = ("Y").equals(String.valueOf(resultData.get("useYn"))) ? "1" : "0";
						paHalfBrand.setUseYn     (useYn);
						paHalfBrand.setInsertId  (Constants.PA_HALF_PROC_ID);
						paHalfBrand.setModifyId  (Constants.PA_HALF_PROC_ID);

						paHalfBrandList.add(paHalfBrand);
					}

					// 브랜드 저장
					String rtnMsg = paHalfCommonService.savePaHalfBrand(paHalfBrandList);
					log.info(rtnMsg);

					
				}else {//하프클럽 통신 실패
					throw processException("pa.connect_error", new String[] {PaHalfComUtill.getApiResult(resultMap).get("message") });
				}
			}
			
			// 브랜드 매핑
			paHalfCommonService.insertPaHalfBrandMapping();
			
			// 신규 브랜드 상품 타겟 등록
			//halfBrandFilterGoodsInsert();
			
		} catch (Exception e) {
			log.info("{}: {}", "하프클럽 상세 브랜드 목록 조회 오류", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);

		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
			log.info("===== 상세 브랜드 목록 조회 API End =====");
		}

		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")),HttpStatus.OK);

	}

	//동기화 시 브랜드 필터 걸렸던 상품 tpagoodstarget에 insert
	private void halfBrandFilterGoodsInsert() throws Exception {		
		List<HashMap<String,Object>> brandFilterGoodsList=  null;

		brandFilterGoodsList = paHalfCommonService.selectBrandFilterGoods();
		if(brandFilterGoodsList.size() > 0) paHalfCommonService.savePaHalfGoodsTarget(brandFilterGoodsList);
	
	}

	/**
     * 배송템플릿 등록
     * @return ResponseEntity
     * @throws Exception
     */
	@ApiOperation(value = "배송템플릿 저장", notes = "배송템플릿 저장", httpMethod = "POST", produces = "application/json")
	@RequestMapping(value = "/entpslip-insert", method = RequestMethod.POST)
	@ResponseBody
    public ResponseEntity<?> entpSlipInsert(HttpServletRequest request
    	  ,	@ApiParam(name="entpCode"			, required=true , value="업체코드"				) @RequestParam(value="entpCode"			, required=true ) String entpCode
    	  , @ApiParam(name="shipManSeq"			, required=true , value="출고담당순번"			) @RequestParam(value="shipManSeq"			, required=true ) String shipManSeq
    	  , @ApiParam(name="returnManSeq"		, required=true	, value="회수담당순번"			) @RequestParam(value="returnManSeq"		, required=true ) String returnManSeq
    	  , @ApiParam(name="shipCostCode"		, required=true	, value="회수담당순번"			) @RequestParam(value="shipCostCode"		, required=true ) String shipCostCode
    	  , @ApiParam(name="paCode"				, required=true	, value="제휴사코드"			) @RequestParam(value="paCode"				, required=true ) String paCode
    	  , @ApiParam(name="noShipJejuIsland"	, required=true	, value="도서산간제주배송불가여부"	) @RequestParam(value="noShipJejuIsland"	, required=true ) String noShipJejuIsland
    	  , @ApiParam(name = "transBatchNo"		, required=false, value = "상품연동번호", defaultValue="999999") @RequestParam(value = "transBatchNo", required = false, defaultValue="999999") long transBatchNo
    	  , @ApiParam(name = "procId"			, required=false, value = "처리자ID", defaultValue="ENHALFAPI") @RequestParam(value = "procId", required = false, defaultValue="ENHALFAPI") String procId
    		) throws Exception  {
		
		ParamMap apiInfoMap		= new ParamMap();
		ParamMap apiDataMap		= new ParamMap();
		ParamMap paramMap   	= new ParamMap();
		Map<String, String> apiResultMap = null;
		String   prg_id 		= "IF_PAHALFAPI_00_004";
		String   paShipCostCode = "";
		//Validation_Check
		paramMap.put("entpCode"			, entpCode);
		paramMap.put("shipManSeq"		, shipManSeq);
		paramMap.put("returnManSeq"		, returnManSeq);
		paramMap.put("shipCostCode"		, shipCostCode);
		paramMap.put("paCode"			, paCode);
		paramMap.put("noShipJejuIsland" , noShipJejuIsland);
		
		HashMap<String,Object> halfShipCost	= paHalfCommonService.selectPaShipCostCode(paramMap);
		if(halfShipCost != null ) {
			paShipCostCode = String.valueOf(halfShipCost.get("PA_SHIP_COST_CODE"));
			return new ResponseEntity<>(new ResponseMsg("200", paShipCostCode ), HttpStatus.OK);  //MESSAGE(paShipCostCode) 바꾸지 말것!!!
		}
		
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(entpCode + shipManSeq + returnManSeq + shipCostCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]하프클럽-배송템플릿등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.HALF.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		try {
			//=Step 1)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			
			//=Step 2) INSERT TPAHALFSHIPINFO
			paramMap.put("paCode", paCode);
			paHalfCommonService.insertTpaHalfShipInfo(paramMap);
			
			//=Step 3) Setting DataMap for Sending HalfClub
			PaHalfShipInfoVO halfShipCostInfo = paHalfCommonService.selectHalfShipCostInfo(paramMap);
			paHalfCommonService.setPaShipCostInfo(halfShipCostInfo ,apiDataMap);
			
			//=Step 4) Connect HalfClub and Send Data
			apiInfoMap.put("paCode", paCode);
			
//			신규 통신모듈로 대체
//			Map<String, Object> resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
			Map<String, Object> resultMap = paHalfAdvConnectUtil.registerSellerAddress(entpCode, shipManSeq, returnManSeq, shipCostCode, transBatchNo, apiInfoMap, apiDataMap);
			apiResultMap = PaHalfComUtill.getApiResult(resultMap);
			
			if(!"200".equals(apiResultMap.get("code"))) throw processException("errors.process_fail", new String[] {"배송템플릿 데이터 송 수신 실패"});
			
			//=Step 5) UPDATE TPAHALFSHIPINFO.PA_SHIP_COST_CODE , LAST_SYNC_DATE, TARGET_YN , MODIFY_DATE
			Map<String,String> dataMap 		= PaHalfComUtill.getApiData(resultMap);
			halfShipCostInfo.setPaShipCostCode(String.valueOf(dataMap.get("result")));
			paHalfCommonService.updateTpaHalfShipInfo(halfShipCostInfo);
			paShipCostCode = halfShipCostInfo.getPaShipCostCode();
			apiInfoMap.put("message", paShipCostCode);

		}catch (Exception e) {
			apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
			log.info("{}: {} 업체: {} 출고지코드: {} 회수지코드: {} 배송비정책: {} PA_CODE: {}", "하프클럽 배송템플릿 저장 오류", PaHalfComUtill.getErrorMessage(e), 
					entpCode, shipManSeq, returnManSeq, shipCostCode, paCode );
			paHalfConnectUtil.checkException(apiInfoMap, e);
		} finally {
			serviceLog.setResultCode(apiResultMap.get("code"));
			serviceLog.setResultMsg(apiResultMap.get("message"));
			transLogService.logTransServiceEnd(serviceLog);
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
     * 배송템플릿 수정
     * @return ResponseEntity
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "배송템플릿 수정", notes = "배송템플릿 수정", httpMethod = "GET", produces = "application/json")
	@RequestMapping(value = "/entpslip-modify", method = RequestMethod.GET)
	@ResponseBody
    public ResponseEntity<?> entpSlipModify(HttpServletRequest request
    	  ,	@ApiParam(name="entpCode"		, required=false , value="업체코드") @RequestParam(value="entpCode"	, required=false ) String entpCode
    	  , @ApiParam(name = "transBatchNo"	, required = false	, value = "상품연동번호", defaultValue="999999") @RequestParam(value = "transBatchNo", required = false, defaultValue="999999") long transBatchNo 
    	  , @ApiParam(name = "procId"			, required=false, value = "처리자ID", defaultValue="ENHALFAPI") @RequestParam(value = "procId", required = false, defaultValue="ENHALFAPI") String procId
    		) throws Exception  {
		
		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		ParamMap paramMap	= new ParamMap();
		String prg_id = "IF_PAHALFAPI_00_005"; //배송템플릿 수정 
		List<HashMap<String,Object>> halfShipInfoModifyList=  null;
		Map<String, String> apiResultMap = null;
		int failCnt  = 0;
		int totalCnt = 0;
		
		try {
			//=Step 0)Duplicate Check
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);
			//=Step 1)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			
			//=Step 2) TPAHALFSHIPINFO MODIFY LIST
			halfShipInfoModifyList = paHalfCommonService.selectPaHalfSlipInfoList(paramMap); //PaHalfSlipInfo.targetYn = 1
			totalCnt = halfShipInfoModifyList.size();
			for(HashMap<String,Object> halfShipInfo : halfShipInfoModifyList) {
				
				//=서비스로그 생성
				PaTransService serviceLog = new PaTransService();
				serviceLog.setTransCode(String.valueOf(halfShipInfo.get("ENTP_CODE"))+ String.valueOf(halfShipInfo.get("SHIP_MAN_SEQ")) + String.valueOf(halfShipInfo.get("RETURN_MAN_SEQ"))  + String.valueOf(halfShipInfo.get("SHIP_COST_CODE")) );
				serviceLog.setTransType(TransType.SHIPCOST.name());
				serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
				serviceLog.setServiceNote("[API]하프클럽-배송템플릿수정");
				serviceLog.setTransBatchNo(transBatchNo);
				serviceLog.setPaGroupCode(PaGroup.HALF.code());
				serviceLog.setProcessId(procId);
				transLogService.logTransServiceStart(serviceLog);
				
				try {
					
					halfShipInfo = (HashMap<String, Object>) PaHalfComUtill.replaceCamel(halfShipInfo);
					paramMap.setParamMap(halfShipInfo);

					//=Step 3) Setting DataMap for Sending HalfClub
					PaHalfShipInfoVO halfShipCostInfo = paHalfCommonService.selectHalfShipCostInfo(paramMap);
					paHalfCommonService.setPaShipCostInfo(halfShipCostInfo ,apiDataMap);
					
					//=Step 4) Connect HalfClub and Send Data
					apiInfoMap.put("paCode", halfShipCostInfo.getPaCode());
					
//					신규 통신모듈로 대체 
//					Map<String, Object> resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
					Map<String, Object> resultMap =  paHalfAdvConnectUtil.registerSellerAddress(halfShipCostInfo.getEntpCode() , halfShipCostInfo.getShipManSeq() ,halfShipCostInfo.getReturnManSeq() , halfShipCostInfo.getShipCostCode() , transBatchNo, apiInfoMap, apiDataMap);
					
					apiResultMap 		= PaHalfComUtill.getApiResult(resultMap);
					
					if(!"200".equals(apiResultMap.get("code"))) throw processException("errors.process_fail", new String[] {"배송템플릿 데이터 송 수신 실패"});
					
					//=Step 5) UPDATE TPAHALFSHIPINFO.PA_SHIP_COST_CODE , LAST_SYNC_DATE, TARGET_YN , MODIFY_DATE
					Map<String,String> dataMap 		= PaHalfComUtill.getApiData(resultMap);
					halfShipCostInfo.setPaShipCostCode(String.valueOf(dataMap.get("result")));
					paHalfCommonService.updateTpaHalfShipInfo(halfShipCostInfo);	
					
				} catch (Exception e) {
					apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
					log.info("{}: {} 업체: {} 출고지코드: {} 회수지코드: {} 배송비정책: {} PA_CODE: {}", "하프클럽 배송템플릿 수정 오류", PaHalfComUtill.getErrorMessage(e), 
							paramMap.getString("entpCode"), paramMap.getString("shipManSeq"), paramMap.getString("returnManSeq"), paramMap.getString("shipCostCode"), paramMap.getString("paCode"));
					failCnt++;
				}finally {
					serviceLog.setResultCode(apiResultMap.get("code"));
					serviceLog.setResultMsg(apiResultMap.get("message"));
					transLogService.logTransServiceEnd(serviceLog);
				}
			}
			//실패건에 대한 throw(알림) 처리
			if(failCnt > 0 ) {
				throw processException("errors.detail", new String[] {"하프클럽 배송템플릿 수정 오류  - 실패:" + failCnt + "건 전체:" + totalCnt + "건"});
			}
			
		}catch (Exception e) {
			log.info("{}: {}", "하프클럽 배송템플릿 수정", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
}
