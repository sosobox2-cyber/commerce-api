package com.cware.api.palton.controller;

import java.sql.Timestamp;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.common.util.StringUtil;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaLtonGoodsVO;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.palton.goods.service.PaLtonGoodsService;
import com.cware.netshopping.palton.util.PaLtonConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/palton/goods", description="공통")
@Controller("com.cware.api.palton.PaLtonGoodsController")
@RequestMapping(value="/palton/goods")
public class PaLtonGoodsController  extends AbstractController{

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "com.cware.api.palton.PaLtonCommonController")
	private PaLtonCommonController paLtonCommonController;

	@Resource(name = "palton.goods.paLtonGoodsService")
	private PaLtonGoodsService paLtonGoodsService;
	
	@Autowired
	private PaLtonAsyncController paLtonAsyncController;
	
	@Autowired
	private PaLtonConnectUtil paLtonConnectUtil;
	@Autowired
	private PaCommonService paCommonServic;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "상품등록", notes = "상품등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 410, message = "이미 등록된 상품입니다."),
			   @ApiResponse(code = 420, message = "기술서를 입력하세요."),
			   @ApiResponse(code = 430, message = "정보고시를 입력하세요."),
			   @ApiResponse(code = 440, message = "상품의 출고지 담당을 확인하세요"),
			   @ApiResponse(code = 441, message = "상품의 회수지 담당을 확인하세요"),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/legacy-goods-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsInsert(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = true) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = true) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_LTON_PROC_ID) String procId,
			@ApiParam(name = "searchTermGb", 	value = "중복체크여부",  defaultValue = "") @RequestParam(value = "searchTermGb",	required = false, defaultValue = "") String searchTermGb,
			@ApiParam(name = "massTargetYn", 	value = "대량입점",  defaultValue = "") @RequestParam(value = "massTargetYn",	required = false, defaultValue = "") String massTargetYn
			) throws Exception{

		ParamMap paramMap = new ParamMap();
		ParamMap bodyMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		ArrayList<Map<String,Object>> paramGoodsInfo = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		String method = "POST";
		String duplicateCheck = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = "";
		
		ResponseEntity<?> responseMsg = null;
		PaLtonGoodsVO paLtonGoods = null;
		List<PaGoodsOffer> paLtonGoodsOffer = null;
		List<PaLtonGoodsdtMappingVO> goodsdtMapping = null;
	    List<PaPromoTarget> paPromoTargetList = null;
	    PaEntpSlip entpSlip = null;
		String prg_id = "IF_PALTONAPI_01_001";
		
		
		try {
			log.info("===== 롯데온 상품등록 API Start =====");
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode"		, prg_id);
			paramMap.put("paGroupCode"	, Constants.PA_LTON_GROUP_CODE);
			paramMap.put("broadCode"	, Constants.PA_LTON_BROAD_CODE);
        	paramMap.put("onlineCode"	, Constants.PA_LTON_ONLINE_CODE);
        	paramMap.put("siteGb"		, Constants.PA_LTON_PROC_ID);
			paramMap.put("modCase"		, "INSERT");
			paramMap.put("method"		, method);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("paCode"	, paCode);
			apiInfo.put("apiInfo"	, paramMap.getString("apiCode"));
						
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode"	, paCode);
			paramMap.put("modifyId"	, procId);
			paramMap.put("url"		, apiInfo.get("API_URL"));
			paramMap.put("dateTime"	, dateTime);
			
			/*//TODO 대량입점은 일단 효용성 문제로 안씀(리스크에 비해 기대효과가 높지 않음).. 개선한다면 테스트 및 소스 보완이 필요해 보임
			//API 호출로 인해 한건 호출
			if(!"1".equals(massTargetYn)) {
				goodsInsert(request,   apiInfo, paramMap, searchTermGb);

			//대량을 통해서 인입될때
			}else {
				paramMap.put("massTargetYn", "");
				List<HashMap<String, String>> paGoodsTargetList = paLtonGoodsService.selectPaLtonGoodsTrans(paramMap);
				
				for(HashMap<String, String> paGoodsTarget : paGoodsTargetList){
					
					paramMap.put("goodsCode"	, paGoodsTarget.get("GOODS_CODE"));
					paramMap.put("paCode"		, paGoodsTarget.get("PA_CODE"));				
					apiInfo.put("paCode"		, paGoodsTarget.get("PA_CODE"));
					
					if(excetpCheck(paramMap, paGoodsTarget)) {
						resultCnt += goodsInsert(request,   apiInfo, paramMap, searchTermGb);
					}
				}
				
				//입점 결과 처리
				if(paGoodsTargetList.size() != resultCnt) {
					paramMap.put("code"		,"490");
					paramMap.put("message"	, paGoodsTargetList.size() + "건 중"  + resultCnt + "성공" );
				}else {
					paramMap.put("code"		,"200");
					paramMap.put("message"	, paGoodsTargetList.size() + "건 입점 완료" );
				}
			}*/
			
			
			//상품 출고지 담당자 주소 체크
			paramMap.put("paAddrGb", "30");
			entpSlip = paLtonGoodsService.selectPaLtonEntpSlip(paramMap);
			
			if(null != entpSlip) {
				responseMsg = paLtonCommonController.entpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaAddrGb(), entpSlip.getPaCode(), searchTermGb);
				log.info("03-1.롯데온 상품등록 API 출고지 등록 CODE CHECK :" + PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")) {
					paramMap.put("code", "440");
					paramMap.put("message", getMessage("pa.check_entp_slip_man_seq", new String[] {" 상품코드 : " + goodsCode}));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}
			
			//상품 회수지 담당자 주소 체크
			paramMap.put("paAddrGb", "20");
			entpSlip = paLtonGoodsService.selectPaLtonEntpSlip(paramMap);
			
			if(null != entpSlip) {
				responseMsg = paLtonCommonController.entpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaAddrGb(), entpSlip.getPaCode() , searchTermGb);
				log.info("03-1.롯데온 상품등록 API 회수지 등록 CODE CHECK :" + PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")) {
					paramMap.put("code", "440");
					paramMap.put("message", getMessage("pa.check_return_slip_man_seq", new String[] {" 상품코드 : " + goodsCode}));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}
			
			log.info("04.롯데온 상품등록 API 배송비 정책 등록");
			paLtonCommonController.entpCustShipCostInsert(request, searchTermGb);
			
			
			//상품 수정 중 동기화배치가 돌아 상품정보 수정될 경우 다시 수정대상에 포함 하기위해 MODIFY_DATE와  수정대상조회 시작시간을 비교함
			String goodsListTime = systemService.getSysdatetimeToString();
			
			log.info("05.상품정보  Select");
			paLtonGoods = paLtonGoodsService.selectPaLtonGoodsInfo(paramMap);
			
			if(paLtonGoods == null){
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			if(paLtonGoods.getPaCode().equals(Constants.PA_LTON_BROAD_CODE)){
				paLtonGoods.setLrtrNo(apiInfo.get(Constants.PA_BROAD));		//하위 거래처 번호
			} else {
				paLtonGoods.setLrtrNo(apiInfo.get(Constants.PA_ONLINE));	//하위 거래처 번호
			}
			paLtonGoods.setTrNo(ConfigUtil.getString("PALTON_ENTP_CODE"));		
			
			//정보고시
			paLtonGoodsOffer = paLtonGoodsService.selectPaLtonGoodsOfferList(paramMap);
			
			if(paLtonGoodsOffer.size() < 1){
				paramMap.put("code", "430");
				paramMap.put("message", getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			///옵션정보
			goodsdtMapping = paLtonGoodsService.selectPaLtonGoodsdtInfoList(paramMap);
			
			//프로모션정보 추가 예정
			paPromoTargetList = paLtonGoodsService.selectPaPromoTarget(paramMap);
			
			paramGoodsInfo = makeGoodsInfo(paLtonGoods, paLtonGoodsOffer, goodsdtMapping, paPromoTargetList); // body 파라미터 값 생성
			
			bodyMap.put("spdLst", paramGoodsInfo);
			
			map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, bodyMap); // 통신
			
			ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) map.get("data");
			
			if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("returnCode"))) {
				
				Map<String, Object> resultData = data.get(0);
				
				paramMap.put("code", "200");
				paramMap.put("message", resultData.get("resultMessage"));
				
				paLtonAsyncController.insertPaGoodsTransLog(paLtonGoods, resultData);
				
				String paGoodsCode = String.valueOf(resultData.get("spdNo"));
				
				if(!"null".equals(paGoodsCode)){
					paLtonGoods.setSpdNo(paGoodsCode);
					
					paLtonGoods.setApprovalStatus("10");
					paLtonGoods.setPaStatus("30");
					paLtonGoods.setModifyId(procId);
					paLtonGoods.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paLtonGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paLtonGoods.setLastModifyDate(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));
					
					rtnMsg = paLtonGoodsService.savePaLtonGoodsTx(paLtonGoods, goodsdtMapping, paPromoTargetList);
				}
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
					paramMap.put("code", "404");
					paramMap.put("message", resultData.get("resultMessage"));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} else {
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
				}
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", map.get("message"));
			}
			
		} catch (Exception e) {
			paramMap.put("code", ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				paramMap.put("message", paramMap.getString("message"));
				paramMap.put("code", 	paramMap.getString("code"));
				systemService.insertApiTrackingTx(request, paramMap);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
		log.info("===== 롯데온 상품등록 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "상품수정", notes = "상품수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 411, message = "존재하지 않는 상품입니다."),
			   @ApiResponse(code = 420, message = "기술서를 입력하세요."),
			   @ApiResponse(code = 430, message = "정보고시를 입력하세요."),
			   @ApiResponse(code = 440, message = "단품저장에 실패했습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/legacy-goods-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsModify(HttpServletRequest request,
			@ApiParam(name="goodsCode",	value="상품코드",	defaultValue = "") @RequestParam(value="goodsCode", required=false) String goodsCode,
			@ApiParam(name="paCode",	value="제휴사코드",defaultValue = "") @RequestParam(value="paCode", required=false) String paCode,
			@ApiParam(name="procId",	value="처리자ID",	defaultValue = "") @RequestParam(value="procId", required=false, defaultValue=Constants.PA_LTON_PROC_ID) String procId,
			@RequestParam(value="searchTermGb"	, required=false, defaultValue = "") String searchTermGb) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();		
		String method = "POST";
		String duplicateCheck = "";
		String dateTime = "";
		
		List<PaEntpSlip> entpSlipList = null;
		PaEntpSlip entpSlip = null;
		List<HashMap<String,String>> spdNoList = null;
		
		ResponseEntity<?> responseMsg = null;
	    List<PaLtonGoodsVO> paLtonGoodsList = null;
		
		String prg_id = "IF_PALTONAPI_01_005";
		
		try{
			
			log.info("===== 롯데온 상품수정 API Start =====");
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode"		, prg_id);
			paramMap.put("paGroupCode"	, Constants.PA_LTON_GROUP_CODE);
			paramMap.put("broadCode"	, Constants.PA_LTON_BROAD_CODE);
        	paramMap.put("onlineCode"	, Constants.PA_LTON_ONLINE_CODE);
        	paramMap.put("siteGb"		, Constants.PA_LTON_PROC_ID);
			paramMap.put("modCase"		, "MODIFY");
			paramMap.put("method"		, method);
			paramMap.put("dateTime"		, dateTime);
			
			log.info("02.롯데온 상품수정 API 중복실행검사");
			//= 중복 실행 Check
			if(!searchTermGb.equals("1")) {
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			}
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode"	, paCode);
			paramMap.put("modifyId"	, procId);
			
			log.info("03.롯데온 상품수정 API 출고지 등록");
			paramMap.put("paAddrGb", "30");
			entpSlipList = paLtonGoodsService.selectPaLtonEntpSlipList(paramMap);
			if(entpSlipList.size() > 0){
				for(int i=0; i<entpSlipList.size(); i++){
					try{
						entpSlip = entpSlipList.get(i);
						
						responseMsg = paLtonCommonController.entpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaAddrGb(), entpSlip.getPaCode(), searchTermGb);
						log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
						if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
							paramMap.put("code","440");
							paramMap.put("message",getMessage("pa.check_entp_slip_man_seq", new String[] {" 롯데온 상품수정 출고지 등록에러 : " + entpSlip.getEntpCode()}));
							log.info("code : "+paramMap.get("code"));
							log.info("Message : "+paramMap.get("message"));
						}
					}catch(Exception e){
						log.info("출고지등록처리 Exception : "+entpSlip.getEntpCode()+"|"+entpSlip.getEntpManSeq());
					}
				}
			}
			
			log.info("04.롯데온 상품수정 API 회수지 등록");
			paramMap.put("paAddrGb", "20");
			entpSlipList = paLtonGoodsService.selectPaLtonEntpSlipList(paramMap);
			if(entpSlipList.size() > 0){
				for(int i=0; i<entpSlipList.size(); i++){
					try{
						entpSlip = entpSlipList.get(i);
						
						responseMsg = paLtonCommonController.entpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaAddrGb(), entpSlip.getPaCode(), searchTermGb);
						log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
						if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
							paramMap.put("code","440");
							paramMap.put("message",getMessage("pa.check_return_slip_man_seq", new String[] {" 롯데온 상품수정 회수지 등록에러 : " + entpSlip.getEntpCode()}));
							log.info("code : "+paramMap.get("code"));
							log.info("Message : "+paramMap.get("message"));
						}
					}catch(Exception e){
						log.info("회수지등록처리 Exception : "+entpSlip.getEntpCode()+"|"+entpSlip.getEntpManSeq());
					}
				}
			}
			
			log.info("05.롯데온 상품수정 API 출고/회수지 수정");
			responseMsg = paLtonCommonController.entpSlipUpdate(request);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
				paramMap.put("code","440");
				paramMap.put("message",getMessage("pa.check_entp_slip_man_seq", new String[] {" 상품코드 : " + goodsCode}));
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			log.info("06.롯데온 상품수정 API 배송비정책 등록");
			responseMsg = paLtonCommonController.entpCustShipCostInsert(request, searchTermGb);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
				paramMap.put("code","440");
				paramMap.put("message",getMessage("pa.error_ship_cost", new String[] {" 상품코드 : " + goodsCode}));
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			log.info("07.롯데온 상품수정 API 배송비정책 수정");
			responseMsg = paLtonCommonController.entpCustShipCostUpdate(request);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
				paramMap.put("code","440");
				paramMap.put("message",getMessage("pa.error_ship_cost", new String[] {" 상품코드 : " + goodsCode}));
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			//승인여부확인
			log.info("08.롯데온 상품수정 API 상품상세조회(승인여부확인)");
			spdNoList = paLtonGoodsService.selectPaApprovalStatusList(paramMap);
			if(spdNoList.size() > 0){
				for(int i=0; i<spdNoList.size(); i++){
					try {
						responseMsg = getGoodsInfo(request, spdNoList.get(i).get("GOODS_CODE").toString(), spdNoList.get(i).get("SPD_NO").toString(), spdNoList.get(i).get("PA_CODE").toString(), "APPROVAL", procId);
						
						log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
						if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
							paramMap.put("code","440");
							paramMap.put("message",getMessage("errors.api.system")+" : getGoodsInfo" + "("+PropertyUtils.describe(responseMsg.getBody()).get("message")+")");
							log.info("code : "+paramMap.get("code"));
							log.info("Message : "+paramMap.get("message"));
						}
					}catch(Exception e){
						log.info("승인여부확인 Exception : "+entpSlip.getEntpCode()+"|"+entpSlip.getEntpManSeq());
					}
				}
			}
			
			//단품번호조회
			log.info("09.롯데온 상품수정 API 상품상세조회(단품번호조회)");
			spdNoList = paLtonGoodsService.selectEmptyPaOptionCodeList(paramMap);
			if(spdNoList.size() > 0){
				for(int i=0; i<spdNoList.size(); i++){
					try {
						responseMsg = getGoodsInfo(request, spdNoList.get(i).get("GOODS_CODE").toString(), spdNoList.get(i).get("SPD_NO").toString(), spdNoList.get(i).get("PA_CODE").toString(), "", procId);
						
						log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
						if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
							paramMap.put("code","440");
							paramMap.put("message",getMessage("errors.api.system")+" : getGoodsInfo" + "("+PropertyUtils.describe(responseMsg.getBody()).get("message")+")");
							log.info("code : "+paramMap.get("code"));
							log.info("Message : "+paramMap.get("message"));
						}
					}catch(Exception e){
						log.info("단품번호조회 Exception : "+entpSlip.getEntpCode()+"|"+entpSlip.getEntpManSeq());
					}
				}
			}
			
			log.info("10.롯데온 상품수정 API 판매상태변경");
			responseMsg = setGoodsSellState(request, goodsCode, paCode, procId);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
				paramMap.put("code","440");
				paramMap.put("message",getMessage("errors.api.system")+" : setGoodsSellState" + "("+PropertyUtils.describe(responseMsg.getBody()).get("message")+")");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			//상품 수정 중 동기화배치가 돌아 상품정보 수정될 경우 다시 수정대상에 포함 하기위해 MODIFY_DATE와  수정대상조회 시작시간을 비교함
			String goodsListTime = systemService.getSysdatetimeToString();
			paramMap.put("goodsListTime", goodsListTime);
			
			log.info("11.롯데온 상품수정 API 상품 수정 대상 조회");
			paLtonGoodsList = paLtonGoodsService.selectPaLtonGoodsInfoList(paramMap);
			if(paLtonGoodsList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list", new String[] {" 상품코드 : " + goodsCode}));
				paramMap.put("resultMessage",paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}
			
			for(PaLtonGoodsVO paLtonGoods : paLtonGoodsList) {
				try {
					paLtonGoodsModify(request, paLtonGoods, apiInfo, paramMap);
				}catch (Exception e) {
					log.info("상품수정 Exception : " + paramMap.get("goodsCode").toString());
					log.info(e.getMessage());
				}
			}
			
			/*
			for(int i=0; i < paLtonGoodsList.size(); i ++) {
				try {
					ParamMap bodyMap = new ParamMap();
					ParamMap asyncMap = new ParamMap();
					PaLtonGoodsVO paLtonGoods = paLtonGoodsList.get(i);
					List<PaGoodsOffer> paLtonGoodsOffer = null;
					List<PaLtonGoodsdtMappingVO> goodsdtMapping = null;
					List<PaPromoTarget> paPromoTargetList = null;
					
					paLtonGoods.setModifyId(procId);
					paLtonGoods.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paLtonGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paLtonGoods.setLastModifyDate(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));
					
					if(paLtonGoods.getPaCode().equals(Constants.PA_LTON_BROAD_CODE)){
						paLtonGoods.setLrtrNo(apiInfo.get(Constants.PA_BROAD));		//하위 거래처 번호
					} else {
						paLtonGoods.setLrtrNo(apiInfo.get(Constants.PA_ONLINE));	//하위 거래처 번호
					}
					paLtonGoods.setTrNo(ConfigUtil.getString("PALTON_ENTP_CODE"));	
					
					asyncMap.put("url", apiInfo.get("API_URL"));
					asyncMap.put("paCode",paLtonGoods.getPaCode());
					asyncMap.put("method", method);
					asyncMap.put("siteGb", Constants.PA_LTON_PROC_ID);
					asyncMap.put("apiCode", prg_id);					
					
					log.info("GOODS_CODE : " + paLtonGoods.getGoodsCode());
					//수정할 롯데온 상품 코드가 존재하지 않을시 처리
					if(StringUtil.isEmpty(paLtonGoods.getSpdNo())){
						paramMap.put("code","411");
						paramMap.put("message",getMessage("pa.not_exists_goods", new String[] {"상품코드 : " + goodsCode}));
						continue;
					}
					
					//수정할 상품 상세설명 존재하지 않을시 처리
					if(StringUtil.isEmpty(paLtonGoods.getDescribeExt())){
						paramMap.put("code","420");
						paramMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
						continue;
					}
					
					paramMap.put("paGroupCode", paLtonGoods.getPaGroupCode());
					paramMap.put("goodsCode", paLtonGoods.getGoodsCode());
					paramMap.put("paCode", paLtonGoods.getPaCode());
					
					//상품 정보고시 조회
					paLtonGoodsOffer = paLtonGoodsService.selectPaLtonGoodsOfferList(paramMap);
					if(paLtonGoodsOffer.size() < 1){
						paramMap.put("code","430");
						paramMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
						continue;
					}
					///옵션정보
					goodsdtMapping = paLtonGoodsService.selectPaLtonGoodsdtInfoList(paramMap);
					
					//프로모션정보 추가 예정
					paPromoTargetList = paLtonGoodsService.selectPaPromoTarget(paramMap);
					
					paramGoodsInfo = makeGoodsInfo(paLtonGoods, paLtonGoodsOffer, goodsdtMapping, paPromoTargetList); // body 파라미터 값 생성
					
					bodyMap.put("spdLst", paramGoodsInfo);
					
					//비동기처리
					paLtonAsyncController.asyncGoodsModify(request, asyncMap, bodyMap, paLtonGoods, goodsdtMapping, paPromoTargetList);
					Thread.sleep(50);
					
				} catch (Exception e) {
					log.info("상품수정 Exception : " + paramMap.get("goodsCode").toString());
					log.info(e.getMessage());
				}
			}
			*/
			
			
			log.info("12.롯데온 재고 API 재고 동기화");
			responseMsg = GoodsStock(request, goodsCode, paCode, procId);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
				paramMap.put("code","440");
				paramMap.put("message",getMessage("errors.api.system")+" : GoodsStock" + "("+PropertyUtils.describe(responseMsg.getBody()).get("message")+")");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			log.info("13.롯데온 단품판매상태변경");
			responseMsg = setGoodsDtSellState(request, goodsCode, paCode, procId);
			
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
				paramMap.put("code","440");
				paramMap.put("message",getMessage("errors.api.system")+" : GoodsStock" + "("+PropertyUtils.describe(responseMsg.getBody()).get("message")+")");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
		}catch (Exception e) {
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
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(!searchTermGb.equals("1")){
				if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("13.롯데온 상품수정 저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}	
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "상품상세조회", notes = "상품상세조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goods-info", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getGoodsInfo(HttpServletRequest request,
			@ApiParam(name = "goodsCode",	value = "상품코드",	defaultValue = "") @RequestParam(value = "goodsCode",	required = true) String goodsCode,
			@ApiParam(name = "spdNo",		value = "제휴상품코드",	defaultValue = "") @RequestParam(value = "spdNo",		required = true) String spdNo,
			@ApiParam(name = "paCode",		value = "제휴사코드",	defaultValue = "") @RequestParam(value = "paCode",		required = true) String paCode,
			@ApiParam(name = "flag",		value = "구분",		defaultValue = "") @RequestParam(value = "flag",		required = true) String flag,
			@ApiParam(name = "procId",		value = "처리자ID",	defaultValue = "") @RequestParam(value = "procId",		required = false, defaultValue = Constants.PA_LTON_PROC_ID) String procId) throws Exception{
		
		log.info("===== 롯데온상품상세조회API Start =====");
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>(); 
		ParamMap bodyMap = new ParamMap();			
		
		String approvalStatus = "";
		String prg_id = "IF_PALTONAPI_01_003";
		String method = "POST";
		String duplicateCheck = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		String rtnMsgApprovalStatus = Constants.SAVE_SUCCESS;
		String dateTime = "";
		
		try {
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", prg_id);
			paramMap.put("paGroupCode", Constants.PA_LTON_GROUP_CODE);
			paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);			
        	paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);			
        	paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
        	paramMap.put("method", method);
        	paramMap.put("code", "200");
			paramMap.put("message", "OK");
			
			log.info("02.롯데온 상품상세조회 중복확인 체크  Select");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			
			paramMap.put("paCode", paCode);
			paramMap.put("procId", procId);	
			paramMap.put("dateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paramMap.put("url", apiInfo.get("API_URL"));
			
			bodyMap.put("spdNo", spdNo);
			bodyMap.put("trGrpCd", "SR");
			if(paCode.equals(Constants.PA_LTON_BROAD_CODE)){
				bodyMap.put("lrtrNo",apiInfo.get(Constants.PA_BROAD));		//하위 거래처 번호
			} else {
				bodyMap.put("lrtrNo",apiInfo.get(Constants.PA_ONLINE));		//하위 거래처 번호
			}
			bodyMap.put("trNo",ConfigUtil.getString("PALTON_ENTP_CODE"));
			
			map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, bodyMap); // 통신

			if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("returnCode"))) {				
				
				HashMap<String, Object> data = (HashMap<String, Object>) map.get("data");
				List<HashMap<String, Object>> itmLst = (List<HashMap<String, Object>>) data.get("itmLst");
				HashMap<String, String> pdAprvStatInfo = (HashMap<String, String>) data.get("pdAprvStatInfo");
				
				//승인여부확인
				if("APPROVAL".equals(flag)) {
					if("Y".equals(pdAprvStatInfo.get("fnlAprvYn"))) {
						approvalStatus = "30";
					}else {
						if("GVBK".equals(pdAprvStatInfo.get("pdAprvReqCd")) || "GVBK".equals(pdAprvStatInfo.get("catAprvStatCd")) || "GVBK".equals(pdAprvStatInfo.get("pdInfoAprvStatCd"))){
							approvalStatus = "20";
						}else {
							approvalStatus = "10";
						}
					}
					
					if("20".equals(approvalStatus) || "30".equals(approvalStatus)) {
						PaLtonGoodsVO paltonGoodsVo = new PaLtonGoodsVO();
						
						paltonGoodsVo.setGoodsCode(goodsCode);
						paltonGoodsVo.setPaCode(paCode);
						paltonGoodsVo.setApprovalStatus(approvalStatus);
						paltonGoodsVo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paltonGoodsVo.setModifyId(paramMap.get("procId").toString());
						
						rtnMsgApprovalStatus = paLtonGoodsService.updatePaLtonApprovalStatus(paltonGoodsVo);
						
						if(!rtnMsgApprovalStatus.equals(Constants.SAVE_SUCCESS)){
							paramMap.put("code", "404");
							paramMap.put("message", getMessage("pa.not_exists_process_list"));
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paramMap.put("code", "200");
							paramMap.put("message", "OK");
						}
					}
				} else { //단품상세조회
					for(int j = 0; j < itmLst.size(); j ++) {
						PaLtonGoodsdtMappingVO ltonGoodsDtMapping = new PaLtonGoodsdtMappingVO();
						
						ltonGoodsDtMapping.setPaOptionCode((String) itmLst.get(j).get("sitmNo"));
						ltonGoodsDtMapping.setGoodsdtCode(((String) itmLst.get(j).get("eitmNo")).substring(10,13));
						ltonGoodsDtMapping.setGoodsdtSeq(((String) itmLst.get(j).get("eitmNo")).substring(13,16));
						ltonGoodsDtMapping.setGoodsCode(goodsCode);
						ltonGoodsDtMapping.setPaCode(paCode);
						ltonGoodsDtMapping.setModifyId(paramMap.get("procId").toString());
						ltonGoodsDtMapping.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						rtnMsg = paLtonGoodsService.updateGoodsdtPaOptionCode(ltonGoodsDtMapping);
					}
					
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						paramMap.put("code", "404");
						paramMap.put("message", getMessage("pa.not_exists_process_list"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else {
						paramMap.put("code", "200");
						paramMap.put("message", "OK");
					}
				}
					
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", map.get("resultMessage"));
			}
			
		} catch (Exception e) {
			paramMap.put("code", ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				paramMap.put("message", paramMap.getString("message"));
				paramMap.put("code", 	paramMap.getString("code"));
				systemService.insertApiTrackingTx(request, paramMap);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
		log.info("===== 롯데온 상품상세조회 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "상품 판매상태 변경", notes = "상품 판매상태 변경", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goods-sellState", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> setGoodsSellState(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = false) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_LTON_PROC_ID) String procId) throws Exception{
	
		log.info("===== 롯데온 상품 판매상태 변경 API Start =====");		
		ParamMap paramMap = new ParamMap();
		Map<String, Object> map = new HashMap<String, Object>();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, Object> spdLst_map = new HashMap<String, Object>(); 
		ParamMap bodyMap = new ParamMap();			
		String prg_id = "IF_PALTONAPI_01_008";
		String method = "POST";
		String duplicateCheck = "";
		String dateTime = "";
		String goodsState = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		List<PaLtonGoodsVO> sellStateList = null;
		PaLtonGoodsVO sellStateTarget = null;
		
		try {
			log.info("02.롯데온 상품상세조회 중복확인 체크  Select");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", prg_id);
			paramMap.put("paGroupCode", Constants.PA_LTON_GROUP_CODE);
			paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);			
        	paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);			
        	paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
        	paramMap.put("method", method);
        	paramMap.put("procId", procId);
        	paramMap.put("paCode", paCode);
        	paramMap.put("goodsCode", goodsCode);

			apiInfo = systemService.selectPaApiInfo(paramMap);
			paramMap.put("dateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paramMap.put("url", apiInfo.get("API_URL"));
			
			// 판매자 상품번호 
			sellStateList = paLtonGoodsService.selectPaLtonSellStateList(paramMap);
			
			if(sellStateList == null || sellStateList.size() <= 0){
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			for(int i = 0; i < sellStateList.size(); i++) {
				ArrayList<Map<String,Object>> spdLst = new ArrayList<Map<String,Object>>();
				
				sellStateTarget = sellStateList.get(i);
				paramMap.put("paCode", sellStateTarget.getPaCode());
	        	paramMap.put("goodsCode", sellStateTarget.getGoodsCode());
				
				String SaleGb = sellStateTarget.getPaSaleGb();
				
				if("20".equals(SaleGb)) {
					goodsState = "SALE";
				}else {
					goodsState = "SOUT";
				}
				/*else if("35".equals(SaleGb)) {
					goodsState = "END"; 미사용. 롯데온 판매종료 후 재사용 불가.
				}*/
				
				if(sellStateTarget.getPaCode().equals(Constants.PA_LTON_BROAD_CODE)){
					spdLst_map.put("lrtrNo",apiInfo.get(Constants.PA_BROAD));		//하위 거래처 번호
				} else {
					spdLst_map.put("lrtrNo",apiInfo.get(Constants.PA_ONLINE));		//하위 거래처 번호
				}
				
				spdLst_map.put("trNo",ConfigUtil.getString("PALTON_ENTP_CODE"));
				spdLst_map.put("trGrpCd","SR");		
				spdLst_map.put("slStatCd",goodsState);
				spdLst_map.put("spdNo", sellStateTarget.getSpdNo());
				spdLst.add(spdLst_map);
				bodyMap.put("spdLst", spdLst);
				
				map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, bodyMap); // 통신
				List<HashMap<String,Object>> data = (List<HashMap<String,Object>> ) map.get("data");
				
				if(Constants.PA_LTON_SUCCESS_CODE.equals(data.get(0).get("resultCode"))) {
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
					log.info("롯데온 판매상태 변경 성공 GOODS_CODE : " + sellStateTarget.getGoodsCode());
					sellStateTarget.setModifyId(procId);
					sellStateTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					sellStateTarget.setTransSaleYn("0");
					
					rtnMsg = paLtonGoodsService.savePaLtonGoodsSellTx(sellStateTarget);
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						paramMap.put("code","500");
						paramMap.put("message",rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else{
						paramMap.put("code", "200");
						paramMap.put("message", "판매중지가 정상적으로 처리되었습니다.");
					}
				} else {
					paramMap.put("code", "500");
					paramMap.put("message", paramMap.get("goodsCode") + "|" + data.get(0).get("resultMessage"));
				}
			}
			
		} catch (Exception e) {
			paramMap.put("code", ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				paramMap.put("message", paramMap.getString("message"));
				paramMap.put("code", 	paramMap.getString("code"));
				systemService.insertApiTrackingTx(request, paramMap);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
		
		log.info("===== 롯데온 상품 판매상태 변경 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "승인상품 수정", notes = "승인상품 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 410, message = "이미 등록된 상품입니다."),
			   @ApiResponse(code = 420, message = "기술서를 입력하세요."),
			   @ApiResponse(code = 430, message = "정보고시를 입력하세요."),
			   @ApiResponse(code = 440, message = "상품의 출고지 담당을 확인하세요"),
			   @ApiResponse(code = 441, message = "상품의 회수지 담당을 확인하세요"),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/approvedGoods-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> approvedGoodsInsert(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = true) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = true) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_LTON_PROC_ID) String procId) throws Exception{
		
		
		log.info("===== 롯데온 승인 상품 수정 API Start =====");	

		ParamMap paramMap = new ParamMap();
		ParamMap bodyMap = new ParamMap();					
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		ArrayList<Map<String,Object>> paramGoodsInfo = new ArrayList<Map<String,Object>>();		
		Map<String, Object> map = new HashMap<String, Object>(); 
		
//		String prg_id = "IF_PALTONAPI_01_005";
		String prg_id = "";	// IF_PALTONAPI_01_005 : goods-modify 로 사용 중 얘는 사용 안하는 API
		String method = "POST";
		String duplicateCheck = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = "";
		
		PaLtonGoodsVO paLtonGoods = null;
		List<PaGoodsOffer> paLtonGoodsOffer = null;
		List<PaLtonGoodsdtMappingVO> goodsdtMapping = null;
	    List<PaPromoTarget> paPromoTargetList = null;
		
		try {
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", prg_id);
			paramMap.put("paGroupCode", Constants.PA_LTON_GROUP_CODE);
			paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);			
        	paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);			
        	paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
			paramMap.put("modCase", "MODIFY");
			paramMap.put("method", method);
			
			log.info("02.롯데온 승인 상품 수정 중복확인 체크  Select");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			paramMap.put("goodsCode", goodsCode);			
			paramMap.put("paCode", paCode);															
			paramMap.put("modifyId", procId);														
			paramMap.put("url", apiInfo.get("API_URL"));											

			log.info("03.롯데온 승인 상품 수정 상품정보  Select");
			paLtonGoods = paLtonGoodsService.selectPaLtonGoodsInfo(paramMap);
			
			if(paLtonGoods == null){
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			if(paLtonGoods.getPaCode().equals(Constants.PA_LTON_BROAD_CODE)){
				paLtonGoods.setLrtrNo(apiInfo.get(Constants.PA_BROAD));		//하위 거래처 번호
			} else {
				paLtonGoods.setLrtrNo(apiInfo.get(Constants.PA_ONLINE));	//하위 거래처 번호
			}
			paLtonGoods.setTrNo(ConfigUtil.getString("PALTON_ENTP_CODE"));		
			
			log.info("04.롯데온 승인 상품 수정 정보고시  Select");
			paLtonGoodsOffer = paLtonGoodsService.selectPaLtonGoodsOfferList(paramMap);
			
			if(paLtonGoodsOffer.size() < 1){
				paramMap.put("code", "430");
				paramMap.put("message", getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			log.info("04.롯데온 승인 상품 수정 옵션정보   Select");
			goodsdtMapping = paLtonGoodsService.selectPaLtonGoodsdtInfoList(paramMap);
			
			//프로모션정보 추가 예정
			paPromoTargetList = paLtonGoodsService.selectPaPromoTarget(paramMap);
			
			log.info("05.롯데온 승인 상품 수정 통신모듈 호출 ");
			paramGoodsInfo = makeGoodsInfo(paLtonGoods, paLtonGoodsOffer, goodsdtMapping, paPromoTargetList); // body 파라미터 값 생성
			bodyMap.put("spdLst", paramGoodsInfo);
			
			map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, bodyMap); // 통신

			ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) map.get("data");
			
			if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("returnCode"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "OK");
				
				Map<String, Object> resultData = data.get(0);
				
				paLtonAsyncController.insertPaGoodsTransLog(paLtonGoods, resultData);
				paLtonGoods.setModifyId(procId);
				paLtonGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				
				rtnMsg = paLtonGoodsService.savePaLtonGoodsTx(paLtonGoods, goodsdtMapping, paPromoTargetList);
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
					paramMap.put("code", "404");
					paramMap.put("message", getMessage("pa.not_exists_process_list"));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} else {
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
				}
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", map.get("resultMessage"));
			}
			
		} catch (Exception e) {
			paramMap.put("code", ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				paramMap.put("message", paramMap.getString("message"));
				paramMap.put("code", 	paramMap.getString("code"));
				systemService.insertApiTrackingTx(request, paramMap);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
		log.info("===== 롯데온 승인 상품 수정 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	public ArrayList<Map<String, Object>> makeGoodsInfo (PaLtonGoodsVO paLtonGoods, List<PaGoodsOffer> paLtonGoodsOffer, List<PaLtonGoodsdtMappingVO> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception {
		ArrayList<Map<String, Object>> dcatLst 		 = new ArrayList<Map<String, Object>>();
		ArrayList<Map<String, Object>> pdItmsArtlLst = new ArrayList<Map<String, Object>>();
		ArrayList<Map<String, Object>> sftyAthnLst = new ArrayList<Map<String, Object>>();
		ArrayList<Map<String, Object>> epnLst 		 = new ArrayList<Map<String, Object>>();
		ArrayList<Map<String, Object>> itmLst 		 = new ArrayList<Map<String, Object>>();
		ArrayList<Map<String, Object>> itmOptLst 	 = new ArrayList<Map<String, Object>>();
		ArrayList<Map<String, Object>> itmImgLst 	 = new ArrayList<Map<String, Object>>();
		ArrayList<Map<String, Object>> spdLst 	 	 = new ArrayList<Map<String, Object>>();
		Map<String, Object> map 			 = new HashMap<String, Object>();
		Map<String, Object> pdItmsInfo 		 = new HashMap<String, Object>();
		Map<String, Object> epnLstMap 		 = new HashMap<String, Object>();
		Map<String, Object> itmLstMap 		 = new HashMap<String, Object>();
		Map<String, Object> purPsbQtyInfo	 = new HashMap<String, Object>();
		Map<String, Object> sftyAthnLstMap	 = new HashMap<String, Object>();
		Map<String, Object> itmImgLstMap 	 = null;
		Map<String, Object> pdItmsArtInfo 	 = null;
		Map<String, Object> itmOptLstMap	 = null;
		Map<String, Object> dcatLstMap 		 = null;
		String pdArtlCnts = "";
		String eitmNo = "";
		String goodsDescData = "";
		double goodsPrice = 0;
		double couponPrice = 0;
		
		long currTime = System.currentTimeMillis();
		
		Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
		String image_address = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
		
		map.put("trGrpCd", "SR");
		map.put("trNo", ConfigUtil.getString("PALTON_ENTP_CODE")); //거래처번호
		map.put("lrtrNo", paLtonGoods.getLrtrNo());
		map.put("scatNo", paLtonGoods.getScatNo());
		
		String[] LfdCatNoArray = paLtonGoods.getLfDcatNo().split(",");
		
		for(int i = 0; i < LfdCatNoArray.length ; i++) {
			dcatLstMap = new HashMap<String, Object>();
			dcatLstMap.put("mallCd", "LTON"); //몰구분코드
			dcatLstMap.put("lfDcatNo",LfdCatNoArray[i]); //leaf전시카테고리번호
			dcatLst.add(dcatLstMap);
		}
		
		map.put("dcatLst", dcatLst);
		
		map.put("epdNo", paLtonGoods.getEpdNo()); //업체상품번호 : 우리꺼 GOODS_CODE
		if(StringUtil.isNotEmpty(paLtonGoods.getSpdNo())){
			map.put("spdNo",paLtonGoods.getSpdNo());
		}
		
		map.put("slTypCd", "GNRL"); // 판매유형코드
		map.put("pdTypCd", "GNRL_GNRL"); // 상품유형코드
		
		//착불일경우 상품명앞 (착불) 표기 21.11.05
		if("2".equals(paLtonGoods.getShipCostReceipt())) {
			paLtonGoods.setSpdNm("(착불)" + paLtonGoods.getSpdNm());
		}
		
		if(paLtonGoods.getSpdNm().length() < 40) {
			map.put("spdNm", paLtonGoods.getSpdNm()); // 판매자상품명 : 우리꺼 GOODS_NAME
		}else {
			map.put("spdNm", paLtonGoods.getSpdNm().substring(0,38) + "..."); // 판매자상품명 : 우리꺼 GOODS_NAME
		}
		
		
		map.put("brdNo", ComUtil.NVL(paLtonGoods.getBrdNo(), "")); // 브랜드번호
		map.put("mfcrNm", paLtonGoods.getMfcrNm()); // 제조사명
		map.put("oplcCd", paLtonGoods.getOplcCd()); // 원산지코드
//		map.put("oplcCdLst", oplcCdLst); // 원산지코드 목록 : 일단 보류, 없이 해보고 얘 때문에 오류나면 추가하기
		map.put("tdfDvsCd", paLtonGoods.getTaxYn().equals("1") ? "01" : "02"); // 과세유형코드
		map.put("slStrtDttm", paLtonGoods.getSlStrtDttm()); // 판매시작일시
		map.put("slEndDttm", paLtonGoods.getSlEndDttm()); // 판매종료일시
		
		//상품고시정보 셋팅
		pdItmsInfo.put("pdItmsCd", paLtonGoodsOffer.get(0).getPaOfferType()); // 상품품목코드 
		for(int i = 0; i < paLtonGoodsOffer.size(); i++) {
			pdItmsArtInfo = new HashMap<String, Object>();
			pdItmsArtInfo.put("pdArtlCd", paLtonGoodsOffer.get(i).getPaOfferCode()); // 상품항목코드
			if("0200".equals(paLtonGoodsOffer.get(i).getPaOfferCode())){
				pdArtlCnts = "Y";
				pdItmsArtInfo.put("pdArtlCnts", pdArtlCnts); // 상품항목내용
			}else {
				pdItmsArtInfo.put("pdArtlCnts", paLtonGoodsOffer.get(i).getPaOfferExt()); // 상품항목내용
			}
			pdItmsArtlLst.add(pdItmsArtInfo);
		}
		pdItmsInfo.put("pdItmsArtlLst", pdItmsArtlLst); // 상품품목항목목록
		map.put("pdItmsInfo", pdItmsInfo);
		
		if("Y".equals(pdArtlCnts)) {
			sftyAthnLstMap.put("sftyAthnTypCd", "DTL_REFC");
			sftyAthnLstMap.put("sftyAthnNo", "상세페이지 참조");
			sftyAthnLst.add(sftyAthnLstMap);
			map.put("sftyAthnLst", sftyAthnLst);
		}
		
		if(paLtonGoods.getOrderMinQty() > 0) {
			purPsbQtyInfo.put("itmByMinPurYn", "Y"); 		 // 단품별최소구매여부
			purPsbQtyInfo.put("itmByMinPurQty", paLtonGoods.getOrderMinQty()); 	 // 단품별최소구매수량
		} else {
			purPsbQtyInfo.put("itmByMinPurYn", "N"); 		 // 단품별최소구매여부
		}
		
		if(paLtonGoods.getOrderMaxQty() > 0) {
			purPsbQtyInfo.put("itmByMaxPurPsbQtyYn", "Y"); // 단품별최대구매가능수량여부
			purPsbQtyInfo.put("maxPurQty", paLtonGoods.getOrderMaxQty()); 			 // 단품별최대구매수량
		} else {
			purPsbQtyInfo.put("itmByMaxPurPsbQtyYn", "N"); // 단품별최대구매가능수량여부
		}
		map.put("purPsbQtyInfo", purPsbQtyInfo);
		map.put("ageLmtCd", paLtonGoods.getAdultYn().equals("0") ? "0" : "19"); // 연령제한코드
		map.put("ctrtTypCd", "A"); // 계약유형코드 : 문의필요
		map.put("pdStatCd", "NEW"); // 상품상태코드 : 새상품
		map.put("dpYn", "Y"); // 전시여부
//		검색키워드목록 scKwdLst [array] : 예시도 없어 어떤 변수명으로 받는지 몰라서 일단 패스
		
		epnLstMap.put("pdEpnTypCd", "DSCRP"); // 상품설명유형코드 : DSCRP(상품기술서)
		
		String goodsCom = "";
		
		goodsCom = (!ComUtil.NVL(paLtonGoods.getGoodsCom()).equals("")) ? ("<div style=\"line-height: 2.0em; font-family: 'NanumBarunGothic'; font-size: 19px;\"><div><h4>&middot;&nbsp;상품구성<h4><pre>" + paLtonGoods.getGoodsCom() + "</pre></div></div>") : "";
		
		if("".equals(paLtonGoods.getCollectImage()) || paLtonGoods.getCollectImage() == null) {
			goodsDescData = ("<div align='center'><img alt='' src='" + paLtonGoods.getTopImage() + "' /><br /><br /><br />" 
					+ goodsCom
					+ paLtonGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" 
					+ paLtonGoods.getBottomImage() + "' /></div>");
		} else {
			goodsDescData = ("<div align='center'><img alt='' src='" + paLtonGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + paLtonGoods.getCollectImage() + "' /><br /><br /><br />"
					+ goodsCom
					+ paLtonGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" 
					+ paLtonGoods.getBottomImage() + "' /></div>");
		}
		
		if(!ComUtil.NVL(paLtonGoods.getNoticeExt()).equals("")) {
			goodsDescData = paLtonGoods.getNoticeExt() + goodsDescData;
		}
		
		epnLstMap.put("cnts", goodsDescData); // 기술서html
		epnLst.add(epnLstMap);
		map.put("epnLst", epnLst); // 상품설명목록
		map.put("dvProcTypCd", "LO_ENTP"); // 배송처리유형코드(LO_ENTP : e커머스 업체배송)
		
		if("2".equals(paLtonGoods.getShipCostReceipt())) { // 착불상품
			map.put("dvPdTypCd", "CHRG_INST"); // 배송상품유형코드 : [CHRG_INST, 유료설치상품]
			map.put("sndBgtNday", "3"); // 발송예정일수
			map.put("dvMnsCd", "DGNN_DV"); // 배송수단코드 DGNN_DV : 전담배송
		} else if("1".equals(paLtonGoods.getInstallYn())) { //설치배송 상품
			map.put("dvPdTypCd", "FREE_INST"); // 배송상품유형코드 : [FREE_INST, 무료설치상품]
			map.put("sndBgtNday", "3"); // 발송예정일수
			map.put("dvMnsCd", "DGNN_DV"); // 배송수단코드 DGNN_DV : 전담배송
		} else if("1".equals(paLtonGoods.getOrderCreateYn())) {
			map.put("dvPdTypCd", "OD_MFG"); // 배송상품유형코드 : [OD_MFG, 주문제작상품]
			map.put("sndBgtNday", "15"); // 발송예정일수
			map.put("dvMnsCd", "DPCL"); // 배송수단코드 DPCL : 택배
		} else {
			map.put("dvPdTypCd", "GNRL"); // 배송상품유형코드 : [GNRL, 일반]
			map.put("sndBgtNday", "3"); // 발송예정일수
			map.put("dvMnsCd", "DPCL"); // 배송수단코드 DPCL : 택배
		}
		
		if("1".equals(paLtonGoods.getReturnNoYn()) && ("1".equals(paLtonGoods.getInstallYn()) || "1".equals(paLtonGoods.getOrderCreateYn()))) { // 설치/주문제작상품 중 교환/반품불가인 경우
			map.put("rtngPsbYn", "N"); // 반품불가
			map.put("xchgPsbYn", "N"); // 교환불가
		} else {
			map.put("rtngPsbYn", "Y");
			map.put("xchgPsbYn", "Y");
		}
		
		if(paLtonGoods.getDelynoAreaCnt() > 0) { // 제주지역, 도서산간 배송불가 상품
			map.put("dvRgsprGrpCd", "GN102"); // 배송권역그룹코드 : GN102[전국(제주도 및 도서지역 제외)]
		} else {
			map.put("dvRgsprGrpCd", "GN000"); // 배송권역그룹코드 : GN000[전국]
			map.put("adtnDvCstPolNo", paLtonGoods.getDvCstPolNo()); //도서산간,제주 추가배송비
		}
		
		
		map.put("owhpNo", paLtonGoods.getOwhpNo()); // 출고지번호
		map.put("dvCstPolNo", paLtonGoods.getGroupCode()); // 배송비정책번호
		
		if("ID".equals(paLtonGoods.getShipCostCode().toString().substring(0, 2))) {
			map.put("cmbnRtngPsbYn", "N"); // 합반품가능여부
			map.put("cmbnDvPsbYn", "N"); // 합배송가능여부
		}else {
			map.put("cmbnRtngPsbYn", "Y"); // 합반품가능여부
			map.put("cmbnDvPsbYn", "Y"); // 합배송가능여부
		}
		map.put("rtrpNo", paLtonGoods.getRtrpNo()); // 회수지번호
		map.put("stkMgtYn", "Y"); // 재고관리여부
		map.put("sitmYn", "Y"); // 판매자단품여부
		
		//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
		if(paPromoTargetList != null && paPromoTargetList.size() > 0) {
			for(PaPromoTarget paPromoTarget : paPromoTargetList) {
				if(paPromoTarget != null) {
					if(!paPromoTarget.getProcGb().equals("D")) {
						log.info("### couponPrice : "+paPromoTarget.getDoCost());
						couponPrice += paPromoTarget.getDoCost();	//할인금액(자동적용쿠폰 + 제휴OUT)
					}
				}
			}
		}
		
		goodsPrice = paLtonGoods.getSalePrice() - (paLtonGoods.getDcAmt() + paLtonGoods.getLumpSumDcAmt() + couponPrice);
		
		for(int i = 0; i < goodsdtMapping.size(); i++) {
			itmImgLstMap 	 = new HashMap<String, Object>();
			
			itmLstMap 		 = new HashMap<String, Object>();
			itmOptLst 	 	 = new ArrayList<Map<String, Object>>();
			itmImgLst 	 	 = new ArrayList<Map<String, Object>>();
			itmOptLstMap 	 = new HashMap<String, Object>();
			
			eitmNo = goodsdtMapping.get(i).getPaCode() + goodsdtMapping.get(i).getGoodsCode() + goodsdtMapping.get(i).getGoodsdtCode() + goodsdtMapping.get(i).getGoodsdtSeq(); 
			itmLstMap.put("eitmNo", eitmNo); // 업체단품번호
			itmLstMap.put("sortSeq", String.valueOf(i)); // 정렬순서
			if("1".equals(goodsdtMapping.get(i).getUseYn())) {
				itmLstMap.put("dpYn", "Y"); // 전시여부				
			}else {
				itmLstMap.put("dpYn", "N"); // 전시여부
			}
			itmLstMap.put("slPrc", goodsPrice); // 판매가
			itmLstMap.put("stkQty", goodsdtMapping.get(i).getTransOrderAbleQty()); // 재고수량
			if(StringUtil.isNotEmpty(goodsdtMapping.get(i).getPaOptionCode())){
				itmLstMap.put("sitmNo",goodsdtMapping.get(i).getPaOptionCode());
			}
			
			itmOptLstMap.put("optNm", "색상/크기/무늬/형태"); // 옵션명
			itmOptLstMap.put("optVal", goodsdtMapping.get(i).getGoodsdtInfo()); // 옵션값
			itmOptLst.add(itmOptLstMap);
			
			itmImgLstMap.put("epsrTypCd", "IMG"); // 노출유형코드
			itmImgLstMap.put("epsrTypDtlCd", "IMG_SQRE"); // 노출유형상세코드
			itmImgLstMap.put("origImgFileNm", image_address + paLtonGoods.getImageUrl() + paLtonGoods.getImageP() + "?v=" + currTime); // 원본이미지파일명
			itmImgLstMap.put("rprtImgYn", "Y"); // 대표이미지여부
			itmImgLst.add(itmImgLstMap);
			
			if(StringUtil.isNotEmpty(paLtonGoods.getImageAP())) {
				itmImgLstMap = new HashMap<String, Object>();
				itmImgLstMap.put("epsrTypCd", "IMG"); // 노출유형코드
				itmImgLstMap.put("epsrTypDtlCd", "IMG_SQRE"); // 노출유형상세코드
				itmImgLstMap.put("origImgFileNm", image_address + paLtonGoods.getImageUrl() + paLtonGoods.getImageAP() + "?v=" + currTime); // 원본이미지파일명
				itmImgLstMap.put("rprtImgYn", "N"); // 대표이미지여부
				itmImgLst.add(itmImgLstMap);
			}
			if(StringUtil.isNotEmpty(paLtonGoods.getImageBP())) {
				itmImgLstMap = new HashMap<String, Object>();
				itmImgLstMap.put("epsrTypCd", "IMG"); // 노출유형코드
				itmImgLstMap.put("epsrTypDtlCd", "IMG_SQRE"); // 노출유형상세코드
				itmImgLstMap.put("origImgFileNm", image_address + paLtonGoods.getImageUrl() + paLtonGoods.getImageBP() + "?v=" + currTime); // 원본이미지파일명
				itmImgLstMap.put("rprtImgYn", "N"); // 대표이미지여부
				itmImgLst.add(itmImgLstMap);
			}
			if(StringUtil.isNotEmpty(paLtonGoods.getImageCP())) {
				itmImgLstMap = new HashMap<String, Object>();
				itmImgLstMap.put("epsrTypCd", "IMG"); // 노출유형코드
				itmImgLstMap.put("epsrTypDtlCd", "IMG_SQRE"); // 노출유형상세코드
				itmImgLstMap.put("origImgFileNm", image_address + paLtonGoods.getImageUrl() + paLtonGoods.getImageCP() + "?v=" + currTime); // 원본이미지파일명
				itmImgLstMap.put("rprtImgYn", "N"); // 대표이미지여부
				itmImgLst.add(itmImgLstMap);
			}
			itmLstMap.put("itmImgLst", itmImgLst);
			itmLstMap.put("itmOptLst", itmOptLst);
			
			itmLst.add(itmLstMap);
		}
		
		map.put("itmLst", itmLst);
		spdLst.add(map);
		
		return spdLst;
	}
	
	/**
	 * 전송관리 테이블 INSERT
	 * @param paLtonGoods
	 * @param resultData
	 * @throws Exception
	 */
	/* asyncController 로 옮김
	private void insertPaGoodsTransLog(PaLtonGoodsVO paLtonGoods, Map<String, Object> resultData) throws Exception {
		String paGoodsCode = String.valueOf(resultData.get("spdNo"));
		String dateTime = systemService.getSysdatetimeToString();;
		
		PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
		paGoodsTransLog.setGoodsCode(paLtonGoods.getEpdNo());
		paGoodsTransLog.setPaCode(paLtonGoods.getPaCode());
		paGoodsTransLog.setItemNo(paGoodsCode.equals("null") ? paLtonGoods.getEpdNo() : paGoodsCode);
		paGoodsTransLog.setRtnCode(String.valueOf(resultData.get("resultCode")));
		paGoodsTransLog.setRtnMsg(String.valueOf(resultData.get("resultMessage")));
		paGoodsTransLog.setSuccessYn(paGoodsTransLog.getRtnCode().equals(Constants.PA_LTON_SUCCESS_CODE) ? "1" : "0");
		paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		paGoodsTransLog.setProcId(Constants.PA_LTON_PROC_ID);
		paLtonGoodsService.insertPaLtonGoodsTransLogTx(paGoodsTransLog);
	}
	*/
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "재고 API", notes = "재고 API", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goods-stock", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> GoodsStock(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = false) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_LTON_PROC_ID) String procId) throws Exception{
	
		log.info("===== 롯데온 재고 API Start =====");		
		String rtnMsg = Constants.SAVE_SUCCESS;
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>(); 
		ParamMap bodyMap = new ParamMap();			
		List<PaLtonGoodsdtMappingVO> paGoodsdtMappingList = null;
		PaLtonGoodsdtMappingVO paGoodsdtMapping = null;
		List<PaLtonGoodsdtMappingVO> paGoodsdtList = null;
		String prg_id = "IF_PALTONAPI_01_006";
		String method = "POST";
		String duplicateCheck = "";
		String dateTime = "";
		
		try {
			log.info("02.롯데온 재고 API 중복확인 체크  Select");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", prg_id);
			paramMap.put("paGroupCode", Constants.PA_LTON_GROUP_CODE);
			paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);			
        	paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);			
        	paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
        	paramMap.put("method", method);
        	paramMap.put("procId", procId);
        	paramMap.put("goodsCode", goodsCode);
        	paramMap.put("paCode", paCode);
        	
        	apiInfo = systemService.selectPaApiInfo(paramMap);
			paramMap.put("dateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paramMap.put("url", apiInfo.get("API_URL"));
			
			log.info("03.재고 API수정 대상 조회");
			paGoodsdtMappingList = paLtonGoodsService.selectPaLtonGoodsdtMappingStock(paramMap);
			
			if(paGoodsdtMappingList.size() == 0) {
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			for(int i = 0; i < paGoodsdtMappingList.size(); i++) {
				List<Map<String,Object>> itmStkLst_list = new ArrayList<Map<String,Object>>();
				paGoodsdtMapping = paGoodsdtMappingList.get(i);
				
				paramMap.put("goodsCode", paGoodsdtMapping.getGoodsCode());
				paramMap.put("paCode", paGoodsdtMapping.getPaCode());
				paGoodsdtList = paLtonGoodsService.selectPaLtonGoodsdtMappingStockList(paramMap);
			
				for(int j = 0 ; j < paGoodsdtList.size(); j++) {
					Map<String,Object> itmStkLst_map = new HashMap<String,Object>();
					paGoodsdtList.get(j).setTransStockYn("0");
					paGoodsdtList.get(j).setModifyDate(Timestamp.valueOf(paramMap.getString("dateTime")));
					paGoodsdtList.get(j).setModifyId(paramMap.getString("procId"));
					
					if(paGoodsdtList.get(j).getPaOptionCode() == null) {
						paramMap.put("code", "404");
						paramMap.put("message",getMessage("partner.insert_entpgoodsdt"));
						continue;
					}
					
					if(paGoodsdtMapping.getPaCode().equals(Constants.PA_LTON_BROAD_CODE)) {
						itmStkLst_map.put("lrtrNo",apiInfo.get(Constants.PA_BROAD));		//하위 거래처 번호
					} else {
						itmStkLst_map.put("lrtrNo",apiInfo.get(Constants.PA_ONLINE));		//하위 거래처 번호
					}
					itmStkLst_map.put("trNo",ConfigUtil.getString("PALTON_ENTP_CODE"));
					itmStkLst_map.put("trGrpCd","SR");	
					itmStkLst_map.put("spdNo",paGoodsdtList.get(j).getSpdNo());
					itmStkLst_map.put("sitmNo", paGoodsdtList.get(j).getPaOptionCode());
					itmStkLst_map.put("stkQty", Integer.parseInt(paGoodsdtList.get(j).getTransOrderAbleQty()));
					
					itmStkLst_list.add(itmStkLst_map);
				}
				bodyMap.put("itmStkLst", itmStkLst_list);
				map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, bodyMap); // 통신

				ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) map.get("data");
				if(Constants.PA_LTON_SUCCESS_CODE.equals(data.get(0).get("resultCode"))) {
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
					log.info("롯데온 재고 변경 성공 GOODS_CODE : " + paGoodsdtMapping.getGoodsCode());
					rtnMsg = paLtonGoodsService.savePaLtonGoodsStockTx(paGoodsdtList);
					}
				log.info("롯데온 상품 재고 수정 동기화 완료 저장");
				if(!rtnMsg.equals("000000")){
					paramMap.put("code","404");
					paramMap.put("message",getMessage("pa.not_exists_process_list"));
					continue;
				} else {
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}
			}
		} catch (Exception e) {
			paramMap.put("code", ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				paramMap.put("message", paramMap.getString("message"));
				paramMap.put("code", 	paramMap.getString("code"));
				systemService.insertApiTrackingTx(request, paramMap);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
		log.info("===== 롯데온 상품 재고 변경 API End =====");
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "단품판매상태변경", notes = "단품판매상태변경", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goodsdt-sellState", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> setGoodsDtSellState(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = false) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_LTON_PROC_ID) String procId) throws Exception{
	
		log.info("===== 롯데온 단품 판매상태 수정 Start =====");		
		String rtnMsg = Constants.SAVE_SUCCESS;
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>(); 
		ParamMap bodyMap = new ParamMap();			
		List<PaLtonGoodsdtMappingVO> paGoodsdtMappingList = null;
		PaLtonGoodsdtMappingVO paGoodsdtMapping = null;
		List<PaLtonGoodsdtMappingVO> paGoodsdtList = null;
		String prg_id = "IF_PALTONAPI_01_009";
		String method = "POST";
		String duplicateCheck = "";
		String dateTime = "";
		
		try {
			log.info("02.롯데온 단품 판매상태 수정 중복확인 체크  Select");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", prg_id);
			paramMap.put("paGroupCode", Constants.PA_LTON_GROUP_CODE);
			paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);			
        	paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);			
        	paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
        	paramMap.put("method", method);
        	paramMap.put("procId", procId);
        	paramMap.put("goodsCode", goodsCode);
        	paramMap.put("paCode", paCode);
        	
        	apiInfo = systemService.selectPaApiInfo(paramMap);
			paramMap.put("dateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paramMap.put("url", apiInfo.get("API_URL"));
			
			log.info("03.단품 판매상태 수정 대상 조회");
			paGoodsdtMappingList = paLtonGoodsService.selectPaLtonDtSellState(paramMap);
			
			if(paGoodsdtMappingList.size() == 0) {
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			for(int i = 0; i < paGoodsdtMappingList.size(); i++) {
				List<Map<String,Object>> dtSaleLst_list = new ArrayList<Map<String,Object>>();
				paGoodsdtMapping = paGoodsdtMappingList.get(i);
				
				paramMap.put("goodsCode", paGoodsdtMapping.getGoodsCode());
				paramMap.put("paCode", paGoodsdtMapping.getPaCode());
				paGoodsdtList = paLtonGoodsService.selectPaLtonDtSellStateList(paramMap);
			
				for(int j = 0 ; j < paGoodsdtList.size(); j++) {
					Map<String,Object> dtSaleLst_map = new HashMap<String,Object>();
					paGoodsdtList.get(j).setTransSaleYn("0");
					paGoodsdtList.get(j).setModifyDate(Timestamp.valueOf(paramMap.getString("dateTime")));
					paGoodsdtList.get(j).setModifyId(paramMap.getString("procId"));
					
					if(paGoodsdtList.get(j).getPaOptionCode() == null) {
						paramMap.put("code", "404");
						paramMap.put("message",getMessage("partner.insert_entpgoodsdt"));
						continue;
					}
					
					if(paGoodsdtMapping.getPaCode().equals(Constants.PA_LTON_BROAD_CODE)) {
						dtSaleLst_map.put("lrtrNo",apiInfo.get(Constants.PA_BROAD));		//하위 거래처 번호
					} else {
						dtSaleLst_map.put("lrtrNo",apiInfo.get(Constants.PA_ONLINE));		//하위 거래처 번호
					}
					dtSaleLst_map.put("trNo",ConfigUtil.getString("PALTON_ENTP_CODE"));
					dtSaleLst_map.put("trGrpCd","SR");	
					dtSaleLst_map.put("spdNo",paGoodsdtList.get(j).getSpdNo());
					dtSaleLst_map.put("sitmNo", paGoodsdtList.get(j).getPaOptionCode());
					dtSaleLst_map.put("slStatCd", "SALE"); //TPALTONGOODSDTMAPPING에 trans_sale_yn을 1로 업데이트 하는 경우는 재고 0에서 재고가 들어오는 경우밖에 없기때문
					
					dtSaleLst_list.add(dtSaleLst_map);
				}
				bodyMap.put("sitmLst", dtSaleLst_list);
				map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, bodyMap); // 통신

				ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) map.get("data");
				if(Constants.PA_LTON_SUCCESS_CODE.equals(data.get(0).get("resultCode"))) {
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
					log.info("롯데온 단품 판매상태 변경 성공 GOODS_CODE : " + paGoodsdtMapping.getGoodsCode());
					rtnMsg = paLtonGoodsService.savePaLtonGoodsStockTx(paGoodsdtList);
					}
				log.info("롯데온 단품 판매상태 변경 완료 저장");
				if(!rtnMsg.equals("000000")){
					paramMap.put("code","404");
					paramMap.put("message",getMessage("pa.not_exists_process_list"));
					continue;
				} else {
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}
			}
		} catch (Exception e) {
			paramMap.put("code", ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				paramMap.put("message", paramMap.getString("message"));
				paramMap.put("code", 	paramMap.getString("code"));
				systemService.insertApiTrackingTx(request, paramMap);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
		log.info("===== 롯데온 단품 판매상태 변경 API End =====");
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	public boolean excetpCheck(ParamMap paramMap, HashMap<String, String> paGoodsTarget) throws Exception {
		
		paramMap.put("paGroupCode"	, "08");
		paramMap.put("marginCode"	, "82");
		paramMap.put("minpriceCode"	, "83");

		String exceptStr = paCommonServic.paExceptGoodsYn(paramMap);
		if(!exceptStr.equals("000000")){
			paGoodsTarget.put("RETURN_NOTE"	, exceptStr);
			paGoodsTarget.put("PA_STATUS"	, "20");
			paGoodsTarget.put("MODIFY_ID"	, paramMap.getString("modifyId"));
			paLtonGoodsService.updatePaLtonGoodsFail(paGoodsTarget);
			return false;
		}
		return true;
	}

	
	@SuppressWarnings("unchecked")
	public int goodsInsert(HttpServletRequest request, HashMap<String, String> apiInfo, ParamMap paramMap, String searchTermGb) {
		ResponseEntity<?> responseMsg 				 = null;
		Map<String, Object> map		  				 = new HashMap<String, Object>();
		ArrayList<Map<String,Object>> paramGoodsInfo = new ArrayList<Map<String,Object>>();
		String rtnMsg 								 = Constants.SAVE_SUCCESS;
		ParamMap bodyMap							 = new ParamMap(); 
		
		String goodsCode = paramMap.getString("goodsCode");
		String dateTime  = paramMap.getString("dateTime");
		String procId	 = paramMap.getString("modifyId");
		
		try {
			//상품 출고지 담당자 주소 체크
			paramMap.put("paAddrGb", "30");
			PaEntpSlip entpSlip = paLtonGoodsService.selectPaLtonEntpSlip(paramMap);
			
			if(null != entpSlip) {
				responseMsg = paLtonCommonController.entpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaAddrGb(), entpSlip.getPaCode() ,searchTermGb);
				log.info("03-1.롯데온 상품등록 API 출고지 등록 CODE CHECK :" + PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")) {
					paramMap.put("code", "440");
					paramMap.put("message", getMessage("pa.check_entp_slip_man_seq", new String[] {" 상품코드 : " + goodsCode}));
					return 0;
				}
			}
			
			//상품 회수지 담당자 주소 체크
			paramMap.put("paAddrGb", "20");
			entpSlip = paLtonGoodsService.selectPaLtonEntpSlip(paramMap);
			
			if(null != entpSlip) {
				responseMsg = paLtonCommonController.entpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaAddrGb(), entpSlip.getPaCode(), searchTermGb);
				log.info("03-1.롯데온 상품등록 API 회수지 등록 CODE CHECK :" + PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")) {
					paramMap.put("code", "440");
					paramMap.put("message", getMessage("pa.check_return_slip_man_seq", new String[] {" 상품코드 : " + goodsCode}));
					return 0;
					//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}
			
			log.info("04.롯데온 상품등록 API 배송비 정책 등록");
			paLtonCommonController.entpCustShipCostInsert(request, searchTermGb);
			
			//상품 수정 중 동기화배치가 돌아 상품정보 수정될 경우 다시 수정대상에 포함 하기위해 MODIFY_DATE와  수정대상조회 시작시간을 비교함
			String goodsListTime = systemService.getSysdatetimeToString();
			
			log.info("05.상품정보  Select");
			PaLtonGoodsVO paLtonGoods = paLtonGoodsService.selectPaLtonGoodsInfo(paramMap);
			
			if(paLtonGoods == null){
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("pa.not_exists_process_list"));
				return 0;
			}
			
			if(paLtonGoods.getPaCode().equals(Constants.PA_LTON_BROAD_CODE)){
				paLtonGoods.setLrtrNo(apiInfo.get(Constants.PA_BROAD));		//하위 거래처 번호
			} else {
				paLtonGoods.setLrtrNo(apiInfo.get(Constants.PA_ONLINE));	//하위 거래처 번호
			}
			paLtonGoods.setTrNo(ConfigUtil.getString("PALTON_ENTP_CODE"));		
			
			//정보고시
			List<PaGoodsOffer> paLtonGoodsOffer = paLtonGoodsService.selectPaLtonGoodsOfferList(paramMap);
			
			if(paLtonGoodsOffer.size() < 1){
				paramMap.put("code", "430");
				paramMap.put("message", getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
				return 0;
			}
			
			///옵션정보
			List<PaLtonGoodsdtMappingVO> goodsdtMapping = paLtonGoodsService.selectPaLtonGoodsdtInfoList(paramMap);
			
			//프로모션정보 추가 예정
			List<PaPromoTarget> paPromoTargetList = paLtonGoodsService.selectPaPromoTarget(paramMap);
			
			paramGoodsInfo = makeGoodsInfo(paLtonGoods, paLtonGoodsOffer, goodsdtMapping, paPromoTargetList); // body 파라미터 값 생성
			
			bodyMap.put("spdLst", paramGoodsInfo);
			
			map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, bodyMap); // 통신
			
			ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) map.get("data");
			
			if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("returnCode"))) {
				
				Map<String, Object> resultData = data.get(0);
				
				paramMap.put("code", "200");
				paramMap.put("message", resultData.get("resultMessage"));
				
				paLtonAsyncController.insertPaGoodsTransLog(paLtonGoods, resultData);
				
				String paGoodsCode = String.valueOf(resultData.get("spdNo"));
				
				if(!"null".equals(paGoodsCode)){
					paLtonGoods.setSpdNo(paGoodsCode);
					
					paLtonGoods.setApprovalStatus("10");
					paLtonGoods.setPaStatus("30");
					paLtonGoods.setModifyId(procId);
					paLtonGoods.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paLtonGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paLtonGoods.setLastModifyDate(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));
					
					rtnMsg = paLtonGoodsService.savePaLtonGoodsTx(paLtonGoods, goodsdtMapping, paPromoTargetList);
				}
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
					paramMap.put("code", "404");
					paramMap.put("message", resultData.get("resultMessage"));
					return 0;
				} else {
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
				}
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", map.get("message"));
				return 0;
			}
			return 1;
		
			
			} catch (Exception e) {
				paramMap.put("code"		, "500");
				paramMap.put("message"	, e.toString());
				return 0;
			} finally {
				if(!paramMap.getString("code").equals("200")) {
					log.info(paramMap.getString("goodsCode") + "(" + paramMap.getString("paCode") + ") : " + paramMap.getString("code") + " " + paramMap.getString("message"));
				}
			}
	}
	
	
	public void paLtonGoodsModify(HttpServletRequest request, PaLtonGoodsVO paLtonGoods, HashMap<String, String> apiInfo , ParamMap paramMap ) throws Exception{
		List<PaGoodsOffer> paLtonGoodsOffer 		  = null;
		List<PaLtonGoodsdtMappingVO> goodsdtMapping   = null;
		List<PaPromoTarget> paPromoTargetList 		  = null;
		ArrayList<Map<String,Object>> paramGoodsInfo  = new ArrayList<Map<String,Object>>();
		ParamMap bodyMap 		= new ParamMap();
		ParamMap asyncMap		= new ParamMap();		
		String method 			= "POST";
		String procId 			= paramMap.getString("modifyId");
		String dateTime 		= paramMap.getString("dateTime");
		String goodsListTime	= paramMap.getString("goodsListTime");
		String prg_id			= paramMap.getString("apiCode");
		String goodsCode		= paLtonGoods.getGoodsCode();

		paLtonGoods.setModifyId			(procId);
		paLtonGoods.setInsertDate		(DateUtil.toTimestamp(dateTime		, "yyyy/MM/dd HH:mm:ss"));
		paLtonGoods.setModifyDate		(DateUtil.toTimestamp(dateTime		, "yyyy/MM/dd HH:mm:ss"));
		paLtonGoods.setLastModifyDate	(DateUtil.toTimestamp(goodsListTime	, "yyyy/MM/dd HH:mm:ss"));
			
		if(paLtonGoods.getPaCode().equals(Constants.PA_LTON_BROAD_CODE)){
			paLtonGoods.setLrtrNo(apiInfo.get(Constants.PA_BROAD));		//하위 거래처 번호
		} else {
			paLtonGoods.setLrtrNo(apiInfo.get(Constants.PA_ONLINE));	//하위 거래처 번호
		
		}
		paLtonGoods.setTrNo(ConfigUtil.getString("PALTON_ENTP_CODE"));	
			
		asyncMap.put("url"		, apiInfo.get("API_URL"));
		asyncMap.put("paCode"	,paLtonGoods.getPaCode());
		asyncMap.put("method"	, method);
		asyncMap.put("siteGb"	, Constants.PA_LTON_PROC_ID);
		asyncMap.put("apiCode"	, prg_id);					
		
		log.info("GOODS_CODE : " + paLtonGoods.getGoodsCode());
			//수정할 롯데온 상품 코드가 존재하지 않을시 처리
		if(StringUtil.isEmpty(paLtonGoods.getSpdNo())){
			paramMap.put("code","411");
			paramMap.put("message",getMessage("pa.not_exists_goods", new String[] {"상품코드 : " + goodsCode}));
			return;
		}
			
		//수정할 상품 상세설명 존재하지 않을시 처리
		if(StringUtil.isEmpty(paLtonGoods.getDescribeExt())){
			paramMap.put("code","420");
			paramMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
			return;
		}
			
		paramMap.put("paGroupCode", paLtonGoods.getPaGroupCode());
		paramMap.put("goodsCode", paLtonGoods.getGoodsCode());
		paramMap.put("paCode", paLtonGoods.getPaCode());
		
		//상품 정보고시 조회
		paLtonGoodsOffer = paLtonGoodsService.selectPaLtonGoodsOfferList(paramMap);
		if(paLtonGoodsOffer.size() < 1){
			paramMap.put("code","430");
			paramMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
			return;
		}
		
		///옵션정보
		goodsdtMapping = paLtonGoodsService.selectPaLtonGoodsdtInfoList(paramMap);
		
		//프로모션정보 
		paPromoTargetList = paLtonGoodsService.selectPaPromoTarget(paramMap);
			
		paramGoodsInfo = makeGoodsInfo(paLtonGoods, paLtonGoodsOffer, goodsdtMapping, paPromoTargetList); // body 파라미터 값 생성
			
		bodyMap.put("spdLst", paramGoodsInfo);
			
		//비동기처리
		paLtonAsyncController.asyncGoodsModify(request, asyncMap, bodyMap, paLtonGoods, goodsdtMapping, paPromoTargetList);
		Thread.sleep(50);
	}
	
}
