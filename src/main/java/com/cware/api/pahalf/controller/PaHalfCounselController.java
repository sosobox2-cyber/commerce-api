package com.cware.api.pahalf.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pacommon.counsel.service.PaCounselService;
import com.cware.netshopping.pahalf.counsel.service.PaHalfCounselService;
import com.cware.netshopping.pahalf.util.PaHalfComUtill;
import com.cware.netshopping.pahalf.util.PaHalfConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/pahalf/counsel", description="하프클럽 상담")
@Controller("com.cware.api.pahalf.PaHalfCounselController")
@RequestMapping(value = "/pahalf/counsel")
public class PaHalfCounselController extends AbstractController {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	@Resource(name = "pahalf.counsel.paHalfCounselService")
	private PaHalfCounselService paHalfCounselService;

	@Resource(name = "pacommon.counsel.paCounselService")
	private PaCounselService paCounselService;

	@Autowired
	private PaHalfConnectUtil paHalfConnectUtil;

	/**
	 * CS관리 목록 조회
	 * 
	 * @param request
	 * @param basicDate
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "CS관리 목록 조회", notes = "CS관리 목록 조회", httpMethod = "GET", produces = "application/json")
	@RequestMapping(value = "/cs-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> csList(HttpServletRequest request,
			@ApiParam(name = "fromYMD", required = false, value = "FROM날짜", defaultValue = "") @RequestParam(value = "fromYMD", required = false, defaultValue = "") String fromYMD,
			@ApiParam(name = "toYMD", required = false, value = "TO날짜", defaultValue = "") @RequestParam(value = "toYMD", required = false, defaultValue = "") String toYMD) throws Exception {

		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		String prg_id = "IF_PAHALFAPI_02_001";
		String paCode = "";
		int failCnt   = 0;
		
		log.info("===== CS 관리 목록 조회 API Start=====");

		try {
			String dateTime = systemService.getSysdatetimeToString();
			// =Step 1)중복체크
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);
			// =Step 2)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅

			// 날짜가 비어있는 경우 기간입력
			if ((fromYMD == null || fromYMD.equals("")) || (toYMD == null || toYMD.equals(""))) {
				fromYMD =  DateUtil.addDay(DateUtil.getCurrentDateAsString(), -3, DateUtil.GENERAL_DATE_FORMAT);
				toYMD =  DateUtil.addDay(DateUtil.getCurrentDateAsString(), 0, DateUtil.GENERAL_DATE_FORMAT);
			}

			// 쿼리 만들어주기
			apiDataMap.put("fromYMD", fromYMD);
			apiDataMap.put("toYMD", toYMD);
			apiDataMap.put("siteCd", "1");
			apiInfoMap.put("queryString", PaHalfComUtill.makeQuryString(apiDataMap));

			for(int q = 0; q < Constants.PA_HALF_CONTRACT_CNT; q++) {
				
				paCode = (q == 0) ? Constants.PA_HALF_ONLINE_CODE : Constants.PA_HALF_BROAD_CODE;
				apiInfoMap.put("paCode", paCode);
				
				try {
					insertCsData(apiInfoMap, apiDataMap, dateTime);
				} catch (Exception e) {
					log.info("{}: {} FROM_DATE: {} TO_DATE: {}", "하프클럽 CS관리 목록 조회 오류", PaHalfComUtill.getErrorMessage(e), fromYMD, toYMD);
					failCnt++;
				}
			}
			if(failCnt>0) {
				throw processException("errors.detail", new String[] {"하프클럽 CS관리 목록 조회 오류 "});
			}
 
		} catch (Exception e) {
			log.info("{}: {}", "하프클럽 CS관리 목록 조회", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
			log.info("===== CS 관리 목록 조회 API End=====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	//CS 관리 목록 조회
	public void insertCsData(ParamMap apiInfoMap, ParamMap apiDataMap, String dateTime) throws Exception{
		
		List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();
		// 통신
		Map<String, Object> resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
		List<Map<String, Object>> apiResultList = PaHalfComUtill.map2List(resultMap);

		if(apiResultList.size() < 1) {
			apiInfoMap.put("message", "조회된 CS가 없습니다");
			return;
		}
		
		for (Map<String, Object> csData : apiResultList) {
			// 문의상태 0:질문, 1:답변완료, 9:문의종료
			String isFinish = String.valueOf(csData.get("isFinish"));
			// 협력사는 comType = 'H' 인 내용만 답변 할수 있음. C는 협력사가 문의한 내용
			String comType = String.valueOf(csData.get("comType"));
			if (!"0".equals(isFinish) || !"H".contentEquals(comType)) {
				continue;
			}

			Paqnamoment paqnamoment = new Paqnamoment();
			paqnamoment.setPaCode(apiInfoMap.getString("paCode"));
			paqnamoment.setPaGroupCode(Constants.PA_HALF_GROUP_CODE);
			paqnamoment.setMsgGb("10");
			paqnamoment.setPaCounselNo(String.valueOf(csData.get("qacd")));
			paqnamoment.setCounselDate(DateUtil.toTimestamp(String.valueOf(csData.get("qcreateDt")), "yyyy/MM/dd HH:mm:ss"));
			paqnamoment.setTitle((String) csData.get("qtitle"));
			paqnamoment.setQuestComment((String) csData.get("qcontents"));
			paqnamoment.setCounselGb("07");
			paqnamoment.setPaGoodsCode(String.valueOf(csData.get("prdNo")));
			String ordNo = String.valueOf(csData.get("ordNo"));
			if ( ordNo != null) {
				paqnamoment.setPaOrderNo(ordNo.trim());
			}
			paqnamoment.setOrderYn(StringUtils.isBlank(ordNo) ? "0" : "1");
			paqnamoment.setDisplayYn("0");
			paqnamoment.setPaCustNo(String.valueOf(csData.get("qcreateId")));
			paqnamoment.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paqnamoment.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paqnamoment.setInsertId(Constants.PA_HALF_PROC_ID);
			paqnamoment.setModifyId(Constants.PA_HALF_PROC_ID);

			paqnamomentList.add(paqnamoment);
		}
		if(paqnamomentList.size()>0) {
			paCounselService.savePaQnaTx(paqnamomentList, "10");
		}
	}
	
	/**
	 * CS답변 등록
	 * 
	 * @param request
	 * @param basicDate
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "CS답변 등록", notes = "CS답변 등록", httpMethod = "GET", produces = "application/json")
	@RequestMapping(value = "/cs-ans-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> csProc(HttpServletRequest request) throws Exception {
	
		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		ParamMap paramMap = new ParamMap();
		String prg_id = "IF_PAHALFAPI_02_002";
		int failCnt   = 0;
		int totalCnt  = 0;
		log.info("===== CS답변 등록 API Start=====");
	
		try {
			String dateTime = systemService.getSysdatetimeToString();
			// =Step 1)중복체크
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);
			// =Step 2)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
	
			paramMap.put("msgGb", "10");
			List<PaqnamVO> ansList = paHalfCounselService.selectPaHalfAnsQna(paramMap);
			totalCnt = ansList.size();
			for(PaqnamVO paQna : ansList) {
				try {
					apiDataMap.put("qacd", paQna.getPaCounselNo());
					apiDataMap.put("atitle", paQna.getTitle());
					if (paQna.getProcNote().length() < 10) {
						apiDataMap.put("acontents", "[SK스토아 답변내용]" + paQna.getProcNote()); // 답변내용(최소10자,최대1500자)
					} else {
						apiDataMap.put("acontents", paQna.getProcNote()); // 답변 내용 (최소10자, 최대1500자)
					}

					apiInfoMap.put("paCode", paQna.getPaCode());
					paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
				
					paQna.setTitle(""); 
					paQna.setProcGb("40");
					paQna.setModifyId(Constants.PA_HALF_PROC_ID);
					paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paHalfCounselService.savePaHalfQnaTransTx(paQna);
				} catch (Exception e) {
					String errorMsg = PaHalfComUtill.getErrorMessage(e);
					if(errorMsg.contains("중복") || errorMsg.contains("존재하지")) {
						paQna.setTitle("중복답변 혹은 삭제된 문의입니다"); 
						paQna.setProcGb("40");
						paQna.setModifyId(Constants.PA_HALF_PROC_ID);
						paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paHalfCounselService.savePaHalfQnaTransTx(paQna);
					}else {
						log.info("{}: {} PA_COUNSEL_SEQ: {}", "하프클럽 CS 답변 등록 오류", PaHalfComUtill.getErrorMessage(e), paQna.getPaCounselSeq());
						failCnt++;
					}
				}
			}
			
			if(failCnt > 0 ) {
				throw processException("errors.detail", new String[] {"하프클럽 CS 답변 등록 오류 - 실패:" + failCnt + "건 전체:" + totalCnt + "건"});
			}			
			
				
		} catch (Exception e) {
			log.info("{}: {}", "하프클럽 CS 답변 등록", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e); 
		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
			log.info("===== CS답변 등록  API End=====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * QnA목록 조회
	 * @param request
	 * @param strDt
	 * @param endDt
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "QnA목록 조회", notes = "QnA목록 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/qna-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> qnaList(HttpServletRequest request,
			@ApiParam(name = "strDt", required = false, value = "start날짜", defaultValue = "") @RequestParam(value = "strDt", required = false, defaultValue = "") String strDt,
			@ApiParam(name = "endDt", required = false, value = "end날짜",   defaultValue = "") @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt)
			throws Exception {

		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		String prg_id       = "IF_PAHALFAPI_02_003";
		String startDate    = "";
		String endDate      = "";
		String dateTime     = systemService.getSysdatetimeToString();
		String paCode		= "";
		int failCnt         = 0;

		log.info("=====하프클럽 Q&A 목록 조회 API Start=====");

		try {
			// =Step 1)중복체크
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);
			// =Step 2)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅

			// 기간입력
			if (!(strDt.isEmpty() || strDt.equals("")) && !(endDt.isEmpty() || endDt.equals(""))) {
				startDate = strDt;
				endDate = endDt;
			}
			// 예외
			else {
				startDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), -3, DateUtil.GENERAL_DATE_FORMAT);
				endDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), 0, DateUtil.GENERAL_DATE_FORMAT);
			}

			// =Step 3)DATA Setting
			apiDataMap.put("siteCd", "1");
			apiDataMap.put("strDt", startDate);
			apiDataMap.put("endDt", endDate);
			apiInfoMap.put("queryString", PaHalfComUtill.makeQuryString(apiDataMap));
			
			for(int i = 0; i < Constants.PA_HALF_CONTRACT_CNT ; i++ ) {
				paCode = (i==0 )? Constants.PA_HALF_BROAD_CODE : Constants.PA_HALF_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				try {
					// =Step 4)Connect and Get API Response
					Map<String, Object> resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);

					// =API DATA GET
					List<Map<String, Object>> apiResultList = PaHalfComUtill.map2List(resultMap);

					if (apiResultList == null || apiResultList.size() <= 0) continue;

					List<Paqnamoment> paqnamomentList = new ArrayList<Paqnamoment>();

					Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);

					for (Map<String, Object> qna : apiResultList) {
						
						String qnaTypeCd = (String) qna.get("qnaTypeCd");
						
						if ("Y".equals(qna.get("delYn"))) { // 삭제된건 저장 x
							continue;
						} else if (qna.get("ordNum") == null && !"MB082".equals(qnaTypeCd)) {
							continue;
						}
						
						Paqnamoment paqnamoment = new Paqnamoment();
						
						paqnamoment.setPaCode(apiInfoMap.getString("paCode"));
						paqnamoment.setPaGroupCode(Constants.PA_HALF_GROUP_CODE);
						paqnamoment.setPaCounselNo(String.valueOf(qna.get("qnaNo")));

						// 밀리세컨드 삭제
						String createDt = String.valueOf(qna.get("createDt"));
						String[] createDtArray = createDt.split("\\.");

						paqnamoment.setCounselDate(DateUtil.toTimestamp(createDtArray[0], "yyyy-MM-dd HH:mm:ss"));
						paqnamoment.setTitle(String.valueOf(qna.get("title")));
						paqnamoment.setQuestComment(String.valueOf(qna.get("qustTxt")));
						
						if ("MB082".equals(qnaTypeCd)) {
							paqnamoment.setCounselGb("01");
						} else {
							paqnamoment.setCounselGb("05");
						}
						
						if (qna.get("ordNum") != null) {
							paqnamoment.setPaOrderNo(String.valueOf(qna.get("ordNum")).trim());
						}
						
						paqnamoment.setPaGoodsCode(String.valueOf(qna.get("prdNo")));
						paqnamoment.setOrderYn(StringUtils.isBlank(paqnamoment.getPaOrderNo()) ? "0" : "1");
						paqnamoment.setDisplayYn("0");
						paqnamoment.setMsgGb("00");
						paqnamoment.setToken((String) qna.get("qnaTypeCd") + (String)qna.get("qnaTypeDetailCd"));
						paqnamoment.setInsertId(Constants.PA_HALF_PROC_ID);
						paqnamoment.setModifyId(Constants.PA_HALF_PROC_ID);
						paqnamoment.setInsertDate(sysdateTime);
						paqnamoment.setModifyDate(sysdateTime);
						paqnamomentList.add(paqnamoment);
					}
					if(paqnamomentList.size()>0) {
						paCounselService.savePaQnaTx(paqnamomentList, "00");
					}
				}catch (Exception e) {
					log.info("{}: {} FROM_DATE: {} TO_DATE: {}", "하프클럽 Q&A목록 조회 오류", PaHalfComUtill.getErrorMessage(e), strDt, endDt);
					failCnt++;
				}
				
			}
			if(failCnt > 0 ) {
				throw processException("errors.detail", new String[] {"하프클럽  Q&A목록 조회 오류 "});
			}
		} catch (Exception e) {
			log.info("{}: {}", "하프클럽 Q&A목록 조회", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);

		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
			log.info("=====하프클럽 Q&A 목록 조회 API End=====");

		}

		return new ResponseEntity<ResponseMsg>(
				new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * qna 답변 등록
	 * @param request
	 * @param strDt
	 * @param endDt
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "QnA답변 등록", notes = "QnA답변 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/qna-answer-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> qnaAnswerInsert(HttpServletRequest request) throws Exception {

		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		String prg_id = "IF_PAHALFAPI_02_004";
		
		ParamMap paramMap = new ParamMap();
		int failCnt = 0;
		int totalCnt = 0;

		log.info("===== 하프클럽 Q&A 답변등록 API Start=====");

		try {
			// =Step 1)중복체크
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);
			// =Step 2)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			String dateTime = systemService.getSysdatetimeToString();

			paramMap.put("msgGb", "00");

			List<PaqnamVO> ansList = paHalfCounselService.selectPaHalfAnsQna(paramMap); //sk스토아에서 답변완료된 리스트
			
			totalCnt = ansList.size();
			if(ansList ==null || ansList.size()<=0) {
				apiInfoMap.put("code", "404");
				apiInfoMap.put("message", getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			for(PaqnamVO paQna : ansList) {
				try {
					// request 세팅
					apiDataMap.put("qnaNo", paQna.getPaCounselNo()); 
					apiDataMap.put("title",	paQna.getTitle());
					apiDataMap.put("qustTxt", paQna.getProcNote()); 
					
					if (paQna.getProcNote().length() < 10) {
						apiDataMap.put("qustTxt","[SK스토아 답변내용]" + paQna.getProcNote()); // 답변내용(최소10자,최대1500자)
					} else {
						apiDataMap.put("qustTxt", paQna.getProcNote()); // 답변 내용 (최소10자, 최대1500자)
					}

					// 통신
					apiInfoMap.put("paCode", paQna.getPaCode());
					@SuppressWarnings("unused")
					Map<String, Object> resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
					
					paQna.setModifyId(Constants.PA_HALF_PROC_ID);
					paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paQna.setProcGb("40"); // = 완료
					paQna.setTitle("");
					paHalfCounselService.savePaHalfQnaTransTx(paQna);
					
				} catch (Exception e) {	
					
					String errorMessage = PaHalfComUtill.getErrorMessage(e);
				
					if (errorMessage.contains("중복") || errorMessage.contains("존재하지")) {
						paQna.setModifyId(Constants.PA_HALF_PROC_ID);
						paQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paQna.setProcGb("40"); // = 완료
						paQna.setTitle("중복답변 혹은 삭제된 문의입니다");
					
						paHalfCounselService.savePaHalfQnaTransTx(paQna);
					} else { 
						failCnt++;
						log.info("{}: {} PA_COUNSEL_SEQ: {}", "하프클럽 Q&A 답변 등록 오류", PaHalfComUtill.getErrorMessage(e), paQna.getPaCounselSeq());
					}

                }

			}
			
			if(failCnt > 0 ) {
				throw processException("errors.detail", new String[] {"하프클럽 Q&A 답변 등록 오류  - 실패:" + failCnt + "건 전체:" + totalCnt + "건"});
			}
			
		} catch (Exception e) {
			log.info("{}: {}", "하프클럽 Q&A 답변 등록 오류", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
			log.info("=====하프클럽 Q&A 답변 등록 API End=====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	
}
