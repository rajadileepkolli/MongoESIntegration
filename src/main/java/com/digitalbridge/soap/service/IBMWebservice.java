package com.digitalbridge.soap.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import com.digitalbridge.exception.DigitalBridgeException;

/**
 * <p>
 * IBMWebservice interface.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
@WebService(targetNamespace = "http://digitalbridge.com/", name = "SOAPWebService")
public interface IBMWebservice {
    /**
     * <p>
     * sayHello.
     * </p>
     *
     * @param myname a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     * @throws com.digitalbridge.exception.DigitalBridgeException if any.
     */
    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "sayHello", targetNamespace = "http://service.digitalbridge.com/", className = "com.digitalbridge.soap.service.SayHello")
    @WebMethod(action = "urn:SayHello")
    @ResponseWrapper(localName = "sayHelloResponse", targetNamespace = "http://service.digitalbridge.com/", className = "com.digitalbridge.soap.service.SayHelloResponse")
    public String sayHello(@WebParam(name = "myname", targetNamespace = "") String myname)
            throws DigitalBridgeException;
}
