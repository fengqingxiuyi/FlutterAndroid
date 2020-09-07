package com.example.flutterandroid.base

import android.util.Log
import com.baidu.flutter_bmfbase.BmfMapApplication
import com.example.aop.AopUtil
import com.example.flutterandroid.BuildConfig
import com.example.flutterandroid.channel.FlutterMethodChannel
import com.example.flutterandroid.hotfix.FlutterPatch
import com.example.flutterandroid.router.RouteUtil
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoost.BoostLifecycleListener
import com.idlefish.flutterboost.interfaces.INativeRouter
import io.flutter.embedding.android.FlutterView

/**
 * @author fqxyi
 * @date 2020/9/2
 */
class BaseApplication : BmfMapApplication() {

    override fun onCreate() {
        super.onCreate()
        initAop()
        initFlutterBoost()
    }

    private fun initAop() {
        AopUtil.instance.setAopListener(object : AopUtil.AopListener {
            override fun getLibAppSoPath(path: String?) {
                FlutterPatch.init(path)
            }
        })
    }

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
}