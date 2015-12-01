package com.digitalbridge.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.digitalbridge.DigitalBridgeApplicationTests;
import com.digitalbridge.controller.AggregationSearchController;
import com.digitalbridge.request.SearchResponse;
import com.digitalbridge.security.SecurityUtils;

public class AggregationSearchControllerTests extends DigitalBridgeApplicationTests
{
    @Autowired
    AggregationSearchController aggregationSearch;

    @Test
    public final void testPerformBasicAggregationSearchMVC() throws Exception
    {
        this.mockMvc
                .perform(post("/api/assetwrapper/search/performBasicAggregationSearch")
                        .param("searchKeyword", "garden")
                        .param("fieldNames", "aName", "cuisine")
                        .header("Authorization", "Basic YXBwVXNlcjphcHBQYXNzd29yZA==")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(document("headers", requestHeaders(headerWithName("Authorization")
                        .description("Basic auth credentials"))));
    }

    @Test
    public final void testPerformBasicAggregationSearch() throws Exception
    {
        String searchKeyword = "garden";
        String[] fieldNames = new String[] { "aName", "cuisine" };
        SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
        SearchResponse response = aggregationSearch.performBasicAggregationSearch(true, null,
                null, searchKeyword, fieldNames);
        assertTrue(response.getTotalElements() > 0);
    }

}
