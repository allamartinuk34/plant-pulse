package com.plntpls.appssxdc.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.plntpls.appssxdc.R
import com.plntpls.appssxdc.domain.model.PourUnit

@Composable
fun pourText(pourMl: Int, unit: PourUnit): String = when {
    unit == PourUnit.ML -> stringResource(R.string.pour_ml, pourMl)
    isSingleCup(pourMl) -> stringResource(R.string.pour_cup_one)
    else -> stringResource(R.string.pour_cups, cupsLabel(pourMl))
}
