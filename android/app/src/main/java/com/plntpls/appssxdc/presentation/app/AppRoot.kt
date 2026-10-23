package com.plntpls.appssxdc.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plntpls.appssxdc.core.di.AppViewModelFactory
import com.plntpls.appssxdc.core.navigation.Screen
import com.plntpls.appssxdc.core.theme.BgBase
import com.plntpls.appssxdc.presentation.game.GameRoute
import com.plntpls.appssxdc.presentation.gameover.GameOverRoute
import com.plntpls.appssxdc.presentation.menu.MenuRoute
import com.plntpls.appssxdc.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) {
        appViewModel.back()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgBase)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                when {
                    initialState is Screen.Menu && targetState is Screen.Game ->
                        slideInHorizontally(initialOffsetX = { it / 3 }) + fadeIn(animationSpec = tween(300)) togetherWith
                            slideOutHorizontally(targetOffsetX = { -it / 6 }) + fadeOut(animationSpec = tween(240))

                    targetState is Screen.GameOver ->
                        fadeIn(animationSpec = tween(380)) + scaleIn(initialScale = 0.96f) togetherWith
                            fadeOut(animationSpec = tween(260))

                    else ->
                        fadeIn(animationSpec = tween(420)) togetherWith fadeOut(animationSpec = tween(320))
                }
            },
            label = "screen"
        ) { target ->
            when (target) {
                Screen.Splash -> SplashScreen(onFinished = appViewModel::showMenu)

                Screen.Menu -> MenuRoute(onStartCare = appViewModel::startCare)

                is Screen.Game -> GameRoute(
                    sessionId = target.sessionId,
                    onBack = appViewModel::back,
                    onRoundFinished = appViewModel::finishRound
                )

                is Screen.GameOver -> GameOverRoute(
                    summary = target.summary,
                    onCareAgain = appViewModel::careAgain,
                    onMenu = appViewModel::showMenu
                )
            }
        }
    }
}
