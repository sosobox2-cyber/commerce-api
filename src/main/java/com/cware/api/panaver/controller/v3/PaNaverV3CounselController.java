package com.cware.api.panaver.controller.v3;

import java.security.Security;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pa11st.counsel.service.Pa11stCounselService;
import com.cware.netshopping.pacommon.counsel.service.PaCounselService;
import com.cware.netshopping.panaver.counsel.service.PaNaverCounselService;
import com.cware.netshopping.panaver.v3.domain.CustCounsel;
import com.cware.netshopping.panaver.v3.domain.CustCounselList;
import com.cware.netshopping.panaver.v3.domain.GoodsCounsel;
import com.cware.netshopping.panaver.v3.domain.GoodsCounselList;
import com.cware.netshopping.panaver.v3.util.PaNaverV3ConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller("com.cware.api.panaver.v3.PaNaverV3CounselController")
@RequestMapping(value="/panaver/v3/counsel")
public class PaNaverV3CounselController extends AbstractController{
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	@Resource(name = "pacommon.counsel.paCounselService")
	private PaCounselService paCounselService;
	
	@Resource(name = "panaver.counsel.paNaverCounselService")
	private PaNaverCounselService paNaverCounselService;

	@Resource(name = "pa11st.counsel.pa11stCounselService")
	private Pa11stCounselService pa11stCounselService;
	
	@Autowired
	private PaNaverV3ConnectUtil paNaverV3ConnectUtil;

	static { //ComUtil 오류없이 실행
		Security.addProvider(new BouncyCastleProvider());
	}
	
	/**
	 * 상품 문의 목록 조회
	 * 
	 * @param request
	 * @param page
	 * @param size
	 * @param answered
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goods-counsel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> questionAnswerListNaver(HttpServletRequest httpServletRequest,
			@RequestParam(value="page", required=false, defaultValue = "1") int page,
			@RequestParam(value="size", required=false, defaultValue = "100") int size,
			@RequestParam(value="answered", required=false, defaultValue = "false") String answered,
			@RequestParam(value="fromDate", required=false, defaultValue = "") String fromDate,			
			@RequestParam(value="toDate", required=false, defaultValue = "") String toDate) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PANAVERAPI_V3_02_001";
		String duplicateCheck = "";
		List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
		Paqnamoment paqnamoment = new Paqnamoment();
		Map<String, Object> goodsCounselListMap = new HashMap<String, Object>() ;
		String msgGb = "00";
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = systemService.getSysdatetimeToString();
		Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
		boolean isRetrieve = false;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", dateTime);
		paramMap.put("siteGb", "PANAVER");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		queryParameters.put("page", Integer.toString(page));
		queryParameters.put("size", Integer.toString(size)); // 최대 100건
		queryParameters.put("answered", answered);
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		GoodsCounselList goodsCounselList = new GoodsCounselList();

		log.info("======= 상품 문의 목록 조회 API Start =======");
		
		try{
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			//기간입력
			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				from.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(fromDate));
				to.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(toDate));
			}	
			//예외(Default D-2 ~ D)
			else{
				from.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateTime));
				from.add(Calendar.DATE, -2);
				to.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateTime));
			}
			
			queryParameters.put("fromDate", dateFormat.format(from.getTime()));
			queryParameters.put("toDate", dateFormat.format(to.getTime()));

			// 상품 문의 목록 조회 통신
			goodsCounselListMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, null);	
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			goodsCounselList = objectMapper.convertValue(goodsCounselListMap, GoodsCounselList.class);
			
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			if(goodsCounselList.getGoodsCounselList().size() > 0) {
				for(GoodsCounsel goodsCounsel : goodsCounselList.getGoodsCounselList()) {
					String note = ComUtil.NVL(goodsCounsel.getQuestion());
					int contentLength = note.getBytes("UTF-8").length;
					
					Date counselDate = Date.from(ZonedDateTime.parse(goodsCounsel.getCreateDate()).toInstant());
					
					paqnamoment = new Paqnamoment();
					paqnamoment.setPaGroupCode("04");
					paqnamoment.setPaCode(Constants.PA_NAVER_CODE);
					paqnamoment.setPaCounselNo(ComUtil.objToStr(goodsCounsel.getQuestionId()));
					paqnamoment.setCounselDate(DateUtil.toTimestamp(dateFormat.format(counselDate), "yyyy-MM-dd HH:mm:ss"));
					paqnamoment.setTitle("네이버 상품Q&A");
					if(contentLength >= 4000){
						paqnamoment.setQuestComment(ComUtil.subStringBytes(note, 3980, 3) + "...(내용잘림)");
					}else{
						paqnamoment.setQuestComment(goodsCounsel.getQuestion());
					}
					paqnamoment.setCounselGb("01");
					paqnamoment.setPaGoodsCode(String.valueOf(goodsCounsel.getProductId()));
					paqnamoment.setPaGoodsDtCode("");
					paqnamoment.setPaOrderNo("");
					paqnamoment.setOrderYn("");
					paqnamoment.setDisplayYn("");
					paqnamoment.setPaCustNo("");
					paqnamoment.setCustTel("");
					paqnamoment.setReceiptDate(null);
					paqnamoment.setMsgGb("00"); //일반상담 Q&A
					paqnamoment.setInsertId("PANAVER");
					paqnamoment.setModifyId("PANAVER");
					paqnamoment.setInsertDate(sysdateTime);
					paqnamoment.setModifyDate(sysdateTime);

					paqnamomentList.add(paqnamoment);
				}
				rtnMsg = paCounselService.savePaQnaTx(paqnamomentList, msgGb);
				
				if(!goodsCounselList.isLast()) { // 마지막 페이지 여부 확인
					page += 1;
					isRetrieve = true;
				}
				
				if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}else {
					paramMap.put("code","500");
					paramMap.put("message",rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			} else {
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("errors.no.select"));
			}
			
			
		} catch (TransApiException ex) {
			paramMap.put("code", "500");
			paramMap.put("message", ex.getMessage());
			log.error(paramMap.getString("message"), ex);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e){
			paNaverV3ConnectUtil.dealException(e, paramMap);
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally{
			try{
				paNaverV3ConnectUtil.dealSuccess(paramMap, httpServletRequest);
			} catch(Exception e){
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			
			// 다음 페이지 조회
			if(isRetrieve) questionAnswerListNaver(httpServletRequest, page, size, answered, fromDate, toDate);
			log.info("======= 상품 문의 목록 조회API End =======");	
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품 문의 답변 등록
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goods-counsel-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> manageQuestionAnswerNaver(HttpServletRequest httpServletRequest) throws Exception{

		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PANAVERAPI_V3_02_002";
		String duplicateCheck = "";
		String dateTime = systemService.getSysdatetimeToString();
		StringBuffer sb = new StringBuffer();
		PaqnamVO paQna = null;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", dateTime);
		paramMap.put("siteGb", "PANAVER");
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// Path Parameters
		String pathParameters = "";
		
		// Body 세팅
		ParamMap apiDataObject = new ParamMap();
		
		log.info("======= 상품 문의 답변 등록 API Start =======");
		
		try{
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			List<PaqnamVO> ansList = paNaverCounselService.selectPaNaverAnsQna();
			
			if(ansList.size() < 1) {
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("pa.not_exists_process_list"));
			}
			
			for(int i=0; i<ansList.size(); i++){
				try {
					pathParameters = ansList.get(i).getPaCounselNo();
					apiDataObject.put("commentContent", ansList.get(i).getProcNote());
					
					// 상품 문의 답변 등록 통신
					paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);	
					
					paQna = ansList.get(i);
					paQna.setModifyId("PANAVER");
					paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paQna.setProcGb("40"); // 완료
					paQna.setTitle("");
					
					pa11stCounselService.savePa11stQnaTransTx(paQna);
					
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
					
				} catch (TransApiException ex) {
					sb.append(ansList.get(i).getPaCounselNo() + " : " + ex.getMessage() + "/ ");
				}
			}
		}catch(Exception e){
			paNaverV3ConnectUtil.dealException(e, paramMap);
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally{
			try{
				if(sb.length() != 0) {
					paramMap.put("message", sb.toString());
					paramMap.put("code", "500");
				}
				paNaverV3ConnectUtil.dealSuccess(paramMap, httpServletRequest);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("======= 상품 문의 답변 등록 API End =======");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 고객 문의 조회
	 * 
	 * @param request
	 * @param page
	 * @param size
	 * @param startSearchDate
	 * @param endSearchDate
	 * @param answered
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cust-counsel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> customerInquiryListNaver(HttpServletRequest httpServletRequest, 
			@RequestParam(value = "page", required = false, defaultValue = "1" ) int page,
			@RequestParam(value = "size", required = false, defaultValue = "100" ) int size,
			@RequestParam(value = "startSearchDate", required = false, defaultValue = "" ) String startSearchDate,
			@RequestParam(value = "endSearchDate", required = false, defaultValue = "" ) String endSearchDate, 
			@RequestParam(value = "answered", required = false, defaultValue = "false") String answered) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		
		String duplicateCheck = "";
		String apiCode = "IF_PANAVERAPI_V3_04_001";
		List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
		Paqnamoment paqnamoment = new Paqnamoment();
		Map<String, Object> custCounselListMap = new HashMap<String, Object>() ;
		String rtnMsg = Constants.SAVE_SUCCESS;
		String msgGb = "30";
		String time = systemService.getSysdatetimeToString();
		Timestamp dateTime = DateUtil.toTimestamp(time);
		boolean isRetrieve = false;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("startDate", time);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		queryParameters.put("page", Integer.toString(page));
		queryParameters.put("size", Integer.toString(size)); // 10 ~ 200 건
		queryParameters.put("answered", answered);
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		CustCounselList custCounselList = new CustCounselList();
		
		log.info("======= 고객 문의 조회 API Start =======");
		
		try{
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//기간입력
			if(!(startSearchDate.isEmpty() || startSearchDate.equals("")) && !(endSearchDate.isEmpty() || endSearchDate.equals(""))){
				from.setTime(new SimpleDateFormat("yyyyMMdd").parse(startSearchDate));
				to.setTime(new SimpleDateFormat("yyyyMMdd").parse(endSearchDate));
			}	
			//예외(Default D-2 ~ D)
			else{
				from.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(time));
				from.add(Calendar.DATE, -2);
				to.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(time));
			}
			
			queryParameters.put("startSearchDate", dateFormat.format(from.getTime()));
			queryParameters.put("endSearchDate", dateFormat.format(to.getTime()));
			
			// 고객 문의 조회 통신
			custCounselListMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, null);
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			custCounselList = objectMapper.convertValue(custCounselListMap, CustCounselList.class);
			
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			if(custCounselList.getCustCounselList().size() > 0) {
				for(CustCounsel custCounsel : custCounselList.getCustCounselList()){
					String note = ComUtil.NVL(custCounsel.getInquiryContent());
					int contentLength = note.getBytes("UTF-8").length;
					
					Date counselDate = Date.from(ZonedDateTime.parse(custCounsel.getInquiryRegistrationDateTime()).toInstant());

					paqnamoment = new Paqnamoment();
					paqnamoment.setPaGroupCode("04");
					paqnamoment.setPaCode(Constants.PA_NAVER_CODE);
					paqnamoment.setPaCounselNo(ComUtil.objToStr(custCounsel.getInquiryNo()));
					paqnamoment.setCounselDate(DateUtil.toTimestamp(dateFormat.format(counselDate), "yyyy-MM-dd HH:mm:ss"));
					paqnamoment.setTitle(ComUtil.NVL(custCounsel.getTitle(), "네이버 스마트스토어 상담 문의"));
					if(contentLength > 200){								
						paqnamoment.setQuestComment(ComUtil.subStringBytes("["+custCounsel.getTitle()+"]" + note, 185) + "…(내용잘림)");
					}else{
						paqnamoment.setQuestComment("["+custCounsel.getTitle()+"]" + custCounsel.getInquiryContent());
					}
					paqnamoment.setCounselGb(custCounsel.getCategory());
					paqnamoment.setPaGoodsCode(custCounsel.getProductNo());
					paqnamoment.setPaGoodsDtCode(custCounsel.getProductOrderOption());
					paqnamoment.setPaOrderNo(custCounsel.getOrderId());
					paqnamoment.setOrderYn("");
					paqnamoment.setDisplayYn("");
					paqnamoment.setPaCustNo("");
					paqnamoment.setCustTel("");
					paqnamoment.setReceiptDate(null);
					paqnamoment.setMsgGb("30"); //판매자 문의
					paqnamoment.setInsertId("PANAVER");
					paqnamoment.setModifyId("PANAVER");
					paqnamoment.setInsertDate(dateTime);
					paqnamoment.setModifyDate(dateTime);

					paqnamomentList.add(paqnamoment);
					paramMap.put("code", "00");
				} 
				rtnMsg = paCounselService.savePaQnaTx(paqnamomentList, msgGb);
				
				if(!custCounselList.isLast()) { // 마지막 페이지 여부 확인
					page += 1;
					isRetrieve = true;
				}
				
				if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}else {
					paramMap.put("code","500");
					paramMap.put("message",rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			} else {
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("errors.no.select"));
			}
		} catch (TransApiException ex) {
			paramMap.put("code", "500");
			paramMap.put("message", ex.getMessage());
			log.error(paramMap.getString("message"), ex);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e){
			paNaverV3ConnectUtil.dealException(e, paramMap);
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally{
			try{
				paNaverV3ConnectUtil.dealSuccess(paramMap, httpServletRequest);
			} catch(Exception e){
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));

			// 다음 페이지 조회
			if(isRetrieve) customerInquiryListNaver(httpServletRequest,page, size, startSearchDate, endSearchDate, answered);
			log.info("=======  고객 문의 조회 API End =======");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 고객 문의 답변 등록
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cust-counsel-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> answerCustomerInquiryNaver(HttpServletRequest httpServletRequest) throws Exception{

		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PANAVERAPI_V3_04_002";
		String duplicateCheck = "";
		String dateTime = systemService.getSysdatetimeToString();
		StringBuffer sb = new StringBuffer();
		PaqnamVO paQna = null;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", dateTime);
		paramMap.put("siteGb", "PANAVER");
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// Path Parameters
		String pathParameters = "";
		
		// Body 세팅
		ParamMap apiDataObject = new ParamMap();
		
		log.info("======= 고객 문의 답변 등록 API Start =======");
		
		try{
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			List<PaqnamVO> ansList = paNaverCounselService.selectPaNaverCustAnsQna();
			
			if(ansList.size() < 1) {
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("pa.not_exists_process_list"));
			}
			
			for(int i=0; i<ansList.size(); i++){
				try {
					pathParameters = ansList.get(i).getPaCounselNo();
					apiDataObject.put("answerComment", ansList.get(i).getProcNote());
					apiDataObject.put("answerTemplateId", "1");
					
					// 고객 문의 답변 등록 통신
					paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);
					
					paQna = ansList.get(i);
					paQna.setModifyId("PANAVER");
					paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paQna.setProcGb("40"); // 완료
					paQna.setTitle("");
					
					pa11stCounselService.savePa11stQnaTransTx(paQna);
					
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
					
				} catch (TransApiException ex) {
					
					if("ERR-NC-101004".equals(ex.getCode()) || "ERR-NC-101005".equals(ex.getCode()) 
							|| "ERR-NC-101010".equals(ex.getCode())) { // 문의가 없거나 삭제된 경우 / 문의가 유효하지 않은 경우 / 이미 답변이 존재하는 경우
						paQna = ansList.get(i);
						paQna.setModifyId("PANAVER");
						paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paQna.setProcGb("40"); // 완료
						paQna.setTitle(ex.getMessage());
						
						pa11stCounselService.savePa11stQnaTransTx(paQna);
						
						paramMap.put("code", "200");
						paramMap.put("message", "OK");
					} else {
						sb.append(ansList.get(i).getPaCounselNo() + " : " + ex.getMessage() + "/ ");
					}
				}
			}
		}catch(Exception e){
			paNaverV3ConnectUtil.dealException(e, paramMap);
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally{
			try{
				if(sb.length() != 0) {
					paramMap.put("message", sb.toString());
					paramMap.put("code", "500");
				}
				paNaverV3ConnectUtil.dealSuccess(paramMap, httpServletRequest);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("======= 고객 문의 답변 등록 API End =======");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
}
