package uk.co.cyberbliss;

import io.github.robwin.swagger.test.SwaggerAssert;
import io.github.robwin.swagger.test.SwaggerAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = App.class, loader = SpringApplicationContextLoader.class)
@WebAppConfiguration
public class VerifySwaggerContractTest {

    @Test
    public void validateImplementationAgainstDesignSpec(){
        String designFirstSwaggerSpec = SwaggerAssert.class.getClassLoader().getResource("swagger/swagger-spec.yaml").getPath();
        SwaggerAssertions.assertThat("http://localhost:9080/v2/api-docs").isEqualTo(designFirstSwaggerSpec);
    }
}
