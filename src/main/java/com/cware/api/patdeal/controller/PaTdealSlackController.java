package com.cware.api.patdeal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.model.PaTdealSlackGoods;
import com.cware.netshopping.patdeal.service.PaTdealSlackService;
import com.cware.netshopping.patdeal.util.PaTdealConnectUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller("com.cware.api.patdeal.PaTdealSlackController")
@RequestMapping(value="/patdeal/slack")
public class PaTdealSlackController extends AbstractController{

	@Autowired 
	@Qualifier("patdeal.slack.paTdealSlackService")
	private PaTdealSlackService paTdealSlackService;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	private PaTdealConnectUtil paTdealConnectUtil;
	
	@Value("${partner.tdeal.api.slack.channel}")
	String TDEAL_SLACK_CHANNEL_URL;
	
	/**
	 * 상품 변경 정보 Tdeal slack 공유
	 * 
	 * @param request
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "슬랙 상품 변경점 전송", notes = "슬랙 상품 변경점 전송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "전송 실패"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/goods-info-send")
	public ResponseEntity<ResponseMsg> slackTransferTdealList(HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PATDEALAPI_06_001";
		String duplicateCheck = "";
		String dateTime = "";
		boolean checkFlag = true;
		
		List<PaTdealSlackGoods> slackTransferInfo = null;
		
		try {
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", apiCode);
			paramMap.put("startDate", dateTime);
			paramMap.put("siteGb", Constants.PA_TDEAL_PROC_ID);
			paramMap.put("url", "https://hooks.slack.com/services/" + TDEAL_SLACK_CHANNEL_URL);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
			
			slackTransferInfo = paTdealSlackService.slackTransferTdealList(request);
			
			if (slackTransferInfo.size() > 0) {
				Map<String, Object> finalResultMap = new HashMap<>();
		        List<Map<String, Object>> finalResults = new ArrayList<>();

		        int chunkSize = 6; // 해당 API는 제목 포함하여 ITEM항목이 50개가 넘어가면 안됨. 상품가격, 상품명, 상품코드, 구분선 모두 있다고 가정했을 경우 6개의 상품이 한번에 통신할 수 있는 최대치
		        int totalSize = slackTransferInfo.size();
		        for (int i = 0; i < totalSize; i += chunkSize) {
		        	Map<String, Object> returnMap = new HashMap<String, Object>();
		        	String titleYn = i == 0 ? "Y" : "N";
		        	List<PaTdealSlackGoods> chunk = slackTransferInfo.subList(i, Math.min(totalSize, i + chunkSize));
	                
		        	ParamMap apiDataMap = makeProductInfo(chunk, titleYn);
	                String body = paTdealConnectUtil.getBody(apiDataMap);
	                paramMap.put("body", body);
	                
	                String result = paTdealConnectUtil.connectSlackTransfer(paramMap);
	                
	                returnMap.put("result", result);
	                finalResults.add(returnMap);
		        }
		        finalResultMap.put("results", finalResults);
		        
		        for (Map<String, Object> checkResult : finalResults) {
		        	if (!"ok".equals(checkResult.get("result"))) {
		        		checkFlag = false;
		        		paramMap.put("message", checkResult.get("result"));
		        	}
		        }
		        
		        if (checkFlag) {
		        	paramMap.put("code", "200");
		        	paramMap.put("message", "SUCCESS");
		        } else {
		        	paramMap.put("code", "500");
		        }
			}
		} catch (Exception e) {
			if ("1".equals(duplicateCheck)) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		} finally {
			try {
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_TDEAL_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if ("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 전문 생성
	 * @param slackTransferInfo
	 * @return
	 * @throws Exception
	 */
	private ParamMap makeProductInfo(List<PaTdealSlackGoods> slackTransferInfo, String titleYn) throws Exception {
		ParamMap slackGoodsMap = new ParamMap();
		List<Map<String, Object>> blocks  = new ArrayList<Map<String, Object>>(); // 전문의 최상위
		List<Map<String, Object>> fields  = null;
		Map<String, Object> insertItemSub = null;
		Map<String, Object> insertItem 	  = null;
		Map<String, Object> mainText 	  = new HashMap<String, Object>(); // 제목(상품정보 변경이력)
		Map<String, Object> textInfo 	  = new HashMap<String, Object>();
		Map<String, Object> division 	  = new HashMap<String, Object>();
		
		String dateTime = systemService.getSysdatetimeToString();
		
		if ("Y".equals(titleYn)) {
			textInfo.put("type", "mrkdwn");
			textInfo.put("text", "*[" + dateTime + "] SK스토아 - T deal 상품정보 변경이력*");
			mainText.put("type", "section");
			mainText.put("text", textInfo);
			
			blocks.add(mainText);
		}
		
		for (PaTdealSlackGoods transferInfo : slackTransferInfo) {
			fields = new ArrayList<Map<String, Object>>();
			insertItem = new HashMap<String, Object>();
			
			insertItem.put("type", "section");
			for (int i = 0; i < 2; i++) { // SK, TDEAL 상품코드 셋팅
				insertItemSub = new HashMap<String, Object>();
				insertItemSub.put("type", "mrkdwn");
				insertItemSub.put("text", i == 0 ?  "*SK스토아 상품코드* \n" + transferInfo.getGoodsCode() : "*TDeal상품코드* \n" + transferInfo.getMallProductNo());
				fields.add(insertItemSub);
			}
			insertItem.put("fields", fields);
			
			blocks.add(insertItem);
			
			fields = new ArrayList<Map<String, Object>>();
			insertItem = new HashMap<String, Object>();
			for (int i = 0; i < 2; i++) { // 변경가격 셋팅
				if (transferInfo.getAsisTransDate() == null) continue;
				insertItemSub = new HashMap<String, Object>();
				insertItemSub.put("type", "mrkdwn");
				insertItemSub.put("text", i == 0 ?  "*변경 전 가격* \n" + transferInfo.getAsisBestPrice() : "*변경 후 가격* \n" + transferInfo.getTobeBestPrice());
				fields.add(insertItemSub);
			}
			if (fields.size() != 0) {
				insertItem.put("type", "section");
				insertItem.put("fields", fields);
				blocks.add(insertItem);
			}
			
			fields = new ArrayList<Map<String, Object>>();
			insertItem = new HashMap<String, Object>();
			for (int i = 0; i < 2; i++) { // 상품명 셋팅
				if (transferInfo.getAsisGoodsName() == null) continue;
				insertItemSub = new HashMap<String, Object>();
				insertItemSub.put("type", "mrkdwn");
				insertItemSub.put("text", i == 0 ?  "*변경 전 상품명* \n" + transferInfo.getAsisGoodsName() : "*변경 후 상품명* \n" + transferInfo.getTobeGoodsName());
				fields.add(insertItemSub);
			}
			if (fields.size() != 0) {
				insertItem.put("type", "section");
				insertItem.put("fields", fields);
				blocks.add(insertItem);
			}
			
			fields = new ArrayList<Map<String, Object>>();
			insertItem = new HashMap<String, Object>();
			for (int i = 0; i < 2; i++) { // 표준카테고리 셋팅
				if (transferInfo.getAsisPaLmsdKey() == null) continue;
				insertItemSub = new HashMap<String, Object>();
				insertItemSub.put("type", "mrkdwn");
				insertItemSub.put("text", i == 0 ?  "*변경 전 표준카테고리* \n" + transferInfo.getAsisPaLmsdKey() : "*변경 후 표준카테고리* \n" + transferInfo.getTobePaLmsdKey());
				fields.add(insertItemSub);
			}
			if (fields.size() != 0) {
				insertItem.put("type", "section");
				insertItem.put("fields", fields);
				blocks.add(insertItem);
			}
			
			fields = new ArrayList<Map<String, Object>>();
			insertItem = new HashMap<String, Object>();
			for (int i = 0; i < 2; i++) { // 전시카테고리 셋팅
				if (transferInfo.getAsisDispCatId() == null) continue;
				insertItemSub = new HashMap<String, Object>();
				insertItemSub.put("type", "mrkdwn");
				insertItemSub.put("text", i == 0 ?  "*변경 전 전시카테고리* \n" + transferInfo.getAsisDispCatId() : "*변경 후 전시카테고리* \n" + transferInfo.getTobeDispCatId());
				fields.add(insertItemSub);
			}
			if (fields.size() != 0) {
				insertItem.put("type", "section");
				insertItem.put("fields", fields);
				blocks.add(insertItem);
			}
			
			fields = new ArrayList<Map<String, Object>>();
			insertItem = new HashMap<String, Object>();
			for (int i = 0; i < 2; i++) { // 즉시할인가 셋팅
				if (transferInfo.getAsisCouponDcAmt() == null) continue;
				insertItemSub = new HashMap<String, Object>();
				insertItemSub.put("type", "mrkdwn");
				insertItemSub.put("text", i == 0 ?  "*변경 전 즉시할인가* \n" + transferInfo.getAsisCouponDcAmt() : "*변경 후 즉시할인가* \n" + transferInfo.getTobeCouponDcAmt());
				fields.add(insertItemSub);
			}
			if (fields.size() != 0) {
				insertItem.put("type", "section");
				insertItem.put("fields", fields);
				blocks.add(insertItem);
			}
			
			fields = new ArrayList<Map<String, Object>>();
			insertItem = new HashMap<String, Object>();
			for (int i = 0; i < 2; i++) { // 판매중단 셋팅
				if (transferInfo.getStopPaSaleGb() == null) {
					continue;
				} else if ("30".equals(transferInfo.getStopPaSaleGb())) {
					insertItemSub = new HashMap<String, Object>();
					insertItemSub.put("type", "mrkdwn");
					insertItemSub.put("text", i == 0 ?  "*변경 전 판매상태* \n판매중" : "*변경 후 판매상태* \n판매중지");
					fields.add(insertItemSub);
				}
			}
			if (fields.size() != 0) {
				insertItem.put("type", "section");
				insertItem.put("fields", fields);
				blocks.add(insertItem);
			}
			
			fields = new ArrayList<Map<String, Object>>();
			insertItem = new HashMap<String, Object>();
			for (int i = 0; i < 2; i++) { // 판매재개 셋팅
				if (transferInfo.getStartPaSaleGb() == null) {
					continue;
				} else if ("20".equals(transferInfo.getStartPaSaleGb())) {
					insertItemSub = new HashMap<String, Object>();
					insertItemSub.put("type", "mrkdwn");
					insertItemSub.put("text", i == 0 ?  "*변경 전 판매상태* \n판매중지" : "*변경 후 판매상태* \n판매중");
					fields.add(insertItemSub);
				}
			}
			if (fields.size() != 0) {
				insertItem.put("type", "section");
				insertItem.put("fields", fields);
				blocks.add(insertItem);
			}
//			
//			판매수수료도 보내야 된다면 쿼리에도 추가해줘야됨
//			fields = new ArrayList<Map<String, Object>>();
//			insertItem = new HashMap<String, Object>();
//			for (int i = 0; i < 2; i++) { // 판매수수료 셋팅
//				Integer ss = transferInfo.getAsisMarginRate();
//				if (ss == null) continue;
//				insertItemSub = new HashMap<String, Object>();
//				insertItemSub.put("type", "mrkdwn");
//				insertItemSub.put("text", i == 0 ?  "*변경 전 판매수수료* \n" + transferInfo.getAsisMarginRate() : "*변경 후 판매수수료* \n" + transferInfo.getTobeMarginRate());
//				fields.add(insertItemSub);
//			}
//			if (fields.size() != 0) {
//				insertItem.put("type", "section");
//				insertItem.put("fields", fields);
//				blocks.add(insertItem);
//			}
			division.put("type", "divider"); // 상품별 구분선
			blocks.add(division);
		}
		
		slackGoodsMap.put("blocks", blocks);
		
		return slackGoodsMap;
	}
	
}
