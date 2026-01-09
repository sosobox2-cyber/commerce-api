package com.cware.netshopping.pagmkt.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.pagmkt.order.service.PaGmktOrderService;

@Repository("com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")

public class PaGmktCommonUtil {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pagmkt.order.PaGmktOrderService")
	private PaGmktOrderService paGmktOrderService;
	
	@Autowired 
	MessageSource ms;
	
	
	public void setParams(ParamMap paramMap) throws Exception{
		String apiCode 		= paramMap.getString("apiCode");
		String paGroupCode  = paramMap.getString("paGroupCode");
		
		switch(paGroupCode){
		case "02":
			apiCode = apiCode.replace("GMKT", "GMKT");
			paramMap.put("apiCode"		, apiCode);
			paramMap.put("siteGb"		, "PAG");
			paramMap.put("siteType"		, "2");  // 1: A,  2: G/G9,    3: G,	4. G9	-> 3에서 2로 변경 (듀얼리스팅)
			paramMap.put("siteType2"	, "2");  // 1: A,  2: G/G9,    3:G9   * 교환 CASE에서 사용한다.
			
			break;
		case "03":
			apiCode = apiCode.replace("GMKT", "IAC");
			paramMap.put("apiCode"		, apiCode);
			paramMap.put("siteGb"		, "PAA");
			paramMap.put("siteType"		, "1");  // 1: A,  2: G/G9, 3: G,	4. G9
			paramMap.put("siteType2"	, "1");  // 1: A,  2: G/G9,    3:G9 

			break;
		default:
			paramMap.put("code"    , "404");
			paramMap.put("message" , "404");
			throw new Exception("Bad PaGroupCode Approach");
		}
	}
			
	public void dealException(Exception se, ParamMap paramMap, String addMessage){
		String message =  null;
		dealException(se,paramMap);
		
		message = addMessage + paramMap.getString("message");
		paramMap.put("message", message);
	}
	
	
	public void dealException(Exception se, ParamMap paramMap){
		String message = null;
		String duplicatrionMessage = ms.getMessage("msg.batch_process_duplicated",new String[] { "" }, null);
		String connectionMessage = ms.getMessage("msg.jwt_connect_error", new String[] { "" }, null);
		String jsonSplitMessage    = ms.getMessage("msg.error_jsonsplit",new String[] { "" }, null);
		
		if(se.getMessage() ==null){
			message = se.toString();
		}else{
			message = se.getMessage();
		}
		
		//= 중복체크
		if(message.contains(duplicatrionMessage)){
			paramMap.put("code","490");
			paramMap.put("message",message.length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage());
		}
			
		//= Json 가공 에러
		if(message.contains(jsonSplitMessage)){
			paramMap.put("code","480");
			paramMap.put("message",message.length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage());
		}
		
		//= JWT 통신 에러
		if(message.contains(connectionMessage)){
			paramMap.put("code","400");
			paramMap.put("message",message.length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage());
		}

		//= 기타 프로세스 에러
		if(paramMap.getString("code").equals("")){
			paramMap.put("code","500");
			paramMap.put("message",message.length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage());
		}
	
		//= 추적이 불가능한 에러
		if(message == null ||message.equals("")){
			paramMap.put("code","304");
			paramMap.put("message","에러 내역이 존재하지 않는 기타 에러건");
		}
		
	}
	
	public void failCheck(String message, ParamMap paramMap){
		if(message.contains("@FAIL")){
			paramMap.put("code", "500");
		}else{
			paramMap.put("code", "200");
		}
		message = message.replace("null", "");
		message = message.replace("@FAIL", "");
		paramMap.put("message",message);
	}
	
	public String setResultMessage(String type, String paCode ,int successCount, int failCount){
		
		String message = "";
		
		if(paCode.equals("")){
			message = type + " 성공 : " +successCount +"건, 실패 : "+ failCount + "건  " +  "\n";;
		}else{
			message = type + "(" + paCode +") 성공 : " +successCount +"건, 실패 : "+ failCount + "건  " +  "\n";;
		}
		
		if(failCount > 0) {
			message += "@FAIL";
		}				
		return message;
	}
	
	public String setResultMessage(String type, int failCount){
		
		String message = "";
		
		message = type + " - " + failCount +" 건 실패  " +  "\n";;
		
		
		if(failCount > 0) {
			message += "@FAIL";
		}				
		return message;
	}

	/** paramMap 필수 값 : apiCode startDate siteGb    
			       선택 값 : message, code  **/
	public void dealSuccess(ParamMap paramMap, HttpServletRequest request) throws Exception {
		String rtnCode = paramMap.getString("code");
		
		if(!rtnCode.equals("200") &&  rtnCode.equals("")){
			paramMap.put("code","304");
			paramMap.put("message","에러 내역이 존재하지 않는 기타 에러건");
		}

		systemService.insertPaApiTrackingTx(request,paramMap);	
	}
	
	public void dealSuccess(ParamMap paramMap) throws Exception {
		String rtnCode = paramMap.getString("code");
		
		if(!rtnCode.equals("200") &&  rtnCode.equals("")){
			paramMap.put("code","304");
			paramMap.put("message","에러 내역이 존재하지 않는 기타 에러건");
		}

		systemService.insertPaApiTrackingTx(paramMap);	
	}
	
	public String getMaxPaOrderWSeq(String payNo, String contrNo) throws Exception{
		long canCelNo = Long.parseLong(systemService.getMaxNo("TPAGMKTCANCELLIST","CONTR_NO_SEQ", "PAY_NO = '" + payNo  + "' AND "
					+ "CONTR_NO = '" + contrNo + "' " , 3));
		
		long claimNo = 	Long.parseLong(systemService.getMaxNo("TPAGMKTCLAIMLIST" ,"CONTR_NO_SEQ", "PAY_NO = '" + payNo  + "' AND "
					+ "CONTR_NO = '" + contrNo + "' " , 3));
		 
		String resultNo = null;
		 
		if(canCelNo == 1) canCelNo ++;
		if(claimNo  == 1) claimNo ++;
	
		if(canCelNo > claimNo){
		  resultNo = (ComUtil.lpad(Long.toString(canCelNo), 3, "0"));
		}else{
		  resultNo = (ComUtil.lpad(Long.toString(claimNo), 3, "0"));
		}
		
		if(resultNo == null || resultNo.equals("")) return "002";  
		 
		return resultNo;
	}
	
	
	public String getLocalOrNot(HttpServletRequest request){
		String isLocalYN = "Y";
		
		if(request.getRemoteHost().equals("0:0:0:0:0:0:0:1")||request.getRemoteHost().equals("127.0.0.1")){
			isLocalYN = "Y";
		}else{
			isLocalYN = "N";
		}
		
		return isLocalYN;		
	}
	
	public String checkSourcingMediaForTestClaim(String payNo, String contrNo, String paCode) throws Exception{
		
		String realServerYn = systemService.getConfig("PA_REAL_SERVER_YN").getVal();
		String changedPacode = null;
		
		if("Y".equals(realServerYn)){  
			return paCode;
		}
		
		if("Y".equals(realServerYn)){  
			return paCode;
		}
		
		if("".equals(payNo) || "".equals(contrNo) || "".equals(paCode)){
			throw new Exception("소싱매체 검증 인자값이 없습니다. : checkSourcingMediaForTestClaim" );
		}
		
		String paGroupCode = paCode.substring(0,1);
		
		Map<String, String> map  = new HashMap<String, String>();
		map.put("payNo"			 , payNo);
		map.put("contrNo"		 , contrNo);
		map.put("paGroupCode"	 , paGroupCode);
		
		try{
			changedPacode = paGmktOrderService.getSourcingMediaForTest(map);
			
			if(changedPacode == null){
				throw new Exception("Pacode is null: checkSourcingMediaForTestClaim");
			}
			
		}catch(Exception e){
			throw new Exception("TPAORDERM에서 소싱매체 가져오는것을 실패하였습니다.: checkSourcingMediaForTestClaim");
		}
		
		return changedPacode;
	}
	
	public String checkSourcingMediaForTestOrder(String goodsCode, String paCode) throws Exception{
		
		String realServerYn = systemService.getConfig("PA_REAL_SERVER_YN").getVal();
		String changedPacode = null;
		
		//Real에서 구지 체크를 안하는 이유는 Real에서 상품 맵핑 PaCode와 주문 Pacode 미 일치 케이스는 Async에서 에러로 잡아내는게 맞다고 판단했기 때문.
		if("Y".equals(realServerYn)){  
			return paCode;
		}
		
		if(goodsCode.equals("") || paCode.equals("")) throw new Exception("소싱매체 검증 인자값이 없습니다. : checkSourcingMediaForTest");
		
		try{
			changedPacode = "";
			changedPacode = paGmktOrderService.getSourcingMediaForTest(goodsCode);
			String paGroupCode = paCode.substring(0,1);
			
			switch(paGroupCode){
			
			case "2" : 
					if("21".equals(changedPacode)){
						changedPacode = "21";
					}else if("22".equals(changedPacode)){
						changedPacode = "22";
					}else{
						throw new Exception("TGOODS에서 소싱매체 is null : checkSourcingMediaForTest");
					}
				break;
					
			case "3" :
					if("21".equals(changedPacode)){
						changedPacode = "21";
					}else if("22".equals(changedPacode)){
						changedPacode = "22";
					}else{
						throw new Exception("TGOODS에서 소싱매체 is null : checkSourcingMediaForTest");
					}
				break;	
			
			default : 
				throw new Exception("제휴사 아이디를 확인해 주세요. : checkSourcingMediaForTest");
			}
			
		}catch(Exception e){
			throw new Exception("TGOODS에서 소싱매체 가져오는것을 실패하였습니다.: checkSourcingMediaForTest");
		}
		return changedPacode;
	}
	
	public String refineUrlForEncoding(String url) throws Exception{
		
		if(url == null) return "";
		
		char[] urlChar = url.toCharArray();
		for (int j = 0; j < urlChar.length; j++) {
		    if (urlChar[j] >= '\uAC00' && urlChar[j] <= '\uD7A3') { //한글일때만..
		    
		        String targetText = String.valueOf(urlChar[j]);
		        try {
		        	url = url.replace(targetText, URLEncoder.encode(targetText, "UTF-8"));
		        } catch (UnsupportedEncodingException e) {
		            e.printStackTrace();
		        }
		    } 
		}
		
		url = url.replaceAll("\\ ", "%20");  //공백 제거
		url = url.replaceAll("\\[", "%5B");
		url = url.replaceAll("\\]", "%5D");
		return url;
	}
	
    /**
	 * 무통장 주문건의 경우 미입금 취소 건들은 취소 생성(SaveCancel) 함수 에서 OrderReceipt 및 OrderShipCost를 다시 생성해준다.
	 * 이 과정에서 연동 특성상 실시간이 아닌만큼 데이터가 꼬일 수 있다. 따라서 모든 취소가 처리 된 후에   
	 * 총 주문수량   == 취소처리 완료 된 주문수량 + 결제 완료 된 주문 수량 
	 * **/
	public boolean existUnAttendedCount(String payNo) throws Exception {
		int unAttendedCount = 0;
		unAttendedCount = paGmktOrderService.selectUnAttendedCount(payNo);
		if(unAttendedCount > 0){
			return true;
		}else{
			return false;
		}
	}
	
}
