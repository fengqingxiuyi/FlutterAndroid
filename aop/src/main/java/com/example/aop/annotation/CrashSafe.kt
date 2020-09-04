package com.example.aop.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/***
 * 捕获Crash
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(
    RetentionPolicy.CLASS
)
annotation class CrashSafe