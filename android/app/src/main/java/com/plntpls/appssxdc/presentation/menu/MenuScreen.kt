package com.plntpls.appssxdc.presentation.menu

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plntpls.appssxdc.R
import com.plntpls.appssxdc.core.di.AppViewModelFactory
import com.plntpls.appssxdc.core.theme.Alert
import com.plntpls.appssxdc.core.theme.BgBase
import com.plntpls.appssxdc.core.theme.BgSunk
import com.plntpls.appssxdc.core.theme.BorderHair
import com.plntpls.appssxdc.core.theme.Primary
import com.plntpls.appssxdc.core.theme.Secondary
import com.plntpls.appssxdc.core.theme.TextMuted
import com.plntpls.appssxdc.core.theme.TextPrimary
import com.plntpls.appssxdc.core.theme.TextSecondary
import com.plntpls.appssxdc.core.ui.BottomActionDock
import com.plntpls.appssxdc.core.ui.DockAction
import com.plntpls.appssxdc.core.ui.EditorialRule
import com.plntpls.appssxdc.core.ui.MoistureRing
import com.plntpls.appssxdc.core.ui.PaperCard
import com.plntpls.appssxdc.core.ui.PlantAvatar
import com.plntpls.appssxdc.core.ui.ScreenBackground
import com.plntpls.appssxdc.core.ui.weekdayName
import com.plntpls.appssxdc.domain.model.Plant
import com.plntpls.appssxdc.domain.model.PlantStatus
import com.plntpls.appssxdc.domain.model.PourUnit
import com.plntpls.appssxdc.domain.model.WeekStart
import com.plntpls.appssxdc.presentation.dialog.ConfirmResetDialog
import com.plntpls.appssxdc.presentation.panels.CareLogPanel
import com.plntpls.appssxdc.presentation.panels.PlantProfilePanel
import com.plntpls.appssxdc.presentation.panels.SchedulePanel
import com.plntpls.appssxdc.presentation.panels.SettingsPanel

@Composable
fun MenuRoute(onStartCare: () -> Unit) {
    val menuViewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        menuViewModel.refresh()
    }

    MenuScreen(
        state = state,
        onStartCare = onStartCare,
        onOpenPanel = menuViewModel::openPanel,
        onOpenPlant = menuViewModel::openPlant,
        onClosePanel = menuViewModel::closePanel,
        onToggleRename = menuViewModel::toggleRename,
        onRenameDraft = menuViewModel::onRenameDraft,
        onCommitRename = menuViewModel::commitRename,
        onNudgeInterval = menuViewModel::nudgeInterval,
        onWaterSelected = menuViewModel::waterSelected,
        onPourUnit = menuViewModel::setPourUnit,
        onWeekStart = menuViewModel::setWeekStart,
        onIntervalNudge = menuViewModel::setIntervalNudge,
        onRequestReset = menuViewModel::requestReset,
        onDismissReset = menuViewModel::dismissReset,
        onConfirmReset = menuViewModel::confirmReset
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    onStartCare: () -> Unit,
    onOpenPanel: (MenuPanel) -> Unit,
    onOpenPlant: (String) -> Unit,
    onClosePanel: () -> Unit,
    onToggleRename: () -> Unit,
    onRenameDraft: (String) -> Unit,
    onCommitRename: () -> Unit,
    onNudgeInterval: (Int) -> Unit,
    onWaterSelected: () -> Unit,
    onPourUnit: (PourUnit) -> Unit,
    onWeekStart: (WeekStart) -> Unit,
    onIntervalNudge: (Int) -> Unit,
    onRequestReset: () -> Unit,
    onDismissReset: () -> Unit,
    onConfirmReset: () -> Unit
) {
    BackHandler(enabled = state.panel != null) {
        onClosePanel()
    }

    val idle = rememberInfiniteTransition(label = "menuIdle")
    val heroBreath by idle.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heroBreath"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        ScreenBackground(
            imageRes = R.drawable.bg_menu,
            wash = Brush.verticalGradient(
                listOf(
                    BgBase.copy(alpha = 0.92f),
                    BgBase.copy(alpha = 0.97f),
                    BgSunk.copy(alpha = 0.99f)
                )
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 22.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(
                        R.string.menu_date,
                        weekdayName(state.weekdayIndex),
                        state.weekNumber
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                IconButton(
                    onClick = { onOpenPanel(MenuPanel.SETTINGS) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = stringResource(R.string.cd_settings),
                        tint = Primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = stringResource(R.string.menu_hero_line1),
                        style = MaterialTheme.typography.displayLarge,
                        color = TextPrimary
                    )
                    Text(
                        text = stringResource(R.string.menu_hero_line2),
                        style = MaterialTheme.typography.displayLarge,
                        color = Primary
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    EditorialRule()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.menu_tagline),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
                Image(
                    painter = painterResource(R.drawable.sprite_watering_can),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = 10.dp)
                        .size(128.dp)
                        .graphicsLayer {
                            rotationZ = -8f
                            scaleX = heroBreath
                            scaleY = heroBreath
                        }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            DueBanner(state = state)

            if (state.readFailed) {
                Spacer(modifier = Modifier.height(10.dp))
                PaperCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = Alert,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.menu_error),
                        style = MaterialTheme.typography.labelSmall,
                        color = Alert
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(items = state.plants, key = { it.id }) { plant ->
                    PlantRow(plant = plant, onClick = { onOpenPlant(plant.id) })
                }
            }

            Text(
                text = stringResource(R.string.menu_hint),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            BottomActionDock(
                primaryText = stringResource(R.string.cta_start_care),
                onPrimary = onStartCare,
                primaryEnabled = state.plants.isNotEmpty(),
                primaryIconRes = R.drawable.ic_play,
                secondary = listOf(
                    DockAction(
                        text = stringResource(R.string.cta_schedule),
                        onClick = { onOpenPanel(MenuPanel.SCHEDULE) }
                    ),
                    DockAction(
                        text = stringResource(R.string.cta_care_log),
                        onClick = { onOpenPanel(MenuPanel.LOG) }
                    )
                )
            )
        }

        PlantProfilePanel(
            visible = state.panel == MenuPanel.PROFILE,
            plant = state.selectedPlant,
            entries = state.selectedEntries,
            today = state.today,
            settings = state.settings,
            renaming = state.renaming,
            renameDraft = state.renameDraft,
            onToggleRename = onToggleRename,
            onRenameChange = onRenameDraft,
            onRenameCommit = onCommitRename,
            onIntervalChange = onNudgeInterval,
            onWater = onWaterSelected,
            onDismiss = onClosePanel
        )
        CareLogPanel(
            visible = state.panel == MenuPanel.LOG,
            entries = state.log,
            today = state.today,
            composeMode = false,
            draft = "",
            plantName = null,
            onDraftChange = {},
            onSaveNote = {},
            onDismiss = onClosePanel
        )
        SchedulePanel(
            visible = state.panel == MenuPanel.SCHEDULE,
            days = state.schedule,
            weekStart = state.settings.weekStart,
            onDismiss = onClosePanel
        )
        SettingsPanel(
            visible = state.panel == MenuPanel.SETTINGS,
            settings = state.settings,
            onPourUnit = onPourUnit,
            onWeekStart = onWeekStart,
            onIntervalNudge = onIntervalNudge,
            onReset = onRequestReset,
            onDismiss = onClosePanel
        )
        ConfirmResetDialog(
            visible = state.showResetDialog,
            onConfirm = onConfirmReset,
            onDismiss = onDismissReset
        )
    }
}

@Composable
private fun DueBanner(state: MenuUiState) {
    val headline = when {
        state.dueCount == 0 -> stringResource(R.string.menu_due_none)
        state.dueCount == 1 -> stringResource(R.string.menu_due_one)
        else -> stringResource(R.string.menu_due_count, state.dueCount)
    }
    val sub = if (state.dueCount == 0) {
        stringResource(R.string.menu_next_round, state.nextDueDays)
    } else {
        stringResource(R.string.menu_next_mist)
    }
    PaperCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
        contentPadding = PaddingValues(horizontal = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MoistureRing(
                progress = state.shelfHealth / 100f,
                diameter = 54.dp,
                thickness = 6.dp
            ) {
                Text(
                    text = stringResource(R.string.percent_value, state.shelfHealth),
                    style = MaterialTheme.typography.labelSmall,
                    color = Primary
                )
            }
            Spacer(modifier = Modifier.size(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = headline,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = sub,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun PlantRow(plant: Plant, onClick: () -> Unit) {
    val dotColor = when (plant.status) {
        PlantStatus.THIRSTY -> Alert
        PlantStatus.DUE -> Secondary
        PlantStatus.HEALTHY -> Primary
    }
    PaperCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .clickable(onClick = onClick),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlantAvatar(
                sprite = plant.sprite,
                description = stringResource(R.string.cd_plant, plant.name, plant.species)
            )
            Spacer(modifier = Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plant.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(R.string.menu_row_subtitle, plant.species, plant.spot),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(color = dotColor, shape = CircleShape)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(R.string.days_short, plant.daysUntilDue),
                style = MaterialTheme.typography.labelSmall,
                color = dotColor
            )
        }
    }
}
