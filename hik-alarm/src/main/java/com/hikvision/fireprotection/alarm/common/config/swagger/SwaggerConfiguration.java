package com.hikvision.fireprotection.alarm.common.config.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * SwaggerConfiguration
 *
 * @author wangjinchang5
 * @date 2020/12/18 19:40
 * @since 1.0.100
 */
@ConditionalOnExpression("${hik.config.service.swagger:false}==true")
@EnableSwagger2
@Configuration
public class SwaggerConfiguration {
    @Value("${hik.config.service.name:unknown}")
    private String name;
    @Value("${hik.config.service.version:1.0.100}")
    private String version;

    @Bean
    public Docket webRestApi() {
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(401).message("Unauthorized").build());
        responseMessageList.add(new ResponseMessageBuilder().code(403).message("Forbidden").build());
        responseMessageList.add(new ResponseMessageBuilder().code(404).message("Not Found").build());
        responseMessageList.add(new ResponseMessageBuilder().code(500).message("Server Error").build());

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("1.alarm_web")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hikvision.fireprotection"))

                //不匹配以/api/开头的接口路径
                .paths(PathSelectors.regex("^((?!/api/).)*$"))
                .build()
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(name + "服务接口")
                .description(name + "API接口描述")
                .version(version)
                .contact(new Contact("hikDeveloper", "", "hikDeveloper@hikvision.com.cn"))
                .build();
    }
}
