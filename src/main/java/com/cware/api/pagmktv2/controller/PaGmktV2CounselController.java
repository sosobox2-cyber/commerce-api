package com.cware.api.pagmktv2.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.cware.netshopping.common.util.PostUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pacommon.counsel.service.PaCounselService;
import com.cware.netshopping.pagmkt.counsel.service.PaGmktCounselService;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;
import com.cware.netshopping.pagmkt.util.rest.PaGmktCounselRest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pagmktv2/counsel", description="공지사항/고객게시판")
@Controller("com.cware.api.pagmktv2.PaGmktV2CounselController")
@RequestMapping(value="/pagmktv2/counsel")
public class PaGmktV2CounselController extends AbstractController {


	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "com.cware.netshopping.common.util.PostUtil")
	private PostUtil postUtil;
	
	@Resource(name = "pacommon.counsel.paCounselService")
	private PaCounselService paCounselService;
	
	@Resource(name = "pagmkt.counsel.paGmktCounselService")
	private PaGmktCounselService paGmktCounselService;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	private PaGmktCommonUtil CommonUtil;
		
	/**
	 * ESM 공지사항 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "ESM 공지사항 조회", notes = "ESM 공지사항 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-notice-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postNoticeList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCounselRest();
		String apiCode = "IF_PAGMKTAPI_V2_05_001";            
		String procPaCode = null; 
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		procPaCode = "21";
		paramMap.put("paCode", procPaCode);
		paramMap.put("FROM_DATE", DateUtil.addDay(systemService.getSysdatetime(),  0 ));
		paramMap.put("TO_DATE", DateUtil.addDay(systemService.getSysdatetime(),  +30 ));

		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
		
			//= Step 2) 통신
			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			
			/*JSONArray resArray = (JSONArray) resMap.get("Data");
			List<Map<String, Object>> responseList = getListMapFromJsonArray (resArray);
			for (int i=0; i<responseList.size(); i++) {
				
			}*/
			//= Step 3) 통신후 resMap 파싱 & setter
	    	
	    	//= Step 4) table insert
	    	//paCounselService.savePaQnaTx(paqnamomentList, msgGb);
		    	
			
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
			log.info("===== ESM 공지사항 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 고객 게시판 문의 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	//IF_PAGMKTAPI_V2_05_002_L
	@ApiOperation(value = "고객 게시판 문의 조회(IF_PAGMKTAPI_V2_05_002_L)", notes = "고객 게시판 문의 조회(IF_PAGMKTAPI_V2_05_002_L)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-counsel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postCounsel(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		
		String paCode[] = {"21", "22"};
		for(String pa : paCode){
			postCounselList(request, pa);
		}
		
		postEmergency(request);
		
		paramMap.put("code", "200");
		paramMap.put("message", "OK");
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 고객 게시판 문의 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "고객 게시판 문의 조회(IF_PAGMKTAPI_V2_05_002)", notes = "고객 게시판 문의 조회(IF_PAGMKTAPI_V2_05_002)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-counsel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postCounselList(HttpServletRequest request,
			@RequestParam(value="paCode", required=true) String paCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCounselRest();
		String apiCode = "IF_PAGMKTAPI_V2_05_002";            
		String duplicateCheck = "";
		Paqnamoment paqnamoment = null;
		List<Paqnamoment> paqnamomentList = null;
		String msgGb = "00"; //일반상담
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAE");
		paramMap.put("paCode", paCode);

		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
		
			//= Step 2) 통신
			for (int count = 0; count < Constants.PA_GMKT_CONTRACT_CNT + 1; count++) {
				paqnamomentList = new ArrayList<Paqnamoment>();
				paramMap.put("count", count);
				if (count == 0) {
					paramMap.put("siteGb", "PAG");//G마켓 게시판
				} else if (count == 1) {
					paramMap.put("siteGb", "PAA");//옥션 게시판
				} else if (count == 2) {
					paramMap.put("siteGb", "PAA");//옥션쪽지
				}
				
				String response = restUtil.getConnection(rest,  paramMap);
				Map<String,Object> resMap = ComUtil.splitJson(response);
				
				//= Step 3) 통신후 resMap 파싱 & setter
				if (resMap != null) {
					List<HashMap<String,Object>> responseList = (List)resMap.get("Data");
					
					for (int i=0; i<responseList.size(); i++) {
						paqnamoment = new Paqnamoment();
						paqnamoment.setPaGroupCode(paramMap.getString("siteGb").equals("PAG") ? "02" : "03");
						paqnamoment.setPaCode(paramMap.getString("paCode"));
						paqnamoment.setPaCounselNo(responseList.get(i).get("messageNo").toString());
						paqnamoment.setCounselDate(DateUtil.toTimestamp(responseList.get(i).get("receiveDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));
						
						//paqnamoment.setTitle(responseList.get(i).get("title").toString());
						paqnamoment.setTitle(ComUtil.NVL(responseList.get(i).get("title").toString(), "제목없음"));
						
						//내용제한
						paqnamoment.setQuestComment( ComUtil.NVL(ComUtil.subStringBytes( responseList.get(i).get("details").toString().replaceAll("/br", "\n"), 2000 ), "내용없음"));
						
						if (responseList.get(i).get("contractType").toString().substring(0, 2).equals("상품")) {
							paqnamoment.setCounselGb("01");
						} else if (responseList.get(i).get("contractType").toString().substring(0, 2).equals("배송")) {
							paqnamoment.setCounselGb("02");
						} else if (responseList.get(i).get("contractType").toString().equals("반품/환불") || responseList.get(i).get("contractType").toString().equals("반품/취소/환불") 
								    || responseList.get(i).get("contractType").toString().equals("취소/반품/환불")) {
							paqnamoment.setCounselGb("03");
						} else if (responseList.get(i).get("contractType").toString().equals("취소")) {
							paqnamoment.setCounselGb("04");
						} else if (responseList.get(i).get("contractType").toString().equals("교환")) {
							paqnamoment.setCounselGb("05");
						} else if (responseList.get(i).get("contractType").toString().equals("기타")) {
							paqnamoment.setCounselGb("06");
						}
						paqnamoment.setPaGoodsCode(responseList.get(i).get("siteGoodsNo").toString());
						paqnamoment.setPaGoodsDtCode("");//제휴상품 단품코드
						paqnamoment.setPaOrderNo(responseList.get(i).get("orderNo").toString());
						paqnamoment.setOrderYn(responseList.get(i).get("orderNo").toString().equals("-")?"0":"1");
						//paqnamoment.setDisplayYn(resMap.get("siteGoodsNo").toString());//G마켓은 없음
						//paqnamoment.setPaCustNo(resMap.get("siteGoodsNo").toString());//G마켓은 없음
						if (responseList.get(i).get("inquirerName") == null) {
							paqnamoment.setCustName("");
						} else {
							paqnamoment.setCustName(responseList.get(i).get("inquirerName").toString());
						}
						if (responseList.get(i).get("inquirerPhone") == null) {
							paqnamoment.setCustTel("");
						} else {
							paqnamoment.setCustTel(responseList.get(i).get("inquirerPhone").toString().trim());
						}
						//paqnamoment.setReceiptDate("");//결제일시 G마켓은 없음
						paqnamoment.setToken(responseList.get(i).get("token").toString());
						//paqnamoment.setEsmGoodsCode(responseList.get(i).get("goodsNo").toString());
						if (count == 0 || count == 1) {
							paqnamoment.setMsgGb("00"); //일반상담 Q&A
						} else if (count == 2) {
							paqnamoment.setMsgGb("20"); //옥션 쪽지
						}
						paqnamoment.setInsertId(paramMap.getString("siteGb"));
						paqnamoment.setModifyId(paramMap.getString("siteGb"));
						paqnamoment.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
						paqnamoment.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
						
						paqnamomentList.add(paqnamoment);
					}
			    	
			    	//= Step 4) table insert
			    	paCounselService.savePaQnaTx(paqnamomentList, msgGb);
				}
			}
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
			log.info("===== 고객 게시판 문의 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 고객 게시판 문의 답변
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "고객 게시판 문의 답변", notes = "고객 게시판 문의 답변", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-counsel-list-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postCounselListProc(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		
		String paCode[] = {"21", "22"};
		for(String pa : paCode){
			postCounselProc(request, pa);
		}
		
		postEmergencyListProc(request);
		
		paramMap.put("code", "200");
		paramMap.put("message", "OK");
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 고객 게시판 문의 답변
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "고객 게시판 문의 답변(IF_PAGMKTAPI_V2_05_003)", notes = "고객 게시판 문의 답변(IF_PAGMKTAPI_V2_05_003)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-counsel-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postCounselProc(HttpServletRequest request,
			@RequestParam(value="paCode", required=true) String paCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCounselRest();
		String apiCode = "IF_PAGMKTAPI_V2_05_003";            
		String duplicateCheck = "";
		PaqnamVO paQna = null;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAE");
		paramMap.put("paCode", paCode);
		paramMap.put("msgGb", "00");

		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			for (int count = 0; count < Constants.PA_GMKT_CONTRACT_CNT + 1; count++) {
				
				if (count == 0) {
					paramMap.put("siteGb", "PAG");
				} else if (count == 1) {
					paramMap.put("siteGb", "PAA");
				} else if (count == 2) {
					paramMap.put("msgGb", "20");
				}
				
				List<PaqnamVO> ansList = paGmktCounselService.selectPaGmktAnsQna(paramMap);
				
				if (ansList.size() > 0) {
					for (int i=0; i<ansList.size(); i++) {
						try {
							paramMap.put("title", ansList.get(i).getTitle());
							paramMap.put("procNote", ansList.get(i).getProcNote());
							paramMap.put("messageNo", ansList.get(i).getPaCounselNo());
							paramMap.put("token", ansList.get(i).getToken());
					
							//= Step 2) 통신
							String response = restUtil.getConnection(rest,  paramMap);
							Map<String,Object> resMap = ComUtil.splitJson(response);
							
							//= Step 3) 통신후 resMap 파싱 & setter
							paQna = ansList.get(i);
							paQna.setModifyId(paramMap.getString("siteGb"));
							paQna.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
							paQna.setProcGb("40"); //= 완료
							paQna.setTitle("");
					    	
					    	//= Step 4) table insert
							paGmktCounselService.savePaGmktQnaTransTx(paQna);
							
						} catch (Exception e) {
							if(StringUtils.contains(e.getMessage(), "구매자에 의해 삭제된")){
								
								paQna = ansList.get(i);
								paQna.setModifyId(paramMap.getString("siteGb"));
								paQna.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
								paQna.setProcGb("40"); //= 완료
								paQna.setTitle(e.getMessage());
						    	
						    	//= Step 4) table insert
								paGmktCounselService.savePaGmktQnaTransTx(paQna);
								
							} else {
								systemService.insertPassingErrorToApitracking(paramMap, e);
								continue;
							}
						}
						
					}
				}
			}
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
			log.info("===== 고객 게시판 문의 답변 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 고객 긴급메시지 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "고객 긴급메시지 조회", notes = "고객 긴급메시지 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-emergency", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postEmergency(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		
		String paCode[] = {"21", "22"};
		for(String pa : paCode){
			postEmergencyList(request, pa);
		}
		
		paramMap.put("code", "200");
		paramMap.put("message", "OK");
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 고객 긴급메시지 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "고객 긴급메시지 조회(IF_PAGMKTAPI_V2_05_004)", notes = "고객 긴급메시지 조회(IF_PAGMKTAPI_V2_05_004)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-emergency-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postEmergencyList(HttpServletRequest request,
			@RequestParam(value="paCode", required=true) String paCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCounselRest();
		String apiCode = "IF_PAGMKTAPI_V2_05_004";            
		String duplicateCheck = "";
		Paqnamoment paqnamoment = null;
		List<Paqnamoment> paqnamomentList = null;
		String msgGb = "10"; //일반상담
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAE");
		paramMap.put("paCode", paCode);

		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
		
			//= Step 2) 통신
			for (int count = 0; count < Constants.PA_GMKT_CONTRACT_CNT; count++) {
				paqnamomentList = new ArrayList<Paqnamoment>();
				if (count == 0) {
					paramMap.put("siteGb", "PAG");
				} else if (count == 1) {
					paramMap.put("siteGb", "PAA");
					paramMap.put("apiCode", "IF_PAGMKTAPI_V2_05_006");
				}
				
				String response = restUtil.getConnection(rest,  paramMap);
				Map<String,Object> resMap = ComUtil.splitJson(response);
				
				//= Step 3) 통신후 resMap 파싱 & setter
				if (resMap != null) {
					List<HashMap<String,Object>> responseList = (List)resMap.get("Data");
					
					//G마켓은 데이터가 없으면 resMap이 null, 옥션은 데이터가 없으면 resMap null이 아님. Data값을 리스화시키면 null이므로 에러
					if(responseList == null) {
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
					}
					
					if(paramMap.getString("siteGb").equals("PAG")) {
						for (int i=0; i<responseList.size(); i++) {
							paqnamoment = new Paqnamoment();
							paqnamoment.setPaGroupCode(paramMap.getString("siteGb").equals("PAG") ? "02" : "03");
							paqnamoment.setPaCode(paramMap.getString("paCode"));
							paqnamoment.setPaCounselNo(responseList.get(i).get("emerMessageNo").toString());
							paqnamoment.setCounselDate(DateUtil.toTimestamp(responseList.get(i).get("receiveDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));
							
							//paqnamoment.setTitle(responseList.get(i).get("title").toString());
							paqnamoment.setTitle(ComUtil.NVL(responseList.get(i).get("title").toString(), "제목없음"));
							
							//내용제한
							paqnamoment.setQuestComment( ComUtil.NVL(ComUtil.subStringBytes( responseList.get(i).get("details").toString().replaceAll("/br", "\n"), 2000 ), "내용없음"));
							
							paqnamoment.setCounselGb("08");
							paqnamoment.setPaGoodsCode(responseList.get(i).get("siteGoodsNo").toString());
							paqnamoment.setPaGoodsDtCode("");//제휴상품 단품코드
							paqnamoment.setPaOrderNo(responseList.get(i).get("orderNo").toString());
							paqnamoment.setOrderYn(responseList.get(i).get("orderNo").toString().equals("-")?"0":"1");
							//paqnamoment.setDisplayYn(resMap.get("siteGoodsNo").toString());//G마켓은 없음
							//paqnamoment.setPaCustNo(resMap.get("siteGoodsNo").toString());//G마켓은 없음
							//paqnamoment.setCustName(responseList.get(i).get("inquirerName").toString());
							//paqnamoment.setCustTel(responseList.get(i).get("inquirerPhone").toString());
							//paqnamoment.setReceiptDate("");//결제일시 G마켓은 없음
							paqnamoment.setToken(responseList.get(i).get("token").toString());
							//paqnamoment.setEsmGoodsCode(responseList.get(i).get("goodsNo").toString());
							paqnamoment.setMsgGb("10"); //긴급메세지 Q&A
							paqnamoment.setInsertId(paramMap.getString("siteGb"));
							paqnamoment.setModifyId(paramMap.getString("siteGb"));
							paqnamoment.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
							paqnamoment.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
							
							paqnamomentList.add(paqnamoment);
						}
					} else {
						for (int i=0; i<responseList.size(); i++) {
							paqnamoment = new Paqnamoment();
							paqnamoment.setPaGroupCode(paramMap.getString("siteGb").equals("PAG") ? "02" : "03");
							paqnamoment.setPaCode(paramMap.getString("paCode"));
							paqnamoment.setPaCounselNo(responseList.get(i).get("EmerMessageNo").toString());
							paqnamoment.setCounselDate(DateUtil.toTimestamp(responseList.get(i).get("ReceiveDate").toString().replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss"));
							
							//paqnamoment.setTitle(responseList.get(i).get("Title").toString());
							paqnamoment.setTitle(ComUtil.NVL(responseList.get(i).get("Title").toString(), "제목없음"));

							//내용제한
							paqnamoment.setQuestComment( ComUtil.NVL(ComUtil.subStringBytes( responseList.get(i).get("Details").toString().replaceAll("/br", "\n"), 2000 ), "내용없음"));
							
							
							paqnamoment.setCounselGb("08");
							paqnamoment.setPaGoodsCode(responseList.get(i).get("SiteGoodsNo").toString());
							paqnamoment.setPaGoodsDtCode("");//제휴상품 단품코드
							paqnamoment.setPaOrderNo(responseList.get(i).get("OrderNo").toString());
							paqnamoment.setOrderYn(responseList.get(i).get("OrderNo").toString().equals("-")?"0":"1");
							//paqnamoment.setDisplayYn(resMap.get("siteGoodsNo").toString());//G마켓은 없음
							//paqnamoment.setPaCustNo(resMap.get("siteGoodsNo").toString());//G마켓은 없음
							//paqnamoment.setCustName(responseList.get(i).get("inquirerName").toString());
							//paqnamoment.setCustTel(responseList.get(i).get("inquirerPhone").toString());
							//paqnamoment.setReceiptDate("");//결제일시 G마켓은 없음
							//paqnamoment.setToken(responseList.get(i).get("token").toString());
							//paqnamoment.setEsmGoodsCode(responseList.get(i).get("goodsNo").toString());
							paqnamoment.setMsgGb("10"); //긴급메세지 Q&A
							paqnamoment.setInsertId(paramMap.getString("siteGb"));
							paqnamoment.setModifyId(paramMap.getString("siteGb"));
							paqnamoment.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
							paqnamoment.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
							
							paqnamomentList.add(paqnamoment);
						}
					}
			    	
			    	//= Step 4) table insert
					paCounselService.savePaQnaTx(paqnamomentList, msgGb);
				}
			}
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
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
			log.info("===== 고객 긴급메세지 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 고객 긴급메시지 답변
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "고객 긴급메시지 답변", notes = "고객 긴급메시지 답변", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-emergency-list-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postEmergencyListProc(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		
		String paCode[] = {"21", "22"};
		for(String pa : paCode){
			postEmergencyProc(request, pa);
		}
		
		paramMap.put("code", "200");
		paramMap.put("message", "OK");
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 고객 긴급메세지 답변
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "고객 긴급메시지 답변(IF_PAGMKTAPI_V2_05_005)", notes = "고객 긴급메시지 답변(IF_PAGMKTAPI_V2_05_005)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-emergency-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postEmergencyProc(HttpServletRequest request,
			@RequestParam(value="paCode", required=true) String paCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCounselRest();
		String apiCode = "IF_PAGMKTAPI_V2_05_005";            
		String duplicateCheck = "";
		PaqnamVO paQna = null;
		String msgGb = "10";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("paCode", paCode);
		paramMap.put("msgGb", msgGb);

		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			for (int count = 0; count < Constants.PA_GMKT_CONTRACT_CNT; count++) {
				paramMap.put("count", count);
				if (count == 0) {
					paramMap.put("siteGb", "PAG");
				} else if (count == 1) {
					paramMap.put("siteGb", "PAA");
					paramMap.put("apiCode", "IF_PAGMKTAPI_V2_05_007");
				}
				
				List<PaqnamVO> ansList = paGmktCounselService.selectPaGmktAnsQna(paramMap);
				
				if (ansList.size() > 0) {
					for (int i=0; i<ansList.size(); i++) {
						paramMap.put("procNote", ansList.get(i).getProcNote());
						paramMap.put("emerMessageNo", ansList.get(i).getPaCounselNo());
						paramMap.put("token", ansList.get(i).getToken());
				
						//= Step 2) 통신
						String response = restUtil.getConnection(rest,  paramMap);
						Map<String,Object> resMap = ComUtil.splitJson(response);
						
						//= Step 3) 통신후 resMap 파싱 & setter
						paQna = ansList.get(i);
						paQna.setModifyId(paramMap.getString("siteGb"));
						paQna.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
						paQna.setProcGb("40"); //= 완료
						paQna.setTitle("");
				    	
				    	//= Step 4) table insert
						paGmktCounselService.savePaGmktQnaTransTx(paQna);
					}
				}
			}
			
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
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
			log.info("===== 고객 긴급메세지 답변 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
}
