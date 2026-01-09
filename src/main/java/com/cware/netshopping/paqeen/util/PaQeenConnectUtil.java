package com.cware.netshopping.paqeen.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;


@Repository("com.cware.netshopping.paQeen.util.paQeenConnectUtil")
public class PaQeenConnectUtil extends AbstractService {
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	private PaQeenApiRequest apiRequest;
	
	/* API Call */
	public Map<String, Object> callPaQeenAPILegacy(ParamMap apiBaseInfo, Object apiRequestObject, HashMap<String, String> getParameters) throws Exception {
		String body = "";
		String connectResult = "";
		String getParameter = "";
		Map<String, Object> returnMap = null;
		
		// 1) URL Method 등 세팅
		apiRequest.getApiInfo(apiBaseInfo);
		
		// 2) Body 생성 (Object(HashMap 또는 ArrayList) 데이터를 String로 전환)
		if(apiRequestObject != null) {
			body = apiRequest.getBody(apiRequestObject);
			apiBaseInfo.put("body", body);
		}
		// 3) Url 세팅(GET 파라미터 세팅)
		if(getParameters != null) {
			getParameter = apiRequest.convertMapToUrl(getParameters);
			apiBaseInfo.put("getParameter", "?" + getParameter);
		}
		// 3) 통신 호출 및 결과 수신
		connectResult = apiRequest.connectPaQeenAPILegacy(apiBaseInfo);

		// 4) JSON -> MAP 변환
		returnMap = ComUtil.splitJson(connectResult);

		return returnMap;
	}
	
	/* API Call */
	public Map<String, Object> callPaQeenAPI(ParamMap apiBaseInfo, ParamMap apiRequestObject, PaTransService serviceLog,HashMap<String, String> getParameters) throws Exception {
		// apiBaseInfo 에 API_CODE 넣기
		// apiRequestObject 에 Body 넣기
		// get 통신시 pathParameters에 파라미터 값 넣기 (?sample=test 일경우 getParameters.put("sample","test"))
		// urlParameter 넣어야 할 시 pathParameters에 넣기 getParameters.put("urlParameter","test")
		String connectResult = "";
		Map<String, Object> returnMap = null;
		String getParameter = "";
		
		// 1) URL Method 등 세팅
		apiRequest.getApiInfo(apiBaseInfo);
		
		if(getParameters != null) {
			// 2) Url 세팅(GET 파라미터 세팅)
			getParameter = apiRequest.convertMapToUrl(getParameters);
			apiRequestObject.put("getParameter", "?" + getParameter);
		}
		// 3) 통신 호출 및 결과 수신
		connectResult = apiRequest.connectPaQeenAPI(apiBaseInfo, serviceLog, apiRequestObject);

		// 4) JSON -> MAP 변환
		returnMap = ComUtil.splitJson(connectResult);

		return returnMap;
	}
	
	/* API Close */
	public void closeApi(HttpServletRequest request, ParamMap apiBaseInfo) {
		try {
			systemService.insertApiTrackingTx(request, apiBaseInfo);
			
			if ("0".equals(apiBaseInfo.getString("duplicateCheck"))) {
				systemService.checkCloseHistoryTx("end", apiBaseInfo.getString("apiCode"));
			}
			
		} catch (Exception e) {
			log.error("ApiTracking/CloseHistory process Error : " + e.getMessage());
		}
	}
	
	/* API Check Exception */
	public void checkException(ParamMap apiBaseInfo , Exception e) {
		String resultCode = apiBaseInfo.getString("duplicateCheck").equals("1") ? "490" : "500";
		String resultMsg  = apiBaseInfo.getString("duplicateCheck").equals("1") ? getMessage("msg.batch_process_duplicated") : e.getMessage();

		log.error("Exception occurs : " + resultMsg);
		
		apiBaseInfo.put("code", resultCode);
		apiBaseInfo.put("message", resultMsg);
	}
	
	/* 조회날짜데이터 포맷 셋팅 
	 * 패션플러스 정책 : 조회기간은 최대 7일
	 * */
	public ParamMap setRetrieveDateFormat(String startDate, String endDate, String flag) throws Exception {
		ParamMap paramMap = new ParamMap();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp sysdatetime = systemService.getSysdatetime();

		if (StringUtils.isBlank(startDate) || StringUtils.isBlank(startDate)) {
			if ("1".equals(flag)) { // 주문 조회
				startDate = dateFormat.format(DateUtil.addDay(sysdatetime, -3));
				endDate = dateFormat.format(sysdatetime);
			} else if ("2".equals(flag)) { // 배송완료 조회(1주전부터 현재까지)
				startDate = dateFormat.format(DateUtil.addDay(sysdatetime, -7));
				endDate = dateFormat.format(sysdatetime);
			} else if("4".equals(flag)){ // 클레임 철회 조회(8주 전부터 현재까지) 
				startDate = dateFormat.format(DateUtil.addDay(sysdatetime, -56));
				endDate = dateFormat.format(sysdatetime);
			} else if("5".equals(flag)){ // 정산조회 조회(20일 전부터 현재까지) 
				startDate = dateFormat.format(DateUtil.addDay(sysdatetime, -20));
				endDate = dateFormat.format(sysdatetime);
			}else {
				dateFormat.applyPattern("yyyy-MM-dd");
				startDate = dateFormat.format(DateUtil.addDay(sysdatetime, -3));
				endDate = dateFormat.format(sysdatetime);
			}
		} else if ("3".equals(flag)) { // 배송완료 조회(2주전부터 1주전까지)
			if (startDate.length() == 10) {
				dateFormat.applyPattern("yyyy-MM-dd");
			}
			
			startDate = dateFormat.format(DateUtil.addDay(dateFormat.parse(startDate), -7));
			endDate = dateFormat.format(DateUtil.addHour(DateUtil.addDay(dateFormat.parse(endDate), -7), -1));
		}

		paramMap.put("startAt", startDate);
		paramMap.put("endAt", endDate);

		return paramMap;
	}
}
