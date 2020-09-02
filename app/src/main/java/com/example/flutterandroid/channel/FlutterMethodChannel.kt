package com.example.flutterandroid.channel

import android.content.Context
import android.widget.Toast
import com.idlefish.flutterboost.FlutterBoost
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

/**
 * MethodChannel：用于传递方法调用，一次性通信，比如Flutter调用Native获取系统电量，发起Toast调用。
 */
class FlutterMethodChannel(private val context: Context) : MethodCallHandler {

    companion object {
        //channel的名称，由于app中可能会有多个channel，这个名称需要在app内是唯一的。
        private const val CHANNEL_TOAST = "channel_flutter_toast"
    }

    fun registerChannel() {
        // 直接 new MethodChannel，然后设置一个Callback来处理Flutter端调用
        MethodChannel(
            FlutterBoost.instance().engineProvider().dartExecutor,
            CHANNEL_TOAST
        ).setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "showToast" -> {
                val content = call.argument<String>("content")
                Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
            }
            else -> result.notImplemented()
        }
    }

}