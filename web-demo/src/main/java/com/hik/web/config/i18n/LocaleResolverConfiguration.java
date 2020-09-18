package com.hik.web.config.i18n;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 多语言解析器配置类
 *
 * @author wangjinchang5
 * @date 2020/9/18 14:57
 */
@Configuration
public class LocaleResolverConfiguration {

    @Bean
    public LocaleResolver localeResolver() {
        return new LocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest httpServletRequest) {
                // HTTP请求头中，携带language ，如果language不存在，就使用默认的Locale，否则就根据请求头切换Locale
                String language = httpServletRequest.getHeader("language");
                Locale locale;
                if (StringUtils.isNotBlank(language)) {
                    try {
                        // language必须使用zh_CN格式
                        String[] split = language.split("_");
                        locale = new Locale(split[0], split[1]);
                    } catch (RuntimeException e) {
                        locale = Locale.getDefault();
                    }
                } else {
                    locale = Locale.getDefault();
                }
                return locale;
            }

            @Override
            public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                  Locale locale) {

            }
        };
    }
}
