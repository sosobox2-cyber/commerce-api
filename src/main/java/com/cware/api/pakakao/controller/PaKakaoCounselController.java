package com.cware.api.pakakao.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.pakakao.counsel.service.PaKakaoCounselService;
import com.cware.netshopping.pakakao.util.PaKakaoConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/pakakao/counsel", description="상담")
@Controller("com.cware.api.pakakao.PaKakoCounselController")
@RequestMapping(value = "/pakakao/counsel")
public class PaKakaoCounselController extends AbstractController  {
	
	private transient static Logger log = LoggerFactory.getLogger(PaKakaoCommonController.class);
	
	@Autowired
	private PaKakaoConnectUtil paKakaoConnectUtil;
	
	@Autowired
    private SystemService systemService;
	
	@Autowired
	private PaKakaoCounselService paKakaoCounselService;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "상품문의 목록 조회", notes = "상품문의 목록 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/qna-list", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<ResponseMsg> qnaList(HttpServletRequest request,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value = "procId", required = false, defaultValue = Constants.PA_KAKAO_PROC_ID) String procId
			) throws Exception {
		
		String	prg_id 		= "IF_PAKAKAOAPI_02_001";
		ParamMap apiInfoMap	= new ParamMap();
		ParamMap errorMap	= null;
		String paCode		= "";
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> qnaList = null;
		List<PaqnamVO> paQnaList = null;
		String msgGb = "00";
		//구두점, 마크 및 공백 허용: \p{P} \p{Z} \p{M}
		//허용 번호: \p{N}
		//모든 언어 문자 집합 허용: \p{L}
		//보이지 않는 제어 문자 허용: \p{Cs} \s{Cf}
		String emoRegex = "[^\\p{P}\\p{Z}\\p{M}\\p{N}\\p{L}\\p{Cs}\\p{Cf}\\s]";
		
		try {
			
			log.info(prg_id + "KAKAO 상품문의조회 API - 01.프로그램 중복 실행 검사 [start]");
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			String dateTime = systemService.getSysdatetimeToString();
			
			for(int i = 0; i < Constants.PA_KAKAO_CONTRACT_CNT; i++) {
				
				paCode = (i == 0) ? Constants.PA_KAKAO_BROAD_CODE : Constants.PA_KAKAO_ONLINE_CODE;
				paQnaList = new ArrayList<PaqnamVO>();
				int page = 1;
				boolean	lastYn = false;
				apiInfoMap.put("paCode", paCode);
				
				while(!lastYn) {
					apiInfoMap.put("url", url.replace("{page}", Integer.toString(page)));
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
					
					if("200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
						qnaList = (List<Map<String, Object>>)(map.get("contents"));
						for(int j=0; j<qnaList.size(); j++) {
							try {
								PaqnamVO paqnamVo = new PaqnamVO();
								paqnamVo.setPaCode(paCode);
								paqnamVo.setPaGroupCode(Constants.PA_KAKAO_GROUP_CODE);
								paqnamVo.setPaCounselDtSeq("1");
								paqnamVo.setPaCounselNo(qnaList.get(j).get("qnaId").toString());
								paqnamVo.setCounselDate(DateUtil.toTimestamp(qnaList.get(j).get("createdAt").toString(), DateUtil.DEFAULT_JAVA_DATE_FORMAT));
								paqnamVo.setTitle("카카오 상품문의");
								paqnamVo.setProcNote(qnaList.get(j).get("content").toString().replaceAll(emoRegex, ""));
								if(qnaList.get(j).containsKey("imageUrls")) {
									paqnamVo.setProcNote(paqnamVo.getProcNote() + qnaList.get(j).get("imageUrls").toString());
								}					
								paqnamVo.setProcId(procId);
								paqnamVo.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paqnamVo.setPaGoodsCode(qnaList.get(j).get("productId").toString());
								paqnamVo.setDisplayYn("true".equals(qnaList.get(j).get("secret").toString())?"0":"1");
								paqnamVo.setOrderYn("true".equals(qnaList.get(j).get("ordered").toString())?"1":"0");
								paqnamVo.setPaOrderNo("true".equals(qnaList.get(j).get("ordered").toString())?qnaList.get(j).get("orderId").toString():"");
								paqnamVo.setMsgGb(msgGb);
								paqnamVo.setInsertId(procId);
								paqnamVo.setModifyId(procId);
								paqnamVo.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paqnamVo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
								paqnamVo.setProcGb("10");
								paqnamVo.setTransYn("0");
								
								if("PRODUCT".equals(qnaList.get(j).get("type").toString())) {
									paqnamVo.setCounselGb("01");
								} else if ("DELIVERY".equals(qnaList.get(j).get("type").toString())) {
									paqnamVo.setCounselGb("02");
								} else if ("RETURN".equals(qnaList.get(j).get("type").toString())) {
									paqnamVo.setCounselGb("03");
								} else if ("EXCHANGE".equals(qnaList.get(j).get("type").toString())) {
									paqnamVo.setCounselGb("04");
								} else if ("CANCEL".equals(qnaList.get(j).get("type").toString())) {
									paqnamVo.setCounselGb("05");
								} else {
									paqnamVo.setCounselGb("06");
								}
								
								paQnaList.add(paqnamVo);
								
							} catch (Exception e) {
								apiInfoMap.put("code", "500");
								apiInfoMap.put("message", e.getMessage());
							}
						}
						
					} else {
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
						
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", errorMap.get("errorMsg").toString());
						break;
					}
					
					lastYn = "true".equals(map.get("last").toString())?true:false;
					page++;
				}
				
				paKakaoCounselService.savePaQnaTx(paQnaList);
			}
			
			//SK스토아 상담 생성
			paKakaoCounselService.saveCustCounselTx();
			
			log.info(prg_id + "KAKAO 상품문의조회 API - END");
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품Q&A 답변 API
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품Q&A 답변", notes = "상품Q&A 답변", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/qna-anser", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseMsg> qnaAnser(HttpServletRequest request,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value = "procId", required = false, defaultValue = Constants.PA_KAKAO_PROC_ID) String procId
			) throws Exception {
		
		String	prg_id 		= "IF_PAKAKAOAPI_02_002";
		ParamMap apiInfoMap	= new ParamMap();
		ParamMap apiDataMap	= null;
		ParamMap errorMap	= null;
		String msgGb = "00"; //지금은 일반상담밖에 없음. 나중에 긴급메세지 생길 지 모르니 일단 00하드코딩
		ParamMap paramMap = new ParamMap();
		List<PaqnamVO> answerQnaList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			log.info(prg_id + "KAKAO 상품문의답변 API - 01.프로그램 중복 실행 검사 [start]");
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
		
			String	dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("msgGb", msgGb);
			paramMap.put("paGroupCode", Constants.PA_KAKAO_GROUP_CODE);
			
			answerQnaList = paKakaoCounselService.selectPaAnserQnaList(paramMap);
			
			for(PaqnamVO answerQna : answerQnaList) {
				
				try {
					apiInfoMap.put("paCode", answerQna.getPaCode());
					
					apiDataMap	= new ParamMap();
					apiDataMap.put("qnaId", answerQna.getPaCounselNo());
					apiDataMap.put("answer", answerQna.getProcNote());
					
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
					
					if("200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
						
						answerQna.setModifyId(procId);
						answerQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						answerQna.setProcGb("40");
						answerQna.setTitle("");
						answerQna.setProcId(procId);
						answerQna.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						paKakaoCounselService.savePaQnaTransTx(answerQna);
						
					} else {
						
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
						
						if(errorMap.get("errorMsg").toString().indexOf("상품문의를 찾을수 없습니다") != -1) {
							answerQna.setModifyId(procId);
							answerQna.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							answerQna.setProcGb("40");
							answerQna.setTitle("삭제된 상담처리");
							answerQna.setProcId(procId);
							answerQna.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							paKakaoCounselService.savePaQnaTransTx(answerQna);
						} else {
							apiInfoMap.put("code", "500");
							apiInfoMap.put("message", errorMap.get("errorMsg").toString());							
						}
					}
				} catch (Exception e) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", e.getMessage());
				}
			}
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
}
