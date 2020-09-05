# aop

AOP工具库

- [gradle_plugin_android_aspectjx](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)
- [AOP之AspectJ在Android中的应用](https://www.jianshu.com/p/80a1e70598fe)
- [Android Aop之Aspectj](https://www.zybuluo.com/TryLoveCatch/note/1430181)

# 目录结构

```
aop
  |--src
       |--main/java/com/example/aop //多级目录折叠
            |--annotation //注解目录，通过切这些注解实现AOP拦截
            |--aspect //Aspect目录
                 |--CrashSafeAspect //Crash检测
                 |--SophixAspect //Sophix检测，获取libapp.so文件存放的路径
            |--AopUtil //工具类，包含回调SophixAspect拦截到的libapp.so的路径等功能
  |--build.gradle //aop模块的gradle脚本文件
  |--README.md //AOP总结
```

# 集成步骤

1. 在project的build.gradle中添加`classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.10'`

注意：kotlin插件的版本不能高于`1.3.61`，gradle插件的版本不能高于`3.6.1`

2. 创建aop模块，并在aop模块的build.gradle中添加`implementation 'org.aspectj:aspectjrt:1.9.5'`

3. 编辑app模块的build.gradle文件，添加以下`必要`内容：

```groovy
//AOP
apply plugin: 'com.hujiang.android-aspectjx'

aspectjx {
    //指定只对含有关键字'universal-image-loader', 'AspectJX-Demo/library'的库进行织入扫描，忽略其他库，提升编译效率
//    includeJarFilter 'universal-image-loader', 'AspectJX-Demo/library'
//    excludeJarFilter '.jar'
//    ajcArgs '-Xlint:warning'
}
```

4. 编辑app模块的build.gradle文件，添加以下`可选`内容：

```groovy
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main

//AOP日志输出
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