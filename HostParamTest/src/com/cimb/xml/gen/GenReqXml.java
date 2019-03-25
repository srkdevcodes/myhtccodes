package com.cimb.xml.gen;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GenReqXml  extends GenXmlJDBCImpl{
	 /**
     * Starting point for the SAAJ - SOAP Client Testing
     */
	
	private static final String my = "MY";
	final static Logger logger = Logger.getLogger(GenReqXml.class);
	
	
	
	
	
	
    public static void main(String args[]) {
        try {
            // Create SOAP Connection
           // SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
           // SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
           // String url = "http://ws.cdyne.com/emailverify/Emailvernotestemail.asmx";
            /*URL rags = new URL("http://ragnarok.whoi.edu:8001/hpb/contactInfoUpdate.go");
            SOAPConnection con = soap.getConnection();
            SOAPMessage response = con.call(message, rags);*/
            GenReqXml saaj = new GenReqXml();
            saaj.createSOAPRequest();

            // Process the SOAP Response
           // saaj.printSOAPResponse(soapResponse);
            
            //readAndUpdateFromResponse();
            
            
            
            
           // soapConnection.close();
        } catch (Exception e) {
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
        }
    }

    private void createSOAPRequest() throws Exception {
    	
    	try {
           // SOAP Body
    	SeqGen sequnce = new SeqGen();
        List<TempModel> tempList = new ArrayList<TempModel>();
        tempList = getTempTableData();
        String rquid = "";
        if(!tempList.isEmpty()){
        	logger.info("tempList size"+ tempList.size());
        for (TempModel model : tempList) {
        	MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();	
            
            String serverURI = "urn:ifxforum-org:XSD:1";

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("urn", serverURI);
		SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("CIMB_UpdateCustomerInfoSvc", "urn");
        SOAPElement CIMB_SignonRq = soapBodyElem.addChildElement("CIMB_SignonRq", "urn");
        SOAPElement ClientDt = CIMB_SignonRq.addChildElement("ClientDt", "urn");
        ClientDt.addTextNode("2016-03-28T07:53:22.000282");
        SOAPElement CustLangPref = CIMB_SignonRq.addChildElement("CustLangPref", "urn");
        CustLangPref.addTextNode("ENG");
        
        SOAPElement clientApp = CIMB_SignonRq.addChildElement("ClientApp", "urn");
        SOAPElement org = clientApp.addChildElement("Org", "urn");
        org.addTextNode("CIMB_MY");
        SOAPElement name = clientApp.addChildElement("Name", "urn");
        name.addTextNode("CIMB_MY");
        SOAPElement version = clientApp.addChildElement("Version", "urn");
        version.addTextNode("1.0");
        
        SOAPElement CIMB_HeaderRq = CIMB_SignonRq.addChildElement("CIMB_HeaderRq", "urn");
        SOAPElement CIMB_ConsumerId = CIMB_HeaderRq.addChildElement("CIMB_ConsumerId", "urn");
        CIMB_ConsumerId.addTextNode("CIMB_MY");
        SOAPElement CIMB_ConsumerPasswd = CIMB_HeaderRq.addChildElement("CIMB_ConsumerPasswd", "urn");
        CIMB_ConsumerPasswd.addTextNode("ABC123");
        SOAPElement CIMB_ServiceName = CIMB_HeaderRq.addChildElement("CIMB_ServiceName", "urn");
        CIMB_ServiceName.addTextNode("UpdateCustomerInfo");
        SOAPElement CIMB_ServiceVersion = CIMB_HeaderRq.addChildElement("CIMB_ServiceVersion", "urn");
        CIMB_ServiceVersion.addTextNode("1.0");
        
        SOAPElement CIMB_ProviderList = CIMB_HeaderRq.addChildElement("CIMB_ProviderList", "urn");
        SOAPElement CIMB_Provider = CIMB_ProviderList.addChildElement("CIMB_Provider", "urn");
        SOAPElement CIMB_ProviderSystemCode = CIMB_Provider.addChildElement("CIMB_ProviderSystemCode", "urn");
        CIMB_ProviderSystemCode.addTextNode("SIBS_MY");
        
        SOAPElement CIMB_SrcCountryCode = CIMB_HeaderRq.addChildElement("CIMB_SrcCountryCode", "urn");
        CIMB_SrcCountryCode.addTextNode("MY");
        
        SOAPElement CIMB_UpdateCustomerInfoRq = soapBodyElem.addChildElement("CIMB_UpdateCustomerInfoRq", "urn");
        SOAPElement CIMB_RqUID = CIMB_UpdateCustomerInfoRq.addChildElement("CIMB_RqUID", "urn");
        rquid = String.valueOf("MY"+sequnce.nextUniqueInt());
        CIMB_RqUID.addTextNode(rquid);
        
        SOAPElement CIMB_CustomerCreationInfo = CIMB_UpdateCustomerInfoRq.addChildElement("CIMB_CustomerCreationInfo", "urn");
        SOAPElement CIMB_CustomerName = CIMB_CustomerCreationInfo.addChildElement("CIMB_CustomerName", "urn");
        CIMB_CustomerName.addTextNode(model.getCustomerName());
        
        SOAPElement CIMB_TCustomerName = CIMB_CustomerCreationInfo.addChildElement("CIMB_TCustomerName", "urn");
        CIMB_TCustomerName.addTextNode(model.getTcusomterName());
        
        SOAPElement CIMB_CustomerNumber = CIMB_CustomerCreationInfo.addChildElement("CIMB_CustomerNumber", "urn");
        CIMB_CustomerNumber.addTextNode(model.getCustomerNumber());
        
        SOAPElement CIMB_SectorCode = CIMB_CustomerCreationInfo.addChildElement("CIMB_SectorCode", "urn");
        CIMB_SectorCode.addTextNode(model.getSectorCode());
        
        SOAPElement CIMB_BusinessType = CIMB_CustomerCreationInfo.addChildElement("CIMB_BusinessType", "urn");
        CIMB_BusinessType.addTextNode(model.getBusinessType());
        
        SOAPElement CIMB_CIFId = CIMB_CustomerCreationInfo.addChildElement("CIMB_CIFId", "urn");
        CIMB_CIFId.addTextNode(model.getCifId());
        
        
       
       String sopMessage =  SOAPUtil.toString(soapMessage);
       logger.info("message : " + sopMessage);
       upateTemptable(sopMessage,rquid, model.getCustomerNumber());
       
       SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
       SOAPConnection con = soapConnectionFactory.createConnection();
      try {
    	  URL rags = new URL("http://10.104.130.17:5001/IHProcess");
          SOAPMessage response = con.call(soapMessage, rags);
          String soapResponse =  SOAPUtil.toString(response);
          logger.info("Response : " + soapResponse);
	} catch (Exception e) {
		e.printStackTrace();
	}
       
       
       con.close();
        	}
        }
    	       
    	} catch (Exception e) {
			// TODO: handle exception
		}
	
    }

    /**
     * Method used to print the SOAP Response
     * @throws org.xml.sax.SAXException 
     * @throws SOAPException 
     */
   /* private void printSOAPResponse(SOAPMessage soapResponse, String customerNumber) throws Exception {
    	try {
			
    		logger.info("custNumber  "+customerNumber);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
       
        System.out.print("\nResponse Rajkiran = ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
        addRowToCoffeeDescriptions(transformer.toString(), customerNumber);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }*/
    
    /*private String getRqUIDSeq(){
    	SeqGen sequnce = new SeqGen();
    	StringBuilder sd = new StringBuilder();
    	sequnce.nextUniqueInt();
    	sd.append(my).append(sequnce.nextUniqueInt());
		return sd.toString();
    }*/
    
    public static SOAPElement stringToSOAPElement(String xmlText) throws org.xml.sax.SAXException {
        try {
            // Load the XML text into a DOM Document
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            InputStream stream  = new ByteArrayInputStream(xmlText.getBytes());
            Document doc = builderFactory.newDocumentBuilder().parse(stream); 
  
            // Use SAAJ to convert Document to SOAPElement
            // Create SoapMessage
            MessageFactory msgFactory = MessageFactory.newInstance();
            SOAPMessage    message    = msgFactory.createMessage();
            SOAPBody       soapBody   = message.getSOAPBody();
             
            // This returns the SOAPBodyElement 
            // that contains ONLY the Payload
            return soapBody.addDocument(doc);
           
        } catch (SOAPException  e) {
            System.out.println("SOAPException : " + e);
            return null;
  
        } catch (IOException  e) {
            System.out.println("IOException : " + e);
            return null;
  
        } catch (ParserConfigurationException  e) {
            System.out.println("ParserConfigurationException : " + e);
            return null;
  
        }
    }
    
    
    
    @SuppressWarnings({ "rawtypes", "unused" })
	private static void readAndUpdateFromResponse() throws SOAPException {
    	try {	
    		 File xmlFile = new File("D:\\ICRES\\Update_Customer_Info_Response.xml");
    	     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    	     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    	     Document document = documentBuilder.parse(xmlFile);
    	     
    	     document.getDocumentElement().normalize();
    	     System.out.println("Root element : "+document.getDocumentElement().getElementsByTagName("CIMB_AuthorizeRatingSvc"));
    	     
    	     
    	     NodeList list = document.getElementsByTagName("urn:CIMB_UpdateCustomerInfoSvc");
    	     System.out.println("The Node list is as folliws : - ");
    	     

    		for(int temp = 0; temp < list.getLength(); temp++){
    	    	 Node  node = list.item(temp);
    	    	 System.out.println("\nCurrentElement : "+node.getNodeName());
    	     
    		if(node.getNodeType() == Node.ELEMENT_NODE){
    			Element element = (Element) node;
    			
    			Node RqUID = element.getElementsByTagName("urn:CIMB_RqUID").item(0);
    			Node Status = element.getElementsByTagName("urn:StatusCode").item(0);
    			Node statusDesc = element.getElementsByTagName("urn:StatusDesc").item(0);
    			
    			/*Node ClaimID = element.getElementsByTagName("urn:CIMB_ClaimID").item(0);
    			Node CustomerName = element.getElementsByTagName("urn:CIMB_CustomerName").item(0);
    			Node TCustomerName = element.getElementsByTagName("urn:CIMB_TCustomerName").item(0);
    			Node CustomerNumber = element.getElementsByTagName("urn:CIMB_CustomerNumber").item(0);
    			Node SectorCode = element.getElementsByTagName("urn:CIMB_SectorCode").item(0);
    			Node BusinessType = element.getElementsByTagName("urn:CIMB_BusinessType").item(0);
    			Node CIFId = element.getElementsByTagName("urn:CIMB_CIFId").item(0);*/
    			
    			String CIMB_RqUID = RqUID.getFirstChild().getNodeValue();
    			String status = Status.getFirstChild().getNodeValue();
    			String stsDesc = statusDesc.getFirstChild().getNodeValue();
    			/*String CIMB_ClaimID = ClaimID.getFirstChild().getNodeValue();
    			String CIMB_CustomerName = CustomerName.getFirstChild().getNodeValue();
    			String CIMB_TCustomerName = TCustomerName.getFirstChild().getNodeValue();
    			String CIMB_CustomerNumber = CustomerNumber.getFirstChild().getNodeValue();
    			String CIMB_SectorCode = SectorCode.getFirstChild().getNodeValue();
    			String CIMB_BusinessType = BusinessType.getFirstChild().getNodeValue();
    			String CIMB_CIFId = CIFId.getFirstChild().getNodeValue();*/
    			
    			System.out.println("CIMB_AuthorizeRatingSvc :"+element.getAttribute("CIMB_AuthorizeRatingSvc"));
    			System.out.println("CIMB_RqUID : "+ CIMB_RqUID);
    			System.out.println("status : "+ status);
    			System.out.println("stsDesc : "+ stsDesc);
    			/*System.out.println("CIMB_ClaimID : "+ CIMB_ClaimID);
    			System.out.println("CIMB_CustomerName : "+ CIMB_CustomerName);
    			System.out.println("CIMB_TCustomerName : "+ CIMB_TCustomerName);
    			System.out.println("CIMB_CustomerNumber : "+ CIMB_CustomerNumber);
    			System.out.println("CIMB_SectorCode : "+ CIMB_SectorCode);
    			System.out.println("CIMB_BusinessType : "+ CIMB_BusinessType);
    			System.out.println("CIMB_CIFId : "+ CIMB_CIFId);*/
    			
    			
    		}
    		}
         } catch (Exception e) {
            e.printStackTrace();
         }

    }
    
}
