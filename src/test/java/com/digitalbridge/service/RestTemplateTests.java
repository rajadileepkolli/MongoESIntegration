package com.digitalbridge.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.digitalbridge.domain.Address;
import com.digitalbridge.request.UpdateRequest;

public class RestTemplateTests {
	private static final String assetID = "56094694bd51636546272ee8";

	@Test
	@Ignore
	public final void testUpdateAddressFieldUsingRestTemplate() {
		final String uri = "http://localhost:8080/assetwrapper/search/update/addressValue";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("assetID", assetID);
		int random = new Random().nextInt(1000);
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.setKey("building");
		updateRequest.setValue(random);
		params.put("updateValues", updateRequest);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
//		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Authorization", "Basic YXBwVXNlcjphcHBQYXNzd29yZA==");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		// restTemplate.put( uri, null, params);
		ResponseEntity<Address> result = restTemplate.exchange(uri, HttpMethod.PUT,
				entity, Address.class, params);
		System.out.println(result);
	}
}
