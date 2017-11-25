package com.waes.assignment.waesdifferentiator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.waes.assignment.waesdifferentiator"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Assignment Scalable Web - WAES",
                "",
                "1.0",
                "urn:tos",
                new Contact("Lucas Gentile", "", ""),
                "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<VendorExtension>());
    }
}