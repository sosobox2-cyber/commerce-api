package com.cware.netshopping.pafaple.process.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractProcess;
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
import com.cware.netshopping.pafaple.domain.PaFapleQnaVO;
import com.cware.netshopping.pafaple.process.PaFapleCounselProcess;
import com.cware.netshopping.pafaple.repository.PaFapleCounselDAO;
import com.cware.netshopping.pafaple.service.PaFapleCounselService;
import com.cware.netshopping.pafaple.util.PaFapleApiRequest;
import com.cware.netshopping.pafaple.util.PaFapleConnectUtil;

@Service("pafaple.counsel.paFapleCounselProcess")
public class PaFapleCounselProcessImpl extends AbstractProcess implements PaFapleCounselProcess {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	private PaFapleConnectUtil paFapleConnectUtil;
	
	
	@Autowired
	private PaFapleApiRequest paFapleApiRequest;
	
	@Resource(name="common.system.systemProcess")
	private SystemProcess systemProcess;
	
	@Resource(name = "pacommon.counsel.paCounselService")
	private PaCounselService paCounselService;
	
    @Resource(name = "pafaple.counsel.paFapleCounselDAO")
    private PaFapleCounselDAO paFapleCounselDAO;
    
	@Autowired 
	@Qualifier("pafaple.counsel.paFapleCounselService")
	private PaFapleCounselService paFapleCounselService;
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getCsNoticeList(String fromDate, String toDate, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PAFAPLEAPI_04_001";
		String duplicateCheck = "";
		String paCode  = "";
		String msgGb = "10";
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		log.info("======= 패플 CS 알리미 조회 API Start - {} =======");
		try {
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paFapleApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		    SimpleDateFormat formatToFaple = new SimpleDateFormat("yyyy-MM-dd");
		    
		    String dateTime = "";
		    dateTime = systemService.getSysdatetimeToString();
		    Date date = format.parse(paramMap.getString("startDate"));		    
		    Calendar cal = Calendar.getInstance();	    
		    cal.setTime(date);
		    if(toDate.equals("")) {
		    	toDate = formatToFaple.format(cal.getTime()).toString();
		    }
		   	cal.add(Calendar.DATE, -6); 
		   	if(fromDate.equals("")) {
		   		fromDate = formatToFaple.format(cal.getTime()).toString();
		    }
		   	
		   	int paFapleContractCnt = Constants.PA_FAPLE_CONTRACT_CNT;
			//로컬 세팅
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
				paFapleContractCnt = 1;
			}
		   	
		   	for(int i = 0; i< paFapleContractCnt ; i ++) {
				
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				
				paramMap.put("paCode",paCode);
				
				//필요한 파라미터 및 통신 세팅
				// Body 세팅S
				ParamMap apiDataObject = new ParamMap();
				
				apiDataObject.put("Sdate", fromDate);
				apiDataObject.put("Edate", toDate);
				apiDataObject.put("isscm", 0);//문의주체 (0:패션플러스,1:협력사)
				apiDataObject.put("ProcessType", 0);//	처리상태 (0 : 전체, 1 : 미확인, 2 : 답변대기, 3 : 답변완료, 4 : 재답변요청, 5: 재답변완료, 6 : 통화완료. 7 : 전체미처리)
				
				//통신
				responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiDataObject);
				
				if(!responseMap.containsKey("Data")) continue;
				List<Map<String, Object>> resultList = (List<Map<String, Object>>)(responseMap.get("Data"));
				List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
				
				if(resultList!=null) {
					for(Map<String, Object> m : resultList) {
						String processType = m.get("ProcessType_name").toString();
						
						if(!"답변완료".equals(processType) && !"재답변완료".equals(processType)) {
							Paqnamoment paqnamoment = new Paqnamoment();
							
							paqnamoment.setPaCode(paCode);
							paqnamoment.setPaGroupCode(Constants.PA_FAPLE_GROUP_CODE);
							paqnamoment.setPaCounselNo(m.get("idx").toString());
							paqnamoment.setPaOrderNo(String.valueOf(m.get("order_id")!= null ? m.get("order_id"):""));
							paqnamoment.setCounselDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paqnamoment.setMsgGb(msgGb);
							paqnamoment.setInsertId(Constants.PA_FAPLE_PROC_ID);
							paqnamoment.setModifyId(Constants.PA_FAPLE_PROC_ID);
							paqnamoment.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paqnamoment.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paqnamoment.setCounselGb("06"); 
							paqnamoment.setPaGoodsDtCode("");
							paqnamoment.setOrderYn(String.valueOf(m.get("order_id")!= null ? "1" : "0"));
							paqnamoment.setDisplayYn("");
							paqnamoment.setPaCustNo("");
							paqnamoment.setCustTel("");
							paqnamoment.setTitle("[FAPLE] CS알리미 문의");
							paqnamoment.setCounselDate(DateUtil.toTimestamp(m.get("CreateDate").toString(), "yyyy-MM-dd HH:mm:ss"));
							
							List<Map<String, Object>> reNotiList  = (List<Map<String, Object>>)(m.get("re_Notis"));
							//재문의 처리 
							if("재답변요청".equals(processType)&&reNotiList.size() != 0) {
									
								Long token = reNotiList.stream().filter(x -> x.containsKey("re_Noti_memo")).count();// 재문의 개수로 토큰 
								if(token>0) {
									paqnamoment.setToken(token.toString());
										
									StringBuilder sb = new StringBuilder();
									sb.append("\n\n");
//										sb.append("[Q] : " + m.get("Noti_Memo").toString());
//										sb.append("\n");
//										sb.append("[A] : " + m.get("Noti_Answer").toString());
//										sb.append("\n");
									
									int s = reNotiList.size();
									
									Map<String, Object> reNoti = reNotiList.get(reNotiList.size() - 1);
									sb.append(ComUtil.NVL(reNoti.get("re_Noti_memo"), reNoti.get("re_Noti_Answer"))); 
									if(sb.toString().contains("SK스토아입니다")) continue;
									
//										for(Map<String, Object> reNoti : reNotiList) {
//											
//											if(reNoti.containsKey("re_Noti_memo")) {
//												sb.append("[Q] : " + reNoti.get("re_Noti_memo"));//재문의내용
//												sb.append("\n");
//											}
//											if(reNoti.containsKey("re_Noti_Answer")) {
//												sb.append("[A] : " + reNoti.get("re_Noti_Answer"));//재답변내용
//												sb.append("\n");
//											}
//											
//										}
									paqnamoment.setQuestComment(sb.toString());
									
								}
							}else { // 일반 문의
								paqnamoment.setToken("0");
								paqnamoment.setQuestComment(m.get("Noti_Memo").toString() +"\n"+ m.get("Noti_Answer").toString());
							}
							
							
							if(String.valueOf(m.get("ItemNo"))!= null && !"".equals(String.valueOf(m.get("ItemNo")))) {
								String[] itemNo = m.get("ItemNo").toString().split("_");
								String paGoodsCode = paFapleCounselDAO.selectPaGoodsCode(itemNo[1]);
								paqnamoment.setPaGoodsCode(ComUtil.objToStr(paGoodsCode));
								
							}else {
								paqnamoment.setPaGoodsCode("");
							}
							paqnamomentList.add(paqnamoment);
						}
					}
					if(paqnamomentList.size() > 0) {
						rtnMsg = paCounselService.savePaQnaTx(paqnamomentList, msgGb);
					}
					
					if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
						paramMap.put("code","200");
						paramMap.put("message","OK");
					}else {
						paramMap.put("code","500");
						paramMap.put("message",rtnMsg);
					}
				}
				
				
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paFapleConnectUtil.checkException(paramMap ,  e);
		} finally {
			
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "CS 알리미 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			
			paFapleConnectUtil.closeApi(request, paramMap);
		}
		
		
		log.info("======= 패플 CS 알리미 조회 API End - {} =======");
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg csNoticeProc(String paCounselNo, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PAFAPLEAPI_04_002";
		String duplicateCheck = "";
		String paCode  = "";
		int failCnt   = 0;
		int totalCnt  = 0;
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		log.info("======= 패플 CS알리미 답변 등록 API Start - {} =======");
		try {
			
			paramMap.put("apiCode", apiCode);
			paFapleApiRequest.getApiInfo(paramMap);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			/** 1) 전문 데이터 세팅  **/
			String dateTime = systemService.getSysdatetimeToString();
			
			if(String.valueOf(paCounselNo)!=null) {
				paramMap.put("paCounselNo", paCounselNo);
			}
			
			// 답변 내역 조회
			List<PaqnamVO> ansList = paFapleCounselDAO.selectCsNoticeAnsList(paramMap);
			totalCnt = ansList.size();
			
			for(PaqnamVO paQna : ansList) {
				try {
					paCode = paQna.getPaCode();
					paramMap.put("paCode", paCode);
					
					//필요한 파라미터 및 통신 세팅
					// Body 세팅S
					
					ParamMap apiDataObject = new ParamMap();
					List<Map<String, Object>> csNotiList = new ArrayList<Map<String,Object>>();
					
					Map<String,Object> csNoti = new HashMap<String, Object>();
					csNoti.put("isscm", 0);//문의주체 (0:패션플러스,1:협력사)
					csNoti.put("idx", Integer.parseInt(paQna.getPaCounselNo()));//CS알리미 API에서 수집한 CS 알리미 순번 (문의 주체가 패션플러스인 경우에만 입력)
					csNoti.put("Memo", ComUtil.subStringBytes(paQna.getProcNote(), 1000));//답변
					
					csNotiList.add(csNoti);
					
				    apiDataObject.put("CSNoti_list", csNotiList);
					// Body 세팅 E
					
					//통신
					responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiDataObject);
					
					List<Map<String, Object>> orderList = (List<Map<String, Object>>)(responseMap.get("OderList"));
					
					Map<String, Object> resultMap = orderList.get(0);
					if(Constants.PA_FAPLE_SUCCESS_STATUS.equals(resultMap.get("Status"))) {
					
						paQna.setModifyId(Constants.PA_FAPLE_PROC_ID);
						paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paQna.setProcGb("40"); // 완료
						paQna.setTitle("");
						
						paFapleCounselService.savePaFapleQnaTransTx(paQna);
						
						paramMap.put("code", "200");
						paramMap.put("message", "답변성공");
						
					} else {
						paramMap.put("code", "500");
						paramMap.put("message", "패션플러스 CS 알리미 답변 API Fail : " + resultMap.get("Message"));
					}
					
				} catch (TransApiException e) {
					paramMap.put("code", "400");
					paramMap.put("message", e.getMessage());
					failCnt++;
				} catch (Exception e) {
					log.info("{}: {} PA_COUNSEL_SEQ: {}", "패플 CS 알리미 답변 등록 오류", e.getMessage(), paQna.getPaCounselSeq());
					failCnt++;
					paramMap.put("code", "400");
					paramMap.put("message", e.getMessage());
					
				}
				
			}
			
			if(failCnt > 0 ) {
				throw processException("errors.detail", new String[] {"패플 CS 알리미 답변 등록 오류 - 실패:" + failCnt + "건 전체:" + totalCnt + "건"});
			}
			
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paFapleConnectUtil.checkException(paramMap ,  e);
		} finally {
			
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "패플 CS 알리미 답변 대상 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			
			paFapleConnectUtil.closeApi(request, paramMap);
		}
		
		
		log.info("======= 패플 CS 알리미 답변 등록 API End - {} =======");
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
		
	}
	
	/**
	 * 패플 CS알리미답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	@Override
	public void savePaFapleQnaTrans(PaqnamVO paQna) throws Exception {
		int executedRtn = 0;
		String paCounselDtSeq = "";
		
		executedRtn = paFapleCounselDAO.updatePaQnaTrans(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAQNAM update" });
		}
		
		paCounselDtSeq = paFapleCounselDAO.selectPaQnaDtMaxSeq(paQna);
		paQna.setPaCounselDtSeq(paCounselDtSeq);
		
		executedRtn = paFapleCounselDAO.insertPaQnaDt(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",new String[] { "TPAQNADT insert" });
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getBbsList(String fromDate, String toDate, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PAFAPLEAPI_02_001";
		String duplicateCheck = "";
		String paCode  = "";
		String msgGb = "00";
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		log.info("======= 패플 고객문의게시판조회 조회 API Start - {} =======");
		try {
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paFapleApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		    SimpleDateFormat formatToFaple = new SimpleDateFormat("yyyy-MM-dd");
		    
		    String dateTime = "";
		    dateTime = systemService.getSysdatetimeToString();
		    Date date = format.parse(paramMap.getString("startDate"));		    
		    Calendar cal = Calendar.getInstance();	    
		    cal.setTime(date);
		    if(toDate.equals("")) {
		    	toDate = formatToFaple.format(cal.getTime()).toString();
		    }
		   	cal.add(Calendar.DATE, -3); 
		   	if(fromDate.equals("")) {
		   		fromDate = formatToFaple.format(cal.getTime()).toString();
		    }
		   	
		   	int paFapleContractCnt = Constants.PA_FAPLE_CONTRACT_CNT;
			//로컬 세팅
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
				paFapleContractCnt = 1;
			}
		   	
		   	for(int i = 0; i< paFapleContractCnt ; i ++) {
				
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				
				paramMap.put("paCode",paCode);
				
				//필요한 파라미터 및 통신 세팅
				// Body 세팅S
				ParamMap apiDataObject = new ParamMap();
				
				apiDataObject.put("Sdate", fromDate);
				apiDataObject.put("Edate", toDate);
				apiDataObject.put("status", 0);//처리상태( 0:전체, 1:미답변, 2:답변완료)
				
				//통신
				responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiDataObject);
				
				List<Map<String, Object>> resultList = (List<Map<String, Object>>)(responseMap.get("BBS_list"));
				List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
				
				if(resultList!=null) {
					for(Map<String, Object> m : resultList) {
						String statusName = m.get("status_name").toString();
						
						if(!"답변완료".equals(statusName)) {
							Paqnamoment paqnamoment = new Paqnamoment();
							PaFapleQnaVO paFapleQna = new PaFapleQnaVO();
							
							paqnamoment.setPaCode(paCode);
							paqnamoment.setPaGroupCode(Constants.PA_FAPLE_GROUP_CODE);
							paqnamoment.setPaCounselNo(m.get("numm").toString());
							paqnamoment.setPaOrderNo(String.valueOf(m.get("order_id")!= null ? m.get("order_id"):""));
							paqnamoment.setCounselDate(DateUtil.toTimestamp(m.get("created").toString(), "yyyy-MM-dd HH:mm:ss"));
							paqnamoment.setMsgGb(msgGb);
							paqnamoment.setInsertId(Constants.PA_FAPLE_PROC_ID);
							paqnamoment.setModifyId(Constants.PA_FAPLE_PROC_ID);
							paqnamoment.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paqnamoment.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
							switch(m.get("question_type_name").toString()) {
							case "상품":
								paqnamoment.setCounselGb("01"); 
								break;
							case "배송문의":
								paqnamoment.setCounselGb("02"); 				
								break;
							case "주문/결제":
								paqnamoment.setCounselGb("05"); 
								break;
							case "반품/교환":
								paqnamoment.setCounselGb("03"); 
								break;
							case "취소/환불":
								paqnamoment.setCounselGb("03"); 
								break;
							default:
								paqnamoment.setCounselGb("05"); 
								break;
							}
							paqnamoment.setPaGoodsDtCode("");
							paqnamoment.setOrderYn(String.valueOf(m.get("order_id")!= null ? "1" : "0"));
							paqnamoment.setDisplayYn("");
							paqnamoment.setPaCustNo("");
							paqnamoment.setCustTel("");
							paqnamoment.setTitle("[FAPLE] 고객게시판 문의");
							paqnamoment.setToken("0");
							paqnamoment.setQuestComment(m.get("body").toString());
							
							paFapleQna.setBbsKind(ComUtil.objToStr(m.get("bbs_kind")));        
							paFapleQna.setBbsId(ComUtil.objToStr(m.get("bbs_id")));          
							paFapleQna.setNumm(ComUtil.objToStr(m.get("numm")));          
							paFapleQna.setRef(ComUtil.objToStr(m.get("ref")));            
							paFapleQna.setStep(ComUtil.objToStr(m.get("step")));          
							paFapleQna.setRelevel(ComUtil.objToStr(m.get("relevel")));        
							
							if(paFapleCounselDAO.selectCountPaFapleQna(paFapleQna) < 1) {
								paFapleCounselDAO.insertPaFapleQna(paFapleQna); 	
							}
							
							if(String.valueOf(m.get("ItemNo"))!= null && !"".equals(String.valueOf(m.get("ItemNo")))) {
								String[] itemNo = m.get("ItemNo").toString().split("_");
								String paGoodsCode = paFapleCounselDAO.selectPaGoodsCode(itemNo[1]);
								paqnamoment.setPaGoodsCode(ComUtil.objToStr(paGoodsCode));
								
							}else {
								paqnamoment.setPaGoodsCode("");
							}
							paqnamomentList.add(paqnamoment);
						}
					}
					if(paqnamomentList.size() > 0) {
						rtnMsg = paCounselService.savePaQnaTx(paqnamomentList, msgGb);
					}
					
					if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
						paramMap.put("code","200");
						paramMap.put("message","OK");
					}else {
						paramMap.put("code","500");
						paramMap.put("message",rtnMsg);
					}
				}
				
				
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paFapleConnectUtil.checkException(paramMap ,  e);
		} finally {
			
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "CS 알리미 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			
			paFapleConnectUtil.closeApi(request, paramMap);
		}
		
		
		log.info("======= 패플 CS 알리미 조회 API End - {} =======");
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
		
	}
	
	/**
	 * 고객문의답변등록
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg goodsCounselProc(String paCounselNo, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PAFAPLEAPI_02_002";
		String duplicateCheck = "";
		int failCnt   = 0;
		int totalCnt  = 0;
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		log.info("======= 패션플러스 상품 문의 답변 등록 API Start - {} =======");
		try {
			
			paramMap.put("apiCode", apiCode);
			paFapleApiRequest.getApiInfo(paramMap);
			
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			/** 1) 전문 데이터 세팅  **/
			String dateTime = systemService.getSysdatetimeToString();
			
			if(String.valueOf(paCounselNo)!=null) {
				paramMap.put("paCounselNo", paCounselNo);
			}
			
			// 답변 내역 조회
			List<PaqnamVO> ansList = paFapleCounselDAO.selectPaFapleAnsQna(paramMap);
			totalCnt = ansList.size();
			
			for(PaqnamVO paQna : ansList) {
				try {
					// Query Parameters 세팅
					PaFapleQnaVO paFapleQna = new PaFapleQnaVO();
					paFapleQna = paFapleCounselDAO.selectPaFapleQnaDt(paQna);
					
					//필요한 파라미터 및 통신 세팅
					paramMap.put("paCode",paQna.getPaCode());
					// Body 세팅S
					Map<String, Object> BBSAdd = new HashMap<String, Object>();
					BBSAdd.put("bbs_kind",paFapleQna.getBbsKind());
					BBSAdd.put("transfer","N");
					BBSAdd.put("bbs_id",ComUtil.objToInt(paFapleQna.getBbsId()) );
					BBSAdd.put("numm",ComUtil.objToInt(paFapleQna.getNumm()));
					BBSAdd.put("ref", ComUtil.objToInt(paFapleQna.getRef()) );
					BBSAdd.put("step",ComUtil.objToInt(paFapleQna.getStep()) );
					BBSAdd.put("relevel",ComUtil.objToInt(paFapleQna.getRelevel()));
					BBSAdd.put("ReBody",ComUtil.subStringBytes(paQna.getProcNote(), 4000));
					
					List<Map<String, Object>> BBSAdd_list = new ArrayList<Map<String,Object>>();
					BBSAdd_list.add(BBSAdd);
					ParamMap apiDataObject = new ParamMap();
				    apiDataObject.put("BBSAdd_list", BBSAdd_list);
					
					//통신
					responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiDataObject);
					List<Map<String, Object>> orderList = (List<Map<String, Object>>) responseMap.get("OderList");
					if("OK".equals(orderList.get(0).get("Status")) || "Err-BBSA-002".equals(orderList.get(0).get("Status"))) {
						paramMap.put("code", "200");
						paramMap.put("message", "답변성공");
					}else {
						throw processException("errors.detail", new String[] {"패션플러스 상담 상품문의 답변 오류 "});						
					}
					
				} catch (TransApiException e) {
					failCnt++;
					paramMap.put("code", "400");
					paramMap.put("message", e.getMessage());
				} catch (Exception e) {
					log.info("{}: {} PA_COUNSEL_SEQ: {}", "패션플러스 상품문의 답변 오류", e.getMessage(), paQna.getPaCounselSeq());
					failCnt++;
					paramMap.put("code", "400");
					paramMap.put("message", e.getMessage());
					
				} finally {
					try {
						if("200".equals(paramMap.get("code"))) {
							paQna.setProcGb("40");
							paQna.setModifyId(Constants.PA_FAPLE_PROC_ID);
							paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paFapleCounselService.savePaFapleQnaTransTx(paQna);
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
				throw processException("errors.detail", new String[] {"패션플러스 상품문의 답변 등록 오류 - 실패:" + failCnt + "건 전체:" + totalCnt + "건"});
			}
			
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paFapleConnectUtil.checkException(paramMap ,  e);
		} finally {
			
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "상품 문의 답변 대상 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			
			paFapleConnectUtil.closeApi(request, paramMap);
		}
		
		
		log.info("======= 패션플러스 상품 문의 답변 등록 API End - {} =======");
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
		
	}
}
