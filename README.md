# FlutterAndroid

学习总结Flutter：这是一个集成Flutter模块的Android工程

- [Flutter官网](https://flutter.dev/)
- [Flutter中文网](https://flutterchina.club/)

# 命令

- flutter --version：查看当前安装的flutter版本
- flutter devices：识别连接的Android设备
- flutter doctor：校验本机flutter环境是否正确
- flutter upgrade：更新flutter代码,实质就是git代码更新拉取,下载flutter sdk是git仓库的打包
- flutter version：查看所有flutter版本
- flutter version [versionName]：切换到指定的versionName
- flutter clean：删除`build/`和`.dart_tool/`目录,清除缓存信息,避免之前不同版本代码的影响
- flutter run --[设备名称]：运行项目到指定设备
- flutter -v [command]：查看APP所有日志的输出，在调试时需要配合run命令使用，即`flutter -v run`

# 下载

git clone https://gitee.com/mirrors/Flutter

- 本机Flutter版本：1.14.6
- FlutterBoost版本：1.12.13

# 目录结构

```
flutter
   |--FlutterAndroid //Native工程
         |--app/src/main/java/com/example/flutterandroid //多级目录折叠
               |--base
                    |--BaseApplication //Flutter初始化类
               |--channel //Flutter与Native之间消息传递模块
               |--router //Flutter与Native之间路由跳转工具模块
               |--MainActivity //测试Native跳转到Flutter
               |--TestActivity //测试Flutter跳转到Native
   |--flutter_module //Flutter模块
        |--lib
             |--constants //常量模块
             |--page //页面
             |--utils //工具模块
             |--main.dart //flutter_module入口文件
        |--pubspec.yaml //flutter_module的配置文件
```

# 从0到1的步骤

IDE：AndroidStudio

0. 环境变量配置：参考[Flutter中文网教程](https://flutterchina.club/get-started/install/)

1. 创建flutter_module：File -> New -> New Flutter Project... -> Flutter Module

2. 创建FlutterAndroid：File -> New -> New Project...

3. FlutterAndroid依赖flutter_module

3.1. 编辑settings.gradle文件，依赖flutter_module

`flutterRoot`在`gradle.properties`文件中配置，配置内容如下：

```properties
# flutter项目根目录名称
flutterRoot=flutter_module
```

接着在settings.gradle文件中增加如下配置：

```groovy
//省略其他内容
Properties properties = new Properties()
properties.load(new File(rootProject.getProjectDir(), 'local.properties').newDataInputStream())
boolean isFlutterMode = Boolean.valueOf(properties.getProperty('FlutterMode'))

if (isFlutterMode) {
    setBinding(new Binding([gradle: this]))
    evaluate(new File(
            settingsDir.parent,
            "${flutterRoot}/.android/include_flutter.groovy"
    ))

    include ":${flutterRoot}"
    project(":${flutterRoot}").projectDir = new File("../${flutterRoot}")
}
```

3.2. 编辑FlutterAndroid/app/build.gradle文件，依赖flutter和flutter_boost

```groovy
dependencies {
    //省略其他内容
    //flutter依赖，flutter调试模式使用本地依赖，否则远程依赖
    if (project.hasProperty('isFlutterMode') && project.getProperty('isFlutterMode')) {
        implementation project(':flutter')
        implementation project(':flutter_boost')
    } else {
//        implementation 'com.example.flutter:flutter_module:1.0.0'
    }
}
```

3.3. 在AndroidManifest.xml中注册FlutterActivity和BoostFlutterActivity

```xml
<application>
    <!-- 省略其他内容 -->
    <activity
        android:name="com.idlefish.flutterboost.containers.BoostFlutterActivity"
        android:configChanges="orientation|keyboardHidden|keyboard|screenSize|locale|layoutDirection|fontScale|screenLayout|density"
        android:hardwareAccelerated="true"
        android:theme="@style/Theme.AppCompat"
        android:windowSoftInputMode="adjustResize" />
</application>
```

4. 初始化FlutterBoost

FlutterAndroid中的初始化：具体细节在`BaseApplication.java`中
flutter_module中的初始化：具体细节在`main.dart`中

# 开启和关闭Flutter

- 开启Flutter：在`local.properties`文件中添加`FlutterMode=true`
- 关闭Flutter：在`local.properties`文件中添加`FlutterMode=false`

# 断点调试

1. 在flutter_module工程中点击`Flutter Attach`，此时Console中会有类似的以下消息：

```
Waiting for a connection from Flutter on DLI AL10...
```

2. 在FlutterAndroid工程中启动应用，此时如果Console中有类似的以下消息，表示与设备连接成功：

```
Waiting for a connection from Flutter on DLI AL10...
Debug service listening on ws://127.0.0.1:55862/15dVuHBz2Xw=/ws
Syncing files to device DLI AL10...
```

3. 和Android一样的打断点方式