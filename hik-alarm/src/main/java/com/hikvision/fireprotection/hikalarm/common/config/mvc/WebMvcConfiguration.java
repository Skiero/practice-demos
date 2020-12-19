package com.hikvision.fireprotection.hikalarm.common.config.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfiguration
 *
 * @author wangjinchang5
 * @date 2020/12/18 21:20
 * @since 1.0.100
 */
@Configuration
public class WebMvcConfiguration {

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(@SuppressWarnings("NullableProblems") ResourceHandlerRegistry registry) {
                registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                        "classpath:/META-INF/resources/");
                registry.addResourceHandler("/webjars/**").addResourceLocations(
                        "classpath:/META-INF/resources/webjars/");

                registry.addResourceHandler("index.html").addResourceLocations(
                        "classpath:/resources/static/");
                registry.addResourceHandler("/static/**").addResourceLocations(
                        "classpath:/resources/static/");
            }

            @Override
            public void addCorsMappings(@SuppressWarnings("NullableProblems") CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
                        .maxAge(3600);
            }
        };
    }
}
