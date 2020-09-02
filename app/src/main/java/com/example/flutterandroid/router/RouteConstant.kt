package com.example.flutterandroid.router;

public class RouteConstant {

    /**
     * Flutter页面
     */
    //测试界面
    public static String flutterTest = "flutter://test";

    /**
     * Native页面
     */
    private static String nativeScheme = "flutterandroid://";
    //测试界面
    public static String nativeTest = nativeScheme + "app/test";

    public interface Test {
        String EXTRA_CONTENT = "content";
    }

}
