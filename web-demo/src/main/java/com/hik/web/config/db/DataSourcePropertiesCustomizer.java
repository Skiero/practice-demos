package com.hik.web.config.db;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Druid数据源配置参数
 *
 * @author wangjinchang5
 * @date 2020/8/19 10:41
 */
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourcePropertiesCustomizer {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
