package com.example.flutterandroid.router

object RouteConstant {
    /**
     * Flutter页面
     */
    const val flutterScheme = "flutter://"
    //测试界面
    var flutterTest = flutterScheme + "test"

    /**
     * Native页面
     */
    const val nativeScheme = "flutterandroid://"

    //测试界面
    var nativeTest = nativeScheme + "app/test"

    interface Test {
        companion object {
            const val EXTRA_CONTENT = "content"
        }
    }
}