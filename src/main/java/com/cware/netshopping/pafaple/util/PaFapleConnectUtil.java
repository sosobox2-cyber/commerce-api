package com.cware.netshopping.pafaple.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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


@Repository("com.cware.netshopping.pafaple.util.paFapleConnectUtil")
public class PaFapleConnectUtil extends AbstractService {
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	private PaFapleApiRequest apiRequest;
	
	/* API Call */
	public Map<String, Object> callPaFapleAPILegacy(ParamMap apiBaseInfo, Object apiRequestObject) throws Exception {
		String body = "";
		String connectResult = "";
		Map<String, Object> returnMap = null;
		
		// 1) 패션플러스 API KEY 생성
		apiRequest.createApiKey(apiBaseInfo);

		// 2) URL Method 등 세팅
		apiRequest.getApiInfo(apiBaseInfo);
		
		// 3) Body 생성 (Object(HashMap 또는 ArrayList) 데이터를 String로 전환)
		body = apiRequest.getBody(apiRequestObject);
		apiBaseInfo.put("body", body);
		
		// 4) 통신 호출 및 결과 수신
		connectResult = apiRequest.connectPaFapleAPILegacy(apiBaseInfo);

		// 5) JSON -> MAP 변환
		returnMap = ComUtil.splitJson(connectResult);

		return returnMap;
	}
	
	/* API Call */
	public Map<String, Object> callPaFapleAPI(ParamMap apiBaseInfo, ParamMap apiRequestObject, PaTransService serviceLog) throws Exception {
		String connectResult = "";
		Map<String, Object> returnMap = null;
		
		// 1) 패션플러스 API KEY 생성
		apiRequest.createApiKey(apiBaseInfo);

		// 2) URL Method 등 세팅
		apiRequest.getApiInfo(apiBaseInfo);
				
		// 3) 통신 호출 및 결과 수신
		connectResult = apiRequest.connectPaFapleAPI(apiBaseInfo, serviceLog, apiRequestObject);

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
			} else {
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

		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);

		return paramMap;
	}
}
