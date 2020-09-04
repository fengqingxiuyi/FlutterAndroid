# aop

AOP工具库

# 基础知识

<https://www.jianshu.com/p/80a1e70598fe>

<https://www.zybuluo.com/TryLoveCatch/note/1430181>

# 三方库

<https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx>

# 集成步骤

1、在project的build.gradle中添加`classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.6'`

2、在自定义module的build.gradle中添加`implementation 'org.aspectj:aspectjrt:1.9.1'`

3、在app的build.gradle中添加`apply plugin: 'com.hujiang.android-aspectjx'`

4、在app的build.gradle中添加下面语句，用于打印log

```
final def log = project.logger
final def variants = project.android.applicationVariants //libraryVariants
//在构建工程时，执行编织
variants.all { variant ->
    if (!variant.buildType.isDebuggable()) {
        log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
        return
    }

    TaskProvider<JavaCompile> provider =  variant.javaCompileProvider
    JavaCompile javaCompile = provider.get()
    javaCompile.doLast {
        String[] args = ["-showWeaveInfo",
                         "-1.8",
                         "-inpath", javaCompile.destinationDir.toString(),
                         "-aspectpath", javaCompile.classpath.asPath,
                         "-d", javaCompile.destinationDir.toString(),
                         "-classpath", javaCompile.classpath.asPath,
                         "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
        log.debug "ajc args: " + Arrays.toString(args)

        MessageHandler handler = new MessageHandler(true)
        new Main().run(args, handler)
        for (IMessage message : handler.getMessages(null, true)) {
            switch (message.getKind()) {
                case IMessage.ABORT:
                case IMessage.ERROR:
                case IMessage.FAIL:
                    log.error message.message, message.thrown
                    break
                case IMessage.WARNING:
                    log.warn message.message, message.thrown
                    break
                case IMessage.INFO:
                    log.info message.message, message.thrown
                    break
                case IMessage.DEBUG:
                    log.debug message.message, message.thrown
                    break
            }
        }
    }
}
```

# 开发请参考CrashSafeAspect