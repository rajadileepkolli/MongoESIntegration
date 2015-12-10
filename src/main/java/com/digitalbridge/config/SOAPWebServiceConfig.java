package com.digitalbridge.config;

import javax.xml.ws.Endpoint;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;

import com.digitalbridge.soap.service.IBMWebservice;
import com.digitalbridge.soap.service.IBMWebserviceImpl;

@Configuration
/**
 * <p>SOAPWebServiceConfig class.</p>
 *
 * @author rajakolli
 * @version $Id: $Id
 */
@EnableWs
public class SOAPWebServiceConfig extends WsConfigurerAdapter {
	/**
	 * <p>dispatcherServlet.</p>
	 *
	 * @return a {@link org.springframework.boot.context.embedded.ServletRegistrationBean} object.
	 */
	@Bean
	public ServletRegistrationBean dispatcherServlet() {
		CXFServlet cxfServlet = new CXFServlet();
		return new ServletRegistrationBean(cxfServlet, "/Service/*");
	}

	/**
	 * <p>springBus.</p>
	 *
	 * @return a {@link org.apache.cxf.bus.spring.SpringBus} object.
	 */
	@Bean(name = "cxf")
	public SpringBus springBus() {
		return new SpringBus();
	}

	/**
	 * <p>myService.</p>
	 *
	 * @return a {@link com.digitalbridge.soap.service.IBMWebservice} object.
	 */
	@Bean
	public IBMWebservice myService() {
		return new IBMWebserviceImpl();
	}

	/**
	 * <p>endpoint.</p>
	 *
	 * @return a {@link javax.xml.ws.Endpoint} object.
	 */
	@Bean
	public Endpoint endpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), myService());
		endpoint.publish("/SOAPWebService");
		return endpoint;
	}
}
