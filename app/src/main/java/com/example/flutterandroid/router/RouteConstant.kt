package com.example.flutterandroid.router

object RouteConstant {
    /**
     * Flutter页面
     */
    const val flutterScheme = "flutter://"
    //测试界面
    const val flutterTest = flutterScheme + "test"
    //ECharts页面
    const val flutterECharts = flutterScheme + "echarts"
    //百度地图Demo页面
    const val flutterBmfMapDemo = flutterScheme + "bmfMapDemo"
    //Map页面
    const val flutterMap = flutterScheme + "map"

    /**
     * Native页面
     */
    const val nativeScheme = "flutterandroid://"

    //测试界面
    const val nativeTest = nativeScheme + "app/test"

    interface Test {
        companion object {
            const val EXTRA_CONTENT = "content"
        }
    }
}