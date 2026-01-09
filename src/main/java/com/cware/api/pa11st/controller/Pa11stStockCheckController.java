package com.cware.api.pa11st.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cware.framework.core.basic.AbstractController;
import com.cware.netshopping.domain.StockCheckVO;
import com.cware.netshopping.pa11st.stockcheck.service.Pa11stStockCheckService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pa11st/stock", description="실시간 재고 체크")
@Controller("com.cware.api.pa11st.Pa11stStockCheckController")
@RequestMapping(value="/pa11st/stock")
public class Pa11stStockCheckController extends AbstractController {

    @Resource(name = "pa11st.stock.pa11stStockCheckService")
    private Pa11stStockCheckService pa11stStockCheckService;


    /**
     * 11번가 실시간 재고 체크 
     * @return Map
     * @throws Exception
     */

	@ApiOperation(value = "11번가 실시간 재고 체크 ", notes = "11번가 실시간 재고 체크", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
    @RequestMapping(value = "/stock-check", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView stockCheck(HttpServletRequest request, HttpServletResponse response) {

	Element code = new Element("code");
	Element message = new Element("message");

	String rtnXml = "";
	ModelAndView mav = new ModelAndView();
	String forwardPage = "api/pa11st/returnToXML";	

	Document rtnDoc = new Document();
	Element root;
	List<HashMap<String, String>> stockList = new ArrayList<HashMap<String, String>>();

	String inOpenApiCode 		= "";
	String chkOpenApiCodeCode 	= "";
	String logInfo = "";
	BufferedReader in = null; 
	
	//log.info("01.실시간재고체크");
	try{
	    StringBuffer inputXmlString = new StringBuffer();
	    String  inputXmlString2 = "";
	    
	    in = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
	    
	    while ((inputXmlString2 = in.readLine())!= null) {
		inputXmlString.append(inputXmlString2);
	    } 
	    
	    byte [] byteStr = inputXmlString.toString().getBytes("UTF-8");
	    String enCodeString = new String(byteStr,"UTF-8");
	    
	    //log.info("01.실시간재고체크 RAW-DATA -> " + enCodeString);    
	    Document doc = new SAXBuilder().build(new StringReader(enCodeString));
	    
	    inOpenApiCode = request.getHeader("openApiCode");
	    root = doc.getRootElement();
	    List<?> l_list = root.getChildren();

	    for(int j = 0; j <= l_list.size()-1; j++){
		Element e_list = (Element)l_list.get(j);
		List<?> l_basic = e_list.getChildren();
		HashMap<String, String> stockMap = new HashMap<String, String>();	    
		for(int i = 0; i<l_basic.size();i++){
		    Element e_basic = (Element) l_basic.get(i);		    
		    if(e_basic.getName().trim().equals("prdNo") ||e_basic.getName().trim().equals("stockNo")||e_basic.getName().trim().equals("ordQty")){
			stockMap.put(e_basic.getName().trim(), e_basic.getText().trim());
			logInfo += e_basic.getName().trim() +" : " +e_basic.getText().trim() + "/";
		    }		   
		}		
		stockList.add(stockMap);
	    }
	    //log.info("02.수신받은 데이터 -> "+ logInfo);
	    chkOpenApiCodeCode = pa11stStockCheckService.selectCheckOpenApiCode();
	    
	    if(chkOpenApiCodeCode.equals(inOpenApiCode)){
		rtnDoc = pa11stStockCheckXmlSetting(pa11stStockCheckService.selectStockCheck(stockList));
		log.info("실시간재고체크 성공 "+logInfo);
	    }else{
		rtnDoc = new Document();

		Element error = new Element("error");
		Element resultCode = new Element("resultCode");
		Element resultMessage = new Element("resultMessage");

		code.addContent("411");	    
		resultCode.addContent("411");

		resultMessage.addContent("인증코드가일치하지않습니다.");	    
		message.addContent("인증코드가일치하지않습니다.");


		error = new Element("error");
		error.addContent(resultMessage);
		error.addContent(resultCode);

		rtnDoc.setContent(error);
		log.info("실시간재고체크 실패");
	    }

	}catch(Exception e){
	    rtnDoc = new Document();

	    Element error = new Element("error");
	    Element resultCode = new Element("resultCode");
	    Element resultMessage = new Element("resultMessage");
	    log.info("실시간재고체크 실패");
	    code.addContent("500");	    
	    resultCode.addContent("500");
	    resultMessage.addContent("시스템 오류입니다.");	    
	    message.addContent("시스템 오류입니다.");

	    error = new Element("error");
	    error.addContent(resultMessage);
	    error.addContent(resultCode);

	    rtnDoc.setContent(error);
	}finally{
	    XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8"));
	    rtnXml = outputter.outputString(rtnDoc);
	    mav.addObject("rtnXml", rtnXml);
	    mav.setViewName(forwardPage);
	    response.setContentType("text/xml;charset=UTF-8");
	    try {
		in.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    
	}
	return mav;
    }

    private Document pa11stStockCheckXmlSetting(List<StockCheckVO> rtnStockCheck) throws Exception{

	Document doc = new Document();    	

	Element code = new Element("code");
	Element message = new Element("message");
	Element resultCode = new Element("resultCode");
	Element resultMessage = new Element("resultMessage");	
	Element products = new Element("products");
	Element product = new Element("product");
	Element prdNo = new Element("prdNo");
	Element stockNo = new Element("stockNo");
	Element addPrc = new Element("addPrc");
	Element stockStat = new Element("stockStat");
	
	if(rtnStockCheck.size()> 0){

	    code.addContent("200");
	    resultCode.addContent("200");
	    message.addContent("실시간 재고체크 성공");
	    resultMessage.addContent("실시간 재고체크 성공");	    

	    products = new Element("products");

	    for(int i = 0; i < rtnStockCheck.size(); i++){

		StockCheckVO rtnVo = (StockCheckVO)rtnStockCheck.get(i);

		product = new Element("product");
		addPrc = new Element("addPrc");
		prdNo = new Element("prdNo");
		stockNo = new Element("stockNo");
		stockStat = new Element("stockStat");

		prdNo.addContent(rtnVo.getPrdNo());
		stockNo.addContent(rtnVo.getStockNo());
		addPrc.addContent(rtnVo.getAddPrc());
		stockStat.addContent(rtnVo.getStockStat());
		//log.info("02.실시간재고체크 상품 : "+ rtnVo.getPrdNo());
		product.addContent(prdNo);
		product.addContent(stockNo);
		product.addContent(addPrc);
		product.addContent(stockStat);

		products.addContent(product);

	    }

	}else{
	    code.addContent("201");
	    resultCode.addContent("201");
	    message.addContent("조회된 데이터가 없습니다.");
	    resultMessage.addContent("조회된 데이터가 없습니다.");
	}

	products.addContent(resultMessage);
	products.addContent(resultCode);

	doc.setContent(products);
	return doc;
    }
    
    private String convertStreamToString(InputStream stream) throws IOException {
        // To convert the InputStream to String we use the
        // Reader.read(char[] buffer) method. We iterate until the
        // Reader return -1 which means there's no more data to
        // read. We use the StringWriter class to produce the string.
        if (stream != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                stream.close();
            }
            return writer.toString();
        }
        return "";
    }
}