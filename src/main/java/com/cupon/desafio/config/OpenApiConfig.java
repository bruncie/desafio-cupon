package com.cupon.desafio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class OpenApiConfig {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public OpenAPI customOpenAPI() {
        Schema errorSchema = new Schema().type("object");

        Map<String, Schema> props = new LinkedHashMap<>();
        props.put("timestamp", new Schema().type("string").format("date-time"));
        props.put("status", new Schema().type("integer").format("int32"));
        props.put("error", new Schema().type("string"));
        props.put("message", new Schema().type("string"));
        props.put("path", new Schema().type("string"));

        errorSchema.setProperties(props);

        Components components = new Components();
        components.addSchemas("ErrorResponse", errorSchema);

        return new OpenAPI()
                .components(components)
                .info(new Info()
                        .title("Cupon API")
                        .version("v1")
                        .description("API para gerenciar cupons - documentação gerada pelo SpringDoc/OpenAPI")
                );
    }
}
