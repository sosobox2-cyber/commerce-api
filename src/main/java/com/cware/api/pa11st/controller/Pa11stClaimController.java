package com.cware.api.pa11st.controller;

import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.Pa11stclaimlistVO;
import com.cware.netshopping.pa11st.claim.service.Pa11stClaimService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "/pa11st/claim", description="반품")
@Controller("com.cware.api.pa11st.Pa11stClaimController")
@RequestMapping(value = "/pa11st/claim")
@EnableAsync
public class Pa11stClaimController extends AbstractController {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	@Resource(name = "pa11st.claim.pa11stClaimService")
	private Pa11stClaimService pa11stClaimService;

	@Resource(name = "com.cware.api.pa11st.Pa11stAsycController")
	private Pa11stAsycController asycController;

	/**
	 * 반품신청목록조회(반품 요청 목록조회) IF_PA11STAPI_03_010
	 * 
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "반품신청목록조회", notes = "반품신청목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnList(
			HttpServletRequest request,
			@ApiParam(name = "fromDate", value = "검색시작일[yyyyMMddhhmm]", defaultValue = "") @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
			@ApiParam(name = "toDate", value = "검색종료일[yyyyMMddhhmm]", defaultValue = "") @RequestParam(value = "toDate", required = false, defaultValue = "") String toDate)
			throws Exception {

		/**
		 * 11번가 날짜 전달 파라미터 날짜포맷 : 년(4) 월(2) 일(2) 시(2) 분(2) 기간입력 Default D-2 ~ D
		 * */

		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String dateTime = "";
		HttpURLConnection conn = null;

		Document doc = null;
		String duplicateCheck = "";
		NodeList descNodes = null;
		String startDate = "";
		String endDate = "";
		String procPaCode = "";
		String paCode = "";
		Pa11stclaimlistVO pa11stclaimlist = null;

		log.info("===== 반품신청목록조회 API Start=====");
		// log.info("01.API 기본정보 세팅");

		// = connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_010";
		String request_type = "GET";
		dateTime = systemService.getSysdatetimeToString();

		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);

		// For Testing
		// orderClaimMain4Test(request);

		try {
			// log.info("02.API 중복실행검사");
			// = 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated",
						new String[] { prg_id });

			apiInfo = systemService.selectPaApiInfo(paramMap);

			// 기간입력
			if (!(fromDate.isEmpty() || fromDate.equals(""))
					&& !(toDate.isEmpty() || toDate.equals(""))) {
				startDate = fromDate;
				endDate = toDate;
			}
			// 예외(Default D-2 ~ D)
			else {
				endDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(),
						+1, DateUtil.GENERAL_DATE_FORMAT) + "0000";
				startDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(),
						-2, DateUtil.GENERAL_DATE_FORMAT)
						+ DateUtil.getCurrentHourAsString()
						+ DateUtil.getCurrentMinuteAsString();
			}

			String parameter = "/" + startDate + "/" + endDate;

			for (int count = 0; count < Constants.PA_11ST_CONTRACT_CNT; count++) {

				if (count == 0) {
					procPaCode = Constants.PA_11ST_ONLINE;
					paCode = Constants.PA_11ST_ONLINE_CODE;
				} else {
					procPaCode = Constants.PA_11ST_BROAD;
					paCode = Constants.PA_11ST_BROAD_CODE;
				}

				log.info("03.반품신청목록 조회 API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo, procPaCode,
						request_type, parameter);

				// RESPONSE XML
				doc = ComUtil.pa11stParseXML(conn.getInputStream());
				descNodes = doc.getElementsByTagName("ns2:orders");

				conn.disconnect();

				List<ParamMap> responseList = new ArrayList<ParamMap>();
				List<Pa11stclaimlistVO> arrPa11stclaimlist = new ArrayList<Pa11stclaimlistVO>();

				for (int j = 0; j < descNodes.getLength(); j++) {
					for (Node node = descNodes.item(j).getFirstChild(); node != null; node = node
							.getNextSibling()) { // 첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
						if (node.getNodeName().trim().equals("ns2:order")) {
							ParamMap pa11stParamMap = new ParamMap();
							for (int i = 0; i < node.getChildNodes()
									.getLength(); i++) {
								Node directionList = node.getChildNodes().item(
										i);
								pa11stParamMap.put(directionList.getNodeName()
										.trim(), directionList.getTextContent()
										.trim());
							}
							responseList.add(pa11stParamMap);
						} else {
							paramMap.put(node.getNodeName().trim(), node
									.getTextContent().trim());
						}
					}
				}

				if (responseList.size() > 0) {
					Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
					for (int i = 0; i < responseList.size(); i++) {
						pa11stclaimlist = new Pa11stclaimlistVO();

						pa11stclaimlist.setPaCode(paCode);
						pa11stclaimlist.setOrdNo(responseList.get(i).getString(
								"ordNo"));
						pa11stclaimlist.setOrdPrdSeq(responseList.get(i)
								.getString("ordPrdSeq"));
						pa11stclaimlist.setClmReqSeq(responseList.get(i)
								.getString("clmReqSeq"));
						pa11stclaimlist.setPaOrderGb("30");
						pa11stclaimlist.setAffliateBndlDlvSeq(responseList.get(
								i).getString("affliateBndlDlvSeq"));
						pa11stclaimlist.setClmReqCont(responseList.get(i)
								.getString("clmReqCont"));
						pa11stclaimlist.setClmReqQty(responseList.get(i)
								.getInt("clmReqQty"));
						pa11stclaimlist.setClmReqRsn(responseList.get(i)
								.getString("clmReqRsn"));
						pa11stclaimlist.setClmStat(responseList.get(i)
								.getString("clmStat"));
						pa11stclaimlist.setOptName(responseList.get(i)
								.getString("optName"));
						pa11stclaimlist.setPrdNo(responseList.get(i).getLong(
								"prdNo"));
						pa11stclaimlist.setReqDt(DateUtil.toTimestamp(
								responseList.get(i).getString("reqDt"),
								"yyyy-MM-dd HH:mm:ss"));
						pa11stclaimlist.setOrdNm(responseList.get(i).getString(
								"ordNm"));
						pa11stclaimlist.setOrdTlphnNo(responseList.get(i)
								.getString("ordTlphnNo"));
						pa11stclaimlist.setOrdPrtblTel(responseList.get(i)
								.getString("ordPrtblTel"));
						pa11stclaimlist.setRcvrMailNo(responseList.get(i)
								.getString("rcvrMailNo"));
						pa11stclaimlist.setRcvrMailNoSeq(responseList.get(i)
								.getString("rcvrMailNoSeq"));
						pa11stclaimlist.setRcvrBaseAddr(responseList.get(i)
								.getString("rcvrBaseAddr"));
						pa11stclaimlist.setRcvrDtlsAddr(responseList.get(i)
								.getString("rcvrDtlsAddr"));
						pa11stclaimlist.setRcvrTypeAdd(responseList.get(i)
								.getString("rcvrTypeAdd"));
						pa11stclaimlist.setRcvrTypeBilNo(responseList.get(i)
								.getString("rcvrTypeBilNo"));
						pa11stclaimlist.setTwMthd(responseList.get(i)
								.getString("twMthd"));
						pa11stclaimlist.setExchNm(responseList.get(i)
								.getString("exchNm"));
						pa11stclaimlist.setExchTlphnNo(responseList.get(i)
								.getString("exchTlphnNo"));
						pa11stclaimlist.setExchPrtbTel(responseList.get(i)
								.getString("exchPrtbTel"));
						pa11stclaimlist.setExchMailNo(responseList.get(i)
								.getString("exchMailNo"));
						pa11stclaimlist.setExchMailNoSeq(responseList.get(i)
								.getString("exchMailNoSeq"));
						pa11stclaimlist.setExchBaseAddr(responseList.get(i)
								.getString("exchBaseAddr"));
						pa11stclaimlist.setExchDtlsAddr(responseList.get(i)
								.getString("exchDtlsAddr"));
						pa11stclaimlist.setExchTypeAdd(responseList.get(i)
								.getString("exchTypeAdd"));
						pa11stclaimlist.setExchTypeBilNo(responseList.get(i)
								.getString("exchTypeBilNo"));
						pa11stclaimlist.setClmLstDlvCst(responseList.get(i)
								.getInt("clmLstDlvCst"));
						pa11stclaimlist.setAppmtDlvCst(responseList.get(i)
								.getInt("appmtDlvCst"));
						pa11stclaimlist.setClmDlvCstMthd(responseList.get(i)
								.getString("clmDlvCstMthd"));
						pa11stclaimlist.setTwPrdInvcNo(responseList.get(i)
								.getString("twPrdInvcNo"));
						pa11stclaimlist.setDlvEtprsCd(responseList.get(i)
								.getString("dlvEtprsCd"));
						pa11stclaimlist.setInsertDate(sysdateTime);
						pa11stclaimlist.setModifyDate(sysdateTime);

						arrPa11stclaimlist.add(pa11stclaimlist);
					}

					pa11stClaimService.saveReturnListTx(arrPa11stclaimlist);

					paramMap.put("code", "200");
					paramMap.put("message", "OK");

				} else if (paramMap.getString("ns2:result_code").equals("-1")) {
					paramMap.put("code", "200");
					paramMap.put("message",
							paramMap.getString("ns2:result_text"));
					log.info(paramMap.getString("ns2:result_text"));
				} else {
					// API 연결 실패
					paramMap.put("code", "500");
					paramMap.put(
							"message",
							getMessage("errors.exist",
									new String[] { "returnList" }));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(
							paramMap.getString("code"),
							paramMap.getString("message")), HttpStatus.OK);
				}
			}

		} catch (Exception e) {
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			if (e.getMessage() != null) {
				paramMap.put("message", e.getMessage().length() > 3950 ? e
						.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			} else {
				log.error(paramMap.getString("message"), e);
			}
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(
					HttpStatus.OK.value(), paramMap.getString("code"),
					paramMap.getString("message")), HttpStatus.OK);
		} finally {
			if (conn != null)
				conn.disconnect();
			try {
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}

			log.info("04.반품신청목록 조회 API END");
			orderClaimMain(request); // = 반품 데이터 생성.
		}

		return new ResponseEntity<ResponseMsg>(new ResponseMsg(
				HttpStatus.OK.value(), paramMap.getString("code"),
				paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 반품승인처리 IF_PA11STAPI_03_011
	 * 
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품승인처리", notes = "반품승인처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-confirm-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnConfirmProc(HttpServletRequest request)
			throws Exception {
		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		String msg = "";
		int targetCount = 0;
		int successCount = 0;
		int failCount = 0;
		String duplicateCheck = "";
		StringBuffer sb = new StringBuffer();
		HashMap<String, Object> returnMap = null;
		ParamMap resultMap = null;
		HashMap<String, String> apiInfo = new HashMap<String, String>();

		log.info("===== 반품승인처리 Start=====");
		// = connectionSetting 설정
		dateTime = systemService.getSysdatetimeToString();
		String prg_id = "IF_PA11STAPI_03_011";

		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);

		try {

			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated",
						new String[] { prg_id });

			log.info("01.회수확정 처리 건 조회");
			List<Object> returnList = pa11stClaimService.selectReturnOkList();

			ParamMap apiMap = new ParamMap();
			apiMap.put("apiCode", prg_id);
			apiMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
			apiMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
			apiInfo = systemService.selectPaApiInfo(apiMap);

			log.info("02.반품승인처리 API 호출");
			if (returnList != null) {
				if (returnList.size() > 0) {
					targetCount = returnList.size();
					for (int i = 0; i < returnList.size(); i++) {
						returnMap = (HashMap<String, Object>) returnList.get(i);
						returnMap.put("apiInfo", apiInfo);
						try {
							// = 반품승인처리 API 호출
							resultMap = pa11stClaimService
									.saveReturnConfirmProcTx(returnMap);
							if (resultMap.getString("rtnMsg").equals(Constants.SAVE_SUCCESS)) {
								successCount = successCount
										+ resultMap.getInt("successCnt");
							} else {
								failCount = failCount
										+ resultMap.getInt("failCnt");
								log.info(returnMap.get("MAPPING_SEQ")
										.toString() + ": 반품승인처리 fail - " + "|");
								sb.append(returnMap.get("MAPPING_SEQ")
										.toString() + ": 반품승인처리 fail - " + "|");
							}
						} catch (Exception e) {
							log.info(returnMap.get("MAPPING_SEQ").toString()
									+ ": 반품승인처리 fail - " + e.getMessage() + "|");
							sb.append(returnMap.get("MAPPING_SEQ").toString()
									+ ": 반품승인처리 fail - " + e.getMessage() + "|");
							continue;
						}
					}
				}
			}
		} catch (Exception e) {
			msg = "대상건수:" + targetCount + ", 성공건수:" + successCount + ", 실패건수:"
					+ failCount + "|";

			paramMap.put("code", "500");
			paramMap.put("message", msg + e.getMessage());
			log.error(msg + " : " + paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(
					HttpStatus.OK.value(), paramMap.getString("code"),
					paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				if (!paramMap.getString("code").equals("500")) {
					msg = "대상건수:" + targetCount + ", 성공건수:" + successCount
							+ ", 실패건수:" + failCount + "|";

					// 대상건수 모두 성공하였을 경우
					if (targetCount == successCount) {
						paramMap.put("code", "200");
					} else {
						paramMap.put("code", "500");
					}
					paramMap.put("message", msg + sb.toString());
				}
				systemService.insertApiTrackingTx(request, paramMap);

				if (duplicateCheck.equals("0")) {
					systemService.checkCloseHistoryTx("end", prg_id);
				}
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			log.info("03.반품승인처리 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"),
				HttpStatus.OK);
	}

	/**
	 * 반품철회목록조회 IF_PA11STAPI_03_012
	 * 
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "반품철회목록조회", notes = "반품철회목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-cancel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnCancelList(
			HttpServletRequest request,
			@ApiParam(name = "fromDate", value = "검색시작일[yyyyMMddhhmm]", defaultValue = "") @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
			@ApiParam(name = "toDate", value = "검색종료일[yyyyMMddhhmm]", defaultValue = "") @RequestParam(value = "toDate", required = false, defaultValue = "") String toDate)
			throws Exception {

		/**
		 * 11번가 날짜 전달 파라미터 날짜포맷 : 년(4) 월(2) 일(2) 시(2) 분(2) 기간입력 Default D-2 ~ D
		 * */

		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String dateTime = "";
		HttpURLConnection conn = null;

		Document doc = null;
		String duplicateCheck = "";
		NodeList descNodes = null;
		String startDate = "";
		String endDate = "";
		String procPaCode = "";
		String paCode = "";
		Pa11stclaimlistVO pa11stclaimlist = null;

		log.info("===== 반품철회목록조회 API Start=====");
		// log.info("01.API 기본정보 세팅");

		// = connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_012";
		String request_type = "GET";
		dateTime = systemService.getSysdatetimeToString();

		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);

		try {
			// log.info("02.API 중복실행검사");
			// = 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated",
						new String[] { prg_id });

			apiInfo = systemService.selectPaApiInfo(paramMap);

			// 기간입력
			if (!(fromDate.isEmpty() || fromDate.equals(""))
					&& !(toDate.isEmpty() || toDate.equals(""))) {
				startDate = fromDate;
				endDate = toDate;
			}
			// 예외(Default D-2 ~ D)
			else {
				endDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(),
						+1, DateUtil.GENERAL_DATE_FORMAT) + "0000";
				startDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(),
						-2, DateUtil.GENERAL_DATE_FORMAT)
						+ DateUtil.getCurrentHourAsString()
						+ DateUtil.getCurrentMinuteAsString();
			}

			String parameter = "/" + startDate + "/" + endDate;

			for (int count = 0; count < Constants.PA_11ST_CONTRACT_CNT; count++) {

				if (count == 0) {
					procPaCode = Constants.PA_11ST_ONLINE;
					paCode = Constants.PA_11ST_ONLINE_CODE;
				} else {
					procPaCode = Constants.PA_11ST_BROAD;
					paCode = Constants.PA_11ST_BROAD_CODE;
				}

				log.info("03.반품철회목록 조회 API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo, procPaCode,
						request_type, parameter);

				// RESPONSE XML
				doc = ComUtil.pa11stParseXML(conn.getInputStream());
				descNodes = doc.getElementsByTagName("ns2:orders");

				conn.disconnect();

				List<ParamMap> responseList = new ArrayList<ParamMap>();
				List<Pa11stclaimlistVO> arrPa11stclaimlist = new ArrayList<Pa11stclaimlistVO>();

				for (int j = 0; j < descNodes.getLength(); j++) {
					for (Node node = descNodes.item(j).getFirstChild(); node != null; node = node
							.getNextSibling()) {
						if (node.getNodeName().trim().equals("ns2:order")) {
							ParamMap pa11stParamMap = new ParamMap();
							for (int i = 0; i < node.getChildNodes()
									.getLength(); i++) {
								Node directionList = node.getChildNodes().item(
										i);
								pa11stParamMap.put(directionList.getNodeName()
										.trim(), directionList.getTextContent()
										.trim());
							}
							responseList.add(pa11stParamMap);
						} else {
							paramMap.put(node.getNodeName().trim(), node
									.getTextContent().trim());
						}
					}
				}

				if (responseList.size() > 0) {
					Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
					for (int i = 0; i < responseList.size(); i++) {
						pa11stclaimlist = new Pa11stclaimlistVO();

						pa11stclaimlist.setPaCode(paCode);
						pa11stclaimlist.setOrdNo(responseList.get(i).getString(
								"ordNo"));
						pa11stclaimlist.setOrdPrdSeq(responseList.get(i)
								.getString("ordPrdSeq"));
						pa11stclaimlist.setClmReqSeq(responseList.get(i)
								.getString("clmReqSeq"));
						pa11stclaimlist.setPaOrderGb("31");
						pa11stclaimlist.setAffliateBndlDlvSeq(responseList.get(
								i).getString("affliateBndlDlvSeq"));
						pa11stclaimlist.setClmReqCont(responseList.get(i)
								.getString("clmReqCont"));
						pa11stclaimlist.setClmReqQty(responseList.get(i)
								.getInt("clmReqQty"));
						pa11stclaimlist.setClmReqRsn(responseList.get(i)
								.getString("clmReqRsn"));
						pa11stclaimlist.setClmStat(responseList.get(i)
								.getString("clmStat"));
						pa11stclaimlist.setOptName(responseList.get(i)
								.getString("optName"));
						pa11stclaimlist.setPrdNo(responseList.get(i).getLong(
								"prdNo"));
						pa11stclaimlist.setReqDt(DateUtil.toTimestamp(
								responseList.get(i).getString("reqDt"),
								"yyyy-MM-dd HH:mm:ss"));
						pa11stclaimlist.setInsertDate(sysdateTime);
						pa11stclaimlist.setModifyDate(sysdateTime);

						arrPa11stclaimlist.add(pa11stclaimlist);
					}

					pa11stClaimService
							.saveReturnCancelListTx(arrPa11stclaimlist);

					paramMap.put("code", "200");
					paramMap.put("message", "OK");

				} else if (paramMap.getString("ns2:result_code").equals("-1")) {
					paramMap.put("code", "200");
					paramMap.put("message",
							paramMap.getString("ns2:result_text"));
					log.info(paramMap.getString("ns2:result_text"));
				} else {
					// API 연결 실패
					paramMap.put("code", "500");
					paramMap.put(
							"message",
							getMessage("errors.exist",
									new String[] { "returnCancelList" }));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(
							paramMap.getString("code"),
							paramMap.getString("message")), HttpStatus.OK);
				}
			}

		} catch (Exception e) {
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			if (e.getMessage() != null) {
				paramMap.put("message", e.getMessage().length() > 3950 ? e
						.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			} else {
				log.error(paramMap.getString("message"), e);
			}
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(
					HttpStatus.OK.value(), paramMap.getString("code"),
					paramMap.getString("message")), HttpStatus.OK);
		} finally {
			if (conn != null)
				conn.disconnect();
			try {
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}

			log.info("04.반품철회목록 조회 API END");
			claimCancelMain(request); // = 반품취소 데이터 생성

		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(
				HttpStatus.OK.value(), paramMap.getString("code"),
				paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 반품완료보류처리 IF_PA11STAPI_03_013
	 * 
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품완료보류처리", notes = "반품완료보류처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-hold-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnHoldProc(HttpServletRequest request)
			throws Exception {
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String dateTime = "";
		String msg = "";
		int targetCount = 0;
		int successCount = 0;
		int failCount = 0;
		String duplicateCheck = "";
		StringBuffer sb = new StringBuffer();
		HashMap<String, Object> returnMap = null;
		ParamMap resultMap = null;

		log.info("===== 반품완료보류 처리 Start=====");
		// = connectionSetting 설정
		dateTime = systemService.getSysdatetimeToString();
		String prg_id = "IF_PA11STAPI_03_013";

		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);

		try {

			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated",
						new String[] { prg_id });

			apiInfo = systemService.selectPaApiInfo(paramMap);

			log.info("01.보류대상 조회");
			List<Object> returnList = pa11stClaimService.selectReturnHoldList();

			if (returnList != null) {
				if (returnList.size() > 0) {
					targetCount = returnList.size();
					log.info("02.반품완료보류 처리 API 호출");
					for (int i = 0; i < returnList.size(); i++) {
						returnMap = (HashMap<String, Object>) returnList.get(i);
						returnMap.put("apiInfo", apiInfo);
						try {
							// = 반품완료보류 API 호출
							resultMap = pa11stClaimService
									.saveReturnHoldProcTx(returnMap);
							if (resultMap.getString("rtnMsg").equals("000000")) {
								successCount = successCount
										+ resultMap.getInt("successCnt");
							} else {
								failCount = failCount
										+ resultMap.getInt("failCnt");
								log.info(returnMap.get("MAPPING_SEQ")
										.toString()
										+ ": 반품완료보류처리 fail - "
										+ "|");
								sb.append(returnMap.get("MAPPING_SEQ")
										.toString()
										+ ": 반품완료보류처리 fail - "
										+ "|");
							}
						} catch (Exception e) {
							log.info(returnMap.get("MAPPING_SEQ").toString()
									+ ": 반품완료보류처리 fail - " + e.getMessage()
									+ "|");
							sb.append(returnMap.get("MAPPING_SEQ").toString()
									+ ": 반품완료보류처리 fail - " + e.getMessage()
									+ "|");
							continue;
						}
					}
				}
			}
		} catch (Exception e) {
			msg = "대상건수:" + targetCount + ", 성공건수:" + successCount + ", 실패건수:"
					+ failCount + "|";

			paramMap.put("code", "500");
			paramMap.put("message", msg + e.getMessage());
			log.error(msg + " : " + paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(
					HttpStatus.OK.value(), paramMap.getString("code"),
					paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				if (!paramMap.getString("code").equals("500")) {
					msg = "대상건수:" + targetCount + ", 성공건수:" + successCount
							+ ", 실패건수:" + failCount + "|";

					// 대상건수 모두 성공하였을 경우
					if (targetCount == successCount) {
						paramMap.put("code", "200");
					} else {
						paramMap.put("code", "500");
					}
					paramMap.put("message", msg + sb.toString());
				}
				systemService.insertApiTrackingTx(request, paramMap);

				if (duplicateCheck.equals("0")) {
					systemService.checkCloseHistoryTx("end", prg_id);
				}
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			log.info("03.반품완료보류 처리 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"),
				HttpStatus.OK);
	}

	/**
	 * 수취확인후 직권취소목록조회 IF_PA11STAPI_03_018
	 * 
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "수취확인후 직권취소목록조회", notes = "수취확인후 직권취소목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/auth-cancel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> authCancelList(
			HttpServletRequest request,
			@ApiParam(name = "fromDate", value = "검색시작일[yyyyMMddhhmm]", defaultValue = "") @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
			@ApiParam(name = "toDate", value = "검색종료일[yyyyMMddhhmm]", defaultValue = "") @RequestParam(value = "toDate", required = false, defaultValue = "") String toDate)
			throws Exception {

		/**
		 * 11번가 날짜 전달 파라미터 날짜포맷 : 년(4) 월(2) 일(2) 시(2) 분(2) 기간입력 Default D-2 ~ D
		 * */

		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String dateTime = "";
		HttpURLConnection conn = null;

		Document doc = null;
		String duplicateCheck = "";
		NodeList descNodes = null;
		String startDate = "";
		String endDate = "";
		String procPaCode = "";
		String paCode = "";
		Pa11stclaimlistVO pa11stclaimlist = null;

		log.info("===== 수취확인후 직권취소목록조회 API Start=====");
		// log.info("01.API 기본정보 세팅");

		// = connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_018";
		String request_type = "GET";
		dateTime = systemService.getSysdatetimeToString();

		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);

		try {
			// log.info("02.API 중복실행검사");
			// = 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated",
						new String[] { prg_id });

			apiInfo = systemService.selectPaApiInfo(paramMap);

			// 기간입력
			if (!(fromDate.isEmpty() || fromDate.equals(""))
					&& !(toDate.isEmpty() || toDate.equals(""))) {
				startDate = fromDate;
				endDate = toDate;
			}
			// 예외(Default D-2 ~ D)
			else {
				endDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(),
						+1, DateUtil.GENERAL_DATE_FORMAT) + "0000";
				startDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(),
						-2, DateUtil.GENERAL_DATE_FORMAT)
						+ DateUtil.getCurrentHourAsString()
						+ DateUtil.getCurrentMinuteAsString();
			}

			String parameter = "/" + startDate + "/" + endDate;

			for (int count = 0; count < Constants.PA_11ST_CONTRACT_CNT; count++) {

				if (count == 0) {
					procPaCode = Constants.PA_11ST_ONLINE;
					paCode = Constants.PA_11ST_ONLINE_CODE;
				} else {
					procPaCode = Constants.PA_11ST_BROAD;
					paCode = Constants.PA_11ST_BROAD_CODE;
				}

				log.info("03.수취확인후 직권취소목록조회 API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo, procPaCode,
						request_type, parameter);

				// RESPONSE XML
				doc = ComUtil.pa11stParseXML(conn.getInputStream());
				descNodes = doc.getElementsByTagName("ns2:orders");

				conn.disconnect();

				List<ParamMap> responseList = new ArrayList<ParamMap>();
				List<Pa11stclaimlistVO> arrPa11stclaimlist = new ArrayList<Pa11stclaimlistVO>();

				for (int j = 0; j < descNodes.getLength(); j++) {
					for (Node node = descNodes.item(j).getFirstChild(); node != null; node = node
							.getNextSibling()) {
						if (node.getNodeName().trim().equals("ns2:order")) {
							ParamMap pa11stParamMap = new ParamMap();
							for (int i = 0; i < node.getChildNodes()
									.getLength(); i++) {
								Node directionList = node.getChildNodes().item(
										i);
								pa11stParamMap.put(directionList.getNodeName()
										.trim(), directionList.getTextContent()
										.trim());
							}
							responseList.add(pa11stParamMap);
						} else {
							paramMap.put(node.getNodeName().trim(), node
									.getTextContent().trim());
						}
					}
				}

				if (responseList.size() > 0) {
					Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
					for (int i = 0; i < responseList.size(); i++) {
						pa11stclaimlist = new Pa11stclaimlistVO();
						/*
						 * pa11stclaimlist.setPaCode(paCode);
						 * pa11stclaimlist.setOrdNo
						 * (responseList.get(i).getString("ordNo"));
						 * pa11stclaimlist
						 * .setOrdPrdSeq(responseList.get(i).getString
						 * ("ordPrdSeq"));
						 * pa11stclaimlist.setClmReqSeq(responseList
						 * .get(i).getString("clmReqSeq"));
						 * pa11stclaimlist.setPaOrderGb("30");
						 * pa11stclaimlist.setAffliateBndlDlvSeq
						 * (responseList.get
						 * (i).getString("affliateBndlDlvSeq"));
						 * pa11stclaimlist.
						 * setClmReqCont(responseList.get(i).getString
						 * ("clmReqCont"));
						 * pa11stclaimlist.setClmReqQty(responseList
						 * .get(i).getInt("clmReqQty"));
						 * pa11stclaimlist.setClmReqRsn
						 * (responseList.get(i).getString("clmReqRsn"));
						 * pa11stclaimlist
						 * .setClmStat(responseList.get(i).getString
						 * ("clmStat"));
						 * pa11stclaimlist.setOptName(responseList.
						 * get(i).getString("optName"));
						 * pa11stclaimlist.setPrdNo
						 * (responseList.get(i).getLong("prdNo"));
						 * pa11stclaimlist
						 * .setReqDt(DateUtil.toTimestamp(responseList
						 * .get(i).getString("reqDt"), "yyyy-MM-dd HH:mm:ss"));
						 * pa11stclaimlist.setInsertDate(sysdateTime);
						 * pa11stclaimlist.setModifyDate(sysdateTime);
						 */

						pa11stclaimlist.setPaCode(paCode);
						pa11stclaimlist.setOrdNo(responseList.get(i).getString(
								"ordNo"));
						pa11stclaimlist.setOrdPrdSeq(responseList.get(i)
								.getString("ordPrdSeq"));
						pa11stclaimlist.setClmReqSeq(responseList.get(i)
								.getString("clmReqSeq"));
						pa11stclaimlist.setPaOrderGb("30");
						pa11stclaimlist.setAffliateBndlDlvSeq(responseList.get(
								i).getString("affliateBndlDlvSeq"));
						pa11stclaimlist.setClmReqCont(responseList.get(i)
								.getString("clmReqCont"));
						pa11stclaimlist.setClmReqQty(responseList.get(i)
								.getInt("clmReqQty"));
						pa11stclaimlist.setClmReqRsn(responseList.get(i)
								.getString("clmReqRsn"));
						pa11stclaimlist.setClmStat(responseList.get(i)
								.getString("clmStat"));
						pa11stclaimlist.setOptName(responseList.get(i)
								.getString("optName"));
						pa11stclaimlist.setPrdNo(responseList.get(i).getLong(
								"prdNo"));
						pa11stclaimlist.setReqDt(DateUtil.toTimestamp(
								responseList.get(i).getString("reqDt"),
								"yyyy-MM-dd HH:mm:ss"));
						pa11stclaimlist.setOrdNm(responseList.get(i).getString(
								"ordNm"));
						pa11stclaimlist.setOrdTlphnNo(responseList.get(i)
								.getString("ordTlphnNo"));
						pa11stclaimlist.setOrdPrtblTel(responseList.get(i)
								.getString("ordPrtblTel"));
						pa11stclaimlist.setRcvrMailNo(responseList.get(i)
								.getString("rcvrMailNo"));
						pa11stclaimlist.setRcvrMailNoSeq(responseList.get(i)
								.getString("rcvrMailNoSeq"));
						pa11stclaimlist.setRcvrBaseAddr(responseList.get(i)
								.getString("rcvrBaseAddr"));
						pa11stclaimlist.setRcvrDtlsAddr(responseList.get(i)
								.getString("rcvrDtlsAddr"));
						pa11stclaimlist.setRcvrTypeAdd(responseList.get(i)
								.getString("rcvrTypeAdd"));
						pa11stclaimlist.setRcvrTypeBilNo(responseList.get(i)
								.getString("rcvrTypeBilNo"));
						pa11stclaimlist.setTwMthd(responseList.get(i)
								.getString("twMthd"));
						pa11stclaimlist.setExchNm(responseList.get(i)
								.getString("exchNm"));
						pa11stclaimlist.setExchTlphnNo(responseList.get(i)
								.getString("exchTlphnNo"));
						pa11stclaimlist.setExchPrtbTel(responseList.get(i)
								.getString("exchPrtbTel"));
						pa11stclaimlist.setExchMailNo(responseList.get(i)
								.getString("exchMailNo"));
						pa11stclaimlist.setExchMailNoSeq(responseList.get(i)
								.getString("exchMailNoSeq"));
						pa11stclaimlist.setExchBaseAddr(responseList.get(i)
								.getString("exchBaseAddr"));
						pa11stclaimlist.setExchDtlsAddr(responseList.get(i)
								.getString("exchDtlsAddr"));
						pa11stclaimlist.setExchTypeAdd(responseList.get(i)
								.getString("exchTypeAdd"));
						pa11stclaimlist.setExchTypeBilNo(responseList.get(i)
								.getString("exchTypeBilNo"));
						pa11stclaimlist.setClmLstDlvCst(responseList.get(i)
								.getInt("clmLstDlvCst"));
						pa11stclaimlist.setAppmtDlvCst(responseList.get(i)
								.getInt("appmtDlvCst"));
						pa11stclaimlist.setClmDlvCstMthd(responseList.get(i)
								.getString("clmDlvCstMthd"));
						pa11stclaimlist.setTwPrdInvcNo(responseList.get(i)
								.getString("twPrdInvcNo"));
						pa11stclaimlist.setDlvEtprsCd(responseList.get(i)
								.getString("dlvEtprsCd"));
						pa11stclaimlist.setInsertDate(sysdateTime);
						pa11stclaimlist.setModifyDate(sysdateTime);

						arrPa11stclaimlist.add(pa11stclaimlist);
					}

					pa11stClaimService.saveAuthCancelListTx(arrPa11stclaimlist);

					paramMap.put("code", "200");
					paramMap.put("message", "OK");

				} else if (paramMap.getString("ns2:result_code").equals("-1")) {
					paramMap.put("code", "200");
					paramMap.put("message",
							paramMap.getString("ns2:result_text"));
					log.info(paramMap.getString("ns2:result_text"));
				} else {
					// API 연결 실패
					paramMap.put("code", "500");
					paramMap.put(
							"message",
							getMessage("errors.exist",
									new String[] { "authCancelList" }));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(
							paramMap.getString("code"),
							paramMap.getString("message")), HttpStatus.OK);
				}
			}

		} catch (Exception e) {
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			if (e.getMessage() != null) {
				paramMap.put("message", e.getMessage().length() > 3950 ? e
						.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			} else {
				log.error(paramMap.getString("message"), e);
			}
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(
					HttpStatus.OK.value(), paramMap.getString("code"),
					paramMap.getString("message")), HttpStatus.OK);
		} finally {
			if (conn != null)
				conn.disconnect();
			try {
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}

			log.info("04.수취확인후 직권취소목록조회 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(
				HttpStatus.OK.value(), paramMap.getString("code"),
				paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 11번가 반품접수 데이터 생성
	 * 
	 * @return Map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품접수 데이터 생성", notes = "반품접수 데이터 생성", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/order-claim", method = RequestMethod.GET)
	@ResponseBody
	@Async
	public void orderClaimMain(HttpServletRequest request) throws Exception {
		String duplicateCheck = "";
		String prg_id = "PA11ST_ORDER_CLAIM";
		String isLocalYn = "N";

		log.info("=========================== 11st Order Claim Start =========================");
		try {
			// = 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated",
						new String[] { prg_id });
			List<Object> orderClaimTargetList = pa11stClaimService
					.selectOrderClaimTargetList();

			HashMap<String, Object> hmSheet = null;
			int procCnt = orderClaimTargetList.size();

			for (int i = 0; procCnt > i; i++) {
				try {
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) orderClaimTargetList
							.get(i);

					if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost())
							|| ("127.0.0.1").equals(request.getRemoteHost())) {
						isLocalYn = "Y";
					} else {
						isLocalYn = "N";
					}

					asycController.orderClaimAsync(hmSheet, request, isLocalYn);
				} catch (Exception e) {
					continue;
				}
			}

		} catch (Exception e) {
			log.info("error msg : " + e.getMessage());
		} finally {
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("=========================== 11st Order Claim End =========================");
		}
		return;
	}

    @ApiIgnore
	@RequestMapping(value = "/order-claim-4Test", method = RequestMethod.GET)
	@ResponseBody
	@Async
	public void orderClaimMain4Test(HttpServletRequest request)
			throws Exception {
		asycController.orderClaimAsync4Test(request);
		return;
	}

	/**
	 * 11번가 반품취소 데이터 생성
	 * 
	 * @return Map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품취소 데이터 생성", notes = "반품취소 데이터 생성", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/claim-cancel", method = RequestMethod.GET)
	@ResponseBody
	public void claimCancelMain(HttpServletRequest request) throws Exception {
		String duplicateCheck = "";
		String prg_id = "PA11ST_CLAIM_CANCEL";

		log.info("=========================== 11st Claim Cancel Create Start =========================");
		try {
			// = 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated",
						new String[] { prg_id });
			List<Object> claimCancelTargetList = pa11stClaimService
					.selectClaimCancelTargetList();

			HashMap<String, Object> hmSheet = null;
			int procCnt = claimCancelTargetList.size();

			for (int i = 0; procCnt > i; i++) {
				try {
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) claimCancelTargetList
							.get(i);
					asycController.claimCancelAsync(hmSheet, request);
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			log.info("error msg : " + e.getMessage());
		} finally {
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("=========================== 11st Claim Cancel Create End =========================");
		}
		return;
	}
	
	/**
	 * 11번가 반품완료 데이터  생성 IF_PA11STAPI_03_025
	 * 
	 * @return Map
	 * @throws Exception
	 */
	@ApiOperation(value = "반품완료 데이터  생성", notes = "반품완료 데이터  생성", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-complete-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnCompleteList(
			HttpServletRequest request,
			@ApiParam(name = "fromDate", value = "검색시작일[yyyyMMddhhmm]", defaultValue = "") @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
			@ApiParam(name = "toDate", value = "검색종료일[yyyyMMddhhmm]", defaultValue = "") @RequestParam(value = "toDate", required = false, defaultValue = "") String toDate)
			throws Exception {
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String dateTime = "";
		HttpURLConnection conn = null;
		
		Document doc = null;
		String duplicateCheck = "";
		NodeList descNodes = null;
		String startDate = "";
		String endDate = "";
		String procPaCode = "";
		String paCode = "";
		Pa11stclaimlistVO pa11stclaimlist = null;
		
		log.info("===== 반품완료목록조회 API Start=====");
		
		// = connectionSetting 설정
		String prg_id = "IF_PA11STAPI_03_025";
		String request_type = "GET";
		dateTime = systemService.getSysdatetimeToString();
	
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		try {
			// = 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated", new String[] { prg_id });
			
			apiInfo = systemService.selectPaApiInfo(paramMap);

			// 기간입력
			if (!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))) {
				startDate = fromDate;
				endDate = toDate;
			}
			// 예외(Default D-2 ~ D)
			else {
				endDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), +1, DateUtil.GENERAL_DATE_FORMAT) + "0000";
				startDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), -2, DateUtil.GENERAL_DATE_FORMAT) + DateUtil.getCurrentHourAsString() + DateUtil.getCurrentMinuteAsString();  
			}
			
			String parameter = "/" + startDate + "/" + endDate;

			for (int count = 0; count < Constants.PA_11ST_CONTRACT_CNT; count++) {
				if (count == 0) {
					procPaCode = Constants.PA_11ST_ONLINE;
					paCode = Constants.PA_11ST_ONLINE_CODE;
				} else {
					procPaCode = Constants.PA_11ST_BROAD;
					paCode = Constants.PA_11ST_BROAD_CODE;
				}

				log.info("반품완료목록 조회 API 호출");
				conn = ComUtil.pa11stConnectionSetting(apiInfo, procPaCode,request_type, parameter);
				
				// RESPONSE XML
				doc = ComUtil.pa11stParseXML(conn.getInputStream());
				descNodes = doc.getElementsByTagName("ns2:orders");
				conn.disconnect();
				
				List<ParamMap> responseList = new ArrayList<ParamMap>();
				List<Pa11stclaimlistVO> arrPa11stclaimlist = new ArrayList<Pa11stclaimlistVO>();
				
				for (int j = 0; j < descNodes.getLength(); j++) {
					for (Node node = descNodes.item(j).getFirstChild(); node != null; node = node.getNextSibling()) { // 첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
						if (node.getNodeName().trim().equals("ns2:order")) {
							ParamMap pa11stParamMap = new ParamMap();
							for (int i = 0; i < node.getChildNodes().getLength(); i++) {
								Node directionList = node.getChildNodes().item(i);
								pa11stParamMap.put(directionList.getNodeName().trim(), directionList.getTextContent().trim());
							}
							responseList.add(pa11stParamMap);
						} else {
							paramMap.put(node.getNodeName().trim(), node.getTextContent().trim());
						}
					}
				}
				
				if (responseList.size() > 0) {
					Timestamp sysdateTime = DateUtil.toTimestamp(dateTime);
					for (int i = 0; i < responseList.size(); i++) {
						pa11stclaimlist = new Pa11stclaimlistVO();

						pa11stclaimlist.setPaCode(paCode);
						pa11stclaimlist.setOrdNo(responseList.get(i).getString("ordNo"));
						pa11stclaimlist.setOrdPrdSeq(responseList.get(i).getString("ordPrdSeq"));
						pa11stclaimlist.setClmReqSeq(responseList.get(i).getString("clmReqSeq"));
						pa11stclaimlist.setPaOrderGb("30");
						pa11stclaimlist.setAffliateBndlDlvSeq(responseList.get(i).getString("affliateBndlDlvSeq")); //추가배송비 여부
						pa11stclaimlist.setClmReqCont(responseList.get(i).getString("clmReqCont"));
						pa11stclaimlist.setClmReqQty(responseList.get(i).getInt("clmReqQty"));
						pa11stclaimlist.setClmReqRsn(responseList.get(i).getString("clmReqRsn"));
						pa11stclaimlist.setClmStat(responseList.get(i).getString("clmStat"));
						pa11stclaimlist.setOptName(responseList.get(i).getString("optName"));
						pa11stclaimlist.setPrdNo(responseList.get(i).getLong("prdNo"));
						pa11stclaimlist.setReqDt(DateUtil.toTimestamp(responseList.get(i).getString("trtEndDt"),"yyyy-MM-dd HH:mm:ss"));
						pa11stclaimlist.setClmLstDlvCst(responseList.get(i).getInt("addDlvCst")); //추가배송비 -> 반품배송비에 저장
						pa11stclaimlist.setDlvCstRespnClf(responseList.get(i).getString("dlvCstRespnClf")); //배송비 부담여부
						pa11stclaimlist.setInsertDate(sysdateTime);
						pa11stclaimlist.setModifyDate(sysdateTime);
						arrPa11stclaimlist.add(pa11stclaimlist);
					}
					
					pa11stClaimService.saveReturnCompleteListTx(arrPa11stclaimlist);
					
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
				} else if (paramMap.getString("ns2:result_code").equals("-1")) {
					paramMap.put("code", "200");
					paramMap.put("message",
							paramMap.getString("ns2:result_text"));
					log.info(paramMap.getString("ns2:result_text"));
				} else {
					// API 연결 실패
					paramMap.put("code", "500");
					paramMap.put("message", getMessage("errors.exist", new String[] { "returnList" }));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			} // end for
				
		} catch (Exception e) {
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			if (e.getMessage() != null) {
				paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
				log.error(paramMap.getString("message"), e);
			} else {
				log.error(paramMap.getString("message"), e);
			}
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"),
					paramMap.getString("message")), HttpStatus.OK);
		} finally {
			if (conn != null)
				conn.disconnect();
			try {
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}

			log.info("반품완료목록 조회 API END");
			orderClaimMain(request); // = 반품 데이터 생성.
		}

		return new ResponseEntity<ResponseMsg>(new ResponseMsg(
				HttpStatus.OK.value(), paramMap.getString("code"),
				paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 반품송장입력 IF_PA11STAPI_03_026
	 * 
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품송장입력", notes = "반품송장입력", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-slip-out-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnSlipOutProc(HttpServletRequest request)
			throws Exception {
		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		String msg = "";
		int targetCount = 0;
		int successCount = 0;
		int failCount = 0;
		String duplicateCheck = "";
		StringBuffer sb = new StringBuffer();
		HashMap<String, Object> returnSlipMap = null;
		ParamMap resultMap = null;
		HashMap<String, String> apiInfo = new HashMap<String, String>();

		log.info("===== 반품송장입력 Start=====");
		// = connectionSetting 설정
		dateTime = systemService.getSysdatetimeToString();
		String prg_id = "IF_PA11STAPI_03_026";

		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", dateTime);

		try {

			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated",
						new String[] { prg_id });

			log.info("01.회수송장 등록 건 조회");
			//쿼리 수정 필요
			List<Object> returnSlipProcList = pa11stClaimService.selectReturnSlipProcList();
			
			ParamMap apiMap = new ParamMap();
			apiMap.put("apiCode", prg_id);
			apiMap.put("broadCode", Constants.PA_11ST_BROAD_CODE);
			apiMap.put("onlineCode", Constants.PA_11ST_ONLINE_CODE);
			apiInfo = systemService.selectPaApiInfo(apiMap);
			
			log.info("02.반품송장등록처리 API 호출");
			
			if (returnSlipProcList != null) {
				if (returnSlipProcList.size() > 0) {					
					targetCount = returnSlipProcList.size();
					for (int i = 0; i < returnSlipProcList.size(); i++) {
						returnSlipMap = (HashMap<String, Object>) returnSlipProcList.get(i);
						returnSlipMap.put("apiInfo", apiInfo);
						
						try {
							// = 반품송장등록처리 API 호출
							resultMap = pa11stClaimService.saveReturnSlipProcTx(returnSlipMap);
							
							if (resultMap.getString("result_code").equals("0")) {
								successCount++;
							} else {
								failCount++;
								log.info(returnSlipMap.get("MAPPING_SEQ").toString() + ": 반품송장등록처리 fail - " + "|");
								sb.append(returnSlipMap.get("MAPPING_SEQ").toString() + ": 반품송장등록처리 fail - " + "|");
							}
						} catch (Exception e) {
							log.info(returnSlipMap.get("MAPPING_SEQ").toString() + ": 반품송장등록처리 fail - " + e.getMessage() + "|");
							sb.append(returnSlipMap.get("MAPPING_SEQ").toString() + ": 반품송장등록처리 fail - " + e.getMessage() + "|");
							continue;
						}
					}
				}
			}
		} catch (Exception e) {
			msg = "대상건수:" + targetCount + ", 성공건수:" + successCount + ", 실패건수:" + failCount + "|";

			paramMap.put("code", "500");
			paramMap.put("message", msg + e.getMessage());
			log.error(msg + " : " + paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				if (!paramMap.getString("code").equals("500")) {
					msg = "대상건수:" + targetCount + ", 성공건수:" + successCount + ", 실패건수:" + failCount + "|";

					// 대상건수 모두 성공하였을 경우
					if (targetCount == successCount) {
						paramMap.put("code", "200");
					} else {
						paramMap.put("code", "500");
					}
					paramMap.put("message", msg + sb.toString());
				}
				systemService.insertApiTrackingTx(request, paramMap);

				if (duplicateCheck.equals("0")) {
					systemService.checkCloseHistoryTx("end", prg_id);
				}
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			log.info("03.반품송장등록처리 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
}
