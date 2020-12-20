package com.hikvision.fireprotection.alarm;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author wangjinchang5
 * @date 2020/12/18 16:00
 * @since 1.0.100
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HikAlarmApplication.class);
    }

}
