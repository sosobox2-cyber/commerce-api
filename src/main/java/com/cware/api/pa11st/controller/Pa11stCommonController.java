package com.cware.api.pa11st.controller;
/**
* Common Controller
* 
* company        author   date               Description
* commerceware   shnam    2018.04.16        SK스토아에 맞게 수정
* commerceware   shnam    2018.04.18        브랜드 엑셀 파일 조회
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.xml.sax.InputSource;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.common.util.StringUtil;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.common.util.PostUtil;
import com.cware.netshopping.domain.Pa11stnodelyareamVO;
import com.cware.netshopping.domain.model.Pa11stOrigin;
import com.cware.netshopping.domain.model.Pa11stSettlement;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.pa11st.common.service.Pa11stCommonService;
import com.cware.netshopping.pa11st.goods.service.Pa11stGoodsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pa11st/common", description="공통")
@Controller("com.cware.api.pa11st.Pa11stCommonController")
@RequestMapping(value="/pa11st/common")
public class Pa11stCommonController extends AbstractController {

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pa11st.common.pa11stCommonService")
	private Pa11stCommonService pa11stCommonService;
	
	@Resource(name = "com.cware.netshopping.common.util.PostUtil")
	private PostUtil postUtil;
	
	@Resource(name = "pa11st.goods.pa11stGoodsService")
	private Pa11stGoodsService pa11stGoodsService;
	/**
	 * 전체카테고리조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "전체카테고리조회", notes = "전체카테고리조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goodskinds-org-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsKindsOrgList(HttpServletRequest request) throws Exception{ 
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn = null;
		String rtnMsg = Constants.SAVE_SUCCESS;

		List<PaGoodsKinds> paGoodsKindsList = new ArrayList<PaGoodsKinds>(); 
		Document doc = null;
		NodeList descNodes = null;
		String dateTime = "";
		String duplicateCheck = "";
		
		log.info("===== 전체카테고리조회 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_00_001";
		String request_type = "GET";
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
		
			log.info("03.카테고리 조회 API 호출");
			conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"");
			
			// RESPONSE XML 			
			doc = ComUtil.parseXML(conn.getInputStream());
		    descNodes = doc.getElementsByTagName("ns2:categorys");
		
			conn.disconnect();
		
			List<ParamMap> responseList = new ArrayList<ParamMap>();
			for(int j=0; j<descNodes.getLength();j++){
		        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
		        	if(node.getNodeName().trim().equals("ns2:category")){
		        		ParamMap pa11stParamMap = new ParamMap();
	        			for(int i=0; i<node.getChildNodes().getLength(); i++){
	            			Node directionList = node.getChildNodes().item(i);
	            			pa11stParamMap.put(directionList.getNodeName().trim(), directionList.getTextContent().trim());
	        			}
	        			responseList.add(pa11stParamMap);
		        	}else{
		        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
		        	}
		        }
		    }
			
			String lgroup = "";
			String lgroupName = "";
			String mgroup = "";
			String mgroupName = "";
			String sgroup = "";
			String sgroupName = "";
			String dgroup = "";
			String dgroupName = "";
			for(int k=0; k<responseList.size(); k++){

				if(responseList.get(k).getString("depth").equals("1")){
					lgroup = "";
					lgroupName = "";
					lgroup = responseList.get(k).getString("dispNo");
					lgroupName = responseList.get(k).getString("dispNm");
				} else if(responseList.get(k).getString("depth").equals("2")){
					mgroup = "";
					mgroupName = "";
					mgroup = responseList.get(k).getString("dispNo");
					mgroupName = responseList.get(k).getString("dispNm");
				} else if(responseList.get(k).getString("depth").equals("3")){
					sgroup = "";
					sgroupName = "";
					sgroup = responseList.get(k).getString("dispNo");
					sgroupName = responseList.get(k).getString("dispNm");
					for(int r=k+1; r<responseList.size(); r++){
						
						if(responseList.get(r).getString("depth").equals("3")){
							if(responseList.get(r).getString("parentDispNo").equals(mgroup) 
									&& !(responseList.get(r-1).getString("depth").equals("4"))){
								PaGoodsKinds paGoodsKinds = new PaGoodsKinds();
								paGoodsKinds.setPaGroupCode("01");
								paGoodsKinds.setPaLgroup(lgroup);
								paGoodsKinds.setPaLgroupName(lgroupName);
								paGoodsKinds.setPaMgroup(mgroup);
								paGoodsKinds.setPaMgroupName(mgroupName);
								paGoodsKinds.setPaSgroup(sgroup);
								paGoodsKinds.setPaSgroupName(sgroupName);
								paGoodsKinds.setPaDgroup(dgroup);
								paGoodsKinds.setPaDgroupName(dgroupName);
								paGoodsKinds.setInsertId("PA11");
								paGoodsKinds.setModifyId("PA11");
								paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
								
								paGoodsKindsList.add(paGoodsKinds);
							}
							sgroup = "";
							sgroupName = "";
							sgroup = responseList.get(r).getString("dispNo");
							sgroupName = responseList.get(r).getString("dispNm");
						} else if(responseList.get(r).getString("depth").equals("4")){
							
							
							dgroup = responseList.get(r).getString("dispNo");
							dgroupName = responseList.get(r).getString("dispNm");
							
							PaGoodsKinds paGoodsKinds = new PaGoodsKinds();
							paGoodsKinds.setPaGroupCode("01");
							paGoodsKinds.setPaLgroup(lgroup);
							paGoodsKinds.setPaLgroupName(lgroupName);
							paGoodsKinds.setPaMgroup(mgroup);
							paGoodsKinds.setPaMgroupName(mgroupName);
							paGoodsKinds.setPaSgroup(sgroup);
							paGoodsKinds.setPaSgroupName(sgroupName);
							paGoodsKinds.setPaDgroup(dgroup);
							paGoodsKinds.setPaDgroupName(dgroupName);
							paGoodsKinds.setInsertId("PA11");
							paGoodsKinds.setModifyId("PA11");
							paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
							
							paGoodsKindsList.add(paGoodsKinds);
							dgroup = "";
							dgroupName = "";
						} else {
							PaGoodsKinds paGoodsKinds = new PaGoodsKinds();
							paGoodsKinds.setPaGroupCode("01");
							paGoodsKinds.setPaLgroup(lgroup);
							paGoodsKinds.setPaLgroupName(lgroupName);
							paGoodsKinds.setPaMgroup(mgroup);
							paGoodsKinds.setPaMgroupName(mgroupName);
							paGoodsKinds.setPaSgroup(sgroup);
							paGoodsKinds.setPaSgroupName(sgroupName);
							paGoodsKinds.setPaDgroup(dgroup);
							paGoodsKinds.setPaDgroupName(dgroupName);
							paGoodsKinds.setInsertId("PA11");
							paGoodsKinds.setModifyId("PA11");
							paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
							
							paGoodsKindsList.add(paGoodsKinds);
							k = r-1;
							break;
						}
						if(r==responseList.size()-1){
							k = r;
							PaGoodsKinds paGoodsKinds = new PaGoodsKinds();
							paGoodsKinds.setPaGroupCode("01");
							paGoodsKinds.setPaLgroup(lgroup);
							paGoodsKinds.setPaLgroupName(lgroupName);
							paGoodsKinds.setPaMgroup(mgroup);
							paGoodsKinds.setPaMgroupName(mgroupName);
							paGoodsKinds.setPaSgroup(sgroup);
							paGoodsKinds.setPaSgroupName(sgroupName);
							paGoodsKinds.setPaDgroup(dgroup);
							paGoodsKinds.setPaDgroupName(dgroupName);
							paGoodsKinds.setInsertId("PA11");
							paGoodsKinds.setModifyId("PA11");
							paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
							
							paGoodsKindsList.add(paGoodsKinds);
						}
					}
				}
			}

			log.info("04.전체카테고리 저장");
			rtnMsg = pa11stCommonService.savePa11stGoodsKindsTx(paGoodsKindsList);
			if(!rtnMsg.equals("000000")){
				paramMap.put("code","404");
				paramMap.put("message",rtnMsg);
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			} else {
				paramMap.put("code","200");
				paramMap.put("message","OK");
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
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 카테고리 엑셀 조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "카테고리 엑셀 조회", notes = "카테고리 엑셀 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goodskinds-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsKindsList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
	        
        String dateTime = "";
        String duplicateCheck = "";
        
        List<PaGoodsKinds> paGoodsKindsList = new ArrayList<PaGoodsKinds>();
        PaGoodsKinds paGoodsKinds = null;

        try{
            log.info("===== 카테고리 엑셀 조회 API Start=====");

            //log.info("01.API 기본정보 세팅");
    		dateTime = systemService.getSysdatetimeToString();
    		
            paramMap.put("apiCode", "IF_PA11STAPI_00_001");
            paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
            paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
            paramMap.put("startDate", dateTime);
            
            //log.info("02.API 중복실행검사");
            //= 중복 실행 Check
		    duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
		    if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
	            
		    log.info("03.카테고리 엑셀파일 조회");

			String fileName ="Category_list.xlsx";
			String fileDirPath = new HttpServletRequestWrapper(request).getRealPath("/uploadfile");
			File file = new File(fileDirPath+"/"+fileName);

			if (!file.exists()) {
				paramMap.put("ERROR_MESSAGE", getMessage("msg.upload_failure"));
			}
			
			XSSFWorkbook work = new XSSFWorkbook(new FileInputStream(file));
			
			XSSFRow row = null;
			XSSFSheet sheet = work.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			if (rows > 0) {
				for (int r = 3; r < rows; r++) {
					row = sheet.getRow(r);
					
					if (row == null) {
						paramMap.put("code","404");
						paramMap.put("message",getMessage("errors.no.select"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else if ((int)row.getCell(10).getNumericCellValue() != 0){
						paGoodsKinds = new PaGoodsKinds();
						paGoodsKinds.setPaLgroup(Integer.toString((int)row.getCell(2).getNumericCellValue()));
						paGoodsKinds.setPaLgroupName(row.getCell(3).getStringCellValue());
						paGoodsKinds.setPaMgroup(Integer.toString((int)row.getCell(4).getNumericCellValue()));
						paGoodsKinds.setPaMgroupName(row.getCell(5).getStringCellValue());
						
						if(!row.getCell(7).getStringCellValue().isEmpty()){
							paGoodsKinds.setPaSgroup(Integer.toString((int)row.getCell(6).getNumericCellValue()));
							paGoodsKinds.setPaSgroupName(row.getCell(7).getStringCellValue());
							if(!row.getCell(9).getStringCellValue().isEmpty()){
								paGoodsKinds.setPaDgroup(Integer.toString((int)row.getCell(8).getNumericCellValue()));
								paGoodsKinds.setPaDgroupName(row.getCell(9).getStringCellValue());
							}
						}
						paGoodsKinds.setPaLmsdKey(Integer.toString((int)row.getCell(10).getNumericCellValue()));
						paGoodsKinds.setPaGroupCode("01");
						paGoodsKinds.setInsertId("PA11");
						paGoodsKinds.setModifyId("PA11");
						paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
						
						paGoodsKindsList.add(paGoodsKinds);
					}
				}
			}
			
		    log.info("04.카테고리 엑셀 조회 정보 저장");
 			rtnMsg = pa11stCommonService.savePa11stGoodsKindsTx(paGoodsKindsList);
 			certGoodsKindsList(request);
			
			if(!rtnMsg.equals("000000")){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("errors.no.select"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			} else {
				paramMap.put("code","200");
				paramMap.put("message","OK");
			}

        }catch (Exception e) {
			paramMap.put("code","500");
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR );
		}finally{
		    	try{
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}

			//log.info("05.저장 완료 API END");
		}
        
        return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 원산지조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "원산지조회", notes = "원산지조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/origin-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> originList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
	
		Document doc = null;
		NodeList codesNodes = null;
		
		InputStream is = null;
		InputStreamReader isr = null;
		DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        
        String dateTime = "";
        String duplicateCheck = "";
        
        List<Pa11stOrigin> pa11stOriginList = new ArrayList<Pa11stOrigin>();
	
        try{
            log.info("===== 원산지조회 API Start=====");

            //log.info("01.API 기본정보 세팅");
    		dateTime = systemService.getSysdatetimeToString();
    		
            paramMap.put("apiCode", "IF_PA11STAPI_00_002");
            paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
            paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
            paramMap.put("startDate", dateTime);
            
            //log.info("02.API 중복실행검사");
            //= 중복 실행 Check
		    duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
		    if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
	            
		    //log.info("03.API정보 조회");
            
		    apiInfo = systemService.selectPaApiInfo(paramMap);
	        
            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            
            // XXE 취약점 예방 소스 추가
            objDocumentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            objDocumentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            objDocumentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            objDocumentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            objDocumentBuilderFactory.setXIncludeAware(false);
            objDocumentBuilderFactory.setExpandEntityReferences(false);
            
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();

            //log.info("04.원산지 정보 조회");
            is = new URL(ConfigUtil.getString("PA11ST_COM_XML_SCHEMA_URL") + apiInfo.get("API_URL")).openStream();
            isr = new InputStreamReader(is, "euc-kr");
            doc = (Document) objDocumentBuilder.parse(new InputSource(isr));         
            
            //log.info("05.원산지 정보 셋팅");
            
            // RESPONSE XML 			
 		    codesNodes = doc.getElementsByTagName("Codes");
 		
 			List<ParamMap> responseList = new ArrayList<ParamMap>();
 			for(int j=0; j<codesNodes.getLength();j++){
 		        for(Node node = codesNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
 		        	if(node.getNodeName().trim().equals("Code")){
 		        		ParamMap pa11stParamMap = new ParamMap();
 	        			for(int i=0; i<node.getChildNodes().getLength(); i++){
 	            			Node directionList = node.getChildNodes().item(i);
 	            			pa11stParamMap.put(directionList.getNodeName().trim(), directionList.getTextContent().trim());
 	        			}
 	        			responseList.add(pa11stParamMap);
 		        	}else{
 		        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
 		        	}
 		        }
 		    }
 			
 			for(int i=0; i<responseList.size(); i++){
 				Pa11stOrigin pa11stOrigin = new Pa11stOrigin();
 				if(responseList.get(i).getString("County").equals("국내")){
 					pa11stOrigin.setOrgnTypCd("01");
 				} else if(responseList.get(i).getString("County").equals("해외")){
 					pa11stOrigin.setOrgnTypCd("02");
 				} else {
 					pa11stOrigin.setOrgnTypCd("03");
 				}
 				pa11stOrigin.setOrgnTypDtlsCd(responseList.get(i).getString("Value"));
 				pa11stOrigin.setAreaName(responseList.get(i).getString("Area"));
 				pa11stOrigin.setDetailAreaName(responseList.get(i).getString("Name"));
 				pa11stOrigin.setInsertId("PA11");
 				pa11stOrigin.setModifyId("PA11");
 				pa11stOrigin.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
 				pa11stOrigin.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
 				pa11stOriginList.add(pa11stOrigin);
 			}
 			
 			rtnMsg = pa11stCommonService.saveMappingPa11stOriginTx(pa11stOriginList);
			
			if(!rtnMsg.equals("000000")){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("errors.no.select"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			} else {
				paramMap.put("code","200");
				paramMap.put("message","OK");
			}

        }catch (Exception e) {
			paramMap.put("code","500");
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR );
		}finally{
	    	try{
	    		is.close();
	            isr.close();
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}

			//log.info("07.저장 완료 API END");
		}
        
        return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);

	}
	

	/**
	 * 출고지등록
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "출고지등록", notes = "출고지등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/entpslip-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpSlipInsert(HttpServletRequest request,
			@ApiParam(name = "entpCode", value = "업체코드", defaultValue = "") @RequestParam(value="entpCode"		, required=true) String entpCode,			
			@ApiParam(name = "entpManSeq", value = "업체순번", defaultValue = "") @RequestParam(value="entpManSeq"	, required=true) String entpManSeq,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode"		, required=true) String paCode,
			@ApiParam(name = "searchTermGb", value = "중복실행검사여부('1':진행)", defaultValue = "") @RequestParam(value="searchTermGb"	, required=false , defaultValue = "") String searchTermGb
			) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Document doc = null;
		NodeList descNodes = null;
		String dateTime = "";
		String duplicateCheck = "";
		OutputStreamWriter out = null;
		StringBuilder reqXml = null;
		int respCode = 0;
		String respMsg = null;
		PaEntpSlip paEntpSlip = new PaEntpSlip();
		
		if(entpCode.equals("") || entpCode == null) {
			paramMap.put("code","404");
			paramMap.put("message",getMessage("pa.check_entp_code"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}
		
		log.info("===== 출고지등록 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_00_003";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			
			if(!searchTermGb.equals("1")) {
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			}
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb("30");//출고
			paEntpSlip.setPaCode(paCode);
			
			String buildMnNo = ""; //건물관리번호
			String dtlsAddr = ""; //상세주소
			String gnrlTlphnNo  = ""; //일반전화번호
			String prtblTlphnNo  = ""; //휴대전화번호
			String rcvrNm = ""; //이름
			String addrNm = ""; //주소명
			
			log.info("03.출고지 주소지,건물관리번호 조회"+"entpCode"+paEntpSlip.getEntpCode()+"/entpManSeq"+paEntpSlip.getEntpManSeq()+"/paCode:"+paEntpSlip.getPaCode());
			HashMap<String,String> entpUserMap = new HashMap<String,String>();
		    entpUserMap = pa11stCommonService.selectEntpShipInsertList(paEntpSlip);
		    
		    if(entpUserMap==null){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("partner.no_change_data"));
		    } else {
		    	Map<String, Object> post = new HashMap<String, Object>();
				ArrayList<HashMap<String, Object>> paramList = new ArrayList<>();
				HashMap<String,Object> hm = new HashMap<>();
				hm.put("POST_NO", entpUserMap.get("POST_NO").toString());
				hm.put("ROAD_ADDR_YN", entpUserMap.get("ROAD_ADDR_YN").toString());
				if (entpUserMap.get("ROAD_ADDR_YN").equals("1")) {
					hm.put("SEARCH_ADDR", entpUserMap.get("ROAD_POST_ADDR").toString());
					hm.put("SEARCH_ADDR_INFO","");
					hm.put("SEARCH_ADDR2", entpUserMap.get("ROAD_ADDR").toString());
				} else if (entpUserMap.get("ROAD_ADDR_YN").equals("0")) {
					hm.put("SEARCH_ADDR", entpUserMap.get("POST_ADDR").toString());
					hm.put("SEARCH_ADDR_INFO","");
					hm.put("SEARCH_ADDR2", entpUserMap.get("ADDR").toString());
				}
				
				paramList = postUtil.checkLocalYn(paramList, request, hm); //로컬 체크
				try{
					post = postUtil.retrieveVerifyPost(paramList);
					//log.info("post정제결과,오류데이터 검증:::  "+post);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				/*log.info("주소정제 실패 유무 Flag 찾기..");
				String flg = post.get("FLAG").toString();
				log.info("flg" + flg);   	
				
				if ("H".equals(flg) || "I".equals(flg)){
					log.info("주소정제 성공");
				}else{
					log.info("주소정제 실패");
				}//end of FLG
				 */
			    
				ArrayList<HashMap<String, Object>> b = (ArrayList<HashMap<String, Object>>) post.get("RESULT");
				Map<String, Object> result = (Map<String, Object>) b.get(0);
				gnrlTlphnNo  = entpUserMap.get("ENTP_MAN_TEL").toString(); //일반전화번호
				prtblTlphnNo  = entpUserMap.get("ENTP_MAN_HP").toString(); //휴대전화번호
				rcvrNm = entpUserMap.get("ENTP_MAN_NAME").toString(); //이름
				addrNm = entpUserMap.get("ENTP_CODE").toString()+"-"+entpUserMap.get("ENTP_MAN_SEQ").toString(); //주소명
				buildMnNo = result.get("NNMB").toString();
				dtlsAddr = result.get("NADR1S").toString() +" "+result.get("NADR2S").toString();
				
				if(buildMnNo.equals("") || dtlsAddr.equals("")){
					paramMap.put("code","404");
					paramMap.put("message",getMessage("pa.address_fail"));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} else {
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}
				
				//log.info("주소 정제결과:"+gnrlTlphnNo+" "+prtblTlphnNo+" "+rcvrNm+" "+addrNm+" "+buildMnNo+" "+dtlsAddr);
				if(paEntpSlip.getPaCode().equals(Constants.PA_11ST_BROAD_CODE)){
					conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"");
				} else {
					conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"");
				}
				
				// REQUEST XML 생성.
				reqXml = new StringBuilder();
				reqXml.append("<InOutAddress>");
				reqXml.append("<addrNm>"+addrNm+"</addrNm>");
				reqXml.append("<rcvrNm>"+rcvrNm+"</rcvrNm>");
				reqXml.append("<gnrlTlphnNo>"+gnrlTlphnNo+"</gnrlTlphnNo>");
				reqXml.append("<prtblTlphnNo>"+prtblTlphnNo+"</prtblTlphnNo>");
				reqXml.append("<mailNO>"+""+"</mailNO>");
				reqXml.append("<ordpeNm>"+""+"</ordpeNm>");
				reqXml.append("<buildMngNO>"+buildMnNo+"</buildMngNO>");
				reqXml.append("<dtlsAddr><![CDATA["+dtlsAddr+"]]></dtlsAddr>");
				reqXml.append("<baseAddrYN>"+""+"</baseAddrYN>");
				reqXml.append("</InOutAddress>");
				
				out = new OutputStreamWriter(conn.getOutputStream());
				out.write(String.valueOf(reqXml));
				out.flush();
				
				//log.info("전문::"+reqXml);
				respCode = conn.getResponseCode();
				respMsg  = conn.getResponseMessage();
				//log.info(" connect respCode : "+respCode);
				//log.info(" connect respMsg  : "+respMsg);
				
				if(respCode == 200){
					// RESPONSE XML 			
					doc = ComUtil.parseXML(conn.getInputStream());
				    descNodes = doc.getElementsByTagName("ns2:inOutAddresss");
					conn.disconnect();
				
	        		ParamMap pa11stParamMap = new ParamMap();
					for(int j=0; j<descNodes.getLength();j++){
				        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
				        	if(node.getNodeName().trim().equals("ns2:inOutAddress")){
			        			for(int i=0; i<node.getChildNodes().getLength(); i++){
			            			Node directionList = node.getChildNodes().item(i);
			            			pa11stParamMap.put(directionList.getNodeName().trim(), directionList.getTextContent().trim());
			        			}
				        	}else{
				        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
				        	}
				        }
				    }
					log.info("insert result message : "+paramMap.getString("ns2:result_message"));
			
					if(pa11stParamMap.getString("addrSeq").equals("") || pa11stParamMap.getString("addrSeq").equals(null)){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("partner.no_change_data"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else {
						paEntpSlip.setPaAddrSeq(pa11stParamMap.getString("addrSeq"));
						paEntpSlip.setTransTargetYn("0");
						paEntpSlip.setInsertId("PA11");
						paEntpSlip.setModifyId("PA11");
						paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						//log.info("05.출고지 저장");
						rtnMsg = pa11stCommonService.savePa11stEntpSlipTx(paEntpSlip);
						
						if(!rtnMsg.equals("000000")){
							paramMap.put("code","404");
							paramMap.put("message",rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paramMap.put("code","200");
							paramMap.put("message","OK");
						}
					}
				} else {
					paramMap.put("code","500");
					paramMap.put("message",respMsg);
					
					//전송관리 테이블 저장
					PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(entpUserMap.get("ENTP_CODE").toString());
					paGoodsTransLog.setPaCode(paCode);
					paGoodsTransLog.setItemNo(entpUserMap.get("ENTP_MAN_SEQ").toString());
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn(paramMap.getString("code").equals("200")==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId("PA11");
					pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
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
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			
			if(!searchTermGb.equals("1")) { //GOODS_INSERT에서는 체크하지 않음
				if(duplicateCheck.equals("0")){
					systemService.checkCloseHistoryTx("end", prg_id);
				}	
			}
			//log.info("05.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	
	/**
	 * 출고지수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "출고지수정", notes = "출고지수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/entpslip-update", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<?> entpSlipUpdate(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
				
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = "";
		String duplicateCheck = "";
		HashMap<String, String> entpUserMap = null;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		OutputStreamWriter out = null;
		StringBuilder reqXml = null;
		
		int respCode = 0;
		String respMsg = null;
		PaEntpSlip paEntpSlip = new PaEntpSlip();
		String buildMnNo = ""; //건물관리번호
		String dtlsAddr = ""; //상세주소
		String gnrlTlphnNo  = ""; //일반전화번호
		String prtblTlphnNo  = ""; //휴대전화번호
		String rcvrNm = ""; //이름
		String addrNm = ""; //주소명
		String addrSeq = ""; //11번가seq
		
		log.info("===== 출고지수정 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_00_004";
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
			
			List<Object> entpShipUpdateList = pa11stCommonService.selectEntpShipUpdateList("30");
			
			if(entpShipUpdateList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("partner.no_change_data"));
			} else {
				for(int _i=0; _i<entpShipUpdateList.size(); _i++){
					
					buildMnNo = ""; //건물관리번호
					dtlsAddr = ""; //상세주소
					gnrlTlphnNo  = ""; //일반전화번호
					prtblTlphnNo  = ""; //휴대전화번호
					rcvrNm = ""; //이름
					addrNm = ""; //주소명
					
					log.info("03.출고지 주소지,건물관리번호 수정");
				    entpUserMap = (HashMap<String, String>) entpShipUpdateList.get(_i);
				    Map<String, Object> post = new HashMap<String, Object>();
					ArrayList<HashMap<String, Object>> paramList = new ArrayList<>();
					HashMap<String,Object> hm = new HashMap<>();
					hm.put("POST_NO", entpUserMap.get("POST_NO").toString());
					hm.put("ROAD_ADDR_YN", entpUserMap.get("ROAD_ADDR_YN").toString());
					if (entpUserMap.get("ROAD_ADDR_YN").equals("1")) {
						hm.put("SEARCH_ADDR", entpUserMap.get("ROAD_POST_ADDR").toString());
						hm.put("SEARCH_ADDR_INFO","");
						hm.put("SEARCH_ADDR2", entpUserMap.get("ROAD_ADDR").toString());
					} else if (entpUserMap.get("ROAD_ADDR_YN").equals("0")) {
						hm.put("SEARCH_ADDR", entpUserMap.get("POST_ADDR").toString());
						hm.put("SEARCH_ADDR_INFO","");
						hm.put("SEARCH_ADDR2", entpUserMap.get("ADDR").toString());
					}
					
					paramList = postUtil.checkLocalYn(paramList, request, hm); //로컬 체크
					
					try{
						post = postUtil.retrieveVerifyPost(paramList);
					}catch(Exception e){
						e.printStackTrace();
					}
				    
					ArrayList<HashMap<String, Object>> b = (ArrayList<HashMap<String, Object>>) post.get("RESULT");
					Map<String, Object> result = (Map<String, Object>) b.get(0);
					
					gnrlTlphnNo  = entpUserMap.get("ENTP_MAN_TEL").toString(); //일반전화번호
					prtblTlphnNo  = entpUserMap.get("ENTP_MAN_HP").toString(); //휴대전화번호
					rcvrNm = entpUserMap.get("ENTP_MAN_NAME").toString(); //이름
					addrNm = entpUserMap.get("ENTP_CODE").toString()+"-"+entpUserMap.get("ENTP_MAN_SEQ").toString(); //주소명
					addrSeq = entpUserMap.get("PA_ADDR_SEQ").toString();
					buildMnNo = result.get("NNMB").toString();
					dtlsAddr = result.get("NADR1S").toString()+" " + result.get("NADR2S").toString();
					
					if(buildMnNo.equals("") || dtlsAddr.equals("")){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("pa.address_fail"));
						//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						
						//전송관리 테이블 저장
						PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
						paGoodsTransLog.setGoodsCode(entpUserMap.get("ENTP_CODE").toString());
						paGoodsTransLog.setPaCode(entpUserMap.get("PA_CODE").toString());
						paGoodsTransLog.setItemNo(entpUserMap.get("ENTP_MAN_SEQ").toString());
						paGoodsTransLog.setRtnCode(paramMap.getString("code"));
						paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
						paGoodsTransLog.setSuccessYn(paramMap.getString("code").equals("200")==true?"1":"0");
						paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paGoodsTransLog.setProcId("PA11");
						pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
						
					} else {
						paramMap.put("code","200");
						paramMap.put("message","OK");
					}
				
					//log.info("주소 정제결과:"+gnrlTlphnNo+" "+prtblTlphnNo+" "+rcvrNm+" "+addrNm+" "+buildMnNo+" "+dtlsAddr);
					
					//log.info("04.출고지 수정 API 호출");
					
					if(entpUserMap.get("PA_CODE").equals(Constants.PA_11ST_BROAD_CODE)){//방송
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"");
						paEntpSlip.setPaCode(Constants.PA_11ST_BROAD_CODE);
					} else {//온라인
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"");
						paEntpSlip.setPaCode(Constants.PA_11ST_ONLINE_CODE);
					}
					
					// REQUEST XML 생성.
					reqXml = new StringBuilder();
					reqXml.append("<InOutAddress>");
					reqXml.append("<addrNm>"+addrNm+"</addrNm>");
					reqXml.append("<rcvrNm>"+rcvrNm+"</rcvrNm>");
					reqXml.append("<gnrlTlphnNo>"+gnrlTlphnNo+"</gnrlTlphnNo>");
					reqXml.append("<prtblTlphnNo>"+prtblTlphnNo+"</prtblTlphnNo>");
					reqXml.append("<mailNO>"+""+"</mailNO>");
					reqXml.append("<ordpeNm>"+""+"</ordpeNm>");
					reqXml.append("<buildMngNO>"+buildMnNo+"</buildMngNO>");
					reqXml.append("<dtlsAddr><![CDATA["+dtlsAddr+"]]></dtlsAddr>");
					reqXml.append("<addrSeq >"+addrSeq+"</addrSeq >");
					reqXml.append("</InOutAddress>");
					
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
					    descNodes = doc.getElementsByTagName("ns2:inOutAddresss");
						conn.disconnect();
					
		        		ParamMap pa11stParamMap = new ParamMap();
						for(int j=0; j<descNodes.getLength();j++){
					        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
					        	if(node.getNodeName().trim().equals("ns2:inOutAddress")){
				        			for(int i=0; i<node.getChildNodes().getLength(); i++){
				            			Node directionList = node.getChildNodes().item(i);
				            			pa11stParamMap.put(directionList.getNodeName().trim(), directionList.getTextContent().trim());
				        			}
					        	}else{
					        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
					        	}
					        }
					    }
						log.info("insert result message : "+paramMap.getString("ns2:result_message"));
						
						if(pa11stParamMap.getString("addrSeq").equals("") || pa11stParamMap.getString("addrSeq").equals(null)){
							paramMap.put("code","404");
							paramMap.put("message",getMessage("partner.no_change_data"));
						} else {
							paEntpSlip.setEntpCode(entpUserMap.get("ENTP_CODE"));
							paEntpSlip.setEntpManSeq(entpUserMap.get("ENTP_MAN_SEQ"));
							paEntpSlip.setPaAddrGb(entpUserMap.get("ENTP_MAN_GB"));
							paEntpSlip.setPaAddrSeq(pa11stParamMap.getString("addrSeq"));
							paEntpSlip.setTransTargetYn("0");
							paEntpSlip.setModifyId("PA11");
							paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							//log.info("05.출고지 저장");
							rtnMsg = pa11stCommonService.savePa11stEntpSlipUpdateTx(paEntpSlip);
							
							if(!rtnMsg.equals("000000")){
								paramMap.put("code","404");
								paramMap.put("message",rtnMsg);
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							} else {
								paramMap.put("code","200");
								paramMap.put("message","OK");
							}
						}
						
					}else {
						paramMap.put("code","500");
						paramMap.put("message",respMsg);
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
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 반품/교환처 등록
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "반품/교환처 등록", notes = "반품/교환처 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/returnslip-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnSlipInsert(HttpServletRequest request,
			@ApiParam(name = "entpCode", value = "업체코드", defaultValue = "") @RequestParam(value="entpCode"		, required=true) String entpCode,			
			@ApiParam(name = "entpManSeq", value = "업체순번", defaultValue = "") @RequestParam(value="entpManSeq"	, required=true) String entpManSeq,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode"		, required=true) String paCode,
			@ApiParam(name = "searchTermGb", value = "중복실행검사여부('1':진행)", defaultValue = "") @RequestParam(value="searchTermGb"	, required=false , defaultValue = "") String searchTermGb
			) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Document doc = null;
		NodeList descNodes = null;
		String dateTime = "";
		String duplicateCheck = "";
		OutputStreamWriter out = null;
		StringBuilder reqXml = null;
		int respCode = 0;
		String respMsg = null;
		PaEntpSlip paEntpSlip = new PaEntpSlip();
		
		if(entpCode.equals("") || entpCode == null) {
			paramMap.put("code","404");
			paramMap.put("message",getMessage("pa.check_entp_code"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}
		
		log.info("===== 반품/교환처등록 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_00_005";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			if(!searchTermGb.equals("1")) {
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			}
			
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb("20");//회수
			paEntpSlip.setPaCode(paCode);
			
			String buildMnNo = ""; //건물관리번호
			String dtlsAddr = ""; //상세주소
			String gnrlTlphnNo  = ""; //일반전화번호
			String prtblTlphnNo  = ""; //휴대전화번호
			String rcvrNm = ""; //이름
			String addrNm = ""; //주소명
			
			log.info("03.회수지 주소지,건물관리번호 조회"+"entpCode"+paEntpSlip.getEntpCode()+"/entpManSeq"+paEntpSlip.getEntpManSeq()+"/paCode:"+paEntpSlip.getPaCode());
			//log.info("entpCode"+paEntpSlip.getEntpCode()+"/entpManSeq"+paEntpSlip.getEntpManSeq()+"/paCode:"+paEntpSlip.getPaCode());
			Map<String,String> entpUserMap = new HashMap<String,String>();
		    entpUserMap = pa11stCommonService.selectEntpShipInsertList(paEntpSlip);
		    
		    if(entpUserMap==null){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("partner.no_change_data"));
		    } else {
		    	Map<String, Object> post = new HashMap<String, Object>();
				ArrayList<HashMap<String, Object>> paramList = new ArrayList<>();
				HashMap<String,Object> hm = new HashMap<>();
				hm.put("POST_NO", entpUserMap.get("POST_NO").toString());
				hm.put("ROAD_ADDR_YN", entpUserMap.get("ROAD_ADDR_YN").toString());
				if (entpUserMap.get("ROAD_ADDR_YN").equals("1")) {
					hm.put("SEARCH_ADDR", entpUserMap.get("ROAD_POST_ADDR").toString());
					hm.put("SEARCH_ADDR_INFO","");
					hm.put("SEARCH_ADDR2", entpUserMap.get("ROAD_ADDR").toString());
				} else if (entpUserMap.get("ROAD_ADDR_YN").equals("0")) {
					hm.put("SEARCH_ADDR", entpUserMap.get("POST_ADDR").toString());
					hm.put("SEARCH_ADDR_INFO","");
					hm.put("SEARCH_ADDR2", entpUserMap.get("ADDR").toString());
				}
				
				paramList = postUtil.checkLocalYn(paramList, request, hm); //로컬 체크
				
				try{
					post = postUtil.retrieveVerifyPost(paramList);
				}catch(Exception e){
					e.printStackTrace();
				}
			    
				ArrayList<HashMap<String, Object>> b = (ArrayList<HashMap<String, Object>>) post.get("RESULT");
				Map<String, Object> result = (Map<String, Object>) b.get(0);
				gnrlTlphnNo  = entpUserMap.get("ENTP_MAN_TEL").toString(); //일반전화번호
				prtblTlphnNo  = entpUserMap.get("ENTP_MAN_HP").toString(); //휴대전화번호
				rcvrNm = entpUserMap.get("ENTP_MAN_NAME").toString(); //이름
				addrNm = entpUserMap.get("ENTP_CODE").toString()+"-"+entpUserMap.get("ENTP_MAN_SEQ").toString(); //주소명
				buildMnNo = result.get("NNMB").toString();
				dtlsAddr = result.get("NADR1S").toString() +" "+ result.get("NADR2S").toString();
				
				if(buildMnNo.equals("") || dtlsAddr.equals("")){
					paramMap.put("code","404");
					paramMap.put("message",getMessage("pa.address_fail"));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} else {
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}
				
				log.info("주소 정제결과:"+gnrlTlphnNo+" "+prtblTlphnNo+" "+rcvrNm+" "+addrNm+" "+buildMnNo+" "+dtlsAddr);
//				log.info("04.회수지 등록 API 호출");
				
				if(paEntpSlip.getPaCode().equals(Constants.PA_11ST_BROAD_CODE)){//방송상품
					conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"");
				} else {//온라인상품
					conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"");
				}
				
				// REQUEST XML 생성.
				reqXml = new StringBuilder();
				reqXml.append("<InOutAddress>");
				reqXml.append("<addrNm>"+addrNm+"</addrNm>");
				reqXml.append("<rcvrNm>"+rcvrNm+"</rcvrNm>");
				reqXml.append("<gnrlTlphnNo>"+gnrlTlphnNo+"</gnrlTlphnNo>");
				reqXml.append("<prtblTlphnNo>"+prtblTlphnNo+"</prtblTlphnNo>");
				reqXml.append("<mailNO>"+""+"</mailNO>");
				reqXml.append("<ordpeNm>"+""+"</ordpeNm>");
				reqXml.append("<buildMngNO>"+buildMnNo+"</buildMngNO>");
				reqXml.append("<dtlsAddr><![CDATA["+dtlsAddr+"(※반품시 판매자 지정 택배사 필수, 직접발송불가)]]></dtlsAddr>");
				reqXml.append("<baseAddrYN>"+""+"</baseAddrYN>");
				reqXml.append("</InOutAddress>");
				
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
				    descNodes = doc.getElementsByTagName("ns2:inOutAddresss");
					conn.disconnect();
				
	        		ParamMap pa11stParamMap = new ParamMap();
					for(int j=0; j<descNodes.getLength();j++){
				        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
				        	if(node.getNodeName().trim().equals("ns2:inOutAddress")){
			        			for(int i=0; i<node.getChildNodes().getLength(); i++){
			            			Node directionList = node.getChildNodes().item(i);
			            			pa11stParamMap.put(directionList.getNodeName().trim(), directionList.getTextContent().trim());
			        			}
				        	}else{
				        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
				        	}
				        }
				    }
					log.info("insert result message : "+paramMap.getString("ns2:result_message"));
					
					if(pa11stParamMap.getString("addrSeq").equals("") || pa11stParamMap.getString("addrSeq").equals(null)){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("partner.no_change_data"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else {
						paEntpSlip.setPaAddrSeq(pa11stParamMap.getString("addrSeq"));
						paEntpSlip.setTransTargetYn("0");
						paEntpSlip.setInsertId("PA11");
						paEntpSlip.setModifyId("PA11");
						paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						//log.info("05.반품/교환처 저장");
						rtnMsg = pa11stCommonService.savePa11stEntpSlipTx(paEntpSlip);
						
						if(!rtnMsg.equals("000000")){
							paramMap.put("code","404");
							paramMap.put("message",rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paramMap.put("code","200");
							paramMap.put("message","OK");
						}
					}
				} else {
					paramMap.put("code","500");
					paramMap.put("message",respMsg);
					
					//전송관리 테이블 저장
					PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(entpUserMap.get("ENTP_CODE").toString());
					paGoodsTransLog.setPaCode(paCode);
					paGoodsTransLog.setItemNo(entpUserMap.get("ENTP_MAN_SEQ").toString());
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn(paramMap.getString("code").equals("200")==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId("PA11");
					pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
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
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(!searchTermGb.equals("1")) {
				if(duplicateCheck.equals("0")){
					systemService.checkCloseHistoryTx("end", prg_id);
				}
			}
			//log.info("05.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 반품/교환처 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품/교환처 수정", notes = "반품/교환처 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/returnslip-update", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnShipUpdate(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = "";
		String duplicateCheck = "";
		HashMap<String, String> entpUserMap = null;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		OutputStreamWriter out = null;
		StringBuilder reqXml = null;
		
		int respCode = 0;
		String respMsg = null;
		PaEntpSlip paEntpSlip = new PaEntpSlip();
		String buildMnNo = ""; //건물관리번호
		String dtlsAddr = ""; //상세주소
		String gnrlTlphnNo  = ""; //일반전화번호
		String prtblTlphnNo  = ""; //휴대전화번호
		String rcvrNm = ""; //이름
		String addrNm = ""; //주소명
		String addrSeq = ""; //11번가seq
		
		log.info("===== 반품/교환처 수정 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_00_006";
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
			
			List<Object> entpShipUpdateList = pa11stCommonService.selectEntpShipUpdateList("20");
			
			if(entpShipUpdateList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("partner.no_change_data"));
			} else {
				for(int _i=0; _i<entpShipUpdateList.size(); _i++){
					
					buildMnNo = ""; //건물관리번호
					dtlsAddr = ""; //상세주소
					gnrlTlphnNo  = ""; //일반전화번호
					prtblTlphnNo  = ""; //휴대전화번호
					rcvrNm = ""; //이름
					addrNm = ""; //주소명
					
					log.info("03.반품/교환 주소지,건물관리번호 수정");
				    entpUserMap = (HashMap<String, String>) entpShipUpdateList.get(_i);
				    
				    Map<String, Object> post = new HashMap<String, Object>();
					ArrayList<HashMap<String, Object>> paramList = new ArrayList<>();
					HashMap<String,Object> hm = new HashMap<>();
					hm.put("POST_NO", entpUserMap.get("POST_NO").toString());
					hm.put("ROAD_ADDR_YN", entpUserMap.get("ROAD_ADDR_YN").toString());
					if (entpUserMap.get("ROAD_ADDR_YN").equals("1")) {
						hm.put("SEARCH_ADDR", entpUserMap.get("ROAD_POST_ADDR").toString());
						hm.put("SEARCH_ADDR_INFO","");
						hm.put("SEARCH_ADDR2", entpUserMap.get("ROAD_ADDR").toString());
					} else if (entpUserMap.get("ROAD_ADDR_YN").equals("0")) {
						hm.put("SEARCH_ADDR", entpUserMap.get("POST_ADDR").toString());
						hm.put("SEARCH_ADDR_INFO","");
						hm.put("SEARCH_ADDR2", entpUserMap.get("ADDR").toString());
					}

					paramList = postUtil.checkLocalYn(paramList, request, hm); //로컬 체크
					
					try{
						post = postUtil.retrieveVerifyPost(paramList);
					}catch(Exception e){
						e.printStackTrace();
					}
				    
					ArrayList<HashMap<String, Object>> b = (ArrayList<HashMap<String, Object>>) post.get("RESULT");
					Map<String, Object> result = (Map<String, Object>) b.get(0);
					
					gnrlTlphnNo  = entpUserMap.get("ENTP_MAN_TEL").toString(); //일반전화번호
					prtblTlphnNo  = entpUserMap.get("ENTP_MAN_HP").toString(); //휴대전화번호
					rcvrNm = entpUserMap.get("ENTP_MAN_NAME").toString(); //이름
					addrNm = entpUserMap.get("ENTP_CODE").toString()+"-"+entpUserMap.get("ENTP_MAN_SEQ").toString(); //주소명
					addrSeq = entpUserMap.get("PA_ADDR_SEQ").toString();
					buildMnNo = result.get("NNMB").toString();
					dtlsAddr = result.get("NADR1S").toString()+" " + result.get("NADR2S").toString();
					
					if(buildMnNo.equals("") || dtlsAddr.equals("")){
						paramMap.put("code","404");
						paramMap.put("message",getMessage("pa.address_fail"));
						//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						
						//전송관리 테이블 저장
						PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
						paGoodsTransLog.setGoodsCode(entpUserMap.get("ENTP_CODE").toString());
						paGoodsTransLog.setPaCode(entpUserMap.get("PA_CODE").toString());
						paGoodsTransLog.setItemNo(entpUserMap.get("ENTP_MAN_SEQ").toString());
						paGoodsTransLog.setRtnCode(paramMap.getString("code"));
						paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
						paGoodsTransLog.setSuccessYn(paramMap.getString("code").equals("200")==true?"1":"0");
						paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paGoodsTransLog.setProcId("PA11");
						pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
						
					} else {
						paramMap.put("code","200");
						paramMap.put("message","OK");
					}
				
					log.info("주소 정제결과:"+gnrlTlphnNo+" "+prtblTlphnNo+" "+rcvrNm+" "+addrNm+" "+buildMnNo+" "+dtlsAddr);
//					log.info("04.반품/교환처 수정 API 호출");
					
					if(entpUserMap.get("PA_CODE").equals(Constants.PA_11ST_BROAD_CODE)){//방송
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"");
						paEntpSlip.setPaCode(Constants.PA_11ST_BROAD_CODE);
					} else {//온라인
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"");
						paEntpSlip.setPaCode(Constants.PA_11ST_ONLINE_CODE);
					}
					
					// REQUEST XML 생성.
					reqXml = new StringBuilder();
					reqXml.append("<InOutAddress>");
					reqXml.append("<addrNm>"+addrNm+"</addrNm>");
					reqXml.append("<rcvrNm>"+rcvrNm+"</rcvrNm>");
					reqXml.append("<gnrlTlphnNo>"+gnrlTlphnNo+"</gnrlTlphnNo>");
					reqXml.append("<prtblTlphnNo>"+prtblTlphnNo+"</prtblTlphnNo>");
					reqXml.append("<mailNO>"+""+"</mailNO>");
					reqXml.append("<ordpeNm>"+""+"</ordpeNm>");
					reqXml.append("<buildMngNO>"+buildMnNo+"</buildMngNO>");
					reqXml.append("<dtlsAddr><![CDATA["+dtlsAddr+"(※반품시 판매자 지정 택배사 필수, 직접발송불가)]]></dtlsAddr>");
					reqXml.append("<addrSeq >"+addrSeq+"</addrSeq >");
					reqXml.append("</InOutAddress>");
					
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
					    descNodes = doc.getElementsByTagName("ns2:inOutAddresss");
						conn.disconnect();
					
		        		ParamMap pa11stParamMap = new ParamMap();
						for(int j=0; j<descNodes.getLength();j++){
					        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){
					        	if(node.getNodeName().trim().equals("ns2:inOutAddress")){
				        			for(int i=0; i<node.getChildNodes().getLength(); i++){
				            			Node directionList = node.getChildNodes().item(i);
				            			pa11stParamMap.put(directionList.getNodeName().trim(), directionList.getTextContent().trim());
				        			}
					        	}else{
					        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
					        	}
					        }
					    }
						log.info("insert result message : "+paramMap.getString("ns2:result_message"));
						
						if(pa11stParamMap.getString("addrSeq").equals("") || pa11stParamMap.getString("addrSeq").equals(null)){
							paramMap.put("code","404");
							paramMap.put("message",getMessage("partner.no_change_data"));
						} else {
							paEntpSlip.setEntpCode(entpUserMap.get("ENTP_CODE"));
							paEntpSlip.setEntpManSeq(entpUserMap.get("ENTP_MAN_SEQ"));
							paEntpSlip.setPaAddrGb(entpUserMap.get("ENTP_MAN_GB"));
							paEntpSlip.setPaAddrSeq(pa11stParamMap.getString("addrSeq"));
							paEntpSlip.setTransTargetYn("0");
							paEntpSlip.setModifyId("PA11");
							paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							//log.info("05.반품/교환처 저장");
							rtnMsg = pa11stCommonService.savePa11stEntpSlipUpdateTx(paEntpSlip);
							
							if(!rtnMsg.equals("000000")){
								paramMap.put("code","404");
								paramMap.put("message",rtnMsg);
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							} else {
								paramMap.put("code","200");
								paramMap.put("message","OK");
							}
						}
					} else {
						paramMap.put("code","500");
						paramMap.put("message",respMsg);
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
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}

	
	/**
	 * 카테고리별 인증여부 조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "카테고리별 인증여부 조회", notes = "카테고리별 인증여부 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cert-goodskinds-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> certGoodsKindsList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
	        
        String dateTime = "";
        String duplicateCheck = "";
        
        List<PaGoodsKinds> paGoodsKindsList = new ArrayList<PaGoodsKinds>();
        PaGoodsKinds paGoodsKinds = null;

        try{
            log.info("===== 카테고리별 인증여부 조회 API Start=====");

            //log.info("01.API 기본정보 세팅");
    		dateTime = systemService.getSysdatetimeToString();
    		
            paramMap.put("apiCode", "IF_PA11STAPI_00_007");
            paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
            paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
            paramMap.put("startDate", dateTime);
            
            //log.info("02.API 중복실행검사");
            //= 중복 실행 Check
		    duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
		    if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
	            
		    log.info("03.카테고리별 인증 엑셀파일 조회");

			POIFSFileSystem fs = null;

			String fileName ="Product_Cert.xls";
			String fileDirPath = new HttpServletRequestWrapper(request).getRealPath("/uploadfile");
			File file = new File(fileDirPath+"/"+fileName);

			if (!file.exists()) {
				paramMap.put("ERROR_MESSAGE", getMessage("msg.upload_failure"));
			}
			
			fs = new POIFSFileSystem(new FileInputStream(file));
			
			// 워크북생성
			HSSFWorkbook wb = new HSSFWorkbook(fs); // 파일에 대한 워크북을 생성

			HSSFRow row = null;
			HSSFSheet sheet = wb.getSheetAt(0); // 시트가져오기(현재 1개의 시트만사용하므로 0번째의 시트를 가져온다)
			int rows = sheet.getPhysicalNumberOfRows(); // 행갯수 가져오기(총행의 갯수)
	    	if (rows > 0) {
				for (int r = 5; r < rows; r++) {
					
					row = sheet.getRow(r);
					
					if (row == null) {
						paramMap.put("code","404");
						paramMap.put("message",getMessage("errors.no.select"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					if(row.getCell(12).getStringCellValue().equals("필수")){

						paGoodsKinds = new PaGoodsKinds();
						paGoodsKinds.setPaLgroupName(row.getCell(2).getStringCellValue());
						paGoodsKinds.setPaLgroup(Integer.toString((int)row.getCell(3).getNumericCellValue()));
						paGoodsKinds.setPaMgroupName(row.getCell(4).getStringCellValue());
						paGoodsKinds.setPaMgroup(Integer.toString((int)row.getCell(5).getNumericCellValue()));
						
						if(!row.getCell(6).getStringCellValue().isEmpty()){
							paGoodsKinds.setPaSgroupName(row.getCell(6).getStringCellValue());
							paGoodsKinds.setPaSgroup(Integer.toString((int)row.getCell(7).getNumericCellValue()));
							if(!row.getCell(8).getStringCellValue().isEmpty()){
								paGoodsKinds.setPaDgroupName(row.getCell(8).getStringCellValue());
								paGoodsKinds.setPaDgroup(Integer.toString((int)row.getCell(9).getNumericCellValue()));
							}
						}
						paGoodsKinds.setCertYn("1");
						paGoodsKinds.setPaGroupCode("01");
						paGoodsKinds.setModifyId("PA11");
						paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
						
						paGoodsKindsList.add(paGoodsKinds);
					}
				}
	    	}
		    log.info("04.카테고리별 인증 정보 저장");
 			rtnMsg = pa11stCommonService.savePa11stCertGoodsKindsTx(paGoodsKindsList);
			
			if(!rtnMsg.equals("000000")){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("errors.no.select"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			} else {
				paramMap.put("code","200");
				paramMap.put("message","OK");
			}

        }catch (Exception e) {
			paramMap.put("code","500");
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR );
		}finally{
		    	try{
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}

			//log.info("05.저장 완료 API END");
		}
        
        return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품고시조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품고시조회", notes = "상품고시조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-offer-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsOfferList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
	        
        String dateTime = "";
        String duplicateCheck = "";
        
        List<PaOfferCode> pa11stOfferCodeList = new ArrayList<PaOfferCode>();
        PaOfferCode pa11stOfferCode = null;

        try{
            log.info("===== 상품고시조회 API Start=====");

            //log.info("01.API 기본정보 세팅");
    		dateTime = systemService.getSysdatetimeToString();
    		
            paramMap.put("apiCode", "IF_PA11STAPI_00_008");
            paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
            paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
            paramMap.put("startDate", dateTime);
            
            //log.info("02.API 중복실행검사");
            //= 중복 실행 Check
		    duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
		    if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
	            
		    log.info("03.11번가 상품고시 엑셀파일 조회");

			POIFSFileSystem fs = null;

			String fileName ="Product_notification.xls";
			String fileDirPath = new HttpServletRequestWrapper(request).getRealPath("/uploadfile");
			File file = new File(fileDirPath+"/"+fileName);

			if (!file.exists()) {
				paramMap.put("ERROR_MESSAGE", getMessage("msg.upload_failure"));
			}
			
			fs = new POIFSFileSystem(new FileInputStream(file));
			
			// 워크북생성
			HSSFWorkbook wb = new HSSFWorkbook(fs); // 파일에 대한 워크북을 생성

			HSSFRow row = null;
			HSSFSheet sheet = wb.getSheetAt(0); // 시트가져오기(현재 1개의 시트만사용하므로 0번째의 시트를 가져온다)
			int rows = sheet.getPhysicalNumberOfRows(); // 행갯수 가져오기(총행의 갯수)
	    	if (rows > 0) {
				for (int r = 1; r < rows; r++) {
					
					row = sheet.getRow(r);
					
					if (row == null) {
						paramMap.put("code","404");
						paramMap.put("message",getMessage("errors.no.select"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					

					pa11stOfferCode = new PaOfferCode();
					pa11stOfferCode.setPaGroupCode("01");
					pa11stOfferCode.setPaOfferTypeName(row.getCell(0).getStringCellValue());//유형명
					pa11stOfferCode.setPaOfferType(Integer.toString((int)row.getCell(1).getNumericCellValue()));//유형코드					
					pa11stOfferCode.setPaOfferCodeName(row.getCell(2).getStringCellValue());//항목명
					pa11stOfferCode.setPaOfferCode(Integer.toString((int)row.getCell(3).getNumericCellValue()));//항목코드
					pa11stOfferCode.setRemark(row.getCell(5).getStringCellValue());//상세입력방법
					pa11stOfferCode.setRequiredYn("1");
					pa11stOfferCode.setUseYn("1");
					pa11stOfferCode.setInsertId("PA11");
					pa11stOfferCode.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
					pa11stOfferCode.setModifyId("PA11");
					pa11stOfferCode.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
					
					pa11stOfferCodeList.add(pa11stOfferCode);
				
				}
	    	}
		    log.info("04.정보고시 저장");
 			rtnMsg = pa11stCommonService.savePa11stOfferCodeTx(pa11stOfferCodeList);
			
			if(!rtnMsg.equals("000000")){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("errors.no.select"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			} else {
				paramMap.put("code","200");
				paramMap.put("message","OK");
			}

        }catch (Exception e) {
			paramMap.put("code","500");
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR );
		}finally{
		    	try{
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}

			//log.info("05.저장 완료 API END");
		}
        
        return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 배송불가지역 등록 (배송불가지역등록 제휴사코드 별로 호출 후 수동으로 배송불가지역 조회 호출 필요! 메모리 부족현상 발생)
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	@ApiOperation(value = "배송불가지역 등록", notes = "배송불가지역 등록(배송불가지역등록 제휴사코드 별로 호출 후 수동으로 배송불가지역 조회 호출 필요! 메모리 부족현상 발생)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/no-dely-area-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> noDelyAreaInsert(HttpServletRequest request,@RequestParam(value="paCode", required=true) String paCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		 
		Document doc = null;
		NodeList descNodes = null;
		String dateTime = "";
		String duplicateCheck = "";
		OutputStreamWriter out = null;
		StringBuilder reqXml = null;
		int respCode = 0;
		String respMsg = null;
		String procPaCode = "";
		String tempMailNo = "";
		HashMap<String, Object> postMap = null;
		StringBuilder reqXmlList[] = new StringBuilder[10];
		int checkCnt = 0;
		
		log.info("===== 배송불가지역 등록 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_00_009";
		String request_type = "POST";
		String noDelyAreaName = "배송불가지역";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		if(StringUtil.isEmpty(paCode)){
			paramMap.put("code","410");
			paramMap.put("message",getMessage("pa.check_pa_code_data"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			if(paCode.equals(Constants.PA_11ST_ONLINE_CODE)){
				procPaCode = Constants.PA_11ST_ONLINE;
			}else {
				procPaCode = Constants.PA_11ST_BROAD;
			}
			
			log.info("03.tpa11stnodelyaream 데이터 중복 체크"); 
			checkCnt = pa11stCommonService.checkTpa11stNoDelyAream(paCode);
			if(checkCnt > 0) throw processException("ssg.cannot_dup_data", new String[] {paCode});
		
			log.info("04.우편번호 조회"); 
			List<Object> postList = pa11stCommonService.selectTroadPostList(paCode);
			
			if(postList.size() > 0){
				log.info("05.배송불가지역 등록 API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,"",300000,300000);
				
				// REQUEST XML 생성.
				reqXml = new StringBuilder();
				reqXml.append("<ProductDlvLimits>");
				reqXml.append("<prdInfoTmpltNm>"+noDelyAreaName+"</prdInfoTmpltNm>");
				int nextCnt = 0;
				for(int i=0; i<postList.size(); i++){
					postMap = (HashMap<String, Object>) postList.get(i);
					reqXml.append("<ProductDlvLimit>");
					reqXml.append("<mailNo>"+postMap.get("POST_NO").toString()+"</mailNo>");
					reqXml.append("<mailNoSeq></mailNoSeq>");
					reqXml.append("</ProductDlvLimit>");
					if(i == postList.size() - 1){
						reqXml.append("</ProductDlvLimits>");
						reqXmlList[nextCnt] = reqXml;
					}else if(reqXml.length() > 5000){
						reqXmlList[nextCnt] = reqXml;
						nextCnt++;
						reqXml = new StringBuilder();
					}
				}
				
				if(reqXmlList != null){
					out = new OutputStreamWriter(conn.getOutputStream());
					for(int j=0; j < reqXmlList.length ; j++){
						if(reqXmlList[j] != null){
							StringBuilder xmlReq = reqXmlList[j];
							out.write(String.valueOf(xmlReq));
						}
					}
					out.flush();
					out.close();
					
					respCode = conn.getResponseCode();
					respMsg  = conn.getResponseMessage();
					//log.info(" connect respCode : "+respCode);
					//log.info(" connect respMsg  : "+respMsg);
					
					if(respCode == 200){
						// RESPONSE XML 			
						doc = ComUtil.parseXML(conn.getInputStream());
						descNodes = doc.getElementsByTagName("ProductDlvLimits");
						conn.disconnect();
						
						List<ParamMap> responseList = new ArrayList<ParamMap>();
						Pa11stnodelyareamVO pa11stnodelyaream = null;
						ParamMap pa11stParamMap = null;
						Node directionList = null;
						List<Pa11stnodelyareamVO> arrPa11stNoDelyAreaList = new ArrayList<Pa11stnodelyareamVO>();
						for(int j=0; j<descNodes.getLength();j++){
							for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
								if(node.getNodeName().trim().equals("ProductDlvLimit")){
									pa11stParamMap = new ParamMap();
				        			for(int i=0; i<node.getChildNodes().getLength(); i++){
				        				directionList = node.getChildNodes().item(i);
				        				if(directionList.getNodeName().trim().equals("mailNo")){
				        					if(tempMailNo.equals(directionList.getTextContent().trim())){
				        						break;
				        					}else {
				        						tempMailNo = directionList.getTextContent().trim();
				        					}
				        				}
				        				pa11stParamMap.put(directionList.getNodeName().trim(), directionList.getTextContent().trim());
				        			}
				        			if(StringUtil.isNotEmpty(pa11stParamMap.getString("mailNo"))){
				        				responseList.add(pa11stParamMap);
				        			}
								}else{
									paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
								}
							}
						}
						
						if( responseList.size() > 0 ){
							Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
							for(int i=0; i<postList.size(); i++){
								postMap = (HashMap<String, Object>) postList.get(i);
								pa11stnodelyaream = new Pa11stnodelyareamVO();
								
								pa11stnodelyaream.setPaCode(paCode);
								pa11stnodelyaream.setPrdInfoTmpltNo(responseList.get(0).getString("prdInfoTmpltNo"));
								pa11stnodelyaream.setPrdInfoTmpltNm(noDelyAreaName);
								pa11stnodelyaream.setMailNo(postMap.get("POST_NO").toString());
								pa11stnodelyaream.setMailNoSeq("0");
								pa11stnodelyaream.setInsertDate(sysdateTime);
								pa11stnodelyaream.setInsertId("PA11");
								pa11stnodelyaream.setModifyDate(sysdateTime);
								pa11stnodelyaream.setModifyId("PA11");
								
								arrPa11stNoDelyAreaList.add(pa11stnodelyaream);
								
							}
							
							log.info("06.배송불가지역 등록 저장");
							rtnMsg = pa11stCommonService.saveNoDelyAreaInsertTx(arrPa11stNoDelyAreaList);
							
							if(!rtnMsg.equals("000000")){
								paramMap.put("code","500");
								paramMap.put("message",rtnMsg);
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							} else {
								paramMap.put("code","200");
								paramMap.put("message","OK");
//									log.info("06.배송불가지역 조회 호출");
//									conn.disconnect();
//									noDelyAreaSelect(request,arrPa11stNoDelyAreaList.get(0).getNoDelyAreaCode(),arrPa11stNoDelyAreaList.get(0).getPrdInfoTmpltNo(),paCode);
								
							}
							
						} else {
							//11ST API 연결 실패
							paramMap.put("code","500");
							paramMap.put("message", getMessage("errors.exist",new String[] { "noDelyAreaInsert" }));
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						}
					}
				}
			}else {
				paramMap.put("code","200");
				paramMap.put("message","OK[조회할 대상이 없습니다]");
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
			if (out != null)
				out.close();
			try{
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
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 배송불가지역 조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "배송불가지역 조회", notes = "배송불가지역 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/no-dely-area-select", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> noDelyAreaSelect(HttpServletRequest request,
			@ApiParam(name = "noDelyAreaCode", value = "배송불가지역순번", defaultValue = "") @RequestParam(value="noDelyAreaCode", required=true) String noDelyAreaCode,
			@ApiParam(name = "prdInfoTmpltNo", value = "배송불가지역 템플릿번호", defaultValue = "") @RequestParam(value="prdInfoTmpltNo", required=true) String prdInfoTmpltNo,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		 
		Document doc = null;
		NodeList descNodes = null;
		String dateTime = "";
		String duplicateCheck = "";
		String procPaCode = "";
		String tempMailNo = "";
		
		log.info("===== 배송불가지역 조회 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String parameter = "";
		String prg_id = "IF_PA11STAPI_00_011";
		String request_type = "GET";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		if(StringUtil.isEmpty(paCode)){
			paramMap.put("code","410");
			paramMap.put("message",getMessage("pa.check_pa_code_data"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}
		
		if(StringUtil.isEmpty(prdInfoTmpltNo)){
			paramMap.put("code","411");
			paramMap.put("message",getMessage("pa.check_pa_tmplt_no_data"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}
		
		if(StringUtil.isEmpty(noDelyAreaCode)){
			paramMap.put("code","412");
			paramMap.put("message",getMessage("pa.check_dely_area_code_data"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}
		
		parameter = "/"+prdInfoTmpltNo;
		
		if(paCode.equals(Constants.PA_11ST_ONLINE_CODE)){
			procPaCode = Constants.PA_11ST_ONLINE;
		}else {
			procPaCode = Constants.PA_11ST_BROAD;
		}
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			log.info("03.배송불가지역 조회 API 호출");
			conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,parameter,300000,300000);
			
			// RESPONSE XML 			
			doc = ComUtil.parseXML(conn.getInputStream());
			descNodes = doc.getElementsByTagName("ProductDlvLimits");
			
			conn.disconnect();
		
			List<ParamMap> responseList = new ArrayList<ParamMap>();
			List<Pa11stnodelyareamVO> arrPa11stNoDelyAreaList = new ArrayList<Pa11stnodelyareamVO>();
			ParamMap pa11stParamMap = null;
			Node directionList = null;
			int i=0;
			for(int j=0; j<descNodes.getLength();j++){
				for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
		        	if(node.getNodeName().trim().equals("ProductDlvLimit")){
		        		pa11stParamMap = new ParamMap();
	        			for(i=0; i<node.getChildNodes().getLength(); i++){
	        				directionList = node.getChildNodes().item(i);
	        				if(directionList.getNodeName().trim().equals("mailNo")){
	        					if(tempMailNo.equals(directionList.getTextContent().trim())){
	        						break;
	        					}else {
	        						tempMailNo = directionList.getTextContent().trim();
	        					}
	        				}
	        				pa11stParamMap.put(directionList.getNodeName().trim(), directionList.getTextContent().trim());
	        			}
	        			if(StringUtil.isNotEmpty(pa11stParamMap.getString("mailNo"))){
	        				responseList.add(pa11stParamMap);
	        			}
		        	}else{
		        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
		        	}
		        }
		    }
			
			
			if( responseList.size() > 0 ){
				Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
				Pa11stnodelyareamVO pa11stnodelyaream = null;
				for(int z=0; z<responseList.size(); z++){
					pa11stnodelyaream = new Pa11stnodelyareamVO();
					
					pa11stnodelyaream.setPaCode(paCode);
					pa11stnodelyaream.setNoDelyAreaCode(noDelyAreaCode);
					pa11stnodelyaream.setDlvCnntAreaNo(responseList.get(z).getString("dlvCnntAreaNo"));
					pa11stnodelyaream.setPrdInfoTmpltNm(responseList.get(z).getString("prdInfoTmpltNm"));
					pa11stnodelyaream.setMailNo(responseList.get(z).getString("mailNo"));
					pa11stnodelyaream.setAddr(responseList.get(z).getString("addr"));
					pa11stnodelyaream.setInsertDate(sysdateTime);
					pa11stnodelyaream.setModifyDate(sysdateTime);
					pa11stnodelyaream.setModifyId("PA11");
					
					arrPa11stNoDelyAreaList.add(pa11stnodelyaream);
					
					
				}
				
				log.info("04.배송불가지역 배송불가지역번호 저장");
				rtnMsg = pa11stCommonService.saveNoDelyAreaSelectTx(arrPa11stNoDelyAreaList);
				
				if(!rtnMsg.equals("000000")){
					paramMap.put("code","500");
					paramMap.put("message",rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} else {
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}
				
			} else {
				//11ST API 연결 실패
				paramMap.put("code","500");
				paramMap.put("message", getMessage("errors.exist",new String[] { "noDelyAreaSelect" }));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
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
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("05.배송불가지역 조회 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 배송불가지역 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	@ApiOperation(value = "배송불가지역 수정", notes = "배송불가지역 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/no-dely-area-update", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> noDelyAreaUpdate(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		 
		Document doc = null;
		NodeList descNodes = null;
		String dateTime = "";
		String duplicateCheck = "";
		OutputStreamWriter out = null;
		StringBuilder reqXml = null;
		int respCode = 0;
		String respMsg = null;
		String procPaCode = "";
		String paCode = "";
		HashMap<String, Object> postMap = null;
		StringBuilder reqXmlList[] = new StringBuilder[10];
		
		log.info("===== 배송불가지역 수정 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_00_010";
		String request_type = "POST";
		String noDelyAreaName = "배송불가지역";
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
			
			for(int count=0 ; count<Constants.PA_11ST_CONTRACT_CNT; count++){
				
				if(count==0){
					procPaCode = Constants.PA_11ST_ONLINE;
					paCode = Constants.PA_11ST_ONLINE_CODE;
				}else{
					procPaCode = Constants.PA_11ST_BROAD;
					paCode = Constants.PA_11ST_BROAD_CODE;
				}
				
				log.info("03.우편번호 추가등록 및 배송불가지역 삭제할 대상 조회"); 
				List<Object> postList = pa11stCommonService.selectNoDelyAreaUpdateList(paCode);
				
				if(postList.size() > 0){
					log.info("04.배송불가지역 수정 API 호출");
					conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,"",180000,180000);
					
					// REQUEST XML 생성.
					reqXml = new StringBuilder();
					reqXml.append("<ProductDlvLimits>");
					reqXml.append("<prdInfoTmpltNo>"+((HashMap<String, Object>) postList.get(0)).get("PRD_INFO_TMPLT_NO").toString()+"</prdInfoTmpltNo>");
					reqXml.append("<prdInfoTmpltNm>"+noDelyAreaName+"</prdInfoTmpltNm>");
					int nextCnt = 0;
					for(int i=0; i<postList.size(); i++){
						postMap = (HashMap<String, Object>) postList.get(i);
						reqXml.append("<ProductDlvLimit>");
						reqXml.append("<mailNo>"+postMap.get("POST_NO").toString()+"</mailNo>");
						reqXml.append("<mailNoSeq></mailNoSeq>");
						if(postMap.get("DELETE_YN").toString().equals("Y")){
							reqXml.append("<deleteYn>"+postMap.get("DELETE_YN").toString()+"</deleteYn>");
							reqXml.append("<dlvCnntAreaNo>"+postMap.get("DLV_CNNT_AREA_NO").toString()+"</dlvCnntAreaNo>");
						}
						reqXml.append("</ProductDlvLimit>");
						if(i == postList.size() - 1){
							reqXml.append("</ProductDlvLimits>");
							reqXmlList[nextCnt] = reqXml;
						}else if(reqXml.length() > 5000){
							reqXmlList[nextCnt] = reqXml;
							nextCnt++;
							reqXml = new StringBuilder();
						}
					}
					
					if(reqXmlList != null){
						out = new OutputStreamWriter(conn.getOutputStream());
						for(int j=0; j < reqXmlList.length ; j++){
							if(reqXmlList[j] != null){
								StringBuilder xmlReq = reqXmlList[j];
								out.write(String.valueOf(xmlReq));
							}
						}
						out.flush();
						out.close();
						
						respCode = conn.getResponseCode();
						respMsg  = conn.getResponseMessage();
						//log.info(" connect respCode : "+respCode);
						//log.info(" connect respMsg  : "+respMsg);
						
						if(respCode == 200){
							// RESPONSE XML 			
							doc = ComUtil.parseXML(conn.getInputStream());
							descNodes = doc.getElementsByTagName("ProductDlvLimits");
							conn.disconnect();
							
							List<ParamMap> responseList = new ArrayList<ParamMap>();
							Pa11stnodelyareamVO pa11stnodelyaream = null;
							List<Pa11stnodelyareamVO> arrPa11stNoDelyAreaList = new ArrayList<Pa11stnodelyareamVO>();
							for(int j=0; j<descNodes.getLength();j++){
								for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
									if(node.getNodeName().trim().equals("ProductDlvLimit")){
										ParamMap pa11stParamMap = new ParamMap();
										for(int i=0; i<node.getChildNodes().getLength(); i++){
											Node directionList = node.getChildNodes().item(i);
											pa11stParamMap.put(directionList.getNodeName().trim(), directionList.getTextContent().trim());
										}
										responseList.add(pa11stParamMap);
									}else{
										paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
									}
								}
							}
							
							if( responseList.size() > 0 ){
								Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
								for(int i=0; i<postList.size(); i++){
									postMap = (HashMap<String, Object>) postList.get(i);
									pa11stnodelyaream = new Pa11stnodelyareamVO();
									
									pa11stnodelyaream.setPaCode(paCode);
									pa11stnodelyaream.setCwareAction(postMap.get("CWARE_ACTION").toString());
									pa11stnodelyaream.setNoDelyAreaCode(postMap.get("NO_DELY_AREA_CODE").toString());
									pa11stnodelyaream.setPrdInfoTmpltNo(responseList.get(0).getString("prdInfoTmpltNo"));
									pa11stnodelyaream.setPrdInfoTmpltNm(noDelyAreaName);
									pa11stnodelyaream.setMailNo(postMap.get("POST_NO").toString());
									pa11stnodelyaream.setMailNoSeq("0");
									pa11stnodelyaream.setDeleteYn(postMap.get("DELETE_YN").toString());
									pa11stnodelyaream.setDlvCnntAreaNo(postMap.get("DLV_CNNT_AREA_NO").toString());
									pa11stnodelyaream.setInsertDate(sysdateTime);
									pa11stnodelyaream.setInsertId("PA11");
									pa11stnodelyaream.setModifyDate(sysdateTime);
									pa11stnodelyaream.setModifyId("PA11");
									
									arrPa11stNoDelyAreaList.add(pa11stnodelyaream);
									
								}
								
								log.info("05.배송불가지역 수정 저장");
								rtnMsg = pa11stCommonService.saveNoDelyAreaUpdateTx(arrPa11stNoDelyAreaList);
								
								if(!rtnMsg.equals("000000")){
									paramMap.put("code","500");
									paramMap.put("message",rtnMsg);
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								} else {
									log.info("06.배송불가지역 조회 호출");
									paramMap.put("code","200");
									paramMap.put("message","OK");
									conn.disconnect();
									noDelyAreaSelect(request,arrPa11stNoDelyAreaList.get(0).getNoDelyAreaCode(),arrPa11stNoDelyAreaList.get(0).getPrdInfoTmpltNo(),paCode);
								}
								
							} else {
								//11ST API 연결 실패
								paramMap.put("code","500");
								paramMap.put("message", getMessage("errors.exist",new String[] { "noDelyAreaInsert" }));
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							}
						}else {
							paramMap.put("code",respCode);
							paramMap.put("message",respMsg);
						}
					}
				}else {
					paramMap.put("code","200");
					paramMap.put("message","OK[조회할 대상이 없습니다]");
					
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
			if (out != null)
				out.close();
			try{
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("07.배송불가지역 수정 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 배송불가지역 적용
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	@ApiOperation(value = "배송불가지역 적용", notes = "배송불가지역 적용", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/no-dely-area-apply", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> noDelyAreaApply(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		String dateTime = "";
		String duplicateCheck = "";
		int respCode = 0;
		String respMsg = null;
		StringBuffer sb = new StringBuffer();
		String procPaCode = "";
		String paCode = "";
		String parameter ="";
		HashMap<String, Object> goodsMap = null;
		
		log.info("===== 배송불가지역 적용 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_00_012";
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
			
			for(int count=0 ; count<Constants.PA_11ST_CONTRACT_CNT; count++){
				
				if(count==0){
					procPaCode = Constants.PA_11ST_ONLINE;
					paCode = Constants.PA_11ST_ONLINE_CODE;
				}else{
					procPaCode = Constants.PA_11ST_BROAD;
					paCode = Constants.PA_11ST_BROAD_CODE;
				}
				
				try{
					log.info("03.배송불가지역 속성 상품 추가"); 
					rtnMsg = pa11stCommonService.saveNoDelyAreaApplyGoodsTx(paCode);
					
					if(!rtnMsg.equals("000000")){
						paramMap.put("code","500");
						paramMap.put("message",rtnMsg);
					} else {
						
						log.info("04.배송불가 적용할 상품 조회"); 
						List<Object> goodsList = pa11stCommonService.selectNoDelyAreaApplyList(paCode);
						
						if(goodsList.size() > 0){
							log.info("05.배송불가지역 적용 API 호출");
							Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
							
							for(int i=0; i<goodsList.size(); i++){
								goodsMap = (HashMap<String, Object>) goodsList.get(i);
								parameter = "/"+goodsMap.get("PRD_INFO_TMPLT_NO").toString()+"/"+goodsMap.get("PRD_NO").toString();
								try{
									conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,parameter);
									
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
											for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
												paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
											}
										}
										log.info("result message : "+paramMap.getString("message"));
										
										if(paramMap.getString("resultCode").equals("200")){
											
											Pa11stnodelyareamVO pa11stnodelyaream = new Pa11stnodelyareamVO();
											pa11stnodelyaream.setPaCode(paCode);
											pa11stnodelyaream.setPrdInfoTmpltNo(goodsMap.get("PRD_INFO_TMPLT_NO").toString());
											pa11stnodelyaream.setNoDelyAreaCode(goodsMap.get("NO_DELY_AREA_CODE").toString());
											pa11stnodelyaream.setPrdNo(goodsMap.get("PRD_NO").toString());
											pa11stnodelyaream.setApplyYn("1");
											pa11stnodelyaream.setInsertDate(sysdateTime);
											pa11stnodelyaream.setModifyDate(sysdateTime);
											pa11stnodelyaream.setInsertId("PA11");
											pa11stnodelyaream.setModifyId("PA11");
											
											log.info("06.배송불가 적용 저장");
											rtnMsg = pa11stCommonService.saveNoDelyAreaApplyTx(pa11stnodelyaream);
											
											if(!rtnMsg.equals("000000")){
												paramMap.put("code","500");
												paramMap.put("message",rtnMsg);
												log.info(goodsMap.get("PRD_NO").toString() + ": 배송불가지역적용 fail - " + "|");
												sb.append(goodsMap.get("PRD_NO").toString() + ": 배송불가지역적용 fail - " + "|");
												return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
											} else {
												paramMap.put("code","200");
												paramMap.put("message","OK");
												
											}
										} else {
											paramMap.put("code",paramMap.getString("resultCode"));
										}
									} else {
										paramMap.put("code",respCode);
										paramMap.put("message",respMsg);
									}
								}catch (Exception e) {
									paramMap.put("code","500");
									if(e.getMessage()!=null){
										paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
										log.error(paramMap.getString("message"), e);
									}else{
										log.error(paramMap.getString("message"), e);
									}
									log.info(goodsMap.get("PRD_NO").toString() + ": 배송불가지역적용 fail - " +e.getMessage() + "|");
									sb.append(goodsMap.get("PRD_NO").toString() + ": 배송불가지역적용 fail - " +e.getMessage() + "|");
									continue;
								}
							}
						}else {
							paramMap.put("code","200");
							paramMap.put("message","OK[조회할 대상이 없습니다]");
							
						}
					}
					
				}catch (Exception e) {
					paramMap.put("code","500");
					if(e.getMessage()!=null){
						paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
						log.error(paramMap.getString("message"), e);
					}else{
						log.error(paramMap.getString("message"), e);
					}
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
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("07.배송불가지역 적용 저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 배송불가지역 해제
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	@ApiOperation(value = "배송불가지역 해제", notes = "배송불가지역 해제", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/no-dely-area-clear", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> noDelyAreaClear(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		HttpURLConnection conn = null;
		Document doc = null;
		NodeList descNodes = null;
		String dateTime = "";
		String duplicateCheck = "";
		int respCode = 0;
		String respMsg = null;
		StringBuffer sb = new StringBuffer();
		String procPaCode = "";
		String paCode = "";
		String parameter ="";
		HashMap<String, Object> goodsMap = null;
		
		log.info("===== 배송불가지역 해제 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_00_013";
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
			
			for(int count=0 ; count<Constants.PA_11ST_CONTRACT_CNT; count++){
				
				if(count==0){
					procPaCode = Constants.PA_11ST_ONLINE;
					paCode = Constants.PA_11ST_ONLINE_CODE;
				}else{
					procPaCode = Constants.PA_11ST_BROAD;
					paCode = Constants.PA_11ST_BROAD_CODE;
				}
				
				log.info("03.배송불가지역 해제할 상품 조회"); 
				List<Object> goodsList = pa11stCommonService.selectNoDelyAreaDeleteList(paCode);
				
				if(goodsList.size() > 0){
					log.info("04.배송불가지역 해제 API 호출");
					Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
					
					for(int i=0; i<goodsList.size(); i++){
						goodsMap = (HashMap<String, Object>) goodsList.get(i);
						parameter = "/"+goodsMap.get("PRD_NO").toString();
						try{
							conn = ComUtil.pa11stConnectionSetting(apiInfo,procPaCode,request_type,parameter);
							
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
							        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
							        	paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
							        }
							    }
								log.info("result message : "+paramMap.getString("message"));

								if(paramMap.getString("resultCode").equals("200")){
									
									Pa11stnodelyareamVO pa11stnodelyaream = new Pa11stnodelyareamVO();
									pa11stnodelyaream.setPaCode(paCode);
									pa11stnodelyaream.setPrdInfoTmpltNo(goodsMap.get("PRD_INFO_TMPLT_NO").toString());
									pa11stnodelyaream.setNoDelyAreaCode(goodsMap.get("NO_DELY_AREA_CODE").toString());
									pa11stnodelyaream.setPrdNo(goodsMap.get("PRD_NO").toString());
									pa11stnodelyaream.setApplyYn("0");
									pa11stnodelyaream.setInsertDate(sysdateTime);
									pa11stnodelyaream.setModifyDate(sysdateTime);
									pa11stnodelyaream.setInsertId("PA11");
									pa11stnodelyaream.setModifyId("PA11");
									
									log.info("05.배송불가 해제 저장");
									rtnMsg = pa11stCommonService.saveNoDelyAreaApplyTx(pa11stnodelyaream);
									
									if(!rtnMsg.equals("000000")){
										paramMap.put("code","500");
										paramMap.put("message",rtnMsg);
										log.info(goodsMap.get("PRD_NO").toString() + ": 배송불가지역해제 fail - " + "|");
										sb.append(goodsMap.get("PRD_NO").toString() + ": 배송불가지역해제 fail - " + "|");
										return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
									} else {
										paramMap.put("code","200");
										paramMap.put("message","OK");
										
									}
								} else {
									paramMap.put("code",paramMap.getString("resultCode"));
								}
							} else {
								paramMap.put("code",respCode);
								paramMap.put("message",respMsg);
							}
						}catch (Exception e) {
							paramMap.put("code","500");
							if(e.getMessage()!=null){
								paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
								log.error(paramMap.getString("message"), e);
							}else{
								log.error(paramMap.getString("message"), e);
							}
							log.info(goodsMap.get("PRD_NO").toString() + ": 배송불가지역해제 fail - " +e.getMessage() + "|");
							sb.append(goodsMap.get("PRD_NO").toString() + ": 배송불가지역해제 fail - " +e.getMessage() + "|");
							continue;
						}
					}
				}else {
					paramMap.put("code","200");
					paramMap.put("message","OK[조회할 대상이 없습니다]");
					
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
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("06.배송불가지역 해제 저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 브랜드 조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "브랜드 조회", notes = "브랜드 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goodsbrand-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> GoodsBrandList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
	        
        String dateTime = "";
        String duplicateCheck = "";
        
        List<PaBrand> paBrandList = new ArrayList<PaBrand>();
        PaBrand paBrand = null;

        try{
            log.info("===== 브랜드 조회 API Start=====");

            //log.info("01.API 기본정보 세팅");
    		dateTime = systemService.getSysdatetimeToString();
    		
            paramMap.put("apiCode", "IF_PA11STAPI_00_014");
            paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
            paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
            paramMap.put("startDate", dateTime);
            
            log.info("02.API 중복실행검사");
            //= 중복 실행 Check
		    duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
		    if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
	            
		    log.info("03.브랜드 정보 엑셀파일 조회");

			String fileName ="Product_brand.xlsx";
			String fileDirPath = new HttpServletRequestWrapper(request).getRealPath("/uploadfile");
			File file = new File(fileDirPath+"/"+fileName);

			if (!file.exists()) {
				paramMap.put("ERROR_MESSAGE", getMessage("msg.upload_failure"));
			}
			
			
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
						paBrand = new PaBrand();
						paBrand.setPaGroupCode("01");
						paBrand.setPaBrandNo(row.getCell(0).getStringCellValue());
						paBrand.setPaBrandName(row.getCell(1).getStringCellValue());
						paBrand.setInsertId("PA11");
						paBrand.setModifyId("PA11");
						paBrand.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paBrand.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
						
						paBrandList.add(paBrand);
					}
				}
			}
		    log.info("04.브랜드 정보 저장");
 			rtnMsg = pa11stCommonService.savePa11stBrandTx(paBrandList);
			
			if(!rtnMsg.equals("000000")){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("errors.no.select"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			} else {
				paramMap.put("code","200");
				paramMap.put("message","OK");
			}

        }catch (Exception e) {
			paramMap.put("code","500");
			if(e.getMessage()!=null){
				paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			}else{
				log.error(paramMap.getString("message"), e);
			}
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR );
		}finally{
		    	try{
				paramMap.put("resultMessage", paramMap.getString("message"));
				//systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}

			//log.info("05.저장 완료 API END");
		}
        
        return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 출고지 배송비 정책 등록/수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "출고지 배송비 정책 등록/수정", notes = "출고지 배송비 정책 등록/수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/entpslip-cost-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpSlipCostInsert(HttpServletRequest request,
			@RequestParam(value="paCode"		, required=false) String paCode
			) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Document doc = null;
		NodeList descNodes = null;
		String dateTime = "";
		OutputStreamWriter out = null;
		StringBuilder reqXml = null;
		int respCode = 0;
		String respMsg = null;
		
		String entpCode;
		String paAddrSeq;
		String ordCost;
		String shipCostBaseAmt;
		String shipCostCode;
		
		int cnt=0;
		log.info("===== 출고지 배송비 등록/수정 API Start=====");
		
		//= connectionSetting 설정
		String prg_id = "IF_PA11STAPI_00_014";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try{
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			for(int count=0 ; count<Constants.PA_11ST_CONTRACT_CNT; count++){
				if(count==0){
					paCode = Constants.PA_11ST_ONLINE_CODE;
				}else{
					paCode = Constants.PA_11ST_BROAD_CODE;
				}
				log.info("출고지 배송비 등록/수정 paCode : "+paCode);
				List<HashMap<?,?>> entpSlipCostMap = pa11stCommonService.selectEntpSlipCost(paCode);
			    
			    if(entpSlipCostMap==null){
					paramMap.put("code","404");
					paramMap.put("message",getMessage("partner.no_change_data"));
			    } else {
			    	for(HashMap<?,?> entpSlipCost : entpSlipCostMap){
			    		entpCode  = entpSlipCost.get("ENTP_CODE").toString();
			    		paAddrSeq = entpSlipCost.get("PA_ADDR_SEQ").toString();
			    		ordCost   = entpSlipCost.get("ORD_COST").toString();
			    		shipCostBaseAmt = entpSlipCost.get("SHIP_COST_BASE_AMT").toString();
			    		shipCostCode = entpSlipCost.get("SHIP_COST_CODE").toString();
			    		if(shipCostBaseAmt.equals("0")){
			    			log.info("조건부배송 shipCostBaseAmt = 0 업체 100원처리, entpCode = "+ entpCode+", shipCostCode = "+ shipCostCode);
			    			shipCostBaseAmt= "100";
			    		}
			    		
			    		System.out.println(entpCode+","+paAddrSeq+","+ordCost+","+shipCostBaseAmt);
			    	
					if(paCode.equals(Constants.PA_11ST_BROAD_CODE)){
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"");
					} else {
						conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"");
					}
					
					// REQUEST XML 생성.
					reqXml = new StringBuilder();
					reqXml.append("<InOutAddress>");
					reqXml.append("<memNo/>");
					reqXml.append("<addrSeq>"+paAddrSeq+"</addrSeq>"); //11,12 인지 구분 필요
					reqXml.append("<addrBasiDlvCst>");
					reqXml.append("<dlvCst>"+ordCost+"</dlvCst>"); //주문배송비
					reqXml.append("<ordBgnAmt>0</ordBgnAmt>");
					reqXml.append("<ordEndAmt>"+shipCostBaseAmt+"</ordEndAmt>"); //무료 기준금액
					reqXml.append("<mbAddrLocation>01</mbAddrLocation>");
					reqXml.append("</addrBasiDlvCst>");
					reqXml.append("<addrBasiDlvCst>");
					reqXml.append("<dlvCst>0</dlvCst>");
					reqXml.append("<ordBgnAmt>"+shipCostBaseAmt+"</ordBgnAmt>");
					reqXml.append("<ordEndAmt>999999999999</ordEndAmt>");
					reqXml.append("<mbAddrLocation>01</mbAddrLocation>");
					reqXml.append("</addrBasiDlvCst>");
					reqXml.append("</InOutAddress>");
					
					out = new OutputStreamWriter(conn.getOutputStream());
					out.write(String.valueOf(reqXml));
					out.flush();
					
					respCode = conn.getResponseCode();
					respMsg  = conn.getResponseMessage();
						if(respCode == 200){
							// RESPONSE XML 			
							doc = ComUtil.parseXML(conn.getInputStream());
						    descNodes = doc.getElementsByTagName("ns2:inOutAddresss");
							conn.disconnect();
						
							for(int j=0; j<descNodes.getLength();j++){
						        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
					        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
						        }
						    }
							//log.info("insert result message : "+paramMap.getString("ns2:result_message"));
				
							//TRANS_CN_COST_YN = 0
							HashMap<String,String> hashMap = new HashMap<>();
							hashMap.put("entpCode", entpCode);
							hashMap.put("shipCostCode", shipCostCode);
							hashMap.put("paCode", paCode);
							hashMap.put("transEndDate", dateTime);
							
							cnt = pa11stCommonService.updateEntpSlipCost(hashMap);
							
							if(cnt != 1){
								paramMap.put("code","404");
								paramMap.put("message",rtnMsg);
							} else {
								paramMap.put("code","200");
								paramMap.put("message","OK");
							}
						} else {
							paramMap.put("code","500");
							paramMap.put("message",respMsg);
						}
						paramMap.put("code","200");
						paramMap.put("message","OK");	
				    }
			    	paramMap.put("code","200");
					paramMap.put("message","OK");	
			    }
		    	//System.out.println("test");
			}
		}catch (Exception e) {
			paramMap.put("code","500");
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
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 정산내역 조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "정산내역 조회", notes = "정산내역 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/settlement-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> settlementList(HttpServletRequest request,
			@ApiParam(name = "fromDate", value = "검색시작일", defaultValue = "") @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
			@ApiParam(name = "toDate", value = "검색종료일", defaultValue = "") @RequestParam(value = "toDate", required = false, defaultValue = "") String toDate) 
			throws Exception{ 
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HttpURLConnection conn = null;
		String rtnMsg = Constants.SAVE_SUCCESS;

		List<Pa11stSettlement> pa11stSettlementList = new ArrayList<Pa11stSettlement>(); 
		Document doc = null;
		NodeList descNodes = null;
		String dateTime = "";
		String duplicateCheck = "";
		String startDate = "";
		String endDate = "";
		String procPaCode = "";
		
		log.info("===== 정산내역조회 API Start=====");
		//log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_API_05_003";//
		String request_type = "GET";
		dateTime = systemService.getSysdatetimeToString();

		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try{
			//log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			//duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			//if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			
			//기간입력
			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				startDate = fromDate;
				endDate   = toDate;
			}	
			//예외(Default D-1 ~ D-1)
			else{
				startDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT);
			    endDate   = DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT);
			}
			
			String parameter = "/"+startDate+"/"+endDate;
		
			log.info("03.정산내역조회 조회 API 호출");
			
			for(int count=0 ; count<Constants.PA_11ST_CONTRACT_CNT; count++){
				
				if(count==0){
					procPaCode = Constants.PA_11ST_ONLINE;
				}else{
					procPaCode = Constants.PA_11ST_BROAD;
				}
				conn = ComUtil.pa11stConnectionSetting(apiInfo, procPaCode, request_type, parameter, ConfigUtil.getString("PA11ST_COM_BASE_SSL"));
				
				// RESPONSE XML 			
				doc = ComUtil.parseXML(conn.getInputStream());
			    descNodes = doc.getElementsByTagName("ns2:seStlDtlLists");
			
				conn.disconnect();
			
				List<ParamMap> responseList = new ArrayList<ParamMap>();
				for(int j=0; j<descNodes.getLength();j++){
			        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
			        	if(node.getNodeName().trim().equals("ns2:seStlDtlList")){
			        		ParamMap pa11stParamMap = new ParamMap();
		        			for(int i=0; i<node.getChildNodes().getLength(); i++){
		            			Node directionList = node.getChildNodes().item(i);
		            			pa11stParamMap.put(directionList.getNodeName().trim(), directionList.getTextContent().trim());
		        			}
		        			responseList.add(pa11stParamMap);
			        	}else{
			        		paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
			        	}
			        }
			    }
			log.info("----------------------------------------------------");
			log.info(responseList.toString());
			log.info("----------------------------------------------------");
//			String	ordNo					= ""; //주문번호
//			String	ordPrdSeq				= ""; //주문순번
//			long	ordQty					= 0;  //주문수량
//			
//			long   	abrdCnDlvCst   			= 0; //해외취소배송비
//			long	addClmDlvCst   			= 0; //반품추가배송비
//			long	bmClmFstDlvCst 			= 0; //도서산간초도배송비
//			long	bmStlDlvCst	   			= 0; //도서산간배송비
//			long	clmDlvCst	   			= 0; //반품/교환 배송비
//			long	clmFstDlvCst 			= 0; //초도배송비
//			long	cupnAmt					= 0; //판매자할인쿠폰
//			
//			Double	deductAmt				= 0.0;  //공제금액
//			long	dlvAmt					= 0;	//선결제배송비
//			long	dlvNo					= 0;	//배송번호
//			String	feeTypeNm				= "";	//서비스이용료정책동의/미동의/별도계약/고정
//			String	memId					= "";	//회원ID
//			String	memNm					= "";	//회원명
//			long	optAmt					= 0;	//옵션가
//			
//			String	ordStlEndDt				= "";   //결제완료일(YYYY/MM/DD)
//			String	pocnfrmDt				= "";	//구매확정일(YYYY/MM/DD)
//			String	prdNm					= "";	//상품명
//			long	prdNo					= 0;	//상품번호
//			Double	selFee					= 0.0;	//수수료
//			String	selFixedFee				= "";	//기본서비스이용료율0%
//			
//			long	selPrc					= 0;	//판매가(판매단가*(주문수량-취소수량))
//			long	selPrcAmt				= 0;	//판매금액합계	판매가+옵션가+배송비
//			long	sellerCupnAmt			= 0;	//판매자기본할인금액
//			long	sellerDfrmAppDlvAmt		= 0;	//지정택배이용료
//			long	sellerDfrmChpCstPrd		= 0;	//칩이용료
//			long	sellerDfrmDeferredAdFee	= 0;	//후불광고비
//			long	sellerDfrmIntfreeFee	= 0;	//무이자할부수수료
//			long	sellerDfrmMultiDscCst	= 0;	//복수구매할인금액
//			long	sellerDfrmOcbAmt		= 0;	//ocb공제금액
//			long	sellerDfrmPntPrd		= 0;	//포인트공제금액
//			
//			String	sellerPrdNo				= "";	//판매자상품코드
//			long	seqNo					= 0;	//NO
//			String	slctPrdOptNm			= "";	//옵션명
//			String	sndEndDt				= "";	//발송완료일(YYYY/MM/DD)
//			Double	stlAmt					= 0.0;	//정산금액(정산금액– 공제금액)
//			String	stlPlnDy				= "";	//송금예정일(YYYY/MM/DD)
//			long	tmallApplyDscAmt		= 0;	//판매자추가할인
//			long	tmallOverDscAmt			= 0;	//11번가추가할인
//			long	totalCount				= 0;	//총갯수

				for(int k=0; k<responseList.size(); k++){
					Pa11stSettlement pa11stSettlement = new Pa11stSettlement();
					
					pa11stSettlement.setGatherDate				(responseList.get(k).getString("pocnfrmDt"));//(startDate);
					pa11stSettlement.setOrdNo					(responseList.get(k).getString("ordNo"));
					pa11stSettlement.setOrdPrdSeq				(responseList.get(k).getString("ordPrdSeq"));
					pa11stSettlement.setOrdQty					(responseList.get(k).getLong("ordQty"));
					
					pa11stSettlement.setAbrdCnDlvCst   			(responseList.get(k).getLong("abrdCnDlvCst"));
					pa11stSettlement.setAddClmDlvCst   			(responseList.get(k).getLong("addClmDlvCst"));
					pa11stSettlement.setBmClmFstDlvCst 			(responseList.get(k).getLong("bmClmFstDlvCst"));
					pa11stSettlement.setBmStlDlvCst	   			(responseList.get(k).getLong("bmStlDlvCst"));
					pa11stSettlement.setClmDlvCst	   			(responseList.get(k).getLong("clmDlvCst"));
					pa11stSettlement.setClmFstDlvCst 			(responseList.get(k).getLong("clmFstDlvCst"));
					pa11stSettlement.setCupnAmt					(responseList.get(k).getLong("cupnAmt"));
					pa11stSettlement.setDebaGtnStlAmt			(responseList.get(k).getLong("debaGtnStlAmt"));
					
					
					pa11stSettlement.setDeductAmt				(responseList.get(k).getLong("deductAmt"));
					pa11stSettlement.setDlvAmt					(responseList.get(k).getLong("dlvAmt"));
					pa11stSettlement.setDlvNo					(responseList.get(k).getLong("dlvNo"));
					pa11stSettlement.setFeeTypeNm				(responseList.get(k).getString("feeTypeNm"));
					pa11stSettlement.setMemId					(responseList.get(k).getString("memId"));
					pa11stSettlement.setMemNm					(responseList.get(k).getString("memNm"));
					pa11stSettlement.setOptAmt					(responseList.get(k).getLong("optAmt"));
					
					pa11stSettlement.setOrdStlEndDt				(responseList.get(k).getString("ordStlEndDt"));
					pa11stSettlement.setPocnfrmDt				(responseList.get(k).getString("pocnfrmDt"));
					pa11stSettlement.setPrdNm					(responseList.get(k).getString("prdNm"));
					pa11stSettlement.setPrdNo					(responseList.get(k).getLong("prdNo"));
					pa11stSettlement.setSelFee					(responseList.get(k).getLong("selFee"));
					pa11stSettlement.setSelFixedFee				(responseList.get(k).getString("selFixedFee"));
					
					pa11stSettlement.setSelPrc					(responseList.get(k).getLong("selPrc"));
					pa11stSettlement.setSelPrcAmt				(responseList.get(k).getLong("selPrcAmt"));
					pa11stSettlement.setSellerCupnAmt			(responseList.get(k).getLong("sellerCupnAmt"));
					pa11stSettlement.setSellerDfrmAppDlvAmt		(responseList.get(k).getLong("sellerDfrmAppDlvAmt"));
					pa11stSettlement.setSellerDfrmChpCstPrd		(responseList.get(k).getLong("sellerDfrmChpCstPrd"));
					pa11stSettlement.setSellerDfrmDeferredAdFee	(responseList.get(k).getLong("sellerDfrmDeferredAdFee"));
					pa11stSettlement.setSellerDfrmIntfreeFee	(responseList.get(k).getLong("sellerDfrmIntfreeFee"));
					pa11stSettlement.setSellerDfrmMultiDscCst	(responseList.get(k).getLong("sellerDfrmMultiDscCst"));
					pa11stSettlement.setSellerDfrmOcbAmt		(responseList.get(k).getLong("sellerDfrmOcbAmt"));
					pa11stSettlement.setSellerDfrmPntPrd		(responseList.get(k).getLong("sellerDfrmPntPrd"));
					
					pa11stSettlement.setSellerPrdNo				(responseList.get(k).getString("sellerPrdNo"));
					pa11stSettlement.setSeqNo					(responseList.get(k).getLong("seqNo")); 
					pa11stSettlement.setSlctPrdOptNm			(responseList.get(k).getString("slctPrdOptNm"));
					pa11stSettlement.setSndEndDt				(responseList.get(k).getString("sndEndDt"));
					pa11stSettlement.setStlAmt					(responseList.get(k).getLong("stlAmt"));
					pa11stSettlement.setStlPlnDy				(responseList.get(k).getString("stlPlnDy"));
					pa11stSettlement.setTmallApplyDscAmt		(responseList.get(k).getLong("tmallApplyDscAmt"));
					pa11stSettlement.setTmallOverDscAmt			(responseList.get(k).getLong("tmallOverDscAmt"));
					pa11stSettlement.setTotalCount				(responseList.get(k).getLong("totalCount"));
					
					pa11stSettlementList.add(pa11stSettlement);
					
				}
			}

			log.info("04.정산데이터 저장");
			rtnMsg = pa11stCommonService.savePa11stSettlementTx(pa11stSettlementList);
			if(!rtnMsg.equals("000000")){
				paramMap.put("code","404");
				paramMap.put("message",rtnMsg);
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			} else {
				paramMap.put("code","200");
				paramMap.put("message","OK");
			}
		}catch (Exception e) {
			/*if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}*/
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
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			/*
			 * if(duplicateCheck.equals("0")){ systemService.checkCloseHistoryTx("end",
			 * prg_id); }
			 */
			
			//log.info("05.저장 완료 API END");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	@ApiOperation(value = "서버체크", notes = "서버체크", httpMethod = "GET", produces = "text/plain")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/serverCheck")
	@ResponseBody
	public String checkPing() throws Exception {
		try {
			long timeS = System.currentTimeMillis();
			if ( !"".equals(ComUtil.NVL(systemService.getSysdatetimeToString())) ) {
				long timeE = System.currentTimeMillis();
				log.info("== check db time == :: " + Long.toString(timeE-timeS));
				return "true";
			}
		} catch (Exception e) {	 }
		return "false";
	}
	
	@ApiOperation(value = "시간체크", notes = "시간체크", httpMethod = "GET", produces = "text/plain")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/dateCheck")
	@ResponseBody
	public String checkDate() throws Exception {
		log.info("== 배포일자 2018-09-12 오전8:30 ==");
		return "true";
	}
}