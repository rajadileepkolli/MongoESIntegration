package com.digitalbridge.mongodb.repository;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

import com.digitalbridge.DigitalBridgeApplicationTests;

public class NotesRepositoryTests extends DigitalBridgeApplicationTests
{

    @Test
    public void notesListExample() throws Exception
    {
        this.mockMvc
                .perform(get("/restapi/notes")
                        .header("Authorization","Basic YXBwVXNlcjphcHBQYXNzd29yZA=="))
                .andExpect(status().isOk())
                .andDo(document("notes-list-example",
                        links(
                                linkWithRel("first").description("The <<notes-index-first, Notes resource>>"),
                                linkWithRel("self").description("The <<resources-notes-self,Notes resource>>"),
                                linkWithRel("next").description("The <<resources-notes-next,Notes resource>>"),
                                linkWithRel("last").description("The <<resources-notes-last,Notes resource>>"),
                                linkWithRel("profile").description("The <<resources-notes-profile,Notes resource>>")),
                        responseFields(
                                fieldWithPath("_embedded.notes").description("An array of <<resources-notes,Notes resource>>"),
                                fieldWithPath("page").description("The <<resources-response-page, Notes resource>>"),
                                fieldWithPath("_links").description("The <<notes-index-links, Links>> to other resources"))))
                .andReturn();
    }
    
    @Test
    public void notesFirstLink() throws Exception
    {
        this.mockMvc
                .perform(get("/restapi/notes?page=0&size=20")
                        .header("Authorization","Basic YXBwVXNlcjphcHBQYXNzd29yZA=="))
                .andExpect(status().isOk())
                .andReturn();
    }

}
