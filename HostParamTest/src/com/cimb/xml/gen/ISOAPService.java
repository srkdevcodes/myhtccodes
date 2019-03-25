package com.cimb.xml.gen;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;

public interface ISOAPService {
	
	 public void init() throws Exception;
	 
	 public SOAPConnection getConnection() throws Exception;
	 
	 public SOAPMessage getMessage( boolean header ) throws Exception;
	 
	 public SOAPMessage getMessage( MimeHeaders headers, InputStream in ) throws Exception;
	 
	 public boolean addMimeHeader( SOAPMessage msg, String name, String value );
	 
	 public SOAPEnvelope getEnvelope( SOAPMessage msg ) throws Exception;
	 
	 public Name createName( String localName, String prefix, String uri ) throws Exception;
	    
	    public SOAPElement createElement( String localName, String prefix, String uri ) throws Exception;
	    
	    public SOAPBodyElement addBodyElement( SOAPMessage msg, Name n ) throws Exception ;
	    
	    public SOAPElement addChildElement( SOAPElement element, Name n ) throws Exception;
	    
	    public SOAPElement addChildElement( SOAPElement element, SOAPElement e ) throws Exception ;
	    
	    public SOAPElement addChildElement( SOAPElement element, String localName, String prefix, String uri ) throws Exception;
	    
	    public void addAttachment( SOAPMessage msg, URL url, String contentIDName ) throws Exception;
	    
	    public void addImageAttachment( SOAPMessage msg, URL url, String contentIDName, String contentType ) throws Exception;
	    
	    public void getPredefinedSOAPMessage( SOAPMessage msg, FileInputStream fos ) throws Exception;
	    
	    public void readResponse( SOAPMessage response, OutputStream out ) throws Exception;

}
