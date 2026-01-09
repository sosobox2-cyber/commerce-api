package com.cware.netshopping.paqeen.process.impl;


import java.sql.Timestamp;
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
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pacommon.counsel.service.PaCounselService;
import com.cware.netshopping.paqeen.domain.OrderHistory;
import com.cware.netshopping.paqeen.domain.OrderSummary;
import com.cware.netshopping.paqeen.domain.QeenCounselComments;
import com.cware.netshopping.paqeen.domain.QeenCounselCommentsResponse;
import com.cware.netshopping.paqeen.domain.QeenCounselDetail;
import com.cware.netshopping.paqeen.domain.QeenCounselResponse;
import com.cware.netshopping.paqeen.domain.QeenCounselSummary;
import com.cware.netshopping.paqeen.message.CounselCommentsResoponseMsg;
import com.cware.netshopping.paqeen.message.CounselDetailResoponseMsg;
import com.cware.netshopping.paqeen.process.PaQeenCounselProcess;
import com.cware.netshopping.paqeen.repository.PaQeenCounselDAO;
import com.cware.netshopping.paqeen.service.PaQeenCounselService;
import com.cware.netshopping.paqeen.util.PaQeenApiRequest;
import com.cware.netshopping.paqeen.util.PaQeenConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("paqeen.counsel.paQeenCounselProcess")
public class PaQeenCounselProcessImpl extends AbstractProcess implements PaQeenCounselProcess {
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pacommon.counsel.paCounselService")
	private PaCounselService paCounselService;
	
	@Autowired
	private PaQeenConnectUtil paQeenConnectUtil;
	
	@Autowired
	private PaQeenApiRequest paQeenApiRequest;
	
    @Resource(name = "paqeen.counsel.paQeenCounselDAO")
    private PaQeenCounselDAO paQeenCounselDAO;
	
	@Autowired 
	@Qualifier("paqeen.counsel.paQeenCounselService")
	private PaQeenCounselService paQeenCounselService;
	
	/** 
	 * 퀸잇 문의 사항 목록 조회(목록조회 > 상세조회 > 댓글조회(댓글 존재하는 경우) )
	 */
	@Override
	public ResponseMsg getCxCounselList(String fromDate, String toDate, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PAQEENAPI_04_001";
		String duplicateCheck = "";
		String paCode  = "";
		String msgGb = "10";
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		log.info("======= 퀸잇 문의 사항 목록 조회 API Start - {} =======");
		try {
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			for(int i = 0; i< Constants.PA_QEEN_CONTRACT_CNT ; i ++) {
				
				paCode = (i==0 )? Constants.PA_QEEN_BROAD_CODE : Constants.PA_QEEN_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				
				List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
				
				
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
					from.add(Calendar.DATE, -10);
				}
				
				if (toDate != null && !toDate.equals("")) {
					to.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(toDate));
				} else {
					to.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateTime));
				}
				
				int page =1;
				
				boolean isNext = true;
				
				while(isNext) { 
					
					HashMap<String, String> getParameters = new HashMap<String, String>();
					//getParameters.put("orderId", pathParameters);
					getParameters.put("page", Integer.toString(page));
					getParameters.put("size", "30");
					getParameters.put("startAt", dateFormat.format(from.getTime()));
					getParameters.put("endAt", dateFormat.format(to.getTime()));
					getParameters.put("inquiryStatuses", "WAIT,BEFORE_WORK,IN_PROGRESS"); //WAIT,BEFORE_WORK,IN_PROGRESS,COMPLETED
					getParameters.put("orderBy", "ASC");//ASC,DESC (등록일 기준, 값없으면 최신순 정렬)
//					getParameters.put("writerType", "SELLER");//SELLER, CX , Seller가 보낸 문의를 조회하려면 SELLER, 받은 문의를 조회하려면 CX로 조회
					getParameters.put("writerType", "CX");//SELLER, CX , Seller가 보낸 문의를 조회하려면 SELLER, 받은 문의를 조회하려면 CX로 조회
					getParameters.put("newOnly", "false");//TRUE,FALSE CX팀이 답변을 달아 확인이 필요한 글, FALSE는 글 모두 조회
					
					//목록조회 api 통신
					responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, null, getParameters);
					
					// vo변환
					ObjectMapper objectMapper = new ObjectMapper();
					QeenCounselResponse qeenCounselResponse = objectMapper.convertValue(responseMap, QeenCounselResponse.class);
					
					List<QeenCounselSummary> cxSummaryList  = qeenCounselResponse.getList();
					
					String inquiryId = "";
					String paGoodsCode = "";
					String paOrderNo = "";
					
					if(cxSummaryList.isEmpty() || qeenCounselResponse.getTotalPageCount() == page) {
						isNext = false;
					}
					page ++;
					
					for(QeenCounselSummary cxSummary : cxSummaryList) {//목록조회
						
						Paqnamoment paqnamoment = new Paqnamoment();
						
						CounselDetailResoponseMsg detailResoponseMsg = null ; // 당사 상세조회 api 호출 후 리턴값
						CounselCommentsResoponseMsg commentsResoponseMsg = null;//당사 댓글 조회 api 호출 후 리턴값
						QeenCounselDetail detail = null;
						QeenCounselCommentsResponse commentsResponse = null;
						
						inquiryId = Integer.toString(cxSummary.getId());
						
						int totalCommentCount = cxSummary.getTotalCommentCount();
						
						ParamMap chkMap = new ParamMap();
						
						if (totalCommentCount <= 0) { //댓글없으면 최초 문의이므로 중복체크해서 상세조회만 태움.
							
							chkMap.put("paCounselNo",inquiryId);
							chkMap.put("token", inquiryId);
							chkMap.put("msgGb", msgGb);
							// 댓글 없는 경우 paCounselNo 중복 체크 , 댓글 존재하는 경우는 상세, 댓글 조회 둘다 태우기 
							if(paQeenCounselDAO.selectPaQnaCount(chkMap) > 0){
								continue; 
							}
						}
						
						// 문의 상세 조회 api 호출 S
						detailResoponseMsg = getCxCounselDetail( paCode, inquiryId, request);// 문의 상세조회
						
						detail  = detailResoponseMsg.getQeenCounselDetail();
						
						if(!"200".equals(detailResoponseMsg.getCode()) || detail == null ) {
							continue;
						}
						// 문의 상세 조회 api 호출 E
						
						
						
						// 문의 댓글 조회 api 호출 S
						List<QeenCounselComments> commentsList = null;
						if(totalCommentCount > 0) {
							
							commentsResoponseMsg = getCxCounselComments( paCode, inquiryId, request);// 문의 댓글조회
							commentsResponse = commentsResoponseMsg.getCommentsResponse();
							
							if(!"200".equals(commentsResoponseMsg.getCode()) || commentsResponse == null ) {
								continue;
							}
							
							commentsList = commentsResponse.getList();
							commentsList.sort((a, b) -> Integer.compare(a.getId(), b.getId())); //오름차순 정렬
							
							if("SELLER".equals(commentsList.get(commentsList.size()-1).getWriter().getType())) { // 가장 마지막 문의 SELLER면 패스
								continue;
							}
						}
						// 문의 댓글 조회 api 호출 E
						
						
						// paqnamoment 세팅 S
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String formattedTimestamp = sdf.format(detail.getCreatedAtMillis()); 
						
						Date date = sdf.parse(formattedTimestamp);
						Timestamp counselDate = new Timestamp(date.getTime());
						
						switch (detail.getType()) { 
						
						case "PRODUCT": //상품 자체에 대한 문의
							paqnamoment.setCounselGb("01");
							break;
						case "DELIVERY": //	DELIVERY 배송 관련 문의
							paqnamoment.setCounselGb("02");
							break;
						case "ORDER_CANCEL": //ORDER_CANCEL 주문 취소에 대한 문의
							paqnamoment.setCounselGb("03");
							break;
						case "PRODUCT_RETURN": //PRODUCT_RETURN 반품에 대한 문의
							paqnamoment.setCounselGb("04");
							break;
						case "PRODUCT_EXCHANGE": //PRODUCT_EXCHANGE 상품 교환에 대한 문의
							paqnamoment.setCounselGb("05");
							break;
						case "ETC": //ETC 기타 문의
							paqnamoment.setCounselGb("06");
							break;
						default:
							paqnamoment.setCounselGb("06");
							break;
						}
						
						if(detail.getOrderHistory() != null) {
							OrderHistory orderHistory = detail.getOrderHistory();
							OrderSummary orderSummary = orderHistory.getOrderSummary();
							
							paOrderNo =  orderSummary.getOrderId();
							//  한 주문에 a,b 상품 구매 시 문의를 상품 a,b 전부에 대하여 문의 가능하기 때문에 첫번째 로우에 있는 상품코드 가져오기로
							if(detail.getOrderLineIds()!=null && detail.getOrderLineIds().size() > 0) {
								paGoodsCode = paQeenCounselDAO.selectProposalId(detail.getOrderLineIds().get(0));
							}
							
						}
						
						paqnamoment.setPaGroupCode(Constants.PA_QEEN_GROUP_CODE);
						paqnamoment.setPaCode(paCode);
						paqnamoment.setPaCounselNo(inquiryId);
						
						paqnamoment.setTitle("퀸잇 문의"); 
						
						paqnamoment.setPaOrderNo(paOrderNo);
						paqnamoment.setPaGoodsCode(paGoodsCode);
						paqnamoment.setPaGoodsDtCode("");
						paqnamoment.setMsgGb(msgGb);
						paqnamoment.setOrderYn("".equals(paOrderNo) ? "0": "1");
						paqnamoment.setDisplayYn("");
						paqnamoment.setPaCustNo(""); 
						paqnamoment.setCustTel("");	
						paqnamoment.setInsertId(Constants.PA_QEEN_PROC_ID);
						paqnamoment.setModifyId(Constants.PA_QEEN_PROC_ID);
						paqnamoment.setInsertDate(sysdateTime);
						paqnamoment.setModifyDate(sysdateTime);
						
						String writer  =  "CX".equals(detail.getWriter().getType())  ?  "[퀸잇]" : "[스토아]" ;
						
						StringBuilder sb = new StringBuilder();
						
						StringBuilder sb2 = new StringBuilder();
						boolean isResult = false;
						
						//  해당 주문이 서로 다른 PRODUCT_ITEM_ID을 주문한 경우에만
						//  A상품 단품 a 2개 주문 후 해당 건에 대해서 문의 시 아래 내용 기재하지 않음(PRODUCT_ITEM_ID 동일 o)
						//  A상품 단품 a, b 주문한 경우 내용 기재 , A상품과 B상품 주문한 경우 내용 기재(PRODUCT_ITEM_ID 동일 x)
						if(detail.getOrderLineIds()!=null && detail.getOrderLineIds().size() > 0 ) {
							
							sb2.append("*퀸잇 복합 주문 문의 대상 상품 정보*");
							
							Map<String, String> dtCodeMap = new HashMap<String, String>();
							for( String orderline : detail.getOrderLineIds()) {
								ParamMap param = new ParamMap();
								param.put("orderId", paOrderNo);
								param.put("orderLineId", orderline);
								
								// 복합 주문인 경우에만(각각 다른 단품 or 다른 상품 조합하여 2개이상) 단품에 대한 정보를 문의 내용에 추가한다.
								HashMap<String, Object> result = paQeenCounselDAO.selectPaQeenOrderLineGoodsInfo(param);
								
								if(result!=null) {
									
									//  같은 단품에 대해서 중복 기재하지 않기 위함
									if(!"1".equals(dtCodeMap.get(result.get("PRODUCT_ITEM_ID").toString())) ) {
										dtCodeMap.put(result.get("PRODUCT_ITEM_ID").toString(), "1");
										
										sb2.append("\n"+ "-상품코드 : " + result.get("GOODS_CODE") 
										+ ", 단품코드 : " + result.get("GOODSDT_CODE")
										+ ", 상품명 : " + result.get("GOODS_NAME")
										+ ", 단품명 : " + result.get("GOODSDT_INFO")
												);
										
										isResult = true;
									}
									
								}
								
							}
							if(isResult) {
								sb.append("\n\n"+ sb2.toString()+"\n");
							}
							
						}
						
						String token = "";
						token = inquiryId;

						//본문 생성
						StringBuilder counselDetail = new StringBuilder();
						counselDetail.append("\n\n"+ writer);
						counselDetail.append("\n"+ detail.getContent());
						if(detail.getFileUrls().size() > 0) {
							counselDetail.append("\n"+  "-첨부파일: " +  detail.getFileUrls().toString().replace("[", "").replace("]", ""));
						}
						paqnamoment.setCounselDate(counselDate);
						paqnamoment.setQuestComment(sb.toString() + counselDetail.toString());// 문의내용
						paqnamoment.setToken(token);
						//중복 체크
						chkMap.put("paCounselNo",inquiryId);
						chkMap.put("token", token);
						chkMap.put("msgGb", msgGb);
						
						if(paQeenCounselDAO.selectPaQnaCount(chkMap) <= 0){ 
							paqnamomentList.add(paqnamoment);
						}
						
						//댓글 있을 경우
						if(commentsResponse!=null) { 
							
							for(QeenCounselComments comment : commentsList) { //댓글 합치지 않고, 하나씩 상담생성
								if("CX".equals(comment.getWriter().getType())) { //퀸잇 측에서 작성한 댓글만 생성.

									StringBuilder counselComment = new StringBuilder();
									
									counselComment.append("\n\n"+ "[퀸잇]");
									counselComment.append("\n"+ comment.getContent());
									
									if(comment.getFileUrls().size() > 0) {
										counselComment.append("\n"+ "-첨부파일: " +  comment.getFileUrls().toString().replace("[", "").replace("]", ""));
									}
									
									token = Integer.toString(comment.getId());
									
									//중복 체크
									chkMap.put("paCounselNo",inquiryId);
									chkMap.put("token", token);
									chkMap.put("msgGb", msgGb);
									if(paQeenCounselDAO.selectPaQnaCount(chkMap) > 0){ 
										continue; 
									}
									
									Paqnamoment paqnamoment2 = new Paqnamoment();

									paqnamoment2.setCounselGb(paqnamoment.getCounselGb());
									
									paqnamoment2.setPaGroupCode(paqnamoment.getPaGroupCode());
									paqnamoment2.setPaCode(paqnamoment.getPaCode());
									paqnamoment2.setPaCounselNo(paqnamoment.getPaCounselNo());
									
									paqnamoment2.setTitle(paqnamoment.getTitle()); 
									
									paqnamoment2.setPaOrderNo(paqnamoment.getPaOrderNo());
									paqnamoment2.setPaGoodsCode(paqnamoment.getPaGoodsCode());
									paqnamoment2.setPaGoodsDtCode(paqnamoment.getPaGoodsDtCode());
									paqnamoment2.setMsgGb(paqnamoment.getMsgGb());
									paqnamoment2.setOrderYn(paqnamoment.getOrderYn());
									paqnamoment2.setDisplayYn("");
									paqnamoment2.setPaCustNo(""); 
									paqnamoment2.setCustTel("");	
									paqnamoment2.setInsertId(Constants.PA_QEEN_PROC_ID);
									paqnamoment2.setModifyId(Constants.PA_QEEN_PROC_ID);
									paqnamoment2.setInsertDate(sysdateTime);
									paqnamoment2.setModifyDate(sysdateTime);

									date = sdf.parse(sdf.format(comment.getCreatedAtMillis()));
									counselDate = new Timestamp(date.getTime()); 
									paqnamoment2.setCounselDate(counselDate);
									paqnamoment2.setQuestComment(sb.toString() + counselComment.toString());// 문의내용
									paqnamoment2.setToken(token);
									
									
									
									paqnamomentList.add(paqnamoment2);
								}
							}
						}
						// paqnamoment 세팅 E
					}
					
				} //while
				
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
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paQeenConnectUtil.closeApi(request, paramMap);
		}
		
		
		
		log.info("======= 퀸잇 문의 사항 목록 조회 API End - {} =======");
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	
	/**
	 * 퀸잇 문의 사항 상세 조회
	 */
	@Override
	public CounselDetailResoponseMsg getCxCounselDetail(String paCode, String inquiryId, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PAQEENAPI_04_002";
		String duplicateCheck = "";
		// Path Parameters
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		log.info("======= 퀸잇 문의 사항 상세 조회 API Start - {} =======");
		QeenCounselDetail qeenCounselDetail = null;
		try {
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			paramMap.put("paCode", paCode);
			
			/** 1) 전문 데이터 세팅  **/
			
			paramMap.put("urlParameter", inquiryId);
			
			responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, null, null);
			
			// vo변환
			ObjectMapper objectMapper = new ObjectMapper();
			qeenCounselDetail = objectMapper.convertValue(responseMap, QeenCounselDetail.class);
			
			paramMap.put("code","200");
			paramMap.put("message","OK");
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paQeenConnectUtil.closeApi(request, paramMap);
		}
		
		
		log.info("======= 퀸잇 문의 사항 상세 조회 API End - {} =======");
		return new CounselDetailResoponseMsg(paramMap.getString("code"), paramMap.getString("message"), qeenCounselDetail);
	}
	
	/**
	 * 퀸잇 문의 사항 댓글 조회
	 */
	@Override
	public CounselCommentsResoponseMsg getCxCounselComments(String paCode, String inquiryId, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PAQEENAPI_04_003";
		String duplicateCheck = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		log.info("======= 퀸잇 문의 사항 댓글 조회 API Start - {} =======");
		
		QeenCounselCommentsResponse commentsResponse = null;
		
		try {
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			paramMap.put("paCode", paCode);
			
			/** 1) 전문 데이터 세팅  **/
			
			boolean isLast = false;
			List<QeenCounselComments> list = new ArrayList<QeenCounselComments>();
			String nextCursor = "";
			while(!isLast) {
				
				HashMap<String, String> getParameters = new HashMap<String, String>();
				getParameters.put("cursor", nextCursor);
				getParameters.put("size", "5");
				
				paramMap.put("urlParameter", inquiryId);
				responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, null, getParameters);
				
				// vo변환
				ObjectMapper objectMapper = new ObjectMapper();
				commentsResponse = objectMapper.convertValue(responseMap, QeenCounselCommentsResponse.class);
				isLast = commentsResponse.isLast();
				
				nextCursor = commentsResponse.getNextCursor();
				
				if (commentsResponse.getList() != null) {
					list.addAll(commentsResponse.getList());
				}else {
					isLast = false;
				}
				
			}
			
			commentsResponse.setList(list);
			
		
			paramMap.put("code","200");
			paramMap.put("message","OK");
			
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paQeenConnectUtil.closeApi(request, paramMap);
		}
		
		
		log.info("======= 퀸잇 문의 사항 댓글 조회 API End - {} =======");
		return new CounselCommentsResoponseMsg(paramMap.getString("code"), paramMap.getString("message"), commentsResponse);
	}
	
	
	/**
	 * 퀸잇 문의 답변 댓글 등록
	 */
	@SuppressWarnings("unused")
	@Override
	public ResponseMsg cxCounselProc(String paCounselNo, HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PAQEENAPI_04_004";
		String duplicateCheck = "";
		String paCode  = "";
		int failCnt   = 0;
		int totalCnt  = 0;
		
		Map<String, Object> responseMap = new HashMap<String, Object>() ;
		log.info("======= 퀸잇 문의 답변 댓글 등록 API Start - {} =======");
		try {
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			/** 1) 전문 데이터 세팅  **/
			String dateTime = systemService.getSysdatetimeToString();
			
			if(String.valueOf(paCounselNo)!=null) {
				paramMap.put("paCounselNo", paCounselNo);
			}
			
			/** 1) 전문 데이터 세팅  **/
			
			// 답변 내역 조회
			List<PaqnamVO> ansList = paQeenCounselDAO.selectCsAnsList(paramMap);
			totalCnt = ansList.size();
			
			for(PaqnamVO paQna : ansList) {
				
				try {
					paCode = paQna.getPaCode();
					
					paramMap.put("paCode", paCode);
					
					paramMap.put("urlParameter", paQna.getPaCounselNo());
					
					ParamMap apiRequestObject = new ParamMap();
					apiRequestObject.put("content", paQna.getProcNote());
					
					responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, null);
				
					// 통신 완료 후 후처리 
					paQna.setModifyId(Constants.PA_QEEN_PROC_ID);
					paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paQna.setProcGb("40"); // 완료
					paQna.setTitle("");
					
					paQeenCounselService.savePaQeenQnaTransTx(paQna);
					
					paramMap.put("code", "200");
					paramMap.put("message", "퀸잇 문의 답변 댓글 등록 성공");
				
				} catch (TransApiException e) {
					
					if(e.getMessage().contains("Illegal state")) {
						paramMap.put("code", "200");
						paramMap.put("message",e.getMessage());
					}else {
						paramMap.put("code", "400");
						paramMap.put("message", e.getMessage());
						failCnt++;
					}
					
				} catch (Exception e) {
					log.info("{}: {} PA_COUNSEL_SEQ: {}", "퀸잇 문의 답변 댓글 등록 오류", e.getMessage(), paQna.getPaCounselSeq());
					failCnt++;
					paramMap.put("code", "400");
					paramMap.put("message", e.getMessage());
				} finally {
					
					try {
						if("200".equals(paramMap.get("code"))) {
							if(paramMap.get("message").toString().contains("Illegal state")) {
								paQna.setTitle("처리 완료된 답변 or 삭제된 답변 입니다."); 
							}else {
								paQna.setTitle(""); 
							}
							
							paQna.setProcGb("40");
							paQna.setModifyId(Constants.PA_QEEN_PROC_ID);
							paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paQeenCounselService.savePaQeenQnaTransTx(paQna);
							paramMap.put("code", "200");
							paramMap.put("message", "퀸잇 문의 답변 댓글 등록 성공");
							
						}
					} catch (Exception e) {
							paramMap.put("code", "400");
							paramMap.put("message", e.getMessage());
					}
					
				}

			}
			
			if(failCnt > 0 ) {
				throw processException("errors.detail", new String[] {"퀸잇 문의 답변 댓글 등록 오류 - 실패:" + failCnt + "건 전체:" + totalCnt + "건"});
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "퀸잇 문의 답변 댓글 등록 대상 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			
			paQeenConnectUtil.closeApi(request, paramMap);
		}
		
		
		log.info("======= 퀸잇 문의 답변 댓글 등록 API End - {} =======");
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	
	/**
	 * 퀸잇 문의 답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	@Override
	public void savePaQeenQnaTrans(PaqnamVO paQna) throws Exception {
		int executedRtn = 0;
		String paCounselDtSeq = "";
		
		executedRtn = paQeenCounselDAO.updatePaQnaTrans(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAQNAM update" });
		}
		
		paCounselDtSeq = paQeenCounselDAO.selectPaQnaDtMaxSeq(paQna);
		paQna.setPaCounselDtSeq(paCounselDtSeq);
		
		executedRtn = paQeenCounselDAO.insertPaQnaDt(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",new String[] { "TPAQNADT insert" });
		}
	}
	
	
}
