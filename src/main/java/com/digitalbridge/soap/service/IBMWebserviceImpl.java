package com.digitalbridge.soap.service;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digitalbridge.exception.DigitalBridgeException;

/**
 * <p>
 * IBMWebserviceImpl class.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
@WebService(serviceName = "SOAPWebService", portName = "SOAPWebServicePort", targetNamespace = "http://digitalbridge.com/", endpointInterface = "com.digitalbridge.soap.service.IBMWebservice")
public class IBMWebserviceImpl implements IBMWebservice {

    private static final Logger LOGGER = LoggerFactory.getLogger(IBMWebserviceImpl.class);

    @Override
    public String sayHello(String myname) throws DigitalBridgeException {
        try {
            return "Welcome to CXF Spring boot " + myname + "!!!";
        }
        catch (Exception ex) {
            LOGGER.error("{}", ex.getMessage(), ex);
            throw new DigitalBridgeException("RunTimeException", ex.getMessage());
        }
    }
}
