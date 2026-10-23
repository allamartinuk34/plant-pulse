package com.plntpls.appssxdc.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.plntpls.appssxdc.R

@Composable
fun weekdayName(index: Int): String = when (((index % 7) + 7) % 7) {
    0 -> stringResource(R.string.day_mon)
    1 -> stringResource(R.string.day_tue)
    2 -> stringResource(R.string.day_wed)
    3 -> stringResource(R.string.day_thu)
    4 -> stringResource(R.string.day_fri)
    5 -> stringResource(R.string.day_sat)
    else -> stringResource(R.string.day_sun)
}
