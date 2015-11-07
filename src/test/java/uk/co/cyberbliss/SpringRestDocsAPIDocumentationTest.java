package uk.co.cyberbliss;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.http.MediaType;
import org.springframework.restdocs.response.ResponsePostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.restdocs.RestDocumentation.document;
import static org.springframework.restdocs.RestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.RestDocumentation.modifyResponseTo;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.LinkExtractors.halLinks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = App.class, loader = SpringApplicationContextLoader.class)
@WebAppConfiguration
public class SpringRestDocsAPIDocumentationTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(documentationConfiguration())
                .build();
    }

    @Test
    public void testAndDocumentGetBooks() throws Exception {
        this.mockMvc.perform(get("/api/books").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(modifyResponseTo(ResponsePostProcessors.prettyPrintContent())
                        .andDocument("get_a_list_of_books").withResponseFields(
                            fieldWithPath("[]isbn").description("The Book's ISBN"),
                            fieldWithPath("[]title").description("The Book's title"),
                            fieldWithPath("[]author").description("The Book's author"),
                            fieldWithPath("[]_links").description("<<resources-index-links,Links>> to resources")
                ));
    }

    @Test
    public void testAndDocumentGetBook() throws Exception {
        Book book = new Book("isbn-t1","test title","test author");
        mockMvc.perform(post("/api/book")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJson(book)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/book/isbn-t1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(book.getTitle())))
                .andDo(modifyResponseTo(ResponsePostProcessors.prettyPrintContent())
                        .andDocument("get_details_for_a_book")
                        .withResponseFields(
                                fieldWithPath("isbn").description("The Book's ISBN"),
                                fieldWithPath("title").description("The Book's title"),
                                fieldWithPath("author").description("The Book's author"),
                                fieldWithPath("_links").description("<<resources-index-links,Links>> to resources")
                        )
                        .withLinks(halLinks(),
                                linkWithRel("self").description("Link to this resource")
                        ));
    }

    @Test
    public void testAndDocumentAddBook() throws Exception {
        Book book = new Book("isbn-t2","add test title","addtest author");
        mockMvc.perform(post("/api/book")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJson(book)))
                .andExpect(status().isCreated())
                .andDo(modifyResponseTo(ResponsePostProcessors.prettyPrintContent())
                        .andDocument("add_a_new_book")
                            .withRequestFields(
                                    fieldWithPath("isbn").description("The Book's ISBN"),
                                    fieldWithPath("title").description("The Book's title"),
                                    fieldWithPath("author").description("The Book's author"),
                                    fieldWithPath("links").optional().description("Only used with response")
                            )
                            .withResponseFields(
                                    fieldWithPath("isbn").description("The Book's ISBN"),
                                    fieldWithPath("title").description("The Book's title"),
                                    fieldWithPath("author").description("The Book's author"),
                                    fieldWithPath("_links").description("<<resources-index-links,Links>> to resources")
                            )
                            .withLinks(halLinks(),
                                    linkWithRel("self").description("Link to this resource"),
                                    linkWithRel("Books").description("Link to get all Books")
                            )

                );
    }

    @Test
    public void testAndDocumentUpdateBook() throws Exception {
        Book book = new Book("isbn-t3","update test title","updatetest author");
        mockMvc.perform(post("/api/book")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJson(book)))
                .andExpect(status().isCreated());

        book.setTitle("An updated title");
        book.setIsbn(null);

        mockMvc.perform(put("/api/book/isbn-t3")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJson(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(book.getTitle())))
                .andDo(modifyResponseTo(ResponsePostProcessors.prettyPrintContent())
                        .andDocument("update_details_for_a_book")
                                .withRequestFields(
                                        fieldWithPath("isbn").description("Leave this field blank"),
                                        fieldWithPath("title").description("The Book's title"),
                                        fieldWithPath("author").description("The Book's author"),
                                        fieldWithPath("links").optional().description("Only used with response")
                                )
                                .withResponseFields(
                                        fieldWithPath("isbn").description("The Book's ISBN"),
                                        fieldWithPath("title").description("The Book's title"),
                                        fieldWithPath("author").description("The Book's author"),
                                        fieldWithPath("_links").description("<<resources-index-links,Links>> to resources")
                                )
                                .withLinks(halLinks(),
                                        linkWithRel("self").description("Link to this resource"),
                                        linkWithRel("Books").description("Link to get all Books")
                                )

                );
    }

    @Test
    public void testAndDocumentDeleteBook() throws Exception {
        Book book = new Book("isbn-t4","delete test title","deletetest author");
        mockMvc.perform(post("/api/book")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJson(book)))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/book/isbn-t4"))
                .andExpect(status().isOk())
                .andDo(document("delete_a_book"));

        mockMvc.perform(get("/api/book/isbn-t4")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private static byte[] convertObjectToJson(Object source) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(source);
    }
}
