package com.example.flutterandroid.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.idlefish.flutterboost.containers.BoostFlutterActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RouteUtil {

    public static boolean openFlutterPage(Context context, String url) {
        return openFlutterPage(context, url, new HashMap<>());
    }

    public static boolean openFlutterPage(Context context, String url, @NonNull Map params) {
        return openFlutterPage(context, url, params, 0);
    }

    public static boolean openFlutterPage(Context context, String url, @NonNull Map params, int requestCode) {
        try {
            Intent intent = BoostFlutterActivity.withNewEngine().url(url).params(params)
                    .backgroundMode(BoostFlutterActivity.BackgroundMode.opaque).build(context);
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                activity.startActivityForResult(intent, requestCode);
            } else {
                context.startActivity(intent);
            }
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    public static boolean openNativePage(Context context, String url) {
        return openNativePage(context, url, 0);
    }

    public static boolean openNativePage(Context context, String url, int requestCode) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent();
            initParams(intent, url);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                activity.startActivityForResult(intent, requestCode);
            } else {
                context.startActivity(intent);
            }
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private static void initParams(Intent intent, String url) {
        Uri uri = Uri.parse(url);
        Set<String> params = uri.getQueryParameterNames();
        if (params == null) {
            return;
        }
        String v;
        for (String name : params) {
            v = uri.getQueryParameter(name);
            if ("true".equals(v)) {
                intent.putExtra(name, true);
            } else if ("false".equals(v)) {
                intent.putExtra(name, false);
            } else {
                try {
                    int dv = Integer.parseInt(v);
                    intent.putExtra(name, dv);
                } catch (Throwable e) {
                    intent.putExtra(name, v);
                }
            }
        }
    }

}
