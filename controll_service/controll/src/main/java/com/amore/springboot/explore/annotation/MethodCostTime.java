package com.amore.springboot.explore.annotation;

import java.lang.annotation.*;

/**
 * 方法耗时统计注解
 * 标记在方法上，会自动打印该方法的执行耗时
 */
// 注解作用在方法上
@Target(ElementType.METHOD)
// 注解会保留到运行时（AOP 需要运行时获取注解）
@Retention(RetentionPolicy.RUNTIME)
// 允许注解被继承（子类继承父类方法时，注解也生效）
@Inherited
// 生成 javadoc 时包含该注解
@Documented
public @interface MethodCostTime {
    /**
     * 可选：方法描述（用于日志更清晰）
     */
    String value() default "";
}
