package com.digitalbridge.soap.service;

import javax.jws.WebService;

/**
 * <p>IBMWebserviceImpl class.</p>
 *
 * @author rajakolli
 * @version 1:0
 */
@WebService(serviceName = "SOAPWebService", portName = "SOAPWebServicePort", targetNamespace = "http://digitalbridge.com/", endpointInterface = "com.digitalbridge.soap.service.IBMWebservice")
public class IBMWebserviceImpl implements IBMWebservice {
	
	/** {@inheritDoc} */
	@Override
	public String sayHello(String myname) {
		try {
			return "Welcome to CXF Spring boot " + myname + "!!!";
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
}
