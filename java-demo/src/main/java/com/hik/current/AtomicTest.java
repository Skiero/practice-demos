package com.hik.current;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 原子类测试
 *
 * @author wangjinchang5
 * @date 2020/8/19 16:54
 */
public class AtomicTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtomicTest.class);
    /**
     * 并发次数
     */
    private final int time = 1000;
    /**
     * 模拟并发时的沉睡时间
     */
    private final long sleepTime = 10;
    /**
     * 线程不安全的成员变量
     */
    private int a = 0;
    /**
     * 线程安全的成员变量
     */
    private final AtomicLong atomicLong = new AtomicLong();

    /**
     * 线程不安全和线程安全的自增操作，引入 java.util.concurrent.atomic 包下的原子类
     */
    @Test
    public void testAtomic() {
        List<CompletableFuture<Integer>> noAtomicAsyncList = Lists.newArrayListWithCapacity(time);
        for (int i = 0; i < time; i++) {
            CompletableFuture<Integer> noAtomicAsync = CompletableFuture.supplyAsync(() -> {
                a++;
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return a;
            });
            noAtomicAsyncList.add(noAtomicAsync);
        }

        long noAtomicAsyncCount = noAtomicAsyncList.stream().peek(CompletableFuture::join).count();
        LOGGER.info("非原子操作，执行了[{}]次并发自增操作", noAtomicAsyncCount);
        LOGGER.info("非原子操作的情况下，执行{}次后，a的值为[{}]", time, a);

        List<CompletableFuture<AtomicLong>> atomicAsyncList = Lists.newArrayListWithCapacity(time);
        for (int i = 0; i < time; i++) {
            CompletableFuture<AtomicLong> atomicAsync = CompletableFuture.supplyAsync(() -> {
                atomicLong.incrementAndGet();
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return atomicLong;
            });
            atomicAsyncList.add(atomicAsync);
        }

        long atomicAsyncCount = atomicAsyncList.stream().peek(CompletableFuture::join).count();
        LOGGER.info("原子操作，执行了[{}]次并发自增操作", atomicAsyncCount);
        LOGGER.info("原子操作的情况下，执行{}次后，atomicLong的值为[{}]", time, atomicLong.intValue());
    }

    @Test
    public void testOperationAtomic() {
        // get方法用来获取当前值。
        long currentValue = atomicLong.get();
        LOGGER.info("atomicLong.get()的获取当前值，atomicLong的默认值为[{}]", currentValue);

        // incrementAndGet方法用来自增并获取当前值（先自增再获取）
        long incrementAndGetValue = atomicLong.incrementAndGet();
        LOGGER.info("atomicLong.incrementAndGet()的先自增再获取当前值为[{}]", incrementAndGetValue);

        // decrementAndGet方法用来先自减并获取当前值（先自减再获取）
        long decrementAndGetValue = atomicLong.decrementAndGet();
        LOGGER.info("atomicLong.decrementAndGet()的先自减再获取当前值为[{}]", decrementAndGetValue);

        // getAndAdd方法先获取当前值，然后在当前值的基础上增加 delta
        long getAndAdd = atomicLong.getAndAdd(10);
        LOGGER.info("atomicLong.getAndAdd()的先获取然后给原子类增加delta，获取的值[{}]，增加后的值[{}]",
                getAndAdd, atomicLong.get());

        // getAndSet方法先获取当前值，然后对该原子类重新赋值 newValue
        long getAndSet = atomicLong.getAndSet(0);
        LOGGER.info("atomicLong.getAndSet()的方法先获取然后给原子类重新赋值newValue，获取的值[{}]，赋值后的值[{}]",
                getAndSet, atomicLong.get());

        // getAndIncrement方法先获取再自增
        long getAndIncrement = atomicLong.getAndIncrement();
        LOGGER.info("atomicLong.getAndIncrement()的方法先获取值然后自增，获取的值[{}]，自增后的值[{}]",
                getAndIncrement, atomicLong.get());

        // getAndDecrement方法先获取再自减
        long getAndDecrement = atomicLong.getAndDecrement();
        LOGGER.info("atomicLong.getAndDecrement()的方法先获取值然后自减，获取的值[{}]，自减后的值[{}]",
                getAndDecrement, atomicLong.get());

        // set方法用来设置为给定值。
        atomicLong.set(10);
        LOGGER.info("atomicLong.set()的方法用来设置给定值，当前的值为[{}]", atomicLong.get());

        // accumulateAndGet方法是使用 对当前值（e1）和给定值（e2）应用给定函数的结果以原子方式更新当前值，并返回更新后的值。
        long accumulateAndGet = atomicLong.accumulateAndGet(5, (e1, e2) -> (e1 + e2) / e2);
        LOGGER.info("atomicLong.accumulateAndGet的方法是计算 (10+5)/5=3 的值，计算结果为[{}]", accumulateAndGet);
    }

    @Test
    public void testLongAdder() {
        LongAdder longAdder = new LongAdder();

        // **Value返回当前的值
        int intValue = longAdder.intValue();
        LOGGER.info("longAdder.intValue()以int类型返回当前值，longAdder的默认值是[{}]", intValue);

        // 自减
        longAdder.decrement();
        LOGGER.info("longAdder.decrement()自减，自减后的值为[{}]", longAdder.intValue());

        // 自增
        longAdder.increment();
        LOGGER.info("longAdder.increment()自增，自增后的值为[{}]", longAdder.intValue());

        // 增加指定的数值
        longAdder.add(10);
        LOGGER.info("longAdder.add()用来增加指定的数值，增加[10]后的值为[{}]", longAdder.intValue());

        // 返回当前总和，非并发情况下返回值准确，并发情况下返回值可能不会合并更新的值（个人理解和**Value一样，只不过线程不安全）
        long sum = longAdder.sum();
        LOGGER.info("longAdder.sum()用来获取longAdder的总和，总和为[{}]", sum);

        // 返回当前总和，然后重置起始值
        long sumThenReset = longAdder.sumThenReset();
        LOGGER.info("longAdder.sumThenReset()先返回当前总和，然后重置起始值（为0），当前总和为[{}]，起始值为[{}]",
                sumThenReset, longAdder.intValue());
    }
}
