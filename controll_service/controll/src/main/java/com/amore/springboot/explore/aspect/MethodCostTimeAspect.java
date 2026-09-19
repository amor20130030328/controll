package com.amore.springboot.explore.aspect;

import com.amore.springboot.explore.annotation.MethodCostTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 方法耗时注解的切面实现
 * 拦截被 @MethodCostTime 标记的方法，计算并打印耗时
 */
@Aspect
@Component
public class MethodCostTimeAspect {

    private static final Logger log = LoggerFactory.getLogger(MethodCostTimeAspect.class);

    /**
     * 修正：切点表达式使用真实的注解包名（推荐用这种“直接引用注解类”的方式，避免拼写错误）
     */
    @Pointcut("@annotation(methodCostTime)") // 直接引用注解参数，无需写全类名
    public void methodCostTimePointcut(MethodCostTime methodCostTime) {} // 入参绑定注解

    /**
     * 环绕通知：绑定注解参数，逻辑不变
     */
    @Around("methodCostTimePointcut(methodCostTime)") // 关联切点的注解参数
    public Object around(ProceedingJoinPoint joinPoint, MethodCostTime methodCostTime) throws Throwable {
        // 1. 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getDeclaringClass().getName() + "." + method.getName();
        // 直接使用入参的注解对象，无需再通过method.getAnnotation()获取
        String methodDesc = methodCostTime.value().isEmpty() ? methodName : methodCostTime.value();

        // 2. 记录开始时间
        long startTime = System.nanoTime();
        try {
            // 执行原方法
            return joinPoint.proceed();
        } finally {
            // 3. 计算并打印耗时
            long costTime = (System.nanoTime() - startTime) / 1_000_000;
            log.info("【方法耗时】{} - 执行耗时：{} ms", methodDesc, costTime);
        }
    }
}