package com.plntpls.appssxdc.presentation.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.plntpls.appssxdc.R
import com.plntpls.appssxdc.core.theme.BorderHair
import com.plntpls.appssxdc.core.theme.OnPrimary
import com.plntpls.appssxdc.core.theme.Primary
import com.plntpls.appssxdc.core.theme.SurfaceTint
import com.plntpls.appssxdc.core.theme.TextMuted
import com.plntpls.appssxdc.core.theme.TextSecondary
import com.plntpls.appssxdc.core.ui.PanelScaffold
import com.plntpls.appssxdc.core.ui.SecondaryButton
import com.plntpls.appssxdc.domain.model.PourUnit
import com.plntpls.appssxdc.domain.model.ShelfSettings
import com.plntpls.appssxdc.domain.model.WeekStart

@Composable
fun SettingsPanel(
    visible: Boolean,
    settings: ShelfSettings,
    onPourUnit: (PourUnit) -> Unit,
    onWeekStart: (WeekStart) -> Unit,
    onIntervalNudge: (Int) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    PanelScaffold(
        visible = visible,
        title = stringResource(R.string.settings_title),
        onDismiss = onDismiss,
        fillFraction = 0.66f
    ) {
        SettingRow(label = stringResource(R.string.settings_units)) {
            SegmentOption(
                text = stringResource(R.string.settings_unit_ml),
                selected = settings.pourUnit == PourUnit.ML,
                onClick = { onPourUnit(PourUnit.ML) },
                modifier = Modifier.weight(1f)
            )
            SegmentOption(
                text = stringResource(R.string.settings_unit_cups),
                selected = settings.pourUnit == PourUnit.CUPS,
                onClick = { onPourUnit(PourUnit.CUPS) },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        SettingRow(label = stringResource(R.string.settings_week)) {
            SegmentOption(
                text = stringResource(R.string.settings_week_mon),
                selected = settings.weekStart == WeekStart.MON,
                onClick = { onWeekStart(WeekStart.MON) },
                modifier = Modifier.weight(1f)
            )
            SegmentOption(
                text = stringResource(R.string.settings_week_sun),
                selected = settings.weekStart == WeekStart.SUN,
                onClick = { onWeekStart(WeekStart.SUN) },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        SettingRow(label = stringResource(R.string.settings_nudge)) {
            SegmentOption(
                text = stringResource(R.string.settings_nudge_minus),
                selected = settings.intervalNudge == -1,
                onClick = { onIntervalNudge(-1) },
                modifier = Modifier.weight(1f)
            )
            SegmentOption(
                text = stringResource(R.string.settings_nudge_zero),
                selected = settings.intervalNudge == 0,
                onClick = { onIntervalNudge(0) },
                modifier = Modifier.weight(1f)
            )
            SegmentOption(
                text = stringResource(R.string.settings_nudge_plus),
                selected = settings.intervalNudge == 1,
                onClick = { onIntervalNudge(1) },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(22.dp))
        SecondaryButton(
            text = stringResource(R.string.cta_reset_shelf),
            onClick = onReset
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = stringResource(R.string.settings_footer),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SettingRow(
    label: String,
    content: @Composable RowScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = content
        )
    }
}

@Composable
private fun SegmentOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val description = if (selected) {
        stringResource(R.string.sd_selected)
    } else {
        stringResource(R.string.sd_not_selected)
    }
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .height(48.dp)
            .background(color = if (selected) Primary else SurfaceTint, shape = shape)
            .border(
                width = 1.dp,
                color = if (selected) Primary else BorderHair,
                shape = shape
            )
            .clickable(onClick = onClick)
            .semantics { stateDescription = description },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) OnPrimary else TextSecondary
        )
    }
}
