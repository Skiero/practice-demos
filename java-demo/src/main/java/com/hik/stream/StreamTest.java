package com.hik.stream;

import com.google.common.collect.Lists;
import com.hik.model.User;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Stream练习
 *
 * @author wangjinchang5
 * @date 2020/8/4 11:22
 */
public class StreamTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamTest.class);

    List<User> userList = new ArrayList<>();

    @Before
    public void before() {
        // 创建5个User，用于Stream的测试
        int count = 5;
        for (int i = 0; i < count; i++) {
            User user = new User("李" + i, i % 2 == 0 ? 0 : 1, i);
            userList.add(user);
        }
    }

    @Test
    public void filter() {
        // filter方法的参数是断言，用来做条件筛选，返回符合断言的流
        long count = userList.stream().filter(e -> e.getAge() > 3).count();
        LOGGER.info("年龄大于3的人数：{}", count);
    }

    @Test
    public void map() {
        // map方法的参数是function，传入一个参数，返回一个map返回对象的流
        long count = userList.stream().mapToInt(User::getAge).filter(e -> e < 3).count();
        LOGGER.info("年龄小于3的人数：{}", count);
    }

    @Test
    public void distinct() {
        // distinct方法是无参的，可以去重
        long count = userList.stream().mapToInt(User::getGender).distinct().count();
        LOGGER.info("性别类型（去重）的数量：{}", count);
    }

    @Test
    public void collectToList() {
        // collect方法用来将stream收集为list
        List<Integer> genderList = userList.stream().map(User::getGender).distinct().collect(Collectors.toList());
        LOGGER.info("去重了的性别类型List：{}", genderList);

        // collect方法用来将stream收集为set
        Set<Integer> genderSet = userList.stream().map(User::getGender).collect(Collectors.toSet());
        LOGGER.info("性别类型Set：{}", genderSet);

        // collect方法将stream收集为list，如果元素为null，也可以收集
        userList.add(new User());
        List<String> nameList = userList.stream().map(User::getName).collect(Collectors.toList());
        LOGGER.info("包含null元素的姓名List：{}", nameList);
    }

    /**
     * <ul>stream转map一般有2个问题
     *
     * <li>IllegalStateException caused by key is Duplicate</li>
     * <ul>key重复的解决方案有3个
     *
     * <li>1覆盖，即后者覆盖前者，或者前者覆盖后者
     * <pre><code> (e1,e2)->e1; or (e1,e2)->e2; </code></pre>
     * </li>
     *
     * <li>2拼接，多用于字符串
     * <pre><code (e1,e2)->e1+","+e2; </code></pre>
     * </li>
     *
     * <li>3组合成集合</li>
     * </ul>
     *
     * <li>NullPointerException caused by value is Null</li>
     * <ul>value为null的解决方案有2个
     *
     * <li>1转为非null</li>
     *
     * <li>2使用list等支持null值的集合</li>
     * </ul>
     * </ul>
     */
    @Test
    public void collectToMap() {
        // collect方法用来将stream收集为map（根据名称映射）
        Map<String, List<User>> nameMap = userList.stream().collect(Collectors.groupingBy(User::getName));
        LOGGER.info("根据 姓名->用户 映射（groupingBy方法）：{}", nameMap);

        // collect方法用来将stream收集为map（key为name，value为gender）
        Map<String, Integer> genderMap = userList.stream().collect(Collectors.toMap(User::getName, User::getGender));
        LOGGER.info("根据 姓名->性别 映射（toMap方法）：{}", genderMap);

        // collect方法用来处理key值重复的情况，将stream收集为map
        Map<Integer, String> duplicateGenderMap = new HashMap<>(1);
        try {
            duplicateGenderMap = userList.stream().collect(Collectors.toMap(User::getGender, User::getName));
        } catch (IllegalStateException e) {
            LOGGER.error("根据 性别（key重复）->姓名 映射：{}", duplicateGenderMap, e);
        }

        duplicateGenderMap = userList.stream().collect(Collectors.toMap(User::getGender, User::getName, (e1, e2) -> e2));
        LOGGER.info("根据 性别（后者代替前者）->姓名 映射：{}", duplicateGenderMap);

        // collect方法用来处理key值重复的情况，将stream收集为map
        Map<Integer, List<String>> listMap = userList.stream().collect(Collectors.toMap(User::getGender, e -> {
            List<String> nameList = new ArrayList<>();
            nameList.add(e.getName());
            return nameList;
        }, (e1, e2) -> {
            e1.addAll(e2);
            return e1;
        }));
        LOGGER.info("根据性别（如果有重复则合并为集合）和姓名映射：{}", listMap);

        // collect方法用来处理value为null的情况，将stream收集为map（key为null时不影响，因为map的merge方法只判断value的值）
        userList.add(new User());
        Map<String, User> nullNameMap1 = userList.stream().collect(Collectors.toMap(User::getName, e -> e));
        LOGGER.info("根据 姓名（key为null）->用户 映射：{}", nullNameMap1);

        Map<String, Integer> nullNameMap2 = new HashMap<>(1);
        try {
            nullNameMap2 = userList.stream().collect(Collectors.toMap(User::getName, User::getAge));
        } catch (NullPointerException e) {
            LOGGER.error("根据 姓名->年龄（value为null） 映射：{}", nullNameMap2, e);
        }

        // collect将stream收集为map，value为null时给定默认值
        Map<String, Integer> nullNameMap3 = userList.stream()
                .collect(Collectors.toMap(User::getName, e -> Objects.isNull(e.getAge()) ? -1 : e.getAge()));
        LOGGER.info("根据 姓名->年龄（value有默认值） 映射：{}", nullNameMap3);

        // collect将stream收集为map，value为null时转换为非null元素
        Map<String, ArrayList<Integer>> nullNameMap4 = userList.stream().collect(Collectors.toMap(User::getName, e -> Lists.newArrayList(e.getAge()), (e1, e2) -> {
            e1.addAll(e2);
            return e1;
        }));
        LOGGER.info("根据 姓名->年龄（value转为list） 映射：" + nullNameMap4);
    }

    @Test
    public void reduce() {
        // reduce以0为计数起始值，然后求和
        Integer count1 = userList.stream().map(User::getAge).peek(System.out::println).reduce(0, Integer::sum);
        LOGGER.info("用户的年龄总和（起始为0）是：{}", count1);

        // reduce以1为起始值，然后求stream的和（以自定义的起始值进行计算）
        Integer count2 = userList.stream().map(User::getAge).reduce(1, Integer::sum);
        LOGGER.info("用户的年龄总和（起始值为1）是：{}", count2);

        // reduce以1为默认值，然后求stream的最大值（和默认值比较）
        Integer count3 = userList.stream().map(User::getAge).reduce(1, Integer::max);
        LOGGER.info("用户的年龄总和（默认值为1）是：{}", count3);

        // reduce获取stream中年龄最大的用户
        Optional<User> optional = userList.stream().reduce(BinaryOperator.maxBy(Comparator.comparing(User::getAge)));
        optional.ifPresent(e -> LOGGER.info("用户的年龄最大（无默认值）的是：{}", e));

        // reduce获取stream中年龄最大的用户，和默认值进行比较，选最大值
        User user = userList.stream().reduce(new User("user", 0, 10),
                BinaryOperator.maxBy(Comparator.comparing(User::getAge)));
        LOGGER.info("用户的年龄最大（有默认值）的是：{}", user);
    }
}
