package com.example.flutterandroid.channel;

import android.content.Context;
import android.widget.Toast;

import com.idlefish.flutterboost.FlutterBoost;

import org.jetbrains.annotations.NotNull;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * MethodChannel：用于传递方法调用，一次性通信，比如Flutter调用Native获取系统电量，发起Toast调用。
 */
public class FlutterMethodChannel implements MethodChannel.MethodCallHandler {

    private Context context;

    //channel的名称，由于app中可能会有多个channel，这个名称需要在app内是唯一的。
    private static final String CHANNEL_TOAST = "channel_flutter_toast";

    public FlutterMethodChannel(Context context) {
        this.context = context;
    }

    public void registerChannel() {
        // 直接 new MethodChannel，然后设置一个Callback来处理Flutter端调用
        new MethodChannel(FlutterBoost.instance().engineProvider().getDartExecutor(), CHANNEL_TOAST).setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NotNull MethodCall call, @NotNull MethodChannel.Result result) {
        switch (call.method) {
            case "showToast": //调用原生的toast
                String content = call.argument("content");
                Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
                break;
            default:
                result.notImplemented();
        }
    }
}
