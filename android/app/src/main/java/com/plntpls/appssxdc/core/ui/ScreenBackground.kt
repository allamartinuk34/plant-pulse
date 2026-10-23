package com.plntpls.appssxdc.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun ScreenBackground(
    imageRes: Int,
    wash: Brush,
    modifier: Modifier = Modifier,
    vignette: Brush? = null
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(wash)
        )
        if (vignette != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(vignette)
            )
        }
    }
}
