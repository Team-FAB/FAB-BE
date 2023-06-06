package com.fab.banggabgo.config;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
  @Value("${jwt.auth.atk}")
  String ATK;
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.OAS_30)
        .useDefaultResponseMessages(false)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())
        .securityContexts(Collections.singletonList(securityContext()))
        .securitySchemes(List.of(apiKey()));
  }

  public ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("방갑고")
        .description("방을 쉐어할 룸메이트 매칭서비스 :)")
        .version("1.0")
        .build();
  }

  private SecurityContext securityContext() {
    return springfox
        .documentation
        .spi.service
        .contexts
        .SecurityContext
        .builder()
        .securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
  }

  List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return List.of(new SecurityReference(ATK, authorizationScopes));
  }

  private ApiKey apiKey() {
    return new ApiKey(ATK, ATK, "header");
  }

}
