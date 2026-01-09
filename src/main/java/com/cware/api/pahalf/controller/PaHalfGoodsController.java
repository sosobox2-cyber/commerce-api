package com.cware.api.pahalf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.PaStatus;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.PaHalfGoodsVO;
import com.cware.netshopping.domain.PaHalfGoodsdtMappingVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaHalfGoods;
import com.cware.netshopping.domain.model.PaHalfGoodsdtMapping;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pahalf.common.service.PaHalfCommonService;
import com.cware.netshopping.pahalf.goods.service.PaHalfGoodsService;
import com.cware.netshopping.pahalf.util.PaHalfAdvConnectUtil;
import com.cware.netshopping.pahalf.util.PaHalfComUtill;
import com.cware.netshopping.pahalf.util.PaHalfConnectUtil;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/pahalf/goods", description="하프클럽 상품")
@Controller("com.cware.api.pahalf.PaHalfGoodsController")
@RequestMapping(value = "/pahalf/goods")
public class PaHalfGoodsController extends AbstractController {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	@Resource(name = "pahalf.common.paHalfCommonService")
	private PaHalfCommonService paHalfCommonService;

	@Resource(name = "paHalf.goods.paHalfGoodsService")
	private PaHalfGoodsService paHalfGoodsService;

	@Resource(name = "com.cware.api.pahalf.PaHalfCommonController")
	private PaHalfCommonController paHalfCommonController;

	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;	
	
	@Autowired
	private PaHalfConnectUtil paHalfConnectUtil;
	
	@Autowired
	private PaHalfAdvConnectUtil paHalfAdvConnectUtil;
	
	@Autowired
	private TransLogService transLogService;
	
    @ApiOperation(value = "상품등록")
    @ApiResponses(value = {
               @ApiResponse(code = 200, message = "제휴상품으로 등록되었습니다."), 
               @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
               @ApiResponse(code = 410, message = "이미 등록된 상품입니다."),
               @ApiResponse(code = 420, message = "기술서를 입력하세요."),
               @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다")
               })
    @RequestMapping(value = "/goods-insert", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> goodsInsert(HttpServletRequest request,
            @ApiParam(name="goodsCode"		, required=false	, value="상품코드"			, defaultValue="") 		 @RequestParam(value="goodsCode"	, required=false	, defaultValue="") 		 String goodsCode
          , @ApiParam(name="paCode"			, required=false	, value="제휴사코드"		, defaultValue="") 		 @RequestParam(value="paCode"		, required=false	, defaultValue="")		 String paCode
          //, @ApiParam(name="inComingUrl"	, required=false	, value ="BO호출구분"		, defaultValue="") 		 @RequestParam(value="inComingUrl"	, required=false	, defaultValue = "") 	 String inComingUrl
          , @ApiParam(name="searchTermGb"	, required=false	, value="실행중복체크여부"	, defaultValue="0") 	 @RequestParam(value="searchTermGb"	, required=false	, defaultValue="insert") String searchTermGb
          , @ApiParam(name = "transBatchNo"	, required=false	, value = "상품연동번호", defaultValue="999999") @RequestParam(value = "transBatchNo", required = false, defaultValue="999999") long transBatchNo
          , @ApiParam(name = "procId"		, required=false	, value = "처리자ID", defaultValue="") @RequestParam(value = "procId", required = false, defaultValue="") String procId) throws Exception{
    	
		log.info("===== 하프클럽 상품 등록 API Start =====");
		ParamMap apiInfoMap = new ParamMap();
		ParamMap paramMap	= new ParamMap();
		String prg_id 		= "IF_PAHALFAPI_01_001";
		int	failCnt			= 0;
		
		paramMap.put("goodsCode"	, goodsCode);
		paramMap.put("paCode"		, paCode);
		paramMap.put("searchTermGb"	, searchTermGb);
		
		try {
			// =Step 1) API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			// =Step 2) 중복체크
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);
						
			// =Step 3) 대상조회
			List<HashMap<String, String>> paGoodsTargetList = paHalfGoodsService.selectPaHalfGoodsTrans(paramMap);
			
			// =Step 4) Validation Check -> goodsInsert
			for(HashMap<String, String> halfGoods : paGoodsTargetList) {
				int chkOk = paHalfGoodsService.goodsValidationCheck	(halfGoods.get("PA_CODE") , halfGoods.get("GOODS_CODE"));
				if(chkOk != 1 ) continue;
				
				ResponseEntity<?> responseMsg  = goodsInsert	(request, halfGoods.get("PA_CODE") , halfGoods.get("GOODS_CODE") , transBatchNo, procId);
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")) failCnt++;
			}
			
			if(failCnt > 0) apiInfoMap.put("message", paGoodsTargetList.size() + "건 중  " + failCnt + "실패");
			
		}catch (Exception e) {
			log.info("{}: {}","하프클럽 상품등록", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}

		log.info("===== 하프클럽 상품 등록 API End =====");
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
    }
        
    
	@ApiOperation(value = "상품 등록(상품코드)", notes = "상품 등록(상품코드)")
	@RequestMapping(value = "/goods-insert/{paCode}/{goodsCode}/{transBatchNo}/{procId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsInsert(HttpServletRequest request,
			 @PathVariable("paCode")	 String paCode
		   , @PathVariable("goodsCode") String goodsCode
		   , @PathVariable("transBatchNo") long transBatchNo
		   , @PathVariable("procId") String procId) throws Exception {
	
		ParamMap paramMap = new ParamMap();
		String productNo  = "";
		paramMap.put("code"			, "200");
		paramMap.put("goodsCode"	, goodsCode);
		paramMap.put("paCode"		, paCode);
		paramMap.put("paGroupCode"	, "12");
		paramMap.put("modCase"		, "INSERT");
		try {
			
			PaHalfGoodsVO paHalfGoodsTarget = paHalfGoodsService.selectPaHalfGoodsStatus(paramMap);
			
			//입점요청중 확인
			if (PaStatus.PROCEEDING.code().equals(paHalfGoodsTarget.getPaStatus())) {
				paramMap.put("code"		, String.valueOf(HttpStatus.NO_CONTENT));
				paramMap.put("message"	, "입점요청중인 상품입니다.");
				log.info("{} 상품: {} PA_CODE: {}", "하프클럽 입점요청중인 상품입니다.", goodsCode, paCode );
				return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}

			if(paHalfGoodsTarget.getProductNo() != null) {
				paramMap.put("code"		, "411");
				paramMap.put("message"	, "이미 등록된 상품입니다. 하프클럽 상품코드 : " + paHalfGoodsTarget.getProductNo());
				log.info("{} 하프클럽 상품코드: {} PA_CODE: {}", "이미 등록된 상품입니다.:", paHalfGoodsTarget.getProductNo(), paCode );
				return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
	
			//입점요청중 update
			paHalfGoodsService.updateProceeding(paramMap); 
			
			//1)배송비 템플릿 코드 체크
			Map<String,String> goodsSlipMap	= paHalfGoodsService.selectGoodsEntpSlip(paramMap); 
			if(goodsSlipMap == null || goodsSlipMap.isEmpty()) throw processException("errors.notemplate");
		
			//ㄴ1-1)도서산간 제주배송 확인
			String noShipJejuIsland = paHalfGoodsService.selectDelyNoAreaGb(paramMap);
			goodsSlipMap.put("NO_SHIP_JEJU_ISLAND", noShipJejuIsland);
			//ㄴ1-2)배송비 정책 등록
			ResponseEntity<?> responseMsg  = paHalfCommonController.entpSlipInsert(request, goodsSlipMap.get("ENTP_CODE"), goodsSlipMap.get("SHIP_MAN_SEQ"), goodsSlipMap.get("RETURN_MAN_SEQ"), goodsSlipMap.get("SHIP_COST_CODE"), goodsSlipMap.get("PA_CODE"), noShipJejuIsland, transBatchNo, procId);
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")) throw processException("errors.process_fail", new String[] {"HALFCLUB_DELY_TMPL_ERROR"});
			//ㄴ1-3)배송비가 제대로 등록되었으면 TPAHALFGOODS.PA_SHIP_COST_CODE UPDATE
			paHalfGoodsService.saveHalfGoodsShipCostCode(goodsSlipMap);
		
			//2) 상품 정보 조회
			//상품 기준정보 및 정책정보 조회
			List<PaHalfGoodsVO> paHalfGoodsList		= paHalfGoodsService.selectPaHalfGoodsInfo(paramMap);  
			//상품 컨텐츠 정보 등록
			PaHalfGoodsVO paHalfGoodsContents		= paHalfGoodsService.selectPaHalfGoodsContentsInfo(paramMap);
			//옵션 기본정보 조회
			List<PaHalfGoodsdtMapping> goodsdtMapping 	= paHalfGoodsService.selectPaHalfGoodsdtInfoList(paramMap); 
			//정보고시 조회
			List<PaGoodsOffer> paHalfGoodsOffer 	= paHalfGoodsService.selectPaHalfGoodsOfferList(paramMap); 
			
			PaHalfGoodsVO paHalfGoods = paHalfGoodsList.get(0);
						
			//3) 상품 기준정보 등록 
			productNo = callGoodsStdInfoApi(request, paHalfGoods, paramMap, transBatchNo, procId);
			if(productNo == null ||"".equals(productNo)) throw processException("msg.cannot_create", new String[] { "HALFCLUB-PRODUCT_NO" });
			
			paHalfGoods.setProductNo		(productNo);
			paHalfGoodsContents.setProductNo(productNo);
			paramMap.put("productNo", productNo);
			
			//4) 상품 정책정보 등록
			callGoodsPrdPlcyDtInfoAPI(request, paHalfGoods, goodsdtMapping, paramMap, transBatchNo, procId);
			
			//5) 상품 컨텐츠정보 등록
			callGoodsCntsDtlInfoApi(request, paHalfGoodsContents, paHalfGoodsOffer, paramMap, transBatchNo, procId);
		
			//6) TPAHALFGOODS, TPAHALFGOODSDTMAPPING, TPAGOODSTARGET UPDATE
			paHalfGoodsService.savePaHalfGoodsTx(paHalfGoods, goodsdtMapping, paramMap); 
			
			//7) 상품 옵션 번호 저장
			if("200".equals(paramMap.getString("code"))) goodsList(request, paCode, goodsCode , transBatchNo, procId);
			
		}catch (Exception e) {
			paramMap.put("code"		, "500");
			paramMap.put("message"	, PaHalfComUtill.getErrorMessage(e));
			log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 상품등록(상품코드) 오류", PaHalfComUtill.getErrorMessage(e), goodsCode, paCode );
		}finally {
			codeReset(request, paramMap, transBatchNo, procId);
		}
		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	private void codeReset(HttpServletRequest request, ParamMap paramMap, long transBatchNo, String procId) {
		//제휴상품코드(ProductNo)가 생긴 상태에서 에러가 발생하면 CodeReset을 통해 제휴사 상품 연동 정보를 삭제해준다.
		String productNo = paramMap.getString("productNo");
		String code		 = paramMap.getString("code");
		String message   = ComUtil.NVL(paramMap.getString("message"));
		
		
		if(productNo == null || "".equals(productNo)) paramMap.put("productNo", "99999");
		if("200".equals(code)) return; 						  //정상적으로 프로세스가 종료된경우 codeReset을 호출하지 않음.
		if((message.contains("입점요청중인 상품입니다"))
		   ||(message.contains("이미 등록된 상품입니다"))) return; 	  //입점요청 or 입점완료상태인 경우 codeReset을 호출하지 않음.

		try {
			//입점요청중 해제
			paHalfGoodsService.updateClearProceeding(paramMap);
			goodsReset(request, paramMap.getString("goodsCode"), paramMap.getString("paCode"), paramMap.getString("productNo"), transBatchNo, procId);
		}catch (Exception e) {
			log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 codeReset", PaHalfComUtill.getErrorMessage(e), paramMap.getString("goodsCode"), paramMap.getString("paCode") );
		}
	}


	//상품 기준 정보 등록
	@SuppressWarnings("unchecked")
	private String callGoodsStdInfoApi(HttpServletRequest request ,PaHalfGoodsVO paHalfGoods, ParamMap paramMap, long transBatchNo, String procId) throws Exception {
		
		String prg_id 			= "IF_PAHALFAPI_01_002";
		ParamMap apiInfoMap		= new ParamMap();
		ParamMap apiDataMap		= new ParamMap();
		String prdNo			= "";
//		String paBrandNo	  	= "";
		
		Map<String, Object> resultMap 	 = null;
		Map<String, String> apiResultMap = null;
		
		//서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(paHalfGoods.getGoodsCode());
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]하프클럽-상품기준정보등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.HALF.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		try {
			apiInfoMap.put("paCode", paHalfGoods.getPaCode());
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			
			//브랜드 번호는 동기화에서 세팅해줌
//			paBrandNo = paHalfGoodsService.selectPaHalfBrandNo(paHalfGoods);
//			paHalfGoods.setPaBrandNo(paBrandNo);
			
			apiDataMap.put("siteCd"		, "1");
			apiDataMap.put("prdCd"		, paHalfGoods.getGoodsCode());
			apiDataMap.put("prdGroupNo"	, paHalfGoods.getPaBrandNo());		
			apiDataMap.put("stdCtgrNo3"	, paHalfGoods.getPaLmsdKey());
			apiDataMap.put("mdNo"		, ""); //MD연동하지 않기로 협의함..
			
//			신규 통신모듈로 대체
//			resultMap 	 = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);	
			resultMap 	 = paHalfAdvConnectUtil.registerProduct(paHalfGoods.getGoodsCode(), transBatchNo, apiInfoMap, apiDataMap);
			apiResultMap = PaHalfComUtill.getApiResult(resultMap);
			
			if(!"200".equals(apiResultMap.get("code"))) throw processException("pa.connect_error", new String[] {"하프클럽 기준정보 등록 실패 : " + paHalfGoods.getGoodsCode()} ); 
			
			Map<String,Object> dataMap 	= (Map<String, Object>) PaHalfComUtill.getApiData(resultMap, "prdMstInfo");
			prdNo	= String.valueOf(dataMap.get("prdNo"));

		}catch (Exception e) {
			apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
			paramMap.put("code"		, "500");
			paramMap.put("message"	, prg_id + "||" + apiResultMap.get("message"));
			log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 상품 기준 정보 등록 오류", PaHalfComUtill.getErrorMessage(e), paHalfGoods.getGoodsCode(), paHalfGoods.getPaCode());
			paHalfGoodsService.updatePaHalfGoodsReturnNote(paHalfGoods, paramMap);
			paHalfConnectUtil.checkException(apiInfoMap, e);
		}finally {
			serviceLog.setResultCode(apiResultMap.get("code"));
			serviceLog.setResultMsg(apiResultMap.get("message"));
			transLogService.logTransServiceEnd(serviceLog);
			//전송관리테이블 기록
			paHalfGoods.setProductNo(prdNo);
			if(prdNo == null || "".equals(prdNo)) {
				paHalfGoods.setProductNo("99999");
			}
			PaGoodsTransLog paGoodsTransLog = PaHalfComUtill.setGoodsTransLog(prg_id, paHalfGoods, apiResultMap);
			paHalfGoodsService.insertPaHalfGoodsTransLog(paGoodsTransLog);
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		return prdNo;
	}
	
	//상품 정책 정보 등록
	private void callGoodsPrdPlcyDtInfoAPI(HttpServletRequest request, PaHalfGoodsVO paHalfGoods, List<PaHalfGoodsdtMapping> goodsdtMapping, ParamMap paramMap, long transBatchNo, String procId) throws Exception {		
		if(!"200".equals(paramMap.get("code"))) return;
		
		String prg_id 			= "IF_PAHALFAPI_01_003";
		ParamMap apiInfoMap		= new ParamMap();
		ParamMap apiDataMap		= new ParamMap();
		Map<String, Object> resultMap 	= null;
		Map<String,String> apiResultMap = null;

		//서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(paHalfGoods.getGoodsCode());
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]하프클럽-상품정책정보등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.HALF.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		try {
			apiInfoMap.put("paCode", paHalfGoods.getPaCode());
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			paHalfGoodsService.setGoodsDtInfo(paHalfGoods, goodsdtMapping, apiDataMap,  false);
			
//			신규 통신모듈로 대체
//			resultMap 	 = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
			resultMap 	 = paHalfAdvConnectUtil.registerProduct(paHalfGoods.getGoodsCode(), transBatchNo, apiInfoMap, apiDataMap);
			apiResultMap = PaHalfComUtill.getApiResult(resultMap);
			
			if(!"200".equals(apiResultMap.get("code")))  throw processException("pa.connect_error", new String[] {"하프클럽 정책정보 등록 실패 : " + paHalfGoods.getGoodsCode()} ); 
			
		}catch (Exception e) {
			apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
			paramMap.put("code"		, "500");
			paramMap.put("message"	, prg_id + "||" + apiResultMap.get("message"));
			log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 상품 정책 정보 등록 오류", PaHalfComUtill.getErrorMessage(e), paHalfGoods.getGoodsCode(), paHalfGoods.getPaCode() );
			paHalfConnectUtil.checkException(apiInfoMap, e);
		}finally {
			serviceLog.setResultCode(apiResultMap.get("code"));
			serviceLog.setResultMsg(apiResultMap.get("message"));
			transLogService.logTransServiceEnd(serviceLog);
			PaGoodsTransLog paGoodsTransLog = PaHalfComUtill.setGoodsTransLog(prg_id, paHalfGoods, apiResultMap);
			paHalfGoodsService.insertPaHalfGoodsTransLog(paGoodsTransLog);
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
	}
	//상품 컨텐츠 정보 등록
	private void callGoodsCntsDtlInfoApi(HttpServletRequest request, PaHalfGoodsVO paHalfGoods, List<PaGoodsOffer> paHalfGoodsOffer, ParamMap paramMap, long transBatchNo, String procId) throws Exception {
		if(!"200".equals(paramMap.get("code"))) return;
		
		String prg_id 			= "IF_PAHALFAPI_01_004";
		ParamMap apiInfoMap		= new ParamMap();
		ParamMap apiDataMap		= new ParamMap();
		Map<String, Object> resultMap 	= null;
		Map<String,String> apiResultMap = null;
		
		//서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(paHalfGoods.getGoodsCode());
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]하프클럽-상품컨텐츠정보등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.HALF.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		try {
			apiInfoMap.put("paCode", paHalfGoods.getPaCode());
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			paHalfGoodsService.setGoodsContentsInfo(paHalfGoods, paHalfGoodsOffer, apiDataMap,  false);
			
//			신규 통신모듈로 대체
//			resultMap 	 = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
			resultMap 	 = paHalfAdvConnectUtil.registerProduct(paHalfGoods.getGoodsCode(), transBatchNo, apiInfoMap, apiDataMap);
			apiResultMap = PaHalfComUtill.getApiResult(resultMap);
			
			if(!"200".equals(apiResultMap.get("code"))) throw processException("pa.connect_error", new String[] {"하프클럽 컨텐츠정보 등록 실패 : " + paHalfGoods.getGoodsCode()});
			
		}catch (Exception e) {
			apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
			paramMap.put("code"		, "500");
			paramMap.put("message"	, prg_id + "||" + apiResultMap.get("message"));
			log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 상품 컨텐츠 정보 등록 오류", PaHalfComUtill.getErrorMessage(e), paHalfGoods.getGoodsCode(), paHalfGoods.getPaCode() );
			paHalfConnectUtil.checkException(apiInfoMap, e);
		}finally {
			serviceLog.setResultCode(apiResultMap.get("code"));
			serviceLog.setResultMsg(apiResultMap.get("message"));
			transLogService.logTransServiceEnd(serviceLog);
			//전송관리테이블 기록 
			PaGoodsTransLog paGoodsTransLog = PaHalfComUtill.setGoodsTransLog(prg_id, paHalfGoods, apiResultMap);
			paHalfGoodsService.insertPaHalfGoodsTransLog(paGoodsTransLog);
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
	}
	
	
	/**
	 * 상품 수정
	 * 
	 * @param request
	 * @param goodsCode
	 * @param paCode
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 수정", notes = "상품 수정")
	@RequestMapping(value = "/goods-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsModify(HttpServletRequest request,
			@ApiParam(name = "paCode"		, required = true	, value = "제휴사코드")   							@RequestParam(value = "paCode"	  		, required = true) String paCode,
			@ApiParam(name = "goodsCode"	, required = true	, value = "상품코드") 		 						@RequestParam(value = "goodsCode"		, required = true) String goodsCode,
			@ApiParam(name = "searchTermGb"	, required = false	, value = "실행중복체크여부"	, defaultValue="0") 	@RequestParam(value = "searchTermGb"	, required = false	, defaultValue="insert") String searchTermGb,
			@ApiParam(name = "transBatchNo"	, required = false	, value = "상품연동번호", defaultValue="999999") 	@RequestParam(value = "transBatchNo", required = false, defaultValue="999999") long transBatchNo,
			@ApiParam(name = "procId"	    , required = false	, value = "처리자ID", defaultValue="ENHALFAPI") @RequestParam(value = "procId", required = false, defaultValue="ENHALFAPI") String procId) throws Exception{

		log.info("===== 하프클럽 상품 통합 수정 API Start =====");
		ParamMap apiInfoMap = new ParamMap();
		ParamMap paramMap 	= new ParamMap();
		String prg_id 		= "IF_PAHALFAPI_01_005";
		
		Map<String, String> goodsSlipMap 		= null;
		ResponseEntity<?> responseMsg 			= null;
		List<PaHalfGoodsVO> paHalfGoodsList 	= null;
		List<PaHalfGoodsdtMapping> goodsdtMapping 	= null;
		PaHalfGoodsVO paHalfGoods			  	= null;
		List<PaGoodsOffer> paHalfGoodsOffer   	= null;
		String currentTime                      = "";
				
		paramMap.put("code"			, "200");
		paramMap.put("goodsCode"	, goodsCode);
		paramMap.put("paCode"		, paCode);
		paramMap.put("paGroupCode"	, "12");
		paramMap.put("modCase"		, "MODIFY");
		paramMap.put("searchTermGb"	, searchTermGb);
		
		try {
			//1. API 정보 
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
//			//2. 중복체크
//			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);

			//3. 배송비 템플릿 수정 체크  => 배치로 분리
	//		paHalfCommonController.entpSlipModify(request, null, transBatchNo, procId); 
			
			//4. 판매상태변경 => 배치로 분리
	//		goodsSellModify(request, goodsCode, paCode, transBatchNo, procId);

			//5. 상품 이미지 수정 => 상품 컨텐츠 정보 수정으로 대체함
			//goodsImgUrlModify(request, goodsCode, paCode, transBatchNo, procId);
			
			//6. 상품 목록조회 (상품 단품코드 TPAHALFGOODSDTMAPPING.PA_OPTION_CODE 저장)
 			goodsList(request, paCode, goodsCode, transBatchNo, procId);
			
			//7. 상품 수정
 			currentTime = DateUtil.getCurrentDateTimeAsString();
			paHalfGoodsList	= paHalfGoodsService.selectPaHalfGoodsInfo(paramMap);
			for(PaHalfGoodsVO paHalf : paHalfGoodsList) {
				try {
					//ㄴ 배송 템플릿 신규 추가 체크
					paramMap.put("goodsCode", paHalf.getGoodsCode());
					paramMap.put("paCode"	, paHalf.getPaCode());
					paramMap.put("code"		, "200");
					paramMap.put("message"  , "");
					
					 //7-1. 배송 템플릿  등록 (상품의 출고지, 회수지, 배송정책 자체가 변경된 경우 신규 배송 템플릿 등록 필요), 단순 배송정책의 수정인 경우 7.상품 수정안에서 해결하지 않음
					goodsSlipMap = paHalfGoodsService.selectGoodsEntpSlip(paramMap);
					//7-1-1)도서산간 제주배송 확인
					String noShipJejuIsland = paHalfGoodsService.selectDelyNoAreaGb(paramMap);
					responseMsg  = paHalfCommonController.entpSlipInsert(request, goodsSlipMap.get("ENTP_CODE"), goodsSlipMap.get("SHIP_MAN_SEQ"), goodsSlipMap.get("RETURN_MAN_SEQ"), goodsSlipMap.get("SHIP_COST_CODE"), goodsSlipMap.get("PA_CODE"), noShipJejuIsland, transBatchNo, procId);
					
					switch (String.valueOf(PropertyUtils.describe(responseMsg.getBody()).get("code"))) {
					case "200":  //배송비가 신규로 들어간 경우 (상품은 입점되 있으나 출고지/회수지/배송정책 변경 됨, 기존 배송 템플릿이 없어서, 배송템플릿이 새로 따진 경우
								 //배송비가 신규로 들어간 경우 (상품은 입점되 있으나 출고지/회수지/배송정책 변경 됨, 기존 배송있는 경우
						String paShipCostCode = String.valueOf(PropertyUtils.describe(responseMsg.getBody()).get("message"));
						if(!paHalf.getPaShipCostCode().equals(paShipCostCode)) {
							paHalf.setPaShipCostCode(paShipCostCode); 
							//savePaHalfGoodsTx에서 UPDATE TPAHALFGOODS.PA_SHIP_COST_CODE
						}
						break;

					default://연동 실패 시, 상품 수정 재시도
						//paHalfGoodsService.updateEntpSlipInsertFail(paHalf); 
						continue;
					}
					
					//7-2. 정책 정보 수정을 위해 PrdPrcNo 채번 
					if (paHalf.getPrdPrcNo() == null) {
						responseMsg = goodsPrcInfo(request, paHalf.getGoodsCode(), paHalf.getPaCode(), transBatchNo, procId);
						
						if(!"200".equals(String.valueOf(PropertyUtils.describe(responseMsg.getBody()).get("code")))) throw processException("msg.cannot_create", new String[] { "ERROR :: PaHalfGoodsController.goodsModify - PrdPrcNo : null" });
						String prdPrcNo = String.valueOf(PropertyUtils.describe(responseMsg.getBody()).get("message"));
					    paHalf.setPrdPrcNo(prdPrcNo);
					}
					
					//7-3. 기준정보 수정
					goodsStdInfoModify(request, paHalf.getPaCode(), paHalf.getGoodsCode(),  paHalf.getProductNo(), paramMap, transBatchNo, procId);
					
					//7-4. 상품 정책정보수정
					goodsdtMapping 	= paHalfGoodsService.selectPaHalfGoodsdtInfoList(paramMap);
					goodsPrdPlcyDtInfoModify(request, paHalf, goodsdtMapping, paramMap, transBatchNo, procId);
					
					//7-5. 상품 컨텐츠정보 수정
					paHalfGoods			= paHalfGoodsService.selectPaHalfGoodsContentsInfo(paramMap); //컨텐츠 정보 조회
					paHalfGoodsOffer 	= paHalfGoodsService.selectPaHalfGoodsOfferList(paramMap);	  //정보고시 조회
					if(paHalfGoods != null) goodsCntsDtlInfoModify	(request, paHalfGoods ,paHalfGoodsOffer ,paramMap, transBatchNo, procId); 
				
					paHalf.setCurrentTime(currentTime);
					paHalfGoodsService.savePaHalfGoodsTx(paHalf, goodsdtMapping, paramMap);
				} catch (Exception e) {
					paramMap.put("code"		, "500");
					paramMap.put("message"	, PaHalfComUtill.getErrorMessage(e));
					log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 상품 통합 수정 오류", PaHalfComUtill.getErrorMessage(e), paramMap.getString("goodsCode"), paramMap.getString("paCode"));
				}
			}
		}catch (Exception e) {
			log.info("{}: {}", "하프클럽 상품 통합 수정", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info("===== 하프클럽 상품 통합 수정 API End =====");
		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품 기준정보 수정 Function
	 */
	public void goodsStdInfoModify(HttpServletRequest request
											, String paCode
											, String goodsCode
											, String productNo
											, ParamMap paramMap
											, long transBatchNo
											, String procId) throws Exception {
 		String prg_id 					 = "IF_PAHALFAPI_01_006";
		ParamMap apiInfoMap 			 = new ParamMap();
		ParamMap apiDataMap 			 = new ParamMap();
		Map<String, Object> resultMap = null;
		Map<String, String> apiResultMap = null;

		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]하프클럽-상품 기준정보 수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.HALF.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		try {
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			apiDataMap.put("prdNo" , productNo);
			apiDataMap.put("mdNo"  , null);
			// 통신
			apiInfoMap.put("paCode", paCode);
			
//			신규 통신모듈로 대체 
//			resultMap 		= paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
			resultMap 	 =  paHalfAdvConnectUtil.updateProduct(goodsCode, transBatchNo, apiInfoMap, apiDataMap);
			
			apiResultMap 	= PaHalfComUtill.getApiResult(resultMap);
			if (!"200".equals(apiResultMap.get("code"))) throw processException("pa.connect_error", new String[] { "ERROR :: PaHalfGoodsController.goodsStdInfoModify" });
		} catch (Exception e) {
			apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
			paramMap.put("code"		, "500");
			paramMap.put("message"	, prg_id + "||" + apiResultMap.get("message"));
			log.info("{}: {} 제휴상품: {} PA_CODE: {}", "하프클럽 상품 기준정보 수정 오류", PaHalfComUtill.getErrorMessage(e), productNo, paCode);
			paHalfConnectUtil.checkException(apiInfoMap, e);

		} finally {	
			serviceLog.setResultCode(apiResultMap.get("code"));
			serviceLog.setResultMsg(apiResultMap.get("message"));
			transLogService.logTransServiceEnd(serviceLog);
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
 	}

	/*/**
	 * 상품 정책정보 수정
	 * 
	 * @param request
	 * @param goodsCode
	 * @param paCode
	 * @return
	 * @throws Exception
	 
	@ApiOperation(value = "상품 정책정보 수정", notes = "상품 정책정보 수정", httpMethod = "PUT", produces = "application/json")
	@RequestMapping(value = "/goods-modify/goodsdt/{paCode}/{goodsCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsPrdPlcyDtInfoModify(HttpServletRequest request,
			@ApiParam(name = "paCode"	, value = "제휴사코드")   @RequestParam(value = "paCode"	 , required = true) String paCode,
			@ApiParam(name = "goodsCode", value = "상품코드") 		@RequestParam(value = "goodsCode", required = true) String goodsCode) throws Exception {

		log.info("===== 하프클럽 상품 정책정보 수정 API Start =====");
		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap	= new ParamMap();
		ParamMap paramMap 	= new ParamMap();
		String prg_id 		= "IF_PAHALFAPI_01_007";
		Map<String, Object> resultMap 	 = null;
		Map<String, String> apiResultMap = null;
		PaHalfGoodsVO paHalfGoods = null;
		
		paramMap.put("code"			, "200");
		paramMap.put("goodsCode"	, goodsCode);
		paramMap.put("paCode"		, paCode);
		paramMap.put("paGroupCode"	, "12");
		paramMap.put("modCase"		, "MODIFY");
			
		try {
			
			//상품 정책정보 번호 조회
			goodsPrcInfo(request, goodsCode, paCode);
			//상품 옵션코드 조회
			goodsList(request, paCode, goodsCode);
			
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			//상품 기준정보
			List<PaHalfGoodsVO> paHalfGoodsList		= paHalfGoodsService.selectPaHalfGoodsInfo(paramMap);
			paHalfGoods = paHalfGoodsList.get(0);
			//상품 옵션 상세
			List<PaHalfGoodsdtMapping> goodsdtMappingList 	= paHalfGoodsService.selectPaHalfGoodsdtInfoList(paramMap);
			
			paHalfGoodsService.setGoodsDtInfo(paHalfGoods, goodsdtMappingList, apiDataMap,  true);
			
			apiInfoMap.put("paCode", paHalfGoods.getPaCode());
			resultMap 	 = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
			apiResultMap = PaHalfComUtill.getApiResult(resultMap);
			if(!"200".equals(apiResultMap.get("code")))  throw processException("pa.connect_error", new String[] {"하프클럽 정책정보 수정 실패 : " + goodsCode}); 
			
			paHalfGoodsService.savePrdPlcyDtInfoModifyTx(paHalfGoods, goodsdtMappingList);
			
			//상품 수정시에  새로 추가된 단품번호(옵션번호)가 있을 경우 상품목록조회를 통해 옵션번호를 받아옴 
			checkNewStock(request,  goodsdtMappingList , paramMap);
			
		}catch (Exception e) {
			apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
			paramMap.put("code"		, "500");
			paramMap.put("message"	, prg_id + "||" + apiResultMap.get("message")); 
			log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 상품 정책 정보 수정 오류", PaHalfComUtill.getErrorMessage(e), goodsCode, paCode);
		}finally {
			PaGoodsTransLog paGoodsTransLog = PaHalfComUtill.setGoodsTransLog(prg_id, paHalfGoods, apiResultMap);
			paHalfGoodsService.insertPaHalfGoodsTransLog(paGoodsTransLog);
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}

		log.info("===== 하프클럽 상품 정책정보 수정 API End =====");
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	*/
	
	/**
	 * 상품 정책정보 수정 Function
	 */
	public void goodsPrdPlcyDtInfoModify(HttpServletRequest request, PaHalfGoodsVO paHalfGoods , List<PaHalfGoodsdtMapping> goodsdtMapping , ParamMap paramMap, long transBatchNo, String procId) throws Exception {
		if(!"200".equals(paramMap.getString("code"))) return;
		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap	= new ParamMap();
		String prg_id 		= "IF_PAHALFAPI_01_007";
		Map<String, Object> resultMap = null;
		Map<String, String> apiResultMap = null;

		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(paHalfGoods.getGoodsCode());
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]하프클럽-상품 정책정보 수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.HALF.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		try {
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
				
			//상품 옵션 상세			
			paHalfGoodsService.setGoodsDtInfo(paHalfGoods, goodsdtMapping, apiDataMap,  true);
			
			apiInfoMap.put("paCode", paHalfGoods.getPaCode());
			
//			신규 통신모듈로 대체
//			resultMap 	 = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);			
			resultMap 	 = paHalfAdvConnectUtil.updateProduct(paHalfGoods.getGoodsCode(), transBatchNo, apiInfoMap, apiDataMap);			
			
			apiResultMap = PaHalfComUtill.getApiResult(resultMap);
			
			if(!"200".equals(apiResultMap.get("code")))  throw processException("pa.connect_error", new String[] {"하프클럽 정책정보 수정 실패 : " + paHalfGoods.getGoodsCode()});
			
			
			//상품 수정시에  새로 추가된 단품번호(옵션번호)가 있을 경우 해당 옵션번호를 받아옴 
			checkNewStock(request, goodsdtMapping , paramMap, transBatchNo, procId);
			
		}catch (Exception e) {
			apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
			paramMap.put("code"		, "500"); 
			paramMap.put("message"	, prg_id + "||" + apiResultMap.get("message")); 
			log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 상품 정책 정보 수정 오류", PaHalfComUtill.getErrorMessage(e), paHalfGoods.getGoodsCode(), paHalfGoods.getPaCode());
			paHalfConnectUtil.checkException(apiInfoMap, e);
		}finally {
			serviceLog.setResultCode(apiResultMap.get("code"));
			serviceLog.setResultMsg(apiResultMap.get("message"));
			transLogService.logTransServiceEnd(serviceLog);
			//전송 테이블 관리
			PaGoodsTransLog paGoodsTransLog = PaHalfComUtill.setGoodsTransLog(prg_id, paHalfGoods, apiResultMap);
			paHalfGoodsService.insertPaHalfGoodsTransLog(paGoodsTransLog);
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
	}
		
	/*
	// 상품 컨텐츠 정보 수정
	@ApiOperation(value = "상품 컨텐츠 정보 수정(상품코드)", notes = "상품 컨텐츠 정보 수정(상품코드)", httpMethod = "PUT", produces = "application/json")
	@RequestMapping(value = "/goods-modify/contents/{paCode}/{goodsCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsCntsDtlInfoModify(HttpServletRequest request,
					@PathVariable("paCode")	 String paCode
				  , @PathVariable("goodsCode") String goodsCode) throws Exception {
		
		String prg_id 		= "IF_PAHALFAPI_01_008";
		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		Map<String, Object> resultMap     = null;
		Map<String, String> apiResultMap  = null;
		PaHalfGoodsVO paHalfGoods 		  = null;
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("code"			, "200");
		paramMap.put("goodsCode"	, goodsCode);
		paramMap.put("paCode"		, paCode);
		paramMap.put("paGroupCode"	, "12");
		paramMap.put("modCase"		, "MODIFY");
		
		try {
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			//1.대상조회
			paHalfGoods	= paHalfGoodsService.selectPaHalfGoodsContentsInfo(paramMap);
			//정보고시 조회
			List<PaGoodsOffer> paHalfGoodsOffer = paHalfGoodsService.selectPaHalfGoodsOfferList(paramMap);
			paHalfGoodsService.setGoodsContentsInfo(paHalfGoods, paHalfGoodsOffer, apiDataMap, true);

			//통신
			apiInfoMap.put("paCode", paCode);
			resultMap 	 = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
			apiResultMap = PaHalfComUtill.getApiResult(resultMap);
			if (!"200".equals(apiResultMap.get("code"))) throw processException("pa.connect_error", new String[] { "하프클럽 컨텐츠정보 수정 실패 : " + goodsCode });

			paHalfGoodsService.updatePaHalfGoodsOffer(paHalfGoods);
						
		} catch (Exception e) {
			apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
			paramMap.put("code", "500");
			paramMap.put("message", prg_id + "||" + apiResultMap.get("message")); 
			log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 상품 컨텐츠 정보 수정 오류", PaHalfComUtill.getErrorMessage(e), goodsCode, paCode);
		} finally {
			// 전송관리테이블 기록
			PaGoodsTransLog paGoodsTransLog = PaHalfComUtill.setGoodsTransLog(prg_id, paHalfGoods, apiResultMap);
			paHalfGoodsService.insertPaHalfGoodsTransLog(paGoodsTransLog);
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}*/

	// 상품 컨텐츠 정보 수정 
	public void goodsCntsDtlInfoModify(HttpServletRequest request, PaHalfGoodsVO paHalfGoods, List<PaGoodsOffer> paHalfGoodsOffer, ParamMap paramMap, long transBatchNo, String procId) throws Exception{
		
		if(!"200".equals(paramMap.getString("code"))) return;
		
		String prg_id 		= "IF_PAHALFAPI_01_008";
		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		Map<String, Object> resultMap = null;
		Map<String, String> apiResultMap = null;

		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(paHalfGoods.getGoodsCode());
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]하프클럽-상품 컨텐츠 정보 수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.HALF.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
				
		try {
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			paHalfGoodsService.setGoodsContentsInfo(paHalfGoods, paHalfGoodsOffer, apiDataMap, true);
			//통신
			apiInfoMap.put("paCode", paHalfGoods.getPaCode());
			
//			신규 통신모듈로 대체
//			resultMap 	 = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
			resultMap 	 = paHalfAdvConnectUtil.updateProduct(paHalfGoods.getGoodsCode(), transBatchNo, apiInfoMap, apiDataMap);
			
			apiResultMap = PaHalfComUtill.getApiResult(resultMap);
			if (!"200".equals(apiResultMap.get("code"))) throw processException("pa.connect_error", new String[] { "하프클럽 컨텐츠정보 수정 실패 : " + paHalfGoods.getGoodsCode() });
			
			
		} catch (Exception e) {
			apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
			paramMap.put("code", "500"); 
			paramMap.put("message", prg_id + "||" + apiResultMap.get("message")); 
			log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 상품 컨텐츠 정보 수정 오류", PaHalfComUtill.getErrorMessage(e), paHalfGoods.getGoodsCode(), paHalfGoods.getPaCode());
			paHalfConnectUtil.checkException(apiInfoMap, e);
		} finally {
			serviceLog.setResultCode(apiResultMap.get("code"));
			serviceLog.setResultMsg(apiResultMap.get("message"));
			transLogService.logTransServiceEnd(serviceLog);
			// 전송관리테이블 기록
			PaGoodsTransLog paGoodsTransLog = PaHalfComUtill.setGoodsTransLog(prg_id, paHalfGoods, apiResultMap);
			paHalfGoodsService.insertPaHalfGoodsTransLog(paGoodsTransLog);
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
	}
	
	/**
	 * 상품 이미지 URL 수정
	 * @param request
	 * @param goodsCode
	 * @param paCode            
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 이미지 url 수정", notes = "상품 이미지 url 수정")
	@RequestMapping(value = "/goods-imgUrl-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsImgUrlModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode"    , required = false , value = "상품코드"  	   , defaultValue = "") @RequestParam(value = "goodsCode"	, required = false) String goodsCode,
			@ApiParam(name = "paCode"	    , required = false , value = "제휴사코드"	   , defaultValue = "") @RequestParam(value = "paCode"		, required = false) String paCode,
			@ApiParam(name = "transBatchNo"	, required = false , value = "상품연동번호"    , defaultValue="999999") @RequestParam(value = "transBatchNo", required = false, defaultValue="999999") long transBatchNo,
			@ApiParam(name = "procId"	    , required = false , value = "서비스프로세스ID" , defaultValue="ENHALFAPI") @RequestParam(value = "procId", required = false, defaultValue="ENHALFAPI") String procId
			) throws Exception {
		
		ParamMap apiInfoMap						 = new ParamMap();
		String prg_id 							 = "IF_PAHALFAPI_01_009";
		ParamMap paramMap 						 = new ParamMap();
        List<PaHalfGoodsVO> paHalfGoodsList		 = null;
        List<Map<String, Object>> apiResultList  = null;
        Map<String, String> apiResultMap 		 = null;
        String resultCode 						 = null;
        String resultMessage					 = "";
		Config imageUrl 	 					 = systemService.getConfig("IMG_SERVER_1_URL");
		String image_address 					 = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
        String imageResizePath                   = "/dims/resize/600X600";
		int totalCnt = 0;
		int failCnt  = 0;
		
		try {
			// =Step 1)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			// =Step 2)중복체크
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);

			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode"	, paCode);

			//이미지 수정 대상 조회
			paHalfGoodsList = paHalfGoodsService.selectPaHalfGoodsImageList(paramMap);
		
			if (paHalfGoodsList == null || paHalfGoodsList.size() <= 0) {
				apiInfoMap.put("code", "404");
				apiInfoMap.put("message", getMessage("pa.not_exists_process_list"));
				
				return new ResponseEntity<ResponseMsg>( new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			
			totalCnt = paHalfGoodsList.size();
			
			for (PaHalfGoodsVO paHalfGoods : paHalfGoodsList) {
				try {
					List<Map<String, String>> apiDataList = new ArrayList<Map<String, String>>();
					Map<String, String> paHalfGoodsImage = new HashMap<String, String>();
					// request 세팅
					paHalfGoodsImage.put("prdNo", paHalfGoods.getProductNo());
					paHalfGoodsImage.put("basicImgUrl", image_address + paHalfGoods.getImageUrl() + paHalfGoods.getImageP() + imageResizePath);
					// 추가 이미지가 null일 경우 ""으로 세팅한다 ("" 세팅시 등록된 이미지 삭제가능)
					paHalfGoodsImage.put("add1ImgUrl", paHalfGoods.getImageAP() == null ? "" : image_address + paHalfGoods.getImageUrl() + paHalfGoods.getImageAP() + imageResizePath);
					paHalfGoodsImage.put("add2ImgUrl", paHalfGoods.getImageBP() == null ? "" : image_address + paHalfGoods.getImageUrl() + paHalfGoods.getImageBP() + imageResizePath);
					paHalfGoodsImage.put("add3ImgUrl", paHalfGoods.getImageCP() == null ? "" : image_address + paHalfGoods.getImageUrl() + paHalfGoods.getImageCP() + imageResizePath);
					paHalfGoodsImage.put("add4ImgUrl", paHalfGoods.getImageDP() == null ? "" : image_address + paHalfGoods.getImageUrl() + paHalfGoods.getImageDP() + imageResizePath);

					apiDataList.add(paHalfGoodsImage);
					
					apiInfoMap.put("paCode", paHalfGoods.getPaCode());
					
//					신규 통신모듈로 대체
//					Map<String, Object> resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataList); // 통신					
					Map<String, Object> resultMap	 = paHalfAdvConnectUtil.updateProduct(paHalfGoods.getGoodsCode(), transBatchNo, apiInfoMap, apiDataList);

					apiResultList = PaHalfComUtill.map2List(resultMap);
					resultCode 		= String.valueOf(apiResultList.get(0).get("code"));
					resultMessage	= String.valueOf(apiResultList.get(0).get("message"));
					apiResultMap = new HashMap<String, String>();
					apiResultMap.put("code"		, resultCode);
					apiResultMap.put("message"	, resultMessage);
					
					if ("200".equals(resultCode)) { 
						paHalfGoodsService.updatePaHalfGoodsImage(paHalfGoods);
					} else{
						throw processException("pa.connect_error", new String[] { "ERROR :: PaHalfGoodsController.goodsImgUrlModify" });
					}
				} catch (Exception e) {
					log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 상품 이미지 수정 오류", PaHalfComUtill.getErrorMessage(e), paHalfGoods.getGoodsCode(), paHalfGoods.getPaCode());
					failCnt++;
					apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
				}finally {
					//전송관리 테이블 기록 
					PaGoodsTransLog paGoodsTransLog = PaHalfComUtill.setGoodsTransLog(prg_id, paHalfGoods, apiResultMap);
					paHalfGoodsService.insertPaHalfGoodsTransLog(paGoodsTransLog);		
					apiResultMap = null;
				}
				
			}//End of For
			
			if(failCnt > 0) throw processException("errors.detail", new String[] {"하프클럽 상품 이미지 수정 - 실패 : " + failCnt + "건 전체 : " + totalCnt + "건"});
			
		} catch (Exception e) {
			log.info("{}: {}", "하프클럽 상품 이미지 수정", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}

		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")),HttpStatus.OK);
	}
	
	/**
	 * 상품 판매상태 변경
	 * 
	 * @param request
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 판매상태 변경", notes = "상품 판매상태 변경")
	@RequestMapping(value = "/goods-sell-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsSellModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",   defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode",    value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode", required = false) String paCode,
			@ApiParam(name = "transBatchNo"	, required = false	, value = "상품연동번호", defaultValue="999999") @RequestParam(value = "transBatchNo", required = false, defaultValue="999999") long transBatchNo,  
		    @ApiParam(name = "procId"		, required = false, value = "처리자ID", defaultValue="ENHALFAPI") @RequestParam(value = "procId", required = false, defaultValue="ENHALFAPI") String procId) throws Exception {


		ParamMap apiInfoMap 	= new ParamMap();
		ParamMap paramMap 		= new ParamMap();
		String prg_id 			= "IF_PAHALFAPI_01_010";
		Map<String, String> apiResultMap = null;
		List<PaHalfGoods> sellStateModifiedList = null;
		String goodsState = "";

		int totalCnt = 0;
		int failCnt = 0;

		try {
		
			// =Step 1)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			// =Step 2)중복체크
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);

			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode"	, paCode);
			// =Step 3) 판매중, 판매중지 리스트 조회
			sellStateModifiedList = paHalfGoodsService.selectPaHalfSellStateList(paramMap);

			if (sellStateModifiedList == null || sellStateModifiedList.size() <= 0) {
				apiInfoMap.put("code", "404");
				apiInfoMap.put("message", getMessage("pa.not_exists_process_list"));
				
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}

			totalCnt = sellStateModifiedList.size();
			
			for (PaHalfGoods paHalfGoods : sellStateModifiedList) {
				
				// 서비스로그 생성
				PaTransService serviceLog = new PaTransService();
				serviceLog.setTransCode(paHalfGoods.getGoodsCode());
				serviceLog.setTransType(TransType.PRODUCT.name());
				serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
				serviceLog.setServiceNote("[API]하프클럽-상품 판매상태 변경");
				serviceLog.setTransBatchNo(transBatchNo);
				serviceLog.setPaGroupCode(PaGroup.HALF.code());
				serviceLog.setProcessId(procId);
				transLogService.logTransServiceStart(serviceLog);
				
				try {
					
					String saleGb = paHalfGoods.getPaSaleGb(); // 상품 판매상태

					if (("20").equals(saleGb)) {
						goodsState = "02"; // 하프클럽 판매중 코드
					} else {
						goodsState = "03"; // 하프클럽 판매중단 코드
					}
					// request 세팅
					ParamMap apiDataMap = new ParamMap();
					apiDataMap.put("prdNo"	 , paHalfGoods.getProductNo()); // 상품번호 세팅
					apiDataMap.put("prdSelCd", goodsState); // 판매상태 세팅
					// 통신
					apiInfoMap.put("paCode", paHalfGoods.getPaCode());
					
//					신규 통신모듈로 대체 
//					Map<String, Object> resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);					
					Map<String, Object> resultMap = paHalfAdvConnectUtil.updateProduct(paHalfGoods.getGoodsCode(), transBatchNo, apiInfoMap, apiDataMap);
					// =API RESULT GET
					apiResultMap = PaHalfComUtill.getApiResult(resultMap);
					
					// 후처리
					if ("success".equals(apiResultMap.get("message"))) {// 하프클럽 통신 결과 success일 경우
						log.info("하프클럽 판매상태 변경 성공 GOODS_CODE : " + paHalfGoods.getGoodsCode());
						paHalfGoods.setModifyId(Constants.PA_HALF_PROC_ID);
						paHalfGoods.setTransSaleYn("0");
						paHalfGoodsService.savePaHalfGoodsSell(paHalfGoods);
					}
					
				} catch (Exception e) {
					apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
					failCnt++;
					log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 판매상태 변경 오류", PaHalfComUtill.getErrorMessage(e), paHalfGoods.getGoodsCode(), paHalfGoods.getPaCode());
				} finally {
					serviceLog.setResultCode(apiResultMap.get("code"));
					serviceLog.setResultMsg(apiResultMap.get("message"));
					transLogService.logTransServiceEnd(serviceLog);
					//전송관리 테이블 기록 
					PaGoodsTransLog paGoodsTransLog = PaHalfComUtill.setGoodsTransLog(prg_id, paHalfGoods, apiResultMap);
					paHalfGoodsService.insertPaHalfGoodsTransLog(paGoodsTransLog);		
					apiResultMap = null;
				}
			}
			
			if(failCnt > 0) throw processException("errors.detail", new String[] {"하프클럽 판매상태 변경 - 실패 : " + failCnt + "건 전체 : " + totalCnt + "건"});
			
		} catch (Exception e) { 
			log.info("{}: {}", "하프클럽 판매상태 변경", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")),	HttpStatus.OK);
	}
	
	/**
	 * 상품 재고 수량 수정
	 * @param request
	 * @param goodsCode
	 * @param paCode
	 * @param transBatchNo
	 * @param procId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 재고 수량 수정", notes = "상품 재고 수량 수정")
	@RequestMapping(value = "/goods-stock-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsStockModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", required = false, value = "상품코드"  	, defaultValue = "") @RequestParam(value = "goodsCode"	, required = false) String goodsCode,
			@ApiParam(name = "paCode"	, required = false,	value = "제휴사코드"	, defaultValue = "") @RequestParam(value = "paCode"		, required = false) String paCode,
            @ApiParam(name = "transBatchNo", required = false, value = "상품연동번호", defaultValue = "999999") @RequestParam(value = "transBatchNo", required = false, defaultValue = "999999") long transBatchNo,
            @ApiParam(name = "procId", required = false, value = "처리자ID", defaultValue = "ENHALFAPI") @RequestParam(value = "procId", required = false, defaultValue = "ENHALFAPI") String procId) throws Exception {

		log.info("===== 하프클럽 상품 재고 수량 수정 API Start =====");
		
		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		String 	 prg_id 	= "IF_PAHALFAPI_01_011";
		ParamMap paramMap 	= new ParamMap();

		Map<String, String> apiResultMap = null;
		Map<String, Object> prdMstInfo 	 = new HashMap<String, Object>();
		Map<String, Object> prdOptInfo 	 = new HashMap<String, Object>();
		
		List<PaHalfGoodsdtMappingVO> paGoodsdtMappingList = null;
		
		int totalCnt = 0;
		int failCnt  = 0;
		
		try {
			// =Step 1)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅

			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode"	, paCode);

			// 상품재고 수정 대상 조회
			paGoodsdtMappingList = paHalfGoodsService.selectPaHalfGoodsdtMappingStockList(paramMap);

			if (paGoodsdtMappingList == null || paGoodsdtMappingList.size() <= 0) {
				apiInfoMap.put("code", "404");
				apiInfoMap.put("message", getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			
			totalCnt = paGoodsdtMappingList.size();
			
			// 위에서 뽑은 리스트 for문 돌려서 각 상품에 대한 단품 리스트 생성 및 통신
			for (PaHalfGoodsdtMappingVO paHalfGoods : paGoodsdtMappingList) {
				
				PaTransService serviceLog = new PaTransService();
				serviceLog.setTransCode(paHalfGoods.getGoodsCode());
				serviceLog.setTransType(TransType.PRODUCT.name());
				serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
				serviceLog.setServiceNote("[API]하프클럽-재고 수정");
				serviceLog.setTransBatchNo(transBatchNo);
				serviceLog.setPaGroupCode(PaGroup.HALF.code());
				serviceLog.setProcessId(procId);
				transLogService.logTransServiceStart(serviceLog);
				
				try {
					ParamMap subParamMap = new ParamMap();
					subParamMap.put("paCode"	, paHalfGoods.getPaCode());
					subParamMap.put("goodsCode"	, paHalfGoods.getGoodsCode());

					// 단품 리스트
					List<Map<String, Object>> optStckList = paHalfGoodsService.selectPaHalfStockInfoList(subParamMap);
					PaHalfComUtill.replaceCamelList(optStckList);
					// request 세팅
					prdMstInfo.put("prdNo"		, paHalfGoods.getProductNo());
					prdOptInfo.put("optStckList", optStckList);

					apiDataMap.put("prdMstInfo", prdMstInfo);
					apiDataMap.put("prdOptInfo", prdOptInfo);
					apiInfoMap.put("paCode", paHalfGoods.getPaCode());
					
                    Map<String, Object> resultMap = paHalfAdvConnectUtil.updateProduct(paHalfGoods.getGoodsCode(), transBatchNo, apiInfoMap, apiDataMap);
					
					apiResultMap = PaHalfComUtill.getApiResult(resultMap);
					
					// 후처리
					if ("success".equals(apiResultMap.get("message"))) {// 하프클럽 통신 결과 success일 경우
						log.info("하프클럽 상품 재고 수량 수정 성공 GOODS_CODE : " + paHalfGoods.getGoodsCode());
						paHalfGoodsService.savePaHalfGoodsdtStock(optStckList);
					} else { 
						throw processException("pa.connect_error", new String[] { "ERROR :: PaHalfGoodsController.goodsStockModify GOODS_CODE:" + paHalfGoods.getGoodsCode()});
					}
				} catch (Exception e) {
					failCnt++;
					log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 재고 수량 수정 오류", PaHalfComUtill.getErrorMessage(e), paHalfGoods.getGoodsCode(), paHalfGoods.getPaCode());
					apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
				} finally {
					serviceLog.setResultCode(apiResultMap.get("code"));
					serviceLog.setResultMsg(apiResultMap.get("message"));
					transLogService.logTransServiceEnd(serviceLog);
					//전송관리 테이블 기록 
					PaGoodsTransLog paGoodsTransLog = PaHalfComUtill.setGoodsTransLog(prg_id, paHalfGoods.getGoodsCode(), paHalfGoods.getPaCode(), paHalfGoods.getProductNo() , apiResultMap);
					paHalfGoodsService.insertPaHalfGoodsTransLog(paGoodsTransLog);		
					apiResultMap = null;
				}
			}
			
			if(failCnt > 0) throw processException("errors.detail", new String[] {"하프클럽 재고 수량 수정 - 실패 : " + failCnt + "건 전체 : " + totalCnt + "건"});
			
		} catch (Exception e) {
			log.info("{}: {}", "하프클럽 재고 수량 수정", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info("===== 하프클럽 상품 재고 수량 수정 API End =====");
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")),HttpStatus.OK);
	}


	/**
	 * 상품 등록 리셋
	 * 
	 * @param request
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 등록 리셋", notes = "상품 등록 리셋")
	@RequestMapping(value = "/goods-reset", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsReset(HttpServletRequest request,
			@ApiParam(name = "goodsCode", required = true, value = "상품코드") 	 	@RequestParam(value = "goodsCode", required = true)	String goodsCode,
			@ApiParam(name = "paCode",    required = true, value = "제휴사코드") 	 	@RequestParam(value = "paCode",    required = true) String paCode,
			@ApiParam(name = "productNo", required = true, value = "하프클럽상품번호")	@RequestParam(value = "productNo", required = true) String productNo,
            @ApiParam(name = "transBatchNo", required = false, value = "상품연동번호", defaultValue="999999") @RequestParam(value = "transBatchNo", required = false, defaultValue="999999") long transBatchNo,
            @ApiParam(name = "procId"	   , required = false, value = "처리자ID", defaultValue="ENHALFAPI") @RequestParam(value = "procId", required = false, defaultValue="ENHALFAPI") String procId) throws Exception {

		log.info("===== 하프클럽 상품 등록 리셋 API Start =====");
		ParamMap apiInfoMap = new ParamMap();
		ParamMap paramMap 	= new ParamMap();
		String prg_id 		= "IF_PAHALFAPI_01_012";
		String resultCode 	= "";

		Map<String, String> apiResultMap = null;
		Map<String, String> map 		 = new HashMap<String, String>();
		
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paCode"	, paCode);
		paramMap.put("prgId"	, prg_id);
		
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]하프클럽-상품등록리셋");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.HALF.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
					
		try {
			
			// =Step 1)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅

			List<Map<String, String>> apiDataList = new ArrayList<Map<String, String>>();
			
			map.put("prdCd", goodsCode);
			apiDataList.add(map);

			apiInfoMap.put("paCode", paCode);
			
//			신규 통신모듈로 대체
//			Map<String, Object> resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataList);
            Map<String, Object> resultMap = paHalfAdvConnectUtil.registerProduct(goodsCode, transBatchNo, apiInfoMap, apiDataList);
			// response값
			apiResultMap = PaHalfComUtill.getApiResult(resultMap);
			resultCode   = apiResultMap.get("code");
			
			log.info("하프클럽 상품 등록 리셋 API "+("200".equals(resultCode) ? "성공" : "실패")+": " + goodsCode);
			if(!"200".equals(resultCode)) {
				throw processException("errors.detail", new String[] {"하프클럽 상품등록 리셋 실패 - 상품 : " + goodsCode});
			}
			
			paHalfGoodsService.updatePaHalfGoodsReset(paramMap);
			
		} catch (Exception e) {
			apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
			log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 상품등록 리셋 오류", PaHalfComUtill.getErrorMessage(e), goodsCode, paCode);
			paHalfConnectUtil.checkException(apiInfoMap, e);
		} finally {
			serviceLog.setResultCode(apiResultMap.get("code"));
			serviceLog.setResultMsg(apiResultMap.get("message"));
			transLogService.logTransServiceEnd(serviceLog);
			//전송관리테이블 기록
			PaGoodsTransLog paGoodsTransLog = PaHalfComUtill.setGoodsTransLog(prg_id, goodsCode, paCode, productNo , apiResultMap);
			paHalfGoodsService.insertPaHalfGoodsTransLog(paGoodsTransLog);
			//무한 상품 등록 발생을 막기 위해 상품 등록 리셋 호출 10회 초과시 입점중단 세팅 
			paHalfGoodsService.checkGoodsInsertFail(paramMap);
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info("===== 하프클럽 상품 등록 리셋 API End =====");
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 상품 목록 조회  ( 하프클럽에서 발행한, 단품의 옵션(재고)코드 값을 하프클럽으로 부터 받아와서 TPAHALFGOODSDTMAPPING.PA_ORDER_CODE  UPDATE 하기위해 사용)
	 * 
	 * @param request
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 목록 조회", notes = "상품 목록 조회")
	@RequestMapping(value = "/goods-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsList(HttpServletRequest request,
			 @ApiParam(name = "paCode",    required = true, value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",    required = true) String paCode,
			 @ApiParam(name = "goodsCode", required = true, value = "상품코드") 					   @RequestParam(value = "goodsCode", required = true) String goodsCode,
			 @ApiParam(name = "transBatchNo", required = false, value = "상품연동번호", defaultValue="999999") @RequestParam(value = "transBatchNo", required = false, defaultValue="999999") long transBatchNo,
			 @ApiParam(name = "procId"		, required = false, value = "처리자ID", defaultValue="ENHALFAPI") @RequestParam(value = "procId", required = false, defaultValue="ENHALFAPI") String procId) throws Exception {

		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = null;
		ParamMap paramMap 	= new ParamMap();
		String prg_id 		= "IF_PAHALFAPI_01_013";
		
		List<String> prdStatCdArray = null;
		List<String> prdCdArray 	= null;
		Map<String, String> resMap  = null;
		
		int failCnt  = 0;

		try {
			// API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅

			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode"	, paCode);
			// 해당 상품에 대한 빈 제휴사 단품코드 체크
			PaHalfGoodsdtMapping goods = paHalfGoodsService.selectCheckPaOptionCode(paramMap);
			// 제휴사 단품코드가 비어있지 않으면 끝내기
			if (goods == null) {
				apiInfoMap.put("code"		, "404");
				apiInfoMap.put("message"	, getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			
			// 서비스로그 생성
			PaTransService serviceLog = new PaTransService();
			serviceLog.setTransCode(goodsCode);
			serviceLog.setTransType(TransType.PRODUCT.name());
			serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
			serviceLog.setServiceNote("[API]하프클럽-상품목록조회");
			serviceLog.setTransBatchNo(transBatchNo);
			serviceLog.setPaGroupCode(PaGroup.HALF.code());
			serviceLog.setProcessId(procId);
			transLogService.logTransServiceStart(serviceLog);
			
			try {
				apiDataMap 		= new ParamMap();
				prdStatCdArray  = new ArrayList<String>();
				prdCdArray 		= new ArrayList<String>();
				
				// 데이터 세팅
				apiDataMap.put("siteCd", "1"); // 하프클럽 고정
				prdStatCdArray.add("04"); // 승인요청
				prdStatCdArray.add("05"); // 승인완료
				apiDataMap.put("prdStatCdArray", prdStatCdArray);
				apiDataMap.put("prdCdTyp"	   , "01"); // 검색 기준 - 01 : 업체 상품 코드
				prdCdArray.add(goods.getGoodsCode());
				apiDataMap.put("prdCdArray", prdCdArray); // 검색어
				// 통신
				apiInfoMap.put("paCode", goods.getPaCode());
				
//					신규 통신모듈로 대체
//					Map<String, Object> resultMap 	= paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
				Map<String, Object> resultMap 	= paHalfAdvConnectUtil.updateProduct(goods.getGoodsCode() , transBatchNo, apiInfoMap, apiDataMap);
				resMap = PaHalfComUtill.getApiResult(resultMap);
				
				if(!"200".equals(resMap.get("code"))) {
					throw processException("errors.detail", new String[] {"Error::상품목록조회오류 상품 : " + goods.getGoodsCode()});
				}
			
				List<Map<String, Object>> apiResultMap = PaHalfComUtill.map2List(resultMap);
				//제휴사 단품 코드 저장
				paOptionCodeMapping(apiResultMap, goods);
				
			} catch (Exception e) {
				resMap = PaHalfComUtill.checkResultMap(resMap , e);
				failCnt++;
				log.info("{}: {} 상품: {} PA_CODE: {}", "하프클럽 상품목록조회 오류", PaHalfComUtill.getErrorMessage(e), goods.getGoodsCode(), goods.getPaCode());
			} finally {
				serviceLog.setResultCode(resMap.get("code"));
				serviceLog.setResultMsg(resMap.get("message"));
				transLogService.logTransServiceEnd(serviceLog);
			}
			
			if(failCnt > 0) throw processException("errors.detail", new String[] {"하프클럽 상품목록조회 - 실패 : " + goods.getGoodsCode()});
			
		} catch (Exception e) {
			apiInfoMap.put("code", "500");
			apiInfoMap.put("message", "상품 목록 조회- ERROR::" + PaHalfComUtill.getErrorMessage(e));
			log.info("{}: {}", "하프클럽 상품목록조회", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 제휴사 단품 코드 세팅
	 * 
	 * @param apiResultMap
	 * @param goods
	 * @throws Exception 
	 */
	public void paOptionCodeMapping(List<Map<String, Object>> apiResultMap, PaHalfGoodsdtMapping goods) throws Exception {
		for (Map<String, Object> idx : apiResultMap) {		
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> stockInfoList = (List<Map<String, Object>>) idx.get("stockInfo");

			for (Map<String, Object> stockInfo : stockInfoList) {
				if (stockInfo.get("stockNo") == null) {	continue; }
			
				String paOptionCode 	= String.valueOf(stockInfo.get("stockNo"));
				String goodsCode 		= ((String) stockInfo.get("optValueArf")).split("_")[0];
				String goodsdtCode  	= ((String) stockInfo.get("optValueArf")).split("_")[1]; // 상품코드_순번 이므로 _로 자른 후 세팅
				String goodsdtSeq 		= ((String) stockInfo.get("optValueArf")).split("_")[2]; // 상품코드_순번 이므로 _로 자른 후 세팅
				if (goodsdtCode == null) {	continue; }
				
				// paGoodsdtMapping 세팅
				PaHalfGoodsdtMapping paGoodsdtMapping = new PaHalfGoodsdtMapping();
				paGoodsdtMapping.setPaOptionCode	(paOptionCode);
				paGoodsdtMapping.setGoodsCode		(goodsCode);
				paGoodsdtMapping.setGoodsdtCode		(goodsdtCode);
				paGoodsdtMapping.setGoodsdtSeq		(goodsdtSeq);
				paGoodsdtMapping.setModifyId		(Constants.PA_HALF_PROC_ID);
				paGoodsdtMapping.setPaCode			(goods.getPaCode());
				
				paHalfGoodsService.updateTpaGoodsdtMapping(paGoodsdtMapping);
			}
		}
	}
	
	/**
	 * 상품 정책 상세 정보 조회
	 * 
	 * @param request
	 * @param goodsCode
	 * @param paCode
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "상품 정책 상세 정보 조회", notes = "상품 정책 상세 정보 조회", httpMethod = "GET", produces = "application/json")
	@RequestMapping(value = "/goods-info", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsPrcInfo(HttpServletRequest request,
			@ApiParam(name = "goodsCode", required = true, 	value = "상품코드"  ) @RequestParam(value = "goodsCode"	, required = true) String goodsCode,
			@ApiParam(name = "paCode"	, required = true,	value = "제휴사코드" ) @RequestParam(value = "paCode"		, required = true) String paCode,
			@ApiParam(name = "transBatchNo"	, required = false	, value = "상품연동번호", defaultValue="999999") @RequestParam(value = "transBatchNo", required = false, defaultValue="999999") long transBatchNo,
			@ApiParam(name = "procId"	    , required = false  , value = "서비스프로세스ID" , defaultValue="ENHALFAPI") @RequestParam(value = "procId", required = false, defaultValue="ENHALFAPI") String procId
			) throws Exception {
		ParamMap apiInfoMap 	= new ParamMap();
		ParamMap apiDataMap 	= new ParamMap();
		String prg_id 			= "IF_PAHALFAPI_01_014";
		ParamMap paramMap 		= new ParamMap();
		Map<String, String> apiResultMap = null;
		paramMap.put("goodsCode"	, goodsCode);
		paramMap.put("paCode", paCode);

		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName(Thread.currentThread().getStackTrace()[1].getMethodName());
		serviceLog.setServiceNote("[API]하프클럽-상품 정책 상세 정보 조회");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.HALF.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		try {
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			//1. 대상 데이터 조회
			PaHalfGoodsVO paHalfGoodsVO = paHalfGoodsService.selectPrdPrcNoList(paramMap);
			apiDataMap.put("prdNo"		, paHalfGoodsVO.getProductNo());
			apiInfoMap.put("queryString", PaHalfComUtill.makeQuryString(apiDataMap));
			apiInfoMap.put("paCode"		, paHalfGoodsVO.getPaCode());
			
//			신규 통신모듈로 대체
//			Map<String, Object> resultMap 	  = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
			Map<String, Object> resultMap 	  = paHalfAdvConnectUtil.updateProduct(goodsCode, transBatchNo, apiInfoMap, apiDataMap);
			apiResultMap = PaHalfComUtill.getApiResult(resultMap);
			if(!"200".equals(apiResultMap.get("code"))) {
				throw processException("errors.detail", new String[] {"Error:: 하프클럽 상품 정책 상세 정보 조회 통신오류 상품 :" + paHalfGoodsVO.getGoodsCode()});
			}
			
			Map<String, Object> prdSelInfoMap = (Map<String, Object>) PaHalfComUtill.getApiData(resultMap, "prdSelInfo");
			String prdPrcNo = String.valueOf(prdSelInfoMap.get("prdPrcNo"));
			if (prdPrcNo == null) {
				throw processException("errors.detail", new String[] {"Error:: 상품 정책 상세 정보 조회 prdPrcNo 없음. 상품 :" + paHalfGoodsVO.getGoodsCode()});
			}
			
			paHalfGoodsVO.setPrdPrcNo(prdPrcNo);
			paHalfGoodsService.updatePrdPrcNo(paHalfGoodsVO);
			apiInfoMap.put("code"	, 200); //변경금지
			apiInfoMap.put("message", prdPrcNo); //변경금지
			 
		} catch (Exception e) {
			apiResultMap = PaHalfComUtill.checkResultMap(apiResultMap , e);
			log.info("{}: {}", "하프클럽 상품 정책 상세 정보 조회", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);

		} finally {
			serviceLog.setResultCode(apiResultMap.get("code"));
			serviceLog.setResultMsg(apiResultMap.get("message"));
			transLogService.logTransServiceEnd(serviceLog);
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}

	private void checkNewStock(HttpServletRequest request,  List<PaHalfGoodsdtMapping> goodsdtMapping ,ParamMap paramMap, long transBatchNo, String procId) {
		try {
			String goodsCode = paramMap.getString("goodsCode");
			String paCode    = paramMap.getString("paCode");
			for(PaHalfGoodsdtMapping goodsdt : goodsdtMapping) {
				if(goodsdt.getPaOptionCode() == null || "".equals(goodsdt.getPaOptionCode())) {
					goodsList(request, paCode, goodsCode , transBatchNo, procId);
					break;
				}
			}		
		}catch (Exception e) {					
			log.info("{}: {} 상품: {} PA_CODE: {}", "checkNewStock 오류", PaHalfComUtill.getErrorMessage(e), paramMap.getString("goodsCode"),  paramMap.getString("paCode"));
		}
	}
	
	
}
