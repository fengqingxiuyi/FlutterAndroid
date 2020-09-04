package com.example.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 捕获Crash
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface CrashSafe {
}
