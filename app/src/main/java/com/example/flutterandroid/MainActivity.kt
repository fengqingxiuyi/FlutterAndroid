package com.example.flutterandroid

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aop.annotation.CrashSafe
import com.example.flutterandroid.router.RouteConstant
import com.example.flutterandroid.router.RouteUtil
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostPlugin
import com.idlefish.flutterboost.interfaces.IFlutterViewContainer

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CODE_SUCCESS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFlutterBoostEventBus()
    }

    private fun initFlutterBoostEventBus() {
        //监听Flutter向Native发送的消息
        FlutterBoost.instance().channel()
            .addEventListener("flutterEventBus", FlutterBoostPlugin.EventListener { name, args ->
                Toast.makeText(
                    this@MainActivity,
                    "在MainActivity中监听到的消息：$name, $args",
                    Toast.LENGTH_SHORT
                ).show()
            })
    }

    fun sendMessage(view: View) {
        //Native向Flutter发送消息
        val map = hashMapOf<String, String>()
        map["params"] = "Message From MainActivity"
        FlutterBoost.instance().channel().sendEvent("nativeEventBus", map)
    }

    fun openTest(view: View) {
        val paramsMap = HashMap<String, String>()
        paramsMap["content"] = "Content From MainActivity"
        RouteUtil.openPage(this, RouteConstant.flutterTest, paramsMap, CODE_SUCCESS)
    }

    @CrashSafe
    fun testAOP(view: View) {
        val arr = arrayListOf<Int>()
        print(arr[1])
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_SUCCESS) {
            val map = data?.getSerializableExtra(IFlutterViewContainer.RESULT_KEY) as HashMap<String, Object>?
            Toast.makeText(
                this@MainActivity,
                "在MainActivity中监听到的消息：${map?.get("result")}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}