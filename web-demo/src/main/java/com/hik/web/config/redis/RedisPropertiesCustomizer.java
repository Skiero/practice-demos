package com.hik.web.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis配置文件
 *
 * @author wangjinchang5
 * @date 2020/8/18 16:35
 */
@ConfigurationProperties(prefix = "spring.redis")
public class RedisPropertiesCustomizer {
    private int database = 0;
    private String host = "localhost";
    private int port = 6379;
    private String password;
    private int maxActive = 8;
    private int maxIdle = 8;
    private int minIdle = 0;
    private int maxWait = -1;
    private boolean customize;

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public boolean isCustomize() {
        return customize;
    }

    public void setCustomize(boolean customize) {
        this.customize = customize;
    }
}
