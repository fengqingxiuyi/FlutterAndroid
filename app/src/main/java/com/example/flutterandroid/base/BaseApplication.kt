package com.example.flutterandroid.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.flutterandroid.BuildConfig;
import com.example.flutterandroid.channel.FlutterMethodChannel;
import com.example.flutterandroid.router.RouteUtil;
import com.idlefish.flutterboost.FlutterBoost;
import com.idlefish.flutterboost.Platform;
import com.idlefish.flutterboost.Utils;
import com.idlefish.flutterboost.interfaces.INativeRouter;

import java.util.Map;

import io.flutter.embedding.android.FlutterView;

/**
 * @author fqxyi
 * @date 2020/9/2
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initFlutterBoost();
    }

    private void initFlutterBoost() {
        //在Flutter页面中打开Native页面
        INativeRouter router = new INativeRouter() {
            @Override
            public void openContainer(Context context, String url, Map<String, Object> urlParams, int requestCode, Map<String, Object> exts) {
                String assembleUrl = Utils.assembleUrl(url, urlParams);
                Log.i("assembleUrl", assembleUrl);
                RouteUtil.openNativePage(context, assembleUrl);
            }
        };
        //FlutterBoost生命周期回调
        FlutterBoost.BoostLifecycleListener boostLifecycleListener = new FlutterBoost.BoostLifecycleListener() {
            @Override
            public void beforeCreateEngine() {}

            @Override
            public void onEngineCreated() {
                new FlutterMethodChannel(BaseApplication.this).registerChannel();
            }

            @Override
            public void onPluginsRegistered() {}

            @Override
            public void onEngineDestroy() {}
        };
        //FlutterBoost初始化
        Platform platform = new FlutterBoost
                .ConfigBuilder(this, router)
                .isDebug(BuildConfig.DEBUG)
                .whenEngineStart(FlutterBoost.ConfigBuilder.ANY_ACTIVITY_CREATED)
                .renderMode(FlutterView.RenderMode.texture)
                .lifecycleListener(boostLifecycleListener)
                .build();
        FlutterBoost.instance().init(platform);
    }
}
