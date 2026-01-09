package com.cware.api.patmon.controller;

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
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaTmonGoodsVO;
import com.cware.netshopping.domain.PaTmonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.patmon.goods.service.PaTmonGoodsService;
import com.cware.netshopping.patmon.util.PaTmonConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@Api(value="/patmon/goods", description="상품")
@Controller("com.cware.api.patmon.PaTmonGoodsController")
@RequestMapping(value="/patmon/goods")
public class PaTmonGoodsController extends AbstractController{

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "com.cware.api.patmon.PaTmonCommonController")
	private PaTmonCommonController paTmonCommonController;
	
	@Resource(name = "patmon.goods.paTmonGoodsService")
	private PaTmonGoodsService paTmonGoodsService;
	
	@Autowired
	private PaTmonAsyncController paTmonAsyncController;
	
	@Autowired
	private PaTmonConnectUtil paTmonConnectUtil;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "상품등록", notes = "상품등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   //@ApiResponse(code = 410, message = "이미 등록된 상품입니다."),
			   //@ApiResponse(code = 420, message = "기술서를 입력하세요."),
			   @ApiResponse(code = 430, message = "정보고시를 입력하세요."),
			   @ApiResponse(code = 440, message = "상품의 출고지/회수지 담당을 확인하세요"),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/legacy-goods-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsInsert(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = true) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = true) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_TMON_PROC_ID) String procId) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		Map<String, Object>	map = new HashMap<String, Object>();
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = "";
		
		ResponseEntity<?> responseMsg = null;
		PaTmonGoodsVO paTmonGoods = null;
		List<PaGoodsOfferVO> paTmonGoodsOffer = null;
		List<PaGoodsdtMapping> goodsdtMapping = null;
	    List<PaPromoTarget> paPromoTargetList = null;
	    List<PaEntpSlip> entpSlipList = null;
	    PaEntpSlip entpSlip = null;
	    HashMap<String, Object> describeData	= null;
	    
	    log.info("===== 티몬상품등록 API Start =====");	
		String prg_id = "IF_PATMONAPI_01_001";
		dateTime = systemService.getSysdatetimeToString();
		
		try {
			log.info("01.티몬상품등록 API 중복체크");
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			paramMap.put("goodsCode", goodsCode );
			paramMap.put("paGroupCode", Constants.PA_TMON_GROUP_CODE);
			paramMap.put("paCode", paCode);
			paramMap.put("modCase", "INSERT");
			
			log.info("02.티몬상품등록 API 출고지/회수지 등록");
			entpSlipList = paTmonGoodsService.selectPaTmonEntpSlip(paramMap);
			if(entpSlipList.size() > 0){
				for(int i=0; i<entpSlipList.size(); i++){
					try {
						entpSlip = entpSlipList.get(i);
						responseMsg = paTmonCommonController.entpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaAddrGb(), entpSlip.getPaCode());
						log.info("티몬 상품등록 API 출고지/회수지 등록 CODE CHECK :" + PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
						
						if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")) {
							apiInfoMap.put("code", "440");
							apiInfoMap.put("message", getMessage("pa.check_entp_slip_man_seq", new String[] {" 상품코드 : " + goodsCode}));
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
						}
						
					}catch(Exception e){
						log.info("출고지/회수지 등록처리 Exception : "+entpSlip.getEntpCode()+"|"+entpSlip.getEntpManSeq());
					}
				}
			}
			
			log.info("03.티몬상품등록 API 배송비 템플릿 등록");
			paTmonCommonController.entpCustShipCostInsert(request, paCode);
			
			log.info("04.티몬상품등록 API 상품정보  Select");
			paTmonGoods = paTmonGoodsService.selectPaTmonGoodsInfo(paramMap);
			
			if(paTmonGoods == null){
				apiInfoMap.put("code", "404");
				apiInfoMap.put("message", getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			
			describeData = paCommonService.selectDescData(paramMap);
			paTmonGoods.setDescribeExt(ComUtil.getClobToString(describeData.get("DESCRIBE_EXT")));
			
			paTmonGoodsOffer = paTmonGoodsService.selectPaTmonGoodsOfferList(paramMap);
			
			if(paTmonGoodsOffer.size() < 1){
				apiInfoMap.put("code", "430");
				apiInfoMap.put("message", getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			
			goodsdtMapping = paTmonGoodsService.selectPaTmonGoodsdtInfoList(paramMap);
			
			//프로모션정보 추가 예정
			paPromoTargetList = paTmonGoodsService.selectPaPromoTarget(paramMap);
			
			apiDataMap = makeGoodsInfo(paTmonGoods, paTmonGoodsOffer, goodsdtMapping, paPromoTargetList, paramMap.get("modCase").toString());
			
			log.info("05.티몬상품등록 API 호출");
			apiInfoMap.put("paCode", paCode);
	    	map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);	
	    	
    		paTmonAsyncController.insertPaGoodsTransLog(paTmonGoods, map);
    		
    		if(!"null".equals(String.valueOf(map.get("dealNo")))){
	    		HashMap<String, Object> dealNoMap = (HashMap<String, Object>) map.get("dealNo");
	    		String dealNo = dealNoMap.get(paTmonGoods.getPaCode() + "-" + paTmonGoods.getGoodsCode()).toString();
	    		paTmonGoods.setDealNo(dealNo);
	    		paTmonGoods.setPaStatus("30");
	    		paTmonGoods.setModifyId(procId);
	    		paTmonGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	 
	    		
	    		HashMap<String, Object> dealOptionNos = (HashMap<String, Object>) map.get("dealOptionNos");
	    		for(int i=0; i < goodsdtMapping.size(); i++) {
	    			String paOptionCode = goodsdtMapping.get(i).getPaCode().toString() + goodsdtMapping.get(i).getGoodsCode().toString() + goodsdtMapping.get(i).getGoodsdtCode().toString();
	    			goodsdtMapping.get(i).setPaOptionCode(dealOptionNos.get(paOptionCode).toString());
	    		}
	    		
	    		rtnMsg = paTmonGoodsService.savePaTmonGoodsTx(paTmonGoods, goodsdtMapping, paPromoTargetList);
	    		
	    		if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
	    			apiInfoMap.put("code", "500");
					apiInfoMap.put("message", rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
				} else {
					apiInfoMap.put("code", "200");
					apiInfoMap.put("message", "OK");
				}
	    	} else if (!"null".equals(String.valueOf(map.get("error")))) {
	    		
				apiInfoMap.put("message", String.valueOf(map.get("error")));
				paTmonGoods.setReturnNote(String.valueOf(map.get("error")));
				
				if((String.valueOf(map.get("error")).indexOf("금칙어") != -1) || (String.valueOf(map.get("error")).indexOf("배송템플릿") != -1)){
					paTmonGoods.setPaSaleGb("30");
					paTmonGoods.setPaStatus("20");					
				} else {
					paTmonGoods.setPaSaleGb("20");
					paTmonGoods.setPaStatus("20");
					
				}
                paTmonGoods.setReturnNote(ComUtil.subStringBytes(ComUtil.objToStr(map.get("error")), 500));
				
				rtnMsg = paTmonGoodsService.savePaTmonGoodsErrorTx(paTmonGoods);
				
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
	    			apiInfoMap.put("code", "500");
					apiInfoMap.put("message", rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
				}
			}
    		
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		log.info("===== 티몬상품등록 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "상품수정", notes = "상품수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 411, message = "존재하지 않는 상품입니다."),
			   @ApiResponse(code = 420, message = "기술서를 입력하세요."),
			   @ApiResponse(code = 430, message = "정보고시를 입력하세요."),
			   @ApiResponse(code = 440, message = "상품의 출고지 담당을 확인하세요"),
			   @ApiResponse(code = 441, message = "상품의 회수지 담당을 확인하세요"),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/legacy-goods-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = false) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_TMON_PROC_ID) String procId) throws Exception{
		log.info("===== 티몬 상품 수정 API Start =====");		
		ParamMap	apiInfoMap				= new ParamMap();
		ParamMap	paramMap				= new ParamMap();
		List<PaTmonGoodsVO> paTmonGoodsList = null;
		ResponseEntity<?> responseMsg		= null;
		List<PaEntpSlip> entpSlipList 		= null;
		String dateTime						= "";
		String prg_id						= "IF_PATMONAPI_01_002";
		PaEntpSlip entpSlip = null;
		HashMap<String, Object> describeData	= null;
		
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("paCode", paCode);
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("procId", procId);
		paramMap.put("modCase", "MODIFY");
		try {
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			log.info("02.티몬상품수정 API 출고지/회수지 등록");
			entpSlipList = paTmonGoodsService.selectPaTmonEntpSlip(paramMap);
			if(entpSlipList.size() > 0){
				for(int i=0; i<entpSlipList.size(); i++){
					try {
						entpSlip = entpSlipList.get(i);
						responseMsg = paTmonCommonController.entpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaAddrGb(), entpSlip.getPaCode());
						log.info("티몬 상품등록 API 출고지/회수지 등록 CODE CHECK :" + PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
						
						if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")) {
							apiInfoMap.put("code", "440");
							apiInfoMap.put("message", getMessage("pa.check_entp_slip_man_seq", new String[] {" 상품코드 : " + goodsCode}));
						}
						
					}catch(Exception e){
						log.info("출고지/회수지 등록처리 Exception : "+entpSlip.getEntpCode()+"|"+entpSlip.getEntpManSeq());
					}
				}
			}
			
			log.info("03.티몬상품수정 API 배송비 템플릿 등록");
			paTmonCommonController.entpCustShipCostInsert(request, paCode);
			
			log.info("04.티몬 상품수정 API 출고/회수지 수정");
			responseMsg = paTmonCommonController.entpSlipUpdate(request);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
				apiInfoMap.put("code","440");
				apiInfoMap.put("message",getMessage("pa.check_entp_slip_man_seq", new String[] {" 상품코드 : " + goodsCode}));
				log.info("code : "+ apiInfoMap.get("code"));
				log.info("Message : "+ apiInfoMap.get("message"));
			}
			  
			log.info("05.티몬 상품수정 API 판매상태변경");
			responseMsg = setGoodsSellState(request, goodsCode, paCode, procId);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
				apiInfoMap.put("code","500");
				apiInfoMap.put("message",getMessage("errors.api.system")+" : setGoodsSellState" + "("+PropertyUtils.describe(responseMsg.getBody()).get("message")+")");
				log.info("code : "+ apiInfoMap.get("code"));
				log.info("Message : "+ apiInfoMap.get("message"));
			}
			
			log.info("06.티몬 옵션 등록");
			responseMsg = GoodsOptionModify(request, goodsCode, paCode, procId);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
				apiInfoMap.put("code","500");
				apiInfoMap.put("message",getMessage("errors.api.system")+" : GoodsOptionModify" + "("+PropertyUtils.describe(responseMsg.getBody()).get("message")+")");
				log.info("code : "+ apiInfoMap.get("code"));
				log.info("Message : "+ apiInfoMap.get("message"));
			}			
			
			log.info("07.티몬 상품수정 API 수정 대상 조회");
			paTmonGoodsList = paTmonGoodsService.selectPaTmonGoodsInfoList(paramMap);
			
			if(paTmonGoodsList.size()==0){
				apiInfoMap.put("code","404");
				apiInfoMap.put("message",getMessage("pa.not_exists_process_list", new String[] {" 상품코드 : " + goodsCode}));
				apiInfoMap.put("resultMessage",apiInfoMap.getString("message"));

			}
			
			String url = apiInfoMap.get("url").toString();
			
			for(int i = 0; i < paTmonGoodsList.size(); i++) {
				ParamMap bodyMap = new ParamMap();
				ParamMap asyncMap = new ParamMap();
				PaTmonGoodsVO paTmonGoods = paTmonGoodsList.get(i);
				List<PaGoodsOfferVO> paTmonGoodsOffer = null;
				List<PaGoodsdtMapping> goodsdtMapping = null;
				List<PaPromoTarget> paPromoTargetList = null;
				
				try {
					paTmonGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paTmonGoods.setModifyId(procId);
					
					asyncMap.put("paBroad",  apiInfoMap.get("paBroad"));
					asyncMap.put("paOnline", apiInfoMap.get("paOnline"));
					asyncMap.put("url", url.replace("{vendorDealNo}", paTmonGoods.getPaCode() + "-" + paTmonGoods.getGoodsCode()));
					asyncMap.put("paCode", paTmonGoods.getPaCode());
					asyncMap.put("method", apiInfoMap.get("method"));
					asyncMap.put("siteGb", Constants.PA_TMON_PROC_ID);
					asyncMap.put("apiCode", prg_id);
					asyncMap.put("goodsCode", paTmonGoods.getGoodsCode());
					
					log.info("GOODS_CODE : " + paTmonGoods.getGoodsCode());
					//수정할 티몬 상품 코드가 존재하지 않을시 처리
					if(StringUtil.isEmpty(paTmonGoods.getDealNo())){
						apiInfoMap.put("code","411");
						apiInfoMap.put("message",getMessage("pa.not_exists_goods", new String[] {"상품코드 : " + goodsCode}));
						continue;
					}
										
					paramMap.put("paGroupCode", paTmonGoods.getPaGroupCode());
					paramMap.put("goodsCode", paTmonGoods.getGoodsCode());
					paramMap.put("paCode", paTmonGoods.getPaCode());
					
					describeData = paCommonService.selectDescData(paramMap);
					paTmonGoods.setDescribeExt(ComUtil.getClobToString(describeData.get("DESCRIBE_EXT")));
					//수정할 상품 상세설명 존재하지 않을시 처리
					if(StringUtil.isEmpty(paTmonGoods.getDescribeExt())){
						paramMap.put("code","420");
						paramMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
						continue;
					}
					
					//상품 정보고시 조회
					paTmonGoodsOffer = paTmonGoodsService.selectPaTmonGoodsOfferList(paramMap);
					if(paTmonGoodsOffer.size() < 1){
						apiInfoMap.put("code","430");
						apiInfoMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
						continue;
					}
					
					///옵션정보
					goodsdtMapping = paTmonGoodsService.selectPaTmonGoodsdtInfoList(paramMap);
					
					//프로모션정보 추가 예정
					paPromoTargetList = paTmonGoodsService.selectPaPromoTarget(paramMap);
					
					bodyMap = makeGoodsInfo(paTmonGoods, paTmonGoodsOffer, goodsdtMapping, paPromoTargetList, paramMap.get("modCase").toString());
					
					// 동기화로 처리
					paTmonAsyncController.asyncGoodsModify(request, asyncMap, bodyMap, paTmonGoods,goodsdtMapping, paPromoTargetList);
					Thread.sleep(120);
					
				} catch (Exception e) {
					log.info("상품수정 Exception : " + asyncMap.get("goodsCode").toString());
					log.info(e.getMessage()); 
				}
			}
			
			log.info("08.티몬 상품수정 API 재고수량 변경");
			responseMsg = setGoodsStockModify(request, goodsCode, paCode, procId);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
				apiInfoMap.put("code","500");
				apiInfoMap.put("message",getMessage("errors.api.system")+" : setGoodsStockModify" + "("+PropertyUtils.describe(responseMsg.getBody()).get("message")+")");
				log.info("code : "+ apiInfoMap.get("code"));
				log.info("Message : "+ apiInfoMap.get("message"));
			}
		} catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		
		log.info("===== 티몬상품수정 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	
	

	private ParamMap makeGoodsInfo(PaTmonGoodsVO paTmonGoods, List<PaGoodsOfferVO> paTmonGoodsOffer, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList, String API_GB) throws Exception {
		
		List<String> images				   = new ArrayList<String>();
		List<String> keywords			   = new ArrayList<String>();
		List<Object> offerArray			   = new ArrayList<Object>();
		List<Object> offerDetailArray	   = new ArrayList<Object>();
		List<Object> goodsDtArray		   = new ArrayList<Object>();
		List<String> sections			   = new ArrayList<String>();
		ParamMap tmonGoodsMap			   = new ParamMap();
		Map<String, Object> goodsDtMap	   = new HashMap<String, Object>();
		Map<String, Object> offerMap	   = new HashMap<String, Object>();
		Map<String, Object> offerDetailMap = null;
		
		double couponPrice	 = 0;
		Config imageUrl		 = systemService.getConfig("IMG_SERVER_1_URL");
		String image_address = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
		String goodsDescData = "";
		
		
		tmonGoodsMap.put("vendorDealNo", paTmonGoods.getPaCode() + "-" + paTmonGoods.getGoodsCode()); //딜번호(티몬전시단위)에 해당 연동업체측 키
		tmonGoodsMap.put("vendorPolicyNo", ConfigUtil.getString("TMON_VENDOR_POLICY_NO")); //티몬 오퍼레이터를 통해 받은 정책번호
		tmonGoodsMap.put("deliveryTemplateNo", paTmonGoods.getPaShipPolicyNo()); //배송템플릿번호 > Integer 형이라 확인 필요
		
		sections.add("선택");
		tmonGoodsMap.put("sections", sections); //상품선택정보들
//		tmonGoodsMap.put("managedTitle", paTmonGoods.getManagedTitle()); //티몬 내부에서 관리될 딜이름
		tmonGoodsMap.put("managedTitle", paTmonGoods.getManagedTitle().length() > 60 ? paTmonGoods.getManagedTitle().substring(0,57) + "..." : paTmonGoods.getManagedTitle());
		//tmonGoodsMap.put("salesEndDate", paTmonGoods.getSalesEndDate()); //판매종료일 > 날짜 형식 확인 필요  --> 제거
		
		
		//tmonGoodsMap.put("subcategoryNos", ""); //하위 카테고리 ??
		if("57000000".equals(paTmonGoods.getPaLgroup()) || "48000000".equals(paTmonGoods.getPaLgroup()) || "49000000".equals(paTmonGoods.getPaLgroup()) || 
		   "52000000".equals(paTmonGoods.getPaLgroup()) || "54000000".equals(paTmonGoods.getPaLgroup())) {
			//가전·컴퓨터 : 57000000, 뷰티 : 48000000, 생활·주방 : 49000000, 식품·건강 : 52000000, 출산·유아동 : 54000000 일 경우 필수.
			tmonGoodsMap.put("legalPermissionType", "NONE"); //법적허가/신고대상 상품코드
		}
		//tmonGoodsMap.put("importAdvertisementCertificate", ""); //광고심의필증 ??
			
		if(paTmonGoods.getCustOrdQtyCheckYn() == 1) {		//기간별 판매 체크가 되어 있으면 
			tmonGoodsMap.put("maxPurchaseQty", paTmonGoods.getTermOrderQty()); //1인당 최대 구매가능 수량
			if(paTmonGoods.getCustOrdQtyCheckTerm() > 0) {//0일로 넣었을경우 에러남 
				tmonGoodsMap.put("purchaseResetPeriod", paTmonGoods.getCustOrdQtyCheckTerm()); //1인당 최대 구매가능 수량  주기	
			}
		}else if(paTmonGoods.getOrderMaxQty() > 0 && paTmonGoods.getOrderMaxQty() < 1000) {
			tmonGoodsMap.put("maxPurchaseQty", paTmonGoods.getOrderMaxQty()); //1인당 최대 구매가능 수량
			tmonGoodsMap.put("purchaseResetPeriod", 1 ); //1인당 최대 구매가능 수량  주기			
		}else {
			tmonGoodsMap.put("maxPurchaseQty", 999 ); 	//  0 이거나 999보다 클경우 이슈 발생 
			tmonGoodsMap.put("purchaseResetPeriod", 7 ); 
		}
		
//		tmonGoodsMap.put("title", paTmonGoods.getTitle()); //판매용 메인 제목(딜명)
		tmonGoodsMap.put("title", paTmonGoods.getTitle().length() > 60 ? paTmonGoods.getTitle().substring(0,57) + "..." : paTmonGoods.getTitle()); //판매용 메인 제목(딜명)
		if("1".equals(paTmonGoods.getCollectYn())) {
			tmonGoodsMap.put("titleDecoration", "착불 | 행복한 쇼핑, SK스토아"); //판매용 제목 상단 홍보 문구(딜 홍보 문구)
		}else {
			tmonGoodsMap.put("titleDecoration", "행복한 쇼핑, SK스토아"); //판매용 제목 상단 홍보 문구(딜 홍보 문구)
		}
		
		
		images.add(image_address + paTmonGoods.getImageUrl() + paTmonGoods.getImageP());
		if(StringUtil.isNotEmpty(paTmonGoods.getImageAP())) {
			images.add(image_address + paTmonGoods.getImageUrl() + paTmonGoods.getImageAP());
		}
		if(StringUtil.isNotEmpty(paTmonGoods.getImageBP())) {
			images.add(image_address + paTmonGoods.getImageUrl() + paTmonGoods.getImageBP());
		}
		if(StringUtil.isNotEmpty(paTmonGoods.getImageCP())) {
			images.add(image_address + paTmonGoods.getImageUrl() + paTmonGoods.getImageCP());
		}
		if(StringUtil.isNotEmpty(paTmonGoods.getImageDP())) {
			images.add(image_address + paTmonGoods.getImageUrl() + paTmonGoods.getImageDP());
		}
		tmonGoodsMap.put("mainImages", images); //사이트 노출용 메인이미지들
		//tmonGoodsMap.put("homeRecommendedImage", ""); //홈추천 이미지 ??
		
		String goodsCom = "";
		
		goodsCom = (!ComUtil.NVL(paTmonGoods.getGoodsCom()).equals("")) ? ("<div style=\"line-height: 2.0em; font-family: 'NanumBarunGothic'; font-size: 19px;\"><div><h4>&middot;&nbsp;상품구성<h4><pre>" + paTmonGoods.getGoodsCom() + "</pre></div></div>") : "";
		
		if("".equals(paTmonGoods.getCollectImage()) || paTmonGoods.getCollectImage() == null) {
			goodsDescData = ("<div align='center'><img alt='' src='" + paTmonGoods.getTopImage() + "' /><br /><br /><br />"	//상단 이미지 
					+ goodsCom	//상품 구성
					+ paTmonGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='"	//기술서 
					+ paTmonGoods.getBottomImage() + "' /></div>");	//하단 이미지
		} else {
			goodsDescData = ("<div align='center'><img alt='' src='" + paTmonGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + paTmonGoods.getCollectImage() + "' /><br /><br /><br />"	//상단 이미지 + 착불 이미지
					+ goodsCom	//상품 구성
					+ paTmonGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='"	//기술서 
					+ paTmonGoods.getBottomImage() + "' /></div>");	//하단 이미지
		}
		
		if(!ComUtil.NVL(paTmonGoods.getNoticeExt()).equals("")) {
			goodsDescData = paTmonGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + goodsDescData;
		}
		
		tmonGoodsMap.put("detailContents", goodsDescData); //사이트 노출용 딜상세 내용
		tmonGoodsMap.put("additionalInput", false); //주문시 부가정보 입력받을지 여부
		//tmonGoodsMap.put("additionalInputTitle", ""); //주문시 입력받을 부가정보
		
		if("0082".equals(paTmonGoods.getOriginCode())) {
			tmonGoodsMap.put("originCountryType", "O");	// 원산지 표기 방식 (한국)
		}else if("9999".equals(paTmonGoods.getOriginCode())) {
			tmonGoodsMap.put("originCountryType", "D");	// 원산지 표기 방식 (상세설명 참조)
		}else {
			tmonGoodsMap.put("originCountryType", "E");	// 원산지 표기 방식 (직접입력)
			tmonGoodsMap.put("originCountryDetail", paTmonGoods.getOriginName());
		}
		
		for(int i = 0; i < paTmonGoodsOffer.size(); i++) {
			offerDetailMap = new HashMap<String, Object>();
			offerDetailMap.put("section", paTmonGoodsOffer.get(i).getPaOfferCodeName());
			offerDetailMap.put("description", paTmonGoodsOffer.get(i).getPaOfferExt());
			offerDetailArray.add(offerDetailMap);
		}
		offerMap.put("productType", paTmonGoodsOffer.get(0).getPaOfferTypeName());
		offerMap.put("productInfos", offerDetailArray);
		offerArray.add(offerMap);
		tmonGoodsMap.put("productInfos", offerArray); //상품정보제공고시 정보들
		
		tmonGoodsMap.put("kcAuthSubmitType", "X"); //KC인증 제출방식
		//tmonGoodsMap.put("kcAuths", ""); //KC인증들
		//임시로 하드코딩.
		tmonGoodsMap.put("deliveryCorp", paTmonGoods.getDelyGb()); //배송사(택배사)
		tmonGoodsMap.put("search", true); //검색 노출 여부
		keywords.add("sk스토아");
		keywords.add("SK스토아");
		keywords.add("SKstoa");
		keywords.add("skstoa");
		keywords.add("에스케이스토아");
		tmonGoodsMap.put("keywords", keywords); //검색 키워드		
		tmonGoodsMap.put("priceComparison", true); //가격비교 노출동의 여부
		//tmonGoodsMap.put("dealProductStatus", "NEW"); //상품상태 메타정보
		//tmonGoodsMap.put("dealSellMethod", ""); //판매방식 메타정보
		
		
		if("INSERT".equals(API_GB)) {	// 상품 수정에서 제외되는 항목 
			tmonGoodsMap.put("productType", paTmonGoods.getProductType()); //배송상품 유형 타입
			tmonGoodsMap.put("categoryNo", paTmonGoods.getCategoryNo()); //티몬 카테고리 번호 > Long 형이라 확인 필요
			
			//고객 상품 수령 시 재판매가치가 없어지는 상품유형인 냉장/냉동/신선 식품(DP01), 화물/설치(DP04), 주문제작(DP05)를 선택한 경우에만 '아니오' 설정이 가능합니다
			//단순변심환불여부 사용여부 확인필요
	//		if("DP01".equals(paTmonGoods.getProductType()) || "DP04".equals(paTmonGoods.getProductType()) || "DP05".equals(paTmonGoods.getProductType())) {
	//			tmonGoodsMap.put("simpleRefundAble", false); //단순변심환불 가능 여부
	//			tmonGoodsMap.put("simpleRefundNotAvailableReason", "단순 변심 환불 불가 상품입니다."); //단순변심환불 불가능 사유
	//		} else {
	//			tmonGoodsMap.put("simpleRefundAble", true); //단순변심환불 가능 여부
	//		}
			
			tmonGoodsMap.put("adultOnly", "1".equals(paTmonGoods.getAdultYn())?true:false);
			
		}
		
		
		tmonGoodsMap.put("brandName", paTmonGoods.getBrandName()); //브랜드이름
			
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
		
		for(int i = 0; i < goodsdtMapping.size(); i++) {
			goodsDtMap = new HashMap<String, Object>();
			List<String> dtSection = new ArrayList<String>();
			
			dtSection.add(goodsdtMapping.get(i).getGoodsdtInfo());
			goodsDtMap.put("vendorDealOptionNo", goodsdtMapping.get(i).getPaCode() + goodsdtMapping.get(i).getGoodsCode() + goodsdtMapping.get(i).getGoodsdtCode());
			goodsDtMap.put("sections", dtSection);
			goodsDtMap.put("salesPrice",(paTmonGoods.getSalePrice() - (paTmonGoods.getDcAmt() + paTmonGoods.getLumpSumDcAmt() + couponPrice) )); //프로모션 적용 필요
			
			if("INSERT".equals(API_GB)) {	// 상품 수정에서 제외되는 항목 
				goodsDtMap.put("stock", goodsdtMapping.get(i).getTransOrderAbleQty());
			}
			
			goodsDtArray.add(goodsDtMap);
		}
		
		tmonGoodsMap.put("dealOptions", goodsDtArray); //옵션들
		
		return tmonGoodsMap;
	}
	
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
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_TMON_PROC_ID) String procId) throws Exception{
	
		log.info("===== 티몬 상품 판매상태 변경 API Start =====");		
		ParamMap	apiInfoMap			  = new ParamMap();
		ParamMap	apiDataMap			  = new ParamMap();
		ParamMap	paramMap			  = new ParamMap();
		Map<String, Object> map			  = new HashMap<String, Object>() ;
		List<PaTmonGoodsVO> sellStateList = null;
		PaTmonGoodsVO sellStateTarget     = null;
		String rtnMsg					  = Constants.SAVE_SUCCESS;
		String dateTime					  = "";
		String prg_id					  = "IF_PATMONAPI_01_003";
		int	targetCount					  = 0;
		int	procCount					  = 0;
		String status					  = "";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("paCode", paCode);
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("procId", procId);
		
		try {
			
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			// 상품 판매 중지인지 시작인지 
			sellStateList = paTmonGoodsService.selectPaTmonSellStateList(paramMap);
			
			if(sellStateList == null || sellStateList.size() <= 0){
				apiInfoMap.put("code", "404");
				apiInfoMap.put("message", getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			
			targetCount = sellStateList.size();
			for(int i = 0; i < sellStateList.size(); i++) {
				
				sellStateTarget = sellStateList.get(i);
				apiInfoMap.put("paCode", sellStateTarget.getPaCode());
				
				if("30".equals(sellStateTarget.getPaSaleGb())) {
					status = "pause";
				}else {
					status = "resume";					
				}
				
				apiInfoMap.put("url", url.replace("{tmonDealNo}", sellStateList.get(i).getDealNo()).replace("{status}", status));
				
				map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);	
				
				if(!"null".equals(String.valueOf(map.get("error")))){					
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", map.get("error"));
					
				} else {
					log.info("티몬 단품 판매상태 변경 성공 GOODS_CODE : " + sellStateTarget.getGoodsCode());
					
					sellStateTarget.setModifyId(procId);
					sellStateTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					rtnMsg = paTmonGoodsService.updateTmonGoodsStatus(sellStateTarget);
					
					log.info("티몬 단품 판매상태 변경  저장 : "+ rtnMsg);
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						apiInfoMap.put("code","404");
						apiInfoMap.put("message",rtnMsg);
					} else {
						apiInfoMap.put("code","200");
						apiInfoMap.put("message","OK");
						procCount++;
					}
				}
			}
		} catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			if(targetCount != procCount){
				apiInfoMap.put("code","500");
				apiInfoMap.put("message", "대상건수:" + targetCount + ", 성공건수:" + procCount);
			}
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info("===== 티몬 상품 판매상태 변경 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "상품 재고수량 변경", notes = "상품 재고수량 변경", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goods-stock", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> setGoodsStockModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = false) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_TMON_PROC_ID) String procId) throws Exception{
	
		log.info("===== 티몬 상품 재고수량 변경 API Start =====");		
		ParamMap	apiInfoMap			  = new ParamMap();
		ParamMap	apiDataMap			  = null;
		ParamMap	paramMap			  = new ParamMap();
		Map<String,Object> map			  = new HashMap<String,Object> () ;
		List<PaTmonGoodsdtMappingVO>	GoodsStockList = null;
		List<PaTmonGoodsdtMappingVO> GoodsStockMapping = null;
		String rtnMsg					  = Constants.SAVE_SUCCESS;
		String dateTime					  = "";
		String prg_id					  = "IF_PATMONAPI_01_005";
		int	targetCount					  = 0;
		int	procCount					  = 0;
		Map<String,Object> dealOptions = null;
		List<Object> dealOptionsList = null;
		
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("paCode", paCode);
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("procId", procId);
		
		try {
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			GoodsStockList = paTmonGoodsService.selectPaTmonGoodsdtStockList(paramMap);
			
			if(GoodsStockList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			targetCount = GoodsStockList.size();
			
			for (int k = 0 ; k < GoodsStockList.size(); k ++) {
				PaTmonGoodsdtMappingVO paGoodsdtMapping = GoodsStockList.get(k);
				
				paramMap.put("goodsCode", paGoodsdtMapping.getGoodsCode());
				paramMap.put("paCode", paGoodsdtMapping.getPaCode());
				
				apiInfoMap.put("paCode", paGoodsdtMapping.getPaCode());
				apiInfoMap.put("url", url.replace("{tmonDealNo}", GoodsStockList.get(k).getDealNo()));
				
				GoodsStockMapping = paTmonGoodsService.selectPaTmonGoodsdtStockMappingList(paramMap);
				
				apiDataMap = new ParamMap();
				dealOptionsList = new ArrayList<Object>();
				
				for(int i = 0; i < GoodsStockMapping.size(); i++) {					
					dealOptions	  = new HashMap<String, Object>();					
					dealOptions.put("vendorDealOptionNo", GoodsStockMapping.get(i).getPaCode() + GoodsStockMapping.get(i).getGoodsCode() + GoodsStockMapping.get(i).getGoodsdtCode());
					dealOptions.put("stock", GoodsStockMapping.get(i).getTransOrderAbleQty());					
					dealOptionsList.add(dealOptions);
				}
				
				apiDataMap.put("dealOptions", dealOptionsList);
				
				map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);	
				
				if("null".equals(String.valueOf(map.get("error")))){
					for(int i=0; i < GoodsStockMapping.size(); i++) {
		    			String paOptionCode = GoodsStockMapping.get(i).getPaCode().toString() + GoodsStockMapping.get(i).getGoodsCode().toString() + GoodsStockMapping.get(i).getGoodsdtCode().toString();
		    			HashMap<String,Object> mapInfo = (HashMap<String, Object>) map.get(paOptionCode);
		    					    			
		    			GoodsStockMapping.get(i).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		    			GoodsStockMapping.get(i).setModifyId(procId);
		    			if("true".equals(String.valueOf(mapInfo.get("success")))) {
		    				GoodsStockMapping.get(i).setTransTargetYn("0");
		    			}
		    		}
					
					rtnMsg = paTmonGoodsService.updatePaTmonGoodsdtMappingQtyTx(GoodsStockMapping);
    				
    				if(!rtnMsg.equals("000000")){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("pa.not_exists_process_list"));
						continue;
					} else {
						procCount++;
						paramMap.put("code","200");
						paramMap.put("message","OK");
					}
				}else {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", map.get("error"));
				}
			}
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			if(targetCount != procCount){
				apiInfoMap.put("code","500");
				apiInfoMap.put("message", "대상건수:" + targetCount + ", 성공건수:" + procCount);
			}
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info("===== 티몬 상품 재고수량 변경 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "딜옵션 개별등록", notes = "딜옵션 개별등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/legacy-goods-modify-option", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> GoodsOptionModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = false) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_TMON_PROC_ID) String procId) throws Exception{

		log.info("===== 티몬 딜옵션 개별등록 API Start =====");		
		ParamMap	apiInfoMap			  = new ParamMap();
		ParamMap	apiDataMap			  = null;
		ParamMap	paramMap			  = new ParamMap();
		Map<String,Object> map			  = new HashMap<String,Object> () ;
		List<PaTmonGoodsVO> modifyOptionList = null;
		List<PaPromoTarget> paPromoTargetList = null;
		
		List<PaTmonGoodsdtMappingVO> GoodsAddedMapping = null;
		String rtnMsg					  = Constants.SAVE_SUCCESS;
		String dateTime					  = "";
		String prg_id					  = "IF_PATMONAPI_01_004";
		int	targetCount					  = 0;
		int	procCount					  = 0;
		Map<String,Object> dealOptions = null;
		List<Object> dealOptionsList = null;
		
		dateTime = systemService.getSysdatetimeToString();

		paramMap.put("paCode", paCode);
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("procId", procId);
		paramMap.put("modCase", "option");
		
		try {
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			modifyOptionList = paTmonGoodsService.selectPaTmonModifyOptionList(paramMap);
			
			if(modifyOptionList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			targetCount = modifyOptionList.size();
			
			for(int i = 0; i < modifyOptionList.size(); i++ ) {
				double couponPrice	 = 0;
				
				paramMap.put("paCode", modifyOptionList.get(i).getPaCode());
				paramMap.put("goodsCode", modifyOptionList.get(i).getGoodsCode());				
				
				apiInfoMap.put("paCode", modifyOptionList.get(i).getPaCode());
				apiInfoMap.put("url", url.replace("{vendorDealNo}", modifyOptionList.get(i).getPaCode() +"-"+modifyOptionList.get(i).getGoodsCode()));
				
				paPromoTargetList = paTmonGoodsService.selectPaPromoTarget(paramMap);	// 프로모션 조회
				GoodsAddedMapping = paTmonGoodsService.selectPaTmonGoodsdtAddedList(paramMap);
				
				apiDataMap = new ParamMap();
				dealOptionsList = new ArrayList<Object>();
				
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
				
				for(int j = 0; j < GoodsAddedMapping.size(); j++) {	
					
					List<String> dtSection = new ArrayList<String>();
					dtSection.add(GoodsAddedMapping.get(j).getGoodsdtInfo());
					
					dealOptions	= new HashMap<String, Object>();					
					dealOptions.put("vendorDealOptionNo", GoodsAddedMapping.get(j).getPaCode() + GoodsAddedMapping.get(j).getGoodsCode() + GoodsAddedMapping.get(j).getGoodsdtCode());
					dealOptions.put("stock", GoodsAddedMapping.get(j).getTransOrderAbleQty());
					//나중에 프로모션 적용 필요
					dealOptions.put("salesPrice",(modifyOptionList.get(i).getSalePrice() - (modifyOptionList.get(i).getDcAmt() + modifyOptionList.get(i).getLumpSumDcAmt() + couponPrice)));
					dealOptions.put("sections", dtSection);
					dealOptionsList.add(dealOptions);
				}
				
				apiDataMap.put("dealOptions", dealOptionsList);
				
				map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);	
				
				if("null".equals(String.valueOf(map.get("error")))){
					for(int j = 0; j < GoodsAddedMapping.size(); j++) {
						String paOptionCode = GoodsAddedMapping.get(j).getPaCode().toString() + GoodsAddedMapping.get(j).getGoodsCode().toString() + GoodsAddedMapping.get(j).getGoodsdtCode().toString();		
						
						GoodsAddedMapping.get(j).setPaOptionCode(map.get(paOptionCode).toString());
						GoodsAddedMapping.get(j).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						GoodsAddedMapping.get(j).setModifyId(procId);
						GoodsAddedMapping.get(j).setTransTargetYn("0");
		    		}
					rtnMsg = paTmonGoodsService.updatePaTmonGoodsdtMappingAddedTx(GoodsAddedMapping);
					
					if(!rtnMsg.equals("000000")){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("pa.not_exists_process_list"));
						continue;
					} else {
						procCount++;
						paramMap.put("code","200");
						paramMap.put("message","OK");
					}
				}else {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", map.get("error"));
				}
				
			}
			
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			if(targetCount != procCount){
				apiInfoMap.put("code","500");
				apiInfoMap.put("message", "대상건수:" + targetCount + ", 성공건수:" + procCount);
			}
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		log.info("===== 티몬 딜옵션 개별등록 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
}
