package com.amore.springboot.explore.test;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main4 {



    static void schedule(ScheduledThreadPoolExecutor executor) {
        executor.schedule(()->{
           int n = 1 / 0;
            System.out.println(n);
        }, 10, TimeUnit.NANOSECONDS);
    }

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        schedule(executor);
        executor.shutdown();
    }
}
