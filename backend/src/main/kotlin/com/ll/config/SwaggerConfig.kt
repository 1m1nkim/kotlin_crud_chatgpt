package com.ll.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Posts")
                    .description("PostApi")
            )
    }

    @Bean
    fun postGroupedOpenApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("Post")
            .pathsToMatch("/api/posts/**")
            .build()
    }
}