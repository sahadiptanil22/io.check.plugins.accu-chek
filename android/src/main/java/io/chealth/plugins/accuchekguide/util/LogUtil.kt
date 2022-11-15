package io.chealth.plugins.accuchekguide.util

/**
 * This is done to evade the unit test exception: "java.lang.RuntimeException: Method d in android.util.Log not mocked."
 */
object LogUtil {
    fun d(tag: String, msg: String): Int {
        println("DEBUG: $tag: $msg")
        return 0
    }
}