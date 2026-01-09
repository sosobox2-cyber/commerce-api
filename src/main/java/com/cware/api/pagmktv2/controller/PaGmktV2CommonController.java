package com.cware.api.pagmktv2.controller;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.common.util.PostUtil;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaEsmGoodsKinds;
import com.cware.netshopping.domain.model.PaGmktOrigin;
import com.cware.netshopping.domain.model.PaGmktPolicy;
import com.cware.netshopping.domain.model.PaGmktSettlement;
import com.cware.netshopping.domain.model.PaGmktShipCostM;
import com.cware.netshopping.domain.model.PaMaker;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaSiteGoodsKinds;
import com.cware.netshopping.pagmkt.common.service.PaGmktCommonService;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;
import com.cware.netshopping.pagmkt.util.rest.PaGmktCommonRest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pagmktv2/common", description="공통")
@Controller("com.cware.api.pagmktv2.PaGmktV2CommonController")
@RequestMapping(value="/pagmktv2/common")
public class PaGmktV2CommonController extends AbstractController {


	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "com.cware.netshopping.common.util.PostUtil")
	private PostUtil postUtil;
	
	@Resource(name = "pagmkt.common.paGmktCommonService")
	private PaGmktCommonService paGmktCommonService;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	private PaGmktCommonUtil CommonUtil;
		
	/**
	 * ESM 카테고리 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "ESM 카테고리 조회", notes = "ESM 카테고리 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/get-categories-esm", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getCategoriesEsm(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PAGMKTAPI_V2_00_003";            
		String duplicateCheck = "";
		List<PaEsmGoodsKinds> paEsmGoodsKindsList = new ArrayList<PaEsmGoodsKinds>();
		PaEsmGoodsKinds paEsmGoodsKinds = null;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("paCode", "21");
		
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			//= Step 1) select 테이블 후 urlParameter 셋팅
			paramMap.put("urlParameter", "0");
			
			//= Step 2) 통신
			String response = restUtil.getConnection(null,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			
			//= Step 3) 통신후 resMap 파싱 & setter
			List<Object> resList = (List<Object>)resMap.get("sdCategoryTree");
			
			for(Object esmCategory : resList) {
				HashMap<String,Object> result = new HashMap<>();
				paEsmGoodsKinds = new PaEsmGoodsKinds();
				
				result = (HashMap<String, Object>) esmCategory;
				
				paEsmGoodsKinds.setLgroup(result.get("SDCategoryCode").toString().substring(0, 4));
				paEsmGoodsKinds.setMgroup(result.get("SDCategoryCode").toString().substring(4, 8));
				paEsmGoodsKinds.setSgroup(result.get("SDCategoryCode").toString().substring(8, 12));
				paEsmGoodsKinds.setDgroup(result.get("SDCategoryCode").toString().substring(12, 16));
				paEsmGoodsKinds.setNgroup(result.get("SDCategoryCode").toString().substring(16, 20));
				
				if(!paEsmGoodsKinds.getNgroup().equals("0000")) {
					paEsmGoodsKinds.setNgroupName(result.get("SDCategoryName").toString());
				}
				
				if(!paEsmGoodsKinds.getDgroup().equals("0000")) {
					paEsmGoodsKinds.setDgroupName(result.get("SDCategoryName").toString());
				}
				
				if(!paEsmGoodsKinds.getSgroup().equals("0000")) {
					paEsmGoodsKinds.setSgroupName(result.get("SDCategoryName").toString());
				}
				
				if(!paEsmGoodsKinds.getMgroup().equals("0000")) {
					paEsmGoodsKinds.setMgroupName(result.get("SDCategoryName").toString());
				}
				
				if(!paEsmGoodsKinds.getLgroup().equals("0000")) {
					paEsmGoodsKinds.setLgroupName(result.get("SDCategoryName").toString());
				}
				
				paEsmGoodsKinds.setLmsdnCode(result.get("SDCategoryCode").toString());
				paEsmGoodsKinds.setDoNotLeafYn(result.get("IsLeafCategory").toString().equals("true") ? "1" : "0");
				paEsmGoodsKinds.setInsertId("PAG");
				paEsmGoodsKinds.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate").toString(), "yyyy/MM/dd HH:mm:ss"));
				
				paEsmGoodsKindsList.add(paEsmGoodsKinds);
			}
			
			//= Step 4) table insert
			paGmktCommonService.savePaGmktGoodsKindsEsmTx(paEsmGoodsKindsList);
	    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== ESM 카테고리 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * SITE 카테고리 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "SITE 카테고리 조회", notes = "SITE 카테고리 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/get-categories-site", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getCategoriesSite(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PAGMKTAPI_V2_00_001_L";            
		String duplicateCheck = "";
		List<PaSiteGoodsKinds> paSiteGoodsKindsList = new ArrayList<PaSiteGoodsKinds>();
		PaSiteGoodsKinds paSiteGoodsKinds = null;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("paCode", "21");
		
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			//= Step 1) select 테이블 후 urlParameter 셋팅
			paramMap.put("urlParameter", "");
			
			//= Step 2) 통신
			String response = restUtil.getConnection(null,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			
			apiCode = "IF_PAGMKTAPI_V2_00_001";
			paramMap.put("apiCode", apiCode);
			
			List<HashMap<String, Object>> resList = (List<HashMap<String, Object>>)resMap.get("subCats");
			
			for(HashMap<String, Object> siteCategoryL : resList) {
				paramMap.put("urlParameter", siteCategoryL.get("catCode").toString());
				
				response = restUtil.getConnection(null,  paramMap);
				resMap = ComUtil.splitJson(response);
				
				resList = (List<HashMap<String, Object>>)resMap.get("subCats");
				
				String lgroup = resMap.get("catCode").toString();
				String lgroupName = resMap.get("catName").toString();
				
				for(HashMap<String, Object> siteCategoryM : resList) {
					paramMap.put("urlParameter", siteCategoryM.get("catCode").toString());
					
					response = restUtil.getConnection(null,  paramMap);
					resMap = ComUtil.splitJson(response);
					
					resList = (List<HashMap<String, Object>>)resMap.get("subCats");
					
					String mgroup = resMap.get("catCode").toString();
					String mgroupName = resMap.get("catName").toString();
					
					for(HashMap<String, Object> siteCategoryS : resList) {
						paSiteGoodsKinds = new PaSiteGoodsKinds();
						
						paSiteGoodsKinds.setLgroup(lgroup);
						paSiteGoodsKinds.setLgroupName(lgroupName);
						paSiteGoodsKinds.setMgroup(mgroup);
						paSiteGoodsKinds.setMgroupName(mgroupName);
						paSiteGoodsKinds.setSgroup(siteCategoryS.get("catCode").toString());
						paSiteGoodsKinds.setSgroupName(siteCategoryS.get("catName").toString());
						paSiteGoodsKinds.setSiteGb("01");//g마켓
						paSiteGoodsKinds.setLmsdCode(lgroup + mgroup + siteCategoryS.get("catCode").toString());
						paSiteGoodsKinds.setDoNotLeafYn(siteCategoryS.get("isLeaf").toString().equals("true") ? "1" : "0");
						paSiteGoodsKinds.setInsertId("PAG");
						paSiteGoodsKinds.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate").toString(), "yyyy/MM/dd HH:mm:ss"));
						
						if (siteCategoryS.get("isLeaf").toString().equals("false")) {
							paramMap.put("urlParameter", siteCategoryS.get("catCode").toString());
							
							response = restUtil.getConnection(null,  paramMap);
							resMap = ComUtil.splitJson(response);
							
							resList = (List<HashMap<String, Object>>)resMap.get("subCats");
							String sgroup = resMap.get("catCode").toString();
							String sgroupName = resMap.get("catName").toString();
							
							//옥션은 DGROUP까지 존재
							for(HashMap<String, Object> siteCategoryD : resList) {
								paSiteGoodsKinds = new PaSiteGoodsKinds();
								
								paSiteGoodsKinds.setLgroup(lgroup);
								paSiteGoodsKinds.setLgroupName(lgroupName);
								paSiteGoodsKinds.setMgroup(mgroup);
								paSiteGoodsKinds.setMgroupName(mgroupName);
								paSiteGoodsKinds.setSgroup(sgroup);
								paSiteGoodsKinds.setSgroupName(sgroupName);
								paSiteGoodsKinds.setDgroup(siteCategoryD.get("catCode").toString());
								paSiteGoodsKinds.setDgroupName(siteCategoryD.get("catName").toString());
								paSiteGoodsKinds.setSiteGb("01");
								paSiteGoodsKinds.setLmsdCode(lgroup + mgroup + sgroup + siteCategoryD.get("catCode").toString());
								paSiteGoodsKinds.setDoNotLeafYn(siteCategoryD.get("isLeaf").toString().equals("true") ? "1" : "0");
								paSiteGoodsKinds.setInsertId("PAG");
								paSiteGoodsKinds.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate").toString(), "yyyy/MM/dd HH:mm:ss"));
								
								paSiteGoodsKindsList.add(paSiteGoodsKinds);
							}
						} else {
							paSiteGoodsKindsList.add(paSiteGoodsKinds);
						}
					}
				}
			}
			//Step 4) table insert
		    paGmktCommonService.savePaGmktGoodsKindsSiteTx(paSiteGoodsKindsList);
	    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== SITE 카테고리 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * ESM-Site 카테고리 매칭 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "ESM-Site 카테고리 매칭 조회", notes = "ESM-Site 카테고리 매칭 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/get-categories-matching", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getCategoriesMatching(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PAGMKTAPI_V2_00_004";            
		String duplicateCheck = "";
		PaSiteGoodsKinds paSiteGoodsKinds = null;
		List<PaSiteGoodsKinds> paSiteGoodsKindsList = new ArrayList<PaSiteGoodsKinds>();
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("paCode", "21");
		
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			List<String> lmsdnList = paGmktCommonService.selectLmsdnCodeList();
			
			for(String lmsdnCode : lmsdnList) {
				//= Step 1) select 테이블 후 urlParameter 셋팅
				paramMap.put("urlParameter", lmsdnCode);
				
				//= Step 2) 통신
				String response = restUtil.getConnection(null,  paramMap);
				Map<String,Object> resMap = ComUtil.splitJson(response);
				
				//= Step 3) 통신후 resMap 파싱 & setter
				HashMap<Object, Object> resMap2 = (HashMap<Object, Object>) resMap.get("MatchedCategory");
				List<HashMap<String, Object>> gmktList = (List<HashMap<String, Object>>)resMap2.get("Gmkt");
				List<HashMap<String, Object>> iacList = (List<HashMap<String, Object>>)resMap2.get("Iac");
				
				for(HashMap<String, Object> matchingCategory : gmktList) {
					paSiteGoodsKinds = new PaSiteGoodsKinds();
					
					paSiteGoodsKinds.setSiteGb("01");
					paSiteGoodsKinds.setLmsdnCode(lmsdnCode);
					paSiteGoodsKinds.setLmsdCode(matchingCategory.get("CategoryCode").toString());
					paSiteGoodsKinds.setDoNotLeafYn(matchingCategory.get("IsLeaf").equals("true") ? "1" : "0");
					paSiteGoodsKinds.setInsertId("PAG");
					paSiteGoodsKinds.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate").toString(), "yyyy/MM/dd HH:mm:ss"));
					
					paSiteGoodsKindsList.add(paSiteGoodsKinds);
				}
				
				for(HashMap<String, Object> matchingCategory : iacList) {
					paSiteGoodsKinds = new PaSiteGoodsKinds();
					
					paSiteGoodsKinds.setSiteGb("02");
					paSiteGoodsKinds.setLmsdnCode(lmsdnCode);
					paSiteGoodsKinds.setLmsdCode(matchingCategory.get("CategoryCode").toString());
					paSiteGoodsKinds.setDoNotLeafYn(matchingCategory.get("IsLeaf").toString().equals("true") ? "1" : "0");
					paSiteGoodsKinds.setInsertId("PAG");
					paSiteGoodsKinds.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate").toString(), "yyyy/MM/dd HH:mm:ss"));
					
					paSiteGoodsKindsList.add(paSiteGoodsKinds);
				}
			}
			
			//= Step 4) table insert
			paGmktCommonService.savePaGmktGoodsKindsMatchingTx(paSiteGoodsKindsList);
	    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== SITE 카테고리 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 브랜드 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "브랜드 조회", notes = "브랜드 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/brand-list-gmkt", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> brandListGmkt(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PAGMKTAPI_V2_00_017";
		String duplicateCheck = "";
		List<PaBrand> paBrandList = new ArrayList<PaBrand>();
        PaBrand paBrand = null;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");

		try{
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			//= Step 1) 브랜드 정보 엑셀파일 조회
			
			String fileName ="Product_Gmkt_Brand.xlsx";
			String fileDirPath = new HttpServletRequestWrapper(request).getRealPath("/uploadfile");
			File file = new File(fileDirPath+"/"+fileName);
			
			if (!file.exists()) {
				paramMap.put("ERROR_MESSAGE", getMessage("msg.upload_failure"));
			}
			
			//= Step 2) setter
			
			XSSFWorkbook work = new XSSFWorkbook(new FileInputStream(file));
			
			XSSFRow row = null;
			XSSFSheet sheet = work.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			if (rows > 0) {
				for (int r = 1; r < rows; r++) {
					row = sheet.getRow(r);
					
					if (row == null) {
						paramMap.put("code","404");
						paramMap.put("message",getMessage("errors.no.select"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else if (row.getCell(4).getStringCellValue().equals("")) {
						continue;
					} else {
						paBrand = new PaBrand();
						paBrand.setPaGroupCode("02");
						paBrand.setPaBrandNo(row.getCell(4).getStringCellValue());
						paBrand.setPaBrandName(row.getCell(6).getStringCellValue());
						paBrand.setInsertId("PAG");
						paBrand.setModifyId("PAG");
						paBrand.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
						paBrand.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));	
						
						paBrandList.add(paBrand);
					}
				}
				
				for (int r = 1; r < rows; r++) {
					row = sheet.getRow(r);
					
					if (row == null) {
						paramMap.put("code","404");
						paramMap.put("message",getMessage("errors.no.select"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else if (row.getCell(4).getStringCellValue().equals("")) {
						continue;
					} else if (row.getCell(8) != null){
						paBrand = new PaBrand();
						paBrand.setPaGroupCode("02");
						paBrand.setPaBrandNo(row.getCell(8).getStringCellValue());
						paBrand.setPaBrandName(row.getCell(6).getStringCellValue() + "_" + row.getCell(10).getStringCellValue());
						paBrand.setInsertId("PAG");
						paBrand.setModifyId("PAG");
						paBrand.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
						paBrand.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));	
						
						paBrandList.add(paBrand);
					}
				}
			}
	    	//= Step 3) table insert
	    	paGmktCommonService.savePaGmktBrandListTx(paBrandList);
	    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 브랜드 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 제조사 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "제조사 조회", notes = "제조사 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/maker-list-gmkt", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> makerListGmkt(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PAGMKTAPI_V2_00_018";
		String duplicateCheck = "";
		List<PaMaker> paMakerList = new ArrayList<PaMaker>();
        PaMaker paMaker = null;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");

		try{
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			//= Step 1) 제조사 정보 엑셀파일 조회
			
			String fileName ="Product_Gmkt_Brand.xlsx";
			String fileDirPath = new HttpServletRequestWrapper(request).getRealPath("/uploadfile");
			File file = new File(fileDirPath+"/"+fileName);
			
			if (!file.exists()) {
				paramMap.put("ERROR_MESSAGE", getMessage("msg.upload_failure"));
			}
			
			//= Step 2) setter
			
			XSSFWorkbook work = new XSSFWorkbook(new FileInputStream(file));
			
			XSSFRow row = null;
			XSSFSheet sheet = work.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			if (rows > 0) {
				for (int r = 1; r < rows; r++) {
					row = sheet.getRow(r);
					
					if (row == null) {
						paramMap.put("code","404");
						paramMap.put("message",getMessage("errors.no.select"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else if (row.getCell(0).getStringCellValue().equals("")) {
						continue;
					} else {
						paMaker = new PaMaker();
						paMaker.setPaGroupCode("02");
						paMaker.setPaMakerNo(row.getCell(0).getStringCellValue());
						paMaker.setPaMakerName(row.getCell(2).getStringCellValue());
						paMaker.setInsertId("PAG");
						paMaker.setModifyId("PAG");
						paMaker.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
						paMaker.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));	
						
						paMakerList.add(paMaker);
					}
				}
			}
	    	//= Step 3) table insert
	    	paGmktCommonService.savePaGmktMakerListTx(paMakerList);
	    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 제조사 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 원산지 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "원산지 조회", notes = "원산지 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/origin-list-gmkt", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> originListGmkt(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PAGMKTAPI_V2_00_021";
		String duplicateCheck = "";
		List<PaGmktOrigin> paGmktOriginList = new ArrayList<PaGmktOrigin>();
        PaGmktOrigin paGmktOrigin = null;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");

		try{
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			//= Step 1) 원산지 정보 엑셀파일 조회
			
			String fileName ="Product_Gmkt_Origin.xlsx";
			String fileDirPath = new HttpServletRequestWrapper(request).getRealPath("/uploadfile");
			File file = new File(fileDirPath+"/"+fileName);
			
			if (!file.exists()) {
				paramMap.put("ERROR_MESSAGE", getMessage("msg.upload_failure"));
			}
			
			//= Step 2) setter
			
			XSSFWorkbook work = new XSSFWorkbook(new FileInputStream(file));
			
			XSSFRow row = null;
			XSSFSheet sheet = work.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			if (rows > 0) {
				for (int r = 1; r < rows; r++) {
					row = sheet.getRow(r);
					
					if (row == null) {
						paramMap.put("code","404");
						paramMap.put("message",getMessage("errors.no.select"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else {
						if (Integer.toString((int)row.getCell(3).getNumericCellValue()).equals("0")) {
							continue;
						}
						paGmktOrigin = new PaGmktOrigin();
						paGmktOrigin.setOrgnTypCd(Integer.toString((int)row.getCell(0).getNumericCellValue()));//01 한국 02외국  
						paGmktOrigin.setOrgnTypDtlsCd(Integer.toString((int)row.getCell(3).getNumericCellValue()));
						paGmktOrigin.setAreaName(row.getCell(1).getStringCellValue());
						paGmktOrigin.setDetailAreaName(row.getCell(2).getStringCellValue());
						paGmktOrigin.setInsertId("PAG");
						paGmktOrigin.setModifyId("PAG");
						paGmktOrigin.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
						paGmktOrigin.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));	
						
						paGmktOriginList.add(paGmktOrigin);
					}
				}
			}
	    	//= Step 3) table insert
	    	paGmktCommonService.savePaGmktOriginListTx(paGmktOriginList);
	    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 원산지 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 정보고시 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "정보고시 조회", notes = "정보고시 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/offercode-list-gmkt", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> offerCodeListGmkt(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PAGMKTAPI_V2_00_021";
		String duplicateCheck = "";
		List<PaOfferCode> paGmktOfferCodeList = new ArrayList<PaOfferCode>();
		PaOfferCode paOfferCode = null;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");

		try{
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			//= Step 1) 정보고시 정보 엑셀파일 조회
			
			String fileName ="Product_Gmkt_OfferCode.xlsx";
			String fileDirPath = new HttpServletRequestWrapper(request).getRealPath("/uploadfile");
			File file = new File(fileDirPath+"/"+fileName);
			
			if (!file.exists()) {
				paramMap.put("ERROR_MESSAGE", getMessage("msg.upload_failure"));
			}
			
			//= Step 2) setter
			
			XSSFWorkbook work = new XSSFWorkbook(new FileInputStream(file));
			
			XSSFRow row = null;
			XSSFSheet sheet = work.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			if (rows > 0) {
				for (int r = 1; r < rows; r++) {
					row = sheet.getRow(r);
					
					if (row == null) {
						paramMap.put("code","404");
						paramMap.put("message",getMessage("errors.no.select"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else {
						paOfferCode = new PaOfferCode();
						paOfferCode.setPaGroupCode("02");
						paOfferCode.setPaOfferType(Integer.toString((int)row.getCell(1).getNumericCellValue()));
						paOfferCode.setPaOfferCode(row.getCell(2).getStringCellValue());
						paOfferCode.setPaOfferTypeName(row.getCell(3).getStringCellValue());
						paOfferCode.setPaOfferCodeName(row.getCell(4).getStringCellValue());
						paOfferCode.setRemark(row.getCell(7).getStringCellValue());
						paOfferCode.setInsertId("PAG");
						paOfferCode.setModifyId("PAG");
						paOfferCode.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
						paOfferCode.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));	
						
						paGmktOfferCodeList.add(paOfferCode);
					}
				}
			}
	    	//= Step 3) table insert
	    	paGmktCommonService.savePaGmktOfferCodeListTx(paGmktOfferCodeList);
	    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 정보고시 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	

	/**
	 * 판매자주소 등록
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "판매자주소 등록", notes = "판매자주소 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-seller-addr", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postSellerAddr(HttpServletRequest request,
			@ApiParam(name = "entpCode", value = "업체코드", defaultValue = "") @RequestParam(value="entpCode"		, required=true) String entpCode,			
			@ApiParam(name = "entpManSeq", value = "업체순번", defaultValue = "") @RequestParam(value="entpManSeq"	, required=true) String entpManSeq,
			@ApiParam(name = "paAddrGb", value = "출고 30,회수 20", defaultValue = "") @RequestParam(value="paAddrGb"		, required=true) String paAddrGb,
			@ApiParam(name = "searchTermGb", value = "중복실행검사여부('1':진행)", defaultValue = "") @RequestParam(value="searchTermGb"	, required=false) String searchTermGb
			) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCommonRest();
		String apiCode = "IF_PAGMKTAPI_V2_00_005";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("paCode","21");//EBAY 판매자주소 등록은 계정하나로 통합되어있다.
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			if(!"1".equals(searchTermGb)){
				duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			}
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			PaEntpSlip paEntpSlip = new PaEntpSlip();//판매자주소(출고)수정
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb(paAddrGb);//출고 30,회수 20
			paEntpSlip.setPaCode("21");
			List<HashMap<String,String>> mapList = paGmktCommonService.selectEntpShipInsertList(paEntpSlip);
			for(Map<String,String> requestMap : mapList){
				String realYn = "DEV";
				//판매자주소 = 개발환경_업체코드_업체명_담당자순번
				//운영 REAL_100001_커머스웨어_001
				//개발 DEV_100001_커머스웨어_001
				//모두 같은 계정에 생성되서 구분처리, config PA_REAL_SERVER_YN
				if(systemService.getConfig("PA_REAL_SERVER_YN").getVal().equals("Y")){
					realYn ="REAL";
				}
				paramMap.put("realYn", realYn);
				paramMap.put("map", requestMap);
				
				//= Step 2) 통신
				String response = restUtil.getConnection(rest,  paramMap);
				Map<String,Object> resMap = ComUtil.splitJson(response);
				
				//= Step 3) 통신후 resMap 파싱 & setter
				String resAddrNo = resMap.get("addrNo").toString();
				
				paEntpSlip = new PaEntpSlip();//테이블 insert용도
				paEntpSlip.setEntpCode(requestMap.get("ENTP_CODE"));
				paEntpSlip.setEntpManSeq(requestMap.get("ENTP_MAN_SEQ"));
				paEntpSlip.setPaCode("21");
				paEntpSlip.setPaAddrSeq(resAddrNo);
				paEntpSlip.setPaAddrGb(requestMap.get("ENTP_MAN_GB"));
		    	paEntpSlip.setTransTargetYn(Constants.PA_GMKT_NUM_N);
		    	paEntpSlip.setInsertId("PAG");
		    	paEntpSlip.setModifyId("PAG");
		    	paEntpSlip.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
		    	paEntpSlip.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
		    	
		    	//= Step 4) table insert
		    	paGmktCommonService.savePaGmktEntpSlipTx(paEntpSlip);
		    	
			}
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
			throw processException("msg.cannot_save",new String[] {paramMap.getString("message")});
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(!"1".equals(searchTermGb)){
				if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));	
			}
			log.info("===== 판매자주소 등록 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 판매자주소 수정
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "판매자주소 수정", notes = "판매자주소 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-seller-addr", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putSellerAddr(HttpServletRequest request,
			@ApiParam(name = "entpCode", value = "업체코드", defaultValue = "") @RequestParam(value="entpCode"		, required=true) String entpCode,			
			@ApiParam(name = "entpManSeq", value = "업체순번", defaultValue = "") @RequestParam(value="entpManSeq"	, required=true) String entpManSeq,
			@ApiParam(name = "paAddrGb", value = "출고 30,회수 20", defaultValue = "") @RequestParam(value="paAddrGb"		, required=true) String paAddrGb,
			@ApiParam(name = "searchTermGb", value = "중복실행검사여부('1':진행)", defaultValue = "") @RequestParam(value="searchTermGb"	, required=false) String searchTermGb
			) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCommonRest();
		String apiCode = "IF_PAGMKTAPI_V2_00_006";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("paCode", "21");//EBAY 판매자주소 등록은 계정하나로 통합되어있다.
		
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			if(!"1".equals(searchTermGb)){
				duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			}
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			PaEntpSlip paEntpSlip = new PaEntpSlip();//판매자주소(출고)수정
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb(paAddrGb);//출고 30,회수 20
			paEntpSlip.setPaCode("21");
			
			List<HashMap<String,String>> mapList = paGmktCommonService.selectEntpShipModifyList(paEntpSlip);
			for(Map<String,String> requestMap : mapList){
				String realYn = "DEV";
				//판매자주소 = 개발환경_업체코드_업체명_담당자순번
				//운영 REAL_100001_커머스웨어_001
				//개발 DEV_100001_커머스웨어_001
				//모두 같은 계정에 생성되서 구분처리, config PA_REAL_SERVER_YN
				if(systemService.getConfig("PA_REAL_SERVER_YN").getVal().equals("Y")){
					realYn ="REAL";
				}
				paramMap.put("realYn", realYn);
				paramMap.put("map", requestMap);
				paramMap.put("urlParameter",requestMap.get("PA_ADDR_SEQ"));
			
				//= Step 2) 통신
				String response = restUtil.getConnection(rest,  paramMap);
				Map<String,Object> resMap = ComUtil.splitJson(response);
				
				//= Step 3) 통신후 resMap 파싱 & setter
				String resAddrNo = resMap.get("addrNo").toString();
				
				paEntpSlip = new PaEntpSlip();//테이블 insert용도
				paEntpSlip.setEntpCode(requestMap.get("ENTP_CODE"));
				paEntpSlip.setEntpManSeq(requestMap.get("ENTP_MAN_SEQ"));
				paEntpSlip.setPaCode("21");
				paEntpSlip.setPaAddrSeq(resAddrNo);
				paEntpSlip.setPaAddrGb(requestMap.get("ENTP_MAN_GB"));
		    	paEntpSlip.setTransTargetYn(Constants.PA_GMKT_NUM_N);
		    	paEntpSlip.setInsertId("PAG");
		    	paEntpSlip.setModifyId("PAG");
		    	paEntpSlip.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
		    	paEntpSlip.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
		    	
		    	//= Step 4) table insert
		    	paGmktCommonService.savePaGmktEntpSlipUpdateTx(paEntpSlip);
			}
	    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
			throw processException("msg.cannot_save",new String[] {paramMap.getString("message")});
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(!"1".equals(searchTermGb)){
				if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));	
			}
			
			log.info("===== 판매자주소 수정 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 * 출하지 등록
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "출하지 등록", notes = "출하지 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-shipping-places", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postShippingPlaces(HttpServletRequest request,
			@ApiParam(name = "entpCode", value = "업체코드", defaultValue = "") @RequestParam(value="entpCode"		, required=true) String entpCode,			
			@ApiParam(name = "entpManSeq", value = "업체순번", defaultValue = "") @RequestParam(value="entpManSeq"	, required=true) String entpManSeq,
			@ApiParam(name = "searchTermGb", value = "중복실행검사여부('1':진행)", defaultValue = "") @RequestParam(value="searchTermGb"	, required=false) String searchTermGb
			) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCommonRest();
		String apiCode = "IF_PAGMKTAPI_V2_00_008";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("paCode", "21");//EBAY 출하지 등록은 계정하나로 통합되어있다.
		
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			}
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			PaEntpSlip paEntpSlip = new PaEntpSlip();//출하지
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb("30");//출고 30,회수 20 - 출하지만 하기때문에 30고정
			paEntpSlip.setPaCode("21");
			
			List<HashMap<String,String>> mapList = paGmktCommonService.selectEntpShipCostInsertList(paEntpSlip);
			for(Map<String,String> requestMap : mapList){
				String realYn = "DEV";
				//출하지주소 = 개발환경_업체코드_담당자순번
				//운영 REAL_100001_001
				//개발 DEV_100001_001
				//모두 같은 계정에 생성되서 구분처리, config PA_REAL_SERVER_YN
				if(systemService.getConfig("PA_REAL_SERVER_YN").getVal().equals("Y")){
					realYn ="REAL";
				}
				paramMap.put("realYn", realYn);
				paramMap.put("map", requestMap);
				paramMap.put("USE_NEW_GMKT_SHIPCOST_YN", paGmktCommonService.selectTConfigVal("USE_NEW_GMKT_SHIPCOST_YN")); //TODO 안정화 이후 제거 필요
				//= Step 2) 통신
				String response = restUtil.getConnection(rest,  paramMap);
				PaGmktShipCostM paGmktShipCostM = new PaGmktShipCostM();
				
				if(response.indexOf("동일한 출하지")>0 ) {
					paGmktShipCostM.setGmktShipNo(requestMap.get("GMKT_SHIP_NO"));
					paGmktShipCostM.setEntpCode(requestMap.get("ENTP_CODE"));
					paGmktShipCostM.setEntpManSeq(requestMap.get("ENTP_MAN_SEQ"));
				}else {
					Map<String,Object> resMap = ComUtil.splitJson(response);				
					System.out.println(resMap);
					//= Step 3) 통신후 resMap 파싱 & setter
					paGmktShipCostM.setGmktShipNo(resMap.get("placeNo").toString());
					//paGmktShipCostM.setIslandCost(Double.parseDouble(resMap.get("backwoodsAdditionalShippingFee").toString()));
					//paGmktShipCostM.setJejuCost(Double.parseDouble(resMap.get("jejuAdditionalShippingFee").toString()));
					paGmktShipCostM.setEntpCode(entpCode);
					paGmktShipCostM.setEntpManSeq(entpManSeq);					
				}
				
		    	//= Step 4) table insert
		    	paGmktCommonService.savePaGmktShipCostMInsertTx(paGmktShipCostM);

			}
	    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se,paramMap);
			log.error(paramMap.getString("message"), se);
			//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
			throw processException("msg.cannot_save",new String[] {paramMap.getString("message")});
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}

			if(!searchTermGb.equals("1")){
				if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));	
			}
			log.info("===== 출하지 등록 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 출하지 수정
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "출하지 수정", notes = "출하지 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-shipping-places", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putShippingPlaces(HttpServletRequest request, ParamMap map,
			@ApiParam(name = "entpCode", value = "업체코드", defaultValue = "") @RequestParam(value="entpCode"		, required=true) String entpCode,			
			@ApiParam(name = "entpManSeq", value = "업체순번", defaultValue = "") @RequestParam(value="entpManSeq"	, required=true) String entpManSeq,
			@ApiParam(name = "searchTermGb", value = "중복실행검사여부('1':진행)", defaultValue = "") @RequestParam(value="searchTermGb"	, required=false) String searchTermGb
			) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCommonRest();
		String apiCode = "IF_PAGMKTAPI_V2_00_009";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("paCode", "21");//EBAY 출하지 등록은 계정하나로 통합되어있다.
		
		PaEntpSlip paEntpSlip = new PaEntpSlip();//출하지
		
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			}
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb("30");//출고 30,회수 20 - 출하지만 하기때문에 30고정
			paEntpSlip.setPaCode("21");
			
			List<HashMap<String,String>> mapList = paGmktCommonService.selectEntpShipCostModifyList(paEntpSlip);
			for(Map<String,String> requestMap : mapList){
				String realYn = "DEV";
				//출하지주소 = 개발환경_업체코드_담당자순번
				//운영 REAL_100001_001
				//개발 DEV_100001_001
				//모두 같은 계정에 생성되서 구분처리, config PA_REAL_SERVER_YN
				if(systemService.getConfig("PA_REAL_SERVER_YN").getVal().equals("Y")){
					realYn ="REAL";
				}
				paramMap.put("realYn", realYn);
				paramMap.put("map", requestMap);
				paramMap.put("urlParameter",requestMap.get("GMKT_SHIP_NO"));
				map.put("gmktShipNo",requestMap.get("GMKT_SHIP_NO"));
				paramMap.put("USE_NEW_GMKT_SHIPCOST_YN", paGmktCommonService.selectTConfigVal("USE_NEW_GMKT_SHIPCOST_YN")); //TODO 안정화 이후 제거 필요
				
				//= Step 2) 통신
				String response = restUtil.getConnection(rest,  paramMap);				
				PaGmktShipCostM paGmktShipCostM = new PaGmktShipCostM();
				
				if(response.indexOf("동일한 출하지")>0 ) {
					paGmktShipCostM.setGmktShipNo(requestMap.get("GMKT_SHIP_NO"));
					paGmktShipCostM.setEntpCode(requestMap.get("ENTP_CODE"));
					paGmktShipCostM.setEntpManSeq(requestMap.get("ENTP_MAN_SEQ"));
				}else {
					Map<String,Object> resMap = ComUtil.splitJson(response);
					System.out.println(resMap);
					//= Step 3) 통신후 resMap 파싱 & setter
					paGmktShipCostM.setGmktShipNo(resMap.get("placeNo").toString());
					//paGmktShipCostM.setIslandCost(Double.parseDouble(resMap.get("backwoodsAdditionalShippingFee").toString()));
					//paGmktShipCostM.setJejuCost(Double.parseDouble(resMap.get("jejuAdditionalShippingFee").toString()));
					paGmktShipCostM.setEntpCode(entpCode);
					paGmktShipCostM.setEntpManSeq(entpManSeq);					
				}				
		    	//= Step 4) table insert
		    	paGmktCommonService.savePaGmktShipCostMUpdateTx(paGmktShipCostM);
		    	
			}
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			
			if("400".equals(paramMap.getString("code")) && paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_00_009500") > -1){
					
				/* 500에러 발생 시
				 * STEP 1. PAGMKTSHIPCOSTM USE_YN = 0, ERROR_YN = '1' 처리
				 * STEP 2. 상품 타켓 켜친 채로 EXCEPTION 발생
				 * STEP 3. 다음 배치 시간때, 출하지 등록 + 묶음배송 등록 API 진행. 
				 * 
				 * */
				paEntpSlip.setOldGmktShipNo(map.get("gmktShipNo").toString());
				paGmktCommonService.updatePaGmktShipCostMUseYnTx(paEntpSlip);
				map.put("putShippingPlaces500Flag","Y");
			}
			
			throw processException("msg.cannot_save",new String[] {paramMap.getString("message")});
			
			//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
				
				if("400".equals(paramMap.getString("code")) && paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_00_009500") > -1){
					
					/* 500에러 발생 시
					 * STEP 1. PAGMKTSHIPCOSTM USE_YN = 0, ERROR_YN = '1' 처리
					 * STEP 2. 상품 타켓 켜친 채로 EXCEPTION 발생
					 * STEP 3. 다음 배치 시간때, 출하지 등록 + 묶음배송 등록 API 진행. 
					 * 
					 * */
					paEntpSlip.setOldGmktShipNo(map.get("gmktShipNo").toString());
					paGmktCommonService.updatePaGmktShipCostMUseYnTx(paEntpSlip);
					map.put("putShippingPlaces500Flag","Y");
				}
				
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(!searchTermGb.equals("1")){
				if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));	
			}
			log.info("===== 출하지 수정 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 * 묶음배송 등록(출하지별)
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "묶음배송 등록(출하지별)", notes = "묶음배송 등록(출하지별)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-shipping-policies", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postShippingPolicies(HttpServletRequest request,
			@ApiParam(name = "gmktShipNo", value = "GMKT_SHIP_NO", defaultValue = "") @RequestParam(value="gmktShipNo"	, required=true) String gmktShipNo,
			@ApiParam(name = "searchTermGb", value = "중복실행검사여부('1':진행)", defaultValue = "") @RequestParam(value="searchTermGb"	, required=false) String searchTermGb) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCommonRest();
		String apiCode = "IF_PAGMKTAPI_V2_00_011";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("paCode", "21");//EBAY 묶음배송 등록은 계정하나로 통합되어있다.
		
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			}
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			List<HashMap<String,String>> mapList = paGmktCommonService.selectEntpShipPoliciesInsertList(gmktShipNo);
			for(Map<String,String> requestMap : mapList){
				paramMap.put("map", requestMap);
				//조건부배송일때 배송비 0원이면 api를 발송하지 않음, 검증 로직
				if(Integer.parseInt(String.valueOf(requestMap.get("ORD_COST_AMT"))) < 100 
						&& (requestMap.get("SHIP_COST_CODE").substring(0, 2).equals("CN") || requestMap.get("SHIP_COST_CODE").substring(0, 2).equals("PL") || requestMap.get("SHIP_COST_CODE").substring(0, 2).equals("ID"))){
					continue;
				}
				//= Step 2) 통신
				String response = restUtil.getConnection(rest,  paramMap);
				Map<String,Object> resMap = ComUtil.splitJson(response);
				
				System.out.println(resMap);
				//= Step 3) 통신후 resMap 파싱 & setter
				HashMap<String,Object> resultMap = new HashMap<>();
				resultMap.put("placeNo", resMap.get("placeNo"));
				resultMap.put("policyNo", resMap.get("policyNo"));
				resultMap.put("modifyId", "PAG");
				resultMap.put("entpCode", requestMap.get("ENTP_CODE"));
				resultMap.put("entpManSeq", requestMap.get("ENTP_MAN_SEQ"));
				resultMap.put("shipCostCode", requestMap.get("SHIP_COST_CODE"));
				
		    	//= Step 4) table insert
		    	paGmktCommonService.updateEntpShipPoliciesInsertTx(resultMap);
			}
			
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
			throw processException("msg.cannot_save",new String[] {paramMap.getString("message")});
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(!searchTermGb.equals("1")){
				if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}
			log.info("===== 묶음배송 등록 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 묶음배송비 수정
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "묶음배송비 수정", notes = "묶음배송비 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-shipping-policies", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putShippingPolicies(HttpServletRequest request,
			@ApiParam(name = "gmktShipNo", value = "GMKT_SHIP_NO", defaultValue = "") @RequestParam(value="gmktShipNo"	, required=true) String gmktShipNo,
			@ApiParam(name = "searchTermGb", value = "중복실행검사여부('1':진행)", defaultValue = "") @RequestParam(value="searchTermGb"	, required=false) String searchTermGb) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCommonRest();
		String apiCode = "IF_PAGMKTAPI_V2_00_012";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("paCode", "21");//EBAY 묶음배송 등록은 계정하나로 통합되어있다.
		
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			}
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			List<HashMap<String,String>> mapList = paGmktCommonService.selectEntpShipPoliciesModifyList(gmktShipNo);
			for(Map<String,String> requestMap : mapList){
				paramMap.put("map", requestMap);
				paramMap.put("urlParameter",requestMap.get("BUNDLE_NO"));
				
				//조건부배송일때 배송비 0원이면 api를 발송하지 않음, 검증 로직
				if(Integer.parseInt(String.valueOf(requestMap.get("ORD_COST_AMT"))) < 100 
						&& (requestMap.get("SHIP_COST_CODE").substring(0, 2).equals("CN") || requestMap.get("SHIP_COST_CODE").substring(0, 2).equals("PL") || requestMap.get("SHIP_COST_CODE").substring(0, 2).equals("ID"))){
					continue;
				}
				
				//= Step 2) 통신
				String response = restUtil.getConnection(rest,  paramMap);
				Map<String,Object> resMap = ComUtil.splitJson(response);
				System.out.println(resMap);
				//= Step 3) 통신후 resMap 파싱 & setter
				HashMap<String,Object> resultMap = new HashMap<>();
				resultMap.put("placeNo", resMap.get("placeNo"));
				resultMap.put("policyNo", resMap.get("policyNo"));
				resultMap.put("modifyId", "PAG");
				
		    	//= Step 4) table insert
		    	paGmktCommonService.updateEntpShipPoliciesModifyTx(resultMap);
	    	
			}
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
			throw processException("msg.cannot_save",new String[] {paramMap.getString("message")});
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(!searchTermGb.equals("1")){
				if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));	
			}
			log.info("===== 묶음배송 수정 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 발송정책 조회 후 저장
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "발송정책 조회 후 저장", notes = "발송정책 조회 후 저장", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/get-shipping-dispatch-policies", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getShippingDispatchPolicies(HttpServletRequest request,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,
			@ApiParam(name = "paCode", value = "제휴사그룹코드", defaultValue = "") @RequestParam(value="paGroupCode", required=true) String paGroupCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCommonRest();
		String apiCode = "IF_PAGMKTAPI_V2_00_015";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		paramMap.put("paCode",paCode);
		if(paGroupCode.equals("02")){
			paramMap.put("siteGb", "PAG");
		}else{
			paramMap.put("siteGb", "PAA");
		}
		paramMap.put("paGroupCode", paGroupCode);
		
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			//= Step 2) 통신
			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			System.out.println(resMap);
			
			//= Step 3) 통신후 resMap 파싱 & setter
			List<PaGmktPolicy> policies = new ArrayList<>();
			List<HashMap<String,Object>> dispatchPolicies = (List<HashMap<String, Object>>) resMap.get("dispatchPolicies");
			for(HashMap<String,Object> policy : dispatchPolicies){
				PaGmktPolicy gmktPolicy = new PaGmktPolicy();
				gmktPolicy.setPaCode(paramMap.getString("paCode"));
				gmktPolicy.setPolicyNo(policy.get("dispatchPolicyNo").toString());
				gmktPolicy.setPolicyType(policy.get("dispatchType").toString());
				gmktPolicy.setDuration(policy.get("readyDurationDay").toString());
				gmktPolicy.setIsDefault(policy.get("isDefault").toString());
				gmktPolicy.setPaGroupCode(paramMap.getString("paGroupCode"));
				policies.add(gmktPolicy);
			}
			
			//= Step 4) table insert
	    	paGmktCommonService.savePaGmktPolicyTx(policies);
    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 발송정책 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 정산 전체 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "정산 전체 조회", notes = "정산 전체 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	//IF_PAGMKTAPI_V2_04_001_L
	@RequestMapping(value = "/get-settle-list/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getSettleList(HttpServletRequest request,
			@ApiParam(name = "remitDate", value = "검색시작일", defaultValue = "") @RequestParam(value="remitDate", required=false, defaultValue = "") String fromDate, 
			@ApiParam(name = "paGroupCode", value = "제휴사코드", defaultValue = "") @PathVariable("paGroupCode") String paGroupCode) 
			throws Exception{
		ParamMap paramMap = new ParamMap();
		String remitDate = "";
		
		if(!(fromDate.isEmpty() || fromDate.equals(""))){
			remitDate = fromDate;
		} else {
			remitDate = systemService.getRemitDate();
		}
		
		paramMap.put("remitDate", remitDate);
		paramMap.put("paGroupCode", paGroupCode);
		
		//paGmktCommonService.deletePaGmktSettleTx(paramMap); //pa_group_code 구분으로 해당 정산대사만 지울 수 있도록
		
		String paCode[] = {"21", "22"};
		for(String pa : paCode){
			getSettleOrder(request, pa, remitDate, paGroupCode);
			getSettleDelivery(request, pa, remitDate, paGroupCode);
		}
		
		paramMap.put("code", "200");
		paramMap.put("message", "OK");
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 정산 판매대금 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "정산 판매대금 조회", notes = "정산 판매대금 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/get-settle-order", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getSettleOrder(HttpServletRequest request,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,
			@ApiParam(name = "remitDate", value = "검색시작일", defaultValue = "") @RequestParam(value="remitDate", required=true) String remitDate,
			@ApiParam(name = "paGroupCode", value = "제휴사그룹코드", defaultValue = "") @RequestParam(value="paGroupCode", required=true) String paGroupCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCommonRest();
		String apiCode = "IF_PAGMKTAPI_V2_04_001";            
		String duplicateCheck = "";
		PaGmktSettlement paGmktSettlement = null;
		List<PaGmktSettlement> paGmktSettlementList = new ArrayList<PaGmktSettlement>();
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paGroupCode", paGroupCode);
		CommonUtil.setParams(paramMap);
		
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("remitDate", remitDate);
		paramMap.put("paCode", paCode);
		
		//G마켓, 옥션 구분 값
		String siteType = (paGroupCode.equals("02")) ? "G" : "A";
		paramMap.put("siteType", siteType);
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			//duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			//if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			
			//= Step 2) 통신
			paramMap.put("PageNo", 0);
			paramMap.put("PageRowCnt", 0);
			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			if( Integer.parseInt(resMap.get("TotalCount").toString()) > 0 ){
				paramMap.put("PageNo", 1);
				paramMap.put("PageRowCnt", resMap.get("TotalCount"));
				response = restUtil.getConnection(rest,  paramMap);
				resMap = ComUtil.splitJson(response);
			}else{
				paramMap.put("code", "200");
				paramMap.put("message", "OK");
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			//= Step 3) 통신후 resMap 파싱 & setter
			List<Object> resList = (List<Object>)resMap.get("Data");
			
			for(Object gmktSettlement : resList) {
				HashMap<String,Object> result = new HashMap<>();
				paGmktSettlement = new PaGmktSettlement();
				
				result = (HashMap<String, Object>) gmktSettlement;
				
				paGmktSettlement.setGatherDate(paramMap.getString("remitDate"));
				
				//GMKT, AUCTION 구분 값
				paGmktSettlement.setSiteType(siteType);
				
				paGmktSettlement.setPayNo(result.get("PackNo").toString());
				paGmktSettlement.setContrNo(result.get("ContrNo").toString());
				
				//GMKT만 존재
				if(paGroupCode.equals("02")){
					paGmktSettlement.setGoodsNo(result.get("GoodsNo").toString());
					paGmktSettlement.setSettleExceptName(result.get("SettleExceptName").toString());
				}
				paGmktSettlement.setSiteGoodsNo(result.get("SiteGoodsNo").toString());
				paGmktSettlement.setOrderDate(DateUtil.toTimestamp(result.get("OrderDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));
				paGmktSettlement.setPayDate(DateUtil.toTimestamp(result.get("PayDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));//(result.get("PayDate").toString());
				paGmktSettlement.setShippingDate(DateUtil.toTimestamp(result.get("ShippingDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));//(result.get("ShippingDate").toString());
				paGmktSettlement.setShippingCmplDate(DateUtil.toTimestamp(result.get("ShippingCmplDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));//(result.get("ShippingCmplDate").toString());
				paGmktSettlement.setBuyDecisionDate(DateUtil.toTimestamp(result.get("BuyDecisonDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));//(result.get("BuyDecisonDate").toString());
				paGmktSettlement.setSettleExpectDate(DateUtil.toTimestamp(result.get("SettleExpectDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));//(result.get("SettleExpectDate").toString());
				paGmktSettlement.setRemitDate(DateUtil.toTimestamp(result.get("RemitDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));//(result.get("RemitDate").toString());
				paGmktSettlement.setRefundDate(DateUtil.toTimestamp(result.get("RefundDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));//(result.get("RefundDate").toString());
				paGmktSettlement.setSettleType(result.get("SettleType").toString());
				paGmktSettlement.setOrderUnitPrice(result.get("OrderUnitPrice").toString());
				paGmktSettlement.setOrderQty(result.get("OrderQty").toString());
				paGmktSettlement.setSellOrderPrice(result.get("SellOrderPrice").toString());
				paGmktSettlement.setOptionPrice(result.get("OptionPrice").toString());
				paGmktSettlement.setGoodsCost(result.get("GoodsCost").toString());
				paGmktSettlement.setOptionCost(result.get("OptionCost").toString());
				paGmktSettlement.setBasicCommission(result.get("BasicCommission").toString());
				paGmktSettlement.setOptionCommission(result.get("OptionCommission").toString());
				paGmktSettlement.setDeductTaxPrice(result.get("DeductTaxPrice").toString());
				paGmktSettlement.setTotCommission(result.get("TotCommission").toString());
				paGmktSettlement.setDeductNonTaxPrice(result.get("DeductNontaxPrice").toString());
				paGmktSettlement.setSettlementPrice(result.get("SettlementPrice").toString());
				paGmktSettlement.setServiceFee(result.get("ServiceFee").toString());
				paGmktSettlement.setCorpDiscountTotalPrice(result.get("CorpDiscountTotalPrice").toString());
				paGmktSettlement.setOverProfitDiscountPrice(result.get("OverProfitDiscountPrice").toString());
				paGmktSettlement.setFeeDiscountPrice(result.get("FeeDiscountPrice").toString());
				paGmktSettlement.setSellerDiscountTotalPrice(result.get("SellerDiscountTotalPrice").toString());
				paGmktSettlement.setBuyerPayAmt(result.get("BuyerPayAmt").toString());
				paGmktSettlement.setBranchPrice(result.get("BranchPrice").toString());
				paGmktSettlement.setBranchCode(result.get("BranchCode").toString());
				paGmktSettlement.setTaxYn(result.get("TaxYn").toString());
				paGmktSettlement.setSellerDiscountPrice1(result.get("SellerDiscountPrice1").toString());
				paGmktSettlement.setSellerDiscountPrice2(result.get("SellerDiscountPrice2").toString());
				paGmktSettlement.setSellerDiscountPrice(result.get("SellerDiscountPrice").toString());
				paGmktSettlement.setSellerPcsFee(result.get("SellerPcsFee").toString());
				paGmktSettlement.setDelFeeOverseaAmt(result.get("DelFeeOverseaAmt").toString());
				paGmktSettlement.setKind(result.get("Kind").toString());
				paGmktSettlement.setSettlementType("00");
				paGmktSettlement.setRevenueBaseDate(DateUtil.toTimestamp(result.get("RevenueBaseDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));
				
				paGmktSettlementList.add(paGmktSettlement);
			}
			
			//= Step 4) table insert
			paGmktCommonService.savePaGmktSettleOrderTx(paGmktSettlementList);
	    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			//if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 정산 판매대금 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 정산 배송비 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "정산 배송비 조회", notes = "정산 배송비 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/get-settle-delivery", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getSettleDelivery(HttpServletRequest request,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,
			@ApiParam(name = "remitDate", value = "검색시작일", defaultValue = "") @RequestParam(value="remitDate", required=false) String remitDate,
			@ApiParam(name = "paGroupCode", value = "제휴사그룹코드", defaultValue = "") @RequestParam(value="paGroupCode", required=true) String paGroupCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCommonRest();
		String apiCode = "IF_PAGMKTAPI_V2_04_002";            
		String duplicateCheck = "";
		PaGmktSettlement paGmktSettlement = null;
		List<PaGmktSettlement> paGmktSettlementList = new ArrayList<PaGmktSettlement>();
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paGroupCode", paGroupCode);
		CommonUtil.setParams(paramMap);
		
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("remitDate", remitDate);
		paramMap.put("paCode", paCode);
		
		//G마켓, 옥션 구분 값
		String siteType = (paGroupCode.equals("02")) ? "G" : "A";
		paramMap.put("siteType", siteType);
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			//duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			//if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			
			//= Step 2) 통신
			paramMap.put("PageNo", 0);
			paramMap.put("PageRowCnt", 0);
			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			if( Integer.parseInt(resMap.get("TotalCount").toString()) > 0 ){
				paramMap.put("PageNo", 1);
				paramMap.put("PageRowCnt", resMap.get("TotalCount"));
				response = restUtil.getConnection(rest,  paramMap);
				resMap = ComUtil.splitJson(response);
			}else{
				paramMap.put("code", "200");
				paramMap.put("message", "OK");
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			//= Step 3) 통신후 resMap 파싱 & setter
			List<Object> resList = (List<Object>)resMap.get("Data");
			
			for(Object gmktSettlement : resList) {
				HashMap<String,Object> result = new HashMap<>();
				paGmktSettlement = new PaGmktSettlement();
				
				result = (HashMap<String, Object>) gmktSettlement;
				
				paGmktSettlement.setGatherDate(paramMap.getString("remitDate"));
				paGmktSettlement.setSiteType(siteType);
				paGmktSettlement.setPayNo(result.get("PackNo").toString());
				paGmktSettlement.setPayDate(DateUtil.toTimestamp(result.get("PayDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));//(result.get("PayDate").toString());
				paGmktSettlement.setRefundDate(DateUtil.toTimestamp(result.get("RefundDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));//(result.get("RefundDate").toString());
				paGmktSettlement.setRemitDate(DateUtil.toTimestamp(result.get("RemitDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));//(result.get("RemitDate").toString());
				paGmktSettlement.setDelFeeAmt(result.get("DelFeeAmt").toString());
				paGmktSettlement.setDelFeeNo(result.get("DelFeeNo").toString());
				paGmktSettlement.setDelFeePayWay(result.get("DelFeePayWay").toString());
				paGmktSettlement.setKind(result.get("Kind").toString());
				paGmktSettlement.setDeliveryGroupNo(result.get("DeliveryGroupNo").toString());
				paGmktSettlement.setDelFeeType(result.get("DelFeeType").toString());
				paGmktSettlement.setSettlementType("01");
				paGmktSettlement.setRevenueDate(DateUtil.toTimestamp(result.get("RevenueDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));
				paGmktSettlement.setDelFeeCommission(result.get("DelFeeCommission").toString());
				
				paGmktSettlementList.add(paGmktSettlement);
			}
			
			//= Step 4) table insert
			paGmktCommonService.savePaGmktSettleDeliveryTx(paGmktSettlementList);
	    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			//if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 정산 배송비 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "판매자주소 등록 후 출고지 insert", notes = "판매자주소 등록 후 출고지 insert", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/savePaGmktShipCostM", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> savePaGmktShipCostM(HttpServletRequest request,PaEntpSlip paEntpSlip) throws Exception{
		log.info("판매자주소 등록 후 출고지 insert");
		ParamMap paramMap = new ParamMap();
		paGmktCommonService.savePaGmktShipCostMTx(paEntpSlip);
		paramMap.put("code", "200");
		paramMap.put("message", "OK");
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "출고지 등록 후 묶음배송 insert", notes = "출고지 등록 후 묶음배송 insert", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/savePaGmktShipCostDt", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> savePaGmktShipCostDt(HttpServletRequest request,PaEntpSlip paEntpSlip) throws Exception{
		log.info("출고지 등록 후 묶음배송 insert");
		ParamMap paramMap = new ParamMap();
		paGmktCommonService.savePaGmktShipCostDtTx(paEntpSlip);
		paramMap.put("code", "200");
		paramMap.put("message", "OK");
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	
	/**
	 * 상품 등록 이전 처리(판매자주소 등록 -> 출고지 등록 -> 출고지별 묶음배송 등록) 
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "상품 등록 이전 처리(판매자주소 등록 -> 출고지 등록 -> 출고지별 묶음배송 등록)", notes = "상품 등록 이전 처리(판매자주소 등록 -> 출고지 등록 -> 출고지별 묶음배송 등록)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/before-goods-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> beforeGoodsInsert(HttpServletRequest request, ParamMap mapFor500,
			@RequestParam(value="goodsCode"		, required=true) String goodsCode,
			@RequestParam(value="searchTermGb"  , required=false) String searchTermGb
			) throws Exception{
		
		//TODO 다음 로직은 이베이 회수지/출고지 배송비 연동 안정화가 되면 소스에서 삭제 할 예정.. HSBAEK
		String val = paGmktCommonService.selectTConfigVal("USE_NEW_GMKT_SHIPCOST_YN");
		if("Y".equals(val)) {
			return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "noUse"), HttpStatus.OK);
		}// 여기까지..
		
		
		ParamMap paramMap = new ParamMap();
		log.info("===== 상품 등록 기초정보 setting =====");
		HashMap<String,String> baseEntpInfo = paGmktCommonService.selectBeforeInsertGoodsBaseInfo(goodsCode);
		PaEntpSlip paEntpSlip = new PaEntpSlip();
		if(baseEntpInfo!=null){
			paEntpSlip.setEntpCode(baseEntpInfo.get("ENTP_CODE"));
			paEntpSlip.setEntpManSeq(baseEntpInfo.get("SHIP_MAN_SEQ"));
		}
		
		String entpCode ="";
		String entpManSeq ="";
		String entpManGb ="";
		String gmktShipNo ="";
		
		log.info("===== 상품 등록 이전 처리 PROCESS START =====");
		//상품코드 기준 전체SELECT필요
		//상품등록때는 상품코드 필수, 상품 수정떄는 상품코드 없이 해야됨.
		log.info("===== 판매자주소 상품코드 기준 등록 =====");
		List<HashMap<String,String>> entpInfoList = paGmktCommonService.selectBeforeInsertGoodsEntp(goodsCode);
		for(HashMap<String,String> entpInfo : entpInfoList){
			entpCode = entpInfo.get("ENTP_CODE");
			entpManSeq = entpInfo.get("ENTP_MAN_SEQ");
			entpManGb = entpInfo.get("ENTP_MAN_GB");
			//판매자주소등록
			postSellerAddr(request, entpCode, entpManSeq, entpManGb, searchTermGb);
		}	
		
		//상품 수정떄 이걸 매번 왜하고있지?
		log.info("===== 판매자주소 수정 =====");
		List<HashMap<String,String>> entpInfoModifyList = paGmktCommonService.selectBeforeInsertGoodsEntpModify(paramMap);
		for(HashMap<String,String> entpInfo : entpInfoModifyList){
			entpCode = entpInfo.get("ENTP_CODE");
			entpManSeq = entpInfo.get("ENTP_MAN_SEQ");
			entpManGb = entpInfo.get("ENTP_MAN_GB");
			
			if(!(paEntpSlip.getEntpCode().equals(entpCode) && paEntpSlip.getEntpManSeq().equals(entpManSeq))){
				continue;
			}
			
			//판매자주소수정
			putSellerAddr(request, entpCode, entpManSeq, entpManGb, searchTermGb);
		}	
		
		log.info("===== 판매자주소반영건 m생성 =====");
		if(baseEntpInfo!=null){
			savePaGmktShipCostM(request,paEntpSlip);
		}
		log.info("===== 출고지 일괄 등록 =====");
		List<HashMap<String,String>> entpInfoForShipList = paGmktCommonService.selectBeforeInsertGoodsShip();
		for(HashMap<String,String> entpInfo : entpInfoForShipList){ 
			entpCode = entpInfo.get("ENTP_CODE");
			entpManSeq = entpInfo.get("ENTP_MAN_SEQ");
			entpManGb = entpInfo.get("PA_ADDR_GB");
			
			if(!(paEntpSlip.getEntpCode().equals(entpCode) && paEntpSlip.getEntpManSeq().equals(entpManSeq))){
				continue;
			}
			
			//출고지등록 m
			postShippingPlaces(request, entpCode, entpManSeq, searchTermGb);
		}
		log.info("===== 출고지(여러정책적용or 수정시) 일괄 수정 =====");
		List<HashMap<String,String>> entpInfoForShipModifyList = paGmktCommonService.selectBeforeInsertGoodsShipModify();
		for(HashMap<String,String> entpInfo : entpInfoForShipModifyList){
			entpCode = entpInfo.get("ENTP_CODE");
			entpManSeq = entpInfo.get("ENTP_MAN_SEQ");
			entpManGb = entpInfo.get("PA_ADDR_GB");
			
			if(!(paEntpSlip.getEntpCode().equals(entpCode) && paEntpSlip.getEntpManSeq().equals(entpManSeq))){
				continue;
			}
			
			//출고지수정 m
			putShippingPlaces(request, mapFor500, entpCode, entpManSeq, searchTermGb);
		}

		log.info("===== 출고지반영건 dt생성 =====");
		if(baseEntpInfo!=null){
			savePaGmktShipCostDt(request,paEntpSlip);
		}
		
		log.info("===== 묶음배송 일괄 등록 =====");
		List<HashMap<String,String>> entpInfoForBundleList = paGmktCommonService.selectBeforeInsertGoodsBundle();
		for(HashMap<String,String> entpInfo : entpInfoForBundleList){ 
			gmktShipNo = entpInfo.get("GMKT_SHIP_NO");
			entpCode = entpInfo.get("ENTP_CODE");
			entpManSeq = entpInfo.get("ENTP_MAN_SEQ");
			
			if(!(paEntpSlip.getEntpCode().equals(entpCode) && paEntpSlip.getEntpManSeq().equals(entpManSeq))){
				continue;
			}
			
			//묶음배송등록 dt
			postShippingPolicies(request, gmktShipNo, searchTermGb);
		}
		log.info("===== 묶음배송 일괄 수정 =====");
		List<HashMap<String,String>> entpInfoForBundleModifyList = paGmktCommonService.selectBeforeInsertGoodsBundleModify();
		for(HashMap<String,String> entpInfo : entpInfoForBundleModifyList){ 
			gmktShipNo = entpInfo.get("GMKT_SHIP_NO");
			entpCode = entpInfo.get("ENTP_CODE");
			entpManSeq = entpInfo.get("ENTP_MAN_SEQ");
			
			if(!(paEntpSlip.getEntpCode().equals(entpCode) && paEntpSlip.getEntpManSeq().equals(entpManSeq))){
				continue;
			}
			
			//묶음배송수정 dt
			putShippingPolicies(request, gmktShipNo, searchTermGb);
		}
		
		updatePaGmktShipCostMFor500(request, paEntpSlip, goodsCode);
		
		paramMap.put("code", "200");
		paramMap.put("message", "OK");
		log.info("===== 상품 등록 이전 처리 PROCESS END =====");
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	private void updatePaGmktShipCostMFor500(HttpServletRequest request, PaEntpSlip paEntpSlip, String goodsCode) throws Exception {
		
		PaGmktShipCostM paGmktShipCostM = new PaGmktShipCostM();
		paGmktShipCostM.setEntpCode(paEntpSlip.getEntpCode());
		paGmktShipCostM.setEntpManSeq(paEntpSlip.getEntpManSeq());
		
		HashMap<String, String> oldShipCostM = paGmktCommonService.selectPaGmktShipCostMFor500(paGmktShipCostM);
    	if(oldShipCostM != null){
    		if(paGmktCommonService.updatePaGmktShipCostMErrorYn(paGmktShipCostM) < 1){
    			log.error("500에러 출고지 재 등록 후 TPAGMKTSHIPCOSTM TRANS_ERROR_YN 0 처리 fail entpCode: " + paEntpSlip.getEntpCode() + " entpManSeq: " + paEntpSlip.getEntpManSeq());
				throw processException("msg.cannot_save", new String[] { "500에러 출고지 재 등록 후 TPAGMKTSHIPCOSTM TRANS_ERROR_YN 0 처리 fail" });
    		}
    		
    		//해당 출고지에 속한 상품 TARGET-ON
    		List<HashMap<String, String>> goodsList = paGmktCommonService.selectPaGmtkGoodsTargetList(paEntpSlip);
    		if(goodsList.size() > 0){
    			for(HashMap<String, String> goods : goodsList){
    				if(!goodsCode.equals(goods.get("GOODS_CODE").toString())){
    					paGmktCommonService.updatePaGmtkGoodsTargetOn(goods.get("GOODS_CODE").toString());
    				}
    			}
    		}
    	}
	}
	
	
	
	
	/*
	 * ***********************************************************************************************************************************
	 * 배송비 개선 GOGOSING~******************************************************************************************************************
	 * ***********************************************************************************************************************************
	 * ***********************************************************************************************************************************
	 */
	@ApiOperation(value = "배송비 개선", notes = "배송비 개선", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/legacy-enroll-entp-shipcost", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> enrollEntpShipCost(HttpServletRequest request,
												@RequestParam(value="entpCode"		, required=false) String entpCode,
												//@RequestParam(value="entpManSeq"	, required=false) String entpManSeq,  TODO 추가 필요
												@RequestParam(value="searchTermGb"  , required=false) String searchTermGb) throws Exception{
		ParamMap paramMap 		= new ParamMap();
		String code 				= "200";
		String message 			= "OK";
		String apiCode		  	= "PAGMKT_ENROLL_ENTP_SHIP"; 
		String duplicateCheck 	= "";
		paramMap.put("entpCode", entpCode);
		
		try {
			//TODO 다음 로직은 이베이 회수지/출고지 배송비 연동 안정화가 되면 소스에서 삭제 할 예정.. HSBAEK
			String val = paGmktCommonService.selectTConfigVal("USE_NEW_GMKT_SHIPCOST_YN");
			if("N".equals(val)) {
				return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "noUse"), HttpStatus.OK);
			}// 여기까지..
			
			//중복실행
			if(!"1".equals(searchTermGb)){
				duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			}
						
			//Step 1. 판매자 주소 등록
			List<HashMap<String,String>> entpInfoList = paGmktCommonService.selectNewGoodsEntpTarget(paramMap);
			for(HashMap<String,String> entpInfo : entpInfoList){
				try {
					postSellerAddr (request, entpInfo.get("ENTP_CODE"), entpInfo.get("ENTP_MAN_SEQ"), entpInfo.get("ENTP_MAN_GB"), searchTermGb); //판매자 주소등록 API 호출 밑 INSERT TPAENTPSLIP
					//신규 판매자 주소 배송비 내부 데이터 생성 INSERT/UPDATE TPAGMKTSHIPCOSTDT
					HashMap<String,String> custshipcost = paGmktCommonService.selectEntpSlipCostByEntpCodeNSeq(entpInfo);  // TPACUSTSHIPCOST 존재, TPAGMKTSHIPCOSTDT 존재하지 않음
					if(custshipcost == null || custshipcost.isEmpty()) continue;
					paGmktCommonService.saveTPaGmktShipCostdtTx(custshipcost);					
				}catch (Exception e) {
					log.info(entpInfo.get("ENTP_CODE").toString() + e.getMessage());
					continue;
				}
			}
			
			//Step 2. 판매자 주소 수정
			List<HashMap<String,String>> entpInfoModifyList = paGmktCommonService.selectBeforeInsertGoodsEntpModify(paramMap);
			for(HashMap<String,String> entpInfo : entpInfoModifyList){
				try {
					putSellerAddr  (request, entpInfo.get("ENTP_CODE"), entpInfo.get("ENTP_MAN_SEQ"), entpInfo.get("ENTP_MAN_GB"), searchTermGb);	
				}catch (Exception e) {
					log.info(entpInfo.get("ENTP_CODE").toString() + e.getMessage());
					continue;
				}
			}
			
			//Step 3. 이베이 배송비 관련 내부 데이터 생성  INSERT/UPDATE TPAGMKTSHIPCOSTDT
			List<HashMap<String,String>> custShipCostList = paGmktCommonService.selectEntpSlipCost(paramMap);  // TPACUSTSHIPCOST 존재, TPAGMKTSHIPCOSTDT 존재하지 않음
			for(HashMap<String,String> custshipcost : custShipCostList) {
				saveTPaGmktShipCostdt(custshipcost);
			}
			
			//Step 4. 출하지 관리 API
			//   ㄴ 4-1. 출하지 등록 
			List<HashMap<String,String>> entpInfoForShipInsert = paGmktCommonService.selectEntpSlipShip4Insert(paramMap);
			for(HashMap<String,String> entpInfo : entpInfoForShipInsert) {
				shippingPlaces( request ,entpInfo.get("ENTP_CODE"), entpInfo.get("ENTP_MAN_SEQ"), entpInfo.get("PA_ADDR_SEQ") , null , "I" ,"1");
			}
			
			//   ㄴ 4-2. 출하지 수정 (배송비 변경에 따른 제주/도서산간 배송비 재 연동)
			List<HashMap<String,String>> entpInfoForShipModify = paGmktCommonService.selectEntpSlipShip4Modify(paramMap);
			for(HashMap<String,String> entplnfo : entpInfoForShipModify) {
				shippingPlaces( request ,entplnfo.get("ENTP_CODE"), entplnfo.get("ENTP_MAN_SEQ"), entplnfo.get("PA_ADDR_SEQ") , entplnfo.get("PA_GMKT_SHIP_NO") , "U" ,"1");
			}
			
			//Step 5. 이베이 배송비 연동(묶음배송비 연동 - 등록/수정)  
			List<HashMap<String,String>> pagmktModifyShipCostList = paGmktCommonService.selectPaGmktShipCostTargetList(paramMap);  //TPAGMKTSHIPCOSTDT.TRANS_TARGET = 1
			for(HashMap<String,String> shipcost : pagmktModifyShipCostList ) {
				shippingPolicies(request, shipcost.get("ENTP_CODE"), shipcost.get("ENTP_MAN_SEQ"), "1" );	
			}
						
		}catch (Exception e) {
			code = "498";
			message = e.getMessage() == null ? e.toString() : e.getMessage();
			log.error(message);
		}finally {
			if(!"1".equals(searchTermGb)){
				if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);	
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg( code, message ), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/shipping-places", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> shippingPlaces(HttpServletRequest request,
			@RequestParam(value="entpCode"		, required=true) String entpCode,			
			@RequestParam(value="entpManSeq"	, required=true) String entpManSeq,
			@RequestParam(value="paAddrSeq"		, required=true) String paAddrSeq,
			@RequestParam(value="paGmktShipNo"	, required=false) String paGmktShipNo,		
			@RequestParam(value="apiType"		, required=true) String apiType,
			@RequestParam(value="searchTermGb"	, required=false) String searchTermGb
			) throws Exception{
		
		ParamMap paramMap 		= new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCommonRest();
		String apiCode 			= "I".equals(apiType)?"IF_PAGMKTAPI_V2_00_008" : "IF_PAGMKTAPI_V2_00_009"; // IF_PAGMKTAPI_V2_00_008 : 출하지 등록, IF_PAGMKTAPI_V2_00_009 : 출하지 수정
		String duplicateCheck	= "0";
		
		paramMap.put("apiCode"		, apiCode);
		paramMap.put("startDate"	, systemService.getSysdatetimeToString());
		paramMap.put("siteGb"		, "PAG");
		paramMap.put("paCode"		, "21");//EBAY 출하지 등록은 계정하나로 통합되어있다.
		if(paGmktShipNo != null) paramMap.put("urlParameter", paGmktShipNo);  //출하지 수정의 경우 출하지 번호 필수..

		try{			
			//= 중복 실행 Check
			if(!"1".equals(searchTermGb)){
				duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			}
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			HashMap<String,String> templetMap = new HashMap<String, String>();
			templetMap.put("ENTP_CODE"		, entpCode);
			templetMap.put("ENTP_MAN_SEQ"	, entpManSeq);
			templetMap.put("PA_ADDR_SEQ"	, paAddrSeq);

			HashMap<String,String> map = paGmktCommonService.selectMaxShipCostFee(templetMap);
			
			templetMap.put("ORD_COST"		, String.valueOf(map.get("ORD_COST")));
			templetMap.put("ISLAND_COST"	, String.valueOf(map.get("ISLAND_COST")));
			templetMap.put("JEJU_COST"		, String.valueOf(map.get("JEJU_COST")));
			
			paramMap.put("realYn"	, systemService.getConfig("PA_REAL_SERVER_YN").getVal().equals("Y") ?  "REAL" : "DEV");
			paramMap.put("map"		, templetMap);
			paramMap.put("USE_NEW_GMKT_SHIPCOST_YN", paGmktCommonService.selectTConfigVal("USE_NEW_GMKT_SHIPCOST_YN")); //TODO 안정화 이후 제거 필요
			
			String response = restUtil.getConnection(rest,  paramMap);
						
			if(response != null) {
				if(response.indexOf("동일한 출하지") > 0 ) throw processException("partner.already_data", new String[] {"출하지(동일)"});				
			}
			
			Map<String,Object> resMap 	= ComUtil.splitJson(response);
			String placeNo 				=  resMap.get("placeNo").toString();
			if(placeNo == null || "".equals(placeNo)) throw processException("partner.not.targetData", new String[] {"출하지번호(placeNo)"});
			templetMap.put("GMKT_SHIP_NO"		, placeNo);
			templetMap.put("SEND_ISLAND_COST"	, String.valueOf(paramMap.get("backwoodsAdditionalShippingFee"))); //실제로 연동한 도서산간 배송비
			templetMap.put("SEND_JEJU_COST"		, String.valueOf(paramMap.get("jejuAdditionalShippingFee")));	   //실제로 연동한 제주 배송비
			
			//UPDATE TPAGMKTSHIPCOSTDT.GMKT_SHIP_NO  , TPAENTPSLIP.GMKT_SHIP_NO
			paGmktCommonService.saveGmktShipNoTx(templetMap);
			
			paramMap.put("code"		, "200");
			paramMap.put("message"	, "OK");
		
		} catch (Exception se) {
			log.error(paramMap.getString("message"), se);
			CommonUtil.dealException(se,paramMap);
			
		}finally {
			CommonUtil.dealSuccess(paramMap, request);
			if(!"1".equals(searchTermGb)){
				if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));	
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/shipping-policies", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> shippingPolicies(HttpServletRequest request,
			@RequestParam(value="entpCode"		, required=true) String entpCode,			
			@RequestParam(value="entpManSeq"	, required=true) String entpManSeq,
			@RequestParam(value="searchTermGb"	, required=false) String searchTermGb) throws Exception{
		
		ParamMap paramMap 		= new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCommonRest();
		String apiCode 			= "IF_PAGMKTAPI_V2_00_012";           
		String duplicateCheck 	= "";
		String response 		= "";

		paramMap.put("startDate"	, systemService.getSysdatetimeToString());
		paramMap.put("siteGb"		, "PAG");
		paramMap.put("paCode"		, "21");//EBAY 묶음배송 등록은 계정하나로 통합되어있다.
		paramMap.put("entpCode"		, entpCode);
		paramMap.put("entpManSeq"	, entpManSeq);
		
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			if(!"1".equals(searchTermGb)){
				duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			}
			//= 묶음 배송비 연동 데이터 조회
			List<HashMap<String,String>> shipcostList = paGmktCommonService.selectPaGmktShipCostDtTargetList(paramMap);
			for(Map<String,String> requestMap : shipcostList) {
				
				//= Step 1) 묶음 배송비 등록/ 수정결정  ==> API 종류 선택  - IF_PAGMKTAPI_V2_00_011 : 등록 , IF_PAGMKTAPI_V2_00_012 : 수정 
				String bundleNo = requestMap.get("BUNDLE_NO");
				if(bundleNo == null || "".equals(bundleNo)) {
					paramMap.put("apiCode"		, "IF_PAGMKTAPI_V2_00_011");		
				}else if(bundleNo.equals("중단") || bundleNo.equals("미사용")) {
					paGmktCommonService.savePaShipCostTx(requestMap); //UPDATE TPACUSTSHIPCOST.TRANS_TARGET_YN = 0 & TPAGMKTSHIPCOSTDT.TRANS_TARGET = 0
					continue;
				}else {
					paramMap.put("apiCode"		, "IF_PAGMKTAPI_V2_00_012");
					paramMap.put("urlParameter"	, bundleNo);
				}
				paramMap.put("map", requestMap);
				
				//= Step 2) 제약조건으로 인한 연동 제외 관리 : 유료배송비 기준 100원 미만 배송비 연동하지 않음
				if(Integer.parseInt(String.valueOf(requestMap.get("ORD_COST_AMT"))) < 100 
						&& (requestMap.get("SHIP_COST_CODE").substring(0, 2).equals("CN") || requestMap.get("SHIP_COST_CODE").substring(0, 2).equals("PL") || requestMap.get("SHIP_COST_CODE").substring(0, 2).equals("ID"))){
					paGmktCommonService.savePaShipCostTx(requestMap); //UPDATE TPACUSTSHIPCOST.TRANS_TARGET_YN = 0 & TPAGMKTSHIPCOSTDT.TRANS_TARGET = 0
					continue;
				}
								
				//= Step 3) 통신
				try {
					response = restUtil.getConnection(rest,  paramMap);	
					Map<String,Object> resMap = ComUtil.splitJson(response);
					requestMap.put("placeNo"	, String.valueOf(resMap.get("placeNo")));
					requestMap.put("policyNo"	, String.valueOf(resMap.get("policyNo")));
					
				}catch (Exception e) {
					requestMap.put("remark1V", "ERROR" + apiCode +  e.toString() );
				}		
				
				//= Step 4) UPDATE TPAGMKTSHIPCOSTDT.TRANS_TARGET = 0 &  TPAGMKTSHIPCOSTDT.BUNDLE_NO , TPACUSTSHIPCOST.TRANS_TARGET_YN = 0
				paGmktCommonService.savePaShipCostTx(requestMap);
			}
			
			paramMap.put("code"		, "200");
			paramMap.put("message"	, "OK");

		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
		}finally {
			CommonUtil.dealSuccess(paramMap, request);
			if(!"1".equals(searchTermGb)){
				if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));	
			}
			log.info("===== 묶음배송 수정 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
			
	private void saveTPaGmktShipCostdt(HashMap<String,String> custshipcost) {
		try {
			paGmktCommonService.saveTPaGmktShipCostdtTx(custshipcost);	
		}catch (Exception e) {
			log.info(e.toString());
		}
	}

}

