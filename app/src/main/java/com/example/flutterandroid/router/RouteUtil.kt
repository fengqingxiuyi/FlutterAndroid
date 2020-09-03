package com.example.flutterandroid.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import java.io.Serializable
import java.util.*

object RouteUtil {

    /**
     * 打开页面
     */
    fun openPage(
        context: Context,
        url: String,
        params: Map<String, Any> = HashMap<String, Any>(),
        requestCode: Int = 0
    ): Boolean {
        return when {
            url.startsWith(RouteConstant.flutterScheme) -> {
                openFlutterPage(context, url, params, requestCode)
                true
            }
            url.startsWith(RouteConstant.nativeScheme) -> {
                openNativePage(context, url, params, requestCode)
                true
            }
            else -> {
                Log.e("RouteUtil", "${url}页面不存在")
                false
            }
        }
    }

    /**
     * 打开Flutter页面
     */
    private fun openFlutterPage(
        context: Context,
        url: String,
        params: Map<String, Any> = HashMap<String, Any>(),
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
    private fun openNativePage(
        context: Context,
        url: String,
        params: Map<String, Any> = HashMap<String, Any>(),
        requestCode: Int = 0
    ): Boolean {
        return try {
            val uri = Uri.parse(url)
            val intent = Intent()
            params.entries.forEach { out ->
                if (out.value is Map<*, *>) {
                    (out.value as Map<*, *>).entries.forEach { inner ->
                        if (inner.key != null && inner.value != null) {
                            putExtra(intent, inner.key.toString(), inner.value!!)
                        }
                    }
                }
            }
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

    private fun putExtra(intent: Intent, key: String, value: Any) {
        when (value) {
            is Boolean -> {
                intent.putExtra(key, value)
            }
            is Byte -> {
                intent.putExtra(key, value)
            }
            is Char -> {
                intent.putExtra(key, value)
            }
            is Short -> {
                intent.putExtra(key, value)
            }
            is Int -> {
                intent.putExtra(key, value)
            }
            is Long -> {
                intent.putExtra(key, value)
            }
            is Float -> {
                intent.putExtra(key, value)
            }
            is Double -> {
                intent.putExtra(key, value)
            }
            is String -> {
                intent.putExtra(key, value)
            }
            is CharSequence -> {
                intent.putExtra(key, value)
            }
            is Parcelable -> {
                intent.putExtra(key, value)
            }
            is Serializable -> {
                intent.putExtra(key, value)
            }
            is Bundle -> {
                intent.putExtra(key, value)
            }
        }
    }
}
