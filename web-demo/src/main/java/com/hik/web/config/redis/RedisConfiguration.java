package com.hik.web.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hik.web.constant.RedisConstant;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置
 *
 * @author wangjinchang5
 * @date 2020/8/18 14:23
 */
@Configuration
@EnableConfigurationProperties(RedisPropertiesCustomizer.class)
public class RedisConfiguration {

    private final RedisPropertiesCustomizer redisProperties;

    public RedisConfiguration(RedisPropertiesCustomizer redisProperties) {
        this.redisProperties = redisProperties;
    }

    /**
     * spring boot auto config redis，创建LettuceConnectionFactory时使用RedisProperties的配置，如果配置中涉及敏感信息，可以使用以下方式进行配置，仅需配置RedisProperties，后续自动配置会由spring boot完成
     */
    @Bean
    @Primary
    public RedisProperties redisProperties(RedisProperties redisProperties) {
        // 配置中的敏感信息通过读取其他配置获取，切勿在yml中配置，否则会覆盖该处的配置
        redisProperties.setDatabase(this.redisProperties.getDatabase());
        redisProperties.setHost(this.redisProperties.getHost());
        redisProperties.setPort(this.redisProperties.getPort());
        redisProperties.setPassword(this.redisProperties.getPassword());
        return redisProperties;
    }

    /**
     * 配置文件中，前缀是spring.redis，Property是customize，值为true时，才注册该bean
     */
    @ConditionalOnProperty(prefix = "spring.redis", name = "customize", havingValue = "true")
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        // 单机版redis配置
        RedisStandaloneConfiguration standaloneConfiguration =
                new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        standaloneConfiguration.setDatabase(redisProperties.getDatabase());

        // Lettuce客户端配置 这是简单版的配置，spring boot auto config 见 LettuceConnectionConfiguration的getLettuceClientConfiguration方法
        GenericObjectPoolConfig<?> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(redisProperties.getMaxActive());
        poolConfig.setMaxIdle(redisProperties.getMaxIdle());
        poolConfig.setMinIdle(redisProperties.getMinIdle());
        poolConfig.setMaxWaitMillis(redisProperties.getMaxWait());
        // poolConfig(GenericObjectPoolConfig poolConfig)的参数是GenericObjectPoolConfig，所以要加对应的依赖 <!-- https://mvnrepository.com/artifact/org.apache.commons/commons-pool2 -->
        LettucePoolingClientConfiguration clientConfiguration =
                LettucePoolingClientConfiguration.builder().poolConfig(poolConfig).build();

        // 将使用自定义的redis配置创建LettuceConnectionFactory，注意，如果在yml中配置了pool的参数，spring boot将会自动配置LettuceConnectionFactory
        return new LettuceConnectionFactory(standaloneConfiguration, clientConfiguration);
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // DefaultSerializer序列化方式默认是JdkSerializationRedisSerializer
        RedisTemplate<Object, Object> template = new RedisTemplate<>();

        // key的序列化方式由 JdkSerializationRedisSerializer 改为 StringRedisSerializer（编码默认为UTF_8）
        template.setKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jsonRedisSerializer.setObjectMapper(objectMapper);
        // DefaultSerializer序列化方式设置为 Jackson2JsonRedisSerializer，会将value等以json的格式序列化
        template.setDefaultSerializer(jsonRedisSerializer);

        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean(name = "stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // StringRedisTemplate的KeySerializer、ValueSerializer均是StringRedisSerializer，可以参考空参构造器
        StringRedisTemplate template = new StringRedisTemplate();

        // 将key的序列化方式KeySerializer，定制为带有前缀StringRedisKeySerializer
        template.setKeySerializer(new StringRedisKeySerializer(RedisConstant.COMPONENT_PREFIX));
        // StringRedisTemplate相当于RedisTemplate将DefaultSerializer设置为StringRedisSerializer，即value只能存入string类型的数据，否则抛出类型转换异常，默认编码为UTF_8
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
