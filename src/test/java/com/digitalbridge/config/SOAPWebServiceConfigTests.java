package com.digitalbridge.config;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.StringReader;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.OutputCapture;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.digitalbridge.DigitalBridgeApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DigitalBridgeApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
@DirtiesContext
public class SOAPWebServiceConfigTests {

	@Rule
	public OutputCapture output = new OutputCapture();

	private WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

	@Value("${local.server.port}")
	private int serverPort;

	@Before
	public void setUp() {
		this.webServiceTemplate.setDefaultUri(
				"http://localhost:" + this.serverPort + "/Service/SOAPWebService");
	}

	@Test
	public void testHelloRequest() {
		final String request = "<q0:sayHello xmlns:q0=\"http://service.digitalbridge.com/\"><myname>Raja</myname></q0:sayHello>";

		StreamSource source = new StreamSource(new StringReader(request));
		StreamResult result = new StreamResult(System.out);

		this.webServiceTemplate.sendSourceAndReceiveToResult(source, result);
		assertThat(this.output.toString(), containsString(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns2:sayHelloResponse xmlns:ns2=\"http://service.digitalbridge.com/\"><return>Welcome to CXF Spring boot Raja!!!</return></ns2:sayHelloResponse>"));
	}

}
