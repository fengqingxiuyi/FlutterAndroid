# app

app模块

# 目录结构

```
app
  |--src
       |--main/java/com/example/flutterandroid //多级目录折叠
            |--base
                 |--BaseApplication //Flutter初始化类
            |--channel //Flutter与Native之间消息传递模块
            |--hotfix //Flutter热修复实现模块
            |--router //Flutter与Native之间路由跳转工具模块
            |--MainActivity //测试Native跳转到Flutter、获取Flutter页面回调的消息等功能
            |--TestActivity //测试Flutter跳转到Native、接收Flutter传递给Native的数据等功能
  |--aliyun-emas-services.json //EMAS配置文件，包含Sophix
  |--build.gradle //aop模块的gradle脚本文件
  |--flutterandroid.jks //debug包签名文件
  |--flutterandroid.keystore //release包签名文件
  |--README.md //总结
```

# 证书

## 常用命令

`-genkey`生成密钥，而`-genkeypair`生成密钥对（公钥和私钥）。

```
keytool -genkeypair [OPTION]...

生成密钥对

选项:

 -alias <alias>                  要处理的条目的别名
 -keyalg <keyalg>                密钥算法名称
 -keysize <keysize>              密钥位大小
 -sigalg <sigalg>                签名算法名称
 -destalias <destalias>          目标别名
 -dname <dname>                  唯一判别名
 -startdate <startdate>          证书有效期开始日期/时间
 -ext <value>                    X.509 扩展
 -validity <valDays>             有效天数
 -keypass <arg>                  密钥口令
 -keystore <keystore>            密钥库名称
 -storepass <arg>                密钥库口令
 -storetype <storetype>          密钥库类型
 -providername <providername>    提供方名称
 -providerclass <providerclass>  提供方类名
 -providerarg <arg>              提供方参数
 -providerpath <pathlist>        提供方类路径
 -v                              详细输出
 -protected                      通过受保护的机制的口令
```

## 生成证书

```
fqxyi@FQXYI-MBP FlutterAndroid % keytool -genkeypair -alias flutterandroid -keyalg RSA -keysize 2048 -keypass 123456 -keystore app/flutterandroid.jks -storepass 123456 -storetype PKCS12
您的名字与姓氏是什么?
  [Unknown]:  fqxyi
您的组织单位名称是什么?
  [Unknown]:  shandian    
您的组织名称是什么?
  [Unknown]:  shikongdiandong
您所在的城市或区域名称是什么?
  [Unknown]:  hangzhou
您所在的省/市/自治区名称是什么?
  [Unknown]:  zhejiang
该单位的双字母国家/地区代码是什么?
  [Unknown]:  CN
CN=fqxyi, OU=shandian, O=shikongdiandong, L=hangzhou, ST=zhejiang, C=CN是否正确?
  [否]:  y
```

# Sophix热修复

1. [开发接入教程](https://help.aliyun.com/document_detail/61082.html?spm=a2c4g.11186623.6.575.383d77e8IxQC80)
2. [EMAS中添加应用](https://emas.console.aliyun.com/?spm=5176.12818093.nav-right.1.488716d0pjkhrH#/productList)
3. [EMAS使用和补丁管理](https://help.aliyun.com/document_detail/51434.html?spm=a2c4g.11186623.6.554.c7c3f2aem5IVlH)
4. [查看EMAS中移动热修复的统计](https://emas.console.aliyun.com/?spm=5176.12818093.nav-right.1.488716d0pjkhrH#/product/3571321/hotfix/31203843/2)

## 下载

1. [Mac生成补丁工具](http://ams-hotfix-repo.oss-cn-shanghai.aliyuncs.com/SophixPatchTool_macos.zip?spm=a2c4g.11186623.2.10.7e9b77e81bSqTn&file=SophixPatchTool_macos.zip)
1. [Android调试补丁工具](http://ams-hotfix-repo.oss-cn-shanghai.aliyuncs.com/hotfix_debug_tool-release.apk?spm=a2c4g.11186623.2.10.592129faRGrxnt&file=hotfix_debug_tool-release.apk)

## EMAS中添加应用步骤

1. 下载`aliyun-emas-services.json`
2. 参考![EMAS配置](sophix/EMAS配置.png)