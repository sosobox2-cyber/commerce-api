package com.cware.netshopping.patdeal.process.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pacommon.counsel.service.PaCounselService;
import com.cware.netshopping.patdeal.domain.GoodsCounselResponse;
import com.cware.netshopping.patdeal.domain.OrderDetail;
import com.cware.netshopping.patdeal.domain.OrderOptions;
import com.cware.netshopping.patdeal.domain.OrderProducts;
import com.cware.netshopping.patdeal.domain.TdealGoodsCounsel;
import com.cware.netshopping.patdeal.process.PaTdealClaimProcess;
import com.cware.netshopping.patdeal.process.PaTdealCounselProcess;
import com.cware.netshopping.patdeal.repository.PaTdealCounselDAO;
import com.cware.netshopping.patdeal.service.PaTdealCounselService;
import com.cware.netshopping.patdeal.util.PaTdealApiRequest;
import com.cware.netshopping.patdeal.util.PaTdealConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service("patdeal.counsel.paTdealCounselProcess")
public class PaTdealCounselProcessImpl extends AbstractService implements PaTdealCounselProcess{
	
	@Autowired
	private PaTdealConnectUtil paTdealConnectUtil;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	private PaTdealApiRequest paTdealApiRequest;
	
	@Resource(name="common.system.systemProcess")
	private SystemProcess systemProcess;
	
	@Resource(name = "pacommon.counsel.paCounselService")
	private PaCounselService paCounselService;
	
	
    @Resource(name = "patdeal.counsel.paTdealCounselDAO")
    private PaTdealCounselDAO paTdealCounselDAO;
    
	@Autowired
	@Qualifier("patdeal.counsel.paTdealCounselService")
	private PaTdealCounselService paTdealCounselService;
    
	@Resource(name = "patdeal.claim.paTdealClaimProcess")
	PaTdealClaimProcess paTdealClaimProcess;
	
	
	@Value("${partner.tdeal.api.tv.registerAdminNo}")
	String TDEAL_TV_AUTHORIZATION_KEY; //방송
	
	@Value("${partner.tdeal.api.online.registerAdminNo}")
	String TDEAL_ONLINE_AUTHORIZATION_KEY; //온라인
	/**
	 * 상품문의조회
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getGoodsCounselList(String fromDate, String toDate, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PATDEALAPI_02_001";
		String duplicateCheck = "";
		String paCode  = "";
		String msgGb = "00";
		String rtnMsg = Constants.SAVE_SUCCESS;
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		log.info("======= 티딜 상품 문의 조회 API Start - {} =======");
		try {
			
			paramMap = paTdealApiRequest.getApiInfo(apiCode);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			
			for(int i = 0; i< Constants.PA_TDEAL_CONTRACT_CNT ; i ++) {
				
				paCode = (i==0 )? Constants.PA_TDEAL_BROAD_CODE : Constants.PA_TDEAL_ONLINE_CODE;
				
				List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
				Paqnamoment paqnamoment = new Paqnamoment();
				
				/** 1) 전문 데이터 세팅  **/
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String dateTime = systemService.getSysdatetimeToString();
				Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
				
				Calendar from = Calendar.getInstance();
				Calendar to = Calendar.getInstance();
				
				if (fromDate != null && !fromDate.equals("")) {
					from.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(fromDate));
				} else {
					from.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateTime));
					from.add(Calendar.DATE, -1);
				}
				
				if (toDate != null && !toDate.equals("")) {
					to.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(toDate));
				} else {
					to.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateTime));
					
				}
				
				// Query Parameters 세팅
				Map<String, String> queryParameters = new HashMap<String, String>();
				
				queryParameters.put("startYmd", dateFormat.format(from.getTime()));
				queryParameters.put("endYmd", dateFormat.format(to.getTime()));
				queryParameters.put("replyYn", "N");//답변여부
				//필요한 파라미터 및 통신 세팅
				// Body 세팅S
				ParamMap apiDataObject = new ParamMap();
				// Body 세팅 E
				
				GoodsCounselResponse response = new GoodsCounselResponse();
				//통신
				responseMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters,  queryParameters,  apiDataObject);
				
				if(((List<Map<String, Object>>)(responseMap.get("Data"))).size() < 1) {
					continue;
				}
				// vo변환
				ObjectMapper objectMapper = new ObjectMapper();
				response = objectMapper.convertValue(responseMap, GoodsCounselResponse.class);
				if(response.getData() != null) {
					if(response.getData().size()> 0) {
						paramMap.put("code", "200");
						//tpaorderlist, tpaorderm 저장
						for(TdealGoodsCounsel counsel : response.getData()) {

							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String formattedTimestamp = sdf.format(counsel.getUpdateYmdt());
							Date date = sdf.parse(formattedTimestamp);
					        Timestamp counselDate = new Timestamp(date.getTime());
					        
					        
							paqnamoment = new Paqnamoment();
							paqnamoment.setPaGroupCode(Constants.PA_TDEAL_GROUP_CODE);
							paqnamoment.setPaCode(paCode);
							paqnamoment.setPaCounselNo(counsel.getInquiryNo());
							paqnamoment.setCounselDate(counselDate);
							paqnamoment.setTitle("티딜 상품문의");
							paqnamoment.setQuestComment(counsel.getContent());
							
							switch(counsel.getType()){ // 상품 문의할때 문의 유형 선택하는 부분 있으나 결국 다 PRODUCT로 들어온다.... 문의 필요
							
								case "PRODUCT" :
									paqnamoment.setCounselGb("01");						
									break;
								case "DELIVERY" :
									paqnamoment.setCounselGb("02");
									break;
								case "CANCEL" :
									paqnamoment.setCounselGb("03");
									break;
								case "RETURN" :
									paqnamoment.setCounselGb("04");
									break;
								case "EXCHANGE" :
									paqnamoment.setCounselGb("05");
									break;
								case "REFUND" :
									paqnamoment.setCounselGb("06");
									break;
								case "OTHER" :
									paqnamoment.setCounselGb("07");
									break;
								default :
									paqnamoment.setCounselGb("01");
									break;
							}
							
							
							paqnamoment.setPaGoodsCode(String.valueOf(counsel.getProductNo()));
							paqnamoment.setPaGoodsDtCode("");
							
							if (counsel.getOrderNo() != null) {
								paqnamoment.setPaOrderNo(String.valueOf(counsel.getOrderNo()));
							}else {
								paqnamoment.setPaOrderNo("");
							}
							
							paqnamoment.setOrderYn(StringUtils.isBlank(paqnamoment.getPaOrderNo()) ? "0" : "1");
							paqnamoment.setToken(""); 
							paqnamoment.setDisplayYn("");
							paqnamoment.setPaCustNo("");
							paqnamoment.setCustTel("");
							paqnamoment.setReceiptDate(null);
							paqnamoment.setMsgGb("00"); //일반상담 Q&A
							paqnamoment.setInsertId(Constants.PA_TDEAL_PROC_ID);
							paqnamoment.setModifyId(Constants.PA_TDEAL_PROC_ID);
							paqnamoment.setInsertDate(sysdateTime);
							paqnamoment.setModifyDate(sysdateTime);

							paqnamomentList.add(paqnamoment);
						
						}
						rtnMsg = paCounselService.savePaQnaTx(paqnamomentList, msgGb);
						
						if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
							paramMap.put("code","200");
							paramMap.put("message","OK");
						}else {
							paramMap.put("code","500");
							paramMap.put("message",rtnMsg);
						}
					}
					
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(paramMap ,  e);
		} finally {
			
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "상품 문의 대상 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			
			paTdealConnectUtil.closeApi(request, paramMap);
		}
		
		
		log.info("======= 티딜 상품 문의 조회 API End - {} =======");
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
		
	}
	
	/**
	 * 상품문의답변등록
	 */
	@Override
	public ResponseMsg goodsCounselProc(String paCounselNo, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PATDEALAPI_02_002";
		String duplicateCheck = "";
		String paCode  = "";
		int failCnt   = 0;
		int totalCnt  = 0;
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		log.info("======= 티딜 상품 문의 답변 등록 API Start - {} =======");
		try {
			
			paramMap = paTdealApiRequest.getApiInfo(apiCode);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			/** 1) 전문 데이터 세팅  **/
			String dateTime = systemService.getSysdatetimeToString();
			
			if(String.valueOf(paCounselNo)!=null) {
				paramMap.put("paCounselNo", paCounselNo);
			}
			
			// 답변 내역 조회
			List<PaqnamVO> ansList = paTdealCounselDAO.selectPaTdealAnsQna(paramMap);
			totalCnt = ansList.size();
			
			for(PaqnamVO paQna : ansList) {
				try {
					pathParameters = paQna.getPaCounselNo();//inquiryNo
					paCode = paQna.getPaCode();
					
					// Query Parameters 세팅
					Map<String, String> queryParameters = new HashMap<String, String>();
					
					//필요한 파라미터 및 통신 세팅
					// Body 세팅S
					List<String> body = new ArrayList<String>();
					body.add(ComUtil.subStringBytes(paQna.getProcNote(), 1800).replaceAll("\n", "<br>")); // 1000글자까지 제한 있음, 넉넉하게 1800바이트로
					
					/* 티딜 api 문서에서는  text/plain; charset=UTF-8" 세팅 후 body에 string을 입력하라고 되어 있으나 실제로 해당 세팅후 입력하면 
					   답변 내용을 입력해도 {}값으로 답변이 기록 됨, 
					   따라서 기존 application/json 형태에 body에 List<String> 형태로 입력해서 등록하면 답변이 등록 됨
					   허나, 답변 내영에 [""] 가 남게 됨, 그리고 줄바꿈 \n을 문자 그대로 입력됨
					   \n 은 <br>로 바꾸면 띄어쓰기 인식이 되어 처리해두었음 
					 */
					
					ParamMap apiDataObject = new ParamMap();
				    apiDataObject.put("body", body);
					// Body 세팅 E
					
					//통신
					responseMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters,  queryParameters,  apiDataObject);
					
					if(!"204".equals(responseMap.get("status")) || responseMap.get("status")== null ) {
						throw processException("errors.detail", new String[] {"티딜 상담 상품문의 답변 오류 "});
						
					}else {
						paramMap.put("code", "200");
						paramMap.put("message", "답변성공");
					}
					
				} catch (TransApiException e) {
					
					if(e.getMessage().contains("없습니다")) {
						paramMap.put("code", "200");
						paramMap.put("message",e.getMessage());
					}else {
						paramMap.put("code", "400");
						paramMap.put("message", e.getMessage());
					}
					
				} catch (Exception e) {
					log.info("{}: {} PA_COUNSEL_SEQ: {}", "티딜 상품문의 답변 오류", e.getMessage(), paQna.getPaCounselSeq());
					failCnt++;
					paramMap.put("code", "400");
					paramMap.put("message", e.getMessage());
					
				} finally {
					try {
						if("200".equals(paramMap.get("code"))) {
							if(paramMap.get("message").toString().contains("없습니다")) {
								paQna.setTitle("중복답변 혹은 삭제된 문의입니다"); 
							}else {
								paQna.setTitle(""); 
							}
							
							paQna.setProcGb("40");
							paQna.setModifyId(Constants.PA_TDEAL_PROC_ID);
							paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paTdealCounselService.savePaTdealQnaTransTx(paQna);
							paramMap.put("code", "200");
							paramMap.put("message", "답변성공 및 저장 완료");
							
						}
					} catch (Exception e) {
							paramMap.put("code", "400");
							paramMap.put("message", e.getMessage());
					}
				}
				
			}
			
			if(failCnt > 0 ) {
				throw processException("errors.detail", new String[] {"티딜 상품문의 답변 등록 오류 - 실패:" + failCnt + "건 전체:" + totalCnt + "건"});
			}
			
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(paramMap ,  e);
		} finally {
			
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "상품 문의 답변 대상 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			
			paTdealConnectUtil.closeApi(request, paramMap);
		}
		
		
		log.info("======= 티딜 상품 문의 답변 등록 API End - {} =======");
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
		
	}
	
	/**
	 * 티딜 상품문의답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	@Override
	public void savePaTdealQnaTrans(PaqnamVO paQna) throws Exception {
		int executedRtn = 0;
		String paCounselDtSeq = "";
		
		executedRtn = paTdealCounselDAO.updatePaQnaTrans(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAQNAM update" });
		}
		
		paCounselDtSeq = paTdealCounselDAO.selectPaQnaDtMaxSeq(paQna);
		paQna.setPaCounselDtSeq(paCounselDtSeq);
		
		executedRtn = paTdealCounselDAO.insertPaQnaDt(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",new String[] { "TPAQNADT insert" });
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getTaskMessagesList(String fromDate, String toDate, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PATDEALAPI_02_003";
		String duplicateCheck = "";
		String paCode  = "";
		String msgGb = "10";
		String rtnMsg = Constants.SAVE_SUCCESS;
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		log.info("======= 티딜 업무 메시지 조회 API Start - {} =======");
		try {
			
			paramMap = paTdealApiRequest.getApiInfo(apiCode);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		    SimpleDateFormat formatToTdeal = new SimpleDateFormat("yyyy-MM-dd");
		    OrderDetail orderDetail = null;
		    
		    String dateTime = "";
		    String paGoodsCode  = "";
		    dateTime = systemService.getSysdatetimeToString();
		    Date date = format.parse(paramMap.getString("startDate"));		    
		    Calendar cal = Calendar.getInstance();	    
		    cal.setTime(date);
		    if(toDate.equals("")) {
		    	toDate = formatToTdeal.format(cal.getTime()).toString();
		    }
		    toDate = toDate + " 23:59:59";
		   	//티딜 업무메시지는 메인 메세지 등록 시간 기준으로 조회가 가능하다. 
		   	cal.add(Calendar.DATE, -10); //임시 10일로 설정 문제 생길 시 변동
		   	if(fromDate.equals("")) {
		   		fromDate = formatToTdeal.format(cal.getTime()).toString();
		    }
	    	fromDate = fromDate + " 00:00:00";
	    	Map<String, Object> registerInfo = new HashMap<>();
	    	int paqnaCount = 0; 
			for(int i = 0; i< Constants.PA_TDEAL_CONTRACT_CNT ; i ++) {
				
				paCode = (i==0 )? Constants.PA_TDEAL_BROAD_CODE : Constants.PA_TDEAL_ONLINE_CODE;
				
				pathParameters = "startDateTime=" + fromDate + "&endDateTime=" + toDate;
				
				// Query Parameters 세팅
				Map<String, String> queryParameters = new HashMap<String, String>();
				
				//필요한 파라미터 및 통신 세팅
				// Body 세팅S
				ParamMap apiDataObject = new ParamMap();
				
				//통신
				responseMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters,  queryParameters,  apiDataObject);
				
				if ("0".equals(String.valueOf(responseMap.get("totalCount")))) {
					continue;
				}

				String[] registerTvAdminNoArr =TDEAL_TV_AUTHORIZATION_KEY.split(",");
				String[] registerOnlineAdminNoArr =TDEAL_ONLINE_AUTHORIZATION_KEY.split(",");
				List<String> registerTvAdminNoList = Arrays.asList(registerTvAdminNoArr);
				List<String> registerOnlineAdminNoList = Arrays.asList(registerOnlineAdminNoArr);
				
				
				List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
				List<Map<String, Object>> resultList = (List<Map<String, Object>>)responseMap.get("items");
				List<Map<String, Object>> taskMessageDetails = new ArrayList<Map<String, Object>>();
				for(Map<String, Object> m : resultList) {
					
					paCode =  paTdealCounselDAO.selectPaCode(m.get("orderNo").toString()); // 당사 메세지 파악용도, 우리 데이터만 들어오는 경우 삭제해도 됨
					if(paCode == null) continue; 
					
					Paqnamoment paqnamoment = new Paqnamoment();
					taskMessageDetails = (List<Map<String, Object>>) m.get("taskMessageDetails");
					 
					 if(taskMessageDetails.size() > 0) {
						 Map<String, String> lastDetailInfoMap = new HashMap<String, String>();
						 lastDetailInfoMap = (Map<String, String>) taskMessageDetails.get(taskMessageDetails.size() - 1).get("registerInfo");
						 if(taskMessageDetails.get(taskMessageDetails.size() - 1).get("taskMessageChannelType").equals("PARTNER")
								 ||  registerTvAdminNoList.contains(String.valueOf(lastDetailInfoMap.get("registerAdminNo")))
								 ||  registerOnlineAdminNoList.contains(String.valueOf(lastDetailInfoMap.get("registerAdminNo"))) ){
								 
							 continue; //마지막 하위 메시지의 작성자가 sk스토아일 경우 질문이 아니므로 스킵. 
						 }
					 }
					 
					 paqnamoment.setPaCode(paCode);
					 paqnamoment.setPaGroupCode(Constants.PA_TDEAL_GROUP_CODE);
					 paqnamoment.setPaCounselNo(m.get("no").toString());
					 paqnamoment.setPaOrderNo(m.containsKey("orderNo") ? m.get("orderNo").toString() : "");
					 paqnamoment.setCounselDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					 paqnamoment.setMsgGb(msgGb); 
					 paqnamoment.setInsertId(Constants.PA_TDEAL_PROC_ID);
					 paqnamoment.setModifyId(Constants.PA_TDEAL_PROC_ID);
					 paqnamoment.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					 paqnamoment.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss")); 
					 paqnamoment.setCounselGb("08"); //긴급 건으로 처리
					 paqnamoment.setPaGoodsDtCode("");
					 paqnamoment.setOrderYn("1");
					 paqnamoment.setDisplayYn("");
					 paqnamoment.setPaCustNo(""); 
					 paqnamoment.setCustTel("");	
					 paqnamoment.setTitle("TDEAL: " + m.get("orderNo").toString() + " 업무메세지");
					// paGoodsCode =  paTdealCounselDAO.selectMallProductNo(m.get("orderProductOptionNo").toString());
						 orderDetail = paTdealClaimProcess.orderInfoDetail(paCode, m.get("orderNo").toString(), request);
						 List<OrderProducts> responseData = orderDetail.getOrderProducts();
							for (OrderProducts op : responseData) {
								for (OrderOptions oo : op.getOrderOptions()) {
									if(oo.getOrderOptionNo() == (Long.valueOf(m.get("orderProductOptionNo").toString()))) {
										paGoodsCode = ComUtil.objToStr(oo.getMallProductNo());
									}
								}
							}
					 paqnamoment.setPaGoodsCode(paGoodsCode);
					 paqnamoment.setToken(m.get("no").toString());
					 registerInfo = (Map<String, Object>) m.get("registerInfo");
					 paqnamoment.setCounselDate(DateUtil.toTimestamp(registerInfo.get("registerYmdt").toString(), "yyyy-MM-dd HH:mm:ss"));
					 
					 StringBuilder sb = new StringBuilder();
					 sb.append("\n"+ (m.get("taskMessageChannelType").toString().equals("SERVICE") ? "Q:" : "A:") +  m.get("content").toString());// 티딜 질문 : Q, SK스토아 답장 : A
					 
					 for(Map<String, Object> s : taskMessageDetails) { //하위 추가 메세지 내용 뽑아내기
						 sb.append("\n");
						 //sb.append((s.get("taskMessageChannelType").toString().equals("SERVICE") ? "Q:" : "A:") +  s.get("content").toString());// 티딜 질문 : Q, SK스토아 답장 : A
						 
						 Map<String, Object> detailInfoMap = new HashMap<>();
						 detailInfoMap =  (Map<String, Object>)s.get("registerInfo");
						 if(s.get("taskMessageChannelType").toString().equals("PARTNER")
								 ||  registerTvAdminNoList.contains(String.valueOf(detailInfoMap.get("registerAdminNo")))
								 ||  registerOnlineAdminNoList.contains(String.valueOf(detailInfoMap.get("registerAdminNo"))) ) {
 							 sb.append("A:" +  s.get("content").toString());
						 }else {
							 sb.append("Q:" +  s.get("content").toString());
						 }
						 
						 paqnamoment.setToken(s.get("no").toString());
						 
					 }
					 paqnamoment.setQuestComment(sb.toString()); 
					 
					 
					 paqnaCount = paTdealCounselDAO.selectPaqnaCount(paqnamoment);
					 if(paqnaCount == 0) paqnamomentList.add(paqnamoment);
				}
				
				if(paqnamomentList.size() > 0)
					rtnMsg = paCounselService.savePaQnaTx(paqnamomentList, msgGb);
				
				if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}else {
					paramMap.put("code","500");
					paramMap.put("message",rtnMsg);
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(paramMap ,  e);
		} finally {
			
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "업무메세지 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			
			paTdealConnectUtil.closeApi(request, paramMap);
		}
		
		
		log.info("======= 티딜 업무 메시지 조회 API End - {} =======");
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
		
	}
	
	/**
	 * 업무메시지 답변 등록(상세 업무 메시지 등록)
	 */
	@Override
	public ResponseMsg getTaskMessagesProc(String paCounselNo, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PATDEALAPI_02_004";
		String duplicateCheck = "";
		String paCode  = "";
		int failCnt   = 0;
		int totalCnt  = 0;
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		log.info("======= 티딜 업무 메시지 답변 등록 API Start - {} =======");
		try {
			
			paramMap = paTdealApiRequest.getApiInfo(apiCode);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			/** 1) 전문 데이터 세팅  **/
			String dateTime = systemService.getSysdatetimeToString();
			
			if(String.valueOf(paCounselNo)!=null) {
				paramMap.put("paCounselNo", paCounselNo);
			}
			
			// 답변 내역 조회
			List<PaqnamVO> ansList = paTdealCounselDAO.selectTaskMessagesAnsList(paramMap);
			totalCnt = ansList.size();
			
			for(PaqnamVO paQna : ansList) {
				try {
					pathParameters = paQna.getPaCounselNo();//inquiryNo
					paCode = paQna.getPaCode();
					
					
					String[] registerAdminNoArr ;
					if(Constants.PA_TDEAL_BROAD_CODE.equals(paCode)) {
						registerAdminNoArr = TDEAL_TV_AUTHORIZATION_KEY.split(",");
					}else {
						registerAdminNoArr = TDEAL_ONLINE_AUTHORIZATION_KEY.split(",");
					}
					List<String> registerAdminNoList = Arrays.asList(registerAdminNoArr);
					String registerAdminNo = registerAdminNoList.get(0);
					
					// Query Parameters 세팅
					Map<String, String> queryParameters = new HashMap<String, String>();
					
					//필요한 파라미터 및 통신 세팅
					// Body 세팅S
					
					ParamMap apiDataObject = new ParamMap();
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("completion", "true");//처리완료여부
					map.put("content", ComUtil.subStringBytes(paQna.getProcNote(), 1800));//내용
					map.put("registerAdminName", "SKSTOA");//메시지를 작성한 담당자 이름 (nullable)
					map.put("registerAdminNo", registerAdminNo);//관리자번호 (nullable)
					
					
				    apiDataObject.put("body", map);
					// Body 세팅 E
					
					//통신
					responseMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters,  queryParameters,  apiDataObject);
					
					if(responseMap.containsKey("status") && !"200".equals(responseMap.get("status"))) {
						if(responseMap.containsKey("message") && responseMap.get("message").toString().contains("존재하지 않는 업무메시지 입니다")) {
							throw new TransApiException(responseMap.get("message").toString(), String.valueOf(responseMap.get("status")));
						}else {
							throw processException("errors.detail", new String[] {"티딜 업무 메시지 답변 등록 오류 "});
						}
					}
					paramMap.put("code", "200");
					paramMap.put("message", "답변성공");
					
				} catch (TransApiException e) {
					
					if(e.getMessage().contains("존재하지 않는 업무메시지 입니다")) {
						paramMap.put("code", "200");
						paramMap.put("message",e.getMessage());
					}else {
						paramMap.put("code", "400");
						paramMap.put("message", e.getMessage());
					}
					
				} catch (Exception e) {
					log.info("{}: {} PA_COUNSEL_SEQ: {}", "티딜 업무 메시지 답변 등록 오류", e.getMessage(), paQna.getPaCounselSeq());
					failCnt++;
					paramMap.put("code", "400");
					paramMap.put("message", e.getMessage());
					
				} finally {
					try {
						if("200".equals(paramMap.get("code"))) {
							if(paramMap.get("message").toString().contains("존재하지 않는 업무메시지 입니다")) {
								paQna.setTitle("중복답변 혹은 삭제된 문의입니다"); 
							}else {
								paQna.setTitle(""); 
							}
							
							paQna.setProcGb("40");
							paQna.setModifyId(Constants.PA_TDEAL_PROC_ID);
							paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paTdealCounselService.savePaTdealQnaTransTx(paQna);
							paramMap.put("code", "200");
							paramMap.put("message", "업무메세지 답변성공 및 저장 완료");
							
						}
					} catch (Exception e) {
							paramMap.put("code", "400");
							paramMap.put("message", e.getMessage());
					}
				}
				
			}
			
			if(failCnt > 0 ) {
				throw processException("errors.detail", new String[] {"티딜 업무 메시지 답변 등록 오류 - 실패:" + failCnt + "건 전체:" + totalCnt + "건"});
			}
			
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(paramMap ,  e);
		} finally {
			
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "티딜 업무 메시지 답변 대상 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			
			paTdealConnectUtil.closeApi(request, paramMap);
		}
		
		
		log.info("======= 티딜 업무 메시지 답변 등록 API End - {} =======");
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
		
	}
	
}	
