package com.cware.netshopping.common.system.process.impl;

/*
 * System 		: OPEN-API
 * FileName 	: SystemProcessImpl
 * Description 	: 시스템 관련  ProcessImpl Class
 * History
 * company		author	date        Description
 * commerceware	webzest 2011.08.203   신규생성
 */
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import zipit.rfnCustCommonAddrList;

import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.framework.core.property.PropertyService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.domain.CodeVO;
import com.cware.netshopping.domain.model.ApiTracking;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaApiTracking;
import com.cware.netshopping.domain.model.PaRequestMap;
import com.cware.netshopping.common.util.PostUtil;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
@Service("common.system.systemProcess")
public class SystemProcessImpl extends AbstractProcess implements SystemProcess {

    @Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;

    @Resource(name = "propertyService")
    private PropertyService propertyService;
    
    @Resource(name = "com.cware.netshopping.common.util.PostUtil")
	private PostUtil postUtil;

    private List configList = null;
    private List codeList = null;

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
		selectConfigList();
		selectCodeList();
		ConfigUtil.setConfiguration(propertyService.getConfiguration());
    }

    @Override
    public void selectCodeList() throws Exception {
    	this.codeList = systemDAO.selectCodeList(null);
    }

    @Override
    public List getCodeList() throws Exception {
    	return codeList;
    }

    @Override
    public CodeVO[] getCodeList(String codeLgroup) throws Exception {
    	return getCodeList(codeLgroup, false, null, null);
    }

    @Override
    public CodeVO[] getAvailCodeList(String codeLgroup) throws Exception {
    	return getCodeList(codeLgroup, true, null, null);
    }

    @Override
    public CodeVO[] getAvailTermCodeList(String codeLgroup, String codeGreater, String codeLess) throws Exception {
    	return getCodeList(codeLgroup, true, codeGreater, codeLess);
    }

    @Override
    public CodeVO[] getCodeList(String codeLgroup, boolean isAvail, String codeGreater, String codeLess) throws Exception {
		List argList = null;
		Iterator i = codeList.iterator();
		CodeVO tmpCodeVO;
		while (i.hasNext()) {
		    tmpCodeVO = (CodeVO) i.next();
		    if (tmpCodeVO.getCodeLgroup().equals(codeLgroup)) {
			if (!isAvail || (isAvail && "1".equals(tmpCodeVO.getUseYn()) && !"0000".equals(tmpCodeVO.getCodeMgroup()))) {
			    if ((codeGreater == null || codeGreater.compareTo(tmpCodeVO.getCodeMgroup()) < 0)
				    && (codeLess == null || codeLess.compareTo(tmpCodeVO.getCodeMgroup()) > 0)) {
				if (argList == null)
				    argList = new ArrayList();
				argList.add(tmpCodeVO);
			    }
			}
		    } else {
			if (argList != null && argList.size() > 0)
			    break;
		    }
		}
		return (CodeVO[]) argList.toArray(new CodeVO[0]);
    }

    @Override
    public CodeVO getCode(String codeLgroup, String codeMgroup) throws Exception {
    	return getCode(codeLgroup, codeMgroup, false, null, null);
    }

    @Override
    public CodeVO getAvailCode(String codeLgroup, String codeMgroup) throws Exception {
    	return getCode(codeLgroup, codeMgroup, true, null, null);
    }

    @Override
    public HashMap<String, Object> getAvailCodeAsMap(String codeLgroup, String codeMgroup) throws Exception {
		CodeVO code = getCode(codeLgroup, codeMgroup, true, null, null);
		if (code == null)
		    return null;
		HashMap hashMap = new HashMap();
		hashMap.put("CODE_MGROUP", code.getCodeMgroup());
		hashMap.put("CODE_NAME", code.getCodeName());
		hashMap.put("CODE_GROUP", code.getCodeGroup());
		hashMap.put("REMARK", code.getRemark());
		return hashMap;
    }

    @Override
    public HashMap<String, Object>[] getAvailCodeListAsMap(String codeLgroup, String codeMgroup) throws Exception {
		CodeVO[] argList = getCodeList(codeLgroup);
		ArrayList codeMgroupList = new ArrayList();
		for (int i = 0; i < argList.length; i++) {
		    if ("1".equals(argList[i].getUseYn()) && !"0000".equals(argList[i].getCodeMgroup())) {
			if (argList[i].getCodeMgroup().matches(codeMgroup + "*")) {
			    HashMap hashMap = new HashMap();
			    hashMap.put("CODE_MGROUP", argList[i].getCodeMgroup());
			    hashMap.put("CODE_NAME", argList[i].getCodeName());
			    hashMap.put("CODE_GROUP", argList[i].getCodeGroup());
			    hashMap.put("REMARK", argList[i].getRemark());
			    codeMgroupList.add(hashMap);
			}
		    }
		}
		return (HashMap[]) codeMgroupList.toArray(new HashMap[0]);
    }

    @Override
    public CodeVO getAvailTermCode(String codeLgroup, String codeMgroup, String codeGreater, String codeLess) throws Exception {
    	return getCode(codeLgroup, codeMgroup, true, codeGreater, codeLess);
    }

    @Override
    public CodeVO getCode(String codeLgroup, String codeMgroup, boolean isAvail, String codeGreater, String codeLess) throws Exception {
		CodeVO[] argList = getCodeList(codeLgroup);
		CodeVO tmpCodeVO;
		for (int i = 0; i < argList.length; i++) {
		    tmpCodeVO = argList[i];
		    if (tmpCodeVO.getCodeMgroup().equals(codeMgroup)) {
			if (!isAvail || (isAvail && "1".equals(tmpCodeVO.getUseYn()))) {
			    if ((codeGreater == null || codeGreater.compareTo(tmpCodeVO.getCodeMgroup()) < 0)
				    && (codeLess == null || codeLess.compareTo(tmpCodeVO.getCodeMgroup()) > 0)) {
				return tmpCodeVO;
			    }
			}
		    }
		}
		return null;
    }

    @Override
    public void selectConfigList() throws Exception {
    	this.configList = systemDAO.selectConfigList(null);
    }

    @Override
    public List<Config> getConfigList() throws Exception {
    	return configList;
    }

    @Override
    public Config getConfig(String item) throws Exception {
		Iterator i = configList.iterator();
		Config config;
		while (i.hasNext()) {
		    config = (Config) i.next();
		    if (config.getItem().equals(item)) {
			return config;
		    }
		}
		return null;
    }

    @Override
    public String getVal(String item) throws Exception {
    	return getConfig(item).getVal();
    }

    @Override
    public HashMap<String, Object> getConfig() throws Exception {
		HashMap configMap = new HashMap();
		Iterator i = configList.iterator();
		Config config;
		while (i.hasNext()) {
		    config = (Config) i.next();
		    configMap.put(config.getItem(), config.getVal());
		}
		return configMap;
    }

    @Override
    public List<Config> selectConfigCardList() throws Exception {
    	return (List<Config>) systemDAO.selectConfigCardList();
    }

    @Override
    public Timestamp getSysdate() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return new Timestamp(sdf.parse(systemDAO.getSysdate()).getTime());
    }

    @Override
    public Timestamp getSysdatetime() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return new Timestamp(sdf.parse(systemDAO.getSysdatetime()).getTime());
    }

    @Override
    public String getSysdateToString() throws Exception {
    	return systemDAO.getSysdate();
    }
    
    @Override
    public String getRemitDate() throws Exception {
    	return systemDAO.getRemitDate();
    }

    @Override
    public String getSysdatetimeToString() throws Exception {
    	return systemDAO.getSysdatetime();
    }

    @Override
    public String getSysday() throws Exception {
    	return systemDAO.getSysday();
    }

    @Override
    public Date getDate() throws Exception {
    	return systemDAO.getDate();
    }

    private CodeVO getCodeByCondition(CodeVO codeVO, HashMap<String, Object> hashMap) {
		if (codeVO == null)
		    return codeVO;
		if (hashMap.get("not_code_mgroup") != null) {
		    if (codeVO.getCodeMgroup().equals(ComUtil.objToStr(hashMap.get("not_code_mgroup")))) {
			return null;
		    }
		}
		if (hashMap.get("use_yn") != null) {
		    if (!codeVO.getUseYn().equals(ComUtil.objToStr(hashMap.get("use_yn")))) {
			return null;
		    }
		}
		return codeVO;
    }

    @Override
    public String getSequenceNo(String type) throws Exception {
		String sequenceNo = "";
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("sequence_type", type);
		if (hashMap.get("sequence_type").equals("CUST_NO") || hashMap.get("sequence_type").equals("ORDER_NO") || hashMap.get("sequence_type").equals("GOODS_SELECT_NO")
			|| hashMap.get("sequence_type").equals("RECEIPT_NO")) {
		    hashMap.put("condition_flag", (String) systemDAO.selectSequenceNoCondition(hashMap));
		}
		sequenceNo = (String) systemDAO.selectSequenceNo(hashMap);
		if (sequenceNo == null || sequenceNo.equals(""))
		    throw processException("msg.cannot_retrieve", new String[] { "getSequenceNo : " + type });
		return sequenceNo;
    }

    @Override
    public String getMaxNo(String tableName, String columnName, String modString, int seqFormat) throws Exception {
		String maxNo = "";
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("tableName", tableName);
		hashMap.put("columnName", columnName);
		hashMap.put("modQuery", modString);
		maxNo = (String) systemDAO.selectMaxNo(hashMap);
	
		if (maxNo == null || ("").equals(maxNo)) {
		    maxNo = ComUtil.lpad("1", seqFormat, "0");
		} else {
		    maxNo = ComUtil.increaseLpad(maxNo, seqFormat, "0");
		}
		return maxNo;
    }

    @Override
    public String getSeqNo(String tableName, String columnName, Timestamp argDate, int seqFormat) throws Exception {
		String seqNo = "";
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("table_name", tableName);
		hashMap.put("column_name", columnName);
		hashMap.put("arg_date", argDate);
		seqNo = (String) systemDAO.selectSeqNo(hashMap);
	
		if (seqNo == null || ("").equals(seqNo)) {
		    hashMap.put("arg_date", argDate);
		    hashMap.put("seq_format", seqFormat);
		    seqNo = (String) systemDAO.selectSeqNoEmpty(hashMap);
		}
		return seqNo;
    }

    @Override
    public String getSeqNo(String tableName, String columnName, String modString, int seqFormat) throws Exception {
		String seqNo = "";
		String format = "0";
	
		for (int i = 1; i < seqFormat; i++) {
		    format += "0";
		}
	
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("tableName", tableName);
		hashMap.put("columnName", columnName);
		hashMap.put("modQuery", modString);
		hashMap.put("format", format);
	
		seqNo = (String) systemDAO.selectSeqNoFormat(hashMap);
	
		return seqNo;
    }


	@Override
	public int insertApiTracking(HttpServletRequest request, ParamMap paramMap) throws Exception {
		
		ApiTracking apiTracking = new ApiTracking();
		Map<String, String[]> requestMap = null;
		Enumeration<String>  formKeys = null;
		String key = null;
		String emtpyValue ="Empty";
		StringBuilder urlParamter = new StringBuilder();
		
		apiTracking.setLinkCode(ComUtil.NVL(paramMap.getString("linkCode"),emtpyValue));
		apiTracking.setIpAddr(request.getRemoteHost());
		apiTracking.setApiCode(paramMap.getString("apiCode"));
		apiTracking.setUserId(ComUtil.NVL(paramMap.getString("entpId").toUpperCase(),"999999"));
		apiTracking.setMethod(request.getMethod());
		
			requestMap = request.getParameterMap();
			formKeys = request.getParameterNames();
			while (formKeys.hasMoreElements()) {
				key = (String) formKeys.nextElement();
				urlParamter.append(key);
				urlParamter.append("=");
				if(key.equals("entpPass")){
					int PassLength = requestMap.get(key)[0].toString().length();
					StringBuilder Pass = new StringBuilder();
						for(int i = 0; i < PassLength; i++){
							Pass.append("*");							
						}
					urlParamter.append(Pass);
				}else if(key.equals("entpId")){
					urlParamter.append(requestMap.get(key)[0].toString().toUpperCase());
				}else{					
					urlParamter.append(requestMap.get(key)[0].toString());
				}
				if(formKeys.hasMoreElements()){
					urlParamter.append("&");
				}
			}
			apiTracking.setPrm(urlParamter.toString());
		
		apiTracking.setResultCode(paramMap.getString("code"));
		apiTracking.setResultMsg(ComUtil.subStringBytes(paramMap.getString("message"), 2000));
		apiTracking.setStartDate(paramMap.getTimestamp("startDate"));
		apiTracking.setApiType("OPEN-API");
		
		return systemDAO.insertApiTracking(apiTracking);
		
	}
	
	
	
	@Override
	public int insertPaApiTracking(HttpServletRequest request, ParamMap paramMap) throws Exception {
		
		PaApiTracking apiTracking = new PaApiTracking();
		Map<String, String[]> requestMap = null;
		Enumeration<String>  formKeys = null;
		String key = null;
		String emtpyValue ="Empty";
		StringBuilder urlParamter = new StringBuilder();
		String paSiteGb = paramMap.getString("siteGb");
		apiTracking.setLinkCode(ComUtil.NVL(paramMap.getString("linkCode"),emtpyValue));
		apiTracking.setIpAddr(request.getRemoteHost());
		apiTracking.setApiCode(paramMap.getString("apiCode"));
		apiTracking.setUserId(ComUtil.NVL(paramMap.getString("entpId").toUpperCase(),"999999"));
		apiTracking.setMethod(request.getMethod());
		
			requestMap = request.getParameterMap();
			formKeys = request.getParameterNames();
			while (formKeys.hasMoreElements()) {
				key = (String) formKeys.nextElement();
				urlParamter.append(key);
				urlParamter.append("=");
				if(key.equals("entpPass")){
					int PassLength = requestMap.get(key)[0].toString().length();
					StringBuilder Pass = new StringBuilder();
						for(int i = 0; i < PassLength; i++){
							Pass.append("*");							
						}
					urlParamter.append(Pass);
				}else if(key.equals("entpId")){
					urlParamter.append(requestMap.get(key)[0].toString().toUpperCase());
				}else{					
					urlParamter.append(requestMap.get(key)[0].toString());
				}
				if(formKeys.hasMoreElements()){
					urlParamter.append("&");
				}
			}
			apiTracking.setPrm(urlParamter.toString());
		
		apiTracking.setResultCode(paramMap.getString("code"));
		apiTracking.setResultMsg(ComUtil.subStringBytes(paramMap.getString("message"), 2000));
		apiTracking.setStartDate(paramMap.getTimestamp("startDate"));
		
		if(paSiteGb.equals("")){
			paSiteGb = "PA11";
			if(paramMap.getString("paCode").equals("41")){
				paSiteGb = "PANAVER";
			}
		}
		apiTracking.setSiteGb(paSiteGb);
		apiTracking.setApiType("OPEN-API");
		
		return systemDAO.insertPaApiTracking(apiTracking);
		
	}
	
	@Override
	public int insertPaApiTracking(ParamMap paramMap) throws Exception {
		
		PaApiTracking apiTracking = new PaApiTracking();
		
		apiTracking.setLinkCode(ComUtil.NVL(paramMap.getString("linkCode"), "Empty"));
		apiTracking.setApiCode(ComUtil.NVL(paramMap.getString("apiCode"), "unknown"));
		apiTracking.setUserId(ComUtil.NVL(paramMap.getString("entpId").toUpperCase(),"999999"));
		apiTracking.setResultCode(paramMap.getString("code"));
		apiTracking.setResultMsg(ComUtil.subStringBytes(paramMap.getString("message"), 2000));
		apiTracking.setStartDate(getSysdatetime());
		apiTracking.setSiteGb(ComUtil.NVL(paramMap.getString("siteGb"), "PA11"));
		apiTracking.setIpAddr("");
		apiTracking.setPrm("");
		apiTracking.setApiType("OPEN-API");

		return systemDAO.insertPaApiTracking(apiTracking);
		
	}
	
	public HashMap<String, String> selectSsgApiInfo(String apiCode) throws Exception{

	    ParamMap rtnMap = new ParamMap();	    
	    HashMap<String, String> rtnAMap = systemDAO.selectSsgApiInfo(apiCode);
	    
	    return rtnAMap;
	}
	
	public HashMap<String, String> selectPaApiInfo(ParamMap paramMap) throws Exception{

	    ParamMap rtnMap = new ParamMap();	    
	    HashMap<String, String> rtnAMap = systemDAO.selectPaApiInfo(paramMap);
	    
	    return rtnAMap;
	}
	
	public HashMap<String, String> selectPaApiInfo4Url(String apiCode) throws Exception{

	    ParamMap rtnMap = new ParamMap();	    
	    HashMap<String, String> rtnAMap = systemDAO.selectPaApiInfo4Url(apiCode);
	    
	    return rtnAMap;
	}
	
	public String checkCloseHistory(String flag, String prg_id) throws Exception{
		String proc_yn = "";
		int executedRtn = 0;
		ParamMap paramMap = new ParamMap();
		
		proc_yn = systemDAO.checkCloseHistory(prg_id);
		
		paramMap.put("prg_id", prg_id);
		
		if(flag.equals("start")){
			if(proc_yn == null){
				executedRtn = systemDAO.insertCloseHistory(prg_id);
				if(executedRtn != 1){
					 throw processException("msg.cannot_save", new String[] { "TCLOSEHISTORY" });
				}
			} else if(proc_yn.equals("0")){
				// ok_yn = 0 , proc_yn =1
				paramMap.put("ok_yn", "0");
				paramMap.put("proc_yn", "1");
				paramMap.put("proc_flag", "0");
				executedRtn = systemDAO.updateCloseHistory(paramMap);
				if(executedRtn != 1){
					return proc_yn = "1";
				}
			}
		} else if(flag.equals("end")){
			// ok_yn = 1 , proc_yn =0
			paramMap.put("ok_yn", "1");
			paramMap.put("proc_yn", "0");
			executedRtn = systemDAO.updateCloseHistory(paramMap);
			if(executedRtn != 1){
				 throw processException("msg.cannot_save", new String[] { "TCLOSEHISTORY" });
			}
		}
		
		if(proc_yn == null || proc_yn.equals("")) proc_yn = "0";
		
		return proc_yn;
	}
	
	/**
	 * tconfig 조회
	 * 
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	@Override
	public List<HashMap<String, Object>> selectTconfig(ParamMap paramMap) throws Exception {
		return systemDAO.selectTconfig(paramMap);
	}
	
	public ParamMap selectStdAddress(ParamMap paramMap) throws Exception {
		ParamMap rtnMap = new ParamMap();
		Map<String, Object> post = new HashMap<String, Object>();
		
		log.info("===== order-input : selectStdAddress START =====");

		/** 주소정제 추가 **/
		ArrayList<HashMap<String, Object>> paramList = new ArrayList<>();
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("POST_NO", 		paramMap.getString("post_no"));
		hm.put("POST_SEQ", 		paramMap.getString("post_seq"));
		hm.put("ROAD_ADDR_YN",	paramMap.getString("road_addr_yn"));
		hm.put("SEARCH_ADDR", 	paramMap.getString("search_addr"));
		hm.put("SEARCH_ADDR2", 	paramMap.getString("search_addr2"));
		hm.put("LOCAL_YN",		paramMap.getString("localYn"));
		hm.put("SEARCH_ADDR_INFO", "");  //뭐에 쓰는거지??.삭제? 확인필요
		paramList.add(hm);
		try {
			rtnMap.put("SEARCH_POST"		, paramMap.getString("post_no"));
			rtnMap.put("SEARCH_POST_SEQ"	, paramMap.getString("post_seq"));
			rtnMap.put("SEARCH_ADDR"		, paramMap.getString("search_addr"));
			rtnMap.put("SEARCH_ADDRDT"		, paramMap.getString("search_addr2"));
			rtnMap.put("FULL_ADDR"			, paramMap.getString("full_addr"));
			
			post = postUtil.retrieveVerifyPost(paramList);
			
			ArrayList<HashMap<String, Object>> b = (ArrayList<HashMap<String, Object>>)post.get("RESULT");
			 
			if (b != null){
			
				String flg = post.get("FLAG").toString();
				
				if ("H".equals(flg) || "I".equals(flg) || "Local".equals(flg)){
					rtnMap.put("FLG" , 	"1");//정제성공
				}else{
					rtnMap.put("FLG" , 	"0");
				}//end of FLG			
				
				//[{ZPRNR=10863, CNT=1, NODE=D, STDADDR=341-1번지 CK푸드원, NADR2S=64, CK푸드원 (신촌동), NNMB=4148012000103410001000001, NADR1S=경기도 파주시 소라지로177번길, GISY=37.7374256945, IDX=0, ZPRNSR=610, NNMX=126.6934067778, ADDR1Y=경기도 파주시 신촌동, NNMY=37.7375097473, ZPRNSJB=002, ZPRNJ=10863, GISX=126.6934070524},
				// {ZPRNR=10863, NODE=P, CNT=0, STDADDR=341-1번지 CK푸드원, NADR2S=64, CK푸드원 (신촌동), NNMB=4148012000103410001000001, NADR1S=경기도 파주시 소라지로177번길, GISY=37.7374256945, IDX=0, ZPRNSR=610, NNMX=126.6934067778, ADDR1Y=경기도 파주시 신촌동, NNMY=37.7375097473, ZPRNSJB=002, ZPRNJ=10863, GISX=126.6934070524}]
				//result.get("NNMB").toString();
				Map<String, Object> result = (Map<String, Object>) b.get(0);

				
				if ( paramMap.getInt("road_addr_yn") == 1){
					rtnMap.put("ADDR_GBN" , 	"2"	);//주소입력타입[1:지번주소, 2:도로명주소] 			
				}
				else{
					rtnMap.put("ADDR_GBN" , 	"1"	);//주소입력타입[1:지번주소, 2:도로명주소] 					
				}
				
				rtnMap.put("CL_SUCCESS_YN" , 	"1"); 							
				rtnMap.put("ROAD_ADDR_NO",      result.get("NNMB").toString());
				
				rtnMap.put("STD_POST_NO" ,		result.get("ZPRNJ").toString()); 
				rtnMap.put("STD_POST_SEQ" , 	    result.get("ZPRNSJB").toString());
				
				rtnMap.put("STD_ROAD_POST_NO" ,		result.get("ZPRNJ").toString());
				rtnMap.put("STD_ROAD_POST_SEQ" , 	result.get("ZPRNSR").toString()); 
				
				rtnMap.put("STD_POST_ADDR1" ,               result.get("ADDR1Y").toString());  //지번주소 Base
				rtnMap.put("STD_POST_ADDR2" ,               result.get("STDADDR").toString());  //지번주소 DT
								
				rtnMap.put("STD_ROAD_POST_ADDR1" ,			result.get("NADR1S").toString());   //도로명 주소 Base
				rtnMap.put("STD_ROAD_POST_ADDR2" ,	     	result.get("NADR2S").toString());   //도로명 주소 DT

				rtnMap.put("STD_POST_LNGX", result.get("GISX").toString());      //지번 좌표 X
				rtnMap.put("STD_POST_LATY", result.get("GISY").toString());      //지번 좌표 Y
				
				rtnMap.put("STD_ROAD_POST_LNGX", result.get("NNMX").toString());  //도로명 좌표 X
				rtnMap.put("STD_ROAD_POST_LATY", result.get("NNMY").toString());  //도로명 좌표 Y
				
				rtnMap.put("REFINE_RESULT_CODE", post.get("FLAG").toString());			
				rtnMap.put("STD_SEQ_ROAD_NO" , 	"");
				
				if( rtnMap.getString("FLG").equals("1") ) {
					boolean check = true;
				
					if( rtnMap.getString("ADDR_GBN").equals("2") && rtnMap.getString("ROAD_ADDR_NO").equals("") ) {
						check = false;
					} else if( rtnMap.getString("STD_POST_NO").equals("") ) {
						check = false;
					} else if( rtnMap.getString("STD_POST_SEQ").equals("") ) {
						check = false;
					} else if( rtnMap.getString("STD_ROAD_POST_NO").equals("") ) {
						check = false;
					} else if( rtnMap.getString("STD_ROAD_POST_SEQ").equals("") ) {
						check = false;
					} else if( rtnMap.getString("STD_POST_ADDR1").equals("") ) {
						check = false;
					} else if( rtnMap.getString("STD_POST_ADDR2").equals("") ) {
						check = false;
					} else if( rtnMap.getString("STD_ROAD_POST_ADDR1").equals("") ) {
						check = false;
					} else if( rtnMap.getString("STD_ROAD_POST_ADDR2").equals("") ) {
						check = false;
					}
				
					if(!check) {
						rtnMap.put("FLG" , 	"0");
					}
				}
			
			}else{ 		
				rtnMap.put("CL_SUCCESS_YN" , "0"); 
			}	  
			log.info("result" + ":" +
			rtnMap.toString());
			log.info("===== order-input : selectStdAddress END ====="); 
			
			
			//rtnMap.put("FLG" , 	"0");//for Testing address refinary
			return rtnMap;
			
		} catch (Exception e) {
			throw processException("msg.cannot_create", new String[] { "주소정제" });
		}// end of 정제

		
	}
	
	public int selectPaprocCheck(ParamMap paramMap) throws Exception{
		return systemDAO.selectPaprocCheck(paramMap);
	}
	
	@Override
	public String insertPaRequestMap(PaRequestMap paRequestMap) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;
		
		try{
			String procYn = systemDAO.selectCodeRemarkChkForRequestMap(paRequestMap.getReqApiCode());

			if(procYn.equals("1")){
				executedRtn = systemDAO.insertPaRequestMap(paRequestMap);
				if(executedRtn < 1){
					log.error("전문저장 실패");
				}
			}	
		}catch(Exception e){
			log.error("전문저장 실패2 ");
		}
	
		return rtnMsg;
	}

	@Override
	public HashMap<String, String> selectCloseHistory(String apiCode) throws Exception {
		return systemDAO.selectCloseHistory(apiCode);
	}

	@Override
	public String selectTcodeVal(String codeLGroup, String codeMGroup) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("codeLGroup", codeLGroup);
		paramMap.put("codeMGroup", codeMGroup);
		return systemDAO.selectTcodeVal(paramMap);
	}

	@Override
	public String getValRealTime(String item) throws Exception {
		return systemDAO.getVal(item);
	}
}