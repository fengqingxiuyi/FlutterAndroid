package com.example.aop.aspect

import android.util.Log
import com.example.aop.AopUtil.Companion.instance
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * Sophix检测，获取libapp.so文件存放的路径
 */
@Aspect
class SophixAspect {

    companion object {
        private const val TAG = "SophixAspect"
    }

    @Pointcut("execution(* com.taobao.sophix.a.c.a(String))")
    fun methodAnnotated() {
    }

    @Around("execution(!synthetic * *(..)) && methodAnnotated()")
    @Throws(Throwable::class)
    fun doMethod(joinPoint: ProceedingJoinPoint): Any? {
        var result: Any? = null
        try {
            val arr = joinPoint.args
            val path = arr[0] as String
            instance.setLibAppSoPath(path)
            Log.e(TAG, "路径=>$path")
            result = joinPoint.proceed(arr)
        } catch (e: Throwable) {
            Log.e(TAG, "路径异常=>", e)
        }
        return result
    }

}