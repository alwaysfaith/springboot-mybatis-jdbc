package com.ocean.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhouliang
 * @Date: 2019/4/9 18:06
 */
@Configuration
@Slf4j
public class ThreadTaskPoolConfig {

    /**
     * jvm启动的时候初始化线程池，jvm关闭的时候关闭线程池
     *
     * @return
     */
    @Bean(name = "importPool")
    public ThreadPoolExecutor initImportPool() {
        int maximumPoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                5,
                maximumPoolSize,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000),
                r -> {
                    Thread t = new Thread(r);
                    if (t.isDaemon()) {
                        t.setDaemon(Boolean.FALSE);
                    }
                    return t;
                },
                new MyAbortPolicy()
        );
        //调用钩子关闭线程池
        CloseMyFactory closeMyFactory = new CloseMyFactory(poolExecutor);
        Runtime.getRuntime().addShutdownHook(closeMyFactory);
        return poolExecutor;
    }

    /**
     * jvm启动的时候把线程池注册到钩子函数里面，jvm关闭的时候会回调钩子函数的方法。
     */
    private class CloseMyFactory extends Thread {
        private ThreadPoolExecutor executor;

        public CloseMyFactory(ThreadPoolExecutor executor) {
            this.executor = executor;
        }

        @Override
        public void run() {
            shutdownThreadPool(executor, "订单线程");
        }
    }

    /**
     * 优雅关闭线程池
     *
     * @param threadPool
     * @param alias
     */
    private void shutdownThreadPool(ExecutorService threadPool, String alias) {
        log.info("Start to shutdown the thead pool: {}", alias);

        threadPool.shutdown(); // 使新任务无法提交.
        try {
            // 等待未完成任务结束
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow(); // 取消当前执行的任务
                log.warn("Interrupt the worker, which may cause some task inconsistent. Please check the biz logs.");

                // 等待任务取消的响应
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.error("Thread pool can't be shutdown even with interrupting worker threads, which may cause some task inconsistent. Please check the biz logs.");
                }
            }
        } catch (InterruptedException ie) {
            // 重新取消当前线程进行中断
            threadPool.shutdownNow();
            log.error("The current server thread is interrupted when it is trying to stop the worker threads. This may leave an inconcistent state. Please check the biz logs.");

            // 保留中断状态
            Thread.currentThread().interrupt();
        }

        log.info("Finally shutdown the thead pool: {}", alias);
    }
}
