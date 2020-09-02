package com.example.flutterandroid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flutterandroid.router.RouteConstant
import com.idlefish.flutterboost.interfaces.IFlutterViewContainer
import kotlinx.android.synthetic.main.activity_test.*

/**
 * @author fqxyi
 * @date 2020/9/2
 */
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        dealIntent()
    }

    private fun dealIntent() {
        val intent = intent
        val content = intent.getStringExtra(RouteConstant.Test.EXTRA_CONTENT)
        contentView.text = content
    }

    fun callbackMessage(view: View) {
        val map = HashMap<String, String>()
        map["content"] = "Content From TestActivity"
        val intent = Intent()
        intent.putExtra(IFlutterViewContainer.RESULT_KEY, map)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}