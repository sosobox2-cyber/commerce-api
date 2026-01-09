package com.cware.api.palton.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaLtonAttrList;
import com.cware.netshopping.domain.model.PaLtonDispCtgr;
import com.cware.netshopping.domain.model.PaLtonDispList;
import com.cware.netshopping.domain.model.PaLtonPdItmsList;
import com.cware.netshopping.domain.model.PaLtonRetrieveCode;
import com.cware.netshopping.domain.model.PaLtonSettlement;
import com.cware.netshopping.domain.model.PaLtonStdCtgr;
import com.cware.netshopping.palton.common.service.PaLtonCommonService;
import com.cware.netshopping.palton.util.PaLtonComUtill;
import com.cware.netshopping.palton.util.PaLtonConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/palton/common", description="롯데온 공통")
@Controller("com.cware.api.palton.PaLtonCommonController")
@RequestMapping(value = "/palton/common")
public class PaLtonCommonController extends AbstractController  {
	
	private transient static Logger log = LoggerFactory.getLogger(PaLtonCommonController.class);
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Autowired
	private PaLtonCommonService paLtonCommonService;
	
	@Autowired
	PaLtonConnectUtil paLtonConnectUtil;
	
	/**
	 * 전시카테고리 조회 API
	 * @param request
	 * @param skip
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/goodsKinds-display-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsKindsDisplayList(HttpServletRequest request,
			@ApiParam(name="skip", required=false, value="조회시작번호", defaultValue = "0") @RequestParam(value="skip", required=false, defaultValue = "0") int skip,
			@ApiParam(name="limit", required=false, value="페이지별 목록 개수", defaultValue = "100") @RequestParam(value="limit", required=false, defaultValue = "100") int limit) throws Exception {
	
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		
		int totalSize = 0;
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		String prg_id = "IF_PALTONAPI_00_005";
		String duplicateCheck = "";
	
		ParamMap paramMap = new ParamMap();
		ParamMap requestInfoMap = new ParamMap();
		boolean flag = true;
		
		paramMap.put("method", "GET");
		paramMap.put("url", ConfigUtil.getString("PALTON_COMMON_URL") + "?job=cheetahDisplayCategory&skip=" + skip + "&limit=" + limit);
		paramMap.put("apiCode", prg_id);
		paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
		
		try {
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if ("1".equals(duplicateCheck))	throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			while(flag) {
				paramMap.put("url", ConfigUtil.getString("PALTON_COMMON_URL") + "?job=cheetahDisplayCategory&direction=asc&sort=sort_2&skip=" + skip + "&limit=" + limit);
				
				map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, requestInfoMap);
				ArrayList<?> arr = (ArrayList<Map<String, Object>>) map.get("itemList");
				totalSize = arr.size();
				
				for(int i = 0; i < totalSize; i++) {
					PaLtonDispCtgr displayData = new PaLtonDispCtgr();
					map = (Map<String, Object>) arr.get(i);
					resultData = (Map<String, Object>) PaLtonComUtill.replaceCamel((Map<String, Object>) map.get("data"));
					
					if(resultData.size() != 0 && resultData != null) {
						
						displayData = (PaLtonDispCtgr) PaLtonComUtill.map2VO(resultData, PaLtonDispCtgr.class);
						
						rtnMsg = paLtonCommonService.insertPaDisplayCategory(displayData);
						
						if(!rtnMsg.equals(Constants.SAVE_SUCCESS)) {
							paramMap.put("code", "400");
							paramMap.put("message", rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						}
					}
				}
				
				if(totalSize == limit) {
					flag = true;
					skip = skip + 100;
				} else {
					flag = false;
				}
				
			}
			
		} catch(Exception e) {
			paramMap.put("code"   , ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(duplicateCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		} finally {
			
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
			
			if(totalSize == limit) {
				//goodsKindsDisplayList(request, skip += 100, limit);
			} else {
				paramMap.put("code", "200");
				paramMap.put("message", "success");
				systemService.insertApiTrackingTx(request, paramMap);
			}
			log.info("===== 전시카테고리 조회 API End =====");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 표준카테고리 조회 API
	 * @param request
	 * @param skip
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/goodsKinds-std-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsKindsStandardList(HttpServletRequest request,
			@ApiParam(name="skip", required=false, value="조회시작번호", defaultValue = "0") @RequestParam(value="skip", required=false, defaultValue = "0") int skip,
			@ApiParam(name="limit", required=false, value="페이지별 목록 개수", defaultValue = "100") @RequestParam(value="limit", required=false, defaultValue = "100") int limit) throws Exception {
	
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		
		ArrayList<Map<String, Object>> dispList = new ArrayList<Map<String, Object>>();
		//ArrayList<Map<String, Object>> attrList = new ArrayList<Map<String, Object>>();
		//ArrayList<Map<String, Object>> pdItmsList = new ArrayList<Map<String, Object>>();
		
		PaLtonDispList dis = new PaLtonDispList();
		//PaLtonAttrList attr = new PaLtonAttrList();
		//PaLtonPdItmsList pdItms = new PaLtonPdItmsList();
		
		List<PaLtonDispList> disInfoList = null;
		List<PaLtonAttrList> attrInfoList = null;
		List<PaLtonPdItmsList> pdItmsInfoList = null;
		
		int totalSize = 0;
		
		boolean flag = true;
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		String prg_id = "IF_PALTONAPI_00_007";
		String duplicateCheck = "";
	
		ParamMap paramMap = new ParamMap();
		ParamMap requestInfoMap = new ParamMap();
		paramMap.put("method", "GET");
		paramMap.put("url", ConfigUtil.getString("PALTON_COMMON_URL") + "?job=cheetahStandardCategory&direction=asc&sort=sort_2&skip=" + skip + "&limit=" + limit);
		paramMap.put("apiCode", prg_id);
		paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if ("1".equals(duplicateCheck))	throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			while(flag) {
				
				paramMap.put("url", ConfigUtil.getString("PALTON_COMMON_URL") + "?job=cheetahStandardCategory&direction=asc&sort=sort_2&skip=" + skip + "&limit=" + limit);
				map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, requestInfoMap);
				ArrayList<?> arr = (ArrayList<Map<String, Object>>) map.get("itemList");
				totalSize = arr.size();
				
				for(int i = 0; i < totalSize; i++) {
					PaLtonStdCtgr stdCtgrData = new PaLtonStdCtgr();
					disInfoList    = new ArrayList<PaLtonDispList>();
					//attrInfoList   = new ArrayList<PaLtonAttrList>();
					//pdItmsInfoList = new ArrayList<PaLtonPdItmsList>();
					
					map = (Map<String, Object>) arr.get(i);
					resultData = (Map<String, Object>) PaLtonComUtill.replaceCamel((Map<String, Object>) map.get("data"));
					
					if(resultData.size() != 0 && resultData != null) {
						dispList = (ArrayList<Map<String, Object>>) resultData.get("dispList");
						//attrList = (ArrayList<Map<String, Object>>) resultData.get("attrList");
						//pdItmsList = (ArrayList<Map<String, Object>>) resultData.get("pdItmsList");
					if(!dispList.isEmpty()) {
						PaLtonComUtill.replaceCamelList(dispList);
						for(int k = 0; k < dispList.size(); k++) { //전시카테고리 리스트
							dis = new PaLtonDispList();
							dis.setStdCatId(String.valueOf(dispList.get(k).get("stdCatId")));
							dis.setMallDvsCd(String.valueOf(dispList.get(k).get("mallDvsCd")));
							dis.setDispCatId(String.valueOf(dispList.get(k).get("dispCatId")));
							
							disInfoList.add(dis);
						}
					}
						/*
					if(!attrList.isEmpty()) {
						PaLtonComUtill.replaceCamelList(attrList);
						for(int k = 0; k < attrList.size(); k++) { //속성유형 리스트
							attr = new PaLtonAttrList();
							attr.setStdCatId(String.valueOf(attrList.get(k).get("stdCatId")));
							attr.setAttrPiType(String.valueOf(attrList.get(k).get("attrPiType")));
							attr.setAttrId(Integer.parseInt(String.valueOf(attrList.get(k).get("attrId"))));
							attr.setPrioRnk(Integer.parseInt(String.valueOf(attrList.get(k).get("prioRnk"))));
							
							attrInfoList.add(attr);
						}
					}
					
					if(!pdItmsList.isEmpty()) {
						PaLtonComUtill.replaceCamelList(pdItmsList);
						for(int k = 0; k < pdItmsList.size(); k++) { //상품품목고시 정보 리스트
							pdItms = new PaLtonPdItmsList();
							pdItms.setStdCatId(String.valueOf(pdItmsList.get(k).get("stdCatId")));
							pdItms.setPdItmsCd(String.valueOf(pdItmsList.get(k).get("pdItmsCd")));
							
							pdItmsInfoList.add(pdItms);
						}
					}
						 */
						stdCtgrData = (PaLtonStdCtgr) PaLtonComUtill.map2VO(resultData, PaLtonStdCtgr.class);
						
						rtnMsg = paLtonCommonService.saveLtonStdCategoryInfoTx(disInfoList, attrInfoList, pdItmsInfoList, stdCtgrData);
						
						if(!rtnMsg.equals(Constants.SAVE_SUCCESS)) {
							paramMap.put("code", "400");
							paramMap.put("message", rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						}
						
					}
					
				} //for
				
				if(totalSize == limit) {
					flag = true;
					skip = skip + 100;
				} else {
					flag = false;
				}
				
			}
			
		} catch(Exception e) {
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage());
			log.error(e.getMessage());
		} finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
			//System.out.println(totalSize);
			if(totalSize == limit) {
				//goodsKindsStandardList(request, skip += 100, limit); // 최대 100개씩(limit의 default가 100) 조회 가능, 처음 조회시 : 0~99(100개), 다음 조회 : 100~199(100개)...
			} else {
				paramMap.put("code", "200");
				paramMap.put("message", "success");
				systemService.insertApiTrackingTx(request, paramMap);
			}
			
			log.info("===== 표준카테고리 조회 API End =====");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 브랜드 조회 API
	 * @param request
	 * @param skip
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/brand-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> brandList(HttpServletRequest request,
			@ApiParam(name="skip", required=false, value="조회시작번호", defaultValue = "0") @RequestParam(value="skip", required=false, defaultValue = "0") int skip,
			@ApiParam(name="limit", required=false, value="페이지별 목록 개수", defaultValue = "100") @RequestParam(value="limit", required=false, defaultValue = "100") int limit) throws Exception {
	
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		
		int totalSize = 0;
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		String prg_id = "IF_PALTONAPI_00_008";
		String duplicateCheck = "";
		PaBrand paBrand = null;
	
		ParamMap paramMap = new ParamMap();
		ParamMap requestInfoMap = new ParamMap();
		paramMap.put("method", "GET");
		paramMap.put("url", ConfigUtil.getString("PALTON_COMMON_URL") + "?job=cheetahBrnd&skip=" + skip + "&limit=" + limit);
		paramMap.put("apiCode", prg_id);
		paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if ("1".equals(duplicateCheck))	throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, requestInfoMap);
			List<PaBrand> paBrandList = new ArrayList<PaBrand>();
			ArrayList<?> arr = (ArrayList<Map<String, Object>>)map.get("itemList");
			totalSize = arr.size();
			
			for(int i = 0; i < totalSize; i++) {
				map = (Map<String, Object>) arr.get(i);
				resultData = (Map<String, Object>) PaLtonComUtill.replaceCamel((Map<String, Object>)map.get("data"));
				
				paBrand = new PaBrand();
				paBrand.setPaGroupCode(Constants.PA_LTON_GROUP_CODE);
				paBrand.setPaBrandNo(String.valueOf(resultData.get("brndId")));
				paBrand.setPaBrandName(String.valueOf(resultData.get("brndNm")));
				paBrand.setUseYn(String.valueOf(resultData.get("useYn")).equals("Y") ? "1" : "0");
				
				paBrandList.add(paBrand);
			}
			
			rtnMsg = paLtonCommonService.saveLtonBrandTx(paBrandList);
			
			if(!rtnMsg.equals(Constants.SAVE_SUCCESS)) {
				paramMap.put("code", "400");
				paramMap.put("message", rtnMsg);
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			
		} catch(Exception e) {
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage());
			log.error(e.getMessage());
		} finally {
			
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
			
			if(totalSize == limit) {
				brandList(request, skip += 100, limit); // 최대 100개씩(limit의 default가 100) 조회 가능, 처음 조회시 : 0~99(100개), 다음 조회 : 100~199(100개)...
			} else {
				paramMap.put("code", "200");
				paramMap.put("message", "success");
				systemService.insertApiTrackingTx(request, paramMap);
			}
			log.info("===== 롯데ON 브랜드 조회 API End =====");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 공통코드 상세조회
	 * @param request
	 * @param skip
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieve-common-code", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> retrieveCommonCode(HttpServletRequest request,
			@ApiParam(name="grpCd", required=true, value="그룹코드") @RequestParam(value="grpCd", required=true) String grpCd) throws Exception {
	
		Map<String, Object> map = new HashMap<String, Object>();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		List<PaLtonRetrieveCode> paLtonOrigin = null;
		PaLtonRetrieveCode paLtonRetrieveCode = null;
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		String prg_id = "IF_PALTONAPI_00_009";
		String duplicateCheck = "";
	
		ParamMap paramMap = new ParamMap();
		ParamMap requestInfoMap = new ParamMap();
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if ("1".equals(duplicateCheck))	throw processException("msg.batch_process_duplicated", new String[] {prg_id});
		
			paramMap.put("method", "GET");
			paramMap.put("apiCode", prg_id);
			paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);
			paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			
			paramMap.put("url", apiInfo.get("API_URL") + "?grpCd=" + grpCd);
			
			map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, requestInfoMap);
			
			if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("returnCode"))){
				ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String,Object>>) map.get("data");
				paLtonOrigin = new ArrayList<PaLtonRetrieveCode>();
				
				for(int i = 0; i < data.size(); i++) {
					paLtonRetrieveCode = new PaLtonRetrieveCode();
					Map<String, Object> resultData = data.get(i);
					
					paLtonRetrieveCode = (PaLtonRetrieveCode) PaLtonComUtill.map2VO(resultData, PaLtonRetrieveCode.class);
					paLtonOrigin.add(paLtonRetrieveCode);
				}
				rtnMsg = paLtonCommonService.insertPaLtonOrigin(paLtonOrigin);
			}
			
			if(!rtnMsg.equals(Constants.SAVE_SUCCESS)) {
				paramMap.put("code", "400");
				paramMap.put("message", rtnMsg);
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
		} catch(Exception e) {
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage());
			log.error(e.getMessage());
		} finally {
			
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
			systemService.insertApiTrackingTx(request, paramMap);
			
			log.info("===== 롯데ON 공통코드 상세조회 =====");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 반품지,출고지 등록
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "출고지/반품지등록", notes = "출고지/반품지등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."),
		    @ApiResponse(code = 500, message = "시스템 오류") 
			   })
	@RequestMapping(value = "/entpslip-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpSlipInsert(HttpServletRequest request,
			@ApiParam(name="entpCode", 	 	value="업체코드", 		defaultValue = "") @RequestParam(value="entpCode", required=true) String entpCode,
			@ApiParam(name="entpManSeq", 	value="업체담당자순번", defaultValue = "") @RequestParam(value="entpManSeq", required=true) String entpManSeq,
			@ApiParam(name="addrGb",	 	value="등록구분", 		defaultValue = "") @RequestParam(value="addrGb", required=true) String addrGb,
			@ApiParam(name="paCode", 	 	value="제휴사코드",    defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,
			@ApiParam(name="searchTermGb", 	value="중복체크",   	defaultValue = "") @RequestParam(value="searchTermGb", required=true) String searchTermGb) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		Map<String,Object> paramSlip = new HashMap<String,Object>();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		ArrayList<Map<String, Object> > list = new ArrayList<Map<String,Object>>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Map<String, Object> map = new HashMap<String, Object>() ;
		
		String dateTime = "";
		String duplicateCheck = "";
		
		log.info("===== 반품지/출고지 등록 API Start =====");
		log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PALTONAPI_00_001";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
		paramMap.put("paCode", paCode);
		
		try{			
			log.info("02. 반품지/출고지 등록 API 중복실행검사");
			//= 중복 실행 Check
			if(!"1".equals(searchTermGb)) {
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			}
			
		
			apiInfo = systemService.selectPaApiInfo(paramMap);
			
			paramMap.put("method", "POST");
			paramMap.put("url", apiInfo.get("API_URL"));
			
			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb(addrGb);
			paEntpSlip.setPaCode(paCode);
			
			Map<String,Object> entpSlipMap = paLtonCommonService.selectSlipInsertList(paEntpSlip);				
		    if(entpSlipMap == null) {
				paramMap.put("code","404");
				paramMap.put("message",getMessage("partner.no_change_data"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		    }
		    
		    if(paCode.equals(Constants.PA_LTON_BROAD_CODE)){
				entpSlipMap.put("afflLrtrCd", apiInfo.get(Constants.PA_BROAD));					//하위 거래처 번호
			} else {
				entpSlipMap.put("afflLrtrCd", apiInfo.get(Constants.PA_ONLINE));
			}
		    entpSlipMap.put("afflTrCd",  ConfigUtil.getString("PALTON_ENTP_CODE"));				//상위 거래처 번호
		    entpSlipMap.put("paCode", paCode);

		   
		    log.info("03.반품지/출고지 등록 API 파라미터 생성");
		    paramSlip = entpSlipMapping(entpSlipMap);		  
		    list.add(paramSlip);
		    
		    try {
		    	log.info("05.반품지/출고지 등록 API 호출");
		    	map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, list);			
					
		    	if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("returnCode"))){
		    		
		    		ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String,Object>>) map.get("data");
		    		Map<String, Object> result =  (Map<String, Object>)data.get(0);
		    		
					// 출고지 INSERT
					log.info("05-1.반품지/출고지 등록 API 성공 : ");
					paEntpSlip.setPaAddrSeq(result.get("dvpNo").toString());
					paEntpSlip.setTransTargetYn("0");
					paEntpSlip.setInsertId(Constants.PA_LTON_PROC_ID);
					paEntpSlip.setModifyId(Constants.PA_LTON_PROC_ID);
					paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
					rtnMsg = paLtonCommonService.savePaLtonEntpSlipTx(paEntpSlip);
					log.info("05-2.반품지/출고지 등록 API 저장 : "+rtnMsg);
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						paramMap.put("code","404");
						paramMap.put("message",rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else {
						paramMap.put("code","200");
						paramMap.put("message","OK");
					}
				} else {
					paramMap.put("code","404");
					paramMap.put("message",map.get("message"));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			} catch(Exception e){ 
				String errMsg = e.getMessage();
				paramMap.put("code","500");
				paramMap.put("message", errMsg);
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
		}catch (Exception e) {
			if(duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",e.getMessage().length() > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}
		finally {
			try {
				paramMap.put("message", "entp_code" + ":" + entpCode + ">" + paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(!"1".equals(searchTermGb)) {
				if(duplicateCheck.equals("0")) {
					systemService.checkCloseHistoryTx("end", prg_id);
				}				
			}
			
			log.info("05.저장 완료 API END");
			log.info("===== 반품지/출고지 등록 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}
	
	/**
	 * 반품지/출고지 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "출고지/반품지수정", notes = "출고지/반품지수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."),
		    @ApiResponse(code = 500, message = "시스템 오류") 
			   })
	@RequestMapping(value = "/entpslip-update", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpSlipUpdate(HttpServletRequest request) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		PaEntpSlip paEntpSlip = new PaEntpSlip();
		HashMap<String, Object> entpSlipMap = null;
		Map<String, Object> map = new HashMap<String, Object>() ;
		Map<String, Object> paramSlip = new HashMap<String, Object>() ;
		
		String paCode = "";		
		String dateTime = systemService.getSysdatetimeToString();
		String duplicateCheck = "";
		int procCount = 0;
		int targetCount = 0;
		
		List<HashMap<String,Object>> entpSlipUpdateList = null;
		
		log.info("===== 반품지/출고지 수정 API Start =====");
		log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PALTONAPI_00_002";
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
		
		try{
			
			log.info("02. 반품지/출고지 수정 API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			log.info("03.반품지/출고지 수정 대상 리스트 조회");
			entpSlipUpdateList = paLtonCommonService.selectEntpSlipUpdateList();
			
			targetCount += entpSlipUpdateList.size();
			
			if(entpSlipUpdateList.size() <= 0) {
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("partner.no_change_data"));
			}else {
				for(int i= 0; i< entpSlipUpdateList.size(); i++) {
					ArrayList<Map<String, Object> > list = new ArrayList<Map<String,Object>>();
			
					entpSlipMap = (HashMap<String, Object>) entpSlipUpdateList.get(i);	
					
					paCode = entpSlipMap.get("PA_CODE").toString();
					paramMap.put("paCode", paCode);
					paramMap.put("method", "POST");
					paramMap.put("url", apiInfo.get("API_URL"));
					entpSlipMap.put("paCode", paCode);
		    
					if(entpSlipMap.get("PA_CODE").equals(Constants.PA_LTON_BROAD_CODE)) {
						entpSlipMap.put("afflLrtrCd", apiInfo.get(Constants.PA_BROAD));
					}else {
						entpSlipMap.put("afflLrtrCd", apiInfo.get(Constants.PA_ONLINE));
					}
					entpSlipMap.put("afflTrCd",  ConfigUtil.getString("PALTON_ENTP_CODE"));
		    
					paramSlip = entpSlipMapping(entpSlipMap);
					list.add(paramSlip);
					
					log.info("04.반품지/출고지 수정 API 호출");
					map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, list);				
						
					if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("returnCode"))){
						// 출고지 INSERT
						log.info("05-1.반품지/출고지 등록 API 수정 성공 : ");
						paEntpSlip.setPaCode(entpSlipMap.get("PA_CODE").toString());
						paEntpSlip.setEntpCode(entpSlipMap.get("ENTP_CODE").toString());
						paEntpSlip.setPaAddrSeq(entpSlipMap.get("PA_ADDR_SEQ").toString());
						paEntpSlip.setTransTargetYn("0");
						paEntpSlip.setModifyId(Constants.PA_LTON_PROC_ID);
						paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								
						rtnMsg = paLtonCommonService.updatePaLtonEntpSlipTx(paEntpSlip);
						
						log.info("04-2.반품지/출고지 수정 동기화 저장 : "+rtnMsg);
						if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
							paramMap.put("code","404");
							paramMap.put("message",rtnMsg);
						} else {
							paramMap.put("code","200");
							paramMap.put("message","OK");
							procCount++;
						}
					} else {
						paramMap.put("code","404");
						paramMap.put("message",map.get("message"));
					}
				}
			}
		}catch (Exception e) {
			if(duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",e.getMessage().length() > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				if(paramMap.getString("code").equals("200") && (targetCount != procCount)){
					paramMap.put("code","500");
					paramMap.put("message", "대상건수:" + targetCount + ", 성공건수:" + procCount);
				}
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("05.저장 완료 API END");
			log.info("===== 반품지/출고지 수정 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}
	
	
	public Map<String, Object> entpSlipMapping(Map<String, Object> entpSlipMap) {
		Map<String, Object> Map = new HashMap<String, Object>();
		
		String addrNm  = entpSlipMap.get("paCode").toString() + "-" + entpSlipMap.get("ENTP_CODE").toString() + "-" + entpSlipMap.get("ENTP_MAN_SEQ").toString();
		
		if(!"".equals(entpSlipMap.get("PA_ADDR_SEQ")) && entpSlipMap.get("PA_ADDR_SEQ") != null) {
			Map.put("dvpNo", entpSlipMap.get("PA_ADDR_SEQ"));
			Map.put("useYn", 'Y');
		}
		
		if("20".equals(entpSlipMap.get("ENTP_MAN_GB"))) {
			Map.put("dvpTypCd", "01");
		}else {
			Map.put("dvpTypCd", "02");
		}
		Map.put("dvpNm", addrNm);
		Map.put("zipNo", entpSlipMap.get("POST_NO"));
		Map.put("zipAddr", entpSlipMap.get("POST_ADDR"));
		Map.put("dtlAddr", entpSlipMap.get("ADDR"));
		Map.put("stnmZipNo", entpSlipMap.get("STD_ROAD_POST"));
		Map.put("stnmZipAddr", entpSlipMap.get("STD_POST_ADDR1"));
		Map.put("stnmDtlAddr", entpSlipMap.get("STD_POST_ADDR2"));
		Map.put("rpbtrNm", entpSlipMap.get("ENTP_MAN_NAME"));
		Map.put("mphnNo", entpSlipMap.get("ENTP_MAN_HP"));
		Map.put("telNo", entpSlipMap.get("ENTP_MAN_HP"));
		Map.put("afflTrCd", entpSlipMap.get("afflTrCd"));	
		Map.put("afflLrtrCd", entpSlipMap.get("afflLrtrCd"));
		
		return Map;
	}
	


	/**
	 * 배송정책 생성
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "배송정책 등록", notes = "배송정책 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."),
		    @ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/entpcustshipcost-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpCustShipCostInsert(HttpServletRequest request, String searchTermGb) throws Exception {
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String,Object> paramShipCost = new HashMap<String,Object>();	
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String dateTime = systemService.getSysdatetimeToString();
		String apiCode = "IF_PALTONAPI_00_003";
		String duplicateCheck = "";
		log.info("01.API 기본정보 세팅");

		paramMap.put("apiCode", apiCode);
		paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		paramMap.put("modCase", "INSERT");
        
		log.info("02.API정보 조회");
		apiInfo = systemService.selectPaApiInfo(paramMap);
		apiInfo.put("apiInfo", paramMap.getString("apiCode"));
		
		paramMap.put("method", "POST");
		paramMap.put("url", apiInfo.get("API_URL"));
		
	    try {
	    	log.info("03.API 중복실행검사");
	    	if(!"1".equals(searchTermGb)) {
				duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
				if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			log.info("04.배송정책 대상 리스트 조회");
			List<HashMap<String,Object>> entpShipCostMap = paLtonCommonService.selectEntpSlipCost(paramMap);
	    
			if (entpShipCostMap == null || entpShipCostMap.size() <= 0) {
				paramMap.put("code", "404");
		    	paramMap.put("message", getMessage("partner.no_change_data"));
			} else {
		    	for(HashMap<String, Object> entpShipCost : entpShipCostMap){
		    		ArrayList<Map<String, Object> > list = new ArrayList<Map<String,Object>>();
		    		
		    		paramMap.put("paCode", entpShipCost.get("PA_CODE").toString());
		    		if(entpShipCost.get("PA_CODE").toString().equals(Constants.PA_LTON_BROAD_CODE)){
		    			entpShipCost.put("afflLrtrCd", apiInfo.get(Constants.PA_BROAD));
		    		} else {
		    			entpShipCost.put("afflLrtrCd", apiInfo.get(Constants.PA_ONLINE));
		 			}
		    		entpShipCost.put("afflTrCd", ConfigUtil.getString("PALTON_ENTP_CODE"));
			    
		    		paramShipCost = shipCostMapping(entpShipCost, "N");
				    
		    		
		    		list.add(paramShipCost);
		    		
		    		try {
		    			map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, list);
		    			log.info("05.배송정책 생성 API 호출");
					
		    			if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("returnCode"))){
						
		    				ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String,Object>>) map.get("data");
		    				Map<String, Object> result =  (Map<String, Object>)data.get(0);
						
		    				ParamMap custShipCostMap = new ParamMap();
		    				custShipCostMap.put("paCode", entpShipCost.get("PA_CODE").toString());
		    				custShipCostMap.put("entpCode", entpShipCost.get("ENTP_CODE").toString());
		    				custShipCostMap.put("shipCostCode", entpShipCost.get("SHIP_COST_CODE").toString());
		    				custShipCostMap.put("delvCostPlcNo", result.get("dvCstPolNo").toString());
		    				custShipCostMap.put("modifyId", Constants.PA_LTON_PROC_ID);
		    				custShipCostMap.put("modifyDate", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
	    					
		    				rtnMsg = paLtonCommonService.savePaLtonCustShipCostTx(custShipCostMap);
		    				log.info("05-2.배송정책 저장 : "+rtnMsg);
						
		    				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
		    					paramMap.put("code","404");
		    					paramMap.put("message",rtnMsg);
		    					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		    				} else {
		    					paramMap.put("code","200");
		    					paramMap.put("message","OK");
		    				}
		    			} else if ("2000".equals(map.get("returnCode"))){
		    				paramMap.put("code","500");
		    				paramMap.put("message", "ENTP_CODE:" + entpShipCost.get("ENTP_CODE").toString() + " | " +  map.get("message"));
		    				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		    			} else {
		    				paramMap.put("code","404");
		    				paramMap.put("message",map.get("message"));
		    				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		    			}
		    		}catch(Exception e){ // API 오류
		    			String errMsg = e.getMessage();
		    			paramMap.put("code","500");
		    			paramMap.put("message", errMsg);
		    			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		    		}
		    	}
			}
			
			log.info("06.추가 배송정책 대상 리스트 조회");
			List<HashMap<String,Object>> addShipCostMap = paLtonCommonService.selectAddShipCost(paramMap);
	    
			if (addShipCostMap == null || addShipCostMap.size() <= 0) {
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("partner.no_change_data"));
			} else {
				for(HashMap<String, Object> addShipCost : addShipCostMap){
					ArrayList<Map<String, Object> > addShipCostlist = new ArrayList<Map<String,Object>>();
		    	
					paramMap.put("paCode", addShipCost.get("PA_CODE").toString());
					if(addShipCost.get("PA_CODE").toString().equals(Constants.PA_LTON_BROAD_CODE)){
						addShipCost.put("afflLrtrCd", apiInfo.get(Constants.PA_BROAD));
					} else {
						addShipCost.put("afflLrtrCd", apiInfo.get(Constants.PA_ONLINE));
					}
					addShipCost.put("afflTrCd", ConfigUtil.getString("PALTON_ENTP_CODE"));
		    
					paramShipCost = shipCostMapping(addShipCost, "Y");
				    
					addShipCostlist.add(paramShipCost);
					try {
						map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, addShipCostlist);
						log.info("07.배송정책 생성 API 호출");
					
						if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("returnCode"))){
						
							ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String,Object>>) map.get("data");
							Map<String, Object> result =  (Map<String, Object>)data.get(0);
						
							ParamMap custShipCostMap = new ParamMap();
							custShipCostMap.put("paCode", addShipCost.get("PA_CODE").toString());
							custShipCostMap.put("islandCost", addShipCost.get("ISLAND_COST").toString());
							custShipCostMap.put("jejuCost", addShipCost.get("JEJU_COST").toString());
							custShipCostMap.put("delvCostPlcNo", result.get("dvCstPolNo").toString());
	    					
							rtnMsg = paLtonCommonService.savePaLtonAddCustShipCostTx(custShipCostMap);
							log.info("07-2.배송정책 저장 : "+ rtnMsg);
						
							if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
								paramMap.put("code","404");
								paramMap.put("message",rtnMsg);
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							} else {
								paramMap.put("code","200");
								paramMap.put("message","OK");
							}
						} else {
							paramMap.put("code","404");
							paramMap.put("message",map.get("message"));
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						}
					}catch(Exception e){ // API 오류
						String errMsg = e.getMessage();
						paramMap.put("code","500");
						paramMap.put("message", errMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
				}
			}
	    }catch (Exception e) {
	    	if (duplicateCheck.equals("1")) {
	    		paramMap.put("code", "490");
	    	} else {
	    		paramMap.put("code", "500");
	    	}
	    	paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
	    	log.error(paramMap.getString("message"), e);
	    	return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")),HttpStatus.OK);
	    } finally {
	    	try {
	    		paramMap.put("message", paramMap.getString("message"));
	    		paramMap.put("resultCode", paramMap.getString("code"));
	    		paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
	    		systemService.insertApiTrackingTx(request, paramMap);
	    	}catch (Exception e){
	    		log.error("ApiTracking Insert Error : " + e.getMessage());
	    	}
	    	if(!"1".equals(searchTermGb)) {
		    	if (duplicateCheck.equals("0")){
		    		systemService.checkCloseHistoryTx("end", apiCode);
		    	}
	    	}
	    	log.info("===== 배송정책 생성 API END =====");
	    }
	    return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 배송정책 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "배송정책 수정", notes = "배송정책 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."),
		    @ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/entpcustshipcost-update", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpCustShipCostUpdate(HttpServletRequest request) throws Exception {
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String,Object> paramShipCost = new HashMap<String,Object>();	
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String dateTime = systemService.getSysdatetimeToString();
		String apiCode = "IF_PALTONAPI_00_004";
		String duplicateCheck = "";
		log.info("01.API 기본정보 세팅");

		paramMap.put("apiCode", apiCode);
		paramMap.put("broadCode", Constants.PA_LTON_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		paramMap.put("modCase", "MODIFY");
		
		log.info("02.API정보 조회");
		apiInfo = systemService.selectPaApiInfo(paramMap);
		apiInfo.put("apiInfo", paramMap.getString("apiCode"));
		
		paramMap.put("method", "POST");
		paramMap.put("url", apiInfo.get("API_URL"));
		
	    try {
	    	log.info("03.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			
			log.info("04.배송정책수정 대상 리스트 조회");
			List<HashMap<String,Object>> entpShipCostMap = paLtonCommonService.selectEntpSlipCost(paramMap);
	    
			if (entpShipCostMap == null || entpShipCostMap.size() <= 0) {
				paramMap.put("code", "404");
		    	paramMap.put("message", getMessage("partner.no_change_data"));
			} else {
		    	for(HashMap<String, Object> entpShipCost : entpShipCostMap){
		    		ArrayList<Map<String, Object> > list = new ArrayList<Map<String,Object>>();
		    	
		    		paramMap.put("paCode", entpShipCost.get("PA_CODE").toString());
		    		if(entpShipCost.get("PA_CODE").toString().equals(Constants.PA_LTON_BROAD_CODE)){
		    			entpShipCost.put("afflLrtrCd", apiInfo.get(Constants.PA_BROAD));
		    		} else {
		    			entpShipCost.put("afflLrtrCd", apiInfo.get(Constants.PA_ONLINE));
		 			}
		    		entpShipCost.put("afflTrCd", ConfigUtil.getString("PALTON_ENTP_CODE"));
			    
		    		paramShipCost = shipCostMapping(entpShipCost, "N");
				    
		    		list.add(paramShipCost);
		    		try {
		    			map = paLtonConnectUtil.apiGetObjectByLtn(paramMap, list);
		    			log.info("05.배송정책 수정 API 호출");
					
		    			if(Constants.PA_LTON_SUCCESS_CODE.equals(map.get("returnCode"))) {
		    				ParamMap custShipCostMap = new ParamMap();
		    				custShipCostMap.put("paCode", entpShipCost.get("PA_CODE").toString());
		    				custShipCostMap.put("entpCode", entpShipCost.get("ENTP_CODE").toString());
		    				custShipCostMap.put("shipCostCode", entpShipCost.get("SHIP_COST_CODE").toString());
		    				custShipCostMap.put("modifyId", Constants.PA_LTON_PROC_ID);
		    				custShipCostMap.put("modifyDate", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
	    					
		    				rtnMsg = paLtonCommonService.savePaLtonCustShipCostTx(custShipCostMap);
		    				log.info("05-2.배송정책수정  저장 : "+rtnMsg);
						
		    				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
		    					paramMap.put("code","404");
		    					paramMap.put("message",rtnMsg);
		    					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		    				} else {
		    					paramMap.put("code","200");
		    					paramMap.put("message","OK");
		    				}
		    			} else {
		    				paramMap.put("code","404");
		    				paramMap.put("message",map.get("message"));
		    				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		    			}
		    		}catch(Exception e){
		    			String errMsg = e.getMessage();
		    			paramMap.put("code","500");
		    			paramMap.put("message", errMsg);
		    			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		    		}
		    	}
			}
	    }catch (Exception e) {
	    	if (duplicateCheck.equals("1")) {
	    		paramMap.put("code", "490");
	    	} else {
	    		paramMap.put("code", "500");
	    	}
	    	paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
	    	log.error(paramMap.getString("message"), e);
	    	return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")),HttpStatus.OK);
	    } finally {
	    	try {
	    		paramMap.put("message", paramMap.getString("message"));
	    		paramMap.put("resultCode", paramMap.getString("code"));
	    		paramMap.put("siteGb", Constants.PA_LTON_PROC_ID);
	    		systemService.insertApiTrackingTx(request, paramMap);
	    	}catch (Exception e){
	    		log.error("ApiTracking Insert Error : " + e.getMessage());
	    	}
	    	if (duplicateCheck.equals("0")){
	    		systemService.checkCloseHistoryTx("end", apiCode);
	    	}
	    	log.info("===== 배송정책 수정 API END =====");
	    }
	    return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 * 배송비 정책 paramMap 으로 변경
	 * @throws Exception 
	 */
	private Map<String, Object> shipCostMapping(HashMap<String,Object> entpSlipCost, String addYn) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		int jejuCost = Integer.parseInt(entpSlipCost.get("JEJU_COST").toString());
		int islandCost = Integer.parseInt(entpSlipCost.get("ISLAND_COST").toString());

		map.put("aplyStrtDt", DateUtil.getCurrentDateAsString());
		map.put("aplyEndDt", "29991220");
		map.put("ctrtTypCd", "A");
		map.put("dvTypCd", "DRECT");
		map.put("dvProcTypCd", "LO_ENTP");
		map.put("afflTrCd", entpSlipCost.get("afflTrCd"));
		map.put("afflLrtrCd", entpSlipCost.get("afflLrtrCd"));
		map.put("useYn", "Y");
		
		if("N".equals(addYn)) {
			String shipCostCode = StringUtils.substring(entpSlipCost.get("SHIP_COST_CODE").toString(), 0, 2);
			map.put("dvCstTypCd", "DV_CST");
			map.put("cndlFreeStdAmt",Integer.parseInt(entpSlipCost.get("SHIP_COST_BASE_AMT").toString()));
			map.put("dvCst", Integer.parseInt(entpSlipCost.get("ORD_COST").toString()));
			map.put("rcst", Integer.parseInt(entpSlipCost.get("RETURN_COST").toString()));
			
			if("FR".equals(shipCostCode)) {
				map.put("dvCstDvsCd", "B");
			}else if("CN".equals(shipCostCode) || "PL".equals(shipCostCode)){
				map.put("dvCstDvsCd", "C");
			}else if("ID".equals(shipCostCode)) {
				map.put("dvCstDvsCd", "A");
			}
			
			if(!"".equals(entpSlipCost.get("GROUP_CODE")) && entpSlipCost.get("GROUP_CODE") != null) {
				map.put("dvCstPolNo", entpSlipCost.get("GROUP_CODE"));
			}
		} else {
			map.put("dvCstTypCd", "ADTN_DV_CST");
			map.put("inrmAdtnDvCst", islandCost);
			map.put("jejuAdtnDvCst", jejuCost);	
			
			if(jejuCost > 0 || islandCost > 0) {
				map.put("dvCstDvsCd", "A");
			}else {
				map.put("dvCstDvsCd", "B");
			}	
		}
		return map;
	}
	
	
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/settlement-goods", method = {RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<?> settlementGoods(
			HttpServletRequest request,
			@RequestParam(value = "fromDate" , required = false) String fromDate,
			@RequestParam(value = "toDate"	 , required = false) String toDate,
			@RequestParam(value = "delYn"	 , required = false, defaultValue = "N") String delYn) throws Exception {
		
		List<Map<String, Object>> ltonDataList = new ArrayList<Map<String, Object>>();
		HashMap<String, String>	  apiInfo	   = new HashMap<String, String>();
		Map<String, Object>       map 		   = new HashMap<String, Object>();
		ParamMap 				  apiInfoMap   = new ParamMap();
		ParamMap				  apiDataMap   = new ParamMap();
		StringBuffer			  sb		   = new StringBuffer();
		
		String startDate = ComUtil.NVL(fromDate).length() == 8 ? fromDate : DateUtil.addDay( DateUtil.getCurrentDateAsString() , -1, DateUtil.GENERAL_DATE_FORMAT);
		String endDate   = ComUtil.NVL(toDate).length()   == 8 ? toDate   : DateUtil.getCurrentDateAsString();
		String prgId 	 = "IF_PALTONAPI_05_001";
		
		apiInfoMap.put("apiCode", 	 prgId);
		apiInfoMap.put("paCode", 	 Constants.PA_LTON_BROAD_CODE);
		apiInfoMap.put("broadCode",  Constants.PA_LTON_BROAD_CODE);
		apiInfoMap.put("onlineCode", Constants.PA_LTON_ONLINE_CODE);
		
		apiInfo = systemService.selectPaApiInfo(apiInfoMap);
		
		try {
			String duplicateCheck = systemService.checkCloseHistoryTx("start", prgId);
			if ("1".equals(duplicateCheck))	throw processException("msg.batch_process_duplicated", new String[] {prgId});
			
			String[] apiNumber = apiInfo.get("API_URL").split(";");
			
			for(int i = 0; i < apiNumber.length; i++) {
				apiInfoMap = new ParamMap();
				apiInfoMap.put("duplicateCheck", duplicateCheck);
				apiInfoMap.put("method", "POST");
				apiInfoMap.put("url", apiNumber[i]);
				apiInfoMap.put("siteGb", Constants.PA_LTON_PROC_ID);
				apiInfoMap.put("apiCode", prgId);
				
				apiDataMap.put("startDate", startDate);
				apiDataMap.put("endDate", endDate);
				
				map = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
				
				if(!String.valueOf(map.get("returnCode")).equals("SUCCESS")) {
					sb.append(i + " : " + String.valueOf(map.get("message")) + " | ");
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", sb.toString());
					continue;
				}
				
				ltonDataList = (List<Map<String, Object>>)map.get("data");
				
				if(ltonDataList.size() < 1) {
					//통신 성공 했지만 조회 내역 없을 경우
					continue;
				} else {
					for(Map<String, Object> dataMap : ltonDataList) {
						PaLtonSettlement settlementData = new PaLtonSettlement();
						try {
							settlementData = (PaLtonSettlement) PaLtonComUtill.map2VO(dataMap, PaLtonSettlement.class);
							switch(i) {
							case 0 :
								settlementData.setFlag("01");
								break;
							case 1 :
								settlementData.setFlag("02");
								break;
							case 2 :
								settlementData.setFlag("03");
								break;
							}
							settlementData.setDelYn(delYn);
							paLtonCommonService.saveLtonSettlementTx(settlementData, apiDataMap);
						} catch(Exception e) {
							log.error("ERROR - SaveLtonSettlement :::::" + PaLtonComUtill.getErrorMessage(e));
						}
					}
				}
			}
		} catch(Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
}
