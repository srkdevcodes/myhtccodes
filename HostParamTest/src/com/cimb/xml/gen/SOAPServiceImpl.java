package com.cimb.xml.gen;
import javax.xml.soap.*;
import java.net.URL;
import javax.activation.DataHandler;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

public class SOAPServiceImpl implements ISOAPService {
	
	protected SOAPConnectionFactory connectionFactory;
    protected SOAPFactory factory;
    protected MessageFactory messageFactory;
    
    public SOAPServiceImpl(){
        try{
            init();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void init() throws Exception {
        
        connectionFactory = SOAPConnectionFactory.newInstance();
        factory = SOAPFactory.newInstance();
        messageFactory = MessageFactory.newInstance();
        
    }
    
    public SOAPConnection getConnection() throws Exception {
        return connectionFactory.createConnection();
    }
    
    public SOAPMessage getMessage( boolean header ) throws Exception {
        SOAPMessage msg = messageFactory.createMessage();
        if( !header ) msg.getSOAPHeader().detachNode();
        return msg;
    }
    
    public SOAPMessage getMessage( MimeHeaders headers, InputStream in ) throws Exception {
        return messageFactory.createMessage(headers, in);
    }
    
    public boolean addMimeHeader( SOAPMessage msg, String name, String value ){
        if( msg == null || name == null || value == null ) return false;
        msg.getMimeHeaders().addHeader( name, value );
        return true;
    }
    
    public SOAPEnvelope getEnvelope( SOAPMessage msg ) throws Exception {
        if( msg == null ) return null;
        return msg.getSOAPPart().getEnvelope();
    }
    
    public Name createName( String localName, String prefix, String uri ) throws Exception {
        if( localName == null ) return null;
        if( prefix == null || uri == null ) return factory.createName( localName );
        else return factory.createName( localName, prefix, uri );
    }
    
    public SOAPElement createElement( String localName, String prefix, String uri ) throws Exception {
        if( localName == null ) return null;
        if( prefix == null || uri == null ) return factory.createElement( localName );
        else return factory.createElement( localName, prefix, uri );
    }
    
    public SOAPBodyElement addBodyElement( SOAPMessage msg, Name n ) throws Exception {
        if( msg == null || n == null ) return null;
        return msg.getSOAPBody().addBodyElement( n );
    }
    
    public SOAPElement addChildElement( SOAPElement element, Name n ) throws Exception {
        if( element == null || n == null ) return null;
        return element.addChildElement( n );
    }
    
    public SOAPElement addChildElement( SOAPElement element, SOAPElement e ) throws Exception {
        if( element == null || e == null ) return null;
        return element.addChildElement( e );
    }
    
    public SOAPElement addChildElement( SOAPElement element, String localName, String prefix, String uri ) throws Exception {
        if( element == null || localName == null ) return null;
        if( uri != null && prefix != null ) return element.addChildElement( localName, prefix, uri );
        else if( prefix != null ) return element.addChildElement( localName, prefix );
        else return element.addChildElement( localName );
    }
    
    public void addAttachment( SOAPMessage msg, URL url, String contentIDName ) throws Exception {
        DataHandler dataHandler = new DataHandler( url );
        AttachmentPart att = msg.createAttachmentPart( dataHandler );
        att.setContentId( contentIDName );
        msg.addAttachmentPart(att);
    }
    
    public void addImageAttachment( SOAPMessage msg, URL url, String contentIDName, String contentType ) throws Exception {
        java.awt.Image im  = java.awt.Toolkit.getDefaultToolkit().createImage(url);
        AttachmentPart att = msg.createAttachmentPart(im, contentType );
        att.setContentId( contentIDName );
        msg.addAttachmentPart(att);
    }
    
    public void getPredefinedSOAPMessage( SOAPMessage msg, FileInputStream fos ) throws Exception {
        StreamSource src = new StreamSource( fos );
        msg.getSOAPPart().setContent( src );
    }
    
    public void readResponse( SOAPMessage response, OutputStream out ) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = response.getSOAPPart().getContent();
        StreamResult result = new StreamResult( out );
        transformer.transform( sourceContent, result );
    }
}
