package com.plntpls.appssxdc.core.ui

import com.plntpls.appssxdc.core.config.GameConfig

fun cupsLabel(pourMl: Int): String {
    val tenths = (pourMl * 10 + GameConfig.ML_PER_CUP / 2) / GameConfig.ML_PER_CUP
    val whole = tenths / 10
    val fraction = tenths % 10
    return if (fraction == 0) whole.toString() else whole.toString() + "." + fraction.toString()
}

fun isSingleCup(pourMl: Int): Boolean = cupsLabel(pourMl) == "1"
