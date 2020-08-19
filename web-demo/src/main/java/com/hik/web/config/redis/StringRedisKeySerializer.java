package com.hik.web.config.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 自定义 redis_key 序列化方式
 * <p>StringRedisKeySerializer 支持有前缀的序列化，如增加组件id，如果仅是序列化为string，请使用 {@link StringRedisSerializer}</p>
 *
 * @author wangjinchang5
 * @date 2020/8/18 19:37
 */
public class StringRedisKeySerializer extends StringRedisSerializer {

    private final String prefix;

    public StringRedisKeySerializer(String prefix) {
        super();
        // ":"可以将key以目录的形式分隔
        this.prefix = prefix.concat(":");
    }

    @Override
    public String deserialize(byte[] bytes) {
        String redisKey = super.deserialize(bytes);
        return StringUtils.isBlank(redisKey) ? prefix : redisKey.substring(prefix.length());
    }

    @Override
    public byte[] serialize(String string) {
        String redisKey = StringUtils.isBlank(string) ? prefix : prefix.concat(string);
        return super.serialize(redisKey);
    }
}
