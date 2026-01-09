package com.cware.api.passg.controller;

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
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaSsgGoodsVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaSsgDisplayMapping;
import com.cware.netshopping.domain.model.PaSsgGoodsdtMapping;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.passg.goods.service.PaSsgGoodsService;
import com.cware.netshopping.passg.util.PaSsgConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/passg/goods", description="상품")
@Controller("com.cware.api.passg.PaSsgGoodsController")
@RequestMapping(value="/passg/goods")
public class PaSsgGoodsController extends AbstractController{
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "com.cware.api.passg.PaSsgCommonController")
	private PaSsgCommonController paSsgCommonController;

	@Resource(name = "passg.goods.paSsgGoodsService")
	private PaSsgGoodsService paSsgGoodsService;
	
	@Autowired
	private PaSsgAsyncController paSsgAsyncController;
	
	@Autowired
	private PaSsgConnectUtil paSsgConnectUtil;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@SuppressWarnings({ "unchecked" })
	@ApiOperation(value = "상품 정보 등록", notes = "상품 정보 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 410, message = "이미 등록된 상품입니다."),
			   @ApiResponse(code = 420, message = "기술서를 입력하세요."),
			   @ApiResponse(code = 430, message = "정보고시를 입력하세요."),
			   @ApiResponse(code = 440, message = "상품의 출고지,회수지를 확인하세요"),
			   @ApiResponse(code = 450, message = "상품의 농수산물 필수값을 확인하세요"),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/legacy-goods-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsInsert(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = true) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = true) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_SSG_PROC_ID) String procId) throws Exception{
		
		String dateTime = "";
		String rtnMsg						= Constants.SAVE_SUCCESS;
		ParamMap paramMap = new ParamMap();
		ParamMap apiInfoMap				= new ParamMap();
		ParamMap apiDataMap				= new ParamMap();
		Map<String, Object> result		= new HashMap<String, Object>();
		
		List<PaEntpSlip> entpSlipList = null;
		PaEntpSlip entpSlip = null;
		Map<String, Object> map				= new HashMap<String, Object>() ;
		ResponseEntity<?> responseMsg = null;
		PaSsgGoodsVO paSsgGoods = null;
		List<PaGoodsOfferVO> paSsgGoodsOffer = null;
		List<PaSsgGoodsdtMapping> goodsdtMapping = null;
		List<PaSsgDisplayMapping> ssgDisplayList = null;
		HashMap<String, Object> describeData	= null;
		HashMap<String, Object> SsgFoodData     = null;
		
		
		dateTime = systemService.getSysdatetimeToString();
		log.info("===== SSG 상품등록 API Start =====");	
		String prg_id = "IF_PASSGAPI_01_003";
		//String dateTime = systemService.getSysdatetimeToString();
		
		try {
			log.info(prg_id + " SSG 상품 정보 등록 - 01.프로그램 중복 실행 검사 [start]");
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			paramMap.put("goodsCode", goodsCode );
			paramMap.put("paGroupCode", Constants.PA_SSG_GROUP_CODE);
			paramMap.put("paCode", paCode);
			paramMap.put("modCase", "INSERT");
			
			log.info("SSG상품등록 API 출고지/회수지 등록");
			entpSlipList = paSsgGoodsService.selectPaSsgEntpSlip(paramMap);
			if(entpSlipList.size() > 0){
				for(int i=0; i<entpSlipList.size(); i++){
					try {
						entpSlip = entpSlipList.get(i);
						responseMsg = paSsgCommonController.ssgEntpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaCode());
						log.info("SSG 상품등록 API 출고지/회수지 등록 CODE CHECK :" + PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
						
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
			
			log.info("SSG상품등록 API 배송비 템플릿 등록");
			paSsgCommonController.entpCustShipCostInsert(request);
			
			log.info("SSG상품등록 API 상품정보  Select");
			paSsgGoods = paSsgGoodsService.selectPaSsgGoodsInfo(paramMap);
			
			if(paSsgGoods == null) {
				apiInfoMap.put("code", "404");
				apiInfoMap.put("message", getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			
			log.info("SSG 상품수정 API 기술서 조회 ");
			describeData = paCommonService.selectDescData(paramMap);
			if(StringUtil.isEmpty(describeData.get("DESCRIBE_EXT").toString())){
				apiInfoMap.put("code","420");
				apiInfoMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			paSsgGoods.setDescribeExt(ComUtil.getClobToString(describeData.get("DESCRIBE_EXT")));
			
			log.info("SSG상품등록 API 정보고시 조회");
			paSsgGoodsOffer = paCommonService.selectPaGoodsOfferList(paramMap);
			if(paSsgGoodsOffer.size() < 1){
				apiInfoMap.put("code", "430");
				apiInfoMap.put("message", getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			
			if("0000000019".equals(paSsgGoodsOffer.get(0).getPaOfferType()) || 
		       "0000000020".equals(paSsgGoodsOffer.get(0).getPaOfferType()) || 
		       "0000000021".equals(paSsgGoodsOffer.get(0).getPaOfferType())) {
				
				log.info("SSG상품등록 API 농산물 필수값 조회");
				paramMap.put("paOfferType", paSsgGoodsOffer.get(0).getPaOfferType());

				SsgFoodData = paSsgGoodsService.selectSsgFoodInfo(paramMap);
				if(SsgFoodData == null){
					apiInfoMap.put("code", "450");
					apiInfoMap.put("message", getMessage("pa.insert_ssg_food_info", new String[] {"상품코드 : " + goodsCode}));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
				}
			}
			
			log.info("SSG상품등록 API 단품정보 조회");
			goodsdtMapping = paSsgGoodsService.selectPaSsgGoodsdtInfoList(paramMap);
			
			log.info("SSG상품등록 API 전시카테고리 조회");
			ssgDisplayList = paSsgGoodsService.selectPaSsgDisplayList(paramMap);
						
			//전문만들기
			apiDataMap = makeGoodsInfo(paSsgGoods, paSsgGoodsOffer, goodsdtMapping, ssgDisplayList, paramMap.get("modCase").toString(), SsgFoodData);			
			
			log.info("SSG상품등록 API 호출");
			apiInfoMap.put("paCode", paCode);
	    	map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
			
	    	result = (HashMap<String,Object>)map.get("result");
	      	
	    	result.put("modCase", "insert");	    	
	    	
			if(result.get("resultCode").toString().equals("00")) {
				paSsgGoods.setItemId(result.get("itemId").toString());
				paSsgGoods.setPaStatus("30");
				//입점 후 상태는 MD승인요청상태
				paSsgGoods.setProcStatCd("10");
				paSsgGoods.setModifyId(procId);
				paSsgGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				
				List<Map<String, Object>> uitems  = new ArrayList<Map<String,Object>>();
				uitems = (List<Map<String, Object>>)(result.get("uitems"));
									
				List<Map<String, Object>> uitem  = new ArrayList<Map<String,Object>>();
				
				//단품의 개수 여러개 -> 리스트 | 단품 개수 한 개 -> 맵   
				if(uitems.get(0).get("uitem") instanceof Map<?, ?>) {
					uitem.add((Map<String, Object>)(uitems.get(0).get("uitem")));
				}else {
					uitem = (List<Map<String, Object>>)(uitems.get(0).get("uitem"));
				}
				
				for(int i=0; i<uitem.size(); i++) {
					for(int j=0; j<goodsdtMapping.size(); j++) {
						String tempGoodsdtCode = goodsdtMapping.get(j).getPaCode() + goodsdtMapping.get(j).getGoodsCode() + goodsdtMapping.get(j).getGoodsdtCode() + goodsdtMapping.get(j).getGoodsdtSeq();
								
						if(tempGoodsdtCode.equals(uitem.get(i).get("tempUitemId").toString())) {
							goodsdtMapping.get(j).setPaOptionCode(uitem.get(i).get("uitemId").toString());
							goodsdtMapping.get(j).setTransOrderAbleQty(uitem.get(i).get("baseInvQty").toString());
							continue;
						}
					}
				}
				
				rtnMsg = paSsgGoodsService.savePaSsgGoodsTx(paSsgGoods, goodsdtMapping);
				
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
	    			apiInfoMap.put("code", "500");
					apiInfoMap.put("message", rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
				} else {
					apiInfoMap.put("code", "200");
					apiInfoMap.put("message", "OK");
				}
				
			} else if(result.get("resultCode").toString().equals("99") && 
					(result.get("resultDesc").toString().contains("금칙어") || result.get("resultDesc").toString().contains("전시카테고리는")
					|| result.get("resultDesc").toString().contains("동일한") || result.get("resultDesc").toString().contains("상품고시") 
					|| result.get("resultDesc").toString().contains("인증항목") || result.get("resultDesc").toString().contains("중복 된 옵션")
					|| result.get("resultDesc").toString().contains("지원하지 않는 문자") || result.get("resultDesc").toString().contains("이미지")
					|| result.get("resultDesc").toString().contains("해당 카테고리에 등록할 수 없는 과세구분") || result.get("resultDesc").toString().contains("입력하신 판매가가"))){
				// 금칙어 처리 
				String resultDesc = result.get("resultDesc").toString();
				int idx = resultDesc.indexOf("BizMsgException");
				String returnNote = resultDesc.substring(idx);
				paSsgGoods.setReturnNote(returnNote);
				paSsgGoods.setModifyId(procId);
				paSsgGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paSsgGoods.setPaSaleGb("30");
				paSsgGoods.setPaStatus("20");
				
				rtnMsg = paSsgGoodsService.savePaSsgGoodsErrorTx(paSsgGoods);
				
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
	    			apiInfoMap.put("code", "500");
					apiInfoMap.put("message", rtnMsg +" || "+ returnNote );
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
				} else {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", returnNote);
				}
				
			} else {
				//반려처리
				apiInfoMap.put("message", "[" + paSsgGoods.getGoodsCode() + "] " + String.valueOf(result.get("resultDesc")));
				apiInfoMap.put("code", "500");
			}
			
			paSsgAsyncController.insertPaGoodsTransLog(paSsgGoods, result);
			
		} catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}

	private ParamMap makeGoodsInfo(PaSsgGoodsVO paSsgGoods, List<PaGoodsOfferVO> paSsgGoodsOffer, List<PaSsgGoodsdtMapping> goodsdtMapping, List<PaSsgDisplayMapping> ssgDisplayList, String modCase, HashMap<String, Object> SsgFoodData) throws Exception {
		
		ParamMap ssgGoodsMap			   = new ParamMap();
		Map<String,Object> insertItem	   = new HashMap<String, Object>();
				
		Config imageUrl		 = systemService.getConfig("IMG_SERVER_1_URL");
		String image_address = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
		String goodsDescData = "";
		
		insertItem.put("itemNm", paSsgGoods.getGoodsName());		// 상품명
		insertItem.put("brandId", paSsgGoods.getBrandId());
		insertItem.put("stdCtgId", paSsgGoods.getStdCtgId());
		
		Map<String, Object> siteMap		= new HashMap<String, Object>();
		List<Map<String, Object>> siteList	= new ArrayList<Map<String,Object>>();
		
		int siteCnt = 0;
		for(int i=0; i < ssgDisplayList.size(); i++) {
			if(!"6005".equals(ssgDisplayList.get(i).getSiteNo())) {
				Map<String, Object> site		= new HashMap<String, Object>();
				site.put("siteNo", ssgDisplayList.get(i).getSiteNo());
				site.put("sellStatCd", "20");
				
				siteList.add(site);
				siteCnt++;
			}
		}
		//이마트, 신세계몰 카테고리 없을 경우 전부 연동하는걸로 생각하면됨.
		if(siteCnt < 1) {
			Map<String, Object> site		= new HashMap<String, Object>();
			site.put("siteNo", "6001");
			site.put("sellStatCd", "20");
			
			siteList.add(site);
			
			site		= new HashMap<String, Object>();
			site.put("siteNo", "6004");
			site.put("sellStatCd", "20");
			
			siteList.add(site);
		}
		siteMap.put("site", siteList);
		insertItem.put("sites", siteMap);
		
		insertItem.put("itemChrctDivCd", "10"); 	// 상품 특성 구분 코드 -  일반 : 10으로 하드코딩  
		insertItem.put("exusItemDivCd", "10");		// 전용 상품 구분 코드 - 일반 : 10 
		insertItem.put("exusItemDtlCd", "10");		// 전용 상품 상세 코드 - 일반 : 10 
		insertItem.put("dispAplRngTypeCd", "10");		// 전시 적용 범위 유형 코드 - 10 : 전체 (모바일 + PC)
		
		insertItem.put("itemMngPropClsId", paSsgGoodsOffer.get(0).getPaOfferType());

		/*농산물, 수산물, 축산물 필수값 세팅 */
		if(SsgFoodData != null) {
			insertItem.put("sellTotCapa", SsgFoodData.get("SELLTOTCAPA"));		// 판매 총 용량
			insertItem.put("sellUnitCapa", SsgFoodData.get("SELLUNITCAPA"));	// 판매 단위 용량
			insertItem.put("sellUnitQty", SsgFoodData.get("SELLUNITQTY"));		// 판매 단위 수량
			insertItem.put("sellCapaUnitCd", SsgFoodData.get("COMMCD"));		// 판매 용량 단위 코드
		}
		
		Map<String, Object> ItemMngAttrMap		= new HashMap<String, Object>();
		List<Map<String, Object>> itemMngAttrList	= new ArrayList<Map<String,Object>>();
		
		for(int i = 0; i < paSsgGoodsOffer.size(); i++) {// 정보고시 
			Map<String, Object> itemMngAttr		= new HashMap<String, Object>();
			itemMngAttr.put("itemMngPropId", paSsgGoodsOffer.get(i).getPaOfferCode());
			if("40".equals(paSsgGoodsOffer.get(i).getIptMthdCd()) || "50".equals(paSsgGoodsOffer.get(i).getIptMthdCd()) || "70".equals(paSsgGoodsOffer.get(i).getIptMthdCd())) {
				//Y,N으로 처리해야됨.
				if("0000000008".equals(paSsgGoodsOffer.get(i).getPaOfferCode()) && !"2000000078".equals(paSsgGoods.getOrplcId())) {
				//수입여부(0000000008)
					itemMngAttr.put("itemMngCntt", "Y".equals(paSsgGoods.getImportedYn())?"Y":"N");	
				}else{
					itemMngAttr.put("itemMngCntt", "N");	
				}
			} else if("30".equals(paSsgGoodsOffer.get(i).getIptMthdCd())){
				itemMngAttr.put("itemMngCntt", paSsgGoods.getOrplcId());	
			} else {
				itemMngAttr.put("itemMngCntt", paSsgGoodsOffer.get(i).getPaOfferExt());	
			}
			
			itemMngAttrList.add(itemMngAttr);
			
			
			if("0000000008".equals(paSsgGoodsOffer.get(i).getPaOfferCode()) && "Y".equals(paSsgGoods.getImportedYn())) {
			
				if("0000000023".equals(paSsgGoodsOffer.get(i).getPaOfferType())){
					//수입여부(0000000008)이고 Y 일 경우 수입자(0000000009) 필수 입력 필요
					itemMngAttr	= new HashMap<String, Object>();
					itemMngAttr.put("itemMngPropId", "0000000195");
					itemMngAttr.put("itemMngCntt", "상세설명참조");
					itemMngAttrList.add(itemMngAttr);
					itemMngAttr	= new HashMap<String, Object>();
					itemMngAttr.put("itemMngPropId", "0000000196");
					itemMngAttr.put("itemMngCntt", "1000000990".equals(paSsgGoods.getOrplcId()) ? "1000000000" : paSsgGoods.getOrplcId()); //제조국 : "기타"일 경우 "상세설명참조" 11/28 임시
					itemMngAttrList.add(itemMngAttr);
				}else {
					//수입여부(0000000008)이고 Y 일 경우 수입자(0000000009) 필수 입력 필요
					itemMngAttr	= new HashMap<String, Object>();
					itemMngAttr.put("itemMngPropId", "0000000009");
					itemMngAttr.put("itemMngCntt", "상세설명참조");
					itemMngAttrList.add(itemMngAttr);
					
					//수입여부(0000000008)이고 Y 일 경우 식품위생법에 따른 수입신고(0000000186) 필수 입력 필요
					if("0000000017".equals(paSsgGoodsOffer.get(i).getPaOfferType())) {
						itemMngAttr	= new HashMap<String, Object>();
						itemMngAttr.put("itemMngPropId", "0000000186");
						itemMngAttr.put("itemMngCntt", "20");
						itemMngAttrList.add(itemMngAttr);
					}	
				}
			}
			
		}
		ItemMngAttrMap.put("itemMngAttr", itemMngAttrList);
		insertItem.put("itemMngAttrs", ItemMngAttrMap);
		
		insertItem.put("manufcoNm", paSsgGoods.getManufcoNm());// 제조사명
//		insertItem.put("prodManufCntryId", "4000000302".equals(paSsgGoods.getOrplcId()) ? "2000000078" : paSsgGoods.getOrplcId()); //제조국
		insertItem.put("prodManufCntryId", "1000000990".equals(paSsgGoods.getOrplcId()) ? "1000000000" : paSsgGoods.getOrplcId()); //제조국 : "기타"일 경우 "상세설명참조" 11/28 임시
		
		Map<String, Object> dispCtgMap		= new HashMap<String, Object>();
		List<Map<String, Object>> dispCtgList	= new ArrayList<Map<String,Object>>();
		for(int i=0; i < ssgDisplayList.size(); i++) {
			Map<String, Object> dispCtg		= new HashMap<String, Object>();
			dispCtg.put("siteNo", ssgDisplayList.get(i).getSiteNo());
			dispCtg.put("dispCtgId", ssgDisplayList.get(i).getDispCtgId());
			
			dispCtgList.add(dispCtg);
		}
		dispCtgMap.put("dispCtg", dispCtgList);
		insertItem.put("dispCtgs", dispCtgMap);
		
		insertItem.put("dispStrtDts", paSsgGoods.getSsgSaleStartDate());
		insertItem.put("dispEndDts", paSsgGoods.getSsgSaleEndDate());		
		insertItem.put("srchPsblYn", "Y");
		insertItem.put("itemSrchwdNm", "SK스토아,에스케이스토아,skstoa,SKSTOA");
		insertItem.put("minOnetOrdPsblQty", paSsgGoods.getOrderMinQty());		// 최소 1회 주문 가능 수량
		//최대주문가능수량이 1일 주문가능수량보다 클 수 없다. 999개로 세팅
		insertItem.put("maxOnetOrdPsblQty", Long.parseLong(paSsgGoods.getTermOrderQty())<paSsgGoods.getOrderMaxQty()?Long.parseLong(paSsgGoods.getTermOrderQty()):paSsgGoods.getOrderMaxQty());// 최대 1회 주문 가능 수량
		insertItem.put("max1dyOrdPsblQty", paSsgGoods.getTermOrderQty());// 1일 주문 가능 수량
		insertItem.put("adultItemTypeCd", "1".equals(paSsgGoods.getAdultYn())?"10":"90");
		
		//임시
		insertItem.put("hriskItemYn", "N");	//고 위험 상품 여부 
		insertItem.put("nitmAplYn", "N");	//신 상품 적용 여부	
		insertItem.put("buyFrmCd", "60");	//매입 형태 코드
		//insertItem.put("txnDivCd", "10");
		insertItem.put("txnDivCd", "1".equals(paSsgGoods.getTaxYn())?"10":"20");	//과세 구분 코드
		insertItem.put("prcMngMthd", "1");
		
		// 공급가가 어떤 공급가를 넣어야 하는지 우리쪽에서 받은 공급가는 넣을필요 없을꺼같은데. 마진율 ....?
		Map<String, Object> uitemPrcMap	= new HashMap<String, Object>();
		Map<String, Object> uitemPrc	= new HashMap<String, Object>();
		uitemPrc.put("splprc", paSsgGoods.getSalePrice() - paSsgGoods.getDcAmt() - paSsgGoods.getLumpSumDcAmt() - paSsgGoods.getPromoDcAmt());// 공급가
		uitemPrc.put("sellprc", paSsgGoods.getSalePrice() - paSsgGoods.getDcAmt() - paSsgGoods.getLumpSumDcAmt() - paSsgGoods.getPromoDcAmt());// 판매가
		uitemPrc.put("mrgrt", paSsgGoods.getFeeRate());// 마진율 
		uitemPrcMap.put("uitemPrc", uitemPrc);
		insertItem.put("salesPrcInfos", uitemPrcMap);
		
		insertItem.put("invMngYn", "Y");			// 재고 관리 여부 
		insertItem.put("invQtyMarkgYn", "N");		// 재고 수량 표기 여부 
		insertItem.put("itemSellTypeCd", "20");		// 상품판매유형코드 
		insertItem.put("itemSellTypeDtlCd", "10");	// 상품판매유형상세코드  
		
		Map<String, Object> uitemMap		   = new HashMap<String, Object>();
		List<Map<String, Object>> uitemList	= new ArrayList<Map<String,Object>>();
		Map<String, Object> uitem = null;
		
		Map<String, Object> uitemPrc20Map	= new HashMap<String, Object>();
		List<Map<String, Object>> uitemPrc20List	= new ArrayList<Map<String,Object>>();
		Map<String, Object> uitemPrc20	= null;
		
		for(int i = 0; i < goodsdtMapping.size(); i++) {// 단품 
			uitem = new HashMap<String, Object>();
			
			if(goodsdtMapping.get(i).getPaOptionCode() == null) {
				uitem.put("tempUitemId", goodsdtMapping.get(i).getPaCode() + goodsdtMapping.get(i).getGoodsCode() + goodsdtMapping.get(i).getGoodsdtCode() + goodsdtMapping.get(i).getGoodsdtSeq());
			} else {
				uitem.put("uitemId", goodsdtMapping.get(i).getPaOptionCode());
			}
			
			uitem.put("uitemOptnTypeNm1", "색상/사이즈/형태/무늬");
			uitem.put("uitemOptnNm1", goodsdtMapping.get(i).getGoodsdtInfo());
			
			if(ComUtil.objToInt(goodsdtMapping.get(i).getTransOrderAbleQty()) >= 9999) {
				uitem.put("baseInvQty", 9998);
			} else {
				uitem.put("baseInvQty", ComUtil.objToInt(goodsdtMapping.get(i).getTransOrderAbleQty()));				
			}
			
			uitem.put("useYn", "1".equals(goodsdtMapping.get(i).getUseYn()) ? "Y":"N");
			
			//uitem.put("sellStatCd", "1".equals(goodsdtMapping.get(i).getUseYn())?"20":"80");
							
			uitemList.add(uitem);
				
		}
		
		for(int i = 0; i < goodsdtMapping.size(); i++) {// 단품 
			
			uitemPrc20 = new HashMap<String, Object>();
				
			if(goodsdtMapping.get(i).getPaOptionCode() == null) {
				uitemPrc20.put("tempUitemId", goodsdtMapping.get(i).getPaCode() + goodsdtMapping.get(i).getGoodsCode() + goodsdtMapping.get(i).getGoodsdtCode() + goodsdtMapping.get(i).getGoodsdtSeq());
			} else {
				uitemPrc20.put("uitemId", goodsdtMapping.get(i).getPaOptionCode());
			}
			
			uitemPrc20.put("splprc",paSsgGoods.getSalePrice() - paSsgGoods.getDcAmt() - paSsgGoods.getLumpSumDcAmt() - paSsgGoods.getPromoDcAmt());		// 공급가
			uitemPrc20.put("sellprc",paSsgGoods.getSalePrice() - paSsgGoods.getDcAmt() - paSsgGoods.getLumpSumDcAmt() - paSsgGoods.getPromoDcAmt());		// 판매가
			uitemPrc20.put("mrgrt",paSsgGoods.getFeeRate());			// 마진율
			uitemPrc20List.add(uitemPrc20);
		}
		
		uitemMap.put("uitem", uitemList);
		insertItem.put("uitems", uitemMap);
		uitemPrc20Map.put("uitemPrc", uitemPrc20List);
		insertItem.put("uitemPluralPrcs", uitemPrc20Map);
		
		insertItem.put("shppItemDivCd", paSsgGoods.getShppItemDivCd());// 배송상품 구분 코드 ( 01 : 일반 , 03 : 설치 유료, 02 : 주문제작 만 해당되는데 
		//AS-IS
		//배송상품 구분이 일반이 아닌 상품은 반품교환불가여부[retExchPsblYn]를 N로만 설정 가능합니다.
		//insertItem.put("retExchPsblYn", "01".equals(paSsgGoods.getShppItemDivCd())?"Y":"N");//반품 교환 가능 여부
		//TO-BE
		//반품,교환 불가한 카테고리가 존재. TPAGOODSKINDS > FOOD_YN = 1으로 관리. 이 경우 반품 교환 불가처리
		//insertItem.put("retExchPsblYn", "1".equals(paSsgGoods.getNoReturnYn())?"N":"Y");
		if("1".equals(paSsgGoods.getNoReturnYn()) || "03".equals(paSsgGoods.getShppItemDivCd()) || "05".equals(paSsgGoods.getShppItemDivCd())) {
			insertItem.put("retExchPsblYn", "N");
		}else {
			insertItem.put("retExchPsblYn", "Y");
		}
		
		insertItem.put("shppMainCd", "41");		//배송 주체 코드  31 : 자사창고 , 32: 업체창고, 41 : 협력업체 
		insertItem.put("shppMthdCd", "20"); 
		insertItem.put("mareaShppYn", "N");
		insertItem.put("jejuShppDisabYn", "0".equals(paSsgGoods.getNoJejuYn())?"N":"Y");// 	제주도 배송불가 여부		
		insertItem.put("ismtarShppDisabYn", "0".equals(paSsgGoods.getNoIslandYn())?"N":"Y");// 도서산간 배송불가 여부

		
		insertItem.put("shppRqrmDcnt", "01".equals(paSsgGoods.getShppItemDivCd())?3:15); //일반상품 3일, 설치/주문제작상품 15일
		
		insertItem.put("splVenItemId", paSsgGoods.getPaCode() + paSsgGoods.getGoodsCode());//공급업체상품ID
		insertItem.put("whoutShppcstId", paSsgGoods.getWhoutCost()); 		// 출고 배송비 ID
		insertItem.put("retShppcstId", paSsgGoods.getRetCost()); 			// 반품 배송비 ID
        insertItem.put("ismtarAddShppcstId", paSsgGoods.getIsCost());         //도서산간 추가배송비 ID
        insertItem.put("jejuAddShppcstId", paSsgGoods.getJejuCost());         //제주도 추가배송비 ID
		insertItem.put("whoutAddrId", paSsgGoods.getWhoutAddr()); 			//출고 주소 ID
		insertItem.put("snbkAddrId", paSsgGoods.getSnbAddr()); 				//반품 주소 ID
		
		Map<String, Object> imgInfoMap	   = new HashMap<String, Object>();
		List<Map<String, Object>> imgInfoList	= new ArrayList<Map<String,Object>>();
		Map<String, Object> imgInfo	   = new HashMap<String, Object>();
		
		imgInfo.put("dataSeq", 1);
		imgInfo.put("dataFileNm", image_address + paSsgGoods.getImageUrl() + paSsgGoods.getImageP());
		imgInfo.put("rplcTextNm", "image1");
		imgInfoList.add(imgInfo);
		
		if(StringUtil.isNotEmpty(paSsgGoods.getImageAP())) {
			imgInfo = new HashMap<String, Object>();
			imgInfo.put("dataSeq", 2);
			imgInfo.put("dataFileNm", image_address + paSsgGoods.getImageUrl() + paSsgGoods.getImageAP());
			imgInfo.put("rplcTextNm", "image2");
			imgInfoList.add(imgInfo);
		}
		if(StringUtil.isNotEmpty(paSsgGoods.getImageBP())) {
			imgInfo = new HashMap<String, Object>();
			imgInfo.put("dataSeq", 3);
			imgInfo.put("dataFileNm", image_address + paSsgGoods.getImageUrl() + paSsgGoods.getImageBP());
			imgInfo.put("rplcTextNm", "image3");
			imgInfoList.add(imgInfo);
		}
		if(StringUtil.isNotEmpty(paSsgGoods.getImageCP())) {
			imgInfo = new HashMap<String, Object>();
			imgInfo.put("dataSeq", 4);
			imgInfo.put("dataFileNm", image_address + paSsgGoods.getImageUrl() + paSsgGoods.getImageCP());
			imgInfo.put("rplcTextNm", "image4");
			imgInfoList.add(imgInfo);
		}
		if(StringUtil.isNotEmpty(paSsgGoods.getImageDP())) {
			imgInfo = new HashMap<String, Object>();
			imgInfo.put("dataSeq", 5);
			imgInfo.put("dataFileNm", image_address + paSsgGoods.getImageUrl() + paSsgGoods.getImageDP());
			imgInfo.put("rplcTextNm", "image5");
			imgInfoList.add(imgInfo);
		}
		
		imgInfoMap.put("imgInfo", imgInfoList);		
		insertItem.put("itemImgs", imgInfoMap);
		
		String goodsCom = "";
		
		goodsCom = (!ComUtil.NVL(paSsgGoods.getGoodsCom()).equals("")) ? ("<div style=\"line-height: 2.0em; font-family: 'NanumBarunGothic'; font-size: 19px;\"><div><h4>&middot;&nbsp;상품구성<h4><pre>" + paSsgGoods.getGoodsCom() + "</pre></div></div>") : "";
		
		if("".equals(paSsgGoods.getCollectImage()) || paSsgGoods.getCollectImage() == null) {
			goodsDescData = ("<div align='center'><img alt='' src='" + paSsgGoods.getTopImage() + "' /><br /><br /><br />"	//상단 이미지
				+ goodsCom	//상품 구성
				+ paSsgGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='"	//기술서
				+ paSsgGoods.getBottomImage() + "' /></div>");	//하단 이미지
		} else {
			goodsDescData = ("<div align='center'><img alt='' src='" + paSsgGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + paSsgGoods.getCollectImage() + "' /><br /><br /><br />"	//상단 이미지 + 착불 이미지
				+ goodsCom	//상품 구성
				+ paSsgGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='"	//기술서
				+ paSsgGoods.getBottomImage() + "' /></div>");	//하단 이미지
		}
		
		
		if(!ComUtil.NVL(paSsgGoods.getNoticeExt()).equals("")) {
			goodsDescData = paSsgGoods.getNoticeExt() + goodsDescData;
		}
		
		insertItem.put("itemDesc", goodsDescData);
		
		insertItem.put("giftPsblYn", "N");		// 선물 가능 여부		
		insertItem.put("palimpItemYn", "N");	// 병행수입 상품 여부 
		insertItem.put("itemSellWayCd", "10");	// 상품 판매 방식 코드
		insertItem.put("itemStatTypeCd", "10");	// 상품 상태 유형 코드
		insertItem.put("whinNotiYn", "Y");		// 입고 알림 여부
		
		// 상품수정일때 
		if("MODIFY".equals(modCase)) {
			insertItem.put("itemId", paSsgGoods.getItemId());
			insertItem.put("sellStatCd", "20".equals(paSsgGoods.getPaSaleGb())?"20":"80");		// 판매상태 | 20 : 판매중, 80 : 판매 일시 중지, 90 : 영구판매 중지
			ssgGoodsMap.put("updateItem", insertItem);	
		} else if("INSERT".equals(modCase)) {
			ssgGoodsMap.put("insertItem", insertItem);			
		}			
		
		return ssgGoodsMap;
	}	

	@ApiOperation(value = "상품 정보 수정", notes = "상품 정보 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 410, message = "이미 등록된 상품입니다."),
			   @ApiResponse(code = 411, message = "존재하지 않는 상품입니다."),
			   @ApiResponse(code = 420, message = "기술서를 입력하세요."),
			   @ApiResponse(code = 430, message = "정보고시를 입력하세요."),
			   @ApiResponse(code = 440, message = "상품의 출고지,회수지를 확인하세요"),
			   @ApiResponse(code = 450, message = "상품의 농수산물 필수값을 확인하세요"),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/legacy-goods-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = false) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_SSG_PROC_ID) String procId) throws Exception{
		log.info("===== 신세계 상품 수정 API Start =====");		
		ParamMap	apiInfoMap				= new ParamMap();
		ParamMap	paramMap				= new ParamMap();
		List<PaSsgGoodsVO> paSsgGoodsList = null;
		ResponseEntity<?> responseMsg		= null;
		List<PaEntpSlip> entpSlipList 		= null;
		String dateTime						= "";
		String prg_id						= "IF_PASSGAPI_01_004";
		PaEntpSlip entpSlip = null;
		HashMap<String, Object> describeData	= null;
		HashMap<String, Object> paNoticeData	= null;
		HashMap<String, Object> SsgFoodData	= null;
		
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("paCode", paCode);
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("procId", procId);
		paramMap.put("modCase", "MODIFY");
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			log.info("SSG상품수정 API 출고지/회수지 등록");
			entpSlipList = paSsgGoodsService.selectPaSsgEntpSlip(paramMap);
			if(entpSlipList.size() > 0){
				for(int i=0; i<entpSlipList.size(); i++){
					try {
						entpSlip = entpSlipList.get(i);
						responseMsg = paSsgCommonController.ssgEntpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaCode());
						log.info("SSG 상품수정 API 출고지/회수지 등록 CODE CHECK :" + PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
						
						if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")) {
							apiInfoMap.put("code", "440");
							apiInfoMap.put("message", getMessage("pa.check_entp_slip_man_seq", new String[] {" 상품코드 : " + goodsCode}));
						}
						
					}catch(Exception e){
						log.info("출고지/회수지 등록처리 Exception : "+entpSlip.getEntpCode()+"|"+entpSlip.getEntpManSeq());
					}
				}
			}
			
			log.info("SSG상품수정 API 배송비 등록");
			paSsgCommonController.entpCustShipCostInsert(request);
			
			log.info("SSG 상품수정 API 출고지/회수지 수정");
			paSsgCommonController.ssgEntpSlipModify(request);
			
			//상품 승인됬는지 확인
			log.info("SSG 상품 승인여부 확인");
			//SsgGoodsApprovalCheck(request, goodsCode, paCode, procId);
			
			//판매상태 변경
			log.info("SSG 판매 중단처리");
			setGoodsSellState(request, goodsCode, paCode, procId);
			
			log.info("SSG 상품수정 API 대상 조회");
			paSsgGoodsList = paSsgGoodsService.selectPaSsgGoodsInfoList(paramMap);
			
			if(paSsgGoodsList.size()==0){
				apiInfoMap.put("code","404");
				apiInfoMap.put("message",getMessage("pa.not_exists_process_list", new String[] {" 상품코드 : " + goodsCode}));
				apiInfoMap.put("resultMessage",apiInfoMap.getString("message"));
			}
			
			for(int i = 0; i < paSsgGoodsList.size(); i++) {
				try {
					
					ParamMap bodyMap = new ParamMap();
					ParamMap asyncMap = new ParamMap();
					PaSsgGoodsVO paSsgGoods = paSsgGoodsList.get(i);
					List<PaGoodsOfferVO> paSsgGoodsOffer = null;
					List<PaSsgGoodsdtMapping> goodsdtMapping = null;
					List<PaSsgDisplayMapping> ssgDisplayList = null;
					
					paSsgGoods.setProcStatCd("10");
					paSsgGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paSsgGoods.setModifyId(procId);
					
					asyncMap.put("paBroad",  apiInfoMap.get("paBroad"));
					asyncMap.put("paOnline", apiInfoMap.get("paOnline"));
					asyncMap.put("url", apiInfoMap.get("url"));
					asyncMap.put("paCode", paSsgGoods.getPaCode());
					asyncMap.put("method", apiInfoMap.get("method"));
					asyncMap.put("siteGb", Constants.PA_SSG_PROC_ID);
					asyncMap.put("apiCode", prg_id);
					
					log.info("GOODS_CODE : " + paSsgGoods.getGoodsCode());
					//수정할 SSG 상품 코드가 존재하지 않을시 처리
					if(StringUtil.isEmpty(paSsgGoods.getItemId())){
						apiInfoMap.put("code","411");
						apiInfoMap.put("message",getMessage("pa.not_exists_goods", new String[] {"상품코드 : " + goodsCode}));
						continue;
					}
					
					paramMap.put("paGroupCode", paSsgGoods.getPaGroupCode());
					paramMap.put("goodsCode", paSsgGoods.getGoodsCode());
					paramMap.put("paCode", paSsgGoods.getPaCode());
					
					log.info("SSG 상품수정 API 기술서 조회 ");
					describeData = paCommonService.selectDescData(paramMap);
					//수정할 상품 상세설명 존재하지 않을시 처리
					if(StringUtil.isEmpty(describeData.get("DESCRIBE_EXT").toString())){
						apiInfoMap.put("code","420");
						apiInfoMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
						continue;
					}
					paSsgGoods.setDescribeExt(ComUtil.getClobToString(describeData.get("DESCRIBE_EXT")));
					
					paNoticeData = paSsgGoodsService.selectPaNoticeData(paramMap);
					if(paNoticeData != null) {
						paSsgGoods.setNoticeExt(ComUtil.getClobToString(paNoticeData.get("NOTICE_EXT")));
					}
					
					log.info("SSG 상품수정 API 정보고시 조회 ");
					paSsgGoodsOffer = paCommonService.selectPaGoodsOfferList(paramMap);
					if(paSsgGoodsOffer.size() < 1){
						apiInfoMap.put("code","430");
						apiInfoMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
						continue;
					}
					
					if("0000000019".equals(paSsgGoodsOffer.get(0).getPaOfferType()) || 
				       "0000000020".equals(paSsgGoodsOffer.get(0).getPaOfferType()) || 
				       "0000000021".equals(paSsgGoodsOffer.get(0).getPaOfferType())) {
						
						log.info("SSG상품등록 API 농산물 필수값 조회");
						paramMap.put("paOfferType", paSsgGoodsOffer.get(0).getPaOfferType());

						SsgFoodData = paSsgGoodsService.selectSsgFoodInfo(paramMap);
						if(SsgFoodData == null){
							apiInfoMap.put("code", "450");
							apiInfoMap.put("message", getMessage("pa.insert_ssg_food_info", new String[] {"상품코드 : " + goodsCode}));
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
						}
					}
					
					log.info("SSG 상품수정 API 단품정보 조회 ");
					goodsdtMapping = paSsgGoodsService.selectPaSsgGoodsdtInfoList(paramMap);
					
					log.info("SSG상품등록 API 전시카테고리 조회");
					ssgDisplayList = paSsgGoodsService.selectPaSsgDisplayList(paramMap);
					
					bodyMap = makeGoodsInfo(paSsgGoods, paSsgGoodsOffer, goodsdtMapping, ssgDisplayList, paramMap.get("modCase").toString(), SsgFoodData);
					
					// 동기화로 처리
					paSsgAsyncController.asyncGoodsModify(request, asyncMap, bodyMap, paSsgGoods, goodsdtMapping);
					Thread.sleep(50);
					
				} catch (Exception e) {
					log.info("상품수정 Exception : " + paramMap.get("goodsCode").toString());
					log.info(e.getMessage()); 
				}
			}
			
			//재고변경
			log.info("SSG 단품 재고변경");
			setGoodsStockModify(request, goodsCode, paCode, procId);
			
		} catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
		
	@ApiOperation(value = "상품승인여부확인", notes = "딜옵션 개별등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/ssg-goods-approval-check", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> SsgGoodsApprovalCheck(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = false) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_SSG_PROC_ID) String procId) throws Exception{

		log.info("===== SSG 상품 승인 체크 API Start =====");		
		ParamMap	apiInfoMap			  = new ParamMap();
		ParamMap	apiDataMap			  = null;
		ParamMap	paramMap			  = new ParamMap();
		Map<String,Object> map			  = new HashMap<String,Object> () ;
		List<PaSsgGoodsVO> ssgGoodsApprovalList = null;
		String rtnMsg					  = Constants.SAVE_SUCCESS;
		String dateTime					  = "";
		String prg_id					  = "IF_PASSGAPI_01_005";
		
		dateTime = systemService.getSysdatetimeToString();

		paramMap.put("paCode", paCode);
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("procId", procId);
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);;
			
			String url = apiInfoMap.get("url").toString();
			
			ssgGoodsApprovalList = paSsgGoodsService.selectPaSsgGoodsApprovalList(paramMap);
			
			if(ssgGoodsApprovalList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			for(int i = 0; i < ssgGoodsApprovalList.size(); i++ ) {
				apiInfoMap.put("paCode", ssgGoodsApprovalList.get(i).getPaCode());
				apiInfoMap.put("url", url.replace("{itemId}", ssgGoodsApprovalList.get(i).getItemId()));				
				
				map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				
				HashMap<String, Object> result =  (HashMap<String, Object>) map.get("responseItemChngDemndList");
				if("SUCCESS".equals(result.get("resultMessage"))) {
					List<Map<String, Object>> itemChngDemndList  = new ArrayList<Map<String,Object>>();
					itemChngDemndList = (List<Map<String, Object>>)(result.get("itemChngDemndList"));
					List<Map<String, Object>> itemChngDemnd  = new ArrayList<Map<String,Object>>();
					if(itemChngDemnd.isEmpty()) {
						ssgGoodsApprovalList.get(i).setProcStatCd("20");
						ssgGoodsApprovalList.get(i).setModifyId(procId);
						ssgGoodsApprovalList.get(i).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
					}else {
						if(itemChngDemndList.get(0).get("itemChngDemnd") instanceof Map<?, ?>) {
							itemChngDemnd.add((HashMap<String, Object>) itemChngDemndList.get(0).get("itemChngDemnd"));
						} else {
							itemChngDemnd = (List<Map<String, Object>>)itemChngDemndList.get(0).get("itemChngDemnd");
						}
						
						for(int j=0; j<itemChngDemnd.size(); j++) {
							//신상품
							if("00".equals(itemChngDemnd.get(j).get("itemrChngDemndDivCd").toString())) {
								
								if("20".equals(itemChngDemnd.get(j).get("chngDemndProcStatCd").toString())) {
									ssgGoodsApprovalList.get(i).setProcStatCd(itemChngDemnd.get(j).get("chngDemndProcStatCd").toString());
									ssgGoodsApprovalList.get(i).setModifyId(procId);
									ssgGoodsApprovalList.get(i).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								}
							}
						}
					}
					
					rtnMsg = paSsgGoodsService.updatePaSsgGoodsApproval(ssgGoodsApprovalList.get(i));
					
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
		    			apiInfoMap.put("code", "500");
						apiInfoMap.put("message", rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
					} else {
						apiInfoMap.put("code", "200");
						apiInfoMap.put("message", "OK");
					}
				}				
			}
		}catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		log.info("===== SSG 상품 승인 체크 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "상품 재고수량 변경", notes = "상품 재고수량 변경", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goods-stock", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings({ "unchecked"})
	public ResponseEntity<?> setGoodsStockModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = false) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_SSG_PROC_ID) String procId) throws Exception{

		log.info("===== SSG 단품 재고수량 변경 API Start =====");		
		ParamMap	apiInfoMap			  = new ParamMap();
		ParamMap	apiDataMap			  = null;
		ParamMap	paramMap			  = new ParamMap();
		Map<String,Object> map			  = new HashMap<String,Object> () ;
		List<PaSsgGoodsVO> goodsStockList = null;
		List<PaSsgGoodsdtMapping> GoodsStockMapping = null;
		String rtnMsg					  = Constants.SAVE_SUCCESS;
		String dateTime					  = "";
		String prg_id					  = "IF_PASSGAPI_01_007";
		List<PaSsgDisplayMapping> ssgDisplayList = null;
		
		dateTime = systemService.getSysdatetimeToString();

		paramMap.put("paCode", paCode);
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("procId", procId);
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			goodsStockList = paSsgGoodsService.selectPaGoodsdtStockList(paramMap);
			
			if(goodsStockList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			for(int i = 0; i < goodsStockList.size(); i++ ) {
				
				apiDataMap = new ParamMap();
				apiInfoMap.put("paCode", goodsStockList.get(i).getPaCode());
				GoodsStockMapping = paSsgGoodsService.selectPaGoodsdtStockMappingList(goodsStockList.get(i));
				
				Map<String,Object> updateItem = new HashMap<String, Object>();
				
				updateItem.put("itemId", goodsStockList.get(i).getItemId());
				
				paramMap.put("goodsCode", goodsStockList.get(i).getGoodsCode());
				log.info("SSG재고수정");
				ssgDisplayList = paSsgGoodsService.selectPaSsgDisplayList(paramMap);
				
				Map<String, Object> siteMap		= new HashMap<String, Object>();
				List<Map<String, Object>> siteList	= new ArrayList<Map<String,Object>>();
				Map<String, Object> dispCtgMap		= new HashMap<String, Object>();
				List<Map<String, Object>> dispCtgList	= new ArrayList<Map<String,Object>>();
				
				int siteCnt = 0;
				for(int j=0; j < ssgDisplayList.size(); j++) {
					if(!"6005".equals(ssgDisplayList.get(j).getSiteNo())) {
						Map<String, Object> site		= new HashMap<String, Object>();
						site.put("siteNo", ssgDisplayList.get(j).getSiteNo());
						site.put("sellStatCd", "20");
						
						siteList.add(site);
						siteCnt++;
					}
					
					Map<String, Object> dispCtg		= new HashMap<String, Object>();
					dispCtg.put("siteNo", ssgDisplayList.get(j).getSiteNo());
					dispCtg.put("dispCtgId", ssgDisplayList.get(j).getDispCtgId());
					
					dispCtgList.add(dispCtg);
				}
				
				if(siteCnt < 1) {
					Map<String, Object> site		= new HashMap<String, Object>();
					site.put("siteNo", "6001");
					site.put("sellStatCd", "20");
					
					siteList.add(site);
					
					site		= new HashMap<String, Object>();
					site.put("siteNo", "6004");
					site.put("sellStatCd", "20");
					
					siteList.add(site);
				}
				
				siteMap.put("site", siteList);
				updateItem.put("sites", siteMap);
				dispCtgMap.put("dispCtg", dispCtgList);
				updateItem.put("dispCtgs", dispCtgMap);
				
				Map<String, Object> uitemMap		   = new HashMap<String, Object>();
				List<Map<String, Object>> uitemList	= new ArrayList<Map<String,Object>>();
				Map<String, Object> uitem = null;
				
				for(int j = 0; j < GoodsStockMapping.size(); j++) {
					uitem = new HashMap<String, Object>();			
					uitem.put("uitemId", GoodsStockMapping.get(j).getPaOptionCode());
					if(ComUtil.objToInt(GoodsStockMapping.get(j).getTransOrderAbleQty()) >= 9999) {
						uitem.put("baseInvQty", 9998);	
					} else {
						uitem.put("baseInvQty", ComUtil.objToInt(GoodsStockMapping.get(j).getTransOrderAbleQty()));						
					}
					uitemList.add(uitem);
					
					GoodsStockMapping.get(j).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					GoodsStockMapping.get(j).setModifyId(procId);
					
				}
				
				uitemMap.put("uitem", uitemList);
				updateItem.put("uitems", uitemMap);
				apiDataMap.put("updateItem", updateItem);
				
				map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				
				HashMap<String, Object> result =  (HashMap<String, Object>) map.get("result");
				if("SUCCESS".equals(result.get("resultMessage"))) {
					
					rtnMsg = paSsgGoodsService.updatePaGoodsdtMappingQtyTx(GoodsStockMapping);
    				
    				if(!rtnMsg.equals("000000")){
    					apiInfoMap.put("code","500");
    					apiInfoMap.put("message","재고수정 실패(SK스토아)");
						continue;
					} else {
						apiInfoMap.put("code","200");
						apiInfoMap.put("message","OK");
					}
				}				
			}
		}catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		log.info("===== SSG 단품 재고수량 변경 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "상품 판매상태 변경", notes = "상품 판매상태 변경", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goods-sell-state", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings({ "unchecked"})
	public ResponseEntity<?> setGoodsSellState(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = false) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_SSG_PROC_ID) String procId) throws Exception{

		log.info("===== SSG 상품 판매상태 변경 API Start =====");		
		ParamMap	apiInfoMap			  = new ParamMap();
		ParamMap	apiDataMap			  = null;
		ParamMap	paramMap			  = new ParamMap();
		Map<String,Object> map			  = new HashMap<String,Object> () ;
		List<PaSsgGoodsVO> sellStateList = null;
		String rtnMsg					  = Constants.SAVE_SUCCESS;
		String dateTime					  = "";
		String prg_id					  = "IF_PASSGAPI_01_008";
		List<PaSsgDisplayMapping> ssgDisplayList = null;
		
		dateTime = systemService.getSysdatetimeToString();

		paramMap.put("paCode", paCode);
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("procId", procId);
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);;
			
			sellStateList = paSsgGoodsService.selectPaSellStateList(paramMap);
			
			if(sellStateList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			for(int i = 0; i < sellStateList.size(); i++ ) {
				
				apiDataMap = new ParamMap();
				apiInfoMap.put("paCode", sellStateList.get(i).getPaCode());				
				
				Map<String,Object> updateItem = new HashMap<String, Object>();
				
				if ("50".equals(sellStateList.get(i).getPaStatus())) updateItem.put("sellStatCd", "90"); // 폐기판매종료 상품 -> 영구판매중지
				else updateItem.put("sellStatCd", ("20".equals(sellStateList.get(i).getPaSaleGb())?"20":"80"));

				updateItem.put("itemId", sellStateList.get(i).getItemId());
				
				paramMap.put("goodsCode", sellStateList.get(i).getGoodsCode());
				log.info("SSG 판매상태 변경처리");
				ssgDisplayList = paSsgGoodsService.selectPaSsgDisplayList(paramMap);
				
				
				Map<String, Object> siteMap		= new HashMap<String, Object>();
				List<Map<String, Object>> siteList	= new ArrayList<Map<String,Object>>();
				Map<String, Object> dispCtgMap		= new HashMap<String, Object>();
				List<Map<String, Object>> dispCtgList	= new ArrayList<Map<String,Object>>();
				
				int siteCnt = 0;
				for(int j=0; j < ssgDisplayList.size(); j++) {
					Map<String, Object> dispCtg		= new HashMap<String, Object>();
					dispCtg.put("siteNo", ssgDisplayList.get(j).getSiteNo());
					dispCtg.put("dispCtgId", ssgDisplayList.get(j).getDispCtgId());
					
					dispCtgList.add(dispCtg);
					
					if(!"6005".equals(ssgDisplayList.get(j).getSiteNo())) {
						Map<String, Object> site		= new HashMap<String, Object>();
						site.put("siteNo", ssgDisplayList.get(j).getSiteNo());
						site.put("sellStatCd", "20");
						
						siteList.add(site);	
						siteCnt++;
					}
				}
				
				if(siteCnt < 1) {
					Map<String, Object> site		= new HashMap<String, Object>();
					site.put("siteNo", "6001");
					site.put("sellStatCd", "20");
					
					siteList.add(site);
					
					site		= new HashMap<String, Object>();
					site.put("siteNo", "6004");
					site.put("sellStatCd", "20");
					
					siteList.add(site);
				}
				
				siteMap.put("site", siteList);
				updateItem.put("sites", siteMap);
				dispCtgMap.put("dispCtg", dispCtgList);
				updateItem.put("dispCtgs", dispCtgMap);
				apiDataMap.put("updateItem", updateItem);
				
				map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				
				HashMap<String, Object> result =  (HashMap<String, Object>) map.get("result");
				if("SUCCESS".equals(result.get("resultMessage")) || result.get("resultDesc").toString().contains("영구판매중지인")) {
					
					sellStateList.get(i).setModifyId(procId);
					sellStateList.get(i).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					
					rtnMsg = paSsgGoodsService.updatePaGoodsStatus(sellStateList.get(i));
    				
    				if(!rtnMsg.equals("000000")){
						paramMap.put("code","500");
						paramMap.put("message","판매상태변경 실패(SK스토아)");
						continue;
					} else {
						paramMap.put("code","200");
						paramMap.put("message","OK");
					}
				}				
			}
		}catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		log.info("===== SSG 상품 판매상태 변경 API End =====");		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
}
