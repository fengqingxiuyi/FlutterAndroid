package com.example.flutterandroid.hotfix

import android.text.TextUtils
import android.util.Log
import io.flutter.embedding.engine.loader.FlutterLoader
import java.io.File

/**
 * @author fqxyi
 * @date 2020/9/4
 * Flutter热修复实现类
 */
object FlutterPatch {

    private const val TAG = "FlutterPatch"

    fun init(path: String?) {
        reflect(hookSophix(path))
    }

    private fun hookSophix(path: String?): String {
        var libPathFromSophix = ""
        if (TextUtils.isEmpty(path)) return libPathFromSophix
        val file = File("$path/libs/libapp.so")
        if (file.exists() && !file.isDirectory) {
            libPathFromSophix = file.absolutePath
            Log.i(TAG, "path is $libPathFromSophix")
        } else {
            Log.i(TAG, "path file is not exist")
        }
        return libPathFromSophix
    }

    private fun reflect(libPath: String?) {
        if (TextUtils.isEmpty(libPath)) return
        try {
            val flutterLoader = FlutterLoader.getInstance()
            val field = FlutterLoader::class.java.getDeclaredField("aotSharedLibraryName")
            field.isAccessible = true
            field[flutterLoader] = libPath
            Log.i(TAG, "flutter patch is loaded successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "flutter patch is loaded exception", e)
        }
    }
}