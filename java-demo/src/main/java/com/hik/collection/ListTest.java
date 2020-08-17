package com.hik.collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * List练习
 * <p>guava相关的用法，可以参考 guava集合工具 博文</p>
 *
 * @author wangjinchang5
 * @date 2020/8/7 14:08
 * @see <a href = "https://blog.csdn.net/weixin_41835612/article/details/83646257">guava集合工具</a>
 */
public class ListTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListTest.class);

    @Test
    public void iteratorList() {
        ArrayList<String> stringList = Lists.newArrayList("tom", "jack", "marry");

        // 通过 for i 遍历（顺序）
        System.out.println(" ----  顺序遍历  ---- ");
        for (int i = 0; i < stringList.size(); i++) {
            LOGGER.info("stringList中的第{}个元素是[{}]", i + 1, stringList.get(i));
        }

        // 通过 for r 遍历（倒序）
        System.out.println(" ----  倒序遍历  ---- ");
        for (int i = stringList.size() - 1; i >= 0; i--) {
            LOGGER.info("stringList中的第{}个元素是[{}]", i + 1, stringList.get(i));
        }

        // 通过 超级for循环 遍历
        System.out.println(" ----  超级for循环遍历  ---- ");
        for (String item : stringList) {
            LOGGER.info("stringList中的元素是[{}]", item);
        }

        // 通过forEach遍历
        System.out.println(" ----  forEach遍历  ---- ");
        stringList.forEach(e -> LOGGER.info("stringList中的元素是[{}]", e));
    }

    @Test
    public void iteratorSet() {
        HashSet<String> stringSet = Sets.newHashSet("tom", "jack", "marry");

        // 通过 iterator 遍历
        System.out.println(" ----  Iterator以while循环遍历  ---- ");
        Iterator<String> iterator = stringSet.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            LOGGER.info("stringSet中的元素是[{}]", item);
        }

        // 通过 for r 遍历（倒序）
        System.out.println(" ----  Iterator以for循环遍历  ---- ");
        for (Iterator<String> i = stringSet.iterator(); i.hasNext(); ) {
            String item = i.next();
            LOGGER.info("stringSet中的元素是[{}]", item);
        }

        // 通过 超级for循环 遍历
        System.out.println(" ----  超级for循环遍历  ---- ");
        for (String item : stringSet) {
            LOGGER.info("stringSet中的元素是[{}]", item);
        }

        // 通过 iterator的forEach 遍历
        System.out.println(" ----  Iterator + forEach 循环遍历  ---- ");
        stringSet.iterator().forEachRemaining(e -> LOGGER.info("stringSet中的元素是[{}]", e));

        // 通过forEach遍历
        System.out.println(" ----  forEach遍历  ---- ");
        stringSet.forEach(e -> LOGGER.info("stringSet中的元素是[{}]", e));
    }

    @Test
    public void testLists() {
        // 使用 asList 将数组转换为List
        System.out.println(" ----  asList将数组转换为List  ---- ");
        String[] rest = new String[]{"warn", "error"};
        List<String> logLevel = Lists.asList("debug", "info", rest);
        logLevel.forEach(e -> LOGGER.info("logLevel中的元素是[{}]", e));

        // 使用 partition 将List按照指定大小进行拆分
        System.out.println(" ----  partition 将List以2的大小进行拆分  ---- ");
        List<List<String>> partition = Lists.partition(logLevel, 2);
        partition.forEach(e -> LOGGER.info("partition中的元素是[{}]", e));

        // 使用 reverse 将List倒序排序
        System.out.println(" ----  reverse 将List顺序倒置  ---- ");
        List<String> reverse = Lists.reverse(logLevel);
        reverse.forEach(e -> LOGGER.info("reverse中的元素是[{}]", e));

        // 使用 transform 将List转换成新的List
        System.out.println(" ----  transform 将List按照function的返回值（字符串长度）转换成新的List  ---- ");
        List<Integer> transform = Lists.transform(logLevel, e -> e.length());
        transform.forEach(e -> LOGGER.info("transform中的元素是[{}]", e));

        // 使用 charactersOf 将字符串拆分成char
        System.out.println(" ----  charactersOf 将string拆分成Character的List  ---- ");
        ImmutableList<Character> hello = Lists.charactersOf("hello");
        hello.forEach(e -> LOGGER.info("hello中的元素是[{}]", e));

        // cartesianProduct 取多个List的笛卡儿积结果
        System.out.println(" ----  cartesianProduct 取参数中的多个List的笛卡儿积  ---- ");
        List<List<Character>> cartesianProduct = Lists.cartesianProduct(hello, Lists.charactersOf("world"));
        cartesianProduct.forEach(e -> LOGGER.info("cartesianProduct中的元素是[{}]", e));
    }

    @Test
    public void testSets() {
        HashSet<String> stringSet = Sets.newHashSet("张三", "李四", "王五");

        // Set的size组合
        System.out.println(" ----  combinations key为Set，size为组合Set的大小  ---- ");
        Set<Set<String>> combinations = Sets.combinations(stringSet, 2);
        combinations.forEach(e -> LOGGER.info("combinations中的元素是：[{}]", e));

        // 根据断言筛选Set
        System.out.println(" ----  filter 根绝断言（以张开头的元素）对Set进行筛选  ---- ");
        Set<String> zhangSet = Sets.filter(stringSet, e -> e.startsWith("张"));
        zhangSet.forEach(e -> LOGGER.info("zhangSet中的元素是：[{}]", e));

        // 对比两个Set，特别注意，是左边的对比右边的，类似于LeftJoin，获取一个视图模型
        System.out.println(" ----  difference 对比两个Set（求差集），返回一个视图模型（继承自Set）  ---- ");
        Sets.SetView<String> setView = Sets.difference(stringSet, zhangSet);
        setView.forEach(e -> LOGGER.info("difference中的元素是：[{}]", e));

        // 两个集合的交集，类似于InnerJoin，获取一个视图模型
        System.out.println(" ----  intersection 对比两个Set（求交集），返回一个视图模型（继承自Set）  ---- ");
        Sets.SetView<String> intersection = Sets.intersection(stringSet, zhangSet);
        intersection.forEach(e -> LOGGER.info("intersection中的元素是：[{}]", e));

        // 两个集合的并集，类似于Union，获取一个视图模型
        System.out.println(" ----  union 对比两个Set（求并集），返回一个视图模型（继承自Set）  ---- ");
        stringSet.add("赵六");
        Sets.SetView<String> union = Sets.union(stringSet, zhangSet);
        union.forEach(e -> LOGGER.info("union中的元素是：[{}]", e));
    }
}
