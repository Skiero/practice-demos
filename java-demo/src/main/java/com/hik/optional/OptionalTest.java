package com.hik.optional;

import com.hik.model.User;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * optional练习
 *
 * @author wangjinchang5
 * @date 2020/8/4 11:33
 */
public class OptionalTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptionalTest.class);

    Optional<Integer> optional = Optional.empty();

    @Before
    public void before() {
        int random = RandomUtils.nextInt(1, 3);
        boolean b = random % 2 == 0;
        LOGGER.info("随机数为{}，optional{}空", random, b ? "不为" : "为");
        if (b) {
            optional = Optional.of(random);
        }
    }

    @Test
    public void present() {
        // ifPresent方法在optional存在时执行
        optional.ifPresent(e -> LOGGER.info("optional中的元素为：{}", e));
        // isPresent方法返回布尔值
        LOGGER.info("optional{}空", optional.isPresent() ? "不为" : "为");
    }

    @Test
    public void orElse() {
        // ifPresent方法为，如果存在，则获取该值，否则不操作
        optional.ifPresent(e -> LOGGER.info("optional.ifPresent = {}", e));
        // orElse方法为，如果不存在，则返回else的值
        LOGGER.info("optional.orElse = {}", optional.orElse(-1));
        // orElse方法的参数是Supplier
        LOGGER.info("optional.orElseGet = {}", optional.orElseGet(() -> -1));
    }

    @Test
    public void map() {
        // map方法是if-else的简化，如果optional不存在，返回empty，如果存在，返回function中的值
        Optional<User> userOptional = optional.map(e -> new User());
        try {
            LOGGER.info("optional不为空则userOptional为user：{}", userOptional.get());
        } catch (NoSuchElementException e) {
            LOGGER.error("optional为空导致userOptional也为空", e);
        }
    }
}
