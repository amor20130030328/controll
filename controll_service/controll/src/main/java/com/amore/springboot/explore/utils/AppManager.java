package com.amore.springboot.explore.utils;

import java.util.concurrent.*;

public class AppManager {

    // 定义一个静态的线程池实例（全局复用）
    private static ExecutorService app;

    // 初始化线程池（静态代码块，确保只初始化一次）
    static {

        app = new ThreadPoolExecutor(
                10, // 核心线程数
                10, // 最大线程数
                0L, // 空闲线程存活时间
                TimeUnit.SECONDS, // 时间单位
                new LinkedBlockingQueue<>(100), // 有界任务队列（容量100）
                new ThreadFactory() { // 自定义线程工厂，给线程命名
                    private int count = 1;

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("custom-thread-pool-" + count++);
                        return thread;
                    }
                },
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略：任务满时抛RejectedExecutionException
        );
    }

    public static void main(String[] args) {
        // 测试：提交20个任务到线程池
        for (int i = 1; i <= 20; i++) {
            int taskId = i;
            // 提交任务
            app.submit(() -> {
                try {
                    // 模拟任务执行（休眠1秒）
                    Thread.sleep(1000);
                    System.out.println("任务" + taskId + "执行完成，执行线程：" + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 恢复中断状态
                    System.out.println("任务" + taskId + "被中断：" + e.getMessage());
                }
            });
        }

        // 关闭线程池（所有任务执行完成后关闭）
        app.shutdown();
        try {
            // 等待线程池关闭（最多等10秒）
            if (!app.awaitTermination(10, TimeUnit.SECONDS)) {
                app.shutdownNow(); // 强制关闭
            }
        } catch (InterruptedException e) {
            app.shutdownNow(); // 捕获中断，强制关闭
        }
    }

    // 提供获取线程池的方法（外部调用）
    public static ExecutorService getFixedThreadPool() {
        return app;
    }
}