package com.cware.api.pa11st.controller;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.ShipCostFlag;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.Pa11stGoodsVO;
import com.cware.netshopping.domain.Pa11stGoodsdtMappingVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pa11st.goods.service.Pa11stGoodsService;
import com.cware.netshopping.pa11st.v2.repository.Pa11stCnShipCostMapper;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pa11st/goods", description="상품")
@Controller("com.cware.api.pa11st.Pa11stGoodsController")
@RequestMapping(value="/pa11st/goods")
public class Pa11stGoodsController extends AbstractController {
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pa11st.goods.pa11stGoodsService")
	private Pa11stGoodsService pa11stGoodsService;
	
	@Resource(name = "com.cware.api.pa11st.Pa11stCommonController")
	private Pa11stCommonController pa11stCommonController;
	
	@Resource(name = "com.cware.api.pa11st.Pa11stAsycController")
	private Pa11stAsycController pa11stAsyncController;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;

	@Autowired
	Pa11stCnShipCostMapper shipCostMapper;
	
	@ApiOperation(value = "상품등록 API", notes = "상품등록 API", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/legacy-goods-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsInsert(HttpServletRequest request,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 			@RequestParam(value="paCode", required=true) String paCode,			
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "")			@RequestParam(value="goodsCode", required=true) String goodsCode,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 				@RequestParam(value="procId"		, required=false, defaultValue="PA11") String procId,
			@ApiParam(name = "searchTermGb", value = "중복실행체크여부", defaultValue = "")  @RequestParam(value="searchTermGb"	, required=false, defaultValue = "") String searchTermGb,
			@ApiParam(name = "massTargetYn", value = "massTargetYn", defaultValue = "") @RequestParam(value="massTargetYn"	, required=false, defaultValue = "") String massTargetYn
			) throws Exception{
		ParamMap paramMap 				= new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String dateTime 				= "";
		String duplicateCheck 			= "";
		Pa11stGoodsVO pa11stGoods 		= null;
		String prg_id 					= "IF_PA11STAPI_01_001";
		
		if(!"1".equals(massTargetYn))  massTargetYn = "0";
		
		log.info("===== 상품등록 API Start=====");
		//log.info("01.API 기본정보 세팅");
		//= connectionSetting 설정
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode"		, prg_id);
		paramMap.put("broadCode"	, Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode"	, Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate"	, dateTime);
		paramMap.put("modCase"		, "INSERT");
		paramMap.put("goodsCode"	, goodsCode);
		paramMap.put("paCode"		, paCode);
		paramMap.put("massTargetYn"	, massTargetYn);
		
		try{
			if(goodsCode != null && !goodsCode.equals("")) {
				log.info("상품코드 : " + goodsCode);
				log.info("제휴사코드: "+ paCode);
				log.info("처리자ID : "+ procId.toUpperCase());
			}
						
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			}
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			//log.info("03.SK스토아 TV쇼핑 상품정보 Select");
			paramMap.put("modifyId"	, procId);
			paramMap.put("url"		, ConfigUtil.getString("PA11ST_COM_BASE_URL") + apiInfo.get("API_URL"));
			paramMap.put("header"	, "Content-Type=" + apiInfo.get("contentType"));
 			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode"	, paCode);
			
			goodsInsert11st(request, apiInfo , paramMap, searchTermGb);
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			//if (conn != null) conn.disconnect();
			try{
				if (paramMap.get("code").equals("490")) {
					log.error("중복 실행 중입니다.");
				} else if(!paramMap.get("code").equals("200")){
					paramMap.put("message", paramMap.getString("message").length() > 999 ? paramMap.getString("message").substring(0, 999) : paramMap.getString("message"));
					
					pa11stGoods = new Pa11stGoodsVO();
					pa11stGoods.setGoodsCode(goodsCode);
					pa11stGoods.setPaCode(paCode);
					
					if ( paramMap.getString("message").replace(" ","").indexOf("goodsStockList") >= 0 ) {
						//이미 입점 되었고 재고 처리 하는 부분에서 에러난 케이스.
						//상품 수정을 통해 처리하면 된다.
						pa11stGoods.setPaStatus("30");
						pa11stGoods.setTransTargetYn("1");
					} else {
						pa11stGoods.setPaStatus("20");//입점반려
					}
					
					if ( paramMap.get("code").equals("440") ) {
						pa11stGoods.setReturnNote("주소정제에 실패했습니다.");
					}
					
					pa11stGoods.setReturnNote(paramMap.getString("message"));
					pa11stGoods.setModifyId(procId);
					
					//log.info("07.제휴사 실패정보 저장");
					pa11stGoodsService.savePa11stGoodsFailTx(pa11stGoods);
				}
				
				paramMap.put("resultMessage", paramMap.getString("message"));
				log.info(paramMap.get().toString());
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(!searchTermGb.equals("1")){
				if(duplicateCheck.equals("0")){
					systemService.checkCloseHistoryTx("end", prg_id);
				}	
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	public void goodsInsert11st(HttpServletRequest request, HashMap<String, String> apiInfo, ParamMap paramMap, String searchTermGb) throws Exception {
		 
		PaEntpSlip returnSlip 			 		= null;
		PaEntpSlip entpSlip 					= null;
		ResponseEntity<?> responseMsg 			= null;
		Pa11stGoodsVO pa11stGoods 		 		= null;
		List<PaPromoTarget> paPromoTargetList 	= null;
		List<PaGoodsdtMapping> pa11stGoodsdt 	= null;
		PaGoodsTransLog paGoodsTransLog 		= null;
		List<PaGoodsOffer> pa11sstGoodsOffer 	= null;
		StringBuilder reqXml 					= null;
		OutputStreamWriter out 					= null;
		String checkSaleGb 						= "";
		HashMap<String, String> policyMap 		= new HashMap<String, String>(); //발송 예정일 템플릿 용
		HashMap<String, String> paNotice 		= new HashMap<String, String>();
		String request_type 					= "POST";
		Document doc 							= null;
		NodeList descNodes 						= null;
		String responseTime 					= null;
		int respCode 							= 0;
		String respMsg 							= null;
		String rtnMsg 							= Constants.SAVE_SUCCESS;
		double timeS 							= 0;
		double timeE 							= 0;
		HttpURLConnection conn					= null;
		
		String goodsCode 	= paramMap.getString("goodsCode");
		String prg_id		= paramMap.getString("apiCode");
		String paCode	 	= paramMap.getString("paCode");
		String dateTime  	= paramMap.getString("startDate");
		String procId	 	= paramMap.getString("modifyId");		
		
		try {
			
					
			//상품 출고지, 회수지 담당자 주소 체크
			entpSlip = pa11stGoodsService.selectPa11stEntpSlip(paramMap);
			if(entpSlip != null){
				//출고지 등록
				responseMsg= pa11stCommonController.entpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaCode(), searchTermGb);
				//log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
					paramMap.put("code","440");
					paramMap.put("message",getMessage("pa.check_entp_slip_man_seq", new String[] {" 업체코드 : " +  entpSlip.getEntpCode()}));
					return;
				}
			}
			
			
			returnSlip = pa11stGoodsService.selectPa11stReturnSlip(paramMap);
			if(returnSlip != null){
				//반품/회수지 등록
				responseMsg= pa11stCommonController.returnSlipInsert(request, returnSlip.getEntpCode(), returnSlip.getEntpManSeq(), returnSlip.getPaCode() , searchTermGb);
				//log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
					paramMap.put("code","440");
					paramMap.put("message",getMessage("pa.check_return_slip_man_seq", new String[] {" 업체코드 : " +  returnSlip.getEntpCode()}));
					return;
				}
			}
			
			//출고지별 배송비 추가/수정 (2018.10.15 ,thjeon)
			pa11stCommonController.entpSlipCostInsert(request, null );
	        
			//상품정보 조회
			pa11stGoods = pa11stGoodsService.selectPa11stGoodsInfo(paramMap);
			if(pa11stGoods == null){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return;
			}
			if(pa11stGoods.getProductNo()!=null){
				paramMap.put("code","410");
				paramMap.put("message",getMessage("pa.already_insert_goods", new String[] {"11번가 상품코드 : " + pa11stGoods.getProductNo()}));
				return;
			}
			
			String goodsCom = (pa11stGoods.getGoodsComLen() > 0) ? ("<div style=\"line-height: 2.0em; font-family: 'NanumBarunGothic'; font-size: 19px;\"><div><h4>&middot;&nbsp;상품구성<h4><pre>" +  pa11stGoods.getGoodsCom() + "</pre></div></div>") : "";
			
			if("".equals(pa11stGoods.getCollectImage()) || pa11stGoods.getCollectImage() == null) {
					pa11stGoods.setDescribeExt("<div align='center'><img alt='' src='" + pa11stGoods.getTopImage() + "' /><br /><br /><br />" + goodsCom							//상단 이미지 + 상품구성
				  +	pa11stGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + pa11stGoods.getBottomImage() + "' /></div>");	//히딘 이미지
			}else {
					pa11stGoods.setDescribeExt("<div align='center'><img alt='' src='" + pa11stGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + pa11stGoods.getCollectImage() + "' /><br /><br /><br />" +	goodsCom	//상단이미지 + 착불이미지 + 상품구성 
			      + pa11stGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + pa11stGoods.getBottomImage() + "' /></div>");	//하단 이미지
			}
			
			paNotice = pa11stGoodsService.selectPa11stGoodsNoticeYn(paramMap);
			//상품공지관리
			if(paNotice != null) {
			    pa11stGoods.setDescribeExt(paNotice.get("CONTENT").replaceAll("\n", "<br />") + pa11stGoods.getDescribeExt());
			}
			
			int shipCostCnt = 0;
			shipCostCnt = pa11stGoodsService.selectPa11stGoodsShipCnt(goodsCode);
			
			if(shipCostCnt < 1){
				paramMap.put("code","450");
				paramMap.put("message",getMessage("pa.error_ship_cost", new String[] {"상품코드 : " + goodsCode}));
				return;
			}
			//단품조회
			pa11stGoodsdt = pa11stGoodsService.selectPa11stGoodsdtInfoList(paramMap);
			
			paramMap.put("paGroupCode", pa11stGoods.getPaGroupCode());
			pa11sstGoodsOffer = pa11stGoodsService.selectPa11stGoodsOfferList(paramMap);
			if(pa11sstGoodsOffer.size() < 1){
				paramMap.put("code","430");
				paramMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
				return;
			}
			
			//상품 발송 예정일 템플릿 조회
			policyMap = pa11stGoodsService.selectGoodsFor11stPolicy(pa11stGoods);
			if(policyMap == null){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list", new String[] {"상품 발송 예정일 템플릿 API 상품코드 : " + goodsCode}));
				return;
			}
			
			//즉시할인쿠폰 프로모션 개발
			paPromoTargetList = paPromoTargetInfoSetting(paramMap);
			
			log.info("03.상품등록 API 호출");//TODO
			if(paCode.equals(Constants.PA_11ST_BROAD_CODE)){
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"");
			} else {
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"");
			}

			reqXml = pa11stGoodsInfoSetting(pa11stGoods, pa11stGoodsdt, pa11sstGoodsOffer, paPromoTargetList);
			//log.info("11번가로 전달한 전문[등록]"); /* 향후 삭제 필요 */
		    if(reqXml != null){
//		    	log.info(String.valueOf(reqXml));
		    	paramMap.put("body", String.valueOf(reqXml));
		    }

			if(reqXml != null){
				//log.info("04.11번가 API 호출");
				timeS = System.nanoTime();
				out = new OutputStreamWriter(conn.getOutputStream());
				out.write(String.valueOf(reqXml));
				out.flush();

				respCode = conn.getResponseCode();
				respMsg  = conn.getResponseMessage();
				//log.info(" connect respCode : "+respCode);
				//log.info(" connect respMsg  : "+respMsg);
				
				timeE = System.nanoTime();
				responseTime = Double.toString((timeE-timeS)/1000000000);
				paramMap.put("responseTime", responseTime);
				paramMap.put("result", respMsg);
				
				pa11stAsyncController.insertPaRequestMap(paramMap);
				
				if(respCode == 200){
					// RESPONSE XML 		
					doc = ComUtil.parseXML(conn.getInputStream());
					descNodes = doc.getElementsByTagName("ClientMessage");
					conn.disconnect();

					for(int j=0; j<descNodes.getLength();j++){
				        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
				        	paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
				        }
				    }
					
					log.info("result message : "+paramMap.getString("message"));

					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paramMap.getString("goodsCode"));
					paGoodsTransLog.setPaCode(paramMap.getString("paCode"));
					paGoodsTransLog.setItemNo(paramMap.getString("productNo").isEmpty()?paramMap.getString("goodsCode"):paramMap.getString("productNo"));
					paGoodsTransLog.setRtnCode(paramMap.getString("resultCode"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn(paramMap.getString("resultCode").equals("200")==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId("PA11");
					pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
					
					if(paramMap.getString("resultCode").equals("200")){
					//11번가 단일상품 개발중지
					//if(paramMap.getString("resultCode").equals("200") || paramMap.getString("resultCode").equals("210")){
						
						pa11stGoods.setPaStatus("30");//입점완료
						pa11stGoods.setProductNo(paramMap.getString("productNo"));
						pa11stGoods.setReturnNote("");
						pa11stGoods.setInsertId("PA11");
						pa11stGoods.setModifyId("PA11");
						pa11stGoods.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						pa11stGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						pa11stGoods.setLastModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
						log.info("05.제휴사 상품정보 저장");
						rtnMsg = pa11stGoodsService.savePa11stGoodsTx(pa11stGoods, pa11stGoodsdt, paPromoTargetList, prg_id);
						
						if(!rtnMsg.equals("000000")){
							paramMap.put("code","404");
							paramMap.put("message",rtnMsg);
							return;
						} else {
							paramMap.put("code","200");
							paramMap.put("message","OK");
						}
						
						checkSaleGb = pa11stGoodsService.checkPa11stCheckSaleGb(pa11stGoods.getGoodsCode());
						
						if(checkSaleGb.equals("11")){
							responseMsg= goodsSellStop(request, pa11stGoods.getGoodsCode(), pa11stGoods.getPaCode(), "PA11");
							//log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
							if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
								paGoodsTransLog.setRtnCode(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
								paGoodsTransLog.setRtnMsg(getMessage("pa.fail_stop_goods_sale", new String[] {" 상품코드 : " + goodsCode}));
								paGoodsTransLog.setSuccessYn(paramMap.getString("resultCode").equals("200")==true?"1":"0");
								paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paGoodsTransLog.setProcId("PA11");
								pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
							}
						}
						
						responseMsg = null;
						log.info("06.재고번호 조회 API호출 : /goods-stock-list");
						responseMsg = goodsStockList(request, pa11stGoods.getGoodsCode(), pa11stGoods.getProductNo(), pa11stGoods.getPaCode(), procId, "1");
						if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
							paramMap.put("code","500");
							paramMap.put("message",getMessage("errors.api.system")+" : goodsStockList");
							log.error("11번가 상품등록 재고번호 호출 에러");
							return;
						}
						
					} else {
						paramMap.put("code",paramMap.getString("resultCode"));
					}
				} else {
					paramMap.put("code"		,	respCode);
					paramMap.put("message"	,	respMsg);
				}
			}
			
		}catch (Exception e) {
			if (conn != null) conn.disconnect();
			throw new Exception(e);
		}
		
	}
	
	@ApiOperation(value = "상품수정 API", notes = "상품수정 API", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})	
	@RequestMapping(value = "/legacy-goods-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsModify(HttpServletRequest request,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 			@RequestParam(value="paCode", required=false) String paCode,			
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "")			@RequestParam(value="goodsCode", required=false) String goodsCode,
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 				@RequestParam(value="procId"		, required=false, defaultValue="PA11") String procId,
		@ApiParam(name = "searchTermGb", value = "중복실행체크여부", defaultValue = "")  @RequestParam(value="searchTermGb"	, required=false, defaultValue = "") String searchTermGb) 
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String dateTime = "";
		String duplicateCheck = "";
		//StringBuilder reqXml = null;
		List<Pa11stGoodsVO> pa11stGoodsList = null;
		Pa11stGoodsVO pa11stGoods = null;
		List<PaEntpSlip> entpSlipList = null;
		List<PaEntpSlip> returnSlipList = null;
		PaEntpSlip entpSlip = null;
		PaEntpSlip returnSlip = null;
		ResponseEntity<?> responseMsg = null;
		
		log.info("===== 상품수정 API Start=====");
		//log.info("01.API 기본정보 세팅");
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_01_002";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		paramMap.put("modCase", "MODIFY");
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paCode", paCode);
		
		try{
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);			
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});				
			}
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			log.info("03.출고지/회수지 정보 check(update/insert)");
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("url", ConfigUtil.getString("PA11ST_COM_BASE_URL") + apiInfo.get("API_URL"));
			paramMap.put("header", "Content-Type=" + apiInfo.get("contentType"));
			
			//상품 출고지, 회수지 담당자 주소 체크
			entpSlipList = pa11stGoodsService.selectPa11stEntpSlipList(paramMap);
			if(entpSlipList.size() > 0){
				//출고지 등록
				for(int i=0; i<entpSlipList.size(); i++){
					entpSlip = entpSlipList.get(i);
					responseMsg= pa11stCommonController.entpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaCode() , searchTermGb);
					//log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
					if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
						paramMap.put("code","440");
//						paramMap.put("message",getMessage("pa.check_entp_slip_man_seq", new String[] {" 업체코드 : " + entpSlip.getEntpCode()}));
						//log.info("/entp-slip-insert");
						//log.info("code : "+paramMap.get("code"));
						log.info("/entp-slip-insert Message : "+paramMap.get("message"));
					}
				}
			}
			
			returnSlipList = pa11stGoodsService.selectPa11stReturnSlipList(paramMap);
			if(returnSlipList.size() > 0){
				//반품/회수지 등록
				for(int i=0; i<returnSlipList.size(); i++){
					returnSlip = returnSlipList.get(i);
					responseMsg= pa11stCommonController.returnSlipInsert(request, returnSlip.getEntpCode(), returnSlip.getEntpManSeq(), returnSlip.getPaCode() , searchTermGb);
					//log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
					if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
						paramMap.put("code","440");
//						paramMap.put("message",getMessage("pa.check_return_slip_man_seq", new String[] {" 업체코드 : " + returnSlip.getEntpCode()}));
						//log.info("/return-slip-insert");
						//log.info("code : "+paramMap.get("code"));
						log.info("/return-slip-insert Message : "+paramMap.get("message"));
					}
				}
			}
			
			//출고지 주소 UPDATE
			responseMsg= pa11stCommonController.entpSlipUpdate(request);
			//log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
				paramMap.put("code","440");
//				paramMap.put("message",getMessage("pa.check_entp_slip_man_seq", new String[] {" 업체코드 : " + entpSlip.getEntpCode()}));
				log.info("/entp-ship-update");
				//log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			//회수지 주소 UPDATE
			responseMsg= pa11stCommonController.returnShipUpdate(request);
			//log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
				paramMap.put("code","440");
//				paramMap.put("message",getMessage("pa.check_return_slip_man_seq", new String[] {" 업체코드 : " + returnSlip.getEntpCode()}));
				//log.info("/return-ship-update");
				//log.info("code : "+paramMap.get("code"));
				log.info("/return-ship-update Message : "+paramMap.get("message"));
			}
			
        	//출고지별 배송비 추가/수정 (2018.09.07 ,thjeon 10월초 오픈 예정 )
			pa11stCommonController.entpSlipCostInsert(request, null);
		
			//판매 중지해제 처리 대상 조회 
			//log.info("수정API-판매중지처리해제대상 조회");
			pa11stGoodsList = pa11stGoodsService.selectPa11stGoodsSaleRestartList(paramMap);
			for(int j=0; j<pa11stGoodsList.size(); j++){
				log.info("판매중지해제 /goods-sell-restart");
				pa11stGoods = pa11stGoodsList.get(j);				
				
				boolean isRetentionGoodsYn= checkRetention(pa11stGoods.getGoodsCode() ,pa11stGoods.getProductNo(), pa11stGoods.getPaCode());
				if(isRetentionGoodsYn) continue;				
				
				responseMsg = goodsSellRestart(request, pa11stGoods.getGoodsCode(), pa11stGoods.getPaCode(), procId);
				//log.info("code : "+PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				log.info("message : "+PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				//log.info("/goods-sell-restart");
				//log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			pa11stGoods = null;
			pa11stGoodsList = null;
			
			//상품 수정 중 동기화배치가 돌아 상품정보 수정될 경우 다시 수정대상에 포함 하기위해 MODIFY_DATE와  수정대상조회 시작시간을 비교함
			String goodsListTime = systemService.getSysdatetimeToString(); 
			paramMap.put("goodsListTime", goodsListTime);
			
			log.info("04.변경된 상품정보 select");
			pa11stGoodsList = pa11stGoodsService.selectPa11stGoodsInfoList(paramMap);
			
			if(pa11stGoodsList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list", new String[] {" 상품코드 : " + goodsCode}));
				paramMap.put("resultMessage",paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}
			
			//11번가 상품 수정 및 통신
			int modifyFailCnt = 0;
			for(Pa11stGoodsVO asyncPa11stGoods : pa11stGoodsList){
				try {
					//HS BAEK  - 리텐션이 판매중지 -> 판매중인 케이스에서 발생되기 때문에 밑의 소스는 주석처리 해둠, 추후에 필요하면 OPEN 
					boolean isRetentionGoodsYn= checkRetention(asyncPa11stGoods.getGoodsCode() ,asyncPa11stGoods.getProductNo(), asyncPa11stGoods.getPaCode());
					if(isRetentionGoodsYn) continue;
					
					goodsModify11st(request,   asyncPa11stGoods, paramMap , apiInfo);
				}catch (Exception e) {
					modifyFailCnt ++;
					continue;
				}
			}//end of For
			
			log.info("/goods-modify Message : "+paramMap.get("message"));
			
			//단품수정api
			//2021-11-25 부 별도 배치로 분리함
			//BO에서 호출하거나 URL로 상품코드 직접 입력하여 호출하는 경우에만 타도록 수정
			//2022.04.29 단품수정API 위치 변경 LEEJY
			if ( searchTermGb.equals("1") && !"".equals(paramMap.get("goodsCode")) ) {
				
				responseMsg= goodsdtModify(request, paramMap.get("goodsCode").toString(), paramMap.get("paCode").toString(), procId, searchTermGb); 
				if ( !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") ) {
					if( PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404") ) {
						paramMap.put("code","404");
						paramMap.put("message", PropertyUtils.describe(responseMsg.getBody()).get("message"));
					} else {
						paramMap.put("code","440");
						paramMap.put("message",getMessage("pa.fail_update_goodsdt", new String[] {" 상품코드 : " + goodsCode}));
					}
					paramMap.put("resultMessage",paramMap.getString("message"));
					systemService.insertApiTrackingTx(request,paramMap);
				}					
			}
			
			//상품가격 수정
			goodspriceCouponModify(request,goodsCode,paCode,procId, "0", null);
			
			paramMap.put("goodsCode", goodsCode);
			//판매 중지 처리 대상 조회
			 pa11stGoodsList = pa11stGoodsService.selectPa11stGoodsSaleStopList(paramMap);
			
			for(int j=0; j<pa11stGoodsList.size(); j++){
			  log.info("판매중지처리 goodsSellStop"); pa11stGoods = pa11stGoodsList.get(j);
			  responseMsg = goodsSellStop(request, pa11stGoods.getGoodsCode(), pa11stGoods.getPaCode(), procId);
			  paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			  paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
			  log.info("/goods-sell-stop Message : "+paramMap.get("message")); 
			}
			
			if(modifyFailCnt > 0) {			
				throw processException("msg.errors.detail", new String[] {"11번가 상품수정 실패 (기타이유) - " + modifyFailCnt});
			}			
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else if(paramMap.getString("message").indexOf("쇼킹딜 참여 신청 상품은") < 0 || paramMap.getString("message").indexOf("OpenAPI 상품") < 0
					|| paramMap.getString("message").indexOf("이미지") < 0 || paramMap.getString("message").indexOf("connection") < 0
					|| paramMap.getString("message").indexOf("goodsStockList") < 0 ){
				paramMap.put("message", e.getMessage());
				pa11stAsyncController.savePaGoodsModifyFail(paramMap);
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				paramMap.put("message", paramMap.getString("message").length() > 999 ? paramMap.getString("message").substring(0, 999) : paramMap.getString("message"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(!searchTermGb.equals("1")){
				if(duplicateCheck.equals("0")){
					systemService.checkCloseHistoryTx("end", prg_id);
				}				
			}
		}
		/*REQ_PRM_041 11번가 제휴OUT 딜 상품 옵션 수정 호출*/
		//딜 주석처리
		//alcoutDealGoodsdtModify(request, paramMap.getString("goodsCode"), paramMap.getString("paCode"), "PA11DEAL", "");
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	
	public void goodsModify11st(HttpServletRequest request,  Pa11stGoodsVO asyncPa11stGoods, ParamMap paramMap , HashMap<String, String> apiInfo ) throws Exception{
			
		String goodsListTime = paramMap.getString("goodsListTime");
		String procId		 = paramMap.getString("modifyId");
		String prg_id 		 = paramMap.getString("apiCode");
		String goodsCode	 = paramMap.getString("goodsCode");
		
		HashMap<String, String> policyMap 		= new HashMap<String, String>();
		HashMap<String, String> paNotice  		= new HashMap<String, String>();
		List<PaGoodsOffer> pa11sstGoodsOffer 	= null;

		
		ParamMap asyncMap = new ParamMap();
		asyncPa11stGoods.setLastModifyDate(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));
		
		List<PaGoodsdtMapping> pa11stGoodsdt 	= null;
		List<PaPromoTarget> paPromoTargetList 	= null;
		StringBuilder reqXml 					= null;
		
		asyncMap.put("paCode"		,	asyncPa11stGoods.getPaCode());
		asyncMap.put("goodsCode"	,	asyncPa11stGoods.getGoodsCode());
		asyncMap.put("siteGb"		,	Constants.PA_INTP_PROC_ID);
		asyncMap.put("apiCode"		,	prg_id);
		asyncMap.put("url"			, 	ConfigUtil.getString("PA11ST_COM_BASE_URL") + apiInfo.get("API_URL"));
		asyncMap.put("header"		, 	"Content-Type=" + apiInfo.get("contentType"));
		
		if(asyncPa11stGoods.getProductNo()==null){
			paramMap.put("code","411");
			paramMap.put("message",getMessage("pa.not_exists_goods", new String[] {"상품코드 : " + goodsCode}));
			log.error(paramMap.getString("message"));
			return;
		}
		
		//딜 상품 관리 대상 update
		int excuteCnt = 0;
		excuteCnt = pa11stGoodsService.selectOutDealGoodsCheck(asyncPa11stGoods);
		if(excuteCnt > 0) {
			asyncPa11stGoods.setTransTargetYn("1");
			pa11stGoodsService.updateOutDealGoodsTarget(asyncPa11stGoods);
		}
		
		paramMap.put("goodsCode", asyncPa11stGoods.getGoodsCode());
		paramMap.put("paCode", asyncPa11stGoods.getPaCode());
		paramMap.put("productNo", asyncPa11stGoods.getProductNo());
		
		String goodsCom = (asyncPa11stGoods.getGoodsComLen() > 0) ? ("<div style=\"line-height: 2.0em; font-family: 'NanumBarunGothic'; font-size: 19px;\"><div><h4>&middot;&nbsp;상품구성<h4><pre>" +  asyncPa11stGoods.getGoodsCom() + "</pre></div></div>") : "";
		
		if("".equals(asyncPa11stGoods.getCollectImage()) || asyncPa11stGoods.getCollectImage() == null) {
				asyncPa11stGoods.setDescribeExt("<div align='center'>" + "<img alt='' src='" + asyncPa11stGoods.getTopImage() + "' /><br /><br /><br />" + goodsCom
			  + asyncPa11stGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + asyncPa11stGoods.getBottomImage() + "' /></div>");				
		} else {
				asyncPa11stGoods.setDescribeExt("<div align='center'>" + "<img alt='' src='" + asyncPa11stGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + asyncPa11stGoods.getCollectImage() + "' /><br /><br /><br />" + 
				goodsCom + asyncPa11stGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + asyncPa11stGoods.getBottomImage() + "' /></div>");
		}
		
		if(asyncPa11stGoods.getNoticeExtLen() > 0) asyncPa11stGoods.setDescribeExt(asyncPa11stGoods.getNoticeExt().replaceAll("\n", "<br />") + asyncPa11stGoods.getDescribeExt());
		
//		if(asyncPa11stGoods.getDescribeLen() + asyncPa11stGoods.getNoticeExtLen() + asyncPa11stGoods.getGoodsComLen() > 6000){
//			paramMap.put("code","415");
//			paramMap.put("message","상품 기술서 길이가 6000자가 넘습니다. 상품코드 : " + goodsCode);
//			return;
//		}
		
//		paNotice = pa11stGoodsService.selectPa11stGoodsNoticeYn(paramMap);
//		//상품공지관리
//		if(paNotice != null) {
//			asyncPa11stGoods.setDescribeExt(paNotice.get("CONTENT").replaceAll("\n", "<br />") + asyncPa11stGoods.getDescribeExt());
//		}
		
//		if(asyncPa11stGoods.getDescribeLen() > 6000){
//			paramMap.put("code","415");
//			paramMap.put("message","상품 기술서 길이가 6000자가 넘습니다. 상품코드 : " + goodsCode);
//			log.info(paramMap.getString("message"));
//			return;
//		}
//		
//		if(asyncPa11stGoods.getDescribeExt().equals("")){
//			paramMap.put("code","420");
//			paramMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + asyncPa11stGoods.getGoodsCode()}));
//			//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
//			log.info(paramMap.getString("message"));
//			return;
//		}

		int shipCostCnt = 0;						
		shipCostCnt = pa11stGoodsService.selectPa11stGoodsShipCnt(asyncPa11stGoods.getGoodsCode());
		
		if(shipCostCnt < 1){
			paramMap.put("code","450");
			paramMap.put("message",getMessage("pa.error_ship_cost", new String[] {"상품코드 : " + asyncPa11stGoods.getGoodsCode()}));
			asyncPa11stGoods.setPaSaleGb("30");//판매중단
			asyncPa11stGoods.setReturnNote(paramMap.getString("message"));
			asyncPa11stGoods.setModifyId(procId);
			
			log.error("배송비 확인"+asyncPa11stGoods.getGoodsCode());
			//log.info("07.제휴사 실패정보 저장");
			//pa11stGoodsService.saveShipCostPaGoodsTx(pa11stGoods);
			return;
		}
		
		//상품 발송 예정일 템플릿 조회
		policyMap = pa11stGoodsService.selectGoodsFor11stPolicy(asyncPa11stGoods);
		if(policyMap == null){
			paramMap.put("code","404");
			paramMap.put("message",getMessage("pa.not_exists_process_list", new String[] {"상품 발송 예정일 템플릿 API 상품코드 : " + goodsCode}));
			log.info(paramMap.getString("message"));
			return;
		}
		
		//즉시할인쿠폰 프로모션 개발
		paPromoTargetList = paPromoTargetInfoSetting(paramMap);
		
		paramMap.put("paGroupCode", asyncPa11stGoods.getPaGroupCode());
		paramMap.put("goodsCode", asyncPa11stGoods.getGoodsCode());
		paramMap.put("paCode", asyncPa11stGoods.getPaCode());
		pa11stGoodsdt = pa11stGoodsService.selectPa11stGoodsdtInfoList(paramMap);
		pa11sstGoodsOffer = pa11stGoodsService.selectPa11stGoodsOfferList(paramMap);
							
		reqXml = pa11stGoodsInfoSetting(asyncPa11stGoods, pa11stGoodsdt, pa11sstGoodsOffer, paPromoTargetList);
		//log.info("11번가로 전달한 전문[수정]"); /* 향후 삭제 필요 */
	    if(reqXml != null){
	    	//log.info(String.valueOf(reqXml));
	    	asyncMap.put("body", String.valueOf(reqXml));
	    }

		if(reqXml != null){
			pa11stAsyncController.asyncGoodsModify(request, asyncMap, apiInfo, asyncPa11stGoods, pa11stGoodsdt, paPromoTargetList, reqXml, prg_id);
			Thread.sleep(50);
		}
		
	}
	
	

	private List<PaPromoTarget> paPromoTargetInfoSetting(ParamMap paramMap) throws Exception{
		List<HashMap<String,Object>> requestPriceMapList = pa11stGoodsService.selectPaPromoTarget(paramMap);
		if(requestPriceMapList.size()==0){
			requestPriceMapList = new ArrayList<>();
			
			HashMap<String,Object> requestPriceMap = new HashMap<>();
			requestPriceMap.put("PROMO_NO","000000000000");
			requestPriceMap.put("SEQ","0000");
			requestPriceMap.put("DO_COST",0);
			requestPriceMap.put("DO_OWN_COST",0);
			requestPriceMap.put("DO_ENTP_COST",0);
			requestPriceMap.put("PROC_GB",'D');
			requestPriceMap.put("TRANS_DATE","");
			
			requestPriceMapList.add(requestPriceMap);
		}
		
		PaPromoTarget paPromoTarget;
    	List<PaPromoTarget> paPromoTargetList = new ArrayList<>();
    	for(HashMap<String,Object> priceMap : requestPriceMapList){
    		paPromoTarget = new PaPromoTarget();
    		paPromoTarget.setGoodsCode(paramMap.getString("goodsCode"));
    		paPromoTarget.setPaCode(paramMap.getString("paCode"));
    		paPromoTarget.setPromoNo(priceMap.get("PROMO_NO").toString());
    		paPromoTarget.setSeq(priceMap.get("SEQ").toString());
    		paPromoTarget.setProcGb(priceMap.get("PROC_GB").toString());
    		paPromoTarget.setDoCost(Double.parseDouble(priceMap.get("DO_COST").toString()));
    		paPromoTarget.setDoOwnCost(Double.parseDouble(priceMap.get("DO_OWN_COST").toString()));
    		paPromoTarget.setDoEntpCost(Double.parseDouble(priceMap.get("DO_ENTP_COST").toString()));
    		
    		if( !priceMap.get("TRANS_DATE").toString().equals("") ){
    			paPromoTarget.setTransDate(DateUtil.toTimestamp(priceMap.get("TRANS_DATE").toString(), "yyyy-MM-dd HH:mm:ss"));	
    		}else{
    			paPromoTarget.setTransDate(null);	
    		}
    		
    		paPromoTargetList.add(paPromoTarget);
    	}
		
		return paPromoTargetList;
	}
	
	private StringBuilder pa11stGoodsInfoSetting(Pa11stGoodsVO pa11stGoods, List<PaGoodsdtMapping> pa11stGoodsdtList, List<PaGoodsOffer> pa11stGoodsOffer, List<PaPromoTarget> paPromoTarget) throws Exception{
		StringBuilder reqXml = new StringBuilder();
		//운영 FLEX_IMG_SERVER_URL : //1.255.85.245/
		//개발 FLEX_IMG_SERVER_URL : http://1.255.85.245/
		//config : PARTNER_API_IMAGE_PROTOCOL 운영 Y, 개발 N
		Config imageUrl = systemService.getConfig("FLEX_IMG_SERVER_URL");
		Config protocol = systemService.getConfig("PARTNER_API_IMAGE_PROTOCOL");
		String http = null;
		if(protocol.getVal().equals("Y")){
			http ="http:";
		}else{
			http ="";
		}
		
		reqXml.append("<Product>");
		
		reqXml.append("<selMthdCd>"+"01"+"</selMthdCd>"); //판매방식 01:고정가판매
		reqXml.append("<dispCtgrNo>"+pa11stGoods.getPaLmsdKey()+"</dispCtgrNo>"); //카테고리번호
		reqXml.append("<prdTypCd>"+"01"+"</prdTypCd>"); //서비스 상품 코드 01:일반배송상품
		//reqXml.append("<prdNm>"+pa11stGoods.getGoodsName()+"</prdNm>"); //상품명
		
		reqXml.append("<prdNm><![CDATA["+ComUtil.subStringBytes(pa11stGoods.getGoodsName(),99)+"]]></prdNm>"); //상품명(사이즈 변경 100byte 이하)
		reqXml.append("<brand><![CDATA["+pa11stGoods.getBrandName()+"]]></brand>"); //브랜드명
		
		/** 브랜드 신규 개발*/
		if (pa11stGoods.getBrandNo() != null && !pa11stGoods.getBrandNo().equals("")) {
			reqXml.append("<apiPrdAttrBrandCd>" + pa11stGoods.getBrandNo() + "</apiPrdAttrBrandCd>");
		}
		reqXml.append("<orgnTypCd>"+pa11stGoods.getOrgnTypCd()+"</orgnTypCd>"); //원산지코드
		reqXml.append("<orgnTypDtlsCd>"+pa11stGoods.getOrgnTypDtlsCd()+"</orgnTypDtlsCd>"); //원산지지역코드
		reqXml.append("<orgnNmVal>"+pa11stGoods.getOrgnNmVal()+"</orgnNmVal>"); //원산지명


		if(pa11stGoods.getTaxYn().equals("1")){//부가세,면세
			reqXml.append("<suplDtyfrPrdClfCd>"+"01"+"</suplDtyfrPrdClfCd>"); //01:과세
		} else {
			if(pa11stGoods.getTaxSmallYn().equals("1")){
				reqXml.append("<suplDtyfrPrdClfCd>"+"03"+"</suplDtyfrPrdClfCd>"); //03:영세
			} else {
				reqXml.append("<suplDtyfrPrdClfCd>"+"02"+"</suplDtyfrPrdClfCd>"); //02:면세
			}
		}
		
		 if("1".equals(pa11stGoods.getOrderCreateYn())){
			 reqXml.append("<prdStatCd>"+"10"+"</prdStatCd>");//상품상태(10:주문제작상품)				 
		 }else {
			 reqXml.append("<prdStatCd>"+"01"+"</prdStatCd>");//상품상태(01:새상품)
		 }
		
		reqXml.append(pa11stGoods.getAdultYn()=="1"?"<minorSelCnYn>"+"N"+"</minorSelCnYn>":"<minorSelCnYn>"+"Y"+"</minorSelCnYn>");//미성년자 구매가능
		reqXml.append("<prdImage01>"+http+imageUrl.getVal()+pa11stGoods.getImageUrl()+pa11stGoods.getImageP()+"?dc="+System.currentTimeMillis()+"</prdImage01>");
		log.info("이미지 경로 ::::   "+http+imageUrl.getVal()+pa11stGoods.getImageUrl()+pa11stGoods.getImageP()+"?dc="+System.currentTimeMillis());
		
		if(pa11stGoods.getImageAP()!=null){
			reqXml.append("<prdImage02>"+http+imageUrl.getVal()+pa11stGoods.getImageUrl()+pa11stGoods.getImageAP()+"?dc="+System.currentTimeMillis()+"</prdImage02>");
		}else{
			reqXml.append("<prdImage02></prdImage02>");
		}
		
		if(pa11stGoods.getImageBP()!=null){
			reqXml.append("<prdImage03>"+http+imageUrl.getVal()+pa11stGoods.getImageUrl()+pa11stGoods.getImageBP()+"?dc="+System.currentTimeMillis()+"</prdImage03>");
		}else{
			reqXml.append("<prdImage03></prdImage03>");
		}
		
		if(pa11stGoods.getImageCP()!=null){
			reqXml.append("<prdImage04>"+http+imageUrl.getVal()+pa11stGoods.getImageUrl()+pa11stGoods.getImageCP()+"?dc="+System.currentTimeMillis()+"</prdImage04>");
		}else{
			reqXml.append("<prdImage04></prdImage04>");
		}
		
		//yekim 20.03.19 목록이미지 prdImage01(대표 이미지 URL)과 동일하게. 
		reqXml.append("<prdImage05>"+http+imageUrl.getVal()+pa11stGoods.getImageUrl()+pa11stGoods.getImageP()+"?dc="+System.currentTimeMillis()+"</prdImage05>");
		/*if(pa11stGoods.getImageDP()!=null)
			reqXml.append("<prdImage05>"+http+imageUrl.getVal()+pa11stGoods.getImageUrl()+pa11stGoods.getImageDP()+"?dc="+System.currentTimeMillis()+"</prdImage05>");*/
		reqXml.append("<htmlDetail>"+"<![CDATA["+pa11stGoods.getDescribeExt()+"]]>"+"</htmlDetail>");
		if(pa11stGoods.getCertYn().equals("1")){//인증필수
			//crtfGrpTypCd 인증정보그룹→ 01 : 전기용품/생활용품 KC인증→ 02 : 어린이제품 KC인증→ 03 : 방송통신기자재 KC인증→ 04 : 위해우려제품 모두 써줘야함
			//crtfGrpObjClfCd 인증정보그룹→ 01 : KC인증대상→ 02 : KC면제대상→ 03 : KC인증대상 아님→ 04 : 위해우려제품 대상→ 05 : 위해우려제품 대상 아님
			reqXml.append("<ProductCertGroup>");
			reqXml.append("<crtfGrpTypCd>01</crtfGrpTypCd>");
			reqXml.append("<crtfGrpObjClfCd>01</crtfGrpObjClfCd>");
			reqXml.append("<ProductCert><certTypeCd>132</certTypeCd></ProductCert>");
			reqXml.append("</ProductCertGroup>");
			
			reqXml.append("<ProductCertGroup>");
			reqXml.append("<crtfGrpTypCd>02</crtfGrpTypCd>");
			reqXml.append("<crtfGrpObjClfCd>03</crtfGrpObjClfCd>");
			reqXml.append("</ProductCertGroup>");
			
			reqXml.append("<ProductCertGroup>");
			reqXml.append("<crtfGrpTypCd>03</crtfGrpTypCd>");
			reqXml.append("<crtfGrpObjClfCd>03</crtfGrpObjClfCd>");
			reqXml.append("</ProductCertGroup>");
			
			reqXml.append("<ProductCertGroup>");
			reqXml.append("<crtfGrpTypCd>04</crtfGrpTypCd>");
			reqXml.append("<crtfGrpObjClfCd>05</crtfGrpObjClfCd>");
			reqXml.append("</ProductCertGroup>");
		}
		
		if(pa11stGoods.getMedicalKey() != null && pa11stGoods.getMedicalRetail() != null && pa11stGoods.getMedicalAd() != null){
			reqXml.append("<ProductMedical>");//의료기기 품목허가
			reqXml.append("<MedicalKey>"+pa11stGoods.getMedicalKey()+"</MedicalKey>");
			reqXml.append("<MedicalRetail>"+pa11stGoods.getMedicalRetail()+"</MedicalRetail>");
			reqXml.append("<MedicalAd>"+pa11stGoods.getMedicalAd()+"</MedicalAd>");
			reqXml.append("</ProductMedical>");
		}
		
		if(pa11stGoods.getBeefTraceNo() != null){
			reqXml.append("<beefTraceStat>"+"01"+"</beefTraceStat>");
			reqXml.append("<beefTraceNo>"+pa11stGoods.getBeefTraceNo()+"</beefTraceNo>");
		}
		
		reqXml.append("<sellerPrdCd>"+pa11stGoods.getGoodsCode()+"</sellerPrdCd>"); //판매자 상품코드
		reqXml.append("<selTermUseYn>"+"N"+"</selTermUseYn>");//판매기간 설정안함
		
		
		//TODO 판매가 기준변경... 
		//sale_price - dc_amt - immedate coupon promo		
		int salePrice = (int) pa11stGoods.getSalePrice(); 
		
		double dcAmt 		= pa11stGoods.getDcAmt();	// ARS할인금액
		double lumpSumDcAmt = pa11stGoods.getLumpSumDcAmt(); 				// 일시불 할인금액
		double couponPrice  = 0;
		
		//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
		for(PaPromoTarget promoTarget : paPromoTarget) {
			if(promoTarget.getProcGb() != null){
				if(!promoTarget.getProcGb().equals("D")){
					couponPrice += promoTarget.getDoCost(); //할인쿠폰(자동적용쿠폰 + 제휴OUT) 할인금액
				}
			}
		}
		//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
		
		double promoPrice = dcAmt + lumpSumDcAmt + couponPrice; //SD1 연동 금액
				
		reqXml.append("<selPrc>"+salePrice+"</selPrc>");//판매가
		
		// 판매가 기준변경 적용 시 주석 처리 해야함
		if(promoPrice > 0){
			reqXml.append("<cuponcheck>Y</cuponcheck>");//기본즉시할인 설정여부
			reqXml.append("<dscAmtPercnt>"+(long)promoPrice+"</dscAmtPercnt>");//할인금액
			reqXml.append("<cupnDscMthdCd>01</cupnDscMthdCd>");//01:원, 02:%
		}
		
		reqXml.append("<optSelectYn>Y</optSelectYn>");
		reqXml.append("<txtColCnt>1</txtColCnt>");
		reqXml.append("<prdExposeClfCd>00</prdExposeClfCd>");
		
		
		/** 
		 아래부터 신규 개발 사항 
		 1.브랜드 - TPA11STGOODS 에 BRAND_CODE 컬럼 추가, TPA11STBRAND
		 2.단일상품 - TPA11STGOODS에 단일상품여부(STD_PRD_YN) 컬럼 추가, 단일상품일시 
		 의류가 아닌 상품 단일상품 등록 시 - 일반상품 등록
		 의류가      상품 단일상품 등록 시 - (조건안맞을시) 상품등록 실패
	 	 의류가      상품 단일상품 등록 시 - (조건맞을시)   단일상품등록 성공
		 ⓞ단일상품 등록&수정 전문
	 	 ⓞ카테고리에 단일상품여부 컬럼 추가
		 ⓞ단일상품 등록조건
		 if( 단일가능 카테고리 &&  단품색상&크기만 보고 무늬&형태는 버림 ){
			if(단품 cnt = 1){
			reqXml.append("<stdPrdYn>Y</stdPrdYn>"); //단일상품 여부 Y로 보냄
			reqXml.append("<prdSelQty>10000</prdSelQty>"); //(단품 1개인경우) 재고수량 통으로 보냄
			} else { //단품이 여러개인 경우
			reqXml.append("<optionAllQty>10000</optionAllQty>");//옵션값에 갯수가없을때 이걸로 셋팅됨
			reqXml.append("<optionAllAddPrc>0</optionAllAddPrc>"); //옵션추가금
			reqXml.append("<optMixYn>N</optMixYn>");//조합형 옵션사용 여부
			reqXml.append("<optUpdateYn>Y</optUpdateYn>");
		
			//아래 ProductOption을 통해 옵션추가 가능
			reqXml.append("<ProductOption>");
			reqXml.append("<colOptPrice>0</colOptPrice>");
			reqXml.append("<colOptCount>10</colOptCount>"); //옵션재고 10, 없으면 optionAllQty로 각각 보내짐
			reqXml.append("<optionMappingKey>"+"색상:레드†사이즈:72"+"</optionMappingKey>");
			reqXml.append("</ProductOption>");
			}
				
		 }else{  //기존의 로직
			reqXml.append("<ProductOption>");
			reqXml.append("<useYn>N</useYn>");
			reqXml.append("<useYn>Y</useYn>");
			reqXml.append("<colOptPrice>0</colOptPrice>");
			reqXml.append("<colValue0><![CDATA["+pa11stGoodsdt.getGoodsdtInfo()+"]]></colValue0>");
			reqXml.append("<colCount>"+pa11stGoodsdt.getTransOrderAbleQty()+"</colCount>");
			reqXml.append("<colSellerStockCd>"+pa11stGoodsdt.getGoodsCode()+pa11stGoodsdt.getGoodsdtCode()+pa11stGoods.getPaCode()+"</colSellerStockCd>");
			reqXml.append("<optionMappingKey>"+"색상:레드2+사이즈:77"+"</optionMappingKey>");
			reqXml.append("</ProductOption>");
		 }
		 3.배송비 - 하기건 개발완료, 테스트중
		 * */
		
		//reqXml.append("<apiPrdAttrBrandCd>"+pa11stGoods.getBrandCode()+"</apiPrdAttrBrandCd>"); //브랜드코드

		/** 단일상품 신규 개발*/
		/*reqXml.append("<stdPrdYn>Y</stdPrdYn>"); //단일상품 여부 Y로 보냄
		reqXml.append("<prdSelQty>10000</prdSelQty>"); //(단품 1개인경우) 재고수량 통으로 보냄
		
		reqXml.append("<optionAllQty>10000</optionAllQty>");//옵션값에 갯수가없을때 이걸로 셋팅됨
		reqXml.append("<optionAllAddPrc>0</optionAllAddPrc>"); //옵션추가금
		reqXml.append("<optMixYn>N</optMixYn>");//조합형 옵션사용 여부
		reqXml.append("<optUpdateYn>Y</optUpdateYn>");
		//ProductOptionExt
		reqXml.append("<ProductOptionExt>");
		
		reqXml.append("<ProductOption>");
		reqXml.append("<colOptPrice>0</colOptPrice>");
		reqXml.append("<optionMappingKey>"+"색상:xxx†사이즈:xx"+"</optionMappingKey>");
		reqXml.append("</ProductOption>");
		
		reqXml.append("<ProductOption>");
		reqXml.append("<colOptPrice>0</colOptPrice>"); 
		reqXml.append("<optionMappingKey>"+"색상:네이비†사이즈:P1"+"</optionMappingKey>");
		reqXml.append("</ProductOption>");
		
		reqXml.append("<ProductOption>");
		reqXml.append("<colOptPrice>0</colOptPrice>");
		reqXml.append("<colOptCount>10</colOptCount>"); //옵션재고 10, 없으면 optionAllQty로 각각 보내짐
		reqXml.append("<optionMappingKey>"+"색상:레드†사이즈:72"+"</optionMappingKey>");
		reqXml.append("</ProductOption>");
		
		reqXml.append("<ProductOption>");
		reqXml.append("<colOptPrice>0</colOptPrice>");
		reqXml.append("<colOptCount>10</colOptCount>"); // 옵션재고
		reqXml.append("<optionMappingKey>"+"색상:노랑노랑해†사이즈:77"+"</optionMappingKey>");
		reqXml.append("</ProductOption>");
		
		reqXml.append("</ProductOptionExt>");*/
		/** 여기까지 신규개발*/
		
		
		reqXml.append("<colTitle>옵션명</colTitle>");
		if(pa11stGoodsdtList.size() > 0){
			
			//리텐션
			String retentionSeq = pa11stGoodsService.selectMaxRetentionSeq(pa11stGoodsdtList.get(0).getGoodsCode(), pa11stGoodsdtList.get(0).getPaCode());
			if(retentionSeq == null) retentionSeq = "";
			
			for(int i=0; i<pa11stGoodsdtList.size(); i++){
				PaGoodsdtMapping pa11stGoodsdt = pa11stGoodsdtList.get(i);
				reqXml.append("<ProductOption>");
				if(pa11stGoodsdt.getTransOrderAbleQty().equals("0")){
					reqXml.append("<useYn>N</useYn>");
				}else{
					reqXml.append("<useYn>Y</useYn>");
				}
				reqXml.append("<colOptPrice>0</colOptPrice>");
				reqXml.append("<colValue0><![CDATA["+pa11stGoodsdt.getGoodsdtInfo()+"]]></colValue0>");
				reqXml.append("<colCount>"+pa11stGoodsdt.getTransOrderAbleQty()+"</colCount>");
				reqXml.append("<colSellerStockCd>"+pa11stGoodsdt.getGoodsCode()+pa11stGoodsdt.getGoodsdtCode()+pa11stGoods.getPaCode() + retentionSeq +"</colSellerStockCd>");
				reqXml.append("</ProductOption>");
			}
		}
		
		reqXml.append("<selMinLimitTypCd>01</selMinLimitTypCd>"); //최소구매수량 설정코드 (01:1회제한)
		reqXml.append("<selMinLimitQty>"+pa11stGoods.getOrderMinQty()+"</selMinLimitQty>"); //최소구매수량 설정코드 (01:1회제한)
		
		if(pa11stGoods.getOrderMaxQty() > 0){
			if(pa11stGoods.getCustOrdQtyCheckYn()==0){//주문수량검사여부 컬럼 추가 18.06.05 thjeon
				reqXml.append("<selLimitTypCd>01</selLimitTypCd>"); //최대구매수량  설정코드 (01:1회제한)
				reqXml.append("<selLimitQty>"+pa11stGoods.getOrderMaxQty()+"</selLimitQty>");
			} else{
				reqXml.append("<selLimitTypCd>02</selLimitTypCd>"); //최대구매수량  설정코드 (02:기간제한)
				//reqXml.append("<selLimitQty>"+pa11stGoods.getOrderMaxQty()+"</selLimitQty>"); //정책에따라 변경가능
				reqXml.append("<selLimitQty>"+pa11stGoods.getTermOrderQty()+"</selLimitQty>");
				long townSelLmtDy = pa11stGoods.getCustOrdQtyCheckTerm();
				if(townSelLmtDy==0 || townSelLmtDy>30){
					townSelLmtDy=30;
				}
				reqXml.append("<townSelLmtDy>"+townSelLmtDy+"</townSelLmtDy>");
			}
		}
		if(pa11stGoods.getDoNotIslandDelyYn().equals("1")){
			reqXml.append("<dlvCnAreaCd>02</dlvCnAreaCd>"); //배송가능지역 (02:전국(제주 도서산간지역 제외))
			reqXml.append("<advrtStmt>도서산간 배송불가</advrtStmt>"); //상품홍보문구 : 도서산간불가			
		} else {
			reqXml.append("<dlvCnAreaCd>01</dlvCnAreaCd>"); //배송가능지역 (01:전국)
			reqXml.append("<advrtStmt> </advrtStmt>"); //수정시 셋팅안하면 도서산간 배송불가 문구 남아있음 
		}
//		reqXml.append("<dlvWyCd>01</dlvWyCd>"); //배송방법 (01:택배)
		reqXml.append("<dlvWyCd>"+ (pa11stGoods.getInstallYn().equals("1") ? "03" : "01")+"</dlvWyCd>"); //배송방법 (01:택배, 03:직접전달)
		reqXml.append("<dlvSendCloseTmpltNo>"+pa11stGoods.getPaPolicyNo()+"</dlvSendCloseTmpltNo>"); //발송 템플릿 no
		
		if("2".equals(pa11stGoods.getShipCostReceipt())) {
			reqXml.append("<dlvCstInstBasiCd>02</dlvCstInstBasiCd>");//배송비종류 (01:무료, 02:고정배송비, 03:상품조건부무료, 05:1개당배송비, 08: 출고지조건부무료)
			reqXml.append("<dlvCst1>"+pa11stGoods.getOrdCost()+"</dlvCst1>");
			reqXml.append("<dlvCstInfoCd>01</dlvCstInfoCd>");// 상품상세참고
			reqXml.append("<dlvCstPayTypCd>02</dlvCstPayTypCd>"); //결제방법(02: 착불, 03:선결제필수)
		}else if(pa11stGoods.getShipCostCode().substring(0, 2).equals("FR")){
			pa11stGoods.setOrdCost(0);
			reqXml.append("<dlvCstInstBasiCd>01</dlvCstInstBasiCd>");//배송비종류 (01:무료, 03:상품조건부무료, 05:1개당배송비, 02:고정배송비, 08: 출고지조건부무료)
			reqXml.append("<bndlDlvCnYn>Y</bndlDlvCnYn>"); // 묶음배송가능여부
			reqXml.append("<dlvCstPayTypCd>03</dlvCstPayTypCd>"); //결제방법(02: 착불, 03:선결제필수)
		} else if(pa11stGoods.getShipCostCode().substring(0, 2).equals("CN")){
			reqXml.append("<dlvCstInstBasiCd>08</dlvCstInstBasiCd>");
			reqXml.append("<bndlDlvCnYn>Y</bndlDlvCnYn>"); // 묶음배송가능여부
			reqXml.append("<dlvCstPayTypCd>03</dlvCstPayTypCd>"); //결제방법(02: 착불, 03:선결제필수)
		} else if(pa11stGoods.getShipCostCode().substring(0, 2).equals("PL")){
			reqXml.append("<dlvCstInstBasiCd>03</dlvCstInstBasiCd>");
			reqXml.append("<dlvCst1>"+pa11stGoods.getOrdCost()+"</dlvCst1>");
			reqXml.append("<PrdFrDlvBasiAmt>"+pa11stGoods.getShipCostBaseAmt()+"</PrdFrDlvBasiAmt>"); //기준금액
			reqXml.append("<bndlDlvCnYn>N</bndlDlvCnYn>"); // 묶음배송가능여부
			reqXml.append("<dlvCstPayTypCd>03</dlvCstPayTypCd>"); //결제방법(02: 착불, 03:선결제필수)
		} else if(pa11stGoods.getShipCostCode().substring(0, 2).equals("ID")){
			reqXml.append("<dlvCstInstBasiCd>02</dlvCstInstBasiCd>");
			reqXml.append("<dlvCst1>"+pa11stGoods.getOrdCost()+"</dlvCst1>");//고정배송비
			reqXml.append("<bndlDlvCnYn>N</bndlDlvCnYn>"); // 묶음배송가능여부
			reqXml.append("<dlvCstPayTypCd>03</dlvCstPayTypCd>"); //결제방법(02: 착불, 03:선결제필수)
		}

//		reqXml.append("<dlvCstPayTypCd>03</dlvCstPayTypCd>"); //결제방법(03:선결제필수)
		
		long addJejuShipcost = pa11stGoods.getJejuCost()-pa11stGoods.getOrdCost();
		long addIslandShipcost = pa11stGoods.getJejuCost()-pa11stGoods.getOrdCost();
		
		if(addJejuShipcost < 0){
			addJejuShipcost = 0;
		}
		if(addIslandShipcost < 0){
			addIslandShipcost = 0;
		}
		
		reqXml.append("<jejuDlvCst>"+addJejuShipcost+"</jejuDlvCst>");//제주 추가 배송비(주문만)
		reqXml.append("<islandDlvCst>"+addIslandShipcost+"</islandDlvCst>");//도서산간 추가 배송비(주문만)
		
		reqXml.append("<addrSeqOut>"+pa11stGoods.getAddrSeqOut()+"</addrSeqOut>");//출고지 주소코드		
		reqXml.append("<addrSeqIn>"+pa11stGoods.getAddrSeqIn()+"</addrSeqIn>");//반품/교환지 주소코드
		
		if(pa11stGoodsOffer.size() > 0){
			reqXml.append("<ProductNotification>");
			reqXml.append("<type>"+pa11stGoodsOffer.get(0).getPaOfferType()+"</type>");
			for(int i=0; i<pa11stGoodsOffer.size(); i++){
				PaGoodsOffer goodsOffer = pa11stGoodsOffer.get(i);
				reqXml.append("<item>");
				reqXml.append("<code>"+goodsOffer.getPaOfferCode()+"</code>");
				reqXml.append("<name><![CDATA["+goodsOffer.getPaOfferExt().replace("<", " ").replace(">", " ")+"]]></name>");
				reqXml.append("</item>");
			}
			reqXml.append("</ProductNotification>");
		}
	
		reqXml.append("<rtngdDlvCst>"+pa11stGoods.getReturnCost()+"</rtngdDlvCst>");
		reqXml.append("<exchDlvCst>"+pa11stGoods.getChangeCost()+"</exchDlvCst>");
		/* 없어도 되는지 ?*/
		 reqXml.append("<rtngdDlvCd>"+"02"+"</rtngdDlvCd>");//반품시 왕복배송비(01,입력금액2배), 편도(02)로받을지 받을지 
		
		reqXml.append("<asDetail>"+pa11stGoods.getCsTel()+"</asDetail>");//A/S안내
		reqXml.append("<rtngExchDetail>SK Stoa : "+pa11stGoods.getCsTel()+"</rtngExchDetail>");//반품/교환안내
		
		reqXml.append("<prcCmpExpYn>Y</prcCmpExpYn>"); //가격비교사이트등록여부
		
		reqXml.append("<rmaterialTypCd>05</rmaterialTypCd>"); //원재료 유형 코드
		
		reqXml.append("</Product>");

		return reqXml;
	}
	
	/**
	 * 상품가격/즉시할인 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품가격/즉시할인 수정", notes = "상품가격/즉시할인 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})	
	@RequestMapping(value = "/goodsprice-coupon-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodspriceCouponModify(HttpServletRequest request,
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "")			@RequestParam(value="goodsCode", required=false) String goodsCode,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 			@RequestParam(value="paCode", required=false) String paCode,			
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 				@RequestParam(value="procId"		, required=false, defaultValue="PA11") String procId,
		@ApiParam(name = "searchTermGb", value = "중복실행체크여부", defaultValue = "")	@RequestParam(value="searchTermGb"	, required=false, defaultValue = "") String searchTermGb, 
		@ApiParam(name = "massInfoMap", value = "massInfoMap", defaultValue = "")	@RequestParam(value="massInfoMap"	, required=false, defaultValue = "") PaGoodsPriceVO massInfoMap) 
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		StringBuilder reqXml = null;
		OutputStreamWriter out = null;
		int respCode = 0;
		String respMsg = null;

		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		List<PaGoodsPriceVO> paGoodsPriceList = null;
		List<PaPromoTarget> paPromoTargetList = null;
		PaGoodsPriceVO paGoodsPrice = null;
	
		log.info("===== 상품가격/즉시할인정보 수정 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_01_003";
		String request_type = "GET";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		int priceFailCnt = 0;
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			}	
			
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.SK스토아 상품가격/즉시할인 수정대상 조회");
			if(massInfoMap == null) {
				paGoodsPriceList = pa11stGoodsService.selectPa11stGoodsPriceList(paramMap);	
			}else {
				paGoodsPriceList = new ArrayList<PaGoodsPriceVO>();
				paGoodsPriceList.add(massInfoMap);
			}
			
			if(paGoodsPriceList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			for(int i=0; i<paGoodsPriceList.size(); i++){
				
				try {
					paGoodsPrice = paGoodsPriceList.get(i);
					
					//log.info("04.11번가 API호출");
					if(paGoodsPrice.getPaCode().equals(Constants.PA_11ST_BROAD_CODE)){
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"/"+paGoodsPrice.getProductNo());
					} else {
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"/"+paGoodsPrice.getProductNo());
					}
					reqXml = new StringBuilder();
					reqXml.append("<Product>");
					
					paPromoTargetList = paPromoTargetInfoSetting(paramMap);
					
					double salePrice 	= Double.parseDouble(paGoodsPrice.getSalePrice());
					double dcAmt 		= Double.parseDouble(paGoodsPrice.getDcAmt());
					double lumpSumDcAmt = Double.parseDouble(paGoodsPrice.getLumpSumDcAmt());
					double couponPrice  = 0;				
					
					//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
					for(PaPromoTarget promoTarget : paPromoTargetList) {
						if(promoTarget.getProcGb() != null){
							if(!promoTarget.getProcGb().equals("D")){
								couponPrice += promoTarget.getDoCost(); //할인쿠폰(자동적용쿠폰 + 제휴OUT) 할인금액
							}
						}
					}
					//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
					
					double promoPrice = dcAmt + lumpSumDcAmt + couponPrice; //SD1 연동 금액
					
					
					reqXml.append("<selPrc>"+Math.round(salePrice)+"</selPrc>");
					
					if(promoPrice == 0){
						reqXml.append("<cuponcheck>N</cuponcheck>");
					} else {
						reqXml.append("<cuponcheck>Y</cuponcheck>");
						reqXml.append("<dscAmtPercnt>"+(long)promoPrice+"</dscAmtPercnt>");
						reqXml.append("<cupnDscMthdCd>01</cupnDscMthdCd>");
						reqXml.append("<cupnUseLmtDyYn>N</cupnUseLmtDyYn>");
					}
					reqXml.append("</Product>");
					
					if(reqXml != null){
						out = new OutputStreamWriter(conn.getOutputStream());
						out.write(String.valueOf(reqXml));
						out.flush();

						respCode = conn.getResponseCode();
						respMsg  = conn.getResponseMessage();
						log.info(" connect respCode : "+respCode);
						log.info(" connect respMsg  : "+respMsg);
						if(respCode == 200){
							
							// RESPONSE XML 			
							doc = ComUtil.parseXML(conn.getInputStream());
							descNodes = doc.getElementsByTagName("ClientMessage");
							conn.disconnect();
			
							for(int j=0; j<descNodes.getLength();j++){
						        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
						        	paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
						        }
						    }
							log.info("result message : "+paramMap.getString("message"));
							
							//전송관리 테이블 저장
							paGoodsTransLog = new PaGoodsTransLog();
							paGoodsTransLog.setGoodsCode(paGoodsPrice.getGoodsCode());
							paGoodsTransLog.setPaCode(paGoodsPrice.getPaCode());
							paGoodsTransLog.setItemNo(paGoodsPrice.getProductNo());
							paGoodsTransLog.setRtnCode(paramMap.getString("resultCode"));
							paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
							paGoodsTransLog.setSuccessYn(paramMap.getString("resultCode").equals("200")==true?"1":"0");
							paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paGoodsTransLog.setProcId("PA11");
							pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
						
							
							if(paramMap.getString("resultCode").equals("200")){
								paGoodsPrice.setModifyId("PA11");
								paGoodsPrice.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								//log.info("05.동기화 완료 저장");
								rtnMsg = pa11stGoodsService.savePa11stGoodsPriceTx(paGoodsPrice, paPromoTargetList);
								
								if(!rtnMsg.equals("000000")){
									paramMap.put("code","404");
									paramMap.put("message",rtnMsg);
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								} else {
									paramMap.put("code","200");
									paramMap.put("message","OK");
								}
							} else if( paramMap.getString("message").indexOf("가격 업데이트 실패") > -1){
								//가격 업데이트 실패 시 상품수정 재연동 처리
								paramMap.put("goodsCode", paGoodsPrice.getGoodsCode());
								paramMap.put("paCode", paGoodsPrice.getPaCode());
								paramMap.put("transTargetYn", "1");

								pa11stGoodsService.updatePa11stGoodsTransTargetYnTx(paramMap);
								
								paramMap.put("code","200");
								paramMap.put("message","OK");
								log.info("===TPA11STGOODS trans_target_yn 1 처리===");
							} else {
								paramMap.put("code",paramMap.getString("resultCode"));
							}
						}
					}
					
				}catch (Exception e) {
					priceFailCnt++;
					continue;
				}
				
				//실패건에 대한 throw(알림) 처리
				if(priceFailCnt > 0 ) {
					throw processException("msg.errors.detail", new String[] {"11번가 상품수정 실패 (기타이유) - " + priceFailCnt});
				}
				
				/*REQ_PRM_041 11번가 제휴OUT 딜 상품 옵션 수정 호출*/
				//딜 주석처리
				//alcoutDealGoodsdtModify(request, paramMap.getString("goodsCode"), paramMap.getString("paCode"), "PA11DEAL", "");
			}
		 
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			
			
			
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(!searchTermGb.equals("1")){
				if(duplicateCheck.equals("0")){
					systemService.checkCloseHistoryTx("end", prg_id);
				}				
			}
			
			//log.info("06.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품옵션 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품옵션 수정", notes = "상품옵션 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})	
	@RequestMapping(value = "/legacy-goodsdt-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsdtModify(HttpServletRequest request,
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "")	@RequestParam(value="goodsCode", required=false) String goodsCode,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 	@RequestParam(value="paCode", required=false) String paCode,			
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 				@RequestParam(value="procId", required=false, defaultValue="PA11") String procId,
		@ApiParam(name = "searchTermGb", value = "중복실행체크여부", defaultValue = "")	@RequestParam(value="searchTermGb", required=false, defaultValue = "") String searchTermGb) 
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		StringBuilder reqXml = null;
		OutputStreamWriter out = null;
		int respCode = 0;
		String respMsg = null;
		String responseTime = null;
		double timeS = 0;
		double timeE = 0;
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		List<Pa11stGoodsdtMappingVO> paGoodsdtMappingList = null;
		List<Pa11stGoodsdtMappingVO> paGoodsdtMappingParamList = null;
		Pa11stGoodsdtMappingVO paGoodsdtMapping = null;
		ResponseEntity<?> responseMsg = null;
		String tempGoodsCode = "";
	
		log.info("===== 상품옵션 수정 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_01_004";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			}
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paramMap.put("url", ConfigUtil.getString("PA11ST_COM_BASE_URL") + apiInfo.get("API_URL"));
			paramMap.put("header", "Content-Type=" + apiInfo.get("contentType"));

			log.info("03.SK스토아 tv쇼핑 상품옵션 수정대상 조회");
			paGoodsdtMappingList = pa11stGoodsService.selectPa11stGoodsdtMappingList(paramMap);
			if(paGoodsdtMappingList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			if(paGoodsdtMappingList.size() > 0){
				
				if(paramMap.getString("paCode").equals("")) {
					paramMap.put("paCode" , paGoodsdtMappingList.get(0).getPaCode());
				}
				
				tempGoodsCode = paGoodsdtMappingList.get(0).getGoodsCode();
				paGoodsdtMappingParamList = new ArrayList<Pa11stGoodsdtMappingVO>();		

				for(int i=0;i<paGoodsdtMappingList.size();i++){
					
					if(paGoodsdtMappingList.get(i).getGoodsCode().equals(tempGoodsCode)){
						paGoodsdtMapping = null;
						paGoodsdtMapping = paGoodsdtMappingList.get(i);
						if(paGoodsdtMapping.getSaleGb().equals("00") && !paGoodsdtMapping.getTransOrderAbleQty().equals("0")) {
							paGoodsdtMappingParamList.add(paGoodsdtMapping);
						} 
					} 
					
					if(!(paGoodsdtMappingList.get(i).getGoodsCode().equals(tempGoodsCode)) || (i == paGoodsdtMappingList.size()-1)){ //paGoodsdtMapping.getGoodsCode() != tempGoodsCode)
						
						try {
							//log.info("04.11번가 API호출");
							if(paGoodsdtMapping.getPaCode().equals(Constants.PA_11ST_BROAD_CODE)){
								conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"/"+paGoodsdtMapping.getProductNo());
							} else {
								conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"/"+paGoodsdtMapping.getProductNo());
							}
							reqXml = new StringBuilder();
							reqXml.append("<Product>");
							
							if(paGoodsdtMappingParamList.size() > 0){
								
								String retentionSeq = pa11stGoodsService.selectMaxRetentionSeq(paGoodsdtMappingParamList.get(0).getGoodsCode(), paGoodsdtMappingParamList.get(0).getPaCode());
								if(retentionSeq == null) retentionSeq = "";
								
								reqXml.append("<optSelectYn>Y</optSelectYn>");
								reqXml.append("<txtColCnt>1</txtColCnt>");
								reqXml.append("<prdExposeClfCd>00</prdExposeClfCd>");
								reqXml.append("<colTitle>옵션명</colTitle>");
								for(int j=0; j<paGoodsdtMappingParamList.size(); j++){
									PaGoodsdtMapping pa11stGoodsdt = paGoodsdtMappingParamList.get(j);
									reqXml.append("<ProductOption>");
									reqXml.append("<useYn>Y</useYn>");
									reqXml.append("<colOptPrice>0</colOptPrice>");
									reqXml.append("<colValue0><![CDATA["+pa11stGoodsdt.getGoodsdtInfo()+"]]></colValue0>");
									reqXml.append("<colCount>"+pa11stGoodsdt.getTransOrderAbleQty()+"</colCount>");
									reqXml.append("<colSellerStockCd>"+pa11stGoodsdt.getGoodsCode()+pa11stGoodsdt.getGoodsdtCode()+ retentionSeq + "</colSellerStockCd>");
									reqXml.append("</ProductOption>");
								}
							}
							reqXml.append("</Product>");
							
							if(reqXml != null){
								
								timeS = System.nanoTime();
								out = new OutputStreamWriter(conn.getOutputStream());
								out.write(String.valueOf(reqXml));
								out.flush();

								respCode = conn.getResponseCode();
								respMsg  = conn.getResponseMessage();
								//log.info(" connect respCode : "+respCode);
								//log.info(" connect respMsg  : "+respMsg);
								
								
								timeE = System.nanoTime();
								responseTime = Double.toString((timeE-timeS)/1000000000);
								paramMap.put("responseTime", responseTime);
								paramMap.put("body", reqXml);
								paramMap.put("result", respCode + " " + respMsg);
								
								pa11stAsyncController.insertPaRequestMap(paramMap);
								
								if(respCode == 200){
									// RESPONSE XML 			
									doc = ComUtil.parseXML(conn.getInputStream());
									descNodes = doc.getElementsByTagName("Product");
									conn.disconnect();

									for(int j=0; j<descNodes.getLength();j++){
								        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){
								        	paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
								        }
								    }
									log.info("result message : "+paramMap.getString("message"));
									
									//전송관리 테이블 저장
									paGoodsTransLog = new PaGoodsTransLog();
									paGoodsTransLog.setGoodsCode(paGoodsdtMapping.getGoodsCode());
									paGoodsTransLog.setPaCode(paGoodsdtMapping.getPaCode());
									paGoodsTransLog.setItemNo(paGoodsdtMapping.getProductNo());
									paGoodsTransLog.setRtnCode(paramMap.getString("resultCode"));
									paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
									paGoodsTransLog.setSuccessYn(paramMap.getString("resultCode").equals("200")==true?"1":"0");
									paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
									paGoodsTransLog.setProcId("PA11");
									pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
																	
									if(paramMap.getString("resultCode").equals("200")){
										for(int k=0; k<paGoodsdtMappingParamList.size(); k++){
											Pa11stGoodsdtMappingVO pa11stGoodsdtVO = paGoodsdtMappingParamList.get(k);
											pa11stGoodsdtVO.setModifyId("PA11");
											pa11stGoodsdtVO.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
											
											//log.info("05.동기화 완료 저장");
											rtnMsg = pa11stGoodsService.savePa11stGoodsdtMappingTx(pa11stGoodsdtVO);
											
											if(!rtnMsg.equals("000000")){
												paramMap.put("code","404");
												paramMap.put("message",rtnMsg);
												//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
											} else {
												paramMap.put("code","200");
												paramMap.put("message","OK");
											}
										}
										//SALE_GB 00 아닌 상품들도 TARGET 꺼지도록 함.
										pa11stGoodsService.savePa11stGoodsdtTargetTx( tempGoodsCode );
										
									} else {
										// 11번가 단품 에러 발생시 무조건 판매중단 처리 한다. 20220413 LEEJY
                                        paramMap.put("code",paramMap.getString("resultCode"));
                                        paramMap.put("message", paramMap.getString("message"));                                        
                                        paramMap.put("goodsCode", paGoodsdtMapping.getGoodsCode());
                                        paramMap.put("paCode", paGoodsdtMapping.getPaCode());
                                        paramMap.put("productNo", paGoodsdtMapping.getProductNo());                                       
                                        
                                        pa11stAsyncController.savePaGoodsModifyFail(paramMap);
									}
									
									log.info("06.재고번호 조회 API호출 : /goods-stock-list");
									
									responseMsg = goodsStockList(request, paGoodsdtMapping.getGoodsCode(), paGoodsdtMapping.getProductNo(), paGoodsdtMapping.getPaCode(), procId, "1");
									//log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
									if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
										paramMap.put("code","500");
										paramMap.put("message",getMessage("errors.api.system")+" : goodsStockList");
										//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
									}
									
								}	
							}
							tempGoodsCode = paGoodsdtMappingList.get(i).getGoodsCode();
							paGoodsdtMappingParamList = new ArrayList<Pa11stGoodsdtMappingVO>();
							paGoodsdtMapping = null;
							paGoodsdtMapping = paGoodsdtMappingList.get(i);
							paGoodsdtMappingParamList.add(paGoodsdtMapping);
						} catch (Exception e) {
							paramMap.put("code","500");
							paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
						}		
					}
					
					/*REQ_PRM_041 11번가 제휴OUT 딜 상품 옵션 수정 호출*/
					//딜 주석처리
					//alcoutDealGoodsdtModify(request, paGoodsdtMapping.getGoodsCode(), paGoodsdtMapping.getPaCode(), "PA11DEAL", "");
				}
				
			}
		 
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			//log.info("07.저장 완료 API END");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품재고 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품재고 수정", notes = "상품재고 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})	
	@RequestMapping(value = "/goods-stock-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsStockModify(HttpServletRequest request,
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "")	@RequestParam(value="goodsCode", required=false) String goodsCode,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 	@RequestParam(value="paCode", required=false) String paCode,			
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 		@RequestParam(value="procId", required=false, defaultValue="PA11") String procId)
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		StringBuilder reqXml = null;
		OutputStreamWriter out = null;
		int respCode = 0;
		String respMsg = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		List<Pa11stGoodsdtMappingVO> paGoodsdtMappingList = null;
		Pa11stGoodsdtMappingVO paGoodsdtMapping = null;
		
	
		//log.info("===== 상품재고 수정 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_01_005";
		String request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.SK스토아 상품재고 수정대상 조회");
			paGoodsdtMappingList = pa11stGoodsService.selectPa11stGoodsdtMappingStockList(paramMap);
			if(paGoodsdtMappingList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			for(int i=0; i<paGoodsdtMappingList.size(); i++){
				paGoodsdtMapping = paGoodsdtMappingList.get(i);
				//log.info("04.11번가 API호출");
				if(paGoodsdtMapping.getPaCode().equals(Constants.PA_11ST_BROAD_CODE)){
					conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"/"+paGoodsdtMapping.getPaOptionCode());
				} else {
					conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"/"+paGoodsdtMapping.getPaOptionCode());
				}

				reqXml = new StringBuilder();
				reqXml.append("<ProductStock>");
				reqXml.append("<prdNo>"+paGoodsdtMapping.getProductNo()+"</prdNo>");
				reqXml.append("<prdStckNo>"+paGoodsdtMapping.getPaOptionCode()+"</prdStckNo>");
				reqXml.append("<stckQty>"+paGoodsdtMapping.getTransOrderAbleQty()+"</stckQty>");				
				reqXml.append("</ProductStock>");
				
				if(reqXml != null){
					out = new OutputStreamWriter(conn.getOutputStream());
					out.write(String.valueOf(reqXml));
					out.flush();

					respCode = conn.getResponseCode();
					respMsg  = conn.getResponseMessage();
					//log.info(" connect respCode : "+respCode);
					//log.info(" connect respMsg  : "+respMsg);
					if(respCode == 200){
						// RESPONSE XML 			
						doc = ComUtil.parseXML(conn.getInputStream());
						descNodes = doc.getElementsByTagName("ClientMessage");
						conn.disconnect();

						for(int j=0; j<descNodes.getLength();j++){
					        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
					        	paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
					        }
					    }
						log.info("result message : "+paramMap.getString("message"));
						
						//전송관리 테이블 저장
						paGoodsTransLog = new PaGoodsTransLog();
						paGoodsTransLog.setGoodsCode(paGoodsdtMapping.getGoodsCode());
						paGoodsTransLog.setPaCode(paGoodsdtMapping.getPaCode());
						paGoodsTransLog.setItemNo(paGoodsdtMapping.getProductNo());
						paGoodsTransLog.setRtnCode(paramMap.getString("resultCode"));
						paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
						paGoodsTransLog.setSuccessYn(paramMap.getString("resultCode").equals("200")==true?"1":"0");
						paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paGoodsTransLog.setProcId("PA11");
						pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
					
						
						if(paramMap.getString("resultCode").equals("200")){
							paGoodsdtMapping.setModifyId("PA11");
							paGoodsdtMapping.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							//log.info("05.동기화 완료 저장");
							rtnMsg = pa11stGoodsService.savePa11stGoodsdtQtyTx(paGoodsdtMapping);
							
							if(!rtnMsg.equals("000000")){
								paramMap.put("code","404");
								paramMap.put("message",rtnMsg);
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							} else {
								paramMap.put("code","200");
								paramMap.put("message","OK");
							}
						} else if( paramMap.getString("message").indexOf("옵션재고 번호") > -1 && paramMap.getString("message").indexOf("수량 업데이트 실패") > -1){
							//11번가에 상품 없어져 재고 연동 불가능할 경우 연동 제외 처리. -> 옵션수정 연동하면 재고번호 갱신되고 재고변경 가능하여 상품 수정 태우기
							paramMap.put("goodsCode", paGoodsdtMapping.getGoodsCode());
							paramMap.put("paCode", paGoodsdtMapping.getPaCode());
							paramMap.put("transTargetYn", "1");
							
							pa11stGoodsService.updateTransTargetYnTx(paramMap);
							
							paramMap.put("code","200");
							paramMap.put("message","OK");
							log.info("==재고번호 찾을수 없어단품 코드 재연동 처리 process===");
						} else if( paramMap.getString("message").indexOf("재고번호가 아닙니다") > -1){
							//11번가에 상품 없어져 재고 번호 없을 경우 상품 수정 태우기.
							paramMap.put("goodsCode", paGoodsdtMapping.getGoodsCode());
							paramMap.put("paCode", paGoodsdtMapping.getPaCode());
							paramMap.put("transTargetYn", "1");

							pa11stGoodsService.updateTransTargetYnTx(paramMap);
							
							paramMap.put("code","200");
							paramMap.put("message","OK");
							log.info("===재고번호 사라져 단품 코드 재연동 처리===");
						} else {
							paramMap.put("code",paramMap.getString("resultCode"));
						}
					}
				}
				/*REQ_PRM_041 11번가 제휴OUT 딜 상품 옵션 수정 호출*/
				//alcoutDealGoodsdtModify(request, paGoodsdtMapping.getGoodsCode(), paGoodsdtMapping.getPaCode(), "PA11DEAL", "");
			}
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			//log.info("06.저장 완료 API END");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품재고번호 조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	//11번가 PRODUCTNO 필수
	@ApiOperation(value = "상품재고번호 조회", notes = "상품재고번호 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})	
	@RequestMapping(value = "/goods-stock-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsStockList(HttpServletRequest request,
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "")			@RequestParam(value="goodsCode", required=true) String goodsCode,
		@ApiParam(name = "productNo", value = "11번가 상품코드", defaultValue = "") 		@RequestParam(value="productNo", required=true) String productNo,			
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 			@RequestParam(value="paCode", required=true) String paCode,			
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 				@RequestParam(value="procId"		, required=false, defaultValue="PA11") String procId,
		@ApiParam(name = "searchTermGb", value = "중복실행체크여부", defaultValue = "")	@RequestParam(value="searchTermGb"	, required=false, defaultValue = "") String searchTermGb)
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		StringBuilder reqXml = null;
		OutputStreamWriter out = null;
		int respCode = 0;
		String respMsg = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
		List<Pa11stGoodsdtMappingVO> paGoodsdtMappingList = new ArrayList<Pa11stGoodsdtMappingVO>();		
	
		log.info("===== 상품재고번호 조회 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_01_006";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			}
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			paramMap.put("productNo", productNo);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			//log.info("03.11번가 API호출");
			System.out.println("재고조회호출");
			if(paCode.equals(Constants.PA_11ST_BROAD_CODE)){
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"");
			} else {
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"");
			}
			System.out.println("재고조회종료");

			reqXml = new StringBuilder();
			reqXml.append("<ProductStocks>");
			reqXml.append("<ProductStock>");
			reqXml.append("<prdNo>"+productNo+"</prdNo>");	
			reqXml.append("</ProductStock>");
			reqXml.append("</ProductStocks>");
			
			if(reqXml != null){
				out = new OutputStreamWriter(conn.getOutputStream());
				out.write(String.valueOf(reqXml));
				out.flush();

				respCode = conn.getResponseCode();
				respMsg  = conn.getResponseMessage();
				//log.info(" connect respCode : "+respCode);
				//log.info(" connect respMsg  : "+respMsg);
				if(respCode == 200){
					// RESPONSE XML 			
					doc = ComUtil.parseXML(conn.getInputStream());
					descNodes = doc.getElementsByTagName("ns2:ProductStockss");
					conn.disconnect();
					
					List<ParamMap> productStockList = new ArrayList<ParamMap>();

					for(int j=0; j<descNodes.getLength();j++){
				        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
			            	if(node.getNodeName().trim().equals("ns2:ProductStocks")){
			            		for(int x=0; x<node.getChildNodes().getLength(); x++){
				            		if(node.getChildNodes().item(x).getNodeName().trim().equals("ns2:ProductStock")){
					            		ParamMap pa11stParamMap = new ParamMap();
					            		Node pa11stList = node.getChildNodes().item(x);
					            		for(Node pa11st = pa11stList.getFirstChild(); pa11st!=null; pa11st=pa11st.getNextSibling()){ 
					            			pa11stParamMap.put(pa11st.getNodeName().trim(), pa11st.getTextContent().trim());
					            		}
					            		productStockList.add(pa11stParamMap);
			            			}
			            		}
			            	} else {
			            		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
				        	}
				        }
				    }
					//log.info("result message : "+paramMap.getString("ns2:message"));
					if(productStockList.size() > 0){
						for(int i=0; i<productStockList.size(); i++){
							Pa11stGoodsdtMappingVO paGoodsdtMapping = new Pa11stGoodsdtMappingVO();
							paGoodsdtMapping.setGoodsCode(goodsCode);
							paGoodsdtMapping.setPaCode(paCode);
							paGoodsdtMapping.setSellerStockCd(productStockList.get(i).getString("sellerStockCd"));
							paGoodsdtMapping.setPaOptionCode(productStockList.get(i).getString("prdStckNo"));
							paGoodsdtMapping.setTransOrderAbleQty(productStockList.get(i).getString("stckQty"));
							paGoodsdtMapping.setModifyId("PA11");
							paGoodsdtMapping.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paGoodsdtMappingList.add(paGoodsdtMapping);
						}
						paGoodsTransLog.setRtnCode("200");
						paGoodsTransLog.setSuccessYn("1");
						paramMap.put("message", "OK");
						paramMap.put("code", "200");

					} else {
						paGoodsTransLog.setRtnCode("404");
						paGoodsTransLog.setSuccessYn("0");
						paramMap.put("message", paramMap.getString("ns2:message"));
						paramMap.put("code", "404");
					}
					//전송관리 테이블 저장
					paGoodsTransLog.setGoodsCode(goodsCode);
					paGoodsTransLog.setPaCode(paCode);
					paGoodsTransLog.setItemNo(productNo);
					paGoodsTransLog.setRtnMsg(paramMap.getString("ns2:message"));
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId("PA11");
					pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
				
					
					if(paGoodsdtMappingList.size()>0){
						log.info("04.재고번호 저장");
						rtnMsg = pa11stGoodsService.savePa11stGoodsdtMappingPaOptionCodeTx(paGoodsdtMappingList);
						if(!rtnMsg.equals("000000")){
							paramMap.put("code","404");
							paramMap.put("message",rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paramMap.put("code","200");
							paramMap.put("message","OK");
						}
					}
				}
			}
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			//log.info("05.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 * 상품상세설명 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품상세설명 수정", notes = "상품상세설명 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})	
	@RequestMapping(value = "/goods-desc-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsDescModify(HttpServletRequest request,
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "")	@RequestParam(value="goodsCode", required=true) String goodsCode,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 	@RequestParam(value="paCode", required=true) String paCode,			
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 		@RequestParam(value="procId", required=false, defaultValue="PA11") String procId)
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		StringBuilder reqXml = null;
		OutputStreamWriter out = null;
		int respCode = 0;
		String respMsg = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		HashMap<String, String> goodsDesc = new HashMap<String, String>();
	
		log.info("===== 상품상세설명 수정 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_01_007";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.SK스토아 상품상세설명 수정대상 조회");
			goodsDesc = pa11stGoodsService.selectPa11stGoodsDescribe(paramMap);
			if(goodsDesc==null || goodsDesc.isEmpty()){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}

			//log.info("04.11번가 API호출");
			if(goodsDesc.get("PA_CODE").equals(Constants.PA_11ST_BROAD_CODE)){
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"/"+goodsDesc.get("PRODUCT_NO"));
			} else {
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"/"+goodsDesc.get("PRODUCT_NO"));
			}
			
			reqXml = new StringBuilder();
			reqXml.append("<ProductDetailCont>");
			reqXml.append("<prdDescContClob>"+"<![CDATA["+goodsDesc.get("DESCRIBE_EXT")+goodsDesc.get("DESCRIBE_EXT1")+"]]>"+"</prdDescContClob>");
			reqXml.append("</ProductDetailCont>");
			
			if(reqXml != null){
				out = new OutputStreamWriter(conn.getOutputStream());
				out.write(String.valueOf(reqXml));
				out.flush();

				respCode = conn.getResponseCode();
				respMsg  = conn.getResponseMessage();
				//log.info(" connect respCode : "+respCode);
				//log.info(" connect respMsg  : "+respMsg);
				if(respCode == 200){
					// RESPONSE XML 			
					doc = ComUtil.parseXML(conn.getInputStream());
					descNodes = doc.getElementsByTagName("ProductDetailCont");
					conn.disconnect();

					for(int j=0; j<descNodes.getLength();j++){
				        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
				        	paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
				        }
				    }
					log.info("result message : "+paramMap.getString("message"));
					
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(goodsDesc.get("GOODS_CODE"));
					paGoodsTransLog.setPaCode(goodsDesc.get("PA_CODE"));
					paGoodsTransLog.setItemNo(goodsDesc.get("PRODUCT_NO"));
					paGoodsTransLog.setRtnCode(paramMap.getString("resultCode"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn(paramMap.getString("resultCode").equals("200")==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId("PA11");
					pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
				
					
					if(paramMap.getString("resultCode").equals("000")){
						goodsDesc.put("MODIFY_ID","PA11");
						goodsDesc.put("MODIFY_DATE",dateTime);

						//log.info("05.동기화 완료 저장");
						rtnMsg = pa11stGoodsService.savePa11stGoodsDescribeTx(goodsDesc);
						
						if(!rtnMsg.equals("000000")){
							paramMap.put("code","404");
							paramMap.put("message",rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paramMap.put("code","200");
							paramMap.put("message","OK");
						}
					} else {
						paramMap.put("code",paramMap.getString("resultCode"));
					}
				}
			}
		 
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			//log.info("06.저장 완료 API END");
		}
		
		/* REQ_PRM_041 11번가 제휴OUT 딜 상품 상세설명 수정*/
		//alcoutDealgoodsDescModify(request, paramMap.getString("goodsCode"), paramMap.getString("paCode"), "PA11STDEAL");
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}	
	
	
	/**
	 * 판매중지처리
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "판매중지처리", notes = "판매중지처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-sell-stop", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsSellStop(HttpServletRequest request,
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "")	@RequestParam(value="goodsCode", required=true) String goodsCode,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 	@RequestParam(value="paCode", required=true) String paCode,			
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 		@RequestParam(value="procId", required=false, defaultValue="PA11") String procId)
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		Pa11stGoodsVO pa11stgoods = null;
	
		log.info("===== 판매중지처리 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PA11STAPI_01_008";
		String request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.판매중지대상 조회");
			paramMap.put("paSaleGb", "30");
			pa11stgoods = pa11stGoodsService.selectPa11stGoodsProductNo(paramMap);
			if(pa11stgoods == null){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			//log.info("04.11번가 api호출");
			if(pa11stgoods.getPaCode().equals(Constants.PA_11ST_BROAD_CODE)){
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"/"+pa11stgoods.getProductNo());
			} else {
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"/"+pa11stgoods.getProductNo());
			}
						
			doc = ComUtil.parseXML(conn.getInputStream());
			descNodes = doc.getElementsByTagName("ClientMessage");
			conn.disconnect();

			for(int j=0; j<descNodes.getLength();j++){
		        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
		        	paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
		        }
		    }
			log.info("result message : "+paramMap.getString("message"));
			
			//전송관리 테이블 저장
			paGoodsTransLog = new PaGoodsTransLog();
			paGoodsTransLog.setGoodsCode(pa11stgoods.getGoodsCode());
			paGoodsTransLog.setPaCode(pa11stgoods.getPaCode());
			paGoodsTransLog.setItemNo(pa11stgoods.getProductNo());
			paGoodsTransLog.setRtnCode(paramMap.getString("resultCode"));
			paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
			paGoodsTransLog.setSuccessYn(paramMap.getString("resultCode").equals("200")==true?"1":"0");
			paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paGoodsTransLog.setProcId("PA11");
			pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
		
			
			if(paramMap.getString("resultCode").equals("200")){
				pa11stgoods.setModifyId("PA11");
				pa11stgoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				pa11stgoods.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				pa11stgoods.setTransSaleYn("0");
				//log.info("05.동기화 완료 저장");
				rtnMsg = pa11stGoodsService.savePa11stGoodsSellTx(pa11stgoods);
				
				if(!rtnMsg.equals("000000")){
					paramMap.put("code","404");
					paramMap.put("message",rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} else {
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}
			} else if( paramMap.getString("message").replace(" ","").indexOf("상품만판매중지가가능합니다") >= 0 ){
				paramMap.put("goodsCode", pa11stgoods.getGoodsCode());
				paramMap.put("paCode", pa11stgoods.getPaCode());
				paramMap.put("transSaleYn", "0");
				pa11stGoodsService.updateTransSaleYn(paramMap);
				
				paramMap.put("code","200");
				paramMap.put("message","OK");
				log.info("===이미 중지된 상품 TRANS_SALE_YN = 0 UPDATE process===");
				pa11stGoodsService.saveStopMonitering(pa11stgoods);
			} else if (paramMap.getString("message").replace(" ","").indexOf("해당상품의정보를찾을수없습니다") >= 0){
				//상품정보 조회 불가. PA_STATUS = 90, RETURN_NOTE에 메세지 넣기.
				paramMap.put("goodsCode", pa11stgoods.getGoodsCode());
				paramMap.put("paCode", pa11stgoods.getPaCode());
				paramMap.put("transSaleYn", "0");
				paramMap.put("paSaleGb", "30");
				paramMap.put("paStatus", "90");
				
				pa11stGoodsService.updatePaStatus90(paramMap);
				
				paramMap.put("code","200");
				paramMap.put("message","OK");
				log.info("===상품정보 찾을수 없어 PA_STATUS 90으로 UPDATE process===");
			} else {
				paramMap.put("code",paramMap.getString("resultCode"));
			}
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			/*REQ_PRM_041 11번가 제휴OUT 딜 상품 옵션 수정 호출*/
			//딜 주석처리
			//alcoutDealGoodsdtModify(request, paramMap.getString("goodsCode"), paramMap.getString("paCode"), "PA11DEAL", "");
			
			//log.info("06.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 판매중지해제
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "판매중지해제", notes = "판매중지해제", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-sell-restart", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsSellRestart(HttpServletRequest request,
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "")	@RequestParam(value="goodsCode", required=true) String goodsCode,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 	@RequestParam(value="paCode", required=true) String paCode,			
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 		@RequestParam(value="procId", required=false, defaultValue="PA11") String procId)
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		Pa11stGoodsVO pa11stgoods = null;
		ResponseEntity<?> responseMsg = null;

		log.info("===== 판매중지해제 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PA11STAPI_01_009";
		String request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.판매중지해제대상 조회");
			paramMap.put("paSaleGb", "20");
			pa11stgoods = pa11stGoodsService.selectPa11stGoodsProductNo(paramMap);
			if(pa11stgoods == null){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			if(Integer.parseInt(pa11stgoods.getTransOrderAbleQty()) == 0){
				responseMsg = goodsStockModify(request, pa11stgoods.getGoodsCode(), pa11stgoods.getPaCode(), null);
				//log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
					if(PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
						paramMap.put("code","500");
					} else {
						paramMap.put("code","404");
					}
					paramMap.put("message",getMessage("errors.api.system")+" : goodsStockModify" + "("+PropertyUtils.describe(responseMsg.getBody()).get("message")+")");
					paramMap.put("resultCode", paramMap.getString("code"));
					paramMap.put("resultMessage", paramMap.getString("message"));
					systemService.insertApiTrackingTx(request,paramMap);
				} else {
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}
			}
			
			//log.info("04.11번가 api호출");
			if(pa11stgoods.getPaCode().equals(Constants.PA_11ST_BROAD_CODE)){
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"/"+pa11stgoods.getProductNo());
			} else {
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"/"+pa11stgoods.getProductNo());
			}
						
			doc = ComUtil.parseXML(conn.getInputStream());
			descNodes = doc.getElementsByTagName("ClientMessage");
			conn.disconnect();

			for(int j=0; j<descNodes.getLength();j++){
		        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
		        	paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
		        }
		    }
			log.info("result message : "+paramMap.getString("message"));
			
			//전송관리 테이블 저장
			paGoodsTransLog = new PaGoodsTransLog();
			paGoodsTransLog.setGoodsCode(pa11stgoods.getGoodsCode());
			paGoodsTransLog.setPaCode(pa11stgoods.getPaCode());
			paGoodsTransLog.setItemNo(pa11stgoods.getProductNo());
			paGoodsTransLog.setRtnCode(paramMap.getString("resultCode"));
			paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
			paGoodsTransLog.setSuccessYn(paramMap.getString("resultCode").equals("200")==true?"1":"0");
			paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paGoodsTransLog.setProcId("PA11");
			pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
		
			
			if(paramMap.getString("resultCode").equals("200")){
				pa11stgoods.setModifyId("PA11");
				pa11stgoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				pa11stgoods.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				pa11stgoods.setTransSaleYn("0");
				//log.info("05.동기화 완료 저장");
				rtnMsg = pa11stGoodsService.savePa11stGoodsSellTx(pa11stgoods);
				
				if(!rtnMsg.equals("000000")){
					paramMap.put("code","404");
					paramMap.put("message",rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} else {
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}
			} else if (paramMap.getString("message").replace(" ","").indexOf("해당상품의정보를찾을수없습니다") >= 0){
				//상품정보 조회 불가. PA_STATUS = 90, RETURN_NOTE에 메세지 넣기.
				paramMap.put("goodsCode", pa11stgoods.getGoodsCode());
				paramMap.put("paCode", pa11stgoods.getPaCode());
				paramMap.put("transSaleYn", "0");
				paramMap.put("paSaleGb", "30");
				paramMap.put("paStatus", "30");
				paramMap.put("transSaleYn", "1");
				pa11stGoodsService.updatePaStatus90(paramMap);
				
				paramMap.put("code","200");
				paramMap.put("message","OK");
				log.info("===상품정보 찾을수 없어 PA_STATUS 90으로 UPDATE process===");
			} else if ( paramMap.getString("message").indexOf("상품만 판매중지해제가 가능합니다.") > -1 ){
				paramMap.put("goodsCode", pa11stgoods.getGoodsCode());
				paramMap.put("paCode", pa11stgoods.getPaCode());
				paramMap.put("transSaleYn", "0");
				pa11stGoodsService.updateTransSaleYn(paramMap);
				
				paramMap.put("code","200");
				paramMap.put("message","OK");
				log.info("===이미 중지해제된 상품 TRANS_SALE_YN = 0 UPDATE process===");
				pa11stGoodsService.saveRestartMonitering(pa11stgoods);
			} else {
				paramMap.put("code",paramMap.getString("resultCode"));
			}
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			/*REQ_PRM_041 11번가 제휴OUT 딜 상품 옵션 수정 호출*/
			//딜 주석처리
			//alcoutDealGoodsdtModify(request, paramMap.getString("goodsCode"), paramMap.getString("paCode"), "PA11DEAL", "");
			//log.info("06.저장 완료 API END");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	
	/**
	 * 상품재고 동기화
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품재고 동기화", notes = "상품재고 동기화", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/legacy-goods-stock-sync", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsStockSync(HttpServletRequest request,
			@RequestParam(value="procId", required=false, defaultValue="PA11") String procId) throws Exception{
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		List<Pa11stGoodsdtMappingVO> paGoodsdtMappingList = null;
		Pa11stGoodsdtMappingVO paGoodsdtMapping = null;
		List<Pa11stGoodsVO> pa11stGoodsList = null;
		Pa11stGoodsVO pa11stGoods = null;

		ResponseEntity<?> responseMsg = null;

		log.info("===== 상품재고 동기화 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_01_010";
		
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			

			log.info("02.재고번호 조회 대상 추출");
			paGoodsdtMappingList = pa11stGoodsService.selectPa11stGoodsDtStockList();

			log.info("03.재고번호 조회 API호출 : /goods-stock-list");
			for(int i=0; i<paGoodsdtMappingList.size(); i++){
				paGoodsdtMapping = paGoodsdtMappingList.get(i);
				responseMsg = goodsStockList(request, paGoodsdtMapping.getGoodsCode(), paGoodsdtMapping.getProductNo(), paGoodsdtMapping.getPaCode(), procId, "0");
				//log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
					if(PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
						paramMap.put("code","500");
					} else {
						paramMap.put("code","404");
					}
					
					paramMap.put("message",getMessage("errors.api.system")+" : goodsStockList");
					paramMap.put("resultCode", paramMap.getString("code"));
					paramMap.put("resultMessage", paramMap.getString("message"));
					systemService.insertApiTrackingTx(request,paramMap);
				}
				
				/*REQ_PRM_041 11번가 제휴OUT 딜 상품 옵션 수정 호출*/
				//alcoutDealGoodsdtModify(request, paGoodsdtMapping.getGoodsCode(), paGoodsdtMapping.getPaCode(), "PA11DEAL", "");
			}
			
			pa11stGoodsList = pa11stGoodsService.selectedSoldOutPa11stGoodsList();
			for(int j=0; j<pa11stGoodsList.size(); j++){
				pa11stGoods = pa11stGoodsList.get(j);
				log.info("품절대상 pa_sale_gb update");
				pa11stGoodsService.updateSoldOutTransSaleYn(pa11stGoods);
				responseMsg= goodsSellStop(request, pa11stGoods.getGoodsCode(), pa11stGoods.getPaCode(), procId);
				//log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
					if(PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
						paramMap.put("code","500");
					} else {
						paramMap.put("code","404");
					}
					
					paramMap.put("message",getMessage("errors.api.system")+" : goodsSellStop");
					paramMap.put("resultCode", paramMap.getString("code"));
					paramMap.put("resultMessage", paramMap.getString("message"));
					systemService.insertApiTrackingTx(request,paramMap);
				} else {
					pa11stGoods.setModifyId(procId);
					pa11stGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					rtnMsg = pa11stGoodsService.savePa11stGoodsdtMappingQtyTx(pa11stGoods);
					if(!rtnMsg.equals("000000")){
						paramMap.put("code","500");
						paramMap.put("message", rtnMsg);
						paramMap.put("resultCode", paramMap.getString("code"));
						paramMap.put("resultMessage", paramMap.getString("message"));
						systemService.insertApiTrackingTx(request,paramMap);
					} else {
						paramMap.put("code","200");
						paramMap.put("message","OK");
					}
				}
				/*REQ_PRM_041 11번가 제휴OUT 딜 상품 옵션 수정 호출*/
				//alcoutDealGoodsdtModify(request, pa11stGoods.getGoodsCode(), pa11stGoods.getPaCode(), "PA11DEAL", "");
			}
			
			responseMsg = goodsStockModify(request, null, null, null);
			//log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
				if(PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
					paramMap.put("code","500");
				} else {
					paramMap.put("code","404");
				}
				
				paramMap.put("message",getMessage("errors.api.system")+" : goodsStockModify" + "("+PropertyUtils.describe(responseMsg.getBody()).get("message")+")");
				
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			} else {
				paramMap.put("code","200");
				paramMap.put("message","OK");
			}
			
			paMoniteringStockList(request, null);
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			//log.info("06.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 * 상품정보 동기화
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품정보 동기화", notes = "상품정보 동기화", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-info-sync", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsInfoSync(HttpServletRequest request,
			@RequestParam(value="goodsCode", required=false) String goodsCode,
			@RequestParam(value="procId", required=false, defaultValue="PA11") String procId) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String,Object> resultMap = null; 

		try {
			log.info("/goods-info-sync");
			paramMap.put("proc_id", procId);
			paramMap.put("goods_code", goodsCode);
			resultMap = pa11stGoodsService.procPa11stGoodsSync(paramMap);
		} catch (Exception e) {
			resultMap.put("out_code", "500");
		}
				
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(resultMap.get("out_code").toString(), resultMap.get("out_msg").toString()), HttpStatus.OK);
	}
	
	/**
	 * 재고번호매칭 실패 단품 재고번호 조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "재고번호매칭 실패 단품 재고번호 조회", notes = "재고번호매칭 실패 단품 재고번호 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/pa-moniter-stock-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> paMoniteringStockList(HttpServletRequest request,
			@RequestParam(value="procId", required=false, defaultValue="PA11") String procId) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn 	= null;
		Document doc 			= null;
		NodeList descNodes 		= null;
		StringBuilder reqXml 	= null;
		OutputStreamWriter out 	= null;
		int respCode 			= 0;
		String respMsg 			= null;
		String goodsCode 		= "";
		String productNo 		= "";
		String paCode 			= "";	
		
		String dateTime 		= "";
		String duplicateCheck 	= "";
		PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
		List<Pa11stGoodsdtMappingVO> orderMoniterringList = new ArrayList<Pa11stGoodsdtMappingVO>();
		Pa11stGoodsdtMappingVO 		 orderGoodsdtMapping = null;
		
		List<Pa11stGoodsdtMappingVO> paGoodsdtMappingList = new ArrayList<Pa11stGoodsdtMappingVO>();		
	
		log.info("===== 주문불가분재고번호 조회 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_01_006";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try{
//			//log.info("02.API 중복실행검사"); 중복실행안함 기본 API와 별도로 체크하지 않음
//			//= 중복 실행 Check
//			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
//			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			orderMoniterringList = pa11stGoodsService.selectPa11stOrderMappingList(paramMap);
			
			if(orderMoniterringList.size() > 0){
				for(int i=0;i<orderMoniterringList.size();i++){
					orderGoodsdtMapping = orderMoniterringList.get(i);
					goodsCode 		= orderGoodsdtMapping.getGoodsCode();
					productNo		= orderGoodsdtMapping.getProductNo();
					paCode 			= orderGoodsdtMapping.getPaCode();	
					paramMap.put("productNo", 	productNo);
					paramMap.put("paCode", 	  	paCode);
					paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					//log.info("02.11번가 API호출");
					if(paCode.equals(Constants.PA_11ST_BROAD_CODE)){
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"");
					} else {
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"");
					}

					reqXml = new StringBuilder();
					reqXml.append("<ProductStocks>");
					reqXml.append("<ProductStock>");
					reqXml.append("<prdNo>"+productNo+"</prdNo>");	
					reqXml.append("</ProductStock>");
					reqXml.append("</ProductStocks>");
					
					if(reqXml != null){
						out = new OutputStreamWriter(conn.getOutputStream());
						out.write(String.valueOf(reqXml));
						out.flush();

						respCode = conn.getResponseCode();
						respMsg  = conn.getResponseMessage();
						log.info(" connect respCode : "+respCode);
						log.info(" connect respMsg  : "+respMsg);
						if(respCode == 200){
							// RESPONSE XML 			
							doc = ComUtil.parseXML(conn.getInputStream());
							descNodes = doc.getElementsByTagName("ns2:ProductStockss");
							conn.disconnect();
							
							List<ParamMap> productStockList = new ArrayList<ParamMap>();

							for(int j=0; j<descNodes.getLength();j++){
						        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
					            	if(node.getNodeName().trim().equals("ns2:ProductStocks")){
				            			for(int x=0; x<node.getChildNodes().getLength(); x++){
						            		if(node.getChildNodes().item(x).getNodeName().trim().equals("ns2:ProductStock")){
							            		ParamMap pa11stParamMap = new ParamMap();
							            		Node pa11stList = node.getChildNodes().item(x);
							            		for(Node pa11st = pa11stList.getFirstChild(); pa11st!=null; pa11st=pa11st.getNextSibling()){ 
							            			pa11stParamMap.put(pa11st.getNodeName().trim(), pa11st.getTextContent().trim());
							            		}
							            		productStockList.add(pa11stParamMap);
					            			}
					            		}
					            	} else {
					            		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
						        	}
						        }
						    }
							log.info("result message : "+paramMap.getString("ns2:message"));
							if(productStockList.size() > 0){
								for(int y=0; y<productStockList.size(); y++){
									Pa11stGoodsdtMappingVO paGoodsdtMapping = new Pa11stGoodsdtMappingVO();
									paGoodsdtMapping.setGoodsCode(goodsCode);
									paGoodsdtMapping.setPaCode(paCode);
									paGoodsdtMapping.setSellerStockCd(productStockList.get(y).getString("sellerStockCd"));
									paGoodsdtMapping.setPaOptionCode(productStockList.get(y).getString("prdStckNo"));
									paGoodsdtMapping.setModifyId("PA11");
									paGoodsdtMapping.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
									paGoodsdtMappingList.add(paGoodsdtMapping);
								}
								paGoodsTransLog.setRtnCode("200");
								paGoodsTransLog.setSuccessYn("1");
								paramMap.put("message", "OK");
								paramMap.put("code", "200");

							} else {
								paGoodsTransLog.setRtnCode("404");
								paGoodsTransLog.setSuccessYn("0");
								paramMap.put("message", paramMap.getString("ns2:message"));
								paramMap.put("code", "404");
							}
							//전송관리 테이블 저장 등록안함
//							paGoodsTransLog.setGoodsCode(goodsCode);
//							paGoodsTransLog.setPaCode(paCode);
//							paGoodsTransLog.setItemNo(productNo);
//							paGoodsTransLog.setRtnMsg(paramMap.getString("ns2:message"));
//							paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
//							paGoodsTransLog.setProcId("PA11");
//							pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
						
							/* 테이블 존재하지 않음
							if(paGoodsdtMappingList.size()>0){
								log.info("03.재고번호(TPAMONITERINGINFO) 저장");
								rtnMsg = pa11stGoodsService.savePa11stGoodsdtMappingOrderTx(paGoodsdtMappingList);
								if(!rtnMsg.equals("000000")){
									paramMap.put("code","404");
									paramMap.put("message",rtnMsg);
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								} else {
									paramMap.put("code","200");
									paramMap.put("message","OK");
								}
							}
							*/
						}
					}
				}
			}	
			
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				//systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error1 : "+e.getMessage());
			}
//			if(duplicateCheck.equals("0")){
//				systemService.checkCloseHistoryTx("end", prg_id);
//			}
			
			//log.info("04.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/** 판매중지처리/message처리 */
	/*async로 옮김
	public void savePaGoodsModifyFail(ParamMap paramMap) throws Exception{
		Pa11stGoodsVO pa11stGoods = new Pa11stGoodsVO();
		pa11stGoods.setPaGroupCode("01");
		pa11stGoods.setGoodsCode(paramMap.getString("goodsCode"));
		pa11stGoods.setPaCode(paramMap.getString("paCode"));
		pa11stGoods.setPaSaleGb("30");
		pa11stGoods.setReturnNote(paramMap.getString("message"));
		pa11stGoods.setProductNo(paramMap.getString("productNo"));
		pa11stGoodsService.savePaGoodsModifyFailTx(pa11stGoods);
	}
	
	
	private void insertPaRequestMap(ParamMap param) throws Exception{
		PaRequestMap paRequestMap = new PaRequestMap();
		paRequestMap.setPaCode(param.getString("paCode"));
		paRequestMap.setReqApiCode(param.getString("apiCode"));
		paRequestMap.setReqUrl(param.getString("url"));
		paRequestMap.setReqHeader(param.getString("header")+"");
		paRequestMap.setRequestMap(param.getString("body"));
		paRequestMap.setResponseMap(param.getString("result"));
		paRequestMap.setRemark(param.getString("responseTime"));
		systemService.insertPaRequestMapTx(paRequestMap);
	}
	*/
	
	/**
	 * 상품 VOD URL 연동
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 VOD URL 연동", notes = "상품 VOD URL 연동", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-vod-url", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsVodUrl(HttpServletRequest request,
			@RequestParam(value="procId", required=false, defaultValue="PA11") String procId) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn 	= null;
		Document doc 			= null;
		NodeList descNodes 		= null;
		StringBuilder reqXml 	= null;
		OutputStreamWriter out 	= null;
		int respCode 			= 0;
		String respMsg 			= null;
		String dateTime 		= "";
		String startTime		= systemService.getSysdatetimeToString();
		int executeCnt = 0;
		List<Pa11stGoodsVO> pickcastList = new ArrayList<Pa11stGoodsVO>();
		Pa11stGoodsVO pickcast = null;
		int targetCount = 0;
		int procCount = 0;
		String msg = "";
		
		log.info("===== 상품 VOD URL 연동 Start=====");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_06_001";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		
		Timestamp sysdate = DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss");
		
		try{
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			try {
				
				pa11stGoodsService.saveTpagoodsVodUrlTx(pickcast);
			
			} catch(Exception e) {
				log.error(paramMap.getString("message"), e);
				
			}
			
			List<Pa11stGoodsVO> pa11stGoodsVodTransList = new ArrayList<Pa11stGoodsVO>();
			pa11stGoodsVodTransList = pa11stGoodsService.selectPa11stVodUrlTransList();
			Pa11stGoodsVO pa11stGoodsVodTrans = null;
			targetCount = pa11stGoodsVodTransList.size();
			if(pa11stGoodsVodTransList.size() > 0){
				for(int i=0;i<pa11stGoodsVodTransList.size();i++){
					try {
						String goodsCode = "";
						String vodUrl 	 = "";
						String productNo = "";
						String paCode 	 = "";	
						
						pa11stGoodsVodTrans = pa11stGoodsVodTransList.get(i);
						pa11stGoodsVodTrans.setApplyDate(sysdate);
						
						goodsCode		= pa11stGoodsVodTrans.getGoodsCode();
						productNo		= pa11stGoodsVodTrans.getProductNo();
						paCode 			= pa11stGoodsVodTrans.getPaCode();
						vodUrl			= pa11stGoodsVodTrans.getVodUrl();
						
						paramMap.put("goodsCode", 	goodsCode);
						paramMap.put("productNo", 	productNo);
						paramMap.put("paCode", 	  	paCode);
						paramMap.put("vodUrl", 		vodUrl);
						paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						if(paCode.equals(Constants.PA_11ST_BROAD_CODE)){
							conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"");
						} else {
							conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"");
						}
						
						reqXml = new StringBuilder();
						reqXml.append("<ProductMovie>");
						reqXml.append("<prdNo>"+productNo+"</prdNo>");	
						reqXml.append("<movieUrl>"+vodUrl+"</movieUrl>");	
						reqXml.append("</ProductMovie>");
						
						if(reqXml != null){
							out = new OutputStreamWriter(conn.getOutputStream());
							out.write(String.valueOf(reqXml));
							out.flush();
	
							respCode = conn.getResponseCode();
							respMsg  = conn.getResponseMessage();
							
							paramMap.put("respCode", respCode);
							paramMap.put("respMsg", respMsg);
							
							// RESPONSE XML 			
							doc = ComUtil.parseXML(conn.getInputStream()); //read
						    descNodes = doc.getElementsByTagName("Result");
						    conn.disconnect();
						    
						    for(int j=0; j<descNodes.getLength();j++){
						        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
					        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
						        }
							}
						    
							if(paramMap.getString("resultCode").equals("200")){
								// update trans_target = '0'
								procCount++;
								pa11stGoodsService.updatePaGoodsVodUrlTransYn(pa11stGoodsVodTrans);
							} else {
								pa11stGoodsService.updatePaGoodsVodUrlFailMsg(paramMap);
							}
						}
					}catch(Exception e) {
						log.error(e.getMessage());
					}
				}	
			}	
		}catch (Exception e) {
			paramMap.put("code","400");
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				if(!paramMap.getString("code").equals("400")){
					msg = "대상건수:" + targetCount + ", 성공건수:" + procCount;
					paramMap.put("message", msg);
				}
				if(targetCount == procCount){
					paramMap.put("code","200");
				} else {
					paramMap.put("code","500");
				}
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("startDate", startTime);
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error1 : "+e.getMessage());
			}
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/* 제휴OUT 딜 START*/
	/* REQ_PRM_041 11번가 제휴OUT 딜 상품 등록 */
	@ApiOperation(value = "REQ_PRM_041 11번가 제휴OUT 딜 상품 등록", notes = "REQ_PRM_041 11번가 제휴OUT 딜 상품 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goods-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealGoodsInsert(HttpServletRequest request,
		@ApiParam(name = "alcoutDealCode", value = "제휴OUT딜코드", defaultValue = "") @RequestParam(value="alcoutDealCode", required=true) String alcoutDealCode,
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") 			@RequestParam(value="goodsCode", required=true) String goodsCode,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 			@RequestParam(value="paCode", required=true) String paCode,
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 			@RequestParam(value="procId", required=false, defaultValue="PA11DEAL") String procId) 
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HashMap<String, String> policyMap = new HashMap<String, String>(); //발송 예정일 템플릿 용
		String dateTime = "";
		String duplicateCheck = "";
		String rtnMsg = Constants.SAVE_SUCCESS;

		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		OutputStreamWriter out = null;
		StringBuilder reqXml = null;

		ResponseEntity<?> responseMsg = null;
		Pa11stGoodsVO pa11stGoods = null;
		List<PaGoodsOffer> pa11stGoodsOffer = null;
		List<PaGoodsPriceApply> paGoodsPriceApplyList = null;
		PaGoodsTransLog paGoodsTransLog = null;
		String checkSaleGb = "";
		int respCode = 0;
		String respMsg = null;
		double timeS = 0;
		double timeE = 0;
		String responseTime = null;
		
		HashMap<String, Object> pa11stAlcoutDealInfo = new HashMap<String, Object>();
		List<HashMap<String, Object>> pa11stAlcoutDealGoodsdtList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> alcoutDealgoodsdtInfoMap = new HashMap<String, Object>();
		
		List<Pa11stGoodsVO> ps11stGoodsDescribeList = null; // 수정대상 기술서 목록
		
		log.info("===== 제휴OUT딜 상품등록 API Start =====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_01_011";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		paramMap.put("modCase", "INSERT");
		
		try{
			log.info("제휴OUT딜코드 : "	+ alcoutDealCode);
			log.info("대표상품코드 : "	+ goodsCode);
			log.info("제휴사코드: "	+ paCode);
			log.info("처리자ID : "	+ procId.toUpperCase());
			
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			//log.info("03.SK스토아 TV쇼핑 상품정보 Select");
			
			paramMap.put("alcoutDealCode", alcoutDealCode);
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("url", ConfigUtil.getString("PA11ST_COM_BASE_URL") + apiInfo.get("API_URL"));
			paramMap.put("header", "Content-Type=" + apiInfo.get("contentType"));
			
			/* 출고지/반품/회수지/출고지별 배송지는 상품이 연됭됐다는 가정하에 전송 x
			//상품 출고지, 회수지 담당자 주소 체크
			entpSlip = pa11stGoodsService.selectPa11stEntpSlip(paramMap);
			if(entpSlip != null){
				//출고지 등록
				responseMsg= pa11stCommonController.entpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaCode());
				//log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
					paramMap.put("code","440");
					paramMap.put("message",getMessage("pa.check_entp_slip_man_seq", new String[] {" 업체코드 : " +  entpSlip.getEntpCode()}));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}
			
			returnSlip = pa11stGoodsService.selectPa11stReturnSlip(paramMap);
			if(returnSlip != null){
				//반품/회수지 등록
				responseMsg= pa11stCommonController.returnSlipInsert(request, returnSlip.getEntpCode(), returnSlip.getEntpManSeq(), returnSlip.getPaCode());
				//log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
					paramMap.put("code","440");
					paramMap.put("message",getMessage("pa.check_return_slip_man_seq", new String[] {" 업체코드 : " +  returnSlip.getEntpCode()}));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}
			
			//출고지별 배송비 추가/수정 (2018.10.15 ,thjeon)
			pa11stCommonController.entpSlipCostInsert(request, null);
	        */
			
			//제휴OUT 딜정보 조회
			pa11stAlcoutDealInfo = pa11stGoodsService.selectPa11stAlcoutDealInfo(paramMap);
			
			//제휴OUT 딜대표 상품정보 조회
			pa11stGoods = pa11stGoodsService.selectPa11stAlcoutDealGoodsInfo(paramMap);
			
			ps11stGoodsDescribeList = pa11stGoodsService.selectPa11stAlcoutDealGoodsDescribe(paramMap); //기술서 조회
			
			if(pa11stGoods == null){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			if(pa11stGoods.getProductNo()!=null){
				paramMap.put("code","410");
				paramMap.put("message",getMessage("pa.already_insert_goods", new String[] {"11번가 상품코드 : " + pa11stGoods.getProductNo()}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			String strDescribeExt = "";
			long describeLength = 0;
			
			/* 기술서 통합 */
			for(Pa11stGoodsVO ps11stGoodsDescribe : ps11stGoodsDescribeList) {
				if(!("").equals(ps11stGoodsDescribe.getDescribeExt()) && ps11stGoodsDescribe.getDescribeExt() != null) {
					strDescribeExt += "<b><p style=\"font-size:15px\">" + ps11stGoodsDescribe.getGoodsName() + "</p></b>" + "<br />" + ps11stGoodsDescribe.getDescribeExt() + "<br />";
					describeLength += ps11stGoodsDescribe.getDescribeLen();
				}
			}
			
			if("".equals(pa11stGoods.getCollectImage()) || pa11stGoods.getCollectImage() == null) {
				pa11stGoods.setDescribeExt("<div align='center'><img alt='' src='" + pa11stGoods.getTopImage() + "' /><br /><br /><br />" + strDescribeExt.replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + pa11stGoods.getBottomImage() + "' /></div>");				
			}else {
				pa11stGoods.setDescribeExt("<div align='center'><img alt='' src='" + pa11stGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + pa11stGoods.getCollectImage() + "' /><br /><br /><br />" + strDescribeExt.replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + pa11stGoods.getBottomImage() + "' /></div>");
			}
			
			if(describeLength > 6000){
				paramMap.put("code","415");
				paramMap.put("message","상품 기술서 길이가 6000자가 넘습니다. 상품코드 : " + goodsCode);
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			if(pa11stGoods.getDescribeExt().equals("")){
				paramMap.put("code","420");
				paramMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			/*int shipCostCnt = 0;
			shipCostCnt = pa11stGoodsService.selectPa11stGoodsShipCnt(goodsCode);
			
			if(shipCostCnt < 1){
				paramMap.put("code","450");
				paramMap.put("message",getMessage("pa.error_ship_cost", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			} */
			
			//제휴OUT 딜 상품/단품조회
			pa11stAlcoutDealGoodsdtList = pa11stGoodsService.selectPa11stAlcoutDealGoodsdtInfoList(paramMap);
			
			if(pa11stAlcoutDealGoodsdtList == null){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			paramMap.put("paGroupCode", pa11stGoods.getPaGroupCode());
			
			//제휴OUT 딜 대표상품 고시정보 조회
			pa11stGoodsOffer = pa11stGoodsService.selectPa11stGoodsOfferList(paramMap);
			
			if(pa11stGoodsOffer.size() < 1){
				paramMap.put("code","430");
				paramMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}

			// 출고지배송비정책 설정
			String shipCostFlag = pa11stGoods.getShipCostCode().substring(0, 2);
			if (shipCostFlag.equals(ShipCostFlag.BASEAMT.code())
					|| shipCostFlag.equals(ShipCostFlag.BASEAMT_CODE.code())) {
				String cnPaAddrSeq = shipCostMapper.getCnPaAddrSeq(pa11stGoods.getGoodsCode(), pa11stGoods.getPaCode());
				if (cnPaAddrSeq == null) {
					paramMap.put("code","404");
					paramMap.put("message","출고지배송비정책 연동을 선행하세요.");
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} 
				pa11stGoods.setAddrSeqOut(cnPaAddrSeq);
			}
			
			//상품 발송 예정일 템플릿 조회
			/* policyMap = pa11stGoodsService.selectGoodsFor11stPolicy(pa11stGoods);
			if(policyMap == null){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list", new String[] {"상품 발송 예정일 템플릿 API 상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			} */
			
			//제휴OUT 딜 상품 즉시할인쿠폰 프로모션
			//paPromoTargetList = paPromoTargetAlcoutDealGoodsInfoSetting(paramMap);
			paGoodsPriceApplyList = paPromoTargetAlcoutDealGoodsInfoSetting(pa11stAlcoutDealGoodsdtList);			
			
			log.info("03.상품등록 API 호출");
			if(paCode.equals(Constants.PA_11ST_BROAD_CODE)){
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"");
			} else {
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"");
			}

			alcoutDealgoodsdtInfoMap = pa11stAlcoutDealGoodsInfoSetting(pa11stAlcoutDealInfo, pa11stGoods, pa11stAlcoutDealGoodsdtList, pa11stGoodsOffer, paGoodsPriceApplyList);
			
			reqXml = (StringBuilder) alcoutDealgoodsdtInfoMap.get("reqXml");
			
			List<String> optValGoodslist = new ArrayList<>(); 
			
			optValGoodslist = (List<String>) alcoutDealgoodsdtInfoMap.get("optValGoodslist");
			
			//log.info("11번가로 전달한 전문[등록]"); /* 향후 삭제 필요 */
		    if(reqXml != null){
		    	//log.info(String.valueOf(reqXml));
		    	paramMap.put("body", String.valueOf(reqXml));
		    }

			if(reqXml != null){
				//log.info("04.11번가 API 호출");
				timeS = System.nanoTime();
				out = new OutputStreamWriter(conn.getOutputStream());
				out.write(String.valueOf(reqXml));
				out.flush();

				respCode = conn.getResponseCode();
				respMsg  = conn.getResponseMessage();
				log.info(" connect respCode : "+respCode);
				log.info(" connect respMsg  : "+respMsg);
				log.info(" connect respMsg  : "+conn);
				
				timeE = System.nanoTime();
				responseTime = Double.toString((timeE-timeS)/1000000000);
				paramMap.put("responseTime", responseTime);
				paramMap.put("result", respMsg);
				
				pa11stAsyncController.insertPaRequestMap(paramMap);
				
				if(respCode == 200){
					// RESPONSE XML 		
					doc = ComUtil.parseXML(conn.getInputStream());
					descNodes = doc.getElementsByTagName("ClientMessage");
					conn.disconnect();

					for(int j=0; j<descNodes.getLength();j++){
				        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
				        	paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
				        }
				    }
					
					log.info("result message : "+paramMap.getString("message"));

					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paramMap.getString("goodsCode"));
					paGoodsTransLog.setPaCode(paramMap.getString("paCode"));
					paGoodsTransLog.setItemNo(paramMap.getString("productNo").isEmpty()?paramMap.getString("goodsCode"):paramMap.getString("productNo"));
					paGoodsTransLog.setRtnCode(paramMap.getString("resultCode"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn(paramMap.getString("resultCode").equals("200")==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId("PA11DEAL");
					pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
					
					if(paramMap.getString("resultCode").equals("200")){
						
						pa11stGoods.setPaStatus("30");//입점완료
						pa11stGoods.setProductNo(paramMap.getString("productNo"));
						pa11stGoods.setReturnNote("");
						pa11stGoods.setInsertId("PA11DEAL");
						pa11stGoods.setModifyId("PA11DEAL");
						pa11stGoods.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						pa11stGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						pa11stAlcoutDealInfo.put("alcoutDealCode", paramMap.getString("alcoutDealCode"));
						pa11stAlcoutDealInfo.put("paGoodsCode", 	 paramMap.getString("productNo"));
						pa11stAlcoutDealInfo.put("paCode"		, 	 paramMap.getString("paCode"));
						pa11stAlcoutDealInfo.put("paStatus"	, "30");// 입점완료
						pa11stAlcoutDealInfo.put("modifyId"	, 	 "PA11DEAL");
						pa11stAlcoutDealInfo.put("modifyDate"	, DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						log.info("05.제휴사 상품정보 저장");
						// 제휴사 제휴OUT 딜코드 저장
						rtnMsg = pa11stGoodsService.savePa11stAlcoutDealTx(pa11stAlcoutDealInfo);
						
						if(optValGoodslist.size() > 0) {
							for(String otpValgoodsCode : optValGoodslist){
								pa11stAlcoutDealInfo.put("goodsCode", otpValgoodsCode);
								pa11stAlcoutDealInfo.put("paStatus", "20"); //입점반려
								pa11stAlcoutDealInfo.put("returnNote",  "판매가 오류");
								pa11stAlcoutDealInfo.put("modifyId"	, "PA11DEAL");
								pa11stAlcoutDealInfo.put("modifyDate"	, DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								
								log.info("07.제휴사 실패정보 저장");
								pa11stGoodsService.savePa11stAlcoutDealGoodsFailTx(pa11stAlcoutDealInfo);
							}
						}
						
						if(!rtnMsg.equals("000000")){
							paramMap.put("code","404");
							paramMap.put("message",rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paramMap.put("code","200");
							paramMap.put("message","OK");
						}
						
						responseMsg = null;
						log.info("06.재고번호 조회 API호출 : /goods-stock-list");
						// 제휴OUT 딜 상품+단품 재고번호 조회
						responseMsg = alcoutDealGoodsStockList(request, pa11stAlcoutDealInfo.get("ALCOUT_DEAL_CODE").toString(), pa11stGoods.getGoodsCode(), pa11stGoods.getProductNo(), pa11stGoods.getPaCode(), procId);
						
						if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
							paramMap.put("code","500");
							paramMap.put("message",getMessage("errors.api.system")+" : goodsStockList");
							log.error("11번가 상품등록 재고번호 호출 에러");
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						}
						
					} else {
						paramMap.put("code",paramMap.getString("resultCode"));
					}
				} else {
					paramMap.put("code",respCode);
					paramMap.put("message",respMsg);
				}
			}
			
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				if (paramMap.get("code").equals("490")) {
					log.error("중복 실행 중입니다.");
				} else if(paramMap.getString("message").indexOf("출고지 조건부 배송비가 해당 출고지에 설정되어있지 않습니다.") > -1){
					pa11stGoodsService.insertCnCostMoniteringTx(pa11stGoods);
					pa11stGoodsService.updateCnShipCostByMoniteringTx(pa11stGoods);
					log.info("출고지 조건부 배송비 process 입점반려 미처리");
				} else if(paramMap.get("code").equals("440")){
					pa11stAlcoutDealInfo.put("paStatus", "20"); //입점반려
					pa11stAlcoutDealInfo.put("returnNote", "주소정제에 실패했습니다.");
					pa11stAlcoutDealInfo.put("modifyId"	, "PA11DEAL");
					pa11stAlcoutDealInfo.put("modifyDate"	, DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					
					
					//log.info("07.제휴사 실패정보 저장");
					pa11stGoodsService.savePa11stAlcoutDealGoodsFailTx(pa11stAlcoutDealInfo);
				} else if(!paramMap.get("code").equals("200")){
					paramMap.put("message", paramMap.getString("message").length() > 999 ? paramMap.getString("message").substring(0, 999) : paramMap.getString("message"));
					if(paramMap.getString("message").replace(" ","").indexOf("goodsStockList") >= 0) {
						//이미 입점 되었고 재고 처리 하는 부분에서 에러난 케이스.
						//상품 수정을 통해 처리하면 된다.
						pa11stAlcoutDealInfo.put("paStatus", "20");
					}else {
						pa11stAlcoutDealInfo.put("paStatus", "20"); //입점반려
					}
					pa11stAlcoutDealInfo.put("returnNote",  paramMap.getString("message"));
					pa11stAlcoutDealInfo.put("modifyId"	, "PA11DEAL");
					pa11stAlcoutDealInfo.put("modifyDate"	, DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					//log.info("07.제휴사 실패정보 저장");
					pa11stGoodsService.savePa11stAlcoutDealGoodsFailTx(pa11stAlcoutDealInfo);
				}
				paramMap.put("resultMessage", paramMap.getString("message"));
				
				log.info(paramMap.get().toString());
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/* REQ_PRM_041 11번가 제휴OUT 딜 상품 등록 전문 세팅 */
	private HashMap<String, Object> pa11stAlcoutDealGoodsInfoSetting(HashMap<String, Object> pa11stAlcoutDealInfo, Pa11stGoodsVO pa11stGoods, List<HashMap<String, Object>> pa11stAlcoutDealGoodsdtList, List<PaGoodsOffer> pa11stGoodsOffer, List<PaGoodsPriceApply> paGoodsPriceApply) throws Exception{
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		StringBuilder reqXml = new StringBuilder();
		List<String> optValGoodslist = new ArrayList<>();
		//운영 FLEX_IMG_SERVER_URL : //1.255.85.245/
		//개발 FLEX_IMG_SERVER_URL : http://1.255.85.245/
		//config : PARTNER_API_IMAGE_PROTOCOL 운영 Y, 개발 N
		Config imageUrl = systemService.getConfig("FLEX_IMG_SERVER_URL");
		Config protocol = systemService.getConfig("PARTNER_API_IMAGE_PROTOCOL");
		String http = null;
		if(protocol.getVal().equals("Y")){
			http ="http:";
		}else{
			http ="";
		}
		
		reqXml.append("<Product>");
		
		reqXml.append("<selMthdCd>"+"01"+"</selMthdCd>"); //판매방식 01:고정가판매
		reqXml.append("<dispCtgrNo>"+pa11stGoods.getPaLmsdKey()+"</dispCtgrNo>"); //카테고리번호
		reqXml.append("<prdTypCd>"+"01"+"</prdTypCd>"); //서비스 상품 코드 01:일반배송상품
		
		reqXml.append("<prdNm><![CDATA["+ComUtil.subStringBytes(pa11stAlcoutDealInfo.get("ALCOUT_DEAL_NM").toString(), 99)+"]]></prdNm>"); //제휴OUT 딜 명 (사이즈 변경 100byte 이하)
		reqXml.append("<brand><![CDATA["+pa11stGoods.getBrandName()+"]]></brand>"); //브랜드명
		
		/** 브랜드 신규 개발*/
		if (pa11stGoods.getBrandNo() != null && !pa11stGoods.getBrandNo().equals("")) {
			reqXml.append("<apiPrdAttrBrandCd>" + pa11stGoods.getBrandNo() + "</apiPrdAttrBrandCd>");
		}
		reqXml.append("<orgnTypCd>"+pa11stGoods.getOrgnTypCd()+"</orgnTypCd>"); //원산지코드
		reqXml.append("<orgnTypDtlsCd>"+pa11stGoods.getOrgnTypDtlsCd()+"</orgnTypDtlsCd>"); //원산지지역코드
		reqXml.append("<orgnNmVal>"+pa11stGoods.getOrgnNmVal()+"</orgnNmVal>"); //원산지명


		if(pa11stGoods.getTaxYn().equals("1")){//부가세,면세
			reqXml.append("<suplDtyfrPrdClfCd>"+"01"+"</suplDtyfrPrdClfCd>"); //01:과세
		} else {
			if(pa11stGoods.getTaxSmallYn().equals("1")){
				reqXml.append("<suplDtyfrPrdClfCd>"+"03"+"</suplDtyfrPrdClfCd>"); //03:영세
			} else {
				reqXml.append("<suplDtyfrPrdClfCd>"+"02"+"</suplDtyfrPrdClfCd>"); //02:면세
			}
		}
		
		reqXml.append("<prdStatCd>"+"01"+"</prdStatCd>");//상품상태(01:새상품)
		reqXml.append(pa11stGoods.getAdultYn()=="1"?"<minorSelCnYn>"+"N"+"</minorSelCnYn>":"<minorSelCnYn>"+"Y"+"</minorSelCnYn>");//미성년자 구매가능
		reqXml.append("<prdImage01>"+http+imageUrl.getVal()+pa11stGoods.getImageUrl()+pa11stGoods.getImageP()+"?dc="+System.currentTimeMillis()+"</prdImage01>");
		log.info("이미지 경로 ::::   "+http+imageUrl.getVal()+pa11stGoods.getImageUrl()+pa11stGoods.getImageP()+"?dc="+System.currentTimeMillis());
		
		if(pa11stGoods.getImageAP()!=null){
			reqXml.append("<prdImage02>"+http+imageUrl.getVal()+pa11stGoods.getImageUrl()+pa11stGoods.getImageAP()+"?dc="+System.currentTimeMillis()+"</prdImage02>");
		}else{
			reqXml.append("<prdImage02></prdImage02>");
		}
		
		if(pa11stGoods.getImageBP()!=null){
			reqXml.append("<prdImage03>"+http+imageUrl.getVal()+pa11stGoods.getImageUrl()+pa11stGoods.getImageBP()+"?dc="+System.currentTimeMillis()+"</prdImage03>");
		}else{
			reqXml.append("<prdImage03></prdImage03>");
		}
		
		if(pa11stGoods.getImageCP()!=null){
			reqXml.append("<prdImage04>"+http+imageUrl.getVal()+pa11stGoods.getImageUrl()+pa11stGoods.getImageCP()+"?dc="+System.currentTimeMillis()+"</prdImage04>");
		}else{
			reqXml.append("<prdImage04></prdImage04>");
		}
		
		//yekim 20.03.19 목록이미지 prdImage01(대표 이미지 URL)과 동일하게. 
		reqXml.append("<prdImage05>"+http+imageUrl.getVal()+pa11stGoods.getImageUrl()+pa11stGoods.getImageP()+"?dc="+System.currentTimeMillis()+"</prdImage05>");
		/*if(pa11stGoods.getImageDP()!=null)
			reqXml.append("<prdImage05>"+"image.skstoa.com"+pa11stGoods.getImageUrl()+pa11stGoods.getImageDP()+"?dc="+System.currentTimeMillis()+"</prdImage05>");*/
		reqXml.append("<htmlDetail>"+"<![CDATA["+pa11stGoods.getDescribeExt()+"]]>"+"</htmlDetail>");
		if(pa11stGoods.getCertYn().equals("1")){//인증필수
			//crtfGrpTypCd 인증정보그룹→ 01 : 전기용품/생활용품 KC인증→ 02 : 어린이제품 KC인증→ 03 : 방송통신기자재 KC인증→ 04 : 위해우려제품 모두 써줘야함
			//crtfGrpObjClfCd 인증정보그룹→ 01 : KC인증대상→ 02 : KC면제대상→ 03 : KC인증대상 아님→ 04 : 위해우려제품 대상→ 05 : 위해우려제품 대상 아님
			reqXml.append("<ProductCertGroup>");
			reqXml.append("<crtfGrpTypCd>01</crtfGrpTypCd>");
			reqXml.append("<crtfGrpObjClfCd>01</crtfGrpObjClfCd>");
			reqXml.append("<ProductCert><certTypeCd>132</certTypeCd></ProductCert>");
			reqXml.append("</ProductCertGroup>");
			
			reqXml.append("<ProductCertGroup>");
			reqXml.append("<crtfGrpTypCd>02</crtfGrpTypCd>");
			reqXml.append("<crtfGrpObjClfCd>03</crtfGrpObjClfCd>");
			reqXml.append("</ProductCertGroup>");
			
			reqXml.append("<ProductCertGroup>");
			reqXml.append("<crtfGrpTypCd>03</crtfGrpTypCd>");
			reqXml.append("<crtfGrpObjClfCd>03</crtfGrpObjClfCd>");
			reqXml.append("</ProductCertGroup>");
			
			reqXml.append("<ProductCertGroup>");
			reqXml.append("<crtfGrpTypCd>04</crtfGrpTypCd>");
			reqXml.append("<crtfGrpObjClfCd>05</crtfGrpObjClfCd>");
			reqXml.append("</ProductCertGroup>");
		}
		
		if(pa11stGoods.getMedicalKey() != null && pa11stGoods.getMedicalRetail() != null && pa11stGoods.getMedicalAd() != null){
			reqXml.append("<ProductMedical>");//의료기기 품목허가
			reqXml.append("<MedicalKey>"+pa11stGoods.getMedicalKey()+"</MedicalKey>");
			reqXml.append("<MedicalRetail>"+pa11stGoods.getMedicalRetail()+"</MedicalRetail>");
			reqXml.append("<MedicalAd>"+pa11stGoods.getMedicalAd()+"</MedicalAd>");
			reqXml.append("</ProductMedical>");
		}
		
		if(pa11stGoods.getBeefTraceNo() != null){
			reqXml.append("<beefTraceStat>"+"01"+"</beefTraceStat>");
			reqXml.append("<beefTraceNo>"+pa11stGoods.getBeefTraceNo()+"</beefTraceNo>");
		}
		
		reqXml.append("<sellerPrdCd>"+pa11stAlcoutDealInfo.get("ALCOUT_DEAL_CODE")+"</sellerPrdCd>"); //판매자 상품코드 (제휴OUT 딜 코드)
		reqXml.append("<selTermUseYn>"+"N"+"</selTermUseYn>");//판매기간 설정안함
		
		//제휴OUT 딜 대표상품코드 판매가
		int salePrice = (int) pa11stGoods.getSalePrice(); 
		
		//제휴OUT 딜 대표상품코드 할인가 계산
		double dcAmt 			  = pa11stGoods.getDcAmt();						// ARS할인금액
		double lumpSumDcAmt = pa11stGoods.getLumpSumDcAmt(); 		// 일시불 할인금액
		double couponPrice  	  = 0;														//제휴OUT 할인금액
		
		//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
		//제휴OUT 딜 대표상품코드 프로모션  
		for(PaGoodsPriceApply goodsPriceApply : paGoodsPriceApply) {
			if(goodsPriceApply != null){
                if(goodsPriceApply.getGoodsCode().equals(pa11stGoods.getGoodsCode())){
                	couponPrice = goodsPriceApply.getCouponDcAmt(); //제휴OUT 할인금액                	
                }
			}
		}
		//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
		
		double promoPrice = dcAmt + lumpSumDcAmt + couponPrice; 	//SD1 연동 금액
				
		reqXml.append("<selPrc>"+salePrice+"</selPrc>");//판매가
		
		if(promoPrice > 0){
			reqXml.append("<cuponcheck>Y</cuponcheck>");//기본즉시할인 설정여부
			reqXml.append("<dscAmtPercnt>"+(long)promoPrice+"</dscAmtPercnt>");//할인금액
			reqXml.append("<cupnDscMthdCd>01</cupnDscMthdCd>");//01:원, 02:%
		}
		
		reqXml.append("<optSelectYn>Y</optSelectYn>");
		reqXml.append("<txtColCnt>1</txtColCnt>");
		reqXml.append("<prdExposeClfCd>00</prdExposeClfCd>");
		
		
		reqXml.append("<colTitle>옵션명</colTitle>");
		
		double minOptPrice = salePrice * -0.5;
		double maxOptPrice = salePrice;
		
		List<HashMap<String, Object>> alcoutDealPriceLogList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> alcoutDealPriceLogMap = null;
		if(pa11stAlcoutDealGoodsdtList.size() > 0){
			for(int i=0; i<pa11stAlcoutDealGoodsdtList.size(); i++){
				HashMap<String, Object> pa11stGoodsdt = pa11stAlcoutDealGoodsdtList.get(i);
				
				
				//제휴OUT 딜 상품코드 판매가
				int goodsSalePrice = Integer.parseInt(String.valueOf(pa11stGoodsdt.get("SALE_PRICE"))); 
				
				//제휴OUT 딜 상품코드 할인가 계산
				int goodsDcAmt 		= Integer.parseInt(String.valueOf(pa11stGoodsdt.get("DC_AMT")));	// ARS할인금액
				int goodsLumpSumDcAmt = Integer.parseInt(String.valueOf(pa11stGoodsdt.get("LUMP_SUM_DC_AMT")));	// 일시불 할인금액
				int goodsCouponPrice  = 0;
					
		        for(PaGoodsPriceApply goodsPriceApply : paGoodsPriceApply) { 
		            if(goodsPriceApply != null){
		            	if(goodsPriceApply.getGoodsCode().equals(pa11stGoodsdt.get("GOODS_CODE"))){
		            		goodsCouponPrice = (int)goodsPriceApply.getCouponDcAmt(); //제휴OUT 할인금액
		            	}
		            }
		        }
				//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
				
				int goodsPromoPrice = goodsDcAmt + goodsLumpSumDcAmt + goodsCouponPrice; //SD1 연동 금액
				
				int optPrice = (int) (goodsSalePrice - salePrice + promoPrice - goodsPromoPrice); // 옵션가 : 상품 판매가 - 대표 상품판매가 + 대표 상품 할인가 - 각 상품 할인가
				
				if(optPrice < minOptPrice || optPrice > maxOptPrice){ // 옵션가가 대표상품 판매가 -50%보다 작거나 100%보다 클 경우 예외처리
					optValGoodslist.add(pa11stGoodsdt.get("GOODS_CODE").toString()+pa11stGoodsdt.get("GOODSDT_CODE"));
					continue;
				}else if ( "0".equals(pa11stGoodsdt.get("TRANS_ORDER_ABLE_QTY").toString())
						 	|| pa11stGoodsdt.get("PA_SALE_GB").equals("30")
						 	|| pa11stGoodsdt.get("PA_SALE_GB").equals("35")
						 	|| pa11stGoodsdt.get("PA_SALE_GB").equals("40")
						 	|| pa11stGoodsdt.get("PA_STATUS").equals("40")
						 	|| pa11stGoodsdt.get("PA_STATUS").equals("50")
						 	|| pa11stGoodsdt.get("PA_STATUS").equals("90")) { 
							reqXml.append("<ProductOption>");
							reqXml.append("<useYn>N</useYn>");
				}else{
					reqXml.append("<ProductOption>");
					reqXml.append("<useYn>Y</useYn>");
				}
				
				if("".equals(pa11stGoodsdt.get("EXPOSURE_GOODS_NM").toString()) || pa11stGoodsdt.get("EXPOSURE_GOODS_NM").toString() == null ) {
					reqXml.append("<colValue0><![CDATA["+ComUtil.subStringBytes(pa11stGoodsdt.get("GOODS_NAME").toString(), 40)+" "+ComUtil.subStringBytes(pa11stGoodsdt.get("GOODSDT_INFO").toString(), 20)+"]]></colValue0>"); // 옵션값, 옵션명 20byte //옵션명 수정 필요
				} else {
					reqXml.append("<colValue0><![CDATA["+ComUtil.subStringBytes(pa11stGoodsdt.get("EXPOSURE_GOODS_NM").toString(), 40)+" "+ComUtil.subStringBytes(pa11stGoodsdt.get("GOODSDT_INFO").toString(), 20)+"]]></colValue0>"); // 옵션값, 옵션명 20byte //옵션명 수정 필요
				}
				
				reqXml.append("<colOptPrice>" + optPrice + "</colOptPrice>");
				
				reqXml.append("<colCount>"+pa11stGoodsdt.get("TRANS_ORDER_ABLE_QTY")+"</colCount>"); // 옵션재고수량
				reqXml.append("<colSellerStockCd>"+pa11stGoodsdt.get("GOODS_CODE")+pa11stGoodsdt.get("GOODSDT_CODE") 
								+ pa11stAlcoutDealInfo.get("ALCOUT_DEAL_CODE").toString().substring(8, 12) + goodsSalePrice +  "</colSellerStockCd>"); //샐러재고번호
				reqXml.append("</ProductOption>");
				// 2021.04.28 제휴OUT딜가격 데이터생성
				alcoutDealPriceLogMap = new HashMap<String, Object>();
				alcoutDealPriceLogMap.put("ALCOUT_DEAL_CODE", pa11stAlcoutDealInfo.get("ALCOUT_DEAL_CODE")); 
				alcoutDealPriceLogMap.put("PA_CODE", pa11stGoodsdt.get("PA_CODE"));
				alcoutDealPriceLogMap.put("GOODS_CODE", pa11stGoodsdt.get("GOODS_CODE"));
				alcoutDealPriceLogMap.put("GOODSDT_CODE", pa11stGoodsdt.get("GOODSDT_CODE"));
				alcoutDealPriceLogMap.put("SALE_PRICE", goodsSalePrice);
				alcoutDealPriceLogMap.put("DC_AMT", goodsPromoPrice);
				alcoutDealPriceLogMap.put("OPT_AMT", optPrice);
				alcoutDealPriceLogMap.put("MASTER_GOODS_PRICE", salePrice);
				alcoutDealPriceLogMap.put("MASTER_GOODS_DC_AMT", promoPrice);
				alcoutDealPriceLogMap.put("INSERT_ID", "PA11DEAL");
				alcoutDealPriceLogMap.put("MODIFY_ID", "PA11DEAL");
				alcoutDealPriceLogList.add(alcoutDealPriceLogMap);
			}
		}
		
		reqXml.append("<selMinLimitTypCd>01</selMinLimitTypCd>"); //최소구매수량 설정코드 (01:1회제한)
		reqXml.append("<selMinLimitQty>"+pa11stGoods.getOrderMinQty()+"</selMinLimitQty>"); //최소구매수량 설정코드 (01:1회제한)
		
		if(pa11stGoods.getOrderMaxQty() > 0){
			if(pa11stGoods.getCustOrdQtyCheckYn()==0){//주문수량검사여부 컬럼 추가 18.06.05 thjeon
				reqXml.append("<selLimitTypCd>01</selLimitTypCd>"); //최대구매수량  설정코드 (01:1회제한)
				reqXml.append("<selLimitQty>"+pa11stGoods.getOrderMaxQty()+"</selLimitQty>");
			} else{
				reqXml.append("<selLimitTypCd>02</selLimitTypCd>"); //최대구매수량  설정코드 (02:기간제한)
				//reqXml.append("<selLimitQty>"+pa11stGoods.getOrderMaxQty()+"</selLimitQty>"); //정책에따라 변경가능
				reqXml.append("<selLimitQty>"+pa11stGoods.getTermOrderQty()+"</selLimitQty>");
				long townSelLmtDy = pa11stGoods.getCustOrdQtyCheckTerm();
				if(townSelLmtDy==0 || townSelLmtDy>30){
					townSelLmtDy=30;
				}
				reqXml.append("<townSelLmtDy>"+townSelLmtDy+"</townSelLmtDy>");
			}
		}
		if(pa11stGoods.getDoNotIslandDelyYn().equals("1")){
			reqXml.append("<dlvCnAreaCd>02</dlvCnAreaCd>"); //배송가능지역 (02:전국(제주 도서산간지역 제외))
			reqXml.append("<advrtStmt>도서산간 배송불가</advrtStmt>"); //상품홍보문구 : 도서산간불가			
		} else {
			reqXml.append("<dlvCnAreaCd>01</dlvCnAreaCd>"); //배송가능지역 (01:전국)
			reqXml.append("<advrtStmt> </advrtStmt>"); //수정시 셋팅안하면 도서산간 배송불가 문구 남아있음
			
		}
//		reqXml.append("<dlvWyCd>01</dlvWyCd>"); //배송방법 (01:택배)
		reqXml.append("<dlvWyCd>"+ (pa11stGoods.getInstallYn().equals("1") ? "03" : "01")+"</dlvWyCd>"); //배송방법 (01:택배, 03:직접전달)
//  	11번가 톡딜의 경우 발송템플릿 세팅 안하도록 임시방편개선...
//		reqXml.append("<dlvSendCloseTmpltNo>"+pa11stGoods.getPaPolicyNo()+"</dlvSendCloseTmpltNo>"); //발송 템플릿 no
		if(pa11stGoods.getShipCostCode().substring(0, 2).equals("FR")){
			pa11stGoods.setOrdCost(0);
			reqXml.append("<dlvCstInstBasiCd>01</dlvCstInstBasiCd>");//배송비종류 (01:무료, 03:상품조건부무료, 05:1개당배송비, 02:고정배송비, 08: 출고지조건부무료)
			reqXml.append("<bndlDlvCnYn>Y</bndlDlvCnYn>"); // 묶음배송가능여부
		} else if(pa11stGoods.getShipCostCode().substring(0, 2).equals("CN") ||
				pa11stGoods.getShipCostCode().substring(0, 2).equals("PL")){
			reqXml.append("<dlvCstInstBasiCd>08</dlvCstInstBasiCd>");
			reqXml.append("<bndlDlvCnYn>Y</bndlDlvCnYn>"); // 묶음배송가능여부
		} else if(pa11stGoods.getShipCostCode().substring(0, 2).equals("ID")){
			reqXml.append("<dlvCstInstBasiCd>02</dlvCstInstBasiCd>");
			reqXml.append("<dlvCst1>"+pa11stGoods.getOrdCost()+"</dlvCst1>");//고정배송비
			reqXml.append("<bndlDlvCnYn>N</bndlDlvCnYn>"); // 묶음배송가능여부
		}

		reqXml.append("<dlvCstPayTypCd>03</dlvCstPayTypCd>"); //결제방법(03:선결제필수)
		
		long addJejuShipcost = pa11stGoods.getJejuCost()-pa11stGoods.getOrdCost();
		long addIslandShipcost = pa11stGoods.getJejuCost()-pa11stGoods.getOrdCost();
		
		if(addJejuShipcost < 0){
			addJejuShipcost = 0;
		}
		if(addIslandShipcost < 0){
			addIslandShipcost = 0;
		}
		
		reqXml.append("<jejuDlvCst>"+addJejuShipcost+"</jejuDlvCst>");//제주 추가 배송비(주문만)
		reqXml.append("<islandDlvCst>"+addIslandShipcost+"</islandDlvCst>");//도서산간 추가 배송비(주문만)
		
		reqXml.append("<addrSeqOut>"+pa11stGoods.getAddrSeqOut()+"</addrSeqOut>");//출고지 주소코드		
		reqXml.append("<addrSeqIn>"+pa11stGoods.getAddrSeqIn()+"</addrSeqIn>");//반품/교환지 주소코드
		
		if(pa11stGoodsOffer.size() > 0){
			reqXml.append("<ProductNotification>");
			reqXml.append("<type>"+pa11stGoodsOffer.get(0).getPaOfferType()+"</type>");
			for(int i=0; i<pa11stGoodsOffer.size(); i++){
				PaGoodsOffer goodsOffer = pa11stGoodsOffer.get(i);
				reqXml.append("<item>");
				reqXml.append("<code>"+goodsOffer.getPaOfferCode()+"</code>");
				reqXml.append("<name><![CDATA["+goodsOffer.getPaOfferExt().replace("<", " ").replace(">", " ")+"]]></name>");
				reqXml.append("</item>");
			}
			reqXml.append("</ProductNotification>");
		}
	
		reqXml.append("<rtngdDlvCst>"+pa11stGoods.getReturnCost()+"</rtngdDlvCst>");
		reqXml.append("<exchDlvCst>"+pa11stGoods.getChangeCost()+"</exchDlvCst>");
		/* 없어도 되는지 ?*/
		 reqXml.append("<rtngdDlvCd>"+"02"+"</rtngdDlvCd>");//반품시 왕복배송비(01,입력금액2배), 편도(02)로받을지 받을지 
		
		reqXml.append("<asDetail>"+pa11stGoods.getCsTel()+"</asDetail>");//A/S안내
		reqXml.append("<rtngExchDetail>SK Stoa : "+pa11stGoods.getCsTel()+"</rtngExchDetail>");//반품/교환안내
		
		reqXml.append("<prcCmpExpYn>Y</prcCmpExpYn>"); //가격비교사이트등록여부
		
		reqXml.append("<rmaterialTypCd>05</rmaterialTypCd>"); //원재료 유형 코드
		
		reqXml.append("</Product>");
		resultMap.put("reqXml", reqXml);
		resultMap.put("optValGoodslist", optValGoodslist);
		
		// 2021.04.28 제휴OUT딜가격 로그저장
		//pa11stGoodsService.saveAlcoutDealPriceLogTx(alcoutDealPriceLogList);
		for(HashMap<String, Object> logMap : alcoutDealPriceLogList) {
			pa11stGoodsService.insertAlcoutDealPriceLog(logMap);	
		}
		
		return resultMap;
	}
	
	/* REQ_PRM_041 11번가 제휴OUT 딜 상품 프로모션 정보 세팅 */
	private List<PaGoodsPriceApply> paPromoTargetAlcoutDealGoodsInfoSetting(ParamMap paramMap) throws Exception{
		//제휴OUT 딜 상품 조회
		List<HashMap<String,Object>> requestAlcoutDealGoodsList = pa11stGoodsService.selectAlcoutDealGoodsList(paramMap);

		List<PaGoodsPriceApply> paGoodsPriceApplyList = new ArrayList<>();
		for(HashMap<String,Object> AlcoutDealGoodsMap : requestAlcoutDealGoodsList) {
			//제휴OUT 딜 상품 프로모션 조회
			ParamMap goodsParamMap = new ParamMap();
			goodsParamMap.put("goodsCode", AlcoutDealGoodsMap.get("GOODS_CODE"));
			goodsParamMap.put("paCode", AlcoutDealGoodsMap.get("PA_CODE"));
			List<HashMap<String,Object>> requestPriceMapList = pa11stGoodsService.selectPaGoodsPriceApply(goodsParamMap);
				
			if(requestPriceMapList.size()==0){
				requestPriceMapList = new ArrayList<>();
				
				HashMap<String,Object> requestPriceMap = new HashMap<>();

				requestPriceMap.put("PRICE_APPLY_SEQ",0);
				requestPriceMap.put("PRICE_SEQ","0000");				
				requestPriceMap.put("SALE_PRICE",0);
				requestPriceMap.put("ARS_DC_AMT",0);
				
				requestPriceMap.put("ARS_OWN_DC_AMT",0);
				requestPriceMap.put("ARS_ENTP_DC_AMT",0);
				requestPriceMap.put("LUMP_SUM_DC_AMT",0);
				requestPriceMap.put("LUMP_SUM_OWN_DC_AMT",0);
				requestPriceMap.put("LUMP_SUM_ENTP_DC_AMT",0);
				
				requestPriceMap.put("COUPON_PROMO_NO", "000000000000");
				requestPriceMap.put("COUPON_DC_AMT",0);
				requestPriceMap.put("COUPON_OWN_COST",0);
				requestPriceMap.put("COUPON_ENTP_COST",0);
				
				requestPriceMapList.add(requestPriceMap);
			}
			
            PaGoodsPriceApply paGoodsPriceApply;
	    	
	    	for(HashMap<String,Object> priceMap : requestPriceMapList){
	    		
                paGoodsPriceApply = new PaGoodsPriceApply();
                paGoodsPriceApply.setGoodsCode                (goodsParamMap.get("goodsCode").toString());
                paGoodsPriceApply.setPaGroupCode             ("01");
                paGoodsPriceApply.setPriceApplySeq            (Integer.parseInt(priceMap.get("PRICE_APPLY_SEQ").toString()));
                paGoodsPriceApply.setPriceSeq                    (priceMap.get("PRICE_SEQ").toString());
                paGoodsPriceApply.setSalePrice                   (Double.parseDouble(priceMap.get("SALE_PRICE").toString()));
                paGoodsPriceApply.setArsDcAmt                  (Double.parseDouble(priceMap.get("ARS_DC_AMT").toString()));
	    		
                paGoodsPriceApply.setArsOwnDcAmt             (Double.parseDouble(priceMap.get("ARS_OWN_DC_AMT").toString()));
                paGoodsPriceApply.setArsEntpDcAmt             (Double.parseDouble(priceMap.get("ARS_ENTP_DC_AMT").toString()));
                paGoodsPriceApply.setLumpSumDcAmt          (Double.parseDouble(priceMap.get("LUMP_SUM_DC_AMT").toString()));
                paGoodsPriceApply.setLumpSumOwnDcAmt    (Double.parseDouble(priceMap.get("LUMP_SUM_OWN_DC_AMT").toString()));
                paGoodsPriceApply.setLumpSumEntpDcAmt    (Double.parseDouble(priceMap.get("LUMP_SUM_ENTP_DC_AMT").toString()));
                
                paGoodsPriceApply.setCouponPromoNo         (priceMap.get("COUPON_PROMO_NO").toString());
                paGoodsPriceApply.setCouponDcAmt             (Double.parseDouble(priceMap.get("COUPON_DC_AMT").toString()));
                paGoodsPriceApply.setCouponOwnCost          (Double.parseDouble(priceMap.get("COUPON_OWN_COST").toString()));
                paGoodsPriceApply.setCouponEntpCost          (Double.parseDouble(priceMap.get("COUPON_ENTP_COST").toString()));
                
                paGoodsPriceApplyList.add(paGoodsPriceApply);            
	    		
	    	}
		}
		
		return paGoodsPriceApplyList;		
	}
	
	private List<PaGoodsPriceApply> paPromoTargetAlcoutDealGoodsInfoSetting(List<HashMap<String, Object>> pa11stAlcoutDealGoodsdtList) throws Exception{
		//제휴OUT 딜 상품 조회
		List<PaGoodsPriceApply> paGoodsPriceApplyList = new ArrayList<>();

		for(HashMap<String,Object> AlcoutDealGoodsMap : pa11stAlcoutDealGoodsdtList) {
			//제휴OUT 딜 상품 프로모션 조회
			ParamMap goodsParamMap = new ParamMap();
			goodsParamMap.put("goodsCode"	, AlcoutDealGoodsMap.get("GOODS_CODE"));
			goodsParamMap.put("paCode"		, AlcoutDealGoodsMap.get("PA_CODE"));
			List<HashMap<String,Object>> requestPriceMapList = pa11stGoodsService.selectPaGoodsPriceApply(goodsParamMap);
		
			if(requestPriceMapList.size()==0){
				requestPriceMapList = new ArrayList<>();
				
				HashMap<String,Object> requestPriceMap = new HashMap<>();

				requestPriceMap.put("PRICE_APPLY_SEQ",0);
				requestPriceMap.put("PRICE_SEQ","0000");				
				requestPriceMap.put("SALE_PRICE",0);
				requestPriceMap.put("ARS_DC_AMT",0);
				
				requestPriceMap.put("ARS_OWN_DC_AMT",0);
				requestPriceMap.put("ARS_ENTP_DC_AMT",0);
				requestPriceMap.put("LUMP_SUM_DC_AMT",0);
				requestPriceMap.put("LUMP_SUM_OWN_DC_AMT",0);
				requestPriceMap.put("LUMP_SUM_ENTP_DC_AMT",0);
				
				requestPriceMap.put("COUPON_PROMO_NO", "000000000000");
				requestPriceMap.put("COUPON_DC_AMT",0);
				requestPriceMap.put("COUPON_OWN_COST",0);
				requestPriceMap.put("COUPON_ENTP_COST",0);
				
				requestPriceMapList.add(requestPriceMap);
			}
			
			PaGoodsPriceApply paGoodsPriceApply;
			
	    	for(HashMap<String,Object> priceMap : requestPriceMapList){
	    		
                paGoodsPriceApply = new PaGoodsPriceApply();
                paGoodsPriceApply.setGoodsCode                (goodsParamMap.get("goodsCode").toString());
                paGoodsPriceApply.setPaGroupCode             ("01");
                paGoodsPriceApply.setPriceApplySeq            (Integer.parseInt(priceMap.get("PRICE_APPLY_SEQ").toString()));
                paGoodsPriceApply.setPriceSeq                    (priceMap.get("PRICE_SEQ").toString());
                paGoodsPriceApply.setSalePrice                   (Double.parseDouble(priceMap.get("SALE_PRICE").toString()));
                paGoodsPriceApply.setArsDcAmt                  (Double.parseDouble(priceMap.get("ARS_DC_AMT").toString()));
	    		
                paGoodsPriceApply.setArsOwnDcAmt             (Double.parseDouble(priceMap.get("ARS_OWN_DC_AMT").toString()));
                paGoodsPriceApply.setArsEntpDcAmt             (Double.parseDouble(priceMap.get("ARS_ENTP_DC_AMT").toString()));
                paGoodsPriceApply.setLumpSumDcAmt          (Double.parseDouble(priceMap.get("LUMP_SUM_DC_AMT").toString()));
                paGoodsPriceApply.setLumpSumOwnDcAmt    (Double.parseDouble(priceMap.get("LUMP_SUM_OWN_DC_AMT").toString()));
                paGoodsPriceApply.setLumpSumEntpDcAmt    (Double.parseDouble(priceMap.get("LUMP_SUM_ENTP_DC_AMT").toString()));
                
                paGoodsPriceApply.setCouponPromoNo         (priceMap.get("COUPON_PROMO_NO").toString());
                paGoodsPriceApply.setCouponDcAmt             (Double.parseDouble(priceMap.get("COUPON_DC_AMT").toString()));
                paGoodsPriceApply.setCouponOwnCost          (Double.parseDouble(priceMap.get("COUPON_OWN_COST").toString()));
                paGoodsPriceApply.setCouponEntpCost          (Double.parseDouble(priceMap.get("COUPON_ENTP_COST").toString()));
                
                paGoodsPriceApplyList.add(paGoodsPriceApply);    
	    	}
	    	
		}
		
		return paGoodsPriceApplyList;
	}

	
	
	
	/**
	 * REQ_PRM_041 11번가 제휴OUT 딜 상품 재고번호 조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	//11번가 PRODUCTNO 필수
	@ApiOperation(value = "REQ_PRM_041 11번가 제휴OUT 딜 상품 재고번호 조회", notes = "REQ_PRM_041 11번가 제휴OUT 딜 상품 재고번호 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goods-stock-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealGoodsStockList(HttpServletRequest request,
		@ApiParam(name = "alcoutDealCode", value = "제휴OUT딜코드", defaultValue = "") @RequestParam(value="alcoutDealCode", required=true) String alcoutDealCode,
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") 			@RequestParam(value="goodsCode", required=true) String goodsCode,
		@ApiParam(name = "productNo", value = "11번가상품코드", defaultValue = "") 		@RequestParam(value="productNo", required=true) String productNo,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 			@RequestParam(value="paCode", required=true) String paCode,
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 			@RequestParam(value="procId", required=false, defaultValue="PA11") String procId) 
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		StringBuilder reqXml = null;
		OutputStreamWriter out = null;
		int respCode = 0;
		String respMsg = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
		List<HashMap<String, Object>> alcoutDealGoodsMappingList = new ArrayList<HashMap<String, Object>>();
	
		log.info("===== 제휴OUT딜상품재고번호 조회 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_01_006";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			/*duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});*/
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			paramMap.put("productNo", productNo);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			//log.info("03.11번가 API호출");
			if(paCode.equals(Constants.PA_11ST_BROAD_CODE)){
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"");
			} else {
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"");
			}

			reqXml = new StringBuilder();
			reqXml.append("<ProductStocks>");
			reqXml.append("<ProductStock>");
			reqXml.append("<prdNo>"+productNo+"</prdNo>");	
			reqXml.append("</ProductStock>");
			reqXml.append("</ProductStocks>");
			
			if(reqXml != null){
				out = new OutputStreamWriter(conn.getOutputStream());
				out.write(String.valueOf(reqXml));
				out.flush();

				respCode = conn.getResponseCode();
				respMsg  = conn.getResponseMessage();
				//log.info(" connect respCode : "+respCode);
				//log.info(" connect respMsg  : "+respMsg);
				if(respCode == 200){
					// RESPONSE XML 			
					doc = ComUtil.parseXML(conn.getInputStream());
					descNodes = doc.getElementsByTagName("ns2:ProductStockss");
					conn.disconnect();
					
					List<ParamMap> productStockList = new ArrayList<ParamMap>();

					for(int j=0; j<descNodes.getLength();j++){
				        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
			            	if(node.getNodeName().trim().equals("ns2:ProductStocks")){
			            		for(int x=0; x<node.getChildNodes().getLength(); x++){
				            		if(node.getChildNodes().item(x).getNodeName().trim().equals("ns2:ProductStock")){
					            		ParamMap pa11stParamMap = new ParamMap();
					            		Node pa11stList = node.getChildNodes().item(x);
					            		for(Node pa11st = pa11stList.getFirstChild(); pa11st!=null; pa11st=pa11st.getNextSibling()){ 
					            			pa11stParamMap.put(pa11st.getNodeName().trim(), pa11st.getTextContent().trim());
					            		}
					            		productStockList.add(pa11stParamMap);
			            			}
			            		}
			            	} else {
			            		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
				        	}
				        }
				    }
					//log.info("result message : "+paramMap.getString("ns2:message"));
					if(productStockList.size() > 0){
						for(int i=0; i<productStockList.size(); i++){
							HashMap<String, Object> alcoutDealGoodsMapping = new HashMap<String, Object>();
							alcoutDealGoodsMapping.put("alcoutDealCode", 	alcoutDealCode);
							alcoutDealGoodsMapping.put("paGoodsCode",		productNo);
							alcoutDealGoodsMapping.put("paCode", 			paCode);
							alcoutDealGoodsMapping.put("sellerStockCd", 	productStockList.get(i).getString("sellerStockCd"));
							alcoutDealGoodsMapping.put("paOptionCode", 		productStockList.get(i).getString("prdStckNo"));
							alcoutDealGoodsMapping.put("modifyId", 			"PA11DEAL");
							alcoutDealGoodsMapping.put("modifyDate", 		DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							alcoutDealGoodsMappingList.add(alcoutDealGoodsMapping);
						}
						paGoodsTransLog.setRtnCode("200");
						paGoodsTransLog.setSuccessYn("1");
						paramMap.put("message", "OK");
						paramMap.put("code", "200");

					} else {
						paGoodsTransLog.setRtnCode("404");
						paGoodsTransLog.setSuccessYn("0");
						paramMap.put("message", paramMap.getString("ns2:message"));
						paramMap.put("code", "404");
					}
					//전송관리 테이블 저장
					paGoodsTransLog.setGoodsCode(goodsCode);
					paGoodsTransLog.setPaCode(paCode);
					paGoodsTransLog.setItemNo(productNo);
					paGoodsTransLog.setRtnMsg(paramMap.getString("ns2:message"));
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId("PA11DEAL");
					pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
				
					
					if(alcoutDealGoodsMappingList.size()>0){
						log.info("04.재고번호 저장");
						rtnMsg = pa11stGoodsService.savePa11stAlcoutDealGoodsdtMappingPaOptionCodeTx(alcoutDealGoodsMappingList);
						if(!rtnMsg.equals("000000")){
							paramMap.put("code","404");
							paramMap.put("message",rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paramMap.put("code","200");
							paramMap.put("message","OK");
						}
					}
				}
			}
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			/*if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}*/
			
			//log.info("05.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * REQ_PRM_041 11번가 제휴OUT 딜 상품 옵션 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "REQ_PRM_041 11번가 제휴OUT 딜 상품 옵션 수정", notes = "REQ_PRM_041 11번가 제휴OUT 딜 상품 옵션 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goodsdt-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealGoodsdtModify(HttpServletRequest request,
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") 			@RequestParam(value="goodsCode", required=true) String goodsCode,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 			@RequestParam(value="paCode", required=true) String paCode,
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 			@RequestParam(value="procId", required=false, defaultValue="PA11DEAL") String procId, 
		@ApiParam(name = "alcoutDealCode", value = "제휴OUT딜코드", defaultValue = "") @RequestParam(value="alcoutDealCode", required=false) String alcoutDealCode)
		throws Exception{
		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		StringBuilder reqXml = null;
		OutputStreamWriter out = null;
 		int respCode = 0;
		String respMsg = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		ResponseEntity<?> responseMsg = null;
		
		List<HashMap<String, Object>> modifyAlcoutDealList = new ArrayList<>();  //수정대상 딜 목록
		Pa11stGoodsVO pa11stGoods = null;  //수정대상 딜 대표상품 목록
		List<Pa11stGoodsVO> ps11stGoodsDescribeList = null; // 수정대상 기술서 목록
		
		List<PaGoodsPriceApply> paGoodsPriceApply = null;
		List<HashMap<String, Object>> pa11stAlcoutDealGoodsdtList = new ArrayList<>(); // 수정대상 단품목록
		List<HashMap<String, Object>> pa11stNotExistsGoodsdtList = new ArrayList<>(); // 수정대상 추가 단품목록
		List<HashMap<String, Object>> optValGoodsList = new ArrayList<>();
		List<PaGoodsOffer> pa11stGoodsOffer = null;
		HashMap<String, String> policyMap = new HashMap<String, String>(); //발송 예정일 템플릿 용
		HashMap<String, Object> alcoutDealgoodsdtInfoMap = new HashMap<String, Object>();
		
		double timeS = 0;
		double timeE = 0;
		String responseTime = null;
		
		log.info("===== 제휴OUT딜 상품옵션 수정 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_01_012";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paCode", paCode);
		
		if(!"".equals(alcoutDealCode)){
			paramMap.put("alcoutDealCode", alcoutDealCode);	
		}
		
		try{
			log.info("02.11번가 제휴OUT 딜 상품 수정 호출 ");
			alcoutDealGoodsModify(request, paramMap.getString("goodsCode"), paramMap.getString("paCode"), "PA11DEAL");
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			//paramMap.put("goodsCode", goodsCode);
			//paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.SK스토아 tv쇼핑 딜 상품옵션 수정대상 조회");
			modifyAlcoutDealList = pa11stGoodsService.selectPa11stModifyAlcoutDealList(paramMap); //수정대상 딜 목록
			
			if(modifyAlcoutDealList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			if(modifyAlcoutDealList.size() > 0){
				for(HashMap<String, Object> pa11stAlcoutDealInfo : modifyAlcoutDealList){
					HashMap<String, Object> optValGoodsMap = new HashMap<>();
					
					paramMap.put("alcoutDealCode", 			pa11stAlcoutDealInfo.get("ALCOUT_DEAL_CODE"));
					
					/* 추가 단품 제휴OUT 딜 상품매핑 테이블에 추가*/
					pa11stNotExistsGoodsdtList = pa11stGoodsService.selectPa11stNotExistsGoodsdtList(paramMap);
					for(HashMap<String, Object> pa11stNotExistsGoodsdt : pa11stNotExistsGoodsdtList) {
						pa11stNotExistsGoodsdt.put("alcoutDealCode", 	paramMap.get("alcoutDealCode"));
						pa11stNotExistsGoodsdt.put("paGoodsCode", 		pa11stAlcoutDealInfo.get("PA_GOODS_CODE"));
						pa11stNotExistsGoodsdt.put("paCode", 			paramMap.get("paCode"));
						pa11stGoodsService.insertPa11stNotExistsGoodsdtTx(pa11stNotExistsGoodsdt);
					}
					
					pa11stGoods = pa11stGoodsService.selectPa11stAlcoutDealGoodsInfo(paramMap); //수정대상 딜 대표상품 목록
					
					ps11stGoodsDescribeList = pa11stGoodsService.selectPa11stAlcoutDealGoodsDescribe(paramMap); //수정대상 기술서 조회
					
					if(pa11stGoods == null){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("pa.not_exists_process_list"));
						continue;
					}
					
					if("".equals(pa11stGoods.getProductNo()) || pa11stGoods.getProductNo() == null) {
						paramMap.put("code","404");
						paramMap.put("message",getMessage("pa.not_exists_process_list"));
						continue;
					}
					
					if(pa11stGoods.getDescribeExt().equals("")){
						paramMap.put("code","420");
						paramMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + pa11stGoods.getGoodsCode()}));
						continue;
					}
					
					String strDescribeExt = "";
					long describeLength = 0;
					
					for(Pa11stGoodsVO ps11stGoodsDescribe : ps11stGoodsDescribeList) {
						if(!("").equals(ps11stGoodsDescribe.getDescribeExt()) && ps11stGoodsDescribe.getDescribeExt() != null) {
							strDescribeExt += ps11stGoodsDescribe.getGoodsName() + "<br />" + ps11stGoodsDescribe.getDescribeExt() + "<br />";
							describeLength += ps11stGoodsDescribe.getDescribeLen();
						}
					}
					
					if(describeLength > 6000){
						paramMap.put("code","415");
						paramMap.put("message","상품 기술서 길이가 6000자가 넘습니다. 상품코드 : " + pa11stGoods.getGoodsCode());
						continue;
					}

					
					if("".equals(pa11stGoods.getCollectImage()) || pa11stGoods.getCollectImage() == null) {
						pa11stGoods.setDescribeExt("<div align='center'><img alt='' src='" + pa11stGoods.getTopImage() + "' /><br /><br /><br />" + strDescribeExt.replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + pa11stGoods.getBottomImage() + "' /></div>");				
					}else {
						pa11stGoods.setDescribeExt("<div align='center'><img alt='' src='" + pa11stGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + pa11stGoods.getCollectImage() + "' /><br /><br /><br />" + strDescribeExt.replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + pa11stGoods.getBottomImage() + "' /></div>");
					}
					
					//수정대상 제휴OUT 딜 상품/단품조회
					pa11stAlcoutDealGoodsdtList = pa11stGoodsService.selectPa11stAlcoutDealGoodsdtInfoList(paramMap);
					
					paramMap.put("paGroupCode", pa11stGoods.getPaGroupCode());
					
					//수정대상 제휴OUT 딜 대표상품 고시정보 조회
					pa11stGoodsOffer = pa11stGoodsService.selectPa11stGoodsOfferList(paramMap);
					
					if(pa11stGoodsOffer.size() < 1){
						paramMap.put("code","430");
						paramMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + pa11stGoods.getGoodsCode()}));
						continue;
					}
					
					//상품 발송 예정일 템플릿 조회
					/* policyMap = pa11stGoodsService.selectGoodsFor11stPolicy(pa11stGoods);
					if(policyMap == null){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("pa.not_exists_process_list", new String[] {"상품 발송 예정일 템플릿 API 상품코드 : " + pa11stGoods.getGoodsCode()}));
						continue;
					} */
					
					//수정대상 제휴OUT 딜 상품 즉시할인쿠폰 프로모션
					paGoodsPriceApply = paPromoTargetAlcoutDealGoodsInfoSetting(paramMap);
					
					//log.info("04.11번가 API호출");
					if(pa11stGoods.getPaCode().equals(Constants.PA_11ST_BROAD_CODE)){
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"/"+pa11stGoods.getProductNo());
					} else {
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"/"+pa11stGoods.getProductNo());
					}
					alcoutDealgoodsdtInfoMap = pa11stAlcoutDealGoodsInfoSetting(pa11stAlcoutDealInfo, pa11stGoods, pa11stAlcoutDealGoodsdtList, pa11stGoodsOffer, paGoodsPriceApply);
					
					reqXml = (StringBuilder) alcoutDealgoodsdtInfoMap.get("reqXml");
					
					List<String> optValGoodslist = new ArrayList<>(); 
					
					optValGoodslist = (List<String>) alcoutDealgoodsdtInfoMap.get("optValGoodslist");
					//log.info("11번가로 전달한 전문[등록]"); /* 향후 삭제 필요 */
				    if(reqXml != null){
				    	//log.info(String.valueOf(reqXml));
				    	paramMap.put("body", String.valueOf(reqXml));
				    }
				    
					if(reqXml != null){
						//log.info("04.11번가 API 호출");
						timeS = System.nanoTime();
						out = new OutputStreamWriter(conn.getOutputStream());
						out.write(String.valueOf(reqXml));
						out.flush();
						

						respCode = conn.getResponseCode();
						respMsg  = conn.getResponseMessage();
						log.info(" connect respCode : "+respCode);
						log.info(" connect respMsg  : "+respMsg);
						
						timeE = System.nanoTime();
						responseTime = Double.toString((timeE-timeS)/1000000000);
						paramMap.put("responseTime", responseTime);
						paramMap.put("result", respMsg);
						
						pa11stAsyncController.insertPaRequestMap(paramMap);
						
						if(respCode == 200){
							// RESPONSE XML 		
							doc = ComUtil.parseXML(conn.getInputStream());
							descNodes = doc.getElementsByTagName("ClientMessage");
							conn.disconnect();

							for(int j=0; j<descNodes.getLength();j++){
						        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
						        	paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
						        }
						    }
							//전송관리 테이블 저장
							paGoodsTransLog = new PaGoodsTransLog();
							paGoodsTransLog.setGoodsCode(paramMap.getString("goodsCode"));
							paGoodsTransLog.setPaCode(paramMap.getString("paCode"));
							paGoodsTransLog.setItemNo(paramMap.getString("productNo").isEmpty()?paramMap.getString("goodsCode"):paramMap.getString("productNo"));
							paGoodsTransLog.setRtnCode(paramMap.getString("resultCode"));
							paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
							paGoodsTransLog.setSuccessYn(paramMap.getString("resultCode").equals("200")==true?"1":"0");
							paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paGoodsTransLog.setProcId("PA11DEAL");
							pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
							
							pa11stAlcoutDealInfo.put("alcoutDealCode", 	paramMap.getString("alcoutDealCode"));
							pa11stAlcoutDealInfo.put("paGoodsCode",	 	pa11stGoods.getProductNo());
							pa11stAlcoutDealInfo.put("paCode", 	 		paramMap.getString("paCode"));
							pa11stAlcoutDealInfo.put("paStatus", 		"30");// 입점완료
							pa11stAlcoutDealInfo.put("modifyId"	, 		"PA11DEAL");
							pa11stAlcoutDealInfo.put("modifyDate",		DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							log.info("05.제휴사 상품정보 저장");
							// 제휴사 제휴OUT 딜코드 저장
							rtnMsg = pa11stGoodsService.savePa11stAlcoutDealTx(pa11stAlcoutDealInfo);
							
							if(optValGoodslist.size() > 0) {
								for(String otpValgoodsCode : optValGoodslist){
									pa11stAlcoutDealInfo.put("goodsCode", otpValgoodsCode);
									pa11stAlcoutDealInfo.put("paStatus", "20"); //입점반려
									pa11stAlcoutDealInfo.put("returnNote",  "판매가 오류");
									pa11stAlcoutDealInfo.put("modifyId"	, "PA11DEAL");
									pa11stAlcoutDealInfo.put("modifyDate"	, DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
									
									log.info("07.제휴사 실패정보 저장");
									pa11stGoodsService.savePa11stAlcoutDealGoodsFailTx(pa11stAlcoutDealInfo);
								}
							}
							
							responseMsg = null;
							log.info("06.재고번호 조회 API호출 : /goods-stock-list");
							// 제휴OUT 딜 상품+단품 재고번호 조회
							responseMsg = alcoutDealGoodsStockList(request, pa11stAlcoutDealInfo.get("alcoutDealCode").toString(), pa11stGoods.getGoodsCode(), pa11stGoods.getProductNo(), pa11stGoods.getPaCode(), "PA11DEAL");
							
							if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
								paramMap.put("code","500");
								paramMap.put("message",getMessage("errors.api.system")+" : goodsStockList");
								log.error("11번가 상품등록 재고번호 호출 에러");
								continue;
							} else {
								pa11stGoods.setTransTargetYn("0");
								pa11stGoods.setAlcoutDealCode(pa11stAlcoutDealInfo.get("alcoutDealCode").toString());
								pa11stGoodsService.updateOutDealGoodsTarget(pa11stGoods);
							}
						} else {
							paramMap.put("code",respCode);
							paramMap.put("message",respMsg);
						}
					}
				}
			}
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			//log.info("07.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * REQ_PRM_041 11번가 제휴OUT 딜 상품 상세설명 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "REQ_PRM_041 11번가 제휴OUT 딜 상품 상세설명 수정", notes = "REQ_PRM_041 11번가 제휴OUT 딜 상품 상세설명 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goods-desc-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealgoodsDescModify(HttpServletRequest request,
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") 			@RequestParam(value="goodsCode", required=true) String goodsCode,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 			@RequestParam(value="paCode", required=true) String paCode,
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 			@RequestParam(value="procId", required=false, defaultValue="PA11DEAL") String procId) 
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		StringBuilder reqXml = null;
		OutputStreamWriter out = null;
		int respCode = 0;
		String respMsg = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		
		List<HashMap<String, Object>> modifyAlcoutDealList = new ArrayList<>();  //수정대상 딜 목록
	
		List<HashMap<String, String>> goodsDescList = new ArrayList<>();
		
		log.info("===== 제휴OUT딜 상품상세설명 수정 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_01_007";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		
		String strProductNo = "";
		String strDescribe = "";
		String strPaCode = "";
		String strGoodsCode = "";
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paCode", paCode);
		paramMap.put("procId", procId);
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			// 제휴OUT딜 상품상세설명 수정대상 조회
			modifyAlcoutDealList = pa11stGoodsService.selectPa11stModifyAlcoutDealList(paramMap); //수정대상 딜 목록
			
			if(modifyAlcoutDealList.size() > 0) {
				for(HashMap<String, Object> modifyAlcoutDealMap: modifyAlcoutDealList) {

					log.info("03.SK스토아 상품상세설명 수정대상 조회");
					goodsDescList = pa11stGoodsService.selectPa11stAlcoutDealGoodsDescribeModify(modifyAlcoutDealMap);
					if(goodsDescList.size() == 0){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("pa.not_exists_process_list"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					
					
					for(HashMap<String, String> goodsDesc : goodsDescList) {
						strDescribe += "<![CDATA["+goodsDesc.get("DESCRIBE_EXT")+goodsDesc.get("DESCRIBE_EXT1")+"]]>" + "<br /><br />";
						strProductNo = goodsDesc.get("PRODUCT_NO");
						strPaCode = goodsDesc.get("PA_CODE");
						strGoodsCode = goodsDesc.get("GOODS_CODE");
					}
					
					//log.info("04.11번가 API호출");
					if(strPaCode.equals(Constants.PA_11ST_BROAD_CODE)){
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"/"+strProductNo);
					} else {
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"/"+strProductNo);
					}
					
					reqXml = new StringBuilder();
					reqXml.append("<ProductDetailCont>");
					reqXml.append("<prdDescContClob>"+ strDescribe +"</prdDescContClob>");
					reqXml.append("</ProductDetailCont>");
					
					if(reqXml != null){
						out = new OutputStreamWriter(conn.getOutputStream());
						out.write(String.valueOf(reqXml));
						out.flush();
		
						respCode = conn.getResponseCode();
						respMsg  = conn.getResponseMessage();
						//log.info(" connect respCode : "+respCode);
						//log.info(" connect respMsg  : "+respMsg);
						if(respCode == 200){
							// RESPONSE XML 			
							doc = ComUtil.parseXML(conn.getInputStream());
							descNodes = doc.getElementsByTagName("ProductDetailCont");
							conn.disconnect();
		
							for(int j=0; j<descNodes.getLength();j++){
						        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
						        	paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
						        }
						    }
							log.info("result message : "+paramMap.getString("message"));
							
							//전송관리 테이블 저장
							paGoodsTransLog = new PaGoodsTransLog();
							paGoodsTransLog.setGoodsCode(strGoodsCode);
							paGoodsTransLog.setPaCode(strPaCode);
							paGoodsTransLog.setItemNo(strProductNo);
							paGoodsTransLog.setRtnCode(paramMap.getString("resultCode"));
							paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
							paGoodsTransLog.setSuccessYn(paramMap.getString("resultCode").equals("200")==true?"1":"0");
							paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paGoodsTransLog.setProcId("PA11");
							pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
						
							
							if(paramMap.getString("resultCode").equals("000")){
								if(!rtnMsg.equals("000000")){
									paramMap.put("code","404");
									paramMap.put("message",rtnMsg);
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								} else {
									paramMap.put("code","200");
									paramMap.put("message","OK");
								}
							} else {
								paramMap.put("code",paramMap.getString("resultCode"));
							}
						}
					}
				}
			}
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			//log.info("06.저장 완료 API END");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 제휴OUT 딜 상품 VOD URL 연동
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "제휴OUT 딜 상품 VOD URL 연동", notes = "제휴OUT 딜 상품 VOD URL 연동", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goods-vod-url", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealgoodsVodUrl(HttpServletRequest request,
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value="procId", required=false, defaultValue="PA11") String procId) 
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn 	= null;
		Document doc 			= null;
		NodeList descNodes 		= null;
		StringBuilder reqXml 	= null;
		OutputStreamWriter out 	= null;
		int respCode 			= 0;
		String respMsg 			= null;
		String dateTime 		= "";
		String startTime		= systemService.getSysdatetimeToString();
		int executeCnt = 0;
		List<Pa11stGoodsVO> pickcastList = new ArrayList<Pa11stGoodsVO>();
		Pa11stGoodsVO pickcast = null;
		int targetCount = 0;
		int procCount = 0;
		String msg = "";
		
		log.info("===== 상품 VOD URL 연동 Start=====");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_06_001";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		
		Timestamp sysdate = DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss");
		
		try{
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			List<Pa11stGoodsVO> pa11stGoodsVodTransList = new ArrayList<Pa11stGoodsVO>();
			pa11stGoodsVodTransList = pa11stGoodsService.selectPa11stAlcoutDealVodUrlTransList();
			Pa11stGoodsVO pa11stGoodsVodTrans = null;
			targetCount = pa11stGoodsVodTransList.size();
			if(pa11stGoodsVodTransList.size() > 0){
				for(int i=0;i<pa11stGoodsVodTransList.size();i++){
					try {
						String goodsCode = "";
						String vodUrl 	 = "";
						String productNo = "";
						String paCode 	 = "";	
						
						pa11stGoodsVodTrans = pa11stGoodsVodTransList.get(i);
						pa11stGoodsVodTrans.setApplyDate(sysdate);
						
						goodsCode		= pa11stGoodsVodTrans.getGoodsCode();
						productNo		= pa11stGoodsVodTrans.getProductNo();
						paCode 			= pa11stGoodsVodTrans.getPaCode();
						vodUrl			= pa11stGoodsVodTrans.getVodUrl();
						
						paramMap.put("goodsCode", 	goodsCode);
						paramMap.put("productNo", 	productNo);
						paramMap.put("paCode", 	  	paCode);
						paramMap.put("vodUrl", 		vodUrl);
						paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						if(paCode.equals(Constants.PA_11ST_BROAD_CODE)){
							conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"");
						} else {
							conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"");
						}
						
						reqXml = new StringBuilder();
						reqXml.append("<ProductMovie>");
						reqXml.append("<prdNo>"+productNo+"</prdNo>");	
						reqXml.append("<movieUrl>"+vodUrl+"</movieUrl>");	
						reqXml.append("</ProductMovie>");
						
						if(reqXml != null){
							out = new OutputStreamWriter(conn.getOutputStream());
							out.write(String.valueOf(reqXml));
							out.flush();
	
							respCode = conn.getResponseCode();
							respMsg  = conn.getResponseMessage();
							
							paramMap.put("respCode", respCode);
							paramMap.put("respMsg", respMsg);
							
							// RESPONSE XML 			
							doc = ComUtil.parseXML(conn.getInputStream()); //read
						    descNodes = doc.getElementsByTagName("Result");
						    conn.disconnect();
						    
						    for(int j=0; j<descNodes.getLength();j++){
						        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
					        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
						        }
							}
						    
							if(paramMap.getString("resultCode").equals("200")){
								// update trans_target = '0'
								procCount++;
								pa11stGoodsService.updatePaGoodsVodUrlTransYn(pa11stGoodsVodTrans);
							} else {
								pa11stGoodsService.updatePaGoodsVodUrlFailMsg(paramMap);
							}
						}
					}catch(Exception e) {
						log.error(e.getMessage());
					}
				}	
			}	
		}catch (Exception e) {
			paramMap.put("code","400");
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				if(!paramMap.getString("code").equals("400")){
					msg = "대상건수:" + targetCount + ", 성공건수:" + procCount;
					paramMap.put("message", msg);
				}
				if(targetCount == procCount){
					paramMap.put("code","200");
				} else {
					paramMap.put("code","500");
				}
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("startDate", startTime);
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error1 : "+e.getMessage());
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * REQ_PRM_041 11번가 제휴OUT 딜 상품 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "REQ_PRM_041 11번가 제휴OUT 딜 상품 수정", notes = "REQ_PRM_041 11번가 제휴OUT 딜 상품 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goods-modify(no_use)", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealGoodsModify(HttpServletRequest request,
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") 	@RequestParam(value="goodsCode", required=false) String goodsCode,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 	@RequestParam(value="paCode", required=false) String paCode,
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 	@RequestParam(value="procId", required=false, defaultValue="PA11DEAL") String procId) 
		throws Exception{
		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		StringBuilder reqXml = null;
		OutputStreamWriter out = null;
		int respCode = 0;
		String respMsg = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		ResponseEntity<?> responseMsg = null;
		
		List<HashMap<String, Object>> modifyAlcoutDealList = new ArrayList<>();  //수정대상 딜 목록
		Pa11stGoodsVO pa11stGoods = null;  //수정대상 딜 대표상품 목록
		List<Pa11stGoodsVO> ps11stGoodsDescribeList = null; // 수정대상 기술서 목록
		
		List<PaGoodsPriceApply> paGoodsPriceApplyList = null;
		List<HashMap<String, Object>> pa11stAlcoutDealGoodsdtList = new ArrayList<>(); // 수정대상 단품목록
		List<HashMap<String, Object>> pa11stNotExistsGoodsdtList = new ArrayList<>(); // 수정대상 추가 단품목록
		List<HashMap<String, Object>> optValGoodsList = new ArrayList<>();
		List<PaGoodsOffer> pa11stGoodsOffer = null;
		HashMap<String, String> policyMap = new HashMap<String, String>(); //발송 예정일 템플릿 용
		HashMap<String, Object> alcoutDealgoodsdtInfoMap = new HashMap<String, Object>();
		
		double timeS = 0;
		double timeE = 0;
		String responseTime = null;
		
		log.info("===== 제휴OUT딜 상품 수정 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_01_013";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paCode", paCode);
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			//paramMap.put("goodsCode", goodsCode);
			//paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.SK스토아 tv쇼핑 딜 상품 수정대상 조회");
			modifyAlcoutDealList = pa11stGoodsService.selectPa11stModifyAlcoutDealList(paramMap); //수정대상 딜 목록
			
			if(modifyAlcoutDealList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			if(modifyAlcoutDealList.size() > 0){
				for(HashMap<String, Object> pa11stAlcoutDealInfo : modifyAlcoutDealList){
					HashMap<String, Object> optValGoodsMap = new HashMap<>();
					
					paramMap.put("alcoutDealCode", 			pa11stAlcoutDealInfo.get("ALCOUT_DEAL_CODE"));
					if("".equals(goodsCode) || goodsCode == null) {
						paramMap.put("goodsCode", 				pa11stAlcoutDealInfo.get("GOODS_CODE"));
					}
					if("".equals(paCode) || paCode == null) {
						paramMap.put("paCode", 				pa11stAlcoutDealInfo.get("PA_CODE"));					
					}
					
					/* 추가 단품 제휴OUT 딜 상품매핑 테이블에 추가*/
					pa11stNotExistsGoodsdtList = pa11stGoodsService.selectPa11stNotExistsGoodsdtList(paramMap);
					for(HashMap<String, Object> pa11stNotExistsGoodsdt : pa11stNotExistsGoodsdtList) {
						pa11stNotExistsGoodsdt.put("alcoutDealCode", 	paramMap.get("alcoutDealCode"));
						pa11stNotExistsGoodsdt.put("paGoodsCode", 		pa11stAlcoutDealInfo.get("PA_GOODS_CODE"));
						pa11stNotExistsGoodsdt.put("paCode", 			paramMap.get("paCode"));
						pa11stGoodsService.insertPa11stNotExistsGoodsdtTx(pa11stNotExistsGoodsdt);
					}
					
					pa11stGoods = pa11stGoodsService.selectPa11stAlcoutDealGoodsInfo(paramMap); //수정대상 딜 대표상품 목록
					
					ps11stGoodsDescribeList = pa11stGoodsService.selectPa11stAlcoutDealGoodsDescribe(paramMap); //수정대상 기술서 조회
					
					if(pa11stGoods == null){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("pa.not_exists_process_list"));
						continue;
					}
					
					if("".equals(pa11stGoods.getProductNo()) || pa11stGoods.getProductNo() == null) {
						paramMap.put("code","404");
						paramMap.put("message",getMessage("pa.not_exists_process_list"));
						continue;
					}
					
					if(pa11stGoods.getDescribeExt().equals("")){
						paramMap.put("code","420");
						paramMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + pa11stGoods.getGoodsCode()}));
						continue;
					}
					
					String strDescribeExt = "";
					long describeLength = 0;
					
					for(Pa11stGoodsVO ps11stGoodsDescribe : ps11stGoodsDescribeList) {
						if(!("").equals(ps11stGoodsDescribe.getDescribeExt()) && ps11stGoodsDescribe.getDescribeExt() != null) {
							strDescribeExt += ps11stGoodsDescribe.getGoodsName() + "<br />" + ps11stGoodsDescribe.getDescribeExt() + "<br />";
							describeLength += ps11stGoodsDescribe.getDescribeLen();
						}
					}
					
					if(describeLength > 6000){
						paramMap.put("code","415");
						paramMap.put("message","상품 기술서 길이가 6000자가 넘습니다. 상품코드 : " + pa11stGoods.getGoodsCode());
						continue;
					}

					
					if("".equals(pa11stGoods.getCollectImage()) || pa11stGoods.getCollectImage() == null) {
						pa11stGoods.setDescribeExt("<div align='center'><img alt='' src='" + pa11stGoods.getTopImage() + "' /><br /><br /><br />" + strDescribeExt.replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + pa11stGoods.getBottomImage() + "' /></div>");				
					}else {
						pa11stGoods.setDescribeExt("<div align='center'><img alt='' src='" + pa11stGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + pa11stGoods.getCollectImage() + "' /><br /><br /><br />" + strDescribeExt.replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + pa11stGoods.getBottomImage() + "' /></div>");
					}
					
					//수정대상 제휴OUT 딜 상품/단품조회
					pa11stAlcoutDealGoodsdtList = pa11stGoodsService.selectPa11stAlcoutDealGoodsdtInfoList(paramMap);
					
					paramMap.put("paGroupCode", pa11stGoods.getPaGroupCode());
					
					//수정대상 제휴OUT 딜 대표상품 고시정보 조회
					pa11stGoodsOffer = pa11stGoodsService.selectPa11stGoodsOfferList(paramMap);
					
					if(pa11stGoodsOffer.size() < 1){
						paramMap.put("code","430");
						paramMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + pa11stGoods.getGoodsCode()}));
						continue;
					}
					
					//상품 발송 예정일 템플릿 조회
					/* policyMap = pa11stGoodsService.selectGoodsFor11stPolicy(pa11stGoods);
					if(policyMap == null){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("pa.not_exists_process_list", new String[] {"상품 발송 예정일 템플릿 API 상품코드 : " + pa11stGoods.getGoodsCode()}));
						continue;
					} */
					
					//수정대상 제휴OUT 딜 상품 즉시할인쿠폰 프로모션
					paGoodsPriceApplyList = paPromoTargetAlcoutDealGoodsInfoSetting(paramMap);
					
					//log.info("04.11번가 API호출");
					if(pa11stGoods.getPaCode().equals(Constants.PA_11ST_BROAD_CODE)){
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"/"+pa11stGoods.getProductNo());
					} else {
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"/"+pa11stGoods.getProductNo());
					}
					alcoutDealgoodsdtInfoMap = pa11stAlcoutDealGoodsInfoSetting(pa11stAlcoutDealInfo, pa11stGoods, pa11stAlcoutDealGoodsdtList, pa11stGoodsOffer, paGoodsPriceApplyList);
					
					reqXml = (StringBuilder) alcoutDealgoodsdtInfoMap.get("reqXml");
					
					List<String> optValGoodslist = new ArrayList<>(); 
					
					optValGoodslist = (List<String>) alcoutDealgoodsdtInfoMap.get("optValGoodslist");
					//log.info("11번가로 전달한 전문[등록]"); /* 향후 삭제 필요 */
				    if(reqXml != null){
				    	//log.info(String.valueOf(reqXml));
				    	paramMap.put("body", String.valueOf(reqXml));
				    }
				    
					if(reqXml != null){
						//log.info("04.11번가 API 호출");
						timeS = System.nanoTime();
						out = new OutputStreamWriter(conn.getOutputStream());
						out.write(String.valueOf(reqXml));
						out.flush();
						

						respCode = conn.getResponseCode();
						respMsg  = conn.getResponseMessage();
						log.info(" connect respCode : "+respCode);
						log.info(" connect respMsg  : "+respMsg);
						
						timeE = System.nanoTime();
						responseTime = Double.toString((timeE-timeS)/1000000000);
						paramMap.put("responseTime", responseTime);
						paramMap.put("result", respMsg);
						
						pa11stAsyncController.insertPaRequestMap(paramMap);
						
						if(respCode == 200){
							// RESPONSE XML 		
							doc = ComUtil.parseXML(conn.getInputStream());
							descNodes = doc.getElementsByTagName("ClientMessage");
							conn.disconnect();

							for(int j=0; j<descNodes.getLength();j++){
						        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
						        	paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
						        }
						    }
							//전송관리 테이블 저장
							paGoodsTransLog = new PaGoodsTransLog();
							paGoodsTransLog.setGoodsCode(paramMap.getString("goodsCode"));
							paGoodsTransLog.setPaCode(paramMap.getString("paCode"));
							paGoodsTransLog.setItemNo(paramMap.getString("productNo").isEmpty()?paramMap.getString("goodsCode"):paramMap.getString("productNo"));
							paGoodsTransLog.setRtnCode(paramMap.getString("resultCode"));
							paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
							paGoodsTransLog.setSuccessYn(paramMap.getString("resultCode").equals("200")==true?"1":"0");
							paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paGoodsTransLog.setProcId("PA11DEAL");
							pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
							
							pa11stAlcoutDealInfo.put("alcoutDealCode", 	paramMap.getString("alcoutDealCode"));
							pa11stAlcoutDealInfo.put("paGoodsCode",	 	pa11stGoods.getProductNo());
							pa11stAlcoutDealInfo.put("paCode", 	 		paramMap.getString("paCode"));
							pa11stAlcoutDealInfo.put("paStatus", 		"30");// 입점완료
							pa11stAlcoutDealInfo.put("modifyId"	, 		"PA11DEAL");
							pa11stAlcoutDealInfo.put("modifyDate",		DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							log.info("05.제휴사 상품정보 저장");
							// 제휴사 제휴OUT 딜코드 저장
							rtnMsg = pa11stGoodsService.savePa11stAlcoutDealTx(pa11stAlcoutDealInfo);
							
							if("200".equals(paramMap.getString("resultCode"))) {
								pa11stGoods.setTransTargetYn("1"); // 옵션수정을 위해 타겟 유지
								pa11stGoods.setAlcoutDealCode(paramMap.getString("alcoutDealCode"));
								pa11stGoodsService.updateOutDealGoodsTarget(pa11stGoods);
							}
							
							if(optValGoodslist.size() > 0) {
								for(String otpValgoodsCode : optValGoodslist){
									pa11stAlcoutDealInfo.put("goodsCode", otpValgoodsCode);
									pa11stAlcoutDealInfo.put("paStatus", "20"); //입점반려
									pa11stAlcoutDealInfo.put("returnNote",  "판매가 오류");
									pa11stAlcoutDealInfo.put("modifyId"	, "PA11DEAL");
									pa11stAlcoutDealInfo.put("modifyDate"	, DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
									
									log.info("07.제휴사 실패정보 저장");
									pa11stGoodsService.savePa11stAlcoutDealGoodsFailTx(pa11stAlcoutDealInfo);
								}
							}
							
							responseMsg = null;
							log.info("06.재고번호 조회 API호출 : /goods-stock-list");
							// 제휴OUT 딜 상품+단품 재고번호 조회
							responseMsg = alcoutDealGoodsStockList(request, pa11stAlcoutDealInfo.get("alcoutDealCode").toString(), pa11stGoods.getGoodsCode(), pa11stGoods.getProductNo(), pa11stGoods.getPaCode(), "PA11DEAL");
							
							if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
								paramMap.put("code","500");
								paramMap.put("message",getMessage("errors.api.system")+" : goodsStockList");
								log.error("11번가 상품등록 재고번호 호출 에러");
								continue;
							}
						} else {
							paramMap.put("code",respCode);
							paramMap.put("message",respMsg);
						}
					}
				}
			}
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			//log.info("07.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/* 제휴OUT 딜 END*/
	
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "제휴OUT딜 상품 수정 API", notes = "제휴OUT딜 상품 수정 API", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goods-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutNewDealGoodsModify(HttpServletRequest request,
		@ApiParam(name = "alcoutDealCode", value = "제휴OUT딜코드", defaultValue = "") @RequestParam(value="alcoutDealCode", required=false) String alcoutDealCode,
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") 			@RequestParam(value="goodsCode", required=false) String goodsCode,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 			@RequestParam(value="paCode", required=false) String paCode,
		@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 			@RequestParam(value="procId", required=false, defaultValue="PA11DEAL") String procId) 
		throws Exception{
		
		HashMap<String, String> apiInfo 		= new HashMap<String, String>();
		HashMap<String, String> apiInfoOption 	= new HashMap<String, String>();
		ParamMap paramMap 						= new ParamMap();
		HttpURLConnection conn 					= null;
		StringBuilder reqXml 					= null;
		String dateTime 						= "";
		String duplicateCheck 					= "";

		
		List<HashMap<String, Object>> modifyAlcoutDealList  	= new ArrayList<>();  //수정대상 딜 목록
		Pa11stGoodsVO pa11stGoods 									= null;  //수정대상 딜 대표상품 목록
		List<PaGoodsPriceApply> paGoodsPriceApplyList 		= null;
		
		List<HashMap<String, Object>> pa11stAlcoutDealGoodsdtList 	= new ArrayList<>(); // 수정대상 단품목록
		List<PaGoodsOffer> pa11stGoodsOffer 						= null;
		HashMap<String, Object> alcoutDealgoodsdtInfoMap 			= new HashMap<String, Object>();
		List<String> optValGoodslist 								= new ArrayList<>(); 
		
		log.info("===== 제휴OUT딜 상품 수정 API Start=====");
		
		//= connectionSetting 설정
		String prg_id 		= "IF_PA11STAPI_01_013";
		String prg_id_sub	= "IF_PA11STAPI_01_012";
		String request_type = "POST";
		dateTime 			= systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode"			, prg_id);
		paramMap.put("broadCode"		, Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode"		, Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("goodsCode"		, goodsCode);
		paramMap.put("paCode"			, paCode);
		paramMap.put("alcoutDealCode"	, alcoutDealCode); 
		paramMap.put("dateTime"			, dateTime);
		paramMap.put("sysDateTime"		, DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			//상품 수정 API Information 조회
			apiInfo 		= systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			apiInfo.put("method"	 , request_type);
			
			//상품 옵션 수정 API Information 조회
			paramMap.put("apiCode"		, prg_id_sub);
			apiInfoOption   = systemService.selectPaApiInfo(paramMap);
			apiInfoOption.put("contentType", "text/xml;charset=utf-8");
			apiInfoOption.put("method"	 , request_type);
			paramMap.put("apiCode"		, prg_id);
			
			
			log.info("03.SK스토아 tv쇼핑 딜 상품 수정대상 조회");
			//modifyAlcoutDealList = pa11stGoodsService.selectPa11stModifyAlcoutDealList(paramMap); //수정대상 딜 목록
			modifyAlcoutDealList = pa11stGoodsService.selectPa11stModifyAlcoutDealTarget(paramMap); 
			
			
			if(modifyAlcoutDealList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			
			for(HashMap<String, Object> pa11stAlcoutDealInfo : modifyAlcoutDealList){
				paramMap.put("alcoutDealCode"	, 		pa11stAlcoutDealInfo.get("ALCOUT_DEAL_CODE"));
				paramMap.put("goodsCode"		, 		pa11stAlcoutDealInfo.get("GOODS_CODE"));
				paramMap.put("paCode"			, 		pa11stAlcoutDealInfo.get("PA_CODE"));
				
				//01. 수정대상 딜 대표상품 목록
				pa11stGoods = pa11stGoodsService.selectPa11stAlcoutDealGoodsInfo(paramMap); 
				if(!chkMastDealGoods(pa11stGoods, paramMap)) continue;
				
				//02. 수정 기술서 세팅
				String strDescribeExt = getDescribeInfo(pa11stGoods, paramMap);  //pa11stGoodsService.selectPa11stAlcoutDealGoodsDescribe()
				if("".equals(strDescribeExt)) continue;
				
				if("".equals(pa11stGoods.getCollectImage()) || pa11stGoods.getCollectImage() == null) {
					pa11stGoods.setDescribeExt("<div align='center'><img alt='' src='" + pa11stGoods.getTopImage() + "' /><br /><br /><br />" + strDescribeExt.replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + pa11stGoods.getBottomImage() + "' /></div>");				
				}else {
					pa11stGoods.setDescribeExt("<div align='center'><img alt='' src='" + pa11stGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + pa11stGoods.getCollectImage() + "' /><br /><br /><br />" + strDescribeExt.replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + pa11stGoods.getBottomImage() + "' /></div>");
				}
				
				//03. 수정대상 제휴OUT 딜 상품/단품조회
				pa11stAlcoutDealGoodsdtList = pa11stGoodsService.selectPa11stAlcoutDealGoodsdtInfoList(paramMap);
				paramMap.put("paGroupCode", pa11stGoods.getPaGroupCode());
				
				//03-1.INSERT TGDS_ALCOUT_DEAL_GOODS_MAPP
				addPa11stNotExistsGoodsdtList(pa11stAlcoutDealGoodsdtList);
				
				//04. 수정대상 제휴OUT 딜 대표상품 고시정보 조회
				pa11stGoodsOffer = pa11stGoodsService.selectPa11stGoodsOfferList(paramMap);
				if(pa11stGoodsOffer.size() < 1){
					paramMap.put("code","430");
					paramMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + pa11stGoods.getGoodsCode()}));
					continue;
				}
				
				//05. 수정대상 제휴OUT 딜 상품 즉시할인쿠폰 프로모션
				//paPromoTargetList = paPromoTargetAlcoutDealGoodsInfoSetting(paramMap);
				paGoodsPriceApplyList = paPromoTargetAlcoutDealGoodsInfoSetting(pa11stAlcoutDealGoodsdtList);
				
				//06. 11st ConnectSetting
				alcoutDealgoodsdtInfoMap = pa11stAlcoutDealGoodsInfoSetting(pa11stAlcoutDealInfo, pa11stGoods, pa11stAlcoutDealGoodsdtList, pa11stGoodsOffer, paGoodsPriceApplyList);
				reqXml = (StringBuilder) alcoutDealgoodsdtInfoMap.get("reqXml");
				
				//07. 11st Connect - 상품수정
				connect11StDealGoodsModify(conn, pa11stGoods, reqXml, paramMap, apiInfo);
				
				//07-2. 11st Connect - 옵션 수정
				connect11StDealGoodsModify(conn, pa11stGoods, reqXml, paramMap, apiInfoOption);
				
				//08. 연동 예외 상품 관리  - 옵션가가 대표상품 판매가 -50%보다 작거나 100%보다 클 경우 예외처리
				optValGoodslist = (List<String>) alcoutDealgoodsdtInfoMap.get("optValGoodslist");
				for(String otpValgoodsCode : optValGoodslist){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("goodsCode"		, otpValgoodsCode);
					map.put("paCode"		, pa11stAlcoutDealInfo.get("PA_CODE"));
					map.put("alcoutDealCode", pa11stAlcoutDealInfo.get("ALCOUT_DEAL_CODE"));
					map.put("paStatus"		, "20"); //입점반려
					map.put("returnNote"	, "판매가 오류");
					map.put("modifyId"		, "PA11DEAL");
					map.put("modifyDate"	, DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					pa11stGoodsService.savePa11stAlcoutDealGoodsFailTx(map);
				}
				if(!"200".equals(paramMap.getString("code"))) continue;
				
				//09. updateTransTarget  = 0, TGDS_ALCOUT_DEAL_GOODS_MAPP.PA_STATUS = 30 or 50 
				updateAlcountInfo(paramMap, pa11stGoods);

				//10. Stock Check				pa11stGoods.getGoodsCode() -> paGoodsTransLog 관리용..
				ResponseEntity<?> responseMsg = alcoutDealGoodsStockList(request, pa11stAlcoutDealInfo.get("ALCOUT_DEAL_CODE").toString(), pa11stGoods.getGoodsCode(), pa11stGoods.getProductNo(), pa11stGoods.getPaCode(), "PA11DEAL");
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
					paramMap.put("code","500");
					paramMap.put("message",getMessage("errors.api.system")+" : goodsStockList");
					log.error("11번가 상품등록 재고번호 호출 에러");
					continue;
				}
			}
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			if (conn != null) 
				conn.disconnect();
			try{
				paramMap.put("resultCode"		, paramMap.getString("code"));
				paramMap.put("resultMessage"	, paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	private void addPa11stNotExistsGoodsdtList(List<HashMap<String, Object>> pa11stAlcoutDealGoodsdtList) throws Exception {
		List<HashMap<String, Object>> pa11stNotExistsGoodsdtList = new ArrayList<>();
		ParamMap paramMap = new ParamMap();
		for(HashMap<String, Object> goodsdt : pa11stAlcoutDealGoodsdtList) {

			paramMap.put("alcoutDealCode"	, goodsdt.get("ALCOUT_DEAL_CODE"));
			paramMap.put("goodsCode"		, goodsdt.get("GOODS_CODE"));
			paramMap.put("paCode"			, goodsdt.get("PA_CODE"));
			
			pa11stNotExistsGoodsdtList = pa11stGoodsService.selectPa11stNotExistsGoodsdtList(paramMap);
			
			for(HashMap<String, Object> pa11stNotExistsGoodsdt : pa11stNotExistsGoodsdtList) {
				pa11stNotExistsGoodsdt.put("alcoutDealCode"	, 	paramMap.get("alcoutDealCode"));
				pa11stNotExistsGoodsdt.put("paGoodsCode"	,	goodsdt.get("PA_GOODS_CODE"));
				pa11stNotExistsGoodsdt.put("paCode"			,	paramMap.get("paCode"));
				pa11stGoodsService.insertPa11stNotExistsGoodsdtTx(pa11stNotExistsGoodsdt);
			}
		}
	}
	
	private boolean chkMastDealGoods(Pa11stGoodsVO pa11stGoods , ParamMap paramMap) throws Exception {
		
		//01_1. 딜 대표 상품 NullCheck
		if(pa11stGoods == null){
			paramMap.put("code","404");
			paramMap.put("message",getMessage("pa.not_exists_process_list"));
			return false;
		}
		
		//01_2. 딜 대표 상품 ProductNo Check
		if("".equals(pa11stGoods.getProductNo()) || pa11stGoods.getProductNo() == null) {
			paramMap.put("code","404");
			paramMap.put("message",getMessage("pa.not_exists_process_list"));
			return false;
		}
		
		//01_3. 딜 대표 상품 기술서 Check
		if(pa11stGoods.getDescribeExt().equals("")){
			paramMap.put("code","420");
			paramMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + pa11stGoods.getGoodsCode()}));
			return false;
		}
		return true;
	}
	
	private String getDescribeInfo(Pa11stGoodsVO pa11stGoods, ParamMap paramMap) throws Exception{
		String strDescribeExt 	= "";
		long describeLength 	= 0;
		List<Pa11stGoodsVO> ps11stGoodsDescribeList = pa11stGoodsService.selectPa11stAlcoutDealGoodsDescribe(paramMap); //수정대상 기술서 조회
		
		for(Pa11stGoodsVO ps11stGoodsDescribe : ps11stGoodsDescribeList) {
			if(!("").equals(ps11stGoodsDescribe.getDescribeExt()) && ps11stGoodsDescribe.getDescribeExt() != null) {
				strDescribeExt +=  "<b><p style=\"font-size:15px\">" + ps11stGoodsDescribe.getGoodsName() + "</p></b>" + "<br />" + ps11stGoodsDescribe.getDescribeExt() + "<br />";
				describeLength += ps11stGoodsDescribe.getDescribeLen();
			}
		}
		
		if(describeLength > 6000){
			paramMap.put("code","415");
			paramMap.put("message","상품 기술서 길이가 6000자가 넘습니다. 상품코드 : " + pa11stGoods.getGoodsCode());
			strDescribeExt = "";
		}
		
		return strDescribeExt;
	}

	private void connect11StDealGoodsModify(HttpURLConnection conn , Pa11stGoodsVO pa11stGoods, StringBuilder reqXml , ParamMap paramMap , HashMap<String, String> apiInfo) throws Exception {
		if(reqXml == null) return;
		
		OutputStreamWriter out 	= null;
		double timeS 			= 0;
		double timeE 			= 0;
		String method			= apiInfo.get("method");
		Document doc 			= null;
		NodeList descNodes 		= null;	
		
		String paMediaCode = pa11stGoods.getPaCode().equals(Constants.PA_11ST_BROAD_CODE)? Constants.PA_11ST_BROAD : Constants.PA_11ST_ONLINE;
		conn = ComUtil.pa11stConnectionSetting(apiInfo, paMediaCode, method,"/"+pa11stGoods.getProductNo());
		paramMap.put("body", String.valueOf(reqXml));
		
		timeS = System.nanoTime();
		out = new OutputStreamWriter(conn.getOutputStream());
		out.write(String.valueOf(reqXml));
		out.flush();
		
		int respCode 	= conn.getResponseCode();
		String respMsg  = conn.getResponseMessage();
		log.info(" connect respCode : "+respCode +"  " + "connect respMsg  : "+respMsg );
		
		timeE = System.nanoTime();
		
		String responseTime = Double.toString((timeE-timeS)/1000000000);
		paramMap.put("responseTime"	, responseTime);
		paramMap.put("result"		, respMsg);
		
		pa11stAsyncController.insertPaRequestMap(paramMap);
		
		if(respCode == 200){
			// RESPONSE XML 		
			doc 		= ComUtil.parseXML(conn.getInputStream());
			descNodes 	= doc.getElementsByTagName("ClientMessage");
			conn.disconnect();

			for(int j=0; j<descNodes.getLength();j++){
		        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
		        	paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
		        }
		    }
			//전송관리 테이블 저장
			insertPaGoodsTransLog(paramMap);
			//updateAlcountInfo	 (paramMap, pa11stGoods);
		}
		
		paramMap.put("code"		,respCode);
		paramMap.put("message"	,respMsg);
		
	
	}
	
	private void updateAlcountInfo(ParamMap paramMap, Pa11stGoodsVO pa11stGoods) throws Exception{
		//String transTargetYn							= paramMap.getString("transTarget");
		String transTargetYn							= "0";
		String dateTime									= paramMap.getString("dateTime");
		HashMap<String, Object> pa11stAlcoutDealInfo 	= new HashMap<String, Object>();
		
		pa11stAlcoutDealInfo.put("alcoutDealCode", 	paramMap.getString("alcoutDealCode"));
		pa11stAlcoutDealInfo.put("paGoodsCode",	 	pa11stGoods.getProductNo());
		pa11stAlcoutDealInfo.put("paCode", 	 		paramMap.getString("paCode"));
		pa11stAlcoutDealInfo.put("paStatus", 		"30");// 입점완료
		pa11stAlcoutDealInfo.put("modifyId"	, 		"PA11DEAL");
		pa11stAlcoutDealInfo.put("modifyDate",		DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		pa11stGoodsService.savePa11stAlcoutDealTx(pa11stAlcoutDealInfo);
		
		pa11stGoods.setTransTargetYn(transTargetYn); // 옵션수정을 위해 타겟 유지
		pa11stGoods.setAlcoutDealCode(paramMap.getString("alcoutDealCode"));
		pa11stGoodsService.updateOutDealGoodsTarget(pa11stGoods);
		
	}
	
	private void insertPaGoodsTransLog(ParamMap paramMap) {
		try {
			String dateTime = paramMap.getString("dateTime");
			//전송관리 테이블 저장
			PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
			paGoodsTransLog.setGoodsCode	(paramMap.getString("goodsCode"));
			paGoodsTransLog.setPaCode		(paramMap.getString("paCode"));
			paGoodsTransLog.setItemNo		(paramMap.getString("productNo").isEmpty()?paramMap.getString("goodsCode"):paramMap.getString("productNo"));
			paGoodsTransLog.setRtnCode		(paramMap.getString("resultCode"));
			paGoodsTransLog.setRtnMsg		(paramMap.getString("message"));
			paGoodsTransLog.setSuccessYn	(paramMap.getString("resultCode").equals("200")==true?"1":"0");
			paGoodsTransLog.setProcDate		(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paGoodsTransLog.setProcId		("PA11DEAL");
			pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);			
		}catch (Exception e) {
			log.error(e.toString());
		}
	}
	
	public Map<String, Object>  getProductStatue(String paGoodsCode, String paCode) throws Exception { 
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn 			= null;
		String dateTime 				= systemService.getSysdatetimeToString();
		String prg_id 					= "IF_PA11STAPI_01_014";
		String request_type				= "GET";
		ParamMap paramMap 				= new ParamMap();
		Map<String, Object> resultMap   = new HashMap<String, Object>();
		
		try {

			paramMap.put("apiCode"		, prg_id);
			paramMap.put("broadCode"	, Constants.PA_11ST_BROAD_CODE);
			paramMap.put("onlineCode"	, Constants.PA_11ST_ONLINE_CODE);
			paramMap.put("startDate"	, dateTime);
			paramMap.put("paCode"		, paCode);
			
			Document doc = null;
			NodeList descNodes = null;
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			
			if(paCode.equals(Constants.PA_11ST_BROAD_CODE)){
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"/"+ paGoodsCode);
			} else {
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"/"+ paGoodsCode);
			}
			
			doc 		= ComUtil.parseXML(conn.getInputStream());
			descNodes 	= doc.getElementsByTagName("Product");
			
			for(int j=0; j<descNodes.getLength();j++){
		        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
		        	resultMap.put(node.getNodeName().trim(), node.getTextContent().trim());
		        }
		    }
			//rtnMsg = String.valueOf( resultMap.get("message")) ;
						
		}catch (Exception e) {
			//conn.disconnect();
			log.error(e.toString());
		}finally {
			conn.disconnect();
		}
		return resultMap;
	}
	
	private boolean checkRetention(String goodsCode , String paGoodsCode, String paCode) throws Exception {
		boolean  isRetentionYn 	= false;
		if(paGoodsCode ==null ) return isRetentionYn;
				
		try {
			//1)11번가 상품조회(단일) API 호출
			Map<String, Object> resultMap  	= getProductStatue(paGoodsCode, paCode);
			String message 					= String.valueOf(resultMap.get("message"));
			
			//2)상품조회 message Return값을 통해 리텐션 정책 확인
			if(message.indexOf("해당 상품의 정보를 찾을 수 없습니다") > -1) {
				isRetentionYn = true;
			}
			if(message.indexOf("11번가에서 상품사라짐") > -1) {
				isRetentionYn = true;
			}
			
			if(!isRetentionYn) return false;
			
			//3) 리텐션 정책으로 데이터가 삭제됬기 때문에 입점 상태로 만들기 위해 데이터 초기화
			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode"	, goodsCode);
			paramMap.put("paCode"		, paCode);
			paramMap.put("paGroupCode"	, "01");
			paramMap.put("status"		, "U");
			paramMap.put("argModCase"	, "U");
			paramMap.put("paGoodsCode"	, paGoodsCode);
			paramMap.put("targetYn"		, "0");//11번가는 History성으로 TPARETENTIONGOODS 사용(단 colSellerStockCd 새로 채번할때 TPARETENTIONGOODS 사용)
			paramMap.put("memo"			, message);
			
			paCommonService.saveRetentionGoodsTx(paramMap);	
			
		}catch (Exception e) {
			log.error(e.toString());
			return true;
		}
		return isRetentionYn;
	}
	
	
}