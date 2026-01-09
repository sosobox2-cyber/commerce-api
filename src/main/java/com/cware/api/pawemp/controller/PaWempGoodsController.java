package com.cware.api.pawemp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
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
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaWempGoodsVO;
import com.cware.netshopping.domain.PaWempGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.PaWempEntpSlip;
import com.cware.netshopping.pawemp.common.service.PaWempApiService;
import com.cware.netshopping.pawemp.goods.model.ReturnData;
import com.cware.netshopping.pawemp.goods.service.PaWempGoodsService;
import com.cware.netshopping.pawemp.system.exception.WmpApiException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
@ApiIgnore
@Api(value = "/pawemp/goods", description = "위메프 상품")
@Controller("com.cware.api.pawemp.PaWempGoodsController")
@RequestMapping(value = "/pawemp/goods")
public class PaWempGoodsController extends AbstractController {
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pawemp.common.paWempApiService")
	private PaWempApiService paWempApiService;
	
	@Resource(name = "pawemp.goods.paWempGoodsService")
	private PaWempGoodsService paWempGoodsService;
	
	@Resource(name = "com.cware.api.pawemp.PaWempCommonController")
	private PaWempCommonController paWempCommonController;
	
	@Resource(name = "com.cware.api.pawemp.PaWempAsyncController")
	private PaWempAsyncController asyncController;
	
	@ApiOperation(value = "상품등록", notes = "상품등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "제휴상품으로 등록되었습니다."), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 410, message = "이미 등록된 상품입니다."),
			   @ApiResponse(code = 420, message = "기술서를 입력하세요."),
			   @ApiResponse(code = 430, message = "정보고시를 입력하세요."),
			   @ApiResponse(code = 440, message = "상품의 출고지 담당을 확인하세요"),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다"),
			   @ApiResponse(code = 501, message = "조건부무료 기준 금액을 입력해주세요.")})
	@RequestMapping(value = "/legacy-goods-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsInsert(HttpServletRequest request,
			@ApiParam(name="goodsCode", required=true, 	value="상품코드") @RequestParam(value="goodsCode", required=true) String goodsCode,
			@ApiParam(name="paCode", required=true,	value="제휴사코드") @RequestParam(value="paCode", required=true) String paCode,
			@ApiParam(name="procId", required=false, value="처리자ID", defaultValue="PAWEMP") @RequestParam(value="procId", required=false, defaultValue="PAWEMP") String procId,
			@ApiParam(name="searchTermGb", required=false, value="searchTermGb", defaultValue="") @RequestParam(value="searchTermGb", required=false, defaultValue="") String searchTermGb,	
			@ApiParam(name="massTargetYn", required=false, value="massTargetYn", defaultValue="") @RequestParam(value="massTargetYn", required=false, defaultValue="") String massTargetYn		
			) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String dateTime = "";
		String duplicateCheck = "";
		String prg_id = "IF_PAWEMPAPI_01_001";
		String productNo = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		ResponseEntity<?> responseMsg = null;
		
		PaWempGoodsVO paWempGoods = null;
		ReturnData returnData = null;
		
		List<PaGoodsdtMapping> goodsdtMapping = null;
		List<PaGoodsOfferVO> goodsOffer = null;
		PaWempEntpSlip entpSlip = null;
		List<PaPromoTarget> paPromoTargetList = null;//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
		try{
        	log.info("===== 상품등록 API Start =====");
        	log.info("01.API 기본정보 세팅");
        	//= connectionSetting 설정
        	dateTime = systemService.getSysdatetimeToString();
        	
        	paramMap.put("apiCode", prg_id);
        	paramMap.put("broadCode"	, Constants.PA_WEMP_BROAD_CODE);
        	paramMap.put("onlineCode"	, Constants.PA_WEMP_ONLINE_CODE);
        	paramMap.put("startDate"	, dateTime);
        	paramMap.put("modCase"		, "INSERT");
        	paramMap.put("massTargetYn"	, massTargetYn);
        	
        	if(paCode.equals(Constants.PA_WEMP_BROAD_CODE)){
        		paramMap.put("paName", Constants.PA_BROAD);
        	} else {
        		paramMap.put("paName", Constants.PA_ONLINE);
        	}
        	
        	log.info("02.API 중복실행검사");
        	if(!searchTermGb.equals("1")) {
    		    duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
    		    if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
        	}
        	        	
		    log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
		    
		    log.info("04.SK스토아 신규 배송정책 등록");
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("paAddrGb", "20");
			// 상품 배송정책 추가 ( 출고지, 회수지 담당자 주소 체크)
			entpSlip = paWempGoodsService.selectPaWempEntpSlip(paramMap);
			if(entpSlip != null){
				//출고지 등록
				responseMsg= paWempCommonController.entpSlipInsert(request, entpSlip.getPaCode(), entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getShipCostCode(), entpSlip.getNoShipIsland(), entpSlip.getInstallYn(), searchTermGb);
				String code = PropertyUtils.describe(responseMsg.getBody()).get("code").toString();
				String msg = PropertyUtils.describe(responseMsg.getBody()).get("message").toString();
				if (code.equals("501")){
					paramMap.put("code","501");
					paramMap.put("message", "조건부무료 기준 금액을 입력해주세요.(상품코드 : "+goodsCode+")");
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} else if (code.equals("500")){
					// 배송정책등록 실패 사유
					paramMap.put("code","500");
					paramMap.put("message", msg);
				} else if(!code.equals("200")){
					paramMap.put("code","440");
					paramMap.put("message",getMessage("pa.check_entp_slip_man_seq", new String[] {" 업체코드 : " + entpSlip.getEntpCode()}));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}

			//상품 수정 중 동기화배치가 돌아 상품정보 수정될 경우 다시 수정대상에 포함 하기위해 MODIFY_DATE와  수정대상조회 시작시간을 비교함
			String goodsListTime = systemService.getSysdatetimeToString();
			
			 log.info("05.SK스토아 상품정보 조회");
			//위메프 제휴상품정보 조회 PaWempGoods
			paWempGoods = paWempGoodsService.selectPaWempGoodsInfo(paramMap);
			
			if(paWempGoods == null || paWempGoods.getGoodsCode() == null) {
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			if(paWempGoods.getProductNo() != null){
				paramMap.put("code","410");
				paramMap.put("message",getMessage("pa.already_insert_goods", new String[] {"위메프 상품번호 : " + paWempGoods.getProductNo()}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			if(paWempGoods.getDescribeExt() == null){
				paramMap.put("code","420");
				paramMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			//기술서
			String goodsCom = "";
			
			goodsCom = (!ComUtil.NVL(paWempGoods.getGoodsCom()).equals("")) ? ("<div style=\"line-height: 2.0em; font-family: 'NanumBarunGothic'; font-size: 19px;\"><div><h4>&middot;&nbsp;상품구성<h4><pre>" + paWempGoods.getGoodsCom() + "</pre></div></div>") : "";
			
			if("".equals(paWempGoods.getCollectImage()) || paWempGoods.getCollectImage() == null) {
				paWempGoods.setDescribeExt("<div align='center'><img alt='' src='" + paWempGoods.getTopImage() + "' /><br /><br /><br />"	//상단 이미지
			  + goodsCom	//상품 구성
			  + paWempGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + paWempGoods.getBottomImage() + "' /></div>");	//기술서 + 하단 이미지
			}else {
				paWempGoods.setDescribeExt("<div align='center'><img alt='' src='" + paWempGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + paWempGoods.getCollectImage() + "' /><br /><br /><br />"	//상단 이미지 + 착불 이미지
			  + goodsCom		//상품 구성
			  + paWempGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + paWempGoods.getBottomImage() + "' /></div>");	//기술서 + 하단 이미지
			}
			
			paramMap.put("paGroupCode", paWempGoods.getPaGroupCode());
			
			log.info("06.SK스토아 정보고시 조회");
			goodsOffer = paWempGoodsService.selectPaWempGoodsOfferList(paramMap);
			if(goodsOffer.size() < 1){
				paramMap.put("code","430");
				paramMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			log.info("07.SK스토아 단품 옵션 조회");
			goodsdtMapping = paWempGoodsService.selectPaWempGoodsdtInfoList(paramMap);
			//프로모션조회
			paPromoTargetList = paWempGoodsService.selectPaPromoTarget(paramMap);//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
			
			try {
				// 위메프 상품등록  API 실행
				log.info("08.위메프 상픔등록 API 호출");
				returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST", asyncController.getProductObject(false, paWempGoods, goodsdtMapping, goodsOffer, paPromoTargetList), ReturnData.class, paramMap.getString("paName"));//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
				
				if(StringUtils.isNotBlank(Long.toString(returnData.getProductNo()))){
					productNo = Long.toString(returnData.getProductNo()); 
					log.info("08-1.위메프 상픔등록 API 성공 : "+productNo);
					paramMap.put("code", "200");
					paramMap.put("message", "상품등록이 정상적으로 처리되었습니다.");
				} else {
					paramMap.put("code","404");
					paramMap.put("message",getMessage("partner.no_change_data"));
				}
			} catch(WmpApiException e){ // API 오류
				String errMsg = e.getMessage();
				String[] msg = errMsg.split("error:");
				if(msg.length > 1){
					errMsg = msg[1].replaceAll("\"", "");
				}
				paramMap.put("code","500");
				paramMap.put("message", errMsg);
			} catch(Exception e){
			    log.error("위메프 상픔등록 API err", e);
			}

			log.info("09.제휴사 상품정보 전송관리 테이블 저장");
			asyncController.insertPaGoodsTransLog(paramMap, productNo);
			
			if(StringUtils.isNotBlank(productNo)) {
				// TPAWEMPGOODS에 결과 UPDATE
				paWempGoods.setProductNo(productNo); // 위메프 상품번호
				paWempGoods.setPaStatus("30"); // 입점완료
				paWempGoods.setInsertId(procId);
				paWempGoods.setModifyId(procId);
				paWempGoods.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paWempGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paWempGoods.setLastModifyDate(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));
				log.info("10.제휴사 상품등록 상품정보 저장");
				rtnMsg = paWempGoodsService.savePaWempGoodsTx(paWempGoods, goodsdtMapping, paPromoTargetList);//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
				
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
					paramMap.put("code","404");
					paramMap.put("message",rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} else {
					paramMap.put("code",paramMap.getString("code"));
					paramMap.put("message",paramMap.getString("message"));
				}
			} 
		}
		catch(Exception e){
 			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}
		finally{
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(!searchTermGb.equals("1")) {
				if(duplicateCheck.equals("0")){
					systemService.checkCloseHistoryTx("end", prg_id);
				}
			}
			log.info("===== 상품등록  API END =====");       
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "상품수정", notes = "상품수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "일반상품으로 등록되었습니다."),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 411, message = "존재하지 않는 상품입니다."), @ApiResponse(code = 420, message = "기술서를 입력하세요."),
			@ApiResponse(code = 430, message = "정보고시를 입력하세요."), @ApiResponse(code = 440, message = "단품저장에 실패했습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."), @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다"),
			@ApiResponse(code = 501, message = "조건부무료 기준 금액을 입력해주세요."), @ApiResponse(code = 520, message = "제휴상품 정보고시 번호가 없습니다.") })
	@RequestMapping(value = "/legacy-goods-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", required = false, value = "상품코드") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode", required = false, value = "제휴사코드") @RequestParam(value = "paCode", required = false) String paCode,
			@ApiParam(name = "procId", required = false, value = "처리자ID", defaultValue = "PAWEMP") @RequestParam(value = "procId", required = false, defaultValue = "PAWEMP") String procId,
			@RequestParam(value="searchTermGb"	, required=false, defaultValue = "") String searchTermGb)
			throws Exception {
		
		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		String duplicateCheck = "";
		String prg_id = "IF_PAWEMPAPI_01_002";
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		ResponseEntity<?> responseMsg = null;
		List<PaWempEntpSlip> entpSlipList = null;
		List<PaWempGoodsVO> paWempGoodsList = null;
		
		try{
        	log.info("===== 상품수정 API Start =====");
        	log.info("01.API 기본정보 세팅");
        	//= connectionSetting 설정
        	dateTime = systemService.getSysdatetimeToString();
        	
        	paramMap.put("apiCode", prg_id);
        	paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
        	paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
        	paramMap.put("startDate", dateTime);
        	paramMap.put("modCase", "MODIFY");
			log.info("처리자ID : " + procId.toUpperCase());

			log.info("02.API 중복실행검사");
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
				if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			log.info("04.출고지 생성");
			//상품 출고지, 회수지 담당자 주소 체크
			paramMap.put("paAddrGb", "20");
			entpSlipList = paWempGoodsService.selectPaWempEntpSlipList(paramMap);
			if(entpSlipList.size() != 0){
				//출고지 등록
				for(PaWempEntpSlip entpSlip : entpSlipList) {
					responseMsg= paWempCommonController.entpSlipInsert(request, entpSlip.getPaCode(), entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getShipCostCode(), entpSlip.getNoShipIsland(), entpSlip.getInstallYn(), searchTermGb);
					String code = PropertyUtils.describe(responseMsg.getBody()).get("code").toString();
					String msg = PropertyUtils.describe(responseMsg.getBody()).get("message").toString();
					if (code.equals("501")){
						paramMap.put("code","501");
						paramMap.put("message", "조건부무료 기준 금액을 입력해주세요.(상품코드 : "+entpSlip.getGoodsCode()+")");
					} else if (code.equals("500")){
						// 배송정책등록 실패 사유
						paramMap.put("code","500");
						paramMap.put("message", msg);
					} else if(!code.equals("200")){
						paramMap.put("code","440");
						paramMap.put("message",getMessage("pa.check_entp_slip_man_seq", new String[] {" 업체코드 : " + entpSlip.getEntpCode()}));
					}
					log.info("/entp-slip-insert");
					log.info("code : "+paramMap.get("code"));
					log.info("Message : "+paramMap.get("message"));
				}
			}
			log.info("05.출고지 수정");
			responseMsg = paWempCommonController.entpSlipUpdate(request);
			if (PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
			}
			else if (PropertyUtils.describe(responseMsg.getBody()).get("code").equals("500")){
				// 배송정책수정 실패 사유
				paramMap.put("code","500");
				paramMap.put("message", PropertyUtils.describe(responseMsg.getBody()).get("message"));
			}
			else if(PropertyUtils.describe(responseMsg.getBody()).get("code").equals("501")){
				paramMap.put("code","501");
				paramMap.put("message", "조건부무료 기준 금액을 입력해주세요.");
			}
			else if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")) { 
				paramMap.put("code","440");
				paramMap.put("message",getMessage("pa.check_entp_slip_man_seq", new String[] {" 상품코드 : " + goodsCode}));
			}
			log.info("/entp-ship-update");
			log.info("code : "+paramMap.get("code"));
			log.info("Message : "+paramMap.get("message"));
						
			log.info("07. 상품상태 판매중지");
			//판매중지 처리대상 조회
			paWempGoodsList = null;
			paWempGoodsList = paWempGoodsService.selectPaWempGoodsSaleStopList(paramMap);
			for(PaWempGoodsVO paWempGoods : paWempGoodsList) {
				responseMsg = goodsSellStop(request, paWempGoods.getGoodsCode(), paWempGoods.getPaCode(), procId);
				paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				log.info("/goods-sell-stop");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			log.info("08. 상품상태 판매재개");
			//판매재시작 처리대상 조회
			paWempGoodsList = paWempGoodsService.selectPaWempGoodsSaleRestartList(paramMap);
			for(PaWempGoodsVO paWempGoods : paWempGoodsList) {
				responseMsg = goodsSellRestart(request, paWempGoods.getGoodsCode(), paWempGoods.getPaCode(), procId);
				paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				log.info("/goods-sell-restart");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			//상품 수정 중 동기화배치가 돌아 상품정보 수정될 경우 다시 수정대상에 포함 하기위해 MODIFY_DATE와  수정대상조회 시작시간을 비교함
			String goodsListTime = systemService.getSysdatetimeToString();
			paramMap.put("goodsListTime", goodsListTime);
			
			log.info("09.변경된 상품정보 조회");
			paWempGoodsList = null;
			paWempGoodsList = paWempGoodsService.selectPaWempGoodsInfoList(paramMap);
			if(paWempGoodsList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				paramMap.put("resultMsg",paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request,paramMap);
			}
			
			for(PaWempGoodsVO paWempGoods : paWempGoodsList) {
				try {
					goodsModifyWemp(request, paramMap, apiInfo, paWempGoods);
				}catch (Exception e) {
					log.error(e.toString()); //운영상 문제점있으면 찾기
					continue;
				}
			}
			
			//상품수정 전부 끝나는거 대기. 7초
			Thread.sleep(7000);
			
			log.info("06.상품정보 조회(제휴상품정보고시 번호가 없는) : /goods-info");
			paWempGoodsList = paWempGoodsService.selectPaWempGoodsStockList(paramMap);
			for(PaWempGoodsVO paWempGoods : paWempGoodsList){
				responseMsg = getGoodsInfo(request, paWempGoods.getGoodsCode(), paWempGoods.getProductNo(), paWempGoods.getPaCode(), procId);
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
					if(PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
						paramMap.put("code","404");
						paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message"));
					} else {
						paramMap.put("code","500");
						paramMap.put("message",getMessage("errors.api.system")+" : goodsStockList" + "("+PropertyUtils.describe(responseMsg.getBody()).get("message")+")");
					}
					paramMap.put("resultCode", paramMap.getString("code"));
					paramMap.put("resultMsg", paramMap.getString("message"));
					paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
					systemService.insertApiTrackingTx(request,paramMap);
				}
			}
			
			log.info("10.재고 수정");
			responseMsg = goodsStockModify(request, goodsCode, paCode, procId);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
				if(PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
					paramMap.put("code","404");
					paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message"));
				} else {
					paramMap.put("code","500");
					paramMap.put("message",getMessage("errors.api.system")+" : goodsStockModify" + "("+PropertyUtils.describe(responseMsg.getBody()).get("message")+")");
				}
			} else {
				paramMap.put("code","200");
				paramMap.put("message","OK");
			}
			
		} catch (Exception e) {
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} 
		finally {
			try {
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(!searchTermGb.equals("1")){
				if (duplicateCheck.equals("0")) {
					systemService.checkCloseHistoryTx("end", prg_id);
				}				
			}
			log.info("===== 상품 수정 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 상품상태 수정 (판매중지)
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 상태 판매 중지", notes = "상품 상태 판매 중지", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다")})
	@RequestMapping(value = "/goods-sell-stop", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsSellStop(HttpServletRequest request,
			@ApiParam(name="goodsCode", required=true, value="상품코드") @RequestParam(value="goodsCode", required=true) String goodsCode,
			@ApiParam(name="paCode", required=true, value="제휴사코드") @RequestParam(value="paCode", required=true) String paCode,
			@ApiParam(name="procId", required=false, value="처리자ID", defaultValue="PAWEMP") @RequestParam(value="procId", required=false, defaultValue="PAWEMP") String procId) throws Exception{
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String dateTime = "";
		String productNo = "";
		String duplicateCheck = "";
		PaWempGoodsVO paWempGoods = null;
		
		log.info("===== 상품상태 판매중지 API Start =====");
		log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PAWEMPAPI_01_005";
		dateTime = systemService.getSysdatetimeToString();
		
    	paramMap.put("apiCode", prg_id);
    	paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		if(paCode.equals(Constants.PA_WEMP_BROAD_CODE)){
			paramMap.put("paName", Constants.PA_BROAD);
		} else {
			paramMap.put("paName", Constants.PA_ONLINE);
		}
		
		try{
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			log.info("04.SK스토아 상품 판매중지대상 조회");
			paramMap.put("paSaleGb", "30");
			paWempGoods = paWempGoodsService.selectPaWempGoodsProductNo(paramMap);
			
			if(paWempGoods == null){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			log.info("05.판매중지 api호출");
			try {
				ReturnData returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST", updateProdStatus(paWempGoods), ReturnData.class, paramMap.getString("paName"));
				if(StringUtils.isNotBlank(Long.toString(returnData.getProductNo()))){
					productNo = Long.toString(returnData.getProductNo()); 
					log.info("05-1.위메프 판매중지 API 성공 : " + productNo);
					
					paWempGoods.setModifyId(procId);
					paWempGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paWempGoods.setTransSaleYn("0");
					rtnMsg = paWempGoodsService.savePaWempGoodsSellTx(paWempGoods);
					log.info("05-2.상품 판매중지 동기화 완료 저장 : " + rtnMsg);				
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						paramMap.put("code","404");
						paramMap.put("message",rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else{
						paramMap.put("code", "200");
						paramMap.put("message", "판매중지가 정상적으로 처리되었습니다.");
					}
				} else {
					paramMap.put("code","404");
					paramMap.put("message",getMessage("partner.no_change_data"));
				}
			} catch(WmpApiException e){ // API 오류
				String errMsg = e.getMessage();
				String[] msg = errMsg.split("error:");
				if(msg.length > 1){
					errMsg = msg[1].replaceAll("\"", "");
				}
				//상품 상태 변경할 필요가 없는 경우.
				if(errMsg.replace(" ","").indexOf("변경할수없는상태") >= 0) {
					paWempGoods.setModifyId(procId);
					paWempGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paWempGoods.setTransSaleYn("0");
					rtnMsg = paWempGoodsService.savePaWempGoodsSellTx(paWempGoods);
					log.info("05-2.상품 판매중지 동기화 완료 저장 : " + rtnMsg);				
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						paramMap.put("code","404");
						paramMap.put("message",rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else{
						paramMap.put("code", "200");
						paramMap.put("message", "trans_sale_yn 상태 변경처리");
					}
				}else {
					paramMap.put("code","500");
					paramMap.put("message", errMsg);
				}
			}
			// 전송관리 테이블 저장
			asyncController.insertPaGoodsTransLog(paramMap, productNo);

		} catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				paramMap.put("message", goodsCode + "|" + paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 상품상태 판매중지 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품상태 수정 (판매재개)
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 상태 판매 재개", notes = "상품 상태 판매 재개", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다")})
	@RequestMapping(value = "/goods-sell-restart", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsSellRestart(HttpServletRequest request,
			@ApiParam(name="goodsCode", required=true, value="상품코드") @RequestParam(value="goodsCode", required=true) String goodsCode,
			@ApiParam(name="paCode", required=true, value="제휴사코드") @RequestParam(value="paCode", required=true) String paCode,
			@ApiParam(name="procId", required=false, value="처리자ID", defaultValue="PAWEMP") @RequestParam(value="procId", required=false, defaultValue="PAWEMP") String procId) throws Exception{
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String dateTime = "";
		String productNo = "";
		String duplicateCheck = "";
		PaWempGoodsVO paWempGoods = null;
		
		log.info("===== 상품상태 판매재개 API Start =====");
		log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PAWEMPAPI_01_006";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
    	paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
    	paramMap.put("startDate", dateTime);
    	if(paCode.equals(Constants.PA_WEMP_BROAD_CODE)){
    		paramMap.put("paName", Constants.PA_BROAD);
    	} else {
    		paramMap.put("paName", Constants.PA_ONLINE);
    	}
		
		try{
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			log.info("04.SK스토아 상품 판매재개대상 조회");
			paramMap.put("paSaleGb", "20");
			paWempGoods = paWempGoodsService.selectPaWempGoodsProductNo(paramMap);
			
			if(paWempGoods == null){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			log.info("05.판매재개 api 호출");
			try{
				ReturnData returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST", updateProdStatus(paWempGoods), ReturnData.class, paramMap.getString("paName"));
				if(StringUtils.isNotBlank(Long.toString(returnData.getProductNo()))){
					productNo = Long.toString(returnData.getProductNo()); 
					log.info("05-1.위메프 상품판매재개 API 성공 : " + productNo);
					
					paWempGoods.setModifyId(procId);
					paWempGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paWempGoods.setTransSaleYn("0");
					rtnMsg = paWempGoodsService.savePaWempGoodsSellTx(paWempGoods);
					log.info("05-2.상품 판매재개 동기화 완료 저장 : "+rtnMsg);				
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						paramMap.put("code","404");
						paramMap.put("message",rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else {
						paramMap.put("code", "200");
						paramMap.put("message", "판매재개가 정상적으로 처리되었습니다.");
					}
				} else {
					paramMap.put("code","404");
					paramMap.put("message",getMessage("partner.no_change_data"));
				}
			} catch(WmpApiException e){ // API 오류
				String errMsg = e.getMessage();
				String[] msg = errMsg.split("error:");
				if(msg.length > 1){
					errMsg = msg[1].replaceAll("\"", "");
				}
				//상품 상태 변경할 필요가 없는 경우.
				if(errMsg.replace(" ","").indexOf("변경할수없는상태") >= 0) {
					paWempGoods.setModifyId(procId);
					paWempGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paWempGoods.setTransSaleYn("0");
					rtnMsg = paWempGoodsService.savePaWempGoodsSellTx(paWempGoods);
					log.info("05-2.상품 판매재개 동기화 완료 저장 : "+rtnMsg);
					
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						paramMap.put("code","404");
						paramMap.put("message",rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else {
						paramMap.put("code", "200");
						paramMap.put("message", "trans_sale_yn 상태 변경처리");
					}
				}else {
					paramMap.put("code","500");
					paramMap.put("message", errMsg);
				}
			}
			// 전송관리 테이블 저장
			asyncController.insertPaGoodsTransLog(paramMap, productNo);
			
		} catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				paramMap.put("message", goodsCode + "|" + paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 상품상태 판매재개 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품 재고 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 재고 수정", notes = "상품 재고 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다")})
	@RequestMapping(value = "/goods-stock-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsStockModify(HttpServletRequest request,
			@ApiParam(name="goodsCode", required=false, value="상품코드") @RequestParam(value="goodsCode", required=false) String goodsCode,
			@ApiParam(name="paCode", required=false, value="제휴사코드") @RequestParam(value="paCode", required=false) String paCode,
			@ApiParam(name="procId", required=false, value="처리자ID", defaultValue="PAWEMP") @RequestParam(value="procId", required=false, defaultValue="PAWEMP") String procId) throws Exception{
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		String dateTime = "";
		String duplicateCheck = "";
		
		List<PaWempGoodsdtMappingVO> paGoodsdtMappingList = null;
		log.info("===== 상품재고 수정 API Start =====");
		log.info("01.API 기본정보 세팅");
		//= connectionSetting 설정
		String prg_id = "IF_PAWEMPAPI_01_003";
		dateTime = systemService.getSysdatetimeToString();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		paramMap.put("apiCode", prg_id);
    	paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
    	paramMap.put("startDate", dateTime);
		
		try{
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			log.info("04.SK스토아 상품재고 수정대상 조회");  // 재고 수량이 다른 상품 옵션 조회
			paGoodsdtMappingList = paWempGoodsService.selectPaWempGoodsdtMappingStockList(paramMap);
			if(paGoodsdtMappingList.size() == 0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			for(PaWempGoodsdtMappingVO paGoodsdtMapping : paGoodsdtMappingList){
				if(paGoodsdtMapping.getPaCode().toString().equals(Constants.PA_WEMP_BROAD_CODE)){
					paramMap.put("paName", Constants.PA_BROAD);
				} else {
					paramMap.put("paName", Constants.PA_ONLINE);
				}
				ReturnData returnData = null;
				String productNo = "";
				ParamMap subParamMap = new ParamMap();
				subParamMap.put("goodsCode", paGoodsdtMapping.getGoodsCode());
				subParamMap.put("paCode", paGoodsdtMapping.getPaCode());
				
				List<PaGoodsdtMapping> goodsdtMapping = paWempGoodsService.selectPaWempGoodsdtInfoList(subParamMap);
				try {
					if(goodsdtMapping != null){
						log.info("05.위메프 상품재고수정(OPTION) API호출");
						returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST", updateOptionObject(paGoodsdtMapping.getProductNo(), goodsdtMapping), ReturnData.class, paramMap.getString("paName"));
					}
					// 조회결과 체크
					if(returnData != null && StringUtils.isNotBlank(Long.toString(returnData.getProductNo()))){
						productNo = Long.toString(returnData.getProductNo()); 
						log.info("05-1.위메프 상품재고수정 API 성공 : "+productNo);
						paramMap.put("code", "200");
						paramMap.put("message", "재고수정이 정상적으로 처리되었습니다.("+productNo+")");
					} else {
						paramMap.put("code","404");
						paramMap.put("message",getMessage("partner.no_change_data"));
					}
				} catch(WmpApiException e){ // API 오류
					String errMsg = "";
					
					if(e.getMessage() == null) {
						errMsg = e.toString();
					}else {
						String[] msg = errMsg.split("error:");
						if(msg.length > 1){
							errMsg = msg[1].replaceAll("\"", "");
						}
					}
					
					paramMap.put("code","500");
					paramMap.put("message", errMsg);
				}
				subParamMap.put("code",paramMap.get("code"));
				subParamMap.put("message",paramMap.get("message"));
				// 전송관리 테이블 저장
				asyncController.insertPaGoodsTransLog(subParamMap, productNo);
															
				if(subParamMap.getString("code").equals("200")){
					subParamMap.put("modifyId", procId);
					rtnMsg = paWempGoodsService.savePaWempGoodsdtOrderAbleQtyTx(goodsdtMapping, subParamMap);
					log.info("05-2.상품재고수정 동기화 완료 저장 : "+rtnMsg);
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						paramMap.put("code","404");
						paramMap.put("message",rtnMsg);
					} 
				}
				log.info("/goods-stock-modify");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
		}
		catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 상품 재고 수정 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품 정보 조회
	 * @param request
	 * @param brandName
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 정보 조회", notes = "상품 정보 조회", httpMethod = "GET", produces = "application/json")
	@RequestMapping(value = "/goods-info", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getGoodsInfo(HttpServletRequest request,
			@ApiParam(name="goodsCode", required=true, value="상품코드") @RequestParam(value="goodsCode", required=true) String goodsCode,
			@ApiParam(name="productNo", required=true, value="제휴상품번호") @RequestParam(value="productNo", required=true) String productNo,
			@ApiParam(name="paCode", value="제휴사코드", defaultValue="") @RequestParam(value="paCode", required=true) String paCode,
			@ApiParam(name="procId", required=false, value="처리자ID", defaultValue="PAWEMP") @RequestParam(value="procId", required=false, defaultValue="PAWEMP") String procId)	throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = "";
		String duplicateCheck = "";
		
		log.info("===== 상품정보 조회 API Start =====");
		log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PAWEMPAPI_01_007";
		dateTime = systemService.getSysdatetimeToString();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		paramMap.put("apiCode", prg_id);
    	paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
    	paramMap.put("startDate", dateTime);
    	if(paCode.equals(Constants.PA_WEMP_BROAD_CODE)){
    		paramMap.put("paName", Constants.PA_BROAD);
    	} else {
    		paramMap.put("paName", Constants.PA_ONLINE);
    	}
		
		try{
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("productNo", productNo);
			paramMap.put("paCode", paCode);
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			// 조회 파라미터
			log.info("04.위메프 상품정보조회 API호출");				
			String queryStr = "productNo=" + productNo;
			Object returnData = paWempApiService.callWApiObejct(apiInfo, "GET", Object.class, queryStr, paramMap.getString("paName"));
			
			if(returnData != null){
				JsonElement response = new Gson().toJsonTree(returnData);
				JsonArray noticeList = response.getAsJsonObject().get("noticeList").getAsJsonArray();
				if(noticeList != null && noticeList.size() > 0){
					long productGroupNoticeNo = noticeList.get(0).getAsJsonObject().get("productGroupNoticeNo").getAsLong();
					paramMap.put("productGroupNoticeNo", productGroupNoticeNo); // 정보고시번호 맵핑
					paramMap.put("code", "200");
					paramMap.put("message", "상품정보조회가 정상적으로 처리되었습니다.");
					log.info("04-1.위메프 상품정보조회 API 성공 : "+productGroupNoticeNo);
				} else {
					paramMap.put("code","404");
					paramMap.put("message",getMessage("pa.not_exists_process_list"));	
				}
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", getMessage("errors.api.system"));
			}
			// 전송관리 테이블 저장
			asyncController.insertPaGoodsTransLog(paramMap, productNo);
			// 제휴상품 정보고시번호 업데이트
			if(paramMap.getString("code").equals("200")) {
				paramMap.put("modifyId", procId);
				paramMap.put("modifyDate", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				rtnMsg = paWempGoodsService.savePaWempGoodsGroupNoticeNoTx(paramMap);
				log.info("04-2.상품정보조회(정보고시번호) 동기화 완료 저장 : "+rtnMsg);
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
					paramMap.put("code","404");
					paramMap.put("message",rtnMsg);
				} 
			}
		}
		catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 상품 정보 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품상태 판매중지 / 판매가능
	 * @param paWempGoods
	 * @return
	 * @throws Exception
	 */
	private JsonObject updateProdStatus(PaWempGoodsVO paWempGoods) throws Exception{
		JsonObject jStatus = new JsonObject();
		
		jStatus.addProperty("productNo", paWempGoods.getProductNo());
		if(paWempGoods.getPaSaleGb().equals("30")){
			jStatus.addProperty("productStatus", "S");
		} else if(paWempGoods.getPaSaleGb().equals("20")){
			jStatus.addProperty("productStatus", "A");
		}
		
		return jStatus;
	}
	
	/**
	 * 위메프 옵션상품(재고) 수정 API JSON OBJECT 처리
	 * @param paGoodsdtMappingList
	 * @return
	 * @throws Exception
	 */
	public JsonObject updateOptionObject(String productNo, List<PaGoodsdtMapping> paGoodsOption) throws Exception{
		JsonObject root = new JsonObject();
				
		JsonObject jOption = new JsonObject();
		JsonArray jOptionValueList = new JsonArray();
		
		jOption.addProperty("selectOptionUseYn", "Y"); // 선택형 옵션 사용여부
		jOption.addProperty("selectOptionDepth", 1); 
		jOption.addProperty("selectOptionTitle1", paGoodsOption.get(0).getGoodsdtInfoKind());
		
		for(PaGoodsdtMapping optionItem : paGoodsOption){
			JsonObject oItem = new JsonObject();
			oItem.addProperty("optionValue1", optionItem.getGoodsdtInfo()); // 옵션값1 (최대 70자)
			oItem.addProperty("stockCount", asyncController.getOrderAbleQty(optionItem.getTransOrderAbleQty())); // 옵션 재고수량 (0 ~ 99999)
			oItem.addProperty("displayYn", "Y"); // 옵션 노춡여부
			oItem.addProperty("sellerOptionCode", optionItem.getGoodsdtCode()); // 업체 옵션코드 최대(50자)
			jOptionValueList.add(oItem);
		}
		jOption.add("selectOptionValueList", jOptionValueList);
		jOption.addProperty("textOptionUseYn", "N"); // 텍스트 옵션 사용여부
		
		root.addProperty("productNo", Long.parseLong(productNo));
		root.add("option", jOption);
		
		return root;
	}
	
	public void goodsModifyWemp(HttpServletRequest request, ParamMap paramMap , HashMap<String, String> apiInfo  , PaWempGoodsVO paWempGoods) throws Exception {
		
		String goodsCode 		= paramMap.getString("goodsCode");
		String procId			= paramMap.getString("modifyId");
		String dateTime 		= paramMap.getString("startDate");
		String prg_id 			= paramMap.getString("apiCode");
		String goodsListTime	= paramMap.getString("goodsListTime");
		
		//PaWempGoodsVO paWempGoods = paWempGoodsList.get(i);				
		ParamMap asyncMap = new ParamMap();
		asyncMap.put("url"		, apiInfo.get("API_URL"));
		asyncMap.put("paCode"	,paWempGoods.getPaCode());
		asyncMap.put("siteGb"	, Constants.PA_WEMP_PROC_ID);
		asyncMap.put("apiCode"	, prg_id);
		
		if(paWempGoods.getProductNo() == null){
			paramMap.put("code","411");
			paramMap.put("message",getMessage("pa.not_exists_goods", new String[] {"상품코드 : " + paWempGoods.getGoodsCode()}));
			log.info("/goods-modify");
			log.info("code : "+paramMap.get("code"));
			log.info("Message : "+paramMap.get("message"));
			return;
		}
		if(paWempGoods.getDescribeExt() == null){
			paramMap.put("code","420");
			paramMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + paWempGoods.getGoodsCode()}));
			log.info("/goods-modify");
			log.info("code : "+paramMap.get("code"));
			log.info("Message : "+paramMap.get("message"));
			return;
		}
		
		List<PaGoodsdtMapping> goodsdtMapping = null;
		List<PaGoodsOfferVO> goodsOffer = null;
		List<PaPromoTarget> paPromoTargetList = null;//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
		//기술서
		String goodsCom = "";
		
		goodsCom = (!ComUtil.NVL(paWempGoods.getGoodsCom()).equals("")) ? ("<div style=\"line-height: 2.0em; font-family: 'NanumBarunGothic'; font-size: 19px;\"><div><h4>&middot;&nbsp;상품구성<h4><pre>" + paWempGoods.getGoodsCom() + "</pre></div></div>") : "";
		
		if("".equals(paWempGoods.getCollectImage()) || paWempGoods.getCollectImage() == null) {
			paWempGoods.setDescribeExt("<div align='center'><img alt='' src='" + paWempGoods.getTopImage() + "' /><br /><br /><br />"	//상단 이미지
		  + goodsCom	//상품 구성
		  + paWempGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + paWempGoods.getBottomImage() + "' /></div>");	//기술서 + 하단 이미지
		}else {
			paWempGoods.setDescribeExt("<div align='center'><img alt='' src='" + paWempGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + paWempGoods.getCollectImage() + "' /><br /><br /><br />"	//상단 이미지 + 착불 이미지
		  + goodsCom	//상품 구성
		  + paWempGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + paWempGoods.getBottomImage() + "' /></div>");	//기술서 + 하단 이미지
		}
		
		if(!ComUtil.NVL(paWempGoods.getNoticeExt()).equals("")) {
			paWempGoods.setDescribeExt(paWempGoods.getNoticeExt() + paWempGoods.getDescribeExt());
		}
		
		paramMap.put("paGroupCode", paWempGoods.getPaGroupCode());
		paramMap.put("goodsCode", paWempGoods.getGoodsCode());
		paramMap.put("paCode", paWempGoods.getPaCode());
		log.info("09-1.SK스토아 정보고시 조회");
		goodsOffer = paWempGoodsService.selectPaWempGoodsOfferList(paramMap);
		if(goodsOffer.size() < 1){
			paramMap.put("code","430");
			paramMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
			//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			return;
		}
		log.info("09-2.SK스토아 단품 옵션 조회");
		goodsdtMapping = paWempGoodsService.selectPaWempGoodsdtInfoList(paramMap);
		//프로모션조회
		paPromoTargetList = paWempGoodsService.selectPaPromoTarget(paramMap);//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
		
		paWempGoods.setModifyId			(procId);
		paWempGoods.setModifyDate		(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		paWempGoods.setLastModifyDate	(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));
		
		asyncController.asyncGoodsModify(request, asyncMap, apiInfo, paWempGoods, goodsdtMapping, goodsOffer, paPromoTargetList);
		Thread.sleep(50);
	}
}