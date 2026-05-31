package ru.app.partmatcher.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI partMatcherOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PartMatcher API")
                        .description("Система интеллектуального подбора автозапчастей по VIN-номеру")
                        .version("1.0.0"))
                .externalDocs(new ExternalDocumentation()
                        .description("PartMatcher Documentation")
                        .url("https://example.com/docs"));
    }
}
