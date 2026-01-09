package com.cware.netshopping.pagmkt.util;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import com.cware.netshopping.common.util.ConfigUtil;

public class PaGmktSOAPMessageFactory {

	/**
	 * G마켓 REQUEST MESSAGE 생성
	 * @param soapAction
	 * @param apiKey
	 * @return SOAPMessage
	 * @throws Exception
	 */
	public static SOAPMessage createGmktSOAPRequest(String soapAction, String apiKey) throws Exception {
    	MessageFactory 	messageFactory 	= MessageFactory.newInstance();
        SOAPMessage 	soapMessage 	= messageFactory.createMessage();
        SOAPPart 		soapPart 		= soapMessage.getSOAPPart();

        MimeHeaders 	headers 		= soapMessage.getMimeHeaders();

        headers.setHeader("SOAPAction", soapAction);
        headers.setHeader("Content-Type", "text/xml; charset=utf-8");

        // SOAP Envelope
        SOAPEnvelope 	envelope 		= soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
        envelope.addNamespaceDeclaration("soap", "http://schemas.xmlsoap.org/soap/envelope/");

	     // SOAP Header
		SOAPHeader 		soapHeader 		= soapMessage.getSOAPHeader();
		QName qname = new QName(ConfigUtil.getString("PAGMKT_COM_BASE_URL"), "EncTicket");
		SOAPHeaderElement headerElement = soapHeader.addHeaderElement(qname);

		SOAPElement encTicket = headerElement.addChildElement("encTicket");
		encTicket.setTextContent(apiKey);

		//SOAP Body

		return soapMessage;
	}

}
