package com.digitalbridge.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;

import com.digitalbridge.DigitalBridgeApplicationTests;
import com.digitalbridge.domain.AssetWrapper;
import com.digitalbridge.domain.Notes;
import com.digitalbridge.request.UpdateRequest;
import com.digitalbridge.security.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AssetWrapperServiceTests extends DigitalBridgeApplicationTests
{

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public final void testGetAll()
    {
        SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
        assetWrapperService.getAll();
    }

    @Test
    public final void testGeospatialsearch()
    {
        SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
        Page<AssetWrapper> response = assetWrapperService.geospatialsearch();
        assertTrue(response.hasContent());
    }

    @Test
    public final void testCreateGeoSpatialIndex()
    {
        SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_ADMIN);
        assetWrapperService.createGeoSpatialIndex();
    }

    @Test
    public void testSetUpdateValue() throws Exception
    {
        SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
        List<UpdateRequest> getRequest = getUpdateRequestList();
        AssetWrapper assetWrapper = assetWrapperService.updateIndividualFields(assetID,
                getRequest);
        assertTrue(assetWrapper.getId().equalsIgnoreCase(assetID));
    }

    private List<UpdateRequest> getUpdateRequestList()
    {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setKey("aName");
        updateRequest.setValue("Matt'S Grill Restaurant");
        UpdateRequest updateRequest2 = new UpdateRequest();
        updateRequest2.setKey("cuisine");
        updateRequest2.setValue("Indian");
        return Arrays.asList(updateRequest, updateRequest2);
    }

    @Test
    public void testSetUpdateValueApi() throws Exception
    {
        List<UpdateRequest> getRequest = getUpdateRequestList();
        this.mockMvc
                .perform(get("/restapi/assetwrapper/search/update/arrayvalue")
                        .param("assetID", assetID)
                        .content(objectMapper.writeValueAsString(getRequest))
                        .header("Authorization", "Basic YXBwVXNlcjphcHBQYXNzd29yZA==")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void testAddNotesandDeleteNotes()
    {
        SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
        Notes notes = new Notes(RandomStringUtils.randomAlphabetic(10), new Date(1441712050500l), 21);
        notesRepository.save(notes);
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setKey("notes");
        updateRequest.setValue(notes);
        AssetWrapper assetWrapper = assetWrapperRepository.findOne(assetID);
        int originalCount = assetWrapper.getNotes().size();
        AssetWrapper restaurants = assetWrapperService.addToFieldArray(assetID,
                Arrays.asList(updateRequest));
        assertTrue(restaurants.getId().equalsIgnoreCase(assetID));
        assertEquals(assetWrapperRepository.findOne(assetID).getNotes().size(),
                originalCount + 1);
        assetWrapperService.removeFromFieldArray(assetID, Arrays.asList(updateRequest));
        assertEquals(assetWrapperRepository.findOne(assetID).getNotes().size(), originalCount);
    }

    @Test
    @Ignore
    public void testAddToFieldArrayApi() throws Exception
    {
        SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
        Notes notes = new Notes(RandomStringUtils.randomAlphabetic(10), new Date(1441712050500l), 21);
        notesRepository.save(notes);
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setKey("notes");
        updateRequest.setValue(notes);
        addFieldToArray(updateRequest);
        removeFieldFromArray(updateRequest);
    }

    private void removeFieldFromArray(UpdateRequest updateRequest)
            throws Exception, JsonProcessingException
    {
        this.mockMvc
                .perform(
                        get("/restapi/assetwrapper/search/update/removeFromFieldArray")
                                .param("assetID", assetID)
                                .content(objectMapper
                                        .writeValueAsString(Arrays.asList(updateRequest)))
                .header("Authorization", "Basic YXBwVXNlcjphcHBQYXNzd29yZA==")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("removeFromFieldArray",
                        requestHeaders(headerWithName("Authorization")
                                .description("Basic auth credentials")),
                        requestParameters(parameterWithName("assetId").description(
                                "The assetId for which Request should be processed"))))
                .andReturn();
    }

    private void addFieldToArray(UpdateRequest updateRequest)
            throws Exception, JsonProcessingException
    {
        this.mockMvc
                .perform(
                        get("/restapi/assetwrapper/search/update/updateArrayValue")
                                .param("assetID", assetID)
                                .content(objectMapper
                                        .writeValueAsString(Arrays.asList(updateRequest)))
                .header("Authorization", "Basic YXBwVXNlcjphcHBQYXNzd29yZA==")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    }
    
    @Test
    public void testFindAsset() throws Exception{
        this.mockMvc
                .perform(get("/restapi/assetwrapper/search/findAsset")
                .param("assetID", assetID)
                .header("Authorization", "Basic YXBwVXNlcjphcHBQYXNzd29yZA==")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    }

}
