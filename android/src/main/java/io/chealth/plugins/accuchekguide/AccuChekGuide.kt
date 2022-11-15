package io.chealth.plugins.accuchekguide

import io.chealth.plugins.accuchekguide.util.LogUtil

class AccuChekGuide {
    fun echo(value: String): String {
        LogUtil.d("Echo", "value")
        return value
    }
}