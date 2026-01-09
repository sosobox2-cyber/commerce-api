package com.cware.api.pagmktv2.controller;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jdom.DefaultJDOMFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMFactory;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.PaGmktGoodsdtMappingVO;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pagmkt.stockcheck.service.PaGmktStockCheckService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pagmktv2/stock", description="재고 체크")
@Controller("com.cware.api.pagmktv2.PaGmktStockCheckController")
@RequestMapping(value="/pagmktv2/stock")
public class PaGmktStockCheckController extends AbstractController {

        @Resource(name = "pagmkt.stockcheck.PaGmktStockCheckService")
    	private PaGmktStockCheckService paGmktStockCheckService;
    
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService pacommonService;
    
	@ApiOperation(value = "재고 체크", notes = "재고 체크", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/stock-check", method = RequestMethod.POST, produces = MediaType.TEXT_XML_VALUE)
	@ResponseBody
	public ModelAndView stockCheck(HttpServletRequest request, HttpServletResponse response) throws Exception{
		SAXBuilder builder;
		Element root;
		
		Document doc = new Document();	
		Document rtnDoc = new Document();
		ModelAndView mav = new ModelAndView();
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8"));
		List<PaGmktGoodsdtMappingVO> goodsDtInfoList = new ArrayList<PaGmktGoodsdtMappingVO>();
		ParamMap paramMap = new ParamMap();
		
		PaGmktGoodsdtMappingVO goodsDtStockInfo = null;
		
		String code 		= "";
		String message 		= "";
		
		response.setContentType("text/xml;charset=UTF-8");
		
		try{
		    
		    paramMap.put("apiCode", "IF_PAGMKTAPI_V2_03_020");
		    paramMap.put("startDate", systemService.getSysdatetimeToString());
		    
		    paramMap.put("paCode", 		"GMKT");
		    
		    //TODO IP추적. 추후제거
		    String ip = null;

		    if (request.getHeader("X-Forwarded-For") != null) {
		        String xForwardedFor = request.getHeader("X-Forwarded-For");
		        if (xForwardedFor.indexOf(",") != -1) {
		            ip = xForwardedFor.substring(xForwardedFor.lastIndexOf(",") + 2);
		        } else {
		            ip = xForwardedFor;
		        }
		    } else {
		        ip = request.getRemoteAddr();
		    }

		    log.info("Request from IP : "+ip);
		    //G마켓요청으로 보안키 체크 안함 sk스토아 내부협의 완료20181213 제거
//		    paramMap.put("openApiCode",	request.getHeader("openApiCode"));
//		    
//		    String rtnMsg = pacommonService.selectCheckOpenApiCode(paramMap);
//			
//		    if(Constants.PA_GMKT_Y.equals(rtnMsg)){
				builder = new SAXBuilder();
				doc = builder.build(new InputStreamReader(request.getInputStream(), "UTF-8"));
				root = doc.getRootElement();
				
				//log.info("CharacterEncoding : " + request.getCharacterEncoding());
				//log.info("request xml : " + outputter.outputString(doc));
				
				Element product = root.getChild("PRODUCT");
				
				
				if("02".equals(product.getAttributeValue("EXT_GB"))){
					// 옥션인 경우 true로 들어옴
					paramMap.put("siteGb", "PAA");
					paramMap.put("goodsCode", 	product.getAttributeValue("NO")); //제휴사 상품번호
					paramMap.put("orderAmt", 	product.getAttributeValue("ORDER_QTY")); //상품 주문 수량
					paramMap.put("price", 		product.getAttributeValue("PRICE")); //상품 판매 가격
					paramMap.put("itemNo", 		product.getAttributeValue("GD_NO")); //G마켓상품번호
				}else{
					// 지마켓인 경우...
					paramMap.put("siteGb", "PAG");
					paramMap.put("goodsCode", 	product.getAttributeValue("NO")); //제휴사 상품번호
					paramMap.put("orderAmt", 	product.getAttributeValue("ORDER_AMT")); //상품 주문 수량
					paramMap.put("price", 		product.getAttributeValue("PRICE")); //상품 판매 가격
					paramMap.put("itemNo", 		product.getAttributeValue("EXT_GB")); //옥션 구분값
				}
				
				Element orderOption = root.getChild("ORDER_OPTION");
				
				if(orderOption != null){
				    Iterator<?> optionInfoList = orderOption.getChildren("OPTION_INFO").iterator();
				    
				    while(optionInfoList.hasNext()){
					PaGmktGoodsdtMappingVO goodsDtInfo = new PaGmktGoodsdtMappingVO();
					Element optionInfo = (Element) optionInfoList.next();
					//String price = optionInfo.getChild("PRICE").getValue(); //선택정보 추가 금액 int
					/*
					if(StringUtils.isNotBlank(paramMap.getString("itemNo"))){
					    goodsDtInfo.setItemNo(paramMap.getString("itemNo")); //G마켓 상품번호
					}else {
					    goodsDtInfo.setPaCode(Constants.PA_GMKT_BROAD_CODE);
					}
					*/
					//goodsDtInfo.setGoodsdtCode(stockNo);
					goodsDtInfo.setGoodsCode			(paramMap.getString("goodsCode"));
					goodsDtInfo.setPaOptionCode			(StringUtils.trim(optionInfo.getChild("STOCK_NO").getValue())); //옵션번호
					
					if(paramMap.get("siteGb").equals("PAG")){
						//지마켓인 경우
						if(StringUtils.isNumericSpace		(StringUtils.trim(optionInfo.getChild("AMT").getValue()))){
						    goodsDtInfo.setOrdQty			(Long.valueOf(StringUtils.trim(optionInfo.getChild("AMT").getValue()))); //선택정보 수량 int
						}else {
						    goodsDtInfo.setOrdQty(Long.MAX_VALUE); //선택정보 수량 int
						}
					}else{
						//옥션인 경우
						if(StringUtils.isNumericSpace		(StringUtils.trim(optionInfo.getChild("QTY").getValue()))){
						    goodsDtInfo.setOrdQty			(Long.valueOf(StringUtils.trim(optionInfo.getChild("QTY").getValue()))); //선택정보 수량 int
						}else {
						    goodsDtInfo.setOrdQty(Long.MAX_VALUE); //선택정보 수량 int
						}
					}
					
					
					goodsDtInfo.setGoodsdtInfoKind		(StringUtils.trim(optionInfo.getChild("NAME").getValue())); //선택정보명 String
					goodsDtInfo.setGoodsdtInfo			(StringUtils.trim(optionInfo.getChild("VALUE").getValue())); //선택정보값 String
					
					goodsDtStockInfo = paGmktStockCheckService.selectStockCheck(goodsDtInfo);
					
					if(goodsDtStockInfo == null){
					    goodsDtInfo.setTransOrderAbleQty("0");
					}else {
					    goodsDtInfo.setTransOrderAbleQty(goodsDtStockInfo.getTransOrderAbleQty());
					}
					goodsDtInfoList.add(goodsDtInfo);
					
					//log.info("OPTION_INFO VALUE : " + optionInfo.getChild("VALUE").getValue());
				    }
				}
				
				if("".equals(code)){
				    rtnDoc = createGmktStockCheckResponseXml(paramMap, goodsDtInfoList);
				    log.info("stock check success: goods_code: "+paramMap.getString("goodsCode")+", incoming: "+paramMap.getString("siteGb"));
				    code = "200";
				    message = "OK";
				}
//		    }else {
//				code = "411";
//				message = "인증코드가일치하지않습니다." + " (인증코드 : " + request.getHeader("openApiCode") + ")";
//				log.info("실시간재고체크 실패 :ESM_CODE: "+paramMap.getString("goodsCode")+", GMKT_CODE:"+paramMap.getString("itemNo"));
//		    }
		    
		    paramMap.put("code",code);
		    paramMap.put("message",message);
		    
		}catch(Exception e){
		    log.error("[PAGMKT STOCK CHECK ERROR]");
		    if(doc.hasRootElement()){
			log.error("error xml : " + outputter.outputString(doc), e);
		    }
		    
		    paramMap.put("code","500");
		    paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    
		} finally{
		    try{
			if("".equals(code)){
			    paramMap.put("code","500");
			}

			paramMap.put("resultMessage", paramMap.getString("message"));
			systemService.insertApiTrackingTx(request,paramMap);
		    }catch(Exception e){
			log.error("ApiTracking Insert Error : "+e.getMessage());
		    }

		    if(!rtnDoc.hasRootElement()){
			rtnDoc = createGmktStockCheckResponseXml(paramMap, goodsDtInfoList);
		    }
		    
		    outputter.outputString(doc);
		    //log.info("response xml : " + outputter.outputString(doc));
		    
		    mav.addObject("rtnXml", outputter.outputString(rtnDoc));
		    mav.setViewName("api/pagmkt/returnToXML");
		}
		
		return mav;
	}
	
	private Document createGmktStockCheckResponseXml(ParamMap paramMap, List<PaGmktGoodsdtMappingVO> goodsDtStockInfoList) throws Exception {
	    JDOMFactory factory = new DefaultJDOMFactory();
	    String remainYn = goodsDtStockInfoList.isEmpty()?"N":"Y";
	    
	    Element stockRemainInfo = factory.element("STOCK_REMAIN_INFO");
	    Element product = factory.element("PRODUCT");
	    Element orderOption = factory.element("ORDER_OPTION");
	    
	    stockRemainInfo.addContent(product);
	    stockRemainInfo.addContent(orderOption);

	    for (PaGmktGoodsdtMappingVO goodsDtInfo : goodsDtStockInfoList){
			if(Long.valueOf(goodsDtInfo.getTransOrderAbleQty()) < goodsDtInfo.getOrdQty()){
			    remainYn = "N";
			}
	    }
	    if(paramMap.get("siteGb").equals("PAG")){
	    	//지마켓인 경우..
	    	product.setAttribute("NO", paramMap.getString("goodsCode"));
		    product.setAttribute("REMAIN_YN", remainYn);
		    
		    for (PaGmktGoodsdtMappingVO goodsDtInfo : goodsDtStockInfoList){
				Element optionInfo = factory.element("OPTION_INFO");
				orderOption.addContent(optionInfo);
				optionInfo.addContent(factory.element("NAME").addContent(factory.cdata(goodsDtInfo.getGoodsdtInfoKind())));
				optionInfo.addContent(factory.element("VALUE").addContent(factory.cdata(goodsDtInfo.getGoodsdtInfo())));
				optionInfo.addContent(factory.element("REMAIN_YN").addContent(remainYn));
				optionInfo.addContent(factory.element("GOODS_QTY").addContent(goodsDtInfo.getTransOrderAbleQty()));
				optionInfo.addContent(factory.element("STOCK_NO").addContent(goodsDtInfo.getPaOptionCode()));
		    }
	    }else{
	    	//옥션인 경우..
	    	product.setAttribute("NO", paramMap.getString("goodsCode"));
		    product.setAttribute("REMAIN_YN", remainYn);
		    product.setAttribute("PRICE_YN", "Y");
		    product.setAttribute("GOODS_QTY", goodsDtStockInfoList.get(0).getTransOrderAbleQty());
		    
		    for (PaGmktGoodsdtMappingVO goodsDtInfo : goodsDtStockInfoList){
				Element optionInfo = factory.element("OPTION_INFO");
				orderOption.addContent(optionInfo);
				optionInfo.addContent(factory.element("NAME").addContent(factory.cdata(goodsDtInfo.getGoodsdtInfoKind())));
				optionInfo.addContent(factory.element("VALUE").addContent(factory.cdata(goodsDtInfo.getGoodsdtInfo())));
				optionInfo.addContent(factory.element("REMAIN_YN").addContent(remainYn));
				optionInfo.addContent(factory.element("PRICE_YN").addContent("Y"));
				optionInfo.addContent(factory.element("GOODS_QTY").addContent(goodsDtInfo.getTransOrderAbleQty()));
				optionInfo.addContent(factory.element("STOCK_NO").addContent(goodsDtInfo.getPaOptionCode()));
		    }
	    }
	    
	    
	    return factory.document(stockRemainInfo);
	}
    
}
