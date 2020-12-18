package com.hikvision.fireprotection.hikalarm.common.config.mvc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(@SuppressWarnings("NullableProblems") ResourceHandlerRegistry registry) {
                registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                        "classpath:/META-INF/resources/");
                registry.addResourceHandler("/webjars/**").addResourceLocations(
                        "classpath:/META-INF/resources/webjars/");
            }
        };
    }

    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
        return new WebSecurityConfigurerAdapter() {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests()
                        .mvcMatchers((contextPath + "/**")).permitAll()
                        .anyRequest().authenticated()
                        .and().csrf().disable();
            }
        };
    }
}
