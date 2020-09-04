package com.example.aop

/**
 * @author fqxyi
 * @date 2020/9/4
 */
class AopUtil private constructor() {

    companion object {
        val instance: AopUtil
            get() = AopUtilHolder.instance
    }

    private object AopUtilHolder {
        var instance = AopUtil()
    }

    private var path: String? = null

    /**
     * 因为Sophix的执行速度很快，所以此处的listener实际上是null，因此把path记录了下来
     */
    fun setLibAppSoPath(path: String?) {
        this.path = path
        listener?.getLibAppSoPath(path)
    }

    interface AopListener {
        fun getLibAppSoPath(path: String?)
    }

    private var listener: AopListener? = null

    /**
     * 因为先执行了setLibAppSoPath函数，再执行此函数，所以这边也去执行了getLibAppSoPath函数
     */
    fun setAopListener(listener: AopListener?) {
        this.listener = listener
        listener?.getLibAppSoPath(path)
    }
}