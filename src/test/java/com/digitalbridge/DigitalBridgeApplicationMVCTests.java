package com.digitalbridge;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.RequestDispatcher;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.digitalbridge.controller.AggregationSearchControllerTests;
import com.digitalbridge.domain.AssetWrapper;
import com.digitalbridge.domain.Notes;
import com.digitalbridge.security.SecurityUtils;

public class DigitalBridgeApplicationMVCTests extends DigitalBridgeApplicationTests {

	@Test
	public void errorExample() throws Exception {
		this.mockMvc
				.perform(get("/error")
						.requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
						.requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/restapi")
						.requestAttr(RequestDispatcher.ERROR_MESSAGE,
								"The tag 'http://localhost:8080/restapi/123' does not exist"))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("error", is("Bad Request")))
				.andExpect(jsonPath("timestamp", is(notNullValue())))
				.andExpect(jsonPath("status", is(400)))
				.andExpect(jsonPath("path", is(notNullValue())))
				.andDo(document("error-example", responseFields(fieldWithPath("error")
						.description("The HTTP error that occurred, e.g. 'Bad Request'"),
						fieldWithPath("message")
								.description("A description of the cause of the error"),
						fieldWithPath("path")
								.description("The path to which the request was made"),
						fieldWithPath("status")
								.description("The HTTP status code, e.g. '400'"),
						fieldWithPath("timestamp").description(
								"The time, in milliseconds, at which the error occurred"))));
	}

	@Test
	public void indexExample() throws Exception {
		this.mockMvc
				.perform(get("/restapi").header("Authorization",
						"Basic YXBwVXNlcjphcHBQYXNzd29yZA=="))
				.andExpect(status().isOk())
				.andDo(document("index-example",
						links(linkWithRel("notes")
								.description("The <<resources-notes,Notes resource>>"),
								linkWithRel("appUsers").description(
										"The <<resources-appUsers, Users resource>>"),
								linkWithRel("assets").description(
										"The <<resources-assets, Assets resource>>"),
								linkWithRel("profile")
										.description("The ALPS profile for the service")),
						responseFields(fieldWithPath("_links").description(
								"<<resources-index-links,Links>> to other resources")),
						requestHeaders(headerWithName("Authorization")
								.description("Basic auth credentials"))));

	}

	@Test
	public void createData() {
		SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
		Notes notes = new Notes(RandomStringUtils.randomAlphabetic(5), new Date(),
				Integer.parseInt(RandomStringUtils.randomNumeric(2)));
		Notes notes1 = new Notes(RandomStringUtils.randomAlphabetic(5), new Date(),
				Integer.parseInt(RandomStringUtils.randomNumeric(2)));
		Notes notes2 = new Notes(RandomStringUtils.randomAlphabetic(5), new Date(),
				Integer.parseInt(RandomStringUtils.randomNumeric(2)));
		AssetWrapper assetWrapper = new AssetWrapper();
		assetWrapper.setBuilding(RandomStringUtils.randomAlphabetic(5));
		assetWrapper.setStreet(RandomStringUtils.randomAlphabetic(5));
		assetWrapper.setZipcode(RandomStringUtils.randomNumeric(2));
		assetWrapper.setLocation(new GeoJsonPoint(-73.9387768, 40.8509032));
		assetWrapper.setOrgAssetId(RandomStringUtils.randomAlphabetic(5));
		assetWrapper.setAssetName(RandomStringUtils.randomAlphabetic(5));
		assetWrapper.setBorough(RandomStringUtils.randomAlphabetic(5));
		assetWrapper.setCuisine(RandomStringUtils.randomAlphabetic(5));
		assetWrapper.setNotes(Arrays.asList(notes, notes1, notes2));
		AssetWrapper aWrapper = assetWrapperRepository.save(assetWrapper);
		assertNotNull(aWrapper);
		assetID = assetWrapper.getId();
	}

	@Test
	@Ignore
    public void loadTest() throws Exception {
        int count = 0;
        AggregationSearchControllerTests controllerTests = new AggregationSearchControllerTests();
        controllerTests.objectMapper = objectMapper;
        controllerTests.mockMvc = mockMvc;
        do {
            createData();
            controllerTests.testPerformAdvancedAggregationSearchApi();
            controllerTests.testPerformBasicAggregationSearchApi();
            controllerTests.testPerformIconicSearchApi();
            count++;
        }
        while (count <= 2);
    }
}
