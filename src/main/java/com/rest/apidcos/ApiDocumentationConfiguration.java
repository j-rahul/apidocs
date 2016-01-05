package com.rest.apidcos;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
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
          .directModelSubstitute(LocalDate.class,
              String.class)
          .genericModelSubstitutes(ResponseEntity.class)
          .alternateTypeRules(
              newRule(typeResolver.resolve(DeferredResult.class,
                      typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                  typeResolver.resolve(WildcardType.class)))
          .useDefaultResponseMessages(false)
          .globalResponseMessage(RequestMethod.GET,
              newArrayList(new ResponseMessageBuilder()
                  .code(500)
                  .message("500 message")
                  .responseModel(new ModelRef("Error"))
                  .build()))
//          .securitySchemes(newArrayList(apiKey()))
//          .securityContexts(newArrayList(securityContext()))
          .enableUrlTemplating(true)
//          .globalOperationParameters(
//              newArrayList(new ParameterBuilder()
//                  .name("someGlobalParameter")
//                  .description("Description of someGlobalParameter")
//                  .modelRef(new ModelRef("string"))
//                  .parameterType("query")
//                  .required(true)
//                  .build()))
          ;
    }

    @Bean
    public UiConfiguration uiConfig() {
      return UiConfiguration.DEFAULT;
    }

    private ApiInfo metadata() {
      return new ApiInfoBuilder()
        .title("My awesome API")
        .description("Some description")
        .version("1.0")
        .contact("my-email@domain.org")
        .build();
    }
}
