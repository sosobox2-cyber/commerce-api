package com.cware.api.pamassgoods.controller;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cware.api.pa11st.controller.Pa11stGoodsController;
import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.pacopn.controller.PaCopnGoodsController;
import com.cware.api.pagmktv2.controller.PaGmktV2GoodsController;
import com.cware.api.paintp.controller.PaIntpGoodsController;
import com.cware.api.palton.controller.PaLtonGoodsController;
import com.cware.api.pawemp.controller.PaWempGoodsController;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.Pa11stGoodsVO;
import com.cware.netshopping.domain.PaCopnGoodsVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.PaIntpGoodsVO;
import com.cware.netshopping.domain.PaLtonGoodsVO;
import com.cware.netshopping.domain.PaWempGoodsVO;
import com.cware.netshopping.pa11st.goods.service.Pa11stGoodsService;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacopn.goods.service.PaCopnGoodsService;
import com.cware.netshopping.pagmkt.goods.service.PaGmktGoodsService;
import com.cware.netshopping.paintp.goods.service.PaIntpGoodsService;
import com.cware.netshopping.palton.goods.service.PaLtonGoodsService;
import com.cware.netshopping.pawemp.goods.service.PaWempGoodsService;

@Controller("com.cware.api.pamassgoods.controller")
@RequestMapping(value="/pamass/mass")

public class PaMassGoodsController extends AbstractController{
	
	@Autowired
	Pa11stGoodsController pa11stGoodsController;
	@Autowired
	private Pa11stGoodsService pa11stGoodsService;	
	@Autowired
	private PaGmktV2GoodsController paGmktV2GoodsController;
	@Autowired
	private PaGmktGoodsService paGmktGoodsService;
	@Autowired
	private PaCopnGoodsController paCopnGoodsController;
	@Autowired
	private PaCopnGoodsService paCopnGoodsService;   
	@Autowired 
	private PaWempGoodsController paWempGoodsController;
	@Autowired
	private PaWempGoodsService paWempGoodsService;
	@Autowired
	private PaIntpGoodsController paIntpGoodsController;
	@Autowired
	private PaIntpGoodsService paIntpGoodsService;
	@Autowired
	private PaLtonGoodsController paLtonGoodsController;
	@Autowired
	private PaLtonGoodsService paLtonGoodsService;
	@Autowired
    private SystemService systemService;
	@Autowired  
	private PaCommonService paCommonService;
		
	private final String PA_11ST_PROC_ID = "PA11";
	private final String PA_GMKT_PROC_ID = "PAGMKT";
	private final String PA_COPN_PROC_ID = Constants.PA_COPN_PROC_ID;
	private final String PA_WEMP_PROC_ID = Constants.PA_WEMP_PROC_ID;
	private final String PA_INTP_PROC_ID = Constants.PA_INTP_PROC_ID;
	private final String PA_LTON_PROC_ID = Constants.PA_LTON_PROC_ID;

	
	
	//쿠팡  대량입점
	@RequestMapping(value = "/pacopn-mass-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> paCopnMassInsert(HttpServletRequest request) throws Exception {
		String   rtnCode  				= "200";
		String   rtnMessage				= "OK";
		int		 resultCnt				= 0;
		String 	 prg_id 				= "IF_PACOPNAPI_01_001(Mass)";
		String	 duplicateCheck			= "0";
		ParamMap paramMap				= new ParamMap();
		String	 exceptStr				= "";
		
		try {
			//API DUPLICATE CHECK
			duplicateCheck 	= systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			paramMap.put("massTargetYn", "1");
			List<HashMap<String, String>> paGoodsTargetList = paCopnGoodsService.selectPaCopnGoodsTrans(paramMap);
			
			for(HashMap<String, String> paGoods :  paGoodsTargetList) {
				paramMap.put("goodsCode", paGoods.get("GOODS_CODE"));
				paramMap.put("paCode"	, paGoods.get("PA_CODE"));
				
				//EXCEPT 처리 (마진율, 재고, 옴부즈맨 등등)
				paramMap.put("pa_group_code"	, "05");
				paramMap.put("margin_code"		, "60");
				paramMap.put("minprice_code"	, "61");
				paramMap.put("pa_code"			, paGoods.get("PA_CODE"));
				paramMap.put("goods_code"		, paGoods.get("GOODS_CODE"));
				paramMap.replaceCamel();
				
				exceptStr = paCommonService.paExceptGoodsYn(paramMap);
				if(!exceptStr.equals("000000")){
					paGoods.put("RETURN_NOTE"	, exceptStr);
					paGoods.put("PA_STATUS"		, "20");
					paGoods.put("PA_SALE_GB"	, "20");
					paGoods.put("MODIFY_ID"		, PA_COPN_PROC_ID);
					
					PaCopnGoodsVO paCopnGoods = new PaCopnGoodsVO();
					paCopnGoods.setReturnNote	(exceptStr);
					paCopnGoods.setPaStatus		("20");
					paCopnGoods.setPaSaleGb		("20");
					paCopnGoods.setModifyId		(PA_COPN_PROC_ID);
					paCopnGoods.setModifyDate	(DateUtil.toTimestamp(systemService.getSysdatetimeToString(), "yyyy/MM/dd HH:mm:ss"));
					
					paCopnGoods.setGoodsCode	(paGoods.get("GOODS_CODE"));
					paCopnGoods.setPaCode		(paGoods.get("PA_CODE"));
					
					paCopnGoodsService.updatePaCopnGoodsFail(paCopnGoods);
					continue;
				}
				
				//CALL 11번가 GoodsInsert API
				ResponseEntity<?> responseMsg = paCopnGoodsController.goodsInsert(request, paGoods.get("GOODS_CODE"), paGoods.get("PA_CODE"), PA_COPN_PROC_ID , "1" , "1");
				
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){ //API 결과 실패
					resultCnt++;
				}else {
					PaCopnGoodsVO paCopnGoods = new PaCopnGoodsVO();
					paCopnGoods.setPaCode		(paGoods.get("PA_CODE"));
					paCopnGoods.setGoodsCode	(paGoods.get("GOODS_CODE"));
					paCopnGoods.setMassTargetYn	("0");
					paCopnGoodsService.updateMassTargetYn(paCopnGoods);
				}
			}
			
				
			if(paGoodsTargetList.size() != resultCnt) {
				paramMap.put("code"		,"490");
				paramMap.put("message"	, paGoodsTargetList.size() + "건 중"  + resultCnt + "성공" );
			}else {
				paramMap.put("code"		,"200");
				paramMap.put("message"	, paGoodsTargetList.size() + "건 입점 완료" );
			}
			
				
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				rtnCode = "490";
			} else {
				rtnCode = "500";
			}
			rtnMessage = errorCode(e);
		
		}finally {
			insertApiTracking( request, prg_id, rtnCode, rtnMessage, PA_COPN_PROC_ID);
			
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}		
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), rtnCode , rtnMessage), HttpStatus.OK);
	}
		
	
	//상품 대량수정(공통)
	@RequestMapping(value = "/pa-mass-modify/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> patMassModify(HttpServletRequest request, @PathVariable("paGroupCode") String paGroupCode) throws Exception {
		
		String mssApiCode				= "";
		String apiCode					= "";
		String rtnCode					= "200";
		String rtnMessage				= "OK";
		ParamMap paramMap 				= new ParamMap();
		String	 modifyId				= "";
		String   broadCode				= "";
		String   siteName				= "";
		String 	 onlineCode				= "";
		
		try {		
			switch (paGroupCode) {
			
			case "01": //11번가
				mssApiCode  = "IF_PA11STAPI_01_002(MASS)";  //가칭
				apiCode		= "IF_PA11STAPI_01_002";
				broadCode   = Constants.PA_11ST_BROAD_CODE;
				onlineCode	= Constants.PA_11ST_ONLINE_CODE;
				modifyId	= PA_11ST_PROC_ID;
				siteName	= "PA11ST";
				break; //G마켓

			case "02":
				mssApiCode  = "IF_PAGMKTAPI_V2_01_003(MASS)";
				apiCode 	= "IF_PAGMKTAPI_V2_01_003";
				modifyId	= PA_GMKT_PROC_ID;
				siteName    = "PAGMKT";
				break;
				
			case "05":
				mssApiCode  = "IF_PACOPNAPI_01_005(MASS)";
				apiCode		= "IF_PACOPNAPI_01_005";
				modifyId 	= PA_COPN_PROC_ID;
				siteName	= "PACOPN";
				broadCode   = Constants.PA_COPN_BROAD_CODE;
				onlineCode	= Constants.PA_COPN_ONLINE_CODE;
				break;		
				
			case "06":
				mssApiCode  = "IF_PAWEMPAPI_01_002(MASS)";
				apiCode		= "IF_PAWEMPAPI_01_002";
				broadCode	= Constants.PA_WEMP_BROAD_CODE;
				onlineCode	= Constants.PA_WEMP_ONLINE_CODE;
				modifyId	= PA_WEMP_PROC_ID;
				siteName	= "PAWEMP";
				break;
			
			case "07":
				mssApiCode  = "IF_PAINTPAPI_01_002(MASS)";
				apiCode		= "IF_PAINTPAPI_01_002";
				broadCode	= Constants.PA_INTP_BROAD_CODE;
				onlineCode	= Constants.PA_INTP_ONLINE_CODE;
				modifyId	= PA_INTP_PROC_ID;
				siteName	= "PAINTP";
				break;
					
			case "08":
				mssApiCode  = "IF_PALTONAPI_01_005(MASS)";
				apiCode		= "IF_PALTONAPI_01_005";				
				broadCode	= Constants.PA_LTON_BROAD_CODE;
				onlineCode	= Constants.PA_LTON_ONLINE_CODE;
				modifyId	= PA_LTON_PROC_ID;
				break;
				
			default:
				//Throw Exception
				break;
			}
			
		
			HashMap<String, String> apiInfo = new HashMap<String, String>();
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode"		, apiCode);
			paramMap.put("broadCode"	, broadCode);
			paramMap.put("onlineCode"	, onlineCode);
			paramMap.put("startDate"	, dateTime);
			paramMap.put("modCase"		, "MODIFY");
			paramMap.put("siteName"		, siteName);
			paramMap.put("dateTime"		, dateTime);
			paramMap.put("goodsListTime", dateTime);
			paramMap.put("modifyId"		, modifyId);
			//paramMap.put("syncMethod"	, "MASS"); 		
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));

			
			switch (paGroupCode) {
			
			case "01":
				apiInfo.put("contentType", "text/xml;charset=utf-8");
				pa11stGoodsModify(request, paramMap, apiInfo);
				break;
			
			case "02":
				pagmktGoodsModify	(request, paramMap);
				break;
				
			case "05":
				paCopnGoodsModify	(request , paramMap , apiInfo );
				break;
				
			case "06":
				paWempGoodsModify	(request , paramMap , apiInfo );
				break;
			
			case "07":
				apiInfo.put("contentType", "text/xml;charset=utf-8");
				paIntpGoodsModify	(request, paramMap, apiInfo);
				break;
			case "08":
				apiInfo.put("contentType", "text/xml;charset=utf-8");
				paLtonGoodsModify   (request, paramMap, apiInfo);
				break;
			}
						
		}catch (Exception e) {
			rtnCode	   = "500";
			paramMap.put("message" , errorCode(e));
		}finally {
			rtnMessage = paramMap.getString("message");
			insertApiTracking(request, mssApiCode, rtnCode, rtnMessage, siteName);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), rtnCode , rtnMessage), HttpStatus.OK);
	}
		
	//11번가 대량 수정
	private void pa11stGoodsModify(HttpServletRequest request , ParamMap paramMap , HashMap<String, String> apiInfo) throws Exception {
		
		// 상품 단품 수정
		String massTargetEp =  getMassEpCode();
		paramMap.put("massTargetEp"	, massTargetEp);
		paramMap.put("massTargetYn"	, "2");
		paramMap.put("paGroupCode"	, "01");
		
		List<Pa11stGoodsVO> pa11stGoodsList   = pa11stGoodsService.selectPa11stGoodsInfoListMass	(paramMap);
		List<PaGoodsPriceVO> paGoodsPriceList = pa11stGoodsService.selectPa11stGoodsPriceListMass	(paramMap);
			
		for(Pa11stGoodsVO asyncPa11stGoods : pa11stGoodsList){
			try {
				//1)MODIFY 11ST_GOODS 
				paramMap.put("goodsCode"	, asyncPa11stGoods.getGoodsCode());
				paramMap.put("paCode"		, asyncPa11stGoods.getPaCode());
				paramMap.put("modCase"		, "MODIFY");
				pa11stGoodsController.goodsModify11st(request, asyncPa11stGoods, paramMap, apiInfo);		
				
			}catch (Exception e) {
				log.error(e.toString());
				//UPDATE PA11STGOODS.MASS_TARGET_YN = 9 
				asyncPa11stGoods.setMassTargetYn("9");
				pa11stGoodsService.updateMassTargetYn(asyncPa11stGoods);
				continue;
			}
		}
		
		for(PaGoodsPriceVO vo : paGoodsPriceList ) {
			ResponseEntity<?> responseMsg = pa11stGoodsController.goodspriceCouponModify(request,"", "", paramMap.getString("modifyId"), "1", vo);
			
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){ //API 결과 실패
				log.error(PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				
				Pa11stGoodsVO asyncPa11stGoods = new Pa11stGoodsVO();
				asyncPa11stGoods.setPaCode		 (vo.getPaCode());
				asyncPa11stGoods.setGoodsCode	 (vo.getGoodsCode());
				asyncPa11stGoods.setMassTargetYn ("9");
				pa11stGoodsService.updateMassTargetYn(asyncPa11stGoods);
			}
		}
		
		HashMap<String, String> massMap = new HashMap<String, String>();
		massMap.put("massTargetYn"	, "0");
		massMap.put("massTargetEp"	, massTargetEp);
		pa11stGoodsService.updateMassTargetYnByEpCode(massMap);
		
		paramMap.put("message", "상품수정 : " + pa11stGoodsList.size() + "  / 가격수정 : "  + paGoodsPriceList.size() );
		
		
	}
	
	//지마켓 대량 수정
	private void pagmktGoodsModify(HttpServletRequest request, ParamMap paramMap) throws Exception {
		HashMap<String,Object> target = new HashMap<String,Object>();
		target.put("rowNum", paramMap.get("rowNum"));
		int failCnt = 0; 
		String massTargetEp =   getMassEpCode();
		
		target.put("massTargetEp"	,  massTargetEp);
		target.put("massTargetYn"	,  "2");
		target.put("paGroupCode"	,  "02");
		target.put("modCase"		, Constants.PA_GMKT_MOD_CASE_MODIFY);
		
		List<HashMap<String,String>> targetGoodsList = paGmktGoodsService.selectPagmktGoodsInfoListMass(target);
		
		
		for(HashMap<String,String> targetGoods : targetGoodsList){
			try {
				//1)MODIFY 11ST_GOODS 
				ResponseEntity<?> responseMsg = paGmktV2GoodsController.putGoods(request, targetGoods.get("PA_CODE").toString() , targetGoods.get("GOODS_CODE").toString() , "MASS", "1");	
				
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
					throw new Exception(PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				}
				//3)UPDATE PA11STGOODS.MASS_TARGET_YN = 0
				targetGoods.put("MASS_TARGET_YN"	, "0");
				paGmktGoodsService.updateMassTargetYn(targetGoods);
				
			}catch (Exception e) {
				log.error(errorCode(e));
				//UPDATE PA11STGOODS.MASS_TARGET_YN = 9 
				targetGoods.put("MASS_TARGET_YN"	, "0"); //원래 9였으나 일단 0 으로....
				paGmktGoodsService.updateMassTargetYn(targetGoods);
				failCnt++;
				continue;
			}
		}
		
		paramMap.put("message", targetGoodsList.size() + " 건 수행 " + "에러 :  " + failCnt );
	}
	
	//쿠팡 대량 수정  @Async 쓰려면 private 제거
	private void paCopnGoodsModify(HttpServletRequest request , ParamMap paramMap , HashMap<String, String> apiInfo) throws Exception {

		//1)
		String massTargetEp =   getMassEpCode();
		paramMap.put("massTargetEp"	, massTargetEp);
		paramMap.put("massTargetYn"	, "2");
		paramMap.put("paGroupCode"	, "05");
		List<PaCopnGoodsVO> paCopnGoodsList 	= paCopnGoodsService.selectPaCopnGoodsInfoListMass	(paramMap);
		List<PaGoodsPriceVO> paGoodsPriceList 	= paCopnGoodsService.selectCopnPriceModifyMass		(paramMap);
		
		int goodsFailCnt 		= 0 ;
		int goodsPriceFailCnt 	= 0;
		
		
		for(PaCopnGoodsVO paCopnGoods : paCopnGoodsList){
			try {
				
				//1)MODIFY 11ST_GOODS 
				paramMap.put("modCase"		, "MODIFY");
				paramMap.put("goodsCode"	, paCopnGoods.getGoodsCode());
				paramMap.put("paCode"		, paCopnGoods.getPaCode());
				paramMap.put("modifyId"		, PA_COPN_PROC_ID);
				//paramMap.put("paAddrGb"		, "30");
				paCopnGoodsController.goodsModifyCopn(request, apiInfo, paramMap, paCopnGoods);		
				
			}catch (Exception e) {
				log.error(e.toString());
				//UPDATE PA11STGOODS.MASS_TARGET_YN = 9 
				paCopnGoods.setMassTargetYn("0");
				paCopnGoodsService.updateMassTargetYn(paCopnGoods);
				goodsFailCnt++;
				continue;
			}
		}
		
		for(PaGoodsPriceVO goodsPrice : paGoodsPriceList) {
			
			try {
				paCopnGoodsController.goodsPriceModifyCopn(request, paramMap, goodsPrice,  "1");	
			}catch (Exception e) {
				log.error(e.toString());
				PaCopnGoodsVO paCopnGoods = new PaCopnGoodsVO();
				paCopnGoods.setPaCode					(goodsPrice.getPaCode());
				paCopnGoods.setGoodsCode				(goodsPrice.getGoodsCode());
				paCopnGoods.setMassTargetYn				("0");
				paCopnGoodsService.updateMassTargetYn	(paCopnGoods);
				goodsPriceFailCnt++;
				continue;
			}
		}
		
		HashMap<String, String> massMap = new HashMap<String, String>();
		massMap.put("massTargetYn"	, "0");
		massMap.put("massTargetEp"	, massTargetEp);
		paCopnGoodsService.updateMassTargetYnByEpCode(massMap);
		
		
		paramMap.put("message", "상품수정 : " + paCopnGoodsList.size() + "(" + goodsFailCnt + ")" + "  / 가격수정 : "  + paGoodsPriceList.size() + "(" +  goodsPriceFailCnt + ")" );
		
	}
	
	//위메프 대량 수정
	private void paWempGoodsModify(HttpServletRequest request , ParamMap paramMap , HashMap<String, String> apiInfo) throws Exception {
		int goodsFailCnt	= 0;
		String massTargetEp =   getMassEpCode();
		paramMap.put("massTargetEp"	, massTargetEp);
		paramMap.put("massTargetYn"	, "2");
		paramMap.put("paGroupCode"	, "06");
		List<PaWempGoodsVO> paWempGoodsList 	= paWempGoodsService.selectPaWempGoodsInfoListMass (paramMap);
		
		for(PaWempGoodsVO paWempGoods : paWempGoodsList){
			try {
				paramMap.put("goodsCode", paWempGoods.getGoodsCode());
				paramMap.put("paCode"	, paWempGoods.getPaCode());
				paramMap.put("siteGb"	, Constants.PA_WEMP_PROC_ID);
				
				paWempGoodsController.goodsModifyWemp(request, paramMap, apiInfo, paWempGoods);
				
			}catch (Exception e) {
				log.error(e.toString());
				//UPDATE PA11STGOODS.MASS_TARGET_YN = 9 
				paWempGoods.setMassTargetYn("9");
				paWempGoodsService.updateMassTargetYn(paWempGoods);
				goodsFailCnt++;
				continue;
			}
		}
		
		HashMap<String, String> massMap = new HashMap<String, String>();
		massMap.put("massTargetYn"	, "0");
		massMap.put("massTargetEp"	, massTargetEp);
		paWempGoodsService.updateMassTargetYnByEpCode(massMap);		
		
		paramMap.put("message", "상품수정 : " + paWempGoodsList.size() + "(" + goodsFailCnt + ")" );
	}
	
	//인터파크 대량 수정
	private void paIntpGoodsModify(HttpServletRequest request , ParamMap paramMap , HashMap<String, String> apiInfo) throws Exception {
			
		String massTargetEp =   getMassEpCode();
		paramMap.put("massTargetEp"	, massTargetEp);
		paramMap.put("massTargetYn"	, "2");
		paramMap.put("paGroupCode"	, "07");
		List<PaIntpGoodsVO> paIntpGoodsList 	= paIntpGoodsService.selectPaIntpGoodsInfoListMass (paramMap);
			
		for(PaIntpGoodsVO paIntpGoods : paIntpGoodsList){
			try {
				paramMap.put("goodsCode", paIntpGoods.getGoodsCode());
				paramMap.put("paCode"	, paIntpGoods.getPaCode());
				paramMap.put("siteGb"	, Constants.PA_INTP_PROC_ID);
				
				paIntpGoodsController.goodsModifyIntp(request, apiInfo, paramMap, paIntpGoods);
				
			}catch (Exception e) {
				log.error(e.toString());
				//UPDATE PA11STGOODS.MASS_TARGET_YN = 9 
				paIntpGoods.setMassTargetYn("0");
				paIntpGoodsService.updateMassTargetYn(paIntpGoods);
				continue;
			}
		}
			
		HashMap<String, String> massMap = new HashMap<String, String>();
		massMap.put("massTargetYn"	, "0");
		massMap.put("massTargetEp"	, massTargetEp);
		paIntpGoodsService.updateMassTargetYnByEpCode(massMap);		
	}
	
	//롯데온 대량 수정
	private void paLtonGoodsModify(HttpServletRequest request , ParamMap paramMap , HashMap<String, String> apiInfo) throws Exception {
		int goodsFailCnt	= 0;
		String massTargetEp =   getMassEpCode();
		paramMap.put("massTargetEp"	, massTargetEp);
		paramMap.put("massTargetYn"	, "2");
		paramMap.put("method"		, "POST");
		paramMap.put("paGroupCode"	, Constants.PA_LTON_GROUP_CODE);
		
		List<PaLtonGoodsVO> paltonGoodsList 	= paLtonGoodsService.selectPaLtonGoodsInfoListMass (paramMap);

		for(PaLtonGoodsVO paltonGoods : paltonGoodsList){
			try {
				paramMap.put("goodsCode", paltonGoods.getGoodsCode());
				paramMap.put("paCode"	, paltonGoods.getPaCode());
				paramMap.put("siteGb"	, Constants.PA_LTON_PROC_ID);
				
				paLtonGoodsController.paLtonGoodsModify(request, paltonGoods, apiInfo, paramMap);
				
			}catch (Exception e) {
				log.error(e.toString());
				//UPDATE PA11STGOODS.MASS_TARGET_YN = 9 
				paltonGoods.setMassTargetYn("9");
				paLtonGoodsService.updateMassTargetYn(paltonGoods);
				goodsFailCnt++;
				continue;
			}
		}
		
		HashMap<String, String> massMap = new HashMap<String, String>();
		massMap.put("massTargetYn"	, "0");
		massMap.put("massTargetEp"	, massTargetEp);
		paLtonGoodsService.updateMassTargetYnByEpCode(massMap);		
		paramMap.put("message", "상품수정 : " + paltonGoodsList.size() + "(" + goodsFailCnt + ")" );
	}
		
	/*	
	
	//11번가 대량입점
	@RequestMapping(value = "/pa11st-mass-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> pa11stMassInsert(HttpServletRequest request) throws Exception {
		ParamMap paramMap 				= new ParamMap();
		String   rtnCode  				= "200";
		String   rtnMessage				= "OK";
		int		 resultCnt				= 0;
		String 	 prg_id 				= "IF_PA11STAPI_01_001(MESS)";
		String	 duplicateCheck			= "0";
		String exceptStr  				= "";
	
		try {
			//API DUPLICATE CHECK
			duplicateCheck 	= systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			//입점 상품 조회 및 11번가 상품입점 API 호출
			paramMap.put("massTargetYn", "1");
			List<HashMap<String,String>> paGoodsTargetList = pa11stGoodsService.selectPaGoodsTrans(paramMap);
			for(HashMap<String,String> paGoods:  paGoodsTargetList ) {

				paramMap.put("goodsCode", paGoods.get("GOODS_CODE"));
				paramMap.put("paCode"	, paGoods.get("PA_CODE"));
				
				//EXCEPT 처리 (마진율, 재고, 옴부즈맨 등등)
				exceptStr = paCommonService.paExceptGoodsYn(paramMap);
				if(!exceptStr.equals("000000")){
					paGoods.put("RETURN_NOTE"	, exceptStr);
					paGoods.put("PA_STATUS"		, "20");
					paGoods.put("MODIFY_ID"		, PA_11ST_PROC_ID);
					pa11stGoodsService.updatePa11stGoodsFailInsert(paGoods);
					continue;
				}
				
				//CALL 11번가 GoodsInsert API
				ResponseEntity<?> responseMsg = pa11stGoodsController.goodsInsert(request, paGoods.get("GOODS_CODE"), paGoods.get("PA_CODE"), PA_11ST_PROC_ID , "1", "1");
				
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){ //API 결과 실패
					resultCnt++;
				}else {
					Pa11stGoodsVO asyncPa11stGoods = new Pa11stGoodsVO();
					asyncPa11stGoods.setPaCode		 (paGoods.get("PA_CODE"));
					asyncPa11stGoods.setGoodsCode	 (paGoods.get("GOODS_CODE"));
					asyncPa11stGoods.setMassTargetYn ("0");
					pa11stGoodsService.updateMassTargetYn(asyncPa11stGoods);					
				}
			}
			
			if(paGoodsTargetList.size() != resultCnt) {
				paramMap.put("code"		,"490");
				paramMap.put("message"	, paGoodsTargetList.size() + "건 중"  + resultCnt + "성공" );
			}else {
				paramMap.put("code"		,"200");
				paramMap.put("message"	, paGoodsTargetList.size() + "건 입점 완료" );
			}
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				rtnCode = "490";
			} else {
				rtnCode = "500";
			}
			rtnMessage = errorCode(e);
		
		}finally {
			insertApiTracking( request, prg_id, rtnCode, rtnMessage, PA_11ST_PROC_ID);
			
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}		
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), rtnCode , rtnMessage), HttpStatus.OK);
	}
	
	//G마켓 대량입점 TODO 확인
	@RequestMapping(value = "/pagmkt-mass-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> paGmktMassInsert(HttpServletRequest request) throws Exception {
		
		String   rtnCode  				= "200";
		String   rtnMessage				= "OK";
		int		 resultCnt				= 0;
		String 	 prg_id 				= "IF_PAGMKTAPI_V2_01_001(MESS)";
		String	 duplicateCheck			= "0";
		HashMap<String,Object> target	= new HashMap<String, Object>();
		try {
			//API DUPLICATE CHECK
			duplicateCheck 	= systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			target.put("massTargetYn"	, "1");
							
			List<HashMap<String,String>> paGoodsTargetList = paGmktGoodsService.selectGmktGoodsInsertList(target); //TODO 살리려면 해당 쿼리 에 가서 주석처리된거 풀고 테스트 필요!!!
			
			for(HashMap<String,String> paGoodsTarget : paGoodsTargetList) {
	
				ResponseEntity<?> responseMsg = paGmktV2GoodsController.postGoods(request, "" ,"" , "1",  paGoodsTarget ); //Rquest, GoodsCode ,paCode, searchTermGb, massTargetYn
				
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){ //API 결과 실패
					resultCnt++;
				}else {
					paGoodsTarget.put("massTargetYn"	, "1");
					paGmktGoodsService.updateMassTargetYn(paGoodsTarget);
				}
				
			}
			
			if(paGoodsTargetList.size() != resultCnt) {
				rtnCode 	= "490";
				rtnMessage  =	paGoodsTargetList.size() + "건 중"  + resultCnt + "성공";
			}else {
				rtnCode     = "200";
				rtnMessage  = paGoodsTargetList.size() + "건 입점 완료";
			}
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				rtnCode = "490";
			} else {
				rtnCode = "500";
			}
			rtnMessage = errorCode(e);
		
		}finally {
			insertApiTracking( request, prg_id, rtnCode, rtnMessage, PA_GMKT_PROC_ID);
			
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}		
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), rtnCode , rtnMessage), HttpStatus.OK);
	}
	
	//쿠팡  대량입점
	@RequestMapping(value = "/pacopn-mass-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> paCopnMassInsert(HttpServletRequest request) throws Exception {
		String   rtnCode  				= "200";
		String   rtnMessage				= "OK";
		int		 resultCnt				= 0;
		String 	 prg_id 				= "IF_PACOPNAPI_01_001(Mass)";
		String	 duplicateCheck			= "0";
		ParamMap paramMap				= new ParamMap();
		String	 exceptStr				= "";
		
		try {
			//API DUPLICATE CHECK
			duplicateCheck 	= systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			paramMap.put("massTargetYn", "1");
			List<HashMap<String, String>> paGoodsTargetList = paCopnGoodsService.selectPaCopnGoodsTrans(paramMap);
			
			for(HashMap<String, String> paGoods :  paGoodsTargetList) {
				paramMap.put("goodsCode", paGoods.get("GOODS_CODE"));
				paramMap.put("paCode"	, paGoods.get("PA_CODE"));
				
				//EXCEPT 처리 (마진율, 재고, 옴부즈맨 등등)
				paramMap.put("pa_group_code"	, "05");
				paramMap.put("margin_code"		, "60");
				paramMap.put("minprice_code"	, "61");
				paramMap.put("pa_code"			, paGoods.get("PA_CODE"));
				paramMap.put("goods_code"		, paGoods.get("GOODS_CODE"));
				paramMap.replaceCamel();
				
				exceptStr = paCommonService.paExceptGoodsYn(paramMap);
				if(!exceptStr.equals("000000")){
					paGoods.put("RETURN_NOTE"	, exceptStr);
					paGoods.put("PA_STATUS"		, "20");
					paGoods.put("PA_SALE_GB"	, "20");
					paGoods.put("MODIFY_ID"		, PA_COPN_PROC_ID);
					
					PaCopnGoodsVO paCopnGoods = new PaCopnGoodsVO();
					paCopnGoods.setReturnNote	(exceptStr);
					paCopnGoods.setPaStatus		("20");
					paCopnGoods.setPaSaleGb		("20");
					paCopnGoods.setModifyId		(PA_COPN_PROC_ID);
					paCopnGoods.setModifyDate	(DateUtil.toTimestamp(systemService.getSysdatetimeToString(), "yyyy/MM/dd HH:mm:ss"));
					
					paCopnGoods.setGoodsCode	(paGoods.get("GOODS_CODE"));
					paCopnGoods.setPaCode		(paGoods.get("PA_CODE"));
					
					paCopnGoodsService.updatePaCopnGoodsFail(paCopnGoods);
					continue;
				}
				
				//CALL 11번가 GoodsInsert API
				ResponseEntity<?> responseMsg = paCopnGoodsController.goodsInsert(request, paGoods.get("GOODS_CODE"), paGoods.get("PA_CODE"), PA_COPN_PROC_ID , "1" , "1");
				
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){ //API 결과 실패
					resultCnt++;
				}else {
					PaCopnGoodsVO paCopnGoods = new PaCopnGoodsVO();
					paCopnGoods.setPaCode		(paGoods.get("PA_CODE"));
					paCopnGoods.setGoodsCode	(paGoods.get("GOODS_CODE"));
					paCopnGoods.setMassTargetYn	("0");
					paCopnGoodsService.updateMassTargetYn(paCopnGoods);
				}
			}
			
			
			if(paGoodsTargetList.size() != resultCnt) {
				paramMap.put("code"		,"490");
				paramMap.put("message"	, paGoodsTargetList.size() + "건 중"  + resultCnt + "성공" );
			}else {
				paramMap.put("code"		,"200");
				paramMap.put("message"	, paGoodsTargetList.size() + "건 입점 완료" );
			}
			
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				rtnCode = "490";
			} else {
				rtnCode = "500";
			}
			rtnMessage = errorCode(e);
		
		}finally {
			insertApiTracking( request, prg_id, rtnCode, rtnMessage, PA_COPN_PROC_ID);
			
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}		
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), rtnCode , rtnMessage), HttpStatus.OK);
	}
	
	//위메프 대량입점
	@RequestMapping(value = "/pawemp-mass-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> paWempMassInsert(HttpServletRequest request) throws Exception {
		String   rtnCode  				= "200";
		String   rtnMessage				= "OK";
		int		 resultCnt				= 0;
		String 	 prg_id 				= "IF_PAWEMPAPI_01_001(Mass)";
		String	 duplicateCheck			= "0";
		ParamMap paramMap				= new ParamMap();
		String	 exceptStr				= "";
		
		try {
			duplicateCheck 	= systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			paramMap.put("massTargetYn", "1"); 
			List<HashMap<String, String>> paGoodsTargetList = paWempGoodsService.selectPaWempGoodsTrans(paramMap);
			
			for(HashMap<String, String> paGoods :  paGoodsTargetList) {
				paramMap.put("goodsCode", paGoods.get("GOODS_CODE"));
				paramMap.put("paCode"	, paGoods.get("PA_CODE"));
				
				//EXCEPT 처리 (마진율, 재고, 옴부즈맨 등등)
				paramMap.put("pa_group_code"	, "06");
				paramMap.put("margin_code"		, "70");
				paramMap.put("minprice_code"	, "71");
				paramMap.put("pa_code"			, paGoods.get("PA_CODE"));
				paramMap.put("goods_code"		, paGoods.get("GOODS_CODE"));
				paramMap.replaceCamel();
				
				exceptStr = paCommonService.paExceptGoodsYn(paramMap);
				if(!exceptStr.equals("000000")){
					paGoods.put("RETURN_NOTE"	, exceptStr);
					paGoods.put("PA_STATUS"		, "20");
					paGoods.put("MODIFY_ID"		, PA_WEMP_PROC_ID);
					paWempGoodsService.updatePaWempGoodsFail(paGoods);
					continue;
				}
				
				ResponseEntity<?> responseMsg = paWempGoodsController.goodsInsert(request, paGoods.get("GOODS_CODE"), paGoods.get("PA_CODE"), PA_WEMP_PROC_ID , "1", "1");
				
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){ //API 결과 실패
					resultCnt++;
				}else {
					PaWempGoodsVO paWempGoods = new PaWempGoodsVO();
					paWempGoods.setPaCode		(paGoods.get("PA_CODE"));
					paWempGoods.setGoodsCode	(paGoods.get("GOODS_CODE"));
					paWempGoods.setMassTargetYn	("0");
					paWempGoodsService.updateMassTargetYn(paWempGoods);
				}
			}
			
			if(paGoodsTargetList.size() != resultCnt) {
				paramMap.put("code"		,"490");
				paramMap.put("message"	, paGoodsTargetList.size() + "건 중"  + resultCnt + "성공" );
			}else {
				paramMap.put("code"		,"200");
				paramMap.put("message"	, paGoodsTargetList.size() + "건 입점 완료" );
			}
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				rtnCode = "490";
			} else {
				rtnCode = "500";
			}
			rtnMessage = errorCode(e);
		
		}finally {
			insertApiTracking( request, prg_id, rtnCode, rtnMessage, PA_COPN_PROC_ID);
			
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}		
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), rtnCode , rtnMessage), HttpStatus.OK);
	}
	
	//인터파크 대량입점
	@RequestMapping(value = "/paintp-mass-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> paIntpMassInsert(HttpServletRequest request) throws Exception {
		String   rtnCode  				= "200";
		String   rtnMessage				= "OK";
		int		 resultCnt				= 0;
		String 	 prg_id 				= "IF_PAINTPAPI_01_001(Mass)";
		String	 duplicateCheck			= "0";
		ParamMap paramMap				= new ParamMap();
		String dateTime 				= systemService.getSysdatetimeToString();
		
		try {
			//API DUPLICATE CHECK
			duplicateCheck 	= systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			
			paramMap.put("apiCode"		, "IF_PAINTPAPI_01_001");
			paramMap.put("broadCode"	, Constants.PA_INTP_BROAD_CODE);
        	paramMap.put("onlineCode"	, Constants.PA_INTP_ONLINE_CODE);
        	paramMap.put("siteGb"		, PA_INTP_PROC_ID);
			paramMap.put("startDate"	, dateTime);
			paramMap.put("modCase"		, "INSERT");
			paramMap.put("massTargetYn" , "1");
			
			
			HashMap<String, String> apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
						
			//입점 상품 조회 및 11번가 상품입점 API 호출
			paramMap.put("massTargetYn", "1");
			List<HashMap<String,String>> paGoodsTargetList = paIntpGoodsService.selectPaIntpGoodsTrans(paramMap);
			
			for(HashMap<String,String> paGoods:  paGoodsTargetList ) {
				resultCnt += paIntpGoodsController.goodsInsert(paGoods, paramMap, apiInfo, request, "1");
			}
			
			//입점 결과 처리
			if(paGoodsTargetList.size() != resultCnt) {
				paramMap.put("code"		,"490");
				paramMap.put("message"	, paGoodsTargetList.size() + "건 중"  + resultCnt + "성공" );
			}else {
				paramMap.put("code"		,"200");
				paramMap.put("message"	, paGoodsTargetList.size() + "건 입점 완료" );
			}
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				rtnCode = "490";
			} else {
				rtnCode = "500";
			}
			rtnMessage = errorCode(e);
		
		}finally {
			insertApiTracking( request, prg_id, rtnCode, rtnMessage, PA_INTP_PROC_ID);
			
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), rtnCode , rtnMessage), HttpStatus.OK);
	}

	//롯데온 대량입점
	@RequestMapping(value = "/palton-mass-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> paLtonMassInsert(HttpServletRequest request) throws Exception {
		
		String   rtnCode  				= "200";
		String   rtnMessage				= "OK";
		int		 resultCnt				= 0;
		String 	 prg_id 				= "IF_PALTONAPI_01_001(Mass)";
		String	 duplicateCheck			= "0";
		ParamMap paramMap				= new ParamMap();
		String dateTime 				= systemService.getSysdatetimeToString();
		
		try {
			//API DUPLICATE CHECK
			duplicateCheck 	= systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode"		, "IF_PALTONAPI_01_001");
			paramMap.put("paGroupCode"	, Constants.PA_LTON_GROUP_CODE);
			paramMap.put("broadCode"	, Constants.PA_LTON_BROAD_CODE);
        	paramMap.put("onlineCode"	, Constants.PA_LTON_ONLINE_CODE);
        	paramMap.put("siteGb"		, Constants.PA_LTON_PROC_ID);
			paramMap.put("modCase"		, "INSERT");
			paramMap.put("method"		, "POST");
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			HashMap<String, String> apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo"	, paramMap.getString("apiCode"));
						
			paramMap.put("modifyId"	, Constants.PA_LTON_PROC_ID);
			paramMap.put("url"		, apiInfo.get("API_URL"));
			paramMap.put("dateTime"	, dateTime);

			
			paramMap.put("massTargetYn", "1");
			List<HashMap<String, String>> paGoodsTargetList = paLtonGoodsService.selectPaLtonGoodsTrans(paramMap);
			
			for(HashMap<String, String> paGoodsTarget : paGoodsTargetList){
				
				paramMap.put("goodsCode"	, paGoodsTarget.get("GOODS_CODE"));
				paramMap.put("paCode"		, paGoodsTarget.get("PA_CODE"));				
				apiInfo.put("paCode"		, paGoodsTarget.get("PA_CODE"));
				
				if(paLtonGoodsController.excetpCheck(paramMap, paGoodsTarget)) {
					resultCnt += paLtonGoodsController.goodsInsert(request,   apiInfo, paramMap, "1");
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
			
		}catch (Exception e) {
			if(duplicateCheck.equals("1")){
				rtnCode = "490";
			} else {
				rtnCode = "500";
			}
			rtnMessage = errorCode(e);
		
		}finally {
			insertApiTracking( request, prg_id, rtnCode, rtnMessage, PA_INTP_PROC_ID);
			
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), rtnCode , rtnMessage), HttpStatus.OK);		
	}
	
	
	
	//상품 대량수정(공통)
	@RequestMapping(value = "/pa-mass-modify/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> patMassModify(HttpServletRequest request, @PathVariable("paGroupCode") String paGroupCode) throws Exception {
		
		String mssApiCode				= "";
		String apiCode					= "";
		String rtnCode					= "200";
		String rtnMessage				= "OK";
		ParamMap paramMap 				= new ParamMap();
		String	 modifyId				= "";
		String   broadCode				= "";
		String   siteName				= "";
		String 	 onlineCode				= "";
		
		try {		
			switch (paGroupCode) {

			case "01": //11번가
				mssApiCode  = "IF_PA11STAPI_01_002(MASS)";  //가칭
				apiCode		= "IF_PA11STAPI_01_002";
				broadCode   = Constants.PA_11ST_BROAD_CODE;
				onlineCode	= Constants.PA_11ST_ONLINE_CODE;
				modifyId	= PA_11ST_PROC_ID;
				siteName	= "PA11ST";
				break; //G마켓

			case "02":
				mssApiCode  = "IF_PAGMKTAPI_V2_01_003(MASS)";
				modifyId	= PA_GMKT_PROC_ID;
				siteName    = "PAGMKT";
				break;
				
			case "05":
				mssApiCode  = "IF_PACOPNAPI_01_005(MASS)";
				apiCode		= "IF_PACOPNAPI_01_005";
				modifyId 	= PA_COPN_PROC_ID;
				siteName	= "PACOPN";
				broadCode   = Constants.PA_COPN_BROAD_CODE;
				onlineCode	= Constants.PA_COPN_ONLINE_CODE;
				break;		
				
			case "06":
				mssApiCode  = "IF_PAWEMPAPI_01_002(MASS)";
				apiCode		= "IF_PAWEMPAPI_01_002";
				broadCode	= Constants.PA_WEMP_BROAD_CODE;
				onlineCode	= Constants.PA_WEMP_ONLINE_CODE;
				modifyId	= PA_WEMP_PROC_ID;
				siteName	= "PAWEMP";
			
			case "07":
				mssApiCode  = "IF_PAINTPAPI_01_002(MASS)";
				apiCode		= "IF_PAINTPAPI_01_002";
				broadCode	= Constants.PA_INTP_BROAD_CODE;
				onlineCode	= Constants.PA_INTP_ONLINE_CODE;
				modifyId	= PA_INTP_PROC_ID;
				siteName	= "PAINTP";
				
				
			case "08":
				mssApiCode  = "IF_PALTONAPI_01_005(MASS)";
				apiCode		= "IF_PALTONAPI_01_005";				
				broadCode	= Constants.PA_LTON_BROAD_CODE;
				onlineCode	= Constants.PA_LTON_ONLINE_CODE;
				modifyId	= PA_LTON_PROC_ID;
				
			default:
				//Throw Exception
				break;
			}
			
		
			HashMap<String, String> apiInfo = new HashMap<String, String>();
			
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode"		, apiCode);
			paramMap.put("broadCode"	, broadCode);
			paramMap.put("onlineCode"	, onlineCode);
			paramMap.put("startDate"	, dateTime);
			paramMap.put("modCase"		, "MODIFY");
			paramMap.put("siteName"		, siteName);
			paramMap.put("dateTime"		, dateTime);
			paramMap.put("goodsListTime", dateTime);
			paramMap.put("modifyId"		, modifyId);
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));			
			//paramMap.put("syncMethod"	, "MASS"); 		
			
			switch (paGroupCode) {
			
			case "01":
				apiInfo.put("contentType", "text/xml;charset=utf-8");
				pa11stGoodsModify(request, paramMap, apiInfo);
				break;
			
			case "02":
				pagmktGoodsModify	(request, paramMap);
				break;
				
			case "05":
				paCopnGoodsModify	(request , paramMap , apiInfo );
				break;
				
			case "06":
				paWempGoodsModify	(request , paramMap , apiInfo );
				break;
			
			case "07":
				apiInfo.put("contentType", "text/xml;charset=utf-8");
				paIntpGoodsModify	(request, paramMap, apiInfo);
				break;
			case "08":
				apiInfo.put("contentType", "text/xml;charset=utf-8");
				paLtonGoodsModify   (request, paramMap, apiInfo);
				break;
			}
					
		}catch (Exception e) {
			rtnCode	   = "500";
			rtnMessage = errorCode(e);
		}finally {
			insertApiTracking(request, mssApiCode, rtnCode, rtnMessage, "PA11");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), rtnCode , rtnMessage), HttpStatus.OK);
	}

	//11번가 대량 수정
	private void pa11stGoodsModify(HttpServletRequest request , ParamMap paramMap , HashMap<String, String> apiInfo) throws Exception {
		
		// 상품 단품 수정
		
		String massTargetEp =   getMassEpCode();
		paramMap.put("massTargetEp"	, massTargetEp);
		paramMap.put("massTargetYn"	, "2");
		paramMap.put("paGroupCode"	, "01");
		
		List<Pa11stGoodsVO> pa11stGoodsList   = pa11stGoodsService.selectPa11stGoodsInfoListMass	(paramMap);
		List<PaGoodsPriceVO> paGoodsPriceList = pa11stGoodsService.selectPa11stGoodsPriceListMass	(paramMap);
			
		for(Pa11stGoodsVO asyncPa11stGoods : pa11stGoodsList){
			try {
				//1)MODIFY 11ST_GOODS 
				paramMap.put("goodsCode"	, asyncPa11stGoods.getGoodsCode());
				paramMap.put("paCode"		, asyncPa11stGoods.getPaCode());
				paramMap.put("modCase"		, "MODIFY");
				pa11stGoodsController.goodsModify11st(request, asyncPa11stGoods, paramMap, apiInfo);		
				
			}catch (Exception e) {
				log.error(e.toString());
				//UPDATE PA11STGOODS.MASS_TARGET_YN = 9 
				asyncPa11stGoods.setMassTargetYn("9");
				pa11stGoodsService.updateMassTargetYn(asyncPa11stGoods);
				continue;
			}
		}
		
		for(PaGoodsPriceVO vo : paGoodsPriceList ) {
			ResponseEntity<?> responseMsg = pa11stGoodsController.goodspriceCouponModify(request,"", "", paramMap.getString("modifyId"), "1", vo);
			
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){ //API 결과 실패
				log.error(PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				
				Pa11stGoodsVO asyncPa11stGoods = new Pa11stGoodsVO();
				asyncPa11stGoods.setPaCode		 (vo.getPaCode());
				asyncPa11stGoods.setGoodsCode	 (vo.getGoodsCode());
				asyncPa11stGoods.setMassTargetYn ("9");
				pa11stGoodsService.updateMassTargetYn(asyncPa11stGoods);
			}
		}
		
		HashMap<String, String> massMap = new HashMap<String, String>();
		massMap.put("massTargetYn"	, "0");
		massMap.put("massTargetEp"	, massTargetEp);
		pa11stGoodsService.updateMassTargetYnByEpCode(massMap);
	}
	
	//G마켓 대량 수정		
	private void pagmktGoodsModify(HttpServletRequest request, ParamMap paramMap) throws Exception {
		HashMap<String,Object> target = new HashMap<String,Object>();
		target.put("rowNum", paramMap.get("rowNum"));
		
		String massTargetEp =   getMassEpCode();
		paramMap.put("massTargetEp"	, massTargetEp);
		paramMap.put("massTargetYn"	, "2");
		paramMap.put("paGroupCode"	, "02");
		paramMap.put("modCase", Constants.PA_GMKT_MOD_CASE_MODIFY);
		List<HashMap<String,String>> targetGoodsList = paGmktGoodsService.selectPagmktGoodsInfoListMass(target);
		
		for(HashMap<String,String> targetGoods : targetGoodsList){
			try {
				//1)MODIFY 11ST_GOODS 
				ResponseEntity<?> responseMsg = paGmktV2GoodsController.putGoods(request, targetGoods.get("PA_CODE").toString() , targetGoods.get("GOODS_CODE").toString() , "BO", "1");	
				
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
					throw new Exception(PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				}
				//3)UPDATE PA11STGOODS.MASS_TARGET_YN = 0
				targetGoods.put("MASS_TARGET_YN"	, "0");
				paGmktGoodsService.updateMassTargetYn(targetGoods);
				
			}catch (Exception e) {
				log.error(errorCode(e));
				//UPDATE PA11STGOODS.MASS_TARGET_YN = 9 
				targetGoods.put("MASS_TARGET_YN"	, "9");
				paGmktGoodsService.updateMassTargetYn(targetGoods);
				continue;
			}
		}
	}
	
	//쿠팡 대량 수정  @Async 쓰려면 private 제거
	private void paCopnGoodsModify(HttpServletRequest request , ParamMap paramMap , HashMap<String, String> apiInfo) throws Exception {

		//1)
		String massTargetEp =   getMassEpCode();
		paramMap.put("massTargetEp"	, massTargetEp);
		paramMap.put("massTargetYn"	, "2");
		paramMap.put("paGroupCode"	, "05");
		List<PaCopnGoodsVO> paCopnGoodsList 	= paCopnGoodsService.selectPaCopnGoodsInfoListMass	(paramMap);
		List<PaGoodsPriceVO> paGoodsPriceList 	= paCopnGoodsService.selectCopnPriceModifyMass		(paramMap);
		
		for(PaCopnGoodsVO paCopnGoods : paCopnGoodsList){
			try {
				
				//1)MODIFY 11ST_GOODS 
				paramMap.put("modCase"		, "MODIFY");
				paramMap.put("goodsCode"	, paCopnGoods.getGoodsCode());
				paramMap.put("paCode"		, paCopnGoods.getPaCode());
				paramMap.put("modifyId"		, PA_COPN_PROC_ID);
				//paramMap.put("paAddrGb"		, "30");
				paCopnGoodsController.goodsModifyCopn(request, apiInfo, paramMap, paCopnGoods);		
				
			}catch (Exception e) {
				log.error(e.toString());
				//UPDATE PA11STGOODS.MASS_TARGET_YN = 9 
				paCopnGoods.setMassTargetYn("9");
				paCopnGoodsService.updateMassTargetYn(paCopnGoods);
				continue;
			}
		}
	
		for(PaGoodsPriceVO goodsPrice : paGoodsPriceList) {
			
			try {
				paCopnGoodsController.goodsPriceModifyCopn(request, paramMap, goodsPrice,  "1");	
			}catch (Exception e) {
				log.error(e.toString());
				PaCopnGoodsVO paCopnGoods = new PaCopnGoodsVO();
				paCopnGoods.setPaCode					(goodsPrice.getPaCode());
				paCopnGoods.setGoodsCode				(goodsPrice.getGoodsCode());
				paCopnGoods.setMassTargetYn				("9");
				paCopnGoodsService.updateMassTargetYn	(paCopnGoods);
				continue;
			}
		}
		
		HashMap<String, String> massMap = new HashMap<String, String>();
		massMap.put("massTargetYn"	, "0");
		massMap.put("massTargetEp"	, massTargetEp);
		paCopnGoodsService.updateMassTargetYnByEpCode(massMap);
	}
	
	//위메프 대량 수정
	private void paWempGoodsModify(HttpServletRequest request , ParamMap paramMap , HashMap<String, String> apiInfo) throws Exception {
		
		String massTargetEp =   getMassEpCode();
		paramMap.put("massTargetEp"	, massTargetEp);
		paramMap.put("massTargetYn"	, "2");
		paramMap.put("paGroupCode"	, "06");
		List<PaWempGoodsVO> paWempGoodsList 	= paWempGoodsService.selectPaWempGoodsInfoListMass (paramMap);
		
		for(PaWempGoodsVO paWempGoods : paWempGoodsList){
			try {
				paramMap.put("goodsCode", paWempGoods.getGoodsCode());
				paramMap.put("paCode"	, paWempGoods.getPaCode());
				paramMap.put("siteGb"	, Constants.PA_WEMP_PROC_ID);
				
				paWempGoodsController.goodsModifyWemp(request, paramMap, apiInfo, paWempGoods);
				
			}catch (Exception e) {
				log.error(e.toString());
				//UPDATE PA11STGOODS.MASS_TARGET_YN = 9 
				paWempGoods.setMassTargetYn("9");
				paWempGoodsService.updateMassTargetYn(paWempGoods);
				continue;
			}
		}
		
		HashMap<String, String> massMap = new HashMap<String, String>();
		massMap.put("massTargetYn"	, "0");
		massMap.put("massTargetEp"	, massTargetEp);
		paWempGoodsService.updateMassTargetYnByEpCode(massMap);		
	}
	
	//인터파크 대량 수정
	private void paIntpGoodsModify(HttpServletRequest request , ParamMap paramMap , HashMap<String, String> apiInfo) throws Exception {
		
		String massTargetEp =   getMassEpCode();
		paramMap.put("massTargetEp"	, massTargetEp);
		paramMap.put("massTargetYn"	, "2");
		paramMap.put("paGroupCode"	, "07");
		List<PaIntpGoodsVO> paIntpGoodsList 	= paIntpGoodsService.selectPaIntpGoodsInfoListMass (paramMap);
		
		for(PaIntpGoodsVO paIntpGoods : paIntpGoodsList){
			try {
				paramMap.put("goodsCode", paIntpGoods.getGoodsCode());
				paramMap.put("paCode"	, paIntpGoods.getPaCode());
				paramMap.put("siteGb"	, Constants.PA_INTP_PROC_ID);
				
				paIntpGoodsController.goodsModifyIntp(request, apiInfo, paramMap, paIntpGoods);
				
			}catch (Exception e) {
				log.error(e.toString());
				//UPDATE PA11STGOODS.MASS_TARGET_YN = 9 
				paIntpGoods.setMassTargetYn("9");
				paIntpGoodsService.updateMassTargetYn(paIntpGoods);
				continue;
			}
		}
		
		HashMap<String, String> massMap = new HashMap<String, String>();
		massMap.put("massTargetYn"	, "0");
		massMap.put("massTargetEp"	, massTargetEp);
		paIntpGoodsService.updateMassTargetYnByEpCode(massMap);		
	}
	
	//롯데온 대량 수정
	private void paLtonGoodsModify(HttpServletRequest request , ParamMap paramMap , HashMap<String, String> apiInfo) throws Exception {
		
		String massTargetEp =   getMassEpCode();
		paramMap.put("massTargetEp"	, massTargetEp);
		paramMap.put("massTargetYn"	, "2");
		paramMap.put("method"		, "POST");
		paramMap.put("paGroupCode"	, Constants.PA_LTON_GROUP_CODE);
		
		List<PaLtonGoodsVO> paltonGoodsList 	= paLtonGoodsService.selectPaLtonGoodsInfoListMass (paramMap);

		for(PaLtonGoodsVO paltonGoods : paltonGoodsList){
			try {
				paramMap.put("goodsCode", paltonGoods.getGoodsCode());
				paramMap.put("paCode"	, paltonGoods.getPaCode());
				paramMap.put("siteGb"	, Constants.PA_LTON_PROC_ID);
				
				paLtonGoodsController.paLtonGoodsModify(request, paltonGoods, apiInfo, paramMap);
				
			}catch (Exception e) {
				log.error(e.toString());
				//UPDATE PA11STGOODS.MASS_TARGET_YN = 9 
				paltonGoods.setMassTargetYn("9");
				paLtonGoodsService.updateMassTargetYn(paltonGoods);
				continue;
			}
		}
		
		HashMap<String, String> massMap = new HashMap<String, String>();
		massMap.put("massTargetYn"	, "0");
		massMap.put("massTargetEp"	, massTargetEp);
		paLtonGoodsService.updateMassTargetYnByEpCode(massMap);		
	}
	
	*/
				
	private String getMassEpCode() {
		String massTargetEp = "";
		
		try {
			//massTargetEp = systemService.getSysdatetimeToString();	 //TODO 시퀀스로 수정 필요??
			//massTargetEp =((massTargetEp.replace("/", "")).replace(":", "")).replace(" ","").trim(); 
			massTargetEp = systemService.getSequenceNo("SEQ_MASS_EP");
			
		}catch (Exception e) {
			log.error(e.toString());
		}	
			
		return massTargetEp;
	}
	
	//API Tracking
	private void insertApiTracking(HttpServletRequest request, String prg_id , String rtnCode ,String rtnMessage, String SiteGb) {
		ParamMap paramMap = new ParamMap();
		try{
			String dateTime 		= systemService.getSysdatetimeToString();
			paramMap.put("apiCode"		, prg_id);
			paramMap.put("code"			, rtnCode);
			paramMap.put("message"		, rtnMessage);
			paramMap.put("siteGb"		, SiteGb);
			paramMap.put("startDate"	, dateTime);
			systemService.insertApiTrackingTx(request,paramMap);
		}catch (Exception e) {
			log.error(errorCode(e));
		}
	}
	
	private String errorCode(Exception e) {
		String rtnMessage = "";
		
		if(e.getMessage()!=null){
			rtnMessage =  e.getMessage();
			
		}else{
			rtnMessage = e.toString();
		}
		rtnMessage = rtnMessage.length() > 3950 ? rtnMessage.substring(0, 3950) : rtnMessage;
		
		return rtnMessage;
	}
	
}

