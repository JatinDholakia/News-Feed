package work.jatin.newsfeed.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("News Feed")
                        .description("A news feed for your social networking application")
                        .version("0.0.1")
                        .contact(new Contact()
                                .name("Jatin Dholakia")
                                .email("dholakia98@gmail.com")));
    }
}