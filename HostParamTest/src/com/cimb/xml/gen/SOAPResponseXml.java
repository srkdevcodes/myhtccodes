package com.cimb.xml.gen;

import java.net.URL;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

//import edu.whoi.xml.soap.*;

public class SOAPResponseXml {
	 /** Creates a new instance of SOAPTest */
    public SOAPResponseXml() {
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            
            
            ISOAPService soap = new SOAPServiceImpl();
            
            SOAPMessage message = soap.getMessage( false );
            SOAPBody body = message.getSOAPBody();
            Name cwUpdate = soap.createName("connectWHOIUpdate", null, null);
            SOAPBodyElement cwupdate = body.addBodyElement( cwUpdate );
            
            Name attname = soap.createName("id", null, null);
            cwupdate.addAttribute(attname, "ashepherd");

            SOAPElement mailstop = cwupdate.addChildElement("mailstop");
            mailstop.addTextNode("46");
            
            SOAPElement room = cwupdate.addChildElement("room");
            room.addTextNode("768");
            
            SOAPElement phone = cwupdate.addChildElement("phone");
            phone.addTextNode("1234");
            
            SOAPElement title = cwupdate.addChildElement("title");
            title.addTextNode("tester");

            try{
                message.writeTo( System.out );
            }catch(Exception read){
                read.printStackTrace();
            }
            
            URL rags = new URL("http://ragnarok.whoi.edu:8001/hpb/contactInfoUpdate.go");
            SOAPConnection con = soap.getConnection();
            SOAPMessage response = con.call(message, rags);
            try{
                response.writeTo( System.out );
            }catch(Exception read){
                read.printStackTrace();
            }
            con.close();

            body = response.getSOAPBody();
            java.util.Iterator updates = body.getChildElements();
            while (updates.hasNext()) {
                System.out.println();
                // The listing and its ID
                SOAPElement update = (SOAPElement)updates.next();
                String status = update.getAttribute("id");
                System.out.println("Status: "+status );
                java.util.Iterator i = update.getChildElements();
                while( i.hasNext() ){
                    SOAPElement e = (SOAPElement)i.next();
                    String name = e.getLocalName();
                    String value = e.getValue();
                    System.out.println( name+": "+value);
                }
            }
            
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
