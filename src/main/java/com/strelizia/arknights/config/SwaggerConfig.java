package com.strelizia.arknights.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;


/**
 * @author wangzy
 * @Date 2020/11/20 17:01
 **/
@Configuration
@EnableSwagger2
@Profile(value = {"staging", "development"})
/**
 * swagger配置信息
 */
public class SwaggerConfig {

    @Bean
    public Docket api() {
      return new Docket(DocumentationType.SWAGGER_2)
          .enable(false)
          .apiInfo(apiInfo())
          .select()
          .apis(basePackage("com.strelizia.arknights.controller"))
          .paths(PathSelectors.any())
          .build();
  }
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        //页面标题
        .title("Spring Boot Swagger2 构建RESTful API")
        //条款地址
        .termsOfServiceUrl("http://despairyoke.github.io/")
        .contact("zwd")
        .version("1.0")
        //描述
        .description("API 描述")
        .build();
  }
}
