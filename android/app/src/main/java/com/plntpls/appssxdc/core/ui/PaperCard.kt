package com.plntpls.appssxdc.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.plntpls.appssxdc.core.theme.BorderHair
import com.plntpls.appssxdc.core.theme.Secondary
import com.plntpls.appssxdc.core.theme.SurfacePaper

@Composable
fun PaperCard(
    modifier: Modifier = Modifier,
    corner: Dp = 20.dp,
    borderColor: Color = BorderHair,
    container: Color = SurfacePaper.copy(alpha = 0.92f),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(corner)
    Column(
        modifier = modifier
            .shadow(elevation = 2.dp, shape = shape, clip = false)
            .background(color = container, shape = shape)
            .border(width = 1.dp, color = borderColor, shape = shape)
            .padding(contentPadding),
        content = content
    )
}

@Composable
fun EditorialRule(
    modifier: Modifier = Modifier,
    width: Dp = 56.dp,
    color: Color = Secondary
) {
    Box(
        modifier = modifier
            .width(width)
            .height(2.dp)
            .background(color = color, shape = RoundedCornerShape(1.dp))
    )
}
