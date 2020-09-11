package com.hik.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author wangjinchang5
 */
@EnableSwagger2
@SpringBootApplication
public class WebDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebDemoApplication.class, args);
    }

}
