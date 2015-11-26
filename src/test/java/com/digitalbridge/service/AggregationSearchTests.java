package com.digitalbridge.service;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.digitalbridge.DigitalBridgeApplicationTests;

public class AggregationSearchTests extends DigitalBridgeApplicationTests {

	@Test
	public final void testPerformBasicAggregationSearchMVC() throws Exception {
		this.mockMvc
				.perform(post("/assetwrapper/search/performBasicAggregationSearch")
						.param("searchKeyword", "garden")
						.param("fieldNames", "aName", "cuisine")
						.header("Authorization", "Basic YXBwVXNlcjphcHBQYXNzd29yZA==")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(document("headers", requestHeaders(headerWithName("Authorization")
						.description("Basic auth credentials"))));
	}

}
