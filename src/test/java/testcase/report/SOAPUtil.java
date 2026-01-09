package testcase.report;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

public class SOAPUtil {
    
    public static SOAPMessage callSoapWebService(SOAPMessage requestMassge, String soapEndpointUrl) throws Exception {

	SOAPMessage responseMessage = null;
	SOAPConnectionFactory soapConnectionFactory = null;
	SOAPConnection soapConnection = null;
	
	try {
	    soapConnectionFactory = SOAPConnectionFactory.newInstance();
	    soapConnection =  soapConnectionFactory.createConnection();
	    
	    //인터페이스 CALL
	    responseMessage = soapConnection.call(requestMassge, soapEndpointUrl);

	}catch (Exception e) {
	    // TODO: Exception 처리 
	    System.out.println(e.getMessage());
	}finally  {
	    if(soapConnection != null){
		soapConnection.close();
	    }
	}
	
	return responseMessage;
    }
    
    public static SOAPMessage createGmrkSOAPRequest(String soapAction) throws Exception {
	
	MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        
        MimeHeaders headers = soapMessage.getMimeHeaders();
        
        headers.setHeader("SOAPAction", soapAction);
        headers.setHeader("Content-Type", "text/xml; charset=utf-8");
        
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
        //envelope.addNamespaceDeclaration("soap", "http://schemas.xmlsoap.org/soap/envelope/");
        
        // SOAP Header
	SOAPHeader soapHeader = soapMessage.getSOAPHeader();
	QName qname = new QName("http://tpl.gmarket.co.kr/", "EncTicket");
	SOAPHeaderElement headerElement = soapHeader.addHeaderElement(qname);
	
	SOAPElement encTicket = headerElement.addChildElement("encTicket");
	// 신세계TV쇼핑 방송용ID(운영)
	encTicket.setTextContent("42BBCE257B9B729364EEAFD70DF3E961C05B07961D44E20CE32DD7CBF72FB5A38139F6ED7B3C9627F1DC82FF95C72970F9EFE1FB6AE0AB46F18E1FB7E3F5018C69FB626EB0E8C04CB022FA482ABF97AC131C6AD094C9548385BE3E6237FAC811DEEDCE57F25E97336C48003935FE8CFC");
	// 신세계TV쇼핑 온라인용ID(운영)
	//encTicket.setTextContent("");
	
	// 오승열 파트너 개인ID(개발용)
	//encTicket.setTextContent("EBA8C27F3718B40AE698F2C5F2C356F8142DEDFC3DFBAE18266B3547BF7BD386E978088A3C7FB007C852DA4325B329D1D4BE02724716B0C9FD8F6F9459479D7CD4814C484F53C732D7A510DBFA081BFA4A33025B9DCE70802E656EE8F42F08FE");
	
	//SOAP Body
	
	return soapMessage;
    }
    
}
