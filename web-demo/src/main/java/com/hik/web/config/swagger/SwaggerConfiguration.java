package com.hik.web.config.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger配置类
 *
 * @author wangjinchang5
 * @date 2020/9/10 15:07
 */
@Configuration
public class SwaggerConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "spring.profiles", name = "active", havingValue = "dev")
    public Docket testDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("test")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hik.web.controller.test"))
                // 所有path都会展示
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.profiles", name = "active", havingValue = "dev")
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("controller")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hik.web.controller"))
                // 根据 ^((?!/test).)*$ 进行过滤，如果path中不包含 /test 的才展示
                .paths(PathSelectors.regex("^((?!/test).)*$"))
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.profiles", name = "active", havingValue = "pro")
    public Docket proDocket() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.none()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot Demo")
                .description("Spring Boot 练习demo")
                .termsOfServiceUrl("http://www.feisuzhibo.net/")
                .contact(new Contact("uuid", "https://hub.fastgit.org/", "edckb03@126.com"))
                .version("1.0")
                .build();
    }
}
