package com.hik.collection;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hik.model.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Map练习
 *
 * @author wangjinchang5
 * @date 2020/8/7 14:09
 */
public class MapTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapTest.class);

    /**
     * 通过Map.Entry遍历，适用于获取键值
     */
    @Test
    public void iteratorByEntry() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put(1, "苹果");
        map.put(2, "香蕉");
        map.put(3, "橘子");
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            LOGGER.info("map中的数据：{}={}", key, value);
        }
    }

    /**
     * 通过keySet、values分别获取key的Set，value的Collection，适用于获取建或者获取值，如果要获取键值对，建议使用 {@link #iteratorByEntry()}
     */
    @Test
    public void iteratorByFor() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put(1, "苹果");
        map.put(2, "香蕉");
        map.put(3, "橘子");
        for (Object key : map.keySet()) {
            LOGGER.info("map中的key：{}", key);
        }

        for (Object value : map.values()) {
            LOGGER.info("map中的value：{}", value);
        }
    }

    /**
     * 先获取map的key，然后根据key查询map的value，该方法效率低，建议使用 {@link #iteratorByEntry()}
     */
    @Test
    public void iteratorByKey() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put(1, "苹果");
        map.put(2, "香蕉");
        map.put(3, "橘子");
        for (Object key : map.keySet()) {
            LOGGER.info("map中的key：{}，value：{}", key, map.get(key));
        }
    }

    /**
     * 通过Iterator迭代器进行遍历，其本质和通过Map.Entry遍历是一样的，建议使用 {@link #iteratorByEntry()}
     */
    @Test
    public void iteratorByIterator() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put(1, "苹果");
        map.put(2, "香蕉");
        map.put(3, "橘子");
        Iterator<Map.Entry<Object, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            LOGGER.info("map中的数据：{}={}", key, value);
        }
    }

    @Test
    public void testMaps() {
        HashSet<String> hashSet = Sets.newHashSet("zs", "ls", "ww", "zl");
        // 根据Set映射成Map，第二个参数是function，用于构造value
        System.out.println(" ----  使用asMap方法将Set映射为Map  ---- ");
        Map<String, User> nameUserMap = Maps.asMap(hashSet, e -> new User(e, 0, 1));
        nameUserMap.forEach((k, v) -> LOGGER.info("asMap中的数据：{}={}", k, v));
        System.out.println(" ----  使用toMap方法将Set映射为Map  ---- ");
        ImmutableMap<String, User> toMap = Maps.toMap(hashSet, e -> new User(e, 0, 1));
        toMap.forEach((k, v) -> LOGGER.debug("toMap中的数据：{}={}", k, v));

        HashMap<String, User> hashMap = Maps.newHashMap();
        hashMap.put("zs", new User("zs", 0, 1));
        hashMap.put("ls", new User("lz", 1, 2));
        hashMap.put("ww", new User("ww", 0, 3));
        hashMap.put("zl", new User("zl", 1, 4));
        // 根据entry过滤map
        System.out.println(" ----  根据Entry筛选：key包含z或value的性别为0的Map  ---- ");
        Map<String, User> filterEntries = Maps.filterEntries(hashMap, e -> 0 == e.getValue().getGender() || e.getKey().contains("z"));
        filterEntries.forEach((k, v) -> LOGGER.info("filterEntries中的数据：{}={}", k, v));

        // 根据key过滤map
        System.out.println(" ----  根据key筛选：key包含l的Map  ---- ");
        Map<String, User> filterKeys = Maps.filterKeys(hashMap, e -> e.contains("l"));
        filterKeys.forEach((k, v) -> LOGGER.info("filterKeys中的数据：{}={}", k, v));

        // 根据value过滤map
        System.out.println(" ----  根据value筛选：value的性别为0的Map  ---- ");
        Map<String, User> filterValues = Maps.filterValues(hashMap, e -> 0 == e.getGender());
        filterValues.forEach((k, v) -> LOGGER.info("filterValues中的数据：{}={}", k, v));

        // 将Properties映射为immutableMap
        System.out.println(" ----  fromProperties：根据Properties映射为ImmutableMap  ---- ");
        Properties properties = new Properties();
        properties.setProperty("name", "zhangSan");
        properties.setProperty("age", "18");
        ImmutableMap<String, String> immutableMap = Maps.fromProperties(properties);
        immutableMap.forEach((k, v) -> LOGGER.info("immutableMap中的数据：{}={}", k, v));

        // 根据value转换成新的map，注意该方法和filter的区别：filter是根据Predicate来匹配符合条件的数据，transform是根据Function创建信息对象
        System.out.println(" ----  transformValues：第一个参数为Map，第二个参数为function，映射结果为key为Map的key，value为function返回值  ---- ");
        Map<String, Integer> transformValues = Maps.transformValues(hashMap, User::getAge);
        transformValues.forEach((k, v) -> LOGGER.info("transformValues中的数据：{}={}", k, v));

        // 根据Iterable来创建map，Iterable作为value，function返回是对象是key
        System.out.println(" ----  uniqueIndex： 第一个参数为Iterable，第二个参数为function，映射结果为key为function返回值，value为Iterable ---- ");
        ImmutableMap<User, String> uniqueIndex = Maps.uniqueIndex(hashSet, e -> new User(e, 1, 0));
        uniqueIndex.forEach((k, v) -> LOGGER.info("uniqueIndex中的数据：{}={}", k, v));
    }
}
