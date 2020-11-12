package com.hik.current;

import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 * CompletableFuture测试
 *
 * @author wangjinchang5
 * @date 2020/11/5 14:56
 */
public class CompletableFutureTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompletableFuture.class);

    /**
     * thenApply方法
     * <p>
     * 返回一个新的CompletionStage，当此阶段正常完成时，将此阶段的结果作为所提供函数的参数来执行。
     * 有关异常完成的规则，请参阅CompletionStage文档。
     * <p>
     * 参数 fn–用于计算返回的CompletionStage值的函数
     * 泛型 <U> –函数的返回类型
     * 返回 新的完成阶段
     */
    @Test
    public void testThenApply() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return "hello";
        }).thenApply(r -> {
            // 当CompletableFuture执行完后，再执行thenApply
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行thenApply的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return r + " world";
        }).join();

        // 打印hello world，耗时2s
        LOGGER.info("先执行supplyAsync，再执行thenApply的最终结果为["
                + result + "]，总耗时[" + stopwatch.toString() + "]");
    }

    /**
     * thenApplyAsync方法
     * <p>
     * 返回一个新的CompletionStage，当此阶段正常完成时，将使用此阶段的默认异步执行工具执行该阶段，并将此阶段的结果作为所提供函数的参数。
     * 有关异常完成的规则，请参阅CompletionStage文档。
     * <p>
     * 参数 fn–用于计算返回的CompletionStage值的函数
     * 泛型 <U> –函数的返回类型
     * 返回 新的完成阶段
     */
    @Test
    public void testThenApplyAsync() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return "hello";
        }).thenApplyAsync(r -> {
            // 当CompletableFuture执行完后，再以指定执行器执行thenApply
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行thenApply的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return r + " world";
        }).join();

        // 打印hello world，耗时2s
        LOGGER.info("先执行supplyAsync，再执行thenApplyAsync的最终结果为["
                + result + "]，总耗时[" + stopwatch.toString() + "]");
    }

    /**
     * thenCombine方法
     * <p>
     * 返回一个新的CompletionStage，当此阶段和另一个给定阶段都正常完成时，将使用两个结果作为所提供函数的参数来执行该阶段。
     * 有关异常完成的规则，请参阅CompletionStage文档。
     * <p>
     * 参数 其他-另一个完成阶段
     * 参数 fn–用于计算返回的CompletionStage值的函数
     * 泛型 <U> –其他完成阶段的结果类型
     * 泛型 <V> –函数的返回类型
     * <p>
     * 返回 新的完成阶段
     */
    @Test
    public void testThenCombine() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return "hello";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            // CompletableFuture和thenCombine的CompletableFuture同时执行，然后再执行thenCombine的BiFunction
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行thenCombine中的supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return " world";
        }), (s1, s2) -> s1 + s2).join();

        // 打印hello world，耗时1s
        LOGGER.info("先执行supplyAsync，再执行thenCombine的最终结果为["
                + result + "]，总耗时[" + stopwatch.toString() + "]");
    }

    /**
     * applyToEither方法
     * <p>
     * 返回一个新的CompletionStage，当此阶段或其他给定阶段正常完成时，将相应的结果作为所提供函数的参数来执行。
     * 有关异常完成的规则，请参阅CompletionStage文档。
     * <p>
     * 参数 其他-另一个完成阶段
     * 参数 fn–用于计算返回的CompletionStage值的函数
     * 泛型 <U> –函数的返回类型
     * 返回 新的完成阶段
     */
    @Test
    public void testApplyToEither() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return "hello";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            // CompletableFuture和applyToEither的CompletableFuture同时执行，哪个先执行完，就执行applyToEither的Function
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行thenCombine中的supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return " world";
        }), (r) -> r).join();

        // 打印hello或者world，哪个先执行完就打印哪个，耗时1s
        LOGGER.info("先执行supplyAsync，再执行applyToEither的最终结果为["
                + result + "]，总耗时[" + stopwatch.toString() + "]");
    }

    /**
     * thenAccept方法
     * <p>
     * 返回一个新的CompletionStage，当此阶段正常完成时，将此阶段的结果作为所提供操作的参数来执行。
     * 有关异常完成的规则，请参阅CompletionStage文档。
     * <p>
     * 参数 action–在完成返回的CompletionStage之前要执行的操作
     * 返回 新的完成阶段
     */
    @Test
    public void testThenAccept() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return "hello";
        }).thenAccept(s -> {
            // 当CompletableFuture执行完后，再执行thenAccept，如果不适用join，则直接执行LOGGER.info，因为其他方法还未执行完
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行thenAccept的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]，接收到的结果为[" + s + "]");
        }).join();

        // 耗时2s
        LOGGER.info("先执行supplyAsync，再执行thenAccept，总耗时[" + stopwatch.toString() + "]");
    }

    /**
     * thenAcceptBoth方法
     * <p>
     * 返回一个新的CompletionStage，当此阶段和另一个给定阶段都正常完成时，将使用两个结果作为所提供操作的参数来执行该阶段。
     * 有关异常完成的规则，请参阅CompletionStage文档。
     * <p>
     * 参数 其他-另一个完成阶段
     * 参数 action–在完成返回的CompletionStage之前要执行的操作
     * 泛型 <U> –其他完成阶段的结果类型
     * 返回 新的完成阶段
     */
    @Test
    public void testThenAcceptBoth() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return "hello";
        }).thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
            // CompletableFuture和thenAcceptBoth的CompletableFuture同时执行，执行完后，再执行thenAcceptBoth的BiConsumer
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行thenAcceptBoth中的supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return " world";
        }), (r1, r2) -> LOGGER.info("先执行supplyAsync，再执行thenAcceptBoth的最终结果为["
                + (r1 + r2) + "]，总耗时[" + stopwatch.toString() + "]")).join();

        // 耗时1s
        LOGGER.info("先执行supplyAsync，再执行thenAccept，总耗时[" + stopwatch.toString() + "]");
    }

    /**
     * acceptEither方法
     * <p>
     * 返回一个新的CompletionStage，当该阶段或另一个给定阶段正常完成时，将相应的结果作为所提供操作的参数来执行。
     * 有关异常完成的规则，请参阅CompletionStage文档。
     * <p>
     * 参数 其他-另一个完成阶段
     * 参数 action–在完成返回的CompletionStage之前要执行的操作
     * 返回 新的完成阶段
     */
    @Test
    public void testAcceptEither() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return "hello";
        }).acceptEither(CompletableFuture.supplyAsync(() -> {
            // CompletableFuture和acceptEither的CompletableFuture同时执行，哪个先执行完，就执行acceptEither的Consumer
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行acceptEither中的supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return " world";
        }), (r) -> LOGGER.info("先执行supplyAsync，再执行acceptEither的最终结果为[" +
                r + "]，总耗时[" + stopwatch.toString() + "]")).join();

        // 耗时1s
        LOGGER.info("先执行supplyAsync，再执行acceptEither，总耗时[" + stopwatch.toString() + "]");
    }

    /**
     * testThenRun方法
     * <p>
     * 返回一个新的CompletionStage，当此阶段正常完成时，将执行给定的操作。
     * 有关异常完成的规则，请参阅CompletionStage文档。
     * <p>
     * 参数 action–在完成返回的CompletionStage之前要执行的操作
     * 返回 新的完成阶段
     */
    @Test
    public void testThenRun() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return "hello";
        }).thenRun(() -> {
            // 先执行supplyAsync，执行完后再执行thenRun
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行thenRun的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
        }).join();

        // 耗时2s
        LOGGER.info("先执行supplyAsync，再执行thenRun，总耗时[" + stopwatch.toString() + "]");
    }

    /**
     * runAfterBoth方法
     * <p>
     * 返回一个新的CompletionStage，当此阶段和另一个给定阶段都正常完成时，将执行给定的操作。
     * 有关异常完成的规则，请参阅CompletionStage文档。
     * <p>
     * 参数 其他-另一个完成阶段
     * 参数 action–在完成返回的CompletionStage之前要执行的操作
     * 返回 新的完成阶段
     */
    @Test
    public void testRunAfterBoth() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return "hello";
        }).runAfterBoth(CompletableFuture.supplyAsync(() -> {
            // supplyAsync和runAfterBoth的supplyAsync同时执行，当两个都执行完了再执行runAfterBoth的Runnable
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行runAfterBoth的supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return " world";
        }), () -> {
            // supplyAsync和runAfterBoth的supplyAsync同时执行，当两个都执行完了再执行runAfterBoth的Runnable
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行runAfterBoth的Runnable的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
        }).join();

        // 耗时2s
        LOGGER.info("先执行supplyAsync，再执行runAfterBoth，总耗时[" + stopwatch.toString() + "]");
    }

    /**
     * runAfterEither方法
     * <p>
     * 返回一个新的CompletionStage，当此阶段或其他给定阶段正常完成时，将执行给定的操作。
     * 有关异常完成的规则，请参阅CompletionStage文档。
     * <p>
     * 参数 其他-另一个完成阶段
     * 参数 action–在完成返回的CompletionStage之前要执行的操作
     * 返回 新的完成阶段
     */
    @Test
    public void testRunAfterEither() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行runAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
        }).runAfterEither(CompletableFuture.runAsync(() -> {
            // runAsync和runAfterEither的runAsync同时执行，任何一个执行完后，就执行runAfterEither的Runnable
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行runAfterEither的runAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
        }), () -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行runAfterEither的Runnable的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
        }).join();

        // 耗时2s
        LOGGER.info("先执行runAsync，再执行runAfterEither，总耗时[" + stopwatch.toString() + "]");
    }

    /**
     * thenCompose方法
     * <p>
     * 返回一个新的CompletionStage，当此阶段正常完成时，将此阶段作为所提供函数的参数来执行。
     * 有关异常完成的规则，请参阅CompletionStage文档。
     * <p>
     * 参数 fn–返回新CompletionStage的函数
     * 泛型 <U> –返回的CompletionStage结果的类型
     * 返回 完成阶段
     */
    @Test
    public void testThenCompose() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return "hello";
        }).thenCompose((s) -> CompletableFuture.supplyAsync(() -> {
            // 先执行supplyAsync，当supplyAsync执行完后，再执行thenCompose，并将前者的返回值作为后者的参数
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行thenCompose的supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            return s + " world";
        }));

        for (; ; ) {
            /*
            isDone方法
            如果以任何方式完成，则返回true：正常、异常或通过取消。
            返回 如果完成则为真
             */
            if (completableFuture.isDone()) {
                // 打印hello world，耗时2s
                LOGGER.info("先执行supplyAsync，再执行thenCompose的最终结果为[" +
                        completableFuture.getNow(null) + "]，总耗时[" + stopwatch.toString() + "]");
                return;
            }
        }
    }

    /**
     * whenComplete方法
     * <p>
     * 返回与此阶段具有相同结果或异常的新CompletionStage，该阶段在该阶段完成时执行给定的操作。
     * <p>
     * 此阶段完成后，将使用此阶段的结果（如果没有，则为null）和异常（如果没有，则为null）作为参数调用给定的操作。返回的阶段在操作返回时完成。
     * 如果提供的操作本身遇到异常，则返回的阶段将异常完成，并出现此异常，除非此阶段也异常完成。
     * <p>
     * 参数 动作–要执行的动作
     * 返回 新的完成阶段
     */
    @Test
    public void testWhenComplete() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        boolean b = ThreadLocalRandom.current().nextBoolean();

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            if (b) {
                // 如果抛出异常，会有日志记录错误信息，但是主程序也会终止
                throw new RuntimeException("这是一条运行时异常，模拟执行supplyAsync是的业务异常");
            }
            return "hello";
        }).whenComplete((s, t) -> {
            // 先执行supplyAsync，再执行whenComplete的BiConsumer，如果前者出现异常，则后者的第二个参数为异常信息
            if (t != null) {
                LOGGER.error("执行supplyAsync时出现异常，异常信息是[" + t.toString() + "]");
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行whenComplete的线程为[" + Thread.currentThread().getName()
                    + "]，执行supplyAsync的返回值为[" + s + "]，耗时[" + stopwatch.toString() + "]");
        });

        // 如果出现异常，那么whenComplete的方法会执行完，执行完后main方法中止，耗时记录的操作不会执行
        LOGGER.info("执行supplyAsync时[" + (b ? "发生" : "未发生") + "]异常");

        CompletableFuture.allOf(completableFuture).join();

        // 耗时2s
        LOGGER.info("先执行supplyAsync，再执行whenComplete，总耗时[" + stopwatch.toString() + "]");
    }

    /**
     * handle方法
     * <p>
     * 返回一个新的CompletionStage，当此阶段正常或异常完成时，将此阶段的结果和异常作为所提供函数的参数来执行。
     * <p>
     * 当此阶段完成时，将使用此阶段的结果（如果没有，则为null）和异常（如果没有，则为null）作为参数调用给定函数，并使用函数的结果来完成返回的阶段。
     * <p>
     * 参数 函数返回计算fn的值
     * 泛型 <U> –函数的返回类型
     * 返回 新的完成阶段
     */
    @Test
    public void testHandle() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        boolean b = RandomUtils.nextBoolean();

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            if (!b) {
                // 如果抛出异常，handle会处理异常信息，主程序不会终止
                throw new RuntimeException("这是一条运行时异常，模拟执行supplyAsync是的业务异常");
            }
            return "hello";
        }).handle((s, t) -> {
            // 先执行supplyAsync，再执行handle的BiFunction，如果前者出现异常，则后者的第二个参数为异常信息，不管有无异常，handle均执行
            if (t != null) {
                LOGGER.error("执行handle时捕获了supplyAsync抛出的异常，异常信息是[" + t.toString() + "]");
                return "出现异常";
            }
            LOGGER.info("执行handle时，未发现supplyAsync抛出异常，返回原始值");
            return s;
        });

        CompletableFuture.allOf(completableFuture).join();

        // 打印结果根据supplyAsync有无异常确定，如果没有异常打印hello，如果有异常打印出现异常，耗时1s
        LOGGER.info("先执行supplyAsync，再执行handle，执行supplyAsync时[" + (!b ? "发生" : "未发生")
                + "]异常，最终结果为[" + completableFuture.join() + "]，总耗时[" + stopwatch.toString() + "]");
    }

    /**
     * exceptionally方法
     * <p>
     * 返回新的CompletionStage，当此阶段异常完成时，将此阶段的异常作为所提供函数的参数来执行。
     * 否则，如果此阶段正常完成，则返回的阶段也将以相同的值正常完成。
     * <p>
     * 参数 fn–如果此CompletionStage异常完成，则用于计算返回的CompletionStage值的函数
     * 返回 新的完成阶段
     */
    @Test
    public void testExceptionally() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        boolean b = RandomUtils.nextBoolean();

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程沉睡时发生异常", e);
            }
            LOGGER.info("执行supplyAsync的线程为[" + Thread.currentThread().getName()
                    + "]，耗时[" + stopwatch.toString() + "]");
            if (!b) {
                // 如果抛出异常，exceptionally会处理异常信息，主程序不会终止
                throw new RuntimeException("这是一条运行时异常，模拟执行supplyAsync是的业务异常");
            }
            return "hello";
        }).exceptionally((t) -> {
            // 先执行supplyAsync，如果执行过程中出现异常，则执行exceptionally，如果没有出现异常，则不执行exceptionally
            LOGGER.error("执行supplyAsync时出现了异常", t);
            return "出现异常";
        });

        // 打印结果根据supplyAsync有无异常确定，如果没有异常打印hello，如果有异常打印出现异常，耗时2s
        LOGGER.info("先执行supplyAsync，可能执行exceptionally，执行supplyAsync时[" + (!b ? "发生" : "未发生")
                + "]异常，最终结果为[" + completableFuture.join() + "]，总耗时[" + stopwatch.toString() + "]");
    }
}
