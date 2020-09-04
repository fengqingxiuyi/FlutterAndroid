package com.example.aop.aspect;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Crash检测
 */
@Aspect
public class CrashSafeAspect {

    /**
     * 定义切入点
     * 1. Pointcuts 可以在普通的 class 或 Aspect class 中定义
     * 2. 由 org.aspectj.lang.annotation.Pointcut 注解修饰的方法声明
     * 3. 方法返回值只能是 void
     * 4. @Pointcut 修饰的方法只能由空的方法实现而且不能有 throws 语句
     * 5. 方法的参数和 pointcut 中的参数相对应。
     *
     * @annotation 匹配那些有指定注解的连接点
     * execution 表示的Join Point，在方法执行的位置
     *           匹配规则：(<修饰符模式>? <返回类型模式> <方法名模式>(<参数模式>) <异常模式>?)
     * call      表示的Join Point，在方法调用的位置
     */
    @Pointcut("@within(com.example.aop.annotation.CrashSafe) || @annotation(com.example.aop.annotation.CrashSafe)")
    public void methodAnnotated() {}

    /**
     * 定义一个切面方法，包裹切点方法
     *
     * @Around 环绕JPoint执行操作，它包含了前后两个过程，使用这种类型需要手动调用proceed方法来执行原操作
     *
     * 有synthetic标记的field和method是class内部使用的，正常的源代码里不会出现synthetic field。
     */
    @Around("execution(!synthetic * *(..)) && methodAnnotated()")
    public Object doMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            Log.e("CrashSafeAspect", "已拦截Crash", e);
        }
        return result;
    }

}
