package com.cimb.xml.gen;

import java.io.FileOutputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

public class GenerateRequestXml {
	public static void main(String[] args) {

	    try{

	        MessageFactory factory = MessageFactory.newInstance();
	        SOAPMessage soapMsg = factory.createMessage();
	        SOAPPart part = soapMsg.getSOAPPart();

	        SOAPEnvelope envelope = part.getEnvelope();
	        SOAPHeader header = envelope.getHeader();
	        SOAPBody body = envelope.getBody();



	        header.addTextNode("Training Details");
	        
	        SOAPBodyElement element = body.addBodyElement(envelope.createName("CIMB_UpdateCustomerInfoSvc", "urn", ""));
	        	SOAPElement childElement1 = element.addChildElement(envelope.createName("CIMB_SignonRq"));
	        	        childElement1.addChildElement("ClientDt").addTextNode("2016-03-28T07:53:22.000282");
	        	        childElement1.addChildElement("CustLangPref").addTextNode("ENG");
	        	        SOAPElement subElement1 =childElement1.addChildElement(envelope.createName("ClientApp"));
	        	        subElement1.addChildElement("Org").addTextNode("CIMB_MY");
	        			subElement1.addChildElement("Name").addTextNode("CLMS_MY");
	        			subElement1.addChildElement("Version").addTextNode("1.0");
	        	        
	        	        
//	        	SOAPElement childElement2 = element.addChildElement(envelope.createName("CIMB_SignonRq"));        
//	        			childElement2.addChildElement("Org").addTextNode("CIMB_MY");
//	        			childElement2.addChildElement("Name").addTextNode("CLMS_MY");
//	        			childElement2.addChildElement("Version").addTextNode("1.0");
	        			
	        soapMsg.writeTo(System.out);

	        FileOutputStream fOut = new FileOutputStream("SoapMessage.xml");
	        soapMsg.writeTo(fOut);

	        System.out.println();
	        System.out.println("SOAP msg created");



	    }catch(Exception e){

	        e.printStackTrace();

	    }



	   }
}
