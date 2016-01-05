package com.rest.apidocs;

import static com.google.common.collect.Lists.newArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;

@Configuration
public class ApiDocumentationConfiguration {
    
    @Autowired
    private TypeResolver typeResolver;
    
    @Bean
    public Docket petApi() {
      return new Docket(DocumentationType.SWAGGER_2)
          .select()
            .apis(RequestHandlerSelectors.any())
            .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
            .paths(PathSelectors.any())
            .build()
          .pathMapping("/")
          .globalResponseMessage(RequestMethod.GET,
              newArrayList(new ResponseMessageBuilder()
                  .code(500)
                  .message("Internal Server Error")
                  .responseModel(new ModelRef("Error"))
                  .build()))
             .apiInfo(apiInfo()) 
          ;
    }

    @Bean
    public UiConfiguration uiConfig() {
      return UiConfiguration.DEFAULT;
    }

    private ApiInfo apiInfo() {
      return new ApiInfoBuilder()
        .title("My awesome API")
        .description("My awesome API uses springfox for document automation")
        .version("1.0")
        .contact("rjain6@buffalo.edu")
        .build();
    }
}
