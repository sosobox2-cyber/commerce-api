package com.cware.netshopping.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Repository;

import zipit.rfnCustCommonAddrList;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;


/**
 * 정제솔루션 모듈화
 * @author  thjeon  2018.04.10
 */
@Repository("com.cware.netshopping.common.util.PostUtil")
public class PostUtil implements java.io.Serializable{

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	private static final long serialVersionUID = 1L;

	/**
	 * 주소검증 
	 * 
	 * @param ArrayList
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> retrieveVerifyPost(ArrayList paramList) throws Exception {
		
		ParamMap paramMap = FlexUtil.getParamMap(paramList);
		
		String postNo = paramMap.get("POST_NO").toString();
		String searchAddr = paramMap.get("SEARCH_ADDR").toString() + paramMap.get("SEARCH_ADDR_INFO").toString();
		String searchAddrDetail = paramMap.get("SEARCH_ADDR2").toString();
		String roadAddrYn = paramMap.get("ROAD_ADDR_YN").toString().equals("1") ? "N" : "J";
		String postIp = "";
		String postPort = "";
		Boolean localYn = false;
		
//		if( "Y".equals(paramMap.get("LOCAL_YN").toString())) {  // 로컬 체크
//			 localYn = true;
//		}else{
//			 localYn = false;
//		}

		ArrayList 	retData   = null;					// 주소정제 데이터영역 오브젝트
		List 		arMacList = null;					// 해쉬키 목록
		
		rfnCustCommonAddrList rfn = new rfnCustCommonAddrList();
		
		// TODO : [수지원넷소프트] 모든 로그 정지
		rfn.log.disableLogs(true);
		//rfn.log.setEnableDebug(true);			rfn.setServerProp(postIp, postPort, "UTF-8");
		
		List configList = systemService.selectTconfig(paramMap);
		
		for(int i=0; i< configList.size(); i++){
			
			HashMap<String, Object> config = (HashMap<String, Object>) configList.get(i);
			
			if(config.get("ITEM").toString().equals("POST_SERVER_IP")){
				postIp = config.get("VAL").toString();
			}else if(config.get("ITEM").toString().equals("POST_SERVER_PORT")){
				postPort = config.get("VAL").toString();
			}
		}
		
		rfn.setServerProp(postIp, postPort, "UTF-8");
		/*
		if (localYn == true) {
			HashMap<String, Object> address = new HashMap<String, Object>();
			ArrayList<HashMap<String, Object>> addressList = new ArrayList<HashMap<String, Object>>();
			
	
			//일반 Case
			
			//[{ZPRNR=10863, CNT=1, NODE=D, STDADDR=341-1번지 CK푸드원, NADR2S=64, CK푸드원 (신촌동), NNMB=4148012000103410001000001, NADR1S=경기도 파주시 소라지로177번길, GISY=37.7374256945, IDX=0, ZPRNSR=610, NNMX=126.6934067778, ADDR1Y=경기도 파주시 신촌동, NNMY=37.7375097473, ZPRNSJB=002, ZPRNJ=10863, GISX=126.6934070524},
			// {ZPRNR=10863, NODE=P, CNT=0, STDADDR=341-1번지 CK푸드원, NADR2S=64, CK푸드원 (신촌동), NNMB=4148012000103410001000001, NADR1S=경기도 파주시 소라지로177번길, GISY=37.7374256945, IDX=0, ZPRNSR=610, NNMX=126.6934067778, ADDR1Y=경기도 파주시 신촌동, NNMY=37.7375097473, ZPRNSJB=002, ZPRNJ=10863, GISX=126.6934070524}]
				
				
			address.put("NNMB",       "4148012000103410001000001");
			address.put("ZPRNJ" ,		  "10863" ); 
			address.put("ZPRNSJB" , 	  "002"   );
			address.put("ZPRNJ" ,  "10863" );
			address.put("ZPRNSR" , "610"   ); 
			address.put("ADDR1Y" ,    "경기도 파주시 신촌동"  );  //지번주소 Base
			address.put("STDADDR" ,    "341-1번지 CK푸드원" );  //지번주소 DT
			address.put("NADR1S" ,	"경기도 파주시 소라지로177번길"	  );   //도로명 주소 Base
			address.put("NADR2S" ,	"64, CK푸드원 (신촌동)"       );   //도로명 주소 DT
			address.put("GISX",        "126.6934070524"      );      //지번 좌표 X
			address.put("GISY",        "37.7374256945"      );      //지번 좌표 Y
			address.put("NNMX",   "126.6934067778"      );  //도로명 좌표 X
			address.put("NNMY", 	"37.7375097473"      );  //도로명 좌표 Y
			
			
			address.put("FLAG", "L");			
			address.put("STD_SEQ_ROAD_NO" , 	"");
			
			//제주 Case
			address.put("NNMB",       "5013025321112290001000001");
			address.put("ZPRNJ" ,		  "63619" ); 
			address.put("ZPRNSJB" , 	  "001"   );
			address.put("ZPRNJ" ,  "63619" );
			address.put("ZPRNSR" , "605"   ); 
			address.put("ADDR1Y" ,    "제주특별자치도 서귀포시 남원읍"  );  //지번주소 Base
			address.put("STDADDR" ,    "남원리 1229-1번지 서귀포시동부노인종합복지회관 101호" );  //지번주소 DT
			address.put("NADR1S" ,	"제주특별자치도 서귀포시 남원읍 남한로"	  );   //도로명 주소 Base
			address.put("NADR2S" ,	"67, 101호 (남원리,서귀포시동부노인종합복지회관)"       );   //도로명 주소 DT
			address.put("GISX",        "126.7149380288"      );      //지번 좌표 X
			address.put("GISY",        "33.2843565698"      );      //지번 좌표 Y
			address.put("NNMX",   "126.7148850032"      );  //도로명 좌표 X
			address.put("NNMY", 	"33.2842633207"      );  //도로명 좌표 Y
			
			
			address.put("FLAG", "L");			
			address.put("STD_SEQ_ROAD_NO" , 	"");
			
			
			addressList.add(address);
			paramMap.put("RESULT", addressList);
			paramMap.put("FLAG", "Local");
			
			return paramMap.get();
		}
		*/
		Map resultMap = rfn.getRfnAddrMap(postNo,searchAddr,searchAddrDetail,"UTF-8",roadAddrYn);
		
		if( arMacList == null ) arMacList=rfn.getMacroList(1);
		
		retData= (ArrayList) resultMap.get("DATA");	
		
//		if( retData != null ) {
//
//			for( int i=0; i<retData.size(); i++ ) {
//				log.debug("[RESULT] ============= [데이터영역(" + (i+1) + "/" + retData.size() + ")] =============");
//				log.debug("[RESULT] [NODE]  " + ((Map) retData.get(i)).get("NODE"));	// D:부모, P:자식
//			
//				
//				// TODO : 해쉬 키값은 가변적임(전문 인터페이스 정의서 참조)
//				for( int j=0; j<arMacList.size(); j++ ) {
//					log.debug("[RESULT] <" + arMacList.get(j) + "> : " + ((Map) retData.get(i)).get(arMacList.get(j)));
//				}
//				System.out.println("");
//			}
//		}		
		
		paramMap.put("RESULT", retData);
		paramMap.put("MSG1",   resultMap.get("RMG2"));
		paramMap.put("MSG2",   resultMap.get("RMG3"));
		paramMap.put("FLAG",   resultMap.get("RCD3"));
		
		return paramMap.get();
	}
	
	public ArrayList<HashMap<String, Object>> checkLocalYn(ArrayList<HashMap<String, Object>> paramList, HttpServletRequest request, HashMap<String,Object> hm) {
		
		if(request.getRemoteHost().equals("0:0:0:0:0:0:0:1")||request.getRemoteHost().equals("127.0.0.1")){
			hm.put("LOCAL_YN","Y");
		} else {
			hm.put("LOCAL_YN","N");
		}
		paramList.add(hm);
		
		return paramList;
	}
} // end of class
