package com.example.flutterandroid.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import java.util.*

object RouteUtil {

    /**
     * 打开Flutter页面
     */
    fun openFlutterPage(
        context: Context,
        url: String,
        params: Map<*, *> = HashMap<Any, Any>(),
        requestCode: Int = 0
    ): Boolean {
        return try {
            val intent = BoostFlutterActivity
                .withNewEngine()
                .url(url)
                .params(params)
                .backgroundMode(BoostFlutterActivity.BackgroundMode.opaque)
                .build(context)
            if (context is Activity) {
                context.startActivityForResult(intent, requestCode)
            } else {
                context.startActivity(intent)
            }
            true
        } catch (t: Throwable) {
            false
        }
    }

    /**
     * 打开Native页面
     */
    fun openNativePage(context: Context, url: String, requestCode: Int = 0): Boolean {
        return try {
            val uri = Uri.parse(url)
            val intent = Intent()
            initParams(intent, url)
            intent.action = Intent.ACTION_VIEW
            intent.data = uri
            if (context is Activity) {
                context.startActivityForResult(intent, requestCode)
            } else {
                context.startActivity(intent)
            }
            true
        } catch (t: Throwable) {
            false
        }
    }

    /**
     * 解析url中携带的参数
     */
    private fun initParams(intent: Intent, url: String) {
        val uri = Uri.parse(url)
        val params = uri.queryParameterNames ?: return
        var v: String?
        for (name in params) {
            v = uri.getQueryParameter(name)
            if (v == null) {
                continue
            }
            when (v) {
                "true" -> {
                    intent.putExtra(name, true)
                }
                "false" -> {
                    intent.putExtra(name, false)
                }
                else -> {
                    try {
                        val dv = v.toInt()
                        intent.putExtra(name, dv)
                    } catch (e: Throwable) {
                        intent.putExtra(name, v)
                    }
                }
            }
        }
    }
}