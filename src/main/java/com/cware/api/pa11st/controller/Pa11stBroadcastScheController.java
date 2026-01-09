package com.cware.api.pa11st.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;
import com.cware.netshopping.pa11st.broadcast.service.Pa11stBroadcastService;
import com.cware.netshopping.pacommon.common.service.PaCommonService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pa11st/broadcast", description="방송/편성")
@Controller("com.cware.api.pa11st.Pa11stBroadcastScheController")
@RequestMapping(value="/pa11st/broadcast")
public class Pa11stBroadcastScheController extends AbstractController {
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService pacommonService;
	
	@Resource(name = "pa11st.broadcast.pa11stBroadcastService")
	private Pa11stBroadcastService pa11stBroadcastService;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	 /**
     * 편성스케줄조회
     * @param channelCode
     * @param broadScheDate
     * @throws Exception
     */
	//미사용. 아래 새로 만든걸로 사용 2022.05.24 by jchoi
    @RequestMapping(value = "/broadcastsche-list_old", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView broadCastScheListOld(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam(value="openApiCode", required = true) String openApiCode,
	    @RequestParam(value="bdDate", 	  required = true) String bdDate
	    ) {
		
		Document rtnDoc = new Document();
		Element error = new Element("error");
		Element resultMessage = new Element("resultMessage");
		Element resultCode = new Element("resultCode");
		
		String rtnXml = "";
		ModelAndView mav = new ModelAndView();
		String forwardPage = "api/pa11st/returnToXML";
		ParamMap paramMap = new ParamMap();
		
		paramMap.put("paCode", "11ST");
		paramMap.put("openApiCode", openApiCode);
		paramMap.put("bdDate", 	    bdDate);
		
		try{
			String rtnMsg = pacommonService.selectCheckOpenApiCode(paramMap);
			
			if(!rtnMsg.equals("Y")){
				resultCode.addContent("410");
				resultMessage.addContent("인증코드가 일치하지 않습니다.");
				error.addContent(resultMessage);
				error.addContent(resultCode);
				rtnDoc.setContent(error);
				return mav;		
		    }
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd",java.util.Locale.KOREA);
			formatter.setLenient(false);
			try{
				formatter.parse(bdDate);
			}catch(java.text.ParseException e){
				resultCode.addContent("411");
				resultMessage.addContent("입력 날짜형식 오류 입니다.");
				error.addContent(resultMessage);
				error.addContent(resultCode);
				rtnDoc.setContent(error);
				return mav;
			}catch(Exception e){
				resultCode.addContent("500");
				resultMessage.addContent("시스템 오류입니다. : "+e.getMessage());
				error.addContent(resultMessage);
				error.addContent(resultCode);
				rtnDoc.setContent(error);
				return mav;
			}
			
			rtnDoc = pa11stBroadcasXmlSetting(pa11stBroadcastService.selectBroadcastScheListOld(paramMap));
			
		}catch(Exception e){
			resultCode.addContent("500");
			resultMessage.addContent("시스템 오류입니다. : "+e.getMessage());
			error.addContent(resultMessage);
			error.addContent(resultCode);
			rtnDoc.setContent(error);
		    return mav;
		}finally{
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8"));
			rtnXml = outputter.outputString(rtnDoc);
            mav.addObject("rtnXml", rtnXml.replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
            mav.setViewName(forwardPage);
	        response.setContentType("text/xml;charset=UTF-8");
		}
		return mav;
	}

	private Document pa11stBroadcasXmlSetting(List<?> rtnBroadcastSche) throws Exception{
	    Document doc = new Document();
	    
	    Element resultMessage = new Element("resultMessage");
		Element resultCode = new Element("resultCode");

	    Element programs 	 = new Element("programs");
	    Element program 	 = new Element("program");
	    Element progName 	 = new Element("progName");
	    Element bdImgUrl 	 = new Element("bdImgUrl");
	    Element hostStaff 	 = new Element("hostStaff");

	    Element items 		 = new Element("items");
	    Element item 		 = new Element("item");
	    Element priority	 = new Element("priority");
	    Element itemName 	 = new Element("itemName");
	    Element itemImgUrl 	 = new Element("itemImgUrl");
	    Element bannerImgUrl = new Element("bannerImgUrl");
	    Element itemPrice 	 = new Element("itemPrice");
	    Element publicItemYn = new Element("publicItemYn");
	    Element intngItemYn  = new Element("intngItemYn");
	    Element repItemYn 	 = new Element("repItemYn");
	    
	    if(rtnBroadcastSche.size()> 0){

		    resultCode.addContent("200");
		    resultMessage.addContent("성공");
	
			for(int i = 0; i < rtnBroadcastSche.size(); i++){
				
				int priorityRank = 1;
	
			    MultiframescheVO rtnVo = (MultiframescheVO)rtnBroadcastSche.get(i);	
			    
		    	program = new Element("program");
		    	
			    if(i==0){
			    	programs.setAttribute("bdDate",rtnVo.getBdDate());
			    }
	
				program.setAttribute("progCode",	rtnVo.getSeqFrameNo());
				program.setAttribute("bdBtime",		rtnVo.getBdBtime());
				program.setAttribute("bdEtime",		rtnVo.getBdEtime());
	
				progName = new Element("progName");
				if(rtnVo.getPublicItemYn().equals("Y")){
				    progName.addContent("공익방송");
				}else{
				    progName.addContent("<![CDATA["+rtnVo.getProgName()+"]]>");
				}
				program.addContent(progName);
	
				bdImgUrl = new Element("bdImgUrl");
				bdImgUrl.addContent("<![CDATA["+rtnVo.getBannerImgUrl()+"]]>");
				program.addContent(bdImgUrl);
	
				hostStaff = new Element("hostStaff");
				hostStaff.addContent(rtnVo.getHostSraff());
				program.addContent(hostStaff);
				
				items = new Element("items");
	
			    item = new Element("item");
			    if(rtnVo.getPublicItemYn().equals("Y")){
			    	item.setAttribute("itemCode","0");
			    }else{
			    	item.setAttribute("itemCode",rtnVo.getItemCode());
			    }
			    item.setAttribute("marketItemCode",rtnVo.getMakeItemCode());
	
			    priority = new Element("priority");
			    priority.addContent(ComUtil.objToStr(priorityRank));
			    item.addContent(priority);
	
			    itemName = new Element("itemName");
			    if(rtnVo.getPublicItemYn().equals("Y")){
			    	itemName.addContent("공익방송");
			    }else{
			    	itemName.addContent("<![CDATA["+rtnVo.getItemName()+"]]>");
			    }
			    item.addContent(itemName);
	
			    itemImgUrl = new Element("itemImgUrl");
			    if(rtnVo.getPublicItemYn().equals("Y")){
			    	itemImgUrl.addContent("<![CDATA["+rtnVo.getItemImgUrl()+"]]>");
			    }else{
			    	itemImgUrl.addContent("<![CDATA["+rtnVo.getItemImgUrl()+"]]>");
			    }
			    item.addContent(itemImgUrl);
	
			    bannerImgUrl = new Element("bannerImgUrl");
			    bannerImgUrl.addContent("<![CDATA["+rtnVo.getBannerImgUrl()+"]]>");
			    item.addContent(bannerImgUrl);
	
			    itemPrice = new Element("itemPrice");
			    if(rtnVo.getPublicItemYn().equals("Y")){
			    	itemPrice.addContent("0");
			    }else{
			    	itemPrice.addContent(ComUtil.objToStr(rtnVo.getItemPrice()));
			    }
			    item.addContent(itemPrice);
	
			    publicItemYn = new Element("publicItemYn");
			    publicItemYn.addContent(rtnVo.getPublicItemYn());
			    item.addContent(publicItemYn);
	
			    intngItemYn = new Element("intngItemYn");
			    intngItemYn.addContent(rtnVo.getIntngItemYn());
			    item.addContent(intngItemYn);
	
			    repItemYn = new Element("repItemYn");
			    repItemYn.addContent(rtnVo.getRepItemYn());
			    item.addContent(repItemYn);
	
			    items.addContent(item);
			    //추가 상품 연동 필요하면 추가 개발 필요
			    
			    program.addContent(items);
			    programs.addContent(program);
			}
	    }else{
	    	resultCode.addContent("201");
		    resultMessage.addContent("조회된 데이터가 없습니다.");
	    }
	    
	    programs.addContent(resultMessage);
	    programs.addContent(resultCode);

	    doc.setContent(programs);

	    return doc;
	}
	
	@ApiOperation(value = "편성스케줄조회", notes = "편성스케줄조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
    @RequestMapping(value = "/broadcastsche-list", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView broadCastScheList(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam(value="openApiCode", required = true) String openApiCode,
	    @RequestParam(value="bdDate", 	  required = true) String bdDate
	    ) {
		
		Document rtnDoc = new Document();
		Element error = new Element("error");
		Element resultMessage = new Element("resultMessage");
		Element resultCode = new Element("resultCode");
		
		String rtnXml = "";
		ModelAndView mav = new ModelAndView();
		String forwardPage = "api/pa11st/returnToXML";
		ParamMap paramMap = new ParamMap();
		
		paramMap.put("paCode", "11ST");
		paramMap.put("openApiCode", openApiCode);
		paramMap.put("bdDate", 	    bdDate);
		paramMap.put("paGroupCode", "01");
		
		List<Pabroadreplace> alternativeInfo = null;
		
		try{
			String dateTime = systemService.getSysdatetimeToString();
			String rtnMsg = pacommonService.selectCheckOpenApiCode(paramMap);
			
			if(!rtnMsg.equals("Y")){
				resultCode.addContent("410");
				resultMessage.addContent("인증코드가 일치하지 않습니다.");
				error.addContent(resultMessage);
				error.addContent(resultCode);
				rtnDoc.setContent(error);
				return mav;		
		    }
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd",java.util.Locale.KOREA);
			formatter.setLenient(false);
			try{
				formatter.parse(bdDate);
			}catch(java.text.ParseException e){
				resultCode.addContent("411");
				resultMessage.addContent("입력 날짜형식 오류 입니다.");
				error.addContent(resultMessage);
				error.addContent(resultCode);
				rtnDoc.setContent(error);
				return mav;
			}catch(Exception e){
				resultCode.addContent("500");
				resultMessage.addContent("시스템 오류입니다. : "+e.getMessage());
				error.addContent(resultMessage);
				error.addContent(resultCode);
				rtnDoc.setContent(error);
				return mav;
			}			
			
			//대체편성 상품 설정. 매주 일요일마다 7일간 제일 많이 팔린 상품을 대체편성 상품으로 설정한다.
		    log.info("대체편성 상품 설정");
		    pa11stBroadcastService.refreshBroadReplaceGoodsTx(paramMap);
			
		    log.info("11번가 홈쇼핑 방송편성표 대상 조회");
		    //연동 대상 조회
		    List<Pabroadsche> broadInfoList = pa11stBroadcastService.selectBroadcastScheList(paramMap);
			
		    if(broadInfoList.size() > 0) {
		    	int idx = -1;			    	
		    	//대체편성상품 세팅
		    	alternativeInfo = pa11stBroadcastService.selectAlternativeBroadcastLink(paramMap.getString("paGroupCode"));
		    	int idxsize = alternativeInfo.size();
		    	for(int j=0; j<alternativeInfo.size(); j++) {
		    		if("1".equals(alternativeInfo.get(j).getTargetYn())) {
		    			idx = j;
		    			break;
		    		}
		    	}
		    	if(idx < 0) {
		    		idx = 0;
		    	}
		    	
		    	for(Pabroadsche broadInfo : broadInfoList){
			    	
		    		if("D".equals(broadInfo.getModifyFlag())){
		    			broadInfo.setProgramUseYn("0");
						broadInfo.setGoodsUseYn("0");
		    		} else {
		    			broadInfo.setProgramUseYn("1");
						broadInfo.setGoodsUseYn("1");
		    		}
		    		
		    		//대채편성상품 처리
		    		if("".equals(broadInfo.getPaGoodsCode()) || broadInfo.getPaGoodsCode() == "" || broadInfo.getPaGoodsCode() == null) {
		    			broadInfo.setBroadcastProgramType("Alternative");
		    			broadInfo.setPaGoodsCode(alternativeInfo.get(idx%idxsize).getPaGoodsCode());
		    			broadInfo.setProgName(alternativeInfo.get(idx%idxsize).getGoodsName());
		    			broadInfo.setShManName				(alternativeInfo.get(idx%idxsize).getShManName());
		    			broadInfo.setGoodsCode 				(alternativeInfo.get(idx%idxsize).getGoodsCode());
		    			broadInfo.setGoodsName				(alternativeInfo.get(idx%idxsize).getGoodsName());
		    			broadInfo.setGoodsVodUrl			(alternativeInfo.get(idx%idxsize).getVodUrl());
		    			broadInfo.setVideoImage				(alternativeInfo.get(idx%idxsize).getVodImage());

		    			idx++;
			    	}else{
			    		broadInfo.setBroadcastProgramType("Live");
			    	}
			    	
	    			broadInfo.setProgramType("Broadcasting");
		    		broadInfo.setBroadcastProgramDetailType("AlwaysOnItem");
		    		broadInfo.setTransYn("1");
					broadInfo.setTransDate(dateTime);
					broadInfo.setInsertId("PA11ST");
					
		    		rtnMsg = pa11stBroadcastService.save11stBroadScheTx(broadInfo);
    				if(!rtnMsg.equals("000000")){
    					log.info("[Error] 편성데이터 저장에 실패 하였습니다. : SEQ_FRAME_NO :"+broadInfo.getSeqFrameNo()+", MSG : "+rtnMsg);
    					continue;
    				}
			    }		    	
		    	
		    	//대체편성 TARGET UPDATE
		    	log.info("대체편성 TARGET UPDATE");
		    	pa11stBroadcastService.updatePaBroadReplaceTx(alternativeInfo.get(idx%idxsize));
		    	
		    }
			
			rtnDoc = pa11stBroadcasXmlSetting(pa11stBroadcastService.select11stBroadcastScheList(paramMap));
			
		} catch(Exception e){
			resultCode.addContent("500");
			resultMessage.addContent("시스템 오류입니다. : "+e.getMessage());
			error.addContent(resultMessage);
			error.addContent(resultCode);
			rtnDoc.setContent(error);
		    return mav;
		}finally{
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8"));
			rtnXml = outputter.outputString(rtnDoc);
            mav.addObject("rtnXml", rtnXml.replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
            mav.setViewName(forwardPage);
	        response.setContentType("text/xml;charset=UTF-8");
		}
		return mav;
	}
}
