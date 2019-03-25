package com.cimb.icres.req;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;

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
	
	final static Logger logger = Logger.getLogger(GenReqXml.class);
	ResourceBundle resourceBundle = ResourceBundle.getBundle("ICRES");
	ResourceBundle busProps = ResourceBundle.getBundle("BUSINESS_TYPE");
	
			
	
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
            logger.error("----------Am in main method--------- ERROR : "+e.getMessage(),e);
        }
    }
    
    private String getTDate(Date tdate){
    	String myDate = "";
    	try {
    		String pattern = "yyyy-MM-dd'T'HH:mm:ss.FFFFFF";
    		DateFormat df = new SimpleDateFormat(pattern);
    		myDate = df.format(new Date());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    	
		return myDate;
    }
    
   

   
	private void createSOAPRequest() throws Exception {
    	
    	try {
           // SOAP Body
        HashSet<TempModel> tempList = new HashSet<TempModel>();
        tempList = getTempTableData();
        String rquid = "";
       if(!tempList.isEmpty()){
        	//logger.info("TempList size"+ tempList.size());
        for (TempModel model : tempList) {
        	//logger.info("Customer Number --> "+ model.getCustomerNumber());
        	MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();	
            
            String serverURI = resourceBundle.getString("URI");

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("urn", serverURI);
		SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("CIMB_UpdateCustomerInfoSvc", "urn");
        SOAPElement CIMB_SignonRq = soapBodyElem.addChildElement("CIMB_SignonRq", "urn");
        SOAPElement ClientDt = CIMB_SignonRq.addChildElement("ClientDt", "urn");
       // ClientDt.addTextNode(String.valueOf(instant.toEpochMilli()));
        ClientDt.addTextNode(getTDate(new Date()));
        SOAPElement CustLangPref = CIMB_SignonRq.addChildElement("CustLangPref", "urn");
        CustLangPref.addTextNode(resourceBundle.getString("LANGUAGE"));
        
        SOAPElement clientApp = CIMB_SignonRq.addChildElement("ClientApp", "urn");
        SOAPElement org = clientApp.addChildElement("Org", "urn");
        org.addTextNode(resourceBundle.getString("ORG"));
        SOAPElement name = clientApp.addChildElement("Name", "urn");
        name.addTextNode(resourceBundle.getString("NAME"));
        SOAPElement version = clientApp.addChildElement("Version", "urn");
        version.addTextNode(resourceBundle.getString("VERSION"));
        
        SOAPElement CIMB_HeaderRq = CIMB_SignonRq.addChildElement("CIMB_HeaderRq", "urn");
        SOAPElement CIMB_ConsumerId = CIMB_HeaderRq.addChildElement("CIMB_ConsumerId", "urn");
        CIMB_ConsumerId.addTextNode(resourceBundle.getString("CONSUMER_ID"));
        SOAPElement CIMB_ConsumerPasswd = CIMB_HeaderRq.addChildElement("CIMB_ConsumerPasswd", "urn");
        CIMB_ConsumerPasswd.addTextNode(resourceBundle.getString("CONSUMER_PWD"));
        SOAPElement CIMB_ServiceName = CIMB_HeaderRq.addChildElement("CIMB_ServiceName", "urn");
        CIMB_ServiceName.addTextNode(resourceBundle.getString("SERVICE_NAME"));
        SOAPElement CIMB_ServiceVersion = CIMB_HeaderRq.addChildElement("CIMB_ServiceVersion", "urn");
        CIMB_ServiceVersion.addTextNode(resourceBundle.getString("SERVICE_VERSION"));
        SOAPElement CIMB_SrcSystem = CIMB_HeaderRq.addChildElement("CIMB_SrcSystem", "urn");
        CIMB_SrcSystem.addTextNode(resourceBundle.getString("SRC_SYSTEM"));
        
        SOAPElement CIMB_ProviderList = CIMB_HeaderRq.addChildElement("CIMB_ProviderList", "urn");
        SOAPElement CIMB_Provider = CIMB_ProviderList.addChildElement("CIMB_Provider", "urn");
        SOAPElement CIMB_ProviderSystemCode = CIMB_Provider.addChildElement("CIMB_ProviderSystemCode", "urn");
        CIMB_ProviderSystemCode.addTextNode(resourceBundle.getString("PROVIDER_SYS_CODE"));
        
        SOAPElement CIMB_SrcCountryCode = CIMB_HeaderRq.addChildElement("CIMB_SrcCountryCode", "urn");
        CIMB_SrcCountryCode.addTextNode(resourceBundle.getString("SOURCE_CNTRY"));
        
        SOAPElement CIMB_UpdateCustomerInfoRq = soapBodyElem.addChildElement("CIMB_UpdateCustomerInfoRq", "urn");
        SOAPElement CIMB_RqUID = CIMB_UpdateCustomerInfoRq.addChildElement("CIMB_RqUID", "urn");
        if(model.getRquId().contains(resourceBundle.getString("SOURCE_CNTRY"))){
        	rquid = String.valueOf(model.getRquId());
        }else{
        	rquid = String.valueOf(resourceBundle.getString("SOURCE_CNTRY")+model.getRquId());
        }
        CIMB_RqUID.addTextNode(rquid);
        
        SOAPElement CIMB_ClaimID = CIMB_UpdateCustomerInfoRq.addChildElement("CIMB_ClaimID", "urn");
        
        CIMB_ClaimID.addTextNode(rquid);
        
        SOAPElement CIMB_CustomerCreationInfo = CIMB_UpdateCustomerInfoRq.addChildElement("CIMB_CustomerCreationInfo", "urn");
        SOAPElement CIMB_CustomerName = CIMB_CustomerCreationInfo.addChildElement("CIMB_CustomerName", "urn");
        CIMB_CustomerName.addTextNode(model.getCustomerName());
        
        SOAPElement CIMB_TCustomerName = CIMB_CustomerCreationInfo.addChildElement("CIMB_TCustomerName", "urn");
        CIMB_TCustomerName.addTextNode(model.getTcusomterName());
        
        SOAPElement CIMB_CustomerNumber = CIMB_CustomerCreationInfo.addChildElement("CIMB_CustomerNumber", "urn");
        CIMB_CustomerNumber.addTextNode(model.getCustomerNumber());
        if(resourceBundle.getString("SOURCE_CNTRY").equalsIgnoreCase("TH")){
        	SOAPElement CIMB_SectorCode = CIMB_CustomerCreationInfo.addChildElement("CIMB_SectorCode", "urn");
            CIMB_SectorCode.addTextNode(model.getBusinessType());
        }else{
        	SOAPElement CIMB_SectorCode = CIMB_CustomerCreationInfo.addChildElement("CIMB_SectorCode", "urn");
            CIMB_SectorCode.addTextNode(model.getSectorCode());
        }
        
        
        SOAPElement CIMB_BusinessType = CIMB_CustomerCreationInfo.addChildElement("CIMB_BusinessType", "urn");
        CIMB_BusinessType.addTextNode(getBusinessTypeValue(model.getBusinessType()));
      
        
        SOAPElement CIMB_CIFId = CIMB_CustomerCreationInfo.addChildElement("CIMB_CIFId", "urn");
        CIMB_CIFId.addTextNode(model.getCifId());
                
       
       String sopMessage =  SOAPUtil.toString(soapMessage);
     //  logger.info("message : " + sopMessage);
       upateTemptable(sopMessage,rquid, model.getId());
       upateCustNotifyFlag(model.getCustomerId());
       
       SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
       SOAPConnection con = soapConnectionFactory.createConnection();
      try {
    	 
    	  URL rags = new URL(resourceBundle.getString("POST_URL"));
          SOAPMessage response = con.call(soapMessage, rags);
          String soapResponse =  SOAPUtil.toString(response);
         // logger.info("Response : " + soapResponse);
          //stringToSOAPElement(soapResponse);
          readAndUpdateFromResponse(response, rquid,model.getCustomerName(),model.getId(), model.getCustomerId(),model.getCounter());
         
          
	} catch (Exception e) {
		logger.error("------------------- ERROR : "+e.getMessage());
		logger.error("----------Am in SOAP call --------- ERROR : "+e.getMessage(),e);
	}
       
      
       con.close();
        	}
        }
    	       
    	} catch (Exception e) {
    		logger.error("------------------- ERROR : "+e.getMessage());
		}
	
    }

    private String getBusinessTypeValue(String businessType) {
    	String bustype ="";
    	String type ="";
    	if( !businessType.isEmpty() && businessType != null){
    		businessType.trim();
    		type = businessType.substring(0, 1);
    		bustype = busProps.getString("BUS_TYPE_"+type);
    		/*
    		if(businessType.contains("A")){
    			bustype = "1";
    		}
    		if(businessType.contains("B")){
    			bustype = "2";
    		}
    		if(businessType.contains("C")){
    			bustype = "3";
    		}
    		if(businessType.contains("D")){
    			bustype = "4";
    		}
    		if(businessType.contains("E")){
    			bustype = "5";
    		}
    		if(businessType.contains("F")){
    			bustype = "6";
    		}
    		if(businessType.contains("G")){
    			bustype = "7";
    		}
    		if(businessType.contains("H")){
    			bustype = "8";
    		}
    		if(businessType.contains("I")){
    			bustype = "9";
    		}
    		if(businessType.contains("J")){
    			bustype = "10";
    		}
    		if(businessType.contains("K")){
    			bustype = "11";
    		}
    		if(businessType.contains("L")){
    			bustype = "12";
    		}
    		if(businessType.contains("M")){
    			bustype = "13";
    		}
    		if(businessType.contains("N")){
    			bustype = "14";
    		}
    		if(businessType.contains("O")){
    			bustype = "15";
    		}
    		if(businessType.contains("P")){
    			bustype = "16";
    		}
    		if(businessType.contains("Q")){
    			bustype = "17";
    		}
    		if(businessType.contains("R")){
    			bustype = "18";
    		}
    		if(businessType.contains("S")){
    			bustype = "19";
    		}
    		if(businessType.contains("T")){
    			bustype = "20";
    		}
    		if(businessType.contains("U")){
    			bustype = "21";
    		}*/
    		
    	}
		
		return bustype;
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
    
	private void readAndUpdateFromResponse(final SOAPMessage soapResponse, String rqUID, String customerName,Integer id, String customerId, Integer counter) {
		
		String statusDesc ="";
		String updt_flag = "";
		NodeList responseList = null;
        /*MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage soapMsg = factory.createMessage();
        SOAPPart part = soapMsg.getSOAPPart();

        SOAPEnvelope envelope = part.getEnvelope();
        SOAPBody body = envelope.getBody();
        SOAPBodyElement element_response = body.addBodyElement(envelope.createName("response"));
        SOAPBodyElement element_result = body.addBodyElement(envelope.createName("result"));

        element_result.addChildElement("code").addTextNode("A000");
        element_result.addChildElement("description").addTextNode("Successful");
        element_response.addChildElement(element_result);
        //soapMsg.writeTo(System.out);/**/
        try {
        	logger.info("Country ID :"+resourceBundle.getString("SOURCE_CNTRY"));
        	if(resourceBundle.getString("SOURCE_CNTRY").equalsIgnoreCase("ID")){
        		responseList = soapResponse.getSOAPBody().getElementsByTagName("esb:CIMB_ProviderRespStatusList");
        		logger.info("Reading for Niaga....");
        	}else if(resourceBundle.getString("SOURCE_CNTRY").equalsIgnoreCase("IDN")){
            		responseList = soapResponse.getSOAPBody().getElementsByTagName("esb:CIMB_ProviderRespStatusList");
            		logger.info("Reading for Niaga....");	
        	}else{
        		responseList = soapResponse.getSOAPBody().getElementsByTagName("urn:CIMB_ProviderRespStatusList");
        		logger.info("Reading for MST....");
        	}
        	if(responseList != null){
            for (int i = 0; i < responseList.getLength(); i++) {
                NodeList resultList = responseList.item(i).getChildNodes();
                for (int j = 0; j < resultList.getLength(); j++) {
                    NodeList codeAndDescList = resultList.item(j).getChildNodes();
                    for (int k = 0; k < codeAndDescList.getLength(); k++) {
                    	logger.info(codeAndDescList.item(k).getNodeName().trim());
                    	logger.info(codeAndDescList.item(k).getTextContent().trim());
                    	if(codeAndDescList.item(k).getNodeName().contains("urn:CIMB_StatusDesc") || codeAndDescList.item(k).getNodeName().contains("esb:CIMB_StatusDesc")){
                    		logger.info("Status Reading....!");
                    		statusDesc = codeAndDescList.item(k).getTextContent().trim();
                    		logger.info("CIMB_Statud_desc :"+statusDesc);
                    	}
                    }
                }
            }
        }
            
            logger.info(statusDesc);
            if(statusDesc.equalsIgnoreCase("SUCCESS")){
            	updt_flag = "Y";
            try {
				updateAllCustomerRecords(updt_flag,customerId);
			} catch (Exception e) {
				logger.error("----------Am in updating Status get Resposne SUCCESS --------- ERROR : "+e.getMessage(),e);
				e.printStackTrace();
			}	
            }else {  updt_flag = "N"; }
            try {
				upateResTemptable(SOAPUtil.toString(soapResponse),updt_flag,statusDesc,id,counter);
			} catch (Exception e) {
				logger.error("----------Am in retreiving Response and Updating Temp Table --------- ERROR : "+e.getMessage(),e);
				e.printStackTrace();
			}
            
        } catch (SOAPException ex) {
        	logger.error("----------Am in retreiving Response --------- ERROR : "+ex.getMessage(),ex);
            System.out.println(ex);
        }

    }
	
	
	
    public static SOAPElement stringToSOAPElement(String xmlText) throws org.xml.sax.SAXException {
        try {
        	
        	logger.info(" StringTOSOAPElement ----------------");
            // Load the XML text into a DOM Document
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
           // InputStream stream  = new ByteArrayInputStream(xmlText.getBytes());
            Document document = builderFactory.newDocumentBuilder().parse(xmlText); 
            
            document.getDocumentElement().normalize();
            logger.info("Root element : "+document.getDocumentElement().getElementsByTagName("urn:CIMB_UpdateCustomerInfoSvc"));
   	     
   	     
   	     	NodeList list = document.getElementsByTagName("urn:CIMB_UpdateCustomerInfoSvc");
   	     	logger.info("The Node list is as folliws : - ");
            
            // Use SAAJ to convert Document to SOAPElement
            // Create SoapMessage
            MessageFactory msgFactory = MessageFactory.newInstance();
            SOAPMessage    message    = msgFactory.createMessage();
            SOAPBody       soapBody   = message.getSOAPBody();
            
           // NodeList list = soapBody.getElementsByTagName("urn:CIMB_UpdateCustomerInfoSvc");
            
            for(int temp = 0; temp < list.getLength(); temp++){
   	    	 Node  node = list.item(temp);
   	    	logger.info("\nCurrentElement : "+node.getNodeName());
   	     
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
   			
   			logger.info("CIMB_AuthorizeRatingSvc :"+element.getAttribute("CIMB_AuthorizeRatingSvc"));
   			logger.info("CIMB_RqUID : "+ CIMB_RqUID);
   			logger.info("status : "+ status);
   			logger.info("stsDesc : "+ stsDesc);
   			/*System.out.println("CIMB_ClaimID : "+ CIMB_ClaimID);
   			System.out.println("CIMB_CustomerName : "+ CIMB_CustomerName);
   			System.out.println("CIMB_TCustomerName : "+ CIMB_TCustomerName);
   			System.out.println("CIMB_CustomerNumber : "+ CIMB_CustomerNumber);
   			System.out.println("CIMB_SectorCode : "+ CIMB_SectorCode);
   			System.out.println("CIMB_BusinessType : "+ CIMB_BusinessType);
   			System.out.println("CIMB_CIFId : "+ CIMB_CIFId);*/
   			
   			
   		}
   		}
             
            // This returns the SOAPBodyElement 
            // that contains ONLY the Payload
            return soapBody.addDocument(document);
           
        } catch (SOAPException  e) {
        	logger.error("SOAPException : " + e);
            return null;
  
        } catch (IOException  e) {
        	logger.error("IOException : " + e);
            return null;
  
        } catch (ParserConfigurationException  e) {
        	logger.error("ParserConfigurationException : " + e);
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
    			
//    			System.out.println("CIMB_AuthorizeRatingSvc :"+element.getAttribute("CIMB_AuthorizeRatingSvc"));
//    			System.out.println("CIMB_RqUID : "+ CIMB_RqUID);
//    			System.out.println("status : "+ status);
//    			System.out.println("stsDesc : "+ stsDesc);
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
