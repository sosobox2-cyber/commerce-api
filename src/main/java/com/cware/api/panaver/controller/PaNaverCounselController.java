package com.cware.api.panaver.controller;

import java.security.Security;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.AccessCredentialsType;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.AnswerCustomerInquiryRequest;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.AnswerCustomerInquiryRequestE;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.AnswerCustomerInquiryResponseE;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.GetCustomerInquiryListRequest;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.GetCustomerInquiryListRequestE;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.GetCustomerInquiryListResponseE;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.InquiryType;
import com.cware.api.panaver.order.seller.NaverSignature;
import com.cware.api.panaver.product.type.GetQuestionAnswerListRequestType;
import com.cware.api.panaver.product.type.GetQuestionAnswerListResponseType;
import com.cware.api.panaver.product.type.ManageQuestionAnswerRequestType;
import com.cware.api.panaver.product.type.ManageQuestionAnswerResponseType;
import com.cware.api.panaver.product.type.QuestionAnswerReturnType;
import com.cware.api.panaver.product.type.QuestionAnswerService;
import com.cware.api.panaver.product.type.QuestionAnswerServicePortType;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pa11st.counsel.service.Pa11stCounselService;
import com.cware.netshopping.pacommon.counsel.service.PaCounselService;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.panaver.counsel.service.PaNaverCounselService;

@Controller("com.cware.api.panaver.PaNaverCounselController")
@RequestMapping(value="/panaver/counsel")
public class PaNaverCounselController extends AbstractController{

	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	@Resource(name = "pacommon.counsel.paCounselService")
	private PaCounselService paCounselService;
	
	@Resource(name = "panaver.counsel.paNaverCounselService")
	private PaNaverCounselService paNaverCounselService;

	@Resource(name = "pa11st.counsel.pa11stCounselService")
	private Pa11stCounselService pa11stCounselService;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	private PaGmktCommonUtil CommonUtil;

	static { //ComUtil 오류없이 실행
		Security.addProvider(new BouncyCastleProvider());
	}

	@RequestMapping(value = "/goods-counsel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> questionAnswerListNaver(HttpServletRequest httpServletRequest,
			@RequestParam(value="fromDate", required=false, defaultValue = "") String fromDate,			
			@RequestParam(value="toDate", required=false, defaultValue = "") String toDate,
			@RequestParam(value="answered", required=false, defaultValue = "N") String answered,
			@RequestParam(value="page", required=false, defaultValue = "1") int page) throws Exception{

		ParamMap paramMap = new ParamMap();

		String apiCode = "IF_PANAVERAPI_02_001";
		String duplicateCheck = "";
		List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
		Paqnamoment paqnamoment = new Paqnamoment();
		String endDate = "";
		String startDate = "";
		String dateTime = "";
		String msgGb = "00";
		String rtnMsg = Constants.SAVE_SUCCESS;
		dateTime = systemService.getSysdatetimeToString();
		Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
		boolean isRetrieve = false;
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", dateTime);
		paramMap.put("siteGb", "PANAVER");

		log.info("======= 상품 Q&A 목록 조회 API Start =======");

		try{
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			//기간입력
			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				endDate = toDate;
				startDate = fromDate;
			}	
			//예외(Default D-2 ~ D)
			else{
				endDate = DateUtil.getCurrentNaverDateAsString();
				startDate = DateUtil.addDay(endDate, -2, DateUtil.NAVER_DATE_FORMAT);
			}
			GetQuestionAnswerListRequestType request = new GetQuestionAnswerListRequestType();
			request.setRequestID(UUID.randomUUID().toString());
			request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
			request.setSellerId(ConfigUtil.getString("PANAVER_MALL_ID"));			
			request.setFromDate(startDate);
			request.setToDate(endDate);
			request.setAnswered(answered);
			request.setPage(page);
			request.setAccessCredentials(ComUtil.paNaverCreateAccessCredentials("QuestionAnswerService", "GetQuestionAnswerList"));

			QuestionAnswerService questionAnswerService = new QuestionAnswerService();
			QuestionAnswerServicePortType port = questionAnswerService.getQuestionAnswerServiceSOAP12PortHttp();
			GetQuestionAnswerListResponseType response = port.getQuestionAnswerList(request);

			if("SUCCESS".equals(response.getResponseType())&&(!response.getQuestionAnswerList().isEmpty())){
				for(QuestionAnswerReturnType questionAnswer : response.getQuestionAnswerList().get(0).getQuestionAnswer()){
					String note = ComUtil.NVL(questionAnswer.getQuestion());
					int contentLength = note.getBytes("UTF-8").length;
					
					paqnamoment = new Paqnamoment();
					paqnamoment.setPaGroupCode("04");
					paqnamoment.setPaCode(Constants.PA_NAVER_CODE);
					paqnamoment.setPaCounselNo(ComUtil.objToStr(questionAnswer.getQuestionAnswerId()));
					paqnamoment.setCounselDate(DateUtil.toTimestamp(questionAnswer.getCreateDate(), "yyyy-MM-dd"));
					paqnamoment.setTitle("네이버 상품Q&A");
					if(contentLength >= 4000){
						paqnamoment.setQuestComment(ComUtil.subStringBytes(note, 3980, 3) + "...(내용잘림)");
					}else{
						paqnamoment.setQuestComment(questionAnswer.getQuestion());
					}
					paqnamoment.setCounselGb("01");
					paqnamoment.setPaGoodsCode(String.valueOf(questionAnswer.getProductId()));
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
				
				if(response.getQuestionAnswerList().get(0).getQuestionAnswer().size() >= 100){
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
			}else if("SUCCESS".equals(response.getResponseType()) && response.getQuestionAnswerList().isEmpty()) {
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("errors.no.select"));
			}else{
				paramMap.put("code","500");
				paramMap.put("message", getMessage("errors.exist",new String[] { "orderConfirmList" }));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
		}catch(Exception e){
			CommonUtil.dealException(e, paramMap);
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally{
			try{
				CommonUtil.dealSuccess(paramMap, httpServletRequest);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			if(isRetrieve) questionAnswerListNaver(httpServletRequest, fromDate, toDate, answered, page);
			log.info("======= 상품 Q&A 목록 조회 API End =======");
			
			
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/goods-counsel-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> manageQuestionAnswerNaver(HttpServletRequest httpServletRequest) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PANAVERAPI_02_002";
		String duplicateCheck = "";
		String dateTime = "";
		dateTime = systemService.getSysdatetimeToString();
		StringBuffer sb = new StringBuffer();
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", dateTime);
		paramMap.put("siteGb", "PANAVER");
		PaqnamVO paQna = null;
		
		log.info("======= 상품 Q&A 처리 API Start =======");
		
		try{
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			List<PaqnamVO> ansList = paNaverCounselService.selectPaNaverAnsQna();
			
			if(ansList.size() < 1) {
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("pa.not_exists_process_list"));
			}
			
			for(int i=0; i<ansList.size(); i++){
				ManageQuestionAnswerRequestType request = new ManageQuestionAnswerRequestType();
				request.setRequestID(UUID.randomUUID().toString());
				request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
				request.setSellerId(ConfigUtil.getString("PANAVER_MALL_ID"));
				request.setQuestionAnswerId(ComUtil.objToLong(ansList.get(i).getPaCounselNo()));
				request.setAnswer(ansList.get(i).getProcNote());
				request.setAccessCredentials(ComUtil.paNaverCreateAccessCredentials("QuestionAnswerService", "ManageQuestionAnswer"));

				QuestionAnswerService questionAnswerService = new QuestionAnswerService();
				QuestionAnswerServicePortType port = questionAnswerService.getQuestionAnswerServiceSOAP11PortHttp();
				ManageQuestionAnswerResponseType response = port.manageQuestionAnswer(request);

				if("SUCCESS".equals(response.getResponseType())){
					paQna = ansList.get(i);
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
					paQna.setModifyId("PANAVER");
					paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paQna.setProcGb("40"); //= 완료
					paQna.setTitle("");

					pa11stCounselService.savePa11stQnaTransTx(paQna);
					
				}else{
					sb.append(ansList.get(i).getPaCounselNo() + " : 상품 Q&A 답변 처리 실패 ");
					paramMap.put("code", "500");
					paramMap.put("message", getMessage("errors.exist",new String[] { "orderConfirmList" }));
				}
			}
		}catch(Exception e){
			CommonUtil.dealException(e, paramMap);
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally{
			try{
				CommonUtil.dealSuccess(paramMap, httpServletRequest);
				paramMap.put("message", sb.toString());
				systemService.insertApiTrackingTx(httpServletRequest, paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("======= 상품 Q&A 처리 API End =======");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/cust-counsel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> customerInquiryListNaver(HttpServletRequest httpServletRequest, 
			@RequestParam(value = "inquiryStartDate", required = false, defaultValue = "" )String inquiryStartDate , 
			@RequestParam(value = "inquiryEndDate", required = false, defaultValue = "")String inquiryEndDate ) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		String tx = "";
		List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
		Paqnamoment paqnamoment = new Paqnamoment();
		Calendar startDate = null;
		Calendar endDate = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		Calendar sysdateTime = Calendar.getInstance();
		String time = "";
		String msgGb = "30";
		time = systemService.getSysdatetimeToString();
		Timestamp dateTime = DateUtil.toTimestamp(time);
		
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE,  -2);
		
		boolean isRequestRetrieve = false;
		
		paramMap.put("apiCode", "IF_PANAVERAPI_04_001");
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		log.info("======= 판매자 문의 목록 조회 API Start =======");
		
		try{
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(tx.equals("1")){
				throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apicode")});
			}
			endDate = sysdateTime;
			startDate = calendar;
			
			Security.addProvider(new BouncyCastleProvider());
			AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
			GetCustomerInquiryListRequestE requestE = new GetCustomerInquiryListRequestE();
			GetCustomerInquiryListRequest request = new GetCustomerInquiryListRequest();
			CustomerInquiryCommon stub = new CustomerInquiryCommon();
			
			NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("GetCustomerInquiryList",ConfigUtil.getString("PANAVER_CUST_SERVICE_NAME"));
			
			accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
			accessCredentialsType.setSignature(naverSignature.getSignature());
			accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());
			
			request.setAccessCredentials(accessCredentialsType);
			request.setRequestID(UUID.randomUUID().toString());
			request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
			request.setVersion(ConfigUtil.getString("PANAVER_CUST_SERVICE_VERSION"));
			request.setServiceType("SHOPN");
			request.setMallID(ConfigUtil.getString("PANAVER_MALL_ID"));
			if(inquiryStartDate.equals("")){				
				request.setInquiryTimeFrom(startDate);
			}else{
				request.setInquiryTimeFrom(startDate);
			}
			request.setInquiryTimeTo(endDate);
			request.setIsAnswered(false);

				requestE.setGetCustomerInquiryListRequest(request);
				GetCustomerInquiryListResponseE responseE = stub.getCustomerInquiryList(requestE);

				if("SUCCESS".equals(responseE.getGetCustomerInquiryListResponse().getResponseType())){
					
					isRequestRetrieve = responseE.getGetCustomerInquiryListResponse().getHasMoreData();
					
					if(responseE.getGetCustomerInquiryListResponse().getCustomerInquiryList() != null){
						for(InquiryType inquiry : responseE.getGetCustomerInquiryListResponse().getCustomerInquiryList().getCustomerInquiry()){
							String note = ComUtil.NVL(inquiry.getInquiryContent());
							int contentLength = note.getBytes("UTF-8").length;
		
							paqnamoment = new Paqnamoment();
							paqnamoment.setPaGroupCode("04");
							paqnamoment.setPaCode(Constants.PA_NAVER_CODE);
							paqnamoment.setPaCounselNo(inquiry.getInquiryID());
							paqnamoment.setCounselDate(new Timestamp(inquiry.getInquiryDateTime().getTimeInMillis()));
							paqnamoment.setTitle(ComUtil.NVL(inquiry.getTitle(), "네이버 스마트스토어 상담 문의"));
							if(contentLength > 200){								
								paqnamoment.setQuestComment(ComUtil.subStringBytes("["+inquiry.getTitle()+"]" + note, 185) + "…(내용잘림)");
							}else{
								paqnamoment.setQuestComment("["+inquiry.getTitle()+"]" + inquiry.getInquiryContent());
							}
							paqnamoment.setCounselGb(inquiry.getCategory());
							paqnamoment.setPaGoodsCode(inquiry.getProductID());
							paqnamoment.setPaGoodsDtCode("");
							paqnamoment.setPaOrderNo(inquiry.getOrderID());
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
							
							//마지막
						    inquiryStartDate = timeConvert(inquiry.getInquiryDateTime().getTimeInMillis());
						} 
						rtnMsg = paCounselService.savePaQnaTx(paqnamomentList, msgGb);
						
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
				}else{
					log.debug("GetCustomerInquiryListResponse Error Msg : " + responseE.getGetCustomerInquiryListResponse().getError().getMessage());
					paramMap.put("code", "404");
					paramMap.put("message", responseE.getGetCustomerInquiryListResponse().getError().getMessage());
					paramMap.put("resultCode", "99");
					paramMap.put("resultMessage", "GetCustomerInquiryList request failed");
					isRequestRetrieve = true;
				}
		}catch(Exception e){
			CommonUtil.dealException(e, paramMap);
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally{
			try{
				CommonUtil.dealSuccess(paramMap, httpServletRequest);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(tx.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			
			if(isRequestRetrieve) customerInquiryListNaver(httpServletRequest,inquiryStartDate, inquiryEndDate);
			log.info("=======  판매자 문의 목록 조회 API End =======");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@RequestMapping(value = "cust-counsel-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> answerCustomerInquiryNaver(HttpServletRequest httpServletRequest) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		
		String tx = "";
		
		paramMap.put("apiCode", "IF_PANAVERAPI_04_002");
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		PaqnamVO paQna = null;
		String dateTime = "";
		dateTime = systemService.getSysdatetimeToString();
		StringBuffer sb = new StringBuffer();
		
		log.info("======= 판매자 문의 답변 등록 API Start =======");
		
		try{
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(tx.equals("1")){
				throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apicode")});
			}
			
			List<PaqnamVO> ansList = paNaverCounselService.selectPaNaverCustAnsQna();
			
			AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
			AnswerCustomerInquiryRequestE requestE = new AnswerCustomerInquiryRequestE();
			AnswerCustomerInquiryRequest request = new AnswerCustomerInquiryRequest();
			CustomerInquiryCommon stub = new CustomerInquiryCommon();
			
			if(ansList.size() < 1) {
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("pa.not_exists_process_list"));
			}

			for(int i=0; i<ansList.size(); i++){
				NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("AnswerCustomerInquiry",ConfigUtil.getString("PANAVER_CUST_SERVICE_NAME"));
				accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
				accessCredentialsType.setSignature(naverSignature.getSignature());
				accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());
				request.setInquiryID(ansList.get(i).getPaCounselNo());				
				request.setAccessCredentials(accessCredentialsType);
				request.setRequestID(UUID.randomUUID().toString());
				request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
				request.setVersion(ConfigUtil.getString("PANAVER_CUST_SERVICE_VERSION"));
				request.setMallID(ConfigUtil.getString("PANAVER_MALL_ID"));
				request.setServiceType("SHOPN");
				request.setAnswerContent(ansList.get(i).getProcNote());
				request.setAnswerContentID("");
				request.setActionType("INSERT");
				request.setAnswerTempleteID("1");
				requestE.setAnswerCustomerInquiryRequest(request);
				
				AnswerCustomerInquiryResponseE responseE = stub.AnswerCustomerInquiry(requestE);
				paQna = null;
				if(("SUCCESS".equals(responseE.getAnswerCustomerInquiryResponse().getResponseType()))
					||(responseE.getAnswerCustomerInquiryResponse().getError().getCode().equals("ERR-NC-101005")&&
					responseE.getAnswerCustomerInquiryResponse().getError().getMessage().equals("문의 "+ansList.get(i).getPaCounselNo()+"의 상태가 유효하지 않습니다."))
					||(responseE.getAnswerCustomerInquiryResponse().getError().getCode().equals("ERR-NC-101004")&&
							responseE.getAnswerCustomerInquiryResponse().getError().getMessage().contains("삭제된 문의 입니다."))){
						paQna = ansList.get(i);
						paramMap.put("code", 200);
						paramMap.put("message", "OK");
						paQna.setModifyId("PANAVER");
						paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paQna.setProcGb("40");
						if("SUCCESS".equals(responseE.getAnswerCustomerInquiryResponse().getResponseType())) {
							paQna.setTitle("");
						} else {
							paQna.setTitle(responseE.getAnswerCustomerInquiryResponse().getError().getMessage());
						}
						pa11stCounselService.savePa11stQnaTransTx(paQna);
				}else{
					sb.append(ansList.get(i).getPaCounselNo() + " : 판매자 문의 답변 처리 실패 ");
					paramMap.put("code", "500");
				}
			}
		}catch(Exception e){
			CommonUtil.dealException(e, paramMap);
			log.error(paramMap.getString("message"), e);
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally{
			try{
				CommonUtil.dealSuccess(paramMap, httpServletRequest);
				paramMap.put("message", sb.toString());
				systemService.insertApiTrackingTx(httpServletRequest, paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(tx.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("======= 판매자 문의 답변 등록 API End =======");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	public String timeConvert(long inquiry){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(inquiry);
		String dateToString = sdf.format(date);
		return dateToString;
	}
}
