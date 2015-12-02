package com.digitalbridge.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.digitalbridge.DigitalBridgeApplicationTests;
import com.digitalbridge.exception.DigitalBridgeException;
import com.digitalbridge.request.SearchResponse;
import com.digitalbridge.security.SecurityUtils;

public class AggregationSearchControllerTests extends DigitalBridgeApplicationTests
{
    @Autowired
    AggregationSearchController aggregationSearchController;

    @Test
    public final void testPerformBasicAggregationSearchApi() throws Exception
    {
        this.mockMvc
                .perform(post("/restapi/assetwrapper/search/performBasicAggregationSearch")
                        .param("searchKeyword", "garden")
                        .param("fieldNames", "aName", "cuisine")
                        .header("Authorization", "Basic YXBwVXNlcjphcHBQYXNzd29yZA==")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(document("headers", requestHeaders(headerWithName("Authorization")
                        .description("Basic auth credentials"))))
                .andReturn();
    }

    @Test
    public final void testPerformBasicAggregationSearch() throws DigitalBridgeException
    {
        String searchKeyword = "garden";
        String[] fieldNames = new String[] { "aName", "cuisine" };
        SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
        SearchResponse response = aggregationSearchController
                .performBasicAggregationSearch(true, null, null, searchKeyword, fieldNames);
        assertTrue(response.getTotalElements() > 0);
    }

    @Test
    public final void testPerformIconicSearch() throws IOException
    {
        String searchKeyword = "garden";
        String fieldName = "aName";
        SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
        ResponseEntity<Set<String>> entity = aggregationSearchController
                .performIconicSearch(searchKeyword, fieldName, false);
        assertNotNull(entity.getBody());
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public final void testPerformIconicSearchApi() throws Exception
    {
        this.mockMvc
                .perform(post("/restapi/assetwrapper/search/performIconicSearch")
                        .param("searchKeyword", "garden").param("fieldName", "aName")
                        .header("Authorization", "Basic YXBwVXNlcjphcHBQYXNzd29yZA==")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

}
