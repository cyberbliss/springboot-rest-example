package uk.co.cyberbliss;

import nl.tritales.springrestdoc.SpringRestDoc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = App.class, loader = SpringApplicationContextLoader.class)
@WebAppConfiguration
public class SpringRestDocAPIDocumentationTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void generateDocsInJsonFormattest() throws Exception {
        final Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        final SpringRestDoc springRestDoc = new SpringRestDoc(handlerMethods);
        System.out.println(springRestDoc.json(true));
    }

    @Test
    public void generateDocsInAsciiDocFormatTest() throws Exception {
        final Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        final String sourceFolder = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "java";
        final SpringRestDoc springRestDoc = new SpringRestDoc(handlerMethods, new File(sourceFolder));

        final File outputFolder = new File(System.getProperty("user.dir") + File.separator + "target" + File.separator + "generated-ascii-doc");
        outputFolder.mkdirs();
        final FileWriter writer = new FileWriter(new File(outputFolder, "spring-restdoc.asciidoc"));
        springRestDoc.asciidoc(writer);
    }
}
