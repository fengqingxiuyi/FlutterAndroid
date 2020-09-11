# FlutterAndroid

学习总结Flutter：这是一个集成Flutter模块的Android工程

- [Flutter官网](https://flutter.dev/)
- [Flutter中文网](https://flutterchina.club/)
- [Flutter插件官网](https://pub.dev/)

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

# 目录结构

```
flutter //本机创建的文件夹
   |--FlutterAndroid //Native工程
         |--aop //aop模块
         |--app //app模块
         |--sophix //Sophix热修复相关资料
         |--build.gradle //工程和所有模块的gradle脚本文件
         |--gradle.properties //工程gradle配置文件
         |--README.md //总结
         |--settings.gradle //根据gradle.properties中的配置引入flutter_module
```

# Flutter与Android混编 从0到1的步骤

1. 下载Flutter源码，可通过镜像源下载，如：`git clone https://gitee.com/mirrors/Flutter`

- 本机Flutter版本：1.14.6
- flutter_boost版本：1.12.13

2. 环境变量配置：参考[Flutter中文网教程](https://flutterchina.club/get-started/install/)

这是我Mac电脑上的配置，可参考，将其添加到`.zshrc`文件中，再通过`source`命令使其生效

```
#ADDED BY ANDROID
export ANDROID_HOME=`Android SDK 路径`
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/platform-tools

#ADDED BY FLUTTER
export PUB_HOSTED_URL=https://pub.flutter-io.cn
export FLUTTER_STORAGE_BASE_URL=https://storage.flutter-io.cn
export PATH=`Flutter下载路径`/bin:$PATH
```

2. 为IDE(如AndroidStudio)添加flutter和dart插件

3. 执行`flutter doctor`命令查看是否需要安装其它依赖项来完成安装

4. 创建flutter_module工程：File -> New -> New Flutter Project... -> Flutter Module

4.1. 目前选择了flutter_boost作为混编的管理方式，所以需要在`pubspec.yaml`文件中增加配置：

```yaml
dependencies:
  #https://pub.dev/packages/flutter_boost
  flutter_boost:
    git:
      url: 'https://github.com/alibaba/flutter_boost.git'
      ref: '1.12.13'
```

4.2. 编辑`main.dart`文件，初始化flutter_boost

```dart
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    //注册路由页面
    FlutterBoost.singleton.registerPageBuilders({
      // 测试页面
      'flutter://test': (pageName, params, _) => TestPage(params), //TestPage是自己写的测试页面
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'flutter_module',
      builder: FlutterBoost.init(postPush: _onRoutePushed),
      home: MainPage(), //MainPage为通过flutter_module启动的首页
    );
  }

  void _onRoutePushed(
      String pageName, String uniqueId, Map params, Route route, Future _) {}
}
```

5. 创建FlutterAndroid工程：File -> New -> New Project...

**需要保证FlutterAndroid工程和flutter_module工程处于同一目录下**

6. 使FlutterAndroid工程依赖flutter_module工程

6.1. 在`gradle.properties`文件中配置`flutterRoot`，配置内容如下：

```properties
# flutter项目根目录名称
flutterRoot=flutter_module
```

6.2. 编辑`settings.gradle`文件，依赖flutter_module工程：

```groovy
//省略其他内容
boolean isFlutterProjectMode = "1" == FlutterProjectMode
if (isFlutterProjectMode) {
    setBinding(new Binding([gradle: this]))
    evaluate(new File(
            settingsDir.parent,
            "${flutterRoot}/.android/include_flutter.groovy"
    ))

    include ":${flutterRoot}"
    project(":${flutterRoot}").projectDir = new File("../${flutterRoot}")
}
```

6.3. 使`app模块`依赖flutter和flutter_boost

编辑`工程的build.gradle`文件，获取`isFlutterProjectMode`属性值：

```groovy
allprojects {
    //省略其他内容
    parseLocalProperties()
}

def parseLocalProperties() {
    ext.isFlutterProjectMode = "1" == FlutterProjectMode
}
```

编辑`app模块的build.gradle`文件：

```groovy
dependencies {
    //省略其他内容
    //flutter依赖，flutter调试模式使用本地依赖，否则远程依赖
    if (isFlutterProjectMode) {
        implementation project(':flutter')
        implementation project(':flutter_boost')
    } else {
        implementation 'com.example.flutter:flutter_module:1.0.0'
    }
}
```

6.4. 在AndroidManifest.xml中注册BoostFlutterActivity

```xml
<application>
    <!-- 省略其他内容 -->
    <activity
        android:name="com.idlefish.flutterboost.containers.BoostFlutterActivity"
        android:configChanges="orientation|keyboardHidden|keyboard|screenSize|locale|layoutDirection|fontScale|screenLayout|density"
        android:hardwareAccelerated="true"
        android:theme="@style/Theme.AppCompat"
        android:windowSoftInputMode="adjustResize">
        <meta-data
            android:name="io.flutter.embedding.android.SplashScreenDrawable"
            android:resource="@mipmap/ic_launcher_round" />
    </activity>
</application>
```

7. 在Android工程中初始化flutter_boost

```kotlin
private fun initFlutterBoost() {
    //在Flutter页面中打开Flutter或Native页面
    val router = INativeRouter { context, url, urlParams, requestCode, exts ->
        Log.i("INativeRouter", "$url , $urlParams")
        RouteUtil.openPage(context, url, urlParams, requestCode)
    }
    //FlutterBoost生命周期回调
    val boostLifecycleListener = object : BoostLifecycleListener {
        override fun beforeCreateEngine() {}
        override fun onEngineCreated() {
            FlutterMethodChannel(this@BaseApplication).registerChannel()
        }
        override fun onPluginsRegistered() {}
        override fun onEngineDestroy() {}
    }
    //FlutterBoost初始化
    val platform = FlutterBoost.ConfigBuilder(this, router)
        .isDebug(BuildConfig.DEBUG)
        .whenEngineStart(FlutterBoost.ConfigBuilder.ANY_ACTIVITY_CREATED)
        .renderMode(FlutterView.RenderMode.texture)
        .lifecycleListener(boostLifecycleListener)
        .build()
    FlutterBoost.instance().init(platform)
}
```

# 依赖Flutter产物或依赖Flutter工程

- 依赖Flutter工程：在`gradle.properties`文件中添加`FlutterProjectMode=1`
- 依赖Flutter产物：在`gradle.properties`文件中添加`FlutterProjectMode=0`

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

# 构建产物

运行flutter_module工程下的`build_android.sh`文件即可，命令：/bin/bash `build_android.sh的路径`

## 构建配置

配置文件：flutter_module/configs/gradle.properties

配置开关：
```properties
#0=>上传产物到远程仓库 1=>上传产物到本地仓库
CLOUND_MAVEN=1
#0=>构建Release产物 1=>构建snapshot产物
SNAPSHOT=1

#版本名称
VERSION_NAME=1.0.0
```