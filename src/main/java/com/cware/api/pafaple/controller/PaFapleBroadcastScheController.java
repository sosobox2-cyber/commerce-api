package com.cware.api.pafaple.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pafaple.domain.Programs;
import com.cware.netshopping.pafaple.domain.model.Item;
import com.cware.netshopping.pafaple.domain.model.Program;
import com.cware.netshopping.pafaple.service.PaFapleBroadcastService;
import com.cware.netshopping.pafaple.util.PaFapleConnectUtil;

import io.swagger.annotations.Api;

@Api(value="/pafaple/broadcast", description="패션플러스 방송")
@RestController
@RequestMapping(path = "/pafaple/broadcast")
public class PaFapleBroadcastScheController extends AbstractProcess {
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private PaFapleConnectUtil paFapleConnectUtil;
	
	@Resource(name = "pafaple.broadcast.PaFapleBroadcastService")
	private PaFapleBroadcastService paFapleBroadcastService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService pacommonService;
	
	/**
	 * 패션플러스 방송편성표 조회
	 * @param broadScheDate
	 * @return programs
	 */
	@GetMapping(value="/programs")
	public ResponseEntity<?> broadCastScheList(HttpServletRequest request,
		    @RequestParam(value = "broadScheDate", required = true) String broadScheDate,
		    @RequestParam(value = "openApiCode", required = true) String openApiCode) throws Exception {
		
		String duplicateCheck = "";
		String apiCode = "IF_PAFAPLEAPI_06_001";
		
		ParamMap paramMap = new ParamMap();
		
		paramMap.put("paCode", "FAPLE");
		paramMap.put("apiCode", apiCode);
		paramMap.put("openApiCode", openApiCode);
		paramMap.put("paGroupCode", PaGroup.FAPLE.code()); 
		paramMap.put("bdDate", broadScheDate);
		paramMap.put("siteGb", "PAFAPLE");
		paramMap.put("insertId", "PAFAPLE");
		
		Programs programs = new Programs();
		
		List<Pabroadreplace> alternativeInfo = null;

		try {
			log.info("======= 방송편성표 조회 Start - {} =======", broadScheDate);
			
			 //= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
	    	if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});	
	    	
			String dateTime = systemService.getSysdatetimeToString();
			String rtnMsg = "";
			
			// 입력날짜 세팅
			SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd", java.util.Locale.KOREA);
			formatter.setLenient(false);
			formatter.parse(broadScheDate);
			
			rtnMsg = pacommonService.selectCheckOpenApiCode(paramMap);
			if (!rtnMsg.equals("Y")) {
				log.info("[Error] 인증코드가 일치하지 않습니다.");
				return new ResponseEntity<ResponseMsg>(new ResponseMsg("410", "인증코드가 일치하지 않습니다."), HttpStatus.LENGTH_REQUIRED);
			}
			
			// 대체편성 상품 설정. 매주 일요일마다 7일간 제일 많이 팔린 상품을 대체편성 상품으로 설정한다.
			log.info("01. 대체편성 상품 설정");
			paFapleBroadcastService.refreshBroadReplaceGoodsTx(paramMap);
			
			// 연동 대상 조회
			log.info("02. 패션플러스 방송편성표 대상 조회");
			List<Pabroadsche> broadInfoList = paFapleBroadcastService.selectBroadcastScheList(paramMap);
			
			if (broadInfoList.size() > 0) {
				
					int idx = -1;
					// 대체편성상품 세팅
					log.info("03. 패션플러스 대체편성상품 처리");
					alternativeInfo = paFapleBroadcastService.selectAlternativeBroadcastLink(paramMap.getString("paGroupCode"));
					
					int idxsize = alternativeInfo.size();
					for (int j = 0; j < alternativeInfo.size(); j++) {
						if ("1".equals(alternativeInfo.get(j).getTargetYn())) {
							idx = j;
							break;
						}
					}
					if (idx < 0) {
						idx = 0;
					}
				
				for (Pabroadsche broadInfo : broadInfoList) {
					
					if ("D".equals(broadInfo.getModifyFlag())) {
						broadInfo.setProgramUseYn("0");
						broadInfo.setGoodsUseYn("0");
					} else {
						broadInfo.setProgramUseYn("1");
						broadInfo.setGoodsUseYn("1");
					}
	
					// 대채편성상품 처리
					if ("".equals(broadInfo.getPaGoodsCode()) || broadInfo.getPaGoodsCode() == "" || broadInfo.getPaGoodsCode() == null) {
						broadInfo.setBroadcastProgramType("Alternative");
						broadInfo.setPaGoodsCode(alternativeInfo.get(idx % idxsize).getPaGoodsCode());
						broadInfo.setProgName	(alternativeInfo.get(idx % idxsize).getGoodsName());
						broadInfo.setShManName	(alternativeInfo.get(idx % idxsize).getShManName());
						broadInfo.setGoodsCode	(alternativeInfo.get(idx % idxsize).getGoodsCode());
						broadInfo.setGoodsName	(alternativeInfo.get(idx % idxsize).getGoodsName());
						broadInfo.setGoodsVodUrl(alternativeInfo.get(idx % idxsize).getVodUrl());
						broadInfo.setVideoImage	(alternativeInfo.get(idx % idxsize).getVodImage());	
						idx++;
					} else {
						broadInfo.setBroadcastProgramType("Live");
					}
					
					broadInfo.setProgramType("Broadcasting");
					broadInfo.setBroadcastProgramDetailType("AlwaysOnItem");
					broadInfo.setTransYn("1");
					broadInfo.setTransDate(dateTime);
					broadInfo.setInsertId("PAFAPLE");
	
					rtnMsg = paFapleBroadcastService.saveFapleBroadScheTx(broadInfo);
					
					if (!rtnMsg.equals("000000")) {
						log.info("[Error] 편성데이터 저장에 실패 하였습니다. : SEQ_FRAME_NO :" + broadInfo.getSeqFrameNo() + ", MSG : " + rtnMsg);
						continue;
					}
				}
				
				// 대체편성 TARGET UPDATE
				log.info("04. 패션플러스 대체편성 TARGET UPDATE");
				paFapleBroadcastService.updatePaBroadReplaceTx(alternativeInfo.get(idx % idxsize));		
			}
			
			// 패션플러스 방송편성표 RETURN
			log.info("05. 패션플러스 방송편성표 객체(programs) 반환");
			programs = paFapleBroadcastSetting(paFapleBroadcastService.selectFapleBroadcastScheList(paramMap));
			
			paramMap.put("code", "200");
			paramMap.put("message","SUCCESS. programs: " + programs);
			
		} catch (java.text.ParseException e) {
			paFapleConnectUtil.checkException(paramMap, e);		
			paramMap.put("code", "411");
			paramMap.put("message", "입력 날짜형식 오류 입니다.");
			log.error("ApiTracking Insert Error : " + e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.LENGTH_REQUIRED);
		} catch (Exception e) {
			paFapleConnectUtil.checkException(paramMap, e);
			paramMap.put("code", "500");
			paramMap.put("message","시스템 오류가 발생하였습니다. Message: " + e.getMessage());
			log.error("ApiTracking Insert Error : " + e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			paFapleConnectUtil.closeApi(request, paramMap);	
			log.info("=======  방송편성표 조회 API End - {} ======= ", broadScheDate);
		}
		
		return new ResponseEntity<>(programs, HttpStatus.OK);
		
	}
	
	
	// 방송편성표 객체(Programs) 세팅
	private Programs paFapleBroadcastSetting(List<?> rtnBroadcastSche) throws Exception {
		
		Programs programs =  new Programs();
		List<Program> programList = new ArrayList<Program>();
		
		for (int i = 0; i < rtnBroadcastSche.size(); i++) {
			
			Program program =  new Program();
			Item item = new Item();
			
			int priorityRank = 1;

			MultiframescheVO rtnVo = (MultiframescheVO) rtnBroadcastSche.get(i);
			
			if (i == 0) programs.setBroadDate(rtnVo.getBdDate());
			
			program.setProgCode(rtnVo.getSeqFrameNo());
			program.setBdBtime(rtnVo.getBdBtime());
			program.setBdEtime(rtnVo.getBdEtime());
			program.setBdImgUrl(rtnVo.getBannerImgUrl());
			program.setHostStaff(rtnVo.getHostSraff());
			
			// 공익방송 여부
			if (rtnVo.getPublicItemYn().equals("Y")) {
				program.setProgName("공익방송");
				item.setItemCode("0");
				item.setItemName("공익방송");
				item.setItemPrice(0);
			} else {
				program.setProgName(rtnVo.getProgName());
				item.setItemCode(rtnVo.getItemCode());
				item.setItemName(rtnVo.getItemName());
				item.setItemPrice(rtnVo.getItemPrice());
			}

			item.setMarketItemCode(rtnVo.getMakeItemCode());
			item.setPriority(priorityRank);
							
			item.setItemImgUrl(rtnVo.getItemImgUrl());
			item.setBannerImgUrl(rtnVo.getBannerImgUrl());
			item.setPublicItemYn(rtnVo.getPublicItemYn());
			
			item.setIntngItemYn(rtnVo.getIntngItemYn());
			item.setRepItemYn(rtnVo.getRepItemYn());
			
			program.setItem(item);			
			programList.add(program);			
		}
		
		programs.setProgram(programList);
		
		return programs;		
	}	
	
}
