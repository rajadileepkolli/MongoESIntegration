package com.digitalbridge.util;

import static org.junit.Assert.assertNotNull;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.digitalbridge.DigitalBridgeApplicationTests;

public class AuthorizationHeaderTests extends DigitalBridgeApplicationTests
{

    AuthorizationHeader authorizationHeader = new AuthorizationHeader();

    @Test
    public final void testGetHeader() throws UnsupportedEncodingException
    {
        String val = authorizationHeader.getBasicHeader("appUser",
                "appPassword");
        assertNotNull(val);
    }

    @Test
    public final void testGetHeaderApi() throws Exception
    {
        this.mockMvc
                .perform(get("/restapi/digitalbridge/search/getEncoded/{username}/{password}","user" ,"password")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("getEncoded",
                        pathParameters(
                                parameterWithName("username").description("The username for encoded Header"),
                                parameterWithName("password").description("The password for encoded Header"))
                        ))
                .andReturn();
    }

}
