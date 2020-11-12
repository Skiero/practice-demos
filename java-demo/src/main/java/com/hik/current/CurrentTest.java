package com.hik.current;

import com.google.common.base.Stopwatch;
import com.hik.model.User;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 并发测试
 *
 * @author wangjinchang5
 * @date 2020/10/29 17:24
 */
public class CurrentTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentTest.class);
    /**
     * 虚拟数据（用户id，模拟过程为根据用户id查询用户信息）
     */
    private final List<Integer> mockData = new ArrayList<>(CYCLE_COUNT);
    /**
     * 测试后的结果（用户信息）
     */
    private final List<User> mockResult = new ArrayList<>(CYCLE_COUNT);
    /**
     * 循环次数
     */
    private static final int CYCLE_COUNT = 10;

    @Before
    public void initializeMockData() {
        for (int i = 0; i < CYCLE_COUNT; i++) {
            mockData.add(i + 1);
        }
    }

    /**
     * 遍历时串行化执行
     */
    @Test
    public void testForEachWithSerialize() {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        mockData.forEach(e -> mockResult.add(mockServiceProcessing(e)));
        LOGGER.info("forEach串行化执行任务耗时[" + stopwatch.toString() + "]，任务总数为["
                + mockData.size() + "]，回调结果总数为[" + mockResult.size() + "]");
    }

    /**
     * 遍历时异步执行
     */
    @Test
    public void testForEachWithCurrent() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        // 遍历时创建异步任务，然后收集CompletableFuture<User>，这个过程很快，可能在提交任务的时候，有的任务已经执行完毕（前提条件是异步任务本身就不耗时）
        List<CompletableFuture<User>> completableFutures = mockData.stream()
                .map(e -> CompletableFuture.supplyAsync(() -> mockServiceProcessing(e)))
                .collect(Collectors.toList());

        // 阻塞，等待所有CompletableFuture完成
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();

        System.err.println("-----所有异步任务均执行完毕-----");

        // 遍历时获取异步任务的返回值，此时异步任务必定全部执行完毕，遍历时是串行执行
        completableFutures.forEach(r -> {
            /*
            如果完成，则返回结果值（或引发任何遇到的异常），否则返回给定的值valueIfAbsent。
            参数 valueIfAbsent–未完成时返回的值
            返回 如果结果值已完成，则为给定值valueIfAbsent
            异常 CancellationException–如果计算被取消
            异常 CompletionException–如果此未来异常完成或完成计算引发异常
             */
            User user = r.getNow(null);
            System.out.println("线程[" + Thread.currentThread().getName() + "]执行第[" + user.getAge() + "]个回调任务");
            mockResult.add(user);
        });

        LOGGER.info("forEach异步执行任务耗时[" + stopwatch.toString() + "]，任务总数为["
                + mockData.size() + "]，回调结果总数为[" + mockResult.size() + "]");
    }

    /**
     * 方法1：遍历时异步执行
     * <p>
     * 1、遍历用户id，以异步任务执行业务逻辑；
     * 2、收集CompletableFuture；
     * 3、遍历CompletableFuture列表，分别get用户信息
     * 总结：首先创建异步任务，将返回值收集，顺序遍历返回值，当异步任务执行完后，然后get到数据，具体可运行测试类观察
     * </p>
     */
    @Test
    public void testForEachWithCurrentByMethodOne() {
        Stopwatch stopwatch = Stopwatch.createStarted();

        // 遍历时创建异步任务，然后收集CompletableFuture<User>，这个过程很快，可能在提交任务的时候，有的任务已经执行完毕
        List<CompletableFuture<User>> futures = mockData.stream()
                .map(e -> CompletableFuture.supplyAsync(() -> mockServiceProcessing(e)))
                .collect(Collectors.toList());

        LOGGER.info("forEach提交异步任务耗时[" + stopwatch.toString() + "]，当前ForkJoinPool的并行级别为["
                + ForkJoinPool.commonPool().getParallelism() + "]");

        // 遍历时获取异步任务的返回值，开始遍历前，可能有异步任务已经完成，但是仍然顺序（串行化）遍历
        futures.forEach(e -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500L);
            } catch (InterruptedException exception) {
                LOGGER.error("线程沉睡时出现异常", exception);
            }
            /*
            完成时返回结果值，如果异常完成，则抛出（未选中）异常。
            为了更好地符合常用函数形式的使用，如果完成此CompletableFuture所涉及的计算引发了异常，
            则此方法将抛出一个（未经检查）CompletionException，并将底层异常作为其原因。
             */
            User user = e.join();
            mockResult.add(user);
            System.out.println("线程[" + Thread.currentThread().getName() + "]执行第[" + user.getAge() + "]个回调任务");
        });

        LOGGER.info("forEach异步执行任务耗时[" + stopwatch.toString() + "]，任务总数为["
                + mockData.size() + "]，回调结果总数为[" + mockResult.size() + "]");
    }

    /**
     * 方法1错误编码：遍历时异步执行
     * 该方法和方法1的思路一样，首先以流的方式遍历用户id，然后提交异步任务执行业务逻辑，此时流变为Stream<CompletableFuture<User>>
     * 如果这里直接顺序遍历CompletableFuture<User>流，那么相当于只有一个CompletableFuture<User>阻塞，并不是所有任务都异步执行，实质和串行化操作一样，具体可运行测试类观察
     * 如果这里改用并行流遍历CompletableFuture<User>，那么相当于每个CompletableFuture<User>都在阻塞，具体可运行测试类观察
     */
    @Test
    public void testForEachWithCurrentByMethodOneWithError() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        LOGGER.info("当前JVM可用的处理器数量为[" + Runtime.getRuntime().availableProcessors() + "]个");
        mockData.stream()
                .map(e -> CompletableFuture.supplyAsync(() -> mockServiceProcessing(e)))
                /*
                如果要测试并行流，取消该注释即可，如果不取消，这个操作和串行化效果一样，就算是用并行流，执行效率还是低，类似于map操作提交后，forEach阻塞等待，唯一不同的就是forEach是串行还是并行
                并行流：map先提交异步任务，foreach遍历时获取返回值，但是这个过程（foreach中的方法）不阻塞，直接继续下一个map操作
                串行化：map先提交异步任务，foreach遍历时获取返回值，但是这个过程（foreach中的方法）会阻塞，执行完后继续下一个map操作
                 */
                //.parallel()
                .forEach(e -> {
                    User user = null;
                    try {
                        /*
                        如果需要，等待此未来完成，然后返回其结果。
                        CancellationException–如果此未来被取消
                        ExecutionException–如果此未来异常完成
                        InterruptedException–如果当前线程在等待时被中断
                         */
                        user = e.get();
                        System.out.println("线程[" + Thread.currentThread().getName() + "]执行第["
                                + user.getAge() + "]个回调任务");
                    } catch (InterruptedException | ExecutionException exception) {
                        exception.printStackTrace();
                    }
                    mockResult.add(user);
                });

        LOGGER.info("forEach异步执行任务耗时[" + stopwatch.toString() + "]，任务总数为["
                + mockData.size() + "]，回调结果总数为[" + mockResult.size() + "]");
    }

    /**
     * 方法2：遍历时异步执行（异步任务会有异常）
     * 分而治之，allOf().join()方法用于等待所有异步任务执行完毕，此过程会阻塞
     */
    @Test
    public void testForEachWithCurrentByMethodTwo() {
        LOGGER.info("当前JVM可用的处理器数量为[" + Runtime.getRuntime().availableProcessors() + "]个");
        final Stopwatch stopwatch = Stopwatch.createStarted();

        // 该写法和将all(CompletableFuture<?>... cfs)中的cfs提取出来的效果一样，建议采用该写法，相对简洁
        CompletableFuture.allOf(
                mockData.stream().map(e ->
                        CompletableFuture.supplyAsync(() -> mockServiceProcessingWithError(e))
                                .exceptionally((t) -> {
                                    LOGGER.error("执行任务时出现了异常，详细信息为[" + t.getMessage() + "]");
                                    return new User();
                                })
                                .thenAccept((u) -> {
                                    System.out.println("线程[" + Thread.currentThread().getName()
                                            + "]执行第[" + u.getAge() + "]个回调任务");
                                    mockResult.add(u);
                                })
                ).toArray(CompletableFuture<?>[]::new)
        ).join();

        LOGGER.info("forEach异步执行任务耗时[" + stopwatch.toString() + "]，任务总数为["
                + mockData.size() + "]，回调结果总数为[" + mockResult.size() + "]");
    }

    /**
     * 方法3：遍历时异步执行（异步任务的组合式操作）
     * 异步任务的组合式操作，即使用CompletableFuture提供的各种api，组合使用，以满足业务需要
     */
    @Test
    public void testForEachWithCurrentByMethodThree() {
        LOGGER.info("当前ForkJoinPool的并行级别为[" + ForkJoinPool.commonPool().getParallelism() + "]");
        final Stopwatch stopwatch = Stopwatch.createStarted();

        // 列表遍历时提交异步任务，收集CompletableFuture<?>，?取决于CompletableFuture的最终操作，stream流操作为多个异步任务并行，单个执行按照CompletableFuture流操作顺序执行，可能受方法（比如thenCombine）影响执行顺序
        List<CompletableFuture<Integer>> completableFutureList = mockData.stream().map(e ->
                CompletableFuture.supplyAsync(() -> mockServiceProcessingWithError(e))
                        // 处理异常（如果有异常就执行该方法，没有异常就不执行）
                        .exceptionally((t) -> {
                            System.err.println("执行任务时出现了异常，详细信息为[" + t.getMessage() + "]");
                            return new User();
                        })
                        // supplyAsync执行完后执行thenApply，多个异步任务之间是并行，即同时有多个异步任务执行CompletableFuture流操作
                        .thenApply((u) -> {
                            System.out.println("线程[" + Thread.currentThread().getName()
                                    + "]执行第[" + u.getAge() + "]个回调任务");
                            return u.getAge();
                        })
                        // thenCombine的supplyAsync先于thenApply（等待supplyAsync）执行，BiFunction在一个CompletableFuture流操作的所有操作完成后执行（即不需要等待所有的异步任务完成，各自走各自的流程），但是多个异步任务是并行的
                        .thenCombine(CompletableFuture.supplyAsync(() -> {
                            Optional<Integer> optional = mockData.stream().reduce(Integer::sum);
                            if (optional.isPresent()) {
                                Integer sum = optional.get();
                                LOGGER.info("线程[" + Thread.currentThread().getName()
                                        + "]执行计算总和的任务，总和为[" + sum + "]");
                                return sum;
                            } else {
                                return 0;
                            }
                        }), (r1, r2) -> {
                            int sum = (r1 == null ? 0 : r1) + (r2 == null ? 0 : r2);
                            LOGGER.info("第[" + r1 + "]个任务计算的总和为[" + r2 + "]，即[" + r1 + "+" + r2 + "=" + sum + "]");
                            return sum;
                        })).collect(Collectors.toList());

        LOGGER.info("所有CompletableFuture流操作均已提交，耗时[" + stopwatch.toString() + "]");

        // join()将main方法阻塞，等待异步任务执行完成，在此之前，可以进行其他操作，在此处等待异步任务执行完毕后，再进行后续操作
        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture<?>[0])).join();

        LOGGER.info("forEach异步执行任务耗时[" + stopwatch.toString() + "]，任务总数为["
                + mockData.size() + "]，回调结果总数为[" + completableFutureList.size() + "]");
    }

    /**
     * stream之并行流
     * <p>使用stream的并行流时，线程池为ForkJoinPool.commonPool，当commonPool执行异步任务时，main也处理一部分异步任务，任务由stream分配
     * 总结来讲，所有异步任务由main线程和commonPool线程池共同完成，当调用并行流时，后续操作都是以异步任务进行操作的</p>
     */
    @Test
    public void testParallelStream() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        // 使用并行流时必须有聚合操作（除非直接遍历操作），但不能close()，否则并行流不会执行（我理解的是没有执行，具体可参考代码）
        Stream<Boolean> booleanStream = mockData.parallelStream().map(e -> mockResult.add(mockServiceProcessing(e)));
        LOGGER.info("以并行流的形式提交异步任务，耗时[" + stopwatch.toString() + "]");
        long count = booleanStream.count();
        // 并行流会阻塞，全部任务执行完后执行日志记录操作，耗时2s，回调结果为10个
        LOGGER.info("stream并行流且聚合操作，执行任务耗时[" + stopwatch.toString() + "]，任务总数为["
                + mockData.size() + "]，回调结果总数为[" + count + "]");

        System.err.println("----------------------------------------------------");

        stopwatch.reset().start();
        // 使用并行流时，如果不使用聚合操作，就得使用forEach()遍历，forEach()遍历时整体阻塞，其效果是等待所有并行任务执行完毕
        mockData.stream().parallel().forEach(e -> mockResult.add(mockServiceProcessing(e)));
        // 并行流会阻塞，全部任务执行完后执行日志记录操作，耗时2s，回调结果为20个
        LOGGER.info("stream并行流且遍历操作，执行任务耗时[" + stopwatch.toString() + "]，任务总数为["
                + mockData.size() + "]，回调结果总数为[" + mockResult.size() + "]");
    }

    /**
     * 模拟业务处理，根据用户id查询用户信息
     * <p>线程沉睡1秒钟，然后返回用户信息</p>
     *
     * @param id 用户id
     * @return 用户信息
     */
    private User mockServiceProcessing(Integer id) {
        // mock finding info from database or remote service
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            LOGGER.error("线程沉睡时出现异常", e);
            return null;
        }

        System.out.println("线程[" + Thread.currentThread().getName() + "]执行第[" + id + "]个业务");
        return new User("mock" + id, id, id);
    }

    private User mockServiceProcessingWithError(Integer id) {
        // mock finding info from database or remote service
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            LOGGER.error("线程沉睡时出现异常", e);
            return null;
        }

        int random = RandomUtils.nextInt(1, CYCLE_COUNT);
        if (id == random) {
            throw new RuntimeException("id是[" + id + "]的用户信息不存在");
        }

        System.out.println("线程[" + Thread.currentThread().getName() + "]执行第[" + id + "]个业务");
        return new User("mock" + id, id, id);
    }
}
