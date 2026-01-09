package com.cware.netshopping.pagmkt.util.rest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cware.framework.core.basic.ParamMap;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public abstract class PaGmktAbstractRest {

	public abstract String getRequstBody(ParamMap param) throws Exception;

	
	//TODO 해당 클레스는 사실 Interface로 바꾸고 다음 함수들은 Inferface의 Default Method로 구현 했어야함.. 시간이 허락하면 전 소스 수정 필요....
	
	//To change Date Format For G-market   
	//input  : any Date type (String) 
	//output : yyyy-MM-dd'T'HH:mm:ssZ(String) 
	protected String gDate(String inputDate) { 
		
		if(inputDate == null || inputDate.equals("")) return null;
			
		String DATE_FORMAT_I = "yyyy-MM-dd HH:mm:ss";
		String DATE_FORMAT_O = "yyyy-MM-dd'T'HH:mm:ssZ"; 
			
		return changeDateFormat(inputDate, DATE_FORMAT_I,DATE_FORMAT_O);
		
	}
	
	//To change Date Format For G-market   
	//input  : any Date type (String) 
	//output : yyyy-MM-dd    (String) 
	protected String yymmddDate(String inputDate) { 
		
		if(inputDate == null || inputDate.equals("")) return "";
			
		String DATE_FORMAT_I = "yyyy-MM-dd HH:mm:ss";
		String DATE_FORMAT_O = "yyyy-MM-dd"; 
			
		return changeDateFormat(inputDate, DATE_FORMAT_I,DATE_FORMAT_O);
		
	}
	
	private String changeDateFormat(String inputDate, String dateFormatI,String dateFormatO ){
		String dateString; 
		
		try {
			
			SimpleDateFormat formatInput = new SimpleDateFormat(dateFormatI);
			SimpleDateFormat formatOutput = new SimpleDateFormat(dateFormatO);
			Date date = formatInput.parse(inputDate);
			dateString = formatOutput.format(date);
		}catch (ParseException e){
			e.printStackTrace();
			return null;
		}
		return dateString;
	}
	
	protected String mapToJson(Map<String,Object> param) {
		
		if(param == null) return "";
		if(param.size() < 1) return "";
		
		ObjectMapper objectMapper = new ObjectMapper();
	
		try {
			return objectMapper.writeValueAsString(param);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			return "";
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	public String getJsonObject4Testing() throws JSONException { return null; };
	
	/**Testing을 위한 함수**/
	public ResponseEntity<String> getJSONResponse(Object obj){
		
		String json = obj.toString();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<String>(json, responseHeaders, HttpStatus.OK);
	}
	

}
