package co.za.flash.credential.management.helper.utils;

import co.za.flash.credential.management.helper.interfaces.ISoapMessageMapper;

import javax.xml.soap.*;

public class SoapClientUtil {
    /*private static void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("soap", "http://example.com/SMX/example");
        SOAPBody soapBody = envelope.getBody();
    }*/

    public static SOAPMessage callSoapWebService(String soapEndpointUrl,
                                                 String soapAction, ISoapMessageMapper requestBody)
            throws SOAPException, UnsupportedOperationException, IllegalArgumentException{
        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        // Send SOAP Message to SOAP Server
        SOAPMessage soapRequest = createSOAPRequest(soapAction, requestBody);
        SOAPMessage soapResponse = soapConnection.call(soapRequest, soapEndpointUrl);
        return soapResponse;
    }

    private static SOAPMessage createSOAPRequest(String soapAction, ISoapMessageMapper requestBody)
            throws IllegalArgumentException, SOAPException{
        //MessageFactory messageFactory = MessageFactory.newInstance();
        //SOAPMessage soapMessage = messageFactory.createMessage();
        //createSoapEnvelope(soapMessage);
        SOAPMessage soapMessage = requestBody.convertToSoapMessage();

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();
        return soapMessage;
    }
}
