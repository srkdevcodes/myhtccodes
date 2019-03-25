package com.cimb.xml.gen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMSource;

import org.apache.axis.Message;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;

/**
 * @author ktang
 * 
 */
public class SOAPUtil {

	/**
	 * Convert a SOAPNessage to String <p/>
	 * 
	 * @param soapMsg
	 * @return
	 * @throws Exception
	 */
	public static String toString(SOAPMessage soapMsg) throws Exception {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapMsg.writeTo(out);
		String soapMsgStr = new String(out.toByteArray());
		return soapMsgStr;
	}

	/**
	 * Convert a DOM Document into a soap message. <p/>
	 * 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static SOAPMessage toSOAPMessage(Document doc) throws Exception {
		Canonicalizer c14n = Canonicalizer
				.getInstance(Canonicalizer.ALGO_ID_C14N_WITH_COMMENTS);
		byte[] canonicalMessage = c14n.canonicalizeSubtree(doc);
		ByteArrayInputStream in = new ByteArrayInputStream(canonicalMessage);
		MessageFactory factory = MessageFactory.newInstance();
		return factory.createMessage(null, in);
	}

	/**
	 * Convert a DOM Document into an Axis message. <p/>
	 * 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static Message toAxisMessage(Document doc) throws Exception {
		Canonicalizer c14n = Canonicalizer
				.getInstance(Canonicalizer.ALGO_ID_C14N_WITH_COMMENTS);
		byte[] canonicalMessage = c14n.canonicalizeSubtree(doc);
		ByteArrayInputStream in = new ByteArrayInputStream(canonicalMessage);
		return new Message(in);
	}

	/**
	 * Update soap message. <p/>
	 * 
	 * @param doc
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static SOAPMessage updateSOAPMessage(Document doc,
			SOAPMessage message) throws Exception {
		DOMSource domSource = new DOMSource(doc);
		message.getSOAPPart().setContent(domSource);
		return message;
	}

	/**
	 * Search within a SOAP message to get all the param values for a method.
	 * Return a map with all found param-value pairs. Return a empty map if
	 * nothing is found. <p/>
	 * 
	 * @param message
	 * @param methodName
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getMethodParameters(Message message,
			String methodName, List<String> params) throws Exception {
		Document doc = message.getSOAPEnvelope().getAsDocument();
		return getMethodParameters(doc, methodName, params);
	}

	/**
	 * @param doc
	 * @param methodName
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getMethodParameters(Document doc,
			String methodName, List<String> params) throws Exception {

		Map<String, String> paramMap = new HashMap<String, String>();
		if (doc == null) {
			return paramMap;
		}
		// Ensure the DOM view is same as it was saved
		doc.getDocumentElement().normalize();
		NodeList soapbody = doc.getElementsByTagName("soapenv:Body");
		Element soapBodyElement = (Element) soapbody.item(0);
		if (soapBodyElement == null) {
			throw new Exception("Wrong DOM Format: no soapenv:Body found!");
		}

		NodeList methods = soapBodyElement.getElementsByTagName(methodName);
		if (methods == null || methods.getLength() == 0) {
			return paramMap;
		}
		if (methods.getLength() > 1) {
			throw new Exception("Multiple methods with same name " + methodName
					+ " are found in a SOAP Message!");
		}
		Node method = methods.item(0);

		if (method.getNodeType() == Node.ELEMENT_NODE) {
			Element me = (Element) method;
			Element pe = null;
			Iterator<String> iterator = params.iterator();
			String param = null;
			String value = null;
			// loop over the param list and store param-value pair in the map
			while (iterator.hasNext()) {
				param = (String) iterator.next();
				pe = (Element) (me.getElementsByTagName(param).item(0));
				// ignore if the param can't be found in xml
				if (pe != null) {
					// get value of param(i)
					value = pe.getFirstChild().getNodeValue().trim();
					paramMap.put(param, value);
				}
			}
		}
		return paramMap;
	}

}
